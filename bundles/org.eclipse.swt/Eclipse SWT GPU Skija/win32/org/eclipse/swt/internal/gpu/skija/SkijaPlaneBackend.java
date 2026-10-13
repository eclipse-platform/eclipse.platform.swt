/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Uses HumbleUI Skija 0.143.18 (Apache-2.0) and Skia m143 (BSD-3-Clause) as a
 * runtime library. The WGL setup follows the JetBrains Skiko (Apache-2.0)
 * Windows OpenGL renderer pattern; no Skiko source is copied.
 * See docs/licenses/swt-gpu-retained-planes-NOTICE.txt.
 *******************************************************************************/
package org.eclipse.swt.internal.gpu.skija;

import org.eclipse.swt.internal.gpu.*;
import org.eclipse.swt.internal.opengl.win32.*;
import org.eclipse.swt.internal.win32.*;

import io.github.humbleui.skija.*;

/**
 * Skija (Skia GL) backend for retained planes on win32.
 * <p>
 * One OpenGL context per display lives on a hidden, disabled, never-shown
 * popup window (WS_POPUP | WS_DISABLED, WS_EX_NOACTIVATE | WS_EX_TOOLWINDOW).
 * It is not a child of any widget, never takes focus and receives no input:
 * the widget remains the viewport and presents its own pixels. Tiles are GPU
 * render targets; frames are composited on the GPU and read back into the
 * widget's retained DIB (fence: {@code readPixels} completes the GPU work).
 * </p>
 * <p>
 * Native objects are created only when a replayed op needs them and are cached
 * by int key: Paint by {@link PaintKeys} key, Font by {@link FontTable} id,
 * TextBlob by (string hash, length, font id) with string equality on hit.
 * Replay calls Skija's primitive native entry points, so steady frames do not
 * allocate Java objects.
 * </p>
 */
public final class SkijaPlaneBackend implements PlaneBackend {
	static final int TILE = TileGrid.SIZE;
	static final int PAINTS = 512, BLOBS = Integer.getInteger ("swt.gpu.blobs", 4096);

	long hostHwnd, hostDC, hglrc;
	DirectContext context;
	FontMgr fontMgr;
	final Font [] fonts = new Font [FontTable.CAPACITY];
	final RetainedSlots paintSlots = new RetainedSlots (PAINTS);
	final Paint [] paints = new Paint [PAINTS];
	final RetainedSlots blobSlots = new RetainedSlots (BLOBS);
	final TextBlob [] blobs = new TextBlob [BLOBS];
	final String [] blobText = new String [BLOBS];
	Paint srcPaint;
	/* ClearType-like LCD text needs the surface pixel geometry; tiles and frames are opaque. */
	SurfaceProps lcdProps;
	long liveTileBytes;
	/* Evictors are created once; native objects close when their slot is reused. */
	final RetainedSlots.Evictor paintEvictor = (slot, key) -> {
		paints [slot].close ();
		paints [slot] = null;
	};
	final RetainedSlots.Evictor blobEvictor = (slot, key) -> {
		if (blobs [slot] != null) blobs [slot].close ();
		blobs [slot] = null;
		blobText [slot] = null;
	};

	@Override
	public boolean init () {
		char [] cls = "STATIC\0".toCharArray ();
		hostHwnd = OS.CreateWindowEx (OS.WS_EX_NOACTIVATE | OS.WS_EX_TOOLWINDOW, cls, null, OS.WS_POPUP | OS.WS_DISABLED, 0, 0, 1, 1, 0, 0, OS.GetModuleHandle (null), null);
		if (hostHwnd == 0) return false;
		hostDC = OS.GetDC (hostHwnd);
		PIXELFORMATDESCRIPTOR pfd = new PIXELFORMATDESCRIPTOR ();
		pfd.nSize = (short) PIXELFORMATDESCRIPTOR.sizeof;
		pfd.nVersion = 1;
		pfd.dwFlags = WGL.PFD_DRAW_TO_WINDOW | WGL.PFD_SUPPORT_OPENGL | WGL.PFD_DOUBLEBUFFER;
		pfd.iPixelType = (byte) WGL.PFD_TYPE_RGBA;
		pfd.cColorBits = 32;
		pfd.cAlphaBits = 8;
		pfd.cStencilBits = 8;
		pfd.iLayerType = (byte) WGL.PFD_MAIN_PLANE;
		int format = WGL.ChoosePixelFormat (hostDC, pfd);
		if (format == 0 || !WGL.SetPixelFormat (hostDC, format, pfd)) return false;
		hglrc = WGL.wglCreateContext (hostDC);
		if (hglrc == 0 || !WGL.wglMakeCurrent (hostDC, hglrc)) return false;
		context = DirectContext.makeGL ();
		fontMgr = FontMgr.getDefault ();
		srcPaint = new Paint ().setBlendMode (BlendMode.SRC);
		lcdProps = new SurfaceProps (false, PixelGeometry.RGB_H);
		return context != null;
	}

	/** Re-binds this display's GL context if application code made another one current. */
	void makeCurrent () {
		if (WGL.wglGetCurrentContext () != hglrc) {
			WGL.wglMakeCurrent (hostDC, hglrc);
			context.resetGLAll ();
		}
	}

	@Override
	public Target createTarget (int tileCapacity) {
		return new SkijaTarget (this, tileCapacity);
	}

	/** Creates the Skija font of id {@code id} from its {@link FontTable} value; called on first replay. */
	void defineFont (int id, String face, int heightPx, int weight, int italic) {
		FontStyle style = new FontStyle (weight, 5, italic != 0 ? FontSlant.ITALIC : FontSlant.UPRIGHT);
		Typeface typeface = fontMgr.matchFamilyStyle (face, style);
		Font font = new Font (typeface, heightPx);
		font.setEdging (FontEdging.SUBPIXEL_ANTI_ALIAS);
		font.setHinting (FontHinting.NORMAL);
		font.setSubpixel (false);
		font.setBaselineSnapped (true);
		fonts [id] = font;
		GpuCounters.inc (GpuCounters.NATIVE_CREATES);
	}

	Font font (int id) {
		Font font = fonts [id];
		if (font == null) {
			FontTable table = FontTable.INSTANCE;
			defineFont (id, table.face [id], table.heightPx [id], table.weight [id], table.italic [id]);
			font = fonts [id];
		}
		return font;
	}

	/** Paint for a packed (mask, ARGB) key; created on first use, evicted least recent first. */
	long paint (int mask, int argb) {
		int slot = paintSlots.acquire (PaintKeys.key (mask, argb), paintEvictor);
		if (slot < 0) return 0;
		if (paintSlots.isNew ()) {
			Paint paint = new Paint ().setColor (argb).setAntiAlias (false);
			int kind = PaintKeys.kind (mask);
			if (kind == PaintKeys.KIND_STROKE) {
				float width = PaintKeys.lineWidth (mask);
				paint.setMode (PaintMode.STROKE).setStrokeWidth (width <= 1f ? 0f : width);
				int cap = PaintKeys.lineCap (mask);
				paint.setStrokeCap (cap == 2 ? PaintStrokeCap.ROUND : cap == 3 ? PaintStrokeCap.SQUARE : PaintStrokeCap.BUTT);
				int join = PaintKeys.lineJoin (mask);
				paint.setStrokeJoin (join == 2 ? PaintStrokeJoin.ROUND : join == 3 ? PaintStrokeJoin.BEVEL : PaintStrokeJoin.MITER);
			} else if (kind == PaintKeys.KIND_TEXT) {
				paint.setAntiAlias (true);
			}
			paints [slot] = paint;
			GpuCounters.inc (GpuCounters.PAINT_MISS);
			GpuCounters.inc (GpuCounters.NATIVE_CREATES);
		} else {
			GpuCounters.inc (GpuCounters.PAINT_HIT);
		}
		return paints [slot]._ptr;
	}

	/** Glyph run of {@code text} in font {@code fontId}; created on miss with Skia advances. */
	long blob (String text, int fontId) {
		long key = ((long) text.hashCode () << 32) | ((long) fontId << 20) | (text.length () & 0xFFFFF);
		int slot = blobSlots.peek (key);
		if (slot >= 0 && text.equals (blobText [slot])) {
			blobSlots.touch (slot);
			GpuCounters.inc (GpuCounters.BLOB_HIT);
			return blobs [slot] != null ? blobs [slot]._ptr : 0;
		}
		if (slot >= 0) {
			blobEvictor.evict (slot, key);
			blobSlots.remove (slot);
		}
		slot = blobSlots.acquire (key, blobEvictor);
		GpuCounters.inc (GpuCounters.BLOB_MISS);
		if (slot < 0) return 0;
		Font font = font (fontId);
		short [] glyphs = font.getStringGlyphs (text);
		TextBlob blob = glyphs.length == 0 ? null : TextBlob.makeFromPosH (glyphs, gdiPositions (text, fontId, glyphs.length), 0, font);
		blobs [slot] = blob;
		blobText [slot] = text;
		GpuCounters.inc (GpuCounters.NATIVE_CREATES);
		return blob != null ? blob._ptr : 0;
	}

	/* Shaped glyph runs (TextLayout): keyed by a hash of font id, glyph ids and advances; hits are verified. */
	final RetainedSlots runSlots = new RetainedSlots (BLOBS);
	final TextBlob [] runBlobs = new TextBlob [BLOBS];
	final int [][] runKeys = new int [BLOBS][];
	final RetainedSlots.Evictor runEvictor = (slot, key) -> {
		if (runBlobs [slot] != null) runBlobs [slot].close ();
		runBlobs [slot] = null;
		runKeys [slot] = null;
	};

	/** Blob for the GLYPH_RUN op at {@code a[o]}: fontId, x, y, argb, count, glyphs[count], advances[count]. */
	long glyphRun (int [] a, int o) {
		int fontId = a [o], count = a [o + 4];
		long hash = fontId * 0x9E3779B97F4A7C15L ^ count;
		for (int i = 0, n = 2 * count; i < n; i++) hash = hash * 31 + a [o + 5 + i];
		int slot = runSlots.peek (hash);
		if (slot >= 0 && sameRun (runKeys [slot], a, o, count)) {
			runSlots.touch (slot);
			GpuCounters.inc (GpuCounters.BLOB_HIT);
			return runBlobs [slot] != null ? runBlobs [slot]._ptr : 0;
		}
		if (slot >= 0) {
			runEvictor.evict (slot, hash);
			runSlots.remove (slot);
		}
		GpuCounters.inc (GpuCounters.BLOB_MISS);
		slot = runSlots.acquire (hash, runEvictor);
		if (slot < 0) return 0;
		short [] glyphs = new short [count];
		float [] xpos = new float [count];
		int [] key = new int [2 + 2 * count];
		key [0] = fontId;
		key [1] = count;
		for (int i = 0, x = 0; i < count; i++) {
			glyphs [i] = (short) a [o + 5 + i];
			xpos [i] = x;
			x += a [o + 5 + count + i];
		}
		System.arraycopy (a, o + 5, key, 2, 2 * count);
		runBlobs [slot] = TextBlob.makeFromPosH (glyphs, xpos, 0, font (fontId));
		runKeys [slot] = key;
		GpuCounters.inc (GpuCounters.NATIVE_CREATES);
		return runBlobs [slot] != null ? runBlobs [slot]._ptr : 0;
	}

	static boolean sameRun (int [] key, int [] a, int o, int count) {
		if (key == null || key [0] != a [o] || key [1] != count) return false;
		for (int i = 0, n = 2 * count; i < n; i++) if (key [2 + i] != a [o + 5 + i]) return false;
		return true;
	}

	/** Glyph x positions from the platform (GDI) advance widths so runs land where ExtTextOut puts them. */
	static float [] gdiPositions (String text, int fontId, int count) {
		FontTable table = FontTable.INSTANCE;
		float [] xpos = new float [count];
		int x = 0;
		for (int i = 0; i < count && i < text.length (); i++) {
			xpos [i] = x;
			x += Math.max (0, table.advance (fontId, text.charAt (i)));
		}
		return xpos;
	}

	@Override
	public String describe () {
		return "Skija 0.143.18 GL " + context + " paints=" + paintSlots.size () + " blobs=" + blobSlots.size () + " tileBytes=" + liveTileBytes;
	}

	@Override
	public void dispose () {
		for (int s = blobSlots.first (); s >= 0; s = blobSlots.next (s)) if (blobs [s] != null) blobs [s].close ();
		for (int s = runSlots.first (); s >= 0; s = runSlots.next (s)) if (runBlobs [s] != null) runBlobs [s].close ();
		for (int s = paintSlots.first (); s >= 0; s = paintSlots.next (s)) paints [s].close ();
		for (Font font : fonts) if (font != null) font.close ();
		if (srcPaint != null) srcPaint.close ();
		if (context != null) {
			context.abandon ();
			context.close ();
		}
		WGL.wglMakeCurrent (0, 0);
		if (hglrc != 0) WGL.wglDeleteContext (hglrc);
		if (hostDC != 0) OS.ReleaseDC (hostHwnd, hostDC);
		if (hostHwnd != 0) OS.DestroyWindow (hostHwnd);
		hglrc = hostDC = hostHwnd = 0;
	}
}

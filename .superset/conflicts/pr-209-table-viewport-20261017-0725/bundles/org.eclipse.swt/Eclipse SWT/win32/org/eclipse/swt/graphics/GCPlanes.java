/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gpu.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Plane gate and GPU op recorder beneath win32 {@link GC}.
 * <p>
 * Every drawing method of GC calls one {@code divert*} method once
 * {@code data.planeDraw != -1} (that is, only inside a retained-plane paint
 * pass). A diverted call returns {@code true} when the GC must not touch its
 * HDC: either the current plane is not executed by this pass, or the call was
 * recorded into the pass's {@link OpStream} for GPU replay. A call the GPU
 * cannot replay marks the stream failed; the frame driver then repaints that
 * plane with GDI. State setters (colour, font, clip) still update the HDC so
 * metric queries made by widget paint code stay exact.
 * </p>
 */
final class GCPlanes {
	/** retainedMask bits tracked by GC state setters while a sink is active. */
	static final int TRANSFORMED = 1 << 8, XOR = 1 << 9;
	static final int TEXT_OPAQUE = 1;

	/* Recorder scratch, reused across GCs: recording runs only in retained passes on the display thread. */
	private static final SIZE SIZE_SCRATCH = new SIZE ();
	private static final RECT RECT_SCRATCH = new RECT ();
	private static final float [] TRANSFORM_SCRATCH = new float [6];
	private static char [] charScratch = new char [256];

	private GCPlanes () {
	}

	private static OpStream sink (GC gc) {
		return (OpStream) gc.data.planeSink;
	}

	/** True when the current plane is skipped or a sink is recording. */
	static boolean skipOrRecord (GC gc) {
		GCData data = gc.data;
		return (data.plane & data.planeDraw) == 0 || data.planeSink != null;
	}

	private static boolean unsupportedState (GCData data) {
		return (data.retainedMask & XOR) != 0 || data.foregroundPattern != null || data.backgroundPattern != null
			|| (data.lineStyle == SWT.LINE_CUSTOM && data.lineDashes != null);
	}

	private static int mask (GCData data, int kind) {
		return PaintKeys.mask (kind, data.lineStyle, data.lineCap, data.lineJoin, -1, 0, data.lineWidth, PaintKeys.BACKEND_SKIJA);
	}

	/** Shape ops: FILL_RECT/FILL_OVAL use the background, DRAW_* the foreground. */
	static boolean divert (GC gc, int op, int a, int b, int c, int d) {
		GCData data = gc.data;
		if ((data.plane & data.planeDraw) == 0) return true;
		OpStream sink = sink (gc);
		if (sink == null) return false;
		if (unsupportedState (data)) {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
			return true;
		}
		boolean fill = op == OpStream.FILL_RECT || op == OpStream.FILL_OVAL;
		int colorref = fill ? data.background : data.foreground;
		sink.rect (op, a, b, c, d, PaintKeys.argbFromColorref (colorref, data.alpha), mask (data, fill ? PaintKeys.KIND_FILL : PaintKeys.KIND_STROKE));
		return true;
	}

	static boolean divertGradient (GC gc, int x, int y, int w, int h, boolean vertical) {
		GCData data = gc.data;
		if ((data.plane & data.planeDraw) == 0) return true;
		OpStream sink = sink (gc);
		if (sink == null) return false;
		if (unsupportedState (data)) {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
			return true;
		}
		sink.gradient (x, y, w, h, PaintKeys.argbFromColorref (data.foreground, data.alpha),
			PaintKeys.argbFromColorref (data.background, data.alpha), vertical ? 1 : 0);
		return true;
	}

	static boolean divertPoly (GC gc, int op, int [] points) {
		GCData data = gc.data;
		if ((data.plane & data.planeDraw) == 0) return true;
		OpStream sink = sink (gc);
		if (sink == null) return false;
		if (unsupportedState (data)) {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
			return true;
		}
		boolean fill = op == OpStream.FILL_POLY;
		sink.poly (op, points, PaintKeys.argbFromColorref (fill ? data.background : data.foreground, data.alpha),
			mask (data, fill ? PaintKeys.KIND_FILL : PaintKeys.KIND_STROKE));
		return true;
	}

	/** Operations with no GPU replay (images, paths, arcs, focus, copyArea, TextLayout). */
	static boolean divertUnsupported (GC gc, int reason) {
		GCData data = gc.data;
		if ((data.plane & data.planeDraw) == 0) return true;
		OpStream sink = sink (gc);
		if (sink == null) return false;
		sink.fail |= reason;
		return true;
	}

	/**
	 * Records SWT's absolute affine state in the same packed stream as the draw
	 * operations. Translation is converted to the GC's current device-pixel zoom;
	 * the linear matrix terms remain unitless.
	 */
	static void recordTransform (GC gc, Transform transform) {
		OpStream sink = sink (gc);
		if (sink == null) return;
		if (transform == null) {
			sink.transformIdentity ();
			return;
		}
		transform.getElements (TRANSFORM_SCRATCH);
		int zoom = gc.getZoom ();
		sink.transform (
			TRANSFORM_SCRATCH [0], TRANSFORM_SCRATCH [1],
			TRANSFORM_SCRATCH [2], TRANSFORM_SCRATCH [3],
			Win32DPIUtils.pointToPixel (gc.drawable, TRANSFORM_SCRATCH [4], zoom),
			Win32DPIUtils.pointToPixel (gc.drawable, TRANSFORM_SCRATCH [5], zoom));
	}

	/** Records a clip change; non-rectangular or transform-local clips fail closed. */
	static void recordClip (GC gc, long hRgn) {
		/*
		 * Damage clipping is installed on the HDC before recording starts and is
		 * therefore already device-relative. An application clip changed while a
		 * transform is active has subtler GDI+ region semantics; leave that case to
		 * the stock GDI oracle until it has an exact differential proof.
		 */
		if ((gc.data.retainedMask & TRANSFORMED) != 0) {
			sink (gc).fail |= OpStream.FAIL_TRANSFORM;
			return;
		}
		OpStream sink = sink (gc);
		if (hRgn == 0) {
			sink.clipReset ();
			return;
		}
		RECT box = RECT_SCRATCH;
		int kind = OS.GetRgnBox (hRgn, box);
		if (kind == 2 /* SIMPLEREGION */) {
			sink.clip (box.left, box.top, box.right - box.left, box.bottom - box.top);
		} else if (kind == OS.NULLREGION) {
			sink.clip (0, 0, 0, 0);
		} else {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
		}
	}

	/** Text through drawString/drawText; tabs, delimiters and mnemonics are not replayed. */
	static boolean divertText (GC gc, String string, int x, int y, int flags) {
		GCData data = gc.data;
		if ((data.plane & data.planeDraw) == 0) return true;
		OpStream sink = sink (gc);
		if (sink == null) return false;
		if (unsupportedState (data) || (data.style & SWT.MIRRORED) != 0 || !plainText (string, flags)) {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
			return true;
		}
		int id = fontId (gc);
		if (id < 0) {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
			return true;
		}
		FontTable fonts = FontTable.INSTANCE;
		if (!loadAdvances (gc, fonts, id, string)) {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
			return true;
		}
		boolean opaque = (flags & SWT.DRAW_TRANSPARENT) == 0;
		int width = opaque ? textWidth (gc, string) : 0;
		sink.text (string, 0, string.length (), id, x, y, PaintKeys.argbFromColorref (data.foreground, data.alpha),
			PaintKeys.argbFromColorref (data.background, data.alpha), opaque ? TEXT_OPAQUE : 0, width,
			fonts.cellHeight [id], fonts.ascent [id]);
		return true;
	}

	/** Characters replayed as one glyph each without shaping: printable Latin, Greek, Cyrillic, Armenian. */
	static boolean plainText (String string, int flags) {
		for (int i = 0, n = string.length (); i < n; i++) {
			char c = string.charAt (i);
			if (c < ' ' || c >= 0x0590 || (c == '&' && (flags & SWT.DRAW_MNEMONIC) != 0)) return false;
		}
		return true;
	}

	/** Loads GDI advance widths (ABC sums) for the pages {@code string} uses; ExtTextOut places glyphs by these. */
	static boolean loadAdvances (GC gc, FontTable fonts, int id, String string) {
		int lastPage = -1;
		for (int i = 0, n = string.length (); i < n; i++) {
			int page = string.charAt (i) >>> 8;
			if (page == lastPage || fonts.hasPage (id, page)) {
				lastPage = page;
				continue;
			}
			gc.checkGC (GC.FONT);
			int [] abc = new int [256 * 3], widths = new int [256];
			if (OS.GetCharABCWidths (gc.handle, page << 8, (page << 8) + 255, abc)) {
				for (int c = 0; c < 256; c++) widths [c] = abc [c * 3] + abc [c * 3 + 1] + abc [c * 3 + 2];
			} else if (!OS.GetCharWidth (gc.handle, page << 8, (page << 8) + 255, widths)) {
				return false;
			}
			fonts.setPage (id, page, widths);
			lastPage = page;
		}
		return true;
	}

	private static int textWidth (GC gc, String string) {
		int length = string.length ();
		if (charScratch.length < length) charScratch = new char [Integer.highestOneBit (length) << 1];
		string.getChars (0, length, charScratch, 0);
		OS.GetTextExtentPoint32 (gc.handle, charScratch, length, SIZE_SCRATCH);
		return SIZE_SCRATCH.cx;
	}

	/** Int id of the GC's font in {@link FontTable}, or -1 when it cannot be replayed. */
	static int fontId (GC gc) {
		Font font = gc.data.font;
		if (font.gpuFontId != 0) return font.gpuFontId - 1;
		gc.checkGC (GC.FONT);
		return defineFont (gc, font, SWTFontProvider.getFontHandle (font, gc.data.nativeZoom), false);
	}

	/** Int id of an arbitrary font (a TextLayout run font) measured on the GC's HDC. */
	static int fontIdOf (GC gc, Font font) {
		if (font.gpuFontId != 0) return font.gpuFontId - 1;
		return defineFont (gc, font, SWTFontProvider.getFontHandle (font, gc.data.nativeZoom), true);
	}

	private static int defineFont (GC gc, Font font, long hFont, boolean select) {
		LOGFONT logFont = new LOGFONT ();
		OS.GetObject (hFont, LOGFONT.sizeof, logFont);
		if (logFont.lfUnderline != 0 || logFont.lfStrikeOut != 0) return -1;
		long old = select ? OS.SelectObject (gc.handle, hFont) : 0;
		TEXTMETRIC metric = new TEXTMETRIC ();
		OS.GetTextMetrics (gc.handle, metric);
		if (select) OS.SelectObject (gc.handle, old);
		char [] face = logFont.lfFaceName;
		int n = 0;
		while (n < face.length && face [n] != 0) n++;
		int pixelHeight = logFont.lfHeight < 0 ? -logFont.lfHeight : metric.tmHeight - metric.tmInternalLeading;
		int id = FontTable.INSTANCE.define (face, n, logFont.lfHeight, logFont.lfWeight, logFont.lfItalic, pixelHeight, metric.tmAscent, metric.tmHeight);
		if (id >= 0) font.gpuFontId = id + 1;
		return id;
	}

	/* Scratch for native glyph arrays; UI-thread confined like every GC. */
	private static char [] glyphScratch = new char [256];
	private static int [] intScratch = new int [512];

	/**
	 * Records one Uniscribe-shaped run (glyph ids and advances read from native
	 * memory) for GPU replay. Runs with glyph offsets (combining marks) return
	 * false and are left to GDI.
	 */
	static boolean recordGlyphRun (OpStream sink, long glyphs, long advances, long goffsets, int count, int fontId, int x, int baselineY, int argb) {
		if (glyphScratch.length < count) glyphScratch = new char [Integer.highestOneBit (count) << 1];
		if (intScratch.length < count * 2) intScratch = new int [Integer.highestOneBit (count * 2) << 1];
		if (goffsets != 0) {
			OS.MoveMemory (intScratch, goffsets, count * 8);
			for (int i = 0; i < count * 2; i++) if (intScratch [i] != 0) return false;
		}
		OS.MoveMemory (glyphScratch, glyphs, count * 2);
		OS.MoveMemory (intScratch, advances, count * 4);
		int o = sink.begin (OpStream.GLYPH_RUN, 5 + 2 * count);
		if (o < 0) return false;
		int [] ops = sink.ops ();
		ops [o] = fontId; ops [o + 1] = x; ops [o + 2] = baselineY; ops [o + 3] = argb; ops [o + 4] = count;
		for (int i = 0; i < count; i++) {
			ops [o + 5 + i] = glyphScratch [i];
			ops [o + 5 + count + i] = intScratch [i];
		}
		return true;
	}
}

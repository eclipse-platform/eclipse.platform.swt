/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.render.m3;

import java.util.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.render.*;
import org.eclipse.swt.internal.win32.*;

import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.FontMetrics;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.Path;
import io.github.humbleui.types.Rect;

/**
 * The <code>m3</code> backend on win32, the M3 renderer: it draws through the Skija library (Apache-2.0) on the CPU,
 * straight into the pixels of the plane's back buffer (<code>Surface.makeRasterDirect</code> over the DIB section), so
 * a frame allocates no surface and copies nothing; the plane then copies the damage to the window. This package is the
 * one adapter between SWT and the library's binding classes, which keep their package because the library's prebuilt
 * natives bind to it.
 * <p>
 * Caches, all bounded LRUs closed on eviction: one library font (with its metrics) per native font handle, shaped
 * text lines per font and string with their blob and width (the skija-canvas text LRU pattern), images per SWT
 * image, theme part images per part, state and size. This class is loaded by name, so SWT runs without the Skija
 * jars when this mode is not used.
 * </p><p>
 * The paint path is data oriented (rule 18): the per-row operations (fills, text) run on cached pointers through
 * the library's primitive native entry points and allocate nothing; objects are made only when a cache misses.
 * </p>
 */
public final class M3RowCanvas implements BufferCanvas {
	private static final int FONTS = 32, LINES = 2048, PART_IMAGES = 256;
	private static final int CLIP_INTERSECT = ClipMode.INTERSECT.ordinal ();
	private static final String ELLIPSIS_TEXT = "...";

	/** A cached font and its metrics, measured once. */
	static final class FontEntry implements AutoCloseable {
		final Font font;
		final float ascent, lineHeight;

		FontEntry (Font font) {
			this.font = font;
			FontMetrics metrics = font.getMetrics ();
			ascent = -metrics.getAscent ();
			lineHeight = metrics.getDescent () - metrics.getAscent ();
		}

		@Override
		public void close () {
			font.close ();
		}
	}

	/** A cached shaped line: its blob pointer and width, read once; and its ellipsis form for the last box width asked. */
	static final class LineEntry implements AutoCloseable {
		final TextLine line;
		final TextBlob blob;
		final long blobPtr;
		final float width;
		int fittedWidth = -1;
		LineEntry fitted;

		LineEntry (TextLine line) {
			this.line = line;
			blob = line.getTextBlob ();
			blobPtr = blob == null ? 0 : io.github.humbleui.skija.impl.Native.getPtr (blob);
			width = line.getWidth ();
		}

		@Override
		public void close () {
			if (fitted != null) fitted.close ();
			if (blob != null) blob.close ();
			line.close ();
		}
	}

	private final IntSupplier zoom;
	private final ThemeParts parts;
	private final Paint paint = new Paint ().setAntiAlias (false);
	private final Paint textPaint = new Paint ().setAntiAlias (true);
	private final Paint focusPaint = new Paint ().setMode (PaintMode.STROKE).setStrokeWidth (1).setColor (0xFF000000)
		.setPathEffect (PathEffect.makeDash (new float [] {1, 1}, 0));
	private final long paintPtr = io.github.humbleui.skija.impl.Native.getPtr (paint);
	private final long textPaintPtr = io.github.humbleui.skija.impl.Native.getPtr (textPaint);
	private final SurfaceProps props = new SurfaceProps (false, PixelGeometry.RGB_H);
	private Surface surface;
	private Canvas canvas;
	private long canvasPtr;
	private long dc, bits;
	private int width, height;
	private final Map<Long, FontEntry> fonts = lru (FONTS);
	private final Map<Long, Map<String, LineEntry>> lines = new HashMap<> ();
	private final Map<org.eclipse.swt.graphics.Image, Image> images = new WeakHashMap<> ();
	private final Map<ImageData, Image> glyphs = new WeakHashMap<> ();
	private final Map<Long, Image> partImages = lru (PART_IMAGES);
	private final LOGFONT logFont = new LOGFONT ();
	/* the gpu mode: the frame is drawn on the graphics hardware and read back into the back buffer */
	private final M3Gpu gpu;
	private Pixmap readback;
	private int frameX, frameY, frameWidth, frameHeight;

	/**
	 * The CPU (raster) backend of the <code>m3</code> mode.
	 *
	 * @param hwnd the plane's window (theme parts are opened for it)
	 * @param itemThemeClass the uxtheme class of rows (NUL-terminated)
	 * @param zoom the widget's current native zoom
	 */
	public M3RowCanvas (long hwnd, char [] itemThemeClass, IntSupplier zoom) {
		this (hwnd, itemThemeClass, zoom, null);
	}

	private M3RowCanvas (long hwnd, char [] itemThemeClass, IntSupplier zoom, M3Gpu gpu) {
		this.zoom = zoom;
		this.parts = new ThemeParts (hwnd, itemThemeClass, zoom);
		this.gpu = gpu;
	}

	/**
	 * The graphics hardware backend of the <code>gpu</code> mode (Skia over OpenGL, see {@link M3Gpu}), or null when
	 * no GL context can be made.
	 */
	public static M3RowCanvas gpu (long hwnd, char [] itemThemeClass, IntSupplier zoom) {
		M3Gpu gpu = M3Gpu.acquire ();
		return gpu == null ? null : new M3RowCanvas (hwnd, itemThemeClass, zoom, gpu);
	}

	private static <K, V> Map<K, V> lru (int capacity) {
		return new LinkedHashMap<> (16, 0.75f, true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry (Map.Entry<K, V> eldest) {
				if (size () <= capacity) return false;
				if (eldest.getValue () instanceof AutoCloseable closeable) close (closeable);
				return true;
			}
		};
	}

	private static void close (AutoCloseable closeable) {
		try {
			closeable.close ();
		} catch (Exception e) {
			/* Skija's close does not throw */
		}
	}

	@Override
	public void target (BackBuffer buffer) {
		dc = buffer.dc;
		if (surface != null && buffer.bits == bits && buffer.width == width && buffer.height == height) return;
		if (surface != null) surface.close ();
		bits = buffer.bits;
		width = buffer.width;
		height = buffer.height;
		ImageInfo info = new ImageInfo (width, height, ColorType.BGRA_8888, ColorAlphaType.PREMUL);
		if (gpu != null) {
			gpu.makeCurrent ();
			surface = Surface.makeRenderTarget (gpu.context, false, info, 0, SurfaceOrigin.TOP_LEFT, props);
		} else {
			surface = Surface.makeRasterDirect (info, bits, width * 4L, props);
		}
		canvas = surface.getCanvas ();
		canvasPtr = io.github.humbleui.skija.impl.Native.getPtr (canvas);
	}

	@Override
	public void begin (int x, int y, int width, int height) {
		/* a GDI call that answers flushes the GDI batch of the behaviour pass before Skia writes the same pixels */
		OS.GetPixel (dc, x, y);
		if (gpu != null) gpu.makeCurrent ();
		frameX = x;
		frameY = y;
		frameWidth = width;
		frameHeight = height;
		canvas.save ();
		canvas.clipRect (Rect.makeXYWH (x, y, width, height));
	}

	@Override
	public void end () {
		canvas.restore ();
		if (gpu == null || frameWidth <= 0 || frameHeight <= 0) return;
		/* the GPU frame is read back into the damage of the back buffer; the read waits for the GPU work */
		gpu.context.flush ();
		ImageInfo info = new ImageInfo (frameWidth, frameHeight, ColorType.BGRA_8888, ColorAlphaType.PREMUL);
		long address = bits + ((long) frameY * width + frameX) * 4L;
		if (readback == null) {
			readback = Pixmap.make (info, address, width * 4);
		} else {
			readback.reset (info, address, width * 4);
		}
		surface.readPixels (readback, frameX, frameY);
	}

	@Override
	public void fill (int x, int y, int width, int height, int rgb) {
		if (width <= 0 || height <= 0) return;
		paint.setColor (0xFF000000 | rgb);
		Canvas._nDrawRect (canvasPtr, x, y, x + width, y + height, paintPtr);
	}

	@Override
	public void text (String text, long font, int rgb, int x, int y, int width, int height, int align) {
		if (text.isEmpty () || width <= 0) return;
		FontEntry entry = font (font);
		LineEntry line = line (font, entry.font, text);
		if ((align & ELLIPSIS) != 0 && line.width > width) line = fitted (font, entry.font, line, text, width);
		align &= ~ELLIPSIS;
		if (line.blobPtr == 0) return;
		float baseline = y + (height - entry.lineHeight) / 2 + entry.ascent;
		float left = align == SWT.RIGHT ? x + width - line.width : align == SWT.CENTER ? x + (width - line.width) / 2 : x;
		textPaint.setColor (0xFF000000 | rgb);
		Canvas._nSave (canvasPtr);
		Canvas._nClipRect (canvasPtr, x, y, x + width, y + height, CLIP_INTERSECT, false);
		Canvas._nDrawTextBlob (canvasPtr, line.blobPtr, Math.round (left), Math.round (baseline), textPaintPtr);
		Canvas._nRestore (canvasPtr);
	}

	/* one library font per native font handle: face, pixel height, weight and italic of its LOGFONT */
	private FontEntry font (long hFont) {
		FontEntry cached = fonts.get (hFont);
		if (cached != null) return cached;
		String face = "Segoe UI";
		int height = -12, weight = 400;
		boolean italic = false;
		if (hFont != 0 && hFont != -1 && OS.GetObject (hFont, LOGFONT.sizeof, logFont) != 0) {
			int length = 0;
			while (length < logFont.lfFaceName.length && logFont.lfFaceName [length] != 0) length++;
			face = new String (logFont.lfFaceName, 0, length);
			height = logFont.lfHeight;
			weight = logFont.lfWeight;
			italic = logFont.lfItalic != 0;
		}
		FontStyle style = new FontStyle (weight == 0 ? 400 : weight, FontWidth.NORMAL, italic ? FontSlant.ITALIC : FontSlant.UPRIGHT);
		Typeface typeface = FontMgr.getDefault ().matchFamilyStyle (face, style);
		Font font = new Font (typeface, Math.abs (height));
		if (height > 0) {
			/* a positive height is the cell height: scale the em size so ascent plus descent match it */
			FontMetrics metrics = font.getMetrics ();
			font.setSize (height * height / Math.max (1f, metrics.getDescent () - metrics.getAscent ()));
		}
		font.setEdging (FontEdging.SUBPIXEL_ANTI_ALIAS).setSubpixel (true).setHinting (FontHinting.NORMAL);
		FontEntry entry = new FontEntry (font);
		fonts.put (hFont, entry);
		return entry;
	}

	/*
	 * The line cut as DrawText's DT_END_ELLIPSIS cuts it: the longest prefix whose width plus the width of "..." fits
	 * the box, then "..." (only "..." when no character fits). Kept on the full line for the last width asked.
	 */
	private LineEntry fitted (long hFont, Font font, LineEntry full, String text, int width) {
		if (full.fittedWidth == width) return full.fitted;
		float room = width - line (hFont, font, ELLIPSIS_TEXT).width;
		int low = 0, high = text.length ();
		while (low < high) {
			int middle = (low + high + 1) >>> 1;
			if (full.line.getCoordAtOffset (middle) <= room) low = middle; else high = middle - 1;
		}
		if (full.fitted != null) full.fitted.close ();
		full.fitted = new LineEntry (TextLine.make (text.substring (0, low) + ELLIPSIS_TEXT, font));
		full.fittedWidth = width;
		return full.fitted;
	}

	private LineEntry line (long hFont, Font font, String text) {
		Map<String, LineEntry> cache = lines.computeIfAbsent (hFont, key -> lru (LINES));
		LineEntry line = cache.get (text);
		if (line == null) {
			line = new LineEntry (TextLine.make (text, font));
			cache.put (text, line);
		}
		return line;
	}

	@Override
	public void image (org.eclipse.swt.graphics.Image image, int x, int y, int width, int height) {
		if (image == null || image.isDisposed () || width <= 0 || height <= 0) return;
		Image skImage = images.get (image);
		if (skImage == null) {
			skImage = convert (image.getImageData (zoom.getAsInt ()));
			images.put (image, skImage);
		}
		canvas.drawImageRect (skImage, Rect.makeXYWH (x, y, width, height));
	}

	private static Image convert (ImageData data) {
		return Image.makeRasterFromBytes (new ImageInfo (data.width, data.height, ColorType.BGRA_8888, ColorAlphaType.PREMUL), RowImages.bgra (data), data.width * 4L);
	}

	@Override
	public void imageData (ImageData data, int x, int y, int width, int height) {
		if (data == null || width <= 0 || height <= 0) return;
		Image image = glyphs.computeIfAbsent (data, M3RowCanvas::convert);
		canvas.drawImageRect (image, Rect.makeXYWH (x, y, width, height));
	}

	@Override
	public void polygon (int [] points, int count, int rgb) {
		if (count < 3) return;
		try (Path path = new Path ()) {
			path.moveTo (points [0], points [1]);
			for (int i = 1; i < count; i++) path.lineTo (points [2 * i], points [2 * i + 1]);
			path.closePath ();
			paint.setColor (0xFF000000 | rgb);
			canvas.drawPath (path, paint);
		}
	}

	@Override
	public void part (int part, int state, int x, int y, int width, int height) {
		if (width <= 0 || height <= 0) return;
		long key = (long) part << 56 | (long) (state & 0xFFFF) << 40 | (long) (width & 0xFFFFF) << 20 | height & 0xFFFFF;
		Image image = partImages.get (key);
		if (image == null) {
			byte [] pixels = parts.image (part, state, width, height);
			image = Image.makeRasterFromBytes (new ImageInfo (width, height, ColorType.BGRA_8888, ColorAlphaType.PREMUL), pixels, width * 4L);
			partImages.put (key, image);
		}
		canvas.drawImage (image, x, y);
	}

	@Override
	public void focus (int x, int y, int width, int height) {
		canvas.drawRect (Rect.makeXYWH (x + 0.5f, y + 0.5f, width - 1, height - 1), focusPaint);
	}

	@Override
	public void dispose () {
		parts.close ();
		fonts.values ().forEach (M3RowCanvas::close);
		lines.values ().forEach (cache -> cache.values ().forEach (M3RowCanvas::close));
		images.values ().forEach (M3RowCanvas::close);
		glyphs.values ().forEach (M3RowCanvas::close);
		glyphs.clear ();
		partImages.values ().forEach (M3RowCanvas::close);
		fonts.clear ();
		lines.clear ();
		images.clear ();
		partImages.clear ();
		if (surface != null) surface.close ();
		surface = null;
		if (readback != null) readback.close ();
		readback = null;
		if (gpu != null) gpu.release ();
		paint.close ();
		textPaint.close ();
		focusPaint.close ();
	}
}

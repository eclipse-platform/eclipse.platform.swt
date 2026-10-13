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
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gpu.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Retained-plane frame driver for one CANVAS composite whose state carries
 * {@link Widget#GPU_RETAINED}. It renders into the widget's own window during
 * WM_PAINT: there is no child window, so focus, traversal, input, IME and
 * accessibility stay on the widget.
 * <p>
 * A frame: (1) map damage to tiles; (2) raster only the visible-but-invalid
 * part of each tile by sending the widget a normal Paint event clipped to that
 * rectangle (GDI into the retained back buffer, or recorded into an
 * {@link OpStream} and replayed on the GPU); (3) composite the visible tiles;
 * (4) run an overlay-only Paint pass when the widget draws selection, caret or
 * feedback planes; (5) present the damaged rectangle with one BitBlt from the
 * back buffer (after a GPU readback when the Skija backend is active).
 * </p>
 * <p>
 * Scrolling through {@link Canvas#scroll} translates the tile grid; only the
 * exposed band is rasterized. Content damage (redraw, OS invalidation) also
 * drops off-screen tiles because immediate-mode widgets report damage only for
 * what is visible. Any backend failure fails closed: the GPU target is
 * dropped and the widget continues on GDI.
 * </p>
 */
final class RetainedFrame implements TileGrid.Listener {
	static final int HAS_OVERLAY = 1, CONTENT_GDI = 2, OVERLAY_GDI = 4;
	static final int MAX_TILES = Integer.getInteger ("swt.gpu.tiles", 256);
	static final int CELL = TileGrid.SIZE, ATLAS_COLUMNS = 8;

	final Composite control;
	TileGrid grid;
	final RectList raster = new RectList (8), compositeOnly = new RectList (4);
	final OpStream ops = new OpStream (4096, 1 << 22);
	final int [] r = new int [4], parts = new int [16];
	final PAINTSTRUCT ps = new PAINTSTRUCT ();
	final RECT rect = new RECT (), fill = new RECT ();
	int flags, bgArgb, bgReplayable;
	long bufDC, bufBitmap, bufOld, bufBits;
	int bufW, bufH;
	long atlasDC, atlasBitmap, atlasOld;
	long clipRgn;
	PlaneBackend.Target gpu;
	GCData lastData;

	RetainedFrame (Composite control) {
		this.control = control;
	}

	/** WM_PAINT entry; returns false when the caller must run the stock path. */
	static boolean paint (Composite control) {
		RetainedFrame frame = control.retainedFrame;
		if (frame == null) frame = control.retainedFrame = new RetainedFrame (control);
		long hwnd = control.handle;
		long hDC = OS.BeginPaint (hwnd, frame.ps);
		int ok = 0;
		try {
			ok = frame.frame (hDC) ? 1 : 0;
		} finally {
			OS.EndPaint (hwnd, frame.ps);
			if (ok == 0 && !control.isDisposed ()) frame.failClosed ();
		}
		return true;
	}

	/** Drops the retained path for this widget; the next paint is stock GDI. */
	void failClosed () {
		GpuCounters.inc (GpuCounters.FALLBACKS);
		control.state &= ~Widget.GPU_RETAINED;
		dispose ();
		control.retainedFrame = null;
		OS.InvalidateRect (control.handle, null, true);
	}

	boolean frame (long hDC) {
		OS.GetClientRect (control.handle, rect);
		int cw = rect.right - rect.left, ch = rect.bottom - rect.top;
		int px = ps.left, py = ps.top, pw = ps.right - ps.left, ph = ps.bottom - ps.top;
		if (cw <= 0 || ch <= 0 || pw <= 0 || ph <= 0) return true;
		if (!ensure (cw, ch)) return false;
		if (!compositeOnlyCovers (px, py, pw, ph)) damageContent (px, py, pw, ph);
		compositeOnly.clear ();
		GpuCounters.inc (GpuCounters.FRAMES);
		if (!rasterVisible ()) return false;
		composite (px, py, pw, ph);
		int overlayGdi = 0;
		if ((flags & HAS_OVERLAY) != 0) overlayGdi = overlay (px, py, pw, ph) ? 0 : 1;
		if (gpu != null && !gpuPresent (px, py, pw, ph)) return false;
		if (overlayGdi != 0) overlayPass (px, py, pw, ph, null);
		OS.BitBlt (hDC, px, py, pw, ph, bufDC, px, py, OS.SRCCOPY);
		GpuCounters.add (GpuCounters.COMPOSITE_PIXELS, (long) pw * ph);
		return true;
	}

	/* ---- damage ---- */

	boolean compositeOnlyCovers (int x, int y, int w, int h) {
		for (int i = 0; i < compositeOnly.count (); i++) {
			if (compositeOnly.x0 (i) <= x && compositeOnly.y0 (i) <= y && compositeOnly.x1 (i) >= x + w && compositeOnly.y1 (i) >= y + h) return true;
		}
		return false;
	}

	/** Content changed inside the client rectangle (pixels); off-screen tiles are no longer trustworthy. */
	void damageContent (int x, int y, int w, int h) {
		if (grid == null) return;
		grid.damage (x, y, w, h);
		grid.damageOffscreen ();
	}

	/** Control.redraw: {@code null} is a full redraw, which makes every retained tile stale. */
	void damageRedraw (RECT damaged) {
		if (grid == null) return;
		if (damaged == null) {
			grid.damageAll ();
		} else {
			damageContent (damaged.left, damaged.top, damaged.right - damaged.left, damaged.bottom - damaged.top);
		}
	}

	/** Canvas.scroll: content inside {@code src} moved by (dx, dy) client pixels. */
	void scroll (int dx, int dy, RECT src) {
		if (grid == null) {
			OS.InvalidateRect (control.handle, null, true);
			return;
		}
		OS.GetClientRect (control.handle, rect);
		int cw = rect.right, ch = rect.bottom;
		int sx0 = Math.max (src.left, 0), sy0 = Math.max (src.top, 0), sx1 = Math.min (src.right, cw), sy1 = Math.min (src.bottom, ch);
		grid.translate (dx, dy);
		GpuCounters.inc (GpuCounters.SCROLL_TRANSLATES);
		int n = Rects.subtract (0, 0, cw, ch, sx0, sy0, sx1 - sx0, sy1 - sy0, parts);
		for (int i = 0; i < n; i++) grid.damage (parts [i * 4], parts [i * 4 + 1], parts [i * 4 + 2], parts [i * 4 + 3]);
		n = Rects.subtract (sx0, sy0, sx1 - sx0, sy1 - sy0, sx0 + dx, sy0 + dy, sx1 - sx0, sy1 - sy0, parts);
		for (int i = 0; i < n; i++) grid.damage (parts [i * 4], parts [i * 4 + 1], parts [i * 4 + 2], parts [i * 4 + 3]);
		compositeOnly.add (0, 0, cw, ch);
		OS.InvalidateRect (control.handle, null, false);
	}

	/** Overlay planes changed inside the rectangle (pixels); tiles stay valid. */
	void invalidateOverlay (int x, int y, int w, int h) {
		if (w <= 0 || h <= 0) return;
		compositeOnly.add (x, y, x + w, y + h);
		OS.SetRect (rect, x, y, x + w, y + h);
		OS.InvalidateRect (control.handle, rect, false);
	}

	/* ---- resources ---- */

	boolean ensure (int cw, int ch) {
		int needed = TileGrid.tilesFor (cw, ch);
		if (needed > MAX_TILES) return false;
		if (grid == null || grid.capacity () < needed) {
			releaseTiles ();
			grid = new TileGrid (Math.min (MAX_TILES, Math.max (16, needed * 2)));
			grid.setListener (this);
		}
		grid.setClient (cw, ch);
		if (bufW < cw || bufH < ch) {
			releaseBuffer ();
			if (!createBuffer (Math.max (cw, bufW), Math.max (ch, bufH))) return false;
		}
		if (clipRgn == 0) clipRgn = OS.CreateRectRgn (0, 0, 0, 0);
		if (gpu == null && (RetainedPlanes.MODE & RetainedPlanes.MODE_SKIJA) != 0) gpu = createTarget ();
		if (gpu != null && !gpu.beginFrame (cw, ch)) dropGpu ();
		if (gpu == null && atlasDC == 0) return createAtlas ();
		return true;
	}

	PlaneBackend.Target createTarget () {
		PlaneBackend backend = control.display.gpuBackend ();
		if (backend == null) return null;
		try {
			return backend.createTarget (grid.capacity ());
		} catch (Throwable failure) {
			GpuCounters.inc (GpuCounters.FALLBACKS);
			return null;
		}
	}

	/** GPU failure: continue on the GDI atlas with every tile invalid. */
	void dropGpu () {
		GpuCounters.inc (GpuCounters.FALLBACKS);
		PlaneBackend.Target target = gpu;
		gpu = null;
		try {
			if (target != null) target.dispose ();
		} catch (Throwable ignored) {
		}
		if (grid != null) grid.invalidateAll ();
		flags |= CONTENT_GDI;
	}

	boolean createBuffer (int w, int h) {
		long [] bits = new long [1];
		bufBitmap = createDib (w, h, bits);
		if (bufBitmap == 0) return false;
		bufBits = bits [0];
		bufDC = OS.CreateCompatibleDC (0);
		bufOld = OS.SelectObject (bufDC, bufBitmap);
		bufW = w;
		bufH = h;
		return true;
	}

	boolean createAtlas () {
		int rows = (grid.capacity () + ATLAS_COLUMNS - 1) / ATLAS_COLUMNS;
		long [] bits = new long [1];
		atlasBitmap = createDib (ATLAS_COLUMNS * CELL, rows * CELL, bits);
		if (atlasBitmap == 0) return false;
		atlasDC = OS.CreateCompatibleDC (0);
		atlasOld = OS.SelectObject (atlasDC, atlasBitmap);
		GpuCounters.add (GpuCounters.CPU_TILE_BYTES, (long) ATLAS_COLUMNS * CELL * rows * CELL * 4);
		return true;
	}

	static long createDib (int w, int h, long [] bits) {
		BITMAPINFOHEADER header = new BITMAPINFOHEADER ();
		header.biSize = BITMAPINFOHEADER.sizeof;
		header.biWidth = w;
		header.biHeight = -h;
		header.biPlanes = 1;
		header.biBitCount = 32;
		header.biCompression = OS.BI_RGB;
		byte [] bmi = new byte [BITMAPINFOHEADER.sizeof];
		OS.MoveMemory (bmi, header, BITMAPINFOHEADER.sizeof);
		return OS.CreateDIBSection (0, bmi, OS.DIB_RGB_COLORS, bits, 0, 0);
	}

	@Override
	public void tileEvicted (int slot) {
		if (gpu != null) gpu.tileEvicted (slot);
	}

	void releaseTiles () {
		if (gpu != null) {
			PlaneBackend.Target target = gpu;
			gpu = null;
			try {
				target.dispose ();
			} catch (Throwable ignored) {
			}
		}
		if (atlasDC != 0) {
			OS.SelectObject (atlasDC, atlasOld);
			OS.DeleteDC (atlasDC);
			OS.DeleteObject (atlasBitmap);
			atlasDC = atlasBitmap = atlasOld = 0;
		}
		grid = null;
	}

	void releaseBuffer () {
		if (bufDC != 0) {
			OS.SelectObject (bufDC, bufOld);
			OS.DeleteDC (bufDC);
			OS.DeleteObject (bufBitmap);
			bufDC = bufBitmap = bufOld = bufBits = 0;
			bufW = bufH = 0;
		}
	}

	void dispose () {
		releaseTiles ();
		releaseBuffer ();
		if (clipRgn != 0) {
			OS.DeleteObject (clipRgn);
			clipRgn = 0;
		}
	}

	/* ---- raster ---- */

	boolean rasterVisible () {
		grid.beginFrame ();
		raster.clear ();
		int tx1 = grid.visibleTx1 (), ty1 = grid.visibleTy1 ();
		for (int ty = grid.visibleTy0 (); ty <= ty1; ty++) {
			for (int tx = grid.visibleTx0 (); tx <= tx1; tx++) {
				int slot = grid.acquireVisible (tx, ty);
				if (slot < 0) return false;
				if (grid.rasterRect (slot, r)) raster.add (r [0], r [1], r [0] + r [2], r [1] + r [3]);
			}
		}
		if (raster.count () == 0) return true;
		int recorded = gpu != null && (flags & CONTENT_GDI) == 0 && backgroundReplayable () ? recordContent () : 0;
		if (recorded == 0) rasterContentGdi ();
		long [] visible = grid.visibleBits ();
		for (int slot = TileGrid.nextSet (visible, 0); slot >= 0; slot = TileGrid.nextSet (visible, slot + 1)) {
			if (!grid.rasterRect (slot, r)) continue;
			if (!store (slot, recorded)) return false;
			grid.rastered (slot, r [0], r [1], r [2], r [3]);
		}
		return true;
	}

	/** Records all raster rectangles into {@link #ops}; returns 1 when the stream is replayable. */
	int recordContent () {
		ops.reset ();
		for (int i = 0; i < raster.count (); i++) {
			ops.clipReset ();
			paintPass (raster.x0 (i), raster.y0 (i), raster.x1 (i), raster.y1 (i), RetainedPlanes.RETAINED, ops, 0);
		}
		if (ops.fail == 0) return 1;
		flags |= CONTENT_GDI;
		return 0;
	}

	void rasterContentGdi () {
		int background = (control.style & SWT.NO_BACKGROUND) == 0 ? 1 : 0;
		for (int i = 0; i < raster.count (); i++) {
			paintPass (raster.x0 (i), raster.y0 (i), raster.x1 (i), raster.y1 (i), RetainedPlanes.RETAINED, null, background);
		}
		OS.GetPixel (bufDC, 0, 0); // a GDI call returning a value flushes the thread's GDI batch before the CPU reads the DIB
	}

	boolean store (int slot, int recorded) {
		int lx = r [0] - grid.clientX (slot), ly = r [1] - grid.clientY (slot);
		if (gpu == null) {
			int cell = slot;
			OS.BitBlt (atlasDC, (cell % ATLAS_COLUMNS) * CELL + lx, (cell / ATLAS_COLUMNS) * CELL + ly, r [2], r [3], bufDC, r [0], r [1], OS.SRCCOPY);
			return true;
		}
		try {
			if (recorded != 0) {
				GpuCounters.inc (GpuCounters.GPU_RASTER_TILES);
				return gpu.rasterTile (slot, grid.clientX (slot), grid.clientY (slot), lx, ly, r [2], r [3], bgArgb, ops);
			}
			return gpu.uploadTile (slot, bufBits, bufW * 4, r [0], r [1], lx, ly, r [2], r [3]);
		} catch (Throwable failure) {
			return false; // fail closed: the frame driver drops the retained path for this widget
		}
	}

	boolean backgroundReplayable () {
		if ((control.style & SWT.NO_BACKGROUND) != 0) {
			bgReplayable = 0;
			return false;
		}
		Control background = control.findBackgroundControl ();
		int pixel = -1;
		if (background != null) {
			if (background.backgroundImage != null) return false;
			pixel = background.getBackgroundPixel ();
		}
		if (pixel == -1 && (control.state & Widget.THEME_BACKGROUND) != 0 && control.findThemeControl () != null) return false;
		if (pixel == -1) pixel = control.getBackgroundPixel ();
		bgArgb = PaintKeys.argbFromColorref (pixel, 0xFF);
		bgReplayable = 1;
		return true;
	}

	/**
	 * Sends one Paint event for the client rectangle into the back buffer with
	 * the given plane mask; {@code sink} non-null records instead of drawing.
	 */
	void paintPass (int x0, int y0, int x1, int y1, int planeDraw, OpStream sink, int background) {
		int saved = OS.SaveDC (bufDC);
		OS.SetRectRgn (clipRgn, x0, y0, x1, y1);
		OS.SelectClipRgn (bufDC, clipRgn);
		OS.SetMetaRgn (bufDC);
		if (background != 0) {
			OS.SetRect (fill, x0, y0, x1, y1);
			control.drawBackground (bufDC, fill);
		}
		GCData data = new GCData ();
		data.device = control.display;
		data.foreground = control.getForegroundPixel ();
		Control bg = control.findBackgroundControl ();
		data.background = (bg != null ? bg : control).getBackgroundPixel ();
		data.font = SWTFontProvider.getFont (control.display, OS.SendMessage (control.handle, OS.WM_GETFONT, 0, 0), control.nativeZoom);
		data.uiState = (int) OS.SendMessage (control.handle, OS.WM_QUERYUISTATE, 0, 0);
		data.planeDraw = planeDraw;
		data.planeSink = sink;
		GC gc = control.createNewGC (bufDC, data);
		try {
			Event event = new Event ();
			event.gc = gc;
			event.setBounds (Win32DPIUtils.pixelToPoint (new Rectangle (x0, y0, x1 - x0, y1 - y0), control.getAutoscalingZoom ()));
			control.sendEvent (SWT.Paint, event);
			event.gc = null;
		} finally {
			if (!gc.isDisposed ()) gc.dispose ();
			OS.RestoreDC (bufDC, saved);
		}
		if ((data.retainedMask & GCData.RETAIN_PLANES_USED) != 0) flags |= HAS_OVERLAY;
	}

	/* ---- composite, overlay, present ---- */

	void composite (int px, int py, int pw, int ph) {
		long [] visible = grid.visibleBits ();
		for (int slot = TileGrid.nextSet (visible, 0); slot >= 0; slot = TileGrid.nextSet (visible, slot + 1)) {
			int x = grid.clientX (slot), y = grid.clientY (slot);
			if (gpu != null) {
				gpu.composite (slot, x, y);
				continue;
			}
			int x0 = Math.max (x, px), y0 = Math.max (y, py), x1 = Math.min (x + CELL, px + pw), y1 = Math.min (y + CELL, py + ph);
			if (x0 >= x1 || y0 >= y1) continue;
			OS.BitBlt (bufDC, x0, y0, x1 - x0, y1 - y0, atlasDC, (slot % ATLAS_COLUMNS) * CELL + x0 - x, (slot / ATLAS_COLUMNS) * CELL + y0 - y, OS.SRCCOPY);
		}
	}

	/** Overlay planes; returns false when the GPU could not replay them (GDI after present). */
	boolean overlay (int px, int py, int pw, int ph) {
		GpuCounters.inc (GpuCounters.OVERLAY_FRAMES);
		if (gpu == null) {
			overlayPass (px, py, pw, ph, null);
			return true;
		}
		if ((flags & OVERLAY_GDI) != 0) return false;
		ops.reset ();
		ops.clipReset ();
		overlayPass (px, py, pw, ph, ops);
		if (ops.fail == 0 && gpu.replay (ops, px, py, pw, ph)) return true;
		flags |= OVERLAY_GDI;
		return false;
	}

	void overlayPass (int px, int py, int pw, int ph, OpStream sink) {
		paintPass (px, py, px + pw, py + ph, RetainedPlanes.OVERLAY, sink, 0);
	}

	boolean gpuPresent (int px, int py, int pw, int ph) {
		try {
			return gpu.present (bufBits, bufW * 4, px, py, pw, ph);
		} catch (Throwable failure) {
			return false; // fail closed
		}
	}
}

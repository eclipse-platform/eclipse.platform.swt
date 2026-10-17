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
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gpu.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Retained-plane frame driver for one CANVAS composite whose state carries
 * {@link Widget#GPU_RETAINED} in {@code retainedBits}, in the remote-framebuffer (VNC/RFB) model. It
 * renders into the widget's own window during WM_PAINT; there is no child
 * window, so focus, traversal, input, IME and accessibility stay on the widget.
 * <p>
 * The widget keeps a <em>content framebuffer</em> (a client-sized DIB, or a GPU
 * render target with the Skija backend) holding its retained planes without
 * overlays. A frame: (1) coalesce content damage into a reusable {@code int[]}
 * rectangle list (RFB framebuffer-update rectangles); (2) rasterize only those
 * rectangles by sending the widget normal Paint events clipped to them (GDI, or
 * recorded into an {@link OpStream} and replayed once on the GPU); (3) copy the
 * rasterized rectangles into content-space tiles; (4) when the widget draws
 * selection, caret or feedback planes, compose them over a copy of the content
 * in a present buffer; (5) present the damaged rectangle with one BitBlt.
 * </p>
 * <p>
 * {@link Canvas#scroll} is RFB CopyRect: the OS moves the on-screen pixels
 * (ScrollWindowEx, as stock), the content framebuffer moves the same block, the
 * tile grid translates, and only the exposed band is damaged; when the tiles
 * still hold it (scrolling back) the band is refilled from the tiles instead of
 * rasterized. Pixels outside the scrolled block (fixed margins) are never
 * damaged. Any backend failure fails closed to stock GDI.
 * </p>
 */
final class RetainedFrame implements TileGrid.Listener {
	static final int HAS_OVERLAY = 1, CONTENT_GDI = 2, OVERLAY_GDI = 4;
	static final int MAX_TILES = Integer.getInteger ("swt.gpu.tiles", 256);
	static final int CELL = TileGrid.SIZE, ATLAS_COLUMNS = 8;
	static final int FILL_MASK = PaintKeys.mask (PaintKeys.KIND_FILL, 0, 0, 0, -1, 0, 0, PaintKeys.BACKEND_SKIJA);
	static final int RECORD_BACKOFF_FRAMES = 32;
	static final long TRIM_NANOS = Long.getLong ("swt.gpu.trimMillis", 30_000L) * 1_000_000L;
	static final boolean DEBUG_OPS = Boolean.getBoolean ("swt.gpu.debugOps");

	final Composite control;
	TileGrid grid;
	/* Content damage (client pixels), coalesced per frame: the RFB framebuffer-update rectangles. */
	final RectList damage = new RectList (16), compositeOnly = new RectList (4);
	final OpStream ops = new OpStream (4096, 1 << 22);
	final int [] parts = new int [16];
	final PAINTSTRUCT ps = new PAINTSTRUCT ();
	final RECT rect = new RECT (), fill = new RECT ();
	int flags, bgArgb, contentValid, recordBackoff, debugFrames;
	long lastVisibleNanos;
	/* content framebuffer (GDI raster target) and present buffer (overlays, GPU readback) */
	long bufDC, bufBitmap, bufOld, bufBits;
	long preDC, preBitmap, preOld, preBits;
	int bufW, bufH;
	long atlasDC, atlasBitmap, atlasOld;
	long clipRgn;
	PlaneBackend.Target gpu;

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
		control.retainedBits &= ~Widget.GPU_RETAINED;
		dispose ();
		control.retainedFrame = null;
		OS.InvalidateRect (control.handle, null, true);
	}

	boolean frame (long hDC) {
		OS.GetClientRect (control.handle, rect);
		int cw = rect.right - rect.left, ch = rect.bottom - rect.top;
		int px = ps.left, py = ps.top, pw = ps.right - ps.left, ph = ps.bottom - ps.top;
		if (cw <= 0 || ch <= 0 || pw <= 0 || ph <= 0) return true;
		if (!effectivelyVisible ()) {
			/* not visible: no raster, composite or present; the damage stays queued until it is */
			damage.add (px, py, px + pw, py + ph);
			return true;
		}
		lastVisibleNanos = System.nanoTime ();
		if (!ensure (cw, ch)) return false;
		if (contentValid == 0) {
			damage.add (0, 0, cw, ch);
			contentValid = 1;
		}
		if (!compositeOnlyCovers (px, py, pw, ph)) damageContent (px, py, pw, ph);
		compositeOnly.clear ();
		GpuCounters.inc (GpuCounters.FRAMES);
		long submit = System.nanoTime ();
		if (damage.count () > 0) {
			if (!rasterDamage (cw, ch)) return false;
			storeTiles ();
			damage.clear ();
		}
		int hasOverlay = flags & HAS_OVERLAY;
		if (gpu != null) {
			int fromPresent = 0, overlayGdi = 0;
			if (hasOverlay != 0) {
				fromPresent = overlayGpu (px, py, pw, ph) ? 1 : 0;
				overlayGdi = 1 - fromPresent;
			}
			GpuCounters.add (GpuCounters.GPU_SUBMIT_NANOS, System.nanoTime () - submit);
			if (!gpuPresent (px, py, pw, ph, fromPresent)) return false;
			if (overlayGdi != 0) overlayPass (preDC, px, py, pw, ph, null);
			OS.BitBlt (hDC, px, py, pw, ph, preDC, px, py, OS.SRCCOPY);
		} else if (hasOverlay != 0) {
			GpuCounters.inc (GpuCounters.OVERLAY_FRAMES);
			OS.BitBlt (preDC, px, py, pw, ph, bufDC, px, py, OS.SRCCOPY);
			overlayPass (preDC, px, py, pw, ph, null);
			OS.BitBlt (hDC, px, py, pw, ph, preDC, px, py, OS.SRCCOPY);
		} else {
			OS.BitBlt (hDC, px, py, pw, ph, bufDC, px, py, OS.SRCCOPY);
		}
		GpuCounters.add (GpuCounters.COMPOSITE_PIXELS, (long) pw * ph);
		noteFrameEnd ();
		return true;
	}

	/* ---- damage (RFB update rectangles) ---- */

	/** The single damage adapter: forwards to the installed listener (ViewportDamageRecorder in tests). */
	void notePainted (int x, int y, int w, int h) {
		RetainedPlanes.DamageListener listener = RetainedPlanes.damageListener ();
		if (listener != null) listener.painted (control, x, y, w, h);
	}

	void noteCopied (int srcX, int srcY, int dstX, int dstY, int w, int h) {
		RetainedPlanes.DamageListener listener = RetainedPlanes.damageListener ();
		if (listener != null) listener.copied (control, srcX, srcY, dstX, dstY, w, h);
	}

	void noteFrameEnd () {
		RetainedPlanes.DamageListener listener = RetainedPlanes.damageListener ();
		if (listener != null) listener.frameEnd (control);
	}

	boolean compositeOnlyCovers (int x, int y, int w, int h) {
		for (int i = 0; i < compositeOnly.count (); i++) {
			if (compositeOnly.x0 (i) <= x && compositeOnly.y0 (i) <= y && compositeOnly.x1 (i) >= x + w && compositeOnly.y1 (i) >= y + h) return true;
		}
		return false;
	}

	/** Content changed inside the client rectangle (pixels); off-screen tiles are no longer trustworthy. */
	void damageContent (int x, int y, int w, int h) {
		if (w <= 0 || h <= 0) return;
		damage.add (x, y, x + w, y + h);
		if (grid == null) return;
		grid.damage (x, y, w, h);
		grid.damageOffscreen ();
	}

	/**
	 * Single visibility read for the frame driver. Until the effective-visibility
	 * gate (SWT #124, {@code EffectiveVisibility}) is merged, this asks Windows
	 * directly; afterwards this one method becomes
	 * {@code !EffectiveVisibility.paintGated (control) && !EffectiveVisibility.occluded (control)},
	 * which adds occlusion and cloaking to minimized/hidden.
	 */
	private boolean effectivelyVisible () {
		return OS.IsWindowVisible (control.handle) && !OS.IsIconic (control.getShell ().handle);
	}

	/** Releases tile and buffer memory of a surface hidden longer than {@code swt.gpu.trimMillis}; lazy and bounded. */
	void trimIfHidden () {
		if (bufDC == 0 || lastVisibleNanos == 0 || effectivelyVisible ()) return;
		if (System.nanoTime () - lastVisibleNanos < TRIM_NANOS) return;
		releaseTiles ();
		releaseBuffers ();
		lastVisibleNanos = 0;
		GpuCounters.inc (GpuCounters.TILE_EVICT);
	}

	/** Control.redraw: {@code null} is a full redraw, which makes every retained tile stale. */
	void damageRedraw (RECT damaged) {
		trimIfHidden ();
		if (damaged == null) {
			OS.GetClientRect (control.handle, rect);
			damage.add (0, 0, rect.right, rect.bottom);
			if (grid != null) grid.damageAll ();
		} else {
			damageContent (damaged.left, damaged.top, damaged.right - damaged.left, damaged.bottom - damaged.top);
		}
	}

	/**
	 * Canvas.scroll as RFB CopyRect: the block inside {@code src} moves by
	 * {@code (dx, dy)} on screen (ScrollWindowEx), in the content framebuffer and
	 * in the tile grid; the exposed band is refilled from tiles or damaged.
	 * Pixels outside the block are untouched. Returns false when the caller must
	 * fall back to stock scrolling.
	 */
	boolean scroll (int dx, int dy, RECT src) {
		if (grid == null || contentValid == 0 || bufDC == 0) return false;
		if (damage.count () > 0) {
			/* queued damage is in pre-scroll coordinates: re-raster everything after the stock scroll */
			contentValid = 0;
			return false;
		}
		OS.GetClientRect (control.handle, rect);
		int cw = rect.right, ch = rect.bottom;
		int sx0 = Math.max (src.left, 0), sy0 = Math.max (src.top, 0), sx1 = Math.min (src.right, cw), sy1 = Math.min (src.bottom, ch);
		if (sx0 >= sx1 || sy0 >= sy1) return false;
		int mx0 = Math.max (sx0 + dx, 0), my0 = Math.max (sy0 + dy, 0), mx1 = Math.min (sx1 + dx, cw), my1 = Math.min (sy1 + dy, ch);
		if (mx0 < mx1 && my0 < my1) {
			copyContent (mx0 - dx, my0 - dy, mx0, my0, mx1 - mx0, my1 - my0);
			noteCopied (mx0 - dx, my0 - dy, mx0, my0, mx1 - mx0, my1 - my0);
		}
		/* tiles that held fixed pixels (outside the block) would scroll with content: invalidate them first */
		int n = Rects.subtract (0, 0, cw, ch, Math.min (sx0, sx0 + dx), Math.min (sy0, sy0 + dy), Math.max (sx1, sx1 + dx) - Math.min (sx0, sx0 + dx), Math.max (sy1, sy1 + dy) - Math.min (sy0, sy0 + dy), parts);
		if (dx != 0 && dy != 0) n = Rects.subtract (0, 0, cw, ch, sx0, sy0, sx1 - sx0, sy1 - sy0, parts);
		for (int i = 0; i < n; i++) {
			/*
			 * A strip spanning the whole client height (a fixed margin beside a vertical scroll) is
			 * invalidated for the whole column in content space, so each tile keeps one full-height
			 * valid rectangle instead of losing rows to the single-rectangle validity rule; likewise
			 * full-width strips for horizontal scrolls.
			 */
			int x = parts [i * 4], y = parts [i * 4 + 1], w = parts [i * 4 + 2], h = parts [i * 4 + 3];
			if (h == ch) { y = -(1 << 28); h = 1 << 29; }
			if (w == cw) { x = -(1 << 28); w = 1 << 29; }
			grid.damage (x, y, w, h);
		}
		grid.translate (dx, dy);
		GpuCounters.inc (GpuCounters.SCROLL_TRANSLATES);
		int hasOverlay = flags & HAS_OVERLAY;
		n = Rects.subtract (sx0, sy0, sx1 - sx0, sy1 - sy0, mx0, my0, Math.max (0, mx1 - mx0), Math.max (0, my1 - my0), parts);
		for (int i = 0; i < n; i++) {
			int x = parts [i * 4], y = parts [i * 4 + 1], w = parts [i * 4 + 2], h = parts [i * 4 + 3];
			/* refill is pixel-exact only at integral zoom; fractional zoom rounds content per position, so raster as stock does */
			/* the band is known damage, not an external change: its WM_PAINT must not drop off-screen tiles */
			compositeOnly.add (x, y, x + w, y + h);
			if (control.getAutoscalingZoom () % 100 != 0 || !refillFromTiles (x, y, w, h)) damage.add (x, y, x + w, y + h);
		}
		if (hasOverlay == 0) {
			/* the screen moves exactly as stock; WM_PAINT then covers only the exposed band */
			OS.ScrollWindowEx (control.handle, dx, dy, src, null, 0, null, OS.SW_INVALIDATE | OS.SW_ERASE);
		} else {
			/* overlays are composed per frame, so the whole client is presented from the present buffer */
			compositeOnly.add (0, 0, cw, ch);
			OS.InvalidateRect (control.handle, null, false);
		}
		return true;
	}

	/** Overlay planes changed inside the rectangle (pixels); content and tiles stay valid. */
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
			releaseBuffers ();
			if (!createBuffers (Math.max (cw, bufW), Math.max (ch, bufH))) return false;
			contentValid = 0;
		}
		if (clipRgn == 0) clipRgn = OS.CreateRectRgn (0, 0, 0, 0);
		if (gpu == null && (RetainedPlanes.MODE & RetainedPlanes.MODE_SKIJA) != 0 && (flags & CONTENT_GDI) == 0) gpu = createTarget ();
		if (gpu != null) {
			int state;
			try {
				state = gpu.beginFrame (cw, ch);
			} catch (Throwable failure) {
				state = 0;
			}
			if (state == 0) return false;
			if (state == 2) contentValid = 0;
		}
		if (gpu == null && atlasDC == 0) return createAtlas ();
		return true;
	}

	PlaneBackend.Target createTarget () {
		PlaneBackend backend = control.display.gpuBackend ();
		if (backend == null) {
			flags |= CONTENT_GDI;
			return null;
		}
		try {
			return backend.createTarget (grid.capacity ());
		} catch (Throwable failure) {
			GpuCounters.inc (GpuCounters.FALLBACKS);
			flags |= CONTENT_GDI;
			return null;
		}
	}

	boolean createBuffers (int w, int h) {
		long [] bits = new long [1];
		bufBitmap = createDib (w, h, bits);
		if (bufBitmap == 0) return false;
		bufBits = bits [0];
		bufDC = OS.CreateCompatibleDC (0);
		bufOld = OS.SelectObject (bufDC, bufBitmap);
		preBitmap = createDib (w, h, bits);
		if (preBitmap == 0) return false;
		preBits = bits [0];
		preDC = OS.CreateCompatibleDC (0);
		preOld = OS.SelectObject (preDC, preBitmap);
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
		contentValid = 0;
	}

	void releaseBuffers () {
		if (bufDC != 0) {
			OS.SelectObject (bufDC, bufOld);
			OS.DeleteDC (bufDC);
			OS.DeleteObject (bufBitmap);
			bufDC = bufBitmap = bufOld = bufBits = 0;
		}
		if (preDC != 0) {
			OS.SelectObject (preDC, preOld);
			OS.DeleteDC (preDC);
			OS.DeleteObject (preBitmap);
			preDC = preBitmap = preOld = preBits = 0;
		}
		bufW = bufH = 0;
		contentValid = 0;
	}

	void dispose () {
		releaseTiles ();
		releaseBuffers ();
		if (clipRgn != 0) {
			OS.DeleteObject (clipRgn);
			clipRgn = 0;
		}
	}

	/* ---- raster (RFB Raw) and tile cache ---- */

	/** Rasterizes the coalesced damage once: recorded and replayed on the GPU, or painted with GDI. */
	boolean rasterDamage (int cw, int ch) {
		int recorded = 0;
		if (gpu != null && backgroundReplayable ()) {
			/* after a failed recording, rasterize with GDI for a while before trying the GPU again */
			if (recordBackoff > 0) recordBackoff--; else recorded = recordContent ();
		}
		int background = (control.style & SWT.NO_BACKGROUND) == 0 ? 1 : 0;
		try {
			for (int i = 0; i < damage.count (); i++) {
				int x0 = Math.max (0, damage.x0 (i)), y0 = Math.max (0, damage.y0 (i)), x1 = Math.min (cw, damage.x1 (i)), y1 = Math.min (ch, damage.y1 (i));
				if (x0 >= x1 || y0 >= y1) continue;
				if (recorded != 0) {
					if (!gpu.rasterFrame (x0, y0, x1 - x0, y1 - y0, bgArgb, ops)) return false;
				} else {
					paintPass (bufDC, x0, y0, x1, y1, RetainedPlanes.RETAINED, null, background);
					if (gpu != null) {
						OS.GetPixel (bufDC, 0, 0); // a GDI call returning a value flushes the GDI batch before the CPU reads the DIB
						if (!gpu.uploadFrame (bufBits, bufW * 4, x0, y0, x1 - x0, y1 - y0)) return false;
					}
				}
				notePainted (x0, y0, x1 - x0, y1 - y0);
				GpuCounters.add (GpuCounters.RASTER_PIXELS, (long) (x1 - x0) * (y1 - y0));
			}
		} catch (RuntimeException | Error failure) {
			if (gpu == null) throw failure;
			return false; // GPU error: fail closed
		}
		return true;
	}

	/** Records all damage rectangles into {@link #ops}; returns 1 when the stream is replayable. */
	int recordContent () {
		ops.reset ();
		for (int i = 0; i < damage.count (); i++) {
			ops.clipReset ();
			paintPass (bufDC, damage.x0 (i), damage.y0 (i), damage.x1 (i), damage.y1 (i), RetainedPlanes.RETAINED, ops, 0);
		}
		if (ops.fail == 0) {
			if (DEBUG_OPS) debugOps ();
			GpuCounters.add (GpuCounters.GPU_RASTER_TILES, damage.count ());
			return 1;
		}
		GpuCounters.inc (GpuCounters.RECORD_FAILS);
		GpuCounters.set (GpuCounters.RECORD_FAIL_BITS, GpuCounters.get (GpuCounters.RECORD_FAIL_BITS) | ops.fail);
		recordBackoff = RECORD_BACKOFF_FRAMES;
		return 0;
	}

	/** Diagnostic only (-Dswt.gpu.debugOps): op-type histogram of a recorded content pass. */
	void debugOps () {
		if (debugFrames++ % 50 != 0) return;
		int [] histogram = new int [32];
		int [] a = ops.ops ();
		for (int i = 0; i < ops.size (); i = i + 1 + OpStream.lengthOf (a [i])) histogram [OpStream.opOf (a [i]) & 31]++;
		System.err.println ("swt.gpu ops=" + ops.size () + " histogram=" + java.util.Arrays.toString (histogram) + " rects=" + damage.count ());
	}

	/** Copies the freshly rasterized rectangles into the content-space tile cache. */
	void storeTiles () {
		for (int i = 0; i < damage.count (); i++) {
			int x0 = Math.max (0, damage.x0 (i)), y0 = Math.max (0, damage.y0 (i));
			int x1 = Math.min (grid.clientWidth (), damage.x1 (i)), y1 = Math.min (grid.clientHeight (), damage.y1 (i));
			if (x0 >= x1 || y0 >= y1) continue;
			int ox = grid.originX (), oy = grid.originY ();
			int tx1 = (x1 - 1 + ox) >> TileGrid.SHIFT, ty1 = (y1 - 1 + oy) >> TileGrid.SHIFT;
			for (int ty = (y0 + oy) >> TileGrid.SHIFT; ty <= ty1; ty++) {
				for (int tx = (x0 + ox) >> TileGrid.SHIFT; tx <= tx1; tx++) {
					int slot = grid.acquire (tx, ty);
					if (slot < 0) continue;
					int cx = grid.clientX (slot), cy = grid.clientY (slot);
					int px0 = Math.max (x0, cx), py0 = Math.max (y0, cy), px1 = Math.min (x1, cx + CELL), py1 = Math.min (y1, cy + CELL);
					if (px0 >= px1 || py0 >= py1 || !storeTile (slot, cx, cy, px0, py0, px1 - px0, py1 - py0)) continue;
					grid.rastered (slot, px0, py0, px1 - px0, py1 - py0);
				}
			}
		}
	}

	boolean storeTile (int slot, int cx, int cy, int x, int y, int w, int h) {
		if (gpu == null) {
			return OS.BitBlt (atlasDC, (slot % ATLAS_COLUMNS) * CELL + x - cx, (slot / ATLAS_COLUMNS) * CELL + y - cy, w, h, bufDC, x, y, OS.SRCCOPY);
		}
		try {
			return gpu.copyFrameToTile (slot, cx, cy, x - cx, y - cy, w, h);
		} catch (Throwable failure) {
			return false;
		}
	}

	/** RFB cache hit: copies the band from valid tiles into the content framebuffer; false when any part is not cached. */
	boolean refillFromTiles (int x, int y, int w, int h) {
		if (w <= 0 || h <= 0) return true;
		int ox = grid.originX (), oy = grid.originY ();
		int tx0 = (x + ox) >> TileGrid.SHIFT, ty0 = (y + oy) >> TileGrid.SHIFT, tx1 = (x + w - 1 + ox) >> TileGrid.SHIFT, ty1 = (y + h - 1 + oy) >> TileGrid.SHIFT;
		for (int ty = ty0; ty <= ty1; ty++) {
			for (int tx = tx0; tx <= tx1; tx++) {
				int slot = grid.peekSlot (tx, ty);
				if (slot < 0 || !grid.covers (slot, x, y, w, h)) return false;
			}
		}
		for (int ty = ty0; ty <= ty1; ty++) {
			for (int tx = tx0; tx <= tx1; tx++) {
				int slot = grid.peekSlot (tx, ty);
				int cx = grid.clientX (slot), cy = grid.clientY (slot);
				int px0 = Math.max (x, cx), py0 = Math.max (y, cy), px1 = Math.min (x + w, cx + CELL), py1 = Math.min (y + h, cy + CELL);
				if (gpu == null) {
					OS.BitBlt (bufDC, px0, py0, px1 - px0, py1 - py0, atlasDC, (slot % ATLAS_COLUMNS) * CELL + px0 - cx, (slot / ATLAS_COLUMNS) * CELL + py0 - cy, OS.SRCCOPY);
				} else if (!gpu.copyTileToFrame (slot, cx, cy, px0 - cx, py0 - cy, px1 - px0, py1 - py0)) {
					return false;
				}
				grid.slots ().touch (slot);
				GpuCounters.inc (GpuCounters.TILE_REUSED);
			}
		}
		noteCopied (x, y, x, y, w, h);
		return true;
	}

	/** RFB CopyRect on the content framebuffer. */
	void copyContent (int srcX, int srcY, int dstX, int dstY, int w, int h) {
		if (gpu == null) {
			OS.BitBlt (bufDC, dstX, dstY, w, h, bufDC, srcX, srcY, OS.SRCCOPY);
			return;
		}
		boolean ok;
		try {
			ok = gpu.copyFrame (srcX, srcY, dstX, dstY, w, h);
		} catch (Throwable failure) {
			ok = false;
		}
		if (!ok) contentValid = 0;
	}

	boolean backgroundReplayable () {
		if ((control.style & SWT.NO_BACKGROUND) != 0) {
			/* NO_BACKGROUND: no fill; pixels the widget does not paint keep the framebuffer's previous content, as on GDI */
			bgArgb = 0;
			return true;
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
		return true;
	}

	/**
	 * Control.drawBackground(GC, ...) paints on the HDC directly; inside a retained
	 * pass it is gated by plane and recorded as a fill (images and themes are not
	 * replayable). Mirrors {@code Control.drawBackground(long, RECT, int, int, int)}.
	 */
	static boolean divertBackground (Control control, GCData data, int x, int y, int width, int height, int pixel) {
		if ((data.plane & data.planeDraw) == 0) return true;
		OpStream sink = (OpStream) data.planeSink;
		if (sink == null) return false;
		Control background = control.findBackgroundControl ();
		if (background != null) {
			if (background.backgroundImage != null) {
				sink.fail |= OpStream.FAIL_IMAGE;
				return true;
			}
			pixel = background.getBackgroundPixel ();
		}
		if (pixel == -1 && (control.state & Widget.THEME_BACKGROUND) != 0 && control.findThemeControl () != null) {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
			return true;
		}
		if (pixel == -1) pixel = control.getBackgroundPixel ();
		sink.rect (OpStream.FILL_RECT, x, y, width, height, PaintKeys.argbFromColorref (pixel, 0xFF), FILL_MASK);
		return true;
	}

	/**
	 * Sends one Paint event for the client rectangle into {@code hdc} with the
	 * given plane mask; {@code sink} non-null records instead of drawing.
	 */
	void paintPass (long hdc, int x0, int y0, int x1, int y1, int planeDraw, OpStream sink, int background) {
		int saved = OS.SaveDC (hdc);
		OS.SetRectRgn (clipRgn, x0, y0, x1, y1);
		OS.SelectClipRgn (hdc, clipRgn);
		OS.SetMetaRgn (hdc);
		if (background != 0) {
			OS.SetRect (fill, x0, y0, x1, y1);
			control.drawBackground (hdc, fill);
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
		GC gc = control.createNewGC (hdc, data);
		try {
			Event event = new Event ();
			event.gc = gc;
			event.setBounds (Win32DPIUtils.pixelToPoint (new Rectangle (x0, y0, x1 - x0, y1 - y0), control.getAutoscalingZoom ()));
			control.sendEvent (SWT.Paint, event);
			event.gc = null;
		} finally {
			if (!gc.isDisposed ()) gc.dispose ();
			OS.RestoreDC (hdc, saved);
		}
		if ((data.retainedMask & GCData.RETAIN_PLANES_USED) != 0) flags |= HAS_OVERLAY;
	}

	/* ---- overlay planes and present ---- */

	/** Overlay planes on the GPU present surface; false when they must be drawn with GDI after the readback. */
	boolean overlayGpu (int px, int py, int pw, int ph) {
		GpuCounters.inc (GpuCounters.OVERLAY_FRAMES);
		if ((flags & OVERLAY_GDI) != 0) return false;
		ops.reset ();
		ops.clipReset ();
		overlayPass (preDC, px, py, pw, ph, ops);
		try {
			if (ops.fail == 0 && gpu.overlay (ops, px, py, pw, ph)) return true;
		} catch (Throwable failure) {
			return false;
		}
		flags |= OVERLAY_GDI;
		return false;
	}

	void overlayPass (long hdc, int px, int py, int pw, int ph, OpStream sink) {
		paintPass (hdc, px, py, px + pw, py + ph, RetainedPlanes.OVERLAY, sink, 0);
	}

	boolean gpuPresent (int px, int py, int pw, int ph, int fromPresent) {
		long start = System.nanoTime ();
		try {
			return gpu.present (preBits, bufW * 4, px, py, pw, ph, fromPresent);
		} catch (Throwable failure) {
			return false; // fail closed
		} finally {
			GpuCounters.add (GpuCounters.GPU_WAIT_NANOS, System.nanoTime () - start);
		}
	}
}

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
package org.eclipse.swt.internal.gpu.skija;

import org.eclipse.swt.internal.gpu.*;

import io.github.humbleui.skija.*;
import io.github.humbleui.types.*;

/**
 * Per-widget GPU target in the remote-framebuffer model: a content render
 * target (the widget's retained planes), a present render target (content plus
 * overlay planes) and a pool of tile render targets indexed by tile slot
 * (reused across evictions, never reallocated per frame). Every copy is an
 * integer-offset blit with source blending ({@code SRC}), so tiles meet at exact
 * device-pixel seams and nothing is filtered. Replay uses Skija's primitive
 * native entry points on canvas pointers; only cache misses allocate.
 */
final class SkijaTarget implements PlaneBackend.Target {
	static final int TILE = TileGrid.SIZE;
	static final int INTERSECT = ClipMode.INTERSECT.ordinal (), SRC = BlendMode.SRC.ordinal ();
	static final int TEXT_MASK = PaintKeys.mask (PaintKeys.KIND_TEXT, 0, 0, 0, -1, 0, 0, PaintKeys.BACKEND_SKIJA);

	final SkijaPlaneBackend backend;
	final Surface [] tiles;
	Surface frame, present;
	int frameW, frameH;
	final Pixmap pixmap = new Pixmap ();
	ImageInfo info;
	int infoW, infoH;

	SkijaTarget (SkijaPlaneBackend backend, int capacity) {
		this.backend = backend;
		tiles = new Surface [capacity];
	}

	ImageInfo info (int w, int h) {
		if (info == null || infoW != w || infoH != h) {
			info = new ImageInfo (w, h, ColorType.BGRA_8888, ColorAlphaType.OPAQUE);
			infoW = w;
			infoH = h;
		}
		return info;
	}

	Surface target (int w, int h) {
		Surface surface = Surface.makeRenderTarget (backend.context, false, new ImageInfo (w, h, ColorType.BGRA_8888, ColorAlphaType.OPAQUE), 0, SurfaceOrigin.TOP_LEFT, backend.lcdProps, false);
		GpuCounters.add (GpuCounters.GPU_TILE_BYTES, 4L * w * h);
		GpuCounters.inc (GpuCounters.NATIVE_CREATES);
		return surface;
	}

	static void close (Surface surface, int w, int h) {
		if (surface == null) return;
		surface.close ();
		GpuCounters.add (GpuCounters.GPU_TILE_BYTES, -4L * w * h);
	}

	@Override
	public int beginFrame (int w, int h) {
		backend.makeCurrent ();
		if (frame != null && frameW >= w && frameH >= h) return 1;
		close (frame, frameW, frameH);
		close (present, frameW, frameH);
		frameW = Math.max (w, frameW);
		frameH = Math.max (h, frameH);
		frame = target (frameW, frameH);
		present = target (frameW, frameH);
		return frame != null && present != null ? 2 : 0;
	}

	Surface tile (int slot) {
		Surface tile = tiles [slot];
		if (tile == null) tile = tiles [slot] = target (TILE, TILE);
		return tile;
	}

	@Override
	public boolean rasterFrame (int x, int y, int w, int h, int background, OpStream ops) {
		long canvas = frame.getCanvas ()._ptr;
		int save = Canvas._nSave (canvas);
		Canvas._nClipRect (canvas, x, y, x + w, y + h, INTERSECT, false);
		if ((background >>> 24) != 0) Canvas._nDrawColor (canvas, background, SRC);
		try {
			Replay.run (backend, canvas, ops);
		} finally {
			Canvas._nRestoreToCount (canvas, save);
		}
		return true;
	}

	@Override
	public boolean uploadFrame (long bits, int stride, int x, int y, int w, int h) {
		pixmap.reset (info (w, h), bits + (long) y * stride + x * 4L, stride);
		frame.writePixels (pixmap, x, y);
		return true;
	}

	@Override
	public boolean copyFrame (int srcX, int srcY, int dstX, int dstY, int w, int h) {
		/* RFB CopyRect within one surface: snapshot the source block, then an integer-offset SRC blit */
		try (Image block = frame.makeImageSnapshot (IRect.makeXYWH (srcX, srcY, w, h))) {
			if (block == null) return false;
			io.github.humbleui.skija.Canvas canvas = frame.getCanvas ();
			int save = Canvas._nSave (canvas._ptr);
			Canvas._nClipRect (canvas._ptr, dstX, dstY, dstX + w, dstY + h, INTERSECT, false);
			canvas.drawImage (block, dstX, dstY, backend.srcPaint);
			Canvas._nRestoreToCount (canvas._ptr, save);
		}
		return true;
	}

	@Override
	public boolean copyFrameToTile (int slot, int tileClientX, int tileClientY, int localX, int localY, int w, int h) {
		Surface tile = tile (slot);
		long canvas = tile.getCanvas ()._ptr;
		int save = Canvas._nSave (canvas);
		Canvas._nClipRect (canvas, localX, localY, localX + w, localY + h, INTERSECT, false);
		try {
			frame.draw (tile.getCanvas (), -tileClientX, -tileClientY, backend.srcPaint);
		} finally {
			Canvas._nRestoreToCount (canvas, save);
		}
		return true;
	}

	@Override
	public boolean copyTileToFrame (int slot, int tileClientX, int tileClientY, int localX, int localY, int w, int h) {
		Surface tile = tiles [slot];
		if (tile == null) return false;
		long canvas = frame.getCanvas ()._ptr;
		int save = Canvas._nSave (canvas);
		Canvas._nClipRect (canvas, tileClientX + localX, tileClientY + localY, tileClientX + localX + w, tileClientY + localY + h, INTERSECT, false);
		try {
			tile.draw (frame.getCanvas (), tileClientX, tileClientY, backend.srcPaint);
		} finally {
			Canvas._nRestoreToCount (canvas, save);
		}
		return true;
	}

	@Override
	public void tileEvicted (int slot) {
		/* the render target stays in the pool; the next tile in this slot overwrites it */
	}

	@Override
	public boolean overlay (OpStream ops, int x, int y, int w, int h) {
		long canvas = present.getCanvas ()._ptr;
		int save = Canvas._nSave (canvas);
		Canvas._nClipRect (canvas, x, y, x + w, y + h, INTERSECT, false);
		try {
			frame.draw (present.getCanvas (), 0, 0, backend.srcPaint);
			if (ops != null) Replay.run (backend, canvas, ops);
		} finally {
			Canvas._nRestoreToCount (canvas, save);
		}
		return true;
	}

	@Override
	public boolean present (long bits, int stride, int x, int y, int w, int h, int fromPresent) {
		backend.context.flush ();
		pixmap.reset (info (w, h), bits + (long) y * stride + x * 4L, stride);
		return (fromPresent != 0 ? present : frame).readPixels (pixmap, x, y);
	}

	@Override
	public long gpuBytes () {
		long bytes = frame != null ? 8L * frameW * frameH : 0;
		for (Surface tile : tiles) if (tile != null) bytes += 4L * TILE * TILE;
		return bytes;
	}

	@Override
	public void dispose () {
		backend.makeCurrent ();
		for (int i = 0; i < tiles.length; i++) {
			close (tiles [i], TILE, TILE);
			tiles [i] = null;
		}
		close (frame, frameW, frameH);
		close (present, frameW, frameH);
		frame = present = null;
		pixmap.close ();
	}
}

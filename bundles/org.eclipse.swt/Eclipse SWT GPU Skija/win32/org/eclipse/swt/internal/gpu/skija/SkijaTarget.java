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
package org.eclipse.swt.internal.gpu.skija;

import org.eclipse.swt.internal.gpu.*;

import io.github.humbleui.skija.*;

/**
 * Per-widget GPU target: a pool of tile render targets indexed by tile slot
 * (reused across evictions, never reallocated per frame) and one frame render
 * target of the client size. Replay uses Skija's primitive native entry points
 * on the canvas pointer; only cache misses allocate.
 */
final class SkijaTarget implements PlaneBackend.Target {
	static final int TILE = TileGrid.SIZE;
	static final int INTERSECT = ClipMode.INTERSECT.ordinal (), SRC = BlendMode.SRC.ordinal ();
	static final int TEXT_MASK = PaintKeys.mask (PaintKeys.KIND_TEXT, 0, 0, 0, -1, 0, 0, PaintKeys.BACKEND_SKIJA);

	final SkijaPlaneBackend backend;
	final Surface [] tiles;
	Surface frame;
	int frameW, frameH;
	final Pixmap pixmap = new Pixmap ();
	ImageInfo info;
	int infoW, infoH;
	final float [] matrix = {1, 0, 0, 0, 1, 0, 0, 0, 1};

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

	@Override
	public boolean beginFrame (int w, int h) {
		backend.makeCurrent ();
		if (frame == null || frameW < w || frameH < h) {
			if (frame != null) {
				frame.close ();
				GpuCounters.add (GpuCounters.GPU_TILE_BYTES, -4L * frameW * frameH);
			}
			frameW = Math.max (w, frameW);
			frameH = Math.max (h, frameH);
			frame = Surface.makeRenderTarget (backend.context, false, new ImageInfo (frameW, frameH, ColorType.BGRA_8888, ColorAlphaType.OPAQUE));
			GpuCounters.add (GpuCounters.GPU_TILE_BYTES, 4L * frameW * frameH);
		}
		return frame != null;
	}

	Surface tile (int slot) {
		Surface tile = tiles [slot];
		if (tile == null) {
			tile = tiles [slot] = Surface.makeRenderTarget (backend.context, false, new ImageInfo (TILE, TILE, ColorType.BGRA_8888, ColorAlphaType.OPAQUE));
			GpuCounters.add (GpuCounters.GPU_TILE_BYTES, 4L * TILE * TILE);
			GpuCounters.inc (GpuCounters.NATIVE_CREATES);
		}
		return tile;
	}

	@Override
	public boolean uploadTile (int slot, long bits, int stride, int srcX, int srcY, int dstX, int dstY, int w, int h) {
		Surface tile = tile (slot);
		pixmap.reset (info (w, h), bits + (long) srcY * stride + srcX * 4L, stride);
		tile.writePixels (pixmap, dstX, dstY);
		return true;
	}

	@Override
	public boolean rasterTile (int slot, int tileClientX, int tileClientY, int localX, int localY, int w, int h, int background, OpStream ops) {
		long canvas = tile (slot).getCanvas ()._ptr;
		int save = Canvas._nSave (canvas);
		Canvas._nClipRect (canvas, localX, localY, localX + w, localY + h, INTERSECT, false);
		Canvas._nDrawColor (canvas, background, SRC);
		matrix [2] = -tileClientX;
		matrix [5] = -tileClientY;
		Canvas._nConcat (canvas, matrix);
		try {
			Replay.run (backend, canvas, ops);
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
	public void composite (int slot, int x, int y) {
		Surface tile = tiles [slot];
		if (tile != null) tile.draw (frame.getCanvas (), x, y, backend.srcPaint);
	}

	@Override
	public boolean replay (OpStream ops, int clipX, int clipY, int clipW, int clipH) {
		long canvas = frame.getCanvas ()._ptr;
		int save = Canvas._nSave (canvas);
		Canvas._nClipRect (canvas, clipX, clipY, clipX + clipW, clipY + clipH, INTERSECT, false);
		try {
			Replay.run (backend, canvas, ops);
		} finally {
			Canvas._nRestoreToCount (canvas, save);
		}
		return true;
	}

	@Override
	public boolean present (long bits, int stride, int x, int y, int w, int h) {
		backend.context.flush ();
		pixmap.reset (info (w, h), bits + (long) y * stride + x * 4L, stride);
		return frame.readPixels (pixmap, x, y);
	}

	@Override
	public boolean swap (long hwnd) {
		return false;
	}

	@Override
	public long gpuBytes () {
		long bytes = frame != null ? 4L * frameW * frameH : 0;
		for (Surface tile : tiles) if (tile != null) bytes += 4L * TILE * TILE;
		return bytes;
	}

	@Override
	public void dispose () {
		backend.makeCurrent ();
		for (int i = 0; i < tiles.length; i++) {
			if (tiles [i] != null) {
				tiles [i].close ();
				tiles [i] = null;
				GpuCounters.add (GpuCounters.GPU_TILE_BYTES, -4L * TILE * TILE);
			}
		}
		if (frame != null) {
			frame.close ();
			GpuCounters.add (GpuCounters.GPU_TILE_BYTES, -4L * frameW * frameH);
			frame = null;
		}
		pixmap.close ();
	}
}

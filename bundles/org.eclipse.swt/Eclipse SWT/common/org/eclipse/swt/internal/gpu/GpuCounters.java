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
package org.eclipse.swt.internal.gpu;

/**
 * Process-wide retained-layer counters in one {@code long[]}, indexed by int
 * ids. UI-thread confined; read by tests and benchmarks through
 * {@link #snapshot(long[])}. Incrementing never allocates.
 */
public final class GpuCounters {
	public static final int PEN_HIT = 0, PEN_MISS = 1, BRUSH_HIT = 2, BRUSH_MISS = 3;
	public static final int GDIP_BRUSH_HIT = 4, GDIP_BRUSH_MISS = 5, GDIP_FONT_HIT = 6, GDIP_FONT_MISS = 7;
	public static final int FONT_HIT = 8, FONT_MISS = 9, PAINT_HIT = 10, PAINT_MISS = 11;
	public static final int BLOB_HIT = 12, BLOB_MISS = 13, TILE_REUSED = 14, TILE_RASTER = 15;
	public static final int TILE_EVICT = 16, FRAMES = 17, OVERLAY_FRAMES = 18, FALLBACKS = 19;
	public static final int RASTER_PIXELS = 20, COMPOSITE_PIXELS = 21, GPU_TILE_BYTES = 22, CPU_TILE_BYTES = 23;
	public static final int SCROLL_TRANSLATES = 24, GPU_RASTER_TILES = 25, OPS_RECORDED = 26, NATIVE_CREATES = 27;
	public static final int COUNT = 28;

	public static final String [] NAMES = {
		"pen.hit", "pen.miss", "brush.hit", "brush.miss",
		"gdipBrush.hit", "gdipBrush.miss", "gdipFont.hit", "gdipFont.miss",
		"font.hit", "font.miss", "paint.hit", "paint.miss",
		"blob.hit", "blob.miss", "tile.reused", "tile.raster",
		"tile.evict", "frames", "overlayFrames", "fallbacks",
		"rasterPixels", "compositePixels", "gpuTileBytes", "cpuTileBytes",
		"scrollTranslates", "gpuRasterTiles", "opsRecorded", "nativeCreates",
	};

	static final long [] VALUES = new long [COUNT];

	private GpuCounters () {
	}

	public static void inc (int id) {
		VALUES [id]++;
	}

	public static void add (int id, long amount) {
		VALUES [id] += amount;
	}

	public static void set (int id, long value) {
		VALUES [id] = value;
	}

	public static long get (int id) {
		return VALUES [id];
	}

	/** Copies all counters into {@code out} (length {@link #COUNT}). */
	public static void snapshot (long [] out) {
		System.arraycopy (VALUES, 0, out, 0, COUNT);
	}

	public static void reset () {
		java.util.Arrays.fill (VALUES, 0L);
	}

	/** Hit rate in percent for a hit/miss pair, or -1 when there were no lookups. */
	public static double hitRate (long [] values, int hitId) {
		long hits = values [hitId], total = hits + values [hitId + 1];
		return total == 0 ? -1 : 100.0 * hits / total;
	}
}

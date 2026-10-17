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
package org.eclipse.swt.internal.gpu;

/**
 * Half-open integer rectangle operations over packed {@code (x0, y0, x1, y1)}
 * quads stored in caller-owned {@code int[]} at an offset. Allocation free.
 */
public final class Rects {
	private Rects () {
	}

	public static void empty (int [] r, int o) {
		r [o] = 0; r [o + 1] = 0; r [o + 2] = 0; r [o + 3] = 0;
	}

	public static boolean isEmpty (int [] r, int o) {
		return r [o] >= r [o + 2] || r [o + 1] >= r [o + 3];
	}

	static long area (int x0, int y0, int x1, int y1) {
		return x0 >= x1 || y0 >= y1 ? 0 : (long) (x1 - x0) * (y1 - y0);
	}

	private static void put (int [] r, int o, int x0, int y0, int x1, int y1) {
		r [o] = x0; r [o + 1] = y0; r [o + 2] = x1; r [o + 3] = y1;
	}

	/**
	 * Replaces {@code r[o..o+3]} with {@code r ∪ b} when that union is a rectangle,
	 * otherwise with the larger of the two (validity stays conservative).
	 */
	public static void unionOrLargest (int [] r, int o, int x0, int y0, int x1, int y1) {
		if (x0 >= x1 || y0 >= y1) return;
		int a0 = r [o], b0 = r [o + 1], a1 = r [o + 2], b1 = r [o + 3];
		if (a0 >= a1 || b0 >= b1 || (x0 <= a0 && y0 <= b0 && x1 >= a1 && y1 >= b1)) {
			put (r, o, x0, y0, x1, y1);
		} else if (a0 <= x0 && b0 <= y0 && a1 >= x1 && b1 >= y1) {
			return;
		} else if (a0 == x0 && a1 == x1 && y0 <= b1 && b0 <= y1) {
			put (r, o, a0, Math.min (b0, y0), a1, Math.max (b1, y1));
		} else if (b0 == y0 && b1 == y1 && x0 <= a1 && a0 <= x1) {
			put (r, o, Math.min (a0, x0), b0, Math.max (a1, x1), b1);
		} else if (area (x0, y0, x1, y1) > area (a0, b0, a1, b1)) {
			put (r, o, x0, y0, x1, y1);
		}
	}

	/** Replaces {@code r} with the largest rectangular piece of {@code r \ d}. */
	public static void subtractKeepLargest (int [] r, int o, int x0, int y0, int x1, int y1) {
		int a0 = r [o], b0 = r [o + 1], a1 = r [o + 2], b1 = r [o + 3];
		if (a0 >= a1 || b0 >= b1 || x1 <= a0 || x0 >= a1 || y1 <= b0 || y0 >= b1) return;
		long top = area (a0, b0, a1, y0), bottom = area (a0, y1, a1, b1);
		int my0 = Math.max (b0, y0), my1 = Math.min (b1, y1);
		long left = area (a0, my0, x0, my1), right = area (x1, my0, a1, my1);
		long best = Math.max (Math.max (top, bottom), Math.max (left, right));
		if (best == 0) {
			empty (r, o);
		} else if (best == top) {
			put (r, o, a0, b0, a1, y0);
		} else if (best == bottom) {
			put (r, o, a0, y1, a1, b1);
		} else if (best == left) {
			put (r, o, a0, my0, x0, my1);
		} else {
			put (r, o, x1, my0, a1, my1);
		}
	}

	/**
	 * Bounding box of {@code v \ r} (v given as v0..v1) written to {@code out}
	 * as x, y, w, h. Returns false when the difference is empty.
	 */
	public static boolean differenceBounds (int v0, int w0, int v1, int w1, int [] r, int o, int [] out) {
		int a0 = Math.max (v0, r [o]), b0 = Math.max (w0, r [o + 1]);
		int a1 = Math.min (v1, r [o + 2]), b1 = Math.min (w1, r [o + 3]);
		if (a0 >= a1 || b0 >= b1) {
			out [0] = v0; out [1] = w0; out [2] = v1 - v0; out [3] = w1 - w0;
			return true;
		}
		if (a0 == v0 && b0 == w0 && a1 == v1 && b1 == w1) return false;
		int x0 = v1, y0 = w1, x1 = v0, y1 = w0;
		if (b0 > w0) { x0 = v0; y0 = w0; x1 = v1; y1 = Math.max (y1, b0); }
		if (b1 < w1) { x0 = v0; x1 = v1; y0 = Math.min (y0, b1); y1 = w1; }
		if (a0 > v0) { x0 = v0; y0 = Math.min (y0, b0); x1 = Math.max (x1, a0); y1 = Math.max (y1, b1); }
		if (a1 < v1) { x0 = Math.min (x0, a1); y0 = Math.min (y0, b0); x1 = v1; y1 = Math.max (y1, b1); }
		out [0] = x0; out [1] = y0; out [2] = x1 - x0; out [3] = y1 - y0;
		return true;
	}

	/**
	 * Writes up to four rectangles of {@code (outer) \ (inner)} as x, y, w, h into
	 * {@code out} starting at 0 and returns how many were written.
	 */
	public static int subtract (int ox, int oy, int ow, int oh, int ix, int iy, int iw, int ih, int [] out) {
		int ox1 = ox + ow, oy1 = oy + oh;
		int x0 = Math.max (ox, ix), y0 = Math.max (oy, iy), x1 = Math.min (ox1, ix + iw), y1 = Math.min (oy1, iy + ih);
		int n = 0;
		if (x0 >= x1 || y0 >= y1) return emit (out, n, ox, oy, ow, oh);
		if (y0 > oy) n = emit (out, n, ox, oy, ow, y0 - oy);
		if (y1 < oy1) n = emit (out, n, ox, y1, ow, oy1 - y1);
		if (x0 > ox) n = emit (out, n, ox, y0, x0 - ox, y1 - y0);
		if (x1 < ox1) n = emit (out, n, x1, y0, ox1 - x1, y1 - y0);
		return n;
	}

	private static int emit (int [] out, int n, int x, int y, int w, int h) {
		if (w <= 0 || h <= 0) return n;
		int o = n * 4;
		out [o] = x; out [o + 1] = y; out [o + 2] = w; out [o + 3] = h;
		return n + 1;
	}
}

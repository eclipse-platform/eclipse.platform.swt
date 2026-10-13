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
package org.eclipse.swt.internal;

import java.util.Arrays;

/**
 * A set of damaged pixel rectangles, kept disjoint, in one reusable {@code int} lane.
 *
 * <p>Models the framebuffer-update rectangle list of a remote-framebuffer (VNC/RFB)
 * server: the exact area a paint pass changed, as a short list of rectangles, plus the
 * RFB CopyRect case where a block moves (a blit scroll) and only its destination is
 * damaged. A test that paints through a GC and then compares this region with the area
 * it expected can tell "only the changed plane was repainted" from "everything was
 * repainted".</p>
 *
 * <p>State is one {@code int} lane of {@code x, y, width, height} quadruples and two
 * scratch lanes that are reused by every call. Adding a rectangle subtracts the
 * existing ones from it, so the stored rectangles never overlap and {@link #area()} is
 * exact; rectangles that share a full edge are merged. When more than {@code maxRects}
 * remain the region collapses to its bounding box ({@link #overflowed()}), the same
 * trade a remote-framebuffer encoder makes, and then {@link #area()} is an upper bound.</p>
 *
 * <p>Rectangles are in device pixels; {@link #addPoints} converts points with one rounding rule.
 * Coordinates and sizes must stay within {@code +-2^29}. No allocation after the lanes
 * have grown to the high-water mark; no clone, no {@code Objects} helpers and no
 * 32-bit wrap-around arithmetic, so the class is GWT-friendly.</p>
 *
 * @noreference This class is not intended to be referenced by clients.
 */
public final class ViewportDamageRegion {
	/** Default cap on stored rectangles before the region collapses to its bounding box. */
	public static final int DEFAULT_MAX_RECTS = 64;
	private static final int LIMIT = 1 << 29;

	private final int maxRects;
	private int [] rects = new int [16 * 4];
	private int count;
	private boolean overflowed;
	private long copyCount;
	private long copiedPixels;
	private int [] workIn = new int [16 * 4];
	private int [] workOut = new int [16 * 4];
	private final int [] box = new int [4];

	public ViewportDamageRegion () {
		this (DEFAULT_MAX_RECTS);
	}

	public ViewportDamageRegion (int maxRects) {
		if (maxRects < 1) throw new IllegalArgumentException ("maxRects must be at least 1");
		this.maxRects = maxRects;
	}

	public int rectCount () {
		return count;
	}

	public int x (int index) {
		checkIndex (index);
		return rects [index * 4];
	}

	public int y (int index) {
		checkIndex (index);
		return rects [index * 4 + 1];
	}

	public int width (int index) {
		checkIndex (index);
		return rects [index * 4 + 2];
	}

	public int height (int index) {
		checkIndex (index);
		return rects [index * 4 + 3];
	}

	public boolean isEmpty () {
		return count == 0;
	}

	/** True when the region collapsed to its bounding box: it is then a superset of the true damage. */
	public boolean overflowed () {
		return overflowed;
	}

	public void clear () {
		count = 0;
		overflowed = false;
		copyCount = 0;
		copiedPixels = 0;
	}

	/** Number of CopyRect-style moves recorded since the last {@link #clear()}. */
	public long copyCount () {
		return copyCount;
	}

	/** Pixels moved by CopyRect-style moves since the last {@link #clear()}. */
	public long copiedPixels () {
		return copiedPixels;
	}

	/** Damages a rectangle. Empty rectangles are ignored. */
	public void add (int x, int y, int width, int height) {
		if (width <= 0 || height <= 0) return;
		checkRange (x, y, width, height);
		if (overflowed) {
			unionIntoBounds (x, y, width, height);
			return;
		}
		int pieces = subtract (x, y, width, height);
		for (int i = 0; i < pieces; i++) {
			append (workIn [i * 4], workIn [i * 4 + 1], workIn [i * 4 + 2], workIn [i * 4 + 3]);
		}
		coalesce ();
		if (count > maxRects) collapse ();
	}

	/**
	 * Records a block move: only the destination is damaged, the source is not. This is the RFB
	 * CopyRect case and equals a blit scroll or a tile translate.
	 */
	public void addCopy (int srcX, int srcY, int dstX, int dstY, int width, int height) {
		if (width <= 0 || height <= 0) return;
		checkRange (srcX, srcY, width, height);
		copyCount++;
		copiedPixels += (long) width * height;
		add (dstX, dstY, width, height);
	}

	/** Damaged pixels; exact unless {@link #overflowed()}, then an upper bound. */
	public long area () {
		long sum = 0;
		for (int i = 0; i < count; i++) {
			sum += (long) rects [i * 4 + 2] * rects [i * 4 + 3];
		}
		return sum;
	}

	/** Damaged pixels inside the given rectangle. */
	public long areaWithin (int x, int y, int width, int height) {
		if (width <= 0 || height <= 0) return 0;
		long sum = 0;
		for (int i = 0; i < count; i++) {
			long left = Math.max (x, rects [i * 4]);
			long top = Math.max (y, rects [i * 4 + 1]);
			long right = Math.min ((long) x + width, (long) rects [i * 4] + rects [i * 4 + 2]);
			long bottom = Math.min ((long) y + height, (long) rects [i * 4 + 1] + rects [i * 4 + 3]);
			if (right > left && bottom > top) sum += (right - left) * (bottom - top);
		}
		return sum;
	}

	/** True when every pixel of the rectangle is damaged. */
	public boolean covers (int x, int y, int width, int height) {
		if (width <= 0 || height <= 0) return true;
		checkRange (x, y, width, height);
		return subtract (x, y, width, height) == 0;
	}

	/** True when every damaged pixel of this region lies inside {@code other}. */
	public boolean isSubsetOf (ViewportDamageRegion other) {
		for (int i = 0; i < count; i++) {
			if (!other.covers (rects [i * 4], rects [i * 4 + 1], rects [i * 4 + 2], rects [i * 4 + 3])) return false;
		}
		return true;
	}

	/** Writes {@code x, y, width, height} of the bounding box into {@code out}; all zero when empty. */
	public void boundingBox (int [] out) {
		if (out.length < 4) throw new IllegalArgumentException ("out needs 4 elements");
		if (count == 0) {
			out [0] = out [1] = out [2] = out [3] = 0;
			return;
		}
		long left = Long.MAX_VALUE, top = Long.MAX_VALUE, right = Long.MIN_VALUE, bottom = Long.MIN_VALUE;
		for (int i = 0; i < count; i++) {
			left = Math.min (left, rects [i * 4]);
			top = Math.min (top, rects [i * 4 + 1]);
			right = Math.max (right, (long) rects [i * 4] + rects [i * 4 + 2]);
			bottom = Math.max (bottom, (long) rects [i * 4 + 1] + rects [i * 4 + 3]);
		}
		out [0] = (int) left;
		out [1] = (int) top;
		out [2] = (int) (right - left);
		out [3] = (int) (bottom - top);
	}

	/**
	 * Damages a rectangle given in points at {@code zoomPercent} (100 = one point per pixel).
	 * The start rounds down and the end rounds up with {@link #pixelFloor} and {@link #pixelCeil}, the
	 * same at every zoom, so the pixel rectangle always contains the exact scaled one. Two point
	 * rectangles that touch can share one boundary pixel after rounding; the region keeps the
	 * union, so rounding never leaves a gap and never counts a pixel twice.
	 */
	public void addPoints (int x, int y, int width, int height, int zoomPercent) {
		if (width <= 0 || height <= 0) return;
		int left = pixelFloor (x, zoomPercent);
		int top = pixelFloor (y, zoomPercent);
		int right = pixelCeil ((long) x + width, zoomPercent);
		int bottom = pixelCeil ((long) y + height, zoomPercent);
		add (left, top, right - left, bottom - top);
	}

	/**
	 * Damages the ring between an outer rectangle and the rectangle inset by the given edges, in
	 * pixels: a border, a scrollbar track, an inset or a margin. The four strips are disjoint by
	 * construction, so nested rings added one after the other leave no gap and no overlap. An edge
	 * of 0 adds nothing on that side (an overlay scrollbar has no edge). If the edges meet or cross,
	 * the whole outer rectangle is the ring.
	 */
	public void addFrame (int x, int y, int width, int height, int top, int right, int bottom, int left) {
		if (top < 0 || right < 0 || bottom < 0 || left < 0) throw new IllegalArgumentException ("negative edge");
		if (width <= 0 || height <= 0) return;
		checkRange (x, y, width, height);
		checkRange (0, 0, top + bottom, left + right);
		long innerWidth = (long) width - left - right;
		long innerHeight = (long) height - top - bottom;
		if (innerWidth <= 0 || innerHeight <= 0) {
			add (x, y, width, height);
			return;
		}
		add (x, y, width, top);
		add (x, y + top + (int) innerHeight, width, bottom);
		add (x, y + top, left, (int) innerHeight);
		add (x + left + (int) innerWidth, y + top, right, (int) innerHeight);
	}

	/** Device pixel of a start coordinate: {@code floor (point * zoomPercent / 100)}. */
	public static int pixelFloor (int point, int zoomPercent) {
		checkZoom (zoomPercent);
		return toInt (floorDiv ((long) point * zoomPercent, 100));
	}

	/** Device pixel of an end coordinate: {@code ceil (point * zoomPercent / 100)}. */
	public static int pixelCeil (int point, int zoomPercent) {
		return pixelCeil ((long) point, zoomPercent);
	}

	private static int pixelCeil (long point, int zoomPercent) {
		checkZoom (zoomPercent);
		return toInt (-floorDiv (-point * zoomPercent, 100));
	}

	private static void checkZoom (int zoomPercent) {
		if (zoomPercent < 1 || zoomPercent > 1600) throw new IllegalArgumentException ("zoom percent out of range");
	}

	private static int toInt (long pixel) {
		if (pixel < -LIMIT || pixel > LIMIT) throw new IllegalArgumentException ("pixel coordinate out of range");
		return (int) pixel;
	}

	private static long floorDiv (long a, long b) {
		long q = a / b;
		return (a % b != 0 && (a < 0) != (b < 0)) ? q - 1 : q;
	}

	/** Pixels that are damaged in both regions. */
	public long intersectionArea (ViewportDamageRegion other) {
		long sum = 0;
		for (int i = 0; i < count; i++) {
			sum += other.areaWithin (rects [i * 4], rects [i * 4 + 1], rects [i * 4 + 2], rects [i * 4 + 3]);
		}
		return sum;
	}

	private void checkIndex (int index) {
		if (index < 0 || index >= count) throw new IndexOutOfBoundsException ("rectangle " + index + " of " + count);
	}

	private static void checkRange (int x, int y, int width, int height) {
		if (x < -LIMIT || x > LIMIT || y < -LIMIT || y > LIMIT || width > LIMIT || height > LIMIT) {
			throw new IllegalArgumentException ("damage rectangle out of range");
		}
	}

	private void append (int x, int y, int width, int height) {
		if ((count + 1) * 4 > rects.length) rects = Arrays.copyOf (rects, rects.length * 2);
		rects [count * 4] = x;
		rects [count * 4 + 1] = y;
		rects [count * 4 + 2] = width;
		rects [count * 4 + 3] = height;
		count++;
	}

	/**
	 * Subtracts every stored rectangle from the given one. The remaining disjoint pieces are left
	 * in {@code workIn}; returns their number.
	 */
	private int subtract (int x, int y, int width, int height) {
		workIn [0] = x;
		workIn [1] = y;
		workIn [2] = width;
		workIn [3] = height;
		int inCount = 1;
		for (int e = 0; e < count && inCount > 0; e++) {
			int outCount = 0;
			for (int p = 0; p < inCount; p++) {
				if ((outCount + 4) * 4 > workOut.length) workOut = Arrays.copyOf (workOut, workOut.length * 2);
				outCount = split (workIn, p * 4, rects [e * 4], rects [e * 4 + 1], rects [e * 4 + 2], rects [e * 4 + 3], outCount);
			}
			int [] swap = workIn;
			workIn = workOut;
			workOut = swap;
			if (workOut.length < workIn.length) workOut = Arrays.copyOf (workOut, workIn.length);
			inCount = outCount;
		}
		return inCount;
	}

	/** Appends the parts of rectangle {@code r} (at {@code offset} in {@code src}) not covered by e. */
	private int split (int [] src, int offset, int ex, int ey, int ew, int eh, int outCount) {
		int rx = src [offset], ry = src [offset + 1], rw = src [offset + 2], rh = src [offset + 3];
		int rr = rx + rw, rb = ry + rh, er = ex + ew, eb = ey + eh;
		if (ex >= rr || er <= rx || ey >= rb || eb <= ry) {
			return emit (outCount, rx, ry, rw, rh);
		}
		int n = outCount;
		if (ey > ry) n = emit (n, rx, ry, rw, ey - ry);
		if (eb < rb) n = emit (n, rx, eb, rw, rb - eb);
		int top = Math.max (ry, ey);
		int bottom = Math.min (rb, eb);
		if (ex > rx) n = emit (n, rx, top, ex - rx, bottom - top);
		if (er < rr) n = emit (n, er, top, rr - er, bottom - top);
		return n;
	}

	private int emit (int outCount, int x, int y, int width, int height) {
		workOut [outCount * 4] = x;
		workOut [outCount * 4 + 1] = y;
		workOut [outCount * 4 + 2] = width;
		workOut [outCount * 4 + 3] = height;
		return outCount + 1;
	}

	/** Merges rectangles that share a complete edge, until none do. */
	private void coalesce () {
		boolean merged = true;
		while (merged) {
			merged = false;
			for (int i = 0; i < count && !merged; i++) {
				for (int j = i + 1; j < count; j++) {
					if (tryMerge (i, j)) {
						rects [j * 4] = rects [(count - 1) * 4];
						rects [j * 4 + 1] = rects [(count - 1) * 4 + 1];
						rects [j * 4 + 2] = rects [(count - 1) * 4 + 2];
						rects [j * 4 + 3] = rects [(count - 1) * 4 + 3];
						count--;
						merged = true;
						break;
					}
				}
			}
		}
	}

	private boolean tryMerge (int i, int j) {
		int ax = rects [i * 4], ay = rects [i * 4 + 1], aw = rects [i * 4 + 2], ah = rects [i * 4 + 3];
		int bx = rects [j * 4], by = rects [j * 4 + 1], bw = rects [j * 4 + 2], bh = rects [j * 4 + 3];
		if (ax == bx && aw == bw && (ay + ah == by || by + bh == ay)) {
			rects [i * 4 + 1] = Math.min (ay, by);
			rects [i * 4 + 3] = ah + bh;
			return true;
		}
		if (ay == by && ah == bh && (ax + aw == bx || bx + bw == ax)) {
			rects [i * 4] = Math.min (ax, bx);
			rects [i * 4 + 2] = aw + bw;
			return true;
		}
		return false;
	}

	private void collapse () {
		boundingBox (box);
		count = 0;
		append (box [0], box [1], box [2], box [3]);
		overflowed = true;
	}

	private void unionIntoBounds (int x, int y, int width, int height) {
		boundingBox (box);
		int left = Math.min (box [0], x);
		int top = Math.min (box [1], y);
		int right = Math.max (box [0] + box [2], x + width);
		int bottom = Math.max (box [1] + box [3], y + height);
		count = 0;
		append (left, top, right - left, bottom - top);
	}
}

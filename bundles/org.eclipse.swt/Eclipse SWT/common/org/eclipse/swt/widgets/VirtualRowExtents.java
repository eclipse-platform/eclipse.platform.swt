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

import java.util.*;

/**
 * Variable row extents with a measurement cache: where a row starts, which row
 * holds an offset, which rows a viewport shows, how far to scroll to reveal a row,
 * and how much to move the scroll offset when a row above the viewport changes size.
 *
 * <p>{@link VirtualScrollMetrics} covers uniform rows; this class is its variable
 * counterpart for widgets whose rows differ in height (wrapped text, owner-drawn
 * items, nested content). Rows that have not been measured count as the estimate,
 * so the scrollable extent is available before every row is laid out.</p>
 *
 * <p>State is two primitive lanes and nothing per row: an {@code int} lane with the
 * measured extent or {@code -1}, and a Fenwick tree in a {@code long} lane over
 * the effective extents. Offsets, row lookup and size changes cost O(log n); an
 * insert, a remove or a new estimate rebuilds the tree in O(n) into the same arrays.
 * The behaviour follows two pattern donors, read at pinned commits and not copied:
 * react-window (MIT, f/439cfee: lib/core getOffsetForIndex, getEstimatedSize,
 * createCachedBounds) for lazy measurement, the start/end/center/auto/smart
 * alignments and the oversized-row rule; and TanStack Virtual (MIT, f211d3a:
 * packages/virtual-core getOffsetForIndex and resizeItem) for the scroll-anchoring
 * rule when a row above the fold is measured. Unlike react-window the estimate is a
 * constant the caller sets (setEstimate); pass the running average to mimic it.</p>
 */
final class VirtualRowExtents {
	static final int ALIGN_AUTO = 0;
	static final int ALIGN_START = 1;
	static final int ALIGN_CENTER = 2;
	static final int ALIGN_END = 3;
	static final int ALIGN_SMART = 4;
	private static final int UNMEASURED = -1;

	private int estimate;
	private int count;
	private int measured;
	private int [] extents = new int [0];
	private long [] tree = new long [1];

	VirtualRowExtents (int estimate) {
		checkEstimate (estimate);
		this.estimate = estimate;
	}

	int rowCount () {
		return count;
	}

	int estimate () {
		return estimate;
	}

	int measuredCount () {
		return measured;
	}

	boolean isMeasured (int row) {
		checkRow (row);
		return extents [row] != UNMEASURED;
	}

	/** Measured extent, or the estimate for a row that was never measured. */
	int extentOf (int row) {
		checkRow (row);
		return effective (row);
	}

	void setEstimate (int estimate) {
		checkEstimate (estimate);
		if (estimate == this.estimate) return;
		this.estimate = estimate;
		rebuild ();
	}

	/** Forgets every measurement and sets the row count; all rows become the estimate. */
	void reset (int rows) {
		if (rows < 0) throw new IllegalArgumentException ("negative row count");
		resizeLanes (rows);
		Arrays.fill (extents, 0, rows, UNMEASURED);
		count = rows;
		measured = 0;
		rebuild ();
	}

	/** Records the measured extent of one row. */
	void setExtent (int row, int extent) {
		checkRow (row);
		if (extent < 0) throw new IllegalArgumentException ("negative extent");
		int before = effective (row);
		if (extents [row] == UNMEASURED) measured++;
		extents [row] = extent;
		add (row, (long) extent - before);
	}

	/** Returns one row to the estimate. */
	void clearExtent (int row) {
		checkRow (row);
		if (extents [row] == UNMEASURED) return;
		int before = extents [row];
		extents [row] = UNMEASURED;
		measured--;
		add (row, (long) estimate - before);
	}

	/** Returns every row from {@code row} on to the estimate (react-window resetAfterIndex). */
	void clearFrom (int row) {
		if (row < 0 || row > count) throw new IllegalArgumentException ("invalid row");
		for (int i = row; i < count; i++) {
			if (extents [i] != UNMEASURED) measured--;
			extents [i] = UNMEASURED;
		}
		rebuild ();
	}

	/** Opens {@code rows} unmeasured rows at {@code index}. */
	void insert (int index, int rows) {
		if (rows < 0 || index < 0 || index > count) throw new IllegalArgumentException ("invalid insert");
		if (rows == 0) return;
		int next = Math.addExact (count, rows);
		resizeLanes (next);
		System.arraycopy (extents, index, extents, index + rows, count - index);
		Arrays.fill (extents, index, index + rows, UNMEASURED);
		count = next;
		rebuild ();
	}

	/** Deletes {@code rows} rows at {@code index}. */
	void remove (int index, int rows) {
		if (rows < 0 || index < 0 || index > count - rows) throw new IllegalArgumentException ("invalid remove");
		if (rows == 0) return;
		for (int i = index; i < index + rows; i++) {
			if (extents [i] != UNMEASURED) measured--;
		}
		System.arraycopy (extents, index + rows, extents, index, count - index - rows);
		count -= rows;
		rebuild ();
	}

	/** Start offset of {@code row}; {@code offsetOf (rowCount ())} is the total extent. */
	long offsetOf (int row) {
		if (row < 0 || row > count) throw new IllegalArgumentException ("invalid row");
		long sum = 0;
		for (int i = row; i > 0; i -= i & -i) sum += tree [i];
		return sum;
	}

	long totalExtent () {
		return offsetOf (count);
	}

	/** Row that contains {@code offset}; clamped to the first or last row, -1 when empty. */
	int rowAt (long offset) {
		if (count == 0) return -1;
		if (offset <= 0) return 0;
		int position = 0;
		long remaining = offset;
		for (int step = Integer.highestOneBit (count); step > 0; step >>= 1) {
			int next = position + step;
			if (next <= count && tree [next] <= remaining) {
				position = next;
				remaining -= tree [next];
			}
		}
		return Math.min (position, count - 1);
	}

	/**
	 * Rows a viewport shows. Writes the first row to {@code out [0]} and the row after
	 * the last one to {@code out [1]}; both are 0 for an empty list.
	 */
	void visibleRange (long offset, int viewport, int [] out) {
		if (viewport < 0) throw new IllegalArgumentException ("negative viewport");
		if (count == 0) {
			out [0] = 0;
			out [1] = 0;
			return;
		}
		int first = rowAt (offset);
		out [0] = first;
		out [1] = viewport == 0 ? first : rowAt (offset + viewport - 1) + 1;
	}

	long maxScrollOffset (int viewport) {
		return Math.max (0L, totalExtent () - viewport);
	}

	long clampOffset (long offset, int viewport) {
		return Math.max (0L, Math.min (offset, maxScrollOffset (viewport)));
	}

	/**
	 * Scroll offset that reveals {@code row}. START puts the row at the top, END at the
	 * bottom, CENTER in the middle; AUTO is the smallest move (none when the row is already
	 * revealed); SMART keeps the offset when the row is revealed and centers it otherwise.
	 * A row taller than the viewport is revealed while it fills the viewport. The result
	 * is clamped to the scrollable range.
	 */
	long scrollOffsetFor (int row, int align, long current, int viewport) {
		checkRow (row);
		long start = offsetOf (row);
		long end = start + effective (row);
		long target = switch (align) {
			case ALIGN_START -> start;
			case ALIGN_END -> end - viewport;
			case ALIGN_CENTER -> start + Math.floorDiv (effective (row) - viewport, 2);
			case ALIGN_AUTO -> autoTarget (start, end, current, viewport);
			case ALIGN_SMART -> isRevealed (start, end, current, viewport) ? current
					: start + Math.floorDiv (effective (row) - viewport, 2);
			default -> throw new IllegalArgumentException ("invalid alignment");
		};
		return clampOffset (target, viewport);
	}

	/**
	 * Measures {@code row} and returns how far the caller should move its scroll offset
	 * so content already in view does not jump: the whole change when a first measurement
	 * lands on a row that starts above the offset, or when a re-measured row lies entirely
	 * above it and the user is not scrolling backward; otherwise 0.
	 */
	long setExtentAnchored (int row, int extent, long scrollOffset, boolean scrollingBackward) {
		checkRow (row);
		if (extent < 0) throw new IllegalArgumentException ("negative extent");
		boolean first = extents [row] == UNMEASURED;
		long start = offsetOf (row);
		long end = start + effective (row);
		long delta = (long) extent - effective (row);
		setExtent (row, extent);
		if (delta == 0) return 0;
		boolean above = first ? start < scrollOffset : end <= scrollOffset && !scrollingBackward;
		return above ? delta : 0;
	}

	/** Offsets at which the row is revealed lie in [min (start, end - viewport), max (start, end - viewport)]. */
	private static boolean isRevealed (long start, long end, long current, int viewport) {
		return current >= Math.min (start, end - viewport) && current <= Math.max (start, end - viewport);
	}

	private static long autoTarget (long start, long end, long current, int viewport) {
		long low = Math.min (start, end - viewport);
		long high = Math.max (start, end - viewport);
		return current < low ? low : Math.min (current, high);
	}

	private int effective (int row) {
		int value = extents [row];
		return value == UNMEASURED ? estimate : value;
	}

	/** Adds {@code delta} to the extent of {@code row} in the tree. */
	private void add (int row, long delta) {
		if (delta == 0) return;
		for (int i = row + 1; i <= count; i += i & -i) tree [i] += delta;
	}

	private void rebuild () {
		Arrays.fill (tree, 0, count + 1, 0L);
		for (int i = 1; i <= count; i++) {
			tree [i] += effective (i - 1);
			int parent = i + (i & -i);
			if (parent <= count) tree [parent] += tree [i];
		}
	}

	private void resizeLanes (int rows) {
		if (rows > extents.length) {
			int grown = Math.max (rows, extents.length + (extents.length >> 1) + 8);
			extents = Arrays.copyOf (extents, grown);
		}
		if (rows + 1 > tree.length) {
			int grown = Math.max (rows + 1, tree.length + (tree.length >> 1) + 8);
			tree = Arrays.copyOf (tree, grown);
		}
	}

	private void checkRow (int row) {
		if (row < 0 || row >= count) throw new IllegalArgumentException ("invalid row");
	}

	private static void checkEstimate (int estimate) {
		if (estimate < 1) throw new IllegalArgumentException ("non-positive estimate");
	}
}

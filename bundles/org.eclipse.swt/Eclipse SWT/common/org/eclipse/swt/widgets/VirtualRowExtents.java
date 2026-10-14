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

import org.eclipse.swt.internal.PackedHeapGrowth;

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
 * <p>Unmeasured rows are implicit: logical count and estimate allocate no row-sized
 * backing. Sorted primitive row IDs and measured extents carry only measured rows;
 * the existing Fenwick algorithm indexes their signed deltas from the estimate.
 * With m measured rows, offset and repeat measurement are O(log m), row lookup is
 * O(log squared m), and sequential first measurements append in O(log m). A first
 * measurement inserted in the middle, deletion of measurements, or an estimate
 * change is O(m). This is a sparse-residency tradeoff, not a universal speedup.
 * No Item/proxy or per-row object is created. Reset drops all measurement backing.
 * The behaviour follows two pattern donors, read at pinned commits and not copied:
 * react-window (MIT, 439cfee: lib/core getOffsetForIndex, getEstimatedSize,
 * createCachedBounds, getStartStopIndices) for lazy measurement, the
 * start/end/center/auto/smart alignments and the oversized-row rule; and TanStack
 * Virtual (MIT, f211d3a: packages/virtual-core getOffsetForIndex and resizeItem) for
 * the scroll-anchoring rule when a row above the fold is measured. Two differences:
 * react-window keeps an append-only prefix cache (rows are measured in order and a
 * reset is needed to re-measure), whereas rows here can be measured, re-measured or
 * cleared in any order; and the estimate for unmeasured rows is a constant
 * the caller sets (setEstimate), where react-window uses the running average.</p>
 */
final class VirtualRowExtents {
	static final int ALIGN_AUTO = 0;
	static final int ALIGN_START = 1;
	static final int ALIGN_CENTER = 2;
	static final int ALIGN_END = 3;
	static final int ALIGN_SMART = 4;
	private static final int [] EMPTY = new int [0];
	private static final long [] EMPTY_TREE = new long [1];

	private int estimate;
	private int count;
	private int measured;
	private int [] rowIds = EMPTY;
	private int [] extents = EMPTY;
	private long [] tree = EMPTY_TREE;

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
		return position (row) >= 0;
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
		count = rows;
		measured = 0;
		rowIds = extents = EMPTY;
		tree = EMPTY_TREE;
	}

	/** Records the measured extent of one row. */
	void setExtent (int row, int extent) {
		checkRow (row);
		if (extent < 0) throw new IllegalArgumentException ("negative extent");
		int at = position (row);
		if (at >= 0) {
			int before = extents [at];
			extents [at] = extent;
			add (at, (long) extent - before);
			return;
		}
		at = -at - 1;
		resizeLanes (Math.addExact (measured, 1));
		System.arraycopy (rowIds, at, rowIds, at + 1, measured - at);
		System.arraycopy (extents, at, extents, at + 1, measured - at);
		rowIds [at] = row;
		extents [at] = extent;
		measured++;
		if (at == measured - 1) {
			// New Fenwick node includes preceding nodes in its low-bit block.
			tree [measured] = (long) extent - estimate + deltaPrefix (measured - 1)
					- deltaPrefix (measured - (measured & -measured));
		} else {
			rebuild ();
		}
	}

	/** Returns one row to the estimate. */
	void clearExtent (int row) {
		checkRow (row);
		int at = position (row);
		if (at < 0) return;
		System.arraycopy (rowIds, at + 1, rowIds, at, measured - at - 1);
		System.arraycopy (extents, at + 1, extents, at, measured - at - 1);
		measured--;
		rebuild ();
	}

	/** Returns every row from {@code row} on to the estimate (react-window resetAfterIndex). */
	void clearFrom (int row) {
		if (row < 0 || row > count) throw new IllegalArgumentException ("invalid row");
		int keep = lowerBound (row);
		if (keep == measured) return;
		measured = keep;
		rebuild ();
	}

	/** Opens {@code rows} unmeasured rows at {@code index}. */
	void insert (int index, int rows) {
		if (rows < 0 || index < 0 || index > count) throw new IllegalArgumentException ("invalid insert");
		if (rows == 0) return;
		int next = Math.addExact (count, rows);
		for (int i = lowerBound (index); i < measured; i++) rowIds [i] += rows;
		count = next;
		// Delta order and values did not change; do not rebuild or allocate.
	}

	/** Deletes {@code rows} rows at {@code index}. */
	void remove (int index, int rows) {
		if (rows < 0 || index < 0 || index > count - rows) throw new IllegalArgumentException ("invalid remove");
		if (rows == 0) return;
		int left = lowerBound (index), right = lowerBound (index + rows);
		int removed = right - left;
		System.arraycopy (rowIds, right, rowIds, left, measured - right);
		System.arraycopy (extents, right, extents, left, measured - right);
		measured -= removed;
		for (int i = left; i < measured; i++) rowIds [i] -= rows;
		count -= rows;
		if (removed != 0) rebuild ();
	}

	/** Start offset of {@code row}; {@code offsetOf (rowCount ())} is the total extent. */
	long offsetOf (int row) {
		if (row < 0 || row > count) throw new IllegalArgumentException ("invalid row");
		return (long) row * estimate + deltaPrefix (lowerBound (row));
	}

	long totalExtent () {
		return offsetOf (count);
	}

	/** Row that contains {@code offset}; clamped to the first or last row, -1 when empty. */
	int rowAt (long offset) {
		if (count == 0) return -1;
		if (offset <= 0) return 0;
		if (offset >= totalExtent ()) return count - 1;
		// Upper bound of measured-row ends; zero-height runs remain half-open.
		int low = 0, high = measured;
		while (low < high) {
			int mid = (low + high) >>> 1;
			long end = ((long) rowIds [mid] + 1) * estimate + deltaPrefix (mid + 1);
			if (end <= offset) low = mid + 1;
			else high = mid;
		}
		int first = low == 0 ? 0 : rowIds [low - 1] + 1;
		long start = (long) first * estimate + deltaPrefix (low);
		long inGap = first + (offset - start) / estimate;
		int limit = low == measured ? count - 1 : rowIds [low];
		return (int) Math.min (inGap, limit);
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
		boolean first = position (row) < 0;
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
		int at = position (row);
		return at < 0 ? estimate : extents [at];
	}

	private int position (int row) {
		return Arrays.binarySearch (rowIds, 0, measured, row);
	}

	private int lowerBound (int row) {
		int at = position (row);
		return at < 0 ? -at - 1 : at;
	}

	private long deltaPrefix (int entries) {
		long sum = 0;
		for (int i = entries; i > 0; i -= i & -i) sum += tree [i];
		return sum;
	}

	/** Adds a measured-slot delta; the tree is over measured slots, not logical rows. */
	private void add (int slot, long delta) {
		if (delta == 0) return;
		for (int i = slot + 1; i <= measured;) {
			tree [i] += delta;
			int step = i & -i;
			if (step > measured - i) break;
			i += step;
		}
	}

	private void rebuild () {
		if (measured == 0) {
			rowIds = extents = EMPTY;
			tree = EMPTY_TREE;
			return;
		}
		Arrays.fill (tree, 0, measured + 1, 0L);
		for (int i = 1; i <= measured; i++) {
			tree [i] += (long) extents [i - 1] - estimate;
			int step = i & -i;
			if (step <= measured - i) tree [i + step] += tree [i];
		}
	}

	private void resizeLanes (int entries) {
		if (entries <= extents.length) return;
		int grown = PackedHeapGrowth.capacity (extents.length, entries, 16);
		// One parent-owned three-lane directory. Stage all allocations before publication.
		int [] nextRows = Arrays.copyOf (rowIds, grown);
		int [] nextExtents = Arrays.copyOf (extents, grown);
		long [] nextTree = Arrays.copyOf (tree, Math.addExact (grown, 1));
		rowIds = nextRows;
		extents = nextExtents;
		tree = nextTree;
	}

	private void checkRow (int row) {
		if (row < 0 || row >= count) throw new IllegalArgumentException ("invalid row");
	}

	private static void checkEstimate (int estimate) {
		if (estimate < 1) throw new IllegalArgumentException ("non-positive estimate");
	}
}

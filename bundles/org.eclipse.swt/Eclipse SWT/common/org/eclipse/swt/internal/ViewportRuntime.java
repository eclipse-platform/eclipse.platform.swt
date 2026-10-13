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

import java.util.*;

/**
 * Shared model-only runtime for viewport-driven SWT controls.
 *
 * <p>This class owns no native resources and does not replace SWT's public
 * {@code GC}, {@code ScrollBar}, {@code Item}, or event contracts. It centralizes
 * origin/layer invalidation and scrollbar fixed-point calculations so platform
 * widgets and custom widgets can share the same viewport mechanics.</p>
 *
 * @noreference This class is not intended to be referenced by clients.
 */
public final class ViewportRuntime {

	public static final int BODY = 1 << 0;
	public static final int FROZEN = 1 << 1;
	public static final int HEADER = 1 << 2;
	public static final int EDITOR = 1 << 3;
	public static final int SCROLLBAR = 1 << 4;
	public static final int FEEDBACK = 1 << 5;
	public static final int ALL = BODY | FROZEN | HEADER | EDITOR | SCROLLBAR | FEEDBACK;

	public static final int AUTO = 0;
	public static final int ALWAYS = 1;
	public static final int NEVER = 2;

	public record PixelLayout (
			boolean horizontalVisible,
			boolean verticalVisible,
			boolean cornerVisible,
			int bodyWidth,
			int bodyHeight,
			long contentWidth,
			long contentHeight) {
	}

	public record RowLayout (
			boolean horizontalVisible,
			boolean verticalVisible,
			boolean cornerVisible,
			int bodyWidth,
			int bodyHeight,
			int headerWidth,
			int headerHeight,
			int visibleRows,
			long logicalRows,
			long estimatedContentHeight,
			long logicalContentWidth) {
	}

	public static final class RowWindow {
		public static final int DEFAULT_OVERSCAN_ROWS = 8;

		private long logicalCount;
		private long firstVisible;
		private int visibleCount;
		// Capacity survives clipping at the logical tail or an empty model.
		private int viewportRows;
		private int overscanRows = DEFAULT_OVERSCAN_ROWS;
		private long generation;

		public long logicalCount () {
			return logicalCount;
		}

		public void setLogicalCount (long count) {
			if (count < 0) throw new IllegalArgumentException ("negative logical count");
			if (count == logicalCount) return;
			logicalCount = count;
			clampViewport ();
			generation++;
		}

		public void insert (long index, long count) {
			if (count < 0 || index < 0 || index > logicalCount) {
				throw new IllegalArgumentException ("invalid insert");
			}
			if (count == 0) return;
			long nextCount = Math.addExact (logicalCount, count);
			if (index <= firstVisible && logicalCount != 0) {
				firstVisible = Math.addExact (firstVisible, count);
			}
			logicalCount = nextCount;
			clampViewport ();
			generation++;
		}

		public void remove (long index, long count) {
			if (count < 0 || index < 0 || index > logicalCount - count) {
				throw new IllegalArgumentException ("invalid remove");
			}
			if (count == 0) return;
			if (index < firstVisible) {
				firstVisible -= Math.min (count, firstVisible - index);
			}
			logicalCount -= count;
			clampViewport ();
			generation++;
		}

		public void setViewport (long first, int visibleRows) {
			if (first < 0 || visibleRows < 0) {
				throw new IllegalArgumentException ("negative viewport");
			}
			long nextFirst = logicalCount == 0 ? 0 : Math.min (first, logicalCount - 1);
			int nextVisible = (int)Math.min (
					(long)visibleRows, Math.max (0L, logicalCount - nextFirst));
			if (nextFirst == firstVisible && nextVisible == visibleCount
					&& visibleRows == viewportRows) return;
			firstVisible = nextFirst;
			visibleCount = nextVisible;
			viewportRows = visibleRows;
			generation++;
		}

		public long firstVisible () {
			return firstVisible;
		}

		public int visibleCount () {
			return visibleCount;
		}

		public void setOverscanRows (int rows) {
			if (rows < 0) throw new IllegalArgumentException ("negative overscan");
			if (rows == overscanRows) return;
			overscanRows = rows;
			generation++;
		}

		public int overscanRows () {
			return overscanRows;
		}

		public long paintStart () {
			if (visibleCount == 0) return firstVisible;
			return Math.max (0L, firstVisible - overscanRows);
		}

		public long paintEndExclusive () {
			if (visibleCount == 0) return firstVisible;
			long visibleEnd = saturatedAdd (firstVisible, visibleCount);
			return Math.min (logicalCount, saturatedAdd (visibleEnd, overscanRows));
		}

		public int paintCount () {
			return Math.toIntExact (paintEndExclusive () - paintStart ());
		}

		public boolean isVisible (long index) {
			return 0 <= index && index < logicalCount
					&& firstVisible <= index
					&& index < saturatedAdd (firstVisible, visibleCount);
		}

		public boolean isPaintCandidate (long index) {
			return 0 <= index && index < logicalCount
					&& paintStart () <= index && index < paintEndExclusive ();
		}

		public void ensureVisible (long row) {
			if (row < 0 || row >= logicalCount) {
				throw new IllegalArgumentException ("row outside viewport model");
			}
			if (viewportRows <= 0 || row < firstVisible) {
				setViewport (row, viewportRows);
				return;
			}
			long end = saturatedAdd (firstVisible, viewportRows);
			if (row >= end) setViewport (row - viewportRows + 1L, viewportRows);
		}

		public long generation () {
			return generation;
		}

		private void clampViewport () {
			if (logicalCount == 0) {
				firstVisible = 0;
				visibleCount = 0;
				return;
			}
			firstVisible = Math.min (firstVisible, logicalCount - 1);
			visibleCount = (int)Math.min (
					(long)viewportRows, Math.max (0L, logicalCount - firstVisible));
		}
	}

	public static final class Selection {
		private int logicalCount;
		private boolean allSelected;
		private int [] starts = new int [4];
		private int [] ends = new int [4];
		private int rangeCount;

		public int logicalCount () {
			return logicalCount;
		}

		public void setLogicalCount (int count) {
			if (count < 0) throw new IllegalArgumentException ("negative logical count");
			if (count == logicalCount) return;
			if (count < logicalCount) {
				remove (count, logicalCount - count);
				return;
			}
			int oldCount = logicalCount;
			logicalCount = count;
			if (allSelected && oldCount < count) addRange (oldCount, count);
		}

		public boolean isSelected (int index) {
			checkIndex (index);
			boolean represented = contains (index);
			return allSelected ? !represented : represented;
		}

		public void setSelected (int index, boolean selected) {
			checkIndex (index);
			if (selected == isSelected (index)) return;
			if (allSelected) {
				if (selected) removeRange (index, index + 1);
				else addRange (index, index + 1);
			} else {
				if (selected) addRange (index, index + 1);
				else removeRange (index, index + 1);
			}
		}

		public void selectRange (int start, int endExclusive) {
			checkRange (start, endExclusive);
			if (allSelected) removeRange (start, endExclusive);
			else addRange (start, endExclusive);
		}

		public void deselectRange (int start, int endExclusive) {
			checkRange (start, endExclusive);
			if (allSelected) addRange (start, endExclusive);
			else removeRange (start, endExclusive);
		}

		public void clear () {
			allSelected = false;
			rangeCount = 0;
		}

		public void selectAll () {
			allSelected = true;
			rangeCount = 0;
		}

		public int selectedCount () {
			long represented = 0;
			for (int i = 0; i < rangeCount; i++) represented += ends [i] - starts [i];
			long selected = allSelected ? (long)logicalCount - represented : represented;
			return Math.toIntExact (selected);
		}

		public int [] toArray () {
			int count = selectedCount ();
			int [] result = new int [count];
			int offset = 0;
			if (!allSelected) {
				for (int i = 0; i < rangeCount; i++) {
					offset = appendRange (result, offset, starts [i], ends [i]);
				}
				return result;
			}
			// Excluded ranges are already normalized; enumerate only the selected gaps.
			int start = 0;
			for (int range = 0; range < rangeCount; range++) {
				offset = appendRange (result, offset, start, starts [range]);
				start = ends [range];
			}
			appendRange (result, offset, start, logicalCount);
			return result;
		}

		private static int appendRange (int [] result, int offset, int start, int endExclusive) {
			for (int value = start; value < endExclusive; value++) {
				result [offset++] = value;
			}
			return offset;
		}

		public void insert (int index, int count) {
			if (count < 0 || index < 0 || index > logicalCount) {
				throw new IllegalArgumentException ("invalid insert");
			}
			if (count == 0) return;
			Selection shifted = new Selection ();
			shifted.logicalCount = Math.addExact (logicalCount, count);
			shifted.allSelected = allSelected;
			for (int i = 0; i < rangeCount; i++) {
				int start = starts [i], end = ends [i];
				if (end <= index) {
					shifted.addRange (start, end);
				} else if (start >= index) {
					shifted.addRange (start + count, end + count);
				} else {
					shifted.addRange (start, index);
					shifted.addRange (index + count, end + count);
				}
			}
			if (allSelected) shifted.addRange (index, index + count);
			copyFrom (shifted);
		}

		public void remove (int index, int count) {
			if (count < 0 || index < 0 || index > logicalCount - count) {
				throw new IllegalArgumentException ("invalid remove");
			}
			if (count == 0) return;
			int endRemoved = index + count;
			Selection shifted = new Selection ();
			shifted.logicalCount = logicalCount - count;
			shifted.allSelected = allSelected;
			for (int i = 0; i < rangeCount; i++) {
				int start = starts [i], end = ends [i];
				if (start < index) shifted.addRange (start, Math.min (end, index));
				if (end > endRemoved) {
					shifted.addRange (Math.max (start, endRemoved) - count, end - count);
				}
			}
			copyFrom (shifted);
		}

		public int rangeCount () {
			return rangeCount;
		}

		public int rangeStart (int range) {
			return starts [rangeIndex (range)];
		}

		public int rangeEndExclusive (int range) {
			return ends [rangeIndex (range)];
		}

		public boolean complementMode () {
			return allSelected;
		}

		private void addRange (int start, int endExclusive) {
			if (start >= endExclusive) return;
			int first = 0;
			while (first < rangeCount && ends [first] < start) first++;
			int mergedStart = start, mergedEnd = endExclusive;
			int last = first;
			while (last < rangeCount && starts [last] <= mergedEnd) {
				mergedStart = Math.min (mergedStart, starts [last]);
				mergedEnd = Math.max (mergedEnd, ends [last]);
				last++;
			}
			int removed = last - first;
			ensureCapacity (rangeCount - removed + 1);
			System.arraycopy (starts, last, starts, first + 1, rangeCount - last);
			System.arraycopy (ends, last, ends, first + 1, rangeCount - last);
			starts [first] = mergedStart;
			ends [first] = mergedEnd;
			rangeCount = rangeCount - removed + 1;
		}

		private void removeRange (int start, int endExclusive) {
			if (start >= endExclusive || rangeCount == 0) return;
			int [] nextStarts = new int [Math.max (4, rangeCount * 2)];
			int [] nextEnds = new int [nextStarts.length];
			int nextCount = 0;
			for (int i = 0; i < rangeCount; i++) {
				int rangeStart = starts [i], rangeEnd = ends [i];
				if (rangeEnd <= start || rangeStart >= endExclusive) {
					nextStarts [nextCount] = rangeStart;
					nextEnds [nextCount++] = rangeEnd;
					continue;
				}
				if (rangeStart < start) {
					nextStarts [nextCount] = rangeStart;
					nextEnds [nextCount++] = start;
				}
				if (rangeEnd > endExclusive) {
					nextStarts [nextCount] = endExclusive;
					nextEnds [nextCount++] = rangeEnd;
				}
			}
			starts = nextStarts;
			ends = nextEnds;
			rangeCount = nextCount;
		}

		private boolean contains (int index) {
			int low = 0, high = rangeCount - 1;
			while (low <= high) {
				int mid = (low + high) >>> 1;
				if (index < starts [mid]) high = mid - 1;
				else if (index >= ends [mid]) low = mid + 1;
				else return true;
			}
			return false;
		}

		private void ensureCapacity (int required) {
			if (required <= starts.length) return;
			int next = Math.max (required, Math.max (4, starts.length * 3 / 2));
			starts = Arrays.copyOf (starts, next);
			ends = Arrays.copyOf (ends, next);
		}

		private void copyFrom (Selection other) {
			logicalCount = other.logicalCount;
			allSelected = other.allSelected;
			starts = Arrays.copyOf (other.starts, other.starts.length);
			ends = Arrays.copyOf (other.ends, other.ends.length);
			rangeCount = other.rangeCount;
		}

		private int rangeIndex (int range) {
			if (range < 0 || range >= rangeCount) {
				throw new IndexOutOfBoundsException ("Index " + range + " out of bounds for length " + rangeCount);
			}
			return range;
		}

		private void checkIndex (int index) {
			if (index < 0 || index >= logicalCount) {
				throw new IllegalArgumentException ("index outside logical model");
			}
		}

		private void checkRange (int start, int endExclusive) {
			if (start < 0 || endExclusive < start || endExclusive > logicalCount) {
				throw new IllegalArgumentException ("range outside logical model");
			}
		}
	}

	private double originX;
	private double originY;
	private boolean originInitialized;

	public void initializeOrigin (double x, double y) {
		requireFinite (x, y);
		originX = x;
		originY = y;
		originInitialized = true;
	}

	/**
	 * Updates the logical viewport origin and returns the dirty z-plane mask.
	 */
	public int scrollTo (double x, double y) {
		requireFinite (x, y);
		if (!originInitialized) {
			initializeOrigin (x, y);
			return 0;
		}
		boolean horizontal = Double.doubleToLongBits (originX) != Double.doubleToLongBits (x);
		boolean vertical = Double.doubleToLongBits (originY) != Double.doubleToLongBits (y);
		originX = x;
		originY = y;

		int dirty = 0;
        if (horizontal) {
            dirty |= BODY | HEADER | EDITOR | FEEDBACK | SCROLLBAR;
        }
        if (vertical) {
            dirty |= BODY | FROZEN | EDITOR | FEEDBACK | SCROLLBAR;
        }
		return dirty;
	}

	public double originX () {
		return originX;
	}

	public double originY () {
		return originY;
	}

	public boolean originInitialized () {
		return originInitialized;
	}

	/**
	 * Solves arbitrary pixel-sized content and scrollbar visibility to a fixed
	 * point. This is the general form used by ScrolledComposite-like controls.
	 */
	public static PixelLayout solvePixels (
			int outerWidth,
			int outerHeight,
			long contentWidth,
			long contentHeight,
			int horizontalBarHeight,
			int verticalBarWidth,
			int horizontalPolicy,
			int verticalPolicy) {
		if (outerWidth < 0 || outerHeight < 0) {
			throw new IllegalArgumentException ("negative viewport extent");
		}
		if (contentWidth < 0 || contentHeight < 0) {
			throw new IllegalArgumentException ("negative logical extent");
		}
		if (horizontalBarHeight < 0 || verticalBarWidth < 0) {
			throw new IllegalArgumentException ("negative scrollbar extent");
		}
		checkPolicy (horizontalPolicy);
		checkPolicy (verticalPolicy);

		boolean horizontal = horizontalPolicy == ALWAYS;
		boolean vertical = verticalPolicy == ALWAYS;
		for (int pass = 0; pass < 4; pass++) {
			int bodyWidth = Math.max (0, outerWidth - (vertical ? verticalBarWidth : 0));
			int bodyHeight = Math.max (0, outerHeight - (horizontal ? horizontalBarHeight : 0));
			boolean nextHorizontal = policyVisible (horizontalPolicy, contentWidth > bodyWidth);
			boolean nextVertical = policyVisible (verticalPolicy, contentHeight > bodyHeight);
            if (horizontal == nextHorizontal && vertical == nextVertical) {
                break;
            }
			horizontal = nextHorizontal;
			vertical = nextVertical;
		}

		int bodyWidth = Math.max (0, outerWidth - (vertical ? verticalBarWidth : 0));
		int bodyHeight = Math.max (0, outerHeight - (horizontal ? horizontalBarHeight : 0));
		return new PixelLayout (
				horizontal,
				vertical,
				horizontal && vertical,
				bodyWidth,
				bodyHeight,
				contentWidth,
				contentHeight);
	}

	/**
	 * Solves a uniform-row viewport. This preserves the historical viewport rule
	 * that a partially visible final row counts as visible.
	 */
	public static RowLayout solveRows (
			int outerWidth,
			int outerHeight,
			int headerHeight,
			long logicalRows,
			int sampleRowHeight,
			long logicalContentWidth,
			int horizontalBarHeight,
			int verticalBarWidth,
			int horizontalPolicy,
			int verticalPolicy) {
		if (outerWidth < 0 || outerHeight < 0) {
			throw new IllegalArgumentException ("negative viewport extent");
		}
		if (headerHeight < 0 || headerHeight > outerHeight) {
			throw new IllegalArgumentException ("invalid header extent");
		}
		if (logicalRows < 0 || logicalContentWidth < 0) {
			throw new IllegalArgumentException ("negative logical extent");
		}
		if (sampleRowHeight <= 0) {
			throw new IllegalArgumentException ("non-positive sample row extent");
		}
		if (horizontalBarHeight < 0 || verticalBarWidth < 0) {
			throw new IllegalArgumentException ("negative scrollbar extent");
		}
		checkPolicy (horizontalPolicy);
		checkPolicy (verticalPolicy);

		boolean horizontal = horizontalPolicy == ALWAYS;
		boolean vertical = verticalPolicy == ALWAYS;
		for (int pass = 0; pass < 4; pass++) {
			int bodyWidth = Math.max (0, outerWidth - (vertical ? verticalBarWidth : 0));
			int bodyHeight = Math.max (
					0, outerHeight - headerHeight - (horizontal ? horizontalBarHeight : 0));
			boolean nextHorizontal = policyVisible (
					horizontalPolicy, logicalContentWidth > bodyWidth);
			int visibleRows = visibleRows (logicalRows, sampleRowHeight, bodyHeight);
			boolean nextVertical = policyVisible (verticalPolicy, logicalRows > visibleRows);
            if (horizontal == nextHorizontal && vertical == nextVertical) {
                break;
            }
			horizontal = nextHorizontal;
			vertical = nextVertical;
		}

		int bodyWidth = Math.max (0, outerWidth - (vertical ? verticalBarWidth : 0));
		int bodyHeight = Math.max (
				0, outerHeight - headerHeight - (horizontal ? horizontalBarHeight : 0));
		int visibleRows = bodyWidth == 0 ? 0 : visibleRows (logicalRows, sampleRowHeight, bodyHeight);
		return new RowLayout (
				horizontal,
				vertical,
				horizontal && vertical,
				bodyWidth,
				bodyHeight,
				bodyWidth,
				headerHeight,
				visibleRows,
				logicalRows,
				saturatedMultiply (logicalRows, sampleRowHeight),
				logicalContentWidth);
	}

	private static boolean policyVisible (int policy, boolean automatic) {
		return switch (policy) {
			case ALWAYS -> true;
			case NEVER -> false;
			default -> automatic;
		};
	}

	private static int visibleRows (long logicalRows, int sampleRowHeight, int bodyHeight) {
        if (logicalRows == 0 || bodyHeight == 0) {
            return 0;
        }
		long rows = ((long)bodyHeight + sampleRowHeight - 1L) / sampleRowHeight;
		return (int)Math.min (logicalRows, Math.max (1L, rows));
	}

	/** Saturating addition for the nonnegative row coordinates and counts above. */
	private static long saturatedAdd (long value, int increment) {
		return value > Long.MAX_VALUE - increment ? Long.MAX_VALUE : value + increment;
	}

	private static long saturatedMultiply (long value, int multiplier) {
        if (value == 0) {
            return 0;
        }
        if (value > Long.MAX_VALUE / multiplier) {
            return Long.MAX_VALUE;
        }
		return value * multiplier;
	}

	private static void checkPolicy (int policy) {
		if (policy != AUTO && policy != ALWAYS && policy != NEVER) {
			throw new IllegalArgumentException ("invalid scrollbar policy");
		}
	}

	private static void requireFinite (double x, double y) {
		if (!Double.isFinite (x) || !Double.isFinite (y)) {
			throw new IllegalArgumentException ("non-finite viewport origin");
		}
	}
}

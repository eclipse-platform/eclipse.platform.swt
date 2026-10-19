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

import org.eclipse.swt.internal.*;

/**
 * Compatibility adapter over the shared viewport runtime for flat virtual
 * widgets.
 *
 * <p>Selection remains widget-specific, while visible/overscan coordinates are
 * owned by {@link ViewportRuntime.RowWindow}. Native TableItem/TreeItem facades
 * remain identity-pinned once exposed.</p>
 */
final class VirtualViewportPlanner {
	static final int DEFAULT_OVERSCAN_ROWS = ViewportRuntime.RowWindow.DEFAULT_OVERSCAN_ROWS;

	private final VirtualSelectionModel selection;
	private final VirtualScrollMetrics scrollMetrics = new VirtualScrollMetrics ();
	private final ViewportRuntime.RowWindow window = new ViewportRuntime.RowWindow ();
	private int repaintLockDepth;
	private int generation;
	private HorizontalWindow horizontal;

	HorizontalWindow horizontal () {
		if (horizontal == null) horizontal = new HorizontalWindow ();
		return horizontal;
	}

	/** Hybrid selection with the measured cut-over (SWT #119; see {@link VirtualSelectionModel#VirtualSelectionModel()}). */
	VirtualViewportPlanner () {
		selection = new VirtualSelectionModel ();
	}

	/** {@code hybridSelection}: ranges plus bitmap selection containers with the eager cut-over, or ranges only (SWT #129). */
	VirtualViewportPlanner (boolean hybridSelection) {
		selection = new VirtualSelectionModel (hybridSelection);
	}

	int logicalCount () {
		return Math.toIntExact (window.logicalCount ());
	}

	void setLogicalCount (int count) {
		if (count < 0) throw new IllegalArgumentException ("negative logical count");
		if (count == logicalCount ()) return;
		selection.setLogicalCount (count);
		mutateWindow (() -> window.setLogicalCount (count));
	}

	void insert (int index, int count) {
		if (count < 0 || index < 0 || index > logicalCount ()) {
			throw new IllegalArgumentException ("invalid insert");
		}
		if (count == 0) return;
		selection.insert (index, count);
		mutateWindow (() -> window.insert (index, count));
	}

	void remove (int index, int count) {
		if (count < 0 || index < 0 || index > logicalCount () - count) {
			throw new IllegalArgumentException ("invalid remove");
		}
		if (count == 0) return;
		selection.remove (index, count);
		mutateWindow (() -> window.remove (index, count));
	}

	void setViewport (int first, int visibleRows) {
		mutateWindow (() -> window.setViewport (first, visibleRows));
	}

	void setUniformGeometry (
			int logicalRows, int sampleRowExtent, int viewportExtent, int requestedTopRow) {
		scrollMetrics.configure (logicalRows, sampleRowExtent, viewportExtent);
		setLogicalCount (logicalRows);
		int top = scrollMetrics.clampTopRow (requestedTopRow);
		setViewport (top, scrollMetrics.visibleRows ());
	}

	VirtualScrollMetrics scrollMetrics () {
		return scrollMetrics;
	}

	int firstVisible () {
		return Math.toIntExact (window.firstVisible ());
	}

	int visibleCount () {
		return window.visibleCount ();
	}

	void setOverscanRows (int rows) {
		mutateWindow (() -> window.setOverscanRows (rows));
	}

	int overscanRows () {
		return window.overscanRows ();
	}

	int paintStart () {
		return Math.toIntExact (window.paintStart ());
	}

	int paintEndExclusive () {
		return Math.toIntExact (window.paintEndExclusive ());
	}

	boolean isVisible (int index) {
		return window.isVisible (index);
	}

	boolean isPaintCandidate (int index) {
		return window.isPaintCandidate (index);
	}

	VirtualSelectionModel selection () {
		return selection;
	}

	void beginRepaintLock () {
		repaintLockDepth++;
	}

	void endRepaintLock () {
		if (repaintLockDepth == 0) throw new IllegalStateException ("repaint lock underflow");
		repaintLockDepth--;
		generation++;
	}

	boolean repaintLocked () {
		return repaintLockDepth != 0;
	}

	int generation () {
		return generation;
	}

	private void mutateWindow (Runnable mutation) {
		long before = window.generation ();
		mutation.run ();
		if (window.generation () != before) generation++;
	}

	/**
	 * Column-directory adapter for the shared viewport runtime. Logical pixels
	 * increase from the leading edge in display order. Public column widths and
	 * identities are never replaced by the clipped native projection.
	 *
	 * <p>Call {@link #setColumns} only when widths/order change, then use
	 * {@link #setViewport} when the existing layout/origin owner moves. Prefix
	 * construction is O(columns); warm seeks are O(log positiveColumns), allocate
	 * nothing, and never visit zero-width runs. The native adapter must actually
	 * apply the returned pixel clip: this directory alone bounds no surface.</p>
	 */
	static final class HorizontalWindow {
		private long [] widths = new long [0];
		private int [] order = new int [0];
		private long [] starts = new long [0];
		private long [] positiveEnds = new long [0];
		private int [] positiveColumns = new int [0];
		private long contentWidth, origin, paintStart, paintEnd;
		private int viewportWidth, overscanPixels, firstSlot, endSlot;
		private boolean rtl;
		private long geometryGeneration, generation;

		/** Invalid input/overflow leaves both the directory and window unchanged. */
		boolean setColumns (long [] widthsByLogicalColumn, int [] displayOrder) {
			Objects.requireNonNull (widthsByLogicalColumn);
			Objects.requireNonNull (displayOrder);
			int count = widthsByLogicalColumn.length;
			if (displayOrder.length != count) throw new IllegalArgumentException ("column order length");
			if (Arrays.equals (widths, widthsByLogicalColumn) && Arrays.equals (order, displayOrder)) return false;
			long [] nextWidths = widthsByLogicalColumn.clone ();
			int [] nextOrder = displayOrder.clone ();
			long [] nextStarts = new long [count];
			boolean [] seen = new boolean [count];
			int positiveCount = 0;
			for (long width : nextWidths) {
				if (width < 0) throw new IllegalArgumentException ("negative column width");
				if (width != 0) positiveCount++;
			}
			long [] nextEnds = new long [positiveCount];
			int [] nextColumns = new int [positiveCount];
			long total = 0;
			int slot = 0;
			for (int column : nextOrder) {
				if (column < 0 || column >= count || seen [column]) {
					throw new IllegalArgumentException ("column order is not a permutation");
				}
				seen [column] = true;
				nextStarts [column] = total;
				total = Math.addExact (total, nextWidths [column]);
				if (nextWidths [column] != 0) {
					nextEnds [slot] = total;
					nextColumns [slot++] = column;
				}
			}
			widths = nextWidths;
			order = nextOrder;
			starts = nextStarts;
			positiveEnds = nextEnds;
			positiveColumns = nextColumns;
			contentWidth = total;
			geometryGeneration++;
			generation++;
			updateWindow (clampOrigin (origin));
			return true;
		}

		/**
		 * Configures a pixel window, clamping the requested leading-edge origin.
		 * A zero-width viewport has no demand, including overscan. The maximum
		 * resident width must fit a native signed-int pixel extent.
		 */
		boolean setViewport (long requestedOrigin, int width, int overscan, boolean rightToLeft) {
			if (width < 0 || overscan < 0 || (long)width + 2L * overscan > Integer.MAX_VALUE) {
				throw new IllegalArgumentException ("invalid horizontal viewport extent");
			}
			long nextOrigin = Math.min (Math.max (0, requestedOrigin), Math.max (0, contentWidth - width));
			if (origin == nextOrigin && viewportWidth == width && overscanPixels == overscan && rtl == rightToLeft) return false;
			viewportWidth = width;
			overscanPixels = overscan;
			rtl = rightToLeft;
			updateWindow (nextOrigin);
			generation++;
			return true;
		}

		private long clampOrigin (long requestedOrigin) {
			return Math.min (Math.max (0, requestedOrigin), Math.max (0, contentWidth - viewportWidth));
		}

		private void updateWindow (long nextOrigin) {
			origin = nextOrigin;
			if (viewportWidth == 0 || contentWidth == 0) {
				paintStart = paintEnd = origin;
				firstSlot = endSlot = 0;
				return;
			}
			paintStart = Math.max (0, origin - overscanPixels);
			long visibleEnd = origin + Math.min ((long)viewportWidth, contentWidth - origin);
			paintEnd = visibleEnd + Math.min ((long)overscanPixels, contentWidth - visibleEnd);
			firstSlot = firstGreater (positiveEnds, paintStart);
			endSlot = firstAtLeast (positiveEnds, paintEnd) + 1;
		}

		int columnCount () { return widths.length; }
		long contentWidth () { return contentWidth; }
		long origin () { return origin; }
		int viewportWidth () { return viewportWidth; }
		boolean rightToLeft () { return rtl; }
		long paintStart () { return paintStart; }
		long paintEndExclusive () { return paintEnd; }
		int residentWidth () { return Math.toIntExact (paintEnd - paintStart); }
		int firstSlot () { return firstSlot; }
		int endSlot () { return endSlot; }
		long geometryGeneration () { return geometryGeneration; }
		long generation () { return generation; }
		int columnAtSlot (int slot) { return positiveColumns [Objects.checkIndex (slot, positiveColumns.length)]; }
		int columnAtDisplayIndex (int index) { return order [Objects.checkIndex (index, order.length)]; }
		long columnStart (int column) { return starts [Objects.checkIndex (column, starts.length)]; }
		long columnWidth (int column) { return widths [Objects.checkIndex (column, widths.length)]; }

		/** Full column's physical-left X in the viewport; may be far offscreen. */
		long viewportX (int column) {
			long start = columnStart (column);
			return rtl ? origin + viewportWidth - (start + widths [column]) : start - origin;
		}

		/** Physical-left X of the clipped column in the bounded resident plane. */
		int residentX (int column) {
			long start = columnStart (column);
			long left = Math.max (paintStart, start);
			long right = Math.min (paintEnd, start + widths [column]);
			return left >= right ? -1 : Math.toIntExact (rtl ? paintEnd - right : left - paintStart);
		}

		int residentColumnWidth (int column) {
			long start = columnStart (column);
			long left = Math.max (paintStart, start);
			long right = Math.min (paintEnd, start + widths [column]);
			return Math.toIntExact (Math.max (0, right - left));
		}

		/** Pixels skipped from the physical left edge of the full column. */
		long sourceOffset (int column) {
			long start = columnStart (column);
			long end = start + widths [column];
			long left = Math.max (paintStart, start);
			long right = Math.min (paintEnd, end);
			return left >= right ? -1 : rtl ? end - right : left - start;
		}

		/** Physical-left X of the resident plane relative to the viewport. */
		int residentViewportX () {
			return Math.toIntExact (rtl ? origin + viewportWidth - paintEnd : paintStart - origin);
		}

		/** Pixel hit tests use half-open coordinates; blank/outside pixels return -1. */
		long logicalAtViewportPixel (int x) {
			if (x < 0 || x >= viewportWidth) return -1;
			long logical = origin + (rtl ? viewportWidth - 1L - x : x);
			return logical < contentWidth ? logical : -1;
		}

		int columnAtViewportPixel (int x) {
			long logical = logicalAtViewportPixel (x);
			return logical < 0 ? -1 : positiveColumns [firstGreater (positiveEnds, logical)];
		}

		/** Oversized columns reveal their leading edge; zero-width columns do not scroll. */
		boolean ensureColumnVisible (int column) {
			long start = columnStart (column), width = widths [column];
			if (width == 0 || viewportWidth == 0) return false;
			long target = origin;
			if (start < origin || width > viewportWidth) target = start;
			else if (start + width > origin + viewportWidth) target = start + width - viewportWidth;
			return setViewport (target, viewportWidth, overscanPixels, rtl);
		}

		/* Standard half-open lower/upper bounds over the strictly increasing ends.
		 * The existing Synexia LongLowerBound owner uses the same range contract;
		 * these internal atoms avoid adding a runtime dependency or public owner.
		 */
		private static int firstAtLeast (long [] values, long target) {
			int low = 0, high = values.length;
			while (low < high) {
				int middle = low + ((high - low) >>> 1);
				if (values [middle] < target) low = middle + 1;
				else high = middle;
			}
			return low;
		}

		private static int firstGreater (long [] values, long target) {
			int low = 0, high = values.length;
			while (low < high) {
				int middle = low + ((high - low) >>> 1);
				if (values [middle] <= target) low = middle + 1;
				else high = middle;
			}
			return low;
		}
	}
}

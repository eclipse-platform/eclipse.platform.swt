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

import org.eclipse.swt.internal.*;

/**
 * Logical viewport over a virtual Tree's flat visible-row projection.
 *
 * <p>The tree keeps its topology/projection specialization, while the generic
 * visible + overscan window is owned by {@link ViewportRuntime.RowWindow}.
 * Native scrollbar ranges and native row residency remain projections of this
 * state, not its authority.</p>
 */
final class VirtualTreeViewport {
	static final int DEFAULT_OVERSCAN_ROWS = ViewportRuntime.RowWindow.DEFAULT_OVERSCAN_ROWS;
	static final int MAX_PAINT_ROWS = 16_384;
	private static final long ESTIMATED_DERIVED_ROW_BYTES = 4_096;


	private final VirtualTreeVisibleProjection projection;
	private final VirtualScrollMetrics scrollMetrics = new VirtualScrollMetrics ();
	private final ViewportRuntime.RowWindow window = new ViewportRuntime.RowWindow ();
	private long generation;
	private final int requestedOverscanRows;

	VirtualTreeViewport (VirtualTreeVisibleProjection projection) {
		this (projection, DEFAULT_OVERSCAN_ROWS);
	}

	VirtualTreeViewport (VirtualTreeVisibleProjection projection, int overscanRows) {
		if (projection == null) throw new IllegalArgumentException ("projection");
		if (overscanRows < 0) throw new IllegalArgumentException ("negative overscan");
		this.projection = projection;
		this.requestedOverscanRows = overscanRows;
		window.setOverscanRows (overscanRows);
	}

	void configureGeometry (int sampleRowExtent, int viewportExtent) {
		// Validate the bounded paint window before changing scroll geometry.
		// An invalid resize must not poison native scrollbar state.
		validateVisibleBudget (projection.visibleRowCount (), sampleRowExtent, viewportExtent);
		long beforeTop = topRow ();
		int beforeRowExtent = scrollMetrics.sampleRowExtent ();
		int beforeViewportExtent = scrollMetrics.viewportExtent ();
		scrollMetrics.configure (projection.visibleRowCount (), sampleRowExtent, viewportExtent);
		long top = scrollMetrics.clampTopRow (beforeTop);
		syncWindow (top);
		if (beforeTop != top || beforeRowExtent != sampleRowExtent
				|| beforeViewportExtent != viewportExtent) generation++;
	}

	/**
	 * Automatic, parent-widget-owned memory-pressure admission. A Tree only
	 * materializes the paint window: the JVM heap cannot be sampled for every
	 * key/mouse callback, and native widget identities cannot be evicted.
	 *
	 * @return true only if effective overscan changed
	 */
	boolean refreshOverscanForMemoryPressure () {
		int visible = scrollMetrics.visibleRows ();
		if (visible > MAX_PAINT_ROWS) {
			throw new IllegalArgumentException ("visible tree exceeds bounded projection");
		}
		int cap = (MAX_PAINT_ROWS - visible) / 2;
		Runtime vm = Runtime.getRuntime ();
		long committed = vm.totalMemory ();
		long used = Math.max (0, committed - vm.freeMemory ());
		int desired = adaptiveOverscan (requestedOverscanRows, visible, cap,
				vm.maxMemory (), used);
		if (desired == window.overscanRows ()) return false;
		window.setOverscanRows (desired);
		return true;
	}

	/** Pure budget oracle; callable from existing SWT tests without native handles. */
	static int adaptiveOverscan (int wanted, int visible, int perSideCap,
			long maxHeap, long usedHeap) {
		if (wanted < 0 || visible < 0 || perSideCap < 0 || maxHeap <= 0
				|| usedHeap < 0 || usedHeap > maxHeap) {
			throw new IllegalArgumentException ("invalid tree viewport memory policy");
		}
		if (visible == 0 || wanted == 0) return 0;
		long reserve = Math.max (1L << 20, maxHeap / 16L);
		long free = maxHeap - usedHeap;
		long requiredVisible = (long)visible * ESTIMATED_DERIVED_ROW_BYTES;
		if (free <= reserve || requiredVisible >= free - reserve) return 0;
		long remainingRows = ((free - reserve - requiredVisible)
				/ ESTIMATED_DERIVED_ROW_BYTES) / 2L;
		return (int)Math.min (Math.min ((long)wanted, perSideCap), remainingRows);
	}

	void refreshLogicalRange () {
		long nextLogical = projection.visibleRowCount ();
		validateVisibleBudget (nextLogical, scrollMetrics.sampleRowExtent (),
				scrollMetrics.viewportExtent ());
		long beforeTop = topRow ();
		scrollMetrics.configure (
				nextLogical,
				scrollMetrics.sampleRowExtent (),
				scrollMetrics.viewportExtent ());
		long top = scrollMetrics.clampTopRow (beforeTop);
		syncWindow (top);
		if (beforeTop != top) generation++;
	}

	void setTopRow (long requested) {
		long next = scrollMetrics.clampTopRow (requested);
		if (next == topRow ()) {
			syncWindow (next);
			return;
		}
		syncWindow (next);
		generation++;
	}

	void setTopMaterializedId (int materializedId) {
		long row = projection.visibleIndexOf (materializedId);
		if (row >= 0) setTopRow (row);
	}

	long topRow () {
		return window.firstVisible ();
	}

	long visibleRowCount () {
		return scrollMetrics.logicalRowCount ();
	}

	int visibleRows () {
		return scrollMetrics.visibleRows ();
	}

	int scrollbarMaximum () {
		return scrollMetrics.maximum ();
	}

	int scrollbarThumb () {
		return scrollMetrics.thumb ();
	}

	int scrollbarPageIncrement () {
		return scrollMetrics.pageIncrement ();
	}

	int scrollbarSelection () {
		return scrollMetrics.selectionForTopRow (topRow ());
	}

	void setScrollbarSelection (int selection) {
		setTopRow (scrollMetrics.topRowForSelection (selection));
	}

	long firstPaintRow () {
		return window.paintStart ();
	}

	int paintRowCount () {
		return window.paintCount ();
	}

	VirtualTreeVisibleProjection.Row [] visibleWindow () {
		return projection.window (topRow (), visibleRows ());
	}

	VirtualTreeVisibleProjection.Row [] paintWindow () {
		return projection.window (firstPaintRow (), paintRowCount ());
	}

	/** Reuses caller-owned primitive storage for the current paint window and its ancestors. */
	void paintResidency (VirtualTreeVisibleProjection.Residency target) {
		projection.residencyWindow (firstPaintRow (), paintRowCount (), target);
	}

	void ensureVisible (long row) {
		long total = visibleRowCount ();
		if (row < 0 || row >= total) {
			throw new IllegalArgumentException ("row outside viewport model");
		}
		int visible = visibleRows ();
		if (visible <= 0) {
			setTopRow (row);
			return;
		}
		if (row < topRow ()) {
			setTopRow (row);
			return;
		}
		long end = saturatedAdd (topRow (), visible);
		if (row >= end) setTopRow (row - visible + 1L);
	}

	long generation () {
		return generation + window.generation () + projection.generation ();
	}

	/** Parent-owned widget bit revision. Does not imply geometry repaint/rebuild. */
	long presentationGeneration () {
		return projection.presentationGeneration ();
	}

	/**
	 * GTK physical pixel adjustments lose row precision beyond the native
	 * integer control range. Use VirtualScrollMetrics' bounded model projection
	 * only in that case; ordinary trees retain sub-row pixel wheel semantics.
	 */
	boolean usesScaledNativeScrollbar () {
		return visibleRowCount () > Integer.MAX_VALUE;
	}

	private void syncWindow (long top) {
		window.setLogicalCount (scrollMetrics.logicalRowCount ());
		window.setViewport (top, scrollMetrics.visibleRows ());
		// The Tree is the viewport owner: every accepted geometry or logical
		// scroll transition updates its disposable overscan automatically.
		// The existing explicit paint-time refresh remains useful when heap
		// pressure changes without a scroll. Neither path evicts native Item IDs.
		refreshOverscanForMemoryPressure ();
	}

	private static void validateVisibleBudget (
			long logicalRows, int sampleRowExtent, int viewportExtent) {
		if (logicalRows < 0) throw new IllegalArgumentException ("negative logical rows");
		if (sampleRowExtent <= 0 || viewportExtent < 0) {
			throw new IllegalArgumentException ("invalid viewport geometry");
		}
		if (logicalRows == 0 || viewportExtent == 0) return;
		long visible = Math.min (logicalRows,
				((long)viewportExtent + sampleRowExtent - 1L) / sampleRowExtent);
		if (visible > MAX_PAINT_ROWS) {
			throw new IllegalArgumentException ("visible tree exceeds bounded projection");
		}
	}

	private static long saturatedAdd (long value, int delta) {
		if (delta > 0 && value > Long.MAX_VALUE - delta) return Long.MAX_VALUE;
		return value + delta;
	}
}

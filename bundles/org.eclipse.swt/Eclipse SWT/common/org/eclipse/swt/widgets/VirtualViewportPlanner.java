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

	VirtualViewportPlanner () {
		this (false);
	}

	/** {@code hybridSelection}: ranges plus bitmap selection containers (see {@link VirtualSelectionModel}). */
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
}

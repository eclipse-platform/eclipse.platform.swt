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

import org.eclipse.swt.*;

/**
 * Variable row extents with an estimate for unmeasured rows (enhanced M3 API), for widgets that
 * draw rows of different heights (owner-drawn lists, wrapped text, custom row containers).
 * <p>
 * Rows that were never measured count as the estimate, so the scrollable extent is known before
 * every row is laid out. Only measured rows are stored: their deltas from the estimate live in a
 * Fenwick tree, so with m measured rows an offset or a re-measurement costs O(log m) and no
 * per-row object exists. {@link #setExtentAnchored(int, int, long, boolean)} tells the caller how
 * far to move its scroll offset so that the content in view does not jump when a row above it is
 * measured.
 * </p>
 * <p>
 * Instances are not thread-safe.
 * </p>
 *
 * @since 3.138
 */
public final class M3RowExtents {

	/** Alignment of {@link #scrollOffsetFor}: the smallest move that reveals the row (none when it is revealed). */
	public static final int ALIGN_AUTO = VirtualRowExtents.ALIGN_AUTO;
	/** Alignment of {@link #scrollOffsetFor}: the row at the top. */
	public static final int ALIGN_START = VirtualRowExtents.ALIGN_START;
	/** Alignment of {@link #scrollOffsetFor}: the row in the middle. */
	public static final int ALIGN_CENTER = VirtualRowExtents.ALIGN_CENTER;
	/** Alignment of {@link #scrollOffsetFor}: the row at the bottom. */
	public static final int ALIGN_END = VirtualRowExtents.ALIGN_END;
	/** Alignment of {@link #scrollOffsetFor}: unchanged when revealed, centered otherwise. */
	public static final int ALIGN_SMART = VirtualRowExtents.ALIGN_SMART;

	private final VirtualRowExtents extents;

	/**
	 * Constructs the extents of <code>rows</code> unmeasured rows.
	 *
	 * @param rows the number of rows, not negative
	 * @param estimate the extent of an unmeasured row, at least 1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the rows are negative or the estimate is below 1</li>
	 * </ul>
	 */
	public M3RowExtents (int rows, int estimate) {
		if (rows < 0 || estimate < 1) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		extents = new VirtualRowExtents (estimate);
		extents.reset (rows);
	}

	/**
	 * Returns the number of rows.
	 *
	 * @return the row count
	 */
	public int rowCount () {
		return extents.rowCount ();
	}

	/**
	 * Returns the extent of an unmeasured row.
	 *
	 * @return the estimate
	 */
	public int estimate () {
		return extents.estimate ();
	}

	/**
	 * Returns the number of measured rows.
	 *
	 * @return the measured count
	 */
	public int measuredCount () {
		return extents.measuredCount ();
	}

	/**
	 * Returns whether the row was measured.
	 *
	 * @param row the row
	 * @return <code>true</code> when measured
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a row</li>
	 * </ul>
	 */
	public boolean isMeasured (int row) {
		checkRow (row);
		return extents.isMeasured (row);
	}

	/**
	 * Returns the extent of a row: its measurement, or the estimate.
	 *
	 * @param row the row
	 * @return the extent
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a row</li>
	 * </ul>
	 */
	public int extentOf (int row) {
		checkRow (row);
		return extents.extentOf (row);
	}

	/**
	 * Sets the extent of unmeasured rows.
	 *
	 * @param estimate the estimate, at least 1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the estimate is below 1</li>
	 * </ul>
	 */
	public void setEstimate (int estimate) {
		if (estimate < 1) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		extents.setEstimate (estimate);
	}

	/**
	 * Records the measured extent of a row.
	 *
	 * @param row the row
	 * @param extent the extent, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a row</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the extent is negative</li>
	 * </ul>
	 */
	public void setExtent (int row, int extent) {
		checkRow (row);
		if (extent < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		extents.setExtent (row, extent);
	}

	/**
	 * Returns a row to the estimate.
	 *
	 * @param row the row
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a row</li>
	 * </ul>
	 */
	public void clearExtent (int row) {
		checkRow (row);
		extents.clearExtent (row);
	}

	/**
	 * Returns every row from <code>row</code> on to the estimate.
	 *
	 * @param row the first row, <code>0 &lt;= row &lt;= rowCount()</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is outside the rows</li>
	 * </ul>
	 */
	public void clearFrom (int row) {
		if (row < 0 || row > rowCount ()) SWT.error (SWT.ERROR_INVALID_RANGE);
		extents.clearFrom (row);
	}

	/**
	 * Opens <code>rows</code> unmeasured rows at <code>index</code>; later measurements shift.
	 *
	 * @param index the first new row, <code>0 &lt;= index &lt;= rowCount()</code>
	 * @param rows the number of rows, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the index is outside the rows</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the rows are negative</li>
	 * </ul>
	 */
	public void insert (int index, int rows) {
		if (index < 0 || index > rowCount ()) SWT.error (SWT.ERROR_INVALID_RANGE);
		if (rows < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		extents.insert (index, rows);
	}

	/**
	 * Deletes <code>rows</code> rows at <code>index</code>; later measurements shift.
	 *
	 * @param index the first deleted row
	 * @param rows the number of rows, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the rows are not all rows</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the rows are negative</li>
	 * </ul>
	 */
	public void remove (int index, int rows) {
		if (rows < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		if (index < 0 || index > rowCount () - rows) SWT.error (SWT.ERROR_INVALID_RANGE);
		extents.remove (index, rows);
	}

	/**
	 * Forgets every measurement and sets the row count.
	 *
	 * @param rows the number of rows, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the rows are negative</li>
	 * </ul>
	 */
	public void reset (int rows) {
		if (rows < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		extents.reset (rows);
	}

	/**
	 * Returns the start offset of a row; <code>offsetOf(rowCount())</code> is the total extent.
	 *
	 * @param row the row, <code>0 &lt;= row &lt;= rowCount()</code>
	 * @return the offset
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is outside the rows</li>
	 * </ul>
	 */
	public long offsetOf (int row) {
		if (row < 0 || row > rowCount ()) SWT.error (SWT.ERROR_INVALID_RANGE);
		return extents.offsetOf (row);
	}

	/**
	 * Returns the total extent of the rows.
	 *
	 * @return the total extent
	 */
	public long totalExtent () {
		return extents.totalExtent ();
	}

	/**
	 * Returns the row that contains an offset, clamped to the first or last row; -1 without rows.
	 *
	 * @param offset the offset
	 * @return the row, or -1
	 */
	public int rowAt (long offset) {
		return extents.rowAt (offset);
	}

	/**
	 * Returns the rows that intersect <code>[offset, offset + viewport)</code>.
	 *
	 * @param offset the scroll offset
	 * @param viewport the viewport extent, not negative
	 * @return the rows (empty without rows or with an empty viewport)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the viewport is negative</li>
	 * </ul>
	 */
	public M3ItemRange visibleRange (long offset, int viewport) {
		if (viewport < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		int [] out = new int [2];
		extents.visibleRange (offset, viewport, out);
		return M3ItemRange.of (out [0], out [1] - 1);
	}

	/**
	 * Returns the largest scroll offset for a viewport.
	 *
	 * @param viewport the viewport extent
	 * @return the maximum offset, at least 0
	 */
	public long maxScrollOffset (int viewport) {
		return extents.maxScrollOffset (viewport);
	}

	/**
	 * Returns the scroll offset that reveals a row with an alignment, clamped to the scrollable
	 * range; a row taller than the viewport is revealed while it fills the viewport.
	 *
	 * @param row the row
	 * @param align one of the <code>ALIGN_</code> constants
	 * @param current the current scroll offset
	 * @param viewport the viewport extent
	 * @return the scroll offset
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a row</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the alignment is not one of the constants</li>
	 * </ul>
	 */
	public long scrollOffsetFor (int row, int align, long current, int viewport) {
		checkRow (row);
		if (align < ALIGN_AUTO || align > ALIGN_SMART) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		return extents.scrollOffsetFor (row, align, current, viewport);
	}

	/**
	 * Measures a row and returns how far the caller should move its scroll offset so the content
	 * in view does not jump: the whole change when a first measurement lands on a row that starts
	 * above the offset, or when a re-measured row lies entirely above it and the user is not
	 * scrolling backward; otherwise 0.
	 *
	 * @param row the row
	 * @param extent the measured extent, not negative
	 * @param scrollOffset the current scroll offset
	 * @param scrollingBackward whether the user scrolls toward the start
	 * @return the change to add to the scroll offset
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a row</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the extent is negative</li>
	 * </ul>
	 */
	public long setExtentAnchored (int row, int extent, long scrollOffset, boolean scrollingBackward) {
		checkRow (row);
		if (extent < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		return extents.setExtentAnchored (row, extent, scrollOffset, scrollingBackward);
	}

	private void checkRow (int row) {
		if (row < 0 || row >= rowCount ()) SWT.error (SWT.ERROR_INVALID_RANGE);
	}

	@Override
	public String toString () {
		return "M3RowExtents [" + rowCount () + " rows, " + measuredCount () + " measured, estimate " + estimate () + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}

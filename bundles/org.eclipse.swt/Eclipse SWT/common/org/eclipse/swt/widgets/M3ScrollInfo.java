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

/**
 * The vertical scroll state of a <code>Table</code>, <code>List</code> or <code>Tree</code>
 * at pixel level (enhanced M3 API), answered by {@link M3Scrolling#getScrollInfo(Table)} and
 * its overloads.
 * <p>
 * All rows have the same extent (<code>getItemHeight()</code>), so the content extent is
 * <code>rowCount * rowExtent</code> and the pixel position of the viewport is
 * <code>topIndex * rowExtent + pixelOffset</code>. The native modes align the top row, so
 * their pixel offset is 0; drawn modes may scroll by pixels. For a Tree, rows are the visible
 * rows (items whose ancestors are all expanded) and the top index is a visible-row index.
 * </p>
 * <p>
 * A value: immutable, computed when asked, holding no reference to the widget.
 * </p>
 *
 * @since 3.138
 */
public final class M3ScrollInfo {
	private final int topIndex;
	private final int pixelOffset;
	private final int rowExtent;
	private final int viewportExtent;
	private final int rowCount;

	M3ScrollInfo (int topIndex, int pixelOffset, int rowExtent, int viewportExtent, int rowCount) {
		this.topIndex = topIndex;
		this.pixelOffset = pixelOffset;
		this.rowExtent = rowExtent;
		this.viewportExtent = viewportExtent;
		this.rowCount = rowCount;
	}

	/**
	 * Returns the index of the first row that intersects the viewport (0 when there are no rows).
	 *
	 * @return the top index
	 */
	public int topIndex () {
		return topIndex;
	}

	/**
	 * Returns the pixels of the top row scrolled above the viewport; 0 in the native modes.
	 *
	 * @return the pixel offset
	 */
	public int pixelOffset () {
		return pixelOffset;
	}

	/**
	 * Returns the extent of one row in pixels (<code>getItemHeight()</code>).
	 *
	 * @return the row extent
	 */
	public int rowExtent () {
		return rowExtent;
	}

	/**
	 * Returns the extent of the item area in pixels: the client area below the header.
	 *
	 * @return the viewport extent
	 */
	public int viewportExtent () {
		return viewportExtent;
	}

	/**
	 * Returns the number of rows: items of a Table or List, visible rows of a Tree.
	 *
	 * @return the row count
	 */
	public int rowCount () {
		return rowCount;
	}

	/**
	 * Returns the number of rows that intersect the viewport, a partial last row included.
	 *
	 * @return the visible rows
	 */
	public int visibleRows () {
		return Math.min (rowCount, M3ItemSelection.rows (viewportExtent + pixelOffset, rowExtent));
	}

	/**
	 * Returns the number of rows the viewport shows completely; at least 1 when a row fits.
	 *
	 * @return the full rows
	 */
	public int fullRows () {
		return rowExtent <= 0 || viewportExtent <= 0 ? 0 : viewportExtent / rowExtent;
	}

	/**
	 * Returns the extent of all rows in pixels, <code>rowCount * rowExtent</code>.
	 *
	 * @return the content extent
	 */
	public long contentExtent () {
		return (long) rowCount * rowExtent;
	}

	/**
	 * Returns the pixel position of the viewport within the content,
	 * <code>topIndex * rowExtent + pixelOffset</code>.
	 *
	 * @return the pixel position
	 */
	public long pixelPosition () {
		return (long) topIndex * rowExtent + pixelOffset;
	}

	/**
	 * Returns the largest top index the widget accepts: the one that shows the last row at
	 * the bottom, <code>max(0, rowCount - fullRows)</code>.
	 *
	 * @return the maximum top index
	 */
	public int maxTopIndex () {
		return Math.max (0, rowCount - Math.max (1, fullRows ()));
	}

	@Override
	public String toString () {
		return "M3ScrollInfo [top " + topIndex + " +" + pixelOffset + "px, rows " + rowCount + " x " + rowExtent + "px, viewport " + viewportExtent + "px]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}
}

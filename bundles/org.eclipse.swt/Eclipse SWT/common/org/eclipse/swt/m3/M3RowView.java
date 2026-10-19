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
package org.eclipse.swt.m3;

/**
 * A view of the rows of an application's source (enhanced M3 API).
 * <p>
 * A view maps its rows (what a widget shows, <code>0 .. size() - 1</code>) to source rows
 * (<code>0 .. sourceSize() - 1</code>) and back. Views compose: {@link M3ColumnFilter} keeps the
 * source rows that pass its criteria, {@link M3ColumnSort} orders the rows of its input view, and
 * {@link M3RowWindow} exposes a window of its input view. Edits of the source may be reported to
 * any view of a chain; they go to the first view and flow down the chain as {@link M3RowChange}s.
 * </p>
 * <p>
 * Views keep their state in int and long lanes; no object is made per row.
 * </p>
 *
 * @since 3.138
 */
public interface M3RowView {

	/** Receives the changes of a view. */
	@FunctionalInterface
	interface Listener {
		/**
		 * Called after a view changed. The change object belongs to the view and is reused for the
		 * next change; listeners must not keep it.
		 *
		 * @param change the change
		 */
		void viewChanged (M3RowChange change);
	}

	/**
	 * Returns the number of rows of the view.
	 *
	 * @return the view size
	 */
	int size ();

	/**
	 * Returns the source row shown by a view row.
	 *
	 * @param row the view row
	 * @return the source row
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a row of the view</li>
	 * </ul>
	 */
	int map (int row);

	/**
	 * Returns the view row of a source row, or -1 when the view does not show it.
	 *
	 * @param sourceRow the source row
	 * @return the view row, or -1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a source row</li>
	 * </ul>
	 */
	int unmap (int sourceRow);

	/**
	 * Returns the number of source rows.
	 *
	 * @return the source size
	 */
	int sourceSize ();

	/**
	 * Replaces the source by one of <code>sourceSize</code> rows whose values may all have changed.
	 *
	 * @param sourceSize the number of source rows, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the size is negative</li>
	 * </ul>
	 */
	void setSourceSize (int sourceSize);

	/**
	 * Reports that <code>count</code> source rows were inserted at <code>start</code>; the source
	 * rows from <code>start</code> on move up by <code>count</code>.
	 *
	 * @param start the first inserted source row
	 * @param count the number of inserted rows, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if start is not in <code>0 .. sourceSize()</code> or count is negative</li>
	 * </ul>
	 */
	void rowsInserted (int start, int count);

	/**
	 * Reports that the <code>count</code> source rows from <code>start</code> were removed; the
	 * source rows after them move down by <code>count</code>.
	 *
	 * @param start the first removed source row
	 * @param count the number of removed rows, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the range is not inside the source</li>
	 * </ul>
	 */
	void rowsRemoved (int start, int count);

	/**
	 * Reports that the values of some source rows changed.
	 *
	 * @param sourceRows the changed source rows (duplicates are allowed)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the array is null</li>
	 *    <li>ERROR_INVALID_RANGE - if a row is not a source row</li>
	 * </ul>
	 */
	void rowsChanged (int... sourceRows);

	/**
	 * Adds a listener that is told about every change of the view.
	 *
	 * @param listener the listener
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 */
	void addListener (Listener listener);

	/**
	 * Removes a listener.
	 *
	 * @param listener the listener
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 */
	void removeListener (Listener listener);
}

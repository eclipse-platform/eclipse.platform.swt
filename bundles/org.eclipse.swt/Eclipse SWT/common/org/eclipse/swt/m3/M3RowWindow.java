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

import org.eclipse.swt.widgets.*;

/**
 * A window of an input view: its rows <code>first .. first + count - 1</code> (enhanced M3 API).
 * <p>
 * The window is the resident part of a longer view, for example the rows a virtual widget shows.
 * Changes of the input are passed on with their damage clipped to the window and expressed in
 * window rows, so a listener refreshes only the resident rows that changed.
 * </p>
 *
 * @since 3.138
 */
public final class M3RowWindow extends M3RowViewBase {

	private int first, count;

	/**
	 * Constructs an empty window at the start of an input view.
	 *
	 * @param input the input view
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the input is null</li>
	 * </ul>
	 */
	public M3RowWindow (M3RowView input) {
		super (M3Checks.nonNull (input), 0);
	}

	/**
	 * Moves the window. Rows past the end of the input are not part of the window.
	 *
	 * @param first the first input row of the window, not negative
	 * @param count the number of rows, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if a value is negative</li>
	 * </ul>
	 */
	public void setWindow (int first, int count) {
		M3Checks.argument (first >= 0 && count >= 0);
		this.first = first;
		this.count = count;
		change.set (M3RowChange.RESET, 0, 0);
		change.damage (0, size () - 1);
		fire ();
	}

	/**
	 * Moves the window to a range of input rows (an empty range makes the window empty).
	 *
	 * @param range the input rows, for example a widget's visible range
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the range is null</li>
	 * </ul>
	 */
	public void setWindow (M3ItemRange range) {
		M3Checks.nonNull (range);
		setWindow (range.start (), range.size ());
	}

	/**
	 * Returns the input rows of the window as a range (empty when the window is empty).
	 *
	 * @return the range
	 */
	public M3ItemRange getRange () {
		return M3ItemRange.of (first, first + size () - 1);
	}

	/**
	 * Returns the first input row of the window.
	 *
	 * @return the first row
	 */
	public int getFirst () {
		return first;
	}

	/**
	 * Returns the number of rows the window asks for (the window is shorter at the end of the input).
	 *
	 * @return the requested count
	 */
	public int getCount () {
		return count;
	}

	@Override
	public int size () {
		return Math.max (0, Math.min (count, input.size () - first));
	}

	@Override
	public int map (int row) {
		return input.map (first + M3Checks.range (row, size ()));
	}

	@Override
	public int unmap (int sourceRow) {
		int row = input.unmap (sourceRow) - first;
		return row >= 0 && row < size () ? row : -1;
	}

	@Override
	void inputChanged (M3RowChange in) {
		change.set (in.kind, in.start, in.count);
		for (int i = 0; i < in.rowCount; i++) change.add (in.rows [i]);
		int last = size () - 1;
		if (in.kind == M3RowChange.RESET) {
			change.damage (0, last);
		} else if (in.firstDamaged >= 0) {
			change.damage (Math.max (in.firstDamaged - first, 0), Math.min (in.lastDamaged - first, last));
		}
		fire ();
	}

	@Override
	public String toString () {
		return "M3RowWindow [" + first + ", " + size () + " rows]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}

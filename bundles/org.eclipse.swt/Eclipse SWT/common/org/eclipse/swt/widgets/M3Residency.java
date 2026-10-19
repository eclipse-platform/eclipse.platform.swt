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
 * Residency views of <code>Table</code>, <code>List</code> and <code>Tree</code> (enhanced M3
 * API): which rows the widget holds ready to paint and how many item objects exist.
 * <p>
 * With a viewport the widgets keep a bounded window of rows and make item objects (facades) on
 * demand: a List keeps its native rows in a window, a Tree keeps a paint window with overscan,
 * and a Table holds its facades strongly in a ring of recent rows and weakly after that. These
 * views make that observable for diagnostics and tests. They never make an item and never move
 * the window. Without a viewport every row is resident.
 * </p>
 * <p>
 * The win32 widgets answer. On other platforms {@link #residentRange(Table)} is every row, the
 * facade counts are -1 (unknown) and {@link #isMaterialized(Table, int)} answers only for tables
 * without <code>SWT.VIRTUAL</code>.
 * </p>
 *
 * @since 3.138
 */
public final class M3Residency {

	/** Index in {@link #stats(Table)}: the number of rows of the resident range. */
	public static final int RESIDENT_ROWS = 0;
	/** Index in {@link #stats(Table)}: item objects alive, strongly or weakly reachable (-1 when unknown). */
	public static final int FACADES = 1;
	/** Index in {@link #stats(Table)}: item objects the widget holds strongly (-1 when unknown). */
	public static final int HELD_FACADES = 2;

	private M3Residency () {
	}

	/**
	 * Returns the rows the table holds ready to paint: with the viewport (a table without
	 * <code>SWT.VIRTUAL</code>; a virtual table keeps the stock owner-data path), the rows of the last
	 * native cache hint (empty before the first paint); otherwise every row.
	 *
	 * @param table the table
	 * @return the resident rows
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public static M3ItemRange residentRange (Table table) {
		check (table);
		return range (table.m3ResidentRange (), table.getItemCount ());
	}

	/**
	 * Returns the rows the list holds as native rows: with the viewport, its row window;
	 * otherwise every row.
	 *
	 * @param list the list
	 * @return the resident rows
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the list has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the list</li>
	 * </ul>
	 */
	public static M3ItemRange residentRange (List list) {
		check (list);
		return range (list.m3ResidentRange (), list.getItemCount ());
	}

	/**
	 * Returns the visible rows (as numbered by {@link M3RowMath}) the tree holds ready to paint:
	 * with the viewport, its paint window including the overscan; otherwise every visible row.
	 *
	 * @param tree the tree
	 * @return the resident visible rows
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 * @exception ArithmeticException if a resident row is beyond <code>Integer.MAX_VALUE</code>
	 */
	public static M3ItemRange residentRange (Tree tree) {
		check (tree);
		long [] window = tree.m3ResidentRange ();
		if (window == null) return M3ItemRange.of (0, Math.toIntExact (M3RowMath.visibleRowCount (tree) - 1));
		return window [1] < window [0] ? M3ItemRange.EMPTY : M3ItemRange.of (Math.toIntExact (window [0]), Math.toIntExact (window [1]));
	}

	/**
	 * Returns whether a <code>TableItem</code> exists for the row now. Asking never makes one.
	 *
	 * @param table the table
	 * @param index the row
	 * @return <code>true</code> when the row has an item object
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table is null</li>
	 *    <li>ERROR_INVALID_RANGE - if the index is not a row of the table</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 *    <li>ERROR_NOT_IMPLEMENTED - for a virtual table on a platform without the residency seam</li>
	 * </ul>
	 */
	public static boolean isMaterialized (Table table, int index) {
		check (table);
		if (index < 0 || index >= table.getItemCount ()) SWT.error (SWT.ERROR_INVALID_RANGE);
		int answer = table.m3Materialized (index);
		if (answer >= 0) return answer == 1;
		if ((table.getStyle () & SWT.VIRTUAL) == 0) return true;
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED);
		return false;
	}

	/**
	 * Returns the residency counts of the table, indexed by {@link #RESIDENT_ROWS},
	 * {@link #FACADES} and {@link #HELD_FACADES}. One pass over the item lanes, O(rows): for
	 * diagnostics and tests.
	 *
	 * @param table the table
	 * @return a new array of the counts
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public static long [] stats (Table table) {
		return new long [] {residentRange (table).size (), table.m3Facades (false), table.m3Facades (true)};
	}

	/**
	 * Returns the residency counts of the list (see {@link #stats(Table)}); a list has no item
	 * objects, so both facade counts are 0.
	 *
	 * @param list the list
	 * @return a new array of the counts
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the list has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the list</li>
	 * </ul>
	 */
	public static long [] stats (List list) {
		return new long [] {residentRange (list).size (), 0, 0};
	}

	/**
	 * Returns the residency counts of the tree (see {@link #stats(Table)}).
	 *
	 * @param tree the tree
	 * @return a new array of the counts
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static long [] stats (Tree tree) {
		return new long [] {residentRange (tree).size (), tree.m3Facades (false), tree.m3Facades (true)};
	}

	/* [first, last] from the seam, clamped to the rows; null means every row */
	private static M3ItemRange range (int [] window, int count) {
		if (window == null) return M3ItemRange.of (0, count - 1);
		int first = Math.max (0, window [0]), last = Math.min (window [1], count - 1);
		return M3ItemRange.of (first, last);
	}

	private static void check (Widget widget) {
		if (widget == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
	}
}

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
 * Row search of <code>Table</code>, <code>List</code> and <code>Tree</code> (enhanced M3 API):
 * the first row from a cursor whose text starts with or equals a string, ignoring case, with
 * optional wrap-around. This is the rule of the native list view search.
 * <p>
 * With the Table viewport the search reads the text lane and makes no <code>TableItem</code>;
 * a List has no item objects. Without the viewport a Table is scanned through
 * <code>getItem</code> (a virtual table fills the rows it passes), and a Tree reads the text of
 * its visible rows (numbered as by {@link M3RowMath}).
 * </p>
 *
 * @since 3.138
 */
public final class M3TypeAhead {

	private M3TypeAhead () {
	}

	/**
	 * Returns the first row at or after <code>from</code> (clamped to the rows) whose first-column
	 * text starts with (<code>prefix</code>) or equals <code>text</code>, ignoring case; with
	 * <code>wrap</code> the search continues from row 0 up to <code>from</code>.
	 *
	 * @param table the table
	 * @param text the text to find
	 * @param from the first row to look at
	 * @param prefix whether a row matches when its text starts with <code>text</code>
	 * @param wrap whether the search wraps around
	 * @return the row, or -1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table or the text is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public static int indexOf (Table table, String text, int from, boolean prefix, boolean wrap) {
		check (table, text);
		int row = table.m3IndexOf (text, from, prefix, wrap);
		if (row != -2) return row;
		int count = table.getItemCount ();
		int start = Math.max (0, Math.min (from, count));
		for (int k = 0; k < count; k++) {
			int index = next (start, k, count, wrap);
			if (index < 0) break;
			if (matches (table.getItem (index).getText (), text, prefix)) return index;
		}
		return -1;
	}

	/**
	 * Returns the first row of the list at or after <code>from</code> whose item starts with or
	 * equals <code>text</code>, ignoring case (see {@link #indexOf(Table, String, int, boolean, boolean)}).
	 *
	 * @param list the list
	 * @param text the text to find
	 * @param from the first row to look at
	 * @param prefix whether a row matches when its item starts with <code>text</code>
	 * @param wrap whether the search wraps around
	 * @return the row, or -1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list or the text is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the list has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the list</li>
	 * </ul>
	 */
	public static int indexOf (List list, String text, int from, boolean prefix, boolean wrap) {
		check (list, text);
		int count = list.getItemCount ();
		int start = Math.max (0, Math.min (from, count));
		for (int k = 0; k < count; k++) {
			int index = next (start, k, count, wrap);
			if (index < 0) break;
			if (matches (list.getItem (index), text, prefix)) return index;
		}
		return -1;
	}

	/**
	 * Returns the first visible row of the tree at or after <code>from</code> whose text starts
	 * with or equals <code>text</code>, ignoring case (see
	 * {@link #indexOf(Table, String, int, boolean, boolean)}).
	 *
	 * @param tree the tree
	 * @param text the text to find
	 * @param from the first visible row to look at
	 * @param prefix whether a row matches when its text starts with <code>text</code>
	 * @param wrap whether the search wraps around
	 * @return the visible row, or -1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree or the text is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static long indexOf (Tree tree, String text, long from, boolean prefix, boolean wrap) {
		check (tree, text);
		/* one preorder walk from the start row, then (wrap) one from row 0 up to it: O(rows), not O(rows) per row */
		int start = (int) Math.max (0, Math.min (from, Integer.MAX_VALUE));
		M3TreeWalk walk = new M3TreeWalk (tree);
		long row = walk.advance (start);
		for (TreeItem item = walk.next (); item != null; item = walk.next (), row++) {
			if (matches (item.getText (), text, prefix)) return row;
		}
		if (!wrap) return -1;
		walk = new M3TreeWalk (tree);
		row = 0;
		for (TreeItem item = walk.next (); item != null && row < start; item = walk.next (), row++) {
			if (matches (item.getText (), text, prefix)) return row;
		}
		return -1;
	}

	/* the k-th row after start, wrapping when asked; -1 past the end without wrap */
	private static int next (int start, int k, int count, boolean wrap) {
		int row = start + k;
		if (row < count) return row;
		return wrap ? row - count : -1;
	}

	private static boolean matches (String candidate, String text, boolean prefix) {
		return prefix ? candidate.regionMatches (true, 0, text, 0, text.length ()) : candidate.equalsIgnoreCase (text);
	}

	private static void check (Widget widget, String text) {
		if (widget == null || text == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
	}
}

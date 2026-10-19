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
 * Scroll memento of <code>Table</code>, <code>List</code> and <code>Tree</code> (enhanced M3 API):
 * the scroll position as one <code>long</code> that survives reloads and structural changes.
 * <p>
 * {@link #save(Table)} packs the anchor: the top row (for a Tree the visible row of
 * {@link M3RowMath}). {@link #remap(long, int, int)} moves the
 * anchor through an edit the caller makes (rows inserted or removed), and
 * {@link #restore(Table, long)} scrolls back to it, clamped to the rows. A long that
 * {@link #save(Table)} did not make is recognized by its tag and ignored.
 * </p>
 *
 * @since 3.138
 */
public final class M3ScrollMemento {

	/* tag (7 bits) | row (31 bits) | 24 reserved bits (0); bit 63 stays clear */
	private static final long TAG = 0x4DL;
	private static final int ROW_SHIFT = 24;
	private static final long ROW_MASK = 0x7FFF_FFFFL;
	private static final int TAG_SHIFT = 55;
	private static final long RESERVED = (1L << ROW_SHIFT) - 1;

	private M3ScrollMemento () {
	}

	/**
	 * Returns the scroll memento of the table.
	 *
	 * @param table the table
	 * @return the memento
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public static long save (Table table) {
		check (table);
		return pack (table.getTopIndex ());
	}

	/**
	 * Returns the scroll memento of the list.
	 *
	 * @param list the list
	 * @return the memento
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the list has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the list</li>
	 * </ul>
	 */
	public static long save (List list) {
		check (list);
		return pack (list.getTopIndex ());
	}

	/**
	 * Returns the scroll memento of the tree; the anchor is the visible row of the top item.
	 *
	 * @param tree the tree
	 * @return the memento
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static long save (Tree tree) {
		check (tree);
		TreeItem top = tree.getTopItem ();
		long row = top == null ? 0 : Math.max (0, M3RowMath.rowOf (top));
		return pack ((int) Math.min (row, ROW_MASK));
	}

	/**
	 * Moves the anchor of a memento through an edit: <code>delta</code> rows inserted at
	 * <code>index</code> when positive, <code>-delta</code> rows removed from <code>index</code>
	 * on when negative. An anchor below the edit shifts with it; a removed anchor becomes the row
	 * that took its place. A long that is not a memento is answered unchanged.
	 *
	 * @param memento the memento
	 * @param index the first row of the edit, not negative
	 * @param delta the rows inserted (positive) or removed (negative)
	 * @return the remapped memento
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the index is negative</li>
	 * </ul>
	 */
	public static long remap (long memento, int index, int delta) {
		if (index < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		if (!isMemento (memento) || delta == 0) return memento;
		long row = row (memento);
		if (delta > 0) {
			if (row >= index) row += delta;
		} else {
			long removedEnd = (long) index - delta;
			if (row >= removedEnd) {
				row += delta;
			} else if (row >= index) {
				row = index;
			}
		}
		return pack ((int) Math.min (Math.max (row, 0), ROW_MASK));
	}

	/**
	 * Scrolls the table back to a memento; the anchor is clamped to the rows.
	 *
	 * @param table the table
	 * @param memento the memento
	 * @return <code>false</code>, with nothing changed, when the long is not a memento
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public static boolean restore (Table table, long memento) {
		check (table);
		if (!isMemento (memento)) return false;
		int count = table.getItemCount ();
		if (count > 0) table.setTopIndex ((int) Math.min (row (memento), count - 1));
		return true;
	}

	/**
	 * Scrolls the list back to a memento; the anchor is clamped to the rows.
	 *
	 * @param list the list
	 * @param memento the memento
	 * @return <code>false</code>, with nothing changed, when the long is not a memento
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the list has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the list</li>
	 * </ul>
	 */
	public static boolean restore (List list, long memento) {
		check (list);
		if (!isMemento (memento)) return false;
		int count = list.getItemCount ();
		if (count > 0) list.setTopIndex ((int) Math.min (row (memento), count - 1));
		return true;
	}

	/**
	 * Scrolls the tree back to a memento: the item at the anchor's visible row (clamped to the
	 * visible rows) becomes the top item.
	 *
	 * @param tree the tree
	 * @param memento the memento
	 * @return <code>false</code>, with nothing changed, when the long is not a memento
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static boolean restore (Tree tree, long memento) {
		check (tree);
		if (!isMemento (memento)) return false;
		long count = M3RowMath.visibleRowCount (tree);
		if (count > 0) tree.setTopItem (M3RowMath.itemAt (tree, Math.min (row (memento), count - 1)));
		return true;
	}

	/* ------------------------------------------------------------------ packing */

	static long pack (int row) {
		return TAG << TAG_SHIFT | ((long) row & ROW_MASK) << ROW_SHIFT;
	}

	static boolean isMemento (long memento) {
		return memento >>> TAG_SHIFT == TAG && (memento & RESERVED) == 0;
	}

	private static long row (long memento) {
		return (memento >>> ROW_SHIFT) & ROW_MASK;
	}

	private static void check (Widget widget) {
		if (widget == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
	}
}

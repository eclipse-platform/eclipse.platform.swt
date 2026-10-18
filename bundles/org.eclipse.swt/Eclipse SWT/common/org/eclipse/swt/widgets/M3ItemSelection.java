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
 * Range-based selection and visibility queries of <code>Table</code>, <code>List</code> and
 * <code>Tree</code> (enhanced M3 API).
 * <p>
 * Every method is additive and opt-in: the widgets' own selection methods and events are
 * unchanged, and the answers are the same in every startup mode (see {@link M3Mode}). Ranges
 * are {@link M3ItemRange} values in normal form (sorted, disjoint, non-adjacent).
 * </p>
 * <ul>
 * <li><b>Table and List:</b> indices are row indices, as in <code>getSelectionIndices()</code>.</li>
 * <li><b>Tree selection:</b> indices are the indices of the children of one parent item, or of
 * the roots when the parent is <code>null</code>; selected items under other parents are not
 * part of the answer, and <code>setSelectionRanges</code> replaces the whole selection of the
 * tree with the given children.</li>
 * <li><b>Visible range:</b> the rows whose rectangles intersect the item area (the client area
 * below the header), so a partially shown last row is included. For a Tree the indices are
 * visible-row indices: the position in the row order of the items whose ancestors are all
 * expanded, roots first.</li>
 * </ul>
 * <p>
 * Setting a selection here never scrolls, unlike <code>setSelection</code>; scrolling is a
 * separate concern of <code>M3Scrolling</code>. No <code>SWT.Selection</code> event is sent,
 * as for every programmatic selection in SWT.
 * </p>
 *
 * @since 3.138
 */
public final class M3ItemSelection {

	private M3ItemSelection () {
	}

	/* ------------------------------------------------------------------ Table */

	/**
	 * Returns the selected rows of the table as ranges in normal form.
	 *
	 * @param table the table
	 * @return the selected ranges, empty when nothing is selected
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static M3ItemRange [] getSelectionRanges (Table table) {
		check (table);
		return M3ItemRange.coalesce (table.getSelectionIndices ());
	}

	/**
	 * Replaces the selection of the table with the rows of the ranges. Indices outside the
	 * table are ignored. The table is not scrolled.
	 *
	 * @param table the table
	 * @param ranges the ranges to select
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table, the array or one of its elements is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static void setSelectionRanges (Table table, M3ItemRange [] ranges) {
		check (table);
		checkRanges (ranges);
		table.deselectAll ();
		for (M3ItemRange range : ranges) select (table, range);
	}

	/**
	 * Selects the rows of the range, keeping the current selection. Indices outside the
	 * table are ignored.
	 *
	 * @param table the table
	 * @param range the range to select
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table or the range is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static void select (Table table, M3ItemRange range) {
		check (table);
		checkRange (range);
		if (!range.isEmpty ()) table.select (range.start (), range.end ());
	}

	/**
	 * Deselects the rows of the range. Indices outside the table are ignored.
	 *
	 * @param table the table
	 * @param range the range to deselect
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table or the range is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static void deselect (Table table, M3ItemRange range) {
		check (table);
		checkRange (range);
		if (!range.isEmpty ()) table.deselect (range.start (), range.end ());
	}

	/**
	 * Returns the rows of the table that intersect the item area, from the top index down,
	 * including a partially shown last row.
	 *
	 * @param table the table
	 * @return the visible rows, empty when no row is shown
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static M3ItemRange getVisibleRange (Table table) {
		check (table);
		int header = table.getHeaderVisible () ? table.getHeaderHeight () : 0;
		int area = table.getClientArea ().height - header;
		return window (table.getTopIndex (), table.getItemCount (), area, table.getItemHeight ());
	}

	/* ------------------------------------------------------------------ List */

	/**
	 * Returns the selected rows of the list as ranges in normal form.
	 *
	 * @param list the list
	 * @return the selected ranges, empty when nothing is selected
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static M3ItemRange [] getSelectionRanges (List list) {
		check (list);
		return M3ItemRange.coalesce (list.getSelectionIndices ());
	}

	/**
	 * Replaces the selection of the list with the rows of the ranges. Indices outside the
	 * list are ignored. The list is not scrolled.
	 *
	 * @param list the list
	 * @param ranges the ranges to select
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list, the array or one of its elements is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static void setSelectionRanges (List list, M3ItemRange [] ranges) {
		check (list);
		checkRanges (ranges);
		list.deselectAll ();
		for (M3ItemRange range : ranges) select (list, range);
	}

	/**
	 * Selects the rows of the range, keeping the current selection. Indices outside the
	 * list are ignored.
	 *
	 * @param list the list
	 * @param range the range to select
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list or the range is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static void select (List list, M3ItemRange range) {
		check (list);
		checkRange (range);
		if (!range.isEmpty ()) list.select (range.start (), range.end ());
	}

	/**
	 * Deselects the rows of the range. Indices outside the list are ignored.
	 *
	 * @param list the list
	 * @param range the range to deselect
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list or the range is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static void deselect (List list, M3ItemRange range) {
		check (list);
		checkRange (range);
		if (!range.isEmpty ()) list.deselect (range.start (), range.end ());
	}

	/**
	 * Returns the rows of the list that intersect the client area, from the top index down,
	 * including a partially shown last row.
	 *
	 * @param list the list
	 * @return the visible rows, empty when no row is shown
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static M3ItemRange getVisibleRange (List list) {
		check (list);
		return window (list.getTopIndex (), list.getItemCount (), list.getClientArea ().height, list.getItemHeight ());
	}

	/* ------------------------------------------------------------------ Tree */

	/**
	 * Returns the selected children of <code>parent</code> (the selected roots when the parent
	 * is <code>null</code>) as ranges of child indices in normal form.
	 *
	 * @param tree the tree
	 * @param parent the parent item, or <code>null</code> for the roots
	 * @return the selected ranges, empty when no child of the parent is selected
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 *    <li>ERROR_INVALID_PARENT - if the parent item does not belong to the tree</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver or the parent has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static M3ItemRange [] getSelectionRanges (Tree tree, TreeItem parent) {
		check (tree);
		checkParent (tree, parent);
		TreeItem [] selection = tree.getSelection ();
		int [] indices = new int [selection.length];
		int count = 0;
		for (TreeItem item : selection) {
			if (item.getParentItem () == parent) indices [count++] = indexOf (tree, parent, item);
		}
		return M3ItemRange.coalesce (indices, count);
	}

	/**
	 * Replaces the selection of the tree with the children of <code>parent</code> (the roots
	 * when the parent is <code>null</code>) whose indices lie in the ranges. Indices beyond
	 * the children are ignored. The tree is not scrolled.
	 *
	 * @param tree the tree
	 * @param parent the parent item, or <code>null</code> for the roots
	 * @param ranges the ranges of child indices to select
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree, the array or one of its elements is null</li>
	 *    <li>ERROR_INVALID_PARENT - if the parent item does not belong to the tree</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver or the parent has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static void setSelectionRanges (Tree tree, TreeItem parent, M3ItemRange [] ranges) {
		check (tree);
		checkParent (tree, parent);
		checkRanges (ranges);
		tree.deselectAll ();
		for (M3ItemRange range : ranges) select (tree, parent, range);
	}

	/**
	 * Selects the children of <code>parent</code> (the roots when the parent is
	 * <code>null</code>) whose indices lie in the range, keeping the current selection.
	 * Indices beyond the children are ignored.
	 *
	 * @param tree the tree
	 * @param parent the parent item, or <code>null</code> for the roots
	 * @param range the range of child indices to select
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree or the range is null</li>
	 *    <li>ERROR_INVALID_PARENT - if the parent item does not belong to the tree</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver or the parent has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static void select (Tree tree, TreeItem parent, M3ItemRange range) {
		check (tree);
		checkParent (tree, parent);
		checkRange (range);
		int last = Math.min (range.end (), childCount (tree, parent) - 1);
		for (int i = range.start (); i <= last; i++) tree.select (child (tree, parent, i));
	}

	/**
	 * Deselects the children of <code>parent</code> (the roots when the parent is
	 * <code>null</code>) whose indices lie in the range. Indices beyond the children are
	 * ignored.
	 *
	 * @param tree the tree
	 * @param parent the parent item, or <code>null</code> for the roots
	 * @param range the range of child indices to deselect
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree or the range is null</li>
	 *    <li>ERROR_INVALID_PARENT - if the parent item does not belong to the tree</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver or the parent has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static void deselect (Tree tree, TreeItem parent, M3ItemRange range) {
		check (tree);
		checkParent (tree, parent);
		checkRange (range);
		int last = Math.min (range.end (), childCount (tree, parent) - 1);
		for (int i = range.start (); i <= last; i++) tree.deselect (child (tree, parent, i));
	}

	/**
	 * Returns the visible rows of the tree that intersect the item area, as a range of
	 * visible-row indices from the top item down, including a partially shown last row.
	 * <p>
	 * The first index is the number of visible rows above the top item; this walks the rows
	 * above the top item once (O(rows above) with the public API; the viewport lanes answer it
	 * in O(log n) when active).
	 * </p>
	 *
	 * @param tree the tree
	 * @return the visible rows, empty when no row is shown
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static M3ItemRange getVisibleRange (Tree tree) {
		check (tree);
		TreeItem top = tree.getTopItem ();
		int rows = rows (itemAreaHeight (tree), tree.getItemHeight ());
		if (top == null || rows == 0) return M3ItemRange.EMPTY;
		M3TreeWalk walk = new M3TreeWalk (tree);
		int first = walk.indexOf (top);
		return new M3ItemRange (first, first + walk.advance (rows - 1));
	}

	/**
	 * Returns the visible rows of the tree that intersect the item area, as items from the
	 * top item down, including a partially shown last row.
	 *
	 * @param tree the tree
	 * @return the items of the visible rows, empty when no row is shown
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static TreeItem [] getVisibleItems (Tree tree) {
		M3ItemRange range = getVisibleRange (tree);
		TreeItem [] items = new TreeItem [range.size ()];
		M3TreeWalk walk = new M3TreeWalk (tree);
		walk.advance (range.start ());
		for (int i = 0; i < items.length; i++) items [i] = walk.next ();
		return items;
	}

	/* ------------------------------------------------------------------ arithmetic */

	/* Rows [top, top + rows - 1] clamped to the item count; EMPTY when nothing fits. */
	static M3ItemRange window (int top, int count, int areaHeight, int itemHeight) {
		int rows = rows (areaHeight, itemHeight);
		if (rows == 0 || top >= count) return M3ItemRange.EMPTY;
		return new M3ItemRange (top, Math.min (count - 1, top + rows - 1));
	}

	/* Number of rows that intersect an area, counting a partial last row. */
	static int rows (int areaHeight, int itemHeight) {
		int extent = Math.max (1, itemHeight);
		return areaHeight <= 0 ? 0 : (areaHeight + extent - 1) / extent;
	}

	private static int itemAreaHeight (Tree tree) {
		int header = tree.getHeaderVisible () ? tree.getHeaderHeight () : 0;
		return tree.getClientArea ().height - header;
	}

	private static int childCount (Tree tree, TreeItem parent) {
		return parent == null ? tree.getItemCount () : parent.getItemCount ();
	}

	private static TreeItem child (Tree tree, TreeItem parent, int index) {
		return parent == null ? tree.getItem (index) : parent.getItem (index);
	}

	private static int indexOf (Tree tree, TreeItem parent, TreeItem item) {
		return parent == null ? tree.indexOf (item) : parent.indexOf (item);
	}

	/* ------------------------------------------------------------------ checks */

	private static void check (Widget widget) {
		if (widget == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
	}

	private static void checkParent (Tree tree, TreeItem parent) {
		if (parent == null) return;
		parent.checkWidget ();
		if (parent.getParent () != tree) SWT.error (SWT.ERROR_INVALID_PARENT);
	}

	private static void checkRange (M3ItemRange range) {
		if (range == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	}

	private static void checkRanges (M3ItemRange [] ranges) {
		if (ranges == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		for (M3ItemRange range : ranges) checkRange (range);
	}
}

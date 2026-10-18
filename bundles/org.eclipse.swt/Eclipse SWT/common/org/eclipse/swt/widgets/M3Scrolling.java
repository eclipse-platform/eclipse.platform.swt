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
 * Pixel-level scroll queries and aligned scroll-to-row of <code>Table</code>, <code>List</code>
 * and <code>Tree</code> (enhanced M3 API).
 * <p>
 * Additive and opt-in: the widgets' own <code>setTopIndex</code>, <code>setTopItem</code> and
 * <code>showItem</code> are unchanged, and the answers are the same in every startup mode.
 * <code>scrollTo</code> places a row by one of four alignments and returns where the top
 * landed:
 * </p>
 * <ul>
 * <li>{@link #TOP}: the row becomes the top row;</li>
 * <li>{@link #BOTTOM}: the row becomes the last completely visible row;</li>
 * <li>{@link #CENTER}: the row is placed in the middle of the completely visible rows;</li>
 * <li>{@link #MIN_DISTANCE}: the shortest scroll that shows the row completely, no scroll when
 * it is already shown (the rule of <code>showItem</code>).</li>
 * </ul>
 * <p>
 * The target top index is clamped to <code>[0, maxTopIndex]</code>, like <code>setTopIndex</code>.
 * For a Tree the collapsed ancestors of the item are expanded first (as <code>showItem</code>
 * does), without scrolling.
 * </p>
 *
 * @since 3.138
 */
public final class M3Scrolling {

	/** Alignment: the row becomes the top row. */
	public static final int TOP = 0;
	/** Alignment: the row is placed in the middle of the completely visible rows. */
	public static final int CENTER = 1;
	/** Alignment: the row becomes the last completely visible row. */
	public static final int BOTTOM = 2;
	/** Alignment: the shortest scroll that shows the row completely. */
	public static final int MIN_DISTANCE = 3;

	private M3Scrolling () {
	}

	/* ------------------------------------------------------------------ Table */

	/**
	 * Returns the scroll state of the table.
	 *
	 * @param table the table
	 * @return the scroll state
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static M3ScrollInfo getScrollInfo (Table table) {
		check (table);
		int header = table.getHeaderVisible () ? table.getHeaderHeight () : 0;
		int area = table.getClientArea ().height - header;
		return new M3ScrollInfo (table.getTopIndex (), 0, table.getItemHeight (), area, table.getItemCount ());
	}

	/**
	 * Scrolls the table so that the row is placed by the alignment, and returns the resulting
	 * top index. The index is clamped to the rows; nothing happens when the table is empty.
	 *
	 * @param table the table
	 * @param index the row to show
	 * @param alignment one of {@link #TOP}, {@link #CENTER}, {@link #BOTTOM}, {@link #MIN_DISTANCE}
	 * @return the top index after scrolling
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the alignment is not one of the four</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static int scrollTo (Table table, int index, int alignment) {
		M3ScrollInfo info = getScrollInfo (table);
		checkAlignment (alignment);
		if (info.rowCount () == 0) return 0;
		table.setTopIndex (targetTop (info, index, alignment));
		return table.getTopIndex ();
	}

	/* ------------------------------------------------------------------ List */

	/**
	 * Returns the scroll state of the list.
	 *
	 * @param list the list
	 * @return the scroll state
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static M3ScrollInfo getScrollInfo (List list) {
		check (list);
		return new M3ScrollInfo (list.getTopIndex (), 0, list.getItemHeight (), list.getClientArea ().height, list.getItemCount ());
	}

	/**
	 * Scrolls the list so that the row is placed by the alignment, and returns the resulting
	 * top index. The index is clamped to the rows; nothing happens when the list is empty.
	 *
	 * @param list the list
	 * @param index the row to show
	 * @param alignment one of {@link #TOP}, {@link #CENTER}, {@link #BOTTOM}, {@link #MIN_DISTANCE}
	 * @return the top index after scrolling
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the alignment is not one of the four</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static int scrollTo (List list, int index, int alignment) {
		M3ScrollInfo info = getScrollInfo (list);
		checkAlignment (alignment);
		if (info.rowCount () == 0) return 0;
		list.setTopIndex (targetTop (info, index, alignment));
		return list.getTopIndex ();
	}

	/* ------------------------------------------------------------------ Tree */

	/**
	 * Returns the scroll state of the tree over its visible rows. With the public API this walks
	 * the visible rows once (O(rows)); the viewport lanes answer it in O(log n) when active.
	 *
	 * @param tree the tree
	 * @return the scroll state
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static M3ScrollInfo getScrollInfo (Tree tree) {
		check (tree);
		int header = tree.getHeaderVisible () ? tree.getHeaderHeight () : 0;
		int area = tree.getClientArea ().height - header;
		TreeItem top = tree.getTopItem ();
		M3TreeWalk walk = new M3TreeWalk (tree);
		int topIndex = top == null ? -1 : walk.indexOf (top);
		int rows = topIndex < 0 ? 0 : topIndex + 1 + walk.advance (Integer.MAX_VALUE);
		return new M3ScrollInfo (Math.max (0, topIndex), 0, tree.getItemHeight (), area, rows);
	}

	/**
	 * Expands the collapsed ancestors of the item, scrolls the tree so that the item's row is
	 * placed by the alignment, and returns the resulting top item.
	 *
	 * @param tree the tree
	 * @param item the item to show
	 * @param alignment one of {@link #TOP}, {@link #CENTER}, {@link #BOTTOM}, {@link #MIN_DISTANCE}
	 * @return the top item after scrolling
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree or the item is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the alignment is not one of the four</li>
	 *    <li>ERROR_INVALID_PARENT - if the item does not belong to the tree</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver or the item has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public static TreeItem scrollTo (Tree tree, TreeItem item, int alignment) {
		check (tree);
		checkItem (tree, item);
		checkAlignment (alignment);
		expandAncestors (item);
		M3ScrollInfo info = getScrollInfo (tree);
		M3TreeWalk walk = new M3TreeWalk (tree);
		int row = walk.indexOf (item);
		int target = targetTop (info, row, alignment);
		M3TreeWalk toTarget = new M3TreeWalk (tree);
		toTarget.advance (target);
		tree.setTopItem (toTarget.next ());
		return tree.getTopItem ();
	}

	/* ------------------------------------------------------------------ arithmetic */

	/* The top index that places <code>index</code> by the alignment, clamped like setTopIndex. */
	static int targetTop (M3ScrollInfo info, int index, int alignment) {
		int row = Math.max (0, Math.min (index, info.rowCount () - 1));
		int full = Math.max (1, info.fullRows ());
		int target;
		switch (alignment) {
			case TOP: target = row; break;
			case BOTTOM: target = row - full + 1; break;
			case CENTER: target = row - (full - 1) / 2; break;
			default: target = minDistance (info.topIndex (), full, row); break;
		}
		return Math.max (0, Math.min (target, info.maxTopIndex ()));
	}

	/* Draw2D reveal rule per axis: clamp the view into [row - full + 1, row], no move when inside. */
	private static int minDistance (int top, int full, int row) {
		if (row < top) return row;
		if (row > top + full - 1) return row - full + 1;
		return top;
	}

	private static void expandAncestors (TreeItem item) {
		for (TreeItem parent = item.getParentItem (); parent != null; parent = parent.getParentItem ()) {
			if (!parent.getExpanded ()) parent.setExpanded (true);
		}
	}

	/* ------------------------------------------------------------------ checks */

	private static void check (Widget widget) {
		if (widget == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
	}

	private static void checkItem (Tree tree, TreeItem item) {
		if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		item.checkWidget ();
		if (item.getParent () != tree) SWT.error (SWT.ERROR_INVALID_PARENT);
	}

	private static void checkAlignment (int alignment) {
		if (alignment < TOP || alignment > MIN_DISTANCE) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
}

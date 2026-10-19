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
 * Visible-row arithmetic of a <code>Tree</code> (enhanced M3 API).
 * <p>
 * The visible rows of a tree are its roots and, recursively, the children of expanded items,
 * in the order the tree shows them; row 0 is the first root. An item is visible when all its
 * ancestors are expanded (whether it is scrolled into view does not matter). Rows are
 * <code>long</code>: a virtual tree's visible cardinality is not bounded by one level's item
 * count.
 * </p>
 * <p>
 * With the tree viewport (an <code>SWT.VIRTUAL</code> tree in the viewport mode) the answers come
 * from the viewport's topology lanes in O(depth &times; log k), without walking rows or making
 * facades. Otherwise they come from one preorder walk over the public API, O(rows above the
 * target). The answers are the same in every startup mode.
 * </p>
 *
 * @since 3.138
 */
public final class M3RowMath {

	/* Answer of a platform seam that has no lanes for the question. */
	static final long UNKNOWN = -2;

	private M3RowMath () {
	}

	/**
	 * Returns the number of visible rows of the tree.
	 *
	 * @param tree the tree
	 * @return the visible row count
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static long visibleRowCount (Tree tree) {
		check (tree);
		long count = tree.m3VisibleRowCount ();
		return count != UNKNOWN ? count : new M3TreeWalk (tree).remaining ();
	}

	/**
	 * Returns the visible row of the item, or -1 when one of its ancestors is collapsed.
	 *
	 * @param item the item
	 * @return the row, or -1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the item has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the item</li>
	 * </ul>
	 */
	public static long rowOf (TreeItem item) {
		check (item);
		long row = item.getParent ().m3RowOf (item);
		if (row != UNKNOWN) return row;
		for (TreeItem ancestor = item.getParentItem (); ancestor != null; ancestor = ancestor.getParentItem ()) {
			if (!ancestor.getExpanded ()) return -1;
		}
		M3TreeWalk walk = new M3TreeWalk (item.getParent ());
		row = 0;
		for (TreeItem next = walk.next (); next != item; next = walk.next ()) {
			if (next == null) return -1;
			row++;
		}
		return row;
	}

	/**
	 * Returns the item at a visible row.
	 *
	 * @param tree the tree
	 * @param row the row, <code>0 &lt;= row &lt; visibleRowCount(tree)</code>
	 * @return the item
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a visible row</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static TreeItem itemAt (Tree tree, long row) {
		check (tree);
		if (row < 0) SWT.error (SWT.ERROR_INVALID_RANGE);
		long count = tree.m3VisibleRowCount ();
		if (count != UNKNOWN) {
			if (row >= count) SWT.error (SWT.ERROR_INVALID_RANGE);
			return tree.m3ItemAt (row);
		}
		M3TreeWalk walk = new M3TreeWalk (tree);
		TreeItem item = walk.next ();
		for (long skipped = 0; skipped < row && item != null; skipped++) item = walk.next ();
		if (item == null) SWT.error (SWT.ERROR_INVALID_RANGE);
		return item;
	}

	/**
	 * Returns the number of rows the item and its visible descendants take: 1 for a collapsed
	 * item, otherwise 1 plus the rows of its children, recursively. The count does not depend on
	 * whether the item itself is visible.
	 *
	 * @param item the item
	 * @return the subtree row count, at least 1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the item has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the item</li>
	 * </ul>
	 */
	public static long subtreeRowCount (TreeItem item) {
		check (item);
		long rows = item.getParent ().m3SubtreeRowCount (item);
		if (rows != UNKNOWN) return rows;
		return item.getExpanded () ? 1 + new M3TreeWalk (item).remaining () : 1;
	}

	/**
	 * Returns the pixel offset of the top of a row from the top of row 0. Tree rows share one
	 * height, so this is <code>row * getItemHeight()</code>; <code>row</code> may equal the visible
	 * row count (the offset of the end of the rows).
	 *
	 * @param tree the tree
	 * @param row the row, <code>0 &lt;= row &lt;= visibleRowCount(tree)</code>
	 * @return the offset in pixels
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 *    <li>ERROR_INVALID_RANGE - if the row is outside the rows</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static long rowExtent (Tree tree, long row) {
		if (row < 0 || row > visibleRowCount (tree)) SWT.error (SWT.ERROR_INVALID_RANGE);
		return Math.multiplyExact (row, (long) tree.getItemHeight ());
	}

	/**
	 * Returns the row whose pixels contain an offset from the top of row 0, or -1 when the
	 * offset is above row 0 or below the last row.
	 *
	 * @param tree the tree
	 * @param y the offset in pixels
	 * @return the row, or -1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static long rowAtExtent (Tree tree, long y) {
		long count = visibleRowCount (tree);
		if (y < 0) return -1;
		long row = y / Math.max (1, tree.getItemHeight ());
		return row < count ? row : -1;
	}

	private static void check (Widget widget) {
		if (widget == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
	}
}

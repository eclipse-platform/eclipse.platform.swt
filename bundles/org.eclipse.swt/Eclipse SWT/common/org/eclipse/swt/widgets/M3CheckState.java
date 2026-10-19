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

import java.util.*;

import org.eclipse.swt.*;

/**
 * Tri-state check dig-in for trees with <code>SWT.CHECK</code> (enhanced M3 API).
 * <p>
 * {@link #setChecked(TreeItem, boolean)} checks or unchecks an item together with its whole
 * subtree and recomputes every ancestor: an ancestor is {@link #CHECKED} when all its children
 * are checked, {@link #UNCHECKED} when none is checked or partial, and {@link #PARTIAL} (checked
 * and grayed) otherwise. The state is kept in the stock checked and grayed bits, so every
 * existing reader sees it. {@link #setDigIn(Tree, boolean)} applies the same rule to the check
 * toggles of the user (<code>SWT.Selection</code> with <code>detail == SWT.CHECK</code>);
 * <code>TreeItem.setChecked</code> called by the application stays a single-item change, as in
 * stock SWT.
 * </p>
 * <p>
 * In a tree without <code>SWT.CHECK</code> the items have no check state and nothing changes. A
 * virtual tree makes the items of the subtree it visits.
 * </p>
 *
 * @since 3.138
 */
public final class M3CheckState {

	/** The state of an item that is not checked. */
	public static final int UNCHECKED = 0;
	/** The state of an item that is checked and not grayed. */
	public static final int CHECKED = 1;
	/** The state of an item that is checked and grayed: some of its descendants are checked. */
	public static final int PARTIAL = 2;

	private static final String KEY = "org.eclipse.swt.widgets.M3CheckState"; //$NON-NLS-1$

	private M3CheckState () {
	}

	/**
	 * Returns the check state of an item: {@link #PARTIAL} when checked and grayed,
	 * {@link #CHECKED} when checked, {@link #UNCHECKED} otherwise.
	 *
	 * @param item the item
	 * @return the state
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the item has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the item</li>
	 * </ul>
	 */
	public static int state (TreeItem item) {
		if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		item.checkWidget ();
		if (!item.getChecked ()) return UNCHECKED;
		return item.getGrayed () ? PARTIAL : CHECKED;
	}

	/**
	 * Checks or unchecks the item and every descendant (none of them grayed), then recomputes
	 * every ancestor from its children.
	 *
	 * @param item the item
	 * @param checked whether to check
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the item has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the item</li>
	 * </ul>
	 */
	public static void setChecked (TreeItem item, boolean checked) {
		if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		item.checkWidget ();
		if ((item.getParent ().getStyle () & SWT.CHECK) == 0) return;
		/* the subtree, iteratively: an explicit stack instead of recursion */
		ArrayDeque<TreeItem> stack = new ArrayDeque<> ();
		stack.push (item);
		while (!stack.isEmpty ()) {
			TreeItem next = stack.pop ();
			next.setChecked (checked);
			next.setGrayed (false);
			for (TreeItem child : next.getItems ()) stack.push (child);
		}
		for (TreeItem parent = item.getParentItem (); parent != null; parent = parent.getParentItem ()) recompute (parent);
	}

	/**
	 * Turns dig-in on or off for the check toggles of the user: when on, a toggle checks or
	 * unchecks the subtree of the item and recomputes its ancestors, as
	 * {@link #setChecked(TreeItem, boolean)} does. Listeners added after this call see the
	 * propagated state.
	 *
	 * @param tree the tree
	 * @param digIn whether user toggles dig in
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static void setDigIn (Tree tree, boolean digIn) {
		if (tree == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		tree.checkWidget ();
		Listener listener = tree.getData (KEY) instanceof Listener installed ? installed : null;
		if (digIn == (listener != null)) return;
		if (digIn) {
			listener = event -> {
				if (event.detail == SWT.CHECK && event.item instanceof TreeItem item && !item.isDisposed ()) {
					setChecked (item, item.getChecked ());
				}
			};
			tree.setData (KEY, listener);
			tree.addListener (SWT.Selection, listener);
		} else {
			tree.removeListener (SWT.Selection, listener);
			tree.setData (KEY, null);
		}
	}

	/**
	 * Returns whether user toggles dig in.
	 *
	 * @param tree the tree
	 * @return <code>true</code> when dig-in is on
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static boolean isDigIn (Tree tree) {
		if (tree == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		tree.checkWidget ();
		return tree.getData (KEY) instanceof Listener;
	}

	/* an ancestor from its children: all checked, none checked, or partial */
	private static void recompute (TreeItem parent) {
		boolean any = false, all = true;
		for (TreeItem child : parent.getItems ()) {
			int state = state (child);
			any |= state != UNCHECKED;
			all &= state == CHECKED;
			if (any && !all) break;
		}
		parent.setChecked (any);
		parent.setGrayed (any && !all);
	}
}

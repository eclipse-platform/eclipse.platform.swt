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

/**
 * Cursor over the visible rows of a <code>Tree</code> in row order: the items whose
 * ancestors are all expanded, roots first, each item followed by its expanded subtree.
 * <p>
 * The state is two lanes indexed by depth (the parent facade and the next child index),
 * so a walk allocates nothing per row; only the depth lanes grow. Children are reached with
 * <code>getItem(int)</code> and never copied into arrays. This is the public-API path of the
 * M3 row queries; when the viewport is active the topology lanes can answer the same questions
 * in O(log n) (see M3-ROW-MATH in docs/m3/ENHANCED_API_MAP.tsv).
 * </p>
 */
final class M3TreeWalk {
	private final Tree tree;
	private TreeItem [] parents = new TreeItem [8];
	private int [] next = new int [8];
	/* depth of the frame to continue from; -1 when the walk is over */
	private int depth = 0;

	/** A walk positioned before the first root of <code>tree</code>. */
	M3TreeWalk (Tree tree) {
		this.tree = tree;
	}

	/**
	 * Returns the next visible item in row order, or <code>null</code> after the last one.
	 */
	TreeItem next () {
		while (depth >= 0) {
			TreeItem parent = parents [depth];
			int index = next [depth];
			if (index < childCount (parent)) {
				next [depth] = index + 1;
				TreeItem item = child (parent, index);
				enter (item);
				return item;
			}
			depth--;
		}
		return null;
	}

	/**
	 * Advances past <code>target</code> and returns the number of visible rows before it,
	 * or -1 when the target is not a visible row (the walk is then over).
	 */
	int indexOf (TreeItem target) {
		int row = 0;
		for (TreeItem item = next (); item != null; item = next ()) {
			if (item == target) return row;
			row++;
		}
		return -1;
	}

	/**
	 * Advances up to <code>rows</code> rows and returns how many rows were available.
	 */
	int advance (int rows) {
		int advanced = 0;
		while (advanced < rows && next () != null) advanced++;
		return advanced;
	}

	/* Pushes the children of an expanded item so that they come before the item's successors. */
	private void enter (TreeItem item) {
		if (!item.getExpanded () || item.getItemCount () == 0) return;
		if (depth + 1 == parents.length) grow ();
		depth++;
		parents [depth] = item;
		next [depth] = 0;
	}

	private void grow () {
		parents = Arrays.copyOf (parents, parents.length * 2);
		next = Arrays.copyOf (next, next.length * 2);
	}

	private int childCount (TreeItem parent) {
		return parent == null ? tree.getItemCount () : parent.getItemCount ();
	}

	private TreeItem child (TreeItem parent, int index) {
		return parent == null ? tree.getItem (index) : parent.getItem (index);
	}
}

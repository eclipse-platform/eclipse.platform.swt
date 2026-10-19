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
import java.util.function.*;

import org.eclipse.swt.*;

/**
 * Identity diff of <code>List</code>, <code>Table</code> and <code>Tree</code> rows (enhanced M3
 * API): moves the widget from one ordered set of row identities to another while leaving every
 * row that stays in place untouched.
 * <p>
 * The caller gives the identities of the current rows (<code>oldIds</code>, one per row, unique)
 * and of the wanted rows (<code>newIds</code>, unique). The rows kept in place are a longest
 * increasing subsequence of the old positions taken in the new order, so the number of removed
 * and inserted rows is minimal for a widget whose only edits are remove and insert. Kept rows
 * keep their item object, text, selection, checked state and (Tree) expansion; the other rows of
 * <code>oldIds</code> are removed in one call, and the rows of <code>newIds</code> that are not
 * kept are inserted in ascending position and filled by the caller. The edit runs as one
 * {@link M3BulkUpdates} update. The diff is O(n log n) over <code>long</code> and
 * <code>int</code> lanes; no item object is made for a kept row.
 * </p>
 * <p>
 * Each method answers <code>{kept, removed, inserted}</code>.
 * </p>
 *
 * @since 3.138
 */
public final class M3DiffApply {

	/** Index in the answer: rows left in place. */
	public static final int KEPT = 0;
	/** Index in the answer: rows removed (dropped or moved). */
	public static final int REMOVED = 1;
	/** Index in the answer: rows inserted (new or moved). */
	public static final int INSERTED = 2;

	private M3DiffApply () {
	}

	/**
	 * Moves the list from the rows <code>oldIds</code> to the rows <code>newIds</code>.
	 *
	 * @param list the list
	 * @param oldIds the identities of the current rows, one per row, unique
	 * @param newIds the identities of the wanted rows, unique
	 * @param text the text of an inserted row, by identity
	 * @return <code>{kept, removed, inserted}</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if an argument is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if <code>oldIds</code> does not have one identity per row, or an array repeats an identity</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the list has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the list</li>
	 * </ul>
	 */
	public static int [] apply (List list, long [] oldIds, long [] newIds, LongFunction<String> text) {
		if (list == null || text == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		list.checkWidget ();
		Plan plan = plan (oldIds, newIds, list.getItemCount ());
		M3BulkUpdates.run (list, () -> {
			if (plan.removed.length > 0) list.remove (plan.removed);
			for (int index : plan.inserted) list.add (text.apply (newIds [index]), index);
		});
		return plan.answer ();
	}

	/**
	 * Moves the table from the rows <code>oldIds</code> to the rows <code>newIds</code>. Each
	 * inserted row is a new <code>TableItem</code> handed to <code>fill</code> with its identity;
	 * a virtual table may pass <code>null</code> and fill the rows through <code>SWT.SetData</code>.
	 *
	 * @param table the table
	 * @param oldIds the identities of the current rows, one per row, unique
	 * @param newIds the identities of the wanted rows, unique
	 * @param fill fills an inserted row, or <code>null</code> for a virtual table
	 * @return <code>{kept, removed, inserted}</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if an argument is null (<code>fill</code> only for a table that is not virtual)</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if <code>oldIds</code> does not have one identity per row, or an array repeats an identity</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public static int [] apply (Table table, long [] oldIds, long [] newIds, ObjLongConsumer<TableItem> fill) {
		if (table == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		table.checkWidget ();
		if (fill == null && (table.getStyle () & SWT.VIRTUAL) == 0) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		Plan plan = plan (oldIds, newIds, table.getItemCount ());
		M3BulkUpdates.run (table, () -> {
			if (plan.removed.length > 0) table.remove (plan.removed);
			for (int index : plan.inserted) {
				TableItem item = new TableItem (table, SWT.NONE, index);
				if (fill != null) {
					fill.accept (item, newIds [index]);
				} else {
					table.clear (index);
				}
			}
		});
		return plan.answer ();
	}

	/**
	 * Moves the children of <code>parent</code> (the roots of the tree when <code>parent</code> is
	 * <code>null</code>) from <code>oldIds</code> to <code>newIds</code>. Each inserted row is a new
	 * <code>TreeItem</code> handed to <code>fill</code> with its identity; a moved row is inserted
	 * anew, so <code>fill</code> also rebuilds its children.
	 *
	 * @param tree the tree
	 * @param parent the parent of the rows, or <code>null</code> for the roots
	 * @param oldIds the identities of the current child rows, one per row, unique
	 * @param newIds the identities of the wanted child rows, unique
	 * @param fill fills an inserted row
	 * @return <code>{kept, removed, inserted}</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree, an id array or <code>fill</code> is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if <code>parent</code> is disposed or not an item of the tree, if <code>oldIds</code> does not have one identity per row, or an array repeats an identity</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static int [] applyChildren (Tree tree, TreeItem parent, long [] oldIds, long [] newIds, ObjLongConsumer<TreeItem> fill) {
		if (tree == null || fill == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		tree.checkWidget ();
		if (parent != null && (parent.isDisposed () || parent.getParent () != tree)) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		Plan plan = plan (oldIds, newIds, parent == null ? tree.getItemCount () : parent.getItemCount ());
		M3BulkUpdates.run (tree, () -> {
			for (int i = plan.removed.length - 1; i >= 0; i--) {
				(parent == null ? tree.getItem (plan.removed [i]) : parent.getItem (plan.removed [i])).dispose ();
			}
			for (int index : plan.inserted) {
				TreeItem item = parent == null ? new TreeItem (tree, SWT.NONE, index) : new TreeItem (parent, SWT.NONE, index);
				fill.accept (item, newIds [index]);
			}
		});
		return plan.answer ();
	}

	/* ------------------------------------------------------------------ plan */

	/* The edit: old positions to remove and new positions to insert, both ascending. */
	static final class Plan {
		final int kept;
		final int [] removed;
		final int [] inserted;

		Plan (int kept, int [] removed, int [] inserted) {
			this.kept = kept;
			this.removed = removed;
			this.inserted = inserted;
		}

		int [] answer () {
			return new int [] {kept, removed.length, inserted.length};
		}
	}

	/* The plan of a diff: rows kept are a longest increasing subsequence of the old positions in new order. */
	static Plan plan (long [] oldIds, long [] newIds, int rows) {
		if (oldIds == null || newIds == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (oldIds.length != rows) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		Positions old = new Positions (oldIds.length);
		for (int i = 0; i < oldIds.length; i++) {
			if (old.put (oldIds [i], i) >= 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		}
		Positions seen = new Positions (newIds.length);
		/* the old positions of the surviving rows, in new order (seqOld), and their new positions (seqNew) */
		int [] seqOld = new int [Math.min (oldIds.length, newIds.length)];
		int [] seqNew = new int [seqOld.length];
		int m = 0;
		for (int j = 0; j < newIds.length; j++) {
			if (seen.put (newIds [j], j) >= 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
			int position = old.get (newIds [j]);
			if (position >= 0) {
				seqOld [m] = position;
				seqNew [m++] = j;
			}
		}
		/* LIS by patience sorting: tails[k] is the sequence index ending the best run of length k + 1 */
		int [] tails = new int [Math.max (1, m)];
		int [] previous = new int [Math.max (1, m)];
		int length = 0;
		for (int s = 0; s < m; s++) {
			int value = seqOld [s], low = 0, high = length;
			while (low < high) {
				int middle = (low + high) >>> 1;
				if (seqOld [tails [middle]] < value) low = middle + 1; else high = middle;
			}
			previous [s] = low > 0 ? tails [low - 1] : -1;
			tails [low] = s;
			if (low == length) length++;
		}
		BitSet keptOld = new BitSet (oldIds.length), keptNew = new BitSet (newIds.length);
		for (int s = length > 0 ? tails [length - 1] : -1; s >= 0; s = previous [s]) {
			keptOld.set (seqOld [s]);
			keptNew.set (seqNew [s]);
		}
		return new Plan (length, complement (keptOld, oldIds.length), complement (keptNew, newIds.length));
	}

	/* ascending positions in [0, size) that are not set */
	private static int [] complement (BitSet set, int size) {
		int [] result = new int [size - set.cardinality ()];
		int k = 0;
		for (int i = set.nextClearBit (0); i < size; i = set.nextClearBit (i + 1)) result [k++] = i;
		return result;
	}

	/* Open addressing long id -> int position, linear probing, load at most 1/2. */
	static final class Positions {
		private final long [] keys;
		private final int [] values;
		private final int mask;

		Positions (int capacity) {
			int size = Integer.highestOneBit (Math.max (2, capacity) * 2 - 1) << 1;
			keys = new long [size];
			values = new int [size];
			mask = size - 1;
		}

		/* stores the position of a new key; answers the stored position when the key is already present */
		int put (long key, int position) {
			int slot = slot (key);
			while (values [slot] != 0) {
				if (keys [slot] == key) return values [slot] - 1;
				slot = (slot + 1) & mask;
			}
			keys [slot] = key;
			values [slot] = position + 1;
			return -1;
		}

		int get (long key) {
			int slot = slot (key);
			while (values [slot] != 0) {
				if (keys [slot] == key) return values [slot] - 1;
				slot = (slot + 1) & mask;
			}
			return -1;
		}

		private int slot (long key) {
			long h = key * 0x9E3779B97F4A7C15L;
			return (int) (h ^ (h >>> 32)) & mask;
		}
	}
}

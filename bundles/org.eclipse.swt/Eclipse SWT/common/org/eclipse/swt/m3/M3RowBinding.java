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
 *     Hitesh Soliwal (Synexia) - adapted from TV_5.0 treeviewer SortedElementProvider and
 *         FilteredTreeElementProvider (docs/m3/sort-filter/PROVENANCE.tsv)
 *******************************************************************************/
package org.eclipse.swt.m3;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Drives a virtual Table or Tree from an {@link M3RowView} (enhanced M3 API).
 * <p>
 * The widget's item count follows the view, and each <code>SWT.SetData</code> request fills the
 * item from the source row the view maps it to. When the view changes, only its damaged rows are
 * cleared, so the widget asks again only for the rows it shows (in viewport mode, the resident
 * window). A tree is driven by an {@link M3ColumnSort} in tree mode: the children of an item are
 * the sorted children of its source row.
 * </p>
 *
 * @since 3.138
 */
public final class M3RowBinding {

	/** Fills a widget item from a source row. */
	@FunctionalInterface
	public interface RowFill<T> {
		/**
		 * Fills the item.
		 *
		 * @param item the item to fill
		 * @param sourceRow the source row it shows
		 */
		void fill (T item, int sourceRow);
	}

	private static final String ROW = "org.eclipse.swt.m3.M3RowBinding.row"; //$NON-NLS-1$

	private final Composite control;
	private final M3RowView view;
	private final Listener setData;
	private final M3RowView.Listener changed;

	private M3RowBinding (Composite control, M3RowView view, Listener setData, M3RowView.Listener changed) {
		this.control = control;
		this.view = view;
		this.setData = setData;
		this.changed = changed;
		control.addListener (SWT.SetData, setData);
		view.addListener (changed);
		control.addListener (SWT.Dispose, event -> view.removeListener (changed));
	}

	/**
	 * Drives a virtual table from a view.
	 *
	 * @param table a table with <code>SWT.VIRTUAL</code>
	 * @param view the view
	 * @param fill fills an item from a source row
	 * @return the binding
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if an argument is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the table is not virtual</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public static M3RowBinding bind (Table table, M3RowView view, RowFill<TableItem> fill) {
		check (table, view, fill);
		M3RowBinding binding = new M3RowBinding (table, view, event -> {
			if (event.index < view.size ()) fill.fill ((TableItem) event.item, view.map (event.index));
		}, change -> refresh (table, change));
		table.setItemCount (view.size ());
		table.clearAll ();
		return binding;
	}

	/**
	 * Drives a virtual tree from a sort in tree mode.
	 *
	 * @param tree a tree with <code>SWT.VIRTUAL</code>
	 * @param sort the sort, with parents set
	 * @param fill fills an item from a source row
	 * @return the binding
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if an argument is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the tree is not virtual or the sort is not in tree mode</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static M3RowBinding bind (Tree tree, M3ColumnSort sort, RowFill<TreeItem> fill) {
		check (tree, sort, fill);
		int roots = sort.childCount (-1);
		M3RowBinding binding = new M3RowBinding (tree, sort, event -> {
			TreeItem item = (TreeItem) event.item, parentItem = item.getParentItem ();
			int parent = parentItem == null ? -1 : sourceRow (parentItem);
			if (event.index < sort.childCount (parent)) {
				int row = sort.child (parent, event.index);
				item.setData (ROW, row);
				fill.fill (item, row);
				item.setItemCount (sort.childCount (row));
			}
		}, change -> {
			tree.setItemCount (sort.childCount (-1));
			tree.clearAll (true);
		});
		tree.setItemCount (roots);
		tree.clearAll (true);
		return binding;
	}

	/**
	 * Returns the source row a tree item shows, or -1 when the binding has not filled it.
	 *
	 * @param item the item
	 * @return the source row, or -1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 * </ul>
	 */
	public static int sourceRow (TreeItem item) {
		Object row = M3Checks.nonNull (item).getData (ROW);
		return row instanceof Integer ? (Integer) row : -1;
	}

	private static void check (Composite control, M3RowView view, RowFill<?> fill) {
		M3Checks.nonNull (control);
		M3Checks.nonNull (view);
		M3Checks.nonNull (fill);
		M3Checks.argument ((control.getStyle () & SWT.VIRTUAL) != 0);
	}

	private static void refresh (Table table, M3RowChange change) {
		if (table.isDisposed ()) return;
		int size = change.view ().size ();
		if (table.getItemCount () != size) table.setItemCount (size);
		if (change.kind () == M3RowChange.RESET) {
			table.clearAll ();
		} else if (change.firstDamaged () >= 0 && change.firstDamaged () < size) {
			table.clear (change.firstDamaged (), Math.min (change.lastDamaged (), size - 1));
		}
	}

	/**
	 * Returns the view.
	 *
	 * @return the view
	 */
	public M3RowView getView () {
		return view;
	}

	/** Stops driving the widget; the widget keeps its items. */
	public void dispose () {
		view.removeListener (changed);
		if (!control.isDisposed ()) control.removeListener (SWT.SetData, setData);
	}
}

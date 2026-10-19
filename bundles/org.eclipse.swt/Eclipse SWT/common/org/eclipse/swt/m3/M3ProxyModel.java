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
package org.eclipse.swt.m3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * A sorted and filtered view of source rows, kept as int lanes (enhanced M3 API).
 * <p>
 * The model maps the rows of a view (what a widget shows) to the rows of the application's
 * source and back. Stages run in the order they were added: a filter keeps the rows its
 * predicate accepts, a sort orders the rows stably by a comparator of source rows (or by
 * {@link M3SortKeys}). Edits take effect at {@link #refresh()}. The mapping is one permutation
 * lane and its inverse; no row object is made.
 * </p>
 * <p>
 * {@link #bind(Table, RowFill)} drives a virtual table from the model: its item count is the view
 * size and each <code>SWT.SetData</code> request fills the item from the mapped source row.
 * {@link #bind(List, IntFunction)} sets the items of a list.
 * </p>
 *
 * @since 3.138
 */
public final class M3ProxyModel {

	/** Orders two source rows. */
	@FunctionalInterface
	public interface RowComparator {
		/**
		 * Compares two source rows.
		 *
		 * @param sourceA a source row
		 * @param sourceB another source row
		 * @return negative, zero or positive as <code>sourceA</code> sorts before, with or after <code>sourceB</code>
		 */
		int compare (int sourceA, int sourceB);
	}

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

	private static final String BINDING = "org.eclipse.swt.m3.M3ProxyModel"; //$NON-NLS-1$

	private int sourceSize;
	/* stages: IntPredicate (filter) or RowComparator (sort), in order */
	private final java.util.List<Object> stages = new ArrayList<> ();
	/* view row -> source row */
	private int [] rows = new int [0];
	private int size;
	/* source row -> view row, or -1 */
	private int [] inverse = new int [0];

	/**
	 * Constructs an identity model over <code>sourceSize</code> source rows.
	 *
	 * @param sourceSize the number of source rows, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the size is negative</li>
	 * </ul>
	 */
	public M3ProxyModel (int sourceSize) {
		setSourceSize (sourceSize);
		refresh ();
	}

	/**
	 * Sets the number of source rows; takes effect at {@link #refresh()}.
	 *
	 * @param sourceSize the number of source rows, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the size is negative</li>
	 * </ul>
	 */
	public void setSourceSize (int sourceSize) {
		if (sourceSize < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		this.sourceSize = sourceSize;
	}

	/**
	 * Returns the number of source rows.
	 *
	 * @return the source size
	 */
	public int sourceSize () {
		return sourceSize;
	}

	/**
	 * Appends a filter stage: the rows for which <code>keep</code> answers <code>false</code> leave
	 * the view.
	 *
	 * @param keep the predicate over source rows
	 * @return this model
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the predicate is null</li>
	 * </ul>
	 */
	public M3ProxyModel addFilter (IntPredicate keep) {
		if (keep == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		stages.add (keep);
		return this;
	}

	/**
	 * Appends a stable sort stage over source rows.
	 *
	 * @param comparator the order of source rows
	 * @return this model
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the comparator is null</li>
	 * </ul>
	 */
	public M3ProxyModel addSort (RowComparator comparator) {
		if (comparator == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		stages.add (comparator);
		return this;
	}

	/**
	 * Appends a stable sort stage by collation keys: source row <code>s</code> sorts by key
	 * <code>s</code>.
	 *
	 * @param keys the keys, one per source row
	 * @return this model
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the keys are null</li>
	 * </ul>
	 */
	public M3ProxyModel addSort (M3SortKeys keys) {
		if (keys == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		return addSort (keys::compare);
	}

	/**
	 * Removes every stage; takes effect at {@link #refresh()}.
	 *
	 * @return this model
	 */
	public M3ProxyModel clearStages () {
		stages.clear ();
		return this;
	}

	/**
	 * Recomputes the view from the source size and the stages.
	 *
	 * @return this model
	 */
	public M3ProxyModel refresh () {
		int [] lane = new int [sourceSize], scratch = new int [sourceSize];
		for (int s = 0; s < sourceSize; s++) lane [s] = s;
		int n = sourceSize;
		for (Object stage : stages) {
			if (stage instanceof IntPredicate keep) {
				int kept = 0;
				for (int i = 0; i < n; i++) {
					if (keep.test (lane [i])) lane [kept++] = lane [i];
				}
				n = kept;
			} else {
				sort (lane, scratch, n, (RowComparator) stage);
			}
		}
		rows = lane;
		size = n;
		if (inverse.length != sourceSize) inverse = new int [sourceSize];
		Arrays.fill (inverse, -1);
		for (int r = 0; r < n; r++) inverse [rows [r]] = r;
		return this;
	}

	/**
	 * Returns the number of view rows.
	 *
	 * @return the view size
	 */
	public int size () {
		return size;
	}

	/**
	 * Returns the source row of a view row.
	 *
	 * @param row the view row
	 * @return the source row
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a view row</li>
	 * </ul>
	 */
	public int map (int row) {
		if (row < 0 || row >= size) SWT.error (SWT.ERROR_INVALID_RANGE);
		return rows [row];
	}

	/**
	 * Returns the view row of a source row, or -1 when a filter removed it.
	 *
	 * @param sourceRow the source row
	 * @return the view row, or -1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a source row of the last refresh</li>
	 * </ul>
	 */
	public int unmap (int sourceRow) {
		if (sourceRow < 0 || sourceRow >= inverse.length) SWT.error (SWT.ERROR_INVALID_RANGE);
		return inverse [sourceRow];
	}

	/**
	 * Drives a virtual table from the model: the item count becomes the view size, the items are
	 * cleared, and each <code>SWT.SetData</code> request fills the item from its source row. Call
	 * again after {@link #refresh()}; a later binding replaces an earlier one.
	 *
	 * @param table a table with <code>SWT.VIRTUAL</code>
	 * @param fill fills an item from a source row
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table or the fill is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the table is not virtual</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public void bind (Table table, RowFill<TableItem> fill) {
		if (table == null || fill == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if ((table.getStyle () & SWT.VIRTUAL) == 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		if (table.getData (BINDING) instanceof Listener old) table.removeListener (SWT.SetData, old);
		Listener listener = event -> {
			if (event.index < size) fill.fill ((TableItem) event.item, rows [event.index]);
		};
		table.setData (BINDING, listener);
		table.addListener (SWT.SetData, listener);
		table.setItemCount (size);
		table.clearAll ();
	}

	/**
	 * Sets the items of the list to the view rows, by source row.
	 *
	 * @param list the list
	 * @param text the text of a source row
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list, the text function or a text is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the list has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the list</li>
	 * </ul>
	 */
	public void bind (List list, IntFunction<String> text) {
		if (list == null || text == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		String [] items = new String [size];
		for (int r = 0; r < size; r++) items [r] = text.apply (rows [r]);
		list.setItems (items);
	}

	/* stable bottom-up merge sort of lane [0, n) by the comparator of source rows */
	private static void sort (int [] lane, int [] scratch, int n, RowComparator comparator) {
		int [] from = lane, to = scratch;
		for (int width = 1; width < n; width *= 2) {
			for (int low = 0; low < n; low += 2 * width) {
				int middle = Math.min (low + width, n), high = Math.min (low + 2 * width, n);
				int left = low, right = middle, out = low;
				while (left < middle && right < high) {
					to [out++] = comparator.compare (from [right], from [left]) < 0 ? from [right++] : from [left++];
				}
				while (left < middle) to [out++] = from [left++];
				while (right < high) to [out++] = from [right++];
			}
			int [] swap = from;
			from = to;
			to = swap;
		}
		if (from != lane) System.arraycopy (from, 0, lane, 0, n);
	}

	@Override
	public String toString () {
		return "M3ProxyModel [" + size + " of " + sourceSize + " rows, " + stages.size () + " stages]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}

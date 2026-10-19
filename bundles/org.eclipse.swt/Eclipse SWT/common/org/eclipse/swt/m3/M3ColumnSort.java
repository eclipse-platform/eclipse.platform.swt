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

import java.util.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * A multi-column sort of the rows of an input view (enhanced M3 API).
 * <p>
 * The sort keys are an ordered list of (column, direction) pairs: rows are ordered by the first
 * key, rows equal in it by the second key, and so on; rows equal in every key keep ascending
 * source order, in both directions (the sort is stable). Each column reads its values through an
 * {@link M3ColumnKey} or, by default, through the cell text. The result is a permutation lane
 * over the source rows, kept up to date incrementally as rows change, enter or leave the input.
 * </p>
 * <p>
 * The sort is a {@link M3RowView}: bind it to a virtual widget with {@link M3RowBinding}, so that
 * only the rows the widget materializes are read. {@link #install(Table)} and
 * {@link #install(Tree)} connect the column headers: a click sorts by the column (ascending,
 * descending, unsorted), a Shift+click adds the column as the next key, and every sorted column
 * shows its direction and, with several keys, its priority number.
 * </p>
 * <p>
 * In tree mode ({@link #setParents(IntUnaryOperator)}) siblings are sorted inside their parent:
 * {@link #childCount(int)} and {@link #child(int, int)} answer the sorted children of a row.
 * </p>
 *
 * @see M3ColumnFilter
 * @since 3.138
 */
public final class M3ColumnSort extends M3RowViewBase {

	private static final int [] NONE = {};

	private final M3ColumnKey [] keys, textKeys;
	private M3CellText text;
	private int [] sortColumns = NONE, sortDirections = NONE;
	private M3ColumnKey [] chain = {};
	private boolean [] descending = {};
	private IntUnaryOperator parentOf;
	private final IntBinaryOperator compareRows = this::compare;
	final M3SortOrder order = new M3SortOrder (this);

	/**
	 * Constructs a sort of all rows of a source, in source order until sort keys are set.
	 *
	 * @param columnCount the number of columns, not negative
	 * @param sourceSize the number of source rows, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if a count is negative</li>
	 * </ul>
	 */
	public M3ColumnSort (int columnCount, int sourceSize) {
		this (columnCount, null, sourceSize);
	}

	/**
	 * Constructs a sort of the rows of an input view, for example an {@link M3ColumnFilter}.
	 *
	 * @param columnCount the number of columns, not negative
	 * @param input the input view
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the count is negative</li>
	 *    <li>ERROR_NULL_ARGUMENT - if the input is null</li>
	 * </ul>
	 */
	public M3ColumnSort (int columnCount, M3RowView input) {
		this (columnCount, M3Checks.nonNull (input), 0);
	}

	private M3ColumnSort (int columnCount, M3RowView input, int sourceSize) {
		super (input, sourceSize);
		M3Checks.argument (columnCount >= 0);
		keys = new M3ColumnKey [columnCount];
		textKeys = new M3ColumnKey [columnCount];
		rebuild ();
	}

	/**
	 * Returns the number of columns.
	 *
	 * @return the column count
	 */
	public int getColumnCount () {
		return keys.length;
	}

	/**
	 * Sets how a column reads its sort key; <code>null</code> reads the cell text (see
	 * {@link #setCellText(M3CellText)}). Takes effect at the next change of the sort keys.
	 *
	 * @param column the column
	 * @param key the key, or <code>null</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is not a column</li>
	 * </ul>
	 */
	public void setKey (int column, M3ColumnKey key) {
		keys [M3Checks.range (column, keys.length)] = key;
	}

	/**
	 * Returns the key set for a column, or <code>null</code> when it reads the cell text.
	 *
	 * @param column the column
	 * @return the key, or <code>null</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is not a column</li>
	 * </ul>
	 */
	public M3ColumnKey getKey (int column) {
		return keys [M3Checks.range (column, keys.length)];
	}

	/**
	 * Sets the cell text read by the columns that have no key of their own (ordered by
	 * <code>String.compareTo</code>, empty cells first). Takes effect at the next change of the
	 * sort keys.
	 *
	 * @param text the cell text, or <code>null</code>
	 */
	public void setCellText (M3CellText text) {
		this.text = text;
		Arrays.fill (textKeys, null);
	}

	/**
	 * Replaces the sort keys and sorts. Each direction is <code>SWT.UP</code> (ascending) or
	 * <code>SWT.DOWN</code> (descending); empty arrays restore source order.
	 *
	 * @param columns the columns, most significant first, each at most once
	 * @param directions the direction of each column
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if an array is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the lengths differ, a column repeats, a direction is not UP or DOWN, or a column has neither a key nor cell text</li>
	 *    <li>ERROR_INVALID_RANGE - if a column is not a column</li>
	 * </ul>
	 */
	public void setSortKeys (int [] columns, int [] directions) {
		M3Checks.nonNull (columns);
		M3Checks.nonNull (directions);
		M3Checks.argument (columns.length == directions.length);
		M3ColumnKey [] newChain = new M3ColumnKey [columns.length];
		boolean [] newDescending = new boolean [columns.length];
		for (int i = 0; i < columns.length; i++) {
			M3Checks.range (columns [i], keys.length);
			M3Checks.argument (directions [i] == SWT.UP || directions [i] == SWT.DOWN);
			for (int j = 0; j < i; j++) M3Checks.argument (columns [j] != columns [i]);
			newChain [i] = keyOf (columns [i]);
			newDescending [i] = directions [i] == SWT.DOWN;
		}
		sortColumns = columns.clone ();
		sortDirections = directions.clone ();
		chain = newChain;
		descending = newDescending;
		order.resort ();
		change.set (M3RowChange.ORDER, 0, 0);
		change.damage (0, order.size - 1);
		fire ();
	}

	private M3ColumnKey keyOf (int column) {
		if (keys [column] != null) return keys [column];
		M3Checks.argument (text != null);
		if (textKeys [column] == null) {
			M3CellText cells = text;
			textKeys [column] = M3ColumnKey.ofText (row -> cells.text (row, column), null);
		}
		return textKeys [column];
	}

	/**
	 * Clicks a column header: without <code>accumulate</code> the column becomes the only key and
	 * its direction cycles ascending, descending, unsorted; with <code>accumulate</code> (Shift+click)
	 * a column that is not a key is appended ascending, and a key column cycles its own direction
	 * in place, leaving the keys after descending.
	 *
	 * @param column the column
	 * @param accumulate whether to keep the other keys
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is not a column</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the column has neither a key nor cell text</li>
	 * </ul>
	 */
	public void toggle (int column, boolean accumulate) {
		M3Checks.range (column, keys.length);
		int priority = getPriority (column);
		int next = priority < 0 ? SWT.UP : sortDirections [priority] == SWT.UP ? SWT.DOWN : SWT.NONE;
		if (!accumulate) {
			boolean off = next == SWT.NONE;
			setSortKeys (off ? NONE : new int [] {column}, off ? NONE : new int [] {next});
			return;
		}
		int [] columns = sortColumns, directions = sortDirections.clone ();
		if (priority < 0) {
			columns = append (columns, column);
			directions = append (directions, SWT.UP);
		} else if (next == SWT.NONE) {
			columns = without (columns, priority);
			directions = without (directions, priority);
		} else {
			directions [priority] = next;
		}
		setSortKeys (columns, directions);
	}

	/** Removes every sort key: the rows return to source order. */
	public void clear () {
		setSortKeys (NONE, NONE);
	}

	/**
	 * Returns the number of sort keys.
	 *
	 * @return the key count
	 */
	public int getSortKeyCount () {
		return sortColumns.length;
	}

	/**
	 * Returns the column of a sort key.
	 *
	 * @param priority the index of the key, 0 for the most significant
	 * @return the column
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if there is no such key</li>
	 * </ul>
	 */
	public int getSortColumn (int priority) {
		return sortColumns [M3Checks.range (priority, sortColumns.length)];
	}

	/**
	 * Returns the direction of a sort key, <code>SWT.UP</code> or <code>SWT.DOWN</code>.
	 *
	 * @param priority the index of the key, 0 for the most significant
	 * @return the direction
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if there is no such key</li>
	 * </ul>
	 */
	public int getSortDirection (int priority) {
		return sortDirections [M3Checks.range (priority, sortDirections.length)];
	}

	/**
	 * Returns the priority of a column among the sort keys (0 for the most significant), or -1.
	 *
	 * @param column the column
	 * @return the priority, or -1 when the column is not a sort key
	 */
	public int getPriority (int column) {
		for (int i = 0; i < sortColumns.length; i++) {
			if (sortColumns [i] == column) return i;
		}
		return -1;
	}

	/**
	 * Returns the direction of a column: <code>SWT.UP</code>, <code>SWT.DOWN</code>, or
	 * <code>SWT.NONE</code> when the column is not a sort key.
	 *
	 * @param column the column
	 * @return the direction
	 */
	public int getDirection (int column) {
		int priority = getPriority (column);
		return priority < 0 ? SWT.NONE : sortDirections [priority];
	}

	/**
	 * Compares two source rows by the sort keys; rows equal in every key compare by source row.
	 * This is the order of the view; it has the shape of <code>M3ProxyModel.RowComparator</code>.
	 *
	 * @param sourceA a source row
	 * @param sourceB another source row
	 * @return negative, zero or positive as <code>sourceA</code> sorts before, with or after <code>sourceB</code>
	 */
	public int compare (int sourceA, int sourceB) {
		for (int i = 0; i < chain.length; i++) {
			int c = descending [i] ? chain [i].compare (sourceB, sourceA) : chain [i].compare (sourceA, sourceB);
			if (c != 0) return c;
		}
		return Integer.compare (sourceA, sourceB);
	}

	void sortRows (int [] rows, int n) {
		order.lanes.sort (rows, n, chain, descending, chain.length, compareRows);
	}

	@Override
	public int size () {
		return order.size;
	}

	@Override
	public int map (int row) {
		return order.order [M3Checks.range (row, order.size)];
	}

	@Override
	public int unmap (int sourceRow) {
		return order.inverse [M3Checks.range (sourceRow, order.sourceSize)];
	}

	/**
	 * Turns tree mode on: siblings (rows with the same parent) are sorted inside their parent.
	 * The parent of a root is -1. <code>null</code> turns tree mode off.
	 *
	 * @param parentOf the parent source row of a source row, or -1
	 */
	public void setParents (IntUnaryOperator parentOf) {
		this.parentOf = parentOf;
		order.invalidateTree ();
	}

	/**
	 * Returns the number of sorted children of a row in tree mode.
	 *
	 * @param parentRow the parent source row, or -1 for the roots
	 * @return the child count
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is neither -1 nor a source row</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if tree mode is off, or a parent is neither -1 nor a source row</li>
	 * </ul>
	 */
	public int childCount (int parentRow) {
		tree (parentRow);
		return order.childCount (parentRow);
	}

	/**
	 * Returns a sorted child of a row in tree mode.
	 *
	 * @param parentRow the parent source row, or -1 for the roots
	 * @param index the index among the children shown by the view
	 * @return the source row of the child
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is neither -1 nor a source row, or the index is not a child index</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if tree mode is off, or a parent is neither -1 nor a source row</li>
	 * </ul>
	 */
	public int child (int parentRow, int index) {
		tree (parentRow);
		return order.child (parentRow, index);
	}

	/**
	 * Returns the index of a row among the sorted children of its parent, or -1 when the view
	 * does not show it.
	 *
	 * @param sourceRow the source row
	 * @return the index among its siblings, or -1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a source row</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if tree mode is off, or a parent is neither -1 nor a source row</li>
	 * </ul>
	 */
	public int indexOfChild (int sourceRow) {
		tree (M3Checks.range (sourceRow, order.sourceSize));
		return order.indexOfChild (sourceRow);
	}

	private void tree (int row) {
		M3Checks.range (row + 1, order.sourceSize + 1);
		M3Checks.argument (parentOf != null);
		order.buildTree (parentOf);
	}

	/**
	 * Connects the column headers of a table to this sort (see the class comment). The columns
	 * of the table are the columns of the sort, in creation order.
	 *
	 * @param table the table
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public void install (Table table) {
		M3SortHeader.install (this, M3Checks.nonNull (table));
	}

	/**
	 * Connects the column headers of a tree to this sort (see the class comment). The columns of
	 * the tree are the columns of the sort, in creation order.
	 *
	 * @param tree the tree
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public void install (Tree tree) {
		M3SortHeader.install (this, M3Checks.nonNull (tree));
	}

	@Override
	void inputChanged (M3RowChange in) {
		int kind = in.kind;
		if (kind == M3RowChange.ORDER) return;
		change.set (kind, in.start, in.count);
		for (int i = 0; i < in.rowCount; i++) change.add (in.rows [i]);
		int first = -1;
		switch (kind) {
			case M3RowChange.RESET: rebuild (); first = 0; break;
			case M3RowChange.INSERTED: order.insertSourceRange (in.start, in.count); first = order.insert (in.rows, in.rowCount); break;
			case M3RowChange.REMOVED: first = order.removeSourceRange (in.start, in.count); break;
			case M3RowChange.ENTERED: first = order.insert (in.rows, in.rowCount); break;
			case M3RowChange.LEFT: first = order.remove (in.rows, in.rowCount); break;
			default: order.update (in.rows, in.rowCount, change); break;
		}
		if (first >= 0) change.damage (first, order.size - 1);
		fire ();
	}

	private void rebuild () {
		int n = input == null ? sourceSize () : input.size ();
		int [] rows = new int [n];
		for (int i = 0; i < n; i++) rows [i] = input == null ? i : input.map (i);
		order.reset (rows, n, sourceSize ());
	}

	private static int [] append (int [] lane, int value) {
		int [] grown = Arrays.copyOf (lane, lane.length + 1);
		grown [lane.length] = value;
		return grown;
	}

	private static int [] without (int [] lane, int index) {
		int [] shrunk = new int [lane.length - 1];
		System.arraycopy (lane, 0, shrunk, 0, index);
		System.arraycopy (lane, index + 1, shrunk, index, shrunk.length - index);
		return shrunk;
	}

	@Override
	public String toString () {
		return "M3ColumnSort [" + order.size + " of " + order.sourceSize + " rows, keys " + Arrays.toString (sortColumns) + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}

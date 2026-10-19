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
 *     Hitesh Soliwal (Synexia) - adapted from TV_5.0 treeviewer IMultiPropertyTextFilter,
 *         MultiPropertyTextFilter, ElementFilter, FilteredTreeElementProvider and
 *         TreeFilterStateProvider (docs/m3/sort-filter/PROVENANCE.tsv)
 *******************************************************************************/
package org.eclipse.swt.m3;

import java.util.*;
import java.util.function.*;

/**
 * Per-column filters over the rows of a source (enhanced M3 API).
 * <p>
 * Each column may have one criterion: a text match ({@link M3TextMatch}: contains, starts with,
 * equals, wildcard or regular expression, optionally negated or without case) or a value list
 * (texts, or value ids). One more criterion, addressed as {@link #ALL_COLUMNS}, matches one text
 * against several columns (a row matches when any of them matches). Criteria combine from left
 * to right, the multi-column criterion first and then the columns in order, each with
 * {@link #AND} (the default) or {@link #OR}. The view shows the source rows that pass, in source
 * order.
 * </p>
 * <p>
 * Every criterion keeps a bit mask over the source rows; combining is a word-wide fold, so
 * changing one criterion re-tests one column. Cell texts come from an {@link M3CellText}; widget
 * items are never read. In tree mode ({@link #setParents(IntUnaryOperator)}) every ancestor of a
 * passing row is shown too, so that the row stays reachable.
 * </p>
 * <p>
 * Criterion changes report the rows that left and entered the view to the listeners, so an
 * {@link M3ColumnSort} over this filter updates incrementally.
 * </p>
 *
 * @see M3ColumnSort
 * @since 3.138
 */
public final class M3ColumnFilter extends M3RowViewBase {

	/** Logic: a row passes when it passes the criteria before and this one. */
	public static final int AND = 0;

	/** Logic: a row passes when it passes the criteria before or this one. */
	public static final int OR = 1;

	/** The index of the multi-column criterion. */
	public static final int ALL_COLUMNS = -1;

	private final M3CellText text;
	private final M3FilterCriterion [] criteria;
	private IntUnaryOperator parentOf;
	private long [] matched = {}, kept = {}, previous = {};
	private int [] rows = {}, rank = {};
	private int size;
	private final M3FilterDelta delta = new M3FilterDelta (this);

	/**
	 * Constructs a filter without criteria (every row passes).
	 *
	 * @param columnCount the number of columns, not negative
	 * @param sourceSize the number of source rows, not negative
	 * @param text the cell text read by text criteria, or <code>null</code> when only value-id lists are used
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if a count is negative</li>
	 * </ul>
	 */
	public M3ColumnFilter (int columnCount, int sourceSize, M3CellText text) {
		super (null, sourceSize);
		M3Checks.argument (columnCount >= 0);
		this.text = text;
		criteria = new M3FilterCriterion [columnCount + 1];
		for (int i = 0; i < criteria.length; i++) criteria [i] = new M3FilterCriterion ();
		recompute (null);
	}

	/**
	 * Returns the number of columns.
	 *
	 * @return the column count
	 */
	public int getColumnCount () {
		return criteria.length - 1;
	}

	/**
	 * Sets the text criterion of a column; a <code>null</code> pattern, or one without tokens,
	 * removes the criterion.
	 *
	 * @param column the column
	 * @param pattern the pattern text (see {@link M3TextMatch}), or <code>null</code>
	 * @param mode the mode and flags of {@link M3TextMatch}
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is not a column</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the mode or a regular expression is not valid, or the filter has no cell text</li>
	 * </ul>
	 */
	public void setText (int column, String pattern, int mode) {
		M3Checks.range (column, criteria.length - 1);
		setText (column, new int [] {column}, pattern, mode);
	}

	/**
	 * Sets the multi-column criterion: a row matches when the pattern matches any of the columns
	 * (with {@link M3TextMatch#NEGATE}: none of them). A <code>null</code> pattern, or one without
	 * tokens, removes the criterion.
	 *
	 * @param columns the columns
	 * @param pattern the pattern text, or <code>null</code>
	 * @param mode the mode and flags of {@link M3TextMatch}
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the columns are null</li>
	 *    <li>ERROR_INVALID_RANGE - if a column is not a column</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the mode or a regular expression is not valid, or the filter has no cell text</li>
	 * </ul>
	 */
	public void setText (int [] columns, String pattern, int mode) {
		M3Checks.nonNull (columns);
		for (int column : columns) M3Checks.range (column, criteria.length - 1);
		setText (ALL_COLUMNS, columns.clone (), pattern, mode);
	}

	private void setText (int index, int [] columns, String pattern, int mode) {
		M3TextMatch match = pattern == null ? null : M3TextMatch.compile (pattern, mode);
		M3Checks.argument (text != null || match == null);
		M3FilterCriterion criterion = criterion (index);
		criterion.clear ();
		if (match != null && match.isActive ()) {
			criterion.match = match;
			criterion.columns = columns;
		}
		changed (criterion);
	}

	/**
	 * Sets a value list on a column: a row passes when its cell text (an empty cell is the empty
	 * text) is one of the values. <code>null</code> removes the criterion.
	 *
	 * @param column the column
	 * @param values the values, or <code>null</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is not a column</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the filter has no cell text</li>
	 * </ul>
	 */
	public void setValues (int column, String... values) {
		M3FilterCriterion criterion = criterion (M3Checks.range (column, criteria.length - 1));
		M3Checks.argument (text != null || values == null);
		criterion.clear ();
		if (values != null) {
			criterion.values = new HashSet<> (Arrays.asList (values));
			criterion.columns = new int [] {column};
		}
		changed (criterion);
	}

	/**
	 * Sets a value list of ids on a column: a row passes when the id of its value is one of the
	 * allowed ids (ids are not negative; a negative id never passes).
	 *
	 * @param column the column
	 * @param valueId the value id of a source row
	 * @param allowedIds the allowed ids
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is not a column</li>
	 *    <li>ERROR_NULL_ARGUMENT - if the function or the ids are null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if an id is negative</li>
	 * </ul>
	 */
	public void setValues (int column, IntUnaryOperator valueId, int... allowedIds) {
		M3FilterCriterion criterion = criterion (M3Checks.range (column, criteria.length - 1));
		M3Checks.nonNull (valueId);
		M3Checks.nonNull (allowedIds);
		long [] allowed = {};
		for (int id : allowedIds) {
			M3Checks.argument (id >= 0);
			allowed = M3RowMask.ensure (allowed, id + 1);
			M3RowMask.set (allowed, id, true);
		}
		criterion.clear ();
		criterion.valueId = valueId;
		criterion.allowed = allowed;
		criterion.columns = new int [] {column};
		changed (criterion);
	}

	/**
	 * Removes the criterion of a column, or of {@link #ALL_COLUMNS}.
	 *
	 * @param column the column, or {@link #ALL_COLUMNS}
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is neither a column nor ALL_COLUMNS</li>
	 * </ul>
	 */
	public void clear (int column) {
		M3FilterCriterion criterion = criterion (column);
		criterion.clear ();
		changed (criterion);
	}

	/** Removes every criterion: every row passes. */
	public void clearAll () {
		for (M3FilterCriterion criterion : criteria) criterion.clear ();
		recompute (null);
	}

	/**
	 * Sets how the criterion of a column combines with the criteria before it.
	 *
	 * @param column the column, or {@link #ALL_COLUMNS}
	 * @param logic {@link #AND} or {@link #OR}
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is neither a column nor ALL_COLUMNS</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the logic is neither AND nor OR</li>
	 * </ul>
	 */
	public void setLogic (int column, int logic) {
		M3FilterCriterion criterion = criterion (column);
		M3Checks.argument (logic == AND || logic == OR);
		criterion.logic = logic;
		recompute (null);
	}

	/**
	 * Returns how the criterion of a column combines with the criteria before it.
	 *
	 * @param column the column, or {@link #ALL_COLUMNS}
	 * @return {@link #AND} or {@link #OR}
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is neither a column nor ALL_COLUMNS</li>
	 * </ul>
	 */
	public int getLogic (int column) {
		return criterion (column).logic;
	}

	/**
	 * Returns whether a column, or {@link #ALL_COLUMNS}, has a criterion.
	 *
	 * @param column the column, or {@link #ALL_COLUMNS}
	 * @return whether it has a criterion
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is neither a column nor ALL_COLUMNS</li>
	 * </ul>
	 */
	public boolean isActive (int column) {
		return criterion (column).isActive ();
	}

	/**
	 * Returns the text criterion of a column, or <code>null</code> when it has none.
	 *
	 * @param column the column, or {@link #ALL_COLUMNS}
	 * @return the match, or <code>null</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is neither a column nor ALL_COLUMNS</li>
	 * </ul>
	 */
	public M3TextMatch getMatch (int column) {
		return criterion (column).match;
	}

	/**
	 * Returns the columns of the multi-column criterion (empty when it is not set).
	 *
	 * @return a copy of the columns
	 */
	public int [] getColumns () {
		return criteria [0].columns.clone ();
	}

	/**
	 * Returns whether a source row passes the criteria (before the tree ancestor rule).
	 *
	 * @param sourceRow the source row
	 * @return whether it passes
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the row is not a source row</li>
	 * </ul>
	 */
	public boolean matches (int sourceRow) {
		return M3RowMask.get (matched, M3Checks.range (sourceRow, sourceSize ()));
	}

	/**
	 * Turns tree mode on: every ancestor of a passing row is shown. The parent of a root is -1.
	 * <code>null</code> turns tree mode off.
	 *
	 * @param parentOf the parent source row of a source row, or -1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if a parent is neither -1 nor a source row</li>
	 * </ul>
	 */
	public void setParents (IntUnaryOperator parentOf) {
		this.parentOf = parentOf;
		recompute (null);
	}

	@Override
	public int size () {
		return size;
	}

	@Override
	public int map (int row) {
		return rows [M3Checks.range (row, size)];
	}

	@Override
	public int unmap (int sourceRow) {
		return M3RowMask.rank (kept, rank, M3Checks.range (sourceRow, sourceSize ()));
	}

	private M3FilterCriterion criterion (int column) {
		return criteria [M3Checks.range (column + 1, criteria.length)];
	}

	private void changed (M3FilterCriterion criterion) {
		int n = sourceSize ();
		criterion.mask = M3RowMask.ensure (criterion.mask, n);
		if (criterion.isActive ()) criterion.evaluate (0, n, text);
		recompute (null);
	}

	@Override
	void inputChanged (M3RowChange in) {
		int n = sourceSize ();
		switch (in.kind) {
			case M3RowChange.INSERTED: shift (in.start, in.count, true); evaluate (in.start, in.start + in.count); break;
			case M3RowChange.REMOVED: shift (in.start, in.count, false); break;
			case M3RowChange.UPDATED: for (int i = 0; i < in.rowCount; i++) evaluate (in.rows [i], in.rows [i] + 1); break;
			default: for (M3FilterCriterion criterion : criteria) criterion.mask = new long [M3RowMask.words (n)]; evaluate (0, n); break;
		}
		recompute (in);
	}

	private void evaluate (int start, int end) {
		for (M3FilterCriterion criterion : criteria) {
			if (criterion.isActive ()) criterion.evaluate (start, end, text);
		}
	}

	private void shift (int start, int count, boolean insert) {
		int n = sourceSize (), old = insert ? n - count : n + count;
		for (M3FilterCriterion criterion : criteria) criterion.mask = shift (criterion.mask, old, n, start, count, insert);
		previous = shift (previous, old, n, start, count, insert);
	}

	private static long [] shift (long [] mask, int old, int n, int start, int count, boolean insert) {
		mask = M3RowMask.ensure (mask, Math.max (old, n));
		if (insert) M3RowMask.insert (mask, old, start, count); else M3RowMask.remove (mask, old, start, count);
		return mask;
	}

	/* folds the criteria, applies the ancestor rule, compacts the view, and reports the change */
	private void recompute (M3RowChange in) {
		int n = sourceSize (), words = M3RowMask.words (n);
		fold (n, words);
		kept = M3RowMask.ensure (kept, n);
		System.arraycopy (matched, 0, kept, 0, words);
		if (parentOf != null) ancestors (n, words);
		rows = rows.length >= n ? rows : new int [n];
		rank = rank.length >= words ? rank : new int [words];
		size = M3RowMask.compact (kept, words, rows, rank);
		previous = M3RowMask.ensure (previous, n);
		delta.report (in, previous, kept, n);
		System.arraycopy (kept, 0, previous, 0, words);
	}

	private void fold (int n, int words) {
		matched = M3RowMask.ensure (matched, n);
		boolean first = true;
		for (M3FilterCriterion criterion : criteria) {
			if (!criterion.isActive ()) continue;
			long [] mask = criterion.mask;
			for (int w = 0; w < words; w++) {
				matched [w] = first ? mask [w] : criterion.logic == AND ? matched [w] & mask [w] : matched [w] | mask [w];
			}
			first = false;
		}
		if (first) M3RowMask.fill (matched, n);
		else if ((n & 63) != 0) matched [words - 1] &= (1L << n) - 1;
	}

	/* keeps every ancestor of a matching row: walk up until an ancestor is already kept (each row once) */
	private void ancestors (int n, int words) {
		for (int w = 0; w < words; w++) {
			for (long x = matched [w]; x != 0; x &= x - 1) {
				int parent = parentOf.applyAsInt (w << 6 | Long.numberOfTrailingZeros (x));
				while (parent != -1) {
					M3Checks.argument (parent >= 0 && parent < n);
					if (M3RowMask.get (kept, parent)) break;
					M3RowMask.set (kept, parent, true);
					parent = parentOf.applyAsInt (parent);
				}
			}
		}
	}

	/* the number of rows of the view before a source row */
	int rankBefore (int sourceRow) {
		if (sourceRow >= sourceSize ()) return size;
		int w = sourceRow >>> 6;
		return rank [w] + Long.bitCount (kept [w] & (1L << sourceRow) - 1);
	}

	@Override
	public String toString () {
		return "M3ColumnFilter [" + size + " of " + sourceSize () + " rows]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}

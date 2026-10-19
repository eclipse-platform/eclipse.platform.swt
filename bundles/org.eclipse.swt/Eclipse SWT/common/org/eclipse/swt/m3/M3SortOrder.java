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

/*
 * The lanes of M3ColumnSort: the order lane (view row -> source row), its inverse (source row ->
 * view row or -1), and the sibling index of tree mode (CSR: child start per parent, children in
 * sorted order, sibling position per source row). Changes are applied incrementally: a moved row
 * is found by binary search and shifted with one arraycopy; an entering batch is sorted and
 * merged from the back; leaving rows are compacted in one pass. Each operation returns the
 * first view row whose content changed.
 */
final class M3SortOrder {

	private static final int REMOVED = -2;

	final M3SortLanes lanes = new M3SortLanes ();
	private final M3ColumnSort sort;
	int [] order = new int [0];
	int size;
	int [] inverse = new int [0];
	int sourceSize;
	private int [] batch = new int [0];
	private int [] childStart = new int [0], children = new int [0], siblingIndex = new int [0], cursor = new int [0];
	private boolean treeValid;

	M3SortOrder (M3ColumnSort sort) {
		this.sort = sort;
	}

	/* replaces the rows by rows [0, n) of a source of sourceSize rows and sorts them */
	void reset (int [] rows, int n, int sourceSize) {
		this.sourceSize = sourceSize;
		order = ensure (order, n, 0);
		System.arraycopy (rows, 0, order, 0, n);
		size = n;
		resort ();
	}

	/* sorts the current rows again (the sort keys changed) */
	void resort () {
		sort.sortRows (order, size);
		if (inverse.length < sourceSize) inverse = new int [sourceSize];
		Arrays.fill (inverse, 0, sourceSize, -1);
		reindex (0);
	}

	/* source rows from start move up by count; the new rows are not in the view yet */
	void insertSourceRange (int start, int count) {
		inverse = ensure (inverse, sourceSize + count, sourceSize);
		System.arraycopy (inverse, start, inverse, start + count, sourceSize - start);
		Arrays.fill (inverse, start, start + count, -1);
		sourceSize += count;
		for (int i = 0; i < size; i++) {
			if (order [i] >= start) order [i] += count;
		}
		treeValid = false;
	}

	/* removes source rows start .. start + count - 1; returns the first damaged view row */
	int removeSourceRange (int start, int count) {
		int first = size;
		for (int s = start; s < start + count; s++) {
			if (inverse [s] >= 0) {
				first = Math.min (first, inverse [s]);
				inverse [s] = REMOVED;
			}
		}
		compact (first);
		for (int i = 0; i < size; i++) {
			if (order [i] >= start + count) order [i] -= count;
		}
		System.arraycopy (inverse, start + count, inverse, start, sourceSize - start - count);
		sourceSize -= count;
		return first;
	}

	/* adds rows [0, m) (not in the view); returns the first damaged view row */
	int insert (int [] rows, int m) {
		if (m == 0) return size;
		if (m > (size >> 2) && m >= M3SortLanes.MERGE_CUTOVER) return union (rows, m);
		batch = ensure (batch, m, 0);
		System.arraycopy (rows, 0, batch, 0, m);
		sort.sortRows (batch, m);
		order = ensure (order, size + m, size);
		int i = size - 1, out = size + m - 1, first = size;
		for (int j = m - 1; j >= 0; j--) {
			int row = batch [j], p = upper (row, 0, i + 1);
			int moved = i + 1 - p;
			System.arraycopy (order, p, order, out - moved + 1, moved);
			out -= moved;
			i = p - 1;
			order [out--] = row;
			first = p;
		}
		size += m;
		reindex (first);
		return first;
	}

	/* removes rows [0, m) that are in the view; returns the first damaged view row */
	int remove (int [] rows, int m) {
		int first = size;
		for (int k = 0; k < m; k++) {
			int position = inverse [rows [k]];
			if (position >= 0) {
				first = Math.min (first, position);
				inverse [rows [k]] = REMOVED;
			}
		}
		compact (first);
		return first;
	}

	/* rows [0, m) of the view changed their values; returns {first, last} damaged view rows */
	void update (int [] rows, int m, M3RowChange change) {
		if (m == 1) {
			move (rows [0], change);
			return;
		}
		int first = remove (rows, m);
		first = Math.min (first, insert (rows, m));
		change.damage (first, size - 1);
	}

	private void move (int row, M3RowChange change) {
		int old = inverse [row];
		if (old < 0) return;
		if (old > 0 && sort.compare (order [old - 1], row) > 0) {
			int p = upper (row, 0, old);
			System.arraycopy (order, p, order, p + 1, old - p);
			order [p] = row;
			reindex (p, old + 1);
			change.damage (p, old);
		} else if (old < size - 1 && sort.compare (order [old + 1], row) < 0) {
			int q = upper (row, old + 1, size) - 1;
			System.arraycopy (order, old + 1, order, old, q - old);
			order [q] = row;
			reindex (old, q + 1);
			change.damage (old, q);
		} else {
			change.damage (old, old);
		}
		treeValid = false;
	}

	/* the first position in [low, high) whose row sorts after row (rows are distinct, the order is total) */
	private int upper (int row, int low, int high) {
		while (low < high) {
			int middle = (low + high) >>> 1;
			if (sort.compare (order [middle], row) > 0) high = middle; else low = middle + 1;
		}
		return low;
	}

	private int union (int [] rows, int m) {
		order = ensure (order, size + m, size);
		System.arraycopy (rows, 0, order, size, m);
		size += m;
		sort.sortRows (order, size);
		reindex (0);
		return 0;
	}

	private void compact (int first) {
		int write = first;
		for (int read = first; read < size; read++) {
			int row = order [read];
			if (inverse [row] == REMOVED) {
				inverse [row] = -1;
			} else {
				order [write] = row;
				inverse [row] = write++;
			}
		}
		size = write;
		treeValid = false;
	}

	private void reindex (int first) {
		reindex (first, size);
	}

	private void reindex (int first, int end) {
		for (int i = first; i < end; i++) inverse [order [i]] = i;
		treeValid = false;
	}

	/* the sibling index for tree mode, rebuilt on demand by one stable counting pass over the order */
	void buildTree (IntUnaryOperator parentOf) {
		if (treeValid) return;
		childStart = ensureExact (childStart, sourceSize + 2);
		Arrays.fill (childStart, 0, sourceSize + 2, 0);
		children = ensure (children, size, 0);
		siblingIndex = ensure (siblingIndex, sourceSize, 0);
		for (int i = 0; i < size; i++) {
			siblingIndex [order [i]] = bucket (parentOf, order [i]);
			childStart [siblingIndex [order [i]] + 1]++;
		}
		for (int b = 0; b <= sourceSize; b++) childStart [b + 1] += childStart [b];
		cursor = ensureExact (cursor, sourceSize + 1);
		System.arraycopy (childStart, 0, cursor, 0, sourceSize + 1);
		for (int i = 0; i < size; i++) {
			int row = order [i], b = siblingIndex [row];
			int position = cursor [b]++;
			children [position] = row;
			siblingIndex [row] = position - childStart [b];
		}
		treeValid = true;
	}

	void invalidateTree () {
		treeValid = false;
	}

	private int bucket (IntUnaryOperator parentOf, int row) {
		int parent = parentOf.applyAsInt (row);
		M3Checks.argument (parent >= -1 && parent < sourceSize);
		return parent + 1;
	}

	int childCount (int parent) {
		return childStart [parent + 2] - childStart [parent + 1];
	}

	int child (int parent, int index) {
		M3Checks.range (index, childCount (parent));
		return children [childStart [parent + 1] + index];
	}

	int indexOfChild (int row) {
		return inverse [row] < 0 ? -1 : siblingIndex [row];
	}

	private static int [] ensure (int [] lane, int capacity, int keep) {
		if (lane.length >= capacity) return lane;
		int [] grown = new int [Math.max (capacity, lane.length + (lane.length >> 1))];
		System.arraycopy (lane, 0, grown, 0, keep);
		return grown;
	}

	private static int [] ensureExact (int [] lane, int capacity) {
		return lane.length >= capacity ? lane : new int [capacity];
	}
}

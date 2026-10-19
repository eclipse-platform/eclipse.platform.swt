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
 * Columnar per-row state of a list widget with columns: one array per (field, column), indexed by row.
 * <p>
 * A widget names its fields: object fields (for example text, image, font) and <code>int</code> fields
 * with a default value each (for example colours, default -1). A lane, the array of one field in one
 * column, is allocated on its first value that is not the default, so an untouched field or column costs
 * nothing. Rows are inserted, removed and truncated in every allocated lane together (one array copy per
 * lane); columns are inserted and removed by moving lanes, not values. Growth follows
 * {@link VirtualGrowth}. Only <code>int</code> arithmetic is used.
 * </p>
 */
final class VirtualCellLanes {
	private final int [] intDefaults;
	/* [field][column] -> row values; null until the lane holds a value that is not the default */
	private Object [][][] objects;
	private int [][][] ints;
	private int columns, length, capacity;

	/**
	 * @param objectFields the number of object fields (default <code>null</code>)
	 * @param intDefaults the default value of every <code>int</code> field
	 * @param columns the number of columns (at least one)
	 */
	VirtualCellLanes (int objectFields, int [] intDefaults, int columns) {
		this.intDefaults = intDefaults.clone ();
		this.columns = Math.max (1, columns);
		objects = new Object [objectFields] [this.columns] [];
		ints = new int [intDefaults.length] [this.columns] [];
	}

	int length () {
		return length;
	}

	int columns () {
		return columns;
	}

	Object get (int field, int column, int row) {
		Object [] lane = column < columns ? objects [field] [column] : null;
		return lane == null || row >= length ? null : lane [row];
	}

	void set (int field, int column, int row, Object value) {
		checkRow (row);
		if (column >= columns) return;
		Object [] lane = objects [field] [column];
		if (lane == null) {
			if (value == null) return;
			lane = objects [field] [column] = new Object [capacity];
		}
		lane [row] = value;
	}

	int getInt (int field, int column, int row) {
		int [] lane = column < columns ? ints [field] [column] : null;
		return lane == null || row >= length ? intDefaults [field] : lane [row];
	}

	void setInt (int field, int column, int row, int value) {
		checkRow (row);
		if (column >= columns) return;
		int [] lane = ints [field] [column];
		if (lane == null) {
			if (value == intDefaults [field]) return;
			lane = ints [field] [column] = new int [capacity];
			if (intDefaults [field] != 0) Arrays.fill (lane, intDefaults [field]);
		}
		lane [row] = value;
	}

	/** The largest value of an <code>int</code> field in one column over every row (its default when nothing is set). */
	int maxInt (int field, int column) {
		int [] lane = column < columns ? ints [field] [column] : null;
		int max = intDefaults [field];
		if (lane == null) return max;
		for (int row = 0; row < length; row++) max = Math.max (max, lane [row]);
		return max;
	}

	/** Whether any field of any column holds a value that is not the default in a row. */
	boolean hasState (int row) {
		if (row < 0 || row >= length) return false;
		for (Object [][] field : objects) {
			for (Object [] lane : field) {
				if (lane != null && lane [row] != null) return true;
			}
		}
		for (int f = 0; f < ints.length; f++) {
			for (int [] lane : ints [f]) {
				if (lane != null && lane [row] != intDefaults [f]) return true;
			}
		}
		return false;
	}

	/** Sets every field of every column of a row to its default. */
	void clearRow (int row) {
		if (row < 0 || row >= length) return;
		for (Object [][] field : objects) {
			for (Object [] lane : field) {
				if (lane != null) lane [row] = null;
			}
		}
		for (int f = 0; f < ints.length; f++) {
			for (int [] lane : ints [f]) {
				if (lane != null) lane [row] = intDefaults [f];
			}
		}
	}

	/** Drops the lane of one object field in one column: the field holds its default in every row. */
	void dropLane (int field, int column) {
		if (column < columns) objects [field] [column] = null;
	}

	/** Drops the lane of one <code>int</code> field in one column. */
	void dropIntLane (int field, int column) {
		if (column < columns) ints [field] [column] = null;
	}

	/** Sets every field of one column to its default in every row (the column's lanes are dropped). */
	void clearColumn (int column) {
		if (column >= columns) return;
		for (Object [][] field : objects) field [column] = null;
		for (int [][] field : ints) field [column] = null;
	}

	/** Changes the row count; rows beyond a smaller count are dropped, new rows hold defaults. */
	void setLength (int newLength) {
		if (newLength < 0) throw new IllegalArgumentException ("negative length");
		if (newLength < length) fillDefaults (newLength, length);
		ensureCapacity (newLength);
		length = newLength;
	}

	/** Inserts <code>count</code> rows holding defaults before <code>index</code>. */
	void insertRows (int index, int count) {
		if (index < 0 || index > length || count < 0) throw new IndexOutOfBoundsException (index);
		if (count == 0) return;
		ensureCapacity (length + count);
		forEachLane (lane -> System.arraycopy (lane, index, lane, index + count, length - index), lane -> System.arraycopy (lane, index, lane, index + count, length - index));
		length += count;
		fillDefaults (index, index + count);
	}

	/** Removes the rows <code>[index, index + count)</code>. */
	void removeRows (int index, int count) {
		if (index < 0 || count < 0 || index + count > length) throw new IndexOutOfBoundsException (index);
		if (count == 0) return;
		int tail = length - index - count;
		forEachLane (lane -> System.arraycopy (lane, index + count, lane, index, tail), lane -> System.arraycopy (lane, index + count, lane, index, tail));
		fillDefaults (length - count, length);
		length -= count;
	}

	/** Inserts a column of defaults before <code>column</code>. */
	void insertColumn (int column) {
		if (column < 0 || column > columns) throw new IndexOutOfBoundsException (column);
		for (int f = 0; f < objects.length; f++) {
			Object [][] lanes = new Object [columns + 1] [];
			System.arraycopy (objects [f], 0, lanes, 0, column);
			System.arraycopy (objects [f], column, lanes, column + 1, columns - column);
			objects [f] = lanes;
		}
		for (int f = 0; f < ints.length; f++) {
			int [][] lanes = new int [columns + 1] [];
			System.arraycopy (ints [f], 0, lanes, 0, column);
			System.arraycopy (ints [f], column, lanes, column + 1, columns - column);
			ints [f] = lanes;
		}
		columns++;
	}

	/** Removes a column; the last column is never removed (it is cleared instead). */
	void removeColumn (int column) {
		if (column < 0 || column >= columns) throw new IndexOutOfBoundsException (column);
		if (columns == 1) {
			clearColumn (0);
			return;
		}
		for (int f = 0; f < objects.length; f++) {
			Object [][] lanes = new Object [columns - 1] [];
			System.arraycopy (objects [f], 0, lanes, 0, column);
			System.arraycopy (objects [f], column + 1, lanes, column, columns - column - 1);
			objects [f] = lanes;
		}
		for (int f = 0; f < ints.length; f++) {
			int [][] lanes = new int [columns - 1] [];
			System.arraycopy (ints [f], 0, lanes, 0, column);
			System.arraycopy (ints [f], column + 1, lanes, column, columns - column - 1);
			ints [f] = lanes;
		}
		columns--;
	}

	private interface ObjectLaneAction {
		void apply (Object [] lane);
	}

	private interface IntLaneAction {
		void apply (int [] lane);
	}

	private void forEachLane (ObjectLaneAction objectAction, IntLaneAction intAction) {
		for (Object [][] field : objects) {
			for (Object [] lane : field) {
				if (lane != null) objectAction.apply (lane);
			}
		}
		for (int [][] field : ints) {
			for (int [] lane : field) {
				if (lane != null) intAction.apply (lane);
			}
		}
	}

	private void fillDefaults (int start, int end) {
		for (Object [][] field : objects) {
			for (Object [] lane : field) {
				if (lane != null) Arrays.fill (lane, start, end, null);
			}
		}
		for (int f = 0; f < ints.length; f++) {
			for (int [] lane : ints [f]) {
				if (lane != null) Arrays.fill (lane, start, end, intDefaults [f]);
			}
		}
	}

	private void ensureCapacity (int required) {
		if (required <= capacity) return;
		int newCapacity = VirtualGrowth.capacity (capacity, required, 4);
		for (Object [][] field : objects) {
			for (int c = 0; c < field.length; c++) {
				if (field [c] != null) field [c] = Arrays.copyOf (field [c], newCapacity);
			}
		}
		for (int f = 0; f < ints.length; f++) {
			for (int c = 0; c < ints [f].length; c++) {
				int [] lane = ints [f] [c];
				if (lane == null) continue;
				int [] grown = Arrays.copyOf (lane, newCapacity);
				if (intDefaults [f] != 0) Arrays.fill (grown, lane.length, newCapacity, intDefaults [f]);
				ints [f] [c] = grown;
			}
		}
		capacity = newCapacity;
	}

	private void checkRow (int row) {
		if (row < 0 || row >= length) throw new IndexOutOfBoundsException (row);
	}
}

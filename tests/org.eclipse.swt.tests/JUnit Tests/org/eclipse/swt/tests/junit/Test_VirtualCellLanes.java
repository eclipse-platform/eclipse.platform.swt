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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.Test;

/**
 * Headless differential test of the package-private columnar cell lanes
 * (<code>VirtualCellLanes</code>, reached by reflection like the other viewport tests) against an
 * oracle that keeps every cell in a map: seeded scripts over rows, columns and both field kinds,
 * and the error contract.
 */
public class Test_VirtualCellLanes {
	/** Two object fields and two int fields with the defaults of the Table's cell lanes. */
	private static final int [] DEFAULTS = {-1, 0};

	private final Object lanes = construct (2, DEFAULTS, 2);

	private static Object construct (int objectFields, int [] intDefaults, int columns) {
		try {
			Constructor<?> constructor = Class.forName ("org.eclipse.swt.widgets.VirtualCellLanes").getDeclaredConstructor (int.class, int [].class, int.class);
			constructor.setAccessible (true);
			return constructor.newInstance (objectFields, intDefaults, columns);
		} catch (ReflectiveOperationException failure) {
			throw new AssertionError (failure);
		}
	}

	private Object call (String name, Class<?> [] types, Object... args) {
		try {
			Method method = lanes.getClass ().getDeclaredMethod (name, types);
			method.setAccessible (true);
			return method.invoke (lanes, args);
		} catch (InvocationTargetException failure) {
			if (failure.getCause () instanceof RuntimeException runtime) throw runtime;
			throw new AssertionError (failure.getCause ());
		} catch (ReflectiveOperationException failure) {
			throw new AssertionError (failure);
		}
	}

	private static final Class<?> [] NONE = {};
	private static final Class<?> [] INT = {int.class};
	private static final Class<?> [] INT_INT = {int.class, int.class};
	private static final Class<?> [] GET = {int.class, int.class, int.class};
	private static final Class<?> [] SET = {int.class, int.class, int.class, Object.class};
	private static final Class<?> [] SET_INT = {int.class, int.class, int.class, int.class};

	/** Oracle: object and int cells by field and column, as lists of rows; absent means default. */
	private final List<List<Map<Integer, Object>>> objects = kind (2, 2);
	private final List<List<Map<Integer, Integer>>> ints = kind (2, 2);
	private int length;

	private static <V> List<List<Map<Integer, V>>> kind (int fields, int columns) {
		List<List<Map<Integer, V>>> result = new ArrayList<> ();
		for (int f = 0; f < fields; f++) {
			List<Map<Integer, V>> lanes = new ArrayList<> ();
			for (int c = 0; c < columns; c++) lanes.add (new HashMap<> ());
			result.add (lanes);
		}
		return result;
	}

	private int columns () {
		return objects.get (0).size ();
	}

	private <V> void moveRows (Map<Integer, V> lane, int from, int shift) {
		Map<Integer, V> moved = new HashMap<> ();
		for (Iterator<Map.Entry<Integer, V>> rows = lane.entrySet ().iterator (); rows.hasNext ();) {
			Map.Entry<Integer, V> cell = rows.next ();
			if (cell.getKey () >= from) {
				int row = cell.getKey () + shift;
				if (shift > 0 || cell.getKey () >= from - shift) moved.put (row, cell.getValue ());
				rows.remove ();
			}
		}
		lane.putAll (moved);
	}

	private void forEachLane (java.util.function.Consumer<Map<Integer, ?>> action) {
		for (List<Map<Integer, Object>> field : objects) field.forEach (action::accept);
		for (List<Map<Integer, Integer>> field : ints) field.forEach (action::accept);
	}

	private void verify () {
		assertEquals (length, (int) (Integer) call ("length", NONE), "length");
		assertEquals (columns (), (int) (Integer) call ("columns", NONE), "columns");
		for (int row = 0; row < length; row++) {
			boolean state = false;
			for (int f = 0; f < 2; f++) {
				for (int c = 0; c < columns (); c++) {
					Object value = objects.get (f).get (c).get (row);
					assertEquals (value, call ("get", GET, f, c, row), "get");
					Integer intValue = ints.get (f).get (c).get (row);
					assertEquals (intValue == null ? DEFAULTS [f] : intValue, (int) (Integer) call ("getInt", GET, f, c, row), "getInt");
					state |= value != null || intValue != null && intValue != DEFAULTS [f];
				}
			}
			assertEquals (state, call ("hasState", INT, row), "hasState " + row);
		}
		for (int f = 0; f < 2; f++) {
			for (int c = 0; c < columns (); c++) {
				int max = DEFAULTS [f];
				for (Map.Entry<Integer, Integer> cell : ints.get (f).get (c).entrySet ()) {
					if (cell.getKey () < length) max = Math.max (max, cell.getValue ());
				}
				assertEquals (max, (int) (Integer) call ("maxInt", INT_INT, f, c), "maxInt");
			}
		}
		assertFalse ((Boolean) call ("hasState", INT, -1));
		assertFalse ((Boolean) call ("hasState", INT, length));
		assertNull (call ("get", GET, 0, columns (), 0), "a column beyond the last answers the default");
		assertEquals (DEFAULTS [0], (int) (Integer) call ("getInt", GET, 0, columns (), 0));
	}

	/** Seeded scripts over every operation, answered like the oracle. */
	@Test
	public void seededScriptsAnswerLikeAMapOfCells () {
		for (int seed = 0; seed < 6; seed++) {
			Random random = new Random (seed);
			call ("setLength", INT, 0);
			length = 0;
			forEachLane (Map::clear);
			for (int step = 0; step < 300; step++) {
				int field = random.nextInt (2), column = random.nextInt (columns () + 1);
				switch (random.nextInt (length == 0 ? 3 : 12)) {
					case 0 -> { /* insert rows */
						int at = random.nextInt (length + 1), count = random.nextInt (4);
						call ("insertRows", INT_INT, at, count);
						if (count > 0) forEachLane (lane -> moveRows (lane, at, count));
						length += count;
					}
					case 1 -> { /* change the row count */
						int next = random.nextInt (8);
						call ("setLength", INT, next);
						if (next < length) forEachLane (lane -> lane.keySet ().removeIf (row -> row >= next));
						length = next;
					}
					case 2 -> { /* insert a column */
						int at = random.nextInt (columns () + 1);
						call ("insertColumn", INT, at);
						for (List<Map<Integer, Object>> lanes : objects) lanes.add (at, new HashMap<> ());
						for (List<Map<Integer, Integer>> lanes : ints) lanes.add (at, new HashMap<> ());
					}
					case 3 -> { /* remove a column; the last one is cleared instead */
						int at = random.nextInt (columns ());
						call ("removeColumn", INT, at);
						if (columns () == 1) {
							forEachLane (Map::clear);
						} else {
							for (List<Map<Integer, Object>> lanes : objects) lanes.remove (at);
							for (List<Map<Integer, Integer>> lanes : ints) lanes.remove (at);
						}
					}
					case 4, 5 -> { /* set an object cell (a write to a column beyond the last is ignored) */
						int row = random.nextInt (length);
						Object value = random.nextBoolean () ? "v" + random.nextInt (4) : null;
						call ("set", SET, field, column, row, value);
						if (column < columns ()) {
							if (value == null) objects.get (field).get (column).remove (row);
							else objects.get (field).get (column).put (row, value);
						}
					}
					case 6, 7 -> { /* set an int cell */
						int row = random.nextInt (length);
						int value = random.nextBoolean () ? random.nextInt (5) : DEFAULTS [field];
						call ("setInt", SET_INT, field, column, row, value);
						if (column < columns ()) {
							if (value == DEFAULTS [field]) ints.get (field).get (column).remove (row);
							else ints.get (field).get (column).put (row, value);
						}
					}
					case 8 -> { /* remove rows */
						int at = random.nextInt (length), count = random.nextInt (length - at + 1);
						call ("removeRows", INT_INT, at, count);
						if (count > 0) forEachLane (lane -> moveRows (lane, at, -count));
						length -= count;
					}
					case 9 -> { /* clear one row */
						int row = random.nextInt (length);
						call ("clearRow", INT, row);
						forEachLane (lane -> lane.remove (row));
					}
					case 10 -> { /* drop a lane */
						if (random.nextBoolean ()) {
							call ("dropLane", INT_INT, field, column);
							if (column < columns ()) objects.get (field).get (column).clear ();
						} else {
							call ("dropIntLane", INT_INT, field, column);
							if (column < columns ()) ints.get (field).get (column).clear ();
						}
					}
					default -> { /* clear one column */
						int at = random.nextInt (columns ());
						call ("clearColumn", INT, at);
						for (List<Map<Integer, Object>> lanes : objects) lanes.get (at).clear ();
						for (List<Map<Integer, Integer>> lanes : ints) lanes.get (at).clear ();
					}
				}
				verify ();
			}
		}
	}

	/** Reads beyond the row count answer the default; writes and invalid rows are refused. */
	@Test
	public void boundsAnswerDefaultsAndWritesAreRefused () {
		call ("setLength", INT, 2);
		call ("set", SET, 0, 0, 1, "kept");
		assertNull (call ("get", GET, 0, 0, 5), "a row beyond the count answers the default without an error");
		assertEquals (-1, (int) (Integer) call ("getInt", GET, 0, 0, 5));
		assertThrows (IndexOutOfBoundsException.class, () -> call ("set", SET, 0, 0, 2, "x"));
		assertThrows (IndexOutOfBoundsException.class, () -> call ("setInt", SET_INT, 0, 0, -1, 3));
		assertThrows (IndexOutOfBoundsException.class, () -> call ("insertRows", INT_INT, 3, 1));
		assertThrows (IndexOutOfBoundsException.class, () -> call ("insertRows", INT_INT, 0, -1));
		assertThrows (IndexOutOfBoundsException.class, () -> call ("removeRows", INT_INT, 1, 2));
		assertThrows (IndexOutOfBoundsException.class, () -> call ("removeRows", INT_INT, -1, 1));
		assertThrows (IllegalArgumentException.class, () -> call ("setLength", INT, -1));
		assertThrows (IndexOutOfBoundsException.class, () -> call ("insertColumn", INT, 3));
		assertThrows (IndexOutOfBoundsException.class, () -> call ("removeColumn", INT, 2));
		call ("clearRow", INT, 5);
		call ("clearRow", INT, -1);
		assertEquals ("kept", call ("get", GET, 0, 0, 1), "out-of-range clears change nothing");
		call ("dropLane", INT_INT, 0, 5);
		call ("dropIntLane", INT_INT, 0, 5);
		call ("clearColumn", INT, 5);
		assertEquals ("kept", call ("get", GET, 0, 0, 1), "a column beyond the last is ignored");
	}

	/** A default write makes no lane; rows beyond a shrink hold defaults after growing again. */
	@Test
	public void lanesExistOnlyForValuesThatAreNotTheDefault () {
		call ("setLength", INT, 3);
		call ("set", SET, 0, 0, 1, null);
		call ("setInt", SET_INT, 0, 0, 1, -1);
		assertFalse ((Boolean) call ("hasState", INT, 1), "default writes make no lane");
		call ("setInt", SET_INT, 1, 1, 2, 7);
		call ("set", SET, 1, 1, 0, "state");
		assertTrue ((Boolean) call ("hasState", INT, 2));
		assertEquals (7, (int) (Integer) call ("maxInt", INT_INT, 1, 1));
		call ("setLength", INT, 1);
		call ("setLength", INT, 64);
		assertEquals (0, (int) (Integer) call ("getInt", GET, 1, 1, 2), "a dropped row holds the default after growing again");
		assertEquals ("state", call ("get", GET, 1, 1, 0), "a kept row keeps its state");
	}
}

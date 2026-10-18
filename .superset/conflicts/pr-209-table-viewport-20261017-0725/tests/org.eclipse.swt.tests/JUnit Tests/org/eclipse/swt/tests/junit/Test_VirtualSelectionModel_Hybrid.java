/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Headless differential contract of the hybrid selection containers (ranges plus
 * int bitmap): every random sequence gives the same answers as the range-only
 * model, the bitmap is entered for scattered selections and left on clear and
 * selectAll, and select-all over 10,000,000 rows is O(1).
 */
public class Test_VirtualSelectionModel_Hybrid {

	private static final class Model {
		final Object target;

		Model (boolean hybrid) {
			try {
				Constructor<?> constructor = Class.forName ("org.eclipse.swt.widgets.VirtualSelectionModel").getDeclaredConstructor (boolean.class);
				constructor.setAccessible (true);
				target = constructor.newInstance (hybrid);
			} catch (ReflectiveOperationException e) {
				throw new IllegalStateException (e);
			}
		}

		Object call (String name, Object... arguments) {
			Class<?> [] types = new Class<?> [arguments.length];
			for (int i = 0; i < arguments.length; i++) {
				types [i] = arguments [i] instanceof Boolean ? boolean.class : int.class;
			}
			try {
				Method method = target.getClass ().getDeclaredMethod (name, types);
				method.setAccessible (true);
				return method.invoke (target, arguments);
			} catch (InvocationTargetException e) {
				if (e.getCause () instanceof RuntimeException runtime) throw runtime;
				throw new IllegalStateException (e.getCause ());
			} catch (ReflectiveOperationException e) {
				throw new IllegalStateException (e);
			}
		}

		int number (String name, Object... arguments) {
			return (Integer) call (name, arguments);
		}
	}

	private static void assertSameState (Model expected, Model actual, String context) {
		int count = expected.number ("logicalCount");
		assertEquals (count, actual.number ("logicalCount"), context);
		assertEquals (expected.number ("selectedCount"), actual.number ("selectedCount"), context);
		assertArrayEquals ((int []) expected.call ("toArray"), (int []) actual.call ("toArray"), context);
		for (int i = 0; i < count; i++) {
			assertEquals (expected.call ("isSelected", i), actual.call ("isSelected", i), context + " row " + i);
		}
		if (!(Boolean) expected.call ("complementMode")) {
			assertEquals (expected.number ("rangeCount"), actual.number ("rangeCount"), context);
			for (int r = 0; r < expected.number ("rangeCount"); r++) {
				assertEquals (expected.number ("rangeStart", r), actual.number ("rangeStart", r), context);
				assertEquals (expected.number ("rangeEndExclusive", r), actual.number ("rangeEndExclusive", r), context);
			}
		}
	}

	@Test
	public void randomSequencesAgreeWithTheRangeModel () {
		int bitmapSeen = 0;
		for (int seed = 1; seed <= 40; seed++) {
			Random random = new Random (seed);
			Model ranges = new Model (false), hybrid = new Model (true);
			int count = 200 + random.nextInt (400);
			ranges.call ("setLogicalCount", count);
			hybrid.call ("setLogicalCount", count);
			for (int step = 0; step < 400; step++) {
				int logical = ranges.number ("logicalCount");
				int a = logical == 0 ? 0 : random.nextInt (logical);
				int b = Math.min (logical, a + random.nextInt (8));
				int pick = random.nextInt (100);
				Object [] call;
				if (pick < 45 && logical > 0) {
					call = new Object [] {"setSelected", a, random.nextInt (3) != 0};
				} else if (pick < 55) {
					call = new Object [] {"selectRange", a, b};
				} else if (pick < 62) {
					call = new Object [] {"deselectRange", a, b};
				} else if (pick < 72) {
					call = new Object [] {"insert", a, random.nextInt (5)};
				} else if (pick < 82 && logical > 0) {
					call = new Object [] {"remove", a, Math.min (logical - a, random.nextInt (5))};
				} else if (pick < 86) {
					call = new Object [] {"setLogicalCount", Math.max (0, logical + random.nextInt (21) - 10)};
				} else if (pick < 88) {
					call = new Object [] {"clear"};
				} else if (pick < 90) {
					call = new Object [] {"selectAll"};
				} else {
					call = new Object [] {"selectedCount"};
				}
				Object [] arguments = new Object [call.length - 1];
				System.arraycopy (call, 1, arguments, 0, arguments.length);
				ranges.call ((String) call [0], arguments);
				hybrid.call ((String) call [0], arguments);
				if ((Boolean) hybrid.call ("bitmapMode")) bitmapSeen++;
				assertSameState (ranges, hybrid, "seed=" + seed + " step=" + step + " " + call [0]);
			}
		}
		assertTrue (bitmapSeen > 0, "the random sequences reach the bitmap container");
	}

	@Test
	public void scatteredSelectionUsesTheBitmapAndClearLeavesIt () {
		Model hybrid = new Model (true);
		hybrid.call ("setLogicalCount", 100_000);
		for (int i = 0; i < 100_000; i += 7) hybrid.call ("setSelected", i, true);
		assertTrue ((Boolean) hybrid.call ("bitmapMode"));
		assertEquals ((100_000 + 6) / 7, hybrid.number ("selectedCount"));
		hybrid.call ("clear");
		assertFalse ((Boolean) hybrid.call ("bitmapMode"));
		assertEquals (0, hybrid.number ("selectedCount"));
	}

	@Test
	public void selectAllOverTenMillionRowsIsConstant () {
		Model hybrid = new Model (true);
		hybrid.call ("setLogicalCount", 10_000_000);
		long t0 = System.nanoTime ();
		hybrid.call ("selectAll");
		int selected = hybrid.number ("selectedCount");
		long t1 = System.nanoTime ();
		hybrid.call ("setSelected", 5_000_000, false);
		long t2 = System.nanoTime ();
		assertEquals (10_000_000, selected);
		assertEquals (9_999_999, hybrid.number ("selectedCount"));
		assertFalse ((Boolean) hybrid.call ("bitmapMode"), "select-all stays a complement range set");
		System.out.println ("selectAll+count 10M: " + (t1 - t0) / 1000 + " us; deselect one: " + (t2 - t1) / 1000 + " us");
		assertTrue (t1 - t0 < 50_000_000L, "select-all over 10M rows took " + (t1 - t0) + " ns");
	}
}

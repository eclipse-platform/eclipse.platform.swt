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

import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.Test;

/**
 * Headless differential test of the package-private parent bit lanes (checked, grayed, tab
 * filled bits) and of the package-private block cache behind VirtualPageSource, against
 * independent boolean/array oracles. Reached by reflection like the other viewport tests.
 */
public class Test_VirtualBitLanes {
	private static Object construct (String name, Class<?> [] types, Object... args) throws Exception {
		Constructor<?> constructor = Class.forName ("org.eclipse.swt.widgets." + name).getDeclaredConstructor (types);
		constructor.setAccessible (true);
		return constructor.newInstance (args);
	}

	private static Object call (Object target, String name, Object... args) throws Exception {
		Class<?> [] types = new Class<?> [args.length];
		for (int i = 0; i < args.length; i++) {
			Object v = args [i];
			types [i] = v instanceof Integer ? int.class : v instanceof Boolean ? boolean.class : v instanceof Long ? long.class : v.getClass ();
		}
		Method method = target.getClass ().getDeclaredMethod (name, types);
		method.setAccessible (true);
		try {
			return method.invoke (target, args);
		} catch (InvocationTargetException failure) {
			if (failure.getCause () instanceof RuntimeException runtime) throw runtime;
			if (failure.getCause () instanceof Error error) throw error;
			throw failure;
		}
	}

	private static Object lanes (int count) throws Exception {
		return construct ("VirtualBitLanes", new Class<?> [] {int.class}, count);
	}

	/** Oracle: a growable list of boolean rows per lane. */
	private static final class Oracle {
		final List<boolean []> rows = new ArrayList<> ();
		final int lanes;

		Oracle (int lanes) {
			this.lanes = lanes;
		}

		void setLength (int length) {
			while (rows.size () > length) rows.remove (rows.size () - 1);
			while (rows.size () < length) rows.add (new boolean [lanes]);
		}

		void insert (int index, int count) {
			for (int i = 0; i < count; i++) rows.add (index, new boolean [lanes]);
		}

		void remove (int index, int count) {
			for (int i = 0; i < count; i++) rows.remove (index);
		}
	}

	private static void assertSame (Oracle oracle, Object lanes) throws Exception {
		assertEquals (oracle.rows.size (), (int) call (lanes, "length"));
		for (int lane = 0; lane < oracle.lanes; lane++) {
			int count = 0;
			int expectedNext = -1;
			for (int i = oracle.rows.size () - 1; i >= 0; i--) {
				boolean bit = oracle.rows.get (i) [lane];
				assertEquals (bit, call (lanes, "get", lane, i), "lane " + lane + " row " + i);
				if (bit) {
					count++;
					expectedNext = i;
				}
			}
			assertEquals (count, (int) call (lanes, "count", lane), "count lane " + lane);
			assertEquals (expectedNext, (int) call (lanes, "nextSet", lane, 0), "nextSet lane " + lane);
		}
	}

	@Test
	public void test_randomDifferentialAgainstBooleanRows () throws Exception {
		Random random = new Random (20261013);
		for (int round = 0; round < 40; round++) {
			Object lanes = lanes (2);
			Oracle oracle = new Oracle (2);
			int length = random.nextInt (300);
			call (lanes, "setLength", length);
			oracle.setLength (length);
			for (int op = 0; op < 400; op++) {
				int size = oracle.rows.size ();
				int lane = random.nextInt (2);
				switch (random.nextInt (7)) {
					case 0, 1 -> {
						if (size == 0) break;
						int i = random.nextInt (size);
						boolean v = random.nextBoolean ();
						call (lanes, "set", lane, i, v);
						oracle.rows.get (i) [lane] = v;
					}
					case 2 -> {
						int a = random.nextInt (size + 1), b = random.nextInt (size + 1);
						int start = Math.min (a, b), end = Math.max (a, b);
						boolean v = random.nextBoolean ();
						call (lanes, "setRange", lane, start, end, v);
						for (int i = start; i < end; i++) oracle.rows.get (i) [lane] = v;
					}
					case 3 -> {
						int i = random.nextInt (size + 1);
						int n = random.nextInt (70);
						call (lanes, "insert", i, n);
						oracle.insert (i, n);
					}
					case 4 -> {
						if (size == 0) break;
						int i = random.nextInt (size);
						int n = random.nextInt (Math.min (70, size - i) + 1);
						call (lanes, "remove", i, n);
						oracle.remove (i, n);
					}
					case 5 -> {
						int n = random.nextInt (400);
						call (lanes, "setLength", n);
						oracle.setLength (n);
					}
					default -> {
						int from = random.nextInt (size + 1);
						int expected = -1;
						for (int i = from; i < size; i++) {
							if (oracle.rows.get (i) [lane]) {
								expected = i;
								break;
							}
						}
						assertEquals (expected, (int) call (lanes, "nextSet", lane, from));
					}
				}
			}
			assertSame (oracle, lanes);
		}
	}

	@Test
	public void test_checkAll10MillionRowsIsWordWise () throws Exception {
		int rows = 10_000_000;
		Object lanes = lanes (2);
		call (lanes, "setLength", rows);
		long t0 = System.nanoTime ();
		call (lanes, "setRange", 0, 0, rows, true);
		int checked = (int) call (lanes, "count", 0);
		long t1 = System.nanoTime ();
		call (lanes, "setRange", 0, 0, rows, false);
		int cleared = (int) call (lanes, "count", 0);
		long t2 = System.nanoTime ();
		call (lanes, "set", 0, 9_999_999, true);
		call (lanes, "insert", 0, 1);
		boolean shifted = (boolean) call (lanes, "get", 0, 10_000_000);
		long t3 = System.nanoTime ();
		assertEquals (rows, checked);
		assertEquals (0, cleared);
		assertTrue (shifted);
		System.out.printf ("LAZY-METRIC\tcheck_all_10M_ms\t%.3f%nLAZY-METRIC\tuncheck_all_10M_ms\t%.3f%nLAZY-METRIC\tinsert_shift_10M_ms\t%.3f%n",
				(t1 - t0) / 1e6, (t2 - t1) / 1e6, (t3 - t2) / 1e6);
	}

	@Test
	public void test_untouchedLaneAllocatesNothing () throws Exception {
		Object lanes = lanes (2);
		call (lanes, "setLength", 10_000_000);
		Field field = lanes.getClass ().getDeclaredField ("lanes");
		field.setAccessible (true);
		int [][] words = (int [][]) field.get (lanes);
		assertNull (words [0]);
		assertNull (words [1]);
		call (lanes, "set", 1, 5, false);
		assertNull (words [1]);
		call (lanes, "set", 1, 5, true);
		assertNotNull (((int [][]) field.get (lanes)) [1]);
		assertNull (((int [][]) field.get (lanes)) [0]);
	}

	@Test
	public void test_blockCacheReservesEvictsLeastRecentAndProtectsCurrentFrame () throws Exception {
		Object cache = construct ("VirtualBlockCache", new Class<?> [] {int.class, int.class, int.class}, 4, 3, 2);
		int loaded = staticInt ("VirtualBlockStore", "LOADED");
		int pending = staticInt ("VirtualBlockStore", "PENDING");
		call (cache, "nextFrame");
		int a = (int) call (cache, "reserve", -1, 0);
		int b = (int) call (cache, "reserve", -1, 1);
		int c = (int) call (cache, "reserve", -1, 2);
		assertEquals (3, Set.of (a, b, c).size ());
		assertEquals (pending, (int) call (cache, "state", a));
		assertEquals (-1, (int) call (cache, "reserve", -1, 3), "all slots busy");
		for (int slot : new int [] {a, b, c}) call (cache, "state", slot, loaded);
		assertEquals (-1, (int) call (cache, "reserve", -1, 3), "all slots touched in the current frame");
		call (cache, "nextFrame");
		call (cache, "touch", a);
		call (cache, "touch", c);
		call (cache, "nextFrame");
		call (cache, "touch", c);
		int d = (int) call (cache, "reserve", -1, 3);
		assertEquals (b, d, "least recently touched block is evicted");
		assertEquals (1, (int) call (cache, "evictions"));
		assertEquals (-1, (int) call (cache, "find", -1, 1));
		assertEquals (a, (int) call (cache, "find", -1, 0));
		call (cache, "text", a, 3, 1, "x");
		assertEquals ("x", call (cache, "text", a, 3, 1));
		assertThrows (IndexOutOfBoundsException.class, () -> call (cache, "text", a, 4, 0));
		call (cache, "free", a);
		assertEquals (-1, (int) call (cache, "find", -1, 0));
	}

	private static Object selection () throws Exception {
		return construct ("VirtualSelectionModel", new Class<?> [0]);
	}

	private static void assertSelectionSame (boolean [] oracle, Object model) throws Exception {
		int count = 0;
		java.util.List<Integer> selected = new ArrayList<> ();
		for (int i = 0; i < oracle.length; i++) {
			assertEquals (oracle [i], call (model, "isSelected", i), "row " + i);
			if (oracle [i]) {
				count++;
				selected.add (i);
			}
		}
		assertEquals (oracle.length, (int) call (model, "logicalCount"));
		assertEquals (count, (int) call (model, "selectedCount"));
		int [] array = (int []) call (model, "toArray");
		assertEquals (selected.size (), array.length);
		for (int k = 0; k < array.length; k++) assertEquals ((int) selected.get (k), array [k]);
		int spans = 0;
		int ranges = (int) call (model, "rangeCount");
		for (int r = 0; r < ranges; r++) spans += (int) call (model, "rangeEndExclusive", r) - (int) call (model, "rangeStart", r);
		assertEquals (count, (boolean) call (model, "complementMode") ? oracle.length - spans : spans, "range view");
	}

	@Test
	public void test_hybridSelectionDifferentialAcrossRepresentationSwitches () throws Exception {
		Random random = new Random (99);
		for (int round = 0; round < 6; round++) {
			int n = 6_000 + random.nextInt (3_000);
			Object model = selection ();
			call (model, "setLogicalCount", n);
			boolean [] oracle = new boolean [n];
			boolean sawBits = false;
			for (int op = 0; op < 4_000; op++) {
				int kind = random.nextInt (100);
				if (kind < 80) {
					int i = random.nextInt (oracle.length);
					boolean v = random.nextBoolean ();
					call (model, "setSelected", i, v);
					oracle [i] = v;
				} else if (kind < 88) {
					int a = random.nextInt (oracle.length + 1), b = random.nextInt (oracle.length + 1);
					int start = Math.min (a, b), end = Math.max (a, b);
					boolean v = random.nextBoolean ();
					call (model, v ? "selectRange" : "deselectRange", start, end);
					for (int i = start; i < end; i++) oracle [i] = v;
				} else if (kind < 91) {
					call (model, "invert");
					for (int i = 0; i < oracle.length; i++) oracle [i] = !oracle [i];
				} else if (kind < 95) {
					int i = random.nextInt (oracle.length + 1), c = random.nextInt (40);
					call (model, "insert", i, c);
					boolean [] next = new boolean [oracle.length + c];
					System.arraycopy (oracle, 0, next, 0, i);
					System.arraycopy (oracle, i, next, i + c, oracle.length - i);
					oracle = next;
				} else if (kind < 99) {
					if (oracle.length == 0) continue;
					int i = random.nextInt (oracle.length), c = random.nextInt (Math.min (40, oracle.length - i) + 1);
					call (model, "remove", i, c);
					boolean [] next = new boolean [oracle.length - c];
					System.arraycopy (oracle, 0, next, 0, i);
					System.arraycopy (oracle, i + c, next, i, oracle.length - i - c);
					oracle = next;
				} else {
					call (model, random.nextBoolean () ? "selectAll" : "clear");
					boolean v = (boolean) call (model, "isSelected", 0);
					Arrays.fill (oracle, v);
				}
				sawBits |= (boolean) call (model, "bitsMode");
			}
			assertSelectionSame (oracle, model);
			assertTrue (sawBits, "scattered selections switched to the bitset form");
			assertTrue ((int) call (model, "representationSwitches") > 0);
		}
	}

	@Test
	public void test_selection10MillionRowsTimingsAndCutover () throws Exception {
		int rows = 10_000_000;
		Object model = selection ();
		call (model, "setLogicalCount", rows);
		long t0 = System.nanoTime ();
		call (model, "selectAll");
		int all = (int) call (model, "selectedCount");
		long t1 = System.nanoTime ();
		call (model, "invert");
		int none = (int) call (model, "selectedCount");
		long t2 = System.nanoTime ();
		call (model, "setSelected", 1_000, true);
		call (model, "selectRange", 1_000, 7_000_001);
		int shift = (int) call (model, "selectedCount");
		long t3 = System.nanoTime ();
		assertEquals (rows, all);
		assertEquals (0, none);
		assertEquals (7_000_001 - 1_000, shift);
		System.out.printf (Locale.ROOT, "LAZY-METRIC	selection_10M	select_all_ms	%.3f%nLAZY-METRIC	selection_10M	invert_ms	%.3f%nLAZY-METRIC	selection_10M	shift_click_ms	%.3f%n",
				(t1 - t0) / 1e6, (t2 - t1) / 1e6, (t3 - t2) / 1e6);
		int cutover = rows >>> 6;
		for (int k : new int [] {1_000, 10_000, 50_000, cutover, 300_000, 1_000_000}) {
			Object hybrid = selection ();
			call (hybrid, "setLogicalCount", rows);
			Random random = new Random (k);
			long start = System.nanoTime ();
			for (int i = 0; i < k; i++) call (hybrid, "setSelected", (int) ((long) i * rows / k) + random.nextInt (Math.max (1, rows / k - 1)) / 2, true);
			long scatter = System.nanoTime () - start;
			start = System.nanoTime ();
			call (hybrid, "invert");
			long invert = System.nanoTime () - start;
			start = System.nanoTime ();
			int [] array = (int []) call (hybrid, "toArray");
			long enumerate = System.nanoTime () - start;
			assertEquals (rows - k, array.length);
			System.out.printf (Locale.ROOT, "LAZY-METRIC	selection_cutover	k=%d	bits=%s	scatter_ms=%.1f	invert_ms=%.3f	toArray_ms=%.1f%n",
					k, call (hybrid, "bitsMode"), scatter / 1e6, invert / 1e6, enumerate / 1e6);
		}
	}

	private static int staticInt (String type, String name) throws Exception {
		Field field = Class.forName ("org.eclipse.swt.widgets." + type).getDeclaredField (name);
		field.setAccessible (true);
		return field.getInt (null);
	}
}

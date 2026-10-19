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

import java.text.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.m3.*;
import org.junit.jupiter.api.*;

/** Headless tests of {@link M3ColumnSort}: order, stability, keys, toggling, incremental updates, tree mode. */
public class Test_M3ColumnSort {

	/* a source of rows with an int, a long, a double and a text column, kept in lists so tests can edit it */
	static final class Rows {
		final List<Integer> ints = new ArrayList<> ();
		final List<Long> longs = new ArrayList<> ();
		final List<Double> doubles = new ArrayList<> ();
		final List<String> texts = new ArrayList<> ();

		Rows (int n, long seed, int distinct) {
			Random random = new Random (seed);
			for (int i = 0; i < n; i++) add (i, random, distinct);
		}

		void add (int at, Random random, int distinct) {
			ints.add (at, random.nextInt (distinct) - distinct / 2);
			longs.add (at, random.nextBoolean () ? Long.MIN_VALUE + random.nextInt (distinct) : Long.MAX_VALUE - random.nextInt (distinct));
			doubles.add (at, new double [] {Double.NaN, -0.0, 0.0, -1.5, 2.5, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY} [random.nextInt (7)]);
			texts.add (at, random.nextInt (8) == 0 ? null : "t" + random.nextInt (distinct)); //$NON-NLS-1$
		}

		void remove (int start, int count) {
			for (int i = 0; i < count; i++) {
				ints.remove (start);
				longs.remove (start);
				doubles.remove (start);
				texts.remove (start);
			}
		}

		int size () {
			return ints.size ();
		}

		M3ColumnSort sort () {
			M3ColumnSort sort = new M3ColumnSort (4, size ());
			keys (sort);
			return sort;
		}

		void keys (M3ColumnSort sort) {
			sort.setKey (0, M3ColumnKey.ofInts (ints::get));
			sort.setKey (1, M3ColumnKey.ofLongs (longs::get));
			sort.setKey (2, M3ColumnKey.ofDoubles (doubles::get));
			sort.setKey (3, M3ColumnKey.ofText (texts::get, null));
		}

		Comparator<Integer> comparator (int column) {
			switch (column) {
				case 0: return Comparator.comparing (ints::get);
				case 1: return Comparator.comparing (longs::get);
				case 2: return (a, b) -> Double.compare (doubles.get (a), doubles.get (b));
				default: return Comparator.comparing (texts::get, Comparator.nullsFirst (Comparator.naturalOrder ()));
			}
		}

		/* the reference: List.sort (stable TimSort) of the source rows by the key chain */
		int [] reference (int [] columns, int [] directions) {
			List<Integer> rows = new ArrayList<> ();
			for (int i = 0; i < size (); i++) rows.add (i);
			Comparator<Integer> chain = (a, b) -> 0;
			for (int k = 0; k < columns.length; k++) {
				Comparator<Integer> c = comparator (columns [k]);
				chain = chain.thenComparing (directions [k] == SWT.DOWN ? c.reversed () : c);
			}
			rows.sort (chain);
			return rows.stream ().mapToInt (Integer::intValue).toArray ();
		}
	}

	static int [] order (M3RowView view) {
		int [] order = new int [view.size ()];
		for (int i = 0; i < order.length; i++) order [i] = view.map (i);
		return order;
	}

	static void assertInverse (M3RowView view) {
		for (int i = 0; i < view.size (); i++) assertEquals (i, view.unmap (view.map (i)));
		int shown = 0;
		for (int s = 0; s < view.sourceSize (); s++) {
			if (view.unmap (s) >= 0) shown++;
		}
		assertEquals (view.size (), shown);
	}

	@Test
	public void test_identityWithoutKeys () {
		M3ColumnSort sort = new Rows (10, 1, 5).sort ();
		assertArrayEquals (new int [] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, order (sort));
		assertEquals (0, sort.getSortKeyCount ());
		assertEquals (4, sort.getColumnCount ());
		assertEquals (10, sort.sourceSize ());
		assertTrue (sort.toString ().contains ("10 of 10")); //$NON-NLS-1$
	}

	@Test
	public void test_singleKeysBothDirectionsSmallAndLarge () {
		for (int n : new int [] {0, 1, 2, 7, 47, 48, 200, 5000}) {
			Rows rows = new Rows (n, n, 10);
			M3ColumnSort sort = rows.sort ();
			for (int column = 0; column < 4; column++) {
				for (int direction : new int [] {SWT.UP, SWT.DOWN}) {
					sort.setSortKeys (new int [] {column}, new int [] {direction});
					assertArrayEquals (rows.reference (new int [] {column}, new int [] {direction}), order (sort), "n " + n + " column " + column); //$NON-NLS-1$ //$NON-NLS-2$
					assertInverse (sort);
				}
			}
		}
	}

	@Test
	public void test_stableTiesKeepSourceOrderInBothDirections () {
		Rows rows = new Rows (300, 3, 3);
		M3ColumnSort sort = rows.sort ();
		for (int direction : new int [] {SWT.UP, SWT.DOWN}) {
			sort.setSortKeys (new int [] {0}, new int [] {direction});
			for (int i = 1; i < sort.size (); i++) {
				int a = sort.map (i - 1), b = sort.map (i);
				if (rows.ints.get (a).equals (rows.ints.get (b))) assertTrue (a < b);
			}
		}
	}

	@Test
	public void test_multiKeyOrder () {
		Random random = new Random (11);
		for (int n : new int [] {30, 3000}) {
			Rows rows = new Rows (n, 5 + n, 6);
			M3ColumnSort sort = rows.sort ();
			for (int trial = 0; trial < 20; trial++) {
				List<Integer> columns = new ArrayList<> (List.of (0, 1, 2, 3));
				Collections.shuffle (columns, random);
				int keys = 1 + random.nextInt (4);
				int [] c = new int [keys], d = new int [keys];
				for (int k = 0; k < keys; k++) {
					c [k] = columns.get (k);
					d [k] = random.nextBoolean () ? SWT.UP : SWT.DOWN;
				}
				sort.setSortKeys (c, d);
				assertArrayEquals (rows.reference (c, d), order (sort));
				for (int k = 0; k < keys; k++) {
					assertEquals (c [k], sort.getSortColumn (k));
					assertEquals (d [k], sort.getSortDirection (k));
					assertEquals (k, sort.getPriority (c [k]));
					assertEquals (d [k], sort.getDirection (c [k]));
				}
			}
		}
	}

	@Test
	public void test_keyValueEdgeCases () {
		double [] doubles = {Double.NaN, 1, -0.0, 0.0, Double.NEGATIVE_INFINITY, -1, Double.POSITIVE_INFINITY, Double.MIN_VALUE};
		long [] longs = {Long.MAX_VALUE, Long.MIN_VALUE, 0, -1, 1, Long.MIN_VALUE + 1, Long.MAX_VALUE - 1, 0};
		int [] ints = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, -1, 1, 5, -5, 0};
		M3ColumnSort sort = new M3ColumnSort (3, 8);
		sort.setKey (0, M3ColumnKey.ofDoubles (i -> doubles [i]));
		sort.setKey (1, M3ColumnKey.ofLongs (i -> longs [i]));
		sort.setKey (2, M3ColumnKey.ofInts (i -> ints [i]));
		{
			sort.setSortKeys (new int [] {0}, new int [] {SWT.UP});
			assertArrayEquals (new int [] {4, 5, 2, 3, 7, 1, 6, 0}, order (sort));
			sort.setSortKeys (new int [] {1}, new int [] {SWT.UP});
			assertArrayEquals (new int [] {1, 5, 3, 2, 7, 4, 6, 0}, order (sort));
			sort.setSortKeys (new int [] {2}, new int [] {SWT.DOWN});
			assertArrayEquals (new int [] {0, 5, 4, 2, 7, 3, 6, 1}, order (sort));
			assertEquals (8, sort.size ());
		}
	}

	@Test
	public void test_largeKeyValuesTakeTheRadixPath () {
		int n = 4096;
		long [] longs = new long [n];
		Random random = new Random (7);
		for (int i = 0; i < n; i++) longs [i] = random.nextLong ();
		M3ColumnSort sort = new M3ColumnSort (2, n);
		sort.setKey (0, M3ColumnKey.ofLongs (i -> longs [i]));
		sort.setKey (1, M3ColumnKey.ofDoubles (i -> longs [i] / 3.0));
		for (int column = 0; column < 2; column++) {
			for (int direction : new int [] {SWT.UP, SWT.DOWN}) {
				sort.setSortKeys (new int [] {column}, new int [] {direction});
				for (int i = 1; i < n; i++) {
					int c = Long.compare (longs [sort.map (i - 1)], longs [sort.map (i)]);
					assertTrue (direction == SWT.UP ? c <= 0 : c >= 0);
				}
			}
		}
	}

	@Test
	public void test_textKeysWithCollatorAndRowComparator () {
		String [] words = {"b", "A", "a", null, "B", "c", null, "C"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		Collator collator = Collator.getInstance (Locale.ENGLISH);
		collator.setStrength (Collator.PRIMARY);
		M3ColumnSort sort = new M3ColumnSort (2, words.length);
		sort.setKey (0, M3ColumnKey.ofText (i -> words [i], collator));
		sort.setKey (1, M3ColumnKey.ofRows ((a, b) -> Integer.compare (b % 3, a % 3)));
		sort.setSortKeys (new int [] {0}, new int [] {SWT.UP});
		assertArrayEquals (new int [] {3, 6, 1, 2, 0, 4, 5, 7}, order (sort));
		sort.setSortKeys (new int [] {1, 0}, new int [] {SWT.UP, SWT.DOWN});
		List<Integer> reference = new ArrayList<> (List.of (0, 1, 2, 3, 4, 5, 6, 7));
		Comparator<Integer> text = Comparator.comparing (i -> words [i], Comparator.nullsFirst (collator));
		reference.sort (Comparator.<Integer> comparingInt (i -> -(i % 3)).thenComparing (text.reversed ()));
		assertArrayEquals (reference.stream ().mapToInt (Integer::intValue).toArray (), order (sort));
		assertSame (sort.getKey (0), sort.getKey (0));
		assertTrue (sort.getKey (1).compare (2, 1) < 0);
	}

	@Test
	public void test_cellTextDefaultKey () {
		String [][] cells = {{"b", "1"}, {"a", "2"}, {"c", "0"}}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		M3ColumnSort sort = new M3ColumnSort (2, 3);
		assertThrows (IllegalArgumentException.class, () -> sort.toggle (0, false));
		sort.setCellText ((row, column) -> cells [row][column]);
		sort.toggle (0, false);
		assertArrayEquals (new int [] {1, 0, 2}, order (sort));
		sort.setSortKeys (new int [] {1}, new int [] {SWT.DOWN});
		assertArrayEquals (new int [] {1, 0, 2}, order (sort));
		sort.setCellText ((row, column) -> cells [2 - row][column]);
		sort.setSortKeys (new int [] {0}, new int [] {SWT.UP});
		assertArrayEquals (new int [] {1, 2, 0}, order (sort));
		assertNull (sort.getKey (0));
	}

	@Test
	public void test_toggleCycleAndAccumulate () {
		Rows rows = new Rows (50, 9, 4);
		M3ColumnSort sort = rows.sort ();
		sort.toggle (0, false);
		assertEquals (SWT.UP, sort.getDirection (0));
		sort.toggle (0, false);
		assertEquals (SWT.DOWN, sort.getDirection (0));
		sort.toggle (0, false);
		assertEquals (SWT.NONE, sort.getDirection (0));
		assertEquals (0, sort.getSortKeyCount ());
		sort.toggle (0, false);
		sort.toggle (1, true);
		sort.toggle (3, true);
		assertEquals (3, sort.getSortKeyCount ());
		assertEquals (2, sort.getPriority (3));
		sort.toggle (1, true);
		assertEquals (SWT.DOWN, sort.getDirection (1));
		assertEquals (1, sort.getPriority (1));
		sort.toggle (1, true);
		assertEquals (-1, sort.getPriority (1));
		assertEquals (1, sort.getPriority (3));
		assertArrayEquals (rows.reference (new int [] {0, 3}, new int [] {SWT.UP, SWT.UP}), order (sort));
		sort.toggle (3, false);
		assertEquals (1, sort.getSortKeyCount ());
		assertEquals (SWT.DOWN, sort.getDirection (3));
		sort.clear ();
		assertEquals (0, sort.getSortKeyCount ());
		assertArrayEquals (rows.reference (new int [0], new int [0]), order (sort));
	}

	@Test
	public void test_argumentChecks () {
		M3ColumnSort sort = new Rows (5, 2, 3).sort ();
		assertThrows (IllegalArgumentException.class, () -> new M3ColumnSort (-1, 1));
		assertThrows (IllegalArgumentException.class, () -> new M3ColumnSort (1, -1));
		assertThrows (IllegalArgumentException.class, () -> new M3ColumnSort (1, (M3RowView) null));
		assertThrows (IllegalArgumentException.class, () -> sort.setSortKeys (null, new int [0]));
		assertThrows (IllegalArgumentException.class, () -> sort.setSortKeys (new int [0], null));
		assertThrows (IllegalArgumentException.class, () -> sort.setSortKeys (new int [] {0}, new int [0]));
		assertThrows (IllegalArgumentException.class, () -> sort.setSortKeys (new int [] {4}, new int [] {SWT.UP}));
		assertThrows (IllegalArgumentException.class, () -> sort.setSortKeys (new int [] {-1}, new int [] {SWT.UP}));
		assertThrows (IllegalArgumentException.class, () -> sort.setSortKeys (new int [] {0}, new int [] {SWT.NONE}));
		assertThrows (IllegalArgumentException.class, () -> sort.setSortKeys (new int [] {0, 0}, new int [] {SWT.UP, SWT.UP}));
		assertThrows (IllegalArgumentException.class, () -> sort.setKey (4, null));
		assertThrows (IllegalArgumentException.class, () -> sort.getKey (-1));
		assertThrows (IllegalArgumentException.class, () -> sort.toggle (9, false));
		assertThrows (IllegalArgumentException.class, () -> sort.getSortColumn (0));
		assertThrows (IllegalArgumentException.class, () -> sort.getSortDirection (0));
		assertThrows (IllegalArgumentException.class, () -> sort.map (5));
		assertThrows (IllegalArgumentException.class, () -> sort.map (-1));
		assertThrows (IllegalArgumentException.class, () -> sort.unmap (5));
		assertThrows (IllegalArgumentException.class, () -> M3ColumnKey.ofInts (null));
		assertThrows (IllegalArgumentException.class, () -> M3ColumnKey.ofLongs (null));
		assertThrows (IllegalArgumentException.class, () -> M3ColumnKey.ofDoubles (null));
		assertThrows (IllegalArgumentException.class, () -> M3ColumnKey.ofText (null, null));
		assertThrows (IllegalArgumentException.class, () -> M3ColumnKey.ofRows (null));
		assertThrows (IllegalArgumentException.class, () -> sort.install ((org.eclipse.swt.widgets.Table) null));
		assertThrows (IllegalArgumentException.class, () -> sort.install ((org.eclipse.swt.widgets.Tree) null));
		assertEquals (-1, sort.getPriority (9));
		assertEquals (0, sort.compare (3, 3));
		assertTrue (sort.compare (1, 3) < 0);
	}

	@Test
	public void test_sourceEditsStayEqualToAFullSort () {
		Random random = new Random (21);
		for (int n : new int [] {0, 5, 40, 400, 3000}) {
			Rows rows = new Rows (n, n + 1, 7);
			M3ColumnSort sort = rows.sort ();
			int [] c = {3, 0, 2}, d = {SWT.DOWN, SWT.UP, SWT.DOWN};
			sort.setSortKeys (c, d);
			for (int step = 0; step < 60; step++) {
				edit (rows, sort, random);
				assertEquals (rows.size (), sort.sourceSize ());
				assertArrayEquals (rows.reference (c, d), order (sort), "n " + n + " step " + step); //$NON-NLS-1$ //$NON-NLS-2$
				assertInverse (sort);
			}
		}
	}

	static void edit (Rows rows, M3RowView view, Random random) {
		int n = rows.size ();
		switch (random.nextInt (5)) {
			case 0: {
				int start = random.nextInt (n + 1), count = random.nextInt (random.nextInt (8) == 0 ? n + 50 : 4);
				for (int i = 0; i < count; i++) rows.add (start + i, random, 7);
				view.rowsInserted (start, count);
				break;
			}
			case 1: {
				int start = random.nextInt (n + 1), count = random.nextInt (Math.min (4, n - start) + 1);
				rows.remove (start, count);
				view.rowsRemoved (start, count);
				break;
			}
			case 2: {
				if (n == 0) break;
				int row = random.nextInt (n);
				rows.remove (row, 1);
				rows.add (row, random, 7);
				view.rowsChanged (row);
				break;
			}
			case 3: {
				int count = random.nextInt (Math.max (1, n / 4) + 1);
				int [] changed = new int [n == 0 ? 0 : count];
				for (int i = 0; i < changed.length; i++) {
					changed [i] = random.nextInt (n);
					rows.remove (changed [i], 1);
					rows.add (changed [i], random, 7);
				}
				view.rowsChanged (changed);
				break;
			}
			default: {
				int start = random.nextInt (n + 1);
				for (int i = 0; i < 3; i++) rows.add (start, random, 7);
				view.setSourceSize (rows.size ());
				break;
			}
		}
	}

	@Test
	public void test_singleMoveDamage () {
		int [] values = {10, 20, 30, 40, 50};
		M3ColumnSort sort = new M3ColumnSort (1, 5);
		sort.setKey (0, M3ColumnKey.ofInts (i -> values [i]));
		sort.setSortKeys (new int [] {0}, new int [] {SWT.UP});
		List<String> log = new ArrayList<> ();
		M3RowView.Listener listener = change -> log.add (change.kind () + ":" + change.firstDamaged () + ".." + change.lastDamaged () + ":" + change.rowCount ()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sort.addListener (listener);
		sort.addListener (listener);
		values [4] = 15;
		sort.rowsChanged (4);
		assertArrayEquals (new int [] {0, 4, 1, 2, 3}, order (sort));
		values [0] = 45;
		sort.rowsChanged (0, 0);
		assertArrayEquals (new int [] {4, 1, 2, 3, 0}, order (sort));
		values [2] = 31;
		sort.rowsChanged (2);
		assertEquals (List.of ("6:1..4:1", "6:0..4:1", "6:2..2:1"), log); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sort.removeListener (listener);
		sort.removeListener (listener);
		sort.rowsChanged (2);
		assertEquals (3, log.size ());
		assertThrows (IllegalArgumentException.class, () -> sort.addListener (null));
		assertThrows (IllegalArgumentException.class, () -> sort.removeListener (null));
		assertThrows (IllegalArgumentException.class, () -> sort.rowsChanged ((int []) null));
		assertThrows (IllegalArgumentException.class, () -> sort.rowsChanged (5));
		assertThrows (IllegalArgumentException.class, () -> sort.rowsInserted (6, 1));
		assertThrows (IllegalArgumentException.class, () -> sort.rowsInserted (0, -1));
		assertThrows (IllegalArgumentException.class, () -> sort.rowsRemoved (4, 2));
		assertThrows (IllegalArgumentException.class, () -> sort.setSourceSize (-1));
		sort.dispose ();
	}

	@Test
	public void test_treeModeSortsSiblings () {
		/* 0 root(b) [2(z), 3(a)], 1 root(a) [4(m) [5(c), 6(b)]] */
		int [] parents = {-1, -1, 0, 0, 1, 4, 4};
		String [] names = {"b", "a", "z", "a", "m", "c", "b"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		M3ColumnSort sort = new M3ColumnSort (1, 7);
		assertThrows (IllegalArgumentException.class, () -> sort.childCount (-1));
		sort.setParents (i -> parents [i]);
		sort.setKey (0, M3ColumnKey.ofText (i -> names [i], null));
		sort.setSortKeys (new int [] {0}, new int [] {SWT.UP});
		assertEquals (2, sort.childCount (-1));
		assertEquals (1, sort.child (-1, 0));
		assertEquals (0, sort.child (-1, 1));
		assertEquals (3, sort.child (0, 0));
		assertEquals (2, sort.child (0, 1));
		assertEquals (6, sort.child (4, 0));
		assertEquals (0, sort.childCount (6));
		assertEquals (1, sort.indexOfChild (2));
		assertEquals (0, sort.indexOfChild (1));
		sort.toggle (0, false);
		assertEquals (0, sort.child (-1, 0));
		assertEquals (2, sort.child (0, 0));
		assertThrows (IllegalArgumentException.class, () -> sort.child (0, 2));
		assertThrows (IllegalArgumentException.class, () -> sort.childCount (-2));
		assertThrows (IllegalArgumentException.class, () -> sort.childCount (7));
		assertThrows (IllegalArgumentException.class, () -> sort.indexOfChild (7));
		parents [6] = 9;
		sort.rowsChanged (6);
		assertThrows (IllegalArgumentException.class, () -> sort.childCount (-1));
		parents [6] = 4;
		sort.rowsChanged (6);
		sort.setParents (null);
		assertThrows (IllegalArgumentException.class, () -> sort.child (-1, 0));
	}
}

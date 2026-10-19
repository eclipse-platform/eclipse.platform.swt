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

import java.util.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.m3.*;
import org.junit.jupiter.api.*;

/** Headless tests of {@link M3ColumnFilter}, {@link M3RowWindow} and the filter-sort-window chain. */
@SuppressWarnings ("nls")
public class Test_M3ColumnFilter {

	static final String [][] CELLS = {
		{"apple", "red", "1"},
		{"banana", "yellow", "2"},
		{"cherry", "red", "3"},
		{"date", "brown", "4"},
		{"elder", "black", "5"},
		{"fig", "purple", null},
	};

	static M3ColumnFilter fruits () {
		return new M3ColumnFilter (3, CELLS.length, (row, column) -> CELLS [row][column]);
	}

	static int [] rows (M3RowView view) {
		return Test_M3ColumnSort.order (view);
	}

	static M3CellText text (Test_M3ColumnSort.Rows rows) {
		return (row, column) -> switch (column) {
			case 0 -> String.valueOf (rows.ints.get (row));
			case 1 -> String.valueOf (rows.longs.get (row));
			case 2 -> String.valueOf (rows.doubles.get (row));
			default -> rows.texts.get (row);
		};
	}

	@Test
	public void test_textCriteriaPerColumn () {
		M3ColumnFilter filter = fruits ();
		assertArrayEquals (new int [] {0, 1, 2, 3, 4, 5}, rows (filter));
		filter.setText (1, "red", M3TextMatch.EXACT);
		assertArrayEquals (new int [] {0, 2}, rows (filter));
		filter.setText (0, "c*", M3TextMatch.WILDCARD);
		assertArrayEquals (new int [] {2}, rows (filter));
		filter.setText (0, "a.*,b.*", M3TextMatch.REGEX);
		assertArrayEquals (new int [] {0}, rows (filter));
		filter.setText (0, "A", M3TextMatch.CONTAINS | M3TextMatch.IGNORE_CASE | M3TextMatch.NEGATE);
		assertArrayEquals (new int [] {2}, rows (filter));
		assertTrue (filter.isActive (0));
		assertEquals ("A", filter.getMatch (0).pattern ());
		filter.setText (0, null, M3TextMatch.CONTAINS);
		assertFalse (filter.isActive (0));
		assertNull (filter.getMatch (0));
		filter.setText (0, ",", M3TextMatch.CONTAINS);
		assertFalse (filter.isActive (0));
		assertArrayEquals (new int [] {0, 2}, rows (filter));
		filter.setText (2, "", M3TextMatch.EXACT);
		assertArrayEquals (new int [] {0, 2}, rows (filter));
		filter.clear (1);
		assertEquals (6, filter.size ());
		assertEquals (3, filter.getColumnCount ());
		assertTrue (filter.toString ().contains ("6 of 6"));
	}

	@Test
	public void test_multiColumnCriterionAndLogic () {
		M3ColumnFilter filter = fruits ();
		filter.setText (new int [] {0, 1}, "r", M3TextMatch.CONTAINS);
		assertArrayEquals (new int [] {0, 2, 3, 4, 5}, rows (filter));
		assertArrayEquals (new int [] {0, 1}, filter.getColumns ());
		assertTrue (filter.isActive (M3ColumnFilter.ALL_COLUMNS));
		filter.setText (new int [] {0, 1}, "r", M3TextMatch.CONTAINS | M3TextMatch.NEGATE);
		assertArrayEquals (new int [] {1}, rows (filter));
		filter.setText (new int [] {0, 1}, "e", M3TextMatch.CONTAINS);
		filter.setText (1, "red", M3TextMatch.EXACT);
		assertArrayEquals (new int [] {0, 2}, rows (filter));
		filter.setLogic (1, M3ColumnFilter.OR);
		assertEquals (M3ColumnFilter.OR, filter.getLogic (1));
		assertArrayEquals (new int [] {0, 1, 2, 3, 4, 5}, rows (filter));
		filter.setText (2, "5", M3TextMatch.EXACT);
		assertArrayEquals (new int [] {4}, rows (filter));
		filter.setLogic (M3ColumnFilter.ALL_COLUMNS, M3ColumnFilter.OR);
		assertArrayEquals (new int [] {4}, rows (filter));
		filter.clear (M3ColumnFilter.ALL_COLUMNS);
		assertArrayEquals (new int [0], filter.getColumns ());
		filter.setLogic (2, M3ColumnFilter.OR);
		assertArrayEquals (new int [] {0, 2, 4}, rows (filter));
		filter.clearAll ();
		assertEquals (6, filter.size ());
	}

	@Test
	public void test_valueLists () {
		M3ColumnFilter filter = fruits ();
		filter.setValues (1, "red", "black");
		assertArrayEquals (new int [] {0, 2, 4}, rows (filter));
		filter.setValues (2, "");
		assertArrayEquals (new int [0], rows (filter));
		filter.setValues (2, (String []) null);
		filter.setValues (1, (String []) null);
		assertFalse (filter.isActive (1));
		int [] ids = {3, -1, 70, 0, 3, 64};
		filter.setValues (0, row -> ids [row], 3, 64, 0);
		assertArrayEquals (new int [] {0, 3, 4, 5}, rows (filter));
		filter.setValues (0, row -> ids [row]);
		assertArrayEquals (new int [0], rows (filter));
		assertThrows (IllegalArgumentException.class, () -> filter.setValues (0, row -> 0, -1));
		assertThrows (IllegalArgumentException.class, () -> filter.setValues (0, null, 1));
		assertThrows (IllegalArgumentException.class, () -> filter.setValues (0, row -> 0, (int []) null));
		M3ColumnFilter idsOnly = new M3ColumnFilter (1, 6, null);
		idsOnly.setValues (0, row -> ids [row], 70);
		assertArrayEquals (new int [] {2}, rows (idsOnly));
		assertThrows (IllegalArgumentException.class, () -> idsOnly.setValues (0, "x"));
		assertThrows (IllegalArgumentException.class, () -> idsOnly.setText (0, "x", M3TextMatch.CONTAINS));
		idsOnly.setText (0, null, M3TextMatch.CONTAINS);
		idsOnly.setValues (0, (String []) null);
		assertEquals (6, idsOnly.size ());
	}

	@Test
	public void test_argumentChecks () {
		M3ColumnFilter filter = fruits ();
		assertThrows (IllegalArgumentException.class, () -> new M3ColumnFilter (-1, 0, null));
		assertThrows (IllegalArgumentException.class, () -> new M3ColumnFilter (1, -1, null));
		assertThrows (IllegalArgumentException.class, () -> filter.setText (3, "x", 0));
		assertThrows (IllegalArgumentException.class, () -> filter.setText (-1, "x", 0));
		assertThrows (IllegalArgumentException.class, () -> filter.setText ((int []) null, "x", 0));
		assertThrows (IllegalArgumentException.class, () -> filter.setText (new int [] {3}, "x", 0));
		assertThrows (IllegalArgumentException.class, () -> filter.setText (0, "(", M3TextMatch.REGEX));
		assertThrows (IllegalArgumentException.class, () -> filter.setValues (3, "x"));
		assertThrows (IllegalArgumentException.class, () -> filter.clear (-2));
		assertThrows (IllegalArgumentException.class, () -> filter.clear (3));
		assertThrows (IllegalArgumentException.class, () -> filter.setLogic (0, 2));
		assertThrows (IllegalArgumentException.class, () -> filter.setLogic (0, -1));
		assertThrows (IllegalArgumentException.class, () -> filter.getLogic (4));
		assertThrows (IllegalArgumentException.class, () -> filter.isActive (4));
		assertThrows (IllegalArgumentException.class, () -> filter.getMatch (4));
		assertThrows (IllegalArgumentException.class, () -> filter.matches (6));
		assertThrows (IllegalArgumentException.class, () -> filter.map (6));
		assertThrows (IllegalArgumentException.class, () -> filter.unmap (-1));
		filter.setText (0, "a", M3TextMatch.CONTAINS);
		assertEquals (-1, filter.unmap (2));
		assertEquals (1, filter.unmap (1));
		assertTrue (filter.matches (3));
		assertFalse (filter.matches (5));
	}

	@Test
	public void test_treeKeepsAncestorsOfMatches () {
		/* 0 [1 [2, 3], 4], 5 [6], 7 */
		int [] parents = {-1, 0, 1, 1, 0, -1, 5, -1};
		String [] names = {"root", "dir", "match", "other", "file", "top", "x", "lone"};
		M3ColumnFilter filter = new M3ColumnFilter (1, 8, (row, column) -> names [row]);
		filter.setParents (row -> parents [row]);
		filter.setText (0, "match", M3TextMatch.EXACT);
		assertArrayEquals (new int [] {0, 1, 2}, rows (filter));
		assertTrue (filter.matches (2));
		assertFalse (filter.matches (1));
		filter.setText (0, "match,x", M3TextMatch.EXACT);
		assertArrayEquals (new int [] {0, 1, 2, 5, 6}, rows (filter));
		filter.setText (0, "dir", M3TextMatch.EXACT);
		assertArrayEquals (new int [] {0, 1}, rows (filter));
		filter.setParents (null);
		assertArrayEquals (new int [] {1}, rows (filter));
		parents [3] = 8;
		filter.setText (0, "other", M3TextMatch.EXACT);
		assertThrows (IllegalArgumentException.class, () -> filter.setParents (row -> parents [row]));
		parents [3] = -2;
		assertThrows (IllegalArgumentException.class, () -> filter.setParents (row -> parents [row]));
	}

	@Test
	public void test_treeChainWithSortedSiblings () {
		int [] parents = {-1, 0, 1, 1, 0, -1, 5, -1};
		String [] names = {"root", "dir", "match", "amatch", "file", "top", "x", "lone"};
		M3ColumnFilter filter = new M3ColumnFilter (1, 8, (row, column) -> names [row]);
		filter.setParents (row -> parents [row]);
		M3ColumnSort sort = new M3ColumnSort (1, filter);
		sort.setParents (row -> parents [row]);
		sort.setCellText ((row, column) -> names [row]);
		sort.toggle (0, false);
		filter.setText (0, "*match", M3TextMatch.WILDCARD);
		assertEquals (1, sort.childCount (-1));
		assertEquals (0, sort.child (-1, 0));
		assertEquals (1, sort.childCount (0));
		assertEquals (3, sort.child (1, 0));
		assertEquals (2, sort.child (1, 1));
		names [6] = "xmatch";
		sort.rowsChanged (6);
		assertEquals (2, sort.childCount (-1));
		assertEquals (5, sort.child (-1, 1));
		assertEquals (6, sort.child (5, 0));
		names [2] = "gone";
		names [3] = "gone";
		filter.rowsChanged (2, 3);
		assertEquals (1, sort.childCount (-1));
		assertEquals (-1, sort.unmap (0));
		assertEquals (-1, sort.indexOfChild (1));
	}

	@Test
	public void test_changeEventsAndDamage () {
		List<String []> cells = new ArrayList<> ();
		for (String [] row : CELLS) cells.add (row.clone ());
		M3ColumnFilter filter = new M3ColumnFilter (3, cells.size (), (row, column) -> cells.get (row) [column]);
		List<String> log = new ArrayList<> ();
		filter.addListener (change -> {
			StringBuilder rows = new StringBuilder ();
			for (int i = 0; i < change.rowCount (); i++) rows.append (change.row (i));
			log.add (change.kind () + ":" + rows + ":" + change.firstDamaged () + ".." + change.lastDamaged ());
			assertSame (filter, change.view ());
		});
		filter.setText (1, "red,yellow", M3TextMatch.EXACT);
		filter.setText (1, "red,brown", M3TextMatch.EXACT);
		assertEquals (List.of ("5:345:-1..-1", "5:1:1..2", "4:3:2..2"), log);
		log.clear ();
		cells.get (3) [1] = "red";
		filter.rowsChanged (3, 0);
		assertEquals (List.of ("6:03:0..2"), log);
		log.clear ();
		cells.add (1, new String [] {"kiwi", "green", "9"});
		cells.add (2, new String [] {"lime", "red", "7"});
		filter.rowsInserted (1, 2);
		cells.remove (1);
		cells.remove (1);
		filter.rowsRemoved (1, 2);
		filter.setSourceSize (6);
		cells.get (3) [1] = "brown";
		filter.rowsChanged (3);
		filter.rowsChanged (1);
		assertEquals (List.of ("2:2:1..3", "3::1..2", "1::0..2", "6:3:2..2"), log);
	}

	@Test
	public void test_incrementalEditsEqualAReference () {
		Random random = new Random (31);
		for (int n : new int [] {0, 3, 63, 64, 65, 200, 1500}) {
			Test_M3ColumnSort.Rows rows = new Test_M3ColumnSort.Rows (n, n + 3, 7);
			M3CellText text = text (rows);
			M3ColumnFilter filter = new M3ColumnFilter (4, n, text);
			M3ColumnSort sort = new M3ColumnSort (4, filter);
			rows.keys (sort);
			M3RowWindow window = new M3RowWindow (sort);
			window.setWindow (2, 10);
			int [] c = {3, 0}, d = {SWT.UP, SWT.DOWN};
			sort.setSortKeys (c, d);
			for (int step = 0; step < 80; step++) {
				if (random.nextInt (4) == 0) criterion (filter, random); else Test_M3ColumnSort.edit (rows, window, random);
				int [] expected = reference (rows, filter, text, c, d);
				assertArrayEquals (expected, rows (sort), "n " + n + " step " + step);
				Test_M3ColumnSort.assertInverse (filter);
				Test_M3ColumnSort.assertInverse (sort);
				assertEquals (Math.max (0, Math.min (10, expected.length - 2)), window.size ());
				for (int i = 0; i < window.size (); i++) assertEquals (expected [i + 2], window.map (i));
			}
		}
	}

	static void criterion (M3ColumnFilter filter, Random random) {
		int column = random.nextInt (5) - 1;
		switch (random.nextInt (5)) {
			case 0: filter.clear (column); break;
			case 1: {
				String pattern = random.nextBoolean () ? "t1*,t2*" : "*1*";
				if (column < 0) filter.setText (new int [] {0, 3}, pattern, M3TextMatch.WILDCARD); else filter.setText (column, pattern, M3TextMatch.WILDCARD);
				break;
			}
			case 2: if (column >= 0) filter.setText (column, "-", M3TextMatch.CONTAINS | M3TextMatch.NEGATE); break;
			case 3: filter.setLogic (column, random.nextInt (2)); break;
			default: if (column >= 0) filter.setValues (column, "0", "1", "t3", "NaN"); break;
		}
	}

	/* the reference: test each row against a plain predicate fold, then List.sort by the key chain */
	static int [] reference (Test_M3ColumnSort.Rows rows, M3ColumnFilter filter, M3CellText text, int [] c, int [] d) {
		List<Integer> kept = new ArrayList<> ();
		for (int row = 0; row < rows.size (); row++) {
			if (passes (filter, text, row)) kept.add (row);
		}
		Comparator<Integer> chain = (a, b) -> 0;
		for (int k = 0; k < c.length; k++) {
			Comparator<Integer> comparator = rows.comparator (c [k]);
			chain = chain.thenComparing (d [k] == SWT.DOWN ? comparator.reversed () : comparator);
		}
		kept.sort (chain);
		return kept.stream ().mapToInt (Integer::intValue).toArray ();
	}

	static boolean passes (M3ColumnFilter filter, M3CellText text, int row) {
		Boolean result = null;
		for (int column = -1; column < filter.getColumnCount (); column++) {
			if (!filter.isActive (column)) continue;
			boolean value = test (filter, text, column, row);
			result = result == null ? value : filter.getLogic (column) == M3ColumnFilter.AND ? result && value : result || value;
		}
		return result == null || result;
	}

	static boolean test (M3ColumnFilter filter, M3CellText text, int column, int row) {
		M3TextMatch match = filter.getMatch (column);
		if (match == null) {
			String value = text.text (row, column);
			return Set.of ("0", "1", "t3", "NaN").contains (value == null ? "" : value);
		}
		int [] columns = column < 0 ? filter.getColumns () : new int [] {column};
		IntPredicate positive = c -> M3TextMatch.compile (match.pattern (), match.mode () & ~M3TextMatch.NEGATE).matches (text.text (row, c));
		boolean any = Arrays.stream (columns).anyMatch (positive);
		return any != ((match.mode () & M3TextMatch.NEGATE) != 0);
	}

	@Test
	public void test_windowClipsDamage () {
		M3ColumnSort sort = new M3ColumnSort (1, 100);
		sort.setKey (0, M3ColumnKey.ofInts (i -> -i));
		M3RowWindow window = new M3RowWindow (sort);
		List<String> log = new ArrayList<> ();
		window.addListener (change -> log.add (change.kind () + ":" + change.firstDamaged () + ".." + change.lastDamaged ()));
		window.setWindow (10, 20);
		assertEquals (10, window.getFirst ());
		assertEquals (20, window.getCount ());
		assertEquals (20, window.size ());
		assertEquals (10, window.map (0));
		assertEquals (5, window.unmap (15));
		assertEquals (-1, window.unmap (5));
		assertEquals (-1, window.unmap (30));
		sort.rowsChanged (50);
		sort.rowsChanged (15);
		sort.rowsRemoved (0, 95);
		assertEquals (0, window.size ());
		sort.setSourceSize (12);
		assertEquals (2, window.size ());
		assertEquals (List.of ("1:0..19", "6:-1..-1", "6:5..5", "3:-1..-1", "1:0..1"), log);
		assertThrows (IllegalArgumentException.class, () -> window.setWindow (-1, 1));
		assertThrows (IllegalArgumentException.class, () -> window.setWindow (1, -1));
		assertThrows (IllegalArgumentException.class, () -> window.map (2));
		assertThrows (IllegalArgumentException.class, () -> new M3RowWindow (null));
		assertTrue (window.toString ().contains ("2 rows"));
		window.dispose ();
		sort.setSourceSize (3);
		assertEquals (5, log.size ());
		assertEquals (M3RowChange.ORDER, 7);
		M3RowChange [] seen = new M3RowChange [1];
		sort.addListener (change -> seen [0] = change);
		sort.toggle (0, true);
		assertTrue (seen [0].toString ().contains ("ORDER"));
		assertThrows (IllegalArgumentException.class, () -> seen [0].row (0));
		assertEquals (0, seen [0].start ());
		assertEquals (0, seen [0].count ());
	}
}

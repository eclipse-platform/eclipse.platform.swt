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

import java.util.Comparator;
import java.util.Locale;
import java.util.Random;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.m3.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3ProxyModel (enhanced M3 API, leaf 14). Oracle: the same stages as a stream over
 * boxed source rows (filter, then a stable sort). The widget bindings run on the test display.
 */
public class Test_M3ProxyModel {

	/* the oracle: stages applied to boxed rows in order */
	private static int [] oracle (int n, Object... stages) {
		java.util.List<Integer> rows = IntStream.range (0, n).boxed ().collect (Collectors.toList ());
		for (Object stage : stages) {
			if (stage instanceof java.util.function.IntPredicate keep) {
				rows = rows.stream ().filter (keep::test).collect (Collectors.toList ());
			} else {
				M3ProxyModel.RowComparator comparator = (M3ProxyModel.RowComparator) stage;
				rows = rows.stream ().sorted ((Comparator<Integer>) (a, b) -> comparator.compare (a, b)).collect (Collectors.toList ());
			}
		}
		return rows.stream ().mapToInt (Integer::intValue).toArray ();
	}

	private static int [] view (M3ProxyModel model) {
		return IntStream.range (0, model.size ()).map (model::map).toArray ();
	}

	@Test
	public void test_identityModel () {
		M3ProxyModel model = new M3ProxyModel (5);
		assertEquals (5, model.size ());
		assertEquals (5, model.sourceSize ());
		assertArrayEquals (new int [] {0, 1, 2, 3, 4}, view (model));
		for (int s = 0; s < 5; s++) assertEquals (s, model.unmap (s));
		assertEquals ("M3ProxyModel [5 of 5 rows, 0 stages]", model.toString ());
	}

	@Test
	public void test_chainsEqualTheirComposition () {
		Random random = new Random (21);
		for (int round = 0; round < 100; round++) {
			int n = random.nextInt (300);
			int [] keys = random.ints (n, 0, 20).toArray ();
			int modulus = 2 + random.nextInt (4);
			java.util.function.IntPredicate even = s -> keys [s] % modulus != 0;
			M3ProxyModel.RowComparator byKey = (a, b) -> Integer.compare (keys [a], keys [b]);
			M3ProxyModel.RowComparator byKeyDescending = (a, b) -> Integer.compare (keys [b] / 3, keys [a] / 3);
			M3ProxyModel model = new M3ProxyModel (n).addFilter (even).addSort (byKey).addSort (byKeyDescending).refresh ();
			int [] expected = oracle (n, even, byKey, byKeyDescending);
			assertArrayEquals (expected, view (model), "round " + round);
			assertEquals (expected.length, model.size ());
			for (int r = 0; r < model.size (); r++) assertEquals (r, model.unmap (model.map (r)));
			for (int s = 0; s < n; s++) {
				int r = model.unmap (s);
				if (even.test (s)) assertEquals (s, model.map (r)); else assertEquals (-1, r);
			}
		}
	}

	@Test
	public void test_editsTakeEffectAtRefresh () {
		M3ProxyModel model = new M3ProxyModel (10).addFilter (s -> s % 2 == 0).refresh ();
		assertEquals (5, model.size ());
		model.setSourceSize (20);
		assertEquals (5, model.size ());
		model.refresh ();
		assertEquals (10, model.size ());
		model.clearStages ().refresh ();
		assertEquals (20, model.size ());
		model.setSourceSize (0);
		model.refresh ();
		assertEquals (0, model.size ());
	}

	@Test
	public void test_sortByCollationKeys () {
		String [] names = {"Zoë", "anna", "Åsa", "Ärla", "bob", "Anna"};
		M3SortKeys keys = M3Collation.keys (names, Locale.forLanguageTag ("sv"));
		M3ProxyModel model = new M3ProxyModel (names.length).addSort (keys).refresh ();
		assertArrayEquals (keys.sortedOrder (), view (model));
	}

	@Test
	public void test_argumentsAreChecked () {
		assertThrows (IllegalArgumentException.class, () -> new M3ProxyModel (-1));
		M3ProxyModel model = new M3ProxyModel (3);
		assertThrows (IllegalArgumentException.class, () -> model.setSourceSize (-1));
		assertThrows (IllegalArgumentException.class, () -> model.addFilter (null));
		assertThrows (IllegalArgumentException.class, () -> model.addSort ((M3ProxyModel.RowComparator) null));
		assertThrows (IllegalArgumentException.class, () -> model.addSort ((M3SortKeys) null));
		assertThrows (IllegalArgumentException.class, () -> model.map (3));
		assertThrows (IllegalArgumentException.class, () -> model.map (-1));
		assertThrows (IllegalArgumentException.class, () -> model.unmap (3));
		assertThrows (IllegalArgumentException.class, () -> model.unmap (-1));
	}

	/* ------------------------------------------------------------------ widget bindings */

	@Test
	public void test_virtualTableAsksTheSourceRows () {
		Display display = Display.getDefault ();
		Shell shell = new Shell (display);
		try {
			shell.setBounds (20, 20, 320, 420);
			Table table = new Table (shell, SWT.VIRTUAL | SWT.BORDER);
			table.setBounds (5, 5, 300, 380);
			shell.open ();
			int n = 10_000;
			M3ProxyModel model = new M3ProxyModel (n).addFilter (s -> s % 3 == 0).addSort ((a, b) -> Integer.compare (b, a)).refresh ();
			java.util.Set<Integer> asked = new java.util.HashSet<> ();
			model.bind (table, (item, source) -> {
				asked.add (source);
				item.setText ("source " + source);
			});
			assertEquals (model.size (), table.getItemCount ());
			for (int r = 0; r < model.size (); r += 97) assertEquals ("source " + model.map (r), table.getItem (r).getText ());
			while (display.readAndDispatch ()) {
				// paint the first rows
			}
			for (int source : asked) assertEquals (0, source % 3, "asked a filtered row " + source);
			/* a new stage and a new binding: the table follows */
			model.addFilter (s -> s < 300).refresh ();
			model.bind (table, (item, source) -> item.setText ("again " + source));
			assertEquals (100, table.getItemCount ());
			assertEquals ("again 297", table.getItem (0).getText ());
			assertThrows (IllegalArgumentException.class, () -> model.bind (new Table (shell, SWT.NONE), (item, source) -> {}));
			assertThrows (IllegalArgumentException.class, () -> model.bind ((Table) null, (item, source) -> {}));
			assertThrows (IllegalArgumentException.class, () -> model.bind (table, null));
		} finally {
			shell.dispose ();
		}
	}

	@Test
	public void test_listBinding () {
		Display display = Display.getDefault ();
		Shell shell = new Shell (display);
		try {
			List list = new List (shell, SWT.SINGLE);
			String [] names = {"pear", "apple", "fig", "kiwi"};
			M3ProxyModel model = new M3ProxyModel (names.length).addFilter (s -> !names [s].equals ("fig")).addSort ((a, b) -> names [a].compareTo (names [b])).refresh ();
			model.bind (list, s -> names [s]);
			assertArrayEquals (new String [] {"apple", "kiwi", "pear"}, list.getItems ());
			assertThrows (IllegalArgumentException.class, () -> model.bind ((List) null, s -> ""));
			assertThrows (IllegalArgumentException.class, () -> model.bind (list, null));
		} finally {
			shell.dispose ();
		}
	}
}

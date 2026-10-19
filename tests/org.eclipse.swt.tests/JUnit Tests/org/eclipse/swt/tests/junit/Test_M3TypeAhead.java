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

import java.util.Random;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3TypeAhead (enhanced M3 API, leaf 15). The oracle is a linear scan over the texts
 * the test put in, ignoring case like the native search. The class passes unchanged in every
 * startup mode; with the Table viewport no item object is made.
 */
public class Test_M3TypeAhead {
	static final String [] WORDS = {"apple", "Apricot", "banana", "Blueberry", "cherry", "", "APPLE", "apple pie", "Äpfel", "kiwi"};
	Display display;
	Shell shell;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setBounds (20, 20, 320, 420);
		shell.open ();
	}

	@AfterEach
	public void tearDown () {
		shell.dispose ();
	}

	private static long oracle (IntFunction<String> textOf, int count, String text, long from, boolean prefix, boolean wrap) {
		long start = Math.max (0, Math.min (from, count));
		for (long k = 0; k < count; k++) {
			long row = start + k;
			if (row >= count) {
				if (!wrap) break;
				row -= count;
			}
			String candidate = textOf.apply ((int) row);
			if (prefix ? candidate.regionMatches (true, 0, text, 0, text.length ()) : candidate.equalsIgnoreCase (text)) return row;
		}
		return -1;
	}

	private static String [] texts (Random random, int n) {
		String [] texts = new String [n];
		for (int i = 0; i < n; i++) texts [i] = WORDS [random.nextInt (WORDS.length)];
		return texts;
	}

	private static final String [] QUERIES = {"a", "ap", "APPLE", "apple", "b", "blue", "z", "", "äp", "apple pie", "kiwi"};

	@Test
	public void test_tableAgreesWithALinearScan () {
		Random random = new Random (8);
		String [] texts = texts (random, 400);
		Table table = new Table (shell, SWT.MULTI);
		table.setBounds (5, 5, 300, 380);
		for (String text : texts) new TableItem (table, SWT.NONE).setText (text);
		for (String query : QUERIES) {
			for (int from : new int [] {-3, 0, 1, 199, 399, 400, 1000}) {
				for (boolean prefix : new boolean [] {true, false}) {
					for (boolean wrap : new boolean [] {true, false}) {
						long expected = oracle (i -> texts [i], texts.length, query, from, prefix, wrap);
						assertEquals (expected, M3TypeAhead.indexOf (table, query, from, prefix, wrap), query + " from " + from + " prefix " + prefix + " wrap " + wrap);
					}
				}
			}
		}
	}

	@Test
	public void test_tableLaneSearchMakesNoItem () {
		Table table = new Table (shell, SWT.MULTI);
		table.setBounds (5, 5, 300, 380);
		table.setItemCount (20_000);
		table.getItem (15_000).setText ("needle");
		System.gc ();
		long before = M3Residency.stats (table) [M3Residency.FACADES];
		assertEquals (15_000, M3TypeAhead.indexOf (table, "NEED", 100, true, false));
		assertEquals (15_000, M3TypeAhead.indexOf (table, "needle", 16_000, false, true));
		assertEquals (-1, M3TypeAhead.indexOf (table, "needle", 16_000, false, false));
		if (M3Mode.of (table) == M3Mode.VIEWPORT) {
			assertTrue (M3Residency.stats (table) [M3Residency.FACADES] <= before, "the lane search made items");
		}
	}

	@Test
	public void test_virtualTableAndListAgreeWithALinearScan () {
		Random random = new Random (9);
		String [] texts = texts (random, 300);
		Table table = new Table (shell, SWT.VIRTUAL);
		table.addListener (SWT.SetData, event -> ((TableItem) event.item).setText (texts [event.index]));
		table.setItemCount (texts.length);
		List list = new List (shell, SWT.MULTI);
		list.setItems (texts);
		for (String query : QUERIES) {
			for (int from : new int [] {0, 150, 299, 300}) {
				long expected = oracle (i -> texts [i], texts.length, query, from, true, true);
				assertEquals (expected, M3TypeAhead.indexOf (table, query, from, true, true), "table " + query + " from " + from);
				assertEquals (expected, M3TypeAhead.indexOf (list, query, from, true, true), "list " + query + " from " + from);
				long exact = oracle (i -> texts [i], texts.length, query, from, false, false);
				assertEquals (exact, M3TypeAhead.indexOf (list, query, from, false, false), "list exact " + query + " from " + from);
			}
		}
	}

	@Test
	public void test_treeSearchesTheVisibleRows () {
		Tree tree = new Tree (shell, SWT.MULTI);
		tree.setBounds (5, 5, 300, 380);
		Random random = new Random (10);
		for (int i = 0; i < 40; i++) {
			TreeItem root = new TreeItem (tree, SWT.NONE);
			root.setText (WORDS [random.nextInt (WORDS.length)]);
			for (int j = 0; j < 3; j++) new TreeItem (root, SWT.NONE).setText (WORDS [random.nextInt (WORDS.length)]);
			if (i % 4 == 0) root.setExpanded (true);
		}
		int rows = (int) M3RowMath.visibleRowCount (tree);
		IntFunction<String> textOf = row -> M3RowMath.itemAt (tree, row).getText ();
		for (String query : QUERIES) {
			for (long from : new long [] {0, 7, rows - 1, rows, rows + 5}) {
				for (boolean wrap : new boolean [] {true, false}) {
					assertEquals (oracle (textOf, rows, query, from, true, wrap), M3TypeAhead.indexOf (tree, query, from, true, wrap), query + " from " + from + " wrap " + wrap);
				}
			}
		}
	}

	@Test
	public void test_argumentsAreChecked () {
		Table table = new Table (shell, SWT.NONE);
		List list = new List (shell, SWT.NONE);
		Tree tree = new Tree (shell, SWT.NONE);
		assertEquals (-1, M3TypeAhead.indexOf (table, "x", 0, true, true));
		assertEquals (-1, M3TypeAhead.indexOf (list, "x", 0, true, true));
		assertEquals (-1, M3TypeAhead.indexOf (tree, "x", 0, true, true));
		assertThrows (IllegalArgumentException.class, () -> M3TypeAhead.indexOf ((Table) null, "x", 0, true, true));
		assertThrows (IllegalArgumentException.class, () -> M3TypeAhead.indexOf (table, null, 0, true, true));
		assertThrows (IllegalArgumentException.class, () -> M3TypeAhead.indexOf ((List) null, "x", 0, true, true));
		assertThrows (IllegalArgumentException.class, () -> M3TypeAhead.indexOf (list, null, 0, true, true));
		assertThrows (IllegalArgumentException.class, () -> M3TypeAhead.indexOf ((Tree) null, "x", 0, true, true));
		assertThrows (IllegalArgumentException.class, () -> M3TypeAhead.indexOf (tree, null, 0, true, true));
		table.dispose ();
		assertThrows (SWTException.class, () -> M3TypeAhead.indexOf (table, "x", 0, true, true));
	}
}

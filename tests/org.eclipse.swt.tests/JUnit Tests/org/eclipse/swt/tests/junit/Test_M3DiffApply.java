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

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Random;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3DiffApply (enhanced M3 API, leaf 10). Oracles: the final rows equal the new ids,
 * the kept count equals a quadratic dynamic-programming LIS of the old positions in new order,
 * and kept rows are the same item objects with the same selection, checked state and expansion.
 */
public class Test_M3DiffApply {
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

	private static String text (long id) {
		return "id " + id;
	}

	/* quadratic LIS of the old positions in new order: the oracle of the kept count */
	private static int lis (long [] oldIds, long [] newIds) {
		java.util.Map<Long, Integer> position = new java.util.HashMap<> ();
		for (int i = 0; i < oldIds.length; i++) position.put (oldIds [i], i);
		int [] sequence = LongStream.of (newIds).filter (position::containsKey).mapToInt (id -> position.get (id)).toArray ();
		int [] best = new int [sequence.length];
		int longest = 0;
		for (int i = 0; i < sequence.length; i++) {
			best [i] = 1;
			for (int j = 0; j < i; j++) if (sequence [j] < sequence [i]) best [i] = Math.max (best [i], best [j] + 1);
			longest = Math.max (longest, best [i]);
		}
		return longest;
	}

	private static long [] randomIds (Random random, long [] from, double drop, int add, long nextId) {
		java.util.List<Long> ids = new ArrayList<> ();
		for (long id : from) if (random.nextDouble () >= drop) ids.add (id);
		for (int i = 0; i < add; i++) ids.add (random.nextInt (ids.size () + 1), nextId + i);
		/* a few moves */
		for (int i = 0; i < 3 && ids.size () > 1; i++) ids.add (random.nextInt (ids.size ()), ids.remove (random.nextInt (ids.size ())));
		return ids.stream ().mapToLong (Long::longValue).toArray ();
	}

	/* ------------------------------------------------------------------ List */

	@Test
	public void test_listAgreesWithTheOracleOnRandomDiffs () {
		List list = new List (shell, SWT.MULTI | SWT.V_SCROLL);
		list.setBounds (5, 5, 300, 380);
		Random random = new Random (11);
		long [] ids = LongStream.range (0, 60).toArray ();
		for (long id : ids) list.add (text (id));
		long next = 1000;
		for (int round = 0; round < 60; round++) {
			long [] wanted = randomIds (random, ids, 0.1, random.nextInt (6), next);
			next += 10;
			int [] answer = M3DiffApply.apply (list, ids, wanted, Test_M3DiffApply::text);
			assertArrayEquals (LongStream.of (wanted).mapToObj (Test_M3DiffApply::text).toArray (String []::new), list.getItems (), "round " + round);
			int kept = lis (ids, wanted);
			assertArrayEquals (new int [] {kept, ids.length - kept, wanted.length - kept}, answer, "round " + round);
			ids = wanted;
		}
	}

	@Test
	public void test_listHandlesEmptyAndDisjointSets () {
		List list = new List (shell, SWT.SINGLE);
		assertArrayEquals (new int [] {0, 0, 3}, M3DiffApply.apply (list, new long [0], new long [] {1, 2, 3}, Test_M3DiffApply::text));
		assertArrayEquals (new int [] {0, 3, 2}, M3DiffApply.apply (list, new long [] {1, 2, 3}, new long [] {7, 8}, Test_M3DiffApply::text));
		assertArrayEquals (new String [] {text (7), text (8)}, list.getItems ());
		assertArrayEquals (new int [] {0, 2, 0}, M3DiffApply.apply (list, new long [] {7, 8}, new long [0], Test_M3DiffApply::text));
		assertEquals (0, list.getItemCount ());
	}

	@Test
	public void test_largeReorderIsFast () {
		List list = new List (shell, SWT.MULTI | SWT.V_SCROLL);
		list.setBounds (5, 5, 300, 380);
		int n = 20_000;
		long [] ids = LongStream.range (0, n).toArray ();
		list.setItems (LongStream.of (ids).mapToObj (Test_M3DiffApply::text).toArray (String []::new));
		/* rotate by one: one row moves, n - 1 stay */
		long [] rotated = LongStream.concat (LongStream.range (1, n), LongStream.of (0)).toArray ();
		long start = System.nanoTime ();
		int [] answer = M3DiffApply.apply (list, ids, rotated, Test_M3DiffApply::text);
		long millis = (System.nanoTime () - start) / 1_000_000;
		assertArrayEquals (new int [] {n - 1, 1, 1}, answer);
		assertEquals (text (1), list.getItem (0));
		assertEquals (text (0), list.getItem (n - 1));
		assertTrue (millis < 10_000, "rotation took " + millis + " ms");
	}

	/* ------------------------------------------------------------------ Table */

	@Test
	public void test_tableKeepsTheItemsAndStateOfKeptRows () {
		Table table = new Table (shell, SWT.MULTI | SWT.CHECK);
		table.setBounds (5, 5, 300, 380);
		long [] ids = LongStream.range (0, 10).toArray ();
		TableItem [] before = new TableItem [ids.length];
		for (int i = 0; i < ids.length; i++) (before [i] = new TableItem (table, SWT.NONE)).setText (text (ids [i]));
		before [3].setChecked (true);
		table.select (new int [] {2, 5});
		/* move row 0 to the end, drop row 7, add 42 in front */
		long [] wanted = {42, 1, 2, 3, 4, 5, 6, 8, 9, 0};
		int [] answer = M3DiffApply.apply (table, ids, wanted, (item, id) -> item.setText (text (id)));
		assertArrayEquals (new int [] {8, 2, 2}, answer);
		for (int i = 0; i < wanted.length; i++) assertEquals (text (wanted [i]), table.getItem (i).getText ());
		IdentityHashMap<TableItem, Boolean> now = new IdentityHashMap<> ();
		for (TableItem item : table.getItems ()) now.put (item, true);
		for (int id : new int [] {1, 2, 3, 4, 5, 6, 8, 9}) assertTrue (now.containsKey (before [id]), "kept row " + id + " lost its item");
		assertTrue (before [0].isDisposed (), "a moved row is a new item");
		assertTrue (before [3].getChecked ());
		assertArrayEquals (new int [] {2, 5}, table.getSelectionIndices ());
	}

	@Test
	public void test_virtualTableFillsInsertedRowsThroughSetData () {
		Table table = new Table (shell, SWT.VIRTUAL | SWT.MULTI);
		table.setBounds (5, 5, 300, 380);
		long [][] model = {LongStream.range (0, 100).toArray ()};
		table.addListener (SWT.SetData, event -> ((TableItem) event.item).setText (text (model [0] [event.index])));
		table.setItemCount (100);
		long [] wanted = LongStream.concat (LongStream.of (500, 501), LongStream.range (0, 98)).toArray ();
		long [] old = model [0];
		model [0] = wanted;
		int [] answer = M3DiffApply.apply (table, old, wanted, null);
		assertArrayEquals (new int [] {98, 2, 2}, answer);
		for (int i = 0; i < 100; i += 9) assertEquals (text (wanted [i]), table.getItem (i).getText ());
	}

	/* ------------------------------------------------------------------ Tree */

	@Test
	public void test_treeRootsKeepTheirExpansionAndChildren () {
		Tree tree = new Tree (shell, SWT.MULTI);
		tree.setBounds (5, 5, 300, 380);
		long [] ids = LongStream.range (0, 6).toArray ();
		TreeItem [] roots = new TreeItem [ids.length];
		for (int i = 0; i < ids.length; i++) {
			roots [i] = new TreeItem (tree, SWT.NONE);
			roots [i].setText (text (ids [i]));
			for (int j = 0; j < 3; j++) new TreeItem (roots [i], SWT.NONE).setText ("child " + j);
		}
		roots [2].setExpanded (true);
		long [] wanted = {5, 0, 1, 2, 3, 4};
		int [] answer = M3DiffApply.applyChildren (tree, null, ids, wanted, (item, id) -> {
			item.setText (text (id));
			new TreeItem (item, SWT.NONE).setText ("rebuilt");
		});
		assertArrayEquals (new int [] {5, 1, 1}, answer);
		for (int i = 0; i < wanted.length; i++) assertEquals (text (wanted [i]), tree.getItem (i).getText ());
		assertSame (roots [2], tree.getItem (3));
		assertTrue (roots [2].getExpanded ());
		assertEquals (3, roots [2].getItemCount ());
		assertEquals ("rebuilt", tree.getItem (0).getItem (0).getText ());
		/* children of one item */
		long [] children = {0, 1, 2};
		long [] reordered = {2, 1, 0};
		assertArrayEquals (new int [] {1, 2, 2}, M3DiffApply.applyChildren (tree, roots [2], children, reordered, (item, id) -> item.setText ("child " + id)));
		assertEquals ("child 2", roots [2].getItem (0).getText ());
		assertEquals ("child 0", roots [2].getItem (2).getText ());
	}

	/* ------------------------------------------------------------------ arguments */

	@Test
	public void test_argumentsAreChecked () {
		List list = new List (shell, SWT.SINGLE);
		list.add (text (1));
		list.add (text (2));
		Table table = new Table (shell, SWT.NONE);
		Tree tree = new Tree (shell, SWT.NONE);
		long [] two = {1, 2};
		assertThrows (IllegalArgumentException.class, () -> M3DiffApply.apply ((List) null, two, two, Test_M3DiffApply::text));
		assertThrows (IllegalArgumentException.class, () -> M3DiffApply.apply (list, null, two, Test_M3DiffApply::text));
		assertThrows (IllegalArgumentException.class, () -> M3DiffApply.apply (list, two, null, Test_M3DiffApply::text));
		assertThrows (IllegalArgumentException.class, () -> M3DiffApply.apply (list, two, two, null));
		assertThrows (IllegalArgumentException.class, () -> M3DiffApply.apply (list, new long [] {1}, two, Test_M3DiffApply::text));
		assertThrows (IllegalArgumentException.class, () -> M3DiffApply.apply (list, new long [] {1, 1}, two, Test_M3DiffApply::text));
		assertThrows (IllegalArgumentException.class, () -> M3DiffApply.apply (list, two, new long [] {3, 3}, Test_M3DiffApply::text));
		assertArrayEquals (new String [] {text (1), text (2)}, list.getItems (), "a rejected diff changes nothing");
		assertThrows (IllegalArgumentException.class, () -> M3DiffApply.apply (table, new long [0], two, null));
		assertThrows (IllegalArgumentException.class, () -> M3DiffApply.apply ((Table) null, new long [0], two, (item, id) -> {}));
		assertThrows (IllegalArgumentException.class, () -> M3DiffApply.applyChildren (null, null, new long [0], two, (item, id) -> {}));
		assertThrows (IllegalArgumentException.class, () -> M3DiffApply.applyChildren (tree, null, new long [0], two, null));
		Tree other = new Tree (shell, SWT.NONE);
		TreeItem foreign = new TreeItem (other, SWT.NONE);
		assertThrows (IllegalArgumentException.class, () -> M3DiffApply.applyChildren (tree, foreign, new long [0], two, (item, id) -> {}));
		list.dispose ();
		assertThrows (SWTException.class, () -> M3DiffApply.apply (list, two, two, Test_M3DiffApply::text));
	}
}

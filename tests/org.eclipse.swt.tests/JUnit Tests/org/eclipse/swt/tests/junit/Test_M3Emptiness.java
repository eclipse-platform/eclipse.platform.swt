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
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3Emptiness (enhanced M3 API, leaf 5). The oracle is <code>getItemCount</code>
 * before and after every public operation: an event is expected exactly when the count crossed
 * zero. The class passes unchanged in every startup mode; the mode differential is this class run
 * with <code>-Dswt.widget.mode=viewport</code> and with <code>-Dswt.widget.mode=original</code>.
 */
public class Test_M3Emptiness {
	Display display;
	Shell shell;
	/* events received: {type, detail, count} */
	final java.util.List<int []> events = new ArrayList<> ();
	final Listener recorder = event -> events.add (new int [] {event.type, event.detail, event.count});

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

	/* asserts the events since the last call: each pair is {detail, count} */
	private void expect (int... detailCount) {
		assertEquals (detailCount.length / 2, events.size (), () -> "events " + describe ());
		for (int i = 0; i < events.size (); i++) {
			assertEquals (M3Emptiness.EVENT, events.get (i) [0]);
			assertEquals (detailCount [2 * i], events.get (i) [1], () -> "detail " + describe ());
			assertEquals (detailCount [2 * i + 1], events.get (i) [2], () -> "count " + describe ());
		}
		events.clear ();
	}

	private String describe () {
		StringBuilder text = new StringBuilder ();
		for (int [] event : events) text.append (Arrays.toString (event));
		return text.toString ();
	}

	private static final int EMPTY = M3Emptiness.BECAME_EMPTY;
	private static final int FILLED = M3Emptiness.BECAME_NON_EMPTY;

	/* ------------------------------------------------------------------ Table */

	@Test
	public void test_tableReportsEveryZeroCrossingOnce () {
		Table table = new Table (shell, SWT.MULTI | SWT.BORDER);
		M3Emptiness.addListener (table, recorder);
		TableItem first = new TableItem (table, SWT.NONE);
		expect (FILLED, 1);
		TableItem second = new TableItem (table, SWT.NONE, 0);
		table.clearAll ();
		second.dispose ();
		expect ();
		first.dispose ();
		expect (EMPTY, 0);
		for (int i = 0; i < 3; i++) new TableItem (table, SWT.NONE);
		expect (FILLED, 1);
		table.remove (0);
		table.remove (0, 1);
		expect (EMPTY, 0);
		table.setItemCount (5);
		expect (FILLED, 5);
		table.setItemCount (5);
		table.setItemCount (2);
		expect ();
		table.remove (new int [] {1, 0, 1});
		expect (EMPTY, 0);
		table.removeAll ();
		table.remove (new int [0]);
		table.setItemCount (0);
		expect ();
		table.setItemCount (3);
		table.removeAll ();
		expect (FILLED, 3, EMPTY, 0);
	}

	@Test
	public void test_virtualTableReportsCountChanges () {
		Table table = new Table (shell, SWT.VIRTUAL | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		table.addListener (SWT.SetData, event -> ((TableItem) event.item).setText ("row " + event.index));
		M3Emptiness.addListener (table, recorder);
		table.setItemCount (100000);
		expect (FILLED, 100000);
		table.getItem (99999).dispose ();
		table.remove (0, 49999);
		expect ();
		table.setItemCount (0);
		expect (EMPTY, 0);
	}

	/* ------------------------------------------------------------------ List */

	@Test
	public void test_listReportsEveryZeroCrossingOnce () {
		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List (shell, SWT.MULTI | SWT.BORDER);
		M3Emptiness.addListener (list, recorder);
		list.add ("a");
		expect (FILLED, 1);
		/* remove and add inside: the transient empty list is not reported */
		list.setItem (0, "b");
		assertEquals ("b", list.getItem (0));
		expect ();
		list.add ("c", 0);
		list.remove ("b");
		expect ();
		/* remove (String) calls remove (int): one event */
		list.remove ("c");
		expect (EMPTY, 0);
		list.setItems ("x", "y");
		expect (FILLED, 2);
		list.setItems ();
		expect (EMPTY, 0);
		/* the native setItemCount adds and removes through the public methods: one event each way */
		list.setItemCount (3);
		expect (FILLED, 3);
		list.setItemCount (0);
		expect (EMPTY, 0);
		list.setItems ("a", "b", "c", "d");
		list.remove (new int [] {3, 1});
		list.remove (0);
		expect (FILLED, 4);
		list.remove (0, 0);
		expect (EMPTY, 0);
		list.removeAll ();
		list.remove (new int [0]);
		expect ();
		list.add ("z");
		list.removeAll ();
		expect (FILLED, 1, EMPTY, 0);
	}

	@Test
	public void test_listFailedOperationsAreReportedByTheNextChange () {
		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List (shell, SWT.SINGLE);
		M3Emptiness.addListener (list, recorder);
		assertThrows (IllegalArgumentException.class, () -> list.setItem (0, "x"));
		assertThrows (IllegalArgumentException.class, () -> list.add (null));
		assertThrows (IllegalArgumentException.class, () -> list.remove (5));
		expect ();
		/* the hold of the failed setItem was released: the next change is reported */
		list.add ("a");
		expect (FILLED, 1);
	}

	/* ------------------------------------------------------------------ Tree */

	@Test
	public void test_treeCountsRootsOnly () {
		Tree tree = new Tree (shell, SWT.MULTI | SWT.BORDER);
		M3Emptiness.addListener (tree, recorder);
		TreeItem root = new TreeItem (tree, SWT.NONE);
		expect (FILLED, 1);
		TreeItem child = new TreeItem (root, SWT.NONE);
		new TreeItem (child, SWT.NONE);
		TreeItem other = new TreeItem (tree, SWT.NONE, 0);
		child.dispose ();
		other.dispose ();
		root.removeAll ();
		root.setItemCount (3);
		expect ();
		/* the root goes with its children: one event */
		root.dispose ();
		expect (EMPTY, 0);
		tree.setItemCount (4);
		expect (FILLED, 4);
		tree.getItem (0).setItemCount (2);
		tree.setItemCount (1);
		expect ();
		tree.removeAll ();
		expect (EMPTY, 0);
		tree.removeAll ();
		tree.setItemCount (0);
		expect ();
	}

	@Test
	public void test_virtualTreeReportsCountChanges () {
		Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> ((TreeItem) event.item).setText ("row " + event.index));
		M3Emptiness.addListener (tree, recorder);
		tree.setItemCount (50000);
		expect (FILLED, 50000);
		tree.getItem (0).setItemCount (10);
		tree.getItem (1).dispose ();
		expect ();
		tree.setItemCount (0);
		expect (EMPTY, 0);
		new TreeItem (tree, SWT.NONE);
		expect (FILLED, 1);
		tree.getItem (0).dispose ();
		expect (EMPTY, 0);
	}

	@Test
	public void test_stockTreeEventIsUnchangedAndSeparate () {
		Tree tree = new Tree (shell, SWT.BORDER);
		java.util.List<int []> stock = new ArrayList<> ();
		tree.addListener (SWT.EmptinessChanged, event -> stock.add (new int [] {event.type, event.detail}));
		M3Emptiness.addListener (tree, recorder);
		TreeItem item = new TreeItem (tree, SWT.NONE);
		item.dispose ();
		new TreeItem (tree, SWT.NONE);
		int stockBeforeRemoveAll = stock.size ();
		tree.removeAll ();
		expect (FILLED, 1, EMPTY, 0, FILLED, 1, EMPTY, 0);
		/* stock: first item created (0), last item disposed (1); its removeAll sends nothing */
		assertTrue (stock.size () >= 2);
		for (int [] event : stock) assertEquals (SWT.EmptinessChanged, event [0]);
		assertEquals (0, stock.get (0) [1]);
		assertEquals (1, stock.get (1) [1]);
		assertEquals (stockBeforeRemoveAll, stock.size ());
		assertNotEquals (SWT.EmptinessChanged, M3Emptiness.EVENT);
	}

	/* ------------------------------------------------------------------ registration */

	@Test
	public void test_listenersStartFromTheCurrentStateAndCanBeRemoved () {
		Table table = new Table (shell, SWT.NONE);
		new TableItem (table, SWT.NONE);
		M3Emptiness.addListener (table, recorder);
		new TableItem (table, SWT.NONE);
		expect ();
		java.util.List<Integer> second = new ArrayList<> ();
		Listener counter = event -> second.add (event.detail);
		M3Emptiness.addListener (table, counter);
		table.removeAll ();
		expect (EMPTY, 0);
		assertEquals (java.util.List.of (EMPTY), second);
		M3Emptiness.removeListener (table, recorder);
		new TableItem (table, SWT.NONE);
		expect ();
		assertEquals (java.util.List.of (EMPTY, FILLED), second);
		M3Emptiness.removeListener (table, counter);
		table.removeAll ();
		assertEquals (2, second.size ());
		/* a new registration reads the state again */
		M3Emptiness.addListener (table, recorder);
		new TableItem (table, SWT.NONE);
		expect (FILLED, 1);
	}

	@Test
	public void test_listenerMayChangeTheWidget () {
		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List (shell, SWT.NONE);
		/* a placeholder that refills the list when it becomes empty */
		M3Emptiness.addListener (list, event -> {
			if (event.detail == EMPTY) list.add ("placeholder");
		});
		M3Emptiness.addListener (list, recorder);
		list.add ("a");
		list.remove (0);
		assertEquals (1, list.getItemCount ());
		expect (FILLED, 1, EMPTY, 0, FILLED, 1);
	}

	@Test
	public void test_argumentsAreChecked () {
		Tree tree = new Tree (shell, SWT.NONE);
		Table table = new Table (shell, SWT.NONE);
		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List (shell, SWT.NONE);
		assertThrows (IllegalArgumentException.class, () -> M3Emptiness.addListener ((Tree) null, recorder));
		assertThrows (IllegalArgumentException.class, () -> M3Emptiness.addListener (table, null));
		assertThrows (IllegalArgumentException.class, () -> M3Emptiness.addListener ((org.eclipse.swt.widgets.List) null, recorder));
		assertThrows (IllegalArgumentException.class, () -> M3Emptiness.removeListener ((Table) null, recorder));
		assertThrows (IllegalArgumentException.class, () -> M3Emptiness.removeListener (tree, null));
		assertThrows (IllegalArgumentException.class, () -> M3Emptiness.removeListener (list, null));
		M3Emptiness.removeListener (tree, recorder);
		tree.dispose ();
		assertThrows (SWTException.class, () -> M3Emptiness.addListener (tree, recorder));
		assertThrows (SWTException.class, () -> M3Emptiness.removeListener (tree, recorder));
	}

	/* ------------------------------------------------------------------ oracle */

	@Test
	public void test_randomOperationsAgreeWithTheCountOracle () {
		Random random = new Random (31);
		Table table = new Table (shell, SWT.MULTI);
		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List (shell, SWT.MULTI);
		Tree tree = new Tree (shell, SWT.MULTI);
		M3Emptiness.addListener (table, recorder);
		M3Emptiness.addListener (list, recorder);
		M3Emptiness.addListener (tree, recorder);
		java.util.List<Consumer<Random>> tableOps = java.util.List.of (
			r -> new TableItem (table, SWT.NONE),
			r -> { if (table.getItemCount () > 0) table.getItem (r.nextInt (table.getItemCount ())).dispose (); },
			r -> { if (table.getItemCount () > 0) table.remove (r.nextInt (table.getItemCount ())); },
			r -> table.setItemCount (r.nextInt (4)),
			r -> table.removeAll (),
			r -> { int n = table.getItemCount (); if (n > 0) table.remove (0, r.nextInt (n)); });
		java.util.List<Consumer<Random>> listOps = java.util.List.of (
			r -> list.add ("x"),
			r -> { if (list.getItemCount () > 0) list.setItem (0, "y"); },
			r -> { if (list.getItemCount () > 0) list.remove (r.nextInt (list.getItemCount ())); },
			r -> list.setItemCount (r.nextInt (4)),
			r -> list.setItems (r.nextBoolean () ? new String [0] : new String [] {"a"}),
			r -> list.removeAll ());
		java.util.List<Consumer<Random>> treeOps = java.util.List.of (
			r -> new TreeItem (tree, SWT.NONE),
			r -> { if (tree.getItemCount () > 0) new TreeItem (tree.getItem (0), SWT.NONE); },
			r -> { if (tree.getItemCount () > 0) tree.getItem (r.nextInt (tree.getItemCount ())).dispose (); },
			r -> tree.setItemCount (r.nextInt (4)),
			r -> tree.removeAll ());
		for (int step = 0; step < 600; step++) {
			int kind = random.nextInt (3);
			Control widget = kind == 0 ? table : kind == 1 ? list : tree;
			java.util.List<Consumer<Random>> ops = kind == 0 ? tableOps : kind == 1 ? listOps : treeOps;
			int before = count (widget);
			ops.get (random.nextInt (ops.size ())).accept (random);
			int after = count (widget);
			if ((before == 0) == (after == 0)) {
				expect ();
			} else {
				expect (after == 0 ? EMPTY : FILLED, after);
			}
		}
	}

	private static int count (Control widget) {
		if (widget instanceof Table table) return table.getItemCount ();
		if (widget instanceof Tree tree) return tree.getItemCount ();
		return ((org.eclipse.swt.widgets.List) widget).getItemCount ();
	}
}

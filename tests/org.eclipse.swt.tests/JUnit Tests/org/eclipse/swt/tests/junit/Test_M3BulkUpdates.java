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

import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3BulkUpdates (enhanced M3 API, leaf 8). The oracle is the same edit script run
 * without the call on a twin widget. The class passes unchanged in every startup mode.
 */
public class Test_M3BulkUpdates {
	Display display;
	Shell shell;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setBounds (20, 20, 640, 420);
		shell.open ();
	}

	@AfterEach
	public void tearDown () {
		shell.dispose ();
	}

	private void flush () {
		while (display.readAndDispatch ()) {
			// deliver pending paints and notifications
		}
	}

	/* ------------------------------------------------------------------ results */

	private static void listScript (List list) {
		for (int i = 0; i < 500; i++) list.add ("row " + i);
		list.remove (10, 19);
		list.add ("inserted", 3);
		list.setItem (0, "first");
		list.select (new int [] {1, 4, 7});
		list.setItemCount (450);
		list.remove (new int [] {5, 6});
		list.setTopIndex (100);
	}

	@Test
	public void test_listResultsEqualTheUnbatchedEdits () {
		List plain = new List (shell, SWT.MULTI | SWT.V_SCROLL);
		plain.setBounds (5, 5, 300, 380);
		List bulk = new List (shell, SWT.MULTI | SWT.V_SCROLL);
		bulk.setBounds (320, 5, 300, 380);
		listScript (plain);
		M3BulkUpdates.run (bulk, () -> listScript (bulk));
		flush ();
		assertArrayEquals (plain.getItems (), bulk.getItems ());
		assertArrayEquals (plain.getSelectionIndices (), bulk.getSelectionIndices ());
		assertEquals (plain.getTopIndex (), bulk.getTopIndex ());
	}

	private static void tableScript (Table table) {
		for (int i = 0; i < 2000; i++) new TableItem (table, SWT.NONE).setText ("row " + i);
		table.remove (100, 199);
		new TableItem (table, SWT.NONE, 5).setText ("inserted");
		table.getItem (0).setText ("first");
		table.select (new int [] {2, 9});
		table.setItemCount (1500);
	}

	@Test
	public void test_tableResultsEqualTheUnbatchedEdits () {
		Table plain = new Table (shell, SWT.MULTI);
		plain.setBounds (5, 5, 300, 380);
		Table bulk = new Table (shell, SWT.MULTI);
		bulk.setBounds (320, 5, 300, 380);
		tableScript (plain);
		M3BulkUpdates.run (bulk, () -> tableScript (bulk));
		flush ();
		assertEquals (plain.getItemCount (), bulk.getItemCount ());
		for (int i = 0; i < plain.getItemCount (); i += 7) assertEquals (plain.getItem (i).getText (), bulk.getItem (i).getText ());
		assertArrayEquals (plain.getSelectionIndices (), bulk.getSelectionIndices ());
	}

	private static void treeScript (Tree tree) {
		for (int i = 0; i < 200; i++) {
			TreeItem root = new TreeItem (tree, SWT.NONE);
			root.setText ("root " + i);
			if (i % 10 == 0) for (int j = 0; j < 5; j++) new TreeItem (root, SWT.NONE).setText ("child " + j);
		}
		tree.getItem (0).setExpanded (true);
		tree.getItem (20).dispose ();
		tree.setItemCount (150);
	}

	@Test
	public void test_treeResultsEqualTheUnbatchedEdits () {
		Tree plain = new Tree (shell, SWT.MULTI);
		plain.setBounds (5, 5, 300, 380);
		Tree bulk = new Tree (shell, SWT.MULTI);
		bulk.setBounds (320, 5, 300, 380);
		treeScript (plain);
		M3BulkUpdates.run (bulk, () -> treeScript (bulk));
		flush ();
		assertEquals (M3RowMath.visibleRowCount (plain), M3RowMath.visibleRowCount (bulk));
		for (long row = 0; row < M3RowMath.visibleRowCount (plain); row += 3) {
			assertEquals (M3RowMath.itemAt (plain, row).getText (), M3RowMath.itemAt (bulk, row).getText ());
		}
	}

	/* ------------------------------------------------------------------ painting */

	@Test
	public void test_nothingIsPaintedWhileTheEditsRun () {
		Table table = new Table (shell, SWT.MULTI);
		table.setBounds (5, 5, 300, 380);
		for (int i = 0; i < 50; i++) new TableItem (table, SWT.NONE).setText ("row " + i);
		flush ();
		int [] paints = new int [1];
		table.addListener (SWT.Paint, event -> paints [0]++);
		M3BulkUpdates.run (table, () -> {
			for (int i = 0; i < 50; i++) table.getItem (i).setText ("edited " + i);
			table.redraw ();
			table.update ();
			flush ();
			assertEquals (0, paints [0], "painted inside the bulk update");
		});
		table.redraw ();
		table.update ();
		flush ();
		assertTrue (paints [0] > 0, "painted after the bulk update");
	}

	@Test
	public void test_nestedRunsResumeAtTheOuterEnd () {
		List list = new List (shell, SWT.SINGLE);
		list.setBounds (5, 5, 300, 380);
		list.add ("a");
		flush ();
		int [] paints = new int [1];
		list.addListener (SWT.Paint, event -> paints [0]++);
		M3BulkUpdates.run (list, () -> {
			M3BulkUpdates.run (list, () -> list.add ("b"));
			list.redraw ();
			list.update ();
			flush ();
			assertEquals (0, paints [0], "painted after the inner end");
		});
		list.redraw ();
		list.update ();
		flush ();
		assertTrue (paints [0] > 0);
	}

	/* ------------------------------------------------------------------ emptiness */

	@Test
	public void test_oneEmptinessAnnouncementWithTheFinalCount () {
		List list = new List (shell, SWT.SINGLE);
		java.util.List<int []> events = new java.util.ArrayList<> ();
		M3Emptiness.addListener (list, event -> events.add (new int [] {event.detail, event.count}));
		M3BulkUpdates.run (list, () -> {
			list.add ("a");
			list.removeAll ();
			list.add ("b");
			list.add ("c");
		});
		assertEquals (1, events.size ());
		assertArrayEquals (new int [] {M3Emptiness.BECAME_NON_EMPTY, 2}, events.get (0));
		M3BulkUpdates.run (list, () -> {
			list.removeAll ();
			list.add ("d");
		});
		assertEquals (1, events.size ());
	}

	/* ------------------------------------------------------------------ failure */

	@Test
	public void test_anExceptionLeavesNothingFrozen () {
		Table table = new Table (shell, SWT.SINGLE);
		table.setBounds (5, 5, 300, 380);
		java.util.List<Integer> details = new java.util.ArrayList<> ();
		M3Emptiness.addListener (table, event -> details.add (event.detail));
		IllegalStateException failure = new IllegalStateException ("edit failed");
		IllegalStateException thrown = assertThrows (IllegalStateException.class, () -> M3BulkUpdates.run (table, () -> {
			new TableItem (table, SWT.NONE).setText ("partial");
			throw failure;
		}));
		assertSame (failure, thrown);
		/* the crossing made before the failure is announced when the update closes */
		assertEquals (java.util.List.of (M3Emptiness.BECAME_NON_EMPTY), details);
		flush ();
		int [] paints = new int [1];
		table.addListener (SWT.Paint, event -> paints [0]++);
		table.redraw ();
		table.update ();
		flush ();
		assertTrue (paints [0] > 0, "the table still paints");
		table.removeAll ();
		assertEquals (java.util.List.of (M3Emptiness.BECAME_NON_EMPTY, M3Emptiness.BECAME_EMPTY), details);
	}

	@Test
	public void test_theEditsMayDisposeTheWidget () {
		Table table = new Table (shell, SWT.NONE);
		M3BulkUpdates.run (table, table::dispose);
		assertTrue (table.isDisposed ());
	}

	@Test
	public void test_argumentsAreChecked () {
		Tree tree = new Tree (shell, SWT.NONE);
		Runnable nothing = () -> {};
		assertThrows (IllegalArgumentException.class, () -> M3BulkUpdates.run ((Tree) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3BulkUpdates.run ((Table) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3BulkUpdates.run ((List) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3BulkUpdates.run (tree, null));
		tree.dispose ();
		Consumer<Runnable> onDisposed = edits -> M3BulkUpdates.run (tree, edits);
		assertThrows (SWTException.class, () -> onDisposed.accept (nothing));
	}
}

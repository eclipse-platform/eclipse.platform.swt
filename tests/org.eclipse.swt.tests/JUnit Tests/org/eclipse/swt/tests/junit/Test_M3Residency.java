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

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3Residency (enhanced M3 API, leaf 7). The oracle for the window is the visible
 * range (<code>M3ItemSelection.getVisibleRange</code>): once a widget has painted, its resident
 * rows contain it. With <code>-Dswt.widget.mode=viewport</code> the windows are also checked to be
 * bounded; with <code>-Dswt.widget.mode=original</code> every row is resident.
 */
public class Test_M3Residency {
	static final int ROWS = 100_000;
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

	private void flush () {
		while (display.readAndDispatch ()) {
			// paint and deliver the pending notifications
		}
	}

	private static void assertCovers (M3ItemRange outer, M3ItemRange inner, String what) {
		if (inner.isEmpty ()) return;
		assertTrue (outer.start () <= inner.start () && inner.end () <= outer.end (), what + ": " + outer + " does not cover " + inner);
	}

	private static boolean viewport (Widget widget) {
		return M3Mode.of (widget) == M3Mode.VIEWPORT;
	}

	private Table virtualTable () {
		Table table = new Table (shell, SWT.VIRTUAL | SWT.MULTI | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		table.addListener (SWT.SetData, event -> ((TableItem) event.item).setText ("row " + event.index));
		table.setItemCount (ROWS);
		flush ();
		table.update ();
		flush ();
		return table;
	}

	/* ------------------------------------------------------------------ Table */

	@Test
	public void test_tableWindowCoversTheVisibleRowsAndIsBounded () {
		Table table = virtualTable ();
		for (int top : new int [] {0, 5000, ROWS / 2, ROWS - 1}) {
			table.setTopIndex (top);
			table.redraw ();
			table.update ();
			flush ();
			M3ItemRange resident = M3Residency.residentRange (table);
			assertCovers (resident, M3ItemSelection.getVisibleRange (table), "top " + top);
			if (viewport (table)) {
				assertTrue (resident.size () < 1000, "bounded window " + resident);
			} else {
				assertEquals (ROWS, resident.size ());
			}
			assertEquals (resident.size (), M3Residency.stats (table) [M3Residency.RESIDENT_ROWS]);
		}
	}

	@Test
	public void test_isMaterializedNeverMakesAnItem () {
		Table table = virtualTable ();
		int far = ROWS - 7;
		long facadesBefore = M3Residency.stats (table) [M3Residency.FACADES];
		assertFalse (M3Residency.isMaterialized (table, far));
		assertFalse (M3Residency.isMaterialized (table, far));
		assertEquals (facadesBefore, M3Residency.stats (table) [M3Residency.FACADES]);
		TableItem item = table.getItem (far);
		assertTrue (M3Residency.isMaterialized (table, far));
		assertEquals ("row " + far, item.getText ());
		assertTrue (M3Residency.stats (table) [M3Residency.FACADES] > facadesBefore);
	}

	@Test
	public void test_plainTableHasAnItemForEveryRow () {
		Table table = new Table (shell, SWT.MULTI | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		TableItem [] items = new TableItem [300];
		for (int i = 0; i < items.length; i++) (items [i] = new TableItem (table, SWT.NONE)).setText ("row " + i);
		flush ();
		table.update ();
		flush ();
		/* the application holds every item, so each one is materialized */
		for (int i = 0; i < items.length; i += 37) assertTrue (M3Residency.isMaterialized (table, i));
		long [] stats = M3Residency.stats (table);
		assertEquals (items.length, stats [M3Residency.FACADES]);
		assertTrue (stats [M3Residency.HELD_FACADES] <= stats [M3Residency.FACADES]);
		assertCovers (M3Residency.residentRange (table), M3ItemSelection.getVisibleRange (table), "plain");
	}

	@Test
	public void test_heldFacadesStayBoundedWhileScrolling () {
		Table table = virtualTable ();
		for (int top = 0; top < ROWS; top += ROWS / 40) {
			table.setTopIndex (top);
			table.update ();
			flush ();
		}
		long [] stats = M3Residency.stats (table);
		assertTrue (stats [M3Residency.FACADES] >= stats [M3Residency.HELD_FACADES]);
		assertTrue (stats [M3Residency.HELD_FACADES] >= 0);
		if (viewport (table)) assertTrue (stats [M3Residency.HELD_FACADES] < 2000, "held " + stats [M3Residency.HELD_FACADES]);
	}

	/* ------------------------------------------------------------------ List */

	@Test
	public void test_listWindowCoversTheVisibleRowsAndIsBounded () {
		List list = new List (shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		list.setBounds (5, 5, 300, 380);
		String [] strings = new String [ROWS];
		for (int i = 0; i < ROWS; i++) strings [i] = "row " + i;
		list.setItems (strings);
		for (int top : new int [] {0, ROWS / 3, ROWS - 1}) {
			list.setTopIndex (top);
			list.update ();
			flush ();
			M3ItemRange resident = M3Residency.residentRange (list);
			assertCovers (resident, M3ItemSelection.getVisibleRange (list), "top " + top);
			if (viewport (list)) {
				assertTrue (resident.size () < 2000, "bounded window " + resident);
			} else {
				assertEquals (ROWS, resident.size ());
			}
			long [] stats = M3Residency.stats (list);
			assertArrayEquals (new long [] {resident.size (), 0, 0}, stats);
		}
		list.removeAll ();
		flush ();
		assertTrue (M3Residency.residentRange (list).isEmpty ());
	}

	/* ------------------------------------------------------------------ Tree */

	@Test
	public void test_treeWindowCoversTheVisibleRowsAndIsBounded () {
		Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.MULTI | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> ((TreeItem) event.item).setText ("node " + event.index));
		tree.setItemCount (ROWS);
		flush ();
		for (int index : new int [] {0, ROWS / 2, ROWS - 1}) {
			tree.setTopItem (tree.getItem (index));
			tree.update ();
			flush ();
			M3ItemRange resident = M3Residency.residentRange (tree);
			assertCovers (resident, M3ItemSelection.getVisibleRange (tree), "top " + index);
			if (viewport (tree)) {
				assertTrue (resident.size () < 20_000, "bounded window " + resident);
			} else {
				assertEquals (M3RowMath.visibleRowCount (tree), resident.size ());
			}
			long [] stats = M3Residency.stats (tree);
			assertEquals (resident.size (), stats [M3Residency.RESIDENT_ROWS]);
			assertTrue (stats [M3Residency.FACADES] >= 1);
			assertTrue (stats [M3Residency.HELD_FACADES] <= stats [M3Residency.FACADES]);
		}
		tree.removeAll ();
		flush ();
		assertTrue (M3Residency.residentRange (tree).isEmpty ());
	}

	/* ------------------------------------------------------------------ arguments */

	@Test
	public void test_argumentsAreChecked () {
		Table table = new Table (shell, SWT.NONE);
		new TableItem (table, SWT.NONE);
		assertThrows (IllegalArgumentException.class, () -> M3Residency.residentRange ((Table) null));
		assertThrows (IllegalArgumentException.class, () -> M3Residency.residentRange ((List) null));
		assertThrows (IllegalArgumentException.class, () -> M3Residency.residentRange ((Tree) null));
		assertThrows (IllegalArgumentException.class, () -> M3Residency.stats ((Table) null));
		assertThrows (IllegalArgumentException.class, () -> M3Residency.stats ((List) null));
		assertThrows (IllegalArgumentException.class, () -> M3Residency.stats ((Tree) null));
		assertThrows (IllegalArgumentException.class, () -> M3Residency.isMaterialized (null, 0));
		assertThrows (IllegalArgumentException.class, () -> M3Residency.isMaterialized (table, -1));
		assertThrows (IllegalArgumentException.class, () -> M3Residency.isMaterialized (table, 1));
		Table empty = new Table (shell, SWT.NONE);
		assertTrue (M3Residency.residentRange (empty).isEmpty ());
		assertEquals (0, M3Residency.stats (empty) [M3Residency.FACADES]);
		table.dispose ();
		assertThrows (SWTException.class, () -> M3Residency.residentRange (table));
		assertThrows (SWTException.class, () -> M3Residency.isMaterialized (table, 0));
	}
}

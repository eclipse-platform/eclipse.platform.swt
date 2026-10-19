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
 * Contract of M3ScrollMemento (enhanced M3 API, leaf 13). The oracle is the text of the top row
 * before and after the edits. The class passes unchanged in every startup mode.
 */
public class Test_M3ScrollMemento {
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

	private Table table (int rows) {
		Table table = new Table (shell, SWT.MULTI | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		for (int i = 0; i < rows; i++) new TableItem (table, SWT.NONE).setText ("row " + i);
		return table;
	}

	@Test
	public void test_restoreReturnsToTheSavedTopRow () {
		Table table = table (500);
		table.setTopIndex (240);
		long memento = M3ScrollMemento.save (table);
		table.setTopIndex (0);
		assertTrue (M3ScrollMemento.restore (table, memento));
		assertEquals (240, table.getTopIndex ());
		/* a rebuild with the same rows */
		table.removeAll ();
		for (int i = 0; i < 500; i++) new TableItem (table, SWT.NONE).setText ("row " + i);
		assertTrue (M3ScrollMemento.restore (table, memento));
		assertEquals ("row 240", table.getItem (table.getTopIndex ()).getText ());
	}

	@Test
	public void test_remapKeepsTheAnchorRowOnTop () {
		Table table = table (500);
		table.setTopIndex (200);
		long memento = M3ScrollMemento.save (table);
		/* 10 rows inserted above, 5 removed above, 3 inserted below */
		for (int i = 0; i < 10; i++) new TableItem (table, SWT.NONE, 50).setText ("new " + i);
		memento = M3ScrollMemento.remap (memento, 50, 10);
		table.remove (0, 4);
		memento = M3ScrollMemento.remap (memento, 0, -5);
		for (int i = 0; i < 3; i++) new TableItem (table, SWT.NONE, 400).setText ("below " + i);
		memento = M3ScrollMemento.remap (memento, 400, 3);
		table.setTopIndex (0);
		assertTrue (M3ScrollMemento.restore (table, memento));
		assertEquals ("row 200", table.getItem (table.getTopIndex ()).getText ());
		/* the anchor itself removed: the row that took its place is on top */
		int top = table.getTopIndex ();
		table.remove (top, top + 2);
		memento = M3ScrollMemento.remap (memento, top, -3);
		table.setTopIndex (0);
		M3ScrollMemento.restore (table, memento);
		assertEquals ("row 203", table.getItem (table.getTopIndex ()).getText ());
	}

	@Test
	public void test_restoreClampsToTheRows () {
		List list = new List (shell, SWT.SINGLE | SWT.V_SCROLL);
		list.setBounds (5, 5, 300, 380);
		for (int i = 0; i < 300; i++) list.add ("row " + i);
		list.setTopIndex (280);
		long memento = M3ScrollMemento.save (list);
		list.setItemCount (100);
		assertTrue (M3ScrollMemento.restore (list, memento));
		assertTrue (list.getTopIndex () <= 99);
		list.removeAll ();
		assertTrue (M3ScrollMemento.restore (list, memento));
		assertEquals (0, list.getItemCount ());
	}

	@Test
	public void test_treeAnchorIsAVisibleRow () {
		Tree tree = new Tree (shell, SWT.MULTI | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		for (int i = 0; i < 100; i++) {
			TreeItem root = new TreeItem (tree, SWT.NONE);
			root.setText ("root " + i);
			for (int j = 0; j < 3; j++) new TreeItem (root, SWT.NONE).setText ("root " + i + "." + j);
		}
		tree.getItem (10).setExpanded (true);
		tree.setTopItem (tree.getItem (10).getItem (1));
		long memento = M3ScrollMemento.save (tree);
		tree.setTopItem (tree.getItem (0));
		assertTrue (M3ScrollMemento.restore (tree, memento));
		assertEquals ("root 10.1", tree.getTopItem ().getText ());
	}

	@Test
	public void test_foreignLongsAreIgnored () {
		Table table = table (100);
		table.setTopIndex (30);
		for (long foreign : new long [] {0L, -1L, 30L, Long.MAX_VALUE, Long.MIN_VALUE, M3ScrollMemento.save (table) | 1L}) {
			assertFalse (M3ScrollMemento.restore (table, foreign), Long.toHexString (foreign));
			assertEquals (30, table.getTopIndex ());
			assertEquals (foreign, M3ScrollMemento.remap (foreign, 0, 5), "remap leaves a foreign long alone");
		}
	}

	@Test
	public void test_argumentsAreChecked () {
		Table table = table (3);
		long memento = M3ScrollMemento.save (table);
		assertThrows (IllegalArgumentException.class, () -> M3ScrollMemento.save ((Table) null));
		assertThrows (IllegalArgumentException.class, () -> M3ScrollMemento.save ((List) null));
		assertThrows (IllegalArgumentException.class, () -> M3ScrollMemento.save ((Tree) null));
		assertThrows (IllegalArgumentException.class, () -> M3ScrollMemento.restore ((Table) null, memento));
		assertThrows (IllegalArgumentException.class, () -> M3ScrollMemento.restore ((List) null, memento));
		assertThrows (IllegalArgumentException.class, () -> M3ScrollMemento.restore ((Tree) null, memento));
		assertThrows (IllegalArgumentException.class, () -> M3ScrollMemento.remap (memento, -1, 1));
		assertEquals (memento, M3ScrollMemento.remap (memento, 0, 0));
		table.dispose ();
		assertThrows (SWTException.class, () -> M3ScrollMemento.save (table));
		assertThrows (SWTException.class, () -> M3ScrollMemento.restore (table, memento));
	}
}

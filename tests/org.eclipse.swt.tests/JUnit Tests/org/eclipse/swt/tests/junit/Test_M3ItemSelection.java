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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3ItemSelection and M3Mode (enhanced M3 API, leaf 1). Every answer is checked
 * against a public-API oracle, so the class passes unchanged in every startup mode: the mode
 * differential of the leaf is this class run with <code>-Dswt.widget.mode=viewport</code> and
 * with <code>-Dswt.widget.mode=original</code>.
 */
public class Test_M3ItemSelection {
	static final int ROWS = 200;
	static final int ROOTS = 40;
	static final int CHILDREN = 4;
	Display display;
	Shell shell;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setBounds (20, 20, 320, 420);
	}

	@AfterEach
	public void tearDown () {
		shell.dispose ();
	}

	private Table table (int style) {
		Table table = new Table (shell, style | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		for (int i = 0; i < ROWS; i++) new TableItem (table, SWT.NONE).setText ("row " + i);
		shell.open ();
		flush ();
		return table;
	}

	private org.eclipse.swt.widgets.List list (int style) {
		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List (shell, style | SWT.BORDER | SWT.V_SCROLL);
		list.setBounds (5, 5, 300, 380);
		for (int i = 0; i < ROWS; i++) list.add ("row " + i);
		shell.open ();
		flush ();
		return list;
	}

	/* ROOTS roots; every third root has CHILDREN children; roots 0, 3 and 9 are expanded */
	private Tree tree (int style) {
		Tree tree = new Tree (shell, style | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		for (int i = 0; i < ROOTS; i++) {
			TreeItem root = new TreeItem (tree, SWT.NONE);
			root.setText ("root " + i);
			if (i % 3 != 0) continue;
			for (int j = 0; j < CHILDREN; j++) new TreeItem (root, SWT.NONE).setText ("root " + i + " / " + j);
		}
		tree.getItem (0).setExpanded (true);
		tree.getItem (3).setExpanded (true);
		tree.getItem (9).setExpanded (true);
		shell.open ();
		flush ();
		return tree;
	}

	private void flush () {
		while (display.readAndDispatch ()) {
		}
	}

	/* oracle: the visible rows of a tree in row order, by a recursive walk of the public API */
	private static void visibleRows (Tree tree, TreeItem parent, java.util.List<TreeItem> rows) {
		for (TreeItem item : parent == null ? tree.getItems () : parent.getItems ()) {
			rows.add (item);
			if (item.getExpanded ()) visibleRows (tree, item, rows);
		}
	}

	/* ------------------------------------------------------------------ Table */

	@Test
	public void test_tableSelectionRangesMatchIndices () {
		Table table = table (SWT.MULTI);
		assertEquals (0, M3ItemSelection.getSelectionRanges (table).length);
		table.setSelection (new int [] {3, 4, 5, 9, 20, 21});
		M3ItemRange [] ranges = M3ItemSelection.getSelectionRanges (table);
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (3, 5), new M3ItemRange (9, 9), new M3ItemRange (20, 21)}, ranges);
		assertEquals (table.getSelectionCount (), M3ItemRange.countOf (ranges));
		assertArrayEquals (M3ItemRange.coalesce (table.getSelectionIndices ()), ranges);
	}

	@Test
	public void test_tableSetSelectionRangesRoundTripWithoutScrolling () {
		Table table = table (SWT.MULTI);
		M3ItemRange [] ranges = {new M3ItemRange (2, 4), new M3ItemRange (150, 152), new M3ItemRange (199, 250)};
		M3ItemSelection.setSelectionRanges (table, ranges);
		assertArrayEquals (new int [] {2, 3, 4, 150, 151, 152, 199}, table.getSelectionIndices ());
		assertEquals (0, table.getTopIndex ());
		M3ItemSelection.setSelectionRanges (table, new M3ItemRange [] {M3ItemRange.EMPTY, new M3ItemRange (7, 7)});
		assertArrayEquals (new int [] {7}, table.getSelectionIndices ());
		M3ItemSelection.setSelectionRanges (table, new M3ItemRange [0]);
		assertEquals (0, table.getSelectionCount ());
	}

	@Test
	public void test_tableSelectAndDeselectRanges () {
		Table table = table (SWT.MULTI);
		M3ItemSelection.select (table, new M3ItemRange (10, 19));
		M3ItemSelection.select (table, new M3ItemRange (30, 31));
		M3ItemSelection.deselect (table, new M3ItemRange (12, 17));
		M3ItemSelection.deselect (table, M3ItemRange.EMPTY);
		M3ItemSelection.select (table, M3ItemRange.EMPTY);
		assertArrayEquals (new int [] {10, 11, 18, 19, 30, 31}, table.getSelectionIndices ());
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (10, 11), new M3ItemRange (18, 19), new M3ItemRange (30, 31)}, M3ItemSelection.getSelectionRanges (table));
		M3ItemSelection.select (table, new M3ItemRange (500, 600));
		assertEquals (6, table.getSelectionCount ());
	}

	@Test
	public void test_tableSingleSelectionRanges () {
		Table table = table (SWT.SINGLE);
		M3ItemSelection.setSelectionRanges (table, new M3ItemRange [] {new M3ItemRange (5, 5)});
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (5, 5)}, M3ItemSelection.getSelectionRanges (table));
	}

	@Test
	public void test_tableVisibleRangeMatchesItemBounds () {
		Table table = table (SWT.MULTI);
		assertVisibleRows (table, M3ItemSelection.getVisibleRange (table));
		table.setTopIndex (50);
		flush ();
		M3ItemRange scrolled = M3ItemSelection.getVisibleRange (table);
		assertEquals (table.getTopIndex (), scrolled.start ());
		assertVisibleRows (table, scrolled);
		table.setHeaderVisible (true);
		flush ();
		assertVisibleRows (table, M3ItemSelection.getVisibleRange (table));
		table.setTopIndex (ROWS - 1);
		flush ();
		M3ItemRange end = M3ItemSelection.getVisibleRange (table);
		assertEquals (ROWS - 1, end.end ());
		assertVisibleRows (table, end);
	}

	/* every row of the range intersects the item area and its neighbours do not */
	private void assertVisibleRows (Table table, M3ItemRange range) {
		assertFalse (range.isEmpty ());
		int top = table.getHeaderVisible () ? table.getHeaderHeight () : 0;
		int bottom = table.getClientArea ().height;
		for (int i = range.start (); i <= range.end (); i++) {
			Rectangle bounds = table.getItem (i).getBounds ();
			assertTrue (bounds.y < bottom && bounds.y + bounds.height > top, "row " + i + " " + bounds + " in " + range);
		}
		if (range.start () > 0) {
			Rectangle above = table.getItem (range.start () - 1).getBounds ();
			assertTrue (above.y + above.height <= top, "row above " + above);
		}
		if (range.end () + 1 < table.getItemCount ()) {
			Rectangle below = table.getItem (range.end () + 1).getBounds ();
			assertTrue (below.y >= bottom, "row below " + below);
		}
	}

	@Test
	public void test_tableVisibleRangeOfEmptyAndTinyTables () {
		Table table = table (SWT.MULTI);
		table.removeAll ();
		assertTrue (M3ItemSelection.getVisibleRange (table).isEmpty ());
		assertEquals (0, M3ItemSelection.getSelectionRanges (table).length);
		for (int i = 0; i < 3; i++) new TableItem (table, SWT.NONE).setText ("few " + i);
		flush ();
		assertEquals (new M3ItemRange (0, 2), M3ItemSelection.getVisibleRange (table));
		table.setSize (300, 0);
		flush ();
		assertTrue (M3ItemSelection.getVisibleRange (table).isEmpty ());
	}

	/* ------------------------------------------------------------------ List */

	@Test
	public void test_listSelectionRanges () {
		org.eclipse.swt.widgets.List list = list (SWT.MULTI);
		assertEquals (0, M3ItemSelection.getSelectionRanges (list).length);
		list.setSelection (new int [] {0, 1, 2, 40, 42});
		M3ItemRange [] ranges = M3ItemSelection.getSelectionRanges (list);
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (0, 2), new M3ItemRange (40, 40), new M3ItemRange (42, 42)}, ranges);
		assertArrayEquals (M3ItemRange.coalesce (list.getSelectionIndices ()), ranges);
		M3ItemSelection.setSelectionRanges (list, new M3ItemRange [] {new M3ItemRange (100, 102)});
		assertArrayEquals (new int [] {100, 101, 102}, list.getSelectionIndices ());
		assertEquals (0, list.getTopIndex ());
		M3ItemSelection.select (list, new M3ItemRange (5, 6));
		M3ItemSelection.deselect (list, new M3ItemRange (101, 101));
		M3ItemSelection.select (list, M3ItemRange.EMPTY);
		M3ItemSelection.deselect (list, M3ItemRange.EMPTY);
		assertArrayEquals (new int [] {5, 6, 100, 102}, list.getSelectionIndices ());
		M3ItemSelection.setSelectionRanges (list, new M3ItemRange [0]);
		assertEquals (0, list.getSelectionCount ());
	}

	@Test
	public void test_listVisibleRangeFollowsTopIndexAndHeight () {
		org.eclipse.swt.widgets.List list = list (SWT.MULTI);
		int itemHeight = list.getItemHeight ();
		M3ItemRange range = M3ItemSelection.getVisibleRange (list);
		assertEquals (0, range.start ());
		int rows = (list.getClientArea ().height + itemHeight - 1) / itemHeight;
		assertEquals (Math.min (ROWS, rows), range.size ());
		list.setTopIndex (60);
		flush ();
		range = M3ItemSelection.getVisibleRange (list);
		assertEquals (list.getTopIndex (), range.start ());
		assertEquals (Math.min (ROWS - range.start (), rows), range.size ());
		/* exact multiple: no partial row; one more pixel: one partial row */
		Rectangle trim = list.computeTrim (0, 0, 300, 5 * itemHeight);
		list.setSize (trim.width, trim.height);
		flush ();
		assertEquals (5, M3ItemSelection.getVisibleRange (list).size ());
		list.setSize (trim.width, trim.height + 1);
		flush ();
		assertEquals (6, M3ItemSelection.getVisibleRange (list).size ());
		list.removeAll ();
		assertTrue (M3ItemSelection.getVisibleRange (list).isEmpty ());
	}

	/* ------------------------------------------------------------------ Tree */

	@Test
	public void test_treeSelectionRangesPerParent () {
		Tree tree = tree (SWT.MULTI);
		TreeItem root3 = tree.getItem (3);
		tree.setSelection (new TreeItem [] {tree.getItem (5), tree.getItem (6), tree.getItem (7), tree.getItem (20), root3.getItem (1), root3.getItem (2)});
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (5, 7), new M3ItemRange (20, 20)}, M3ItemSelection.getSelectionRanges (tree, null));
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (1, 2)}, M3ItemSelection.getSelectionRanges (tree, root3));
		assertEquals (0, M3ItemSelection.getSelectionRanges (tree, tree.getItem (0)).length);
		assertEquals (0, M3ItemSelection.getSelectionRanges (tree, tree.getItem (1)).length);
	}

	@Test
	public void test_treeSetSelectionRangesReplacesTheSelection () {
		Tree tree = tree (SWT.MULTI);
		TreeItem root0 = tree.getItem (0);
		tree.setSelection (tree.getItem (12));
		flush ();
		TreeItem top = tree.getTopItem ();
		M3ItemSelection.setSelectionRanges (tree, root0, new M3ItemRange [] {new M3ItemRange (0, 1), new M3ItemRange (3, 9)});
		TreeItem [] selection = tree.getSelection ();
		assertEquals (3, selection.length);
		for (TreeItem item : selection) assertSame (root0, item.getParentItem ());
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (0, 1), new M3ItemRange (3, 3)}, M3ItemSelection.getSelectionRanges (tree, root0));
		assertEquals (0, M3ItemSelection.getSelectionRanges (tree, null).length);
		assertSame (top, tree.getTopItem ());
		M3ItemSelection.setSelectionRanges (tree, null, new M3ItemRange [0]);
		assertEquals (0, tree.getSelectionCount ());
	}

	@Test
	public void test_treeSelectAndDeselectRanges () {
		Tree tree = tree (SWT.MULTI);
		M3ItemSelection.select (tree, null, new M3ItemRange (30, 33));
		M3ItemSelection.select (tree, tree.getItem (9), new M3ItemRange (2, 3));
		M3ItemSelection.deselect (tree, null, new M3ItemRange (31, 32));
		M3ItemSelection.select (tree, null, M3ItemRange.EMPTY);
		M3ItemSelection.deselect (tree, null, M3ItemRange.EMPTY);
		M3ItemSelection.deselect (tree, tree.getItem (9), new M3ItemRange (40, 50));
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (30, 30), new M3ItemRange (33, 33)}, M3ItemSelection.getSelectionRanges (tree, null));
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (2, 3)}, M3ItemSelection.getSelectionRanges (tree, tree.getItem (9)));
		assertEquals (4, tree.getSelectionCount ());
	}

	@Test
	public void test_treeVisibleRangeAndItemsMatchTheRowOrder () {
		Tree tree = tree (SWT.MULTI);
		java.util.List<TreeItem> rows = new ArrayList<> ();
		visibleRows (tree, null, rows);
		assertEquals (ROOTS + 3 * CHILDREN, rows.size ());
		assertVisibleTreeRows (tree, rows, M3ItemSelection.getVisibleRange (tree));
		tree.setTopItem (tree.getItem (12));
		flush ();
		M3ItemRange scrolled = M3ItemSelection.getVisibleRange (tree);
		assertEquals (rows.indexOf (tree.getTopItem ()), scrolled.start ());
		assertVisibleTreeRows (tree, rows, scrolled);
		tree.setHeaderVisible (true);
		flush ();
		assertVisibleTreeRows (tree, rows, M3ItemSelection.getVisibleRange (tree));
		tree.setTopItem (tree.getItem (ROOTS - 1));
		flush ();
		M3ItemRange end = M3ItemSelection.getVisibleRange (tree);
		assertEquals (rows.size () - 1, end.end ());
		assertVisibleTreeRows (tree, rows, end);
	}

	/* the range names the rows of the oracle that intersect the item area; the items agree */
	private void assertVisibleTreeRows (Tree tree, java.util.List<TreeItem> rows, M3ItemRange range) {
		assertFalse (range.isEmpty ());
		int top = tree.getHeaderVisible () ? tree.getHeaderHeight () : 0;
		int bottom = tree.getClientArea ().height;
		TreeItem [] items = M3ItemSelection.getVisibleItems (tree);
		assertEquals (range.size (), items.length);
		for (int i = range.start (); i <= range.end (); i++) {
			assertSame (rows.get (i), items [i - range.start ()]);
			Rectangle bounds = rows.get (i).getBounds ();
			assertTrue (bounds.y < bottom && bounds.y + bounds.height > top, "row " + i + " " + bounds + " in " + range);
		}
		if (range.start () > 0) {
			Rectangle above = rows.get (range.start () - 1).getBounds ();
			assertTrue (above.y + above.height <= top, "row above " + above);
		}
		if (range.end () + 1 < rows.size ()) {
			Rectangle below = rows.get (range.end () + 1).getBounds ();
			assertTrue (below.y >= bottom, "row below " + below);
		}
	}

	@Test
	public void test_treeVisibleRangeOfEmptyAndCollapsedTrees () {
		Tree tree = tree (SWT.MULTI);
		tree.getItem (0).setExpanded (false);
		tree.getItem (3).setExpanded (false);
		tree.getItem (9).setExpanded (false);
		flush ();
		M3ItemRange range = M3ItemSelection.getVisibleRange (tree);
		assertEquals (0, range.start ());
		assertEquals (range.size (), M3ItemSelection.getVisibleItems (tree).length);
		tree.removeAll ();
		assertTrue (M3ItemSelection.getVisibleRange (tree).isEmpty ());
		assertEquals (0, M3ItemSelection.getVisibleItems (tree).length);
		assertEquals (0, M3ItemSelection.getSelectionRanges (tree, null).length);
	}

	/* ------------------------------------------------------------------ errors */

	@Test
	public void test_argumentsAreChecked () {
		Table table = table (SWT.MULTI);
		Tree tree = new Tree (shell, SWT.MULTI);
		Tree other = new Tree (shell, SWT.MULTI);
		TreeItem foreign = new TreeItem (other, SWT.NONE);
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.getSelectionRanges ((Table) null));
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.getSelectionRanges ((org.eclipse.swt.widgets.List) null));
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.getSelectionRanges (null, null));
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.getVisibleRange ((Tree) null));
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.getVisibleItems (null));
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.setSelectionRanges (table, null));
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.setSelectionRanges (table, new M3ItemRange [] {null}));
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.select (table, null));
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.deselect (table, null));
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.getSelectionRanges (tree, foreign));
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.select (tree, foreign, M3ItemRange.EMPTY));
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.deselect (tree, null, null));
		assertThrows (IllegalArgumentException.class, () -> M3ItemSelection.setSelectionRanges (tree, null, null));
		table.dispose ();
		assertThrows (org.eclipse.swt.SWTException.class, () -> M3ItemSelection.getVisibleRange (table));
	}

	/* ------------------------------------------------------------------ M3Mode */

	@Test
	public void test_modeFollowsTheStartupProperties () {
		Table table = table (SWT.MULTI);
		Tree tree = new Tree (shell, SWT.NONE);
		Label label = new Label (shell, SWT.NONE);
		assertEquals (expectedMode ("Tree", true), M3Mode.of (tree));
		assertEquals (expectedMode ("Table", false), M3Mode.of (table));
		assertEquals (M3Mode.ORIGINAL, M3Mode.of (label));
		assertEquals ("viewport", M3Mode.name (M3Mode.VIEWPORT));
		assertEquals ("original", M3Mode.name (M3Mode.ORIGINAL));
		assertEquals ("custom", M3Mode.name (M3Mode.CUSTOM));
		assertEquals ("m3", M3Mode.name (M3Mode.M3));
		assertEquals ("gpu", M3Mode.name (M3Mode.GPU));
		assertThrows (IllegalArgumentException.class, () -> M3Mode.name (5));
		assertThrows (IllegalArgumentException.class, () -> M3Mode.name (-1));
		assertThrows (IllegalArgumentException.class, () -> M3Mode.of (null));
		label.dispose ();
		assertThrows (org.eclipse.swt.SWTException.class, () -> M3Mode.of (label));
	}

	/* the gate rule: the per-widget property, else the global one; unsupported drawn modes fall back to viewport */
	private static int expectedMode (String widget, boolean drawn) {
		String value = System.getProperty ("swt.widget.mode." + widget, System.getProperty ("swt.widget.mode", "viewport"));
		String name = value.trim ().toLowerCase (Locale.ROOT);
		if (name.equals ("original")) return M3Mode.ORIGINAL;
		if (!drawn) return M3Mode.VIEWPORT;
		if (name.equals ("custom")) return M3Mode.CUSTOM;
		if (name.equals ("m3")) return M3Mode.M3;
		if (name.equals ("gpu")) return M3Mode.GPU;
		return M3Mode.VIEWPORT;
	}
}

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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.m3.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Widget tests of the M3 sort and filter API on Table and Tree: header clicks and indicators,
 * virtual bindings, the filter popup and the filter row. Run on the hidden test desktop.
 */
@SuppressWarnings ("nls")
public class Test_M3SortFilterWidgets {

	static final int ROWS = 20_000;
	Display display;
	Shell shell;
	int [] numbers;
	String [] names;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setLayout (new GridLayout ());
		shell.setSize (500, 400);
		Random random = new Random (3);
		numbers = new int [ROWS];
		names = new String [ROWS];
		for (int i = 0; i < ROWS; i++) {
			numbers [i] = random.nextInt (50);
			names [i] = "n" + random.nextInt (1000);
		}
	}

	@AfterEach
	public void tearDown () {
		shell.dispose ();
		drain ();
	}

	void drain () {
		while (display.readAndDispatch ()) {
			/* run pending events */
		}
	}

	void waitFor (BooleanSupplier condition) {
		long end = System.currentTimeMillis () + 5000;
		while (!condition.getAsBoolean () && System.currentTimeMillis () < end) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		assertTrue (condition.getAsBoolean ());
	}

	Table table (int style) {
		Table table = new Table (shell, style | SWT.VIRTUAL | SWT.BORDER);
		table.setHeaderVisible (true);
		table.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		for (String title : new String [] {"Number", "Name", "Row"}) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (title);
			column.setWidth (120);
		}
		return table;
	}

	String cell (int row, int column) {
		return column == 0 ? String.valueOf (numbers [row]) : column == 1 ? names [row] : "r" + row;
	}

	M3ColumnSort sort (M3RowView input) {
		M3ColumnSort sort = input == null ? new M3ColumnSort (3, ROWS) : new M3ColumnSort (3, input);
		sort.setKey (0, M3ColumnKey.ofInts (r -> numbers [r]));
		sort.setCellText (this::cell);
		return sort;
	}

	static void click (Item column, boolean shift) {
		Event event = new Event ();
		event.stateMask = shift ? SWT.SHIFT : 0;
		column.notifyListeners (SWT.Selection, event);
	}

	static int [] fills = new int [1];

	M3RowBinding bind (Table table, M3RowView view) {
		return M3RowBinding.bind (table, view, (item, row) -> {
			fills [0]++;
			for (int c = 0; c < 3; c++) item.setText (c, cell (row, c));
		});
	}

	@Test
	public void test_headerClicksSortWithShiftAndShowPriorities () {
		Table table = table (SWT.NONE);
		Image own = new Image (display, 8, 8);
		table.getColumn (2).setImage (own);
		M3ColumnSort sort = sort (null);
		bind (table, sort);
		sort.install (table);
		sort.install (table);
		shell.open ();
		drain ();
		click (table.getColumn (0), false);
		assertEquals (SWT.UP, sort.getDirection (0));
		assertSame (table.getColumn (0), table.getSortColumn ());
		assertEquals (SWT.UP, table.getSortDirection ());
		assertNull (table.getColumn (0).getImage ());
		click (table.getColumn (1), true);
		click (table.getColumn (2), true);
		assertEquals (3, sort.getSortKeyCount ());
		Image mark = table.getColumn (1).getImage ();
		assertNotNull (mark);
		assertNotSame (own, table.getColumn (2).getImage ());
		ImageData data = mark.getImageData (100);
		assertEquals (20, data.width);
		assertEquals (10, data.height);
		int empty = data.getPixel (0, 0);
		assertNotEquals (empty, data.getPixel (3, 3), "arrow apex");
		assertNotEquals (empty, data.getPixel (9, 2), "digit 2 top");
		assertEquals (empty, data.getPixel (16, 5), "no filter mark");
		assertEquals (40, mark.getImageData (200).width);
		click (table.getColumn (2), true);
		click (table.getColumn (2), true);
		assertSame (own, table.getColumn (2).getImage ());
		String first = table.getItem (0).getText (0);
		assertEquals (cell (sort.map (0), 0), first);
		click (table.getColumn (0), false);
		assertEquals (SWT.DOWN, table.getSortDirection ());
		assertNull (table.getColumn (1).getImage ());
		click (table.getColumn (0), false);
		assertNull (table.getSortColumn ());
		assertEquals (SWT.NONE, table.getSortDirection ());
		TableColumn extra = new TableColumn (table, SWT.NONE);
		sort.install (table);
		click (extra, false);
		assertEquals (0, sort.getSortKeyCount ());
		M3ColumnSort other = sort (null);
		other.install (table);
		click (table.getColumn (1), false);
		assertEquals (1, other.getSortKeyCount ());
		assertEquals (0, sort.getSortKeyCount ());
		table.dispose ();
		other.toggle (1, false);
		own.dispose ();
	}

	@Test
	public void test_virtualTableAsksOnlyForResidentRows () {
		Table table = table (SWT.NONE);
		M3ColumnFilter filter = new M3ColumnFilter (3, ROWS, this::cell);
		M3ColumnSort sort = sort (filter);
		fills [0] = 0;
		M3RowBinding binding = bind (table, sort);
		assertSame (sort, binding.getView ());
		shell.open ();
		drain ();
		int visible = table.getClientArea ().height / table.getItemHeight () + 2;
		assertTrue (fills [0] <= visible * 2, "fills " + fills [0]);
		assertEquals (ROWS, table.getItemCount ());
		fills [0] = 0;
		sort.toggle (1, false);
		drain ();
		table.getItem (0).getText ();
		assertTrue (fills [0] <= visible * 2, "fills after sort " + fills [0]);
		assertEquals (cell (sort.map (0), 1), table.getItem (0).getText (1));
		filter.setText (1, "n1*", M3TextMatch.WILDCARD);
		drain ();
		assertEquals (sort.size (), table.getItemCount ());
		assertTrue (table.getItem (0).getText (1).startsWith ("n1"));
		numbers [sort.map (0)] = -1;
		sort.rowsChanged (sort.map (0));
		filter.setSourceSize (ROWS);
		drain ();
		assertEquals (sort.size (), table.getItemCount ());
		binding.dispose ();
		sort.toggle (1, false);
		assertNotEquals (cell (sort.map (0), 1), table.getItem (0).getText (1));
		assertThrows (IllegalArgumentException.class, () -> M3RowBinding.bind (new Table (shell, SWT.NONE), sort, (item, row) -> {}));
		assertThrows (IllegalArgumentException.class, () -> M3RowBinding.bind ((Table) null, sort, (item, row) -> {}));
		assertThrows (IllegalArgumentException.class, () -> M3RowBinding.bind (table, null, (item, row) -> {}));
		assertThrows (IllegalArgumentException.class, () -> M3RowBinding.bind (table, sort, null));
		M3RowBinding again = bind (table, sort);
		table.dispose ();
		sort.toggle (1, false);
		again.dispose ();
	}

	@Test
	public void test_virtualTreeFollowsTheSortedFilteredTree () {
		/* 0 [1 [2, 3], 4], 5 [6], 7 */
		int [] parents = {-1, 0, 1, 1, 0, -1, 5, -1};
		String [] labels = {"root", "dir", "match", "amatch", "file", "top", "x", "lone"};
		Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.BORDER);
		tree.setHeaderVisible (true);
		new TreeColumn (tree, SWT.NONE).setWidth (200);
		M3ColumnFilter filter = new M3ColumnFilter (1, 8, (row, column) -> labels [row]);
		filter.setParents (r -> parents [r]);
		M3ColumnSort sort = new M3ColumnSort (1, filter);
		sort.setParents (r -> parents [r]);
		sort.setCellText ((row, column) -> labels [row]);
		assertThrows (IllegalArgumentException.class, () -> M3RowBinding.bind (tree, new M3ColumnSort (1, 1), (item, row) -> {}));
		M3RowBinding.bind (tree, sort, (item, row) -> item.setText (labels [row]));
		sort.install (tree);
		M3FilterPopup popup = M3FilterPopup.install (tree, filter);
		shell.open ();
		drain ();
		assertEquals (3, tree.getItemCount ());
		click (tree.getColumn (0), false);
		drain ();
		assertEquals ("lone", tree.getItem (0).getText ());
		filter.setText (0, "*match", M3TextMatch.WILDCARD);
		drain ();
		assertEquals (1, tree.getItemCount ());
		TreeItem root = tree.getItem (0);
		assertEquals ("root", root.getText ());
		assertEquals (0, M3RowBinding.sourceRow (root));
		assertEquals (1, root.getItemCount ());
		TreeItem dir = root.getItem (0);
		assertEquals (2, dir.getItemCount ());
		assertEquals ("amatch", dir.getItem (0).getText ());
		assertEquals (-1, M3RowBinding.sourceRow (new TreeItem (new Tree (shell, SWT.NONE), SWT.NONE)));
		assertThrows (IllegalArgumentException.class, () -> M3RowBinding.sourceRow (null));
		assertNotNull (tree.getColumn (0).getImage (), "filter mark");
		popup.open (0);
		assertTrue (popup.isOpen ());
		popup.dispose ();
		assertFalse (popup.isOpen ());
		assertThrows (IllegalArgumentException.class, () -> M3FilterPopup.install ((Tree) null, filter));
		assertThrows (IllegalArgumentException.class, () -> M3FilterPopup.install (tree, null));
	}

	static <T extends Control> T find (Composite parent, Class<T> type, int index) {
		java.util.List<T> found = new ArrayList<> ();
		collect (parent, type, found);
		return found.get (index);
	}

	static <T extends Control> void collect (Composite parent, Class<T> type, java.util.List<T> found) {
		for (Control child : parent.getChildren ()) {
			if (type.isInstance (child)) found.add (type.cast (child));
			if (child instanceof Composite composite) collect (composite, type, found);
		}
	}

	static void traverse (Control control, int detail) {
		Event event = new Event ();
		event.detail = detail;
		event.doit = true;
		control.notifyListeners (SWT.Traverse, event);
	}

	static void select (ToolItem item) {
		item.notifyListeners (SWT.Selection, new Event ());
	}

	@Test
	public void test_filterPopupEditsTheColumnFilter () {
		Table table = table (SWT.NONE);
		M3ColumnFilter filter = new M3ColumnFilter (2, ROWS, this::cell);
		M3ColumnSort sort = sort (filter);
		bind (table, sort);
		M3FilterPopup popup = M3FilterPopup.install (table, filter);
		assertNotSame (popup, M3FilterPopup.install (table, filter));
		popup = M3FilterPopup.install (table, filter);
		shell.open ();
		drain ();
		M3FilterPopup installed = popup;
		assertThrows (IllegalArgumentException.class, () -> installed.open (2));
		popup.open (1);
		assertEquals (1, popup.getColumn ());
		Shell window = popup.getShell ();
		Combo enablement = find (window, Combo.class, 0), type = find (window, Combo.class, 1);
		Text text = find (window, Text.class, 0);
		Label feedback = find (window, Label.class, 0);
		assertEquals (0, enablement.getSelectionIndex ());
		assertFalse (text.getEnabled ());
		assertEquals ("*", text.getText ());
		enablement.select (1);
		enablement.notifyListeners (SWT.Modify, new Event ());
		text.setText ("n1*");
		waitFor (() -> filter.isActive (1));
		assertTrue (sort.size () < ROWS);
		assertNotNull (table.getColumn (1).getImage (), "filter mark");
		type.select (2);
		text.setText ("(");
		traverse (text, SWT.TRAVERSE_RETURN);
		assertFalse (popup.isOpen ());
		assertEquals ("n1*", filter.getMatch (1).pattern ());
		popup.open (1);
		window = popup.getShell ();
		type = find (window, Combo.class, 1);
		text = find (window, Text.class, 0);
		feedback = find (window, Label.class, 0);
		assertEquals ("n1*", text.getText ());
		assertEquals (0, type.getSelectionIndex ());
		type.select (2);
		text.setText ("(");
		assertFalse (feedback.isDisposed ());
		ToolItem [] tools = find (window, ToolBar.class, 0).getItems ();
		select (tools [0]);
		assertEquals (".*", text.getText ());
		type.select (4);
		select (tools [0]);
		assertEquals ("", text.getText ());
		text.setText ("n2");
		traverse (text, SWT.TRAVERSE_TAB_NEXT);
		traverse (text, SWT.TRAVERSE_ESCAPE);
		assertFalse (popup.isOpen ());
		assertEquals ("n2", filter.getMatch (1).pattern ());
		popup.open (1);
		select (find (popup.getShell (), ToolBar.class, 0).getItem (1));
		select (find (popup.getShell (), ToolBar.class, 0).getItem (2));
		assertFalse (popup.isOpen ());
		popup.open (1);
		enablement = find (popup.getShell (), Combo.class, 0);
		enablement.select (0);
		enablement.notifyListeners (SWT.Modify, new Event ());
		waitFor (() -> !filter.isActive (1));
		popup.close ();
		assertNull (table.getColumn (1).getImage ());
		filter.setText (0, "1", M3TextMatch.STARTS_WITH | M3TextMatch.NEGATE);
		popup.open (0);
		assertEquals (0, find (popup.getShell (), Combo.class, 1).getSelectionIndex ());
		popup.close ();
		filter.setText (0, "1", M3TextMatch.CONTAINS);
		popup.open (0);
		assertEquals (4, find (popup.getShell (), Combo.class, 1).getSelectionIndex ());
		table.notifyListeners (SWT.MouseDown, new Event ());
		assertFalse (popup.isOpen ());
		popup.open (0);
		shell.notifyListeners (SWT.Move, new Event ());
		assertFalse (popup.isOpen ());
		popup.open (0);
		popup.getShell ().notifyListeners (SWT.Deactivate, new Event ());
		shell.setActive ();
		waitFor (() -> !installed.isOpen ());
		M3FilterPopup last = popup;
		Event menu = new Event ();
		menu.detail = SWT.MENU_MOUSE;
		int top = table.getClientArea ().y;
		Point header = table.toDisplay (130, top + 2);
		menu.x = header.x;
		menu.y = header.y;
		table.notifyListeners (SWT.MenuDetect, menu);
		assertEquals (1, last.getColumn ());
		assertFalse (menu.doit);
		last.close ();
		menu.doit = true;
		menu.x = table.toDisplay (370, top + 2).x;
		table.notifyListeners (SWT.MenuDetect, menu);
		assertTrue (menu.doit);
		menu.x = table.toDisplay (1000, top + 2).x;
		table.notifyListeners (SWT.MenuDetect, menu);
		menu.x = table.toDisplay (130, top + 2).x;
		menu.y = table.toDisplay (0, top + table.getHeaderHeight () + 5).y;
		table.notifyListeners (SWT.MenuDetect, menu);
		menu.y = table.toDisplay (0, top - 1).y;
		table.notifyListeners (SWT.MenuDetect, menu);
		menu.y = header.y;
		menu.detail = SWT.MENU_KEYBOARD;
		table.notifyListeners (SWT.MenuDetect, menu);
		assertFalse (last.isOpen ());
		last.open (0);
		table.dispose ();
		assertFalse (last.isOpen ());
		last.dispose ();
	}

	@Test
	public void test_filterRowFollowsColumnsAndFilters () {
		M3ColumnFilter filter = new M3ColumnFilter (3, ROWS, this::cell);
		Table table = table (SWT.H_SCROLL);
		M3FilterRow row = new M3FilterRow (shell, table, filter);
		row.moveAbove (table);
		row.setLayoutData (new GridData (SWT.FILL, SWT.TOP, true, false));
		M3ColumnSort sort = sort (filter);
		bind (table, sort);
		shell.layout (true, true);
		shell.open ();
		drain ();
		assertEquals (table.getColumn (1).getWidth (), row.getText (1).getSize ().x);
		int x = row.getText (1).getLocation ().x;
		table.getColumn (0).setWidth (150);
		drain ();
		assertEquals (x + 30, row.getText (1).getLocation ().x);
		table.setColumnOrder (new int [] {1, 0, 2});
		drain ();
		assertTrue (row.getText (1).getLocation ().x < row.getText (0).getLocation ().x);
		table.getHorizontalBar ().notifyListeners (SWT.Selection, new Event ());
		row.getText (1).setText ("N12");
		waitFor (() -> filter.isActive (1));
		assertTrue (table.getItem (0).getText (1).contains ("12"));
		assertNotNull (table.getColumn (1).getImage ());
		row.getText (1).setText ("");
		waitFor (() -> !filter.isActive (1));
		new TableColumn (table, SWT.NONE).setWidth (40);
		row.refresh ();
		assertThrows (IllegalArgumentException.class, () -> row.getText (3));
		assertEquals (row.getText (0).computeSize (SWT.DEFAULT, SWT.DEFAULT).y, row.computeSize (SWT.DEFAULT, SWT.DEFAULT).y);
		assertEquals (77, row.computeSize (77, 9).x);
		assertEquals (9, row.computeSize (77, 9).y);
		M3ColumnFilter none = new M3ColumnFilter (0, 1, null);
		assertEquals (0, new M3FilterRow (shell, table, none).computeSize (SWT.DEFAULT, SWT.DEFAULT).y);
		Tree tree = new Tree (shell, SWT.NONE);
		new TreeColumn (tree, SWT.NONE).setWidth (50);
		M3FilterRow treeRow = new M3FilterRow (shell, tree, filter);
		treeRow.getText (0).setText ("x");
		treeRow.dispose ();
		tree.dispose ();
		row.getText (0).setText ("1");
		table.dispose ();
		assertTrue (row.isDisposed ());
		assertThrows (IllegalArgumentException.class, () -> new M3FilterRow (shell, (Table) null, filter));
		assertThrows (IllegalArgumentException.class, () -> new M3FilterRow (shell, (Tree) null, filter));
		assertThrows (IllegalArgumentException.class, () -> new M3FilterRow (shell, new Table (shell, SWT.NONE), null));
	}
}

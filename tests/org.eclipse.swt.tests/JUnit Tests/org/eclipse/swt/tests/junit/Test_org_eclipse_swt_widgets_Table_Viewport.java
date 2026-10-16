/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Table as a viewport (docs/m3/TABLE_MEDIATED_VIEWPORT.md): the win32 ListView of every Table is
 * owner data, so the native control holds no item per row, while the public answers, the scroll
 * bar and the pixels are those of a stock Table holding the same rows. The rules checked here
 * were measured on the native control by the oracle (docs/m3/table-viewport/evidence).
 */
public class Test_org_eclipse_swt_widgets_Table_Viewport {
	static final int MILLION = 1_000_000;
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

	/* the owner-data style of a plain Table is the cheap, non-blocking sign of the viewport */
	private Table plainTable (int style) {
		Table table = new Table (shell, style | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()) && (OS.GetWindowLong (table.handle, OS.GWL_STYLE) & OS.LVS_OWNERDATA) != 0,
				"Table viewport mediation is not active in this build");
		return table;
	}

	private void settle () {
		long end = System.currentTimeMillis () + 200;
		while (System.currentTimeMillis () < end) {
			if (!display.readAndDispatch ()) {
				try {
					Thread.sleep (5);
				} catch (InterruptedException e) {
					Thread.currentThread ().interrupt ();
					return;
				}
			}
		}
	}

	private static int nativeCount (Table table) {
		return (int) OS.SendMessage (table.handle, OS.LVM_GETITEMCOUNT, 0, 0);
	}

	private static int focusIndex (Table table) {
		return Test_org_eclipse_swt_widgets_Table_ViewportDifferential.focusIndex (table);
	}

	@Test
	@Timeout (120)
	public void aMillionPlainRowsHaveALogicalBar () {
		Table table = plainTable (SWT.MULTI);
		table.setItemCount (MILLION);
		shell.open ();
		settle ();
		assertEquals (MILLION, table.getItemCount ());
		assertEquals (MILLION, nativeCount (table), "one owner-data count, no native item per row");
		ScrollBar bar = table.getVerticalBar ();
		assertEquals (MILLION - 1, bar.getMaximum (), "bar maximum covers every row");
		table.setTopIndex (MILLION - 10);
		settle ();
		/* the native list keeps its last page full: the top is clamped to count - rows per page */
		int page = (int) OS.SendMessage (table.handle, OS.LVM_GETCOUNTPERPAGE, 0, 0);
		assertEquals (Math.min (MILLION - 10, MILLION - page), table.getTopIndex ());
		TableItem last = table.getItem (MILLION - 1);
		assertSame (last, table.getItem (MILLION - 1), "the same row answers the same TableItem");
		assertEquals (MILLION - 1, table.indexOf (last));
	}

	@Test
	public void identitySurvivesInsertsAndRemovals () {
		Table table = plainTable (SWT.MULTI);
		for (int i = 0; i < 100; i++) new TableItem (table, SWT.NONE).setText ("row " + i);
		TableItem item = table.getItem (10);
		new TableItem (table, SWT.NONE, 0).setText ("first");
		assertEquals (11, table.indexOf (item));
		assertSame (item, table.getItem (11));
		table.remove (0, 4);
		assertEquals (6, table.indexOf (item));
		table.getItem (0).dispose ();
		assertEquals (5, table.indexOf (item));
		assertEquals ("row 10", table.getItem (5).getText ());
		assertEquals (95, nativeCount (table), "100 rows, one inserted, five removed, one disposed");
	}

	@Test
	public void selectionFocusAndMarkShiftWithInsertsLikeNativeRows () {
		Table table = plainTable (SWT.MULTI);
		for (int i = 0; i < 50; i++) new TableItem (table, SWT.NONE).setText ("row " + i);
		table.setSelection (new int [] {20, 10, 11});
		assertEquals (20, focusIndex (table));
		new TableItem (table, SWT.NONE, 5).setText ("inserted");
		assertArrayEquals (new int [] {11, 12, 21}, sortedSelection (table));
		assertEquals (21, focusIndex (table));
		table.remove (21);
		assertArrayEquals (new int [] {11, 12}, sortedSelection (table));
	}

	/* oracle case B and L: a count shrink moves a focus beyond the new end to the new last row */
	@Test
	public void shrinkingTheCountMovesTheFocusToTheNewLastRow () {
		Table table = plainTable (SWT.MULTI);
		for (int i = 0; i < 50; i++) new TableItem (table, SWT.NONE).setText ("row " + i);
		table.setSelection (40);
		table.select (30);
		table.setItemCount (35);
		assertEquals (34, focusIndex (table));
		assertArrayEquals (new int [] {30}, sortedSelection (table));
		assertEquals (35, nativeCount (table));
	}

	/* oracle case F: LVN_ODFINDITEM answered like the native incremental search */
	@Test
	public void typeAheadSelectsLikeTheNativeList () {
		Table table = plainTable (SWT.SINGLE);
		String [] words = {"apple", "Apricot", "banana", "berry", "Blueberry", "cherry"};
		for (int i = 0; i < 30; i++) new TableItem (table, SWT.NONE).setText (words [i % words.length] + " " + i);
		shell.open ();
		table.setFocus ();
		settle ();
		/* posted, as typed characters arrive: the incremental search times its keys by the message time */
		OS.PostMessage (table.handle, OS.WM_CHAR, 'b', 0);
		settle ();
		assertEquals (2, table.getSelectionIndex ());
		OS.PostMessage (table.handle, OS.WM_CHAR, 'b', 0);
		settle ();
		assertEquals (3, table.getSelectionIndex (), "a repeated character moves to the next match");
	}

	/* oracle case G: owner data measures only the rows in view; pack measures every row, as the stock Table does */
	@Test
	public void packMeasuresRowsOutOfView () {
		Table table = plainTable (SWT.MULTI);
		TableColumn column = new TableColumn (table, SWT.NONE);
		for (int i = 0; i < 300; i++) new TableItem (table, SWT.NONE).setText ("row " + i);
		table.getItem (250).setText ("an unusually long row text to widen the column xxxxxxxxxxxxxxxxxx");
		shell.open ();
		settle ();
		table.setTopIndex (240);
		column.pack ();
		int inView = column.getWidth ();
		column.setWidth (20);
		table.setTopIndex (0);
		column.pack ();
		assertEquals (inView, column.getWidth (), "pack with the long row out of view");
		assertEquals (0, table.getTopIndex (), "pack keeps the top row");
		assertTrue (inView > 200, "the long row was measured: " + inView);
	}

	/* a plain row has a facade only once an API call asked for it (or a paint listener needs one) */
	@Test
	public void setItemCountCreatesNoFacadeUntilAsked () throws ReflectiveOperationException {
		Table table = plainTable (SWT.MULTI);
		table.setItemCount (MILLION);
		shell.open ();
		settle ();
		assertEquals (0, facades (table), "painting the rows in view needs no facade");
		TableItem item = table.getItem (MILLION / 2);
		assertEquals (1, facades (table));
		assertSame (item, table.getItem (MILLION / 2), "the facade stays: the row keeps its identity");
		assertEquals ("", item.getText ());
		item.setText ("middle");
		table.setTopIndex (MILLION / 2 - 3);
		settle ();
		assertEquals (1, facades (table));
		assertEquals ("middle", table.getItem (MILLION / 2).getText ());
	}

	private static int facades (Table table) throws ReflectiveOperationException {
		java.lang.reflect.Field field = Table.class.getDeclaredField ("items");
		field.setAccessible (true);
		int count = 0;
		for (Object item : (Object []) field.get (table)) {
			if (item != null) count++;
		}
		return count;
	}

	/* stock indexOf scans the rows: a loop over indexOf after an insert at the front costs O(n) per call */
	@Test
	@Timeout (60)
	public void indexOfAfterAnInsertIsConstantTime () {
		Table table = plainTable (SWT.MULTI);
		int rows = 200_000;
		table.setItemCount (rows);
		TableItem [] items = table.getItems ();
		new TableItem (table, SWT.NONE, 0).setText ("first");
		long t0 = System.nanoTime ();
		for (int i = rows - 1; i >= 0; i--) assertEquals (i + 1, table.indexOf (items [i]));
		long millis = (System.nanoTime () - t0) / 1_000_000;
		assertTrue (millis < 2_000, "indexOf over " + rows + " rows took " + millis + " ms");
	}

	/* the viewport keeps native pixels: a plain viewport Table paints like a stock owner-data (virtual) Table */
	@Test
	public void pixelsMatchAStockOwnerDataTable () {
		Table plain = plainTable (SWT.MULTI | SWT.FULL_SELECTION);
		Table virtual = new Table (shell, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.VIRTUAL);
		virtual.setBounds (5, 5, 300, 380);
		for (Table table : new Table [] {plain, virtual}) {
			table.setHeaderVisible (true);
			table.setLinesVisible (true);
			for (int c = 0; c < 2; c++) {
				TableColumn column = new TableColumn (table, SWT.NONE);
				column.setText ("column " + c);
				column.setWidth (120);
			}
		}
		for (int i = 0; i < 200; i++) new TableItem (plain, SWT.NONE).setText (new String [] {"row " + i, "cell " + i});
		virtual.addListener (SWT.SetData, event -> ((TableItem) event.item).setText (new String [] {"row " + event.index, "cell " + event.index}));
		virtual.setItemCount (200);
		shell.open ();
		for (Table table : new Table [] {plain, virtual}) {
			table.setSelection (new int [] {3, 4, 120});
			table.setTopIndex (100);
		}
		assertEquals (pixels (virtual, plain), pixels (plain, virtual));
	}

	private String pixels (Table shown, Table hidden) {
		display.setCursorLocation (shell.getBounds ().x + shell.getBounds ().width + 40, shell.getBounds ().y);
		hidden.setVisible (false);
		shown.setVisible (true);
		shown.setFocus ();
		settle ();
		ImageData data = Test_org_eclipse_swt_widgets_Table_ViewportDifferential.print (display, shown);
		hidden.setVisible (true);
		return ViewportDifferential.hash (data);
	}

	private static int [] sortedSelection (Table table) {
		int [] indices = table.getSelectionIndices ();
		Arrays.sort (indices);
		return indices;
	}
}

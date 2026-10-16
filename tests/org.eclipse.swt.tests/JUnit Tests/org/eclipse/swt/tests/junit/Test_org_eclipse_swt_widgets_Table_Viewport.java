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
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IAccessible;
import org.eclipse.swt.internal.ole.win32.VARIANT;
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

	/*
	 * Columnar lanes: the state of a row outlives its facade. Facades nobody holds are released (only
	 * the most recent ones stay held), rows answer new facades loaded from the lanes, and a facade the
	 * application holds keeps its identity.
	 */
	@Test
	public void rowsKeepTheirStateWhenTheirFacadesAreReleased () throws ReflectiveOperationException {
		Table table = plainTable (SWT.MULTI | SWT.CHECK);
		int rows = 5_000;
		table.setItemCount (rows);
		Color red = display.getSystemColor (SWT.COLOR_RED);
		for (int i = 0; i < rows; i++) {
			TableItem item = table.getItem (i);
			item.setText ("row " + i);
			if (i % 7 == 0) item.setChecked (true);
			if (i % 11 == 0) item.setBackground (red);
		}
		TableItem held = table.getItem (10);
		collectGarbage ();
		assertTrue (facades (table) < rows / 2, "facades held strongly after the writes: " + facades (table));
		assertSame (held, table.getItem (10), "a facade the application holds keeps its identity");
		for (int i = 0; i < rows; i++) {
			TableItem item = table.getItem (i);
			assertEquals ("row " + i, item.getText (), "text of row " + i);
			assertEquals (i % 7 == 0, item.getChecked (), "check of row " + i);
			assertEquals (i % 11 == 0 ? red : table.getBackground (), item.getBackground (), "background of row " + i);
		}
		table.remove (0, 99);
		collectGarbage ();
		assertEquals ("row 100", table.getItem (0).getText ());
		assertEquals (rows - 100, table.indexOf (table.getItem (rows - 101)) + 1);
		assertTrue (table.getItem (5).getChecked (), "row 105 is checked (105 % 7 == 0)");
	}

	/* columns inserted and removed move the lanes of rows without a live facade */
	@Test
	public void columnChangesMoveTheStateOfReleasedRows () {
		Table table = plainTable (SWT.MULTI);
		for (int c = 0; c < 3; c++) new TableColumn (table, SWT.NONE).setWidth (60);
		int rows = 3_000;
		for (int i = 0; i < rows; i++) new TableItem (table, SWT.NONE).setText (new String [] {"a" + i, "b" + i, "c" + i});
		collectGarbage ();
		new TableColumn (table, SWT.NONE, 0).setWidth (60);
		collectGarbage ();
		table.getColumn (2).dispose ();
		collectGarbage ();
		for (int i = 0; i < rows; i += 97) {
			TableItem item = table.getItem (i);
			assertEquals ("", item.getText (0), "new first column of row " + i);
			assertEquals ("a" + i, item.getText (1), "row " + i);
			assertEquals ("c" + i, item.getText (2), "row " + i);
		}
	}

	/* rows without a live facade paint from the lanes exactly as with one */
	@Test
	public void releasedRowsPaintTheirState () {
		Table table = plainTable (SWT.MULTI | SWT.FULL_SELECTION);
		new TableColumn (table, SWT.NONE).setWidth (120);
		new TableColumn (table, SWT.NONE).setWidth (120);
		Color blue = display.getSystemColor (SWT.COLOR_BLUE);
		for (int i = 0; i < 2_000; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (new String [] {"row " + i, "cell " + i});
			if (i % 3 == 0) item.setForeground (1, blue);
		}
		shell.open ();
		table.setTopIndex (40);
		String before = snapshot (table);
		table.setTopIndex (1_500);
		table.getItems ();
		collectGarbage ();
		table.setTopIndex (40);
		assertEquals (before, snapshot (table));
	}

	/*
	 * T-Table-4: appends are one native count change per burst. The bar, the count and the rows are
	 * those of the stock Table the moment anything asks for them.
	 */
	@Test
	@Timeout (120)
	public void appendsAreBatchedAndVisibleWhenAsked () {
		Table table = plainTable (SWT.MULTI);
		shell.open ();
		settle ();
		int rows = 20_000;
		long t0 = System.nanoTime ();
		for (int i = 0; i < rows; i++) new TableItem (table, SWT.NONE).setText ("row " + i);
		long millis = (System.nanoTime () - t0) / 1_000_000;
		assertEquals (rows, table.getItemCount ());
		assertEquals (rows - 1, table.getVerticalBar ().getMaximum (), "the bar follows the appends as soon as it is read");
		table.setSelection (rows - 1);
		assertEquals (rows - 1, table.getSelectionIndex ());
		assertTrue (table.getTopIndex () > rows - 40, "showSelection scrolled to the last row: top " + table.getTopIndex ());
		assertEquals ("row " + (rows - 1), table.getItem (rows - 1).getText ());
		new TableItem (table, SWT.NONE, 0).setText ("first");
		assertEquals (rows, table.getSelectionIndex (), "an insert in front shifts the selection natively");
		settle ();
		System.out.println ("appendsAreBatchedAndVisibleWhenAsked: " + rows + " appends in " + millis + " ms");
		assertTrue (millis < 10_000, rows + " appends took " + millis + " ms");
	}

	/* T-Table-4: with a MeasureItem listener pack measures every row, rows without a facade too */
	@Test
	public void packWithMeasureItemMeasuresEveryRow () {
		Table table = plainTable (SWT.MULTI);
		TableColumn column = new TableColumn (table, SWT.NONE);
		int rows = 300;
		table.setItemCount (rows);
		table.getItem (7).setText ("seven");
		Set<Integer> measured = new HashSet<> ();
		table.addListener (SWT.MeasureItem, event -> {
			int row = table.indexOf ((TableItem) event.item);
			measured.add (row);
			event.width = row == 250 ? 333 : 20;
		});
		column.pack ();
		assertEquals (rows, measured.size (), "MeasureItem rows");
		assertTrue (column.getWidth () >= 333, "pack takes the widest measured row: " + column.getWidth ());
	}

	/* a MeasureItem listener that throws out of pack leaves no state behind: later walks over the rows make no facades */
	@Test
	public void aThrowingMeasureItemListenerLeavesNoFacadeWalk () throws ReflectiveOperationException {
		Table table = plainTable (SWT.MULTI);
		TableColumn column = new TableColumn (table, SWT.NONE);
		table.setItemCount (1_000);
		Listener thrower = event -> {
			throw new IllegalStateException ("measure");
		};
		table.addListener (SWT.MeasureItem, thrower);
		assertThrows (IllegalStateException.class, column::pack);
		table.removeListener (SWT.MeasureItem, thrower);
		int before = facades (table);
		new TableColumn (table, SWT.NONE, 0);
		assertEquals (before, facades (table), "inserting a column walks the rows without making facades");
	}

	/*
	 * Table.setTextDirection reaches every row in both modes: rows whose facades are held weakly or were
	 * released answer the direction of the strongly held rows with the same kind of text, and a row added
	 * later has none (as a stock item made later). Runs in both modes: stock items are the reference.
	 */
	@Test
	public void textDirectionReachesEveryRow () {
		Table table = new Table (shell, SWT.MULTI | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		int rows = 3_000;
		/* digits have no strong direction; the Hebrew rows have one */
		for (int i = 0; i < rows; i++) new TableItem (table, SWT.NONE).setText (i % 2 == 0 ? Integer.toString (i) : "שורה " + i);
		TableItem kept = table.getItem (1);
		/* in the viewport the facade of row 1 leaves the strong hold: 1,198 other rows were asked for since */
		for (int i = 2; i < 1_200; i++) table.getItem (i);
		collectGarbage ();
		table.setTextDirection (SWT.AUTO_TEXT_DIRECTION);
		int digits = table.getItem (1_198).getStyle () & SWT.FLIP_TEXT_DIRECTION;
		int hebrew = table.getItem (1_199).getStyle () & SWT.FLIP_TEXT_DIRECTION;
		assertNotEquals (digits, hebrew, "the two kinds of rows resolve to different directions");
		assertEquals (hebrew, kept.getStyle () & SWT.FLIP_TEXT_DIRECTION, "a facade the application kept");
		for (int i = 0; i < rows; i++) {
			assertEquals (i % 2 == 0 ? digits : hebrew, table.getItem (i).getStyle () & SWT.FLIP_TEXT_DIRECTION, "row " + i);
		}
		new TableItem (table, SWT.NONE).setText ("ש later");
		assertEquals (0, table.getItem (rows).getStyle () & SWT.FLIP_TEXT_DIRECTION, "a row added later");
	}

	/*
	 * Screen readers see the logical rows: the ListView's accessible object counts and names every row,
	 * rows of a pending append burst, rows without a facade and rows after a removal included. Runs in
	 * both modes: stock rows are the reference.
	 */
	@Test
	public void accessibilityAnswersEveryLogicalRow () {
		Table table = new Table (shell, SWT.MULTI | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		shell.open ();
		settle ();
		long [] ppv = new long [1];
		assertEquals (COM.S_OK, COM.CreateStdAccessibleObject (table.handle, OS.OBJID_CLIENT, COM.IIDIAccessible, ppv));
		IAccessible accessible = new IAccessible (ppv [0]);
		try {
			/* children that are not rows: the empty Table's count (one, with stock rows and with the viewport) */
			int other = childCount (accessible);
			int rows = 5_000;
			for (int i = 0; i < rows; i++) new TableItem (table, SWT.NONE).setText ("row " + i);
			assertEquals (other + rows, childCount (accessible), "children while the appends are pending");
			collectGarbage ();
			for (int row : new int [] {0, 1, rows / 2, rows - 1}) {
				assertEquals ("row " + row, name (accessible, row), "row " + row);
			}
			table.setItemCount (rows + 10);
			table.getItem (rows + 5).setText ("late");
			assertEquals (other + rows + 10, childCount (accessible));
			assertEquals ("late", name (accessible, rows + 5));
			table.remove (0, 99);
			collectGarbage ();
			assertEquals (other + rows - 90, childCount (accessible));
			assertEquals ("row 100", name (accessible, 0));
			assertEquals ("row " + (rows - 1), name (accessible, rows - 101));
		} finally {
			accessible.Release ();
		}
	}

	/*
	 * The thumb of the bar follows a burst of appends at once: ScrollBar.getThumbBounds and
	 * getThumbTrackBounds read the native bar with GetScrollBarInfo, not through scrolledHandle. Runs in
	 * both modes: stock rows are the reference.
	 */
	@Test
	public void thumbBoundsFollowAnAppendBurst () {
		Table table = new Table (shell, SWT.MULTI | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		shell.open ();
		/* few rows, so that the thumb is not at its minimum size before or after */
		for (int i = 0; i < 40; i++) new TableItem (table, SWT.NONE).setText ("row " + i);
		settle ();
		ScrollBar bar = table.getVerticalBar ();
		Rectangle before = bar.getThumbBounds ();
		for (int i = 0; i < 40; i++) new TableItem (table, SWT.NONE).setText ("more " + i);
		Rectangle during = bar.getThumbBounds (), track = bar.getThumbTrackBounds ();
		settle ();
		assertNotEquals (before, bar.getThumbBounds (), "40 more rows shrink the thumb");
		assertEquals (bar.getThumbBounds (), during, "thumb bounds read during the burst");
		assertEquals (bar.getThumbTrackBounds (), track, "thumb track bounds read during the burst");
	}

	private static int childCount (IAccessible accessible) {
		long heap = OS.GetProcessHeap (), count = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, 4);
		try {
			assertEquals (COM.S_OK, accessible.get_accChildCount (count));
			int [] result = new int [1];
			C.memmove (result, count, 4);
			return result [0];
		} finally {
			OS.HeapFree (heap, 0, count);
		}
	}

	/* the accessible name of a row (child id row + 1) */
	private static String name (IAccessible accessible, int row) {
		long heap = OS.GetProcessHeap ();
		long variant = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, VARIANT.sizeof);
		long name = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, C.PTR_SIZEOF);
		try {
			C.memmove (variant, new short [] {COM.VT_I4}, 2);
			C.memmove (variant + 8, new int [] {row + 1}, 4);
			assertEquals (COM.S_OK, accessible.get_accName (variant, name), "get_accName of row " + row);
			long [] bstr = new long [1];
			C.memmove (bstr, name, C.PTR_SIZEOF);
			if (bstr [0] == 0) return null;
			char [] chars = new char [COM.SysStringLen (bstr [0])];
			C.memmove (chars, bstr [0], chars.length * 2L);
			COM.SysFreeString (bstr [0]);
			return new String (chars);
		} finally {
			OS.HeapFree (heap, 0, variant);
			OS.HeapFree (heap, 0, name);
		}
	}

	private String snapshot (Table table) {
		table.setFocus ();
		table.redraw ();
		settle ();
		return ViewportDifferential.hash (Test_org_eclipse_swt_widgets_Table_ViewportDifferential.print (display, table));
	}

	private static void collectGarbage () {
		for (int i = 0; i < 4; i++) {
			System.gc ();
			try {
				Thread.sleep (20);
			} catch (InterruptedException e) {
				Thread.currentThread ().interrupt ();
			}
		}
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

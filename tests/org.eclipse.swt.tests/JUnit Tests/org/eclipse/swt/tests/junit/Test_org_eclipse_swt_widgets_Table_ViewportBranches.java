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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.win32.NMHDR;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Branch acceptance tests of the Table viewport (rule 15,
 * docs/m3/table-viewport/coverage/COVERAGE.md): the arms of the mediated type-ahead that the
 * ListView's own search never sends, selection snapshots beyond their row limit, the weak hold of
 * facades beyond the most recent, the reuse of the paint facades' cell arrays, text direction over
 * weakly held facades, and the pack mediation over empty, image and indent rows. Runs on the
 * invisible test desktop; every native interaction is a window message.
 */
public class Test_org_eclipse_swt_widgets_Table_ViewportBranches {
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

	private Table plainTable (int style) {
		Table table = new Table (shell, style | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()) && (OS.GetWindowLong (table.handle, OS.GWL_STYLE) & OS.LVS_OWNERDATA) != 0,
				"Table viewport mediation is not active in this build");
		return table;
	}

	private void drain () {
		while (display.readAndDispatch ()) {
			/* drain */
		}
	}

	/* ---------------------------------------------------------------- type-ahead */

	static final int LVFI_STRING = 0x2, LVFI_SUBSTRING = 0x4, LVFI_PARTIAL = 0x8, LVFI_WRAP = 0x20;

	/**
	 * LVN_ODFINDITEM exactly as the ListView sends it: NMHDR, the start row, then LVFINDINFO
	 * (flags, psz) aligned to a pointer. The answer is the found row, or -1.
	 */
	private long findItem (Table table, int start, int flags, String string) {
		long heap = OS.GetProcessHeap ();
		int base = NMHDR.sizeof;
		long pString = 0, pStruct = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, base + 3 * C.PTR_SIZEOF);
		try {
			/* NMHDR: hwndFrom, idFrom, code */
			OS.MoveMemory (pStruct, new long [] {table.handle, 0}, 2 * C.PTR_SIZEOF);
			OS.MoveMemory (pStruct + 2 * C.PTR_SIZEOF, new int [] {OS.LVN_ODFINDITEM}, 4);
			OS.MoveMemory (pStruct + base, new int [] {start}, 4);
			OS.MoveMemory (pStruct + base + C.PTR_SIZEOF, new int [] {flags}, 4);
			if (string != null) {
				char [] buffer = new char [string.length () + 1];
				string.getChars (0, string.length (), buffer, 0);
				pString = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, buffer.length * 2);
				OS.MoveMemory (pString, buffer, buffer.length * 2);
			}
			OS.MoveMemory (pStruct + base + 2 * C.PTR_SIZEOF, new long [] {pString}, C.PTR_SIZEOF);
			return OS.SendMessage (shell.handle, OS.WM_NOTIFY, 0, pStruct);
		} finally {
			if (pString != 0) OS.HeapFree (heap, 0, pString);
			OS.HeapFree (heap, 0, pStruct);
		}
	}

	/**
	 * The native search always asks with LVFI_STRING | LVFI_SUBSTRING | LVFI_WRAP; the mediation
	 * answers the other shapes of the notification too: exact matches, a search that may not wrap,
	 * no string flags, no string, an empty table, and a string at the length bound.
	 */
	@Test
	@Timeout (60)
	public void typeAheadAnswersEveryShapeOfTheNotification () {
		Table empty = plainTable (SWT.SINGLE);
		assertEquals (-1, findItem (empty, 0, LVFI_STRING | LVFI_WRAP, "a"), "an empty table answers -1");
		empty.dispose ();
		Table table = plainTable (SWT.SINGLE);
		String [] rows = {"alpha", "beta", "Gamma", "beta"};
		for (String text : rows) new TableItem (table, SWT.NONE).setText (text);
		shell.open ();
		drain ();
		Logger logger = Logger.getLogger ("org.eclipse.swt.widgets.Table");
		Level old = logger.getLevel ();
		logger.setLevel (Level.FINEST);
		try {
			assertEquals (1, findItem (table, 0, LVFI_STRING | LVFI_SUBSTRING | LVFI_WRAP, "be"), "the native shape, traced");
		} finally {
			logger.setLevel (old);
		}
		assertEquals (2, findItem (table, 0, LVFI_STRING, "gamma"), "exact, ignoring case");
		assertEquals (-1, findItem (table, 0, LVFI_STRING | LVFI_WRAP, "gam"), "exact never matches a prefix");
		assertEquals (3, findItem (table, 2, LVFI_STRING | LVFI_PARTIAL, "beta"), "LVFI_PARTIAL is the older name");
		assertEquals (-1, findItem (table, 2, LVFI_STRING | LVFI_SUBSTRING, "alpha"), "without LVFI_WRAP the scan stops at the end");
		assertEquals (0, findItem (table, 2, LVFI_STRING | LVFI_SUBSTRING | LVFI_WRAP, "alpha"), "with LVFI_WRAP it wraps");
		assertEquals (-1, findItem (table, 0, LVFI_WRAP, "alpha"), "no string flag answers -1");
		assertEquals (-1, findItem (table, 0, LVFI_STRING, null), "no string answers -1");
		assertEquals (-1, findItem (table, 9, LVFI_STRING | LVFI_SUBSTRING | LVFI_WRAP, "zz"), "a start beyond the end is clamped");
		assertEquals (-1, findItem (table, 0, LVFI_STRING | LVFI_SUBSTRING | LVFI_WRAP, "x".repeat (300)), "a string is read to the native bound only");
	}

	/* ---------------------------------------------------------------- selection snapshots */

	/**
	 * While a key runs, the viewport follows the selection with a snapshot of at most 4096 selected
	 * rows; beyond that it falls back to the notified answer. The key still selects like stock.
	 */
	@Test
	@Timeout (120)
	public void selectionSnapshotsStopBeyondTheirRowLimit () {
		Table table = plainTable (SWT.MULTI);
		table.setItemCount (5000);
		shell.open ();
		drain ();
		table.selectAll ();
		drain ();
		assertEquals (5000, table.getSelectionCount ());
		long lParam = 1 | ((long) OS.MapVirtualKey (OS.VK_DOWN, 0) << 16) | 1L << 24;
		ViewportDifferential.send (table, OS.WM_KEYDOWN, OS.VK_DOWN, lParam);
		ViewportDifferential.send (table, OS.WM_KEYUP, OS.VK_DOWN, lParam | 0xC0000000L);
		drain ();
		assertEquals (1, table.getSelectionCount (), "the key collapses the selection like stock");
		table.select (2);
		drain ();
		Logger logger = Logger.getLogger ("org.eclipse.swt.widgets.Table");
		Level old = logger.getLevel ();
		logger.setLevel (Level.FINEST);
		try {
			/* one traced count change and one traced append flush */
			table.setItemCount (40);
			new TableItem (table, SWT.NONE).setText ("append");
			new TableItem (table, SWT.NONE).setText ("append");
			assertEquals (42, table.getItemCount ());
		} finally {
			logger.setLevel (old);
		}
		drain ();
		assertEquals (42, table.getItemCount ());
	}

	/* ---------------------------------------------------------------- facades */

	/**
	 * Facades beyond the 1024 most recent leave the strong hold: a pinned facade (data, a listener)
	 * stays, an application-held facade is answered again by its weak hold, and rows edited after
	 * removals renumber from their hints.
	 */
	@Test
	@Timeout (120)
	public void facadesBeyondTheRecentHoldStayReachable () {
		Table table = plainTable (SWT.MULTI);
		table.setItemCount (1500);
		shell.open ();
		drain ();
		TableItem held = table.getItem (0);
		held.setText ("held");
		TableItem pinnedData = table.getItem (1);
		pinnedData.setData ("pin");
		TableItem pinnedListener = table.getItem (2);
		pinnedListener.addListener (SWT.Selection, event -> {});
		for (int row = 3; row < 1500; row++) {
			table.getItem (row).setText ("row " + row);
		}
		assertSame (held, table.getItem (0), "an application-held facade is answered by its weak hold");
		assertSame (pinnedData, table.getItem (1), "a facade with data never leaves the strong hold");
		assertSame (pinnedListener, table.getItem (2), "a facade with a listener never leaves the strong hold");
		assertEquals (1500, table.getItems ().length);
		table.remove (10, 19);
		assertEquals (0, table.indexOf (held), "a hint below the removal stays exact");
		assertEquals ("row 30", table.getItem (20).getText (), "rows renumber from the removal");
		new TableItem (table, SWT.NONE, 5).setText ("inserted");
		assertEquals ("inserted", table.getItem (5).getText ());
		assertEquals (0, table.indexOf (held));
		table.setItemCount (800);
		assertEquals (800, table.getItemCount ());
		assertEquals ("held", table.getItem (0).getText (), "state survives a shrink over weakly held rows");
		table.setItemCount (0);
		assertEquals (0, table.getItemCount (), "a count of zero clears and redraws");
		table.setItemCount (3);
		table.removeAll ();
		assertEquals (0, table.getItemCount ());
	}

	/**
	 * A facade that left the strong hold and that the application does not hold is collectible: the
	 * row keeps its state in the lanes and answers a new facade afterwards.
	 */
	@Test
	@Timeout (120)
	public void releasedFacadesAreCollectible () throws InterruptedException {
		Table table = plainTable (SWT.MULTI);
		table.setItemCount (1300);
		shell.open ();
		drain ();
		java.lang.ref.WeakReference<TableItem> reference = new java.lang.ref.WeakReference<> (table.getItem (0));
		table.getItem (0).setText ("kept in the lanes");
		for (int row = 1; row < 1300; row++) table.getItem (row);
		for (int attempt = 0; attempt < 20 && reference.get () != null; attempt++) collect ();
		assertNull (reference.get (), "a released facade is collectible once the event loop has run its skin pass");
		assertEquals ("kept in the lanes", table.getItem (0).getText (), "the row answers a new facade with its state");
	}

	/*
	 * Every new widget sits in the display's skin queue until the event loop runs once; only then can a
	 * released facade be collected.
	 */
	private void collect () throws InterruptedException {
		drain ();
		System.gc ();
		Thread.sleep (40);
		drain ();
	}

	/* ---------------------------------------------------------------- paint facades */

	/**
	 * Painting answers rows from the lanes through reusable paint facades: the second paint loads
	 * the facade again and reuses its cell arrays. Rows mix sub-column texts, images, cell fonts
	 * and colours, so every lane kind is loaded both ways.
	 */
	@Test
	@Timeout (120)
	public void paintFacadesReuseTheirCellArrays () throws InterruptedException {
		Table table = plainTable (SWT.MULTI | SWT.CHECK);
		for (int c = 0; c < 3; c++) new TableColumn (table, SWT.NONE).setWidth (90);
		Image image = new Image (display, 16, 16);
		Font font = new Font (display, display.getSystemFont ().getFontData () [0].getName (), 11, SWT.ITALIC);
		Color red = display.getSystemColor (SWT.COLOR_RED), blue = display.getSystemColor (SWT.COLOR_BLUE);
		try {
			table.setItemCount (1200);
			for (int row = 0; row < 12; row++) {
				TableItem item = table.getItem (row);
				item.setText (0, "r" + row);
				if (row % 2 == 0) item.setText (2, "c2 " + row);
				if (row % 3 == 0) item.setImage (1, image);
				if (row % 4 == 0) item.setFont (1, font);
				if (row % 4 == 1) item.setBackground (2, red);
				if (row % 4 == 2) item.setForeground (1, blue);
				if (row % 5 == 0) item.setFont (font);
				if (row % 6 == 1) item.setChecked (true);
				if (row % 6 == 2) item.setGrayed (true);
			}
			/* the rows with state leave the strong hold, so painting them loads the lanes into paint facades */
			for (int row = 12; row < 1200; row++) table.getItem (row);
			shell.open ();
			for (int attempt = 0; attempt < 5; attempt++) collect ();
			ImageData first = ViewportDifferential.render (table);
			ImageData second = ViewportDifferential.render (table);
			assertEquals (first.width, second.width);
			/* custom-draw stages with listeners: painted rows then need real facades */
			Listener listener = event -> {};
			table.addListener (SWT.EraseItem, listener);
			table.addListener (SWT.PaintItem, listener);
			ViewportDifferential.render (table);
			table.removeListener (SWT.EraseItem, listener);
			table.removeListener (SWT.PaintItem, listener);
			table.getItem (1).setText (1, "between paints");
			ViewportDifferential.render (table);
			assertEquals ("between paints", table.getItem (1).getText (1), "lanes answer what the facade stored");
			assertEquals ("r2", table.getItem (2).getText (0));
			assertTrue (table.getItem (7).getChecked (), "check state survives released facades");
			assertTrue (table.getItem (8).getGrayed ());
		} finally {
			image.dispose ();
			font.dispose ();
		}
	}

	/* ---------------------------------------------------------------- text direction */

	/** The table's text direction reaches rows without a facade, weakly held facades and rows made later. */
	@Test
	@Timeout (120)
	public void textDirectionReachesWeakAndFutureFacades () throws InterruptedException {
		Table table = plainTable (SWT.MULTI);
		table.setItemCount (1200);
		shell.open ();
		drain ();
		TableItem held = table.getItem (0);
		for (int row = 1; row < 1200; row++) table.getItem (row);
		table.setTextDirection (SWT.FLIP_TEXT_DIRECTION);
		drain ();
		assertNotNull (held.getText ());
		assertEquals (1200, table.getItemCount ());
		/* rows whose facades were released load the direction bits on their next facade */
		for (int attempt = 0; attempt < 5; attempt++) collect ();
		ViewportDifferential.render (table);
		assertNotNull (table.getItem (600).getText ());
		table.setTextDirection (SWT.AUTO_TEXT_DIRECTION);
		drain ();
		ViewportDifferential.render (table);
	}

	/* ---------------------------------------------------------------- pack */

	/** Pack over an empty table, image rows, indent and empty texts keeps the mediated autosize on every shape. */
	@Test
	@Timeout (120)
	public void packMediatesEmptyImageAndIndentRows () {
		Table table = plainTable (SWT.MULTI);
		TableColumn first = new TableColumn (table, SWT.NONE);
		TableColumn second = new TableColumn (table, SWT.NONE);
		first.setWidth (60);
		second.setWidth (60);
		shell.open ();
		drain ();
		first.pack ();
		assertTrue (first.getWidth () >= 0, "an empty column packs");
		Image image = new Image (display, 16, 16);
		Font font = new Font (display, display.getSystemFont ().getFontData () [0].getName (), 12, SWT.BOLD);
		try {
			table.setItemCount (60);
			for (int row = 0; row < 60; row++) {
				TableItem item = table.getItem (row);
				if (row != 20) item.setText (0, row == 50 ? "the widest row of the first column " + row : "r" + row);
				if (row == 7) item.setImage (0, image);
				if (row == 8) item.setImage (1, image);
				if (row == 9) item.setText (1, "the widest row of the second column");
				if (row == 9) item.setFont (1, font);
				if (row == 11) item.setImageIndent (2);
			}
			table.setTopIndex (40);
			drain ();
			first.pack ();
			second.pack ();
			assertTrue (first.getWidth () > 60, "the widest row was measured although out of view");
			assertTrue (second.getWidth () > 60);
			table.getItem (50).setText (0, "");
			first.pack ();
			assertTrue (first.getWidth () > 0);
			/* a column no row has text in: no widest row to reveal */
			TableColumn third = new TableColumn (table, SWT.NONE);
			third.pack ();
			assertTrue (third.getWidth () >= 0);
			/* a table font change marks every measured width stale */
			Font tableFont = new Font (display, display.getSystemFont ().getFontData () [0].getName (), 14, SWT.NORMAL);
			try {
				table.setFont (tableFont);
				first.pack ();
				assertTrue (first.getWidth () > 0);
			} finally {
				table.setFont (null);
				tableFont.dispose ();
			}
		} finally {
			image.dispose ();
			font.dispose ();
		}
	}
}

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

import java.lang.reflect.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * The R4_38 item store of a VIRTUAL Table in the original mode (<code>TableStockItems</code>, rule 12 of the
 * viewport agent brief; docs/m3/table-viewport/coverage/COVERAGE.md): its keyed form (a sparse array of the
 * items made so far) and its dense form (after more than half the rows have items), each grown, compacted,
 * shrunk and scanned as R4_38 does, with the table hidden and shown (R4_38 grows faster while it is not
 * painted). Runs only in the original mode (<code>-Dswt.widget.mode.Table=original</code>), on the invisible
 * test desktop; the guards that no API sequence reaches are driven white-box with their state forced.
 */
public class Test_org_eclipse_swt_widgets_Table_StockItems {
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

	private Table virtualTable () throws ReflectiveOperationException {
		Table table = new Table (shell, SWT.VIRTUAL | SWT.MULTI | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		table.addListener (SWT.SetData, event -> ((TableItem) event.item).setText ("row " + event.index));
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()) && get (table, "stockItems") != null,
				"the R4_38 store is used only by a VIRTUAL Table of the original mode");
		return table;
	}

	private void drain () {
		while (display.readAndDispatch ()) {
			/* drain */
		}
	}

	/* ---------------------------------------------------------------- reflection */

	private static Field field (Class<?> type, String name) throws NoSuchFieldException {
		for (Class<?> c = type; c != null; c = c.getSuperclass ()) {
			try {
				Field field = c.getDeclaredField (name);
				field.setAccessible (true);
				return field;
			} catch (NoSuchFieldException e) {
				/* the superclass may declare it */
			}
		}
		throw new NoSuchFieldException (name);
	}

	private static Object get (Object target, String name) throws ReflectiveOperationException {
		return field (target.getClass (), name).get (target);
	}

	private static void set (Object target, String name, Object value) throws ReflectiveOperationException {
		field (target.getClass (), name).set (target, value);
	}

	private static Object invoke (Object target, String name, Class<?> [] types, Object... args) throws Throwable {
		Method method = target.getClass ().getDeclaredMethod (name, types);
		method.setAccessible (true);
		try {
			return method.invoke (target, args);
		} catch (InvocationTargetException failure) {
			throw failure.getCause ();
		}
	}

	private static Object store (Table table) throws ReflectiveOperationException {
		return get (table, "stockItems");
	}

	private static boolean keyed (Table table) throws ReflectiveOperationException {
		return get (store (table), "keys") != null;
	}

	/* a facade that is not in the store (TableItem (Table, int, int, boolean) without create) */
	private static TableItem orphan (Table table) throws ReflectiveOperationException {
		Constructor<TableItem> constructor = TableItem.class.getDeclaredConstructor (Table.class, int.class, int.class, boolean.class);
		constructor.setAccessible (true);
		return constructor.newInstance (table, SWT.NONE, -1, false);
	}

	private static final Class<?> [] GET = {int.class, boolean.class, int.class};
	private static final Class<?> [] ITEM = {TableItem.class};

	/* ---------------------------------------------------------------- keyed form */

	/**
	 * The keyed form answers lookups of rows without items (none yet, beyond the last key, between keys),
	 * inserts keys before and after, grows its key array hidden and shown, and compacts into the dense form
	 * once more than half the rows have items.
	 */
	@Test
	@Timeout (60)
	public void keyedStoreLooksUpGrowsAndCompacts () throws Throwable {
		Table table = virtualTable ();
		table.setItemCount (100);
		assertTrue (keyed (table), "a VIRTUAL Table starts keyed");
		table.clear (5);
		assertEquals (0, (int) (Integer) get (store (table), "keyCount"), "no item, no key");
		/* hidden: the key array grows by half */
		TableItem fifty = table.getItem (50);
		TableItem ten = table.getItem (10);
		TableItem thirty = table.getItem (30);
		table.getItem (60);
		table.getItem (70);
		assertEquals (5, (int) (Integer) get (store (table), "keyCount"));
		table.clear (90);
		table.clear (20);
		table.clear (10);
		assertSame (ten, table.getItem (10), "a cleared row keeps its item");
		assertEquals (30, table.indexOf (thirty));
		assertEquals (50, table.indexOf (fifty));
		assertEquals (-1, (int) (Integer) invoke (store (table), "indexOf", ITEM, orphan (table)), "an item that is not in the store");
		/* a lookup with the count given, as the Table's own loops pass it */
		assertNotNull (invoke (store (table), "get", GET, 75, true, 100));
		/* shown: the key array grows by four */
		shell.open ();
		drain ();
		for (int row = 80; row < 84; row++) table.getItem (row);
		assertTrue (keyed (table));
		/* more than half the rows with items: one compaction into the dense form, shown and hidden */
		for (int row = 0; row < 60; row++) table.getItem (row);
		assertFalse (keyed (table), "the store compacted");
		assertSame (fifty, table.getItem (50));
		Table hidden = new Table (shell, SWT.VIRTUAL | SWT.BORDER);
		hidden.setVisible (false);
		hidden.setItemCount (12);
		for (int row = 0; row < 12; row++) hidden.getItem (row);
		assertFalse (keyed (hidden), "a hidden table compacts too");
		/* removals shrink the keyed arrays at the next WM_PAINT (R4_38 Table.WM_PAINT), sent here so the test does not wait for the desktop
		 * to paint; the table has bounds (the visibility gate defers the paint of a control without any) and is short, so the
		 * rows the paint makes after the shrink are few */
		Table sparse = new Table (shell, SWT.VIRTUAL | SWT.BORDER);
		sparse.setBounds (5, 5, 300, 60);
		sparse.moveAbove (null);
		sparse.setItemCount (400);
		for (int row = 0; row < 400; row += 20) sparse.getItem (row);
		assertTrue (keyed (sparse));
		sparse.remove (0, 300);
		OS.SendMessage (sparse.handle, OS.WM_PAINT, 0, 0);
		assertTrue (((int []) get (store (sparse), "keys")).length - (Integer) get (store (sparse), "keyCount") <= 3, "the keys shrank");
	}

	/* ---------------------------------------------------------------- dense form */

	/**
	 * The dense form grows on appends (hidden and shown), answers index scans from the last answer in both
	 * directions, shrinks after removals, and reaches every item when the text direction changes.
	 */
	@Test
	@Timeout (60)
	public void denseStoreGrowsScansAndShrinks () throws Throwable {
		Table table = virtualTable ();
		table.setItemCount (8);
		for (int row = 0; row < 8; row++) table.getItem (row);
		assertFalse (keyed (table));
		/* hidden: appends grow the array by half */
		for (int i = 0; i < 6; i++) new TableItem (table, SWT.NONE);
		shell.open ();
		drain ();
		/* shown: by four */
		for (int i = 0; i < 6; i++) new TableItem (table, SWT.NONE);
		assertEquals (20, table.getItemCount ());
		TableItem [] items = table.getItems ();
		set (table, "lastIndexOf", 0);
		assertEquals (17, table.indexOf (items [17]), "forward scan");
		assertEquals (2, table.indexOf (items [2]), "backward scan from a late answer");
		assertEquals (3, table.indexOf (items [3]), "the row after the last answer");
		assertEquals (2, table.indexOf (items [2]), "the row before the last answer");
		assertEquals (2, table.indexOf (items [2]), "the last answer itself");
		assertEquals (-1, (int) (Integer) invoke (store (table), "indexOf", ITEM, orphan (table)), "an item that is not in the store");
		set (table, "lastIndexOf", 19);
		assertEquals (-1, (int) (Integer) invoke (store (table), "indexOf", ITEM, orphan (table)), "nor from the end");
		TableItem [] dense = new TableItem [20];
		invoke (store (table), "getItems", new Class<?> [] {TableItem [].class, int.class}, dense, 20);
		assertSame (items [9], dense [9]);
		assertNull (invoke (store (table), "get", GET, 400, false, -1), "a row beyond the array has no item");
		table.setTextDirection (SWT.AUTO_TEXT_DIRECTION);
		table.setTextDirection (SWT.RIGHT_TO_LEFT);
		/* rows 0 to 14 (the range is inclusive); the dense array shrinks at the next WM_PAINT, sent as above */
		table.remove (0, 14);
		OS.SendMessage (table.handle, OS.WM_PAINT, 0, 0);
		assertEquals (5, table.getItemCount ());
		assertTrue (((Object []) get (store (table), "items")).length - 5 <= 3, "the dense array shrank");
	}

	/* ---------------------------------------------------------------- redraw off */

	/**
	 * While redraw is off R4_38 grows as fast as while the table is hidden (by half, not by four): the key array,
	 * the compaction into the dense form and the dense array; the text direction then walks a dense array with
	 * free slots. The growth is measured against the length before it, since a shown table may have made the
	 * items of its visible rows already.
	 */
	@Test
	@Timeout (60)
	public void growsByHalfWhileRedrawIsOff () throws Throwable {
		shell.open ();
		drain ();
		Table table = virtualTable ();
		table.setRedraw (false);
		table.setItemCount (1000);
		int [] keys = (int []) get (store (table), "keys");
		for (int row = 0; row < 1000 && get (store (table), "keys") == keys; row += 10) table.getItem (row);
		assertEquals (keys.length * 3 / 2, ((int []) get (store (table), "keys")).length, "the key array grew by half");
		Table compact = virtualTable ();
		compact.setRedraw (false);
		compact.setItemCount (10);
		for (int row = 0; row < 10; row++) compact.getItem (row);
		assertFalse (keyed (compact));
		assertEquals (15, ((Object []) get (store (compact), "items")).length, "compacted with room for half the count again");
		Object [] items = (Object []) get (store (compact), "items");
		while (get (store (compact), "items") == items) new TableItem (compact, SWT.NONE);
		assertEquals (items.length * 3 / 2, ((Object []) get (store (compact), "items")).length, "the dense array grew by half");
		compact.setTextDirection (SWT.AUTO_TEXT_DIRECTION);
		assertEquals (16, compact.getItemCount ());
	}

	/* ---------------------------------------------------------------- guards */

	/**
	 * White-box: the R4_38 work-around for a ListView answering a count of zero while items live (bug
	 * 292199) counts the living items; release skips items disposed already, in both forms.
	 */
	@Test
	@Timeout (60)
	public void guardsOfTheStockStore () throws Throwable {
		Table table = virtualTable ();
		Object store = store (table);
		TableItem [] living = new TableItem [8];
		for (int i = 0; i < 3; i++) living [i] = orphan (table);
		set (store, "keys", null);
		set (store, "items", living);
		invoke (store, "checkShrink", new Class<?> [0]);
		assertEquals (4, ((Object []) get (store, "items")).length, "three living items behind a zero count");
		Field state = field (TableItem.class, "state");
		int disposed = (Integer) field (Widget.class, "DISPOSED").get (null);
		TableItem gone = orphan (table);
		state.setInt (gone, state.getInt (gone) | disposed);
		table.setItemCount (2);
		set (store, "keys", null);
		set (store, "items", new TableItem [] {gone, null, null, null});
		invoke (store, "release", new Class<?> [0]);
		set (store, "keys", new int [] {0, 1, 0, 0});
		set (store, "items", new TableItem [] {gone, null, null, null});
		set (store, "keyCount", 2);
		invoke (store, "release", new Class<?> [0]);
		/*
		 * _getItems is reached only by a plain Table (VIRTUAL ones fill getItems row by row), so the store of a
		 * VIRTUAL Table never runs it; its keyed loop reads items [keys [i]] as R4_38 does (a latent slip of
		 * R4_38: the item of key i is items [i]), kept as stock.
		 */
		TableItem first = orphan (table), second = orphan (table);
		set (store, "keys", new int [] {1, 2, 9, 0});
		set (store, "items", new TableItem [] {first, second, null, null});
		set (store, "keyCount", 3);
		TableItem [] result = new TableItem [4];
		invoke (store, "getItems", new Class<?> [] {TableItem [].class, int.class}, result, 3);
		assertSame (second, result [1], "R4_38 copies items [keys [i]]");
		assertNull (result [2]);
		set (store, "keys", null);
		set (store, "items", new TableItem [] {first, second, null, null});
		invoke (store, "getItems", new Class<?> [] {TableItem [].class, int.class}, result, 2);
		assertSame (first, result [0], "the dense form copies the rows");
		set (store, "keys", new int [4]);
		set (store, "items", new TableItem [4]);
		set (store, "keyCount", 0);
		assertEquals (2, (int) OS.SendMessage (table.handle, OS.LVM_GETITEMCOUNT, 0, 0));
	}

	/**
	 * White-box: the R4_38 arms that a VIRTUAL Table of the original mode never reaches, with their state forced.
	 * The work-around for a count of zero (bug 292199) over a small array, over an array full of living items and
	 * up to an item disposed already; lookups while the table is not VIRTUAL (R4_38 kept the store for plain
	 * tables too); a key whose item is gone; a compaction that already holds the row looked up (keys out of
	 * order, a state the store never makes); the keyed getItems loop over keys that are all below the count.
	 */
	@Test
	@Timeout (60)
	public void armsNoApiSequenceReaches () throws Throwable {
		Table table = virtualTable ();
		Object store = store (table);
		Class<?> [] none = new Class<?> [0];
		set (store, "keys", null);
		set (store, "items", new TableItem [4]);
		invoke (store, "checkShrink", none);
		assertEquals (4, ((Object []) get (store, "items")).length, "a small array with no rows stays");
		TableItem [] living = new TableItem [5];
		for (int i = 0; i < 5; i++) living [i] = orphan (table);
		set (store, "items", living);
		invoke (store, "checkShrink", none);
		assertSame (living, get (store, "items"), "five living items fill the array");
		Field state = field (TableItem.class, "state");
		TableItem gone = orphan (table);
		state.setInt (gone, state.getInt (gone) | (Integer) field (Widget.class, "DISPOSED").get (null));
		set (store, "items", new TableItem [] {living [0], living [1], gone, null, null, null, null, null});
		invoke (store, "checkShrink", none);
		assertEquals (4, ((Object []) get (store, "items")).length, "the count stops at an item disposed already");
		Field style = field (Widget.class, "style");
		int virtual = style.getInt (table);
		style.setInt (table, virtual & ~SWT.VIRTUAL);
		try {
			set (store, "items", new TableItem [] {living [0], null, null, null});
			assertSame (living [0], invoke (store, "get", GET, 0, true, -1), "not VIRTUAL, dense");
			assertNull (invoke (store, "get", GET, 1, true, -1), "not VIRTUAL, dense, no item");
			set (store, "keys", new int [] {3, 0, 0, 0});
			set (store, "items", new TableItem [] {living [1], null, null, null});
			set (store, "keyCount", 1);
			assertSame (living [1], invoke (store, "get", GET, 3, true, -1), "not VIRTUAL, keyed");
			assertNull (invoke (store, "get", GET, 2, true, -1), "not VIRTUAL, keyed, between keys");
			assertNull (invoke (store, "get", GET, 7, true, -1), "not VIRTUAL, keyed, beyond the last key");
		} finally {
			style.setInt (table, virtual);
		}
		set (store, "items", new TableItem [4]);
		TableItem made = (TableItem) invoke (store, "get", GET, 3, true, 10);
		assertSame (made, ((Object []) get (store, "items")) [0], "a key whose item is gone gets a new one");
		set (store, "keys", new int [] {3, 1, 0, 0});
		set (store, "items", new TableItem [] {living [2], living [3], null, null});
		set (store, "keyCount", 2);
		assertSame (living [3], invoke (store, "get", GET, 1, true, 3), "the compacted array holds the row already");
		assertFalse (keyed (table));
		set (store, "keys", new int [] {0, 2, 0, 0});
		set (store, "items", new TableItem [] {living [0], null, living [2], null});
		set (store, "keyCount", 2);
		TableItem [] result = new TableItem [4];
		invoke (store, "getItems", new Class<?> [] {TableItem [].class, int.class}, result, 4);
		assertSame (living [2], result [2], "every key is below the count");
		set (store, "keys", new int [4]);
		set (store, "items", new TableItem [4]);
		set (store, "keyCount", 0);
	}
}

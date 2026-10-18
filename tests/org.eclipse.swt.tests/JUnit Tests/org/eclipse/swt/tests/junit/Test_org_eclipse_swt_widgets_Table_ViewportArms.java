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
import java.util.function.IntFunction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.internal.win32.NMHDR;
import org.eclipse.swt.internal.win32.NMLVCUSTOMDRAW;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * White-box arms of the package-private Table viewport (rule 15,
 * docs/m3/table-viewport/coverage/COVERAGE.md), reached by reflection like the other viewport
 * tests: the selection tracking of input under every notification shape and nesting, the
 * defensive answers for facades that are not rows of the table, the custom-draw stages the Table
 * never answers, and the helpers of the paint facades. Every native structure is real memory
 * filled as the ListView fills it. Runs on the invisible test desktop.
 */
public class Test_org_eclipse_swt_widgets_Table_ViewportArms {
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
		TableViewportScripts.modifiers (false, false);
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
		Class<?> type = target instanceof Class<?> c ? c : target.getClass ();
		Method method = type.getDeclaredMethod (name, types);
		method.setAccessible (true);
		try {
			return method.invoke (target instanceof Class<?> ? null : target, args);
		} catch (InvocationTargetException failure) {
			throw failure.getCause ();
		}
	}

	private static Object viewport (Table table) throws ReflectiveOperationException {
		return get (table, "viewport");
	}

	private static final Class<?> [] BEGIN = {int.class, long.class};
	private static final Class<?> [] END = {Object.class, boolean.class};
	private static final Class<?> [] LONG = {long.class};
	private static final Class<?> [] INT = {int.class};
	private static final Class<?> [] ITEM = {TableItem.class};

	private static Object begin (Object viewport, int msg, long wParam) throws Throwable {
		return invoke (viewport, "selectionBegin", BEGIN, msg, wParam);
	}

	private static boolean end (Object viewport, Object token, boolean notified) throws Throwable {
		return (Boolean) invoke (viewport, "selectionEnd", END, token, notified);
	}

	/* a facade that is not a row of the table (TableItem (Table, int, int, boolean) without create) */
	private static TableItem orphan (Table table) throws ReflectiveOperationException {
		Constructor<TableItem> constructor = TableItem.class.getDeclaredConstructor (Table.class, int.class, int.class, boolean.class);
		constructor.setAccessible (true);
		return constructor.newInstance (table, SWT.NONE, -1, false);
	}

	/* ---------------------------------------------------------------- native notifications */

	/** NMLISTVIEW: NMHDR, iItem, iSubItem, uNewState, uOldState, uChanged, then the action point and lParam. */
	private long itemChanged (Table table, int item, int newState, int oldState, int changed) {
		long heap = OS.GetProcessHeap (), pointer = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, 64);
		OS.MoveMemory (pointer, new long [] {table.handle}, 8);
		OS.MoveMemory (pointer + NMHDR.sizeof, new int [] {item, 0, newState, oldState, changed}, 20);
		return pointer;
	}

	/** NMLVODSTATECHANGE: NMHDR, iFrom, iTo, uNewState, uOldState. */
	private long stateChanged (Table table, int from, int to, int newState, int oldState) {
		long heap = OS.GetProcessHeap (), pointer = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, NMHDR.sizeof + 16);
		OS.MoveMemory (pointer, new long [] {table.handle}, 8);
		OS.MoveMemory (pointer + NMHDR.sizeof, new int [] {from, to, newState, oldState}, 16);
		return pointer;
	}

	private void notifyItem (Object viewport, long pointer) throws Throwable {
		try {
			invoke (viewport, "itemChanged", LONG, pointer);
		} finally {
			OS.HeapFree (OS.GetProcessHeap (), 0, pointer);
		}
	}

	private void notifyState (Object viewport, long pointer) throws Throwable {
		try {
			invoke (viewport, "stateChanged", LONG, pointer);
		} finally {
			OS.HeapFree (OS.GetProcessHeap (), 0, pointer);
		}
	}

	/* ---------------------------------------------------------------- selection tracking */

	/**
	 * Messages that cannot change the selection start no track; a Shift key starts a range track only
	 * for the range keys; nested messages restore the outer track; a token that is not the running
	 * one answers the Table's own notification.
	 */
	@Test
	@Timeout (60)
	public void selectionTracksNestAndAnswerOnlyRealChanges () throws Throwable {
		Table table = plainTable (SWT.MULTI);
		table.setItemCount (10);
		shell.open ();
		drain ();
		Object viewport = viewport (table);
		assertNull (begin (viewport, OS.WM_PAINT, 0), "a paint starts no track");
		assertTrue (end (viewport, null, true), "without a track the Table's answer stands");
		TableViewportScripts.modifiers (true, false);
		Object left = begin (viewport, OS.WM_KEYDOWN, OS.VK_LEFT);
		assertNull (get (left, "before"), "Shift+Left is no range key");
		assertFalse (end (viewport, left, true), "no row changed under the track");
		Object space = begin (viewport, OS.WM_KEYDOWN, OS.VK_SPACE);
		assertNotNull (get (space, "before"), "Shift+Space is a range key");
		table.select (4);
		assertTrue (end (viewport, space, false), "a different selection after a range key counts");
		TableViewportScripts.modifiers (true, true);
		Object ctrlSpace = begin (viewport, OS.WM_KEYDOWN, OS.VK_SPACE);
		assertNull (get (ctrlSpace, "before"), "Ctrl+Shift+Space is no range key");
		end (viewport, ctrlSpace, false);
		TableViewportScripts.modifiers (false, false);
		Object outer = begin (viewport, OS.WM_LBUTTONDOWN, 0);
		Object inner = begin (viewport, OS.WM_KEYDOWN, OS.VK_DOWN);
		assertSame (outer, get (inner, "outer"));
		assertTrue (end (viewport, outer, true), "a token that is not the running one answers the notification");
		assertSame (outer, get (viewport, "track"), "the outer track runs again");
		end (viewport, outer, false);
		assertNull (get (viewport, "track"));
		Object move = begin (viewport, OS.WM_MOUSEMOVE, 0);
		assertNull (move, "a move without a button starts no track");
		end (viewport, move, false);
		Object drag = begin (viewport, OS.WM_MOUSEMOVE, OS.MK_LBUTTON);
		assertNotNull (drag, "a move with a button down selects (marquee)");
		end (viewport, drag, false);
	}

	/** Tracks of more than 4096 selected rows stop: the Table's notification decides, also for an outer track. */
	@Test
	@Timeout (120)
	public void largeSelectionsStopTheTracks () throws Throwable {
		Table table = plainTable (SWT.MULTI);
		table.setItemCount (5000);
		shell.open ();
		drain ();
		Object viewport = viewport (table);
		TableViewportScripts.modifiers (true, false);
		Object range = begin (viewport, OS.WM_KEYDOWN, OS.VK_END);
		assertNotNull (get (range, "before"));
		table.selectAll ();
		assertTrue (end (viewport, range, true), "beyond the limit the notification decides");
		TableViewportScripts.modifiers (false, false);
		table.deselectAll ();
		Object outer = begin (viewport, OS.WM_LBUTTONDOWN, 0);
		Object inner = begin (viewport, OS.WM_KEYDOWN, OS.VK_DOWN);
		table.selectAll ();
		end (viewport, inner, false);
		assertNull (get (viewport, "track"), "the outer track stops once the selection outgrew it");
		end (viewport, outer, false);
		table.deselectAll ();
	}

	/** LVN_ITEMCHANGED and LVN_ODSTATECHANGED in every shape, with and without a track. */
	@Test
	@Timeout (60)
	public void notificationsCountOnlyChangedRows () throws Throwable {
		Table table = plainTable (SWT.MULTI);
		table.setItemCount (10);
		shell.open ();
		drain ();
		Object viewport = viewport (table);
		notifyItem (viewport, itemChanged (table, 2, OS.LVIS_SELECTED, 0, OS.LVIF_STATE));
		Object track = begin (viewport, OS.WM_LBUTTONDOWN, 0);
		set (table, "ignoreSelect", true);
		notifyItem (viewport, itemChanged (table, 2, OS.LVIS_SELECTED, 0, OS.LVIF_STATE));
		notifyState (viewport, stateChanged (table, 0, 3, OS.LVIS_SELECTED, 0));
		set (table, "ignoreSelect", false);
		assertFalse ((Boolean) get (track, "changed"), "ignored notifications change nothing");
		notifyItem (viewport, itemChanged (table, 2, OS.LVIS_SELECTED, 0, OS.LVIF_TEXT));
		notifyItem (viewport, itemChanged (table, -1, OS.LVIS_FOCUSED, 0, OS.LVIF_STATE));
		notifyItem (viewport, itemChanged (table, 3, OS.LVIS_FOCUSED, 0, OS.LVIF_STATE));
		assertFalse ((Boolean) get (track, "changed"), "text and focus-only changes are no selection change");
		notifyItem (viewport, itemChanged (table, -1, 0, OS.LVIS_SELECTED, OS.LVIF_STATE));
		assertFalse ((Boolean) get (track, "changed"), "deselecting rows that were not selected changes none");
		notifyItem (viewport, itemChanged (table, -1, OS.LVIS_SELECTED | OS.LVIS_FOCUSED, OS.LVIS_FOCUSED, OS.LVIF_STATE));
		assertTrue ((Boolean) get (track, "changed"), "selecting every row changes rows");
		set (track, "changed", false);
		notifyItem (viewport, itemChanged (table, 4, OS.LVIS_SELECTED, 0, OS.LVIF_STATE));
		assertFalse ((Boolean) get (track, "changed"), "a row selected again is no change");
		notifyItem (viewport, itemChanged (table, 4, 0, OS.LVIS_SELECTED, OS.LVIF_STATE));
		assertTrue ((Boolean) get (track, "changed"), "a row deselected is a change");
		set (track, "changed", false);
		notifyState (viewport, stateChanged (table, 1, 3, OS.LVIS_FOCUSED, 0));
		assertFalse ((Boolean) get (track, "changed"), "a range change without the selected bit is none");
		notifyState (viewport, stateChanged (table, 3, 5, OS.LVIS_SELECTED, 0));
		assertTrue ((Boolean) get (track, "changed"), "a range selected is a change");
		end (viewport, track, false);
		notifyState (viewport, stateChanged (table, 5, 6, OS.LVIS_SELECTED, 0));
		Table empty = plainTable (SWT.MULTI);
		Object emptyViewport = viewport (empty);
		Object emptyTrack = begin (emptyViewport, OS.WM_LBUTTONDOWN, 0);
		notifyItem (emptyViewport, itemChanged (empty, -1, OS.LVIS_SELECTED, 0, OS.LVIF_STATE));
		assertFalse ((Boolean) get (emptyTrack, "changed"), "every row of an empty table is no row");
		end (emptyViewport, emptyTrack, false);
	}

	/* ---------------------------------------------------------------- facades */

	/** Facades that are not rows, paint facades, rows out of range and the shrink guard answer without side effects. */
	@Test
	@Timeout (60)
	public void facadesOutsideTheRowsAnswerDefensively () throws Throwable {
		Table table = plainTable (SWT.MULTI | SWT.CHECK);
		table.setItemCount (20);
		shell.open ();
		drain ();
		Object viewport = viewport (table);
		Class<?> [] facadeTypes = {int.class, boolean.class};
		assertNull (invoke (viewport, "facade", facadeTypes, -1, true));
		assertNull (invoke (viewport, "facade", facadeTypes, 20, true));
		set (table, "ignoreShrink", true);
		assertNull (invoke (viewport, "facade", facadeTypes, 15, true), "no facade is made while the native control deletes rows");
		set (table, "ignoreShrink", false);
		/* one paint facade per nesting depth; a setter on a paint facade writes nothing back */
		Field painting = field (viewport.getClass (), "painting");
		for (int depth = 1; depth <= 5; depth++) {
			painting.setInt (viewport, depth);
			TableItem paint = (TableItem) invoke (viewport, "facade", facadeTypes, 16, true);
			assertNotNull (paint);
			invoke (viewport, "store", ITEM, paint);
		}
		painting.setInt (viewport, 0);
		assertFalse ((Boolean) invoke (viewport, "hasState", INT, 16), "stores of paint facades are ignored");
		TableItem orphan = orphan (table);
		orphan.setText ("not a row");
		invoke (viewport, "store", ITEM, orphan);
		invoke (viewport, "created", ITEM, orphan);
		invoke (viewport, "release", ITEM, orphan);
		invoke (viewport, "destroyItem", ITEM, orphan);
		assertEquals (-1, (int) (Integer) invoke (viewport, "indexOf", ITEM, orphan));
		assertEquals (20, table.getItemCount (), "an orphan facade changes no row");
		/* release keeps facades the lanes cannot replace: in the recent ring, disposed, with listeners */
		TableItem recent = table.getItem (3);
		invoke (viewport, "release", ITEM, recent);
		assertSame (recent, table.getItem (3));
		TableItem zoom = table.getItem (4);
		Listener nothing = event -> {};
		zoom.addListener (SWT.ZoomChanged, nothing);
		assertFalse ((Boolean) invoke (viewport.getClass (), "hasListeners", ITEM, zoom), "a ZoomChanged listener does not pin");
		zoom.addListener (SWT.Selection, nothing);
		zoom.removeListener (SWT.Selection, nothing);
		assertFalse ((Boolean) invoke (viewport.getClass (), "hasListeners", ITEM, zoom), "a removed listener does not pin");
		zoom.addListener (SWT.Selection, nothing);
		assertTrue ((Boolean) invoke (viewport.getClass (), "hasListeners", ITEM, zoom));
		TableItem disposed = table.getItem (5);
		disposed.dispose ();
		invoke (viewport, "release", ITEM, disposed);
		assertEquals (19, table.getItemCount ());
		/* row state of each lane kind */
		Font font = new Font (display, display.getSystemFont ().getFontData () [0].getName (), 11, SWT.BOLD);
		try {
			table.getItem (6).setFont (font);
			table.getItem (7).setChecked (true);
			table.getItem (8).setGrayed (true);
			assertTrue ((Boolean) invoke (viewport, "hasState", INT, 6), "a row font is state");
			assertTrue ((Boolean) invoke (viewport, "hasState", INT, 7), "a check is state");
			assertTrue ((Boolean) invoke (viewport, "hasState", INT, 8), "a gray is state");
			assertFalse ((Boolean) invoke (viewport, "hasState", INT, 9));
		} finally {
			table.getItem (6).setFont (null);
			font.dispose ();
		}
	}

	/* ---------------------------------------------------------------- paint helpers */

	/** The paint-facade array helpers and the draw stages the Table never answers. */
	@Test
	@Timeout (60)
	public void paintHelpersAnswerEveryShape () throws Throwable {
		Table table = plainTable (SWT.MULTI);
		table.setItemCount (5);
		shell.open ();
		drain ();
		Object viewport = viewport (table);
		Class<?> type = viewport.getClass ();
		Class<?> [] clearedTypes = {Object [].class, int.class, IntFunction.class};
		IntFunction<String []> make = String []::new;
		String [] kept = {"a", "b"};
		assertSame (kept, invoke (type, "cleared", clearedTypes, kept, 2, make), "an array of the right length is reused");
		assertNull (kept [0], "and cleared");
		assertEquals (3, ((Object []) invoke (type, "cleared", clearedTypes, kept, 3, make)).length, "another length makes a new one");
		assertEquals (2, ((Object []) invoke (type, "cleared", clearedTypes, null, 2, make)).length);
		Class<?> [] intTypes = {int [].class, int.class};
		int [] ints = {4, 5};
		assertSame (ints, invoke (type, "cleared", intTypes, ints, 2));
		assertEquals (-1, ints [1]);
		assertEquals (3, ((int []) invoke (type, "cleared", intTypes, ints, 3)).length);
		assertEquals (1, ((int []) invoke (type, "cleared", intTypes, null, 1)).length);
		Class<?> [] spareTypes = {Object [].class, int.class};
		assertNull (invoke (type, "spare", spareTypes, null, 1), "a facade for the application has no kept arrays");
		assertSame (kept, invoke (type, "spare", spareTypes, new Object [] {null, kept}, 1));
		NMLVCUSTOMDRAW nmcd = new NMLVCUSTOMDRAW ();
		nmcd.dwDrawStage = OS.CDDS_PREERASE;
		assertFalse ((Boolean) invoke (type, "drawsStage", new Class<?> [] {NMLVCUSTOMDRAW.class}, nmcd), "the Table answers no erase stage");
		assertNull (invoke (viewport, "customDraw", new Class<?> [] {NMLVCUSTOMDRAW.class, long.class, long.class}, nmcd, 0L, 0L));
		assertEquals (0, (int) (Integer) invoke (viewport, "textWidth", new Class<?> [] {String.class}, ""), "an empty text has no width");
		Field painting = field (type, "painting");
		painting.setInt (viewport, 1);
		assertFalse ((Boolean) invoke (viewport, "appending", LONG, 1L), "no append is batched while painting");
		painting.setInt (viewport, 0);
		assertFalse ((Boolean) invoke (viewport, "appending", LONG, 0L), "an insert without an item is no append");
		int top = table.getTopIndex ();
		invoke (viewport, "restoreTop", INT, top);
		assertEquals (top, table.getTopIndex (), "restoring the current top moves nothing");
	}
}

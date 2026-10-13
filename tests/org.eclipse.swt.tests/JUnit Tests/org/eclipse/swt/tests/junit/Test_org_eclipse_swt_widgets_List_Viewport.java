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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * SWT.VIRTUAL List (owner-data viewport): rows are materialized only when they
 * are drawn or read, and a virtual list answers every API call like a plain
 * list holding the same items. The item count is 200,000 by default and
 * 10,000,000 with {@code -Dswt.list.scaleTest=true}.
 */
public class Test_org_eclipse_swt_widgets_List_Viewport {
	static final int COUNT = Boolean.getBoolean ("swt.list.scaleTest") ? 10_000_000 : 200_000;
	/* visible rows of a 300 px list plus the overscan any native paint may ask for */
	static final int MATERIALIZED_BOUND = 64;
	Display display;
	Shell shell;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setLayout (null);
		shell.setBounds (20, 20, 300, 360);
	}

	@AfterEach
	public void tearDown () {
		shell.dispose ();
	}

	private List virtualList (int style, int [] setData) {
		List list = new List (shell, style | SWT.VIRTUAL | SWT.V_SCROLL);
		list.setBounds (5, 5, 260, 300);
		list.addListener (SWT.SetData, event -> {
			setData [0]++;
			list.setItem (event.index, "item " + event.index);
		});
		return list;
	}

	private void paint (List list) {
		list.redraw ();
		list.update ();
		while (display.readAndDispatch ()) {
			// drain
		}
	}

	@Test
	public void setItemCountMaterializesOnlyPaintedRows () {
		int [] setData = {0};
		List list = virtualList (SWT.MULTI, setData);
		list.setItemCount (COUNT);
		shell.open ();
		paint (list);
		assertEquals (COUNT, list.getItemCount ());
		int first = setData [0];
		assertTrue (first > 0 && first <= MATERIALIZED_BOUND, "rows materialized by the first paint: " + first);
		list.setTopIndex (COUNT - 1);
		paint (list);
		assertTrue (list.getTopIndex () > COUNT - MATERIALIZED_BOUND, "top " + list.getTopIndex ());
		int afterScroll = setData [0];
		assertTrue (afterScroll - first <= MATERIALIZED_BOUND, "rows materialized by scroll-to-end: " + (afterScroll - first));
		assertEquals ("item " + (COUNT / 2), list.getItem (COUNT / 2));
		assertEquals (afterScroll + 1, setData [0], "a read materializes exactly the row it names");
		assertEquals ("item " + (COUNT / 2), list.getItem (COUNT / 2));
		assertEquals (afterScroll + 1, setData [0], "a materialized row is not requested again");
	}

	@Test
	public void selectionAndRemovalShiftLikeAPlainList () {
		int [] setData = {0};
		List list = virtualList (SWT.MULTI, setData);
		list.setItemCount (COUNT);
		list.select (COUNT - 10, COUNT - 1);
		list.select (3);
		assertEquals (11, list.getSelectionCount ());
		list.remove (0, COUNT / 2 - 1);
		assertEquals (COUNT - COUNT / 2, list.getItemCount ());
		int [] expected = new int [10];
		for (int i = 0; i < 10; i++) expected [i] = COUNT - COUNT / 2 - 10 + i;
		assertArrayEquals (expected, list.getSelectionIndices ());
		assertEquals ("item " + (COUNT - 1), list.getItem (list.getItemCount () - 1));
		list.clear (list.getItemCount () - 1);
		int before = setData [0];
		assertEquals ("item " + (list.getItemCount () - 1), list.getItem (list.getItemCount () - 1));
		assertEquals (before + 1, setData [0], "clear forgets the row text");
		list.setItemCount (5);
		assertEquals (5, list.getItemCount ());
		assertEquals (0, list.getSelectionCount ());
	}

	@Test
	public void setItemsKeepsOneReferencePerItemAndAnswersExactly () {
		String [] items = new String [COUNT];
		for (int i = 0; i < COUNT; i++) items [i] = "row " + i;
		int [] setData = {0};
		List list = virtualList (SWT.SINGLE, setData);
		long t0 = System.nanoTime ();
		list.setItems (items);
		long millis = (System.nanoTime () - t0) / 1_000_000;
		assertEquals (COUNT, list.getItemCount ());
		assertEquals ("row " + (COUNT - 1), list.getItem (COUNT - 1));
		assertEquals (COUNT - 1, list.indexOf ("row " + (COUNT - 1)));
		assertEquals (0, setData [0], "items given by setItems never ask for SetData");
		list.setSelection (COUNT - 2);
		assertEquals (COUNT - 2, list.getSelectionIndex ());
		list.add ("head", 0);
		assertEquals (COUNT - 1, list.getSelectionIndex (), "single selection shifts on insert");
		list.remove (0);
		assertEquals (COUNT - 2, list.getSelectionIndex (), "single selection shifts on remove");
		assertTrue (millis < 5_000, "setItems took " + millis + " ms");
		Object store = field (list, "itemStore");
		assertNotNull (store);
		String [] stored = (String []) field (store, "items");
		assertTrue (stored.length < COUNT + COUNT / 2 + 16, "store capacity " + stored.length);
	}

	@Test
	public void plainListKeepsTheNativeStrings () {
		List list = new List (shell, SWT.MULTI);
		list.setItems ("a", "b");
		assertNull (field (list, "itemStore"), "a list without SWT.VIRTUAL has no Java item store");
	}

	@Test
	public void virtualAndPlainListsAnswerTheSameForTheSameItems () {
		String [] items = new String [500];
		for (int i = 0; i < items.length; i++) items [i] = (i % 7 == 0 ? "" : "w" + (i % 13)) + (i % 5 == 0 ? "\0tail" : "");
		for (int style : new int [] {SWT.SINGLE, SWT.MULTI, SWT.MULTI | SWT.H_SCROLL}) {
			List plain = new List (shell, style | SWT.V_SCROLL);
			List virtual = new List (shell, style | SWT.V_SCROLL | SWT.VIRTUAL);
			plain.setBounds (0, 0, 200, 200);
			virtual.setBounds (0, 0, 200, 200);
			plain.setItems (items);
			virtual.setItems (items);
			assertArrayEquals (plain.getItems (), virtual.getItems ());
			assertEquals (plain.getItemHeight (), virtual.getItemHeight ());
			assertEquals (plain.computeSize (SWT.DEFAULT, SWT.DEFAULT), virtual.computeSize (SWT.DEFAULT, SWT.DEFAULT));
			for (String probe : new String [] {"", "w3", "w3\0tail", "w12", "absent"}) {
				for (int start : new int [] {0, 1, 250, 499}) {
					assertEquals (plain.indexOf (probe, start), virtual.indexOf (probe, start), probe + "@" + start);
				}
			}
			plain.remove (10, 20);
			virtual.remove (10, 20);
			plain.remove (new int [] {5, 6, 7, 100, 300});
			virtual.remove (new int [] {5, 6, 7, 100, 300});
			assertArrayEquals (plain.getItems (), virtual.getItems ());
			assertEquals (plain.computeSize (SWT.DEFAULT, SWT.DEFAULT), virtual.computeSize (SWT.DEFAULT, SWT.DEFAULT));
			plain.dispose ();
			virtual.dispose ();
		}
	}

	@Test
	public void accessibleNamesComeFromTheItemStore () throws Exception {
		int [] setData = {0};
		List list = virtualList (SWT.SINGLE, setData);
		list.setItemCount (100);
		shell.open ();
		paint (list);
		org.eclipse.swt.internal.win32.OS.SendMessage (list.handle, org.eclipse.swt.internal.win32.OS.WM_GETOBJECT, 0, org.eclipse.swt.internal.win32.OS.OBJID_CLIENT);
		Accessible accessible = list.getAccessible ();
		@SuppressWarnings ("unchecked")
		java.util.List<AccessibleListener> listeners = new ArrayList<> ((java.util.List<AccessibleListener>) field (accessible, "accessibleListeners"));
		assertFalse (listeners.isEmpty (), "the owner-data list names its rows");
		AccessibleEvent event = new AccessibleEvent (accessible);
		event.childID = 42;
		for (AccessibleListener listener : listeners) listener.getName (event);
		assertEquals ("item 42", event.result);
	}

	static Object field (Object target, String name) {
		for (Class<?> type = target.getClass (); type != null; type = type.getSuperclass ()) {
			try {
				Field field = type.getDeclaredField (name);
				field.setAccessible (true);
				return field.get (target);
			} catch (NoSuchFieldException e) {
				// superclass
			} catch (IllegalAccessException e) {
				throw new IllegalStateException (e);
			}
		}
		throw new IllegalStateException ("no field " + name + " in " + target.getClass () + " " + Arrays.toString (new Object [0]));
	}
}

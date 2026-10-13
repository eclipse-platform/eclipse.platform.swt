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
	/* visible rows of a 300 px list (20) + 2 x overscan (2 x 8) + one prefetch page (20), with slack */
	static final int MATERIALIZED_BOUND = 96;
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

	/* rows asked for more than once within one residency; must stay empty */
	final java.util.Set<Integer> requested = new java.util.HashSet<> ();
	final java.util.List<Integer> repeated = new ArrayList<> ();

	private List virtualList (int style, int [] setData) {
		List list = new List (shell, style | SWT.VIRTUAL | SWT.V_SCROLL);
		list.setBounds (5, 5, 260, 300);
		list.addListener (SWT.SetData, event -> {
			setData [0]++;
			if (!requested.add (event.index)) repeated.add (event.index);
			list.setItem (event.index, "item " + event.index);
		});
		return list;
	}

	/* Lets the debounced overscan/prefetch steps run (STORE_PREFETCH_DELAY is 40 ms). */
	private void settle () {
		long end = System.currentTimeMillis () + 400;
		while (System.currentTimeMillis () < end) {
			if (!display.readAndDispatch ()) {
				try {
					Thread.sleep (10);
				} catch (InterruptedException e) {
					Thread.currentThread ().interrupt ();
					return;
				}
			}
		}
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
		settle ();
		assertEquals (COUNT, list.getItemCount ());
		int first = setData [0];
		int top = list.getTopIndex ();
		Object store = field (list, "itemStore");
		for (int row = top; row < top + 28; row++) {
			assertTrue (isMaterialized (store, row), "visible and overscan row " + row + " is requested after the debounce");
		}
		assertTrue (first > 0 && first <= MATERIALIZED_BOUND, "rows materialized by the first paint: " + first);
		list.setTopIndex (COUNT - 1);
		paint (list);
		settle ();
		assertTrue (list.getTopIndex () > COUNT - MATERIALIZED_BOUND, "top " + list.getTopIndex ());
		int afterScroll = setData [0];
		assertTrue (afterScroll - first <= MATERIALIZED_BOUND, "rows materialized by scroll-to-end: " + (afterScroll - first));
		assertEquals ("item " + (COUNT / 2), list.getItem (COUNT / 2));
		assertEquals (afterScroll + 1, setData [0], "a read materializes exactly the row it names");
		assertEquals ("item " + (COUNT / 2), list.getItem (COUNT / 2));
		assertEquals (afterScroll + 1, setData [0], "a materialized row is not requested again");
		assertTrue (repeated.isEmpty (), "SetData once per row per residency, repeated: " + repeated);
	}

	private static boolean isMaterialized (Object store, int row) {
		try {
			java.lang.reflect.Method method = store.getClass ().getDeclaredMethod ("isMaterialized", int.class);
			method.setAccessible (true);
			return (Boolean) method.invoke (store, row);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException (e);
		}
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
	public void bulkChangesFreezeOnlyTheContentArea () {
		int [] setData = {0};
		List list = virtualList (SWT.MULTI, setData);
		shell.open ();
		String [] items = new String [5000];
		for (int i = 0; i < items.length; i++) items [i] = "bulk " + i;
		list.setItems (items);
		assertEquals (0, ((Integer) field (list, "storeBits")) & (1 << 5), "the content freeze ends with the bulk change");
		org.eclipse.swt.internal.win32.SCROLLINFO info = new org.eclipse.swt.internal.win32.SCROLLINFO ();
		info.cbSize = org.eclipse.swt.internal.win32.SCROLLINFO.sizeof;
		info.fMask = org.eclipse.swt.internal.win32.OS.SIF_RANGE;
		org.eclipse.swt.internal.win32.OS.GetScrollInfo (list.handle, org.eclipse.swt.internal.win32.OS.SB_VERT, info);
		assertEquals (items.length - 1, info.nMax, "the scroll bar follows the bulk change");
		list.remove (100, 4000);
		assertEquals (0, ((Integer) field (list, "storeBits")) & (1 << 5));
		assertEquals ("bulk 4001", list.getItem (100));
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
	public void scrollRoundTripReproducesStateAndPixels () {
		for (int style : new int [] {SWT.SINGLE, SWT.MULTI}) {
			int [] setData = {0};
			List list = virtualList (style, setData);
			list.setItemCount (COUNT);
			shell.open ();
			list.setTopIndex (1000);
			if ((style & SWT.MULTI) != 0) {
				list.setSelection (new int [] {1002, 1005, 1006});
			} else {
				list.setSelection (1003);
			}
			list.setTopIndex (1000);
			paint (list);
			settle ();
			String before = state (list) + " pixels=" + pixelHash (list);
			int callsBefore = setData [0];
			list.setTopIndex (COUNT - 1);
			paint (list);
			settle ();
			list.setTopIndex (COUNT / 2);
			paint (list);
			settle ();
			int callsAway = setData [0];
			list.setTopIndex (1000);
			paint (list);
			settle ();
			String after = state (list) + " pixels=" + pixelHash (list);
			assertEquals (before, after, "scroll away and back must reproduce API state and pixels");
			assertTrue (callsAway - callsBefore <= 2 * MATERIALIZED_BOUND, "SetData for two away pages: " + (callsAway - callsBefore));
			assertEquals (callsAway, setData [0], "returning to materialized rows sends no SetData");
			assertTrue (repeated.isEmpty (), "SetData once per row per residency, repeated: " + repeated);
			requested.clear ();
			list.dispose ();
		}
	}

	private static String state (List list) {
		return "top=" + list.getTopIndex () + " focus=" + list.getFocusIndex () + " selection=" + Arrays.toString (list.getSelectionIndices ())
			+ " count=" + list.getItemCount () + " item=" + list.getItem (list.getTopIndex ());
	}

	private String pixelHash (List list) {
		org.eclipse.swt.graphics.Point size = list.getSize ();
		org.eclipse.swt.graphics.Image image = new org.eclipse.swt.graphics.Image (display, size.x, size.y);
		org.eclipse.swt.graphics.GC gc = new org.eclipse.swt.graphics.GC (image);
		try {
			list.print (gc);
		} finally {
			gc.dispose ();
		}
		org.eclipse.swt.graphics.ImageData data = image.getImageData ();
		image.dispose ();
		/* capture mode: Control.print (WM_PRINT); the owner-drawn rows must be in the image, not a blank client area */
		java.util.Set<Integer> colors = new java.util.HashSet<> ();
		for (int y = 0; y < data.height && colors.size () < 3; y++) {
			for (int x = 0; x < data.width && colors.size () < 3; x++) {
				colors.add (data.getPixel (x, y));
			}
		}
		assertTrue (colors.size () >= 3, "print rendered the rows (distinct colours: " + colors.size () + ")");
		return Integer.toHexString (Arrays.hashCode (data.data));
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

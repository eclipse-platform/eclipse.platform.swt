/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SegmentListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Combo
 *
 * @see org.eclipse.swt.widgets.Combo
 */
public class Test_org_eclipse_swt_widgets_Combo extends Test_org_eclipse_swt_widgets_Composite {

	Combo combo;

@Override
@Before
public void setUp() {
	super.setUp();
	combo = new Combo(shell, 0);
	setWidget(combo);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	assertThrows("No exception thrown for parent == null", IllegalArgumentException.class, () ->
		combo = new Combo(null, 0));

	int[] cases = {SWT.DROP_DOWN, SWT.SIMPLE};
	for (int i = 0; i < cases.length; i++) {
		combo = new Combo(shell, cases[i]);
		assertEquals(":a:" + String.valueOf(i), cases[i], (combo.getStyle() & cases[i]));
	}
}

@Test
public void test_addLjava_lang_String() {
	assertThrows("No exception thrown for item == null", IllegalArgumentException.class, () ->
		combo.add(null));

	combo.add("");
	assertArrayEquals(":a:", new String[]{""}, combo.getItems());
	combo.add("");
	assertArrayEquals(":b:", new String[]{"", ""}, combo.getItems());
	combo.add("fred");
	assertArrayEquals(":c:", new String[]{"", "", "fred"}, combo.getItems());

}

@Test
public void test_addLjava_lang_StringI() {
	assertThrows("No exception thrown for index == null", IllegalArgumentException.class, () ->
		combo.add(null, 0));

	assertThrows("No exception thrown for index < 0", IllegalArgumentException.class, () ->
		combo.add("string", -1));

	combo.add("string0", 0);
	assertThrows("No exception thrown for index > size", IllegalArgumentException.class, () ->
		combo.add("string1", 2));
	combo.removeAll();

	combo.add("fred", 0);
	assertArrayEquals("fred", new String[]{"fred"}, combo.getItems());
	combo.add("fred", 0);
	assertArrayEquals("fred fred", new String[]{"fred", "fred"}, combo.getItems());
	combo.add("fred");
	assertArrayEquals("fred fred fred", new String[]{"fred", "fred", "fred"}, combo.getItems());
	combo.removeAll();

	int number = 3;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	combo.add("fred", number);
	assertArrayEquals("fred0 fred1 fred2 fred", new String[]{"fred0", "fred1", "fred2", "fred"}, combo.getItems());

	combo.removeAll();
	number = 3;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	combo.add("fred", 1);
	assertArrayEquals("fred0 fred fred1 fred2", new String[]{"fred0", "fred", "fred1", "fred2"}, combo.getItems());
	combo.add("fred", 0);
	assertArrayEquals("fred fred0 fred fred1 fred2", new String[]{"fred", "fred0", "fred", "fred1", "fred2"}, combo.getItems());
	combo.add("fred", 4);
	assertArrayEquals("fred fred0 fred fred1 fred fred2", new String[]{"fred", "fred0", "fred", "fred1", "fred", "fred2"}, combo.getItems());
}

@Test
public void test_addModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	ModifyListener listener = event -> listenerCalled = true;
	assertThrows(IllegalArgumentException.class, () -> combo.addModifyListener(null));

	// test whether all content modifying API methods send a Modify event
	combo.addModifyListener(listener);
	listenerCalled = false;
	combo.setText("new text");
	assertTrue("setText does not send event", listenerCalled);

	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded few test scenarios in test_addModifyListenerLorg_eclipse_swt_events_ModifyListener(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo).");
		}
	}
	else {
		combo.removeAll();
		combo.add("one");
		combo.select(0);
		listenerCalled = false;
		combo.remove(0);
		assertTrue("remove(int index) for last item:", listenerCalled);

		combo.removeAll();
		combo.add("one");
		combo.add("two");
		combo.select(0);
		listenerCalled = false;
		combo.remove(0, 1);
		assertTrue("remove(int start, int end) for all items:", listenerCalled);
	}

	listenerCalled = false;
	combo.removeModifyListener(listener);
	// cause to call the listener.
	combo.setText("line");
	assertFalse("Listener not removed", listenerCalled);
	assertThrows(IllegalArgumentException.class, () ->
		combo.removeModifyListener(null));
}

@Test
public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			listenerCalled = true;
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent event) {
		}
	};
	assertThrows(IllegalArgumentException.class, () -> combo.addSelectionListener(null));
	combo.addSelectionListener(listener);
	combo.select(0);
	assertFalse(":a:", listenerCalled);
	combo.removeSelectionListener(listener);
	assertThrows(IllegalArgumentException.class, () ->	combo.removeSelectionListener(null));
}

@Test
public void test_addSelectionListenerWidgetSelectedAdapterLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = SelectionListener.widgetSelectedAdapter(e-> listenerCalled = true);

	combo.addSelectionListener(listener);
	combo.select(0);
	assertFalse(":a:", listenerCalled);

	combo.removeSelectionListener(listener);
	listenerCalled = false;
	combo.select(0);
	assertFalse(listenerCalled);
}

@Test
public void test_clearSelection() {
	int number = 5;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	combo.clearSelection();
	assertEquals(":a:",new Point(0, 0), combo.getSelection());
	combo.setSelection(new Point(0, 5));
	assertEquals(":b:",new Point(0, 0), combo.getSelection());  //nothing is selected
	combo.setText("some text");
	combo.setSelection(new Point(0, 5));
	assertEquals(":c:",new Point(0, 5), combo.getSelection());
	combo.clearSelection();
	assertEquals(":d:", combo.getSelection().x, combo.getSelection().y);
}

@Override
@Test
public void test_computeSizeIIZ() {
	// super class test is sufficient
}

@Test
public void test_copy() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_copy(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo).");
		}
		return;
	}
	combo.setText("123456");
	combo.setSelection(new Point(1,3));
	combo.copy();
	combo.setSelection(new Point(0,0));
	combo.paste();
	assertEquals(":a:","23123456", combo.getText());
}

@Test
public void test_cut() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_cut(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo).");
		}
		return;
	}
	combo.setText("123456");
	combo.setSelection(new Point(1,3));
	combo.cut();
	assertEquals(":a:","1456", combo.getText());
}

@Test
public void test_deselectAll() {
	combo.add("123");
	combo.add("456");
	combo.add("789");
	combo.select(0);
	combo.select(2);
	combo.deselectAll();
	assertEquals(":a:",-1, combo.getSelectionIndex());
}
@Test
public void test_deselectI() {
	// indices out of range are ignored
	String[] items = {"item0", "item1", "item2"};
	combo.setItems(items);
	combo.select(1);
	combo.deselect(10);
	assertEquals(1, combo.getSelectionIndex());
	combo.removeAll();

	combo.deselect(2);

	int number = 10;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	for (int i = 0; i < number; i++) {
		combo.select(i);
		assertEquals(":a:" + i,i, combo.getSelectionIndex());
		combo.deselect(i);
		assertEquals(":b:" + i,-1, combo.getSelectionIndex());
	}
}

@Override
@Test
public void test_getChildren() {
	// Combo cannot have children
}

@Test
public void test_getItemCount() {
	int number = 10;
	for (int i = 0; i < number; i++) {
		assertEquals(":a:" + i,i,  combo.getItemCount());
		combo.add("fred" + i);
	}
	assertEquals(":aa:", number, combo.getItemCount());

	for (int i = 0; i < number; i++) {
		assertEquals(":b:" + i, number-i, combo.getItemCount());
		combo.remove(0);
	}
	combo.removeAll();
	assertEquals(":c:", 0, combo.getItemCount());
}

@Test
public void test_getItemHeight() {
	combo.getItemHeight();
}

@Test
public void test_getItemI() {
	assertThrows(IllegalArgumentException.class, () -> combo.getItem(0));

	int number = 10;
	for (int i = 0; i < number; i++) {
		combo.add("fred" + i);
	}
	for (int i = 0; i < number; i++)
		assertEquals("fred" + i, combo.getItem(i));
}

@Test
public void test_getItems() {
	combo.removeAll();
	combo.add("1");
	combo.add("2");
	combo.add("3");
	String[] items = combo.getItems();
	assertEquals(":a:", 3, items.length);
	assertEquals(":a:", "1", items[0]);
	assertEquals(":a:", "2", items[1]);
	assertEquals(":a:", "3", items[2]);
}

@Test
public void test_getOrientation() {
	// tested in setOrientation
}

@Test
public void test_getSelection() {
	combo.setText("123456");
	combo.setSelection(new Point(1,3));
	combo.getSelection();
	assertEquals(":a:", new Point(1,3), combo.getSelection());
}

@Test
public void test_getSelectionIndex() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getSelectionIndex(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo)");
		}
		return;
	}
	int number = 5;
	for (int i = 0; i < number; i++) {
		combo.add("fred");
	}
	assertEquals(-1, combo.getSelectionIndex());
	for (int i = 0; i < number; i++) {
		combo.select(i);
		assertEquals(i, combo.getSelectionIndex());
	}

	combo.removeAll();
	for (int i = 0; i < number; i++) {
		combo.add("fred");
	}
	assertEquals(-1, combo.getSelectionIndex());
	for (int i = 0; i < number; i++) {
		combo.select(i);
		combo.deselect(i);
		assertEquals(-1, combo.getSelectionIndex());
	}
}

@Test
public void test_getSelectionIndex_duringSelect() {
	final AtomicInteger duringModification = new AtomicInteger(-2);
	combo.add("abc");
	combo.addListener(SWT.Modify, evt -> {
		duringModification.set(combo.getSelectionIndex());
	});

	combo.select(0);

	assertEquals(0, duringModification.get());
}

@Test
public void test_getText() {
	String[] cases = {"", "fred", "fredfred"};
	for (int i = 0; i < cases.length; i++) {
		combo.setText(cases[i]);
		assertEquals(":a:" + String.valueOf(i), cases[i], combo.getText());
	}
}

@Test
public void test_getTextHeight() {
	combo.getTextHeight();
}

@Test
public void test_getTextLimit() {
	combo.setTextLimit(3);
	assertEquals(":a:", 3, combo.getTextLimit());
}

@Test
public void test_hasFocus() {
	// not public api
}

@Test
public void test_indexOfLjava_lang_String() {
	combo.add("string0");
	assertThrows(IllegalArgumentException.class, () -> combo.indexOf(null));
	combo.removeAll();

	int number = 5;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	for (int i = 0; i < number; i++)
		assertEquals(i, combo.indexOf("fred" + i));

	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	combo.removeAll();
	for (int i = 0; i < number; i++)
		assertEquals(-1, combo.indexOf("fred" + i));

	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	combo.remove("fred3");
	for (int i = 0; i < 3; i++)
		assertEquals(i, combo.indexOf("fred" + i));
	assertEquals(-1, combo.indexOf("fred3"));
	for (int i = 4; i < number; i++)
		assertEquals(i - 1, combo.indexOf("fred" + i));

	combo.removeAll();
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	combo.remove(2);
	for (int i = 0; i < 2; i++)
		assertEquals(i, combo.indexOf("fred" + i));
	assertEquals(-1, combo.indexOf("fred2"));
	for (int i = 3; i < number; i++)
		assertEquals(i - 1, combo.indexOf("fred" + i));
}

@Test
public void test_indexOfLjava_lang_StringI() {
	combo.add("string0");
	assertThrows(IllegalArgumentException.class, () ->	combo.indexOf(null));
	assertEquals(-1, combo.indexOf("string0", -1));
	combo.removeAll();

	int number = 5;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	for (int i = 0; i < number; i++)
		assertEquals(":a:" + i, i, combo.indexOf("fred" + i, 0));
	for (int i = 0; i < number; i++)
		assertEquals(":b:" + i, -1, combo.indexOf("fred" + i, i + 1));

	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	for (int i = 0; i < 3; i++)
		assertEquals(":a:" + i, i, combo.indexOf("fred" + i, 0));
	for (int i = 3; i < number; i++)
		assertEquals(":b:" + i, i, combo.indexOf("fred" + i, 3));
	for (int i = 0; i < number; i++)
		assertEquals(":b:" + i, i, combo.indexOf("fred" + i, i));
}

@Test
public void test_paste() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_paste(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo).");
		}
		return;
	}
	combo.setText("123456");
	combo.setSelection(new Point(1,3));
	combo.cut();
	assertEquals(":a:", "1456", combo.getText());
	combo.paste();
	assertEquals(":a:", "123456", combo.getText());
}

@Test
public void test_removeAll() {
	combo.add("1");
	combo.add("2");
	combo.removeAll();
	assertEquals(":a:", 0, combo.getItems().length);
}

@Test
public void test_removeI() {
	assertThrows(IllegalArgumentException.class, () ->	combo.remove(0));

	assertThrows(IllegalArgumentException.class, () ->	combo.remove(3));

	combo.add("string0");
	assertThrows(IllegalArgumentException.class, () ->	combo.remove(-1));
	combo.removeAll();

	int number = 5;
	for (int i = 0; i < number; i++) {
		combo.add("fred" + i);
	}
	for (int i = 0; i < number; i++) {
		assertEquals("Wrong number of items", number - i, combo.getItemCount());
		combo.remove(0);
	}

	for (int i = 0; i < number; i++) {
		combo.add("fred");  // all items the same
	}
	for (int i = 0; i < number; i++) {
		assertEquals("Wrong number of items", number - i, combo.getItemCount());
		combo.remove(0);
	}

	for (int i = 0; i < number; i++) {
		combo.add("fred" + i); // different items
	}
	for (int i = 0; i < number; i++) {
		assertEquals("index " + i, number - i, combo.getItemCount());
		combo.select(0);
		assertEquals("index " + i, 0, combo.getSelectionIndex());
		combo.remove(0);
		if (SwtTestUtil.isWindows || SwtTestUtil.isGTK ) {
			// The behavior on Windows and GTK when the selected item is removed
			// is to simply say that no items are selected.
			assertEquals("index " + i, -1, combo.getSelectionIndex());
		} else {
			// The behavior on other platforms when the selected item is removed
			// is to select the item that is now at the same index, and send a
			// selection event. If there is no item at the selected index, then
			// the platform says that no items are selected.
			if (i < number - 1) {
				assertEquals("index " + i, 0, combo.getSelectionIndex());
			} else {
				assertEquals("index " + i, -1, combo.getSelectionIndex());
			}
		}
	}

	for (int i = 0; i < number; i++)
		combo.add("fred" + i); // different items
	for (int i = 0; i < number; i++) {
		assertEquals("index " + i, number - i, combo.getItemCount());
		combo.remove(number-i-1);
	}
}

@Test
public void test_removeII() {
	int number = 5;
	for (int i = 0; i < number; i++) {
		combo.add("fred");
	}
	combo.remove(0, 4);
	assertEquals(0, combo.getItemCount());

	combo.removeAll();
	for (int i = 0; i < number; i++) {
		combo.add("fred");
	}
	combo.remove(0, 2);
	assertEquals(2, combo.getItemCount());

	combo.removeAll();
	for (int i = 0; i < number; i++) {
		combo.add("fred");
	}
	combo.remove(2, 4);
	assertEquals(2, combo.getItemCount());

	combo.removeAll();
	for (int i = 0; i < number; i++) {
		combo.add("fred");
	}
	combo.remove(3, 2);
	assertEquals(number, combo.getItemCount());

	combo.removeAll();
	for (int i = 0; i < number; i++) {
		combo.add("fred");
	}

	assertThrows(IllegalArgumentException.class, () -> combo.remove(2, 100));

	assertThrows(IllegalArgumentException.class, () ->	combo.remove(-1, number-1));

	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded few test scenarios in test_removeII(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo).");
		}
	}
	else {
		combo.removeAll();
		combo.add("one");
		combo.select(0);
		combo.remove(0);
		assertTrue(combo.getText().isEmpty());

		combo.removeAll();
		combo.add("one");
		combo.add("two");
		combo.select(0);
		combo.remove(0, 1);
		assertTrue(combo.getText().isEmpty());
	}
}

@Test
public void test_removeLjava_lang_String() {
	int number = 5;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	for (int i = 0; i < number; i++) {
		assertEquals(number - i, combo.getItemCount());
		combo.remove("fred" + i);
	}

	for (int i = 0; i < number; i++)
		combo.add("fred");
	for (int i = 0; i < number; i++) {
		assertEquals(number - i, combo.getItemCount());
		combo.remove("fred");
	}

	for (int i = 0; i < number; i++)
		combo.add("fred");
	assertThrows("No exception thrown for item == null", IllegalArgumentException.class,
			() ->	combo.remove(null));

	combo.removeAll();
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	assertThrows("No exception thrown for item not found", IllegalArgumentException.class,
			() -> combo.remove("fred"));

	assertEquals(number, combo.getItemCount());
}

@Test
public void test_selectI() {
	combo.add("123");
	combo.add("456");
	combo.add("789");
	combo.select(1);
	assertEquals(":a:", 1, combo.getSelectionIndex());

	// indices out of range are ignored
	combo.select(10);
	assertEquals(1, combo.getSelectionIndex());
}

@Test
public void test_setBackgroundDropDownCombo() {
	Combo dropDown = new Combo(shell, SWT.DROP_DOWN);
	Color color = new Color(255, 0, 0);
	dropDown.setBackground(color);
	assertEquals("getBackground not equal after setBackground for SWT.DROP_DOWN Combo",
			color, dropDown.getBackground());
	dropDown.setBackground(null);
	assertNotEquals("getBackground unchanged after setBackground(null) for SWT.DROP_DOWN Combo", color,
			dropDown.getBackground());
	color = new Color(255, 0, 0, 0);
	dropDown.setBackground(color);
	assertEquals("getBackground not equal after setBackground with 0 alpha for SWT.DROP_DOWN Combo",
			color, dropDown.getBackground());
	dropDown.setBackground(null);
	assertNotEquals("getBackground unchanged after setBackground(null) with 0 alpha for SWT.DROP_DOWN Combo", color,
			dropDown.getBackground());
	if ("gtk".equals(SWT.getPlatform ())) {
		Color fg = new Color(0, 255, 0);
		dropDown.setBackground(color);
		dropDown.setForeground(fg);
		assertEquals("Setting a foreground disrupted the background color for SWT.DROP_DOWN Combo",
				color, dropDown.getBackground());
		assertEquals("Setting a foreground onto an SWT.DROP_DOWN Combo with a background failed",
				fg, dropDown.getForeground());
	}
	dropDown.dispose();
}

@Test
public void test_setBackgroundAlphaDropDownCombo() {
	Combo dropDown = new Combo(shell, SWT.DROP_DOWN);
	Color color = new Color (255, 0, 0, 0);
	dropDown.setBackground(color);
	assertEquals(color, dropDown.getBackground());
	Color fg = new Color(0, 255, 0, 0);
	dropDown.setForeground(fg);
	assertEquals(color, dropDown.getBackground());
	dropDown.dispose();
}

@Test
public void test_setBackgroundSimpleCombo() {
	Combo simple = new Combo(shell, SWT.SIMPLE);
	Color color = new Color(255, 0, 0);
	simple.setBackground(color);
	assertEquals("getBackground not equal after setBackground for SWT.SIMPLE Combo",
			color, simple.getBackground());
	simple.setBackground(null);
	assertNotEquals("getBackground unchanged after setBackground(null) for SWT.SIMPLE Combo",
			color, simple.getBackground());
	color = new Color(255, 0, 0, 0);
	simple.setBackground(color);
	assertEquals("getBackground not equal after setBackground with 0 alpha for SWT.SIMPLE Combo",
			color, simple.getBackground());
	simple.setBackground(null);
	assertNotEquals("getBackground unchanged after setBackground(null) with 0 alpha for SWT.SIMPLE Combo",
			color, simple.getBackground());
	if ("gtk".equals(SWT.getPlatform ())) {
		Color fg = new Color(0, 255, 0);
		simple.setBackground(color);
		simple.setForeground(fg);
		assertEquals("Setting a foreground disrupted the background color for SWT.SIMPLE Combo",
				color, simple.getBackground());
		assertEquals("Setting a foreground onto an SWT.SIMPLE Combo with a background failed",
				fg, simple.getForeground());
	}
	simple.dispose();
}

@Test
public void test_setBackgroundAlphaSimpleCombo() {
	Combo simple = new Combo(shell, SWT.SIMPLE);
	Color color = new Color (255, 0, 0, 0);
	simple.setBackground(color);
	assertEquals(color, simple.getBackground());
	Color fg = new Color(0, 255, 0, 0);
	simple.setForeground(fg);
	assertEquals(color, simple.getBackground());
	simple.dispose();
}

@Test
public void test_setForegroundDropDownCombo() {
	Combo dropDown = new Combo(shell, SWT.DROP_DOWN);
	Color color = new Color(255, 0, 0);
	dropDown.setForeground(color);
	assertEquals(color, dropDown.getForeground());
	dropDown.setForeground(null);
	assertNotEquals(color, dropDown.getForeground());
	if ("gtk".equals(SWT.getPlatform ())) {
		Color bg = new Color(0, 255, 0);
		dropDown.setForeground(color);
		dropDown.setBackground(bg);
		assertEquals("Setting a background disrupted the foreground color for SWT.DROP_DOWN Combo",
				color, dropDown.getForeground());
		assertEquals("Setting a background onto an SWT.DROP_DOWN Combo with a foreground failed",
				bg, dropDown.getBackground());
	}
	dropDown.dispose();
}

@Test
public void test_setForegroundAlphaDropDownCombo() {
	Combo dropDown = new Combo(shell, SWT.DROP_DOWN);
	assumeTrue("Alpha support for foreground colors does not exist on Win32",
			SwtTestUtil.isCocoa || SwtTestUtil.isGTK);
	Color color = new Color (255, 0, 0, 0);
	dropDown.setForeground(color);
	assertEquals(color, dropDown.getForeground());
	Color bg = new Color(0, 255, 0, 0);
	dropDown.setBackground(bg);
	assertEquals(color, dropDown.getForeground());
	dropDown.dispose();
}

@Test
public void test_setForegroundSimpleCombo() {
	Combo simple = new Combo(shell, SWT.SIMPLE);
	Color color = new Color(255, 0, 0);
	simple.setForeground(color);
	assertEquals(color, simple.getForeground());
	simple.setForeground(null);
	assertNotEquals(color, simple.getForeground());
	if ("gtk".equals(SWT.getPlatform ())) {
		Color bg = new Color(0, 255, 0);
		simple.setForeground(color);
		simple.setBackground(bg);
		assertEquals("Setting a background disrupted the foreground color for SWT.SIMPLE Combo",
				color, simple.getForeground());
		assertEquals("Setting a background onto an SWT.SIMPLE Combo with a foreground failed",
				bg, simple.getBackground());
	}
	simple.dispose();
}

@Test
public void test_setForegroundAlphaSimpleCombo() {
	Combo simple = new Combo(shell, SWT.SIMPLE);
	assumeTrue("Alpha support for foreground colors does not exist on Win32",
			SwtTestUtil.isCocoa || SwtTestUtil.isGTK);
	Color color = new Color (255, 0, 0, 0);
	simple.setForeground(color);
	assertEquals(color, simple.getForeground());
	Color bg = new Color(0, 255, 0, 0);
	simple.setBackground(bg);
	assertEquals(color, simple.getForeground());
	simple.dispose();
}

@Test
public void test_setItemILjava_lang_String() {
	assertThrows("No exception thrown for item == null", IllegalArgumentException.class,
		() -> combo.setItem(0, null));

	assertThrows(IllegalArgumentException.class, () -> combo.setItem(3, null));

	assertThrows(IllegalArgumentException.class, () -> combo.setItem(0, "fred"));

	combo.add("string0");
	assertThrows("No exception thrown for item == null", IllegalArgumentException.class, () ->
		combo.setItem(0, null));

	assertThrows("No exception thrown for index < 0", IllegalArgumentException.class, () ->
		combo.setItem(-1, "new value"));

	combo.add("joe");
	combo.setItem(0, "fred");
	assertEquals("fred", "fred", combo.getItem(0));

	assertThrows("No exception thrown for index < 0", IllegalArgumentException.class, () ->
		combo.setItem(4, "fred"));

	combo.removeAll();
	int number = 5;
	for (int i = 0; i < number; i++)
		combo.add("fred");
	for (int i = 0; i < number; i++)
		combo.setItem(i, "fred" + i);
	assertArrayEquals(":a:", new String[]{"fred0", "fred1", "fred2", "fred3", "fred4"}, combo.getItems());
}

@Test
public void test_setItems$Ljava_lang_String() {
	assertThrows("No exception thrown for items == null", IllegalArgumentException.class, () ->
		combo.setItems((String [])null));

	String[][] items = {{}, {""}, {"", ""}, {"fred"}, {"fred0", "fred0"}, {"fred", "fred"}};

	for (int i = 0 ; i< items.length; i++){
		combo.setItems(items[i]);
		assertArrayEquals(":a:" + i, items[i], combo.getItems());}
}

@Test
public void test_setOrientationI() {
	combo.setOrientation(SWT.RIGHT_TO_LEFT);
	if ((combo.getStyle() & SWT.MIRRORED) != 0) {
		assertEquals(":a:", SWT.RIGHT_TO_LEFT, combo.getOrientation());
	}
	combo.setOrientation(SWT.LEFT_TO_RIGHT);
	assertEquals(":b:", SWT.LEFT_TO_RIGHT, combo.getOrientation());
}

@Test
public void test_setSelectionLorg_eclipse_swt_graphics_Point() {
	assertThrows("No exception thrown for point == null", IllegalArgumentException.class, () ->
		combo.setSelection(null));

	int number = 5;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	combo.setSelection(new Point(0, 5));
	assertEquals(":a:", new Point(0, 0), combo.getSelection());
	combo.setText("some text");
	combo.setSelection(new Point(0, 5));
	assertEquals(":b:", new Point(0, 5), combo.getSelection());
}

@Test
public void test_setTextLimitI() {
	assertThrows("No exception thrown for limit == 0", IllegalArgumentException.class, () ->
		combo.setTextLimit(0));

	combo.setTextLimit(3);
	assertEquals(":a:", 3, combo.getTextLimit());
}

@Test
public void test_setTextLjava_lang_String() {
	assertThrows("No exception thrown for text == null", IllegalArgumentException.class, () ->
		combo.setText(null));

	String[] cases = {"", "fred", "fred0"};
	for (int i = 0; i < cases.length; i++) {
		combo.setText(cases[i]);
		assertEquals(":a:" + i, cases[i], combo.getText());
	}
	for (int i = 0; i < 5; i++) {
		combo.add("fred");
	}
	for (int i = 0; i < cases.length; i++) {
		combo.setText(cases[i]);
		assertEquals(":b:" + i,cases[i], combo.getText());
	}
	for (int i = 0; i < 5; i++) {
		combo.add("fred" + i);
	}
	for (int i = 0; i < cases.length; i++) {
		combo.setText(cases[i]);
		assertEquals(":c:" + i, cases[i], combo.getText());
	}
}

/* custom */
@Override
@Test
public void test_setBoundsIIII() {
	combo.setBounds(10, 20, 30, 40);
	// only check x, y, and width - you can't set the height of a combo
	assertEquals(10, combo.getBounds().x);
	assertEquals(20, combo.getBounds().y);
	assertEquals(30, combo.getBounds().width);
}

@Override
@Test
public void test_setBoundsLorg_eclipse_swt_graphics_Rectangle() {
	combo.setBounds(new Rectangle(10, 20, 30, 40));
	// only check x, y, and width - you can't set the height of a combo
	assertEquals(10, combo.getBounds().x);
	assertEquals(20, combo.getBounds().y);
	assertEquals(30, combo.getBounds().width);
}

@Override
@Test
public void test_setSizeII() {
	combo.setSize(30, 40);
	// only check the width - you can't set the height of a combo
	assertEquals(30, combo.getSize().x);

	combo.setSize(32, 43);
	// only check the width - you can't set the height of a combo
	assertEquals(32, combo.getSize().x);
}

@Override
@Test
public void test_setSizeLorg_eclipse_swt_graphics_Point() {
	combo.setSize(new Point(30, 40));
	// only check the width - you can't set the height of a combo
	assertEquals(30, combo.getSize().x);

	combo.setBounds(32, 43, 33, 44);
	// only check the width - you can't set the height of a combo
	assertEquals(33, combo.getSize().x);

	combo.setBounds(32, 43, 30, 40);
	combo.setLocation(11, 22);
	combo.setSize(new Point(32, 43));
	// only check the width - you can't set the height of a combo
	assertEquals(32, combo.getSize().x);
}

private void add() {
	combo.add("this");
	combo.add("is");
	combo.add("SWT");
}

@Test
public void test_consistency_MouseSelection () {
	add();
	consistencyPrePackShell();
	consistencyEvent(combo.getSize().x-10, 5, 30, combo.getItemHeight()*2,
					 ConsistencyUtility.SELECTION);
}

@Test
public void test_consistency_KeySelection () {
	add();
	consistencyEvent(0, SWT.ARROW_DOWN, 0, 0, ConsistencyUtility.KEY_PRESS);
}

@Test
public void test_consistency_EnterSelection () {
	add();
	consistencyEvent(10, 13, 0, 0, ConsistencyUtility.KEY_PRESS);
}

@Test
public void test_consistency_MenuDetect () {
	add();
	consistencyPrePackShell();
	//on arrow
	consistencyEvent(combo.getSize().x-10, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
	//on text
	consistencyEvent(10, 5, 3, ConsistencyUtility.ESCAPE_MENU, ConsistencyUtility.MOUSE_CLICK);
}

@Test
public void test_consistency_DragDetect () {
	add();
	consistencyEvent(10, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}

@Test
public void test_consistency_Segments () {
	if (!SwtTestUtil.isWindows) {
		// TODO Fix GTK and Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_consistency_Segments(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo).");
		}
		return;
	}
	final SegmentListener sl1 = event -> {
		if ((event.lineText.length() & 1) == 1) {
			event.segments = new int [] {1, event.lineText.length()};
			event.segmentsChars = null;
		} else {
			event.segments = new int [] {0, 0, event.lineText.length()};
			event.segmentsChars = new char [] {':', '<', '>'};
		}
		listenerCalled = true;
	};
	try {
		combo.addSegmentListener(null);
		fail("No exception thrown for addSegmentListener(null)");
	}
	catch (IllegalArgumentException e) {
	}
	combo.addSegmentListener(sl1);
	doSegmentsTest(true);

	combo.addSegmentListener(sl1);
	doSegmentsTest(true);

	combo.removeSegmentListener(sl1);
	doSegmentsTest(true);

	combo.removeSegmentListener(sl1);
	combo.setText(combo.getText());
	doSegmentsTest(false);
}

private void doSegmentsTest (boolean isListening) {
	String[] items = { "first", "second", "third" };
	String string = "1234";

	// Test setItems
	combo.setItems(items);
	assertEquals(isListening, listenerCalled);
	listenerCalled = false;
	assertArrayEquals(items, combo.getItems());

	// Test setText
	combo.setText(string);
	assertEquals(isListening, listenerCalled);
	listenerCalled = false;
	assertEquals(string, combo.getText());

	// Test limit, getItem, indexOf, select
	int limit = string.length() - 1;
	combo.setTextLimit(limit);
	assertEquals(limit, combo.getTextLimit());
	combo.setText(string);
	assertEquals(string.substring(0, limit), combo.getText());

	combo.setTextLimit(Combo.LIMIT);
	combo.setText(string);
	assertEquals(string, combo.getText());

	int count = items.length;
	for (int i = 0; i < count; i++) {
		assertEquals(items[i], combo.getItem(i));
		assertEquals(i, combo.indexOf(items[i]));

		combo.select(i);
		listenerCalled = false;
		assertEquals(i, combo.getSelectionIndex());
		assertEquals(items[i], combo.getText());
		assertFalse(listenerCalled);

		String currentText = combo.getText();
		combo.deselect(i ^ 1);
		assertEquals(currentText, combo.getText());

		combo.deselect(i);
		assertEquals("", combo.getText());
	}
	for (int i = 0; i < count; i++) {
		combo.setText(combo.getItem(i));
		assertEquals(items[i], combo.getText());
	}
	listenerCalled = false;

	limit = 2;
	combo.setTextLimit(limit);
	assertEquals(limit, combo.getTextLimit());
	combo.setText(string);
	assertEquals(string.substring(0, limit), combo.getText());

	combo.select(1);
	assertEquals(limit, combo.getTextLimit());

	combo.remove(1);
	assertEquals(limit, combo.getTextLimit());
	assertEquals(--count, combo.getItemCount());

	combo.add(items[1], 1);
	assertEquals(limit, combo.getTextLimit());
	assertEquals( ++count, combo.getItemCount());

	combo.deselectAll();
	assertEquals(limit, combo.getTextLimit());

	combo.remove(1, 2);
	count -=2;
	assertEquals(limit, combo.getTextLimit());
	assertEquals(count, combo.getItemCount());

	combo.removeAll();
	assertEquals(limit, combo.getTextLimit());
	assertEquals(0, combo.getItemCount());

	combo.setItems(items);
	count = items.length;
	assertEquals(limit, combo.getTextLimit());

	combo.setTextLimit(Combo.LIMIT);
	combo.setText(string);
	assertEquals(string, combo.getText());

	// Test add item
	String item = "forth";
	combo.add(item);
	assertEquals(isListening, listenerCalled);
	listenerCalled = false;
	assertEquals(item, combo.getItem(count++));
	assertEquals(count, combo.getItemCount());

	combo.select(1);

	// Test remove item by name
	combo.remove(items[1]);
	assertEquals(--count, combo.getItemCount());
	assertEquals(1, combo.indexOf(items[2]));

	// Test set item item by name
	combo.setItem(1, "second");
	assertEquals(count, combo.getItemCount());
	assertEquals(1, combo.indexOf(items[1]));

	combo.setText(string);
	listenerCalled = false;

	// Test selection, copy and paste
	Point pt = new Point(1, 3);
	combo.setSelection(pt);
	assertEquals(pt, combo.getSelection());
	assertFalse(listenerCalled);
	combo.copy();
	assertEquals(isListening, listenerCalled);
	listenerCalled = false;

	String substr = string.substring(pt.x, pt.y);
	pt.x = pt.y = 1;
	combo.setSelection(pt);
	combo.paste();
	assertEquals(isListening, listenerCalled);
	listenerCalled = false;

	assertEquals(string.substring(0, pt.x) + substr + string.substring(pt.y), combo.getText());
	pt.x = pt.y = pt.x + substr.length();
	assertEquals(pt, combo.getSelection());

	// Test cut
	pt.x -= 2;
	combo.setSelection(pt);
	assertEquals(substr, combo.getText().substring(pt.x, pt.y));
	combo.cut();
	assertEquals(isListening, listenerCalled);
	listenerCalled = false;
	assertEquals(string, combo.getText());
}
}

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CCombo
 *
 * @see org.eclipse.swt.custom.CCombo
 */
public class Test_org_eclipse_swt_custom_CCombo extends Test_org_eclipse_swt_widgets_Composite {

@Override
@Before
public void setUp() {
	super.setUp();
	ccombo = new CCombo(shell, 0);
	setWidget(ccombo);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
		assertThrows("No exception thrown for parent == null", IllegalArgumentException.class, () -> ccombo = new CCombo(null, 0));
		int cases[] = { SWT.FLAT, SWT.BORDER };
		for (int i = 0; i < cases.length; i++) {
			ccombo = new CCombo(shell, cases[i]);
			assertEquals(":a:" + String.valueOf(i), cases[i], (ccombo.getStyle() & cases[i]));
		}
		ccombo = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
		// Test all the combo listeners
		int comboListeners[] = { SWT.Dispose, SWT.FocusIn, SWT.Move, SWT.Resize };
		for (int comboListener : comboListeners) {
			assertTrue("Combo Listener events not implemented", ccombo.getListeners(comboListener).length > 0);
		}
		assertEquals("Pop up items are present.",0,  ccombo.getItems().length);
		// test that accessible features are added
		assertNotNull(ccombo.getAccessible());
}

@Test
public void test_copy() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_copy(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_CCombo).");
		}
		return;
	}
	ccombo.setText("123456");
	ccombo.setSelection(new Point(1,3));
	ccombo.copy();
	ccombo.setSelection(new Point(0,0));
	ccombo.paste();
	assertEquals(":a:", "23123456", ccombo.getText());
}

@Test
public void test_cut() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_cut(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_CCombo).");
		}
		return;
	}
	ccombo.setText("123456");
	ccombo.setSelection(new Point(1,3));
	ccombo.cut();
	assertEquals(":a:", "1456", ccombo.getText());
}

@Override
@Test
public void test_computeSizeIIZ() {
}

@Override
@Test
public void test_getChildren() {
}

@Test
@Override
public void test_isFocusControl() throws InterruptedException {
	if (SwtTestUtil.isGTK) {
		// TODO forceFocus returns false, while isFocusControl returns true
		assertFalse(control.isFocusControl());
	} else {
		super.test_isFocusControl();
	}
}

@Test
public void test_paste() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_paste(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_CCombo).");
		}
		return;
	}
	ccombo.setText("123456");
	ccombo.setSelection(new Point(1,3));
	ccombo.cut();
	assertEquals(":a:", "1456", ccombo.getText());
	ccombo.paste();
	assertEquals(":a:", "123456", ccombo.getText());
}

@Override
@Test
public void test_redraw() {
}

@Override
@Test
public void test_redrawIIIIZ() {
}

@Override
@Test
public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	Color colors[] = {new Color(0, 0, 0), new Color(255, 255, 255), new Color(0, 45, 255)};
	for(int i=0; i<3; i++) {
		ccombo.setBackground(colors[i]);
		assertEquals("i="+i, ccombo.getBackground(), colors[i]);
	}
}

@Override
@Test
public void test_setEnabledZ() {
	ccombo.setEnabled(true);
	assertTrue("Set true error", ccombo.getEnabled());
	ccombo.setEnabled(false);
	assertFalse("Set false error", ccombo.getEnabled());
}

@Override
@Test
public void test_setFocus() {
	assertFalse(ccombo.setFocus());
	shell.open();
	shell.setVisible(true);
	boolean exceptionThrown = false;
	try{
		ccombo.setEnabled(false);
		ccombo.setVisible(true);
		assertFalse("Expect false wehn not enabled", ccombo.setFocus());
		ccombo.setEnabled(true);
		ccombo.setVisible(false);
		assertFalse("Expect false wehn not visible", ccombo.setFocus());
		ccombo.setEnabled(false);
		ccombo.setVisible(false);
		assertFalse("Expect false wehn not visible and not enabled", ccombo.setFocus());
		ccombo.setEnabled(true);
		ccombo.setVisible(true);
		processEvents(0, null);
		if(ccombo.isFocusControl())
			assertTrue("Set focus error", ccombo.setFocus());

		if (!SwtTestUtil.isCocoa) {
			ccombo.setEnabled(true);
			ccombo.setVisible(true);
			ccombo.setFocus();
			processEvents(100, () -> ccombo.isFocusControl());
			assertTrue(ccombo.isFocusControl());
			Control focusControl = ccombo.getDisplay().getFocusControl();
			assertTrue(focusControl instanceof Text);
			assertEquals(ccombo, focusControl.getParent());
		}
	}
	catch (Exception e) {
		exceptionThrown = true;
	}
	assertFalse("Exception thrown", exceptionThrown);
}

@Override
@Test
public void test_setFocus_toChild_afterOpen() {
	// The different platforms set focus to a different child
}

@Override
@Test
public void test_setFocus_toChild_beforeOpen() {
	// The different platforms set focus to a different child
}

@Override
@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
}

@Override
@Test
public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	Color c1 = new Color(0, 0, 0);
	Color c2 = new Color(255, 255, 255);
	Color c3 = new Color(23, 45, 151);
	ccombo.setForeground(c1);
	ccombo.setForeground(c2);
	ccombo.setForeground(c3);
}

@Override
@Test
public void test_setToolTipTextLjava_lang_String() {
	ccombo.setToolTipText(null);
	assertNull(ccombo.getToolTipText());
	String[] cases = {"", "fang", "fang0"};
	for (int i = 0; i < cases.length; i++) {
		ccombo.setText(cases[i]);
		assertEquals(":a:" + i, cases[i], ccombo.getText());
	}
}

@Override
@Test
public void test_setVisibleZ() {
	ccombo.getParent().setVisible(true);
	ccombo.setVisible(true);
	if(!ccombo.isDisposed()) {
		assertTrue(ccombo.isVisible());
	}
	ccombo.setVisible(false);
	if(!ccombo.isDisposed()) {
		assertFalse(ccombo.isVisible());
	}
}

/* Custom */
CCombo ccombo;

private void add() {
	ccombo.add("this");
	ccombo.add("is");
	ccombo.add("SWT");
}

@Test
public void test_consistency_MouseSelection () {
	add();
	consistencyPrePackShell();
	consistencyEvent(ccombo.getSize().x-10, 5, 30, ccombo.getItemHeight()*2,
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
	consistencyEvent(ccombo.getSize().x-10, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
	//on text
	consistencyEvent(10, 5, 3, ConsistencyUtility.ESCAPE_MENU, ConsistencyUtility.MOUSE_CLICK);
}

@Test
public void test_consistency_DragDetect () {
	add();
	consistencyEvent(10, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}

@Test
public void test_addLjava_lang_String() {
	ccombo = new CCombo(shell, 0);
	assertThrows("Did not catch null argument",IllegalArgumentException.class, ()-> ccombo.add(null));
	add();
	assertTrue("Items not successfully added", ccombo.getItems().length > 0);

}

@Test
public void test_addLjava_lang_StringI() {
	ccombo = new CCombo(shell, 0);
	assertThrows("Did not catch null argument",IllegalArgumentException.class, ()-> ccombo.add(null, 0));

	add();// add three items
	assertTrue("Items not successfully added", ccombo.getItems().length > 0);

	assertThrows("Did not catch range exception",IllegalArgumentException.class, ()-> ccombo.add("Hello", 7));

	ccombo.add("Hello", 1);
	String test = ccombo.getItem(1);
	assertEquals("Hello", test);
}

@Test
public void test_addModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	ccombo = new CCombo(shell, 0);
	ModifyListener listener = event -> listenerCalled = true;
	assertThrows("Expected exception not thrown", IllegalArgumentException.class, () -> ccombo.addModifyListener(null));

	// test whether all content modifying API methods send a Modify event
	ccombo.addModifyListener(listener);
	listenerCalled = false;
	ccombo.setText("new text");
	assertTrue("setText does not send event", listenerCalled);

	listenerCalled = false;
	ccombo.removeModifyListener(listener);
	// cause to call the listener.
	ccombo.setText("line");
	assertFalse("Listener not removed", listenerCalled);
	assertThrows("Expected exception not thrown", IllegalArgumentException.class, () -> ccombo.removeModifyListener(null));
}

@Test
public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	ccombo = new CCombo(shell, 0);
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
	assertThrows("Expected exception not thrown", IllegalArgumentException.class, () -> ccombo.addSelectionListener(null));
	ccombo.addSelectionListener(listener);
	ccombo.select(0);
	assertFalse(":a:", listenerCalled);
	ccombo.removeSelectionListener(listener);
	assertThrows("Expected exception not thrown", IllegalArgumentException.class, () -> ccombo.removeSelectionListener(null));
}

@Test
public void test_clearSelection() {
	ccombo = new CCombo(shell, 0);
	int number = 5;
	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	ccombo.clearSelection();
	assertEquals(":a:", new Point(0, 0), ccombo.getSelection());
	ccombo.setSelection(new Point(0, 5));
	assertEquals(":b:", new Point(0, 0), ccombo.getSelection());  //nothing is selected
	ccombo.setText("some text");
	ccombo.setSelection(new Point(0, 5));
	assertEquals(":c:", new Point(0, 5), ccombo.getSelection());
	ccombo.clearSelection();
	assertEquals(":d:", ccombo.getSelection().x, ccombo.getSelection().y);
 }

@Test
public void test_deselectAll() {
	ccombo = new CCombo(shell, 0);
	ccombo.add("123");
	ccombo.add("456");
	ccombo.add("789");
	ccombo.select(0);
	ccombo.select(2);
	ccombo.deselectAll();
	assertEquals(":a:", -1,  ccombo.getSelectionIndex());
}

@Test
public void test_deselectI() {
	// indices out of range are ignored by the method
	ccombo = new CCombo(shell, 0);
	String[] items = {"item0", "item1", "item2"};
	ccombo.setItems(items);
	ccombo.select(1);
	ccombo.deselect(10);
	assertEquals(1, ccombo.getSelectionIndex());
	ccombo.removeAll();

	ccombo.deselect(2);

	int number = 10;
	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	for (int i = 0; i < number; i++) {
		ccombo.select(i);
		assertEquals(":a:" + i, i, ccombo.getSelectionIndex());
		ccombo.deselect(i);
		assertEquals(":b:" + i,-1, ccombo.getSelectionIndex());
	}
 }

@Test
public void test_getEditable() {
	ccombo = new CCombo(shell, 0);
	assertTrue("a: Receiver is not editable", ccombo.getEditable());
	ccombo = new CCombo(shell, SWT.BORDER);
	assertTrue("a: Receiver is not editable", ccombo.getEditable());
}

@Test
public void test_getItemCount() {
	int number = 10;
	for (int i = 0; i < number; i++) {
		assertEquals(":a:" + i, i, ccombo.getItemCount());
		ccombo.add("fred" + i);
	}
	assertEquals(":aa:", number, ccombo.getItemCount());

	for (int i = 0; i < number; i++) {
		assertEquals(":b:" + i,number-i, ccombo.getItemCount());
		ccombo.remove(0);
	}
	ccombo.removeAll();
	assertEquals(":c:",0,ccombo.getItemCount());
}

@Test
public void test_getItemHeight() {
	ccombo.getItemHeight();
}

@Test
public void test_getItemI() {
	assertThrows("No exception thrown for illegal index argument", IllegalArgumentException.class, () -> ccombo.getItem(0));

	int number = 10;
	for (int i = 0; i < number; i++) {
		ccombo.add("fred" + i);
	}
	for (int i = 0; i < number; i++)
		assertEquals("fred" + i, ccombo.getItem(i));
}

@Test
public void test_getItems() {
	ccombo.removeAll();
	ccombo.add("1");
	ccombo.add("2");
	ccombo.add("3");
	String[] items = ccombo.getItems();
	assertEquals(":a:",3, items.length);
	assertEquals(":a:", "1", items[0]);
	assertEquals(":a:", "2", items[1]);
	assertEquals(":a:", "3", items[2]);
}

@Test
public void test_getSelection() {
	ccombo.setText("123456");
	ccombo.setSelection(new Point(1,3));
	ccombo.getSelection();
	assertEquals(":a:", new Point(1,3), ccombo.getSelection());
}

@Test
public void test_getSelectionIndex() {
	int number = 5;
	for (int i = 0; i < number; i++) {
		ccombo.add("fred");
	}
	assertEquals(-1, ccombo.getSelectionIndex());
	for (int i = 0; i < number; i++) {
		ccombo.select(i);
		assertEquals(i, ccombo.getSelectionIndex());
	}

	ccombo.removeAll();
	for (int i = 01; i < number; i++) {
		ccombo.add("fred");
	}
	assertEquals(-1, ccombo.getSelectionIndex());
	for (int i = 0; i < number; i++) {
		ccombo.select(i);
		ccombo.deselect(i);
		assertEquals(-1, ccombo.getSelectionIndex());
	}
}

@Test
public void test_getStyle() {
	int style = 0;
	ccombo.setEditable(false);
	style = ccombo.getStyle();
	assertNotEquals(0, (style & SWT.READ_ONLY));
}

@Test
public void test_getText() {
	String[] cases = {"", "fred", "fredfred"};
	for (int i = 0; i < cases.length; i++) {
		ccombo.setText(cases[i]);
		assertEquals(":a:" + String.valueOf(i), cases[i], ccombo.getText());
	}
}

@Test
public void test_getTextHeight() {
	ccombo.getTextHeight();
}

@Test
public void test_getTextLimit() {
	ccombo.setTextLimit(3);
	assertEquals(":a:", 3, ccombo.getTextLimit());
}

@Test
public void test_indexOfLjava_lang_String() {
	ccombo.add("string1");
	assertThrows("No exception thrown for string == null",IllegalArgumentException.class, ()-> ccombo.indexOf(null));

	ccombo.removeAll();

	int number = 5;
	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	for (int i = 0; i < number; i++)
		assertEquals(i, ccombo.indexOf("fred" + i));

	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	ccombo.removeAll();
	for (int i = 0; i < number; i++)
		assertEquals(-1, ccombo.indexOf("fred" + i));

	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	ccombo.remove("fred3");
	for (int i = 0; i < 3; i++)
		assertEquals(i, ccombo.indexOf("fred" + i));
	assertEquals(-1, ccombo.indexOf("fred3"));
	for (int i = 4; i < number; i++)
		assertEquals(i - 1, ccombo.indexOf("fred" + i));

	ccombo.removeAll();
	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	ccombo.remove(2);
	for (int i = 0; i < 2; i++)
		assertEquals(i, ccombo.indexOf("fred" + i));
	assertEquals(-1, ccombo.indexOf("fred2"));
	for (int i = 3; i < number; i++)
		assertEquals(i - 1, ccombo.indexOf("fred" + i));
}

@Test
public void test_indexOfLjava_lang_StringI() {
	ccombo.add("string0");
	assertThrows("No exception thrown for string == null",IllegalArgumentException.class, ()-> ccombo.indexOf(null));
	assertEquals(0, ccombo.indexOf("string0", 0));
	ccombo.removeAll();

	int number = 5;
	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	for (int i = 0; i < number; i++)
		assertEquals(":a:" + i, i, ccombo.indexOf("fred" + i, 0));
	for (int i = 0; i < number; i++)
		assertEquals(":b:" + i,-1,  ccombo.indexOf("fred" + i, i + 1));

	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	for (int i = 0; i < 3; i++)
		assertEquals(":a:" + i, i, ccombo.indexOf("fred" + i, 0));
	for (int i = 3; i < number; i++)
		assertEquals(":b:" + i, i, ccombo.indexOf("fred" + i, 3));
	for (int i = 0; i < number; i++)
		assertEquals(":b:" + i, i, ccombo.indexOf("fred" + i, i));
}

@Test
public void test_removeAll() {
	ccombo = new CCombo(shell, 0);
	add();
	ccombo.removeAll();
	assertEquals(":a:",0, ccombo.getItems().length);
}


@Test
public void test_removeII() {
	ccombo = new CCombo(shell, 0);
	int number = 5;
	for (int i = 0; i < number; i++) {
		ccombo.add("fred");
	}
	ccombo.remove(0, 4);
	assertEquals(0, ccombo.getItemCount());

	ccombo.removeAll();
	for (int i = 0; i < number; i++) {
		ccombo.add("fred");
	}
	ccombo.remove(0, 2);
	assertEquals(2, ccombo.getItemCount());

	ccombo.removeAll();
	for (int i = 0; i < number; i++) {
		ccombo.add("fred");
	}
	ccombo.remove(2, 4);
	assertEquals(2, ccombo.getItemCount());

	ccombo.removeAll();
	for (int i = 0; i < number; i++) {
		ccombo.add("fred");
	}
	ccombo.remove(3, 2);
	assertEquals(number, ccombo.getItemCount());

	ccombo.removeAll();
	for (int i = 0; i < number; i++) {
		ccombo.add("fred");
	}

	assertThrows("No exception thrown for illegal index argument", IllegalArgumentException.class, () -> ccombo.remove(2, 100));

	assertThrows("No exception thrown for start index < 0", IllegalArgumentException.class, () ->ccombo.remove(-1, number-1));
}

@Test
public void test_removeLjava_lang_String() {
	ccombo = new CCombo(shell, 0);
	int number = 5;
	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	for (int i = 0; i < number; i++) {
		assertEquals(number - i, ccombo.getItemCount());
		ccombo.remove("fred" + i);
	}

	for (int i = 0; i < number; i++)
		ccombo.add("fred");
	for (int i = 0; i < number; i++) {
		assertEquals(number - i, ccombo.getItemCount());
		ccombo.remove("fred");
	}

	for (int i = 0; i < number; i++)
		ccombo.add("fred");

	assertThrows("No exception thrown for item == null", IllegalArgumentException.class, () -> ccombo.remove(null));

	ccombo.removeAll();
	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);

	assertThrows("No exception thrown for item not found", IllegalArgumentException.class, () -> ccombo.remove("fred"));

	assertEquals(number, ccombo.getItemCount());
}

@Test
public void test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	ModifyListener listener = event -> listenerCalled = true;
	assertThrows("Expected exception not thrown", IllegalArgumentException.class, () -> ccombo.addModifyListener(null));

	// test whether all content modifying API methods send a Modify event
	ccombo.addModifyListener(listener);
	listenerCalled = false;
	ccombo.setText("new text");
	assertTrue("setText does not send event", listenerCalled);

	listenerCalled = false;
	ccombo.removeModifyListener(listener);
	// cause to call the listener.
	ccombo.setText("line");
	assertFalse("Listener not removed", listenerCalled);
	assertThrows("Expected exception not thrown", IllegalArgumentException.class, () -> ccombo.removeModifyListener(null));
}

@Test
public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
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
	assertThrows("Expected exception not thrown", IllegalArgumentException.class, () -> ccombo.addSelectionListener(null));
	ccombo.addSelectionListener(listener);
	ccombo.select(0);
	assertFalse(":a:", listenerCalled);
	ccombo.removeSelectionListener(listener);
	assertThrows("Expected exception not thrown", IllegalArgumentException.class, () -> ccombo.removeSelectionListener(null));
}

@Test
public void test_selectI() {
	ccombo.add("123");
	ccombo.add("456");
	ccombo.add("789");
	ccombo.select(0);
	ccombo.select(1);
	assertEquals(":a:", 1, ccombo.getSelectionIndex());

	// indices out of range are ignored
	ccombo.select(10);
	assertEquals(1, ccombo.getSelectionIndex());
}

@Test
public void test_setEditableZ() {
	ccombo.setEditable(true);
	assertTrue("Set true error", ccombo.getEditable());
	ccombo.setEditable(false);
	assertFalse("Set false error", ccombo.getEditable());
}

@Test
public void test_setItemILjava_lang_String() {
	assertThrows("No exception thrown for item == null", IllegalArgumentException.class, () -> ccombo.setItem(0, null));

	assertThrows("No exception thrown for illegal index argument", IllegalArgumentException.class, () ->ccombo.setItem(3, "fang"));

	assertThrows("No exception thrown for illegal index argument", IllegalArgumentException.class, () ->ccombo.setItem(0, "fang"));

	ccombo.add("string0");
	assertThrows("No exception thrown for item == null", IllegalArgumentException.class, () -> ccombo.setItem(0, null));

	assertThrows("No exception thrown for index < 0", IllegalArgumentException.class, () ->ccombo.setItem(-1, "new value"));

	assertThrows("No exception thrown for illegal index argument", IllegalArgumentException.class, () -> ccombo.setItem(3, "fang"));

	ccombo.add("joe");
	ccombo.setItem(0, "fang");
	assertEquals("fang", "fang", ccombo.getItem(0));

	assertThrows("No exception thrown for illegal index argument", IllegalArgumentException.class, () ->ccombo.setItem(4, "fang"));

	ccombo.removeAll();
	int number = 5;
	for (int i = 0; i < number; i++)
		ccombo.add("fang");
	for (int i = 0; i < number; i++)
		ccombo.setItem(i, "fang" + i);
	assertArrayEquals(":a:", new String[]{"fang0", "fang1", "fang2", "fang3", "fang4"}, ccombo.getItems());
}

@Test
public void test_setItems$Ljava_lang_String() {
	assertThrows("No exception thrown for items == null", IllegalArgumentException.class, () -> ccombo.setItems(null));

	String nullItem[] = new String[1];
	nullItem[0] = null;

	assertThrows("No exception thrown for items[0] == null", IllegalArgumentException.class, () ->ccombo.setItems(nullItem));

	String[][] items = {{}, {""}, {"", ""}, {"fang"}, {"fang0", "fang0"}, {"fang", "fang"}};

	for (int i = 0 ; i< items.length; i++){
		ccombo.setItems(items[i]);
		assertArrayEquals(":a:" + i, items[i], ccombo.getItems());
	}
}

@Test
public void test_setSelectionLorg_eclipse_swt_graphics_Point() {
	assertThrows("No exception thrown for point == null", IllegalArgumentException.class, () -> ccombo.setSelection(null));

	int number = 5;
	for (int i = 0; i < number; i++)
		ccombo.add("fang" + i);
	ccombo.setSelection(new Point(0, 5));
	assertEquals(":a:", new Point(0, 0), ccombo.getSelection());
	ccombo.setText("some text");
	ccombo.setSelection(new Point(0, 5));
	assertEquals("Has not been implemented :b:", new Point(0, 5), ccombo.getSelection());
}

@Test
public void test_setTextLimitI() {
	assertThrows("No exception thrown for limit == 0", IllegalArgumentException.class, () -> ccombo.setTextLimit(0));

	ccombo.setTextLimit(3);
	assertEquals(":a:", 3, ccombo.getTextLimit());
}

@Test
public void test_setTextLjava_lang_String() {
	assertThrows("No exception thrown for text == null", IllegalArgumentException.class, () -> ccombo.setText(null));

	String[] cases = {"", "fang", "fang0"};
	for (int i = 0; i < cases.length; i++) {
		ccombo.setText(cases[i]);
		assertEquals(":a:" + i, cases[i], ccombo.getText());
	}
	for (int i = 0; i < 5; i++) {
		ccombo.add("fang");
	}
	for (int i = 0; i < cases.length; i++) {
		ccombo.setText(cases[i]);
		assertEquals(":b:" + i, cases[i], ccombo.getText());
	}
	for (int i = 0; i < 5; i++) {
		ccombo.add("fang" + i);
	}
	for (int i = 0; i < cases.length; i++) {
		ccombo.setText(cases[i]);
		assertEquals(":c:" + i, cases[i], ccombo.getText());
	}
}

@Test
public void test_setAlignment() {
	assertEquals(":a:", SWT.LEAD, ccombo.getAlignment());

	ccombo.setText("Trail");
	ccombo.setAlignment(SWT.TRAIL);
	assertEquals(":b:", SWT.TRAIL, ccombo.getAlignment());
	assertEquals(":b:", "Trail", ccombo.getText());

	ccombo.add("Center");
	ccombo.select(ccombo.getItemCount() - 1);
	ccombo.setAlignment(SWT.CENTER);
	assertEquals(":c:", SWT.CENTER, ccombo.getAlignment());
	assertEquals(":c:", "Center", ccombo.getText());

	ccombo.setAlignment(SWT.LEFT);
	assertEquals(":d:", SWT.LEFT, ccombo.getAlignment());
	assertEquals(":d:", "Center", ccombo.getText());
}
}

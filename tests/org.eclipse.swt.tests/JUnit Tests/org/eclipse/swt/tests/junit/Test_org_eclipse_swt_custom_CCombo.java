/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
		try {
			ccombo = new CCombo(null, 0);
			fail("No exception thrown for parent == null");
		} catch (IllegalArgumentException e) {
		}
		int cases[] = { SWT.FLAT, SWT.BORDER };
		for (int i = 0; i < cases.length; i++) {
			ccombo = new CCombo(shell, cases[i]);
			assertTrue(":a:" + String.valueOf(i), (ccombo.getStyle() & cases[i]) == cases[i]);
		}
		ccombo = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
		// Test all the combo listeners
		int comboListeners[] = { SWT.Dispose, SWT.FocusIn, SWT.Move, SWT.Resize };
		for (int comboListener : comboListeners) {
			assertTrue("Combo Listener events not implemented", ccombo.getListeners(comboListener).length > 0);
		}
		assertTrue("Pop up items are present.", ccombo.getItems().length == 0);
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
	assertTrue(":a:", ccombo.getText().equals("23123456"));
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
	assertTrue(":a:", ccombo.getText().equals("1456"));
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
	assertTrue(":a:", ccombo.getText().equals("1456"));
	ccombo.paste();
	assertTrue(":a:", ccombo.getText().equals("123456"));
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
	boolean exceptionThrown = false;
	Color colors[] = {new Color(0, 0, 0), new Color(255, 255, 255), new Color(0, 45, 255)};
	try {
		for(int i=0; i<3; i++) {
			ccombo.setBackground(colors[i]);
			assertEquals("i="+i, ccombo.getBackground(), colors[i]);
		}
	}
	catch (Exception e) {
		exceptionThrown = true;
	}
	assertFalse("Expected exception thrown", exceptionThrown);
	exceptionThrown = false;
}

@Override
@Test
public void test_setEnabledZ() {
	boolean exceptionThrown = false;
	try{
		ccombo.setEnabled(true);
		assertTrue("Set true error", ccombo.getEnabled());
		ccombo.setEnabled(false);
		assertFalse("Set false error", ccombo.getEnabled());
	}
	catch (Exception e) {
		exceptionThrown = true;
	}
	assertFalse("Exception thrown", exceptionThrown);
}

@Override
@Test
public void test_setFocus() {
	assertTrue(!ccombo.setFocus());
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
			processEvents(0, null);
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
	boolean exceptionThrown = false;
	Color c1 = new Color(0, 0, 0);
	Color c2 = new Color(255, 255, 255);
	Color c3 = new Color(23, 45, 151);
	try {
		ccombo.setForeground(c1);
		ccombo.setForeground(c2);
		ccombo.setForeground(c3);
	}
	catch (Exception e) {
		exceptionThrown = true;
	}
	assertFalse("Expected exception thrown", exceptionThrown);
}

@Override
@Test
public void test_setToolTipTextLjava_lang_String() {
	boolean exceptionThrown = false;
	try {
		ccombo.setToolTipText(null);
		assertNull(ccombo.getToolTipText());
		String[] cases = {"", "fang", "fang0"};
		for (int i = 0; i < cases.length; i++) {
			ccombo.setText(cases[i]);
			assertTrue(":a:" + i, ccombo.getText().equals(cases[i]));
		}
	}
	catch (Exception e) {
		exceptionThrown = true;
	}
	assertFalse("Unexpected exception thrown", exceptionThrown);
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
	try {
		ccombo.add(null);
		fail("Did not catch null argument");
	} catch (IllegalArgumentException e) {
	}
	add();
	assertTrue("Items not successfully added", ccombo.getItems().length > 0);

}

@Test
public void test_addLjava_lang_StringI() {
	ccombo = new CCombo(shell, 0);
	try {
		ccombo.add(null, 0);
		fail("Did not catch null argument");
	} catch (IllegalArgumentException e) {
	}

	add();// add three items
	assertTrue("Items not successfully added", ccombo.getItems().length > 0);
	try {
		ccombo.add("Hello", 7);
		fail("Did not catch invalid range exception");
	} catch (IllegalArgumentException e) {
	}

	ccombo.add("Hello", 1);
	String test = ccombo.getItem(1);
	assertEquals("Hello", test);
}

@Test
public void test_addModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	ccombo = new CCombo(shell, 0);
	boolean exceptionThrown = false;
	ModifyListener listener = event -> listenerCalled = true;
	try {
		ccombo.addModifyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
	exceptionThrown = false;

	// test whether all content modifying API methods send a Modify event
	ccombo.addModifyListener(listener);
	listenerCalled = false;
	ccombo.setText("new text");
	assertTrue("setText does not send event", listenerCalled);

	listenerCalled = false;
	ccombo.removeModifyListener(listener);
	// cause to call the listener.
	ccombo.setText("line");
	assertTrue("Listener not removed", listenerCalled == false);
	try {
		ccombo.removeModifyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
}

@Test
public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	ccombo = new CCombo(shell, 0);
	listenerCalled = false;
	boolean exceptionThrown = false;
	SelectionListener listener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			listenerCalled = true;
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent event) {
		}
	};
	try {
		ccombo.addSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
	exceptionThrown = false;
	ccombo.addSelectionListener(listener);
	ccombo.select(0);
	assertTrue(":a:", listenerCalled == false);
	ccombo.removeSelectionListener(listener);
	try {
		ccombo.removeSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
}

@Test
public void test_clearSelection() {
	ccombo = new CCombo(shell, 0);
	int number = 5;
	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	ccombo.clearSelection();
	assertTrue(":a:", ccombo.getSelection().equals(new Point(0, 0)));
	ccombo.setSelection(new Point(0, 5));
	assertTrue(":b:", ccombo.getSelection().equals(new Point(0, 0)));  //nothing is selected
	ccombo.setText("some text");
	ccombo.setSelection(new Point(0, 5));
	assertTrue(":c:", ccombo.getSelection().equals(new Point(0, 5)));
	ccombo.clearSelection();
	assertTrue(":d:", ccombo.getSelection().x==ccombo.getSelection().y);
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
	assertTrue(":a:", ccombo.getSelectionIndex()== -1);
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
		assertTrue(":a:" + i, ccombo.getSelectionIndex()==i);
		ccombo.deselect(i);
		assertTrue(":b:" + i, ccombo.getSelectionIndex()==-1);
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
		assertTrue(":a:" + i, ccombo.getItemCount() == i);
		ccombo.add("fred" + i);
	}
	assertTrue(":aa:", ccombo.getItemCount() == number);

	for (int i = 0; i < number; i++) {
		assertTrue(":b:" + i, ccombo.getItemCount() == number-i);
		ccombo.remove(0);
	}
	ccombo.removeAll();
	assertTrue(":c:", ccombo.getItemCount() == 0);
}

@Test
public void test_getItemHeight() {
	ccombo.getItemHeight();
}

@Test
public void test_getItemI() {
	try {
		ccombo.getItem(0);
		fail("No exception thrown for illegal index argument");
	} catch (IllegalArgumentException e) {
	}

	int number = 10;
	for (int i = 0; i < number; i++) {
		ccombo.add("fred" + i);
	}
	for (int i = 0; i < number; i++)
		assertTrue(ccombo.getItem(i).equals("fred" + i));
}

@Test
public void test_getItems() {
	ccombo.removeAll();
	ccombo.add("1");
	ccombo.add("2");
	ccombo.add("3");
	String[] items = ccombo.getItems();
	assertTrue(":a:", items.length==3);
	assertTrue(":a:", items[0].equals("1"));
	assertTrue(":a:", items[1].equals("2"));
	assertTrue(":a:", items[2].equals("3"));
}

@Test
public void test_getSelection() {
	ccombo.setText("123456");
	ccombo.setSelection(new Point(1,3));
	ccombo.getSelection();
	assertTrue(":a:", ccombo.getSelection().equals(new Point(1,3)));
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
	try {
		style = ccombo.getStyle();
	}
	catch(Exception e) {
		fail("Unexpected exception thrown for getStyle");
	}
	assertTrue((style & SWT.READ_ONLY) != 0);
}

@Test
public void test_getText() {
	String[] cases = {"", "fred", "fredfred"};
	for (int i = 0; i < cases.length; i++) {
		ccombo.setText(cases[i]);
		assertTrue(":a:" + String.valueOf(i), cases[i].equals(ccombo.getText()));
	}
}

@Test
public void test_getTextHeight() {
		ccombo.getTextHeight();
}

@Test
public void test_getTextLimit() {
	ccombo.setTextLimit(3);
	assertTrue(":a:", ccombo.getTextLimit()==3);
}

@Test
public void test_indexOfLjava_lang_String() {
	ccombo.add("string1");
	try {
		ccombo.indexOf(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
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
	try {
		ccombo.indexOf(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
	assertEquals(0, ccombo.indexOf("string0", 0));
	ccombo.removeAll();

	int number = 5;
	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	for (int i = 0; i < number; i++)
		assertTrue(":a:" + i, ccombo.indexOf("fred" + i, 0) == i);
	for (int i = 0; i < number; i++)
		assertTrue(":b:" + i, ccombo.indexOf("fred" + i, i + 1) == -1);

	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	for (int i = 0; i < 3; i++)
		assertTrue(":a:" + i, ccombo.indexOf("fred" + i, 0) == i);
	for (int i = 3; i < number; i++)
		assertTrue(":b:" + i, ccombo.indexOf("fred" + i, 3) == i);
	for (int i = 0; i < number; i++)
		assertTrue(":b:" + i, ccombo.indexOf("fred" + i, i) == i);
}

@Test
public void test_removeAll() {
	ccombo = new CCombo(shell, 0);
	add();
	ccombo.removeAll();
	assertTrue(":a:", ccombo.getItems().length==0);
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

	try {
		ccombo.remove(2, 100);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		ccombo.remove(-1, number-1);
		fail("No exception thrown for start index < 0");
	}
	catch (IllegalArgumentException e) {
	}
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
	try {
		ccombo.remove(null);
		fail("No exception thrown for item == null");
	}
	catch (IllegalArgumentException e) {
	}

	ccombo.removeAll();
	for (int i = 0; i < number; i++)
		ccombo.add("fred" + i);
	try {
		ccombo.remove("fred");
		fail("No exception thrown for item not found");
	}
	catch (IllegalArgumentException e) {
	}

	assertEquals(number, ccombo.getItemCount());
}

@Test
public void test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	boolean exceptionThrown = false;
	ModifyListener listener = event -> listenerCalled = true;
	try {
		ccombo.addModifyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
	exceptionThrown = false;

	// test whether all content modifying API methods send a Modify event
	ccombo.addModifyListener(listener);
	listenerCalled = false;
	ccombo.setText("new text");
	assertTrue("setText does not send event", listenerCalled);

	listenerCalled = false;
	ccombo.removeModifyListener(listener);
	// cause to call the listener.
	ccombo.setText("line");
	assertTrue("Listener not removed", listenerCalled == false);
	try {
		ccombo.removeModifyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
}

@Test
public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	boolean exceptionThrown = false;
	SelectionListener listener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			listenerCalled = true;
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent event) {
		}
	};
	try {
		ccombo.addSelectionListener(null);
	} catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
	exceptionThrown = false;
	ccombo.addSelectionListener(listener);
	ccombo.select(0);
	assertTrue(":a:", listenerCalled == false);
	ccombo.removeSelectionListener(listener);
	try {
		ccombo.removeSelectionListener(null);
	} catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
}

@Test
public void test_selectI() {
	ccombo.add("123");
	ccombo.add("456");
	ccombo.add("789");
	ccombo.select(0);
	ccombo.select(1);
	assertTrue(":a:", ccombo.getSelectionIndex()== 1);

	// indices out of range are ignored
	ccombo.select(10);
	assertEquals(1, ccombo.getSelectionIndex());
}

@Test
public void test_setEditableZ() {
	boolean exceptionThrown = false;
	try{
		ccombo.setEditable(true);
		assertTrue("Set true error", ccombo.getEditable());
		ccombo.setEditable(false);
		assertFalse("Set false error", ccombo.getEditable());
	}
	catch (Exception e) {
		exceptionThrown = true;
	}
	assertFalse("Exception thrown", exceptionThrown);
}

@Test
public void test_setItemILjava_lang_String() {
	try {
		ccombo.setItem(0, null);
		fail("No exception thrown for item == null");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		ccombo.setItem(3, "fang");
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		ccombo.setItem(0, "fang");
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	ccombo.add("string0");
	try {
		ccombo.setItem(0, null);
		fail("No exception thrown for item == null");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		ccombo.setItem(-1, "new value");
		fail("No exception thrown for index < 0");
	}
	catch (IllegalArgumentException e) {
	}
	try {
		ccombo.setItem(3, "fang");
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	ccombo.add("joe");
	ccombo.setItem(0, "fang");
	assertTrue("fang", ccombo.getItem(0).equals("fang"));

	try {
		ccombo.setItem(4, "fang");
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

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
	try {
		ccombo.setItems(null);
		fail("No exception thrown for items == null");
	}
	catch (IllegalArgumentException e) {
	}
	String nullItem[] = new String[1];
	nullItem[0] = null;
	try {
		ccombo.setItems(nullItem);
		fail("No exception thrown for items[0] == null");
	}
	catch (IllegalArgumentException e) {
	}

	String[][] items = {{}, {""}, {"", ""}, {"fang"}, {"fang0", "fang0"}, {"fang", "fang"}};

	for (int i = 0 ; i< items.length; i++){
		ccombo.setItems(items[i]);
		assertArrayEquals(":a:" + i, items[i], ccombo.getItems());
	}
}

@Test
public void test_setSelectionLorg_eclipse_swt_graphics_Point() {
	try {
		ccombo.setSelection(null);
		fail("No exception thrown for point == null");
	}
	catch (IllegalArgumentException e) {
	}

	int number = 5;
	for (int i = 0; i < number; i++)
		ccombo.add("fang" + i);
	ccombo.setSelection(new Point(0, 5));
	assertTrue(":a:", ccombo.getSelection().equals(new Point(0, 0)));
	ccombo.setText("some text");
	ccombo.setSelection(new Point(0, 5));
	assertTrue("Has not been implemented :b:", ccombo.getSelection().equals(new Point(0, 5)));
}

@Test
public void test_setTextLimitI() {
	try {
		ccombo.setTextLimit(0);
		fail("No exception thrown for limit == 0");
	}
	catch (IllegalArgumentException e) {
	}

	ccombo.setTextLimit(3);
	assertTrue(":a:", ccombo.getTextLimit()==3);
}

@Test
public void test_setTextLjava_lang_String() {
	try {
		ccombo.setText(null);
		fail("No exception thrown for text == null");
	}
	catch (IllegalArgumentException e) {
	}

	String[] cases = {"", "fang", "fang0"};
	for (int i = 0; i < cases.length; i++) {
		ccombo.setText(cases[i]);
		assertTrue(":a:" + i, ccombo.getText().equals(cases[i]));
	}
	for (int i = 0; i < 5; i++) {
		ccombo.add("fang");
	}
	for (int i = 0; i < cases.length; i++) {
		ccombo.setText(cases[i]);
		assertTrue(":b:" + i, ccombo.getText().equals(cases[i]));
	}
	for (int i = 0; i < 5; i++) {
		ccombo.add("fang" + i);
	}
	for (int i = 0; i < cases.length; i++) {
		ccombo.setText(cases[i]);
		assertTrue(":c:" + i, ccombo.getText().equals(cases[i]));
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

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Combo
 *
 * @see org.eclipse.swt.widgets.Combo
 */
public class Test_org_eclipse_swt_widgets_Combo extends Test_org_eclipse_swt_widgets_Composite {

	Combo combo;

public Test_org_eclipse_swt_widgets_Combo(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	combo = new Combo(shell, 0);
	setWidget(combo);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		combo = new Combo(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}

	int[] cases = {SWT.DROP_DOWN, SWT.SIMPLE};
	for (int i = 0; i < cases.length; i++) {
		combo = new Combo(shell, cases[i]);
		assertTrue(":a:" + String.valueOf(i), (combo.getStyle() & cases[i]) == cases[i]);
	}
}

public void test_addLjava_lang_String() {
	try {
		combo.add(null);
		fail("No exception thrown for item == null");
	}
	catch (IllegalArgumentException e) {
	}

	combo.add("");
	assertEquals(":a:", new String[]{""}, combo.getItems());
	combo.add("");
	assertEquals(":b:", new String[]{"", ""}, combo.getItems());
	combo.add("fred");
	assertEquals(":c:", new String[]{"", "", "fred"}, combo.getItems());

}

public void test_addLjava_lang_StringI() {
	try {
		combo.add(null, 0);
		fail("No exception thrown for item == null");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		combo.add("string", -1);
		fail("No exception thrown for index < 0");
	}
	catch (IllegalArgumentException e) {
	}

	combo.add("string0", 0);
	try {
		combo.add("string1", 2);
		fail("No exception thrown for index > size");
	}
	catch (IllegalArgumentException e) {
	}
	combo.removeAll();
	
	combo.add("fred", 0);
	assertEquals("fred", new String[]{"fred"}, combo.getItems());
	combo.add("fred", 0);
	assertEquals("fred fred", new String[]{"fred", "fred"}, combo.getItems());
	combo.add("fred");
	assertEquals("fred fred fred", new String[]{"fred", "fred", "fred"}, combo.getItems());
	combo.removeAll();
	
	int number = 3;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	combo.add("fred", number);
	assertEquals("fred0 fred1 fred2 fred", new String[]{"fred0", "fred1", "fred2", "fred"}, combo.getItems());

	combo.removeAll();
	number = 3;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	combo.add("fred", 1);
	assertEquals("fred0 fred fred1 fred2", new String[]{"fred0", "fred", "fred1", "fred2"}, combo.getItems());
	combo.add("fred", 0);
	assertEquals("fred fred0 fred fred1 fred2", new String[]{"fred", "fred0", "fred", "fred1", "fred2"}, combo.getItems());
	combo.add("fred", 4);
	assertEquals("fred fred0 fred fred1 fred fred2", new String[]{"fred", "fred0", "fred", "fred1", "fred", "fred2"}, combo.getItems());
}

public void test_addModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	boolean exceptionThrown = false;
	ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent event) {
			listenerCalled = true;
		}
	};
	try {
		combo.addModifyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
	exceptionThrown = false;
	
	// test whether all content modifying API methods send a Modify event	
	combo.addModifyListener(listener);
	listenerCalled = false;
	combo.setText("new text");	
	assertTrue("setText does not send event", listenerCalled);

	listenerCalled = false;	
	combo.removeModifyListener(listener);
	// cause to call the listener. 
	combo.setText("line");	
	assertTrue("Listener not removed", listenerCalled == false);
	try {
		combo.removeModifyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	boolean exceptionThrown = false;
	SelectionListener listener = new SelectionListener() {
		public void widgetSelected(SelectionEvent event) {
			listenerCalled = true;
		}
		public void widgetDefaultSelected(SelectionEvent event) {
		}
	};
	try {
		combo.addSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
	exceptionThrown = false;
	combo.addSelectionListener(listener);
	combo.select(0);
	assertTrue(":a:", listenerCalled == false);
	combo.removeSelectionListener(listener);
	try {
		combo.removeSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
}

public void test_clearSelection() {
	int number = 5;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	combo.clearSelection();
	assertTrue(":a:", combo.getSelection().equals(new Point(0, 0)));
	combo.setSelection(new Point(0, 5));
	assertTrue(":b:", combo.getSelection().equals(new Point(0, 0)));  //nothing is selected
	combo.setText("some text");
	combo.setSelection(new Point(0, 5));
	assertTrue(":c:", combo.getSelection().equals(new Point(0, 5)));
	combo.clearSelection();
	assertTrue(":d:", combo.getSelection().x==combo.getSelection().y);
}

public void test_computeSizeIIZ() {
	// super class test is sufficient
}

public void test_copy() {
	combo.setText("123456");
	combo.setSelection(new Point(1,3));
	combo.copy();
	combo.setSelection(new Point(0,0));
	combo.paste();
	assertTrue(":a:", combo.getText().equals("23123456"));
}

public void test_cut() {
	combo.setText("123456");
	combo.setSelection(new Point(1,3));
	combo.cut();
	assertTrue(":a:", combo.getText().equals("1456"));
}

public void test_deselectAll() {
	combo.add("123");
	combo.add("456");
	combo.add("789");
	combo.select(0);
	combo.select(2);
	combo.deselectAll();
	assertTrue(":a:", combo.getSelectionIndex()== -1);
}
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
		assertTrue(":a:" + i, combo.getSelectionIndex()==i);
		combo.deselect(i);
		assertTrue(":b:" + i, combo.getSelectionIndex()==-1);
	}
}

public void test_getChildren() {
	// Combo cannot have children
}

public void test_getItemCount() {
	int number = 10;
	for (int i = 0; i < number; i++) {
		assertTrue(":a:" + i, combo.getItemCount() == i);
		combo.add("fred" + i);
	}
	assertTrue(":aa:", combo.getItemCount() == number);

	for (int i = 0; i < number; i++) {
		assertTrue(":b:" + i, combo.getItemCount() == number-i);
		combo.remove(0);
	}
	combo.removeAll();
	assertTrue(":c:", combo.getItemCount() == 0);
}

public void test_getItemHeight() {
	combo.getItemHeight();
}

public void test_getItemI() {
	try {
		combo.getItem(0);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	int number = 10;
	for (int i = 0; i < number; i++) {
		combo.add("fred" + i);
	}
	for (int i = 0; i < number; i++)
		assertTrue(combo.getItem(i).equals("fred" + i));
}

public void test_getItems() {
	combo.removeAll();
	combo.add("1");
	combo.add("2");
	combo.add("3");
	String[] items = combo.getItems();
	assertTrue(":a:", items.length==3);
	assertTrue(":a:", items[0].equals("1"));
	assertTrue(":a:", items[1].equals("2"));
	assertTrue(":a:", items[2].equals("3"));
}

public void test_getOrientation() {
	// tested in setOrientation
}

public void test_getSelection() {
	combo.setText("123456");
	combo.setSelection(new Point(1,3));
	combo.getSelection();
	assertTrue(":a:", combo.getSelection().equals(new Point(1,3)));
}

public void test_getSelectionIndex() {
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

public void test_getText() {
	String[] cases = {"", "fred", "fredfred"};
	for (int i = 0; i < cases.length; i++) {
		combo.setText(cases[i]);
		assertTrue(":a:" + String.valueOf(i), cases[i].equals(combo.getText()));
	}
}

public void test_getTextHeight() {
	combo.getTextHeight();
}

public void test_getTextLimit() {
	combo.setTextLimit(3);
	assertTrue(":a:", combo.getTextLimit()==3);
}

public void test_hasFocus() {
	// not public api
}

public void test_indexOfLjava_lang_String() {
	combo.add("string0");
	try {
		combo.indexOf(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
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

public void test_indexOfLjava_lang_StringI() {
	combo.add("string0");
	try {
		combo.indexOf(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
	assertEquals(-1, combo.indexOf("string0", -1));
	combo.removeAll();
	
	int number = 5;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	for (int i = 0; i < number; i++)
		assertTrue(":a:" + i, combo.indexOf("fred" + i, 0) == i);
	for (int i = 0; i < number; i++)
		assertTrue(":b:" + i, combo.indexOf("fred" + i, i + 1) == -1);

	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	for (int i = 0; i < 3; i++)
		assertTrue(":a:" + i, combo.indexOf("fred" + i, 0) == i);
	for (int i = 3; i < number; i++)
		assertTrue(":b:" + i, combo.indexOf("fred" + i, 3) == i);
	for (int i = 0; i < number; i++)
		assertTrue(":b:" + i, combo.indexOf("fred" + i, i) == i);
}

public void test_paste() {
	combo.setText("123456");
	combo.setSelection(new Point(1,3));
	combo.cut();
	assertTrue(":a:", combo.getText().equals("1456"));
	combo.paste();
	assertTrue(":a:", combo.getText().equals("123456"));
}

public void test_removeAll() {
	combo.add("1");
	combo.add("2");
	combo.removeAll();
	assertTrue(":a:", combo.getItems().length==0);
}

public void test_removeI() {
	try {
		combo.remove(0);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		combo.remove(3);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	combo.add("string0");
	try {
		combo.remove(-1);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
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
		if (SwtJunit.isWindows || SwtJunit.isGTK || SwtJunit.isCarbon) {
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

	try {
		combo.remove(2, 100);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		combo.remove(-1, number-1);
		fail("No exception thrown for start index < 0");
	}
	catch (IllegalArgumentException e) {
	}

}

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
	try {
		combo.remove(null);
		fail("No exception thrown for item == null");
	}
	catch (IllegalArgumentException e) {
	}

	combo.removeAll();
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	try {	
		combo.remove("fred");
		fail("No exception thrown for item not found");
	}
	catch (IllegalArgumentException e) {
	}
	
	assertEquals(number, combo.getItemCount());
}

public void test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	// tested in addModifyListener method
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	// tested in addSelectionListener method
}
public void test_selectI() {
	combo.add("123");
	combo.add("456");
	combo.add("789");
	combo.select(1);
	assertTrue(":a:", combo.getSelectionIndex()== 1);
	
	// indices out of range are ignored
	combo.select(10);
	assertEquals(1, combo.getSelectionIndex());
}

public void test_setItemILjava_lang_String() {
	try {
		combo.setItem(0, null);
		fail("No exception thrown for item == null");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		combo.setItem(3, null);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		combo.setItem(0, "fred");
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
	
	combo.add("string0");
	try {
		combo.setItem(0, null);
		fail("No exception thrown for item == null");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		combo.setItem(-1, "new value");
		fail("No exception thrown for index < 0");
	}
	catch (IllegalArgumentException e) {
	}
	
	combo.add("joe");
	combo.setItem(0, "fred");	
	assertTrue("fred", combo.getItem(0).equals("fred"));

	try {
		combo.setItem(4, "fred");
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	combo.removeAll();
	int number = 5;
	for (int i = 0; i < number; i++)
		combo.add("fred");
	for (int i = 0; i < number; i++)
		combo.setItem(i, "fred" + i);
	assertEquals(":a:", new String[]{"fred0", "fred1", "fred2", "fred3", "fred4"}, combo.getItems());
}

public void test_setItems$Ljava_lang_String() {
	try {
		combo.setItems(null);
		fail("No exception thrown for items == null");
	}
	catch (IllegalArgumentException e) {
	}

	String[][] items = {{}, {""}, {"", ""}, {"fred"}, {"fred0", "fred0"}, {"fred", "fred"}};

	for (int i = 0 ; i< items.length; i++){
		combo.setItems(items[i]);
		assertEquals(":a:" + i, items[i], combo.getItems());}
}

public void test_setOrientationI() {
	combo.setOrientation(SWT.RIGHT_TO_LEFT);
	if ((combo.getStyle() & SWT.MIRRORED) != 0) {
		assertTrue(":a:", combo.getOrientation()==SWT.RIGHT_TO_LEFT);
	}
	combo.setOrientation(SWT.LEFT_TO_RIGHT);
	assertTrue(":b:", combo.getOrientation()==SWT.LEFT_TO_RIGHT);
}

public void test_setSelectionLorg_eclipse_swt_graphics_Point() {
	try {
		combo.setSelection(null);
		fail("No exception thrown for point == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	int number = 5;
	for (int i = 0; i < number; i++)
		combo.add("fred" + i);
	combo.setSelection(new Point(0, 5));
	assertTrue(":a:", combo.getSelection().equals(new Point(0, 0)));
	combo.setText("some text");
	combo.setSelection(new Point(0, 5));
	assertTrue(":b:", combo.getSelection().equals(new Point(0, 5)));
}

public void test_setTextLimitI() {
	try {
		combo.setTextLimit(0);
		fail("No exception thrown for limit == 0");
	}
	catch (IllegalArgumentException e) {
	}

	combo.setTextLimit(3);
	assertTrue(":a:", combo.getTextLimit()==3);
}

public void test_setTextLjava_lang_String() {
	try {
		combo.setText(null);
		fail("No exception thrown for text == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	String[] cases = {"", "fred", "fred0"};
	for (int i = 0; i < cases.length; i++) {
		combo.setText(cases[i]);
		assertTrue(":a:" + i, combo.getText().equals(cases[i]));
	}
	for (int i = 0; i < 5; i++) {
		combo.add("fred");
	}
	for (int i = 0; i < cases.length; i++) {
		combo.setText(cases[i]);
		assertTrue(":b:" + i, combo.getText().equals(cases[i]));
	}
	for (int i = 0; i < 5; i++) {
		combo.add("fred" + i);
	}
	for (int i = 0; i < cases.length; i++) {
		combo.setText(cases[i]);
		assertTrue(":c:" + i, combo.getText().equals(cases[i]));
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Combo((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addLjava_lang_String");
	methodNames.addElement("test_addLjava_lang_StringI");
	methodNames.addElement("test_addModifyListenerLorg_eclipse_swt_events_ModifyListener");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_clearSelection");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_copy");
	methodNames.addElement("test_cut");
	methodNames.addElement("test_deselectAll");
	methodNames.addElement("test_deselectI");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItemHeight");
	methodNames.addElement("test_getItemI");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getOrientation");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getSelectionIndex");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_getTextHeight");
	methodNames.addElement("test_getTextLimit");
	methodNames.addElement("test_indexOfLjava_lang_String");
	methodNames.addElement("test_indexOfLjava_lang_StringI");
	methodNames.addElement("test_paste");
	methodNames.addElement("test_removeAll");
	methodNames.addElement("test_removeI");
	methodNames.addElement("test_removeII");
	methodNames.addElement("test_removeLjava_lang_String");
	methodNames.addElement("test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_selectI");
	methodNames.addElement("test_setItemILjava_lang_String");
	methodNames.addElement("test_setItems$Ljava_lang_String");
	methodNames.addElement("test_setOrientationI");
	methodNames.addElement("test_setSelectionLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setTextLimitI");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_consistency_MouseSelection");
	methodNames.addElement("test_consistency_KeySelection");
	methodNames.addElement("test_consistency_EnterSelection");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addLjava_lang_String")) test_addLjava_lang_String();
	else if (getName().equals("test_addLjava_lang_StringI")) test_addLjava_lang_StringI();
	else if (getName().equals("test_addModifyListenerLorg_eclipse_swt_events_ModifyListener")) test_addModifyListenerLorg_eclipse_swt_events_ModifyListener();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_clearSelection")) test_clearSelection();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_copy")) test_copy();
	else if (getName().equals("test_cut")) test_cut();
	else if (getName().equals("test_deselectAll")) test_deselectAll();
	else if (getName().equals("test_deselectI")) test_deselectI();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItemHeight")) test_getItemHeight();
	else if (getName().equals("test_getItemI")) test_getItemI();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getOrientation")) test_getOrientation();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getSelectionIndex")) test_getSelectionIndex();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_getTextHeight")) test_getTextHeight();
	else if (getName().equals("test_getTextLimit")) test_getTextLimit();
	else if (getName().equals("test_indexOfLjava_lang_String")) test_indexOfLjava_lang_String();
	else if (getName().equals("test_indexOfLjava_lang_StringI")) test_indexOfLjava_lang_StringI();
	else if (getName().equals("test_paste")) test_paste();
	else if (getName().equals("test_removeAll")) test_removeAll();
	else if (getName().equals("test_removeI")) test_removeI();
	else if (getName().equals("test_removeII")) test_removeII();
	else if (getName().equals("test_removeLjava_lang_String")) test_removeLjava_lang_String();
	else if (getName().equals("test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener")) test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_selectI")) test_selectI();
	else if (getName().equals("test_setItemILjava_lang_String")) test_setItemILjava_lang_String();
	else if (getName().equals("test_setItems$Ljava_lang_String")) test_setItems$Ljava_lang_String();
	else if (getName().equals("test_setOrientationI")) test_setOrientationI();
	else if (getName().equals("test_setSelectionLorg_eclipse_swt_graphics_Point")) test_setSelectionLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setTextLimitI")) test_setTextLimitI();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_consistency_MouseSelection")) test_consistency_MouseSelection();
	else if (getName().equals("test_consistency_KeySelection")) test_consistency_KeySelection();
	else if (getName().equals("test_consistency_EnterSelection")) test_consistency_EnterSelection();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else super.runTest();
}

/* custom */
public void test_setBoundsIIII() {
	combo.setBounds(10, 20, 30, 40);
	// only check x, y, and width - you can't set the height of a combo
	assertTrue(combo.getBounds().x == 10);
	assertTrue(combo.getBounds().y == 20);
	assertTrue(combo.getBounds().width == 30);
}

public void test_setBoundsLorg_eclipse_swt_graphics_Rectangle() {
	combo.setBounds(new Rectangle(10, 20, 30, 40));
	// only check x, y, and width - you can't set the height of a combo
	assertTrue(combo.getBounds().x == 10);
	assertTrue(combo.getBounds().y == 20);
	assertTrue(combo.getBounds().width == 30);
}

public void test_setSizeII() {
	combo.setSize(30, 40);
	// only check the width - you can't set the height of a combo
	assertTrue(combo.getSize().x == 30);

	combo.setSize(32, 43);
	// only check the width - you can't set the height of a combo
	assertTrue(combo.getSize().x == 32);
}

public void test_setSizeLorg_eclipse_swt_graphics_Point() {
	combo.setSize(new Point(30, 40));
	// only check the width - you can't set the height of a combo
	assertTrue(combo.getSize().x == 30);

	combo.setBounds(32, 43, 33, 44);
	// only check the width - you can't set the height of a combo
	assertTrue(combo.getSize().x == 33);

	combo.setBounds(32, 43, 30, 40);
	combo.setLocation(11, 22);
	combo.setSize(new Point(32, 43));
	// only check the width - you can't set the height of a combo
	assertTrue(combo.getSize().x == 32);
}

private void add() {
    combo.add("this");
    combo.add("is");
    combo.add("SWT");
}

public void test_consistency_MouseSelection () {
    add();
    consistencyPrePackShell();
    consistencyEvent(combo.getSize().x-10, 5, 30, combo.getItemHeight()*2, 
            		 ConsistencyUtility.SELECTION);
}

public void test_consistency_KeySelection () {
    add();
    consistencyEvent(0, SWT.ARROW_DOWN, 0, 0, ConsistencyUtility.KEY_PRESS);
}

public void test_consistency_EnterSelection () {
    add();
    consistencyEvent(10, 13, 0, 0, ConsistencyUtility.KEY_PRESS);
}

public void test_consistency_MenuDetect () {
    add();
    consistencyPrePackShell();
    //on arrow
    consistencyEvent(combo.getSize().x-10, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
    //on text
    consistencyEvent(10, 5, 3, ConsistencyUtility.ESCAPE_MENU, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_DragDetect () {
    add();
    consistencyEvent(10, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}



}

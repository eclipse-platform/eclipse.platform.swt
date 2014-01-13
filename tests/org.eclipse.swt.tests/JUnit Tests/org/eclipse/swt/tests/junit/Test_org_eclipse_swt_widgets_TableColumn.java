/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.TableColumn
 *
 * @see org.eclipse.swt.widgets.TableColumn
 */
public class Test_org_eclipse_swt_widgets_TableColumn extends Test_org_eclipse_swt_widgets_Item {

public Test_org_eclipse_swt_widgets_TableColumn(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

@Override
protected void setUp() {
	super.setUp();
	table = new Table(shell, SWT.SINGLE);		
	tableColumn = new TableColumn(table, SWT.NULL);	
	setWidget(tableColumn);
}

public void test_ConstructorLorg_eclipse_swt_widgets_TableI() {
	try {
		new TableColumn(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_TableII() {
	try {
		new TableColumn(null, SWT.NULL, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new TableColumn(table, SWT.NULL, -1);
		fail("No exception thrown for index == -1");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new TableColumn(table, SWT.NULL, 2);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	try {
		tableColumn.addSelectionListener(null);		
		fail("No exception thrown for selectionListener == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getWidth() {
	int testWidth = 42;
	
//	try {
//		tableColumn.setWidth(-1);
//		assertTrue("No exception thrown", false);
//	}
//	catch (IllegalArgumentException e) {
//		assertTrue("Wrong error thrown: " + e, e.getMessage().equals("Argument not valid"));
//	}
//	catch (SWTException e) {
//		assertTrue("Wrong error thrown: " + e, e.getMessage().equals("Argument not valid"));
//	}

	tableColumn.setWidth(0);
	assertTrue(":a: width=" + tableColumn.getWidth() + " should be=" + 0, tableColumn.getWidth() == 0);

	tableColumn.setWidth(testWidth);
	assertTrue(":b: width=" + tableColumn.getWidth() + " should be=" + testWidth, tableColumn.getWidth() == testWidth);

	tableColumn.setWidth(testWidth);
	assertTrue(":c: width=" + tableColumn.getWidth() + " should be=" + testWidth, tableColumn.getWidth() == testWidth);
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	SelectionListener listener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {}};

	tableColumn.removeSelectionListener(listener);	
	tableColumn.addSelectionListener(listener);		
	tableColumn.removeSelectionListener(listener);	
	try {
		tableColumn.removeSelectionListener(null);		
		fail("No exception thrown for selectionListener == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_setAlignmentI() {
	TableColumn column2;
	
	assertTrue(":a:", tableColumn.getAlignment() == SWT.LEFT);

	tableColumn.setAlignment(-1);
	assertTrue(":b:", tableColumn.getAlignment() == SWT.LEFT);

	tableColumn.setAlignment(SWT.RIGHT);
	assertTrue(
		":c: Should not be allowed to set alignment of the first column", 
		tableColumn.getAlignment() == SWT.LEFT);

	column2 = new TableColumn(table, SWT.NULL);
	column2.setAlignment(SWT.RIGHT);
	assertTrue(":d:", column2.getAlignment() == SWT.RIGHT);
	
	column2.setAlignment(SWT.CENTER);
	assertTrue(":e:", column2.getAlignment() == SWT.CENTER);	

	column2.setAlignment(SWT.LEFT);
	assertTrue(":f:", column2.getAlignment() == SWT.LEFT);	
}

@Override
public void test_setImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageLorg_eclipse_swt_graphics_Image not written");
}

public void test_setMoveableZ() {
	assertTrue(":a:", tableColumn.getMoveable() == false);

	tableColumn.setMoveable(true);
	assertTrue(":b:", tableColumn.getMoveable() == true);

	tableColumn.setMoveable(true);
	assertTrue(":c:", tableColumn.getMoveable() == true);

	tableColumn.setMoveable(false);
	assertTrue(":d:", tableColumn.getMoveable() == false);
	
	TableColumn tableColumn2 = new TableColumn(tableColumn.getParent(), SWT.NONE);
	tableColumn2.dispose();
	try {
		tableColumn2.getMoveable();
		fail("No exception thrown for widget is Disposed");
	} catch (SWTException ex) {}
	try {
		tableColumn2.setMoveable(true);
		fail("No exception thrown for widget is Disposed");
	} catch (SWTException ex) {}
}

public void test_setResizableZ() {
	assertTrue(":a:", tableColumn.getResizable() == true);

	tableColumn.setResizable(false);
	assertTrue(":b:", tableColumn.getResizable() == false);

	tableColumn.setResizable(false);
	assertTrue(":c:", tableColumn.getResizable() == false);

	tableColumn.setResizable(true);
	assertTrue(":d:", tableColumn.getResizable() == true);
}

@Override
public void test_setTextLjava_lang_String() {
	assertEquals(":a:", tableColumn.getText(), "");

	tableColumn.setText("text");
	assertEquals(":b:", tableColumn.getText(), "text");

	try {
		tableColumn.setText(null);
		fail("No exception thrown for column header == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector<String> methodNames = methodNames();
	java.util.Enumeration<String> e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_TableColumn(e.nextElement()));
	}
	return suite;
}
public static java.util.Vector<String> methodNames() {
	java.util.Vector<String> methodNames = new java.util.Vector<String>();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TableI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TableII");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_getWidth");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_setAlignmentI");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setMoveableZ");
	methodNames.addElement("test_setResizableZ");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Item.methodNames()); // add superclass method names
	return methodNames;
}
@Override
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TableI")) test_ConstructorLorg_eclipse_swt_widgets_TableI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TableII")) test_ConstructorLorg_eclipse_swt_widgets_TableII();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_getWidth")) test_getWidth();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_setAlignmentI")) test_setAlignmentI();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setMoveableZ")) test_setMoveableZ();
	else if (getName().equals("test_setResizableZ")) test_setResizableZ();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else super.runTest();
}

/* custom */
protected TableColumn tableColumn;
protected Table table;
}

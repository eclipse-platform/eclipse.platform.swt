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
 * Automated Test Suite for class org.eclipse.swt.widgets.TreeColumn
 *
 * @see org.eclipse.swt.widgets.TreeColumn
 */
public class Test_org_eclipse_swt_widgets_TreeColumn extends Test_org_eclipse_swt_widgets_Item {

public Test_org_eclipse_swt_widgets_TreeColumn(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	tree = new Tree(shell, SWT.SINGLE);		
	treeColumn = new TreeColumn(tree, SWT.NULL);	
	setWidget(treeColumn);
}

public void test_ConstructorLorg_eclipse_swt_widgets_TreeI() {
	try {
		new TreeColumn(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_TreeII() {
	try {
		new TreeColumn(null, SWT.NULL, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new TreeColumn(tree, SWT.NULL, -1);
		fail("No exception thrown for index == -1");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new TreeColumn(tree, SWT.NULL, 2);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_addControlListenerLorg_eclipse_swt_events_ControlListener() {
	warnUnimpl("Test test_addControlListenerLorg_eclipse_swt_events_ControlListener not written");
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	try {
		treeColumn.addSelectionListener(null);		
		fail("No exception thrown for selectionListener == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getAlignment() {
	warnUnimpl("Test test_getAlignment not written");
}

public void test_getParent() {
	warnUnimpl("Test test_getParent not written");
}

public void test_getResizable() {
	warnUnimpl("Test test_getResizable not written");
}

public void test_getWidth() {
	int testWidth = 42;
	
//	try {
//		treeColumn.setWidth(-1);
//		assertTrue("No exception thrown", false);
//	}
//	catch (IllegalArgumentException e) {
//		assertTrue("Wrong error thrown: " + e, e.getMessage().equals("Argument not valid"));
//	}
//	catch (SWTException e) {
//		assertTrue("Wrong error thrown: " + e, e.getMessage().equals("Argument not valid"));
//	}

	treeColumn.setWidth(0);
	assertTrue(":a: width=" + treeColumn.getWidth() + " should be=" + 0, treeColumn.getWidth() == 0);

	treeColumn.setWidth(testWidth);
	assertTrue(":b: width=" + treeColumn.getWidth() + " should be=" + testWidth, treeColumn.getWidth() == testWidth);

	treeColumn.setWidth(testWidth);
	assertTrue(":c: width=" + treeColumn.getWidth() + " should be=" + testWidth, treeColumn.getWidth() == testWidth);
}

public void test_pack() {
	warnUnimpl("Test test_pack not written");
}

public void test_removeControlListenerLorg_eclipse_swt_events_ControlListener() {
	warnUnimpl("Test test_removeControlListenerLorg_eclipse_swt_events_ControlListener not written");
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	SelectionListener listener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {}};

	treeColumn.removeSelectionListener(listener);	
	treeColumn.addSelectionListener(listener);		
	treeColumn.removeSelectionListener(listener);	
	try {
		treeColumn.removeSelectionListener(null);		
		fail("No exception thrown for selectionListener == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_setAlignmentI() {
	TreeColumn column2;
	
	assertTrue(":a:", treeColumn.getAlignment() == SWT.LEFT);

	treeColumn.setAlignment(-1);
	assertTrue(":b:", treeColumn.getAlignment() == SWT.LEFT);

	treeColumn.setAlignment(SWT.RIGHT);
	assertTrue(
		":c: Should not be allowed to set alignment of the first column", 
		treeColumn.getAlignment() == SWT.LEFT);

	column2 = new TreeColumn(tree, SWT.NULL);
	column2.setAlignment(SWT.RIGHT);
	assertTrue(":d:", column2.getAlignment() == SWT.RIGHT);
	
	column2.setAlignment(SWT.CENTER);
	assertTrue(":e:", column2.getAlignment() == SWT.CENTER);	

	column2.setAlignment(SWT.LEFT);
	assertTrue(":f:", column2.getAlignment() == SWT.LEFT);	
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageLorg_eclipse_swt_graphics_Image not written");
}

public void test_setResizableZ() {
	assertTrue(":a:", treeColumn.getResizable() == true);

	treeColumn.setResizable(false);
	assertTrue(":b:", treeColumn.getResizable() == false);

	treeColumn.setResizable(false);
	assertTrue(":c:", treeColumn.getResizable() == false);

	treeColumn.setResizable(true);
	assertTrue(":d:", treeColumn.getResizable() == true);
}

public void test_setTextLjava_lang_String() {
	assertEquals(":a:", treeColumn.getText(), "");

	treeColumn.setText("text");
	assertEquals(":b:", treeColumn.getText(), "text");

	try {
		treeColumn.setText(null);
		fail("No exception thrown for column header == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_setWidthI() {
	warnUnimpl("Test test_setWidthI not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_TreeColumn((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TreeI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TreeII");
	methodNames.addElement("test_addControlListenerLorg_eclipse_swt_events_ControlListener");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_getAlignment");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getResizable");
	methodNames.addElement("test_getWidth");
	methodNames.addElement("test_pack");
	methodNames.addElement("test_removeControlListenerLorg_eclipse_swt_events_ControlListener");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_setAlignmentI");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setResizableZ");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_setWidthI");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Item.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TreeI")) test_ConstructorLorg_eclipse_swt_widgets_TreeI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TreeII")) test_ConstructorLorg_eclipse_swt_widgets_TreeII();
	else if (getName().equals("test_addControlListenerLorg_eclipse_swt_events_ControlListener")) test_addControlListenerLorg_eclipse_swt_events_ControlListener();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_getAlignment")) test_getAlignment();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getResizable")) test_getResizable();
	else if (getName().equals("test_getWidth")) test_getWidth();
	else if (getName().equals("test_pack")) test_pack();
	else if (getName().equals("test_removeControlListenerLorg_eclipse_swt_events_ControlListener")) test_removeControlListenerLorg_eclipse_swt_events_ControlListener();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_setAlignmentI")) test_setAlignmentI();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setResizableZ")) test_setResizableZ();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_setWidthI")) test_setWidthI();
	else super.runTest();
}

/* custom */
protected TreeColumn treeColumn;
protected Tree tree;
}

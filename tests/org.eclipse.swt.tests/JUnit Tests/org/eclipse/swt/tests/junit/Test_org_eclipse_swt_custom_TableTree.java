/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.TableTree
 *
 * @see org.eclipse.swt.custom.TableTree
 */
public class Test_org_eclipse_swt_custom_TableTree extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_custom_TableTree(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
}

protected void tearDown() {
	super.tearDown();
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeI not written");
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_addTreeListenerLorg_eclipse_swt_events_TreeListener() {
	warnUnimpl("Test test_addTreeListenerLorg_eclipse_swt_events_TreeListener not written");
}

public void test_deselectAll() {
	warnUnimpl("Test test_deselectAll not written");
}

public void test_getItemCount() {
	warnUnimpl("Test test_getItemCount not written");
}

public void test_getItemHeight() {
	warnUnimpl("Test test_getItemHeight not written");
}

public void test_getItemLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_getItemLorg_eclipse_swt_graphics_Point not written");
}

public void test_getItems() {
	warnUnimpl("Test test_getItems not written");
}

public void test_getSelection() {
	warnUnimpl("Test test_getSelection not written");
}

public void test_getSelectionCount() {
	warnUnimpl("Test test_getSelectionCount not written");
}

public void test_getTable() {
	warnUnimpl("Test test_getTable not written");
}

public void test_indexOfLorg_eclipse_swt_custom_TableTreeItem() {
	warnUnimpl("Test test_indexOfLorg_eclipse_swt_custom_TableTreeItem not written");
}

public void test_removeAll() {
	warnUnimpl("Test test_removeAll not written");
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_removeTreeListenerLorg_eclipse_swt_events_TreeListener() {
	warnUnimpl("Test test_removeTreeListenerLorg_eclipse_swt_events_TreeListener not written");
}

public void test_selectAll() {
	warnUnimpl("Test test_selectAll not written");
}

public void test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem() {
	warnUnimpl("Test test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem not written");
}

public void test_showItemLorg_eclipse_swt_custom_TableTreeItem() {
	warnUnimpl("Test test_showItemLorg_eclipse_swt_custom_TableTreeItem not written");
}

public void test_showSelection() {
	warnUnimpl("Test test_showSelection not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_TableTree((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_addTreeListenerLorg_eclipse_swt_events_TreeListener");
	methodNames.addElement("test_deselectAll");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItemHeight");
	methodNames.addElement("test_getItemLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getSelectionCount");
	methodNames.addElement("test_getTable");
	methodNames.addElement("test_indexOfLorg_eclipse_swt_custom_TableTreeItem");
	methodNames.addElement("test_removeAll");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_removeTreeListenerLorg_eclipse_swt_events_TreeListener");
	methodNames.addElement("test_selectAll");
	methodNames.addElement("test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem");
	methodNames.addElement("test_showItemLorg_eclipse_swt_custom_TableTreeItem");
	methodNames.addElement("test_showSelection");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_addTreeListenerLorg_eclipse_swt_events_TreeListener")) test_addTreeListenerLorg_eclipse_swt_events_TreeListener();
	else if (getName().equals("test_deselectAll")) test_deselectAll();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItemHeight")) test_getItemHeight();
	else if (getName().equals("test_getItemLorg_eclipse_swt_graphics_Point")) test_getItemLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getSelectionCount")) test_getSelectionCount();
	else if (getName().equals("test_getTable")) test_getTable();
	else if (getName().equals("test_indexOfLorg_eclipse_swt_custom_TableTreeItem")) test_indexOfLorg_eclipse_swt_custom_TableTreeItem();
	else if (getName().equals("test_removeAll")) test_removeAll();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_removeTreeListenerLorg_eclipse_swt_events_TreeListener")) test_removeTreeListenerLorg_eclipse_swt_events_TreeListener();
	else if (getName().equals("test_selectAll")) test_selectAll();
	else if (getName().equals("test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem")) test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem();
	else if (getName().equals("test_showItemLorg_eclipse_swt_custom_TableTreeItem")) test_showItemLorg_eclipse_swt_custom_TableTreeItem();
	else if (getName().equals("test_showSelection")) test_showSelection();
	else super.runTest();
}
}

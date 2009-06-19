/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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

/**
 * Automated Test Suite for class org.eclipse.swt.custom.TableTreeEditor
 *
 * @see org.eclipse.swt.custom.TableTreeEditor
 */
public class Test_org_eclipse_swt_custom_TableTreeEditor extends Test_org_eclipse_swt_custom_ControlEditor {

public Test_org_eclipse_swt_custom_TableTreeEditor(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorLorg_eclipse_swt_custom_TableTree() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_custom_TableTree not written");
}

public void test_dispose() {
	warnUnimpl("Test test_dispose not written");
}

public void test_getColumn() {
	warnUnimpl("Test test_getColumn not written");
}

public void test_getItem() {
	warnUnimpl("Test test_getItem not written");
}

public void test_setColumnI() {
	warnUnimpl("Test test_setColumnI not written");
}

public void test_setEditorLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_custom_TableTreeItemI() {
	warnUnimpl("Test test_setEditorLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_custom_TableTreeItemI not written");
}

public void test_setItemLorg_eclipse_swt_custom_TableTreeItem() {
	warnUnimpl("Test test_setItemLorg_eclipse_swt_custom_TableTreeItem not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_TableTreeEditor((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_custom_TableTree");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_getColumn");
	methodNames.addElement("test_getItem");
	methodNames.addElement("test_setColumnI");
	methodNames.addElement("test_setEditorLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_custom_TableTreeItemI");
	methodNames.addElement("test_setItemLorg_eclipse_swt_custom_TableTreeItem");
	methodNames.addAll(Test_org_eclipse_swt_custom_ControlEditor.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_custom_TableTree")) test_ConstructorLorg_eclipse_swt_custom_TableTree();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_getColumn")) test_getColumn();
	else if (getName().equals("test_getItem")) test_getItem();
	else if (getName().equals("test_setColumnI")) test_setColumnI();
	else if (getName().equals("test_setEditorLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_custom_TableTreeItemI")) test_setEditorLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_custom_TableTreeItemI();
	else if (getName().equals("test_setItemLorg_eclipse_swt_custom_TableTreeItem")) test_setItemLorg_eclipse_swt_custom_TableTreeItem();
	else super.runTest();
}
}

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
 * Automated Test Suite for class org.eclipse.swt.custom.TreeEditor
 *
 * @see org.eclipse.swt.custom.TreeEditor
 */
public class Test_org_eclipse_swt_custom_TreeEditor extends Test_org_eclipse_swt_custom_ControlEditor {

public Test_org_eclipse_swt_custom_TreeEditor(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorLorg_eclipse_swt_widgets_Tree() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_Tree not written");
}

public void test_dispose() {
	warnUnimpl("Test test_dispose not written");
}

public void test_getItem() {
	warnUnimpl("Test test_getItem not written");
}

public void test_setEditorLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_TreeItem() {
	warnUnimpl("Test test_setEditorLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_TreeItem not written");
}

public void test_setItemLorg_eclipse_swt_widgets_TreeItem() {
	warnUnimpl("Test test_setItemLorg_eclipse_swt_widgets_TreeItem not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_TreeEditor((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Tree");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_getItem");
	methodNames.addElement("test_setEditorLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_TreeItem");
	methodNames.addElement("test_setItemLorg_eclipse_swt_widgets_TreeItem");
	methodNames.addAll(Test_org_eclipse_swt_custom_ControlEditor.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Tree")) test_ConstructorLorg_eclipse_swt_widgets_Tree();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_getItem")) test_getItem();
	else if (getName().equals("test_setEditorLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_TreeItem")) test_setEditorLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_TreeItem();
	else if (getName().equals("test_setItemLorg_eclipse_swt_widgets_TreeItem")) test_setItemLorg_eclipse_swt_widgets_TreeItem();
	else super.runTest();
}
}

package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.custom.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.ControlEditor
 *
 * @see org.eclipse.swt.custom.ControlEditor
 */
public class Test_org_eclipse_swt_custom_ControlEditor extends SwtTestCase {

public Test_org_eclipse_swt_custom_ControlEditor(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_ConstructorLorg_eclipse_swt_widgets_Composite() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_Composite not written");
}

public void test_dispose() {
	warnUnimpl("Test test_dispose not written");
}

public void test_getEditor() {
	warnUnimpl("Test test_getEditor not written");
}

public void test_setEditorLorg_eclipse_swt_widgets_Control() {
	warnUnimpl("Test test_setEditorLorg_eclipse_swt_widgets_Control not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_ControlEditor((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Composite");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_getEditor");
	methodNames.addElement("test_setEditorLorg_eclipse_swt_widgets_Control");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Composite")) test_ConstructorLorg_eclipse_swt_widgets_Composite();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_getEditor")) test_getEditor();
	else if (getName().equals("test_setEditorLorg_eclipse_swt_widgets_Control")) test_setEditorLorg_eclipse_swt_widgets_Control();
}
}

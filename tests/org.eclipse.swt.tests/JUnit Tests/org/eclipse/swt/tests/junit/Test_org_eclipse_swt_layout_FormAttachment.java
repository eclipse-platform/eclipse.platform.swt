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
 * Automated Test Suite for class org.eclipse.swt.layout.FormAttachment
 *
 * @see org.eclipse.swt.layout.FormAttachment
 */
public class Test_org_eclipse_swt_layout_FormAttachment extends SwtTestCase {

public Test_org_eclipse_swt_layout_FormAttachment(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_ConstructorII() {
	warnUnimpl("Test test_ConstructorII not written");
}

public void test_ConstructorIII() {
	warnUnimpl("Test test_ConstructorIII not written");
}

public void test_ConstructorLorg_eclipse_swt_widgets_Control() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_Control not written");
}

public void test_ConstructorLorg_eclipse_swt_widgets_ControlI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_ControlI not written");
}

public void test_ConstructorLorg_eclipse_swt_widgets_ControlII() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_ControlII not written");
}

public void test_toString() {
	warnUnimpl("Test test_toString not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_layout_FormAttachment((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorII");
	methodNames.addElement("test_ConstructorIII");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ControlI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ControlII");
	methodNames.addElement("test_toString");
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorII")) test_ConstructorII();
	else if (getName().equals("test_ConstructorIII")) test_ConstructorIII();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Control")) test_ConstructorLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ControlI")) test_ConstructorLorg_eclipse_swt_widgets_ControlI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ControlII")) test_ConstructorLorg_eclipse_swt_widgets_ControlII();
	else if (getName().equals("test_toString")) test_toString();
}
}

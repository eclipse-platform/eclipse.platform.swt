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
 * Automated Test Suite for class org.eclipse.swt.layout.RowData
 *
 * @see org.eclipse.swt.layout.RowData
 */
public class Test_org_eclipse_swt_layout_RowData extends SwtTestCase {

public Test_org_eclipse_swt_layout_RowData(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_ConstructorII() {
	warnUnimpl("Test test_ConstructorII not written");
}

public void test_ConstructorLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_Point not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_layout_RowData((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_ConstructorII");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_Point");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorII")) test_ConstructorII();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_Point")) test_ConstructorLorg_eclipse_swt_graphics_Point();
}
}

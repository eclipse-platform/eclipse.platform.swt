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
 * Automated Test Suite for class org.eclipse.swt.widgets.Event
 *
 * @see org.eclipse.swt.widgets.Event
 */
public class Test_org_eclipse_swt_widgets_Event extends SwtTestCase {

public Test_org_eclipse_swt_widgets_Event(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_getBounds() {
	warnUnimpl("Test test_getBounds not written");
}

public void test_setBoundsLorg_eclipse_swt_graphics_Rectangle() {
	warnUnimpl("Test test_setBoundsLorg_eclipse_swt_graphics_Rectangle not written");
}

public void test_toString() {
	warnUnimpl("Test test_toString not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Event((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_setBoundsLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_toString");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_setBoundsLorg_eclipse_swt_graphics_Rectangle")) test_setBoundsLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_toString")) test_toString();
}
}

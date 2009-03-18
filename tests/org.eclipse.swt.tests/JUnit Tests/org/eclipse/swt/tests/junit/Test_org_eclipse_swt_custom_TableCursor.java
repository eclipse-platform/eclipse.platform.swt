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
 * Automated Test Suite for class org.eclipse.swt.custom.TableCursor
 *
 * @see org.eclipse.swt.custom.TableCursor
 */
public class Test_org_eclipse_swt_custom_TableCursor extends Test_org_eclipse_swt_widgets_Canvas {

public Test_org_eclipse_swt_custom_TableCursor(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorLorg_eclipse_swt_widgets_TableI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_TableI not written");
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_getColumn() {
	warnUnimpl("Test test_getColumn not written");
}

public void test_getRow() {
	warnUnimpl("Test test_getRow not written");
}

public void test_setSelectionII() {
	warnUnimpl("Test test_setSelectionII not written");
}

public void test_setSelectionLorg_eclipse_swt_widgets_TableItemI() {
	warnUnimpl("Test test_setSelectionLorg_eclipse_swt_widgets_TableItemI not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_TableCursor((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TableI");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_getColumn");
	methodNames.addElement("test_getRow");
	methodNames.addElement("test_setSelectionII");
	methodNames.addElement("test_setSelectionLorg_eclipse_swt_widgets_TableItemI");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Canvas.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TableI")) test_ConstructorLorg_eclipse_swt_widgets_TableI();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_getColumn")) test_getColumn();
	else if (getName().equals("test_getRow")) test_getRow();
	else if (getName().equals("test_setSelectionII")) test_setSelectionII();
	else if (getName().equals("test_setSelectionLorg_eclipse_swt_widgets_TableItemI")) test_setSelectionLorg_eclipse_swt_widgets_TableItemI();
	else super.runTest();
}
}

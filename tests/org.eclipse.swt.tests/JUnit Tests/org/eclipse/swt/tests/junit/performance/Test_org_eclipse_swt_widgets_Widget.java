/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.performance;

import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.widgets.Widget
 *
 * @see org.eclipse.swt.widgets.Widget
 */
public class Test_org_eclipse_swt_widgets_Widget extends SwtPerformanceTestCase {

public Test_org_eclipse_swt_widgets_Widget(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	shell = new Shell();
}

protected void tearDown() {
	if (widget != null) {
		assertEquals(false, widget.isDisposed());
	}
	assertEquals(false, shell.isDisposed());
	shell.dispose();
	if (widget != null) {
		assertTrue(widget.isDisposed());
	}
	assertTrue(shell.isDisposed());
}

public void test_dispose() {
}

public void test_getDisplay() {
}

public void test_getStyle() {
}

public void test_isDisposed() {
}

public void test_notifyListenersILorg_eclipse_swt_widgets_Event() {
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Widget((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_getDisplay");
	methodNames.addElement("test_getStyle");
	methodNames.addElement("test_isDisposed");
	methodNames.addElement("test_notifyListenersILorg_eclipse_swt_widgets_Event");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_getDisplay")) test_getDisplay();
	else if (getName().equals("test_getStyle")) test_getStyle();
	else if (getName().equals("test_isDisposed")) test_isDisposed();
	else if (getName().equals("test_notifyListenersILorg_eclipse_swt_widgets_Event")) test_notifyListenersILorg_eclipse_swt_widgets_Event();
}

/* custom */
public Shell shell;
private Widget widget;

protected void setWidget(Widget w) {
	widget = w;
}
}

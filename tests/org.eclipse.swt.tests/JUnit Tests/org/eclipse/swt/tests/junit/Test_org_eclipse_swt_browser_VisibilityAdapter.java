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
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.VisibilityAdapter
 *
 * @see org.eclipse.swt.browser.VisibilityAdapter
 */
public class Test_org_eclipse_swt_browser_VisibilityAdapter extends SwtTestCase {

public Test_org_eclipse_swt_browser_VisibilityAdapter(String name) {
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
	new VisibilityAdapter() {};
}

public void test_hideLorg_eclipse_swt_browser_VisibilityEvent() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	browser.addVisibilityListener(new VisibilityAdapter(){});
	shell.close();
}

public void test_showLorg_eclipse_swt_browser_VisibilityEvent() {
	// tested in test_hideLorg_eclipse_swt_browser_VisibilityEvent
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_browser_VisibilityAdapter((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_hideLorg_eclipse_swt_browser_VisibilityEvent");
	methodNames.addElement("test_showLorg_eclipse_swt_browser_VisibilityEvent");
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_hideLorg_eclipse_swt_browser_VisibilityEvent")) test_hideLorg_eclipse_swt_browser_VisibilityEvent();
	else if (getName().equals("test_showLorg_eclipse_swt_browser_VisibilityEvent")) test_showLorg_eclipse_swt_browser_VisibilityEvent();
}
}

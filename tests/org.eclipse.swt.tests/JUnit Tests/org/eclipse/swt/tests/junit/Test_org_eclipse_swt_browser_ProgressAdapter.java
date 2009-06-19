/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.ProgressAdapter
 *
 * @see org.eclipse.swt.browser.ProgressAdapter
 */
public class Test_org_eclipse_swt_browser_ProgressAdapter extends SwtTestCase {

public Test_org_eclipse_swt_browser_ProgressAdapter(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	new ProgressAdapter() {};
}

public void test_changedLorg_eclipse_swt_browser_ProgressEvent() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	browser.addProgressListener(new ProgressAdapter() {});
	shell.close();
}

public void test_completedLorg_eclipse_swt_browser_ProgressEvent() {
	warnUnimpl("Test test_completedLorg_eclipse_swt_browser_ProgressEvent not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_browser_ProgressAdapter((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_changedLorg_eclipse_swt_browser_ProgressEvent");
	methodNames.addElement("test_completedLorg_eclipse_swt_browser_ProgressEvent");
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_changedLorg_eclipse_swt_browser_ProgressEvent")) test_changedLorg_eclipse_swt_browser_ProgressEvent();
	else if (getName().equals("test_completedLorg_eclipse_swt_browser_ProgressEvent")) test_completedLorg_eclipse_swt_browser_ProgressEvent();
}
}

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
package org.eclipse.swt.tests.junit.browser;

import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.tests.junit.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.StatusTextListener
 *
 * @see org.eclipse.swt.browser.StatusTextListener
 */
public class Test_BrowserSuite extends SwtTestCase {

public Test_BrowserSuite(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void Browser1() {
	boolean result = Browser1.test();
	assertTrue(result);
}

public void Browser2() {
	boolean result = Browser2.test();
	assertTrue(result);
}

public void Browser3() {
	boolean result = Browser3.test();
	assertTrue(result);
}

public void Browser4() {
	boolean result = Browser4.test();
	assertTrue(result);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_BrowserSuite((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("Browser1");
	methodNames.addElement("Browser2");
	methodNames.addElement("Browser3");
	methodNames.addElement("Browser4");
	return methodNames;
}

protected void runTest() throws Throwable {
	/*
	 * The tests in this suite manage their own Display and event loop
	 * to validate asynchronous use cases.
	 * Dispose any previously existing display for the calling thread
	 * before starting the tests.
	 */
	Display display = Display.getCurrent();
	if (display != null) display.dispose();
	
	if (getName().equals("Browser1")) Browser1();
	if (getName().equals("Browser2")) Browser2();
	if (getName().equals("Browser3")) Browser3();
	if (getName().equals("Browser4")) Browser4();

	/*
	 * Ensure we don't leave a Display from the tests we run.
	 */	
	display = Display.getCurrent();
	if (display != null) display.dispose();
}
}

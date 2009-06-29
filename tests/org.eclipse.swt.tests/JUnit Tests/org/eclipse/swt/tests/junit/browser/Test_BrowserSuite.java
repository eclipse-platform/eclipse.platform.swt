/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public static boolean verbose = false;

public Test_BrowserSuite(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void Browser1() {
	if (verbose) System.out.println("** Browser1 **");
	boolean result = Browser1.test();
	assertTrue(result);
}

public void Browser2() {
	if (verbose) System.out.println("** Browser2 **");
	boolean result = Browser2.test();
	assertTrue(result);
}

public void Browser3() {
	if (verbose) System.out.println("** Browser3 **");
	boolean result = Browser3.test();
	assertTrue(result);
}

public void Browser4() {
	if (verbose) System.out.println("** Browser4 **");
	boolean result = Browser4.test();
	assertTrue(result);
}

public void Browser5() {
	if (verbose) System.out.println("** Browser5 **");
	boolean result = Browser5.test();
	assertTrue(result);
}

public void Browser6() {
	if (verbose) System.out.println("** Browser6 **");
	boolean result = Browser6.test();
	assertTrue(result);
}

public void Browser7() {
	if (verbose) System.out.println("** Browser7 **");
	boolean result = Browser7.test();
	assertTrue(result);
}

public void Browser8() {
	if (verbose) System.out.println("** Browser8 **");
	boolean result = Browser8.test();
	assertTrue(result);
}

public void Browser9() {
	if (verbose) System.out.println("** Browser9 **");
	boolean result = Browser9.test();
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
	methodNames.addElement("Browser5");
	methodNames.addElement("Browser6");
	methodNames.addElement("Browser7");
	methodNames.addElement("Browser8");
	methodNames.addElement("Browser9");
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
	if (getName().equals("Browser5")) Browser5();
	if (getName().equals("Browser6")) Browser6();
	if (getName().equals("Browser7")) Browser7();
	if (getName().equals("Browser8")) Browser8();
	if (getName().equals("Browser9")) Browser9();

	/*
	 * Ensure we don't leave a Display from the tests we run.
	 */	
	display = Display.getCurrent();
	if (display != null) display.dispose();
}
}

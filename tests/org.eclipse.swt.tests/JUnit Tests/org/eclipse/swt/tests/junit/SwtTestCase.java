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

public class SwtTestCase extends TestCase {
	/**
	 * The following flags are used to mark test cases that
	 * are not handled correctly by SWT at this time, or test
	 * cases that maybe themselves dubious (eg. when the correct
	 * behaviour may not be clear). Most of these flagged test
	 * cases involve handling error conditions.
	 *
	 * Setting these flags to true will run those tests. As api
	 * is implemented this gives us a convenient way to include
	 * it in the junit tests.
	 */

	// call should result in an 'Argument cannot be null' SWT error
	public static boolean fCheckSwtNullExceptions = false;

	// an out of range value is not handled gracefully according to our SWT policy
	public static boolean fCheckOutOfRangeBehaviour = false;

	// run test cases that may themselves be dubious
	// these should be eventually checked to see if 
	// there is a valid failure or the test is bogus
	public static boolean fCheckBogusTestCases = false;

	// check multi select tree api
	public static boolean fCheckMultiSelectTree = false;

	// check visibility api (eg in menu)
	public static boolean fCheckVisibility = false;

	// run test cases that check SWT policy not covered by the flags above
	public static boolean fCheckSWTPolicy = false;

	// make dialog open calls, operator must then close them
	public static boolean fTestDialogOpen = false;

	// variable to keep track of the number of unimplemented methods
	public static int unimplementedMethods;
	
	// used to specify verbose mode, if true unimplemented warning messages will 
	// be written to System.out
	public static boolean verbose = false;
	
public SwtTestCase(String name) {
	super(name);
}

static public void assertEquals(String message, Object expected[], Object actual[]) {
	if (expected == null && actual == null)
		return;
	boolean equal = false;
	if (expected != null && actual != null && expected.length == actual.length) {
		if (expected.length == 0)
			return;
		equal = true;
		for (int i = 0; i < expected.length; i++) {
			if (!expected[i].equals(actual[i])) {
				equal = false;
			}
		}
	}
	if (!equal) {
		failNotEquals(message, expected, actual);
	}
}
static public void assertEquals(Object expected[], Object actual[]) {
    assertEquals(null, expected, actual);
}
static public void assertEquals(String message, int expected[], int actual[]) {
	if (expected == null && actual == null)
		return;
	boolean equal = false;
	if (expected != null && actual != null && expected.length == actual.length) {
		if (expected.length == 0)
			return;
		equal = true;
		for (int i = 0; i < expected.length; i++) {
			if (expected[i] != actual[i]) {
				equal = false;
			}
		}
	}
	if (!equal) {
		failNotEquals(message, expected, actual);
	}
}
static public void assertEquals(int expected[], int actual[]) {
    assertEquals(null, expected, actual);
}
// copied exactly from junit.framework.TestCase so that it can be called from here even though private
static private void failNotEquals(String message, Object expected, Object actual) {
	String formatted= "";
	if (message != null)
		formatted= message+" ";
	fail(formatted+"expected:<"+expected+"> but was:<"+actual+">");
}

protected void warnUnimpl(String message) {
	if (verbose) {
		System.out.println(this.getClass() + ": " + message);
	}
	unimplementedMethods++;
}
}

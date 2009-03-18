/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import java.io.*;
import junit.framework.*;
import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;

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

	// run test cases that may themselves be dubious
	// these should be eventually checked to see if 
	// there is a valid failure or the test is bogus
	public static boolean fCheckBogusTestCases = false;

	// check visibility api (eg in menu)
	public static boolean fCheckVisibility = false;

	// run test cases that check SWT policy not covered by the flags above
	public static boolean fCheckSWTPolicy = false;

	// make dialog open calls, operator must then close them
	public static boolean fTestDialogOpen = false;
	
	public static boolean fTestConsistency = false;

	// variable to keep track of the number of unimplemented methods
	public static int unimplementedMethods;
	
	// variable to keep track of the number of unimplemented API methods
	public static int unimplementedAPI;
	
	// used to specify verbose mode, if true unimplemented warning messages will 
	// be written to System.out
	public static boolean verbose = false;
	
	// allow specific image formats to be tested
	public static String[] imageFormats = new String[] {"bmp", "jpg", "gif", "png"};
	public static String[] imageFilenames = new String[] {"folder", "folderOpen", "target"};
	public static String[] invalidImageFilenames = new String[] {"corrupt", "corruptBadBitDepth.png"};
	public static String[] transparentImageFilenames = new String[] {"transparent.png"};
	
	// specify reparentable platforms
	public static String[] reparentablePlatforms = new String[] {"win32", "gtk", "carbon", "cocoa"};
	
public SwtTestCase(String name) {
	super(name);
}
static public void assertSame(String message, Object expected[], Object actual[]) {
	if (expected == null && actual == null) return;
	boolean same = false;
	if (expected != null && actual != null && expected.length == actual.length) {
		if (expected.length == 0) return;
		same = true;
		boolean[] matched = new boolean[expected.length];
		for (int i = 0; i < actual.length; i++) {
			boolean match = false;
			for (int j = 0; j < expected.length; j++) {
				if (!matched[j]) {
					if ((actual[i] == null && expected [j] == null) ||
					    (actual[i] != null && actual[i].equals(expected[j]))) {
						match = true;
						matched[j] = true;
						break;
					}
				}
			}
			if (!match) {
				same = false;
				break;
			}
		}
	}
	if (!same) {
		failNotEquals(message, expected, actual);
	}
}
static public void assertSame(Object expected[], Object actual[]) {
	assertSame(null, expected, actual);
}
static public void assertSame(String message, int expected[], int actual[]) {
	if (expected == null && actual == null) return;
	boolean same = false;
	if (expected != null && actual != null && expected.length == actual.length) {
		if (expected.length == 0) return;
		same = true;
		boolean[] matched = new boolean[expected.length];
		for (int i = 0; i < actual.length; i++) {
			boolean match = false;
			for (int j = 0; j < expected.length; j++) {
				if (!matched[j] && actual[i] == expected[j]) {
					match = true;
					matched[j] = true;
					break;
				}
			}
			if (!match) {
				same = false;
				break;
			}
		}
	}
	if (!same) {
		failNotEquals(message, expected, actual);
	}
}
static public void assertSame(int expected[], int actual[]) {
	assertSame(null, expected, actual);
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
static public void assertEquals(String message, int expectedCode, Throwable actualThrowable) {
	if (actualThrowable instanceof SWTError) {
		SWTError error = (SWTError) actualThrowable;
		assertEquals(message, expectedCode, error.code);
	} else if (actualThrowable instanceof SWTException) {
		SWTException exception = (SWTException) actualThrowable;
		assertEquals(message, expectedCode, exception.code);
	} else {
		try {
			SWT.error(expectedCode);
		} catch (Throwable expectedThrowable) {
			assertEquals(message, expectedThrowable.getMessage(), actualThrowable.getMessage());
		}
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

static private void failNotEquals(String message, Object[] expected, Object[] actual) {
	String formatted= "";
	if (message != null)
		formatted= message+" ";
	formatted += "expected:<";
	if(expected != null) {
	    for(int i=0; i<Math.min(expected.length, 5); i++)
	        formatted += expected[i] +", ";
	    if(expected.length != 0)
	        formatted = formatted.substring(0, formatted.length()-2);
	}
	if(expected.length > 5)
	    formatted += "...";
	formatted += "> but was:<";
	if(actual != null) {
	    for(int i=0; i<Math.min(actual.length, 5); i++)
	        formatted += actual[i] +", ";
	    if(actual.length != 0)
	        formatted = formatted.substring(0, formatted.length()-2);
	}
	if(actual.length > 5)
	    formatted += "...";
	fail(formatted+">");
}

protected boolean isJ2ME() {
	try {
		Compatibility.newFileInputStream("");
	} catch (FileNotFoundException e) {
		return false;
	} catch (IOException e) {
	}
	return true;
}
protected boolean isReparentablePlatform() {
	String platform = SWT.getPlatform();
	for (int i=0; i<reparentablePlatforms.length; i++) {
		if (reparentablePlatforms[i].equals(platform)) return true;
	}
	return false;
}

protected boolean isBidi() {
	return  SWT.getPlatform().equals("gtk") || SWT.getPlatform().equals("carbon") ||  SWT.getPlatform().equals("cocoa") || BidiUtil.isBidiPlatform();// || isMirrored;
}

protected void setUp() {
	try {
		super.setUp();
	} catch (Exception e) {
		SWTError error = new SWTError();
		error.throwable = e;
		throw error;
	}
}

protected void tearDown() {
	try {
		super.tearDown();
		Display display = Display.getCurrent();
		if (display != null) display.readAndDispatch();
	} catch (Exception e) {
		SWTError error = new SWTError();
		error.throwable = e;
		throw error;
	}
}

protected void warnUnimpl(String message) {
	if (verbose) {
		System.out.println(this.getClass() + ": " + message);
	}
	unimplementedMethods++;
}
protected void warnUnimplAPI(String message) {
	if (verbose) {
		System.out.println("API not implemented " + this.getClass() + " " + getName());
	}
	unimplementedAPI++;
}
}

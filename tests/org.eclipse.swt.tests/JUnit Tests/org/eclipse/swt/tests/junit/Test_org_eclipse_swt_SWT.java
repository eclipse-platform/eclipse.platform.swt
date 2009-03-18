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
import org.eclipse.swt.*;

/**
 * Automated Test Suite for class org.eclipse.swt.SWT
 *
 * @see org.eclipse.swt.SWT
 */
public class Test_org_eclipse_swt_SWT extends SwtTestCase {

public Test_org_eclipse_swt_SWT(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	// Do nothing. Class SWT is not intended to be subclassed.
}

public void test_errorI() {
	// Test that we throw the expected kinds of errors for the given error types.
	boolean passed = false;
	try {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	} catch (IllegalArgumentException ex) {
		passed = true;
	} catch (Throwable t) { }
	assertTrue ("did not correctly throw exception for ERROR_NULL_ARGUMENT", passed);
	passed = false;
	try {
		SWT.error(SWT.ERROR_FAILED_EXEC);
	} catch (SWTException ex) {
		passed = true;
	} catch (Throwable t) { }
	assertTrue ("did not correctly throw exception for ERROR_FAILED_EXEC", passed);
	passed = false;
	try {
		SWT.error(SWT.ERROR_NO_HANDLES);
	} catch (SWTError ex) {
		passed = true;
	} catch (Throwable t) { }
	assertTrue ("did not correctly throw exception for ERROR_NO_HANDLES", passed);
	passed = false;
	try {
		SWT.error(-1);
	} catch (SWTError ex) {
		passed = true;
	} catch (Throwable t) { }
	assertTrue ("did not correctly throw exception for error(-1)", passed);
}

public void test_errorILjava_lang_Throwable() {
	// Test that the causing throwable is filled in.
	Throwable cause = new RuntimeException("Just for testing");
	boolean passed = false;
	try {
		SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT, cause);
	} catch (SWTException ex) {
		passed = ex.throwable == cause;
	} catch (Throwable t) { }
	assertTrue ("did not correctly throw exception for ERROR_UNSUPPORTED_FORMAT", passed);
	passed = false;
	try {
		SWT.error(SWT.ERROR_NOT_IMPLEMENTED, cause);
	} catch (SWTError ex) {
		passed = ex.throwable == cause;
	} catch (Throwable t) { }
	assertTrue ("did not correctly throw exception for ERROR_NOT_IMPLEMENTED", passed);
	passed = false;
	try {
		SWT.error(-1, cause);
	} catch (SWTError ex) {
		passed = ex.throwable == cause;
	} catch (Throwable t) { }
	assertTrue ("did not correctly throw exception for error(-1)", passed);
}

public void test_getMessageLjava_lang_String() {
	boolean passed = false;
	try {
		passed = false;
		SWT.getMessage(null);
	} catch (IllegalArgumentException ex) {
		passed = true;
	}
	assertTrue ("did not correctly throw exception with null argument", passed);
	try {
		SWT.getMessage("SWT_Yes");
	} catch (Throwable t) {
		fail ("exception " + t + " generated for SWT_Yes");
	}
	assertTrue (
		"invalid key did not return as itself",
		"_NOT_FOUND_IN_PROPERTIES_".equals(SWT.getMessage("_NOT_FOUND_IN_PROPERTIES_")));
		
}

public void test_getPlatform() {
	// Can't test the list of platforms, since this may change,
	// so just test to see it returns something.
	assertTrue ("returned null platform name", SWT.getPlatform() != null);
}

public void test_getVersion() {
	// Test that the version number which is returned is reasonable.
	int ver = SWT.getVersion();
	assertTrue ("unreasonable value returned", ver > 0 && ver < 1000000);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_SWT((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_errorI");
	methodNames.addElement("test_errorILjava_lang_Throwable");
	methodNames.addElement("test_getMessageLjava_lang_String");
	methodNames.addElement("test_getPlatform");
	methodNames.addElement("test_getVersion");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_errorI")) test_errorI();
	else if (getName().equals("test_errorILjava_lang_Throwable")) test_errorILjava_lang_Throwable();
	else if (getName().equals("test_getMessageLjava_lang_String")) test_getMessageLjava_lang_String();
	else if (getName().equals("test_getPlatform")) test_getPlatform();
	else if (getName().equals("test_getVersion")) test_getVersion();
}
}

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


import org.eclipse.swt.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.SWTException
 *
 * @see org.eclipse.swt.SWTException
 */
public class Test_org_eclipse_swt_SWTException extends SwtTestCase {

public Test_org_eclipse_swt_SWTException(String name) {
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
	assertTrue (
		"did not fill in code properly",
		new SWTException().code == SWT.ERROR_UNSPECIFIED);
}

public void test_ConstructorLjava_lang_String() {
	assertTrue (
		"did not fill in code properly",
		new SWTException("An uninteresting message").code == SWT.ERROR_UNSPECIFIED);
}

public void test_ConstructorI() {
	assertTrue (
		"did not fill in code properly",
		new SWTException(SWT.ERROR_CANNOT_BE_ZERO).code == SWT.ERROR_CANNOT_BE_ZERO);
}

public void test_ConstructorILjava_lang_String() {
	assertTrue (
		"did not fill in code properly",
		new SWTException(SWT.ERROR_CANNOT_BE_ZERO, "An uninteresting message").code 
			== SWT.ERROR_CANNOT_BE_ZERO);
}

public void test_getMessage() {
	assertTrue (
		"did not include creation string in result",
		new SWTException(SWT.ERROR_CANNOT_BE_ZERO, "An interesting message").getMessage() 
			.indexOf("An interesting message") >= 0);
}

public void test_printStackTraceLjava_io_PrintStream() {
	// Should not test. Result varies by VM.
}

public void test_printStackTraceLjava_io_PrintWriter() {
	// Should not test. Result varies by VM.
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_SWTException((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_ConstructorLjava_lang_String");
	methodNames.addElement("test_ConstructorI");
	methodNames.addElement("test_ConstructorILjava_lang_String");
	methodNames.addElement("test_getMessage");
	methodNames.addElement("test_printStackTraceLjava_io_PrintStream");
	methodNames.addElement("test_printStackTraceLjava_io_PrintWriter");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorLjava_lang_String")) test_ConstructorLjava_lang_String();
	else if (getName().equals("test_ConstructorI")) test_ConstructorI();
	else if (getName().equals("test_ConstructorILjava_lang_String")) test_ConstructorILjava_lang_String();
	else if (getName().equals("test_getMessage")) test_getMessage();
	else if (getName().equals("test_printStackTraceLjava_io_PrintStream")) test_printStackTraceLjava_io_PrintStream();
	else if (getName().equals("test_printStackTraceLjava_io_PrintWriter")) test_printStackTraceLjava_io_PrintWriter();
}
}

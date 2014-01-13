/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;

/**
 * Automated Test Suite for class org.eclipse.swt.SWTError
 *
 * @see org.eclipse.swt.SWTError
 */
public class Test_org_eclipse_swt_SWTError extends TestCase {

public void test_Constructor() {
	assertTrue (
		"did not fill in code properly",
		new SWTError().code == SWT.ERROR_UNSPECIFIED);
}

public void test_ConstructorI() {
	assertTrue (
		"did not fill in code properly",
		new SWTError(SWT.ERROR_CANNOT_BE_ZERO).code == SWT.ERROR_CANNOT_BE_ZERO);
}

public void test_ConstructorILjava_lang_String() {
	assertTrue (
		"did not fill in code properly",
		new SWTError(SWT.ERROR_CANNOT_BE_ZERO, "An uninteresting message").code 
			== SWT.ERROR_CANNOT_BE_ZERO);
}

public void test_ConstructorLjava_lang_String() {
	assertTrue (
		"did not fill in code properly",
		new SWTError("An uninteresting message").code == SWT.ERROR_UNSPECIFIED);
}

public void test_getMessage() {
	assertTrue (
		"did not include creation string in result",
		new SWTError(SWT.ERROR_CANNOT_BE_ZERO, "An interesting message").getMessage() 
			.indexOf("An interesting message") >= 0);
}

public void test_printStackTrace() {
	
	// WARNING: this test is not CLDC safe, because it requires java.io.PrintStream
	
	try {
		Class.forName("java.io.PrintStream");
	} catch (ClassNotFoundException e) {
		// ignore test if running on CLDC
		return;
	}
	
	// test default SWTError
	
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	System.setErr(new PrintStream(out));
	SWTError error = new SWTError();
	error.printStackTrace();
	assertTrue(out.size() > 0);
	assertTrue(new String(out.toByteArray()).indexOf("test_printStackTrace") != -1);
	
	// test SWTError with code
	
	out = new ByteArrayOutputStream();
	System.setErr(new PrintStream(out));
	error = new SWTError(SWT.ERROR_INVALID_ARGUMENT);
	error.printStackTrace();
	assertTrue(out.size() > 0);
	assertTrue(new String(out.toByteArray()).indexOf("test_printStackTrace") != -1);
}
}

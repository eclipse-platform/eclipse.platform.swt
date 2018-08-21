/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.SWTException
 *
 * @see org.eclipse.swt.SWTException
 */
public class Test_org_eclipse_swt_SWTException {

@Test
public void test_Constructor() {
	assertTrue (
		"did not fill in code properly",
		new SWTException().code == SWT.ERROR_UNSPECIFIED);
}

@Test
public void test_ConstructorI() {
	assertTrue (
		"did not fill in code properly",
		new SWTException(SWT.ERROR_CANNOT_BE_ZERO).code == SWT.ERROR_CANNOT_BE_ZERO);
}

@Test
public void test_ConstructorILjava_lang_String() {
	assertTrue (
		"did not fill in code properly",
		new SWTException(SWT.ERROR_CANNOT_BE_ZERO, "An uninteresting message").code
			== SWT.ERROR_CANNOT_BE_ZERO);
}

@Test
public void test_ConstructorLjava_lang_String() {
	assertTrue (
		"did not fill in code properly",
		new SWTException("An uninteresting message").code == SWT.ERROR_UNSPECIFIED);
}

@Test
public void test_getMessage() {
	assertTrue (
		"did not include creation string in result",
		new SWTException(SWT.ERROR_CANNOT_BE_ZERO, "An interesting message").getMessage()
			.indexOf("An interesting message") >= 0);
}

@Test
public void test_printStackTrace() {

	// WARNING: this test is not CLDC safe, because it requires java.io.PrintStream

	try {
		Class.forName("java.io.PrintStream");
	} catch (ClassNotFoundException e) {
		// ignore test if running on CLDC
		return;
	}

	// test default SWTException

	ByteArrayOutputStream out = new ByteArrayOutputStream();
	System.setErr(new PrintStream(out));
	SWTException error = new SWTException();
	error.printStackTrace();
	assertTrue(out.size() > 0);
	assertTrue(new String(out.toByteArray()).indexOf("test_printStackTrace") != -1);

	// test SWTException with code

	out = new ByteArrayOutputStream();
	System.setErr(new PrintStream(out));
	error = new SWTException(SWT.ERROR_INVALID_ARGUMENT);
	error.printStackTrace();
	assertTrue(out.size() > 0);
	assertTrue(new String(out.toByteArray()).indexOf("test_printStackTrace") != -1);
}
}

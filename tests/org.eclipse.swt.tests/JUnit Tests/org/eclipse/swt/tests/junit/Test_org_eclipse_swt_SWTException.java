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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.SWTException
 *
 * @see org.eclipse.swt.SWTException
 */
public class Test_org_eclipse_swt_SWTException {

@Test
public void test_Constructor() {
	assertEquals(SWT.ERROR_UNSPECIFIED, new SWTException().code, "did not fill in code properly");
}

@Test
public void test_ConstructorI() {
	assertEquals(SWT.ERROR_CANNOT_BE_ZERO, new SWTException(SWT.ERROR_CANNOT_BE_ZERO).code,
			"did not fill in code properly");
}

@Test
public void test_ConstructorILjava_lang_String() {
	assertEquals(SWT.ERROR_CANNOT_BE_ZERO, new SWTException(SWT.ERROR_CANNOT_BE_ZERO, "An uninteresting message").code,
			"did not fill in code properly");
}

@Test
public void test_ConstructorLjava_lang_String() {
	assertEquals(SWT.ERROR_UNSPECIFIED, new SWTException("An uninteresting message").code,
			"did not fill in code properly");
}

@Test
public void test_getMessage() {
	assertTrue(new SWTException(SWT.ERROR_CANNOT_BE_ZERO, "An interesting message").getMessage()
			.contains("An interesting message"), "did not include creation string in result");
}

@Test
public void test_printStackTrace() {

	// test default SWTException

	ByteArrayOutputStream out = new ByteArrayOutputStream();
	System.setErr(new PrintStream(out));
	SWTException error = new SWTException();
	error.printStackTrace();
	assertTrue(out.size() > 0);
	assertTrue(new String(out.toByteArray()).contains("test_printStackTrace"));

	// test SWTException with code

	out = new ByteArrayOutputStream();
	System.setErr(new PrintStream(out));
	error = new SWTException(SWT.ERROR_INVALID_ARGUMENT);
	error.printStackTrace();
	assertTrue(out.size() > 0);
	assertTrue(new String(out.toByteArray()).contains("test_printStackTrace"));
}
}

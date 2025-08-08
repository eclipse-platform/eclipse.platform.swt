/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
import org.eclipse.swt.SWTError;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.SWTError
 *
 * @see org.eclipse.swt.SWTError
 */
public class Test_org_eclipse_swt_SWTError {

@Test
public void test_Constructor() {
	assertEquals(SWT.ERROR_UNSPECIFIED, new SWTError().code, "did not fill in code properly");
}

@Test
public void test_ConstructorI() {
	assertEquals(SWT.ERROR_CANNOT_BE_ZERO, new SWTError(SWT.ERROR_CANNOT_BE_ZERO).code,
			"did not fill in code properly");
}

@Test
public void test_ConstructorILjava_lang_String() {
	assertEquals(SWT.ERROR_CANNOT_BE_ZERO, new SWTError(SWT.ERROR_CANNOT_BE_ZERO, "An uninteresting message").code,
			"did not fill in code properly");
}

@Test
public void test_ConstructorLjava_lang_String() {
	assertEquals(SWT.ERROR_UNSPECIFIED, new SWTError("An uninteresting message").code, "did not fill in code properly");
}

@Test
public void test_getMessage() {
	assertTrue(new SWTError(SWT.ERROR_CANNOT_BE_ZERO, "An interesting message").getMessage()
			.contains("An interesting message"), "did not include creation string in result");
}

@Test
public void test_printStackTrace() {

	// test default SWTError

	ByteArrayOutputStream out = new ByteArrayOutputStream();
	System.setErr(new PrintStream(out));
	SWTError error = new SWTError();
	error.printStackTrace();
	assertTrue(out.size() > 0);
	assertTrue(new String(out.toByteArray()).contains("test_printStackTrace"));

	// test SWTError with code

	out = new ByteArrayOutputStream();
	System.setErr(new PrintStream(out));
	error = new SWTError(SWT.ERROR_INVALID_ARGUMENT);
	error.printStackTrace();
	assertTrue(out.size() > 0);
	assertTrue(new String(out.toByteArray()).contains("test_printStackTrace"));
}
}

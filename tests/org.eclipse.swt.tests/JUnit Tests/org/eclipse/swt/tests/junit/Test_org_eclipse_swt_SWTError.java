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
	SWTError error1 = new SWTError();
	String stderr = SwtTestUtil.runWithCapturedStderr(() -> {
		error1.printStackTrace();
	});
	assertTrue(stderr.contains("test_printStackTrace"));

	// test SWTError with code
	SWTError error2 = new SWTError(SWT.ERROR_INVALID_ARGUMENT);
	stderr = SwtTestUtil.runWithCapturedStderr(() -> {
		error2.printStackTrace();
	});
	assertTrue(stderr.contains("test_printStackTrace"));
}
}

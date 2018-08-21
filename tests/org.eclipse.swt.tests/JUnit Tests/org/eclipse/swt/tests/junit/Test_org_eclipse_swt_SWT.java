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
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.SWT
 *
 * @see org.eclipse.swt.SWT
 */
public class Test_org_eclipse_swt_SWT {

@Test
public void test_Constructor() {
	// Do nothing. Class SWT is not intended to be subclassed.
}

@Test
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

@Test
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

@Test
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

@Test
public void test_getPlatform() {
	// Can't test the list of platforms, since this may change,
	// so just test to see it returns something.
	assertTrue ("returned null platform name", SWT.getPlatform() != null);
}

@Test
public void test_getVersion() {
	// Test that the version number which is returned is reasonable.
	int ver = SWT.getVersion();
	assertTrue ("unreasonable value returned", ver > 0 && ver < 1000000);
	System.out.println("SWT.getVersion(): " + ver);
}
}

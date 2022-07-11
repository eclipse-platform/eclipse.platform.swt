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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.junit.Test;

import java.nio.file.Paths;
import java.security.CodeSigner;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

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
	assertEquals (
		"invalid key did not return as itself",
		"_NOT_FOUND_IN_PROPERTIES_", SWT.getMessage("_NOT_FOUND_IN_PROPERTIES_"));

}

private String pathFromClass(Class<?> classValue) {
	String filePrefix = SwtTestUtil.isWindows ? "file:/" : "file:";

	String url = classValue.getProtectionDomain().getCodeSource().getLocation().toString();
	assertTrue(url.startsWith(filePrefix));

	String urlPath = url.substring(filePrefix.length());
	String path = Paths.get(urlPath).toAbsolutePath().toString();

	// On Windows, use / for consistency
	return path.replace('\\', '/');
}

private List<String> signersFromClass(Class<?> classValue) {
	List<String> result = new ArrayList<>();

	CodeSigner[] signers = classValue.getProtectionDomain().getCodeSource().getCodeSigners();
	if (signers == null) {
		return result;
	}

	for (CodeSigner signer : signers) {
		Certificate cert = signer.getSignerCertPath().getCertificates().get(0);
		if (!(cert instanceof X509Certificate)) {
			continue;
		}

		X509Certificate x509cert = (X509Certificate)cert;
		result.add(x509cert.getSubjectX500Principal().getName());
	}

	return result;
}

/**
 * Ensure that SWT being tested was built together with the tests. This
 * is a test designed for build scripts, such as the one that checks
 * GitHub Pull Requests.
 */
@Test
public void test_isLocal() {
	// If you change default to NO, make sure that this test runs on GitHub
	if (Boolean.getBoolean("org.eclipse.swt.tests.junit.disable.test_isLocal")) {
		return;
	}

	String swtPath = pathFromClass(SWT.class);
	String tstPath = pathFromClass(Test_org_eclipse_swt_SWT.class);
	List<String> swtSigners = signersFromClass(SWT.class);

	// If SWT is signed by Eclipse, that's a good sign that it wasn't built locally
	for (String signer : swtSigners) {
		if (signer.toLowerCase().contains("cn=eclipse.org")) {
			fail("Tested SWT is signed by Eclipse: (SWT='" + swtPath + "' signer='" + signer + "')");
		}
	}

	// It's hard to devise a condition that would hold for all possible
	// locations of compiled classes. For example, my own project uses
	// output directories different from what Eclipse does by default.

	// SWT shouldn't be published in maven. Note that SWT's local build
	// may still be cached in maven. We observed the following paths:
	// GitHub, bad:   $HOME/.m2/repository/p2/osgi/bundle/org.eclipse.swt.gtk.linux.x86_64/3.121.0.v20220701-1002/org.eclipse.swt.gtk.linux.x86_64-3.121.0.v20220701-1002.jar
	// Jenkins, good: $HOME/.m2/repository/org/eclipse/swt/org.eclipse.swt.gtk.linux.x86_64/3.121.0-SNAPSHOT/org.eclipse.swt.gtk.linux.x86_64-3.121.0-SNAPSHOT.jar
	// The difference here is '/p2/osgi'.
	if (swtPath.contains("/.m2/repository/p2/osgi/")) {
		fail("Tested SWT path is on maven OSGI: (SWT='" + swtPath + "' Test='" + tstPath + "'");
	}
}

@Test
public void test_getPlatform() {
	// Can't test the list of platforms, since this may change,
	// so just test to see it returns something.
	assertNotNull ("returned null platform name", SWT.getPlatform());
}

@Test
public void test_getVersion() {
	// Test that the version number which is returned is reasonable.
	int ver = SWT.getVersion();
	assertTrue ("unreasonable value returned", ver > 0 && ver < 1000000);
	System.out.println("SWT.getVersion(): " + ver);
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;

public class SwtTestUtil {
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

	// used to specify verbose mode, if true unimplemented warning messages will
	// be written to System.out
	public static boolean verbose = false;

	// allow specific image formats to be tested
	public static String[] imageFormats = new String[] {"bmp", "jpg", "gif", "png"};
	public static String[] imageFilenames = new String[] {"folder", "folderOpen", "target"};
	public static String[] invalidImageFilenames = new String[] {"corrupt", "corruptBadBitDepth.png"};
	public static String[] transparentImageFilenames = new String[] {"transparent.png"};

	// specify reparentable platforms
	public static String[] reparentablePlatforms = new String[] {"win32", "gtk", "cocoa"};

	public static final String testFontName;
	// isWindows refers to windows platform, i.e. win32 windowing system; see also isWindowsOS
	public final static boolean isWindows = SWT.getPlatform().startsWith("win32");
	public final static boolean isCocoa = SWT.getPlatform().startsWith("cocoa");
	public final static boolean isGTK = SWT.getPlatform().equals("gtk");
	public final static boolean isWindowsOS = System.getProperty("os.name").startsWith("Windows");
	public final static boolean isLinux = System.getProperty("os.name").equals("Linux");
	public final static boolean isAIX = System.getProperty("os.name").equals("AIX");


	/** Useful if you want some tests not to run on Jenkins with user "genie.platform" */
	public final static boolean isRunningOnContinousIntegration = isGTK && ("genie.platform".equalsIgnoreCase(System.getProperty("user.name")));

	public final static boolean isX11 = isGTK
			&& "x11".equals(System.getProperty("org.eclipse.swt.internal.gdk.backend"));

	static {
		testFontName = "Helvetica";
	}

public static void assertSWTProblem(String message, int expectedCode, Throwable actualThrowable) {
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
			if (actualThrowable.getMessage().length() > expectedThrowable.getMessage().length()) {
				assertTrue(message, actualThrowable.getMessage().startsWith(expectedThrowable.getMessage()));
			}
			else {
				assertEquals(message, expectedThrowable.getMessage(), actualThrowable.getMessage());
			}
		}
	}
}

protected static boolean isReparentablePlatform() {
	String platform = SWT.getPlatform();
	for (int i=0; i<reparentablePlatforms.length; i++) {
		if (reparentablePlatforms[i].equals(platform)) return true;
	}
	return false;
}

/*
 * Checks if the GTK version is 2 or 3.
 * Only works if OS.java has been initialized.
 */
public static boolean isGTK3 () {
	String gtkVersion = System.getProperty("org.eclipse.swt.internal.gtk.version");
	if (gtkVersion != null) {
		return Integer.parseInt(gtkVersion.substring(0,1)) == 3 ? true : false;
	} else if (isGTK && gtkVersion == null) {
		throw new IllegalStateException ("OS.java has not yet been initialized");
	}
	return false;
}

public static boolean isBidi() {
	return true;
}
}

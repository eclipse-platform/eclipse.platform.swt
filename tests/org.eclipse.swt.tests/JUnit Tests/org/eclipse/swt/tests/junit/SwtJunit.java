package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * Platform-specific constants used in SWT test cases.
 */public class SwtJunit {

	public static final String testFontName;
	public final static boolean isLinux = System.getProperty("os.name").equals("Linux");
	public final static boolean isAIX = System.getProperty("os.name").equals("AIX");
	public final static boolean isMotif = isLinux || isAIX;
	public final static boolean isWindows = !isMotif;

	static {
		if (isMotif) {
			testFontName = "misc-fixed";
		}
		else {
			testFontName = "Helvetica";
		}
	}
}

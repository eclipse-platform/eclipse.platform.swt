package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
			testFontName = "adobe-courier";//"misc-fixed";
		}
		else {
			testFontName = "Helvetica";
		}
	}
}

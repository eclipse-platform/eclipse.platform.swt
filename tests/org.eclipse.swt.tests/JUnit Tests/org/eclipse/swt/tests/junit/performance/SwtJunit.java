/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.performance;


/**
 * Platform-specific constants used in SWT test cases.
 */
public class SwtJunit {

	public static final String testFontName;
	public final static boolean isWindows = System.getProperty("os.name").startsWith("Win");
	public final static boolean isLinux = System.getProperty("os.name").equals("Linux");
	public final static boolean isAIX = System.getProperty("os.name").equals("AIX");
	public final static boolean isSolaris = System.getProperty("os.name").equals("Solaris") || System.getProperty("os.name").equals("SunOS");
	public final static boolean isHPUX = System.getProperty("os.name").equals("HP-UX");
	public final static boolean isCarbon = System.getProperty("os.name").startsWith("Mac");

	public final static boolean isMotif = /*!isGTK &&*/ isLinux || isAIX || isSolaris || isHPUX;
	
	static {
		if (isMotif) {
			testFontName = "misc-fixed";
		}
		else {
			testFontName = "Helvetica";
		}
	}
}

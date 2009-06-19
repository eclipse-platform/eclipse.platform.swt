/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import org.eclipse.swt.SWT;


/**
 * Platform-specific constants used in SWT test cases.
 */
public class SwtJunit {

	public static final String testFontName;
	public final static boolean isWindows = SWT.getPlatform().startsWith("win32");
	public final static boolean isCarbon = SWT.getPlatform().startsWith("carbon");
	public final static boolean isCocoa = SWT.getPlatform().startsWith("cocoa");
	public final static boolean isMotif = SWT.getPlatform().equals("motif");
	public final static boolean isGTK = SWT.getPlatform().equals("gtk");
	public final static boolean isPhoton = SWT.getPlatform().equals("photon");
	public final static boolean isLinux = System.getProperty("os.name").equals("Linux");
	public final static boolean isAIX = System.getProperty("os.name").equals("AIX");
	public final static boolean isSolaris = System.getProperty("os.name").equals("Solaris") || System.getProperty("os.name").equals("SunOS");
	public final static boolean isHPUX = System.getProperty("os.name").equals("HP-UX");
	public final static boolean isWPF = SWT.getPlatform().startsWith("wpf");
	
	static {
		if (isMotif) {
			testFontName = "misc-fixed";
		}
		else {
			testFontName = "Helvetica";
		}
	}
}

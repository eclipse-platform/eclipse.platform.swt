/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl;


public class Library {

/**
 * Returns the platform name.
 *
 * @return the platform name of the currently running SWT
 */
static String getPlatform () {
	String [] names = new String [] {"motif", "gtk", "win32", "photon", "carbon"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	for (int i = 0; i < names.length; i++) {
		try {
			Class.forName("org.eclipse.swt.internal."+names[i]+".OS"); //$NON-NLS-1$ //$NON-NLS-2$
			return names[i];
		} catch (ClassNotFoundException e) {
		}
	}
	return "unknown"; //$NON-NLS-1$
}

/**
 * Loads the shared library that matches the version of the
 * Java code which is currently running.  SWT shared libraries
 * follow an encoding scheme where the major, minor and revision
 * numbers are embedded in the library name and this along with
 * <code>name</code> is used to load the library.  If this fails,
 * <code>name</code> is used in another attempt to load the library,
 * this time ignoring the SWT version encoding scheme.
 *
 * @param name the name of the library to load
 */
public static void loadLibrary (String name) {
	/*
     * Include platform name to support different windowing systems
     * on same operating system.
	 */
	String platform = getPlatform ();
	
	try {
		String newName = name + "-" + platform; //$NON-NLS-1$ //$NON-NLS-2$
		System.loadLibrary (newName);
		return;
	} catch (UnsatisfiedLinkError e1) {		
		throw e1;
	}
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.io.*;
import java.net.*;
import java.util.jar.*;

public class Platform {

	public static final String PLATFORM = "cocoa"; //$NON-NLS-1$
	public static final Lock lock = new Lock ();

public static boolean IsLoadable () {
	URL url = Platform.class.getClassLoader ().getResource ("org/eclipse/swt/SWT.class"); //$NON-NLS-1$
	if (!url.getProtocol ().equals ("jar")) { //$NON-NLS-1$
		/* SWT is presumably running in a development environment */
		return true;
	}

	try {
		url = new URL (url.getPath ());
	} catch (MalformedURLException e) {
		/* should never happen since url's initial path value must be valid */
	}
	String path = url.getPath ();
	int index = path.indexOf ('!');
	File file = new File (path.substring (0, index));

	try {
		JarFile jar = new JarFile (file);
		Manifest manifest = jar.getManifest ();
		Attributes attributes = manifest.getMainAttributes ();

		/*
		* Cocoa has a special case since SWT's 32-bit library contains natives for both the
		* x86 and PPC architectures.
		*/
		if (!Library.os ().equals (attributes.getValue ("SWT-OS"))) return false; //$NON-NLS-1$
		String libraryArch = Library.arch ();
		String manifestArch = attributes.getValue ("SWT-Arch"); //$NON-NLS-1$
		return libraryArch.equals (manifestArch) || (manifestArch.equals ("x86") && libraryArch.equals ("ppc")); //$NON-NLS-1$ //$NON-NLS-2$ 
	} catch (IOException e) {
		/* should never happen for a valid SWT jar with the expected manifest values */
	}

	return false;	
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.io.*;
import java.net.*;
import java.util.jar.*;

public class Platform {
	public static final String PLATFORM = "gtk"; //$NON-NLS-1$
	public static final Lock lock = new Lock();

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
		return Library.arch ().equals (attributes.getValue ("SWT-Arch")) && //$NON-NLS-1$
				Library.os ().equals (attributes.getValue ("SWT-OS")); //$NON-NLS-1$
	} catch (IOException e) {
		/* should never happen for a valid SWT jar with the expected manifest values */
	}

	return false;	
}

}

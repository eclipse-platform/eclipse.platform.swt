/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.SWT;

class BrowserFactory {

	static boolean mozillaLibsLoaded;

WebBrowser createWebBrowser (int style) {
	boolean webkitInstalled = WebKit.IsInstalled ();
	if ((style & SWT.MOZILLA) != 0 || (!webkitInstalled && (style & SWT.WEBKIT) == 0)) {
		mozillaLibsLoaded = true;
		return new Mozilla ();
	}
	if (!webkitInstalled) return null;

	/*
	* A crash can occur if XULRunner-1.9.2.x is loaded into a process where WebKit has
	* already been loaded, as a result of conflicting versions of the sqlite3 library.
	* Loading these native renderers in the reverse order does not cause a problem.  The
	* crash workaround is to ensure that Mozilla's libraries (if available) are always
	* loaded before WebKit's.
	*/
	if (!mozillaLibsLoaded) {
		mozillaLibsLoaded = true;
		Mozilla.LoadLibraries ();
	}
	return new WebKit ();
}

}

/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.lang.reflect.*;

import org.eclipse.swt.*;

class BrowserFactory {

private Class<?> chromiumClass;

WebBrowser createWebBrowser (int style) {
	// This function can't throw, otherwise the Browser will be left in inconsistent state.
	WebBrowser browser = null;
	if ((style & SWT.CHROMIUM) != 0) {
		browser = createChromium();
		if (browser != null) return browser;
	}
	if ((style & SWT.EDGE) != 0) {
		try {
			return new Edge();
		} catch (SWTError e) {
			System.err.println(e);
		}
	}
	return new IE ();
}

private WebBrowser createChromium() {
	if (chromiumClass == null) {
		try {
			chromiumClass = Class.forName ("org.eclipse.swt.browser.ChromiumImpl"); //$NON-NLS-1$
			return (WebBrowser) chromiumClass.getConstructor().newInstance(chromiumClass);
		} catch (ClassNotFoundException e) {
			/* chromium fragments missing */
			System.err.println ("SWT.CHROMIUM style was used but chromium.swt fragment/jar is missing from classpath."); //$NON-NLS-1$
		} catch (NoClassDefFoundError | InstantiationException | IllegalAccessException  e) {
			/* second attempt, do not print */
		} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			System.err.println ("SWT.CHROMIUM style was used but contructor not found:"+ e.getMessage()); //$NON-NLS-1$
		} catch (UnsatisfiedLinkError e) {
			System.err.println ("SWT.CHROMIUM style was used but chromium.swt " + SWT.getPlatform() +  " (or CEF binaries) fragment/jar is missing."); //$NON-NLS-1$
		} catch (SWTError e) {
			// a more specific error determined by the implementation.
			System.err.println (e.getMessage()); //$NON-NLS-1$
		}
	}
	return null;
}
}

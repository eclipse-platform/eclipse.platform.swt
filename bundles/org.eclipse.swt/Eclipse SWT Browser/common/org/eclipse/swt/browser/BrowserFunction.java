/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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

/**
 * @since 3.5
 */
public class BrowserFunction {
	Browser browser;
	String name;
	String functionString;
	int index;

/**
 *
 */
public BrowserFunction (Browser browser, String name) {
	super ();
	if (browser == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (name == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (browser.isDisposed ()) SWT.error (SWT.ERROR_WIDGET_DISPOSED);
	this.browser = browser;
	this.name = name;
	browser.webBrowser.addFunction (this);
}

/**
*
*/
public void dispose () {
	dispose (true);
}

void dispose (boolean remove) {
	if (index < 0) return;
	if (remove) browser.webBrowser.removeFunction (this);
	browser = null;
	name = functionString = null;
	index = -1;
}

/**
*
*/
public Object function (Object[] arguments) {
	if (index < 0) SWT.error (SWT.ERROR_FUNCTION_DISPOSED);
	browser.checkWidget ();
	return null;
}

/**
*
*/
public Browser getBrowser () {
	if (index < 0) SWT.error (SWT.ERROR_FUNCTION_DISPOSED);
	browser.checkWidget ();
	return browser;
}

/**
*
*/
public String getName () {
	if (index < 0) SWT.error (SWT.ERROR_FUNCTION_DISPOSED);
	browser.checkWidget ();
	return name;
}

/**
*
*/
public boolean isDisposed () {
	return index < 0;
}
}

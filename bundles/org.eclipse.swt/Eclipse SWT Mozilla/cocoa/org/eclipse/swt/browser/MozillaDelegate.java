/*******************************************************************************
 * Copyright (c) 2003, 2009 IBM Corporation and others.
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
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.widgets.*;

class MozillaDelegate {
	Browser browser;
	Listener listener;
	boolean hasFocus;
	
MozillaDelegate (Browser browser) {
	super ();
	this.browser = browser;
}

static Browser findBrowser (int /*long*/ handle) {
	Display display = Display.getCurrent ();
	return (Browser)display.findWidget (handle);
}

static char[] mbcsToWcs (String codePage, byte [] buffer) {
//	int encoding = OS.CFStringGetSystemEncoding ();
//	int cfstring = OS.CFStringCreateWithBytes (OS.kCFAllocatorDefault, buffer, buffer.length, encoding, false);
//	char[] chars = null;
//	if (cfstring != 0) {
//		int length = OS.CFStringGetLength (cfstring);
//		chars = new char [length];
//		if (length != 0) {
//			CFRange range = new CFRange ();
//			range.length = length;
//			OS.CFStringGetCharacters (cfstring, range, chars);
//		}
//		OS.CFRelease (cfstring);
//	}
//	return chars;
	// TODO implement mbcsToWcs
	return new String(buffer).toCharArray();
}

static byte[] wcsToMbcs (String codePage, String string, boolean terminate) {
//	char[] chars = new char [string.length()];
//	string.getChars (0, chars.length, chars, 0);
//	int cfstring = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, chars, chars.length);
//	byte[] buffer = null;
//	if (cfstring != 0) {
//		CFRange range = new CFRange ();
//		range.length = chars.length;
//		int encoding = OS.CFStringGetSystemEncoding ();
//		int[] size = new int[1];
//		int numChars = OS.CFStringGetBytes (cfstring, range, encoding, (byte)'?', true, null, 0, size);
//		buffer = new byte [size[0] + (terminate ? 1 : 0)];
//		if (numChars != 0) {
//			numChars = OS.CFStringGetBytes (cfstring, range, encoding, (byte)'?', true, buffer, size[0], size);
//		}
//		OS.CFRelease (cfstring);
//	}
//	return buffer;
	// TODO implement wcsToMbcs
	if (terminate) string += "\0";
	return string.getBytes();
}

void addWindowSubclass () {
}

int createBaseWindow (nsIBaseWindow baseWindow) {
	/*
	* Feature of Mozilla on OSX.  Mozilla replaces the OSX application menu whenever
	* a browser's base window is created.  The workaround is to restore the previous
	* menu after creating the base window. 
	*/
	NSApplication application = NSApplication.sharedApplication ();
	NSMenu mainMenu = application.mainMenu ();
	if (mainMenu != null) mainMenu.retain ();
	int rc = baseWindow.Create ();
	application.setMainMenu (mainMenu);
	if (mainMenu != null) mainMenu.release ();
	((Mozilla)browser.webBrowser).Activate ();
	return rc;
}

int /*long*/ getHandle () {
	return browser.view.id;
}

String getJSLibraryName () {
	return "libmozjs.dylib"; //$NON-NLS-1$
}

String getLibraryName () {
	return "libxpcom.dylib"; //$NON-NLS-1$
}

String getProfilePath () {
	String baseDir = System.getProperty ("user.home"); //$NON-NLS-1$
	return baseDir + Mozilla.SEPARATOR_OS + ".mozilla" + Mozilla.SEPARATOR_OS + "eclipse"; //$NON-NLS-1$ //$NON-NLS-2$
}

String getSWTInitLibraryName () {
	return "swt-xulrunner"; //$NON-NLS-1$
}

void handleFocus () {
	if (hasFocus) return;
	hasFocus = true;
	((Mozilla)browser.webBrowser).Activate ();
	browser.setFocus ();
	listener = new Listener () {
		public void handleEvent (Event event) {
			if (event.widget == browser) return;
			((Mozilla)browser.webBrowser).Deactivate ();
			hasFocus = false;
			browser.getDisplay ().removeFilter (SWT.FocusIn, this);
			browser.getShell ().removeListener (SWT.Deactivate, this);
			listener = null;
		}
	
	};
	browser.getDisplay ().addFilter (SWT.FocusIn, listener);
	browser.getShell ().addListener (SWT.Deactivate, listener);
}

void handleMouseDown () {
}

boolean hookEnterExit () {
	return true;
}

void init () {
}

boolean needsSpinup () {
	return false;
}

void onDispose (int /*long*/ embedHandle) {
	if (listener != null) {
		browser.getDisplay ().removeFilter (SWT.FocusIn, listener);
		browser.getShell ().removeListener (SWT.Deactivate, listener);
		listener = null;
	}
	browser = null;
}

void removeWindowSubclass () {
}

void setSize (int /*long*/ embedHandle, int width, int height) {
	// TODO
}

}

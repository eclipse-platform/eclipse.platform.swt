/*******************************************************************************
 * Copyright (c) 2003, 2011 IBM Corporation and others.
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
	Shell eventShell;
	Listener listener;
	boolean hasFocus;

	static final String SET_MOZILLA_COUNT = "org.eclipse.swt.internal.setMozillaCount"; //$NON-NLS-1$

MozillaDelegate (Browser browser) {
	super ();
	this.browser = browser;
}

static Browser findBrowser (int /*long*/ handle) {
	Display display = Display.getCurrent ();
	return (Browser)display.findWidget (handle);
}

static String getCacheParentPath () {
	if (OS.VERSION >= 0x1060) {
		NSArray array = NSFileManager.defaultManager ().URLsForDirectory (OS.NSCachesDirectory, OS.NSUserDomainMask);
		if (array.count () > 0) {
			NSURL url = new NSURL (array.objectAtIndex (0));
			return url.path ().getString () + Mozilla.SEPARATOR_OS + "eclipse"; //$NON-NLS-1$
		}
	}
	String baseDir = System.getProperty ("user.home"); //$NON-NLS-1$
	return baseDir + "/Library/Caches/eclipse"; //$NON-NLS-1$
}

static String getJSLibraryName () {
	return "libxpcom.dylib"; //$NON-NLS-1$
}

static String getJSLibraryName_Pre4 () {
	return "libmozjs.dylib"; //$NON-NLS-1$
}

static String getLibraryName () {
	return "libxpcom.dylib"; //$NON-NLS-1$
}

static String getProfilePath () {
	String baseDir = System.getProperty ("user.home"); //$NON-NLS-1$
	return baseDir + Mozilla.SEPARATOR_OS + ".mozilla" + Mozilla.SEPARATOR_OS + "eclipse"; //$NON-NLS-1$ //$NON-NLS-2$
}

static String getSWTInitLibraryName () {
	return "swt-xulrunner"; //$NON-NLS-1$
}

static void loadAdditionalLibraries (String mozillaPath) {
	// workaround for https://bugzilla.mozilla.org/show_bug.cgi?id=727616
	String utilsPath = mozillaPath + Mozilla.SEPARATOR_OS + "libmozutils.dylib"; //$NON-NLS-1$
	byte[] bytes = MozillaDelegate.wcsToMbcs (null, utilsPath, true);
	OS.NSAddImage (bytes, OS.NSADDIMAGE_OPTION_RETURN_ON_ERROR | OS.NSADDIMAGE_OPTION_MATCH_FILENAME_BY_INSTALLNAME);
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

static boolean needsSpinup () {
	return false;
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
	browser.getDisplay ().setData (SET_MOZILLA_COUNT, new Integer (Mozilla.BrowserCount));

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

int /*long*/ getSiteWindow () {
	return browser.view.id;
}

void handleFocus () {
	if (hasFocus) return;
	hasFocus = true;
	((Mozilla)browser.webBrowser).Activate ();
	browser.setFocus ();
	listener = new Listener () {
		public void handleEvent (Event event) {
			if (event.widget == browser) return;
			if (event.type != SWT.Dispose) {
				((Mozilla)browser.webBrowser).Deactivate ();
				hasFocus = false;
			}
			eventShell.getDisplay ().removeFilter (SWT.FocusIn, this);
			eventShell.removeListener (SWT.Deactivate, this);
			eventShell.removeListener (SWT.Dispose, this);
			eventShell = null;
			listener = null;
		}
	
	};
	eventShell = browser.getShell ();
	eventShell.getDisplay ().addFilter (SWT.FocusIn, listener);
	eventShell.addListener (SWT.Deactivate, listener);
	eventShell.addListener (SWT.Dispose, listener);
}

void handleMouseDown () {
}

boolean hookEnterExit () {
	return true;
}

void init () {
}

void onDispose (int /*long*/ embedHandle) {
	if (listener != null) {
		eventShell.getDisplay ().removeFilter (SWT.FocusIn, listener);
		eventShell.removeListener (SWT.Deactivate, listener);
		eventShell.removeListener (SWT.Dispose, listener);
		eventShell = null;
		listener = null;
	}
	browser.getDisplay ().setData (SET_MOZILLA_COUNT, new Integer (Mozilla.BrowserCount));
	browser = null;
}

void removeWindowSubclass () {
}

boolean sendTraverse () {
	return true;
}

void setSize (int /*long*/ embedHandle, int width, int height) {
	// TODO
}

}

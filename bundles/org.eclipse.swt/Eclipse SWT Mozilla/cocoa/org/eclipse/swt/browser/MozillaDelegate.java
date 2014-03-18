/*******************************************************************************
 * Copyright (c) 2003, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;


import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.widgets.*;

class MozillaDelegate {
	Browser browser;
	Shell eventShell;
	Listener listener;
	boolean hasFocus;

	static Boolean IsXULRunner24;
	static final String LIB_XPCOM = "libxpcom.dylib"; //$NON-NLS-1$
	static final String LIB_XUL = "XUL"; //$NON-NLS-1$
	static final String MOZILLA_RUNNING = "org.eclipse.swt.internal.mozillaRunning"; //$NON-NLS-1$

MozillaDelegate (Browser browser) {
	super ();
	this.browser = browser;
}

static Browser findBrowser (long /*int*/ handle) {
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

static String[] getJSLibraryNames () {
	return new String[] {"libxpcom.dylib"}; //$NON-NLS-1$
}

static String getJSLibraryName_Pre10 () {
	return "libmozjs.dylib"; //$NON-NLS-1$
}

static String getLibraryName (String mozillaPath) {
	/*
	 * The name of the Gecko library to glue to changed between the XULRunner 10 and
	 * 24 releases.  However it's not possible to programmatically know the version
	 * of a XULRunner that's being used before it has been glued.  To determine the
	 * appropriate Gecko library name to return, look for the presence of an "xpcom"
	 * library in the mozilla path, which is present in all supported XULRunner releases
	 * prior to XULRunner 24.  If this library is there then return it, and if it's not
	 * there then assume that XULRunner 24 is being used and return the new library name
	 * instead ("XUL").
	 */
	if (IsXULRunner24 == null) { /* IsXULRunner24 not yet initialized */
		IsXULRunner24 = new File (mozillaPath, LIB_XPCOM).exists () ? Boolean.FALSE : Boolean.TRUE;
	}
	return IsXULRunner24.booleanValue () ? LIB_XUL : LIB_XPCOM;
}

static String getProfilePath () {
	String baseDir = System.getProperty ("user.home"); //$NON-NLS-1$
	return baseDir + Mozilla.SEPARATOR_OS + ".mozilla" + Mozilla.SEPARATOR_OS + "eclipse"; //$NON-NLS-1$ //$NON-NLS-2$
}

static String getSWTInitLibraryName () {
	return "swt-xulrunner"; //$NON-NLS-1$
}

static void loadAdditionalLibraries (String mozillaPath, boolean isGlued) {
	// workaround for https://bugzilla.mozilla.org/show_bug.cgi?id=727616
	if (!isGlued) {
		String utilsPath = mozillaPath + Mozilla.SEPARATOR_OS + "libmozutils.dylib"; //$NON-NLS-1$
		byte[] bytes = MozillaDelegate.wcsToMbcs (null, utilsPath, true);
		OS.NSAddImage (bytes, OS.NSADDIMAGE_OPTION_RETURN_ON_ERROR | OS.NSADDIMAGE_OPTION_MATCH_FILENAME_BY_INSTALLNAME);
	}
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
	browser.getDisplay ().setData (MOZILLA_RUNNING, Boolean.TRUE);

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

long /*int*/ getHandle () {
	return browser.view.id;
}

Point getNativeSize (int width, int height) {
	if (IsXULRunner24.booleanValue ()) {
		NSScreen screen = browser.view.window ().screen ();
		double /*float*/ scaling = screen.backingScaleFactor ();
		return new Point ((int)(width * scaling), (int)(height * scaling));
	}
	return new Point (width, height);
}

long /*int*/ getSiteWindow () {
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
	Mozilla mozilla = (Mozilla)browser.webBrowser;
	if (!mozilla.isActive) mozilla.Activate ();
}

boolean hookEnterExit () {
	return true;
}

void init () {
}

void onDispose (long /*int*/ embedHandle) {
	if (listener != null) {
		eventShell.getDisplay ().removeFilter (SWT.FocusIn, listener);
		eventShell.removeListener (SWT.Deactivate, listener);
		eventShell.removeListener (SWT.Dispose, listener);
		eventShell = null;
		listener = null;
	}
	browser = null;
}

void removeWindowSubclass () {
}

boolean sendTraverse () {
	return true;
}

void setSize (long /*int*/ embedHandle, int width, int height) {
	// TODO
}

}

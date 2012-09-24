/*******************************************************************************
 * Copyright (c) 2003, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.widgets.*;

class MozillaDelegate {
	Browser browser;
	int embedHandle;
	Listener listener;
	boolean hasFocus;
	static Callback Callback3;
	static Hashtable handles = new Hashtable ();

MozillaDelegate (Browser browser) {
	super ();
	this.browser = browser;
}

static Browser findBrowser (int handle) {
	LONG value = (LONG)handles.get (new LONG (handle));
	if (value != null) {
		Display display = Display.getCurrent ();
		return (Browser)display.findWidget (value.value);
	}
	return null;
}

static String getCacheParentPath () {
	return getProfilePath ();
}

static String getLibraryName () {
	return "libxpcom.dylib"; //$NON-NLS-1$
}

static String getJSLibraryName () {
	return "libxpcom.dylib"; //$NON-NLS-1$
}

static String getJSLibraryName_Pre4 () {
	return "libmozjs.dylib"; //$NON-NLS-1$
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
//	String utilsPath = mozillaPath + Mozilla.SEPARATOR_OS + "libmozutils.dylib"; //$NON-NLS-1$
//	byte[] bytes = MozillaDelegate.wcsToMbcs (null, utilsPath, true);
//	OS.NSAddImage (bytes, OS.NSADDIMAGE_OPTION_RETURN_ON_ERROR | OS.NSADDIMAGE_OPTION_MATCH_FILENAME_BY_INSTALLNAME);
}

static char[] mbcsToWcs (String codePage, byte [] buffer) {
	int encoding = OS.CFStringGetSystemEncoding ();
	int cfstring = OS.CFStringCreateWithBytes (OS.kCFAllocatorDefault, buffer, buffer.length, encoding, false);
	char[] chars = null;
	if (cfstring != 0) {
		int length = OS.CFStringGetLength (cfstring);
		chars = new char [length];
		if (length != 0) {
			CFRange range = new CFRange ();
			range.length = length;
			OS.CFStringGetCharacters (cfstring, range, chars);
		}
		OS.CFRelease (cfstring);
	}
	return chars;
}

static boolean needsSpinup () {
	return false;
}

static byte[] wcsToMbcs (String codePage, String string, boolean terminate) {
	char[] chars = new char [string.length()];
	string.getChars (0, chars.length, chars, 0);
	int cfstring = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, chars, chars.length);
	byte[] buffer = null;
	if (cfstring != 0) {
		CFRange range = new CFRange ();
		range.length = chars.length;
		int encoding = OS.CFStringGetSystemEncoding ();
		int[] size = new int[1];
		int numChars = OS.CFStringGetBytes (cfstring, range, encoding, (byte)'?', true, null, 0, size);
		buffer = new byte [size[0] + (terminate ? 1 : 0)];
		if (numChars != 0) {
			numChars = OS.CFStringGetBytes (cfstring, range, encoding, (byte)'?', true, buffer, size[0], size);
		}
		OS.CFRelease (cfstring);
	}
	return buffer;
}

static int eventProc3 (int nextHandler, int theEvent, int userData) {
	Widget widget = Display.getCurrent ().findWidget (userData);
	if (widget instanceof Browser) {
		Browser browser = (Browser) widget;
		switch (OS.GetEventClass (theEvent)) {
			case OS.kEventClassMouse:
				browser.getShell ().forceActive ();
				((Mozilla)browser.webBrowser).Activate ();
				break;
			case OS.kEventClassKeyboard:
				/*
				* Bug in Carbon.  OSX crashes if a HICocoaView is disposed during a key event,
				* presumably as a result of attempting to use it after its refcount has reached
				* 0.  The workaround is to temporarily add an extra ref to the view while the
				* DOM listener is handling the event, in case the Browser gets disposed in a
				* callback.
				*/
				int result = OS.noErr;
				int handle = browser.handle;
				OS.CFRetain (handle);

				/*
				* Pressing the OSX shortcut to put focus into the menu bar does not work in
				* embedded mozilla.  If this shortcut is not handled here then it falls through
				* all of the key handlers for some reason.  The workaround is to detect this
				* shortcut here and put focus into the menu bar.
				*/
				int [] modifiers = new int [1];
				OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, modifiers.length * 4, null, modifiers);
				int [] keyCode = new int [1];
				OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
				if (keyCode [0] == 120 /* F2 */ && (modifiers[0] & (OS.controlKey | OS.cmdKey | OS.optionKey)) == OS.controlKey) {
					int[] event = new int[1];
					OS.CreateEvent (0, OS.kEventClassApplication, OS.kEventAppFocusMenuBar, 0.0, 0, event);
					if (event [0] != 0) {
						OS.SetEventParameter (event [0], OS.kEventParamKeyModifiers, OS.typeUInt32, 4, modifiers);
						result = OS.SendEventToEventTarget (event [0], OS.GetApplicationEventTarget ());
						OS.ReleaseEvent (event [0]);
					}
				} else {
					result = OS.CallNextEventHandler (nextHandler, theEvent);
				}

				OS.CFRelease (handle);
				return result;
		}
	}
	return OS.eventNotHandledErr;
}

void addWindowSubclass () {
}

int createBaseWindow (nsIBaseWindow baseWindow) {
	/*
	* Feature of Mozilla on OSX.  Mozilla replaces the OSX application menu whenever
	* a browser's base window is created.  The workaround is to restore the previous
	* menu after creating the base window. 
	*/
	int application = Cocoa.objc_msgSend (Cocoa.C_NSApplication, Cocoa.S_sharedApplication);
	int mainMenu = Cocoa.objc_msgSend (application, Cocoa.S_mainMenu);
	if (mainMenu != 0) {
		Cocoa.objc_msgSend (mainMenu, Cocoa.S_retain);
	}
	int rc = baseWindow.Create ();
	if (mainMenu != 0) {
		Cocoa.objc_msgSend (application, Cocoa.S_setMainMenu, mainMenu);
		Cocoa.objc_msgSend (mainMenu, Cocoa.S_release);
	}
	return rc;
}

int getHandle () {
    embedHandle = Cocoa.objc_msgSend (Cocoa.C_NSImageView, Cocoa.S_alloc);
	if (embedHandle == 0) {
		browser.dispose ();
		SWT.error (SWT.ERROR_NO_HANDLES);
	}
	NSRect r = new NSRect ();
	embedHandle = Cocoa.objc_msgSend (embedHandle, Cocoa.S_initWithFrame, r);
	int rc;
	int[] outControl = new int[1];
	rc = Cocoa.HICocoaViewCreate (embedHandle, 0, outControl); /* OSX >= 10.5 */
	if (rc == OS.noErr && outControl[0] != 0) {
		Cocoa.objc_msgSend (embedHandle, Cocoa.S_release);
	} else {
		/* will succeed on OSX 10.4 with an updated vm containing HIJavaViewCreateWithCocoaView */
		try {
			System.loadLibrary ("frameembedding"); //$NON-NLS-1$
		} catch (UnsatisfiedLinkError e) {}
		rc = Cocoa.HIJavaViewCreateWithCocoaView (outControl, embedHandle);
		if (!(rc == OS.noErr && outControl[0] != 0)) {
			browser.dispose ();
			SWT.error (SWT.ERROR_NO_HANDLES);
		}
	}
	int subHIView = outControl[0];
	HILayoutInfo newLayoutInfo = new HILayoutInfo ();
	newLayoutInfo.version = 0;
	OS.HIViewGetLayoutInfo (subHIView, newLayoutInfo);
	HISideBinding biding = newLayoutInfo.binding.top;
	biding.toView = 0;
	biding.kind = OS.kHILayoutBindMin;
	biding.offset = 0;
	biding = newLayoutInfo.binding.left;
	biding.toView = 0;
	biding.kind = OS.kHILayoutBindMin;
	biding.offset = 0;
	biding = newLayoutInfo.binding.bottom;
	biding.toView = 0;
	biding.kind = OS.kHILayoutBindMax;
	biding.offset = 0;
	biding = newLayoutInfo.binding.right;
	biding.toView = 0;
	biding.kind = OS.kHILayoutBindMax;
	biding.offset = 0;
	OS.HIViewSetLayoutInfo (subHIView, newLayoutInfo);
	OS.HIViewChangeFeatures (subHIView, OS.kHIViewFeatureIsOpaque, 0);
	OS.HIViewSetVisible (subHIView, true);
	int parentHandle = browser.handle;
	OS.HIViewAddSubview (browser.handle, subHIView);
	CGRect rect = new CGRect ();
	OS.HIViewGetFrame (parentHandle, rect);
	rect.x = rect.y = 0;
	OS.HIViewSetFrame (subHIView, rect);
	handles.put (new LONG (embedHandle), new LONG (browser.handle));

	if (Callback3 == null) Callback3 = new Callback (this.getClass (), "eventProc3", 3); //$NON-NLS-1$
	int callback3Address = Callback3.getAddress ();
	if (callback3Address == 0) {
		browser.dispose ();
		SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	}
	int [] mask = new int [] {
		OS.kEventClassMouse, OS.kEventMouseDown,
		OS.kEventClassKeyboard, OS.kEventRawKeyDown,
	};
	int controlTarget = OS.GetControlEventTarget (subHIView);
	OS.InstallEventHandler (controlTarget, callback3Address, mask.length / 2, mask, browser.handle, null);

	return embedHandle;
}

long /*int*/ getSiteWindow () {
	return embedHandle;
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

void onDispose (int embedHandle) {
	handles.remove (new LONG (embedHandle));
	if (listener != null) {
		browser.getDisplay ().removeFilter (SWT.FocusIn, listener);
		browser.getShell ().removeListener (SWT.Deactivate, listener);
		listener = null;
	}
	browser = null;
}

void removeWindowSubclass () {
}

boolean sendTraverse () {
	return true;
}

void setSize (int embedHandle, int width, int height) {
	// TODO
}

}

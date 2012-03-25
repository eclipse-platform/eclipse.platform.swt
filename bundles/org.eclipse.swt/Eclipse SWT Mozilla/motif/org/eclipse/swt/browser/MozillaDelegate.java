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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.GTK;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.widgets.*;

class MozillaDelegate {
	Browser browser;
	Listener listener;
	boolean hasFocus;

	static boolean GtkLoaded, IsLinux;
	static {
		String osName = System.getProperty ("os.name").toLowerCase (); //$NON-NLS-1$
		IsLinux = osName.startsWith ("linux"); //$NON-NLS-1$
	}

MozillaDelegate (Browser browser) {
	super ();
	if (!IsLinux) {
		browser.dispose ();
		SWT.error (SWT.ERROR_NO_HANDLES, null, " [Unsupported platform]"); //$NON-NLS-1$
	}
	this.browser = browser;
	
	if (!GtkLoaded) {
		GtkLoaded = true;
		try {
			Library.loadLibrary ("swt-gtk"); //$NON-NLS-1$
		} catch (UnsatisfiedLinkError e) {
			browser.dispose ();
			SWT.error (SWT.ERROR_NO_HANDLES, e);
		}
	}

}

static Browser findBrowser (int handle) {
	Display display = Display.getCurrent ();
	Shell[] shells = display.getShells ();
	Browser browser = null;
	for (int i = 0; i < shells.length; i++) {
		browser = findBrowser (shells[i], handle);
		if (browser != null) break;
	}
	return browser; 
}

static Browser findBrowser (Control control, int gtkHandle) {
	if (control instanceof Browser) {
		Browser browser = (Browser)control;
		WebBrowser webBrowser = browser.webBrowser;
		if (webBrowser instanceof Mozilla) {
			if (((Mozilla)webBrowser).embedHandle == gtkHandle) return browser;
		}
	}
	if (control instanceof Composite) {
		Composite composite = (Composite)control;
		Control[] children = composite.getChildren ();
		for (int i = 0; i < children.length; i++) {
			Browser browser = findBrowser (children[i], gtkHandle);
			if (browser != null) return browser;
		}
	}
	return null;
}

static String getCacheParentPath () {
	return getProfilePath ();
}

static String getLibraryName () {
	return "libxpcom.so"; //$NON-NLS-1$
}

static String getJSLibraryName () {
	return "libmozjs.so"; //$NON-NLS-1$
}

static String getJSLibraryName_Pre4() {
	return "libxpcom.so"; //$NON-NLS-1$
}

static String getProfilePath () {
	String baseDir = System.getProperty ("user.home"); //$NON-NLS-1$
	return baseDir + Mozilla.SEPARATOR_OS + ".mozilla" + Mozilla.SEPARATOR_OS + "eclipse"; //$NON-NLS-1$ //$NON-NLS-2$
}

static String getSWTInitLibraryName () {
	return "swt-xpcominit"; //$NON-NLS-1$
}

static void loadAdditionalLibraries (String mozillaPath) {
// the following is intentionally commented

//	if (!Mozilla.IsPre_4) {
//		System.loadLibrary ("swt-xulrunner10"); // get it extracted
//		byte[] bytes = Converter.wcsToMbcs (null, /* path to libswt-xulrunner10.so */ "", true); //$NON-NLS-1$
//		OS.dlopen (bytes, OS.RTLD_NOW | OS.RTLD_GLOBAL);
//	}
}

static char[] mbcsToWcs (String codePage, byte [] buffer) {
	return Converter.mbcsToWcs (codePage, buffer);
}

static boolean needsSpinup () {
	return true;
}

static byte[] wcsToMbcs (String codePage, String string, boolean terminate) {
	return Converter.wcsToMbcs (codePage, string, terminate);
}

void addWindowSubclass () {
}

int createBaseWindow (nsIBaseWindow baseWindow) {
	return baseWindow.Create ();
}

int getHandle() {
	if (Mozilla.BrowserCount == 1) {
		GTK.gtk_init_check (new int[1], null);
		final Display display = browser.getDisplay ();
		display.asyncExec (new Runnable () {
			public void run () {
				if (Mozilla.BrowserCount == 0) return;
				while (GTK.gtk_events_pending () != 0) {
					GTK.gtk_main_iteration ();
				}
				display.timerExec (25, this);
			}
		});
	}
	browser.getShell ().setFocus ();
	int result = GTK.gtk_plug_new (browser.embeddedHandle);
	GTK.gtk_widget_show (result);
	return result;
}

void handleFocus () {
	if (hasFocus) return;
	hasFocus = true;
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
	int shellStyle = browser.getShell ().getStyle (); 
	if ((shellStyle & SWT.ON_TOP) != 0 && (((shellStyle & SWT.NO_FOCUS) == 0) || ((browser.getStyle () & SWT.NO_FOCUS) == 0))) {
		browser.getDisplay ().asyncExec (new Runnable () {
			public void run () {
				if (browser == null || browser.isDisposed ()) return;
				((Mozilla)browser.webBrowser).Activate ();
			}
		});
	}
}

boolean hookEnterExit () {
	return true;
}

void init () {
}

void onDispose (int embedHandle) {
	if (listener != null) {
		browser.getDisplay ().removeFilter (SWT.FocusIn, listener);
		browser.getShell ().removeListener (SWT.Deactivate, listener);
		listener = null;
	}

	GTK.gtk_widget_destroy (embedHandle);
	while (GTK.gtk_events_pending () != 0) {
		GTK.gtk_main_iteration ();
	}

	browser = null;
}

void removeWindowSubclass () {
}

boolean sendTraverse () {
	return true;
}

void setSize(int embedHandle, int width, int height) {
}
}

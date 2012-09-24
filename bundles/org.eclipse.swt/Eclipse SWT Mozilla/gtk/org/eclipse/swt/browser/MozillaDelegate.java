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

import java.io.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.widgets.*;

class MozillaDelegate {
	Browser browser;
	Shell eventShell;
	long /*int*/ mozillaHandle, embedHandle;
	boolean hasFocus;
	Listener listener;
	static Callback eventCallback;
	static long /*int*/ eventProc;
	static final int STOP_PROPOGATE = 1;

	static boolean IsSparc;
	static {
		String osName = System.getProperty ("os.name").toLowerCase (); //$NON-NLS-1$
		String osArch = System.getProperty ("os.arch").toLowerCase (); //$NON-NLS-1$
		IsSparc = (osName.startsWith ("sunos") || osName.startsWith ("solaris")) && osArch.startsWith("sparc"); //$NON-NLS-1$
	}

MozillaDelegate (Browser browser) {
	super ();
	/*
	* The mozilla libraries on SPARC need the C++ runtime library to be loaded, but they do not declare
	* this dependency because they usually get it for free as a result of the mozilla executable pulling it
	* in.  Load this library here and scope it globally so that the mozilla libraries can resolve.
	*/
	if (IsSparc) {
		byte[] buffer = Converter.wcsToMbcs (null, "libCrun.so.1", true); //$NON-NLS-1$
		OS.dlopen (buffer, OS.RTLD_NOW | OS.RTLD_GLOBAL);
	}
	this.browser = browser;
}

static long /*int*/ eventProc (long /*int*/ handle, long /*int*/ gdkEvent, long /*int*/ pointer) {
	long /*int*/ parent = OS.gtk_widget_get_parent (handle);
	parent = OS.gtk_widget_get_parent (parent);
	if (parent == 0) return 0;
	Widget widget = Display.getCurrent ().findWidget (parent);
	if (widget != null && widget instanceof Browser) {
		return ((Mozilla)((Browser)widget).webBrowser).delegate.gtk_event (handle, gdkEvent, pointer);
	}
	return 0;
}

static Browser findBrowser (long /*int*/ handle) {
	/*
	* Note.  On GTK, Mozilla is embedded into a GtkHBox handle
	* and not directly into the parent Composite handle.
	*/
	long /*int*/ parent = OS.gtk_widget_get_parent (handle);
	Display display = Display.getCurrent ();
	return (Browser)display.findWidget (parent); 
}

static String getCacheParentPath () {
	return getProfilePath ();
}

static String getJSLibraryName () {
	return "libxul.so"; //$NON-NLS-1$
}

static String getJSLibraryName_Pre4() {
	return "libmozjs.so"; //$NON-NLS-1$
}

static String getLibraryName () {
	return "libxpcom.so"; //$NON-NLS-1$
}

static String getProfilePath () {
	String baseDir = System.getProperty ("user.home"); //$NON-NLS-1$
	
	/*
	 * Bug in Sun JRE.  Under some circumstances the value of java property "user.home" is
	 * "?", even when the HOME environment variable has a valid value.  If this happens
	 * then attempt to read the value from the environment directly.
	 */
	if (baseDir.equals ("?")) { //$NON-NLS-1$
		long /*int*/ ptr = C.getenv (wcsToMbcs (null, "HOME", true)); //$NON-NLS-1$
		if (ptr != 0) {
			int length = C.strlen (ptr);
			byte[] bytes = new byte[length];
			C.memmove (bytes, ptr, length);
			baseDir = new String (mbcsToWcs (null, bytes));
		}
	}
	
	return baseDir + Mozilla.SEPARATOR_OS + ".mozilla" + Mozilla.SEPARATOR_OS + "eclipse"; //$NON-NLS-1$ //$NON-NLS-2$
}

static String getSWTInitLibraryName () {
	return "swt-xpcominit"; //$NON-NLS-1$
}

static void loadAdditionalLibraries (String mozillaPath) {
	if (Mozilla.IsPre_4) return;

	/*
	* The use of the swt-xulrunner-fix library works around mozilla bug
	* https://bugzilla.mozilla.org/show_bug.cgi?id=720682 (XULRunner 10).
	*/
	String libName = "libswt-xulrunner-fix.so"; //$NON-NLS-1$
	File libsDir = new File (getProfilePath () + "/libs/" + Mozilla.OS() + '/' + Mozilla.Arch ()); //$NON-NLS-1$
	File file = new File (libsDir, libName);
	if (!file.exists()) {
		java.io.InputStream is = Library.class.getResourceAsStream ('/' + libName);
		if (is != null) {
			if (!libsDir.exists ()) {
				libsDir.mkdirs ();
			}
			int read;
			byte [] buffer = new byte [4096];
			try {
				FileOutputStream os = new FileOutputStream (file);
				while ((read = is.read (buffer)) != -1) {
					os.write(buffer, 0, read);
				}
				os.close ();
				is.close ();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
	}
	if (file.exists ()) {
		byte[] bytes = Converter.wcsToMbcs (null, file.getAbsolutePath (), true);
		OS.dlopen (bytes, OS.RTLD_NOW | OS.RTLD_GLOBAL);
	}
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

long /*int*/ getHandle () {
	/*
	* Bug in Mozilla Linux GTK.  Embedding Mozilla into a GtkFixed
	* handle causes problems with some Mozilla plug-ins.  For some
	* reason, the Flash plug-in causes the child of the GtkFixed
	* handle to be resized to 1 when the Flash document is loaded.
	* That could be due to gtk_container_resize_children being called
	* by Mozilla - or one of its plug-ins - on the GtkFixed handle,
	* causing the child of the GtkFixed handle to be resized to 1.
	* The workaround is to embed Mozilla into a GtkHBox handle.
	*/
	if (OS.GTK_VERSION >= OS.VERSION (3, 0, 0)) {
		embedHandle = OS.gtk_box_new (OS.GTK_ORIENTATION_HORIZONTAL, 0);
		OS.gtk_box_set_homogeneous (embedHandle, false);
	} else {
		embedHandle = OS.gtk_hbox_new (false, 0);
	}
	OS.gtk_container_add (browser.handle, embedHandle);
	OS.gtk_widget_show (embedHandle);
	return embedHandle;
}

long /*int*/ getSiteWindow () {
	return embedHandle;
}

long /*int*/ gtk_event (long /*int*/ handle, long /*int*/ gdkEvent, long /*int*/ pointer) {
	GdkEvent event = new GdkEvent ();
	OS.memmove (event, gdkEvent, GdkEvent.sizeof);
	if (event.type == OS.GDK_BUTTON_PRESS) {
		if (!hasFocus) browser.setFocus ();
	}

	/* 
	* Stop the propagation of events that are not consumed by Mozilla, before
	* they reach the parent embedder.  These event have already been received.
	*/
	if (pointer == STOP_PROPOGATE) return 1;
	return 0;
}

void handleFocus () {
	if (hasFocus) return;
	hasFocus = true;
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
	return false;
}

void init () {
	if (eventCallback == null) {
		eventCallback = new Callback (getClass (), "eventProc", 3); //$NON-NLS-1$
		eventProc = eventCallback.getAddress ();
		if (eventProc == 0) {
			browser.dispose ();
			Mozilla.error (SWT.ERROR_NO_MORE_CALLBACKS);
		}
	}

	/*
	* Feature in Mozilla.  GtkEvents such as key down, key pressed may be consumed
	* by Mozilla and never be received by the parent embedder.  The workaround
	* is to find the top Mozilla gtk widget that receives all the Mozilla GtkEvents,
	* i.e. the first child of the parent embedder. Then hook event callbacks and
	* forward the event to the parent embedder before Mozilla received and consumed
	* them.
	*/
	long /*int*/ list = OS.gtk_container_get_children (embedHandle);
	if (list != 0) {
		mozillaHandle = OS.g_list_data (list);
		OS.g_list_free (list);
		
		if (mozillaHandle != 0) {			
			/* Note. Callback to get events before Mozilla receives and consumes them. */
			OS.g_signal_connect (mozillaHandle, OS.event, eventProc, 0);
			
			/* 
			* Note.  Callback to get the events not consumed by Mozilla - and to block 
			* them so that they don't get propagated to the parent handle twice.  
			* This hook is set after Mozilla and is therefore called after Mozilla's 
			* handler because GTK dispatches events in their order of registration.
			*/
			OS.g_signal_connect (mozillaHandle, OS.key_press_event, eventProc, STOP_PROPOGATE);
			OS.g_signal_connect (mozillaHandle, OS.key_release_event, eventProc, STOP_PROPOGATE);
			OS.g_signal_connect (mozillaHandle, OS.button_press_event, eventProc, STOP_PROPOGATE);
		}
	}
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
	OS.gtk_widget_set_size_request (embedHandle, width, height);
}

}

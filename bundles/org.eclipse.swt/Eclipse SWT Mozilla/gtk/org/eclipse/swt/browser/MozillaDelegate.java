/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

class MozillaDelegate {
	Browser browser;
	int /*long*/ mozillaHandle;
	static Callback eventCallback;
	static int /*long*/ eventProc;
	static final int STOP_PROPOGATE = 1;
	static final String ADD_WIDGET_KEY = "org.eclipse.swt.internal.addWidget"; //$NON-NLS-1$

	static boolean IsLinux;
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
}

static int /*long*/ eventProc (int /*long*/ handle, int /*long*/ gdkEvent, int /*long*/ pointer) {
	Widget widget = Display.getCurrent ().findWidget (handle);
	if (widget != null && widget instanceof Browser) {
		return ((Mozilla)((Browser)widget).webBrowser).delegate.gtk_event (handle, gdkEvent, pointer);
	}
	return 0;
}

static Browser findBrowser (int /*long*/ handle) {
	/*
	* Note.  On GTK, Mozilla is embedded into a GtkHBox handle
	* and not directly into the parent Composite handle.
	*/
	int /*long*/ parent = OS.gtk_widget_get_parent (handle);
	Display display = Display.getCurrent ();
	return (Browser)display.findWidget (parent); 
}

public static char[] mbcsToWcs (String codePage, byte [] buffer) {
	return Converter.mbcsToWcs (codePage, buffer);
}

public static byte[] wcsToMbcs (String codePage, String string, boolean terminate) {
	return Converter.wcsToMbcs (codePage, string, terminate);
}

int /*long*/ getHandle () {
	if (eventCallback == null) {
		eventCallback = new Callback (getClass (), "eventProc", 3); //$NON-NLS-1$
		eventProc = eventCallback.getAddress ();
		if (eventProc == 0) {
			browser.dispose ();
			Mozilla.error (SWT.ERROR_NO_MORE_CALLBACKS);
		}
	}

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
	int /*long*/ embedHandle = OS.gtk_hbox_new (false, 0);
	OS.gtk_container_add (browser.handle, embedHandle);
	OS.gtk_widget_show (embedHandle);
	
	/*
	* Feature in Mozilla.  GtkEvents such as key down, key pressed may be consumed
	* by Mozilla and never be received by the parent embedder.  The workaround
	* is to find the top Mozilla gtk widget that receives all the Mozilla GtkEvents,
	* i.e. the first child of the parent embedder. Then hook event callbacks and
	* forward the event to the parent embedder before Mozilla received and consumed
	* them.
	*/
	int /*long*/ list = OS.gtk_container_get_children (embedHandle);
	if (list != 0) {
		mozillaHandle = OS.g_list_data (list);
		OS.g_list_free (list);
		
		if (mozillaHandle != 0) {			
			browser.getDisplay ().setData (ADD_WIDGET_KEY, new Object[] {new LONG (mozillaHandle), browser});

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
		}
	}
	return embedHandle;
}

int /*long*/ gtk_event (int /*long*/ handle, int /*long*/ gdkEvent, int /*long*/ pointer) {
	/* 
	* Stop the propagation of events that are not consumed by Mozilla, before
	* they reach the parent embedder.  These event have already been received.
	*/
	if (pointer == STOP_PROPOGATE) return 1;

	GdkEvent event = new GdkEvent ();
	OS.memmove (event, gdkEvent, GdkEvent.sizeof);
	switch (event.type) {
		case OS.GDK_KEY_PRESS:
		case OS.GDK_KEY_RELEASE:
		case OS.GDK_BUTTON_PRESS:
		case OS.GDK_BUTTON_RELEASE: {
			/* 
			* Forward the event to the parent embedder before Mozilla receives it, 
			* as Mozilla may or may not consume it.
			*/
			OS.gtk_widget_event (browser.handle, gdkEvent);
			break;
		}
	}
	return 0;
}

void onDispose (int /*long*/ embedHandle) {
	browser.getDisplay ().setData (ADD_WIDGET_KEY, new Object[] {new LONG (mozillaHandle), null});
}

void setSize (int /*long*/ embedHandle, int width, int height) {
	OS.gtk_widget_set_size_request (embedHandle, width, height);
}

}

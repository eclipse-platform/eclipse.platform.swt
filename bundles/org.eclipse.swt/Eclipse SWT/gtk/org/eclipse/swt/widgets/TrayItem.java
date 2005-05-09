/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

/**
 * Instances of this class represent icons that can be placed on the
 * system tray or task bar status area.
 *
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, MenuDetect, Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @since 3.0
 */
public class TrayItem extends Item {
	Tray parent;
	String toolTipText;
	int /*long*/ imageHandle;
	int /*long*/ tooltipsHandle;
	ImageList imageList;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tray</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TrayItem (Tray parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (parent.getItemCount ());
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the receiver is selected
 * <code>widgetDefaultSelected</code> is called when the receiver is double-clicked
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void createWidget (int index) {
	super.createWidget (index);
	parent.createItem (this, index);
}

void createHandle (int index) {
	state |= HANDLE;
	handle = OS.gtk_plug_new (0);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	imageHandle = OS.gtk_image_new ();
	if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_container_add (handle, imageHandle);
	OS.gtk_widget_show (handle);
	OS.gtk_widget_show (imageHandle);
	int /*long*/ id = OS.gtk_plug_get_id (handle);
	int monitor = 0;
	int /*long*/ screen = OS.gdk_screen_get_default ();
	if (screen != 0) {
		monitor = OS.gdk_screen_get_number (screen);
	}
	byte [] trayBuffer = Converter.wcsToMbcs (null, "_NET_SYSTEM_TRAY_S" + monitor, true);
	int /*long*/ trayAtom = OS.gdk_atom_intern (trayBuffer, true);
	int /*long*/ xTrayAtom = OS.gdk_x11_atom_to_xatom (trayAtom);
	int /*long*/ xDisplay = OS.GDK_DISPLAY ();
	int /*long*/ trayWindow = OS.XGetSelectionOwner (xDisplay, xTrayAtom);
	byte [] messageBuffer = Converter.wcsToMbcs (null, "_NET_SYSTEM_TRAY_OPCODE", true);
	int /*long*/ messageAtom = OS.gdk_atom_intern (messageBuffer, true);
	int /*long*/ xMessageAtom = OS.gdk_x11_atom_to_xatom (messageAtom);
	XClientMessageEvent event = new XClientMessageEvent ();
	event.type = OS.ClientMessage;
	event.window = trayWindow;
	event.message_type = xMessageAtom;
	event.format = 32;
	event.data [0] = OS.GDK_CURRENT_TIME;
	event.data [1] = OS.SYSTEM_TRAY_REQUEST_DOCK;
	event.data [2] = id;
	int /*long*/ clientEvent = OS.g_malloc (XClientMessageEvent.sizeof);
	OS.memmove (clientEvent, event, XClientMessageEvent.sizeof);
	OS.XSendEvent (OS.GDK_DISPLAY (), trayWindow, false, OS.NoEventMask, clientEvent);
	OS.g_free (clientEvent);
}

/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget ();
	return toolTipText;
}

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ eventPtr) {
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, eventPtr, GdkEventButton.sizeof);
	if (gdkEvent.type == OS.GDK_3BUTTON_PRESS) return 0;
	if (gdkEvent.button == 3 && gdkEvent.type == OS.GDK_BUTTON_PRESS) {
		sendEvent (SWT.MenuDetect);
		return 0;
	}
	if (gdkEvent.type == OS.GDK_2BUTTON_PRESS) {
		postEvent (SWT.DefaultSelection);
	} else {
		postEvent (SWT.Selection);
	}
	return 0;
}

void hookEvents () {
	int eventMask = OS.GDK_BUTTON_PRESS_MASK;
	OS.gtk_widget_add_events (handle, eventMask);
	OS.g_signal_connect (handle, OS.button_press_event, display.windowProc3, BUTTON_PRESS_EVENT);
 }

/**
 * Returns <code>true</code> if the receiver is visible and 
 * <code>false</code> otherwise.
 *
 * @return the receiver's visibility
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget ();
	return OS.GTK_WIDGET_VISIBLE (handle);
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseWidget () {
	super.releaseWidget ();
	if (tooltipsHandle != 0) OS.g_object_unref (tooltipsHandle);
	imageHandle = tooltipsHandle = 0;
	if (imageList != null) imageList.dispose ();
	imageList = null;
	toolTipText = null;
	if (handle != 0) OS.gtk_widget_destroy (handle);
	handle = 0;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver is selected.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection, listener);
}

/**
 * Sets the receiver's image.
 *
 * @param image the new image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.image = image;
	if (image != null) {
		Rectangle rect = image.getBounds ();
		OS.gtk_widget_set_size_request (handle, rect.width, rect.height);
		if (imageList == null) imageList = new ImageList ();
		int imageIndex = imageList.indexOf (image);
		if (imageIndex == -1) {
			imageIndex = imageList.add (image);
		} else {
			imageList.put (imageIndex, image);
		}
		int /*long*/ pixbuf = imageList.getPixbuf (imageIndex);
		OS.gtk_image_set_from_pixbuf (imageHandle, pixbuf);
		OS.gtk_widget_show (imageHandle);
	} else {
		OS.gtk_widget_set_size_request (handle, 1, 1);
		OS.gtk_image_set_from_pixbuf (imageHandle, 0);
		OS.gtk_widget_hide (imageHandle);
	}
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
 *
 * @param value the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget ();
	toolTipText = string;
	byte [] buffer = null;
	if (string != null && string.length () > 0) {
		buffer = Converter.wcsToMbcs (null, string, true);
	}
	if (tooltipsHandle == 0) {
		tooltipsHandle = OS.gtk_tooltips_new ();
		if (tooltipsHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.g_object_ref (tooltipsHandle);
		OS.gtk_object_sink (tooltipsHandle);
	}
	OS.gtk_tooltips_set_tip (tooltipsHandle, handle, buffer, null);
}

/**
 * Makes the receiver visible if the argument is <code>true</code>,
 * and makes it invisible otherwise. 
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
	checkWidget ();
	if (OS.GTK_WIDGET_VISIBLE (handle) == visible) return;
	if (visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		OS.gtk_widget_show (handle);
	} else {
		OS.gtk_widget_hide (handle);
		sendEvent (SWT.Hide);
	}
}
}

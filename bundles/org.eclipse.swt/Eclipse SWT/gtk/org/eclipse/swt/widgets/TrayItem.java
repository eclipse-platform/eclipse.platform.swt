/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, MenuDetect, Selection</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tray">Tray, TrayItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.0
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TrayItem extends Item {
	Tray parent;
	ToolTip toolTip;
	String toolTipText;
	long /*int*/ imageHandle;
	long /*int*/ tooltipsHandle;
	ImageList imageList;
	Image highlightImage;

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
 * be notified when the platform-specific context menu trigger
 * has occurred, by sending it one of the messages defined in
 * the <code>MenuDetectListener</code> interface.
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
 * @see MenuDetectListener
 * @see #removeMenuDetectListener
 *
 * @since 3.3
 */
public void addMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MenuDetect, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the receiver is selected
 * <code>widgetDefaultSelected</code> is called when the receiver is double-clicked
 * </p>
 *
 * @param listener the listener which should be notified when the receiver is selected by the user
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
	if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
		handle = OS.gtk_status_icon_new ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		imageHandle = OS.gtk_image_new ();
		OS.gtk_status_icon_set_visible (handle,true);
	} else {
		handle = OS.gtk_plug_new (0);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		imageHandle = OS.gtk_image_new ();
		if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (handle, imageHandle);
		OS.gtk_widget_show (handle);
		OS.gtk_widget_show (imageHandle);
		long /*int*/ id = OS.gtk_plug_get_id (handle);
		int monitor = 0;
		long /*int*/ screen = OS.gdk_screen_get_default ();
		if (screen != 0) {
			monitor = OS.gdk_screen_get_number (screen);
		}
		byte [] trayBuffer = Converter.wcsToMbcs (null, "_NET_SYSTEM_TRAY_S" + monitor, true);
		long /*int*/ trayAtom = OS.gdk_atom_intern (trayBuffer, true);
		long /*int*/ xTrayAtom = OS.gdk_x11_atom_to_xatom (trayAtom);
		long /*int*/ xDisplay = OS.gdk_x11_display_get_xdisplay(OS.gdk_display_get_default());
		long /*int*/ trayWindow = OS.XGetSelectionOwner (xDisplay, xTrayAtom);
		byte [] messageBuffer = Converter.wcsToMbcs (null, "_NET_SYSTEM_TRAY_OPCODE", true);
		long /*int*/ messageAtom = OS.gdk_atom_intern (messageBuffer, true);
		long /*int*/ xMessageAtom = OS.gdk_x11_atom_to_xatom (messageAtom);
		XClientMessageEvent event = new XClientMessageEvent ();
		event.type = OS.ClientMessage;
		event.window = trayWindow;
		event.message_type = xMessageAtom;
		event.format = 32;
		event.data [0] = OS.GDK_CURRENT_TIME;
		event.data [1] = OS.SYSTEM_TRAY_REQUEST_DOCK;
		event.data [2] = id;
		long /*int*/ clientEvent = OS.g_malloc (XClientMessageEvent.sizeof);
		OS.memmove (clientEvent, event, XClientMessageEvent.sizeof);
		OS.XSendEvent (xDisplay, trayWindow, false, OS.NoEventMask, clientEvent);
		OS.g_free (clientEvent);
	}
}

void deregister () {
	super.deregister ();
	display.removeWidget (imageHandle);
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns the receiver's parent, which must be a <code>Tray</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public Tray getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns the receiver's highlight image if it has one, or null
 * if it does not.
 *
 * @return the receiver's highlight image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.8
 */
public Image getHighlightImage () {
	checkWidget ();
	return highlightImage;
}

/**
 * Returns the receiver's tool tip, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public ToolTip getToolTip () {
	checkWidget ();
	return toolTip;
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

long /*int*/ gtk_activate (long /*int*/ widget) {
	sendSelectionEvent (SWT.Selection);
	/*
	* Feature in GTK. GTK will generate a single-click event before sending 
	* a double-click event. To know when to send a DefaultSelection, look for 
	* the single-click as the current event and for the double-click in the
	* event queue.
	*/
	long /*int*/ nextEvent = OS.gdk_event_peek ();
	if (nextEvent != 0) {
		int nextEventType = OS.GDK_EVENT_TYPE (nextEvent);
		long /*int*/ currEvent = OS.gtk_get_current_event ();
		int currEventType = 0;
		if (currEvent != 0) {
			currEventType = OS.GDK_EVENT_TYPE (currEvent);
			OS.gdk_event_free (currEvent);
		}
		OS.gdk_event_free (nextEvent);
		if (currEventType == OS.GDK_BUTTON_PRESS && nextEventType == OS.GDK_2BUTTON_PRESS) {
			sendSelectionEvent (SWT.DefaultSelection);
		}
	}
	return 0;
}

long /*int*/ gtk_button_press_event (long /*int*/ widget, long /*int*/ eventPtr) {
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, eventPtr, GdkEventButton.sizeof);
	if (gdkEvent.type == OS.GDK_3BUTTON_PRESS) return 0;
	if (gdkEvent.button == 3 && gdkEvent.type == OS.GDK_BUTTON_PRESS) {
		sendEvent (SWT.MenuDetect);
		return 0;
	}
	if (gdkEvent.type == OS.GDK_2BUTTON_PRESS) {
		sendSelectionEvent (SWT.DefaultSelection);
	} else {
		sendSelectionEvent (SWT.Selection);
	}
	return 0;
}

long /*int*/ gtk_size_allocate (long /*int*/ widget, long /*int*/ allocation) {
	if (image != null && image.mask != 0) {
		if (OS.gdk_drawable_get_depth (image.mask) == 1) {
			GtkAllocation widgetAllocation = new GtkAllocation ();
			gtk_widget_get_allocation (widget, widgetAllocation);
			int xoffset = (int) Math.floor (widgetAllocation.x + ((widgetAllocation.width -OS.GTK_WIDGET_REQUISITION_WIDTH (widget)) * 0.5) + 0.5);
			int yoffset = (int) Math.floor (widgetAllocation.y + ((widgetAllocation.height - OS.GTK_WIDGET_REQUISITION_HEIGHT (widget)) * 0.5) + 0.5);
			Rectangle b = image.getBounds();
			long /*int*/ gdkImagePtr = OS.gdk_drawable_get_image (image.mask, 0, 0, b.width, b.height);
			if (gdkImagePtr == 0) error(SWT.ERROR_NO_HANDLES);
			GdkImage gdkImage = new GdkImage();
			OS.memmove (gdkImage, gdkImagePtr);
			byte[] maskData = new byte [gdkImage.bpl * gdkImage.height];
			OS.memmove (maskData, gdkImage.mem, maskData.length);
			OS.g_object_unref (gdkImagePtr);
			Region region = new Region (display);
			for (int y = 0; y < b.height; y++) {
				for (int x = 0; x < b.width; x++) {
					int index = (y * gdkImage.bpl) + (x >> 3);
					int theByte = maskData [index] & 0xFF;
					int mask = 1 << (x & 0x7);
					if ((theByte & mask) != 0) {
						region.add (xoffset + x, yoffset + y, 1, 1);
					}
				}
			}
			OS.gtk_widget_realize (handle);
			long /*int*/ window = gtk_widget_get_window (handle);
			OS.gdk_window_shape_combine_region (window, region.handle, 0, 0);
			region.dispose ();
		}
	}
	return 0;
}

long /*int*/ gtk_status_icon_popup_menu (long /*int*/ widget, long /*int*/ button, long /*int*/ activate_time) {
	/*
	* GTK provides a MenuPositionFunc for GtkStatusIcon in order
	* to set the popup-menu aligned to the tray.
	*/
	Display display = this.display;
	display.currentTrayItem = this;
	sendEvent (SWT.MenuDetect);
	if (!isDisposed ()) display.runPopups();
	display.currentTrayItem = null;
	return 0;
}

void hookEvents () {
	if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
		OS.g_signal_connect_closure (handle, OS.activate, display.closures [ACTIVATE], false);
		OS.g_signal_connect_closure (handle, OS.popup_menu, display.closures [STATUS_ICON_POPUP_MENU], false);
	} else {
		int eventMask = OS.GDK_BUTTON_PRESS_MASK;
		OS.gtk_widget_add_events (handle, eventMask);
		OS.g_signal_connect_closure_by_id (handle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.closures [BUTTON_PRESS_EVENT], false);
		OS.g_signal_connect_closure_by_id (imageHandle, display.signalIds [SIZE_ALLOCATE], 0, display.closures [SIZE_ALLOCATE], false);
	}
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
	if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
		return OS.gtk_status_icon_get_visible (handle);
	}
	return gtk_widget_get_visible (handle);
}

void register () {
	super.register ();
	display.addWidget (imageHandle, this);
}

void releaseHandle () {
	if (handle != 0) {
		if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
			OS.g_object_unref (handle);
		} else {
			OS.gtk_widget_destroy (handle);
		}
	}
	handle = imageHandle = 0;
	super.releaseHandle ();
	parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
	if (tooltipsHandle != 0) OS.g_object_unref (tooltipsHandle);
	tooltipsHandle = 0;
	if (imageList != null) imageList.dispose ();
	imageList = null;
	toolTipText = null;
	highlightImage = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the platform-specific context menu trigger has
 * occurred.
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
 * @see MenuDetectListener
 * @see #addMenuDetectListener
 *
 * @since 3.3
 */
public void removeMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MenuDetect, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver is selected by the user.
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
 * Sets the receiver's highlight image.
 *
 * @param image the new highlight image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.8
 */
public void setHighlightImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	highlightImage = image;
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
		if (imageList == null) imageList = new ImageList ();
		int imageIndex = imageList.indexOf (image);
		if (imageIndex == -1) {
			imageIndex = imageList.add (image);
		} else {
			imageList.put (imageIndex, image);
		}
		long /*int*/ pixbuf = imageList.getPixbuf (imageIndex);
		if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
			OS.gtk_status_icon_set_from_pixbuf (handle, pixbuf);
			OS.gtk_status_icon_set_visible (handle, true);
		} else {
			Rectangle rect = image.getBounds ();
			OS.gtk_widget_set_size_request (handle, rect.width, rect.height);
			OS.gtk_image_set_from_pixbuf (imageHandle, pixbuf);
			OS.gtk_widget_show (imageHandle);
		}
	} else {
		OS.gtk_widget_set_size_request (handle, 1, 1);
		if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
			OS.gtk_status_icon_set_from_pixbuf (handle, 0);
			OS.gtk_status_icon_set_visible (handle, false);
		} else {
			OS.gtk_image_set_from_pixbuf (imageHandle, 0);
			OS.gtk_widget_hide (imageHandle);
		}
	}
}

/**
 * Sets the receiver's tool tip to the argument, which
 * may be null indicating that no tool tip should be shown.
 *
 * @param toolTip the new tool tip (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setToolTip (ToolTip toolTip) {
	checkWidget ();
	ToolTip oldTip = this.toolTip, newTip = toolTip;
	if (oldTip != null) oldTip.item = null;
	this.toolTip = newTip;
	if (newTip != null) newTip.item = this;
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the 
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be 
 * escaped by doubling it in the string.
 * </p>
 * 
 * @param string the new tool tip text (or null)
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
	if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
		OS.gtk_status_icon_set_tooltip (handle, buffer);
	} else {
		if (tooltipsHandle == 0) {
			tooltipsHandle = OS.gtk_tooltips_new ();
			if (tooltipsHandle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.g_object_ref (tooltipsHandle);
			g_object_ref_sink (tooltipsHandle);
		}
		OS.gtk_tooltips_set_tip (tooltipsHandle, handle, buffer, null);
	}
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
	if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
		if(OS.gtk_status_icon_get_visible (handle) == visible) return;	
	} else {
		if (gtk_widget_get_visible (handle) == visible) return;
	}
	if (visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
			OS.gtk_status_icon_set_visible (handle, visible);
		} else
			OS.gtk_widget_show (handle);
	} else {
		if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
			OS.gtk_status_icon_set_visible (handle, visible);
		} else
			OS.gtk_widget_hide (handle);
		sendEvent (SWT.Hide);
	}
}
}
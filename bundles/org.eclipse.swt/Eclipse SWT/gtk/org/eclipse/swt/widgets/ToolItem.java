package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a button in a tool bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, PUSH, RADIO, SEPARATOR and DROP_DOWN 
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class ToolItem extends Item {
	int boxHandle, arrowHandle, separatorHandle, labelHandle, imageHandle;
	ToolBar parent;
	Control control;
	Image hotImage, disabledImage;
	String toolTipText;
	boolean drawHotImage;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>) and a style value
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
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (parent.getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
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
 * @param index the index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	int count = parent.getItemCount ();
	if (!(0 <= index && index <= count)) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	createWidget (index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called when the mouse is over the arrow portion of a drop-down tool,
 * the event object detail field contains the value <code>SWT.ARROW</code>.
 * <code>widgetDefaultSelected</code> is not called.
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

void createHandle (int index) {
	state |= HANDLE;
	if ((style & SWT.SEPARATOR) == 0) {
		boxHandle = (parent.style & SWT.RIGHT) != 0 ? OS.gtk_hbox_new (false, 0) : OS.gtk_vbox_new (false, 0);
		if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
		labelHandle = OS.gtk_label_new_with_mnemonic (null);
		if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
		imageHandle = OS.gtk_image_new ();
		if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (boxHandle, imageHandle);
		OS.gtk_container_add (boxHandle, labelHandle);
		OS.gtk_widget_show (boxHandle);
	}	
	int bits = SWT.SEPARATOR | SWT.RADIO | SWT.CHECK | SWT.PUSH | SWT.DROP_DOWN;
	switch (style & bits) {
		case SWT.SEPARATOR:
			handle = OS.gtk_hbox_new (false, 0);
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			boolean isVertical = (parent.style & SWT.VERTICAL) != 0;
			separatorHandle = isVertical ? OS.gtk_hseparator_new() : OS.gtk_vseparator_new();
			if (separatorHandle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.gtk_widget_set_size_request (separatorHandle, isVertical ? 15 : 6, isVertical ? 6 : 15);
			OS.gtk_container_add (handle, separatorHandle);
			OS.gtk_widget_show (separatorHandle);
			break;
		case SWT.DROP_DOWN:
			handle = OS.gtk_button_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			int arrowBoxHandle = OS.gtk_hbox_new (false, 0);
			if (arrowBoxHandle == 0) error(SWT.ERROR_NO_HANDLES);
			arrowHandle = OS.gtk_arrow_new (OS.GTK_ARROW_DOWN, OS.GTK_SHADOW_NONE);
			if (arrowHandle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.gtk_widget_set_size_request (arrowHandle, 8, 6);
			OS.gtk_container_add (handle, arrowBoxHandle);
			OS.gtk_container_add (arrowBoxHandle, boxHandle);	
			OS.gtk_container_add (arrowBoxHandle, arrowHandle);	
			OS.gtk_widget_show (arrowBoxHandle);
			OS.gtk_widget_show (arrowHandle);
			break;
		case SWT.RADIO:
			/*
			*  This code is intentionally commented.  Because GTK
			* enforces radio behavior in a button group a radio group
			* is not created for each set of contiguous buttons, each
			* radio button will not draw unpressed.  The fix is to use
			* toggle buttons instead.
			*/
//			handle = OS.gtk_radio_button_new (0);
//			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
//			OS.gtk_toggle_button_set_mode (handle, false);
//			OS.gtk_container_add (handle, boxHandle);	
//			break;
		case SWT.CHECK:
			handle = OS.gtk_toggle_button_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.gtk_toggle_button_set_mode (handle, false);
			OS.gtk_container_add (handle, boxHandle);	
			break;
		case SWT.PUSH:
		default:
			handle = OS.gtk_button_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.gtk_container_add (handle, boxHandle);
			break;
	}
	if ((style & SWT.SEPARATOR) == 0) {
		int [] relief = new int [1];
		OS.gtk_widget_style_get (parent.handle, OS.button_relief, relief, 0);
		OS.gtk_button_set_relief (handle, relief [0]);
	}
	OS.GTK_WIDGET_UNSET_FLAGS (handle, OS.GTK_CAN_FOCUS);
	OS.gtk_widget_show (handle);
	OS.gtk_toolbar_insert_widget (parent.handle, handle, null, null, index);
	setForegroundColor (parent.getForegroundColor ());
	setFontDescription (parent.getFontDescription ());
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget();
	int topHandle = topHandle ();
	int x = OS.GTK_WIDGET_X (topHandle);
	int y = OS.GTK_WIDGET_Y (topHandle);
	int width = OS.GTK_WIDGET_WIDTH (topHandle);
	int height = OS.GTK_WIDGET_HEIGHT (topHandle);
	return new Rectangle (x, y, width, height);
}

/**
 * Returns the control that is used to fill the bounds of
 * the item when the items is a <code>SEPARATOR</code>.
 *
 * @return the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getControl () {
	checkWidget();
	return control;
}

/**
 * Returns the receiver's disabled image if it has one, or null
 * if it does not.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @return the receiver's disabled image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getDisabledImage () {
	checkWidget();
	return disabledImage;
}

public Display getDisplay () {
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise.
 * <p>
 * A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 * </p>
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getEnabled () {
	checkWidget();
	int topHandle = topHandle ();
	return OS.GTK_WIDGET_SENSITIVE (topHandle);
}

/**
 * Returns the receiver's hot image if it has one, or null
 * if it does not.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @return the receiver's hot image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getHotImage () {
	checkWidget();
	return hotImage;
}

/**
 * Returns the receiver's parent, which must be a <code>ToolBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolBar getParent () {
	checkWidget();
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button). If the receiver is of any other type, this method
 * returns false.
 * </p>
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	return OS.gtk_toggle_button_get_active (handle);
}

/**
 * Returns the receiver's tool tip text, or null if it has not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget();
	return toolTipText;
}

/**
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget();
	int topHandle = topHandle ();
	return OS.GTK_WIDGET_WIDTH (topHandle);
}

void hookEvents () {
	super.hookEvents ();
	if ((style & SWT.SEPARATOR) != 0) return;
	Display display = getDisplay ();
	int windowProc2 = display.windowProc2;
	int windowProc3 = display.windowProc3;
	OS.g_signal_connect (handle, OS.clicked, windowProc2, SWT.Selection);
	OS.g_signal_connect (handle, OS.enter_notify_event, windowProc3, SWT.MouseEnter);
	OS.g_signal_connect (handle, OS.leave_notify_event, windowProc3, SWT.MouseExit);

	/*
	 * Feature in GTK.
	 * Usually, GTK widgets propagate all events to their parent when they
	 * are done their own processing.  However, in contrast to other widgets,
	 * the buttons that make up the tool items, do not propagate the mouse
	 * up/down events.
	 * (It is interesting to note that they DO propagate mouse motion events.)
	 */
	int mask =
		OS.GDK_EXPOSURE_MASK | OS.GDK_POINTER_MOTION_MASK |
		OS.GDK_BUTTON_PRESS_MASK | OS.GDK_BUTTON_RELEASE_MASK | 
		OS.GDK_ENTER_NOTIFY_MASK | OS.GDK_LEAVE_NOTIFY_MASK | 
		OS.GDK_KEY_PRESS_MASK | OS.GDK_KEY_RELEASE_MASK |
		OS.GDK_FOCUS_CHANGE_MASK;
	OS.gtk_widget_add_events (handle, mask);
	OS.g_signal_connect (handle, OS.button_press_event, windowProc3, SWT.MouseDown);
	OS.g_signal_connect (handle, OS.button_release_event, windowProc3, SWT.MouseUp);
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget();
	return getEnabled () && parent.isEnabled ();
}

int processMouseDown (int callData, int arg1, int int2) {
	GdkEventButton e = new GdkEventButton();
	OS.memmove(e, callData, GdkEventButton.sizeof);
	double x_back = e.x;   e.x += OS.GTK_WIDGET_X(handle);
	double y_back = e.y;   e.y += OS.GTK_WIDGET_Y(handle);
	OS.memmove(callData, e, GdkEventButton.sizeof);
	parent.processMouseDown (callData, arg1, int2);
	e.x = x_back; e.y = y_back;
	OS.memmove(callData, e, GdkEventButton.sizeof);
	return 0;
}

int processMouseUp (int callData, int arg1, int int2) {
	GdkEventButton e = new GdkEventButton();
	OS.memmove(e, callData, GdkEventButton.sizeof);
	double x_back = e.x;   e.x += OS.GTK_WIDGET_X(handle);
	double y_back = e.y;   e.y += OS.GTK_WIDGET_Y(handle);
	OS.memmove(callData, e, GdkEventButton.sizeof);
	parent.processMouseUp (callData, arg1, int2);
	e.x = x_back; e.y = y_back;
	OS.memmove(callData, e, GdkEventButton.sizeof);
	return 0;
}

int processMouseEnter (int int0, int int1, int int2) {
	drawHotImage = (parent.style & SWT.FLAT) != 0 && hotImage != null;
	if (drawHotImage && imageHandle != 0) {
		OS.gtk_image_set_from_pixmap (imageHandle, hotImage.pixmap, hotImage.mask);
	}
	return 0;
}

int processMouseExit (int int0, int int1, int int2) {
	if (drawHotImage) {
		drawHotImage = false;
		if (imageHandle != 0 && image != null) {
			OS.gtk_image_set_from_pixmap (imageHandle, image.pixmap, image.mask);
		}	
	}
	return 0;
}

int processSelection  (int int0, int int1, int int2) {
	if ((style & SWT.RADIO) != 0) {
		this.setSelection (true);
		ToolItem [] items = parent.getItems ();
		int index = 0;
		while (index < items.length && items [index] != this) index++;
		ToolItem item;
		int i = index;
		while (--i >= 0 && ((item = items [i]).style & SWT.RADIO) != 0) {
			item.setSelection (false);
		}
		i = index;
		while (++i < items.length && ((item = items [i]).style & SWT.RADIO) != 0) {
			item.setSelection (false);
		}
	}
	Event event = new Event ();
	if ((style & SWT.DROP_DOWN) != 0) {
		int eventPtr = OS.gtk_get_current_event ();
		if (eventPtr != 0) {
			GdkEvent gdkEvent = new GdkEvent ();
			OS.memmove (gdkEvent, eventPtr, GdkEvent.sizeof);
			switch (gdkEvent.type) {
				case OS.GDK_BUTTON_PRESS:
				case OS.GDK_2BUTTON_PRESS: 
				case OS.GDK_BUTTON_RELEASE: {
					double [] x_win = new double [1];
					double [] y_win = new double [1];
					OS.gdk_event_get_coords (eventPtr, x_win, y_win);
					if ((int) x_win [0] > OS.GTK_WIDGET_WIDTH (boxHandle)) {
						event.detail = SWT.ARROW;
					}
					break;
				}
			}
			OS.gdk_event_free (eventPtr);
		}
	}
	postEvent (SWT.Selection, event);
	return 0;
}

void releaseHandle () {
	super.releaseHandle ();
	boxHandle = arrowHandle = separatorHandle = labelHandle = imageHandle = 0;
}

void releaseWidget () {
	// reparent the control back to the toolbar
	if (control != null) setControl (null);
	super.releaseWidget ();
	parent = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
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
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

void resizeControl () {
	if (control == null) return;
	//FIXME - we should not use computeSize()
	control.setSize (control.computeSize (OS.GTK_WIDGET_WIDTH (handle), SWT.DEFAULT));
}

/**
 * Sets the control that is used to fill the bounds of
 * the item when the items is a <code>SEPARATOR</code>.
 *
 * @param control the new control
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if the control is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setControl (Control control) {
	checkWidget ();
	if (control != null) {
		if (control.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if ((style & SWT.SEPARATOR) == 0) return;
	Control newControl = control;
	Control oldControl = this.control;
	if (oldControl == newControl) return;
	this.control = newControl;
	if (oldControl != null) {
		OS.gtk_widget_reparent (oldControl.topHandle(), parent.handle);
	}
	if (newControl != null) {
		OS.gtk_widget_reparent (newControl.topHandle(), handle);
		OS.gtk_widget_hide (separatorHandle);
		resizeControl ();
	} else {
		OS.gtk_widget_show (separatorHandle);
	}
}

/**
 * Sets the receiver's disabled image to the argument, which may be
 * null indicating that no disabled image should be displayed.
 * <p>
 * The disbled image is displayed when the receiver is disabled.
 * </p>
 *
 * @param image the disabled image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDisabledImage (Image image) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	disabledImage = image;
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise.
 * <p>
 * A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 * </p>
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget();
	int topHandle = topHandle ();
	OS.gtk_widget_set_sensitive (topHandle, enabled);
}

void setFontDescription (int font) {
	OS.gtk_widget_modify_font (handle, font);
	if (labelHandle != 0) OS.gtk_widget_modify_font (labelHandle, font);
	if (imageHandle != 0) OS.gtk_widget_modify_font (imageHandle, font);
}

void setForegroundColor (GdkColor color) {
	OS.gtk_widget_modify_fg (handle, 0, color);
	if (labelHandle != 0) OS.gtk_widget_modify_fg (labelHandle, 0, color);
	if (imageHandle != 0) OS.gtk_widget_modify_fg (imageHandle, 0, color);
}

/**
 * Sets the receiver's hot image to the argument, which may be
 * null indicating that no hot image should be displayed.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @param image the hot image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHotImage (Image image) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	hotImage = image;
}

public void setImage (Image image) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	if (imageHandle == 0) return;
	if (image != null) {
		OS.gtk_image_set_from_pixmap (imageHandle, image.pixmap, image.mask);
		OS.gtk_widget_show (imageHandle);
	} else {
		OS.gtk_image_set_from_pixmap (imageHandle, 0, 0);
		OS.gtk_widget_hide (imageHandle);
	}
}

/**
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button).
 * </p>
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	blockSignal (handle, SWT.Selection);
	OS.gtk_toggle_button_set_active (handle, selected);
	unblockSignal (handle, SWT.Selection);
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setText (string);
	if (labelHandle == 0) return;
	char [] chars = fixMnemonic (string);
	byte [] buffer = Converter.wcsToMbcs (null, chars, false);
	OS.gtk_label_set_text_with_mnemonic (labelHandle, buffer);
	if (string.length () != 0) {
		OS.gtk_widget_show (labelHandle);
	} else {
		OS.gtk_widget_hide (labelHandle);
	}
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget();
	toolTipText = string;
	byte [] buffer = null;
	if (string != null && string.length () > 0) {
		buffer = Converter.wcsToMbcs (null, string, true);
	}
	int tooltipsHandle = parent.tooltipsHandle ();
	OS.gtk_tooltips_set_tip (tooltipsHandle, handle, buffer, null);
}

/**
 * Sets the width of the receiver.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget();
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < 0) return;
	OS.gtk_widget_set_size_request (handle, width, -1);
	resizeControl ();
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
}
}

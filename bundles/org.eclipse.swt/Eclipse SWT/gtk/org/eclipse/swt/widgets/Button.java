/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a selectable user interface object that
 * issues notification when pressed and released. 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ARROW, CHECK, PUSH, RADIO, TOGGLE, FLAT</dd>
 * <dd>UP, DOWN, LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ARROW, CHECK, PUSH, RADIO, and TOGGLE 
 * may be specified.
 * </p><p>
 * Note: Only one of the styles LEFT, RIGHT, and CENTER may be specified.
 * </p><p>
 * Note: Only one of the styles UP, DOWN, LEFT, and RIGHT may be specified
 * when the ARROW style is specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class Button extends Control {
	int boxHandle, labelHandle, imageHandle, arrowHandle, groupHandle;
	boolean selected;
	Image image;
	String text;

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
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
 * @see SWT#ARROW
 * @see SWT#CHECK
 * @see SWT#PUSH
 * @see SWT#RADIO
 * @see SWT#TOGGLE
 * @see SWT#FLAT
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Button (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, 0);
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		return checkBits (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
	}
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		return checkBits (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
	}
	if ((style & SWT.ARROW) != 0) {
		style |= SWT.NO_FOCUS;
		return checkBits (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
	}
	return style;
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the control is selected.
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	int width = OS.GTK_WIDGET_WIDTH (fixedHandle);
	int height = OS.GTK_WIDGET_HEIGHT (fixedHandle);
	OS.gtk_widget_set_size_request (handle, wHint, hHint);
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (handle, requisition);
	OS.gtk_widget_set_size_request (handle, width, height);
	width = wHint == SWT.DEFAULT ? requisition.width : wHint;
	height = hHint == SWT.DEFAULT ? requisition.height : hHint;
	return new Point (width, height);	
}

void createHandle (int index) {
	state |= HANDLE;
	int bits = SWT.ARROW | SWT.TOGGLE | SWT.CHECK | SWT.RADIO | SWT.PUSH;
	fixedHandle = OS.gtk_fixed_new ();
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	switch (style & bits) {
		case SWT.ARROW:
			int arrow_type = OS.GTK_ARROW_UP;
			if ((style & SWT.UP) != 0) arrow_type = OS.GTK_ARROW_UP;
			if ((style & SWT.DOWN) != 0) arrow_type = OS.GTK_ARROW_DOWN;
            if ((style & SWT.LEFT) != 0) arrow_type = OS.GTK_ARROW_LEFT;
            if ((style & SWT.RIGHT) != 0) arrow_type = OS.GTK_ARROW_RIGHT;
			handle = OS.gtk_button_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			arrowHandle = OS.gtk_arrow_new (arrow_type, OS.GTK_SHADOW_OUT);
			if (arrowHandle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.TOGGLE:
			handle = OS.gtk_toggle_button_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.CHECK:
			handle = OS.gtk_check_button_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.RADIO:
			groupHandle = OS.gtk_radio_button_new (0);
			if (groupHandle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.g_object_ref (groupHandle);
			OS.gtk_object_sink (groupHandle);
			handle = OS.gtk_radio_button_new (OS.gtk_radio_button_get_group (groupHandle));
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.PUSH:
		default:
			handle = OS.gtk_button_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.GTK_WIDGET_SET_FLAGS(handle, OS.GTK_CAN_DEFAULT);
			break;
	}
	if ((style & SWT.ARROW) != 0) {
		OS.gtk_container_add (handle, arrowHandle);
		OS.gtk_widget_show (arrowHandle);
	} else {
		boxHandle = OS.gtk_hbox_new (false, 0);
		if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
		labelHandle = OS.gtk_label_new_with_mnemonic (null);
		if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
		imageHandle = OS.gtk_image_new ();
		if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (handle, boxHandle);
		OS.gtk_container_add (boxHandle, labelHandle);
		OS.gtk_container_add (boxHandle, imageHandle);
		OS.gtk_widget_show (boxHandle);
		OS.gtk_widget_show (labelHandle);
	}
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_add (parentHandle, fixedHandle);
	OS.gtk_container_add (fixedHandle, handle);
	OS.gtk_widget_show (fixedHandle);
	OS.gtk_widget_show (handle);
	
	if ((style & SWT.ARROW) != 0) return;
	if ((style & SWT.LEFT) != 0) {
		OS.gtk_misc_set_alignment (labelHandle, 0.0f, 0.5f);
		OS.gtk_label_set_justify (labelHandle, OS.GTK_JUSTIFY_LEFT);
		OS.gtk_misc_set_alignment (imageHandle, 0.0f, 0.5f);
		return;
	}
	if ((style & SWT.CENTER) != 0) {
		OS.gtk_misc_set_alignment (labelHandle, 0.5f, 0.5f);
		OS.gtk_label_set_justify (labelHandle, OS.GTK_JUSTIFY_CENTER);
		OS.gtk_misc_set_alignment (imageHandle, 0.5f, 0.5f);
		return;
	}
	if ((style & SWT.RIGHT) != 0) {
		OS.gtk_misc_set_alignment (labelHandle, 1.0f, 0.5f);
		OS.gtk_label_set_justify (labelHandle, OS.GTK_JUSTIFY_RIGHT);
		OS.gtk_misc_set_alignment (imageHandle, 1.0f, 0.5f);
		return;
	}
}

void createWidget (int index) {
	super.createWidget (index);
	text = "";
}

void deregister () {
	super.deregister ();
	if (boxHandle != 0) display.removeWidget (boxHandle);
	if (labelHandle != 0) display.removeWidget (labelHandle);
	if (imageHandle != 0) display.removeWidget (imageHandle);
	if (arrowHandle != 0) display.removeWidget (arrowHandle);
}

int fontHandle () {
	if (labelHandle != 0) return labelHandle;
	return super.fontHandle ();
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in 
 * which case, the alignment will indicate the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>, 
 * <code>UP</code> or <code>DOWN</code>).
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) {
		if ((style & SWT.UP) != 0) return SWT.UP;
		if ((style & SWT.DOWN) != 0) return SWT.DOWN;
		if ((style & SWT.LEFT) != 0) return SWT.LEFT;
		if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
		return SWT.UP;
	}
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

/**
 * Returns the receiver's image if it has one, or null
 * if it does not.
 *
 * @return the receiver's image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage () {
	checkWidget ();
	return image;
}

String getNameText () {
	return getText ();
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed in. If the receiver is of any other type,
 * this method returns false.
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	return OS.gtk_toggle_button_get_active (handle);
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget();
	if ((style & SWT.ARROW) != 0) return "";
	return text;
}

int gtk_button_press_event (int widget, int event) {
	int result = super.gtk_button_press_event (widget, event);
	if (result != 0) return result;
	if ((style & SWT.RADIO) != 0) selected  = getSelection ();
	return result;
}

int gtk_clicked (int widget) {
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) != 0) {
			setSelection (!selected);
		} else {
			selectRadio ();
		}
	}
	postEvent (SWT.Selection);
	return 0;
}

int gtk_focus_in_event (int widget, int event) {
	int result = super.gtk_focus_in_event (widget, event);
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if ((style & SWT.PUSH) != 0 && OS.GTK_WIDGET_HAS_DEFAULT (handle)) {
		Decorations menuShell = menuShell ();
		menuShell.defaultButton = this;
	}
	return result;
}

int gtk_focus_out_event (int widget, int event) {
	int result = super.gtk_focus_out_event (widget, event);
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if ((style & SWT.PUSH) != 0 && !OS.GTK_WIDGET_HAS_DEFAULT (handle)) {
		Decorations menuShell = menuShell ();
		if (menuShell.defaultButton == this) {
			menuShell.defaultButton = null;
		}
	}
	return result;
}

int gtk_key_press_event (int widget, int event) {
	int result = super.gtk_key_press_event (widget, event);
	if (result != 0) return result;
	if ((style & SWT.RADIO) != 0) selected  = getSelection ();
	return result;
}

void hookEvents () {
	super.hookEvents();
	OS.g_signal_connect (handle, OS.clicked, display.windowProc2, CLICKED);
	if (labelHandle != 0) {
		OS.g_signal_connect (labelHandle, OS.mnemonic_activate, display.windowProc3, MNEMONIC_ACTIVATE);
	}
}

boolean mnemonicHit (char key) {
	if (labelHandle == 0) return false;
	boolean result = super.mnemonicHit (labelHandle, key);
	if (result) setFocus ();
	return result;
}

boolean mnemonicMatch (char key) {
	if (labelHandle == 0) return false;
	return mnemonicMatch (labelHandle, key);
}

void register () {
	super.register ();
	if (boxHandle != 0) display.addWidget (boxHandle, this);
	if (labelHandle != 0) display.addWidget (labelHandle, this);
	if (imageHandle != 0) display.addWidget (imageHandle, this);
	if (arrowHandle != 0) display.addWidget (arrowHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	boxHandle = imageHandle = labelHandle = arrowHandle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	if (groupHandle != 0) OS.g_object_unref (groupHandle);
	groupHandle = 0;
	image = null;
	text = null;
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

void selectRadio () {
	/*
	* This code is intentionally commented.  When two groups
	* of radio buttons with the same parent are separated by
	* another control, the correct behavior should be that
	* the two groups act independently.  This is consistent
	* with radio tool and menu items.  The commented code
	* implements this behavior.
	*/
//	int index = 0;
//	Control [] children = parent._getChildren ();
//	while (index < children.length && children [index] != this) index++;
//	int i = index - 1;
//	while (i >= 0 && children [i].setRadioSelection (false)) --i;
//	int j = index + 1;
//	while (j < children.length && children [j].setRadioSelection (false)) j++;
//	setSelection (true);
	Control [] children = parent._getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (this != child) child.setRadioSelection (false);
	}
	setSelection (true);
}

/**
 * Controls how text, images and arrows will be displayed
 * in the receiver. The argument should be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in 
 * which case, the argument indicates the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>, 
 * <code>UP</code> or <code>DOWN</code>).
 *
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return; 
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		int arrow_type = OS.GTK_ARROW_UP;
		boolean isRTL = (style & SWT.RIGHT_TO_LEFT) != 0;
		switch (alignment) {
			case SWT.UP: arrow_type = OS.GTK_ARROW_UP; break;
			case SWT.DOWN: arrow_type = OS.GTK_ARROW_DOWN; break;
			case SWT.LEFT: arrow_type = isRTL? OS.GTK_ARROW_RIGHT : OS.GTK_ARROW_LEFT; break;
			case SWT.RIGHT: arrow_type = isRTL? OS.GTK_ARROW_LEFT : OS.GTK_ARROW_RIGHT; break;
		}
		OS.gtk_arrow_set (arrowHandle, arrow_type, OS.GTK_SHADOW_OUT);
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	if ((style & SWT.LEFT) != 0) {
		OS.gtk_misc_set_alignment (labelHandle, 0.0f, 0.5f);
		OS.gtk_label_set_justify (labelHandle, OS.GTK_JUSTIFY_LEFT);
		OS.gtk_misc_set_alignment (imageHandle, 0.0f, 0.5f);
		return;
	}
	if ((style & SWT.CENTER) != 0) {
		OS.gtk_misc_set_alignment (labelHandle, 0.5f, 0.5f);
		OS.gtk_label_set_justify (labelHandle, OS.GTK_JUSTIFY_CENTER);
		OS.gtk_misc_set_alignment (imageHandle, 0.5f, 0.5f);
		return;
	}
	if ((style & SWT.RIGHT) != 0) {
		OS.gtk_misc_set_alignment (labelHandle, 1.0f, 0.5f);
		OS.gtk_label_set_justify (labelHandle, OS.GTK_JUSTIFY_RIGHT);
		OS.gtk_misc_set_alignment (imageHandle, 1.0f, 0.5f);
		return;
	}
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	setBackgroundColor(fixedHandle, color);
	if (labelHandle != 0) setBackgroundColor(labelHandle, color);
	if (imageHandle != 0) setBackgroundColor(imageHandle, color);
}

boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) { 
	boolean result = super.setBounds (x, y, width, height, move, resize);
	/*
	* Feature in GTK, GtkCheckButton and GtkRadioButton allocate
	* only the minimum size necessary for its child. This causes the child
	* alignment to fail. The fix is to set the child size to all available space
	* excluding trimmings.
	*/
	if (result && (style & (SWT.CHECK | SWT.RADIO)) != 0) {
		int childHeight = 0, buttonWidth = 0, buttonHeight = 0;
		GtkRequisition requisition = new GtkRequisition ();
		OS.gtk_widget_size_request (handle, requisition);
		buttonWidth = requisition.width;
		buttonHeight = requisition.height;
		OS.gtk_widget_size_request (boxHandle, requisition);
		childHeight = requisition.height;
		OS.gtk_widget_set_size_request (handle, -1, -1);
		OS.gtk_widget_set_size_request (boxHandle, -1, -1);
		OS.gtk_widget_size_request (handle, requisition);
		int trim = requisition.width;
		OS.gtk_widget_size_request (boxHandle, requisition);
		trim -= requisition.width;
		OS.gtk_widget_set_size_request (handle, buttonWidth, buttonHeight);
		OS.gtk_widget_set_size_request (boxHandle, Math.max (1, width - trim), childHeight);
	}
	return result;
}
	
void setFontDescription (int font) {
	super.setFontDescription (font);
	if (labelHandle != 0) OS.gtk_widget_modify_font (labelHandle, font);
	if (imageHandle != 0) OS.gtk_widget_modify_font (imageHandle, font);
}

boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		postEvent (SWT.Selection);
	}
	return true;
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	OS.gtk_widget_modify_fg (fixedHandle, OS.GTK_STATE_NORMAL, color);
	if (labelHandle != 0) OS.gtk_widget_modify_fg (labelHandle,  OS.GTK_STATE_NORMAL, color);
	if (imageHandle != 0) OS.gtk_widget_modify_fg (imageHandle,  OS.GTK_STATE_NORMAL, color);
}

/**
 * Sets the receiver's image to the argument, which may be
 * null indicating that no image should be displayed.
 *
 * @param image the image to display on the receiver (may be null)
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
	if ((style & SWT.ARROW) != 0) return;
	if (image != null) {
		if (image.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		OS.gtk_image_set_from_pixmap (imageHandle, image.pixmap, image.mask);
		OS.gtk_widget_hide (labelHandle);
		OS.gtk_widget_show (imageHandle);
	} else {
		OS.gtk_image_set_from_pixmap (imageHandle, 0, 0);
		OS.gtk_widget_show (labelHandle);
		OS.gtk_widget_hide (imageHandle);
	}
	this.image = image;
	/*
	* Bug in GTK.  For some reason, the button does not allocate the size of its internal
	* children if its bounds is set before the image is set.  The fix is to force this by calling
	* gtk_widget_size_request() (and throw the results away).
	*/
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (handle, requisition);
}

void setOrientation () {
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		OS.gtk_widget_set_direction (handle, OS.GTK_TEXT_DIR_RTL);
		if (labelHandle != 0) OS.gtk_widget_set_direction (labelHandle, OS.GTK_TEXT_DIR_RTL);
		if (imageHandle != 0) OS.gtk_widget_set_direction (imageHandle, OS.GTK_TEXT_DIR_RTL);
		if (arrowHandle != 0) {
			switch (style & (SWT.LEFT | SWT.RIGHT)) {
				case SWT.LEFT: OS.gtk_arrow_set (arrowHandle, OS.GTK_ARROW_RIGHT, OS.GTK_SHADOW_OUT); break;
				case SWT.RIGHT: OS.gtk_arrow_set (arrowHandle, OS.GTK_ARROW_LEFT, OS.GTK_SHADOW_OUT); break;
			}
		}
	}
}

/**
 * Sets the selection state of the receiver, if it is of type <code>CHECK</code>, 
 * <code>RADIO</code>, or <code>TOGGLE</code>.
 *
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed in.
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CLICKED);
	OS.gtk_toggle_button_set_active (handle, selected);
	if ((style & SWT.RADIO) != 0) OS.gtk_toggle_button_set_active (groupHandle, !selected);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CLICKED);
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the button label.  The label may include
 * the mnemonic character but must not contain line delimiters.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp' can be
 * escaped by doubling it in the string, causing a single
 *'&amp' to be displayed.
 * </p>
 * 
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	text = string;
	char [] chars = fixMnemonic (string);
	byte [] buffer = Converter.wcsToMbcs (null, chars, true);
	OS.gtk_label_set_text_with_mnemonic (labelHandle, buffer);
	OS.gtk_widget_hide (imageHandle);
	OS.gtk_widget_show (labelHandle);
	/*
	* Bug in GTK.  For some reason, the button does not allocate the size of its internal
	* children if its bounds is set before the text is set.  The fix is to force this by calling
	* gtk_widget_size_request() (and throw the results away).
	*/
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (handle, requisition);
}

int traversalCode (int key, GdkEventKey event) {
	int code = super.traversalCode (key, event);
	if ((style & SWT.PUSH) != 0) return code;
	return code | SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS;
}

}

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


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a column in a table widget.
 *  <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd> Move, Resize, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles LEFT, RIGHT and CENTER may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class TableColumn extends Item {
	int boxHandle, labelHandle, imageHandle, buttonHandle;
	Table parent;
	int modelIndex, lastButton, lastTime, lastX, lastWidth;
	boolean customDraw;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>) and a style value
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
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableColumn (Table parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (parent.getColumnCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>), a style value
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
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableColumn (Table parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is moved or resized, by sending
 * it one of the messages defined in the <code>ControlListener</code>
 * interface.
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
 * @see ControlListener
 * @see #removeControlListener
 */
public void addControlListener(ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the column header is selected.
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

void createWidget (int index) {
	parent.createItem (this, index);
	hookEvents ();
	register ();
	text = "";
}

void deregister() {
	super.deregister ();
	display.removeWidget (handle);
	if (buttonHandle != 0) display.removeWidget (buttonHandle);
	if (labelHandle != 0) display.removeWidget (labelHandle);
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>.
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget();
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

/**
 * Returns the receiver's parent, which must be a <code>Table</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Table getParent () {
	checkWidget();
	return parent;
}

/**
 * Gets the resizable attribute. A column that is
 * not resizable cannot be dragged by the user but
 * may be resized by the programmer.
 *
 * @return the resizable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getResizable () {
	checkWidget();
	return OS.gtk_tree_view_column_get_resizable (handle);
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
	if (!OS.gtk_tree_view_column_get_visible (handle)) {
		return 0;
	} 
	return OS.gtk_tree_view_column_get_width (handle);
}

int gtk_clicked (int widget) {
	/*
	* There is no API to get a double click on a table column.  Normally, when
	* the mouse is double clicked, this is indicated by GDK_2BUTTON_PRESS
	* but the table column sends the click signal on button release.  The fix is to
	* test for doublc click by remembering the last click time and mouse button
	* and testing for the double click interval.
	*/
	boolean doubleClick = false;
	int eventPtr = OS.gtk_get_current_event ();
	if (eventPtr != 0) {
		GdkEventButton gdkEvent = new GdkEventButton ();
		OS.memmove (gdkEvent, eventPtr, GdkEventButton.sizeof);
		switch (gdkEvent.type) {
			case OS.GDK_BUTTON_RELEASE: {
				int clickTime = display.getDoubleClickTime ();
				int eventTime = gdkEvent.time, eventButton = gdkEvent.button;
				if (lastButton == eventButton && lastTime != 0 && Math.abs (lastTime - eventTime) <= clickTime) {
					doubleClick = true;
				}
				lastTime = eventTime == 0 ? 1: eventTime;
				lastButton = eventButton;
			}
		}
		OS.gdk_event_free (eventPtr);
	}
	postEvent (doubleClick ? SWT.DefaultSelection : SWT.Selection);
	return 0;
}

int gtk_mnemonic_activate (int widget, int arg1) {
	return parent.gtk_mnemonic_activate (widget, arg1);
}

int gtk_size_allocate (int widget, int allocation) {
	boolean mapped = OS.GTK_WIDGET_MAPPED (widget); 
	int x = OS.GTK_WIDGET_X (widget);
	int width = OS.GTK_WIDGET_WIDTH (widget);
	if (width != lastWidth) {
		lastWidth = width;
		if (mapped) sendEvent (SWT.Resize);
	}
	if (x != lastX) {
		lastX = x;
		if (mapped) sendEvent (SWT.Move);
	}
	return 0;
}

void hookEvents () {
	super.hookEvents ();
	OS.g_signal_connect (handle, OS.clicked, display.windowProc2, CLICKED);
	if (buttonHandle != 0) OS.g_signal_connect (buttonHandle, OS.size_allocate, display.windowProc3, SIZE_ALLOCATE);
	if (labelHandle != 0) OS.g_signal_connect (labelHandle, OS.mnemonic_activate, display.windowProc3, MNEMONIC_ACTIVATE);
}

/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 */
public void pack () {
	checkWidget();
	/*
	* Feature in GTK.  Calling gtk_tree_view_column_set_sizing() with 
	* GTK_TREE_VIEW_COLUMN_AUTOSIZE causes the tree view
	* column to be not resizable.  The fix is to reset this afterawards.
	*/ 
	boolean resizable = OS.gtk_tree_view_column_get_resizable (handle);
	OS.gtk_tree_view_column_set_sizing (handle, OS.GTK_TREE_VIEW_COLUMN_AUTOSIZE);
	OS.gtk_tree_view_column_set_resizable (handle, resizable);
	OS.gtk_tree_view_column_set_visible (handle, true);
}

void register () {
	super.register ();
	display.addWidget (handle, this);
	if (buttonHandle != 0) display.addWidget (buttonHandle, this);
	if (labelHandle != 0) display.addWidget (labelHandle, this);
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
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

/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.
 *
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget();
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	int index = parent.indexOf (this);
	if (index == -1 || index == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	parent.createRenderers (handle, modelIndex, index == 0, style);
}

void setFontDescription (int font) {
	OS.gtk_widget_modify_font (labelHandle, font);
	OS.gtk_widget_modify_font (imageHandle, font);
}

public void setImage (Image image) {
	checkWidget ();
	super.setImage (image);
	if (image != null) {
		OS.gtk_image_set_from_pixmap (imageHandle, image.pixmap, image.mask);
		OS.gtk_widget_show (imageHandle);
	} else {
		OS.gtk_image_set_from_pixmap (imageHandle, 0, 0);
		OS.gtk_widget_hide (imageHandle);
	}

	/*
	* Bug in GTK.  For some reason, the column button does not allocate the size 
	* of its internal children if its bounds is set before the image is set.  The fix is to
	* force this by calling gtk_widget_size_request() (and throw the results away).
	*/
	if (buttonHandle != 0) {
		GtkRequisition requisition = new GtkRequisition ();
		OS.gtk_widget_size_request (buttonHandle, requisition);
	}
}

/**
 * Sets the resizable attribute.  A column that is
 * not resizable cannot be dragged by the user but
 * may be resized by the programmer.
 *
 * @param resizable the resize attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setResizable (boolean resizable) {
	checkWidget();
	OS.gtk_tree_view_column_set_resizable (handle, resizable);
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	char [] chars = fixMnemonic (string);
	byte [] buffer = Converter.wcsToMbcs (null, chars, true);
	OS.gtk_label_set_text_with_mnemonic (labelHandle, buffer);
	if (string.length () != 0) {
		OS.gtk_widget_show (labelHandle);
	} else {
		OS.gtk_widget_hide (labelHandle);
	}

	/*
	* Bug in GTK.  For some reason, the column button does not allocate the size
	* of its internal children if its bounds is set before the text is set.  The fix is to 
	* force this by calling gtk_widget_size_request() (and throw the results away).
	*/
	if (buttonHandle != 0) {
		GtkRequisition requisition = new GtkRequisition ();
		OS.gtk_widget_size_request (buttonHandle, requisition);
	}
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
	OS.gtk_tree_view_column_set_sizing (handle, OS.GTK_TREE_VIEW_COLUMN_FIXED);
	if (width > 0) {
		OS.gtk_tree_view_column_set_fixed_width (handle, width);
		OS.gtk_tree_view_column_set_visible (handle, true);
	} else {
		OS.gtk_tree_view_column_set_visible (handle, false);
	}
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a expandable item in a expand bar.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see ExpandBar
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.2
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ExpandItem extends Item {
	ExpandBar parent;
	Control control;
	ImageList imageList;
	int /*long*/ clientHandle, boxHandle, labelHandle, imageHandle;
	boolean expanded;
	int x, y, width, height;
	int imageHeight, imageWidth;
	static final int TEXT_INSET = 6;
	static final int BORDER = 1;
	static final int CHEVRON_SIZE = 24;

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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ExpandItem (ExpandBar parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (parent.getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent, a
 * style value describing its behavior and appearance, and the index
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
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ExpandItem (ExpandBar parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	createWidget (index);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void createHandle (int index) {
	state |= HANDLE;
	handle = OS.gtk_expander_new (null);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	clientHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (clientHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_container_add (handle, clientHandle);	
	boxHandle = OS.gtk_hbox_new (false, 4);
	if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	labelHandle = OS.gtk_label_new (null);
	if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	imageHandle = OS.gtk_image_new ();
	if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_container_add (boxHandle, imageHandle);
	OS.gtk_container_add (boxHandle, labelHandle);
	OS.gtk_expander_set_label_widget (handle, boxHandle);
	gtk_widget_set_can_focus (handle, true);
}

void createWidget (int index) {
	super.createWidget (index);
	showWidget (index);
	parent.createItem (this, style, index);
}

void deregister() {
	super.deregister();
	display.removeWidget (clientHandle);
	display.removeWidget (boxHandle);
	display.removeWidget (labelHandle);
	display.removeWidget (imageHandle);
}

void destroyWidget () {
	parent.destroyItem (this);
	super.destroyWidget ();
}

void drawChevron (GC gc, int x, int y) {
	int [] polyline1, polyline2;
	if (expanded) {
		int px = x + 4 + 5;
		int py = y + 4 + 7;
		polyline1 = new int [] {
				px,py, px+1,py, px+1,py-1, px+2,py-1, px+2,py-2, px+3,py-2, px+3,py-3,
				px+3,py-2, px+4,py-2, px+4,py-1, px+5,py-1, px+5,py, px+6,py};
		py += 4;
		polyline2 = new int [] {
				px,py, px+1,py, px+1,py-1, px+2,py-1, px+2,py-2, px+3,py-2, px+3,py-3,
				px+3,py-2, px+4,py-2, px+4,py-1,  px+5,py-1, px+5,py, px+6,py};
	} else {
		int px = x + 4 + 5;
		int py = y + 4 + 4;
		polyline1 = new int[] {
				px,py, px+1,py, px+1,py+1, px+2,py+1, px+2,py+2, px+3,py+2, px+3,py+3,
				px+3,py+2, px+4,py+2, px+4,py+1,  px+5,py+1, px+5,py, px+6,py};
		py += 4;
		polyline2 = new int [] {
				px,py, px+1,py, px+1,py+1, px+2,py+1, px+2,py+2, px+3,py+2, px+3,py+3,
				px+3,py+2, px+4,py+2, px+4,py+1,  px+5,py+1, px+5,py, px+6,py};
	}
	gc.setForeground (display.getSystemColor (SWT.COLOR_TITLE_FOREGROUND));
	gc.drawPolyline (polyline1);
	gc.drawPolyline (polyline2);
}

void drawItem (GC gc, boolean drawFocus) {
	int headerHeight = parent.getBandHeight ();
	Display display = getDisplay ();
	gc.setForeground (display.getSystemColor (SWT.COLOR_TITLE_BACKGROUND));
	gc.setBackground (display.getSystemColor (SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
	gc.fillGradientRectangle (x, y, width, headerHeight, true);
	if (expanded) {
		gc.setForeground (display.getSystemColor (SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		gc.drawLine (x, y + headerHeight, x, y + headerHeight + height - 1);
		gc.drawLine (x, y + headerHeight + height - 1, x + width - 1, y + headerHeight + height - 1);
		gc.drawLine (x + width - 1, y + headerHeight + height - 1, x + width - 1, y + headerHeight);
	}
	int drawX = x;
	if (image != null) {
		drawX += ExpandItem.TEXT_INSET;
		if (imageHeight > headerHeight) {
			gc.drawImage (image, drawX, y + headerHeight - imageHeight);
		} else {
			gc.drawImage (image, drawX, y + (headerHeight - imageHeight) / 2);
		}
		drawX += imageWidth;
	}
	if (text.length() > 0) {
		drawX += ExpandItem.TEXT_INSET;
		Point size = gc.stringExtent (text);
		gc.setForeground (parent.getForeground ());
		gc.drawString (text, drawX, y + (headerHeight - size.y) / 2, true);
	}
	int chevronSize = ExpandItem.CHEVRON_SIZE;
	drawChevron (gc, x + width - chevronSize, y + (headerHeight - chevronSize) / 2);
	if (drawFocus) {
		gc.drawFocus (x + 1, y + 1, width - 2, headerHeight - 2);
	}
}

/**
 * Returns the control that is shown when the item is expanded.
 * If no control has been set, return <code>null</code>.
 *
 * @return the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getControl () {
	checkWidget ();
	return control;
}

/**
 * Returns <code>true</code> if the receiver is expanded,
 * and false otherwise.
 *
 * @return the expanded state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getExpanded () {
	checkWidget ();
	return expanded;
}

/**
 * Returns the height of the receiver's header 
 *
 * @return the height of the header 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getHeaderHeight () {
	checkWidget ();
	GtkAllocation allocation = new GtkAllocation ();
	gtk_widget_get_allocation (handle, allocation);
	return allocation.height - (expanded ? height : 0);
}

/**
 * Gets the height of the receiver.
 *
 * @return the height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getHeight () {
	checkWidget ();
	return height;
}

/**
 * Returns the receiver's parent, which must be a <code>ExpandBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ExpandBar getParent () {
	checkWidget();
	return parent;
}

int getPreferredWidth (GC gc) {
	int width = ExpandItem.TEXT_INSET * 2 + ExpandItem.CHEVRON_SIZE;
	if (image != null) {
		width += ExpandItem.TEXT_INSET + imageWidth;
	}
	if (text.length() > 0) {
		width += gc.stringExtent (text).x;
	}
	return width;
}

int /*long*/ gtk_activate (int /*long*/ widget) {
	Event event = new Event ();
	event.item = this;
	int type = OS.gtk_expander_get_expanded (handle) ? SWT.Collapse : SWT.Expand;
	parent.sendEvent (type, event);
	return 0;
}

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ event) {
	setFocus ();
	return 0;
}

int /*long*/ gtk_focus_out_event (int /*long*/ widget, int /*long*/ event) {
	gtk_widget_set_can_focus (handle, false);
	parent.lastFocus = this;
	return 0;
}

int /*long*/ gtk_size_allocate (int /*long*/ widget, int /*long*/ allocation) {
	parent.layoutItems (0, false);
	return 0;
}

int /*long*/ gtk_enter_notify_event (int /*long*/ widget, int /*long*/ event) {
	parent.gtk_enter_notify_event(widget, event);
	return 0;
}

boolean hasFocus () {
	return gtk_widget_has_focus (handle);
}

void hookEvents () {
	super.hookEvents ();
	OS.g_signal_connect_closure (handle, OS.activate, display.closures [ACTIVATE], false);
	OS.g_signal_connect_closure (handle, OS.activate, display.closures [ACTIVATE_INVERSE], true);
	OS.g_signal_connect_closure_by_id (handle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.closures [BUTTON_PRESS_EVENT], false);
	OS.g_signal_connect_closure_by_id (handle, display.signalIds [FOCUS_OUT_EVENT], 0, display.closures [FOCUS_OUT_EVENT], false);
	OS.g_signal_connect_closure (clientHandle, OS.size_allocate, display.closures [SIZE_ALLOCATE], true);
	OS.g_signal_connect_closure_by_id (handle, display.signalIds [ENTER_NOTIFY_EVENT], 0, display.closures [ENTER_NOTIFY_EVENT], false);
}

void redraw () {
}

void register () {
	super.register ();
	display.addWidget (clientHandle, this);
	display.addWidget (boxHandle, this);
	display.addWidget (labelHandle, this);
	display.addWidget (imageHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	clientHandle = boxHandle = labelHandle = imageHandle = 0;
	parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
	if (imageList != null) imageList.dispose ();
	if (parent.lastFocus == this) parent.lastFocus = null;
	imageList = null;
	control = null;
}

void resizeControl (int yScroll) {
	if (control != null && !control.isDisposed ()) {
		boolean visible = OS.gtk_expander_get_expanded (handle);
		if (visible) {
			GtkAllocation allocation = new GtkAllocation ();
			gtk_widget_get_allocation (clientHandle, allocation);
			int x = allocation.x;
			int y = allocation.y;
			if (x != -1 && y != -1) {
				int width = allocation.width;
				int height = allocation.height;	
				int [] property = new int [1];
				OS.gtk_widget_style_get (handle, OS.focus_line_width, property, 0);				
				y += property [0] * 2;
				height -= property [0] * 2;
				
				/*
				* Feature in GTK. When the ExpandBar is resize too small the control
				* shows up on top of the vertical scrollbar. This happen because the 
				* GtkExpander does not set the size of child smaller than the request
				* size of its parent and because the control is not parented in the 
				* hierarchy of the GtkScrolledWindow.
				* The fix is calculate the width ourselves when the scrollbar is visible.
				*/
				ScrollBar vBar = parent.verticalBar;
				if (vBar != null) {
					if (gtk_widget_get_visible (vBar.handle)) {
						gtk_widget_get_allocation (parent.scrolledHandle, allocation);
						width = allocation.width - parent.vScrollBarWidth () - 2 * parent.spacing;	
					}
				}
				control.setBounds (x, y - yScroll, width, Math.max (0, height), true, true);
			}
		}
		control.setVisible (visible);
	}
}

void setBounds (int x, int y, int width, int height, boolean move, boolean size) {
	redraw ();
	int headerHeight = parent.getBandHeight ();
	if (move) {
		if (imageHeight > headerHeight) {
			y += (imageHeight - headerHeight);
		}
		this.x = x;
		this.y = y;
		redraw ();
	}
	if (size) {
		this.width = width;
		this.height = height;
		redraw ();
	}
	if (control != null && !control.isDisposed ()) {
		if (move) control.setLocation (x + BORDER, y + headerHeight);
		if (size) control.setSize (Math.max (0, width - 2 * BORDER), Math.max (0, height - BORDER));
	}
}

/**
 * Sets the control that is shown when the item is expanded.
 *
 * @param control the new control (or null)
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
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if (this.control == control) return;
	this.control = control;
	if (control != null) {
		control.setVisible (expanded);
	}
	parent.layoutItems (0, true);
}

/**
 * Sets the expanded state of the receiver.
 *
 * @param expanded the new expanded state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setExpanded (boolean expanded) {
	checkWidget ();
	this.expanded = expanded;
	OS.gtk_expander_set_expanded (handle, expanded);
	parent.layoutItems (0, true);
}

boolean setFocus () {
	if (!OS.gtk_widget_get_child_visible (handle)) return false;
	gtk_widget_set_can_focus (handle, true);
	OS.gtk_widget_grab_focus (handle);
	// widget could be disposed at this point
	if (isDisposed ()) return false;
	boolean result = OS.gtk_widget_is_focus (handle);
	if (!result) gtk_widget_set_can_focus (handle, false);
	return result;
}

void setFontDescription (int /*long*/ font) {
	OS.gtk_widget_modify_font (handle, font);
	if (labelHandle != 0) OS.gtk_widget_modify_font (labelHandle, font);
	if (imageHandle != 0) OS.gtk_widget_modify_font (imageHandle, font);
}

void setForegroundColor (GdkColor color) {
	setForegroundColor (handle, color);
	if (labelHandle != 0) setForegroundColor (labelHandle, color);
	if (imageHandle != 0) setForegroundColor (imageHandle, color);
}

/**
 * Sets the height of the receiver. This is height of the item when it is expanded, 
 * excluding the height of the header.
 *
 * @param height the new height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHeight (int height) {
	checkWidget ();
	if (height < 0) return;
	this.height = height;
	OS.gtk_widget_set_size_request (clientHandle, -1, height);
	parent.layoutItems (0, false);
}

public void setImage (Image image) {
	super.setImage (image);
	if (imageList != null) imageList.dispose ();
	imageList = null;
	if (image != null) {
		if (image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		imageList = new ImageList ();
		int imageIndex = imageList.add (image);
		int /*long*/ pixbuf = imageList.getPixbuf (imageIndex);
		OS.gtk_image_set_from_pixbuf (imageHandle, pixbuf);
		if (text.length () == 0) OS.gtk_widget_hide (labelHandle);
		OS.gtk_widget_show (imageHandle);
	} else {
		OS.gtk_image_set_from_pixbuf (imageHandle, 0);
		OS.gtk_widget_show (labelHandle);
		OS.gtk_widget_hide (imageHandle);
	}
}

void setOrientation (boolean create) {
	super.setOrientation (create);
	if ((parent.style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		int dir = (parent.style & SWT.RIGHT_TO_LEFT) != 0 ? OS.GTK_TEXT_DIR_RTL : OS.GTK_TEXT_DIR_LTR;
		OS.gtk_widget_set_direction (handle, dir);
		OS.gtk_container_forall (handle, display.setDirectionProc, dir);
	}
}
	
public void setText (String string) {
	super.setText (string);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	OS.gtk_label_set_text (labelHandle, buffer);
}

void showWidget (int index) {
	OS.gtk_widget_show (handle);
	OS.gtk_widget_show (clientHandle);
	OS.gtk_container_add (parent.handle, handle);
	OS.gtk_box_set_child_packing (parent.handle, handle, false, false, 0, OS.GTK_PACK_START);
	if (boxHandle != 0) OS.gtk_widget_show (boxHandle);
	if (labelHandle != 0) OS.gtk_widget_show (labelHandle);
}

int /*long*/ windowProc (int /*long*/ handle, int /*long*/ user_data) {
	switch ((int)/*64*/user_data) {
		case ACTIVATE_INVERSE: {
			expanded = OS.gtk_expander_get_expanded (handle);
			parent.layoutItems (0, false);
			return 0;
		}
	}
	return super.windowProc (handle, user_data);
}
}

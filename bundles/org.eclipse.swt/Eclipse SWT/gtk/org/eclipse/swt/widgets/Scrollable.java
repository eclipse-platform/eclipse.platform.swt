/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

/**
 * This class is the abstract superclass of all classes which
 * represent controls that have standard scroll bars.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>H_SCROLL, V_SCROLL</dd>
 * <dt><b>Events:</b>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public abstract class Scrollable extends Control {
	int /*long*/ scrolledHandle;
	ScrollBar horizontalBar, verticalBar;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Scrollable () {}

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
 * @see SWT#H_SCROLL
 * @see SWT#V_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Scrollable (Composite parent, int style) {
	super (parent, style);
}

int /*long*/ clientHandle () {
	return handle;
}

/**
 * Given a desired <em>client area</em> for the receiver
 * (as described by the arguments), returns the bounding
 * rectangle which would be required to produce that client
 * area.
 * <p>
 * In other words, it returns a rectangle such that, if the
 * receiver's bounds were set to that rectangle, the area
 * of the receiver which is capable of displaying data
 * (that is, not covered by the "trimmings") would be the
 * rectangle described by the arguments (relative to the
 * receiver's parent).
 * </p>
 * 
 * @param x the desired x coordinate of the client area
 * @param y the desired y coordinate of the client area
 * @param width the desired width of the client area
 * @param height the desired height of the client area
 * @return the required bounds to produce the given client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getClientArea
 */
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	int border = 0;
	if (fixedHandle != 0) border += OS.gtk_container_get_border_width (fixedHandle);
	if (scrolledHandle != 0) border += OS.gtk_container_get_border_width (scrolledHandle);
	int trimX = x - border, trimY = y - border;
	int trimWidth = width + (border * 2), trimHeight = height + (border * 2);
	trimHeight += hScrollBarWidth ();
	trimWidth  += vScrollBarWidth ();
	if (scrolledHandle != 0) {
		if (OS.gtk_scrolled_window_get_shadow_type (scrolledHandle) != OS.GTK_SHADOW_NONE) {
			int /*long*/ style = OS.gtk_widget_get_style (scrolledHandle);
			int xthickness = OS.gtk_style_get_xthickness (style);
			int ythickness = OS.gtk_style_get_ythickness (style);
			trimX -= xthickness;
			trimY -= ythickness;
			trimWidth += xthickness * 2;
			trimHeight += ythickness * 2;
		}
	}
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
}

ScrollBar createScrollBar (int style) {
	if (scrolledHandle == 0) return null;
	ScrollBar bar = new ScrollBar ();
	bar.parent = this;
	bar.style = style;
	bar.display = display;
	bar.state |= HANDLE;
	if ((style & SWT.H_SCROLL) != 0) {
		bar.handle = OS.gtk_scrolled_window_get_hadjustment (scrolledHandle);
	} else {
		bar.handle = OS.gtk_scrolled_window_get_vadjustment (scrolledHandle);
	}
	bar.hookEvents ();
	bar.register ();
	return bar;
}

void createWidget (int index) {
	super.createWidget (index);
	if ((style & SWT.H_SCROLL) != 0) horizontalBar = createScrollBar (SWT.H_SCROLL);
	if ((style & SWT.V_SCROLL) != 0) verticalBar = createScrollBar (SWT.V_SCROLL);
}

void deregister () {
	super.deregister ();
	if (scrolledHandle != 0) display.removeWidget (scrolledHandle);
}

public int getBorderWidth () {
	checkWidget();
	int border = 0;
	if (fixedHandle != 0) border += OS.gtk_container_get_border_width (fixedHandle);
	if (scrolledHandle != 0) {
		border += OS.gtk_container_get_border_width (scrolledHandle);
		if (OS.gtk_scrolled_window_get_shadow_type (scrolledHandle) != OS.GTK_SHADOW_NONE) {
			border += OS.gtk_style_get_xthickness (OS.gtk_widget_get_style (scrolledHandle));
		}
	}
	return border;
}

/**
 * Returns a rectangle which describes the area of the
 * receiver which is capable of displaying data (that is,
 * not covered by the "trimmings").
 * 
 * @return the client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeTrim
 */
public Rectangle getClientArea () {
	checkWidget ();
	//FIXME - List, Table, Tree, ...
	int /*long*/ clientHandle = clientHandle ();
	int width = OS.GTK_WIDGET_WIDTH (clientHandle);
	int height = OS.GTK_WIDGET_HEIGHT (clientHandle);
	if ((state & CANVAS) != 0) {
		return new Rectangle (0, 0, width, height);
	}
	int x = OS.GTK_WIDGET_X (clientHandle);
	int y = OS.GTK_WIDGET_Y (clientHandle);
	return new Rectangle (x, y, width, height);
}
/**
 * Returns the receiver's horizontal scroll bar if it has
 * one, and null if it does not.
 *
 * @return the horizontal scroll bar (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ScrollBar getHorizontalBar () {
	checkWidget ();
	return horizontalBar;
}
/**
 * Returns the receiver's vertical scroll bar if it has
 * one, and null if it does not.
 *
 * @return the vertical scroll bar (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ScrollBar getVerticalBar () {
	checkWidget ();
	return verticalBar;
}

int hScrollBarWidth() {
	if (horizontalBar==null) return 0;
	int /*long*/ hBarHandle = OS.GTK_SCROLLED_WINDOW_HSCROLLBAR(scrolledHandle);
	if (hBarHandle==0) return 0;
	GtkRequisition requisition = new GtkRequisition();
	OS.gtk_widget_size_request(hBarHandle, requisition);
	int spacing = OS.GTK_SCROLLED_WINDOW_SCROLLBAR_SPACING(scrolledHandle);
	return requisition.height + spacing;
}

void setOrientation () {
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		if (scrolledHandle != 0) {
			OS.gtk_scrolled_window_set_placement (scrolledHandle, OS.GTK_CORNER_TOP_RIGHT);
		}
	}
}

boolean setScrollBarVisible (ScrollBar bar, boolean visible) {
	if (scrolledHandle == 0) return false;
	int [] hsp = new int [1], vsp = new int [1];
	OS.gtk_scrolled_window_get_policy (scrolledHandle, hsp, vsp);
	int policy = visible ? OS.GTK_POLICY_ALWAYS : OS.GTK_POLICY_NEVER;
	if ((style & SWT.HORIZONTAL) != 0) {
		if (hsp [0] == policy) return false;
		hsp [0] = policy;
	} else {
		if (vsp [0] == policy) return false;
		vsp [0] = policy;
	}
	OS.gtk_scrolled_window_set_policy (scrolledHandle, hsp [0], vsp [0]);
	/*
	* Force the container to allocate the size of its children.
	*/
	OS.gtk_container_resize_children (scrolledHandle);
	bar.sendEvent (visible ? SWT.Show : SWT.Hide);
	sendEvent (SWT.Resize);
	return true;
}

void register () {
	super.register ();
	if (scrolledHandle != 0) display.addWidget (scrolledHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	scrolledHandle = 0;
}

void releaseWidget () {
	if (horizontalBar != null) horizontalBar.releaseResources ();
	if (verticalBar != null) verticalBar.releaseResources ();
	horizontalBar = verticalBar = null;
	super.releaseWidget ();
}

void resizeHandle (int width, int height) {
	if (fixedHandle != 0) {
		OS.gtk_widget_set_size_request (fixedHandle, width, height);
	}
	/*
	* Feature in GTK.  Some widgets do not allocate the size
	* of their internal children in gtk_widget_size_allocate().
	* Instead this is done in gtk_widget_size_request().  This
	* means that the client area of the widget is not correct.
	* The fix is to call gtk_widget_size_request() (and throw
	* the results away).
	*
	* Note: The following widgets rely on this feature:
	* 	GtkScrolledWindow
	* 	GtkNotebook
	* 	GtkFrame
	* 	GtkCombo
	*/
	GtkRequisition requisition = new GtkRequisition ();
	if (scrolledHandle != 0) {
		OS.gtk_widget_set_size_request (scrolledHandle, width, height);
		OS.gtk_widget_size_request (scrolledHandle, requisition);
	} else {
		OS.gtk_widget_set_size_request (handle, width, height);
		OS.gtk_widget_size_request (handle, requisition);
	}
}

void showWidget () {
	super.showWidget ();
	if (scrolledHandle != 0) OS.gtk_widget_show (scrolledHandle);
}

int /*long*/ topHandle () {
	if (fixedHandle != 0) return fixedHandle;
	if (scrolledHandle != 0) return scrolledHandle;
	return super.topHandle ();
}

int vScrollBarWidth() {
	if (verticalBar == null) return 0;
	int /*long*/ vBarHandle = OS.GTK_SCROLLED_WINDOW_VSCROLLBAR(scrolledHandle);
	if (vBarHandle == 0) return 0;
	GtkRequisition requisition = new GtkRequisition();
	OS.gtk_widget_size_request (vBarHandle, requisition);
	int spacing = OS.GTK_SCROLLED_WINDOW_SCROLLBAR_SPACING(scrolledHandle);
	return requisition.width + spacing;
}
}

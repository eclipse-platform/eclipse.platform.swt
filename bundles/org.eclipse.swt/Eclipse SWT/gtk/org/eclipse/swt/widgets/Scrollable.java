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
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
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
		bar.handle = OS.GTK_SCROLLED_WINDOW_HSCROLLBAR (scrolledHandle);
		bar.adjustmentHandle = OS.gtk_scrolled_window_get_hadjustment (scrolledHandle);
	} else {
		bar.handle = OS.GTK_SCROLLED_WINDOW_VSCROLLBAR (scrolledHandle);
		bar.adjustmentHandle = OS.gtk_scrolled_window_get_vadjustment (scrolledHandle);
	}
	bar.setOrientation (true);
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

void destroyScrollBar (ScrollBar bar) {
	setScrollBarVisible (bar, false);
	//This code is intentionally commented
	//bar.destroyHandle ();
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
	forceResize ();
	int /*long*/ clientHandle = clientHandle ();
	int x = OS.GTK_WIDGET_X (clientHandle);
	int y = OS.GTK_WIDGET_Y (clientHandle);
	int width = (state & ZERO_WIDTH) != 0 ? 0 : OS.GTK_WIDGET_WIDTH (clientHandle);
	int height = (state & ZERO_HEIGHT) != 0 ? 0 : OS.GTK_WIDGET_HEIGHT (clientHandle);
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
 * Returns the mode of the receiver's scrollbars. This will be
 * <em>bitwise</em> OR of one or more of the constants defined in class
 * <code>SWT</code>.<br>
 * <li><code>SWT.SCROLLBAR_OVERLAY</code> - if receiver
 * uses overlay scrollbars</li>
 * <li><code>SWT.NONE</code> - otherwise</li>
 * 
 * @return the mode of scrollbars
 * 
 * @exception SWTException <ul>
 * <li>ERROR_WIDGET_DISPOSED - if the receiver has been
 * disposed</li>
 * <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
 * thread that created the receiver</li>
 * </ul>
 * 
 * @see SWT#SCROLLBAR_OVERLAY
 * 
 * @since 3.8
 */
public int getScrollbarsMode () {
	checkWidget();
	return SWT.NONE;
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

int /*long*/ gtk_scroll_event (int /*long*/ widget, int /*long*/ eventPtr) {
	int /*long*/ result = super.gtk_scroll_event (widget, eventPtr);
	
	/*
	* Feature in GTK.  Scrolled windows do not scroll if the scrollbars
	* are hidden.  This is not a bug, but is inconsistent with other platforms.
	* The fix is to set the adjustment values directly.
	*/
	if ((state & CANVAS) != 0) {
		ScrollBar scrollBar;
		GdkEventScroll gdkEvent = new GdkEventScroll ();
		OS.memmove (gdkEvent, eventPtr, GdkEventScroll.sizeof);
		if (gdkEvent.direction == OS.GDK_SCROLL_UP || gdkEvent.direction == OS.GDK_SCROLL_DOWN) {
			scrollBar = verticalBar;
		} else {
			scrollBar = horizontalBar;
		}
		if (scrollBar != null && !OS.GTK_WIDGET_VISIBLE (scrollBar.handle) && scrollBar.getEnabled()) {
			GtkAdjustment adjustment = new GtkAdjustment ();
			OS.memmove (adjustment, scrollBar.adjustmentHandle);
			/* Calculate wheel delta to match GTK+ 2.4 and higher */
			int wheel_delta = (int) Math.pow(adjustment.page_size, 2.0 / 3.0);
			if (gdkEvent.direction == OS.GDK_SCROLL_UP || gdkEvent.direction == OS.GDK_SCROLL_LEFT)
				wheel_delta = -wheel_delta;
			int value = (int) Math.max(adjustment.lower,
					Math.min(adjustment.upper - adjustment.page_size, adjustment.value + wheel_delta));
			OS.gtk_adjustment_set_value (scrollBar.adjustmentHandle, value);
			return 1;
		}
	}
	return result;
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

void reskinChildren (int flags) {
	if (horizontalBar != null) horizontalBar.reskin (flags);
	if (verticalBar != null) verticalBar.reskin (flags);
	super.reskinChildren (flags);
}

boolean sendLeaveNotify () {
	return scrolledHandle != 0;
}

void setOrientation (boolean create) {
	super.setOrientation (create);
	if ((style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		int dir = (style & SWT.RIGHT_TO_LEFT) != 0 ? OS.GTK_TEXT_DIR_RTL : OS.GTK_TEXT_DIR_LTR;
		if (scrolledHandle != 0) {
			OS.gtk_widget_set_direction (scrolledHandle, dir);
		}
	}
	if (horizontalBar != null) horizontalBar.setOrientation (create);
	if (verticalBar != null) verticalBar.setOrientation (create);	
}

boolean setScrollBarVisible (ScrollBar bar, boolean visible) {
	if (scrolledHandle == 0) return false;
	int [] hsp = new int [1], vsp = new int [1];
	OS.gtk_scrolled_window_get_policy (scrolledHandle, hsp, vsp);
	int policy = visible ? OS.GTK_POLICY_ALWAYS : OS.GTK_POLICY_NEVER;
	if ((bar.style & SWT.HORIZONTAL) != 0) {
		if (hsp [0] == policy) return false;
		hsp [0] = policy;
	} else {
		if (vsp [0] == policy) return false;
		vsp [0] = policy;
	}
	OS.gtk_scrolled_window_set_policy (scrolledHandle, hsp [0], vsp [0]);
	return true;
}

void redrawBackgroundImage () {
}

void redrawWidget (int x, int y, int width, int height, boolean redrawAll, boolean all, boolean trim) {
	super.redrawWidget (x, y, width, height, redrawAll, all, trim);
	if ((OS.GTK_WIDGET_FLAGS (handle) & OS.GTK_REALIZED) == 0) return;
	if (!trim) return;
	int /*long*/ topHandle = topHandle (), paintHandle = paintHandle ();
	if (topHandle == paintHandle) return;
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (topHandle);
	GdkRectangle rect = new GdkRectangle ();
	if (redrawAll) {
		rect.width = OS.GTK_WIDGET_WIDTH (topHandle);
		rect.height = OS.GTK_WIDGET_HEIGHT (topHandle);
	} else {
		int [] destX = new int [1], destY = new int [1];
		OS.gtk_widget_translate_coordinates (paintHandle, topHandle, x, y, destX, destY);
		rect.x = destX [0];
		rect.y = destY [0];
		rect.width = width;
		rect.height = height;
	}
	OS.gdk_window_invalidate_rect (window, rect, all);
}

void register () {
	super.register ();
	if (scrolledHandle != 0) display.addWidget (scrolledHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	scrolledHandle = 0;
}

void releaseChildren (boolean destroy) {
	if (horizontalBar != null) {
		horizontalBar.release (false);
		horizontalBar = null;
	}
	if (verticalBar != null) {
		verticalBar.release (false);
		verticalBar = null;
	}
	super.releaseChildren (destroy);
}

void resizeHandle (int width, int height) {
	if (fixedHandle != 0) OS.gtk_widget_set_size_request (fixedHandle, width, height);
	OS.gtk_widget_set_size_request (scrolledHandle != 0 ? scrolledHandle : handle, width, height);
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

void updateScrollBarValue (ScrollBar bar) {
	redrawBackgroundImage ();
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

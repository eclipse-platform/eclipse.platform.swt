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

/**
 * Instances of this class are controls which are capable
 * of containing other controls.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>NO_BACKGROUND, NO_FOCUS, NO_MERGE_PAINTS, NO_REDRAW_RESIZE, NO_RADIO_GROUP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: The <code>NO_BACKGROUND</code>, <code>NO_FOCUS</code>, <code>NO_MERGE_PAINTS</code>,
 * and <code>NO_REDRAW_RESIZE</code> styles are intended for use with <code>Canvas</code>.
 * They can be used with <code>Composite</code> if you are drawing your own, but their
 * behavior is undefined if they are used with subclasses of <code>Composite</code> other
 * than <code>Canvas</code>.
 * </p><p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are constructed from aggregates
 * of other controls.
 * </p>
 *
 * @see Canvas
 */
public class Composite extends Scrollable {
	int radioHandle;
	Layout layout;
	Control[] tabList;

Composite () {
	/* Do nothing */
}

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
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_FOCUS
 * @see SWT#NO_MERGE_PAINTS
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_RADIO_GROUP
 * @see Widget#getStyle
 */
public Composite (Composite parent, int style) {
	super (parent, style);
}

Control [] _getChildren () {
	int parentHandle = parentingHandle ();
	int list = OS.gtk_container_get_children (parentHandle);
	if (list == 0) return new Control [0];
	list = OS.g_list_reverse (list);
	int count = OS.g_list_length (list);
	Control [] children = new Control [count];
	int i = 0, j = 0;
	while (i < count) {
		int handle = OS.g_list_nth_data (list, i);
		if (handle != 0) {
			Widget widget = WidgetTable.get (handle);
			if (widget != null && widget != this) {
				if (widget instanceof Control) {
					children [j++] = (Control) widget;
				}
			}
		}
		i++;
	}
	OS.g_list_free (list);
	if (i == j) return children;
	Control [] newChildren = new Control [j];
	System.arraycopy (children, 0, newChildren, 0, j);
	return newChildren;
}

Control [] _getTabList () {
	if (tabList == null) return tabList;
	int count = 0;
	for (int i=0; i<tabList.length; i++) {
		if (!tabList [i].isDisposed ()) count++;
	}
	if (count == tabList.length) return tabList;
	Control [] newList = new Control [count];
	int index = 0;
	for (int i=0; i<tabList.length; i++) {
		if (!tabList [i].isDisposed ()) {
			newList [index++] = tabList [i];
		}
	}
	tabList = newList;
	return tabList;
}

protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	Point size;
	if (layout != null) {
		if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
			size = layout.computeSize (this, wHint, hHint, changed);
		} else {
			size = new Point (wHint, hHint);
		}
	} else {
		size = minimumSize ();
	}
	if (size.x == 0) size.x = DEFAULT_WIDTH;
	if (size.y == 0) size.y = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	return new Point (trim.width, trim.height);
}

Control [] computeTabList () {
	Control result [] = super.computeTabList ();
	if (result.length == 0) return result;
	Control [] list = tabList != null ? _getTabList () : _getChildren ();
	for (int i=0; i<list.length; i++) {
		Control child = list [i];
		Control [] childList = child.computeTabList ();
		if (childList.length != 0) {
			Control [] newResult = new Control [result.length + childList.length];
			System.arraycopy (result, 0, newResult, 0, result.length);
			System.arraycopy (childList, 0, newResult, result.length, childList.length);
			result = newResult;
		}
	}
	return result;
}

void createHandle (int index) {
	state |= HANDLE | CANVAS;
	createScrolledHandle (parent.parentingHandle ());
}

void createScrolledHandle (int parentHandle) {
	//TEMPORARY CODE
//	boolean isScrolled = (style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0;
	boolean isScrolled = true;
	if (isScrolled) {
		fixedHandle = OS.gtk_fixed_new ();
		if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_fixed_set_has_window (fixedHandle, true);
		int vadj = OS.gtk_adjustment_new (0, 0, 100, 1, 10, 10);
		if (vadj == 0) error (SWT.ERROR_NO_HANDLES);
		int hadj = OS.gtk_adjustment_new (0, 0, 100, 1, 10, 10);
		if (hadj == 0) error (SWT.ERROR_NO_HANDLES);
		scrolledHandle = OS.gtk_scrolled_window_new (hadj, vadj);
		if (scrolledHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	}
	handle = OS.gtk_fixed_new ();
	if (handle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (handle, true);
	OS.GTK_WIDGET_SET_FLAGS(handle, OS.GTK_CAN_FOCUS);
	if (isScrolled) {
		OS.gtk_container_add (parentHandle, fixedHandle);
		OS.gtk_container_add (fixedHandle, scrolledHandle);
		/*
		* Force the scrolledWindow to have a single child that is
		* not scrolled automatically.  Calling gtk_container_add
		* seems to add the child correctly but cause a warning.
		*/
		Display display = getDisplay ();
		boolean warnings = display.getWarnings ();
		display.setWarnings (false);
		OS.gtk_container_add (scrolledHandle, handle);
		display.setWarnings (warnings);
		
		OS.gtk_widget_show (fixedHandle);
		OS.gtk_widget_show (scrolledHandle);
		int hsp = (style & SWT.H_SCROLL) != 0 ? OS.GTK_POLICY_ALWAYS : OS.GTK_POLICY_NEVER;
		int vsp = (style & SWT.V_SCROLL) != 0 ? OS.GTK_POLICY_ALWAYS : OS.GTK_POLICY_NEVER;
		OS.gtk_scrolled_window_set_policy (scrolledHandle, hsp, vsp);
		//CHECK WIDTH IS THERE ALREADY THEN DON'T SET
		if (hasBorder ()) {
			OS.gtk_scrolled_window_set_shadow_type (scrolledHandle, OS.GTK_SHADOW_ETCHED_IN);
		}
	} else {
		OS.gtk_container_add (parentHandle, handle);		
	}
	OS.gtk_widget_show (handle);
	
	OS.gtk_widget_set_double_buffered(handle, false);
	if ((style & SWT.NO_BACKGROUND) != 0) {
		setBackgroundPixmap ();
	}
	if ((style & SWT.NO_REDRAW_RESIZE) != 0) {
		OS.gtk_widget_set_redraw_on_allocate (handle, false);
	}
}

public int getBorderWidth () {
	checkWidget();
	int topHandle = topHandle ();
	return OS.gtk_container_get_border_width (topHandle);
}

/**
 * Returns an array containing the receiver's children.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of children, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return an array of children
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control [] getChildren () {
	checkWidget();
	return _getChildren ();
}

/**
 * Returns layout which is associated with the receiver, or
 * null if one has not been set.
 *
 * @return the receiver's layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Layout getLayout () {
	checkWidget();
	return layout;
}

/**
 * Gets the last specified tabbing order for the control.
 *
 * @return tabList the ordered list of controls representing the tab order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setTabList
 */
public Control [] getTabList () {
	return new Control [0];
}

boolean hasBorder () {
	return (style & SWT.BORDER) != 0;
}

void hookEvents () {
	super.hookEvents ();
	if ((state & CANVAS) != 0) {
		OS.gtk_widget_add_events (handle, OS.GDK_POINTER_MOTION_HINT_MASK);
	}
}

/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the receiver does not have a layout, do nothing.
 * <p>
 * This is equivalent to calling <code>layout(true)</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout () {
	layout (true);
}

/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the the argument is <code>true</code> the layout must not rely
 * on any cached information it is keeping about the children. If it
 * is <code>false</code> the layout may (potentially) simplify the
 * work it is doing by assuming that the state of the none of the
 * receiver's children has changed since the last layout.
 * If the receiver does not have a layout, do nothing.
 *
 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout (boolean changed) {
	checkWidget();
	if (layout == null) return;
	layout.layout (this, changed);
}

void moveAbove (int child, int sibling) {
	if (child == sibling) return;
	int parentHandle = parentingHandle ();
	GtkFixed fixed = new GtkFixed ();
	OS.memmove (fixed, parentHandle);
	int children = fixed.children;
	if (children == 0) return;
	int [] data = new int [1];
	int [] widget = new int [1];
	int childData = 0, childLink = 0, siblingLink = 0, temp = children;
	while (temp != 0) {
		OS.memmove (data, temp, 4);
		OS.memmove (widget, data [0], 4);
		if (child == widget [0]) {
			childLink = temp;
			childData = data [0];
		} else if (sibling == widget [0]) {
			siblingLink = temp;
		}
		if (childData != 0 && (sibling == 0 || siblingLink != 0)) break;
		temp = OS.g_list_next (temp);
	}
	children = OS.g_list_remove_link (children, childLink);
	if (siblingLink == 0 || OS.g_list_next (siblingLink) == 0) {
		OS.g_list_free_1 (childLink);
		children = OS.g_list_append (children, childData);
	} else {
		temp = OS.g_list_next (siblingLink);
		OS.g_list_next (childLink, temp);
		OS.g_list_previous (temp, childLink);
		OS.g_list_previous (childLink, siblingLink);
		OS.g_list_next (siblingLink, childLink);
	}
	fixed.children = children;
	OS.memmove (parentHandle, fixed);
}

void moveBelow (int child, int sibling) {
	if (child == sibling) return;
	int parentHandle = parentingHandle ();
	GtkFixed fixed = new GtkFixed ();
	OS.memmove (fixed, parentHandle);
	int children = fixed.children;
	if (children == 0) return;
	int [] data = new int [1];
	int [] widget = new int [1];
	int childData = 0, childLink = 0, siblingLink = 0, temp = children;
	while (temp != 0) {
		OS.memmove (data, temp, 4);
		OS.memmove (widget, data [0], 4);
		if (child == widget [0]) {
			childLink = temp;
			childData = data [0];
		} else if (sibling == widget [0]) {
			siblingLink = temp;
		}
		if (childData != 0 && (sibling == 0 || siblingLink != 0)) break;
		temp = OS.g_list_next (temp);
	}
	children = OS.g_list_remove_link (children, childLink);
	if (siblingLink == 0 || OS.g_list_previous (siblingLink) == 0) {
		OS.g_list_free_1 (childLink);
		children = OS.g_list_prepend (children, childData);
	} else {
		temp = OS.g_list_previous (siblingLink);
		OS.g_list_previous (childLink, temp);
		OS.g_list_next (temp, childLink);
		OS.g_list_next (childLink, siblingLink);
		OS.g_list_previous (siblingLink, childLink);
	}
	fixed.children = children;
	OS.memmove (parentHandle, fixed);
}

Point minimumSize () {
	Control [] children = _getChildren ();
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Rectangle rect = children [i].getBounds ();
		width = Math.max (width, rect.x + rect.width);
		height = Math.max (height, rect.y + rect.height);
	}
	return new Point (width, height);
}

int parentingHandle () {
	if ((state & CANVAS) != 0) return handle;
	return fixedHandle != 0 ? fixedHandle : handle;
}

int processFocusIn(int int0, int int1, int int2) {
	int result = super.processFocusIn (int0, int1, int2);
	return (state & CANVAS) != 0 ? 1 : result;
}

int processFocusOut(int int0, int int1, int int2) {
	int result = super.processFocusOut (int0, int1, int2);
	return (state & CANVAS) != 0 ? 1 : result;
}

int processKeyDown (int int0, int int1, int int2) {
	int result = super.processKeyDown (int0, int1, int2);
	return (state & CANVAS) != 0 && result != 1 && hasFocus () ? 1 : result;
}

int processKeyUp (int int0, int int1, int int2) {
	int result = super.processKeyUp (int0, int1, int2);
	return (state & CANVAS) != 0 && result != 1 && hasFocus () ? 1 : result;
}

int processMouseDown (int callData, int arg1, int int2) {
	int result = super.processMouseDown (callData, arg1, int2);
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) == 0) {
			int count = 0;
			int list = OS.gtk_container_get_children (handle);
			if (list != 0) {
				count = OS.g_list_length (list);
				OS.g_list_free (list);
			}
			if (count == 0) OS.gtk_widget_grab_focus (handle);
		}
	}
	return result;
}

int processPaint (int callData, int int1, int int2) {
	if ((state & CANVAS) == 0) {
		return super.processPaint (callData, int1, int2);
	}
	if ((style & SWT.NO_BACKGROUND) == 0) {
		int window = paintWindow ();
		int gc = OS.gdk_gc_new (window);
		OS.gdk_gc_set_foreground (gc, getBackgroundColor ());
		GdkEventExpose gdkEvent = new GdkEventExpose ();
		OS.memmove(gdkEvent, callData, GdkEventExpose.sizeof);
		int x = gdkEvent.area_x, y = gdkEvent.area_y;
		int width = gdkEvent.area_width, height = gdkEvent.area_height;
		OS.gdk_gc_set_clip_region (gc, gdkEvent.region);
		OS.gdk_draw_rectangle (window, gc, 1, x, y, width, height);
		OS.g_object_unref (gc);
	}
	if ((style & SWT.NO_MERGE_PAINTS) == 0) {
		return super.processPaint (callData, int1, int2);
	}
	if (!hooks (SWT.Paint)) return 0;
	GdkEventExpose gdkEvent = new GdkEventExpose ();
	OS.memmove(gdkEvent, callData, GdkEventExpose.sizeof);
	int [] rectangles = new int [1];
	int [] n_rectangles = new int [1];
	OS.gdk_region_get_rectangles (gdkEvent.region, rectangles, n_rectangles);
	GdkRectangle rect = new GdkRectangle ();
	for (int i=0; i<n_rectangles[0]; i++) {
		Event event = new Event ();
		OS.memmove (rect, rectangles [0] + i * GdkRectangle.sizeof, GdkRectangle.sizeof);
		event.x = rect.x;
		event.y = rect.y;
		event.width = rect.width;
		event.height = rect.height;
		GC gc = event.gc = new GC (this);
		gc.setClipping (event.x, event.y, event.width, event.height);
		sendEvent (SWT.Paint, event);
		gc.dispose ();
		event.gc = null;
	}
	OS.g_free (rectangles [0]);
	return 0;
}

int radioGroup() {
	if (radioHandle == 0) {
		radioHandle = OS.gtk_radio_button_new (0);
		if (radioHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	}
	return OS.gtk_radio_button_group (radioHandle);
}

void releaseChildren () {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child != null && !child.isDisposed ()) child.releaseResources ();
	}
}

void releaseHandle () {
	super.releaseHandle ();
	radioHandle = 0;
}

void releaseWidget () {
	releaseChildren ();
	super.releaseWidget ();
	layout = null;
}

void setBackgroundPixmap () {
	if ((style & SWT.NO_BACKGROUND) != 0) {
		OS.gtk_widget_realize (handle);
		int window = OS.GTK_WIDGET_WINDOW (handle);
		OS.gdk_window_set_back_pixmap (window, 0, false);
	}
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	if ((state & CANVAS) != 0) setBackgroundPixmap ();
}

boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	if (changed && resize && layout != null) layout.layout (this, false);
	return changed;
}

public boolean setFocus () {
	checkWidget();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.getVisible () && child.setFocus ()) return true;
	}
	return super.setFocus ();
}

void setFontDescription (int font) {
	super.setFontDescription (font);
	if ((state & CANVAS) != 0) setBackgroundPixmap ();
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	if ((state & CANVAS) != 0) setBackgroundPixmap ();
}

/**
 * Sets the layout which is associated with the receiver to be
 * the argument which may be null.
 *
 * @param layout the receiver's new layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayout (Layout layout) {
	checkWidget();
	this.layout = layout;
}

boolean setTabGroupFocus () {
	if (isTabItem ()) return setTabItemFocus ();
	if ((style & SWT.NO_FOCUS) == 0) {
		boolean takeFocus = true;
		if ((state & CANVAS) != 0) {
			takeFocus = hooks (SWT.KeyDown) || hooks (SWT.KeyUp);
		}
		if (takeFocus && setTabItemFocus ()) return true;
	}
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.isTabItem () && child.setTabItemFocus ()) return true;
	}
	return false;
}

boolean setTabItemFocus () {
	if ((style & SWT.NO_FOCUS) == 0) {
		boolean takeFocus = true;
		if ((state & CANVAS) != 0) {
			takeFocus = hooks (SWT.KeyDown) || hooks (SWT.KeyUp);
		}
		if (takeFocus) {
			if (!isShowing ()) return false;
			if (forceFocus ()) return true;
		}
	}
	return super.setTabItemFocus ();
}

/**
 * Sets the tabbing order for the specified controls to
 * match the order that they occur in the argument list.
 *
 * @param tabList the ordered list of controls representing the tab order or null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if a widget in the tabList is null or has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if widget in the tabList is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTabList (Control [] tabList) {
	checkWidget ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			Control control = tabList [i];
			if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
			if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
			/*
			* This code is intentionally commented.
			* Tab lists are currently only supported
			* for the direct children of a composite.
			*/
//			Shell shell = control.getShell ();
//			while (control != shell && control != this) {
//				control = control.parent;
//			}
//			if (control != this) error (SWT.ERROR_INVALID_PARENT);
			if (control.parent != this) error (SWT.ERROR_INVALID_PARENT);
		}
		Control [] newList = new Control [tabList.length];
		System.arraycopy (tabList, 0, newList, 0, tabList.length);
		tabList = newList;
	} 
	this.tabList = tabList;
}

int traversalCode(int key, int event) {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) return 0;
		if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) return 0;
	}
	return super.traversalCode (key, event);
}

}

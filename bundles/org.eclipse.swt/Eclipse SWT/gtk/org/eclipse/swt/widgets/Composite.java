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
	public int  embeddedHandle;
	int imHandle, socketHandle;
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
			Widget widget = display.getWidget (handle);
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
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
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
	boolean scrolled = (style & (SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER)) != 0;
	createHandle (index, parent.parentingHandle (), scrolled);
}

void createHandle (int index, int parentHandle, boolean scrolled) {
	if (scrolled) {
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
	if ((style & SWT.EMBEDDED) == 0) {
		if ((state & CANVAS) != 0 && (style & SWT.NO_FOCUS) == 0) {
			imHandle = OS.gtk_im_multicontext_new ();
			if (imHandle == 0) error (SWT.ERROR_NO_HANDLES);
		}
	}
	if (scrolled) {
		OS.gtk_container_add (parentHandle, fixedHandle);
		OS.gtk_container_add (fixedHandle, scrolledHandle);
		/*
		* Force the scrolledWindow to have a single child that is
		* not scrolled automatically.  Calling gtk_container_add
		* seems to add the child correctly but cause a warning.
		*/
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
	if ((style & SWT.EMBEDDED) != 0) {
		socketHandle = OS.gtk_socket_new ();
		if (socketHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (handle, socketHandle);
		OS.gtk_widget_show (socketHandle);
		embeddedHandle = OS.gtk_socket_get_id (socketHandle);
	}
	if (imHandle != 0) {
		int window = OS.GTK_WIDGET_WINDOW (handle);
		if (window != 0) {
			OS.gtk_im_context_set_client_window (imHandle, window);
		}
	}
	if ((style & SWT.NO_REDRAW_RESIZE) != 0) {
		OS.gtk_widget_set_redraw_on_allocate (handle, false);
	}
}

void deregister () {
	super.deregister ();
	if (socketHandle != 0) display.removeWidget (socketHandle);
}

void enableWidget (boolean enabled) {
	//NOT DONE - take into account current enabled scroll bar state
	if (scrolledHandle != 0) {
		if (horizontalBar != null) {
			int barHandle = OS.GTK_SCROLLED_WINDOW_HSCROLLBAR (scrolledHandle);
			OS.gtk_widget_set_sensitive (barHandle, enabled);
		}
		if (verticalBar != null) {
			int barHandle = OS.GTK_SCROLLED_WINDOW_VSCROLLBAR (scrolledHandle);
			OS.gtk_widget_set_sensitive (barHandle, enabled);
		}
	}
}

Menu [] findMenus (Control control) {
	if (control == this) return new Menu [0];
	Menu result [] = super.findMenus (control);
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		Menu [] menuList = child.findMenus (control);
		if (menuList.length != 0) {
			Menu [] newResult = new Menu [result.length + menuList.length];
			System.arraycopy (result, 0, newResult, 0, result.length);
			System.arraycopy (menuList, 0, newResult, result.length, menuList.length);
			result = newResult;
		}
	}
	return result;
}

void fixChildren (Shell newShell, Shell oldShell, Decorations newDecorations, Decorations oldDecorations, Menu [] menus) {
	super.fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		children [i].fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
	}
}

void fixTabList (Control control) {
	if (tabList == null) return;
	int count = 0;
	for (int i=0; i<tabList.length; i++) {
		if (tabList [i] == control) count++;
	}
	if (count == 0) return;
	Control [] newList = null;
	int length = tabList.length - count;
	if (length != 0) {
		newList = new Control [length];
		int index = 0;
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] != control) {
				newList [index++] = tabList [i];
			}
		}
	}
	tabList = newList;
}

int focusHandle () {
	if (socketHandle != 0) return socketHandle;
	return super.focusHandle ();
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

int getChildrenCount () {
	/*
	* NOTE: The current implementation will count
	* non-registered children.
	*/
	int list = OS.gtk_container_get_children (handle);
	if (list == 0) return 0;
	int count = OS.g_list_length (list);
	OS.g_list_free (list);
	return count;
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
	checkWidget ();
	Control [] tabList = _getTabList ();
	if (tabList == null) {
		int count = 0;
		Control [] list =_getChildren ();
		for (int i=0; i<list.length; i++) {
			if (list [i].isTabGroup ()) count++;
		}
		tabList = new Control [count];
		int index = 0;
		for (int i=0; i<list.length; i++) {
			if (list [i].isTabGroup ()) {
				tabList [index++] = list [i];
			}
		}
	}
	return tabList;
}

int gtk_button_press_event (int widget, int event) {
	int result = super.gtk_button_press_event (widget, event);
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) == 0 && hooksKeys ()) {
			GdkEventButton gdkEvent = new GdkEventButton ();
			OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
			if (gdkEvent.button == 1) {
				if (getChildrenCount () == 0)  setFocus ();
			}
		}
	}
	return result;
}

int gtk_expose_event (int widget, int eventPtr) {
	if ((state & CANVAS) == 0) {
		return super.gtk_expose_event (widget, eventPtr);
	}
	if ((style & SWT.NO_MERGE_PAINTS) == 0) {
		return super.gtk_expose_event (widget, eventPtr);
	}
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) return 0;
	GdkEventExpose gdkEvent = new GdkEventExpose ();
	OS.memmove(gdkEvent, eventPtr, GdkEventExpose.sizeof);
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

int gtk_key_press_event (int widget, int event) {
	int result = super.gtk_key_press_event (widget, event);
	if (result != 0) return result;
	/*
	* Feature in GTK.  The default behavior when the return key
	* is pressed is to select the default button.  This is not the
	* expected behavior for Composite and its subclasses.  The
	* fix is to avoid calling the default handler.
	*/
	if ((state & CANVAS) != 0) {
		GdkEventKey keyEvent = new GdkEventKey ();
		OS.memmove (keyEvent, event, GdkEventKey.sizeof);
		int key = keyEvent.keyval;
		switch (key) {
			case OS.GDK_Return:
			case OS.GDK_KP_Enter: return 1;
		}
	}
	return result;
}
int gtk_focus_in_event (int widget, int event) {
	int result = super.gtk_focus_in_event (widget, event);
	return (state & CANVAS) != 0 ? 1 : result;
}

int gtk_focus_out_event (int widget, int event) {
	int result = super.gtk_focus_out_event (widget, event);
	return (state & CANVAS) != 0 ? 1 : result;
}

int gtk_scroll_child (int widget, int scrollType, int horizontal) {
	/* Stop GTK scroll child signal for canvas */
	OS.g_signal_stop_emission_by_name (widget, OS.scroll_child);
	return 1;
}

boolean hasBorder () {
	return (style & SWT.BORDER) != 0;
}

void hookEvents () {
	super.hookEvents ();
	if ((state & CANVAS) != 0) {
		OS.gtk_widget_add_events (handle, OS.GDK_POINTER_MOTION_HINT_MASK);
		if (scrolledHandle != 0) {
			OS.g_signal_connect (scrolledHandle, OS.scroll_child, display.windowProc4, SCROLL_CHILD);
		}
	}
}

boolean hooksKeys () {
	return hooks (SWT.KeyDown) || hooks (SWT.KeyUp);
}

int imHandle () {
	return imHandle;
}

boolean isTabGroup() {
	if ((state & CANVAS) != 0) return true;
	return super.isTabGroup();
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

void register () {
	super.register ();
	if (socketHandle != 0) display.addWidget (socketHandle, this);
}

void releaseChildren () {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child != null && !child.isDisposed ()) child.releaseResources ();
	}
}

void releaseWidget () {
	releaseChildren ();
	super.releaseWidget ();
	if (imHandle != 0) OS.g_object_unref (imHandle);
	imHandle = 0;
	layout = null;
}

void removeControl (Control control) {
	fixTabList (control);
}

void resizeHandle (int width, int height) {
	super.resizeHandle (width, height);
	if (socketHandle != 0) OS.gtk_widget_set_size_request (socketHandle, width, height);
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
	boolean takeFocus = (style & SWT.NO_FOCUS) == 0;
	if ((state & CANVAS) != 0) takeFocus = hooksKeys ();
	if (socketHandle != 0) takeFocus = true;
	if (takeFocus  && setTabItemFocus ()) return true;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.isTabItem () && child.setTabItemFocus ()) return true;
	}
	return false;
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
			if (control.parent != this) error (SWT.ERROR_INVALID_PARENT);
		}
		Control [] newList = new Control [tabList.length];
		System.arraycopy (tabList, 0, newList, 0, tabList.length);
		tabList = newList;
	} 
	this.tabList = tabList;
}

boolean translateMnemonic (Event event, Control control) {
	if (super.translateMnemonic (event, control)) return true;
	if (control != null) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			Control child = children [i];
			if (child.translateMnemonic (event, control)) return true;
		}
	}
	return false;
}

int traversalCode(int key, GdkEventKey event) {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) return 0;
		if (hooksKeys ()) return 0;
	}
	return super.traversalCode (key, event);
}

}

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


import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
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
	Layout layout;
	int focusHandle, damagedRegion;
	Control [] tabList;
	
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
	int [] argList = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int ptr = argList [1], count = argList [3];
	if (count == 0 || ptr == 0) return new Control [0];
	int [] handles = new int [count];
	OS.memmove (handles, ptr, count * 4);
	int length = focusHandle != 0 ? count - 1 : count;
	Control [] children = new Control [length];
	int i = 0, j = 0;
	while (i < count) {
		int handle = handles [i];
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
	if (j == length) return children;
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
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	Point size;
	if (layout != null) {
		if ((wHint == SWT.DEFAULT) || (hHint == SWT.DEFAULT)) {
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
protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
}
void createHandle (int index) {
	state |= HANDLE | CANVAS;
	int parentHandle = parent.handle;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
		int border = (style & SWT.BORDER) != 0 ? 1 : 0;
		int [] argList = {
			OS.XmNancestorSensitive, 1,
			OS.XmNborderWidth, border,
			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNtraversalOn, (style & SWT.NO_FOCUS) != 0 ? 0 : 1,
		};
		handle = OS.XmCreateDrawingArea (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	} else {
		createScrolledHandle (parentHandle);
	}
	if ((style & SWT.NO_FOCUS) == 0) {
		focusHandle = OS.XmCreateDrawingArea (handle, null, null, 0);
		if (focusHandle == 0) error (SWT.ERROR_NO_HANDLES);
	}
}
void createScrolledHandle (int topHandle) {
	int [] argList = {OS.XmNancestorSensitive, 1};
	scrolledHandle = OS.XmCreateMainWindow (topHandle, null, argList, argList.length / 2);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER)) != 0) {
		int [] argList1 = {
			OS.XmNmarginWidth, 3,
			OS.XmNmarginHeight, 3, 
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNshadowType, OS.XmSHADOW_IN,
			OS.XmNshadowThickness, (style & SWT.BORDER) != 0 ? display.buttonShadowThickness : 0,
		};
		formHandle = OS.XmCreateForm (scrolledHandle, null, argList1, argList1.length / 2);
		if (formHandle == 0) error (SWT.ERROR_NO_HANDLES);
		int [] argList2 = {
			OS.XmNtopAttachment, OS.XmATTACH_FORM,
			OS.XmNbottomAttachment, OS.XmATTACH_FORM,
			OS.XmNleftAttachment, OS.XmATTACH_FORM,
			OS.XmNrightAttachment, OS.XmATTACH_FORM,
			OS.XmNresizable, 0,
			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
		};
		handle = OS.XmCreateDrawingArea (formHandle, null, argList2, argList2.length / 2);
	} else {
		int [] argList3 = {
			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNtraversalOn, (style & SWT.NO_FOCUS) != 0 ? 0 : 1,
		};
		handle = OS.XmCreateDrawingArea (scrolledHandle, null, argList3, argList3.length / 2);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}
int defaultBackground () {
	return display.compositeBackground;
}
int defaultForeground () {
	return display.compositeForeground;
}
void deregister () {
	super.deregister ();
	if (focusHandle != 0) WidgetTable.remove (focusHandle);
}
int focusHandle () {
	if (focusHandle == 0) return super.focusHandle ();
	return focusHandle;
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
	* NOTE:  The current implementation will count
	* non-registered children.
	* */
	int [] argList = {OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (focusHandle != 0) return Math.max (0, argList [1] - 1);
	return argList [1];
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
void hookEvents () {
	super.hookEvents ();
	if ((state & CANVAS) != 0) {
		OS.XtInsertEventHandler (handle, 0, true, display.windowProc, NON_MASKABLE, OS.XtListTail);
	}
}

boolean hooksKeys () {
	return hooks (SWT.KeyDown) || hooks (SWT.KeyUp);
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
	checkWidget();
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
	int count = getChildrenCount ();
	if (count == 0) return;
	layout.layout (this, changed);
}
void manageChildren () {
	if (focusHandle != 0) {	
		OS.XtSetMappedWhenManaged (focusHandle, false);
		OS.XtManageChild (focusHandle);
	}
	super.manageChildren ();
	if (focusHandle != 0) {
		OS.XtConfigureWidget(focusHandle, 0, 0, 1, 1, 0);
		OS.XtSetMappedWhenManaged (focusHandle, true);
	}
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
void moveAbove (int handle1, int handle2) {
	if (handle1 == handle2) return;
	int [] argList = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int ptr = argList [1], count = argList [3];
	if (count == 0 || ptr == 0) return;
	int [] handles = new int [count];
	OS.memmove (handles, ptr, count * 4);
	if (handle2 == 0) handle2 = handles [0];
	int i = 0, index1 = -1, index2 = -1;
	while (i < count) {
		int handle = handles [i];
		if (handle == handle1) index1 = i;
		if (handle == handle2) index2 = i;
		if (index1 != -1 && index2 != -1) break;
		i++;
	}
	if (index1 == -1 || index2 == -1) return;
	if (index1 == index2) return;
	if (index1 < index2) {
		System.arraycopy (handles, index1 + 1, handles, index1, index2 - index1 - 1);
		handles [index2 - 1] = handle1;
	} else {
		System.arraycopy (handles, index2, handles, index2 + 1, index1 - index2);
		handles [index2] = handle1;
	}
	OS.memmove (ptr, handles, count * 4);
}
void moveBelow (int handle1, int handle2) {
	if (handle1 == handle2) return;
	int [] argList = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int ptr = argList [1], count = argList [3];
	if (count == 0 || ptr == 0) return;
	int [] handles = new int [count];
	OS.memmove (handles, ptr, count * 4);
	if (handle2 == 0) handle2 = handles [count - 1];
	int i = 0, index1 = -1, index2 = -1;
	while (i < count) {
		int handle = handles [i];
		if (handle == handle1) index1 = i;
		if (handle == handle2) index2 = i;
		if (index1 != -1 && index2 != -1) break;
		i++;
	}
	if (index1 == -1 || index2 == -1) return;
	if (index1 == index2) return;
	if (index1 < index2) {
		System.arraycopy (handles, index1 + 1, handles, index1, index2 - index1);
		handles [index2] = handle1;
	} else {
		System.arraycopy (handles, index2 + 1, handles, index2 + 2, index1 - index2 - 1);
		handles [index2 + 1] = handle1;
	}
	OS.memmove (ptr, handles, count * 4);
}
void propagateChildren (boolean enabled) {
	super.propagateChildren (enabled);
	Control [] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		Control child = children [i];
		if (child.getEnabled ()) {
			child.propagateChildren (enabled);
		}
	}
}
void realizeChildren () {
	super.realizeChildren ();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		children [i].realizeChildren ();
	}
	/*
	* Feature in Motif.  XmProcessTraversal() will not give focus to
	* a widget that is obscured so the focus handle must be inside the
	* client area of the parent.  This means that it is visible as a
	* single pixel widget in the parent.  The fix is to unmap the
	* focus handle so that it will be traversed by XmProcessTraversal()
	* and will accept focus but will not be visible in the parent.
	*/
	if (focusHandle != 0) OS.XtUnmapWidget (focusHandle);
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_BACKGROUND) == 0 && (style & SWT.NO_REDRAW_RESIZE) != 0) return;
		int xDisplay = OS.XtDisplay (handle);
		if (xDisplay == 0) return;
		int xWindow = OS.XtWindow (handle);
		if (xWindow == 0) return;
		int flags = 0;
		XSetWindowAttributes attributes = new XSetWindowAttributes ();
		if ((style & SWT.NO_BACKGROUND) != 0) {
			flags |= OS.CWBackPixmap;
			attributes.background_pixmap = OS.None;
		}
		if ((style & SWT.NO_REDRAW_RESIZE) == 0) {
			flags |= OS.CWBitGravity;
			attributes.bit_gravity = OS.ForgetGravity;
		}
		if (flags != 0) {
			OS.XChangeWindowAttributes (xDisplay, xWindow, flags, attributes);
		}
	}
}
void register () {
	super.register ();
	if (focusHandle != 0) WidgetTable.put (focusHandle, this);
}
void redrawWidget (int x, int y, int width, int height, boolean all) {
	super.redrawWidget (x, y, width, height, all);
	if (!all) return;
	Control [] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		Control child = children [i];
		Point location = child.getClientLocation ();
		child.redrawWidget (x - location.x, y - location.y, width, height, all);
	}
}
void releaseChildren () {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (!child.isDisposed ()) child.releaseResources ();
	}
}
void releaseHandle () {
	super.releaseHandle ();
	focusHandle = 0;
}
void releaseWidget () {
	releaseChildren ();
	super.releaseWidget ();
	layout = null;
	tabList = null;
	if (damagedRegion != 0) OS.XDestroyRegion (damagedRegion);
	damagedRegion = 0;
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_BACKGROUND) != 0) {
			int xDisplay = OS.XtDisplay (handle);
			if (xDisplay == 0) return;
			int xWindow = OS.XtWindow (handle);
			if (xWindow == 0) return;
			XSetWindowAttributes attributes = new XSetWindowAttributes ();
			attributes.background_pixmap = OS.None;
			OS.XChangeWindowAttributes (xDisplay, xWindow, OS.CWBackPixmap, attributes);
		}
	}
}
boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	if (changed && resize) {
		if (focusHandle != 0) {
			int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
			OS.XtGetValues (handle, argList, argList.length / 2);
			OS.XtConfigureWidget (focusHandle, 0, 0, argList [1], argList [3], 0);
		}
		if (layout != null) layout.layout (this, false);
	}
	return changed;
}
public boolean setFocus () {
	checkWidget ();
	if ((style & SWT.NO_FOCUS) != 0) return false;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.setFocus ()) return true;
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
boolean setTabGroupFocus () {
	if (isTabItem ()) return setTabItemFocus ();
	if ((style & SWT.NO_FOCUS) == 0) {
		boolean takeFocus = true;
		if ((state & CANVAS) != 0) takeFocus = hooksKeys ();
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
		if ((state & CANVAS) != 0) takeFocus = hooksKeys ();
		if (takeFocus) {
			if (!isShowing ()) return false;
			if (forceFocus ()) return true;
		}
	}
	return super.setTabItemFocus ();
}
int traversalCode (int key, XKeyEvent xEvent) {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) return 0;
		if (hooksKeys ()) return 0;
	}
	return super.traversalCode (key, xEvent);
}
boolean translateMnemonic (char key, XKeyEvent xEvent) {
	if (super.translateMnemonic (key, xEvent)) return true;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.translateMnemonic (key, xEvent)) return true;
	}
	return false;
}
int XButtonPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XButtonPress (w, client_data, call_data, continue_to_dispatch);

	/* Set focus for a canvas with no children */
	if ((state & CANVAS) != 0) {
		XButtonEvent xEvent = new XButtonEvent ();
		OS.memmove (xEvent, call_data, XButtonEvent.sizeof);
		if (xEvent.button == 1) {
			if ((style & SWT.NO_FOCUS) != 0) return result;
			if (getChildrenCount () == 0) setFocus ();
		}
	}
	return result;
}
int XExposure (int w, int client_data, int call_data, int continue_to_dispatch) {
	if ((state & CANVAS) == 0) {
		return super.XExposure (w, client_data, call_data, continue_to_dispatch);
	}
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) return 0;
	if ((style & SWT.NO_MERGE_PAINTS) != 0) {
		return super.XExposure (w, client_data, call_data, continue_to_dispatch);
	}
	XExposeEvent xEvent = new XExposeEvent ();
	OS.memmove (xEvent, call_data, XExposeEvent.sizeof);
	int exposeCount = xEvent.count;
	if (exposeCount == 0) {
		if (OS.XEventsQueued (xEvent.display, OS.QueuedAfterReading) != 0) {
			XAnyEvent xAnyEvent = new XAnyEvent ();
			display.exposeCount = display.lastExpose = 0;
			int checkExposeProc = display.checkExposeProc;
			OS.XCheckIfEvent (xEvent.display, xAnyEvent, checkExposeProc, xEvent.window);
			exposeCount = display.exposeCount;
			int lastExpose = display.lastExpose;
			if (exposeCount != 0 && lastExpose != 0) {
				XExposeEvent xExposeEvent = display.xExposeEvent;
				OS.memmove (xExposeEvent, lastExpose, XExposeEvent.sizeof);
				xExposeEvent.count = 0;
				OS.memmove (lastExpose, xExposeEvent, XExposeEvent.sizeof);
			}
		}
	}
	if (exposeCount == 0 && damagedRegion == 0) {
		return super.XExposure (w, client_data, call_data, continue_to_dispatch);
	}
	if (damagedRegion == 0) damagedRegion = OS.XCreateRegion ();
	OS.XtAddExposureToRegion (call_data, damagedRegion);
	if (exposeCount != 0) return 0;
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return 0;
	Event event = new Event ();
	GC gc = event.gc = new GC (this);
	Region region = Region.motif_new (damagedRegion);
	gc.setClipping (region);
	XRectangle rect = new XRectangle ();
	OS.XClipBox (damagedRegion, rect);
	event.x = rect.x;  event.y = rect.y;
	event.width = rect.width;  event.height = rect.height;
	sendEvent (SWT.Paint, event);
	gc.dispose ();
	event.gc = null;
	OS.XDestroyRegion (damagedRegion);
	damagedRegion = 0;
	return 0;
}
int XNonMaskable (int w, int client_data, int call_data, int continue_to_dispatch) {
	if ((state & CANVAS) != 0) {
		XExposeEvent xEvent = new XExposeEvent ();
		OS.memmove (xEvent, call_data, XExposeEvent.sizeof);
		if (xEvent.type == OS.GraphicsExpose) return XExposure (w, client_data, call_data, continue_to_dispatch);
	}
	return 0;
}
}

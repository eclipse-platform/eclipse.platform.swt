package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.*;
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
	/* AW
	int damagedRegion;
	*/

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
	short[] cnt= new short[1];
	OS.CountSubControls(handle, cnt);
	int count= cnt[0];
	if (count == 0) return new Control [0];
	int handles[]= new int[count];
	int[] outHandle= new int[1];
	
	for (int i= 0; i < count; i++) {
		int index= (count-i);
		if (OS.GetIndexedSubControl(handle, (short)index, outHandle) == 0)	// indices are 1 based
			handles[i]= outHandle[0];
		else
			error (SWT.ERROR_CANNOT_GET_ITEM);
	}
	
	Control [] children = new Control [count];
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
	if (i == j) return children;
	Control [] newChildren = new Control [j];
	System.arraycopy (children, 0, newChildren, 0, j);
	return newChildren;
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
protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
}
void createHandle (int index) {
	state |= HANDLE | CANVAS;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
		int border = (style & SWT.BORDER) != 0 ? 1 : 0;
        /* AW
		int [] argList = {
			OS.XmNancestorSensitive, 1,
			OS.XmNborderWidth, border,
			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNtraversalOn, (style & SWT.NO_FOCUS) != 0 ? 0 : 1,
		};
        */
		int parentHandle = parent.handle;
        /* AW
		handle = OS.XmCreateDrawingArea (parentHandle, null, argList, argList.length / 2);
        */
        handle= MacUtil.createDrawingArea (parentHandle, 0, 0, border);

		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
        /* AW
		Display display = getDisplay ();
		OS.XtOverrideTranslations (handle, display.tabTranslations);
		OS.XtOverrideTranslations (handle, display.arrowTranslations);
        */
	} else {
		createScrolledHandle (parent.handle);
	}
}
void createScrolledHandle (int topHandle) {

    /* AW
	int [] argList = {OS.XmNancestorSensitive, 1};
	scrolledHandle = OS.XmCreateMainWindow (topHandle, null, argList, argList.length / 2);
    */
    scrolledHandle= createScrollView(topHandle, style);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);

	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
        /* AW
		int thickness = display.buttonShadowThickness;
		int [] argList1 = {
			OS.XmNmarginWidth, 3,
			OS.XmNmarginHeight, 3,
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNshadowType, OS.XmSHADOW_IN,
			OS.XmNshadowThickness, thickness,
		};
		formHandle = OS.XmCreateForm (scrolledHandle, null, argList1, argList1.length / 2);
		if (formHandle == 0) error (SWT.ERROR_NO_HANDLES);
		int [] argList2 = {
			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNtopAttachment, OS.XmATTACH_FORM,
			OS.XmNbottomAttachment, OS.XmATTACH_FORM,
			OS.XmNleftAttachment, OS.XmATTACH_FORM,
			OS.XmNrightAttachment, OS.XmATTACH_FORM,
			OS.XmNresizable, 0,
			OS.XmNtraversalOn, (style & SWT.NO_FOCUS) != 0 ? 0 : 1,
		};
		handle = OS.XmCreateDrawingArea (formHandle, null, argList2, argList2.length / 2);
        */
        handle= MacUtil.createDrawingArea (scrolledHandle, 0, 0, 0);
	} else {
        /* AW
		int [] argList3 = {
			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNresizePolicy, OS.XmRESIZE_NONE,
			OS.XmNtraversalOn, (style & SWT.NO_FOCUS) != 0 ? 0 : 1,
		};
		handle = OS.XmCreateDrawingArea (scrolledHandle, null, argList3, argList3.length / 2);
        */
        handle = MacUtil.createDrawingArea (scrolledHandle, 0, 0, 0);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
    /* AW
	OS.XtOverrideTranslations (handle, display.tabTranslations);
	OS.XtOverrideTranslations (handle, display.arrowTranslations);
    */
}
int defaultBackground () {
	return getDisplay ().compositeBackground;
}
int defaultForeground () {
	return getDisplay ().compositeForeground;
}
/* AW
public boolean forceFocus () {
	checkWidget();
	Control [] children = _getChildren ();
	int [] traversals = new int [children.length];
	int [] argList = new int [] {OS.XmNtraversalOn, 0};
	for (int i=0; i<children.length; i++) {
		OS.XtGetValues (children [i].handle, argList, argList.length / 2);
		traversals [i] = argList [1];
		argList [1] = 0;
		OS.XtSetValues (children [i].handle, argList, argList.length / 2);
	}
	boolean result = super.forceFocus ();
	for (int i=0; i<children.length; i++) {
		argList [1] = traversals [i];
		OS.XtSetValues (children [i].handle, argList, argList.length / 2);
	}
	return result;
}
*/
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
	*/
    /* AW
	int [] argList = {OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
    */
	short[] cnt= new short[1];
	OS.CountSubControls(handle, cnt);
	return cnt[0];
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
    /* AW
	int count = 0;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control control = children [i];
		int [] argList = new int [] {OS.XmNnavigationType, 0};
		OS.XtGetValues (control.handle, argList, argList.length / 2);
		if (argList [1] == OS.XmEXCLUSIVE_TAB_GROUP) count++;
	}
	int index = 0;
	Control [] tabList = new Control [count];
	for (int i=0; i<children.length; i++) {
		Control control = children [i];
		int [] argList = new int [] {OS.XmNnavigationType, 0};
		OS.XtGetValues (control.handle, argList, argList.length / 2);
		if (argList [1] == OS.XmEXCLUSIVE_TAB_GROUP) {
			tabList [index++] = control;
		}
	}
	return tabList;
    */
    return new Control[0];
}

void hookEvents () {
	super.hookEvents ();
	if ((state & CANVAS) != 0) {
        /* AW
		int windowProc = getDisplay ().windowProc;
		OS.XtAddEventHandler (handle, 0, true, windowProc, -1);
        */
		Display display= getDisplay();		
		OS.SetControlData(handle, OS.kControlEntireControl, OS.kControlUserPaneDrawProcTag, display.fUserPaneDrawProc);
		OS.SetControlData(handle, OS.kControlEntireControl, OS.kControlUserPaneHitTestProcTag, display.fUserPaneHitTestProc);
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
/* AW
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
*/
int processNonMaskable (Object callData) {
    /* AW
	if ((state & CANVAS) != 0) {
		XExposeEvent xEvent = new XExposeEvent ();
		OS.memmove (xEvent, callData, XExposeEvent.sizeof);
		if (xEvent.type == OS.GraphicsExpose) processPaint (callData);
	}
    */
	return 0;
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
    /* AW
	super.realizeChildren ();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		children [i].realizeChildren ();
	}
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
    */
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
		if (!child.isDisposed ()) {
			child.releaseWidget ();
			child.releaseHandle ();
		}
	}
}
void releaseWidget () {
	releaseChildren ();
	super.releaseWidget ();
	layout = null;
    /* AW
	if (damagedRegion != 0) OS.XDestroyRegion (damagedRegion);
	damagedRegion = 0;
    */
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_BACKGROUND) != 0) {
			/* AW
			int xDisplay = OS.XtDisplay (handle);
			if (xDisplay == 0) return;
			int xWindow = OS.XtWindow (handle);
			if (xWindow == 0) return;
			XSetWindowAttributes attributes = new XSetWindowAttributes ();
			attributes.background_pixmap = OS.None;
			OS.XChangeWindowAttributes (xDisplay, xWindow, OS.CWBackPixmap, attributes);
			*/
		}
	}
}
public void setBounds (int x, int y, int width, int height) {
	super.setBounds (x, y, width, height);
	if (layout != null) layout (false);
}
public boolean setFocus() {
	checkWidget ();
	if ((style & SWT.NO_FOCUS) != 0) return false;
	Control [] children = _getChildren ();
	for (int i= 0; i < children.length; i++) {
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
public void setSize (int width, int height) {
	super.setSize (width, height);
	if (layout != null) layout (false);
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
	if (tabList == null) error (SWT.ERROR_NULL_ARGUMENT);
    /* AW
	int [] argList1 = new int [] {OS.XmNnavigationType, OS.XmTAB_GROUP};
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control control = children [i];
		OS.XtSetValues (control.handle, argList1, argList1.length / 2);
	}
	int [] argList2 = new int [] {OS.XmNnavigationType, OS.XmEXCLUSIVE_TAB_GROUP};
	for (int i=0; i<tabList.length; i++) {
        */
		/*
		* Set the XmNnavigationType twice, once to clear the
		* old value and once to set the new.  If the old value
		* is not cleared, Motif detects that the values are the
		* same and does not change the tab order.
		*/
		/* AW
		Control control = tabList [i];
		OS.XtSetValues (control.handle, argList1, argList1.length / 2);
		OS.XtSetValues (control.handle, argList2, argList2.length / 2);
	}
	*/
}
int traversalCode () {
	if ((state & CANVAS) != 0) {
		if (hooks (SWT.KeyDown)) return 0;
	}
	return super.traversalCode ();
}
/* AW
boolean translateMnemonic (char key, XKeyEvent xEvent) {
	if (super.translateMnemonic (key, xEvent)) return true;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.translateMnemonic (key, xEvent)) return true;
	}
	return false;
}
*/
}

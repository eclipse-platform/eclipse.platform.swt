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


import org.eclipse.swt.internal.carbon.OS;

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
	Control[] tabList;
	int scrolledVisibleRgn, siblingsVisibleRgn;

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
	short [] count = new short [1];
	OS.CountSubControls (handle, count);
	if (count [0] == 0) return new Control [0];
	Control [] children = new Control [count [0]];
	int [] outControl= new int [1];
	int i = 0, j = 0;
	while (i < count [0]) {
		int status = OS.GetIndexedSubControl (handle, (short)(i+1), outControl);
		if (status == OS.noErr) {
			Widget widget = display.getWidget (outControl [0]);
			if (widget != null && widget != this) {
				if (widget instanceof Control) {
					children [j++] = (Control) widget;
				}
			}
		}
		i++;
	}
	if (j == count [0]) return children;
	Control [] newChildren = new Control [j];
	System.arraycopy (children, 0, newChildren, 0, j);
	return newChildren;
}

Control [] _getTabList () {
	if (tabList == null) return null;
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

protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
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

void createHandle () {
	state |= CANVAS | GRAB;
	if ((style & (SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
		createScrolledHandle (parent.handle);
	} else {
		createHandle (parent.handle);
	}
}

void createHandle (int parentHandle) {
	int features = OS.kControlSupportsEmbedding | OS.kControlSupportsFocus | OS.kControlGetsFocusOnClick;
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parentHandle);
	OS.CreateUserPaneControl (window, null, features, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
}

void createScrolledHandle (int parentHandle) {
	int features = OS.kControlSupportsEmbedding;
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parentHandle);
	OS.CreateUserPaneControl (window, null, features, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	scrolledHandle = outControl [0];
	outControl [0] = 0;
	features |= OS.kControlSupportsFocus | OS.kControlGetsFocusOnClick;
	OS.CreateUserPaneControl (window, null, features, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
}

void drawBackground (int control) {
	if (control == scrolledHandle) {
		if ((style & SWT.NO_FOCUS) == 0 && hooksKeys ()) {
			drawFocus (control, hasFocus (), hasBorder (), getParentBackground (), inset ());
		} else {
			drawBackground (control, getParentBackground ());			
		}
	} else {
		if ((state & CANVAS) != 0) {
			if ((style & SWT.NO_BACKGROUND) == 0) {
				drawBackground (control, background);
			}	
		}
	}
}

void enableWidget (boolean enabled) {
	//NOT DONE - take into account current scroll bar state
	if ((state & CANVAS) != 0) {
		if (horizontalBar != null) horizontalBar.enableWidget (enabled);
		if (verticalBar != null) verticalBar.enableWidget (enabled);
		return;
	}
	super.enableWidget (enabled);
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
	*/
	short [] count = new short [1];
	OS.CountSubControls (handle, count);
	return count [0];
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

int getVisibleRegion (int control, boolean clipChildren) {
	if (!clipChildren && control == handle) {
		if (siblingsVisibleRgn == 0) {
			siblingsVisibleRgn = OS.NewRgn ();
			calculateVisibleRegion (control, siblingsVisibleRgn, clipChildren);
		}
		int result = OS.NewRgn ();
		OS.CopyRgn (siblingsVisibleRgn, result);
		return result;
	}
	if (control == scrolledHandle) {
		if (!clipChildren) return super.getVisibleRegion (control, clipChildren);
		if (scrolledVisibleRgn == 0) {
			scrolledVisibleRgn = OS.NewRgn ();
			calculateVisibleRegion (control, scrolledVisibleRgn, clipChildren);
		}
		int result = OS.NewRgn ();
		OS.CopyRgn (scrolledVisibleRgn, result);
		return result;
	}
	return super.getVisibleRegion (control, clipChildren);
}

int kEventControlClick (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlClick (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if ((state & CANVAS) != 0) {
		if (!isEnabled ()) return result;
		if ((style & SWT.NO_FOCUS) == 0 && hooksKeys ()) {
			int [] theControl = new int [1];
			int window = OS.GetControlOwner (handle);
			OS.GetKeyboardFocus (window, theControl);
			if (handle != theControl [0]) {
				short [] count = new short [1];
				OS.CountSubControls (handle, count);
				if (count [0] == 0) {
					if (OS.SetKeyboardFocus (window, handle, (short) OS.kControlFocusNextPart) == OS.noErr) {
						return OS.noErr;
					}
				}
			}
		}
	}
	return result;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetFocusPart (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if ((state & CANVAS) != 0) {
		if (scrolledHandle != 0) {
			if ((style & SWT.NO_FOCUS) == 0 && hooksKeys ()) {
				short [] part = new short [1];
				OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
				drawFocusClipped (scrolledHandle, part [0] != 0, hasBorder (), getParentBackground (), inset ());
			}
		}
		return OS.noErr;
	}
	return result;
}

int kEventRawKey (int nextHandler, int theEvent, int userData) {
	/*
	* Feature in the Macintosh.  For some reason, the default handler
	* does not issue kEventTextInputUnicodeForKeyEvent when the user
	* types Command+Space.  The fix is to look for this case and
	* send the key from kEventRawKeyDown instead.
	* 
	* NOTE: This code relies on Command+Space being consumed and
	* will deliver two events if this ever changes.
	*/	
	if ((state & CANVAS) != 0) {
		int [] keyCode = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
		if (keyCode [0] == 49 /* Space */) {
			int [] modifiers = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
			if (modifiers [0] == OS.cmdKey) {
				if (!sendKeyEvent (SWT.KeyDown, theEvent)) return OS.noErr;
			}
		}
	}
	return OS.eventNotHandledErr;
}

int kEventTextInputUnicodeForKeyEvent (int nextHandler, int theEvent, int userData) {
	int result = super.kEventTextInputUnicodeForKeyEvent (nextHandler, theEvent, userData);
	if ((state & CANVAS) != 0) {
		int [] keyboardEvent = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendKeyboardEvent, OS.typeEventRef, null, keyboardEvent.length * 4, null, keyboardEvent);
		int [] keyCode = new int [1];
		OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
		switch (keyCode [0]) {
			case 36: /* Return */
				/*
				* Feature in the Macintosh.  The default behaviour when the return key is pressed is
				* to select the default button.  This is not the expected behaviour for Composite and
				* its subclasses.  The fix is to avoid calling the default handler.
				*/
				return OS.noErr;
		}
	}
	return result;
}

boolean hooksKeys () {
	return hooks (SWT.KeyDown) || hooks (SWT.KeyUp);
}

void invalidateChildrenVisibleRegion (int control) {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		child.resetVisibleRegion (control);
		child.invalidateChildrenVisibleRegion (control);
	}
}

boolean isTabGroup () {
	if ((state & CANVAS) != 0) return true;
	return super.isTabGroup ();
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

void releaseChildren () {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (!child.isDisposed ()) child.releaseResources ();
	}
}

void releaseWidget () {
	releaseChildren ();
	super.releaseWidget ();
	if (scrolledVisibleRgn != 0) OS.DisposeRgn (scrolledVisibleRgn);
	if (siblingsVisibleRgn != 0) OS.DisposeRgn (siblingsVisibleRgn);
	siblingsVisibleRgn = scrolledVisibleRgn = 0;
	layout = null;
	tabList = null;
}

void resetVisibleRegion (int control) {
	if (scrolledVisibleRgn != 0) {
		OS.DisposeRgn (scrolledVisibleRgn);
		scrolledVisibleRgn = 0;
	}
	if (siblingsVisibleRgn != 0) {
		OS.DisposeRgn (siblingsVisibleRgn);
		siblingsVisibleRgn = 0;
	}
	super.resetVisibleRegion (control);
}

int setBounds (int control, int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	int result = super.setBounds(control, x, y, width, height, move, resize, events);
	if (layout != null && (result & RESIZED) != 0) layout.layout (this, false);
	return result;
}

public boolean setFocus () {
	checkWidget ();
	Control [] children = _getChildren ();
	for (int i= 0; i < children.length; i++) {
		if (children [i].setFocus ()) return true;
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
	if (takeFocus && setTabItemFocus ()) return true;
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

void setZOrder () {
	super.setZOrder ();
	if (scrolledHandle != 0) OS.HIViewAddSubview (scrolledHandle, handle);
}

int traversalCode (int key, int theEvent) {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) return 0;
		if (hooksKeys ()) return 0;
	}
	return super.traversalCode (key, theEvent);
}

}

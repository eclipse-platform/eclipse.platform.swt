/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are controls which are capable
 * of containing other controls.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>NO_BACKGROUND, NO_FOCUS, NO_MERGE_PAINTS, NO_REDRAW_RESIZE, NO_RADIO_GROUP, EMBEDDED, DOUBLE_BUFFERED</dd>
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
	int font;
	WINDOWPOS [] lpwp;
	Control [] tabList;
	int layoutCount = 0;
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Composite () {
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
	int count = 0;
	int hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
	if (hwndChild == 0) return new Control [0];
	while (hwndChild != 0) {
		count++;
		hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
	}
	Control [] children = new Control [count];
	int index = 0;
	hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
	while (hwndChild != 0) {
		Control control = display.getControl (hwndChild);
		if (control != null && control != this) {
			children [index++] = control;
		}
		hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
	}
	if (count == index) return children;
	Control [] newChildren = new Control [index];
	System.arraycopy (children, 0, newChildren, 0, index);
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

/**
 * Clears any data that has been cached by a Layout for all widgets that 
 * are in the parent hierarchy of the changed control up to and including the 
 * receiver.  If an ancestor does not have a layout, it is skipped.
 * 
 * @param changed an array of controls that changed state and require a recalculation of size
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the changed array is null any of its controls are null or have been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void changed (Control[] changed) {
	checkWidget ();
	if (changed == null) error (SWT.ERROR_INVALID_ARGUMENT);
	for (int i=0; i<changed.length; i++) {
		Control control = changed [i];
		if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		boolean ancestor = false;
		Composite composite = control.parent;
		while (composite != null) {
			ancestor = composite == this;
			if (ancestor) break;
			composite = composite.parent;
		}
		if (!ancestor) error (SWT.ERROR_INVALID_PARENT);
	}
	for (int i=0; i<changed.length; i++) {
		Control child = changed [i];
		Composite composite = child.parent;
		while (child != this) {
			if (composite.layout == null || !composite.layout.flushCache (child)) {
				composite.state |= LAYOUT_CHANGED;
			}
			child = composite;
			composite = child.parent;
		}
	}
}

void checkBuffered () {
	if (OS.IsWinCE || (state & CANVAS) == 0) {
		super.checkBuffered ();
	}
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

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	Point size;
	if (layout != null) {
		if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
			changed |= (state & LAYOUT_CHANGED) != 0;
			state &= ~LAYOUT_CHANGED;
			size = layout.computeSize (this, wHint, hHint, changed);
		} else {
			size = new Point (wHint, hHint);
		}
	} else {
		size = minimumSize (wHint, hHint, changed);
	}
	if (size.x == 0) size.x = DEFAULT_WIDTH;
	if (size.y == 0) size.y = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	return new Point (trim.width, trim.height);
}

void createHandle () {
	super.createHandle ();
	state |= CANVAS;
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

/**
 * Returns a (possibly empty) array containing the receiver's children.
 * Children are returned in the order that they are drawn.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of children, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return an array of children
 * 
 * @see Control#moveAbove
 * @see Control#moveBelow
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control [] getChildren () {
	checkWidget ();
	return _getChildren ();
}

int getChildrenCount () {
	/*
	* NOTE: The current implementation will count
	* non-registered children.
	*/
	int count = 0;
	int hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
	while (hwndChild != 0) {
		count++;
		hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
	}
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
	checkWidget ();
	return layout;
}

/**
 * Gets the (possibly empty) tabbing order for the control.
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

boolean hooksKeys () {
	return hooks (SWT.KeyDown) || hooks (SWT.KeyUp);
}

/**
 * Returns <code>true</code> if the receiver has deferred
 * the performing of layout, and <code>false</code> otherwise.
 *
 * @return the receiver's deferred layout state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setLayoutDeferred(boolean)
 * @see #isLayoutDeferred()
 *
 * @since 3.1
 */
public boolean getLayoutDeferred () {
	checkWidget ();
	return layoutCount > 0 ;
}

/**
 * Returns <code>true</code> if the receiver or any ancestor 
 * up to and including the receiver's nearest ancestor shell
 * has deferred the performing of layouts.  Otherwise, <code>false</code>
 * is returned.
 *
 * @return the receiver's deferred layout state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setLayoutDeferred(boolean)
 * @see #getLayoutDeferred()
 * 
 * @since 3.1
 */
public boolean isLayoutDeferred () {
	checkWidget ();
	return layoutCount > 0 || parent.isLayoutDeferred ();
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
	checkWidget ();
	layout (true);
}

/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the argument is <code>true</code> the layout must not rely
 * on any information it has cached about the immediate children. If it
 * is <code>false</code> the layout may (potentially) optimize the
 * work it is doing by assuming that none of the receiver's 
 * children has changed state since the last layout.
 * If the receiver does not have a layout, do nothing.
 * <p>
 * If a child is resized as a result of a call to layout, the 
 * resize event will invoke the layout of the child.  The layout
 * will cascade down through all child widgets in the receiver's widget 
 * tree until a child is encountered that does not resize.  Note that 
 * a layout due to a resize will not flush any cached information 
 * (same as <code>layout(false)</code>).</p>
 *
 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout (boolean changed) {
	checkWidget ();
	if (layout == null) return;
	layout (changed, false);
}

/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the changed argument is <code>true</code> the layout must not rely
 * on any information it has cached about its children. If it
 * is <code>false</code> the layout may (potentially) optimize the
 * work it is doing by assuming that none of the receiver's 
 * children has changed state since the last layout.
 * If the all argument is <code>true</code> the layout will cascade down
 * through all child widgets in the receiver's widget tree, regardless of
 * whether the child has changed size.  The changed argument is applied to 
 * all layouts.  If the all argument is <code>false</code>, the layout will
 * <em>not</em> cascade down through all child widgets in the receiver's widget 
 * tree.  However, if a child is resized as a result of a call to layout, the 
 * resize event will invoke the layout of the child.  Note that 
 * a layout due to a resize will not flush any cached information 
 * (same as <code>layout(false)</code>).</p>
 *
 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
 * @param all <code>true</code> if all children in the receiver's widget tree should be laid out, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void layout (boolean changed, boolean all) {
	checkWidget ();
	if (layout == null && !all) return;
	markLayout (changed, all);
	updateLayout (true, all);
}

/**
 * Forces a lay out (that is, sets the size and location) of all widgets that 
 * are in the parent hierarchy of the changed control up to and including the 
 * receiver.  The layouts in the hierarchy must not rely on any information 
 * cached about the changed control or any of its ancestors.  The layout may 
 * (potentially) optimize the work it is doing by assuming that none of the 
 * peers of the changed control have changed state since the last layout.
 * If an ancestor does not have a layout, skip it.
 * 
 * @param changed a control that has had a state change which requires a recalculation of its size
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the changed array is null any of its controls are null or have been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void layout (Control [] changed) {
	checkWidget ();
	if (changed == null) error (SWT.ERROR_INVALID_ARGUMENT);
	for (int i=0; i<changed.length; i++) {
		Control control = changed [i];
		if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		boolean ancestor = false;
		Composite composite = control.parent;
		while (composite != null) {
			ancestor = composite == this;
			if (ancestor) break;
			composite = composite.parent;
		}
		if (!ancestor) error (SWT.ERROR_INVALID_PARENT);
	}
	int updateCount = 0;
	Composite [] update = new Composite [16];
	for (int i=0; i<changed.length; i++) {
		Control child = changed [i];
		Composite composite = child.parent;
		while (child != this) {
			if (composite.layout != null) {
				composite.state |= LAYOUT_NEEDED;
				if (!composite.layout.flushCache (child)) {
					composite.state |= LAYOUT_CHANGED;
				}
			}
			if (updateCount == update.length) {
				Composite [] newUpdate = new Composite [update.length + 16];
				System.arraycopy (update, 0, newUpdate, 0, update.length);
				update = newUpdate;
			}
			child = update [updateCount++] = composite;
			composite = child.parent;
		}
	}
	for (int i=updateCount-1; i>=0; i--) {
		update [i].updateLayout (true, false);
	}
}

void markLayout (boolean changed, boolean all) {
	if (layout != null) {
		state |= LAYOUT_NEEDED;
		if (changed) state |= LAYOUT_CHANGED;
	}
	if (all) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			children [i].markLayout (changed, all);
		}
	}
}

Point minimumSize (int wHint, int hHint, boolean changed) {
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
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		int hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
		if (hwndChild != 0) {
			int threadId = OS.GetWindowThreadProcessId (hwndChild, null);
			if (threadId != OS.GetCurrentThreadId ()) {
				OS.SetParent (hwndChild, 0);
			}
		}
	}
	layout = null;
	tabList = null;
	lpwp = null;
}

void removeControl (Control control) {
	fixTabList (control);
	resizeChildren ();
}

void resizeChildren () {
	if (lpwp == null) return;
	do {
		WINDOWPOS [] currentLpwp = lpwp;
		lpwp = null;
		if (!resizeChildren (true, currentLpwp)) {
			resizeChildren (false, currentLpwp);
		}
	} while (lpwp != null);
}

boolean resizeChildren (boolean defer, WINDOWPOS [] pwp) {
	if (pwp == null) return true;
	int hdwp = 0;
	if (defer) {
		hdwp = OS.BeginDeferWindowPos (pwp.length);
		if (hdwp == 0) return false;
	}
	for (int i=0; i<pwp.length; i++) {
		WINDOWPOS wp = pwp [i];
		if (wp != null) {
			/*
			* This code is intentionally commented.  All widgets that
			* are created by SWT have WS_CLIPSIBLINGS to ensure that
			* application code does not draw outside of the control.
			*/
//			int count = parent.getChildrenCount ();
//			if (count > 1) {
//				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//				if ((bits & OS.WS_CLIPSIBLINGS) == 0) wp.flags |= OS.SWP_NOCOPYBITS;
//			}
			if (defer) {
				hdwp = DeferWindowPos (hdwp, wp.hwnd, 0, wp.x, wp.y, wp.cx, wp.cy, wp.flags);
				if (hdwp == 0) return false;
			} else {
				SetWindowPos (wp.hwnd, 0, wp.x, wp.y, wp.cx, wp.cy, wp.flags);
			}
		}
	}
	if (defer) return OS.EndDeferWindowPos (hdwp);
	return true;
}

void resizeEmbeddedHandle(int embeddedHandle, int width, int height) {
	if (embeddedHandle == 0) return;
	int [] processID = new int [1];
	int threadId = OS.GetWindowThreadProcessId (embeddedHandle, processID);
	if (threadId != OS.GetCurrentThreadId ()) {
		if (processID [0] == OS.GetCurrentProcessId ()) {
			if (display.msgHook == 0) {
				if (!OS.IsWinCE) {
					display.getMsgCallback = new Callback (display, "getMsgProc", 3);
					display.getMsgProc = display.getMsgCallback.getAddress ();
					if (display.getMsgProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
					display.msgHook = OS.SetWindowsHookEx (OS.WH_GETMESSAGE, display.getMsgProc, OS.GetLibraryHandle(), threadId);
					OS.PostThreadMessage (threadId, OS.WM_NULL, 0, 0);
				}
			}
		}
		int flags = OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE | OS.SWP_ASYNCWINDOWPOS;
		OS.SetWindowPos (embeddedHandle, 0, 0, 0, width, height, flags);
	}
}

boolean setFixedFocus () {
	checkWidget ();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.setRadioFocus ()) return true;
	}
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.setFixedFocus ()) return true;
	}
	return super.setFixedFocus ();
}

public boolean setFocus () {
	checkWidget ();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.setRadioFocus ()) return true;
	}
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
	checkWidget ();
	this.layout = layout;
}

/**
 * If the argument is <code>true</code>, causes subsequent layout
 * operations in the receiver or any of its children to be ignored.
 * No layout of any kind can occur in the receiver or any of its
 * children until the flag is set to false.
 * Layout operations that occurred while the flag was
 * <code>true</code> are remembered and when the flag is set to 
 * <code>false</code>, the layout operations are performed in an
 * optimized manner.  Nested calls to this method are stacked.
 *
 * @param defer the new defer state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #layout(boolean)
 * @see #layout(Control[])
 *
 * @since 3.1
 */
public void setLayoutDeferred (boolean defer) {
	if (!defer) {
		if (--layoutCount == 0) {
			if (!isLayoutDeferred ()) updateLayout (true, true);
		}
	} else {
		layoutCount++;
	}
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

void setResizeChildren (boolean resize) {
	if (resize) {
		resizeChildren ();
	} else {
		int count = getChildrenCount ();
		if (count > 1 && lpwp == null) {
			lpwp = new WINDOWPOS [count];
		}
	}
}

boolean setTabGroupFocus () {
	if (isTabItem ()) return setTabItemFocus ();
	boolean takeFocus = (style & SWT.NO_FOCUS) == 0;
	if ((state & CANVAS) != 0) {
		takeFocus = hooksKeys ();
		if ((style & SWT.EMBEDDED) != 0) takeFocus = true;
	}
	if (takeFocus && setTabItemFocus ()) return true;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.isTabItem () && child.setRadioFocus ()) return true;
	}
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.isTabItem () && child.setTabItemFocus ()) return true;
	}
	return false;
}

String toolTipText (NMTTDISPINFO hdr) {
	if ((hdr.uFlags & OS.TTF_IDISHWND) == 0) {
		return null;
	}
	int hwnd = hdr.idFrom;
	if (hwnd == 0) return null;
	Control control = display.getControl (hwnd);
	if (control == null) return null;
	return control.toolTipText;
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

boolean translateTraversal (MSG msg) {
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) return false;
	return super.translateTraversal (msg);
}

boolean updateFont (Font oldFont, Font newFont) {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control control = children [i];
		if (!control.isDisposed ()) {
			control.updateFont (oldFont, newFont);
		}
	}
	boolean changed = super.updateFont (oldFont, newFont);
	if (changed) {
		/*
		* Call layout() directly so that subclasses that reimplement
		* this method instead of using a Layout will set the size and
		* location of their children when the font changes.
		*/
		layout (true);
	}
	return changed;
}

void updateLayout (boolean resize, boolean all) {
	if (isLayoutDeferred ()) return;
	if ((state & LAYOUT_NEEDED) != 0) {
		boolean changed = (state & LAYOUT_CHANGED) != 0;
		state &= ~(LAYOUT_NEEDED | LAYOUT_CHANGED);
		if (resize) setResizeChildren (false);
		layout.layout (this, changed);
		if (resize) setResizeChildren (true);
	}
	if (all) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			children [i].updateLayout (resize, all);
		}
	}
}

int widgetStyle () {
	/* Force clipping of children by setting WS_CLIPCHILDREN */
	return super.widgetStyle () | OS.WS_CLIPCHILDREN;
}

LRESULT WM_ERASEBKGND (int wParam, int lParam) {
	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
	if (result != null) return result;
	if ((state & CANVAS) != 0) {
		/* Return zero to indicate that the background was not erased */
		if ((style & SWT.NO_BACKGROUND) != 0) return LRESULT.ZERO;
	}
	return result;
}

LRESULT WM_GETDLGCODE (int wParam, int lParam) {
	LRESULT result = super.WM_GETDLGCODE (wParam, lParam);
	if (result != null) return result;
	if ((state & CANVAS) != 0) {
		int flags = 0;
		if (hooksKeys ()) {
			flags |= OS.DLGC_WANTALLKEYS | OS.DLGC_WANTARROWS | OS.DLGC_WANTTAB;
		}
		if ((style & SWT.NO_FOCUS) != 0) flags |= OS.DLGC_STATIC;
		if (OS.GetWindow (handle, OS.GW_CHILD) != 0) flags |= OS.DLGC_STATIC;
		if (flags != 0) return new LRESULT (flags);
	}
	return result;
}

LRESULT WM_GETFONT (int wParam, int lParam) {
	LRESULT result = super.WM_GETFONT (wParam, lParam);
	if (result != null) return result;
	int code = callWindowProc (handle, OS.WM_GETFONT, wParam, lParam);
	if (code != 0) return new LRESULT (code);
	if (font == 0) font = defaultFont ();
	return new LRESULT (font);
}

LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
	LRESULT result = super.WM_LBUTTONDOWN (wParam, lParam);

	/* Set focus for a canvas with no children */
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) == 0 && hooksKeys ()) {
			if (OS.GetWindow (handle, OS.GW_CHILD) == 0) setFocus ();
		}
	}
	return result;
}

LRESULT WM_NOTIFY (int wParam, int lParam) {
	if (!OS.IsWinCE) {
		NMHDR hdr = new NMHDR ();
		OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
		switch (hdr.code) {
			/*
			* Feature in Windows.  When the tool tip control is
			* created, the parent of the tool tip is the shell.
			* If SetParent () is used to reparent the tool bar
			* into a new shell, the tool tip is not reparented
			* and pops up underneath the new shell.  The fix is
			* to make sure the tool tip is a topmost window.
			*/
			case OS.TTN_SHOW:
			case OS.TTN_POP: {
				/*
				* Bug in Windows 98 and NT.  Setting the tool tip to be the
				* top most window using HWND_TOPMOST can result in a parent
				* dialog shell being moved behind its parent if the dialog
				* has a sibling that is currently on top.  The fix is to
				* lock the z-order of the active window.
				* 
				* Feature in Windows.  Using SetWindowPos() with HWND_NOTOPMOST
				* to clear the topmost state of a window whose parent is already
				* topmost clears the topmost state of the parent.  The fix is to
				* check if the parent is already on top and neither set or clear
				* the topmost status of the tool tip.
				*/
				int hwndParent = hdr.hwndFrom;
				do {
					hwndParent = OS.GetParent (hwndParent);
					if (hwndParent == 0) break;
					int bits = OS.GetWindowLong (hwndParent, OS.GWL_EXSTYLE);
					if ((bits & OS.WS_EX_TOPMOST) != 0) break;
				} while (true);
				if (hwndParent != 0) break;
				display.lockActiveWindow = true;
				int flags = OS.SWP_NOACTIVATE | OS.SWP_NOMOVE | OS.SWP_NOSIZE;
				int hwndInsertAfter = hdr.code == OS.TTN_SHOW ? OS.HWND_TOPMOST : OS.HWND_NOTOPMOST;
				SetWindowPos (hdr.hwndFrom, hwndInsertAfter, 0, 0, 0, 0, flags);
				display.lockActiveWindow = false;
				break;
			}
			/*
			* Bug in Windows 98.  For some reason, the tool bar control
			* sends both TTN_GETDISPINFOW and TTN_GETDISPINFOA to get
			* the tool tip text and the tab folder control sends only 
			* TTN_GETDISPINFOW.  The fix is to handle only TTN_GETDISPINFOW,
			* even though it should never be sent on Windows 98.
			*
			* NOTE:  Because the size of NMTTDISPINFO differs between
			* Windows 98 and NT, guard against the case where the wrong
			* kind of message occurs by inlining the memory moves and
			* the UNICODE conversion code.
			*/
			case OS.TTN_GETDISPINFOA:
			case OS.TTN_GETDISPINFOW: {
				NMTTDISPINFO lpnmtdi;
				if (hdr.code == OS.TTN_GETDISPINFOA) {
					lpnmtdi = new NMTTDISPINFOA ();
					OS.MoveMemory ((NMTTDISPINFOA)lpnmtdi, lParam, NMTTDISPINFOA.sizeof);
				} else {
					lpnmtdi = new NMTTDISPINFOW ();
					OS.MoveMemory ((NMTTDISPINFOW)lpnmtdi, lParam, NMTTDISPINFOW.sizeof);
				}
				String string = toolTipText (lpnmtdi);
				if (string != null) {
					Shell shell = getShell ();
					string = Display.withCrLf (string);
					int length = string.length ();
					char [] chars = new char [length + 1];
					string.getChars (0, length, chars, 0);
					
					/*
					* Ensure that the orientation of the tool tip matches
					* the orientation of the control.
					*/
					int hwnd = hdr.idFrom;
					if (hwnd != 0 && ((lpnmtdi.uFlags & OS.TTF_IDISHWND) != 0)) {
						Control control = display.getControl (hwnd);
						if (control != null) {
							if ((control.getStyle () & SWT.RIGHT_TO_LEFT) != 0) {
								lpnmtdi.uFlags |= OS.TTF_RTLREADING;
							} else {
								lpnmtdi.uFlags &= ~OS.TTF_RTLREADING;
							}
						}
					}
					
					if (hdr.code == OS.TTN_GETDISPINFOA) {
						byte [] bytes = new byte [chars.length * 2];
						OS.WideCharToMultiByte (OS.CP_ACP, 0, chars, chars.length, bytes, bytes.length, null, null);
						shell.setToolTipText (lpnmtdi, bytes);
						OS.MoveMemory (lParam, (NMTTDISPINFOA)lpnmtdi, NMTTDISPINFOA.sizeof);
					} else {
						shell.setToolTipText (lpnmtdi, chars);
						OS.MoveMemory (lParam, (NMTTDISPINFOW)lpnmtdi, NMTTDISPINFOW.sizeof);
					}
					return LRESULT.ZERO;
				}
				break;
			}
		}
	}
	return super.WM_NOTIFY (wParam, lParam);
}

LRESULT WM_PARENTNOTIFY (int wParam, int lParam) {
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		if ((wParam & 0xFFFF) == OS.WM_CREATE) {
			RECT rect = new RECT ();
			OS.GetClientRect (handle, rect);
			resizeEmbeddedHandle (lParam, rect.right - rect.left, rect.bottom - rect.top);
		}
	}	
	return super.WM_PARENTNOTIFY (wParam, lParam);
}

LRESULT WM_PAINT (int wParam, int lParam) {
	if ((state & CANVAS) == 0) {
		return super.WM_PAINT (wParam, lParam);
	}

	/* Set the clipping bits */
	int oldBits = 0, newBits = 0;
	if (!OS.IsWinCE) {
		oldBits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		newBits = oldBits | OS.WS_CLIPSIBLINGS | OS.WS_CLIPCHILDREN;	
		if (newBits != oldBits) OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
	}

	/* Paint the control and the background */
	PAINTSTRUCT ps = new PAINTSTRUCT ();
	if (hooks (SWT.Paint)) {
		
		/* Get the damage */
		int [] lpRgnData = null;
		boolean isComplex = false;
		boolean exposeRegion = false;

		int rgn = 0;
		if ((style & (SWT.NO_MERGE_PAINTS | SWT.DOUBLE_BUFFERED)) != 0) {
			rgn = OS.CreateRectRgn (0, 0, 0, 0);
			isComplex = OS.GetUpdateRgn (handle, rgn, false) == OS.COMPLEXREGION;
		}

		if ((style & SWT.NO_MERGE_PAINTS) != 0) {
			if (isComplex) {
				int nBytes = OS.GetRegionData (rgn, 0, null);
				lpRgnData = new int [nBytes / 4];
				exposeRegion = OS.GetRegionData (rgn, nBytes, lpRgnData) != 0;
			}
		}
	
		/* Create the paint GC */
		GCData data = new GCData ();
		data.ps = ps;
		data.hwnd = handle;
		GC gc = GC.win32_new (this, data);
		
		/* Send the paint event */
		int width = ps.right - ps.left;
		int height = ps.bottom - ps.top;
		if (width != 0 && height != 0) {
			GC paintGC = null;
			Image image = null;
			if ((style & SWT.DOUBLE_BUFFERED) != 0) {
				image = new Image (display, width, height);
				paintGC = gc;
				gc = new GC (image, style & SWT.RIGHT_TO_LEFT);
				gc.setForeground (getForeground ());
				gc.setBackground (getBackground ());
				gc.setFont (getFont ());
				if ((style & SWT.NO_BACKGROUND) != 0) {
					paintGC.copyArea (image, ps.left, ps.top);
				} else {
					gc.fillRectangle (0, 0, width, height);
				}
				OS.OffsetRgn (rgn, -ps.left, -ps.top);
				OS.SelectClipRgn (gc.handle, rgn);
				OS.SetMetaRgn (gc.handle);
				OS.SetWindowOrgEx (gc.handle, ps.left, ps.top, null);
			}
			Event event = new Event ();
			event.gc = gc;
			if (isComplex && exposeRegion) {
				RECT rect = new RECT ();
				int count = lpRgnData [2];
				for (int i=0; i<count; i++) {
					OS.SetRect (rect,
						lpRgnData [8 + (i << 2)],
						lpRgnData [8 + (i << 2) + 1],
						lpRgnData [8 + (i << 2) + 2],
						lpRgnData [8 + (i << 2) + 3]);
					if ((style & (SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND)) == 0) {
						drawBackground (gc.handle, rect);
					}
					event.x = rect.left;
					event.y = rect.top;
					event.width = rect.right - rect.left;
					event.height = rect.bottom - rect.top;
					event.count = count - 1 - i;
					/*
					* It is possible (but unlikely), that application
					* code could have disposed the widget in the paint
					* event.  If this happens, attempt to give back the
					* paint GC anyways because this is a scarce Windows
					* resource.
					*/
					sendEvent (SWT.Paint, event);
					if (isDisposed ()) break;
				}
			} else {
				if ((style & (SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND)) == 0) {
					RECT rect = new RECT ();
					OS.SetRect (rect, ps.left, ps.top, ps.right, ps.bottom);
					drawBackground (gc.handle, rect);
				}
				event.x = ps.left;
				event.y = ps.top;
				event.width = width;
				event.height = height;
				sendEvent (SWT.Paint, event);
			}
			// widget could be disposed at this point
			event.gc = null;
			if ((style & SWT.DOUBLE_BUFFERED) != 0) {
				gc.dispose();
				if (!isDisposed ()) {
					paintGC.drawImage(image, ps.left, ps.top);
				}
				image.dispose();
				gc = paintGC;
			}
		}
		
		/* Dispose the paint GC */
		gc.dispose ();
		if (rgn != 0) OS.DeleteObject (rgn);
	} else {
		int hDC = OS.BeginPaint (handle, ps);
		if ((style & SWT.NO_BACKGROUND) == 0) {
			RECT rect = new RECT ();
			OS.SetRect (rect, ps.left, ps.top, ps.right, ps.bottom);
			drawBackground (hDC, rect);
		}
		OS.EndPaint (handle, ps);
	}

	/* Restore the clipping bits */
	if (!OS.IsWinCE) { 
		if (newBits != oldBits) {
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the paint
			* event.  If this happens, don't attempt to restore
			* the style.
			*/
			if (!isDisposed ()) {
				OS.SetWindowLong (handle, OS.GWL_STYLE, oldBits);
			}
		}
	}
	return LRESULT.ZERO;
}

LRESULT WM_PRINTCLIENT (int wParam, int lParam) {
	LRESULT result = super.WM_PRINTCLIENT (wParam, lParam);
	if (result != null) return result;
	if ((state & CANVAS) != 0) {
		forceResize ();
		int nSavedDC = OS.SaveDC (wParam);
		RECT rect = new RECT ();
		OS.GetClientRect (handle, rect);
		if ((style & SWT.NO_BACKGROUND) == 0) {
			drawBackground (wParam, rect);
		}
		if (hooks (SWT.Paint) || filters (SWT.Paint)) {
			GCData data = new GCData ();
			data.device = display;
			GC gc = GC.win32_new (wParam, data);
			Event event = new Event ();
			event.gc = gc;
			event.x = rect.left;
			event.y = rect.top;
			event.width = rect.right - rect.left;
			event.height = rect.bottom - rect.top;
			sendEvent (SWT.Paint, event);
			event.gc = null;
			gc.dispose ();
		}
		OS.RestoreDC (wParam, nSavedDC);
	}
	return result;
}

LRESULT WM_SETFONT (int wParam, int lParam) {
	return super.WM_SETFONT (font = wParam, lParam);
}

LRESULT WM_SIZE (int wParam, int lParam) {

	/* Begin deferred window positioning */
	setResizeChildren (false);
	
	/* Resize and Layout */
	LRESULT result = super.WM_SIZE (wParam, lParam);
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the resize
	* event.  If this happens, end the processing of the
	* Windows message by returning the result of the
	* WM_SIZE message.
	*/
	if (isDisposed ()) return result;
	if (layout != null) {
		markLayout (false, false);
		updateLayout (false, false);
	}

	/* End deferred window positioning */
	setResizeChildren (true);
	
	/* Damage the widget to cause a repaint */
	if (OS.IsWindowVisible (handle)) {
		if ((state & CANVAS) != 0) {
			if ((style & SWT.NO_REDRAW_RESIZE) == 0) {
				if (hooks (SWT.Paint)) {
					OS.InvalidateRect (handle, null, true);
				}
			}
		}
		if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
			if (findThemeControl () != null) {
				int hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
				while (hwndChild != 0) {
					int flags = OS.RDW_ERASE | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
					OS.RedrawWindow (hwndChild, null, 0, flags);
					hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
				}
			}
		}
	}

	/* Resize the embedded window */
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		resizeEmbeddedHandle (OS.GetWindow (handle, OS.GW_CHILD), lParam & 0xFFFF, lParam >> 16);
	}
	return result;
}

LRESULT WM_SYSCOLORCHANGE (int wParam, int lParam) {
	int hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
	while (hwndChild != 0) {
		OS.SendMessage (hwndChild, OS.WM_SYSCOLORCHANGE, 0, 0);
		hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
	}
	return null;
}

LRESULT WM_SYSCOMMAND (int wParam, int lParam) {
	LRESULT result = super.WM_SYSCOMMAND (wParam, lParam);
	if (result != null) return result;
		
	/*
	* Check to see if the command is a system command or
	* a user menu item that was added to the system menu.
	*/
	if ((wParam & 0xF000) == 0) return result;

	/*
	* Bug in Windows.  When a vertical or horizontal scroll bar is
	* hidden or shown while the opposite scroll bar is being scrolled
	* by the user (with WM_HSCROLL code SB_LINEDOWN), the scroll bar
	* does not redraw properly.  The fix is to detect this case and
	* redraw the non-client area.
	*/
	if (!OS.IsWinCE) {
		int cmd = wParam & 0xFFF0;
		switch (cmd) {
			case OS.SC_HSCROLL:
			case OS.SC_VSCROLL:
				boolean showHBar = horizontalBar != null && horizontalBar.getVisible ();
				boolean showVBar = verticalBar != null && verticalBar.getVisible ();
				int code = callWindowProc (handle, OS.WM_SYSCOMMAND, wParam, lParam);
				if ((showHBar != (horizontalBar != null && horizontalBar.getVisible ())) ||
					(showVBar != (verticalBar != null && verticalBar.getVisible ()))) {
						int flags = OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_UPDATENOW;
						OS.RedrawWindow (handle, null, 0, flags);
					}		
				if (code == 0) return LRESULT.ZERO;
				return new LRESULT (code);
		}
	}
	/* Return the result */
	return result;
}

}

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


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
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
	int font;
	WINDOWPOS [] lpwp;
	Control [] tabList;
	
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
	checkWidget ();
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
	checkWidget ();
	if (layout == null) return;
	setResizeChildren (false);
	layout.layout (this, changed);
	setResizeChildren (true);
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
				hdwp = OS.DeferWindowPos (hdwp, wp.hwnd, 0, wp.x, wp.y, wp.cx, wp.cy, wp.flags);
				if (hdwp == 0) return false;
			} else {
				OS.SetWindowPos (wp.hwnd, 0, wp.x, wp.y, wp.cx, wp.cy, wp.flags);
			}
		}
	}
	if (defer) return OS.EndDeferWindowPos (hdwp);
	return true;
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
	if ((state & CANVAS) != 0) takeFocus = hooksKeys ();
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

void updateFont (Font oldFont, Font newFont) {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control control = children [i];
		if (!control.isDisposed ()) {
			control.updateFont (oldFont, newFont);
		}
	}
	super.updateFont (oldFont, newFont);
	layout (true);
}

int widgetStyle () {
	/* Force clipping of children by setting WS_CLIPCHILDREN */
	return super.widgetStyle () | OS.WS_CLIPCHILDREN;
}

LRESULT WM_ERASEBKGND (int wParam, int lParam) {
	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
	if (result != null) return result;
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_BACKGROUND) != 0) return LRESULT.ONE;
	}
	return result;
}

LRESULT WM_GETDLGCODE (int wParam, int lParam) {
	LRESULT result = super.WM_GETDLGCODE (wParam, lParam);
	if (result != null) return result;
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) return new LRESULT (OS.DLGC_STATIC);
		if (hooksKeys ()) {
			int flags = OS.DLGC_WANTALLKEYS | OS.DLGC_WANTARROWS | OS.DLGC_WANTTAB;
			return new LRESULT (flags);
		}
		if (OS.GetWindow (handle, OS.GW_CHILD) != 0) return new LRESULT (OS.DLGC_STATIC);
	}
	return result;
}

LRESULT WM_GETFONT (int wParam, int lParam) {
	LRESULT result = super.WM_GETFONT (wParam, lParam);
	if (result != null) return result;
	int code = callWindowProc (OS.WM_GETFONT, wParam, lParam);
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
				* has a sibling that is currently on top.  The fix is to lock
				* the z-order of the active window.
				*/
				display.lockActiveWindow = true;
				int flags = OS.SWP_NOACTIVATE | OS.SWP_NOMOVE | OS.SWP_NOSIZE;
				int hwndInsertAfter = hdr.code == OS.TTN_SHOW ? OS.HWND_TOPMOST : OS.HWND_NOTOPMOST;
				OS.SetWindowPos (hdr.hwndFrom, hwndInsertAfter, 0, 0, 0, 0, flags);
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
		if ((style & SWT.NO_MERGE_PAINTS) != 0) {
			int rgn = OS.CreateRectRgn (0, 0, 0, 0);
			isComplex = OS.GetUpdateRgn (handle, rgn, false) == OS.COMPLEXREGION;
			if (isComplex) {
				int nBytes = OS.GetRegionData (rgn, 0, null);
				lpRgnData = new int [nBytes / 4];
				exposeRegion = OS.GetRegionData (rgn, nBytes, lpRgnData) != 0;
			}
			OS.DeleteObject (rgn);
		}
	
		/* Create the paint GC */
		GCData data = new GCData ();
		data.ps = ps;
		GC gc = GC.win32_new (this, data);
		int hDC = gc.handle;
		
		/* Send the paint event */
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
				if ((style & SWT.NO_BACKGROUND) == 0) {
					drawBackground (hDC, rect);
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
			if ((style & SWT.NO_BACKGROUND) == 0) {
				RECT rect = new RECT ();
				OS.SetRect (rect, ps.left, ps.top, ps.right, ps.bottom);
				drawBackground (hDC, rect);
			}
			event.x = ps.left;
			event.y = ps.top;
			event.width = ps.right - ps.left;
			event.height = ps.bottom - ps.top;
			sendEvent (SWT.Paint, event);
		}
		// widget could be disposed at this point
	
		/* Dispose the paint GC */
		event.gc = null;
		gc.dispose ();
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
		RECT rect = new RECT ();
		OS.GetClientRect (handle, rect);
		if ((style & SWT.NO_BACKGROUND) == 0) {
			drawBackground (wParam, rect);
		}
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
	if (layout != null) layout.layout (this, false);

	/* End deferred window positioning */
	setResizeChildren (true);
	
	/* Damage the widget to cause a repaint */
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_REDRAW_RESIZE) == 0) {
			if (hooks (SWT.Paint)) {
				OS.InvalidateRect (handle, null, true);
			}
		}
	}

	/* Resize the embedded window */
	if ((state & CANVAS) != 0 && (style & SWT.EMBEDDED) != 0) {
		int hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
		int threadId = OS.GetWindowThreadProcessId (hwndChild, null);
		if (threadId != OS.GetCurrentThreadId ()) {
			if (display.msgHook == 0) {
				if (!OS.IsWinCE) {
					display.getMsgCallback = new Callback (display, "getMsgProc", 3);
					display.getMsgProc = display.getMsgCallback.getAddress ();
					if (display.getMsgProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
					display.msgHook = OS.SetWindowsHookEx (OS.WH_GETMESSAGE, display.getMsgProc, OS.GetLibraryHandle(), threadId);
				}
			}
			OS.PostThreadMessage (threadId, Display.SWT_RESIZE, hwndChild, lParam);
		}
	}
	return result;
}

LRESULT WM_SYSCOLORCHANGE (int wParam, int lParam) {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		int hwndChild = children [i].handle;
		OS.SendMessage (hwndChild, OS.WM_SYSCOLORCHANGE, 0, 0);
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
				int code = callWindowProc (OS.WM_SYSCOMMAND, wParam, lParam);
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

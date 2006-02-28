/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

//TODO - Javadoc
/**
 * Instances of this class represent ... NOT DONE
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BALLOON, ICON_ERROR, ICON_INFORMATION, ICON_WARNING</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */

/*public*/ class ToolTip extends Widget {
	Shell parent;
	TrayItem item;
	String text = "", message = "";
	int id, x, y;
	boolean hasLocation, visible;

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
 * @see SWT#ERROR
 * @see SWT#INFORMATION
 * @see SWT#WARNING
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolTip (Shell parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	checkOrientation (parent);
	parent.createToolTip (this);
}

static int checkStyle (int style) {
	int mask = SWT.ICON_ERROR | SWT.ICON_INFORMATION | SWT.ICON_WARNING;
	if ((style & mask) == 0) return style;
	return checkBits (style, SWT.ICON_INFORMATION, SWT.ICON_WARNING, SWT.ICON_ERROR, 0, 0, 0);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's value changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

void destroyWidget () {
	if (parent != null) parent.destroyToolTip (this);
	releaseHandle ();
}

/**
 * Returns the receiver's message, which will be an empty
 * string if it has never been set.
 *
 * @return the receiver's message
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getMessage () {
	checkWidget();
	return message;
}

/**
 * Returns the receiver's parent, which must be a <code>Shell</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Shell getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget();
	return text;
}

public boolean getVisible () {
	checkWidget();
	if (OS.IsWinCE) return false;
	if (item != null) return visible;
	int hwndToolTip = (style & SWT.BALLOON) != 0 ? parent.balloonTipHandle () : parent.toolTipHandle ();
	if (OS.SendMessage (hwndToolTip, OS.TTM_GETCURRENTTOOL, 0, 0) != 0) {
		TOOLINFO lpti = new TOOLINFO ();
		lpti.cbSize = TOOLINFO.sizeof;
		if (OS.SendMessage (hwndToolTip, OS.TTM_GETCURRENTTOOL, 0, lpti) != 0) {
			return (lpti.uFlags & OS.TTF_IDISHWND) == 0 && lpti.uId == id;
		}
	}
	return false;
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * of the receiver's ancestors are visible and <code>false</code>
 * otherwise.
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getVisible
 */
public boolean isVisible () {
	checkWidget ();
	if (item != null) return getVisible () && item.getVisible ();
	return getVisible ();
}

void releaseHandle () {
	super.releaseHandle ();
	parent = null;
	item = null;
	id = -1;
}

void releaseWidget () {
	super.releaseWidget ();
	if (item != null && item.toolTip == this) {
		item.toolTip = null;
	}
	item = null;
	text = message = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's value changes.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Sets the location of the receiver, which must be a popup,
 * to the point specified by the arguments which are relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p>
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (int x, int y) {
	checkWidget ();
	this.x = x;
	this.y = y;
	hasLocation = true;
	//TODO - update when visible
}

/**
 * Sets the location of the receiver, which must be a popup,
 * to the point specified by the argument which is relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Note that the platform window manager ultimately has control
 * over the location of popup menus.
 * </p>
 *
 * @param location the new location for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (Point location) {
	checkWidget ();
	if (location == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

/**
 * Sets the receiver's message.
 *
 * @param string the new message
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMessage (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	message = string;
	//TODO - update when visible
}

/**
 * Sets the receiver's text.
 *
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	text = string;
	//TODO - update when visible
}

public void setVisible (boolean visible) {
	checkWidget ();
	if (OS.IsWinCE) return;
	if (visible == getVisible ()) return;
	if (item == null) {
		int hwnd = parent.handle;
		TOOLINFO lpti = new TOOLINFO ();
		lpti.cbSize = TOOLINFO.sizeof;
		lpti.uId = id;
		lpti.hwnd = hwnd;
		int hwndToolTip = (style & SWT.BALLOON) != 0 ? parent.balloonTipHandle () : parent.toolTipHandle ();
		if (text.length () != 0) {
			int icon = OS.TTI_NONE;
			if ((style & SWT.ICON_INFORMATION) != 0) icon = OS.TTI_INFO;
			if ((style & SWT.ICON_WARNING) != 0) icon = OS.TTI_WARNING;
			if ((style & SWT.ICON_ERROR) != 0) icon = OS.TTI_ERROR;
			TCHAR pszTitle = new TCHAR (parent.getCodePage (), text, true);
			OS.SendMessage (hwndToolTip, OS.TTM_SETTITLE, icon, pszTitle);
		} else {
			OS.SendMessage (hwndToolTip, OS.TTM_SETTITLE, 0, 0);
		}
		int maxWidth = 0;
		if (OS.WIN32_VERSION < OS.VERSION (4, 10)) {
			RECT rect = new RECT ();
			OS.SystemParametersInfo (OS.SPI_GETWORKAREA, 0, rect, 0);
			maxWidth = (rect.right - rect.left) / 4;
		} else {
			int hmonitor = OS.MonitorFromWindow (hwnd, OS.MONITOR_DEFAULTTONEAREST);
			MONITORINFO lpmi = new MONITORINFO ();
			lpmi.cbSize = MONITORINFO.sizeof;
			OS.GetMonitorInfo (hmonitor, lpmi);
			maxWidth = (lpmi.rcWork_right - lpmi.rcWork_left) / 4;
		}
		OS.SendMessage (hwndToolTip, OS.TTM_SETMAXTIPWIDTH, 0, maxWidth);
		if (visible) {
			int nX = x, nY = y;
			if (!hasLocation) {
				POINT pt = new POINT ();
				if (OS.GetCursorPos (pt)) {
					nX = pt.x;
					nY = pt.y;
				}
			}
			int lParam = nX | (nY << 16);
			OS.SendMessage (hwndToolTip, OS.TTM_TRACKPOSITION, 0, lParam);
			
			/*
			* Feature in Windows.  Windows will not show a tool tip
			* if the cursor is outside the parent window (even on XP,
			* TTM_POPUP will not do this).  The fix is to temporarily
			* move the cursor into the tool window, show the tool tip,
			* and then restore the cursor.
			*/
			POINT pt = new POINT ();
			OS.GetCursorPos (pt);
			RECT rect = new RECT ();
			OS.GetClientRect (hwnd, rect);
			OS.MapWindowPoints (hwnd, 0, rect, 2);
			if (!OS.PtInRect (rect, pt)) {
				int hCursor = OS.GetCursor ();
				OS.SetCursor (0);
				OS.SetCursorPos (rect.left, rect.top);
				OS.SendMessage (hwndToolTip, OS.TTM_TRACKACTIVATE, 1, lpti);
				OS.SetCursorPos (pt.x, pt.y);
				OS.SetCursor (hCursor);
			} else {
				OS.SendMessage (hwndToolTip, OS.TTM_TRACKACTIVATE, 1, lpti);
			}
			//TODO - must be the current tool
			if (OS.SendMessage (hwndToolTip, OS.TTM_GETCURRENTTOOL, 0, 0) == 0) {
				OS.SendMessage (hwndToolTip, OS.TTM_TRACKACTIVATE, 0, lpti);
				OS.SendMessage (hwndToolTip, OS.TTM_SETTITLE, 0, 0);
			}
		} else {
			OS.SendMessage (hwndToolTip, OS.TTM_TRACKACTIVATE, 0, lpti);
			OS.SendMessage (hwndToolTip, OS.TTM_SETTITLE, 0, 0);
			OS.SendMessage (hwndToolTip, OS.TTM_SETMAXTIPWIDTH, 0, 0x7FFF);
		}
		return;
	}
	if (item != null && OS.SHELL32_MAJOR >= 5) {
		if (visible) {
			NOTIFYICONDATA iconData = OS.IsUnicode ? (NOTIFYICONDATA) new NOTIFYICONDATAW () : new NOTIFYICONDATAA ();
			TCHAR buffer1 = new TCHAR (0, text, true);
			TCHAR buffer2 = new TCHAR (0, message, true);
			if (OS.IsUnicode) {
				char [] szInfoTitle = ((NOTIFYICONDATAW) iconData).szInfoTitle;
				int length1 = Math.min (szInfoTitle.length - 1, buffer1.length ());
				System.arraycopy (buffer1.chars, 0, szInfoTitle, 0, length1);
				char [] szInfo = ((NOTIFYICONDATAW) iconData).szInfo;
				int length2 = Math.min (szInfo.length - 1, buffer2.length ());
				System.arraycopy (buffer2.chars, 0, szInfo, 0, length2);
			} else {
				byte [] szInfoTitle = ((NOTIFYICONDATAA) iconData).szInfoTitle;
				int length = Math.min (szInfoTitle.length - 1, buffer1.length ());
				System.arraycopy (buffer1.bytes, 0, szInfoTitle, 0, length);
				byte [] szInfo = ((NOTIFYICONDATAA) iconData).szInfo;
				int length2 = Math.min (szInfo.length - 1, buffer2.length ());
				System.arraycopy (buffer2.bytes, 0, szInfo, 0, length2);
			}
			Display display = item.getDisplay ();
			iconData.cbSize = NOTIFYICONDATA.sizeof;
			iconData.uID = item.id;
			iconData.hWnd = display.hwndMessage;
			iconData.uFlags = OS.NIF_INFO;
			if ((style & SWT.ICON_INFORMATION) != 0) iconData.dwInfoFlags = OS.NIIF_INFO;
			if ((style & SWT.ICON_WARNING) != 0) iconData.dwInfoFlags = OS.NIIF_WARNING;
			if ((style & SWT.ICON_ERROR) != 0) iconData.dwInfoFlags = OS.NIIF_ERROR;
			sendEvent (SWT.Show);
			if (OS.Shell_NotifyIcon (OS.NIM_MODIFY, iconData)) {
				this.visible = true;
			}
		} else {
			sendEvent (SWT.Hide);
			//TODO - not done, hide the tray item
		}
	}
}
}

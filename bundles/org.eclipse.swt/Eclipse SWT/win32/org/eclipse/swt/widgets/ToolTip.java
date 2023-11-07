/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent popup windows that are used
 * to inform or warn the user.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BALLOON, ICON_ERROR, ICON_INFORMATION, ICON_WARNING</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ICON_ERROR, ICON_INFORMATION,
 * and ICON_WARNING may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tooltips">Tool Tips snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.2
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolTip extends Widget {
	Shell parent;
	TrayItem item;
	String text = "", message = "";
	int id, x, y;
	boolean autoHide = true, hasLocation, visible;
	static final int TIMER_ID = 100;

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
 * @see SWT#BALLOON
 * @see SWT#ICON_ERROR
 * @see SWT#ICON_INFORMATION
 * @see SWT#ICON_WARNING
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
 * be notified when the receiver is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the receiver is selected.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the receiver is selected by the user
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

@Override
void destroyWidget () {
	if (parent != null) parent.destroyToolTip (this);
	releaseHandle ();
}

/**
 * Returns <code>true</code> if the receiver is automatically
 * hidden by the platform, and <code>false</code> otherwise.
 *
 * @return the receiver's auto hide state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getAutoHide () {
	checkWidget();
	return autoHide;
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

/**
 * Returns <code>true</code> if the receiver is visible, and
 * <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget();
	if (item != null) return visible;
	long hwndToolTip = hwndToolTip ();
	if (OS.SendMessage (hwndToolTip, OS.TTM_GETCURRENTTOOL, 0, 0) != 0) {
		TOOLINFO lpti = new TOOLINFO ();
		lpti.cbSize = TOOLINFO.sizeof;
		if (OS.SendMessage (hwndToolTip, OS.TTM_GETCURRENTTOOL, 0, lpti) != 0) {
			return (lpti.uFlags & OS.TTF_IDISHWND) == 0 && lpti.uId == id;
		}
	}
	return false;
}

int getWidth () {
	long hwnd = parent.handle;
	long hmonitor = OS.MonitorFromWindow (hwnd, OS.MONITOR_DEFAULTTONEAREST);
	MONITORINFO lpmi = new MONITORINFO ();
	lpmi.cbSize = MONITORINFO.sizeof;
	OS.GetMonitorInfo (hmonitor, lpmi);
	int maxWidth = lpmi.rcWork_right - lpmi.rcWork_left;
	return maxWidth / 4;
}

long hwndToolTip () {
	return (style & SWT.BALLOON) != 0 ? parent.balloonTipHandle () : parent.toolTipHandle ();
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

@Override
void releaseHandle () {
	super.releaseHandle ();
	parent = null;
	item = null;
	id = -1;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (item == null) {
		if (autoHide) {
			long hwndToolTip = hwndToolTip ();
			if (OS.SendMessage (hwndToolTip, OS.TTM_GETCURRENTTOOL, 0, 0) != 0) {
				TOOLINFO lpti = new TOOLINFO ();
				lpti.cbSize = TOOLINFO.sizeof;
				if (OS.SendMessage (hwndToolTip, OS.TTM_GETCURRENTTOOL, 0, lpti) != 0) {
					if ((lpti.uFlags & OS.TTF_IDISHWND) == 0) {
						if (lpti.uId == id) {
							OS.SendMessage (hwndToolTip, OS.TTM_TRACKACTIVATE, 0, lpti);
							OS.SendMessage (hwndToolTip, OS.TTM_POP, 0, 0);
							OS.KillTimer (hwndToolTip, TIMER_ID);
						}
					}
				}
			}
		}
	}
	if (item != null && item.toolTip == this) {
		item.toolTip = null;
	}
	item = null;
	text = message = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver is selected by the user.
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
 * Makes the receiver hide automatically when <code>true</code>,
 * and remain visible when <code>false</code>.
 *
 * @param autoHide the auto hide state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getVisible
 * @see #setVisible
 */
public void setAutoHide (boolean autoHide) {
	checkWidget ();
	this.autoHide = autoHide;
	//TODO - update when visible
}

/**
 * Sets the location of the receiver, which must be a tooltip,
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
	setLocationInPixels(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y));
}

void setLocationInPixels (int x, int y) {
	this.x = x;
	this.y = y;
	hasLocation = true;
	//TODO - update when visible
}

/**
 * Sets the location of the receiver, which must be a tooltip,
 * to the point specified by the argument which is relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Note that the platform window manager ultimately has control
 * over the location of tooltips.
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
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	location = DPIUtil.autoScaleUp(location);
	setLocationInPixels(location.x, location.y);
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

	if (getVisible ()) {
		OS.SendMessage (hwndToolTip (), OS.TTM_UPDATE, 0, 0);
	}
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
	//TODO - support text direction (?)
}

/**
 * Marks the receiver as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
	checkWidget ();
	if (visible == getVisible ()) return;
	if (item == null) {
		long hwnd = parent.handle;
		TOOLINFO lpti = new TOOLINFO ();
		lpti.cbSize = TOOLINFO.sizeof;
		lpti.uId = id;
		lpti.hwnd = hwnd;
		long hwndToolTip = hwndToolTip ();
		Shell shell = parent.getShell ();
		if (text.length () != 0) {
			int icon = OS.TTI_NONE;
			if ((style & SWT.ICON_INFORMATION) != 0) icon = OS.TTI_INFO;
			if ((style & SWT.ICON_WARNING) != 0) icon = OS.TTI_WARNING;
			if ((style & SWT.ICON_ERROR) != 0) icon = OS.TTI_ERROR;
			shell.setToolTipTitle (hwndToolTip, text, icon);
		} else {
			shell.setToolTipTitle (hwndToolTip, null, 0);
		}
		OS.SendMessage (hwndToolTip, OS.TTM_SETMAXTIPWIDTH, 0, getWidth ());
		if (visible) {
			int nX = x, nY = y;
			if (!hasLocation) {
				POINT pt = new POINT ();
				if (OS.GetCursorPos (pt)) {
					nX = pt.x;
					nY = pt.y;
				}
			}
			long lParam = OS.MAKELPARAM (nX, nY);
			OS.SendMessage (hwndToolTip, OS.TTM_TRACKPOSITION, 0, lParam);

			// Feature in Windows. If another tooltip is already active, sending
			// `TTM_TRACKACTIVATE` is ignored and OLD tooltip remains active.
			// The workaround is to explicitly deactivate previous tooltip.
			OS.SendMessage (hwndToolTip, OS.TTM_TRACKACTIVATE, 0, 0);

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
				long hCursor = OS.GetCursor ();
				OS.SetCursor (0);
				OS.SetCursorPos (rect.left, rect.top);
				OS.SendMessage (hwndToolTip, OS.TTM_TRACKACTIVATE, 1, lpti);
				OS.SetCursorPos (pt.x, pt.y);
				OS.SetCursor (hCursor);
			} else {
				OS.SendMessage (hwndToolTip, OS.TTM_TRACKACTIVATE, 1, lpti);
			}

			int time = (int)OS.SendMessage (hwndToolTip, OS.TTM_GETDELAYTIME, OS.TTDT_AUTOPOP, 0);
			OS.SetTimer (hwndToolTip, TIMER_ID, time, 0);
		} else {
			OS.SendMessage (hwndToolTip, OS.TTM_TRACKACTIVATE, 0, lpti);
			OS.SendMessage (hwndToolTip, OS.TTM_POP, 0, 0);
			OS.KillTimer (hwndToolTip, TIMER_ID);
		}
		return;
	}
	if (item != null) {
		if (visible) {
			NOTIFYICONDATA iconData = new NOTIFYICONDATA ();
			char [] szInfoTitle = iconData.szInfoTitle;
			int length1 = Math.min (szInfoTitle.length - 1, text.length ());
			text.getChars (0, length1, szInfoTitle, 0);
			char [] szInfo = iconData.szInfo;
			int length2 = Math.min (szInfo.length - 1, message.length ());
			message.getChars (0, length2, szInfo, 0);
			Display display = item.getDisplay ();
			iconData.cbSize = NOTIFYICONDATA.sizeof;
			iconData.uID = item.id;
			iconData.hWnd = display.hwndMessage;
			iconData.uFlags = OS.NIF_INFO;
			if ((style & SWT.ICON_INFORMATION) != 0) iconData.dwInfoFlags = OS.NIIF_INFO;
			if ((style & SWT.ICON_WARNING) != 0) iconData.dwInfoFlags = OS.NIIF_WARNING;
			if ((style & SWT.ICON_ERROR) != 0) iconData.dwInfoFlags = OS.NIIF_ERROR;
			sendEvent (SWT.Show);
			this.visible = OS.Shell_NotifyIcon (OS.NIM_MODIFY, iconData);
		} else {
			//TODO - hide the tray item
		}
	}
}
}

package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent icons that can be placed on the
 * platform's system tray or task bar status area.
 * 
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class TrayIcon extends Widget {
	int id;
	boolean isVisible = false;
	Image iconImage;
	Image image;
	Menu menu;
	String toolTipText;

/**
 * Constructs a new instance of this class given its Display.
 *
 * @param display the parent display
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the display is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public TrayIcon (Display display) {
	if (display == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.display = display;
	display.addTrayIcon (this);
	createWidget ();
}
	
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the receiver is selected
 * <code>widgetDefaultSelected</code> is called when the receiver is double-clicked
 * </p>
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

void createWidget () {
	NOTIFYICONDATA iconData = OS.IsUnicode ? (NOTIFYICONDATA)
		new NOTIFYICONDATAW () :
		new NOTIFYICONDATAA ();
	iconData.cbSize = NOTIFYICONDATA.sizeof;
	iconData.uID = id;
	iconData.hWnd = display.hwndMessage;
	iconData.uFlags = OS.NIF_MESSAGE;
	iconData.uCallbackMessage = Display.SWT_TRAYICONMSG;
	OS.Shell_NotifyIcon (OS.NIM_ADD, iconData);
}

void destroyWidget () {
	NOTIFYICONDATA iconData = OS.IsUnicode ? (NOTIFYICONDATA)
		new NOTIFYICONDATAW () :
		new NOTIFYICONDATAA ();
	iconData.cbSize = NOTIFYICONDATA.sizeof;
	iconData.uID = id;
	iconData.hWnd = display.hwndMessage;
	OS.Shell_NotifyIcon (OS.NIM_DELETE, iconData);
	display.removeTrayIcon (this);
	super.destroyWidget ();
}

/**
 * Returns the receiver's image, or null if the image has not been set.
 *
 * @return the receiver's image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage () {
	checkWidget ();
	return image;
}

/**
 * Returns the receiver's context menu, or null if the menu has not been set.
 *
 * @return the receiver's context menu
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getMenu () {
	checkWidget ();
	return menu;
}

/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText (String value) {
	checkWidget ();
	return toolTipText;
}

/**
 * Returns <code>true</code> if the receiver is visible and 
 * <code>false</code> otherwise.
 *
 * @return the receiver's visibility
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isVisible () {
	checkWidget ();
	return isVisible;
}

void releaseWidget () {
	super.releaseWidget ();
	if (iconImage != null) {
		iconImage.dispose ();
		iconImage = null;
	}
	image = null;
	toolTipText = null;
	menu = null;
}
	
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver is selected.
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Sets the receiver's image.
 *
 * @param image the new image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.image = image;
	if (iconImage != null) iconImage.dispose ();
	iconImage = null;
	int hIcon = 0;
	Image icon = image;
	if (icon != null) {
		switch (icon.type) {
			case SWT.BITMAP:
				ImageData data = icon.getImageData ();
				ImageData mask = data.getTransparencyMask ();
				iconImage = new Image (display, data, mask);
				hIcon = iconImage.handle;
				break;
			case SWT.ICON:
				hIcon = icon.handle;
				break;
		}
	}
	NOTIFYICONDATA iconData = OS.IsUnicode ? (NOTIFYICONDATA)
		new NOTIFYICONDATAW () :
		new NOTIFYICONDATAA ();
	iconData.cbSize = NOTIFYICONDATA.sizeof;
	iconData.uID = id;
	iconData.hIcon = hIcon;
	iconData.hWnd = display.hwndMessage;
	iconData.uFlags = OS.NIF_ICON;
	OS.Shell_NotifyIcon (OS.NIM_MODIFY, iconData);
}

/**
 * Sets the receiver's context menu.  This argument may be null to indicate
 * that the receiver should have no context menu.
 *
 * @param menu the new menu, or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMenu (Menu menu) {
	checkWidget ();
	if (menu != null && menu.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.menu = menu;
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String value) {
	checkWidget ();
	toolTipText = value;
	NOTIFYICONDATA iconData = OS.IsUnicode ? (NOTIFYICONDATA)
		new NOTIFYICONDATAW () :
		new NOTIFYICONDATAA ();
	TCHAR buffer = new TCHAR (0, toolTipText == null ? "" : toolTipText, true);
	if (OS.IsUnicode) {
		int length = Math.min (((NOTIFYICONDATAW) iconData).szTip.length - 1, buffer.length ());
		char [] szTip = ((NOTIFYICONDATAW) iconData).szTip;
		for (int i = length; i < szTip.length; i++) szTip [i] = 0;
		System.arraycopy (buffer.toString ().toCharArray (), 0, szTip, 0, length);
	} else {
		int length = Math.min (((NOTIFYICONDATAA) iconData).szTip.length - 1, buffer.length ());
		byte [] szTip = ((NOTIFYICONDATAA) iconData).szTip;
		for (int i = length; i < szTip.length; i++) szTip [i] = 0;
		System.arraycopy (buffer.toString ().getBytes (), 0, szTip, 0, length);
	}
	iconData.cbSize = NOTIFYICONDATA.sizeof;
	iconData.uID = id;
	iconData.hWnd = display.hwndMessage;
	iconData.uFlags = OS.NIF_TIP;
	OS.Shell_NotifyIcon (OS.NIM_MODIFY, iconData);
}

/**
 * Makes the receiver visible if the argument is <code>true</code>,
 * and makes it invisible otherwise. 
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
	if (isVisible == visible) return;
	isVisible = visible;
	if (visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
	}
	NOTIFYICONDATA iconData = OS.IsUnicode ? (NOTIFYICONDATA)
		new NOTIFYICONDATAW () :
		new NOTIFYICONDATAA ();
	iconData.cbSize = NOTIFYICONDATA.sizeof;
	iconData.uID = id;
	iconData.hWnd = display.hwndMessage;
	iconData.uFlags = OS.NIF_STATE;
	iconData.dwState = visible ? 0 : OS.NIS_HIDDEN;
	iconData.dwStateMask = OS.NIS_HIDDEN;
	OS.Shell_NotifyIcon (OS.NIM_MODIFY, iconData);
	if (!visible) {
		sendEvent (SWT.Hide);
	}
}

LRESULT WM_LBUTTONDBLCLK (int wParam, int lParam) {
	Event event = new Event ();
	POINT pos = new POINT ();
	OS.GetCursorPos (pos);
	event.x = pos.x;
	event.y = pos.y;
	sendEvent (SWT.DefaultSelection, event);
	return null;
}
	
LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
	Event event = new Event ();
	POINT pos = new POINT ();
	OS.GetCursorPos (pos);
	event.x = pos.x;
	event.y = pos.y;
	sendEvent (SWT.Selection, event);
	return null;
}

LRESULT WM_RBUTTONDBLCLK (int wParam, int lParam) {
	Event event = new Event ();
	POINT pos = new POINT ();
	OS.GetCursorPos (pos);
	event.x = pos.x;
	event.y = pos.y;
	sendEvent (SWT.DefaultSelection, event);
	return null;
}
	
LRESULT WM_RBUTTONDOWN (int wParam, int lParam) {
	if (menu == null) return null;
	POINT pos = new POINT ();
	OS.GetCursorPos (pos);
	menu.setLocation (pos.x, pos.y);
	Event event = new Event ();
	event.x = pos.x;
	event.y = pos.y;
	sendEvent (SWT.MenuDetect, event);
	menu.setVisible(true);
	return null;
}
}

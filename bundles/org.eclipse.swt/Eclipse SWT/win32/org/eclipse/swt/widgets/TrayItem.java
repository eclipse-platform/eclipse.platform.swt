/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent icons that can be placed on the
 * system tray or task bar status area.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, MenuDetect, Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tray">Tray, TrayItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.0
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TrayItem extends Item {
	Tray parent;
	int id;
	Image image2, highlightImage;
	ToolTip toolTip;
	String toolTipText;
	boolean visible = true;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tray</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TrayItem (Tray parent, int style) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
	createUpdateWidget (true);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the receiver is selected
 * <code>widgetDefaultSelected</code> is called when the receiver is double-clicked
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the platform-specific context menu trigger
 * has occurred, by sending it one of the messages defined in
 * the <code>MenuDetectListener</code> interface.
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
 * @see MenuDetectListener
 * @see #removeMenuDetectListener
 *
 * @since 3.3
 */
public void addMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MenuDetect, typedListener);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void createUpdateWidget (boolean newIcon) {
	NOTIFYICONDATA iconData = new NOTIFYICONDATA ();
	iconData.cbSize = NOTIFYICONDATA.sizeof;
	/*
	 * As per MSDN article iconData.uID is unique for every TrayItem
	 * instance(https://msdn.microsoft.com/en-us/library/windows/desktop/
	 * bb762159%28v=vs.85%29.aspx) and this uID value should be specified
	 * during TrayItem instance creation & should be cached and used in
	 * subsequent calls to Shell_NotifyIcon to perform later actions on the
	 * TrayItem instance refer Win10 bug 488739.
	 */
	iconData.uID = id = (newIcon ? display.nextTrayId++ : id);
	iconData.hWnd = display.hwndMessage;
	iconData.uFlags = OS.NIF_MESSAGE;
	iconData.uCallbackMessage = Display.SWT_TRAYICONMSG;
	/*
	 * OS.NIM_ADD message should be called only once in a TrayItem instance
	 * life-cycle i.e. in the TrayItem instance creation step only, else
	 * will lead to multiple TrayIcons entries on Win10 refer bug 488739.
	 */
	OS.Shell_NotifyIcon ((newIcon ? OS.NIM_ADD : OS.NIM_MODIFY), iconData);
}

@Override
void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns the receiver's highlight image if it has one, or null
 * if it does not.
 *
 * @return the receiver's highlight image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
public Image getHighlightImage () {
	checkWidget ();
	return highlightImage;
}

/**
 * Returns the receiver's parent, which must be a <code>Tray</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public Tray getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns the receiver's tool tip, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public ToolTip getToolTip () {
	checkWidget ();
	return toolTip;
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
public String getToolTipText () {
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
public boolean getVisible () {
	checkWidget ();
	return visible;
}

long messageProc (long hwnd, int msg, long wParam, long lParam) {
	/*
	* Feature in Windows.  When the user clicks on the tray
	* icon, another application may be the foreground window.
	* This means that the event loop is not running and can
	* cause problems.  For example, if a menu is shown, when
	* the user clicks outside of the menu to cancel it, the
	* menu is not hidden until an event is processed.  If
	* another application is the foreground window, then the
	* menu is not hidden.  The fix is to force the tray icon
	* message window to the foreground when sending an event.
	*/
	switch ((int)lParam) {
		case OS.WM_LBUTTONDOWN:
			if (hooks (SWT.Selection)) {
				OS.SetForegroundWindow (hwnd);
				sendSelectionEvent (SWT.Selection);
			}
			break;
		case OS.WM_LBUTTONDBLCLK:
		case OS.WM_RBUTTONDBLCLK:
			if (hooks (SWT.DefaultSelection)) {
				OS.SetForegroundWindow (hwnd);
				sendSelectionEvent (SWT.DefaultSelection);
			}
			break;
		case OS.WM_RBUTTONUP: {
			if (hooks (SWT.MenuDetect)) {
				OS.SetForegroundWindow (hwnd);
				sendEvent (SWT.MenuDetect);
				// widget could be disposed at this point
				if (isDisposed()) return 0;
			}
			break;
		}
		case OS.NIN_BALLOONSHOW:
			if (toolTip != null && !toolTip.visible) {
				toolTip.visible = true;
				if (toolTip.hooks (SWT.Show)) {
					OS.SetForegroundWindow (hwnd);
					toolTip.sendEvent (SWT.Show);
					// widget could be disposed at this point
					if (isDisposed()) return 0;
				}
			}
			break;
		case OS.NIN_BALLOONHIDE:
		case OS.NIN_BALLOONTIMEOUT:
		case OS.NIN_BALLOONUSERCLICK:
			if (toolTip != null) {
				if (toolTip.visible) {
					toolTip.visible = false;
					if (toolTip.hooks (SWT.Hide)) {
						OS.SetForegroundWindow (hwnd);
						toolTip.sendEvent (SWT.Hide);
						// widget could be disposed at this point
						if (isDisposed()) return 0;
					}
				}
				if (lParam == OS.NIN_BALLOONUSERCLICK) {
					if (toolTip.hooks (SWT.Selection)) {
						OS.SetForegroundWindow (hwnd);
						toolTip.sendSelectionEvent (SWT.Selection);
						// widget could be disposed at this point
						if (isDisposed()) return 0;
					}
				}
			}
			break;
	}
	display.wakeThread ();
	return 0;
}

void recreate () {
	createUpdateWidget (false);
	if (!visible) setVisible (false);
	if (text.length () != 0) setText (text);
	if (image != null) setImage (image);
	if (toolTipText != null) setToolTipText (toolTipText);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	parent = null;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (toolTip != null) toolTip.item = null;
	toolTip = null;
	if (image2 != null) image2.dispose ();
	image2 = null;
	highlightImage = null;
	toolTipText = null;
	NOTIFYICONDATA iconData = new NOTIFYICONDATA ();
	iconData.cbSize = NOTIFYICONDATA.sizeof;
	iconData.uID = id;
	iconData.hWnd = display.hwndMessage;
	OS.Shell_NotifyIcon (OS.NIM_DELETE, iconData);
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the platform-specific context menu trigger has
 * occurred.
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
 * @see MenuDetectListener
 * @see #addMenuDetectListener
 *
 * @since 3.3
 */
public void removeMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MenuDetect, listener);
}

/**
 * Sets the receiver's highlight image.
 *
 * @param image the new highlight image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
public void setHighlightImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	highlightImage = image;
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
@Override
public void setImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	super.setImage (image);
	if (image2 != null) image2.dispose ();
	image2 = null;
	long hIcon = 0;
	Image icon = image;
	if (icon != null) {
		switch (icon.type) {
			case SWT.BITMAP:
				image2 = Display.createIcon (image);
				hIcon = image2.handle;
				break;
			case SWT.ICON:
				hIcon = icon.handle;
				break;
		}
	}
	NOTIFYICONDATA iconData = new NOTIFYICONDATA ();
	iconData.cbSize = NOTIFYICONDATA.sizeof;
	iconData.uID = id;
	iconData.hWnd = display.hwndMessage;
	iconData.hIcon = hIcon;
	iconData.uFlags = OS.NIF_ICON;
	OS.Shell_NotifyIcon (OS.NIM_MODIFY, iconData);
}

/**
 * Sets the receiver's tool tip to the argument, which
 * may be null indicating that no tool tip should be shown.
 *
 * @param toolTip the new tool tip (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setToolTip (ToolTip toolTip) {
	checkWidget ();
	ToolTip oldTip = this.toolTip, newTip = toolTip;
	if (oldTip != null) oldTip.item = null;
	this.toolTip = newTip;
	if (newTip != null) newTip.item = this;
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be
 * escaped by doubling it in the string.
 * </p>
 * <p>
 * NOTE: This operation is a hint and behavior is platform specific, on Windows
 * for CJK-style mnemonics of the form " (&amp;C)" at the end of the tooltip text
 * are not shown in tooltip.
 * </p>
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget ();
	toolTipText = string;
	NOTIFYICONDATA iconData = new NOTIFYICONDATA ();
	if (string != null) {
		char [] szTip = iconData.szTip;
		int length = Math.min (szTip.length - 1, string.length ());
		string.getChars (0, length, szTip, 0);
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
	if (this.visible == visible) return;
	if (visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
	}
	this.visible = visible;
	NOTIFYICONDATA iconData = new NOTIFYICONDATA ();
	iconData.cbSize = NOTIFYICONDATA.sizeof;
	iconData.uID = id;
	iconData.hWnd = display.hwndMessage;
	iconData.uFlags = OS.NIF_STATE;
	iconData.dwState = visible ? 0 : OS.NIS_HIDDEN;
	iconData.dwStateMask = OS.NIS_HIDDEN;
	OS.Shell_NotifyIcon (OS.NIM_MODIFY, iconData);
	if (!visible) sendEvent (SWT.Hide);
}

}

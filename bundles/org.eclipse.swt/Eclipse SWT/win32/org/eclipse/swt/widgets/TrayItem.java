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


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent icons that can be placed on the
 * system tray or task bar status area.
 *
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
 * @since 3.0
 */
public class TrayItem extends Item {
	Tray parent;
	int id;
	Image image2;
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

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

Image createIcon (Image image) {
	ImageData data = image.getImageData ();
	if (data.alpha == -1 && data.alphaData == null) {
		ImageData mask = data.getTransparencyMask ();
		return new Image (display, data, mask);
	}
	int width = data.width, height = data.height;
	int hMask, hBitmap;
	int hDC = OS.GetDC (0);
	int dstHdc = OS.CreateCompatibleDC (hDC), oldDstBitmap;
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
		hBitmap = Display.create32bitDIB (image.handle, data.alpha, data.alphaData, data.transparentPixel);
		hMask = OS.CreateBitmap (width, height, 1, 1, null);
		oldDstBitmap = OS.SelectObject (dstHdc, hMask);
		OS.PatBlt (dstHdc, 0, 0, width, height, OS.BLACKNESS);
	} else {
		hMask = Display.createMaskFromAlpha (data, width, height);
		/* Icons need black pixels where the mask is transparent */
		hBitmap = OS.CreateCompatibleBitmap (hDC, width, height);
		oldDstBitmap = OS.SelectObject (dstHdc, hBitmap);
		int srcHdc = OS.CreateCompatibleDC (hDC);
		int oldSrcBitmap = OS.SelectObject (srcHdc, image.handle);
		OS.PatBlt (dstHdc, 0, 0, width, height, OS.BLACKNESS);
		OS.BitBlt (dstHdc, 0, 0, width, height, srcHdc, 0, 0, OS.SRCINVERT);
		OS.SelectObject (srcHdc, hMask);
		OS.BitBlt (dstHdc, 0, 0, width, height, srcHdc, 0, 0, OS.SRCAND);
		OS.SelectObject (srcHdc, image.handle);
		OS.BitBlt (dstHdc, 0, 0, width, height, srcHdc, 0, 0, OS.SRCINVERT);
		OS.SelectObject (srcHdc, oldSrcBitmap);
		OS.DeleteDC (srcHdc);
	}
	OS.SelectObject (dstHdc, oldDstBitmap);
	OS.DeleteDC (dstHdc);
	OS.ReleaseDC (0, hDC);
	ICONINFO info = new ICONINFO ();
	info.fIcon = true;
	info.hbmColor = hBitmap;
	info.hbmMask = hMask;
	int hIcon = OS.CreateIconIndirect (info);
	if (hIcon == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.DeleteObject (hBitmap);
	OS.DeleteObject (hMask);
	return Image.win32_new (display, SWT.ICON, hIcon);
}

void createWidget () {
	if (OS.IsWinCE) return;
	NOTIFYICONDATA iconData = OS.IsUnicode ? (NOTIFYICONDATA) new NOTIFYICONDATAW () : new NOTIFYICONDATAA ();
	iconData.cbSize = NOTIFYICONDATA.sizeof;
	iconData.uID = id = display.nextTrayId++;
	iconData.hWnd = display.hwndMessage;
	iconData.uFlags = OS.NIF_MESSAGE;
	iconData.uCallbackMessage = Display.SWT_TRAYICONMSG;
	OS.Shell_NotifyIcon (OS.NIM_ADD, iconData);
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

int messageProc (int hwnd, int msg, int wParam, int lParam) {
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
	switch (lParam) {
		case OS.WM_LBUTTONDOWN:
			if (hooks (SWT.Selection)) {
				OS.SetForegroundWindow (hwnd);
				postEvent (SWT.Selection);
			}
			break;
		case OS.WM_LBUTTONDBLCLK:
		case OS.WM_RBUTTONDBLCLK:
			if (hooks (SWT.DefaultSelection)) {
				OS.SetForegroundWindow (hwnd);
				postEvent (SWT.DefaultSelection);
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
	}
	display.wakeThread ();
	return 0;
}

void recreate () {
	createWidget ();
	if (!visible) setVisible (false);
	if (text.length () != 0) setText (text);
	if (image != null) setImage (image);
	if (toolTipText != null) setToolTipText (toolTipText);
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseWidget () {
	super.releaseWidget ();
	if (image2 != null) image2.dispose ();
	image2 = null;
	toolTipText = null;
	if (OS.IsWinCE) return;
	NOTIFYICONDATA iconData = OS.IsUnicode ? (NOTIFYICONDATA) new NOTIFYICONDATAW () : new NOTIFYICONDATAA ();
	iconData.cbSize = NOTIFYICONDATA.sizeof;
	iconData.uID = id;
	iconData.hWnd = display.hwndMessage;
	OS.Shell_NotifyIcon (OS.NIM_DELETE, iconData);
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
	super.setImage (image);
	if (OS.IsWinCE) return;
	if (image2 != null) image2.dispose ();
	image2 = null;
	int hIcon = 0;
	Image icon = image;
	if (icon != null) {
		switch (icon.type) {
			case SWT.BITMAP:
				image2 = createIcon (image);
				hIcon = image2.handle;
				break;
			case SWT.ICON:
				hIcon = icon.handle;
				break;
		}
	}
	NOTIFYICONDATA iconData = OS.IsUnicode ? (NOTIFYICONDATA) new NOTIFYICONDATAW () : new NOTIFYICONDATAA ();
	iconData.cbSize = NOTIFYICONDATA.sizeof;
	iconData.uID = id;
	iconData.hWnd = display.hwndMessage;
	iconData.hIcon = hIcon;
	iconData.uFlags = OS.NIF_ICON;
	OS.Shell_NotifyIcon (OS.NIM_MODIFY, iconData);
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
 *
 * @param value the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String value) {
	checkWidget ();
	toolTipText = value;
	if (OS.IsWinCE) return;
	NOTIFYICONDATA iconData = OS.IsUnicode ? (NOTIFYICONDATA) new NOTIFYICONDATAW () : new NOTIFYICONDATAA ();
	TCHAR buffer = new TCHAR (0, toolTipText == null ? "" : toolTipText, true);
	/*
	* Note that the size of the szTip field is different
	* in version 5.0 of shell32.dll.
	*/
	int length = OS.SHELL32_MAJOR < 5 ? 64 : 128;
	if (OS.IsUnicode) {
		char [] szTip = ((NOTIFYICONDATAW) iconData).szTip;
		length = Math.min (length - 1, buffer.length ());
		System.arraycopy (buffer.chars, 0, szTip, 0, length);
	} else {
		byte [] szTip = ((NOTIFYICONDATAA) iconData).szTip;
		length = Math.min (length - 1, buffer.length ());
		System.arraycopy (buffer.bytes, 0, szTip, 0, length);
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
	if (!OS.IsWinCE) {
		NOTIFYICONDATA iconData = OS.IsUnicode ? (NOTIFYICONDATA) new NOTIFYICONDATAW () : new NOTIFYICONDATAA ();
		iconData.cbSize = NOTIFYICONDATA.sizeof;
		iconData.uID = id;
		iconData.hWnd = display.hwndMessage;
		if (OS.SHELL32_MAJOR < 5) {
			if (visible) {
				iconData.uFlags = OS.NIF_MESSAGE;
				iconData.uCallbackMessage = Display.SWT_TRAYICONMSG;
				OS.Shell_NotifyIcon (OS.NIM_ADD, iconData);
				setImage (image);
				setToolTipText (toolTipText);
			} else {
				OS.Shell_NotifyIcon (OS.NIM_DELETE, iconData);
			}
		} else {
			iconData.uFlags = OS.NIF_STATE;
			iconData.dwState = visible ? 0 : OS.NIS_HIDDEN;
			iconData.dwStateMask = OS.NIS_HIDDEN;
			OS.Shell_NotifyIcon (OS.NIM_MODIFY, iconData);
		}
	}
	if (!visible) sendEvent (SWT.Hide);
}

}

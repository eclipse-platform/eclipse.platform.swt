/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide an etched border
 * with an optional title.
 * <p>
 * Shadow styles are hints and may not be honoured
 * by the platform.  To create a group with the
 * default shadow style for the platform, do not
 * specify a shadow style.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SHADOW_ETCHED_IN, SHADOW_ETCHED_OUT, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the above styles may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Group extends Composite {
	String text = "";
	static final int CLIENT_INSET = 3;
	static final int /*long*/ GroupProc;
	static final TCHAR GroupClass = new TCHAR (0, OS.IsWinCE ? "BUTTON" : "SWT_GROUP", true);
	static {
		/*
		* Feature in Windows.  The group box window class
		* uses the CS_HREDRAW and CS_VREDRAW style bits to
		* force a full redraw of the control and all children
		* when resized.  This causes flashing.  The fix is to
		* register a new window class without these bits and
		* implement special code that damages only the control.
		* 
		* Feature in WinCE.  On certain devices, defining
		* a new window class which looks like BUTTON causes
		* CreateWindowEx() to crash.  The workaround is to use
		* the class Button directly.
		*/
		WNDCLASS lpWndClass = new WNDCLASS ();
		if (OS.IsWinCE) {
			OS.GetClassInfo (0, GroupClass, lpWndClass);
			GroupProc = lpWndClass.lpfnWndProc;
		} else {
			TCHAR WC_BUTTON = new TCHAR (0, "BUTTON", true);
			OS.GetClassInfo (0, WC_BUTTON, lpWndClass);
			GroupProc = lpWndClass.lpfnWndProc;
			int /*long*/ hInstance = OS.GetModuleHandle (null);
			if (!OS.GetClassInfo (hInstance, GroupClass, lpWndClass)) {
				int /*long*/ hHeap = OS.GetProcessHeap ();
				lpWndClass.hInstance = hInstance;
				lpWndClass.style &= ~(OS.CS_HREDRAW | OS.CS_VREDRAW);
				int byteCount = GroupClass.length () * TCHAR.sizeof;
				int /*long*/ lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
				OS.MoveMemory (lpszClassName, GroupClass, byteCount);
				lpWndClass.lpszClassName = lpszClassName;
				OS.RegisterClass (lpWndClass);
				OS.HeapFree (hHeap, 0, lpszClassName);
			}
		}
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
 * @see SWT#SHADOW_ETCHED_IN
 * @see SWT#SHADOW_ETCHED_OUT
 * @see SWT#SHADOW_IN
 * @see SWT#SHADOW_OUT
 * @see SWT#SHADOW_NONE
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Group (Composite parent, int style) {
	super (parent, checkStyle (style));
}

int /*long*/ callWindowProc (int /*long*/ hwnd, int msg, int /*long*/ wParam, int /*long*/ lParam) {
	if (handle == 0) return 0;
	/*
	* Feature in Windows.  When the user clicks on the group
	* box label, the group box takes focus.  This is unwanted.
	* The fix is to avoid calling the group box window proc.
	*/
	switch (msg) {
		case OS.WM_LBUTTONDOWN:
		case OS.WM_LBUTTONDBLCLK: 
			return OS.DefWindowProc (hwnd, msg, wParam, lParam);
	}
	return OS.CallWindowProc (GroupProc, hwnd, msg, wParam, lParam);
}

static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	Point size = super.computeSize (wHint, hHint, changed);
	int length = text.length ();
	if (length != 0) {
		/*
		* Bug in Windows.  When a group control is right-to-left and
		* is disabled, the first pixel of the text is clipped.  The
		* fix is to add a space to both sides of the text.  Note that
		* the work around must run all the time to stop the preferred
		* size from changing when a group is enabled and disabled.
		*/
		String string = text;
		if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			if (OS.COMCTL32_MAJOR < 6 || !OS.IsAppThemed ()) {
				string = " " + string + " ";
			}
		}
		/*
		* If the group has text, and the text is wider than the
		* client area, pad the width so the text is not clipped.
		*/
		TCHAR buffer = new TCHAR (getCodePage (), string, true);
		int /*long*/ newFont, oldFont = 0;
		int /*long*/ hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		RECT rect = new RECT ();
		int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE;
		OS.DrawText (hDC, buffer, -1, rect, flags);
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
		int offsetY = OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed () ? 0 : 1;
		size.x = Math.max (size.x, rect.right - rect.left + CLIENT_INSET * 6 + offsetY);
	}
	return size;
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	Rectangle trim = super.computeTrim (x, y, width, height);
	int /*long*/ newFont, oldFont = 0;
	int /*long*/ hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	TEXTMETRIC tm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
	OS.GetTextMetrics (hDC, tm);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	int offsetY = OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed () ? 0 : 1;
	trim.x -= CLIENT_INSET;
	trim.y -= tm.tmHeight + offsetY;
	trim.width += CLIENT_INSET * 2;
	trim.height += tm.tmHeight + CLIENT_INSET + offsetY;
	return trim;
}

void createHandle () {
	/*
	* Feature in Windows.  When a button is created,
	* it clears the UI state for all controls in the
	* shell by sending WM_CHANGEUISTATE with UIS_SET,
	* UISF_HIDEACCEL and UISF_HIDEFOCUS to the parent.
	* This is undocumented and unexpected.  The fix
	* is to ignore the WM_CHANGEUISTATE, when sent
	* from CreateWindowEx().
	*/
	parent.state |= IGNORE_WM_CHANGEUISTATE;
	super.createHandle ();
	parent.state &= ~IGNORE_WM_CHANGEUISTATE;
	state |= DRAW_BACKGROUND;
	state &= ~CANVAS;
}

void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	/*
	* Bug in Windows.  When a group control is right-to-left and
	* is disabled, the first pixel of the text is clipped.  The
	* fix is to add a space to both sides of the text.
	*/
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		if (OS.COMCTL32_MAJOR < 6 || !OS.IsAppThemed ()) {
			String string = enabled || text.length() == 0 ? text : " " + text + " ";
			TCHAR buffer = new TCHAR (getCodePage (), string, true);
			OS.SetWindowText (handle, buffer);
		}
	}
}

public Rectangle getClientArea () {
	checkWidget ();
	forceResize ();
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	int /*long*/ newFont, oldFont = 0;
	int /*long*/ hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	TEXTMETRIC tm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
	OS.GetTextMetrics (hDC, tm);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	int offsetY = OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed () ? 0 : 1;
	int x = CLIENT_INSET, y = tm.tmHeight + offsetY;
	int width = Math.max (0, rect.right - CLIENT_INSET * 2);
	int height = Math.max (0, rect.bottom - y - CLIENT_INSET);
	return new Rectangle (x, y, width, height);
}

String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's text, which is the string that the
 * is used as the <em>title</em>. If the text has not previously
 * been set, returns an empty string.
 *
 * @return the text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	return text;
}

boolean mnemonicHit (char key) {
	return setFocus ();
}

boolean mnemonicMatch (char key) {
	char mnemonic = findMnemonic (getText ());
	if (mnemonic == '\0') return false;
	return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
}

void printWidget (int /*long*/ hwnd, int /*long*/ hdc, GC gc) {
	/*
	* Bug in Windows.  For some reason, PrintWindow()
	* returns success but does nothing when it is called
	* on a printer.  The fix is to just go directly to
	* WM_PRINT in this case.
	*/
	boolean success = false;
	if (!(OS.GetDeviceCaps(gc.handle, OS.TECHNOLOGY) == OS.DT_RASPRINTER)) {
		int bits = OS.GetWindowLong (hwnd, OS.GWL_STYLE);
		if ((bits & OS.WS_VISIBLE) == 0) {
			OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 1, 0);
		}
		success = OS.PrintWindow (hwnd, hdc, 0);
		if ((bits & OS.WS_VISIBLE) == 0) {
			OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 0, 0);
		}
	}
	
	/*
	* Bug in Windows.  For some reason, PrintWindow() fails
	* when it is called on a push button.  The fix is to
	* detect the failure and use WM_PRINT instead.  Note
	* that WM_PRINT cannot be used all the time because it
	* fails for browser controls when the browser has focus.
	*/
	if (!success) {
		/*
		* Bug in Windows.  For some reason, WM_PRINT when called
		* with PRF_CHILDREN will not draw the tool bar divider
		* for tool bar children that do not have CCS_NODIVIDER.
		* The fix is to draw the group box and iterate through
		* the children, drawing each one.
		*/
		int flags = OS.PRF_CLIENT | OS.PRF_NONCLIENT | OS.PRF_ERASEBKGND;
		OS.SendMessage (hwnd, OS.WM_PRINT, hdc, flags);
		int nSavedDC = OS.SaveDC (hdc);
		Control [] children = _getChildren ();
		Rectangle rect = getBounds ();
		OS.IntersectClipRect (hdc, 0, 0, rect.width, rect.height);
		for (int i=children.length - 1; i>=0; --i) {
			Point location = children [i].getLocation ();
			int graphicsMode = OS.GetGraphicsMode(hdc);
			if (graphicsMode == OS.GM_ADVANCED) {
				float [] lpXform = {1, 0, 0, 1, location.x, location.y};
				OS.ModifyWorldTransform(hdc, lpXform, OS.MWT_LEFTMULTIPLY);
			} else {
				OS.SetWindowOrgEx (hdc, -location.x, -location.y, null);
			}
			int /*long*/ topHandle = children [i].topHandle();
			int bits = OS.GetWindowLong (topHandle, OS.GWL_STYLE);
			if ((bits & OS.WS_VISIBLE) != 0) {
				children [i].printWidget (topHandle, hdc, gc);
			}
			if (graphicsMode == OS.GM_ADVANCED) {
				float [] lpXform = {1, 0, 0, 1, -location.x, -location.y};
				OS.ModifyWorldTransform(hdc, lpXform, OS.MWT_LEFTMULTIPLY);
			}
		}
		OS.RestoreDC (hdc, nSavedDC);
	}
}

void releaseWidget () {
	super.releaseWidget ();
	text = null;
}

public void setFont (Font font) {
	checkWidget ();
	Rectangle oldRect = getClientArea ();
	super.setFont (font);
	Rectangle newRect = getClientArea ();
	if (!oldRect.equals (newRect)) sendResize ();
}

/**
 * Sets the receiver's text, which is the string that will
 * be displayed as the receiver's <em>title</em>, to the argument,
 * which may not be null. The string may include the mnemonic character.
 * </p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, focus is assigned
 * to the first child of the group. On most platforms, the
 * mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 * '&amp;' can be escaped by doubling it in the string, causing
 * a single '&amp;' to be displayed.
 * </p>
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
	/*
	* Bug in Windows.  When a group control is right-to-left and
	* is disabled, the first pixel of the text is clipped.  The
	* fix is to add a space to both sides of the text.
	*/
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		if (OS.COMCTL32_MAJOR < 6 || !OS.IsAppThemed ()) {
			if (!OS.IsWindowEnabled (handle)) {
				if (string.length() != 0) string = " " + string + " ";
			}
		}
	}
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	OS.SetWindowText (handle, buffer);
}

int widgetStyle () {
	/*
	* Bug in Windows.  When GetDCEx() is called with DCX_INTERSECTUPDATE,
	* the HDC that is returned does not include the current update region.
	* This was confirmed under DEBUG Windows when GetDCEx() complained about
	* invalid flags.  Therefore, it is not easily possible to get an HDC from
	* outside of WM_PAINT that includes the current damage and clips children.
	* Because the receiver has children and draws a frame and label, it is
	* necessary that the receiver always draw clipped, in the current damaged
	* area.  The fix is to force the receiver to be fully clipped by including
	* WS_CLIPCHILDREN and WS_CLIPSIBLINGS in the default style bits.
	*/
	return super.widgetStyle () | OS.BS_GROUPBOX | OS.WS_CLIPCHILDREN | OS.WS_CLIPSIBLINGS;
}

TCHAR windowClass () {
	return GroupClass;
}

int /*long*/ windowProc () {
	return GroupProc;
}

LRESULT WM_ERASEBKGND (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  Group boxes do not erase
	* the background before drawing.  The fix is to
	* fill the background.
	*/
	drawBackground (wParam);
	return LRESULT.ONE;
}

LRESULT WM_NCHITTEST (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_NCHITTEST (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  The window proc for the group box
	* returns HTTRANSPARENT indicating that mouse messages
	* should not be delivered to the receiver and any children.
	* Normally, group boxes in Windows do not have children and
	* this is the correct behavior for this case.  Because we
	* allow children, answer HTCLIENT to allow mouse messages
	* to be delivered to the children.
	*/
	int /*long*/ code = callWindowProc (handle, OS.WM_NCHITTEST, wParam, lParam);
	if (code == OS.HTTRANSPARENT) code = OS.HTCLIENT;
	return new LRESULT (code);
}

LRESULT WM_MOUSEMOVE (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_MOUSEMOVE (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  In version 6.00 of COMCTL32.DLL,
	* every time the mouse moves, the group title redraws.
	* This only happens when WM_NCHITTEST returns HTCLIENT.
	* The fix is to avoid calling the group window proc.
	*/
	return LRESULT.ZERO;
}

LRESULT WM_PRINTCLIENT (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_PRINTCLIENT (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  In version 6.00 of COMCTL32.DLL,
	* when WM_PRINTCLIENT is sent from a child BS_GROUP
	* control to a parent BS_GROUP, the parent BS_GROUP
	* clears the font from the HDC.  Normally, group boxes
	* in Windows do not have children so this behavior is
	* undefined.  When the parent of a BS_GROUP is not a
	* BS_GROUP, there is no problem.  The fix is to save
	* and restore the current font.
	*/
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		int nSavedDC = OS.SaveDC (wParam);
		int /*long*/ code = callWindowProc (handle, OS.WM_PRINTCLIENT, wParam, lParam);
		OS.RestoreDC (wParam, nSavedDC);
		return new LRESULT (code);
	}
	return result;
}

LRESULT WM_UPDATEUISTATE (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_UPDATEUISTATE (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  When WM_UPDATEUISTATE is sent to
	* a group, it sends WM_CTLCOLORBTN to get the foreground
	* and background.  If drawing happens in WM_CTLCOLORBTN,
	* it will overwrite the contents of the control.  The
	* fix is draw the group without drawing the background
	* and avoid the group window proc.
	*/
	boolean redraw = findImageControl () != null;
	if (!redraw) {
		if ((state & THEME_BACKGROUND) != 0) {
			if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
				redraw = findThemeControl () != null;
			}
		}
		if (!redraw) redraw = findBackgroundControl () != null;
	}
	if (redraw) {
		OS.InvalidateRect (handle, null, false);
		int /*long*/ code = OS.DefWindowProc (handle, OS.WM_UPDATEUISTATE, wParam, lParam);
		return new LRESULT (code);
	}
	return result;
}

LRESULT WM_WINDOWPOSCHANGING (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_WINDOWPOSCHANGING (wParam, lParam);
	if (result != null) return result;
	/*
	* Invalidate the portion of the group widget that needs to
	* be redrawn.  Note that for some reason, invalidating the
	* group from inside WM_SIZE causes pixel corruption for
	* radio button children.
	*/
	if (OS.IsWinCE) return result;
	if (!OS.IsWindowVisible (handle)) return result;
	WINDOWPOS lpwp = new WINDOWPOS ();
	OS.MoveMemory (lpwp, lParam, WINDOWPOS.sizeof);
	if ((lpwp.flags & (OS.SWP_NOSIZE | OS.SWP_NOREDRAW)) != 0) {
		return result;
	}
	RECT rect = new RECT ();
	OS.SetRect (rect, 0, 0, lpwp.cx, lpwp.cy);
	OS.SendMessage (handle, OS.WM_NCCALCSIZE, 0, rect);
	int newWidth = rect.right - rect.left;
	int newHeight = rect.bottom - rect.top;
	OS.GetClientRect (handle, rect);
	int oldWidth = rect.right - rect.left;
	int oldHeight = rect.bottom - rect.top;
	if (newWidth == oldWidth && newHeight == oldHeight) {
		return result;
	}
	if (newWidth != oldWidth) {
		int left = oldWidth;
		if (newWidth < oldWidth) left = newWidth;
		OS.SetRect (rect, left - CLIENT_INSET, 0, newWidth, newHeight);
		OS.InvalidateRect (handle, rect, true);
	}
	if (newHeight != oldHeight) {
		int bottom = oldHeight;
		if (newHeight < oldHeight) bottom = newHeight;
		if (newWidth < oldWidth) oldWidth -= CLIENT_INSET;
		OS.SetRect (rect, 0, bottom - CLIENT_INSET, oldWidth, newHeight);
		OS.InvalidateRect (handle, rect, true);
	}
	return result;
}
}

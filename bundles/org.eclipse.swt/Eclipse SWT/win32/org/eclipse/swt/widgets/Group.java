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
 */

public class Group extends Composite {
	static final int CLIENT_INSET = 3;
	static final int GroupProc;
	static final TCHAR GroupClass = new TCHAR (0, OS.IsWinCE ? "BUTTON" : "SWT_GROUP", true);
	static {
		/*
		* Feature in Windows.  The group box window class
		* uses the CS_HREDRAW and CS_VREDRAW style bits to
		* force a full redraw of the control and all children
		* when resized.  This causes flashing.  The fix is to
		* register a new window class without these bits and
		* implement special code that damages only the exposed
		* area.
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
			int hInstance = OS.GetModuleHandle (null);
			if (!OS.GetClassInfo (hInstance, GroupClass, lpWndClass)) {
				int hHeap = OS.GetProcessHeap ();
				lpWndClass.hInstance = hInstance;
				lpWndClass.style &= ~(OS.CS_HREDRAW | OS.CS_VREDRAW);
				int byteCount = GroupClass.length () * TCHAR.sizeof;
				int lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
				OS.MoveMemory (lpszClassName, GroupClass, byteCount);
				lpWndClass.lpszClassName = lpszClassName;
				OS.RegisterClass (lpWndClass);
//				OS.HeapFree (hHeap, 0, lpszClassName);
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

int callWindowProc (int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	/*
	* Feature in Windows.  When the user clicks on the group
	* box label, the group box takes focus.  This is unwanted.
	* The fix is to avoid calling the group box window proc.
	*/
	switch (msg) {
		case OS.WM_LBUTTONDOWN:
		case OS.WM_LBUTTONDBLCLK: 
			return OS.DefWindowProc (handle, msg, wParam, lParam);
	}
	return OS.CallWindowProc (GroupProc, handle, msg, wParam, lParam);
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

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	Rectangle trim = super.computeTrim (x, y, width, height);
	int newFont, oldFont = 0;
	int hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	TEXTMETRIC tm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
	OS.GetTextMetrics (hDC, tm);
	int textWidth = 0;
	int length = OS.GetWindowTextLength (handle);
	if (length != 0) {
		/*
		* If the group has text, and the text is wider than the
		* client area, pad the width so the text is not clipped.
		*/
		TCHAR buffer1 = new TCHAR (getCodePage (), length + 1);
		OS.GetWindowText (handle, buffer1, length + 1);
		RECT rect = new RECT ();
		int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE;
		OS.DrawText (hDC, buffer1, length, rect, flags);
		textWidth = rect.right - rect.left + CLIENT_INSET * 4;
	}
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	trim.x -= CLIENT_INSET;
	trim.y -= tm.tmHeight;
	trim.width = Math.max (trim.width, textWidth) + CLIENT_INSET * 2;
	trim.height += tm.tmHeight + CLIENT_INSET;
	return trim;
}

void createHandle () {
	super.createHandle ();
	state &= ~CANVAS;
}

public Rectangle getClientArea () {
	checkWidget ();
	forceResize ();
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	int newFont, oldFont = 0;
	int hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	TEXTMETRIC tm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
	OS.GetTextMetrics (hDC, tm);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	int x = CLIENT_INSET, y = tm.tmHeight;
	int width = rect.right - CLIENT_INSET * 2;
	int height = rect.bottom - y - CLIENT_INSET;
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
	int length = OS.GetWindowTextLength (handle);
	if (length == 0) return "";
	TCHAR buffer = new TCHAR (getCodePage (), length + 1);
	OS.GetWindowText (handle, buffer, length + 1);
	return buffer.toString (0, length);
}

boolean mnemonicHit (char key) {
	return setFocus ();
}

boolean mnemonicMatch (char key) {
	char mnemonic = findMnemonic (getText ());
	if (mnemonic == '\0') return false;
	return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
}

/**
 * Sets the receiver's text, which is the string that will
 * be displayed as the receiver's <em>title</em>, to the argument,
 * which may not be null. The string may include the mnemonic character.
 * </p>
 * Mnemonics are indicated by an '&amp' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, focus is assgned
 * to the first child of the group. On most platforms, the
 * mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 *'&amp' can be escaped by doubling it in the string, causing
 * a single '&amp' to be displayed.
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
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	OS.SetWindowText (handle, buffer);
}

int widgetStyle () {
	/*
	* Bug in Windows.  When GetDCEx () is called with DCX_INTERSECTUPDATE,
	* the HDC that is returned does not include the current update region.
	* This was confirmed under DEBUG Windows when GetDCEx () complained about
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

int windowProc () {
	return GroupProc;
}

LRESULT WM_ERASEBKGND (int wParam, int lParam) {
	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
	if (result != null) return result;
	/*
	* Feaure in Windows.  Group boxes do not erase
	* the background before drawing.  The fix is to
	* fill the background.
	*/
	drawBackground (wParam);
	return LRESULT.ONE;
}

LRESULT WM_NCHITTEST (int wParam, int lParam) {
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
	int code = callWindowProc (OS.WM_NCHITTEST, wParam, lParam);
	if (code == OS.HTTRANSPARENT) code = OS.HTCLIENT;
	return new LRESULT (code);
}

LRESULT WM_MOUSEMOVE (int wParam, int lParam) {
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

LRESULT WM_PRINTCLIENT (int wParam, int lParam) {
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
	if (COMCTL32_MAJOR >= 6) {
		int hFont = OS.GetCurrentObject (wParam, OS.OBJ_FONT);
		int code = callWindowProc (OS.WM_PRINTCLIENT, wParam, lParam);
		OS.SelectObject (wParam, hFont);
		return new LRESULT (code);
	}
	return result;
}

LRESULT WM_SIZE (int wParam, int lParam) {
	LRESULT result = super.WM_SIZE (wParam, lParam);
	if (OS.IsWinCE) return result;
	OS.InvalidateRect (handle, null, true);
	return result;
}

}

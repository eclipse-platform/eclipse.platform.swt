/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify numeric
 * values.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>READ_ONLY</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, Modify</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @since 3.1
 */
public class Spinner extends Composite {
	int hwndText, hwndUpDown;
	boolean ignoreModify;
	int pageIncrement;
	static final int EditProc;
	static final TCHAR EditClass = new TCHAR (0, "EDIT", true);
	static final int UpDownProc;
	static final TCHAR UpDownClass = new TCHAR (0, OS.UPDOWN_CLASS, true);	
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, EditClass, lpWndClass);
		EditProc = lpWndClass.lpfnWndProc;
		OS.GetClassInfo (0, UpDownClass, lpWndClass);
		UpDownProc = lpWndClass.lpfnWndProc;
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
 * @see SWT#READ_ONLY
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Spinner (Composite parent, int style) {
	super (parent, checkStyle (style));
}

int callWindowProc (int hwnd, int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	if (hwnd == hwndText) {
		return OS.CallWindowProc (EditProc, hwnd, msg, wParam, lParam);
	}
	if (hwnd == hwndUpDown) {
		return OS.CallWindowProc (UpDownProc, hwnd, msg, wParam, lParam);
	}
	return OS.DefWindowProc (handle, msg, wParam, lParam);
}

static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

boolean checkHandle (int hwnd) {
	return hwnd == hwndText || hwnd == hwndUpDown || hwnd == handle;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void createHandle () {
	super.createHandle ();
	state &= ~CANVAS;
	int hInstance = OS.GetModuleHandle (null);
	int textExStyle = (style & SWT.BORDER) != 0 ? OS.WS_EX_CLIENTEDGE : 0;
	int textStyle = OS.WS_CHILD | OS.WS_VISIBLE | OS.ES_AUTOHSCROLL;
	if ((style & SWT.READ_ONLY) != 0) textStyle |= OS.ES_READONLY;
	hwndText = OS.CreateWindowEx (
        textExStyle,
        EditClass,
        null,
        textStyle,
        0, 0, 0, 0,
        handle,
        0,
        hInstance,
        null);
	if (hwndText == 0) error (SWT.ERROR_NO_HANDLES);
	int upDownStyle = OS.WS_CHILD | OS.WS_VISIBLE | OS.UDS_AUTOBUDDY | OS.UDS_WRAP | OS.UDS_SETBUDDYINT;
	hwndUpDown = OS.CreateWindowEx (
        0,
        UpDownClass,
        null,
        upDownStyle,
        0, 0, 0, 0,
        handle,
        0,
        hInstance,
        null);
	if (hwndUpDown == 0) error (SWT.ERROR_NO_HANDLES);
	OS.SendMessage (hwndUpDown, OS.UDM_SETRANGE32, 0, 100);
	OS.SendMessage (hwndUpDown, OS.IsWinCE ? OS.UDM_SETPOS : OS.UDM_SETPOS32, 0, 0);
	pageIncrement = 10;
	TCHAR buffer = new TCHAR (getCodePage (), "0", true);
	OS.SetWindowText (hwndText, buffer);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is modified, by sending
 * it one of the messages defined in the <code>ModifyListener</code>
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
 * @see ModifyListener
 * @see #removeModifyListener
 */
public void addModifyListener (ModifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text.
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

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is verified, by sending
 * it one of the messages defined in the <code>VerifyListener</code>
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
 * @see VerifyListener
 * @see #removeVerifyListener
 */
void addVerifyListener (VerifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		int newFont, oldFont = 0;
		int hDC = OS.GetDC (hwndText);
		newFont = OS.SendMessage (hwndText, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		TEXTMETRIC tm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
		OS.GetTextMetrics (hDC, tm);
		height = tm.tmHeight;
		RECT rect = new RECT ();
		int [] max = new int [1];
		OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, null, max);
		TCHAR buffer = new TCHAR (getCodePage (), String.valueOf (max [0]), false);
		int flags = OS.DT_CALCRECT | OS.DT_EDITCONTROL | OS.DT_NOPREFIX;
		OS.DrawText (hDC, buffer, buffer.length (), rect, flags);
		width = rect.right - rect.left;
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (hwndText, hDC);
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrim (0, 0, width, height);
	if (hHint == SWT.DEFAULT) {
		trim.height = Math.max (trim.height, OS.GetSystemMetrics (OS.SM_CYVSCROLL));
	}
	return new Point (trim.width, trim.height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	
	/* Get the trim of the text control */
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);
	int bits0 = OS.GetWindowLong (hwndText, OS.GWL_STYLE);
	int bits1 = OS.GetWindowLong (hwndText, OS.GWL_EXSTYLE);
	OS.AdjustWindowRectEx (rect, bits0, false, bits1);
	width = rect.right - rect.left;
	height = rect.bottom - rect.top;
	
	/*
	* The preferred height of a single-line text widget
	* has been hand-crafted to be the same height as
	* the single-line text widget in an editable combo
	* box.
	*/
	int margins = OS.SendMessage (hwndText, OS.EM_GETMARGINS, 0, 0);
	x -= margins & 0xFFFF;
	width += (margins & 0xFFFF) + ((margins >> 16) & 0xFFFF);
	if ((style & SWT.BORDER) != 0) {
		x -= 1;
		y -= 1;
		width += 2;
		height += 2;
	}
	width += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
	return new Rectangle (x, y, width, height);
}

/**
 * Copies the selected text.
 * <p>
 * The current selection is copied to the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void copy () {
	checkWidget ();
	OS.SendMessage (hwndText, OS.WM_COPY, 0, 0);
}

/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void cut () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	OS.SendMessage (hwndText, OS.WM_CUT, 0, 0);
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
}

void deregister () {
	super.deregister ();
	display.removeControl (hwndText);
	display.removeControl (hwndUpDown);
}

boolean hasFocus () {
	int hwndFocus = OS.GetFocus ();
	if (hwndFocus == handle) return true;
	if (hwndFocus == hwndText) return true;
	if (hwndFocus == hwndUpDown) return true;
	return false;
}

/**
 * Returns the amount that the receiver's value will be
 * modified by when the up/down arrows are pressed.
 *
 * @return the increment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getIncrement () {
	checkWidget ();
	UDACCEL udaccel = new UDACCEL ();
	OS.SendMessage (hwndUpDown, OS.UDM_GETACCEL, 1, udaccel);
	return udaccel.nInc;
}

/**
 * Returns the maximum value which the receiver will allow.
 *
 * @return the maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMaximum () {
	checkWidget ();
	int [] max = new int [1];
	OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, null, max);
	return max [0];
}

/**
 * Returns the minimum value which the receiver will allow.
 *
 * @return the minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMinimum () {
	checkWidget ();
	int [] min = new int [1];
	OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, min, null);
	return min [0];
}

/**
 * Returns the amount that the receiver's position will be
 * modified by when the page up/down keys are pressed.
 *
 * @return the page increment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getPageIncrement () {
	checkWidget ();
	return pageIncrement;
}

/**
 * Returns the <em>selection</em>, which is the receiver's position.
 *
 * @return the selection 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelection () {
	checkWidget ();
	if (OS.IsWinCE) {
		return OS.SendMessage (hwndUpDown, OS.UDM_GETPOS, 0, 0) & 0xFFFF;
	} else {
		return OS.SendMessage (hwndUpDown, OS.UDM_GETPOS32, 0, 0);
	}
}

int getSelectionText () {
	int length = OS.GetWindowTextLength (hwndText);
	TCHAR buffer = new TCHAR (getCodePage (), length + 1);
	OS.GetWindowText (hwndText, buffer, length + 1);
	String string = buffer.toString (0, length);
	try {
		int value = Integer.parseInt (string);
		int [] max = new int [1], min = new int [1];
		OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, min, max);
		if (min [0] <= value && value <= max [0]) return value;
	} catch (NumberFormatException e) {
	}
	if (OS.IsWinCE) {
		return OS.SendMessage (hwndUpDown, OS.UDM_GETPOS, 0, 0) & 0xFFFF;
	} else {
		return OS.SendMessage (hwndUpDown, OS.UDM_GETPOS32, 0, 0);
	}
}

int mbcsToWcsPos (int mbcsPos) {
	if (mbcsPos <= 0) return 0;
	if (OS.IsUnicode) return mbcsPos;
	int mbcsSize = OS.GetWindowTextLengthA (hwndText);
	if (mbcsSize == 0) return 0;
	if (mbcsPos >= mbcsSize) return mbcsSize;
	byte [] buffer = new byte [mbcsSize + 1];
	OS.GetWindowTextA (hwndText, buffer, mbcsSize + 1);
	return OS.MultiByteToWideChar (getCodePage (), OS.MB_PRECOMPOSED, buffer, mbcsPos, null, 0);
}

/**
 * Pastes text from clipboard.
 * <p>
 * The selected text is deleted from the widget
 * and new text inserted from the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void paste () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	OS.SendMessage (hwndText, OS.WM_PASTE, 0, 0);
}

void register () {
	super.register ();
	display.addControl (hwndText, this);	
	display.addControl (hwndUpDown, this);
}

void releaseHandle () {
	super.releaseHandle ();
	hwndText = hwndUpDown = 0;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's text is modified.
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
 * @see ModifyListener
 * @see #addModifyListener
 */
public void removeModifyListener (ModifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);	
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
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
 * be notified when the control is verified.
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
 * @see VerifyListener
 * @see #addVerifyListener
 */
void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);	
}

boolean sendKeyEvent (int type, int msg, int wParam, int lParam, Event event) {
	if (!super.sendKeyEvent (type, msg, wParam, lParam, event)) {
		return false;
	}
	if ((style & SWT.READ_ONLY) != 0) return true;
	if (type != SWT.KeyDown) return true;
	if (msg != OS.WM_CHAR && msg != OS.WM_KEYDOWN && msg != OS.WM_IME_CHAR) {
		return true;
	}
	if (event.character == 0) return true;
//	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return true;
	char key = event.character;
	int stateMask = event.stateMask;
	
	/*
	* Disable all magic keys that could modify the text
	* and don't send events when Alt, Shift or Ctrl is
	* pressed.
	*/
	switch (msg) {
		case OS.WM_CHAR:
			if (key != 0x08 && key != 0x7F && key != '\r' && key != '\t' && key != '\n') break;
			// FALL THROUGH
		case OS.WM_KEYDOWN:
			if ((stateMask & (SWT.ALT | SWT.SHIFT | SWT.CONTROL)) != 0) return false;
			break;
	}

	/*
	* If the left button is down, the text widget refuses the character.
	*/
	if (OS.GetKeyState (OS.VK_LBUTTON) < 0) {
		return true;
	}

	/* Verify the character */
	String oldText = "";
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
	switch (key) {
		case 0x08:	/* Bs */
			if (start [0] == end [0]) {
				if (start [0] == 0) return true;
				start [0] = start [0] - 1;
				if (OS.IsDBLocale) {
					int [] newStart = new int [1], newEnd = new int [1];
					OS.SendMessage (hwndText, OS.EM_SETSEL, start [0], end [0]);
					OS.SendMessage (hwndText, OS.EM_GETSEL, newStart, newEnd);
					if (start [0] != newStart [0]) start [0] = start [0] - 1;
				}
				start [0] = Math.max (start [0], 0);
			}
			break;
		case 0x7F:	/* Del */
			if (start [0] == end [0]) {
				int length = OS.GetWindowTextLength (hwndText);
				if (start [0] == length) return true;
				end [0] = end [0] + 1;
				if (OS.IsDBLocale) {
					int [] newStart = new int [1], newEnd = new int [1];
					OS.SendMessage (hwndText, OS.EM_SETSEL, start [0], end [0]);
					OS.SendMessage (hwndText, OS.EM_GETSEL, newStart, newEnd);
					if (end [0] != newEnd [0]) end [0] = end [0] + 1;
				}
				end [0] = Math.min (end [0], length);
			}
			break;
		case '\r':	/* Return */
			return true;
		default:	/* Tab and other characters */
			if (key != '\t' && key < 0x20) return true;
			oldText = new String (new char [] {key});
			break;
	}
	String newText = verifyText (oldText, start [0], end [0], event);
	if (newText == null) return false;
	if (newText == oldText) return true;
	TCHAR buffer = new TCHAR (getCodePage (), newText, true);
	OS.SendMessage (hwndText, OS.EM_SETSEL, start [0], end [0]);
	OS.SendMessage (hwndText, OS.EM_REPLACESEL, 0, buffer);
	return false;
}

void setBackgroundPixel (int pixel) {
	if (background == pixel) return;
	super.setBackgroundPixel (pixel);
	OS.InvalidateRect (hwndText, null, true);
}

void setForegroundPixel (int pixel) {
	if (foreground == pixel) return;
	super.setForegroundPixel (pixel);
	OS.InvalidateRect (hwndText, null, true);
}

/**
 * Sets the amount that the receiver's value will be
 * modified by when the up/down arrows are pressed to
 * the argument, which must be at least one.
 *
 * @param increment the new increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setIncrement (int value) {
	checkWidget ();
	if (value < 1) return;
	int hHeap = OS.GetProcessHeap ();
	int count = OS.SendMessage (hwndUpDown, OS.UDM_GETACCEL, 0, (UDACCEL)null);
	int udaccels = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, UDACCEL.sizeof * count);
	OS.SendMessage (hwndUpDown, OS.UDM_GETACCEL, count, udaccels);
	int first = -1;
	UDACCEL udaccel = new UDACCEL ();
	for (int i = 0; i < count; i++) {
		int offset = udaccels + (i * UDACCEL.sizeof);
		OS.MoveMemory (udaccel, offset, UDACCEL.sizeof);
		if (first == -1) first = udaccel.nInc;
		udaccel.nInc  =  udaccel.nInc * value / first;
		OS.MoveMemory (offset, udaccel, UDACCEL.sizeof);
	}
	OS.SendMessage (hwndUpDown, OS.UDM_SETACCEL, count, udaccels);
	OS.HeapFree (hHeap, 0, udaccels);
}

/**
 * Sets the maximum value that the receiver will allow.  This new
 * value will be ignored if it is not greater than the receiver's current
 * minimum value.  If the new maximum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new maximum, which must be greater than the current minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMaximum (int value) {
	checkWidget ();
	if (value < 0) return;
	int [] min = new int [1];
	OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, min, null);
	if (value <= min [0]) return;
	int pos;
	if (OS.IsWinCE) {
		pos = OS.SendMessage (hwndUpDown, OS.UDM_GETPOS, 0, 0) & 0xFFFF;
	} else {
		pos = OS.SendMessage (hwndUpDown, OS.UDM_GETPOS32, 0, 0);
	}
	OS.SendMessage (hwndUpDown , OS.UDM_SETRANGE32, min [0], value);	
	if (pos > value) setSelection (value, false);
}

/**
 * Sets the minimum value that the receiver will allow.  This new
 * value will be ignored if it is negative or is not less than the receiver's
 * current maximum value.  If the new minimum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new minimum, which must be nonnegative and less than the current maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinimum (int value) {
	checkWidget ();
	if (value < 0) return;
	int [] max = new int [1];
	OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, null, max);
	if (value >= max [0]) return;
	int pos;
	if (OS.IsWinCE) {
		pos = OS.SendMessage (hwndUpDown, OS.UDM_GETPOS, 0, 0) & 0xFFFF;
	} else {
		pos = OS.SendMessage (hwndUpDown, OS.UDM_GETPOS32, 0, 0);
	}
	OS.SendMessage (hwndUpDown , OS.UDM_SETRANGE32, value, max [0]);
	if (pos < value) setSelection (value, false);
}

/**
 * Sets the amount that the receiver's position will be
 * modified by when the page up/down keys are pressed
 * to the argument, which must be at least one.
 *
 * @param pageIncrement the page increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setPageIncrement (int value) {
	checkWidget ();
	if (value < 1) return;
	pageIncrement = value;
}

/**
 * Sets the <em>selection</em>, which is the receiver's
 * position, to the argument. If the argument is not within
 * the range specified by minimum and maximum, it will be
 * adjusted to fall within this range.
 *
 * @param value the new selection (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int value) {
	checkWidget ();
	int [] max = new int [1], min = new int [1];
	OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, min, max);
	value = Math.min (Math.max (min [0], value), max [0]);
	setSelection (value, false);
}

void setSelection (int value, boolean notify) {
	OS.SendMessage (hwndUpDown , OS.IsWinCE ? OS.UDM_SETPOS : OS.UDM_SETPOS32, 0, value);
	String string = String.valueOf (value);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int length = OS.GetWindowTextLength (hwndText);
		string = verifyText (string, 0, length, null);
		if (string == null) return;
	}
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	OS.SetWindowText (hwndText, buffer);
	if (notify) postEvent (SWT.Selection);
}

void subclass () {
	super.subclass ();
	int newProc = display.windowProc;
	OS.SetWindowLong (hwndText, OS.GWL_WNDPROC, newProc);
	OS.SetWindowLong (hwndUpDown, OS.GWL_WNDPROC, newProc);
}

void unsubclass () {
	super.unsubclass ();
	OS.SetWindowLong (hwndText, OS.GWL_WNDPROC, EditProc);
	OS.SetWindowLong (hwndUpDown, OS.GWL_WNDPROC, UpDownProc);
}

String verifyText (String string, int start, int end, Event keyEvent) {
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
	if (keyEvent != null) {
		event.character = keyEvent.character;
		event.keyCode = keyEvent.keyCode;
		event.stateMask = keyEvent.stateMask;
	}
	int index = 0;
	while (index < string.length ()) {
		if (!Character.isDigit (string.charAt (index))) break;
		index++;
	}
	event.doit = index == string.length ();
	if (OS.IsDBLocale) {
		event.start = mbcsToWcsPos (start);
		event.end = mbcsToWcsPos (end);
	}
	sendEvent (SWT.Verify, event);
	if (!event.doit || isDisposed ()) return null;
	return event.text;
}

int widgetExtStyle () {
	return super.widgetExtStyle () & ~OS.WS_EX_CLIENTEDGE;
}

int windowProc (int hwnd, int msg, int wParam, int lParam) {
	if (hwnd == hwndText || hwnd == hwndUpDown) {
		LRESULT result = null;
		switch (msg) {
			/* Keyboard messages */
			case OS.WM_CHAR:		result = wmChar (hwnd, wParam, lParam); break;
			case OS.WM_IME_CHAR:	result = wmIMEChar (hwnd, wParam, lParam); break;
			case OS.WM_KEYDOWN:		result = wmKeyDown (hwnd, wParam, lParam); break;
			case OS.WM_KEYUP:		result = wmKeyUp (hwnd, wParam, lParam); break;
			case OS.WM_SYSCHAR:		result = wmSysChar (hwnd, wParam, lParam); break;
			case OS.WM_SYSKEYDOWN:	result = wmSysKeyDown (hwnd, wParam, lParam); break;
			case OS.WM_SYSKEYUP:	result = wmSysKeyUp (hwnd, wParam, lParam); break;
	
			/* Mouse Messages */
			case OS.WM_LBUTTONDBLCLK:	result = wmLButtonDblClk (hwnd, wParam, lParam); break;
			case OS.WM_LBUTTONDOWN:		result = wmLButtonDown (hwnd, wParam, lParam); break;
			case OS.WM_LBUTTONUP:		result = wmLButtonUp (hwnd, wParam, lParam); break;
			case OS.WM_MBUTTONDBLCLK:	result = wmMButtonDblClk (hwnd, wParam, lParam); break;
			case OS.WM_MBUTTONDOWN:		result = wmMButtonDown (hwnd, wParam, lParam); break;
			case OS.WM_MBUTTONUP:		result = wmMButtonUp (hwnd, wParam, lParam); break;
			case OS.WM_MOUSEHOVER:		result = wmMouseHover (hwnd, wParam, lParam); break;
			case OS.WM_MOUSELEAVE:		result = wmMouseLeave (hwnd, wParam, lParam); break;
			case OS.WM_MOUSEMOVE:		result = wmMouseMove (hwnd, wParam, lParam); break;
//			case OS.WM_MOUSEWHEEL:		result = wmMouseWheel (hwnd, wParam, lParam); break;
			case OS.WM_RBUTTONDBLCLK:	result = wmRButtonDblClk (hwnd, wParam, lParam); break;
			case OS.WM_RBUTTONDOWN:		result = wmRButtonDown (hwnd, wParam, lParam); break;
			case OS.WM_RBUTTONUP:		result = wmRButtonUp (hwnd, wParam, lParam); break;
			
			/* Focus Messages */
			case OS.WM_SETFOCUS:		result = wmSetFocus (hwnd, wParam, lParam); break;
			case OS.WM_KILLFOCUS:		result = wmKillFocus (hwnd, wParam, lParam); break;
	
			/* Paint messages */
			case OS.WM_PAINT:			result = wmPaint (hwnd, wParam, lParam); break;
	
			/* Menu messages */
			case OS.WM_CONTEXTMENU:		result = wmContextMenu (hwnd, wParam, lParam);
				
			/* Clipboard messages */
			case OS.WM_CLEAR:
			case OS.WM_CUT:
			case OS.WM_PASTE:
			case OS.WM_UNDO:
			case OS.EM_UNDO:
				if (hwnd == hwndText) {
					result = wmClipboard (hwnd, msg, wParam, lParam);
				}
				break;
		}
		if (result != null) return result.value;
		return callWindowProc (hwnd, msg, wParam, lParam);
	}
	return super.windowProc (hwnd, msg, wParam, lParam);
}

LRESULT WM_ERASEBKGND (int wParam, int lParam) {
	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
	if (result != null) return result;
	drawBackground (wParam);
	return LRESULT.ONE;
}

LRESULT WM_KILLFOCUS (int wParam, int lParam) {
	return null;
}

LRESULT WM_SETFOCUS (int wParam, int lParam) {
	OS.SetFocus (hwndText);
	return null;
}

LRESULT WM_SETFONT (int wParam, int lParam) {
	LRESULT result = super.WM_SETFONT (wParam, lParam);
	if (result != null) return result;
	OS.SendMessage (hwndText, OS.WM_SETFONT, wParam, lParam);
	return result;
}

LRESULT WM_SIZE (int wParam, int lParam) {
	LRESULT result = super.WM_SIZE (wParam, lParam);
	if (isDisposed ()) return result;
	int width = lParam & 0xFFFF, height = lParam >> 16;
    int upDownWidth = OS.GetSystemMetrics (OS.SM_CXVSCROLL);
    int textWidth = width - upDownWidth;
    int flags = OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;    
    SetWindowPos (hwndText, 0, 0, 0, textWidth, height, flags);
    SetWindowPos (hwndUpDown, 0, textWidth, 0, upDownWidth, height, flags);              	
	return result;
}

LRESULT wmChar (int hwnd, int wParam, int lParam) {
	LRESULT result = super.wmChar (hwnd, wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  For some reason, when the
	* widget is a single line text widget, when the
	* user presses tab, return or escape, Windows beeps.
	* The fix is to look for these keys and not call
	* the window proc.
	*/
	switch (wParam) {
		case SWT.CR:
			int value = getSelectionText ();
			setSelection (value, true);
			postEvent (SWT.DefaultSelection);
			// FALL THROUGH		
		case SWT.TAB:
		case SWT.ESC: return LRESULT.ZERO;
	}
	return result;
}

LRESULT wmClipboard (int hwndText, int msg, int wParam, int lParam) {
	if ((style & SWT.READ_ONLY) != 0) return null;
//	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return null;
	boolean call = false;
	int [] start = new int [1], end = new int [1];
	String oldText = null, newText = null;
	switch (msg) {
		case OS.WM_CLEAR:
		case OS.WM_CUT:
			OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
			if (start [0] != end [0]) {
				newText = "";
				call = true;
			}
			break;
		case OS.WM_PASTE:
			OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
			newText = getClipboardText ();
			break;
		case OS.EM_UNDO:
		case OS.WM_UNDO:
			if (OS.SendMessage (hwndText, OS.EM_CANUNDO, 0, 0) != 0) {
				ignoreModify = true;
				OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
				if (OS.IsDBLocale) {
					start [0] = mbcsToWcsPos (start [0]);
					end [0] = mbcsToWcsPos (end [0]);
				}
				OS.CallWindowProc (EditProc, hwndText, msg, wParam, lParam);
				int length = OS.GetWindowTextLength (hwndText);
				if (length != 0 && start [0] != end [0]) {
					TCHAR buffer = new TCHAR (getCodePage (), length + 1);
					OS.GetWindowText (hwndText, buffer, length + 1);
					newText = buffer.toString (start [0], end [0] - start [0]);
				} else {
					newText = "";
				}
				OS.CallWindowProc (EditProc, hwndText, msg, wParam, lParam);
				ignoreModify = false;
			}
			break;
	}
	if (newText != null && !newText.equals (oldText)) {
		oldText = newText;
		newText = verifyText (newText, start [0], end [0], null);
		if (newText == null) return LRESULT.ZERO;
		if (!newText.equals (oldText)) {
			if (call) {
				OS.CallWindowProc (EditProc, hwndText, msg, wParam, lParam);
			}
			TCHAR buffer = new TCHAR (getCodePage (), newText, true);
			if (msg == OS.WM_SETTEXT) {
				int hHeap = OS.GetProcessHeap ();
				int byteCount = buffer.length () * TCHAR.sizeof;
				int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
				OS.MoveMemory (pszText, buffer, byteCount); 
				int code = OS.CallWindowProc (EditProc, hwndText, msg, wParam, pszText);
				OS.HeapFree (hHeap, 0, pszText);
				return new LRESULT (code);
			} else {
				OS.SendMessage (hwndText, OS.EM_REPLACESEL, 0, buffer);
				return LRESULT.ZERO;
			}
		}
	}
	return null;
}

LRESULT wmCommandChild (int wParam, int lParam) {
	int code = wParam >> 16;
	switch (code) {
		case OS.EN_CHANGE:
			if (ignoreModify) break;
			sendEvent (SWT.Modify);
			if (isDisposed ()) return LRESULT.ZERO;
			break;
	}
	return super.wmCommandChild (wParam, lParam);
}

LRESULT wmKeyDown (int hwnd, int wParam, int lParam) {
	LRESULT result = super.wmKeyDown (hwnd, wParam, lParam);
	if (result != null) return result;
	
	/* Increment the value */
	UDACCEL udaccel = new UDACCEL ();
	OS.SendMessage (hwndUpDown, OS.UDM_GETACCEL, 1, udaccel);
	int delta = 0;
	switch (wParam) {
		case OS.VK_UP: delta = udaccel.nInc; break;
		case OS.VK_DOWN: delta = -udaccel.nInc; break;
		case OS.VK_PRIOR: delta = pageIncrement; break;
		case OS.VK_NEXT: delta = -pageIncrement; break;
	}
	if (delta != 0) {
		int value = getSelectionText () + delta;
		int [] max = new int [1], min = new int [1];
		OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, min, max);
		if (value < min [0]) value = max [0];
		if (value > max [0]) value = min [0];
		setSelection (value, true);
	}
	
	/*  Stop the edit control from moving the caret */
	switch (wParam) {
		case OS.VK_UP:
		case OS.VK_DOWN:
			return LRESULT.ZERO;
	}
	return result;
}

LRESULT wmKillFocus (int hwnd, int wParam, int lParam) {
	int value = getSelectionText ();
	setSelection (value, true);
	return super.wmKillFocus (hwnd, wParam, lParam);
}

LRESULT wmScrollChild (int wParam, int lParam) {
	int code = wParam & 0xFFFF;
	switch (code) {
		case OS.SB_THUMBPOSITION:
			postEvent (SWT.Selection);
			break;
	}
	return super.wmScrollChild (wParam, lParam);
}

}

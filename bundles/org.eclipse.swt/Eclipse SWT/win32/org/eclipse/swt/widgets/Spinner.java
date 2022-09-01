/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify numeric
 * values.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, Modify, Verify</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#spinner">Spinner snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.1
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Spinner extends Composite {
	long hwndText, hwndUpDown;
	boolean ignoreModify, ignoreCharacter;
	int pageIncrement, digits;
	static final long EditProc;
	static final TCHAR EditClass = new TCHAR (0, "EDIT", true);
	static final long UpDownProc;
	static final TCHAR UpDownClass = new TCHAR (0, OS.UPDOWN_CLASS, true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, EditClass, lpWndClass);
		EditProc = lpWndClass.lpfnWndProc;
		OS.GetClassInfo (0, UpDownClass, lpWndClass);
		UpDownProc = lpWndClass.lpfnWndProc;
	}

	/**
	 * the operating system limit for the number of characters
	 * that the text field in an instance of this class can hold
	 *
	 * @since 3.4
	 */
	public static final int LIMIT;

	/*
	 * These values can be different on different platforms.
	 * Therefore they are not initialized in the declaration
	 * to stop the compiler from inlining.
	 */
	static {
		LIMIT = 0x7FFFFFFF;
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
 * @see SWT#WRAP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Spinner (Composite parent, int style) {
	super (parent, checkStyle (style));
}

@Override
long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
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

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
void createHandle () {
	super.createHandle ();
	state &= ~(CANVAS | THEME_BACKGROUND);
	long hInstance = OS.GetModuleHandle (null);
	int textExStyle = 0;
	int textStyle = OS.WS_CHILD | OS.WS_VISIBLE | OS.ES_AUTOHSCROLL | OS.WS_CLIPSIBLINGS;
	if ((style & SWT.READ_ONLY) != 0) textStyle |= OS.ES_READONLY;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) textExStyle |= OS.WS_EX_LAYOUTRTL;
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
	OS.SetWindowLongPtr (hwndText, OS.GWLP_ID, hwndText);
	int upDownStyle = OS.WS_CHILD | OS.WS_VISIBLE | OS.UDS_AUTOBUDDY;
	if ((style & SWT.WRAP) != 0) upDownStyle |= OS.UDS_WRAP;
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
	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE;
	OS.SetWindowPos (hwndText, hwndUpDown, 0, 0, 0, 0, flags);
	OS.SetWindowLongPtr (hwndUpDown, OS.GWLP_ID, hwndUpDown);
	OS.SendMessage (hwndUpDown, OS.UDM_SETRANGE32, 0, 100);
	OS.SendMessage (hwndUpDown, OS.UDM_SETPOS32, 0, 0);
	pageIncrement = 10;
	digits = 0;
	OS.SetWindowText (hwndText, new char [] {'0', '\0'});
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
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user
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

@Override Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		long newFont, oldFont = 0;
		long hDC = OS.GetDC (hwndText);
		newFont = OS.SendMessage (hwndText, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		TEXTMETRIC tm = new TEXTMETRIC ();
		OS.GetTextMetrics (hDC, tm);
		height = tm.tmHeight;
		RECT rect = new RECT ();
		int [] max = new int [1];
		OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, null, max);
		String string = String.valueOf (max [0]);
		if (digits > 0) {
			StringBuilder buffer = new StringBuilder ();
			buffer.append (string);
			buffer.append (getDecimalSeparator ());
			int count = digits - string.length ();
			while (count >= 0) {
				buffer.append ("0");
				count--;
			}
			string = buffer.toString ();
		}
		char [] buffer = string.toCharArray ();
		int flags = OS.DT_CALCRECT | OS.DT_EDITCONTROL | OS.DT_NOPREFIX;
		OS.DrawText (hDC, buffer, buffer.length, rect, flags);
		width = rect.right - rect.left;
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (hwndText, hDC);
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT)
		height = hHint;
	else {
		int borderAdjustment = (style & SWT.BORDER) != 0 ? -1 : 3;
		int upDownHeight = OS.GetSystemMetrics (OS.SM_CYVSCROLL);
		height = Math.max(height, upDownHeight + borderAdjustment);
	}

	Rectangle trim = computeTrimInPixels (0, 0, width, height);
	return new Point (trim.width, trim.height);
}

@Override Rectangle computeTrimInPixels (int x, int y, int width, int height) {
	checkWidget ();

	/* Get the trim of the text control */
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);
	int bits0 = OS.GetWindowLong (handle, OS.GWL_STYLE);
	int bits1 = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
	/*
	 * For a very long time, border was WS_EX_CLIENTEDGE. Now that is was
	 * changed to WS_BORDER, preserve old size for compatibility reasons.
	 */
	if ((bits0 & OS.WS_BORDER) != 0) {
		bits0 &= ~OS.WS_BORDER;
		bits1 |= OS.WS_EX_CLIENTEDGE;
	}
	OS.AdjustWindowRectEx (rect, bits0, false, bits1);
	width = rect.right - rect.left;
	height = rect.bottom - rect.top;

	/*
	* The preferred height of a single-line text widget
	* has been hand-crafted to be the same height as
	* the single-line text widget in an editable combo
	* box.
	*/
	long margins = OS.SendMessage (hwndText, OS.EM_GETMARGINS, 0, 0);
	x -= OS.LOWORD (margins);
	width += OS.LOWORD (margins) + OS.HIWORD (margins);
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

@Override
int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
}

@Override
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	OS.EnableWindow (hwndText, enabled);
	OS.EnableWindow (hwndUpDown, enabled);
}

@Override
void deregister () {
	super.deregister ();
	display.removeControl (hwndText);
	display.removeControl (hwndUpDown);
}

@Override
boolean hasFocus () {
	long hwndFocus = OS.GetFocus ();
	if (hwndFocus == handle) return true;
	if (hwndFocus == hwndText) return true;
	if (hwndFocus == hwndUpDown) return true;
	return false;
}

/**
 * Returns the number of decimal places used by the receiver.
 *
 * @return the digits
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getDigits () {
	checkWidget ();
	return digits;
}

String getDecimalSeparator () {
	char [] data = new char [4];
	int size = OS.GetLocaleInfo (OS.LOCALE_USER_DEFAULT, OS.LOCALE_SDECIMAL, data, 4);
	return size != 0 ? new String (data, 0, size - 1) : ".";
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
	return (int)OS.SendMessage (hwndUpDown, OS.UDM_GETPOS32, 0, 0);
}

int getSelectionText (boolean [] parseFail) {
	int length = OS.GetWindowTextLength (hwndText);
	char [] buffer = new char [length + 1];
	OS.GetWindowText (hwndText, buffer, length + 1);
	String string = new String (buffer, 0, length);
	try {
		int value;
		if (digits > 0) {
			String decimalSeparator = getDecimalSeparator ();
			int index = string.indexOf (decimalSeparator);
			if (index != -1)  {
				int startIndex = string.startsWith ("+") || string.startsWith ("-") ? 1 : 0;
				String wholePart = startIndex != index ? string.substring (startIndex, index) : "0";
				String decimalPart = string.substring (index + 1);
				if (decimalPart.length () > digits) {
					decimalPart = decimalPart.substring (0, digits);
				} else {
					int i = digits - decimalPart.length ();
					for (int j = 0; j < i; j++) {
						decimalPart = decimalPart + "0";
					}
				}
				int wholeValue = Integer.parseInt (wholePart);
				int decimalValue = Integer.parseInt (decimalPart);
				for (int i = 0; i < digits; i++) wholeValue *= 10;
				value = wholeValue + decimalValue;
				if (string.startsWith ("-")) value = -value;
			} else {
				value = Integer.parseInt (string);
				for (int i = 0; i < digits; i++) value *= 10;
			}
		} else {
			value = Integer.parseInt (string);
		}
		int [] max = new int [1], min = new int [1];
		OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, min, max);
		if (min [0] <= value && value <= max [0]) return value;
	} catch (NumberFormatException e) {
	}
	parseFail [0] = true;
	return -1;
}

/**
 * Returns a string containing a copy of the contents of the
 * receiver's text field, or an empty string if there are no
 * contents.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public String getText () {
	checkWidget ();
	int length = OS.GetWindowTextLength (hwndText);
	if (length == 0) return "";
	char [] buffer = new char [length + 1];
	OS.GetWindowText (hwndText, buffer, length + 1);
	return new String (buffer, 0, length);
}

/**
 * Returns the maximum number of characters that the receiver's
 * text field is capable of holding. If this has not been changed
 * by <code>setTextLimit()</code>, it will be the constant
 * <code>Spinner.LIMIT</code>.
 *
 * @return the text limit
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #LIMIT
 *
 * @since 3.4
 */
public int getTextLimit () {
	checkWidget ();
	return (int)OS.SendMessage (hwndText, OS.EM_GETLIMITTEXT, 0, 0) & 0x7FFFFFFF;
}

@Override
boolean isUseWsBorder () {
	return true;
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

@Override
void register () {
	super.register ();
	display.addControl (hwndText, this);
	display.addControl (hwndUpDown, this);
}

@Override
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
 * be notified when the control is selected by the user.
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
 * be notified when the control is verified.
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
 * @see VerifyListener
 * @see #addVerifyListener
 */
void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);
}

@Override
boolean sendKeyEvent (int type, int msg, long wParam, long lParam, Event event) {
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
				start [0] = Math.max (start [0], 0);
			}
			break;
		case 0x7F:	/* Del */
			if (start [0] == end [0]) {
				int length = OS.GetWindowTextLength (hwndText);
				if (start [0] == length) return true;
				end [0] = end [0] + 1;
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

@Override
void setBackgroundImage (long hBitmap) {
	super.setBackgroundImage (hBitmap);
	OS.InvalidateRect (hwndText, null, true);
}

@Override
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	OS.InvalidateRect (hwndText, null, true);
}

/**
 * Sets the number of decimal places used by the receiver.
 * <p>
 * The digit setting is used to allow for floating point values in the receiver.
 * For example, to set the selection to a floating point value of 1.37 call setDigits() with
 * a value of 2 and setSelection() with a value of 137. Similarly, if getDigits() has a value
 * of 2 and getSelection() returns 137 this should be interpreted as 1.37. This applies to all
 * numeric APIs.
 * </p>
 *
 * @param value the new digits (must be greater than or equal to zero)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the value is less than zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDigits (int value) {
	checkWidget ();
	if (value < 0) error (SWT.ERROR_INVALID_ARGUMENT);
	if (value == this.digits) return;
	this.digits = value;
	int pos = (int)OS.SendMessage (hwndUpDown, OS.UDM_GETPOS32, 0, 0);
	setSelection (pos, false, true, false);
}

@Override
void setForegroundPixel (int pixel) {
	super.setForegroundPixel (pixel);
	OS.InvalidateRect (hwndText, null, true);
}

/**
 * Sets the amount that the receiver's value will be
 * modified by when the up/down arrows are pressed to
 * the argument, which must be at least one.
 *
 * @param value the new increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setIncrement (int value) {
	checkWidget ();
	if (value < 1) return;
	long hHeap = OS.GetProcessHeap ();
	int count = (int)OS.SendMessage (hwndUpDown, OS.UDM_GETACCEL, 0, (UDACCEL)null);
	long udaccels = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, UDACCEL.sizeof * count);
	OS.SendMessage (hwndUpDown, OS.UDM_GETACCEL, count, udaccels);
	int first = -1;
	UDACCEL udaccel = new UDACCEL ();
	for (int i = 0; i < count; i++) {
		long offset = udaccels + (i * UDACCEL.sizeof);
		OS.MoveMemory (udaccel, offset, UDACCEL.sizeof);
		if (first == -1) first = udaccel.nInc;
		udaccel.nInc  = udaccel.nInc / first * value;
		OS.MoveMemory (offset, udaccel, UDACCEL.sizeof);
	}
	OS.SendMessage (hwndUpDown, OS.UDM_SETACCEL, count, udaccels);
	OS.HeapFree (hHeap, 0, udaccels);
}

/**
 * Sets the maximum value that the receiver will allow.  This new
 * value will be ignored if it is less than the receiver's current
 * minimum value.  If the new maximum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new maximum, which must be greater than or equal to the current minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMaximum (int value) {
	checkWidget ();
	int [] min = new int [1];
	OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, min, null);
	if (value < min [0]) return;
	int pos = (int)OS.SendMessage (hwndUpDown, OS.UDM_GETPOS32, 0, 0);
	OS.SendMessage (hwndUpDown , OS.UDM_SETRANGE32, min [0], value);
	if (pos > value) setSelection (value, true, true, false);
}

/**
 * Sets the minimum value that the receiver will allow.  This new
 * value will be ignored if it is greater than the receiver's
 * current maximum value.  If the new minimum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new minimum, which must be less than or equal to the current maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinimum (int value) {
	checkWidget ();
	int [] max = new int [1];
	OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, null, max);
	if (value > max [0]) return;
	int pos = (int)OS.SendMessage (hwndUpDown, OS.UDM_GETPOS32, 0, 0);
	OS.SendMessage (hwndUpDown , OS.UDM_SETRANGE32, value, max [0]);
	if (pos < value) setSelection (value, true, true, false);
}

/**
 * Sets the amount that the receiver's position will be
 * modified by when the page up/down keys are pressed
 * to the argument, which must be at least one.
 *
 * @param value the page increment (must be greater than zero)
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
	setSelection (value, true, true, false);
}

void setSelection (int value, boolean setPos, boolean setText, boolean notify) {
	if (setPos) {
		OS.SendMessage (hwndUpDown, OS.UDM_SETPOS32, 0, value);
	}
	if (setText) {
		String string;
		if (digits == 0) {
			string = String.valueOf (value);
		} else {
			string = String.valueOf (Math.abs (value));
			String decimalSeparator = getDecimalSeparator ();
			int index = string.length () - digits;
			StringBuilder buffer = new StringBuilder ();
			if (value < 0) buffer.append ("-");
			if (index > 0) {
				buffer.append (string.substring (0, index));
				buffer.append (decimalSeparator);
				buffer.append (string.substring (index));
			} else {
				buffer.append ("0");
				buffer.append (decimalSeparator);
				while (index++ < 0) buffer.append ("0");
				buffer.append (string);
			}
			string = buffer.toString ();
		}
		if (hooks (SWT.Verify) || filters (SWT.Verify)) {
			int length = OS.GetWindowTextLength (hwndText);
			string = verifyText (string, 0, length, null);
			if (string == null) return;
		}
		TCHAR buffer = new TCHAR (getCodePage (), string, true);
		OS.SetWindowText (hwndText, buffer);
		OS.SendMessage (hwndText, OS.EM_SETSEL, 0, -1);
		OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, hwndText, OS.OBJID_CLIENT, 0);
	}
	if (notify) sendSelectionEvent (SWT.Selection);
}

/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 * <p>
 * To reset this value to the default, use <code>setTextLimit(Spinner.LIMIT)</code>.
 * Specifying a limit value larger than <code>Spinner.LIMIT</code> sets the
 * receiver's limit to <code>Spinner.LIMIT</code>.
 * </p>
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #LIMIT
 *
 * @since 3.4
 */
public void setTextLimit (int limit) {
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	OS.SendMessage (hwndText, OS.EM_SETLIMITTEXT, limit, 0);
}

@Override
void setToolTipText (Shell shell, String string) {
	shell.setToolTipText (hwndText, string);
	shell.setToolTipText (hwndUpDown, string);
}

/**
 * Sets the receiver's selection, minimum value, maximum
 * value, digits, increment and page increment all at once.
 * <p>
 * Note: This is similar to setting the values individually
 * using the appropriate methods, but may be implemented in a
 * more efficient fashion on some platforms.
 * </p>
 *
 * @param selection the new selection value
 * @param minimum the new minimum value
 * @param maximum the new maximum value
 * @param digits the new digits value
 * @param increment the new increment value
 * @param pageIncrement the new pageIncrement value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setValues (int selection, int minimum, int maximum, int digits, int increment, int pageIncrement) {
	checkWidget ();
	if (maximum < minimum) return;
	if (digits < 0) return;
	if (increment < 1) return;
	if (pageIncrement < 1) return;
	selection = Math.min (Math.max (minimum, selection), maximum);
	setIncrement (increment);
	this.pageIncrement = pageIncrement;
	this.digits = digits;
	OS.SendMessage (hwndUpDown , OS.UDM_SETRANGE32, minimum, maximum);
	setSelection (selection, true, true, false);
}

@Override
void subclass () {
	super.subclass ();
	long newProc = display.windowProc;
	OS.SetWindowLongPtr (hwndText, OS.GWLP_WNDPROC, newProc);
	OS.SetWindowLongPtr (hwndUpDown, OS.GWLP_WNDPROC, newProc);
}

@Override
void unsubclass () {
	super.unsubclass ();
	OS.SetWindowLongPtr (hwndText, OS.GWLP_WNDPROC, EditProc);
	OS.SetWindowLongPtr (hwndUpDown, OS.GWLP_WNDPROC, UpDownProc);
}

@Override
void updateOrientation () {
	super.updateOrientation ();
	int bits  = OS.GetWindowLong (hwndText, OS.GWL_EXSTYLE);
	int bits1  = OS.GetWindowLong (hwndText, OS.GWL_STYLE);
	if ((style & SWT.RIGHT_TO_LEFT) != 0){
		bits |= OS.WS_EX_RIGHT;
		bits1 |= OS.ES_RIGHT;
	}
	else{
		bits &= ~OS.WS_EX_RIGHT;
		bits1 &= ~OS.ES_RIGHT;
	}
	OS.SetWindowLong (hwndText, OS.GWL_STYLE, bits1);
	OS.SetWindowLong (hwndText, OS.GWL_EXSTYLE, bits);
	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	int width = rect.right - rect.left, height = rect.bottom - rect.top;
	OS.SetWindowPos (handle, 0, 0, 0, width - 1, height - 1, OS.SWP_NOMOVE | OS.SWP_NOZORDER);
	OS.SetWindowPos (handle, 0, 0, 0, width, height, OS.SWP_NOMOVE | OS.SWP_NOZORDER);
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
	if (digits > 0) {
		String decimalSeparator = getDecimalSeparator ();
		index = string.indexOf (decimalSeparator);
		if (index != -1) {
			string = string.substring (0, index) + string.substring (index + 1);
		}
		index = 0;
	}
	if (string.length() > 0) {
		int [] min = new int [1];
		OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, min, null);
		if (min [0] < 0 && string.charAt (0) == '-') index++;
	}
	while (index < string.length ()) {
		if (!Character.isDigit (string.charAt (index))) break;
		index++;
	}
	event.doit = index == string.length ();
	sendEvent (SWT.Verify, event);
	if (!event.doit || isDisposed ()) return null;
	return event.text;
}

@Override
long windowProc (long hwnd, int msg, long wParam, long lParam) {
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
			case OS.WM_CAPTURECHANGED:	result = wmCaptureChanged (hwnd, wParam, lParam); break;
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
			case OS.WM_XBUTTONDBLCLK:	result = wmXButtonDblClk (hwnd, wParam, lParam); break;
			case OS.WM_XBUTTONDOWN:		result = wmXButtonDown (hwnd, wParam, lParam); break;
			case OS.WM_XBUTTONUP:		result = wmXButtonUp (hwnd, wParam, lParam); break;

			/* Focus Messages */
			case OS.WM_SETFOCUS:		result = wmSetFocus (hwnd, wParam, lParam); break;
			case OS.WM_KILLFOCUS:		result = wmKillFocus (hwnd, wParam, lParam); break;

			/* Paint messages */
			case OS.WM_PAINT:			result = wmPaint (hwnd, wParam, lParam); break;
			case OS.WM_PRINT:			result = wmPrint (hwnd, wParam, lParam); break;

			/* Menu messages */
			case OS.WM_CONTEXTMENU:		result = wmContextMenu (hwnd, wParam, lParam); break;

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

@Override
LRESULT WM_ERASEBKGND (long wParam, long lParam) {
	super.WM_ERASEBKGND (wParam, lParam);
	drawBackground (wParam);
	return LRESULT.ONE;
}

@Override
LRESULT WM_KILLFOCUS (long wParam, long lParam) {
	return null;
}

@Override
LRESULT WM_SETFOCUS (long wParam, long lParam) {
	OS.SetFocus (hwndText);
	OS.SendMessage (hwndText, OS.EM_SETSEL, 0, -1);
	return null;
}

@Override
LRESULT WM_SETFONT (long wParam, long lParam) {
	LRESULT result = super.WM_SETFONT (wParam, lParam);
	if (result != null) return result;
	OS.SendMessage (hwndText, OS.WM_SETFONT, wParam, lParam);
	return result;
}

@Override
LRESULT WM_SIZE (long wParam, long lParam) {
	LRESULT result = super.WM_SIZE (wParam, lParam);
	if (isDisposed ()) return result;
	int width = OS.LOWORD (lParam), height = OS.HIWORD (lParam);
	int upDownWidth = OS.GetSystemMetrics (OS.SM_CXVSCROLL) - 1;
	int textWidth = width - upDownWidth;

	/*
	 * For consistency, make text's vertical position the same as in
	 * Text control. The difference only occurs when Spinner uses
	 * WS_BORDER while Text uses WS_EX_CLIENTEDGE.
	 */
	int borderAdjustment = 0;
	if (((style & SWT.BORDER) != 0) && !display.useWsBorderText) {
		borderAdjustment = OS.GetSystemMetrics (OS.SM_CYEDGE) - OS.GetSystemMetrics (OS.SM_CYBORDER);
		/* There is an unexplained 1px additional offset in Windows */
		borderAdjustment++;
	}

	int flags = OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
	OS.SetWindowPos (hwndText, 0, 0, borderAdjustment, textWidth, height - borderAdjustment, flags);
	OS.SetWindowPos (hwndUpDown, 0, textWidth, 0, upDownWidth, height, flags);
	return result;
}

@Override
LRESULT wmIMEChar(long hwnd, long wParam, long lParam) {

	/* Process a DBCS character */
	Display display = this.display;
	display.lastKey = 0;
	display.lastAscii = (int)wParam;
	display.lastVirtual = display.lastDead = false;
	if (!sendKeyEvent (SWT.KeyDown, OS.WM_IME_CHAR, wParam, lParam)) {
		return LRESULT.ZERO;
	}

	/*
	* Feature in Windows.  The Windows text widget uses
	* two 2 WM_CHAR's to process a DBCS key instead of
	* using WM_IME_CHAR.  The fix is to allow the text
	* widget to get the WM_CHAR's but ignore sending
	* them to the application.
	*/
	ignoreCharacter = true;
	long result = callWindowProc (hwnd, OS.WM_IME_CHAR, wParam, lParam);
	MSG msg = new MSG ();
	int flags = OS.PM_REMOVE | OS.PM_NOYIELD | OS.PM_QS_INPUT | OS.PM_QS_POSTMESSAGE;
	while (OS.PeekMessage (msg, hwnd, OS.WM_CHAR, OS.WM_CHAR, flags)) {
		OS.TranslateMessage (msg);
		OS.DispatchMessage (msg);
	}
	ignoreCharacter = false;

	sendKeyEvent (SWT.KeyUp, OS.WM_IME_CHAR, wParam, lParam);
	// widget could be disposed at this point
	display.lastKey = display.lastAscii = 0;
	return new LRESULT (result);
}

@Override
LRESULT wmChar (long hwnd, long wParam, long lParam) {
	if (ignoreCharacter) return null;
	LRESULT result = super.wmChar (hwnd, wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  For some reason, when the
	* widget is a single line text widget, when the
	* user presses tab, return or escape, Windows beeps.
	* The fix is to look for these keys and not call
	* the window proc.
	*/
	switch ((int)wParam) {
		case SWT.CR:
			sendSelectionEvent (SWT.DefaultSelection);
			// FALL THROUGH
		case SWT.TAB:
		case SWT.ESC: return LRESULT.ZERO;
	}
	return result;
}

LRESULT wmClipboard (long hwndText, int msg, long wParam, long lParam) {
	if ((style & SWT.READ_ONLY) != 0) return null;
//	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return null;
	boolean call = false;
	int [] start = new int [1], end = new int [1];
	String newText = null;
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
				OS.CallWindowProc (EditProc, hwndText, msg, wParam, lParam);
				int length = OS.GetWindowTextLength (hwndText);
				int [] newStart = new int [1], newEnd = new int [1];
				OS.SendMessage (hwndText, OS.EM_GETSEL, newStart, newEnd);
				if (length != 0 && newStart [0] != newEnd [0]) {
					char [] buffer = new char [length + 1];
					OS.GetWindowText (hwndText, buffer, length + 1);
					newText = new String (buffer, newStart [0], newEnd [0] - newStart [0]);
				} else {
					newText = "";
				}
				OS.CallWindowProc (EditProc, hwndText, msg, wParam, lParam);
				OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
				ignoreModify = false;
			}
			break;
	}
	if (newText != null) {
		String oldText = newText;
		newText = verifyText (newText, start [0], end [0], null);
		if (newText == null) return LRESULT.ZERO;
		if (!newText.equals (oldText)) {
			if (call) {
				OS.CallWindowProc (EditProc, hwndText, msg, wParam, lParam);
			}
			TCHAR buffer = new TCHAR (getCodePage (), newText, true);
			if (msg == OS.WM_SETTEXT) {
				long hHeap = OS.GetProcessHeap ();
				int byteCount = buffer.length () * TCHAR.sizeof;
				long pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
				OS.MoveMemory (pszText, buffer, byteCount);
				long code = OS.CallWindowProc (EditProc, hwndText, msg, wParam, pszText);
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

@Override
LRESULT wmCommandChild (long wParam, long lParam) {
	int code = OS.HIWORD (wParam);
	switch (code) {
		case OS.EN_CHANGE:
			if (ignoreModify) break;
			boolean [] parseFail = new boolean [1];
			int value = getSelectionText (parseFail);
			if (!parseFail [0]) {
				int pos = (int)OS.SendMessage (hwndUpDown, OS.UDM_GETPOS32, 0, 0);
				if (pos != value) setSelection (value, true, false, true);
			}
			sendEvent (SWT.Modify);
			if (isDisposed ()) return LRESULT.ZERO;
			break;
	}
	return super.wmCommandChild (wParam, lParam);
}

@Override
LRESULT wmKeyDown (long hwnd, long wParam, long lParam) {
	if (ignoreCharacter) return null;
	LRESULT result = super.wmKeyDown (hwnd, wParam, lParam);
	if (result != null) return result;

	/* Increment the value */
	UDACCEL udaccel = new UDACCEL ();
	OS.SendMessage (hwndUpDown, OS.UDM_GETACCEL, 1, udaccel);
	int delta = 0;
	switch ((int)wParam) {
		case OS.VK_UP: delta = udaccel.nInc; break;
		case OS.VK_DOWN: delta = -udaccel.nInc; break;
		case OS.VK_PRIOR: delta = pageIncrement; break;
		case OS.VK_NEXT: delta = -pageIncrement; break;
	}
	if (delta != 0) {
		boolean [] parseFail = new boolean [1];
		int value = getSelectionText (parseFail);
		if (parseFail [0]) {
			value = (int)OS.SendMessage (hwndUpDown, OS.UDM_GETPOS32, 0, 0);
		}
		int newValue = value + delta;
		int [] max = new int [1], min = new int [1];
		OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, min, max);
		if ((style & SWT.WRAP) != 0) {
			if (newValue < min [0]) newValue = max [0];
			if (newValue > max [0]) newValue = min [0];
		}
		newValue = Math.min (Math.max (min [0], newValue), max [0]);
		if (value != newValue) setSelection (newValue, true, true, true);
	}

	/*  Stop the edit control from moving the caret */
	switch ((int)wParam) {
		case OS.VK_UP:
		case OS.VK_DOWN:
			return LRESULT.ZERO;
	}
	return result;
}

@Override
LRESULT wmKillFocus (long hwnd, long wParam, long lParam) {
	boolean [] parseFail = new boolean [1];
	int value = getSelectionText (parseFail);
	if (parseFail [0]) {
		value = (int)OS.SendMessage (hwndUpDown, OS.UDM_GETPOS32, 0, 0);
		setSelection (value, false, true, false);
	}
	return super.wmKillFocus (hwnd, wParam, lParam);
}

@Override
LRESULT wmNotifyChild (NMHDR hdr, long wParam, long lParam) {
	switch (hdr.code) {
		case OS.UDN_DELTAPOS:
			NMUPDOWN lpnmud = new NMUPDOWN ();
			OS.MoveMemory (lpnmud, lParam, NMUPDOWN.sizeof);
			int value = lpnmud.iPos + lpnmud.iDelta;
			int [] max = new int [1], min = new int [1];
			OS.SendMessage (hwndUpDown , OS.UDM_GETRANGE32, min, max);
			if ((style & SWT.WRAP) != 0) {
				if (value < min [0]) value = max [0];
				if (value > max [0]) value = min [0];
			}
			/*
			* The SWT.Modify event is sent after the widget has been
			* updated with the new state.  Rather than allowing
			* the default updown window proc to set the value
			* when the user clicks on the updown control, set
			* the value explicitly and stop the window proc
			* from running.
			*/
			value = Math.min (Math.max (min [0], value), max [0]);
			if (value != lpnmud.iPos) {
				setSelection (value, true, true, true);
			}
			return LRESULT.ONE;
	}
	return super.wmNotifyChild (hdr, wParam, lParam);
}

@Override
LRESULT wmScrollChild (long wParam, long lParam) {
	int code = OS.LOWORD (wParam);
	switch (code) {
		case OS.SB_THUMBPOSITION:
			sendSelectionEvent (SWT.Selection);
			break;
	}
	return super.wmScrollChild (wParam, lParam);
}

}

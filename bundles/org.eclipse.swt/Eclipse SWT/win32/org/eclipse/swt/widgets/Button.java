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
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a selectable user interface object that
 * issues notification when pressed and released. 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ARROW, CHECK, PUSH, RADIO, TOGGLE, FLAT</dd>
 * <dd>UP, DOWN, LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ARROW, CHECK, PUSH, RADIO, and TOGGLE 
 * may be specified.
 * </p><p>
 * Note: Only one of the styles LEFT, RIGHT, and CENTER may be specified.
 * </p><p>
 * Note: Only one of the styles UP, DOWN, LEFT, and RIGHT may be specified
 * when the ARROW style is specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */

public class Button extends Control {
	Image image;
	static final int ButtonProc;
	static final TCHAR ButtonClass = new TCHAR (0,"BUTTON", true);
	static final int CheckWidth, CheckHeight;
	static {
		int hBitmap = OS.LoadBitmap (0, OS.OBM_CHECKBOXES);
		if (hBitmap == 0) {
			CheckWidth = OS.GetSystemMetrics (OS.IsWinCE ? OS.SM_CXSMICON : OS.SM_CXVSCROLL);
			CheckHeight = OS.GetSystemMetrics (OS.IsWinCE ? OS.SM_CYSMICON : OS.SM_CYVSCROLL);
		} else {
			BITMAP bitmap = new BITMAP ();
			OS.GetObject (hBitmap, BITMAP.sizeof, bitmap);
			OS.DeleteObject (hBitmap);
			CheckWidth = bitmap.bmWidth / 4;
			CheckHeight =  bitmap.bmHeight / 3;
		}
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, ButtonClass, lpWndClass);
		ButtonProc = lpWndClass.lpfnWndProc;
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
 * @see SWT#ARROW
 * @see SWT#CHECK
 * @see SWT#PUSH
 * @see SWT#RADIO
 * @see SWT#TOGGLE
 * @see SWT#FLAT
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Button (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the control is selected.
 * <code>widgetDefaultSelected</code> is not called.
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

int callWindowProc (int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (ButtonProc, handle, msg, wParam, lParam);
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, 0);
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		return checkBits (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
	}
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		return checkBits (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
	}
	if ((style & SWT.ARROW) != 0) {
		style |= SWT.NO_FOCUS;
		return checkBits (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
	}
	return style;
}

void click () {
	/*
	* Note: BM_CLICK sends WM_LBUTTONDOWN and WM_LBUTTONUP.
	*/
	OS.SendMessage (handle, OS.BM_CLICK, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int border = getBorderWidth ();
	int width = 0, height = 0;
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN)) != 0) {
			width += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
			height += OS.GetSystemMetrics (OS.SM_CYVSCROLL);
		} else {
			width += OS.GetSystemMetrics (OS.SM_CXHSCROLL);
			height += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
		}
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		width += border * 2; height += border * 2;
		return new Point (width, height);
	}
	int extra = 0;
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & (OS.BS_BITMAP | OS.BS_ICON)) == 0) {
		int oldFont = 0;
		int hDC = OS.GetDC (handle);
		int newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
		OS.GetTextMetrics (hDC, lptm);
		int length = OS.GetWindowTextLength (handle);
		if (length == 0) {
			height += lptm.tmHeight;
		} else {
			extra = Math.max (8, lptm.tmAveCharWidth);
			TCHAR buffer = new TCHAR (getCodePage (), length + 1);
			OS.GetWindowText (handle, buffer, length + 1);
			RECT rect = new RECT ();
			int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE;
			OS.DrawText (hDC, buffer, length, rect, flags);
			width += rect.right - rect.left;
			height += rect.bottom - rect.top;
		}
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
	} else {
		if (image != null) {
			Rectangle rect = image.getBounds ();
			width = rect.width;
			height = rect.height;
			extra = 8;
		}
	}
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		width += CheckWidth + extra;
		height = Math.max (height, CheckHeight + 3);
	}
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		width += 12;  height += 10;
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	width += border * 2; height += border * 2;
	return new Point (width, height);
}

int defaultBackground () {
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		return OS.GetSysColor (OS.COLOR_BTNFACE);
	}
	return super.defaultBackground ();
}

int defaultForeground () {
	return OS.GetSysColor (OS.COLOR_BTNTEXT);
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in 
 * which case, the alignment will indicate the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>, 
 * <code>UP</code> or <code>DOWN</code>).
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) {
		if ((style & SWT.UP) != 0) return SWT.UP;
		if ((style & SWT.DOWN) != 0) return SWT.DOWN;
		if ((style & SWT.LEFT) != 0) return SWT.LEFT;
		if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
		return SWT.UP;
	}
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

boolean getDefault () {
	if ((style & SWT.PUSH) == 0) return false;
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	return (bits & OS.BS_DEFPUSHBUTTON) != 0;
}

/**
 * Returns the receiver's image if it has one, or null
 * if it does not.
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

String getNameText () {
	return getText ();
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed in. If the receiver is of any other type,
 * this method returns false.
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	int state = OS.SendMessage (handle, OS.BM_GETCHECK, 0, 0);
	return (state & OS.BST_CHECKED) != 0;
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set or if the receiver is
 * an <code>ARROW</code> button.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) return "";
	int length = OS.GetWindowTextLength (handle);
	if (length == 0) return "";
	TCHAR buffer = new TCHAR (getCodePage (), length + 1);
	OS.GetWindowText (handle, buffer, length + 1);
	return buffer.toString (0, length);
}

boolean isTabItem () {
	//TEMPORARY CODE
	//if ((style & SWT.PUSH) != 0) return true;
	return super.isTabItem ();
}

boolean mnemonicHit (char ch) {
	if (!setFocus ()) return false;
	/*
	* Feature in Windows.  When a radio button gets focus, 
	* it selects the button in WM_SETFOCUS.  Therefore, it
	* is not necessary to click the button or send events
	* because this has already happened in WM_SETFOCUS.
	*/
	if ((style & SWT.RADIO) == 0) click ();
	return true;
}

boolean mnemonicMatch (char key) {
	char mnemonic = findMnemonic (getText ());
	if (mnemonic == '\0') return false;
	return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
}

void releaseWidget () {
	super.releaseWidget ();
	image = null;
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

void selectRadio () {
	/*
	* This code is intentionally commented.  When two groups
	* of radio buttons with the same parent are separated by
	* another control, the correct behavior should be that
	* the two groups act independently.  This is consistent
	* with radio tool and menu items.  The commented code
	* implements this behavior.
	*/
//	int index = 0;
//	Control [] children = parent._getChildren ();
//	while (index < children.length && children [index] != this) index++;
//	int i = index - 1;
//	while (i >= 0 && children [i].setRadioSelection (false)) --i;
//	int j = index + 1;
//	while (j < children.length && children [j].setRadioSelection (false)) j++;
//	setSelection (true);
	Control [] children = parent._getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (this != child) child.setRadioSelection (false);
	}
	setSelection (true);
}

/**
 * Controls how text, images and arrows will be displayed
 * in the receiver. The argument should be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in 
 * which case, the argument indicates the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>, 
 * <code>UP</code> or <code>DOWN</code>).
 *
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return; 
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		OS.InvalidateRect (handle, null, true);
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	bits &= ~(OS.BS_LEFT | OS.BS_CENTER | OS.BS_RIGHT);
	if ((style & SWT.LEFT) != 0) bits |= OS.BS_LEFT;
	if ((style & SWT.CENTER) != 0) bits |= OS.BS_CENTER;
	if ((style & SWT.RIGHT) != 0) bits |= OS.BS_RIGHT;
	OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
	OS.InvalidateRect (handle, null, true);
}

void setDefault (boolean value) {
	if ((style & SWT.PUSH) == 0) return;
	int hwndShell = menuShell ().handle;
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if (value) {
		bits |= OS.BS_DEFPUSHBUTTON;
		OS.SendMessage (hwndShell, OS.DM_SETDEFID, handle, 0);
	} else {
		bits &= ~OS.BS_DEFPUSHBUTTON;
		OS.SendMessage (hwndShell, OS.DM_SETDEFID, 0, 0);
	}
	OS.SendMessage (handle, OS.BM_SETSTYLE, bits, 1);
}

/**
 * Sets the receiver's image to the argument, which may be
 * null indicating that no image should be displayed.
 *
 * @param image the image to display on the receiver (may be null)
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
	int hImage = 0, imageBits = 0, fImageType = 0;
	if (image != null) {
		if (image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		hImage = image.handle;
		switch (image.type) {
			case SWT.BITMAP:
				imageBits = OS.BS_BITMAP;
				fImageType = OS.IMAGE_BITMAP;
				break;
			case SWT.ICON:
				imageBits = OS.BS_ICON;
				fImageType = OS.IMAGE_ICON;
				break;
			default:
				return;
		}
	}
	this.image = image;
	int newBits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	int oldBits = newBits;
	newBits &= ~(OS.BS_BITMAP | OS.BS_ICON);
	newBits |= imageBits;
	if (newBits != oldBits) {
		OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
	}
	OS.SendMessage (handle, OS.BM_SETIMAGE, fImageType, hImage);
}

boolean setRadioFocus () {
	if ((style & SWT.RADIO) == 0 || !getSelection ()) return false;
	return setFocus ();
}

boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		postEvent (SWT.Selection);
	}
	return true;
}

boolean setSavedFocus () {
	/*
	* Feature in Windows.  When a radio button gets focus, 
	* it selects the button in WM_SETFOCUS.  If the previous
	* saved focus widget was a radio button, allowing the shell
	* to automatically restore the focus to the previous radio
	* button will unexpectedly check that button.  The fix is
	* to disallow focus to be restored to radio button that is
	* not selected.
	*/
	if ((style & SWT.RADIO) != 0 && !getSelection ()) return false;
	return super.setSavedFocus ();
}

/**
 * Sets the selection state of the receiver, if it is of type <code>CHECK</code>, 
 * <code>RADIO</code>, or <code>TOGGLE</code>.
 *
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed in.
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	int flags = selected ? OS.BST_CHECKED : OS.BST_UNCHECKED;
	
	/*
	* Feature in Windows. When BM_SETCHECK is used
	* to set the checked state of a radio or check
	* button, it sets the WM_TABSTOP style.  This
	* is undocumented and unwanted.  The fix is
	* to save and restore the window style bits.
	*/
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	OS.SendMessage (handle, OS.BM_SETCHECK, flags, 0);
	OS.SetWindowLong (handle, OS.GWL_STYLE, bits);     
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the button label.  The label may include
 * the mnemonic character but must not contain line delimiters.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp' can be
 * escaped by doubling it in the string, causing a single
 *'&amp' to be displayed.
 * </p>
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
	if ((style & SWT.ARROW) != 0) return;
	int newBits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	int oldBits = newBits;
	newBits &= ~(OS.BS_BITMAP | OS.BS_ICON);
	if (newBits != oldBits) {
		OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
	}
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	OS.SetWindowText (handle, buffer);
}

int widgetStyle () {
	int bits = super.widgetStyle ();
	if ((style & SWT.FLAT) != 0) bits |= OS.BS_FLAT;
	if ((style & SWT.ARROW) != 0) return bits | OS.BS_OWNERDRAW;
	if ((style & SWT.LEFT) != 0) bits |= OS.BS_LEFT;
	if ((style & SWT.CENTER) != 0) bits |= OS.BS_CENTER;
	if ((style & SWT.RIGHT) != 0) bits |= OS.BS_RIGHT;
	if ((style & SWT.PUSH) != 0) return bits | OS.BS_PUSHBUTTON | OS.WS_TABSTOP;
	if ((style & SWT.CHECK) != 0) return bits | OS.BS_CHECKBOX | OS.WS_TABSTOP;
	if ((style & SWT.RADIO) != 0) return bits | OS.BS_RADIOBUTTON;
	if ((style & SWT.TOGGLE) != 0) return bits | OS.BS_PUSHLIKE | OS.BS_CHECKBOX;
	return bits | OS.BS_PUSHBUTTON | OS.WS_TABSTOP;
}

TCHAR windowClass () {
	return ButtonClass;
}

int windowProc () {
	return ButtonProc;
}

LRESULT WM_GETDLGCODE (int wParam, int lParam) {
	LRESULT result = super.WM_GETDLGCODE (wParam, lParam);
	if (result != null) return result;
	if ((style & SWT.ARROW) != 0) {
		return new LRESULT (OS.DLGC_STATIC);
	}
	return result;
}

LRESULT WM_KILLFOCUS (int wParam, int lParam) {
	LRESULT result = super.WM_KILLFOCUS (wParam, lParam);
	if ((style & SWT.PUSH) != 0 && getDefault ()) {
		menuShell ().setDefaultButton (null, false);
	}
	return result;
}

LRESULT WM_SETFOCUS (int wParam, int lParam) {
	/*
	* Feature in Windows. When Windows sets focus to
	* a radio button, it sets the WM_TABSTOP style.
	* This is undocumented and unwanted.  The fix is
	* to save and restore the window style bits.
	*/
	int bits = 0;
	if ((style & SWT.RADIO) != 0) {
		bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	}
	LRESULT result = super.WM_SETFOCUS (wParam, lParam);
	if ((style & SWT.RADIO) != 0) {
		OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
	}
	if ((style & SWT.PUSH) != 0) {
		menuShell ().setDefaultButton (this, false);
	}
	return result;
}

LRESULT wmCommandChild (int wParam, int lParam) {
	int code = wParam >> 16;
	switch (code) {
		case OS.BN_CLICKED:
		case OS.BN_DOUBLECLICKED:
			if ((style & (SWT.CHECK | SWT.TOGGLE)) != 0) {
				setSelection (!getSelection ());
			} else {
				if ((style & SWT.RADIO) != 0) {
					if ((parent.getStyle () & SWT.NO_RADIO_GROUP) != 0) {
						setSelection (!getSelection ());
					} else {
						selectRadio ();
					}
				}
			}
			postEvent (SWT.Selection);
	}
	return super.wmCommandChild (wParam, lParam);
}

LRESULT wmDrawChild (int wParam, int lParam) {
	if ((style & SWT.ARROW) == 0) return super.wmDrawChild (wParam, lParam);
	DRAWITEMSTRUCT struct = new DRAWITEMSTRUCT ();
	OS.MoveMemory (struct, lParam, DRAWITEMSTRUCT.sizeof);
	int uState = OS.DFCS_SCROLLLEFT;
	switch (style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) {
		case SWT.UP: uState = OS.DFCS_SCROLLUP; break;
		case SWT.DOWN: uState = OS.DFCS_SCROLLDOWN; break;
		case SWT.LEFT: uState = OS.DFCS_SCROLLLEFT; break;
		case SWT.RIGHT: uState = OS.DFCS_SCROLLRIGHT; break;
	}
	if (!getEnabled ()) uState |= OS.DFCS_INACTIVE;
	if ((style & SWT.FLAT) == SWT.FLAT) uState |= OS.DFCS_FLAT;
	if ((struct.itemState & OS.ODS_SELECTED) != 0) uState |= OS.DFCS_PUSHED;
	RECT rect = new RECT ();
	OS.SetRect (rect, struct.left, struct.top, struct.right, struct.bottom);
	OS.DrawFrameControl (struct.hDC, rect, OS.DFC_SCROLL, uState);
	return null;
}

}

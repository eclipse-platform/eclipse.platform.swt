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


import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.swing.*;

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
	ImageList imageList;
	boolean ignoreMouse;
  String text;
//	static final int ButtonProc;
//	static final TCHAR ButtonClass = new TCHAR (0,"BUTTON", true);
//	static final char [] SCROLLBAR = new char [] {'S', 'C', 'R', 'O', 'L', 'L', 'B', 'A', 'R', 0};
//	static final int CheckWidth, CheckHeight;
//	static {
//		int hBitmap = OS.LoadBitmap (0, OS.OBM_CHECKBOXES);
//		if (hBitmap == 0) {
//			CheckWidth = OS.GetSystemMetrics (OS.IsWinCE ? OS.SM_CXSMICON : OS.SM_CXVSCROLL);
//			CheckHeight = OS.GetSystemMetrics (OS.IsWinCE ? OS.SM_CYSMICON : OS.SM_CYVSCROLL);
//		} else {
//			BITMAP bitmap = new BITMAP ();
//			OS.GetObject (hBitmap, BITMAP.sizeof, bitmap);
//			OS.DeleteObject (hBitmap);
//			CheckWidth = bitmap.bmWidth / 4;
//			CheckHeight =  bitmap.bmHeight / 3;
//		}
//		WNDCLASS lpWndClass = new WNDCLASS ();
//		OS.GetClassInfo (0, ButtonClass, lpWndClass);
//		ButtonProc = lpWndClass.lpfnWndProc;
//	}

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

//int callWindowProc (int hwnd, int msg, int wParam, int lParam) {
//	if (handle == 0) return 0;
//	return OS.CallWindowProc (ButtonProc, hwnd, msg, wParam, lParam);
//}

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
  ((CButton)handle).doClick();
//	/*
//	* Feature in Windows.  BM_CLICK sends a fake WM_LBUTTONDOWN and
//	* WM_LBUTTONUP in order to click the button.  This causes the
//	* application to get unexpected mouse events.  The fix is to
//	* ignore mouse events when they are caused by BM_CLICK.
//	*/
//	ignoreMouse = true;
//	OS.SendMessage (handle, OS.BM_CLICK, 0, 0);
//	ignoreMouse = false;
}

//public Point computeSize (int wHint, int hHint, boolean changed) {
//	checkWidget ();
//	int border = getBorderWidth ();
//	int width = 0, height = 0;
//	if ((style & SWT.ARROW) != 0) {
//		if ((style & (SWT.UP | SWT.DOWN)) != 0) {
//			width += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
//			height += OS.GetSystemMetrics (OS.SM_CYVSCROLL);
//		} else {
//			width += OS.GetSystemMetrics (OS.SM_CXHSCROLL);
//			height += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
//		}
//		if (wHint != SWT.DEFAULT) width = wHint;
//		if (hHint != SWT.DEFAULT) height = hHint;
//		width += border * 2; height += border * 2;
//		return new Point (width, height);
//	}
//	int extra = 0;
//	boolean hasImage;
//	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
//		BUTTON_IMAGELIST buttonImageList = new BUTTON_IMAGELIST();
//		OS.SendMessage (handle, OS.BCM_GETIMAGELIST, 0, buttonImageList);
//		hasImage = buttonImageList.himl != 0;
//	} else {
//		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//		hasImage = (bits & (OS.BS_BITMAP | OS.BS_ICON)) != 0;
//	}
//	if (!hasImage) {
//		int oldFont = 0;
//		int hDC = OS.GetDC (handle);
//		int newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
//		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
//		TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
//		OS.GetTextMetrics (hDC, lptm);
//		int length = OS.GetWindowTextLength (handle);
//		if (length == 0) {
//			height += lptm.tmHeight;
//		} else {
//			extra = Math.max (8, lptm.tmAveCharWidth);
//			TCHAR buffer = new TCHAR (getCodePage (), length + 1);
//			OS.GetWindowText (handle, buffer, length + 1);
//			RECT rect = new RECT ();
//			int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE;
//			OS.DrawText (hDC, buffer, length, rect, flags);
//			width += rect.right - rect.left;
//			height += rect.bottom - rect.top;
//		}
//		if (newFont != 0) OS.SelectObject (hDC, oldFont);
//		OS.ReleaseDC (handle, hDC);
//	} else {
//		if (image != null) {
//			Rectangle rect = image.getBounds ();
//			width = rect.width;
//			height = rect.height;
//			extra = 8;
//		}
//	}
//	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
//		width += CheckWidth + extra;
//		height = Math.max (height, CheckHeight + 3);
//	}
//	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
//		width += 12;  height += 10;
//	}
//	if (wHint != SWT.DEFAULT) width = wHint;
//	if (hHint != SWT.DEFAULT) height = hHint;
//	width += border * 2; height += border * 2;
//	return new Point (width, height);
//}

@Override
public Point computeSize(int wHint, int hHint, boolean changed) {
  Point size = super.computeSize(wHint, hHint, changed);
  if ((style & SWT.ARROW) != 0) {
    if(wHint == SWT.DEFAULT) {
      size.x = size.y;
    } else if(hHint == SWT.DEFAULT) {
      size.y = size.x;
    } else {
      size.x = size.y;
    }
  }
  return size;
}

@Override
Container createHandle () {
  return (Container)CButton.Factory.newInstance(this, style);
}

//int defaultBackground () {
//	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
//		return OS.GetSysColor (OS.COLOR_BTNFACE);
//	}
//	return super.defaultBackground ();
//}

//int defaultForeground () {
//	return OS.GetSysColor (OS.COLOR_BTNTEXT);
//}

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

//boolean getDefault () {
//	if ((style & SWT.PUSH) == 0) return false;
//	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//	return (bits & OS.BS_DEFPUSHBUTTON) != 0;
//}

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

@Override
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
    return ((CButton)handle).isSelected();
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
	return text == null? "": text;
}

@Override
boolean isTabItem () {
	//TEMPORARY CODE
	//if ((style & SWT.PUSH) != 0) return true;
	return super.isTabItem ();
}

@Override
boolean isTabGroup() {
  return (style & SWT.RADIO) == 0;
}

@Override
boolean mnemonicHit (char ch) {
	if (!setFocus ()) return false;
	/*
	* Feature in Windows.  When a radio button gets focus,
	* it selects the button in WM_SETFOCUS.  Therefore, it
	* is not necessary to click the button or send events
	* because this has already happened in WM_SETFOCUS.
	*/
//	if ((style & SWT.RADIO) == 0) click ();
	return true;
}

@Override
boolean mnemonicMatch (char key) {
	char mnemonic = findMnemonic (getText ());
	if (mnemonic == '\0') return false;
	return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	image = null;
	if (imageList != null) imageList.dispose ();
	imageList = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
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

//void selectRadio () {
//	/*
//	* This code is intentionally commented.  When two groups
//	* of radio buttons with the same parent are separated by
//	* another control, the correct behavior should be that
//	* the two groups act independently.  This is consistent
//	* with radio tool and menu items.  The commented code
//	* implements this behavior.
//	*/
////	int index = 0;
////	Control [] children = parent._getChildren ();
////	while (index < children.length && children [index] != this) index++;
////	int i = index - 1;
////	while (i >= 0 && children [i].setRadioSelection (false)) --i;
////	int j = index + 1;
////	while (j < children.length && children [j].setRadioSelection (false)) j++;
////	setSelection (true);
//	Control [] children = parent._getChildren ();
//	for (int i=0; i<children.length; i++) {
//		Control child = children [i];
//		if (this != child) child.setRadioSelection (false);
//	}
//	setSelection (true);
//}

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
    if((alignment & SWT.UP) != 0) {
      ((CButton)handle).setAlignment(SwingConstants.NORTH);
    } else if((alignment & SWT.DOWN) != 0) {
      ((CButton)handle).setAlignment(SwingConstants.SOUTH);
    } else if((alignment & SWT.LEFT) != 0) {
      ((CButton)handle).setAlignment(SwingConstants.WEST);
    } else if((alignment & SWT.RIGHT) != 0) {
      ((CButton)handle).setAlignment(SwingConstants.EAST);
    }
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
  if((alignment & SWT.LEFT) != 0) {
    ((CButton)handle).setAlignment(SwingConstants.LEFT);
  } else if((alignment & SWT.RIGHT) != 0) {
    ((CButton)handle).setAlignment(SwingConstants.RIGHT);
  } else if((alignment & SWT.CENTER) != 0) {
    ((CButton)handle).setAlignment(SwingConstants.CENTER);
  }
}

//void setDefault (boolean value) {
//	if ((style & SWT.PUSH) == 0) return;
//	int hwndShell = menuShell ().handle;
//	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//	if (value) {
//		bits |= OS.BS_DEFPUSHBUTTON;
//		OS.SendMessage (hwndShell, OS.DM_SETDEFID, handle, 0);
//	} else {
//		bits &= ~OS.BS_DEFPUSHBUTTON;
//		OS.SendMessage (hwndShell, OS.DM_SETDEFID, 0, 0);
//	}
//	OS.SendMessage (handle, OS.BM_SETSTYLE, bits, 1);
//}

/**
 * Sets the receiver's image to the argument, which may be
 * <code>null</code> indicating that no image should be displayed.
 *
 * @param image the image to display on the receiver (may be <code>null</code>)
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
  if (image != null && image.isDisposed ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
  this.image = image;
  ImageIcon icon = null;
  if (image != null && image.handle != null) {
    icon = new ImageIcon (image.handle);
  }
  ((CButton) handle).setIcon (icon);
}

@Override
boolean setRadioFocus () {
	if ((style & SWT.RADIO) == 0 || !getSelection ()) return false;
	return setFocus ();
}

@Override
boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		sendEvent (SWT.Selection);
	}
	return true;
}

//boolean setSavedFocus () {
//	/*
//	* Feature in Windows.  When a radio button gets focus,
//	* it selects the button in WM_SETFOCUS.  If the previous
//	* saved focus widget was a radio button, allowing the shell
//	* to automatically restore the focus to the previous radio
//	* button will unexpectedly check that button.  The fix is
//	* to disallow focus to be restored to radio button that is
//	* not selected.
//	*/
//	if ((style & SWT.RADIO) != 0 && !getSelection ()) return false;
//	return super.setSavedFocus ();
//}

boolean isAdjustingSelection;

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
	isAdjustingSelection = true;
  ((CButton)handle).setSelected(selected);
  isAdjustingSelection = false;
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the button label.  The label may include
 * the mnemonic character but must not contain line delimiters.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
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
  this.text = string;
  int mnemonicIndex = findMnemonicIndex(string);
  if(mnemonicIndex > 0) {
    String s = string.substring(0, mnemonicIndex - 1).replaceAll("&&", "&");
    string = s + string.substring(mnemonicIndex).replaceAll("&&", "&");
    mnemonicIndex -= mnemonicIndex - 1 - s.length();
    mnemonicIndex--;
  } else {
    string = string.replaceAll("&&", "&");
  }
  CButton cButton = (CButton)handle;
  cButton.setText(string);
  if(mnemonicIndex >= 0) {
    cButton.setMnemonic(string.charAt(mnemonicIndex));
  }
  cButton.setDisplayedMnemonicIndex(mnemonicIndex);
}

//int widgetStyle () {
//	int bits = super.widgetStyle ();
//	if ((style & SWT.FLAT) != 0) bits |= OS.BS_FLAT;
//	if ((style & SWT.ARROW) != 0) return bits | OS.BS_OWNERDRAW;
//	if ((style & SWT.LEFT) != 0) bits |= OS.BS_LEFT;
//	if ((style & SWT.CENTER) != 0) bits |= OS.BS_CENTER;
//	if ((style & SWT.RIGHT) != 0) bits |= OS.BS_RIGHT;
//	if ((style & SWT.PUSH) != 0) return bits | OS.BS_PUSHBUTTON | OS.WS_TABSTOP;
//	if ((style & SWT.CHECK) != 0) return bits | OS.BS_CHECKBOX | OS.WS_TABSTOP;
//	if ((style & SWT.RADIO) != 0) return bits | OS.BS_RADIOBUTTON;
//	if ((style & SWT.TOGGLE) != 0) return bits | OS.BS_PUSHLIKE | OS.BS_CHECKBOX;
//	return bits | OS.BS_PUSHBUTTON | OS.WS_TABSTOP;
//}
//
//TCHAR windowClass () {
//	return ButtonClass;
//}
//
//int windowProc () {
//	return ButtonProc;
//}
//
//LRESULT WM_GETDLGCODE (int wParam, int lParam) {
//	LRESULT result = super.WM_GETDLGCODE (wParam, lParam);
//	if (result != null) return result;
//	if ((style & SWT.ARROW) != 0) {
//		return new LRESULT (OS.DLGC_STATIC);
//	}
//	return result;
//}
//
//LRESULT WM_KILLFOCUS (int wParam, int lParam) {
//	LRESULT result = super.WM_KILLFOCUS (wParam, lParam);
//	if ((style & SWT.PUSH) != 0 && getDefault ()) {
//		menuShell ().setDefaultButton (null, false);
//	}
//	return result;
//}
//
//LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
//	if (ignoreMouse) return null;
//	return super.WM_LBUTTONDOWN (wParam, lParam);
//}
//
//LRESULT WM_LBUTTONUP (int wParam, int lParam) {
//	if (ignoreMouse) return null;
//	return super.WM_LBUTTONUP (wParam, lParam);
//}
//
//LRESULT WM_SETFOCUS (int wParam, int lParam) {
//	/*
//	* Feature in Windows. When Windows sets focus to
//	* a radio button, it sets the WM_TABSTOP style.
//	* This is undocumented and unwanted.  The fix is
//	* to save and restore the window style bits.
//	*/
//	int bits = 0;
//	if ((style & SWT.RADIO) != 0) {
//		bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//	}
//	LRESULT result = super.WM_SETFOCUS (wParam, lParam);
//	if ((style & SWT.RADIO) != 0) {
//		OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
//	}
//	if ((style & SWT.PUSH) != 0) {
//		menuShell ().setDefaultButton (this, false);
//	}
//	return result;
//}
//
//LRESULT wmColorChild (int wParam, int lParam) {
//	LRESULT result = super.wmColorChild (wParam, lParam);
//	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
//		Control control = findThemeControl ();
//		if (control != null) {
//			OS.SetBkMode (wParam, OS.TRANSPARENT);
//			RECT rect = new RECT ();
//			OS.GetClientRect (control.handle, rect);
//			OS.MapWindowPoints (control.handle, handle, rect, 2);
//			control.drawThemeBackground (wParam, rect);
//			return new LRESULT (OS.GetStockObject (OS.NULL_BRUSH));
//		}
//	}
//	return result;
//}
//
//LRESULT wmCommandChild (int wParam, int lParam) {
//	int code = wParam >> 16;
//	switch (code) {
//		case OS.BN_CLICKED:
//		case OS.BN_DOUBLECLICKED:
//			if ((style & (SWT.CHECK | SWT.TOGGLE)) != 0) {
//				setSelection (!getSelection ());
//			} else {
//				if ((style & SWT.RADIO) != 0) {
//					if ((parent.getStyle () & SWT.NO_RADIO_GROUP) != 0) {
//						setSelection (!getSelection ());
//					} else {
//						selectRadio ();
//					}
//				}
//			}
//			postEvent (SWT.Selection);
//	}
//	return super.wmCommandChild (wParam, lParam);
//}
//
//LRESULT wmDrawChild (int wParam, int lParam) {
//	if ((style & SWT.ARROW) == 0) return super.wmDrawChild (wParam, lParam);
//	DRAWITEMSTRUCT struct = new DRAWITEMSTRUCT ();
//	OS.MoveMemory (struct, lParam, DRAWITEMSTRUCT.sizeof);
//	RECT rect = new RECT ();
//	OS.SetRect (rect, struct.left, struct.top, struct.right, struct.bottom);
//	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
//		int hTheme = OS.OpenThemeData (handle, SCROLLBAR);
//		int iStateId = OS.ABS_LEFTNORMAL;
//		switch (style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) {
//			case SWT.UP: iStateId = OS.ABS_UPNORMAL; break;
//			case SWT.DOWN: iStateId = OS.ABS_DOWNNORMAL; break;
//			case SWT.LEFT: iStateId = OS.ABS_LEFTNORMAL; break;
//			case SWT.RIGHT: iStateId = OS.ABS_RIGHTNORMAL; break;
//		}
//		/*
//		* NOTE: The normal, hot, pressed and disabled state is
//		* computed relying on the fact that the increment between
//		* the direction states is invariant (always separated by 4).
//		*/
//		if (!getEnabled ()) iStateId += OS.ABS_UPDISABLED - OS.ABS_UPNORMAL;
//		if ((struct.itemState & OS.ODS_SELECTED) != 0) iStateId += OS.ABS_UPPRESSED - OS.ABS_UPNORMAL;
//		OS.DrawThemeBackground (hTheme, struct.hDC, OS.SBP_ARROWBTN, iStateId, rect, null);
//		OS.CloseThemeData (hTheme);
//	} else {
//		int uState = OS.DFCS_SCROLLLEFT;
//		switch (style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) {
//			case SWT.UP: uState = OS.DFCS_SCROLLUP; break;
//			case SWT.DOWN: uState = OS.DFCS_SCROLLDOWN; break;
//			case SWT.LEFT: uState = OS.DFCS_SCROLLLEFT; break;
//			case SWT.RIGHT: uState = OS.DFCS_SCROLLRIGHT; break;
//		}
//		if (!getEnabled ()) uState |= OS.DFCS_INACTIVE;
//		if ((style & SWT.FLAT) == SWT.FLAT) uState |= OS.DFCS_FLAT;
//		if ((struct.itemState & OS.ODS_SELECTED) != 0) uState |= OS.DFCS_PUSHED;
//		OS.DrawFrameControl (struct.hDC, rect, OS.DFC_SCROLL, uState);
//	}
//	return null;
//}

@Override
public void processEvent(AWTEvent e) {
  int id = e.getID();
  switch(id) {
  case ActionEvent.ACTION_PERFORMED: if((style & SWT.RADIO) == 0 && (isAdjustingSelection || !hooks(SWT.Selection))) { super.processEvent(e); return; } break;
  case ItemEvent.ITEM_STATE_CHANGED: if(isAdjustingSelection || !hooks(SWT.Selection)) { super.processEvent(e); return; } break;
  case KeyEvent.KEY_PRESSED:
    if((style & SWT.RADIO) != 0) {
      switch(((KeyEvent)e).getKeyCode()) {
      case KeyEvent.VK_UP:
      case KeyEvent.VK_LEFT:
      case KeyEvent.VK_DOWN:
      case KeyEvent.VK_RIGHT:
        break;
      default:
        super.processEvent(e);
        return;
      }
    }
    break;
  default: { super.processEvent(e); return; }
  }
  if(isDisposed()) {
    super.processEvent(e);
    return;
  }
  UIThreadUtils.startExclusiveSection(getDisplay());
  if(isDisposed()) {
    UIThreadUtils.stopExclusiveSection();
    super.processEvent(e);
    return;
  }
  try {
    switch(id) {
    case ActionEvent.ACTION_PERFORMED:
      if((style & SWT.RADIO) != 0) {
        if(!getSelection()) {
          ((CButton)handle).setSelected(true);
        }
        if((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
          Component[] components = handle.getParent().getComponents();
          for(int i=0; i<components.length; i++) {
            Component component = components[i];
            if(component instanceof JRadioButton && component != handle) {
              JRadioButton radioButton = (JRadioButton)component;
              if(radioButton.isSelected()) {
                radioButton.setSelected(false);
                if(!isAdjustingSelection && hooks(SWT.Selection)) {
                  ((CControl)radioButton).getSWTHandle().sendEvent (SWT.Selection);
                }
              }
            }
          }
        }
      }
      if(!isAdjustingSelection && hooks(SWT.Selection)) {
        sendEvent (SWT.Selection);
      }
      break;
    case ItemEvent.ITEM_STATE_CHANGED:
      if(hooks(SWT.Selection)) {
        sendEvent (SWT.Selection);
      }
      break;
    case KeyEvent.KEY_PRESSED:
      if((style & SWT.RADIO) != 0) {
        boolean isBackward = false;
        switch(((KeyEvent)e).getKeyCode()) {
        case KeyEvent.VK_UP:
        case KeyEvent.VK_LEFT:
          isBackward = true;
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_RIGHT:
          Component[] components = handle.getParent().getComponents();
          int index = 0;
          for(index=0; index<components.length; index++) {
            Component component = components[index];
            if(component == handle) {
              break;
            }
          }
          for(int i=0; i<components.length; i++) {
            Component component;
            if(isBackward) {
              component = components[(components.length - i + index - 1) % components.length];
            } else {
              component = components[(i + index + 1) % components.length];
            }
            if(component instanceof JRadioButton) {
              if(component != handle) {
                ((JRadioButton)component).requestFocus();
              }
              break;
            }
          }
          break;
        }
      }
      break;
    }
    super.processEvent(e);
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  } finally {
    UIThreadUtils.stopExclusiveSection();
  }
}

}

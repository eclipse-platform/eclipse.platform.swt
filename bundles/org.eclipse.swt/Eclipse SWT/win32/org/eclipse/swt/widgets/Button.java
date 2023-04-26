/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
 *     Conrad Groth - Bug 23837 [FEEP] Button, do not respect foreground and background color on Windows
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent a selectable user interface object that
 * issues notification when pressed and released.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ARROW, CHECK, PUSH, RADIO, TOGGLE, FLAT, WRAP</dd>
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
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#button">Button snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Button extends Control {
	String text = "", message = "";
	Image image, disabledImage;
	ImageList imageList;
	boolean ignoreMouse, grayed, useDarkModeExplorerTheme;
	static final int MARGIN = 4;
	static final int CHECK_WIDTH, CHECK_HEIGHT;
	static final int ICON_WIDTH = 128, ICON_HEIGHT = 128;
	static /*final*/ boolean COMMAND_LINK = false;
	static final char[] STRING_WITH_ZERO_CHAR = new char[] {'0'};
	static final long ButtonProc;
	static final TCHAR ButtonClass = new TCHAR (0, "BUTTON", true);
	static {
		long hBitmap = OS.LoadBitmap (0, OS.OBM_CHECKBOXES);
		if (hBitmap == 0) {
			CHECK_WIDTH = OS.GetSystemMetrics (OS.SM_CXVSCROLL);
			CHECK_HEIGHT = OS.GetSystemMetrics (OS.SM_CYVSCROLL);
		} else {
			BITMAP bitmap = new BITMAP ();
			OS.GetObject (hBitmap, BITMAP.sizeof, bitmap);
			OS.DeleteObject (hBitmap);
			CHECK_WIDTH = bitmap.bmWidth / 4;
			CHECK_HEIGHT =  bitmap.bmHeight / 3;
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
 * @see SWT#UP
 * @see SWT#DOWN
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Button (Composite parent, int style) {
	super (parent, checkStyle (style));
}

void _setImage (Image image) {
	if ((style & SWT.COMMAND) != 0) return;
	if (imageList != null) imageList.dispose ();
	imageList = null;
	if (image != null) {
		imageList = new ImageList (style & SWT.RIGHT_TO_LEFT);
		if (OS.IsWindowEnabled (handle)) {
			imageList.add (image);
		} else {
			if (disabledImage != null) disabledImage.dispose ();
			disabledImage = new Image (display, image, SWT.IMAGE_DISABLE);
			imageList.add (disabledImage);
		}
		BUTTON_IMAGELIST buttonImageList = new BUTTON_IMAGELIST ();
		buttonImageList.himl = imageList.getHandle ();
		int oldBits = OS.GetWindowLong (handle, OS.GWL_STYLE), newBits = oldBits;
		newBits &= ~(OS.BS_LEFT | OS.BS_CENTER | OS.BS_RIGHT);
		if ((style & SWT.LEFT) != 0) newBits |= OS.BS_LEFT;
		if ((style & SWT.CENTER) != 0) newBits |= OS.BS_CENTER;
		if ((style & SWT.RIGHT) != 0) newBits |= OS.BS_RIGHT;
		if (text.length () == 0) {
			if ((style & SWT.LEFT) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
			if ((style & SWT.CENTER) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_CENTER;
			if ((style & SWT.RIGHT) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_RIGHT;
		} else {
			buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
			buttonImageList.margin_left = computeLeftMargin ();
			buttonImageList.margin_right = MARGIN;
			newBits &= ~(OS.BS_CENTER | OS.BS_RIGHT);
			newBits |= OS.BS_LEFT;
		}
		if (newBits != oldBits) {
			OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
			OS.InvalidateRect (handle, null, true);
		}
		OS.SendMessage (handle, OS.BCM_SETIMAGELIST, 0, buttonImageList);
	} else {
		OS.SendMessage (handle, OS.BCM_SETIMAGELIST, 0, 0);
	}
	/*
	* Bug in Windows.  Under certain cirumstances yet to be
	* isolated, BCM_SETIMAGELIST does not redraw the control
	* when a new image is set.  The fix is to force a redraw.
	*/
	OS.InvalidateRect (handle, null, true);
}

void _setText (String text) {
	int oldBits = OS.GetWindowLong (handle, OS.GWL_STYLE), newBits = oldBits;
	newBits &= ~(OS.BS_LEFT | OS.BS_CENTER | OS.BS_RIGHT);
	if ((style & SWT.LEFT) != 0) newBits |= OS.BS_LEFT;
	if ((style & SWT.CENTER) != 0) newBits |= OS.BS_CENTER;
	if ((style & SWT.RIGHT) != 0) newBits |= OS.BS_RIGHT;
	if (imageList != null) {
		BUTTON_IMAGELIST buttonImageList = new BUTTON_IMAGELIST ();
		buttonImageList.himl = imageList.getHandle ();
		if (text.length () == 0) {
			if ((style & SWT.LEFT) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
			if ((style & SWT.CENTER) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_CENTER;
			if ((style & SWT.RIGHT) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_RIGHT;
		} else {
			buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
			buttonImageList.margin_left = computeLeftMargin ();
			buttonImageList.margin_right = MARGIN;
			newBits &= ~(OS.BS_CENTER | OS.BS_RIGHT);
			newBits |= OS.BS_LEFT;
		}
		OS.SendMessage (handle, OS.BCM_SETIMAGELIST, 0, buttonImageList);
	}
	if (newBits != oldBits) {
		OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
		OS.InvalidateRect (handle, null, true);
	}
	/*
	* Bug in Windows.  When a Button control is right-to-left and
	* is disabled, the first pixel of the text is clipped.  The fix
	* is to append a space to the text.
	*/
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		if (!OS.IsAppThemed ()) {
			text = OS.IsWindowEnabled (handle) ? text : text + " ";
		}
	}
	TCHAR buffer = new TCHAR (getCodePage (), text, true);
	OS.SetWindowText (handle, buffer);
	if ((state & HAS_AUTO_DIRECTION) != 0) {
		updateTextDirection (AUTO_TEXT_DIRECTION);
	}
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the control is selected by the user.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 * <p>
 * When the <code>SWT.RADIO</code> style bit is set, the <code>widgetSelected</code> method is
 * also called when the receiver loses selection because another item in the same radio group
 * was selected by the user. During <code>widgetSelected</code> the application can use
 * <code>getSelection()</code> to determine the current selected state of the receiver.
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

@Override
long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (ButtonProc, hwnd, msg, wParam, lParam);
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, COMMAND_LINK ? SWT.COMMAND : 0);
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
	* Feature in Windows.  BM_CLICK sends a fake WM_LBUTTONDOWN and
	* WM_LBUTTONUP in order to click the button.  This causes the
	* application to get unexpected mouse events.  The fix is to
	* ignore mouse events when they are caused by BM_CLICK.
	*/
	ignoreMouse = true;
	OS.SendMessage (handle, OS.BM_CLICK, 0, 0);
	ignoreMouse = false;
}

// TODO: this method ignores the style LEFT, CENTER or RIGHT
int computeLeftMargin () {
	if ((style & (SWT.PUSH | SWT.TOGGLE)) == 0) return MARGIN;
	int margin = 0;
	if (image != null && text.length () != 0) {
		Rectangle bounds = image.getBoundsInPixels ();
		margin += bounds.width + MARGIN * 2;
		long oldFont = 0;
		long hDC = OS.GetDC (handle);
		long newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		char [] buffer = text.toCharArray ();
		RECT rect = new RECT ();
		int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE;
		OS.DrawText (hDC, buffer, buffer.length, rect, flags);
		margin += rect.right - rect.left;
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
		OS.GetClientRect (handle, rect);
		margin = Math.max (MARGIN, (rect.right - rect.left - margin) / 2);
	}
	return margin;
}

@Override Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0, border = getBorderWidthInPixels ();
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN)) != 0) {
			width += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
			height += OS.GetSystemMetrics (OS.SM_CYVSCROLL);
		} else {
			width += OS.GetSystemMetrics (OS.SM_CXHSCROLL);
			height += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
		}
	} else {
		if ((style & SWT.COMMAND) != 0) {
			SIZE size = new SIZE ();
			if (wHint != SWT.DEFAULT) {
				size.cx = wHint;
				OS.SendMessage (handle, OS.BCM_GETIDEALSIZE, 0, size);
				width = size.cx;
				height = size.cy;
			} else {
				OS.SendMessage (handle, OS.BCM_GETIDEALSIZE, 0, size);
				width = size.cy;
				height = size.cy;
				size.cy = 0;
				while (size.cy != height) {
					size.cx = width++;
					size.cy = 0;
					OS.SendMessage (handle, OS.BCM_GETIDEALSIZE, 0, size);
				}
			}
		} else {
			int extra = 0;
			boolean hasImage = image != null, hasText = true;
			if (hasImage) {
				if (image != null) {
					Rectangle rect = image.getBoundsInPixels ();
					width = rect.width;
					if (hasText && text.length () != 0) {
						width += MARGIN * 2;
					}
					height = rect.height;
					extra = MARGIN * 2;
				}
			}
			if (hasText) {
				long oldFont = 0;
				long hDC = OS.GetDC (handle);
				long newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
				if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
				TEXTMETRIC lptm = new TEXTMETRIC ();
				OS.GetTextMetrics (hDC, lptm);
				int length = text.length ();
				if (length == 0) {
					height = Math.max (height, lptm.tmHeight);
				} else {
					extra = Math.max (MARGIN * 2, lptm.tmAveCharWidth);
					char [] buffer = text.toCharArray ();
					RECT rect = new RECT ();
					int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE;
					if ((style & SWT.WRAP) != 0 && wHint != SWT.DEFAULT) {
						flags = OS.DT_CALCRECT | OS.DT_WORDBREAK;
						rect.right = wHint - width - 2 * border;
						if (isRadioOrCheck()) {
							rect.right -= CHECK_WIDTH + 3;
						} else {
							rect.right -= 6;
						}
						if (!OS.IsAppThemed ()) {
							rect.right -= 2;
							if (isRadioOrCheck()) {
								rect.right -= 2;
							}
						}
					}
					OS.DrawText (hDC, buffer, buffer.length, rect, flags);
					width += rect.right - rect.left;
					height = Math.max (height, rect.bottom - rect.top);
				}
				if (newFont != 0) OS.SelectObject (hDC, oldFont);
				OS.ReleaseDC (handle, hDC);
			}
			if (isRadioOrCheck()) {
				width += CHECK_WIDTH + extra;
				height = Math.max (height, CHECK_HEIGHT + 3);
			}
			if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
				width += 12;  height += 10;
			}
		}
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	width += border * 2;
	height += border * 2;
	return new Point (width, height);
}

@Override
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

	if (OS.IsAppThemed ()) {
		/* Set the theme background.
		*
		* NOTE: On Vista this causes problems when the tab
		* key is pressed for push buttons so disable the
		* theme background drawing for these widgets for
		* now.
		*/
		if ((style & (SWT.PUSH | SWT.TOGGLE)) == 0) {
			state |= THEME_BACKGROUND;
		}

		/*
		* Bug in Windows.  For some reason, the HBRUSH that
		* is returned from WM_CTRLCOLOR is misaligned when
		* the button uses it to draw.  If the brush is a solid
		* color, this does not matter.  However, if the brush
		* contains an image, the image is misaligned.  The
		* fix is to draw the background in WM_CTRLCOLOR.
		*
		* NOTE: For comctl32.dll 6.0 with themes disabled,
		* drawing in WM_ERASEBKGND will draw on top of the
		* text of the control.
		*/
		if ((style & SWT.RADIO) != 0) {
			state |= DRAW_BACKGROUND;
		}

		useDarkModeExplorerTheme = display.useDarkModeExplorerTheme;
		maybeEnableDarkSystemTheme();
	}
}

private boolean customBackgroundDrawing() {
	return background != -1 && !isRadioOrCheck();
}

private boolean customDrawing() {
	return customBackgroundDrawing() || customForegroundDrawing();
}

private boolean customForegroundDrawing() {
	return foreground != -1 && !text.isEmpty() && OS.IsWindowEnabled(handle);
}

@Override
int defaultBackground () {
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		return OS.GetSysColor (OS.COLOR_BTNFACE);
	}
	return super.defaultBackground ();
}

@Override
int defaultForeground () {
	return OS.GetSysColor (OS.COLOR_BTNTEXT);
}

@Override
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	/*
	* Bug in Windows.  When a Button control is right-to-left and
	* is disabled, the first pixel of the text is clipped.  The fix
	* is to append a space to the text.
	*/
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		if (!OS.IsAppThemed ()) {
			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
			boolean hasImage = (bits & (OS.BS_BITMAP | OS.BS_ICON)) != 0;
			if (!hasImage) {
				String string = enabled ? text : text + " ";
				TCHAR buffer = new TCHAR (getCodePage (), string, true);
				OS.SetWindowText (handle, buffer);
			}
		}
	}
	/*
	* Bug in Windows.  When a button has the style BS_CHECKBOX
	* or BS_RADIOBUTTON, is checked, and is displaying both an
	* image and some text, when BCM_SETIMAGELIST is used to
	* assign an image list for each of the button states, the
	* button does not draw properly.  When the user drags the
	* mouse in and out of the button, it draws using a blank
	* image.  The fix is to set the complete image list only
	* when the button is disabled.
	*/
	updateImageList ();
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
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the widget does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the grayed state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public boolean getGrayed () {
	checkWidget();
	if ((style & SWT.CHECK) == 0) return false;
	return grayed;
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

/**
 * Returns the widget message. When the widget is created
 * with the style <code>SWT.COMMAND</code>, the message text
 * is displayed to provide further information for the user.
 *
 * @return the widget message
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
/*public*/ String getMessage () {
	checkWidget ();
	return message;
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
	return isChecked();
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
	return text;
}

private boolean isChecked() {
	long flags = OS.SendMessage (handle, OS.BM_GETCHECK, 0, 0);
	return flags != OS.BST_UNCHECKED;
}

private boolean isRadioOrCheck() {
	return (style & (SWT.RADIO | SWT.CHECK)) != 0;
}

@Override
boolean isTabItem () {
	if ((style & SWT.PUSH) != 0) return isTabGroup ();
	return super.isTabItem ();
}

@Override
boolean mnemonicHit (char ch) {
	/*
	 * Feature in Windows. When a radio button gets focus, it selects the button in
	 * WM_SETFOCUS. Workaround is to never set focus to an unselected radio button.
	 * Therefore, don't try to set focus on radio buttons, click will set focus.
	 */
	if ((style & SWT.RADIO) == 0 && !setFocus ()) return false;
	click();
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
	if (imageList != null) imageList.dispose ();
	imageList = null;
	if (disabledImage != null) disabledImage.dispose ();
	disabledImage = null;
	text = null;
	image = null;
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
}

@Override
int resolveTextDirection() {
	return (style & SWT.ARROW) != 0 ? SWT.NONE : BidiUtil.resolveTextDirection(text);
}

void selectRadio () {
	for (Control child : parent._getChildren ()) {
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
	int oldBits = OS.GetWindowLong (handle, OS.GWL_STYLE), newBits = oldBits;
	newBits &= ~(OS.BS_LEFT | OS.BS_CENTER | OS.BS_RIGHT);
	if ((style & SWT.LEFT) != 0) newBits |= OS.BS_LEFT;
	if ((style & SWT.CENTER) != 0) newBits |= OS.BS_CENTER;
	if ((style & SWT.RIGHT) != 0) newBits |= OS.BS_RIGHT;
	if (imageList != null) {
		BUTTON_IMAGELIST buttonImageList = new BUTTON_IMAGELIST ();
		buttonImageList.himl = imageList.getHandle ();
		if (text.length () == 0) {
			if ((style & SWT.LEFT) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
			if ((style & SWT.CENTER) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_CENTER;
			if ((style & SWT.RIGHT) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_RIGHT;
		} else {
			buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
			buttonImageList.margin_left = computeLeftMargin ();
			buttonImageList.margin_right = MARGIN;
			newBits &= ~(OS.BS_CENTER | OS.BS_RIGHT);
			newBits |= OS.BS_LEFT;
		}
		OS.SendMessage (handle, OS.BCM_SETIMAGELIST, 0, buttonImageList);
	}
	if (newBits != oldBits) {
		OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
		OS.InvalidateRect (handle, null, true);
	}
}

/**
 * Sets the button's background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 * <p>
 * Note: This is custom paint operation and only affects {@link SWT#PUSH} and {@link SWT#TOGGLE} buttons. If the native button
 * has a 3D look an feel (e.g. Windows 7), this method will cause the button to look FLAT irrespective of the state of the
 * {@link SWT#FLAT} style.
 * For {@link SWT#CHECK} and {@link SWT#RADIO} buttons, this method delegates to {@link Control#setBackground(Color)}.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setBackground (Color color) {
	// This method only exists in order to provide custom documentation
	super.setBackground(color);
}

void setDefault (boolean value) {
	if ((style & SWT.PUSH) == 0) return;
	long hwndShell = menuShell ().handle;
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

@Override
public boolean setFocus () {
	checkWidget ();
	/*
	* Feature in Windows.  When a radio button gets focus,
	* it selects the button in WM_SETFOCUS.  The fix is to
	* not assign focus to an unselected radio button.
	*/
	if ((style & SWT.RADIO) != 0 && !isChecked ()) return false;
	return super.setFocus ();
}

/**
 * Sets the receiver's image to the argument, which may be
 * <code>null</code> indicating that no image should be displayed.
 * <p>
 * Note that a Button can display an image and text simultaneously.
 * </p>
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
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	this.image = image;
	_setImage (image);
}

/**
 * Sets the grayed state of the receiver.  This state change
 * only applies if the control was created with the SWT.CHECK
 * style.
 *
 * @param grayed the new grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setGrayed (boolean grayed) {
	checkWidget ();
	if ((style & SWT.CHECK) == 0) return;
	this.grayed = grayed;
	long flags = OS.SendMessage (handle, OS.BM_GETCHECK, 0, 0);
	if (grayed) {
		if (flags == OS.BST_CHECKED) updateSelection (OS.BST_INDETERMINATE);
	} else {
		if (flags == OS.BST_INDETERMINATE) updateSelection (OS.BST_CHECKED);
	}
}

/**
 * Sets the widget message. When the widget is created
 * with the style <code>SWT.COMMAND</code>, the message text
 * is displayed to provide further information for the user.
 *
 * @param message the new message
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
/*public*/ void setMessage (String message) {
	checkWidget ();
	if (message == null) error (SWT.ERROR_NULL_ARGUMENT);
	this.message = message;
	if ((style & SWT.COMMAND) != 0) {
		int length = message.length ();
		char [] chars = new char [length + 1];
		message.getChars(0, length, chars, 0);
		OS.SendMessage (handle, OS.BCM_SETNOTE, 0, chars);
	}
}

@Override
boolean setRadioFocus (boolean tabbing) {
	if ((style & SWT.RADIO) == 0 || !getSelection ()) return false;
	return tabbing ? setTabItemFocus () : setFocus ();
}

@Override
boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		sendSelectionEvent (SWT.Selection);
	}
	return true;
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
	if ((style & SWT.CHECK) != 0) {
		if (selected && grayed) flags = OS.BST_INDETERMINATE;
	}
	updateSelection (flags);
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
 * underlined but may be emphasized in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p><p>
 * Note that a Button can display an image and text simultaneously
 * on Windows (starting with XP), GTK+ and OSX.  On other platforms,
 * a Button that has an image and text set into it will display the
 * image or text that was set most recently.
 * </p><p>
 * Also note, if control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
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
	if ((style & SWT.ARROW) != 0) return;
	text = string;
	_setText (string);
}

@Override
boolean updateTextDirection(int textDirection) {
	if (super.updateTextDirection(textDirection)) {
// TODO: Keep for now, to follow up
//		int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
//		style &= ~SWT.MIRRORED;
//		style &= ~flags;
//		style |= textDirection & flags;
//		updateOrientation ();
//		checkMirrored ();
		return true;
	}
	return false;
}

void updateImageList () {
	if (imageList != null) {
		BUTTON_IMAGELIST buttonImageList = new BUTTON_IMAGELIST ();
		OS.SendMessage (handle, OS.BCM_GETIMAGELIST, 0, buttonImageList);
		if (imageList != null) imageList.dispose ();
		imageList = new ImageList (style & SWT.RIGHT_TO_LEFT);
		if (OS.IsWindowEnabled (handle)) {
			imageList.add (image);
		} else {
			if (disabledImage != null) disabledImage.dispose ();
			disabledImage = new Image (display, image, SWT.IMAGE_DISABLE);
			imageList.add (disabledImage);
		}
		buttonImageList.himl = imageList.getHandle ();
		OS.SendMessage (handle, OS.BCM_SETIMAGELIST, 0, buttonImageList);
		/*
		* Bug in Windows.  Under certain cirumstances yet to be
		* isolated, BCM_SETIMAGELIST does not redraw the control
		* when an image is set.  The fix is to force a redraw.
		*/
		OS.InvalidateRect (handle, null, true);
	}
}

@Override
void updateOrientation () {
	super.updateOrientation ();
	updateImageList ();
}

void updateSelection (int flags) {
	if (flags != OS.SendMessage (handle, OS.BM_GETCHECK, 0, 0)) {
		/*
		* Feature in Windows. When BM_SETCHECK is used
		* to set the checked state of a radio or check
		* button, it sets the WS_TABSTOP style.  This
		* is undocumented and unwanted.  The fix is
		* to save and restore the window style bits.
		*/
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		if ((style & SWT.CHECK) != 0) {
			if (flags == OS.BST_INDETERMINATE) {
				bits &= ~OS.BS_CHECKBOX;
				bits |= OS.BS_3STATE;
			} else {
				bits |= OS.BS_CHECKBOX;
				bits &= ~OS.BS_3STATE;
			}
			if (bits != OS.GetWindowLong (handle, OS.GWL_STYLE)) {
				OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
			}
		}
		OS.SendMessage (handle, OS.BM_SETCHECK, flags, 0);
		if (bits != OS.GetWindowLong (handle, OS.GWL_STYLE)) {
			OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
		}
	}
}

@Override
int widgetStyle () {
	int bits = super.widgetStyle ();
	if ((style & SWT.FLAT) != 0) bits |= OS.BS_FLAT;
	if ((style & SWT.ARROW) != 0) return bits | OS.BS_OWNERDRAW;
	if ((style & SWT.LEFT) != 0) bits |= OS.BS_LEFT;
	if ((style & SWT.CENTER) != 0) bits |= OS.BS_CENTER;
	if ((style & SWT.RIGHT) != 0) bits |= OS.BS_RIGHT;
	if ((style & SWT.WRAP) != 0) bits |= OS.BS_MULTILINE;
	if ((style & SWT.PUSH) != 0) return bits | OS.BS_PUSHBUTTON | OS.WS_TABSTOP;
	if ((style & SWT.CHECK) != 0) return bits | OS.BS_CHECKBOX | OS.WS_TABSTOP;
	if ((style & SWT.RADIO) != 0) return bits | OS.BS_RADIOBUTTON;
	if ((style & SWT.TOGGLE) != 0) return bits | OS.BS_PUSHLIKE | OS.BS_CHECKBOX | OS.WS_TABSTOP;
	if ((style & SWT.COMMAND) != 0) return bits | OS.BS_COMMANDLINK | OS.WS_TABSTOP;
	return bits | OS.BS_PUSHBUTTON | OS.WS_TABSTOP;
}

@Override
TCHAR windowClass () {
	return ButtonClass;
}

@Override
long windowProc () {
	return ButtonProc;
}

@Override
LRESULT wmColorChild (long wParam, long lParam) {
	if (isRadioOrCheck()) {
		// In order to match old SWT behavior and SWT behavior on other
		// platforms, Radio and Check have their own background instead
		// of showing parent's background.
		return super.wmColorChild(wParam, lParam);
	} else {
		// Button has "transparent" portions which need to be filled with
		// parent's (and not Button's) background. For example, SWT.PUSH
		// button ~2px transparent area around the button.
		return parent.wmColorChild(wParam, lParam);
	}
}

@Override
LRESULT WM_GETDLGCODE (long wParam, long lParam) {
	LRESULT result = super.WM_GETDLGCODE (wParam, lParam);
	if (result != null) return result;
	if ((style & SWT.ARROW) != 0) {
		return new LRESULT (OS.DLGC_STATIC);
	}
	return result;
}

@Override
LRESULT WM_GETOBJECT (long wParam, long lParam) {
	/*
	* Ensure that there is an accessible object created for this
	* control because support for radio button position in group
	* accessibility is implemented in the accessibility package.
	*/
	if ((style & SWT.RADIO) != 0) {
		if (accessible == null) accessible = new_Accessible (this);
	}
	return super.WM_GETOBJECT (wParam, lParam);
}

@Override
LRESULT WM_KILLFOCUS (long wParam, long lParam) {
	LRESULT result = super.WM_KILLFOCUS (wParam, lParam);
	if ((style & SWT.PUSH) != 0 && getDefault ()) {
		menuShell ().setDefaultButton (null, false);
	}
	return result;
}

@Override
LRESULT WM_LBUTTONDOWN (long wParam, long lParam) {
	if (ignoreMouse) return null;
	return super.WM_LBUTTONDOWN (wParam, lParam);
}

@Override
LRESULT WM_LBUTTONUP (long wParam, long lParam) {
	if (ignoreMouse) return null;
	return super.WM_LBUTTONUP (wParam, lParam);
}

@Override
LRESULT WM_SETFOCUS (long wParam, long lParam) {
	/*
	* Feature in Windows. When Windows sets focus to
	* a radio button, it sets the WS_TABSTOP style.
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

@Override
LRESULT WM_SIZE (long wParam, long lParam) {
	LRESULT result = super.WM_SIZE (wParam, lParam);
	if (result != null) return result;
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		if (imageList != null && text.length () != 0) {
			BUTTON_IMAGELIST buttonImageList = new BUTTON_IMAGELIST ();
			OS.SendMessage (handle, OS.BCM_GETIMAGELIST, 0, buttonImageList);
			buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
			buttonImageList.margin_left = computeLeftMargin ();
			buttonImageList.margin_right = MARGIN;
			OS.SendMessage (handle, OS.BCM_SETIMAGELIST, 0, buttonImageList);
		}
	}
	return result;
}

@Override
LRESULT WM_SYSCOLORCHANGE (long wParam, long lParam) {
	LRESULT result = super.WM_SYSCOLORCHANGE (wParam, lParam);
	if (result != null) return result;
	return result;
}

@Override
LRESULT WM_UPDATEUISTATE (long wParam, long lParam) {
	LRESULT result = super.WM_UPDATEUISTATE (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  When WM_UPDATEUISTATE is sent to
	* a button, it sends WM_CTLCOLORBTN to get the foreground
	* and background.  If drawing happens in WM_CTLCOLORBTN,
	* it will overwrite the contents of the control.  The
	* fix is draw the button without drawing the background
	* and avoid the button window proc.
	*
	* NOTE:  This only happens for radio, check and toggle
	* buttons.
	*/
	if ((style & (SWT.RADIO | SWT.CHECK | SWT.TOGGLE)) != 0) {
		boolean redraw = findImageControl () != null;
		if (!redraw) {
			if ((state & THEME_BACKGROUND) != 0) {
				if (OS.IsAppThemed ()) {
					redraw = findThemeControl () != null;
				}
			}
			if (!redraw) redraw = findBackgroundControl () != null;
		}
		if (redraw) {
			OS.InvalidateRect (handle, null, false);
			long code = OS.DefWindowProc (handle, OS.WM_UPDATEUISTATE, wParam, lParam);
			return new LRESULT (code);
		}
	}
	/*
	* Feature in Windows.  Push and toggle buttons draw directly
	* in WM_UPDATEUISTATE rather than damaging and drawing later
	* in WM_PAINT.  This means that clients who hook WM_PAINT
	* expecting to get all the drawing will not.  The fix is to
	* redraw the control when paint events are hooked.
	*/
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		if (hooks (SWT.Paint) || filters (SWT.Paint) || customDrawing()) {
			OS.InvalidateRect (handle, null, true);
		}
	}
	return result;
}

@Override
LRESULT wmCommandChild (long wParam, long lParam) {
	int code = OS.HIWORD (wParam);
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
			sendSelectionEvent (SWT.Selection);
	}
	return super.wmCommandChild (wParam, lParam);
}

private int getCheckboxTextOffset(long hdc) {
	int result = 0;

	SIZE size = new SIZE();

	if (OS.IsAppThemed ()) {
		OS.GetThemePartSize(display.hButtonTheme(), hdc, OS.BP_CHECKBOX, OS.CBS_UNCHECKEDNORMAL, null, OS.TS_TRUE, size);
		result += size.cx;
	} else {
		result += DPIUtil.autoScaleUpUsingNativeDPI(13);
	}

	// Windows uses half width of '0' as checkbox-to-text distance.
	OS.GetTextExtentPoint32(hdc, STRING_WITH_ZERO_CHAR, 1, size);
	result += size.cx / 2;

	return result;
}

@Override
LRESULT wmNotifyChild (NMHDR hdr, long wParam, long lParam) {
	switch (hdr.code) {
		case OS.NM_CUSTOMDRAW:
			// this message will not appear for owner-draw buttons (currently the ARROW button).

			NMCUSTOMDRAW nmcd = new NMCUSTOMDRAW ();
			OS.MoveMemory (nmcd, lParam, NMCUSTOMDRAW.sizeof);

			switch (nmcd.dwDrawStage) {
				case OS.CDDS_PREPAINT: {
					// buttons are ignoring SetBkColor, SetBkMode and SetTextColor
					if (customBackgroundDrawing()) {
						int pixel = background;
						if ((nmcd.uItemState & OS.CDIS_SELECTED) != 0) {
							pixel = getDifferentColor(background);
						} else if ((nmcd.uItemState & OS.CDIS_HOT) != 0) {
							pixel = getSlightlyDifferentColor(background);
						}
						if ((style & SWT.TOGGLE) != 0 && isChecked()) {
							pixel = getDifferentColor(background);
						}

						long brush = OS.CreateSolidBrush(pixel);

						int inset = 2;
						int radius = 3;
						if (useDarkModeExplorerTheme && (OS.WIN32_BUILD >= OS.WIN32_BUILD_WIN11_21H2)) {
							// On Win11, Light theme and Dark theme images have different sizes
							inset = 1;
							radius = 4;
						}

						int l = nmcd.left + inset;
						int t = nmcd.top + inset;
						int r = nmcd.right - inset;
						int b = nmcd.bottom - inset;

						if (OS.WIN32_BUILD >= OS.WIN32_BUILD_WIN11_21H2) {
							// 'RoundRect' has left/top pixel reserved for border
							l += 1;
							t += 1;

							// Win11 has buttons with rounded corners
							OS.SaveDC(nmcd.hdc);
							OS.SelectObject(nmcd.hdc, brush);
							OS.SelectObject(nmcd.hdc, OS.GetStockObject(OS.NULL_PEN));
							OS.RoundRect(nmcd.hdc, l, t, r, b, radius, radius);
							OS.RestoreDC(nmcd.hdc, -1);
						} else {
							RECT rect = new RECT (l, t, r, b);
							OS.FillRect(nmcd.hdc, rect, brush);
						}

						OS.DeleteObject(brush);
					}
					if (customForegroundDrawing()) {
						int radioOrCheckTextPadding = getCheckboxTextOffset(nmcd.hdc);
						int border = isRadioOrCheck() ? 0 : 3;
						int left = nmcd.left + border;
						int right = nmcd.right - border;
						if (image != null) {
							GCData data = new GCData();
							data.device = display;
							GC gc = GC.win32_new (nmcd.hdc, data);

							int margin = computeLeftMargin();
							int imageWidth = image.getBoundsInPixels().width;
							left += (imageWidth + (isRadioOrCheck() ? 2 * MARGIN : MARGIN)); // for SWT.RIGHT_TO_LEFT right and left are inverted

							int x = margin + (isRadioOrCheck() ? radioOrCheckTextPadding : 3);
							int y = Math.max (0, (nmcd.bottom - image.getBoundsInPixels().height) / 2);
							gc.drawImage (image, DPIUtil.autoScaleDown(x), DPIUtil.autoScaleDown(y));
							gc.dispose ();
						}

						left += isRadioOrCheck() ? radioOrCheckTextPadding : 0;
						RECT textRect = new RECT ();
						OS.SetRect (textRect, left, nmcd.top + border, right, nmcd.bottom - border);

						// draw text
						char [] buffer = text.toCharArray ();
						int flags = 0;
						if ((style & SWT.WRAP) != 0) {
							flags |= OS.DT_WORDBREAK;
							if (!isRadioOrCheck() && image != null) {
								textRect.right -= MARGIN;
							}
						} else {
							flags |= OS.DT_SINGLELINE; // TODO: this always draws the prefix
						}
						OS.DrawText(nmcd.hdc, buffer, buffer.length, textRect, flags | OS.DT_CALCRECT);
						OS.OffsetRect(textRect, 0, Math.max(0, (nmcd.bottom  - textRect.bottom - border) / 2));
						if (image != null) {
							// The default button with an image doesn't respect the text alignment. So we do the same for styled buttons.
							flags |= OS.DT_LEFT;
							if (!isRadioOrCheck()) {
								OS.OffsetRect(textRect, Math.max(MARGIN, (right - textRect.right) / 2 + 1), 0);
							}
						} else if ((style & SWT.LEFT) != 0) {
							flags |= OS.DT_LEFT;
						} else if ((style & SWT.RIGHT) != 0) {
							flags |= OS.DT_RIGHT;
							OS.OffsetRect(textRect, right - textRect.right, 0);
						} else {
							flags |= OS.DT_CENTER;
							OS.OffsetRect(textRect, (right - textRect.right) / 2, 0);
						}
						OS.SetBkMode(nmcd.hdc, OS.TRANSPARENT);
						OS.SetTextColor(nmcd.hdc, foreground);
						OS.DrawText(nmcd.hdc, buffer, buffer.length, textRect, flags);

						// draw focus rect
						if ((nmcd.uItemState & OS.CDIS_FOCUS) != 0) {
							RECT focusRect = new RECT ();
							if (isRadioOrCheck()) {
								if (text.length() > 0) {
									OS.SetRect(focusRect, textRect.left-1, textRect.top, Math.min(nmcd.right, textRect.right+1), Math.min(nmcd.bottom, textRect.bottom+1));
								} else {
									/*
									 * With custom foreground, draw focus rectangle for CheckBox
									 * and Radio buttons considering the native text padding
									 * value(which is DPI aware). See bug 508141 for details.
									 */
									OS.SetRect (focusRect, nmcd.left+1+radioOrCheckTextPadding, nmcd.top, nmcd.right-2, nmcd.bottom-1);
								}
							} else {
								OS.SetRect (focusRect, nmcd.left+4, nmcd.top+4, nmcd.right-4, nmcd.bottom-4);
							}
							OS.DrawFocusRect(nmcd.hdc, focusRect);
						}
						return new LRESULT (OS.CDRF_SKIPDEFAULT);
					}
					return new LRESULT (OS.CDRF_DODEFAULT);
				}
			}
			break;
	}
	return super.wmNotifyChild (hdr, wParam, lParam);
}

static int getThemeStateId(int style, boolean pressed, boolean enabled) {
	int direction = style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);

	/*
	 * Feature in Windows.  DrawThemeBackground() does not mirror the drawing.
	 * The fix is switch left to right and right to left.
	 */
	if ((style & SWT.MIRRORED) != 0) {
		if        (direction == SWT.LEFT) {
			direction = SWT.RIGHT;
		} else if (direction == SWT.RIGHT) {
			direction = SWT.LEFT;
		}
	}

	/*
	 * On Win11, scrollbars no longer show arrows by default.
	 * Arrows only show up when hot/disabled/pushed.
	 * The workaround is to use hot image in place of default.
	 */
	boolean hot = false;
	if (OS.WIN32_BUILD >= OS.WIN32_BUILD_WIN11_21H2) {
		if (!pressed && enabled) {
			hot = true;
		}
	}

	if (hot) {
		switch (direction) {
			case SWT.UP:    return OS.ABS_UPHOT;
			case SWT.DOWN:  return OS.ABS_DOWNHOT;
			case SWT.LEFT:  return OS.ABS_LEFTHOT;
			case SWT.RIGHT: return OS.ABS_RIGHTHOT;
		}
	}

	if (pressed) {
		switch (direction) {
			case SWT.UP:    return OS.ABS_UPPRESSED;
			case SWT.DOWN:  return OS.ABS_DOWNPRESSED;
			case SWT.LEFT:  return OS.ABS_LEFTPRESSED;
			case SWT.RIGHT: return OS.ABS_RIGHTPRESSED;
		}
	}

	if (!enabled) {
		switch (direction) {
			case SWT.UP:    return OS.ABS_UPDISABLED;
			case SWT.DOWN:  return OS.ABS_DOWNDISABLED;
			case SWT.LEFT:  return OS.ABS_LEFTDISABLED;
			case SWT.RIGHT: return OS.ABS_RIGHTDISABLED;
		}
	}

	switch (direction) {
		case SWT.UP:    return OS.ABS_UPNORMAL;
		case SWT.DOWN:  return OS.ABS_DOWNNORMAL;
		case SWT.LEFT:  return OS.ABS_LEFTNORMAL;
		case SWT.RIGHT: return OS.ABS_RIGHTNORMAL;
	}

	// Have some sane value if all else fails
	return OS.ABS_LEFTNORMAL;
}

@Override
LRESULT wmDrawChild (long wParam, long lParam) {
	if ((style & SWT.ARROW) == 0) return super.wmDrawChild (wParam, lParam);
	DRAWITEMSTRUCT struct = new DRAWITEMSTRUCT ();
	OS.MoveMemory (struct, lParam, DRAWITEMSTRUCT.sizeof);
	RECT rect = new RECT ();
	OS.SetRect (rect, struct.left, struct.top, struct.right, struct.bottom);
	if (OS.IsAppThemed ()) {
		boolean pressed = ((struct.itemState & OS.ODS_SELECTED) != 0);
		boolean enabled = getEnabled ();
		int iStateId = getThemeStateId(style, pressed, enabled);
		OS.DrawThemeBackground (display.hScrollBarThemeAuto (), struct.hDC, OS.SBP_ARROWBTN, iStateId, rect, null);
	} else {
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
		OS.DrawFrameControl (struct.hDC, rect, OS.DFC_SCROLL, uState);
	}
	return null;
}

}

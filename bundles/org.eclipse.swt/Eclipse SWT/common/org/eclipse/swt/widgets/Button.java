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
public class Button extends Canvas {
	String text = "", message = "";
	Image image, disabledImage;
	ImageList imageList;
	boolean ignoreMouse, grayed, useDarkModeExplorerTheme;
	static final int MARGIN = 4;
	static final int ICON_WIDTH = 128, ICON_HEIGHT = 128;
	static /*final*/ boolean COMMAND_LINK = false;
	static final char[] STRING_WITH_ZERO_CHAR = new char[] {'0'};
	private int width;
	private int height;
	private Listener listener;

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

	listener = event -> {
		switch (event.type) {
		case SWT.Dispose:
			onDispose(event);
			break;
		case SWT.MouseDown:
			onMouseDown(event);
			break;
		case SWT.MouseUp:
			onMouseUp(event);
			break;
		case SWT.Paint:
			onPaint(event);
			break;
		case SWT.Resize:
			onResize();
			break;
		case SWT.KeyDown:
			onKeyDown(event);
			break;
		case SWT.FocusIn:
			onFocus();
			break;
		case SWT.FocusOut:
			onFocus();
			break;
		case SWT.Traverse:
			onTraverse(event);
			break;
		case SWT.Selection:
			onSelection(event);
			break;
		}
	};
	addListener(SWT.Dispose, listener);
	addListener(SWT.MouseDown, listener);
	addListener(SWT.MouseUp, listener);
	addListener(SWT.Paint, listener);
	addListener(SWT.Resize, listener);
	addListener(SWT.KeyDown, listener);
	addListener(SWT.FocusIn, listener);
	addListener(SWT.FocusOut, listener);
	addListener(SWT.Traverse, listener);
	addListener(SWT.Selection, listener);

}

private void onDispose(DisposeEvent e) {
	this.dispose();
}



private void onMouseUp(MouseEvent e) {
	notifyListeners(SWT.Selection, new Event());
}

private void onKeyReleased(KeyEvent e) {
	System.out.println("Key: " + e.keyCode + " - " + e.character);

	int oneOf = e.keyCode & SWT.CR & SWT.LF & SWT.DEL & SWT.ESC & SWT.TAB;
	System.out.println("Is one of: " + oneOf);

	notifyListeners(SWT.Selection, new Event());
}

@Override
void sendSelectionEvent(int type) {
	System.out.println("YAAAAAY! " + new Throwable().getStackTrace()[0]);
	super.sendSelectionEvent(type);
}

private void onSelection(Event event) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

}

private void onTraverse(Event event) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
}

private void onFocus() {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

}

private void onKeyDown(Event event) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

}

private void onResize() {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

}

private void onPaint(Event event) {
	System.out.println("Paint: GC=" + event.gc);
	GC gc = event.gc;
	doPaint(gc, getBackground(), getForeground());
	gc.dispose();
}

private void onDispose(Event event) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

}

private void onMouseDown(Event e) {
	GC gc = (e.gc != null) ? e.gc : new GC(this);
	// switch fg and bg colors
	doPaint(gc, getForeground(), getBackground());
	gc.dispose();
}

private void onMouseUp(Event e) {
	GC gc = (e.gc != null) ? e.gc : new GC(this);
	doPaint(gc, getBackground(), getForeground());
	gc.dispose();
	notifyListeners(SWT.Selection, new Event());
}

private void doPaint(GC gc, Color bgColor, Color fgColor) {
	// draw background
	gc.setBackground(bgColor);
	gc.fillRectangle(getClientArea());

	// draw border
	gc.setForeground(fgColor);
	gc.drawRectangle(getClientArea());


	// draw text
	// TODO (visjee) the text should be centered both vertically and horizontally
	gc.drawText(getText(), 0, 0);
}

@Override
public Color getBackground() {
	return new Color(0, 0, 255); // blue
}

@Override
public Color getForeground() {
	return new Color(255, 0, 0); // red
}

private void refreshCheckSize(int nativeZoom) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
}

@Override
public void setSize(int width, int height) {
	checkWidget();
	this.width = width;
	this.height = height;
}

@Override
public void setBounds(int x, int y, int width, int height) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	super.setBounds(x, y, width, height);
}

@Override
public Point computeSize(int wHint, int hHint) {
	checkWidget();
	return new Point(wHint, hHint);
}

@Override
public Point getSize() {
	return new Point(width, height);
}

/************************************************/
// Old code (win32)
/************************************************/

void _setImage (Image image) {
//	if ((style & SWT.COMMAND) != 0) return;
//	if (imageList != null) imageList.dispose ();
//	imageList = null;
//	if (image != null) {
//		imageList = new ImageList (style & SWT.RIGHT_TO_LEFT, getZoom());
//		if (OS.IsWindowEnabled (handle)) {
//			imageList.add (image);
//		} else {
//			if (disabledImage != null) disabledImage.dispose ();
//			disabledImage = new Image (display, image, SWT.IMAGE_DISABLE);
//			imageList.add (disabledImage);
//		}
//		BUTTON_IMAGELIST buttonImageList = new BUTTON_IMAGELIST ();
//		buttonImageList.himl = imageList.getHandle(getZoom());
//		int oldBits = OS.GetWindowLong (handle, OS.GWL_STYLE), newBits = oldBits;
//		newBits &= ~(OS.BS_LEFT | OS.BS_CENTER | OS.BS_RIGHT);
//		if ((style & SWT.LEFT) != 0) newBits |= OS.BS_LEFT;
//		if ((style & SWT.CENTER) != 0) newBits |= OS.BS_CENTER;
//		if ((style & SWT.RIGHT) != 0) newBits |= OS.BS_RIGHT;
//		if (text.length () == 0) {
//			if ((style & SWT.LEFT) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
//			if ((style & SWT.CENTER) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_CENTER;
//			if ((style & SWT.RIGHT) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_RIGHT;
//		} else {
//			buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
//			buttonImageList.margin_left = computeLeftMargin ();
//			buttonImageList.margin_right = MARGIN;
//			newBits &= ~(OS.BS_CENTER | OS.BS_RIGHT);
//			newBits |= OS.BS_LEFT;
//		}
//		if (newBits != oldBits) {
//			OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
//			OS.InvalidateRect (handle, null, true);
//		}
//		OS.SendMessage (handle, OS.BCM_SETIMAGELIST, 0, buttonImageList);
//	} else {
//		OS.SendMessage (handle, OS.BCM_SETIMAGELIST, 0, 0);
//	}
//	/*
//	* Bug in Windows.  Under certain cirumstances yet to be
//	* isolated, BCM_SETIMAGELIST does not redraw the control
//	* when a new image is set.  The fix is to force a redraw.
//	*/
//	OS.InvalidateRect (handle, null, true);
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
}

void _setText (String text) {
//	int oldBits = OS.GetWindowLong (handle, OS.GWL_STYLE), newBits = oldBits;
//	newBits &= ~(OS.BS_LEFT | OS.BS_CENTER | OS.BS_RIGHT);
//	if ((style & SWT.LEFT) != 0) newBits |= OS.BS_LEFT;
//	if ((style & SWT.CENTER) != 0) newBits |= OS.BS_CENTER;
//	if ((style & SWT.RIGHT) != 0) newBits |= OS.BS_RIGHT;
//	if (imageList != null) {
//		BUTTON_IMAGELIST buttonImageList = new BUTTON_IMAGELIST ();
//		buttonImageList.himl = imageList.getHandle(getZoom());
//		if (text.length () == 0) {
//			if ((style & SWT.LEFT) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
//			if ((style & SWT.CENTER) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_CENTER;
//			if ((style & SWT.RIGHT) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_RIGHT;
//		} else {
//			buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
//			buttonImageList.margin_left = computeLeftMargin ();
//			buttonImageList.margin_right = MARGIN;
//			newBits &= ~(OS.BS_CENTER | OS.BS_RIGHT);
//			newBits |= OS.BS_LEFT;
//		}
//		OS.SendMessage (handle, OS.BCM_SETIMAGELIST, 0, buttonImageList);
//	}
//	if (newBits != oldBits) {
//		OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
//		OS.InvalidateRect (handle, null, true);
//	}
//	/*
//	* Bug in Windows.  When a Button control is right-to-left and
//	* is disabled, the first pixel of the text is clipped.  The fix
//	* is to append a space to the text.
//	*/
//	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
//		if (!OS.IsAppThemed ()) {
//			text = OS.IsWindowEnabled (handle) ? text : text + " ";
//		}
//	}
//	TCHAR buffer = new TCHAR (getCodePage (), text, true);
//	OS.SetWindowText (handle, buffer);
//	if ((state & HAS_AUTO_DIRECTION) != 0) {
//		updateTextDirection (AUTO_TEXT_DIRECTION);
//	}
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
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
	addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
}

//@Override
//long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
//	if (handle == 0) return 0;
////	return OS.CallWindowProc (ButtonProc, hwnd, msg, wParam, lParam);
//	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
//	return -1;
//}

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
//	OS.SendMessage (handle, OS.BM_CLICK, 0, 0);
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	ignoreMouse = false;
}

// TODO: this method ignores the style LEFT, CENTER or RIGHT
int computeLeftMargin () {
	if ((style & (SWT.PUSH | SWT.TOGGLE)) == 0) return MARGIN;
	int margin = 0;
	if (image != null && text.length () != 0) {
//		Rectangle bounds = DPIUtil.scaleBounds(image.getBounds(), this.getZoom(), 100);
//		margin += bounds.width + MARGIN * 2;
//		long oldFont = 0;
//		long hDC = OS.GetDC (handle);
//		long newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
//		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
//		char [] buffer = text.toCharArray ();
//		RECT rect = new RECT ();
//		int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE;
//		OS.DrawText (hDC, buffer, buffer.length, rect, flags);
//		margin += rect.right - rect.left;
//		if (newFont != 0) OS.SelectObject (hDC, oldFont);
//		OS.ReleaseDC (handle, hDC);
//		OS.GetClientRect (handle, rect);
//		if ((style & SWT.LEFT) != 0) {
//			margin = MARGIN;
//		}
//		else if ((style & SWT.RIGHT) != 0) {
//			margin = Math.max (MARGIN, (rect.right - rect.left - margin - MARGIN));
//		}
//		else {
//			margin = Math.max (MARGIN, (rect.right - rect.left - margin) / 2);
//		}
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}
	return margin;
}

@Override
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	/*
	* Bug in Windows.  When a Button control is right-to-left and
	* is disabled, the first pixel of the text is clipped.  The fix
	* is to append a space to the text.
	*/
//	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
//		if (!OS.IsAppThemed ()) {
//			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//			boolean hasImage = (bits & (OS.BS_BITMAP | OS.BS_ICON)) != 0;
//			if (!hasImage) {
//				String string = enabled ? text : text + " ";
//				TCHAR buffer = new TCHAR (getCodePage (), string, true);
//				OS.SetWindowText (handle, buffer);
//			}
//		}
//	}
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

	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
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
//	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//	return (bits & OS.BS_DEFPUSHBUTTON) != 0;
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	return false;
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
//	char mnemonic = findMnemonic (getText ());
//	if (mnemonic == '\0') return false;
//	return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	return false;
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
//	if ((style & SWT.ARROW) != 0) {
//		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return;
//		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
//		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
//		OS.InvalidateRect (handle, null, true);
//		return;
//	}
//	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
//	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
//	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
//	int oldBits = OS.GetWindowLong (handle, OS.GWL_STYLE), newBits = oldBits;
//	newBits &= ~(OS.BS_LEFT | OS.BS_CENTER | OS.BS_RIGHT);
//	if ((style & SWT.LEFT) != 0) newBits |= OS.BS_LEFT;
//	if ((style & SWT.CENTER) != 0) newBits |= OS.BS_CENTER;
//	if ((style & SWT.RIGHT) != 0) newBits |= OS.BS_RIGHT;
//	if (imageList != null) {
//		BUTTON_IMAGELIST buttonImageList = new BUTTON_IMAGELIST ();
//		buttonImageList.himl = imageList.getHandle(getZoom());
//		if (text.length () == 0) {
//			if ((style & SWT.LEFT) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
//			if ((style & SWT.CENTER) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_CENTER;
//			if ((style & SWT.RIGHT) != 0) buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_RIGHT;
//		} else {
//			buttonImageList.uAlign = OS.BUTTON_IMAGELIST_ALIGN_LEFT;
//			buttonImageList.margin_left = computeLeftMargin ();
//			buttonImageList.margin_right = MARGIN;
//			newBits &= ~(OS.BS_CENTER | OS.BS_RIGHT);
//			newBits |= OS.BS_LEFT;
//		}
//		OS.SendMessage (handle, OS.BCM_SETIMAGELIST, 0, buttonImageList);
//	}
//	if (newBits != oldBits) {
//		OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
//		OS.InvalidateRect (handle, null, true);
//	}
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
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
//	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//	if (value) {
//		bits |= OS.BS_DEFPUSHBUTTON;
//		OS.SendMessage (hwndShell, OS.DM_SETDEFID, handle, 0);
//	} else {
//		bits &= ~OS.BS_DEFPUSHBUTTON;
//		OS.SendMessage (hwndShell, OS.DM_SETDEFID, 0, 0);
//	}
//	OS.SendMessage (handle, OS.BM_SETSTYLE, bits, 1);
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
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
//	long flags = OS.SendMessage (handle, OS.BM_GETCHECK, 0, 0);
//	if (grayed) {
//		if (flags == OS.BST_CHECKED) updateSelection (OS.BST_INDETERMINATE);
//	} else {
//		if (flags == OS.BST_INDETERMINATE) updateSelection (OS.BST_CHECKED);
//	}
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
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
//		OS.SendMessage (handle, OS.BCM_SETNOTE, 0, chars);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}
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
//	int flags = selected ? OS.BST_CHECKED : OS.BST_UNCHECKED;
//	if ((style & SWT.CHECK) != 0) {
//		if (selected && grayed) flags = OS.BST_INDETERMINATE;
//	}
//	updateSelection (flags);
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
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

void updateImageList () {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
}


void updateSelection (int flags) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
}

private boolean isChecked() {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	return false;
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
//	boolean hot = false;
//	if (OS.WIN32_BUILD >= OS.WIN32_BUILD_WIN11_21H2) {
//		if (!pressed && enabled) {
//			hot = true;
//		}
//	}
//
//	if (hot) {
//		switch (direction) {
//			case SWT.UP:    return OS.ABS_UPHOT;
//			case SWT.DOWN:  return OS.ABS_DOWNHOT;
//			case SWT.LEFT:  return OS.ABS_LEFTHOT;
//			case SWT.RIGHT: return OS.ABS_RIGHTHOT;
//		}
//	}
//
//	if (pressed) {
//		switch (direction) {
//			case SWT.UP:    return OS.ABS_UPPRESSED;
//			case SWT.DOWN:  return OS.ABS_DOWNPRESSED;
//			case SWT.LEFT:  return OS.ABS_LEFTPRESSED;
//			case SWT.RIGHT: return OS.ABS_RIGHTPRESSED;
//		}
//	}
//
//	if (!enabled) {
//		switch (direction) {
//			case SWT.UP:    return OS.ABS_UPDISABLED;
//			case SWT.DOWN:  return OS.ABS_DOWNDISABLED;
//			case SWT.LEFT:  return OS.ABS_LEFTDISABLED;
//			case SWT.RIGHT: return OS.ABS_RIGHTDISABLED;
//		}
//	}
//
//	switch (direction) {
//		case SWT.UP:    return OS.ABS_UPNORMAL;
//		case SWT.DOWN:  return OS.ABS_DOWNNORMAL;
//		case SWT.LEFT:  return OS.ABS_LEFTNORMAL;
//		case SWT.RIGHT: return OS.ABS_RIGHTNORMAL;
//	}
//
//	// Have some sane value if all else fails
//	return OS.ABS_LEFTNORMAL;
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	return -1;
}

}

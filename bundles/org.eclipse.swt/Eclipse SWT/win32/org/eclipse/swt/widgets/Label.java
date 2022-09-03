/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
 *     Paul Pazderski - Bug 205199: setImage(null) on Label overrides text
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent a non-selectable
 * user interface object that displays a string or image.
 * When SEPARATOR is specified, displays a single
 * vertical or horizontal line.
 * <p>
 * Shadow styles are hints and may not be honored
 * by the platform.  To create a separator label
 * with the default shadow style for the platform,
 * do not specify a shadow style.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SEPARATOR, HORIZONTAL, VERTICAL</dd>
 * <dd>SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dd>CENTER, LEFT, RIGHT, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of SHADOW_IN, SHADOW_OUT and SHADOW_NONE may be specified.
 * SHADOW_NONE is a HINT. Only one of HORIZONTAL and VERTICAL may be specified.
 * Only one of CENTER, LEFT and RIGHT may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#label">Label snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Label extends Control {
	String text = "";
	Image image;
	boolean isImageMode;	// Resolves ambiguity when both image and text are set
	static final int MARGIN = 4;
	static final long LabelProc;
	static final TCHAR LabelClass = new TCHAR (0, "STATIC", true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, LabelClass, lpWndClass);
		LabelProc = lpWndClass.lpfnWndProc;
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
 * @see SWT#SEPARATOR
 * @see SWT#HORIZONTAL
 * @see SWT#VERTICAL
 * @see SWT#SHADOW_IN
 * @see SWT#SHADOW_OUT
 * @see SWT#SHADOW_NONE
 * @see SWT#CENTER
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#WRAP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Label (Composite parent, int style) {
	super (parent, checkStyle (style));
}

@Override
long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
	if (handle == 0) return 0;
	/*
	* Feature in Windows 7.  When the user double clicks
	* on the label, the text of the label is copied to the
	* clipboard.  This is unwanted. The fix is to avoid
	* calling the label window proc.
	*/
	switch (msg) {
		case OS.WM_LBUTTONDBLCLK: return OS.DefWindowProc (hwnd, msg, wParam, lParam);
	}
	return OS.CallWindowProc (LabelProc, hwnd, msg, wParam, lParam);
}

static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
	if ((style & SWT.SEPARATOR) != 0) {
		style = checkBits (style, SWT.VERTICAL, SWT.HORIZONTAL, 0, 0, 0, 0);
		return checkBits (style, SWT.SHADOW_OUT, SWT.SHADOW_IN, SWT.SHADOW_NONE, 0, 0, 0);
	}
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

@Override Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0, border = getBorderWidthInPixels ();
	if ((style & SWT.SEPARATOR) != 0) {
		int lineWidth = OS.GetSystemMetrics (OS.SM_CXBORDER);
		if ((style & SWT.HORIZONTAL) != 0) {
			width = DEFAULT_WIDTH;  height = lineWidth * 2;
		} else {
			width = lineWidth * 2; height = DEFAULT_HEIGHT;
		}
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		width += border * 2; height += border * 2;
		return new Point (width, height);
	}
	if (isImageMode) {
		Rectangle rect = image.getBoundsInPixels();
		width += rect.width;
		height += rect.height;
	} else {
		long hDC = OS.GetDC (handle);
		long newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		long oldFont = OS.SelectObject (hDC, newFont);
		int length = OS.GetWindowTextLength (handle);
		if (length == 0) {
			TEXTMETRIC tm = new TEXTMETRIC ();
			OS.GetTextMetrics (hDC, tm);
			height = Math.max (height, tm.tmHeight);
		} else {
			RECT rect = new RECT ();
			int flags = OS.DT_CALCRECT | OS.DT_EDITCONTROL | OS.DT_EXPANDTABS;
			if ((style & SWT.WRAP) != 0 && wHint != SWT.DEFAULT) {
				flags |= OS.DT_WORDBREAK;
				rect.right = Math.max (0, wHint - width);
			}
			char [] buffer = new char [length + 1];
			OS.GetWindowText (handle, buffer, length + 1);
			OS.DrawText (hDC, buffer, length, rect, flags);
			width += rect.right - rect.left;
			height = Math.max (height, rect.bottom - rect.top);
		}
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	width += border * 2;
	height += border * 2;
	return new Point (width, height);
}

@Override
void createHandle () {
	super.createHandle ();
	state |= THEME_BACKGROUND;
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is a <code>SEPARATOR</code> label, in
 * which case, <code>NONE</code> is returned.
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
	if ((style & SWT.SEPARATOR) != 0) return 0;
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
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

@Override
String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set or if the receiver is
 * a <code>SEPARATOR</code> label.
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
	if ((style & SWT.SEPARATOR) != 0) return "";
	return text;
}

@Override
boolean isUseWsBorder () {
	return super.isUseWsBorder () || ((display != null) && display.useWsBorderLabel);
}

@Override
boolean mnemonicHit (char key) {
	Control control = this;
	while (control.parent != null) {
		Control [] children = control.parent._getChildren ();
		int index = 0;
		while (index < children.length) {
			if (children [index] == control) break;
			index++;
		}
		index++;
		if (index < children.length) {
			if (children [index].setFocus ()) return true;
		}
		control = control.parent;
	}
	return false;
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
	text = null;
	image = null;
}

@Override
int resolveTextDirection() {
	return (style & SWT.SEPARATOR) != 0 ? SWT.NONE : BidiUtil.resolveTextDirection (text);
}

/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.  If the receiver is a <code>SEPARATOR</code>
 * label, the argument is ignored and the alignment is not changed.
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
	if ((style & SWT.SEPARATOR) != 0) return;
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	updateStyleBits(getEnabled());
	OS.InvalidateRect (handle, null, true);
}

@Override
public void setEnabled (boolean enabled) {
	if ((style & SWT.SEPARATOR) != 0) return;
	/*
	 * Style may need to be changed if Display#disabledLabelForegroundPixel
	 * is active. At the same time, #setEnabled() will cause a repaint with
	 * current style. Therefore, style needs to be changed before #setEnabled().
	 * Note that adding redraw() after #setEnabled() is a worse solution
	 * because it still causes brief old style painting in #setEnabled().
	 */
	updateStyleBits(enabled);

	super.setEnabled(enabled);
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
	if ((style & SWT.SEPARATOR) != 0) return;
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	this.image = image;
	isImageMode = (image != null);
	updateStyleBits(getEnabled());
	OS.InvalidateRect (handle, null, true);
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the widget label.  The label may include
 * the mnemonic character and line delimiters.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, focus is assigned
 * to the control that follows the label. On most platforms,
 * the mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 * '&amp;' can be escaped by doubling it in the string, causing
 * a single '&amp;' to be displayed.
 * </p>
 * <p>
 * Note: If control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
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
	if ((style & SWT.SEPARATOR) != 0) return;
	isImageMode = false;
	updateStyleBits(getEnabled());
	/*
	* Feature in Windows.  For some reason, SetWindowText() for
	* static controls redraws the control, even when the text has
	* has not changed.  The fix is to check for this case and do
	* nothing.
	*/
	if (string.equals (text)) return;
	text = string;
	string = Display.withCrLf (string);
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	OS.SetWindowText (handle, buffer);
	if ((state & HAS_AUTO_DIRECTION) != 0) {
		updateTextDirection (AUTO_TEXT_DIRECTION);
	}
}

void updateStyleBits(boolean isEnabled) {
	boolean useOwnerDraw = isImageMode;

	if (!useOwnerDraw && (display.disabledLabelForegroundPixel != -1) && !isEnabled)
		useOwnerDraw = true;

	int oldBits = OS.GetWindowLong(handle, OS.GWL_STYLE);

	int newBits = oldBits;
	newBits &= ~OS.SS_OWNERDRAW;
	newBits &= ~(OS.SS_LEFTNOWORDWRAP | OS.SS_CENTER | OS.SS_RIGHT);

	if (useOwnerDraw) {
		newBits |= OS.SS_OWNERDRAW;
	} else {
		if ((style & SWT.LEFT) != 0) {
			if ((style & SWT.WRAP) != 0) {
				newBits |= OS.SS_LEFT;
			} else {
				newBits |= OS.SS_LEFTNOWORDWRAP;
			}
		}
		if ((style & SWT.CENTER) != 0) newBits |= OS.SS_CENTER;
		if ((style & SWT.RIGHT) != 0) newBits |= OS.SS_RIGHT;
	}

	if (oldBits != newBits) OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
}

@Override
int widgetExtStyle () {
	int bits = super.widgetExtStyle () & ~OS.WS_EX_CLIENTEDGE;
	if ((style & SWT.BORDER) != 0) return bits | OS.WS_EX_STATICEDGE;
	return bits;
}

@Override
int widgetStyle () {
	int bits = super.widgetStyle () | OS.SS_NOTIFY;
	if ((style & SWT.SEPARATOR) != 0) return bits | OS.SS_OWNERDRAW;
	if ((style & SWT.WRAP) != 0) bits |= OS.SS_EDITCONTROL;
	if ((style & SWT.CENTER) != 0) return bits | OS.SS_CENTER;
	if ((style & SWT.RIGHT) != 0) return bits | OS.SS_RIGHT;
	if ((style & SWT.WRAP) != 0) return bits | OS.SS_LEFT;
	return bits | OS.SS_LEFTNOWORDWRAP;
}

@Override
TCHAR windowClass () {
	return LabelClass;
}

@Override
long windowProc () {
	return LabelProc;
}

@Override
LRESULT WM_ERASEBKGND (long wParam, long lParam) {
	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
	if (result != null) return result;
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.SS_OWNERDRAW) == OS.SS_OWNERDRAW) {
		return LRESULT.ONE;
	}
	return result;
}

@Override
LRESULT WM_SIZE (long wParam, long lParam) {
	LRESULT result = super.WM_SIZE (wParam, lParam);
	if (isDisposed ()) return result;
	if ((style & SWT.SEPARATOR) != 0) {
		OS.InvalidateRect (handle, null, true);
		return result;
	}
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.SS_OWNERDRAW) == OS.SS_OWNERDRAW) {
		OS.InvalidateRect (handle, null, true);
		return result;
	}
	/*
	* Bug in Windows.  For some reason, a label with
	* style SS_LEFT, SS_CENTER or SS_RIGHT does not
	* redraw the text in the new position when resized.
	* Note that SS_LEFTNOWORDWRAP does not have the
	* problem.  The fix is to force the redraw.
	*/
	if ((bits & OS.SS_LEFTNOWORDWRAP) != OS.SS_LEFTNOWORDWRAP) {
		OS.InvalidateRect (handle, null, true);
		return result;
	}
	return result;
}

@Override
LRESULT WM_UPDATEUISTATE (long wParam, long lParam) {
	LRESULT result = super.WM_UPDATEUISTATE (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  When WM_UPDATEUISTATE is sent to
	* a static control, it sends WM_CTLCOLORSTATIC to get the
	* foreground and background.  If any drawing happens in
	* WM_CTLCOLORSTATIC, it overwrites the contents of the control.
	* The fix is draw the static without drawing the background
	* and avoid the static window proc.
	*/
	boolean redraw = findImageControl () != null;
	if (!redraw) {
		if ((state & THEME_BACKGROUND) != 0) {
			if (OS.IsAppThemed ()) {
				redraw = findThemeControl () != null;
			}
		}
	}
	if (redraw) {
		OS.InvalidateRect (handle, null, false);
		long code = OS.DefWindowProc (handle, OS.WM_UPDATEUISTATE, wParam, lParam);
		return new LRESULT (code);
	}
	return result;
}

void wmDrawChildSeparator(DRAWITEMSTRUCT struct) {
	if ((style & SWT.SHADOW_NONE) != 0) return;

	RECT rect = new RECT ();
	int lineWidth = OS.GetSystemMetrics (OS.SM_CXBORDER);
	int flags = (style & SWT.SHADOW_IN) != 0 ? OS.EDGE_SUNKEN : OS.EDGE_ETCHED;
	if ((style & SWT.HORIZONTAL) != 0) {
		int bottom = struct.top + Math.max (lineWidth * 2, (struct.bottom - struct.top) / 2);
		OS.SetRect (rect, struct.left, struct.top, struct.right, bottom);
		OS.DrawEdge (struct.hDC, rect, flags, OS.BF_BOTTOM);
	} else {
		int right = struct.left + Math.max (lineWidth * 2, (struct.right - struct.left) / 2);
		OS.SetRect (rect, struct.left, struct.top, right, struct.bottom);
		OS.DrawEdge (struct.hDC, rect, flags, OS.BF_RIGHT);
	}
}

void wmDrawChildImage(DRAWITEMSTRUCT struct) {
	int width = struct.right - struct.left;
	int height = struct.bottom - struct.top;
	if (width == 0 || height == 0) return;

	Rectangle imageRect = image.getBoundsInPixels ();

	int x = 0;
	if ((style & SWT.CENTER) != 0) {
		x = Math.max (0, (width - imageRect.width) / 2);
	} else if ((style & SWT.RIGHT) != 0) {
		x = width - imageRect.width;
	}

	GCData data = new GCData();
	data.device = display;
	GC gc = GC.win32_new (struct.hDC, data);
	Image image = getEnabled () ? this.image : new Image (display, this.image, SWT.IMAGE_DISABLE);
	gc.drawImage (image, DPIUtil.autoScaleDown(x), DPIUtil.autoScaleDown(Math.max (0, (height - imageRect.height) / 2)));
	if (image != this.image) image.dispose ();
	gc.dispose ();
}

void wmDrawChildText(DRAWITEMSTRUCT struct) {
	int width = struct.right - struct.left;
	int height = struct.bottom - struct.top;
	if (width == 0 || height == 0) return;

	RECT rect = new RECT ();
	rect.left = struct.left;
	rect.top = struct.top;
	rect.right = struct.right;
	rect.bottom = struct.bottom;

	int flags = OS.DT_EDITCONTROL | OS.DT_EXPANDTABS;
	if ((style & SWT.LEFT) != 0)   flags |= OS.DT_LEFT;
	if ((style & SWT.CENTER) != 0) flags |= OS.DT_CENTER;
	if ((style & SWT.RIGHT) != 0)  flags |= OS.DT_RIGHT;
	if ((style & SWT.WRAP) != 0)   flags |= OS.DT_WORDBREAK;

	// Mnemonics are usually not shown on Labels until Alt is pressed.
	long uiState = OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
	if ((uiState & OS.UISF_HIDEACCEL) != 0)
		flags |= OS.DT_HIDEPREFIX;

	if (!getEnabled()) {
		int foregroundPixel = OS.GetSysColor(OS.COLOR_GRAYTEXT);
		if (display.disabledLabelForegroundPixel != -1)
			foregroundPixel = display.disabledLabelForegroundPixel;

		OS.SetTextColor(struct.hDC, foregroundPixel);
	}

	char [] buffer = text.toCharArray ();
	OS.DrawText (struct.hDC, buffer, buffer.length, rect, flags);
}

@Override
LRESULT wmDrawChild (long wParam, long lParam) {
	DRAWITEMSTRUCT struct = new DRAWITEMSTRUCT ();
	OS.MoveMemory (struct, lParam, DRAWITEMSTRUCT.sizeof);
	drawBackground (struct.hDC);
	if ((style & SWT.SEPARATOR) != 0)
		wmDrawChildSeparator(struct);
	else if (isImageMode)
		wmDrawChildImage(struct);
	else
		wmDrawChildText(struct);

	return null;
}

}

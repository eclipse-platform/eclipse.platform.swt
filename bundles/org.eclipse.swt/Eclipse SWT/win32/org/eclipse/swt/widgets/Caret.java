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
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class provide an i-beam that is typically used
 * as the insertion point for text.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#caret">Caret snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Canvas tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Caret extends Widget {
	/** The Caret last updated on the OS-level */
	private static Caret currentCaret;

	Canvas parent;
	int x, y, width, height;
	boolean moved, resized;
	boolean isVisible;
	Image image;
	Font font;
	LOGFONT oldFont;

static {
	DPIZoomChangeRegistry.registerHandler(Caret::handleDPIChange, Caret.class);
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Caret (Canvas parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget ();
}

void createWidget () {
	isVisible = true;
	if (parent.getCaret () == null) {
		parent.setCaret (this);
	}
}

long defaultFont () {
	long hwnd = parent.handle;
	long hwndIME = OS.ImmGetDefaultIMEWnd (hwnd);
	long hFont = 0;
	if (hwndIME != 0) {
		hFont = OS.SendMessage (hwndIME, OS.WM_GETFONT, 0, 0);
	}
	if (hFont == 0) {
		hFont = OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0);
	}
	if (hFont == 0) return parent.defaultFont ();
	return hFont;
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent (or its display if its parent is null).
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget();
	return DPIUtil.autoScaleDown(getBoundsInPixels());
}

Rectangle getBoundsInPixels () {
	if (image != null) {
		Rectangle rect = image.getBoundsInPixels ();
		return new Rectangle (x, y, rect.width, rect.height);
	}
	if (width == 0) {
		int [] buffer = new int [1];
		if (OS.SystemParametersInfo (OS.SPI_GETCARETWIDTH, 0, buffer, 0)) {
			return new Rectangle (x, y, buffer [0], height);
		}
	}
	return new Rectangle (x, y, width, height);
}

/**
 * Returns the font that the receiver will use to paint textual information.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Font getFont () {
	checkWidget();
	if (font == null) {
		long hFont = defaultFont ();
		return Font.win32_new (display, hFont, getZoom());
	}
	return font;
}

/**
 * Returns the image that the receiver will use to paint the caret.
 *
 * @return the receiver's image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage () {
	checkWidget();
	return image;
}

/**
 * Returns a point describing the receiver's location relative
 * to its parent (or its display if its parent is null).
 *
 * @return the receiver's location
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getLocation () {
	checkWidget();
	return DPIUtil.autoScaleDown(getLocationInPixels());
}

Point getLocationInPixels () {
	return new Point (x, y);
}

/**
 * Returns the receiver's parent, which must be a <code>Canvas</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Canvas getParent () {
	checkWidget();
	return parent;
}

/**
 * Returns a point describing the receiver's size.
 *
 * @return the receiver's size
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSize () {
	checkWidget();
	return DPIUtil.autoScaleDown(getSizeInPixels());
}

Point getSizeInPixels () {
	if (image != null) {
		Rectangle rect = image.getBoundsInPixels ();
		return new Point (rect.width, rect.height);
	}
	if (width == 0) {
		int [] buffer = new int [1];
		if (OS.SystemParametersInfo (OS.SPI_GETCARETWIDTH, 0, buffer, 0)) {
			return new Point (buffer [0], height);
		}
	}
	return new Point (width, height);
}

/**
 * Returns <code>true</code> if the receiver is visible, and
 * <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget();
	return isVisible;
}

boolean hasFocus () {
	return parent.handle == OS.GetFocus ();
}

boolean isFocusCaret () {
	return parent.caret == this && hasFocus ();
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * of the receiver's ancestors are visible and <code>false</code>
 * otherwise.
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getVisible
 */
public boolean isVisible () {
	checkWidget();
	return isVisible && parent.isVisible () && hasFocus ();
}

void killFocus () {
	OS.DestroyCaret ();
	restoreIMEFont ();
}

void move () {
	moved = false;
	setCurrentCaret(this);
	if (!OS.SetCaretPos (x, y)) return;
	resizeIME ();
}

void resizeIME () {
	if (!OS.IsDBLocale) return;
	POINT ptCurrentPos = new POINT ();
	if (!OS.GetCaretPos (ptCurrentPos)) return;
	long hwnd = parent.handle;
	long hIMC = OS.ImmGetContext (hwnd);
	IME ime = parent.getIME ();
	if (ime != null && ime.isInlineEnabled ()) {
		Point size = getSizeInPixels ();
		CANDIDATEFORM lpCandidate = new CANDIDATEFORM ();
		lpCandidate.dwStyle = OS.CFS_EXCLUDE;
		lpCandidate.ptCurrentPos = ptCurrentPos;
		lpCandidate.rcArea = new RECT ();
		OS.SetRect (lpCandidate.rcArea, ptCurrentPos.x, ptCurrentPos.y, ptCurrentPos.x + size.x, ptCurrentPos.y + size.y);
		OS.ImmSetCandidateWindow (hIMC, lpCandidate);
	} else {
		RECT rect = new RECT ();
		OS.GetClientRect (hwnd, rect);
		COMPOSITIONFORM lpCompForm = new COMPOSITIONFORM ();
		lpCompForm.dwStyle = OS.CFS_RECT;
		lpCompForm.x = ptCurrentPos.x;
		lpCompForm.y = ptCurrentPos.y;
		lpCompForm.left = rect.left;
		lpCompForm.right = rect.right;
		lpCompForm.top = rect.top;
		lpCompForm.bottom = rect.bottom;
		OS.ImmSetCompositionWindow (hIMC, lpCompForm);
	}
	OS.ImmReleaseContext (hwnd, hIMC);
}

@Override
void releaseParent () {
	super.releaseParent ();
	if (parent != null && this == parent.caret) {
		if (!parent.isDisposed()) parent.setCaret (null);
		else parent.caret = null;
	}
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (isCurrentCaret()) {
		setCurrentCaret(null);
	}
	parent = null;
	image = null;
	font = null;
	oldFont = null;
}

void resize () {
	resized = false;
	long hwnd = parent.handle;
	OS.DestroyCaret ();
	long hBitmap = image != null ? image.handle : 0;
	int width = this.width;
	if (image == null && width == 0) {
		int [] buffer = new int [1];
		if (OS.SystemParametersInfo (OS.SPI_GETCARETWIDTH, 0, buffer, 0)) {
			width = buffer [0];
		}
	}
	OS.CreateCaret (hwnd, hBitmap, width, height);
	OS.SetCaretPos (x, y);
	OS.ShowCaret (hwnd);
	move ();
}

void restoreIMEFont () {
	if (!OS.IsDBLocale) return;
	if (oldFont == null) return;
	long hwnd = parent.handle;
	long hIMC = OS.ImmGetContext (hwnd);
	OS.ImmSetCompositionFont (hIMC, oldFont);
	OS.ImmReleaseContext (hwnd, hIMC);
	oldFont = null;
}

/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the arguments. The <code>x</code> and
 * <code>y</code> arguments are relative to the receiver's
 * parent (or its display if its parent is null).
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 * @param width the new width for the receiver
 * @param height the new height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBounds (int x, int y, int width, int height) {
	checkWidget();
	setBoundsInPixels(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y), DPIUtil.autoScaleUp(width), DPIUtil.autoScaleUp(height));
}

void setBoundsInPixels (int x, int y, int width, int height) {
	boolean samePosition = this.x == x && this.y == y;
	boolean sameExtent = this.width == width && this.height == height;
	if (samePosition && sameExtent && isCurrentCaret()) return;
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	if (sameExtent) {
		moved = true;
		if (isVisible && hasFocus ()) move ();
	} else {
		resized = true;
		if (isVisible && hasFocus ()) resize ();
	}
}

/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the argument. The <code>x</code> and
 * <code>y</code> fields of the rectangle are relative to
 * the receiver's parent (or its display if its parent is null).
 *
 * @param rect the new bounds for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBounds (Rectangle rect) {
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBoundsInPixels(DPIUtil.autoScaleUp(rect));
}

void setBoundsInPixels (Rectangle rect) {
	setBoundsInPixels (rect.x, rect.y, rect.width, rect.height);
}

void setFocus () {
	long hwnd = parent.handle;
	long hBitmap = 0;
	if (image != null) hBitmap = image.handle;
	int width = this.width;
	if (image == null && width == 0) {
		int [] buffer = new int [1];
		if (OS.SystemParametersInfo (OS.SPI_GETCARETWIDTH, 0, buffer, 0)) {
			width = buffer [0];
		}
	}
	OS.CreateCaret (hwnd, hBitmap, width, height);
	move ();
	setIMEFont ();
	if (isVisible) OS.ShowCaret (hwnd);
}

/**
 * Sets the font that the receiver will use to paint textual information
 * to the font specified by the argument, or to the default font for that
 * kind of control if the argument is null.
 *
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the font has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setFont (Font font) {
	checkWidget();
	if (font != null && font.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Shell shell = parent.getShell();
	this.font = font == null ? null : Font.win32_new(font, shell.nativeZoom);
	if (hasFocus ()) setIMEFont ();
}

/**
 * Sets the image that the receiver will use to paint the caret
 * to the image specified by the argument, or to the default
 * which is a filled rectangle if the argument is null
 *
 * @param image the new image (or null)
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
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	this.image = image;
	if (isVisible && hasFocus ()) resize ();
}

void setIMEFont () {
	if (!OS.IsDBLocale) return;
	long hFont = 0;
	if (font != null) hFont = font.handle;
	if (hFont == 0) hFont = defaultFont ();
	long hwnd = parent.handle;
	long hIMC = OS.ImmGetContext (hwnd);
	/* Save the current IME font */
	if (oldFont == null) {
		oldFont = new LOGFONT ();
		if (!OS.ImmGetCompositionFont (hIMC, oldFont)) oldFont = null;
	}
	/* Set new IME font */
	LOGFONT logFont = new LOGFONT ();
	if (OS.GetObject (hFont, LOGFONT.sizeof, logFont) != 0) {
		OS.ImmSetCompositionFont (hIMC, logFont);
	}
	OS.ImmReleaseContext (hwnd, hIMC);
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null).
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (int x, int y) {
	checkWidget();
	setLocationInPixels(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y));
}

void setLocationInPixels (int x, int y) {
	if (this.x == x && this.y == y && isCurrentCaret())  return;
	this.x = x;  this.y = y;
	moved = true;
	if (isVisible && hasFocus ()) move ();
}

private boolean isCurrentCaret() {
	return Caret.currentCaret == this;
}

private void setCurrentCaret(Caret caret) {
	Caret.currentCaret = caret;
}

/**
 * Sets the receiver's location to the point specified by
 * the argument which is relative to the receiver's
 * parent (or its display if its parent is null).
 *
 * @param location the new location for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (Point location) {
	checkWidget();
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	location = DPIUtil.autoScaleUp(location);
	setLocationInPixels(location.x, location.y);
}

/**
 * Sets the receiver's size to the point specified by the arguments.
 *
 * @param width the new width for the receiver
 * @param height the new height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (int width, int height) {
	checkWidget();
	setSizeInPixels(DPIUtil.autoScaleUp(width), DPIUtil.autoScaleUp(height));
}

void setSizeInPixels (int width, int height) {
	if (this.width == width && this.height == height && isCurrentCaret()) return;
	this.width = width;  this.height = height;
	resized = true;
	if (isVisible && hasFocus ()) resize ();
}

/**
 * Sets the receiver's size to the point specified by the argument.
 *
 * @param size the new extent for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (Point size) {
	checkWidget();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	size = DPIUtil.autoScaleUp(size);
	setSizeInPixels(size.x, size.y);
}

/**
 * Marks the receiver as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
	checkWidget();
	if (visible == isVisible) return;
	isVisible = visible;
	long hwnd = parent.handle;
	if (OS.GetFocus () != hwnd) return;
	if (!isVisible) {
		OS.HideCaret (hwnd);
	} else {
		if (resized) {
			resize ();
		} else {
			if (moved) move ();
		}
		OS.ShowCaret (hwnd);
	}
}

private static void handleDPIChange(Widget widget, int newZoom, float scalingFactor) {
	if (!(widget instanceof Caret caret)) {
		return;
	}

	Image image = caret.getImage();
	if (image != null) {
		caret.setImage(Image.win32_new(image, newZoom));
	}

	if (caret.font != null) {
		caret.setFont(caret.font);
	}
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.swing.LookAndFeelUtils;

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
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */

public class Caret extends Widget {
	Canvas parent;
	int x, y, width, height;
	boolean moved, resized;
	boolean isVisible;
	Image image;
	Font font;

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

Timer timer = new Timer (LookAndFeelUtils.getCaretBlinkRate(), new ActionListener () {
  public void actionPerformed (ActionEvent e) {
    Caret.this.blink = !Caret.this.blink;
    if (Caret.this.parent != null && Caret.this.parent.caret == Caret.this) {
      // XXX redraw rect is not correct. for now, redraw all.
      // Rectangle r = Caret.this.getBounds();
      // Caret.this.parent.redraw(x, r.y, r.width, r.height, true);
      // redraw causes GC to throw exceptions in some conditions. Let's directly call the Swing component
      Caret.this.parent.handle.repaint();
    } else {
      timer.stop();
    }
  }
}) {
  public void stop() {
    super.stop();
    blink = true;
    if(Caret.this.parent != null) {
//      Caret.this.parent.redraw ();
      Caret.this.parent.handle.repaint();
    }
  }
};

private boolean blink;

void paintCaret (GC gc) {
  if (blink && isVisible()) {
    if(display == null) return;
    gc.setXORMode (true);
    gc.setBackground (display.getSystemColor (SWT.COLOR_BLACK));
    if (image != null) {
      gc.drawImage (image, x, y);
    }
    gc.fillRectangle (x, y, Math.max(1, width), height);
    gc.setXORMode (false);
  }
}

java.awt.Font defaultFont () {
  return parent.handle.getFont ();
//  int hwnd = parent.handle;
//  int hwndIME = OS.ImmGetDefaultIMEWnd (hwnd);
//  int hFont = 0;
//  if (hwndIME != 0) {
//    hFont = OS.SendMessage (hwndIME, OS.WM_GETFONT, 0, 0);
//  }
//  if (hFont == 0) {
//    hFont = OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0);
//  }
//  if (hFont == 0) return parent.defaultFont ();
//  return hFont;
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
	if (image != null) {
		Rectangle rect = image.getBounds ();
		return new Rectangle (x, y, rect.width, rect.height);
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
  checkWidget ();
  if (font == null) {
    return Font.swing_new (display, defaultFont ());
  }
  return font;
//  checkWidget();
//  if (font == null) {
//    int hFont = defaultFont ();
//    return Font.win32_new (display, hFont);
//  }
//  return font;
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
	if (image != null) {
		Rectangle rect = image.getBounds ();
		return new Point (rect.width, rect.height);
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
  return parent.isFocusControl();
//  return parent.handle == OS.GetFocus ();
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
  timer.stop ();
//	OS.DestroyCaret ();
//	if (font != null) restoreIMEFont ();
}

void move () {
//	moved = false;
//	if (!OS.SetCaretPos (x, y)) return;
//	resizeIME ();
  blink = true;
}

void resizeIME () {
//TODO
//	if (!OS.IsDBLocale) return;
//	POINT ptCurrentPos = new POINT ();
//	if (!OS.GetCaretPos (ptCurrentPos)) return;
//	int hwnd = parent.handle;
//	RECT rect = new RECT ();
//	OS.GetClientRect (hwnd, rect);
//	COMPOSITIONFORM lpCompForm = new COMPOSITIONFORM ();
//	lpCompForm.dwStyle = OS.CFS_RECT;
//	lpCompForm.x = ptCurrentPos.x;
//	lpCompForm.y = ptCurrentPos.y;
//	lpCompForm.left = rect.left;
//	lpCompForm.right = rect.right;
//	lpCompForm.top = rect.top;
//	lpCompForm.bottom = rect.bottom;
//	int hIMC = OS.ImmGetContext (hwnd);
//	OS.ImmSetCompositionWindow (hIMC, lpCompForm);
//	OS.ImmReleaseContext (hwnd, hIMC);
}

void releaseParent () {
	super.releaseParent ();
	if (this == parent.getCaret ()) parent.setCaret (null);
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
	image = null;
	font = null;
//	oldFont = null;
}

void resize () {
// TODO
//	resized = false;
//	int hwnd = parent.handle;
//	OS.DestroyCaret ();		
//	int hBitmap = image != null ? image.handle : 0;
//	OS.CreateCaret (hwnd, hBitmap, width, height);
//	OS.SetCaretPos (x, y);
//	OS.ShowCaret (hwnd);
//	move ();
}

void restoreIMEFont () {
//TODO
//	if (!OS.IsDBLocale) return;
//	if (oldFont == null) return;
//	int hwnd = parent.handle;
//	int hIMC = OS.ImmGetContext (hwnd);
//	OS.ImmSetCompositionFont (hIMC, oldFont);
//	OS.ImmReleaseContext (hwnd, hIMC);
//	oldFont = null;
}

void saveIMEFont () {
//TODO
//	if (!OS.IsDBLocale) return;
//	if (oldFont != null) return;
//	int hwnd = parent.handle;
//	int hIMC = OS.ImmGetContext (hwnd);
//	oldFont = OS.IsUnicode ? (LOGFONT) new LOGFONTW () : new LOGFONTA ();
//	if (!OS.ImmGetCompositionFont (hIMC, oldFont)) oldFont = null;
//	OS.ImmReleaseContext (hwnd, hIMC);
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
	boolean samePosition = this.x == x && this.y == y;
	boolean sameExtent = this.width == width && this.height == height;
	if (samePosition && sameExtent) return;
  timer.stop();
	this.x = x;  this.y = y;
	this.width = width;  this.height = height;
	if (sameExtent) {
		moved = true;
		if (isVisible && hasFocus ()) move ();
	} else {
		resized = true;
		if (isVisible && hasFocus ()) resize ();
	}
  timer.start();
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
	setBounds (rect.x, rect.y, rect.width, rect.height);
}

void setFocus () {
  timer.start ();
//TODO
//	int hwnd = parent.handle;
//	int hBitmap = 0;
//	if (image != null) hBitmap = image.handle;
//	OS.CreateCaret (hwnd, hBitmap, width, height);
//	move ();
//	if (OS.IsDBLocale) {
//		int hFont = 0;
//		if (font != null) hFont = font.handle;
//		if (hFont == 0) hFont = defaultFont ();
//		saveIMEFont ();
//		setIMEFont (hFont);
//	}
//	if (isVisible) OS.ShowCaret (hwnd);
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
//TODO
//	checkWidget();
//	if (font != null && font.isDisposed ()) {
//		error (SWT.ERROR_INVALID_ARGUMENT);
//	}
//	this.font = font;
//	if (isVisible && hasFocus ()) {
//		int hFont = 0;
//		if (font != null) hFont = font.handle;
//		if (hFont == 0) hFont = defaultFont ();
//		saveIMEFont ();
//		setIMEFont (hFont);
//	}
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

void setIMEFont (int hFont) {
//TODO
//	if (!OS.IsDBLocale) return;
//	LOGFONT logFont = OS.IsUnicode ? (LOGFONT) new LOGFONTW () : new LOGFONTA ();
//	if (OS.GetObject (hFont, LOGFONT.sizeof, logFont) != 0) {
//		int hwnd = parent.handle;
//		int hIMC = OS.ImmGetContext (hwnd);
//		OS.ImmSetCompositionFont (hIMC, logFont);
//		OS.ImmReleaseContext (hwnd, hIMC);
//	}
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
	if (this.x == x && this.y == y) return;
  timer.stop();
	this.x = x;  this.y = y;
	moved = true;
	if (isVisible && hasFocus ()) move ();
  timer.start();
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
	setLocation (location.x, location.y);
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
	if (this.width == width && this.height == height) return;
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
	setSize (size.x, size.y);
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
  checkWidget ();
  if (visible == isVisible) return;
  isVisible = visible;
//TODO
//	checkWidget();
//	if (visible == isVisible) return;
//	isVisible = visible;
//	int hwnd = parent.handle;
//	if (OS.GetFocus () != hwnd) return;
//	if (!isVisible) {
//		OS.HideCaret (hwnd);
//	} else {
//		if (resized) {
//			resize ();
//		} else {
//			if (moved) move ();
//		}
//		OS.ShowCaret (hwnd);
//	}
  // TODO activate or deactivate caret timer.
}

}

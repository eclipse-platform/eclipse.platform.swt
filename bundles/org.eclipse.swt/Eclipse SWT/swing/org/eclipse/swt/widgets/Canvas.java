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


import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.ImageIcon;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.swing.CControl;
import org.eclipse.swt.internal.swing.Utils;

/**
 * Instances of this class provide a surface for drawing
 * arbitrary graphics.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are <em>not</em> constructed
 * from aggregates of other controls. That is, they are either
 * painted using SWT graphics calls or are handled by native
 * methods.
 * </p>
 *
 * @see Composite
 */

public class Canvas extends Composite {
	Caret caret;
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Canvas () {
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
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Canvas (Composite parent, int style) {
	super (parent, style);
}

///*
//* Not currently used.
//*/
//void clearArea (int x, int y, int width, int height) {
//	checkWidget ();
//	if (OS.IsWindowVisible (handle)) return;
//	RECT rect = new RECT ();
//	OS.SetRect (rect, x, y, x + width, y + height);
//	int hDC = OS.GetDCEx (handle, 0, OS.DCX_CACHE | OS.DCX_CLIPCHILDREN | OS.DCX_CLIPSIBLINGS);
//	drawBackground (hDC, rect);
//	OS.ReleaseDC (handle, hDC);
//}

/**
 * Returns the caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 *
 * @return the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Caret getCaret () {
	checkWidget ();
	return caret;
}

void releaseChildren (boolean destroy) {
  if (caret != null) {
    caret.release (false);
    caret = null;
  }
  super.releaseChildren (destroy);
}

/** 
 * Fills the interior of the rectangle specified by the arguments,
 * with the receiver's background. 
 *
 * @param gc the gc where the rectangle is to be filled
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void drawBackground (GC gc, int x, int y, int width, int height) {
  checkWidget ();
  if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
  if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
  Graphics2D g = gc.handle.getGraphics();
  if(backgroundImage != null) {
    Shape clip = g.getClip();
    g.clipRect(x, y, width, height);
    Dimension size = handle.getSize();
    Utils.paintTiledImage(g, new ImageIcon(backgroundImage.handle), 0, 0, size.width, size.height);
    g.setClip(clip);
  } else {
    java.awt.Color oldColor = g.getColor();
    g.setColor(getBackground().handle);
    g.fillRect(x, y, width, height);
    g.setColor(oldColor);
  }
}

/**
 * Scrolls a rectangular area of the receiver by first copying 
 * the source area to the destination and then causing the area
 * of the source which is not covered by the destination to
 * be repainted. Children that intersect the rectangle are
 * optionally moved during the operation. In addition, outstanding
 * paint events are flushed before the source area is copied to
 * ensure that the contents of the canvas are drawn correctly.
 *
 * @param destX the x coordinate of the destination
 * @param destY the y coordinate of the destination
 * @param x the x coordinate of the source
 * @param y the y coordinate of the source
 * @param width the width of the area
 * @param height the height of the area
 * @param all <code>true</code>if children should be scrolled, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void scroll (int destX, int destY, int x, int y, int width, int height, boolean all) {
	checkWidget ();
//	forceResize ();
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
//  handle.repaint();
  Container clientArea = ((CControl)handle).getClientArea();
  java.awt.Graphics g = clientArea.getGraphics();
  int dx = destX - x;
  int dy = destY - y;
  g.copyArea(x, y, width, height, dx, dy);
  java.awt.Dimension size = clientArea.getSize();
  if(dx < 0) {
    clientArea.repaint(size.width + dx, 0, -dx, size.height);
  } else if(dx > 0) {
    clientArea.repaint(0, 0, dx, size.height);
  }
  if(dy < 0) {
    clientArea.repaint(0, size.height + dy, size.width, -dy);
  } else if(dy > 0) {
    clientArea.repaint(0, 0, size.width, dy);
  }
//	RECT sourceRect = new RECT ();
//	OS.SetRect (sourceRect, x, y, x + width, y + height);
//	RECT clientRect = new RECT ();
//	OS.GetClientRect (handle, clientRect);
//	if (OS.IntersectRect (clientRect, sourceRect, clientRect)) {
//		if (OS.IsWinCE) {
//			OS.UpdateWindow (handle);
//		} else {
//			int flags = OS.RDW_UPDATENOW | OS.RDW_ALLCHILDREN;
//			OS.RedrawWindow (handle, null, 0, flags);
//		}
//	}
	int deltaX = destX - x, deltaY = destY - y;
//	int flags = OS.SW_INVALIDATE | OS.SW_ERASE;
//	/*
//	* Feature in Windows.  If any child in the widget tree partially
//	* intersects the scrolling rectangle, Windows moves the child
//	* and copies the bits that intersect the scrolling rectangle but
//	* does not redraw the child.
//	* 
//	* Feature in Windows.  When any child in the widget tree does not
//	* intersect the scrolling rectangle but the parent does intersect,
//	* Windows does not move the child.  This is the documented (but
//	* strange) Windows behavior.
//	* 
//	* The fix is to not use SW_SCROLLCHILDREN and move the children
//	* explicitly after scrolling.  
//	*/
////	if (all) flags |= OS.SW_SCROLLCHILDREN;
//	OS.ScrollWindowEx (handle, deltaX, deltaY, sourceRect, null, 0, null, flags);
	if (all) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			Control child = children [i];
			Rectangle rect = child.getBounds ();
			if (Math.min (x + width, rect.x + rect.width) >= Math.max (x, rect.x) && 
				Math.min (y + height, rect.y + rect.height) >= Math.max (y, rect.y)) {
					child.setLocation (rect.x + deltaX, rect.y + deltaY);
			}
		}
	}
	if (isFocus) caret.setFocus ();
}

/**
 * Sets the receiver's caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 * @param caret the new caret for the receiver, may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the caret has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCaret (Caret caret) {
	checkWidget ();
	Caret newCaret = caret;
	Caret oldCaret = this.caret;
	this.caret = newCaret;
	if (isFocusControl ()) {
		if (oldCaret != null) oldCaret.killFocus ();
		if (newCaret != null) {
			if (newCaret.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
			newCaret.setFocus ();
		}
	}
}

public void setFont (Font font) {
	checkWidget ();
	if (caret != null) caret.setFont (font);
	super.setFont (font);
}

//LRESULT WM_INPUTLANGCHANGE (int wParam, int lParam) {
//	LRESULT result  = super.WM_INPUTLANGCHANGE (wParam, lParam);
//	if (caret != null) caret.resizeIME ();
//	return result;
//}
//
//LRESULT WM_KILLFOCUS (int wParam, int lParam) {
//	LRESULT result  = super.WM_KILLFOCUS (wParam, lParam);
//	if (caret != null) caret.killFocus ();
//	return result;
//}
//
//LRESULT WM_SETFOCUS (int wParam, int lParam) {
//	LRESULT result  = super.WM_SETFOCUS (wParam, lParam);
//	if (caret != null) caret.setFocus ();
//	return result;
//}
//
//LRESULT WM_SIZE (int wParam, int lParam) {
//	LRESULT result  = super.WM_SIZE (wParam, lParam);
//	if (caret != null) caret.resizeIME ();
//	return result;
//}
//
//LRESULT WM_WINDOWPOSCHANGED (int wParam, int lParam) {
//	LRESULT result  = super.WM_WINDOWPOSCHANGED (wParam, lParam);
//	if (result != null) return result;
//	/*
//	* Bug in Windows.  When a window with style WS_EX_LAYOUTRTL
//	* that contains a caret is resized, Windows does not move the
//	* caret in relation to the mirrored origin in the top right.
//	* The fix is to hide the caret in WM_WINDOWPOSCHANGING and
//	* show the caret in WM_WINDOWPOSCHANGED.
//	*/
//	boolean isFocus = (style & SWT.RIGHT_TO_LEFT) != 0 && caret != null && caret.isFocusCaret ();
//	if (isFocus) caret.setFocus ();
//	return result;
//}
//
//LRESULT WM_WINDOWPOSCHANGING (int wParam, int lParam) {
//	LRESULT result  = super.WM_WINDOWPOSCHANGING (wParam, lParam);
//	if (result != null) return result;
//	/*
//	* Bug in Windows.  When a window with style WS_EX_LAYOUTRTL
//	* that contains a caret is resized, Windows does not move the
//	* caret in relation to the mirrored origin in the top right.
//	* The fix is to hide the caret in WM_WINDOWPOSCHANGING and
//	* show the caret in WM_WINDOWPOSCHANGED.
//	*/
//	boolean isFocus = (style & SWT.RIGHT_TO_LEFT) != 0 && caret != null && caret.isFocusCaret ();
//	if (isFocus) caret.killFocus ();
//	return result;
//}

}

/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
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
 * @see <a href="http://www.eclipse.org/swt/snippets/#canvas">Canvas snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class Canvas extends Composite {
	Caret caret;
	IME ime;

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
 * @see Widget#getStyle
 */
public Canvas (Composite parent, int style) {
	super (parent, style);
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
	int zoom = getZoom();
	Rectangle rectangle = DPIUtil.scaleUp(new Rectangle(x, y, width, height), zoom);
	drawBackgroundInPixels(gc, rectangle.x, rectangle.y, rectangle.width, rectangle.height, 0, 0);
}

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
 * @return the caret for the receiver, may be null
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

/**
 * Returns the IME.
 *
 * @return the IME
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public IME getIME () {
	checkWidget ();
	return ime;
}

@Override
boolean isUseWsBorder () {
	return super.isUseWsBorder () || ((display != null) && display.useWsBorderCanvas);
}

@Override
void releaseChildren (boolean destroy) {
	if (caret != null) {
		caret.release (false);
		caret = null;
	}
	if (ime != null) {
		ime.release (false);
		ime = null;
	}
	super.releaseChildren (destroy);
}

@Override
void reskinChildren (int flags) {
	if (caret != null) caret.reskin (flags);
	if (ime != null)  ime.reskin (flags);
	super.reskinChildren (flags);
}

/**
 * Scrolls a rectangular area of the receiver by first copying
 * the source area to the destination and then causing the area
 * of the source which is not covered by the destination to
 * be repainted. Children that intersect the rectangle are
 * optionally moved during the operation. In addition, all outstanding
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
	int zoom = getZoom();
	destX = DPIUtil.scaleUp(destX, zoom);
	destY = DPIUtil.scaleUp(destY, zoom);
	Rectangle rectangle = DPIUtil.scaleUp(new Rectangle(x, y, width, height), zoom);
	scrollInPixels(destX, destY, rectangle.x, rectangle.y, rectangle.width, rectangle.height, all);
}

void scrollInPixels (int destX, int destY, int x, int y, int width, int height, boolean all) {
	forceResize ();
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	RECT sourceRect = new RECT ();
	OS.SetRect (sourceRect, x, y, x + width, y + height);
	RECT clientRect = new RECT ();
	OS.GetClientRect (handle, clientRect);
	if (OS.IntersectRect (clientRect, sourceRect, clientRect)) {
		int flags = OS.RDW_UPDATENOW | OS.RDW_ALLCHILDREN;
		OS.RedrawWindow (handle, null, 0, flags);
	}
	int deltaX = destX - x, deltaY = destY - y;
	if (findImageControl () != null) {
		int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
		if (all) flags |= OS.RDW_ALLCHILDREN;
		OS.RedrawWindow (handle, sourceRect, 0, flags);
		OS.OffsetRect (sourceRect, deltaX, deltaY);
		OS.RedrawWindow (handle, sourceRect, 0, flags);
	} else {
		int flags = OS.SW_INVALIDATE | OS.SW_ERASE;
		/*
		* Feature in Windows.  If any child in the widget tree partially
		* intersects the scrolling rectangle, Windows moves the child
		* and copies the bits that intersect the scrolling rectangle but
		* does not redraw the child.
		*
		* Feature in Windows.  When any child in the widget tree does not
		* intersect the scrolling rectangle but the parent does intersect,
		* Windows does not move the child.  This is the documented (but
		* strange) Windows behavior.
		*
		* The fix is to not use SW_SCROLLCHILDREN and move the children
		* explicitly after scrolling.
		*/
//		if (all) flags |= OS.SW_SCROLLCHILDREN;
		OS.ScrollWindowEx (handle, deltaX, deltaY, sourceRect, null, 0, null, flags);
	}
	if (all) {
		for (Control child : _getChildren ()) {
			Rectangle rect = child.getBoundsInPixels ();
			if (Math.min (x + width, rect.x + rect.width) >= Math.max (x, rect.x) &&
				Math.min (y + height, rect.y + rect.height) >= Math.max (y, rect.y)) {
					child.setLocationInPixels (rect.x + deltaX, rect.y + deltaY);
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
	if (hasFocus ()) {
		if (oldCaret != null) oldCaret.killFocus ();
		if (newCaret != null) {
			if (newCaret.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
			newCaret.setFocus ();
		}
	}
}

@Override
public void setFont (Font font) {
	checkWidget ();
	if (caret != null) caret.setFont (font);
	super.setFont (font);
}

/**
 * Sets the receiver's IME.
 *
 * @param ime the new IME for the receiver, may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the IME has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setIME (IME ime) {
	checkWidget ();
	if (ime != null && ime.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	this.ime = ime;
}

@Override
TCHAR windowClass () {
	if (display.useOwnDC) return display.windowOwnDCClass;
	return super.windowClass ();
}

@Override
long windowProc (long hwnd, int msg, long wParam, long lParam) {
	if (msg == Display.SWT_RESTORECARET) {
		if ((state & CANVAS) != 0) {
			if (caret != null) {
				caret.killFocus ();
				caret.setFocus ();
				return 1;
			}
		}
	}
	return super.windowProc (hwnd, msg, wParam, lParam);
}

@Override
LRESULT WM_CHAR (long wParam, long lParam) {
	LRESULT result = super.WM_CHAR (wParam, lParam);
	if (result != null) return result;
	if (caret != null) {
		switch ((int)wParam) {
			case SWT.DEL:
			case SWT.BS:
			case SWT.ESC:
				break;
			default: {
				if (OS.GetKeyState (OS.VK_CONTROL) >= 0) {
					int [] value = new int [1];
					if (OS.SystemParametersInfo (OS.SPI_GETMOUSEVANISH, 0, value, 0)) {
						if (value [0] != 0) OS.SetCursor (0);
					}
				}
			}
		}
	}
	return result;
}

@Override
LRESULT WM_IME_COMPOSITION (long wParam, long lParam) {
	if (ime != null) {
		LRESULT result = ime.WM_IME_COMPOSITION (wParam, lParam);
		if (result != null) return result;
	}
	return super.WM_IME_COMPOSITION (wParam, lParam);
}

@Override
LRESULT WM_IME_COMPOSITION_START (long wParam, long lParam) {
	if (ime != null) {
		LRESULT result = ime.WM_IME_COMPOSITION_START (wParam, lParam);
		if (result != null) return result;
	}
	return super.WM_IME_COMPOSITION_START (wParam, lParam);
}

@Override
LRESULT WM_IME_ENDCOMPOSITION (long wParam, long lParam) {
	if (ime != null) {
		LRESULT result = ime.WM_IME_ENDCOMPOSITION (wParam, lParam);
		if (result != null) return result;
	}
	return super.WM_IME_ENDCOMPOSITION (wParam, lParam);
}

@Override
LRESULT WM_INPUTLANGCHANGE (long wParam, long lParam) {
	LRESULT result  = super.WM_INPUTLANGCHANGE (wParam, lParam);
	if (caret != null && caret.isFocusCaret ()) {
		caret.setIMEFont ();
		caret.resizeIME ();
	}
	return result;
}


@Override
LRESULT WM_KEYDOWN (long wParam, long lParam) {
	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
	if (result != null) return result;
	if (ime != null) {
		ime.WM_KEYDOWN (wParam, lParam);
	}
	return result;
}

@Override
LRESULT WM_KILLFOCUS (long wParam, long lParam) {
	if (ime != null) {
		LRESULT result = ime.WM_KILLFOCUS (wParam, lParam);
		if (result != null) return result;
	}
	Caret caret = this.caret;
	LRESULT result  = super.WM_KILLFOCUS (wParam, lParam);
	if (caret != null) caret.killFocus ();
	return result;
}

@Override
LRESULT WM_LBUTTONDOWN (long wParam, long lParam) {
	if (ime != null) {
		LRESULT result = ime.WM_LBUTTONDOWN (wParam, lParam);
		if (result != null) return result;
	}
	return super.WM_LBUTTONDOWN (wParam, lParam);
}

@Override
LRESULT WM_SETFOCUS (long wParam, long lParam) {
	LRESULT result  = super.WM_SETFOCUS (wParam, lParam);
	if (caret != null && caret.isFocusCaret ()) caret.setFocus ();
	return result;
}

@Override
LRESULT WM_SIZE (long wParam, long lParam) {
	LRESULT result  = super.WM_SIZE (wParam, lParam);
	if (caret != null && caret.isFocusCaret ()) caret.resizeIME ();
	return result;
}

@Override
LRESULT WM_WINDOWPOSCHANGED (long wParam, long lParam) {
	LRESULT result  = super.WM_WINDOWPOSCHANGED (wParam, lParam);
	//if (result != null) return result;
	/*
	* Bug in Windows.  When a window with style WS_EX_LAYOUTRTL
	* that contains a caret is resized, Windows does not move the
	* caret in relation to the mirrored origin in the top right.
	* The fix is to hide the caret in WM_WINDOWPOSCHANGING and
	* show the caret in WM_WINDOWPOSCHANGED.
	*/
	boolean isFocus = (style & SWT.RIGHT_TO_LEFT) != 0 && caret != null && caret.isFocusCaret ();
	if (isFocus) caret.setFocus ();
	return result;
}

@Override
LRESULT WM_WINDOWPOSCHANGING (long wParam, long lParam) {
	LRESULT result  = super.WM_WINDOWPOSCHANGING (wParam, lParam);
	if (result != null) return result;
	/*
	* Bug in Windows.  When a window with style WS_EX_LAYOUTRTL
	* that contains a caret is resized, Windows does not move the
	* caret in relation to the mirrored origin in the top right.
	* The fix is to hide the caret in WM_WINDOWPOSCHANGING and
	* show the caret in WM_WINDOWPOSCHANGED.
	*/
	boolean isFocus = (style & SWT.RIGHT_TO_LEFT) != 0 && caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	return result;
}

}

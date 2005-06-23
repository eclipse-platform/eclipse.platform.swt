/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * This class is the abstract superclass of all classes which
 * represent controls that have standard scroll bars.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>H_SCROLL, V_SCROLL</dd>
 * <dt><b>Events:</b>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */

public abstract class Scrollable extends Control {
	ScrollBar horizontalBar, verticalBar;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Scrollable () {
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
 * @see SWT#H_SCROLL
 * @see SWT#V_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Scrollable (Composite parent, int style) {
	super (parent, style);
}

int callWindowProc (int hwnd, int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.DefWindowProc (hwnd, msg, wParam, lParam);
}

/**
 * Given a desired <em>client area</em> for the receiver
 * (as described by the arguments), returns the bounding
 * rectangle which would be required to produce that client
 * area.
 * <p>
 * In other words, it returns a rectangle such that, if the
 * receiver's bounds were set to that rectangle, the area
 * of the receiver which is capable of displaying data
 * (that is, not covered by the "trimmings") would be the
 * rectangle described by the arguments (relative to the
 * receiver's parent).
 * </p>
 * 
 * @param x the desired x coordinate of the client area
 * @param y the desired y coordinate of the client area
 * @param width the desired width of the client area
 * @param height the desired height of the client area
 * @return the required bounds to produce the given client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getClientArea
 */
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	int scrolledHandle = scrolledHandle ();
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);
	int bits1 = OS.GetWindowLong (scrolledHandle, OS.GWL_STYLE);
	int bits2 = OS.GetWindowLong (scrolledHandle, OS.GWL_EXSTYLE);
	OS.AdjustWindowRectEx (rect, bits1, false, bits2);
	if (horizontalBar != null) rect.bottom += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
	if (verticalBar != null) rect.right += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
	int nWidth = rect.right - rect.left, nHeight = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, nWidth, nHeight);
}

ScrollBar createScrollBar (int type) {
	ScrollBar bar = new ScrollBar (this, type);
	if ((state & CANVAS) != 0) {
		bar.setMaximum (100);
		bar.setThumb (10);
	}
	return bar;
}

void createWidget () {
	super.createWidget ();
	if ((style & SWT.H_SCROLL) != 0) horizontalBar = createScrollBar (SWT.H_SCROLL);
	if ((style & SWT.V_SCROLL) != 0) verticalBar = createScrollBar (SWT.V_SCROLL);
}

/**
 * Returns a rectangle which describes the area of the
 * receiver which is capable of displaying data (that is,
 * not covered by the "trimmings").
 * 
 * @return the client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeTrim
 */
public Rectangle getClientArea () {
	checkWidget ();
	forceResize ();
	RECT rect = new RECT ();
	int scrolledHandle = scrolledHandle ();
	OS.GetClientRect (scrolledHandle, rect);
	return new Rectangle (0, 0, rect.right, rect.bottom);
}

/**
 * Returns the receiver's horizontal scroll bar if it has
 * one, and null if it does not.
 *
 * @return the horizontal scroll bar (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ScrollBar getHorizontalBar () {
	checkWidget ();
	return horizontalBar;
}

/**
 * Returns the receiver's vertical scroll bar if it has
 * one, and null if it does not.
 *
 * @return the vertical scroll bar (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ScrollBar getVerticalBar () {
	checkWidget ();
	return verticalBar;
}

void releaseWidget () {
	if (horizontalBar != null) horizontalBar.releaseResources ();
	if (verticalBar != null) verticalBar.releaseResources ();
	horizontalBar = verticalBar = null;
	super.releaseWidget ();
}

int scrolledHandle () {
	return handle;
}

int widgetExtStyle () {
	return super.widgetExtStyle ();
	/*
	* This code is intentionally commented.  In future,
	* we may wish to support different standard Windows
	* edge styles.  The issue here is that not all of
	* these styles are available on the other platforms
	* this would need to be a hint.
	*/
//	if ((style & SWT.BORDER) != 0) return OS.WS_EX_CLIENTEDGE;
//	if ((style & SWT.SHADOW_IN) != 0) return OS.WS_EX_STATICEDGE;
//	return super.widgetExtStyle ();
}

int widgetStyle () {
	int bits = super.widgetStyle () | OS.WS_TABSTOP;
	if ((style & SWT.H_SCROLL) != 0) bits |= OS.WS_HSCROLL;
	if ((style & SWT.V_SCROLL) != 0) bits |= OS.WS_VSCROLL;
	return bits;
}

TCHAR windowClass () {
	return display.windowClass;
}

int windowProc () {
	return display.windowProc;
}

LRESULT WM_HSCROLL (int wParam, int lParam) {
	LRESULT result = super.WM_HSCROLL (wParam, lParam);
	if (result != null) return result;
	
	/*
	* Bug on WinCE.  lParam should be NULL when the message is not sent
	* by a scroll bar control, but it contains the handle to the window.
	* When the message is sent by a scroll bar control, it correctly
	* contains the handle to the scroll bar.  The fix is to check for
	* both.
	*/
	if (horizontalBar != null && (lParam == 0 || lParam == handle)) {
		return wmScroll (horizontalBar, (state & CANVAS) != 0, handle, OS.WM_HSCROLL, wParam, lParam);
	}
	return result;
}

LRESULT WM_MOUSEWHEEL (int wParam, int lParam) {
	LRESULT result = super.WM_MOUSEWHEEL (wParam, lParam);
	if (result != null) return result;
	
	/*
	* Translate WM_MOUSEWHEEL to WM_VSCROLL or WM_HSCROLL.
	*/
	if ((state & CANVAS) != 0) {
		if ((wParam & (OS.MK_SHIFT | OS.MK_CONTROL)) != 0) return result;
		boolean vertical = verticalBar != null && verticalBar.getEnabled ();
		boolean horizontal = horizontalBar != null && horizontalBar.getEnabled ();
		int msg = (vertical) ? OS.WM_VSCROLL : (horizontal) ? OS.WM_HSCROLL : 0;
		if (msg == 0) return result;
		int [] value = new int [1];
		OS.SystemParametersInfo (OS.SPI_GETWHEELSCROLLLINES, 0, value, 0);
		int delta = (short) (wParam >> 16);
		int code = 0, count = 0;
  		if (value [0] == OS.WHEEL_PAGESCROLL) {	
   			code = delta < 0 ? OS.SB_PAGEDOWN : OS.SB_PAGEUP;
   			count = Math.abs (delta / OS.WHEEL_DELTA);
  		} else {
  			code = delta < 0 ? OS.SB_LINEDOWN : OS.SB_LINEUP;
  			delta = Math.abs (delta);
  			if (delta < OS.WHEEL_DELTA) return result;
  			if (msg == OS.WM_VSCROLL) {
  				count = value [0] * delta / OS.WHEEL_DELTA;
  			} else {
  				count = delta / OS.WHEEL_DELTA;
  			}
  		}
		for (int i=0; i<count; i++) {
			OS.SendMessage (handle, msg, code, 0);
		}
		return LRESULT.ZERO;
	}
		
	/*
	* When the native widget scrolls inside WM_MOUSEWHEEL, it
	* may or may not send a WM_VSCROLL or WM_HSCROLL to do the
	* actual scrolling.  This depends on the implementation of
	* each native widget.  In order to ensure that application
	* code is notified when the scroll bar moves, compare the
	* scroll bar position before and after the WM_MOUSEWHEEL.
	* If the native control sends a WM_VSCROLL or WM_HSCROLL,
	* then the application has already been notified.  If not
	* explicity send the event.
	*/
	int vPosition = verticalBar == null ? 0 : verticalBar.getSelection ();
	int hPosition = horizontalBar == null ? 0 : horizontalBar.getSelection ();
	int code = callWindowProc (handle, OS.WM_MOUSEWHEEL, wParam, lParam);
	if (verticalBar != null) {
		int position = verticalBar.getSelection ();
		if (position != vPosition) {
			Event event = new Event ();
			event.detail = position < vPosition ? SWT.PAGE_UP : SWT.PAGE_DOWN; 
			verticalBar.sendEvent (SWT.Selection, event);
		}
	}
	if (horizontalBar != null) {
		int position = horizontalBar.getSelection ();
		if (position != hPosition) {
			Event event = new Event ();
			event.detail = position < hPosition ? SWT.PAGE_UP : SWT.PAGE_DOWN; 
			horizontalBar.sendEvent (SWT.Selection, event);
		}
	}
	return new LRESULT (code);
}

LRESULT WM_SIZE (int wParam, int lParam) {
	int code = callWindowProc (handle, OS.WM_SIZE, wParam, lParam);
	super.WM_SIZE (wParam, lParam);
	// widget may be disposed at this point
	if (code == 0) return LRESULT.ZERO;
	return new LRESULT (code);
}

LRESULT WM_VSCROLL (int wParam, int lParam) {
	LRESULT result = super.WM_VSCROLL (wParam, lParam);
	if (result != null) return result;
	/*
	* Bug on WinCE.  lParam should be NULL when the message is not sent
	* by a scroll bar control, but it contains the handle to the window.
	* When the message is sent by a scroll bar control, it correctly
	* contains the handle to the scroll bar.  The fix is to check for
	* both.
	*/
	if (verticalBar != null && (lParam == 0 || lParam == handle)) {
		return wmScroll (verticalBar, (state & CANVAS) != 0, handle, OS.WM_VSCROLL, wParam, lParam);
	}
	return result;
}

LRESULT wmScroll (ScrollBar bar, boolean update, int hwnd, int msg, int wParam, int lParam) {
	LRESULT result = null;
	if (update) {
		int type = msg == OS.WM_HSCROLL ? OS.SB_HORZ : OS.SB_VERT;
		SCROLLINFO info = new SCROLLINFO ();
		info.cbSize = SCROLLINFO.sizeof;
		info.fMask = OS.SIF_TRACKPOS | OS.SIF_POS | OS.SIF_RANGE;
		OS.GetScrollInfo (hwnd, type, info);
		info.fMask = OS.SIF_POS;
		int code = wParam & 0xFFFF;
		switch (code) {
			case OS.SB_ENDSCROLL:  return null;
			case OS.SB_THUMBPOSITION:
			case OS.SB_THUMBTRACK:
				/* 
				* Note: On WinCE, the value in SB_THUMBPOSITION is relative to nMin.
				* Same for SB_THUMBPOSITION 'except' for the very first thumb track
				* message which has the actual value of nMin. This is a problem when
				* nMin is not zero.
				*/
				info.nPos = info.nTrackPos;
				break;
			case OS.SB_TOP:
				info.nPos = info.nMin;
				break;
			case OS.SB_BOTTOM:
				info.nPos = info.nMax;
				break;
			case OS.SB_LINEDOWN:
				info.nPos += bar.getIncrement ();
				break;
			case OS.SB_LINEUP:
				int increment = bar.getIncrement ();
				info.nPos = Math.max (info.nMin, info.nPos - increment);
				break;
			case OS.SB_PAGEDOWN:
				info.nPos += bar.getPageIncrement ();
				break;
			case OS.SB_PAGEUP:
				int pageIncrement = bar.getPageIncrement ();
				info.nPos = Math.max (info.nMin, info.nPos - pageIncrement);
				break;
		}
		OS.SetScrollInfo (hwnd, type, info, true);
	} else {
		int code = callWindowProc (hwnd, msg, wParam, lParam);
		result = code == 0 ? LRESULT.ZERO : new LRESULT (code);
	}
	bar.wmScrollChild (wParam, lParam);
	return result;
}

}

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
public Scrollable (Composite parent, int style) {
	super (parent, style);
}

int callWindowProc (int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.DefWindowProc (handle, msg, wParam, lParam);
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

	/* Get the size of the trimmings */
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	boolean hasMenu = ((bits & OS.WS_CHILD) == 0) && (OS.GetMenu (handle) != 0);
	OS.AdjustWindowRectEx (rect, bits, hasMenu, OS.GetWindowLong (handle, OS.GWL_EXSTYLE));

	/* Get the size of the scroll bars */
	if ((horizontalBar != null) && (horizontalBar.getVisible ())) {
		rect.bottom += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
	}
	if ((verticalBar != null) && (verticalBar.getVisible ())) {
		rect.right += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
	}

	/* Get the height of the menu bar */
	if (hasMenu) {
		RECT testRect = new RECT ();
		OS.SetRect (testRect, 0, 0, rect.right - rect.left, rect.bottom - rect.top);
		OS.SendMessage (handle, OS.WM_NCCALCSIZE, 0, testRect);
		while ((testRect.bottom - testRect.top) < height) {
			rect.top -= OS.GetSystemMetrics (OS.SM_CYMENU) - OS.GetSystemMetrics (OS.SM_CYBORDER);
			OS.SetRect(testRect, 0, 0, rect.right - rect.left, rect.bottom - rect.top);
			OS.SendMessage (handle, OS.WM_NCCALCSIZE, 0, testRect);
		}
	}
	return new Rectangle (rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
}

ScrollBar createScrollBar (int type) {
	ScrollBar bar = new ScrollBar (this, type);
	bar.setMaximum (100);
	bar.setThumb (10);
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
	if (parent != null && parent.hdwp != 0) {
		int oldHdwp = parent.hdwp;
		parent.hdwp = 0;
		OS.EndDeferWindowPos (oldHdwp);
		int count = parent.getChildrenCount ();
		parent.hdwp = OS.BeginDeferWindowPos (count);
	}
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
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
	if (horizontalBar != null) horizontalBar.releaseWidget ();
	if (verticalBar != null) verticalBar.releaseWidget ();
	horizontalBar = verticalBar = null;
	super.releaseWidget ();
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

byte [] windowClass () {
	return getDisplay ().windowClass;
}

int windowProc () {
	return getDisplay ().windowProc;
}

LRESULT WM_HSCROLL (int wParam, int lParam) {
	LRESULT result = super.WM_HSCROLL (wParam, lParam);
	if (result != null) return result;
	if ((lParam == 0) && (horizontalBar != null)) {
		result = wmScroll (OS.WM_HSCROLL, wParam, lParam);
		horizontalBar.wmScrollChild (wParam, lParam);
	}
	return result;
}

LRESULT WM_SIZE (int wParam, int lParam) {
	int code = callWindowProc (OS.WM_SIZE, wParam, lParam);
	super.WM_SIZE (wParam, lParam);
	// widget may be disposed at this point
	if (code == 0) return LRESULT.ZERO;
	return new LRESULT (code);
}

LRESULT WM_VSCROLL (int wParam, int lParam) {
	LRESULT result = super.WM_VSCROLL (wParam, lParam);
	if (result != null) return result;
	if ((lParam == 0) && (verticalBar != null)) {
		result = wmScroll (OS.WM_VSCROLL, wParam, lParam);
		verticalBar.wmScrollChild (wParam, lParam);
	}
	return result;
}

LRESULT wmScroll (int msg, int wParam, int lParam) {
	int type = OS.SB_HORZ;
	ScrollBar bar = horizontalBar;
	if (msg == OS.WM_VSCROLL) {
		type = OS.SB_VERT;
		bar = verticalBar;
	}
	if (bar == null) return null;
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_TRACKPOS | OS.SIF_POS | OS.SIF_RANGE;
	OS.GetScrollInfo (handle, type, info);
	info.fMask = OS.SIF_POS;
	int code = wParam & 0xFFFF;
	switch (code) {
		case OS.SB_ENDSCROLL:  return null;
		case OS.SB_THUMBTRACK:
		case OS.SB_THUMBPOSITION:	
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
		case OS.SB_LINEUP: {
			int increment = bar.getIncrement ();
			info.nPos = Math.max (info.nMin, info.nPos - increment);
			break;
		}
		case OS.SB_PAGEDOWN:
			info.nPos += bar.getPageIncrement ();
			break;
		case OS.SB_PAGEUP: {
			int pageIncrement = bar.getPageIncrement ();
			info.nPos = Math.max (info.nMin, info.nPos - pageIncrement);
			break;
		}
	}
	OS.SetScrollInfo (handle, type, info, true);
	return null;
}

}

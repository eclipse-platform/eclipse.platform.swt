/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class Scrollable extends Control {
	ScrollBar horizontalBar, verticalBar;

	/**
	 * The regular expression used to determine the string which should be deleted
	 * when Ctrl+Bs is hit.
	 */
	static final java.util.regex.Pattern CTRL_BS_PATTERN =
			java.util.regex.Pattern.compile ("\\r?\\n\\z|[\\p{Punct}]+[\\t ]*\\z|[^\\p{Punct}\\s\\n\\r]*[\\t ]*\\z");

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

@Override
long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
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
	x = DPIUtil.autoScaleUp(x);
	y = DPIUtil.autoScaleUp(y);
	width = DPIUtil.autoScaleUp(width);
	height = DPIUtil.autoScaleUp(height);
	return DPIUtil.autoScaleDown(computeTrimInPixels(x, y, width, height));
}

Rectangle computeTrimInPixels (int x, int y, int width, int height) {
	long scrolledHandle = scrolledHandle ();
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

@Override
void createHandle () {
	super.createHandle();
	maybeEnableDarkSystemTheme();
}

ScrollBar createScrollBar (int type) {
	ScrollBar bar = new ScrollBar (this, type);
	if ((state & CANVAS) != 0) {
		bar.setMaximum (100);
		bar.setThumb (10);
	}
	return bar;
}

@Override
void createWidget () {
	super.createWidget ();
	if ((style & SWT.H_SCROLL) != 0) horizontalBar = createScrollBar (SWT.H_SCROLL);
	if ((style & SWT.V_SCROLL) != 0) verticalBar = createScrollBar (SWT.V_SCROLL);
}

@Override
void updateBackgroundColor () {
	switch (applyThemeBackground ()) {
		case 0: state &= ~THEME_BACKGROUND; break;
		case 1: state |= THEME_BACKGROUND; break;
		default: /* No change */
	}
	super.updateBackgroundColor ();
}

/**
 * @return
 * 		<li>0 to remove THEME_BACKGROUND</li>
 *      <li>1 to apply THEME_BACKGROUND</li>
 *      <li>otherwise don't change THEME_BACKGROUND state</li>
 */
int applyThemeBackground () {
	return (backgroundAlpha == 0) ? 1 : 0;
}

void destroyScrollBar (int type) {
	long hwnd = scrolledHandle ();
	int bits = OS.GetWindowLong (hwnd, OS.GWL_STYLE);
	if ((type & SWT.HORIZONTAL) != 0) {
		style &= ~SWT.H_SCROLL;
		bits &= ~OS.WS_HSCROLL;
	}
	if ((type & SWT.VERTICAL) != 0) {
		style &= ~SWT.V_SCROLL;
		bits &= ~OS.WS_VSCROLL;
	}
	OS.SetWindowLong (hwnd, OS.GWL_STYLE, bits);
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
	return DPIUtil.autoScaleDown(getClientAreaInPixels());
}

Rectangle getClientAreaInPixels () {
	forceResize ();
	RECT rect = new RECT ();
	long scrolledHandle = scrolledHandle ();
	OS.GetClientRect (scrolledHandle, rect);
	int x = rect.left, y = rect.top;
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	if (scrolledHandle != handle) {
		OS.GetClientRect (handle, rect);
		OS.MapWindowPoints(handle, scrolledHandle, rect, 2);
		x = -rect.left;
		y = -rect.top;
	}
	return new Rectangle (x, y, width, height);
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
 * Returns the mode of the receiver's scrollbars. This will be
 * <em>bitwise</em> OR of one or more of the constants defined in class
 * <code>SWT</code>.<br>
 * <ul>
 * <li><code>SWT.SCROLLBAR_OVERLAY</code> - if receiver
 * uses overlay scrollbars</li>
 * <li><code>SWT.NONE</code> - otherwise</li>
 * </ul>
 *
 * @return the mode of scrollbars
 *
 * @exception SWTException <ul>
 * <li>ERROR_WIDGET_DISPOSED - if the receiver has been
 * disposed</li>
 * <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
 * thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#SCROLLBAR_OVERLAY
 *
 * @since 3.8
 */
public int getScrollbarsMode () {
	checkWidget();
	return SWT.NONE;
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

@Override
void releaseChildren (boolean destroy) {
	if (horizontalBar != null) {
		horizontalBar.release (false);
		horizontalBar = null;
	}
	if (verticalBar != null) {
		verticalBar.release (false);
		verticalBar = null;
	}
	super.releaseChildren (destroy);
}

@Override
void reskinChildren (int flags) {
	if (horizontalBar != null) horizontalBar.reskin (flags);
	if (verticalBar != null) verticalBar.reskin (flags);
	super.reskinChildren (flags);
}

long scrolledHandle () {
	return handle;
}

@Override
int widgetStyle () {
	int bits = super.widgetStyle () | OS.WS_TABSTOP;
	if ((style & SWT.H_SCROLL) != 0) bits |= OS.WS_HSCROLL;
	if ((style & SWT.V_SCROLL) != 0) bits |= OS.WS_VSCROLL;
	return bits;
}

@Override
TCHAR windowClass () {
	return display.windowClass;
}

@Override
long windowProc () {
	return display.windowProc;
}

@Override
LRESULT WM_HSCROLL (long wParam, long lParam) {
	LRESULT result = super.WM_HSCROLL (wParam, lParam);
	if (result != null) return result;
	if (horizontalBar != null && lParam == 0) {
		return wmScroll (horizontalBar, (state & CANVAS) != 0, handle, OS.WM_HSCROLL, wParam, lParam);
	}
	return result;
}

@Override
LRESULT WM_MOUSEWHEEL (long wParam, long lParam) {
	return wmScrollWheel ((state & CANVAS) != 0, wParam, lParam, false);
}

@Override
LRESULT WM_MOUSEHWHEEL (long wParam, long lParam) {
	return wmScrollWheel ((state & CANVAS) != 0, -1 * wParam, lParam, true);
}

@Override
LRESULT WM_SIZE (long wParam, long lParam) {
	long code = callWindowProc (handle, OS.WM_SIZE, wParam, lParam);
	super.WM_SIZE (wParam, lParam);
	// widget may be disposed at this point
	if (code == 0) return LRESULT.ZERO;
	return new LRESULT (code);
}

@Override
LRESULT WM_VSCROLL (long wParam, long lParam) {
	LRESULT result = super.WM_VSCROLL (wParam, lParam);
	if (result != null) return result;
	if (verticalBar != null && lParam == 0) {
		return wmScroll (verticalBar, (state & CANVAS) != 0, handle, OS.WM_VSCROLL, wParam, lParam);
	}
	return result;
}

LRESULT wmScrollWheel (boolean update, long wParam, long lParam, boolean horzWheel) {
	LRESULT result = super.WM_MOUSEWHEEL (wParam, lParam);
	if (result != null) return result;
	/*
	* Translate WM_MOUSEWHEEL and WM_MOUSEHWHEEL to WM_VSCROLL or WM_HSCROLL.
	*/
	if (update) {
		if ((wParam & OS.MK_CONTROL) != 0) {
			return null;
		}
		if ((wParam & OS.MK_SHIFT) != 0) {
			horzWheel = !horzWheel;
		}

		boolean vertical;
		if (verticalBar != null && verticalBar.getEnabled () && !horzWheel) {
			vertical = true;
		} else if (horizontalBar != null && horizontalBar.getEnabled () && horzWheel) {
			vertical = false;
		} else {
			return null;
		}

		ScrollBar bar = vertical ? verticalBar : horizontalBar;
		MouseWheelData wheelData = new MouseWheelData(vertical, bar, wParam, display.scrollRemainderBar);

		if (wheelData.count == 0) return null;

		SCROLLINFO info = new SCROLLINFO ();
		info.cbSize = SCROLLINFO.sizeof;
		info.fMask = OS.SIF_POS;
		OS.GetScrollInfo (handle, bar.scrollBarType (), info);
		info.nPos -= wheelData.count;
		OS.SetScrollInfo (handle, bar.scrollBarType (), info, true);

		int msg = vertical ? OS.WM_VSCROLL : OS.WM_HSCROLL;
		OS.SendMessage (handle, msg, OS.SB_THUMBPOSITION, 0);
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
	* explicitly send the event.
	*/
	int vPosition = verticalBar == null ? 0 : verticalBar.getSelection ();
	int hPosition = horizontalBar == null ? 0 : horizontalBar.getSelection ();
	long code = callWindowProc (handle, OS.WM_MOUSEWHEEL, wParam, lParam);
	if (verticalBar != null) {
		int position = verticalBar.getSelection ();
		if (position != vPosition) {
			Event event = new Event ();
			event.detail = position < vPosition ? SWT.PAGE_UP : SWT.PAGE_DOWN;
			verticalBar.sendSelectionEvent (SWT.Selection, event, true);
		}
	}
	if (horizontalBar != null) {
		int position = horizontalBar.getSelection ();
		if (position != hPosition) {
			Event event = new Event ();
			event.detail = position < hPosition ? SWT.PAGE_UP : SWT.PAGE_DOWN;
			horizontalBar.sendSelectionEvent (SWT.Selection, event, true);
		}
	}
	return new LRESULT (code);
}

LRESULT wmScroll (ScrollBar bar, boolean update, long hwnd, int msg, long wParam, long lParam) {
	LRESULT result = null;
	if (update) {
		int type = msg == OS.WM_HSCROLL ? OS.SB_HORZ : OS.SB_VERT;
		SCROLLINFO info = new SCROLLINFO ();
		info.cbSize = SCROLLINFO.sizeof;
		info.fMask = OS.SIF_TRACKPOS | OS.SIF_POS | OS.SIF_RANGE;
		OS.GetScrollInfo (hwnd, type, info);
		info.fMask = OS.SIF_POS;
		int code = OS.LOWORD (wParam);
		switch (code) {
			case OS.SB_ENDSCROLL:  return null;
			case OS.SB_THUMBPOSITION:
			case OS.SB_THUMBTRACK:
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
		long code = callWindowProc (hwnd, msg, wParam, lParam);
		result = code == 0 ? LRESULT.ZERO : new LRESULT (code);
	}
	bar.wmScrollChild (wParam, lParam);
	return result;
}

}

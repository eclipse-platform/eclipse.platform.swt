/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of the receiver represent a selectable user
 * interface object that present a range of continuous
 * numeric values.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#scale">Scale snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Scale extends Control {
	boolean ignoreResize, ignoreSelection;
	static final long TrackBarProc;
	static final TCHAR TrackBarClass = new TCHAR (0, OS.TRACKBAR_CLASS, true);
	boolean createdAsRTL;
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, TrackBarClass, lpWndClass);
		TrackBarProc = lpWndClass.lpfnWndProc;
		/*
		* Feature in Windows.  The track bar window class
		* does not include CS_DBLCLKS.  This means that these
		* controls will not get double click messages such as
		* WM_LBUTTONDBLCLK.  The fix is to register a new
		* window class with CS_DBLCLKS.
		*
		* NOTE:  Screen readers look for the exact class name
		* of the control in order to provide the correct kind
		* of assistance.  Therefore, it is critical that the
		* new window class have the same name.  It is possible
		* to register a local window class with the same name
		* as a global class.  Since bits that affect the class
		* are being changed, it is possible that other native
		* code, other than SWT, could create a control with
		* this class name, and fail unexpectedly.
		*/
		lpWndClass.hInstance = OS.GetModuleHandle (null);
		lpWndClass.style &= ~OS.CS_GLOBALCLASS;
		lpWndClass.style |= OS.CS_DBLCLKS;
		OS.RegisterClass (TrackBarClass, lpWndClass);
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
 * @see SWT#HORIZONTAL
 * @see SWT#VERTICAL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Scale (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's value, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the user changes the receiver's value.
 * <code>widgetDefaultSelected</code> is not called.
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
 */
public void addSelectionListener(SelectionListener listener) {
	addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
}

@Override
long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (TrackBarProc, hwnd, msg, wParam, lParam);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

@Override Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int border = getBorderWidthInPixels ();
	int width = border * 2, height = border * 2;
	RECT rect = new RECT ();
	OS.SendMessage (handle, OS.TBM_GETTHUMBRECT, 0, rect);
	if ((style & SWT.HORIZONTAL) != 0) {
		width += OS.GetSystemMetrics (OS.SM_CXHSCROLL) * 10;
		int scrollY = OS.GetSystemMetrics (OS.SM_CYHSCROLL);
		height += (rect.top * 2) + scrollY + (scrollY / 3);
	} else {
		int scrollX = OS.GetSystemMetrics (OS.SM_CXVSCROLL);
		width += (rect.left * 2) + scrollX + (scrollX / 3);
		height += OS.GetSystemMetrics (OS.SM_CYVSCROLL) * 10;
	}
	if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
	if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
	return new Point (width, height);
}

@Override
void createHandle () {
	super.createHandle ();
	state |= THEME_BACKGROUND | DRAW_BACKGROUND;
	OS.SendMessage (handle, OS.TBM_SETRANGEMAX, 0, 100);
	OS.SendMessage (handle, OS.TBM_SETPAGESIZE, 0, 10);
	OS.SendMessage (handle, OS.TBM_SETTICFREQ, 10, 0);
	createdAsRTL = (style & SWT.RIGHT_TO_LEFT) != 0;
}

@Override
int defaultForeground () {
	return OS.GetSysColor (OS.COLOR_BTNFACE);
}

/**
 * Returns the amount that the receiver's value will be
 * modified by when the up/down (or right/left) arrows
 * are pressed.
 *
 * @return the increment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getIncrement () {
	checkWidget ();
	return (int)OS.SendMessage (handle, OS.TBM_GETLINESIZE, 0, 0);
}

/**
 * Returns the maximum value which the receiver will allow.
 *
 * @return the maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMaximum () {
	checkWidget ();
	return (int)OS.SendMessage (handle, OS.TBM_GETRANGEMAX, 0, 0);
}

/**
 * Returns the minimum value which the receiver will allow.
 *
 * @return the minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMinimum () {
	checkWidget ();
	return (int)OS.SendMessage (handle, OS.TBM_GETRANGEMIN, 0, 0);
}

/**
 * Returns the amount that the receiver's value will be
 * modified by when the page increment/decrement areas
 * are selected.
 *
 * @return the page increment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getPageIncrement () {
	checkWidget ();
	return (int)OS.SendMessage (handle, OS.TBM_GETPAGESIZE, 0, 0);
}

/**
 * Returns the 'selection', which is the receiver's position.
 *
 * @return the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelection () {
	checkWidget ();
	return (int)OS.SendMessage (handle, OS.TBM_GETPOS, 0, 0);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the user changes the receiver's value.
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
}

@Override
void setBackgroundImage (long hImage) {
	super.setBackgroundImage (hImage);
	/*
	* Bug in Windows.  Changing the background color of the Scale
	* widget and calling InvalidateRect() still draws with the old
	* color.  The fix is to send a fake WM_SIZE event to cause
	* it to redraw with the new background color.
	*/
	ignoreResize = true;
	OS.SendMessage (handle, OS.WM_SIZE, 0, 0);
	ignoreResize = false;
}

@Override
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	/*
	* Bug in Windows.  Changing the background color of the Scale
	* widget and calling InvalidateRect() still draws with the old
	* color.  The fix is to send a fake WM_SIZE event to cause
	* it to redraw with the new background color.
	*/
	ignoreResize = true;
	OS.SendMessage (handle, OS.WM_SIZE, 0, 0);
	ignoreResize = false;
}

@Override
void setBoundsInPixels (int x, int y, int width, int height, int flags, boolean defer) {
	/*
	* Bug in Windows.  If SetWindowPos() is called on a
	* track bar with either SWP_DRAWFRAME, a new size,
	* or both during mouse down, the track bar posts a
	* WM_MOUSEMOVE message when the mouse has not moved.
	* The window proc for the track bar uses WM_MOUSEMOVE
	* to issue WM_HSCROLL or WM_SCROLL events to notify
	* the application that the slider has changed.  The
	* end result is that when the user requests a page
	* scroll and the application resizes the track bar
	* during the change notification, continuous stream
	* of WM_MOUSEMOVE messages are generated and the
	* thumb moves to the mouse position rather than
	* scrolling by a page.  The fix is to clear the
	* SWP_DRAWFRAME flag.
	*
	* NOTE:  There is no fix for the WM_MOUSEMOVE that
	* is generated by a new size.  Clearing SWP_DRAWFRAME
	* does not fix the problem.  However, it is unlikely
	* that the programmer will resize the control during
	* mouse down.
	*/
	flags &= ~OS.SWP_DRAWFRAME;
	super.setBoundsInPixels (x, y, width, height, flags, true);
}

/**
 * Sets the amount that the receiver's value will be
 * modified by when the up/down (or right/left) arrows
 * are pressed to the argument, which must be at least
 * one.
 *
 * @param increment the new increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setIncrement (int increment) {
	checkWidget ();
	if (increment < 1) return;
	int minimum = (int)OS.SendMessage (handle, OS.TBM_GETRANGEMIN, 0, 0);
	int maximum = (int)OS.SendMessage (handle, OS.TBM_GETRANGEMAX, 0, 0);
	if (increment > maximum - minimum) return;
	OS.SendMessage (handle, OS.TBM_SETLINESIZE, 0, increment);
}

/**
 * Sets the maximum value that the receiver will allow.  This new
 * value will be ignored if it is not greater than the receiver's current
 * minimum value.  If the new maximum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new maximum, which must be greater than the current minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMaximum (int value) {
	checkWidget ();
	int minimum = (int)OS.SendMessage (handle, OS.TBM_GETRANGEMIN, 0, 0);
	if (0 <= minimum && minimum < value) {
		OS.SendMessage (handle, OS.TBM_SETRANGEMAX, 1, value);
	}
}

/**
 * Sets the minimum value that the receiver will allow.  This new
 * value will be ignored if it is negative or is not less than the receiver's
 * current maximum value.  If the new minimum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new minimum, which must be nonnegative and less than the current maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinimum (int value) {
	checkWidget ();
	int maximum = (int)OS.SendMessage (handle, OS.TBM_GETRANGEMAX, 0, 0);
	if (0 <= value && value < maximum) {
		OS.SendMessage (handle, OS.TBM_SETRANGEMIN, 1, value);
	}
}

/**
 * Sets the amount that the receiver's value will be
 * modified by when the page increment/decrement areas
 * are selected to the argument, which must be at least
 * one.
 *
 * @param pageIncrement the page increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setPageIncrement (int pageIncrement) {
	checkWidget ();
	if (pageIncrement < 1) return;
	int minimum = (int)OS.SendMessage (handle, OS.TBM_GETRANGEMIN, 0, 0);
	int maximum = (int)OS.SendMessage (handle, OS.TBM_GETRANGEMAX, 0, 0);
	if (pageIncrement > maximum - minimum) return;
	OS.SendMessage (handle, OS.TBM_SETPAGESIZE, 0, pageIncrement);
	OS.SendMessage (handle, OS.TBM_SETTICFREQ, pageIncrement, 0);
}

/**
 * Sets the 'selection', which is the receiver's value,
 * to the argument which must be greater than or equal to zero.
 *
 * @param value the new selection (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int value) {
	checkWidget ();
	OS.SendMessage (handle, OS.TBM_SETPOS, 1, value);
}

@Override
int widgetStyle () {
	int bits = super.widgetStyle () | OS.WS_TABSTOP | OS.TBS_BOTH | OS.TBS_AUTOTICKS;
	if ((style & SWT.HORIZONTAL) != 0) return bits | OS.TBS_HORZ | OS.TBS_DOWNISLEFT;
	return bits | OS.TBS_VERT;
}

@Override
TCHAR windowClass () {
	return TrackBarClass;
}

@Override
long windowProc () {
	return TrackBarProc;
}

@Override
LRESULT WM_KEYDOWN (long wParam, long lParam) {
	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
	if (result != null) return result;
	switch ((int)wParam) {
		case OS.VK_LEFT:
		case OS.VK_RIGHT:
			/*
			* Bug in Windows. The behavior for the left and right keys is not
			* changed if the orientation changes after the control was created.
			* The fix is to replace VK_LEFT by VK_RIGHT and VK_RIGHT by VK_LEFT
			* when the current orientation differs from the orientation used to
			* create the control.
			*/
			boolean isRTL = (style & SWT.RIGHT_TO_LEFT) != 0;
			if (isRTL != createdAsRTL) {
				long code = callWindowProc (handle, OS.WM_KEYDOWN, wParam == OS.VK_RIGHT ? OS.VK_LEFT : OS.VK_RIGHT, lParam);
				return new LRESULT (code);
			}
			break;
	}
	return result;
}

@Override
LRESULT WM_MOUSEWHEEL (long wParam, long lParam) {
	LRESULT result = super.WM_MOUSEWHEEL (wParam, lParam);
	if (result != null) return result;
	/*
	* Bug in Windows.  When a track bar slider is changed
	* from WM_MOUSEWHEEL, it does not always send either
	* a WM_VSCROLL or M_HSCROLL to notify the application
	* of the change.  The fix is to detect that the selection
	* has changed and that notification has not been issued
	* and send the selection event.
	*/
	int oldPosition = (int)OS.SendMessage (handle, OS.TBM_GETPOS, 0, 0);
	ignoreSelection = true;
	long code = callWindowProc (handle, OS.WM_MOUSEWHEEL, wParam, lParam);
	ignoreSelection = false;
	int newPosition = (int)OS.SendMessage (handle, OS.TBM_GETPOS, 0, 0);
	if (oldPosition != newPosition) {
		/*
		* Send the event because WM_HSCROLL and WM_VSCROLL
		* are sent from a modal message loop in windows that
		* is active when the user is scrolling.
		*/
		sendSelectionEvent (SWT.Selection, null, true);
		// widget could be disposed at this point
	}
	return new LRESULT (code);
}

@Override
LRESULT WM_PAINT (long wParam, long lParam) {
	if ((state & DISPOSE_SENT) != 0) return LRESULT.ZERO;

	/*
	* Bug in Windows.  For some reason, when WM_CTLCOLORSTATIC
	* is used to implement transparency and returns a NULL brush,
	* Windows doesn't always draw the track bar.  It seems that
	* it is drawn correctly the first time.  It is possible that
	* Windows double buffers the control and the double buffer
	* strategy fails when WM_CTLCOLORSTATIC returns unexpected
	* results.  The fix is to send a fake WM_SIZE to force it
	* to redraw every time there is a WM_PAINT.
	*/
	boolean fixPaint = findBackgroundControl () != null;
	if (!fixPaint) {
		if (OS.IsAppThemed ()) {
			Control control = findThemeControl ();
			fixPaint = control != null;
		}
	}
	if (fixPaint) {
		boolean redraw = getDrawing () && OS.IsWindowVisible (handle);
		if (redraw) OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
		ignoreResize = true;
		OS.SendMessage (handle, OS.WM_SIZE, 0, 0);
		ignoreResize = false;
		if (redraw) {
			OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
			OS.InvalidateRect (handle, null, false);
		}
	}
	return super.WM_PAINT (wParam, lParam);
}

@Override
LRESULT WM_SIZE (long wParam, long lParam) {
	if (ignoreResize) return null;
	return super.WM_SIZE (wParam, lParam);
}

@Override
LRESULT wmScrollChild (long wParam, long lParam) {

	/* Do nothing when scrolling is ending */
	int code = OS.LOWORD (wParam);
	switch (code) {
		case OS.TB_ENDTRACK:
		case OS.TB_THUMBPOSITION:
			return null;
	}

	if (!ignoreSelection) {
		Event event = new Event ();
		/*
		* This code is intentionally commented.  The event
		* detail field is not currently supported on all
		* platforms.
		*/
//		switch (code) {
//			case OS.TB_TOP: 		event.detail = SWT.HOME;  break;
//			case OS.TB_BOTTOM:		event.detail = SWT.END;  break;
//			case OS.TB_LINEDOWN:	event.detail = SWT.ARROW_DOWN;  break;
//			case OS.TB_LINEUP: 		event.detail = SWT.ARROW_UP;  break;
//			case OS.TB_PAGEDOWN: 	event.detail = SWT.PAGE_DOWN;  break;
//			case OS.TB_PAGEUP: 		event.detail = SWT.PAGE_UP;  break;
//		}
		/*
		* Send the event because WM_HSCROLL and WM_VSCROLL
		* are sent from a modal message loop in windows that
		* is active when the user is scrolling.
		*/
		sendSelectionEvent (SWT.Selection, event, true);
		// widget could be disposed at this point
	}
	return null;
}

}

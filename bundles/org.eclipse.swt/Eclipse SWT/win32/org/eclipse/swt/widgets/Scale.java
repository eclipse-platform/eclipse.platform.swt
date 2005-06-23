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
import org.eclipse.swt.events.*;

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
 * </p><p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */

public class Scale extends Control {
	static final int TrackBarProc;
	static final TCHAR TrackBarClass = new TCHAR (0, OS.TRACKBAR_CLASS, true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, TrackBarClass, lpWndClass);
		TrackBarProc = lpWndClass.lpfnWndProc;
		/*
		* Feature in Windows.  The track bar window class
		* does not include CS_DBLCLKS.  This mean that these
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
		int hInstance = OS.GetModuleHandle (null);
		int hHeap = OS.GetProcessHeap ();
		lpWndClass.hInstance = hInstance;
		lpWndClass.style &= ~OS.CS_GLOBALCLASS;
		lpWndClass.style |= OS.CS_DBLCLKS;
		int byteCount = TrackBarClass.length () * TCHAR.sizeof;
		int lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (lpszClassName, TrackBarClass, byteCount);
		lpWndClass.lpszClassName = lpszClassName;
		OS.RegisterClass (lpWndClass);
//		OS.HeapFree (hHeap, 0, lpszClassName);	
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
 * be notified when the receiver's value changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

int callWindowProc (int hwnd, int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (TrackBarProc, hwnd, msg, wParam, lParam);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int border = getBorderWidth ();
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

void createHandle () {
	super.createHandle ();
	OS.SendMessage (handle, OS.TBM_SETRANGEMAX, 0, 100);
	OS.SendMessage (handle, OS.TBM_SETPAGESIZE, 0, 10);
	OS.SendMessage (handle, OS.TBM_SETTICFREQ, 10, 0);
}

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
	return OS.SendMessage (handle, OS.TBM_GETLINESIZE, 0, 0);
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
	return OS.SendMessage (handle, OS.TBM_GETRANGEMAX, 0, 0);
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
	return OS.SendMessage (handle, OS.TBM_GETRANGEMIN, 0, 0);
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
	return OS.SendMessage (handle, OS.TBM_GETPAGESIZE, 0, 0);
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
	return OS.SendMessage (handle, OS.TBM_GETPOS, 0, 0);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's value changes.
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

void setBackgroundPixel (int pixel) {
	if (background == pixel) return;
	super.setBackgroundPixel (pixel);
	/*
	* Bug in Windows.  Changing the background color of the Scale
	* widget and calling InvalidateRect() still draws with the old
	* color.  The fix is to post a fake WM_SETFOCUS event to cause
	* it to redraw with the new background color.
	* 
	* Note.  This WM_SETFOCUS message causes recursion when
	* setBackground is called from within the focus event
	* listener.
	*/
	OS.PostMessage (handle, OS.WM_SETFOCUS, 0, 0);
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
	int minimum = OS.SendMessage (handle, OS.TBM_GETRANGEMIN, 0, 0);
	int maximum = OS.SendMessage (handle, OS.TBM_GETRANGEMAX, 0, 0);
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
	int minimum = OS.SendMessage (handle, OS.TBM_GETRANGEMIN, 0, 0);
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
	int maximum = OS.SendMessage (handle, OS.TBM_GETRANGEMAX, 0, 0);
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
	int minimum = OS.SendMessage (handle, OS.TBM_GETRANGEMIN, 0, 0);
	int maximum = OS.SendMessage (handle, OS.TBM_GETRANGEMAX, 0, 0);
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

int widgetStyle () {
	int bits = super.widgetStyle () | OS.WS_TABSTOP | OS.TBS_BOTH | OS.TBS_AUTOTICKS;
	if ((style & SWT.HORIZONTAL) != 0) return bits | OS.TBS_HORZ | OS.TBS_DOWNISLEFT;
	return bits | OS.TBS_VERT;
}

TCHAR windowClass () {
	return TrackBarClass;
}

int windowProc () {
	return TrackBarProc;
}

LRESULT wmScrollChild (int wParam, int lParam) {
	
	/* Do nothing when scrolling is ending */
	int code = wParam & 0xFFFF;
	switch (code) {
		case OS.TB_ENDTRACK:
		case OS.TB_THUMBPOSITION:
			return null;
	}

	Event event = new Event ();
	/*
	* This code is intentionally commented.  The event
	* detail field is not currently supported on all
	* platforms.
	*/
//	switch (code) {
//		case OS.TB_TOP: 		event.detail = SWT.HOME;  break;
//		case OS.TB_BOTTOM:		event.detail = SWT.END;  break;
//		case OS.TB_LINEDOWN:	event.detail = SWT.ARROW_DOWN;  break;
//		case OS.TB_LINEUP: 		event.detail = SWT.ARROW_UP;  break;
//		case OS.TB_PAGEDOWN: 	event.detail = SWT.PAGE_DOWN;  break;
//		case OS.TB_PAGEUP: 		event.detail = SWT.PAGE_UP;  break;
//	}
	
	/*
	* Send the event because WM_HSCROLL and WM_VSCROLL
	* are sent from a modal message loop in windows that
	* is active when the user is scrolling.
	*/
	sendEvent (SWT.Selection, event);
	// widget could be disposed at this point
	return null;
}

}

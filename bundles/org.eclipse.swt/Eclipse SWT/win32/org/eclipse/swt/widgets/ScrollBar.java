/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
 * Instances of this class are selectable user interface
 * objects that represent a range of positive, numeric values. 
 * <p>
 * At any given moment, a given scroll bar will have a 
 * single 'selection' that is considered to be its
 * value, which is constrained to be within the range of
 * values the scroll bar represents (that is, between its
 * <em>minimum</em> and <em>maximum</em> values).
 * </p><p>
 * Typically, scroll bars will be made up of five areas:
 * <ol>
 * <li>an arrow button for decrementing the value</li>
 * <li>a page decrement area for decrementing the value by a larger amount</li>
 * <li>a <em>thumb</em> for modifying the value by mouse dragging</li>
 * <li>a page increment area for incrementing the value by a larger amount</li>
 * <li>an arrow button for incrementing the value</li>
 * </ol>
 * Based on their style, scroll bars are either <code>HORIZONTAL</code>
 * (which have a left facing button for decrementing the value and a
 * right facing button for incrementing it) or <code>VERTICAL</code>
 * (which have an upward facing button for decrementing the value
 * and a downward facing buttons for incrementing it).
 * </p><p>
 * On some platforms, the size of the scroll bar's thumb can be
 * varied relative to the magnitude of the range of values it
 * represents (that is, relative to the difference between its
 * maximum and minimum values). Typically, this is used to
 * indicate some proportional value such as the ratio of the
 * visible area of a document to the total amount of space that
 * it would take to display it. SWT supports setting the thumb
 * size even if the underlying platform does not, but in this
 * case the appearance of the scroll bar will not change.
 * </p><p>
 * Scroll bars are created by specifying either <code>H_SCROLL</code>,
 * <code>V_SCROLL</code> or both when creating a <code>Scrollable</code>.
 * They are accessed from the <code>Scrollable</code> using
 * <code>getHorizontalBar</code> and <code>getVerticalBar</code>.
 * </p><p>
 * Note: Scroll bars are not Controls.  On some platforms, scroll bars
 * that appear as part of some standard controls such as a text or list
 * have no operating system resources and are not children of the control.
 * For this reason, scroll bars are treated specially.  To create a control
 * that looks like a scroll bar but has operating system resources, use
 * <code>Slider</code>. 
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see Slider
 * @see Scrollable
 * @see Scrollable#getHorizontalBar
 * @see Scrollable#getVerticalBar
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class ScrollBar extends Widget {	
	Scrollable parent;
	int increment, pageIncrement;

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
ScrollBar (Scrollable parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget ();
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's value, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the event object detail field contains one of the following values:
 * <code>SWT.NONE</code> - for the end of a drag.
 * <code>SWT.DRAG</code>.
 * <code>SWT.HOME</code>.
 * <code>SWT.END</code>.
 * <code>SWT.ARROW_DOWN</code>.
 * <code>SWT.ARROW_UP</code>.
 * <code>SWT.PAGE_DOWN</code>.
 * <code>SWT.PAGE_UP</code>.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the user changes the receiver's value
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
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

void createWidget () {
	increment = 1;
	pageIncrement = 10;
	/*
	* Do not set the initial values of the maximum
	* or the thumb.  These values normally default
	* to 100 and 10 but may have been set already
	* by the widget that owns the scroll bar.  For
	* example, a scroll bar that is created for a
	* list widget, setting these defaults would
	* override the initial values provided by the
	* list widget.
	*/
}

void destroyWidget () {
	long /*int*/ hwnd = hwndScrollBar ();
	int type = scrollBarType ();
	if (OS.IsWinCE) {
		SCROLLINFO info = new SCROLLINFO ();
		info.cbSize = SCROLLINFO.sizeof;
		info.fMask = OS.SIF_RANGE | OS.SIF_PAGE;
		info.nPage = 101;
		info.nMax = 100;
		info.nMin = 0;
		OS.SetScrollInfo (hwnd, type, info, true);
	} else {
		OS.ShowScrollBar (hwnd, type, false);
	}
	parent.destroyScrollBar (style);
	releaseHandle ();
	//This code is intentionally commented
	//parent.sendEvent (SWT.Resize);
}

Rectangle getBounds () {
//	checkWidget ();
	parent.forceResize ();
	RECT rect = new RECT ();
	OS.GetClientRect (parent.scrolledHandle (), rect);
	int x = 0, y = 0, width, height;
	if ((style & SWT.HORIZONTAL) != 0) {
		y = rect.bottom - rect.top;
		width = rect.right - rect.left;
		height = OS.GetSystemMetrics (OS.SM_CYHSCROLL);
	} else {
		x = rect.right - rect.left;
		width = OS.GetSystemMetrics (OS.SM_CXVSCROLL);
		height = rect.bottom - rect.top;
	}
	return new Rectangle (x, y, width, height);
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget();
	return (state & DISABLED) == 0;
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
	checkWidget();
	return increment;
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
	checkWidget();
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_RANGE;
	long /*int*/ hwnd = hwndScrollBar ();
	int type = scrollBarType ();
	OS.GetScrollInfo (hwnd, type, info);
	return info.nMax;
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
	checkWidget();
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_RANGE;
	long /*int*/ hwnd = hwndScrollBar ();
	int type = scrollBarType ();
	OS.GetScrollInfo (hwnd, type, info);
	return info.nMin;
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
	checkWidget();
	return pageIncrement;
}

/**
 * Returns the receiver's parent, which must be a Scrollable.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Scrollable getParent () {
	checkWidget();
	return parent;
}

/**
 * Returns the single 'selection' that is the receiver's value.
 *
 * @return the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelection () {
	checkWidget();
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_POS;
	long /*int*/ hwnd = hwndScrollBar ();
	int type = scrollBarType ();
	OS.GetScrollInfo (hwnd, type, info);
	return info.nPos;
}

/**
 * Returns a point describing the receiver's size. The
 * x coordinate of the result is the width of the receiver.
 * The y coordinate of the result is the height of the
 * receiver.
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
	parent.forceResize ();
	RECT rect = new RECT ();
	OS.GetClientRect (parent.scrolledHandle (), rect);
	int width, height;
	if ((style & SWT.HORIZONTAL) != 0) {
		width = rect.right - rect.left;
		height = OS.GetSystemMetrics (OS.SM_CYHSCROLL);
	} else {
		width = OS.GetSystemMetrics (OS.SM_CXVSCROLL);
		height = rect.bottom - rect.top;
	}
	return new Point (width, height);
}

/**
 * Returns the receiver's thumb value.
 *
 * @return the thumb value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ScrollBar
 */
public int getThumb () {
	checkWidget();
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_PAGE;
	long /*int*/ hwnd = hwndScrollBar ();
	int type = scrollBarType ();
	OS.GetScrollInfo (hwnd, type, info);
	if (info.nPage != 0) --info.nPage;
	return info.nPage;
}

/**
 * Returns a rectangle describing the size and location of the
 * receiver's thumb relative to its parent.
 * 
 * @return the thumb bounds, relative to the {@link #getParent() parent}
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.6
 */
public Rectangle getThumbBounds () {
	checkWidget();
	parent.forceResize ();
	SCROLLBARINFO info = new SCROLLBARINFO();
	info.cbSize = SCROLLBARINFO.sizeof;
	int x, y, width, height;
	if ((style & SWT.HORIZONTAL) != 0) {
		OS.GetScrollBarInfo(parent.handle, OS.OBJID_HSCROLL, info);
		x = info.rcScrollBar.left + info.xyThumbTop;
		y = info.rcScrollBar.top;
		width = info.xyThumbBottom - info.xyThumbTop;
		height = info.rcScrollBar.bottom - info.rcScrollBar.top;
	} else {
		OS.GetScrollBarInfo(parent.handle, OS.OBJID_VSCROLL, info);
		x = info.rcScrollBar.left;
		y = info.rcScrollBar.top + info.xyThumbTop;
		width = info.rcScrollBar.right - info.rcScrollBar.left;
		height = info.xyThumbBottom - info.xyThumbTop;
	}
	RECT rect = new RECT ();
	rect.left = x;
	rect.top  = y;
	rect.right = x + width;
	rect.bottom = y + height;
	OS.MapWindowPoints (0, parent.handle, rect, 2);
	return new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
}

/**
 * Returns a rectangle describing the size and location of the
 * receiver's thumb track relative to its parent. This rectangle
 * comprises the areas 2, 3, and 4 as described in {@link ScrollBar}.
 * 
 * @return the thumb track bounds, relative to the {@link #getParent() parent}
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.6
 */
public Rectangle getThumbTrackBounds () {
	checkWidget();
	parent.forceResize ();
	SCROLLBARINFO info = new SCROLLBARINFO();
	info.cbSize = SCROLLBARINFO.sizeof;
	int x = 0, y = 0, width, height;
	if ((style & SWT.HORIZONTAL) != 0) {
		OS.GetScrollBarInfo(parent.handle, OS.OBJID_HSCROLL, info);
		int size = OS.GetSystemMetrics (OS.SM_CYHSCROLL);
		y = info.rcScrollBar.top;
		width = info.rcScrollBar.right - info.rcScrollBar.left;
		height = size;
		if (width <= 2 * size) {
			x = info.rcScrollBar.left + width / 2;
			width = 0;
		} else {
			x = info.rcScrollBar.left + size;
			width -= 2 * size;
		}
	} else {
		OS.GetScrollBarInfo(parent.handle, OS.OBJID_VSCROLL, info);
		int size = OS.GetSystemMetrics (OS.SM_CYVSCROLL);
		x = info.rcScrollBar.left;
		width = size;
		height = info.rcScrollBar.bottom - info.rcScrollBar.top;
		if (height <= 2 * size) {
			y = info.rcScrollBar.top + height / 2;
			height = 0;
		} else {
			y = info.rcScrollBar.top + size;
			height -= 2 * size;
		}
	}
	RECT rect = new RECT ();
	rect.left = x;
	rect.top  = y;
	rect.right = x + width;
	rect.bottom = y + height;
	OS.MapWindowPoints (0, parent.handle, rect, 2);
	return new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
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
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION(4, 10)) {
		SCROLLBARINFO psbi = new SCROLLBARINFO ();
		psbi.cbSize = SCROLLBARINFO.sizeof;
		int idObject = (style & SWT.VERTICAL) != 0 ? OS.OBJID_VSCROLL : OS.OBJID_HSCROLL;
		OS.GetScrollBarInfo (hwndScrollBar (), idObject, psbi);
		return (psbi.rgstate [0] & OS.STATE_SYSTEM_INVISIBLE) == 0;
	}
	return (state & HIDDEN) == 0;
}

long /*int*/ hwndScrollBar () {
	return parent.scrolledHandle ();
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget();
	return getEnabled () && parent.isEnabled ();
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
	return getVisible () && parent.isVisible ();
}

void releaseHandle () {
	super.releaseHandle ();
	parent = null;
}

void releaseParent () {
	super.releaseParent ();
	if (parent.horizontalBar == this) parent.horizontalBar = null;
	if (parent.verticalBar == this) parent.verticalBar = null;
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

int scrollBarType () {
	return (style & SWT.VERTICAL) != 0 ? OS.SB_VERT : OS.SB_HORZ;
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget();
	/*
	* This line is intentionally commented.  Currently
	* always show scrollbar as being enabled and visible.
	*/
//	if (OS.IsWinCE) error (SWT.ERROR_NOT_IMPLEMENTED);
	if (!OS.IsWinCE) {
		long /*int*/ hwnd = hwndScrollBar ();
		int type = scrollBarType ();
		int flags = enabled ? OS.ESB_ENABLE_BOTH : OS.ESB_DISABLE_BOTH;
		OS.EnableScrollBar (hwnd, type, flags);
		if (enabled) {
			state &= ~DISABLED;
		} else {
			state |= DISABLED;
		}
	}
}

/**
 * Sets the amount that the receiver's value will be
 * modified by when the up/down (or right/left) arrows
 * are pressed to the argument, which must be at least 
 * one.
 *
 * @param value the new increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setIncrement (int value) {
	checkWidget();
	if (value < 1) return;
	increment = value;
}

/**
 * Sets the maximum. If this value is negative or less than or
 * equal to the minimum, the value is ignored. If necessary, first
 * the thumb and then the selection are adjusted to fit within the
 * new range.
 *
 * @param value the new maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMaximum (int value) {
	checkWidget();
	if (value < 0) return;
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	long /*int*/ hwnd = hwndScrollBar ();
	int type = scrollBarType ();
	info.fMask = OS.SIF_RANGE | OS.SIF_DISABLENOSCROLL;
	OS.GetScrollInfo (hwnd, type, info);
	if (value - info.nMin - info.nPage < 1) return;
	info.nMax = value;
	SetScrollInfo (hwnd, type, info, true);
}

/**
 * Sets the minimum value. If this value is negative or greater
 * than or equal to the maximum, the value is ignored. If necessary,
 * first the thumb and then the selection are adjusted to fit within
 * the new range.
 *
 * @param value the new minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinimum (int value) {
	checkWidget();
	if (value < 0) return;
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	long /*int*/ hwnd = hwndScrollBar ();
	int type = scrollBarType ();
	info.fMask = OS.SIF_RANGE | OS.SIF_DISABLENOSCROLL;
	OS.GetScrollInfo (hwnd, type, info);
	if (info.nMax - value - info.nPage < 1) return;
	info.nMin = value;
	SetScrollInfo (hwnd, type, info, true);
}

/**
 * Sets the amount that the receiver's value will be
 * modified by when the page increment/decrement areas
 * are selected to the argument, which must be at least
 * one.
 *
 * @param value the page increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setPageIncrement (int value) {
	checkWidget();
	if (value < 1) return;
	pageIncrement = value;
}

boolean SetScrollInfo (long /*int*/ hwnd, int flags, SCROLLINFO info, boolean fRedraw) {
	/*
	* Bug in Windows.  For some reason, when SetScrollInfo()
	* is used with SIF_POS and the scroll bar is hidden,
	* the opposite scroll bar is incorrectly made visible
	* so that the next time the parent is resized (or another
	* scroll bar operation is performed), the opposite scroll
	* bar draws.  The fix is to hide both scroll bars.
	*/
	boolean barVisible = false;
	boolean visible = getVisible ();
	
	/*
	* This line is intentionally commented.  Currently
	* always show scrollbar as being enabled and visible.
	*/
//	if (OS.IsWinCE) error (SWT.ERROR_NOT_IMPLEMENTED);
	ScrollBar bar = null;
	if (!OS.IsWinCE) {
		switch (flags) {
			case OS.SB_HORZ:
				bar = parent.getVerticalBar ();
				break;
			case OS.SB_VERT:
				bar = parent.getHorizontalBar ();
				break;
		}
		barVisible = bar != null && bar.getVisible ();
	}
	if (!visible || (state & DISABLED) != 0) fRedraw = false;
	boolean result = OS.SetScrollInfo (hwnd, flags, info, fRedraw);
	
	/*
	* Bug in Windows.  For some reason, when the widget
	* is a standard scroll bar, and SetScrollInfo() is
	* called with SIF_RANGE or SIF_PAGE, the widget is
	* incorrectly made visible so that the next time the
	* parent is resized (or another scroll bar operation
	* is performed), the scroll bar draws.  The fix is
	* to hide the scroll bar (again) when already hidden.
	*/
	if (!visible) {
		/*
		* This line is intentionally commented.  Currently
		* always show scrollbar as being enabled and visible.
		*/
//		if (OS.IsWinCE) error (SWT.ERROR_NOT_IMPLEMENTED);
		if (!OS.IsWinCE) {
			OS.ShowScrollBar (hwnd, !barVisible ? OS.SB_BOTH : flags, false);
		}
	}
	
	/*
	* Bug in Windows.  When only one scroll bar is visible,
	* and the thumb changes using SIF_RANGE or SIF_PAGE
	* from being visible to hidden, the opposite scroll
	* bar is incorrectly made visible.  The next time the
	* parent is resized (or another scroll bar operation
	* is performed), the opposite scroll bar draws.  The
	* fix is to hide the opposite scroll bar again. 
	*  
	* NOTE: This problem only happens on Vista
	*/
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
		if (visible && bar != null && !barVisible) {
			OS.ShowScrollBar (hwnd, flags == OS.SB_HORZ ? OS.SB_VERT : OS.SB_HORZ, false);
		}
	}
		
	/*
	* Feature in Windows.  Using SIF_DISABLENOSCROLL,
	* SetScrollInfo () can change enabled and disabled
	* state of the scroll bar causing a scroll bar that
	* was disabled by the application to become enabled.
	* The fix is to disable the scroll bar (again) when
	* the application has disabled the scroll bar.
	*/
	if ((state & DISABLED) != 0) {
		/*
		* This line is intentionally commented.  Currently
		* always show scrollbar as being enabled and visible.
		*/
//		if (OS.IsWinCE) error (SWT.ERROR_NOT_IMPLEMENTED);
		if (!OS.IsWinCE) {
			OS.EnableScrollBar (hwnd, flags, OS.ESB_DISABLE_BOTH);
		}
	}
	return result;
}

/**
 * Sets the single <em>selection</em> that is the receiver's
 * value to the argument which must be greater than or equal
 * to zero.
 *
 * @param selection the new selection (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int selection) {
	checkWidget();
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	long /*int*/ hwnd = hwndScrollBar ();
	int type = scrollBarType ();
	info.fMask = OS.SIF_POS;
	info.nPos = selection;
	SetScrollInfo (hwnd, type, info, true);
}

/**
 * Sets the thumb value. The thumb value should be used to represent 
 * the size of the visual portion of the current range. This value is
 * usually the same as the page increment value.
 * <p>
 * This new value will be ignored if it is less than one, and will be 
 * clamped if it exceeds the receiver's current range.
 * </p>
 *
 * @param value the new thumb value, which must be at least one and not
 * larger than the size of the current range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setThumb (int value) {
	checkWidget();
	if (value < 1) return;
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	long /*int*/ hwnd = hwndScrollBar ();
	int type = scrollBarType ();
	info.fMask = OS.SIF_PAGE | OS.SIF_RANGE | OS.SIF_DISABLENOSCROLL;
	OS.GetScrollInfo (hwnd, type, info);
	info.nPage = value;
	if (info.nPage != 0) info.nPage++;
	SetScrollInfo (hwnd, type, info, true);
}

/**
 * Sets the receiver's selection, minimum value, maximum
 * value, thumb, increment and page increment all at once.
 * <p>
 * Note: This is similar to setting the values individually
 * using the appropriate methods, but may be implemented in a 
 * more efficient fashion on some platforms.
 * </p>
 *
 * @param selection the new selection value
 * @param minimum the new minimum value
 * @param maximum the new maximum value
 * @param thumb the new thumb value
 * @param increment the new increment value
 * @param pageIncrement the new pageIncrement value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setValues (int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement) {
	checkWidget();
	if (minimum < 0) return;
	if (maximum < 0) return;
	if (thumb < 1) return;
	if (increment < 1) return;
	if (pageIncrement < 1) return;
	this.increment = increment;	
	this.pageIncrement = pageIncrement;
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_POS | OS.SIF_PAGE | OS.SIF_RANGE | OS.SIF_DISABLENOSCROLL;
	info.nPos = selection;
	info.nMin = minimum;
	info.nMax = maximum;
	info.nPage = thumb;
	if (info.nPage != 0) info.nPage++;
	long /*int*/ hwnd = hwndScrollBar ();
	int type = scrollBarType ();
	SetScrollInfo (hwnd, type, info, true);
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
	if (visible == getVisible ()) return;
	
	/*
	* On Windows CE, use SIF_DISABLENOSCROLL to show and
	* hide the scroll bar when the page size is equal to
	* the range.
	*/
	if (OS.IsWinCE) {
		SCROLLINFO info = new SCROLLINFO ();
		info.cbSize = SCROLLINFO.sizeof;
		long /*int*/ hwnd = hwndScrollBar ();
		int type = scrollBarType ();
		info.fMask = OS.SIF_RANGE | OS.SIF_PAGE;
		if (visible) info.fMask |= OS.SIF_DISABLENOSCROLL;
		OS.GetScrollInfo (hwnd, type, info);
		if (info.nPage == info.nMax - info.nMin + 1) {
			/*
			* Bug in Windows.  When the only changed flag to
			* SetScrollInfo () is OS.SIF_DISABLENOSCROLL, 
			* Windows does not update the scroll bar state.
			* The fix is to increase and then decrease the
			* maximum, causing Windows to honour the flag.
			*/  
			int max = info.nMax;
			info.nMax++;
			OS.SetScrollInfo (hwnd, type, info, false);
			info.nMax = max;
			OS.SetScrollInfo (hwnd, type, info, true);
		} else {
        	/*
        	* This line is intentionally commented.  Currently
        	* always show scrollbar as being enabled and visible.
        	*/
//			if (OS.IsWinCE) error (SWT.ERROR_NOT_IMPLEMENTED);
		}
		return;
	}
	
	/*
	* Set the state bits before calling ShowScrollBar ()
	* because hiding and showing the scroll bar can cause
	* WM_SIZE messages when the client area is resized.
	* Setting the state before the call means that code
	* that runs during WM_SIZE that queries the visibility
	* of the scroll bar will get the correct value.
	*/
	state = visible ? state & ~HIDDEN : state | HIDDEN;
	long /*int*/ hwnd = hwndScrollBar ();
	int type = scrollBarType ();
	if (OS.ShowScrollBar (hwnd, type, visible)) {
		/*
		* Bug in Windows.  For some reason, when the widget
		* is a standard scroll bar, and SetScrollInfo () is
		* called with SIF_RANGE or SIF_PAGE while the widget
		* is not visible, the widget is incorrectly disabled
		* even though the values for SIF_RANGE and SIF_PAGE,
		* when set for a visible scroll bar would not disable
		* the scroll bar.  The fix is to enable the scroll bar
		* when not disabled by the application and the current
		* scroll bar ranges would cause the scroll bar to be
		* enabled had they been set when the scroll bar was
		* visible.
		*/
		if ((state & DISABLED) == 0) {
			SCROLLINFO info = new SCROLLINFO ();
			info.cbSize = SCROLLINFO.sizeof;
			info.fMask = OS.SIF_RANGE | OS.SIF_PAGE;
			OS.GetScrollInfo (hwnd, type, info);
			if (info.nMax - info.nMin - info.nPage >= 0) {
				OS.EnableScrollBar (hwnd, type, OS.ESB_ENABLE_BOTH);
			}
		}
		sendEvent (visible ? SWT.Show : SWT.Hide);
		// widget could be disposed at this point
	}
}

LRESULT wmScrollChild (long /*int*/ wParam, long /*int*/ lParam) {

	/* Do nothing when scrolling is ending */
	int code = OS.LOWORD (wParam);
	if (code == OS.SB_ENDSCROLL) return null;

	/*
	* Send the event because WM_HSCROLL and
	* WM_VSCROLL are sent from a modal message
	* loop in Windows that is active when the
	* user is scrolling.
	*/
	Event event = new Event ();
	switch (code) {
		case OS.SB_THUMBPOSITION:	event.detail = SWT.NONE;  break;
		case OS.SB_THUMBTRACK:		event.detail = SWT.DRAG;  break;
		case OS.SB_TOP: 			event.detail = SWT.HOME;  break;
		case OS.SB_BOTTOM:			event.detail = SWT.END;  break;
		case OS.SB_LINEDOWN:		event.detail = SWT.ARROW_DOWN;  break;
		case OS.SB_LINEUP: 			event.detail = SWT.ARROW_UP;  break;
		case OS.SB_PAGEDOWN:		event.detail = SWT.PAGE_DOWN;  break;
		case OS.SB_PAGEUP:			event.detail = SWT.PAGE_UP;  break;
	}
	sendSelectionEvent  (SWT.Selection, event, true);
	// the widget could be destroyed at this point
	return null;
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are selectable user interface
 * objects that represent a range of positive, numeric values. 
 * <p>
 * At any given moment, a given scroll bar will have a 
 * single <em>selection</em> that is considered to be its
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
 */
public class ScrollBar extends Widget {
	Scrollable parent;

ScrollBar (Scrollable parent, int style, int handle) {
	super (parent, checkStyle (style));
	this.parent = parent;
	this.handle = handle;
	state |= HANDLE;
	createWidget (0);
}

ScrollBar (Scrollable parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (0);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's value changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the event object detail field contains one of the following values:
 * <code>0</code> - for the end of a drag.
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
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

void createHandle (int index) {
	state |= HANDLE;
	if (handle != 0) return;
	int parentHandle = parent.scrolledHandle;
	int orientation, sizeArg, size, basicFlags;
	if ((style & SWT.HORIZONTAL) != 0) {
		orientation = OS.Pt_HORIZONTAL;
		sizeArg = OS.Pt_ARG_HEIGHT;
		size = display.SCROLLBAR_HEIGHT;
		basicFlags = display.SCROLLBAR_HORIZONTAL_BASIC_FLAGS;
	} else {
		orientation = OS.Pt_VERTICAL;
		sizeArg = OS.Pt_ARG_WIDTH;
		size = display.SCROLLBAR_WIDTH;
		basicFlags = display.SCROLLBAR_VERTICAL_BASIC_FLAGS;
	}
	int [] args = {
		sizeArg, size, 0,
		OS.Pt_ARG_MAXIMUM, 99, 0,
		OS.Pt_ARG_PAGE_INCREMENT, 10, 0,
		OS.Pt_ARG_SLIDER_SIZE, 10, 0,
		OS.Pt_ARG_BASIC_FLAGS, basicFlags, ~0,
		OS.Pt_ARG_ORIENTATION, orientation, 0,
		OS.Pt_ARG_FLAGS, 0, OS.Pt_GETS_FOCUS,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (OS.PtScrollbar (), parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
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
	checkWidget ();
	int topHandle = topHandle ();
	return (OS.PtWidgetFlags (topHandle) & OS.Pt_BLOCKED) == 0;
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
	int [] args = {OS.Pt_ARG_INCREMENT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
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
	int [] args = {OS.Pt_ARG_MINIMUM, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
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
	int [] args = {OS.Pt_ARG_MAXIMUM, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1] + 1;
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
	int [] args = {OS.Pt_ARG_PAGE_INCREMENT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

/**
 * Returns the receiver's parent, which must be scrollable.
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
 * Returns the single <em>selection</em> that is the receiver's value.
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
	int [] args = {OS.Pt_ARG_GAUGE_VALUE, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
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
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return new Point (args [1], args [4]);
}

/**
 * Answers the size of the receiver's thumb relative to the
 * difference between its maximum and minimum values.
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
	int [] args = {OS.Pt_ARG_SLIDER_SIZE, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
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
	checkWidget ();
	int topHandle = topHandle ();
	return (OS.PtWidgetFlags (topHandle) & OS.Pt_DELAY_REALIZE) == 0;
}

void hookEvents () {
	int windowProc = display.windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_SCROLL_MOVE, windowProc, OS.Pt_CB_SCROLL_MOVE);
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
	return OS.PtWidgetIsRealized (handle);
}

int Pt_CB_SCROLL_MOVE (int widget, int info) {
	if (info == 0) return OS.Pt_CONTINUE;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.cbdata == 0) return OS.Pt_CONTINUE;
	PtScrollbarCallback_t cb = new PtScrollbarCallback_t ();
	OS.memmove (cb, cbinfo.cbdata, PtScrollbarCallback_t.sizeof);
	Event event = new Event ();
	switch (cb.action) {
		case OS.Pt_SCROLL_DRAGGED:
			event.detail = SWT.DRAG;
			break;
		case OS.Pt_SCROLL_TO_MIN:
			event.detail = SWT.HOME;
			break;
		case OS.Pt_SCROLL_TO_MAX:
			event.detail = SWT.END;
			break;
		case OS.Pt_SCROLL_INCREMENT:
			event.detail = SWT.ARROW_DOWN;
			break;
		case OS.Pt_SCROLL_DECREMENT :
			event.detail = SWT.ARROW_UP;
			break;
		case OS.Pt_SCROLL_PAGE_DECREMENT:
			event.detail = SWT.PAGE_UP;
			break;
		case OS.Pt_SCROLL_PAGE_INCREMENT:
			event.detail = SWT.PAGE_DOWN;
			break;
	}
	sendEvent(SWT.Selection, event);
	return OS.Pt_CONTINUE;
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

void setBounds (int x, int y, int width, int height) {
	PhArea_t area = new PhArea_t ();
	area.pos_x = (short) x;
	area.pos_y = (short) y;
	area.size_w = (short) (Math.max (width, 0));
	area.size_h = (short) (Math.max (height, 0));
	int ptr = OS.malloc (PhArea_t.sizeof);
	OS.memmove (ptr, area, PhArea_t.sizeof);
	OS.PtSetResource (handle, OS.Pt_ARG_AREA, ptr, 0);
	OS.free (ptr);
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
	checkWidget ();
	int topHandle = topHandle ();
	int flags = enabled ? 0 : OS.Pt_BLOCKED | OS.Pt_GHOST;
	OS.PtSetResource (topHandle, OS.Pt_ARG_FLAGS, flags, OS.Pt_BLOCKED | OS.Pt_GHOST);
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
	OS.PtSetResource (handle, OS.Pt_ARG_INCREMENT, value, 0);
}

/**
 * Sets the maximum value which the receiver will allow
 * to be the argument which must be greater than or
 * equal to zero.
 *
 * @param value the new maximum (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMaximum (int value) {
	checkWidget();
	int [] args = {
		OS.Pt_ARG_MAXIMUM, 0, 0,
		OS.Pt_ARG_MINIMUM, 0, 0,
		OS.Pt_ARG_SLIDER_SIZE, 0, 0,
		OS.Pt_ARG_GAUGE_VALUE, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	int minimum = args [4];
	if (value <= minimum) return;
	int thumb = args [7];
	thumb = Math.min (thumb, value - minimum);
	int selection = args [10];
	selection = Math.min (selection, value - thumb);
	args [1] = value - 1;
	args [7] = thumb;
	args [10] = selection;
	OS.PtSetResources (handle, args.length / 3, args);
}

/**
 * Sets the minimum value which the receiver will allow
 * to be the argument which must be greater than or
 * equal to zero.
 *
 * @param value the new minimum (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinimum (int value) {
	checkWidget();
	if (value < 0) return;
	int [] args = {
		OS.Pt_ARG_MAXIMUM, 0, 0,
		OS.Pt_ARG_MINIMUM, 0, 0,
		OS.Pt_ARG_SLIDER_SIZE, 0, 0,
		OS.Pt_ARG_GAUGE_VALUE, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	int maximum = args [1] + 1;
	if (value >= maximum) return;
	int thumb = args [7];
	thumb = Math.min (thumb, maximum - value);
	int selection = args [10];
	selection = Math.max (selection, value);
	args [4] = value;
	args [7] = thumb;
	args [10] = selection;
	OS.PtSetResources (handle, args.length / 3, args);
}

/**
 * Sets the amount that the receiver's value will be
 * modified by when the page increment/decrement areas
 * are selected to the argument, which must be at least
 * one.
 *
 * @return the page increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setPageIncrement (int value) {
	checkWidget();
	OS.PtSetResource (handle, OS.Pt_ARG_PAGE_INCREMENT, value, 0);
}

/**
 * Sets the single <em>selection</em> that is the receiver's
 * value to the argument which must be greater than or equal
 * to zero.
 *
 * @param value the new selection (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int value) {
	checkWidget();
	OS.PtSetResource (handle, OS.Pt_ARG_GAUGE_VALUE, value, 0);
}

/**
 * Sets the size of the receiver's thumb relative to the
 * difference between its maximum and minimum values to the
 * argument which must be at least one.
 *
 * @param value the new thumb value (must be at least one)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ScrollBar
 */
public void setThumb (int value) {
	checkWidget();
	if (value < 1) return;
	int [] args = {OS.Pt_ARG_MINIMUM, 0, 0, OS.Pt_ARG_MAXIMUM, 0, 0}; 
	OS.PtGetResources (handle, args.length / 3, args);
	int minimum = args [1];
	int maximum = args [4] + 1;
	value = Math.min (value, maximum - minimum);
	OS.PtSetResource (handle, OS.Pt_ARG_SLIDER_SIZE, value, 0);
}

/**
 * Sets the receiver's selection, minimum value, maximum
 * value, thumb, increment and page increment all at once.
 * <p>
 * Note: This is equivalent to setting the values individually
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
	thumb = Math.min (thumb, maximum - minimum);
	int [] args = {
		OS.Pt_ARG_MAXIMUM, maximum - 1, 0,
		OS.Pt_ARG_MINIMUM, minimum, 0,
		OS.Pt_ARG_SLIDER_SIZE, thumb, 0,
		OS.Pt_ARG_GAUGE_VALUE, selection, 0,
		OS.Pt_ARG_INCREMENT, increment, 0,
		OS.Pt_ARG_PAGE_INCREMENT, pageIncrement, 0,
	};
	OS.PtSetResources (handle, args.length / 3, args);
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
	int topHandle = topHandle ();
	int oldFlags = OS.PtWidgetFlags (topHandle);
	int flags = visible ? 0 : OS.Pt_DELAY_REALIZE;
	OS.PtSetResource (topHandle, OS.Pt_ARG_FLAGS, flags, OS.Pt_DELAY_REALIZE);
	if ((oldFlags & OS.Pt_DELAY_REALIZE) == flags) return;
	parent.resizeClientArea ();
	if (visible) {
		sendEvent (SWT.Show);
		OS.PtRealizeWidget (topHandle);
	} else {
		OS.PtUnrealizeWidget (topHandle);
		sendEvent(SWT.Hide);
	}
}

}

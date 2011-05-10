/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.gtk.*;
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
	int /*long*/ adjustmentHandle;
	int detail;
	boolean dragSent;
	
ScrollBar () {
}

/**
* Creates a new instance of the widget.
*/
ScrollBar (Scrollable parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (0);
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

void deregister () {
	super.deregister ();
	if (adjustmentHandle != 0) display.removeWidget (adjustmentHandle);
}

void destroyHandle () {
	super.destroyWidget ();
}

void destroyWidget () {
	parent.destroyScrollBar (this);
	releaseHandle ();
	//parent.sendEvent (SWT.Resize);
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
	if (handle != 0) return OS.GTK_WIDGET_SENSITIVE (handle);
	return true;
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
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	return (int) adjustment.step_increment;
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
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	return (int) adjustment.upper;
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
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	return (int) adjustment.lower;
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
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	return (int) adjustment.page_increment;
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
	checkWidget ();
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
	checkWidget ();
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	return (int) adjustment.value;
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
	checkWidget ();
	if (handle == 0) return new Point (0,0);
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (handle, requisition);
	return new Point (requisition.width, requisition.height);
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
	checkWidget ();
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	return (int) adjustment.page_size;
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
	int slider_start = OS.GTK_RANGE_SLIDER_START (handle);
	int slider_end = OS.GTK_RANGE_SLIDER_END (handle);
	int x, y, width, height;
	if ((style & SWT.VERTICAL) != 0) {
		x = OS.GTK_WIDGET_X (handle);
		y = slider_start;
		width = OS.GTK_WIDGET_WIDTH (handle);
		height = slider_end - slider_start;
	} else {
		x = slider_start;
		y = OS.GTK_WIDGET_Y (handle);
		width = slider_end - slider_start;
		height = OS.GTK_WIDGET_HEIGHT (handle);
	}
	Rectangle rect = new Rectangle(x, y, width, height);
	int [] origin_x = new int [1], origin_y = new int [1];
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (parent.scrolledHandle);
	if (window != 0) OS.gdk_window_get_origin (window, origin_x, origin_y);
	rect.x += origin_x [0];
	rect.y += origin_y [0];
	window = OS.GTK_WIDGET_WINDOW (parent.handle);
	if (window != 0) OS.gdk_window_get_origin (window, origin_x, origin_y);
	rect.x -= origin_x [0];
	rect.y -= origin_y [0];
	return rect;
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
	int x = 0, y = 0, width, height;
	boolean hasA = OS.GTK_RANGE_HAS_STEPPER_A (handle);
	boolean hasB = OS.GTK_RANGE_HAS_STEPPER_B (handle);
	boolean hasC = OS.GTK_RANGE_HAS_STEPPER_C (handle);
	boolean hasD = OS.GTK_RANGE_HAS_STEPPER_D (handle);
	if ((style & SWT.VERTICAL) != 0) {
		int stepperSize = OS.GTK_WIDGET_WIDTH (handle);
		x = OS.GTK_WIDGET_X (handle);
		if (hasA) y += stepperSize;
		if (hasB) y += stepperSize;
		width = OS.GTK_WIDGET_WIDTH (handle);
		height = OS.GTK_WIDGET_HEIGHT (handle) - y;
		if (hasC) height -= stepperSize;
		if (hasD) height -= stepperSize;
		if (height < 0) {
			y = OS.GTK_RANGE_SLIDER_START (handle);
			height = 0;
		}
	} else {
		int stepperSize = OS.GTK_WIDGET_HEIGHT (handle);
		if (hasA) x += stepperSize;
		if (hasB) x += stepperSize;
		y = OS.GTK_WIDGET_Y (handle);
		width = OS.GTK_WIDGET_WIDTH (handle) - x;
		if (hasC) width -= stepperSize;
		if (hasD) width -= stepperSize;
		height = OS.GTK_WIDGET_HEIGHT (handle);
		if (width < 0) {
			x = OS.GTK_RANGE_SLIDER_START (handle);
			width = 0;
		}
	}
	Rectangle rect = new Rectangle(x, y, width, height);
	int [] origin_x = new int [1], origin_y = new int [1];
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (parent.scrolledHandle);
	if (window != 0) OS.gdk_window_get_origin (window, origin_x, origin_y);
	rect.x += origin_x [0];
	rect.y += origin_y [0];
	window = OS.GTK_WIDGET_WINDOW (parent.handle);
	if (window != 0) OS.gdk_window_get_origin (window, origin_x, origin_y);
	rect.x -= origin_x [0];
	rect.y -= origin_y [0];
	return rect;
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
	int /*long*/ scrolledHandle = parent.scrolledHandle;
	int [] hsp = new int [1], vsp = new int [1];
	OS.gtk_scrolled_window_get_policy (scrolledHandle, hsp, vsp);
	if ((style & SWT.HORIZONTAL) != 0) {
		return hsp [0] != OS.GTK_POLICY_NEVER;
	} else {
		return vsp [0] != OS.GTK_POLICY_NEVER;
	}
}

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ eventPtr) {
	int /*long*/ result = super.gtk_button_press_event (widget, eventPtr);
	if (result != 0) return result;
	detail = OS.GTK_SCROLL_NONE;
	dragSent = false;	
	return result;
}

int /*long*/ gtk_change_value (int /*long*/ widget, int /*long*/ scroll, int /*long*/ value1, int /*long*/ value2) {
	detail = (int)/*64*/scroll;
	return 0;
}

int /*long*/ gtk_value_changed (int /*long*/ adjustment) {
	Event event = new Event ();
	dragSent = detail == OS.GTK_SCROLL_JUMP;
	switch (detail) {
		case OS.GTK_SCROLL_NONE:			event.detail = SWT.NONE; break;
		case OS.GTK_SCROLL_JUMP:			event.detail = SWT.DRAG; break;
		case OS.GTK_SCROLL_START:			event.detail = SWT.HOME; break;
		case OS.GTK_SCROLL_END:				event.detail = SWT.END; break;
		case OS.GTK_SCROLL_PAGE_DOWN:
		case OS.GTK_SCROLL_PAGE_RIGHT:
		case OS.GTK_SCROLL_PAGE_FORWARD:	event.detail = SWT.PAGE_DOWN; break;
		case OS.GTK_SCROLL_PAGE_UP:
		case OS.GTK_SCROLL_PAGE_LEFT:
		case OS.GTK_SCROLL_PAGE_BACKWARD:	event.detail = SWT.PAGE_UP; break;
		case OS.GTK_SCROLL_STEP_DOWN:
		case OS.GTK_SCROLL_STEP_RIGHT:
		case OS.GTK_SCROLL_STEP_FORWARD:	event.detail = SWT.ARROW_DOWN; break;
		case OS.GTK_SCROLL_STEP_UP:
		case OS.GTK_SCROLL_STEP_LEFT:
		case OS.GTK_SCROLL_STEP_BACKWARD:	event.detail = SWT.ARROW_UP; break;
	}
	if (!dragSent) detail = OS.GTK_SCROLL_NONE;
	sendSelectionEvent (SWT.Selection, event, false);
	parent.updateScrollBarValue (this);
	return 0;
}

int /*long*/ gtk_event_after (int /*long*/ widget, int /*long*/ gdkEvent) {
	GdkEvent gtkEvent = new GdkEvent ();
	OS.memmove (gtkEvent, gdkEvent, GdkEvent.sizeof);
	switch (gtkEvent.type) {
		case OS.GDK_BUTTON_RELEASE: {
			GdkEventButton gdkEventButton = new GdkEventButton ();
			OS.memmove (gdkEventButton, gdkEvent, GdkEventButton.sizeof);
			if (gdkEventButton.button == 1 && detail == OS.GTK_SCROLL_JUMP) {
				if (!dragSent) {
					Event event = new Event ();
					event.detail = SWT.DRAG;
					sendSelectionEvent (SWT.Selection, event, false);
				}
				sendSelectionEvent (SWT.Selection);
			}
			detail = OS.GTK_SCROLL_NONE;
			dragSent = false;
			break;
		}
	}
	return super.gtk_event_after (widget, gdkEvent);
}

void hookEvents () {
	super.hookEvents ();
	if (OS.GTK_VERSION >= OS.VERSION (2, 6, 0)) {
		OS.g_signal_connect_closure (handle, OS.change_value, display.closures [CHANGE_VALUE], false);
	}
	OS.g_signal_connect_closure (adjustmentHandle, OS.value_changed, display.closures [VALUE_CHANGED], false);
	OS.g_signal_connect_closure_by_id (handle, display.signalIds [EVENT_AFTER], 0, display.closures [EVENT_AFTER], false);
	OS.g_signal_connect_closure_by_id (handle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.closures [BUTTON_PRESS_EVENT], false);	
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
	checkWidget ();
	return getEnabled () && getParent ().getEnabled ();
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
	checkWidget ();
	return getVisible () && getParent ().isVisible ();
}

void register () {
	super.register ();
	if (adjustmentHandle != 0) display.addWidget (adjustmentHandle, this);
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

void releaseWidget () {
	super.releaseWidget ();
	//parent = null;
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
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
	if (handle != 0) OS.gtk_widget_set_sensitive (handle, enabled);
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
	checkWidget ();
	if (value < 1) return;
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	adjustment.step_increment = (float) value;
	OS.memmove (adjustmentHandle, adjustment);
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_adjustment_changed (adjustmentHandle);
	OS.g_signal_handlers_unblock_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
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
	checkWidget ();
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	int minimum = (int) adjustment.lower;
	if (value <= minimum) return;
	adjustment.upper = value;
	adjustment.page_size = Math.min ((int)adjustment.page_size, value - minimum);
	adjustment.value = Math.min ((int)adjustment.value, (int)(value - adjustment.page_size));
	OS.memmove (adjustmentHandle, adjustment);
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_adjustment_changed (adjustmentHandle);
	OS.g_signal_handlers_unblock_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
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
	checkWidget ();
	if (value < 0) return;
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	int maximum = (int) adjustment.upper;
	if (value >= maximum) return;
	adjustment.lower = value;
	adjustment.page_size = Math.min ((int)adjustment.page_size, maximum - value);
	adjustment.value = Math.max ((int)adjustment.value, value);
	OS.memmove (adjustmentHandle, adjustment);
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_adjustment_changed (adjustmentHandle);
	OS.g_signal_handlers_unblock_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

void setOrientation (boolean create) {
	super.setOrientation (create);
	if ((parent.style & SWT.MIRRORED) != 0 || !create) {
		if ((parent.state & CANVAS) != 0) {
			if ((style & SWT.HORIZONTAL) != 0) {
				OS.gtk_range_set_inverted (handle, (parent.style & SWT.RIGHT_TO_LEFT) != 0);
			}
		}
	}
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
	checkWidget ();
	if (value < 1) return;
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	adjustment.page_increment = (float) value;
	OS.memmove (adjustmentHandle, adjustment);
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_adjustment_changed (adjustmentHandle);
	OS.g_signal_handlers_unblock_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
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
public void setSelection (int value) {
	checkWidget ();
	value = Math.min (value, getMaximum() - getThumb());
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_adjustment_set_value (adjustmentHandle, value);
	OS.g_signal_handlers_unblock_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
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
	checkWidget ();
	if (value < 1) return;
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	value = (int) Math.min (value, (int)(adjustment.upper - adjustment.lower)); 
	adjustment.page_size = (double) value;
	adjustment.value = Math.min ((int)adjustment.value, (int)(adjustment.upper - value));
	OS.memmove (adjustmentHandle, adjustment);
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_adjustment_changed (adjustmentHandle);
	OS.g_signal_handlers_unblock_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
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
	checkWidget ();
	if (minimum < 0) return;
	if (maximum < 0) return;
	if (thumb < 1) return;
	if (increment < 1) return;
	if (pageIncrement < 1) return;
	thumb = Math.min (thumb, maximum - minimum);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	adjustment.lower = minimum;
	adjustment.upper = maximum;
	adjustment.step_increment = increment;
	adjustment.page_increment = pageIncrement;
	adjustment.page_size = thumb;
	adjustment.value = Math.min (Math.max (selection, minimum), maximum - thumb);
	OS.memmove (adjustmentHandle, adjustment);
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_adjustment_changed (adjustmentHandle);
	OS.gtk_adjustment_value_changed (adjustmentHandle);
	OS.g_signal_handlers_unblock_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
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
	if (parent.setScrollBarVisible (this, visible)) {
		sendEvent (visible ? SWT.Show : SWT.Hide);
		parent.sendEvent (SWT.Resize);
	}
}

}

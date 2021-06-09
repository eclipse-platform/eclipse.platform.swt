/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
 * Typically, scroll bars will be made up of five areas:</p>
 * <ol>
 * <li>an arrow button for decrementing the value</li>
 * <li>a page decrement area for decrementing the value by a larger amount</li>
 * <li>a <em>thumb</em> for modifying the value by mouse dragging</li>
 * <li>a page increment area for incrementing the value by a larger amount</li>
 * <li>an arrow button for incrementing the value</li>
 * </ol>
 * <p>
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
	long adjustmentHandle;
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

@Override
void deregister () {
	super.deregister ();
	if (adjustmentHandle != 0) display.removeWidget (adjustmentHandle);
}

@Override
void destroyWidget () {
	parent.destroyScrollBar (this);
	releaseHandle ();
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
	if (handle != 0) return GTK.gtk_widget_get_sensitive (handle);
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
	return (int) GTK.gtk_adjustment_get_step_increment (adjustmentHandle);
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
	return (int) GTK.gtk_adjustment_get_upper (adjustmentHandle);
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
	return (int) GTK.gtk_adjustment_get_lower (adjustmentHandle);
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
	return (int) GTK.gtk_adjustment_get_page_increment (adjustmentHandle);
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
	return (int) GTK.gtk_adjustment_get_value (adjustmentHandle);
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
	return DPIUtil.autoScaleDown (getSizeInPixels ());
}

Point getSizeInPixels () {
	checkWidget ();
	if (handle == 0) return new Point (0,0);
	GtkRequisition requisition = new GtkRequisition ();
	gtk_widget_get_preferred_size (handle, requisition);
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
	return (int) GTK.gtk_adjustment_get_page_size (adjustmentHandle);
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
public Rectangle getThumbBounds() {
	checkWidget();
	return DPIUtil.autoScaleDown(getThumbBoundsInPixels());
}

Rectangle getThumbBoundsInPixels() {
	checkWidget();

	int[] slider_start = new int[1], slider_end = new int[1];
	long rangeHandle = GTK.GTK4 ? GTK4.gtk_widget_get_first_child(handle) : handle;
	GTK.gtk_range_get_slider_range(rangeHandle, slider_start, slider_end);

	int x, y, width, height;
	GtkAllocation allocation = new GtkAllocation();
	GTK.gtk_widget_get_allocation(rangeHandle, allocation);
	if ((style & SWT.VERTICAL) != 0) {
		x = allocation.x;
		y = slider_start[0];
		width = allocation.width;
		height = slider_end[0] - slider_start[0];
	} else {
		x = slider_start[0];
		y = allocation.y;
		width = slider_end[0] - slider_start[0];
		height = allocation.height;
	}

	Rectangle rect = new Rectangle(x, y, width, height);
	if (GTK.GTK4) {
		double[] origin_x = new double[1], origin_y = new double[1];
		boolean success = GTK4.gtk_widget_translate_coordinates(parent.scrolledHandle, parent.getShell().shellHandle, 0, 0, origin_x, origin_y);
		if (success) {
			rect.x += origin_x[0];
			rect.y += origin_y[0];
		}
		success = GTK4.gtk_widget_translate_coordinates(parent.handle, parent.getShell().shellHandle, 0, 0, origin_x, origin_y);
		if (success) {
			rect.x -= origin_x[0];
			rect.y -= origin_y[0];
		}
	} else {
		int[] origin_x = new int[1], origin_y = new int[1];
		long window = gtk_widget_get_window(parent.scrolledHandle);
		if (window != 0) GDK.gdk_window_get_origin(window, origin_x, origin_y);
		rect.x += origin_x[0];
		rect.y += origin_y[0];
		window = gtk_widget_get_window(parent.handle);
		if (window != 0) GDK.gdk_window_get_origin(window, origin_x, origin_y);
		rect.x -= origin_x[0];
		rect.y -= origin_y[0];
	}

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
	checkWidget ();
	return DPIUtil.autoScaleDown(getThumbTrackBoundsInPixels());
}

Rectangle getThumbTrackBoundsInPixels () {
	checkWidget();
	int x = 0, y = 0, width, height;

	/*
	 * Only GTK3 scrollbars have steppers, even non-overlay scrollbars in
	 * GTK4 do not have steppers.
	 */
	boolean hasB = false, hasB2 = false, hasF = false, hasF2 = false;
	if (!GTK.GTK4) {
		int[] has_stepper = new int[1];
		GTK3.gtk_widget_style_get(handle, OS.has_backward_stepper, has_stepper, 0);
		hasB = has_stepper[0] != 0;
		GTK3.gtk_widget_style_get(handle, OS.has_secondary_backward_stepper, has_stepper, 0);
		hasB2 = has_stepper[0] != 0;
		GTK3.gtk_widget_style_get(handle, OS.has_forward_stepper, has_stepper, 0);
		hasF = has_stepper[0] != 0;
		GTK3.gtk_widget_style_get(handle, OS.has_secondary_forward_stepper, has_stepper, 0);
		hasF2 = has_stepper[0] != 0;
	}

	GtkAllocation allocation = new GtkAllocation();
	GTK.gtk_widget_get_allocation(handle, allocation);
	if ((style & SWT.VERTICAL) != 0) {
		int stepperSize = allocation.width;
		x = allocation.x;
		if (hasB) y += stepperSize;
		if (hasF2) y += stepperSize;
		width = allocation.width;
		height = allocation.height - y;
		if (hasB2) height -= stepperSize;
		if (hasF) height -= stepperSize;
		if (height < 0) {
			int[] slider_start = new int[1], slider_end = new int[1];
			long rangeHandle = GTK.GTK4 ? GTK4.gtk_widget_get_first_child(handle) : handle;
			GTK.gtk_range_get_slider_range(rangeHandle, slider_start, slider_end);
			y = slider_start[0];
			height = 0;
		}
	} else {
		int stepperSize = allocation.height;
		if (hasB) x += stepperSize;
		if (hasF2) x += stepperSize;
		y = allocation.y;
		width = allocation.width -x;
		if (hasB2) width -= stepperSize;
		if (hasF) width -= stepperSize;
		height = allocation.height;
		if (width < 0) {
			int[] slider_start = new int[1], slider_end = new int[1];
			long rangeHandle = GTK.GTK4 ? GTK4.gtk_widget_get_first_child(handle) : handle;
			GTK.gtk_range_get_slider_range(rangeHandle, slider_start, slider_end);
			x = slider_start[0];
			width = 0;
		}
	}

	Rectangle rect = new Rectangle(x, y, width, height);
	if (GTK.GTK4) {
		double[] origin_x = new double[1], origin_y = new double[1];
		boolean success = GTK4.gtk_widget_translate_coordinates(parent.scrolledHandle, parent.getShell().shellHandle, 0, 0, origin_x, origin_y);
		if (success) {
			rect.x += origin_x[0];
			rect.y += origin_y[0];
		}
		success = GTK4.gtk_widget_translate_coordinates(parent.handle, parent.getShell().shellHandle, 0, 0, origin_x, origin_y);
		if (success) {
			rect.x -= origin_x[0];
			rect.y -= origin_y[0];
		}
	} else {
		int[] origin_x = new int[1], origin_y = new int[1];
		long window = gtk_widget_get_window(parent.scrolledHandle);
		if (window != 0) GDK.gdk_window_get_origin(window, origin_x, origin_y);
		rect.x += origin_x[0];
		rect.y += origin_y[0];
		window = gtk_widget_get_window(parent.handle);
		if (window != 0) GDK.gdk_window_get_origin(window, origin_x, origin_y);
		rect.x -= origin_x[0];
		rect.y -= origin_y[0];
	}

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
	long scrolledHandle = parent.scrolledHandle;
	int [] hsp = new int [1], vsp = new int [1];
	GTK.gtk_scrolled_window_get_policy (scrolledHandle, hsp, vsp);
	if ((style & SWT.HORIZONTAL) != 0) {
		return hsp [0] != GTK.GTK_POLICY_NEVER && hsp[0] != GTK.GTK_POLICY_EXTERNAL && GTK.gtk_widget_get_visible (handle);
	} else {
		return vsp [0] != GTK.GTK_POLICY_NEVER && vsp[0] != GTK.GTK_POLICY_EXTERNAL && GTK.gtk_widget_get_visible (handle);
	}
}

@Override
long gtk_button_press_event (long widget, long eventPtr) {
	long result = super.gtk_button_press_event (widget, eventPtr);
	if (result != 0) return result;
	detail = GTK.GTK_SCROLL_NONE;
	dragSent = false;
	return result;
}

@Override
boolean gtk_change_value (long widget, int scroll, double value, long user_data) {
	detail = scroll;
	return false;
}

@Override
long gtk_value_changed (long range) {
	Event event = new Event ();
	dragSent = detail == GTK.GTK_SCROLL_JUMP;
	switch (detail) {
		case GTK.GTK_SCROLL_NONE:			event.detail = SWT.NONE; break;
		case GTK.GTK_SCROLL_JUMP:			event.detail = SWT.DRAG; break;
		case GTK.GTK_SCROLL_START:			event.detail = SWT.HOME; break;
		case GTK.GTK_SCROLL_END:				event.detail = SWT.END; break;
		case GTK.GTK_SCROLL_PAGE_DOWN:
		case GTK.GTK_SCROLL_PAGE_RIGHT:
		case GTK.GTK_SCROLL_PAGE_FORWARD:	event.detail = SWT.PAGE_DOWN; break;
		case GTK.GTK_SCROLL_PAGE_UP:
		case GTK.GTK_SCROLL_PAGE_LEFT:
		case GTK.GTK_SCROLL_PAGE_BACKWARD:	event.detail = SWT.PAGE_UP; break;
		case GTK.GTK_SCROLL_STEP_DOWN:
		case GTK.GTK_SCROLL_STEP_RIGHT:
		case GTK.GTK_SCROLL_STEP_FORWARD:	event.detail = SWT.ARROW_DOWN; break;
		case GTK.GTK_SCROLL_STEP_UP:
		case GTK.GTK_SCROLL_STEP_LEFT:
		case GTK.GTK_SCROLL_STEP_BACKWARD:	event.detail = SWT.ARROW_UP; break;
	}
	if (!dragSent) detail = GTK.GTK_SCROLL_NONE;
	sendSelectionEvent (SWT.Selection, event, false);
	parent.updateScrollBarValue (this);
	GTK.gtk_widget_queue_draw(parent.handle);
	return 0;
}

@Override
long gtk_event_after (long widget, long gdkEvent) {
	int eventType = GDK.gdk_event_get_event_type(gdkEvent);
	eventType = Control.fixGdkEventTypeValues(eventType);
	switch (eventType) {
		case GDK.GDK_BUTTON_RELEASE: {
			int [] eventButton = new int [1];
			if (GTK.GTK4) {
				eventButton[0] = GDK.gdk_button_event_get_button(gdkEvent);
			} else {
				GDK.gdk_event_get_button(gdkEvent, eventButton);
			}

			if (eventButton[0] == 1 && detail == GTK.GTK_SCROLL_JUMP) {
				if (!dragSent) {
					Event event = new Event ();
					event.detail = SWT.DRAG;
					sendSelectionEvent (SWT.Selection, event, false);
				}
				sendSelectionEvent (SWT.Selection);
			}
			detail = GTK.GTK_SCROLL_NONE;
			dragSent = false;
			break;
		}
	}
	return super.gtk_event_after (widget, gdkEvent);
}

@Override
void hookEvents () {
	super.hookEvents ();
	OS.g_signal_connect_closure (adjustmentHandle, OS.value_changed, display.getClosure (VALUE_CHANGED), false);
	if (GTK.GTK4) {
		//TODO: GTK4 change-value moved to gtk_scroll_child in Composite, event-after

		long clickGesture = GTK4.gtk_gesture_click_new();
		GTK4.gtk_widget_add_controller(handle, clickGesture);
		OS.g_signal_connect(clickGesture, OS.pressed, display.gesturePressReleaseProc, GESTURE_PRESSED);
	} else {
		OS.g_signal_connect_closure (handle, OS.change_value, display.getClosure (CHANGE_VALUE), false);
		OS.g_signal_connect_closure_by_id (handle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.getClosure (BUTTON_PRESS_EVENT), false);
		OS.g_signal_connect_closure_by_id (handle, display.signalIds [EVENT_AFTER], 0, display.getClosure (EVENT_AFTER), false);
	}
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

@Override
void register () {
	super.register ();
	if (adjustmentHandle != 0) display.addWidget (adjustmentHandle, this);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	parent = null;
}

@Override
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
	if (handle != 0) GTK.gtk_widget_set_sensitive (handle, enabled);
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
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	GTK.gtk_adjustment_set_step_increment (adjustmentHandle, value);
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
	gtk_adjustment_get (adjustmentHandle, adjustment);
	int minimum = (int) adjustment.lower;
	if (value <= minimum) return;
	adjustment.upper = value;
	adjustment.page_size = Math.min ((int)adjustment.page_size, value - minimum);
	adjustment.value = Math.min ((int)adjustment.value, (int)(value - adjustment.page_size));
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	GTK.gtk_adjustment_configure(adjustmentHandle, adjustment.value, adjustment.lower, adjustment.upper,
		adjustment.step_increment, adjustment.page_increment, adjustment.page_size);
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
	gtk_adjustment_get (adjustmentHandle, adjustment);
	int maximum = (int) adjustment.upper;
	if (value >= maximum) return;
	adjustment.lower = value;
	adjustment.page_size = Math.min ((int)adjustment.page_size, maximum - value);
	adjustment.value = Math.max ((int)adjustment.value, value);
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	GTK.gtk_adjustment_configure(adjustmentHandle, adjustment.value, adjustment.lower, adjustment.upper,
		adjustment.step_increment, adjustment.page_increment, adjustment.page_size);
	OS.g_signal_handlers_unblock_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	if ((parent.style & SWT.MIRRORED) != 0 || !create) {
		if ((parent.state & CANVAS) != 0) {
			if ((style & SWT.HORIZONTAL) != 0) {
				GTK.gtk_range_set_inverted (handle, (parent.style & SWT.RIGHT_TO_LEFT) != 0);
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
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	GTK.gtk_adjustment_set_page_increment (adjustmentHandle, value);
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
public void setSelection (int selection) {
	checkWidget ();
	selection = Math.min (selection, getMaximum() - getThumb());
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	GTK.gtk_adjustment_set_value (adjustmentHandle, selection);
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
	gtk_adjustment_get (adjustmentHandle, adjustment);
	value = (int) Math.min (value, (int)(adjustment.upper - adjustment.lower));
	adjustment.page_size = (double) value;
	adjustment.value = Math.min ((int)adjustment.value, (int)(adjustment.upper - value));
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	GTK.gtk_adjustment_configure(adjustmentHandle, adjustment.value, adjustment.lower, adjustment.upper,
		adjustment.step_increment, adjustment.page_increment, adjustment.page_size);
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
	adjustment.lower = minimum;
	adjustment.upper = maximum;
	adjustment.step_increment = increment;
	adjustment.page_increment = pageIncrement;
	adjustment.page_size = thumb;
	adjustment.value = Math.min (Math.max (selection, minimum), maximum - thumb);
	OS.g_signal_handlers_block_matched (adjustmentHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	GTK.gtk_adjustment_configure(adjustmentHandle, adjustment.value, adjustment.lower, adjustment.upper,
		adjustment.step_increment, adjustment.page_increment, adjustment.page_size);
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

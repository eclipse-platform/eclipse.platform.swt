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


import org.eclipse.swt.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are selectable user interface
 * objects that represent a range of positive, numeric values. 
 * <p>
 * At any given moment, a given slider will have a 
 * single 'selection' that is considered to be its
 * value, which is constrained to be within the range of
 * values the slider represents (that is, between its
 * <em>minimum</em> and <em>maximum</em> values).
 * </p><p>
 * Typically, sliders will be made up of five areas:
 * <ol>
 * <li>an arrow button for decrementing the value</li>
 * <li>a page decrement area for decrementing the value by a larger amount</li>
 * <li>a <em>thumb</em> for modifying the value by mouse dragging</li>
 * <li>a page increment area for incrementing the value by a larger amount</li>
 * <li>an arrow button for incrementing the value</li>
 * </ol>
 * Based on their style, sliders are either <code>HORIZONTAL</code>
 * (which have a left facing button for decrementing the value and a
 * right facing button for incrementing it) or <code>VERTICAL</code>
 * (which have an upward facing button for decrementing the value
 * and a downward facing buttons for incrementing it).
 * </p><p>
 * On some platforms, the size of the slider's thumb can be
 * varied relative to the magnitude of the range of values it
 * represents (that is, relative to the difference between its
 * maximum and minimum values). Typically, this is used to
 * indicate some proportional value such as the ratio of the
 * visible area of a document to the total amount of space that
 * it would take to display it. SWT supports setting the thumb
 * size even if the underlying platform does not, but in this
 * case the appearance of the slider will not change.
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
 * @see ScrollBar
 * @see <a href="http://www.eclipse.org/swt/snippets/#slider">Slider snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Slider extends Control {
	int detail;
	boolean dragSent;
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
public Slider (Composite parent, int style) {
	super (parent, checkStyle (style));
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

void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	int /*long*/ hAdjustment = OS.gtk_adjustment_new (0, 0, 100, 1, 10, 10);
	if (hAdjustment == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.HORIZONTAL) != 0) {
		handle = OS.gtk_hscrollbar_new (hAdjustment);
	} else {
		handle = OS.gtk_vscrollbar_new (hAdjustment);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	/*
	* Bug in GTK. In GTK 2.10, the buttons on either end of
	* a horizontal slider are created taller then the slider bar
	* when the GTK_CAN_FOCUS flag is set. The fix is not to set
	* the flag for horizontal bars in all versions of 2.10. Note
	* that a bug has been logged with GTK about this issue.
	* (http://bugzilla.gnome.org/show_bug.cgi?id=475909) 
	*/
	if (OS.GTK_VERSION < OS.VERSION (2, 10, 0) || (style & SWT.VERTICAL) != 0) {
		OS.GTK_WIDGET_SET_FLAGS (handle, OS.GTK_CAN_FOCUS);
	}
	OS.gtk_container_add (fixedHandle, handle);
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
	return 0;
}

int /*long*/ gtk_event_after (int /*long*/ widget, int /*long*/ gdkEvent) {
	GdkEvent gtkEvent = new GdkEvent ();
	OS.memmove (gtkEvent, gdkEvent, GdkEvent.sizeof);
	switch (gtkEvent.type) {
		case OS.GDK_BUTTON_RELEASE: {
			GdkEventButton gdkEventButton = new GdkEventButton ();
			OS.memmove (gdkEventButton, gdkEvent, GdkEventButton.sizeof);
			if (gdkEventButton.button == 1 && detail == SWT.DRAG) {
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
	OS.g_signal_connect_closure (handle, OS.value_changed, display.closures [VALUE_CHANGED], false);
}

void register () {
	super.register ();
	int /*long*/ hAdjustment = OS.gtk_range_get_adjustment (handle);
	display.addWidget (hAdjustment, this);
}

void deregister () {
	super.deregister ();
	int /*long*/ hAdjustment = OS.gtk_range_get_adjustment (handle);
	display.removeWidget (hAdjustment);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	Point size = computeNativeSize(handle, wHint, hHint, changed);
	if ((style & SWT.HORIZONTAL) != 0) {
		if (wHint == SWT.DEFAULT) size.x = 2 * size.x;
	} else {
		if (hHint == SWT.DEFAULT) size.y = 2 * size.y;
	}
	return size;
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
	int /*long*/ hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
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
	int /*long*/ hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
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
	int /*long*/ hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
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
	int /*long*/ hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	return (int) adjustment.page_increment;
}

/**
 * Returns the 'selection', which is the receiver's value.
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
	int /*long*/ hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	return (int) adjustment.value;
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
 */
public int getThumb () {
	checkWidget ();
	int /*long*/ hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	return (int) adjustment.page_size;
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
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_range_set_increments (handle, value, getPageIncrement ());
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

/**
 * Sets the maximum. If this value is negative or less than or
 * equal to the minimum, the value is ignored. If necessary, first
 * the thumb and then the selection are adjusted to fit within the
 * new range.
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
	int /*long*/ hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	int minimum = (int) adjustment.lower;
	if (value <= minimum) return;
	adjustment.upper = value;
	adjustment.page_size = Math.min ((int)adjustment.page_size, value - minimum);
	adjustment.value = Math.min ((int)adjustment.value, (int)(value - adjustment.page_size));
	OS.memmove (hAdjustment, adjustment);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_adjustment_changed (hAdjustment);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
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
	int /*long*/ hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	int maximum = (int) adjustment.upper;
	if (value >= maximum) return;
	adjustment.lower = value;
	adjustment.page_size = Math.min ((int)adjustment.page_size, maximum - value);
	adjustment.value = Math.max ((int)adjustment.value, value);
	OS.memmove (hAdjustment, adjustment);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_adjustment_changed (hAdjustment);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

void setOrientation (boolean create) {
	super.setOrientation (create);
	if ((style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		if ((style & SWT.HORIZONTAL) != 0) {
			OS.gtk_range_set_inverted (handle, (style & SWT.RIGHT_TO_LEFT) != 0);
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
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_range_set_increments (handle, getIncrement (), value);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

/**
 * Sets the 'selection', which is the receiver's
 * value, to the argument which must be greater than or equal
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
	checkWidget ();
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_range_set_value (handle, value);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
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
	int /*long*/ hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	value = (int) Math.min (value, (int)(adjustment.upper - adjustment.lower));
	adjustment.page_size = (double) value;
	adjustment.value = Math.min ((int)adjustment.value, (int)(adjustment.upper - value));
	OS.memmove (hAdjustment, adjustment);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_adjustment_changed (hAdjustment);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
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
	int /*long*/ hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	adjustment.value = Math.min (Math.max (selection, minimum), maximum - thumb);
	adjustment.lower = (double) minimum;
	adjustment.upper = (double) maximum;
	adjustment.page_size = (double) thumb;
	adjustment.step_increment = (double) increment;
	adjustment.page_increment = (double) pageIncrement;
	OS.memmove (hAdjustment, adjustment);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_adjustment_changed (hAdjustment);
	OS.gtk_adjustment_value_changed (hAdjustment);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

}

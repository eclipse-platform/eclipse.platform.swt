package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of the receiver represent a selectable user
 * interface object that present a range of continuous
 * numeric values.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd> HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */

public class Scale extends Control {

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
	int hAdjustment = OS.gtk_adjustment_new (0, 0, 100, 1, 10, 0);
	if (hAdjustment == 0) error (SWT.ERROR_NO_HANDLES);	
	if ((style & SWT.HORIZONTAL) != 0) {
		handle = OS.gtk_hscale_new (hAdjustment);
	} else {
		handle = OS.gtk_vscale_new (hAdjustment);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

void setHandleStyle() {
	OS.gtk_scale_set_digits (handle, 0); 
	OS.gtk_scale_set_draw_value (handle, false);
}

void showHandle() {
	OS.gtk_widget_show (handle);
	OS.gtk_widget_realize (handle);
}

void hookEvents () {
	super.hookEvents ();
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment, GtkAdjustment.sizeof);
	signal_connect (hAdjustment, "value_changed", SWT.Selection, 2);
}

void register () {
	super.register ();
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	WidgetTable.put (hAdjustment, this);
}

void deregister () {
	super.deregister ();
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	WidgetTable.remove (hAdjustment);
	/*
	* This code is intentionally commented.
	*/
//	OS.gtk_object_unref (hAdjustment);
//	OS.gtk_object_destroy (hAdjustment);
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
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment, GtkAdjustment.sizeof);
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
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment, GtkAdjustment.sizeof);
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
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment, GtkAdjustment.sizeof);
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
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment, GtkAdjustment.sizeof);
	return (int) adjustment.page_increment;
}

/**
 * Returns the single <em>selection</em> that is the receiver's position.
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
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment, GtkAdjustment.sizeof);
	return (int) adjustment.value;
}

int processSelection (int int0, int int1, int int2) {
	postEvent (SWT.Selection);
	return 0;
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
	checkWidget ();
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
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment, GtkAdjustment.sizeof);
	adjustment.step_increment = (float) value;
	OS.memmove (hAdjustment, adjustment, GtkAdjustment.sizeof);
	OS.gtk_signal_handler_block_by_data (hAdjustment, SWT.Selection);
	OS.gtk_adjustment_changed (hAdjustment);
	OS.gtk_signal_handler_unblock_by_data (hAdjustment, SWT.Selection);
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
	checkWidget ();
	if (value < 0) return;
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment, GtkAdjustment.sizeof);
	adjustment.upper = (float) value;
	OS.memmove (hAdjustment, adjustment, GtkAdjustment.sizeof);
	OS.gtk_signal_handler_block_by_data (hAdjustment, SWT.Selection);
	OS.gtk_adjustment_changed (hAdjustment);
	OS.gtk_signal_handler_unblock_by_data (hAdjustment, SWT.Selection);
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
	checkWidget ();
	if (value < 0) return;
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment, GtkAdjustment.sizeof);
	adjustment.lower = (float) value;
	OS.memmove (hAdjustment, adjustment, GtkAdjustment.sizeof);
	OS.gtk_signal_handler_block_by_data (hAdjustment, SWT.Selection);
	OS.gtk_adjustment_changed (hAdjustment);
	OS.gtk_signal_handler_unblock_by_data (hAdjustment, SWT.Selection);
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
	checkWidget ();
	if (value < 1) return;
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment, GtkAdjustment.sizeof);
	adjustment.page_increment = (float) value;
	OS.memmove (hAdjustment, adjustment, GtkAdjustment.sizeof);
	OS.gtk_signal_handler_block_by_data (hAdjustment, SWT.Selection);
	OS.gtk_adjustment_changed (hAdjustment);
	OS.gtk_signal_handler_unblock_by_data (hAdjustment, SWT.Selection);
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
	if (value < 0) return;
	int hAdjustment = OS.gtk_range_get_adjustment (handle);
	OS.gtk_signal_handler_block_by_data (hAdjustment, SWT.Selection);
	OS.gtk_adjustment_set_value (hAdjustment, value);
	OS.gtk_signal_handler_unblock_by_data (hAdjustment, SWT.Selection);
}

}

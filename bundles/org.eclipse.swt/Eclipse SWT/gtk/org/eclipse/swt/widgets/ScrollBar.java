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
 * (which have left and right facing buttons for incrementing and
 * decrementing the value) or <code>VERTICAL</code> (which have
 * up and down facing buttons for incrementing and decrementing
 * the value).
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

public Display getDisplay () {
	Scrollable parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getEnabled () {
	checkWidget ();
	//NOT DONE
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
	GtkAdjustment adjustment = new GtkAdjustment (handle);
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
	GtkAdjustment adjustment = new GtkAdjustment (handle);
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
	GtkAdjustment adjustment = new GtkAdjustment (handle);
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
	GtkAdjustment adjustment = new GtkAdjustment (handle);
	return (int) adjustment.page_increment;
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
	checkWidget ();
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
	checkWidget ();
	GtkAdjustment adjustment = new GtkAdjustment (handle);
	return (int) adjustment.value;
}

/**
 * For horizontal scroll bars, returns the height of the 
 * instance, and for vertical scroll bars, returns the width
 * of the instance.
 *
 * @return the scroll bar size
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSize () {
	checkWidget ();
	//FIXME - NOT_IMPLEMENTED
	return new Point (10, 10);
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
	checkWidget ();
	GtkAdjustment adjustment = new GtkAdjustment (handle);
	return (int) adjustment.page_size;
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
	//NOT DONE
	return true;
}

void hookEvents () {
	super.hookEvents ();
	signal_connect (handle, "value_changed",  SWT.Selection, 2);
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 * <p>
 * Note: Because of the strong connection between a scroll bar
 * and the widget which contains it (its parent), a scroll bar
 * will not indicate that it is enabled if its parent is not.
 * </p>
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isEnabled () {
	checkWidget ();
	return getEnabled () && getParent ().getEnabled ();
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
public boolean isVisible () {
	checkWidget ();
	return getVisible () && getParent ().isVisible ();
}

int processSelection (int int0, int int1, int int2) {
	postEvent (SWT.Selection);
	return 0;
}

void releaseChild () {
	super.releaseChild ();
	if (parent.horizontalBar == this) parent.horizontalBar = null;
	if (parent.verticalBar == this) parent.verticalBar = null;
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
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
	//NOT DONE
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
	GtkAdjustment adjustment = new GtkAdjustment (handle);
	adjustment.step_increment = (float) value;
	OS.memmove (handle, adjustment);
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_adjustment_changed (handle);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	GtkAdjustment adjustment = new GtkAdjustment (handle);
	adjustment.upper = (float) value;
	OS.memmove (handle, adjustment);
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_adjustment_changed (handle);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	GtkAdjustment adjustment = new GtkAdjustment (handle);
	adjustment.lower = (float) value;
	OS.memmove (handle, adjustment);
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_adjustment_changed (handle);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	GtkAdjustment adjustment = new GtkAdjustment (handle);
	adjustment.page_increment = (float) value;
	OS.memmove (handle, adjustment);
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_adjustment_changed (handle);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	checkWidget ();
	if (value < 0) return;
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_adjustment_set_value (handle, value);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	checkWidget ();
	if (value < 1) return;
	GtkAdjustment adjustment = new GtkAdjustment (handle);
	adjustment.page_size = (float) value;
	OS.memmove (handle, adjustment);
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_adjustment_changed (handle);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	checkWidget ();
	if (selection < 0) return;
	if (minimum < 0) return;
	if (maximum < 0) return;
	if (thumb < 1) return;
	if (maximum - minimum - thumb < 0) return;
	if (increment < 1) return;
	if (pageIncrement < 1) return;
	GtkAdjustment adjustment = new GtkAdjustment (handle);
	adjustment.lower = minimum;
	adjustment.upper = maximum;
	adjustment.step_increment = increment;
	adjustment.page_increment = pageIncrement;
	adjustment.page_size = thumb;
	adjustment.value = selection;
	OS.memmove (handle, adjustment);
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_adjustment_changed (handle);
	OS.gtk_adjustment_value_changed (handle);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	//NOT DONE
}

}

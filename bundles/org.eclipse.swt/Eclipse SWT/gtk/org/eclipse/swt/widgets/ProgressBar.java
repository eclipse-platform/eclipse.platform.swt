package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of the receiver represent is an unselectable
 * user interface object that is used to display progress,
 * typically in the form of a bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SMOOTH, HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class ProgressBar extends Control {
	int min = 0, max = 100, value = 0;

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
 * @see SWT#SMOOTH
 * @see SWT#HORIZONTAL
 * @see SWT#VERTICAL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ProgressBar (Composite parent, int style) {
	super (parent, checkStyle(style));
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

void configureBar (int selection, int minimum, int maximum) {
	OS.gtk_progress_configure (handle, selection, minimum, maximum);
	/*
	* Feature in GTK.  The progress bar does
	* not redraw right away when a value is
	* changed.  This is not strictly incorrect
	* but unexpected.  The fix is to force all
	* outstanding redraws to be delivered.
	*/
	OS.gdk_flush ();
	int window = paintWindow ();
	OS.gdk_window_process_updates (window, false);
}

void createHandle (int index) {
	state |= HANDLE;
	handle = OS.gtk_progress_bar_new ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_progress_configure (handle, value, min, max);
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_add (parentHandle, handle);
	OS.gtk_widget_show (handle);
	int orientation = (style & SWT.VERTICAL) != 0 ? OS.GTK_PROGRESS_TOP_TO_BOTTOM : OS.GTK_PROGRESS_LEFT_TO_RIGHT;
	OS.gtk_progress_bar_set_orientation (handle, orientation);		
	int style = (this.style & SWT.SMOOTH) == 0 ? OS.GTK_PROGRESS_DISCRETE : OS.GTK_PROGRESS_CONTINUOUS;
	OS.gtk_progress_bar_set_bar_style (handle, style);
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
	return max;
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
	return min;
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
	return value;
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
public void setMaximum (int maximum) {
	checkWidget ();
	if (maximum < 0) return;
	max = maximum;
	if (value > maximum) value = maximum;
	configureBar (value, min, max);
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
public void setMinimum (int minimum) {
	checkWidget ();
	if (minimum < 0) return;
	if (value < minimum) value = minimum;
	min = minimum;
	configureBar (value, min, max);
}

/**
 * Sets the single <em>selection</em> that is the receiver's
 * position to the argument which must be greater than or equal
 * to zero.
 *
 * @param value the new selection (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int x) {
	checkWidget ();
	if (x < 0) return;
	value = x;
	configureBar (value, min, max);
}


}

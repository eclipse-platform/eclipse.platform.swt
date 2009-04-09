/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

import org.eclipse.swt.*;

/**
 * Instances of the receiver represent an unselectable
 * user interface object that is used to display progress,
 * typically in the form of a bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SMOOTH, HORIZONTAL, VERTICAL, INDETERMINATE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#progressbar">ProgressBar snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class ProgressBar extends Control {
	
	NSBezierPath visiblePath;

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
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int size = OS.NSProgressIndicatorPreferredThickness;
	int width = 0, height = 0;
	if ((style & SWT.HORIZONTAL) != 0) {
		height = size;
		width = height * 10;
	} else {
		width = size;
		height = width * 10;
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createHandle () {
	NSProgressIndicator widget = (NSProgressIndicator)new SWTProgressIndicator().alloc();
	widget.init();
	widget.setUsesThreadedAnimation(false);
	widget.setIndeterminate((style & SWT.INDETERMINATE) != 0);
	view = widget;
}

NSFont defaultNSFont () {
	return display.progressIndicatorFont;
}

void _drawThemeProgressArea (int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	/*
	* Bug in Cocoa.  When the threaded animation is turned off by calling
	* setUsesThreadedAnimation(), _drawThemeProgressArea() attempts to
	* access a deallocated NSBitmapGraphicsContext when drawing a zero sized
	* progress bar.  The fix is to avoid calling super when progress is
	* zero sized.
	*/
	NSRect frame = view.frame();
	System.out.println(frame);
	if (frame.width == 0 || frame.height == 0) return;
	
	/*
	* Bug in Cocoa. When the progress bar is animating it calls
	* _drawThemeProgressArea() directly without taking into account
	* obscured areas. The fix is to clip the drawing to the visible
	* region of the progress bar before calling super.
	*/	
	if (visiblePath == null) {
		int /*long*/ visibleRegion = getVisibleRegion();
		visiblePath = getPath(visibleRegion);
		OS.DisposeRgn(visibleRegion);
	}
	NSGraphicsContext context = NSGraphicsContext.currentContext();
	context.saveGraphicsState();
	visiblePath.setClip();
	super._drawThemeProgressArea (id, sel, arg0);
	context.restoreGraphicsState();	
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
	return (int)((NSProgressIndicator)view).maxValue();
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
	return (int)((NSProgressIndicator)view).minValue();
}

/**
 * Returns the single 'selection' that is the receiver's position.
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
    return (int)((NSProgressIndicator)view).doubleValue();
}

/**
 * Returns the state of the receiver. The value will be one of:
 * <ul>
 * 	<li>{@link SWT#NORMAL}</li>
 * 	<li>{@link SWT#ERROR}</li>
 * 	<li>{@link SWT#PAUSED}</li>
 * </ul>
 *
 * @return the state 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public int getState () {
	checkWidget ();
	return SWT.NORMAL;
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
	checkWidget();
	int minimum = (int)((NSProgressIndicator)view).minValue();
	if (value <= minimum) return;
	((NSProgressIndicator)view).setMaxValue(value);
	int selection = (int)((NSProgressIndicator)view).doubleValue();
	int newSelection = Math.min (selection, value);
	if (selection != newSelection) {
		((NSProgressIndicator)view).setDoubleValue(newSelection);
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
	checkWidget();
	int maximum =  (int)((NSProgressIndicator)view).maxValue();
	if (!(0 <= value && value < maximum)) return;
	((NSProgressIndicator)view).setMinValue(value);
	int selection = (int)((NSProgressIndicator)view).doubleValue();
	int newSelection = Math.max (selection, value);
	if (selection != newSelection) {
		((NSProgressIndicator)view).setDoubleValue(newSelection);
	}
}

/**
 * Sets the single 'selection' that is the receiver's
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
public void setSelection (int value) {
	checkWidget();
	((NSProgressIndicator)view).setDoubleValue(value);
}

/**
 * Sets the state of the receiver. The state must be one of these values:
 * <ul>
 * 	<li>{@link SWT#NORMAL}</li>
 * 	<li>{@link SWT#ERROR}</li>
 * 	<li>{@link SWT#PAUSED}</li>
 * </ul>
 *
 * @param state the new state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public void setState (int state) {
	checkWidget ();
	//NOT IMPLEMENTED
}

void releaseWidget () {
	super.releaseWidget();
	if (visiblePath != null) visiblePath.release();
	visiblePath = null;
}

void resetVisibleRegion () {
	super.resetVisibleRegion ();
	if (visiblePath != null) visiblePath.release();
	visiblePath = null;
}

void viewDidMoveToWindow(int /*long*/ id, int /*long*/ sel) {
	/*
	 * Bug in Cocoa. An indeterminate progress indicator doesn't start animating until it is in
	 * a visible window.  Workaround is to catch when the bar has been added to a window and start
	 * the animation there.
	 */
	if (view.window() != null) {
		if ((style & SWT.INDETERMINATE) != 0) {
			((NSProgressIndicator)view).startAnimation(null);
		}
	}
}
}

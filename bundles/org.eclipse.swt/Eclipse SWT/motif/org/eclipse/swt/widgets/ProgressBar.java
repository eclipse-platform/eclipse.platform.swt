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

 
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of the receiver represent is an unselectable
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
 */
public class ProgressBar extends Control {
	int timerId;
	static final int DELAY = 100;
	int foreground;

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
	/*
	 * Feature in Motif. If you set the progress bar's value to 0,
	 * the thumb does not disappear. In order to make this happen,
	 * we hide the widget when the value is set to zero by changing
	 * its colors to render it invisible, which means that it
	 * would not visible unless a border is present. The fix is to 
	 * always ensure that there is a border, which will be drawn 
	 * even when the value is 0.
	 */
	super (parent, checkStyle (style | SWT.BORDER));
}
static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	int hScroll = display.scrolledMarginX;
	int vScroll = display.scrolledMarginY;
	if ((style & SWT.HORIZONTAL) != 0) {
		width += hScroll * 10;
		height += vScroll;
	} else {
		width += hScroll;
		height += vScroll * 10;
	}
	if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
	if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
	return new Point (width, height);
}
void createHandle (int index) {
	state |= HANDLE;
	int background = defaultBackground ();
	int parentHandle = parent.handle;
	int [] argList = {
		OS.XmNshowArrows, 0,
		OS.XmNsliderSize, 1,
		OS.XmNtraversalOn, 0,
		OS.XmNtroughColor, background,
		OS.XmNtopShadowColor, background,
		OS.XmNbottomShadowColor, background,
		OS.XmNforeground, background,
		OS.XmNshadowThickness, 1,
		OS.XmNborderWidth, (style & SWT.BORDER) != 0 ? 1 : 0,
		OS.XmNorientation, ((style & SWT.H_SCROLL) != 0) ? OS.XmHORIZONTAL : OS.XmVERTICAL,
		OS.XmNprocessingDirection, ((style & SWT.H_SCROLL) != 0) ? OS.XmMAX_ON_RIGHT : OS.XmMAX_ON_TOP,
		OS.XmNancestorSensitive, 1,
		OS.XmNsliderVisual, OS.XmFOREGROUND_COLOR,
	};
	handle = OS.XmCreateScrollBar (parentHandle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.INDETERMINATE) != 0) createTimer ();
}
void createWidget (int index) {
	super.createWidget (index);
	foreground = defaultForeground ();
}
void createTimer () {
	int xDisplay = display.xDisplay;
	int windowTimerProc = display.windowTimerProc;
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	timerId = OS.XtAppAddTimeOut (xtContext, DELAY, windowTimerProc, handle);
}
void destroyTimer () {
	if (timerId != 0) OS.XtRemoveTimeOut (timerId);
	timerId = 0;
}
void disableButtonPress () {
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return;
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int event_mask = OS.XtBuildEventMask (handle);
	XSetWindowAttributes attributes = new XSetWindowAttributes ();
	attributes.event_mask = event_mask & ~OS.ButtonPressMask;
	OS.XChangeWindowAttributes (xDisplay, xWindow, OS.CWEventMask, attributes);
}
int getForegroundPixel () {
	return foreground == -1 ? super.getForegroundPixel () : foreground;
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
	int [] argList = {OS.XmNmaximum, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
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
	int [] argList = {OS.XmNminimum, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
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
	checkWidget();
	int [] argList = {
		OS.XmNminimum, 0,
		OS.XmNsliderSize, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int minimum = argList [1], sliderSize = argList [3];
	return minimum + (foreground != -1 ? 0 : sliderSize);
}
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	if (enabled) disableButtonPress ();
}
void realizeChildren () {
	super.realizeChildren ();
	disableButtonPress ();
}
void releaseWidget () {
	super.releaseWidget ();
	destroyTimer ();
}
void setBackgroundPixel (int pixel) {
	checkWidget();
	super.setBackgroundPixel (pixel);
	if (foreground != -1) super.setForegroundPixel (pixel);
}
void setForegroundPixel (int pixel) {
	checkWidget();
	if (foreground == -1) {
		super.setForegroundPixel (pixel);
	} else {
		foreground = pixel;
	}
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
	if (value < 0) return;
	int [] argList = {OS.XmNmaximum, value, OS.XmNvalue, 0};
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList, argList.length / 2);
	display.setWarnings (warnings);
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
	int [] argList = {
		OS.XmNminimum, 0,
		OS.XmNmaximum, 0,
		OS.XmNsliderSize, 0,
		OS.XmNvalue, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int minimum = argList [1];
	int maximum = argList [3];
	int sliderSize = argList [5];

	if (value >= maximum) return;
	int selection = sliderSize + minimum;
	if (value > selection) selection = value;
	argList [1] = value;
	argList [7] = value;
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList, argList.length / 2);
	display.setWarnings (warnings);
	setThumb(selection - value);
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
public void setSelection (int value) {
	checkWidget();
	int [] argList = {
		OS.XmNminimum, 0,
		OS.XmNmaximum, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int minimum = argList [1];
	int maximum = argList [3];

	int selection = value;
	if (selection < minimum) selection = minimum;
	if (selection > maximum) selection = maximum;
	setThumb(selection - minimum);
}
void setThumb (int sliderSize) {
	int [] argList1 = new int [] {
		OS.XmNtroughColor, 0,
		OS.XmNminimum, 0,
	};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	int troughColor = argList1 [1];
	if (sliderSize == 0) {
		if (foreground == -1) {
			foreground = getForegroundPixel ();
			super.setForegroundPixel (troughColor);
		}
	} else {
		if (foreground != -1) {
			super.setForegroundPixel (foreground);
			foreground = -1;
		}
	}
	int [] argList2 = new int [] {
		OS.XmNsliderSize, sliderSize == 0 ? 1 : sliderSize,
		OS.XmNvalue, argList1 [3]
	};
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList2, argList2.length / 2);
	display.setWarnings (warnings);
}
int timerProc (int id) {
	int minimum = getMinimum ();
	int range = getMaximum () - minimum + 1;
	int value = getSelection () - minimum + 1;
	setSelection (minimum + (value % range));	
	createTimer ();
	return 0;
}
}

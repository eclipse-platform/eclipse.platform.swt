package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
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
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */

public /*final*/ class ProgressBar extends Control {
/**
* Creates a new instance of the widget.
*/
public ProgressBar (Composite parent, int style) {
	/**
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
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	Display display = getDisplay ();
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
		OS.XmNshadowThickness, 1,
		OS.XmNborderWidth, (style & SWT.BORDER) != 0 ? 1 : 0,
		OS.XmNorientation, ((style & SWT.H_SCROLL) != 0) ? OS.XmHORIZONTAL : OS.XmVERTICAL,
		OS.XmNprocessingDirection, ((style & SWT.H_SCROLL) != 0) ? OS.XmMAX_ON_RIGHT : OS.XmMAX_ON_TOP,
		OS.XmNancestorSensitive, 1,
	};
	handle = OS.XmCreateScrollBar (parentHandle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
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
void disableTraversal () {
	int [] argList = {OS.XmNtraversalOn, 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {
		OS.XmNminimum, 0,
		OS.XmNsliderSize, 0,
		OS.XmNbackground, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int minimum = argList [1], sliderSize = argList [3], background = argList [5];
	if (sliderSize == 1 && background == defaultBackground()) sliderSize = 0;
	return minimum + sliderSize;
}
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	/*
	* ProgressBars never participate in focus traversal when
	* either enabled or disabled.  Also, when enabled
	*/
	if (enabled) {
		disableTraversal ();
		disableButtonPress ();
	}
}
void realizeChildren () {
	super.realizeChildren ();
	disableButtonPress ();
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (value < 0) return;
	int [] argList = {OS.XmNmaximum, value, OS.XmNvalue, 0};
	Display display = getDisplay ();
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	Display display = getDisplay ();
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (value < 0) return;
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
	Display display = getDisplay ();
	int backgroundPixel = defaultBackground ();
	int [] argList1 = new int [] {
		OS.XmNbackground, 0,
		OS.XmNminimum, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	if (sliderSize == 0) {
		if (argList1 [1] != backgroundPixel) {
			OS.XmChangeColor (handle, backgroundPixel);
		}
	} else {
		if (argList1 [1] != display.listForeground) {
			OS.XmChangeColor (handle, display.listForeground);
		}
	}
	int [] argList2 = new int [] {
		OS.XmNsliderSize, (sliderSize == 0) ? 1 : sliderSize,
		OS.XmNtroughColor, backgroundPixel,
		OS.XmNtopShadowColor, backgroundPixel,
		OS.XmNbottomShadowColor, backgroundPixel,
		OS.XmNvalue, argList1[3]
	};
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList2, argList2.length / 2);
	display.setWarnings (warnings);
}
}

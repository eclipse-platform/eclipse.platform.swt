package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/* Imports */
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
*	A progress bar is an unselectable user interface object
* that is used to display progress in the form of a bar graph.
*
* <b>Styles</b><br>
* <dd>HORIZONTAL, VERTICAL<br>
*/

/* Class Definition */
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
/**
* Computes the preferred size.
*/
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
	int backgroundPixel = defaultBackground ();
	int [] argList1 = {
		OS.XmNshowArrows, 0,
		OS.XmNsliderSize, 1,
		OS.XmNtraversalOn, 0,
		OS.XmNtroughColor, backgroundPixel,
		OS.XmNtopShadowColor, backgroundPixel,
		OS.XmNbottomShadowColor, backgroundPixel,
		OS.XmNshadowThickness, 1,
		OS.XmNborderWidth, (style & SWT.BORDER) != 0 ? 1 : 0,
		OS.XmNorientation, ((style & SWT.H_SCROLL) != 0) ? OS.XmHORIZONTAL : OS.XmVERTICAL,
		OS.XmNprocessingDirection, ((style & SWT.H_SCROLL) != 0) ? OS.XmMAX_ON_RIGHT : OS.XmMAX_ON_TOP,
	};
	handle = OS.XmCreateScrollBar (parent.handle, null, argList1, argList1.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}
/**
* Gets the maximum.
* <p>
* @return maximum
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getMaximum () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNmaximum, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
/**
* Gets the minimum.
* <p>
* @return minimum
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getMinimum () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNminimum, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
/**
* Gets the selection.
* <p>
* @return the selection
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
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
	int minimum = argList [1];
	int sliderSize = argList [3];
	int backGround = argList [5];
	if (sliderSize == 1 && backGround == defaultBackground()) sliderSize = 0;
	return minimum + sliderSize;
}
void realizeChildren () {
	super.realizeChildren ();
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return;
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int event_mask = OS.XtBuildEventMask (handle);
	XSetWindowAttributes attributes = new XSetWindowAttributes ();
	attributes.event_mask = event_mask & ~OS.ButtonPressMask;
	OS.XChangeWindowAttributes (xDisplay, xWindow, OS.CWEventMask, attributes);
}
/**
* Sets the maximum.
* <p>
* @param maximum
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
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
* Sets the minimum
* <p>
* @param minimum
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
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
* Sets the selection.
* <p>
* @param value new selection
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
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
	int [] argList1 = new int [] {OS.XmNbackground, 0};
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
	};
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList2, argList2.length / 2);
	display.setWarnings (warnings);
}
}

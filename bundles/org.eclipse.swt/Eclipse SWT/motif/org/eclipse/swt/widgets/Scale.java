package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
/* Imports */
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
*	A scale is a selectable user interface object
* that represents a range of continuous numeric values.
*
* <p>
* <b>Styles</b><br>
* <dd>HORIZONTAL,VERTICAL<br>
* <br>
* <b>Events</b><br>
* <dd>Selection<br
*/

/* Class Definition */
public /*final*/ class Scale extends Control {
/**
* Creates a new instance of the widget.
*/
public Scale (Composite parent, int style) {
	super (parent, checkStyle (style));
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
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
	int [] argList = {
		OS.XmNtitleString, 0,
		OS.XmNborderWidth, (style & SWT.BORDER) != 0 ? 1 : 0,
		OS.XmNorientation, ((style & SWT.H_SCROLL) != 0) ? OS.XmHORIZONTAL : OS.XmVERTICAL,
		OS.XmNprocessingDirection, ((style & SWT.H_SCROLL) != 0) ? OS.XmMAX_ON_RIGHT : OS.XmMAX_ON_BOTTOM,
	};
	handle = OS.XmCreateScale (parent.handle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}
/**
* Gets the increment.
* <p>
* @return the increment
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getIncrement () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return 1;
}
/**
* Gets the maximum.
* <p>
* @return the maximum
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
* @return the minimum
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
* Gets the page increment.
* <p>
* @return the page increment
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getPageIncrement () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNscaleMultiple, 0};
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
	int [] argList = {OS.XmNvalue, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNvalueChangedCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNdragCallback, windowProc, SWT.Selection);
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
}
/**
* Sets the increment value.
* <p>
* @param increment the new increment value
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setIncrement (int increment) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
}
/**
* Sets the maximum.
* <p>
* @param maximum the new maximum
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
	int [] argList = {OS.XmNmaximum, value};
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList, argList.length / 2);
	display.setWarnings (warnings);
}
/**
* Sets the minimum.
* <p>
* @param minimum the new minimum
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
	int [] argList = {OS.XmNminimum, value};
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList, argList.length / 2);
	display.setWarnings (warnings);
}
/**
* Sets the page increment.
* <p>
* @param pageIncrement the new page increment
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setPageIncrement (int pageIncrement) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (pageIncrement < 1) return;
	int [] argList = {OS.XmNscaleMultiple, pageIncrement};
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList, argList.length / 2);
	display.setWarnings (warnings);
}
/**
* Sets the selection.
* <p>
* @param selection the new selection
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setSelection (int selection) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (selection < 0) return;
	int [] argList = {OS.XmNvalue, selection};
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList, argList.length / 2);
	display.setWarnings (warnings);
}
}

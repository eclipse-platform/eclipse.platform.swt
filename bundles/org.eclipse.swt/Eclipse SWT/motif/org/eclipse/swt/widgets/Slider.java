package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/* Imports */
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
*	A slider is a selectable user interface object
* that represents a continuous range of numeric values.
*
* <b>Styles</b><br>
* <dd>HORIZONTAL, VERTICAL<br>
* <br>
* <b>Events</b><br>
* <dd>Selection<br>
*/

/* Class Definition */
public /*final*/ class Slider extends Control {
/**
* Creates a new instance of the widget.
*/
public Slider (Composite parent, int style) {
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
		OS.XmNborderWidth, (style & SWT.BORDER) != 0 ? 1 : 0,
		OS.XmNorientation, ((style & SWT.H_SCROLL) != 0) ? OS.XmHORIZONTAL : OS.XmVERTICAL,
	};
	handle = OS.XmCreateScrollBar (parent.handle, null, argList, argList.length / 2);
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
	int [] argList = {OS.XmNincrement, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
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
	int [] argList = {OS.XmNpageIncrement, 0};
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
/**
* Gets the thumb.
* <p>
* @return the thumb value
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getThumb () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNsliderSize, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNvalueChangedCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNdragCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNtoBottomCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNincrementCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNdecrementCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNpageIncrementCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNpageDecrementCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNtoTopCallback, windowProc, SWT.Selection);
}
int processSelection (int callData) {
	XmAnyCallbackStruct struct = new XmAnyCallbackStruct ();
	OS.memmove (struct, callData, XmAnyCallbackStruct.sizeof);
	Event event = new Event ();
	switch (struct.reason) {
		case OS.XmCR_DRAG:		event.detail = SWT.DRAG;  break;
		/*
		* Do not set the detail field to SWT.DRAG 
		* to indicate that the dragging has ended.
		*/
//		case OS.XmCR_VALUE_CHANGED:	break; 
		case OS.XmCR_TO_TOP:		event.detail = SWT.HOME;  break;
		case OS.XmCR_TO_BOTTOM:		event.detail = SWT.END;  break;
		case OS.XmCR_DECREMENT:		event.detail = SWT.ARROW_UP;  break;
		case OS.XmCR_INCREMENT:		event.detail = SWT.ARROW_DOWN;  break;
		case OS.XmCR_PAGE_DECREMENT: 	event.detail = SWT.PAGE_UP;  break;
		case OS.XmCR_PAGE_INCREMENT: 	event.detail = SWT.PAGE_DOWN;  break;
	}
	sendEvent (SWT.Selection, event);
	return 0;
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
* Sets the increment.
* <p>
* @param value the new increment
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setIncrement (int value) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (value < 1) return;
	int [] argList = {OS.XmNincrement, value};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
* Sets the maximum.
* <p>
* @param value the new maximum
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
* @param value the new minimum
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
	};
	/*
	* Feature in Motif.  For some reason, when minimium
	* is set to be greater than or equal to maximum, Motif
	* does not set the minimum.  Instead, the value is
	* changed and the minimum stays the same.  This behavior
	* differs from setting the maximum where the slider size
	* is always decreased to make room for the new maximum.
	* The fix is to decrease the slider to make room for
	* the new minimum.
	*/
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [3] - value - argList [5] < 0) {
		argList [5] = argList [3] - value;
	}
	argList [1] = value;
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList, argList.length / 2);
	display.setWarnings (warnings);
}
/**
* Sets the page increment.
* <p>
* @param value the new page increment
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setPageIncrement (int value) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (value < 1) return;
	int [] argList = {OS.XmNpageIncrement, value};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
* Sets the selection.
* <p>
* @param value the new selection
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
	int [] argList = {OS.XmNvalue, value};
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList, argList.length / 2);
	display.setWarnings (warnings);
}
/**
* Sets the thumb.
* <p>
* @param value the new thumb
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setThumb (int value) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (value < 1) return;
	int [] argList = {OS.XmNsliderSize, value};
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList, argList.length / 2);
	display.setWarnings (warnings);
}
public void setValues (int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (selection < 0) return;
	if (minimum < 0) return;
	if (maximum < 0) return;
	if (thumb < 1) return;
	if (maximum - minimum - thumb < 0) return;
	if (increment < 1) return;
	if (pageIncrement < 1) return;
	int [] argList = {
		OS.XmNvalue, selection,
		OS.XmNminimum, minimum,
		OS.XmNmaximum, maximum,
		OS.XmNsliderSize, thumb,
		OS.XmNincrement, increment,
		OS.XmNpageIncrement, pageIncrement,
	};
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XtSetValues (handle, argList, argList.length / 2);
	display.setWarnings (warnings);
}
}

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
import org.eclipse.swt.events.*;

/**
*	A scroll bar is a selectable user interface object
* that represents a range of continuous numeric values.
* Typically these values are used to represent lines of
* text in a list or text widget.
*
* <p>
* <b>Styles</b><br>
* <dd>HORIZONTAL, VERTICAL<br>
* <br>
* <b>Events</b><br>
* <dd>Selection<br>
*/

/* Class Definition */
public /*final*/ class ScrollBar extends Widget {
	Scrollable parent;
ScrollBar () {
	/* Do Nothing */
}
ScrollBar (Scrollable parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (0);
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
void createHandle (int index) {
	state |= HANDLE;
	int [] argList = {
		OS.XmNborderWidth, (style & SWT.BORDER) != 0 ? 1 : 0,
		OS.XmNorientation, ((style & SWT.H_SCROLL) != 0) ? OS.XmHORIZONTAL : OS.XmVERTICAL,
	};
	handle = OS.XmCreateScrollBar (parent.scrolledHandle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}
/**
* Gets the Display.
*/
public Display getDisplay () {
	Scrollable parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}
/**
* Gets the enabled state.
* <p>
* @return the enabled flag
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean getEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNsensitive, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
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
* Gets the parent.
* <p>
* @return the parent
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Scrollable getParent () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
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
* Gets the scroll bar size.
* <p>
* @return the scroll bar size
*
* @exception SWTError <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
*	</ul>
*/
public Point getSize () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int borders = argList [5] * 2;
	return new Point (argList [1] + borders, argList [3] + borders);
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
/**
* Gets the visibility state.
* <p>
*	If the parent is not visible or some other condition
* makes the widget not visible, the widget can still be
* considered visible even though it may not actually be
* showing.
*
* @return visible the visibility state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean getVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return OS.XtIsManaged (handle);
}
void hookEvents () {
	int windowProc = parent.getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNvalueChangedCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNdragCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNtoBottomCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNincrementCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNdecrementCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNpageIncrementCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNpageDecrementCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNtoTopCallback, windowProc, SWT.Selection);
}
public boolean isEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getEnabled () && parent.isEnabled ();
}
/**
* Gets the visibility status.
* <p>
*	This method returns the visibility status of the
* widget in the widget hierarchy.  If the parent is not
* visible or some other condition makes the widget not
* visible, this method will return false.
*
* @return the actual visibility state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean isVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getVisible () && parent.isVisible ();
}
void manageChildren () {
	OS.XtManageChild (handle);
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
* Sets the enabled state.
* <p>
* @param enabled the new value of the enabled flag
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setEnabled (boolean enabled) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNsensitive, enabled ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
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
/**
* Sets the thumb.
* <p>
* @param value the new thumb value
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
/**
* Sets the visibility state.
* <p>
*	If the parent is not visible or some other condition
* makes the widget not visible, the widget can still be
* considered visible even though it may not actually be
* showing.
*
* @param visible the new visibility state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setVisible (boolean visible) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	/*
	* Feature in Motif.  Hiding or showing a scroll bar
	* can cause the widget to automatically resize in
	* the OS.  This behavior is unwanted.  The fix is
	* to force the widget to resize to original size.
	*/
	int scrolledHandle = parent.scrolledHandle;
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);

	/* Hide or show the scroll bar */
	if (visible) {
		OS.XtManageChild (handle);
	} else {
		OS.XtUnmanageChild (handle);
	}

	/* Restore the size */
	OS.XtSetValues (scrolledHandle, argList, argList.length / 2);
}
}

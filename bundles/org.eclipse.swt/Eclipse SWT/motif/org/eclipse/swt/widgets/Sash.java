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
*	A sash is a selectable user interface object
* that allows the user to drag a rubber banded
* outline of the sash within the parent window.
*
* <p>
* <b>Styles</b><br>
* <dd>HORIZONTAL,VERTICAL<br>
*/

/* Class Definition */
public /*final*/ class Sash extends Control {
	boolean dragging;
	int startX, startY, lastX, lastY;
	int cursor;
/**
* Creates a new instance of the widget.
*/
public Sash (Composite parent, int style) {
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
	if ((style & SWT.HORIZONTAL) != 0) {
		width += DEFAULT_WIDTH;  height += 3;
	} else {
		width += 3; height += DEFAULT_HEIGHT;
	}
	if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
	if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
	return new Point (width, height);
}
void createHandle (int index) {
	state |= HANDLE;
	int border = (style & SWT.BORDER) != 0 ? 1 : 0;
	int [] argList = {
		OS.XmNborderWidth, border,
		OS.XmNmarginWidth, 0,
		OS.XmNmarginHeight, 0,
		OS.XmNresizePolicy, OS.XmRESIZE_NONE,
	};
	handle = OS.XmCreateDrawingArea (parent.handle, null, argList, argList.length / 2);
}
void drawBand (int x, int y, int width, int height) {
	int display = OS.XtDisplay (parent.handle);
	if (display == 0) return;
	int window = OS.XtWindow (parent.handle);
	if (window == 0) return;
	int [] argList = {OS.XmNforeground, 0, OS.XmNbackground, 0};
	OS.XtGetValues (parent.handle, argList, argList.length / 2);
	int color = argList [1] ^ argList [3];
	byte [] bits = {-86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0};
	int stipplePixmap = OS.XCreateBitmapFromData (display, window, bits, 8, 8);
	int gc = OS.XCreateGC (display, window, 0, null);
	OS.XSetForeground (display, gc, color);
	OS.XSetStipple (display, gc, stipplePixmap);
	OS.XSetSubwindowMode (display, gc, OS.IncludeInferiors);
	OS.XSetFillStyle (display, gc, OS.FillStippled);
	OS.XSetFunction (display, gc, OS.GXxor);
	OS.XFillRectangle (display, window, gc, x, y, width, height);
	OS.XFreePixmap (display, stipplePixmap);
	OS.XFreeGC (display, gc);
}
int processMouseDown (int callData) {
	super.processMouseDown (callData);
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, callData, XButtonEvent.sizeof);
	if (xEvent.button != 1) return 0;
	startX = xEvent.x;  startY = xEvent.y;
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int border = argList [9], width = argList [5] + (border * 2), height = argList [7] + (border * 2);
	lastX = ((short) argList [1]) - border;  lastY = ((short) argList [3]) - border;
	Event event = new Event ();
	event.detail = SWT.DRAG;
	event.time = xEvent.time;
	event.x = lastX;  event.y = lastY;
	event.width = width;  event.height = height;
	sendEvent (SWT.Selection, event);
	if (event.doit) {
		dragging = true;
		OS.XmUpdateDisplay (handle);
		drawBand (lastX = event.x, lastY = event.y, width, height);
	}
	return 0;
}
int processMouseMove (int callData) {
	super.processMouseMove (callData);
	XMotionEvent xEvent = new XMotionEvent ();
	OS.memmove (xEvent, callData, XMotionEvent.sizeof);
	if (!dragging || (xEvent.state & OS.Button1Mask) == 0) return 0;
	int [] argList1 = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	int border = argList1 [9], x = ((short) argList1 [1]) - border, y = ((short) argList1 [3]) - border;
	int width = argList1 [5] + (border * 2), height = argList1 [7] + (border * 2);
	int [] argList2 = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (parent.handle, argList2, argList2.length / 2);
	int parentBorder = argList2 [5];
	int parentWidth = argList2 [1] + (parentBorder * 2);
	int parentHeight = argList2 [3] + (parentBorder * 2);
	int newX = lastX, newY = lastY;
	if ((style & SWT.VERTICAL) != 0) {
		newX = Math.min (Math.max (0, xEvent.x + x - startX - parentBorder), parentWidth - width);
	} else {
		newY = Math.min (Math.max (0, xEvent.y + y - startY - parentBorder), parentHeight - height);
	}
	if (newX == lastX && newY == lastY) return 0;
	drawBand (lastX, lastY, width, height);
	Event event = new Event ();
	event.detail = SWT.DRAG;
	event.time = xEvent.time;
	event.x = newX;  event.y = newY;
	event.width = width;  event.height = height;
	sendEvent (SWT.Selection, event);
	if (event.doit) {
		lastX = event.x;  lastY = event.y;
		OS.XmUpdateDisplay (handle);
		drawBand (lastX, lastY, width, height);
	}
	return 0;
}
int processMouseUp (int callData) {
	super.processMouseUp (callData);
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, callData, XButtonEvent.sizeof);
	if (xEvent.button != 1) return 0;
	if (!dragging) return 0;
	dragging = false;
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int border = argList [5];
	int width = argList [1] + (border * 2), height = argList [3] + (border * 2);
	Event event = new Event ();
	event.time = xEvent.time;
	event.x = lastX;  event.y = lastY;
	event.width = width;  event.height = height;
	drawBand (lastX, lastY, width, height);
	sendEvent (SWT.Selection, event);
	return 0;
}
void realizeChildren () {
	super.realizeChildren ();
	int window = OS.XtWindow (handle);
	if (window == 0) return;
	int display = OS.XtDisplay (handle);
	if (display == 0) return;
	if ((style & SWT.HORIZONTAL) != 0) {
		cursor = OS.XCreateFontCursor (display, OS.XC_sb_v_double_arrow);
	} else {
		cursor = OS.XCreateFontCursor (display, OS.XC_sb_h_double_arrow);
	}
	OS.XDefineCursor (display, window, cursor);
}
void releaseWidget () {
	super.releaseWidget ();
	if (cursor != 0) {
		int display = OS.XtDisplay (handle);
		if (display != 0) OS.XFreeCursor (display, cursor);
	}
	cursor = 0;
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
}

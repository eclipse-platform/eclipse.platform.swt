package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
/* Imports */
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

/**
*/

/* Class Definition */
public /*final*/ class Tracker extends Widget {
	Composite parent;
	Display display;
	boolean tracking, stippled;
	Rectangle [] rectangles = new Rectangle [0];
/**
* Creates a new instance of the widget.
*/
public Tracker (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	display = parent.getDisplay ();
}
/**
* Creates a new instance of the widget.
*/
public Tracker (Display display, int style) {
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.style = style;
	this.display = display;
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
public void addControlListener(ControlListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Move,typedListener);
}
public void close () {
	tracking = false;
}
void drawRectangles () {
	if (parent != null) {
		if (parent.isDisposed ()) return;
		parent.getShell ().update ();
	} else {
		display.update ();
	}
	int xDisplay = display.xDisplay;
	int color = OS.XWhitePixel (xDisplay, 0);
	int xWindow = OS.XDefaultRootWindow (xDisplay);
	if (parent != null) {
		xWindow = OS.XtWindow (parent.handle);
		if (xWindow == 0) return;
		int [] argList = {OS.XmNforeground, 0, OS.XmNbackground, 0};
		OS.XtGetValues (parent.handle, argList, argList.length / 2);
		color = argList [1] ^ argList [3];
	}
	int gc = OS.XCreateGC (xDisplay, xWindow, 0, null);
	OS.XSetForeground (xDisplay, gc, color);
	OS.XSetSubwindowMode (xDisplay, gc, OS.IncludeInferiors);
	OS.XSetFunction (xDisplay, gc, OS.GXxor);
	int stipplePixmap = 0;
	if (stippled) {
		byte [] bits = {-86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0};
		stipplePixmap = OS.XCreateBitmapFromData (xDisplay, xWindow, bits, 8, 8);
		OS.XSetStipple (xDisplay, gc, stipplePixmap);
		OS.XSetFillStyle (xDisplay, gc, OS.FillStippled);
		OS.XSetLineAttributes (xDisplay, gc, 3, OS.LineSolid, OS.CapButt, OS.JoinMiter);
	}
	for (int i=0; i<rectangles.length; i++) {
		Rectangle rect = rectangles [i];
		OS.XDrawRectangle (xDisplay, xWindow, gc, rect.x, rect.y, rect.width, rect.height);
	}
	if (stippled) {
		OS.XFreePixmap (xDisplay, stipplePixmap);
	}
	OS.XFreeGC (xDisplay, gc);
}
public Display getDisplay () {
	return display;
}
public Rectangle [] getRectangles () {
	return rectangles;
}
public boolean getStippled () {
	return stippled;
}
public boolean open () {
	int xDisplay = display.xDisplay;
	int color = OS.XWhitePixel (xDisplay, 0);
	int xWindow = OS.XDefaultRootWindow (xDisplay);
	if (parent != null) {
		xWindow = OS.XtWindow (parent.handle);
		if (xWindow == 0) return false;
	}
	boolean cancelled = false;
	tracking = true;
	drawRectangles ();
	Event event = new Event ();
	XAnyEvent xEvent = new XAnyEvent ();
	int [] unused = new int [1];
	int [] newX = new int [1], newY = new int [1], oldX = new int [1], oldY = new int [1];
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	OS.XQueryPointer (xDisplay, xWindow, unused, unused, oldX, oldY, unused, unused, unused);
	while (tracking) {
		if (parent != null && parent.isDisposed ()) break;
		OS.XtAppNextEvent (xtContext,  xEvent);
		switch (xEvent.type) {
			case OS.ButtonRelease:
			case OS.MotionNotify:
				OS.XQueryPointer (xDisplay, xWindow, unused, unused, newX, newY, unused, unused, unused);
				if (oldX [0] != newX [0] || oldY [0] != newY [0]) {
					drawRectangles ();
					for (int i=0; i<rectangles.length; i++) {
						rectangles [i].x += newX [0] - oldX [0];
						rectangles [i].y += newY [0] - oldY [0];
					}
					sendEvent (SWT.Move);
					drawRectangles ();
					oldX [0] = newX [0];  oldY [0] = newY [0];
				}
				tracking = xEvent.type != OS.ButtonRelease;
				break;
			case OS.KeyPress:
				XKeyEvent keyEvent = new XKeyEvent ();
				OS.memmove (keyEvent, xEvent, XKeyEvent.sizeof);
				if (keyEvent.keycode != 0) {
					int [] keysym = new int [1];
					OS.XLookupString (keyEvent, null, 0, keysym, unused);
					keysym [0] &= 0xFFFF;
					tracking = keysym [0] != OS.XK_Escape && keysym [0] != OS.XK_Cancel;
					cancelled = !tracking;
				}
				break;
		}
	}
	drawRectangles ();
	tracking = false;
	return !cancelled;
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
public void removeControlListener (ControlListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
}
public void setRectangles (Rectangle [] rectangles) {
	this.rectangles = rectangles;
}
public void setStippled (boolean stippled) {
	this.stippled = stippled;
}
}

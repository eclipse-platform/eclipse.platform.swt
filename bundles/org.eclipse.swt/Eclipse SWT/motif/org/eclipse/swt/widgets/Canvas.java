package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/**
*	The canvas class implements a drawing area for
* arbitrary graphics.
*
* Styles
*
*	NO_BACKGROUND,
*	NO_FOCUS,
*	MERGE_EXPOSES,
*	RESIZE_REDRAW
*
* Events
*
**/

/* Imports */
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/* Class Definition */
public class Canvas extends Composite {
	Caret caret;
	
Canvas () {
	/* Do nothing */
}
/**
* Create a Canvas.
*
* PARAMTERS
*
*	parent		a composite widget (cannot be null)
*	style		the bitwise OR'ing of widget styles
*
* REMARKS
*
*	This method creates a child widget using style bits
* to select a particular look or set of properties. 
*
**/
public Canvas (Composite parent, int style) {
	super (parent, style);
}
void createWidget (int index) {
	super.createWidget (index);
	fontList = defaultFont ();
}
/**
* Get the current caret.
*
* REMARKS
*
*	This method gets the current caret for the window.
* The current caret is automatically hidden and shown
* by the window when:
*
*	- during expose and resize
*	- when focus is gained or lost
*	- when an the window is scrolled
*
**/
public Caret getCaret () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return caret;
}

int processFocusIn () {
	int result = super.processFocusIn ();
	if (caret != null) caret.setFocus ();
	return result;
}
int processFocusOut () {
	int result = super.processFocusOut ();
	if (caret != null) caret.killFocus ();
	return result;
}

int processPaint (int callData) {
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	int result = super.processPaint (callData);
	if (isFocus) caret.setFocus ();
	return result;
}

void redrawWidget (int x, int y, int width, int height, boolean all) {
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	super.redrawWidget (x, y, width, height, all);
	if (isFocus) caret.setFocus ();
}

void releaseWidget () {
	if (caret != null) {
		caret.releaseWidget ();
		caret.releaseHandle ();
	}
	caret = null;
	super.releaseWidget();
}

public void scroll (int destX, int destY, int x, int y, int width, int height, boolean all) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - x, deltaY = destY - y;
	if (deltaX == 0 && deltaY == 0) return;
	if (!isVisible ()) return;
		
	/* Hide the caret */
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	
	/* Flush outstanding exposes */
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return;
	XAnyEvent xEvent = new XAnyEvent ();
	OS.XSync (xDisplay, false);  OS.XSync (xDisplay, false);
	while (OS.XCheckWindowEvent (xDisplay, xWindow, OS.ExposureMask, xEvent)) {
		OS.XtDispatchEvent (xEvent);
	}

	/* Scroll the window */
	int xGC = OS.XCreateGC (xDisplay, xWindow, 0, null);
	OS.XCopyArea (xDisplay, xWindow, xWindow, xGC, x, y, width, height, destX, destY);
	OS.XFreeGC (xDisplay, xGC);
	boolean disjoint = (destX + width < x) || (x + width < destX) || (destY + height < y) || (y + height < destY);
	if (disjoint) {
		OS.XClearArea (xDisplay, xWindow, x, y, width, height, true);
	} else {
		if (deltaX != 0) {
			int newX = destX - deltaX;
			if (deltaX < 0) newX = destX + width;
			OS.XClearArea (xDisplay, xWindow, newX, y, Math.abs (deltaX), height, true);
		}
		if (deltaY != 0) {
			int newY = destY - deltaY;
			if (deltaY < 0) newY = destY + height;
			OS.XClearArea (xDisplay, xWindow, x, newY, width, Math.abs (deltaY), true);
		}
	}
	
	/* Show the caret */
	if (isFocus) caret.setFocus ();
}
public void setBounds (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	super.setBounds (x, y, width, height);
	if (isFocus) caret.setFocus ();
}
/**
* Set the current caret.
*
* PARAMTERS
*
* 	caret - the new caret or null
*
* REMARKS
*
*	This method sets the current caret for the window.
* The current caret is automatically hidden and shown
* by the window when:
*
*	- during expose and resize
*	- when focus is gained or lost
*	- when an the window is scrolled
*
**/
public void setCaret (Caret caret) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Caret newCaret = caret;
	Caret oldCaret = this.caret;
	this.caret = newCaret;
	if (hasFocus ()) {
		if (oldCaret != null) oldCaret.killFocus ();
		if (newCaret != null) newCaret.setFocus ();
	}
}

public void setLocation (int x, int y) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	super.setLocation (x, y);
	if (isFocus) caret.setFocus ();
}
public void setSize (int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	super.setSize (width, height);
	if (isFocus) caret.setFocus ();
}
}

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
*	The caret class implements an i-beam that is
* typically used as the insertion point for text.
*/

/* Class Definition */
public /*final*/ class Caret extends Widget {
	Canvas parent;
	int x, y, width, height;
	boolean moved, resized;
	boolean isVisible, isShowing;
	int blinkRate = 500;
/**
* Creates a new instance of the widget.
*/
public Caret (Canvas parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (0);
}
boolean blinkCaret () {
	if (!isVisible) return true;
	if (!isShowing) return showCaret ();
	if (blinkRate == 0) return true;
	return hideCaret ();
}
void createWidget (int index) {
	super.createWidget (index);
	isVisible = true;
	if (parent.getCaret () == null) {
		parent.setCaret (this);
	}
}
boolean drawCaret () {
	if (parent == null) return false;
	if (parent.isDisposed ()) return false;
	int handle = parent.handle;
	int window = OS.XtWindow (handle);
	if (window == 0) return false;
	int xDisplay = OS.XtDisplay (handle);
	int gc = OS.XCreateGC (xDisplay, window, 0, null);
	int [] argList = {OS.XmNforeground, 0, OS.XmNbackground, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int foreground = argList [1];
	int background = argList [3];
	int color = foreground ^ background;
	OS.XSetFunction (xDisplay, gc, OS.GXxor);
	OS.XSetForeground (xDisplay, gc, color);
	int nWidth = width;
	if (nWidth <= 0) nWidth = 2;
	OS.XFillRectangle (xDisplay, window, gc, x, y, nWidth, height);
	OS.XFreeGC (xDisplay, gc);
	return true;
}
/**
* Gets the widget bounds.
* <p>
* @return a rectangle that is the widget bounds.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Rectangle getBounds () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return new Rectangle (x, y, width, height);
}
/**
* Gets the Display.
*/
public Display getDisplay () {
	Composite parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}
/**
* Gets the widget font.
* <p>
* @return the widget font
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Font getFont () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getFont ();
}
/**
* Gets the widget location.
* <p>
* @return the widget location
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Point getLocation () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return new Point (x, y);
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
public Canvas getParent () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}
/**
* Gets the widget size.
* <p>
* @return the widget size
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Point getSize () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return new Point (width, height);
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
	return isVisible;
}
boolean hideCaret () {
	Display display = getDisplay ();
	if (display.currentCaret != this) return false;
	if (!isShowing) return true;
	isShowing = false;
	return drawCaret ();
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
	return isVisible && parent.isVisible () && parent.hasFocus ();
}
boolean isFocusCaret () {
	Display display = getDisplay ();
	return this == display.currentCaret;
}
void killFocus () {
	Display display = getDisplay ();
	if (display.currentCaret != this) return;
	if (isVisible) hideCaret ();
	display.setCurrentCaret (null);
}
void releaseChild () {
	super.releaseChild ();
	if (this == parent.getCaret ()) parent.setCaret (null);
}
void releaseWidget () {
	super.releaseWidget ();
	Display display = getDisplay ();
	if (display.currentCaret == this) {
		if (isVisible) hideCaret ();
		display.setCurrentCaret (null);
	}
	parent = null;
}
/**
* Sets the widget bounds.
* <p>
* @param x the new x coordinate
* @param y the new y coordinate
* @param width the new widget width
* @param height the new widget height
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setBounds (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	boolean samePosition, sameExtent, showing;
	samePosition = (this.x == x) && (this.y == y);
	sameExtent = (this.width == width) && (this.height == height);
	if ((samePosition) && (sameExtent)) return;
	if (isShowing) hideCaret ();
	this.x = x; this.y = y;
	this.width = width; this.height = height;
	if (sameExtent) {
			moved = true;
			if (isVisible ()) {
				moved = false;
				//IsDBLocale ifTrue: [
				//widget == nil ifFalse: [widget updateCaret]].
			}
	} else {
			resized = true;
			if (isVisible ()) {
				moved = false;
				//IsDBLocale ifTrue: [
				//widget == nil ifFalse: [widget updateCaret]].
				resized = false;
			}
	}
	if (isShowing) showCaret ();
}
/**
* Set the widget bounds.
*
* @param rect the new bounding rectangle
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when rect is null
*/
public void setBounds (Rectangle rect) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (rect.x, rect.y, rect.width, rect.height);
}
void setFocus () {
	Display display = getDisplay ();
	if (display.currentCaret == this) return;
	display.setCurrentCaret (this);
	if (isVisible) showCaret ();
}
/**
* Sets the widget font.
* <p>
* When new font is null, the font reverts
* to the default system font for the widget.
*
* @param font the new font (or null)
* 
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setFont (Font font) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
}
/**
* Sets the widget location.
* <p>
* @param x the new x position.
* @param y the new y position.
* 
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setLocation (int x, int y) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setBounds (x, y, width, height);
}
/**
* Sets the widget location.
* <p>
* @param location the new location
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when location is null
*/
public void setLocation (Point location) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}
/**
* Sets the widget size.
* <p>
* @param x the new width
* @param y the new height
* 
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setSize (int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setBounds (x, y, width, height);
}
/**
* Sets the widget size.
* <p>
* @param size the widget size
* 
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when size is null
*/
public void setSize (Point size) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
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
	if (visible == isVisible) return;
	if (isVisible = visible) {
		showCaret ();
	} else {
		hideCaret ();
	}
}
boolean showCaret () {
	if (getDisplay ().currentCaret != this) return false;
	if (isShowing) return true;
	isShowing = true;
	return drawCaret ();
}
}

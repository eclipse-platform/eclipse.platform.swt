/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CGPoint;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of the receiver represent a selectable user interface object
 * that allows the user to drag a rubber banded outline of the sash within
 * the parent control.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>HORIZONTAL, VERTICAL, SMOOTH</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class Sash extends Control {
	Cursor sizeCursor;
	int lastX, lastY;
	private final static int INCREMENT = 1;
	private final static int PAGE_INCREMENT = 9;

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
 * @see SWT#HORIZONTAL
 * @see SWT#VERTICAL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Sash (Composite parent, int style) {
	super (parent, checkStyle (style));
	int cursorStyle = (style & SWT.VERTICAL) != 0 ? SWT.CURSOR_SIZEWE : SWT.CURSOR_SIZENS;
	sizeCursor = new Cursor (display, cursorStyle);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the x, y, width, and height fields of the event object are valid.
 * If the receiver is being dragged, the event object detail field contains the value <code>SWT.DRAG</code>.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	/*
	* Macintosh only supports smooth dragging.
	*/
	style |= SWT.SMOOTH;
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

int callFocusEventHandler (int nextHandler, int theEvent) {
	return OS.noErr;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = 0, height = 0;
	if ((style & SWT.HORIZONTAL) != 0) {
		width += DEFAULT_WIDTH;  height += 5;
	} else {
		width += 5; height += DEFAULT_HEIGHT;
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createHandle () {
	state |= THEME_BACKGROUND;
	int features = OS.kControlSupportsFocus;
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	OS.CreateUserPaneControl (window, null, features, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
}

void drawBackground (int control, int context) {
	fillBackground (control, context, null);
}

int kEventControlClick (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlClick (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (!isEnabled ()) return OS.noErr;
	return result;
}

int kEventControlGetFocusPart (int nextHandler, int theEvent, int userData) {
	return OS.noErr;
}

int kEventControlSetCursor (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetCursor (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	display.setCursor (sizeCursor.handle);
	return OS.noErr;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMouseDown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;

	Shell shell = getShell ();
	shell.bringToTop (true);
	if (isDisposed ()) return OS.noErr;

	display.grabControl = null;
	display.runDeferredEvents ();
	
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	int startX = rect.left;
	int startY = rect.top;			
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	if (!OS.HIVIEW) {
		OS.GetControlBounds (parent.handle, rect);
		startX -= rect.left;
		startY -= rect.top;
	}
	Event event = new Event ();
	event.x = startX;
	event.y = startY;
	event.width = width;
	event.height = height;
	sendEvent (SWT.Selection, event);
	if (isDisposed ()) return result;
	if (!event.doit) return result;
	
	int offsetX, offsetY;
	int window = OS.GetControlOwner (handle);
	if (OS.HIVIEW) {
		CGPoint pt = new CGPoint ();
		OS.GetEventParameter (theEvent, OS.kEventParamWindowMouseLocation, OS.typeHIPoint, null, CGPoint.sizeof, null, pt);
		OS.HIViewConvertPoint (pt, 0, handle);
		offsetX = (int) pt.x;
		offsetY = (int) pt.y;		
	} else {
		int sizeof = org.eclipse.swt.internal.carbon.Point.sizeof;
		org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
		OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeQDPoint, null, sizeof, null, pt);
		OS.GetWindowBounds (window, (short) OS.kWindowContentRgn, rect);
		offsetX = pt.h - rect.left;
		offsetY = pt.v - rect.top;
		OS.GetControlBounds (handle, rect);
		offsetX -= rect.left;
		offsetY -= rect.top;
	}

	int port = OS.HIVIEW ? -1 : OS.GetWindowPort (window);
	int [] outModifiers = new int [1];
	short [] outResult = new short [1];
	CGPoint pt = new CGPoint ();
	org.eclipse.swt.internal.carbon.Point outPt = new org.eclipse.swt.internal.carbon.Point ();
	while (outResult [0] != OS.kMouseTrackingMouseUp) {
		OS.TrackMouseLocationWithOptions (port, 0, OS.kEventDurationForever, outPt, outModifiers, outResult);
		switch (outResult [0]) {
			case OS.kMouseTrackingMouseDown:
			case OS.kMouseTrackingMouseUp:
			case OS.kMouseTrackingMouseDragged: {
				int x, y;
				if (OS.HIVIEW) {
					OS.GetWindowBounds (window, (short) OS.kWindowStructureRgn, rect);
					pt.x = outPt.h - rect.left;
					pt.y = outPt.v - rect.top;
					OS.HIViewConvertPoint (pt, 0, parent.handle);
					x = (int) pt.x;
					y = (int) pt.y;
				} else {
					OS.GetControlBounds (parent.handle, rect);
					x = outPt.h - rect.left;
					y = outPt.v - rect.top;
				}
				int newX = startX, newY = startY;
				if ((style & SWT.VERTICAL) != 0) {
					int clientWidth = rect.right - rect.left;
					newX = Math.min (Math.max (0, x - offsetX), clientWidth - width);
				} else {
					int clientHeight = rect.bottom - rect.top;
					newY = Math.min (Math.max (0, y - offsetY), clientHeight - height);
				}
				event = new Event ();
				event.x = newX;
				event.y = newY;
				event.width = width;
				event.height = height;
				sendEvent (SWT.Selection, event);
				if (isDisposed ()) return result;
				if (event.doit) {
					setBounds (event.x, event.y, width, height);
					if (isDisposed ()) return result;
					if (!OS.HIVIEW) parent.update (true);
				}
				if (outResult [0] == OS.kMouseTrackingMouseUp)  {
					OS.GetControlBounds (handle, rect);
					short [] button = new short [1];
					OS.GetEventParameter (theEvent, OS.kEventParamMouseButton, OS.typeMouseButton, null, 2, null, button);
					int chord = OS.GetCurrentEventButtonState ();
					int modifiers = OS.GetCurrentEventKeyModifiers ();
					sendMouseEvent (SWT.MouseUp, button [0], display.clickCount, true, chord, (short) (x - rect.left), (short) (y - rect.top), modifiers);
				}
				break;
			}
			default:
				outResult [0] = OS.kMouseTrackingMouseUp;
				break;
		}
	}
	return OS.noErr;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetFocusPart (nextHandler, theEvent, userData);
	if (result == OS.noErr) {
		Point location = getLocation();
		lastX = location.x;
		lastY = location.y;
	}
	return result;
}

int kEventUnicodeKeyPressed (int nextHandler, int theEvent, int userData) {
	int result = super.kEventUnicodeKeyPressed (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] keyboardEvent = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendKeyboardEvent, OS.typeEventRef, null, keyboardEvent.length * 4, null, keyboardEvent);
	int [] keyCode = new int [1];
	OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	switch (keyCode [0]) {
		case 126: /* Up arrow */
		case 123: /* Left arrow */
		case 125: /* Down arrow */
		case 124: /* Right arrow */ {
			int xChange = 0, yChange = 0;
			int stepSize = PAGE_INCREMENT;
			int [] modifiers = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
			if ((modifiers [0] & OS.controlKey) != 0) stepSize = INCREMENT;
			if ((style & SWT.VERTICAL) != 0) {
				if (keyCode [0] == 126 || keyCode [0] == 125) break;
				xChange = keyCode [0] == 123 ? -stepSize : stepSize;
			} else {
				if (keyCode [0] == 123 || keyCode [0] == 124) break;
				yChange = keyCode [0] == 126 ? -stepSize : stepSize;
			}
			
			Rectangle bounds = getBounds ();
			int width = bounds.width, height = bounds.height;
			Rectangle parentBounds = parent.getBounds ();
			int parentWidth = parentBounds.width;
			int parentHeight = parentBounds.height;
			int newX = lastX, newY = lastY;
			if ((style & SWT.VERTICAL) != 0) {
				newX = Math.min (Math.max (0, lastX + xChange), parentWidth - width);
			} else {
				newY = Math.min (Math.max (0, lastY + yChange), parentHeight - height);
			}
			if (newX == lastX && newY == lastY) return result;
			Event event = new Event ();
			event.x = newX;
			event.y = newY;
			event.width = width;
			event.height = height;
			sendEvent (SWT.Selection, event);
			if (isDisposed ()) break;
			if (event.doit) {
				setBounds (event.x, event.y, width, height);
				if (isDisposed ()) break;
				lastX = event.x;
				lastY = event.y;
				if (isDisposed ()) return result;
				int cursorX = event.x, cursorY = event.y;
				if ((style & SWT.VERTICAL) != 0) {
					cursorY += height / 2;
				} else {
					cursorX += width / 2;
				}
				display.setCursorLocation (parent.toDisplay (cursorX, cursorY));
			}
			break;
		}
	}

	return result;
}

void releaseWidget () {
	super.releaseWidget ();
	if (sizeCursor != null) sizeCursor.dispose ();
	sizeCursor = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}

int traversalCode (int key, int theEvent) {
	return 0;
}

}

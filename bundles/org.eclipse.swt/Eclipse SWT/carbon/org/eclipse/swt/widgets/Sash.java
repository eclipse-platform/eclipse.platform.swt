package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class Sash extends Control {
	Cursor sizeCursor;

public Sash (Composite parent, int style) {
	super (parent, checkStyle (style));
	int cursorStyle = (style & SWT.VERTICAL) != 0 ? SWT.CURSOR_SIZEWE : SWT.CURSOR_SIZENS;
	sizeCursor = new Cursor (getDisplay (), cursorStyle);
}

public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
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
	int features = OS.kControlSupportsEmbedding | OS.kControlSupportsFocus | OS.kControlGetsFocusOnClick;
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	OS.CreateUserPaneControl (window, null, features, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
}

void drawWidget (int control) {
	drawBackground (handle, background);
}

int kEventControlClick (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlClick (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (!OS.IsControlEnabled (handle)) return OS.noErr;
	return result;
}

int kEventControlSetCursor (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetCursor (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	setCursor (sizeCursor.handle);
	return OS.noErr;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMouseDown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	int startX = rect.left;
	int startY = rect.top;			
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	OS.GetControlBounds (parent.handle, rect);
	Event event = new Event ();
	event.x = startX -= rect.left;
	event.y = startY -= rect.top;
	event.width = width;
	event.height = height;
	sendEvent (SWT.Selection, event);
	update ();
	if (!event.doit) return result;
	
	int sizeof = org.eclipse.swt.internal.carbon.Point.sizeof;
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeQDPoint, null, sizeof, null, pt);
	int window = OS.GetControlOwner (handle);
	OS.GetWindowBounds (window, (short) OS.kWindowContentRgn, rect);
	int offsetX = pt.h - rect.left;
	int offsetY = pt.v - rect.top;
	OS.GetControlBounds (handle, rect);
	offsetX -= rect.left;
	offsetY -= rect.top;
	
	int port = OS.GetWindowPort (window);
	int [] outModifiers = new int [1];
	short [] outResult = new short [1];
	org.eclipse.swt.internal.carbon.Point outPt = new org.eclipse.swt.internal.carbon.Point ();
	while (outResult [0] != OS.kMouseTrackingMouseUp) {
		OS.TrackMouseLocationWithOptions (port, 0, OS.kEventDurationForever, outPt, outModifiers, outResult);
		switch (outResult [0]) {
			case OS.kMouseTrackingMouseDown:
			case OS.kMouseTrackingMouseUp:
			case OS.kMouseTrackingMouseDragged: {
				OS.GetControlBounds (parent.handle, rect);
				int x = outPt.h - rect.left;
				int y = outPt.v - rect.top;				
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
				event.detail = 0; //outResult [0] == OS.kMouseTrackingMouseDragged ? SWT.DRAG : 0;
				sendEvent (SWT.Selection, event);
				if (event.doit) setBounds (newX, newY, width, height);
				update ();
				break;
			}
			default:
				outResult [0] = OS.kMouseTrackingMouseUp;
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

public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}
}

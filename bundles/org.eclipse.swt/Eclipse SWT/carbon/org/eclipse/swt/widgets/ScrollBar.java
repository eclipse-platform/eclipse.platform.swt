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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class ScrollBar extends Widget {
	int handle;
	Scrollable parent;
	boolean dragging;
	int increment = 1;
	int pageIncrement = 10;

ScrollBar (Scrollable parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget ();
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

int actionProc (int theControl, int partCode) {
	Event event = new Event ();
	int value = OS.GetControl32BitValue (handle);
    switch (partCode) {
	    case OS.kControlUpButtonPart:
			value -= increment;
	        event.detail = SWT.ARROW_UP;
	        break;
	    case OS.kControlPageUpPart:
			value -= pageIncrement;
	        event.detail = SWT.PAGE_UP;
	        break;
	    case OS.kControlPageDownPart:
			value += pageIncrement;
	        event.detail = SWT.PAGE_DOWN;
	        break;
	    case OS.kControlDownButtonPart:
			value += increment;
	        event.detail = SWT.ARROW_DOWN;
	        break;
	    case OS.kControlIndicatorPart:
	    	dragging = true;
			event.detail = SWT.DRAG;
	        break;
		default:
			return 0;
	}
	OS.SetControl32BitValue (handle, value);
	sendEvent (SWT.Selection, event);
	return 0;
}

void destroyWidget () {
	int theControl = handle;
	releaseHandle ();
	if (theControl != 0) {
		OS.DisposeControl (theControl);
	}
}

int controlProc (int nextHandler, int theEvent, int userData) {
	dragging = false;
	int status = OS.CallNextEventHandler (nextHandler, theEvent);
	if (dragging) {
		Event event = new Event ();
		sendEvent (SWT.Selection, event);
	}
	dragging = false;
	return status;
}

Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricScrollBarWidth, outMetric);
	int width = 0, height = 0;
	if ((style & SWT.HORIZONTAL) != 0) {
		height = outMetric [0];
		width = height * 10;
	} else {
		width = outMetric [0];
		height = width * 10;
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createWidget () {
	Display display = getDisplay ();
	int actionProc = display.actionProc;
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.scrolledHandle);
	OS.CreateScrollBarControl (window, null, 0, 0, 100, 10, true, actionProc, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
	OS.HIViewAddSubview (parent.scrolledHandle, handle);
	OS.HIViewSetZOrder (handle, OS.kHIViewZOrderAbove, 0);
}

public Display getDisplay () {
	Scrollable parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public boolean getEnabled () {
	checkWidget();
	return (state & DISABLED) == 0;
}

public int getIncrement () {
	checkWidget();
    return increment;
}

public int getMaximum () {
	checkWidget();
    return OS.GetControl32BitMaximum (handle);
}

public int getMinimum () {
	checkWidget();
    return OS.GetControl32BitMinimum (handle);
}

public int getPageIncrement () {
	checkWidget();
    return pageIncrement;
}

public int getSelection () {
	checkWidget();
    return OS.GetControl32BitValue (handle);
}

public Point getSize () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	return new Point (rect.bottom - rect.top, rect.right - rect.left);
}

public int getThumb () {
	checkWidget();
    return OS.GetControlViewSize (handle);
}

public boolean getVisible () {
	checkWidget();
	return (state & HIDDEN) == 0;
}

public boolean isEnabled () {
	checkWidget();
	return OS.IsControlEnabled (handle);
}

public boolean isVisible () {
	checkWidget();
	return OS.HIViewIsVisible (handle);
}

public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}

void releaseChild () {
	super.releaseChild ();
	//NOT DONE
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
}

public void setIncrement (int value) {
	checkWidget();
	if (value < 1) return;
	increment = value;
}

public void setEnabled (boolean enabled) {
	checkWidget();
	if (enabled) {
		if ((state & DISABLED) == 0) return;
		state &= ~DISABLED;
		OS.EnableControl (handle);
	} else {
		if ((state & DISABLED) != 0) return;
		state |= DISABLED;
		OS.DisableControl (handle);
	}
}

public void setMaximum (int value) {
	checkWidget();
	if (value < 0) return;
	int minimum = OS.GetControl32BitMinimum (handle);
	int viewSize = OS.GetControlViewSize (handle);
	if (value - minimum - viewSize < 0) return;
	OS.SetControl32BitMaximum (handle, value);
}

public void setMinimum (int value) {
	checkWidget();
	if (value < 0) return;
	int maximum = OS.GetControl32BitMinimum (handle);
	int viewSize = OS.GetControlViewSize (handle);
	if (maximum - value - viewSize < 0) return;
	OS.SetControl32BitMinimum (handle, value);
}

public void setPageIncrement (int value) {
	checkWidget();
	if (value < 1) return;
	pageIncrement = value;
}

public void setSelection (int value) {
	checkWidget();
	if (value < 0) return;
	OS.SetControl32BitValue (handle, value);
}

public void setThumb (int value) {
	checkWidget();
	if (value < 1) return;
    OS.SetControlViewSize (handle, value);
}

public void setValues (int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement) {
	checkWidget();
	if (selection < 0) return;
	if (minimum < 0) return;
	if (maximum < 0) return;
	if (thumb < 1) return;
	if (maximum - minimum - thumb < 0) return;
	if (increment < 1) return;
	if (pageIncrement < 1) return;
	OS.SetControl32BitMinimum (handle, minimum);
	OS.SetControl32BitMaximum (handle, maximum);
	OS.SetControlViewSize (handle, thumb);
	OS.SetControl32BitValue (handle, selection);
	increment = increment;
	pageIncrement = pageIncrement;
}

public void setVisible (boolean visible) {
	checkWidget();
	if (visible) {
		if ((state & HIDDEN) == 0) return;
		state &= ~HIDDEN;
	} else {
		if ((state & HIDDEN) != 0) return;
		state |= HIDDEN;
	}
	OS.HIViewSetVisible (handle, visible);
	sendEvent (visible ? SWT.Show : SWT.Hide);
}
}

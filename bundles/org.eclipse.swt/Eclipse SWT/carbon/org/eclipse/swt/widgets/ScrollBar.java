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

ScrollBar () {
	/* Do nothing */
}

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
	if (dragging) {
		Display display = getDisplay ();
		display.update ();
	}
	return 0;
}

void destroyWidget () {
	int theControl = handle;
	releaseHandle ();
	if (theControl != 0) {
		OS.DisposeControl (theControl);
	}
}

void createHandle () {
	Display display = getDisplay ();
	int actionProc = display.actionProc;
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.scrolledHandle);
	OS.CreateScrollBarControl (window, null, 0, 0, 90, 10, true, actionProc, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
}

void createWidget () {
	super.createWidget ();
	setZOrder ();
}

void deregister () {
	super.deregister ();
	WidgetTable.remove (handle);
}

public Display getDisplay () {
	Scrollable parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

int getDrawCount () {
	return parent.getDrawCount ();
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
	int maximum = OS.GetControl32BitMaximum (handle);
	int viewSize = OS.GetControlViewSize (handle);
    return maximum + viewSize;
}

public int getMinimum () {
	checkWidget();
    return OS.GetControl32BitMinimum (handle);
}

public int getPageIncrement () {
	checkWidget();
    return pageIncrement;
}

public Scrollable getParent () {
	checkWidget ();
	return parent;
}

public int getSelection () {
	checkWidget();
    return OS.GetControl32BitValue (handle);
}

public Point getSize () {
	checkWidget();
	Rect rect = getControlSize (handle);
	return new Point (rect.right - rect.left, rect.bottom - rect.top);
}

public int getThumb () {
	checkWidget();
    return OS.GetControlViewSize (handle);
}

public boolean getVisible () {
	checkWidget();
	return (state & HIDDEN) == 0;
}

void hookEvents () {
	super.hookEvents ();
	Display display = getDisplay ();
	int controlProc = display.controlProc;
	int [] mask = new int [] {
		OS.kEventClassControl, OS.kEventControlDraw,
	};
	int controlTarget = OS.GetControlEventTarget (handle);
	OS.InstallEventHandler (controlTarget, controlProc, mask.length / 2, mask, handle, null);
}

public boolean isEnabled () {
	checkWidget();
	return OS.IsControlEnabled (handle);
}

public boolean isVisible () {
	checkWidget();
	return OS.HIViewIsVisible (handle);
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	int status = super.kEventMouseDown (nextHandler, theEvent, userData);
	if (status == OS.noErr) return status;
	dragging = false;
	status = OS.CallNextEventHandler (nextHandler, theEvent);
	if (dragging) {
		Event event = new Event ();
		sendEvent (SWT.Selection, event);
	}
	dragging = false;
	return status;
}

public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}

void register () {
	super.register ();
	WidgetTable.put (handle, this);
}

void releaseChild () {
	super.releaseChild ();
	//NOT DONE - layout parent
}

void releaseHandle () {
	super.releaseHandle ();
	handle = 0;
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
	OS.SetControl32BitMaximum (handle, value - viewSize);
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
	int maximum = OS.GetControl32BitMaximum (handle);
	int viewSize = OS.GetControlViewSize (handle);
	OS.SetControl32BitMaximum (handle, maximum + viewSize - value);
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
	OS.SetControl32BitMaximum (handle, maximum - thumb);
	OS.SetControlViewSize (handle, thumb);
	OS.SetControl32BitValue (handle, selection);
	this.increment = increment;
	this.pageIncrement = pageIncrement;
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
	parent.layoutControl();
}

void setZOrder () {
	OS.HIViewAddSubview (parent.scrolledHandle, handle);
}

}

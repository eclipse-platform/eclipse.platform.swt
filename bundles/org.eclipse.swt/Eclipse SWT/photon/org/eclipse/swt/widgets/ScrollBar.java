package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class ScrollBar extends Widget {
	Scrollable parent;

ScrollBar (Scrollable parent, int style, int handle) {
	super (parent, checkStyle (style));
	this.parent = parent;
	this.handle = handle;
	createWidget (0);
}

ScrollBar (Scrollable parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (0);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

public void addSelectionListener (SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

void createHandle (int index) {
	if (handle != 0) return;
	int parentHandle = parent.scrolledHandle;
	int [] args = {
		OS.Pt_ARG_MAXIMUM, 100, 0,
		OS.Pt_ARG_PAGE_INCREMENT, 10, 0,
		OS.Pt_ARG_SLIDER_SIZE, 10, 0,
		OS.Pt_ARG_ORIENTATION, (style & SWT.HORIZONTAL) != 0 ? OS.Pt_HORIZONTAL : OS.Pt_VERTICAL, 0,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (OS.PtScrollbar (), parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.PtExtentWidget (handle);
}

public Display getDisplay () {
	Scrollable parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public boolean getEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return (args [1] & OS.Pt_BLOCKED) == 0;
}

public int getIncrement () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_INCREMENT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public int getMinimum () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_MINIMUM, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public int getMaximum () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_MAXIMUM, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public int getPageIncrement () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_PAGE_INCREMENT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public Scrollable getParent () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}

public int getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_SCROLL_POSITION, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public Point getSize () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return new Point (args [1], args [4]);
}

public int getThumb () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_SLIDER_SIZE, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public boolean getVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (topHandle, args.length / 3, args);
	return (args [1] & OS.Pt_DELAY_REALIZE) == 0;
}

void hookEvents () {
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_SCROLL_MOVE, windowProc, SWT.Selection);
}

public boolean isEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getEnabled () && parent.isEnabled ();
}

public boolean isVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return OS.PtWidgetIsRealized (handle);
}

int processSelection (int info) {
	if (info == 0) return OS.Pt_CONTINUE;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.cbdata == 0) return OS.Pt_CONTINUE;
	PtScrollbarCallback_t cb = new PtScrollbarCallback_t ();
	OS.memmove (cb, cbinfo.cbdata, PtScrollbarCallback_t.sizeof);
	Event event = new Event ();
	switch (cb.action) {
		case OS.Pt_SCROLL_DRAGGED:
			event.detail = SWT.DRAG;
			break;
		case OS.Pt_SCROLL_TO_MIN:
			event.detail = SWT.HOME;
			break;
		case OS.Pt_SCROLL_TO_MAX:
			event.detail = SWT.END;
			break;
		case OS.Pt_SCROLL_INCREMENT:
			event.detail = SWT.ARROW_DOWN;
			break;
		case OS.Pt_SCROLL_DECREMENT :
			event.detail = SWT.ARROW_UP;
			break;
		case OS.Pt_SCROLL_PAGE_DECREMENT:
			event.detail = SWT.PAGE_UP;
			break;
		case OS.Pt_SCROLL_PAGE_INCREMENT:
			event.detail = SWT.PAGE_DOWN;
			break;
	}
	sendEvent(SWT.Selection, event);
	return OS.Pt_CONTINUE;
}

public void removeSelectionListener (SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

void setBounds (int x, int y, int width, int height) {
	PhArea_t area = new PhArea_t ();
	area.pos_x = (short) x;
	area.pos_y = (short) y;
	area.size_w = (short) (Math.max (width, 0));
	area.size_h = (short) (Math.max (height, 0));
	int ptr = OS.malloc (PhArea_t.sizeof);
	OS.memmove (ptr, area, PhArea_t.sizeof);
	int [] args = {OS.Pt_ARG_AREA, ptr, 0};
	OS.PtSetResources (handle, args.length / 3, args);
	OS.free (ptr);
}

public void setEnabled (boolean enabled) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_BLOCKED, OS.Pt_BLOCKED,
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_GHOST, OS.Pt_GHOST,
	};
	OS.PtSetResources (handle, args.length / 3, args);
}
public void setIncrement (int value) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_INCREMENT, value, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setMaximum (int value) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_MAXIMUM, value - 1, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setMinimum (int value) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_MINIMUM, value, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setPageIncrement (int value) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_PAGE_INCREMENT, value, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setSelection (int value) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_SCROLL_POSITION, value, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setThumb (int value) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_SLIDER_SIZE, value, 0};
	OS.PtSetResources (handle, args.length / 3, args);
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
	int [] args = {
		OS.Pt_ARG_SCROLL_POSITION, selection, 0,
		OS.Pt_ARG_INCREMENT, increment, 0,
		OS.Pt_ARG_PAGE_INCREMENT, pageIncrement, 0,
		OS.Pt_ARG_SLIDER_SIZE, thumb, 0,
		OS.Pt_ARG_MINIMUM, minimum, 0,
		OS.Pt_ARG_MAXIMUM, maximum - 1, 0,
	};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setVisible (boolean visible) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {
		OS.Pt_ARG_FLAGS, visible ? 0 : OS.Pt_DELAY_REALIZE, OS.Pt_DELAY_REALIZE,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	if (visible) {
		sendEvent (SWT.Show);
		OS.PtRealizeWidget (handle);
		parent.resizeClientArea ();
	} else {
		OS.PtUnrealizeWidget (handle);
		parent.resizeClientArea ();
		sendEvent(SWT.Hide);
	}	
}

}

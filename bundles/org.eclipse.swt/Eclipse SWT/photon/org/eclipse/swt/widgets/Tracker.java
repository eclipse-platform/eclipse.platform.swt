package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

public /*final*/ class Tracker extends Widget {
	Composite parent;
	Display display;
	boolean tracking, stippled;
	Rectangle [] rectangles = new Rectangle [0];
	
public Tracker (Display display, int style) {
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.style = style;
	this.display = display;
}

public Tracker (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	this.display = parent.getDisplay ();
}
public void addControlListener(ControlListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Move,typedListener);
}
public void close () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	tracking = false;
}
public Display getDisplay () {
	return display;
}

public Rectangle [] getRectangles () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return rectangles;
}
public boolean getStippled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return stippled;
}
public boolean open () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
//	int handle = parent.handle;
	PhRect_t r = new PhRect_t ();
	OS.PhWindowQueryVisible (OS.Ph_QUERY_GRAPHICS, 0, 1, r);
	int width = (short) (r.lr_x - r.ul_x + 1);
	int height = (short) (r.lr_y - r.ul_y + 1);
	int [] args = {
		OS.Pt_ARG_WIDTH, 200 /*width*/, 0,
		OS.Pt_ARG_HEIGHT, 200 /*height*/, 0,
		OS.Pt_ARG_REGION_OPAQUE, 0, ~0,
//		OS.Pt_ARG_REGION_SENSE, OS.Ph_EV_BUT_PRESS | OS.Ph_EV_DRAG, ~0,
//		OS.Pt_ARG_FILL_COLOR, OS.Pg_TRANSPARENT, 0,
	};
	OS.PtSetParentWidget (0);
	int handle = OS.PtCreateWidget (OS.PtRegion (), 0, args.length / 3, args);
	OS.PtRealizeWidget (handle);
	int rid = OS.PtWidgetRid (handle);
	int input_group = OS.PhInputGroup (0);
	PhRect_t rect1 = new PhRect_t ();
	for (int i=0; i<rectangles.length; i++) {
		Rectangle rect = rectangles [i];	
		PhRect_t rect2 = new PhRect_t ();
		rect2.ul_x = (short) rect.x;
		rect2.ul_y = (short) rect.y;
		rect2.lr_x = (short) (rect.x + rect.width - 1);
		rect2.lr_y = (short) (rect.y + rect.height - 1);
		if (i == 0) rect1 = rect2;
		OS.PhRectUnion (rect1, rect2);
	}
	rect1.ul_x = rect1.ul_y = 10;
	rect1.lr_x = rect1.lr_y = 100;
	int result = OS.PhInitDrag (rid, OS.Ph_DRAG_KEY_MOTION | OS.Ph_TRACK_DRAG, rect1, null, input_group, null, null, null, null /*pos*/, null);
	return result == 0;
}
public void removeControlListener (ControlListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
}
public void setRectangles (Rectangle [] rectangles) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (rectangles == null) error (SWT.ERROR_NULL_ARGUMENT);
	this.rectangles = rectangles;
}
public void setStippled (boolean stippled) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	this.stippled = stippled;
}

}
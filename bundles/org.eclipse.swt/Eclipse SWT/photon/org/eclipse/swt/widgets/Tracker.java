package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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

void drawRectangles () {
	if (parent != null) {
		if (parent.isDisposed ()) return;
		parent.getShell ().update ();
	} else {
		display.update ();
	}
	int rid = OS.Ph_DEV_RID;
	if (parent != null) rid = OS.PtWidgetRid (parent.handle);
	
	int phGC = OS.PgCreateGC (0);
	if (phGC == 0) return;
	int prevContext = OS.PgSetGC (phGC);
	OS.PgSetRegion (rid);
	OS.PgSetDrawMode (OS.Pg_DRAWMODE_XOR);
	OS.PgSetFillColor (0xffffff);
	
	int bandWidth = 0;
	if (stippled) {
		bandWidth = 2;
		OS.PgSetFillTransPat (OS.Pg_PAT_HALF);
	}
	for (int i=0; i<rectangles.length; i++) {
		Rectangle r = rectangles [i];
		int x1 = r.x;
		int y1 = r.y;
		int x2 = r.x + r.width;
		int y2 = r.y + r.height;
		OS.PgDrawIRect(x1, y1, x2, y1 + bandWidth, OS.Pg_DRAW_FILL);
		OS.PgDrawIRect(x1, y1 + bandWidth + 1, x1 + bandWidth, y2 - bandWidth - 1, OS.Pg_DRAW_FILL);
		OS.PgDrawIRect(x2 - bandWidth, y1 + bandWidth + 1, x2, y2 - bandWidth - 1, OS.Pg_DRAW_FILL);
		OS.PgDrawIRect(x1, y2 - bandWidth, x2, y2, OS.Pg_DRAW_FILL);
	}
	OS.PgSetGC (prevContext);	
	OS.PgDestroyGC (phGC);
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
	int sense = OS.Ph_EV_DRAG | OS.Ph_EV_KEY | OS.Ph_EV_BUT_PRESS |
		OS.Ph_EV_BUT_RELEASE | OS.Ph_EV_PTR_MOTION;
	int [] args = {
		OS.Pt_ARG_WIDTH, 0, 0,
		OS.Pt_ARG_HEIGHT, 0, 0,
		OS.Pt_ARG_REGION_OPAQUE, 0, ~0,
		OS.Pt_ARG_REGION_SENSE, sense, ~0,
		OS.Pt_ARG_FILL_COLOR, OS.Pg_TRANSPARENT, 0,
	};
	OS.PtSetParentWidget (0);
	int handle = OS.PtCreateWidget (OS.PtRegion (), 0, args.length / 3, args);
	OS.PtRealizeWidget (handle);
	PhRect_t rect = new PhRect_t ();
	int rid = OS.PtWidgetRid (handle);
	int input_group = OS.PhInputGroup (0);
	OS.PhInitDrag (rid, OS.Ph_DRAG_KEY_MOTION | OS.Ph_TRACK_DRAG, rect, null, input_group, null, null, null, null, null);
	PhCursorInfo_t info = new PhCursorInfo_t ();
	OS.PhQueryCursor ((short)input_group, info);
	int oldX = info.pos_x;
	int oldY = info.pos_y;
	int size = PhEvent_t.sizeof + 1024;
	int buffer = OS.malloc (size);
	PhEvent_t event = new PhEvent_t ();
	Event ev = new Event ();

	drawRectangles ();
	boolean tracking = true;
	boolean cancelled = false;
	while (tracking && !cancelled) {
		if (parent != null && parent.isDisposed ()) break;
		int result = OS.PhEventNext (buffer, size);
		switch (result) {
			case OS.Ph_EVENT_MSG: break;
			case OS.Ph_RESIZE_MSG:
				size = OS.PhGetMsgSize (buffer);
				OS.free (buffer);
				buffer = OS.malloc (size);
				continue;
		}
		OS.memmove (event, buffer, PhEvent_t.sizeof);
		if (event.type == OS.Ph_EV_DRAG) {
			switch (event.subtype) {
				case OS.Ph_EV_DRAG_MOTION_EVENT: {
					int data = OS.PhGetData (buffer);
					if (data == 0) break;
					PhPointerEvent_t pe = new PhPointerEvent_t ();
					OS.memmove (pe, data, PhPointerEvent_t.sizeof);
					int newX = pe.pos_x;
					int newY = pe.pos_y;
					if (newX != oldX || newY != oldY) {
						drawRectangles ();
						for (int i=0; i<rectangles.length; i++) {
							rectangles [i].x += newX - oldX;
							rectangles [i].y += newY - oldY;
						}
						/*
						* It is possible (but unlikely), that application
						* code could have disposed the widget in the move
						* event.  If this happens, return false to indicate
						* that the tracking has failed.
						*/
						sendEvent (SWT.Move, ev);
						if (isDisposed ()) return false;
						drawRectangles ();
						oldX = newX;
						oldY = newY;
					}
					break;
				}
				case OS.Ph_EV_DRAG_KEY_EVENT: {
					int data = OS.PhGetData (buffer);
					if (data == 0) break;
					PhKeyEvent_t ke = new PhKeyEvent_t ();
					OS.memmove (ke, data, PhKeyEvent_t.sizeof);
					if ((ke.key_flags & OS.Pk_KF_Sym_Valid) != 0) {
						cancelled = ke.key_sym == OS.Pk_Escape;
					}
					break;
				}
				case OS.Ph_EV_DRAG_COMPLETE: {
					tracking = false;
					break;
				}
			}
		}
		OS.PtEventHandler (buffer);
	}
	drawRectangles ();
	tracking = false;
	OS.PtDestroyWidget (handle);
	return !cancelled;
}
void releaseWidget () {
	super.releaseWidget ();
	parent = null;
	display = null;
	rectangles = null;
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

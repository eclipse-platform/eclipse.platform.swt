package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public /*final*/ class Sash extends Control {
	boolean dragging;
	int startX, startY, lastX, lastY;
	
public Sash (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

public void addSelectionListener (SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	if ((style & SWT.HORIZONTAL) != 0) {
		width += DEFAULT_WIDTH;  height += 3;
	} else {
		width += 3; height += DEFAULT_HEIGHT;
	}
	if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
	if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
	return new Point (width, height);
}

void createHandle (int index) {
	Display display = getDisplay ();
	int clazz = display.PtContainer;
	int parentHandle = parent.handle;
	int cursor = ((style & SWT.HORIZONTAL) != 0) ? OS.Ph_CURSOR_DRAG_VERTICAL : OS.Ph_CURSOR_DRAG_HORIZONTAL;
	int [] args = {
		OS.Pt_ARG_CURSOR_TYPE, cursor, 0,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

void drawBand (int x, int y, int width, int height) {
	if (parent == null) return;
	if (parent.isDisposed ()) return;
	int parentHandle = parent.handle;
	if (!OS.PtWidgetIsRealized (parentHandle)) return;
	int phGC = OS.PgCreateGC (0); // NOTE: PgCreateGC ignores the parameter
	if (phGC == 0) return;
	int [] args = {OS.Pt_ARG_COLOR, 0, 0, OS.Pt_ARG_FILL_COLOR, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int foreground = args [1];
	int background = args [4];
	int color = foreground ^ ~background;
	int prevContext = OS.PgSetGC (phGC);
	OS.PgSetRegion (OS.PtWidgetRid (parentHandle));
	OS.PgSetDrawMode (OS.Pg_DRAWMODE_XOR);
	OS.PgSetFillColor (color);
	OS.PgDrawIRect (x, y, x + width - 1, y + height - 1, OS.Pg_DRAW_FILL);
	OS.PgSetGC (prevContext);	
	OS.PgDestroyGC (phGC);
}

int processMouse (int info) {
	int result = super.processMouse (info);
	
	if (info == 0) return OS.Pt_END;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.event == 0) return OS.Pt_END;
	PhEvent_t ev = new PhEvent_t ();
	OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
	int data = OS.PhGetData (cbinfo.event);
	if (data == 0) return OS.Pt_END;
	PhPointerEvent_t pe = new PhPointerEvent_t ();
	OS.memmove (pe, data, PhPointerEvent_t.sizeof);
	if (pe.buttons != OS.Ph_BUTTON_SELECT) return result;

	int x = pe.pos_x + ev.translation_x;
	int y = pe.pos_y + ev.translation_y;
	PhArea_t area = new PhArea_t();
	OS.PtWidgetArea (handle, area);
	Event event = new Event ();
	int width = event.width = area.size_w;
	int height = event.height = area.size_h;
	switch (ev.type) {
		case OS.Ph_EV_BUT_PRESS:
			PhRect_t rect = new PhRect_t ();
			PhPoint_t pos = new PhPoint_t();
			pos.x = pe.pos_x;
			pos.y = pe.pos_y;
			rect.ul_x = rect.lr_x = (short) (pos.x + ev.translation_x);
			rect.ul_y = rect.lr_y = (short) (pos.y + ev.translation_y);
			int rid = OS.PtWidgetRid (handle);
			int input_group = OS.PhInputGroup (cbinfo.event);
//			int input_group = OS.PhInputGroup (0);
			OS.PhInitDrag (rid, OS.Ph_DRAG_KEY_MOTION | OS.Ph_DRAG_TRACK | OS.Ph_TRACK_DRAG, rect, null, input_group, null, null, null, pos, null);

			/* Compute the banding rectangle */
			startX = x;
			startY = y;
			lastX = area.pos_x;
			lastY = area.pos_y;

			/* The event must be sent because doit flag is used */
			event.x = lastX;  event.y = lastY;
			event.detail = SWT.DRAG;
			
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the selection
			* event.  If this happens, end the processing of the
			* Windows message by returning zero as the result of
			* the window proc.
			*/
			sendEvent (SWT.Selection, event);
			if (isDisposed ()) return OS.Pt_END;
			
			/* Draw the banding rectangle */
			if (event.doit) {
				dragging = true;
				menuShell ().bringToTop ();
				OS.PtFlush ();
				drawBand (lastX = event.x, lastY = event.y, width, height);
			}
			break;
		case OS.Ph_EV_BUT_RELEASE:
			if (ev.subtype != OS.Ph_EV_RELEASE_PHANTOM) {
				return result;
			}
			/* Compute the banding rectangle */
			if (!dragging) return result;
			dragging = false;
			
			/* The event must be sent because doit flag is used */
			event.x = lastX;  event.y = lastY;
			drawBand (lastX, lastY, width, height);
			sendEvent (SWT.Selection, event);
			// widget could be disposed at this point
			break;
		case OS.Ph_EV_PTR_MOTION_BUTTON:
		case OS.Ph_EV_PTR_MOTION_NOBUTTON:
		case OS.Ph_EV_DRAG:
			if (ev.subtype != OS.Ph_EV_DRAG_MOTION_EVENT) {
				return result;
			}
			if (!dragging) return result;
		
			/* Compute the banding rectangle */
			x += area.pos_x;
			y += area.pos_y;
			int parentHandle = parent.handle;
			rect = new PhRect_t ();
			int vParent = OS.PtValidParent (handle, OS.PtContainer ());
			OS.PtCalcCanvas (vParent, rect);
			int clientWidth = rect.lr_x - rect.ul_x + 1;
			int clientHeight = rect.lr_y - rect.ul_y + 1;
			int newX = lastX, newY = lastY;
			if ((style & SWT.VERTICAL) != 0) {
				newX = Math.min (Math.max (0, x - startX), clientWidth - width);
			} else {
				newY = Math.min (Math.max (0, y - startY), clientHeight - height);
			}
			if ((newX == lastX) && (newY == lastY)) return result;
			drawBand (lastX, lastY, width, height);
		
			/* The event must be sent because doit flag is used */
			event.x = newX;  event.y = newY;
			event.detail = SWT.DRAG;
			
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the selection
			* event.  If this happens, end the processing of the
			* Windows message by returning zero as the result of
			* the window proc.
			*/
			sendEvent (SWT.Selection, event);
			if (isDisposed ()) return OS.Pt_END;
		
			/* Draw the banding rectangle */
			if (event.doit) {
				lastX = event.x; lastY = event.y;
				OS.PtFlush ();
				drawBand (lastX, lastY, width, height);
			}
			break;
	}
	return result;
}

int processPaint (int damage) {
	OS.PtSuperClassDraw (OS.PtContainer (), handle, damage);
	sendPaintEvent (damage);
	return OS.Pt_CONTINUE;
}
public void removeSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

}
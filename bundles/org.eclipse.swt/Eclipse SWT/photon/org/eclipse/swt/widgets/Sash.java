/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.photon.*;
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
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#sash">Sash snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Sash extends Control {
	boolean dragging;
	int startX, startY, lastX, lastY;
	final static int INCREMENT = 1;
	final static int PAGE_INCREMENT = 9;
	
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
 * @see SWT#SMOOTH
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Sash (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the x, y, width, and height fields of the event object are valid.
 * If the receiver is being dragged, the event object detail field contains the value <code>SWT.DRAG</code>.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
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
	state |= GRAB | HANDLE;
	int clazz = display.PtContainer;
	int parentHandle = parent.parentingHandle ();
	int cursor = ((style & SWT.HORIZONTAL) != 0) ? OS.Ph_CURSOR_DRAG_VERTICAL : OS.Ph_CURSOR_DRAG_HORIZONTAL;
	int [] args = {
		OS.Pt_ARG_FLAGS, 0, OS.Pt_GETS_FOCUS,
		OS.Pt_ARG_CURSOR_TYPE, cursor, 0,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

byte [] defaultFont () {
	return display.TITLE_FONT;
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
	int disjoint = OS.PtFindDisjoint( handle );
	if( disjoint != 0 )
		OS.PgSetRegion( OS.PtWidgetRid( disjoint ) );
	PhPoint_t pt = new PhPoint_t ();
//	PhRect_t tran_rect = new PhRect_t();
	if (parentHandle <= 0) return; 
	OS.PtWidgetOffset(parentHandle, pt);
	OS.PgSetTranslation(pt,0);
	OS.PgSetDrawMode (OS.Pg_DrawModeDSx);
	OS.PgSetFillColor (color);
	OS.PgDrawIRect (x, y, x + width - 1, y + height - 1, OS.Pg_DRAW_FILL);
	OS.PgSetGC (prevContext);	
	OS.PgDestroyGC (phGC);
}

int Ph_EV_BUT_PRESS (int widget, int info) {
	int result = super.Ph_EV_BUT_PRESS (widget, info);
	if (result != OS.Pt_CONTINUE)return result;
	processMouse (info);
	return result;
}

int Ph_EV_BUT_RELEASE (int widget, int info) {
	int result = super.Ph_EV_BUT_RELEASE (widget, info);
	if (result != OS.Pt_CONTINUE)return result;
	processMouse (info);
	return result;
}

int Ph_EV_DRAG (int widget, int info) {
	int result = super.Ph_EV_DRAG (widget, info);
	if (result != OS.Pt_CONTINUE)return result;
	processMouse (info);
	return result;
}

int Ph_EV_PTR_MOTION (int widget, int info) {
	int result = super.Ph_EV_PTR_MOTION (widget, info);
	if (result != OS.Pt_CONTINUE)return result;
	processMouse (info);
	return result;
}

void processMouse (int info) {	
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	PhEvent_t ev = new PhEvent_t ();
	OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
	int data = OS.PhGetData (cbinfo.event);
	PhPointerEvent_t pe = new PhPointerEvent_t ();
	OS.memmove (pe, data, PhPointerEvent_t.sizeof);
	if (pe.buttons != OS.Ph_BUTTON_SELECT) return;

	int x = pe.pos_x + ev.translation_x;
	int y = pe.pos_y + ev.translation_y;
	PhArea_t area = new PhArea_t ();
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
//			int input_group = OS.PhInputGroup (cbinfo.event);
			int input_group = OS.PhInputGroup (0);
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
			if (isDisposed ()) return;
			
			/* Draw the banding rectangle */
			if (event.doit) {
				dragging = true;
				menuShell ().bringToTop (true);
				OS.PtFlush ();
				drawBand (lastX = event.x, lastY = event.y, width, height);
			}
			break;
		case OS.Ph_EV_BUT_RELEASE:
			if (ev.subtype != OS.Ph_EV_RELEASE_PHANTOM) {
				return;
			}
			/* Compute the banding rectangle */
			if (!dragging) return;
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
				return;
			}
			if (!dragging) return;
		
			/* Compute the banding rectangle */
			x += area.pos_x;
			y += area.pos_y;
			Rectangle r = parent.getClientArea ();
			int clientWidth = r.width;
			int clientHeight = r.height;
			int newX = lastX, newY = lastY;
			if ((style & SWT.VERTICAL) != 0) {
				newX = Math.min (Math.max (0, x - startX), clientWidth - width);
			} else {
				newY = Math.min (Math.max (0, y - startY), clientHeight - height);
			}
			if ((newX == lastX) && (newY == lastY)) return;
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
			if (isDisposed ()) return;
		
			/* Draw the banding rectangle */
			if (event.doit) {
				lastX = event.x; lastY = event.y;
				OS.PtFlush ();
				drawBand (lastX, lastY, width, height);
			}
			break;
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
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
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

int traversalCode (int key_sym, PhKeyEvent_t ke) {
	return 0;
}

boolean translateTraversal (int key_sym, PhKeyEvent_t phEvent) {
	boolean result = super.translateTraversal (key_sym, phEvent);
	if (!result) {
		switch (key_sym) {
			case OS.Pk_Left:
			case OS.Pk_Right:
			case OS.Pk_Up:
			case OS.Pk_Down:
				
				/* Calculate the new x or y position */
				if ((phEvent.button_state & OS.Ph_BUTTON_SELECT) != 0) return result;
				int step = (phEvent.key_mods & OS.Pk_KM_Ctrl) != 0 ? INCREMENT : PAGE_INCREMENT;
				int x = 0, y = 0;
				if ((style & SWT.VERTICAL) != 0) {
					if (key_sym == OS.Pk_Up || key_sym == OS.Pk_Down) break;
					x = key_sym == OS.Pk_Left ? -step : step;
				} else {
					if (key_sym == OS.Pk_Left || key_sym == OS.Pk_Right) break;
					y = key_sym == OS.Pk_Up ? -step : step;
				}				
				PhArea_t area = new PhArea_t ();
				OS.PtWidgetArea (handle, area);
				x += area.pos_x;
				y += area.pos_y;
				int width = area.size_w;
				int height = area.size_h;
				Rectangle r = parent.getClientArea ();
				int clientWidth = r.width;
				int clientHeight = r.height;
				int newX = lastX, newY = lastY;
				if ((style & SWT.VERTICAL) != 0) {
					newX = Math.min (Math.max (0, x - startX), clientWidth - width);
				} else {
					newY = Math.min (Math.max (0, y - startY), clientHeight - height);
				}
				if (newX == lastX && newY == lastY) return result;
			
				/* The event must be sent because doit flag is used */
				Event event = new Event ();
				event.x = newX;  event.y = newY;
				event.width = width;  event.height = height;
				
				/*
				* It is possible (but unlikely), that application
				* code could have disposed the widget in the selection
				* event.  If this happens, end the processing of the
				* Windows message by returning zero as the result of
				* the window proc.
				*/
				sendEvent (SWT.Selection, event);
				if (isDisposed ()) return true;
				if (event.doit) {
					OS.PtWidgetArea (handle, area);
					int cursorX = area.size_w / 2, cursorY = area.size_h / 2;
					short [] absX = new short [1], absY = new short [1];
					OS.PtGetAbsPosition (handle, absX, absY);
					OS.PhMoveCursorAbs (OS.PhInputGroup (0), cursorX + absX [0], cursorY + absY [0]);
				}
				return result;
		}
	}
	return result;
}

int widgetClass () {
	return OS.PtContainer ();
}

}

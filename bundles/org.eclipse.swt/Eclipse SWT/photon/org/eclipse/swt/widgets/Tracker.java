package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

/**
 *  Instances of this class implement rubber banding rectangles that are
 *  drawn onto a parent <code>Composite</code> or <code>Display</code>.
 *  These rectangles can be specified to respond to mouse and key events
 *  by either moving or resizing themselves accordingly.  Trackers are
 *  typically used to represent window geometries in a lightweight manner.
 *  
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, UP, DOWN, RESIZE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Move, Resize</dd>
 * </dl>
 * <p>
 * Note: Rectangle move behavior is assumed unless RESIZE is specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Tracker extends Widget {
	Composite parent;
	Display display;
	boolean tracking, stippled;
	Rectangle [] rectangles, proportions;
	int resizeCursor, clientCursor, clientBitmap;
	int cursorOrientation = SWT.NONE;

	/*
	* The following values mirror step sizes on Windows
	*/
	final static int STEPSIZE_SMALL = 1;
	final static int STEPSIZE_LARGE = 9;
	
/**
 * Constructs a new instance of this class given the display
 * to create it on and a style value describing its behavior
 * and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p><p>
 * Note: Currently, null can be passed in for the display argument.
 * This has the effect of creating the tracker on the currently active
 * display if there is one. If there is no current display, the 
 * tracker is created on a "default" display. <b>Passing in null as
 * the display argument is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param display the display to create the tracker on
 * @param style the style of control to construct
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 * 
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#UP
 * @see SWT#DOWN
 */
public Tracker (Display display, int style) {
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.style = checkStyle (style);
	this.display = display;
}

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
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#UP
 * @see SWT#DOWN
 * @see SWT#RESIZE
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Tracker (Composite parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	display = parent.getDisplay ();
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is moved or resized, by sending
 * it one of the messages defined in the <code>ControlListener</code>
 * interface.
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
 * @see ControlListener
 * @see #removeControlListener
 */
public void addControlListener(ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Move,typedListener);
}

Point adjustMoveCursor () {
	Rectangle bounds = computeBounds ();
	int newX = bounds.x + bounds.width / 2;
	int newY = bounds.y;
	/*
	 * Convert to screen coordinates iff needed
 	 */
	if (parent != null) {
		short [] x = new short [1], y = new short [1];
		OS.PtGetAbsPosition (parent.handle, x, y);
		newX += x [0];
		newY += y [0];
	}
	OS.PhMoveCursorAbs(OS.PhInputGroup (0), newX, newY);
	return new Point (newX, newY);
}

Point adjustResizeCursor () {
	int newX, newY;
	Rectangle bounds = computeBounds ();

	if ((cursorOrientation & SWT.LEFT) != 0) {
		newX = bounds.x;
	} else if ((cursorOrientation & SWT.RIGHT) != 0) {
		newX = bounds.x + bounds.width;
	} else {
		newX = bounds.x + bounds.width / 2;
	}

	if ((cursorOrientation & SWT.UP) != 0) {
		newY = bounds.y;
	} else if ((cursorOrientation & SWT.DOWN) != 0) {
		newY = bounds.y + bounds.height;
	} else {
		newY = bounds.y + bounds.height / 2;
	}

	/*
	 * Convert to screen coordinates iff needed
 	 */
	if (parent != null) {
		short [] x = new short [1], y = new short [1];
		OS.PtGetAbsPosition (parent.handle, x, y);
		newX += x [0];
		newY += y [0];
	}
	OS.PhMoveCursorAbs(OS.PhInputGroup (0), newX, newY);

	/*
	* If the client has not provided a custom cursor then determine
	* the appropriate resize cursor.
	*/
	if (clientCursor == 0) {
		int newCursor = 0;
		switch (cursorOrientation) {
			case SWT.UP:
				newCursor = OS.Ph_CURSOR_DRAG_TOP;
				break;
			case SWT.DOWN:
				newCursor = OS.Ph_CURSOR_DRAG_BOTTOM;
				break;
			case SWT.LEFT:
				newCursor = OS.Ph_CURSOR_DRAG_LEFT;
				break;
			case SWT.RIGHT:
				newCursor = OS.Ph_CURSOR_DRAG_RIGHT;
				break;
			case SWT.LEFT | SWT.UP:
				newCursor = OS.Ph_CURSOR_DRAG_TL;
				break;
			case SWT.RIGHT | SWT.DOWN:
				newCursor = OS.Ph_CURSOR_DRAG_BR;
				break;
			case SWT.LEFT | SWT.DOWN:
				newCursor = OS.Ph_CURSOR_DRAG_BL;
				break;
			case SWT.RIGHT | SWT.UP:
				newCursor = OS.Ph_CURSOR_DRAG_TR;
				break;
			default:
				newCursor = OS.Ph_CURSOR_MOVE;
				break;
		}
		resizeCursor = newCursor;
	}
		
	return new Point (newX, newY);
}

static int checkStyle (int style) {
	if ((style & (SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN)) == 0) {
		style |= SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN;
	}
	return style;
}

/**
 * Stops displaying the tracker rectangles.  Note that this is not considered
 * to be a cancelation by the user.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void close () {
	checkWidget();
	tracking = false;
}

Rectangle computeBounds () {
	int xMin = rectangles [0].x;
	int yMin = rectangles [0].y;
	int xMax = rectangles [0].x + rectangles [0].width;
	int yMax = rectangles [0].y + rectangles [0].height;
	
	for (int i = 1; i < rectangles.length; i++) {
		if (rectangles [i].x < xMin) xMin = rectangles [i].x;
		if (rectangles [i].y < yMin) yMin = rectangles [i].y;
		int rectRight = rectangles [i].x + rectangles [i].width;
		if (rectRight > xMax) xMax = rectRight;		
		int rectBottom = rectangles [i].y + rectangles [i].height;
		if (rectBottom > yMax) yMax = rectBottom;
	}
	
	return new Rectangle (xMin, yMin, xMax - xMin, yMax - yMin);
}

Rectangle [] computeProportions (Rectangle [] rects) {
	Rectangle [] result = new Rectangle [rects.length];
	Rectangle bounds = computeBounds ();
	for (int i = 0; i < rects.length; i++) {
		result[i] = new Rectangle (
			(rects[i].x - bounds.x) * 100 / bounds.width,
			(rects[i].y - bounds.y) * 100 / bounds.height,
			rects[i].width * 100 / bounds.width,
			rects[i].height * 100 / bounds.height);
	}
	return result;
}

void drawRectangles (Rectangle [] rects) {
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
	for (int i=0; i<rects.length; i++) {
		Rectangle r = rects [i];
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

/**
 * Returns the bounds that are being drawn, expressed relative to the parent
 * widget.  If the parent is a <code>Display</code> then these are screen
 * coordinates.
 *
 * @return the bounds of the Rectangles being drawn
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle [] getRectangles () {
	checkWidget();
	return rectangles;
}

/**
 * Returns <code>true</code> if the rectangles are drawn with a stippled line, <code>false</code> otherwise.
 *
 * @return the stippled effect of the rectangles
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getStippled () {
	checkWidget();
	return stippled;
}

void moveRectangles (int xChange, int yChange) {
	if (xChange < 0 && ((style & SWT.LEFT) == 0)) return;
	if (xChange > 0 && ((style & SWT.RIGHT) == 0)) return;
	if (yChange < 0 && ((style & SWT.UP) == 0)) return;
	if (yChange > 0 && ((style & SWT.DOWN) == 0)) return;
	Rectangle bounds = computeBounds ();
	bounds.x += xChange; bounds.y += yChange;
	for (int i = 0; i < rectangles.length; i++) {
		rectangles [i].x += xChange;
		rectangles [i].y += yChange;
	}
}

/**
 * Displays the Tracker rectangles for manipulation by the user.  Returns when
 * the user has either finished manipulating the rectangles or has cancelled the
 * Tracker.
 * 
 * @return <code>true</code> if the user did not cancel the Tracker, <code>false</code> otherwise
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean open () {
	checkWidget();
	if (rectangles == null) return false;
	
	int input_group = OS.PhInputGroup (0);
	PhCursorInfo_t info = new PhCursorInfo_t ();
	OS.PhQueryCursor ((short)input_group, info);
	
	if ((style & SWT.MENU) == 0) {
		/*
		* This code is intentionally commented. Tracking can happen through
		* the keyboard.
		*/
//		if ((info.button_state & OS.Ph_BUTTON_SELECT) == 0) return false;
	}
	
	int region = 0;
	if (info.dragger == 0) {
		PhRect_t rect = new PhRect_t ();
		OS.PhWindowQueryVisible (OS.Ph_QUERY_CONSOLE, 0, 1, rect);
		int sense = OS.Ph_EV_DRAG | OS.Ph_EV_KEY | OS.Ph_EV_BUT_PRESS |
			OS.Ph_EV_BUT_RELEASE | OS.Ph_EV_PTR_MOTION;
		int [] args = {
			OS.Pt_ARG_WIDTH, rect.lr_x - rect.ul_x + 1, 0,
			OS.Pt_ARG_HEIGHT, rect.lr_y - rect.ul_y + 1, 0,
			OS.Pt_ARG_REGION_OPAQUE, 0, ~0,
			OS.Pt_ARG_REGION_SENSE, sense, ~0,
			OS.Pt_ARG_REGION_FLAGS, OS.Ph_FORCE_BOUNDARY, OS.Ph_FORCE_BOUNDARY,
			OS.Pt_ARG_FILL_COLOR, OS.Pg_TRANSPARENT, 0,
		};
		region = OS.PtCreateWidget (OS.PtRegion (), OS.Pt_NO_PARENT, args.length / 3, args);
		OS.PtRealizeWidget (region);
	
		rect = new PhRect_t ();
		int rid = OS.PtWidgetRid (region);
		OS.PhInitDrag (rid, OS.Ph_DRAG_KEY_MOTION | OS.Ph_TRACK_DRAG, rect, null, input_group, null, null, null, null, null);
	}

	int oldX, oldY;
	int size = PhEvent_t.sizeof + 1024;
	int buffer = OS.malloc (size);
	PhEvent_t phEvent = new PhEvent_t ();
	Event event = new Event ();
	Point cursorPos;

	drawRectangles (rectangles);
	if ((style & SWT.MENU) == 0) {
		oldX = info.pos_x;
		oldY = info.pos_y;
	} else {
		if ((style & SWT.RESIZE) != 0) {
			cursorPos = adjustResizeCursor ();
		} else {
			cursorPos = adjustMoveCursor ();
		}
		oldX = cursorPos.x;
		oldY = cursorPos.y;
	}
	
	tracking = true;
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
		OS.memmove (phEvent, buffer, PhEvent_t.sizeof);
		if (phEvent.type == OS.Ph_EV_DRAG) {
			switch (phEvent.subtype) {
				case OS.Ph_EV_DRAG_MOTION_EVENT: {
					int data = OS.PhGetData (buffer);
					if (data == 0) break;
					PhPointerEvent_t pe = new PhPointerEvent_t ();
					OS.memmove (pe, data, PhPointerEvent_t.sizeof);
					int newX = pe.pos_x;
					int newY = pe.pos_y;
					if (newX != oldX || newY != oldY) {
						drawRectangles (rectangles);
						event.x = newX;
						event.y = newY;
						if ((style & SWT.RESIZE) != 0) {
							resizeRectangles (newX - oldX, newY - oldY);
							cursorPos = adjustResizeCursor ();
							newX = cursorPos.x; newY = cursorPos.y;
							sendEvent (SWT.Resize, event);
						} else {
							moveRectangles (newX - oldX, newY - oldY);
							sendEvent (SWT.Move, event);
						}
						/*
						* It is possible (but unlikely), that application
						* code could have disposed the widget in the move
						* event.  If this happens, return false to indicate
						* that the tracking has failed.
						*/
						if (isDisposed ()) return false;
						drawRectangles (rectangles);
						oldX = newX;  oldY = newY;
					}
					break;
				}
				case OS.Ph_EV_DRAG_KEY_EVENT: {
					int data = OS.PhGetData (buffer);
					if (data == 0) break;
					PhKeyEvent_t ke = new PhKeyEvent_t ();
					OS.memmove (ke, data, PhKeyEvent_t.sizeof);
					if ((ke.key_flags & OS.Pk_KF_Sym_Valid) != 0) {
						int stepSize = (ke.key_mods & OS.Pk_KM_Ctrl) != 0 ? STEPSIZE_SMALL : STEPSIZE_LARGE;
						int xChange = 0, yChange = 0;
						switch (ke.key_sym) {
							case OS.Pk_Escape:
								cancelled = true;
								tracking = false;
								break;
							case OS.Pk_Return:
								tracking = false;
								break;
							case OS.Pk_Left:
								xChange = -stepSize;
								break;
							case OS.Pk_Right:
								xChange = stepSize;
								break;
							case OS.Pk_Up:
								yChange = -stepSize;
								break;
							case OS.Pk_Down:
								yChange = stepSize;
								break;
						}
						if (xChange != 0 || yChange != 0) {
							drawRectangles (rectangles);
							int newX = oldX + xChange;
							int newY = oldY + yChange;
							event.x = newX;
							event.y = newY;
							if ((style & SWT.RESIZE) != 0) {
								resizeRectangles (xChange, yChange);
								cursorPos = adjustResizeCursor ();
								sendEvent (SWT.Resize, event);
							} else {
								moveRectangles (xChange, yChange);
								cursorPos = adjustMoveCursor ();
								sendEvent (SWT.Move, event);
							}
							/*
							* It is possible (but unlikely) that application
							* code could have disposed the widget in the move
							* event.  If this happens return false to indicate
							* that the tracking has failed.
							*/
							if (isDisposed ()) return false;
							drawRectangles (rectangles);
							oldX = cursorPos.x;  oldY = cursorPos.y;
						}
					}
					break;
				}
				case OS.Ph_EV_DRAG_COMPLETE: {
					tracking = false;
					break;
				}
			}
			if (phEvent.collector_handle != 0) {
				setCursor (phEvent.collector_handle);
			}
			/*
			* Don't dispatch mouse and key events in general, EXCEPT once this
			* tracker has finished its work.
			*/
			if (tracking && !cancelled) continue;
				
		}
		OS.PtEventHandler (buffer);
	}
	drawRectangles (rectangles);
	tracking = false;
	if (region != 0) OS.PtDestroyWidget (region);
	return !cancelled;
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
	display = null;
	rectangles = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
}

void resizeRectangles (int xChange, int yChange) {
	/*
	* If the cursor orientation has not been set in the orientation of
	* this change then try to set it here.
	*/
	if (xChange < 0 && ((style & SWT.LEFT) != 0) && ((cursorOrientation & SWT.RIGHT) == 0)) {
		cursorOrientation |= SWT.LEFT;
	} else if (xChange > 0 && ((style & SWT.RIGHT) != 0) && ((cursorOrientation & SWT.LEFT) == 0)) {
		cursorOrientation |= SWT.RIGHT;
	} else if (yChange < 0 && ((style & SWT.UP) != 0) && ((cursorOrientation & SWT.DOWN) == 0)) {
		cursorOrientation |= SWT.UP;
	} else if (yChange > 0 && ((style & SWT.DOWN) != 0) && ((cursorOrientation & SWT.UP) == 0)) {
		cursorOrientation |= SWT.DOWN;
	}
	Rectangle bounds = computeBounds ();
	if ((cursorOrientation & SWT.LEFT) != 0) {
		bounds.x += xChange;
		bounds.width -= xChange;
	} else if ((cursorOrientation & SWT.RIGHT) != 0) {
		bounds.width += xChange;
	}
	if ((cursorOrientation & SWT.UP) != 0) {
		bounds.y += yChange;
		bounds.height -= yChange;
	} else if ((cursorOrientation & SWT.DOWN) != 0) {
		bounds.height += yChange;
	}
	/*
	* The following are conditions under which the resize should not be applied
	*/
	if (bounds.width < 0 || bounds.height < 0) return;
	
	Rectangle [] newRects = new Rectangle [rectangles.length];
	for (int i = 0; i < rectangles.length; i++) {
		Rectangle proportion = proportions[i];
		newRects[i] = new Rectangle (
			proportion.x * bounds.width / 100 + bounds.x,
			proportion.y * bounds.height / 100 + bounds.y,
			proportion.width * bounds.width / 100,
			proportion.height * bounds.height / 100);
	}
	rectangles = newRects;	
}

/**
 * Sets the <code>Cursor</code> of the Tracker.  If this cursor is <code>null</code>
 * then the cursor reverts to the default.
 *
 * @param newCursor the new <code>Cursor</code> to display
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCursor (Cursor cursor) {
	checkWidget();
	int type = 0;
	int bitmap = 0;
	if (cursor != null) {
		if (cursor.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		type = cursor.type;
		bitmap = cursor.bitmap;
	}
	clientCursor = type;
	clientBitmap = bitmap;
}

void setCursor (int cursorHandle) {
	if (cursorHandle == 0) return;
	int type = 0;
	int bitmap = 0;
	if (clientCursor != 0) {
		type = clientCursor;
		bitmap = clientBitmap;
	} else if (resizeCursor != 0) {
		type = resizeCursor;
	}
	int [] args = new int []{
		OS.Pt_ARG_CURSOR_TYPE, type, 0,
		OS.Pt_ARG_BITMAP_CURSOR, bitmap, 0,
	};
	OS.PtSetResources (cursorHandle, args.length / 3, args);
	
	/*
	* Bug in Photon. For some reason, the widget cursor will
	* not change, when the new cursor is a bitmap cursor, if
	* the flag Ph_CURSOR_NO_INHERIT is reset. The fix is to reset
	* this flag after changing the cursor type to Ph_CURSOR_BITMAP.
	*/
	if (type == OS.Ph_CURSOR_BITMAP) {
		type &= ~OS.Ph_CURSOR_NO_INHERIT;
		OS.PtSetResource (cursorHandle, OS.Pt_ARG_CURSOR_TYPE, type, 0);
	}
}

/**
 * Specifies the rectangles that should be drawn, expressed relative to the parent
 * widget.  If the parent is a Display then these are screen coordinates.
 *
 * @param rectangles the bounds of the rectangles to be drawn
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setRectangles (Rectangle [] rectangles) {
	checkWidget();
	if (rectangles == null) error (SWT.ERROR_NULL_ARGUMENT);
	this.rectangles = rectangles;
	proportions = computeProportions (rectangles);
}

/**
 * Changes the appearance of the line used to draw the rectangles.
 *
 * @param stippled <code>true</code> if rectangle should appear stippled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setStippled (boolean stippled) {
	checkWidget();
	this.stippled = stippled;
}

}

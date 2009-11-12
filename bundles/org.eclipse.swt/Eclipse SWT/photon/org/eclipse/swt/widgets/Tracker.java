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
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tracker">Tracker snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Tracker extends Widget {
	Composite parent;
	boolean tracking, stippled;
	Rectangle [] rectangles = new Rectangle [0], proportions = rectangles;
	Rectangle bounds;
	int resizeCursor;
	Cursor clientCursor;
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
 * @see SWT#RESIZE
 */
public Tracker (Display display, int style) {
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.style = checkStyle (style);
	this.display = display;
	reskinWidget ();
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
	addListener (SWT.Resize, typedListener);
	addListener (SWT.Move, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard, by sending
 * it one of the messages defined in the <code>KeyListener</code>
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
 * @see KeyListener
 * @see #removeKeyListener
 */
public void addKeyListener (KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.KeyUp,typedListener);
	addListener (SWT.KeyDown,typedListener);
}

Point adjustMoveCursor () {
	if (bounds == null) return null;
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
	if (bounds == null) return null;
	int newX, newY;

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
	if (clientCursor == null) {
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
	if (rectangles.length == 0) return null;
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
	bounds = computeBounds ();
	if (bounds != null) {
		for (int i = 0; i < rects.length; i++) {
			int x = 0, y = 0, width = 0, height = 0;
			if (bounds.width != 0) {
				x = (rects [i].x - bounds.x) * 100 / bounds.width;
				width = rects [i].width * 100 / bounds.width;
			} else {
				width = 100;
			}
			if (bounds.height != 0) {
				y = (rects [i].y - bounds.y) * 100 / bounds.height;
				height = rects [i].height * 100 / bounds.height;
			} else {
				height = 100;
			}
			result [i] = new Rectangle (x, y, width, height);			
		}
	}
	return result;
}

void drawRectangles (Rectangle [] rects, boolean stippled) {
	int rid = OS.Ph_DEV_RID;
	if (parent != null) rid = OS.PtWidgetRid (parent.handle);
	
	int phGC = OS.PgCreateGC (0);
	if (phGC == 0) return;
	int prevContext = OS.PgSetGC (phGC);
	OS.PgSetRegion (rid);
	OS.PgSetDrawMode (OS.Pg_DrawModeDSx);
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
	Rectangle [] result = new Rectangle [rectangles.length];
	for (int i = 0; i < rectangles.length; i++) {
		Rectangle current = rectangles [i];
		result [i] = new Rectangle (current.x, current.y, current.width, current.height);
	}
	return result;
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
	if (bounds == null) return;
	if (xChange < 0 && ((style & SWT.LEFT) == 0)) xChange = 0;
	if (xChange > 0 && ((style & SWT.RIGHT) == 0)) xChange = 0;
	if (yChange < 0 && ((style & SWT.UP) == 0)) yChange = 0;
	if (yChange > 0 && ((style & SWT.DOWN) == 0)) yChange = 0;
	if (xChange == 0 && yChange == 0) return;
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
		OS.PhWindowQueryVisible (OS.Ph_QUERY_CONSOLE, 0, OS.PhInputGroup (0), rect);
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

	int oldX = info.pos_x, oldY = info.pos_y;
	int size = PhEvent_t.sizeof + 1024;
	int buffer = OS.malloc (size);
	PhEvent_t phEvent = new PhEvent_t ();
	Event event = new Event ();
	
	// if exactly one of UP/DOWN is specified as a style then set the cursor
	// orientation accordingly (the same is done for LEFT/RIGHT styles below)
	int vStyle = style & (SWT.UP | SWT.DOWN);
	if (vStyle == SWT.UP || vStyle == SWT.DOWN) {
		cursorOrientation |= vStyle;
	}
	int hStyle = style & (SWT.LEFT | SWT.RIGHT);
	if (hStyle == SWT.LEFT || hStyle == SWT.RIGHT) {
		cursorOrientation |= hStyle;
	}

	update ();
	drawRectangles (rectangles, stippled);
	Point cursorPos = null;
	if ((style & SWT.MENU) == 0) {
		oldX = info.pos_x;
		oldY = info.pos_y;
	} else {
		if ((style & SWT.RESIZE) != 0) {
			cursorPos = adjustResizeCursor ();
		} else {
			cursorPos = adjustMoveCursor ();
		}
		if (cursorPos != null) {
			oldX = cursorPos.x;
			oldY = cursorPos.y;
		}
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
						Rectangle [] oldRectangles = rectangles;
						boolean oldStippled = stippled;
						Rectangle [] rectsToErase = new Rectangle [rectangles.length];
						for (int i = 0; i < rectangles.length; i++) {
							Rectangle current = rectangles [i];
							rectsToErase [i] = new Rectangle (current.x, current.y, current.width, current.height);
						}
						event.x = newX;
						event.y = newY;
						if ((style & SWT.RESIZE) != 0) {
							resizeRectangles (newX - oldX, newY - oldY);
							sendEvent (SWT.Resize, event);
							/*
							* It is possible (but unlikely), that application
							* code could have disposed the widget in the move
							* event.  If this happens, return false to indicate
							* that the tracking has failed.
							*/
							if (isDisposed ()) {
								cancelled = true;
								break;
							}
							boolean draw = false;
							/*
							 * It is possible that application code could have
							 * changed the rectangles in the resize event.  If this
							 * happens then only redraw the tracker if the rectangle
							 * values have changed.
							 */
							if (rectangles != oldRectangles) {
								int length = rectangles.length;
								if (length != rectsToErase.length) {
									draw = true;
								} else {
									for (int i = 0; i < length; i++) {
										if (!rectangles [i].equals (rectsToErase [i])) {
											draw = true;
											break;
										}
									}
								}
							} else {
								draw = true;
							}
							if (draw) {
								drawRectangles (rectsToErase, oldStippled);
								update ();
								drawRectangles (rectangles, stippled);
							}
							cursorPos = adjustResizeCursor ();
							if (cursorPos != null) {
								newX = cursorPos.x;
								newY = cursorPos.y;
							}
						} else {
							moveRectangles (newX - oldX, newY - oldY);
							sendEvent (SWT.Move, event);
							/*
							* It is possible (but unlikely), that application
							* code could have disposed the widget in the move
							* event.  If this happens, return false to indicate
							* that the tracking has failed.
							*/
							if (isDisposed ()) {
								cancelled = true;
								break;
							}
							boolean draw = false;
							/*
							 * It is possible that application code could have
							 * changed the rectangles in the move event.  If this
							 * happens then only redraw the tracker if the rectangle
							 * values have changed.
							 */
							if (rectangles != oldRectangles) {
								int length = rectangles.length;
								if (length != rectsToErase.length) {
									draw = true;
								} else {
									for (int i = 0; i < length; i++) {
										if (!rectangles [i].equals (rectsToErase [i])) {
											draw = true;
											break;
										}
									}
								}
							} else {
								draw = true;
							}
							if (draw) {
								drawRectangles (rectsToErase, oldStippled);
								update ();
								drawRectangles (rectangles, stippled);
							}
						}
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
							Rectangle [] oldRectangles = rectangles;
							boolean oldStippled = stippled;
							Rectangle [] rectsToErase = new Rectangle [rectangles.length];
							for (int i = 0; i < rectangles.length; i++) {
								Rectangle current = rectangles [i];
								rectsToErase [i] = new Rectangle (current.x, current.y, current.width, current.height);
							}
							int newX = oldX + xChange;
							int newY = oldY + yChange;
							event.x = newX;
							event.y = newY;
							if ((style & SWT.RESIZE) != 0) {
								resizeRectangles (xChange, yChange);
								sendEvent (SWT.Resize, event);
								/*
								* It is possible (but unlikely) that application
								* code could have disposed the widget in the move
								* event.  If this happens return false to indicate
								* that the tracking has failed.
								*/
								if (isDisposed ()) {
									cancelled = true;
									break;
								}
								boolean draw = false;
								/*
								 * It is possible that application code could have
								 * changed the rectangles in the resize event.  If this
								 * happens then only redraw the tracker if the rectangle
								 * values have changed.
								 */
								if (rectangles != oldRectangles) {
									int length = rectangles.length;
									if (length != rectsToErase.length) {
										draw = true;
									} else {
										for (int i = 0; i < length; i++) {
											if (!rectangles [i].equals (rectsToErase [i])) {
												draw = true;
												break;
											}
										}
									}
								} else {
									draw = true;
								}
								if (draw) {
									drawRectangles (rectsToErase, oldStippled);
									update ();
									drawRectangles (rectangles, stippled);
								}
								cursorPos = adjustResizeCursor ();
							} else {
								moveRectangles (xChange, yChange);
								sendEvent (SWT.Move, event);
								/*
								* It is possible (but unlikely) that application
								* code could have disposed the widget in the move
								* event.  If this happens return false to indicate
								* that the tracking has failed.
								*/
								if (isDisposed ()) {
									cancelled = true;
									break;
								}
								boolean draw = false;
								/*
								 * It is possible that application code could have
								 * changed the rectangles in the move event.  If this
								 * happens then only redraw the tracker if the rectangle
								 * values have changed.
								 */
								if (rectangles != oldRectangles) {
									int length = rectangles.length;
									if (length != rectsToErase.length) {
										draw = true;
									} else {
										for (int i = 0; i < length; i++) {
											if (!rectangles [i].equals (rectsToErase [i])) {
												draw = true;
												break;
											}
										}
									}
								} else {
									draw = true;
								}
								if (draw) {
									drawRectangles (rectsToErase, oldStippled);
									update ();
									drawRectangles (rectangles, stippled);
								}
								cursorPos = adjustMoveCursor ();
							}
							if (cursorPos != null) {
								oldX = cursorPos.x;
								oldY = cursorPos.y;
							}
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
	OS.free (buffer);
	if (!isDisposed ()) {
		update ();
		drawRectangles (rectangles, stippled);
	}
	tracking = false;
	if (region != 0) OS.PtDestroyWidget (region);
	return !cancelled;
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
	rectangles = proportions = null;
	bounds = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Resize, listener);
	eventTable.unhook (SWT.Move, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard.
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
 * @see KeyListener
 * @see #addKeyListener
 */
public void removeKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.KeyUp, listener);
	eventTable.unhook (SWT.KeyDown, listener);
}

void resizeRectangles (int xChange, int yChange) {
	if (bounds == null) return;
	/*
	* If the cursor orientation has not been set in the orientation of
	* this change then try to set it here.
	*/
	if (xChange < 0 && ((style & SWT.LEFT) != 0) && ((cursorOrientation & SWT.RIGHT) == 0)) {
		cursorOrientation |= SWT.LEFT;
	}
	if (xChange > 0 && ((style & SWT.RIGHT) != 0) && ((cursorOrientation & SWT.LEFT) == 0)) {
		cursorOrientation |= SWT.RIGHT;
	}
	if (yChange < 0 && ((style & SWT.UP) != 0) && ((cursorOrientation & SWT.DOWN) == 0)) {
		cursorOrientation |= SWT.UP;
	}
	if (yChange > 0 && ((style & SWT.DOWN) != 0) && ((cursorOrientation & SWT.UP) == 0)) {
		cursorOrientation |= SWT.DOWN;
	}
	
	/*
	 * If the bounds will flip about the x or y axis then apply the adjustment
	 * up to the axis (ie.- where bounds width/height becomes 0), change the
	 * cursor's orientation accordingly, and flip each Rectangle's origin (only
	 * necessary for > 1 Rectangles) 
	 */
	if ((cursorOrientation & SWT.LEFT) != 0) {
		if (xChange > bounds.width) {
			if ((style & SWT.RIGHT) == 0) return;
			cursorOrientation |= SWT.RIGHT;
			cursorOrientation &= ~SWT.LEFT;
			bounds.x += bounds.width;
			xChange -= bounds.width;
			bounds.width = 0;
			if (proportions.length > 1) {
				for (int i = 0; i < proportions.length; i++) {
					Rectangle proportion = proportions [i];
					proportion.x = 100 - proportion.x - proportion.width;
				}
			}
		}
	} else if ((cursorOrientation & SWT.RIGHT) != 0) {
		if (bounds.width < -xChange) {
			if ((style & SWT.LEFT) == 0) return;
			cursorOrientation |= SWT.LEFT;
			cursorOrientation &= ~SWT.RIGHT;
			xChange += bounds.width;
			bounds.width = 0;
			if (proportions.length > 1) {
				for (int i = 0; i < proportions.length; i++) {
					Rectangle proportion = proportions [i];
					proportion.x = 100 - proportion.x - proportion.width;
				}
			}
		}
	}
	if ((cursorOrientation & SWT.UP) != 0) {
		if (yChange > bounds.height) {
			if ((style & SWT.DOWN) == 0) return;
			cursorOrientation |= SWT.DOWN;
			cursorOrientation &= ~SWT.UP;
			bounds.y += bounds.height;
			yChange -= bounds.height;
			bounds.height = 0;
			if (proportions.length > 1) {
				for (int i = 0; i < proportions.length; i++) {
					Rectangle proportion = proportions [i];
					proportion.y = 100 - proportion.y - proportion.height;
				}
			}
		}
	} else if ((cursorOrientation & SWT.DOWN) != 0) {
		if (bounds.height < -yChange) {
			if ((style & SWT.UP) == 0) return;
			cursorOrientation |= SWT.UP;
			cursorOrientation &= ~SWT.DOWN;
			yChange += bounds.height;
			bounds.height = 0;
			if (proportions.length > 1) {
				for (int i = 0; i < proportions.length; i++) {
					Rectangle proportion = proportions [i];
					proportion.y = 100 - proportion.y - proportion.height;
				}
			}
		}
	}
	
	// apply the bounds adjustment
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
	if (cursor != null && cursor.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	clientCursor = cursor;
}

void setCursor (int cursorHandle) {
	if (cursorHandle == 0) return;
	int type = 0;
	int bitmap = 0;
	if (clientCursor != null) {
		type = clientCursor.type;
		bitmap = clientCursor.bitmap;
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
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of rectangles is null or contains a null rectangle</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setRectangles (Rectangle [] rectangles) {
	checkWidget();
	if (rectangles == null) error (SWT.ERROR_NULL_ARGUMENT);
	this.rectangles = new Rectangle [rectangles.length];
	for (int i = 0; i < rectangles.length; i++) {
		Rectangle current = rectangles [i];
		if (current == null) error (SWT.ERROR_NULL_ARGUMENT);
		this.rectangles [i] = new Rectangle (current.x, current.y, current.width, current.height);
	}
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

void update () {
	if (parent != null) {
		if (parent.isDisposed ()) return;
		parent.getShell ().update ();
	} else {
		display.update ();
	}
}
}

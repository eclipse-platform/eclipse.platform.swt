/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CGPoint;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.Rect;

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
	Control parent;
	boolean tracking, stippled;
	Cursor clientCursor, resizeCursor;
	Rectangle [] rectangles, proportions;
	int cursorOrientation = SWT.NONE;
	boolean inEvent = false;
		
	/*
	* The following values mirror step sizes on Windows
	*/
	final static int STEPSIZE_SMALL = 1;
	final static int STEPSIZE_LARGE = 9;

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
public void addControlListener (ControlListener listener) {
	checkWidget ();
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
		Rect rect = new Rect ();
		OS.GetControlBounds (parent.handle, rect);
		newX += rect.left; 
		newY += rect.top; 
		int window = OS.GetControlOwner (parent.handle);
		OS.GetWindowBounds (window, (short) OS.kWindowContentRgn, rect);
		newX += rect.left; 
		newY += rect.top; 
	}
	CGPoint pt = new CGPoint ();
	pt.x = newX;  pt.y = newY;
	OS.CGWarpMouseCursorPosition (pt);
	return new Point ((int) pt.x, (int) pt.y);
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
		Rect rect = new Rect ();
		OS.GetControlBounds (parent.handle, rect);
		newX += rect.left; 
		newY += rect.top; 
		int window = OS.GetControlOwner (parent.handle);
		OS.GetWindowBounds (window, (short) OS.kWindowContentRgn, rect);
		newX += rect.left; 
		newY += rect.top; 
	}
	CGPoint pt = new CGPoint ();
	pt.x = newX;  pt.y = newY;
	OS.CGWarpMouseCursorPosition (pt);

	/*
	* If the client has not provided a custom cursor then determine
	* the appropriate resize cursor.
	*/
	if (clientCursor == null) {
		Cursor newCursor = null;
		switch (cursorOrientation) {
			case SWT.UP:
				newCursor = new Cursor(display, SWT.CURSOR_SIZENS);
				break;
			case SWT.DOWN:
				newCursor = new Cursor(display, SWT.CURSOR_SIZENS);
				break;
			case SWT.LEFT:
				newCursor = new Cursor(display, SWT.CURSOR_SIZEWE);
				break;
			case SWT.RIGHT:
				newCursor = new Cursor(display, SWT.CURSOR_SIZEWE);
				break;
			case SWT.LEFT | SWT.UP:
				newCursor = new Cursor(display, SWT.CURSOR_SIZENWSE);
				break;
			case SWT.RIGHT | SWT.DOWN:
				newCursor = new Cursor(display, SWT.CURSOR_SIZENWSE);
				break;
			case SWT.LEFT | SWT.DOWN:
				newCursor = new Cursor(display, SWT.CURSOR_SIZENESW);
				break;
			case SWT.RIGHT | SWT.UP:
				newCursor = new Cursor(display, SWT.CURSOR_SIZENESW);
				break;
			default:
				newCursor = new Cursor(display, SWT.CURSOR_SIZEALL);
				break;
		}
		display.setCursor (newCursor.handle);
		if (resizeCursor != null) {
			resizeCursor.dispose ();
		}
		resizeCursor = newCursor;
	}
		
	return new Point ((int) pt.x, (int) pt.y);
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
	checkWidget ();
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
		int x = 0, y = 0, width = 0, height = 0;
		if (bounds.width != 0) {
			x = (rects [i].x - bounds.x) * 100 / bounds.width;
			width = rects [i].width * 100 / bounds.width;
		}
		if (bounds.height != 0) {
			y = (rects [i].y - bounds.y) * 100 / bounds.height;
			height = rects [i].height * 100 / bounds.height;
		}
		result [i] = new Rectangle (x, y, width, height);			
	}
	return result;
}


void drawRectangles (int window, boolean erase) {
	if (parent != null) {
		if (parent.isDisposed ()) return;
		Shell shell = parent.getShell ();
		shell.update (true);
	} else {
		display.update ();
	}
	int[] context = new int [1];
	int port = OS.GetWindowPort (window);
	Rect portRect = new Rect ();
	OS.GetPortBounds (port, portRect);
	OS.QDBeginCGContext (port, context);
	OS.CGContextScaleCTM (context [0], 1, -1);
	OS.CGContextTranslateCTM (context [0], 0, portRect.top - portRect.bottom);
	CGRect cgRect = new CGRect ();
	for (int i=0; i<rectangles.length; i++) {
		Rectangle rect = rectangles [i];
		cgRect.x = rect.x;
		cgRect.y = rect.y;
		cgRect.width = rect.width;
		cgRect.height = rect.height;
		if (erase) {
			cgRect.width++;
			cgRect.height++;
			OS.CGContextClearRect (context [0], cgRect);
		} else {
			cgRect.x += 0.5f;
			cgRect.y += 0.5f;
			OS.CGContextStrokeRect (context [0], cgRect);
		}
	}
	OS.CGContextSynchronize (context [0]);
	OS.QDEndCGContext (port, context);
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
	checkWidget ();
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
	checkWidget ();
	return stippled;
}

void moveRectangles (int xChange, int yChange) {
	if (xChange < 0 && ((style & SWT.LEFT) == 0)) return;
	if (xChange > 0 && ((style & SWT.RIGHT) == 0)) return;
	if (yChange < 0 && ((style & SWT.UP) == 0)) return;
	if (yChange > 0 && ((style & SWT.DOWN) == 0)) return;
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
	checkWidget ();
	if (rectangles == null) return false;
	boolean cancelled = false;
	tracking = true;
	int window = display.createOverlayWindow ();
	OS.ShowWindow (window);
	drawRectangles (window, false);
	Point cursorPos;
	if (OS.StillDown ()) {
		org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
		OS.GetGlobalMouse (pt);
		cursorPos = new Point (pt.h, pt.v);
	} else {
		if ((style & SWT.RESIZE) != 0) {
			cursorPos = adjustResizeCursor ();
		} else {
			cursorPos = adjustMoveCursor ();
		}
	}
	
	int oldX = cursorPos.x, oldY = cursorPos.y;
	/*
	* Tracker behaves like a Dialog with its own OS event loop.
	*/
	Event event = new Event ();
	int [] outEvent  = new int [1];
	while (tracking && !cancelled) {
		int status = OS.ReceiveNextEvent (0, null, OS.kEventDurationNoWait, true, outEvent);
		if (status != OS.noErr) continue;
		int eventClass = OS.GetEventClass (outEvent [0]);
		int eventKind = OS.GetEventKind (outEvent [0]);
		int newX = oldX, newY = oldY;	
		switch (eventClass) {
			case OS.kEventClassMouse: {
				switch (eventKind) {
					case OS.kEventMouseUp:
					case OS.kEventMouseMoved:
					case OS.kEventMouseDragged:
						int sizeof = org.eclipse.swt.internal.carbon.Point.sizeof;
						org.eclipse.swt.internal.carbon.Point where = new org.eclipse.swt.internal.carbon.Point ();
						OS.GetEventParameter (outEvent [0], OS.kEventParamMouseLocation, OS.typeQDPoint, null, sizeof, null, where);
						newX = where.h;
						newY = where.v;	
						if (newX != oldX || newY != oldY) {
							drawRectangles (window, true);
							event.x = newX;
							event.y = newY;
							if ((style & SWT.RESIZE) != 0) {
							   resizeRectangles (newX - oldX, newY - oldY);
								cursorPos = adjustResizeCursor ();
								newX = cursorPos.x; newY = cursorPos.y;
								inEvent = true;
								sendEvent (SWT.Resize, event);
							} else {
								moveRectangles (newX - oldX, newY - oldY);
								inEvent = true;
								sendEvent (SWT.Move, event);
							}
							inEvent = false;
							/*
							* It is possible (but unlikely), that application
							* code could have disposed the widget in the move
							* event.  If this happens, return false to indicate
							* that the tracking has failed.
							*/
							if (isDisposed ()) return false;
							drawRectangles (window, false);
							oldX = newX;  oldY = newY;
						}
						tracking = eventKind != OS.kEventMouseUp;
						break;
				}
				break;
			}
			case OS.kEventClassKeyboard: {
				switch (eventKind) {
					case OS.kEventRawKeyDown:
					case OS.kEventRawKeyModifiersChanged:
					case OS.kEventRawKeyRepeat: {
						int [] keyCode = new int [1];
						OS.GetEventParameter (outEvent [0], OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
						int [] modifiers = new int [1];
						OS.GetEventParameter (outEvent [0], OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
						int stepSize = (modifiers [0] & OS.controlKey) != 0 ? STEPSIZE_SMALL : STEPSIZE_LARGE;
						int xChange = 0, yChange = 0;
						switch (keyCode [0]) {
							case 53: /* Esc */
								cancelled = true;
								tracking = false;
								break;
							case 36: /* Return */
								tracking = false;
								break;
							case 123: /* Left arrow */
								xChange = -stepSize;
								break;
							case 124: /* Right arrow */
								xChange = stepSize;
								break;
							case 126: /* Up arrow */
								yChange = -stepSize;
								break;
							case 125: /* Down arrow */
								yChange = stepSize;
								break;
						}
						if (xChange != 0 || yChange != 0) {
							drawRectangles (window, true);
							newX = oldX + xChange;
							newY = oldY + yChange;
							event.x = newX;
							event.y = newY;
							if ((style & SWT.RESIZE) != 0) {
								resizeRectangles (xChange, yChange);
								cursorPos = adjustResizeCursor ();
								inEvent = true;
								sendEvent (SWT.Resize, event);
							} else {
								moveRectangles (xChange, yChange);
								cursorPos = adjustMoveCursor ();
								inEvent = true;
								sendEvent (SWT.Move, event);
							}
							inEvent = false;
							/*
							* It is possible (but unlikely) that application
							* code could have disposed the widget in the move
							* event.  If this happens return false to indicate
							* that the tracking has failed.
							*/
							if (isDisposed ()) return false;
							drawRectangles (window, false);
							oldX = cursorPos.x;  oldY = cursorPos.y;
						}
						break;
					}
				}
			}
		}
		/*
		* Don't dispatch mouse and key events in general, EXCEPT once this
		* tracker has finished its work.
		*/
		boolean dispatch = true;
		if (tracking && !cancelled) {
			if (eventClass == OS.kEventClassMouse) dispatch = false;
			if (eventClass == OS.kEventClassKeyboard) dispatch = false;
		}
		if (dispatch) OS.SendEventToEventTarget (outEvent [0], OS.GetEventDispatcherTarget ());
		OS.ReleaseEvent (outEvent [0]);
		if (clientCursor != null && resizeCursor == null) {
			display.setCursor (clientCursor.handle);
		}
	}
	drawRectangles (window, true);
	OS.DisposeWindow (window);
	tracking = false;
	display.grabControl = null;
	return !cancelled;
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
	checkWidget ();
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
public void setCursor (Cursor newCursor) {
	checkWidget ();
	clientCursor = newCursor;
	if (newCursor != null) {
		if (inEvent) display.setCursor (newCursor.handle);
	}
}

/**
 * Specifies the rectangles that should be drawn, expressed relative to the parent
 * widget.  If the parent is a Display then these are screen coordinates.
 *
 * @param rectangles the bounds of the rectangles to be drawn
 *
  * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of rectangles is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setRectangles (Rectangle [] rectangles) {
	checkWidget ();
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
	checkWidget ();
	this.stippled = stippled;
}
}

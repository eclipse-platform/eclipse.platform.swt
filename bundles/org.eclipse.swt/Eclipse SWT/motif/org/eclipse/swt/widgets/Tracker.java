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

 
import org.eclipse.swt.internal.motif.*;
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
	boolean tracking, cancelled, stippled;
	Rectangle [] rectangles = new Rectangle [0], proportions = rectangles;
	Rectangle bounds;
	int cursorOrientation = SWT.NONE;
	Cursor cursor;
	int window, oldX, oldY;

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
public void addKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.KeyUp,typedListener);
	addListener(SWT.KeyDown,typedListener);
}

Point adjustMoveCursor () {
	if (bounds == null) return null;
	final int unused[] = new int[1];
	int actualX[] = new int[1];
	int actualY[] = new int[1];
	
	int newX = bounds.x + bounds.width / 2;
	int newY = bounds.y;
	
	int xDisplay = display.xDisplay;
	OS.XWarpPointer (xDisplay, OS.None, window, 0, 0, 0, 0, newX, newY);
	/*
	 * The call to XWarpPointer does not always place the pointer on the
	 * exact location that is specified, so do a query (below) to get the
	 * actual location of the pointer after it has been moved.
	 */
	OS.XQueryPointer (xDisplay, window, unused, unused, actualX, actualY, unused, unused, unused);
	return new Point (actualX[0], actualY[0]);
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

	final int unused[] = new int[1];
	int actualX[] = new int[1];
	int actualY[] = new int[1];
	int xDisplay = display.xDisplay;
	OS.XWarpPointer (xDisplay, 0, window, 0, 0, 0, 0, newX, newY);
	/*
	 * The call to XWarpPointer does not always place the pointer on the
	 * exact location that is specified, so do a query (below) to get the
	 * actual location of the pointer after it has been moved.
	 */
	OS.XQueryPointer (xDisplay, window, unused, unused, actualX, actualY, unused, unused, unused);
	return new Point (actualX[0], actualY[0]);
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
	int xDisplay = display.xDisplay;
	int color = OS.XWhitePixel (xDisplay, 0);
	int xWindow = OS.XDefaultRootWindow (xDisplay);
	if (parent != null) {
		xWindow = OS.XtWindow (parent.handle);
		if (xWindow == 0) return;
		int foreground = parent.getForegroundPixel ();
		Control control = parent.findBackgroundControl ();
		if (control == null) control = parent;
		int background = control.getBackgroundPixel ();
		color = foreground ^ background;
	}
	int gc = OS.XCreateGC (xDisplay, xWindow, 0, null);
	OS.XSetForeground (xDisplay, gc, color);
	OS.XSetSubwindowMode (xDisplay, gc, OS.IncludeInferiors);
	OS.XSetFunction (xDisplay, gc, OS.GXxor);
	int stipplePixmap = 0;
	if (stippled) {
		byte [] bits = {-86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0};
		stipplePixmap = OS.XCreateBitmapFromData (xDisplay, xWindow, bits, 8, 8);
		OS.XSetStipple (xDisplay, gc, stipplePixmap);
		OS.XSetFillStyle (xDisplay, gc, OS.FillStippled);
		OS.XSetLineAttributes (xDisplay, gc, 3, OS.LineSolid, OS.CapButt, OS.JoinMiter);
	}
	for (int i=0; i<rects.length; i++) {
		Rectangle rect = rects [i];
		OS.XDrawRectangle (xDisplay, xWindow, gc, rect.x, rect.y, rect.width, rect.height);
	}
	if (stippled) {
		OS.XFreePixmap (xDisplay, stipplePixmap);
	}
	OS.XFreeGC (xDisplay, gc);
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
	checkWidget ();
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
	checkWidget ();
	int xDisplay = display.xDisplay;
	window = OS.XDefaultRootWindow (xDisplay);
	if (parent != null) {
		window = OS.XtWindow (parent.handle);
		if (window == 0) return false;
	}
	cancelled = false;
	tracking = true;
	update ();
	drawRectangles (rectangles, stippled);
	int [] oldX = new int [1], oldY = new int [1];
	int [] unused = new int [1], mask = new int [1];
	OS.XQueryPointer (xDisplay, window, unused, unused, oldX, oldY, unused, unused, mask);
	
	/*
	* If exactly one of UP/DOWN is specified as a style then set the cursor
	* orientation accordingly (the same is done for LEFT/RIGHT styles below).
	*/
	int vStyle = style & (SWT.UP | SWT.DOWN);
	if (vStyle == SWT.UP || vStyle == SWT.DOWN) {
		cursorOrientation |= vStyle;
	}
	int hStyle = style & (SWT.LEFT | SWT.RIGHT);
	if (hStyle == SWT.LEFT || hStyle == SWT.RIGHT) {
		cursorOrientation |= hStyle;
	}
	
	int mouseMasks = OS.Button1Mask | OS.Button2Mask | OS.Button3Mask;
	boolean mouseDown = (mask [0] & mouseMasks) != 0;
	if (!mouseDown) {
		Point cursorPos = null;
		if ((style & SWT.RESIZE) != 0) {
			cursorPos = adjustResizeCursor ();
		} else {
			cursorPos = adjustMoveCursor ();
		}
		if (cursorPos != null) {
			oldX [0] = cursorPos.x;
			oldY [0] = cursorPos.y;
		}
	}
	this.oldX = oldX [0];
	this.oldY = oldY [0];

	int ptrGrabResult = OS.XGrabPointer (xDisplay, window, 0,
		OS.ButtonPressMask | OS.ButtonReleaseMask | OS.PointerMotionMask,
		OS.GrabModeAsync, OS.GrabModeAsync, OS.None, OS.None, OS.CurrentTime);
	int kbdGrabResult = OS.XGrabKeyboard (xDisplay, window, 0,
		OS.GrabModeAsync, OS.GrabModeAsync, OS.CurrentTime);
				
	/* Tracker behaves like a Dialog with its own OS event loop. */
	XAnyEvent anyEvent = new XAnyEvent();
	int xEvent = OS.XtMalloc (XEvent.sizeof);
	int dispatch = OS.XtMalloc (4);
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	while (tracking) {
		if (parent != null && parent.isDisposed ()) break;
		OS.XtAppNextEvent (xtContext, xEvent);
		OS.memmove (anyEvent, xEvent, XAnyEvent.sizeof);
		int widget = OS.XtWindowToWidget (anyEvent.display, anyEvent.window);
		switch (anyEvent.type) {
			case OS.MotionNotify: XPointerMotion (widget, 0, xEvent, dispatch); break;
			case OS.ButtonRelease: XButtonRelease (widget, 0, xEvent, dispatch); break;
			case OS.KeyPress: XKeyPress (widget, 0, xEvent, dispatch); break;
			case OS.KeyRelease: XKeyRelease (widget, 0, xEvent, dispatch); break;
			case OS.ButtonPress:
			case OS.EnterNotify:
			case OS.LeaveNotify:
				/* Do not dispatch these */
				break;
			case OS.Expose:
				update ();
				drawRectangles (rectangles, stippled);
				OS.XtDispatchEvent (xEvent);
				drawRectangles (rectangles, stippled);
				break;
			default:
				OS.XtDispatchEvent (xEvent);
		}
	}
	if (xEvent != 0) OS.XtFree (xEvent);
	if (dispatch != 0) OS.XtFree (dispatch);
	if (!isDisposed()) {
		update ();
		drawRectangles (rectangles, stippled);
	}
	if (ptrGrabResult == OS.GrabSuccess) OS.XUngrabPointer (xDisplay, OS.CurrentTime);
	if (kbdGrabResult == OS.GrabSuccess) OS.XUngrabKeyboard (xDisplay, OS.CurrentTime);
	window = 0;
	return !cancelled;
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
	checkWidget ();
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
	eventTable.unhook(SWT.KeyUp, listener);
	eventTable.unhook(SWT.KeyDown, listener);
}
void releaseWidget () {
	super.releaseWidget ();
	parent = null;
	rectangles = proportions = null;
	bounds = null;
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
public void setCursor (Cursor value) {
	checkWidget ();
	cursor = value;
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
	checkWidget ();
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
	checkWidget ();
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
int XButtonRelease (int w, int client_data, int call_data, int continue_to_dispatch) {
	return xMouse (OS.ButtonRelease, w, client_data, call_data, continue_to_dispatch);
}

int XKeyPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XKeyPress (w, client_data, call_data, continue_to_dispatch);
	if (result != 0) return result;
	XKeyEvent keyEvent = new XKeyEvent ();
	OS.memmove (keyEvent, call_data, XKeyEvent.sizeof);
	if (keyEvent.keycode != 0) {
		int [] keysym = new int [1];
		OS.XLookupString (keyEvent, null, 0, keysym, null);
		keysym [0] &= 0xFFFF;
		int xChange = 0, yChange = 0;
		int stepSize = ((keyEvent.state & OS.ControlMask) != 0) ? STEPSIZE_SMALL : STEPSIZE_LARGE;
		switch (keysym [0]) {
			case OS.XK_KP_Enter:
			case OS.XK_Return:
				tracking = false;
				/* Eat the subsequent KeyRelease event */
				OS.XtAppNextEvent (OS.XtDisplayToApplicationContext (keyEvent.display), call_data);
				break;
			case OS.XK_Escape:
				tracking = false;
				cancelled = true;
				/* Eat the subsequent KeyRelease event */
				OS.XtAppNextEvent (OS.XtDisplayToApplicationContext (keyEvent.display), call_data);
				break;
			case OS.XK_Left:
				xChange = -stepSize;
				break;
			case OS.XK_Right:
				xChange = stepSize;
				break;
			case OS.XK_Up:
				yChange = -stepSize;
				break;
			case OS.XK_Down:
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
			Event event = new Event ();
			event.x = oldX + xChange;
			event.y = oldY + yChange;
			Point cursorPos;
			if ((style & SWT.RESIZE) != 0) {
				resizeRectangles (xChange, yChange);
				sendEvent (SWT.Resize, event);
				/*
				 * It is possible (but unlikely) that application code
				 * could have disposed the widget in the resize event.
				 * If this happens then return false to indicate that
				 * the move failed.
				 */
				if (isDisposed ()) {
					cancelled = true;
					return 1;
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
				 * It is possible (but unlikely) that application code
				 * could have disposed the widget in the move event.
				 * If this happens then return false to indicate that
				 * the move failed.
				 */
				if (isDisposed ()) {
					cancelled = true;
					return 1;
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
	return result;
}

int XPointerMotion (int w, int client_data, int call_data, int continue_to_dispatch) {
	if (cursor != null) {
		int xDisplay = display.xDisplay;
		OS.XChangeActivePointerGrab (xDisplay,
			OS.ButtonPressMask | OS.ButtonReleaseMask | OS.PointerMotionMask,
			cursor.handle, OS.CurrentTime);
	}
	return xMouse (OS.MotionNotify, w, client_data, call_data, continue_to_dispatch);
}

int xMouse (int type, int w, int client_data, int call_data, int continue_to_dispatch) {
	int xDisplay = display.xDisplay;
	int [] newX = new int [1], newY = new int [1], unused = new int [1];
	OS.XQueryPointer (xDisplay, window, unused, unused, newX, newY, unused, unused, unused);
	if (oldX != newX [0] || oldY != newY [0]) {
		Rectangle [] oldRectangles = rectangles;
		boolean oldStippled = stippled;
		Rectangle [] rectsToErase = new Rectangle [rectangles.length];
		for (int i = 0; i < rectangles.length; i++) {
			Rectangle current = rectangles [i];
			rectsToErase [i] = new Rectangle (current.x, current.y, current.width, current.height);
		}
		Event event = new Event ();
		event.x = newX [0];
		event.y = newY [0];
		if ((style & SWT.RESIZE) != 0) {
			resizeRectangles (newX [0] - oldX, newY [0] - oldY);
			sendEvent (SWT.Resize, event);
			/*
			 * It is possible (but unlikely) that application code
			 * could have disposed the widget in the resize event.
			 * If this happens then return false to indicate that
			 * the move failed.
			 */
			if (isDisposed ()) {
				cancelled = true;
				return 1;
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
			Point cursorPos = adjustResizeCursor ();
			if (cursorPos != null) {
				newX [0] = cursorPos.x;
				newY [0] = cursorPos.y;
			}
		} else {
			moveRectangles (newX [0] - oldX, newY [0] - oldY);
			sendEvent (SWT.Move, event);
			/*
			 * It is possible (but unlikely) that application code
			 * could have disposed the widget in the move event.
			 * If this happens then return false to indicate that
			 * the move failed.
			 */
			if (isDisposed ()) {
				cancelled = true;
				return 1;
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
		oldX = newX [0];
		oldY = newY [0];
	}
	tracking = type != OS.ButtonRelease;
	return 0;
}

}

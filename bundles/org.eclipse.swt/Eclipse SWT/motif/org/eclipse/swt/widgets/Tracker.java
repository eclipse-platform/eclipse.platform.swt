package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

/**
 *  Instances of this class implement rubber banding rectangles.
 *  
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Move</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Tracker extends Widget {
	Composite parent;
	Display display;
	boolean tracking, stippled;
	Rectangle [] rectangles, proportions;
	int cursorOrientation = SWT.NONE;
	int cursor;
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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Tracker (Composite parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	display = parent.getDisplay ();
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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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

Point adjustMoveCursor (int xDisplay, int xWindow) {
	final int unused[] = new int[1];
	int actualX[] = new int[1];
	int actualY[] = new int[1];
	
	Rectangle bounds = computeBounds ();
	int newX = bounds.x + bounds.width / 2;
	int newY = bounds.y;
	
	OS.XWarpPointer (xDisplay, OS.None, xWindow, 0, 0, 0, 0, newX, newY);
	/*
	 * The call to XWarpPointer does not always place the pointer on the
	 * exact location that is specified, so do a query (below) to get the
	 * actual location of the pointer after it has been moved.
	 */
	OS.XQueryPointer (xDisplay, xWindow, unused, unused, actualX, actualY, unused, unused, unused);
	return new Point (actualX[0], actualY[0]);
}
Point adjustResizeCursor (int xDisplay, int xWindow) {
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

	final int unused[] = new int[1];
	int actualX[] = new int[1];
	int actualY[] = new int[1];
	OS.XWarpPointer (xDisplay, SWT.NONE, xWindow, 0, 0, 0, 0, newX, newY);
	/*
	 * The call to XWarpPointer does not always place the pointer on the
	 * exact location that is specified, so do a query (below) to get the
	 * actual location of the pointer after it has been moved.
	 */
	OS.XQueryPointer (xDisplay, xWindow, unused, unused, actualX, actualY, unused, unused, unused);
	return new Point (actualX[0], actualY[0]);
}
static int checkStyle (int style) {
	if ((style & (SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN)) == 0) {
		style |= SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN;
	}
	return style;
}
/**
 * Stop displaying the tracker rectangles.
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
		result[i] = new Rectangle (
			(rects[i].x - bounds.x) * 100 / bounds.width,
			(rects[i].y - bounds.y) * 100 / bounds.height,
			rects[i].width * 100 / bounds.width,
			rects[i].height * 100 / bounds.height);
	}
	return result;
}

void drawRectangles () {
	if (parent != null) {
		if (parent.isDisposed ()) return;
		parent.getShell ().update ();
	} else {
		display.update ();
	}
	int xDisplay = display.xDisplay;
	int color = OS.XWhitePixel (xDisplay, 0);
	int xWindow = OS.XDefaultRootWindow (xDisplay);
	if (parent != null) {
		xWindow = OS.XtWindow (parent.handle);
		if (xWindow == 0) return;
		int [] argList = {OS.XmNforeground, 0, OS.XmNbackground, 0};
		OS.XtGetValues (parent.handle, argList, argList.length / 2);
		color = argList [1] ^ argList [3];
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
	for (int i=0; i<rectangles.length; i++) {
		Rectangle rect = rectangles [i];
		OS.XDrawRectangle (xDisplay, xWindow, gc, rect.x, rect.y, rect.width, rect.height);
	}
	if (stippled) {
		OS.XFreePixmap (xDisplay, stipplePixmap);
	}
	OS.XFreeGC (xDisplay, gc);
}
public Display getDisplay () {
	return display;
}
/**
 * Returns the bounds of the Rectangles being drawn.
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
 * Start displaying the Tracker rectangles.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean open () {
	checkWidget ();
	if (rectangles == null) return false;
	int xDisplay = display.xDisplay;
	int color = OS.XWhitePixel (xDisplay, 0);
	int xWindow = OS.XDefaultRootWindow (xDisplay);
	if (parent != null) {
		xWindow = OS.XtWindow (parent.handle);
		if (xWindow == 0) return false;
	}
	boolean cancelled = false;
	tracking = true;
	drawRectangles ();
	int [] oldX = new int [1], oldY = new int [1];
	int [] unused = new int [1];
	Point cursorPos;
	if ((style & SWT.MENU) != 0) {
		if ((style & SWT.RESIZE) != 0) {
			cursorPos = adjustResizeCursor (xDisplay, xWindow);
		} else {
			cursorPos = adjustMoveCursor (xDisplay, xWindow);
		}
		oldX [0] = cursorPos.x;  oldY [0] = cursorPos.y;
	} else {
		OS.XQueryPointer (xDisplay, xWindow, unused, unused, oldX, oldY, unused, unused, unused);
	}
		
	XAnyEvent xEvent = new XAnyEvent ();
	int [] newX = new int [1], newY = new int [1];
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	/*
	 *  Tracker behaves like a Dialog with its own OS event loop.
	 */
	while (tracking) {
		if (parent != null && parent.isDisposed ()) break;
		OS.XtAppNextEvent (xtContext, xEvent);
		switch (xEvent.type) {
			case OS.MotionNotify:
				if (cursor != 0) {
					OS.XChangeActivePointerGrab (
						xDisplay,
						OS.ButtonPressMask | OS.ButtonReleaseMask | OS.PointerMotionMask,
						cursor,
						OS.CurrentTime);
				}
				// fall through
			case OS.ButtonRelease:
				OS.XQueryPointer (xDisplay, xWindow, unused, unused, newX, newY, unused, unused, unused);
				if (oldX [0] != newX [0] || oldY [0] != newY [0]) {
					drawRectangles ();
					Event event = new Event ();
					event.x = newX [0];
					event.y = newY [0];
					if ((style & SWT.RESIZE) != 0) {
						resizeRectangles (newX [0] - oldX [0], newY [0] - oldY [0]);
						sendEvent (SWT.Resize, event);
						cursorPos = adjustResizeCursor (xDisplay, xWindow);
						newX [0] = cursorPos.x; newY [0] = cursorPos.y;
					} else {
						moveRectangles (newX [0] - oldX [0], newY [0] - oldY [0]);
						sendEvent (SWT.Move, event);
					}
					/*
					 * It is possible (but unlikely) that application code
					 * could have disposed the widget in the move event.
					 * If this happens then return false to indicate that
					 * the move failed.
					 */
					if (isDisposed ()) return false;
					drawRectangles ();
					oldX [0] = newX [0];  oldY [0] = newY [0];
				}
				tracking = xEvent.type != OS.ButtonRelease;
				break;
			case OS.KeyPress:
				XKeyEvent keyEvent = new XKeyEvent ();
				OS.memmove (keyEvent, xEvent, XKeyEvent.sizeof);
				if (keyEvent.keycode != 0) {
					int [] keysym = new int [1];
					OS.XLookupString (keyEvent, null, 0, keysym, null);
					keysym [0] &= 0xFFFF;
					int xChange = 0, yChange = 0;
					int stepSize = ((keyEvent.state & OS.ControlMask) != 0) ? STEPSIZE_SMALL : STEPSIZE_LARGE;
					switch (keysym [0]) {
						case OS.XK_Return:
							tracking = false;
							/*
							 * Eat the subsequent KeyRelease event
							 */
							OS.XtAppNextEvent (xtContext, xEvent);
							break;
						case OS.XK_Escape:
						case OS.XK_Cancel:
							tracking = false;
							cancelled = true;
							/*
							 * Eat the subsequent KeyRelease event
							 */
							OS.XtAppNextEvent (xtContext, xEvent);
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
						drawRectangles ();
						Event event = new Event ();
						event.x = oldX[0] + xChange;
						event.y = oldY[0] + yChange;
						if ((style & SWT.RESIZE) != 0) {
							resizeRectangles (xChange, yChange);
							sendEvent (SWT.Resize, event);
							cursorPos = adjustResizeCursor (xDisplay, xWindow);
						} else {
							moveRectangles (xChange, yChange);
							sendEvent (SWT.Move, event);
							cursorPos = adjustMoveCursor (xDisplay, xWindow);
						}
						/*
						 * It is possible (but unlikely) that application code
						 * could have disposed the widget in the move event.
						 * If this happens then return false to indicate that
						 * the move failed.
						 */
						if (isDisposed ()) return false;
						drawRectangles ();
						oldX[0] = cursorPos.x;  oldY[0] = cursorPos.y;
					}
				}
				break;
			case OS.EnterNotify:
			case OS.LeaveNotify:
				/*
				 * Do not dispatch these
				 */
				break;
			default:
				OS.XtDispatchEvent (xEvent);
		}
	}
	drawRectangles ();
	tracking = false;
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

public void setCursor (Cursor value) {
	checkWidget ();
	cursor = 0;
	if (value != null) cursor = value.handle;
}
/**
 * Specify the rectangles that should be drawn.
 *
 * @param rectangles the bounds of the rectangles to be drawn
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setRectangles (Rectangle [] rectangles) {
	checkWidget ();
	this.rectangles = rectangles;
	proportions = computeProportions (rectangles);
}
/**
 * Change the appearance of the line used to draw the rectangles.
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

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
public /*final*/ class Tracker extends Widget {
	Composite parent;
	Display display;
	boolean tracking, stippled;
	Rectangle [] rectangles;

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
	super (parent, style);
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
	this.style = style;
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
public void addControlListener(ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Move,typedListener);
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
	checkWidget();
	tracking = false;
}
Rectangle computeBounds() {
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

void moveRectangles(int xChange, int yChange) {
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
	checkWidget();
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
	XAnyEvent xEvent = new XAnyEvent ();
	int [] unused = new int [1];
	int [] newX = new int [1], newY = new int [1], oldX = new int [1], oldY = new int [1];
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	OS.XQueryPointer (xDisplay, xWindow, unused, unused, oldX, oldY, unused, unused, unused);
	/*
	 *  Tracker behaves like a Dialog with its own OS event loop.
	 */
	while (tracking) {
		if (parent != null && parent.isDisposed ()) break;
		OS.XtAppNextEvent (xtContext, xEvent);
		switch (xEvent.type) {
			case OS.ButtonRelease:
			case OS.MotionNotify:
				OS.XQueryPointer (xDisplay, xWindow, unused, unused, newX, newY, unused, unused, unused);
				if (oldX [0] != newX [0] || oldY [0] != newY [0]) {
					drawRectangles ();
					if ((style & SWT.RESIZE) != 0) {
						resizeRectangles(newX [0] - oldX [0], newY [0] - oldY [0]);
					} else {
						moveRectangles(newX [0] - oldX [0], newY [0] - oldY [0]);
					}
					Event event = new Event();
					event.x = newX [0];
					event.y = newY [0];
					if ((style & SWT.RESIZE) != 0) {
						sendEvent (SWT.Resize, event);
					} else {
						sendEvent (SWT.Move, event);
					}

					/*
					 * It is possible (but unlikely) that application code
					 * could have disposed the widget in the move event.
					 * If this happens then return false to indicate that
					 * the move failed.
					 */
					if (isDisposed()) return false;
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
					int stepSize = ((keyEvent.state & OS.ControlMask) != 0) ? 2 : 10;
					switch (keysym [0]) {
						case OS.XK_Return:
							tracking = false;
							break;
						case OS.XK_Escape:
							tracking = false;
							cancelled = true;
							break;
						case OS.XK_Cancel:
							tracking = false;
							cancelled = true;
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
						if ((style & SWT.RESIZE) != 0) {
							resizeRectangles(xChange, yChange);
						} else {
							moveRectangles(xChange, yChange);
						}
						Event event = new Event();
						event.x = oldX[0] + xChange;
						event.y = oldY[0] + yChange;
						if ((style & SWT.RESIZE) != 0) {
							sendEvent (SWT.Resize,event);
						} else {
							sendEvent (SWT.Move,event);
						}
						/*
						 * It is possible (but unlikely) that application code
						 * could have disposed the widget in the move event.
						 * If this happens then return false to indicate that
						 * the move failed.
						 */
						if (isDisposed()) return false;
						drawRectangles ();
						Rectangle bounds = computeBounds();
						newX [0] = bounds.x + bounds.width / 2;
						if ((style & SWT.RESIZE) != 0) {
							newY [0] = bounds.y + bounds.height / 2;
						} else {
							newY [0] = bounds.y;
						}
						OS.XWarpPointer (xDisplay, SWT.NONE, xWindow, 0, 0, 0, 0, newX [0], newY [0]);
						/*
						 * The call to XWarpPointer does not always place the pointer on the
						 * exact location that is specified, so do a query (below) to get the
						 * actual location of the pointer after it has been moved.
						 */
						OS.XQueryPointer (xDisplay, xWindow, unused, unused, newX, newY, unused, unused, unused);
						oldX[0] = newX[0];  oldY[0] = newY[0];
					}
				}
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
}
void resizeRectangles(int xChange, int yChange) {
	Rectangle bounds = computeBounds();
	for (int i = 0; i < rectangles.length; i++) {
		rectangles [i].width += rectangles [i].width * xChange / Math.max (1, bounds.width);
		rectangles [i].height += rectangles [i].height * yChange / Math.max (1, bounds.height);
	}
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
	checkWidget();
	this.rectangles = rectangles;
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
	checkWidget();
	this.stippled = stippled;
}
}

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
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
	int cursor, lastCursor;
	boolean tracking, stippled;
	Rectangle [] rectangles = new Rectangle [0];
	int xWindow;
	

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
	super (parent, checkStyle(style));
	this.parent = parent;
	display = parent.getDisplay ();
	xWindow = calculateWindow();
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


/*
 *   ===  ADD / REMOVE LISTENERS  ===
 */

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




/*
 *   ===  PUBLIC ACCESSORS  ===
 */

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



/*
 *   ===  PUBLIC FUNCTIONALITY  ===
 */

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
	boolean cancelled=false;
	tracking = true;
	drawRectangles ();

	int[] newX = new int[1];
	int[] newY = new int[1];
	int[] oldX = new int[1];
	int[] oldY = new int[1];
	OS.gdk_window_get_pointer(xWindow, oldX,oldY, 0);
	
	int ptrGrabResult = OS.gdk_pointer_grab(xWindow,
	                                        true,
	                                        OS.GDK_POINTER_MOTION_MASK | OS.GDK_BUTTON_RELEASE_MASK,
	                                        xWindow,
	                                        cursor,
	                                        OS.GDK_CURRENT_TIME());
	lastCursor = cursor;

	/*
	 *  Tracker behaves like a Dialog with its own OS event loop.
	 */
	while (tracking) {
		if (parent != null && parent.isDisposed ()) break;
		int eventType = waitEvent();
		switch (eventType) {
			case OS.GDK_BUTTON_RELEASE:
			case OS.GDK_MOTION_NOTIFY:
				if (cursor != lastCursor) {}
				OS.gdk_window_get_pointer(xWindow, newX,newY, 0);
				if (oldX [0] != newX [0] || oldY [0] != newY [0]) {
					drawRectangles ();
					for (int i=0; i<rectangles.length; i++) {
						rectangles [i].x += newX [0] - oldX [0];
						rectangles [i].y += newY [0] - oldY [0];
					}
					Event event = new Event();
					event.x = newX[0];
					event.y = newY[0];
					sendEvent (SWT.Move,event);
					drawRectangles ();
					oldX [0] = newX [0];  oldY [0] = newY [0];
				}
				tracking = (eventType != OS.GDK_BUTTON_RELEASE);
				break;
			case OS.GDK_KEY_PRESS:
//				error(SWT.ERROR_NOT_IMPLEMENTED);
				/*
				XKeyEvent keyEvent = new XKeyEvent ();
				OS.memmove (keyEvent, xEvent, XKeyEvent.sizeof);
				if (keyEvent.keycode != 0) {
					int [] keysym = new int [1];
					OS.XLookupString (keyEvent, null, 0, keysym, null);
					keysym [0] &= 0xFFFF;
					tracking = keysym [0] != OS.XK_Escape && keysym [0] != OS.XK_Cancel;
					cancelled = !tracking;
				}*/
				break;
			}  // switch
		}  // while
	drawRectangles();
	tracking = false;
	if (ptrGrabResult == OS.GDK_GRAB_SUCCESS)
		OS.gdk_pointer_ungrab(OS.GDK_CURRENT_TIME());
	return !cancelled;
}



private void drawRectangles () {
	int xWindow = calculateWindow();
	if (parent != null) {
		if (parent.isDisposed ()) return;
		parent.getShell ().update ();
	} else {
		display.update ();
	}
	
	int gc = OS.gdk_gc_new(xWindow);
	if (gc==0) error(SWT.ERROR_UNSPECIFIED);

	/* White foreground */
	int colormap = OS.gdk_colormap_get_system();
	GdkColor color = new GdkColor();
	OS.gdk_color_white(colormap, color);
	OS.gdk_gc_set_foreground(gc, color);

	/* Draw on top of inferior widgets */
	OS.gdk_gc_set_subwindow(gc, OS.GDK_INCLUDE_INFERIORS);
	
	/* XOR */
	OS.gdk_gc_set_function(gc, OS.GDK_XOR);
	
	for (int i=0; i<rectangles.length; i++) {
		Rectangle rect = rectangles [i];
		OS.gdk_draw_rectangle(xWindow, gc, 0, rect.x, rect.y, rect.width, rect.height);
	}
	OS.g_object_unref(gc);
}
/*
 * Wait for an event to show up.
 * Return the event's type as a GdkEventType.
 */
private int waitEvent() {
	int[] eventType = new int[1];
	int eventPtr;

	while (true) {
		eventPtr = OS.gdk_event_get();
		if (eventPtr != 0) {
			// hack, must implement memmove properly
			// GdkEvent event = new GdkEvent(eventPtr);
			OS.memmove(eventType, eventPtr, 4);
			OS.gdk_event_free(eventPtr);
			return eventType[0];
		}
		else {
			try { Thread.sleep(50); } catch (Exception ex) {}
		}
	}
}

/*
 * Figure which GdkWindow we'll draw on.
 * That's normally the root X window, or the parent's GdkWindow if we have a parent.
 */
private int calculateWindow() {
	int answer;
	if (parent == null) answer = OS.GDK_ROOT_PARENT();
		else answer = OS.GTK_WIDGET_WINDOW(parent.paintHandle());
	if (answer==0) error(SWT.ERROR_UNSPECIFIED);
	return answer;
}

public void setCursor (Cursor value) {
	checkWidget ();
	cursor = 0;
	if (value != null) cursor = value.handle;
}
static int checkStyle (int style) {
	if ((style & (SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN)) == 0) {
		style |= SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN;
	}
	return style;
}
}

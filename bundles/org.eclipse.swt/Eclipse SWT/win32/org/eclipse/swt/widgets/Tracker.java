package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.win32.*;
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
	Control parent;
	Display display;
	boolean tracking, stippled;
	Rectangle [] rectangles = new Rectangle [0];

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
	checkWidget ();
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
	checkWidget ();
	tracking = false;
}

/**
 * Draw the rectangles displayed by the tracker.
 */
void drawRectangles () {
	if (parent != null) {
		if (parent.isDisposed ()) return;
		parent.getShell ().update ();
	} else {
		display.update ();
	}
	int bandWidth = 1;
	int hwndTrack = OS.GetDesktopWindow ();
	if (parent != null) hwndTrack = parent.handle;
	int hDC = OS.GetDCEx (hwndTrack, 0, OS.DCX_CACHE);
	int hBitmap = 0, hBrush = 0, oldBrush = 0;
	if (stippled) {
		bandWidth = 3;
		byte [] bits = {-86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0};
		hBitmap = OS.CreateBitmap (8, 8, 1, 1, bits);
		hBrush = OS.CreatePatternBrush (hBitmap);
		oldBrush = OS.SelectObject (hDC, hBrush);
	}
	for (int i=0; i<rectangles.length; i++) {
		Rectangle rect = rectangles [i];
		OS.PatBlt (hDC, rect.x, rect.y, rect.width, bandWidth, OS.PATINVERT);
		OS.PatBlt (hDC, rect.x, rect.y + bandWidth, bandWidth, rect.height - (bandWidth * 2), OS.PATINVERT);
		OS.PatBlt (hDC, rect.x + rect.width - bandWidth, rect.y + bandWidth, bandWidth, rect.height - (bandWidth * 2), OS.PATINVERT);
		OS.PatBlt (hDC, rect.x, rect.y + rect.height - bandWidth, rect.width, bandWidth, OS.PATINVERT);
	}
	if (stippled) {
		OS.SelectObject (hDC, oldBrush);
		OS.DeleteObject (hBrush);
		OS.DeleteObject (hBitmap);
	}
	OS.ReleaseDC (hwndTrack, hDC);
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
	boolean cancelled = false;
	tracking = true;
	Event event = new Event ();
	POINT pt = new POINT ();
	MSG msg = new MSG ();
	drawRectangles ();
	int oldPos = OS.GetMessagePos ();
	// Tracker behaves like a Dialog with its own OS event loop.
	while (tracking && !cancelled) {
		if (parent != null && parent.isDisposed ()) break;
		OS.GetMessage (msg, 0, 0, 0);
		switch (msg.message) {
			case OS.WM_LBUTTONUP:
			case OS.WM_MOUSEMOVE:
				int newPos = OS.GetMessagePos ();
				if (newPos != oldPos) {
					int oldX = (short) (oldPos & 0xFFFF);
					int oldY = (short) (oldPos >> 16);
					int newX = (short) (newPos & 0xFFFF);
					int newY = (short) (newPos >> 16);	
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
					event.x = newX;
					event.y = newY;
					sendEvent (SWT.Move, event);
					if (isDisposed ()) return false;
					drawRectangles ();
					oldPos = newPos;
				}
				tracking = msg.message != OS.WM_LBUTTONUP;
				break;
			case OS.WM_KEYDOWN:
				cancelled = msg.wParam == OS.VK_ESCAPE;
				break;
		}
		OS.DispatchMessage (msg);
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
	if (rectangles == null) error (SWT.ERROR_NULL_ARGUMENT);
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
	checkWidget ();
	this.stippled = stippled;
}

}

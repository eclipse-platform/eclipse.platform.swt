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

 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
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
	Control parent;
	boolean tracking, stippled;
	Rectangle [] rectangles, proportions;
	int resizeCursor, clientCursor, cursorOrientation = SWT.NONE;
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
	POINT pt = new POINT ();
	pt.x = newX;  pt.y = newY;
	/*
	 * Convert to screen coordinates iff needed
 	 */
	if (parent != null) {
		OS.ClientToScreen (parent.handle, pt);
	}
	OS.SetCursorPos (pt.x, pt.y);
	return new Point (pt.x, pt.y);
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

	POINT pt = new POINT ();
	pt.x = newX;  pt.y = newY;
	/*
	 * Convert to screen coordinates iff needed
	 */
	if (parent != null) {
		OS.ClientToScreen (parent.handle, pt);
	}
	OS.SetCursorPos (pt.x, pt.y);

	/*
	* If the client has not provided a custom cursor then determine
	* the appropriate resize cursor.
	*/
	if (clientCursor == 0) {
		int newCursor = 0;
		switch (cursorOrientation) {
			case SWT.UP:
				newCursor = OS.LoadCursor (0, OS.IDC_SIZENS);
				break;
			case SWT.DOWN:
				newCursor = OS.LoadCursor (0, OS.IDC_SIZENS);
				break;
			case SWT.LEFT:
				newCursor = OS.LoadCursor (0, OS.IDC_SIZEWE);
				break;
			case SWT.RIGHT:
				newCursor = OS.LoadCursor (0, OS.IDC_SIZEWE);
				break;
			case SWT.LEFT | SWT.UP:
				newCursor = OS.LoadCursor (0, OS.IDC_SIZENWSE);
				break;
			case SWT.RIGHT | SWT.DOWN:
				newCursor = OS.LoadCursor (0, OS.IDC_SIZENWSE);
				break;
			case SWT.LEFT | SWT.DOWN:
				newCursor = OS.LoadCursor (0, OS.IDC_SIZENESW);
				break;
			case SWT.RIGHT | SWT.UP:
				newCursor = OS.LoadCursor (0, OS.IDC_SIZENESW);
				break;
			default:
				newCursor = OS.LoadCursor (0, OS.IDC_SIZEALL);
				break;
		}
		OS.SetCursor (newCursor);
		if (resizeCursor != 0) {
			OS.DestroyCursor (resizeCursor);
		}
		resizeCursor = newCursor;
	}
		
	return new Point (pt.x, pt.y);
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
/**
 * Draw the rectangles displayed by the tracker.
 */
void drawRectangles (Rectangle [] rects) {
	if (parent != null) {
		if (parent.isDisposed ()) return;
		Shell shell = parent.getShell ();
		shell.update (true);
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
	for (int i=0; i<rects.length; i++) {
		Rectangle rect = rects [i];
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
	checkWidget ();
	if (rectangles == null) return false;
	boolean cancelled = false;
	tracking = true;
	Event event = new Event ();
	MSG msg = new MSG ();
	boolean isMirrored = parent != null && (parent.style & SWT.MIRRORED) != 0;

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

	/*
	* If this tracker is being created without a mouse drag then
	* we need to create a transparent window that fills the screen
	* in order to get all mouse/keyboard events that occur
	* outside of our visible windows (ie.- over the desktop).
	*/
	int hwndTransparent = 0;
	Callback newProc = null;
	boolean mouseDown = OS.GetKeyState(OS.VK_LBUTTON) < 0;
	if (!mouseDown) {
		int width = OS.GetSystemMetrics (OS.SM_CXSCREEN);
		int height = OS.GetSystemMetrics (OS.SM_CYSCREEN);
		hwndTransparent = OS.CreateWindowEx (
			OS.WS_EX_TRANSPARENT,
			display.windowClass,
			null,
			OS.WS_POPUP | OS.WS_VISIBLE,
			0, 0,
			width, height,
			0,
			0,
			OS.GetModuleHandle (null),
			null);
		final int oldProc = OS.GetWindowLong (hwndTransparent, OS.GWL_WNDPROC);
		Object windowProc = new Object () {
			public int windowProc (int hwnd, int msg, int wParam, int lParam) {
				switch (msg) {
					/*
					* We typically do not want to answer that the transparent window is
					* transparent to hits since doing so negates the effect of having it
					* to grab events.  However, clients of the tracker should not be aware
					* of this transparent window.  Therefore if there is a hit query
					* performed as a result of client code then answer that the transparent
					* window is transparent to hits so that its existence will not impact
					* the client.
					*/
					case OS.WM_NCHITTEST:
						if (inEvent) return OS.HTTRANSPARENT;
						break;
					case OS.WM_SETCURSOR:
						if (clientCursor != 0) {
							OS.SetCursor(clientCursor);
							return 1;
						}
						if (resizeCursor != 0) {
							OS.SetCursor(resizeCursor);
							return 1;
						}
				}
				return OS.CallWindowProc (oldProc, hwnd, msg, wParam, lParam);
			}
		};
		newProc = new Callback (windowProc, "windowProc", 4); //$NON-NLS-1$
		OS.SetWindowLong (hwndTransparent, OS.GWL_WNDPROC, newProc.getAddress ());
	}

	drawRectangles (rectangles);
	Point cursorPos;
	if (mouseDown) {
		POINT pt = new POINT ();
		OS.GetCursorPos (pt);
		cursorPos = new Point (pt.x, pt.y);
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
	while (tracking && !cancelled) {
		if (parent != null && parent.isDisposed ()) break;
		OS.GetMessage (msg, 0, 0, 0);
		int message = msg.message;
		switch (message) {
			case OS.WM_LBUTTONUP:
			case OS.WM_MOUSEMOVE:
				int newPos = OS.GetMessagePos ();
				int newX = (short) (newPos & 0xFFFF);
				int newY = (short) (newPos >> 16);	
				if (newX != oldX || newY != oldY) {
					drawRectangles (rectangles);
					event.x = newX;
					event.y = newY;
					if ((style & SWT.RESIZE) != 0) {
						if (isMirrored) {
						   resizeRectangles (oldX - newX, newY - oldY);
						} else {
						   resizeRectangles (newX - oldX, newY - oldY);
						}
						cursorPos = adjustResizeCursor ();
						newX = cursorPos.x; newY = cursorPos.y;
						inEvent = true;
						sendEvent (SWT.Resize, event);
					} else {
						if (isMirrored) {
							moveRectangles (oldX - newX, newY - oldY); 
						} else { 
							moveRectangles (newX - oldX, newY - oldY);
						}
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
					drawRectangles (rectangles);
					oldX = newX;  oldY = newY;
				}
				tracking = msg.message != OS.WM_LBUTTONUP;
				break;
			case OS.WM_SYSKEYDOWN:
				cancelled = true;			
				tracking = false;
				break;
			case OS.WM_KEYDOWN:
				int stepSize = OS.GetKeyState (OS.VK_CONTROL) < 0 ? STEPSIZE_SMALL : STEPSIZE_LARGE;
				int xChange = 0, yChange = 0;
				switch (msg.wParam) {
					case OS.VK_ESCAPE:
						cancelled = true;
						tracking = false;
						break;
					case OS.VK_RETURN:
						tracking = false;
						break;
					case OS.VK_LEFT:
						xChange = isMirrored ? stepSize : -stepSize;
						break;
					case OS.VK_RIGHT:
						xChange = isMirrored ? -stepSize : stepSize;
						break;
					case OS.VK_UP:
						yChange = -stepSize;
						break;
					case OS.VK_DOWN:
						yChange = stepSize;
						break;
				}
				if (xChange != 0 || yChange != 0) {
					drawRectangles (rectangles);
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
					drawRectangles (rectangles);
					oldX = cursorPos.x;  oldY = cursorPos.y;
				}
				break;
		}
		/*
		* Don't dispatch mouse and key events in general, EXCEPT once this
		* tracker has finished its work.
		*/
		if (tracking && !cancelled) {
			if (OS.WM_KEYFIRST <= message && message <= OS.WM_KEYLAST) continue;
			if (OS.WM_MOUSEFIRST <= message && message <= OS.WM_MOUSELAST) continue;
		}
		OS.DispatchMessage (msg);
	}
	drawRectangles (rectangles);
	/*
	* Cleanup: If a transparent window was created in order to capture events then
	* destroy it and its callback object now.
	*/
	if (hwndTransparent != 0) {
		OS.DestroyWindow (hwndTransparent);
	}
	if (newProc != null) {
		newProc.dispose();
	}
	/*
	* Cleanup: If this tracker was resizing then the last cursor that it created
	* needs to be destroyed.
	*/
	if (resizeCursor != 0) {
		OS.DestroyCursor (resizeCursor);
	}
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
public void setCursor(Cursor newCursor) {
	checkWidget();
	clientCursor = 0;
	if (newCursor != null) {
		clientCursor = newCursor.handle;
		if (inEvent) OS.SetCursor(clientCursor);
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

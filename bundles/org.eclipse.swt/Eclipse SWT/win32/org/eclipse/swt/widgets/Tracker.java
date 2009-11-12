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
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tracker">Tracker snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Tracker extends Widget {
	Control parent;
	boolean tracking, cancelled, stippled;
	Rectangle [] rectangles = new Rectangle [0], proportions = rectangles;
	Rectangle bounds;
	int /*long*/ resizeCursor;
	Cursor clientCursor;
	int cursorOrientation = SWT.NONE;
	boolean inEvent = false;
	int /*long*/ hwndTransparent, hwndOpaque, oldTransparentProc, oldOpaqueProc;
	int oldX, oldY;
	
	static boolean IsVista = !OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0);

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
public void addKeyListener (KeyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.KeyUp,typedListener);
	addListener (SWT.KeyDown,typedListener);
}

Point adjustMoveCursor () {
	if (bounds == null) return null;
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
	if (clientCursor == null) {
		int /*long*/ newCursor = 0;
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

/**
 * Draw the rectangles displayed by the tracker.
 */
void drawRectangles (Rectangle [] rects, boolean stippled) {
	if (hwndOpaque != 0) {
		RECT rect1 = new RECT();
		int bandWidth = stippled ? 3 : 1;
		for (int i = 0; i < rects.length; i++) {
			Rectangle rect = rects[i];
			rect1.left = rect.x - bandWidth;
			rect1.top = rect.y - bandWidth;
			rect1.right = rect.x + rect.width + bandWidth * 2;
			rect1.bottom = rect.y + rect.height + bandWidth * 2;
			OS.RedrawWindow (hwndOpaque, rect1, 0, OS.RDW_INVALIDATE);
		}
		return;
	}
	int bandWidth = 1;
	int /*long*/ hwndTrack = parent == null ? OS.GetDesktopWindow () : parent.handle;
	int /*long*/ hDC = OS.GetDCEx (hwndTrack, 0, OS.DCX_CACHE);
	int /*long*/ hBitmap = 0, hBrush = 0, oldBrush = 0;
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
	cancelled = false;
	tracking = true;

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

	Callback newProc = null;
	boolean mouseDown = OS.GetKeyState(OS.VK_LBUTTON) < 0;
	/*
	* Bug in Vista. Drawing directly to the screen with XOR does not
	* perform well. The fix is to draw on layered window instead.
	* 
	* Note that one window (almost opaque) is used for catching all events and a
	* second window is used for drawing the rectangles. 
	*/
	if (IsVista && parent == null) {
		Rectangle bounds = display.getBounds();
		hwndTransparent = OS.CreateWindowEx (
			OS.WS_EX_LAYERED | OS.WS_EX_NOACTIVATE,
			display.windowClass,
			null,
			OS.WS_POPUP,
			bounds.x, bounds.y,
			bounds.width, bounds.height,
			0,
			0,
			OS.GetModuleHandle (null),
			null);
		OS.SetLayeredWindowAttributes (hwndTransparent, 0, (byte)0x01, OS.LWA_ALPHA);
		hwndOpaque = OS.CreateWindowEx (
			OS.WS_EX_LAYERED | OS.WS_EX_NOACTIVATE,
			display.windowClass,
			null,
			OS.WS_POPUP,
			bounds.x, bounds.y,
			bounds.width, bounds.height,
			hwndTransparent,
			0,
			OS.GetModuleHandle (null),
			null);
		OS.SetLayeredWindowAttributes (hwndOpaque, 0xFFFFFF, (byte)0xFF, OS.LWA_COLORKEY | OS.LWA_ALPHA);		
		newProc = new Callback (this, "transparentProc", 4); //$NON-NLS-1$
		int /*long*/ newProcAddress = newProc.getAddress ();
		if (newProcAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		oldTransparentProc = OS.GetWindowLongPtr (hwndTransparent, OS.GWLP_WNDPROC);
		OS.SetWindowLongPtr (hwndTransparent, OS.GWLP_WNDPROC, newProcAddress);
		oldOpaqueProc = OS.GetWindowLongPtr (hwndOpaque, OS.GWLP_WNDPROC);
		OS.SetWindowLongPtr (hwndOpaque, OS.GWLP_WNDPROC, newProcAddress);
		OS.ShowWindow (hwndTransparent, OS.SW_SHOWNOACTIVATE);
		OS.ShowWindow (hwndOpaque, OS.SW_SHOWNOACTIVATE);
	} else {
		/*
		* If this tracker is being created without a mouse drag then
		* we need to create a transparent window that fills the screen
		* in order to get all mouse/keyboard events that occur
		* outside of our visible windows (ie.- over the desktop).
		*/
		if (!mouseDown) {
			Rectangle bounds = display.getBounds();
			System.out.println(bounds);
			hwndTransparent = OS.CreateWindowEx (
				OS.WS_EX_TRANSPARENT,
				display.windowClass,
				null,
				OS.WS_POPUP,
				bounds.x, bounds.y,
				bounds.width, bounds.height,
				0,
				0,
				OS.GetModuleHandle (null),
				null);
			newProc = new Callback (this, "transparentProc", 4); //$NON-NLS-1$
			int /*long*/ newProcAddress = newProc.getAddress ();
			if (newProcAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
			oldTransparentProc = OS.GetWindowLongPtr (hwndTransparent, OS.GWLP_WNDPROC);
			OS.SetWindowLongPtr (hwndTransparent, OS.GWLP_WNDPROC, newProcAddress);
			OS.ShowWindow (hwndTransparent, OS.SW_SHOWNOACTIVATE);
		}
	}

	update ();
	drawRectangles (rectangles, stippled);
	Point cursorPos = null;
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
	if (cursorPos != null) {
		oldX = cursorPos.x;
		oldY = cursorPos.y;
	}

	try {
		/* Tracker behaves like a Dialog with its own OS event loop. */
		MSG msg = new MSG ();
		while (tracking && !cancelled) {
			if (parent != null && parent.isDisposed ()) break;
			OS.GetMessage (msg, 0, 0, 0);
			OS.TranslateMessage (msg);
			switch (msg.message) {
				case OS.WM_LBUTTONUP:
				case OS.WM_MOUSEMOVE:
					wmMouse (msg.message, msg.wParam, msg.lParam);
					break;
				case OS.WM_IME_CHAR: wmIMEChar (msg.hwnd, msg.wParam, msg.lParam); break;
				case OS.WM_CHAR: wmChar (msg.hwnd, msg.wParam, msg.lParam); break;
				case OS.WM_KEYDOWN: wmKeyDown (msg.hwnd, msg.wParam, msg.lParam); break;
				case OS.WM_KEYUP: wmKeyUp (msg.hwnd, msg.wParam, msg.lParam); break;
				case OS.WM_SYSCHAR: wmSysChar (msg.hwnd, msg.wParam, msg.lParam); break;
				case OS.WM_SYSKEYDOWN: wmSysKeyDown (msg.hwnd, msg.wParam, msg.lParam); break;
				case OS.WM_SYSKEYUP: wmSysKeyUp (msg.hwnd, msg.wParam, msg.lParam); break;
			}
			if (OS.WM_KEYFIRST <= msg.message && msg.message <= OS.WM_KEYLAST) continue;
			if (OS.WM_MOUSEFIRST <= msg.message && msg.message <= OS.WM_MOUSELAST) continue;
			if (hwndOpaque == 0) {
				if (msg.message == OS.WM_PAINT) {
					update ();
					drawRectangles (rectangles, stippled);
				}
			}
			OS.DispatchMessage (msg);
			if (hwndOpaque == 0) {
				if (msg.message == OS.WM_PAINT) {
					drawRectangles (rectangles, stippled);
				}
			}
		}
		if (mouseDown) OS.ReleaseCapture ();
		if (!isDisposed()) {
			update ();
			drawRectangles (rectangles, stippled);
		}
	} finally {
		/*
		* Cleanup: If a transparent window was created in order to capture events then
		* destroy it and its callback object now.
		*/
		if (hwndTransparent != 0) {
			OS.DestroyWindow (hwndTransparent);
			hwndTransparent = 0;
		}
		hwndOpaque = 0;
		if (newProc != null) {
			newProc.dispose ();
			oldTransparentProc = oldOpaqueProc = 0;
		}
		/*
		* Cleanup: If this tracker was resizing then the last cursor that it created
		* needs to be destroyed.
		*/
		if (resizeCursor != 0) {
			OS.DestroyCursor (resizeCursor);
			resizeCursor = 0;
		}
	}
	tracking = false;
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
	checkWidget ();
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
public void setCursor(Cursor newCursor) {
	checkWidget();
	clientCursor = newCursor;
	if (newCursor != null) {
		if (inEvent) OS.SetCursor (clientCursor.handle);
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

int /*long*/ transparentProc (int /*long*/ hwnd, int /*long*/ msg, int /*long*/ wParam, int /*long*/ lParam) {
	switch ((int)/*64*/msg) {
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
			if (clientCursor != null) {
				OS.SetCursor (clientCursor.handle);
				return 1;
			}
			if (resizeCursor != 0) {
				OS.SetCursor (resizeCursor);
				return 1;
			}
			break;
		case OS.WM_PAINT:
			if (hwndOpaque == hwnd) {
				PAINTSTRUCT ps = new PAINTSTRUCT();
				int /*long*/ hDC = OS.BeginPaint (hwnd, ps);
				int /*long*/ hBitmap = 0, hBrush = 0, oldBrush = 0;			
				int /*long*/ transparentBrush = OS.CreateSolidBrush(0xFFFFFF);
				oldBrush = OS.SelectObject (hDC, transparentBrush);
				OS.PatBlt (hDC, ps.left, ps.top, ps.right - ps.left, ps.bottom - ps.top, OS.PATCOPY);
				OS.SelectObject (hDC, oldBrush);
				OS.DeleteObject (transparentBrush);
				int bandWidth = 1;
				if (stippled) {
					bandWidth = 3;
					byte [] bits = {-86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0};
					hBitmap = OS.CreateBitmap (8, 8, 1, 1, bits);
					hBrush = OS.CreatePatternBrush (hBitmap);
					oldBrush = OS.SelectObject (hDC, hBrush);
					OS.SetBkColor (hDC, 0xF0F0F0);
				} else {
					oldBrush = OS.SelectObject (hDC, OS.GetStockObject(OS.BLACK_BRUSH));
				}
				Rectangle[] rects = this.rectangles;
				for (int i=0; i<rects.length; i++) {
					Rectangle rect = rects [i];
					OS.PatBlt (hDC, rect.x, rect.y, rect.width, bandWidth, OS.PATCOPY);
					OS.PatBlt (hDC, rect.x, rect.y + bandWidth, bandWidth, rect.height - (bandWidth * 2), OS.PATCOPY);
					OS.PatBlt (hDC, rect.x + rect.width - bandWidth, rect.y + bandWidth, bandWidth, rect.height - (bandWidth * 2), OS.PATCOPY);
					OS.PatBlt (hDC, rect.x, rect.y + rect.height - bandWidth, rect.width, bandWidth, OS.PATCOPY);
				}
				OS.SelectObject (hDC, oldBrush);
				if (stippled) {
					OS.DeleteObject (hBrush);
					OS.DeleteObject (hBitmap);
				}
				OS.EndPaint (hwnd, ps);
				return 0;
			}
	}
	return OS.CallWindowProc (hwnd == hwndTransparent ? oldTransparentProc : oldOpaqueProc, hwnd, (int)/*64*/msg, wParam, lParam);
}

void update () {
	if (hwndOpaque != 0) return;
	if (parent != null) {
		if (parent.isDisposed ()) return;
		Shell shell = parent.getShell ();
		shell.update (true);
	} else {
		display.update ();
	}
}

LRESULT wmKeyDown (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.wmKeyDown (hwnd, wParam, lParam);
	if (result != null) return result;
	boolean isMirrored = parent != null && (parent.style & SWT.MIRRORED) != 0;
	int stepSize = OS.GetKeyState (OS.VK_CONTROL) < 0 ? STEPSIZE_SMALL : STEPSIZE_LARGE;
	int xChange = 0, yChange = 0;
	switch ((int)/*64*/wParam) {
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
			inEvent = true;
			sendEvent (SWT.Resize, event);
			inEvent = false;
			/*
			* It is possible (but unlikely) that application
			* code could have disposed the widget in the resize
			* event.  If this happens return false to indicate
			* that the tracking has failed.
			*/
			if (isDisposed ()) {
				cancelled = true;
				return LRESULT.ONE;
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
			inEvent = true;
			sendEvent (SWT.Move, event);
			inEvent = false;
			/*
			* It is possible (but unlikely) that application
			* code could have disposed the widget in the move
			* event.  If this happens return false to indicate
			* that the tracking has failed.
			*/
			if (isDisposed ()) {
				cancelled = true;
				return LRESULT.ONE;
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
	return result;
}

LRESULT wmSysKeyDown (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.wmSysKeyDown (hwnd, wParam, lParam);
	if (result != null) return result;
	cancelled = true;			
	tracking = false;
	return result;
}

LRESULT wmMouse (int message, int /*long*/ wParam, int /*long*/ lParam) {
	boolean isMirrored = parent != null && (parent.style & SWT.MIRRORED) != 0;
	int newPos = OS.GetMessagePos ();
	int newX = OS.GET_X_LPARAM (newPos);
	int newY = OS.GET_Y_LPARAM (newPos);
	if (newX != oldX || newY != oldY) {
		Rectangle [] oldRectangles = rectangles;
		boolean oldStippled = stippled;
		Rectangle [] rectsToErase = new Rectangle [rectangles.length];
		for (int i = 0; i < rectangles.length; i++) {
			Rectangle current = rectangles [i];
			rectsToErase [i] = new Rectangle (current.x, current.y, current.width, current.height);
		}
		Event event = new Event ();
		event.x = newX;
		event.y = newY;
		if ((style & SWT.RESIZE) != 0) {
			if (isMirrored) {
			   resizeRectangles (oldX - newX, newY - oldY);
			} else {
			   resizeRectangles (newX - oldX, newY - oldY);
			}
			inEvent = true;
			sendEvent (SWT.Resize, event);
			inEvent = false;
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the resize
			* event.  If this happens, return false to indicate
			* that the tracking has failed.
			*/
			if (isDisposed ()) {
				cancelled = true;
				return LRESULT.ONE;
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
			}
			else {
				draw = true;
			}
			if (draw) {
				drawRectangles (rectsToErase, oldStippled);
				update ();
				drawRectangles (rectangles, stippled);
			}
			Point cursorPos = adjustResizeCursor ();
			if (cursorPos != null) {
				newX = cursorPos.x;
				newY = cursorPos.y;
			}
		} else {
			if (isMirrored) {
				moveRectangles (oldX - newX, newY - oldY); 
			} else { 
				moveRectangles (newX - oldX, newY - oldY);
			}
			inEvent = true;
			sendEvent (SWT.Move, event);
			inEvent = false;
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the move
			* event.  If this happens, return false to indicate
			* that the tracking has failed.
			*/
			if (isDisposed ()) {
				cancelled = true;
				return LRESULT.ONE;
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
		oldX = newX;
		oldY = newY;
	}
	tracking = message != OS.WM_LBUTTONUP;
	return null;
}

}

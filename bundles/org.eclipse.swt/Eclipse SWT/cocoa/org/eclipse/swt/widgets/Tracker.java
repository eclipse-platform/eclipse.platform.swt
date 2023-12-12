/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

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
	Cursor clientCursor, resizeCursor;
	Rectangle [] rectangles = new Rectangle [0], proportions = rectangles;
	Rectangle bounds;
	int cursorOrientation = SWT.NONE;
	boolean inEvent = false;
	NSWindow windows[];
	int oldX, oldY;

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
public void addKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.KeyUp,typedListener);
	addListener(SWT.KeyDown,typedListener);
}

Point adjustMoveCursor () {
	if (bounds == null) return null;
	int newX = bounds.x + bounds.width / 2;
	int newY = bounds.y;
	/*
	 * Convert to screen coordinates if needed
	 */
	if (parent != null) {
		Point pt = parent.toDisplay (newX, newY);
		newX = pt.x;
		newY = pt.y;
	}
	display.setCursorLocation(newX, newY);
	return new Point (newX, newY);
}

Point adjustResizeCursor (boolean movePointer) {
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
	 * Convert to screen coordinates if needed
	 */
	if (parent != null) {
		Point pt = parent.toDisplay (newX, newY);
		newX = pt.x;
		newY = pt.y;
	}
	if (movePointer) {
		display.setCursorLocation(newX, newY);
	}

	/*
	* If the client has not provided a custom cursor then determine
	* the appropriate resize cursor.
	*/
	if (clientCursor == null) {
		Cursor newCursor = new Cursor(display, switch (cursorOrientation) {
		  case SWT.UP, SWT.DOWN -> SWT.CURSOR_SIZENS;
		  case SWT.LEFT, SWT.RIGHT -> SWT.CURSOR_SIZEWE;
		  case (SWT.LEFT | SWT.UP ), (SWT.RIGHT | SWT.DOWN)-> SWT.CURSOR_SIZENWSE;
		  case (SWT.LEFT | SWT.DOWN), (SWT.RIGHT | SWT.UP) -> SWT.CURSOR_SIZENESW;
		  default -> SWT.CURSOR_SIZEALL;

		});
		display.lockCursor = false;
		newCursor.handle.set();
		display.lockCursor = true;
		if (resizeCursor != null) {
			resizeCursor.dispose ();
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

void drawRectangles (NSWindow window, Rectangle [] rects, boolean erase) {
	NSGraphicsContext context = window.graphicsContext();
	if (context == null) {
		long width = (long) window.frame().width;
		long height = (long) window.frame().height;
		NSBitmapImageRep rep = (NSBitmapImageRep) new NSBitmapImageRep().alloc();
		rep = rep.initWithBitmapDataPlanes(0, width, height, 8, 3, false, false, OS.NSDeviceRGBColorSpace,
				OS.NSAlphaFirstBitmapFormat, width * 4, 32);
		context = NSGraphicsContext.graphicsContextWithBitmapImageRep(rep);
		rep.release();
	}
	NSGraphicsContext.static_saveGraphicsState();
	NSGraphicsContext.setCurrentContext(context);
	context.saveGraphicsState();
	Point parentOrigin;
	if (parent != null) {
		parentOrigin = display.map (parent, null, 0, 0);
	} else {
		parentOrigin = new Point (0, 0);
	}
	context.setCompositingOperation(erase ? OS.NSCompositeClear : OS.NSCompositeSourceOver);
	NSRect rectFrame = new NSRect();
	NSPoint globalPoint = new NSPoint();
	double screenHeight = display.getPrimaryFrame().height;
	for (int i=0; i<rects.length; i++) {
		Rectangle rect = rects [i];
		rectFrame.x = rect.x + parentOrigin.x;
		rectFrame.y = screenHeight - (int)((rect.y + parentOrigin.y) + rect.height);
		rectFrame.width = rect.width;
		rectFrame.height = rect.height;
		globalPoint.x = rectFrame.x;
		globalPoint.y = rectFrame.y;
		globalPoint = window.convertScreenToBase(globalPoint);
		rectFrame.x = globalPoint.x;
		rectFrame.y = globalPoint.y;

		if (erase) {
			rectFrame.width++;
			rectFrame.height++;
			NSBezierPath.fillRect(rectFrame);
		} else {
			rectFrame.x += 0.5f;
			rectFrame.y += 0.5f;
			NSBezierPath.strokeRect(rectFrame);
		}
	}
	if (!erase) context.flushGraphics();
	context.restoreGraphicsState();
	NSGraphicsContext.static_restoreGraphicsState();
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

void mouse (NSEvent nsEvent) {
	NSPoint location;
	if (nsEvent == null || nsEvent.type() == OS.NSMouseMoved) {
		location = NSEvent.mouseLocation();
	} else {
		location = nsEvent.locationInWindow();
		location = nsEvent.window().convertBaseToScreen(location);
	}
	location.y = display.getPrimaryFrame().height - location.y;
	int newX = (int)location.x, newY = (int)location.y;
	if (newX != oldX || newY != oldY) {
		Rectangle [] oldRectangles = rectangles;
		Rectangle [] rectsToErase = new Rectangle [rectangles.length];
		for (int i = 0; i < rectangles.length; i++) {
			Rectangle current = rectangles [i];
			rectsToErase [i] = new Rectangle (current.x, current.y, current.width, current.height);
		}
		Event event = new Event ();
		event.x = newX;
		event.y = newY;
		if ((style & SWT.RESIZE) != 0) {
			boolean orientationInit = resizeRectangles (newX - oldX, newY - oldY);
			inEvent = true;
			sendEvent (SWT.Resize, event);
			inEvent = false;
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the move
			* event.  If this happens, return false to indicate
			* that the tracking has failed.
			*/
			if (isDisposed ()) {
				cancelled = true;
				return;
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
				for (int i = 0; i < windows.length; i++) {
					drawRectangles (windows[i], rectsToErase, true);
				}
				for (int i = 0; i < windows.length; i++) {
					drawRectangles (windows[i], rectangles, false);
				}
			}
			Point cursorPos = adjustResizeCursor (orientationInit);
			if (cursorPos != null) {
				newX = cursorPos.x;
				newY = cursorPos.y;
			}
		} else {
			moveRectangles (newX - oldX, newY - oldY);
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
				return;
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
				for (int i = 0; i < windows.length; i++) {
					drawRectangles (windows[i], rectsToErase, true);
				}
				for (int i = 0; i < windows.length; i++) {
					drawRectangles (windows[i], rectangles, false);
				}
			}
		}
		oldX = newX;  oldY = newY;
	}
	switch ((int)nsEvent.type()) {
		case OS.NSLeftMouseUp:
		case OS.NSRightMouseUp:
		case OS.NSOtherMouseUp:
			tracking = false;
	}
}

void key (NSEvent nsEvent) {
	int nsType = (int)nsEvent.type();
	long modifierFlags = nsEvent.modifierFlags();
	int nsKeyCode = nsEvent.keyCode();
	int keyCode = Display.translateKey (nsKeyCode);

	switch (nsType) {
		case OS.NSKeyDown:
		case OS.NSKeyUp: {
			Event event = new Event();
			event.keyCode = keyCode;
			int type = nsType == OS.NSKeyDown ? SWT.KeyDown : SWT.KeyUp;
			if (!setKeyState (event, type, nsEvent)) break;
			if (!sendKeyEvent (type, event)) return;
			break;
		}
		case OS.NSFlagsChanged: {
			int mask = 0;
			switch (keyCode) {
				case SWT.ALT: mask = OS.NSAlternateKeyMask; break;
				case SWT.CONTROL: mask = OS.NSControlKeyMask; break;
				case SWT.COMMAND: mask = OS.NSCommandKeyMask; break;
				case SWT.SHIFT: mask = OS.NSShiftKeyMask; break;
				case SWT.CAPS_LOCK:
					Event event = new Event();
					event.keyCode = keyCode;
					setInputState (event, nsEvent, SWT.KeyDown);
					sendKeyEvent (SWT.KeyDown, event);
					setInputState (event, nsEvent, SWT.KeyUp);
					sendKeyEvent (SWT.KeyUp, event);
					break;
			}
			if (mask != 0) {
				int type = (mask & modifierFlags) != 0 ? SWT.KeyDown : SWT.KeyUp;
				Event event = new Event();
				event.keyCode = keyCode;
				setLocationMask(event, nsEvent);
				setInputState (event, nsEvent, type);
				if (!sendKeyEvent (type, event)) return;
			}
			break;
		}
	}

	int stepSize = (modifierFlags & OS.NSControlKeyMask) != 0 ? STEPSIZE_SMALL : STEPSIZE_LARGE;
	int xChange = 0, yChange = 0;
	switch (nsKeyCode) {
		case 53: /* Esc */
			cancelled = true;
			tracking = false;
			break;
		case 76: /* KP Enter */
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
		Rectangle [] oldRectangles = rectangles;
		Rectangle [] rectsToErase = new Rectangle [rectangles.length];
		for (int i = 0; i < rectangles.length; i++) {
			Rectangle current = rectangles [i];
			rectsToErase [i] = new Rectangle (current.x, current.y, current.width, current.height);
		}
		Event event = new Event ();
		int newX = oldX + xChange;
		int newY = oldY + yChange;
		event.x = newX;
		event.y = newY;
		Point cursorPos;
		if ((style & SWT.RESIZE) != 0) {
			resizeRectangles (xChange, yChange);
			inEvent = true;
			sendEvent (SWT.Resize, event);
			inEvent = false;
			/*
			* It is possible (but unlikely) that application
			* code could have disposed the widget in the move
			* event.  If this happens return false to indicate
			* that the tracking has failed.
			*/
			if (isDisposed ()) {
				cancelled = true;
				return;
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
				for (int i = 0; i < windows.length; i++) {
					drawRectangles (windows[i], rectsToErase, true);
				}
				for (int i = 0; i < windows.length; i++) {
					drawRectangles (windows[i], rectangles, false);
				}
			}
			cursorPos = adjustResizeCursor (true);
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
				return;
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
				for (int i = 0; i < windows.length; i++) {
					drawRectangles (windows[i], rectsToErase, true);
				}
				for (int i = 0; i < windows.length; i++) {
					drawRectangles (windows[i], rectangles, false);
				}
			}
			cursorPos = adjustMoveCursor ();
		}
		if (cursorPos != null) {
			oldX = cursorPos.x;
			oldY = cursorPos.y;
		}
	}
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
	Display display = this.display;
	cancelled = false;
	tracking = true;

	/*
	 * Starting from OSX 10.9, displays have separate spaces by default. Hence, one
	 * window can't be visible/drawn across multiple monitors. Due to this change,
	 * we can't draw the Tracker rectangles on all the screens using a single
	 * NSWindow. Fix is to use one NSWindow per screen.
	 */
	NSArray screens = NSScreen.screens();
	int count = (int)screens.count();
	windows = new NSWindow [count];
	for (int i = 0; i < count; i++) {
		NSScreen screen = new NSScreen(screens.objectAtIndex(i));
		NSRect frame = screen.frame();
		NSWindow window = (NSWindow)new NSWindow().alloc();
		window = window.initWithContentRect(frame, OS.NSBorderlessWindowMask, OS.NSBackingStoreBuffered, false);
		window.setOpaque(false);
		window.setLevel(OS.NSStatusWindowLevel);
		window.setContentView(null);
		window.setBackgroundColor(NSColor.clearColor());
		NSGraphicsContext context = window.graphicsContext();
		if (context == null) {
			long width = (long) window.frame().width;
			long height = (long) window.frame().height;
			NSBitmapImageRep rep = (NSBitmapImageRep) new NSBitmapImageRep().alloc();
			rep = rep.initWithBitmapDataPlanes(0, width, height, 8, 3, false, false, OS.NSDeviceRGBColorSpace,
					OS.NSAlphaFirstBitmapFormat, width * 4, 32);
			context = NSGraphicsContext.graphicsContextWithBitmapImageRep(rep);
			rep.release();
		}
		NSGraphicsContext.static_saveGraphicsState();
		NSGraphicsContext.setCurrentContext(context);
		context.setCompositingOperation(OS.NSCompositeClear);
		frame.x = frame.y = 0;
		NSBezierPath.fillRect(frame);
		NSGraphicsContext.static_restoreGraphicsState();
		window.orderFrontRegardless();
		windows[i] = window;
	}

	for (int i = 0; i < windows.length; i++) {
		drawRectangles (windows[i], rectangles, false);
	}

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

	Point cursorPos;
	boolean down = false;
	NSApplication application = NSApplication.sharedApplication();
	NSEvent currentEvent = application.currentEvent();
	if (currentEvent != null) {
		switch ((int)currentEvent.type()) {
			case OS.NSLeftMouseDown:
			case OS.NSLeftMouseDragged:
			case OS.NSRightMouseDown:
			case OS.NSRightMouseDragged:
			case OS.NSOtherMouseDown:
			case OS.NSOtherMouseDragged:
				down = true;
		}
	}
	if (down) {
		cursorPos = display.getCursorLocation();
	} else {
		if ((style & SWT.RESIZE) != 0) {
			cursorPos = adjustResizeCursor (true);
		} else {
			cursorPos = adjustMoveCursor ();
		}
	}
	if (cursorPos != null) {
		oldX = cursorPos.x;
		oldY = cursorPos.y;
	}

	Control oldTrackingControl = display.trackingControl;
	display.trackingControl = null;
	/* Tracker behaves like a Dialog with its own OS event loop. */
	while (tracking && !cancelled) {
		display.addPool();
		try {
			if (parent != null && parent.isDisposed ()) break;
			display.runSkin ();
			display.runDeferredLayouts ();
			NSEvent event = application.nextEventMatchingMask(OS.NSAnyEventMask, NSDate.distantFuture(), OS.NSDefaultRunLoopMode, true);
			if (event == null) continue;
			int type = (int)event.type();
			switch (type) {
				case OS.NSLeftMouseUp:
				case OS.NSRightMouseUp:
				case OS.NSOtherMouseUp:
				case OS.NSMouseMoved:
				case OS.NSLeftMouseDragged:
				case OS.NSRightMouseDragged:
				case OS.NSOtherMouseDragged:
					mouse(event);
					break;
				case OS.NSKeyDown:
				case OS.NSKeyUp:
				case OS.NSFlagsChanged:
					key(event);
					break;
			}
			boolean dispatch = true;
			switch (type) {
				case OS.NSLeftMouseDown:
				case OS.NSLeftMouseUp:
				case OS.NSRightMouseDown:
				case OS.NSRightMouseUp:
				case OS.NSOtherMouseDown:
				case OS.NSOtherMouseUp:
				case OS.NSMouseMoved:
				case OS.NSLeftMouseDragged:
				case OS.NSRightMouseDragged:
				case OS.NSOtherMouseDragged:
				case OS.NSMouseEntered:
				case OS.NSMouseExited:
				case OS.NSKeyDown:
				case OS.NSKeyUp:
				case OS.NSFlagsChanged:
					dispatch = false;
			}
			if (dispatch) application.sendEvent(event);
			if (clientCursor != null && resizeCursor == null) {
				display.lockCursor = false;
				clientCursor.handle.set();
				display.lockCursor = true;
			}
			display.runAsyncMessages (false);
		} finally {
			display.removePool();
		}
	}

	/*
	* Cleanup: If this tracker was resizing then the last cursor that it created
	* needs to be destroyed.
	*/
	if (resizeCursor != null) resizeCursor.dispose();
	resizeCursor = null;

	if (oldTrackingControl != null && !oldTrackingControl.isDisposed()) {
		display.trackingControl = oldTrackingControl;
	}
	display.setCursor(display.findControl(true));
	if (!isDisposed()) {
		for (int i = 0; i < windows.length; i++) {
			drawRectangles (windows[i], rectangles, true);
		}
	}
	for (int i = 0; i < windows.length; i++) {
		if (windows[i] != null) {
			windows[i].close();
			windows[i] = null;
		}
	}
	tracking = false;
	windows = null;
	return !cancelled;
}

@Override
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.KeyUp, listener);
	eventTable.unhook(SWT.KeyDown, listener);
}

/*
 * Returns true if the pointer's orientation was initialized in some dimension,
 * and false otherwise.
 */
boolean resizeRectangles (int xChange, int yChange) {
	if (bounds == null) return false;
	boolean orientationInit = false;
	/*
	* If the cursor orientation has not been set in the orientation of
	* this change then try to set it here.
	*/
	if (xChange < 0 && ((style & SWT.LEFT) != 0) && ((cursorOrientation & SWT.RIGHT) == 0)) {
		if ((cursorOrientation & SWT.LEFT) == 0) {
			cursorOrientation |= SWT.LEFT;
			orientationInit = true;
		}
	}
	if (xChange > 0 && ((style & SWT.RIGHT) != 0) && ((cursorOrientation & SWT.LEFT) == 0)) {
		if ((cursorOrientation & SWT.RIGHT) == 0) {
			cursorOrientation |= SWT.RIGHT;
			orientationInit = true;
		}
	}
	if (yChange < 0 && ((style & SWT.UP) != 0) && ((cursorOrientation & SWT.DOWN) == 0)) {
		if ((cursorOrientation & SWT.UP) == 0) {
			cursorOrientation |= SWT.UP;
			orientationInit = true;
		}
	}
	if (yChange > 0 && ((style & SWT.DOWN) != 0) && ((cursorOrientation & SWT.UP) == 0)) {
		if ((cursorOrientation & SWT.DOWN) == 0) {
			cursorOrientation |= SWT.DOWN;
			orientationInit = true;
		}
	}

	/*
	 * If the bounds will flip about the x or y axis then apply the adjustment
	 * up to the axis (ie.- where bounds width/height becomes 0), change the
	 * cursor's orientation accordingly, and flip each Rectangle's origin (only
	 * necessary for > 1 Rectangles)
	 */
	if ((cursorOrientation & SWT.LEFT) != 0) {
		if (xChange > bounds.width) {
			if ((style & SWT.RIGHT) == 0) return orientationInit;
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
			if ((style & SWT.LEFT) == 0) return orientationInit;
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
			if ((style & SWT.DOWN) == 0) return orientationInit;
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
			if ((style & SWT.UP) == 0) return orientationInit;
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
	return orientationInit;
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
		display.lockCursor = false;
		if (inEvent) newCursor.handle.set();
		display.lockCursor = true;
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
	int length = rectangles.length;
	this.rectangles = new Rectangle [length];
	for (int i = 0; i < length; i++) {
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

}

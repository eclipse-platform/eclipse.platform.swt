/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
	Cursor cursor;
	long lastCursor, window, overlay;
	long surface;
	boolean tracking, cancelled, grabbed, stippled;
	Rectangle [] rectangles = new Rectangle [0], proportions = rectangles;
	Rectangle bounds;
	int cursorOrientation = SWT.NONE;
	int oldX, oldY;
	long provider;

	// Re-use/cache some items for performance reasons as draw-events must be efficient to prevent jitter.
	Rectangle cachedCombinedDisplayResolution = Display.getDefault().getBounds(); // Cached for performance reasons.
	Rectangle cachedUnion = new Rectangle(0, 0, 0, 0);
	Boolean cachedBackgroundIsOpaque; // Note purposely lazy init.

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
	super (parent, checkStyle(style));
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

	Point point = display.mapInPixels(parent, null, newX, newY);
	display.setCursorLocation(point);

	int[] actualX = new int[1], actualY = new int[1], state = new int[1];
	if (GTK.GTK4) {
		double[] actualXDouble = new double[1], actualYDouble = new double[1];
		display.getPointerPosition(actualXDouble, actualYDouble);

		actualX[0] = (int)actualXDouble[0];
		actualY[0] = (int)actualYDouble[0];
	} else {
		display.getWindowPointerPosition(window, actualX, actualY, state);
	}

	return new Point(actualX[0], actualY[0]);
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

	Point point = display.mapInPixels (parent, null, newX, newY);
	display.setCursorLocation (point);

	/*
	 * The call to XWarpPointer does not always place the pointer on the
	 * exact location that is specified, so do a query (below) to get the
	 * actual location of the pointer after it has been moved.
	 */
	int [] actualX = new int [1], actualY = new int [1], state = new int [1];
	if (GTK.GTK4) {
		double[] actualXDouble = new double[1], actualYDouble = new double[1];
		display.getPointerPosition(actualXDouble, actualYDouble);

		actualX[0] = (int)actualXDouble[0];
		actualY[0] = (int)actualYDouble[0];
	} else {
		display.getWindowPointerPosition(window, actualX, actualY, state);
	}

	return new Point(actualX[0], actualY[0]);
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

static int checkStyle (int style) {
	if ((style & (SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN)) == 0) {
		style |= SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN;
	}
	return style;
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
 * Developer note:
 * - Rectangles can have absolute coords [Tracker(Display)] or relative to parent [Tracker(Composite)]
 * - This method is called a lot, optimize your code.
 * - Note, region != rectangle. A region can have a non-squared form, e.g an 'L' shape.
 */
void drawRectangles (Rectangle [] rects) {
	long gdkResource = 0;
	if (GTK.GTK4) {
		if (parent != null) gdkResource = gtk_widget_get_surface(parent.handle);
	} else {
		gdkResource = GDK.gdk_get_default_root_window();
	}
	if (parent != null) {
		long paintHandle = parent.paintHandle();
		gdkResource = GTK.GTK4 ? gtk_widget_get_surface(paintHandle) : gtk_widget_get_window (paintHandle);
	}
	if (gdkResource == 0) return;

	if (overlay == 0) return;
	GTK3.gtk_widget_shape_combine_region (overlay, 0);

	// Bug 498217.
	// As of Gtk 3.9.1, Commit a60ccd3672467efb454b121993febc36f33cbc79, off-screen GDK windows are not processed.
	// Because of this gtk doesn't send move events to SWT. Platform.UI uses an off-screen tracker to draw
	// custom rectangles for it's part-drag-preview. Drawing/updates for these broke because tracker is off screen.
	// Solution: If a tracker is to move off-screen, then instead draw it 1x1 and transparent.
	boolean isOnScreen = true;
	{ // Figure out if the combined rectangles are on or off screen.

		// Produce a single rectangle big enough to contain all rects.
		// Note, this is different from loop below that creates a Region. (See region != rectangle note in javadoc).
		cachedUnion.x = rects[0].x;
		cachedUnion.y = rects[0].y;
		cachedUnion.width = rects[0].width;
		cachedUnion.height = rects[0].height;
		if (rects.length > 1) {
			for (int i = 1; i < rects.length; i++) {
				cachedUnion.add(rects[i]);
			}
		}

		// Ensure we have absolute screen coordinates. (btw, there are no absolute coordinates on Wayland, so Tracker(Display) is probably broken).
		if (parent != null) {  // if Tracker(Display) has absolute coords.  Tracker(Composite) has relative. For relative, we need to find absolute.
			cachedUnion = display.mapInPixels(parent, null, cachedUnion) ;
		}

		if (!cachedCombinedDisplayResolution.intersects(cachedUnion)) {
			isOnScreen = false;
		}
	}

	long region = Cairo.cairo_region_create ();
	cairo_rectangle_int_t rect = new cairo_rectangle_int_t();
	if (isOnScreen) {
		// Combine Rects into a region. (region is not necessarily a rectangle, E.g it can be 'L' shaped etc..).
		for (int i = 0; i < rects.length; i++) {
			// Turn filled rectangles into just the outer lines by drawing one line at a time.
			Rectangle r = parent != null ? display.mapInPixels(parent, null, rects[i]) : rects[i];
			rect.x = r.x;
			rect.y = r.y;
			rect.width = r.width + 1;
			rect.height = 1;
			Cairo.cairo_region_union_rectangle(region, rect); // Top line
			rect.width = 1;
			rect.height = r.height + 1;
			Cairo.cairo_region_union_rectangle(region, rect); // Left line.
			rect.x = r.x + r.width;
			Cairo.cairo_region_union_rectangle(region, rect); // Right line.
			rect.x = r.x;
			rect.y = r.y + r.height;
			rect.width = r.width + 1;
			rect.height = 1;
			Cairo.cairo_region_union_rectangle(region, rect); // Bottom line
		}
		setTrackerBackground(true);
	} else { // Offscreen
		// part of Bug 498217 fix. Tracker must be at least 1x1 for move events to work.
		rect.x = 0;
		rect.y = 0;
		rect.height = 1;
		rect.width = 1;
		Cairo.cairo_region_union_rectangle(region, rect);
		setTrackerBackground(false);
	}

	GTK3.gtk_widget_shape_combine_region (overlay, region);
	Cairo.cairo_region_destroy (region);
	if (GTK.GTK4) {
		long overlaySurface = GTK4.gtk_native_get_surface(GTK4.gtk_widget_get_native (overlay));
		GDK.gdk_surface_hide (overlaySurface);
		/* TODO: GTK does not provide a gdk_surface_show, probably will require use of the present api */
	} else {
		long overlayWindow = GTK3.gtk_widget_get_window (overlay);
		GDK.gdk_window_hide (overlayWindow);
		GDK.gdk_window_show (overlayWindow);
	}
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
		result [i] = DPIUtil.autoScaleDown (new Rectangle (current.x, current.y, current.width, current.height));
	}
	return result;
}

Rectangle [] getRectanglesInPixels () {
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

boolean grab () {
	long cursor = this.cursor != null ? this.cursor.handle : 0;
	int result = gdk_pointer_grab (GTK.GTK4 ? surface : window, GDK.GDK_OWNERSHIP_NONE, false,
			GDK.GDK_POINTER_MOTION_MASK | GDK.GDK_BUTTON_RELEASE_MASK, 0, cursor, GDK.GDK_CURRENT_TIME);
	return result == GDK.GDK_GRAB_SUCCESS;
}

@Override
long gtk_button_release_event (long widget, long event) {
	Control.mouseDown = false;
	return gtk_mouse (GTK.GTK4 ? GDK.GDK4_BUTTON_RELEASE : GDK.GDK_BUTTON_RELEASE, widget, event);
}

@Override
long gtk_key_press_event (long widget, long eventPtr) {
	long result = super.gtk_key_press_event (widget, eventPtr);
	if (result != 0) return result;

	int [] state = new int [1];
	int [] keyval = new int [1];
	if (GTK.GTK4) {
		state[0] = GDK.gdk_event_get_modifier_state(eventPtr);
		keyval[0] = GDK.gdk_key_event_get_keyval(eventPtr);
	} else {
		GDK.gdk_event_get_state(eventPtr, state);
		GDK.gdk_event_get_keyval(eventPtr, keyval);
	}

	int stepSize = ((state[0] & GDK.GDK_CONTROL_MASK) != 0) ? STEPSIZE_SMALL : STEPSIZE_LARGE;
	int xChange = 0, yChange = 0;
	switch (keyval[0]) {
		case GDK.GDK_Escape:
			cancelled = true;
			// fallthrough
		case GDK.GDK_Return:
			tracking = false;
			break;
		case GDK.GDK_Left:
			xChange = -stepSize;
			break;
		case GDK.GDK_Right:
			xChange = stepSize;
			break;
		case GDK.GDK_Up:
			yChange = -stepSize;
			break;
		case GDK.GDK_Down:
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
		Rectangle eventRect = new Rectangle (oldX + xChange, oldY + yChange, 0, 0);
		event.setBounds (DPIUtil.autoScaleDown (eventRect));
		if (parent != null && (parent.style & SWT.MIRRORED) != 0) {
			event.x = DPIUtil.autoScaleDown (parent.getClientWidth ()) - event.width - event.x;
		}
		if ((style & SWT.RESIZE) != 0) {
			resizeRectangles (xChange, yChange);
			sendEvent (SWT.Resize, event);
			/*
			* It is possible (but unlikely) that application
			* code could have disposed the widget in the resize
			* event.  If this happens return false to indicate
			* that the tracking has failed.
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
				drawRectangles (rectangles);
			}
			Point cursorPos = adjustResizeCursor ();
			if (cursorPos != null) {
				oldX = cursorPos.x;
				oldY = cursorPos.y;
			}
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
				drawRectangles (rectsToErase);
				update ();
				drawRectangles (rectangles);
			}
			Point cursorPos = adjustMoveCursor ();
			if (cursorPos != null) {
				oldX = cursorPos.x;
				oldY = cursorPos.y;
			}
		}
	}
	return result;
}

@Override
long gtk_motion_notify_event (long widget, long eventPtr) {
	long cursor = this.cursor != null ? this.cursor.handle : 0;
	if (cursor != lastCursor) {
		ungrab ();
		grabbed = grab ();
		lastCursor = cursor;
	}
	return gtk_mouse (GTK.GTK4 ? GDK.GDK4_MOTION_NOTIFY : GDK.GDK_MOTION_NOTIFY, widget, eventPtr);
}

long gtk_mouse (int eventType, long widget, long eventPtr) {
	int [] newX = new int [1], newY = new int [1];
	if (GTK.GTK4) {
		double[] newXDouble = new double[1], newYDouble = new double[1];
		display.getPointerPosition(newXDouble, newYDouble);

		newX[0] = (int)newXDouble[0];
		newY[0] = (int)newYDouble[0];
	} else {
		display.getWindowPointerPosition(window, newX, newY, null);
	}

	if (oldX != newX [0] || oldY != newY [0]) {
		Rectangle [] oldRectangles = rectangles;
		Rectangle [] rectsToErase = new Rectangle [rectangles.length];
		for (int i = 0; i < rectangles.length; i++) {
			Rectangle current = rectangles [i];
			rectsToErase [i] = new Rectangle (current.x, current.y, current.width, current.height);
		}
		Event event = new Event ();
		if (parent == null) {
			Rectangle eventRect = new Rectangle (newX [0], newY [0], 0, 0);
			event.setBounds (DPIUtil.autoScaleDown (eventRect));
		} else {
			Point screenCoord = display.mapInPixels (parent, null, newX [0], newY [0]);
			Rectangle eventRect = new Rectangle (screenCoord.x, screenCoord.y, 0, 0);
			event.setBounds (DPIUtil.autoScaleDown (eventRect));
		}
		if ((style & SWT.RESIZE) != 0) {
			resizeRectangles (newX [0] - oldX, newY [0] - oldY);
			sendEvent (SWT.Resize, event);
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the resize
			* event.  If this happens, return false to indicate
			* that the tracking has failed.
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
				drawRectangles (rectangles);
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
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the move
			* event.  If this happens, return false to indicate
			* that the tracking has failed.
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
				drawRectangles (rectangles);
			}
		}
		oldX = newX [0];
		oldY = newY [0];
	}
	eventType = Control.fixGdkEventTypeValues(eventType);
	tracking = eventType != GDK.GDK_BUTTON_RELEASE;
	return 0;
}

void moveRectangles (int xChange, int yChange) {
	if (bounds == null) return;
	if (xChange < 0 && ((style & SWT.LEFT) == 0)) xChange = 0;
	if (xChange > 0 && ((style & SWT.RIGHT) == 0)) xChange = 0;
	if (yChange < 0 && ((style & SWT.UP) == 0)) yChange = 0;
	if (yChange > 0 && ((style & SWT.DOWN) == 0)) yChange = 0;
	if (xChange == 0 && yChange == 0) return;
	if (parent != null && (parent.style & SWT.MIRRORED) != 0) xChange *= -1;
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
	if (!GTK.GTK4) window = GDK.gdk_get_default_root_window();
	if (parent != null) {
		window = gtk_widget_get_window (parent.paintHandle());
	}
	if (window == 0) return false;
	cancelled = false;
	tracking = true;
	int [] oldX = new int [1], oldY = new int [1], state = new int [1];
	if (GTK.GTK4) {
		double [] oldXDouble = new double [1], oldYDouble = new double [1];
		display.getPointerPosition(oldXDouble, oldYDouble);

		oldX[0] = (int) oldXDouble[0];
		oldY[0] = (int) oldYDouble[0];
	} else {
		display.getWindowPointerPosition (window, oldX, oldY, state);
	}

	/*
	* if exactly one of UP/DOWN is specified as a style then set the cursor
	* orientation accordingly (the same is done for LEFT/RIGHT styles below)
	*/
	int vStyle = style & (SWT.UP | SWT.DOWN);
	if (vStyle == SWT.UP || vStyle == SWT.DOWN) {
		cursorOrientation |= vStyle;
	}
	int hStyle = style & (SWT.LEFT | SWT.RIGHT);
	if (hStyle == SWT.LEFT || hStyle == SWT.RIGHT) {
		cursorOrientation |= hStyle;
	}

	int mask = GDK.GDK_BUTTON1_MASK | GDK.GDK_BUTTON2_MASK | GDK.GDK_BUTTON3_MASK;
	boolean mouseDown = (state [0] & mask) != 0;
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

	grabbed = grab ();
	lastCursor = this.cursor != null ? this.cursor.handle : 0;

	cachedCombinedDisplayResolution = Display.getDefault().getBounds(); // In case resolution was changed during run time.
	overlay = GTK3.gtk_window_new (GTK.GTK_WINDOW_POPUP);
	GTK3.gtk_window_set_skip_taskbar_hint (overlay, true);
	GTK.gtk_window_set_title (overlay, new byte [1]);
	if (parent != null) GTK.gtk_window_set_transient_for(overlay, parent.topHandle());
	GTK.gtk_widget_realize (overlay);
	if (!GTK.GTK4) {
		long overlayWindow = GTK3.gtk_widget_get_window (overlay);
		GDK.gdk_window_set_override_redirect (overlayWindow, true);
	}
	setTrackerBackground(true);
	Rectangle bounds = display.getBoundsInPixels();
	GTK3.gtk_window_move (overlay, bounds.x, bounds.y);
	GTK3.gtk_window_resize (overlay, bounds.width, bounds.height);
	GTK.gtk_widget_show (overlay);

	/* Tracker behaves like a Dialog with its own OS event loop. */
	Display display = this.display;
	Tracker oldTracker = display.tracker;
	display.tracker = this;
	try {
		while (tracking) {
			if (parent != null && parent.isDisposed ()) break;
			display.runSkin ();
			display.runDeferredLayouts ();
			display.sendPreExternalEventDispatchEvent ();
			if (GTK.GTK4) {
				OS.g_main_context_iteration (0, true);
			} else {
				GTK3.gtk_main_iteration_do (true);
			}
			display.sendPostExternalEventDispatchEvent ();
			display.runAsyncMessages (false);
		}
	} finally {
		display.tracker = oldTracker;
	}
	ungrab ();
	if (overlay != 0) {
		GTK3.gtk_widget_destroy (overlay);
		overlay = 0;
	}
	window = 0;
	return !cancelled;
}

private void setTrackerBackground(boolean opaque) {
	if (cachedBackgroundIsOpaque == null || cachedBackgroundIsOpaque.booleanValue() != opaque) {
		cachedBackgroundIsOpaque = Boolean.valueOf(opaque);
	} else if (opaque == cachedBackgroundIsOpaque.booleanValue()) {
		return;
	}
	String css;
	if (opaque) {
		GTK.gtk_widget_set_opacity (overlay, 1.0);
		css = "window {background-color: rgb(0,0,0);}";
	} else {
		GTK.gtk_widget_set_opacity (overlay, 0.0);
		css = "window {  "
				+ "border-top-color: transparent;"
				+ "border-left-color: transparent;"
				+ "border-right-color: transparent;"
				+ "border-bottom-color: transparent;}";
	}
	long context = GTK.gtk_widget_get_style_context (overlay);
	if (provider == 0) {
		provider = GTK.gtk_css_provider_new ();
		GTK.gtk_style_context_add_provider (context, provider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
		OS.g_object_unref (provider);
	}
	if (GTK.GTK4) {
		GTK4.gtk_css_provider_load_from_data (provider, Converter.wcsToMbcs (css, true), -1);
	} else {
		GTK3.gtk_css_provider_load_from_data (provider, Converter.wcsToMbcs (css, true), -1, null);
		GTK3.gtk_style_context_invalidate (context);
	}

	long region = Cairo.cairo_region_create ();

	if (GTK.GTK4) {
		//TODO: GTK4
		//GDK.gdk_surface_set_opaque_region(context, region);
		//GDK.gdk_surface_set_input_region(context, region);
	} else {
		GTK3.gtk_widget_shape_combine_region (overlay, region);
		GTK3.gtk_widget_input_shape_combine_region (overlay, region);
	}

	Cairo.cairo_region_destroy (region);
}

boolean processEvent (long eventPtr) {
	int eventType = GDK.gdk_event_get_event_type(eventPtr);
	eventType = Control.fixGdkEventTypeValues(eventType);
	long widget = GTK3.gtk_get_event_widget (eventPtr);
	switch (eventType) {
		case GDK.GDK_MOTION_NOTIFY: gtk_motion_notify_event (widget, eventPtr); break;
		case GDK.GDK_BUTTON_RELEASE: gtk_button_release_event (widget, eventPtr); break;
		case GDK.GDK_KEY_PRESS: gtk_key_press_event (widget, eventPtr); break;
		case GDK.GDK_KEY_RELEASE: gtk_key_release_event (widget, eventPtr); break;
		case GDK.GDK_BUTTON_PRESS:
		case GDK.GDK_2BUTTON_PRESS:
		case GDK.GDK_3BUTTON_PRESS:
		case GDK.GDK_ENTER_NOTIFY:
		case GDK.GDK_LEAVE_NOTIFY:
			/* Do not dispatch these */
			break;
		case GDK.GDK_EXPOSE:
			update ();
			GTK3.gtk_main_do_event (eventPtr);
			break;
		default:
			return true;
	}
	return false;
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
	if (parent != null && (parent.style & SWT.MIRRORED) != 0) xChange *= -1;
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
public void setCursor (Cursor newCursor) {
	checkWidget ();
	cursor = newCursor;
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
	int length = rectangles.length;
	for (int i = 0; i < length; i++) {
		rectangles [i] = DPIUtil.autoScaleUp (rectangles [i]);
	}
	setRectanglesInPixels (rectangles);
}

void setRectanglesInPixels (Rectangle [] rectangles) {
	checkWidget();
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
	checkWidget();
	this.stippled = stippled;
}

void ungrab () {
	if (grabbed) gdk_pointer_ungrab (window, GDK.GDK_CURRENT_TIME);
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

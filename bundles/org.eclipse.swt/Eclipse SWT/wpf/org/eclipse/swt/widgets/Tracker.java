/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.internal.wpf.*;
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
	boolean tracking, cancelled, stippled;
	Rectangle [] rectangles, proportions;
	Rectangle bounds;
	int resizeCursor, clientCursor, cursorOrientation = SWT.NONE;
	int oldX, oldY;
	int [] rectShapes;
	int canvasHandle;
	
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
	int newX = bounds.x + bounds.width / 2;
	int newY = bounds.y;
	if (parent != null) {
		int point = OS.gcnew_Point (newX, newY);
		int result = OS.Visual_PointToScreen (handle, point);
		newX = (int) OS.Point_X(result);
		newY = (int) OS.Point_Y(result);
		OS.GCHandle_Free (point);
		OS.GCHandle_Free (result);
	}
	OS.SetCursorPos (newX, newY);	
	return new Point (newX, newY);
}

Point adjustResizeCursor () {
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

	if (parent != null) {
		int point = OS.gcnew_Point (newX, newY);
		int result = OS.Visual_PointToScreen (handle, point);
		newX = (int) OS.Point_X(result);
		newY = (int) OS.Point_Y(result);
		OS.GCHandle_Free (point);
		OS.GCHandle_Free (result);
	}
	OS.SetCursorPos (newX, newY);

	/*
	* If the client has not provided a custom cursor then determine
	* the appropriate resize cursor.
	*/
	if (clientCursor == 0) {
		int newCursor = 0;
		switch (cursorOrientation) {
			case SWT.UP:
				newCursor = OS.Cursors_SizeNS();
				break;
			case SWT.DOWN:
				newCursor = OS.Cursors_SizeNS();
				break;
			case SWT.LEFT:
				newCursor = OS.Cursors_SizeWE();
				break;
			case SWT.RIGHT:
				newCursor = OS.Cursors_SizeWE();
				break;
			case SWT.LEFT | SWT.UP:
				newCursor = OS.Cursors_SizeNWSE();
				break;
			case SWT.RIGHT | SWT.DOWN:
				newCursor = OS.Cursors_SizeNWSE();
				break;
			case SWT.LEFT | SWT.DOWN:
				newCursor = OS.Cursors_SizeNESW();
				break;
			case SWT.RIGHT | SWT.UP:
				newCursor = OS.Cursors_SizeNESW();
				break;
			default:
				newCursor = OS.Cursors_SizeAll();
				break;
		}
		OS.FrameworkElement_Cursor (handle, newCursor);
		if (resizeCursor != 0) {
			OS.GCHandle_Free (resizeCursor);
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
	return result;
}

/**
 * Draw the rectangles displayed by the tracker.
 */
void drawRectangles () {
	for (int i = 0; i < rectShapes.length; i++) {
		Rectangle rect = rectangles [i];
		int child = rectShapes [i];
		OS.Canvas_SetLeft (child, rect.x);
		OS.Canvas_SetTop (child, rect.y);
		OS.FrameworkElement_Width (child, rect.width);
		OS.FrameworkElement_Height (child, rect.height);
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
	int length = 0;
	if (rectangles != null) length = rectangles.length;
	Rectangle [] result = new Rectangle [length];
	for (int i = 0; i < length; i++) {
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

void HandleKeyUp (int sender, int e) {
	if (!sendKeyEvent(SWT.KeyUp, e, false)) return;
}

void HandleKeyDown (int sender, int e) {
	if (!sendKeyEvent(SWT.KeyDown, e, false)) return;
	
	boolean ctrlDown = (OS.Keyboard_Modifiers() & OS.ModifierKeys_Control) != 0;
	int stepSize = ctrlDown ? STEPSIZE_SMALL : STEPSIZE_LARGE;
	int key = OS.KeyEventArgs_Key(e);
	int xChange = 0, yChange = 0;
	switch (key) {
		case OS.Key_System:
		case OS.Key_Escape:
			cancelled = true;
			tracking = false;
			break;
		case OS.Key_Return:
			tracking = false;
			break;
		case OS.Key_Left:
			xChange = -stepSize;
			break;
		case OS.Key_Right:
			xChange = stepSize;
			break;
		case OS.Key_Up:
			yChange = -stepSize;
			break;
		case OS.Key_Down:
			yChange = stepSize;
			break;
	}
	if (xChange != 0 || yChange != 0) {
		Event event = new Event ();
		event.x = oldX + xChange;
		event.y = oldY + yChange;
		Point cursorPos;
		if ((style & SWT.RESIZE) != 0) {
			resizeRectangles (xChange, yChange);
			sendEvent (SWT.Resize, event);
			if (isDisposed ()) {
				cancelled = true;
				return;
			}
			drawRectangles ();
			cursorPos = adjustResizeCursor ();
		} else {
			moveRectangles (xChange, yChange);
			sendEvent (SWT.Move, event);
			if (isDisposed ()) {
				cancelled = true;
				return;
			}
			drawRectangles ();
			cursorPos = adjustMoveCursor ();
		}
		oldX = cursorPos.x;
		oldY = cursorPos.y;
	}
}

void HandleMouseUp (int sender, int e) {
	if (!sendMouseEvent(SWT.MouseUp, e, false)) return;
	tracking = false;
}

void HandleMouseDown (int sender, int e) {
	if (!sendMouseEvent(SWT.MouseDown, e, false)) return;
	tracking = false;
}

void HandleMouseMove (int sender, int e) {
	if (!sendMouseEvent(SWT.MouseMove, e, false)) return;
	int point = OS.MouseEventArgs_GetPosition (e, handle);
	int newX = (int) OS.Point_X (point);
	int newY = (int) OS.Point_Y (point);
	OS.GCHandle_Free (point);
	if (newX != oldX || newY != oldY) {
		Event event = new Event ();
		event.x = newX;
		event.y = newY;
		if ((style & SWT.RESIZE) != 0) {
			resizeRectangles (newX - oldX, newY - oldY);
			sendEvent (SWT.Resize, event);
			if (isDisposed ()) {
				cancelled = true;
			}
			drawRectangles ();
			Point cursorPos = adjustResizeCursor ();
			if (cursorPos != null) {
				newX = cursorPos.x;  
				newY = cursorPos.y;
			}
		} else {
			moveRectangles (newX - oldX, newY - oldY);
			sendEvent (SWT.Move, event);
			if (isDisposed ()) {
				cancelled = true;
			}
			drawRectangles ();
		}
		oldX = newX;
		oldY = newY;
	}
}

void moveRectangles (int xChange, int yChange) {
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
	if (rectangles == null) return false;
	if (rectangles.length == 0) return false;
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
	
	handle = OS.gcnew_Window ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	canvasHandle = OS.gcnew_Canvas ();
	if (canvasHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.ContentControl_Content (handle, canvasHandle);
	if (parent != null) {
		Rectangle bounds = parent.getBounds ();//	wrong
		OS.Window_Left (handle, bounds.x);
		OS.Window_Top (handle, bounds.y);
		OS.FrameworkElement_Width (handle, bounds.width);
		OS.FrameworkElement_Height (handle, bounds.height);
	} else {
		OS.Window_Left (handle, 0);
		OS.Window_Top (handle, 0);
		OS.FrameworkElement_Width (handle, OS.SystemParameters_PrimaryScreenWidth ());
		OS.FrameworkElement_Height (handle, OS.SystemParameters_PrimaryScreenHeight ());
	}
	OS.Window_ShowInTaskbar (handle, false);
	OS.Window_AllowsTransparency (handle, true);
	OS.Window_ResizeMode (handle, OS.ResizeMode_NoResize);
	OS.Window_WindowStyle (handle, OS.WindowStyle_None);
	if (clientCursor != 0) OS.FrameworkElement_Cursor (handle, clientCursor);
	int color = OS.Colors_Black;
	int brush = OS.gcnew_SolidColorBrush (color);
	OS.Brush_Opacity (brush, 0.01);
	OS.Control_Background (handle, brush);
	OS.GCHandle_Free (brush);
	
	int children = OS.Panel_Children (canvasHandle);
	int stroke = stippled ? 3 : 1;
	brush = stippled ? OS.Brushes_Navy() : OS.Brushes_Black();
	rectShapes = new int[rectangles.length];
	for (int i = 0; i < rectShapes.length; i++) {
		int child = rectShapes [i] = OS.gcnew_Rectangle ();
		OS.UIElementCollection_Add (children, child);
		OS.Shape_StrokeThickness(child, stroke);
		OS.Shape_Stroke(child, brush);
	}
	OS.GCHandle_Free(brush);
	drawRectangles ();
	
	jniRef = OS.NewGlobalRef (this);
	if (jniRef == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int handler = OS.gcnew_KeyEventHandler (jniRef, "HandleKeyDown");
	OS.UIElement_KeyDown (handle, handler);	
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_KeyEventHandler (jniRef, "HandleKeyUp");
	OS.UIElement_KeyUp (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_MouseEventHandler (jniRef, "HandleMouseMove");
	OS.UIElement_MouseMove (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_MouseButtonEventHandler (jniRef, "HandleMouseUp");
	OS.UIElement_MouseUp (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_MouseButtonEventHandler (jniRef, "HandleMouseDown");
	OS.UIElement_MouseDown (handle, handler);
	OS.GCHandle_Free (handler);
	
	OS.UIElement_Focus (handle);
	OS.Window_Show (handle);
	
	boolean mouseDown = OS.Mouse_LeftButton() == OS.MouseButtonState_Pressed;
	Point cursorPos;
	if (mouseDown) {
		int point = OS.Mouse_GetPosition (handle);
		cursorPos = new Point ((int) OS.Point_X (point), (int) OS.Point_Y (point));
		OS.GCHandle_Free (point);
	} else {
		if ((style & SWT.RESIZE) != 0) {
			cursorPos = adjustResizeCursor ();
		} else {
			cursorPos = adjustMoveCursor ();
		}
	}
	oldX = cursorPos.x;
	oldY = cursorPos.y;
	
	/* Tracker behaves like a Dialog with its own OS event loop. */
	Display display = Display.getCurrent();
	while (tracking && !cancelled) {
		if (!display.readAndDispatch()) {
			display.sleep();
		}
	}

	for (int i = 0; i < rectShapes.length; i++) {
		int child = rectShapes [i];
		OS.UIElementCollection_Remove (children, child);
		OS.GCHandle_Free (child);
	}
	OS.GCHandle_Free (children);
	if (resizeCursor != 0) {
		OS.GCHandle_Free (resizeCursor);
		resizeCursor = 0;
	}
	OS.Window_Close (handle);
	OS.GCHandle_Free (canvasHandle);
	OS.GCHandle_Free (handle);
	jniRef = 0;
	OS.DeleteGlobalRef (jniRef);
	handle = 0; 
	rectShapes = null;
	tracking = false;
	
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.KeyUp, listener);
	eventTable.unhook (SWT.KeyDown, listener);
}

void resizeRectangles (int xChange, int yChange) {
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
	clientCursor = 0;
	if (newCursor != null) {
		clientCursor = newCursor.handle;
		if (handle != 0) {
			OS.FrameworkElement_Cursor (handle, clientCursor);
		}
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
	if (handle != 0) {
		//TODO CHANGE RECT BRUSH
	}
}

}

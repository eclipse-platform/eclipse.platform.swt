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


import org.eclipse.swt.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of the receiver represent a selectable user interface object
 * that allows the user to drag a rubber banded outline of the sash within
 * the parent control.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class Sash extends Control {
	boolean dragging;
	int startX, startY, lastX, lastY;
	int cursor;

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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#HORIZONTAL
 * @see SWT#VERTICAL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Sash (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the x, y, width, and height fields of the event object are valid.
 * If the reciever is being dragged, the event object detail field contains the value <code>SWT.DRAG</code>.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
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
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	if ((style & SWT.HORIZONTAL) != 0) {
		width += DEFAULT_WIDTH;  height += 3;
	} else {
		width += 3; height += DEFAULT_HEIGHT;
	}
	if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
	if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
	return new Point (width, height);
}

void createHandle (int index) {
	state |= HANDLE;
	handle = OS.gtk_drawing_area_new ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_add (parentHandle, handle);
	OS.gtk_widget_show (handle);
	int type = (style & SWT.VERTICAL) != 0 ? OS.GDK_SB_H_DOUBLE_ARROW : OS.GDK_SB_V_DOUBLE_ARROW;
	cursor = OS.gdk_cursor_new (type);
	int flags = OS.GTK_WIDGET_FLAGS (handle);
	if ((flags & OS.GTK_REALIZED) != 0) { 
		int window = OS.GTK_WIDGET_WINDOW (handle);
		OS.gdk_window_set_cursor (window, cursor);
	}
}

void drawBand (int x, int y, int width, int height) {
	int window = OS.GTK_WIDGET_WINDOW (parent.paintHandle());
	if (window == 0) return;
	byte [] bits = {-86, 85, -86, 85, -86, 85, -86, 85};
	int stipplePixmap = OS.gdk_bitmap_create_from_data (window, bits, 8, 8);
	int gc = OS.gdk_gc_new (window);
	int colormap = OS.gdk_colormap_get_system();
	GdkColor color = new GdkColor ();
	OS.gdk_color_white (colormap, color);
	OS.gdk_gc_set_foreground (gc, color);	
	OS.gdk_gc_set_stipple (gc, stipplePixmap);
	OS.gdk_gc_set_subwindow (gc, OS.GDK_INCLUDE_INFERIORS);
	OS.gdk_gc_set_fill (gc, OS.GDK_STIPPLED);
	OS.gdk_gc_set_function (gc, OS.GDK_XOR);
	OS.gdk_draw_rectangle (window, gc, 1, x, y, width, height);	
	OS.g_object_unref (stipplePixmap);
	OS.g_object_unref (gc);
}

int gtk_button_press_event (int widget, int eventPtr) {
	int result = super.gtk_button_press_event (widget, eventPtr);
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, eventPtr, GdkEventButton.sizeof);
	int button = gdkEvent.button;
	if (button != 1) return 0;
	startX = (int) gdkEvent.x; 
	startY = (int) gdkEvent.y;
	int border = 0;
	int x = OS.GTK_WIDGET_X (handle);
	int y = OS.GTK_WIDGET_Y (handle);
	int width = OS.GTK_WIDGET_WIDTH (handle);
	int height = OS.GTK_WIDGET_HEIGHT (handle);
	lastX = x - border; 
	lastY = y - border;
	/* The event must be sent because its doit flag is used. */
	Event event = new Event ();
	event.detail = SWT.DRAG;
	event.time = gdkEvent.time;
	event.x = lastX;
	event.y = lastY;
	event.width = width;
	event.height = height;
	/*
	 * It is possible (but unlikely) that client code could have disposed
	 * the widget in the selection event.  If this happens end the processing
	 * of this message by returning.
	 */
	sendEvent (SWT.Selection, event);
	if (isDisposed ()) return 0;
	if (event.doit) {
		dragging = true;
//		OS.XmUpdateDisplay (handle);
		drawBand (lastX = event.x, lastY = event.y, width, height);
	}
	return result;	
}

int gtk_button_release_event (int widget, int eventPtr) {
	int result = super.gtk_button_release_event (widget, eventPtr);
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, eventPtr, GdkEventButton.sizeof);
	int button = gdkEvent.button;
	if (button != 1) return 0;
	if (!dragging) return 0;
	dragging = false;
	int width = OS.GTK_WIDGET_WIDTH (handle);
	int height = OS.GTK_WIDGET_HEIGHT (handle);
	/* The event must be sent because its doit flag is used. */
	Event event = new Event ();
	event.time = gdkEvent.time;
	event.x = lastX;
	event.y = lastY;
	event.width = width;
	event.height = height;
	drawBand (lastX, lastY, width, height);
	sendEvent (SWT.Selection, event);
	/* widget could be disposed here */
	return result;
}

int gtk_motion_notify_event (int widget, int eventPtr) {
	int result = super.gtk_motion_notify_event (widget, eventPtr);
	int [] state = new int [1];
	OS.gdk_event_get_state (eventPtr, state);
	if (!dragging || (state [0] & OS.GDK_BUTTON1_MASK) == 0) return 0;
	int x = OS.GTK_WIDGET_X (handle);
	int y = OS.GTK_WIDGET_Y (handle);
	int width = OS.GTK_WIDGET_WIDTH (handle);
	int height = OS.GTK_WIDGET_HEIGHT (handle);
	int parentBorder = 0;
	int parentWidth = OS.GTK_WIDGET_WIDTH (parent.handle);
	int parentHeight = OS.GTK_WIDGET_HEIGHT (parent.handle);
	double[] px = new double [1], py = new double [1];
	OS.gdk_event_get_coords (eventPtr, px, py);
	int newX = lastX, newY = lastY;
	if ((style & SWT.VERTICAL) != 0) {
		newX = Math.min (Math.max (0, (int)px [0] + x - startX - parentBorder), parentWidth - width);
	} else {
		newY = Math.min (Math.max (0, (int)py[0] + y - startY - parentBorder), parentHeight - height);
	}
	if (newX == lastX && newY == lastY) return 0;
	drawBand (lastX, lastY, width, height);
	/* The event must be sent because its doit flag is used. */
	Event event = new Event ();
	event.detail = SWT.DRAG;
	event.time = OS.gdk_event_get_time (eventPtr);
	event.x = newX;  event.y = newY;
	event.width = width;  event.height = height;
	/*
	 * It is possible (but unlikely) that client code could have disposed
	 * the widget in the selection event.  If this happens end the processing
	 * of this message by returning.
	 */
	sendEvent (SWT.Selection, event);
	if (isDisposed ()) return 0;
	if (event.doit) {
		lastX = event.x;  lastY = event.y;
//		OS.XmUpdateDisplay (handle);
		drawBand (lastX, lastY, width, height);
	}
	return result;
}

int gtk_realize (int widget) {
	int window = OS.GTK_WIDGET_WINDOW (handle);
	if (window == 0 || cursor == 0) return 0;
	OS.gdk_window_set_cursor (window, cursor);
	return 0;	
}

void hookEvents () {
	super.hookEvents ();
	Display display = getDisplay ();
	int windowProc2 = display.windowProc2;
	OS.g_signal_connect_after (handle, OS.realize, windowProc2, REALIZE);
}	

void releaseWidget () {
	super.releaseWidget ();
	if (cursor != 0) OS.gdk_cursor_destroy (cursor);
	cursor = 0;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
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
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

}

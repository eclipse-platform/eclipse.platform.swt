/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * <dd>HORIZONTAL, VERTICAL, SMOOTH</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#sash">Sash snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Sash extends Control {
	boolean dragging;
	int startX, startY, lastX, lastY;
	long /*int*/ defaultCursor;

	private final static int INCREMENT = 1;
	private final static int PAGE_INCREMENT = 9;

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
 * @see SWT#SMOOTH
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Sash (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the x, y, width, and height fields of the event object are valid.
 * If the receiver is being dragged, the event object detail field contains the value <code>SWT.DRAG</code>.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user
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
	if (OS.USE_CAIRO) {
		style |= SWT.SMOOTH;
	}
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
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
	state |= HANDLE | THEME_BACKGROUND;
	handle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	gtk_widget_set_has_window (handle, true);
	gtk_widget_set_can_focus (handle, true);
	int type = (style & SWT.VERTICAL) != 0 ? OS.GDK_SB_H_DOUBLE_ARROW : OS.GDK_SB_V_DOUBLE_ARROW;
	defaultCursor = OS.gdk_cursor_new (type);
}

void drawBand (int x, int y, int width, int height) {
	if ((style & SWT.SMOOTH) != 0) return;
	long /*int*/ window = gtk_widget_get_window (parent.paintHandle());
	if (window == 0) return;
	byte [] bits = {-86, 85, -86, 85, -86, 85, -86, 85};
	long /*int*/ stipplePixmap = OS.gdk_bitmap_create_from_data (window, bits, 8, 8);
	long /*int*/ gc = OS.gdk_gc_new (window);
	long /*int*/ colormap = OS.gdk_colormap_get_system();
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

long /*int*/ gtk_button_press_event (long /*int*/ widget, long /*int*/ eventPtr) {
	long /*int*/ result = super.gtk_button_press_event (widget, eventPtr);
	if (result != 0) return result;
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, eventPtr, GdkEventButton.sizeof);
	int button = gdkEvent.button;
	if (button != 1) return 0;
	if (gdkEvent.type == OS.GDK_2BUTTON_PRESS) return 0;
	if (gdkEvent.type == OS.GDK_3BUTTON_PRESS) return 0;
	long /*int*/ window = gtk_widget_get_window (widget);
	int [] origin_x = new int [1], origin_y = new int [1];
	OS.gdk_window_get_origin (window, origin_x, origin_y);
	startX = (int) (gdkEvent.x_root - origin_x [0]);
	startY = (int) (gdkEvent.y_root - origin_y [0]);
	GtkAllocation allocation = new GtkAllocation ();
	gtk_widget_get_allocation(handle, allocation);
	int x = allocation.x;
	int y = allocation.y;
	int width = allocation.width;
	int height = allocation.height;
	lastX = x; 
	lastY = y;
	Event event = new Event ();
	event.time = gdkEvent.time;
	event.x = lastX;
	event.y = lastY;
	event.width = width;
	event.height = height;
	if ((style & SWT.SMOOTH) == 0) {
		event.detail = SWT.DRAG;
	}
	if ((parent.style & SWT.MIRRORED) != 0) event.x = parent.getClientWidth () - width  - event.x;
	sendSelectionEvent (SWT.Selection, event, true);
	if (isDisposed ()) return 0;
	if (event.doit) {
		dragging = true;
		lastX = event.x;
		lastY = event.y;
		if ((parent.style & SWT.MIRRORED) != 0) lastX = parent.getClientWidth () - width  - lastX;
		parent.update (true, (style & SWT.SMOOTH) == 0);
		drawBand (lastX, event.y, width, height);
		if ((style & SWT.SMOOTH) != 0) {
			setBounds (event.x, event.y, width, height);
			// widget could be disposed at this point
		}
	}
	return result;	
}

long /*int*/ gtk_button_release_event (long /*int*/ widget, long /*int*/ eventPtr) {
	long /*int*/ result = super.gtk_button_release_event (widget, eventPtr);
	if (result != 0) return result;
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, eventPtr, GdkEventButton.sizeof);
	int button = gdkEvent.button;
	if (button != 1) return 0;
	if (!dragging) return 0;
	dragging = false;
	GtkAllocation allocation = new GtkAllocation ();
	gtk_widget_get_allocation (handle, allocation);
	int width = allocation.width;
	int height = allocation.height;
	Event event = new Event ();
	event.time = gdkEvent.time;
	event.x = lastX;
	event.y = lastY;
	event.width = width;
	event.height = height;
	drawBand (lastX, lastY, width, height);
	if ((parent.style & SWT.MIRRORED) != 0) event.x = parent.getClientWidth () - width  - event.x;
	sendSelectionEvent (SWT.Selection, event, true);
	if (isDisposed ()) return result;
	if (event.doit) {
		if ((style & SWT.SMOOTH) != 0) {
			setBounds (event.x, event.y, width, height);
			// widget could be disposed at this point
		}
	}
	return result;
}

long /*int*/ gtk_focus_in_event (long /*int*/ widget, long /*int*/ event) {
	long /*int*/ result = super.gtk_focus_in_event (widget, event);
	if (result != 0) return result;
	// widget could be disposed at this point
	if (handle != 0) {
		GtkAllocation allocation = new GtkAllocation ();
		gtk_widget_get_allocation (handle, allocation);
		lastX = allocation.x;
		lastY = allocation.y;	
	}
	return 0;
}

long /*int*/ gtk_key_press_event (long /*int*/ widget, long /*int*/ eventPtr) {
	long /*int*/ result = super.gtk_key_press_event (widget, eventPtr);
	if (result != 0) return result;
	GdkEventKey gdkEvent = new GdkEventKey ();
	OS.memmove (gdkEvent, eventPtr, GdkEventKey.sizeof);
	int keyval = gdkEvent.keyval;
	switch (keyval) {
		case OS.GDK_Left:
		case OS.GDK_Right:
		case OS.GDK_Up:
		case OS.GDK_Down:
			int xChange = 0, yChange = 0;
			int stepSize = PAGE_INCREMENT;
			if ((gdkEvent.state & OS.GDK_CONTROL_MASK) != 0) stepSize = INCREMENT;
			if ((style & SWT.VERTICAL) != 0) {
				if (keyval == OS.GDK_Up || keyval == OS.GDK_Down) break;
				xChange = keyval == OS.GDK_Left ? -stepSize : stepSize;
			} else {
				if (keyval == OS.GDK_Left ||keyval == OS.GDK_Right) break;
				yChange = keyval == OS.GDK_Up ? -stepSize : stepSize;
			}
			int parentBorder = 0;
			GtkAllocation allocation = new GtkAllocation ();
			gtk_widget_get_allocation (handle, allocation);
			int width = allocation.width;
			int height = allocation.height;
			gtk_widget_get_allocation (parent.handle, allocation);
			int parentWidth = allocation.width;
			int parentHeight = allocation.height;
			int newX = lastX, newY = lastY;
			if ((style & SWT.VERTICAL) != 0) {
				newX = Math.min (Math.max (0, lastX + xChange - parentBorder - startX), parentWidth - width);
			} else {
				newY = Math.min (Math.max (0, lastY + yChange - parentBorder - startY), parentHeight - height);
			}
			if (newX == lastX && newY == lastY) return result;
			
			/* Ensure that the pointer image does not change */
			long /*int*/ window = gtk_widget_get_window (handle);
			int grabMask = OS.GDK_POINTER_MOTION_MASK | OS.GDK_BUTTON_RELEASE_MASK;
			long /*int*/ gdkCursor = cursor != null ? cursor.handle : defaultCursor;
			int ptrGrabResult = gdk_pointer_grab (window, OS.GDK_OWNERSHIP_NONE, false, grabMask, window, gdkCursor, OS.GDK_CURRENT_TIME);

			/* The event must be sent because its doit flag is used. */
			Event event = new Event ();
			event.time = gdkEvent.time;
			event.x = newX;
			event.y = newY;
			event.width = width;
			event.height = height;
			if ((parent.style & SWT.MIRRORED) != 0) event.x = parent.getClientWidth () - width  - event.x;
			sendSelectionEvent (SWT.Selection, event, true);
			if (ptrGrabResult == OS.GDK_GRAB_SUCCESS) OS.gdk_pointer_ungrab (OS.GDK_CURRENT_TIME);
			if (isDisposed ()) break;
			
			if (event.doit) {
				lastX = event.x;
				lastY = event.y;
				if ((parent.style & SWT.MIRRORED) != 0) lastX = parent.getClientWidth () - width  - lastX;
				if ((style & SWT.SMOOTH) != 0) {
					setBounds (event.x, event.y, width, height);
					if (isDisposed ()) break;
				}
				int cursorX = event.x, cursorY = event.y;
				if ((style & SWT.VERTICAL) != 0) {
					cursorY += height / 2;
				} else {
					cursorX += width / 2;
				}
				display.setCursorLocation (parent.toDisplay (cursorX, cursorY));
			}
			break;
	}

	return result;
}

long /*int*/ gtk_motion_notify_event (long /*int*/ widget, long /*int*/ eventPtr) {
	long /*int*/ result = super.gtk_motion_notify_event (widget, eventPtr);
	if (result != 0) return result;
	if (!dragging) return 0;
	GdkEventMotion gdkEvent = new GdkEventMotion ();
	OS.memmove (gdkEvent, eventPtr, GdkEventButton.sizeof);
	int eventX, eventY, eventState;
	if (gdkEvent.is_hint != 0) {
		int [] pointer_x = new int [1], pointer_y = new int [1], mask = new int [1];
		OS.gdk_window_get_pointer (gdkEvent.window, pointer_x, pointer_y, mask);
		eventX = pointer_x [0];
		eventY = pointer_y [0];
		eventState = mask [0];
	} else {
		int [] origin_x = new int [1], origin_y = new int [1];
		OS.gdk_window_get_origin (gdkEvent.window, origin_x, origin_y);	
		eventX = (int) (gdkEvent.x_root - origin_x [0]);
		eventY = (int) (gdkEvent.y_root - origin_y [0]);
		eventState = gdkEvent.state;
	}
	if ((eventState & OS.GDK_BUTTON1_MASK) == 0) return 0;
	GtkAllocation allocation = new GtkAllocation ();
	gtk_widget_get_allocation (handle, allocation);
	int x = allocation.x;
	int y = allocation.y;
	int width = allocation.width;
	int height = allocation.height;
	int parentBorder = 0;
	gtk_widget_get_allocation (parent.handle, allocation);
	int parentWidth = allocation.width;
	int parentHeight = allocation.height;
	int newX = lastX, newY = lastY;
	if ((style & SWT.VERTICAL) != 0) {
		newX = Math.min (Math.max (0, eventX + x - startX - parentBorder), parentWidth - width);
	} else {
		newY = Math.min (Math.max (0, eventY + y - startY - parentBorder), parentHeight - height);
	}
	if (newX == lastX && newY == lastY) return 0;
	drawBand (lastX, lastY, width, height);
	
	Event event = new Event ();
	event.time = gdkEvent.time;
	event.x = newX;
	event.y = newY;
	event.width = width;
	event.height = height;
	if ((style & SWT.SMOOTH) == 0) {
		event.detail = SWT.DRAG;
	}
	if ((parent.style & SWT.MIRRORED) != 0) event.x = parent.getClientWidth() - width  - event.x;
	sendSelectionEvent (SWT.Selection, event, true);
	if (isDisposed ()) return 0;
	if (event.doit) {
		lastX = event.x;
		lastY = event.y;
		if ((parent.style & SWT.MIRRORED) != 0) lastX = parent.getClientWidth () - width  - lastX;
	}
	parent.update (true, (style & SWT.SMOOTH) == 0);
	drawBand (lastX, lastY, width, height);
	if ((style & SWT.SMOOTH) != 0) {
		setBounds (event.x, lastY, width, height);
		// widget could be disposed at this point
	}
	return result;
}

long /*int*/ gtk_realize (long /*int*/ widget) {
	setCursor (cursor != null ? cursor.handle : 0);
	return super.gtk_realize (widget);
}

void hookEvents () {
	super.hookEvents ();
	OS.gtk_widget_add_events (handle, OS.GDK_POINTER_MOTION_HINT_MASK);
}

void releaseWidget () {
	super.releaseWidget ();
	if (defaultCursor != 0) OS.gdk_cursor_unref (defaultCursor);
	defaultCursor = 0;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
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

void setCursor (long /*int*/ cursor) {
	super.setCursor (cursor != 0 ? cursor : defaultCursor);
}

int traversalCode (int key, GdkEventKey event) {
	return 0;
}

}

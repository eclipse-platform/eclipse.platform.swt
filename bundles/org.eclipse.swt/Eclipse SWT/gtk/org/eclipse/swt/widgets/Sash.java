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
 * Instances of the receiver represent a selectable user interface object
 * that allows the user to drag a rubber banded outline of the sash within
 * the parent control.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd> HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class Sash extends Control {
	boolean dragging;
	int originX, originY;
	int lastX, lastY; /* relative to the receiver, not the parent */
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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Sash (Composite parent, int style) {
	super (parent, checkStyle (style));
}

void createHandle (int index) {
	state |= HANDLE;
	handle = OS.gtk_drawing_area_new();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_add (parentHandle, handle);
	OS.gtk_widget_show (handle);
	int type = (style & SWT.VERTICAL) != 0 ? OS.GDK_SB_H_DOUBLE_ARROW:OS.GDK_SB_V_DOUBLE_ARROW;
	cursor = OS.gdk_cursor_new (type);
	OS.gtk_widget_realize (handle);
	int window = OS.GTK_WIDGET_WINDOW (handle);
	OS.gdk_window_set_cursor (window, cursor);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.MouseDoubleClick,typedListener);
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
	eventTable.unhook (SWT.MouseDoubleClick,listener);	
}

int processMouseDown (int callData, int arg1, int int2) {
	super.processMouseDown (callData,arg1,int2);
	if (OS.gdk_event_button_get_button(callData) != 1) return 0;
	
	originX = OS.GTK_WIDGET_X(handle);
	originY = OS.GTK_WIDGET_Y(handle);
	lastX = 0;
	lastY = 0;
	
	/* The event must be sent because its doit flag is used. */
	Event event = new Event ();
	event.detail = SWT.DRAG;
	event.time = OS.gdk_event_get_time(callData);
	event.x = originX;
	event.y = originY;
	event.width = OS.GTK_WIDGET_WIDTH (handle);
	event.height = OS.GTK_WIDGET_HEIGHT (handle);
	/*
	 * It is possible (but unlikely) that client code could have disposed
	 * the widget in the selection event.  If this happens end the processing
	 * of this message by returning.
	 */
	sendEvent (SWT.Selection, event);
	if (isDisposed ()) return 0;
	if (event.doit) {
		dragging = true;
		drawBand (originX, originY, event.width, event.height);
	}
	return 0;
}

int processMouseMove (int callData, int arg1, int int2) {
	super.processMouseMove (callData, arg1, int2);
	if (!dragging) return 0;
	
	/* Get the coordinates where the event happened, relative to the receiver */
	double[] px = new double[1];
	double[] py = new double[1];
	OS.gdk_event_get_coords(callData, px, py);
	int x = (int)(px[0]);
	int y = (int)(py[0]);

	if ((style & SWT.VERTICAL) != 0) {
		/* Erase the old one */
		int oldDrawX = originX + lastX;
		drawBand(oldDrawX, originY,
		         OS.GTK_WIDGET_WIDTH(handle), OS.GTK_WIDGET_HEIGHT(handle));
		
	} else {
	}
	return 0;
}

int processMouseUp (int callData, int arg1, int int2) {
	super.processMouseUp(callData, arg1,int2);
	int button = OS.gdk_event_button_get_button(callData);
	if (button != 1) return 0;
	if (!dragging) return 0;
//	drawBand(lastX, lastY, width, height);

/*	int width = OS.GTK_WIDGET_WIDTH (handle);
	int height = OS.GTK_WIDGET_HEIGHT (handle);
	int x = OS.GTK_WIDGET_X(handle);
	int y = OS.GTK_WIDGET_Y(handle);
	Rectangle rect = parent.getClientArea();
	int parentWidth = rect.width - 2;
	int parentHeight = rect.height - 2;
	double[] px = new double[1];
	double[] py = new double[1];
	OS.gdk_event_get_coords(callData, px, py);
	last_root_x=(int)(px[0]); last_root_y=(int)(py[0]);
System.out.println("Event coords: "+last_root_x+"x"+last_root_y);
System.out.println("Widget coords: "+x+"x"+y);
	int newX = lastX, newY = lastY;
	if ((style & SWT.VERTICAL) != 0) {
		if (last_root_x<=start_root_x)
			newX = Math.min (Math.max (0, x - (start_root_x-last_root_x) - startX ), parentWidth - width);
		else 	
			newX = Math.min (Math.max (0, x + (last_root_x-start_root_x) - startX ), parentWidth - width);
	} else {
		if (last_root_y<=start_root_y)
			newY = Math.min (Math.max (0, y - (start_root_y-last_root_y)  - startY ), parentHeight - height);
		else
			newY = Math.min (Math.max (0, y + (last_root_y-start_root_y)  - startY ), parentHeight - height);
	}
	if ((newX == lastX) && (newY == lastY)) return 0;

	Event event = new Event ();
	event.time = OS.gdk_event_get_time(callData);
//	event.x = newX;  event.y = newY;
	event.x = 20;  event.y = 0;
	event.width = width;  event.height = height;
	dragging = false;
	drawing = false;
	sendEvent (SWT.Selection, event);*/
	return 0;
}

int processMouseEnter (int callData, int arg1, int int2) {
	int width = OS.GTK_WIDGET_WIDTH (handle);
	int height = OS.GTK_WIDGET_HEIGHT (handle);
	lastX = OS.GTK_WIDGET_X(handle);  lastY = OS.GTK_WIDGET_Y(handle);
	Event event = new Event ();
	event.time = OS.gdk_event_get_time(callData);
	event.detail = SWT.DRAG;
	event.x = lastX;  event.y = lastY;
	event.width = width;  event.height = height;
	Cursor arrowCursor;
	if ((style & SWT.HORIZONTAL) != 0) {
		arrowCursor = new Cursor(parent.getDisplay(), SWT.CURSOR_SIZENS);	
	} else {
		arrowCursor = new Cursor(parent.getDisplay(), SWT.CURSOR_SIZEWE);
	}
	setCursor(arrowCursor);
	sendEvent (SWT.Selection, event);
	return 0;
}

int processMouseExit (int callData, int arg1, int int2) {
	int width = OS.GTK_WIDGET_WIDTH (handle);
	int height = OS.GTK_WIDGET_HEIGHT (handle);
	Event event = new Event ();
	event.time = OS.gdk_event_get_time(callData);
	event.x = lastX;  event.y = lastY;
	event.width = width;  event.height = height;
	sendEvent (SWT.MouseExit, event);
	return 0;
	
}

void drawBand (int x, int y, int width, int height) {
	Display display= parent.getDisplay ();
	if (display == null) return;
	int window = OS.GTK_WIDGET_WINDOW(parent.paintHandle());
	if (window == 0) return;
	byte [] bits = {-86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0, -86, 0, 85, 0};
	int stipplePixmap = OS.gdk_bitmap_create_from_data (window, bits, 8, 8);
	int gc = OS.gdk_gc_new(window);
	Color color = new Color(display, 0xFF, 0, 0);
	OS.gdk_gc_set_background(gc, color.handle);
	Color color1 = new Color(display, 0, 0xFF, 0);
	OS.gdk_gc_set_foreground(gc, color1.handle);	
	OS.gdk_gc_set_stipple(gc, stipplePixmap);
	OS.gdk_gc_set_subwindow(gc, OS.GDK_INCLUDE_INFERIORS);
	OS.gdk_gc_set_fill(gc, OS.GDK_STIPPLED);
	OS.gdk_gc_set_function(gc, OS.GDK_XOR);
	OS.gdk_draw_rectangle(window, gc, 1, x, y, width, height);	
	OS.g_object_unref(stipplePixmap);
	OS.g_object_unref(gc);
	color.dispose();
	color1.dispose();
}
static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

void releaseWidget () {
	super.releaseWidget ();
	if (cursor != 0) OS.gdk_cursor_destroy (cursor);
	cursor = 0;
}

}

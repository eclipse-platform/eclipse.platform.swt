/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
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
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
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
	int cursor;
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}
static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
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
	state |= THEME_BACKGROUND;
	int border = (style & SWT.BORDER) != 0 ? 1 : 0;
	int [] argList = {
		OS.XmNborderWidth, border,
		OS.XmNmarginWidth, 0,
		OS.XmNmarginHeight, 0,
		OS.XmNresizePolicy, OS.XmRESIZE_NONE,
		OS.XmNancestorSensitive, 1,
		OS.XmNtraversalOn, 0,
	};
	int parentHandle = parent.handle;
	handle = OS.XmCreateDrawingArea (parentHandle, null, argList, argList.length / 2);
}
void drawBand (int x, int y, int width, int height) {
	if ((style & SWT.SMOOTH) != 0) return;
	int display = OS.XtDisplay (parent.handle);
	if (display == 0) return;
	int window = OS.XtWindow (parent.handle);
	if (window == 0) return;
	int foreground = parent.getForegroundPixel ();
	Control control = parent.findBackgroundControl ();
	if (control == null) control = parent;
	int background = control.getBackgroundPixel ();
	int color = foreground ^ background;
	byte [] bits = {-86, 85, -86, 85, -86, 85, -86, 85};
	int stipplePixmap = OS.XCreateBitmapFromData (display, window, bits, 8, 8);
	int gc = OS.XCreateGC (display, window, 0, null);
	OS.XSetForeground (display, gc, color);
	OS.XSetStipple (display, gc, stipplePixmap);
	OS.XSetSubwindowMode (display, gc, OS.IncludeInferiors);
	OS.XSetFillStyle (display, gc, OS.FillStippled);
	OS.XSetFunction (display, gc, OS.GXxor);
	OS.XFillRectangle (display, window, gc, x, y, width, height);
	OS.XFreePixmap (display, stipplePixmap);
	OS.XFreeGC (display, gc);
}
void propagateWidget (boolean enabled) {
	int xCursor = enabled && super.cursor != null ? super.cursor.handle : cursor;
	propagateHandle (enabled, handle, xCursor);
}
void realizeChildren () {
	super.realizeChildren ();
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return;
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	if ((style & SWT.HORIZONTAL) != 0) {
		cursor = OS.XCreateFontCursor (xDisplay, OS.XC_sb_v_double_arrow);
	} else {
		cursor = OS.XCreateFontCursor (xDisplay, OS.XC_sb_h_double_arrow);
	}
	if (super.cursor == null && isEnabled ()) {
		OS.XDefineCursor (xDisplay, xWindow, cursor);
		OS.XFlush (xDisplay);
	}
}
void releaseWidget () {
	super.releaseWidget ();
	if (cursor != 0) {
		int display = OS.XtDisplay (handle);
		if (display != 0) OS.XFreeCursor (display, cursor);
	}
	cursor = 0;
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
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
}
public void setCursor (Cursor cursor) {
	checkWidget();
	super.setCursor (cursor);
	if (cursor == null && this.cursor != 0) {
		int xWindow = OS.XtWindow (handle);
		if (xWindow == 0) return;
		int xDisplay = OS.XtDisplay (handle);
		if (xDisplay == 0) return;
		OS.XDefineCursor (xDisplay, xWindow, this.cursor);
		OS.XFlush (xDisplay);
	}
}
public boolean setFocus () {
	checkWidget();
	return forceFocus ();
}
int XButtonPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XButtonPress (w, client_data, call_data, continue_to_dispatch);
	if (result != 0) return result;
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, call_data, XButtonEvent.sizeof);
	if (xEvent.button != 1) return result;
	short [] x_root = new short [1], y_root = new short [1];
	OS.XtTranslateCoords (handle, (short) 0, (short) 0, x_root, y_root);
	startX = xEvent.x_root - x_root [0];
	startY = xEvent.y_root - y_root [0];
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int border = argList [9], width = argList [5] + (border * 2), height = argList [7] + (border * 2);
	lastX = ((short) argList [1]) - border;  lastY = ((short) argList [3]) - border;
	Event event = new Event ();
	event.time = xEvent.time;
	event.x = lastX;
	event.y = lastY;
	event.width = width;
	event.height = height;
	if ((style & SWT.SMOOTH) == 0) {
		event.detail = SWT.DRAG;
	}
	sendEvent (SWT.Selection, event);
	if (isDisposed ()) return result;
	if (event.doit) {
		dragging = true;
		lastX = event.x;
		lastY = event.y;
		parent.update (true);
		drawBand (event.x, event.y, width, height);
		if ((style & SWT.SMOOTH) != 0) {
			setBounds (event.x, event.y, width, height);
			// widget could be disposed at this point
		}
	}
	return result;
}
int XButtonRelease (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XButtonRelease (w, client_data, call_data, continue_to_dispatch);
	if (result != 0) return result;
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, call_data, XButtonEvent.sizeof);
	if (xEvent.button != 1) return result;
	if (!dragging) return result;
	dragging = false;
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int border = argList [5];
	int width = argList [1] + (border * 2), height = argList [3] + (border * 2);
	Event event = new Event ();
	event.time = xEvent.time;
	event.x = lastX;
	event.y = lastY;
	event.width = width;
	event.height = height;
	drawBand (lastX, lastY, width, height);
	sendEvent (SWT.Selection, event);
	if (isDisposed ()) return result;
	if (event.doit) {
		if ((style & SWT.SMOOTH) != 0) {
			setBounds (event.x, event.y, width, height);
			// widget could be disposed at this point
		}
	}
	return result;
}
int xFocusIn (XFocusChangeEvent xEvent) {
	int result = super.xFocusIn (xEvent);
	if (handle == 0) return result;
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	lastX = argList [1];
	lastY = argList [3];
	return result;
}
int XKeyPress (int w, int client_data, int call_data, int continue_to_dispatch) {	
	int result = super.XKeyPress (w, client_data, call_data, continue_to_dispatch);
	if (result != 0) return result;
	XKeyEvent xEvent = new XKeyEvent ();
	OS.memmove (xEvent, call_data, XKeyEvent.sizeof);
	byte [] buffer = new byte [1];
	int [] keysym = new int [1];
	OS.XLookupString (xEvent, buffer, buffer.length, keysym, null);
	
	switch (keysym [0]) {
		case OS.XK_Left:
		case OS.XK_Right:
		case OS.XK_Up:
		case OS.XK_Down:
			int xChange = 0, yChange = 0;
			int stepSize = PAGE_INCREMENT;
			if ((xEvent.state & OS.ControlMask) != 0) stepSize = INCREMENT;
			if ((style & SWT.VERTICAL) != 0) {
				if (keysym [0] == OS.XK_Up || keysym [0] == OS.XK_Down) break;
				xChange = keysym [0] == OS.XK_Left ? -stepSize : stepSize;
			} else {
				if (keysym [0] == OS.XK_Left || keysym [0] == OS.XK_Right) break;
				yChange = keysym [0] == OS.XK_Up ? -stepSize : stepSize;
			}
			
			int [] argList1 = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
			OS.XtGetValues (handle, argList1, argList1.length / 2);
			int border = argList1 [5];
			int width = argList1 [1] + (border * 2), height = argList1 [3] + (border * 2);
			int [] argList2 = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
			OS.XtGetValues (parent.handle, argList2, argList2.length / 2);
			int parentBorder = argList2 [5];
			int parentWidth = argList2 [1] + (parentBorder * 2);
			int parentHeight = argList2 [3] + (parentBorder * 2);
			int newX = lastX, newY = lastY;
			if ((style & SWT.VERTICAL) != 0) {
				newX = Math.min (Math.max (0, lastX + xChange - parentBorder - startX), parentWidth - width);
			} else {
				newY = Math.min (Math.max (0, lastY + yChange - parentBorder - startY), parentHeight - height);
			}
			if (newX == lastX && newY == lastY) return result;
			
			/* Ensure that the pointer image does not change */
			int xDisplay = display.xDisplay;
			int xWindow = OS.XtWindow (parent.handle);
			int ptrGrabResult = OS.XGrabPointer (
				xDisplay,
				xWindow,
				1,
				OS.None,
				OS.GrabModeAsync,
				OS.GrabModeAsync,
				OS.None,
				cursor,
				OS.CurrentTime);
			
			Event event = new Event ();
			event.time = xEvent.time;
			event.x = newX;
			event.y = newY;
			event.width = width;
			event.height = height;
			sendEvent (SWT.Selection, event);
			if (ptrGrabResult == OS.GrabSuccess) OS.XUngrabPointer (xDisplay, OS.CurrentTime);
			if (isDisposed ()) break;
			
			if (event.doit) {
				lastX = event.x;
				lastY = event.y;
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
				OS.XWarpPointer (xDisplay, OS.None, xWindow, 0, 0, 0, 0, cursorX, cursorY);
			}
			break;
	}

	return result;
}
int XPointerMotion (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XPointerMotion (w, client_data, call_data, continue_to_dispatch);
	if (result != 0) return result;
	XMotionEvent xEvent = new XMotionEvent ();
	OS.memmove (xEvent, call_data, XMotionEvent.sizeof);
	if (!dragging || (xEvent.state & OS.Button1Mask) == 0) return result;
	short [] x_root = new short [1], y_root = new short [1];
	OS.XtTranslateCoords (handle, (short) 0, (short) 0, x_root, y_root);
	int eventX = xEvent.x_root - x_root [0], eventY = xEvent.y_root - y_root [0];
	int [] argList1 = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	int border = argList1 [9], x = ((short) argList1 [1]) - border, y = ((short) argList1 [3]) - border;
	int width = argList1 [5] + (border * 2), height = argList1 [7] + (border * 2);
	int [] argList2 = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (parent.handle, argList2, argList2.length / 2);
	int parentBorder = argList2 [5];
	int parentWidth = argList2 [1] + (parentBorder * 2);
	int parentHeight = argList2 [3] + (parentBorder * 2);
	int newX = lastX, newY = lastY;
	if ((style & SWT.VERTICAL) != 0) {
		newX = Math.min (Math.max (0, eventX + x - startX - parentBorder), parentWidth - width);
	} else {
		newY = Math.min (Math.max (0, eventY + y - startY - parentBorder), parentHeight - height);
	}
	if (newX == lastX && newY == lastY) return result;
	drawBand (lastX, lastY, width, height);
	Event event = new Event ();
	event.time = xEvent.time;
	event.x = newX;
	event.y = newY;
	event.width = width;
	event.height = height;
	if ((style & SWT.SMOOTH) == 0) {
		event.detail = SWT.DRAG;
	}
	sendEvent (SWT.Selection, event);
	if (isDisposed ()) return result;
	if (event.doit) {
		lastX = event.x;
		lastY = event.y;
	}
	parent.update (true);
	drawBand (lastX, lastY, width, height);
	if ((style & SWT.SMOOTH) != 0) {
		setBounds (lastX, lastY, width, height);
		// widget could be disposed at this point
	}
	return result;
}
}

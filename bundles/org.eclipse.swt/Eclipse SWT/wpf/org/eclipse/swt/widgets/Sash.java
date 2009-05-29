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

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.*;
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
	final static int INCREMENT = 1;
	final static int PAGE_INCREMENT = 9;

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
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

void createHandle () {
	state |= THEME_BACKGROUND;
	handle = OS.gcnew_Canvas ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.UIElement_Focusable (handle, true);
	int newCursor = (style & SWT.VERTICAL)!= 0 ? OS.Cursors_SizeWE () : OS.Cursors_SizeNS ();  
	OS.FrameworkElement_Cursor (handle, newCursor);
	OS.GCHandle_Free (newCursor);
}

int defaultBackground () {
	return OS.SystemColors_ControlColor;
}

void HandlePreviewGotKeyboardFocus (int sender, int e) {
	super.HandlePreviewGotKeyboardFocus (sender, e);
	if (!checkEvent (e)) return;
	Point location = getLocation();
	lastX = location.x;
	lastY = location.y;
}

void HandlePreviewKeyDown (int sender, int e) {
	super.HandlePreviewKeyDown (sender, e);
	if (!checkEvent (e)) return;
	int key = OS.KeyEventArgs_Key (e);
	switch (key) {
		case OS.Key_Left:
		case OS.Key_Right:
		case OS.Key_Up:
		case OS.Key_Down:
			int xChange = 0, yChange = 0;
			int stepSize = PAGE_INCREMENT;
			int keyboardDevice = OS.KeyboardEventArgs_KeyboardDevice(e);
			int modifiers = OS.KeyboardDevice_Modifiers(keyboardDevice);
			OS.GCHandle_Free(keyboardDevice);
			if ((modifiers & OS.ModifierKeys_Control) != 0) stepSize = INCREMENT;
			if ((style & SWT.VERTICAL) != 0) {
				if (key == OS.Key_Up || key == OS.Key_Down) break;
				xChange = key == OS.Key_Left ? -stepSize : stepSize;
			} else {
				if (key == OS.Key_Left || key == OS.Key_Right) break;
				yChange = key == OS.Key_Up ? -stepSize : stepSize;
			}
			
			Rectangle bounds = getBounds ();
			int width = bounds.width, height = bounds.height;
			Rectangle parentBounds = parent.getBounds ();
			int parentWidth = parentBounds.width;
			int parentHeight = parentBounds.height;
			int newX = lastX, newY = lastY;
			if ((style & SWT.VERTICAL) != 0) {
				newX = Math.min (Math.max (0, lastX + xChange), parentWidth - width);
			} else {
				newY = Math.min (Math.max (0, lastY + yChange), parentHeight - height);
			}
			if (newX == lastX && newY == lastY) return;
			Event event = new Event ();
			event.x = newX;
			event.y = newY;
			event.width = width;
			event.height = height;
			sendEvent (SWT.Selection, event);
			if (isDisposed ()) break;
			if (event.doit) {
				setBounds (event.x, event.y, width, height);
				if (isDisposed ()) break;
				lastX = event.x;
				lastY = event.y;
				if (isDisposed ()) return;
				int cursorX = event.x, cursorY = event.y;
				if ((style & SWT.VERTICAL) != 0) {
					cursorY += height / 2;
				} else {
					cursorX += width / 2;
				}
				display.setCursorLocation (parent.toDisplay (cursorX, cursorY));
			}
			OS.RoutedEventArgs_Handled(e, true);
			break;
	}	
}

void HandlePreviewMouseDown (int sender, int e) {
	super.HandlePreviewMouseDown (sender, e);
	int eventPos = OS.MouseEventArgs_GetPosition (e, handle);
	startX = (int) OS.Point_X (eventPos); 
	startY = (int) OS.Point_Y (eventPos);
	OS.GCHandle_Free (eventPos);
	Point location = parent.getLocation (this);
	int x = location.x;
	int y = location.y;
	int width = (int) OS.FrameworkElement_ActualWidth (handle);
	int height = (int) OS.FrameworkElement_ActualHeight (handle);
	lastX = x; 
	lastY = y;
	Event event = new Event ();
	event.x = lastX;
	event.y = lastY;
	event.width = width;
	event.height = height;
	sendEvent (SWT.Selection, event);
	if (event.doit) {
		dragging = true;
		lastX = event.x;
		lastY = event.y;
		OS.UIElement_CaptureMouse (handle);
	}
}

void HandlePreviewMouseUp (int sender, int e) {
	super.HandlePreviewMouseUp (sender, e);
	if (!dragging) return;
	OS.UIElement_ReleaseMouseCapture (handle);
	dragging = false;
	int width = (int) OS.FrameworkElement_ActualWidth (handle);
	int height = (int) OS.FrameworkElement_ActualHeight (handle);
	Event event = new Event ();
	event.x = lastX;
	event.y = lastY;
	event.width = width;
	event.height = height;
	sendEvent (SWT.Selection, event);
}

void HandlePreviewMouseMove (int sender, int e) {
	super.HandlePreviewMouseMove (sender, e);
	if (!dragging) return;
	int eventPos = OS.MouseEventArgs_GetPosition (e, handle);
	int eventX = (int) OS.Point_X (eventPos); 
	int eventY = (int) OS.Point_Y (eventPos);
	OS.GCHandle_Free (eventPos);
	Point location = parent.getLocation (this);
	int x = location.x;
	int y = location.y;
	int width = (int) OS.FrameworkElement_ActualWidth (handle);
	int height = (int) OS.FrameworkElement_ActualHeight (handle);
	int parentWidth =  (int) OS.FrameworkElement_ActualWidth (parent.handle);
	int parentHeight = (int) OS.FrameworkElement_ActualHeight (parent.handle);
	int newX = lastX, newY = lastY;
	if ((style & SWT.VERTICAL) != 0) {
		newX = Math.min (Math.max (0, eventX + x - startX), parentWidth - width);
	} else {
		newY = Math.min (Math.max (0, eventY + y - startY), parentHeight - height);
	}
	if (newX == lastX && newY == lastY) return;	
	Event event = new Event ();
	event.x = newX;
	event.y = newY;
	event.width = width;
	event.height = height;
	sendEvent (SWT.Selection, event);
	if (isDisposed ()) return;
	if (event.doit) {
		lastX = event.x;
		lastY = event.y;
	}
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

int traversalCode (int key, int event) {
	return 0;
}

}

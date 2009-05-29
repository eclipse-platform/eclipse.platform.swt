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


import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;

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
	Cursor sizeCursor;
	boolean dragging;
	int lastX, lastY, startX, startY;
	static final int INCREMENT = 1;
	static final int PAGE_INCREMENT = 9;
	static final String [] AX_ATTRIBUTES = {
		OS.kAXOrientationAttribute,
		OS.kAXValueAttribute,
		OS.kAXMaxValueAttribute,
		OS.kAXMinValueAttribute,
	};

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
	int cursorStyle = (style & SWT.VERTICAL) != 0 ? SWT.CURSOR_SIZEWE : SWT.CURSOR_SIZENS;
	sizeCursor = new Cursor (display, cursorStyle);
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
	/*
	* Macintosh only supports smooth dragging.
	*/
	style |= SWT.SMOOTH;
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

int callFocusEventHandler (int nextHandler, int theEvent) {
	return OS.noErr;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = 0, height = 0;
	if ((style & SWT.HORIZONTAL) != 0) {
		width += DEFAULT_WIDTH;  height += 5;
	} else {
		width += 5; height += DEFAULT_HEIGHT;
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createHandle () {
	state |= GRAB | THEME_BACKGROUND;
	int features = OS.kControlSupportsFocus;
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	OS.CreateUserPaneControl (window, null, features, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
}

void drawBackground (int control, int context) {
	fillBackground (control, context, null);
}

String [] getAxAttributes () {
	return AX_ATTRIBUTES;
}

int kEventAccessibleGetNamedAttribute (int nextHandler, int theEvent, int userData) {
	int code = OS.eventNotHandledErr;
	int [] stringRef = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeName, OS.typeCFStringRef, null, 4, null, stringRef);
	int length = 0;
	if (stringRef [0] != 0) length = OS.CFStringGetLength (stringRef [0]);
	char [] buffer = new char [length];
	CFRange range = new CFRange ();
	range.length = length;
	OS.CFStringGetCharacters (stringRef [0], range, buffer);
	String attributeName = new String(buffer);
	if (attributeName.equals (OS.kAXRoleAttribute) || attributeName.equals (OS.kAXRoleDescriptionAttribute)) {
		String roleText = OS.kAXSplitterRole;
		buffer = new char [roleText.length ()];
		roleText.getChars (0, buffer.length, buffer, 0);
		stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (stringRef [0] != 0) {
			if (attributeName.equals (OS.kAXRoleAttribute)) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
			} else { // kAXRoleDescriptionAttribute
				int stringRef2 = OS.HICopyAccessibilityRoleDescription (stringRef [0], 0);
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringRef2});
				OS.CFRelease(stringRef2);
			}
			OS.CFRelease(stringRef [0]);
			code = OS.noErr;
		}
	} else if (attributeName.equals (OS.kAXOrientationAttribute)) {
		String orientation = (style & SWT.VERTICAL) != 0 ? OS.kAXVerticalOrientationValue : OS.kAXHorizontalOrientationValue;
		buffer = new char [orientation.length ()];
		orientation.getChars (0, buffer.length, buffer, 0);
		stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (stringRef [0] != 0) {
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
			OS.CFRelease(stringRef [0]);
			code = OS.noErr;
		}
	} else if (attributeName.equals (OS.kAXValueAttribute)) {
		Point location = getLocation();
		int value = (style & SWT.VERTICAL) != 0 ? location.x : location.y;
		OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeSInt32, 4, new int [] {value});
		code = OS.noErr;
	} else if (attributeName.equals (OS.kAXMaxValueAttribute)) {
		Rect parentBounds = new Rect ();
		OS.GetControlBounds (parent.handle, parentBounds);
		int maxValue = (style & SWT.VERTICAL) != 0 ?
				parentBounds.right - parentBounds.left :
				parentBounds.bottom - parentBounds.top;
		OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeSInt32, 4, new int [] {maxValue});
		code = OS.noErr;
	} else if (attributeName.equals (OS.kAXMinValueAttribute)) {
		OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeSInt32, 4, new int [] {0});
		code = OS.noErr;
	}
	if (accessible != null) {
		code = accessible.internal_kEventAccessibleGetNamedAttribute (nextHandler, theEvent, code);
	}
	return code;
}

int kEventControlClick (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlClick (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (!isEnabled ()) return OS.noErr;
	return result;
}

int kEventControlGetFocusPart (int nextHandler, int theEvent, int userData) {
	return OS.noErr;
}

int kEventControlSetCursor (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetCursor (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	display.setCursor (sizeCursor.handle);
	return OS.noErr;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetFocusPart (nextHandler, theEvent, userData);
	if (result == OS.noErr) {
		Point location = getLocation();
		lastX = location.x;
		lastY = location.y;
	}
	return result;
}

int kEventUnicodeKeyPressed (int nextHandler, int theEvent, int userData) {
	int result = super.kEventUnicodeKeyPressed (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] keyboardEvent = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendKeyboardEvent, OS.typeEventRef, null, keyboardEvent.length * 4, null, keyboardEvent);
	int [] keyCode = new int [1];
	OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	switch (keyCode [0]) {
		case 126: /* Up arrow */
		case 123: /* Left arrow */
		case 125: /* Down arrow */
		case 124: /* Right arrow */ {
			int xChange = 0, yChange = 0;
			int stepSize = PAGE_INCREMENT;
			int [] modifiers = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
			if ((modifiers [0] & OS.controlKey) != 0) stepSize = INCREMENT;
			if ((style & SWT.VERTICAL) != 0) {
				if (keyCode [0] == 126 || keyCode [0] == 125) break;
				xChange = keyCode [0] == 123 ? -stepSize : stepSize;
			} else {
				if (keyCode [0] == 123 || keyCode [0] == 124) break;
				yChange = keyCode [0] == 126 ? -stepSize : stepSize;
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
			if (newX == lastX && newY == lastY) return result;
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
				if (isDisposed ()) return result;
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
	}

	return result;
}

void releaseWidget () {
	super.releaseWidget ();
	if (sizeCursor != null) sizeCursor.dispose ();
	sizeCursor = null;
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

boolean sendMouseEvent (int type, short button, int count, int detail, boolean send, int chord, short x, short y, int modifiers) {
	boolean result = super.sendMouseEvent (type, button, count, detail, send, chord, x, y, modifiers);
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	int controlX = rect.left;
	int controlY = rect.top;
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	OS.GetControlBounds (parent.handle, rect);
	int parentWidth = rect.right - rect.left;
	int parentHeight = rect.bottom - rect.top;
	switch (type) {
		case SWT.MouseDown:
			if (button != 1 || count != 1) break;
			startX = x;
			startY = y;
			Event event = new Event ();
			event.x = controlX;
			event.y = controlY;
			event.width = width;
			event.height = height;
			sendEvent (SWT.Selection, event);
			if (isDisposed ()) return result;
			if (event.doit) {
				lastX = event.x;
				lastY = event.y;
				dragging = true;
				setBounds (event.x, event.y, width, height);
			}
			break;
		case SWT.MouseUp:
			if (!dragging) break;
			dragging = false;
			event = new Event ();
			event.x = lastX;
			event.y = lastY;
			event.width = width;
			event.height = height;
			sendEvent (SWT.Selection, event);
			if (isDisposed ()) return result;
			if (event.doit) {
				setBounds (event.x, event.y, width, height);
			}
			break;
		case SWT.MouseMove:
			if (!dragging) break;
			int newX = lastX, newY = lastY;
			if ((style & SWT.VERTICAL) != 0) {
				newX = Math.min (Math.max (0, x + controlX - startX), parentWidth - width);
			} else {
				newY = Math.min (Math.max (0, y + controlY - startY), parentHeight - height);
			}
			if (newX == lastX && newY == lastY) return result;
			event = new Event ();
			event.x = newX;
			event.y = newY;
			event.width = width;
			event.height = height;
			sendEvent (SWT.Selection, event);
			if (isDisposed ()) return result;
			if (event.doit) {
				lastX = event.x;
				lastY = event.y;
				setBounds (event.x, event.y, width, height);
			}
			break;
	}
	return result;
}

int traversalCode (int key, int theEvent) {
	return 0;
}

}

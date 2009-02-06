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

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

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
 */
public class Sash extends Control {
	Cursor sizeCursor;
	boolean dragging;
	int lastX, lastY, startX, startY;
	private final static int INCREMENT = 1;
	private final static int PAGE_INCREMENT = 9;
	NSArray accessibilityAttributes = null;
	
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
	int cursorStyle = (style & SWT.VERTICAL) != 0 ? SWT.CURSOR_SIZEWE : SWT.CURSOR_SIZENS;
	sizeCursor = new Cursor (display, cursorStyle);
}

int /*long*/ accessibilityAttributeNames(int /*long*/ id, int /*long*/ sel) {
	if (accessibilityAttributes == null) {
		NSMutableArray ourAttributes = NSMutableArray.arrayWithCapacity(10);
		ourAttributes.addObject(OS.NSAccessibilityRoleAttribute);
		ourAttributes.addObject(OS.NSAccessibilityRoleDescriptionAttribute);
		ourAttributes.addObject(OS.NSAccessibilityParentAttribute);
		ourAttributes.addObject(OS.NSAccessibilityPositionAttribute);
		ourAttributes.addObject(OS.NSAccessibilitySizeAttribute);
		ourAttributes.addObject(OS.NSAccessibilityWindowAttribute);
		ourAttributes.addObject(OS.NSAccessibilityTopLevelUIElementAttribute);
		ourAttributes.addObject(OS.NSAccessibilityFocusedAttribute);
		ourAttributes.addObject(OS.NSAccessibilityValueAttribute);
		ourAttributes.addObject(OS.NSAccessibilityMaxValueAttribute);
		ourAttributes.addObject(OS.NSAccessibilityMinValueAttribute);
		// The accessibility documentation says that these next two are optional, but the
		// Accessibility Verifier says they are required.
		ourAttributes.addObject(OS.NSAccessibilityNextContentsAttribute);
		ourAttributes.addObject(OS.NSAccessibilityPreviousContentsAttribute);
		ourAttributes.addObject(OS.NSAccessibilityOrientationAttribute);

		if (accessible != null) {
			// See if the accessible will override or augment the standard list.
			// Help, title, and description can be overridden.
			NSMutableArray extraAttributes = NSMutableArray.arrayWithCapacity(3);
			extraAttributes.addObject(OS.NSAccessibilityHelpAttribute);
			extraAttributes.addObject(OS.NSAccessibilityDescriptionAttribute);
			extraAttributes.addObject(OS.NSAccessibilityTitleAttribute);

			for (int i = (int)/*64*/extraAttributes.count() - 1; i >= 0; i--) {
				NSString attribute = new NSString(extraAttributes.objectAtIndex(i).id);
				if (accessible.internal_accessibilityAttributeValue(attribute, ACC.CHILDID_SELF) != null) {
					ourAttributes.addObject(extraAttributes.objectAtIndex(i));
				}
			}
		}

		accessibilityAttributes = ourAttributes;
		accessibilityAttributes.retain();
	}
	
	return accessibilityAttributes.id;
}

int /*long*/ accessibilityAttributeValue(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	int /*long*/ returnValue = 0;
	NSString attributeName = new NSString(arg0);
	
	if (accessible != null) {
		id returnObject = accessible.internal_accessibilityAttributeValue(attributeName, ACC.CHILDID_SELF);
		
		if (returnObject != null) returnValue = returnObject.id;
	}

	if (returnValue != 0) return returnValue;

	if (attributeName.isEqualToString (OS.NSAccessibilityRoleAttribute) || attributeName.isEqualToString (OS.NSAccessibilityRoleDescriptionAttribute)) {
		NSString roleText = OS.NSAccessibilitySplitterRole;

		if (attributeName.isEqualToString (OS.NSAccessibilityRoleAttribute)) {
			return roleText.id;
		} else { // NSAccessibilityRoleDescriptionAttribute
			return OS.NSAccessibilityRoleDescription (roleText.id, 0);
		}
	} else if (attributeName.isEqualToString (OS.NSAccessibilityEnabledAttribute)) {
		return NSNumber.numberWithBool(isEnabled()).id;
	} else if (attributeName.isEqualToString (OS.NSAccessibilityOrientationAttribute)) {
		NSString orientation = (style & SWT.VERTICAL) != 0 ? OS.NSAccessibilityVerticalOrientationValue : OS.NSAccessibilityHorizontalOrientationValue;
		return orientation.id;
	} else if (attributeName.isEqualToString (OS.NSAccessibilityValueAttribute)) {
		Point location = getLocation();
		int value = (style & SWT.VERTICAL) != 0 ? location.x : location.y;
		return NSNumber.numberWithInt(value).id;
	} else if (attributeName.isEqualToString (OS.NSAccessibilityMaxValueAttribute)) {
		NSRect parentBounds = view.bounds();
		float /*double*/ maxValue = (style & SWT.VERTICAL) != 0 ?
				parentBounds.width :
				parentBounds.height;
		return NSNumber.numberWithInt((int)maxValue).id;
	} else if (attributeName.isEqualToString (OS.NSAccessibilityMinValueAttribute)) {
		return NSNumber.numberWithInt(0).id;
	} else if (attributeName.isEqualToString (OS.NSAccessibilityNextContentsAttribute)) {
		Control[] children =  parent._getChildren();
		Control nextView = null;
		for (int i = 0; i < children.length; i++) {
			if (children[i] == this) {
				if (i < children.length - 1) {
					nextView = children[i + 1];
					break;
				}
			}
		}
		
		if (nextView != null) 
			return NSArray.arrayWithObject(nextView.view).id;
		else
			return NSArray.array().id;
	} else if (attributeName.isEqualToString (OS.NSAccessibilityPreviousContentsAttribute)) {
		Control[] children =  parent._getChildren();
		Control nextView = null;
		for (int i = 0; i < children.length; i++) {
			if (children[i] == this) {
				if (i > 0) {
					nextView = children[i - 1];
					break;
				}
			}
		}
		
		if (nextView != null) 
			return NSArray.arrayWithObject(nextView.view).id;
		else
			return NSArray.array().id;
	}

	return super.accessibilityAttributeValue(id, sel, arg0);
}

boolean accessibilityIsIgnored(int /*long*/ id, int /*long*/ sel) {
	return false;	
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

boolean becomeFirstResponder (int /*long*/ id, int /*long*/ sel) {
	boolean result = super.becomeFirstResponder(id, sel);
	NSRect frame = view.frame();
	lastX = (int)frame.x;
	lastY = (int)frame.y;
	return result;
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
	state |= THEME_BACKGROUND;
	NSView widget = (NSView)new SWTView().alloc();
	widget.init ();
	view = widget;
}

void drawBackground (int /*long*/ id, NSGraphicsContext context, NSRect rect) {
	fillBackground (view, context, rect, -1);
}

Cursor findCursor () {
	Cursor cursor = super.findCursor ();
	if (cursor == null)	{
		int cursorType = (style & SWT.HORIZONTAL) != 0 ? SWT.CURSOR_SIZENS : SWT.CURSOR_SIZEWE;
		cursor = display.getSystemCursor (cursorType);
	}
	return cursor;
}

boolean sendKeyEvent(NSEvent nsEvent, int type) {
	super.sendKeyEvent (nsEvent, type);
	if (type == SWT.KeyDown) {
		int keyCode = nsEvent.keyCode();
		switch (keyCode) {
			case 126: /* Up arrow */
			case 123: /* Left arrow */
			case 125: /* Down arrow */
			case 124: /* Right arrow */ {
				int xChange = 0, yChange = 0;
				int stepSize = PAGE_INCREMENT;
				int /*long*/ modifiers = nsEvent.modifierFlags();
				if ((modifiers & OS.NSControlKeyMask) != 0) stepSize = INCREMENT;
				if ((style & SWT.VERTICAL) != 0) {
					if (keyCode == 126 || keyCode == 125) break;
					xChange = keyCode == 123 ? -stepSize : stepSize;
				} else {
					if (keyCode == 123 || keyCode  == 124) break;
					yChange = keyCode == 126 ? -stepSize : stepSize;
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
				if (newX == lastX && newY == lastY) return true;
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
					if (isDisposed ()) return false;
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
	}
	return true;
}

void mouseDown(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	//TODO use sendMouseEvent
	super.mouseDown(id, sel, theEvent);
	NSEvent nsEvent = new NSEvent(theEvent);
	if (nsEvent.clickCount() != 1) return;
	NSPoint location = nsEvent.locationInWindow();
	NSPoint point = view.convertPoint_fromView_(location, null);
	startX = (int)point.x;
	startY = (int)point.y;
	NSRect frame = view.frame();
	Event event = new Event ();
	event.x = (int)frame.x;
	event.y = (int)frame.y;
	event.width = (int)frame.width;
	event.height = (int)frame.height;
	sendEvent (SWT.Selection, event);
	if (isDisposed ()) return;
	if (event.doit) {
		lastX = event.x;
		lastY = event.y;
		dragging = true;
		setLocation(event.x, event.y);
	}
}

void mouseDragged(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	//TODO use sendMouseEvent
	super.mouseDragged(id, sel, theEvent);
	if (!dragging) return;
	NSEvent nsEvent = new NSEvent(theEvent);
	NSPoint location = nsEvent.locationInWindow();
	NSPoint point = view.convertPoint_fromView_(location, null);
	NSRect frame = view.frame();
	NSRect parentFrame = parent.topView().frame();
	int newX = lastX, newY = lastY;
	if ((style & SWT.VERTICAL) != 0) {
		newX = Math.min (Math.max (0, (int)(point.x + frame.x - startX)), (int)(parentFrame.width - frame.width));
	} else {
		newY = Math.min (Math.max (0, (int)(point.y + frame.y - startY)), (int)(parentFrame.height - frame.height));
	}
	if (newX == lastX && newY == lastY) return;
	Event event = new Event ();
	event.x = newX;
	event.y = newY;
	event.width = (int)frame.width;
	event.height = (int)frame.height;
	sendEvent (SWT.Selection, event);
	if (isDisposed ()) return;
	if (event.doit) {
		lastX = event.x;
		lastY = event.y;
		setBounds (event.x, event.y, (int)frame.width, (int)frame.height);
	}
}

void mouseUp(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	//TODO use sendMouseEvent
	super.mouseUp(id, sel, theEvent);
	if (!dragging) return;
	dragging = false;
	NSRect frame = view.frame();
	Event event = new Event ();
	event.x = lastX;
	event.y = lastY;
	event.width = (int)frame.width;
	event.height = (int)frame.height;
	sendEvent (SWT.Selection, event);
	if (isDisposed ()) return;
	if (event.doit) {
		setBounds (event.x, event.y, (int)frame.width, (int)frame.height);
	}
}

void releaseHandle () {
	super.releaseHandle ();
	if (accessibilityAttributes != null) accessibilityAttributes.release();
	accessibilityAttributes = null;
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

void superKeyDown (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
}

void superKeyUp (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
}

int traversalCode (int key, NSEvent theEvent) {
	return 0;
}

}

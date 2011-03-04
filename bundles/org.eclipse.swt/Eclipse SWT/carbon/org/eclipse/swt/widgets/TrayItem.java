/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class represent icons that can be placed on the
 * system tray or task bar status area.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, MenuDetect, Selection</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tray">Tray, TrayItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.0
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TrayItem extends Item {
	Tray parent;
	ToolTip toolTip;
	String toolTipText;
	boolean visible = true, highlight;
	int handle, nsImage, view, jniRef;
	
	static final float BORDER = 8f;
	
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tray</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TrayItem (Tray parent, int style) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
	createWidget ();
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the platform-specific context menu trigger
 * has occurred, by sending it one of the messages defined in
 * the <code>MenuDetectListener</code> interface.
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
 * @see MenuDetectListener
 * @see #removeMenuDetectListener
 *
 * @since 3.3
 */
public void addMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MenuDetect, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the receiver is selected
 * <code>widgetDefaultSelected</code> is called when the receiver is double-clicked
 * </p>
 *
 * @param listener the listener which should be notified when the receiver is selected by the user
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void createWidget () {
	int statusBar = Cocoa.objc_msgSend (Cocoa.C_NSStatusBar, Cocoa.S_systemStatusBar);
	handle = Cocoa.objc_msgSend (statusBar, Cocoa.S_statusItemWithLength, 0f);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	Cocoa.objc_msgSend (handle, Cocoa.S_retain);
	Cocoa.objc_msgSend (handle, Cocoa.S_setHighlightMode, 1);	
	jniRef = OS.NewGlobalRef (this);
	if (jniRef == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	NSRect rect = new NSRect();
	view = Cocoa.objc_msgSend (Cocoa.C_NSStatusItemImageView, Cocoa.S_alloc);
	if (view == 0) error (SWT.ERROR_NO_HANDLES);
	view = Cocoa.objc_msgSend (view, Cocoa.S_initWithProc_frame_user_data, display.trayItemProc, rect, jniRef);
	Cocoa.objc_msgSend (handle, Cocoa.S_setView, view);
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

Point getLocation () {
	NSRect rect = new NSRect();
	Cocoa.objc_msgSend_stret(rect, view, Cocoa.S_frame);
	NSRect windowRect = new NSRect();
	Cocoa.objc_msgSend_stret(windowRect, Cocoa.objc_msgSend(view, Cocoa.S_window), Cocoa.S_frame);
	rect.x += rect.width / 2;
	rect.y += rect.height;
	Cocoa.objc_msgSend_stret(rect, view, Cocoa.S_convertRect_toView, rect, 0);
	rect.x += windowRect.x;
	return new Point ((int)rect.x, (int)rect.y);
}

Point getMenuLocation () {
	NSRect rect = new NSRect();
	Cocoa.objc_msgSend_stret(rect, view, Cocoa.S_frame);
	NSRect windowRect = new NSRect();
	Cocoa.objc_msgSend_stret(windowRect, Cocoa.objc_msgSend(view, Cocoa.S_window), Cocoa.S_frame);
	rect.y += rect.height;
	Cocoa.objc_msgSend_stret(rect, view, Cocoa.S_convertRect_toView, rect, 0);
	rect.x += windowRect.x;
	/*
	* TODO - the carbon popup menu is not square on the top corners because
	* NSStatusItem.popUpStatusItemMenu() is not called since it takes
	* a NSMenu (not MenuRef). The 4 pixels offset is used to make the menu
	* align with the bottom of the menu bar.
	*/
	return new Point ((int)rect.x, (int)rect.y + 4);
}

/**
 * Returns the receiver's parent, which must be a <code>Tray</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public Tray getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns the receiver's tool tip, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public ToolTip getToolTip () {
	checkWidget ();
	return toolTip;
}

/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget ();
	return toolTipText;
}

/**
 * Returns <code>true</code> if the receiver is visible and 
 * <code>false</code> otherwise.
 *
 * @return the receiver's visibility
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget ();
	return visible;
}

void releaseHandle () {
	super.releaseHandle ();
	parent = null;
	handle = 0;
}

void releaseWidget () {
	int statusBar = Cocoa.objc_msgSend (Cocoa.C_NSStatusBar, Cocoa.S_systemStatusBar);
	Cocoa.objc_msgSend (statusBar, Cocoa.S_removeStatusItem, handle);
	Cocoa.objc_msgSend (handle, Cocoa.S_release);
	if (nsImage != 0) Cocoa.objc_msgSend (nsImage, Cocoa.S_release);
	if (view != 0) Cocoa.objc_msgSend (view, Cocoa.S_release);
	if (jniRef != 0) OS.DeleteGlobalRef (jniRef);
	handle = nsImage = view = jniRef = 0;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the platform-specific context menu trigger has
 * occurred.
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
 * @see MenuDetectListener
 * @see #addMenuDetectListener
 *
 * @since 3.3
 */
public void removeMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MenuDetect, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver is selected by the user.
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Sets the receiver's image.
 *
 * @param image the new image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	super.setImage (image);
	if (nsImage != 0) Cocoa.objc_msgSend (nsImage, Cocoa.S_release, nsImage);
	nsImage = 0;
	if (image != null) {
		CGRect rect = new CGRect ();
		rect.width = OS.CGImageGetWidth (image.handle);
		rect.height = OS.CGImageGetHeight (image.handle);
		NSSize size = new NSSize ();
		size.width = rect.width;
		size.height =  rect.height;
		nsImage = Cocoa.objc_msgSend (Cocoa.C_NSImage, Cocoa.S_alloc);
		nsImage = Cocoa.objc_msgSend (nsImage, Cocoa.S_initWithSize, size);
	    Cocoa.objc_msgSend (nsImage, Cocoa.S_lockFocus);
		int imageContext = Cocoa.objc_msgSend (Cocoa.C_NSGraphicsContext, Cocoa.S_currentContext);
		imageContext = Cocoa.objc_msgSend (imageContext, Cocoa.S_graphicsPort);
		OS.CGContextDrawImage (imageContext, rect, image.handle);
	    Cocoa.objc_msgSend (nsImage, Cocoa.S_unlockFocus);
	}
	Cocoa.objc_msgSend (view, Cocoa.S_setImage, nsImage);
	float width = image != null && visible ? OS.CGImageGetWidth (image.handle) + BORDER : 0;
	Cocoa.objc_msgSend (handle, Cocoa.S_setLength, width);
}

/**
 * Sets the receiver's tool tip to the argument, which
 * may be null indicating that no tool tip should be shown.
 *
 * @param toolTip the new tool tip (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setToolTip (ToolTip toolTip) {
	checkWidget ();
	ToolTip oldTip = this.toolTip, newTip = toolTip;
	if (oldTip != null) oldTip.item = null;
	this.toolTip = newTip;
	if (newTip != null) newTip.item = this;
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the 
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be 
 * escaped by doubling it in the string.
 * </p>
 * 
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget ();
	toolTipText = string;
	_setToolTipText (string);
}

void _setToolTipText (String string) {
	int ptr = 0;
	if (string != null) {
		char[] chars = new char [string.length ()];
		string.getChars(0, chars.length, chars, 0);
		ptr = OS.CFStringCreateWithCharacters (0, chars, chars.length);
	}
	Cocoa.objc_msgSend (view, Cocoa.S_setToolTip, ptr);
	if (ptr != 0) OS.CFRelease (ptr);
}

/**
 * Makes the receiver visible if the argument is <code>true</code>,
 * and makes it invisible otherwise. 
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
	checkWidget ();
	if (this.visible == visible) return;
	if (visible) {
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
	}
	this.visible = visible;
	float width = image != null && visible ? OS.CGImageGetWidth (image.handle) + BORDER : 0;
	Cocoa.objc_msgSend (handle, Cocoa.S_setLength, width);
	if (!visible) sendEvent (SWT.Hide);
}

void displayMenu () {
	if (highlight) {
		Cocoa.objc_msgSend(view, Cocoa.S_display);
		Display display = this.display;
		display.trayItemMenu = null;
		showMenu();
		if (display.trayItemMenu != null) {
			display.trayItemMenu = null;
			highlight = false;
			Cocoa.objc_msgSend (view, Cocoa.S_setNeedsDisplay, 1);
		}
	}
}

boolean shouldShowMenu (int event) {
	if (!hooks(SWT.MenuDetect)) return false;
	switch ((int)/*64*/Cocoa.objc_msgSend(event, Cocoa.S_type)) {
		case Cocoa.NSRightMouseDown: return true;
		case Cocoa.NSLeftMouseDown:
			if (!(hooks(SWT.Selection) || hooks(SWT.DefaultSelection))) {
				return true;
			}
			if ((Cocoa.objc_msgSend(event, Cocoa.S_modifierFlags) & Cocoa.NSDeviceIndependentModifierFlagsMask) == Cocoa.NSControlKeyMask) {
				return true;
			}
			return false;
		case Cocoa.NSLeftMouseDragged:
		case Cocoa.NSRightMouseDragged:
			return true;
	}
	return false;
}

void showMenu () {
	_setToolTipText (null);
	Display display = this.display;
	display.currentTrayItem = this;
	sendEvent (SWT.MenuDetect);
	if (!isDisposed ()) display.runPopups();
	display.currentTrayItem = null;
	if (isDisposed ()) return;
	_setToolTipText (toolTipText);
}

int trayItemProc (int target, int userData, int selector, int arg0) {
	switch (selector) {
		case 0:   //mouseDown
		case 2: { // rightMouseDown
			highlight = true;
			Cocoa.objc_msgSend (view, Cocoa.S_setNeedsDisplay, 1);
			if (shouldShowMenu(arg0)) displayMenu();
			break;
		}
		case 1:   // mouseUp
		case 4: { // rightMouseUp
			if (highlight) {
				if ((int)/*64*/Cocoa.objc_msgSend(arg0, Cocoa.S_type) == Cocoa.NSLeftMouseUp) {
					sendSelectionEvent((int)/*64*/Cocoa.objc_msgSend(arg0, Cocoa.S_clickCount) == 2 ? SWT.DefaultSelection : SWT.Selection);
				}
			}
			highlight = false;
			Cocoa.objc_msgSend (view, Cocoa.S_setNeedsDisplay, 1);
			break;
		}
		case 3: { // drawRect
			NSRect rect = new NSRect ();
			Cocoa.memcpy (rect, arg0, NSRect.sizeof);
			Cocoa.objc_msgSend (handle, Cocoa.S_drawStatusBarBackgroundInRect_withHighlight, rect, highlight ? 1 : 0);
			break;
		}
		case 5:   // mouseDragged
		case 6: { // rightMouseDragged
			NSRect frame = new NSRect();
			Cocoa.objc_msgSend_stret(frame, view, Cocoa.S_frame);
			NSPoint pt = new NSPoint();
			Cocoa.objc_msgSend_stret(pt, arg0, Cocoa.S_locationInWindow);
			highlight = Cocoa.NSPointInRect(pt, frame);
			Cocoa.objc_msgSend (view, Cocoa.S_setNeedsDisplay, 1);
			if (shouldShowMenu(arg0)) displayMenu();
			break;			
		}
	}
	return 0;
}
}


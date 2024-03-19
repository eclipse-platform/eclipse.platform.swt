/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class represent icons that can be placed on the
 * system tray or task bar status area.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, MenuDetect, Selection</dd>
 * </dl>
 * <p>
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
	NSStatusItem item;
	NSImageView view;
	Image highlightImage;

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
	addTypedListener(listener, SWT.MenuDetect);
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
	addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
void createHandle () {
	NSStatusBar statusBar = NSStatusBar.systemStatusBar();
	item = statusBar.statusItemWithLength(0);
	if (item == null) error (SWT.ERROR_NO_HANDLES);
	item.retain();
	item.setHighlightMode(true);
	view = (NSImageView)new SWTImageView().alloc();
	if (view == null) error (SWT.ERROR_NO_HANDLES);
	view.init ();
	item.setView(view);
}

@Override
void deregister () {
	super.deregister ();
	display.removeWidget (view);
	display.removeWidget(view.cell());
}

@Override
void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns the receiver's highlight image if it has one, or null
 * if it does not.
 *
 * @return the receiver's highlight image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
public Image getHighlightImage () {
	checkWidget();
	return highlightImage;
}

Point getLocation () {
	NSRect rect = view.frame();
	NSRect windowRect = view.window().frame();
	NSPoint pt = new NSPoint();
	pt.x = rect.width / 2;
	pt = view.convertPoint_fromView_(pt, null);
	pt.x += windowRect.x;
	return new Point ((int)pt.x, (int)pt.y);
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

@Override
void register () {
	super.register ();
	display.addWidget (view, this);
	display.addWidget (((NSControl)view).cell(), this);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	parent = null;
	if (item != null) item.release();
	if (view != null) view.release();
	item = null;
	view = null;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	NSStatusBar statusBar = NSStatusBar.systemStatusBar();
	statusBar.removeStatusItem(item);
	if (toolTip != null) toolTip.item = null;
	toolTip = null;
	toolTipText = null;
	highlightImage = null;
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
@Override
public void setImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	super.setImage (image);
	updateImage ();
}

/**
 * Sets the receiver's highlight image.
 *
 * @param image the new highlight image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
public void setHighlightImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	highlightImage = image;
	updateImage ();
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
 * <p>
 * NOTE: This operation is a hint and behavior is platform specific, on Windows
 * for CJK-style mnemonics of the form " (&amp;C)" at the end of the tooltip text
 * are not shown in tooltip.
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
	if (string != null) {
		char[] chars = new char [string.length ()];
		string.getChars (0, chars.length, chars, 0);
		int length = fixMnemonic (chars);
		NSString str = NSString.stringWithCharacters (chars, length);
		view.setToolTip (str);
	} else {
		view.setToolTip (null);
	}
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
	updateImage ();
	if (!visible) sendEvent (SWT.Hide);
}

void showMenu (Menu menu) {
	display.trayItemMenu = menu;
	item.popUpStatusItemMenu(menu.nsMenu);
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

void displayMenu () {
	if (highlight) {
		view.display();
		display.trayItemMenu = null;
		showMenu();
		if (display.trayItemMenu != null) {
			display.trayItemMenu = null;
			highlight = false;
			view.setNeedsDisplay(true);
		}
	}
}

boolean shouldShowMenu (NSEvent event) {
	if (!hooks(SWT.MenuDetect)) return false;
	switch ((int)event.type()) {
		case OS.NSRightMouseDown: return true;
		case OS.NSLeftMouseDown:
			if (!(hooks(SWT.Selection) || hooks(SWT.DefaultSelection))) {
				return true;
			}
			if ((event.modifierFlags() & OS.NSDeviceIndependentModifierFlagsMask) == OS.NSControlKeyMask) {
				return true;
			}
			return false;
		case OS.NSLeftMouseDragged:
		case OS.NSRightMouseDragged:
			return true;
	}
	return false;
}

@Override
void mouseDown(long id, long sel, long theEvent) {
	NSEvent nsEvent = new NSEvent(theEvent);
	highlight = true;
	updateImage();
	view.setNeedsDisplay(true);
	if (shouldShowMenu(nsEvent)) {
		displayMenu();
		updateImage ();
	}
}

@Override
void mouseDragged(long id, long sel, long theEvent) {
	NSEvent nsEvent = new NSEvent(theEvent);
	NSRect frame = view.frame();
	boolean oldHighlight = highlight;
	highlight = OS.NSPointInRect(nsEvent.locationInWindow(), frame);
	if (oldHighlight != highlight) {
		updateImage ();
	}
	view.setNeedsDisplay(true);
	if (shouldShowMenu(nsEvent)) {
		displayMenu();
		updateImage ();
	}
}

@Override
void mouseUp(long id, long sel, long theEvent) {
	if (highlight) {
		NSEvent nsEvent = new NSEvent(theEvent);
		if (nsEvent.type() == OS.NSLeftMouseUp) {
			sendSelectionEvent(nsEvent.clickCount() == 2 ? SWT.DefaultSelection : SWT.Selection);
		}
		highlight = false;
		updateImage ();
	}
	view.setNeedsDisplay(true);
}

@Override
void rightMouseDown(long id, long sel, long theEvent) {
	mouseDown(id, sel, theEvent);
}

@Override
void rightMouseUp(long id, long sel, long theEvent) {
	mouseUp(id, sel, theEvent);
}

@Override
void rightMouseDragged(long id, long sel, long theEvent) {
	mouseDragged(id, sel, theEvent);
}

@Override
void drawRect(long id, long sel, NSRect rect) {
	item.drawStatusBarBackgroundInRect(rect, highlight);
	super.drawRect(id, sel, rect);
}

void updateImage () {
	double width = 0;
	Image image = this.image;
	if (highlight && highlightImage != null) image = highlightImage;
	if (image == null) {
		view.setImage (null);
	} else {
		/*
		 * Feature in Cocoa.  If the NSImage object being set into the view is
		 * the same NSImage object that is already there then the view does not
		 * redraw itself.  This results in the view's image not visually updating
		 * if the NSImage object's content has changed since it was last set
		 * into the view.  The workaround is to explicitly redraw the view.
		 */
		view.setImage (image.handle);
		view.setNeedsDisplay (true);
		if (visible) {
			width = OS.NSSquareStatusItemLength();
		}
	}
	item.setLength (width);
}
}

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
 * Instances of this class are user interface objects that contain
 * menu items.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BAR, DROP_DOWN, POP_UP, NO_RADIO_GROUP</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Help, Hide, Show </dd>
 * </dl>
 * <p>
 * Note: Only one of BAR, DROP_DOWN and POP_UP may be specified.
 * Only one of LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#menu">Menu snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Menu extends Widget {
	NSMenu nsMenu;
	int x, y, itemCount;
	boolean hasLocation, visible;
	MenuItem [] items;
	MenuItem cascade, defaultItem;
	Decorations parent;

	static final int GAP = 4;

/**
 * Constructs a new instance of this class given its parent,
 * and sets the style for the instance so that the instance
 * will be a popup menu on the given parent's shell.
 * <p>
 * After constructing a menu, it can be set into its parent
 * using <code>parent.setMenu(menu)</code>.  In this case, the parent may
 * be any control in the same widget tree as the parent.
 * </p>
 *
 * @param parent a control which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#POP_UP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Control parent) {
	this (checkNull (parent).menuShell (), SWT.POP_UP);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Decorations</code>) and a style value
 * describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p><p>
 * After constructing a menu or menuBar, it can be set into its parent
 * using <code>parent.setMenu(menu)</code> or <code>parent.setMenuBar(menuBar)</code>.
 * </p>
 *
 * @param parent a decorations control which will be the parent of the new instance (cannot be null)
 * @param style the style of menu to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#BAR
 * @see SWT#DROP_DOWN
 * @see SWT#POP_UP
 * @see SWT#NO_RADIO_GROUP
 * @see SWT#LEFT_TO_RIGHT
 * @see SWT#RIGHT_TO_LEFT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Decorations parent, int style) {
	checkSubclass ();
	checkParent (parent);
	this.style = checkStyle(style);
	if (parent != null) {
		display = parent.display;
	} else {
		display = Display.getCurrent ();
		if (display == null) display = Display.getDefault ();
		if (!display.isValidThread ()) {
			error (SWT.ERROR_THREAD_INVALID_ACCESS);
		}
	}
	this.parent = parent;
	reskinWidget();
	createWidget();
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Menu</code>) and sets the style
 * for the instance so that the instance will be a drop-down
 * menu on the given parent's parent.
 * <p>
 * After constructing a drop-down menu, it can be set into its parentMenu
 * using <code>parentMenu.setMenu(menu)</code>.
 * </p>
 *
 * @param parentMenu a menu which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Menu parentMenu) {
	this (checkNull (parentMenu).parent, SWT.DROP_DOWN);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>MenuItem</code>) and sets the style
 * for the instance so that the instance will be a drop-down
 * menu on the given parent's parent menu.
 * <p>
 * After constructing a drop-down menu, it can be set into its parentItem
 * using <code>parentItem.setMenu(menu)</code>.
 * </p>
 *
 * @param parentItem a menu item which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (MenuItem parentItem) {
	this (checkNull (parentItem).parent);
}

Menu (Display display) {
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.display = display;
	this.style = SWT.BAR;
	reskinWidget();
	createWidget();
}

Menu (Display display, NSMenu nativeMenu) {
	this.display = display;
	this.style = SWT.DROP_DOWN;
	this.nsMenu = nativeMenu;
	reskinWidget();
	createWidget();
}

static Control checkNull (Control control) {
	if (control == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return control;
}

static Menu checkNull (Menu menu) {
	if (menu == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return menu;
}

static MenuItem checkNull (MenuItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}

@Override
void checkParent (Widget parent) {
	// A null parent is okay when the app menu bar is in use.
	if (parent == null && Display.getDefault().appMenuBar == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (parent != null) {
		if (parent.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		parent.checkWidget ();
		parent.checkOpen ();
	}
}

static int checkStyle (int style) {
	return checkBits (style, SWT.POP_UP, SWT.BAR, SWT.DROP_DOWN, 0, 0, 0);
}

void _setVisible (boolean visible) {
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	TrayItem trayItem = display.currentTrayItem;
	if (trayItem != null && visible) {
		trayItem.showMenu (this);
		return;
	}
	if (visible) {
		Shell shell = getShell ();
		NSWindow window = shell.view.window ();
		NSPoint location = null;
		if (hasLocation) {
			NSView topView = window.contentView();
			Point shellCoord = display.map(null, shell, new Point(x,y));
			location = new NSPoint ();
			location.x = shellCoord.x;
			location.y = topView.frame().height - shellCoord.y;
		} else {
			location = window.mouseLocationOutsideOfEventStream();
		}
		hasLocation = false;

		// Hold on to window in case it is disposed while the popup is open.
		window.retain();
		// NSMenu processes events on its own while the popup is open.
		display.sendPreExternalEventDispatchEvent ();
		NSEvent nsEvent = NSEvent.otherEventWithType(OS.NSApplicationDefined, location, 0, 0.0, window.windowNumber(), window.graphicsContext(), (short)0, 0, 0);
		NSMenu.popUpContextMenu(nsMenu, nsEvent, shell.view);
		display.sendPostExternalEventDispatchEvent ();
		window.release();
	} else {
		nsMenu.cancelTracking ();
	}
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when help events are generated for the control,
 * by sending it one of the messages defined in the
 * <code>HelpListener</code> interface.
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
 * @see HelpListener
 * @see #removeHelpListener
 */
public void addHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when menus are hidden or shown, by sending it
 * one of the messages defined in the <code>MenuListener</code>
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
 * @see MenuListener
 * @see #removeMenuListener
 */
public void addMenuListener (MenuListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Hide,typedListener);
	addListener (SWT.Show,typedListener);
}

@Override
void createHandle () {
	display.addMenu (this);
	if (nsMenu == null) {
		NSMenu widget = (NSMenu)new SWTMenu().alloc();
		widget = widget.initWithTitle(NSString.string());
		widget.setAutoenablesItems(false);
		widget.setDelegate(widget);
		nsMenu = widget;
	} else {
		nsMenu.retain();
		long cls = OS.object_getClass(nsMenu.id);
		long dynNSMenu_class = display.createMenuSubclass(cls, "SWTSystemMenu", true);
		if (cls != dynNSMenu_class) {
			OS.object_setClass(nsMenu.id, dynNSMenu_class);
		}
		nsMenu.setDelegate(nsMenu);
	}
}

void createItem (MenuItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	boolean add = true;
	NSMenuItem nsItem = item.nsItem;
	if (nsItem == null) {
		if ((item.style & SWT.SEPARATOR) != 0) {
			nsItem = NSMenuItem.separatorItem();
			nsItem.retain();
		} else {
			nsItem = (NSMenuItem)new SWTMenuItem().alloc();
			NSString empty = NSString.string();
			nsItem.initWithTitle(empty, 0, empty);
			nsItem.setTarget(nsItem);
			nsItem.setAction(OS.sel_sendSelection);
		}
		item.nsItem = nsItem;
	} else {
		long cls = OS.object_getClass(nsItem.id);
		long dynNSMenuItem_class = display.createMenuItemSubclass(cls, "SWTSystemMenuItem", true);
		if (cls != dynNSMenuItem_class) {
			OS.object_setClass(nsItem.id, dynNSMenuItem_class);
		}
		nsItem.retain();
		item.nsItemAction = nsItem.action();
		item.nsItemTarget = nsItem.target();
		nsItem.setTarget(nsItem);
		nsItem.setAction(OS.sel_sendSelection);

		// Sync native item type to Item's style.
		int type = SWT.PUSH;
		if (nsItem.isSeparatorItem()) type = SWT.SEPARATOR;
		if (nsItem.submenu() != null) type = SWT.CASCADE;
		item.style |= type;

		// Sync native item text to Item's text.
		item.text = nsItem.title().getString();

		// Sync native key equivalent to MenuItem's accelerator.
		// The system menu on OS X only uses command and option, so it's
		// safe to just check for those two key masks.
		long keyMask = nsItem.keyEquivalentModifierMask();
		NSString keyEquivString = nsItem.keyEquivalent();
		long keyEquiv = 0;
		if (keyEquivString != null) {
			keyEquiv = keyEquivString.characterAtIndex(0);
			if ((keyMask & OS.NSCommandKeyMask) != 0) keyEquiv |= SWT.COMMAND;
			if ((keyMask & OS.NSAlternateKeyMask) != 0) keyEquiv |= SWT.ALT;
			item.accelerator = (int) keyEquiv;
		}
		add = false;
	}
	item.createJNIRef();
	item.register();
	if (add) {
		nsMenu.insertItem(nsItem, index);
	}
	if (itemCount == items.length) {
		MenuItem [] newItems = new MenuItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
	if (add) {
		NSMenu emptyMenu = item.createEmptyMenu ();
		if (emptyMenu != null) {
			nsItem.setSubmenu (emptyMenu);
			emptyMenu.release();
		}
		if (display.menuBar == this) {
			NSApplication application = display.application;
			NSMenu menubar = application.mainMenu();
			if (menubar != null) {
				nsItem.setMenu(null);
				menubar.insertItem(nsItem, index + 1);
			}
		}
	}
	//TODO - find a way to disable the menu instead of each item
	if (!getEnabled ()) nsItem.setEnabled (false);
}

@Override
void createWidget () {
	checkOrientation (parent);
	super.createWidget ();
	items = new MenuItem [4];
}

@Override
void deregister () {
	super.deregister ();
	display.removeWidget (nsMenu);
}

void destroyItem (MenuItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) return;
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	if (itemCount == 0) items = new MenuItem [4];
	nsMenu.removeItem (item.nsItem);
	if (display.menuBar == this) {
		NSApplication application = display.application;
		NSMenu menubar = application.mainMenu();
		if (menubar != null) {
			NSMenuItem nsItem = item.nsItem;
			menubar.removeItem(nsItem);
		}
	}
}

void fixMenus (Decorations newParent) {
	this.parent = newParent;
}

/**
 * Returns the default menu item or null if none has
 * been previously set.
 *
 * @return the default menu item.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getDefaultItem () {
	checkWidget();
	return defaultItem;
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled menu is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget();
	return (state & DISABLED) == 0;
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getItem (int index) {
	checkWidget ();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
}

/**
 * Returns the number of items contained in the receiver.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget ();
	return itemCount;
}

/**
 * Returns a (possibly empty) array of <code>MenuItem</code>s which
 * are the items in the receiver.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the items in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem [] getItems () {
	checkWidget ();
	MenuItem [] result = new MenuItem [itemCount];
	int index = 0;
	if (items != null) {
		for (int i = 0; i < itemCount; i++) {
			MenuItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				result [index++] = item;
			}
		}
	}
	if (index != result.length) {
		MenuItem [] newItems = new MenuItem[index];
		System.arraycopy(result, 0, newItems, 0, index);
		result = newItems;
	}
	return result;
}

@Override
String getNameText () {
	String result = "";
	MenuItem [] items = getItems ();
	int length = items.length;
	if (length > 0) {
		for (int i=0; i<length-1; i++) {
			result = result + items [i].getNameText() + ", ";
		}
		result = result + items [length-1].getNameText ();
	}
	return result;
}

/**
 * Returns the orientation of the receiver, which will be one of the
 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @return the orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.7
 */
public int getOrientation () {
	checkWidget ();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

/**
 * Returns the receiver's parent, which must be a <code>Decorations</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Decorations getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>MenuItem</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getParentItem () {
	checkWidget ();
	return cascade;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>Menu</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getParentMenu () {
	checkWidget ();
	if (cascade != null) return cascade.parent;
	return null;
}

/**
 * Returns the receiver's shell. For all controls other than
 * shells, this simply returns the control's nearest ancestor
 * shell. Shells return themselves, even if they are children
 * of other shells. Returns null if receiver or its ancestor
 * is the application menubar.
 *
 * @return the receiver's shell or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getParent
 */
public Shell getShell () {
	checkWidget ();
	/* parent is null when application menubar is in use. */
	if (parent != null) return parent.getShell ();
	return null;
}

/**
 * Returns <code>true</code> if the receiver is visible, and
 * <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget ();
	if ((style & SWT.BAR) != 0) {
		if (this == display.appMenuBar)
			return display.application.isActive();
		else
			return this == parent.menuShell ().menuBar;
	}
	if ((style & SWT.POP_UP) != 0) {
		Menu [] popups = display.popups;
		if (popups == null) return false;
		for (int i=0; i<popups.length; i++) {
			if (popups [i] == this) return true;
		}
	}
	return visible;
}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param item the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (MenuItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<itemCount; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled menu is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget ();
	if (this == display.appMenuBar) return getEnabled();
	if (this == display.appMenu) return getEnabled();
	Menu parentMenu = getParentMenu ();
	if (parentMenu == null) {
		return getEnabled () && parent.isEnabled ();
	}
	return getEnabled () && parentMenu.isEnabled ();
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * of the receiver's ancestors are visible and <code>false</code>
 * otherwise.
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getVisible
 */
public boolean isVisible () {
	checkWidget ();
	return getVisible ();
}

@Override
void menu_willHighlightItem(long id, long sel, long menu, long itemID) {
	Widget widget = display.getWidget(itemID);
	if (widget instanceof MenuItem) {
		MenuItem item = (MenuItem)widget;
		item.sendEvent (SWT.Arm);
	}
}

@Override
void menuNeedsUpdate(long id, long sel, long menu) {
	//This code is intentionally commented
	//sendEvent (SWT.Show);
}

@Override
void menuWillOpen(long id, long sel, long menu) {
	visible = true;
	sendEvent (SWT.Show);
	if (isDisposed()) return;
	double width = 0;
	NSAttributedString[] strs = new NSAttributedString[itemCount];
	for (int i=0; i<itemCount; i++) {
		MenuItem item = items [i];
		NSMenuItem nsItem = item.nsItem;
		strs[i] = nsItem.attributedTitle();
		NSImage nsImage = nsItem.image();
		double w = GAP;
		if (strs[i] != null) {
			w += strs[i].size().width;
		}
		if (nsImage != null) {
			w += (nsImage.size().width + GAP);
		}
		if (strs[i] != null) width = Math.max(width, w);
	}
	for (int i=0; i<itemCount; i++) {
		MenuItem item = items [i];
		if (item.updateAccelerator(true)) continue;
		if (item.accelerator != 0 || strs[i] == null || (style & SWT.BAR) != 0 || (item.style & SWT.CASCADE) != 0) continue;
		int accelIndex = item.text.indexOf ('\t');
		if (accelIndex != -1) {
			String accelText = item.text.substring (accelIndex);
			int length = accelText.length ();
			if (length > 1) {
				NSMenuItem nsItem = item.nsItem;
				NSImage nsImage = nsItem.image();
				double tab = width;
				if (nsImage != null) {
					tab -= (nsImage.size().width + GAP);
				}
				NSMutableAttributedString str = new NSMutableAttributedString(strs[i].mutableCopy());

				/* Append accelerator text */
				NSString label = (NSString) new NSString().alloc();
				label = label.initWithString(accelText);
				NSMutableDictionary dict = ((NSMutableDictionary)new NSMutableDictionary().alloc()).initWithCapacity(1);
				dict.setObject (NSFont.menuBarFontOfSize(0), OS.NSFontAttributeName);
				NSAttributedString attribStr = ((NSAttributedString) new NSAttributedString ().alloc ()).initWithString (label, dict);
				dict.release();
				label.release();
				str.appendAttributedString(attribStr);
				attribStr.release();

				/* Align accelerator text */
				NSRange range = new NSRange();
				range.length = str.length();
				NSMutableParagraphStyle paragraphStyle = (NSMutableParagraphStyle)new NSMutableParagraphStyle ().alloc ().init ();
				paragraphStyle.setTabStops(NSArray.array());
				NSTextTab stop = (NSTextTab)new NSTextTab().alloc();
				stop = stop.initWithType(OS.NSLeftTabStopType, tab);
				paragraphStyle.addTabStop(stop);
				stop.release();
				str.addAttribute(OS.NSParagraphStyleAttributeName, paragraphStyle, range);
				paragraphStyle.release ();

				nsItem.setAttributedTitle(str);
				str.release();
			}
		}
	}
}

@Override
void menuDidClose(long id, long sel, long menu) {
	sendEvent (SWT.Hide);
	if (isDisposed()) return;
	visible = false;
	for (int i=0; i<itemCount; i++) {
		MenuItem item = items [i];
		item.updateAccelerator(false);
		if ((item.style & SWT.SEPARATOR) != 0) continue;
		item.updateText();
	}
}

@Override
void register () {
	super.register ();
	display.addWidget (nsMenu, this);
}

@Override
void releaseChildren (boolean destroy) {
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			MenuItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			}
		}
		items = null;
	}
	super.releaseChildren (destroy);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	if (nsMenu != null) nsMenu.release();
	nsMenu = null;
}

@Override
void releaseParent () {
	super.releaseParent ();
	if (cascade != null) cascade.setMenu (null);
	if ((style & SWT.BAR) != 0 && parent != null && this == parent.menuBar) {
		parent.setMenuBar (null);
	}
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	display.removeMenu (this);
	parent = null;
	cascade = defaultItem = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the help events are generated for the control.
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
 * @see HelpListener
 * @see #addHelpListener
 */
public void removeHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the menu events are generated for the control.
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
 * @see MenuListener
 * @see #addMenuListener
 */
public void removeMenuListener (MenuListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Hide, listener);
	eventTable.unhook (SWT.Show, listener);
}

@Override
void reskinChildren (int flags) {
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		item.reskin (flags);
	}
	super.reskinChildren (flags);
}

/**
 * Sets the default menu item to the argument or removes
 * the default emphasis when the argument is <code>null</code>.
 *
 * @param item the default menu item or null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDefaultItem (MenuItem item) {
	checkWidget();
	if (item != null && item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	defaultItem = item;
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled menu is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget();
	if (enabled) {
		state &= ~DISABLED;
	} else {
		state |= DISABLED;
	}
	//TODO - find a way to disable the menu instead of each item
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		if (item != null) {
			/*
			* Feature in the Macintosh.  When a cascade menu
			* item is disabled, rather than disabling the item,
			* the submenu is disabled.
			*
			* There is no fix for this at this time.
			*/
			item.nsItem.setEnabled (enabled && item.getEnabled ());
		}
	}
}

/**
 * Sets the location of the receiver, which must be a popup,
 * to the point specified by the arguments which are relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Also note that the actual location of the menu is dependent
 * on platform specific behavior. For example: on Linux with
 * Wayland this operation is a hint due to lack of global
 * coordinates.
 * </p>
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (int x, int y) {
	checkWidget ();
	this.x = x;
	this.y = y;
	hasLocation = true;
}

/**
 * Sets the location of the receiver, which must be a popup,
 * to the point specified by the argument which is relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Note that the platform window manager ultimately has control
 * over the location of popup menus.
 * </p>
 *
 * @param location the new location for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.1
 */
public void setLocation (Point location) {
	checkWidget ();
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @param orientation new orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.7
 */
public void setOrientation (int orientation) {
	checkWidget ();
}

/**
 * Marks the receiver as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
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
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	if (visible) {
		display.addPopup (this);
	} else {
		display.removePopup (this);
		_setVisible(false);
	}
}

}

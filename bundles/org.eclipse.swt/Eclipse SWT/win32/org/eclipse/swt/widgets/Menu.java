/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

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
	/**
	 * the handle to the OS resource
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public long handle;

	int x, y;
	long hBrush;
	int foreground = -1, background = -1;
	Image backgroundImage;
	boolean hasLocation;
	MenuItem cascade;
	Decorations parent;
	MenuItem selectedMenuItem;

	/* Timer ID for MenuItem ToolTip */
	static final int ID_TOOLTIP_TIMER = 110;

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
	this (parent, checkStyle (style), 0);
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

Menu (Decorations parent, int style, long handle) {
	super (parent, checkStyle (style));
	this.parent = parent;
	this.handle = handle;
	createWidget ();
}

void _setVisible (boolean visible) {
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	long hwndParent = parent.handle;
	if (visible) {
		int flags = OS.TPM_LEFTBUTTON;
		if (OS.GetKeyState (OS.VK_LBUTTON) >= 0) flags |= OS.TPM_RIGHTBUTTON;
		if ((style & SWT.RIGHT_TO_LEFT) != 0) flags |= OS.TPM_RIGHTALIGN;
		if ((parent.style & SWT.MIRRORED) != 0) {
			flags &= ~OS.TPM_RIGHTALIGN;
			if ((style & SWT.LEFT_TO_RIGHT) != 0) flags |= OS.TPM_RIGHTALIGN;
		}
		int nX = x, nY = y;
		if (!hasLocation) {
			int pos = OS.GetMessagePos ();
			nX = OS.GET_X_LPARAM (pos);
			nY = OS.GET_Y_LPARAM (pos);
		}
		hasLocation = false;
		Display display = this.display;
		display.sendPreExternalEventDispatchEvent ();
		/*
		* Feature in Windows.  It is legal use TrackPopupMenu()
		* to display an empty menu as long as menu items are added
		* inside of WM_INITPOPUPMENU.  If no items are added, then
		* TrackPopupMenu() fails and does not send an indication
		* that the menu has been closed.  This is not strictly a
		* bug but leads to unwanted behavior when application code
		* assumes that every WM_INITPOPUPMENU will eventually result
		* in a WM_MENUSELECT, wParam=MAKEWPARAM (0, 0xFFFF), lParam=0 to
		* indicate that the menu has been closed.  The fix is to detect
		* the case when TrackPopupMenu() fails and the number of items in
		* the menu is zero and issue a fake WM_MENUSELECT.
		*/
		boolean success = OS.TrackPopupMenu (handle, flags, nX, nY, 0, hwndParent, null);
		// widget could be disposed at this point
		display.sendPostExternalEventDispatchEvent ();
		if (!success && OS.GetMenuItemCount (handle) == 0) {
			OS.SendMessage (hwndParent, OS.WM_MENUSELECT, OS.MAKEWPARAM (0, 0xFFFF), 0);
		}
	} else {
		OS.SendMessage (hwndParent, OS.WM_CANCELMODE, 0, 0);
	}
	/*
	* Bug in Windows.  After closing a popup menu, the accessibility focus
	* is not returned to the focus control.  This causes confusion for AT users.
	* The fix is to explicitly set the accessibility focus back to the focus control.
	*/
	long hFocus = OS.GetFocus();
	if (hFocus != 0) {
		OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, hFocus, OS.OBJID_CLIENT, 0);
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

static int checkStyle (int style) {
	return checkBits (style, SWT.POP_UP, SWT.BAR, SWT.DROP_DOWN, 0, 0, 0);
}

void createHandle () {
	if (handle != 0) return;
	if ((style & SWT.BAR) != 0) {
		handle = OS.CreateMenu ();
	} else {
		handle = OS.CreatePopupMenu ();
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	updateBackground ();
}

void createItem (MenuItem item, int index) {
	int count = OS.GetMenuItemCount (handle);
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	display.addMenuItem (item);
	/*
	* Bug in Windows.  For some reason, when InsertMenuItem()
	* is used to insert an item without text, it is not possible
	* to use SetMenuItemInfo() to set the text at a later time.
	* The fix is to insert the item with some text.
	*
	* Feature in Windows.  When an empty string is used instead
	* of a space and InsertMenuItem() is used to set a submenu
	* before setting text to a non-empty string, the menu item
	* becomes unexpectedly disabled.  The fix is to insert a
	* space.
	*/
	long hHeap = OS.GetProcessHeap ();
	long pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, 4);
	OS.MoveMemory (pszText, new char [] {' ', '\0'}, 4);
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_ID | OS.MIIM_TYPE | OS.MIIM_DATA;
	info.wID = item.id;
	info.dwItemData = item.id;
	info.fType = item.widgetStyle ();
	info.dwTypeData = pszText;
	boolean success = OS.InsertMenuItem (handle, index, true, info);
	if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	if (!success) {
		display.removeMenuItem (item);
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}

	if (needsMenuCallback()) {
		/*
		 * Bug in Windows: when MIIM_BITMAP is used together with MFT_STRING,
		 * InsertMenuItem() fails. The workaround is to set MIIM_BITMAP with
		 * a separate SetMenuItemInfo().
		 */

		info.fMask = OS.MIIM_BITMAP;
		info.hbmpItem = OS.HBMMENU_CALLBACK;
		OS.SetMenuItemInfo (handle, index, true, info);
	}

	redraw ();
}

void createWidget () {
	checkOrientation (parent);
	initThemeColors ();
	createHandle ();
	parent.addMenu (this);
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_MENU);
}

int defaultForeground () {
	return OS.GetSysColor (OS.COLOR_MENUTEXT);
}

void destroyAccelerators () {
	parent.destroyAccelerators ();
}

void destroyItem (MenuItem item) {
	if (!OS.DeleteMenu (handle, item.id, OS.MF_BYCOMMAND)) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	redraw ();
}

@Override
void destroyWidget () {
	MenuItem cascade = this.cascade;
	long hMenu = handle;
	releaseHandle ();
	if (cascade != null) {
		cascade.setMenu (null, true);
	} else {
		if (hMenu != 0) OS.DestroyMenu (hMenu);
	}
}

void fixMenus (Decorations newParent) {
	if (isDisposed()) {
		return;
	}
	for (MenuItem item : getItems ()) {
		item.fixMenus (newParent);
	}
	parent.removeMenu (this);
	newParent.addMenu (this);
	this.parent = newParent;
}

/**
 * Returns the receiver's background color.
 *
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
/*public*/ Color getBackground () {
	checkWidget ();
	return Color.win32_new (display, background != -1 ? background : defaultBackground ());
}

/**
 * Returns the receiver's background image.
 *
 * @return the background image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
/*public*/ Image getBackgroundImage () {
	checkWidget ();
	return backgroundImage;
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent (or its display if its parent is null),
 * unless the receiver is a menu or a shell. In this case, the
 * location is relative to the display.
 * <p>
 * Note that the bounds of a menu or menu item are undefined when
 * the menu is not visible.  This is because most platforms compute
 * the bounds of a menu dynamically just before it is displayed.
 * </p>
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
/*public*/ Rectangle getBounds () {
	checkWidget ();
	if ((style & SWT.BAR) != 0) {
		if (parent.menuBar != this) {
			return new Rectangle (0, 0, 0, 0);
		}
		long hwndShell = parent.handle;
		MENUBARINFO info = new MENUBARINFO ();
		info.cbSize = MENUBARINFO.sizeof;
		if (OS.GetMenuBarInfo (hwndShell, OS.OBJID_MENU, 0, info)) {
			int width = info.right - info.left;
			int height = info.bottom - info.top;
			return new Rectangle (info.left, info.top, width, height);
		}
	} else {
		int count = OS.GetMenuItemCount (handle);
		if (count != 0) {
			RECT rect1 = new RECT ();
			if (OS.GetMenuItemRect (0, handle, 0, rect1)) {
				RECT rect2 = new RECT ();
				if (OS.GetMenuItemRect (0, handle, count - 1, rect2)) {
					int x = rect1.left - 2, y = rect1.top - 2;
					int width = (rect2.right - rect2.left) + 4;
					int height = (rect2.bottom - rect1.top) + 4;
					return new Rectangle (x, y, width, height);
				}
			}
		}
	}
	return new Rectangle (0, 0, 0, 0);
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
	checkWidget ();
	int id = OS.GetMenuDefaultItem (handle, OS.MF_BYCOMMAND, OS.GMDI_USEDISABLED);
	if (id == -1) return null;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_ID;
	if (OS.GetMenuItemInfo (handle, id, false, info)) {
		return display.getMenuItem (info.wID);
	}
	return null;
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
	checkWidget ();
	return (state & DISABLED) == 0;
}

/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
/*public*/ Color getForeground () {
	checkWidget ();
	return Color.win32_new (display, foreground != -1 ? foreground : defaultForeground ());
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
	int id = 0;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_DATA;
	if (!OS.GetMenuItemInfo (handle, index, true, info)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	id = (int)info.dwItemData;
	return display.getMenuItem (id);
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
	return OS.GetMenuItemCount (handle);
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
	int index = 0, count = 0;
	int length = OS.GetMenuItemCount (handle);
	if (length < 0) {
		int error = OS.GetLastError();
		SWT.error(SWT.ERROR_CANNOT_GET_COUNT, null, " [GetLastError=0x" + Integer.toHexString(error) + "]");//$NON-NLS-1$ $NON-NLS-2$
	}
	MenuItem [] items = new MenuItem [length];
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_DATA;
	while (OS.GetMenuItemInfo (handle, index, true, info)) {
		if (count == items.length) {
			MenuItem [] newItems = new MenuItem [count + 4];
			System.arraycopy (items, 0, newItems, 0, count);
			items = newItems;
		}
		MenuItem item = display.getMenuItem ((int)info.dwItemData);
		if (item != null) items [count++] = item;
		index++;
	}
	if (count == items.length) return items;
	MenuItem [] result = new MenuItem [count];
	System.arraycopy (items, 0, result, 0, count);
	return result;
}

@Override
String getNameText () {
	String result = "";
	MenuItem [] items = getItems ();
	int length = items.length;
	if (length > 0) {
		for (int i=0; i<=length-1; i++) {
			result += (items [i] == null ? "null" : items [i].getNameText())
					+ (i < (length - 1) ? ", " : "");
		}
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
	return parent.getShell ();
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
		return this == parent.menuShell ().menuBar;
	}
	if ((style & SWT.POP_UP) != 0) {
		Menu [] popups = display.popups;
		if (popups == null) return false;
		for (Menu popup : popups) {
			if (popup == this) return true;
		}
	}
	Shell shell = getShell ();
	Menu menu = shell.activeMenu;
	while (menu != null && menu != this) {
		menu = menu.getParentMenu ();
	}
	return this == menu;
}

void hideCurrentToolTip () {
	if (this.selectedMenuItem != null) {
		selectedMenuItem.hideToolTip ();
	}
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
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return -1;
	int index = 0;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_DATA;
	while (OS.GetMenuItemInfo (handle, index, true, info)) {
		if (info.dwItemData == item.id) return index;
		index++;
	}
	return -1;
}

void initThemeColors () {
	if ((style & SWT.BAR) != 0) {
		foreground = display.menuBarForegroundPixel;
		background = display.menuBarBackgroundPixel;
	}
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


boolean needsMenuCallback() {
	/*
	 * Note: using `HBMMENU_CALLBACK` disables XP theme for entire menu
	 * containing the menu item. This has at least the following side
	 * effects:
	 * 1) Menu bar: items are no longer highlighted when mouse hovers
	 * 2) Menu bar: text is now left-aligned without any margin
	 * 3) Popup menu: Images and checkboxes are no longer merged into a single column
	 */

	if ((background != -1) || (backgroundImage != null)) {
		/*
		 * Since XP theming, `MENUINFO.hbrBack` has two issues:
		 * 1) Menu bar completely ignores it
		 * 2) Popup menus ignore it for image/checkbox area
		 * The workaround is to disable XP theme via `HBMMENU_CALLBACK`.
		 */
		return true;
	}

	/*
	 * Otherwise, if menu has foreground color configured, use
	 * `HBMMENU_CALLBACK` to set color in `MenuItem.wmDrawChild` callback.
	 */
	return (foreground != -1);
}

void redraw () {
	if (!isVisible ()) return;
	if ((style & SWT.BAR) != 0) {
		display.addBar (this);
	} else {
		update ();
	}
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	handle = 0;
	cascade = null;
}

@Override
void releaseChildren (boolean destroy) {
	for (MenuItem item : getItems ()) {
		if (item != null && !item.isDisposed ()) {
			item.release (false);
		}
	}
	super.releaseChildren (destroy);
}

@Override
void releaseParent () {
	super.releaseParent ();
	if ((style & SWT.BAR) != 0) {
		display.removeBar (this);
		if (this == parent.menuBar) {
			parent.setMenuBar (null);
		}
	} else {
		if ((style & SWT.POP_UP) != 0) {
			display.removePopup (this);
		}
	}
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	backgroundImage = null;
	if (hBrush != 0) OS.DeleteObject (hBrush);
	hBrush = 0;
	if (parent != null) parent.removeMenu (this);
	parent = null;
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
	for (MenuItem item : getItems ()) {
		item.reskin (flags);
	}
	super.reskinChildren (flags);
}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
/*public*/ void setBackground (Color color) {
	checkWidget ();
	int pixel = -1;
	if (color != null) {
		if (color.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		pixel = color.handle;
	}
	if (pixel == background) return;
	background = pixel;
	updateBackground ();
}

/**
 * Sets the receiver's background image to the image specified
 * by the argument, or to the default system color for the control
 * if the argument is null.  The background image is tiled to fill
 * the available space.
 *
 * @param image the new image (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument is not a bitmap</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
/*public*/ void setBackgroundImage (Image image) {
	checkWidget ();
	if (image != null) {
		if (image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (image.type != SWT.BITMAP) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (backgroundImage == image) return;
	backgroundImage = image;
	updateBackground ();
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
/*public*/ void setForeground (Color color) {
	checkWidget ();
	int pixel = -1;
	if (color != null) {
		if (color.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		pixel = color.handle;
	}
	if (pixel == foreground) return;
	foreground = pixel;
	updateForeground ();
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
	checkWidget ();
	int newID = -1;
	if (item != null) {
		if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (item.parent != this) return;
		newID = item.id;
	}
	int oldID = OS.GetMenuDefaultItem (handle, OS.MF_BYCOMMAND, OS.GMDI_USEDISABLED);
	if (newID == oldID) return;
	OS.SetMenuDefaultItem (handle, newID, OS.MF_BYCOMMAND);
	redraw ();
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
	checkWidget ();
	state &= ~DISABLED;
	if (!enabled) state |= DISABLED;
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
	setLocationInPixels(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y));
}

void setLocationInPixels (int x, int y) {
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
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
	location = DPIUtil.autoScaleUp(location);
	setLocationInPixels(location.x, location.y);
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
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	_setOrientation (orientation);
}

void _setOrientation (int orientation) {
	int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
	if ((orientation & flags) == 0 || (orientation & flags) == flags) return;
	style &= ~flags;
	style |= orientation & flags;
	style &= ~SWT.FLIP_TEXT_DIRECTION;
	for (MenuItem itm : getItems ()) {
		itm.setOrientation (orientation);
	}
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
		_setVisible (false);
	}
}

void update () {
	if ((style & SWT.BAR) != 0) {
		if (this == parent.menuBar) OS.DrawMenuBar (parent.handle);
		return;
	}
	boolean hasCheck = false, hasImage = false;
	for (MenuItem item : getItems ()) {
		if (item.image != null) {
			if ((hasImage = true) && hasCheck) break;
		}
		if ((item.style & (SWT.CHECK | SWT.RADIO)) != 0) {
			if ((hasCheck = true) && hasImage) break;
		}
	}

	/* Update the menu to hide or show the space for bitmaps */
	MENUINFO lpcmi = new MENUINFO ();
	lpcmi.cbSize = MENUINFO.sizeof;
	lpcmi.fMask = OS.MIM_STYLE;
	OS.GetMenuInfo (handle, lpcmi);
	if (hasImage && !hasCheck) {
		lpcmi.dwStyle |= OS.MNS_CHECKORBMP;
	} else {
		lpcmi.dwStyle &= ~OS.MNS_CHECKORBMP;
	}
	OS.SetMenuInfo (handle, lpcmi);
}

void updateBackground () {
	if (hBrush != 0) OS.DeleteObject (hBrush);
	hBrush = 0;

	if (backgroundImage != null)
		hBrush = OS.CreatePatternBrush (backgroundImage.handle);
	else if (background != -1)
		hBrush = OS.CreateSolidBrush (background);

	MENUINFO lpcmi = new MENUINFO ();
	lpcmi.cbSize = MENUINFO.sizeof;
	lpcmi.fMask = OS.MIM_BACKGROUND;
	lpcmi.hbrBack = hBrush;
	OS.SetMenuInfo (handle, lpcmi);
}

void updateForeground () {
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	int index = 0;
	while (OS.GetMenuItemInfo (handle, index, true, info)) {
		info.fMask = OS.MIIM_BITMAP;
		info.hbmpItem = OS.HBMMENU_CALLBACK;
		OS.SetMenuItemInfo (handle, index, true, info);
		index++;
	}
	redraw ();
}

LRESULT wmTimer (long wParam, long lParam) {
	if (wParam == ID_TOOLTIP_TIMER) {
		POINT pt = new POINT ();
		OS.GetCursorPos (pt);
		if (selectedMenuItem != null) {
			RECT rect = new RECT ();
			boolean success = OS.GetMenuItemRect (0, selectedMenuItem.parent.handle, selectedMenuItem.index, rect);
			if (!success) return null;
			if (OS.PtInRect (rect, pt)) {
				// Mouse cursor is within the bounds of menu item
				selectedMenuItem.showTooltip (pt.x, pt.y + OS.GetSystemMetrics(OS.SM_CYCURSOR) / 2 + 5);
			} else {
				/*
				 * Mouse cursor is outside the bounds of the menu item:
				 * Keyboard or mnemonic was used to select menu item
				 */
				selectedMenuItem.showTooltip ((rect.right + rect.left) / 2, rect.bottom + 5);
			}
		}
	}

	return null;
}
}

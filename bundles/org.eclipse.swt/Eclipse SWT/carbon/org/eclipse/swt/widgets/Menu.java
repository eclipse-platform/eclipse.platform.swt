package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are user interface objects that contain
 * menu items.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BAR, DROP_DOWN, POP_UP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Help, Hide, Show </dd>
 * </dl>
 * <p>
 * Note: Only one of BAR, DROP_DOWN and POP_UP may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class Menu extends Widget {
	int x, y;
	boolean hasLocation;
	MenuItem cascade, defaultItem;
	Decorations parent;
	// AW
	private static int fgMenuId= 5000;
	// AW

/**
 * Constructs a new instance of this class given its parent,
 * and sets the style for the instance so that the instance
 * will be a popup menu on the given parent's shell.
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
	this (checkNull (parent).getShell (), SWT.POP_UP);
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Decorations parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget ();
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Menu</code>) and sets the style
 * for the instance so that the instance will be a drop-down
 * menu on the given parent's parent.
 *
 * @param parent a menu which will be the parent of the new instance (cannot be null)
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
 *
 * @param parent a menu item which will be the parent of the new instance (cannot be null)
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
	Display display= getDisplay();
	int menuHandle[]= new int[1];
	if (OS.CreateNewMenu(fgMenuId++, 0, menuHandle) == OS.kNoErr)
		handle= menuHandle[0];
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.RetainMenu(handle);
	OS.InstallEventHandler(OS.GetMenuEventTarget(handle), display.fMenuProc,
		new int[] {
			OS.kEventClassMenu, OS.kEventMenuOpening,
			OS.kEventClassMenu, OS.kEventMenuClosed
		},
		handle
	);
}

void createItem (MenuItem item, int index) {
	checkWidget ();
	int count = OS.CountMenuItems (handle);
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	parent.add (item);
	boolean success = false;
	/*
	if (OS.IsWinCE) {
		int flags = OS.MF_BYPOSITION;
		if ((style & SWT.SEPARATOR) != 0) flags |= OS.MF_SEPARATOR;
		success = OS.InsertMenu (handle, index, flags, item.id, null); 
	} else {
		int hHeap = OS.GetProcessHeap ();
		int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, TCHAR.sizeof);
		MENUITEMINFO info = new MENUITEMINFO ();
		info.cbSize = MENUITEMINFO.sizeof;
		info.fMask = OS.MIIM_ID | OS.MIIM_TYPE;
		info.wID = item.id;
		info.fType = item.widgetStyle ();
		info.dwTypeData = pszText;
		success = OS.InsertMenuItem (handle, index, true, info);
		if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	}
	*/
	
	/*
	if ((style & SWT.SEPARATOR) != 0) return OS.MFT_SEPARATOR;
	if ((style & SWT.RADIO) != 0) return OS.MFT_RADIOCHECK;
	return OS.MFT_STRING;
	*/
	
	int attributes= 0;
	if ((item.style & SWT.SEPARATOR) != 0) 
		attributes= OS.kMenuItemAttrSeparator;
	if (OS.InsertMenuItemTextWithCFString(handle, 0, (short) index, attributes, item.id) == OS.kNoErr)
		success= true;
	
	if (!success) {
		parent.remove (item);
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	/* AW	
	redraw ();
	*/
}

void createWidget () {
	createHandle ();
	parent.add (this);
	register ();
}

void destroyAcceleratorTable () {
	/* AW
	parent.destroyAcceleratorTable ();
	*/
}

void destroyItem (MenuItem item) {
	/* AW
	if (!OS.RemoveMenu (handle, item.id, OS.MF_BYCOMMAND)) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	*/
	short[] index= new short[1];
	OS.GetIndMenuItemWithCommandID(handle, item.id, 1, null, index);
	if (index[0] >= 1) {
		OS.DeleteMenuItem(handle, index[0]);
	} else
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	
	redraw ();
}

void destroyWidget () {
	int hMenu = handle;
	releaseHandle ();
	if (hMenu != 0) {
		/* AW
		OS.DestroyMenu (hMenu);
		*/
		OS.DisposeMenu (hMenu);
	}
}

/**
 * Returns the default menu item or null if none has
 * been previously set.
 *
 * @return the default menu item.
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getDefaultItem () {
	checkWidget();
	return defaultItem;
}

public Display getDisplay () {
	Decorations parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getEnabled () {
	checkWidget ();
	/* AW
	return (state & DISABLED) == 0;
	*/
	return OS.IsMenuItemEnabled(handle, (short)0);
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
	/* AW
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_ID;
	if (!OS.GetMenuItemInfo (handle, index, true, info)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	return parent.findMenuItem (info.wID);
	*/
	int[] commandID= new int[1];
	if (OS.GetMenuItemCommandID(handle, (short)(index+1), commandID) != OS.kNoErr)
		error (SWT.ERROR_INVALID_RANGE);
	return parent.findMenuItem (commandID[0]);
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
	return OS.CountMenuItems (handle);
}

/**
 * Returns an array of <code>MenuItem</code>s which are the items
 * in the receiver. 
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
	int index = 0;
	int length = OS.CountMenuItems(handle);
	MenuItem [] items = new MenuItem [length];
	/* AW
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_ID;
	while (OS.GetMenuItemInfo (handle, index, true, info)) {
	*/
	int[] commandID= new int[1];	
	while (OS.GetMenuItemCommandID(handle, (short)(index+1), commandID) == OS.kNoErr) {
		if (index == items.length) {
			MenuItem [] newItems = new MenuItem [index + 4];
			System.arraycopy (newItems, 0, items, 0, index);
			items = newItems;
		}
		items [index] = parent.findMenuItem (commandID [0]);
		if (items [index] != null)
			index++;
	}
	if (index == items.length) return items;
	MenuItem [] result = new MenuItem [index];
	System.arraycopy (result, 0, items, 0, index);
	return result;
}

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
 * of other shells.
 *
 * @return the receiver's shell
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
	return true;
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
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (MenuItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	/* AW
	int index = 0;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_ID;
	while (OS.GetMenuItemInfo (handle, index, true, info)) {
		if (info.wID == item.id) return index;
		index++;
	}
	*/
	
	int[] menu= new int[1];
	short[] index= new short[1];
	if (OS.GetIndMenuItemWithCommandID(handle, item.id, 1, menu, index) == OS.kNoErr) {
		if (handle == menu[0])	// ensure that we found item not in submenu
			return index[0];
	}
	return -1;
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isEnabled () {
	checkWidget ();
	Menu parentMenu = getParentMenu ();
	if (parentMenu == null) return getEnabled ();
	return getEnabled () && parentMenu.isEnabled ();
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
public boolean isVisible () {
	checkWidget ();
	return getVisible ();
}

int processHide (Object callData) {
	sendEvent (SWT.Hide);
	return 0;
}

int processShow (Object callData) {
	sendEvent (SWT.Show);
	return 0;
}

void redraw () {
	if ((style & SWT.BAR) != 0) {
		//AW OS.DrawMenuBar (parent.handle);
		return;
	}
	/* AW
	if ((OS.WIN32_MAJOR << 16 | OS.WIN32_MINOR) < (4 << 16 | 10)) {
		return;
	}
	boolean hasCheck = false, hasImage = false;
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		if (item.getImage () != null) {
			if ((hasImage = true) && hasCheck) break;
		}
		if ((item.getStyle () & (SWT.CHECK | SWT.RADIO)) != 0) {
			if ((hasCheck = true) && hasImage) break;
		}
	}
	if (OS.IsWinCE) return;
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
	*/
}

void releaseChild () {
	super.releaseChild ();
	if (cascade != null) cascade.setMenu (null);
	if ((style & SWT.BAR) != 0 && this == parent.menuBar) {
		parent.setMenuBar (null);
	}
}

void releaseWidget () {
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		if (!item.isDisposed ()) item.releaseWidget ();
	}
	super.releaseWidget ();
	if (parent != null) parent.remove (this);
	parent = null;
	cascade = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the help events are generated for the control.
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
 * @see #addMenuListener
 */
public void removeMenuListener (MenuListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Hide, listener);
	eventTable.unhook (SWT.Show, listener);
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
 * and disables it otherwise. A disabled control is typically
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
	/* AW
	state &= ~DISABLED;
	if (!enabled) state |= DISABLED;
	*/
	if (enabled)
		OS.EnableMenuItem(handle, (short)0);
	else
		OS.DisableMenuItem(handle, (short)0);
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the display.
 * <p>
 * Note:  This is different from most widgets where the
 * location of the widget is relative to the parent.
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
	this.x = x;  this.y = y;
	hasLocation = true;
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
	int hwndParent = parent.handle;
	if (!visible) {
		/* AW
		OS.SendMessage (hwndParent, OS.WM_CANCELMODE, 0, 0);
		*/
		return;
	}
	/* AW
	int flags = OS.TPM_LEFTBUTTON | OS.TPM_RIGHTBUTTON | OS.TPM_LEFTALIGN;
	*/
	int nX = x, nY = y;
	if (!hasLocation) {
		/* AW
		int pos = OS.GetMessagePos ();
		nX = (short) (pos & 0xFFFF);
		nY = (short) (pos >> 16);
		*/
		System.out.println("Menu.setVisible: nyi");
	}
	/*
	* Feature in Windows.  It is legal use TrackPopupMenu ()
	* to display an empty menu as long menu items are added
	* inside of WM_INITPOPUPMENU.  If no items are added, then
	* TrackPopupMenu () fails and does not send an indication
	* that the menu has been closed.  This is not strictly a
	* bug but leads to unwanted behavior when application code
	* assumes that every WM_INITPOPUPMENU will eventually result
	* in a WM_MENUSELECT, wParam=0xFFFF0000, lParam=0 to indicate
	* that the menu has been closed.  The fix is to detect the
	* case when TrackPopupMenu fails and the number of items in
	* the menu is zero and issue a fake WM_MENUSELECT.
	*/
	/* AW
	boolean success = OS.TrackPopupMenu (handle, flags, nX, nY, 0, hwndParent, null);
	if (!success && GetMenuItemCount (handle) == 0) {
		OS.SendMessage (hwndParent, OS.WM_MENUSELECT, 0xFFFF0000, 0);
	}
	*/
	int defaultIndex= -1;
	if (defaultItem != null)
		defaultIndex= indexOf(defaultItem);
	getDisplay().menuIsVisible(true);
	int result= OS.PopUpMenuSelect(handle, (short)nY, (short)nX, (short)(defaultIndex+1));
	getDisplay().menuIsVisible(false);
	short menuID= OS.HiWord(result);
	if (menuID != 0) {
		Menu menu= getShell().findMenu(menuID);
		if (menu != null)
			menu.handleMenu(result);
	}
}

///////////////////////////////////////////////////
// Mac stuff
///////////////////////////////////////////////////

	void handleMenu(int menuResult) {
		int index= OS.LoWord(menuResult)-1;	
		if (index >= 0 && index < getItemCount()) {
			MenuItem item= getItem(index);
			if (item != null)
				item.handleMenuSelect();
		}
	}
	
}

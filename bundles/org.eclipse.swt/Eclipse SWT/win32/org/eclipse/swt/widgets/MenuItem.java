package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a selectable user interface object
 * that issues notification when pressed and released. 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CHECK, CASCADE, PUSH, RADIO, SEPARATOR</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Arm, Help, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, CASCADE, PUSH, RADIO and SEPARATOR
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class MenuItem extends Item {
	Menu parent, menu;
	int id, accelerator;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Menu</code>) and a style value
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
 * @param parent a menu control which will be the parent of the new instance (cannot be null)
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
 * @see SWT#CHECK
 * @see SWT#CASCADE
 * @see SWT#PUSH
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public MenuItem (Menu parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Menu</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
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
 * @param parent a menu control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#CHECK
 * @see SWT#CASCADE
 * @see SWT#PUSH
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public MenuItem (Menu parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the arm events are generated for the control, by sending
 * it one of the messages defined in the <code>ArmListener</code>
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
 * @see ArmListener
 * @see #removeArmListener
 */
public void addArmListener (ArmListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Arm, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the help events are generated for the control, by sending
 * it one of the messages defined in the <code>HelpListener</code>
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
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the stateMask field of the event object is valid.
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.CASCADE, 0);
}

void fillAccel (ACCEL accel) {
	accel.fVirt = 0;
	accel.cmd = accel.key = 0;
	if (accelerator == 0) return;
	int fVirt = OS.FVIRTKEY;
	int key = accelerator & ~(SWT.ALT | SWT.CTRL | SWT.SHIFT);
	int vKey = Display.untranslateKey (key);
	if (vKey != 0) {
		key = vKey;	
	} else {
		switch (key) {
			/*
			* Bug in Windows.  For some reason, VkKeyScan
			* fails to map ESC to VK_ESCAPE and DEL to
			* VK_DELETE.  The fix is to map these keys
			* as a special case.
			*/
			case 27: key = OS.VK_ESCAPE; break;
			case 127: key = OS.VK_DELETE; break;
			default: {
				key = wcsToMbcs ((char) key);
				if (key == 0) return;
				if (OS.IsWinCE) {
					key = OS.CharUpper ((short) key);
				} else {
					vKey = OS.VkKeyScan ((short) key) & 0xFF;
					if (vKey == -1) {
						fVirt = 0;
					} else {
						key = vKey;
					}
				}
			}
		}
	}
	accel.key = (short) key;
	accel.cmd = (short) id;
	accel.fVirt = (byte) fVirt;
	if ((accelerator & SWT.ALT) != 0) accel.fVirt |= OS.FALT;
	if ((accelerator & SWT.CTRL) != 0) accel.fVirt |= OS.FCONTROL;
	if ((accelerator & SWT.SHIFT) != 0) accel.fVirt |= OS.FSHIFT;
}

/**
 * Return the widget accelerator.  An accelerator is the bit-wise
 * OR of zero or more modifier masks and a key. Examples:
 * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>.
 *
 * @return the accelerator
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAccelerator () {
	checkWidget ();
	return accelerator;
}

public Display getDisplay () {
	Menu parent = this.parent;
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
 * 
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget ();
	int hMenu = parent.handle;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_STATE;
	boolean success;
	if (OS.IsWinCE) {
		int index = parent.indexOf (this);
		if (index == -1) error (SWT.ERROR_CANNOT_GET_ENABLED);
		success = OS.GetMenuItemInfo (hMenu, index, true, info);
	} else {
		success = OS.GetMenuItemInfo (hMenu, id, false, info);
	}
	if (!success) error (SWT.ERROR_CANNOT_GET_ENABLED);
	return (info.fState & (OS.MFS_DISABLED | OS.MFS_GRAYED)) == 0;
}

/**
 * Returns the receiver's cascade menu if it has one or null
 * if it does not. Only <code>CASCADE</code> menu items can have
 * a pull down menu. The sequence of key strokes, button presses 
 * and/or button releases that are used to request a pull down
 * menu is platform specific.
 *
 * @return the receiver's menu
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getMenu () {
	checkWidget ();
	return menu;
}

String getNameText () {
	if ((style & SWT.SEPARATOR) != 0) return "|";
	return super.getNameText ();
}

/**
 * Returns the receiver's parent, which must be a <code>Menu</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked.
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	int hMenu = parent.handle;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_STATE;
	boolean success = OS.GetMenuItemInfo (hMenu, id, false, info);
	if (!success) error (SWT.ERROR_CANNOT_GET_SELECTION);
	return (info.fState & OS.MFS_CHECKED) !=0;
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled control is typically not selectable from the
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
	return getEnabled () && parent.isEnabled ();
}

void releaseChild () {
	super.releaseChild ();
	if (menu != null) menu.dispose ();
	menu = null;
	parent.destroyItem (this);
}

void releaseWidget () {
	if (menu != null) {
		menu.releaseWidget ();
		menu.releaseHandle ();
	}
	menu = null;
	super.releaseWidget ();
	if (accelerator != 0) {
		parent.destroyAcceleratorTable ();
	}
	accelerator = 0;
	Decorations shell = parent.parent;
	shell.remove (this);
	parent = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the arm events are generated for the control.
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
 * @see ArmListener
 * @see #addArmListener
 */
public void removeArmListener (ArmListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Arm, listener);
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Sets the widget accelerator.  An accelerator is the bit-wise
 * OR of zero or more modifier masks and a key. Examples:
 * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>.
 *
 * @param accelerator an integer that is the bit-wise OR of masks and a key
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAccelerator (int accelerator) {
	checkWidget ();
	this.accelerator = accelerator;
	parent.destroyAcceleratorTable ();
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
	int hMenu = parent.handle;
	if (OS.IsWinCE) {
		int index = parent.indexOf (this);
		if (index == -1) return;
		int uEnable = OS.MF_BYPOSITION | (enabled ? OS.MF_ENABLED : OS.MF_GRAYED);
		OS.EnableMenuItem (hMenu, index, uEnable);
		if (OS.IsPPC) {
			/* if it is a top level menu item, set the state in the corresponding tool item */
			Decorations shell = parent.parent;
			if (parent == shell.menuBar) {
				int fsState = OS.SendMessage (shell.hwndTB, OS.TB_GETSTATE, id, 0);
				fsState &= ~OS.TBSTATE_ENABLED;
				if (enabled) fsState |= OS.TBSTATE_ENABLED;
				OS.SendMessage (shell.hwndTB, OS.TB_SETSTATE, id, fsState);
			}
		}
	} else {
		MENUITEMINFO info = new MENUITEMINFO ();
		info.cbSize = MENUITEMINFO.sizeof;
		info.fMask = OS.MIIM_STATE;
		boolean success = OS.GetMenuItemInfo (hMenu, id, false, info);
		if (!success) error (SWT.ERROR_CANNOT_SET_ENABLED);
		int bits = OS.MFS_DISABLED | OS.MFS_GRAYED;
		if (enabled) {
			if ((info.fState & bits) == 0) return;
			info.fState &= ~bits;
		} else {
			if ((info.fState & bits) == bits) return;
			info.fState |= bits;
		}
		success = OS.SetMenuItemInfo (hMenu, id, false, info);
		if (!success) error (SWT.ERROR_CANNOT_SET_ENABLED);
	}
	parent.redraw ();
}

/**
 * Sets the image the receiver will display to the argument.
 * <p>
 * Note: This feature is not available on all window systems (for example, Window NT),
 * in which case, calling this method will silently do nothing.
 *
 * @param menu the image to display
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image image) {
	checkWidget ();
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	if (OS.IsWinCE) return;
	if ((OS.WIN32_MAJOR << 16 | OS.WIN32_MINOR) < (4 << 16 | 10)) {
		return;
	}
	int hMenu = parent.handle;
	int hHeap = OS.GetProcessHeap ();
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_BITMAP;
	if (image != null) info.hbmpItem = OS.HBMMENU_CALLBACK;
	boolean success = OS.SetMenuItemInfo (hMenu, id, false, info);
	/*
	* This code is intentionally commented.
	*/
//	if (!success) error (SWT.ERROR_CANNOT_SET_TEXT);
	parent.redraw ();
}

/**
 * Sets the receiver's pull down menu to the argument.
 * Only <code>CASCADE</code> menu items can have a
 * pull down menu. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pull down
 * menu is platform specific.
 *
 * @param menu the new pull down menu
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_MENU_NOT_DROP_DOWN - if the menu is not a drop down menu</li>
 *    <li>ERROR_MENUITEM_NOT_CASCADE - if the menu item is not a <code>CASCADE</code></li>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li>
 *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMenu (Menu menu) {
	checkWidget ();

	/* Check to make sure the new menu is valid */
	if ((style & SWT.CASCADE) == 0) {
		error (SWT.ERROR_MENUITEM_NOT_CASCADE);
	}
	if (menu != null) {
		if (menu.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if ((menu.style & SWT.DROP_DOWN) == 0) {
			error (SWT.ERROR_MENU_NOT_DROP_DOWN);
		}
		if (menu.parent != parent.parent) {
			error (SWT.ERROR_INVALID_PARENT);
		}
	}

	/* Assign the new menu */
	Menu oldMenu = this.menu;
	if (oldMenu == menu) return;
	if (oldMenu != null) oldMenu.cascade = null;
	this.menu = menu;

	/* Assign the new menu in the OS */
	
	/*
	* Feature in Windows.  When SetMenuItemInfo () is used to
	* set a submenu and the menu item already has a submenu,
	* Windows destroys the previous menu.  This is undocumented
	* and unexpected but not necessarily wrong.  The fix is to
	* remove the item with RemoveMenu () which does not destroy
	* the submenu and then insert the item with InsertMenuItem ().
	*/
	int hMenu = parent.handle;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_DATA;
	int index = 0;
	while (OS.GetMenuItemInfo (hMenu, index, true, info)) {
		if (info.dwItemData == id) break;
		index++;
	}
	if (info.dwItemData != id) return;
	int cch = 128;
	int hHeap = OS.GetProcessHeap ();
	int byteCount = cch * TCHAR.sizeof;
	int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	info.fMask = OS.MIIM_STATE | OS.MIIM_ID | OS.MIIM_TYPE | OS.MIIM_DATA;
	info.dwTypeData = pszText;
	info.cch = cch;
	boolean success = OS.GetMenuItemInfo (hMenu, index, true, info);
	if (menu != null) {
		menu.cascade = this; 
		info.fMask |= OS.MIIM_SUBMENU;
		info.hSubMenu = menu.handle;
	}
	OS.RemoveMenu (hMenu, index, OS.MF_BYPOSITION);
	if (OS.IsWinCE) {
		/*
		* On WinCE, InsertMenuItem is not available.  SetMenuItemInfo
		* does not set the menu item state and submenu use InsertMenu
		* to set these fields and SetMenuItemInfo to set the menu item
		* data.  NOTE: SetMenuItemInfo is also used to set the string
		* that was queried from the original menu item.
		*/
		int uIDNewItem = id;
		int uFlags = OS.MF_BYPOSITION;
		if (menu != null) {
			uFlags |= OS.MF_POPUP;
			uIDNewItem = menu.handle;
		}
		TCHAR lpNewItem = new TCHAR (0, "", true);
		success = OS.InsertMenu (hMenu, index, uFlags, uIDNewItem, lpNewItem);
		if (success) {
			info.fMask = OS.MIIM_DATA | OS.MIIM_TYPE;
			success = OS.SetMenuItemInfo (hMenu, index, true, info);
			if ((info.fState & (OS.MFS_DISABLED | OS.MFS_GRAYED)) != 0) {
				OS.EnableMenuItem (hMenu, index, OS.MF_BYPOSITION | OS.MF_GRAYED);
			}
			if ((info.fState & OS.MFS_CHECKED) != 0) {
				OS.CheckMenuItem (hMenu, index, OS.MF_BYPOSITION | OS.MF_CHECKED);
			}
			if (OS.IsPPC) {
				if (success) {
					/* if it is a top level menu item, update the corresponding tool item */
					Decorations shell = parent.parent;
					if (parent == shell.menuBar) {
						OS.SendMessage (shell.hwndCB, OS.SHCMBM_SETSUBMENU, id, uIDNewItem);
					}
				}
			}
		}
		
	} else {
		success = OS.InsertMenuItem (hMenu, index, true, info);
	}
	if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	if (!success) error (SWT.ERROR_CANNOT_SET_MENU);
	parent.destroyAcceleratorTable ();
}

/**
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked.
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	int hMenu = parent.handle;
	if (OS.IsWinCE) {
		int index = parent.indexOf (this);
		if (index == -1) return;
		int uCheck = OS.MF_BYPOSITION | (selected ? OS.MF_CHECKED : OS.MF_UNCHECKED);
		OS.CheckMenuItem (hMenu, index, uCheck);
	} else {
		MENUITEMINFO info = new MENUITEMINFO ();
		info.cbSize = MENUITEMINFO.sizeof;
		info.fMask = OS.MIIM_STATE;
		boolean success = OS.GetMenuItemInfo (hMenu, id, false, info);
		if (!success) error (SWT.ERROR_CANNOT_SET_SELECTION);
		info.fState &= ~OS.MFS_CHECKED;
		if (selected) info.fState |= OS.MFS_CHECKED;
		success = OS.SetMenuItemInfo (hMenu, id, false, info);
		if (!success) error (SWT.ERROR_CANNOT_SET_SELECTION);
	}
	parent.redraw ();
}

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	if (text.equals (string)) return;
	super.setText (string);
	boolean fixPPCMenuBar = false;
	if (OS.IsPPC) {
		Decorations shell = parent.parent;
		if (parent == shell.menuBar) {
			fixPPCMenuBar = true;
			/*
			* Bug in WinCE PPC.  Tool items on the menubar don't resize
			* correctly when the character '&' is used (even when it
			* is a sequence '&&').  The fix is to remove all '&' from
			* the string. 
			*/
			if (string.indexOf ('&') != -1) {
				int length = string.length ();
				char[] text = new char [length];
				string.getChars( 0, length, text, 0);
				int i = 0, j = 0;
				for (i=0; i<length; i++) {
					if (text[i] != '&') text [j++] = text [i];
				}
				if (j < i) string = new String (text, 0, j);
			}
		}
	}
	int hMenu = parent.handle;
	int hHeap = OS.GetProcessHeap ();
	/* Use the character encoding for the default locale */
	TCHAR buffer = new TCHAR (0, string, true);
	int byteCount = buffer.length () * TCHAR.sizeof;
	int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (pszText, buffer, byteCount);
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_TYPE;
	info.fType = widgetStyle ();
	info.dwTypeData = pszText;
	boolean success = OS.SetMenuItemInfo (hMenu, id, false, info);
	/*
	* Bug in Windows 2000.  For some reason, when MIIM_TYPE is set
	* on a menu item that also has MIIM_BITMAP, the MIIM_TYPE clears
	* the MIIM_BITMAP style.  The fix is to reset both MIIM_BITMAP.
	* Note, this does not happen on Windows 98.
	*/
	if (!OS.IsWinCE) {
		if ((OS.WIN32_MAJOR << 16 | OS.WIN32_MINOR) >= (4 << 16 | 10)) {
			if (image != null) {
				info.fMask = OS.MIIM_BITMAP;
				info.hbmpItem = OS.HBMMENU_CALLBACK;
				success = OS.SetMenuItemInfo (hMenu, id, false, info);
			}
		}
	}
	if (fixPPCMenuBar) {
		Decorations shell = parent.parent;
		TBBUTTONINFO info2 = new TBBUTTONINFO ();
		info2.cbSize = TBBUTTONINFO.sizeof;
		info2.dwMask = OS.TBIF_TEXT;
		info2.pszText = pszText;
		OS.SendMessage (shell.hwndTB, OS.TB_SETBUTTONINFO, id, info2);
	}
	if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	if (!success) error (SWT.ERROR_CANNOT_SET_TEXT);
	parent.redraw ();
}

int widgetStyle () {
	if ((style & SWT.SEPARATOR) != 0) return OS.MFT_SEPARATOR;
	if ((style & SWT.RADIO) != 0) return OS.MFT_RADIOCHECK;
	return OS.MFT_STRING;
}

LRESULT wmCommandChild (int wParam, int lParam) {
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		setSelection (!getSelection ());
	}
	Event event = new Event ();
	postEvent (SWT.Selection, event);
	return null;
}

LRESULT wmDrawChild (int wParam, int lParam) {
	DRAWITEMSTRUCT struct = new DRAWITEMSTRUCT ();
	OS.MoveMemory (struct, lParam, DRAWITEMSTRUCT.sizeof);
	if (image != null) {
		/*
		* This code intentionally commented.
		*/
//		GC gc = GC.win32_new (struct.hDC, null);
//		gc.drawImage (image, struct.left, struct.top);
		int hImage = image.handle;
		switch (image.type) {
			case SWT.BITMAP:
				BITMAP bm = new BITMAP ();
				OS.GetObject (hImage, BITMAP.sizeof, bm);
				int hDC = OS.CreateCompatibleDC (struct.hDC);
				int oldBitmap = OS.SelectObject (hDC, hImage);
				OS.BitBlt (struct.hDC, struct.left, struct.top + 2, bm.bmWidth, bm.bmHeight, hDC, 0, 0, OS.SRCCOPY);
				OS.SelectObject (hDC, oldBitmap);
				OS.DeleteDC (hDC);
				break;
			case SWT.ICON:
				OS.DrawIconEx (struct.hDC, struct.left, struct.top + 2, hImage, 0, 0, 0, 0, OS.DI_NORMAL);
				break;
		}
	}
	return null;
}

LRESULT wmMeasureChild (int wParam, int lParam) {
	MEASUREITEMSTRUCT struct = new MEASUREITEMSTRUCT ();
	OS.MoveMemory (struct, lParam, MEASUREITEMSTRUCT.sizeof);
	if (image != null) {
		Rectangle rect = image.getBounds ();
		struct.itemWidth = rect.width + 4;
		struct.itemHeight = rect.height + 4;
	}
	OS.MoveMemory (lParam, struct, MEASUREITEMSTRUCT.sizeof);	
	return null;
}

}

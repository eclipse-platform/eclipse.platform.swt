/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
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
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class MenuItem extends Item {
	Menu parent, menu;
	int /*long*/ hBitmap;
	int id, accelerator, userId;
	/*
	* Feature in Windows.  On Windows 98, it is necessary
	* to add 4 pixels to the width of the image or the image
	* and text are too close.  On other Windows platforms,
	* this causes the text of the longest item to touch the
	* accelerator text.  The fix is to use smaller margins
	* everywhere but on Windows 98.
	*/
	final static int MARGIN_WIDTH = OS.IsWin95 ? 2 : 1;
	final static int MARGIN_HEIGHT = OS.IsWin95 ? 2 : 1;

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
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
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

MenuItem (Menu parent, Menu menu, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	this.menu = menu;	
	if (menu != null) menu.cascade = this;
	display.addMenuItem (this);
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
 * be notified when the menu item is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the stateMask field of the event object is valid.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 * <p>
 * When the <code>SWT.RADIO</code> style bit is set, the <code>widgetSelected</code> method is
 * also called when the receiver loses selection because another item in the same radio group 
 * was selected by the user. During <code>widgetSelected</code> the application can use
 * <code>getSelection()</code> to determine the current selected state of the receiver.
 * </p>
 * 
 * @param listener the listener which should be notified when the menu item is selected by the user
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

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

boolean fillAccel (ACCEL accel) {
	accel.cmd = accel.key = accel.fVirt = 0;
	if (accelerator == 0 || !getEnabled ()) return false;
	if ((accelerator & SWT.COMMAND) != 0) return false;
	int fVirt = OS.FVIRTKEY;
	int key = accelerator & SWT.KEY_MASK;
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
				key = Display.wcsToMbcs ((char) key);
				if (key == 0) return false;
				if (OS.IsWinCE) {
					key = (int)/*64*/OS.CharUpper ((short) key);
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
	if ((accelerator & SWT.SHIFT) != 0) accel.fVirt |= OS.FSHIFT;
	if ((accelerator & SWT.CONTROL) != 0) accel.fVirt |= OS.FCONTROL;
	return true;
}

void fixMenus (Decorations newParent) {
	if (menu != null) menu.fixMenus (newParent);
}

/**
 * Returns the widget accelerator.  An accelerator is the bit-wise
 * OR of zero or more modifier masks and a key. Examples:
 * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>.
 * The default value is zero, indicating that the menu item does
 * not have an accelerator.
 *
 * @return the accelerator or 0
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

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent (or its display if its parent is null).
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
	if (OS.IsWinCE) return new Rectangle (0, 0, 0, 0);
	int index = parent.indexOf (this);
	if (index == -1) return new Rectangle (0, 0, 0, 0);
	if ((parent.style & SWT.BAR) != 0) {
		Decorations shell = parent.parent;
		if (shell.menuBar != parent) {
			return new Rectangle (0, 0, 0, 0);
		}
		int /*long*/ hwndShell = shell.handle;
		MENUBARINFO info1 = new MENUBARINFO ();
		info1.cbSize = MENUBARINFO.sizeof;
		if (!OS.GetMenuBarInfo (hwndShell, OS.OBJID_MENU, 1, info1)) {
			return new Rectangle (0, 0, 0, 0);
		}
		MENUBARINFO info2 = new MENUBARINFO ();
		info2.cbSize = MENUBARINFO.sizeof;
		if (!OS.GetMenuBarInfo (hwndShell, OS.OBJID_MENU, index + 1, info2)) {
			return new Rectangle (0, 0, 0, 0);
		}
		int x = info2.left - info1.left;
		int y = info2.top - info1.top;
		int width = info2.right - info2.left;
		int height = info2.bottom - info2.top;
		return new Rectangle (x, y, width, height);
	} else {
		int /*long*/ hMenu = parent.handle;
		RECT rect1 = new RECT ();
		if (!OS.GetMenuItemRect (0, hMenu, 0, rect1)) {
			return new Rectangle (0, 0, 0, 0);
		}
		RECT rect2 = new RECT ();
		if (!OS.GetMenuItemRect (0, hMenu, index, rect2)) {
			return new Rectangle (0, 0, 0, 0);
		}
		int x = rect2.left - rect1.left + 2;
		int y = rect2.top - rect1.top + 2;
		int width = rect2.right - rect2.left;
		int height = rect2.bottom - rect2.top;
		return new Rectangle (x, y, width, height);
	}
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled menu item is typically
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
	if ((OS.IsPPC || OS.IsSP) && parent.hwndCB != 0) {
		int /*long*/ hwndCB = parent.hwndCB;
		TBBUTTONINFO info = new TBBUTTONINFO ();
		info.cbSize = TBBUTTONINFO.sizeof;
		info.dwMask = OS.TBIF_STATE;
		OS.SendMessage (hwndCB, OS.TB_GETBUTTONINFO, id, info);
		return (info.fsState & OS.TBSTATE_ENABLED) != 0;
	}
	/*
	* Feature in Windows.  For some reason, when the menu item
	* is a separator, GetMenuItemInfo() always indicates that
	* the item is not enabled.  The fix is to track the enabled
	* state for separators.
	*/
	if ((style & SWT.SEPARATOR) != 0) {
		return (state & DISABLED) == 0;
	}
	int /*long*/ hMenu = parent.handle;
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
 * Gets the identifier associated with the receiver.
 *
 * @return the receiver's identifier
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.7
 */
public int getID () {
	checkWidget();
	return userId;
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
	if ((OS.IsPPC || OS.IsSP) && parent.hwndCB != 0) return false;
	int /*long*/ hMenu = parent.handle;
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
 * otherwise. A disabled menu item is typically not selectable from the
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

void releaseChildren (boolean destroy) {
	if (menu != null) {
		menu.release (false);
		menu = null;
	}
	super.releaseChildren (destroy);
}

void releaseHandle () {
	super.releaseHandle ();
	parent = null;
	id = -1;
}

void releaseParent () {
	super.releaseParent ();
	if (menu != null) menu.dispose ();
	menu = null;
}

void releaseWidget () {
	super.releaseWidget ();
	if (hBitmap != 0) OS.DeleteObject (hBitmap);
	hBitmap = 0;
	if (accelerator != 0) {
		parent.destroyAccelerators ();
	}
	accelerator = 0;
	display.removeMenuItem (this);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the arm events are generated for the control.
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}


void reskinChildren (int flags) {
	if (menu != null) {
		menu.reskin (flags);
	}
	super.reskinChildren (flags);
}

void selectRadio () {
	int index = 0;
	MenuItem [] items = parent.getItems ();
	while (index < items.length && items [index] != this) index++;
	int i = index - 1;
	while (i >= 0 && items [i].setRadioSelection (false)) --i;
	int j = index + 1;
	while (j < items.length && items [j].setRadioSelection (false)) j++;
	setSelection (true);
}

/**
 * Sets the widget accelerator.  An accelerator is the bit-wise
 * OR of zero or more modifier masks and a key. Examples:
 * <code>SWT.MOD1 | SWT.MOD2 | 'T', SWT.MOD3 | SWT.F2</code>.
 * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>.
 * The default value is zero, indicating that the menu item does
 * not have an accelerator.
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
	if (this.accelerator == accelerator) return;
	this.accelerator = accelerator;
	parent.destroyAccelerators ();
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled menu item is typically
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
	if ((OS.IsPPC || OS.IsSP) && parent.hwndCB != 0) {
		int /*long*/ hwndCB = parent.hwndCB;
		TBBUTTONINFO info = new TBBUTTONINFO ();
		info.cbSize = TBBUTTONINFO.sizeof;
		info.dwMask = OS.TBIF_STATE;
		OS.SendMessage (hwndCB, OS.TB_GETBUTTONINFO, id, info);
		info.fsState &= ~OS.TBSTATE_ENABLED;
		if (enabled) info.fsState |= OS.TBSTATE_ENABLED;
		OS.SendMessage (hwndCB, OS.TB_SETBUTTONINFO, id, info);		
	} else {
		/*
		* Feature in Windows.  For some reason, when the menu item
		* is a separator, GetMenuItemInfo() always indicates that
		* the item is not enabled.  The fix is to track the enabled
		* state for separators.
		*/
		if ((style & SWT.SEPARATOR) != 0) {
			if (enabled) {
				state &= ~DISABLED;
			} else {
				state |= DISABLED;
			}
		}
		int /*long*/ hMenu = parent.handle;
		if (OS.IsWinCE) {
			int index = parent.indexOf (this);
			if (index == -1) return;
			int uEnable = OS.MF_BYPOSITION | (enabled ? OS.MF_ENABLED : OS.MF_GRAYED);
			OS.EnableMenuItem (hMenu, index, uEnable);
		} else {
			MENUITEMINFO info = new MENUITEMINFO ();
			info.cbSize = MENUITEMINFO.sizeof;
			info.fMask = OS.MIIM_STATE;
			boolean success = OS.GetMenuItemInfo (hMenu, id, false, info);
			if (!success) {
				int error = OS.GetLastError();
				SWT.error (SWT.ERROR_CANNOT_SET_ENABLED, null, " [GetLastError=0x" + Integer.toHexString(error) + "]");//$NON-NLS-1$ $NON-NLS-2$
			}
			int bits = OS.MFS_DISABLED | OS.MFS_GRAYED;
			if (enabled) {
				if ((info.fState & bits) == 0) return;
				info.fState &= ~bits;
			} else {
				if ((info.fState & bits) == bits) return;
				info.fState |= bits;
			}
			success = OS.SetMenuItemInfo (hMenu, id, false, info);
			if (!success) {
				/*
				* Bug in Windows.  For some reason SetMenuItemInfo(),
				* returns a fail code when setting the enabled or
				* selected state of a default item, but sets the
				* state anyway.  The fix is to ignore the error.
				* 
				* NOTE:  This only happens on Vista.
				*/
				if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
					success = id == OS.GetMenuDefaultItem (hMenu, OS.MF_BYCOMMAND, OS.GMDI_USEDISABLED);
				}
				if (!success) {
					int error = OS.GetLastError();
					SWT.error (SWT.ERROR_CANNOT_SET_ENABLED, null, " [GetLastError=0x" + Integer.toHexString(error) + "]");//$NON-NLS-1$ $NON-NLS-2$
				}
			}
		}
	}
	parent.destroyAccelerators ();
	parent.redraw ();
}

/**
 * Sets the identifier associated with the receiver to the argument.
 *
 * @param id the new identifier. This must be a non-negative value. System-defined identifiers are negative values.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - if called with an negative-valued argument.</li>
 * </ul>
 * 
 * @since 3.7
 */
public void setID (int id) {
	checkWidget();
	if (id < 0) error(SWT.ERROR_INVALID_ARGUMENT);
	userId = id;
}

/**
 * Sets the image the receiver will display to the argument.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept (for example, Windows NT).
 * Furthermore, some platforms (such as GTK), cannot display both
 * a check box and an image at the same time.  Instead, they hide
 * the image and display the check box.
 * </p>
 *
 * @param image the image to display
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
	if (OS.IsWinCE) {
		if ((OS.IsPPC || OS.IsSP) && parent.hwndCB != 0) {
			int /*long*/ hwndCB = parent.hwndCB;
			TBBUTTONINFO info = new TBBUTTONINFO ();
			info.cbSize = TBBUTTONINFO.sizeof;
			info.dwMask = OS.TBIF_IMAGE;
			info.iImage = parent.imageIndex (image);
			OS.SendMessage (hwndCB, OS.TB_SETBUTTONINFO, id, info);
		}
		return;
	}
	if (OS.WIN32_VERSION < OS.VERSION (4, 10)) return;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_BITMAP;
	if (parent.foreground != -1) {
		info.hbmpItem = OS.HBMMENU_CALLBACK;
	} else {
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0) && OS.IsAppThemed ()) {
			if (hBitmap != 0) OS.DeleteObject (hBitmap);
			info.hbmpItem = hBitmap = image != null ? Display.create32bitDIB (image) : 0;
		} else {
			info.hbmpItem = image != null ? OS.HBMMENU_CALLBACK : 0;
		}
	}
	int /*long*/ hMenu = parent.handle;
	OS.SetMenuItemInfo (hMenu, id, false, info);
	parent.redraw ();
}

/**
 * Sets the receiver's pull down menu to the argument.
 * Only <code>CASCADE</code> menu items can have a
 * pull down menu. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pull down
 * menu is platform specific.
 * <p>
 * Note: Disposing of a menu item that has a pull down menu
 * will dispose of the menu.  To avoid this behavior, set the
 * menu to null before the menu item is disposed.
 * </p>
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
	setMenu (menu, false);
}

void setMenu (Menu menu, boolean dispose) {

	/* Assign the new menu */
	Menu oldMenu = this.menu;
	if (oldMenu == menu) return;
	if (oldMenu != null) oldMenu.cascade = null;
	this.menu = menu;

	/* Assign the new menu in the OS */		
	if ((OS.IsPPC || OS.IsSP) && parent.hwndCB != 0) {
		if (OS.IsPPC) {
			int /*long*/ hwndCB = parent.hwndCB;
			int /*long*/ hMenu = menu == null ? 0 : menu.handle;
			OS.SendMessage (hwndCB, OS.SHCMBM_SETSUBMENU, id, hMenu);
		}
		if (OS.IsSP) error (SWT.ERROR_CANNOT_SET_MENU);
	} else {
		int /*long*/ hMenu = parent.handle;
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
		int /*long*/ hHeap = OS.GetProcessHeap ();
		int byteCount = cch * TCHAR.sizeof;
		int /*long*/ pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		info.fMask = OS.MIIM_STATE | OS.MIIM_ID | OS.MIIM_DATA;
		/*
		* Bug in Windows.  When GetMenuItemInfo() is used to get the text,
		* for an item that has a bitmap set using MIIM_BITMAP, the text is
		* not returned.  This means that when SetMenuItemInfo() is used to
		* set the submenu and the current menu state, the text is lost.
		* The fix is use MIIM_BITMAP and MIIM_STRING.
		*/
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (4, 10)) {
			info.fMask |= OS.MIIM_BITMAP | OS.MIIM_STRING;
		} else {
			info.fMask |= OS.MIIM_TYPE;
		}
		info.dwTypeData = pszText;
		info.cch = cch;
		boolean success = OS.GetMenuItemInfo (hMenu, index, true, info);
		if (menu != null) {
			menu.cascade = this; 
			info.fMask |= OS.MIIM_SUBMENU;
			info.hSubMenu = menu.handle;
		}
		if (OS.IsWinCE) {
			OS.RemoveMenu (hMenu, index, OS.MF_BYPOSITION);
			/*
			* On WinCE, InsertMenuItem() is not available.  The fix is to
			* use SetMenuItemInfo() but this call does not set the menu item
			* state and submenu.  The fix is to use InsertMenu() to insert
			* the item, SetMenuItemInfo() to set the string and EnableMenuItem()
			* and CheckMenuItem() to set the state.
			*/
			int /*long*/ uIDNewItem = id;
			int uFlags = OS.MF_BYPOSITION;
			if (menu != null) {
				uFlags |= OS.MF_POPUP;
				uIDNewItem = menu.handle;
			}
			TCHAR lpNewItem = new TCHAR (0, " ", true);
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
			}
		} else {
			if (dispose || oldMenu == null) {
				success = OS.SetMenuItemInfo (hMenu, index, true, info);
			} else {
				/*
				* Feature in Windows.  When SetMenuItemInfo () is used to
				* set a submenu and the menu item already has a submenu,
				* Windows destroys the previous menu.  This is undocumented
				* and unexpected but not necessarily wrong.  The fix is to
				* remove the item with RemoveMenu () which does not destroy
				* the submenu and then insert the item with InsertMenuItem ().
				*/
				OS.RemoveMenu (hMenu, index, OS.MF_BYPOSITION);
				success = OS.InsertMenuItem (hMenu, index, true, info);
			}
		}
		if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
		if (!success) {
			int error = OS.GetLastError();
			SWT.error (SWT.ERROR_CANNOT_SET_MENU, null, " [GetLastError=0x" + Integer.toHexString(error) + "]");//$NON-NLS-1$ $NON-NLS-2$
		}
	}
	parent.destroyAccelerators ();
}

boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		sendSelectionEvent (SWT.Selection);
	}
	return true;
}

void setOrientation (int orientation) {
    int /*long*/ hMenu = parent.handle;
    MENUITEMINFO info = new MENUITEMINFO ();
    info.cbSize = MENUITEMINFO.sizeof;
    info.fMask = OS.MIIM_FTYPE;
    info.fType = widgetStyle ();
    OS.SetMenuItemInfo (hMenu, id, false, info);
    if (menu != null) menu._setOrientation (orientation);
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
	if ((OS.IsPPC || OS.IsSP) && parent.hwndCB != 0) return;
	int /*long*/ hMenu = parent.handle;
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
		if (!success) {
			/*
			* Bug in Windows.  For some reason SetMenuItemInfo(),
			* returns a fail code when setting the enabled or
			* selected state of a default item, but sets the
			* state anyway.  The fix is to ignore the error.
			* 
			* NOTE:  This only happens on Vista.
			*/
			if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
				success = id == OS.GetMenuDefaultItem (hMenu, OS.MF_BYCOMMAND, OS.GMDI_USEDISABLED);
			}
			if (!success) {
				int error = OS.GetLastError();
				SWT.error (SWT.ERROR_CANNOT_SET_SELECTION, null, " [GetLastError=0x" + Integer.toHexString(error) + "]");//$NON-NLS-1$ $NON-NLS-2$
			}
		}
	}
	parent.redraw ();
}
/**
 * Sets the receiver's text. The string may include
 * the mnemonic character and accelerator text.
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p>
 * <p>
 * Accelerator text is indicated by the '\t' character.
 * On platforms that support accelerator text, the text
 * that follows the '\t' character is displayed to the user,
 * typically indicating the key stroke that will cause
 * the item to become selected.  On most platforms, the
 * accelerator text appears right aligned in the menu.
 * Setting the accelerator text does not install the
 * accelerator key sequence. The accelerator key sequence
 * is installed using #setAccelerator.
 * </p>
 * 
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setAccelerator
 */
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	if (text.equals (string)) return;
	super.setText (string);
	int /*long*/ hHeap = OS.GetProcessHeap ();
	int /*long*/ pszText = 0;
	boolean success = false;
	if ((OS.IsPPC || OS.IsSP) && parent.hwndCB != 0) {
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
		/* Use the character encoding for the default locale */
		TCHAR buffer = new TCHAR (0, string, true);
		int byteCount = buffer.length () * TCHAR.sizeof;
		pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (pszText, buffer, byteCount);	
		int /*long*/ hwndCB = parent.hwndCB;
		TBBUTTONINFO info2 = new TBBUTTONINFO ();
		info2.cbSize = TBBUTTONINFO.sizeof;
		info2.dwMask = OS.TBIF_TEXT;
		info2.pszText = pszText;
		success = OS.SendMessage (hwndCB, OS.TB_SETBUTTONINFO, id, info2) != 0;
	} else {
		MENUITEMINFO info = new MENUITEMINFO ();
		info.cbSize = MENUITEMINFO.sizeof;
		int /*long*/ hMenu = parent.handle;
		
		/* Use the character encoding for the default locale */
		TCHAR buffer = new TCHAR (0, string, true);
		int byteCount = buffer.length () * TCHAR.sizeof;
		pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (pszText, buffer, byteCount);	
		/*
		* Bug in Windows 2000.  For some reason, when MIIM_TYPE is set
		* on a menu item that also has MIIM_BITMAP, the MIIM_TYPE clears
		* the MIIM_BITMAP style.  The fix is to use MIIM_STRING.
		*/
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (4, 10)) {
			info.fMask = OS.MIIM_STRING;
		} else {
			info.fMask = OS.MIIM_TYPE;
			info.fType = widgetStyle ();
		}
		info.dwTypeData = pszText;
		success = OS.SetMenuItemInfo (hMenu, id, false, info);
	}
	if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	if (!success) {
		int error = OS.GetLastError();
		SWT.error (SWT.ERROR_CANNOT_SET_TEXT, null, " [GetLastError=0x" + Integer.toHexString(error) + "]");//$NON-NLS-1$ $NON-NLS-2$
	}
	parent.redraw ();
}

int widgetStyle () {
	int bits = 0;
	Decorations shell = parent.parent;
	if ((shell.style & SWT.MIRRORED) != 0) {
		if ((parent.style & SWT.LEFT_TO_RIGHT) != 0) {
			bits |= OS.MFT_RIGHTJUSTIFY | OS.MFT_RIGHTORDER;
		}
	} else {
		if ((parent.style & SWT.RIGHT_TO_LEFT) != 0) {
			bits |= OS.MFT_RIGHTJUSTIFY | OS.MFT_RIGHTORDER;
		}
	}
	if ((style & SWT.SEPARATOR) != 0) return bits | OS.MFT_SEPARATOR;
	if ((style & SWT.RADIO) != 0) return bits | OS.MFT_RADIOCHECK;
	return bits | OS.MFT_STRING;
}

LRESULT wmCommandChild (int /*long*/ wParam, int /*long*/ lParam) {
	if ((style & SWT.CHECK) != 0) {
		setSelection (!getSelection ());
	} else {
		if ((style & SWT.RADIO) != 0) {
			if ((parent.getStyle () & SWT.NO_RADIO_GROUP) != 0) {
				setSelection (!getSelection ());
			} else {
				selectRadio ();
			}
		}
	}
	sendSelectionEvent (SWT.Selection);
	return null;
}

LRESULT wmDrawChild (int /*long*/ wParam, int /*long*/ lParam) {
	DRAWITEMSTRUCT struct = new DRAWITEMSTRUCT ();
	OS.MoveMemory (struct, lParam, DRAWITEMSTRUCT.sizeof);
	if (image != null) {
		GCData data = new GCData();
		data.device = display;
		GC gc = GC.win32_new (struct.hDC, data);
		/*
		* Bug in Windows.  When a bitmap is included in the
		* menu bar, the HDC seems to already include the left
		* coordinate.  The fix is to ignore this value when
		* the item is in a menu bar.
		*/
		int x = (parent.style & SWT.BAR) != 0 ? MARGIN_WIDTH * 2 : struct.left;
		Image image = getEnabled () ? this.image : new Image (display, this.image, SWT.IMAGE_DISABLE);
		gc.drawImage (image, x, struct.top + MARGIN_HEIGHT);
		if (this.image != image) image.dispose ();
		gc.dispose ();
	}
	if (parent.foreground != -1) OS.SetTextColor (struct.hDC, parent.foreground);
	return null;
}

LRESULT wmMeasureChild (int /*long*/ wParam, int /*long*/ lParam) {
	MEASUREITEMSTRUCT struct = new MEASUREITEMSTRUCT ();
	OS.MoveMemory (struct, lParam, MEASUREITEMSTRUCT.sizeof);
	int width = 0, height = 0;
	if (image != null) {
		Rectangle rect = image.getBounds ();
		width = rect.width;
		height = rect.height;
	} else {
		/*
		* Bug in Windows.  If a menu contains items that have
		* images and can be checked, Windows does not include
		* the width of the image and the width of the check when
		* computing the width of the menu.  When the longest item
		* does not have an image, the label and the accelerator
		* text can overlap.  The fix is to use SetMenuItemInfo()
		* to indicate that all items have a bitmap and then include
		* the width of the widest bitmap in WM_MEASURECHILD.
		*/
		MENUINFO lpcmi = new MENUINFO ();
		lpcmi.cbSize = MENUINFO.sizeof;
		lpcmi.fMask = OS.MIM_STYLE;
		int /*long*/ hMenu = parent.handle;
		OS.GetMenuInfo (hMenu, lpcmi);
		if ((lpcmi.dwStyle & OS.MNS_CHECKORBMP) == 0) {
			MenuItem [] items = parent.getItems ();
			for (int i=0; i<items.length; i++) {
				MenuItem item = items [i];
				if (item.image != null) {
					Rectangle rect = item.image.getBounds ();
					width = Math.max (width, rect.width); 
				}
			}
		}
	}
	if (width != 0 || height != 0) {
		struct.itemWidth = width + MARGIN_WIDTH * 2;
		struct.itemHeight = height + MARGIN_HEIGHT * 2;
		OS.MoveMemory (lParam, struct, MEASUREITEMSTRUCT.sizeof);
	}
	return null;
}

}

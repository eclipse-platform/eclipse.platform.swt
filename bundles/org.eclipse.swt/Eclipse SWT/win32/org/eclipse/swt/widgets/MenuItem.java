/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
	long hBitmap;
	int id, accelerator, userId, index;
	ToolTip itemToolTip;
	/* Image margin. */
	final static int MARGIN_WIDTH = 1;
	final static int MARGIN_HEIGHT = 1;

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
	parent.createItem (this, (index = parent.getItemCount ()));
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
	parent.createItem (this, (this.index = index));
}

MenuItem (Menu parent, Menu menu, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	this.menu = menu;
	this.index = index;
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

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.CASCADE, 0);
}

@Override
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
				if (key == 0) return false;
				vKey = OS.VkKeyScan ((short) key);
				if (vKey == -1) {
					if (key != (int)OS.CharUpper (OS.LOWORD (key))) {
						fVirt = 0;
					}
				} else {
					key = vKey & 0xFF;
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
	if (menu != null && !menu.isDisposed() && !newParent.isDisposed()) menu.fixMenus (newParent);
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
	int index = parent.indexOf (this);
	if (index == -1) return new Rectangle (0, 0, 0, 0);
	if ((parent.style & SWT.BAR) != 0) {
		Decorations shell = parent.parent;
		if (shell.menuBar != parent) {
			return new Rectangle (0, 0, 0, 0);
		}
		long hwndShell = shell.handle;
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
		long hMenu = parent.handle;
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
	/*
	* Feature in Windows.  For some reason, when the menu item
	* is a separator, GetMenuItemInfo() always indicates that
	* the item is not enabled.  The fix is to track the enabled
	* state for separators.
	*/
	if ((style & SWT.SEPARATOR) != 0) {
		return (state & DISABLED) == 0;
	}
	long hMenu = parent.handle;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_STATE;
	boolean success = OS.GetMenuItemInfo (hMenu, id, false, info);
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
@Override
public Menu getMenu () {
	checkWidget ();
	return menu;
}

@Override
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
	long hMenu = parent.handle;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_STATE;
	boolean success = OS.GetMenuItemInfo (hMenu, id, false, info);
	if (!success) error (SWT.ERROR_CANNOT_GET_SELECTION);
	return (info.fState & OS.MFS_CHECKED) !=0;
}

/**
 * Returns the receiver's tool tip text, or null if it has not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.104
 */
public String getToolTipText () {
	checkWidget();
	return (itemToolTip == null || itemToolTip.isDisposed()) ? null : itemToolTip.getMessage();
}

void hideToolTip () {
	if (itemToolTip == null || itemToolTip.isDisposed()) return;
	itemToolTip.setVisible (false);
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

@Override
void releaseChildren (boolean destroy) {
	if (menu != null) {
		menu.release (false);
		menu = null;
	}
	super.releaseChildren (destroy);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	parent = null;
	id = -1;
}

@Override
void releaseParent () {
	super.releaseParent ();
	if (menu != null) menu.dispose ();
	menu = null;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (hBitmap != 0) OS.DeleteObject (hBitmap);
	hBitmap = 0;
	if (accelerator != 0) {
		parent.destroyAccelerators ();
	}
	accelerator = 0;
	if (itemToolTip!= null && !itemToolTip.isDisposed()) {
		itemToolTip.setVisible (false);
		itemToolTip.dispose();
		itemToolTip = null;
	}
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


@Override
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
	long hMenu = parent.handle;
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
		success = id == OS.GetMenuDefaultItem (hMenu, OS.MF_BYCOMMAND, OS.GMDI_USEDISABLED);
		if (!success) {
			int error = OS.GetLastError();
			SWT.error (SWT.ERROR_CANNOT_SET_ENABLED, null, " [GetLastError=0x" + Integer.toHexString(error) + "]");//$NON-NLS-1$ $NON-NLS-2$
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
 * Sets the receiver's image to the argument, which may be
 * null indicating that no image should be displayed.
 * <p>
 * Note: This operation is a <em>HINT</em> and is not supported on
 * platforms that do not have this concept (for example, Windows NT).
 * Some platforms (such as GTK3) support images alongside check boxes.
 * </p>
 *
 * @param image the image to display on the receiver (may be null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setImage (Image image) {
	checkWidget ();
	if (this.image == image) return;
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_BITMAP;
	if (parent.needsMenuCallback()) {
		info.hbmpItem = OS.HBMMENU_CALLBACK;
	} else {
		if (OS.IsAppThemed ()) {
			if (hBitmap != 0) OS.DeleteObject (hBitmap);
			info.hbmpItem = hBitmap = image != null ? Display.create32bitDIB (image) : 0;
		} else {
			info.hbmpItem = image != null ? OS.HBMMENU_CALLBACK : 0;
		}
	}
	long hMenu = parent.handle;
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

	long hMenu = parent.handle;
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
	long hHeap = OS.GetProcessHeap ();
	int byteCount = cch * 2;
	long pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	info.fMask = OS.MIIM_STATE | OS.MIIM_ID | OS.MIIM_DATA;
	/*
	* Bug in Windows.  When GetMenuItemInfo() is used to get the text,
	* for an item that has a bitmap set using MIIM_BITMAP, the text is
	* not returned.  This means that when SetMenuItemInfo() is used to
	* set the submenu and the current menu state, the text is lost.
	* The fix is use MIIM_BITMAP and MIIM_STRING.
	*/
	info.fMask |= OS.MIIM_BITMAP | OS.MIIM_STRING;
	info.dwTypeData = pszText;
	info.cch = cch;
	boolean success = OS.GetMenuItemInfo (hMenu, index, true, info);
	if (menu != null) {
		menu.cascade = this;
		info.fMask |= OS.MIIM_SUBMENU;
		info.hSubMenu = menu.handle;
	}
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
	if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	if (!success) {
		int error = OS.GetLastError();
		SWT.error (SWT.ERROR_CANNOT_SET_MENU, null, " [GetLastError=0x" + Integer.toHexString(error) + "]");//$NON-NLS-1$ $NON-NLS-2$
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
	long hMenu = parent.handle;
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
	long hMenu = parent.handle;
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
		success = id == OS.GetMenuDefaultItem (hMenu, OS.MF_BYCOMMAND, OS.GMDI_USEDISABLED);
		if (!success) {
			int error = OS.GetLastError();
			SWT.error (SWT.ERROR_CANNOT_SET_SELECTION, null, " [GetLastError=0x" + Integer.toHexString(error) + "]");//$NON-NLS-1$ $NON-NLS-2$
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
@Override
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	if (text.equals (string)) return;
	super.setText (string);
	long hHeap = OS.GetProcessHeap ();
	long pszText = 0;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	long hMenu = parent.handle;

	TCHAR buffer = new TCHAR (0, string, true);
	int byteCount = buffer.length () * TCHAR.sizeof;
	pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (pszText, buffer, byteCount);
	/*
	* Bug in Windows 2000.  For some reason, when MIIM_TYPE is set
	* on a menu item that also has MIIM_BITMAP, the MIIM_TYPE clears
	* the MIIM_BITMAP style.  The fix is to use MIIM_STRING.
	*/
	info.fMask = OS.MIIM_STRING;
	info.dwTypeData = pszText;
	boolean success = OS.SetMenuItemInfo (hMenu, id, false, info);
	if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	if (!success) {
		int error = OS.GetLastError();
		SWT.error (SWT.ERROR_CANNOT_SET_TEXT, null, " [GetLastError=0x" + Integer.toHexString(error) + "]");//$NON-NLS-1$ $NON-NLS-2$
	}
	parent.redraw ();
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the
 * control will be shown. For a menu item that has a default
 * tool tip, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be
 * escaped by doubling it in the string.
 * </p>
 * <p>
 * NOTE: Tooltips are currently not shown for top-level menu items in the
 * {@link Shell#setMenuBar(Menu) shell menubar} on Windows, Mac, and Ubuntu Unity desktop.
 * </p>
 * <p>
 * NOTE: This operation is a hint and behavior is platform specific, on Windows
 * for CJK-style mnemonics of the form " (&amp;C)" at the end of the tooltip text
 * are not shown in tooltip.
 * </p>
 * @param toolTip the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.104
 */
public void setToolTipText (String toolTip) {
	checkWidget ();

	if (toolTip == null && itemToolTip != null) {
		 if(!itemToolTip.isDisposed()) {
			 itemToolTip.setVisible (false);
			 itemToolTip.dispose();
		 }
		itemToolTip = null;
	}

	if (toolTip == null || toolTip.trim().length() == 0
			|| (itemToolTip != null && !itemToolTip.isDisposed() && toolTip.equals(itemToolTip.getMessage()))) return;

	if (itemToolTip != null) itemToolTip.dispose();
	itemToolTip = new MenuItemToolTip (this.getParent().getShell());
	itemToolTip.setMessage (toolTip);
	itemToolTip.setVisible (false);
}

void showTooltip (int x, int y) {
	if (itemToolTip == null || itemToolTip.isDisposed()) return;
	itemToolTip.setLocationInPixels (x, y);
	itemToolTip.setVisible (true);
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

LRESULT wmCommandChild (long wParam, long lParam) {
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

LRESULT wmDrawChild (long wParam, long lParam) {
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
		gc.drawImage (image, DPIUtil.autoScaleDown(x), DPIUtil.autoScaleDown(struct.top + MARGIN_HEIGHT));
		if (this.image != image) image.dispose ();
		gc.dispose ();
	}
	if (parent.foreground != -1) OS.SetTextColor (struct.hDC, parent.foreground);
	return null;
}

LRESULT wmMeasureChild (long wParam, long lParam) {
	MEASUREITEMSTRUCT struct = new MEASUREITEMSTRUCT ();
	OS.MoveMemory (struct, lParam, MEASUREITEMSTRUCT.sizeof);

	if ((parent.style & SWT.BAR) != 0) {
		if (parent.needsMenuCallback()) {
			/*
			 * Weirdness in Windows. Setting `HBMMENU_CALLBACK` causes
			 * item sizes to mean something else. It seems that it is
			 * the size of left margin before the text. At the same time,
			 * if menu item has a mnemonic, it's always drawn at a fixed
			 * position. I have tested on Win7, Win8.1, Win10 and found
			 * that value of 5 works well in matching text to mnemonic.
			 * NOTE: autoScaleUpUsingNativeDPI() is used to avoid problems
			 * with applications that disable automatic scaling.
			 */
			struct.itemWidth = DPIUtil.autoScaleUpUsingNativeDPI(5);
			OS.MoveMemory (lParam, struct, MEASUREITEMSTRUCT.sizeof);
			return null;
		}
	}

	int width = 0, height = 0;
	if (image != null) {
		Rectangle rect = image.getBoundsInPixels ();
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
		long hMenu = parent.handle;
		OS.GetMenuInfo (hMenu, lpcmi);
		if ((lpcmi.dwStyle & OS.MNS_CHECKORBMP) == 0) {
			for (MenuItem item : parent.getItems ()) {
				if (item.image != null) {
					Rectangle rect = item.image.getBoundsInPixels ();
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

private static final class MenuItemToolTip extends ToolTip {

	public MenuItemToolTip(Shell parent) {
		super(parent, 0);
		maybeEnableDarkSystemTheme(hwndToolTip ());
	}

	@Override
	long hwndToolTip() {
		return parent.menuItemToolTipHandle();
	}

}

}

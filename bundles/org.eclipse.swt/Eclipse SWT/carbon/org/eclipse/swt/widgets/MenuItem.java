package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;
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

/* AW
void fillAccel (ACCEL accel) {
	accel.fVirt = 0;
	accel.cmd = accel.key = 0;
	if (accelerator == 0) return;
	int key = accelerator & ~(SWT.ALT | SWT.CTRL | SWT.SHIFT);
	int newKey = Display.untranslateKey (key);
	if (newKey != 0) {
		key = newKey;
	} else {
		if (key == 0x7F) {
			key = OS.VK_DELETE;
		} else {
			short ch = (short) wcsToMbcs ((char) key);
			key = OS.CharUpper (ch);
		}
	}
	accel.key = (short) key;
	accel.cmd = (short) id;
	accel.fVirt = (byte) OS.FVIRTKEY;
	if ((accelerator & SWT.ALT) != 0) accel.fVirt |= OS.FALT;
	if ((accelerator & SWT.CTRL) != 0) accel.fVirt |= OS.FCONTROL;
	if ((accelerator & SWT.SHIFT) != 0) accel.fVirt |= OS.FSHIFT;
}
*/

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
 */
public boolean getEnabled () {
	checkWidget ();
	return OS.IsMenuCommandEnabled(parent.handle, id);
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
	
	char[] markchar= new char[1];
	if (OS.GetMenuCommandMark(parent.handle, id, markchar) == OS.kNoErr)
		return markchar[0] != 0;
		
	error (SWT.ERROR_CANNOT_GET_SELECTION);
	return false;
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
		
	int hMenu= parent.handle;
	short[] index= new short[1];
	OS.GetIndMenuItemWithCommandID(hMenu, id, 1, null, index);
	if (index[0] > 0)
		setAccelerator(hMenu, index[0], accelerator);
	else
		error (SWT.ERROR_CANNOT_SET_TEXT);
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
	if (enabled)
		OS.EnableMenuCommand(hMenu, id);
	else
		OS.DisableMenuCommand(hMenu, id);
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
	/* AW
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
	*/
	/*
	* This code is intentionally commented.
	*/
//	if (!success) error (SWT.ERROR_CANNOT_SET_TEXT);
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
	/* AW
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_ID;
	int index = 0;
	while (OS.GetMenuItemInfo (hMenu, index, true, info)) {
		if (info.wID == id) break;
		index++;
	}
	if (info.wID != id) return;
	int cch = 128;
	int hHeap = OS.GetProcessHeap ();
	int byteCount = cch * TCHAR.sizeof;
	int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	info.fMask = OS.MIIM_STATE | OS.MIIM_ID | OS.MIIM_TYPE;
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
		success = OS.InsertMenu (hMenu, index, OS.MF_BYPOSITION, id, null); 
		if (success) success = OS.SetMenuItemInfo (hMenu, index, true, info);
	} else {
		success = OS.InsertMenuItem (hMenu, index, true, info);
	}
	if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	if (!success) error (SWT.ERROR_CANNOT_SET_MENU);
	parent.destroyAcceleratorTable ();
	*/
	
	short[] index= new short[1];
	OS.GetIndMenuItemWithCommandID(hMenu, id, 1, null, index);
	if (index[0] >= 1) {
		OS.SetMenuItemHierarchicalMenu(hMenu, index[0], menu.handle);
		// we set the menu's title to the item's title
		int sHandle= 0;
		try {
			sHandle= OS.CFStringCreateWithCharacters(removeMnemonicsAndShortcut(getText()));
			OS.SetMenuTitleWithCFString(menu.handle, sHandle);
		} finally {
			if (sHandle != 0)
				OS.CFRelease(sHandle);
		}
	}
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
		
	char markChar= 0;
	if (selected)
		markChar= ((style & SWT.RADIO) != 0) ? OS.diamondMark : OS.checkMark;
		
	if (OS.SetMenuCommandMark(parent.handle, id, markChar) != OS.kNoErr)
		error (SWT.ERROR_CANNOT_SET_SELECTION);
}

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	
	super.setText (string);
	
	/*
	if (string != null && string.equals("Co&ntent Assist")) {
		setAccelerator(SWT.CONTROL + ' ');
	}
	*/
	//System.out.println("MenuItem.setText: " + string);
	
	int hMenu = parent.handle;
	
	short[] index= new short[1];
	OS.GetIndMenuItemWithCommandID(hMenu, id, 1, null, index);
	if (index[0] >= 1) {
		int sHandle= 0;
		try {
			sHandle= OS.CFStringCreateWithCharacters(removeMnemonicsAndShortcut(text));
			OS.SetMenuItemTextWithCFString(hMenu, index[0], sHandle);
		} finally {
			OS.CFRelease(sHandle);
		}
		setAccelerator(hMenu, index[0], accelerator);
	} else
		error (SWT.ERROR_CANNOT_SET_TEXT);
		
}

////////////////////////////////////////
// Mac Stuff
////////////////////////////////////////

void handleMenuSelect () {

	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		setSelection (!getSelection ());
	}
	// System.out.println("handleMenuSelect: " + getText() + "  " + getSelection());
	
	Event event = new Event ();
	/* AW
	if (OS.GetKeyState (OS.VK_MENU) < 0) event.stateMask |= SWT.ALT;
	if (OS.GetKeyState (OS.VK_SHIFT) < 0) event.stateMask |= SWT.SHIFT;
	if (OS.GetKeyState (OS.VK_CONTROL) < 0) event.stateMask |= SWT.CONTROL;
	if (OS.GetKeyState (OS.VK_LBUTTON) < 0) event.stateMask |= SWT.BUTTON1;
	if (OS.GetKeyState (OS.VK_MBUTTON) < 0) event.stateMask |= SWT.BUTTON2;
	if (OS.GetKeyState (OS.VK_RBUTTON) < 0) event.stateMask |= SWT.BUTTON3;
	*/
	postEvent (SWT.Selection, event);
}

static String removeMnemonicsAndShortcut(String s) {
	if (s.indexOf('&') >= 0 || s.indexOf('\t') >= 0) {
		StringBuffer sb= new StringBuffer();
		for (int i= 0; i < s.length(); i++) {
			char c= s.charAt(i);
			if (c == '\t')
				break;
			if (c != '&')
				sb.append(c);
		}
		s= sb.toString();
	}
	return s;
}

private static void setAccelerator(int menu, short index, int accelerator) {

	boolean controlIsCommand= true;

	//String t= getText();
	//System.out.println("setAccelerator: " + t + " " + Integer.toHexString(accelerator));
	//setText(removeMnemonicsAndShortcut(t) + "-0x" + Integer.toHexString(accelerator));
	
	if (accelerator == 0) {
		/* AW: hack for demo: we don't remove any accelerators
		OS.SetMenuItemCommandKey(menu, index, false, (char)0);
		OS.SetMenuItemKeyGlyph(menu, index, OS.kMenuNullGlyph);
		OS.SetMenuItemModifiers(menu, index, OS.kMenuNoModifiers);
		*/
		return;
	}

	/*
	if (t != null && t.equals("Co&ntent Assist")) {
		accelerator= SWT.CONTROL + ' ';
	}
	*/
	
	// use SetMenuItemData instead!
	
	int key= accelerator & ~(SWT.SHIFT | SWT.CONTROL | SWT.ALT);
	int macKey= 0;
	if (key != ' ')
		macKey= Display.untranslateKey(key);
	else {
		macKey= 49;
		controlIsCommand= false;
	}
	if (macKey == 0) {
		char c= new String(new char[] { (char)(key & 0xff) } ).toUpperCase().charAt(0);
		//System.out.println("  char: <" + c + ">");
		OS.SetMenuItemCommandKey(menu, index, false, c);
	} else {
		//System.out.println("  virtual key: " + macKey);
		OS.SetMenuItemCommandKey(menu, index, true, (char)macKey);
		OS.SetMenuItemKeyGlyph(menu, index, Display.keyGlyph(key));
	}
	
	byte modifiers= 0;
	if ((accelerator & SWT.SHIFT) != 0)
		modifiers |= OS.kMenuShiftModifier;
	if ((accelerator & SWT.CONTROL) != 0)
		modifiers |= OS.kMenuControlModifier;
	if ((accelerator & SWT.ALT) != 0)
		modifiers |= OS.kMenuOptionModifier;
		
	if (! controlIsCommand) {	// control is control
		modifiers |= OS.kMenuNoCommandModifier;
	} else {	// control is command
		if ((modifiers & OS.kMenuControlModifier) != 0)
			modifiers &= ~OS.kMenuControlModifier;
		else
			modifiers |= OS.kMenuNoCommandModifier;
	}
	
	if (modifiers != 0)
		OS.SetMenuItemModifiers(menu, index, modifiers);
	
	/*
	//OS.InvalidateMenuItems(menu, index, 1);
	Shell shell= getParent().getShell();
	if (shell != null) {
		Menu mb= shell.getMenuBar();
		if (mb != null)
			OS.SetRootMenu(mb.handle);
	}
	*/
}

}

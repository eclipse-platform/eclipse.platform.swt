package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import java.util.StringTokenizer;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.carbon.*;

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
	private int cIconHandle;

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
	if (OS.GetMenuCommandMark(parent.handle, id, markchar) == OS.noErr)
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
	if (cIconHandle != 0) {
		Image.disposeCIcon(cIconHandle);
		cIconHandle= 0;
    }
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
	
	if (MacUtil.USE_MENU_ICONS) {
		if (cIconHandle != 0)
			Image.disposeCIcon(cIconHandle);
		cIconHandle= Image.carbon_createCIcon(image);
		if (cIconHandle != 0) {
			int hMenu = parent.handle;
			short[] index= new short[1];
			OS.GetIndMenuItemWithCommandID(hMenu, id, 1, null, index);
			if (index[0] >= 1) {
				OS.SetMenuItemIconHandle(hMenu, index[0], (byte)4, cIconHandle);
			} else
				error (SWT.ERROR_CANNOT_SET_TEXT);
		}
	}
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

	if (parent == null) {
		System.out.println("MenuItem.setMenu: parent == null");
		return;
	}
	
	/* Assign the new menu in the OS */
	int hMenu = parent.handle;
	int newMenuHandle= 0;
	if (menu != null) {
		menu.cascade = this;
		newMenuHandle= menu.handle;
	}
	
	short[] index= new short[1];
	OS.GetIndMenuItemWithCommandID(hMenu, id, 1, null, index);
	if (index[0] >= 1) {
		if (OS.SetMenuItemHierarchicalMenu(hMenu, index[0], newMenuHandle) == OS.noErr) {
			if (menu != null) {
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
		} else
			error (SWT.ERROR_CANNOT_SET_MENU);
	}
	
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
		
	int markChar= 0;
	if (selected)
		markChar= ((style & SWT.RADIO) != 0) ? OS.diamondMark : OS.checkMark;
		
	if (OS.SetMenuCommandMark(parent.handle, id, (char)markChar) != OS.noErr)
		error (SWT.ERROR_CANNOT_SET_SELECTION);
}

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	
	super.setText (string);
		
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
		setAccelerator(hMenu, index[0], parseShortcut(string));
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
		int l= s.length();
		for (int i= 0; i < l; i++) {
			char c= s.charAt(i);
			if (c == '\t')
				break;
			if (c == '&') {
				if (i+2 < l && s.charAt(i+2) == ' ') {
					if (Character.isDigit(s.charAt(i+1)))
						i+= 2;
				}
			} else {
				sb.append(c);
			}
		}
		s= sb.toString();
	}
	return s;
}

private static void setAccelerator(int menu, short index, int accelerator) {

	if (accelerator == 0) {
		OS.SetMenuItemCommandKey(menu, index, false, (char)0);
		OS.SetMenuItemKeyGlyph(menu, index, (short)OS.kMenuNullGlyph);
		OS.SetMenuItemModifiers(menu, index, (byte)OS.kMenuNoModifiers);
		return;
	}
	
	int key= accelerator & ~(SWT.SHIFT | SWT.CONTROL | SWT.ALT);

	if (MacUtil.KEEP_MAC_SHORTCUTS) {
		if ((accelerator & SWT.CONTROL) != 0)
			if (key == 'H' || key == 'Q')
				return;
	}
	
	int macKey= 0;
	switch (key) {
	case ' ':
		macKey= 49;
		break;
	case SWT.CR:
		macKey= 36;
		break;
	default:
		macKey= Display.untranslateKey(key);
		break;
	}

	// AW todo: use SetMenuItemData instead?

	if (macKey == 0) {
		char c= new String(new char[] { (char)(key & 0xff) } ).toUpperCase().charAt(0);
		//System.out.println("  char: <" + c + ">");
		OS.SetMenuItemCommandKey(menu, index, false, c);
	} else {
		//System.out.println("  virtual key: " + macKey);
		char c= (char)macKey;
		OS.SetMenuItemCommandKey(menu, index, true, c);
		OS.SetMenuItemKeyGlyph(menu, index, keyGlyph(key));
	}
	
	byte modifiers= 0;
		
	if ((accelerator & SWT.SHIFT) != 0)
		modifiers |= OS.kMenuShiftModifier;

	if ((accelerator & SWT.CONTROL) != 0) {
		// we get the Command glyph for free so we don't have to do anything here
	} else {
		// but we have to remove it here...
		modifiers |= OS.kMenuNoCommandModifier;
	}
	
	if ((accelerator & SWT.ALT) != 0) {
		modifiers |= OS.kMenuOptionModifier;
		// force 'Alt' to have a 'Command' (by removing the NoCommand :-)
		modifiers &= ~OS.kMenuNoCommandModifier;
	}
	
	if (modifiers != 0)
		OS.SetMenuItemModifiers(menu, index, modifiers);
}

private static int parseShortcut(String text) {
	int accelerator= 0;
	
	int pos= text.indexOf('\t');
	if (pos >= 0) {
		text= text.substring(pos+1);
		
		/*
		 * This parsing code is wrong because it works only for the english version of eclipse
		 */
		StringTokenizer st= new StringTokenizer(text, "+");
		while (st.hasMoreTokens()) {
			String t= st.nextToken().toUpperCase();
			if ("SHIFT".equals(t))
				accelerator |= SWT.SHIFT;
			else if ("CTRL".equals(t))
				accelerator |= SWT.CONTROL;
			else if ("ALT".equals(t))
				accelerator |= SWT.ALT;
			else if ("F1".equals(t))
				accelerator |= SWT.F1;
			else if ("F2".equals(t))
				accelerator |= SWT.F2;
			else if ("F3".equals(t))
				accelerator |= SWT.F3;
			else if ("F4".equals(t))
				accelerator |= SWT.F4;
			else if ("F5".equals(t))
				accelerator |= SWT.F5;
			else if ("F6".equals(t))
				accelerator |= SWT.F6;
			else if ("F7".equals(t))
				accelerator |= SWT.F7;
			else if ("F8".equals(t))
				accelerator |= SWT.F8;
			else if ("F9".equals(t))
				accelerator |= SWT.F9;
			else if ("F10".equals(t))
				accelerator |= SWT.F10;
			else if ("F11".equals(t))
				accelerator |= SWT.F11;
			else if ("F12".equals(t))
				accelerator |= SWT.F12;
			else if ("DELETE".equals(t))
				accelerator |= SWT.DEL;
			else if ("ENTER".equals(t))
				accelerator |= SWT.CR;
			else if ("ARROW UP".equals(t) || "ARROW_UP".equals(t))
				accelerator |= SWT.ARROW_UP;
			else if ("ARROW DOWN".equals(t) || "ARROW_DOWN".equals(t))
				accelerator |= SWT.ARROW_DOWN;
			else if ("ARROW LEFT".equals(t) || "ARROW_LEFT".equals(t))
				accelerator |= SWT.ARROW_LEFT;
			else if ("ARROW RIGHT".equals(t) || "ARROW_RIGHT".equals(t))
				accelerator |= SWT.ARROW_RIGHT;
			else if ("SPACE".equals(t))
				accelerator |= ' ';
			else if ("TAB".equals(t))
				accelerator |= '\t';
			else {
				if (t.length() > 1)
					System.err.println("MenuItem.parseShortcut: unknown: <" + t + ">");
				else {
					accelerator |= t.charAt(0);
					break;	// must be last
				}
			}
		}
	}
	return accelerator;
}

private static short keyGlyph(int key) {
	switch (key) {
	case ' ':
		return OS.kMenuBlankGlyph;
	case '\t':
		return OS.kMenuTabRightGlyph;
	case SWT.SHIFT:
		return OS.kMenuShiftGlyph;
	case SWT.CONTROL:
		return OS.kMenuControlISOGlyph;
		//return OS.kMenuControlGlyph;
	case SWT.ALT:
		return OS.kMenuOptionGlyph;
	case SWT.ARROW_UP:
		//return OS.kMenuUpArrowDashedGlyph;
		return OS.kMenuUpArrowGlyph;
	case SWT.ARROW_DOWN:
		//return OS.kMenuDownwardArrowDashedGlyph;
		return OS.kMenuDownArrowGlyph;
	case SWT.ARROW_LEFT:
		//return OS.kMenuLeftArrowDashedGlyph;
		return OS.kMenuLeftArrowGlyph;
	case SWT.ARROW_RIGHT:
		//return OS.kMenuRightArrowDashedGlyph;
		return OS.kMenuRightArrowGlyph;
	case SWT.PAGE_UP:
		return OS.kMenuPageUpGlyph;
	case SWT.PAGE_DOWN:
		return OS.kMenuPageDownGlyph;
	case SWT.ESC:
		return OS.kMenuEscapeGlyph;
	case SWT.CR:
		//return OS.kMenuEnterGlyph;
		//return OS.kMenuNonmarkingReturnGlyph;
		//return OS.kMenuReturnR2LGlyph;
		//return OS.kMenuNonmarkingReturnGlyph;
		return OS.kMenuReturnGlyph;
	case SWT.BS:
		return OS.kMenuDeleteLeftGlyph;
	case SWT.DEL:
		return OS.kMenuDeleteRightGlyph;
	case SWT.F1:
		return OS.kMenuF1Glyph;
	case SWT.F2:
		return OS.kMenuF2Glyph;
	case SWT.F3:
		return OS.kMenuF3Glyph;
	case SWT.F4:
		return OS.kMenuF4Glyph;
	case SWT.F5:
		return OS.kMenuF5Glyph;
	case SWT.F6:
		return OS.kMenuF6Glyph;
	case SWT.F7:
		return OS.kMenuF7Glyph;
	case SWT.F8:
		return OS.kMenuF8Glyph;
	case SWT.F9:
		return OS.kMenuF9Glyph;
	case SWT.F10:
		return OS.kMenuF10Glyph;
	case SWT.F11:
		return OS.kMenuF11Glyph;
	case SWT.F12:
		return OS.kMenuF12Glyph;
	default:
		/*
		public static final short kMenuPencilGlyph = 15;
		public static final short kMenuCommandGlyph = 17;
		public static final short kMenuCheckmarkGlyph = 18;
		public static final short kMenuDiamondGlyph = 19;
		public static final short kMenuClearGlyph = 28;
		public static final short kMenuCapsLockGlyph = 99;
		public static final short kMenuHelpGlyph = 103;
		public static final short kMenuContextualMenuGlyph = 109;
		public static final short kMenuPowerGlyph = 110;
		*/
		return OS.kMenuNullGlyph;
	}
}

}

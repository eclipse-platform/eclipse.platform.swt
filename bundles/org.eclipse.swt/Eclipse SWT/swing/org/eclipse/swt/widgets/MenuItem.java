/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.swing.UIThreadUtils;

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
//	int id, accelerator;
  int accelerator;
  JComponent handle;

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
  createHandle();
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
  createHandle();
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
 * be notified when the menu item is selected, by sending
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

void destroyWidget () {
  parent.destroyItem (this);
  releaseHandle ();
}

//void fillAccel (ACCEL accel) {
//	accel.fVirt = 0;
//	accel.cmd = accel.key = 0;
//	if (accelerator == 0 || !getEnabled ()) return;
//	int fVirt = OS.FVIRTKEY;
//	int key = accelerator & SWT.KEY_MASK;
//	int vKey = Display.untranslateKey (key);
//	if (vKey != 0) {
//		key = vKey;	
//	} else {
//		switch (key) {
//			/*
//			* Bug in Windows.  For some reason, VkKeyScan
//			* fails to map ESC to VK_ESCAPE and DEL to
//			* VK_DELETE.  The fix is to map these keys
//			* as a special case.
//			*/
//			case 27: key = OS.VK_ESCAPE; break;
//			case 127: key = OS.VK_DELETE; break;
//			default: {
//				key = Display.wcsToMbcs ((char) key);
//				if (key == 0) return;
//				if (OS.IsWinCE) {
//					key = OS.CharUpper ((short) key);
//				} else {
//					vKey = OS.VkKeyScan ((short) key) & 0xFF;
//					if (vKey == -1) {
//						fVirt = 0;
//					} else {
//						key = vKey;
//					}
//				}
//			}
//		}
//	}
//	accel.key = (short) key;
//	accel.cmd = (short) id;
//	accel.fVirt = (byte) fVirt;
//	if ((accelerator & SWT.ALT) != 0) accel.fVirt |= OS.FALT;
//	if ((accelerator & SWT.SHIFT) != 0) accel.fVirt |= OS.FSHIFT;
//	if ((accelerator & SWT.CONTROL) != 0) accel.fVirt |= OS.FCONTROL;
//}

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
  java.awt.Rectangle bounds = handle.getBounds();
  return new Rectangle (bounds.x, bounds.y, bounds.width, bounds.height);
//	if (OS.IsWinCE) return new Rectangle (0, 0, 0, 0);
//	int index = parent.indexOf (this);
//	if (index == -1) return new Rectangle (0, 0, 0, 0);
//	if ((parent.style & SWT.BAR) != 0) {
//		Decorations shell = parent.parent;
//		if (shell.menuBar != parent) {
//			return new Rectangle (0, 0, 0, 0);
//		}
//		int hwndShell = shell.handle;
//		MENUBARINFO info1 = new MENUBARINFO ();
//		info1.cbSize = MENUBARINFO.sizeof;
//		if (!OS.GetMenuBarInfo (hwndShell, OS.OBJID_MENU, 1, info1)) {
//			return new Rectangle (0, 0, 0, 0);
//		}
//		MENUBARINFO info2 = new MENUBARINFO ();
//		info2.cbSize = MENUBARINFO.sizeof;
//		if (!OS.GetMenuBarInfo (hwndShell, OS.OBJID_MENU, index + 1, info2)) {
//			return new Rectangle (0, 0, 0, 0);
//		}
//		int x = info2.left - info1.left;
//		int y = info2.top - info1.top;
//		int width = info1.right - info1.left;
//		int height = info1.bottom - info1.top;
//		return new Rectangle (x, y, width, height);
//	} else {
//		int hMenu = parent.handle;
//		RECT rect1 = new RECT ();
//		if (!OS.GetMenuItemRect (0, hMenu, 0, rect1)) {
//			return new Rectangle (0, 0, 0, 0);
//		}
//		RECT rect2 = new RECT ();
//		if (!OS.GetMenuItemRect (0, hMenu, index, rect2)) {
//			return new Rectangle (0, 0, 0, 0);
//		}
//		int x = rect2.left - rect1.left + 2;
//		int y = rect2.top - rect1.top + 2;
//		int width = rect2.right - rect2.left;
//		int height = rect2.bottom - rect2.top;
//		return new Rectangle (x, y, width, height);
//	}
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
  return handle.isEnabled();
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
  return ((AbstractButton)handle).isSelected();
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
}

void releaseMenu () {
	setMenu (null);
	menu = null;
}

void releaseParent () {
  super.releaseParent ();
  if (menu != null) menu.dispose ();
  menu = null;
}

void releaseWidget () {
	super.releaseWidget ();
//	if (accelerator != 0) {
//		parent.destroyAccelerators ();
//	}
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
 * be notified when the control is selected.
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
  int key = accelerator & SWT.KEY_MASK;
  int vKey = Display.untranslateKey(key);
  if(vKey != 0) {
    key = vKey; 
  }
  if(handle instanceof JMenu) {
    ((JMenu)handle).setMnemonic(key);
  } else {
    int modifiers = 0;
    if((accelerator & SWT.ALT) != 0) {
      modifiers |= java.awt.event.KeyEvent.ALT_DOWN_MASK;
    } 
    if((accelerator & SWT.SHIFT) != 0) {
      modifiers |= java.awt.event.KeyEvent.SHIFT_DOWN_MASK;
    } 
    if((accelerator & SWT.CONTROL) != 0) {
      modifiers |= java.awt.event.KeyEvent.CTRL_DOWN_MASK;
    } 
    ((JMenuItem)handle).setAccelerator(KeyStroke.getKeyStroke(key, modifiers));
  }
//	parent.destroyAccelerators ();
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
  handle.setEnabled(enabled);
//	parent.destroyAccelerators ();
//	parent.redraw ();
}

/**
 * Sets the image the receiver will display to the argument.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept (for example, Windows NT).
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
  ImageIcon icon = null;
  if (image != null && image.handle != null) {
    icon = new ImageIcon (image.handle);
  }
  ((AbstractButton) handle).setIcon (icon);
//	parent.redraw ();
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
	if(!(handle instanceof JMenu)) {
    JMenu popup = new JMenu() {
      public void menuSelectionChanged(boolean isIncluded) {
        super.menuSelectionChanged(isIncluded);
        if(!isIncluded) return;
        if(!hooks(SWT.Arm)) return;
        UIThreadUtils.startExclusiveSection(getDisplay());
        if(isDisposed()) {
          UIThreadUtils.stopExclusiveSection();
          return;
        }
        try {
//          Event event = new Event();
//          event.stateMask = Display.getInputState();
//          sendEvent(SWT.Arm, event);
          sendEvent(SWT.Arm);
        } catch(Throwable t) {
          UIThreadUtils.storeException(t);
        } finally {
          UIThreadUtils.stopExclusiveSection();
        }
      }
    };
    popup.getPopupMenu().addPopupMenuListener(new PopupMenuListener() {
      public void popupMenuCanceled(PopupMenuEvent e) {
      }
      public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        if(MenuItem.this.menu == null) return;
        if(!MenuItem.this.menu.hooks(SWT.Hide)) return;
        UIThreadUtils.startExclusiveSection(getDisplay());
        if(isDisposed()) {
          UIThreadUtils.stopExclusiveSection();
          return;
        }
        try {
          Event event = new Event();
          event.widget = MenuItem.this.menu;
          MenuItem.this.menu.sendEvent(SWT.Hide, event);
        } catch(Throwable t) {
          UIThreadUtils.storeException(t);
        } finally {
          UIThreadUtils.stopExclusiveSection();
        }
      }
      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        if(MenuItem.this.menu == null) return;
        if(!MenuItem.this.menu.hooks(SWT.Show)) return;
        UIThreadUtils.startExclusiveSection(getDisplay());
        if(isDisposed()) {
          UIThreadUtils.stopExclusiveSection();
          return;
        }
        try {
          Event event = new Event();
          event.widget = MenuItem.this.menu;
          MenuItem.this.menu.sendEvent(SWT.Show, event);
        } catch(Throwable t) {
          UIThreadUtils.storeException(t);
        } finally {
          UIThreadUtils.stopExclusiveSection();
        }
      }
    });
    Container p = handle.getParent();
    if(p != null) {
      for(int i=p.getComponentCount()-1; i>=0; i--) {
        if(p.getComponent(i) == handle) {
          p.remove(handle);
          p.add(popup, i);
          if(p instanceof JComponent) {
            ((JComponent)p).revalidate();
          } else {
            p.invalidate();
            p.validate();
          }
          p.repaint();
          break;
        }
      }
    }
    JMenuItem menuItem = (JMenuItem)handle;
    popup.setText(menuItem.getText());
    popup.setMnemonic(menuItem.getMnemonic());
//    popup.setToolTipText(menuItem.getToolTipText());
    popup.setEnabled(menuItem.isEnabled());
    popup.setSelected(menuItem.isSelected());
    popup.setIcon(menuItem.getIcon());
    handle = popup;
	}
	JMenu menuHandle = (JMenu)handle;
	JPopupMenu popupMenu = menuHandle.getPopupMenu();
  popupMenu.removeAll();
  /* Assign the new menu */
	Menu oldMenu = this.menu;
	if (oldMenu == menu) return;
	if (oldMenu != null) oldMenu.cascade = null;
	this.menu = menu;
  if (menu != null) {
    menu.cascade = this; 
    Component[] components = ((JMenu)menu.handle).getPopupMenu().getComponents();
    for(int i=0; i<components.length; i++) {
      popupMenu.add(components[i]);
    }
    menu.handle = popupMenu;
  }
//	parent.destroyAccelerators ();
}

boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		sendEvent (SWT.Selection);
	}
	return true;
}

boolean adjustSelection;

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
  adjustSelection = true;
  ((AbstractButton)handle).setSelected(selected);
  adjustSelection = false;
//	parent.redraw ();
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
  AbstractButton button = (AbstractButton)handle;
  int index = findMnemonicIndex(string);
  if(index != -1) {
    button.setMnemonic(string.charAt(index));
    string = string.substring(0, index - 1) + string.substring(index);
  } else {
    button.setMnemonic('\0');
  }
//  string = fixMnemonic (string);
  // TODO: check what to do for cascade (CHECK, CASCADE, PUSH, RADIO and SEPARATOR)
  index = string.lastIndexOf('\t');
  if(index != -1) {
    string = string.substring(0, index);
  }
  button.setText(string);
  java.awt.Component comp = button.getParent();
  if(comp instanceof javax.swing.JPopupMenu) {
    ((javax.swing.JPopupMenu)comp).pack();
  }
//	parent.redraw ();
}

//int widgetStyle () {
//	int bits = 0;
//	Decorations shell = parent.parent;
//	if ((shell.style & SWT.MIRRORED) != 0) {
//		if ((parent.style & SWT.LEFT_TO_RIGHT) != 0) {
//			bits |= OS.MFT_RIGHTJUSTIFY | OS.MFT_RIGHTORDER;
//		}
//	} else {
//		if ((parent.style & SWT.RIGHT_TO_LEFT) != 0) {
//			bits |= OS.MFT_RIGHTJUSTIFY | OS.MFT_RIGHTORDER;
//		}
//	}
//	if ((style & SWT.SEPARATOR) != 0) return bits | OS.MFT_SEPARATOR;
//	if ((style & SWT.RADIO) != 0) return bits | OS.MFT_RADIOCHECK;
//	return bits | OS.MFT_STRING;
//}
//
//LRESULT wmCommandChild (int wParam, int lParam) {
//	if ((style & SWT.CHECK) != 0) {
//		setSelection (!getSelection ());
//	} else {
//		if ((style & SWT.RADIO) != 0) {
//			if ((parent.getStyle () & SWT.NO_RADIO_GROUP) != 0) {
//				setSelection (!getSelection ());
//			} else {
//				selectRadio ();
//			}
//		}
//	}
//	Event event = new Event ();
//	setInputState (event, SWT.Selection);
//	postEvent (SWT.Selection, event);
//	return null;
//}
//
//LRESULT wmDrawChild (int wParam, int lParam) {
//	DRAWITEMSTRUCT struct = new DRAWITEMSTRUCT ();
//	OS.MoveMemory (struct, lParam, DRAWITEMSTRUCT.sizeof);
//	if (image != null) {
//		GCData data = new GCData();
//		data.device = display;
//		GC gc = GC.win32_new (struct.hDC, data);
//		/*
//		* Bug in Windows.  When a bitmap is included in the
//		* menu bar, the HDC seems to already include the left
//		* coordinate.  The fix is to ignore this value when
//		* the item is in a menu bar.
//		*/
//		int x = (parent.style & SWT.BAR) != 0 ? (OS.IsWin95 ? 4 : 2) : struct.left;
//		gc.drawImage (image, x, struct.top + 2);
//		gc.dispose ();
//	}
//	return null;
//}
//
//LRESULT wmMeasureChild (int wParam, int lParam) {
//	MEASUREITEMSTRUCT struct = new MEASUREITEMSTRUCT ();
//	OS.MoveMemory (struct, lParam, MEASUREITEMSTRUCT.sizeof);
//	int width = 0, height = 0;
//	if (image != null) {
//		Rectangle rect = image.getBounds ();
//		width = rect.width;
//		height = rect.height;
//	} else {
//		/*
//		* Bug in Windows.  If a menu contains items that have
//		* images and can be checked, Windows does not include
//		* the width of the image and the width of the check when
//		* computing the width of the menu.  When the longest item
//		* does not have an image, the label and the accelerator
//		* text can overlap.  The fix is to use SetMenuItemInfo()
//		* to indicate that all items have a bitmap and then include
//		* the width of the widest bitmap in WM_MEASURECHILD.
//		*/
//		MENUINFO lpcmi = new MENUINFO ();
//		lpcmi.cbSize = MENUINFO.sizeof;
//		lpcmi.fMask = OS.MIM_STYLE;
//		int hMenu = parent.handle;
//		OS.GetMenuInfo (hMenu, lpcmi);
//		if ((lpcmi.dwStyle & OS.MNS_CHECKORBMP) == 0) {
//			MenuItem [] items = parent.getItems ();
//			for (int i=0; i<items.length; i++) {
//				MenuItem item = items [i];
//				if (item.image != null) {
//					Rectangle rect = item.image.getBounds ();
//					width = Math.max (width, rect.width); 
//				}
//			}
//		}
//	}
//	if (width != 0 || height != 0) {
//		/*
//		* Feature in Windows.  On Windows 98, it is necessary
//		* to add 4 pixels to the width of the image or the image
//		* and text are too close.  On other Windows platforms,
//		* this causes the text of the longest item to touch the
//		* accelerator text.  The fix is to add only 2 pixels in
//		* this case.
//		*/
//		struct.itemWidth = width + (OS.IsWin95 ? 4 : 2);
//		struct.itemHeight = height + 4;
//		OS.MoveMemory (lParam, struct, MEASUREITEMSTRUCT.sizeof);
//	}
//	return null;
//}

void createHandle() {
  if((style & SWT.SEPARATOR) != 0) {
    handle = new JSeparator();
  } else if((style & SWT.CASCADE) != 0 || (style & SWT.PUSH) != 0) {
    JMenuItem menuItem = new JMenuItem() {
      public void menuSelectionChanged(boolean isIncluded) {
        super.menuSelectionChanged(isIncluded);
        if(!isIncluded) {
          if(parent == null || parent.cascade == null) return;
          if(!parent.cascade.hooks(SWT.Arm)) return;
          UIThreadUtils.startExclusiveSection(getDisplay());
          if(isDisposed()) {
            UIThreadUtils.stopExclusiveSection();
            return;
          }
//          Event event = new Event();
//          event.stateMask = Display.getInputState();
//          sendEvent(SWT.Arm, event);
          parent.cascade.sendEvent(SWT.Arm);
          UIThreadUtils.stopExclusiveSection();
          return;
        }
        if(!hooks(SWT.Arm)) return;
        UIThreadUtils.startExclusiveSection(getDisplay());
        if(isDisposed()) {
          UIThreadUtils.stopExclusiveSection();
          return;
        }
//        Event event = new Event();
//        event.stateMask = Display.getInputState();
//        sendEvent(SWT.Arm, event);
        sendEvent(SWT.Arm);
        UIThreadUtils.stopExclusiveSection();
      }
    };
    handle = menuItem;
//    menuItem.addChangeListener(new ChangeListener() {
//      boolean isSelected;
//      public void stateChanged(ChangeEvent e) {
//        isSelected = !isSelected;
//        if(isSelected) return;
//        if(!hooks(SWT.Arm)) return;
//        UIThreadUtil.startExclusiveSection(getDisplay());
//        if(isDisposed()) {
//          UIThreadUtil.stopExclusiveSection();
//          return;
//        }
////        Event event = new Event();
////        event.stateMask = Display.getInputState();
////        sendEvent(SWT.Arm, event);
//        sendEvent(SWT.Arm);
//        UIThreadUtil.stopExclusiveSection();
//      }
//    });
    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(!hooks(SWT.Selection)) return;
        UIThreadUtils.startExclusiveSection(getDisplay());
        if(isDisposed()) {
          UIThreadUtils.stopExclusiveSection();
          return;
        }
        try {
          Event event = new Event();
          event.stateMask = Display.getInputState();
          sendEvent(SWT.Selection, event);
        } catch(Throwable t) {
          UIThreadUtils.storeException(t);
        } finally {
          UIThreadUtils.stopExclusiveSection();
        }
      }
    });
  } else if((style & SWT.CHECK) != 0) {
    JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem();
    handle = menuItem;
    menuItem.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if(adjustSelection || !hooks(SWT.Selection)) return;
        UIThreadUtils.startExclusiveSection(getDisplay());
        if(isDisposed()) {
          UIThreadUtils.stopExclusiveSection();
          return;
        }
        try {
//          Event event = new Event();
//          event.stateMask = Display.getInputState();
//          sendEvent(SWT.Selection, event);
          sendEvent(SWT.Selection);
        } catch(Throwable t) {
          UIThreadUtils.storeException(t);
        } finally {
          UIThreadUtils.stopExclusiveSection();
        }
      }
    });
  } else if((style & SWT.RADIO) != 0) {
    JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem() {
      protected void fireActionPerformed(ActionEvent e) {
        if(!isSelected()) {
          setSelected(true);
        }
        Component[] components = getParent().getComponents();
        for(int i=0; i<components.length; i++) {
          Component component = components[i];
          if(component instanceof JRadioButton && component != this) {
            ((JRadioButton)component).setSelected(false);
          }
        }
        super.fireActionPerformed(e);
        if(adjustSelection || !hooks(SWT.Selection)) return;
        UIThreadUtils.startExclusiveSection(getDisplay());
        if(isDisposed()) {
          UIThreadUtils.stopExclusiveSection();
          return;
        }
        Event event = new Event();
        event.stateMask = Display.getInputState();
        sendEvent(SWT.Selection, event);
        UIThreadUtils.stopExclusiveSection();
      }
    };
    handle = menuItem;
    menuItem.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {
          selectRadio();
        }
        if(!hooks(SWT.Arm)) return;
        UIThreadUtils.startExclusiveSection(getDisplay());
        if(isDisposed()) {
          UIThreadUtils.stopExclusiveSection();
          return;
        }
        try {
//          Event event = new Event();
//          event.stateMask = Display.getInputState();
//          sendEvent(SWT.Arm, event);
          sendEvent(SWT.Arm);
        } catch(Throwable t) {
          UIThreadUtils.storeException(t);
        } finally {
          UIThreadUtils.stopExclusiveSection();
        }
      }
    });
  }
}

}

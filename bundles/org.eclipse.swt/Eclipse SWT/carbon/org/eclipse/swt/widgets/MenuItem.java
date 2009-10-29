/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


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
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class MenuItem extends Item {
	Menu parent, menu;
	int accelerator;
	boolean acceleratorSet;
//	int x, y, width, height;

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

int createEmptyMenu () {
	/*
	* Bug in the Macintosh.  When a menu bar item does not have
	* an associated pull down menu, the Macintosh segment faults.
	* The fix is to temporarily attach an empty menu. 
	*/
	if ((parent.style & SWT.BAR) != 0) {
		int outMenuRef [] = new int [1];
		if (OS.CreateNewMenu ((short) 0, 0, outMenuRef) != OS.noErr) {
			error (SWT.ERROR_NO_HANDLES);
		}
		return outMenuRef [0];
	}
	return 0;
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
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

/*public*/ Rectangle getBounds () {
	checkWidget ();
	if ((parent.style & SWT.BAR) != 0) {
		int index = parent.indexOf (this);
		if (index == -1) return new Rectangle (0 ,0, 0, 0);
		Menu menu = display.getMenuBar ();
		if (parent != menu) return new Rectangle (0 ,0, 0, 0);
		int outMenuRef [] = new int [1];
		if (OS.GetMenuItemHierarchicalMenu (menu.handle, (short)(index + 1), outMenuRef) != OS.noErr) {
			return new Rectangle (0 ,0, 0, 0);
		}
		Rect rect = new Rect ();
		//TODO - get the bounds of the menu item from the menu title
//		if (... code needed to do this ... != OS.noErr) {
//			return new Rectangle (0 ,0, 0, 0);
//		}
		int width = rect.right - rect.left;
		int height = rect.bottom - rect.top;
		return new Rectangle (rect.left, rect.top, width, height);
	}
	return new Rectangle (0 ,0, 0, 0);
//	return new Rectangle (x, y, width, height);

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
	checkWidget();
	return (state & DISABLED) == 0;
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
	int index = parent.indexOf (this);
	if (index == -1) return false;
	short [] outMark = new short [1];
	OS.GetItemMark (parent.handle, (short) (index + 1), outMark);
	return outMark [0] != 0;
}

int kEventProcessCommand (int nextHandler, int theEvent, int userData) {
	//TEMPORARY CODE
	if (!isEnabled ()) return OS.noErr;

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
	return OS.noErr;
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

int keyGlyph (int key) {
	switch (key) {
		case SWT.BS: return OS.kMenuDeleteLeftGlyph;
		case SWT.CR: return OS.kMenuReturnGlyph;
		case SWT.DEL: return OS.kMenuDeleteRightGlyph;
		case SWT.ESC: return OS.kMenuEscapeGlyph;
		case SWT.LF: return OS.kMenuReturnGlyph;
		case SWT.TAB: return OS.kMenuTabRightGlyph;
		case ' ': return OS.kMenuBlankGlyph;
//		case ' ': return OS.kMenuSpaceGlyph;
		case SWT.ALT: return OS.kMenuOptionGlyph;
		case SWT.SHIFT: return OS.kMenuShiftGlyph;
		case SWT.CONTROL: return OS.kMenuControlISOGlyph;
		case SWT.COMMAND: return OS.kMenuCommandGlyph;
		case SWT.ARROW_UP: return OS.kMenuUpArrowGlyph;
		case SWT.ARROW_DOWN: return OS.kMenuDownArrowGlyph;
		case SWT.ARROW_LEFT: return OS.kMenuLeftArrowGlyph;
		case SWT.ARROW_RIGHT: return OS.kMenuRightArrowGlyph;
		case SWT.PAGE_UP: return OS.kMenuPageUpGlyph;
		case SWT.PAGE_DOWN: return OS.kMenuPageDownGlyph;
		case SWT.KEYPAD_CR: return OS.kMenuEnterGlyph;
		case SWT.HELP: return OS.kMenuHelpGlyph;
//		case SWT.CAPS_LOCK: return OS.kMenuCapsLockGlyph;
		case SWT.F1: return OS.kMenuF1Glyph;
		case SWT.F2: return OS.kMenuF2Glyph;
		case SWT.F3: return OS.kMenuF3Glyph;
		case SWT.F4: return OS.kMenuF4Glyph;
		case SWT.F5: return OS.kMenuF5Glyph;
		case SWT.F6: return OS.kMenuF6Glyph;
		case SWT.F7: return OS.kMenuF7Glyph;
		case SWT.F8: return OS.kMenuF8Glyph;
		case SWT.F9: return OS.kMenuF9Glyph;
		case SWT.F10: return OS.kMenuF10Glyph;
		case SWT.F11: return OS.kMenuF11Glyph;
		case SWT.F12: return OS.kMenuF12Glyph;
		case SWT.F13: return OS.kMenuF13Glyph;
		case SWT.F14: return OS.kMenuF14Glyph;
		case SWT.F15: return OS.kMenuF15Glyph;
		case SWT.HOME: return OS.kMenuNorthwestArrowGlyph;
		case SWT.END: return OS.kMenuSoutheastArrowGlyph;	
		/*
		* The following lines are intentionally commented.
		* The Mac does not (currently) have glyphs for these keys.
		*/
//		case SWT.INSERT: return OS.kMenuNullGlyph;
	}
	return OS.kMenuNullGlyph;
}

void releaseHandle () {
	super.releaseHandle ();
	parent = null;
}

void releaseChildren (boolean destroy) {
	if (menu != null) {
		menu.release (false);
		menu = null;
	}
	super.releaseChildren (destroy);
}

void releaseWidget () {
	super.releaseWidget ();
	accelerator = 0;
	if (this == parent.defaultItem) parent.defaultItem = null;
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

/*public*/ void select () {
	checkWidget ();
	Menu menu = parent, menuParent;
	while (menu.cascade != null && (menuParent = menu.cascade.parent) != null) {
		if ((menuParent.style & SWT.BAR) != 0) break;
		menu = menuParent;
	}
	if (menu == null) return;
	OS.HiliteMenu (OS.GetMenuID (menu.handle));
	int[] event = new int[1];
	OS.CreateEvent (0, OS.kEventClassCommand, OS.kEventProcessCommand, 0.0, 0, event);
	if (event [0] != 0) {
		int parentHandle = parent.handle;
		HICommand command = new HICommand ();
		command.attributes = OS.kHICommandFromMenu;
		command.menu_menuRef = parentHandle;
		command.menu_menuItemIndex = (short) (parent.indexOf (this) + 1);
		OS.SetEventParameter (event [0], OS.kEventParamDirectObject, OS.typeHICommand, HICommand.sizeof, command);
		OS.SendEventToEventTarget (event [0], OS.GetApplicationEventTarget ());
		OS.ReleaseEvent (event [0]);
	}
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
	int index = parent.indexOf (this);
	if (index == -1) return;
	if (this.accelerator == accelerator) return;
	boolean update = (this.accelerator == 0 && accelerator != 0) || (this.accelerator != 0 && accelerator == 0);
	this.accelerator = accelerator;
	boolean inSetVirtualKey = false;
	int inModifiers = OS.kMenuNoModifiers, inGlyph = OS.kMenuNullGlyph, inKey = 0;
	if (accelerator != 0) {
		inKey = accelerator & SWT.KEY_MASK;
		inGlyph = keyGlyph (inKey);
		int virtualKey = Display.untranslateKey (inKey);
		if (inKey == ' ') virtualKey = 49;
		if (virtualKey != 0) {
			inSetVirtualKey = true;
			inKey = virtualKey;
		} else {
			inKey = Character.toUpperCase ((char)inKey);
		}
		inModifiers = (byte) OS.kMenuNoCommandModifier;
		if ((accelerator & SWT.SHIFT) != 0) inModifiers |= OS.kMenuShiftModifier;
		if ((accelerator & SWT.CONTROL) != 0) inModifiers |= OS.kMenuControlModifier;
		if ((accelerator & SWT.COMMAND) != 0) inModifiers &= ~OS.kMenuNoCommandModifier;
		if ((accelerator & SWT.ALT) != 0) inModifiers |= OS.kMenuOptionModifier;
	}
	short menuIndex = (short) (index + 1);
	OS.SetMenuItemModifiers (parent.handle, menuIndex, (byte)inModifiers);
	OS.SetMenuItemCommandKey (parent.handle, menuIndex, inSetVirtualKey, (char)inKey);
	OS.SetMenuItemKeyGlyph (parent.handle, menuIndex, (short)inGlyph);
	if (update) updateText (menuIndex);
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
	int index = parent.indexOf (this);
	if (index == -1) return;
	int outMenuRef [] = new int [1];
	short menuIndex = (short) (index + 1);
	OS.GetMenuItemHierarchicalMenu (parent.handle, menuIndex, outMenuRef);
	if (enabled) {
		state &= ~DISABLED;
		if (outMenuRef [0] != 0) OS.EnableMenuItem (outMenuRef [0], (short) 0);
		OS.EnableMenuItem (parent.handle, menuIndex);
	} else {
		state |= DISABLED;
		if (outMenuRef [0] != 0) OS.DisableMenuItem (outMenuRef [0], (short) 0);
		OS.DisableMenuItem (parent.handle, menuIndex);
	}
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
	int index = parent.indexOf (this);
	if (index == -1) return;
	super.setImage (image);
	int imageHandle = image != null ? image.handle : 0;
	byte type = image != null ? (byte)OS.kMenuCGImageRefType : (byte)OS.kMenuNoIcon;
	OS.SetMenuItemIconHandle (parent.handle, (short) (index + 1), type, imageHandle);
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
	
	/* Assign the new menu */
	Menu oldMenu = this.menu;
	if (oldMenu == menu) return;
	if (oldMenu != null) oldMenu.cascade = null;
	this.menu = menu;
	
	/* Update the menu in the OS */
	int index = parent.indexOf (this);
	if (index == -1) return;
	int menuHandle = 0;
	if (menu == null) {
		menuHandle = createEmptyMenu ();
	} else {
		menu.cascade = this;
		menuHandle = menu.handle;
	}
	short menuIndex = (short) (index + 1);
	if (menuHandle != 0) {
		int [] outString = new int [1];
		if (OS.CopyMenuItemTextAsCFString (parent.handle, menuIndex, outString) != OS.noErr) {
			error (SWT.ERROR_CANNOT_SET_MENU);
		}
		OS.SetMenuTitleWithCFString (menuHandle, outString [0]);
		OS.CFRelease (outString [0]);
	}
	if (oldMenu != null) OS.RetainMenu (oldMenu.handle);
	if (OS.SetMenuItemHierarchicalMenu (parent.handle, menuIndex, menuHandle) != OS.noErr) {
		error (SWT.ERROR_CANNOT_SET_MENU);
	}
	if (menuHandle != 0) OS.ReleaseMenu (menuHandle);
}

boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		sendSelectionEvent (SWT.Selection);
	}
	return true;
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
	int index = parent.indexOf (this);
	if (index == -1) return;
	int inMark = selected ? ((style & SWT.RADIO) != 0) ? OS.diamondMark : OS.checkMark : 0;
	OS.SetItemMark (parent.handle, (short) (index + 1), (short) inMark);
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
	int index = parent.indexOf (this);
	if (index == -1) return;
	super.setText (string);
	updateText ((short) (index + 1));
}

void updateText (short menuIndex) {
	if ((style & SWT.SEPARATOR) != 0) return;
	char [] buffer = new char [text.length ()];
	text.getChars (0, buffer.length, buffer, 0);
	int i=0, j=0;
	while (i < buffer.length) {
		if (buffer [i] == '\t') break;
		if ((buffer [j++] = buffer [i++]) == '&') {
			if (i == buffer.length) {continue;}
			if (buffer [i] == '&') {i++; continue;}
			j--;
		}
	}
	int str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, j);
	if (str == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetMenuItemTextWithCFString (parent.handle, menuIndex, str);
	int [] outHierMenu = new int [1];
	OS.GetMenuItemHierarchicalMenu (parent.handle, menuIndex, outHierMenu);
	if (outHierMenu [0] != 0) OS.SetMenuTitleWithCFString (outHierMenu [0], str);
	/*
	* Feature in the Macintosh.  Setting text that starts with "-" makes the
	* menu item a separator.  The fix is to clear the separator attribute. 
	*/
	if (text.startsWith ("-")) {
		OS.ChangeMenuItemAttributes (parent.handle, menuIndex, 0, OS.kMenuItemAttrSeparator);
	}
	OS.CFRelease (str);
	
	acceleratorSet = accelerator != 0;
	if (!acceleratorSet) {
		int inModifiers = OS.kMenuNoCommandModifier, inGlyph = OS.kMenuNullGlyph, inKey = 0, swtKey = 0;
		boolean inSetVirtualKey = false;
		if (i + 1 < buffer.length && buffer [i] == '\t') {
			for (j = i + 1; j < buffer.length; j++) {
				switch (buffer [j]) {
					case '\u2303': inModifiers |= OS.kMenuControlModifier; i++; break;
					case '\u2325': inModifiers |= OS.kMenuOptionModifier; i++; break;
					case '\u21E7': inModifiers |= OS.kMenuShiftModifier; i++; break;
					case '\u2318': inModifiers &= ~OS.kMenuNoCommandModifier; i++; break;
					default:
						j = buffer.length;
						break;
				}
			}
			switch (buffer.length - i - 1) {
				case 1:
					inKey = buffer [i + 1];
					switch (inKey) {
						case '\u232B': swtKey = SWT.BS; break;
						case '\u21A9': swtKey = SWT.CR; break;
						case '\u2326': swtKey = SWT.DEL; break;
						case '\u238B': swtKey = SWT.ESC; break;
						case '\u21E5': swtKey = SWT.TAB; break;
						case ' ': swtKey = ' '; break;
						case '\u2423': swtKey = ' '; break;
						case '\u2191': swtKey = SWT.ARROW_UP; break;
						case '\u2193': swtKey = SWT.ARROW_DOWN; break;
						case '\u2190': swtKey = SWT.ARROW_LEFT; break;
						case '\u2192': swtKey = SWT.ARROW_RIGHT; break;
						case '\u21DE': swtKey = SWT.PAGE_UP; break;
						case '\u21DF': swtKey = SWT.PAGE_DOWN; break;
						case '\u0003': swtKey = SWT.KEYPAD_CR; break;
						case '\uF746': swtKey = SWT.HELP; break;
						case '\uF729': swtKey = SWT.HOME; break;
						case '\uF72B': swtKey = SWT.END; break;
//						case '\u21EA': swtKey = SWT.CAPS_LOCK; break;
						case '\uF704': swtKey = SWT.F1; break;
						case '\uF705': swtKey = SWT.F2; break;
						case '\uF706': swtKey = SWT.F3; break;
						case '\uF707': swtKey = SWT.F4; break;
						case '\uF708': swtKey = SWT.F5; break;
						case '\uF709': swtKey = SWT.F6; break;
						case '\uF70A': swtKey = SWT.F7; break;
						case '\uF70B': swtKey = SWT.F8; break;
						case '\uF70C': swtKey = SWT.F9; break;
						case '\uF70D': swtKey = SWT.F10; break;
						case '\uF70E': swtKey = SWT.F11; break;
						case '\uF70F': swtKey = SWT.F12; break;
						case '\uF710': swtKey = SWT.F13; break;
						case '\uF711': swtKey = SWT.F14; break;
						case '\uF712': swtKey = SWT.F15; break;
					}
					break;
				case 2:
					if (buffer [i + 1] == 'F') {
						switch (buffer [i + 2]) {
							case '1': swtKey = SWT.F1; break;
							case '2': swtKey = SWT.F2; break;
							case '3': swtKey = SWT.F3; break;
							case '4': swtKey = SWT.F4; break;
							case '5': swtKey = SWT.F5; break;
							case '6': swtKey = SWT.F6; break;
							case '7': swtKey = SWT.F7; break;
							case '8': swtKey = SWT.F8; break;
							case '9': swtKey = SWT.F9; break;
						}
					}
					break;
				case 3:
					if (buffer [i + 1] == 'F' && buffer [i + 2] == '1') {
						switch (buffer [i + 3]) {
							case '0': swtKey = SWT.F10; break;
							case '1': swtKey = SWT.F11; break;
							case '2': swtKey = SWT.F12; break;
							case '3': swtKey = SWT.F13; break;
							case '4': swtKey = SWT.F14; break;
							case '5': swtKey = SWT.F15; break;
						}
					}
					break;
			}

			inGlyph = keyGlyph (swtKey);
			int virtualKey = Display.untranslateKey (swtKey);
			if (swtKey == ' ') {
				virtualKey = 49;
				inGlyph = OS.kMenuSpaceGlyph;
			}
			if (virtualKey != 0) {
				inSetVirtualKey = true;
				inKey = virtualKey;
			} else {
				inKey = Character.toUpperCase ((char)inKey);
			}
		}
		acceleratorSet = inKey != 0 || inGlyph != OS.kMenuNullGlyph;
		OS.SetMenuItemModifiers (parent.handle, menuIndex, (byte)inModifiers);
		OS.SetMenuItemCommandKey (parent.handle, menuIndex, inSetVirtualKey, (char)inKey);
		OS.SetMenuItemKeyGlyph (parent.handle, menuIndex, (short)inGlyph);
	}
}
}

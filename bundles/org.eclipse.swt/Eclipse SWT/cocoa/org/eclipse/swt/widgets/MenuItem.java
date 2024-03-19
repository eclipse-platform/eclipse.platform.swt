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
	NSMenuItem nsItem;
	Menu parent, menu;
	int accelerator;
	long nsItemAction;
	id nsItemTarget;
	String toolTipText;

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

MenuItem (Menu parent, NSMenuItem nsMenuItem) {
	super(parent, 0);
	this.parent = parent;
	this.nsItem = nsMenuItem;
	parent.createItem(this, parent.getItemCount ());
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
	addTypedListener(listener, SWT.Arm);
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
	addTypedListener(listener, SWT.Help);
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
	addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.CASCADE, 0);
}

NSMenu createEmptyMenu () {
	if ((parent.style & SWT.BAR) != 0) {
		return (NSMenu) new SWTMenu ().alloc ().init ();
	}
	return null;
}

@Override
void deregister () {
	super.deregister ();
	display.removeWidget (nsItem);
}

@Override
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
	return (int)nsItem.tag();
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
	return nsItem.state() == OS.NSOnState;
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
	return toolTipText;
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

int keyChar (int key) {
	//TODO - use the NS key constants
	switch (key) {
		case SWT.BS: return OS.NSBackspaceCharacter;
		case SWT.CR: return OS.NSCarriageReturnCharacter;
		case SWT.DEL: return OS.NSDeleteCharacter;
		case SWT.ESC: return SWT.ESC;
		case SWT.LF: return OS.NSNewlineCharacter;
		case SWT.TAB: return OS.NSTabCharacter;
//		case ' ': return OS.kMenuBlankGlyph;
//		case ' ': return OS.kMenuSpaceGlyph;
		case SWT.ALT: return 0x2325;
		case SWT.SHIFT: return 0x21E7;
		case SWT.CONTROL: return 0xF2303;
		case SWT.COMMAND: return 0x2318;
		case SWT.ARROW_UP: return 0x2191;
		case SWT.ARROW_DOWN: return 0x2193;
		case SWT.ARROW_LEFT: return 0x2190;
		case SWT.ARROW_RIGHT: return 0x2192;
		case SWT.PAGE_UP: return 0x21DE;
		case SWT.PAGE_DOWN: return 0x21DF;
		case SWT.KEYPAD_CR: return OS.NSEnterCharacter;
		case SWT.HELP: return OS.NSHelpFunctionKey;
		case SWT.HOME: return 0xF729;
		case SWT.END: return 0xF72B;
//		case SWT.CAPS_LOCK: return ??;
		case SWT.F1: return 0xF704;
		case SWT.F2: return 0xF705;
		case SWT.F3: return 0xF706;
		case SWT.F4: return 0xF707;
		case SWT.F5: return 0xF708;
		case SWT.F6: return 0xF709;
		case SWT.F7: return 0xF70A;
		case SWT.F8: return 0xF70B;
		case SWT.F9: return 0xF70C;
		case SWT.F10: return 0xF70D;
		case SWT.F11: return 0xF70E;
		case SWT.F12: return 0xF70F;
		case SWT.F13: return 0xF710;
		case SWT.F14: return 0xF711;
		case SWT.F15: return 0xF712;
		/*
		* The following lines are intentionally commented.
		*/
//		case SWT.INSERT: return ??;
	}
	return 0;
}


@Override
void register () {
	super.register ();
	display.addWidget (nsItem, this);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	if (nsItem != null) nsItem.release();
	nsItem = null;
	parent = null;
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

@Override
void sendSelection () {
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

	Event event = new Event ();
	sendSelectionEvent (SWT.Selection, event, nsItemAction != 0);
	// Widget may be disposed at this point
	if (isDisposed()) return;
	if (nsItemAction != 0) {
		if (event.doit) {
			NSApplication app = NSApplication.sharedApplication();
			app.sendAction(nsItemAction, nsItemTarget, app);
		}
	}
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
	int key = accelerator & SWT.KEY_MASK;
	if (key == SWT.ESC && /* no masks */key == accelerator && !display.escAsAcceleratorPresent) {
		display.escAsAcceleratorPresent = true;
	}
	int virtualKey = keyChar (key);
	String string = virtualKey != 0 ? (char)virtualKey + "" : (char)key + "";
	NSString nsstring = (NSString) new NSString().alloc();
	nsstring = nsstring.initWithString(string);
	nsItem.setKeyEquivalent (nsstring.lowercaseString());
	nsstring.release();
	int mask = 0;
	if ((accelerator & SWT.SHIFT) != 0) mask |= OS.NSShiftKeyMask;
	if ((accelerator & SWT.CONTROL) != 0) mask |= OS.NSControlKeyMask;
	if ((accelerator & SWT.COMMAND) != 0) mask |= OS.NSCommandKeyMask;
	if ((accelerator & SWT.ALT) != 0) mask |= OS.NSAlternateKeyMask;
	nsItem.setKeyEquivalentModifierMask (mask);
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
	if (enabled) {
		state &= ~DISABLED;
	} else {
		state |= DISABLED;
	}
	nsItem.setEnabled(enabled);
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
	nsItem.setTag(id);
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
	nsItem.setImage(image != null? image.handle : null);
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
		if (menu.isDisposed() || (menu == parent)) error(SWT.ERROR_INVALID_ARGUMENT);
		if ((menu.style & SWT.DROP_DOWN) == 0) {
			error (SWT.ERROR_MENU_NOT_DROP_DOWN);
		}
		if (parent.parent != null && menu.parent != parent.parent) {
			error (SWT.ERROR_INVALID_PARENT);
		}
	}
	/* Assign the new menu */
	Menu oldMenu = this.menu;
	if (oldMenu == menu) return;
	if (oldMenu != null) oldMenu.cascade = null;
	this.menu = menu;

	/* Update the menu in the OS */
	if (menu == null) {
		NSMenu emptyMenu = createEmptyMenu ();
		if (emptyMenu != null) {
			nsItem.setSubmenu (emptyMenu);
			emptyMenu.release();
		}
	} else {
		menu.cascade = this;
		nsItem.setSubmenu (menu.nsMenu);
	}

	if (menu != null) {
		nsItem.setTarget(null);
		nsItem.setAction(0);
	} else {
		nsItem.setTarget(nsItem);
		nsItem.setAction(OS.sel_sendSelection);
	}

	/* Update menu title with parent item title */
	updateText ();
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
	nsItem.setState(selected ? OS.NSOnState : OS.NSOffState);
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
	updateText ();
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
	checkWidget();

	if (toolTip == null && toolTipText != null) {
		toolTipText = null;
		nsItem.setToolTip (null);
	}
	if (toolTip == null || toolTip.trim().length() == 0 || toolTip.equals(toolTipText)) return;

	toolTipText = toolTip;

	char[] chars = new char [toolTipText.length ()];
	toolTipText.getChars (0, chars.length, chars, 0);
	int length = fixMnemonic (chars);
	nsItem.setToolTip (NSString.stringWithCharacters (chars, length));
}

void updateText () {
	if (isDisposed() || parent.isDisposed()) return;
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
	String text = new String (buffer, 0, j);
	NSMenu submenu = nsItem.submenu ();
	NSString label = (NSString) new NSString().alloc();
	label = label.initWithString(text);
	if(submenu != null && (parent.getStyle () & SWT.BAR) != 0) {
		submenu.setTitle (label);
	} else {
		int direction = (parent.getStyle () & SWT.RIGHT_TO_LEFT) != 0 ? OS.NSWritingDirectionRightToLeft : OS.NSWritingDirectionLeftToRight;
		NSMutableDictionary dict = ((NSMutableDictionary)new NSMutableDictionary().alloc()).initWithCapacity(2);
		NSMutableParagraphStyle paragraphStyle = (NSMutableParagraphStyle)new NSMutableParagraphStyle ().alloc ().init ();
		paragraphStyle.setBaseWritingDirection(direction);
		paragraphStyle.setLineBreakMode(OS.NSLineBreakByTruncatingMiddle);
		dict.setObject (paragraphStyle, OS.NSParagraphStyleAttributeName);
		paragraphStyle.release ();
		dict.setObject (NSFont.menuBarFontOfSize(0), OS.NSFontAttributeName);
		NSAttributedString attribStr = ((NSAttributedString) new NSAttributedString ().alloc ()).initWithString (label, dict);
		dict.release();
		nsItem.setAttributedTitle(attribStr);
		/*
		 * Feature in Cocoa.  Setting the attributed title on an NSMenuItem
		 * also sets the title, but clearing the attributed title does not
		 * clear the title.  The fix is to explicitly set the title to an
		 * empty string in this case.
		 */
		if (text.length() == 0) nsItem.setTitle(NSString.string());
		attribStr.release();
	}
	label.release();
}

boolean updateAccelerator (boolean show) {
	if (accelerator != 0) return false;
	int mask = 0, key = 0;
	if (show) {
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
		if (i < buffer.length && buffer [i] == '\t') {
			for (j = i + 1; j < buffer.length; j++) {
				switch (buffer [j]) {
					case '\u2303': mask |= OS.NSControlKeyMask; i++; break;
					case '\u2325': mask |= OS.NSAlternateKeyMask; i++; break;
					case '\u21E7': mask |= OS.NSShiftKeyMask; i++; break;
					case '\u2318': mask |= OS.NSCommandKeyMask; i++; break;
					default:
						j = buffer.length;
						break;
				}
			}
			switch (buffer.length - i - 1) {
				case 1:
					key = buffer [i + 1];
					if (key == 0x2423) key = ' ';
					break;
				case 2:
					if (buffer [i + 1] == 'F') {
						switch (buffer [i + 2]) {
							case '1': key = 0xF704; break;
							case '2': key = 0xF705; break;
							case '3': key = 0xF706; break;
							case '4': key = 0xF707; break;
							case '5': key = 0xF708; break;
							case '6': key = 0xF709; break;
							case '7': key = 0xF70A; break;
							case '8': key = 0xF70B; break;
							case '9': key = 0xF70C; break;
						}
					}
					break;
				case 3:
					if (buffer [i + 1] == 'F' && buffer [i + 2] == '1') {
						switch (buffer [i + 3]) {
							case '0': key = 0xF70D; break;
							case '1': key = 0xF70E; break;
							case '2': key = 0xF70F; break;
							case '3': key = 0xF710; break;
							case '4': key = 0xF711; break;
							case '5': key = 0xF712; break;
						}
					}
					break;
			}
		}
	}
	nsItem.setKeyEquivalentModifierMask (mask);
	NSString nsstring = (NSString) new NSString().alloc();
	nsstring = nsstring.initWithString(key == 0 ? "" : String.valueOf ((char)key));
	nsItem.setKeyEquivalent (nsstring.lowercaseString ());
	nsstring.release();
	return key != 0;
}

}


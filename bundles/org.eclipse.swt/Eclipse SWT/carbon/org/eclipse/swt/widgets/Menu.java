/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.internal.carbon.FontInfo;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.MenuTrackingData;
import org.eclipse.swt.internal.carbon.Rect;
 
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

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
 */
public class Menu extends Widget {
	/**
	 * the handle to the OS resource 
	 * (Warning: This field is platform dependent)
	 */
	int handle;
	short id;
	int x, y;
	boolean hasLocation, modified, closed;
	MenuItem [] items;
	MenuItem cascade, defaultItem, lastTarget;
	Decorations parent;

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

void _setVisible (boolean visible) {
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	if (!visible) return;
	int left = x, top = y;
	if (!hasLocation) {
		org.eclipse.swt.internal.carbon.Point where = new org.eclipse.swt.internal.carbon.Point ();
		OS.GetGlobalMouse (where);
		left = where.h; top = where.v;
	}
	OS.PopUpMenuSelect (handle, (short)top, (short)left, (short)-1);
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

void createHandle () {
	display.addMenu (this);
	int outMenuRef [] = new int [1];
	OS.CreateNewMenu (id, 0, outMenuRef);
	if (outMenuRef [0] == 0) {
		display.removeMenu (this);
		error (SWT.ERROR_NO_HANDLES);
	}
	handle = outMenuRef [0];
}

void createItem (MenuItem item, int index) {
	checkWidget ();
	int count = OS.CountMenuItems (handle);
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	int attributes = OS.kMenuItemAttrAutoRepeat | OS.kMenuItemAttrCustomDraw;
	if ((item.style & SWT.SEPARATOR) != 0) attributes = OS.kMenuItemAttrSeparator;
	int result = OS.InsertMenuItemTextWithCFString (handle, 0, (short) index, attributes, 0);
	if (result != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	if (count == items.length) {
		MenuItem [] newItems = new MenuItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, count - index);
	items [index] = item;
	modified = true;
	if ((style & SWT.BAR) != 0) {
//		Display display = getDisplay ();
//		short menuID = display.nextMenuId ();
//		int outMenuRef [] = new int [1];
//		if (OS.CreateNewMenu (menuID, 0, outMenuRef) != OS.noErr) {
//			error (SWT.ERROR_NO_HANDLES);
//		}
//		OS.SetMenuItemHierarchicalMenu (handle, (short) (index + 1), outMenuRef [0]);
	}
}

void createWidget () {
	checkOrientation (parent);
	super.createWidget ();
	items = new MenuItem [4];
}

void destroyItem (MenuItem item) {
	int count = OS.CountMenuItems (handle);
	int index = 0;
	while (index < count) {
		if (items [index] == item) break;
		index++;
	}
	if (index == count) return;
	System.arraycopy (items, index + 1, items, index, --count - index);
	items [count] = null;
	if (count == 0) items = new MenuItem [4];
	modified = true;
	if ((style & SWT.BAR) != 0) {
//		int [] outMenuRef = new int [1];
//		OS.GetMenuItemHierarchicalMenu (handle, outIndex [0], outMenuRef);
//		if (outMenuRef [0] != 0) {
//			OS.DeleteMenu (OS.GetMenuID (outMenuRef [0]));
//			OS.DisposeMenu (outMenuRef [0]);
//		}
	}
	OS.DeleteMenuItem (handle, (short) (index + 1));
}

void destroyWidget () {
	int theMenu = handle;
	releaseHandle ();
	if (theMenu != 0) {
		OS.DeleteMenu (OS.GetMenuID (theMenu));
		OS.DisposeMenu (theMenu);
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
	checkWidget();
	return (state & DISABLED) == 0;
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
	int count = OS.CountMenuItems (handle);
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
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
	int count = OS.CountMenuItems (handle);
	MenuItem [] result = new MenuItem [count];
	System.arraycopy (items, 0, result, 0, count);
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
	if ((style & SWT.BAR) != 0) {
		return this == parent.menuShell ().menuBar;
	}
	if ((style & SWT.POP_UP) != 0) {
		Menu [] popups = display.popups;
		if (popups == null) return false;
		for (int i=0; i<popups.length; i++) {
			if (popups [i] == this) return true;
		}
	}
	MenuTrackingData outData = new MenuTrackingData ();
	return OS.GetMenuTrackingData (handle, outData) == OS.noErr;
}

void hookEvents () {
	super.hookEvents ();
	int menuProc = display.menuProc;
	int [] mask = new int [] {
		OS.kEventClassMenu, OS.kEventMenuClosed,
		OS.kEventClassMenu, OS.kEventMenuOpening,
		OS.kEventClassMenu, OS.kEventMenuTargetItem,
		OS.kEventClassMenu, OS.kEventMenuMeasureItemWidth,
		OS.kEventClassMenu, OS.kEventMenuDrawItemContent,
	};
	int menuTarget = OS.GetMenuEventTarget (handle);
	OS.InstallEventHandler (menuTarget, menuProc, mask.length / 2, mask, 0, null);
}

int kEventMenuClosed (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMenuClosed (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	closed = true;
	sendEvent (SWT.Hide);
	return OS.eventNotHandledErr;
}

int kEventMenuDrawItemContent (int nextHandler, int theEvent, int userData) {
	short [] index = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMenuItemIndex, OS.typeMenuItemIndex, null, 2, null, index);
	MenuItem item = items [index [0] - 1];
	if (item.accelerator == 0) {
		int accelIndex = item.text.indexOf ('\t');
		if (accelIndex != -1) {
			String accelText = item.text.substring (accelIndex + 1);
			int length = accelText.length ();
			if (length != 0) {
				int result = OS.CallNextEventHandler (nextHandler, theEvent);
				Rect rect = new Rect ();
				OS.GetEventParameter (theEvent, OS.kEventParamMenuItemBounds, OS.typeQDRectangle, null, Rect.sizeof, null, rect);
				int [] context = new int [1];
				OS.GetEventParameter (theEvent, OS.kEventParamCGContextRef, OS.typeCGContextRef, null, 4, null, context);

				/* Draw the key */
				int modifierIndex = modifierIndex (accelText);
				char [] buffer = new char [length - modifierIndex - 1];
				accelText.getChars (modifierIndex + 1, length, buffer, 0);
				int font = OS.kThemeMenuItemFont;
				if (buffer.length > 1) font = OS.kThemeMenuItemCmdKeyFont;
				byte [] family = new byte [256];
				short [] size = new short [1];
				byte [] style = new byte [1];
				OS.GetThemeFont ((short) font, (short) OS.smSystemScript, family, size, style);
				FontInfo info = new FontInfo ();
				OS.FetchFontInfo (family[0], size[0], style[0], info);
				int [] metric = new int [1];
				OS.GetThemeMetric (OS.kThemeMetricMenuIconTrailingEdgeMargin, metric);
				rect.left = (short) (rect.right - info.widMax - metric [0]);
				int str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
				OS.DrawThemeTextBox (str, (short) font, OS.kThemeStateActive, false, rect, (short) OS.teFlushLeft, context [0]);
				OS.CFRelease (str);
				
				/* Draw the modifiers */
				if (modifierIndex != -1) {
					buffer = new char [modifierIndex + 1];
					accelText.getChars (0, buffer.length, buffer, 0);
					str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
					org.eclipse.swt.internal.carbon.Point size1 = new org.eclipse.swt.internal.carbon.Point ();
					OS.GetThemeTextDimensions (str, (short) OS.kThemeMenuItemCmdKeyFont, 0, false, size1, null);
					rect.right = rect.left;
					rect.left = (short) (rect.right - size1.h);
					OS.DrawThemeTextBox (str, (short) OS.kThemeMenuItemCmdKeyFont, OS.kThemeStateActive, false, rect, (short) OS.teFlushLeft, context [0]);
					OS.CFRelease (str);
				}
				return result;
			}
		}			
	}
	return OS.eventNotHandledErr;
}

int kEventMenuMeasureItemWidth (int nextHandler, int theEvent, int userData) {
	short [] index = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMenuItemIndex, OS.typeMenuItemIndex, null, 2, null, index);
	MenuItem item = items [index [0] - 1];
	if (item.accelerator == 0) {
		int accelIndex = item.text.indexOf ('\t');
		if (accelIndex != -1) {
			String accelText = item.text.substring (accelIndex + 1);
			if (accelText.length () != 0) {
				int result = OS.CallNextEventHandler (nextHandler, theEvent);
				char [] buffer = new char [accelText.length ()];
				accelText.getChars (0, buffer.length, buffer, 0);
				int str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
				org.eclipse.swt.internal.carbon.Point size = new org.eclipse.swt.internal.carbon.Point ();
				OS.GetThemeTextDimensions (str, (short) OS.kThemeMenuItemCmdKeyFont, 0, false, size, null);
				OS.CFRelease (str);
				short [] width = new short [1];
				OS.GetEventParameter (theEvent, OS.kEventParamMenuItemWidth, OS.typeSInt16, null, 2, null, width);
				int [] metric = new int [1];
				OS.GetThemeMetric (OS.kThemeMetricMenuTextTrailingEdgeMargin, metric);
				width [0] += metric [0] + size.h;
				OS.SetEventParameter (theEvent, OS.kEventParamMenuItemWidth, OS.typeSInt16, 2, width);
				return result;
			}
		}			
	}
	return OS.eventNotHandledErr;
}

int kEventMenuOpening (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMenuOpening (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	closed = false;
	sendEvent (SWT.Show);
	modified = false;
	return OS.eventNotHandledErr;
}

int kEventMenuTargetItem (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMenuTargetItem (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	lastTarget = null;
	short [] index = new short [1];
	if (OS.GetEventParameter (theEvent, OS.kEventParamMenuItemIndex, OS.typeMenuItemIndex, null, 2, null, index) == OS.noErr) {
		if (index [0] != 0) lastTarget = items [index [0] - 1];
		if (lastTarget != null) lastTarget.sendEvent (SWT.Arm);
	}
	return OS.eventNotHandledErr;
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
	int count = OS.CountMenuItems (handle);
	for (int i=0; i<count; i++) {
		if (items [i] == item) return i;
	}
	return -1;
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
	checkWidget ();
	Menu parentMenu = getParentMenu ();
	if (parentMenu == null) return getEnabled ();
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

int modifierIndex (String accelText) {
	int start = accelText.length () - 1;
	int index = start;
	while (index >= 0) {
		char c = accelText.charAt (index);
		switch (c) {
			case ' ':
				if (index != start) return index;
				break;
			case '\u2303':
			case '\u2325':
			case '\u21E7':
			case '\u2318':
				return index;
		}
		index--;
	}
	return -1;
}

void releaseChild () {
	super.releaseChild ();
	if (cascade != null) cascade.setMenu (null);
	if ((style & SWT.BAR) != 0 && this == parent.menuBar) {
		parent.setMenuBar (null);
	}
}

void releaseHandle () {
	super.releaseHandle ();
	handle = 0;
}

void releaseWidget () {
	int count = OS.CountMenuItems (handle);
	for (int i=0; i<count; i++) {
		MenuItem item = items [i];
		if (!item.isDisposed ()) item.releaseResources ();
	}
	items = null;
	super.releaseWidget ();
	display.removeMenu (this);
	parent = null;
	cascade = defaultItem = lastTarget = null;
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
	checkWidget();
	if (enabled) {
		state &= ~DISABLED;
		OS.EnableMenuItem (handle, (short)0);
	} else {
		state |= DISABLED;
		OS.DisableMenuItem (handle, (short)0);
	}
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
	this.x = x;
	this.y = y;
	hasLocation = true;
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the display.
 * <p>
 * Note:  This is different from most widgets where the
 * location of the widget is relative to the parent.
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
	setLocation (location.x, location.y);
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
	
}

/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.carbon.ATSFontMetrics;
import org.eclipse.swt.internal.carbon.GDevice;
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
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#menu">Menu snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Menu extends Widget {
	/**
	 * the handle to the OS resource 
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 * 
	 * @noreference This field is not intended to be referenced by clients.
	 */
	int handle;
	short id;
	int x, y, itemCount;
//	int width, height;
	boolean hasLocation, modified, closed, ignoreMatch;
	MenuItem [] items;
	MenuItem cascade, defaultItem, lastTarget;
	Decorations parent;

/**
 * Constructs a new instance of this class given its parent,
 * and sets the style for the instance so that the instance
 * will be a popup menu on the given parent's shell.
 * <p>
 * After constructing a menu, it can be set into its parent
 * using <code>parent.setMenu(menu)</code>.  In this case, the parent may
 * be any control in the same widget tree as the parent.
 * </p>
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
 * </p><p>
 * After constructing a menu or menuBar, it can be set into its parent
 * using <code>parent.setMenu(menu)</code> or <code>parent.setMenuBar(menuBar)</code>.
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
 * @see SWT#NO_RADIO_GROUP
 * @see SWT#LEFT_TO_RIGHT
 * @see SWT#RIGHT_TO_LEFT
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
 * <p>
 * After constructing a drop-down menu, it can be set into its parentMenu
 * using <code>parentMenu.setMenu(menu)</code>.
 * </p>
 *
 * @param parentMenu a menu which will be the parent of the new instance (cannot be null)
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
 * <p>
 * After constructing a drop-down menu, it can be set into its parentItem
 * using <code>parentItem.setMenu(menu)</code>.
 * </p>
 *
 * @param parentItem a menu item which will be the parent of the new instance (cannot be null)
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
	if (visible) {
		org.eclipse.swt.internal.carbon.Point where = new org.eclipse.swt.internal.carbon.Point ();
		TrayItem trayItem = display.currentTrayItem;
		if (trayItem != null) {
			display.trayItemMenu = this;
			Point pt = trayItem.getMenuLocation();
			where.h = (short) pt.x;
			where.v = (short) pt.y;
		} else {
			if (hasLocation) {
				where.h = (short) x;
				where.v = (short) y;
			} else {
				OS.GetGlobalMouse (where);
			}
		}
		/*
		* Bug in the Macintosh.  When a menu is open with ContextualMenuSelect() the
		* system will add other items before displaying it and remove the items before
		* returning from the function.  If the menu is changed in kEventMenuOpening, the
		* system will fail to remove those items.  The fix is to send SWT.Show before
		* calling ContextualMenuSelect() instead of in kEventMenuOpening.
		*/		
		sendEvent (SWT.Show);
		modified = false;
		/*
		* Feature in the Macintosh.  When the application FruitMenu is installed,
		* the output parameters cannot be NULL or ContextualMenuSelect() crashes.
		* The fix is to ensure they are not NULL.
		*/
		OS.ContextualMenuSelect (handle, where, false, OS.kCMHelpItemRemoveHelp, null, null, new int [1], new short [1], new short [1]);
	} else {
		OS.CancelMenuTracking (handle, true, 0);
	}
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
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	int attributes = OS.kMenuItemAttrAutoRepeat | OS.kMenuItemAttrCustomDraw;
	if ((item.style & SWT.SEPARATOR) != 0) attributes = OS.kMenuItemAttrSeparator;
	int result = OS.InsertMenuItemTextWithCFString (handle, 0, (short) index, attributes, 0);
	if (result != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	if (itemCount == items.length) {
		MenuItem [] newItems = new MenuItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
	modified = true;
	int emptyMenu = item.createEmptyMenu ();
	if (emptyMenu != 0) {
		OS.SetMenuItemHierarchicalMenu (handle, (short) (index + 1), emptyMenu);
		OS.ReleaseMenu (emptyMenu);
	}
}

void createWidget () {
	checkOrientation (parent);
	super.createWidget ();
	items = new MenuItem [4];
}

void destroyItem (MenuItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) return;
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	if (itemCount == 0) items = new MenuItem [4];
	modified = true;
	OS.DeleteMenuItem (handle, (short) (index + 1));
}

void destroyWidget () {
	int theMenu = handle;
	releaseHandle ();
	if (theMenu != 0) {
		OS.DisposeMenu (theMenu);
	}
}

void fixMenus (Decorations newParent) {
	this.parent = newParent;
}

/*public*/ Rectangle getBounds () {
	checkWidget ();
	if ((style & SWT.BAR) != 0) {
		Menu menu = display.getMenuBar ();
		if (this != menu) return new Rectangle (0, 0, 0, 0);
		int height = OS.GetMBarHeight ();
		int gdevice = OS.GetMainDevice ();
		int [] ptr = new int [1];
		OS.memmove (ptr, gdevice, 4);
		GDevice device = new GDevice ();
		OS.memmove (device, ptr [0], GDevice.sizeof);
		return new Rectangle (0, 0, device.right - device.left, height);
	}
	OS.CalcMenuSize (handle);
	return new Rectangle (x, y, 0, 0);
//	return new Rectangle (x, y, width, height);
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
 * <code>false</code> otherwise. A disabled menu is typically
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
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
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
	return itemCount;
}

/**
 * Returns a (possibly empty) array of <code>MenuItem</code>s which
 * are the items in the receiver. 
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
	MenuItem [] result = new MenuItem [itemCount];
	int index = 0;
	if (items != null) {
		for (int i = 0; i < itemCount; i++) {
			MenuItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				result [index++] = item;
			}
		}
	}
	if (index != result.length) {
		MenuItem [] newItems = new MenuItem[index];
		System.arraycopy(result, 0, newItems, 0, index);
		result = newItems;
	}
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
		OS.kEventClassMenu, OS.kEventMenuCalculateSize,
		OS.kEventClassMenu, OS.kEventMenuClosed,
		OS.kEventClassMenu, OS.kEventMenuCreateFrameView,
		OS.kEventClassMenu, OS.kEventMenuDrawItem,
		OS.kEventClassMenu, OS.kEventMenuDrawItemContent,
		OS.kEventClassMenu, OS.kEventMenuMeasureItemWidth,
		OS.kEventClassMenu, OS.kEventMenuOpening,
		OS.kEventClassMenu, OS.kEventMenuTargetItem,
		OS.kEventClassMenu, OS.kEventMenuMatchKey,
	};
	int menuTarget = OS.GetMenuEventTarget (handle);
	OS.InstallEventHandler (menuTarget, menuProc, mask.length / 2, mask, handle, null);
}

int kEventMenuCalculateSize (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMenuCalculateSize (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] theControl = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamControlRef, OS.typeControlRef, null, 4, null, theControl);
	int menuProc = display.menuProc;
	int [] mask = new int [] {
		OS.kEventClassMenu, OS.kEventMenuGetFrameBounds,
	};
	int controlTarget = OS.GetControlEventTarget (theControl [0]);
	//TODO - installed multi-times, does this matter?
	OS.InstallEventHandler (controlTarget, menuProc, mask.length / 2, mask, handle, null);
	return result;
}

int kEventMenuClosed (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMenuClosed (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	closed = true;
//	width = height = 0;
//	int count = OS.CountMenuItems (handle);
//	for (int i=0; i<count; i++) {
//		MenuItem item = items [i];
//		item.x = item.y = item.width = item.height = 0;
//	}
	/*
	* Feature in the Macintosh.  In order to populate the search field of
	* the help menu, the events kEventMenuOpening, kEventMenuClosed and
	* others are sent to sub menus even when the cascade item of the submenu
	* is disabled.  Normally, the user can never get to these submenus.
	* This means that application code does not expect SWT.Show and SWT.Hide
	* events.  The fix is to avoid the events when the cascade item is
	* disabled.
	*/
	boolean send = true;
	if (cascade != null && !cascade.getEnabled ()) send = false;
	if (send) sendEvent (SWT.Hide);
	return OS.eventNotHandledErr;
}

int kEventMenuCreateFrameView (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMenuCreateFrameView (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] theControl = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamControlRef, OS.typeControlRef, null, 4, null, theControl);
	int menuProc = display.menuProc;
	int [] mask = new int [] {
		OS.kEventClassMenu, OS.kEventMenuGetFrameBounds,
	};
	int controlTarget = OS.GetControlEventTarget (theControl [0]);
	OS.InstallEventHandler (controlTarget, menuProc, mask.length / 2, mask, handle, null);
	return OS.eventNotHandledErr;
}

int kEventMenuDrawItem (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMenuDrawItem (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
//	short [] index = new short [1];
//	OS.GetEventParameter (theEvent, OS.kEventParamMenuItemIndex, OS.typeMenuItemIndex, null, 2, null, index);
//	MenuItem item = items [index [0] - 1];
//	Rect rect = new Rect ();
//	OS.GetEventParameter (theEvent, OS.kEventParamMenuItemBounds, OS.typeQDRectangle, null, Rect.sizeof, null, rect);
//	item.x = rect.left - x;
//	item.y = rect.top - y;
//	item.width = rect.right - rect.left;
//	item.height = rect.bottom - rect.top;
	return OS.eventNotHandledErr;
}

int kEventMenuDrawItemContent (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMenuDrawItemContent (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	short [] index = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMenuItemIndex, OS.typeMenuItemIndex, null, 2, null, index);
	if (!(0 < index [0] && index [0] <= itemCount)) return result;
	MenuItem item = items [index [0] - 1];
	if (item.accelerator == 0 && !item.acceleratorSet) {
		int accelIndex = item.text.indexOf ('\t');
		if (accelIndex != -1) {
			String accelText = item.text.substring (accelIndex + 1);
			int length = accelText.length ();
			if (length != 0) {
				result = OS.CallNextEventHandler (nextHandler, theEvent);
				Rect rect = new Rect ();
				OS.GetEventParameter (theEvent, OS.kEventParamMenuItemBounds, OS.typeQDRectangle, null, Rect.sizeof, null, rect);
				int [] context = new int [1];
				OS.GetEventParameter (theEvent, OS.kEventParamCGContextRef, OS.typeCGContextRef, null, 4, null, context);

				/* Draw the key */
				int modifierIndex = modifierIndex (accelText);
				char [] buffer = new char [length - modifierIndex - 1];
				accelText.getChars (modifierIndex + 1, length, buffer, 0);
				int themeFont = OS.kThemeMenuItemFont;
				if (buffer.length > 1) themeFont = OS.kThemeMenuItemCmdKeyFont;
				byte [] family = new byte [256];
				short [] size = new short [1];
				byte [] style = new byte [1];
				OS.GetThemeFont ((short) themeFont, (short) OS.smSystemScript, family, size, style);
				short id = OS.FMGetFontFamilyFromName (family);
				int [] font = new int [1]; 
				OS.FMGetFontFromFontFamilyInstance (id, style [0], font, null);
				int atsFont = OS.FMGetATSFontRefFromFont (font [0]);
				ATSFontMetrics fontMetrics = new ATSFontMetrics ();
				OS.ATSFontGetVerticalMetrics (atsFont, OS.kATSOptionFlagsDefault, fontMetrics);
				OS.ATSFontGetHorizontalMetrics (atsFont, OS.kATSOptionFlagsDefault, fontMetrics);
				int [] metric = new int [1];
				OS.GetThemeMetric (OS.kThemeMetricMenuIconTrailingEdgeMargin, metric);
				int str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
				org.eclipse.swt.internal.carbon.Point size1 = new org.eclipse.swt.internal.carbon.Point ();
				OS.GetThemeTextDimensions (str, (short) themeFont, 0, false, size1, null);
				rect.left = (short) (rect.right - Math.max ((int)(fontMetrics.maxAdvanceWidth * size[0]), size1.h) - metric [0]);
				OS.DrawThemeTextBox (str, (short) themeFont, OS.kThemeStateActive, false, rect, (short) OS.teFlushLeft, context [0]);
				OS.CFRelease (str);
				
				/* Draw the modifiers */
				if (modifierIndex != -1) {
					buffer = new char [modifierIndex + 1];
					accelText.getChars (0, buffer.length, buffer, 0);
					str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
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

int kEventMenuGetFrameBounds (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMenuGetFrameBounds (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	result = OS.CallNextEventHandler (nextHandler, theEvent);
//	CGRect rect = new CGRect ();
//	OS.GetEventParameter (theEvent, OS.kEventParamBounds, OS.typeHIRect, null, CGRect.sizeof, null, rect);
//	x = (int) rect.x;
//	y = (int) rect.y;
//	width = (int) rect.width;
//	height = (int) rect.height;
//	if (cascade != null) {
//		OS.GetEventParameter (theEvent, OS.kEventParamMenuItemBounds, OS.typeHIRect, null, CGRect.sizeof, null, rect);
//		cascade.x = (int) rect.x - x;
//		cascade.y = (int) rect.y - y;
//		cascade.width = (int) rect.width;
//		cascade.height = (int) rect.height;
//	}
	return result;
}

int kEventMenuMatchKey (int nextHandler, int theEvent, int userData) {
	if (ignoreMatch) return OS.eventNotHandledErr;
	int [] menuIndex = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamDirectObject, OS.typeMenuRef, null, 4, null, menuIndex);
	int [] eventRef = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamEventRef, OS.typeEventRef, null, 4, null, eventRef);
	int options = OS.kMenuEventDontCheckSubmenus | OS.kMenuEventIncludeDisabledItems | OS.kMenuEventQueryOnly;
	short [] index = new short [1];
	ignoreMatch = true;
	boolean isMenuKeyEvent = OS.IsMenuKeyEvent(menuIndex[0], eventRef[0], options, null, index);
	ignoreMatch = false;
	if (isMenuKeyEvent && 0 <= index [0] && index [0] < items.length) {
		MenuItem item = items [index [0] - 1];
		if (item.accelerator == 0) {
			/* Tell Menu Manager we don't want command key matching */
			return OS.menuItemNotFoundError;
		}
	}
	/* Tell Menu Manager to use default command key matching algorithm */
	return OS.eventNotHandledErr; 
}

int kEventMenuMeasureItemWidth (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMenuMeasureItemWidth (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	short [] index = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMenuItemIndex, OS.typeMenuItemIndex, null, 2, null, index);
	if (!(0 < index [0] && index [0] <= itemCount)) return result;
	MenuItem item = items [index [0] - 1];
	if (item.accelerator == 0 && !item.acceleratorSet) {
		int accelIndex = item.text.indexOf ('\t');
		if (accelIndex != -1) {
			String accelText = item.text.substring (accelIndex + 1);
			if (accelText.length () != 0) {
				result = OS.CallNextEventHandler (nextHandler, theEvent);
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
	/*
	* Bug in the Macintosh.  When a menu is open with ContextualMenuSelect() the
	* system will add other items before displaying it and remove the items before
	* returning from the function.  If the menu is changed in kEventMenuOpening, the
	* system will fail to remove those items.  The fix is to send SWT.Show before
	* calling ContextualMenuSelect() instead of in kEventMenuOpening.
	*/	
	if ((style & SWT.POP_UP) == 0) {
		/*
		* Feature in the Macintosh.  In order to populate the search field of
		* the help menu, the events kEventMenuOpening, kEventMenuClosed and
		* others are sent to sub menus even when the cascade item of the submenu
		* is disabled.  Normally, the user can never get to these submenus.
		* This means that application code does not expect SWT.Show and SWT.Hide
		* events.  The fix is to avoid the events when the cascade item is
		* disabled.
		*/
		boolean send = true;
		if (cascade != null && !cascade.getEnabled ()) send = false;
		if (send) {
			sendEvent (SWT.Show);
			modified = false;
		}
	}
	return OS.eventNotHandledErr;
}

int kEventMenuTargetItem (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMenuTargetItem (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	lastTarget = null;
	short [] index = new short [1];
	if (OS.GetEventParameter (theEvent, OS.kEventParamMenuItemIndex, OS.typeMenuItemIndex, null, 2, null, index) == OS.noErr) {
		if (0 < index [0] && index [0] <= itemCount) lastTarget = items [index [0] - 1];
		if (lastTarget != null) {
			lastTarget.sendEvent (SWT.Arm);
		}
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
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (MenuItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<itemCount; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled menu is typically not selectable from the
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
	if (parentMenu == null) {
		return getEnabled () && parent.isEnabled ();
	}
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

void releaseChildren (boolean destroy) {
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			MenuItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			}
		}
		items = null;
	}
	super.releaseChildren (destroy);
}

void releaseHandle () {
	super.releaseHandle ();
	handle = 0;
}

void releaseParent () {
	super.releaseParent ();
	if (cascade != null) cascade.setMenu (null);
	if ((style & SWT.BAR) != 0 && this == parent.menuBar) {
		parent.setMenuBar (null);
	}
}

void releaseWidget () {
	super.releaseWidget ();
	display.removeMenu (this);
	parent = null;
	cascade = defaultItem = lastTarget = null;
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
 * be notified when the menu events are generated for the control.
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

void reskinChildren (int flags) {
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		item.reskin (flags);
	}
	super.reskinChildren (flags);
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
 * and disables it otherwise. A disabled menu is typically
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
 * Sets the location of the receiver, which must be a popup,
 * to the point specified by the arguments which are relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Note that the platform window manager ultimately has control
 * over the location of popup menus.
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
 * Sets the location of the receiver, which must be a popup,
 * to the point specified by the argument which is relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Note that the platform window manager ultimately has control
 * over the location of popup menus.
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

package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.carbon.*;

public class Menu extends Widget {
	int handle;
	short id, lastIndex;
	int x, y;
	boolean hasLocation;
	MenuItem cascade, defaultItem;
	Decorations parent;

public Menu (Control parent) {
	this (checkNull (parent).getShell (), SWT.POP_UP);
}

public Menu (Decorations parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget ();
}

public Menu (Menu parentMenu) {
	this (checkNull (parentMenu).parent, SWT.DROP_DOWN);
}

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

public void _setVisible (boolean visible) {
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	if (!visible) return;
	int left = x, top = y;
	if (!hasLocation) {
		org.eclipse.swt.internal.carbon.Point where = new org.eclipse.swt.internal.carbon.Point ();
		OS.GetGlobalMouse (where);
		left = where.h; top = where.v;
	}
	int index = defaultItem != null ? indexOf (defaultItem) + 1 : lastIndex;
	int result = OS.PopUpMenuSelect (handle, (short)top, (short)left, (short)(index));
	lastIndex = OS.LoWord (result);
}

public void addHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

public void addMenuListener (MenuListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Hide,typedListener);
	addListener (SWT.Show,typedListener);
}

void createHandle () {
	Display display = getDisplay ();
	int menuProc = display.menuProc;
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
	Display display = getDisplay ();
	display.addMenuItem (item);
	int attributes = 0;
	if ((item.style & SWT.SEPARATOR) != 0) attributes = OS.kMenuItemAttrSeparator;
	int result = OS.InsertMenuItemTextWithCFString (handle, 0, (short) index, attributes, item.id);
	if (result != OS.noErr) {
		display.removeMenuItem (item);
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
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

void destroyItem (MenuItem item) {
	short [] outIndex = new short [1];
	if (OS.GetIndMenuItemWithCommandID (handle, item.id, 1, null, outIndex) != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	if ((style & SWT.BAR) != 0) {
//		int [] outMenuRef = new int [1];
//		OS.GetMenuItemHierarchicalMenu (handle, outIndex [0], outMenuRef);
//		if (outMenuRef [0] != 0) {
//			OS.DeleteMenu (OS.GetMenuID (outMenuRef [0]));
//			OS.DisposeMenu (outMenuRef [0]);
//		}
	}
	OS.DeleteMenuItem (handle, outIndex [0]);
}

void destroyWidget () {
	int theMenu = handle;
	releaseHandle ();
	if (theMenu != 0) {
		OS.DeleteMenu (OS.GetMenuID (theMenu));
		OS.DisposeMenu (theMenu);
	}
}

public MenuItem getDefaultItem () {
	checkWidget();
	return defaultItem;
}

public Display getDisplay () {
	Decorations parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public boolean getEnabled () {
	checkWidget();
	return (state & DISABLED) == 0;
}

public MenuItem getItem (int index) {
	checkWidget ();
	int [] outCommandID= new int [1];
	if (OS.GetMenuItemCommandID (handle, (short)(index+1), outCommandID) != OS.noErr) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	Display display = getDisplay ();
	return display.findMenuItem (outCommandID[0]);
}

public int getItemCount () {
	checkWidget ();
	return OS.CountMenuItems (handle);
}

public MenuItem [] getItems () {
	checkWidget ();
	Display display = getDisplay ();
	int length = OS.CountMenuItems (handle);
	MenuItem [] items = new MenuItem [length];
	int [] outCommandID= new int [1];	
	for (int i=0; i<items.length; i++) {
		if (OS.GetMenuItemCommandID (handle, (short)(i+1), outCommandID) != OS.noErr) {
			error (SWT.ERROR_CANNOT_GET_ITEM);
		}
		items [i] = display.findMenuItem (outCommandID [0]);
	}
	return items;
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

public Decorations getParent () {
	checkWidget ();
	return parent;
}

public MenuItem getParentItem () {
	checkWidget ();
	return cascade;
}

public Menu getParentMenu () {
	checkWidget ();
	if (cascade != null) return cascade.parent;
	return null;
}

public Shell getShell () {
	checkWidget ();
	return parent.getShell ();
}

public boolean getVisible () {
	checkWidget ();
	if ((style & SWT.BAR) != 0) {
		return this == parent.menuShell ().menuBar;
	}
	if ((style & SWT.POP_UP) != 0) {
		Display display = getDisplay ();
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
	Display display = getDisplay ();
	int menuProc = display.menuProc;
	int [] mask = new int [] {
		OS.kEventClassMenu, OS.kEventMenuOpening,
		OS.kEventClassMenu, OS.kEventMenuClosed,
	};
	int menuTarget = OS.GetMenuEventTarget (handle);
	OS.InstallEventHandler (menuTarget, menuProc, mask.length / 2, mask, 0, null);
}

int kEventMenuClosed (int nextHandler, int theEvent, int userData) {
	sendEvent (SWT.Hide);
	return OS.eventNotHandledErr;
}

int kEventMenuOpening (int nextHandler, int theEvent, int userData) {
	sendEvent (SWT.Show);
	return OS.eventNotHandledErr;
}

public int indexOf (MenuItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] outMenu = new int [1];
	short [] outIndex = new short [1];
	if (OS.GetIndMenuItemWithCommandID (handle, item.id, 1, outMenu, outIndex) == OS.noErr) {
		return handle == outMenu [0] ? outIndex [0] - 1 : 0;
	}	
	return -1;
}

public boolean isEnabled () {
	checkWidget ();
	Menu parentMenu = getParentMenu ();
	if (parentMenu == null) return getEnabled ();
	return getEnabled () && parentMenu.isEnabled ();
}

public boolean isVisible () {
	checkWidget ();
	return getVisible ();
}

void releaseChild () {
	super.releaseChild ();
	if (cascade != null) cascade.setMenu (null);
	if ((style & SWT.BAR) != 0 && this == parent.menuBar) {
		parent.setMenuBar (null);
	}
}

void releaseWidget () {
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		if (!item.isDisposed ()) item.releaseResources ();
	}
	super.releaseWidget ();
	Display display = getDisplay ();
	display.removeMenu (this);
	parent = null;
	cascade = null;
}

public void removeHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

public void removeMenuListener (MenuListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Hide, listener);
	eventTable.unhook (SWT.Show, listener);
}

public void setDefaultItem (MenuItem item) {
	checkWidget();
	if (item != null && item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	defaultItem = item;
}

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

public void setLocation (int x, int y) {
	checkWidget ();
	this.x = x;
	this.y = y;
	hasLocation = true;
}

public void setVisible (boolean visible) {
	checkWidget ();
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	Display display = getDisplay ();
	if (visible) {
		display.addPopup (this);
	} else {
		display.removePopup (this);
		_setVisible (false);
	}
}
	
}

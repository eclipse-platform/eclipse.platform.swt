/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSMenu extends NSObject {

public NSMenu() {
	super();
}

public NSMenu(int id) {
	super(id);
}

public void addItem(NSMenuItem newItem) {
	OS.objc_msgSend(this.id, OS.sel_addItem_1, newItem != null ? newItem.id : 0);
}

public NSMenuItem addItemWithTitle(NSString aString, int aSelector, NSString charCode) {
	int result = OS.objc_msgSend(this.id, OS.sel_addItemWithTitle_1action_1keyEquivalent_1, aString != null ? aString.id : 0, aSelector, charCode != null ? charCode.id : 0);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSMenu attachedMenu() {
	int result = OS.objc_msgSend(this.id, OS.sel_attachedMenu);
	return result == this.id ? this : (result != 0 ? new NSMenu(result) : null);
}

public boolean autoenablesItems() {
	return OS.objc_msgSend(this.id, OS.sel_autoenablesItems) != 0;
}

public void cancelTracking() {
	OS.objc_msgSend(this.id, OS.sel_cancelTracking);
}

public id contextMenuRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_contextMenuRepresentation);
	return result != 0 ? new id(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void helpRequested(NSEvent eventPtr) {
	OS.objc_msgSend(this.id, OS.sel_helpRequested_1, eventPtr != null ? eventPtr.id : 0);
}

public NSMenuItem highlightedItem() {
	int result = OS.objc_msgSend(this.id, OS.sel_highlightedItem);
	return result != 0 ? new NSMenuItem(result) : null;
}

public int indexOfItem(NSMenuItem index) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItem_1, index != null ? index.id : 0);
}

public int indexOfItemWithRepresentedObject(id object) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItemWithRepresentedObject_1, object != null ? object.id : 0);
}

public int indexOfItemWithSubmenu(NSMenu submenu) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItemWithSubmenu_1, submenu != null ? submenu.id : 0);
}

public int indexOfItemWithTag(int aTag) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItemWithTag_1, aTag);
}

public int indexOfItemWithTarget(id target, int actionSelector) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItemWithTarget_1andAction_1, target != null ? target.id : 0, actionSelector);
}

public int indexOfItemWithTitle(NSString aTitle) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItemWithTitle_1, aTitle != null ? aTitle.id : 0);
}

public NSMenu initWithTitle(NSString aTitle) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithTitle_1, aTitle != null ? aTitle.id : 0);
	return result != 0 ? this : null;
}

public void insertItem(NSMenuItem newItem, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertItem_1atIndex_1, newItem != null ? newItem.id : 0, index);
}

public NSMenuItem insertItemWithTitle(NSString aString, int aSelector, NSString charCode, int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_insertItemWithTitle_1action_1keyEquivalent_1atIndex_1, aString != null ? aString.id : 0, aSelector, charCode != null ? charCode.id : 0, index);
	return result != 0 ? new NSMenuItem(result) : null;
}

public boolean isAttached() {
	return OS.objc_msgSend(this.id, OS.sel_isAttached) != 0;
}

public boolean isTornOff() {
	return OS.objc_msgSend(this.id, OS.sel_isTornOff) != 0;
}

public NSArray itemArray() {
	int result = OS.objc_msgSend(this.id, OS.sel_itemArray);
	return result != 0 ? new NSArray(result) : null;
}

public NSMenuItem itemAtIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_itemAtIndex_1, index);
	return result != 0 ? new NSMenuItem(result) : null;
}

public void itemChanged(NSMenuItem item) {
	OS.objc_msgSend(this.id, OS.sel_itemChanged_1, item != null ? item.id : 0);
}

public NSMenuItem itemWithTag(int tag) {
	int result = OS.objc_msgSend(this.id, OS.sel_itemWithTag_1, tag);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSMenuItem itemWithTitle(NSString aTitle) {
	int result = OS.objc_msgSend(this.id, OS.sel_itemWithTitle_1, aTitle != null ? aTitle.id : 0);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSPoint locationForSubmenu(NSMenu aSubmenu) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_locationForSubmenu_1, aSubmenu != null ? aSubmenu.id : 0);
	return result;
}

public float menuBarHeight() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_menuBarHeight);
}

public static boolean menuBarVisible() {
	return OS.objc_msgSend(OS.class_NSMenu, OS.sel_menuBarVisible) != 0;
}

public boolean menuChangedMessagesEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_menuChangedMessagesEnabled) != 0;
}

public id menuRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_menuRepresentation);
	return result != 0 ? new id(result) : null;
}

public static int menuZone() {
	return OS.objc_msgSend(OS.class_NSMenu, OS.sel_menuZone);
}

public int numberOfItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfItems);
}

public void performActionForItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_performActionForItemAtIndex_1, index);
}

public boolean performKeyEquivalent(NSEvent theEvent) {
	return OS.objc_msgSend(this.id, OS.sel_performKeyEquivalent_1, theEvent != null ? theEvent.id : 0) != 0;
}

public static void static_popUpContextMenu_withEvent_forView_(NSMenu menu, NSEvent event, NSView view) {
	OS.objc_msgSend(OS.class_NSMenu, OS.sel_popUpContextMenu_1withEvent_1forView_1, menu != null ? menu.id : 0, event != null ? event.id : 0, view != null ? view.id : 0);
}

public static void static_popUpContextMenu_withEvent_forView_withFont_(NSMenu menu, NSEvent event, NSView view, NSFont font) {
	OS.objc_msgSend(OS.class_NSMenu, OS.sel_popUpContextMenu_1withEvent_1forView_1withFont_1, menu != null ? menu.id : 0, event != null ? event.id : 0, view != null ? view.id : 0, font != null ? font.id : 0);
}

public void removeItem(NSMenuItem item) {
	OS.objc_msgSend(this.id, OS.sel_removeItem_1, item != null ? item.id : 0);
}

public void removeItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removeItemAtIndex_1, index);
}

public void setAutoenablesItems(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutoenablesItems_1, flag);
}

public void setContextMenuRepresentation(id menuRep) {
	OS.objc_msgSend(this.id, OS.sel_setContextMenuRepresentation_1, menuRep != null ? menuRep.id : 0);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public static void setMenuBarVisible(boolean visible) {
	OS.objc_msgSend(OS.class_NSMenu, OS.sel_setMenuBarVisible_1, visible);
}

public void setMenuChangedMessagesEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setMenuChangedMessagesEnabled_1, flag);
}

public void setMenuRepresentation(id menuRep) {
	OS.objc_msgSend(this.id, OS.sel_setMenuRepresentation_1, menuRep != null ? menuRep.id : 0);
}

public static void setMenuZone(int aZone) {
	OS.objc_msgSend(OS.class_NSMenu, OS.sel_setMenuZone_1, aZone);
}

public void setShowsStateColumn(boolean showsState) {
	OS.objc_msgSend(this.id, OS.sel_setShowsStateColumn_1, showsState);
}

public void setSubmenu(NSMenu aMenu, NSMenuItem anItem) {
	OS.objc_msgSend(this.id, OS.sel_setSubmenu_1forItem_1, aMenu != null ? aMenu.id : 0, anItem != null ? anItem.id : 0);
}

public void setSupermenu(NSMenu supermenu) {
	OS.objc_msgSend(this.id, OS.sel_setSupermenu_1, supermenu != null ? supermenu.id : 0);
}

public void setTearOffMenuRepresentation(id menuRep) {
	OS.objc_msgSend(this.id, OS.sel_setTearOffMenuRepresentation_1, menuRep != null ? menuRep.id : 0);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1, aString != null ? aString.id : 0);
}

public boolean showsStateColumn() {
	return OS.objc_msgSend(this.id, OS.sel_showsStateColumn) != 0;
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public void submenuAction(id sender) {
	OS.objc_msgSend(this.id, OS.sel_submenuAction_1, sender != null ? sender.id : 0);
}

public NSMenu supermenu() {
	int result = OS.objc_msgSend(this.id, OS.sel_supermenu);
	return result == this.id ? this : (result != 0 ? new NSMenu(result) : null);
}

public id tearOffMenuRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_tearOffMenuRepresentation);
	return result != 0 ? new id(result) : null;
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public void update() {
	OS.objc_msgSend(this.id, OS.sel_update);
}

}

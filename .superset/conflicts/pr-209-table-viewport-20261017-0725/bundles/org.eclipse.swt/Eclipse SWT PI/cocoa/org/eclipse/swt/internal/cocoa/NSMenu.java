/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSMenu extends NSObject {

public NSMenu() {
	super();
}

public NSMenu(long id) {
	super(id);
}

public NSMenu(id id) {
	super(id);
}

public void addItem(NSMenuItem newItem) {
	OS.objc_msgSend(this.id, OS.sel_addItem_, newItem != null ? newItem.id : 0);
}

public NSMenuItem addItemWithTitle(NSString aString, long aSelector, NSString charCode) {
	long result = OS.objc_msgSend(this.id, OS.sel_addItemWithTitle_action_keyEquivalent_, aString != null ? aString.id : 0, aSelector, charCode != null ? charCode.id : 0);
	return result != 0 ? new NSMenuItem(result) : null;
}

public void cancelTracking() {
	OS.objc_msgSend(this.id, OS.sel_cancelTracking);
}

public long indexOfItemWithTarget(id target, long actionSelector) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItemWithTarget_andAction_, target != null ? target.id : 0, actionSelector);
}

public NSMenu initWithTitle(NSString aTitle) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithTitle_, aTitle != null ? aTitle.id : 0);
	return result == this.id ? this : (result != 0 ? new NSMenu(result) : null);
}

public void insertItem(NSMenuItem newItem, long index) {
	OS.objc_msgSend(this.id, OS.sel_insertItem_atIndex_, newItem != null ? newItem.id : 0, index);
}

public NSArray itemArray() {
	long result = OS.objc_msgSend(this.id, OS.sel_itemArray);
	return result != 0 ? new NSArray(result) : null;
}

public NSMenuItem itemAtIndex(long index) {
	long result = OS.objc_msgSend(this.id, OS.sel_itemAtIndex_, index);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSMenuItem itemWithTag(long tag) {
	long result = OS.objc_msgSend(this.id, OS.sel_itemWithTag_, tag);
	return result != 0 ? new NSMenuItem(result) : null;
}

public long numberOfItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfItems);
}

public boolean performKeyEquivalent(NSEvent theEvent) {
	return OS.objc_msgSend_bool(this.id, OS.sel_performKeyEquivalent_, theEvent != null ? theEvent.id : 0);
}

public static void popUpContextMenu(NSMenu menu, NSEvent event, NSView view) {
	OS.objc_msgSend(OS.class_NSMenu, OS.sel_popUpContextMenu_withEvent_forView_, menu != null ? menu.id : 0, event != null ? event.id : 0, view != null ? view.id : 0);
}

public void removeItem(NSMenuItem item) {
	OS.objc_msgSend(this.id, OS.sel_removeItem_, item != null ? item.id : 0);
}

public void removeItemAtIndex(long index) {
	OS.objc_msgSend(this.id, OS.sel_removeItemAtIndex_, index);
}

public void setAutoenablesItems(boolean autoenablesItems) {
	OS.objc_msgSend(this.id, OS.sel_setAutoenablesItems_, autoenablesItems);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, delegate != null ? delegate.id : 0);
}

public void setSubmenu(NSMenu aMenu, NSMenuItem anItem) {
	OS.objc_msgSend(this.id, OS.sel_setSubmenu_forItem_, aMenu != null ? aMenu.id : 0, anItem != null ? anItem.id : 0);
}

public void setTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, title != null ? title.id : 0);
}

public NSString title() {
	long result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

}

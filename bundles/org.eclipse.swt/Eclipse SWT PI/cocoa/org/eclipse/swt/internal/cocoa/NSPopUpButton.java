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

public class NSPopUpButton extends NSButton {

public NSPopUpButton() {
	super();
}

public NSPopUpButton(int id) {
	super(id);
}

public void addItemWithTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_addItemWithTitle_1, title != null ? title.id : 0);
}

public void addItemsWithTitles(NSArray itemTitles) {
	OS.objc_msgSend(this.id, OS.sel_addItemsWithTitles_1, itemTitles != null ? itemTitles.id : 0);
}

public boolean autoenablesItems() {
	return OS.objc_msgSend(this.id, OS.sel_autoenablesItems) != 0;
}

public int indexOfItem(NSMenuItem item) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItem_1, item != null ? item.id : 0);
}

public int indexOfItemWithRepresentedObject(id obj) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItemWithRepresentedObject_1, obj != null ? obj.id : 0);
}

public int indexOfItemWithTag(int tag) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItemWithTag_1, tag);
}

public int indexOfItemWithTarget(id target, int actionSelector) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItemWithTarget_1andAction_1, target != null ? target.id : 0, actionSelector);
}

public int indexOfItemWithTitle(NSString title) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItemWithTitle_1, title != null ? title.id : 0);
}

public int indexOfSelectedItem() {
	return OS.objc_msgSend(this.id, OS.sel_indexOfSelectedItem);
}

public NSPopUpButton initWithFrame(NSRect buttonFrame, boolean flag) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_1pullsDown_1, buttonFrame, flag);
	return result != 0 ? this : null;
}

public void insertItemWithTitle(NSString title, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertItemWithTitle_1atIndex_1, title != null ? title.id : 0, index);
}

public NSArray itemArray() {
	int result = OS.objc_msgSend(this.id, OS.sel_itemArray);
	return result != 0 ? new NSArray(result) : null;
}

public NSMenuItem itemAtIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_itemAtIndex_1, index);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSString itemTitleAtIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_itemTitleAtIndex_1, index);
	return result != 0 ? new NSString(result) : null;
}

public NSArray itemTitles() {
	int result = OS.objc_msgSend(this.id, OS.sel_itemTitles);
	return result != 0 ? new NSArray(result) : null;
}

public NSMenuItem itemWithTitle(NSString title) {
	int result = OS.objc_msgSend(this.id, OS.sel_itemWithTitle_1, title != null ? title.id : 0);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSMenuItem lastItem() {
	int result = OS.objc_msgSend(this.id, OS.sel_lastItem);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSMenu menu() {
	int result = OS.objc_msgSend(this.id, OS.sel_menu);
	return result != 0 ? new NSMenu(result) : null;
}

public int numberOfItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfItems);
}

public int preferredEdge() {
	return OS.objc_msgSend(this.id, OS.sel_preferredEdge);
}

public boolean pullsDown() {
	return OS.objc_msgSend(this.id, OS.sel_pullsDown) != 0;
}

public void removeAllItems() {
	OS.objc_msgSend(this.id, OS.sel_removeAllItems);
}

public void removeItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removeItemAtIndex_1, index);
}

public void removeItemWithTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_removeItemWithTitle_1, title != null ? title.id : 0);
}

public void selectItem(NSMenuItem item) {
	OS.objc_msgSend(this.id, OS.sel_selectItem_1, item != null ? item.id : 0);
}

public void selectItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_selectItemAtIndex_1, index);
}

public boolean selectItemWithTag(int tag) {
	return OS.objc_msgSend(this.id, OS.sel_selectItemWithTag_1, tag) != 0;
}

public void selectItemWithTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_selectItemWithTitle_1, title != null ? title.id : 0);
}

public NSMenuItem selectedItem() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedItem);
	return result != 0 ? new NSMenuItem(result) : null;
}

public void setAutoenablesItems(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutoenablesItems_1, flag);
}

public void setMenu(NSMenu menu) {
	OS.objc_msgSend(this.id, OS.sel_setMenu_1, menu != null ? menu.id : 0);
}

public void setPreferredEdge(int edge) {
	OS.objc_msgSend(this.id, OS.sel_setPreferredEdge_1, edge);
}

public void setPullsDown(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPullsDown_1, flag);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1, aString != null ? aString.id : 0);
}

public void synchronizeTitleAndSelectedItem() {
	OS.objc_msgSend(this.id, OS.sel_synchronizeTitleAndSelectedItem);
}

public NSString titleOfSelectedItem() {
	int result = OS.objc_msgSend(this.id, OS.sel_titleOfSelectedItem);
	return result != 0 ? new NSString(result) : null;
}

}

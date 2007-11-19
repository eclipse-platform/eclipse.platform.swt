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

public class NSTabView extends NSView {

public NSTabView() {
	super();
}

public NSTabView(int id) {
	super(id);
}

public void addTabViewItem(NSTabViewItem tabViewItem) {
	OS.objc_msgSend(this.id, OS.sel_addTabViewItem_1, tabViewItem != null ? tabViewItem.id : 0);
}

public boolean allowsTruncatedLabels() {
	return OS.objc_msgSend(this.id, OS.sel_allowsTruncatedLabels) != 0;
}

public NSRect contentRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentRect);
	return result;
}

public int controlSize() {
	return OS.objc_msgSend(this.id, OS.sel_controlSize);
}

public int controlTint() {
	return OS.objc_msgSend(this.id, OS.sel_controlTint);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public boolean drawsBackground() {
	return OS.objc_msgSend(this.id, OS.sel_drawsBackground) != 0;
}

public NSFont font() {
	int result = OS.objc_msgSend(this.id, OS.sel_font);
	return result != 0 ? new NSFont(result) : null;
}

public int indexOfTabViewItem(NSTabViewItem tabViewItem) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfTabViewItem_1, tabViewItem != null ? tabViewItem.id : 0);
}

public int indexOfTabViewItemWithIdentifier(id identifier) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfTabViewItemWithIdentifier_1, identifier != null ? identifier.id : 0);
}

public void insertTabViewItem(NSTabViewItem tabViewItem, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertTabViewItem_1atIndex_1, tabViewItem != null ? tabViewItem.id : 0, index);
}

public NSSize minimumSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_struct(result, this.id, OS.sel_minimumSize);
	return result;
}

public int numberOfTabViewItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfTabViewItems);
}

public void removeTabViewItem(NSTabViewItem tabViewItem) {
	OS.objc_msgSend(this.id, OS.sel_removeTabViewItem_1, tabViewItem != null ? tabViewItem.id : 0);
}

public void selectFirstTabViewItem(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectFirstTabViewItem_1, sender != null ? sender.id : 0);
}

public void selectLastTabViewItem(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectLastTabViewItem_1, sender != null ? sender.id : 0);
}

public void selectNextTabViewItem(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectNextTabViewItem_1, sender != null ? sender.id : 0);
}

public void selectPreviousTabViewItem(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectPreviousTabViewItem_1, sender != null ? sender.id : 0);
}

public void selectTabViewItem(NSTabViewItem tabViewItem) {
	OS.objc_msgSend(this.id, OS.sel_selectTabViewItem_1, tabViewItem != null ? tabViewItem.id : 0);
}

public void selectTabViewItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_selectTabViewItemAtIndex_1, index);
}

public void selectTabViewItemWithIdentifier(id identifier) {
	OS.objc_msgSend(this.id, OS.sel_selectTabViewItemWithIdentifier_1, identifier != null ? identifier.id : 0);
}

public NSTabViewItem selectedTabViewItem() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedTabViewItem);
	return result != 0 ? new NSTabViewItem(result) : null;
}

public void setAllowsTruncatedLabels(boolean allowTruncatedLabels) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsTruncatedLabels_1, allowTruncatedLabels);
}

public void setControlSize(int controlSize) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_1, controlSize);
}

public void setControlTint(int controlTint) {
	OS.objc_msgSend(this.id, OS.sel_setControlTint_1, controlTint);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_1, flag);
}

public void setFont(NSFont font) {
	OS.objc_msgSend(this.id, OS.sel_setFont_1, font != null ? font.id : 0);
}

public void setTabViewType(int tabViewType) {
	OS.objc_msgSend(this.id, OS.sel_setTabViewType_1, tabViewType);
}

public NSTabViewItem tabViewItemAtIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_tabViewItemAtIndex_1, index);
	return result != 0 ? new NSTabViewItem(result) : null;
}

public NSTabViewItem tabViewItemAtPoint(NSPoint point) {
	int result = OS.objc_msgSend(this.id, OS.sel_tabViewItemAtPoint_1, point);
	return result != 0 ? new NSTabViewItem(result) : null;
}

public NSArray tabViewItems() {
	int result = OS.objc_msgSend(this.id, OS.sel_tabViewItems);
	return result != 0 ? new NSArray(result) : null;
}

public int tabViewType() {
	return OS.objc_msgSend(this.id, OS.sel_tabViewType);
}

public void takeSelectedTabViewItemFromSender(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeSelectedTabViewItemFromSender_1, sender != null ? sender.id : 0);
}

}

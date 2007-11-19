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

public class NSComboBoxCell extends NSTextFieldCell {

public NSComboBoxCell() {
	super();
}

public NSComboBoxCell(int id) {
	super(id);
}

public void addItemWithObjectValue(id object) {
	OS.objc_msgSend(this.id, OS.sel_addItemWithObjectValue_1, object != null ? object.id : 0);
}

public void addItemsWithObjectValues(NSArray objects) {
	OS.objc_msgSend(this.id, OS.sel_addItemsWithObjectValues_1, objects != null ? objects.id : 0);
}

public NSString completedString(NSString string) {
	int result = OS.objc_msgSend(this.id, OS.sel_completedString_1, string != null ? string.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public boolean completes() {
	return OS.objc_msgSend(this.id, OS.sel_completes) != 0;
}

public id dataSource() {
	int result = OS.objc_msgSend(this.id, OS.sel_dataSource);
	return result != 0 ? new id(result) : null;
}

public void deselectItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_deselectItemAtIndex_1, index);
}

public boolean hasVerticalScroller() {
	return OS.objc_msgSend(this.id, OS.sel_hasVerticalScroller) != 0;
}

public int indexOfItemWithObjectValue(id object) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItemWithObjectValue_1, object != null ? object.id : 0);
}

public int indexOfSelectedItem() {
	return OS.objc_msgSend(this.id, OS.sel_indexOfSelectedItem);
}

public void insertItemWithObjectValue(id object, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertItemWithObjectValue_1atIndex_1, object != null ? object.id : 0, index);
}

public NSSize intercellSpacing() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_intercellSpacing);
	return result;
}

public boolean isButtonBordered() {
	return OS.objc_msgSend(this.id, OS.sel_isButtonBordered) != 0;
}

public float itemHeight() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_itemHeight);
}

public id itemObjectValueAtIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_itemObjectValueAtIndex_1, index);
	return result != 0 ? new id(result) : null;
}

public void noteNumberOfItemsChanged() {
	OS.objc_msgSend(this.id, OS.sel_noteNumberOfItemsChanged);
}

public int numberOfItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfItems);
}

public int numberOfVisibleItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfVisibleItems);
}

public id objectValueOfSelectedItem() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectValueOfSelectedItem);
	return result != 0 ? new id(result) : null;
}

public NSArray objectValues() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectValues);
	return result != 0 ? new NSArray(result) : null;
}

public void reloadData() {
	OS.objc_msgSend(this.id, OS.sel_reloadData);
}

public void removeAllItems() {
	OS.objc_msgSend(this.id, OS.sel_removeAllItems);
}

public void removeItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removeItemAtIndex_1, index);
}

public void removeItemWithObjectValue(id object) {
	OS.objc_msgSend(this.id, OS.sel_removeItemWithObjectValue_1, object != null ? object.id : 0);
}

public void scrollItemAtIndexToTop(int index) {
	OS.objc_msgSend(this.id, OS.sel_scrollItemAtIndexToTop_1, index);
}

public void scrollItemAtIndexToVisible(int index) {
	OS.objc_msgSend(this.id, OS.sel_scrollItemAtIndexToVisible_1, index);
}

public void selectItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_selectItemAtIndex_1, index);
}

public void selectItemWithObjectValue(id object) {
	OS.objc_msgSend(this.id, OS.sel_selectItemWithObjectValue_1, object != null ? object.id : 0);
}

public void setButtonBordered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setButtonBordered_1, flag);
}

public void setCompletes(boolean completes) {
	OS.objc_msgSend(this.id, OS.sel_setCompletes_1, completes);
}

public void setDataSource(id aSource) {
	OS.objc_msgSend(this.id, OS.sel_setDataSource_1, aSource != null ? aSource.id : 0);
}

public void setHasVerticalScroller(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHasVerticalScroller_1, flag);
}

public void setIntercellSpacing(NSSize aSize) {
	OS.objc_msgSend(this.id, OS.sel_setIntercellSpacing_1, aSize);
}

public void setItemHeight(float itemHeight) {
	OS.objc_msgSend(this.id, OS.sel_setItemHeight_1, itemHeight);
}

public void setNumberOfVisibleItems(int visibleItems) {
	OS.objc_msgSend(this.id, OS.sel_setNumberOfVisibleItems_1, visibleItems);
}

public void setUsesDataSource(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setUsesDataSource_1, flag);
}

public boolean usesDataSource() {
	return OS.objc_msgSend(this.id, OS.sel_usesDataSource) != 0;
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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

public class NSComboBox extends NSTextField {

public NSComboBox() {
	super();
}

public NSComboBox(long id) {
	super(id);
}

public NSComboBox(id id) {
	super(id);
}

public void addItemWithObjectValue(id object) {
	OS.objc_msgSend(this.id, OS.sel_addItemWithObjectValue_, object != null ? object.id : 0);
}

public void deselectItemAtIndex(long index) {
	OS.objc_msgSend(this.id, OS.sel_deselectItemAtIndex_, index);
}

public long indexOfSelectedItem() {
	return OS.objc_msgSend(this.id, OS.sel_indexOfSelectedItem);
}

public void insertItemWithObjectValue(id object, long index) {
	OS.objc_msgSend(this.id, OS.sel_insertItemWithObjectValue_atIndex_, object != null ? object.id : 0, index);
}

public double itemHeight() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_itemHeight);
}

public id itemObjectValueAtIndex(long index) {
	long result = OS.objc_msgSend(this.id, OS.sel_itemObjectValueAtIndex_, index);
	return result != 0 ? new id(result) : null;
}

public long numberOfItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfItems);
}

public long numberOfVisibleItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfVisibleItems);
}

public void removeAllItems() {
	OS.objc_msgSend(this.id, OS.sel_removeAllItems);
}

public void removeItemAtIndex(long index) {
	OS.objc_msgSend(this.id, OS.sel_removeItemAtIndex_, index);
}

public void selectItemAtIndex(long index) {
	OS.objc_msgSend(this.id, OS.sel_selectItemAtIndex_, index);
}

public void setNumberOfVisibleItems(long numberOfVisibleItems) {
	OS.objc_msgSend(this.id, OS.sel_setNumberOfVisibleItems_, numberOfVisibleItems);
}

public static long cellClass() {
	return OS.objc_msgSend(OS.class_NSComboBox, OS.sel_cellClass);
}

public static void setCellClass(long factoryId) {
	OS.objc_msgSend(OS.class_NSComboBox, OS.sel_setCellClass_, factoryId);
}

}

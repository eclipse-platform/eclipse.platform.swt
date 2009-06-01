/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSComboBox extends NSTextField {

public NSComboBox() {
	super();
}

public NSComboBox(int /*long*/ id) {
	super(id);
}

public NSComboBox(id id) {
	super(id);
}

public void addItemWithObjectValue(id object) {
	OS.objc_msgSend(this.id, OS.sel_addItemWithObjectValue_, object != null ? object.id : 0);
}

public void deselectItemAtIndex(int /*long*/ index) {
	OS.objc_msgSend(this.id, OS.sel_deselectItemAtIndex_, index);
}

public int /*long*/ indexOfSelectedItem() {
	return OS.objc_msgSend(this.id, OS.sel_indexOfSelectedItem);
}

public void insertItemWithObjectValue(id object, int /*long*/ index) {
	OS.objc_msgSend(this.id, OS.sel_insertItemWithObjectValue_atIndex_, object != null ? object.id : 0, index);
}

public id itemObjectValueAtIndex(int /*long*/ index) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_itemObjectValueAtIndex_, index);
	return result != 0 ? new id(result) : null;
}

public int /*long*/ numberOfItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfItems);
}

public int /*long*/ numberOfVisibleItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfVisibleItems);
}

public void removeAllItems() {
	OS.objc_msgSend(this.id, OS.sel_removeAllItems);
}

public void removeItemAtIndex(int /*long*/ index) {
	OS.objc_msgSend(this.id, OS.sel_removeItemAtIndex_, index);
}

public void selectItemAtIndex(int /*long*/ index) {
	OS.objc_msgSend(this.id, OS.sel_selectItemAtIndex_, index);
}

public void setNumberOfVisibleItems(int /*long*/ visibleItems) {
	OS.objc_msgSend(this.id, OS.sel_setNumberOfVisibleItems_, visibleItems);
}

public static int /*long*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSComboBox, OS.sel_cellClass);
}

public static void setCellClass(int /*long*/ factoryId) {
	OS.objc_msgSend(OS.class_NSComboBox, OS.sel_setCellClass_, factoryId);
}

}

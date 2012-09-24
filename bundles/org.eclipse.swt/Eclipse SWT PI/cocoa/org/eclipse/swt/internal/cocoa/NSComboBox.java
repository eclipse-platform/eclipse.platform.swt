/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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

public NSComboBox(long /*int*/ id) {
	super(id);
}

public NSComboBox(id id) {
	super(id);
}

public void addItemWithObjectValue(id object) {
	OS.objc_msgSend(this.id, OS.sel_addItemWithObjectValue_, object != null ? object.id : 0);
}

public void deselectItemAtIndex(long /*int*/ index) {
	OS.objc_msgSend(this.id, OS.sel_deselectItemAtIndex_, index);
}

public long /*int*/ indexOfSelectedItem() {
	return OS.objc_msgSend(this.id, OS.sel_indexOfSelectedItem);
}

public void insertItemWithObjectValue(id object, long /*int*/ index) {
	OS.objc_msgSend(this.id, OS.sel_insertItemWithObjectValue_atIndex_, object != null ? object.id : 0, index);
}

public double /*float*/ itemHeight() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_itemHeight);
}

public id itemObjectValueAtIndex(long /*int*/ index) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_itemObjectValueAtIndex_, index);
	return result != 0 ? new id(result) : null;
}

public long /*int*/ numberOfItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfItems);
}

public long /*int*/ numberOfVisibleItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfVisibleItems);
}

public void removeAllItems() {
	OS.objc_msgSend(this.id, OS.sel_removeAllItems);
}

public void removeItemAtIndex(long /*int*/ index) {
	OS.objc_msgSend(this.id, OS.sel_removeItemAtIndex_, index);
}

public void selectItemAtIndex(long /*int*/ index) {
	OS.objc_msgSend(this.id, OS.sel_selectItemAtIndex_, index);
}

public void setNumberOfVisibleItems(long /*int*/ visibleItems) {
	OS.objc_msgSend(this.id, OS.sel_setNumberOfVisibleItems_, visibleItems);
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSComboBox, OS.sel_cellClass);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSComboBox, OS.sel_setCellClass_, factoryId);
}

}

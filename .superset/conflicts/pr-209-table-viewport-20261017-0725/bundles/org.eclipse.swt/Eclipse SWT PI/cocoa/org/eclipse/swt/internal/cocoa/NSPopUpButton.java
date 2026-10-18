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

public class NSPopUpButton extends NSButton {

public NSPopUpButton() {
	super();
}

public NSPopUpButton(long id) {
	super(id);
}

public NSPopUpButton(id id) {
	super(id);
}

public long indexOfSelectedItem() {
	return OS.objc_msgSend(this.id, OS.sel_indexOfSelectedItem);
}

public NSPopUpButton initWithFrame(NSRect buttonFrame, boolean flag) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_pullsDown_, buttonFrame, flag);
	return result == this.id ? this : (result != 0 ? new NSPopUpButton(result) : null);
}

public NSMenuItem itemAtIndex(long index) {
	long result = OS.objc_msgSend(this.id, OS.sel_itemAtIndex_, index);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSString itemTitleAtIndex(long index) {
	long result = OS.objc_msgSend(this.id, OS.sel_itemTitleAtIndex_, index);
	return result != 0 ? new NSString(result) : null;
}

public NSMenu menu() {
	long result = OS.objc_msgSend(this.id, OS.sel_menu);
	return result != 0 ? new NSMenu(result) : null;
}

public long numberOfItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfItems);
}

public void removeAllItems() {
	OS.objc_msgSend(this.id, OS.sel_removeAllItems);
}

public void removeItemAtIndex(long index) {
	OS.objc_msgSend(this.id, OS.sel_removeItemAtIndex_, index);
}

public void selectItem(NSMenuItem item) {
	OS.objc_msgSend(this.id, OS.sel_selectItem_, item != null ? item.id : 0);
}

public void selectItemAtIndex(long index) {
	OS.objc_msgSend(this.id, OS.sel_selectItemAtIndex_, index);
}

public void setAutoenablesItems(boolean autoenablesItems) {
	OS.objc_msgSend(this.id, OS.sel_setAutoenablesItems_, autoenablesItems);
}

public void setPullsDown(boolean pullsDown) {
	OS.objc_msgSend(this.id, OS.sel_setPullsDown_, pullsDown);
}

public NSString titleOfSelectedItem() {
	long result = OS.objc_msgSend(this.id, OS.sel_titleOfSelectedItem);
	return result != 0 ? new NSString(result) : null;
}

public static long cellClass() {
	return OS.objc_msgSend(OS.class_NSPopUpButton, OS.sel_cellClass);
}

public static void setCellClass(long factoryId) {
	OS.objc_msgSend(OS.class_NSPopUpButton, OS.sel_setCellClass_, factoryId);
}

}

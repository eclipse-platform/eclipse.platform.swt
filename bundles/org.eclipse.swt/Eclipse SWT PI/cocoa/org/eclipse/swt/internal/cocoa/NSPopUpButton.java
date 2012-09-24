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

public class NSPopUpButton extends NSButton {

public NSPopUpButton() {
	super();
}

public NSPopUpButton(long /*int*/ id) {
	super(id);
}

public NSPopUpButton(id id) {
	super(id);
}

public long /*int*/ indexOfSelectedItem() {
	return OS.objc_msgSend(this.id, OS.sel_indexOfSelectedItem);
}

public NSPopUpButton initWithFrame(NSRect buttonFrame, boolean flag) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_pullsDown_, buttonFrame, flag);
	return result == this.id ? this : (result != 0 ? new NSPopUpButton(result) : null);
}

public NSMenuItem itemAtIndex(long /*int*/ index) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_itemAtIndex_, index);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSString itemTitleAtIndex(long /*int*/ index) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_itemTitleAtIndex_, index);
	return result != 0 ? new NSString(result) : null;
}

public NSMenu menu() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_menu);
	return result != 0 ? new NSMenu(result) : null;
}

public long /*int*/ numberOfItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfItems);
}

public void removeAllItems() {
	OS.objc_msgSend(this.id, OS.sel_removeAllItems);
}

public void removeItemAtIndex(long /*int*/ index) {
	OS.objc_msgSend(this.id, OS.sel_removeItemAtIndex_, index);
}

public void selectItem(NSMenuItem item) {
	OS.objc_msgSend(this.id, OS.sel_selectItem_, item != null ? item.id : 0);
}

public void selectItemAtIndex(long /*int*/ index) {
	OS.objc_msgSend(this.id, OS.sel_selectItemAtIndex_, index);
}

public void setAutoenablesItems(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutoenablesItems_, flag);
}

public void setPullsDown(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPullsDown_, flag);
}

public NSString titleOfSelectedItem() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_titleOfSelectedItem);
	return result != 0 ? new NSString(result) : null;
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSPopUpButton, OS.sel_cellClass);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSPopUpButton, OS.sel_setCellClass_, factoryId);
}

}

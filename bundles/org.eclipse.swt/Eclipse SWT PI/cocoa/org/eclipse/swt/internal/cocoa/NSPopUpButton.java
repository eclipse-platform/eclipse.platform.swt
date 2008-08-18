/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

public NSPopUpButton(int /*long*/ id) {
	super(id);
}

public NSPopUpButton(id id) {
	super(id);
}

public int /*long*/ indexOfSelectedItem() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_indexOfSelectedItem);
}

public NSPopUpButton initWithFrame(NSRect buttonFrame, boolean flag) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_pullsDown_, buttonFrame, flag);
	return result == this.id ? this : (result != 0 ? new NSPopUpButton(result) : null);
}

public NSMenuItem itemAtIndex(int /*long*/ index) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_itemAtIndex_, index);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSString itemTitleAtIndex(int /*long*/ index) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_itemTitleAtIndex_, index);
	return result != 0 ? new NSString(result) : null;
}

public NSMenu menu() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_menu);
	return result != 0 ? new NSMenu(result) : null;
}

public int /*long*/ numberOfItems() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_numberOfItems);
}

public void removeAllItems() {
	OS.objc_msgSend(this.id, OS.sel_removeAllItems);
}

public void removeItemAtIndex(int /*long*/ index) {
	OS.objc_msgSend(this.id, OS.sel_removeItemAtIndex_, index);
}

public void selectItem(NSMenuItem item) {
	OS.objc_msgSend(this.id, OS.sel_selectItem_, item != null ? item.id : 0);
}

public void selectItemAtIndex(int /*long*/ index) {
	OS.objc_msgSend(this.id, OS.sel_selectItemAtIndex_, index);
}

public void setAutoenablesItems(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutoenablesItems_, flag);
}

public void setPullsDown(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPullsDown_, flag);
}

public NSString titleOfSelectedItem() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_titleOfSelectedItem);
	return result != 0 ? new NSString(result) : null;
}

}

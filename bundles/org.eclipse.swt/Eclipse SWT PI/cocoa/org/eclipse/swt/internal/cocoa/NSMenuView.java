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

public class NSMenuView extends NSView {

public NSMenuView() {
	super();
}

public NSMenuView(int id) {
	super(id);
}

public void attachSubmenuForItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_attachSubmenuForItemAtIndex_1, index);
}

public NSMenu attachedMenu() {
	int result = OS.objc_msgSend(this.id, OS.sel_attachedMenu);
	return result != 0 ? new NSMenu(result) : null;
}

public NSMenuView attachedMenuView() {
	int result = OS.objc_msgSend(this.id, OS.sel_attachedMenuView);
	return result == this.id ? this : (result != 0 ? new NSMenuView(result) : null);
}

public void detachSubmenu() {
	OS.objc_msgSend(this.id, OS.sel_detachSubmenu);
}

public NSFont font() {
	int result = OS.objc_msgSend(this.id, OS.sel_font);
	return result != 0 ? new NSFont(result) : null;
}

public int highlightedItemIndex() {
	return OS.objc_msgSend(this.id, OS.sel_highlightedItemIndex);
}

public float horizontalEdgePadding() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_horizontalEdgePadding);
}

public float imageAndTitleOffset() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_imageAndTitleOffset);
}

public float imageAndTitleWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_imageAndTitleWidth);
}

public int indexOfItemAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfItemAtPoint_1, point);
}

public NSMenuView initAsTearOff() {
	int result = OS.objc_msgSend(this.id, OS.sel_initAsTearOff);
	return result != 0 ? this : null;
}

public NSRect innerRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_innerRect);
	return result;
}

public boolean isAttached() {
	return OS.objc_msgSend(this.id, OS.sel_isAttached) != 0;
}

public boolean isHorizontal() {
	return OS.objc_msgSend(this.id, OS.sel_isHorizontal) != 0;
}

public boolean isTornOff() {
	return OS.objc_msgSend(this.id, OS.sel_isTornOff) != 0;
}

public void itemAdded(NSNotification notification) {
	OS.objc_msgSend(this.id, OS.sel_itemAdded_1, notification != null ? notification.id : 0);
}

public void itemChanged(NSNotification notification) {
	OS.objc_msgSend(this.id, OS.sel_itemChanged_1, notification != null ? notification.id : 0);
}

public void itemRemoved(NSNotification notification) {
	OS.objc_msgSend(this.id, OS.sel_itemRemoved_1, notification != null ? notification.id : 0);
}

public float keyEquivalentOffset() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_keyEquivalentOffset);
}

public float keyEquivalentWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_keyEquivalentWidth);
}

public NSPoint locationForSubmenu(NSMenu aSubmenu) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_locationForSubmenu_1, aSubmenu != null ? aSubmenu.id : 0);
	return result;
}

public NSMenu menu() {
	int result = OS.objc_msgSend(this.id, OS.sel_menu);
	return result != 0 ? new NSMenu(result) : null;
}

public static float menuBarHeight() {
	return (float)OS.objc_msgSend_fpret(OS.class_NSMenuView, OS.sel_menuBarHeight);
}

public NSMenuItemCell menuItemCellForItemAtIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_menuItemCellForItemAtIndex_1, index);
	return result != 0 ? new NSMenuItemCell(result) : null;
}

public boolean needsSizing() {
	return OS.objc_msgSend(this.id, OS.sel_needsSizing) != 0;
}

public void performActionWithHighlightingForItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_performActionWithHighlightingForItemAtIndex_1, index);
}

public NSRect rectOfItemAtIndex(int index) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectOfItemAtIndex_1, index);
	return result;
}

public void setFont(NSFont font) {
	OS.objc_msgSend(this.id, OS.sel_setFont_1, font != null ? font.id : 0);
}

public void setHighlightedItemIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_setHighlightedItemIndex_1, index);
}

public void setHorizontal(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontal_1, flag);
}

public void setHorizontalEdgePadding(float pad) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontalEdgePadding_1, pad);
}

public void setMenu(NSMenu menu) {
	OS.objc_msgSend(this.id, OS.sel_setMenu_1, menu != null ? menu.id : 0);
}

public void setMenuItemCell(NSMenuItemCell cell, int index) {
	OS.objc_msgSend(this.id, OS.sel_setMenuItemCell_1forItemAtIndex_1, cell != null ? cell.id : 0, index);
}

public void setNeedsDisplayForItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_setNeedsDisplayForItemAtIndex_1, index);
}

public void setNeedsSizing(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setNeedsSizing_1, flag);
}

public void setWindowFrameForAttachingToRect(NSRect screenRect, NSScreen screen, int edge, int selectedItemIndex) {
	OS.objc_msgSend(this.id, OS.sel_setWindowFrameForAttachingToRect_1onScreen_1preferredEdge_1popUpSelectedItem_1, screenRect, screen != null ? screen.id : 0, edge, selectedItemIndex);
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public float stateImageOffset() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_stateImageOffset);
}

public float stateImageWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_stateImageWidth);
}

public boolean trackWithEvent(NSEvent event) {
	return OS.objc_msgSend(this.id, OS.sel_trackWithEvent_1, event != null ? event.id : 0) != 0;
}

public void update() {
	OS.objc_msgSend(this.id, OS.sel_update);
}

}

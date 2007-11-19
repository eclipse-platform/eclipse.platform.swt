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

public class NSMenuItemCell extends NSButtonCell {

public NSMenuItemCell() {
	super();
}

public NSMenuItemCell(int id) {
	super(id);
}

public void calcSize() {
	OS.objc_msgSend(this.id, OS.sel_calcSize);
}

public void drawBorderAndBackgroundWithFrame(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawBorderAndBackgroundWithFrame_1inView_1, cellFrame, controlView != null ? controlView.id : 0);
}

public void drawImageWithFrame(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawImageWithFrame_1inView_1, cellFrame, controlView != null ? controlView.id : 0);
}

public void drawKeyEquivalentWithFrame(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawKeyEquivalentWithFrame_1inView_1, cellFrame, controlView != null ? controlView.id : 0);
}

public void drawSeparatorItemWithFrame(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawSeparatorItemWithFrame_1inView_1, cellFrame, controlView != null ? controlView.id : 0);
}

public void drawStateImageWithFrame(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawStateImageWithFrame_1inView_1, cellFrame, controlView != null ? controlView.id : 0);
}

public void drawTitleWithFrame(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawTitleWithFrame_1inView_1, cellFrame, controlView != null ? controlView.id : 0);
}

public float imageWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_imageWidth);
}

public NSRect keyEquivalentRectForBounds(NSRect cellFrame) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_keyEquivalentRectForBounds_1, cellFrame);
	return result;
}

public float keyEquivalentWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_keyEquivalentWidth);
}

public NSMenuItem menuItem() {
	int result = OS.objc_msgSend(this.id, OS.sel_menuItem);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSMenuView menuView() {
	int result = OS.objc_msgSend(this.id, OS.sel_menuView);
	return result != 0 ? new NSMenuView(result) : null;
}

public boolean needsDisplay() {
	return OS.objc_msgSend(this.id, OS.sel_needsDisplay) != 0;
}

public boolean needsSizing() {
	return OS.objc_msgSend(this.id, OS.sel_needsSizing) != 0;
}

public void setMenuItem(NSMenuItem item) {
	OS.objc_msgSend(this.id, OS.sel_setMenuItem_1, item != null ? item.id : 0);
}

public void setMenuView(NSMenuView menuView) {
	OS.objc_msgSend(this.id, OS.sel_setMenuView_1, menuView != null ? menuView.id : 0);
}

public void setNeedsDisplay(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setNeedsDisplay_1, flag);
}

public void setNeedsSizing(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setNeedsSizing_1, flag);
}

public NSRect stateImageRectForBounds(NSRect cellFrame) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_stateImageRectForBounds_1, cellFrame);
	return result;
}

public float stateImageWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_stateImageWidth);
}

public int tag() {
	return OS.objc_msgSend(this.id, OS.sel_tag);
}

public NSRect titleRectForBounds(NSRect cellFrame) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_titleRectForBounds_1, cellFrame);
	return result;
}

public float titleWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_titleWidth);
}

}

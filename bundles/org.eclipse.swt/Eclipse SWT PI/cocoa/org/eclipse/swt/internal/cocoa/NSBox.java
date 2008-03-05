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

public class NSBox extends NSView {

public NSBox() {
	super();
}

public NSBox(int id) {
	super(id);
}

public NSColor borderColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_borderColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSRect borderRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_borderRect);
	return result;
}

public int borderType() {
	return OS.objc_msgSend(this.id, OS.sel_borderType);
}

public float borderWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_borderWidth);
}

public int boxType() {
	return OS.objc_msgSend(this.id, OS.sel_boxType);
}

public NSView contentView() {
	int result = OS.objc_msgSend(this.id, OS.sel_contentView);
	return result != 0 ? new NSView(result) : null;
}

public NSSize contentViewMargins() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentViewMargins);
	return result;
}

public float cornerRadius() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_cornerRadius);
}

public NSColor fillColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_fillColor);
	return result != 0 ? new NSColor(result) : null;
}

public boolean isTransparent() {
	return OS.objc_msgSend(this.id, OS.sel_isTransparent) != 0;
}

public void setBorderColor(NSColor borderColor) {
	OS.objc_msgSend(this.id, OS.sel_setBorderColor_1, borderColor != null ? borderColor.id : 0);
}

public void setBorderType(int aType) {
	OS.objc_msgSend(this.id, OS.sel_setBorderType_1, aType);
}

public void setBorderWidth(float borderWidth) {
	OS.objc_msgSend(this.id, OS.sel_setBorderWidth_1, borderWidth);
}

public void setBoxType(int boxType) {
	OS.objc_msgSend(this.id, OS.sel_setBoxType_1, boxType);
}

public void setContentView(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_setContentView_1, aView != null ? aView.id : 0);
}

public void setContentViewMargins(NSSize offsetSize) {
	OS.objc_msgSend(this.id, OS.sel_setContentViewMargins_1, offsetSize);
}

public void setCornerRadius(float cornerRadius) {
	OS.objc_msgSend(this.id, OS.sel_setCornerRadius_1, cornerRadius);
}

public void setFillColor(NSColor fillColor) {
	OS.objc_msgSend(this.id, OS.sel_setFillColor_1, fillColor != null ? fillColor.id : 0);
}

public void setFrameFromContentFrame(NSRect contentFrame) {
	OS.objc_msgSend(this.id, OS.sel_setFrameFromContentFrame_1, contentFrame);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1, aString != null ? aString.id : 0);
}

public void setTitleFont(NSFont fontObj) {
	OS.objc_msgSend(this.id, OS.sel_setTitleFont_1, fontObj != null ? fontObj.id : 0);
}

public void setTitlePosition(int aPosition) {
	OS.objc_msgSend(this.id, OS.sel_setTitlePosition_1, aPosition);
}

public void setTitleWithMnemonic(NSString stringWithAmpersand) {
	OS.objc_msgSend(this.id, OS.sel_setTitleWithMnemonic_1, stringWithAmpersand != null ? stringWithAmpersand.id : 0);
}

public void setTransparent(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setTransparent_1, flag);
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public NSCell titleCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_titleCell);
	return result != 0 ? new NSCell(result) : null;
}

public NSFont titleFont() {
	int result = OS.objc_msgSend(this.id, OS.sel_titleFont);
	return result != 0 ? new NSFont(result) : null;
}

public int titlePosition() {
	return OS.objc_msgSend(this.id, OS.sel_titlePosition);
}

public NSRect titleRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_titleRect);
	return result;
}

}

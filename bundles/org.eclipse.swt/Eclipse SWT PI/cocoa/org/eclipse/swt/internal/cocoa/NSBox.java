/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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

public class NSBox extends NSView {

public NSBox() {
	super();
}

public NSBox(long /*int*/ id) {
	super(id);
}

public NSBox(id id) {
	super(id);
}

public double /*float*/ borderWidth() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_borderWidth);
}

public NSView contentView() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_contentView);
	return result != 0 ? new NSView(result) : null;
}

public NSSize contentViewMargins() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentViewMargins);
	return result;
}

public void setBorderType(long /*int*/ borderType) {
	OS.objc_msgSend(this.id, OS.sel_setBorderType_, borderType);
}

public void setBorderWidth(double /*float*/ borderWidth) {
	OS.objc_msgSend(this.id, OS.sel_setBorderWidth_, borderWidth);
}

public void setBoxType(long /*int*/ boxType) {
	OS.objc_msgSend(this.id, OS.sel_setBoxType_, boxType);
}

public void setContentView(NSView contentView) {
	OS.objc_msgSend(this.id, OS.sel_setContentView_, contentView != null ? contentView.id : 0);
}

public void setContentViewMargins(NSSize contentViewMargins) {
	OS.objc_msgSend(this.id, OS.sel_setContentViewMargins_, contentViewMargins);
}

public void setFillColor(NSColor fillColor) {
	OS.objc_msgSend(this.id, OS.sel_setFillColor_, fillColor != null ? fillColor.id : 0);
}

public void setFrameFromContentFrame(NSRect contentFrame) {
	OS.objc_msgSend(this.id, OS.sel_setFrameFromContentFrame_, contentFrame);
}

public void setTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, title != null ? title.id : 0);
}

public void setTitleFont(NSFont titleFont) {
	OS.objc_msgSend(this.id, OS.sel_setTitleFont_, titleFont != null ? titleFont.id : 0);
}

public void setTitlePosition(long /*int*/ titlePosition) {
	OS.objc_msgSend(this.id, OS.sel_setTitlePosition_, titlePosition);
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public NSCell titleCell() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_titleCell);
	return result != 0 ? new NSCell(result) : null;
}

public NSFont titleFont() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_titleFont);
	return result != 0 ? new NSFont(result) : null;
}

}

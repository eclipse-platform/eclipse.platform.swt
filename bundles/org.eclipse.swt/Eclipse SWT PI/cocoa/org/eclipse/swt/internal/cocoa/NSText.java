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

public class NSText extends NSView {

public NSText() {
	super();
}

public NSText(long /*int*/ id) {
	super(id);
}

public NSText(id id) {
	super(id);
}

public void copy(id sender) {
	OS.objc_msgSend(this.id, OS.sel_copy_, sender != null ? sender.id : 0);
}

public void cut(id sender) {
	OS.objc_msgSend(this.id, OS.sel_cut_, sender != null ? sender.id : 0);
}

public id delegate() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public NSFont font() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_font);
	return result != 0 ? new NSFont(result) : null;
}

public boolean isFieldEditor() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isFieldEditor);
}

public void paste(id sender) {
	OS.objc_msgSend(this.id, OS.sel_paste_, sender != null ? sender.id : 0);
}

public void replaceCharactersInRange(NSRange range, NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_replaceCharactersInRange_withString_, range, aString != null ? aString.id : 0);
}

public void scrollRangeToVisible(NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_scrollRangeToVisible_, range);
}

public void selectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectAll_, sender != null ? sender.id : 0);
}

public NSRange selectedRange() {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_selectedRange);
	return result;
}

public void setAlignment(long /*int*/ mode) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_, mode);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, color != null ? color.id : 0);
}

public void setBaseWritingDirection(long /*int*/ writingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setBaseWritingDirection_, writingDirection);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, anObject != null ? anObject.id : 0);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_, flag);
}

public void setEditable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_, flag);
}

public void setFont(NSFont obj) {
	OS.objc_msgSend(this.id, OS.sel_setFont_, obj != null ? obj.id : 0);
}

public void setHorizontallyResizable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontallyResizable_, flag);
}

public void setMaxSize(NSSize newMaxSize) {
	OS.objc_msgSend(this.id, OS.sel_setMaxSize_, newMaxSize);
}

public void setMinSize(NSSize newMinSize) {
	OS.objc_msgSend(this.id, OS.sel_setMinSize_, newMinSize);
}

public void setSelectable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectable_, flag);
}

public void setSelectedRange(NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedRange_, range);
}

public void setString(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setString_, string != null ? string.id : 0);
}

public void setTextColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setTextColor_, color != null ? color.id : 0);
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public NSString string() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_string);
	return result != 0 ? new NSString(result) : null;
}

}

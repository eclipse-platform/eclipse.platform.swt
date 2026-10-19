/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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

public class NSText extends NSView {

public NSText() {
	super();
}

public NSText(long id) {
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

public void setAlignment(long alignment) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_, alignment);
}

public void setBackgroundColor(NSColor backgroundColor) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, backgroundColor != null ? backgroundColor.id : 0);
}

public void setBaseWritingDirection(long baseWritingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setBaseWritingDirection_, baseWritingDirection);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, delegate != null ? delegate.id : 0);
}

public void setDrawsBackground(boolean drawsBackground) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_, drawsBackground);
}

public void setEditable(boolean editable) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_, editable);
}

public void setFont(NSFont font) {
	OS.objc_msgSend(this.id, OS.sel_setFont_, font != null ? font.id : 0);
}

public void setHorizontallyResizable(boolean horizontallyResizable) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontallyResizable_, horizontallyResizable);
}

public void setMaxSize(NSSize maxSize) {
	OS.objc_msgSend(this.id, OS.sel_setMaxSize_, maxSize);
}

public void setMinSize(NSSize minSize) {
	OS.objc_msgSend(this.id, OS.sel_setMinSize_, minSize);
}

public void setSelectable(boolean selectable) {
	OS.objc_msgSend(this.id, OS.sel_setSelectable_, selectable);
}

public void setSelectedRange(NSRange selectedRange) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedRange_, selectedRange);
}

public void setString(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setString_, string != null ? string.id : 0);
}

public void setTextColor(NSColor textColor) {
	OS.objc_msgSend(this.id, OS.sel_setTextColor_, textColor != null ? textColor.id : 0);
}

public NSString string() {
	long result = OS.objc_msgSend(this.id, OS.sel_string);
	return result != 0 ? new NSString(result) : null;
}

}

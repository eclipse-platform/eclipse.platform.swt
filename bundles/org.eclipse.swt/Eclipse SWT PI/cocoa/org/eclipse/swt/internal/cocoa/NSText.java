/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSText extends NSView {

public NSText() {
	super();
}

public NSText(int id) {
	super(id);
}

public static int static_class() {
	return OS.objc_msgSend(OS.class_NSText, OS.sel_class);
}

public NSData RTFDFromRange(NSRange range) {
	int result = OS.objc_msgSend(this.id, OS.sel_RTFDFromRange_1, range);
	return result != 0 ? new NSData(result) : null;
}

public NSData RTFFromRange(NSRange range) {
	int result = OS.objc_msgSend(this.id, OS.sel_RTFFromRange_1, range);
	return result != 0 ? new NSData(result) : null;
}

public void alignCenter(id sender) {
	OS.objc_msgSend(this.id, OS.sel_alignCenter_1, sender != null ? sender.id : 0);
}

public void alignLeft(id sender) {
	OS.objc_msgSend(this.id, OS.sel_alignLeft_1, sender != null ? sender.id : 0);
}

public void alignRight(id sender) {
	OS.objc_msgSend(this.id, OS.sel_alignRight_1, sender != null ? sender.id : 0);
}

public int alignment() {
	return OS.objc_msgSend(this.id, OS.sel_alignment);
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public int baseWritingDirection() {
	return OS.objc_msgSend(this.id, OS.sel_baseWritingDirection);
}

public void changeFont(id sender) {
	OS.objc_msgSend(this.id, OS.sel_changeFont_1, sender != null ? sender.id : 0);
}

public void checkSpelling(id sender) {
	OS.objc_msgSend(this.id, OS.sel_checkSpelling_1, sender != null ? sender.id : 0);
}

public void copy(id sender) {
	OS.objc_msgSend(this.id, OS.sel_copy_1, sender != null ? sender.id : 0);
}

public void copyFont(id sender) {
	OS.objc_msgSend(this.id, OS.sel_copyFont_1, sender != null ? sender.id : 0);
}

public void copyRuler(id sender) {
	OS.objc_msgSend(this.id, OS.sel_copyRuler_1, sender != null ? sender.id : 0);
}

public void cut(id sender) {
	OS.objc_msgSend(this.id, OS.sel_cut_1, sender != null ? sender.id : 0);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void delete(id sender) {
	OS.objc_msgSend(this.id, OS.sel_delete_1, sender != null ? sender.id : 0);
}

public boolean drawsBackground() {
	return OS.objc_msgSend(this.id, OS.sel_drawsBackground) != 0;
}

public NSFont font() {
	int result = OS.objc_msgSend(this.id, OS.sel_font);
	return result != 0 ? new NSFont(result) : null;
}

public boolean importsGraphics() {
	return OS.objc_msgSend(this.id, OS.sel_importsGraphics) != 0;
}

public boolean isEditable() {
	return OS.objc_msgSend(this.id, OS.sel_isEditable) != 0;
}

public boolean isFieldEditor() {
	return OS.objc_msgSend(this.id, OS.sel_isFieldEditor) != 0;
}

public boolean isHorizontallyResizable() {
	return OS.objc_msgSend(this.id, OS.sel_isHorizontallyResizable) != 0;
}

public boolean isRichText() {
	return OS.objc_msgSend(this.id, OS.sel_isRichText) != 0;
}

public boolean isRulerVisible() {
	return OS.objc_msgSend(this.id, OS.sel_isRulerVisible) != 0;
}

public boolean isSelectable() {
	return OS.objc_msgSend(this.id, OS.sel_isSelectable) != 0;
}

public boolean isVerticallyResizable() {
	return OS.objc_msgSend(this.id, OS.sel_isVerticallyResizable) != 0;
}

public NSSize maxSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_maxSize);
	return result;
}

public NSSize minSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_minSize);
	return result;
}

public void paste(id sender) {
	OS.objc_msgSend(this.id, OS.sel_paste_1, sender != null ? sender.id : 0);
}

public void pasteFont(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pasteFont_1, sender != null ? sender.id : 0);
}

public void pasteRuler(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pasteRuler_1, sender != null ? sender.id : 0);
}

public boolean readRTFDFromFile(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_readRTFDFromFile_1, path != null ? path.id : 0) != 0;
}

public void replaceCharactersInRange_withRTF_(NSRange range, NSData rtfData) {
	OS.objc_msgSend(this.id, OS.sel_replaceCharactersInRange_1withRTF_1, range, rtfData != null ? rtfData.id : 0);
}

public void replaceCharactersInRange_withRTFD_(NSRange range, NSData rtfdData) {
	OS.objc_msgSend(this.id, OS.sel_replaceCharactersInRange_1withRTFD_1, range, rtfdData != null ? rtfdData.id : 0);
}

public void replaceCharactersInRange_withString_(NSRange range, NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_replaceCharactersInRange_1withString_1, range, aString != null ? aString.id : 0);
}

public void scrollRangeToVisible(NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_scrollRangeToVisible_1, range);
}

public void selectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectAll_1, sender != null ? sender.id : 0);
}

public NSRange selectedRange() {
	NSRange result = new NSRange();
	OS.objc_msgSend_struct(result, this.id, OS.sel_selectedRange);
	return result;
}

public void setAlignment(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_1, mode);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setBaseWritingDirection(int writingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setBaseWritingDirection_1, writingDirection);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_1, flag);
}

public void setEditable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_1, flag);
}

public void setFieldEditor(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setFieldEditor_1, flag);
}

public void setFont_(NSFont obj) {
	OS.objc_msgSend(this.id, OS.sel_setFont_1, obj != null ? obj.id : 0);
}

public void setFont_range_(NSFont font, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_setFont_1range_1, font != null ? font.id : 0, range);
}

public void setHorizontallyResizable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontallyResizable_1, flag);
}

public void setImportsGraphics(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setImportsGraphics_1, flag);
}

public void setMaxSize(NSSize newMaxSize) {
	OS.objc_msgSend(this.id, OS.sel_setMaxSize_1, newMaxSize);
}

public void setMinSize(NSSize newMinSize) {
	OS.objc_msgSend(this.id, OS.sel_setMinSize_1, newMinSize);
}

public void setRichText(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setRichText_1, flag);
}

public void setSelectable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectable_1, flag);
}

public void setSelectedRange(NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedRange_1, range);
}

public void setString(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setString_1, string != null ? string.id : 0);
}

public void setTextColor_(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setTextColor_1, color != null ? color.id : 0);
}

public void setTextColor_range_(NSColor color, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_setTextColor_1range_1, color != null ? color.id : 0, range);
}

public void setUsesFontPanel(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setUsesFontPanel_1, flag);
}

public void setVerticallyResizable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setVerticallyResizable_1, flag);
}

public void showGuessPanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_showGuessPanel_1, sender != null ? sender.id : 0);
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public NSString string() {
	int result = OS.objc_msgSend(this.id, OS.sel_string);
	return result != 0 ? new NSString(result) : null;
}

public void subscript(id sender) {
	OS.objc_msgSend(this.id, OS.sel_subscript_1, sender != null ? sender.id : 0);
}

public void superscript(id sender) {
	OS.objc_msgSend(this.id, OS.sel_superscript_1, sender != null ? sender.id : 0);
}

public NSColor textColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_textColor);
	return result != 0 ? new NSColor(result) : null;
}

public void toggleRuler(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleRuler_1, sender != null ? sender.id : 0);
}

public void underline(id sender) {
	OS.objc_msgSend(this.id, OS.sel_underline_1, sender != null ? sender.id : 0);
}

public void unscript(id sender) {
	OS.objc_msgSend(this.id, OS.sel_unscript_1, sender != null ? sender.id : 0);
}

public boolean usesFontPanel() {
	return OS.objc_msgSend(this.id, OS.sel_usesFontPanel) != 0;
}

public boolean writeRTFDToFile(NSString path, boolean flag) {
	return OS.objc_msgSend(this.id, OS.sel_writeRTFDToFile_1atomically_1, path != null ? path.id : 0, flag) != 0;
}

}

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

public class NSTextStorage extends NSMutableAttributedString {

public NSTextStorage() {
	super();
}

public NSTextStorage(int id) {
	super(id);
}

public void addLayoutManager(NSLayoutManager obj) {
	OS.objc_msgSend(this.id, OS.sel_addLayoutManager_1, obj != null ? obj.id : 0);
}

public NSArray attributeRuns() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributeRuns);
	return result != 0 ? new NSArray(result) : null;
}

public int changeInLength() {
	return OS.objc_msgSend(this.id, OS.sel_changeInLength);
}

public NSArray characters() {
	int result = OS.objc_msgSend(this.id, OS.sel_characters);
	return result != 0 ? new NSArray(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void edited(int editedMask, NSRange range, int delta) {
	OS.objc_msgSend(this.id, OS.sel_edited_1range_1changeInLength_1, editedMask, range, delta);
}

public int editedMask() {
	return OS.objc_msgSend(this.id, OS.sel_editedMask);
}

public NSRange editedRange() {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_editedRange);
	return result;
}

public void ensureAttributesAreFixedInRange(NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_ensureAttributesAreFixedInRange_1, range);
}

public boolean fixesAttributesLazily() {
	return OS.objc_msgSend(this.id, OS.sel_fixesAttributesLazily) != 0;
}

public NSFont font() {
	int result = OS.objc_msgSend(this.id, OS.sel_font);
	return result != 0 ? new NSFont(result) : null;
}

public NSColor foregroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_foregroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public void invalidateAttributesInRange(NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_invalidateAttributesInRange_1, range);
}

public NSArray layoutManagers() {
	int result = OS.objc_msgSend(this.id, OS.sel_layoutManagers);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray paragraphs() {
	int result = OS.objc_msgSend(this.id, OS.sel_paragraphs);
	return result != 0 ? new NSArray(result) : null;
}

public void processEditing() {
	OS.objc_msgSend(this.id, OS.sel_processEditing);
}

public void removeLayoutManager(NSLayoutManager obj) {
	OS.objc_msgSend(this.id, OS.sel_removeLayoutManager_1, obj != null ? obj.id : 0);
}

public void setAttributeRuns(NSArray attributeRuns) {
	OS.objc_msgSend(this.id, OS.sel_setAttributeRuns_1, attributeRuns != null ? attributeRuns.id : 0);
}

public void setCharacters(NSArray characters) {
	OS.objc_msgSend(this.id, OS.sel_setCharacters_1, characters != null ? characters.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setFont(NSFont font) {
	OS.objc_msgSend(this.id, OS.sel_setFont_1, font != null ? font.id : 0);
}

public void setForegroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setForegroundColor_1, color != null ? color.id : 0);
}

public void setParagraphs(NSArray paragraphs) {
	OS.objc_msgSend(this.id, OS.sel_setParagraphs_1, paragraphs != null ? paragraphs.id : 0);
}

public void setWords(NSArray words) {
	OS.objc_msgSend(this.id, OS.sel_setWords_1, words != null ? words.id : 0);
}

public NSArray words() {
	int result = OS.objc_msgSend(this.id, OS.sel_words);
	return result != 0 ? new NSArray(result) : null;
}

}

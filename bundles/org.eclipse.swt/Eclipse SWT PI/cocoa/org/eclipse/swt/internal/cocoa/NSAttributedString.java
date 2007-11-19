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

public class NSAttributedString extends NSObject {

public NSAttributedString() {
	super();
}

public NSAttributedString(int id) {
	super(id);
}

public id attribute_atIndex_effectiveRange_(NSString attrName, int location, int range) {
	int result = OS.objc_msgSend(this.id, OS.sel_attribute_1atIndex_1effectiveRange_1, attrName != null ? attrName.id : 0, location, range);
	return result != 0 ? new id(result) : null;
}

public id attribute_atIndex_longestEffectiveRange_inRange_(NSString attrName, int location, int range, NSRange rangeLimit) {
	int result = OS.objc_msgSend(this.id, OS.sel_attribute_1atIndex_1longestEffectiveRange_1inRange_1, attrName != null ? attrName.id : 0, location, range, rangeLimit);
	return result != 0 ? new id(result) : null;
}

public NSAttributedString attributedSubstringFromRange(NSRange range) {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedSubstringFromRange_1, range);
	return result == this.id ? this : (result != 0 ? new NSAttributedString(result) : null);
}

public NSDictionary attributesAtIndex_effectiveRange_(int location, int range) {
	int result = OS.objc_msgSend(this.id, OS.sel_attributesAtIndex_1effectiveRange_1, location, range);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary attributesAtIndex_longestEffectiveRange_inRange_(int location, int range, NSRange rangeLimit) {
	int result = OS.objc_msgSend(this.id, OS.sel_attributesAtIndex_1longestEffectiveRange_1inRange_1, location, range, rangeLimit);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSAttributedString initWithAttributedString(NSAttributedString attrStr) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithAttributedString_1, attrStr != null ? attrStr.id : 0);
	return result != 0 ? this : null;
}

public NSAttributedString initWithString_(NSString str) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithString_1, str != null ? str.id : 0);
	return result != 0 ? this : null;
}

public NSAttributedString initWithString_attributes_(NSString str, NSDictionary attrs) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithString_1attributes_1, str != null ? str.id : 0, attrs != null ? attrs.id : 0);
	return result != 0 ? this : null;
}

public boolean isEqualToAttributedString(NSAttributedString other) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToAttributedString_1, other != null ? other.id : 0) != 0;
}

public int length() {
	return OS.objc_msgSend(this.id, OS.sel_length);
}

public NSString string() {
	int result = OS.objc_msgSend(this.id, OS.sel_string);
	return result != 0 ? new NSString(result) : null;
}


public NSSize size() {
	NSSize result = new NSSize();
	OS.objc_msgSend_struct(result, this.id, OS.sel_size);
	return result;
}

public void drawAtPoint(NSPoint pt) {
	OS.objc_msgSend(id, OS.sel_drawAtPoint_, pt);
}

public void drawInRect(NSRect rect) {
	OS.objc_msgSend(id, OS.sel_drawInRect_1, rect);
}

public void drawInRect(NSRect rect, int options) {
	OS.objc_msgSend(id, OS.sel_drawInRect_1, rect, options);
}

public int nextWordFromIndex(int index, boolean forward) {
	return OS.objc_msgSend(id, OS.sel_nextWordFromIndex_1forward_1, index, forward);
}

public NSRange doubleClickAtIndex(int index) {
	NSRange result = new NSRange();
	OS.objc_msgSend_struct(result, id, OS.sel_doubleClickAtIndex_1, index);
	return result;
}

}

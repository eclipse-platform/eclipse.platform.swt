/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSAttributedString extends NSObject {

public NSAttributedString() {
	super();
}

public NSAttributedString(long /*int*/ id) {
	super(id);
}

public NSAttributedString(id id) {
	super(id);
}

public static NSAttributedString attributedStringWithAttachment(NSTextAttachment attachment) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSAttributedString, OS.sel_attributedStringWithAttachment_, attachment != null ? attachment.id : 0);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSRect boundingRectWithSize(NSSize size, long /*int*/ options) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundingRectWithSize_options_, size, options);
	return result;
}

public NSRange doubleClickAtIndex(long /*int*/ location) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_doubleClickAtIndex_, location);
	return result;
}

public void drawAtPoint(NSPoint point) {
	OS.objc_msgSend(this.id, OS.sel_drawAtPoint_, point);
}

public void drawInRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_drawInRect_, rect);
}

public long /*int*/ nextWordFromIndex(long /*int*/ location, boolean isForward) {
	return OS.objc_msgSend(this.id, OS.sel_nextWordFromIndex_forward_, location, isForward);
}

public NSSize size() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_size);
	return result;
}

public id attribute(NSString attrName, long /*int*/ location, long /*int*/ range) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_attribute_atIndex_effectiveRange_, attrName != null ? attrName.id : 0, location, range);
	return result != 0 ? new id(result) : null;
}

public NSAttributedString attributedSubstringFromRange(NSRange range) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_attributedSubstringFromRange_, range);
	return result == this.id ? this : (result != 0 ? new NSAttributedString(result) : null);
}

public NSDictionary attributesAtIndex(long /*int*/ location, long /*int*/ range, NSRange rangeLimit) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_attributesAtIndex_longestEffectiveRange_inRange_, location, range, rangeLimit);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSAttributedString initWithString(NSString str) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithString_, str != null ? str.id : 0);
	return result == this.id ? this : (result != 0 ? new NSAttributedString(result) : null);
}

public NSAttributedString initWithString(NSString str, NSDictionary attrs) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithString_attributes_, str != null ? str.id : 0, attrs != null ? attrs.id : 0);
	return result == this.id ? this : (result != 0 ? new NSAttributedString(result) : null);
}

public long /*int*/ length() {
	return OS.objc_msgSend(this.id, OS.sel_length);
}

public NSString string() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_string);
	return result != 0 ? new NSString(result) : null;
}

}

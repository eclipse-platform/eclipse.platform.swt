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

public class NSAttributedString extends NSObject {

public NSAttributedString() {
	super();
}

public NSAttributedString(long id) {
	super(id);
}

public NSAttributedString(id id) {
	super(id);
}

public NSRect boundingRectWithSize(NSSize size, long options) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundingRectWithSize_options_, size, options);
	return result;
}

public void drawInRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_drawInRect_, rect);
}

public long nextWordFromIndex(long location, boolean isForward) {
	return OS.objc_msgSend(this.id, OS.sel_nextWordFromIndex_forward_, location, isForward);
}

public NSSize size() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_size);
	return result;
}

public id attribute(NSString attrName, long location, long range) {
	long result = OS.objc_msgSend(this.id, OS.sel_attribute_atIndex_effectiveRange_, attrName != null ? attrName.id : 0, location, range);
	return result != 0 ? new id(result) : null;
}

public NSAttributedString attributedSubstringFromRange(NSRange range) {
	long result = OS.objc_msgSend(this.id, OS.sel_attributedSubstringFromRange_, range);
	return result == this.id ? this : (result != 0 ? new NSAttributedString(result) : null);
}

public NSDictionary attributesAtIndex(long location, long range, NSRange rangeLimit) {
	long result = OS.objc_msgSend(this.id, OS.sel_attributesAtIndex_longestEffectiveRange_inRange_, location, range, rangeLimit);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSAttributedString initWithString(NSString str) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithString_, str != null ? str.id : 0);
	return result == this.id ? this : (result != 0 ? new NSAttributedString(result) : null);
}

public NSAttributedString initWithString(NSString str, NSDictionary attrs) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithString_attributes_, str != null ? str.id : 0, attrs != null ? attrs.id : 0);
	return result == this.id ? this : (result != 0 ? new NSAttributedString(result) : null);
}

public long length() {
	return OS.objc_msgSend(this.id, OS.sel_length);
}

public NSString string() {
	long result = OS.objc_msgSend(this.id, OS.sel_string);
	return result != 0 ? new NSString(result) : null;
}

}

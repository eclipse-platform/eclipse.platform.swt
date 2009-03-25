/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSMutableAttributedString extends NSAttributedString {

public NSMutableAttributedString() {
	super();
}

public NSMutableAttributedString(int /*long*/ id) {
	super(id);
}

public NSMutableAttributedString(id id) {
	super(id);
}

public void appendAttributedString(NSAttributedString attrString) {
	OS.objc_msgSend(this.id, OS.sel_appendAttributedString_, attrString != null ? attrString.id : 0);
}

public NSMutableString mutableString() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_mutableString);
	return result != 0 ? new NSMutableString(result) : null;
}

public void replaceCharactersInRange(NSRange range, NSString str) {
	OS.objc_msgSend(this.id, OS.sel_replaceCharactersInRange_withString_, range, str != null ? str.id : 0);
}

public void addAttribute(NSString name, id value, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_addAttribute_value_range_, name != null ? name.id : 0, value != null ? value.id : 0, range);
}

public void beginEditing() {
	OS.objc_msgSend(this.id, OS.sel_beginEditing);
}

public void endEditing() {
	OS.objc_msgSend(this.id, OS.sel_endEditing);
}

public void removeAttribute(NSString name, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_removeAttribute_range_, name != null ? name.id : 0, range);
}

public void setAttributedString(NSAttributedString attrString) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedString_, attrString != null ? attrString.id : 0);
}

public static NSAttributedString attributedStringWithAttachment(NSTextAttachment attachment) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSMutableAttributedString, OS.sel_attributedStringWithAttachment_, attachment != null ? attachment.id : 0);
	return result != 0 ? new NSAttributedString(result) : null;
}

}

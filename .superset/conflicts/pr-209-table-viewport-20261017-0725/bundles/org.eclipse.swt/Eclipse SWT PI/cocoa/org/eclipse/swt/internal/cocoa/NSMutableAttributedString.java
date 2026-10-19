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

public class NSMutableAttributedString extends NSAttributedString {

public NSMutableAttributedString() {
	super();
}

public NSMutableAttributedString(long id) {
	super(id);
}

public NSMutableAttributedString(id id) {
	super(id);
}

public void addAttribute(NSString name, id value, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_addAttribute_value_range_, name != null ? name.id : 0, value != null ? value.id : 0, range);
}

public void appendAttributedString(NSAttributedString attrString) {
	OS.objc_msgSend(this.id, OS.sel_appendAttributedString_, attrString != null ? attrString.id : 0);
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

public void replaceCharactersInRange(NSRange range, NSString str) {
	OS.objc_msgSend(this.id, OS.sel_replaceCharactersInRange_withString_, range, str != null ? str.id : 0);
}

public void setAttributedString(NSAttributedString attrString) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedString_, attrString != null ? attrString.id : 0);
}

}

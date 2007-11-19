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

public class NSFormCell extends NSActionCell {

public NSFormCell() {
	super();
}

public NSFormCell(int id) {
	super(id);
}

public NSAttributedString attributedTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedTitle);
	return result != 0 ? new NSAttributedString(result) : null;
}

public boolean isOpaque() {
	return OS.objc_msgSend(this.id, OS.sel_isOpaque) != 0;
}

public NSAttributedString placeholderAttributedString() {
	int result = OS.objc_msgSend(this.id, OS.sel_placeholderAttributedString);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSString placeholderString() {
	int result = OS.objc_msgSend(this.id, OS.sel_placeholderString);
	return result != 0 ? new NSString(result) : null;
}

public void setAttributedTitle(NSAttributedString obj) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedTitle_1, obj != null ? obj.id : 0);
}

public void setPlaceholderAttributedString(NSAttributedString string) {
	OS.objc_msgSend(this.id, OS.sel_setPlaceholderAttributedString_1, string != null ? string.id : 0);
}

public void setPlaceholderString(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setPlaceholderString_1, string != null ? string.id : 0);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1, aString != null ? aString.id : 0);
}

public void setTitleAlignment(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setTitleAlignment_1, mode);
}

public void setTitleBaseWritingDirection(int writingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setTitleBaseWritingDirection_1, writingDirection);
}

public void setTitleFont(NSFont fontObj) {
	OS.objc_msgSend(this.id, OS.sel_setTitleFont_1, fontObj != null ? fontObj.id : 0);
}

public void setTitleWidth(float width) {
	OS.objc_msgSend(this.id, OS.sel_setTitleWidth_1, width);
}

public void setTitleWithMnemonic(NSString stringWithAmpersand) {
	OS.objc_msgSend(this.id, OS.sel_setTitleWithMnemonic_1, stringWithAmpersand != null ? stringWithAmpersand.id : 0);
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public int titleAlignment() {
	return OS.objc_msgSend(this.id, OS.sel_titleAlignment);
}

public int titleBaseWritingDirection() {
	return OS.objc_msgSend(this.id, OS.sel_titleBaseWritingDirection);
}

public NSFont titleFont() {
	int result = OS.objc_msgSend(this.id, OS.sel_titleFont);
	return result != 0 ? new NSFont(result) : null;
}

public float titleWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_titleWidth);
}

public float titleWidth_(NSSize aSize) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_titleWidth_1, aSize);
}

}

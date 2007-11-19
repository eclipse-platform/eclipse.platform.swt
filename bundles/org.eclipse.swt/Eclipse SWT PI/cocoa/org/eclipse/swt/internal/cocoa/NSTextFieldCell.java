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

public class NSTextFieldCell extends NSActionCell {

public NSTextFieldCell() {
	super();
}

public NSTextFieldCell(int id) {
	super(id);
}

public NSArray allowedInputSourceLocales() {
	int result = OS.objc_msgSend(this.id, OS.sel_allowedInputSourceLocales);
	return result != 0 ? new NSArray(result) : null;
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public int bezelStyle() {
	return OS.objc_msgSend(this.id, OS.sel_bezelStyle);
}

public boolean drawsBackground() {
	return OS.objc_msgSend(this.id, OS.sel_drawsBackground) != 0;
}

public NSAttributedString placeholderAttributedString() {
	int result = OS.objc_msgSend(this.id, OS.sel_placeholderAttributedString);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSString placeholderString() {
	int result = OS.objc_msgSend(this.id, OS.sel_placeholderString);
	return result != 0 ? new NSString(result) : null;
}

public void setAllowedInputSourceLocales(NSArray localeIdentifiers) {
	OS.objc_msgSend(this.id, OS.sel_setAllowedInputSourceLocales_1, localeIdentifiers != null ? localeIdentifiers.id : 0);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setBezelStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setBezelStyle_1, style);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_1, flag);
}

public void setPlaceholderAttributedString(NSAttributedString string) {
	OS.objc_msgSend(this.id, OS.sel_setPlaceholderAttributedString_1, string != null ? string.id : 0);
}

public void setPlaceholderString(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setPlaceholderString_1, string != null ? string.id : 0);
}

public void setTextColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setTextColor_1, color != null ? color.id : 0);
}

public NSText setUpFieldEditorAttributes(NSText textObj) {
	int result = OS.objc_msgSend(this.id, OS.sel_setUpFieldEditorAttributes_1, textObj != null ? textObj.id : 0);
	return result != 0 ? new NSText(result) : null;
}

public void setWantsNotificationForMarkedText(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setWantsNotificationForMarkedText_1, flag);
}

public NSColor textColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_textColor);
	return result != 0 ? new NSColor(result) : null;
}

}

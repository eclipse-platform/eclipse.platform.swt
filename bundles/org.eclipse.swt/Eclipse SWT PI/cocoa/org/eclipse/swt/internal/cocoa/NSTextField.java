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

public class NSTextField extends NSControl {

public NSTextField() {
	super();
}

public NSTextField(int id) {
	super(id);
}

public boolean acceptsFirstResponder() {
	return OS.objc_msgSend(this.id, OS.sel_acceptsFirstResponder) != 0;
}

public boolean allowsEditingTextAttributes() {
	return OS.objc_msgSend(this.id, OS.sel_allowsEditingTextAttributes) != 0;
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public int bezelStyle() {
	return OS.objc_msgSend(this.id, OS.sel_bezelStyle);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public boolean drawsBackground() {
	return OS.objc_msgSend(this.id, OS.sel_drawsBackground) != 0;
}

public boolean importsGraphics() {
	return OS.objc_msgSend(this.id, OS.sel_importsGraphics) != 0;
}

public boolean isBezeled() {
	return OS.objc_msgSend(this.id, OS.sel_isBezeled) != 0;
}

public boolean isBordered() {
	return OS.objc_msgSend(this.id, OS.sel_isBordered) != 0;
}

public boolean isEditable() {
	return OS.objc_msgSend(this.id, OS.sel_isEditable) != 0;
}

public boolean isSelectable() {
	return OS.objc_msgSend(this.id, OS.sel_isSelectable) != 0;
}

public void selectText(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectText_1, sender != null ? sender.id : 0);
}

public void setAllowsEditingTextAttributes(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsEditingTextAttributes_1, flag);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setBezelStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setBezelStyle_1, style);
}

public void setBezeled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBezeled_1, flag);
}

public void setBordered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBordered_1, flag);
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

public void setImportsGraphics(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setImportsGraphics_1, flag);
}

public void setSelectable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectable_1, flag);
}

public void setTextColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setTextColor_1, color != null ? color.id : 0);
}

public void setTitleWithMnemonic(NSString stringWithAmpersand) {
	OS.objc_msgSend(this.id, OS.sel_setTitleWithMnemonic_1, stringWithAmpersand != null ? stringWithAmpersand.id : 0);
}

public NSColor textColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_textColor);
	return result != 0 ? new NSColor(result) : null;
}

public void textDidBeginEditing(NSNotification notification) {
	OS.objc_msgSend(this.id, OS.sel_textDidBeginEditing_1, notification != null ? notification.id : 0);
}

public void textDidChange(NSNotification notification) {
	OS.objc_msgSend(this.id, OS.sel_textDidChange_1, notification != null ? notification.id : 0);
}

public void textDidEndEditing(NSNotification notification) {
	OS.objc_msgSend(this.id, OS.sel_textDidEndEditing_1, notification != null ? notification.id : 0);
}

public boolean textShouldBeginEditing(NSText textObject) {
	return OS.objc_msgSend(this.id, OS.sel_textShouldBeginEditing_1, textObject != null ? textObject.id : 0) != 0;
}

public boolean textShouldEndEditing(NSText textObject) {
	return OS.objc_msgSend(this.id, OS.sel_textShouldEndEditing_1, textObject != null ? textObject.id : 0) != 0;
}

}

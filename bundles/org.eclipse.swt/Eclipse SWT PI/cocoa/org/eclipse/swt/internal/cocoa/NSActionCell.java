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

public class NSActionCell extends NSCell {

public NSActionCell() {
	super();
}

public NSActionCell(int id) {
	super(id);
}

public int action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public NSView controlView() {
	int result = OS.objc_msgSend(this.id, OS.sel_controlView);
	return result != 0 ? new NSView(result) : null;
}

public double doubleValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_doubleValue);
}

public float floatValue() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_floatValue);
}

public int intValue() {
	return OS.objc_msgSend(this.id, OS.sel_intValue);
}

public int integerValue() {
	return OS.objc_msgSend(this.id, OS.sel_integerValue);
}

public void setAction(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setAction_1, aSelector);
}

public void setAlignment(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_1, mode);
}

public void setBezeled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBezeled_1, flag);
}

public void setBordered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBordered_1, flag);
}

public void setControlView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setControlView_1, view != null ? view.id : 0);
}

public void setEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_1, flag);
}

public void setFloatingPointFormat(boolean autoRange, int leftDigits, int rightDigits) {
	OS.objc_msgSend(this.id, OS.sel_setFloatingPointFormat_1left_1right_1, autoRange, leftDigits, rightDigits);
}

public void setFont(NSFont fontObj) {
	OS.objc_msgSend(this.id, OS.sel_setFont_1, fontObj != null ? fontObj.id : 0);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1, image != null ? image.id : 0);
}

public void setObjectValue(id obj) {
	OS.objc_msgSend(this.id, OS.sel_setObjectValue_1, obj != null ? obj.id : 0);
}

public void setTag(int anInt) {
	OS.objc_msgSend(this.id, OS.sel_setTag_1, anInt);
}

public void setTarget(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_1, anObject != null ? anObject.id : 0);
}

public NSString stringValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringValue);
	return result != 0 ? new NSString(result) : null;
}

public int tag() {
	return OS.objc_msgSend(this.id, OS.sel_tag);
}

public id target() {
	int result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSControl extends NSView {

public NSControl() {
	super();
}

public NSControl(int /*long*/ id) {
	super(id);
}

public NSControl(id id) {
	super(id);
}

public boolean abortEditing() {
	return OS.objc_msgSend_bool(this.id, OS.sel_abortEditing);
}

public int /*long*/ action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public NSCell cell() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_cell);
	return result != 0 ? new NSCell(result) : null;
}

public static int /*long*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSControl, OS.sel_cellClass);
}

public NSText currentEditor() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_currentEditor);
	return result != 0 ? new NSText(result) : null;
}

public double doubleValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_doubleValue);
}

public NSFont font() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_font);
	return result != 0 ? new NSFont(result) : null;
}

public boolean isEnabled() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isEnabled);
}

public boolean sendAction(int /*long*/ theAction, id theTarget) {
	return OS.objc_msgSend_bool(this.id, OS.sel_sendAction_to_, theAction, theTarget != null ? theTarget.id : 0);
}

public void setAction(int /*long*/ aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setAction_, aSelector);
}

public void setAlignment(int /*long*/ mode) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_, mode);
}

public void setBaseWritingDirection(int /*long*/ writingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setBaseWritingDirection_, writingDirection);
}

public void setCell(NSCell aCell) {
	OS.objc_msgSend(this.id, OS.sel_setCell_, aCell != null ? aCell.id : 0);
}

public static void setCellClass(int /*long*/ factoryId) {
	OS.objc_msgSend(OS.class_NSControl, OS.sel_setCellClass_, factoryId);
}

public void setDoubleValue(double aDouble) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleValue_, aDouble);
}

public void setEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_, flag);
}

public void setFont(NSFont fontObj) {
	OS.objc_msgSend(this.id, OS.sel_setFont_, fontObj != null ? fontObj.id : 0);
}

public void setFormatter(NSFormatter newFormatter) {
	OS.objc_msgSend(this.id, OS.sel_setFormatter_, newFormatter != null ? newFormatter.id : 0);
}

public void setStringValue(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setStringValue_, aString != null ? aString.id : 0);
}

public void setTarget(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_, anObject != null ? anObject.id : 0);
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public NSString stringValue() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringValue);
	return result != 0 ? new NSString(result) : null;
}

public id target() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

}

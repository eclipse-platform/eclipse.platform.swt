/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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

public class NSControl extends NSView {

public NSControl() {
	super();
}

public NSControl(long /*int*/ id) {
	super(id);
}

public NSControl(id id) {
	super(id);
}

public boolean abortEditing() {
	return OS.objc_msgSend_bool(this.id, OS.sel_abortEditing);
}

public long /*int*/ action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public NSCell cell() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_cell);
	return result != 0 ? new NSCell(result) : null;
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSControl, OS.sel_cellClass);
}

public NSText currentEditor() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_currentEditor);
	return result != 0 ? new NSText(result) : null;
}

public double doubleValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_doubleValue);
}

public NSFont font() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_font);
	return result != 0 ? new NSFont(result) : null;
}

public boolean isEnabled() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isEnabled);
}

public boolean isHighlighted() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isHighlighted);
}

public boolean sendAction(long /*int*/ theAction, id theTarget) {
	return OS.objc_msgSend_bool(this.id, OS.sel_sendAction_to_, theAction, theTarget != null ? theTarget.id : 0);
}

public void setAction(long /*int*/ action) {
	OS.objc_msgSend(this.id, OS.sel_setAction_, action);
}

public void setAlignment(long /*int*/ alignment) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_, alignment);
}

public void setBaseWritingDirection(long /*int*/ baseWritingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setBaseWritingDirection_, baseWritingDirection);
}

public void setCell(NSCell cell) {
	OS.objc_msgSend(this.id, OS.sel_setCell_, cell != null ? cell.id : 0);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSControl, OS.sel_setCellClass_, factoryId);
}

public void setDoubleValue(double doubleValue) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleValue_, doubleValue);
}

public void setEnabled(boolean enabled) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_, enabled);
}

public void setFont(NSFont font) {
	OS.objc_msgSend(this.id, OS.sel_setFont_, font != null ? font.id : 0);
}

public void setFormatter(NSFormatter formatter) {
	OS.objc_msgSend(this.id, OS.sel_setFormatter_, formatter != null ? formatter.id : 0);
}

public void setStringValue(NSString stringValue) {
	OS.objc_msgSend(this.id, OS.sel_setStringValue_, stringValue != null ? stringValue.id : 0);
}

public void setTarget(id target) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_, target != null ? target.id : 0);
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public NSString stringValue() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_stringValue);
	return result != 0 ? new NSString(result) : null;
}

public id target() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

}

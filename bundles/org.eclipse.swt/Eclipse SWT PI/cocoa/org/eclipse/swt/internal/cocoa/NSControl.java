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

public class NSControl extends NSView {

public NSControl() {
	super();
}

public NSControl(int id) {
	super(id);
}

public boolean abortEditing() {
	return OS.objc_msgSend(this.id, OS.sel_abortEditing) != 0;
}

public int action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public int alignment() {
	return OS.objc_msgSend(this.id, OS.sel_alignment);
}

public NSAttributedString attributedStringValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedStringValue);
	return result != 0 ? new NSAttributedString(result) : null;
}

public int baseWritingDirection() {
	return OS.objc_msgSend(this.id, OS.sel_baseWritingDirection);
}

public void calcSize() {
	OS.objc_msgSend(this.id, OS.sel_calcSize);
}

public int cell() {
	int result = OS.objc_msgSend(this.id, OS.sel_cell);
	return result != 0 ? result : 0;
}

public static int cellClass() {
	return OS.objc_msgSend(OS.class_NSControl, OS.sel_cellClass);
}

public NSText currentEditor() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentEditor);
	return result != 0 ? new NSText(result) : null;
}

public double doubleValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_doubleValue);
}

public void drawCell(NSCell aCell) {
	OS.objc_msgSend(this.id, OS.sel_drawCell_1, aCell != null ? aCell.id : 0);
}

public void drawCellInside(NSCell aCell) {
	OS.objc_msgSend(this.id, OS.sel_drawCellInside_1, aCell != null ? aCell.id : 0);
}

public float floatValue() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_floatValue);
}

public NSFont font() {
	int result = OS.objc_msgSend(this.id, OS.sel_font);
	return result != 0 ? new NSFont(result) : null;
}

public id formatter() {
	int result = OS.objc_msgSend(this.id, OS.sel_formatter);
	return result != 0 ? new id(result) : null;
}

public boolean ignoresMultiClick() {
	return OS.objc_msgSend(this.id, OS.sel_ignoresMultiClick) != 0;
}

public int intValue() {
	return OS.objc_msgSend(this.id, OS.sel_intValue);
}

public int integerValue() {
	return OS.objc_msgSend(this.id, OS.sel_integerValue);
}

public boolean isContinuous() {
	return OS.objc_msgSend(this.id, OS.sel_isContinuous) != 0;
}

public boolean isEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isEnabled) != 0;
}

public void mouseDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseDown_1, theEvent != null ? theEvent.id : 0);
}

public id objectValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectValue);
	return result != 0 ? new id(result) : null;
}

public void performClick(id sender) {
	OS.objc_msgSend(this.id, OS.sel_performClick_1, sender != null ? sender.id : 0);
}

public boolean refusesFirstResponder() {
	return OS.objc_msgSend(this.id, OS.sel_refusesFirstResponder) != 0;
}

public void selectCell(NSCell aCell) {
	OS.objc_msgSend(this.id, OS.sel_selectCell_1, aCell != null ? aCell.id : 0);
}

public id selectedCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedCell);
	return result != 0 ? new id(result) : null;
}

public int selectedTag() {
	return OS.objc_msgSend(this.id, OS.sel_selectedTag);
}

public boolean sendAction(int theAction, id theTarget) {
	return OS.objc_msgSend(this.id, OS.sel_sendAction_1to_1, theAction, theTarget != null ? theTarget.id : 0) != 0;
}

public int sendActionOn(int mask) {
	return OS.objc_msgSend(this.id, OS.sel_sendActionOn_1, mask);
}

public void setAction(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setAction_1, aSelector);
}

public void setAlignment(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_1, mode);
}

public void setAttributedStringValue(NSAttributedString obj) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedStringValue_1, obj != null ? obj.id : 0);
}

public void setBaseWritingDirection(int writingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setBaseWritingDirection_1, writingDirection);
}

public void setCell(NSCell aCell) {
	OS.objc_msgSend(this.id, OS.sel_setCell_1, aCell != null ? aCell.id : 0);
}

public static void setCellClass(int factoryId) {
	OS.objc_msgSend(OS.class_NSControl, OS.sel_setCellClass_1, factoryId);
}

public void setContinuous(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setContinuous_1, flag);
}

public void setDoubleValue(double aDouble) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleValue_1, aDouble);
}

public void setEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_1, flag);
}

public void setFloatValue(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setFloatValue_1, aFloat);
}

public void setFloatingPointFormat(boolean autoRange, int leftDigits, int rightDigits) {
	OS.objc_msgSend(this.id, OS.sel_setFloatingPointFormat_1left_1right_1, autoRange, leftDigits, rightDigits);
}

public void setFont(NSFont fontObj) {
	OS.objc_msgSend(this.id, OS.sel_setFont_1, fontObj != null ? fontObj.id : 0);
}

public void setFormatter(NSFormatter newFormatter) {
	OS.objc_msgSend(this.id, OS.sel_setFormatter_1, newFormatter != null ? newFormatter.id : 0);
}

public void setIgnoresMultiClick(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setIgnoresMultiClick_1, flag);
}

public void setIntValue(int anInt) {
	OS.objc_msgSend(this.id, OS.sel_setIntValue_1, anInt);
}

public void setIntegerValue(int anInteger) {
	OS.objc_msgSend(this.id, OS.sel_setIntegerValue_1, anInteger);
}

public void setNeedsDisplay() {
	OS.objc_msgSend(this.id, OS.sel_setNeedsDisplay);
}

public void setObjectValue(id obj) {
	OS.objc_msgSend(this.id, OS.sel_setObjectValue_1, obj != null ? obj.id : 0);
}

public void setRefusesFirstResponder(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setRefusesFirstResponder_1, flag);
}

public void setStringValue(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setStringValue_1, aString != null ? aString.id : 0);
}

public void setTag(int anInt) {
	OS.objc_msgSend(this.id, OS.sel_setTag_1, anInt);
}

public void setTarget(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_1, anObject != null ? anObject.id : 0);
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public NSString stringValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringValue);
	return result != 0 ? new NSString(result) : null;
}

public int tag() {
	return OS.objc_msgSend(this.id, OS.sel_tag);
}

public void takeDoubleValueFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeDoubleValueFrom_1, sender != null ? sender.id : 0);
}

public void takeFloatValueFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeFloatValueFrom_1, sender != null ? sender.id : 0);
}

public void takeIntValueFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeIntValueFrom_1, sender != null ? sender.id : 0);
}

public void takeIntegerValueFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeIntegerValueFrom_1, sender != null ? sender.id : 0);
}

public void takeObjectValueFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeObjectValueFrom_1, sender != null ? sender.id : 0);
}

public void takeStringValueFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeStringValueFrom_1, sender != null ? sender.id : 0);
}

public id target() {
	int result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

public void updateCell(NSCell aCell) {
	OS.objc_msgSend(this.id, OS.sel_updateCell_1, aCell != null ? aCell.id : 0);
}

public void updateCellInside(NSCell aCell) {
	OS.objc_msgSend(this.id, OS.sel_updateCellInside_1, aCell != null ? aCell.id : 0);
}

public void validateEditing() {
	OS.objc_msgSend(this.id, OS.sel_validateEditing);
}

}

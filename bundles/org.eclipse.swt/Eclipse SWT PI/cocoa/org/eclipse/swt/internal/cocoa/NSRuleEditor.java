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

public class NSRuleEditor extends NSControl {

public NSRuleEditor() {
	super();
}

public NSRuleEditor(int id) {
	super(id);
}

public void addRow(id sender) {
	OS.objc_msgSend(this.id, OS.sel_addRow_1, sender != null ? sender.id : 0);
}

public boolean canRemoveAllRows() {
	return OS.objc_msgSend(this.id, OS.sel_canRemoveAllRows) != 0;
}

public NSArray criteriaForRow(int row) {
	int result = OS.objc_msgSend(this.id, OS.sel_criteriaForRow_1, row);
	return result != 0 ? new NSArray(result) : null;
}

public NSString criteriaKeyPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_criteriaKeyPath);
	return result != 0 ? new NSString(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public NSArray displayValuesForRow(int row) {
	int result = OS.objc_msgSend(this.id, OS.sel_displayValuesForRow_1, row);
	return result != 0 ? new NSArray(result) : null;
}

public NSString displayValuesKeyPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_displayValuesKeyPath);
	return result != 0 ? new NSString(result) : null;
}

public NSDictionary formattingDictionary() {
	int result = OS.objc_msgSend(this.id, OS.sel_formattingDictionary);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSString formattingStringsFilename() {
	int result = OS.objc_msgSend(this.id, OS.sel_formattingStringsFilename);
	return result != 0 ? new NSString(result) : null;
}

public void insertRowAtIndex(int rowIndex, int rowType, int parentRow, boolean shouldAnimate) {
	OS.objc_msgSend(this.id, OS.sel_insertRowAtIndex_1withType_1asSubrowOfRow_1animate_1, rowIndex, rowType, parentRow, shouldAnimate);
}

public boolean isEditable() {
	return OS.objc_msgSend(this.id, OS.sel_isEditable) != 0;
}

public int nestingMode() {
	return OS.objc_msgSend(this.id, OS.sel_nestingMode);
}

public int numberOfRows() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfRows);
}

public int parentRowForRow(int rowIndex) {
	return OS.objc_msgSend(this.id, OS.sel_parentRowForRow_1, rowIndex);
}

public NSPredicate predicate() {
	int result = OS.objc_msgSend(this.id, OS.sel_predicate);
	return result != 0 ? new NSPredicate(result) : null;
}

public NSPredicate predicateForRow(int row) {
	int result = OS.objc_msgSend(this.id, OS.sel_predicateForRow_1, row);
	return result != 0 ? new NSPredicate(result) : null;
}

public void reloadCriteria() {
	OS.objc_msgSend(this.id, OS.sel_reloadCriteria);
}

public void reloadPredicate() {
	OS.objc_msgSend(this.id, OS.sel_reloadPredicate);
}

public void removeRowAtIndex(int rowIndex) {
	OS.objc_msgSend(this.id, OS.sel_removeRowAtIndex_1, rowIndex);
}

public void removeRowsAtIndexes(NSIndexSet rowIndexes, boolean includeSubrows) {
	OS.objc_msgSend(this.id, OS.sel_removeRowsAtIndexes_1includeSubrows_1, rowIndexes != null ? rowIndexes.id : 0, includeSubrows);
}

public int rowClass() {
	return OS.objc_msgSend(this.id, OS.sel_rowClass);
}

public int rowForDisplayValue(id displayValue) {
	return OS.objc_msgSend(this.id, OS.sel_rowForDisplayValue_1, displayValue != null ? displayValue.id : 0);
}

public float rowHeight() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_rowHeight);
}

public int rowTypeForRow(int rowIndex) {
	return OS.objc_msgSend(this.id, OS.sel_rowTypeForRow_1, rowIndex);
}

public NSString rowTypeKeyPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_rowTypeKeyPath);
	return result != 0 ? new NSString(result) : null;
}

public void selectRowIndexes(NSIndexSet indexes, boolean extend) {
	OS.objc_msgSend(this.id, OS.sel_selectRowIndexes_1byExtendingSelection_1, indexes != null ? indexes.id : 0, extend);
}

public NSIndexSet selectedRowIndexes() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedRowIndexes);
	return result != 0 ? new NSIndexSet(result) : null;
}

public void setCanRemoveAllRows(boolean val) {
	OS.objc_msgSend(this.id, OS.sel_setCanRemoveAllRows_1, val);
}

public void setCriteria(NSArray criteria, NSArray values, int rowIndex) {
	OS.objc_msgSend(this.id, OS.sel_setCriteria_1andDisplayValues_1forRowAtIndex_1, criteria != null ? criteria.id : 0, values != null ? values.id : 0, rowIndex);
}

public void setCriteriaKeyPath(NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_setCriteriaKeyPath_1, keyPath != null ? keyPath.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setDisplayValuesKeyPath(NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_setDisplayValuesKeyPath_1, keyPath != null ? keyPath.id : 0);
}

public void setEditable(boolean editable) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_1, editable);
}

public void setFormattingDictionary(NSDictionary dictionary) {
	OS.objc_msgSend(this.id, OS.sel_setFormattingDictionary_1, dictionary != null ? dictionary.id : 0);
}

public void setFormattingStringsFilename(NSString stringsFilename) {
	OS.objc_msgSend(this.id, OS.sel_setFormattingStringsFilename_1, stringsFilename != null ? stringsFilename.id : 0);
}

public void setNestingMode(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setNestingMode_1, mode);
}

public void setRowClass(int rowClass) {
	OS.objc_msgSend(this.id, OS.sel_setRowClass_1, rowClass);
}

public void setRowHeight(float height) {
	OS.objc_msgSend(this.id, OS.sel_setRowHeight_1, height);
}

public void setRowTypeKeyPath(NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_setRowTypeKeyPath_1, keyPath != null ? keyPath.id : 0);
}

public void setSubrowsKeyPath(NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_setSubrowsKeyPath_1, keyPath != null ? keyPath.id : 0);
}

public NSIndexSet subrowIndexesForRow(int rowIndex) {
	int result = OS.objc_msgSend(this.id, OS.sel_subrowIndexesForRow_1, rowIndex);
	return result != 0 ? new NSIndexSet(result) : null;
}

public NSString subrowsKeyPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_subrowsKeyPath);
	return result != 0 ? new NSString(result) : null;
}

}

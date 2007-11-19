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

public class NSBrowser extends NSControl {

public NSBrowser() {
	super();
}

public NSBrowser(int id) {
	super(id);
}

public boolean acceptsArrowKeys() {
	return OS.objc_msgSend(this.id, OS.sel_acceptsArrowKeys) != 0;
}

public void addColumn() {
	OS.objc_msgSend(this.id, OS.sel_addColumn);
}

public boolean allowsBranchSelection() {
	return OS.objc_msgSend(this.id, OS.sel_allowsBranchSelection) != 0;
}

public boolean allowsEmptySelection() {
	return OS.objc_msgSend(this.id, OS.sel_allowsEmptySelection) != 0;
}

public boolean allowsMultipleSelection() {
	return OS.objc_msgSend(this.id, OS.sel_allowsMultipleSelection) != 0;
}

public boolean allowsTypeSelect() {
	return OS.objc_msgSend(this.id, OS.sel_allowsTypeSelect) != 0;
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public boolean canDragRowsWithIndexes(NSIndexSet rowIndexes, int column, NSEvent event) {
	return OS.objc_msgSend(this.id, OS.sel_canDragRowsWithIndexes_1inColumn_1withEvent_1, rowIndexes != null ? rowIndexes.id : 0, column, event != null ? event.id : 0) != 0;
}

public static int cellClass() {
	return OS.objc_msgSend(OS.class_NSBrowser, OS.sel_cellClass);
}

public id cellPrototype() {
	int result = OS.objc_msgSend(this.id, OS.sel_cellPrototype);
	return result != 0 ? new id(result) : null;
}

public float columnContentWidthForColumnWidth(float columnWidth) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_columnContentWidthForColumnWidth_1, columnWidth);
}

public int columnOfMatrix(NSMatrix matrix) {
	return OS.objc_msgSend(this.id, OS.sel_columnOfMatrix_1, matrix != null ? matrix.id : 0);
}

public int columnResizingType() {
	return OS.objc_msgSend(this.id, OS.sel_columnResizingType);
}

public float columnWidthForColumnContentWidth(float columnContentWidth) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_columnWidthForColumnContentWidth_1, columnContentWidth);
}

public NSString columnsAutosaveName() {
	int result = OS.objc_msgSend(this.id, OS.sel_columnsAutosaveName);
	return result != 0 ? new NSString(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void displayAllColumns() {
	OS.objc_msgSend(this.id, OS.sel_displayAllColumns);
}

public void displayColumn(int column) {
	OS.objc_msgSend(this.id, OS.sel_displayColumn_1, column);
}

public void doClick(id sender) {
	OS.objc_msgSend(this.id, OS.sel_doClick_1, sender != null ? sender.id : 0);
}

public void doDoubleClick(id sender) {
	OS.objc_msgSend(this.id, OS.sel_doDoubleClick_1, sender != null ? sender.id : 0);
}

public int doubleAction() {
	return OS.objc_msgSend(this.id, OS.sel_doubleAction);
}

public NSImage draggingImageForRowsWithIndexes(NSIndexSet rowIndexes, int column, NSEvent event, int dragImageOffset) {
	int result = OS.objc_msgSend(this.id, OS.sel_draggingImageForRowsWithIndexes_1inColumn_1withEvent_1offset_1, rowIndexes != null ? rowIndexes.id : 0, column, event != null ? event.id : 0, dragImageOffset);
	return result != 0 ? new NSImage(result) : null;
}

public void drawTitleOfColumn(int column, NSRect aRect) {
	OS.objc_msgSend(this.id, OS.sel_drawTitleOfColumn_1inRect_1, column, aRect);
}

public int firstVisibleColumn() {
	return OS.objc_msgSend(this.id, OS.sel_firstVisibleColumn);
}

public NSRect frameOfColumn(int column) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frameOfColumn_1, column);
	return result;
}

public NSRect frameOfInsideOfColumn(int column) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frameOfInsideOfColumn_1, column);
	return result;
}

public boolean hasHorizontalScroller() {
	return OS.objc_msgSend(this.id, OS.sel_hasHorizontalScroller) != 0;
}

public boolean isLoaded() {
	return OS.objc_msgSend(this.id, OS.sel_isLoaded) != 0;
}

public boolean isTitled() {
	return OS.objc_msgSend(this.id, OS.sel_isTitled) != 0;
}

public int lastColumn() {
	return OS.objc_msgSend(this.id, OS.sel_lastColumn);
}

public int lastVisibleColumn() {
	return OS.objc_msgSend(this.id, OS.sel_lastVisibleColumn);
}

public void loadColumnZero() {
	OS.objc_msgSend(this.id, OS.sel_loadColumnZero);
}

public id loadedCellAtRow(int row, int col) {
	int result = OS.objc_msgSend(this.id, OS.sel_loadedCellAtRow_1column_1, row, col);
	return result != 0 ? new id(result) : null;
}

public int matrixClass() {
	return OS.objc_msgSend(this.id, OS.sel_matrixClass);
}

public NSMatrix matrixInColumn(int column) {
	int result = OS.objc_msgSend(this.id, OS.sel_matrixInColumn_1, column);
	return result != 0 ? new NSMatrix(result) : null;
}

public int maxVisibleColumns() {
	return OS.objc_msgSend(this.id, OS.sel_maxVisibleColumns);
}

public float minColumnWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_minColumnWidth);
}

public int numberOfVisibleColumns() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfVisibleColumns);
}

public NSString path() {
	int result = OS.objc_msgSend(this.id, OS.sel_path);
	return result != 0 ? new NSString(result) : null;
}

public NSString pathSeparator() {
	int result = OS.objc_msgSend(this.id, OS.sel_pathSeparator);
	return result != 0 ? new NSString(result) : null;
}

public NSString pathToColumn(int column) {
	int result = OS.objc_msgSend(this.id, OS.sel_pathToColumn_1, column);
	return result != 0 ? new NSString(result) : null;
}

public boolean prefersAllColumnUserResizing() {
	return OS.objc_msgSend(this.id, OS.sel_prefersAllColumnUserResizing) != 0;
}

public void reloadColumn(int column) {
	OS.objc_msgSend(this.id, OS.sel_reloadColumn_1, column);
}

public static void removeSavedColumnsWithAutosaveName(NSString name) {
	OS.objc_msgSend(OS.class_NSBrowser, OS.sel_removeSavedColumnsWithAutosaveName_1, name != null ? name.id : 0);
}

public boolean reusesColumns() {
	return OS.objc_msgSend(this.id, OS.sel_reusesColumns) != 0;
}

public void scrollColumnToVisible(int column) {
	OS.objc_msgSend(this.id, OS.sel_scrollColumnToVisible_1, column);
}

public void scrollColumnsLeftBy(int shiftAmount) {
	OS.objc_msgSend(this.id, OS.sel_scrollColumnsLeftBy_1, shiftAmount);
}

public void scrollColumnsRightBy(int shiftAmount) {
	OS.objc_msgSend(this.id, OS.sel_scrollColumnsRightBy_1, shiftAmount);
}

public void scrollViaScroller(NSScroller sender) {
	OS.objc_msgSend(this.id, OS.sel_scrollViaScroller_1, sender != null ? sender.id : 0);
}

public void selectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectAll_1, sender != null ? sender.id : 0);
}

public void selectRow(int row, int column) {
	OS.objc_msgSend(this.id, OS.sel_selectRow_1inColumn_1, row, column);
}

public void selectRowIndexes(NSIndexSet indexes, int column) {
	OS.objc_msgSend(this.id, OS.sel_selectRowIndexes_1inColumn_1, indexes != null ? indexes.id : 0, column);
}

public id selectedCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedCell);
	return result != 0 ? new id(result) : null;
}

public id selectedCellInColumn(int column) {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedCellInColumn_1, column);
	return result != 0 ? new id(result) : null;
}

public NSArray selectedCells() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedCells);
	return result != 0 ? new NSArray(result) : null;
}

public int selectedColumn() {
	return OS.objc_msgSend(this.id, OS.sel_selectedColumn);
}

public int selectedRowInColumn(int column) {
	return OS.objc_msgSend(this.id, OS.sel_selectedRowInColumn_1, column);
}

public NSIndexSet selectedRowIndexesInColumn(int column) {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedRowIndexesInColumn_1, column);
	return result != 0 ? new NSIndexSet(result) : null;
}

public boolean sendAction() {
	return OS.objc_msgSend(this.id, OS.sel_sendAction) != 0;
}

public boolean sendsActionOnArrowKeys() {
	return OS.objc_msgSend(this.id, OS.sel_sendsActionOnArrowKeys) != 0;
}

public boolean separatesColumns() {
	return OS.objc_msgSend(this.id, OS.sel_separatesColumns) != 0;
}

public void setAcceptsArrowKeys(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAcceptsArrowKeys_1, flag);
}

public void setAllowsBranchSelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsBranchSelection_1, flag);
}

public void setAllowsEmptySelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsEmptySelection_1, flag);
}

public void setAllowsMultipleSelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMultipleSelection_1, flag);
}

public void setAllowsTypeSelect(boolean value) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsTypeSelect_1, value);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

//public void setCellClass(int factoryId) {
//	OS.objc_msgSend(this.id, OS.sel_setCellClass_1, factoryId);
//}

public void setCellPrototype(NSCell aCell) {
	OS.objc_msgSend(this.id, OS.sel_setCellPrototype_1, aCell != null ? aCell.id : 0);
}

public void setColumnResizingType(int columnResizingType) {
	OS.objc_msgSend(this.id, OS.sel_setColumnResizingType_1, columnResizingType);
}

public void setColumnsAutosaveName(NSString name) {
	OS.objc_msgSend(this.id, OS.sel_setColumnsAutosaveName_1, name != null ? name.id : 0);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setDoubleAction(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleAction_1, aSelector);
}

public void setDraggingSourceOperationMask(int mask, boolean isLocal) {
	OS.objc_msgSend(this.id, OS.sel_setDraggingSourceOperationMask_1forLocal_1, mask, isLocal);
}

public void setHasHorizontalScroller(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHasHorizontalScroller_1, flag);
}

public void setLastColumn(int column) {
	OS.objc_msgSend(this.id, OS.sel_setLastColumn_1, column);
}

public void setMatrixClass(int factoryId) {
	OS.objc_msgSend(this.id, OS.sel_setMatrixClass_1, factoryId);
}

public void setMaxVisibleColumns(int columnCount) {
	OS.objc_msgSend(this.id, OS.sel_setMaxVisibleColumns_1, columnCount);
}

public void setMinColumnWidth(float columnWidth) {
	OS.objc_msgSend(this.id, OS.sel_setMinColumnWidth_1, columnWidth);
}

public boolean setPath(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_setPath_1, path != null ? path.id : 0) != 0;
}

public void setPathSeparator(NSString newString) {
	OS.objc_msgSend(this.id, OS.sel_setPathSeparator_1, newString != null ? newString.id : 0);
}

public void setPrefersAllColumnUserResizing(boolean prefersAllColumnResizing) {
	OS.objc_msgSend(this.id, OS.sel_setPrefersAllColumnUserResizing_1, prefersAllColumnResizing);
}

public void setReusesColumns(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setReusesColumns_1, flag);
}

public void setSendsActionOnArrowKeys(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSendsActionOnArrowKeys_1, flag);
}

public void setSeparatesColumns(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSeparatesColumns_1, flag);
}

public void setTakesTitleFromPreviousColumn(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setTakesTitleFromPreviousColumn_1, flag);
}

public void setTitle(NSString aString, int column) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1ofColumn_1, aString != null ? aString.id : 0, column);
}

public void setTitled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setTitled_1, flag);
}

public void setWidth(float columnWidth, int columnIndex) {
	OS.objc_msgSend(this.id, OS.sel_setWidth_1ofColumn_1, columnWidth, columnIndex);
}

public boolean takesTitleFromPreviousColumn() {
	return OS.objc_msgSend(this.id, OS.sel_takesTitleFromPreviousColumn) != 0;
}

public void tile() {
	OS.objc_msgSend(this.id, OS.sel_tile);
}

public NSRect titleFrameOfColumn(int column) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_titleFrameOfColumn_1, column);
	return result;
}

public float titleHeight() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_titleHeight);
}

public NSString titleOfColumn(int column) {
	int result = OS.objc_msgSend(this.id, OS.sel_titleOfColumn_1, column);
	return result != 0 ? new NSString(result) : null;
}

public void updateScroller() {
	OS.objc_msgSend(this.id, OS.sel_updateScroller);
}

public void validateVisibleColumns() {
	OS.objc_msgSend(this.id, OS.sel_validateVisibleColumns);
}

public float widthOfColumn(int column) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_widthOfColumn_1, column);
}

}

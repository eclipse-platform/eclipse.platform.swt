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

public class NSMatrix extends NSControl {

public NSMatrix() {
	super();
}

public NSMatrix(int id) {
	super(id);
}

public boolean acceptsFirstMouse(NSEvent theEvent) {
	return OS.objc_msgSend(this.id, OS.sel_acceptsFirstMouse_1, theEvent != null ? theEvent.id : 0) != 0;
}

public void addColumn() {
	OS.objc_msgSend(this.id, OS.sel_addColumn);
}

public void addColumnWithCells(NSArray newCells) {
	OS.objc_msgSend(this.id, OS.sel_addColumnWithCells_1, newCells != null ? newCells.id : 0);
}

public void addRow() {
	OS.objc_msgSend(this.id, OS.sel_addRow);
}

public void addRowWithCells(NSArray newCells) {
	OS.objc_msgSend(this.id, OS.sel_addRowWithCells_1, newCells != null ? newCells.id : 0);
}

public boolean allowsEmptySelection() {
	return OS.objc_msgSend(this.id, OS.sel_allowsEmptySelection) != 0;
}

public boolean autosizesCells() {
	return OS.objc_msgSend(this.id, OS.sel_autosizesCells) != 0;
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public id cellAtRow(int row, int col) {
	int result = OS.objc_msgSend(this.id, OS.sel_cellAtRow_1column_1, row, col);
	return result != 0 ? new id(result) : null;
}

public NSColor cellBackgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_cellBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

//public int cellClass() {
//	return OS.objc_msgSend(this.id, OS.sel_cellClass);
//}

public NSRect cellFrameAtRow(int row, int col) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_cellFrameAtRow_1column_1, row, col);
	return result;
}

public NSSize cellSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_cellSize);
	return result;
}

public id cellWithTag(int anInt) {
	int result = OS.objc_msgSend(this.id, OS.sel_cellWithTag_1, anInt);
	return result != 0 ? new id(result) : null;
}

public NSArray cells() {
	int result = OS.objc_msgSend(this.id, OS.sel_cells);
	return result != 0 ? new NSArray(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void deselectAllCells() {
	OS.objc_msgSend(this.id, OS.sel_deselectAllCells);
}

public void deselectSelectedCell() {
	OS.objc_msgSend(this.id, OS.sel_deselectSelectedCell);
}

public int doubleAction() {
	return OS.objc_msgSend(this.id, OS.sel_doubleAction);
}

public void drawCellAtRow(int row, int col) {
	OS.objc_msgSend(this.id, OS.sel_drawCellAtRow_1column_1, row, col);
}

public boolean drawsBackground() {
	return OS.objc_msgSend(this.id, OS.sel_drawsBackground) != 0;
}

public boolean drawsCellBackground() {
	return OS.objc_msgSend(this.id, OS.sel_drawsCellBackground) != 0;
}

public void getNumberOfRows(int rowCount, int colCount) {
	OS.objc_msgSend(this.id, OS.sel_getNumberOfRows_1columns_1, rowCount, colCount);
}

public boolean getRow_column_forPoint_(int row, int col, NSPoint aPoint) {
	return OS.objc_msgSend(this.id, OS.sel_getRow_1column_1forPoint_1, row, col, aPoint) != 0;
}

public boolean getRow_column_ofCell_(int row, int col, NSCell aCell) {
	return OS.objc_msgSend(this.id, OS.sel_getRow_1column_1ofCell_1, row, col, aCell != null ? aCell.id : 0) != 0;
}

public void highlightCell(boolean flag, int row, int col) {
	OS.objc_msgSend(this.id, OS.sel_highlightCell_1atRow_1column_1, flag, row, col);
}

public id initWithFrame_(NSRect frameRect) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_1, frameRect);
	return result != 0 ? new id(result) : null;
}

public id initWithFrame_mode_cellClass_numberOfRows_numberOfColumns_(NSRect frameRect, int aMode, int factoryId, int rowsHigh, int colsWide) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_1mode_1cellClass_1numberOfRows_1numberOfColumns_1, frameRect, aMode, factoryId, rowsHigh, colsWide);
	return result != 0 ? new id(result) : null;
}

public id initWithFrame_mode_prototype_numberOfRows_numberOfColumns_(NSRect frameRect, int aMode, NSCell aCell, int rowsHigh, int colsWide) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_1mode_1prototype_1numberOfRows_1numberOfColumns_1, frameRect, aMode, aCell != null ? aCell.id : 0, rowsHigh, colsWide);
	return result != 0 ? new id(result) : null;
}

public void insertColumn_(int column) {
	OS.objc_msgSend(this.id, OS.sel_insertColumn_1, column);
}

public void insertColumn_withCells_(int column, NSArray newCells) {
	OS.objc_msgSend(this.id, OS.sel_insertColumn_1withCells_1, column, newCells != null ? newCells.id : 0);
}

public void insertRow_(int row) {
	OS.objc_msgSend(this.id, OS.sel_insertRow_1, row);
}

public void insertRow_withCells_(int row, NSArray newCells) {
	OS.objc_msgSend(this.id, OS.sel_insertRow_1withCells_1, row, newCells != null ? newCells.id : 0);
}

public NSSize intercellSpacing() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_intercellSpacing);
	return result;
}

public boolean isAutoscroll() {
	return OS.objc_msgSend(this.id, OS.sel_isAutoscroll) != 0;
}

public boolean isSelectionByRect() {
	return OS.objc_msgSend(this.id, OS.sel_isSelectionByRect) != 0;
}

public id keyCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_keyCell);
	return result != 0 ? new id(result) : null;
}

public NSCell makeCellAtRow(int row, int col) {
	int result = OS.objc_msgSend(this.id, OS.sel_makeCellAtRow_1column_1, row, col);
	return result != 0 ? new NSCell(result) : null;
}

public int mode() {
	return OS.objc_msgSend(this.id, OS.sel_mode);
}

public void mouseDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseDown_1, theEvent != null ? theEvent.id : 0);
}

public int mouseDownFlags() {
	return OS.objc_msgSend(this.id, OS.sel_mouseDownFlags);
}

public int numberOfColumns() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfColumns);
}

public int numberOfRows() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfRows);
}

public boolean performKeyEquivalent(NSEvent theEvent) {
	return OS.objc_msgSend(this.id, OS.sel_performKeyEquivalent_1, theEvent != null ? theEvent.id : 0) != 0;
}

public id prototype() {
	int result = OS.objc_msgSend(this.id, OS.sel_prototype);
	return result != 0 ? new id(result) : null;
}

public void putCell(NSCell newCell, int row, int col) {
	OS.objc_msgSend(this.id, OS.sel_putCell_1atRow_1column_1, newCell != null ? newCell.id : 0, row, col);
}

public void removeColumn(int col) {
	OS.objc_msgSend(this.id, OS.sel_removeColumn_1, col);
}

public void removeRow(int row) {
	OS.objc_msgSend(this.id, OS.sel_removeRow_1, row);
}

public void renewRows(int newRows, int newCols) {
	OS.objc_msgSend(this.id, OS.sel_renewRows_1columns_1, newRows, newCols);
}

public void resetCursorRects() {
	OS.objc_msgSend(this.id, OS.sel_resetCursorRects);
}

public void scrollCellToVisibleAtRow(int row, int col) {
	OS.objc_msgSend(this.id, OS.sel_scrollCellToVisibleAtRow_1column_1, row, col);
}

public void selectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectAll_1, sender != null ? sender.id : 0);
}

public void selectCellAtRow(int row, int col) {
	OS.objc_msgSend(this.id, OS.sel_selectCellAtRow_1column_1, row, col);
}

public boolean selectCellWithTag(int anInt) {
	return OS.objc_msgSend(this.id, OS.sel_selectCellWithTag_1, anInt) != 0;
}

public void selectText(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectText_1, sender != null ? sender.id : 0);
}

public id selectTextAtRow(int row, int col) {
	int result = OS.objc_msgSend(this.id, OS.sel_selectTextAtRow_1column_1, row, col);
	return result != 0 ? new id(result) : null;
}

public id selectedCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedCell);
	return result != 0 ? new id(result) : null;
}

public NSArray selectedCells() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedCells);
	return result != 0 ? new NSArray(result) : null;
}

public int selectedColumn() {
	return OS.objc_msgSend(this.id, OS.sel_selectedColumn);
}

public int selectedRow() {
	return OS.objc_msgSend(this.id, OS.sel_selectedRow);
}

public boolean sendAction() {
	return OS.objc_msgSend(this.id, OS.sel_sendAction) != 0;
}

public void sendAction_to_forAllCells_(int aSelector, id anObject, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_sendAction_1to_1forAllCells_1, aSelector, anObject != null ? anObject.id : 0, flag);
}

public void sendDoubleAction() {
	OS.objc_msgSend(this.id, OS.sel_sendDoubleAction);
}

public void setAllowsEmptySelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsEmptySelection_1, flag);
}

public void setAutoscroll(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutoscroll_1, flag);
}

public void setAutosizesCells(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutosizesCells_1, flag);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setCellBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setCellBackgroundColor_1, color != null ? color.id : 0);
}

//public void setCellClass(int factoryId) {
//	OS.objc_msgSend(this.id, OS.sel_setCellClass_1, factoryId);
//}

public void setCellSize(NSSize aSize) {
	OS.objc_msgSend(this.id, OS.sel_setCellSize_1, aSize);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setDoubleAction(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleAction_1, aSelector);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_1, flag);
}

public void setDrawsCellBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsCellBackground_1, flag);
}

public void setIntercellSpacing(NSSize aSize) {
	OS.objc_msgSend(this.id, OS.sel_setIntercellSpacing_1, aSize);
}

public void setKeyCell(NSCell keyCell) {
	OS.objc_msgSend(this.id, OS.sel_setKeyCell_1, keyCell != null ? keyCell.id : 0);
}

public void setMode(int aMode) {
	OS.objc_msgSend(this.id, OS.sel_setMode_1, aMode);
}

public void setPrototype(NSCell aCell) {
	OS.objc_msgSend(this.id, OS.sel_setPrototype_1, aCell != null ? aCell.id : 0);
}

public void setScrollable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setScrollable_1, flag);
}

public void setSelectionByRect(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectionByRect_1, flag);
}

public void setSelectionFrom(int startPos, int endPos, int anchorPos, boolean lit) {
	OS.objc_msgSend(this.id, OS.sel_setSelectionFrom_1to_1anchor_1highlight_1, startPos, endPos, anchorPos, lit);
}

public void setState(int value, int row, int col) {
	OS.objc_msgSend(this.id, OS.sel_setState_1atRow_1column_1, value, row, col);
}

public void setTabKeyTraversesCells(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setTabKeyTraversesCells_1, flag);
}

public void setToolTip(NSString toolTipString, NSCell cell) {
	OS.objc_msgSend(this.id, OS.sel_setToolTip_1forCell_1, toolTipString != null ? toolTipString.id : 0, cell != null ? cell.id : 0);
}

public void setValidateSize(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setValidateSize_1, flag);
}

public void sizeToCells() {
	OS.objc_msgSend(this.id, OS.sel_sizeToCells);
}

public void sortUsingFunction(int compare, int context) {
	OS.objc_msgSend(this.id, OS.sel_sortUsingFunction_1context_1, compare, context);
}

public void sortUsingSelector(int comparator) {
	OS.objc_msgSend(this.id, OS.sel_sortUsingSelector_1, comparator);
}

public boolean tabKeyTraversesCells() {
	return OS.objc_msgSend(this.id, OS.sel_tabKeyTraversesCells) != 0;
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

public NSString toolTipForCell(NSCell cell) {
	int result = OS.objc_msgSend(this.id, OS.sel_toolTipForCell_1, cell != null ? cell.id : 0);
	return result != 0 ? new NSString(result) : null;
}

}

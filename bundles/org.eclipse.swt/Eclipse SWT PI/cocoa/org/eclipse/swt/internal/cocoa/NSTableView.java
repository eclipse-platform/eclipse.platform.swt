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

public class NSTableView extends NSControl {

public NSTableView() {
	super();
}

public NSTableView(int id) {
	super(id);
}

public void addTableColumn(NSTableColumn column) {
	OS.objc_msgSend(this.id, OS.sel_addTableColumn_1, column != null ? column.id : 0);
}

public boolean allowsColumnReordering() {
	return OS.objc_msgSend(this.id, OS.sel_allowsColumnReordering) != 0;
}

public boolean allowsColumnResizing() {
	return OS.objc_msgSend(this.id, OS.sel_allowsColumnResizing) != 0;
}

public boolean allowsColumnSelection() {
	return OS.objc_msgSend(this.id, OS.sel_allowsColumnSelection) != 0;
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

public boolean autoresizesAllColumnsToFit() {
	return OS.objc_msgSend(this.id, OS.sel_autoresizesAllColumnsToFit) != 0;
}

public NSString autosaveName() {
	int result = OS.objc_msgSend(this.id, OS.sel_autosaveName);
	return result != 0 ? new NSString(result) : null;
}

public boolean autosaveTableColumns() {
	return OS.objc_msgSend(this.id, OS.sel_autosaveTableColumns) != 0;
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public boolean canDragRowsWithIndexes(NSIndexSet rowIndexes, NSPoint mouseDownPoint) {
	return OS.objc_msgSend(this.id, OS.sel_canDragRowsWithIndexes_1atPoint_1, rowIndexes != null ? rowIndexes.id : 0, mouseDownPoint) != 0;
}

public int clickedColumn() {
	return OS.objc_msgSend(this.id, OS.sel_clickedColumn);
}

public int clickedRow() {
	return OS.objc_msgSend(this.id, OS.sel_clickedRow);
}

public int columnAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_columnAtPoint_1, point);
}

public int columnAutoresizingStyle() {
	return OS.objc_msgSend(this.id, OS.sel_columnAutoresizingStyle);
}

public NSIndexSet columnIndexesInRect(NSRect rect) {
	int result = OS.objc_msgSend(this.id, OS.sel_columnIndexesInRect_1, rect);
	return result != 0 ? new NSIndexSet(result) : null;
}

public int columnWithIdentifier(id identifier) {
	return OS.objc_msgSend(this.id, OS.sel_columnWithIdentifier_1, identifier != null ? identifier.id : 0);
}

public NSRange columnsInRect(NSRect rect) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_columnsInRect_1, rect);
	return result;
}

public NSView cornerView() {
	int result = OS.objc_msgSend(this.id, OS.sel_cornerView);
	return result != 0 ? new NSView(result) : null;
}

public id dataSource() {
	int result = OS.objc_msgSend(this.id, OS.sel_dataSource);
	return result != 0 ? new id(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void deselectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deselectAll_1, sender != null ? sender.id : 0);
}

public void deselectColumn(int column) {
	OS.objc_msgSend(this.id, OS.sel_deselectColumn_1, column);
}

public void deselectRow(int row) {
	OS.objc_msgSend(this.id, OS.sel_deselectRow_1, row);
}

public int doubleAction() {
	return OS.objc_msgSend(this.id, OS.sel_doubleAction);
}

public NSImage dragImageForRows(NSArray dragRows, NSEvent dragEvent, int dragImageOffset) {
	int result = OS.objc_msgSend(this.id, OS.sel_dragImageForRows_1event_1dragImageOffset_1, dragRows != null ? dragRows.id : 0, dragEvent != null ? dragEvent.id : 0, dragImageOffset);
	return result != 0 ? new NSImage(result) : null;
}

public NSImage dragImageForRowsWithIndexes(NSIndexSet dragRows, NSArray tableColumns, NSEvent dragEvent, int dragImageOffset) {
	int result = OS.objc_msgSend(this.id, OS.sel_dragImageForRowsWithIndexes_1tableColumns_1event_1offset_1, dragRows != null ? dragRows.id : 0, tableColumns != null ? tableColumns.id : 0, dragEvent != null ? dragEvent.id : 0, dragImageOffset);
	return result != 0 ? new NSImage(result) : null;
}

public void drawBackgroundInClipRect(NSRect clipRect) {
	OS.objc_msgSend(this.id, OS.sel_drawBackgroundInClipRect_1, clipRect);
}

public void drawGridInClipRect(NSRect clipRect) {
	OS.objc_msgSend(this.id, OS.sel_drawGridInClipRect_1, clipRect);
}

public void drawRow(int row, NSRect clipRect) {
	OS.objc_msgSend(this.id, OS.sel_drawRow_1clipRect_1, row, clipRect);
}

public boolean drawsGrid() {
	return OS.objc_msgSend(this.id, OS.sel_drawsGrid) != 0;
}

public void editColumn(int column, int row, NSEvent theEvent, boolean select) {
	OS.objc_msgSend(this.id, OS.sel_editColumn_1row_1withEvent_1select_1, column, row, theEvent != null ? theEvent.id : 0, select);
}

public int editedColumn() {
	return OS.objc_msgSend(this.id, OS.sel_editedColumn);
}

public int editedRow() {
	return OS.objc_msgSend(this.id, OS.sel_editedRow);
}

public NSRect frameOfCellAtColumn(int column, int row) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frameOfCellAtColumn_1row_1, column, row);
	return result;
}

public NSColor gridColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_gridColor);
	return result != 0 ? new NSColor(result) : null;
}

public int gridStyleMask() {
	return OS.objc_msgSend(this.id, OS.sel_gridStyleMask);
}

public NSTableHeaderView headerView() {
	int result = OS.objc_msgSend(this.id, OS.sel_headerView);
	return result != 0 ? new NSTableHeaderView(result) : null;
}

public void highlightSelectionInClipRect(NSRect clipRect) {
	OS.objc_msgSend(this.id, OS.sel_highlightSelectionInClipRect_1, clipRect);
}

public NSTableColumn highlightedTableColumn() {
	int result = OS.objc_msgSend(this.id, OS.sel_highlightedTableColumn);
	return result != 0 ? new NSTableColumn(result) : null;
}

public NSImage indicatorImageInTableColumn(NSTableColumn tc) {
	int result = OS.objc_msgSend(this.id, OS.sel_indicatorImageInTableColumn_1, tc != null ? tc.id : 0);
	return result != 0 ? new NSImage(result) : null;
}

public NSSize intercellSpacing() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_intercellSpacing);
	return result;
}

public boolean isColumnSelected(int column) {
	return OS.objc_msgSend(this.id, OS.sel_isColumnSelected_1, column) != 0;
}

public boolean isRowSelected(int row) {
	return OS.objc_msgSend(this.id, OS.sel_isRowSelected_1, row) != 0;
}

public void moveColumn(int column, int newIndex) {
	OS.objc_msgSend(this.id, OS.sel_moveColumn_1toColumn_1, column, newIndex);
}

public void noteHeightOfRowsWithIndexesChanged(NSIndexSet indexSet) {
	OS.objc_msgSend(this.id, OS.sel_noteHeightOfRowsWithIndexesChanged_1, indexSet != null ? indexSet.id : 0);
}

public void noteNumberOfRowsChanged() {
	OS.objc_msgSend(this.id, OS.sel_noteNumberOfRowsChanged);
}

public int numberOfColumns() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfColumns);
}

public int numberOfRows() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfRows);
}

public int numberOfSelectedColumns() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfSelectedColumns);
}

public int numberOfSelectedRows() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfSelectedRows);
}

public NSCell preparedCellAtColumn(int column, int row) {
	int result = OS.objc_msgSend(this.id, OS.sel_preparedCellAtColumn_1row_1, column, row);
	return result != 0 ? new NSCell(result) : null;
}

public NSRect rectOfColumn(int column) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectOfColumn_1, column);
	return result;
}

public NSRect rectOfRow(int row) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectOfRow_1, row);
	return result;
}

public void reloadData() {
	OS.objc_msgSend(this.id, OS.sel_reloadData);
}

public void removeTableColumn(NSTableColumn column) {
	OS.objc_msgSend(this.id, OS.sel_removeTableColumn_1, column != null ? column.id : 0);
}

public int rowAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_rowAtPoint_1, point);
}

public float rowHeight() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_rowHeight);
}

public NSRange rowsInRect(NSRect rect) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rowsInRect_1, rect);
	return result;
}

public void scrollColumnToVisible(int column) {
	OS.objc_msgSend(this.id, OS.sel_scrollColumnToVisible_1, column);
}

public void scrollRowToVisible(int row) {
	OS.objc_msgSend(this.id, OS.sel_scrollRowToVisible_1, row);
}

public void selectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectAll_1, sender != null ? sender.id : 0);
}

public void selectColumn(int column, boolean extend) {
	OS.objc_msgSend(this.id, OS.sel_selectColumn_1byExtendingSelection_1, column, extend);
}

public void selectColumnIndexes(NSIndexSet indexes, boolean extend) {
	OS.objc_msgSend(this.id, OS.sel_selectColumnIndexes_1byExtendingSelection_1, indexes != null ? indexes.id : 0, extend);
}

public void selectRow(int row, boolean extend) {
	OS.objc_msgSend(this.id, OS.sel_selectRow_1byExtendingSelection_1, row, extend);
}

public void selectRowIndexes(NSIndexSet indexes, boolean extend) {
	OS.objc_msgSend(this.id, OS.sel_selectRowIndexes_1byExtendingSelection_1, indexes != null ? indexes.id : 0, extend);
}

public int selectedColumn() {
	return OS.objc_msgSend(this.id, OS.sel_selectedColumn);
}

public NSEnumerator selectedColumnEnumerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedColumnEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public NSIndexSet selectedColumnIndexes() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedColumnIndexes);
	return result != 0 ? new NSIndexSet(result) : null;
}

public int selectedRow() {
	return OS.objc_msgSend(this.id, OS.sel_selectedRow);
}

public NSEnumerator selectedRowEnumerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedRowEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public NSIndexSet selectedRowIndexes() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedRowIndexes);
	return result != 0 ? new NSIndexSet(result) : null;
}

public int selectionHighlightStyle() {
	return OS.objc_msgSend(this.id, OS.sel_selectionHighlightStyle);
}

public void setAllowsColumnReordering(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsColumnReordering_1, flag);
}

public void setAllowsColumnResizing(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsColumnResizing_1, flag);
}

public void setAllowsColumnSelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsColumnSelection_1, flag);
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

public void setAutoresizesAllColumnsToFit(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutoresizesAllColumnsToFit_1, flag);
}

public void setAutosaveName(NSString name) {
	OS.objc_msgSend(this.id, OS.sel_setAutosaveName_1, name != null ? name.id : 0);
}

public void setAutosaveTableColumns(boolean save) {
	OS.objc_msgSend(this.id, OS.sel_setAutosaveTableColumns_1, save);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setColumnAutoresizingStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setColumnAutoresizingStyle_1, style);
}

public void setCornerView(NSView cornerView) {
	OS.objc_msgSend(this.id, OS.sel_setCornerView_1, cornerView != null ? cornerView.id : 0);
}

public void setDataSource(id aSource) {
	OS.objc_msgSend(this.id, OS.sel_setDataSource_1, aSource != null ? aSource.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setDoubleAction(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleAction_1, aSelector);
}

public void setDraggingSourceOperationMask(int mask, boolean isLocal) {
	OS.objc_msgSend(this.id, OS.sel_setDraggingSourceOperationMask_1forLocal_1, mask, isLocal);
}

public void setDrawsGrid(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsGrid_1, flag);
}

public void setDropRow(int row, int op) {
	OS.objc_msgSend(this.id, OS.sel_setDropRow_1dropOperation_1, row, op);
}

public void setGridColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setGridColor_1, color != null ? color.id : 0);
}

public void setGridStyleMask(int gridType) {
	OS.objc_msgSend(this.id, OS.sel_setGridStyleMask_1, gridType);
}

public void setHeaderView(NSTableHeaderView headerView) {
	OS.objc_msgSend(this.id, OS.sel_setHeaderView_1, headerView != null ? headerView.id : 0);
}

public void setHighlightedTableColumn(NSTableColumn tc) {
	OS.objc_msgSend(this.id, OS.sel_setHighlightedTableColumn_1, tc != null ? tc.id : 0);
}

public void setIndicatorImage(NSImage anImage, NSTableColumn tc) {
	OS.objc_msgSend(this.id, OS.sel_setIndicatorImage_1inTableColumn_1, anImage != null ? anImage.id : 0, tc != null ? tc.id : 0);
}

public void setIntercellSpacing(NSSize aSize) {
	OS.objc_msgSend(this.id, OS.sel_setIntercellSpacing_1, aSize);
}

public void setRowHeight(float rowHeight) {
	OS.objc_msgSend(this.id, OS.sel_setRowHeight_1, rowHeight);
}

public void setSelectionHighlightStyle(int selectionHighlightStyle) {
	OS.objc_msgSend(this.id, OS.sel_setSelectionHighlightStyle_1, selectionHighlightStyle);
}

public void setSortDescriptors(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setSortDescriptors_1, array != null ? array.id : 0);
}

public void setUsesAlternatingRowBackgroundColors(boolean useAlternatingRowColors) {
	OS.objc_msgSend(this.id, OS.sel_setUsesAlternatingRowBackgroundColors_1, useAlternatingRowColors);
}

public void setVerticalMotionCanBeginDrag(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setVerticalMotionCanBeginDrag_1, flag);
}

public void sizeLastColumnToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeLastColumnToFit);
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public NSArray sortDescriptors() {
	int result = OS.objc_msgSend(this.id, OS.sel_sortDescriptors);
	return result != 0 ? new NSArray(result) : null;
}

public NSTableColumn tableColumnWithIdentifier(id identifier) {
	int result = OS.objc_msgSend(this.id, OS.sel_tableColumnWithIdentifier_1, identifier != null ? identifier.id : 0);
	return result != 0 ? new NSTableColumn(result) : null;
}

public NSArray tableColumns() {
	int result = OS.objc_msgSend(this.id, OS.sel_tableColumns);
	return result != 0 ? new NSArray(result) : null;
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

public void tile() {
	OS.objc_msgSend(this.id, OS.sel_tile);
}

public boolean usesAlternatingRowBackgroundColors() {
	return OS.objc_msgSend(this.id, OS.sel_usesAlternatingRowBackgroundColors) != 0;
}

public boolean verticalMotionCanBeginDrag() {
	return OS.objc_msgSend(this.id, OS.sel_verticalMotionCanBeginDrag) != 0;
}

}

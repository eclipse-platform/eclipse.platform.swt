/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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

public class NSTableView extends NSControl {

public NSTableView() {
	super();
}

public NSTableView(long id) {
	super(id);
}

public NSTableView(id id) {
	super(id);
}

public void addTableColumn(NSTableColumn tableColumn) {
	OS.objc_msgSend(this.id, OS.sel_addTableColumn_, tableColumn != null ? tableColumn.id : 0);
}

public boolean canDragRowsWithIndexes(NSIndexSet rowIndexes, NSPoint mouseDownPoint) {
	return OS.objc_msgSend_bool(this.id, OS.sel_canDragRowsWithIndexes_atPoint_, rowIndexes != null ? rowIndexes.id : 0, mouseDownPoint);
}

public long clickedColumn() {
	return OS.objc_msgSend(this.id, OS.sel_clickedColumn);
}

public long clickedRow() {
	return OS.objc_msgSend(this.id, OS.sel_clickedRow);
}

public long columnAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_columnAtPoint_, point);
}

public void deselectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deselectAll_, sender != null ? sender.id : 0);
}

public void deselectRow(long row) {
	OS.objc_msgSend(this.id, OS.sel_deselectRow_, row);
}

public NSImage dragImageForRowsWithIndexes(NSIndexSet dragRows, NSArray tableColumns, NSEvent dragEvent, long dragImageOffset) {
	long result = OS.objc_msgSend(this.id, OS.sel_dragImageForRowsWithIndexes_tableColumns_event_offset_, dragRows != null ? dragRows.id : 0, tableColumns != null ? tableColumns.id : 0, dragEvent != null ? dragEvent.id : 0, dragImageOffset);
	return result != 0 ? new NSImage(result) : null;
}

public void drawBackgroundInClipRect(NSRect clipRect) {
	OS.objc_msgSend(this.id, OS.sel_drawBackgroundInClipRect_, clipRect);
}

public NSRect frameOfCellAtColumn(long column, long row) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frameOfCellAtColumn_row_, column, row);
	return result;
}

public NSTableHeaderView headerView() {
	long result = OS.objc_msgSend(this.id, OS.sel_headerView);
	return result != 0 ? new NSTableHeaderView(result) : null;
}

public void highlightSelectionInClipRect(NSRect clipRect) {
	OS.objc_msgSend(this.id, OS.sel_highlightSelectionInClipRect_, clipRect);
}

public NSSize intercellSpacing() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_intercellSpacing);
	return result;
}

public boolean isRowSelected(long row) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isRowSelected_, row);
}

public void moveColumn(long oldIndex, long newIndex) {
	OS.objc_msgSend(this.id, OS.sel_moveColumn_toColumn_, oldIndex, newIndex);
}

public void noteNumberOfRowsChanged() {
	OS.objc_msgSend(this.id, OS.sel_noteNumberOfRowsChanged);
}

public long numberOfColumns() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfColumns);
}

public long numberOfRows() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfRows);
}

public long numberOfSelectedRows() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfSelectedRows);
}

public NSCell preparedCellAtColumn(long column, long row) {
	long result = OS.objc_msgSend(this.id, OS.sel_preparedCellAtColumn_row_, column, row);
	return result != 0 ? new NSCell(result) : null;
}

public NSRect rectOfColumn(long column) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectOfColumn_, column);
	return result;
}

public NSRect rectOfRow(long row) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectOfRow_, row);
	return result;
}

public void reloadData() {
	OS.objc_msgSend(this.id, OS.sel_reloadData);
}

public void removeTableColumn(NSTableColumn tableColumn) {
	OS.objc_msgSend(this.id, OS.sel_removeTableColumn_, tableColumn != null ? tableColumn.id : 0);
}

public long rowAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_rowAtPoint_, point);
}

public double rowHeight() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_rowHeight);
}

public void scrollColumnToVisible(long column) {
	OS.objc_msgSend(this.id, OS.sel_scrollColumnToVisible_, column);
}

public void scrollRowToVisible(long row) {
	OS.objc_msgSend(this.id, OS.sel_scrollRowToVisible_, row);
}

public void selectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectAll_, sender != null ? sender.id : 0);
}

public void selectRowIndexes(NSIndexSet indexes, boolean extend) {
	OS.objc_msgSend(this.id, OS.sel_selectRowIndexes_byExtendingSelection_, indexes != null ? indexes.id : 0, extend);
}

public long selectedRow() {
	return OS.objc_msgSend(this.id, OS.sel_selectedRow);
}

public NSIndexSet selectedRowIndexes() {
	long result = OS.objc_msgSend(this.id, OS.sel_selectedRowIndexes);
	return result != 0 ? new NSIndexSet(result) : null;
}

public void setAllowsColumnReordering(boolean allowsColumnReordering) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsColumnReordering_, allowsColumnReordering);
}

public void setAllowsMultipleSelection(boolean allowsMultipleSelection) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMultipleSelection_, allowsMultipleSelection);
}

public void setBackgroundColor(NSColor backgroundColor) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, backgroundColor != null ? backgroundColor.id : 0);
}

public void setColumnAutoresizingStyle(long columnAutoresizingStyle) {
	OS.objc_msgSend(this.id, OS.sel_setColumnAutoresizingStyle_, columnAutoresizingStyle);
}

public void setDataSource(id aSource) {
	OS.objc_msgSend(this.id, OS.sel_setDataSource_, aSource != null ? aSource.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, delegate != null ? delegate.id : 0);
}

public void setDoubleAction(long doubleAction) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleAction_, doubleAction);
}

public void setDropRow(long row, long dropOperation) {
	OS.objc_msgSend(this.id, OS.sel_setDropRow_dropOperation_, row, dropOperation);
}

public void setGridStyleMask(long gridStyleMask) {
	OS.objc_msgSend(this.id, OS.sel_setGridStyleMask_, gridStyleMask);
}

public void setHeaderView(NSTableHeaderView headerView) {
	OS.objc_msgSend(this.id, OS.sel_setHeaderView_, headerView != null ? headerView.id : 0);
}

public void setHighlightedTableColumn(NSTableColumn highlightedTableColumn) {
	OS.objc_msgSend(this.id, OS.sel_setHighlightedTableColumn_, highlightedTableColumn != null ? highlightedTableColumn.id : 0);
}

public void setIndicatorImage(NSImage anImage, NSTableColumn tableColumn) {
	OS.objc_msgSend(this.id, OS.sel_setIndicatorImage_inTableColumn_, anImage != null ? anImage.id : 0, tableColumn != null ? tableColumn.id : 0);
}

public void setIntercellSpacing(NSSize intercellSpacing) {
	OS.objc_msgSend(this.id, OS.sel_setIntercellSpacing_, intercellSpacing);
}

public void setRowHeight(double rowHeight) {
	OS.objc_msgSend(this.id, OS.sel_setRowHeight_, rowHeight);
}

public void setUsesAlternatingRowBackgroundColors(boolean usesAlternatingRowBackgroundColors) {
	OS.objc_msgSend(this.id, OS.sel_setUsesAlternatingRowBackgroundColors_, usesAlternatingRowBackgroundColors);
}

public NSArray tableColumns() {
	long result = OS.objc_msgSend(this.id, OS.sel_tableColumns);
	return result != 0 ? new NSArray(result) : null;
}

public void tile() {
	OS.objc_msgSend(this.id, OS.sel_tile);
}

public boolean usesAlternatingRowBackgroundColors() {
	return OS.objc_msgSend_bool(this.id, OS.sel_usesAlternatingRowBackgroundColors);
}

public static long cellClass() {
	return OS.objc_msgSend(OS.class_NSTableView, OS.sel_cellClass);
}

public static void setCellClass(long factoryId) {
	OS.objc_msgSend(OS.class_NSTableView, OS.sel_setCellClass_, factoryId);
}

}

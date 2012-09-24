/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTableView extends NSControl {

public NSTableView() {
	super();
}

public NSTableView(long /*int*/ id) {
	super(id);
}

public NSTableView(id id) {
	super(id);
}

public void addTableColumn(NSTableColumn column) {
	OS.objc_msgSend(this.id, OS.sel_addTableColumn_, column != null ? column.id : 0);
}

public boolean allowsColumnReordering() {
	return OS.objc_msgSend_bool(this.id, OS.sel_allowsColumnReordering);
}

public boolean canDragRowsWithIndexes(NSIndexSet rowIndexes, NSPoint mouseDownPoint) {
	return OS.objc_msgSend_bool(this.id, OS.sel_canDragRowsWithIndexes_atPoint_, rowIndexes != null ? rowIndexes.id : 0, mouseDownPoint);
}

public long /*int*/ clickedColumn() {
	return OS.objc_msgSend(this.id, OS.sel_clickedColumn);
}

public long /*int*/ clickedRow() {
	return OS.objc_msgSend(this.id, OS.sel_clickedRow);
}

public long /*int*/ columnAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_columnAtPoint_, point);
}

public NSIndexSet columnIndexesInRect(NSRect rect) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_columnIndexesInRect_, rect);
	return result != 0 ? new NSIndexSet(result) : null;
}

public long /*int*/ columnWithIdentifier(NSString identifier) {
	return OS.objc_msgSend(this.id, OS.sel_columnWithIdentifier_, identifier != null ? identifier.id : 0);
}

public void deselectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deselectAll_, sender != null ? sender.id : 0);
}

public void deselectRow(long /*int*/ row) {
	OS.objc_msgSend(this.id, OS.sel_deselectRow_, row);
}

public NSImage dragImageForRowsWithIndexes(NSIndexSet dragRows, NSArray tableColumns, NSEvent dragEvent, long /*int*/ dragImageOffset) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_dragImageForRowsWithIndexes_tableColumns_event_offset_, dragRows != null ? dragRows.id : 0, tableColumns != null ? tableColumns.id : 0, dragEvent != null ? dragEvent.id : 0, dragImageOffset);
	return result != 0 ? new NSImage(result) : null;
}

public void drawBackgroundInClipRect(NSRect clipRect) {
	OS.objc_msgSend(this.id, OS.sel_drawBackgroundInClipRect_, clipRect);
}

public NSRect frameOfCellAtColumn(long /*int*/ column, long /*int*/ row) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frameOfCellAtColumn_row_, column, row);
	return result;
}

public NSTableHeaderView headerView() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_headerView);
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

public boolean isRowSelected(long /*int*/ row) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isRowSelected_, row);
}

public void moveColumn(long /*int*/ column, long /*int*/ newIndex) {
	OS.objc_msgSend(this.id, OS.sel_moveColumn_toColumn_, column, newIndex);
}

public void noteNumberOfRowsChanged() {
	OS.objc_msgSend(this.id, OS.sel_noteNumberOfRowsChanged);
}

public long /*int*/ numberOfColumns() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfColumns);
}

public long /*int*/ numberOfRows() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfRows);
}

public long /*int*/ numberOfSelectedRows() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfSelectedRows);
}

public NSCell preparedCellAtColumn(long /*int*/ column, long /*int*/ row) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_preparedCellAtColumn_row_, column, row);
	return result != 0 ? new NSCell(result) : null;
}

public NSRect rectOfColumn(long /*int*/ column) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectOfColumn_, column);
	return result;
}

public NSRect rectOfRow(long /*int*/ row) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectOfRow_, row);
	return result;
}

public void reloadData() {
	OS.objc_msgSend(this.id, OS.sel_reloadData);
}

public void removeTableColumn(NSTableColumn column) {
	OS.objc_msgSend(this.id, OS.sel_removeTableColumn_, column != null ? column.id : 0);
}

public long /*int*/ rowAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_rowAtPoint_, point);
}

public double /*float*/ rowHeight() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_rowHeight);
}

public NSRange rowsInRect(NSRect rect) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rowsInRect_, rect);
	return result;
}

public void scrollColumnToVisible(long /*int*/ column) {
	OS.objc_msgSend(this.id, OS.sel_scrollColumnToVisible_, column);
}

public void scrollRowToVisible(long /*int*/ row) {
	OS.objc_msgSend(this.id, OS.sel_scrollRowToVisible_, row);
}

public void selectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectAll_, sender != null ? sender.id : 0);
}

public void selectRowIndexes(NSIndexSet indexes, boolean extend) {
	OS.objc_msgSend(this.id, OS.sel_selectRowIndexes_byExtendingSelection_, indexes != null ? indexes.id : 0, extend);
}

public long /*int*/ selectedRow() {
	return OS.objc_msgSend(this.id, OS.sel_selectedRow);
}

public NSIndexSet selectedRowIndexes() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_selectedRowIndexes);
	return result != 0 ? new NSIndexSet(result) : null;
}

public void setAllowsColumnReordering(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsColumnReordering_, flag);
}

public void setAllowsMultipleSelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMultipleSelection_, flag);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, color != null ? color.id : 0);
}

public void setColumnAutoresizingStyle(long /*int*/ style) {
	OS.objc_msgSend(this.id, OS.sel_setColumnAutoresizingStyle_, style);
}

public void setDataSource(id aSource) {
	OS.objc_msgSend(this.id, OS.sel_setDataSource_, aSource != null ? aSource.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, delegate != null ? delegate.id : 0);
}

public void setDoubleAction(long /*int*/ aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleAction_, aSelector);
}

public void setDropRow(long /*int*/ row, long /*int*/ op) {
	OS.objc_msgSend(this.id, OS.sel_setDropRow_dropOperation_, row, op);
}

public void setGridStyleMask(long /*int*/ gridStyle) {
	OS.objc_msgSend(this.id, OS.sel_setGridStyleMask_, gridStyle);
}

public void setHeaderView(NSTableHeaderView headerView) {
	OS.objc_msgSend(this.id, OS.sel_setHeaderView_, headerView != null ? headerView.id : 0);
}

public void setHighlightedTableColumn(NSTableColumn tc) {
	OS.objc_msgSend(this.id, OS.sel_setHighlightedTableColumn_, tc != null ? tc.id : 0);
}

public void setIndicatorImage(NSImage anImage, NSTableColumn tc) {
	OS.objc_msgSend(this.id, OS.sel_setIndicatorImage_inTableColumn_, anImage != null ? anImage.id : 0, tc != null ? tc.id : 0);
}

public void setIntercellSpacing(NSSize aSize) {
	OS.objc_msgSend(this.id, OS.sel_setIntercellSpacing_, aSize);
}

public void setRowHeight(double /*float*/ rowHeight) {
	OS.objc_msgSend(this.id, OS.sel_setRowHeight_, rowHeight);
}

public void setUsesAlternatingRowBackgroundColors(boolean useAlternatingRowColors) {
	OS.objc_msgSend(this.id, OS.sel_setUsesAlternatingRowBackgroundColors_, useAlternatingRowColors);
}

public NSArray tableColumns() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_tableColumns);
	return result != 0 ? new NSArray(result) : null;
}

public void tile() {
	OS.objc_msgSend(this.id, OS.sel_tile);
}

public boolean usesAlternatingRowBackgroundColors() {
	return OS.objc_msgSend_bool(this.id, OS.sel_usesAlternatingRowBackgroundColors);
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSTableView, OS.sel_cellClass);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSTableView, OS.sel_setCellClass_, factoryId);
}

}

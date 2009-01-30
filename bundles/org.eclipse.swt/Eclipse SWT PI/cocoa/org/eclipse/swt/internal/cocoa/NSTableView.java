/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

public NSTableView(int /*long*/ id) {
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

public int /*long*/ columnAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_columnAtPoint_, point);
}

public NSIndexSet columnIndexesInRect(NSRect rect) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_columnIndexesInRect_, rect);
	return result != 0 ? new NSIndexSet(result) : null;
}

public int /*long*/ columnWithIdentifier(id identifier) {
	return OS.objc_msgSend(this.id, OS.sel_columnWithIdentifier_, identifier != null ? identifier.id : 0);
}

public void deselectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deselectAll_, sender != null ? sender.id : 0);
}

public void deselectRow(int /*long*/ row) {
	OS.objc_msgSend(this.id, OS.sel_deselectRow_, row);
}

public NSRect frameOfCellAtColumn(int /*long*/ column, int /*long*/ row) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frameOfCellAtColumn_row_, column, row);
	return result;
}

public NSTableHeaderView headerView() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_headerView);
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

public boolean isRowSelected(int /*long*/ row) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isRowSelected_, row);
}

public void moveColumn(int /*long*/ column, int /*long*/ newIndex) {
	OS.objc_msgSend(this.id, OS.sel_moveColumn_toColumn_, column, newIndex);
}

public void noteNumberOfRowsChanged() {
	OS.objc_msgSend(this.id, OS.sel_noteNumberOfRowsChanged);
}

public int /*long*/ numberOfColumns() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfColumns);
}

public int /*long*/ numberOfRows() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfRows);
}

public int /*long*/ numberOfSelectedRows() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfSelectedRows);
}

public NSRect rectOfColumn(int /*long*/ column) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectOfColumn_, column);
	return result;
}

public NSRect rectOfRow(int /*long*/ row) {
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

public int /*long*/ rowAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_rowAtPoint_, point);
}

public float /*double*/ rowHeight() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_rowHeight);
}

public NSRange rowsInRect(NSRect rect) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rowsInRect_, rect);
	return result;
}

public void scrollColumnToVisible(int /*long*/ column) {
	OS.objc_msgSend(this.id, OS.sel_scrollColumnToVisible_, column);
}

public void scrollRowToVisible(int /*long*/ row) {
	OS.objc_msgSend(this.id, OS.sel_scrollRowToVisible_, row);
}

public void selectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectAll_, sender != null ? sender.id : 0);
}

public void selectRowIndexes(NSIndexSet indexes, boolean extend) {
	OS.objc_msgSend(this.id, OS.sel_selectRowIndexes_byExtendingSelection_, indexes != null ? indexes.id : 0, extend);
}

public int /*long*/ selectedRow() {
	return OS.objc_msgSend(this.id, OS.sel_selectedRow);
}

public NSIndexSet selectedRowIndexes() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_selectedRowIndexes);
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

public void setColumnAutoresizingStyle(int /*long*/ style) {
	OS.objc_msgSend(this.id, OS.sel_setColumnAutoresizingStyle_, style);
}

public void setDataSource(id aSource) {
	OS.objc_msgSend(this.id, OS.sel_setDataSource_, aSource != null ? aSource.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, delegate != null ? delegate.id : 0);
}

public void setDoubleAction(int /*long*/ aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleAction_, aSelector);
}

public void setHeaderView(NSTableHeaderView headerView) {
	OS.objc_msgSend(this.id, OS.sel_setHeaderView_, headerView != null ? headerView.id : 0);
}

public void setIntercellSpacing(NSSize aSize) {
	OS.objc_msgSend(this.id, OS.sel_setIntercellSpacing_, aSize);
}

public void setRowHeight(float /*double*/ rowHeight) {
	OS.objc_msgSend(this.id, OS.sel_setRowHeight_, rowHeight);
}

public void setUsesAlternatingRowBackgroundColors(boolean useAlternatingRowColors) {
	OS.objc_msgSend(this.id, OS.sel_setUsesAlternatingRowBackgroundColors_, useAlternatingRowColors);
}

public NSArray tableColumns() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_tableColumns);
	return result != 0 ? new NSArray(result) : null;
}

public void tile() {
	OS.objc_msgSend(this.id, OS.sel_tile);
}

public boolean usesAlternatingRowBackgroundColors() {
	return OS.objc_msgSend_bool(this.id, OS.sel_usesAlternatingRowBackgroundColors);
}

public static int /*long*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSTableView, OS.sel_cellClass);
}

public static void setCellClass(int /*long*/ factoryId) {
	OS.objc_msgSend(OS.class_NSTableView, OS.sel_setCellClass_, factoryId);
}

}

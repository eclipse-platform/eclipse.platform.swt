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

public class NSOutlineView extends NSTableView {

public NSOutlineView() {
	super();
}

public NSOutlineView(int id) {
	super(id);
}

public boolean autoresizesOutlineColumn() {
	return OS.objc_msgSend(this.id, OS.sel_autoresizesOutlineColumn) != 0;
}

public boolean autosaveExpandedItems() {
	return OS.objc_msgSend(this.id, OS.sel_autosaveExpandedItems) != 0;
}

public void collapseItem_(id item) {
	OS.objc_msgSend(this.id, OS.sel_collapseItem_1, item != null ? item.id : 0);
}

public void collapseItem_collapseChildren_(id item, boolean collapseChildren) {
	OS.objc_msgSend(this.id, OS.sel_collapseItem_1collapseChildren_1, item != null ? item.id : 0, collapseChildren);
}

public void expandItem_(id item) {
	OS.objc_msgSend(this.id, OS.sel_expandItem_1, item != null ? item.id : 0);
}

public void expandItem_expandChildren_(id item, boolean expandChildren) {
	OS.objc_msgSend(this.id, OS.sel_expandItem_1expandChildren_1, item != null ? item.id : 0, expandChildren);
}

public NSRect frameOfOutlineCellAtRow(int row) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frameOfOutlineCellAtRow_1, row);
	return result;
}

public boolean indentationMarkerFollowsCell() {
	return OS.objc_msgSend(this.id, OS.sel_indentationMarkerFollowsCell) != 0;
}

public float indentationPerLevel() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_indentationPerLevel);
}

public boolean isExpandable(id item) {
	return OS.objc_msgSend(this.id, OS.sel_isExpandable_1, item != null ? item.id : 0) != 0;
}

public boolean isItemExpanded(id item) {
	return OS.objc_msgSend(this.id, OS.sel_isItemExpanded_1, item != null ? item.id : 0) != 0;
}

public id itemAtRow(int row) {
	int result = OS.objc_msgSend(this.id, OS.sel_itemAtRow_1, row);
	return result != 0 ? new id(result) : null;
}

public int levelForItem(id item) {
	return OS.objc_msgSend(this.id, OS.sel_levelForItem_1, item != null ? item.id : 0);
}

public int levelForRow(int row) {
	return OS.objc_msgSend(this.id, OS.sel_levelForRow_1, row);
}

public NSTableColumn outlineTableColumn() {
	int result = OS.objc_msgSend(this.id, OS.sel_outlineTableColumn);
	return result != 0 ? new NSTableColumn(result) : null;
}

public id parentForItem(id item) {
	int result = OS.objc_msgSend(this.id, OS.sel_parentForItem_1, item != null ? item.id : 0);
	return result != 0 ? new id(result) : null;
}

public void reloadItem_(id item) {
	OS.objc_msgSend(this.id, OS.sel_reloadItem_1, item != null ? item.id : 0);
}

public void reloadItem_reloadChildren_(id item, boolean reloadChildren) {
	OS.objc_msgSend(this.id, OS.sel_reloadItem_1reloadChildren_1, item != null ? item.id : 0, reloadChildren);
}

public int rowForItem(id item) {
	return OS.objc_msgSend(this.id, OS.sel_rowForItem_1, item != null ? item.id : 0);
}

public void setAutoresizesOutlineColumn(boolean resize) {
	OS.objc_msgSend(this.id, OS.sel_setAutoresizesOutlineColumn_1, resize);
}

public void setAutosaveExpandedItems(boolean save) {
	OS.objc_msgSend(this.id, OS.sel_setAutosaveExpandedItems_1, save);
}

public void setDropItem(id item, int index) {
	OS.objc_msgSend(this.id, OS.sel_setDropItem_1dropChildIndex_1, item != null ? item.id : 0, index);
}

public void setIndentationMarkerFollowsCell(boolean drawInCell) {
	OS.objc_msgSend(this.id, OS.sel_setIndentationMarkerFollowsCell_1, drawInCell);
}

public void setIndentationPerLevel(float indentationPerLevel) {
	OS.objc_msgSend(this.id, OS.sel_setIndentationPerLevel_1, indentationPerLevel);
}

public void setOutlineTableColumn(NSTableColumn outlineTableColumn) {
	OS.objc_msgSend(this.id, OS.sel_setOutlineTableColumn_1, outlineTableColumn != null ? outlineTableColumn.id : 0);
}

public boolean shouldCollapseAutoExpandedItemsForDeposited(boolean deposited) {
	return OS.objc_msgSend(this.id, OS.sel_shouldCollapseAutoExpandedItemsForDeposited_1, deposited) != 0;
}

}

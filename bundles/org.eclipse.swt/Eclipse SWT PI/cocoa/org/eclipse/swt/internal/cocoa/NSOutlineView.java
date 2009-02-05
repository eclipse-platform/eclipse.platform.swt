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

public class NSOutlineView extends NSTableView {

public NSOutlineView() {
	super();
}

public NSOutlineView(int /*long*/ id) {
	super(id);
}

public NSOutlineView(id id) {
	super(id);
}

public void collapseItem(id item) {
	OS.objc_msgSend(this.id, OS.sel_collapseItem_, item != null ? item.id : 0);
}

public void expandItem(id item) {
	OS.objc_msgSend(this.id, OS.sel_expandItem_, item != null ? item.id : 0);
}

public float /*double*/ indentationPerLevel() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_indentationPerLevel);
}

public boolean isItemExpanded(id item) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isItemExpanded_, item != null ? item.id : 0);
}

public id itemAtRow(int /*long*/ row) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_itemAtRow_, row);
	return result != 0 ? new id(result) : null;
}

public int /*long*/ levelForItem(id item) {
	return OS.objc_msgSend(this.id, OS.sel_levelForItem_, item != null ? item.id : 0);
}

public NSTableColumn outlineTableColumn() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_outlineTableColumn);
	return result != 0 ? new NSTableColumn(result) : null;
}

public void reloadItem(id item, boolean reloadChildren) {
	OS.objc_msgSend(this.id, OS.sel_reloadItem_reloadChildren_, item != null ? item.id : 0, reloadChildren);
}

public int /*long*/ rowForItem(id item) {
	return OS.objc_msgSend(this.id, OS.sel_rowForItem_, item != null ? item.id : 0);
}

public void setAutoresizesOutlineColumn(boolean resize) {
	OS.objc_msgSend(this.id, OS.sel_setAutoresizesOutlineColumn_, resize);
}

public void setAutosaveExpandedItems(boolean save) {
	OS.objc_msgSend(this.id, OS.sel_setAutosaveExpandedItems_, save);
}

public void setOutlineTableColumn(NSTableColumn outlineTableColumn) {
	OS.objc_msgSend(this.id, OS.sel_setOutlineTableColumn_, outlineTableColumn != null ? outlineTableColumn.id : 0);
}

public static int /*long*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSOutlineView, OS.sel_cellClass);
}

public static void setCellClass(int /*long*/ factoryId) {
	OS.objc_msgSend(OS.class_NSOutlineView, OS.sel_setCellClass_, factoryId);
}

}

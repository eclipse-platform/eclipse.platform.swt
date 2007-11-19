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

public class NSTableColumn extends NSObject {

public NSTableColumn() {
	super();
}

public NSTableColumn(int id) {
	super(id);
}

public id dataCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_dataCell);
	return result != 0 ? new id(result) : null;
}

public id dataCellForRow(int row) {
	int result = OS.objc_msgSend(this.id, OS.sel_dataCellForRow_1, row);
	return result != 0 ? new id(result) : null;
}

public NSTableHeaderCell headerCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_headerCell);
	return result != 0 ? new NSTableHeaderCell(result) : null;
}

public NSString headerToolTip() {
	int result = OS.objc_msgSend(this.id, OS.sel_headerToolTip);
	return result != 0 ? new NSString(result) : null;
}

public id identifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_identifier);
	return result != 0 ? new id(result) : null;
}

public NSTableColumn initWithIdentifier(id identifier) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithIdentifier_1, identifier != null ? identifier.id : 0);
	return result != 0 ? this : null;
}

public boolean isEditable() {
	return OS.objc_msgSend(this.id, OS.sel_isEditable) != 0;
}

public boolean isHidden() {
	return OS.objc_msgSend(this.id, OS.sel_isHidden) != 0;
}

public boolean isResizable() {
	return OS.objc_msgSend(this.id, OS.sel_isResizable) != 0;
}

public float maxWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_maxWidth);
}

public float minWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_minWidth);
}

public int resizingMask() {
	return OS.objc_msgSend(this.id, OS.sel_resizingMask);
}

public void setDataCell(NSCell cell) {
	OS.objc_msgSend(this.id, OS.sel_setDataCell_1, cell != null ? cell.id : 0);
}

public void setEditable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_1, flag);
}

public void setHeaderCell(NSCell cell) {
	OS.objc_msgSend(this.id, OS.sel_setHeaderCell_1, cell != null ? cell.id : 0);
}

public void setHeaderToolTip(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setHeaderToolTip_1, string != null ? string.id : 0);
}

public void setHidden(boolean hidden) {
	OS.objc_msgSend(this.id, OS.sel_setHidden_1, hidden);
}

public void setIdentifier(id identifier) {
	OS.objc_msgSend(this.id, OS.sel_setIdentifier_1, identifier != null ? identifier.id : 0);
}

public void setMaxWidth(float maxWidth) {
	OS.objc_msgSend(this.id, OS.sel_setMaxWidth_1, maxWidth);
}

public void setMinWidth(float minWidth) {
	OS.objc_msgSend(this.id, OS.sel_setMinWidth_1, minWidth);
}

public void setResizable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setResizable_1, flag);
}

public void setResizingMask(int resizingMask) {
	OS.objc_msgSend(this.id, OS.sel_setResizingMask_1, resizingMask);
}

public void setSortDescriptorPrototype(NSSortDescriptor sortDescriptor) {
	OS.objc_msgSend(this.id, OS.sel_setSortDescriptorPrototype_1, sortDescriptor != null ? sortDescriptor.id : 0);
}

public void setTableView(NSTableView tableView) {
	OS.objc_msgSend(this.id, OS.sel_setTableView_1, tableView != null ? tableView.id : 0);
}

public void setWidth(float width) {
	OS.objc_msgSend(this.id, OS.sel_setWidth_1, width);
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public NSSortDescriptor sortDescriptorPrototype() {
	int result = OS.objc_msgSend(this.id, OS.sel_sortDescriptorPrototype);
	return result != 0 ? new NSSortDescriptor(result) : null;
}

public NSTableView tableView() {
	int result = OS.objc_msgSend(this.id, OS.sel_tableView);
	return result != 0 ? new NSTableView(result) : null;
}

public float width() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_width);
}

}

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

public class NSTableColumn extends NSObject {

public NSTableColumn() {
	super();
}

public NSTableColumn(long id) {
	super(id);
}

public NSTableColumn(id id) {
	super(id);
}

public NSCell dataCell() {
	long result = OS.objc_msgSend(this.id, OS.sel_dataCell);
	return result != 0 ? new NSCell(result) : null;
}

public NSTableHeaderCell headerCell() {
	long result = OS.objc_msgSend(this.id, OS.sel_headerCell);
	return result != 0 ? new NSTableHeaderCell(result) : null;
}

public NSTableColumn initWithIdentifier(NSString identifier) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithIdentifier_, identifier != null ? identifier.id : 0);
	return result == this.id ? this : (result != 0 ? new NSTableColumn(result) : null);
}

public long resizingMask() {
	return OS.objc_msgSend(this.id, OS.sel_resizingMask);
}

public void setDataCell(id dataCell) {
	OS.objc_msgSend(this.id, OS.sel_setDataCell_, dataCell != null ? dataCell.id : 0);
}

public void setEditable(boolean editable) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_, editable);
}

public void setHeaderCell(NSTableHeaderCell headerCell) {
	OS.objc_msgSend(this.id, OS.sel_setHeaderCell_, headerCell != null ? headerCell.id : 0);
}

public void setIdentifier(NSString identifier) {
	OS.objc_msgSend(this.id, OS.sel_setIdentifier_, identifier != null ? identifier.id : 0);
}

public void setMinWidth(double minWidth) {
	OS.objc_msgSend(this.id, OS.sel_setMinWidth_, minWidth);
}

public void setResizingMask(long resizingMask) {
	OS.objc_msgSend(this.id, OS.sel_setResizingMask_, resizingMask);
}

public void setWidth(double width) {
	OS.objc_msgSend(this.id, OS.sel_setWidth_, width);
}

public double width() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_width);
}

}

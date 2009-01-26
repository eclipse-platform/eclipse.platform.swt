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

public class NSTableColumn extends NSObject {

public NSTableColumn() {
	super();
}

public NSTableColumn(int /*long*/ id) {
	super(id);
}

public NSTableColumn(id id) {
	super(id);
}

public NSCell dataCell() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_dataCell);
	return result != 0 ? new NSCell(result) : null;
}

public NSTableHeaderCell headerCell() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_headerCell);
	return result != 0 ? new NSTableHeaderCell(result) : null;
}

public id initWithIdentifier(id identifier) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithIdentifier_, identifier != null ? identifier.id : 0);
	return result != 0 ? new id(result) : null;
}

public int /*long*/ resizingMask() {
	return OS.objc_msgSend(this.id, OS.sel_resizingMask);
}

public void setDataCell(NSCell cell) {
	OS.objc_msgSend(this.id, OS.sel_setDataCell_, cell != null ? cell.id : 0);
}

public void setEditable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_, flag);
}

public void setHeaderCell(NSCell cell) {
	OS.objc_msgSend(this.id, OS.sel_setHeaderCell_, cell != null ? cell.id : 0);
}

public void setIdentifier(id identifier) {
	OS.objc_msgSend(this.id, OS.sel_setIdentifier_, identifier != null ? identifier.id : 0);
}

public void setMinWidth(float /*double*/ minWidth) {
	OS.objc_msgSend(this.id, OS.sel_setMinWidth_, minWidth);
}

public void setResizingMask(int /*long*/ resizingMask) {
	OS.objc_msgSend(this.id, OS.sel_setResizingMask_, resizingMask);
}

public void setWidth(float /*double*/ width) {
	OS.objc_msgSend(this.id, OS.sel_setWidth_, width);
}

public float /*double*/ width() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_width);
}

}

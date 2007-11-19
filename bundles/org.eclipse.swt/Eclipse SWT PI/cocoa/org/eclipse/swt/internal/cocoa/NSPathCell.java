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

public class NSPathCell extends NSActionCell {

public NSPathCell() {
	super();
}

public NSPathCell(int id) {
	super(id);
}

public NSURL URL() {
	int result = OS.objc_msgSend(this.id, OS.sel_URL);
	return result != 0 ? new NSURL(result) : null;
}

public NSArray allowedTypes() {
	int result = OS.objc_msgSend(this.id, OS.sel_allowedTypes);
	return result != 0 ? new NSArray(result) : null;
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSPathComponentCell clickedPathComponentCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_clickedPathComponentCell);
	return result != 0 ? new NSPathComponentCell(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public int doubleAction() {
	return OS.objc_msgSend(this.id, OS.sel_doubleAction);
}

public void mouseEntered(NSEvent event, NSRect frame, NSView view) {
	OS.objc_msgSend(this.id, OS.sel_mouseEntered_1withFrame_1inView_1, event != null ? event.id : 0, frame, view != null ? view.id : 0);
}

public void mouseExited(NSEvent event, NSRect frame, NSView view) {
	OS.objc_msgSend(this.id, OS.sel_mouseExited_1withFrame_1inView_1, event != null ? event.id : 0, frame, view != null ? view.id : 0);
}

public NSPathComponentCell pathComponentCellAtPoint(NSPoint point, NSRect frame, NSView view) {
	int result = OS.objc_msgSend(this.id, OS.sel_pathComponentCellAtPoint_1withFrame_1inView_1, point, frame, view != null ? view.id : 0);
	return result != 0 ? new NSPathComponentCell(result) : null;
}

public static int pathComponentCellClass() {
	return OS.objc_msgSend(OS.class_NSPathCell, OS.sel_pathComponentCellClass);
}

public NSArray pathComponentCells() {
	int result = OS.objc_msgSend(this.id, OS.sel_pathComponentCells);
	return result != 0 ? new NSArray(result) : null;
}

public int pathStyle() {
	return OS.objc_msgSend(this.id, OS.sel_pathStyle);
}

public NSAttributedString placeholderAttributedString() {
	int result = OS.objc_msgSend(this.id, OS.sel_placeholderAttributedString);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSString placeholderString() {
	int result = OS.objc_msgSend(this.id, OS.sel_placeholderString);
	return result != 0 ? new NSString(result) : null;
}

public NSRect rectOfPathComponentCell(NSPathComponentCell cell, NSRect frame, NSView view) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectOfPathComponentCell_1withFrame_1inView_1, cell != null ? cell.id : 0, frame, view != null ? view.id : 0);
	return result;
}

public void setAllowedTypes(NSArray allowedTypes) {
	OS.objc_msgSend(this.id, OS.sel_setAllowedTypes_1, allowedTypes != null ? allowedTypes.id : 0);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setControlSize(int size) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_1, size);
}

public void setDelegate(id value) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, value != null ? value.id : 0);
}

public void setDoubleAction(int action) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleAction_1, action);
}

public void setObjectValue(id  obj) {
	OS.objc_msgSend(this.id, OS.sel_setObjectValue_1, obj != null ? obj.id : 0);
}

public void setPathComponentCells(NSArray cells) {
	OS.objc_msgSend(this.id, OS.sel_setPathComponentCells_1, cells != null ? cells.id : 0);
}

public void setPathStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setPathStyle_1, style);
}

public void setPlaceholderAttributedString(NSAttributedString string) {
	OS.objc_msgSend(this.id, OS.sel_setPlaceholderAttributedString_1, string != null ? string.id : 0);
}

public void setPlaceholderString(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setPlaceholderString_1, string != null ? string.id : 0);
}

public void setURL(NSURL url) {
	OS.objc_msgSend(this.id, OS.sel_setURL_1, url != null ? url.id : 0);
}

}

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

public class NSForm extends NSMatrix {

public NSForm() {
	super();
}

public NSForm(int id) {
	super(id);
}

public NSFormCell addEntry(NSString title) {
	int result = OS.objc_msgSend(this.id, OS.sel_addEntry_1, title != null ? title.id : 0);
	return result != 0 ? new NSFormCell(result) : null;
}

public id cellAtIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_cellAtIndex_1, index);
	return result != 0 ? new id(result) : null;
}

public void drawCellAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_drawCellAtIndex_1, index);
}

public int indexOfCellWithTag(int aTag) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfCellWithTag_1, aTag);
}

public int indexOfSelectedItem() {
	return OS.objc_msgSend(this.id, OS.sel_indexOfSelectedItem);
}

public NSFormCell insertEntry(NSString title, int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_insertEntry_1atIndex_1, title != null ? title.id : 0, index);
	return result != 0 ? new NSFormCell(result) : null;
}

public void removeEntryAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removeEntryAtIndex_1, index);
}

public void selectTextAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_selectTextAtIndex_1, index);
}

public void setBezeled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBezeled_1, flag);
}

public void setBordered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBordered_1, flag);
}

public void setEntryWidth(float width) {
	OS.objc_msgSend(this.id, OS.sel_setEntryWidth_1, width);
}

public void setFrameSize(NSSize newSize) {
	OS.objc_msgSend(this.id, OS.sel_setFrameSize_1, newSize);
}

public void setInterlineSpacing(float spacing) {
	OS.objc_msgSend(this.id, OS.sel_setInterlineSpacing_1, spacing);
}

public void setTextAlignment(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setTextAlignment_1, mode);
}

public void setTextBaseWritingDirection(int writingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setTextBaseWritingDirection_1, writingDirection);
}

public void setTextFont(NSFont fontObj) {
	OS.objc_msgSend(this.id, OS.sel_setTextFont_1, fontObj != null ? fontObj.id : 0);
}

public void setTitleAlignment(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setTitleAlignment_1, mode);
}

public void setTitleBaseWritingDirection(int writingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setTitleBaseWritingDirection_1, writingDirection);
}

public void setTitleFont(NSFont fontObj) {
	OS.objc_msgSend(this.id, OS.sel_setTitleFont_1, fontObj != null ? fontObj.id : 0);
}

}

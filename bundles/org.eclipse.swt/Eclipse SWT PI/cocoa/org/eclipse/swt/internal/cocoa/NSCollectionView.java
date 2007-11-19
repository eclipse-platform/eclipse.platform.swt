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

public class NSCollectionView extends NSView {

public NSCollectionView() {
	super();
}

public NSCollectionView(int id) {
	super(id);
}

public boolean allowsMultipleSelection() {
	return OS.objc_msgSend(this.id, OS.sel_allowsMultipleSelection) != 0;
}

public NSArray backgroundColors() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColors);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray content() {
	int result = OS.objc_msgSend(this.id, OS.sel_content);
	return result != 0 ? new NSArray(result) : null;
}

public boolean isFirstResponder() {
	return OS.objc_msgSend(this.id, OS.sel_isFirstResponder) != 0;
}

public boolean isSelectable() {
	return OS.objc_msgSend(this.id, OS.sel_isSelectable) != 0;
}

public NSCollectionViewItem itemPrototype() {
	int result = OS.objc_msgSend(this.id, OS.sel_itemPrototype);
	return result != 0 ? new NSCollectionViewItem(result) : null;
}

public NSSize maxItemSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_maxItemSize);
	return result;
}

public int maxNumberOfColumns() {
	return OS.objc_msgSend(this.id, OS.sel_maxNumberOfColumns);
}

public int maxNumberOfRows() {
	return OS.objc_msgSend(this.id, OS.sel_maxNumberOfRows);
}

public NSSize minItemSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_minItemSize);
	return result;
}

public NSCollectionViewItem newItemForRepresentedObject(id object) {
	int result = OS.objc_msgSend(this.id, OS.sel_newItemForRepresentedObject_1, object != null ? object.id : 0);
	return result != 0 ? new NSCollectionViewItem(result) : null;
}

public NSIndexSet selectionIndexes() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectionIndexes);
	return result != 0 ? new NSIndexSet(result) : null;
}

public void setAllowsMultipleSelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMultipleSelection_1, flag);
}

public void setBackgroundColors(NSArray colors) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColors_1, colors != null ? colors.id : 0);
}

public void setContent(NSArray content) {
	OS.objc_msgSend(this.id, OS.sel_setContent_1, content != null ? content.id : 0);
}

public void setItemPrototype(NSCollectionViewItem prototype) {
	OS.objc_msgSend(this.id, OS.sel_setItemPrototype_1, prototype != null ? prototype.id : 0);
}

public void setMaxItemSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setMaxItemSize_1, size);
}

public void setMaxNumberOfColumns(int number) {
	OS.objc_msgSend(this.id, OS.sel_setMaxNumberOfColumns_1, number);
}

public void setMaxNumberOfRows(int number) {
	OS.objc_msgSend(this.id, OS.sel_setMaxNumberOfRows_1, number);
}

public void setMinItemSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setMinItemSize_1, size);
}

public void setSelectable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectable_1, flag);
}

public void setSelectionIndexes(NSIndexSet indexes) {
	OS.objc_msgSend(this.id, OS.sel_setSelectionIndexes_1, indexes != null ? indexes.id : 0);
}

}

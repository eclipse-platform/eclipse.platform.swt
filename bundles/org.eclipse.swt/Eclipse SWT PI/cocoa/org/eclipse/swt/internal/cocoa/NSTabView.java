/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTabView extends NSView {

public NSTabView() {
	super();
}

public NSTabView(long /*int*/ id) {
	super(id);
}

public NSTabView(id id) {
	super(id);
}

public void addTabViewItem(NSTabViewItem tabViewItem) {
	OS.objc_msgSend(this.id, OS.sel_addTabViewItem_, tabViewItem != null ? tabViewItem.id : 0);
}

public NSRect contentRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentRect);
	return result;
}

public void insertTabViewItem(NSTabViewItem tabViewItem, long /*int*/ index) {
	OS.objc_msgSend(this.id, OS.sel_insertTabViewItem_atIndex_, tabViewItem != null ? tabViewItem.id : 0, index);
}

public NSSize minimumSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_minimumSize);
	return result;
}

public void removeTabViewItem(NSTabViewItem tabViewItem) {
	OS.objc_msgSend(this.id, OS.sel_removeTabViewItem_, tabViewItem != null ? tabViewItem.id : 0);
}

public void selectTabViewItemAtIndex(long /*int*/ index) {
	OS.objc_msgSend(this.id, OS.sel_selectTabViewItemAtIndex_, index);
}

public NSTabViewItem selectedTabViewItem() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_selectedTabViewItem);
	return result != 0 ? new NSTabViewItem(result) : null;
}

public void setControlSize(long /*int*/ controlSize) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_, controlSize);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, anObject != null ? anObject.id : 0);
}

public void setFont(NSFont font) {
	OS.objc_msgSend(this.id, OS.sel_setFont_, font != null ? font.id : 0);
}

public void setTabViewType(long /*int*/ tabViewType) {
	OS.objc_msgSend(this.id, OS.sel_setTabViewType_, tabViewType);
}

public NSTabViewItem tabViewItemAtPoint(NSPoint point) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_tabViewItemAtPoint_, point);
	return result != 0 ? new NSTabViewItem(result) : null;
}

}

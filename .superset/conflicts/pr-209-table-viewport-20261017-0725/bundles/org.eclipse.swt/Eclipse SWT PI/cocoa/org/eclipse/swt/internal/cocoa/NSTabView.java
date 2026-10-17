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

public class NSTabView extends NSView {

public NSTabView() {
	super();
}

public NSTabView(long id) {
	super(id);
}

public NSTabView(id id) {
	super(id);
}

public NSRect contentRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentRect);
	return result;
}

public void insertTabViewItem(NSTabViewItem tabViewItem, long index) {
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

public void selectTabViewItemAtIndex(long index) {
	OS.objc_msgSend(this.id, OS.sel_selectTabViewItemAtIndex_, index);
}

public NSTabViewItem selectedTabViewItem() {
	long result = OS.objc_msgSend(this.id, OS.sel_selectedTabViewItem);
	return result != 0 ? new NSTabViewItem(result) : null;
}

public void setControlSize(long controlSize) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_, controlSize);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, delegate != null ? delegate.id : 0);
}

public void setFont(NSFont font) {
	OS.objc_msgSend(this.id, OS.sel_setFont_, font != null ? font.id : 0);
}

public void setTabViewType(long tabViewType) {
	OS.objc_msgSend(this.id, OS.sel_setTabViewType_, tabViewType);
}

public NSTabViewItem tabViewItemAtPoint(NSPoint point) {
	long result = OS.objc_msgSend(this.id, OS.sel_tabViewItemAtPoint_, point);
	return result != 0 ? new NSTabViewItem(result) : null;
}

}

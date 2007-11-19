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

public class NSDrawer extends NSResponder {

public NSDrawer() {
	super();
}

public NSDrawer(int id) {
	super(id);
}

public void close() {
	OS.objc_msgSend(this.id, OS.sel_close);
}

public void close_(id sender) {
	OS.objc_msgSend(this.id, OS.sel_close_1, sender != null ? sender.id : 0);
}

public NSSize contentSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentSize);
	return result;
}

public NSView contentView() {
	int result = OS.objc_msgSend(this.id, OS.sel_contentView);
	return result != 0 ? new NSView(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public int edge() {
	return OS.objc_msgSend(this.id, OS.sel_edge);
}

public NSDrawer initWithContentSize(NSSize contentSize, int edge) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentSize_1preferredEdge_1, contentSize, edge);
	return result != 0 ? this : null;
}

public float leadingOffset() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_leadingOffset);
}

public NSSize maxContentSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_maxContentSize);
	return result;
}

public NSSize minContentSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_minContentSize);
	return result;
}

public void open() {
	OS.objc_msgSend(this.id, OS.sel_open);
}

public void open_(id sender) {
	OS.objc_msgSend(this.id, OS.sel_open_1, sender != null ? sender.id : 0);
}

public void openOnEdge(int edge) {
	OS.objc_msgSend(this.id, OS.sel_openOnEdge_1, edge);
}

public NSWindow parentWindow() {
	int result = OS.objc_msgSend(this.id, OS.sel_parentWindow);
	return result != 0 ? new NSWindow(result) : null;
}

public int preferredEdge() {
	return OS.objc_msgSend(this.id, OS.sel_preferredEdge);
}

public void setContentSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setContentSize_1, size);
}

public void setContentView(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_setContentView_1, aView != null ? aView.id : 0);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setLeadingOffset(float offset) {
	OS.objc_msgSend(this.id, OS.sel_setLeadingOffset_1, offset);
}

public void setMaxContentSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setMaxContentSize_1, size);
}

public void setMinContentSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setMinContentSize_1, size);
}

public void setParentWindow(NSWindow parent) {
	OS.objc_msgSend(this.id, OS.sel_setParentWindow_1, parent != null ? parent.id : 0);
}

public void setPreferredEdge(int edge) {
	OS.objc_msgSend(this.id, OS.sel_setPreferredEdge_1, edge);
}

public void setTrailingOffset(float offset) {
	OS.objc_msgSend(this.id, OS.sel_setTrailingOffset_1, offset);
}

public int state() {
	return OS.objc_msgSend(this.id, OS.sel_state);
}

public void toggle(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggle_1, sender != null ? sender.id : 0);
}

public float trailingOffset() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_trailingOffset);
}

}

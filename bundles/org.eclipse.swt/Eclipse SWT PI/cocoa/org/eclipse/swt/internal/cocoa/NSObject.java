/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSObject extends id {

public NSObject() {
	super();
}

public NSObject(long /*int*/ id) {
	super(id);
}

public NSObject(id id) {
	super(id);
}

public NSObject alloc() {
	this.id = OS.objc_msgSend(objc_getClass(), OS.sel_alloc);
	return this;
}

public id accessibilityAttributeValue(NSString attribute, id parameter) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_accessibilityAttributeValue_forParameter_, attribute != null ? attribute.id : 0, parameter != null ? parameter.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean accessibilitySetOverrideValue(id value, NSString attribute) {
	return OS.objc_msgSend_bool(this.id, OS.sel_accessibilitySetOverrideValue_forAttribute_, value != null ? value.id : 0, attribute != null ? attribute.id : 0);
}

public void draggedImage(NSImage image, NSPoint screenPoint, long /*int*/ operation) {
	OS.objc_msgSend(this.id, OS.sel_draggedImage_endedAt_operation_, image != null ? image.id : 0, screenPoint, operation);
}

public NSWindow draggingDestinationWindow() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_draggingDestinationWindow);
	return result != 0 ? new NSWindow(result) : null;
}

public NSPoint draggingLocation() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_draggingLocation);
	return result;
}

public NSPasteboard draggingPasteboard() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_draggingPasteboard);
	return result != 0 ? new NSPasteboard(result) : null;
}

public long /*int*/ draggingSourceOperationMask() {
	return OS.objc_msgSend(this.id, OS.sel_draggingSourceOperationMask);
}

public boolean outlineView(NSOutlineView outlineView, NSTableColumn tableColumn, id item) {
	return OS.objc_msgSend_bool(this.id, OS.sel_outlineView_shouldEditTableColumn_item_, outlineView != null ? outlineView.id : 0, tableColumn != null ? tableColumn.id : 0, item != null ? item.id : 0);
}

public boolean outlineView(NSOutlineView outlineView, long /*int*/ columnIndex, long /*int*/ newColumnIndex) {
	return OS.objc_msgSend_bool(this.id, OS.sel_outlineView_shouldReorderColumn_toColumn_, outlineView != null ? outlineView.id : 0, columnIndex, newColumnIndex);
}

public boolean outlineView(NSOutlineView outlineView, id item) {
	return OS.objc_msgSend_bool(this.id, OS.sel_outlineView_shouldSelectItem_, outlineView != null ? outlineView.id : 0, item != null ? item.id : 0);
}

public boolean outlineView(NSOutlineView outlineView, NSCell cell, NSTableColumn tableColumn, id item) {
	return OS.objc_msgSend_bool(this.id, OS.sel_outlineView_shouldTrackCell_forTableColumn_item_, outlineView != null ? outlineView.id : 0, cell != null ? cell.id : 0, tableColumn != null ? tableColumn.id : 0, item != null ? item.id : 0);
}

public boolean readSelectionFromPasteboard(NSPasteboard pboard) {
	return OS.objc_msgSend_bool(this.id, OS.sel_readSelectionFromPasteboard_, pboard != null ? pboard.id : 0);
}

public boolean tableView(NSTableView tableView, long /*int*/ columnIndex, long /*int*/ newColumnIndex) {
	return OS.objc_msgSend_bool(this.id, OS.sel_tableView_shouldReorderColumn_toColumn_, tableView != null ? tableView.id : 0, columnIndex, newColumnIndex);
}

public boolean tableView(NSTableView tableView, long /*int*/ row) {
	return OS.objc_msgSend_bool(this.id, OS.sel_tableView_shouldSelectRow_, tableView != null ? tableView.id : 0, row);
}

public boolean tableView(NSTableView tableView, NSCell cell, NSTableColumn tableColumn, long /*int*/ row) {
	return OS.objc_msgSend_bool(this.id, OS.sel_tableView_shouldTrackCell_forTableColumn_row_, tableView != null ? tableView.id : 0, cell != null ? cell.id : 0, tableColumn != null ? tableColumn.id : 0, row);
}

public boolean writeSelectionToPasteboard(NSPasteboard pboard, NSArray types) {
	return OS.objc_msgSend_bool(this.id, OS.sel_writeSelectionToPasteboard_types_, pboard != null ? pboard.id : 0, types != null ? types.id : 0);
}

public NSObject autorelease() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_autorelease);
	return result == this.id ? this : (result != 0 ? new NSObject(result) : null);
}

public void cancelAuthenticationChallenge(NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_cancelAuthenticationChallenge_, challenge != null ? challenge.id : 0);
}

public NSString className() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_className);
	return result != 0 ? new NSString(result) : null;
}

public boolean conformsToProtocol(Protocol aProtocol) {
	return OS.objc_msgSend_bool(this.id, OS.sel_conformsToProtocol_, aProtocol != null ? aProtocol.id : 0);
}

public id copy() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_copy);
	return result != 0 ? new id(result) : null;
}

public NSString description() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_description);
	return result != 0 ? new NSString(result) : null;
}

public NSObject init() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_init);
	return result == this.id ? this : (result != 0 ? new NSObject(result) : null);
}

public boolean isEqual(id object) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isEqual_, object != null ? object.id : 0);
}

public boolean isEqualTo(id object) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isEqualTo_, object != null ? object.id : 0);
}

public boolean isKindOfClass(long /*int*/ aClass) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isKindOfClass_, aClass);
}

public id mutableCopy() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_mutableCopy);
	return result != 0 ? new id(result) : null;
}

public void performSelector(long /*int*/ aSelector, id anArgument, double delay, NSArray modes) {
	OS.objc_msgSend(this.id, OS.sel_performSelector_withObject_afterDelay_inModes_, aSelector, anArgument != null ? anArgument.id : 0, delay, modes != null ? modes.id : 0);
}

public void performSelectorOnMainThread(long /*int*/ aSelector, id arg, boolean wait) {
	OS.objc_msgSend(this.id, OS.sel_performSelectorOnMainThread_withObject_waitUntilDone_, aSelector, arg != null ? arg.id : 0, wait);
}

public void release() {
	OS.objc_msgSend(this.id, OS.sel_release);
}

public boolean respondsToSelector(long /*int*/ aSelector) {
	return OS.objc_msgSend_bool(this.id, OS.sel_respondsToSelector_, aSelector);
}

public id retain() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_retain);
	return result != 0 ? new id(result) : null;
}

public long /*int*/ retainCount() {
	return OS.objc_msgSend(this.id, OS.sel_retainCount);
}

public void setValue(id value, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_setValue_forKey_, value != null ? value.id : 0, key != null ? key.id : 0);
}

public long /*int*/ superclass() {
	return OS.objc_msgSend(this.id, OS.sel_superclass);
}

public void useCredential(NSURLCredential credential, NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_useCredential_forAuthenticationChallenge_, credential != null ? credential.id : 0, challenge != null ? challenge.id : 0);
}

public id valueForKey(NSString key) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_valueForKey_, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public void addEventListener(NSString type, id listener, boolean useCapture) {
	OS.objc_msgSend(this.id, OS.sel_addEventListener_listener_useCapture_, type != null ? type.id : 0, listener != null ? listener.id : 0, useCapture);
}

public void handleEvent(DOMEvent evt) {
	OS.objc_msgSend(this.id, OS.sel_handleEvent_, evt != null ? evt.id : 0);
}

}

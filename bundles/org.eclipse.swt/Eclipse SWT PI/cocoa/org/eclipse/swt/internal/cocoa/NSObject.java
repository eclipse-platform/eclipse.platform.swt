/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

public NSObject(int /*long*/ id) {
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
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_accessibilityAttributeValue_forParameter_, attribute != null ? attribute.id : 0, parameter != null ? parameter.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean accessibilitySetOverrideValue(id value, NSString attribute) {
	return OS.objc_msgSend_bool(this.id, OS.sel_accessibilitySetOverrideValue_forAttribute_, value != null ? value.id : 0, attribute != null ? attribute.id : 0);
}

public void draggedImage(NSImage image, NSPoint screenPoint, int /*long*/ operation) {
	OS.objc_msgSend(this.id, OS.sel_draggedImage_endedAt_operation_, image != null ? image.id : 0, screenPoint, operation);
}

public NSWindow draggingDestinationWindow() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_draggingDestinationWindow);
	return result != 0 ? new NSWindow(result) : null;
}

public NSPoint draggingLocation() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_draggingLocation);
	return result;
}

public NSPasteboard draggingPasteboard() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_draggingPasteboard);
	return result != 0 ? new NSPasteboard(result) : null;
}

public int /*long*/ draggingSourceOperationMask() {
	return OS.objc_msgSend(this.id, OS.sel_draggingSourceOperationMask);
}

public boolean readSelectionFromPasteboard(NSPasteboard pboard) {
	return OS.objc_msgSend_bool(this.id, OS.sel_readSelectionFromPasteboard_, pboard != null ? pboard.id : 0);
}

public boolean writeSelectionToPasteboard(NSPasteboard pboard, NSArray types) {
	return OS.objc_msgSend_bool(this.id, OS.sel_writeSelectionToPasteboard_types_, pboard != null ? pboard.id : 0, types != null ? types.id : 0);
}

public NSObject autorelease() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_autorelease);
	return result == this.id ? this : (result != 0 ? new NSObject(result) : null);
}

public void cancelAuthenticationChallenge(NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_cancelAuthenticationChallenge_, challenge != null ? challenge.id : 0);
}

public NSString className() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_className);
	return result != 0 ? new NSString(result) : null;
}

public boolean conformsToProtocol(Protocol aProtocol) {
	return OS.objc_msgSend_bool(this.id, OS.sel_conformsToProtocol_, aProtocol != null ? aProtocol.id : 0);
}

public id copy() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_copy);
	return result != 0 ? new id(result) : null;
}

public NSString description() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_description);
	return result != 0 ? new NSString(result) : null;
}

public NSObject init() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_init);
	return result == this.id ? this : (result != 0 ? new NSObject(result) : null);
}

public boolean isEqual(id object) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isEqual_, object != null ? object.id : 0);
}

public boolean isEqualTo(id object) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isEqualTo_, object != null ? object.id : 0);
}

public boolean isKindOfClass(int /*long*/ aClass) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isKindOfClass_, aClass);
}

public id mutableCopy() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_mutableCopy);
	return result != 0 ? new id(result) : null;
}

public void performSelector(int /*long*/ aSelector, id anArgument, double delay, NSArray modes) {
	OS.objc_msgSend(this.id, OS.sel_performSelector_withObject_afterDelay_inModes_, aSelector, anArgument != null ? anArgument.id : 0, delay, modes != null ? modes.id : 0);
}

public void performSelectorOnMainThread(int /*long*/ aSelector, id arg, boolean wait) {
	OS.objc_msgSend(this.id, OS.sel_performSelectorOnMainThread_withObject_waitUntilDone_, aSelector, arg != null ? arg.id : 0, wait);
}

public void release() {
	OS.objc_msgSend(this.id, OS.sel_release);
}

public boolean respondsToSelector(int /*long*/ aSelector) {
	return OS.objc_msgSend_bool(this.id, OS.sel_respondsToSelector_, aSelector);
}

public id retain() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_retain);
	return result != 0 ? new id(result) : null;
}

public int /*long*/ retainCount() {
	return OS.objc_msgSend(this.id, OS.sel_retainCount);
}

public void setValue(id value, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_setValue_forKey_, value != null ? value.id : 0, key != null ? key.id : 0);
}

public int /*long*/ superclass() {
	return OS.objc_msgSend(this.id, OS.sel_superclass);
}

public void useCredential(NSURLCredential credential, NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_useCredential_forAuthenticationChallenge_, credential != null ? credential.id : 0, challenge != null ? challenge.id : 0);
}

public id valueForKey(NSString key) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_valueForKey_, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public void addEventListener(NSString type, id listener, boolean useCapture) {
	OS.objc_msgSend(this.id, OS.sel_addEventListener_listener_useCapture_, type != null ? type.id : 0, listener != null ? listener.id : 0, useCapture);
}

public void handleEvent(DOMEvent evt) {
	OS.objc_msgSend(this.id, OS.sel_handleEvent_, evt != null ? evt.id : 0);
}

}

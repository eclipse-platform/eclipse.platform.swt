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

public id autorelease() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_autorelease);
	return result != 0 ? new id(result) : null;
}

public boolean conformsToProtocol(Protocol aProtocol) {
	return OS.objc_msgSend(this.id, OS.sel_conformsToProtocol_, aProtocol != null ? aProtocol.id : 0) != 0;
}

public id copy() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_copy);
	return result != 0 ? new id(result) : null;
}

public NSObject init() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_init);
	return result == this.id ? this : (result != 0 ? new NSObject(result) : null);
}

public boolean isEqual(id object) {
	return OS.objc_msgSend(this.id, OS.sel_isEqual_, object != null ? object.id : 0) != 0;
}

public boolean isEqualTo(id object) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualTo_, object != null ? object.id : 0) != 0;
}

public boolean isKindOfClass(int /*long*/ aClass) {
	return OS.objc_msgSend(this.id, OS.sel_isKindOfClass_, aClass) != 0;
}

public void performSelectorOnMainThread(int /*long*/ aSelector, id arg, boolean wait) {
	OS.objc_msgSend(this.id, OS.sel_performSelectorOnMainThread_withObject_waitUntilDone_, aSelector, arg != null ? arg.id : 0, wait);
}

public void release() {
	OS.objc_msgSend(this.id, OS.sel_release);
}

public boolean respondsToSelector(int /*long*/ aSelector) {
	return OS.objc_msgSend(this.id, OS.sel_respondsToSelector_, aSelector) != 0;
}

public id retain() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_retain);
	return result != 0 ? new id(result) : null;
}

public int /*long*/ retainCount() {
	return OS.objc_msgSend(this.id, OS.sel_retainCount);
}

public int /*long*/ superclass() {
	return OS.objc_msgSend(this.id, OS.sel_superclass);
}

public void addEventListener(NSString type, id  listener, boolean useCapture) {
	OS.objc_msgSend(this.id, OS.sel_addEventListener_listener_useCapture_, type != null ? type.id : 0, listener != null ? listener.id : 0, useCapture);
}

public void handleEvent(DOMEvent evt) {
	OS.objc_msgSend(this.id, OS.sel_handleEvent_, evt != null ? evt.id : 0);
}

}

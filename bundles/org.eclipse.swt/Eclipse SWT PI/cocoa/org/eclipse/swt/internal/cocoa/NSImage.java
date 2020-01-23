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

public class NSImage extends NSObject {

public NSImage() {
	super();
}

public NSImage(long id) {
	super(id);
}

public NSImage(id id) {
	super(id);
}

public NSData TIFFRepresentation() {
	long result = OS.objc_msgSend(this.id, OS.sel_TIFFRepresentation);
	return result != 0 ? new NSData(result) : null;
}

public void addRepresentation(NSImageRep imageRep) {
	OS.objc_msgSend(this.id, OS.sel_addRepresentation_, imageRep != null ? imageRep.id : 0);
}

public NSImageRep bestRepresentationForDevice(NSDictionary deviceDescription) {
	long result = OS.objc_msgSend(this.id, OS.sel_bestRepresentationForDevice_, deviceDescription != null ? deviceDescription.id : 0);
	return result != 0 ? new NSImageRep(result) : null;
}

public void drawInRect(NSRect rect, NSRect fromRect, long op, double delta) {
	OS.objc_msgSend(this.id, OS.sel_drawInRect_fromRect_operation_fraction_, rect, fromRect, op, delta);
}

public static NSImage imageNamed(NSString name) {
	long result = OS.objc_msgSend(OS.class_NSImage, OS.sel_imageNamed_, name != null ? name.id : 0);
	return result != 0 ? new NSImage(result) : null;
}

public NSImage initByReferencingFile(NSString fileName) {
	long result = OS.objc_msgSend(this.id, OS.sel_initByReferencingFile_, fileName != null ? fileName.id : 0);
	return result == this.id ? this : (result != 0 ? new NSImage(result) : null);
}

public NSImage initWithContentsOfFile(NSString fileName) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfFile_, fileName != null ? fileName.id : 0);
	return result == this.id ? this : (result != 0 ? new NSImage(result) : null);
}

public NSImage initWithData(NSData data) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithData_, data != null ? data.id : 0);
	return result == this.id ? this : (result != 0 ? new NSImage(result) : null);
}

public NSImage initWithIconRef(long iconRef) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithIconRef_, iconRef);
	return result == this.id ? this : (result != 0 ? new NSImage(result) : null);
}

public NSImage initWithSize(NSSize aSize) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithSize_, aSize);
	return result == this.id ? this : (result != 0 ? new NSImage(result) : null);
}

public void lockFocus() {
	OS.objc_msgSend(this.id, OS.sel_lockFocus);
}

public void removeRepresentation(NSImageRep imageRep) {
	OS.objc_msgSend(this.id, OS.sel_removeRepresentation_, imageRep != null ? imageRep.id : 0);
}

public NSArray representations() {
	long result = OS.objc_msgSend(this.id, OS.sel_representations);
	return result != 0 ? new NSArray(result) : null;
}

public void setCacheMode(long cacheMode) {
	OS.objc_msgSend(this.id, OS.sel_setCacheMode_, cacheMode);
}

public void setScalesWhenResized(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setScalesWhenResized_, flag);
}

public void setSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setSize_, size);
}

public NSSize size() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_size);
	return result;
}

public void unlockFocus() {
	OS.objc_msgSend(this.id, OS.sel_unlockFocus);
}

}

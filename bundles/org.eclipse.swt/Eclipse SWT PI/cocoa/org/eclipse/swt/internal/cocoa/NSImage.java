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

public class NSImage extends NSObject {

public NSImage() {
	super();
}

public NSImage(long /*int*/ id) {
	super(id);
}

public NSImage(id id) {
	super(id);
}

public NSData TIFFRepresentation() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_TIFFRepresentation);
	return result != 0 ? new NSData(result) : null;
}

public void addRepresentation(NSImageRep imageRep) {
	OS.objc_msgSend(this.id, OS.sel_addRepresentation_, imageRep != null ? imageRep.id : 0);
}

public NSImageRep bestRepresentationForDevice(NSDictionary deviceDescription) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_bestRepresentationForDevice_, deviceDescription != null ? deviceDescription.id : 0);
	return result != 0 ? new NSImageRep(result) : null;
}

public void drawAtPoint(NSPoint point, NSRect fromRect, long /*int*/ op, double /*float*/ delta) {
	OS.objc_msgSend(this.id, OS.sel_drawAtPoint_fromRect_operation_fraction_, point, fromRect, op, delta);
}

public void drawInRect(NSRect rect, NSRect fromRect, long /*int*/ op, double /*float*/ delta) {
	OS.objc_msgSend(this.id, OS.sel_drawInRect_fromRect_operation_fraction_, rect, fromRect, op, delta);
}

public static NSImage imageNamed(NSString name) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSImage, OS.sel_imageNamed_, name != null ? name.id : 0);
	return result != 0 ? new NSImage(result) : null;
}

public NSImage initByReferencingFile(NSString fileName) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initByReferencingFile_, fileName != null ? fileName.id : 0);
	return result == this.id ? this : (result != 0 ? new NSImage(result) : null);
}

public NSImage initWithContentsOfFile(NSString fileName) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfFile_, fileName != null ? fileName.id : 0);
	return result == this.id ? this : (result != 0 ? new NSImage(result) : null);
}

public id initWithData(NSData data) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithData_, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSImage initWithIconRef(long /*int*/ iconRef) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithIconRef_, iconRef);
	return result == this.id ? this : (result != 0 ? new NSImage(result) : null);
}

public NSImage initWithSize(NSSize aSize) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithSize_, aSize);
	return result == this.id ? this : (result != 0 ? new NSImage(result) : null);
}

public void lockFocus() {
	OS.objc_msgSend(this.id, OS.sel_lockFocus);
}

public void removeRepresentation(NSImageRep imageRep) {
	OS.objc_msgSend(this.id, OS.sel_removeRepresentation_, imageRep != null ? imageRep.id : 0);
}

public NSArray representations() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_representations);
	return result != 0 ? new NSArray(result) : null;
}

public void setCacheMode(long /*int*/ mode) {
	OS.objc_msgSend(this.id, OS.sel_setCacheMode_, mode);
}

public void setScalesWhenResized(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setScalesWhenResized_, flag);
}

public void setSize(NSSize aSize) {
	OS.objc_msgSend(this.id, OS.sel_setSize_, aSize);
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

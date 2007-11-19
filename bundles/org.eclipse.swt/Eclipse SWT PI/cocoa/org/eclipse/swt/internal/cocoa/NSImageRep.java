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

public class NSImageRep extends NSObject {

public NSImageRep() {
	super();
}

public NSImageRep(int id) {
	super(id);
}

public int bitsPerSample() {
	return OS.objc_msgSend(this.id, OS.sel_bitsPerSample);
}

public static boolean canInitWithData(NSData data) {
	return OS.objc_msgSend(OS.class_NSImageRep, OS.sel_canInitWithData_1, data != null ? data.id : 0) != 0;
}

public static boolean canInitWithPasteboard(NSPasteboard pasteboard) {
	return OS.objc_msgSend(OS.class_NSImageRep, OS.sel_canInitWithPasteboard_1, pasteboard != null ? pasteboard.id : 0) != 0;
}

public NSString colorSpaceName() {
	int result = OS.objc_msgSend(this.id, OS.sel_colorSpaceName);
	return result != 0 ? new NSString(result) : null;
}

public boolean draw() {
	return OS.objc_msgSend(this.id, OS.sel_draw) != 0;
}

public boolean drawAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_drawAtPoint_1, point) != 0;
}

public boolean drawInRect(NSRect rect) {
	return OS.objc_msgSend(this.id, OS.sel_drawInRect_1, rect) != 0;
}

public boolean hasAlpha() {
	return OS.objc_msgSend(this.id, OS.sel_hasAlpha) != 0;
}

public static NSArray imageFileTypes() {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageFileTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray imagePasteboardTypes() {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imagePasteboardTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static int imageRepClassForData(NSData data) {
	return OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageRepClassForData_1, data != null ? data.id : 0);
}

public static int imageRepClassForFileType(NSString type) {
	return OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageRepClassForFileType_1, type != null ? type.id : 0);
}

public static int imageRepClassForPasteboardType(NSString type) {
	return OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageRepClassForPasteboardType_1, type != null ? type.id : 0);
}

public static int imageRepClassForType(NSString type) {
	return OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageRepClassForType_1, type != null ? type.id : 0);
}

public static id imageRepWithContentsOfFile(NSString filename) {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageRepWithContentsOfFile_1, filename != null ? filename.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id imageRepWithContentsOfURL(NSURL url) {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageRepWithContentsOfURL_1, url != null ? url.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id imageRepWithPasteboard(NSPasteboard pasteboard) {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageRepWithPasteboard_1, pasteboard != null ? pasteboard.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSArray imageRepsWithContentsOfFile(NSString filename) {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageRepsWithContentsOfFile_1, filename != null ? filename.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray imageRepsWithContentsOfURL(NSURL url) {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageRepsWithContentsOfURL_1, url != null ? url.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray imageRepsWithPasteboard(NSPasteboard pasteboard) {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageRepsWithPasteboard_1, pasteboard != null ? pasteboard.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray imageTypes() {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray imageUnfilteredFileTypes() {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageUnfilteredFileTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray imageUnfilteredPasteboardTypes() {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageUnfilteredPasteboardTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray imageUnfilteredTypes() {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageUnfilteredTypes);
	return result != 0 ? new NSArray(result) : null;
}

public boolean isOpaque() {
	return OS.objc_msgSend(this.id, OS.sel_isOpaque) != 0;
}

public int pixelsHigh() {
	return OS.objc_msgSend(this.id, OS.sel_pixelsHigh);
}

public int pixelsWide() {
	return OS.objc_msgSend(this.id, OS.sel_pixelsWide);
}

public static void registerImageRepClass(int imageRepClass) {
	OS.objc_msgSend(OS.class_NSImageRep, OS.sel_registerImageRepClass_1, imageRepClass);
}

public static NSArray registeredImageRepClasses() {
	int result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_registeredImageRepClasses);
	return result != 0 ? new NSArray(result) : null;
}

public void setAlpha(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAlpha_1, flag);
}

public void setBitsPerSample(int anInt) {
	OS.objc_msgSend(this.id, OS.sel_setBitsPerSample_1, anInt);
}

public void setColorSpaceName(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setColorSpaceName_1, string != null ? string.id : 0);
}

public void setOpaque(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setOpaque_1, flag);
}

public void setPixelsHigh(int anInt) {
	OS.objc_msgSend(this.id, OS.sel_setPixelsHigh_1, anInt);
}

public void setPixelsWide(int anInt) {
	OS.objc_msgSend(this.id, OS.sel_setPixelsWide_1, anInt);
}

public void setSize(NSSize aSize) {
	OS.objc_msgSend(this.id, OS.sel_setSize_1, aSize);
}

public NSSize size() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_size);
	return result;
}

public static void unregisterImageRepClass(int imageRepClass) {
	OS.objc_msgSend(OS.class_NSImageRep, OS.sel_unregisterImageRepClass_1, imageRepClass);
}

}

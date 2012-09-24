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

public class NSBitmapImageRep extends NSImageRep {

public NSBitmapImageRep() {
	super();
}

public NSBitmapImageRep(long /*int*/ id) {
	super(id);
}

public NSBitmapImageRep(id id) {
	super(id);
}

public NSData TIFFRepresentation() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_TIFFRepresentation);
	return result != 0 ? new NSData(result) : null;
}

public long /*int*/ bitmapData() {
	return OS.objc_msgSend(this.id, OS.sel_bitmapData);
}

public long /*int*/ bitmapFormat() {
	return OS.objc_msgSend(this.id, OS.sel_bitmapFormat);
}

public long /*int*/ bitsPerPixel() {
	return OS.objc_msgSend(this.id, OS.sel_bitsPerPixel);
}

public long /*int*/ bytesPerPlane() {
	return OS.objc_msgSend(this.id, OS.sel_bytesPerPlane);
}

public long /*int*/ bytesPerRow() {
	return OS.objc_msgSend(this.id, OS.sel_bytesPerRow);
}

public NSColor colorAtX(long /*int*/ x, long /*int*/ y) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_colorAtX_y_, x, y);
	return result != 0 ? new NSColor(result) : null;
}

public void getBitmapDataPlanes(long[] /*int[]*/ data) {
	OS.objc_msgSend(this.id, OS.sel_getBitmapDataPlanes_, data);
}

public static id imageRepWithData(NSData data) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSBitmapImageRep, OS.sel_imageRepWithData_, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSBitmapImageRep initWithBitmapDataPlanes(long /*int*/ planes, long /*int*/ width, long /*int*/ height, long /*int*/ bps, long /*int*/ spp, boolean alpha, boolean isPlanar, NSString colorSpaceName, long /*int*/ bitmapFormat, long /*int*/ rBytes, long /*int*/ pBits) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bitmapFormat_bytesPerRow_bitsPerPixel_, planes, width, height, bps, spp, alpha, isPlanar, colorSpaceName != null ? colorSpaceName.id : 0, bitmapFormat, rBytes, pBits);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public NSBitmapImageRep initWithBitmapDataPlanes(long /*int*/ planes, long /*int*/ width, long /*int*/ height, long /*int*/ bps, long /*int*/ spp, boolean alpha, boolean isPlanar, NSString colorSpaceName, long /*int*/ rBytes, long /*int*/ pBits) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bytesPerRow_bitsPerPixel_, planes, width, height, bps, spp, alpha, isPlanar, colorSpaceName != null ? colorSpaceName.id : 0, rBytes, pBits);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public NSBitmapImageRep initWithData(NSData data) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithData_, data != null ? data.id : 0);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public NSBitmapImageRep initWithFocusedViewRect(NSRect rect) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithFocusedViewRect_, rect);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public boolean isPlanar() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isPlanar);
}

public long /*int*/ numberOfPlanes() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfPlanes);
}

public long /*int*/ samplesPerPixel() {
	return OS.objc_msgSend(this.id, OS.sel_samplesPerPixel);
}

}

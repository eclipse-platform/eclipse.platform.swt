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

public NSBitmapImageRep(int /*long*/ id) {
	super(id);
}

public NSBitmapImageRep(id id) {
	super(id);
}

public NSData TIFFRepresentation() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_TIFFRepresentation);
	return result != 0 ? new NSData(result) : null;
}

public int /*long*/ bitmapData() {
	return OS.objc_msgSend(this.id, OS.sel_bitmapData);
}

public int /*long*/ bitmapFormat() {
	return OS.objc_msgSend(this.id, OS.sel_bitmapFormat);
}

public int /*long*/ bitsPerPixel() {
	return OS.objc_msgSend(this.id, OS.sel_bitsPerPixel);
}

public int /*long*/ bytesPerPlane() {
	return OS.objc_msgSend(this.id, OS.sel_bytesPerPlane);
}

public int /*long*/ bytesPerRow() {
	return OS.objc_msgSend(this.id, OS.sel_bytesPerRow);
}

public NSColor colorAtX(int /*long*/ x, int /*long*/ y) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_colorAtX_y_, x, y);
	return result != 0 ? new NSColor(result) : null;
}

public void getBitmapDataPlanes(int[] /*long[]*/ data) {
	OS.objc_msgSend(this.id, OS.sel_getBitmapDataPlanes_, data);
}

public static id imageRepWithData(NSData data) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSBitmapImageRep, OS.sel_imageRepWithData_, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSBitmapImageRep initWithBitmapDataPlanes(int /*long*/ planes, int /*long*/ width, int /*long*/ height, int /*long*/ bps, int /*long*/ spp, boolean alpha, boolean isPlanar, NSString colorSpaceName, int /*long*/ bitmapFormat, int /*long*/ rBytes, int /*long*/ pBits) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bitmapFormat_bytesPerRow_bitsPerPixel_, planes, width, height, bps, spp, alpha, isPlanar, colorSpaceName != null ? colorSpaceName.id : 0, bitmapFormat, rBytes, pBits);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public NSBitmapImageRep initWithBitmapDataPlanes(int /*long*/ planes, int /*long*/ width, int /*long*/ height, int /*long*/ bps, int /*long*/ spp, boolean alpha, boolean isPlanar, NSString colorSpaceName, int /*long*/ rBytes, int /*long*/ pBits) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bytesPerRow_bitsPerPixel_, planes, width, height, bps, spp, alpha, isPlanar, colorSpaceName != null ? colorSpaceName.id : 0, rBytes, pBits);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public NSBitmapImageRep initWithData(NSData data) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithData_, data != null ? data.id : 0);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public NSBitmapImageRep initWithFocusedViewRect(NSRect rect) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithFocusedViewRect_, rect);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public boolean isPlanar() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isPlanar);
}

public int /*long*/ numberOfPlanes() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfPlanes);
}

public int /*long*/ samplesPerPixel() {
	return OS.objc_msgSend(this.id, OS.sel_samplesPerPixel);
}

}

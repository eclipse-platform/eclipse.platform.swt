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

public int /*long*/ bitmapData() {
	return OS.objc_msgSend(this.id, OS.sel_bitmapData);
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

public NSBitmapImageRep initWithBitmapDataPlanes(int /*long*/ planes, int /*long*/ width, int /*long*/ height, int /*long*/ bps, int /*long*/ spp, boolean alpha, boolean isPlanar, NSString colorSpaceName, int /*long*/ bitmapFormat, int /*long*/ rBytes, int /*long*/ pBits) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bitmapFormat_bytesPerRow_bitsPerPixel_, planes, width, height, bps, spp, alpha, isPlanar, colorSpaceName != null ? colorSpaceName.id : 0, bitmapFormat, rBytes, pBits);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public NSBitmapImageRep initWithBitmapDataPlanes(int /*long*/ planes, int /*long*/ width, int /*long*/ height, int /*long*/ bps, int /*long*/ spp, boolean alpha, boolean isPlanar, NSString colorSpaceName, int /*long*/ rBytes, int /*long*/ pBits) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bytesPerRow_bitsPerPixel_, planes, width, height, bps, spp, alpha, isPlanar, colorSpaceName != null ? colorSpaceName.id : 0, rBytes, pBits);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public boolean isPlanar() {
	return OS.objc_msgSend(this.id, OS.sel_isPlanar) != 0;
}

public int /*long*/ samplesPerPixel() {
	return OS.objc_msgSend(this.id, OS.sel_samplesPerPixel);
}

}

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

public int bitmapData() {
	return OS.objc_msgSend(this.id, OS.sel_bitmapData);
}

public int bitsPerPixel() {
	return OS.objc_msgSend(this.id, OS.sel_bitsPerPixel);
}

public int bytesPerPlane() {
	return OS.objc_msgSend(this.id, OS.sel_bytesPerPlane);
}

public int bytesPerRow() {
	return OS.objc_msgSend(this.id, OS.sel_bytesPerRow);
}

public NSBitmapImageRep initWithBitmapDataPlanes(int planes, int width, int height, int bps, int spp, boolean alpha, boolean isPlanar, NSString colorSpaceName, int bitmapFormat, int rBytes, int pBits) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bitmapFormat_bytesPerRow_bitsPerPixel_, planes, width, height, bps, spp, alpha, isPlanar, colorSpaceName != null ? colorSpaceName.id : 0, bitmapFormat, rBytes, pBits);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public NSBitmapImageRep initWithBitmapDataPlanes(int planes, int width, int height, int bps, int spp, boolean alpha, boolean isPlanar, NSString colorSpaceName, int rBytes, int pBits) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bytesPerRow_bitsPerPixel_, planes, width, height, bps, spp, alpha, isPlanar, colorSpaceName != null ? colorSpaceName.id : 0, rBytes, pBits);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public boolean isPlanar() {
	return OS.objc_msgSend(this.id, OS.sel_isPlanar) != 0;
}

public int samplesPerPixel() {
	return OS.objc_msgSend(this.id, OS.sel_samplesPerPixel);
}

}

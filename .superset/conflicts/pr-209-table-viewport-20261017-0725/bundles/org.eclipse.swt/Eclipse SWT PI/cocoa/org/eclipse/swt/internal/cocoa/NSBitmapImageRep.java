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

public class NSBitmapImageRep extends NSImageRep {

public NSBitmapImageRep() {
	super();
}

public NSBitmapImageRep(long id) {
	super(id);
}

public NSBitmapImageRep(id id) {
	super(id);
}

public long bitmapData() {
	return OS.objc_msgSend(this.id, OS.sel_bitmapData);
}

public long bitmapFormat() {
	return OS.objc_msgSend(this.id, OS.sel_bitmapFormat);
}

public long bitsPerPixel() {
	return OS.objc_msgSend(this.id, OS.sel_bitsPerPixel);
}

public long bytesPerRow() {
	return OS.objc_msgSend(this.id, OS.sel_bytesPerRow);
}

public NSColor colorAtX(long x, long y) {
	long result = OS.objc_msgSend(this.id, OS.sel_colorAtX_y_, x, y);
	return result != 0 ? new NSColor(result) : null;
}

public NSBitmapImageRep initWithBitmapDataPlanes(long planes, long width, long height, long bps, long spp, boolean alpha, boolean isPlanar, NSString colorSpaceName, long bitmapFormat, long rBytes, long pBits) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bitmapFormat_bytesPerRow_bitsPerPixel_, planes, width, height, bps, spp, alpha, isPlanar, colorSpaceName != null ? colorSpaceName.id : 0, bitmapFormat, rBytes, pBits);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public NSBitmapImageRep initWithData(NSData data) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithData_, data != null ? data.id : 0);
	return result == this.id ? this : (result != 0 ? new NSBitmapImageRep(result) : null);
}

public boolean isPlanar() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isPlanar);
}

public long samplesPerPixel() {
	return OS.objc_msgSend(this.id, OS.sel_samplesPerPixel);
}

public static NSImageRep imageRepWithContentsOfFile(NSString filename) {
	long result = OS.objc_msgSend(OS.class_NSBitmapImageRep, OS.sel_imageRepWithContentsOfFile_, filename != null ? filename.id : 0);
	return result != 0 ? new NSImageRep(result) : null;
}

}

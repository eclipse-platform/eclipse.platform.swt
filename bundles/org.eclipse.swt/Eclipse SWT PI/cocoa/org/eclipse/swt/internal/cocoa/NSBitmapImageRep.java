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

public class NSBitmapImageRep extends NSImageRep {

public NSBitmapImageRep() {
	super();
}

public NSBitmapImageRep(int id) {
	super(id);
}

public int CGImage() {
	return OS.objc_msgSend(this.id, OS.sel_CGImage);
}

public NSData TIFFRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_TIFFRepresentation);
	return result != 0 ? new NSData(result) : null;
}

public static NSData static_TIFFRepresentationOfImageRepsInArray_(NSArray array) {
	int result = OS.objc_msgSend(OS.class_NSBitmapImageRep, OS.sel_TIFFRepresentationOfImageRepsInArray_1, array != null ? array.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public static NSData static_TIFFRepresentationOfImageRepsInArray_usingCompression_factor_(NSArray array, int comp, float factor) {
	int result = OS.objc_msgSend(OS.class_NSBitmapImageRep, OS.sel_TIFFRepresentationOfImageRepsInArray_1usingCompression_1factor_1, array != null ? array.id : 0, comp, factor);
	return result != 0 ? new NSData(result) : null;
}

public NSData TIFFRepresentationUsingCompression(int comp, float factor) {
	int result = OS.objc_msgSend(this.id, OS.sel_TIFFRepresentationUsingCompression_1factor_1, comp, factor);
	return result != 0 ? new NSData(result) : null;
}

public int bitmapData() {
	return OS.objc_msgSend(this.id, OS.sel_bitmapData);
}

public int bitmapFormat() {
	return OS.objc_msgSend(this.id, OS.sel_bitmapFormat);
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

public boolean canBeCompressedUsing(int compression) {
	return OS.objc_msgSend(this.id, OS.sel_canBeCompressedUsing_1, compression) != 0;
}

public NSColor colorAtX(int x, int y) {
	int result = OS.objc_msgSend(this.id, OS.sel_colorAtX_1y_1, x, y);
	return result != 0 ? new NSColor(result) : null;
}

public void colorizeByMappingGray(float midPoint, NSColor midPointColor, NSColor shadowColor, NSColor lightColor) {
	OS.objc_msgSend(this.id, OS.sel_colorizeByMappingGray_1toColor_1blackMapping_1whiteMapping_1, midPoint, midPointColor != null ? midPointColor.id : 0, shadowColor != null ? shadowColor.id : 0, lightColor != null ? lightColor.id : 0);
}

public void getBitmapDataPlanes(int data) {
	OS.objc_msgSend(this.id, OS.sel_getBitmapDataPlanes_1, data);
}

public void getCompression(int compression, int factor) {
	OS.objc_msgSend(this.id, OS.sel_getCompression_1factor_1, compression, factor);
}

public void getPixel(int p, int x, int y) {
	OS.objc_msgSend(this.id, OS.sel_getPixel_1atX_1y_1, p, x, y);
}

public static void getTIFFCompressionTypes(int list, int numTypes) {
	OS.objc_msgSend(OS.class_NSBitmapImageRep, OS.sel_getTIFFCompressionTypes_1count_1, list, numTypes);
}

public static id imageRepWithData(NSData data) {
	int result = OS.objc_msgSend(OS.class_NSBitmapImageRep, OS.sel_imageRepWithData_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSArray imageRepsWithData(NSData data) {
	int result = OS.objc_msgSend(OS.class_NSBitmapImageRep, OS.sel_imageRepsWithData_1, data != null ? data.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public int incrementalLoadFromData(NSData data, boolean complete) {
	return OS.objc_msgSend(this.id, OS.sel_incrementalLoadFromData_1complete_1, data != null ? data.id : 0, complete);
}

public NSBitmapImageRep initForIncrementalLoad() {
	int result = OS.objc_msgSend(this.id, OS.sel_initForIncrementalLoad);
	return result != 0 ? this : null;
}

public NSBitmapImageRep initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bitmapFormat_bytesPerRow_bitsPerPixel_(int planes, int width, int height, int bps, int spp, boolean alpha, boolean isPlanar, NSString colorSpaceName, int bitmapFormat, int rBytes, int pBits) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapDataPlanes_1pixelsWide_1pixelsHigh_1bitsPerSample_1samplesPerPixel_1hasAlpha_1isPlanar_1colorSpaceName_1bitmapFormat_1bytesPerRow_1bitsPerPixel_1, planes, width, height, bps, spp, alpha, isPlanar, colorSpaceName != null ? colorSpaceName.id : 0, bitmapFormat, rBytes, pBits);
	return result != 0 ? this : null;
}

public NSBitmapImageRep initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bytesPerRow_bitsPerPixel_(int planes, int width, int height, int bps, int spp, boolean alpha, boolean isPlanar, NSString colorSpaceName, int rBytes, int pBits) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapDataPlanes_1pixelsWide_1pixelsHigh_1bitsPerSample_1samplesPerPixel_1hasAlpha_1isPlanar_1colorSpaceName_1bytesPerRow_1bitsPerPixel_1, planes, width, height, bps, spp, alpha, isPlanar, colorSpaceName != null ? colorSpaceName.id : 0, rBytes, pBits);
	return result != 0 ? this : null;
}

public NSBitmapImageRep initWithCGImage(int cgImage) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCGImage_1, cgImage);
	return result != 0 ? this : null;
}

public NSBitmapImageRep initWithCIImage(CIImage ciImage) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCIImage_1, ciImage != null ? ciImage.id : 0);
	return result != 0 ? this : null;
}

public NSBitmapImageRep initWithData(NSData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1, data != null ? data.id : 0);
	return result != 0 ? this : null;
}

public NSBitmapImageRep initWithFocusedViewRect(NSRect rect) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFocusedViewRect_1, rect);
	return result != 0 ? this : null;
}

public boolean isPlanar() {
	return OS.objc_msgSend(this.id, OS.sel_isPlanar) != 0;
}

public static NSString localizedNameForTIFFCompressionType(int compression) {
	int result = OS.objc_msgSend(OS.class_NSBitmapImageRep, OS.sel_localizedNameForTIFFCompressionType_1, compression);
	return result != 0 ? new NSString(result) : null;
}

public int numberOfPlanes() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfPlanes);
}

public static NSData representationOfImageRepsInArray(NSArray imageReps, int storageType, NSDictionary properties) {
	int result = OS.objc_msgSend(OS.class_NSBitmapImageRep, OS.sel_representationOfImageRepsInArray_1usingType_1properties_1, imageReps != null ? imageReps.id : 0, storageType, properties != null ? properties.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public NSData representationUsingType(int storageType, NSDictionary properties) {
	int result = OS.objc_msgSend(this.id, OS.sel_representationUsingType_1properties_1, storageType, properties != null ? properties.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public int samplesPerPixel() {
	return OS.objc_msgSend(this.id, OS.sel_samplesPerPixel);
}

public void setColor(NSColor color, int x, int y) {
	OS.objc_msgSend(this.id, OS.sel_setColor_1atX_1y_1, color != null ? color.id : 0, x, y);
}

public void setCompression(int compression, float factor) {
	OS.objc_msgSend(this.id, OS.sel_setCompression_1factor_1, compression, factor);
}

public void setPixel(int p, int x, int y) {
	OS.objc_msgSend(this.id, OS.sel_setPixel_1atX_1y_1, p, x, y);
}

public void setProperty(NSString property, id value) {
	OS.objc_msgSend(this.id, OS.sel_setProperty_1withValue_1, property != null ? property.id : 0, value != null ? value.id : 0);
}

public id valueForProperty(NSString property) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueForProperty_1, property != null ? property.id : 0);
	return result != 0 ? new id(result) : null;
}

}

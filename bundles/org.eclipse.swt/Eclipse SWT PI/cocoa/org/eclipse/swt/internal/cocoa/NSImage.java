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

public class NSImage extends NSObject {

public NSImage() {
	super();
}

public NSImage(int id) {
	super(id);
}

public NSData TIFFRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_TIFFRepresentation);
	return result != 0 ? new NSData(result) : null;
}

public NSData TIFFRepresentationUsingCompression(int comp, float aFloat) {
	int result = OS.objc_msgSend(this.id, OS.sel_TIFFRepresentationUsingCompression_1factor_1, comp, aFloat);
	return result != 0 ? new NSData(result) : null;
}

public void addRepresentation(NSImageRep imageRep) {
	OS.objc_msgSend(this.id, OS.sel_addRepresentation_1, imageRep != null ? imageRep.id : 0);
}

public void addRepresentations(NSArray imageReps) {
	OS.objc_msgSend(this.id, OS.sel_addRepresentations_1, imageReps != null ? imageReps.id : 0);
}

public NSRect alignmentRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_alignmentRect);
	return result;
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSImageRep bestRepresentationForDevice(NSDictionary deviceDescription) {
	int result = OS.objc_msgSend(this.id, OS.sel_bestRepresentationForDevice_1, deviceDescription != null ? deviceDescription.id : 0);
	return result != 0 ? new NSImageRep(result) : null;
}

public boolean cacheDepthMatchesImageDepth() {
	return OS.objc_msgSend(this.id, OS.sel_cacheDepthMatchesImageDepth) != 0;
}

public int cacheMode() {
	return OS.objc_msgSend(this.id, OS.sel_cacheMode);
}

public static boolean canInitWithPasteboard(NSPasteboard pasteboard) {
	return OS.objc_msgSend(OS.class_NSImage, OS.sel_canInitWithPasteboard_1, pasteboard != null ? pasteboard.id : 0) != 0;
}

public void cancelIncrementalLoad() {
	OS.objc_msgSend(this.id, OS.sel_cancelIncrementalLoad);
}

public void compositeToPoint_fromRect_operation_(NSPoint point, NSRect rect, int op) {
	OS.objc_msgSend(this.id, OS.sel_compositeToPoint_1fromRect_1operation_1, point, rect, op);
}

public void compositeToPoint_fromRect_operation_fraction_(NSPoint point, NSRect rect, int op, float delta) {
	OS.objc_msgSend(this.id, OS.sel_compositeToPoint_1fromRect_1operation_1fraction_1, point, rect, op, delta);
}

public void compositeToPoint_operation_(NSPoint point, int op) {
	OS.objc_msgSend(this.id, OS.sel_compositeToPoint_1operation_1, point, op);
}

public void compositeToPoint_operation_fraction_(NSPoint point, int op, float delta) {
	OS.objc_msgSend(this.id, OS.sel_compositeToPoint_1operation_1fraction_1, point, op, delta);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void dissolveToPoint_fraction_(NSPoint point, float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_dissolveToPoint_1fraction_1, point, aFloat);
}

public void dissolveToPoint_fromRect_fraction_(NSPoint point, NSRect rect, float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_dissolveToPoint_1fromRect_1fraction_1, point, rect, aFloat);
}

public void drawAtPoint(NSPoint point, NSRect fromRect, int op, float delta) {
	OS.objc_msgSend(this.id, OS.sel_drawAtPoint_1fromRect_1operation_1fraction_1, point, fromRect, op, delta);
}

public void drawInRect(NSRect rect, NSRect fromRect, int op, float delta) {
	OS.objc_msgSend(this.id, OS.sel_drawInRect_1fromRect_1operation_1fraction_1, rect, fromRect, op, delta);
}

public boolean drawRepresentation(NSImageRep imageRep, NSRect rect) {
	return OS.objc_msgSend(this.id, OS.sel_drawRepresentation_1inRect_1, imageRep != null ? imageRep.id : 0, rect) != 0;
}

public static NSArray imageFileTypes() {
	int result = OS.objc_msgSend(OS.class_NSImage, OS.sel_imageFileTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static id imageNamed(NSString name) {
	int result = OS.objc_msgSend(OS.class_NSImage, OS.sel_imageNamed_1, name != null ? name.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSArray imagePasteboardTypes() {
	int result = OS.objc_msgSend(OS.class_NSImage, OS.sel_imagePasteboardTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray imageTypes() {
	int result = OS.objc_msgSend(OS.class_NSImage, OS.sel_imageTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray imageUnfilteredFileTypes() {
	int result = OS.objc_msgSend(OS.class_NSImage, OS.sel_imageUnfilteredFileTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray imageUnfilteredPasteboardTypes() {
	int result = OS.objc_msgSend(OS.class_NSImage, OS.sel_imageUnfilteredPasteboardTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray imageUnfilteredTypes() {
	int result = OS.objc_msgSend(OS.class_NSImage, OS.sel_imageUnfilteredTypes);
	return result != 0 ? new NSArray(result) : null;
}

public NSImage initByReferencingFile(NSString fileName) {
	int result = OS.objc_msgSend(this.id, OS.sel_initByReferencingFile_1, fileName != null ? fileName.id : 0);
	return result != 0 ? this : null;
}

public NSImage initByReferencingURL(NSURL url) {
	int result = OS.objc_msgSend(this.id, OS.sel_initByReferencingURL_1, url != null ? url.id : 0);
	return result != 0 ? this : null;
}

public NSImage initWithContentsOfFile(NSString fileName) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfFile_1, fileName != null ? fileName.id : 0);
	return result != 0 ? this : null;
}

public NSImage initWithContentsOfURL(NSURL url) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1, url != null ? url.id : 0);
	return result != 0 ? this : null;
}

public NSImage initWithData(NSData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1, data != null ? data.id : 0);
	return result != 0 ? this : null;
}

public NSImage initWithIconRef(int iconRef) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithIconRef_1, iconRef);
	return result != 0 ? this : null;
}

public NSImage initWithPasteboard(NSPasteboard pasteboard) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithPasteboard_1, pasteboard != null ? pasteboard.id : 0);
	return result != 0 ? this : null;
}

public NSImage initWithSize(NSSize aSize) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithSize_1, aSize);
	return result != 0 ? this : null;
}

public boolean isCachedSeparately() {
	return OS.objc_msgSend(this.id, OS.sel_isCachedSeparately) != 0;
}

public boolean isDataRetained() {
	return OS.objc_msgSend(this.id, OS.sel_isDataRetained) != 0;
}

public boolean isFlipped() {
	return OS.objc_msgSend(this.id, OS.sel_isFlipped) != 0;
}

public boolean isTemplate() {
	return OS.objc_msgSend(this.id, OS.sel_isTemplate) != 0;
}

public boolean isValid() {
	return OS.objc_msgSend(this.id, OS.sel_isValid) != 0;
}

public void lockFocus() {
	OS.objc_msgSend(this.id, OS.sel_lockFocus);
}

public void lockFocusOnRepresentation(NSImageRep imageRepresentation) {
	OS.objc_msgSend(this.id, OS.sel_lockFocusOnRepresentation_1, imageRepresentation != null ? imageRepresentation.id : 0);
}

public boolean matchesOnMultipleResolution() {
	return OS.objc_msgSend(this.id, OS.sel_matchesOnMultipleResolution) != 0;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public boolean prefersColorMatch() {
	return OS.objc_msgSend(this.id, OS.sel_prefersColorMatch) != 0;
}

public void recache() {
	OS.objc_msgSend(this.id, OS.sel_recache);
}

public void removeRepresentation(NSImageRep imageRep) {
	OS.objc_msgSend(this.id, OS.sel_removeRepresentation_1, imageRep != null ? imageRep.id : 0);
}

public NSArray representations() {
	int result = OS.objc_msgSend(this.id, OS.sel_representations);
	return result != 0 ? new NSArray(result) : null;
}

public boolean scalesWhenResized() {
	return OS.objc_msgSend(this.id, OS.sel_scalesWhenResized) != 0;
}

public void setAlignmentRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_setAlignmentRect_1, rect);
}

public void setBackgroundColor(NSColor aColor) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, aColor != null ? aColor.id : 0);
}

public void setCacheDepthMatchesImageDepth(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCacheDepthMatchesImageDepth_1, flag);
}

public void setCacheMode(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setCacheMode_1, mode);
}

public void setCachedSeparately(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCachedSeparately_1, flag);
}

public void setDataRetained(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDataRetained_1, flag);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setFlipped(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setFlipped_1, flag);
}

public void setMatchesOnMultipleResolution(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setMatchesOnMultipleResolution_1, flag);
}

public boolean setName(NSString string) {
	return OS.objc_msgSend(this.id, OS.sel_setName_1, string != null ? string.id : 0) != 0;
}

public void setPrefersColorMatch(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPrefersColorMatch_1, flag);
}

public void setScalesWhenResized(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setScalesWhenResized_1, flag);
}

public void setSize(NSSize aSize) {
	OS.objc_msgSend(this.id, OS.sel_setSize_1, aSize);
}

public void setTemplate(boolean isTemplate) {
	OS.objc_msgSend(this.id, OS.sel_setTemplate_1, isTemplate);
}

public void setUsesEPSOnResolutionMismatch(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setUsesEPSOnResolutionMismatch_1, flag);
}

public NSSize size() {
	NSSize result = new NSSize();
	OS.objc_msgSend_struct(result, this.id, OS.sel_size);
	return result;
}

public void unlockFocus() {
	OS.objc_msgSend(this.id, OS.sel_unlockFocus);
}

public boolean usesEPSOnResolutionMismatch() {
	return OS.objc_msgSend(this.id, OS.sel_usesEPSOnResolutionMismatch) != 0;
}

}

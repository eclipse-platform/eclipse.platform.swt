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

public class NSPrintInfo extends NSObject {

public NSPrintInfo() {
	super();
}

public NSPrintInfo(int id) {
	super(id);
}

public int PMPageFormat() {
	return OS.objc_msgSend(this.id, OS.sel_PMPageFormat);
}

public int PMPrintSession() {
	return OS.objc_msgSend(this.id, OS.sel_PMPrintSession);
}

public int PMPrintSettings() {
	return OS.objc_msgSend(this.id, OS.sel_PMPrintSettings);
}

public float bottomMargin() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_bottomMargin);
}

public static NSPrinter defaultPrinter() {
	int result = OS.objc_msgSend(OS.class_NSPrintInfo, OS.sel_defaultPrinter);
	return result != 0 ? new NSPrinter(result) : null;
}

public NSMutableDictionary dictionary() {
	int result = OS.objc_msgSend(this.id, OS.sel_dictionary);
	return result != 0 ? new NSMutableDictionary(result) : null;
}

public int horizontalPagination() {
	return OS.objc_msgSend(this.id, OS.sel_horizontalPagination);
}

public NSRect imageablePageBounds() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_imageablePageBounds);
	return result;
}

public id initWithDictionary(NSDictionary attributes) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDictionary_1, attributes != null ? attributes.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isHorizontallyCentered() {
	return OS.objc_msgSend(this.id, OS.sel_isHorizontallyCentered) != 0;
}

public boolean isVerticallyCentered() {
	return OS.objc_msgSend(this.id, OS.sel_isVerticallyCentered) != 0;
}

public NSString jobDisposition() {
	int result = OS.objc_msgSend(this.id, OS.sel_jobDisposition);
	return result != 0 ? new NSString(result) : null;
}

public float leftMargin() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_leftMargin);
}

public NSString localizedPaperName() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedPaperName);
	return result != 0 ? new NSString(result) : null;
}

public int orientation() {
	return OS.objc_msgSend(this.id, OS.sel_orientation);
}

public NSString paperName() {
	int result = OS.objc_msgSend(this.id, OS.sel_paperName);
	return result != 0 ? new NSString(result) : null;
}

public NSSize paperSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_paperSize);
	return result;
}

public NSMutableDictionary printSettings() {
	int result = OS.objc_msgSend(this.id, OS.sel_printSettings);
	return result != 0 ? new NSMutableDictionary(result) : null;
}

public NSPrinter printer() {
	int result = OS.objc_msgSend(this.id, OS.sel_printer);
	return result != 0 ? new NSPrinter(result) : null;
}

public float rightMargin() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_rightMargin);
}

public void setBottomMargin(float margin) {
	OS.objc_msgSend(this.id, OS.sel_setBottomMargin_1, margin);
}

public static void setDefaultPrinter(NSPrinter printer) {
	OS.objc_msgSend(OS.class_NSPrintInfo, OS.sel_setDefaultPrinter_1, printer != null ? printer.id : 0);
}

public void setHorizontalPagination(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontalPagination_1, mode);
}

public void setHorizontallyCentered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontallyCentered_1, flag);
}

public void setJobDisposition(NSString disposition) {
	OS.objc_msgSend(this.id, OS.sel_setJobDisposition_1, disposition != null ? disposition.id : 0);
}

public void setLeftMargin(float margin) {
	OS.objc_msgSend(this.id, OS.sel_setLeftMargin_1, margin);
}

public void setOrientation(int orientation) {
	OS.objc_msgSend(this.id, OS.sel_setOrientation_1, orientation);
}

public void setPaperName(NSString name) {
	OS.objc_msgSend(this.id, OS.sel_setPaperName_1, name != null ? name.id : 0);
}

public void setPaperSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setPaperSize_1, size);
}

public void setPrinter(NSPrinter printer) {
	OS.objc_msgSend(this.id, OS.sel_setPrinter_1, printer != null ? printer.id : 0);
}

public void setRightMargin(float margin) {
	OS.objc_msgSend(this.id, OS.sel_setRightMargin_1, margin);
}

public static void setSharedPrintInfo(NSPrintInfo printInfo) {
	OS.objc_msgSend(OS.class_NSPrintInfo, OS.sel_setSharedPrintInfo_1, printInfo != null ? printInfo.id : 0);
}

public void setTopMargin(float margin) {
	OS.objc_msgSend(this.id, OS.sel_setTopMargin_1, margin);
}

public void setUpPrintOperationDefaultValues() {
	OS.objc_msgSend(this.id, OS.sel_setUpPrintOperationDefaultValues);
}

public void setVerticalPagination(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setVerticalPagination_1, mode);
}

public void setVerticallyCentered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setVerticallyCentered_1, flag);
}

public static NSPrintInfo sharedPrintInfo() {
	int result = OS.objc_msgSend(OS.class_NSPrintInfo, OS.sel_sharedPrintInfo);
	return result != 0 ? new NSPrintInfo(result) : null;
}

public static NSSize sizeForPaperName(NSString name) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, OS.class_NSPrintInfo, OS.sel_sizeForPaperName_1, name != null ? name.id : 0);
	return result;
}

public float topMargin() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_topMargin);
}

public void updateFromPMPageFormat() {
	OS.objc_msgSend(this.id, OS.sel_updateFromPMPageFormat);
}

public void updateFromPMPrintSettings() {
	OS.objc_msgSend(this.id, OS.sel_updateFromPMPrintSettings);
}

public int verticalPagination() {
	return OS.objc_msgSend(this.id, OS.sel_verticalPagination);
}

}

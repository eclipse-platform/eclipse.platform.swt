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

public class NSPrintInfo extends NSObject {

public NSPrintInfo() {
	super();
}

public NSPrintInfo(int /*long*/ id) {
	super(id);
}

public NSPrintInfo(id id) {
	super(id);
}

public static NSPrinter defaultPrinter() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSPrintInfo, OS.sel_defaultPrinter);
	return result != 0 ? new NSPrinter(result) : null;
}

public NSMutableDictionary dictionary() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_dictionary);
	return result != 0 ? new NSMutableDictionary(result) : null;
}

public NSRect imageablePageBounds() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_imageablePageBounds);
	return result;
}

public NSPrintInfo initWithDictionary(NSDictionary attributes) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithDictionary_, attributes != null ? attributes.id : 0);
	return result == this.id ? this : (result != 0 ? new NSPrintInfo(result) : null);
}

public NSString jobDisposition() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_jobDisposition);
	return result != 0 ? new NSString(result) : null;
}

public int /*long*/ orientation() {
	return OS.objc_msgSend(this.id, OS.sel_orientation);
}

public NSSize paperSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_paperSize);
	return result;
}

public NSPrinter printer() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_printer);
	return result != 0 ? new NSPrinter(result) : null;
}

public void setJobDisposition(NSString disposition) {
	OS.objc_msgSend(this.id, OS.sel_setJobDisposition_, disposition != null ? disposition.id : 0);
}

public void setOrientation(int /*long*/ orientation) {
	OS.objc_msgSend(this.id, OS.sel_setOrientation_, orientation);
}

public void setPrinter(NSPrinter printer) {
	OS.objc_msgSend(this.id, OS.sel_setPrinter_, printer != null ? printer.id : 0);
}

public void setUpPrintOperationDefaultValues() {
	OS.objc_msgSend(this.id, OS.sel_setUpPrintOperationDefaultValues);
}

public static NSPrintInfo sharedPrintInfo() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSPrintInfo, OS.sel_sharedPrintInfo);
	return result != 0 ? new NSPrintInfo(result) : null;
}

}

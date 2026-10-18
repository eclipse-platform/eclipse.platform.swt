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

public class NSPrintInfo extends NSObject {

public NSPrintInfo() {
	super();
}

public NSPrintInfo(long id) {
	super(id);
}

public NSPrintInfo(id id) {
	super(id);
}

public long PMPrintSession() {
	return OS.objc_msgSend(this.id, OS.sel_PMPrintSession);
}

public long PMPrintSettings() {
	return OS.objc_msgSend(this.id, OS.sel_PMPrintSettings);
}

public static NSPrinter defaultPrinter() {
	long result = OS.objc_msgSend(OS.class_NSPrintInfo, OS.sel_defaultPrinter);
	return result != 0 ? new NSPrinter(result) : null;
}

public NSMutableDictionary dictionary() {
	long result = OS.objc_msgSend(this.id, OS.sel_dictionary);
	return result != 0 ? new NSMutableDictionary(result) : null;
}

public NSRect imageablePageBounds() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_imageablePageBounds);
	return result;
}

public boolean isSelectionOnly() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isSelectionOnly);
}

public NSString jobDisposition() {
	long result = OS.objc_msgSend(this.id, OS.sel_jobDisposition);
	return result != 0 ? new NSString(result) : null;
}

public NSSize paperSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_paperSize);
	return result;
}

public NSPrinter printer() {
	long result = OS.objc_msgSend(this.id, OS.sel_printer);
	return result != 0 ? new NSPrinter(result) : null;
}

public void setJobDisposition(NSString jobDisposition) {
	OS.objc_msgSend(this.id, OS.sel_setJobDisposition_, jobDisposition != null ? jobDisposition.id : 0);
}

public void setPrinter(NSPrinter printer) {
	OS.objc_msgSend(this.id, OS.sel_setPrinter_, printer != null ? printer.id : 0);
}

public void setSelectionOnly(boolean selectionOnly) {
	OS.objc_msgSend(this.id, OS.sel_setSelectionOnly_, selectionOnly);
}

public void setUpPrintOperationDefaultValues() {
	OS.objc_msgSend(this.id, OS.sel_setUpPrintOperationDefaultValues);
}

public static NSPrintInfo sharedPrintInfo() {
	long result = OS.objc_msgSend(OS.class_NSPrintInfo, OS.sel_sharedPrintInfo);
	return result != 0 ? new NSPrintInfo(result) : null;
}

public void updateFromPMPrintSettings() {
	OS.objc_msgSend(this.id, OS.sel_updateFromPMPrintSettings);
}

}

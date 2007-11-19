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

public class NSPrinter extends NSObject {

public NSPrinter() {
	super();
}

public NSPrinter(int id) {
	super(id);
}

public boolean acceptsBinary() {
	return OS.objc_msgSend(this.id, OS.sel_acceptsBinary) != 0;
}

public boolean booleanForKey(NSString key, NSString table) {
	return OS.objc_msgSend(this.id, OS.sel_booleanForKey_1inTable_1, key != null ? key.id : 0, table != null ? table.id : 0) != 0;
}

public NSDictionary deviceDescription() {
	int result = OS.objc_msgSend(this.id, OS.sel_deviceDescription);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSString domain() {
	int result = OS.objc_msgSend(this.id, OS.sel_domain);
	return result != 0 ? new NSString(result) : null;
}

public float floatForKey(NSString key, NSString table) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_floatForKey_1inTable_1, key != null ? key.id : 0, table != null ? table.id : 0);
}

public NSString host() {
	int result = OS.objc_msgSend(this.id, OS.sel_host);
	return result != 0 ? new NSString(result) : null;
}

public NSRect imageRectForPaper(NSString paperName) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_imageRectForPaper_1, paperName != null ? paperName.id : 0);
	return result;
}

public int intForKey(NSString key, NSString table) {
	return OS.objc_msgSend(this.id, OS.sel_intForKey_1inTable_1, key != null ? key.id : 0, table != null ? table.id : 0);
}

public boolean isColor() {
	return OS.objc_msgSend(this.id, OS.sel_isColor) != 0;
}

public boolean isFontAvailable(NSString faceName) {
	return OS.objc_msgSend(this.id, OS.sel_isFontAvailable_1, faceName != null ? faceName.id : 0) != 0;
}

public boolean isKey(NSString key, NSString table) {
	return OS.objc_msgSend(this.id, OS.sel_isKey_1inTable_1, key != null ? key.id : 0, table != null ? table.id : 0) != 0;
}

public boolean isOutputStackInReverseOrder() {
	return OS.objc_msgSend(this.id, OS.sel_isOutputStackInReverseOrder) != 0;
}

public int languageLevel() {
	return OS.objc_msgSend(this.id, OS.sel_languageLevel);
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public NSString note() {
	int result = OS.objc_msgSend(this.id, OS.sel_note);
	return result != 0 ? new NSString(result) : null;
}

public NSSize pageSizeForPaper(NSString paperName) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_pageSizeForPaper_1, paperName != null ? paperName.id : 0);
	return result;
}

public static NSArray printerNames() {
	int result = OS.objc_msgSend(OS.class_NSPrinter, OS.sel_printerNames);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray printerTypes() {
	int result = OS.objc_msgSend(OS.class_NSPrinter, OS.sel_printerTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSPrinter static_printerWithName_(NSString name) {
	int result = OS.objc_msgSend(OS.class_NSPrinter, OS.sel_printerWithName_1, name != null ? name.id : 0);
	return result != 0 ? new NSPrinter(result) : null;
}

public static NSPrinter static_printerWithName_domain_includeUnavailable_(NSString name, NSString domain, boolean flag) {
	int result = OS.objc_msgSend(OS.class_NSPrinter, OS.sel_printerWithName_1domain_1includeUnavailable_1, name != null ? name.id : 0, domain != null ? domain.id : 0, flag);
	return result != 0 ? new NSPrinter(result) : null;
}

public static NSPrinter printerWithType(NSString type) {
	int result = OS.objc_msgSend(OS.class_NSPrinter, OS.sel_printerWithType_1, type != null ? type.id : 0);
	return result != 0 ? new NSPrinter(result) : null;
}

public NSRect rectForKey(NSString key, NSString table) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectForKey_1inTable_1, key != null ? key.id : 0, table != null ? table.id : 0);
	return result;
}

public NSSize sizeForKey(NSString key, NSString table) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_sizeForKey_1inTable_1, key != null ? key.id : 0, table != null ? table.id : 0);
	return result;
}

public int statusForTable(NSString tableName) {
	return OS.objc_msgSend(this.id, OS.sel_statusForTable_1, tableName != null ? tableName.id : 0);
}

public NSString stringForKey(NSString key, NSString table) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringForKey_1inTable_1, key != null ? key.id : 0, table != null ? table.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSArray stringListForKey(NSString key, NSString table) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringListForKey_1inTable_1, key != null ? key.id : 0, table != null ? table.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSString type() {
	int result = OS.objc_msgSend(this.id, OS.sel_type);
	return result != 0 ? new NSString(result) : null;
}

}

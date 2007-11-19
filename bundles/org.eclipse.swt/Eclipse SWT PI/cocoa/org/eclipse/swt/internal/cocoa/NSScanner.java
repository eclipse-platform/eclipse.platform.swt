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

public class NSScanner extends NSObject {

public NSScanner() {
	super();
}

public NSScanner(int id) {
	super(id);
}

public boolean caseSensitive() {
	return OS.objc_msgSend(this.id, OS.sel_caseSensitive) != 0;
}

public NSCharacterSet charactersToBeSkipped() {
	int result = OS.objc_msgSend(this.id, OS.sel_charactersToBeSkipped);
	return result != 0 ? new NSCharacterSet(result) : null;
}

public NSScanner initWithString(NSString string) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithString_1, string != null ? string.id : 0);
	return result != 0 ? this : null;
}

public boolean isAtEnd() {
	return OS.objc_msgSend(this.id, OS.sel_isAtEnd) != 0;
}

public id locale() {
	int result = OS.objc_msgSend(this.id, OS.sel_locale);
	return result != 0 ? new id(result) : null;
}

public static id localizedScannerWithString(NSString string) {
	int result = OS.objc_msgSend(OS.class_NSScanner, OS.sel_localizedScannerWithString_1, string != null ? string.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean scanCharactersFromSet(NSCharacterSet set, int value) {
	return OS.objc_msgSend(this.id, OS.sel_scanCharactersFromSet_1intoString_1, set != null ? set.id : 0, value) != 0;
}

public boolean scanDecimal(int dcm) {
	return OS.objc_msgSend(this.id, OS.sel_scanDecimal_1, dcm) != 0;
}

public boolean scanDouble(int value) {
	return OS.objc_msgSend(this.id, OS.sel_scanDouble_1, value) != 0;
}

public boolean scanFloat(int value) {
	return OS.objc_msgSend(this.id, OS.sel_scanFloat_1, value) != 0;
}

public boolean scanHexDouble(int result) {
	return OS.objc_msgSend(this.id, OS.sel_scanHexDouble_1, result) != 0;
}

public boolean scanHexFloat(int result) {
	return OS.objc_msgSend(this.id, OS.sel_scanHexFloat_1, result) != 0;
}

public boolean scanHexInt(int value) {
	return OS.objc_msgSend(this.id, OS.sel_scanHexInt_1, value) != 0;
}

public boolean scanHexLongLong(int result) {
	return OS.objc_msgSend(this.id, OS.sel_scanHexLongLong_1, result) != 0;
}

public boolean scanInt(int value) {
	return OS.objc_msgSend(this.id, OS.sel_scanInt_1, value) != 0;
}

public boolean scanInteger(int value) {
	return OS.objc_msgSend(this.id, OS.sel_scanInteger_1, value) != 0;
}

public int scanLocation() {
	return OS.objc_msgSend(this.id, OS.sel_scanLocation);
}

public boolean scanLongLong(int value) {
	return OS.objc_msgSend(this.id, OS.sel_scanLongLong_1, value) != 0;
}

public boolean scanString(NSString string, int value) {
	return OS.objc_msgSend(this.id, OS.sel_scanString_1intoString_1, string != null ? string.id : 0, value) != 0;
}

public boolean scanUpToCharactersFromSet(NSCharacterSet set, int value) {
	return OS.objc_msgSend(this.id, OS.sel_scanUpToCharactersFromSet_1intoString_1, set != null ? set.id : 0, value) != 0;
}

public boolean scanUpToString(NSString string, int value) {
	return OS.objc_msgSend(this.id, OS.sel_scanUpToString_1intoString_1, string != null ? string.id : 0, value) != 0;
}

public static id scannerWithString(NSString string) {
	int result = OS.objc_msgSend(OS.class_NSScanner, OS.sel_scannerWithString_1, string != null ? string.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setCaseSensitive(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCaseSensitive_1, flag);
}

public void setCharactersToBeSkipped(NSCharacterSet set) {
	OS.objc_msgSend(this.id, OS.sel_setCharactersToBeSkipped_1, set != null ? set.id : 0);
}

public void setLocale(id locale) {
	OS.objc_msgSend(this.id, OS.sel_setLocale_1, locale != null ? locale.id : 0);
}

public void setScanLocation(int pos) {
	OS.objc_msgSend(this.id, OS.sel_setScanLocation_1, pos);
}

public NSString string() {
	int result = OS.objc_msgSend(this.id, OS.sel_string);
	return result != 0 ? new NSString(result) : null;
}

}

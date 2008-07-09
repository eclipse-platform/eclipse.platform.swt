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

public class NSData extends NSObject {

public NSData() {
	super();
}

public NSData(int id) {
	super(id);
}

public int bytes() {
	return OS.objc_msgSend(this.id, OS.sel_bytes);
}

public static id data() {
	int result = OS.objc_msgSend(OS.class_NSData, OS.sel_data);
	return result != 0 ? new id(result) : null;
}

public static NSData dataWithBytes(int bytes, int length) {
	int result = OS.objc_msgSend(OS.class_NSData, OS.sel_dataWithBytes_1length_1, bytes, length);
	return result != 0 ? new NSData(result) : null;
}

public static NSData dataWithBytes(byte[] bytes, int length) {
	int result = OS.objc_msgSend(OS.class_NSData, OS.sel_dataWithBytes_1length_1, bytes, length);
	return result != 0 ? new NSData(result) : null;
}

public static id static_dataWithBytesNoCopy_length_(int bytes, int length) {
	int result = OS.objc_msgSend(OS.class_NSData, OS.sel_dataWithBytesNoCopy_1length_1, bytes, length);
	return result != 0 ? new id(result) : null;
}

public static id static_dataWithBytesNoCopy_length_freeWhenDone_(int bytes, int length, boolean b) {
	int result = OS.objc_msgSend(OS.class_NSData, OS.sel_dataWithBytesNoCopy_1length_1freeWhenDone_1, bytes, length, b);
	return result != 0 ? new id(result) : null;
}

public static id static_dataWithContentsOfFile_(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSData, OS.sel_dataWithContentsOfFile_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_dataWithContentsOfFile_options_error_(NSString path, int readOptionsMask, int errorPtr) {
	int result = OS.objc_msgSend(OS.class_NSData, OS.sel_dataWithContentsOfFile_1options_1error_1, path != null ? path.id : 0, readOptionsMask, errorPtr);
	return result != 0 ? new id(result) : null;
}

public static id dataWithContentsOfMappedFile(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSData, OS.sel_dataWithContentsOfMappedFile_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_dataWithContentsOfURL_(NSURL url) {
	int result = OS.objc_msgSend(OS.class_NSData, OS.sel_dataWithContentsOfURL_1, url != null ? url.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_dataWithContentsOfURL_options_error_(NSURL url, int readOptionsMask, int errorPtr) {
	int result = OS.objc_msgSend(OS.class_NSData, OS.sel_dataWithContentsOfURL_1options_1error_1, url != null ? url.id : 0, readOptionsMask, errorPtr);
	return result != 0 ? new id(result) : null;
}

public static id dataWithData(NSData data) {
	int result = OS.objc_msgSend(OS.class_NSData, OS.sel_dataWithData_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString description() {
	int result = OS.objc_msgSend(this.id, OS.sel_description);
	return result != 0 ? new NSString(result) : null;
}

public void getBytes_(int buffer) {
	OS.objc_msgSend(this.id, OS.sel_getBytes_1, buffer);
}

public void getBytes_(byte[] buffer) {
	OS.objc_msgSend(this.id, OS.sel_getBytes_1, buffer);
}

public void getBytes_length_(int buffer, int length) {
	OS.objc_msgSend(this.id, OS.sel_getBytes_1length_1, buffer, length);
}

public void getBytes_range_(int buffer, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_getBytes_1range_1, buffer, range);
}

public NSData initWithBytes(int bytes, int length) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBytes_1length_1, bytes, length);
	return result != 0 ? this : null;
}

public NSData initWithBytesNoCopy_length_(int bytes, int length) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBytesNoCopy_1length_1, bytes, length);
	return result != 0 ? this : null;
}

public NSData initWithBytesNoCopy_length_freeWhenDone_(int bytes, int length, boolean b) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBytesNoCopy_1length_1freeWhenDone_1, bytes, length, b);
	return result != 0 ? this : null;
}

public NSData initWithContentsOfFile_(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfFile_1, path != null ? path.id : 0);
	return result != 0 ? this : null;
}

public NSData initWithContentsOfFile_options_error_(NSString path, int readOptionsMask, int errorPtr) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfFile_1options_1error_1, path != null ? path.id : 0, readOptionsMask, errorPtr);
	return result != 0 ? this : null;
}

public NSData initWithContentsOfMappedFile(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfMappedFile_1, path != null ? path.id : 0);
	return result != 0 ? this : null;
}

public NSData initWithContentsOfURL_(NSURL url) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1, url != null ? url.id : 0);
	return result != 0 ? this : null;
}

public NSData initWithContentsOfURL_options_error_(NSURL url, int readOptionsMask, int errorPtr) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1options_1error_1, url != null ? url.id : 0, readOptionsMask, errorPtr);
	return result != 0 ? this : null;
}

public NSData initWithData(NSData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1, data != null ? data.id : 0);
	return result != 0 ? this : null;
}

public boolean isEqualToData(NSData other) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToData_1, other != null ? other.id : 0) != 0;
}

public int length() {
	return OS.objc_msgSend(this.id, OS.sel_length);
}

public NSData subdataWithRange(NSRange range) {
	int result = OS.objc_msgSend(this.id, OS.sel_subdataWithRange_1, range);
	return result == this.id ? this : (result != 0 ? new NSData(result) : null);
}

public boolean writeToFile_atomically_(NSString path, boolean useAuxiliaryFile) {
	return OS.objc_msgSend(this.id, OS.sel_writeToFile_1atomically_1, path != null ? path.id : 0, useAuxiliaryFile) != 0;
}

public boolean writeToFile_options_error_(NSString path, int writeOptionsMask, int errorPtr) {
	return OS.objc_msgSend(this.id, OS.sel_writeToFile_1options_1error_1, path != null ? path.id : 0, writeOptionsMask, errorPtr) != 0;
}

public boolean writeToURL_atomically_(NSURL url, boolean atomically) {
	return OS.objc_msgSend(this.id, OS.sel_writeToURL_1atomically_1, url != null ? url.id : 0, atomically) != 0;
}

public boolean writeToURL_options_error_(NSURL url, int writeOptionsMask, int errorPtr) {
	return OS.objc_msgSend(this.id, OS.sel_writeToURL_1options_1error_1, url != null ? url.id : 0, writeOptionsMask, errorPtr) != 0;
}

}

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

public class NSString extends NSObject {

public NSString() {
	super();
}

public NSString(long id) {
	super(id);
}

public NSString(id id) {
	super(id);
}

public String getString() {
	char[] buffer = new char[(int)length()];
	getCharacters(buffer);
	return new String(buffer);
}

public NSString initWithString(String str) {
	char[] buffer = new char[str.length()];
	str.getChars(0, buffer.length, buffer, 0);
	return initWithCharacters(buffer, buffer.length);
}

public static NSString stringWith(String str) {
	char[] buffer = new char[str.length()];
	str.getChars(0, buffer.length, buffer, 0);
	return stringWithCharacters(buffer, buffer.length);
}

public long UTF8String() {
	return OS.objc_msgSend(this.id, OS.sel_UTF8String);
}

public char characterAtIndex(long index) {
	return (char)OS.objc_msgSend(this.id, OS.sel_characterAtIndex_, index);
}

public long compare(NSString string) {
	return OS.objc_msgSend(this.id, OS.sel_compare_, string != null ? string.id : 0);
}

public void getCharacters(char[] buffer) {
	OS.objc_msgSend(this.id, OS.sel_getCharacters_, buffer);
}

public void getCharacters(char[] buffer, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_getCharacters_range_, buffer, range);
}

public NSString initWithCharacters(char[] characters, long length) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithCharacters_length_, characters, length);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public boolean isEqualToString(NSString aString) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isEqualToString_, aString != null ? aString.id : 0);
}

public NSString lastPathComponent() {
	long result = OS.objc_msgSend(this.id, OS.sel_lastPathComponent);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public long length() {
	return OS.objc_msgSend(this.id, OS.sel_length);
}

public NSString lowercaseString() {
	long result = OS.objc_msgSend(this.id, OS.sel_lowercaseString);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString pathExtension() {
	long result = OS.objc_msgSend(this.id, OS.sel_pathExtension);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public static NSString string() {
	long result = OS.objc_msgSend(OS.class_NSString, OS.sel_string);
	return result != 0 ? new NSString(result) : null;
}

public NSString stringByAddingPercentEscapesUsingEncoding(long enc) {
	long result = OS.objc_msgSend(this.id, OS.sel_stringByAddingPercentEscapesUsingEncoding_, enc);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAppendingPathComponent(NSString str) {
	long result = OS.objc_msgSend(this.id, OS.sel_stringByAppendingPathComponent_, str != null ? str.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAppendingPathExtension(NSString str) {
	long result = OS.objc_msgSend(this.id, OS.sel_stringByAppendingPathExtension_, str != null ? str.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAppendingString(NSString aString) {
	long result = OS.objc_msgSend(this.id, OS.sel_stringByAppendingString_, aString != null ? aString.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByDeletingLastPathComponent() {
	long result = OS.objc_msgSend(this.id, OS.sel_stringByDeletingLastPathComponent);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByDeletingPathExtension() {
	long result = OS.objc_msgSend(this.id, OS.sel_stringByDeletingPathExtension);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByReplacingOccurrencesOfString(NSString target, NSString replacement) {
	long result = OS.objc_msgSend(this.id, OS.sel_stringByReplacingOccurrencesOfString_withString_, target != null ? target.id : 0, replacement != null ? replacement.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByReplacingPercentEscapesUsingEncoding(long enc) {
	long result = OS.objc_msgSend(this.id, OS.sel_stringByReplacingPercentEscapesUsingEncoding_, enc);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public static NSString stringWithCharacters(char[] characters, long length) {
	long result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithCharacters_length_, characters, length);
	return result != 0 ? new NSString(result) : null;
}

public static NSString stringWithUTF8String(long nullTerminatedCString) {
	long result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithUTF8String_, nullTerminatedCString);
	return result != 0 ? new NSString(result) : null;
}

}

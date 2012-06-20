/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSString extends NSObject {

public NSString() {
	super();
}

public NSString(int /*long*/ id) {
	super(id);
}

public NSString(id id) {
	super(id);
}

public String getString() {
	char[] buffer = new char[(int)/*64*/length()];
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

public int /*long*/ UTF8String() {
	return OS.objc_msgSend(this.id, OS.sel_UTF8String);
}

public int /*long*/ characterAtIndex(int /*long*/ index) {
	return OS.objc_msgSend(this.id, OS.sel_characterAtIndex_, index);
}

public int /*long*/ compare(NSString string) {
	return OS.objc_msgSend(this.id, OS.sel_compare_, string != null ? string.id : 0);
}

public int /*long*/ fileSystemRepresentation() {
	return OS.objc_msgSend(this.id, OS.sel_fileSystemRepresentation);
}

public void getCharacters(char[] buffer) {
	OS.objc_msgSend(this.id, OS.sel_getCharacters_, buffer);
}

public void getCharacters(char[] buffer, NSRange aRange) {
	OS.objc_msgSend(this.id, OS.sel_getCharacters_range_, buffer, aRange);
}

public NSString initWithCharacters(char[] characters, int /*long*/ length) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithCharacters_length_, characters, length);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public boolean isEqualToString(NSString aString) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isEqualToString_, aString != null ? aString.id : 0);
}

public NSString lastPathComponent() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_lastPathComponent);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public int /*long*/ length() {
	return OS.objc_msgSend(this.id, OS.sel_length);
}

public NSString lowercaseString() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_lowercaseString);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString pathExtension() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_pathExtension);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public static NSString string() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSString, OS.sel_string);
	return result != 0 ? new NSString(result) : null;
}

public NSString stringByAddingPercentEscapesUsingEncoding(int /*long*/ enc) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByAddingPercentEscapesUsingEncoding_, enc);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAppendingPathComponent(NSString str) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByAppendingPathComponent_, str != null ? str.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAppendingPathExtension(NSString str) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByAppendingPathExtension_, str != null ? str.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAppendingString(NSString aString) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByAppendingString_, aString != null ? aString.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByDeletingLastPathComponent() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByDeletingLastPathComponent);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByDeletingPathExtension() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByDeletingPathExtension);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByReplacingOccurrencesOfString(NSString target, NSString replacement) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByReplacingOccurrencesOfString_withString_, target != null ? target.id : 0, replacement != null ? replacement.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByReplacingPercentEscapesUsingEncoding(int /*long*/ enc) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByReplacingPercentEscapesUsingEncoding_, enc);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public static NSString stringWithCharacters(char[] characters, int /*long*/ length) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithCharacters_length_, characters, length);
	return result != 0 ? new NSString(result) : null;
}

public static NSString stringWithFormat(NSString format) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithFormat_, format != null ? format.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public static NSString stringWithUTF8String(int /*long*/ nullTerminatedCString) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithUTF8String_, nullTerminatedCString);
	return result != 0 ? new NSString(result) : null;
}

}

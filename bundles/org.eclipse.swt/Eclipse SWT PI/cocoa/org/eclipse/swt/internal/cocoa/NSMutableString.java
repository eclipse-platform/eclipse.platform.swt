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

public class NSMutableString extends NSString {

public NSMutableString() {
	super();
}

public NSMutableString(int id) {
	super(id);
}

public void appendFormat(NSString appendFormat) {
	OS.objc_msgSend(this.id, OS.sel_appendFormat_1, appendFormat != null ? appendFormat.id : 0);
}

public void appendString(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_appendString_1, aString != null ? aString.id : 0);
}

public void deleteCharactersInRange(NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_deleteCharactersInRange_1, range);
}

public NSMutableString initWithCapacity(int capacity) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCapacity_1, capacity);
	return result != 0 ? this : null;
}

public void insertString(NSString aString, int loc) {
	OS.objc_msgSend(this.id, OS.sel_insertString_1atIndex_1, aString != null ? aString.id : 0, loc);
}

public void replaceCharactersInRange(NSRange range, NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_replaceCharactersInRange_1withString_1, range, aString != null ? aString.id : 0);
}

public int replaceOccurrencesOfString(NSString target, NSString replacement, int options, NSRange searchRange) {
	return OS.objc_msgSend(this.id, OS.sel_replaceOccurrencesOfString_1withString_1options_1range_1, target != null ? target.id : 0, replacement != null ? replacement.id : 0, options, searchRange);
}

public void setString(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setString_1, aString != null ? aString.id : 0);
}

public static id stringWithCapacity(int capacity) {
	int result = OS.objc_msgSend(OS.class_NSMutableString, OS.sel_stringWithCapacity_1, capacity);
	return result != 0 ? new id(result) : null;
}

}

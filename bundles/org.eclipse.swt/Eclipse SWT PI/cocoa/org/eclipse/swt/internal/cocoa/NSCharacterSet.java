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

public class NSCharacterSet extends NSObject {

public NSCharacterSet() {
	super();
}

public NSCharacterSet(int id) {
	super(id);
}

public static id alphanumericCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_alphanumericCharacterSet);
	return result != 0 ? new id(result) : null;
}

public NSData bitmapRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_bitmapRepresentation);
	return result != 0 ? new NSData(result) : null;
}

public static id capitalizedLetterCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_capitalizedLetterCharacterSet);
	return result != 0 ? new id(result) : null;
}

public boolean characterIsMember(short aCharacter) {
	return OS.objc_msgSend(this.id, OS.sel_characterIsMember_1, aCharacter) != 0;
}

public static id characterSetWithBitmapRepresentation(NSData data) {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_characterSetWithBitmapRepresentation_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id characterSetWithCharactersInString(NSString aString) {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_characterSetWithCharactersInString_1, aString != null ? aString.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id characterSetWithContentsOfFile(NSString fName) {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_characterSetWithContentsOfFile_1, fName != null ? fName.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id characterSetWithRange(NSRange aRange) {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_characterSetWithRange_1, aRange);
	return result != 0 ? new id(result) : null;
}

public static id controlCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_controlCharacterSet);
	return result != 0 ? new id(result) : null;
}

public static id decimalDigitCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_decimalDigitCharacterSet);
	return result != 0 ? new id(result) : null;
}

public static id decomposableCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_decomposableCharacterSet);
	return result != 0 ? new id(result) : null;
}

public boolean hasMemberInPlane(byte thePlane) {
	return OS.objc_msgSend(this.id, OS.sel_hasMemberInPlane_1, thePlane) != 0;
}

public static id illegalCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_illegalCharacterSet);
	return result != 0 ? new id(result) : null;
}

public NSCharacterSet invertedSet() {
	int result = OS.objc_msgSend(this.id, OS.sel_invertedSet);
	return result == this.id ? this : (result != 0 ? new NSCharacterSet(result) : null);
}

public boolean isSupersetOfSet(NSCharacterSet theOtherSet) {
	return OS.objc_msgSend(this.id, OS.sel_isSupersetOfSet_1, theOtherSet != null ? theOtherSet.id : 0) != 0;
}

public static id letterCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_letterCharacterSet);
	return result != 0 ? new id(result) : null;
}

public boolean longCharacterIsMember(int theLongChar) {
	return OS.objc_msgSend(this.id, OS.sel_longCharacterIsMember_1, theLongChar) != 0;
}

public static id lowercaseLetterCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_lowercaseLetterCharacterSet);
	return result != 0 ? new id(result) : null;
}

public static id newlineCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_newlineCharacterSet);
	return result != 0 ? new id(result) : null;
}

public static id nonBaseCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_nonBaseCharacterSet);
	return result != 0 ? new id(result) : null;
}

public static id punctuationCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_punctuationCharacterSet);
	return result != 0 ? new id(result) : null;
}

public static id symbolCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_symbolCharacterSet);
	return result != 0 ? new id(result) : null;
}

public static id uppercaseLetterCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_uppercaseLetterCharacterSet);
	return result != 0 ? new id(result) : null;
}

public static id whitespaceAndNewlineCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_whitespaceAndNewlineCharacterSet);
	return result != 0 ? new id(result) : null;
}

public static id whitespaceCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_whitespaceCharacterSet);
	return result != 0 ? new id(result) : null;
}

}

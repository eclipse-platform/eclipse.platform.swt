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

public class NSNumber extends NSValue {

public NSNumber() {
	super();
}

public NSNumber(int id) {
	super(id);
}

public boolean boolValue() {
	return OS.objc_msgSend(this.id, OS.sel_boolValue) != 0;
}

public byte charValue() {
	return (byte)OS.objc_msgSend(this.id, OS.sel_charValue);
}

public int compare(NSNumber otherNumber) {
	return OS.objc_msgSend(this.id, OS.sel_compare_1, otherNumber != null ? otherNumber.id : 0);
}

public NSDecimal decimalValue() {
	NSDecimal result = new NSDecimal();
	OS.objc_msgSend_stret(result, this.id, OS.sel_decimalValue);
	return result;
}

public NSString descriptionWithLocale(id locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionWithLocale_1, locale != null ? locale.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public double doubleValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_doubleValue);
}

public float floatValue() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_floatValue);
}

public id initWithBool(boolean value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBool_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithChar(byte value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithChar_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithDouble(double value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDouble_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithFloat(float value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFloat_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithInt(int value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithInt_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithInteger(int value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithInteger_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithLong(int value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithLong_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithLongLong(long value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithLongLong_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithShort(short value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithShort_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithUnsignedChar(byte value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithUnsignedChar_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithUnsignedInt(int value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithUnsignedInt_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithUnsignedInteger(int value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithUnsignedInteger_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithUnsignedLong(int value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithUnsignedLong_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithUnsignedLongLong(long value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithUnsignedLongLong_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithUnsignedShort(short value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithUnsignedShort_1, value);
	return result != 0 ? new id(result) : null;
}

public int intValue() {
	return OS.objc_msgSend(this.id, OS.sel_intValue);
}

public int integerValue() {
	return OS.objc_msgSend(this.id, OS.sel_integerValue);
}

public boolean isEqualToNumber(NSNumber number) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToNumber_1, number != null ? number.id : 0) != 0;
}

public long longLongValue() {
	return (long)OS.objc_msgSend(this.id, OS.sel_longLongValue);
}

public int longValue() {
	return OS.objc_msgSend(this.id, OS.sel_longValue);
}

public static NSNumber numberWithBool(boolean value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithBool_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithChar(byte value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithChar_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithDouble(double value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithDouble_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithFloat(float value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithFloat_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithInt(int value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithInt_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithInteger(int value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithInteger_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithLong(int value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithLong_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithLongLong(long value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithLongLong_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithShort(short value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithShort_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithUnsignedChar(byte value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithUnsignedChar_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithUnsignedInt(int value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithUnsignedInt_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithUnsignedInteger(int value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithUnsignedInteger_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithUnsignedLong(int value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithUnsignedLong_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithUnsignedLongLong(long value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithUnsignedLongLong_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithUnsignedShort(short value) {
	int result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithUnsignedShort_1, value);
	return result != 0 ? new NSNumber(result) : null;
}

public short shortValue() {
	return (short)OS.objc_msgSend(this.id, OS.sel_shortValue);
}

public NSString stringValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringValue);
	return result != 0 ? new NSString(result) : null;
}

public byte unsignedCharValue() {
	return (byte)OS.objc_msgSend(this.id, OS.sel_unsignedCharValue);
}

public int unsignedIntValue() {
	return OS.objc_msgSend(this.id, OS.sel_unsignedIntValue);
}

public int unsignedIntegerValue() {
	return OS.objc_msgSend(this.id, OS.sel_unsignedIntegerValue);
}

public long unsignedLongLongValue() {
	return (long)OS.objc_msgSend(this.id, OS.sel_unsignedLongLongValue);
}

public int unsignedLongValue() {
	return OS.objc_msgSend(this.id, OS.sel_unsignedLongValue);
}

public short unsignedShortValue() {
	return (short)OS.objc_msgSend(this.id, OS.sel_unsignedShortValue);
}

}

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

public class NSDecimalNumber extends NSNumber {

public NSDecimalNumber() {
	super();
}

public NSDecimalNumber(int id) {
	super(id);
}

public int compare(NSNumber decimalNumber) {
	return OS.objc_msgSend(this.id, OS.sel_compare_1, decimalNumber != null ? decimalNumber.id : 0);
}

public NSDecimalNumber decimalNumberByAdding_(NSDecimalNumber decimalNumber) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberByAdding_1, decimalNumber != null ? decimalNumber.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public NSDecimalNumber decimalNumberByAdding_withBehavior_(NSDecimalNumber decimalNumber, id  behavior) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberByAdding_1withBehavior_1, decimalNumber != null ? decimalNumber.id : 0, behavior != null ? behavior.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public NSDecimalNumber decimalNumberByDividingBy_(NSDecimalNumber decimalNumber) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberByDividingBy_1, decimalNumber != null ? decimalNumber.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public NSDecimalNumber decimalNumberByDividingBy_withBehavior_(NSDecimalNumber decimalNumber, id  behavior) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberByDividingBy_1withBehavior_1, decimalNumber != null ? decimalNumber.id : 0, behavior != null ? behavior.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public NSDecimalNumber decimalNumberByMultiplyingBy_(NSDecimalNumber decimalNumber) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberByMultiplyingBy_1, decimalNumber != null ? decimalNumber.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public NSDecimalNumber decimalNumberByMultiplyingBy_withBehavior_(NSDecimalNumber decimalNumber, id  behavior) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberByMultiplyingBy_1withBehavior_1, decimalNumber != null ? decimalNumber.id : 0, behavior != null ? behavior.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public NSDecimalNumber decimalNumberByMultiplyingByPowerOf10_(short power) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberByMultiplyingByPowerOf10_1, power);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public NSDecimalNumber decimalNumberByMultiplyingByPowerOf10_withBehavior_(short power, id  behavior) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberByMultiplyingByPowerOf10_1withBehavior_1, power, behavior != null ? behavior.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public NSDecimalNumber decimalNumberByRaisingToPower_(int power) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberByRaisingToPower_1, power);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public NSDecimalNumber decimalNumberByRaisingToPower_withBehavior_(int power, id  behavior) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberByRaisingToPower_1withBehavior_1, power, behavior != null ? behavior.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public NSDecimalNumber decimalNumberByRoundingAccordingToBehavior(id  behavior) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberByRoundingAccordingToBehavior_1, behavior != null ? behavior.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public NSDecimalNumber decimalNumberBySubtracting_(NSDecimalNumber decimalNumber) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberBySubtracting_1, decimalNumber != null ? decimalNumber.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public NSDecimalNumber decimalNumberBySubtracting_withBehavior_(NSDecimalNumber decimalNumber, id  behavior) {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalNumberBySubtracting_1withBehavior_1, decimalNumber != null ? decimalNumber.id : 0, behavior != null ? behavior.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDecimalNumber(result) : null);
}

public static NSDecimalNumber decimalNumberWithDecimal(NSDecimal dcm) {
	int result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_decimalNumberWithDecimal_1, dcm);
	return result != 0 ? new NSDecimalNumber(result) : null;
}

public static NSDecimalNumber decimalNumberWithMantissa(long mantissa, short exponent, boolean flag) {
	int result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_decimalNumberWithMantissa_1exponent_1isNegative_1, mantissa, exponent, flag);
	return result != 0 ? new NSDecimalNumber(result) : null;
}

public static NSDecimalNumber static_decimalNumberWithString_(NSString numberValue) {
	int result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_decimalNumberWithString_1, numberValue != null ? numberValue.id : 0);
	return result != 0 ? new NSDecimalNumber(result) : null;
}

public static NSDecimalNumber static_decimalNumberWithString_locale_(NSString numberValue, id locale) {
	int result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_decimalNumberWithString_1locale_1, numberValue != null ? numberValue.id : 0, locale != null ? locale.id : 0);
	return result != 0 ? new NSDecimalNumber(result) : null;
}

public NSDecimal decimalValue() {
	NSDecimal result = new NSDecimal();
	OS.objc_msgSend_stret(result, this.id, OS.sel_decimalValue);
	return result;
}

public static id  defaultBehavior() {
	int result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_defaultBehavior);
	return result != 0 ? new id (result) : null;
}

public NSString descriptionWithLocale(id locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionWithLocale_1, locale != null ? locale.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public double doubleValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_doubleValue);
}

public id initWithDecimal(NSDecimal dcm) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDecimal_1, dcm);
	return result != 0 ? new id(result) : null;
}

public id initWithMantissa(long mantissa, short exponent, boolean flag) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithMantissa_1exponent_1isNegative_1, mantissa, exponent, flag);
	return result != 0 ? new id(result) : null;
}

public id initWithString_(NSString numberValue) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithString_1, numberValue != null ? numberValue.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithString_locale_(NSString numberValue, id locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithString_1locale_1, numberValue != null ? numberValue.id : 0, locale != null ? locale.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSDecimalNumber maximumDecimalNumber() {
	int result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_maximumDecimalNumber);
	return result != 0 ? new NSDecimalNumber(result) : null;
}

public static NSDecimalNumber minimumDecimalNumber() {
	int result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_minimumDecimalNumber);
	return result != 0 ? new NSDecimalNumber(result) : null;
}

public static NSDecimalNumber notANumber() {
	int result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_notANumber);
	return result != 0 ? new NSDecimalNumber(result) : null;
}

public int objCType() {
	return OS.objc_msgSend(this.id, OS.sel_objCType);
}

public static NSDecimalNumber one() {
	int result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_one);
	return result != 0 ? new NSDecimalNumber(result) : null;
}

public static void setDefaultBehavior(id  behavior) {
	OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_setDefaultBehavior_1, behavior != null ? behavior.id : 0);
}

public static NSDecimalNumber zero() {
	int result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_zero);
	return result != 0 ? new NSDecimalNumber(result) : null;
}

}

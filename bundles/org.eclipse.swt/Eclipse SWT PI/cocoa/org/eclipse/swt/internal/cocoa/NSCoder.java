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

public class NSCoder extends NSObject {

public NSCoder() {
	super();
}

public NSCoder(int id) {
	super(id);
}

public boolean allowsKeyedCoding() {
	return OS.objc_msgSend(this.id, OS.sel_allowsKeyedCoding) != 0;
}

public boolean containsValueForKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_containsValueForKey_1, key != null ? key.id : 0) != 0;
}

public void decodeArrayOfObjCType(int itemType, int count, int array) {
	OS.objc_msgSend(this.id, OS.sel_decodeArrayOfObjCType_1count_1at_1, itemType, count, array);
}

public boolean decodeBoolForKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_decodeBoolForKey_1, key != null ? key.id : 0) != 0;
}

public int decodeBytesForKey(NSString key, int lengthp) {
	return OS.objc_msgSend(this.id, OS.sel_decodeBytesForKey_1returnedLength_1, key != null ? key.id : 0, lengthp);
}

public int decodeBytesWithReturnedLength(int lengthp) {
	return OS.objc_msgSend(this.id, OS.sel_decodeBytesWithReturnedLength_1, lengthp);
}

public NSData decodeDataObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_decodeDataObject);
	return result != 0 ? new NSData(result) : null;
}

public double decodeDoubleForKey(NSString key) {
	return OS.objc_msgSend_fpret(this.id, OS.sel_decodeDoubleForKey_1, key != null ? key.id : 0);
}

public float decodeFloatForKey(NSString key) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_decodeFloatForKey_1, key != null ? key.id : 0);
}

public int decodeInt32ForKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_decodeInt32ForKey_1, key != null ? key.id : 0);
}

public long decodeInt64ForKey(NSString key) {
	return (long)OS.objc_msgSend(this.id, OS.sel_decodeInt64ForKey_1, key != null ? key.id : 0);
}

public int decodeIntForKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_decodeIntForKey_1, key != null ? key.id : 0);
}

public int decodeIntegerForKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_decodeIntegerForKey_1, key != null ? key.id : 0);
}

public id decodeNXObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_decodeNXObject);
	return result != 0 ? new id(result) : null;
}

public id decodeObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_decodeObject);
	return result != 0 ? new id(result) : null;
}

public id decodeObjectForKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_decodeObjectForKey_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSPoint decodePoint() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_decodePoint);
	return result;
}

public NSPoint decodePointForKey(NSString key) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_decodePointForKey_1, key != null ? key.id : 0);
	return result;
}

public id decodePropertyList() {
	int result = OS.objc_msgSend(this.id, OS.sel_decodePropertyList);
	return result != 0 ? new id(result) : null;
}

public NSRect decodeRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_decodeRect);
	return result;
}

public NSRect decodeRectForKey(NSString key) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_decodeRectForKey_1, key != null ? key.id : 0);
	return result;
}

public NSSize decodeSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_decodeSize);
	return result;
}

public NSSize decodeSizeForKey(NSString key) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_decodeSizeForKey_1, key != null ? key.id : 0);
	return result;
}

public void decodeValueOfObjCType(int type, int data) {
	OS.objc_msgSend(this.id, OS.sel_decodeValueOfObjCType_1at_1, type, data);
}

public void decodeValuesOfObjCTypes(int decodeValuesOfObjCTypes) {
	OS.objc_msgSend(this.id, OS.sel_decodeValuesOfObjCTypes_1, decodeValuesOfObjCTypes);
}

public void encodeArrayOfObjCType(int type, int count, int array) {
	OS.objc_msgSend(this.id, OS.sel_encodeArrayOfObjCType_1count_1at_1, type, count, array);
}

public void encodeBool(boolean boolv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeBool_1forKey_1, boolv, key != null ? key.id : 0);
}

public void encodeBycopyObject(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_encodeBycopyObject_1, anObject != null ? anObject.id : 0);
}

public void encodeByrefObject(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_encodeByrefObject_1, anObject != null ? anObject.id : 0);
}

public void encodeBytes_length_(int byteaddr, int length) {
	OS.objc_msgSend(this.id, OS.sel_encodeBytes_1length_1, byteaddr, length);
}

public void encodeBytes_length_forKey_(int bytesp, int lenv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeBytes_1length_1forKey_1, bytesp, lenv, key != null ? key.id : 0);
}

public void encodeConditionalObject_(id object) {
	OS.objc_msgSend(this.id, OS.sel_encodeConditionalObject_1, object != null ? object.id : 0);
}

public void encodeConditionalObject_forKey_(id objv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeConditionalObject_1forKey_1, objv != null ? objv.id : 0, key != null ? key.id : 0);
}

public void encodeDataObject(NSData data) {
	OS.objc_msgSend(this.id, OS.sel_encodeDataObject_1, data != null ? data.id : 0);
}

public void encodeDouble(double realv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeDouble_1forKey_1, realv, key != null ? key.id : 0);
}

public void encodeFloat(float realv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeFloat_1forKey_1, realv, key != null ? key.id : 0);
}

public void encodeInt32(int intv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeInt32_1forKey_1, intv, key != null ? key.id : 0);
}

public void encodeInt64(long intv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeInt64_1forKey_1, intv, key != null ? key.id : 0);
}

public void encodeInt(int intv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeInt_1forKey_1, intv, key != null ? key.id : 0);
}

public void encodeInteger(int intv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeInteger_1forKey_1, intv, key != null ? key.id : 0);
}

public void encodeNXObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_encodeNXObject_1, object != null ? object.id : 0);
}

public void encodeObject_(id object) {
	OS.objc_msgSend(this.id, OS.sel_encodeObject_1, object != null ? object.id : 0);
}

public void encodeObject_forKey_(id objv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeObject_1forKey_1, objv != null ? objv.id : 0, key != null ? key.id : 0);
}

public void encodePoint_(NSPoint point) {
	OS.objc_msgSend(this.id, OS.sel_encodePoint_1, point);
}

public void encodePoint_forKey_(NSPoint point, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodePoint_1forKey_1, point, key != null ? key.id : 0);
}

public void encodePropertyList(id aPropertyList) {
	OS.objc_msgSend(this.id, OS.sel_encodePropertyList_1, aPropertyList != null ? aPropertyList.id : 0);
}

public void encodeRect_(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_encodeRect_1, rect);
}

public void encodeRect_forKey_(NSRect rect, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeRect_1forKey_1, rect, key != null ? key.id : 0);
}

public void encodeRootObject(id rootObject) {
	OS.objc_msgSend(this.id, OS.sel_encodeRootObject_1, rootObject != null ? rootObject.id : 0);
}

public void encodeSize_(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_encodeSize_1, size);
}

public void encodeSize_forKey_(NSSize size, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeSize_1forKey_1, size, key != null ? key.id : 0);
}

public void encodeValueOfObjCType(int type, int addr) {
	OS.objc_msgSend(this.id, OS.sel_encodeValueOfObjCType_1at_1, type, addr);
}

public void encodeValuesOfObjCTypes(int encodeValuesOfObjCTypes) {
	OS.objc_msgSend(this.id, OS.sel_encodeValuesOfObjCTypes_1, encodeValuesOfObjCTypes);
}

public int objectZone() {
	return OS.objc_msgSend(this.id, OS.sel_objectZone);
}

public void setObjectZone(int zone) {
	OS.objc_msgSend(this.id, OS.sel_setObjectZone_1, zone);
}

public int systemVersion() {
	return OS.objc_msgSend(this.id, OS.sel_systemVersion);
}

public int versionForClassName(NSString className) {
	return OS.objc_msgSend(this.id, OS.sel_versionForClassName_1, className != null ? className.id : 0);
}

}

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

public class NSAppleEventDescriptor extends NSObject {

public NSAppleEventDescriptor() {
	super();
}

public NSAppleEventDescriptor(int id) {
	super(id);
}

public int aeDesc() {
	return OS.objc_msgSend(this.id, OS.sel_aeDesc);
}

public static NSAppleEventDescriptor appleEventWithEventClass(int eventClass, int eventID, NSAppleEventDescriptor targetDescriptor, short returnID, int transactionID) {
	int result = OS.objc_msgSend(OS.class_NSAppleEventDescriptor, OS.sel_appleEventWithEventClass_1eventID_1targetDescriptor_1returnID_1transactionID_1, eventClass, eventID, targetDescriptor != null ? targetDescriptor.id : 0, returnID, transactionID);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public NSAppleEventDescriptor attributeDescriptorForKeyword(int keyword) {
	int result = OS.objc_msgSend(this.id, OS.sel_attributeDescriptorForKeyword_1, keyword);
	return result == this.id ? this : (result != 0 ? new NSAppleEventDescriptor(result) : null);
}

public boolean booleanValue() {
	return OS.objc_msgSend(this.id, OS.sel_booleanValue) != 0;
}

public NSAppleEventDescriptor coerceToDescriptorType(int descriptorType) {
	int result = OS.objc_msgSend(this.id, OS.sel_coerceToDescriptorType_1, descriptorType);
	return result == this.id ? this : (result != 0 ? new NSAppleEventDescriptor(result) : null);
}

public NSData data() {
	int result = OS.objc_msgSend(this.id, OS.sel_data);
	return result != 0 ? new NSData(result) : null;
}

public NSAppleEventDescriptor descriptorAtIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptorAtIndex_1, index);
	return result == this.id ? this : (result != 0 ? new NSAppleEventDescriptor(result) : null);
}

public NSAppleEventDescriptor descriptorForKeyword(int keyword) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptorForKeyword_1, keyword);
	return result == this.id ? this : (result != 0 ? new NSAppleEventDescriptor(result) : null);
}

public int descriptorType() {
	return OS.objc_msgSend(this.id, OS.sel_descriptorType);
}

public static NSAppleEventDescriptor descriptorWithBoolean(boolean b) {
	int result = OS.objc_msgSend(OS.class_NSAppleEventDescriptor, OS.sel_descriptorWithBoolean_1, b);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public static NSAppleEventDescriptor static_descriptorWithDescriptorType_bytes_length_(int descriptorType, int bytes, int byteCount) {
	int result = OS.objc_msgSend(OS.class_NSAppleEventDescriptor, OS.sel_descriptorWithDescriptorType_1bytes_1length_1, descriptorType, bytes, byteCount);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public static NSAppleEventDescriptor static_descriptorWithDescriptorType_data_(int descriptorType, NSData data) {
	int result = OS.objc_msgSend(OS.class_NSAppleEventDescriptor, OS.sel_descriptorWithDescriptorType_1data_1, descriptorType, data != null ? data.id : 0);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public static NSAppleEventDescriptor descriptorWithEnumCode(int enumerator) {
	int result = OS.objc_msgSend(OS.class_NSAppleEventDescriptor, OS.sel_descriptorWithEnumCode_1, enumerator);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public static NSAppleEventDescriptor descriptorWithInt32(int signedInt) {
	int result = OS.objc_msgSend(OS.class_NSAppleEventDescriptor, OS.sel_descriptorWithInt32_1, signedInt);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public static NSAppleEventDescriptor descriptorWithString(NSString string) {
	int result = OS.objc_msgSend(OS.class_NSAppleEventDescriptor, OS.sel_descriptorWithString_1, string != null ? string.id : 0);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public static NSAppleEventDescriptor descriptorWithTypeCode(int typeCode) {
	int result = OS.objc_msgSend(OS.class_NSAppleEventDescriptor, OS.sel_descriptorWithTypeCode_1, typeCode);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public int enumCodeValue() {
	return OS.objc_msgSend(this.id, OS.sel_enumCodeValue);
}

public int eventClass() {
	return OS.objc_msgSend(this.id, OS.sel_eventClass);
}

public int eventID() {
	return OS.objc_msgSend(this.id, OS.sel_eventID);
}

public NSAppleEventDescriptor initListDescriptor() {
	int result = OS.objc_msgSend(this.id, OS.sel_initListDescriptor);
	return result != 0 ? this : null;
}

public NSAppleEventDescriptor initRecordDescriptor() {
	int result = OS.objc_msgSend(this.id, OS.sel_initRecordDescriptor);
	return result != 0 ? this : null;
}

public NSAppleEventDescriptor initWithAEDescNoCopy(int aeDesc) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithAEDescNoCopy_1, aeDesc);
	return result != 0 ? this : null;
}

public NSAppleEventDescriptor initWithDescriptorType_bytes_length_(int descriptorType, int bytes, int byteCount) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDescriptorType_1bytes_1length_1, descriptorType, bytes, byteCount);
	return result != 0 ? this : null;
}

public NSAppleEventDescriptor initWithDescriptorType_data_(int descriptorType, NSData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDescriptorType_1data_1, descriptorType, data != null ? data.id : 0);
	return result != 0 ? this : null;
}

public NSAppleEventDescriptor initWithEventClass(int eventClass, int eventID, NSAppleEventDescriptor targetDescriptor, short returnID, int transactionID) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithEventClass_1eventID_1targetDescriptor_1returnID_1transactionID_1, eventClass, eventID, targetDescriptor != null ? targetDescriptor.id : 0, returnID, transactionID);
	return result != 0 ? this : null;
}

public void insertDescriptor(NSAppleEventDescriptor descriptor, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertDescriptor_1atIndex_1, descriptor != null ? descriptor.id : 0, index);
}

public int int32Value() {
	return OS.objc_msgSend(this.id, OS.sel_int32Value);
}

public int keywordForDescriptorAtIndex(int index) {
	return OS.objc_msgSend(this.id, OS.sel_keywordForDescriptorAtIndex_1, index);
}

public static NSAppleEventDescriptor listDescriptor() {
	int result = OS.objc_msgSend(OS.class_NSAppleEventDescriptor, OS.sel_listDescriptor);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public static NSAppleEventDescriptor nullDescriptor() {
	int result = OS.objc_msgSend(OS.class_NSAppleEventDescriptor, OS.sel_nullDescriptor);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public int numberOfItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfItems);
}

public NSAppleEventDescriptor paramDescriptorForKeyword(int keyword) {
	int result = OS.objc_msgSend(this.id, OS.sel_paramDescriptorForKeyword_1, keyword);
	return result == this.id ? this : (result != 0 ? new NSAppleEventDescriptor(result) : null);
}

public static NSAppleEventDescriptor recordDescriptor() {
	int result = OS.objc_msgSend(OS.class_NSAppleEventDescriptor, OS.sel_recordDescriptor);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public void removeDescriptorAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removeDescriptorAtIndex_1, index);
}

public void removeDescriptorWithKeyword(int keyword) {
	OS.objc_msgSend(this.id, OS.sel_removeDescriptorWithKeyword_1, keyword);
}

public void removeParamDescriptorWithKeyword(int keyword) {
	OS.objc_msgSend(this.id, OS.sel_removeParamDescriptorWithKeyword_1, keyword);
}

public short returnID() {
	return (short)OS.objc_msgSend(this.id, OS.sel_returnID);
}

public void setAttributeDescriptor(NSAppleEventDescriptor descriptor, int keyword) {
	OS.objc_msgSend(this.id, OS.sel_setAttributeDescriptor_1forKeyword_1, descriptor != null ? descriptor.id : 0, keyword);
}

public void setDescriptor(NSAppleEventDescriptor descriptor, int keyword) {
	OS.objc_msgSend(this.id, OS.sel_setDescriptor_1forKeyword_1, descriptor != null ? descriptor.id : 0, keyword);
}

public void setParamDescriptor(NSAppleEventDescriptor descriptor, int keyword) {
	OS.objc_msgSend(this.id, OS.sel_setParamDescriptor_1forKeyword_1, descriptor != null ? descriptor.id : 0, keyword);
}

public NSString stringValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringValue);
	return result != 0 ? new NSString(result) : null;
}

public int transactionID() {
	return OS.objc_msgSend(this.id, OS.sel_transactionID);
}

public int typeCodeValue() {
	return OS.objc_msgSend(this.id, OS.sel_typeCodeValue);
}

}

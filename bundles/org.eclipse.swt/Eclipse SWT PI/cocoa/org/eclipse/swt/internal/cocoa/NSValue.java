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

public class NSValue extends NSObject {

public NSValue() {
	super();
}

public NSValue(int id) {
	super(id);
}

public void getValue(int value) {
	OS.objc_msgSend(this.id, OS.sel_getValue_1, value);
}

public id initWithBytes(int value, int type) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBytes_1objCType_1, value, type);
	return result != 0 ? new id(result) : null;
}

public boolean isEqualToValue(NSValue value) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToValue_1, value != null ? value.id : 0) != 0;
}

public id nonretainedObjectValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_nonretainedObjectValue);
	return result != 0 ? new id(result) : null;
}

public int objCType() {
	return OS.objc_msgSend(this.id, OS.sel_objCType);
}

public NSPoint pointValue() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_pointValue);
	return result;
}

public int pointerValue() {
	return OS.objc_msgSend(this.id, OS.sel_pointerValue);
}

public NSRange rangeValue() {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeValue);
	return result;
}

public NSRect rectValue() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectValue);
	return result;
}

public NSSize sizeValue() {
	NSSize result = new NSSize();
	OS.objc_msgSend_struct(result, this.id, OS.sel_sizeValue);
	return result;
}

public static NSValue value(int value, int type) {
	int result = OS.objc_msgSend(OS.class_NSValue, OS.sel_value_1withObjCType_1, value, type);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithBytes(int value, int type) {
	int result = OS.objc_msgSend(OS.class_NSValue, OS.sel_valueWithBytes_1objCType_1, value, type);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithNonretainedObject(id anObject) {
	int result = OS.objc_msgSend(OS.class_NSValue, OS.sel_valueWithNonretainedObject_1, anObject != null ? anObject.id : 0);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithPoint(NSPoint point) {
	int result = OS.objc_msgSend(OS.class_NSValue, OS.sel_valueWithPoint_1, point);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithPointer(int pointer) {
	int result = OS.objc_msgSend(OS.class_NSValue, OS.sel_valueWithPointer_1, pointer);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithRange(NSRange range) {
	int result = OS.objc_msgSend(OS.class_NSValue, OS.sel_valueWithRange_1, range);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithRect(NSRect rect) {
	int result = OS.objc_msgSend(OS.class_NSValue, OS.sel_valueWithRect_1, rect);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithSize(NSSize size) {
	int result = OS.objc_msgSend(OS.class_NSValue, OS.sel_valueWithSize_1, size);
	return result != 0 ? new NSValue(result) : null;
}

}

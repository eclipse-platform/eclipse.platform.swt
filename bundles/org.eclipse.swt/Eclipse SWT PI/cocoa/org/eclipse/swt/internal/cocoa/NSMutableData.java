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

public class NSMutableData extends NSData {

public NSMutableData() {
	super();
}

public NSMutableData(int id) {
	super(id);
}

public void appendBytes(int bytes, int length) {
	OS.objc_msgSend(this.id, OS.sel_appendBytes_1length_1, bytes, length);
}

public void appendData(NSData other) {
	OS.objc_msgSend(this.id, OS.sel_appendData_1, other != null ? other.id : 0);
}

public static id dataWithCapacity(int aNumItems) {
	int result = OS.objc_msgSend(OS.class_NSMutableData, OS.sel_dataWithCapacity_1, aNumItems);
	return result != 0 ? new id(result) : null;
}

public static id dataWithLength(int length) {
	int result = OS.objc_msgSend(OS.class_NSMutableData, OS.sel_dataWithLength_1, length);
	return result != 0 ? new id(result) : null;
}

public void increaseLengthBy(int extraLength) {
	OS.objc_msgSend(this.id, OS.sel_increaseLengthBy_1, extraLength);
}

public NSMutableData initWithCapacity(int capacity) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCapacity_1, capacity);
	return result != 0 ? this : null;
}

public NSMutableData initWithLength(int length) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithLength_1, length);
	return result != 0 ? this : null;
}

public int mutableBytes() {
	return OS.objc_msgSend(this.id, OS.sel_mutableBytes);
}

public void replaceBytesInRange_withBytes_(NSRange range, int bytes) {
	OS.objc_msgSend(this.id, OS.sel_replaceBytesInRange_1withBytes_1, range, bytes);
}

public void replaceBytesInRange_withBytes_length_(NSRange range, int replacementBytes, int replacementLength) {
	OS.objc_msgSend(this.id, OS.sel_replaceBytesInRange_1withBytes_1length_1, range, replacementBytes, replacementLength);
}

public void resetBytesInRange(NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_resetBytesInRange_1, range);
}

public void setData(NSData data) {
	OS.objc_msgSend(this.id, OS.sel_setData_1, data != null ? data.id : 0);
}

public void setLength(int length) {
	OS.objc_msgSend(this.id, OS.sel_setLength_1, length);
}

}

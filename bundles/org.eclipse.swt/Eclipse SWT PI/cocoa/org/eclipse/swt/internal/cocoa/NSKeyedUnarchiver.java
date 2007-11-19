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

public class NSKeyedUnarchiver extends NSCoder {

public NSKeyedUnarchiver() {
	super();
}

public NSKeyedUnarchiver(int id) {
	super(id);
}

public int classForClassName_(NSString codedName) {
	return OS.objc_msgSend(this.id, OS.sel_classForClassName_1, codedName != null ? codedName.id : 0);
}

public static int static_classForClassName_(NSString codedName) {
	return OS.objc_msgSend(OS.class_NSKeyedUnarchiver, OS.sel_classForClassName_1, codedName != null ? codedName.id : 0);
}

public boolean containsValueForKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_containsValueForKey_1, key != null ? key.id : 0) != 0;
}

public boolean decodeBoolForKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_decodeBoolForKey_1, key != null ? key.id : 0) != 0;
}

public int decodeBytesForKey(NSString key, int lengthp) {
	return OS.objc_msgSend(this.id, OS.sel_decodeBytesForKey_1returnedLength_1, key != null ? key.id : 0, lengthp);
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

public id decodeObjectForKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_decodeObjectForKey_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void finishDecoding() {
	OS.objc_msgSend(this.id, OS.sel_finishDecoding);
}

public id initForReadingWithData(NSData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initForReadingWithData_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setClass_forClassName_(int cls, NSString codedName) {
	OS.objc_msgSend(this.id, OS.sel_setClass_1forClassName_1, cls, codedName != null ? codedName.id : 0);
}

public static void static_setClass_forClassName_(int cls, NSString codedName) {
	OS.objc_msgSend(OS.class_NSKeyedUnarchiver, OS.sel_setClass_1forClassName_1, cls, codedName != null ? codedName.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public static id unarchiveObjectWithData(NSData data) {
	int result = OS.objc_msgSend(OS.class_NSKeyedUnarchiver, OS.sel_unarchiveObjectWithData_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id unarchiveObjectWithFile(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSKeyedUnarchiver, OS.sel_unarchiveObjectWithFile_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

}

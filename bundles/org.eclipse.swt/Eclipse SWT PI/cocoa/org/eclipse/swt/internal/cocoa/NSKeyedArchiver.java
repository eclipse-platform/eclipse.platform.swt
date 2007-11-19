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

public class NSKeyedArchiver extends NSCoder {

public NSKeyedArchiver() {
	super();
}

public NSKeyedArchiver(int id) {
	super(id);
}

public static boolean archiveRootObject(id rootObject, NSString path) {
	return OS.objc_msgSend(OS.class_NSKeyedArchiver, OS.sel_archiveRootObject_1toFile_1, rootObject != null ? rootObject.id : 0, path != null ? path.id : 0) != 0;
}

public static NSData archivedDataWithRootObject(id rootObject) {
	int result = OS.objc_msgSend(OS.class_NSKeyedArchiver, OS.sel_archivedDataWithRootObject_1, rootObject != null ? rootObject.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public NSString classNameForClass_(int cls) {
	int result = OS.objc_msgSend(this.id, OS.sel_classNameForClass_1, cls);
	return result != 0 ? new NSString(result) : null;
}

public static NSString static_classNameForClass_(int cls) {
	int result = OS.objc_msgSend(OS.class_NSKeyedArchiver, OS.sel_classNameForClass_1, cls);
	return result != 0 ? new NSString(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void encodeBool(boolean boolv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeBool_1forKey_1, boolv, key != null ? key.id : 0);
}

public void encodeBytes(int bytesp, int lenv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeBytes_1length_1forKey_1, bytesp, lenv, key != null ? key.id : 0);
}

public void encodeConditionalObject(id objv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeConditionalObject_1forKey_1, objv != null ? objv.id : 0, key != null ? key.id : 0);
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

public void encodeObject(id objv, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_encodeObject_1forKey_1, objv != null ? objv.id : 0, key != null ? key.id : 0);
}

public void finishEncoding() {
	OS.objc_msgSend(this.id, OS.sel_finishEncoding);
}

public id initForWritingWithMutableData(NSMutableData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initForWritingWithMutableData_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public int outputFormat() {
	return OS.objc_msgSend(this.id, OS.sel_outputFormat);
}

public static void static_setClassName_forClass_(NSString codedName, int cls) {
	OS.objc_msgSend(OS.class_NSKeyedArchiver, OS.sel_setClassName_1forClass_1, codedName != null ? codedName.id : 0, cls);
}

public void setClassName_forClass_(NSString codedName, int cls) {
	OS.objc_msgSend(this.id, OS.sel_setClassName_1forClass_1, codedName != null ? codedName.id : 0, cls);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setOutputFormat(int format) {
	OS.objc_msgSend(this.id, OS.sel_setOutputFormat_1, format);
}

}

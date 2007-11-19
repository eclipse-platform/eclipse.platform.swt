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

public class NSUnarchiver extends NSCoder {

public NSUnarchiver() {
	super();
}

public NSUnarchiver(int id) {
	super(id);
}

public static NSString static_classNameDecodedForArchiveClassName_(NSString inArchiveName) {
	int result = OS.objc_msgSend(OS.class_NSUnarchiver, OS.sel_classNameDecodedForArchiveClassName_1, inArchiveName != null ? inArchiveName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString classNameDecodedForArchiveClassName_(NSString inArchiveName) {
	int result = OS.objc_msgSend(this.id, OS.sel_classNameDecodedForArchiveClassName_1, inArchiveName != null ? inArchiveName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public void decodeClassName_asClassName_(NSString inArchiveName, NSString trueName) {
	OS.objc_msgSend(this.id, OS.sel_decodeClassName_1asClassName_1, inArchiveName != null ? inArchiveName.id : 0, trueName != null ? trueName.id : 0);
}

public static void static_decodeClassName_asClassName_(NSString inArchiveName, NSString trueName) {
	OS.objc_msgSend(OS.class_NSUnarchiver, OS.sel_decodeClassName_1asClassName_1, inArchiveName != null ? inArchiveName.id : 0, trueName != null ? trueName.id : 0);
}

public id initForReadingWithData(NSData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initForReadingWithData_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isAtEnd() {
	return OS.objc_msgSend(this.id, OS.sel_isAtEnd) != 0;
}

public int objectZone() {
	return OS.objc_msgSend(this.id, OS.sel_objectZone);
}

public void replaceObject(id object, id newObject) {
	OS.objc_msgSend(this.id, OS.sel_replaceObject_1withObject_1, object != null ? object.id : 0, newObject != null ? newObject.id : 0);
}

public void setObjectZone(int zone) {
	OS.objc_msgSend(this.id, OS.sel_setObjectZone_1, zone);
}

public int systemVersion() {
	return OS.objc_msgSend(this.id, OS.sel_systemVersion);
}

public static id unarchiveObjectWithData(NSData data) {
	int result = OS.objc_msgSend(OS.class_NSUnarchiver, OS.sel_unarchiveObjectWithData_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id unarchiveObjectWithFile(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSUnarchiver, OS.sel_unarchiveObjectWithFile_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

}

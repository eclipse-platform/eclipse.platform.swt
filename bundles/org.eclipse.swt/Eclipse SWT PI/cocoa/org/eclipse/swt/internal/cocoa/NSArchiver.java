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

public class NSArchiver extends NSObject {

public NSArchiver() {
	super();
}

public NSArchiver(int id) {
	super(id);
}

public static boolean archiveRootObject(id rootObject, NSString path) {
	return OS.objc_msgSend(OS.class_NSArchiver, OS.sel_archiveRootObject_1toFile_1, rootObject != null ? rootObject.id : 0, path != null ? path.id : 0) != 0;
}

public static NSData archivedDataWithRootObject(id rootObject) {
	int result = OS.objc_msgSend(OS.class_NSArchiver, OS.sel_archivedDataWithRootObject_1, rootObject != null ? rootObject.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public NSMutableData archiverData() {
	int result = OS.objc_msgSend(this.id, OS.sel_archiverData);
	return result != 0 ? new NSMutableData(result) : null;
}

public NSString classNameEncodedForTrueClassName(NSString trueName) {
	int result = OS.objc_msgSend(this.id, OS.sel_classNameEncodedForTrueClassName_1, trueName != null ? trueName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public void encodeClassName(NSString trueName, NSString inArchiveName) {
	OS.objc_msgSend(this.id, OS.sel_encodeClassName_1intoClassName_1, trueName != null ? trueName.id : 0, inArchiveName != null ? inArchiveName.id : 0);
}

public void encodeConditionalObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_encodeConditionalObject_1, object != null ? object.id : 0);
}

public void encodeRootObject(id rootObject) {
	OS.objc_msgSend(this.id, OS.sel_encodeRootObject_1, rootObject != null ? rootObject.id : 0);
}

public NSArchiver initForWritingWithMutableData(NSMutableData mdata) {
	int result = OS.objc_msgSend(this.id, OS.sel_initForWritingWithMutableData_1, mdata != null ? mdata.id : 0);
	return result != 0 ? this : null;
}

public void replaceObject(id object, id newObject) {
	OS.objc_msgSend(this.id, OS.sel_replaceObject_1withObject_1, object != null ? object.id : 0, newObject != null ? newObject.id : 0);
}

}

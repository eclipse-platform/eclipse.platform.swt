/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSFileManager extends NSObject {

public NSFileManager() {
	super();
}

public NSFileManager(int /*long*/ id) {
	super(id);
}

public NSFileManager(id id) {
	super(id);
}

public NSArray URLsForDirectory(int /*long*/ directory, int /*long*/ domainMask) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_URLsForDirectory_inDomains_, directory, domainMask);
	return result != 0 ? new NSArray(result) : null;
}

public boolean createFileAtPath(NSString path, NSData data, NSDictionary attr) {
	return OS.objc_msgSend_bool(this.id, OS.sel_createFileAtPath_contents_attributes_, path != null ? path.id : 0, data != null ? data.id : 0, attr != null ? attr.id : 0);
}

public static NSFileManager defaultManager() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSFileManager, OS.sel_defaultManager);
	return result != 0 ? new NSFileManager(result) : null;
}

public NSDirectoryEnumerator enumeratorAtPath(NSString path) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_enumeratorAtPath_, path != null ? path.id : 0);
	return result != 0 ? new NSDirectoryEnumerator(result) : null;
}

public boolean fileExistsAtPath(NSString path) {
	return OS.objc_msgSend_bool(this.id, OS.sel_fileExistsAtPath_, path != null ? path.id : 0);
}

public boolean fileExistsAtPath(NSString path, int /*long*/ isDirectory) {
	return OS.objc_msgSend_bool(this.id, OS.sel_fileExistsAtPath_isDirectory_, path != null ? path.id : 0, isDirectory);
}

public boolean isExecutableFileAtPath(NSString path) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isExecutableFileAtPath_, path != null ? path.id : 0);
}

public boolean removeItemAtPath(NSString path, int /*long*/ error) {
	return OS.objc_msgSend_bool(this.id, OS.sel_removeItemAtPath_error_, path != null ? path.id : 0, error);
}

}

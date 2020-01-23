/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSFileManager extends NSObject {

public NSFileManager() {
	super();
}

public NSFileManager(long id) {
	super(id);
}

public NSFileManager(id id) {
	super(id);
}

public static NSFileManager defaultManager() {
	long result = OS.objc_msgSend(OS.class_NSFileManager, OS.sel_defaultManager);
	return result != 0 ? new NSFileManager(result) : null;
}

public NSDirectoryEnumerator enumeratorAtPath(NSString path) {
	long result = OS.objc_msgSend(this.id, OS.sel_enumeratorAtPath_, path != null ? path.id : 0);
	return result != 0 ? new NSDirectoryEnumerator(result) : null;
}

public boolean fileExistsAtPath(NSString path) {
	return OS.objc_msgSend_bool(this.id, OS.sel_fileExistsAtPath_, path != null ? path.id : 0);
}

public boolean fileExistsAtPath(NSString path, long isDirectory) {
	return OS.objc_msgSend_bool(this.id, OS.sel_fileExistsAtPath_isDirectory_, path != null ? path.id : 0, isDirectory);
}

public boolean isExecutableFileAtPath(NSString path) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isExecutableFileAtPath_, path != null ? path.id : 0);
}

}

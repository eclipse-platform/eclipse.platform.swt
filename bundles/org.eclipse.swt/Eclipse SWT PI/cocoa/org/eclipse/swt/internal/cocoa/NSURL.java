/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

public class NSURL extends NSObject {

public NSURL() {
	super();
}

public NSURL(long id) {
	super(id);
}

public NSURL(id id) {
	super(id);
}

public static NSURL URLFromPasteboard(NSPasteboard pasteBoard) {
	long result = OS.objc_msgSend(OS.class_NSURL, OS.sel_URLFromPasteboard_, pasteBoard != null ? pasteBoard.id : 0);
	return result != 0 ? new NSURL(result) : null;
}

public static NSURL URLWithString(NSString URLString) {
	long result = OS.objc_msgSend(OS.class_NSURL, OS.sel_URLWithString_, URLString != null ? URLString.id : 0);
	return result != 0 ? new NSURL(result) : null;
}

public NSString absoluteString() {
	long result = OS.objc_msgSend(this.id, OS.sel_absoluteString);
	return result != 0 ? new NSString(result) : null;
}

public static NSURL fileURLWithPath(NSString path) {
	long result = OS.objc_msgSend(OS.class_NSURL, OS.sel_fileURLWithPath_, path != null ? path.id : 0);
	return result != 0 ? new NSURL(result) : null;
}

public NSString host() {
	long result = OS.objc_msgSend(this.id, OS.sel_host);
	return result != 0 ? new NSString(result) : null;
}

public boolean isFileURL() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isFileURL);
}

public NSString path() {
	long result = OS.objc_msgSend(this.id, OS.sel_path);
	return result != 0 ? new NSString(result) : null;
}

}

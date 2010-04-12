/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSURL extends NSObject {

public NSURL() {
	super();
}

public NSURL(int /*long*/ id) {
	super(id);
}

public NSURL(id id) {
	super(id);
}

public static NSURL URLFromPasteboard(NSPasteboard pasteBoard) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSURL, OS.sel_URLFromPasteboard_, pasteBoard != null ? pasteBoard.id : 0);
	return result != 0 ? new NSURL(result) : null;
}

public void writeToPasteboard(NSPasteboard pasteBoard) {
	OS.objc_msgSend(this.id, OS.sel_writeToPasteboard_, pasteBoard != null ? pasteBoard.id : 0);
}

public static NSURL URLWithString(NSString URLString) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSURL, OS.sel_URLWithString_, URLString != null ? URLString.id : 0);
	return result != 0 ? new NSURL(result) : null;
}

public NSString absoluteString() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_absoluteString);
	return result != 0 ? new NSString(result) : null;
}

public static NSURL fileURLWithPath(NSString path) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSURL, OS.sel_fileURLWithPath_, path != null ? path.id : 0);
	return result != 0 ? new NSURL(result) : null;
}

public NSString host() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_host);
	return result != 0 ? new NSString(result) : null;
}

public boolean isFileURL() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isFileURL);
}

public NSString path() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_path);
	return result != 0 ? new NSString(result) : null;
}

}

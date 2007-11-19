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

public class NSAppleScript extends NSObject {

public NSAppleScript() {
	super();
}

public NSAppleScript(int id) {
	super(id);
}

public boolean compileAndReturnError(int errorInfo) {
	return OS.objc_msgSend(this.id, OS.sel_compileAndReturnError_1, errorInfo) != 0;
}

public NSAppleEventDescriptor executeAndReturnError(int errorInfo) {
	int result = OS.objc_msgSend(this.id, OS.sel_executeAndReturnError_1, errorInfo);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public NSAppleEventDescriptor executeAppleEvent(NSAppleEventDescriptor event, int errorInfo) {
	int result = OS.objc_msgSend(this.id, OS.sel_executeAppleEvent_1error_1, event != null ? event.id : 0, errorInfo);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public NSAppleScript initWithContentsOfURL(NSURL url, int errorInfo) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1error_1, url != null ? url.id : 0, errorInfo);
	return result != 0 ? this : null;
}

public NSAppleScript initWithSource(NSString source) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithSource_1, source != null ? source.id : 0);
	return result != 0 ? this : null;
}

public boolean isCompiled() {
	return OS.objc_msgSend(this.id, OS.sel_isCompiled) != 0;
}

public NSString source() {
	int result = OS.objc_msgSend(this.id, OS.sel_source);
	return result != 0 ? new NSString(result) : null;
}

}

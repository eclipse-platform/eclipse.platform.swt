/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSWorkspace extends NSObject {

public NSWorkspace() {
	super();
}

public NSWorkspace(int /*long*/ id) {
	super(id);
}

public NSWorkspace(id id) {
	super(id);
}

public NSString fullPathForApplication(NSString appName) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_fullPathForApplication_, appName != null ? appName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public boolean getInfoForFile(NSString fullPath, int /*long*/ appName, int /*long*/ type) {
	return OS.objc_msgSend_bool(this.id, OS.sel_getInfoForFile_application_type_, fullPath != null ? fullPath.id : 0, appName, type);
}

public NSImage iconForFile(NSString fullPath) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_iconForFile_, fullPath != null ? fullPath.id : 0);
	return result != 0 ? new NSImage(result) : null;
}

public NSImage iconForFileType(NSString fileType) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_iconForFileType_, fileType != null ? fileType.id : 0);
	return result != 0 ? new NSImage(result) : null;
}

public boolean isFilePackageAtPath(NSString fullPath) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isFilePackageAtPath_, fullPath != null ? fullPath.id : 0);
}

public boolean openFile(NSString fullPath, NSString appName) {
	return OS.objc_msgSend_bool(this.id, OS.sel_openFile_withApplication_, fullPath != null ? fullPath.id : 0, appName != null ? appName.id : 0);
}

public boolean openURL(NSURL url) {
	return OS.objc_msgSend_bool(this.id, OS.sel_openURL_, url != null ? url.id : 0);
}

public boolean openURLs(NSArray urls, NSString bundleIdentifier, int /*long*/ options, NSAppleEventDescriptor descriptor, int /*long*/ identifiers) {
	return OS.objc_msgSend_bool(this.id, OS.sel_openURLs_withAppBundleIdentifier_options_additionalEventParamDescriptor_launchIdentifiers_, urls != null ? urls.id : 0, bundleIdentifier != null ? bundleIdentifier.id : 0, options, descriptor != null ? descriptor.id : 0, identifiers);
}

public static NSWorkspace sharedWorkspace() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSWorkspace, OS.sel_sharedWorkspace);
	return result != 0 ? new NSWorkspace(result) : null;
}

}

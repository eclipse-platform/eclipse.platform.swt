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

public class NSWorkspace extends NSObject {

public NSWorkspace() {
	super();
}

public NSWorkspace(long id) {
	super(id);
}

public NSWorkspace(id id) {
	super(id);
}

public NSString fullPathForApplication(NSString appName) {
	long result = OS.objc_msgSend(this.id, OS.sel_fullPathForApplication_, appName != null ? appName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSImage iconForFile(NSString fullPath) {
	long result = OS.objc_msgSend(this.id, OS.sel_iconForFile_, fullPath != null ? fullPath.id : 0);
	return result != 0 ? new NSImage(result) : null;
}

public boolean isFilePackageAtPath(NSString fullPath) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isFilePackageAtPath_, fullPath != null ? fullPath.id : 0);
}

public boolean openURL(NSURL url) {
	return OS.objc_msgSend_bool(this.id, OS.sel_openURL_, url != null ? url.id : 0);
}

public boolean openURLs(NSArray urls, NSString bundleIdentifier, long options, NSAppleEventDescriptor descriptor, long identifiers) {
	return OS.objc_msgSend_bool(this.id, OS.sel_openURLs_withAppBundleIdentifier_options_additionalEventParamDescriptor_launchIdentifiers_, urls != null ? urls.id : 0, bundleIdentifier != null ? bundleIdentifier.id : 0, options, descriptor != null ? descriptor.id : 0, identifiers);
}

public static NSWorkspace sharedWorkspace() {
	long result = OS.objc_msgSend(OS.class_NSWorkspace, OS.sel_sharedWorkspace);
	return result != 0 ? new NSWorkspace(result) : null;
}

public boolean type(NSString firstTypeName, NSString secondTypeName) {
	return OS.objc_msgSend_bool(this.id, OS.sel_type_conformsToType_, firstTypeName != null ? firstTypeName.id : 0, secondTypeName != null ? secondTypeName.id : 0);
}

public NSString typeOfFile(NSString absoluteFilePath, long outError) {
	long result = OS.objc_msgSend(this.id, OS.sel_typeOfFile_error_, absoluteFilePath != null ? absoluteFilePath.id : 0, outError);
	return result != 0 ? new NSString(result) : null;
}

}

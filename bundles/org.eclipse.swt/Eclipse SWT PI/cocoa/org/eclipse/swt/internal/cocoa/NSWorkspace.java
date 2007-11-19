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

public class NSWorkspace extends NSObject {

public NSWorkspace() {
	super();
}

public NSWorkspace(int id) {
	super(id);
}

public NSString absolutePathForAppBundleWithIdentifier(NSString bundleIdentifier) {
	int result = OS.objc_msgSend(this.id, OS.sel_absolutePathForAppBundleWithIdentifier_1, bundleIdentifier != null ? bundleIdentifier.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSDictionary activeApplication() {
	int result = OS.objc_msgSend(this.id, OS.sel_activeApplication);
	return result != 0 ? new NSDictionary(result) : null;
}

public void checkForRemovableMedia() {
	OS.objc_msgSend(this.id, OS.sel_checkForRemovableMedia);
}

public int extendPowerOffBy(int requested) {
	return OS.objc_msgSend(this.id, OS.sel_extendPowerOffBy_1, requested);
}

public boolean fileSystemChanged() {
	return OS.objc_msgSend(this.id, OS.sel_fileSystemChanged) != 0;
}

public boolean filenameExtension(NSString filenameExtension, NSString typeName) {
	return OS.objc_msgSend(this.id, OS.sel_filenameExtension_1isValidForType_1, filenameExtension != null ? filenameExtension.id : 0, typeName != null ? typeName.id : 0) != 0;
}

public void findApplications() {
	OS.objc_msgSend(this.id, OS.sel_findApplications);
}

public NSString fullPathForApplication(NSString appName) {
	int result = OS.objc_msgSend(this.id, OS.sel_fullPathForApplication_1, appName != null ? appName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public boolean getFileSystemInfoForPath(NSString fullPath, int removableFlag, int writableFlag, int unmountableFlag, int description, int fileSystemType) {
	return OS.objc_msgSend(this.id, OS.sel_getFileSystemInfoForPath_1isRemovable_1isWritable_1isUnmountable_1description_1type_1, fullPath != null ? fullPath.id : 0, removableFlag, writableFlag, unmountableFlag, description, fileSystemType) != 0;
}

public boolean getInfoForFile(NSString fullPath, int appName, int type) {
	return OS.objc_msgSend(this.id, OS.sel_getInfoForFile_1application_1type_1, fullPath != null ? fullPath.id : 0, appName, type) != 0;
}

public void hideOtherApplications() {
	OS.objc_msgSend(this.id, OS.sel_hideOtherApplications);
}

public NSImage iconForFile(NSString fullPath) {
	int result = OS.objc_msgSend(this.id, OS.sel_iconForFile_1, fullPath != null ? fullPath.id : 0);
	return result != 0 ? new NSImage(result) : null;
}

public NSImage iconForFileType(NSString fileType) {
	int result = OS.objc_msgSend(this.id, OS.sel_iconForFileType_1, fileType != null ? fileType.id : 0);
	return result != 0 ? new NSImage(result) : null;
}

public NSImage iconForFiles(NSArray fullPaths) {
	int result = OS.objc_msgSend(this.id, OS.sel_iconForFiles_1, fullPaths != null ? fullPaths.id : 0);
	return result != 0 ? new NSImage(result) : null;
}

public boolean isFilePackageAtPath(NSString fullPath) {
	return OS.objc_msgSend(this.id, OS.sel_isFilePackageAtPath_1, fullPath != null ? fullPath.id : 0) != 0;
}

public boolean launchAppWithBundleIdentifier(NSString bundleIdentifier, int options, NSAppleEventDescriptor descriptor, int identifier) {
	return OS.objc_msgSend(this.id, OS.sel_launchAppWithBundleIdentifier_1options_1additionalEventParamDescriptor_1launchIdentifier_1, bundleIdentifier != null ? bundleIdentifier.id : 0, options, descriptor != null ? descriptor.id : 0, identifier) != 0;
}

public boolean launchApplication_(NSString appName) {
	return OS.objc_msgSend(this.id, OS.sel_launchApplication_1, appName != null ? appName.id : 0) != 0;
}

public boolean launchApplication_showIcon_autolaunch_(NSString appName, boolean showIcon, boolean autolaunch) {
	return OS.objc_msgSend(this.id, OS.sel_launchApplication_1showIcon_1autolaunch_1, appName != null ? appName.id : 0, showIcon, autolaunch) != 0;
}

public NSArray launchedApplications() {
	int result = OS.objc_msgSend(this.id, OS.sel_launchedApplications);
	return result != 0 ? new NSArray(result) : null;
}

public NSString localizedDescriptionForType(NSString typeName) {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedDescriptionForType_1, typeName != null ? typeName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSArray mountNewRemovableMedia() {
	int result = OS.objc_msgSend(this.id, OS.sel_mountNewRemovableMedia);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray mountedLocalVolumePaths() {
	int result = OS.objc_msgSend(this.id, OS.sel_mountedLocalVolumePaths);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray mountedRemovableMedia() {
	int result = OS.objc_msgSend(this.id, OS.sel_mountedRemovableMedia);
	return result != 0 ? new NSArray(result) : null;
}

public void noteFileSystemChanged() {
	OS.objc_msgSend(this.id, OS.sel_noteFileSystemChanged);
}

public void noteFileSystemChanged_(NSString path) {
	OS.objc_msgSend(this.id, OS.sel_noteFileSystemChanged_1, path != null ? path.id : 0);
}

public void noteUserDefaultsChanged() {
	OS.objc_msgSend(this.id, OS.sel_noteUserDefaultsChanged);
}

public NSNotificationCenter notificationCenter() {
	int result = OS.objc_msgSend(this.id, OS.sel_notificationCenter);
	return result != 0 ? new NSNotificationCenter(result) : null;
}

public boolean openFile_(NSString fullPath) {
	return OS.objc_msgSend(this.id, OS.sel_openFile_1, fullPath != null ? fullPath.id : 0) != 0;
}

public boolean openFile_fromImage_at_inView_(NSString fullPath, NSImage anImage, NSPoint point, NSView aView) {
	return OS.objc_msgSend(this.id, OS.sel_openFile_1fromImage_1at_1inView_1, fullPath != null ? fullPath.id : 0, anImage != null ? anImage.id : 0, point, aView != null ? aView.id : 0) != 0;
}

public boolean openFile_withApplication_(NSString fullPath, NSString appName) {
	return OS.objc_msgSend(this.id, OS.sel_openFile_1withApplication_1, fullPath != null ? fullPath.id : 0, appName != null ? appName.id : 0) != 0;
}

public boolean openFile_withApplication_andDeactivate_(NSString fullPath, NSString appName, boolean flag) {
	return OS.objc_msgSend(this.id, OS.sel_openFile_1withApplication_1andDeactivate_1, fullPath != null ? fullPath.id : 0, appName != null ? appName.id : 0, flag) != 0;
}

public boolean openTempFile(NSString fullPath) {
	return OS.objc_msgSend(this.id, OS.sel_openTempFile_1, fullPath != null ? fullPath.id : 0) != 0;
}

public boolean openURL(NSURL url) {
	return OS.objc_msgSend(this.id, OS.sel_openURL_1, url != null ? url.id : 0) != 0;
}

public boolean openURLs(NSArray urls, NSString bundleIdentifier, int options, NSAppleEventDescriptor descriptor, int identifiers) {
	return OS.objc_msgSend(this.id, OS.sel_openURLs_1withAppBundleIdentifier_1options_1additionalEventParamDescriptor_1launchIdentifiers_1, urls != null ? urls.id : 0, bundleIdentifier != null ? bundleIdentifier.id : 0, options, descriptor != null ? descriptor.id : 0, identifiers) != 0;
}

public boolean performFileOperation(NSString operation, NSString source, NSString destination, NSArray files, int tag) {
	return OS.objc_msgSend(this.id, OS.sel_performFileOperation_1source_1destination_1files_1tag_1, operation != null ? operation.id : 0, source != null ? source.id : 0, destination != null ? destination.id : 0, files != null ? files.id : 0, tag) != 0;
}

public NSString preferredFilenameExtensionForType(NSString typeName) {
	int result = OS.objc_msgSend(this.id, OS.sel_preferredFilenameExtensionForType_1, typeName != null ? typeName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public boolean selectFile(NSString fullPath, NSString rootFullpath) {
	return OS.objc_msgSend(this.id, OS.sel_selectFile_1inFileViewerRootedAtPath_1, fullPath != null ? fullPath.id : 0, rootFullpath != null ? rootFullpath.id : 0) != 0;
}

public boolean setIcon(NSImage image, NSString fullPath, int options) {
	return OS.objc_msgSend(this.id, OS.sel_setIcon_1forFile_1options_1, image != null ? image.id : 0, fullPath != null ? fullPath.id : 0, options) != 0;
}

public static NSWorkspace sharedWorkspace() {
	int result = OS.objc_msgSend(OS.class_NSWorkspace, OS.sel_sharedWorkspace);
	return result != 0 ? new NSWorkspace(result) : null;
}

public void slideImage(NSImage image, NSPoint fromPoint, NSPoint toPoint) {
	OS.objc_msgSend(this.id, OS.sel_slideImage_1from_1to_1, image != null ? image.id : 0, fromPoint, toPoint);
}

public boolean type(NSString firstTypeName, NSString secondTypeName) {
	return OS.objc_msgSend(this.id, OS.sel_type_1conformsToType_1, firstTypeName != null ? firstTypeName.id : 0, secondTypeName != null ? secondTypeName.id : 0) != 0;
}

public NSString typeOfFile(NSString absoluteFilePath, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_typeOfFile_1error_1, absoluteFilePath != null ? absoluteFilePath.id : 0, outError);
	return result != 0 ? new NSString(result) : null;
}

public boolean unmountAndEjectDeviceAtPath(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_unmountAndEjectDeviceAtPath_1, path != null ? path.id : 0) != 0;
}

public boolean userDefaultsChanged() {
	return OS.objc_msgSend(this.id, OS.sel_userDefaultsChanged) != 0;
}

}

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

public class NSFileManager extends NSObject {

public NSFileManager() {
	super();
}

public NSFileManager(int id) {
	super(id);
}

public NSDictionary attributesOfFileSystemForPath(NSString path, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_attributesOfFileSystemForPath_1error_1, path != null ? path.id : 0, error);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary attributesOfItemAtPath(NSString path, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_attributesOfItemAtPath_1error_1, path != null ? path.id : 0, error);
	return result != 0 ? new NSDictionary(result) : null;
}

public boolean changeCurrentDirectoryPath(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_changeCurrentDirectoryPath_1, path != null ? path.id : 0) != 0;
}

public boolean changeFileAttributes(NSDictionary attributes, NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_changeFileAttributes_1atPath_1, attributes != null ? attributes.id : 0, path != null ? path.id : 0) != 0;
}

public NSArray componentsToDisplayForPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_componentsToDisplayForPath_1, path != null ? path.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSData contentsAtPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_contentsAtPath_1, path != null ? path.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public boolean contentsEqualAtPath(NSString path1, NSString path2) {
	return OS.objc_msgSend(this.id, OS.sel_contentsEqualAtPath_1andPath_1, path1 != null ? path1.id : 0, path2 != null ? path2.id : 0) != 0;
}

public NSArray contentsOfDirectoryAtPath(NSString path, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_contentsOfDirectoryAtPath_1error_1, path != null ? path.id : 0, error);
	return result != 0 ? new NSArray(result) : null;
}

public boolean copyItemAtPath(NSString srcPath, NSString dstPath, int error) {
	return OS.objc_msgSend(this.id, OS.sel_copyItemAtPath_1toPath_1error_1, srcPath != null ? srcPath.id : 0, dstPath != null ? dstPath.id : 0, error) != 0;
}

public boolean copyPath(NSString src, NSString dest, id handler) {
	return OS.objc_msgSend(this.id, OS.sel_copyPath_1toPath_1handler_1, src != null ? src.id : 0, dest != null ? dest.id : 0, handler != null ? handler.id : 0) != 0;
}

public boolean createDirectoryAtPath_attributes_(NSString path, NSDictionary attributes) {
	return OS.objc_msgSend(this.id, OS.sel_createDirectoryAtPath_1attributes_1, path != null ? path.id : 0, attributes != null ? attributes.id : 0) != 0;
}

public boolean createDirectoryAtPath_withIntermediateDirectories_attributes_error_(NSString path, boolean createIntermediates, NSDictionary attributes, int error) {
	return OS.objc_msgSend(this.id, OS.sel_createDirectoryAtPath_1withIntermediateDirectories_1attributes_1error_1, path != null ? path.id : 0, createIntermediates, attributes != null ? attributes.id : 0, error) != 0;
}

public boolean createFileAtPath(NSString path, NSData data, NSDictionary attr) {
	return OS.objc_msgSend(this.id, OS.sel_createFileAtPath_1contents_1attributes_1, path != null ? path.id : 0, data != null ? data.id : 0, attr != null ? attr.id : 0) != 0;
}

public boolean createSymbolicLinkAtPath_pathContent_(NSString path, NSString otherpath) {
	return OS.objc_msgSend(this.id, OS.sel_createSymbolicLinkAtPath_1pathContent_1, path != null ? path.id : 0, otherpath != null ? otherpath.id : 0) != 0;
}

public boolean createSymbolicLinkAtPath_withDestinationPath_error_(NSString path, NSString destPath, int error) {
	return OS.objc_msgSend(this.id, OS.sel_createSymbolicLinkAtPath_1withDestinationPath_1error_1, path != null ? path.id : 0, destPath != null ? destPath.id : 0, error) != 0;
}

public NSString currentDirectoryPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentDirectoryPath);
	return result != 0 ? new NSString(result) : null;
}

public static NSFileManager defaultManager() {
	int result = OS.objc_msgSend(OS.class_NSFileManager, OS.sel_defaultManager);
	return result != 0 ? new NSFileManager(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public NSString destinationOfSymbolicLinkAtPath(NSString path, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_destinationOfSymbolicLinkAtPath_1error_1, path != null ? path.id : 0, error);
	return result != 0 ? new NSString(result) : null;
}

public NSArray directoryContentsAtPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_directoryContentsAtPath_1, path != null ? path.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSString displayNameAtPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_displayNameAtPath_1, path != null ? path.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSDirectoryEnumerator enumeratorAtPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_enumeratorAtPath_1, path != null ? path.id : 0);
	return result != 0 ? new NSDirectoryEnumerator(result) : null;
}

public NSDictionary fileAttributesAtPath(NSString path, boolean yorn) {
	int result = OS.objc_msgSend(this.id, OS.sel_fileAttributesAtPath_1traverseLink_1, path != null ? path.id : 0, yorn);
	return result != 0 ? new NSDictionary(result) : null;
}

public boolean fileExistsAtPath_(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_fileExistsAtPath_1, path != null ? path.id : 0) != 0;
}

public boolean fileExistsAtPath_isDirectory_(NSString path, int isDirectory) {
	return OS.objc_msgSend(this.id, OS.sel_fileExistsAtPath_1isDirectory_1, path != null ? path.id : 0, isDirectory) != 0;
}

public NSDictionary fileSystemAttributesAtPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_fileSystemAttributesAtPath_1, path != null ? path.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public int fileSystemRepresentationWithPath(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_fileSystemRepresentationWithPath_1, path != null ? path.id : 0);
}

public boolean isDeletableFileAtPath(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_isDeletableFileAtPath_1, path != null ? path.id : 0) != 0;
}

public boolean isExecutableFileAtPath(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_isExecutableFileAtPath_1, path != null ? path.id : 0) != 0;
}

public boolean isReadableFileAtPath(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_isReadableFileAtPath_1, path != null ? path.id : 0) != 0;
}

public boolean isWritableFileAtPath(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_isWritableFileAtPath_1, path != null ? path.id : 0) != 0;
}

public boolean linkItemAtPath(NSString srcPath, NSString dstPath, int error) {
	return OS.objc_msgSend(this.id, OS.sel_linkItemAtPath_1toPath_1error_1, srcPath != null ? srcPath.id : 0, dstPath != null ? dstPath.id : 0, error) != 0;
}

public boolean linkPath(NSString src, NSString dest, id handler) {
	return OS.objc_msgSend(this.id, OS.sel_linkPath_1toPath_1handler_1, src != null ? src.id : 0, dest != null ? dest.id : 0, handler != null ? handler.id : 0) != 0;
}

public boolean moveItemAtPath(NSString srcPath, NSString dstPath, int error) {
	return OS.objc_msgSend(this.id, OS.sel_moveItemAtPath_1toPath_1error_1, srcPath != null ? srcPath.id : 0, dstPath != null ? dstPath.id : 0, error) != 0;
}

public boolean movePath(NSString src, NSString dest, id handler) {
	return OS.objc_msgSend(this.id, OS.sel_movePath_1toPath_1handler_1, src != null ? src.id : 0, dest != null ? dest.id : 0, handler != null ? handler.id : 0) != 0;
}

public NSString pathContentOfSymbolicLinkAtPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_pathContentOfSymbolicLinkAtPath_1, path != null ? path.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public boolean removeFileAtPath(NSString path, id handler) {
	return OS.objc_msgSend(this.id, OS.sel_removeFileAtPath_1handler_1, path != null ? path.id : 0, handler != null ? handler.id : 0) != 0;
}

public boolean removeItemAtPath(NSString path, int error) {
	return OS.objc_msgSend(this.id, OS.sel_removeItemAtPath_1error_1, path != null ? path.id : 0, error) != 0;
}

public boolean setAttributes(NSDictionary attributes, NSString path, int error) {
	return OS.objc_msgSend(this.id, OS.sel_setAttributes_1ofItemAtPath_1error_1, attributes != null ? attributes.id : 0, path != null ? path.id : 0, error) != 0;
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public NSString stringWithFileSystemRepresentation(int str, int len) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringWithFileSystemRepresentation_1length_1, str, len);
	return result != 0 ? new NSString(result) : null;
}

public NSArray subpathsAtPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_subpathsAtPath_1, path != null ? path.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray subpathsOfDirectoryAtPath(NSString path, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_subpathsOfDirectoryAtPath_1error_1, path != null ? path.id : 0, error);
	return result != 0 ? new NSArray(result) : null;
}

}

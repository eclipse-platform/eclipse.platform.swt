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

public class NSFileWrapper extends NSObject {

public NSFileWrapper() {
	super();
}

public NSFileWrapper(int id) {
	super(id);
}

public NSString addFileWithPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_addFileWithPath_1, path != null ? path.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString addFileWrapper(NSFileWrapper doc) {
	int result = OS.objc_msgSend(this.id, OS.sel_addFileWrapper_1, doc != null ? doc.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString addRegularFileWithContents(NSData data, NSString filename) {
	int result = OS.objc_msgSend(this.id, OS.sel_addRegularFileWithContents_1preferredFilename_1, data != null ? data.id : 0, filename != null ? filename.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString addSymbolicLinkWithDestination(NSString path, NSString filename) {
	int result = OS.objc_msgSend(this.id, OS.sel_addSymbolicLinkWithDestination_1preferredFilename_1, path != null ? path.id : 0, filename != null ? filename.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSDictionary fileAttributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary fileWrappers() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileWrappers);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSString filename() {
	int result = OS.objc_msgSend(this.id, OS.sel_filename);
	return result != 0 ? new NSString(result) : null;
}

public NSImage icon() {
	int result = OS.objc_msgSend(this.id, OS.sel_icon);
	return result != 0 ? new NSImage(result) : null;
}

public id initDirectoryWithFileWrappers(NSDictionary docs) {
	int result = OS.objc_msgSend(this.id, OS.sel_initDirectoryWithFileWrappers_1, docs != null ? docs.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initRegularFileWithContents(NSData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initRegularFileWithContents_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initSymbolicLinkWithDestination(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initSymbolicLinkWithDestination_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithPath_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithSerializedRepresentation(NSData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithSerializedRepresentation_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isDirectory() {
	return OS.objc_msgSend(this.id, OS.sel_isDirectory) != 0;
}

public boolean isRegularFile() {
	return OS.objc_msgSend(this.id, OS.sel_isRegularFile) != 0;
}

public boolean isSymbolicLink() {
	return OS.objc_msgSend(this.id, OS.sel_isSymbolicLink) != 0;
}

public NSString keyForFileWrapper(NSFileWrapper doc) {
	int result = OS.objc_msgSend(this.id, OS.sel_keyForFileWrapper_1, doc != null ? doc.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public boolean needsToBeUpdatedFromPath(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_needsToBeUpdatedFromPath_1, path != null ? path.id : 0) != 0;
}

public NSString preferredFilename() {
	int result = OS.objc_msgSend(this.id, OS.sel_preferredFilename);
	return result != 0 ? new NSString(result) : null;
}

public NSData regularFileContents() {
	int result = OS.objc_msgSend(this.id, OS.sel_regularFileContents);
	return result != 0 ? new NSData(result) : null;
}

public void removeFileWrapper(NSFileWrapper doc) {
	OS.objc_msgSend(this.id, OS.sel_removeFileWrapper_1, doc != null ? doc.id : 0);
}

public NSData serializedRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_serializedRepresentation);
	return result != 0 ? new NSData(result) : null;
}

public void setFileAttributes(NSDictionary attributes) {
	OS.objc_msgSend(this.id, OS.sel_setFileAttributes_1, attributes != null ? attributes.id : 0);
}

public void setFilename(NSString filename) {
	OS.objc_msgSend(this.id, OS.sel_setFilename_1, filename != null ? filename.id : 0);
}

public void setIcon(NSImage icon) {
	OS.objc_msgSend(this.id, OS.sel_setIcon_1, icon != null ? icon.id : 0);
}

public void setPreferredFilename(NSString filename) {
	OS.objc_msgSend(this.id, OS.sel_setPreferredFilename_1, filename != null ? filename.id : 0);
}

public NSString symbolicLinkDestination() {
	int result = OS.objc_msgSend(this.id, OS.sel_symbolicLinkDestination);
	return result != 0 ? new NSString(result) : null;
}

public boolean updateFromPath(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_updateFromPath_1, path != null ? path.id : 0) != 0;
}

public boolean writeToFile(NSString path, boolean atomicFlag, boolean updateFilenamesFlag) {
	return OS.objc_msgSend(this.id, OS.sel_writeToFile_1atomically_1updateFilenames_1, path != null ? path.id : 0, atomicFlag, updateFilenamesFlag) != 0;
}

}

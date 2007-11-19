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

public class NSDictionary extends NSObject {

public NSDictionary() {
	super();
}

public NSDictionary(int id) {
	super(id);
}

public NSArray allKeys() {
	int result = OS.objc_msgSend(this.id, OS.sel_allKeys);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray allKeysForObject(id anObject) {
	int result = OS.objc_msgSend(this.id, OS.sel_allKeysForObject_1, anObject != null ? anObject.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray allValues() {
	int result = OS.objc_msgSend(this.id, OS.sel_allValues);
	return result != 0 ? new NSArray(result) : null;
}

public int count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public NSString description() {
	int result = OS.objc_msgSend(this.id, OS.sel_description);
	return result != 0 ? new NSString(result) : null;
}

public NSString descriptionInStringsFileFormat() {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionInStringsFileFormat);
	return result != 0 ? new NSString(result) : null;
}

public NSString descriptionWithLocale_(id locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionWithLocale_1, locale != null ? locale.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString descriptionWithLocale_indent_(id locale, int level) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionWithLocale_1indent_1, locale != null ? locale.id : 0, level);
	return result != 0 ? new NSString(result) : null;
}

public static id dictionary() {
	int result = OS.objc_msgSend(OS.class_NSDictionary, OS.sel_dictionary);
	return result != 0 ? new id(result) : null;
}

public static id dictionaryWithContentsOfFile(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSDictionary, OS.sel_dictionaryWithContentsOfFile_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id dictionaryWithContentsOfURL(NSURL url) {
	int result = OS.objc_msgSend(OS.class_NSDictionary, OS.sel_dictionaryWithContentsOfURL_1, url != null ? url.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id dictionaryWithDictionary(NSDictionary dict) {
	int result = OS.objc_msgSend(OS.class_NSDictionary, OS.sel_dictionaryWithDictionary_1, dict != null ? dict.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id dictionaryWithObject(id object, id key) {
	int result = OS.objc_msgSend(OS.class_NSDictionary, OS.sel_dictionaryWithObject_1forKey_1, object != null ? object.id : 0, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_dictionaryWithObjects_forKeys_(NSArray objects, NSArray keys) {
	int result = OS.objc_msgSend(OS.class_NSDictionary, OS.sel_dictionaryWithObjects_1forKeys_1, objects != null ? objects.id : 0, keys != null ? keys.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_dictionaryWithObjects_forKeys_count_(int objects, int keys, int cnt) {
	int result = OS.objc_msgSend(OS.class_NSDictionary, OS.sel_dictionaryWithObjects_1forKeys_1count_1, objects, keys, cnt);
	return result != 0 ? new id(result) : null;
}

public static id dictionaryWithObjectsAndKeys(id dictionaryWithObjectsAndKeys) {
	int result = OS.objc_msgSend(OS.class_NSDictionary, OS.sel_dictionaryWithObjectsAndKeys_1, dictionaryWithObjectsAndKeys != null ? dictionaryWithObjectsAndKeys.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSDate fileCreationDate() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileCreationDate);
	return result != 0 ? new NSDate(result) : null;
}

public boolean fileExtensionHidden() {
	return OS.objc_msgSend(this.id, OS.sel_fileExtensionHidden) != 0;
}

public NSNumber fileGroupOwnerAccountID() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileGroupOwnerAccountID);
	return result != 0 ? new NSNumber(result) : null;
}

public NSString fileGroupOwnerAccountName() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileGroupOwnerAccountName);
	return result != 0 ? new NSString(result) : null;
}

public int fileHFSCreatorCode() {
	return OS.objc_msgSend(this.id, OS.sel_fileHFSCreatorCode);
}

public int fileHFSTypeCode() {
	return OS.objc_msgSend(this.id, OS.sel_fileHFSTypeCode);
}

public boolean fileIsAppendOnly() {
	return OS.objc_msgSend(this.id, OS.sel_fileIsAppendOnly) != 0;
}

public boolean fileIsImmutable() {
	return OS.objc_msgSend(this.id, OS.sel_fileIsImmutable) != 0;
}

public NSDate fileModificationDate() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileModificationDate);
	return result != 0 ? new NSDate(result) : null;
}

public NSNumber fileOwnerAccountID() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileOwnerAccountID);
	return result != 0 ? new NSNumber(result) : null;
}

public NSString fileOwnerAccountName() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileOwnerAccountName);
	return result != 0 ? new NSString(result) : null;
}

public int filePosixPermissions() {
	return OS.objc_msgSend(this.id, OS.sel_filePosixPermissions);
}

public long fileSize() {
	return (long)OS.objc_msgSend(this.id, OS.sel_fileSize);
}

public int fileSystemFileNumber() {
	return OS.objc_msgSend(this.id, OS.sel_fileSystemFileNumber);
}

public int fileSystemNumber() {
	return OS.objc_msgSend(this.id, OS.sel_fileSystemNumber);
}

public NSString fileType() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileType);
	return result != 0 ? new NSString(result) : null;
}

public void getObjects(int objects, int keys) {
	OS.objc_msgSend(this.id, OS.sel_getObjects_1andKeys_1, objects, keys);
}

public id initWithContentsOfFile(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfFile_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithContentsOfURL(NSURL url) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1, url != null ? url.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithDictionary_(NSDictionary otherDictionary) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDictionary_1, otherDictionary != null ? otherDictionary.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithDictionary_copyItems_(NSDictionary otherDictionary, boolean flag) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDictionary_1copyItems_1, otherDictionary != null ? otherDictionary.id : 0, flag);
	return result != 0 ? new id(result) : null;
}

public id initWithObjects_forKeys_(NSArray objects, NSArray keys) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithObjects_1forKeys_1, objects != null ? objects.id : 0, keys != null ? keys.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithObjects_forKeys_count_(int objects, int keys, int cnt) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithObjects_1forKeys_1count_1, objects, keys, cnt);
	return result != 0 ? new id(result) : null;
}

public id initWithObjectsAndKeys(id initWithObjectsAndKeys) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithObjectsAndKeys_1, initWithObjectsAndKeys != null ? initWithObjectsAndKeys.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isEqualToDictionary(NSDictionary otherDictionary) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToDictionary_1, otherDictionary != null ? otherDictionary.id : 0) != 0;
}

public NSEnumerator keyEnumerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_keyEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public NSArray keysSortedByValueUsingSelector(int comparator) {
	int result = OS.objc_msgSend(this.id, OS.sel_keysSortedByValueUsingSelector_1, comparator);
	return result != 0 ? new NSArray(result) : null;
}

public NSEnumerator objectEnumerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public id objectForKey(id aKey) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectForKey_1, aKey != null ? aKey.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSArray objectsForKeys(NSArray keys, id marker) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectsForKeys_1notFoundMarker_1, keys != null ? keys.id : 0, marker != null ? marker.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public id valueForKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueForKey_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean writeToFile(NSString path, boolean useAuxiliaryFile) {
	return OS.objc_msgSend(this.id, OS.sel_writeToFile_1atomically_1, path != null ? path.id : 0, useAuxiliaryFile) != 0;
}

public boolean writeToURL(NSURL url, boolean atomically) {
	return OS.objc_msgSend(this.id, OS.sel_writeToURL_1atomically_1, url != null ? url.id : 0, atomically) != 0;
}

}

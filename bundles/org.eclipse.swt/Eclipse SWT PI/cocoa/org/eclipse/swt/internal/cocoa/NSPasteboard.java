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

public class NSPasteboard extends NSObject {

public NSPasteboard() {
	super();
}

public NSPasteboard(int id) {
	super(id);
}

public int addTypes(NSArray newTypes, id newOwner) {
	return OS.objc_msgSend(this.id, OS.sel_addTypes_1owner_1, newTypes != null ? newTypes.id : 0, newOwner != null ? newOwner.id : 0);
}

public NSString availableTypeFromArray(NSArray types) {
	int result = OS.objc_msgSend(this.id, OS.sel_availableTypeFromArray_1, types != null ? types.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public int changeCount() {
	return OS.objc_msgSend(this.id, OS.sel_changeCount);
}

public NSData dataForType(NSString dataType) {
	int result = OS.objc_msgSend(this.id, OS.sel_dataForType_1, dataType != null ? dataType.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public int declareTypes(NSArray newTypes, id newOwner) {
	return OS.objc_msgSend(this.id, OS.sel_declareTypes_1owner_1, newTypes != null ? newTypes.id : 0, newOwner != null ? newOwner.id : 0);
}

public static NSPasteboard generalPasteboard() {
	int result = OS.objc_msgSend(OS.class_NSPasteboard, OS.sel_generalPasteboard);
	return result != 0 ? new NSPasteboard(result) : null;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public static NSPasteboard pasteboardByFilteringData(NSData data, NSString type) {
	int result = OS.objc_msgSend(OS.class_NSPasteboard, OS.sel_pasteboardByFilteringData_1ofType_1, data != null ? data.id : 0, type != null ? type.id : 0);
	return result != 0 ? new NSPasteboard(result) : null;
}

public static NSPasteboard pasteboardByFilteringFile(NSString filename) {
	int result = OS.objc_msgSend(OS.class_NSPasteboard, OS.sel_pasteboardByFilteringFile_1, filename != null ? filename.id : 0);
	return result != 0 ? new NSPasteboard(result) : null;
}

public static NSPasteboard pasteboardByFilteringTypesInPasteboard(NSPasteboard pboard) {
	int result = OS.objc_msgSend(OS.class_NSPasteboard, OS.sel_pasteboardByFilteringTypesInPasteboard_1, pboard != null ? pboard.id : 0);
	return result != 0 ? new NSPasteboard(result) : null;
}

public static NSPasteboard pasteboardWithName(NSString name) {
	int result = OS.objc_msgSend(OS.class_NSPasteboard, OS.sel_pasteboardWithName_1, name != null ? name.id : 0);
	return result != 0 ? new NSPasteboard(result) : null;
}

public static NSPasteboard pasteboardWithUniqueName() {
	int result = OS.objc_msgSend(OS.class_NSPasteboard, OS.sel_pasteboardWithUniqueName);
	return result != 0 ? new NSPasteboard(result) : null;
}

public id propertyListForType(NSString dataType) {
	int result = OS.objc_msgSend(this.id, OS.sel_propertyListForType_1, dataType != null ? dataType.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString readFileContentsType(NSString type, NSString filename) {
	int result = OS.objc_msgSend(this.id, OS.sel_readFileContentsType_1toFile_1, type != null ? type.id : 0, filename != null ? filename.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSFileWrapper readFileWrapper() {
	int result = OS.objc_msgSend(this.id, OS.sel_readFileWrapper);
	return result != 0 ? new NSFileWrapper(result) : null;
}

public void releaseGlobally() {
	OS.objc_msgSend(this.id, OS.sel_releaseGlobally);
}

public boolean setData(NSData data, NSString dataType) {
	return OS.objc_msgSend(this.id, OS.sel_setData_1forType_1, data != null ? data.id : 0, dataType != null ? dataType.id : 0) != 0;
}

public boolean setPropertyList(id plist, NSString dataType) {
	return OS.objc_msgSend(this.id, OS.sel_setPropertyList_1forType_1, plist != null ? plist.id : 0, dataType != null ? dataType.id : 0) != 0;
}

public boolean setString(NSString string, NSString dataType) {
	return OS.objc_msgSend(this.id, OS.sel_setString_1forType_1, string != null ? string.id : 0, dataType != null ? dataType.id : 0) != 0;
}

public NSString stringForType(NSString dataType) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringForType_1, dataType != null ? dataType.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSArray types() {
	int result = OS.objc_msgSend(this.id, OS.sel_types);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray typesFilterableTo(NSString type) {
	int result = OS.objc_msgSend(OS.class_NSPasteboard, OS.sel_typesFilterableTo_1, type != null ? type.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public boolean writeFileContents(NSString filename) {
	return OS.objc_msgSend(this.id, OS.sel_writeFileContents_1, filename != null ? filename.id : 0) != 0;
}

public boolean writeFileWrapper(NSFileWrapper wrapper) {
	return OS.objc_msgSend(this.id, OS.sel_writeFileWrapper_1, wrapper != null ? wrapper.id : 0) != 0;
}

}

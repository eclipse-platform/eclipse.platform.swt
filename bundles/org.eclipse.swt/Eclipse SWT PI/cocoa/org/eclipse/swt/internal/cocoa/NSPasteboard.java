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

public class NSPasteboard extends NSObject {

public NSPasteboard() {
	super();
}

public NSPasteboard(int /*long*/ id) {
	super(id);
}

public NSPasteboard(id id) {
	super(id);
}

public int /*long*/ addTypes(NSArray newTypes, id newOwner) {
	return OS.objc_msgSend(this.id, OS.sel_addTypes_owner_, newTypes != null ? newTypes.id : 0, newOwner != null ? newOwner.id : 0);
}

public NSString availableTypeFromArray(NSArray types) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_availableTypeFromArray_, types != null ? types.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSData dataForType(NSString dataType) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_dataForType_, dataType != null ? dataType.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public int /*long*/ declareTypes(NSArray newTypes, id newOwner) {
	return OS.objc_msgSend(this.id, OS.sel_declareTypes_owner_, newTypes != null ? newTypes.id : 0, newOwner != null ? newOwner.id : 0);
}

public static NSPasteboard generalPasteboard() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSPasteboard, OS.sel_generalPasteboard);
	return result != 0 ? new NSPasteboard(result) : null;
}

public static NSPasteboard pasteboardWithName(NSString name) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSPasteboard, OS.sel_pasteboardWithName_, name != null ? name.id : 0);
	return result != 0 ? new NSPasteboard(result) : null;
}

public id propertyListForType(NSString dataType) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_propertyListForType_, dataType != null ? dataType.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean setData(NSData data, NSString dataType) {
	return OS.objc_msgSend_bool(this.id, OS.sel_setData_forType_, data != null ? data.id : 0, dataType != null ? dataType.id : 0);
}

public boolean setPropertyList(id plist, NSString dataType) {
	return OS.objc_msgSend_bool(this.id, OS.sel_setPropertyList_forType_, plist != null ? plist.id : 0, dataType != null ? dataType.id : 0);
}

public boolean setString(NSString string, NSString dataType) {
	return OS.objc_msgSend_bool(this.id, OS.sel_setString_forType_, string != null ? string.id : 0, dataType != null ? dataType.id : 0);
}

public NSString stringForType(NSString dataType) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringForType_, dataType != null ? dataType.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSArray types() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_types);
	return result != 0 ? new NSArray(result) : null;
}

}

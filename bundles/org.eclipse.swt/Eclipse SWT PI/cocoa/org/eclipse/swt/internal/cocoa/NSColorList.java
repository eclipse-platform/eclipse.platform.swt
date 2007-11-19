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

public class NSColorList extends NSObject {

public NSColorList() {
	super();
}

public NSColorList(int id) {
	super(id);
}

public NSArray allKeys() {
	int result = OS.objc_msgSend(this.id, OS.sel_allKeys);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray availableColorLists() {
	int result = OS.objc_msgSend(OS.class_NSColorList, OS.sel_availableColorLists);
	return result != 0 ? new NSArray(result) : null;
}

public static NSColorList colorListNamed(NSString name) {
	int result = OS.objc_msgSend(OS.class_NSColorList, OS.sel_colorListNamed_1, name != null ? name.id : 0);
	return result != 0 ? new NSColorList(result) : null;
}

public NSColor colorWithKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_colorWithKey_1, key != null ? key.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public NSColorList initWithName_(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithName_1, name != null ? name.id : 0);
	return result != 0 ? this : null;
}

public NSColorList initWithName_fromFile_(NSString name, NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithName_1fromFile_1, name != null ? name.id : 0, path != null ? path.id : 0);
	return result != 0 ? this : null;
}

public void insertColor(NSColor color, NSString key, int loc) {
	OS.objc_msgSend(this.id, OS.sel_insertColor_1key_1atIndex_1, color != null ? color.id : 0, key != null ? key.id : 0, loc);
}

public boolean isEditable() {
	return OS.objc_msgSend(this.id, OS.sel_isEditable) != 0;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public void removeColorWithKey(NSString key) {
	OS.objc_msgSend(this.id, OS.sel_removeColorWithKey_1, key != null ? key.id : 0);
}

public void removeFile() {
	OS.objc_msgSend(this.id, OS.sel_removeFile);
}

public void setColor(NSColor color, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_setColor_1forKey_1, color != null ? color.id : 0, key != null ? key.id : 0);
}

public boolean writeToFile(NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_writeToFile_1, path != null ? path.id : 0) != 0;
}

}

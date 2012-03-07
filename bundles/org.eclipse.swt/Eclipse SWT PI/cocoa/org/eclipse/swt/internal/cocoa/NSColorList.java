/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSColorList extends NSObject {

public NSColorList() {
	super();
}

public NSColorList(int /*long*/ id) {
	super(id);
}

public NSColorList(id id) {
	super(id);
}

public NSArray allKeys() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_allKeys);
	return result != 0 ? new NSArray(result) : null;
}

public static NSColorList colorListNamed(NSString name) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColorList, OS.sel_colorListNamed_, name != null ? name.id : 0);
	return result != 0 ? new NSColorList(result) : null;
}

public NSColor colorWithKey(NSString key) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_colorWithKey_, key != null ? key.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public id initWithName(NSString name) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithName_, name != null ? name.id : 0);
	return result != 0 ? new id(result) : null;
}

public void insertColor(NSColor color, NSString key, int /*long*/ loc) {
	OS.objc_msgSend(this.id, OS.sel_insertColor_key_atIndex_, color != null ? color.id : 0, key != null ? key.id : 0, loc);
}

public void removeColorWithKey(NSString key) {
	OS.objc_msgSend(this.id, OS.sel_removeColorWithKey_, key != null ? key.id : 0);
}

}

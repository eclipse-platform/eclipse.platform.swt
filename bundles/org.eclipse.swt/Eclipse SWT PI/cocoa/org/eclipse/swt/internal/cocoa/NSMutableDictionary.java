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

public class NSMutableDictionary extends NSDictionary {

public NSMutableDictionary() {
	super();
}

public NSMutableDictionary(int id) {
	super(id);
}

public void addEntriesFromDictionary(NSDictionary otherDictionary) {
	OS.objc_msgSend(this.id, OS.sel_addEntriesFromDictionary_1, otherDictionary != null ? otherDictionary.id : 0);
}

public static NSMutableDictionary dictionaryWithCapacity(int numItems) {
	int result = OS.objc_msgSend(OS.class_NSMutableDictionary, OS.sel_dictionaryWithCapacity_1, numItems);
	return result != 0 ? new NSMutableDictionary(result) : null;
}

public NSMutableDictionary initWithCapacity(int numItems) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCapacity_1, numItems);
	return result != 0 ? this : null;
}

public void removeAllObjects() {
	OS.objc_msgSend(this.id, OS.sel_removeAllObjects);
}

public void removeObjectForKey(id aKey) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectForKey_1, aKey != null ? aKey.id : 0);
}

public void removeObjectsForKeys(NSArray keyArray) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectsForKeys_1, keyArray != null ? keyArray.id : 0);
}

public void setDictionary(NSDictionary otherDictionary) {
	OS.objc_msgSend(this.id, OS.sel_setDictionary_1, otherDictionary != null ? otherDictionary.id : 0);
}

public void setObject(id anObject, id aKey) {
	OS.objc_msgSend(this.id, OS.sel_setObject_1forKey_1, anObject != null ? anObject.id : 0, aKey != null ? aKey.id : 0);
}

public void setObject(id anObject, int aKey) {
	OS.objc_msgSend(this.id, OS.sel_setObject_1forKey_1, anObject != null ? anObject.id : 0, aKey);
}

public void setValue(id value, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_setValue_1forKey_1, value != null ? value.id : 0, key != null ? key.id : 0);
}

}

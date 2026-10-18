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

public class NSMutableDictionary extends NSDictionary {

public NSMutableDictionary() {
	super();
}

public NSMutableDictionary(long id) {
	super(id);
}

public NSMutableDictionary(id id) {
	super(id);
}

public static NSMutableDictionary dictionaryWithCapacity(long numItems) {
	long result = OS.objc_msgSend(OS.class_NSMutableDictionary, OS.sel_dictionaryWithCapacity_, numItems);
	return result != 0 ? new NSMutableDictionary(result) : null;
}

public NSMutableDictionary initWithCapacity(long numItems) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithCapacity_, numItems);
	return result == this.id ? this : (result != 0 ? new NSMutableDictionary(result) : null);
}

public void removeObjectForKey(id aKey) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectForKey_, aKey != null ? aKey.id : 0);
}

public void setDictionary(NSDictionary otherDictionary) {
	OS.objc_msgSend(this.id, OS.sel_setDictionary_, otherDictionary != null ? otherDictionary.id : 0);
}

public void setObject(id anObject, id aKey) {
	OS.objc_msgSend(this.id, OS.sel_setObject_forKey_, anObject != null ? anObject.id : 0, aKey != null ? aKey.id : 0);
}

public void setValue(id value, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_setValue_forKey_, value != null ? value.id : 0, key != null ? key.id : 0);
}

public static NSMutableDictionary dictionaryWithObject(id object, id key) {
	long result = OS.objc_msgSend(OS.class_NSMutableDictionary, OS.sel_dictionaryWithObject_forKey_, object != null ? object.id : 0, key != null ? key.id : 0);
	return result != 0 ? new NSMutableDictionary(result) : null;
}

}

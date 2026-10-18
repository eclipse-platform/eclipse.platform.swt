/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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

public class NSMutableArray extends NSArray {

public NSMutableArray() {
	super();
}

public NSMutableArray(long id) {
	super(id);
}

public NSMutableArray(id id) {
	super(id);
}

public void addObject(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_addObject_, anObject != null ? anObject.id : 0);
}

public void addObjectsFromArray(NSArray otherArray) {
	OS.objc_msgSend(this.id, OS.sel_addObjectsFromArray_, otherArray != null ? otherArray.id : 0);
}

public static NSMutableArray arrayWithCapacity(long numItems) {
	long result = OS.objc_msgSend(OS.class_NSMutableArray, OS.sel_arrayWithCapacity_, numItems);
	return result != 0 ? new NSMutableArray(result) : null;
}

public NSMutableArray initWithCapacity(long numItems) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithCapacity_, numItems);
	return result == this.id ? this : (result != 0 ? new NSMutableArray(result) : null);
}

public void insertObject(id anObject, long index) {
	OS.objc_msgSend(this.id, OS.sel_insertObject_atIndex_, anObject != null ? anObject.id : 0, index);
}

public void removeLastObject() {
	OS.objc_msgSend(this.id, OS.sel_removeLastObject);
}

public void removeObject(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_removeObject_, anObject != null ? anObject.id : 0);
}

public void removeObjectAtIndex(long index) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectAtIndex_, index);
}

public void removeObjectIdenticalTo(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectIdenticalTo_, anObject != null ? anObject.id : 0);
}

public static NSMutableArray array() {
	long result = OS.objc_msgSend(OS.class_NSMutableArray, OS.sel_array);
	return result != 0 ? new NSMutableArray(result) : null;
}

public static NSMutableArray arrayWithObject(id anObject) {
	long result = OS.objc_msgSend(OS.class_NSMutableArray, OS.sel_arrayWithObject_, anObject != null ? anObject.id : 0);
	return result != 0 ? new NSMutableArray(result) : null;
}

}

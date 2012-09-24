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

public class NSMutableArray extends NSArray {

public NSMutableArray() {
	super();
}

public NSMutableArray(long /*int*/ id) {
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

public static NSMutableArray arrayWithCapacity(long /*int*/ numItems) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSMutableArray, OS.sel_arrayWithCapacity_, numItems);
	return result != 0 ? new NSMutableArray(result) : null;
}

public NSMutableArray initWithCapacity(long /*int*/ numItems) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithCapacity_, numItems);
	return result == this.id ? this : (result != 0 ? new NSMutableArray(result) : null);
}

public void removeLastObject() {
	OS.objc_msgSend(this.id, OS.sel_removeLastObject);
}

public void removeObject(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_removeObject_, anObject != null ? anObject.id : 0);
}

public void removeObjectAtIndex(long /*int*/ index) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectAtIndex_, index);
}

public void removeObjectIdenticalTo(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectIdenticalTo_, anObject != null ? anObject.id : 0);
}

public static NSArray array() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSMutableArray, OS.sel_array);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray arrayWithObject(id anObject) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSMutableArray, OS.sel_arrayWithObject_, anObject != null ? anObject.id : 0);
	return result != 0 ? new NSMutableArray(result) : null;
}

}

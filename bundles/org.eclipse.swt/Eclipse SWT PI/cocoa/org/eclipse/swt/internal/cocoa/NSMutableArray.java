/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

public NSMutableArray(int /*long*/ id) {
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

public static NSMutableArray arrayWithCapacity(int /*long*/ numItems) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSMutableArray, OS.sel_arrayWithCapacity_, numItems);
	return result != 0 ? new NSMutableArray(result) : null;
}

public void removeLastObject() {
	OS.objc_msgSend(this.id, OS.sel_removeLastObject);
}

public void removeObject(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_removeObject_, anObject != null ? anObject.id : 0);
}

public void removeObjectAtIndex(int /*long*/ index) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectAtIndex_, index);
}

public static NSArray array() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSMutableArray, OS.sel_array);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray arrayWithObject(id anObject) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSMutableArray, OS.sel_arrayWithObject_, anObject != null ? anObject.id : 0);
	return result != 0 ? new NSMutableArray(result) : null;
}

}

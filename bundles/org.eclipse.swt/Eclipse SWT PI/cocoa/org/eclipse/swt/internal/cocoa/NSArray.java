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

public class NSArray extends NSObject {

public NSArray() {
	super();
}

public NSArray(long /*int*/ id) {
	super(id);
}

public NSArray(id id) {
	super(id);
}

public static NSArray array() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSArray, OS.sel_array);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray arrayWithObject(id anObject) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSArray, OS.sel_arrayWithObject_, anObject != null ? anObject.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public boolean containsObject(id anObject) {
	return OS.objc_msgSend_bool(this.id, OS.sel_containsObject_, anObject != null ? anObject.id : 0);
}

public long /*int*/ count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public long /*int*/ indexOfObjectIdenticalTo(id anObject) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfObjectIdenticalTo_, anObject != null ? anObject.id : 0);
}

public id objectAtIndex(long /*int*/ index) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_objectAtIndex_, index);
	return result != 0 ? new id(result) : null;
}

}

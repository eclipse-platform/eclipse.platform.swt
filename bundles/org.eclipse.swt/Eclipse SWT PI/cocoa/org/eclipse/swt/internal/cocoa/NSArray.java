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

public class NSArray extends NSObject {

public NSArray() {
	super();
}

public NSArray(int /*long*/ id) {
	super(id);
}

public NSArray(id id) {
	super(id);
}

public static NSArray arrayWithObject(id anObject) {
	int result = OS.objc_msgSend(OS.class_NSArray, OS.sel_arrayWithObject_, anObject != null ? anObject.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public int count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public id objectAtIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectAtIndex_, index);
	return result != 0 ? new id(result) : null;
}

}

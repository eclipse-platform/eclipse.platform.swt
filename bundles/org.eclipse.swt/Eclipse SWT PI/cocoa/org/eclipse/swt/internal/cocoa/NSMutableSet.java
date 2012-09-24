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

public class NSMutableSet extends NSSet {

public NSMutableSet() {
	super();
}

public NSMutableSet(long /*int*/ id) {
	super(id);
}

public NSMutableSet(id id) {
	super(id);
}

public void addObjectsFromArray(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_addObjectsFromArray_, array != null ? array.id : 0);
}

public static NSSet set() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSMutableSet, OS.sel_set);
	return result != 0 ? new NSMutableSet(result) : null;
}

}

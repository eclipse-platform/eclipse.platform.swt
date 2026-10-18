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

public class NSMutableSet extends NSSet {

public NSMutableSet() {
	super();
}

public NSMutableSet(long id) {
	super(id);
}

public NSMutableSet(id id) {
	super(id);
}

public void addObjectsFromArray(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_addObjectsFromArray_, array != null ? array.id : 0);
}

public static NSMutableSet set() {
	long result = OS.objc_msgSend(OS.class_NSMutableSet, OS.sel_set);
	return result != 0 ? new NSMutableSet(result) : null;
}

}

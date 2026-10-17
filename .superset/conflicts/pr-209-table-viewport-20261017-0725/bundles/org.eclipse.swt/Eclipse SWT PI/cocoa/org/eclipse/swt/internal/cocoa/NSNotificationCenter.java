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

public class NSNotificationCenter extends NSObject {

public NSNotificationCenter() {
	super();
}

public NSNotificationCenter(long id) {
	super(id);
}

public NSNotificationCenter(id id) {
	super(id);
}

public void addObserver(id observer, long aSelector, NSString aName, id anObject) {
	OS.objc_msgSend(this.id, OS.sel_addObserver_selector_name_object_, observer != null ? observer.id : 0, aSelector, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0);
}

public static NSNotificationCenter defaultCenter() {
	long result = OS.objc_msgSend(OS.class_NSNotificationCenter, OS.sel_defaultCenter);
	return result != 0 ? new NSNotificationCenter(result) : null;
}

public void removeObserver(id observer) {
	OS.objc_msgSend(this.id, OS.sel_removeObserver_, observer != null ? observer.id : 0);
}

}

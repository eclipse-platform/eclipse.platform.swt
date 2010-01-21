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

public class NSNotificationCenter extends NSObject {

public NSNotificationCenter() {
	super();
}

public NSNotificationCenter(int /*long*/ id) {
	super(id);
}

public NSNotificationCenter(id id) {
	super(id);
}

public void addObserver(id observer, int /*long*/ aSelector, NSString aName, id anObject) {
	OS.objc_msgSend(this.id, OS.sel_addObserver_selector_name_object_, observer != null ? observer.id : 0, aSelector, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0);
}

public static NSNotificationCenter defaultCenter() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSNotificationCenter, OS.sel_defaultCenter);
	return result != 0 ? new NSNotificationCenter(result) : null;
}

public void removeObserver(id observer) {
	OS.objc_msgSend(this.id, OS.sel_removeObserver_, observer != null ? observer.id : 0);
}

public void removeObserver(id observer, NSString aName, id anObject) {
	OS.objc_msgSend(this.id, OS.sel_removeObserver_name_object_, observer != null ? observer.id : 0, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0);
}

}

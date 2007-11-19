/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSNotificationCenter extends NSObject {

public NSNotificationCenter() {
	super();
}

public NSNotificationCenter(int id) {
	super(id);
}

public void addObserver(id observer, int aSelector, NSString aName, id anObject) {
	OS.objc_msgSend(this.id, OS.sel_addObserver_1selector_1name_1object_1, observer != null ? observer.id : 0, aSelector, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0);
}

public static NSNotificationCenter defaultCenter() {
	int result = OS.objc_msgSend(OS.class_NSNotificationCenter, OS.sel_defaultCenter);
	return result != 0 ? new NSNotificationCenter(result) : null;
}

public void postNotification(NSNotification notification) {
	OS.objc_msgSend(this.id, OS.sel_postNotification_1, notification != null ? notification.id : 0);
}

public void postNotificationName_object_(NSString aName, id anObject) {
	OS.objc_msgSend(this.id, OS.sel_postNotificationName_1object_1, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0);
}

public void postNotificationName_object_userInfo_(NSString aName, id anObject, NSDictionary aUserInfo) {
	OS.objc_msgSend(this.id, OS.sel_postNotificationName_1object_1userInfo_1, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0, aUserInfo != null ? aUserInfo.id : 0);
}

public void removeObserver_(id observer) {
	OS.objc_msgSend(this.id, OS.sel_removeObserver_1, observer != null ? observer.id : 0);
}

public void removeObserver_name_object_(id observer, NSString aName, id anObject) {
	OS.objc_msgSend(this.id, OS.sel_removeObserver_1name_1object_1, observer != null ? observer.id : 0, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0);
}

}

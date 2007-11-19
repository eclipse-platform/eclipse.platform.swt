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

public class NSDistributedNotificationCenter extends NSNotificationCenter {

public NSDistributedNotificationCenter() {
	super();
}

public NSDistributedNotificationCenter(int id) {
	super(id);
}

public void addObserver_selector_name_object_(id observer, int aSelector, NSString aName, NSString anObject) {
	OS.objc_msgSend(this.id, OS.sel_addObserver_1selector_1name_1object_1, observer != null ? observer.id : 0, aSelector, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0);
}

public void addObserver_selector_name_object_suspensionBehavior_(id observer, int selector, NSString name, NSString object, int suspensionBehavior) {
	OS.objc_msgSend(this.id, OS.sel_addObserver_1selector_1name_1object_1suspensionBehavior_1, observer != null ? observer.id : 0, selector, name != null ? name.id : 0, object != null ? object.id : 0, suspensionBehavior);
}

public static NSNotificationCenter defaultCenter() {
	int result = OS.objc_msgSend(OS.class_NSDistributedNotificationCenter, OS.sel_defaultCenter);
	return result != 0 ? new NSNotificationCenter(result) : null;
}

public static NSDistributedNotificationCenter notificationCenterForType(NSString notificationCenterType) {
	int result = OS.objc_msgSend(OS.class_NSDistributedNotificationCenter, OS.sel_notificationCenterForType_1, notificationCenterType != null ? notificationCenterType.id : 0);
	return result != 0 ? new NSDistributedNotificationCenter(result) : null;
}

public void postNotificationName_object_(NSString aName, NSString anObject) {
	OS.objc_msgSend(this.id, OS.sel_postNotificationName_1object_1, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0);
}

public void postNotificationName_object_userInfo_(NSString aName, NSString anObject, NSDictionary aUserInfo) {
	OS.objc_msgSend(this.id, OS.sel_postNotificationName_1object_1userInfo_1, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0, aUserInfo != null ? aUserInfo.id : 0);
}

public void postNotificationName_object_userInfo_deliverImmediately_(NSString name, NSString object, NSDictionary userInfo, boolean deliverImmediately) {
	OS.objc_msgSend(this.id, OS.sel_postNotificationName_1object_1userInfo_1deliverImmediately_1, name != null ? name.id : 0, object != null ? object.id : 0, userInfo != null ? userInfo.id : 0, deliverImmediately);
}

public void postNotificationName_object_userInfo_options_(NSString name, NSString object, NSDictionary userInfo, int options) {
	OS.objc_msgSend(this.id, OS.sel_postNotificationName_1object_1userInfo_1options_1, name != null ? name.id : 0, object != null ? object.id : 0, userInfo != null ? userInfo.id : 0, options);
}

public void removeObserver(id observer, NSString aName, NSString anObject) {
	OS.objc_msgSend(this.id, OS.sel_removeObserver_1name_1object_1, observer != null ? observer.id : 0, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0);
}

public void setSuspended(boolean suspended) {
	OS.objc_msgSend(this.id, OS.sel_setSuspended_1, suspended);
}

public boolean suspended() {
	return OS.objc_msgSend(this.id, OS.sel_suspended) != 0;
}

}

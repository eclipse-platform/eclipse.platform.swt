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

public class NSNotification extends NSObject {

public NSNotification() {
	super();
}

public NSNotification(int id) {
	super(id);
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public static id static_notificationWithName_object_(NSString aName, id anObject) {
	int result = OS.objc_msgSend(OS.class_NSNotification, OS.sel_notificationWithName_1object_1, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_notificationWithName_object_userInfo_(NSString aName, id anObject, NSDictionary aUserInfo) {
	int result = OS.objc_msgSend(OS.class_NSNotification, OS.sel_notificationWithName_1object_1userInfo_1, aName != null ? aName.id : 0, anObject != null ? anObject.id : 0, aUserInfo != null ? aUserInfo.id : 0);
	return result != 0 ? new id(result) : null;
}

public id object() {
	int result = OS.objc_msgSend(this.id, OS.sel_object);
	return result != 0 ? new id(result) : null;
}

public NSDictionary userInfo() {
	int result = OS.objc_msgSend(this.id, OS.sel_userInfo);
	return result != 0 ? new NSDictionary(result) : null;
}

}

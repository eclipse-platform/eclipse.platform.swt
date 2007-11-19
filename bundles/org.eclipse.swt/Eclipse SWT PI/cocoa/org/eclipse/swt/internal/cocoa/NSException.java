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

public class NSException extends NSObject {

public NSException() {
	super();
}

public NSException(int id) {
	super(id);
}

public NSArray callStackReturnAddresses() {
	int result = OS.objc_msgSend(this.id, OS.sel_callStackReturnAddresses);
	return result != 0 ? new NSArray(result) : null;
}

public static NSException exceptionWithName(NSString name, NSString reason, NSDictionary userInfo) {
	int result = OS.objc_msgSend(OS.class_NSException, OS.sel_exceptionWithName_1reason_1userInfo_1, name != null ? name.id : 0, reason != null ? reason.id : 0, userInfo != null ? userInfo.id : 0);
	return result != 0 ? new NSException(result) : null;
}

public id initWithName(NSString aName, NSString aReason, NSDictionary aUserInfo) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithName_1reason_1userInfo_1, aName != null ? aName.id : 0, aReason != null ? aReason.id : 0, aUserInfo != null ? aUserInfo.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public void raise() {
	OS.objc_msgSend(this.id, OS.sel_raise);
}

public static void static_raise_format_(NSString name, NSString format) {
	OS.objc_msgSend(OS.class_NSException, OS.sel_raise_1format_1, name != null ? name.id : 0, format != null ? format.id : 0);
}

public static void static_raise_format_arguments_(NSString name, NSString format, int argList) {
	OS.objc_msgSend(OS.class_NSException, OS.sel_raise_1format_1arguments_1, name != null ? name.id : 0, format != null ? format.id : 0, argList);
}

public NSString reason() {
	int result = OS.objc_msgSend(this.id, OS.sel_reason);
	return result != 0 ? new NSString(result) : null;
}

public NSDictionary userInfo() {
	int result = OS.objc_msgSend(this.id, OS.sel_userInfo);
	return result != 0 ? new NSDictionary(result) : null;
}

}

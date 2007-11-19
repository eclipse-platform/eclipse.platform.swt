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

public class NSError extends NSObject {

public NSError() {
	super();
}

public NSError(int id) {
	super(id);
}

public int code() {
	return OS.objc_msgSend(this.id, OS.sel_code);
}

public NSString domain() {
	int result = OS.objc_msgSend(this.id, OS.sel_domain);
	return result != 0 ? new NSString(result) : null;
}

public static id errorWithDomain(NSString domain, int code, NSDictionary dict) {
	int result = OS.objc_msgSend(OS.class_NSError, OS.sel_errorWithDomain_1code_1userInfo_1, domain != null ? domain.id : 0, code, dict != null ? dict.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithDomain(NSString domain, int code, NSDictionary dict) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDomain_1code_1userInfo_1, domain != null ? domain.id : 0, code, dict != null ? dict.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString localizedDescription() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedDescription);
	return result != 0 ? new NSString(result) : null;
}

public NSString localizedFailureReason() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedFailureReason);
	return result != 0 ? new NSString(result) : null;
}

public NSArray localizedRecoveryOptions() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedRecoveryOptions);
	return result != 0 ? new NSArray(result) : null;
}

public NSString localizedRecoverySuggestion() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedRecoverySuggestion);
	return result != 0 ? new NSString(result) : null;
}

public id recoveryAttempter() {
	int result = OS.objc_msgSend(this.id, OS.sel_recoveryAttempter);
	return result != 0 ? new id(result) : null;
}

public NSDictionary userInfo() {
	int result = OS.objc_msgSend(this.id, OS.sel_userInfo);
	return result != 0 ? new NSDictionary(result) : null;
}

}

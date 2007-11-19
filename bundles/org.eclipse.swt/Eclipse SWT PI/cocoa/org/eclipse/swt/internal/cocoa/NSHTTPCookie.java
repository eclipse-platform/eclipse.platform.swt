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

public class NSHTTPCookie extends NSObject {

public NSHTTPCookie() {
	super();
}

public NSHTTPCookie(int id) {
	super(id);
}

public NSString comment() {
	int result = OS.objc_msgSend(this.id, OS.sel_comment);
	return result != 0 ? new NSString(result) : null;
}

public NSURL commentURL() {
	int result = OS.objc_msgSend(this.id, OS.sel_commentURL);
	return result != 0 ? new NSURL(result) : null;
}

public static id cookieWithProperties(NSDictionary properties) {
	int result = OS.objc_msgSend(OS.class_NSHTTPCookie, OS.sel_cookieWithProperties_1, properties != null ? properties.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSArray cookiesWithResponseHeaderFields(NSDictionary headerFields, NSURL URL) {
	int result = OS.objc_msgSend(OS.class_NSHTTPCookie, OS.sel_cookiesWithResponseHeaderFields_1forURL_1, headerFields != null ? headerFields.id : 0, URL != null ? URL.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSString domain() {
	int result = OS.objc_msgSend(this.id, OS.sel_domain);
	return result != 0 ? new NSString(result) : null;
}

public NSDate expiresDate() {
	int result = OS.objc_msgSend(this.id, OS.sel_expiresDate);
	return result != 0 ? new NSDate(result) : null;
}

public id initWithProperties(NSDictionary properties) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithProperties_1, properties != null ? properties.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isSecure() {
	return OS.objc_msgSend(this.id, OS.sel_isSecure) != 0;
}

public boolean isSessionOnly() {
	return OS.objc_msgSend(this.id, OS.sel_isSessionOnly) != 0;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public NSString path() {
	int result = OS.objc_msgSend(this.id, OS.sel_path);
	return result != 0 ? new NSString(result) : null;
}

public NSArray portList() {
	int result = OS.objc_msgSend(this.id, OS.sel_portList);
	return result != 0 ? new NSArray(result) : null;
}

public NSDictionary properties() {
	int result = OS.objc_msgSend(this.id, OS.sel_properties);
	return result != 0 ? new NSDictionary(result) : null;
}

public static NSDictionary requestHeaderFieldsWithCookies(NSArray cookies) {
	int result = OS.objc_msgSend(OS.class_NSHTTPCookie, OS.sel_requestHeaderFieldsWithCookies_1, cookies != null ? cookies.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSString value() {
	int result = OS.objc_msgSend(this.id, OS.sel_value);
	return result != 0 ? new NSString(result) : null;
}

//public int version() {
//	return OS.objc_msgSend(this.id, OS.sel_version);
//}

}

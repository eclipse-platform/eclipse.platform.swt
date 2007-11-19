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

public class NSURLRequest extends NSObject {

public NSURLRequest() {
	super();
}

public NSURLRequest(int id) {
	super(id);
}

public NSData HTTPBody() {
	int result = OS.objc_msgSend(this.id, OS.sel_HTTPBody);
	return result != 0 ? new NSData(result) : null;
}

public NSInputStream HTTPBodyStream() {
	int result = OS.objc_msgSend(this.id, OS.sel_HTTPBodyStream);
	return result != 0 ? new NSInputStream(result) : null;
}

public NSString HTTPMethod() {
	int result = OS.objc_msgSend(this.id, OS.sel_HTTPMethod);
	return result != 0 ? new NSString(result) : null;
}

public boolean HTTPShouldHandleCookies() {
	return OS.objc_msgSend(this.id, OS.sel_HTTPShouldHandleCookies) != 0;
}

public NSURL URL() {
	int result = OS.objc_msgSend(this.id, OS.sel_URL);
	return result != 0 ? new NSURL(result) : null;
}

public NSDictionary allHTTPHeaderFields() {
	int result = OS.objc_msgSend(this.id, OS.sel_allHTTPHeaderFields);
	return result != 0 ? new NSDictionary(result) : null;
}

public int cachePolicy() {
	return OS.objc_msgSend(this.id, OS.sel_cachePolicy);
}

public id initWithURL_(NSURL URL) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithURL_1, URL != null ? URL.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithURL_cachePolicy_timeoutInterval_(NSURL URL, int cachePolicy, double timeoutInterval) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithURL_1cachePolicy_1timeoutInterval_1, URL != null ? URL.id : 0, cachePolicy, timeoutInterval);
	return result != 0 ? new id(result) : null;
}

public NSURL mainDocumentURL() {
	int result = OS.objc_msgSend(this.id, OS.sel_mainDocumentURL);
	return result != 0 ? new NSURL(result) : null;
}

public static NSURLRequest static_requestWithURL_(NSURL URL) {
	int result = OS.objc_msgSend(OS.class_NSURLRequest, OS.sel_requestWithURL_1, URL != null ? URL.id : 0);
	return result != 0 ? new NSURLRequest(result) : null;
}

public static id static_requestWithURL_cachePolicy_timeoutInterval_(NSURL URL, int cachePolicy, double timeoutInterval) {
	int result = OS.objc_msgSend(OS.class_NSURLRequest, OS.sel_requestWithURL_1cachePolicy_1timeoutInterval_1, URL != null ? URL.id : 0, cachePolicy, timeoutInterval);
	return result != 0 ? new id(result) : null;
}

public double timeoutInterval() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_timeoutInterval);
}

public NSString valueForHTTPHeaderField(NSString field) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueForHTTPHeaderField_1, field != null ? field.id : 0);
	return result != 0 ? new NSString(result) : null;
}

}

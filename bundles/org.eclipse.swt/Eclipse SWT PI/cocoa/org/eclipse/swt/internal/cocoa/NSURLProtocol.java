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

public class NSURLProtocol extends NSObject {

public NSURLProtocol() {
	super();
}

public NSURLProtocol(int id) {
	super(id);
}

public NSCachedURLResponse cachedResponse() {
	int result = OS.objc_msgSend(this.id, OS.sel_cachedResponse);
	return result != 0 ? new NSCachedURLResponse(result) : null;
}

public static boolean canInitWithRequest(NSURLRequest request) {
	return OS.objc_msgSend(OS.class_NSURLProtocol, OS.sel_canInitWithRequest_1, request != null ? request.id : 0) != 0;
}

public static NSURLRequest canonicalRequestForRequest(NSURLRequest request) {
	int result = OS.objc_msgSend(OS.class_NSURLProtocol, OS.sel_canonicalRequestForRequest_1, request != null ? request.id : 0);
	return result != 0 ? new NSURLRequest(result) : null;
}

public id  client() {
	int result = OS.objc_msgSend(this.id, OS.sel_client);
	return result != 0 ? new id (result) : null;
}

public id initWithRequest(NSURLRequest request, NSCachedURLResponse cachedResponse, id  client) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithRequest_1cachedResponse_1client_1, request != null ? request.id : 0, cachedResponse != null ? cachedResponse.id : 0, client != null ? client.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id propertyForKey(NSString key, NSURLRequest request) {
	int result = OS.objc_msgSend(OS.class_NSURLProtocol, OS.sel_propertyForKey_1inRequest_1, key != null ? key.id : 0, request != null ? request.id : 0);
	return result != 0 ? new id(result) : null;
}

public static boolean registerClass(int protocolClass) {
	return OS.objc_msgSend(OS.class_NSURLProtocol, OS.sel_registerClass_1, protocolClass) != 0;
}

public static void removePropertyForKey(NSString key, NSMutableURLRequest request) {
	OS.objc_msgSend(OS.class_NSURLProtocol, OS.sel_removePropertyForKey_1inRequest_1, key != null ? key.id : 0, request != null ? request.id : 0);
}

public NSURLRequest request() {
	int result = OS.objc_msgSend(this.id, OS.sel_request);
	return result != 0 ? new NSURLRequest(result) : null;
}

public static boolean requestIsCacheEquivalent(NSURLRequest a, NSURLRequest b) {
	return OS.objc_msgSend(OS.class_NSURLProtocol, OS.sel_requestIsCacheEquivalent_1toRequest_1, a != null ? a.id : 0, b != null ? b.id : 0) != 0;
}

public static void setProperty(id value, NSString key, NSMutableURLRequest request) {
	OS.objc_msgSend(OS.class_NSURLProtocol, OS.sel_setProperty_1forKey_1inRequest_1, value != null ? value.id : 0, key != null ? key.id : 0, request != null ? request.id : 0);
}

public void startLoading() {
	OS.objc_msgSend(this.id, OS.sel_startLoading);
}

public void stopLoading() {
	OS.objc_msgSend(this.id, OS.sel_stopLoading);
}

public static void unregisterClass(int protocolClass) {
	OS.objc_msgSend(OS.class_NSURLProtocol, OS.sel_unregisterClass_1, protocolClass);
}

}

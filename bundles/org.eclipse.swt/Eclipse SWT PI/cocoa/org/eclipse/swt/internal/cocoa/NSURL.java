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

public class NSURL extends NSObject {

public NSURL() {
	super();
}

public NSURL(int id) {
	super(id);
}

public NSURLHandle URLHandleUsingCache(boolean shouldUseCache) {
	int result = OS.objc_msgSend(this.id, OS.sel_URLHandleUsingCache_1, shouldUseCache);
	return result != 0 ? new NSURLHandle(result) : null;
}

public static NSURL static_URLWithString_(NSString URLString) {
	int result = OS.objc_msgSend(OS.class_NSURL, OS.sel_URLWithString_1, URLString != null ? URLString.id : 0);
	return result != 0 ? new NSURL(result) : null;
}

public static id static_URLWithString_relativeToURL_(NSString URLString, NSURL baseURL) {
	int result = OS.objc_msgSend(OS.class_NSURL, OS.sel_URLWithString_1relativeToURL_1, URLString != null ? URLString.id : 0, baseURL != null ? baseURL.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString absoluteString() {
	int result = OS.objc_msgSend(this.id, OS.sel_absoluteString);
	return result != 0 ? new NSString(result) : null;
}

public NSURL absoluteURL() {
	int result = OS.objc_msgSend(this.id, OS.sel_absoluteURL);
	return result == this.id ? this : (result != 0 ? new NSURL(result) : null);
}

public NSURL baseURL() {
	int result = OS.objc_msgSend(this.id, OS.sel_baseURL);
	return result == this.id ? this : (result != 0 ? new NSURL(result) : null);
}

public static NSURL static_fileURLWithPath_(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSURL, OS.sel_fileURLWithPath_1, path != null ? path.id : 0);
	return result != 0 ? new NSURL(result) : null;
}

public static id static_fileURLWithPath_isDirectory_(NSString path, boolean isDir) {
	int result = OS.objc_msgSend(OS.class_NSURL, OS.sel_fileURLWithPath_1isDirectory_1, path != null ? path.id : 0, isDir);
	return result != 0 ? new id(result) : null;
}

public NSString fragment() {
	int result = OS.objc_msgSend(this.id, OS.sel_fragment);
	return result != 0 ? new NSString(result) : null;
}

public NSString host() {
	int result = OS.objc_msgSend(this.id, OS.sel_host);
	return result != 0 ? new NSString(result) : null;
}

public id initFileURLWithPath_(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initFileURLWithPath_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initFileURLWithPath_isDirectory_(NSString path, boolean isDir) {
	int result = OS.objc_msgSend(this.id, OS.sel_initFileURLWithPath_1isDirectory_1, path != null ? path.id : 0, isDir);
	return result != 0 ? new id(result) : null;
}

public id initWithScheme(NSString scheme, NSString host, NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithScheme_1host_1path_1, scheme != null ? scheme.id : 0, host != null ? host.id : 0, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithString_(NSString URLString) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithString_1, URLString != null ? URLString.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithString_relativeToURL_(NSString URLString, NSURL baseURL) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithString_1relativeToURL_1, URLString != null ? URLString.id : 0, baseURL != null ? baseURL.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isFileURL() {
	return OS.objc_msgSend(this.id, OS.sel_isFileURL) != 0;
}

public void loadResourceDataNotifyingClient(id client, boolean shouldUseCache) {
	OS.objc_msgSend(this.id, OS.sel_loadResourceDataNotifyingClient_1usingCache_1, client != null ? client.id : 0, shouldUseCache);
}

public NSString parameterString() {
	int result = OS.objc_msgSend(this.id, OS.sel_parameterString);
	return result != 0 ? new NSString(result) : null;
}

public NSString password() {
	int result = OS.objc_msgSend(this.id, OS.sel_password);
	return result != 0 ? new NSString(result) : null;
}

public NSString path() {
	int result = OS.objc_msgSend(this.id, OS.sel_path);
	return result != 0 ? new NSString(result) : null;
}

public NSNumber port() {
	int result = OS.objc_msgSend(this.id, OS.sel_port);
	return result != 0 ? new NSNumber(result) : null;
}

public id propertyForKey(NSString propertyKey) {
	int result = OS.objc_msgSend(this.id, OS.sel_propertyForKey_1, propertyKey != null ? propertyKey.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString query() {
	int result = OS.objc_msgSend(this.id, OS.sel_query);
	return result != 0 ? new NSString(result) : null;
}

public NSString relativePath() {
	int result = OS.objc_msgSend(this.id, OS.sel_relativePath);
	return result != 0 ? new NSString(result) : null;
}

public NSString relativeString() {
	int result = OS.objc_msgSend(this.id, OS.sel_relativeString);
	return result != 0 ? new NSString(result) : null;
}

public NSData resourceDataUsingCache(boolean shouldUseCache) {
	int result = OS.objc_msgSend(this.id, OS.sel_resourceDataUsingCache_1, shouldUseCache);
	return result != 0 ? new NSData(result) : null;
}

public NSString resourceSpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_resourceSpecifier);
	return result != 0 ? new NSString(result) : null;
}

public NSString scheme() {
	int result = OS.objc_msgSend(this.id, OS.sel_scheme);
	return result != 0 ? new NSString(result) : null;
}

public boolean setProperty(id property, NSString propertyKey) {
	return OS.objc_msgSend(this.id, OS.sel_setProperty_1forKey_1, property != null ? property.id : 0, propertyKey != null ? propertyKey.id : 0) != 0;
}

public boolean setResourceData(NSData data) {
	return OS.objc_msgSend(this.id, OS.sel_setResourceData_1, data != null ? data.id : 0) != 0;
}

public NSURL standardizedURL() {
	int result = OS.objc_msgSend(this.id, OS.sel_standardizedURL);
	return result == this.id ? this : (result != 0 ? new NSURL(result) : null);
}

public NSString user() {
	int result = OS.objc_msgSend(this.id, OS.sel_user);
	return result != 0 ? new NSString(result) : null;
}

}

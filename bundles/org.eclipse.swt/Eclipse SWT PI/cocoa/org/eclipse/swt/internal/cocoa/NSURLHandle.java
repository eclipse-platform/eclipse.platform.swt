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

public class NSURLHandle extends NSObject {

public NSURLHandle() {
	super();
}

public NSURLHandle(int id) {
	super(id);
}

public static int URLHandleClassForURL(NSURL anURL) {
	return OS.objc_msgSend(OS.class_NSURLHandle, OS.sel_URLHandleClassForURL_1, anURL != null ? anURL.id : 0);
}

public void addClient(id  client) {
	OS.objc_msgSend(this.id, OS.sel_addClient_1, client != null ? client.id : 0);
}

public NSData availableResourceData() {
	int result = OS.objc_msgSend(this.id, OS.sel_availableResourceData);
	return result != 0 ? new NSData(result) : null;
}

public void backgroundLoadDidFailWithReason(NSString reason) {
	OS.objc_msgSend(this.id, OS.sel_backgroundLoadDidFailWithReason_1, reason != null ? reason.id : 0);
}

public void beginLoadInBackground() {
	OS.objc_msgSend(this.id, OS.sel_beginLoadInBackground);
}

public static NSURLHandle cachedHandleForURL(NSURL anURL) {
	int result = OS.objc_msgSend(OS.class_NSURLHandle, OS.sel_cachedHandleForURL_1, anURL != null ? anURL.id : 0);
	return result != 0 ? new NSURLHandle(result) : null;
}

public static boolean canInitWithURL(NSURL anURL) {
	return OS.objc_msgSend(OS.class_NSURLHandle, OS.sel_canInitWithURL_1, anURL != null ? anURL.id : 0) != 0;
}

public void cancelLoadInBackground() {
	OS.objc_msgSend(this.id, OS.sel_cancelLoadInBackground);
}

public void didLoadBytes(NSData newBytes, boolean yorn) {
	OS.objc_msgSend(this.id, OS.sel_didLoadBytes_1loadComplete_1, newBytes != null ? newBytes.id : 0, yorn);
}

public void endLoadInBackground() {
	OS.objc_msgSend(this.id, OS.sel_endLoadInBackground);
}

public long expectedResourceDataSize() {
	return (long)OS.objc_msgSend(this.id, OS.sel_expectedResourceDataSize);
}

public NSString failureReason() {
	int result = OS.objc_msgSend(this.id, OS.sel_failureReason);
	return result != 0 ? new NSString(result) : null;
}

public void flushCachedData() {
	OS.objc_msgSend(this.id, OS.sel_flushCachedData);
}

public id initWithURL(NSURL anURL, boolean willCache) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithURL_1cached_1, anURL != null ? anURL.id : 0, willCache);
	return result != 0 ? new id(result) : null;
}

public void loadInBackground() {
	OS.objc_msgSend(this.id, OS.sel_loadInBackground);
}

public NSData loadInForeground() {
	int result = OS.objc_msgSend(this.id, OS.sel_loadInForeground);
	return result != 0 ? new NSData(result) : null;
}

public id propertyForKey(NSString propertyKey) {
	int result = OS.objc_msgSend(this.id, OS.sel_propertyForKey_1, propertyKey != null ? propertyKey.id : 0);
	return result != 0 ? new id(result) : null;
}

public id propertyForKeyIfAvailable(NSString propertyKey) {
	int result = OS.objc_msgSend(this.id, OS.sel_propertyForKeyIfAvailable_1, propertyKey != null ? propertyKey.id : 0);
	return result != 0 ? new id(result) : null;
}

public static void registerURLHandleClass(int anURLHandleSubclass) {
	OS.objc_msgSend(OS.class_NSURLHandle, OS.sel_registerURLHandleClass_1, anURLHandleSubclass);
}

public void removeClient(id  client) {
	OS.objc_msgSend(this.id, OS.sel_removeClient_1, client != null ? client.id : 0);
}

public NSData resourceData() {
	int result = OS.objc_msgSend(this.id, OS.sel_resourceData);
	return result != 0 ? new NSData(result) : null;
}

public int status() {
	return OS.objc_msgSend(this.id, OS.sel_status);
}

public boolean writeData(NSData data) {
	return OS.objc_msgSend(this.id, OS.sel_writeData_1, data != null ? data.id : 0) != 0;
}

public boolean writeProperty(id propertyValue, NSString propertyKey) {
	return OS.objc_msgSend(this.id, OS.sel_writeProperty_1forKey_1, propertyValue != null ? propertyValue.id : 0, propertyKey != null ? propertyKey.id : 0) != 0;
}

}

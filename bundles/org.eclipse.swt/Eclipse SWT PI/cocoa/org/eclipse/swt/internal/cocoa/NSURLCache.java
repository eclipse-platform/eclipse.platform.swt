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

public class NSURLCache extends NSObject {

public NSURLCache() {
	super();
}

public NSURLCache(int id) {
	super(id);
}

public NSCachedURLResponse cachedResponseForRequest(NSURLRequest request) {
	int result = OS.objc_msgSend(this.id, OS.sel_cachedResponseForRequest_1, request != null ? request.id : 0);
	return result != 0 ? new NSCachedURLResponse(result) : null;
}

public int currentDiskUsage() {
	return OS.objc_msgSend(this.id, OS.sel_currentDiskUsage);
}

public int currentMemoryUsage() {
	return OS.objc_msgSend(this.id, OS.sel_currentMemoryUsage);
}

public int diskCapacity() {
	return OS.objc_msgSend(this.id, OS.sel_diskCapacity);
}

public id initWithMemoryCapacity(int memoryCapacity, int diskCapacity, NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithMemoryCapacity_1diskCapacity_1diskPath_1, memoryCapacity, diskCapacity, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public int memoryCapacity() {
	return OS.objc_msgSend(this.id, OS.sel_memoryCapacity);
}

public void removeAllCachedResponses() {
	OS.objc_msgSend(this.id, OS.sel_removeAllCachedResponses);
}

public void removeCachedResponseForRequest(NSURLRequest request) {
	OS.objc_msgSend(this.id, OS.sel_removeCachedResponseForRequest_1, request != null ? request.id : 0);
}

public void setDiskCapacity(int diskCapacity) {
	OS.objc_msgSend(this.id, OS.sel_setDiskCapacity_1, diskCapacity);
}

public void setMemoryCapacity(int memoryCapacity) {
	OS.objc_msgSend(this.id, OS.sel_setMemoryCapacity_1, memoryCapacity);
}

public static void setSharedURLCache(NSURLCache cache) {
	OS.objc_msgSend(OS.class_NSURLCache, OS.sel_setSharedURLCache_1, cache != null ? cache.id : 0);
}

public static NSURLCache sharedURLCache() {
	int result = OS.objc_msgSend(OS.class_NSURLCache, OS.sel_sharedURLCache);
	return result != 0 ? new NSURLCache(result) : null;
}

public void storeCachedResponse(NSCachedURLResponse cachedResponse, NSURLRequest request) {
	OS.objc_msgSend(this.id, OS.sel_storeCachedResponse_1forRequest_1, cachedResponse != null ? cachedResponse.id : 0, request != null ? request.id : 0);
}

}

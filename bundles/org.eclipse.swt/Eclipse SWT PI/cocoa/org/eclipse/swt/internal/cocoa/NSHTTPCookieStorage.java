/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSHTTPCookieStorage extends NSObject {

public NSHTTPCookieStorage() {
	super();
}

public NSHTTPCookieStorage(long /*int*/ id) {
	super(id);
}

public NSHTTPCookieStorage(id id) {
	super(id);
}

public NSArray cookies() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_cookies);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray cookiesForURL(NSURL URL) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_cookiesForURL_, URL != null ? URL.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public void deleteCookie(NSHTTPCookie cookie) {
	OS.objc_msgSend(this.id, OS.sel_deleteCookie_, cookie != null ? cookie.id : 0);
}

public void setCookie(NSHTTPCookie cookie) {
	OS.objc_msgSend(this.id, OS.sel_setCookie_, cookie != null ? cookie.id : 0);
}

public static NSHTTPCookieStorage sharedHTTPCookieStorage() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSHTTPCookieStorage, OS.sel_sharedHTTPCookieStorage);
	return result != 0 ? new NSHTTPCookieStorage(result) : null;
}

}

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

public class NSHTTPCookieStorage extends NSObject {

public NSHTTPCookieStorage() {
	super();
}

public NSHTTPCookieStorage(int id) {
	super(id);
}

public int cookieAcceptPolicy() {
	return OS.objc_msgSend(this.id, OS.sel_cookieAcceptPolicy);
}

public NSArray cookies() {
	int result = OS.objc_msgSend(this.id, OS.sel_cookies);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray cookiesForURL(NSURL URL) {
	int result = OS.objc_msgSend(this.id, OS.sel_cookiesForURL_1, URL != null ? URL.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public void deleteCookie(NSHTTPCookie cookie) {
	OS.objc_msgSend(this.id, OS.sel_deleteCookie_1, cookie != null ? cookie.id : 0);
}

public void setCookie(NSHTTPCookie cookie) {
	OS.objc_msgSend(this.id, OS.sel_setCookie_1, cookie != null ? cookie.id : 0);
}

public void setCookieAcceptPolicy(int cookieAcceptPolicy) {
	OS.objc_msgSend(this.id, OS.sel_setCookieAcceptPolicy_1, cookieAcceptPolicy);
}

public void setCookies(NSArray cookies, NSURL URL, NSURL mainDocumentURL) {
	OS.objc_msgSend(this.id, OS.sel_setCookies_1forURL_1mainDocumentURL_1, cookies != null ? cookies.id : 0, URL != null ? URL.id : 0, mainDocumentURL != null ? mainDocumentURL.id : 0);
}

public static NSHTTPCookieStorage sharedHTTPCookieStorage() {
	int result = OS.objc_msgSend(OS.class_NSHTTPCookieStorage, OS.sel_sharedHTTPCookieStorage);
	return result != 0 ? new NSHTTPCookieStorage(result) : null;
}

}

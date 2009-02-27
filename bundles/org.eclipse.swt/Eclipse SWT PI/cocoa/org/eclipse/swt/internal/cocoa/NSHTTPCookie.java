/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSHTTPCookie extends NSObject {

public NSHTTPCookie() {
	super();
}

public NSHTTPCookie(int /*long*/ id) {
	super(id);
}

public NSHTTPCookie(id id) {
	super(id);
}

public static NSArray cookiesWithResponseHeaderFields(NSDictionary headerFields, NSURL URL) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSHTTPCookie, OS.sel_cookiesWithResponseHeaderFields_forURL_, headerFields != null ? headerFields.id : 0, URL != null ? URL.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public boolean isSessionOnly() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isSessionOnly);
}

public NSString name() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public NSString value() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_value);
	return result != 0 ? new NSString(result) : null;
}

}

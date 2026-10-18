/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSMutableURLRequest extends NSURLRequest {

public NSMutableURLRequest() {
	super();
}

public NSMutableURLRequest(long id) {
	super(id);
}

public NSMutableURLRequest(id id) {
	super(id);
}

public void setCachePolicy(long cachePolicy) {
	OS.objc_msgSend(this.id, OS.sel_setCachePolicy_, cachePolicy);
}

public void setHTTPBody(NSData HTTPBody) {
	OS.objc_msgSend(this.id, OS.sel_setHTTPBody_, HTTPBody != null ? HTTPBody.id : 0);
}

public void setHTTPMethod(NSString HTTPMethod) {
	OS.objc_msgSend(this.id, OS.sel_setHTTPMethod_, HTTPMethod != null ? HTTPMethod.id : 0);
}

public void setURL(NSURL URL) {
	OS.objc_msgSend(this.id, OS.sel_setURL_, URL != null ? URL.id : 0);
}

public void setValue(NSString value, NSString field) {
	OS.objc_msgSend(this.id, OS.sel_setValue_forHTTPHeaderField_, value != null ? value.id : 0, field != null ? field.id : 0);
}

public static NSMutableURLRequest requestWithURL(NSURL URL) {
	long result = OS.objc_msgSend(OS.class_NSMutableURLRequest, OS.sel_requestWithURL_, URL != null ? URL.id : 0);
	return result != 0 ? new NSMutableURLRequest(result) : null;
}

}

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

public class NSMutableURLRequest extends NSURLRequest {

public NSMutableURLRequest() {
	super();
}

public NSMutableURLRequest(int id) {
	super(id);
}

public void addValue(NSString value, NSString field) {
	OS.objc_msgSend(this.id, OS.sel_addValue_1forHTTPHeaderField_1, value != null ? value.id : 0, field != null ? field.id : 0);
}

public void setAllHTTPHeaderFields(NSDictionary headerFields) {
	OS.objc_msgSend(this.id, OS.sel_setAllHTTPHeaderFields_1, headerFields != null ? headerFields.id : 0);
}

public void setCachePolicy(int policy) {
	OS.objc_msgSend(this.id, OS.sel_setCachePolicy_1, policy);
}

public void setHTTPBody(NSData data) {
	OS.objc_msgSend(this.id, OS.sel_setHTTPBody_1, data != null ? data.id : 0);
}

public void setHTTPBodyStream(NSInputStream inputStream) {
	OS.objc_msgSend(this.id, OS.sel_setHTTPBodyStream_1, inputStream != null ? inputStream.id : 0);
}

public void setHTTPMethod(NSString method) {
	OS.objc_msgSend(this.id, OS.sel_setHTTPMethod_1, method != null ? method.id : 0);
}

public void setHTTPShouldHandleCookies(boolean should) {
	OS.objc_msgSend(this.id, OS.sel_setHTTPShouldHandleCookies_1, should);
}

public void setMainDocumentURL(NSURL URL) {
	OS.objc_msgSend(this.id, OS.sel_setMainDocumentURL_1, URL != null ? URL.id : 0);
}

public void setTimeoutInterval(double seconds) {
	OS.objc_msgSend(this.id, OS.sel_setTimeoutInterval_1, seconds);
}

public void setURL(NSURL URL) {
	OS.objc_msgSend(this.id, OS.sel_setURL_1, URL != null ? URL.id : 0);
}

public void setValue(NSString value, NSString field) {
	OS.objc_msgSend(this.id, OS.sel_setValue_1forHTTPHeaderField_1, value != null ? value.id : 0, field != null ? field.id : 0);
}

}

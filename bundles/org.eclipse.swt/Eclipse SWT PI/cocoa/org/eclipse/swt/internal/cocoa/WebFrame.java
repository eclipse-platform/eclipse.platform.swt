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

public class WebFrame extends NSObject {

public WebFrame() {
	super();
}

public WebFrame(int id) {
	super(id);
}

public DOMDocument DOMDocument() {
	int result = OS.objc_msgSend(this.id, OS.sel_DOMDocument);
	return result != 0 ? new DOMDocument(result) : null;
}

public NSArray childFrames() {
	int result = OS.objc_msgSend(this.id, OS.sel_childFrames);
	return result != 0 ? new NSArray(result) : null;
}

public WebDataSource dataSource() {
	int result = OS.objc_msgSend(this.id, OS.sel_dataSource);
	return result != 0 ? new WebDataSource(result) : null;
}

public WebFrame findFrameNamed(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_findFrameNamed_1, name != null ? name.id : 0);
	return result == this.id ? this : (result != 0 ? new WebFrame(result) : null);
}

public int globalContext() {
	return OS.objc_msgSend(this.id, OS.sel_globalContext);
}

public void loadAlternateHTMLString(NSString string, NSURL baseURL, NSURL unreachableURL) {
	OS.objc_msgSend(this.id, OS.sel_loadAlternateHTMLString_1baseURL_1forUnreachableURL_1, string != null ? string.id : 0, baseURL != null ? baseURL.id : 0, unreachableURL != null ? unreachableURL.id : 0);
}

public void loadData(NSData data, NSString MIMEType, NSString encodingName, NSURL URL) {
	OS.objc_msgSend(this.id, OS.sel_loadData_1MIMEType_1textEncodingName_1baseURL_1, data != null ? data.id : 0, MIMEType != null ? MIMEType.id : 0, encodingName != null ? encodingName.id : 0, URL != null ? URL.id : 0);
}

public void loadHTMLString(NSString string, NSURL URL) {
	OS.objc_msgSend(this.id, OS.sel_loadHTMLString_1baseURL_1, string != null ? string.id : 0, URL != null ? URL.id : 0);
}

public void loadRequest(NSURLRequest request) {
	OS.objc_msgSend(this.id, OS.sel_loadRequest_1, request != null ? request.id : 0);
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public WebFrame parentFrame() {
	int result = OS.objc_msgSend(this.id, OS.sel_parentFrame);
	return result == this.id ? this : (result != 0 ? new WebFrame(result) : null);
}

public WebDataSource provisionalDataSource() {
	int result = OS.objc_msgSend(this.id, OS.sel_provisionalDataSource);
	return result != 0 ? new WebDataSource(result) : null;
}

public void reload() {
	OS.objc_msgSend(this.id, OS.sel_reload);
}

public void stopLoading() {
	OS.objc_msgSend(this.id, OS.sel_stopLoading);
}

public WebView webView() {
	int result = OS.objc_msgSend(this.id, OS.sel_webView);
	return result != 0 ? new WebView(result) : null;
}

}

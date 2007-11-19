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

public class WebDataSource extends NSObject {

public WebDataSource() {
	super();
}

public WebDataSource(int id) {
	super(id);
}

//public void addSubresource(WebResource subresource) {
//	OS.objc_msgSend(this.id, OS.sel_addSubresource_1, subresource != null ? subresource.id : 0);
//}

public NSData data() {
	int result = OS.objc_msgSend(this.id, OS.sel_data);
	return result != 0 ? new NSData(result) : null;
}

//public id initWithRequest(NSURLRequest request) {
//	int result = OS.objc_msgSend(this.id, OS.sel_initWithRequest_1, request != null ? request.id : 0);
//	return result != 0 ? new id(result) : null;
//}

//public NSURLRequest initialRequest() {
//	int result = OS.objc_msgSend(this.id, OS.sel_initialRequest);
//	return result != 0 ? new NSURLRequest(result) : null;
//}

public boolean isLoading() {
	return OS.objc_msgSend(this.id, OS.sel_isLoading) != 0;
}

//public WebResource mainResource() {
//	int result = OS.objc_msgSend(this.id, OS.sel_mainResource);
//	return result != 0 ? new WebResource(result) : null;
//}

public NSString pageTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_pageTitle);
	return result != 0 ? new NSString(result) : null;
}

public WebDocumentRepresentation representation() {
	int result = OS.objc_msgSend(this.id, OS.sel_representation);
	return result != 0 ? new WebDocumentRepresentation (result) : null;
}

public NSMutableURLRequest request() {
	int result = OS.objc_msgSend(this.id, OS.sel_request);
	return result != 0 ? new NSMutableURLRequest(result) : null;
}

public NSURLResponse response() {
	int result = OS.objc_msgSend(this.id, OS.sel_response);
	return result != 0 ? new NSURLResponse(result) : null;
}

//public WebResource subresourceForURL(NSURL URL) {
//	int result = OS.objc_msgSend(this.id, OS.sel_subresourceForURL_1, URL != null ? URL.id : 0);
//	return result != 0 ? new WebResource(result) : null;
//}

//public NSArray subresources() {
//	int result = OS.objc_msgSend(this.id, OS.sel_subresources);
//	return result != 0 ? new NSArray(result) : null;
//}

public NSString textEncodingName() {
	int result = OS.objc_msgSend(this.id, OS.sel_textEncodingName);
	return result != 0 ? new NSString(result) : null;
}

//public NSURL unreachableURL() {
//	int result = OS.objc_msgSend(this.id, OS.sel_unreachableURL);
//	return result != 0 ? new NSURL(result) : null;
//}

//public WebArchive webArchive() {
//	int result = OS.objc_msgSend(this.id, OS.sel_webArchive);
//	return result != 0 ? new WebArchive(result) : null;
//}

public WebFrame webFrame() {
	int result = OS.objc_msgSend(this.id, OS.sel_webFrame);
	return result != 0 ? new WebFrame(result) : null;
}

}

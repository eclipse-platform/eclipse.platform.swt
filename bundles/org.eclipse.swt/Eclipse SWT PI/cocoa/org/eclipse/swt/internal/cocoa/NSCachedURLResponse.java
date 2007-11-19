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

public class NSCachedURLResponse extends NSObject {

public NSCachedURLResponse() {
	super();
}

public NSCachedURLResponse(int id) {
	super(id);
}

public NSData data() {
	int result = OS.objc_msgSend(this.id, OS.sel_data);
	return result != 0 ? new NSData(result) : null;
}

public NSCachedURLResponse initWithResponse_data_(NSURLResponse response, NSData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithResponse_1data_1, response != null ? response.id : 0, data != null ? data.id : 0);
	return result != 0 ? this : null;
}

public NSCachedURLResponse initWithResponse_data_userInfo_storagePolicy_(NSURLResponse response, NSData data, NSDictionary userInfo, int storagePolicy) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithResponse_1data_1userInfo_1storagePolicy_1, response != null ? response.id : 0, data != null ? data.id : 0, userInfo != null ? userInfo.id : 0, storagePolicy);
	return result != 0 ? this : null;
}

public NSURLResponse response() {
	int result = OS.objc_msgSend(this.id, OS.sel_response);
	return result != 0 ? new NSURLResponse(result) : null;
}

public int storagePolicy() {
	return OS.objc_msgSend(this.id, OS.sel_storagePolicy);
}

public NSDictionary userInfo() {
	int result = OS.objc_msgSend(this.id, OS.sel_userInfo);
	return result != 0 ? new NSDictionary(result) : null;
}

}

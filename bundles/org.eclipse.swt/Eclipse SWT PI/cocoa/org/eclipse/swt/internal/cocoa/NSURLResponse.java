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

public class NSURLResponse extends NSObject {

public NSURLResponse() {
	super();
}

public NSURLResponse(int id) {
	super(id);
}

public NSString MIMEType() {
	int result = OS.objc_msgSend(this.id, OS.sel_MIMEType);
	return result != 0 ? new NSString(result) : null;
}

public NSURL URL() {
	int result = OS.objc_msgSend(this.id, OS.sel_URL);
	return result != 0 ? new NSURL(result) : null;
}

public long expectedContentLength() {
	return (long)OS.objc_msgSend(this.id, OS.sel_expectedContentLength);
}

public id initWithURL(NSURL URL, NSString MIMEType, int length, NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithURL_1MIMEType_1expectedContentLength_1textEncodingName_1, URL != null ? URL.id : 0, MIMEType != null ? MIMEType.id : 0, length, name != null ? name.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString suggestedFilename() {
	int result = OS.objc_msgSend(this.id, OS.sel_suggestedFilename);
	return result != 0 ? new NSString(result) : null;
}

public NSString textEncodingName() {
	int result = OS.objc_msgSend(this.id, OS.sel_textEncodingName);
	return result != 0 ? new NSString(result) : null;
}

}

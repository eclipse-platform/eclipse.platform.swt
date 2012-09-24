/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

public WebFrame(long /*int*/ id) {
	super(id);
}

public WebFrame(id id) {
	super(id);
}

public DOMDocument DOMDocument() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_DOMDocument);
	return result != 0 ? new DOMDocument(result) : null;
}

public WebDataSource dataSource() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_dataSource);
	return result != 0 ? new WebDataSource(result) : null;
}

public long /*int*/ globalContext() {
	return OS.objc_msgSend(this.id, OS.sel_globalContext);
}

public void loadHTMLString(NSString string, NSURL URL) {
	OS.objc_msgSend(this.id, OS.sel_loadHTMLString_baseURL_, string != null ? string.id : 0, URL != null ? URL.id : 0);
}

public void loadRequest(NSURLRequest request) {
	OS.objc_msgSend(this.id, OS.sel_loadRequest_, request != null ? request.id : 0);
}

}

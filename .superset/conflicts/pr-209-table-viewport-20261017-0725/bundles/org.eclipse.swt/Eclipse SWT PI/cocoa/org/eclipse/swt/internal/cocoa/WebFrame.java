/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

public class WebFrame extends NSObject {

public WebFrame() {
	super();
}

public WebFrame(long id) {
	super(id);
}

public WebFrame(id id) {
	super(id);
}

public DOMDocument DOMDocument() {
	long result = OS.objc_msgSend(this.id, OS.sel_DOMDocument);
	return result != 0 ? new DOMDocument(result) : null;
}

public WebDataSource dataSource() {
	long result = OS.objc_msgSend(this.id, OS.sel_dataSource);
	return result != 0 ? new WebDataSource(result) : null;
}

public long globalContext() {
	return OS.objc_msgSend(this.id, OS.sel_globalContext);
}

public void loadHTMLString(NSString string, NSURL URL) {
	OS.objc_msgSend(this.id, OS.sel_loadHTMLString_baseURL_, string != null ? string.id : 0, URL != null ? URL.id : 0);
}

public void loadRequest(NSURLRequest request) {
	OS.objc_msgSend(this.id, OS.sel_loadRequest_, request != null ? request.id : 0);
}

}

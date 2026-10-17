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

public class WebDataSource extends NSObject {

public WebDataSource() {
	super();
}

public WebDataSource(long id) {
	super(id);
}

public WebDataSource(id id) {
	super(id);
}

public NSString pageTitle() {
	long result = OS.objc_msgSend(this.id, OS.sel_pageTitle);
	return result != 0 ? new NSString(result) : null;
}

public WebDocumentRepresentation representation() {
	long result = OS.objc_msgSend(this.id, OS.sel_representation);
	return result != 0 ? new WebDocumentRepresentation(result) : null;
}

public NSMutableURLRequest request() {
	long result = OS.objc_msgSend(this.id, OS.sel_request);
	return result != 0 ? new NSMutableURLRequest(result) : null;
}

public WebFrame webFrame() {
	long result = OS.objc_msgSend(this.id, OS.sel_webFrame);
	return result != 0 ? new WebFrame(result) : null;
}

}

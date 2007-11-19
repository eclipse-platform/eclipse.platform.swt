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

public class NSHTTPURLResponse extends NSURLResponse {

public NSHTTPURLResponse() {
	super();
}

public NSHTTPURLResponse(int id) {
	super(id);
}

public NSDictionary allHeaderFields() {
	int result = OS.objc_msgSend(this.id, OS.sel_allHeaderFields);
	return result != 0 ? new NSDictionary(result) : null;
}

public static NSString localizedStringForStatusCode(int statusCode) {
	int result = OS.objc_msgSend(OS.class_NSHTTPURLResponse, OS.sel_localizedStringForStatusCode_1, statusCode);
	return result != 0 ? new NSString(result) : null;
}

public int statusCode() {
	return OS.objc_msgSend(this.id, OS.sel_statusCode);
}

}

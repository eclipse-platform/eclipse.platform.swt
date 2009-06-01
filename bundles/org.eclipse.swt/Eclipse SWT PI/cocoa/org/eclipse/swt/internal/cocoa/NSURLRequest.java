/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSURLRequest extends NSObject {

public NSURLRequest() {
	super();
}

public NSURLRequest(int /*long*/ id) {
	super(id);
}

public NSURLRequest(id id) {
	super(id);
}

public NSURL URL() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_URL);
	return result != 0 ? new NSURL(result) : null;
}

public NSURLRequest initWithURL(NSURL URL) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithURL_, URL != null ? URL.id : 0);
	return result == this.id ? this : (result != 0 ? new NSURLRequest(result) : null);
}

public static NSURLRequest requestWithURL(NSURL URL) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSURLRequest, OS.sel_requestWithURL_, URL != null ? URL.id : 0);
	return result != 0 ? new NSURLRequest(result) : null;
}

}

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

public class NSMutableURLRequest extends NSURLRequest {

public NSMutableURLRequest() {
	super();
}

public NSMutableURLRequest(int /*long*/ id) {
	super(id);
}

public NSMutableURLRequest(id id) {
	super(id);
}

public void setCachePolicy(int /*long*/ policy) {
	OS.objc_msgSend(this.id, OS.sel_setCachePolicy_, policy);
}

public void setURL(NSURL URL) {
	OS.objc_msgSend(this.id, OS.sel_setURL_, URL != null ? URL.id : 0);
}

public static NSURLRequest requestWithURL(NSURL URL) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSMutableURLRequest, OS.sel_requestWithURL_, URL != null ? URL.id : 0);
	return result != 0 ? new NSMutableURLRequest(result) : null;
}

}

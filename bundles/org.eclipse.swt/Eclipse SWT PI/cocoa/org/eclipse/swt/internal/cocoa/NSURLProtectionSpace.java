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

public class NSURLProtectionSpace extends NSObject {

public NSURLProtectionSpace() {
	super();
}

public NSURLProtectionSpace(int /*long*/ id) {
	super(id);
}

public NSURLProtectionSpace(id id) {
	super(id);
}

public NSString host() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_host);
	return result != 0 ? new NSString(result) : null;
}

public int /*long*/ port() {
	return OS.objc_msgSend(this.id, OS.sel_port);
}

public NSString realm() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_realm);
	return result != 0 ? new NSString(result) : null;
}

}

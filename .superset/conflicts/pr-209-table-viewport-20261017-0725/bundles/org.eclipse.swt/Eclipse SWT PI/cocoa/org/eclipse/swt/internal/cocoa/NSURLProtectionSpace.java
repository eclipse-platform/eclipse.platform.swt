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

public class NSURLProtectionSpace extends NSObject {

public NSURLProtectionSpace() {
	super();
}

public NSURLProtectionSpace(long id) {
	super(id);
}

public NSURLProtectionSpace(id id) {
	super(id);
}

public NSString host() {
	long result = OS.objc_msgSend(this.id, OS.sel_host);
	return result != 0 ? new NSString(result) : null;
}

public long port() {
	return OS.objc_msgSend(this.id, OS.sel_port);
}

public NSString realm() {
	long result = OS.objc_msgSend(this.id, OS.sel_realm);
	return result != 0 ? new NSString(result) : null;
}

}

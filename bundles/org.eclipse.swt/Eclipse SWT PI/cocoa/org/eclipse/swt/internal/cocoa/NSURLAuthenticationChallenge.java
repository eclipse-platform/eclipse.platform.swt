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

public class NSURLAuthenticationChallenge extends NSObject {

public NSURLAuthenticationChallenge() {
	super();
}

public NSURLAuthenticationChallenge(long /*int*/ id) {
	super(id);
}

public NSURLAuthenticationChallenge(id id) {
	super(id);
}

public long /*int*/ previousFailureCount() {
	return OS.objc_msgSend(this.id, OS.sel_previousFailureCount);
}

public NSURLCredential proposedCredential() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_proposedCredential);
	return result != 0 ? new NSURLCredential(result) : null;
}

public NSURLProtectionSpace protectionSpace() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_protectionSpace);
	return result != 0 ? new NSURLProtectionSpace(result) : null;
}

public id sender() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_sender);
	return result != 0 ? new id(result) : null;
}

}

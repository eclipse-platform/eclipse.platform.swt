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

public NSURLAuthenticationChallenge(int /*long*/ id) {
	super(id);
}

public NSURLAuthenticationChallenge(id id) {
	super(id);
}

public int /*long*/ previousFailureCount() {
	return OS.objc_msgSend(this.id, OS.sel_previousFailureCount);
}

public NSURLCredential proposedCredential() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_proposedCredential);
	return result != 0 ? new NSURLCredential(result) : null;
}

public NSURLProtectionSpace protectionSpace() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_protectionSpace);
	return result != 0 ? new NSURLProtectionSpace(result) : null;
}

public id sender() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_sender);
	return result != 0 ? new id(result) : null;
}

}

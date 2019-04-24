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

public class NSURLAuthenticationChallenge extends NSObject {

public NSURLAuthenticationChallenge() {
	super();
}

public NSURLAuthenticationChallenge(long id) {
	super(id);
}

public NSURLAuthenticationChallenge(id id) {
	super(id);
}

public long previousFailureCount() {
	return OS.objc_msgSend(this.id, OS.sel_previousFailureCount);
}

public NSURLCredential proposedCredential() {
	long result = OS.objc_msgSend(this.id, OS.sel_proposedCredential);
	return result != 0 ? new NSURLCredential(result) : null;
}

public NSURLProtectionSpace protectionSpace() {
	long result = OS.objc_msgSend(this.id, OS.sel_protectionSpace);
	return result != 0 ? new NSURLProtectionSpace(result) : null;
}

public id sender() {
	long result = OS.objc_msgSend(this.id, OS.sel_sender);
	return result != 0 ? new id(result) : null;
}

}

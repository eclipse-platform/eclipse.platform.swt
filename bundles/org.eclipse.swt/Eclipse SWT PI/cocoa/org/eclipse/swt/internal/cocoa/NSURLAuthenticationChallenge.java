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

public class NSURLAuthenticationChallenge extends NSObject {

public NSURLAuthenticationChallenge() {
	super();
}

public NSURLAuthenticationChallenge(int id) {
	super(id);
}

public NSError error() {
	int result = OS.objc_msgSend(this.id, OS.sel_error);
	return result != 0 ? new NSError(result) : null;
}

public NSURLResponse failureResponse() {
	int result = OS.objc_msgSend(this.id, OS.sel_failureResponse);
	return result != 0 ? new NSURLResponse(result) : null;
}

public id initWithAuthenticationChallenge(NSURLAuthenticationChallenge challenge, id sender) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithAuthenticationChallenge_1sender_1, challenge != null ? challenge.id : 0, sender != null ? sender.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithProtectionSpace(NSURLProtectionSpace space, NSURLCredential credential, int previousFailureCount, NSURLResponse response, NSError error, id sender) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithProtectionSpace_1proposedCredential_1previousFailureCount_1failureResponse_1error_1sender_1, space != null ? space.id : 0, credential != null ? credential.id : 0, previousFailureCount, response != null ? response.id : 0, error != null ? error.id : 0, sender != null ? sender.id : 0);
	return result != 0 ? new id(result) : null;
}

public int previousFailureCount() {
	return OS.objc_msgSend(this.id, OS.sel_previousFailureCount);
}

public NSURLCredential proposedCredential() {
	int result = OS.objc_msgSend(this.id, OS.sel_proposedCredential);
	return result != 0 ? new NSURLCredential(result) : null;
}

public NSURLProtectionSpace protectionSpace() {
	int result = OS.objc_msgSend(this.id, OS.sel_protectionSpace);
	return result != 0 ? new NSURLProtectionSpace(result) : null;
}

public id sender() {
	int result = OS.objc_msgSend(this.id, OS.sel_sender);
	return result != 0 ? new id(result) : null;
}

}

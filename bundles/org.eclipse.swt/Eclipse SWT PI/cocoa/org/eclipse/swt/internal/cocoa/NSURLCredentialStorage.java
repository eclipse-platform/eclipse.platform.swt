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

public class NSURLCredentialStorage extends NSObject {

public NSURLCredentialStorage() {
	super();
}

public NSURLCredentialStorage(int id) {
	super(id);
}

public NSDictionary allCredentials() {
	int result = OS.objc_msgSend(this.id, OS.sel_allCredentials);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary credentialsForProtectionSpace(NSURLProtectionSpace space) {
	int result = OS.objc_msgSend(this.id, OS.sel_credentialsForProtectionSpace_1, space != null ? space.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSURLCredential defaultCredentialForProtectionSpace(NSURLProtectionSpace space) {
	int result = OS.objc_msgSend(this.id, OS.sel_defaultCredentialForProtectionSpace_1, space != null ? space.id : 0);
	return result != 0 ? new NSURLCredential(result) : null;
}

public void removeCredential(NSURLCredential credential, NSURLProtectionSpace space) {
	OS.objc_msgSend(this.id, OS.sel_removeCredential_1forProtectionSpace_1, credential != null ? credential.id : 0, space != null ? space.id : 0);
}

public void setCredential(NSURLCredential credential, NSURLProtectionSpace space) {
	OS.objc_msgSend(this.id, OS.sel_setCredential_1forProtectionSpace_1, credential != null ? credential.id : 0, space != null ? space.id : 0);
}

public void setDefaultCredential(NSURLCredential credential, NSURLProtectionSpace space) {
	OS.objc_msgSend(this.id, OS.sel_setDefaultCredential_1forProtectionSpace_1, credential != null ? credential.id : 0, space != null ? space.id : 0);
}

public static NSURLCredentialStorage sharedCredentialStorage() {
	int result = OS.objc_msgSend(OS.class_NSURLCredentialStorage, OS.sel_sharedCredentialStorage);
	return result != 0 ? new NSURLCredentialStorage(result) : null;
}

}

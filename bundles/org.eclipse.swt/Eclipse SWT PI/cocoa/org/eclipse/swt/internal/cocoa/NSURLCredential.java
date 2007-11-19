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

public class NSURLCredential extends NSObject {

public NSURLCredential() {
	super();
}

public NSURLCredential(int id) {
	super(id);
}

public static NSURLCredential credentialWithUser(NSString user, NSString password, int persistence) {
	int result = OS.objc_msgSend(OS.class_NSURLCredential, OS.sel_credentialWithUser_1password_1persistence_1, user != null ? user.id : 0, password != null ? password.id : 0, persistence);
	return result != 0 ? new NSURLCredential(result) : null;
}

public boolean hasPassword() {
	return OS.objc_msgSend(this.id, OS.sel_hasPassword) != 0;
}

public id initWithUser(NSString user, NSString password, int persistence) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithUser_1password_1persistence_1, user != null ? user.id : 0, password != null ? password.id : 0, persistence);
	return result != 0 ? new id(result) : null;
}

public NSString password() {
	int result = OS.objc_msgSend(this.id, OS.sel_password);
	return result != 0 ? new NSString(result) : null;
}

public int persistence() {
	return OS.objc_msgSend(this.id, OS.sel_persistence);
}

public NSString user() {
	int result = OS.objc_msgSend(this.id, OS.sel_user);
	return result != 0 ? new NSString(result) : null;
}

}

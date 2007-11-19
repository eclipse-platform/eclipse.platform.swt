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

public class NSHost extends NSObject {

public NSHost() {
	super();
}

public NSHost(int id) {
	super(id);
}

public NSString address() {
	int result = OS.objc_msgSend(this.id, OS.sel_address);
	return result != 0 ? new NSString(result) : null;
}

public NSArray addresses() {
	int result = OS.objc_msgSend(this.id, OS.sel_addresses);
	return result != 0 ? new NSArray(result) : null;
}

public static NSHost currentHost() {
	int result = OS.objc_msgSend(OS.class_NSHost, OS.sel_currentHost);
	return result != 0 ? new NSHost(result) : null;
}

public static void flushHostCache() {
	OS.objc_msgSend(OS.class_NSHost, OS.sel_flushHostCache);
}

public static NSHost hostWithAddress(NSString address) {
	int result = OS.objc_msgSend(OS.class_NSHost, OS.sel_hostWithAddress_1, address != null ? address.id : 0);
	return result != 0 ? new NSHost(result) : null;
}

public static NSHost hostWithName(NSString name) {
	int result = OS.objc_msgSend(OS.class_NSHost, OS.sel_hostWithName_1, name != null ? name.id : 0);
	return result != 0 ? new NSHost(result) : null;
}

public boolean isEqualToHost(NSHost aHost) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToHost_1, aHost != null ? aHost.id : 0) != 0;
}

public static boolean isHostCacheEnabled() {
	return OS.objc_msgSend(OS.class_NSHost, OS.sel_isHostCacheEnabled) != 0;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public NSArray names() {
	int result = OS.objc_msgSend(this.id, OS.sel_names);
	return result != 0 ? new NSArray(result) : null;
}

public static void setHostCacheEnabled(boolean flag) {
	OS.objc_msgSend(OS.class_NSHost, OS.sel_setHostCacheEnabled_1, flag);
}

}

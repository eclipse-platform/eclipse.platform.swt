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

public class NSMachBootstrapServer extends NSPortNameServer {

public NSMachBootstrapServer() {
	super();
}

public NSMachBootstrapServer(int id) {
	super(id);
}

public NSPort portForName_(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_portForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSPort(result) : null;
}

public NSPort portForName_host_(NSString name, NSString host) {
	int result = OS.objc_msgSend(this.id, OS.sel_portForName_1host_1, name != null ? name.id : 0, host != null ? host.id : 0);
	return result != 0 ? new NSPort(result) : null;
}

public boolean registerPort(NSPort port, NSString name) {
	return OS.objc_msgSend(this.id, OS.sel_registerPort_1name_1, port != null ? port.id : 0, name != null ? name.id : 0) != 0;
}

public NSPort servicePortWithName(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_servicePortWithName_1, name != null ? name.id : 0);
	return result != 0 ? new NSPort(result) : null;
}

public static id sharedInstance() {
	int result = OS.objc_msgSend(OS.class_NSMachBootstrapServer, OS.sel_sharedInstance);
	return result != 0 ? new id(result) : null;
}

}

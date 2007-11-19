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

public class NSURLProtectionSpace extends NSObject {

public NSURLProtectionSpace() {
	super();
}

public NSURLProtectionSpace(int id) {
	super(id);
}

public NSString authenticationMethod() {
	int result = OS.objc_msgSend(this.id, OS.sel_authenticationMethod);
	return result != 0 ? new NSString(result) : null;
}

public NSString host() {
	int result = OS.objc_msgSend(this.id, OS.sel_host);
	return result != 0 ? new NSString(result) : null;
}

public id initWithHost(NSString host, int port, NSString protocol, NSString realm, NSString authenticationMethod) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithHost_1port_1protocol_1realm_1authenticationMethod_1, host != null ? host.id : 0, port, protocol != null ? protocol.id : 0, realm != null ? realm.id : 0, authenticationMethod != null ? authenticationMethod.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithProxyHost(NSString host, int port, NSString type, NSString realm, NSString authenticationMethod) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithProxyHost_1port_1type_1realm_1authenticationMethod_1, host != null ? host.id : 0, port, type != null ? type.id : 0, realm != null ? realm.id : 0, authenticationMethod != null ? authenticationMethod.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isProxy() {
	return OS.objc_msgSend(this.id, OS.sel_isProxy) != 0;
}

public int port() {
	return OS.objc_msgSend(this.id, OS.sel_port);
}

public NSString protocol() {
	int result = OS.objc_msgSend(this.id, OS.sel_protocol);
	return result != 0 ? new NSString(result) : null;
}

public NSString proxyType() {
	int result = OS.objc_msgSend(this.id, OS.sel_proxyType);
	return result != 0 ? new NSString(result) : null;
}

public NSString realm() {
	int result = OS.objc_msgSend(this.id, OS.sel_realm);
	return result != 0 ? new NSString(result) : null;
}

public boolean receivesCredentialSecurely() {
	return OS.objc_msgSend(this.id, OS.sel_receivesCredentialSecurely) != 0;
}

}

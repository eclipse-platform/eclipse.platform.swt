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

public class NSSocketPort extends NSPort {

public NSSocketPort() {
	super();
}

public NSSocketPort(int id) {
	super(id);
}

public NSData address() {
	int result = OS.objc_msgSend(this.id, OS.sel_address);
	return result != 0 ? new NSData(result) : null;
}

public id initRemoteWithProtocolFamily(int family, int type, int protocol, NSData address) {
	int result = OS.objc_msgSend(this.id, OS.sel_initRemoteWithProtocolFamily_1socketType_1protocol_1address_1, family, type, protocol, address != null ? address.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initRemoteWithTCPPort(short port, NSString hostName) {
	int result = OS.objc_msgSend(this.id, OS.sel_initRemoteWithTCPPort_1host_1, port, hostName != null ? hostName.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithProtocolFamily_socketType_protocol_address_(int family, int type, int protocol, NSData address) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithProtocolFamily_1socketType_1protocol_1address_1, family, type, protocol, address != null ? address.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithProtocolFamily_socketType_protocol_socket_(int family, int type, int protocol, int sock) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithProtocolFamily_1socketType_1protocol_1socket_1, family, type, protocol, sock);
	return result != 0 ? new id(result) : null;
}

public id initWithTCPPort(short port) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithTCPPort_1, port);
	return result != 0 ? new id(result) : null;
}

public int protocol() {
	return OS.objc_msgSend(this.id, OS.sel_protocol);
}

public int protocolFamily() {
	return OS.objc_msgSend(this.id, OS.sel_protocolFamily);
}

public int socket() {
	return OS.objc_msgSend(this.id, OS.sel_socket);
}

public int socketType() {
	return OS.objc_msgSend(this.id, OS.sel_socketType);
}

}

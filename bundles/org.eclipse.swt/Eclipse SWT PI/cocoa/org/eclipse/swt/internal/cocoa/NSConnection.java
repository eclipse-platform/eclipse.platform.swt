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

public class NSConnection extends NSObject {

public NSConnection() {
	super();
}

public NSConnection(int id) {
	super(id);
}

public void addRequestMode(NSString rmode) {
	OS.objc_msgSend(this.id, OS.sel_addRequestMode_1, rmode != null ? rmode.id : 0);
}

public void addRunLoop(NSRunLoop runloop) {
	OS.objc_msgSend(this.id, OS.sel_addRunLoop_1, runloop != null ? runloop.id : 0);
}

public static NSArray allConnections() {
	int result = OS.objc_msgSend(OS.class_NSConnection, OS.sel_allConnections);
	return result != 0 ? new NSArray(result) : null;
}

public static id connectionWithReceivePort(NSPort receivePort, NSPort sendPort) {
	int result = OS.objc_msgSend(OS.class_NSConnection, OS.sel_connectionWithReceivePort_1sendPort_1, receivePort != null ? receivePort.id : 0, sendPort != null ? sendPort.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_connectionWithRegisteredName_host_(NSString name, NSString hostName) {
	int result = OS.objc_msgSend(OS.class_NSConnection, OS.sel_connectionWithRegisteredName_1host_1, name != null ? name.id : 0, hostName != null ? hostName.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_connectionWithRegisteredName_host_usingNameServer_(NSString name, NSString hostName, NSPortNameServer server) {
	int result = OS.objc_msgSend(OS.class_NSConnection, OS.sel_connectionWithRegisteredName_1host_1usingNameServer_1, name != null ? name.id : 0, hostName != null ? hostName.id : 0, server != null ? server.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id currentConversation() {
	int result = OS.objc_msgSend(OS.class_NSConnection, OS.sel_currentConversation);
	return result != 0 ? new id(result) : null;
}

public static NSConnection defaultConnection() {
	int result = OS.objc_msgSend(OS.class_NSConnection, OS.sel_defaultConnection);
	return result != 0 ? new NSConnection(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void enableMultipleThreads() {
	OS.objc_msgSend(this.id, OS.sel_enableMultipleThreads);
}

public boolean independentConversationQueueing() {
	return OS.objc_msgSend(this.id, OS.sel_independentConversationQueueing) != 0;
}

public id initWithReceivePort(NSPort receivePort, NSPort sendPort) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithReceivePort_1sendPort_1, receivePort != null ? receivePort.id : 0, sendPort != null ? sendPort.id : 0);
	return result != 0 ? new id(result) : null;
}

public void invalidate() {
	OS.objc_msgSend(this.id, OS.sel_invalidate);
}

public boolean isValid() {
	return OS.objc_msgSend(this.id, OS.sel_isValid) != 0;
}

public NSArray localObjects() {
	int result = OS.objc_msgSend(this.id, OS.sel_localObjects);
	return result != 0 ? new NSArray(result) : null;
}

public boolean multipleThreadsEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_multipleThreadsEnabled) != 0;
}

public NSPort receivePort() {
	int result = OS.objc_msgSend(this.id, OS.sel_receivePort);
	return result != 0 ? new NSPort(result) : null;
}

public boolean registerName_(NSString name) {
	return OS.objc_msgSend(this.id, OS.sel_registerName_1, name != null ? name.id : 0) != 0;
}

public boolean registerName_withNameServer_(NSString name, NSPortNameServer server) {
	return OS.objc_msgSend(this.id, OS.sel_registerName_1withNameServer_1, name != null ? name.id : 0, server != null ? server.id : 0) != 0;
}

public NSArray remoteObjects() {
	int result = OS.objc_msgSend(this.id, OS.sel_remoteObjects);
	return result != 0 ? new NSArray(result) : null;
}

public void removeRequestMode(NSString rmode) {
	OS.objc_msgSend(this.id, OS.sel_removeRequestMode_1, rmode != null ? rmode.id : 0);
}

public void removeRunLoop(NSRunLoop runloop) {
	OS.objc_msgSend(this.id, OS.sel_removeRunLoop_1, runloop != null ? runloop.id : 0);
}

public double replyTimeout() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_replyTimeout);
}

public NSArray requestModes() {
	int result = OS.objc_msgSend(this.id, OS.sel_requestModes);
	return result != 0 ? new NSArray(result) : null;
}

public double requestTimeout() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_requestTimeout);
}

public id rootObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_rootObject);
	return result != 0 ? new id(result) : null;
}

public NSDistantObject rootProxy() {
	int result = OS.objc_msgSend(this.id, OS.sel_rootProxy);
	return result != 0 ? new NSDistantObject(result) : null;
}

public static NSDistantObject static_rootProxyForConnectionWithRegisteredName_host_(NSString name, NSString hostName) {
	int result = OS.objc_msgSend(OS.class_NSConnection, OS.sel_rootProxyForConnectionWithRegisteredName_1host_1, name != null ? name.id : 0, hostName != null ? hostName.id : 0);
	return result != 0 ? new NSDistantObject(result) : null;
}

public static NSDistantObject static_rootProxyForConnectionWithRegisteredName_host_usingNameServer_(NSString name, NSString hostName, NSPortNameServer server) {
	int result = OS.objc_msgSend(OS.class_NSConnection, OS.sel_rootProxyForConnectionWithRegisteredName_1host_1usingNameServer_1, name != null ? name.id : 0, hostName != null ? hostName.id : 0, server != null ? server.id : 0);
	return result != 0 ? new NSDistantObject(result) : null;
}

public void runInNewThread() {
	OS.objc_msgSend(this.id, OS.sel_runInNewThread);
}

public NSPort sendPort() {
	int result = OS.objc_msgSend(this.id, OS.sel_sendPort);
	return result != 0 ? new NSPort(result) : null;
}

public static id static_serviceConnectionWithName_rootObject_(NSString name, id root) {
	int result = OS.objc_msgSend(OS.class_NSConnection, OS.sel_serviceConnectionWithName_1rootObject_1, name != null ? name.id : 0, root != null ? root.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_serviceConnectionWithName_rootObject_usingNameServer_(NSString name, id root, NSPortNameServer server) {
	int result = OS.objc_msgSend(OS.class_NSConnection, OS.sel_serviceConnectionWithName_1rootObject_1usingNameServer_1, name != null ? name.id : 0, root != null ? root.id : 0, server != null ? server.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setIndependentConversationQueueing(boolean yorn) {
	OS.objc_msgSend(this.id, OS.sel_setIndependentConversationQueueing_1, yorn);
}

public void setReplyTimeout(double ti) {
	OS.objc_msgSend(this.id, OS.sel_setReplyTimeout_1, ti);
}

public void setRequestTimeout(double ti) {
	OS.objc_msgSend(this.id, OS.sel_setRequestTimeout_1, ti);
}

public void setRootObject(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setRootObject_1, anObject != null ? anObject.id : 0);
}

public NSDictionary statistics() {
	int result = OS.objc_msgSend(this.id, OS.sel_statistics);
	return result != 0 ? new NSDictionary(result) : null;
}

}

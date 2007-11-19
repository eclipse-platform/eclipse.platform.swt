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

public class NSNetService extends NSObject {

public NSNetService() {
	super();
}

public NSNetService(int id) {
	super(id);
}

public NSData TXTRecordData() {
	int result = OS.objc_msgSend(this.id, OS.sel_TXTRecordData);
	return result != 0 ? new NSData(result) : null;
}

public NSArray addresses() {
	int result = OS.objc_msgSend(this.id, OS.sel_addresses);
	return result != 0 ? new NSArray(result) : null;
}

public static NSData dataFromTXTRecordDictionary(NSDictionary txtDictionary) {
	int result = OS.objc_msgSend(OS.class_NSNetService, OS.sel_dataFromTXTRecordDictionary_1, txtDictionary != null ? txtDictionary.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public static NSDictionary dictionaryFromTXTRecordData(NSData txtData) {
	int result = OS.objc_msgSend(OS.class_NSNetService, OS.sel_dictionaryFromTXTRecordData_1, txtData != null ? txtData.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSString domain() {
	int result = OS.objc_msgSend(this.id, OS.sel_domain);
	return result != 0 ? new NSString(result) : null;
}

public boolean getInputStream(int inputStream, int outputStream) {
	return OS.objc_msgSend(this.id, OS.sel_getInputStream_1outputStream_1, inputStream, outputStream) != 0;
}

public NSString hostName() {
	int result = OS.objc_msgSend(this.id, OS.sel_hostName);
	return result != 0 ? new NSString(result) : null;
}

public id initWithDomain_type_name_(NSString domain, NSString type, NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDomain_1type_1name_1, domain != null ? domain.id : 0, type != null ? type.id : 0, name != null ? name.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithDomain_type_name_port_(NSString domain, NSString type, NSString name, int port) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDomain_1type_1name_1port_1, domain != null ? domain.id : 0, type != null ? type.id : 0, name != null ? name.id : 0, port);
	return result != 0 ? new id(result) : null;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public int port() {
	return OS.objc_msgSend(this.id, OS.sel_port);
}

public NSString protocolSpecificInformation() {
	int result = OS.objc_msgSend(this.id, OS.sel_protocolSpecificInformation);
	return result != 0 ? new NSString(result) : null;
}

public void publish() {
	OS.objc_msgSend(this.id, OS.sel_publish);
}

public void publishWithOptions(int options) {
	OS.objc_msgSend(this.id, OS.sel_publishWithOptions_1, options);
}

public void removeFromRunLoop(NSRunLoop aRunLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_removeFromRunLoop_1forMode_1, aRunLoop != null ? aRunLoop.id : 0, mode != null ? mode.id : 0);
}

public void resolve() {
	OS.objc_msgSend(this.id, OS.sel_resolve);
}

public void resolveWithTimeout(double timeout) {
	OS.objc_msgSend(this.id, OS.sel_resolveWithTimeout_1, timeout);
}

public void scheduleInRunLoop(NSRunLoop aRunLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_scheduleInRunLoop_1forMode_1, aRunLoop != null ? aRunLoop.id : 0, mode != null ? mode.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setProtocolSpecificInformation(NSString specificInformation) {
	OS.objc_msgSend(this.id, OS.sel_setProtocolSpecificInformation_1, specificInformation != null ? specificInformation.id : 0);
}

public boolean setTXTRecordData(NSData recordData) {
	return OS.objc_msgSend(this.id, OS.sel_setTXTRecordData_1, recordData != null ? recordData.id : 0) != 0;
}

public void startMonitoring() {
	OS.objc_msgSend(this.id, OS.sel_startMonitoring);
}

public void stop() {
	OS.objc_msgSend(this.id, OS.sel_stop);
}

public void stopMonitoring() {
	OS.objc_msgSend(this.id, OS.sel_stopMonitoring);
}

public NSString type() {
	int result = OS.objc_msgSend(this.id, OS.sel_type);
	return result != 0 ? new NSString(result) : null;
}

}

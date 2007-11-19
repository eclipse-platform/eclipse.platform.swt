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

public class NSPortMessage extends NSObject {

public NSPortMessage() {
	super();
}

public NSPortMessage(int id) {
	super(id);
}

public NSArray components() {
	int result = OS.objc_msgSend(this.id, OS.sel_components);
	return result != 0 ? new NSArray(result) : null;
}

public NSPortMessage initWithSendPort(NSPort sendPort, NSPort replyPort, NSArray components) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithSendPort_1receivePort_1components_1, sendPort != null ? sendPort.id : 0, replyPort != null ? replyPort.id : 0, components != null ? components.id : 0);
	return result != 0 ? this : null;
}

public int msgid() {
	return OS.objc_msgSend(this.id, OS.sel_msgid);
}

public NSPort receivePort() {
	int result = OS.objc_msgSend(this.id, OS.sel_receivePort);
	return result != 0 ? new NSPort(result) : null;
}

public boolean sendBeforeDate(NSDate date) {
	return OS.objc_msgSend(this.id, OS.sel_sendBeforeDate_1, date != null ? date.id : 0) != 0;
}

public NSPort sendPort() {
	int result = OS.objc_msgSend(this.id, OS.sel_sendPort);
	return result != 0 ? new NSPort(result) : null;
}

public void setMsgid(int msgid) {
	OS.objc_msgSend(this.id, OS.sel_setMsgid_1, msgid);
}

}

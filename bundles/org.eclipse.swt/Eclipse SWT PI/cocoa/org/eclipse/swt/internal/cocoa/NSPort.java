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

public class NSPort extends NSObject {

public NSPort() {
	super();
}

public NSPort(int id) {
	super(id);
}

public void addConnection(NSConnection conn, NSRunLoop runLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_addConnection_1toRunLoop_1forMode_1, conn != null ? conn.id : 0, runLoop != null ? runLoop.id : 0, mode != null ? mode.id : 0);
}

public static id allocWithZone(int zone) {
	int result = OS.objc_msgSend(OS.class_NSPort, OS.sel_allocWithZone_1, zone);
	return result != 0 ? new id(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void invalidate() {
	OS.objc_msgSend(this.id, OS.sel_invalidate);
}

public boolean isValid() {
	return OS.objc_msgSend(this.id, OS.sel_isValid) != 0;
}

public static NSPort port() {
	int result = OS.objc_msgSend(OS.class_NSPort, OS.sel_port);
	return result != 0 ? new NSPort(result) : null;
}

public void removeConnection(NSConnection conn, NSRunLoop runLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_removeConnection_1fromRunLoop_1forMode_1, conn != null ? conn.id : 0, runLoop != null ? runLoop.id : 0, mode != null ? mode.id : 0);
}

public void removeFromRunLoop(NSRunLoop runLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_removeFromRunLoop_1forMode_1, runLoop != null ? runLoop.id : 0, mode != null ? mode.id : 0);
}

public int reservedSpaceLength() {
	return OS.objc_msgSend(this.id, OS.sel_reservedSpaceLength);
}

public void scheduleInRunLoop(NSRunLoop runLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_scheduleInRunLoop_1forMode_1, runLoop != null ? runLoop.id : 0, mode != null ? mode.id : 0);
}

public boolean sendBeforeDate_components_from_reserved_(NSDate limitDate, NSMutableArray components, NSPort receivePort, int headerSpaceReserved) {
	return OS.objc_msgSend(this.id, OS.sel_sendBeforeDate_1components_1from_1reserved_1, limitDate != null ? limitDate.id : 0, components != null ? components.id : 0, receivePort != null ? receivePort.id : 0, headerSpaceReserved) != 0;
}

public boolean sendBeforeDate_msgid_components_from_reserved_(NSDate limitDate, int msgID, NSMutableArray components, NSPort receivePort, int headerSpaceReserved) {
	return OS.objc_msgSend(this.id, OS.sel_sendBeforeDate_1msgid_1components_1from_1reserved_1, limitDate != null ? limitDate.id : 0, msgID, components != null ? components.id : 0, receivePort != null ? receivePort.id : 0, headerSpaceReserved) != 0;
}

public void setDelegate(id anId) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anId != null ? anId.id : 0);
}

}

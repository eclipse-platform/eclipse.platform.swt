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

public class NSAppleEventManager extends NSObject {

public NSAppleEventManager() {
	super();
}

public NSAppleEventManager(int id) {
	super(id);
}

public NSAppleEventDescriptor appleEventForSuspensionID(int suspensionID) {
	int result = OS.objc_msgSend(this.id, OS.sel_appleEventForSuspensionID_1, suspensionID);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public NSAppleEventDescriptor currentAppleEvent() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentAppleEvent);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public NSAppleEventDescriptor currentReplyAppleEvent() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentReplyAppleEvent);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public short dispatchRawAppleEvent(int theAppleEvent, int theReply, int handlerRefCon) {
	return (short)OS.objc_msgSend(this.id, OS.sel_dispatchRawAppleEvent_1withRawReply_1handlerRefCon_1, theAppleEvent, theReply, handlerRefCon);
}

public void removeEventHandlerForEventClass(int eventClass, int eventID) {
	OS.objc_msgSend(this.id, OS.sel_removeEventHandlerForEventClass_1andEventID_1, eventClass, eventID);
}

public NSAppleEventDescriptor replyAppleEventForSuspensionID(int suspensionID) {
	int result = OS.objc_msgSend(this.id, OS.sel_replyAppleEventForSuspensionID_1, suspensionID);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public void resumeWithSuspensionID(int suspensionID) {
	OS.objc_msgSend(this.id, OS.sel_resumeWithSuspensionID_1, suspensionID);
}

public void setCurrentAppleEventAndReplyEventWithSuspensionID(int suspensionID) {
	OS.objc_msgSend(this.id, OS.sel_setCurrentAppleEventAndReplyEventWithSuspensionID_1, suspensionID);
}

public void setEventHandler(id handler, int handleEventSelector, int eventClass, int eventID) {
	OS.objc_msgSend(this.id, OS.sel_setEventHandler_1andSelector_1forEventClass_1andEventID_1, handler != null ? handler.id : 0, handleEventSelector, eventClass, eventID);
}

public static NSAppleEventManager sharedAppleEventManager() {
	int result = OS.objc_msgSend(OS.class_NSAppleEventManager, OS.sel_sharedAppleEventManager);
	return result != 0 ? new NSAppleEventManager(result) : null;
}

public int suspendCurrentAppleEvent() {
	return OS.objc_msgSend(this.id, OS.sel_suspendCurrentAppleEvent);
}

}

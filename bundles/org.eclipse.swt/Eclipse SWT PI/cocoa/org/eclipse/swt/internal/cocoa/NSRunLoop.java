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

public class NSRunLoop extends NSObject {

public NSRunLoop() {
	super();
}

public NSRunLoop(int id) {
	super(id);
}

public void acceptInputForMode(NSString mode, NSDate limitDate) {
	OS.objc_msgSend(this.id, OS.sel_acceptInputForMode_1beforeDate_1, mode != null ? mode.id : 0, limitDate != null ? limitDate.id : 0);
}

public void addPort(NSPort aPort, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_addPort_1forMode_1, aPort != null ? aPort.id : 0, mode != null ? mode.id : 0);
}

public void addTimer(NSTimer timer, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_addTimer_1forMode_1, timer != null ? timer.id : 0, mode != null ? mode.id : 0);
}

public void cancelPerformSelector(int aSelector, id target, id arg) {
	OS.objc_msgSend(this.id, OS.sel_cancelPerformSelector_1target_1argument_1, aSelector, target != null ? target.id : 0, arg != null ? arg.id : 0);
}

public void cancelPerformSelectorsWithTarget(id target) {
	OS.objc_msgSend(this.id, OS.sel_cancelPerformSelectorsWithTarget_1, target != null ? target.id : 0);
}

public void configureAsServer() {
	OS.objc_msgSend(this.id, OS.sel_configureAsServer);
}

public NSString currentMode() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentMode);
	return result != 0 ? new NSString(result) : null;
}

public static NSRunLoop currentRunLoop() {
	int result = OS.objc_msgSend(OS.class_NSRunLoop, OS.sel_currentRunLoop);
	return result != 0 ? new NSRunLoop(result) : null;
}

public int getCFRunLoop() {
	return OS.objc_msgSend(this.id, OS.sel_getCFRunLoop);
}

public NSDate limitDateForMode(NSString mode) {
	int result = OS.objc_msgSend(this.id, OS.sel_limitDateForMode_1, mode != null ? mode.id : 0);
	return result != 0 ? new NSDate(result) : null;
}

public static NSRunLoop mainRunLoop() {
	int result = OS.objc_msgSend(OS.class_NSRunLoop, OS.sel_mainRunLoop);
	return result != 0 ? new NSRunLoop(result) : null;
}

public void performSelector(int aSelector, id target, id arg, int order, NSArray modes) {
	OS.objc_msgSend(this.id, OS.sel_performSelector_1target_1argument_1order_1modes_1, aSelector, target != null ? target.id : 0, arg != null ? arg.id : 0, order, modes != null ? modes.id : 0);
}

public void removePort(NSPort aPort, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_removePort_1forMode_1, aPort != null ? aPort.id : 0, mode != null ? mode.id : 0);
}

public void run() {
	OS.objc_msgSend(this.id, OS.sel_run);
}

public boolean runMode(NSString mode, NSDate limitDate) {
	return OS.objc_msgSend(this.id, OS.sel_runMode_1beforeDate_1, mode != null ? mode.id : 0, limitDate != null ? limitDate.id : 0) != 0;
}

public void runUntilDate(NSDate limitDate) {
	OS.objc_msgSend(this.id, OS.sel_runUntilDate_1, limitDate != null ? limitDate.id : 0);
}

}

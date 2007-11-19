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

public class NSThread extends NSObject {

public NSThread() {
	super();
}

public NSThread(int id) {
	super(id);
}

public static NSArray callStackReturnAddresses() {
	int result = OS.objc_msgSend(OS.class_NSThread, OS.sel_callStackReturnAddresses);
	return result != 0 ? new NSArray(result) : null;
}

public void cancel() {
	OS.objc_msgSend(this.id, OS.sel_cancel);
}

public static NSThread currentThread() {
	int result = OS.objc_msgSend(OS.class_NSThread, OS.sel_currentThread);
	return result != 0 ? new NSThread(result) : null;
}

public static void detachNewThreadSelector(int selector, id target, id argument) {
	OS.objc_msgSend(OS.class_NSThread, OS.sel_detachNewThreadSelector_1toTarget_1withObject_1, selector, target != null ? target.id : 0, argument != null ? argument.id : 0);
}

public static void exit() {
	OS.objc_msgSend(OS.class_NSThread, OS.sel_exit);
}

public id initWithTarget(id target, int selector, id argument) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithTarget_1selector_1object_1, target != null ? target.id : 0, selector, argument != null ? argument.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isCancelled() {
	return OS.objc_msgSend(this.id, OS.sel_isCancelled) != 0;
}

public boolean isExecuting() {
	return OS.objc_msgSend(this.id, OS.sel_isExecuting) != 0;
}

public boolean isFinished() {
	return OS.objc_msgSend(this.id, OS.sel_isFinished) != 0;
}

public static boolean static_isMainThread() {
	return OS.objc_msgSend(OS.class_NSThread, OS.sel_isMainThread) != 0;
}

public boolean isMainThread() {
	return OS.objc_msgSend(this.id, OS.sel_isMainThread) != 0;
}

public static boolean isMultiThreaded() {
	return OS.objc_msgSend(OS.class_NSThread, OS.sel_isMultiThreaded) != 0;
}

public void main() {
	OS.objc_msgSend(this.id, OS.sel_main);
}

public static NSThread mainThread() {
	int result = OS.objc_msgSend(OS.class_NSThread, OS.sel_mainThread);
	return result != 0 ? new NSThread(result) : null;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public void setName(NSString n) {
	OS.objc_msgSend(this.id, OS.sel_setName_1, n != null ? n.id : 0);
}

public void setStackSize(int s) {
	OS.objc_msgSend(this.id, OS.sel_setStackSize_1, s);
}

public static boolean setThreadPriority(double p) {
	return OS.objc_msgSend(OS.class_NSThread, OS.sel_setThreadPriority_1, p) != 0;
}

public static void sleepForTimeInterval(double ti) {
	OS.objc_msgSend(OS.class_NSThread, OS.sel_sleepForTimeInterval_1, ti);
}

public static void sleepUntilDate(NSDate date) {
	OS.objc_msgSend(OS.class_NSThread, OS.sel_sleepUntilDate_1, date != null ? date.id : 0);
}

public int stackSize() {
	return OS.objc_msgSend(this.id, OS.sel_stackSize);
}

public void start() {
	OS.objc_msgSend(this.id, OS.sel_start);
}

public NSMutableDictionary threadDictionary() {
	int result = OS.objc_msgSend(this.id, OS.sel_threadDictionary);
	return result != 0 ? new NSMutableDictionary(result) : null;
}

public static double threadPriority() {
	return OS.objc_msgSend_fpret(OS.class_NSThread, OS.sel_threadPriority);
}

}

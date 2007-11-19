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

public class NSURLConnection extends NSObject {

public NSURLConnection() {
	super();
}

public NSURLConnection(int id) {
	super(id);
}

public static boolean canHandleRequest(NSURLRequest request) {
	return OS.objc_msgSend(OS.class_NSURLConnection, OS.sel_canHandleRequest_1, request != null ? request.id : 0) != 0;
}

public void cancel() {
	OS.objc_msgSend(this.id, OS.sel_cancel);
}

public static NSURLConnection connectionWithRequest(NSURLRequest request, id delegate) {
	int result = OS.objc_msgSend(OS.class_NSURLConnection, OS.sel_connectionWithRequest_1delegate_1, request != null ? request.id : 0, delegate != null ? delegate.id : 0);
	return result != 0 ? new NSURLConnection(result) : null;
}

public id initWithRequest_delegate_(NSURLRequest request, id delegate) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithRequest_1delegate_1, request != null ? request.id : 0, delegate != null ? delegate.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithRequest_delegate_startImmediately_(NSURLRequest request, id delegate, boolean startImmediately) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithRequest_1delegate_1startImmediately_1, request != null ? request.id : 0, delegate != null ? delegate.id : 0, startImmediately);
	return result != 0 ? new id(result) : null;
}

public void scheduleInRunLoop(NSRunLoop aRunLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_scheduleInRunLoop_1forMode_1, aRunLoop != null ? aRunLoop.id : 0, mode != null ? mode.id : 0);
}

public static NSData sendSynchronousRequest(NSURLRequest request, int response, int error) {
	int result = OS.objc_msgSend(OS.class_NSURLConnection, OS.sel_sendSynchronousRequest_1returningResponse_1error_1, request != null ? request.id : 0, response, error);
	return result != 0 ? new NSData(result) : null;
}

public void start() {
	OS.objc_msgSend(this.id, OS.sel_start);
}

public void unscheduleFromRunLoop(NSRunLoop aRunLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_unscheduleFromRunLoop_1forMode_1, aRunLoop != null ? aRunLoop.id : 0, mode != null ? mode.id : 0);
}

}

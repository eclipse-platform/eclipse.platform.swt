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

public class NSStream extends NSObject {

public NSStream() {
	super();
}

public NSStream(int id) {
	super(id);
}

public void close() {
	OS.objc_msgSend(this.id, OS.sel_close);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public static void getStreamsToHost(NSHost host, int port, int inputStream, int outputStream) {
	OS.objc_msgSend(OS.class_NSStream, OS.sel_getStreamsToHost_1port_1inputStream_1outputStream_1, host != null ? host.id : 0, port, inputStream, outputStream);
}

public void open() {
	OS.objc_msgSend(this.id, OS.sel_open);
}

public id propertyForKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_propertyForKey_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public void removeFromRunLoop(NSRunLoop aRunLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_removeFromRunLoop_1forMode_1, aRunLoop != null ? aRunLoop.id : 0, mode != null ? mode.id : 0);
}

public void scheduleInRunLoop(NSRunLoop aRunLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_scheduleInRunLoop_1forMode_1, aRunLoop != null ? aRunLoop.id : 0, mode != null ? mode.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public boolean setProperty(id property, NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_setProperty_1forKey_1, property != null ? property.id : 0, key != null ? key.id : 0) != 0;
}

public NSError streamError() {
	int result = OS.objc_msgSend(this.id, OS.sel_streamError);
	return result != 0 ? new NSError(result) : null;
}

public int streamStatus() {
	return OS.objc_msgSend(this.id, OS.sel_streamStatus);
}

}

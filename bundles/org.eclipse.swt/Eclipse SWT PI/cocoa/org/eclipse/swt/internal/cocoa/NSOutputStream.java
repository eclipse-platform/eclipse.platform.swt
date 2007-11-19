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

public class NSOutputStream extends NSStream {

public NSOutputStream() {
	super();
}

public NSOutputStream(int id) {
	super(id);
}

public boolean hasSpaceAvailable() {
	return OS.objc_msgSend(this.id, OS.sel_hasSpaceAvailable) != 0;
}

public NSOutputStream initToBuffer(int buffer, int capacity) {
	int result = OS.objc_msgSend(this.id, OS.sel_initToBuffer_1capacity_1, buffer, capacity);
	return result != 0 ? this : null;
}

public NSOutputStream initToFileAtPath(NSString path, boolean shouldAppend) {
	int result = OS.objc_msgSend(this.id, OS.sel_initToFileAtPath_1append_1, path != null ? path.id : 0, shouldAppend);
	return result != 0 ? this : null;
}

public NSOutputStream initToMemory() {
	int result = OS.objc_msgSend(this.id, OS.sel_initToMemory);
	return result != 0 ? this : null;
}

public static id outputStreamToBuffer(int buffer, int capacity) {
	int result = OS.objc_msgSend(OS.class_NSOutputStream, OS.sel_outputStreamToBuffer_1capacity_1, buffer, capacity);
	return result != 0 ? new id(result) : null;
}

public static id outputStreamToFileAtPath(NSString path, boolean shouldAppend) {
	int result = OS.objc_msgSend(OS.class_NSOutputStream, OS.sel_outputStreamToFileAtPath_1append_1, path != null ? path.id : 0, shouldAppend);
	return result != 0 ? new id(result) : null;
}

public static id outputStreamToMemory() {
	int result = OS.objc_msgSend(OS.class_NSOutputStream, OS.sel_outputStreamToMemory);
	return result != 0 ? new id(result) : null;
}

public int write(int buffer, int len) {
	return OS.objc_msgSend(this.id, OS.sel_write_1maxLength_1, buffer, len);
}

}

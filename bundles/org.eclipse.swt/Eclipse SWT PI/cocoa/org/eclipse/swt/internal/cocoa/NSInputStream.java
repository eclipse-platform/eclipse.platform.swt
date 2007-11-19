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

public class NSInputStream extends NSStream {

public NSInputStream() {
	super();
}

public NSInputStream(int id) {
	super(id);
}

public boolean getBuffer(int buffer, int len) {
	return OS.objc_msgSend(this.id, OS.sel_getBuffer_1length_1, buffer, len) != 0;
}

public boolean hasBytesAvailable() {
	return OS.objc_msgSend(this.id, OS.sel_hasBytesAvailable) != 0;
}

public id initWithData(NSData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithFileAtPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFileAtPath_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id inputStreamWithData(NSData data) {
	int result = OS.objc_msgSend(OS.class_NSInputStream, OS.sel_inputStreamWithData_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id inputStreamWithFileAtPath(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSInputStream, OS.sel_inputStreamWithFileAtPath_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public int read(int buffer, int len) {
	return OS.objc_msgSend(this.id, OS.sel_read_1maxLength_1, buffer, len);
}

}

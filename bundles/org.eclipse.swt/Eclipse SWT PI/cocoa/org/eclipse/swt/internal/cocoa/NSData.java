/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSData extends NSObject {

public NSData() {
	super();
}

public NSData(int /*long*/ id) {
	super(id);
}

public NSData(id id) {
	super(id);
}

public int /*long*/ bytes() {
	return OS.objc_msgSend(this.id, OS.sel_bytes);
}

public static NSData dataWithBytes(byte[] bytes, int /*long*/ length) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSData, OS.sel_dataWithBytes_length_, bytes, length);
	return result != 0 ? new NSData(result) : null;
}

public void getBytes(byte[] buffer) {
	OS.objc_msgSend(this.id, OS.sel_getBytes_, buffer);
}

public void getBytes(int /*long*/ buffer, int /*long*/ length) {
	OS.objc_msgSend(this.id, OS.sel_getBytes_length_, buffer, length);
}

public int /*long*/ length() {
	return OS.objc_msgSend(this.id, OS.sel_length);
}

}

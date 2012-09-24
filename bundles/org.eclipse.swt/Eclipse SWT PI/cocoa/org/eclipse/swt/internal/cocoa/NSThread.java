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

public class NSThread extends NSObject {

public NSThread() {
	super();
}

public NSThread(long /*int*/ id) {
	super(id);
}

public NSThread(id id) {
	super(id);
}

public static NSThread currentThread() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSThread, OS.sel_currentThread);
	return result != 0 ? new NSThread(result) : null;
}

public static boolean isMainThread() {
	return OS.objc_msgSend_bool(OS.class_NSThread, OS.sel_isMainThread);
}

public NSMutableDictionary threadDictionary() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_threadDictionary);
	return result != 0 ? new NSMutableDictionary(result) : null;
}

}

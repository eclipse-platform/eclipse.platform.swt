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

public class NSAutoreleasePool extends NSObject {

public NSAutoreleasePool() {
	super();
}

public NSAutoreleasePool(int id) {
	super(id);
}

public static void static_addObject_(id anObject) {
	OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_addObject_1, anObject != null ? anObject.id : 0);
}

public void addObject_(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_addObject_1, anObject != null ? anObject.id : 0);
}

public static int autoreleasedObjectCount() {
	return OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_autoreleasedObjectCount);
}

public void drain() {
	OS.objc_msgSend(this.id, OS.sel_drain);
}

public static void enableFreedObjectCheck(boolean enable) {
	OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_enableFreedObjectCheck_1, enable);
}

public static void enableRelease(boolean enable) {
	OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_enableRelease_1, enable);
}

public static int poolCountHighWaterMark() {
	return OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_poolCountHighWaterMark);
}

public static int poolCountHighWaterResolution() {
	return OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_poolCountHighWaterResolution);
}

public static void resetTotalAutoreleasedObjects() {
	OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_resetTotalAutoreleasedObjects);
}

public static void setPoolCountHighWaterMark(int count) {
	OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_setPoolCountHighWaterMark_1, count);
}

public static void setPoolCountHighWaterResolution(int res) {
	OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_setPoolCountHighWaterResolution_1, res);
}

public static void showPools() {
	OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_showPools);
}

public static int topAutoreleasePoolCount() {
	return OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_topAutoreleasePoolCount);
}

public static int totalAutoreleasedObjects() {
	return OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_totalAutoreleasedObjects);
}

}

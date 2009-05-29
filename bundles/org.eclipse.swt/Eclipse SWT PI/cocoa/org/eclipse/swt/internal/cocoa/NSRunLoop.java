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

public class NSRunLoop extends NSObject {

public NSRunLoop() {
	super();
}

public NSRunLoop(int /*long*/ id) {
	super(id);
}

public NSRunLoop(id id) {
	super(id);
}

public void addTimer(NSTimer timer, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_addTimer_forMode_, timer != null ? timer.id : 0, mode != null ? mode.id : 0);
}

public static NSRunLoop currentRunLoop() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSRunLoop, OS.sel_currentRunLoop);
	return result != 0 ? new NSRunLoop(result) : null;
}

public static NSRunLoop mainRunLoop() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSRunLoop, OS.sel_mainRunLoop);
	return result != 0 ? new NSRunLoop(result) : null;
}

public boolean runMode(NSString mode, NSDate limitDate) {
	return OS.objc_msgSend_bool(this.id, OS.sel_runMode_beforeDate_, mode != null ? mode.id : 0, limitDate != null ? limitDate.id : 0);
}

}

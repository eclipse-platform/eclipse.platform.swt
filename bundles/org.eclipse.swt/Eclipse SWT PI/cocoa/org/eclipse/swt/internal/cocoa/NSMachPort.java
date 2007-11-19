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

public class NSMachPort extends NSPort {

public NSMachPort() {
	super();
}

public NSMachPort(int id) {
	super(id);
}

public id initWithMachPort_(int machPort) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithMachPort_1, machPort);
	return result != 0 ? new id(result) : null;
}

public id initWithMachPort_options_(int machPort, int f) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithMachPort_1options_1, machPort, f);
	return result != 0 ? new id(result) : null;
}

public int machPort() {
	return OS.objc_msgSend(this.id, OS.sel_machPort);
}

public static NSPort static_portWithMachPort_(int machPort) {
	int result = OS.objc_msgSend(OS.class_NSMachPort, OS.sel_portWithMachPort_1, machPort);
	return result != 0 ? new NSPort(result) : null;
}

public static NSPort static_portWithMachPort_options_(int machPort, int f) {
	int result = OS.objc_msgSend(OS.class_NSMachPort, OS.sel_portWithMachPort_1options_1, machPort, f);
	return result != 0 ? new NSPort(result) : null;
}

public void removeFromRunLoop(NSRunLoop runLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_removeFromRunLoop_1forMode_1, runLoop != null ? runLoop.id : 0, mode != null ? mode.id : 0);
}

public void scheduleInRunLoop(NSRunLoop runLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_scheduleInRunLoop_1forMode_1, runLoop != null ? runLoop.id : 0, mode != null ? mode.id : 0);
}

}

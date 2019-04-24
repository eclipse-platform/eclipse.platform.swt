/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSScreen extends NSObject {

public NSScreen() {
	super();
}

public NSScreen(long id) {
	super(id);
}

public NSScreen(id id) {
	super(id);
}

public double backingScaleFactor() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_backingScaleFactor);
}

public int depth() {
	return (int)OS.objc_msgSend(this.id, OS.sel_depth);
}

public NSDictionary deviceDescription() {
	long result = OS.objc_msgSend(this.id, OS.sel_deviceDescription);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSRect frame() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frame);
	return result;
}

public static NSScreen mainScreen() {
	long result = OS.objc_msgSend(OS.class_NSScreen, OS.sel_mainScreen);
	return result != 0 ? new NSScreen(result) : null;
}

public static NSArray screens() {
	long result = OS.objc_msgSend(OS.class_NSScreen, OS.sel_screens);
	return result != 0 ? new NSArray(result) : null;
}

public NSRect visibleFrame() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_visibleFrame);
	return result;
}

}

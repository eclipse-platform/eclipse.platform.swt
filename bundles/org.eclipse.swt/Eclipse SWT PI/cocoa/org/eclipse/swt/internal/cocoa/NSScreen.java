/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSScreen extends NSObject {

public NSScreen() {
	super();
}

public NSScreen(long /*int*/ id) {
	super(id);
}

public NSScreen(id id) {
	super(id);
}

public double /*float*/ backingScaleFactor() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_backingScaleFactor);
}

public int depth() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_depth);
}

public NSDictionary deviceDescription() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_deviceDescription);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSRect frame() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frame);
	return result;
}

public static NSScreen mainScreen() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSScreen, OS.sel_mainScreen);
	return result != 0 ? new NSScreen(result) : null;
}

public static NSArray screens() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSScreen, OS.sel_screens);
	return result != 0 ? new NSArray(result) : null;
}

public double /*float*/ userSpaceScaleFactor() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_userSpaceScaleFactor);
}

public NSRect visibleFrame() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_visibleFrame);
	return result;
}

}

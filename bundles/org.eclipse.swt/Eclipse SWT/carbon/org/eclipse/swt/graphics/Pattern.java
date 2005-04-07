/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.carbon.*;

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public class Pattern extends Resource {

	int jniRef;
	
	Image image;

public Pattern(Device device, Image image) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.device = device;
	this.image = image;
	device.createPatternCallbacks();
	jniRef = OS.NewGlobalRef(this);
	if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

int createPattern(int context) {
	float[] transform = new float[6];
	OS.CGContextGetCTM(context, transform);
	int imageHandle = image.handle;
	CGRect rect = new CGRect();
	rect.width = OS.CGImageGetWidth(imageHandle);
	rect.height = OS.CGImageGetHeight(imageHandle);	
	CGPatternCallbacks callbacks = new CGPatternCallbacks();
	callbacks.drawPattern = device.drawPatternProc;
	int pattern = OS.CGPatternCreate(jniRef, rect, transform, rect.width, rect.height, OS.kCGPatternTilingNoDistortion, 0, callbacks);
	if (pattern == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	return pattern;
}

public void dispose() {
	if (jniRef == 0) return;
	if (device.isDisposed()) return;
	if (jniRef != 0) OS.DeleteGlobalRef(jniRef);
	jniRef = 0;
	image = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

int drawPatternProc (int ref, int context) {
	if (image != null) {
		if (image.isDisposed()) return 0;
		int imageHandle = image.handle;
		CGRect rect = new CGRect();
		rect.width = OS.CGImageGetWidth(imageHandle);
		rect.height = OS.CGImageGetHeight(imageHandle);
		OS.CGContextScaleCTM(context, 1, -1);
	 	OS.CGContextTranslateCTM(context, 0, -rect.height);
		OS.CGContextDrawImage(context, rect, imageHandle);
	}
	return 0;
}

public boolean isDisposed() {
	return jniRef == 0;
}

public String toString() {
	if (isDisposed()) return "Pattern {*DISPOSED*}";
	return "Pattern {" + jniRef + "}";
}
	
}

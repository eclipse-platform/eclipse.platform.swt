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
	Color color1, color2;
	float x1, y1, x2, y2;
	int function, shading;

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

public Pattern(Device device, float x1, float y1, float x2, float y2, Color color1, Color color2) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color1 == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color1.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (color2 == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color2.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.device = device;
	this.x1 = x1;
	this.y1 = y1;
	this.x2 = x2;
	this.y2 = y2;
	this.color1 = color1;
	this.color2 = color2;
	device.createPatternCallbacks();
	jniRef = OS.NewGlobalRef(this);
	if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	CGPoint start = new CGPoint();
	start.x = x1;
	start.y = y1;
	CGPoint end = new CGPoint();
	end.x = x2;
	end.y = y2;
	CGFunctionCallbacks fCallbacks = new CGFunctionCallbacks();
	fCallbacks.evaluate = device.axialShadingProc;
	function = OS.CGFunctionCreate(jniRef, 1, new float[]{0, 1}, 4, new float[]{0, 1, 0, 1, 0, 1, 0, 1}, fCallbacks);
	if (function == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	shading = OS.CGShadingCreateAxial(device.colorspace, start, end, function, true, true);	
	if (shading == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

int axialShadingProc (int ref, int in, int out) {
	float[] buffer = new float[4];
	OS.memcpy(buffer, in, 4);
	float factor2 = buffer[0], factor1 = 1 - factor2;
	float[] c1 = color1.handle;
	float[] c2 = color2.handle;
	buffer[0] = (c2[0] * factor2) + (c1[0] * factor1);
	buffer[1] = (c2[1] * factor2) + (c1[1] * factor1);
	buffer[2] = (c2[2] * factor2) + (c1[2] * factor1);
	//TODO - how about alpha?
	buffer[3] = 1;
	OS.memcpy(out, buffer, buffer.length * 4);
	return 0;
}

int createPattern(int context) {
	float[] transform = new float[6];
	OS.CGContextGetCTM(context, transform);
	CGRect rect = new CGRect();
	if (image != null) {
		int imageHandle = image.handle;
		rect.width = OS.CGImageGetWidth(imageHandle);
		rect.height = OS.CGImageGetHeight(imageHandle);
	} else {
		rect.x = x1 - 0.5f;
		rect.y = y1 - 0.5f;
		rect.width = x2 - x1 + 1;
		rect.height = y2 - y1 + 1;
	}
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
	if (function != 0) OS.CGFunctionRelease(function);
	if (shading != 0) OS.CGShadingRelease(shading);
	jniRef = function = shading = 0;
	image = null;
	color1 = color2 = null;
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
	} else {
		OS.CGContextDrawShading(context, shading);
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

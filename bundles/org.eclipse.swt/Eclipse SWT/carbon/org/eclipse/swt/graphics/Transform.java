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
import org.eclipse.swt.internal.Compatibility;
import org.eclipse.swt.internal.carbon.*;

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public class Transform {
	/**
	 * the handle to the OS path resource
	 * (Warning: This field is platform dependent)
	 */
	public float[] handle;
	
	/**
	 * the device where this font was created
	 */
	Device device;
	
public Transform (Device device) {
	this(device, 1, 0, 0, 1, 0, 0);
}

public Transform(Device device, float[] elements) {
	this (device, checkTransform(elements)[0], elements[1], elements[2], elements[3], elements[4], elements[5]);
}

public Transform (Device device, float m11, float m12, float m21, float m22, float dx, float dy) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	handle = new float[6];
	OS.CGAffineTransformMake(m11, m12, m21, m22, dx, dy, handle);
	if (handle == null) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

static float[] checkTransform(float[] elements) {
	if (elements == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (elements.length < 6) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return elements;
}

public void dispose() {
	if (handle == null) return;
	if (device.isDisposed()) return;
	handle = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public void getElements(float[] elements) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (elements == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (elements.length < 6) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	System.arraycopy(handle, 0, elements, 0, handle.length);
}

public void invert() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if ((handle [0] * handle [3] - handle [1] * handle [2]) == 0) {
		SWT.error(SWT.ERROR_CANNOT_INVERT_MATRIX);
	}
	OS.CGAffineTransformInvert(handle, handle);
}

public boolean isDisposed() {
	return handle == null;
}

public boolean isIdentity() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return handle[0] == 1 && handle[1] == 0 && handle[2] == 0 && handle[3] == 1 && handle[4] == 0 && handle[5] == 0;
}

public void multiply(Transform matrix) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (matrix == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (matrix.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.CGAffineTransformConcat(matrix.handle, handle, handle);
}

public void rotate(float angle) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.CGAffineTransformRotate(handle, angle * (float)Compatibility.PI / 180, handle);
}

public void scale(float scaleX, float scaleY) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.CGAffineTransformScale(handle, scaleX, scaleY, handle);
}

public void setElements(float m11, float m12, float m21, float m22, float dx, float dy) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.CGAffineTransformMake(m11, m12, m21, m22, dx, dy, handle);
}

public void transform(float[] pointArray) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	CGPoint point = new CGPoint();
	int length = pointArray.length / 2;
	for (int i = 0, j = 0; i < length; i++, j += 2) {
		point.x = pointArray[j];
		point.y = pointArray[j + 1];
		OS.CGPointApplyAffineTransform(point, handle, point);
		pointArray[j] = point.x;				
		pointArray[j + 1] = point.y;				
	}
}

public void translate(float offsetX, float offsetY) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.CGAffineTransformTranslate(handle, offsetX, offsetY, handle);
}

public String toString() {
	if (isDisposed()) return "Transform {*DISPOSED*}";
	float[] elements = new float[6];
	getElements(elements);
	return "Transform {" + elements [0] + ", " + elements [1] + ", " +elements [2] + ", " +elements [3] + ", " +elements [4] + ", " +elements [5] + "}";
}

}

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
import org.eclipse.swt.internal.gdip.*;

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public class Transform extends Resource {
	/**
	 * the handle to the OS path resource
	 * (Warning: This field is platform dependent)
	 */
	public int handle;
	
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
	device.checkGDIP();
	handle = Gdip.Matrix_new(m11, m12, m21, m22, dx, dy);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

static float[] checkTransform(float[] elements) {
	if (elements == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (elements.length < 6) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return elements;
}

public void dispose() {
	if (handle == 0) return;
	if (device.isDisposed()) return;
	Gdip.Matrix_delete(handle);
	handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public void getElements(float[] elements) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (elements == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (elements.length < 6) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Gdip.Matrix_GetElements(handle, elements);
}

public void invert() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (Gdip.Matrix_Invert(handle) != 0) SWT.error(SWT.ERROR_CANNOT_INVERT_MATRIX);
}

public boolean isDisposed() {
	return handle == 0;
}

public boolean isIdentity() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return Gdip.Matrix_IsIdentity(handle);
}

public void multiply(Transform matrix) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (matrix == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (matrix.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Gdip.Matrix_Multiply(handle, matrix.handle, Gdip.MatrixOrderPrepend);
}

public void rotate(float angle) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Gdip.Matrix_Rotate(handle, angle, Gdip.MatrixOrderPrepend);
}

public void scale(float scaleX, float scaleY) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Gdip.Matrix_Scale(handle, scaleX, scaleY, Gdip.MatrixOrderPrepend);
}

public void setElements(float m11, float m12, float m21, float m22, float dx, float dy) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Gdip.Matrix_SetElements(handle, m11, m12, m21, m22, dx, dy);
}

public void transform(float[] pointArray) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	Gdip.Matrix_TransformPoints(handle, pointArray, pointArray.length / 2);
}

public void translate(float offsetX, float offsetY) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Gdip.Matrix_Translate(handle, offsetX, offsetY, Gdip.MatrixOrderPrepend);
}

public String toString() {
	if (isDisposed()) return "Transform {*DISPOSED*}";
	float[] elements = new float[6];
	getElements(elements);
	return "Transform {" + elements [0] + "," + elements [1] + "," +elements [2] + "," +elements [3] + "," +elements [4] + "," +elements [5] + "}";
}

}

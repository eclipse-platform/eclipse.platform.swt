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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;

public class Transform extends Resource {
	/**
	 * the handle to the OS transform resource
	 * (Warning: This field is platform dependent)
	 */
	public int /*long*/ handle;
	
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
	device.checkCairo();
	handle = Cairo.cairo_matrix_create();
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	Cairo.cairo_matrix_set_affine(handle, m11, m12, m21, m22, dx, dy);
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
	Cairo.cairo_matrix_destroy(handle);
	handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public void getElements(float[] elements) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (elements == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (elements.length < 6) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	double[] a = new double[1], b = new double[1], c = new double[1], d = new double[1];
	double[] tx = new double[1], ty = new double[1];
	Cairo.cairo_matrix_get_affine(handle, a, b, c, d, tx, ty);
	elements[0] = (float)a[0];
	elements[1] = (float)b[0];
	elements[2] = (float)c[0];
	elements[3] = (float)d[0];
	elements[4] = (float)tx[0];
	elements[5] = (float)ty[0];
}

public void invert() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (Cairo.cairo_matrix_invert(handle) != 0) {
		SWT.error(SWT.ERROR_CANNOT_INVERT_MATRIX);
	}
}

public boolean isDisposed() {
	return handle == 0;
}

public boolean isIdentity() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	float[] m  = new float[6];
	getElements(m);
	return m[0] == 1 && m[1] == 0 && m[2] == 0 && m[3] == 1 && m[4] == 0 && m[5] == 0;
}

public void multiply(Transform matrix) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (matrix == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (matrix.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Cairo.cairo_matrix_multiply(handle, matrix.handle, handle);
}

public void rotate(float angle) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Cairo.cairo_matrix_rotate(handle, angle * (float)Compatibility.PI / 180);
}

public void scale(float scaleX, float scaleY) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Cairo.cairo_matrix_scale(handle, scaleX, scaleY);
}

public void setElements(float m11, float m12, float m21, float m22, float dx, float dy) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Cairo.cairo_matrix_set_affine(handle, m11, m12, m21, m22, dx, dy);
}

public void transform(float[] pointArray) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	double[] dx = new double[1], dy = new double[1];
	int length = pointArray.length / 2;
	for (int i = 0, j = 0; i < length; i++, j += 2) {
		dx[0] = pointArray[j];
		dy[0] = pointArray[j + 1];
		Cairo.cairo_matrix_transform_point(handle, dx, dy);
		pointArray[j] = (float)dx[0];
		pointArray[j + 1] = (float)dy[0];
	}
}

public void translate(float offsetX, float offsetY) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Cairo.cairo_matrix_translate(handle, offsetX, offsetY);
}

public String toString() {
	if (isDisposed()) return "Transform {*DISPOSED*}";
	float[] elements = new float[6];
	getElements(elements);
	return "Transform {" + elements [0] + "," + elements [1] + "," +elements [2] + "," +elements [3] + "," +elements [4] + "," +elements [5] + "}";
}

}

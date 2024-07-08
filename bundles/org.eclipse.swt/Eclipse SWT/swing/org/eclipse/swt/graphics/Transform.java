/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import org.eclipse.swt.SWT;

/**
 * Instances of this class represent transformation matrices for 
 * points expressed as (x, y) pairs of floating point numbers.
 * <p>
 * Application code must explicitly invoke the <code>Transform.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * 
 * @since 3.1
 */
public class Transform extends Resource {

  /**
   * the OS resource for the Transform
   * (Warning: This field is platform dependent)
   * <p>
   * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
   * public API. It is marked public only so that it can be shared
   * within the packages provided by SWT. It is not available on all
   * platforms and should never be accessed from application code.
   * </p>
   */
	public AffineTransform handle;

/**
 * Constructs a new identity Transform.
 * 
 * @param device the device on which to allocate the Transform
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the Transform could not be obtained/li>
 * </ul>
 * 
 * @see #dispose()
 */
public Transform (Device device) {
	this(device, 1, 0, 0, 1, 0, 0);
}

/**
 * Constructs a new Transform given an array of elements that represent the 
 * matrix that describes the transformation.
 * 
 * @param device the device on which to allocate the Transform
 * @param elements an array of floats that describe the transformation matrix
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device, or the elements array is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the elements array is too small to hold the matrix values</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the Transform could not be obtained/li>
 * </ul>
 * 
 * @see #dispose()
 */
public Transform(Device device, float[] elements) {
	this (device, checkTransform(elements)[0], elements[1], elements[2], elements[3], elements[4], elements[5]);
}

/**
 * Constructs a new Transform given all of the elements that represent the 
 * matrix that describes the transformation.
 * 
 * @param device the device on which to allocate the Transform
 * @param m11 the first element of the first row of the matrix
 * @param m12 the second element of the first row of the matrix
 * @param m21 the first element of the second row of the matrix
 * @param m22 the second element of the second row of the matrix
 * @param dx the third element of the first row of the matrix
 * @param dy the third element of the second row of the matrix
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the Transform could not be obtained/li>
 * </ul>
 * 
 * @see #dispose()
 */
public Transform (Device device, float m11, float m12, float m21, float m22, float dx, float dy) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	handle = new AffineTransform(m11, m12, m21, m22, dx, dy);
	if (handle == null) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

static float[] checkTransform(float[] elements) {
	if (elements == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (elements.length < 6) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return elements;
}

/**
 * Disposes of the operating system resources associated with
 * the Transform. Applications must dispose of all Transforms that
 * they allocate.
 */
public void dispose() {
	if (handle == null) return;
	if (device.isDisposed()) return;
	handle = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

/**
 * Fills the parameter with the values of the transformation matrix
 * that the receiver represents, in the order {m11, m12, m21, m22, dx, dy}.
 *
 * @param elements array to hold the matrix values
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is too small to hold the matrix values</li>
 * </ul>
 */
public void getElements(float[] elements) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (elements == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (elements.length < 6) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	double[] matrix = new double[6];
	handle.getMatrix(matrix);
	for (int i = 0; i < 6; i++) {
		elements[i] = (float) matrix[i];
	}
}

/**
 * Modifies the receiver such that the matrix it represents becomes the
 * the mathematical inverse of the matrix it previously represented. 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_CANNOT_INVERT_MATRIX - if the matrix is not invertable</li>
 * </ul>
 */
public void invert() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	try {
		handle = handle.createInverse();
	} catch (NoninvertibleTransformException e) {
		SWT.error(SWT.ERROR_CANNOT_INVERT_MATRIX);
	}
}

/**
 * Returns <code>true</code> if the Transform has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the Transform.
 * When a Transform has been disposed, it is an error to
 * invoke any other method using the Transform.
 *
 * @return <code>true</code> when the Transform is disposed, and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == null;
}

/**
 * Returns <code>true</code> if the Transform represents the identity matrix
 * and false otherwise.
 *
 * @return <code>true</code> if the receiver is an identity Transform, and <code>false</code> otherwise
 */
public boolean isIdentity() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return handle.isIdentity();
}

/**
 * Modifies the receiver such that the matrix it represents becomes the
 * the result of multiplying the matrix it previously represented by the
 * argument. 
 *
 * @param matrix the matrix to multiply the receiver by
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 */
public void multiply(Transform matrix) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (matrix == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (matrix.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	handle.concatenate(matrix.handle);
}

/**
 * Modifies the receiver so that it represents a transformation that is
 * equivalent to its previous transformation rotated by the specified angle.
 * The angle is specified in degrees and for the identity transform 0 degrees
 * is at the 3 o'clock position. A positive value indicates a clockwise rotation
 * while a negative value indicates a counter-clockwise rotation.
 *
 * @param angle the angle to rotate the transformation by
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void rotate(float angle) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	handle.rotate(angle * Math.PI / 180);
}

/**
 * Modifies the receiver so that it represents a transformation that is
 * equivalent to its previous transformation scaled by (scaleX, scaleY).
 * 
 * @param scaleX the amount to scale in the X direction
 * @param scaleY the amount to scale in the Y direction
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void scale(float scaleX, float scaleY) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	handle.scale(scaleX, scaleY);
}

/**
 * Modifies the receiver to represent a new transformation given all of
 * the elements that represent the matrix that describes that transformation.
 * 
 * @param m11 the first element of the first row of the matrix
 * @param m12 the second element of the first row of the matrix
 * @param m21 the first element of the second row of the matrix
 * @param m22 the second element of the second row of the matrix
 * @param dx the third element of the first row of the matrix
 * @param dy the third element of the second row of the matrix
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setElements(float m11, float m12, float m21, float m22, float dx, float dy) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	handle.setTransform(m11, m12, m21, m22, dx, dy);
}

/** 
 * Given an array containing points described by alternating x and y values,
 * modify that array such that each point has been replaced with the result of
 * applying the transformation represented by the receiver to that point.
 *
 * @param pointArray an array of alternating x and y values to be transformed
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point array is null</li>
 * </ul>  
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void transform(float[] pointArray) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	handle.transform(pointArray, 0, pointArray, 0, pointArray.length);
}

/**
 * Modifies the receiver so that it represents a transformation that is
 * equivalent to its previous transformation translated by (offsetX, offsetY).
 * 
 * @param offsetX the distance to translate in the X direction
 * @param offsetY the distance to translate in the Y direction
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void translate(float offsetX, float offsetY) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	handle.translate(offsetX, offsetY);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString() {
	if (isDisposed()) return "Transform {*DISPOSED*}";
	float[] elements = new float[6];
	getElements(elements);
	return "Transform {" + elements [0] + "," + elements [1] + "," +elements [2] + "," +elements [3] + "," +elements [4] + "," +elements [5] + "}";
}

}

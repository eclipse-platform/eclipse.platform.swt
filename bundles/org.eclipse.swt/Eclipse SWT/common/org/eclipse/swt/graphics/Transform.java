/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;

/**
 * Instances of this class represent transformation matrices for
 * points expressed as (x, y) pairs of floating point numbers.
 * <p>
 * Application code must explicitly invoke the <code>Transform.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * <p>
 * This class requires the operating system's advanced graphics subsystem
 * which may not be available on some platforms.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: GraphicsExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.1
 */
public class Transform extends Resource {
	double m11, m12, m21, m22, dx, dy;

/**
 * Constructs a new identity Transform.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * <p>
 * You must dispose the transform when it is no longer required.
 * </p>
 *
 * @param device the device on which to allocate the Transform
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 * </ul>
 *
 * @see #dispose()
 */
public Transform (Device device) {
	super(device);
	identity();
}

/**
 * Constructs a new Transform given an array of elements that represent the
 * matrix that describes the transformation.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * <p>
 * You must dispose the transform when it is no longer required.
 * </p>
 *
 * @param device the device on which to allocate the Transform
 * @param elements an array of floats that describe the transformation matrix
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device, or the elements array is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the elements array is too small to hold the matrix values</li>
 * </ul>
 *
 * @see #dispose()
 */
public Transform(Device device, float[] elements) {
	super(device);
	if (elements == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (elements.length < 6) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	setElements(elements);
}

/**
 * Constructs a new Transform given all of the elements that represent the
 * matrix that describes the transformation.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * <p>
 * You must dispose the transform when it is no longer required.
 * </p>
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
 *
 * @see #dispose()
 */
public Transform (Device device, float m11, float m12, float m21, float m22, float dx, float dy) {
	super(device);
	setElements(m11, m12, m21, m22, dx, dy);
}

/**
 * Fills the parameter with the values of the transformation matrix
 * that the receiver represents, in the order {m11, m12, m21, m22, dx, dy}.
 *
 * @param elements array to hold the matrix values
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is too small to hold the matrix values</li>
 * </ul>
 */
public void getElements(float[] elements) {
	if (elements == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (elements.length < 6) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	elements[0] = (float)m11;
	elements[1] = (float)m12;
	elements[2] = (float)m21;
	elements[3] = (float)m22;
	elements[4] = (float)dx;
	elements[5] = (float)dy;
}

void getElements(double[] elements) {
	elements[0] = m11;
	elements[1] = m12;
	elements[2] = m21;
	elements[3] = m22;
	elements[4] = dx;
	elements[5] = dy;
}

/**
 * Modifies the receiver such that the matrix it represents becomes the
 * identity matrix.
 *
 * @since 3.4
 */
public void identity() {
	m11 = 1.;
	m12 = 0.;
	m21 = 0.;
	m22 = 1.;
	dx = 0.;
	dy = 0.;
}

/**
 * Modifies the receiver such that the matrix it represents becomes
 * the mathematical inverse of the matrix it previously represented.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_CANNOT_INVERT_MATRIX - if the matrix is not invertible</li>
 * </ul>
 */
public void invert() {
	double m11 = this.m11;
	double m12 = this.m12;
	double m21 = this.m21;
	double m22 = this.m22;
	double dx = this.dx;
	double dy = this.dy;
	double det = m11 * m22 - m12 * m21;
	if (det == 0. || !Double.isFinite(det)) SWT.error(SWT.ERROR_CANNOT_INVERT_MATRIX);
	double dinv = 1. / det;
	this.m11 = m22 * dinv;
	this.m12 = -m12 * dinv;
	this.m21 = -m21 * dinv;
	this.m22 = m11 * dinv;
	this.dx = (m21 * dy - m22 * dx) * dinv;
	this.dy = (m12 * dx - m11 * dy) * dinv;
}

/**
 * Returns <code>true</code> if the Transform has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the Transform.
 * When a Transform has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the Transform.
 *
 * @return <code>true</code> when the Transform is disposed, and <code>false</code> otherwise
 */
@Override
public boolean isDisposed() {
	return device == null;
}

/**
 * Returns <code>true</code> if the Transform represents the identity matrix
 * and false otherwise.
 *
 * @return <code>true</code> if the receiver is an identity Transform, and <code>false</code> otherwise
 */
public boolean isIdentity() {
	return m11 == 1. && m12 == 0. && m21 == 0. && m22 == 1. && dx == 0. && dy == 0.;
}

/**
 * Modifies the receiver such that the matrix it represents becomes the
 * the result of multiplying the matrix it previously represented by the
 * argument.
 *
 * @param matrix the matrix to multiply the receiver by
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 */
public void multiply(Transform matrix) {
	if (matrix == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	double m11 = this.m11;
	double m12 = this.m12;
	double m21 = this.m21;
	double m22 = this.m22;
	double dx = this.dx;
	double dy = this.dy;
	this.m11 = m11 * matrix.m11 + m12 * matrix.m21;
	this.m12 = m11 * matrix.m12 + m12 * matrix.m22;
	this.m21 = m21 * matrix.m11 + m22 * matrix.m21;
	this.m22 = m21 * matrix.m12 + m22 * matrix.m22;
	this.dx = dx * matrix.m11 + dy * matrix.m21 + matrix.dx;
	this.dy = dx * matrix.m12 + dy * matrix.m22 + matrix.dy;
}

/**
 * Modifies the receiver so that it represents a transformation that is
 * equivalent to its previous transformation rotated by the specified angle.
 * The angle is specified in degrees and for the identity transform 0 degrees
 * is at the 3 o'clock position. A positive value indicates a clockwise rotation
 * while a negative value indicates a counter-clockwise rotation.
 *
 * @param angle the angle to rotate the transformation by
 */
public void rotate(float angle) {
	double m11 = this.m11;
	double m12 = this.m12;
	double m21 = this.m21;
	double m22 = this.m22;
	double rangle = Math.toRadians(angle);
	double sin = Math.sin(rangle);
	double cos = Math.cos(rangle);
	this.m11 = cos * m11 + sin * m21;
	this.m12 = cos * m12 + sin * m22;
	this.m21 = -sin * m11 + cos * m21;
	this.m22 = -sin * m12 + cos * m22;
}

/**
 * Modifies the receiver so that it represents a transformation that is
 * equivalent to its previous transformation scaled by (scaleX, scaleY).
 *
 * @param scaleX the amount to scale in the X direction
 * @param scaleY the amount to scale in the Y direction
 */
public void scale(float scaleX, float scaleY) {
	m11 *= scaleX;
	m12 *= scaleX;
	m21 *= scaleY;
	m22 *= scaleY;
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
 */
public void setElements(float m11, float m12, float m21, float m22, float dx, float dy) {
	this.m11 = m11;
	this.m12 = m12;
	this.m21 = m21;
	this.m22 = m22;
	this.dx = dx;
	this.dy = dy;
}

void setElements(float [] elements) {
	m11 = elements[0];
	m12 = elements[1];
	m21 = elements[2];
	m22 = elements[3];
	dx = elements[4];
	dy = elements[5];
}

void setElements(double [] elements) {
	m11 = elements[0];
	m12 = elements[1];
	m21 = elements[2];
	m22 = elements[3];
	dx = elements[4];
	dy = elements[5];
}

/**
 * Modifies the receiver so that it represents a transformation that is
 * equivalent to its previous transformation sheared by (shearX, shearY).
 *
 * @param shearX the shear factor in the X direction
 * @param shearY the shear factor in the Y direction
 *
 * @since 3.4
 */
public void shear(float shearX, float shearY) {
	double m11 = this.m11;
	double m12 = this.m12;
	double m21 = this.m21;
	double m22 = this.m22;
	this.m11 += shearY * m21;
	this.m12 += shearY * m22;
	this.m21 += shearX * m11;
	this.m22 += shearX * m12;
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
 */
public void transform(float[] pointArray) {
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int length = pointArray.length;
	for (int i = 0; i < length; i += 2) {
		float x = pointArray[i];
		float y = pointArray[i+1];
		pointArray[i] = (float)(x * m11 + y * m21 + dx);
		pointArray[i+1] = (float)(x * m12 + y * m22 + dy);
	}
}

/**
 * Modifies the receiver so that it represents a transformation that is
 * equivalent to its previous transformation translated by (offsetX, offsetY).
 *
 * @param offsetX the distance to translate in the X direction
 * @param offsetY the distance to translate in the Y direction
 */
public void translate(float offsetX, float offsetY) {
	dx += offsetX * m11 + offsetY * m21;
	dy += offsetX * m12 + offsetY * m22;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString() {
	return "Transform {" + m11 + ", " + m12 + ", " + m21 + ", " + m22 + ", " + dx + ", " + dy + "}";
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gdip.*;

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

	private int initialZoom;

	private HashMap<Integer, Long> zoomLevelToHandle = new HashMap<>();

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
 * @exception SWTException <ul>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the Transform could not be obtained</li>
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
 * @exception SWTException <ul>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the Transform could not be obtained</li>
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
 * @exception SWTException <ul>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the Transform could not be obtained</li>
 * </ul>
 *
 * @see #dispose()
 */
public Transform (Device device, float m11, float m12, float m21, float m22, float dx, float dy) {
	super(device);
	initialZoom = DPIUtil.getDeviceZoom();
	this.device.checkGDIP();
	long handle = Gdip.Matrix_new(m11, m12, m21, m22,
			DPIUtil.scaleUp(this.device, dx, initialZoom), DPIUtil.scaleUp(this.device, dy, initialZoom));
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	zoomLevelToHandle.put(initialZoom, handle);
	init();
	this.device.registerResourceWithZoomSupport(this);
}

static float[] checkTransform(float[] elements) {
	if (elements == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (elements.length < 6) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return elements;
}

@Override
void destroy() {
	device.deregisterResourceWithZoomSupport(this);
	zoomLevelToHandle.values().forEach(Gdip::Matrix_delete);
	zoomLevelToHandle.clear();
}

@Override
void destroyHandlesExcept(Set<Integer> zoomLevels) {
	zoomLevelToHandle.entrySet().removeIf(entry -> {
		final Integer zoom = entry.getKey();
		if (!zoomLevels.contains(zoom) && zoom != initialZoom) {
			Gdip.Matrix_delete(entry.getValue());
			return true;
		}
		return false;
	});
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
	Gdip.Matrix_GetElements(getHandle(initialZoom), elements);
	Drawable drawable = getDevice();
	elements[4] = DPIUtil.scaleDown(drawable, elements[4], initialZoom);
	elements[5] = DPIUtil.scaleDown(drawable, elements[5], initialZoom);
}

/**
 * Modifies the receiver such that the matrix it represents becomes the
 * identity matrix.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.4
 */
public void identity() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Gdip.Matrix_SetElements(getHandle(initialZoom), 1, 0, 0, 1, 0, 0);
}

/**
 * Modifies the receiver such that the matrix it represents becomes
 * the mathematical inverse of the matrix it previously represented.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_CANNOT_INVERT_MATRIX - if the matrix is not invertible</li>
 * </ul>
 */
public void invert() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (Gdip.Matrix_Invert(getHandle(initialZoom)) != 0) SWT.error(SWT.ERROR_CANNOT_INVERT_MATRIX);
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
	return zoomLevelToHandle.isEmpty();
}

/**
 * Returns <code>true</code> if the Transform represents the identity matrix
 * and false otherwise.
 *
 * @return <code>true</code> if the receiver is an identity Transform, and <code>false</code> otherwise
 */
public boolean isIdentity() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return Gdip.Matrix_IsIdentity(getHandle(initialZoom));
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
	Gdip.Matrix_Multiply(getHandle(initialZoom), matrix.getHandle(initialZoom), Gdip.MatrixOrderPrepend);
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
	Gdip.Matrix_Rotate(getHandle(initialZoom), angle, Gdip.MatrixOrderPrepend);
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
	Gdip.Matrix_Scale(getHandle(initialZoom), scaleX, scaleY, Gdip.MatrixOrderPrepend);
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
	Drawable drawable = getDevice();
	Gdip.Matrix_SetElements(getHandle(initialZoom), m11, m12, m21, m22, DPIUtil.scaleUp(drawable, dx, initialZoom), DPIUtil.scaleUp(drawable, dy, initialZoom));
}

/**
 * Modifies the receiver so that it represents a transformation that is
 * equivalent to its previous transformation sheared by (shearX, shearY).
 *
 * @param shearX the shear factor in the X direction
 * @param shearY the shear factor in the Y direction
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.4
 */
public void shear(float shearX, float shearY) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Gdip.Matrix_Shear(getHandle(initialZoom), shearX, shearY, Gdip.MatrixOrderPrepend);
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
	int length = pointArray.length;
	Drawable drawable = getDevice();
	for (int i = 0; i < length; i++) {
		pointArray[i] = DPIUtil.scaleUp(drawable, pointArray[i], initialZoom);
	}
	Gdip.Matrix_TransformPoints(getHandle(initialZoom), pointArray, length / 2);
	for (int i = 0; i < length; i++) {
		pointArray[i] = DPIUtil.scaleDown(drawable, pointArray[i], initialZoom);
	}
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
	Drawable drawable = getDevice();
	Gdip.Matrix_Translate(getHandle(initialZoom), DPIUtil.scaleUp(drawable, offsetX, initialZoom), DPIUtil.scaleUp(drawable, offsetY, initialZoom), Gdip.MatrixOrderPrepend);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString() {
	if (isDisposed()) return "Transform {*DISPOSED*}";
	float[] elements = new float[6];
	getElements(elements);
	return "Transform {" + elements [0] + "," + elements [1] + "," +elements [2] + "," +elements [3] + "," +elements [4] + "," +elements [5] + "}";
}

long getHandle(int zoomLevel) {
	if(zoomLevelToHandle.get(zoomLevel) == null) {
		float[] elements = new float[6];
		getElements(elements);
		elements[4] = DPIUtil.scaleUp(device, DPIUtil.scaleDown(device, elements[4], initialZoom), zoomLevel);
		elements[5] = DPIUtil.scaleUp(device, DPIUtil.scaleDown(device, elements[5], initialZoom), zoomLevel);

		zoomLevelToHandle.put(zoomLevel, Gdip.Matrix_new(elements[0], elements[1], elements[2], elements[3], elements[4], elements[5]));
	}
	return zoomLevelToHandle.get(zoomLevel);
}

}

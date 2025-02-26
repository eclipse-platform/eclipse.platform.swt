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
import java.util.function.*;

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

	private Map<Integer, TransformHandle> zoomToHandle = new HashMap<>();

	private List<Operation> operations = new ArrayList<>();

	private boolean isDestroyed;

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
	this.device.checkGDIP();
	storeAndApplyOperationForAllHandles(new SetElementsOperation(m11, m12, m21, m22, dx, dy));
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
	zoomToHandle.values().forEach(TransformHandle::destroy);
	zoomToHandle.clear();
	this.isDestroyed = true;
}

@Override
void destroyHandlesExcept(Set<Integer> zoomLevels) {
	// As long as we keep the operations, we can cleanup all handles
	zoomToHandle.values().forEach(TransformHandle::destroy);
	zoomToHandle.clear();
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
	applyUsingAnyHandle(transformHandle -> {
		Gdip.Matrix_GetElements(transformHandle.handle, elements);
		Drawable drawable = getDevice();
		int zoom = transformHandle.zoom;
		elements[4] = DPIUtil.scaleDown(drawable, elements[4], zoom);
		elements[5] = DPIUtil.scaleDown(drawable, elements[5], zoom);
		return true;
	});
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
	// identity invalidates all previous operations, so we remove them
	operations.clear();
	storeAndApplyOperationForAllHandles(new SetElementsOperation(1, 0, 0, 1, 0, 0));
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
	storeAndApplyOperationForAllHandles(new InvertOperation());
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
	return this.isDestroyed;
}

/**
 * Returns <code>true</code> if the Transform represents the identity matrix
 * and false otherwise.
 *
 * @return <code>true</code> if the receiver is an identity Transform, and <code>false</code> otherwise
 */
public boolean isIdentity() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return applyUsingAnyHandle(transformHandle -> {
		return Gdip.Matrix_IsIdentity(transformHandle.handle);
	});
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
	storeAndApplyOperationForAllHandles(new MultiplyOperation(matrix));
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
	storeAndApplyOperationForAllHandles(new RotateOperation(angle));
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
	storeAndApplyOperationForAllHandles(new ScaleOperation(scaleX, scaleY));
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
	// setElements invalidates all previous operations, so we remove them
	operations.clear();
	storeAndApplyOperationForAllHandles(new SetElementsOperation(m11, m12, m21, m22, dx, dy));
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
	storeAndApplyOperationForAllHandles(new ShearOperation(shearX, shearY));
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
	Drawable drawable = getDevice();
	applyUsingAnyHandle(transformHandle -> {
		int length = pointArray.length;
		for (int i = 0; i < length; i++) {
			pointArray[i] = DPIUtil.scaleUp(drawable, pointArray[i], transformHandle.zoom);
		}
		Gdip.Matrix_TransformPoints(transformHandle.handle, pointArray, length / 2);
		for (int i = 0; i < length; i++) {
			pointArray[i] = DPIUtil.scaleDown(drawable, pointArray[i], transformHandle.zoom);
		}
		return pointArray;
	});

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
	storeAndApplyOperationForAllHandles(new TranslateOperation(offsetX, offsetY));
}

private record TransformHandle(long handle, int zoom) {
	void destroy() {
		Gdip.Matrix_delete(handle);
	}
}

private record InvertOperation() implements Operation {
	@Override
	public void apply(TransformHandle transformHandle) {
		long handle = transformHandle.handle;
		if (Gdip.Matrix_Invert(handle) != 0) SWT.error(SWT.ERROR_CANNOT_INVERT_MATRIX);
	}
}

private class MultiplyOperation implements Operation {
	private final float[] elements;

	public MultiplyOperation(Transform matrix) {
		// As operation are executed on demand on new handles the passed matrix could
		// already be disposed. Therefore, we need to store the elements to restore
		// a temporary matrix for this operation on demand
		elements = new float[6];
		matrix.getElements(elements);
	}

	@Override
	public void apply(TransformHandle transformHandle) {
		long handle = transformHandle.handle;
		int zoom = transformHandle.zoom;
		long newHandle = Gdip.Matrix_new(elements[0], elements[1], elements[2], elements[3], DPIUtil.scaleUp(elements[4], zoom), DPIUtil.scaleUp(elements[5], zoom));
		if (newHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		try {
			Gdip.Matrix_Multiply(handle, newHandle, Gdip.MatrixOrderPrepend);
		} finally {
			Gdip.Matrix_delete(newHandle);
		}
	}
}

private record RotateOperation(float angle) implements Operation {
	@Override
	public void apply(TransformHandle transformHandle) {
		long handle = transformHandle.handle;
		Gdip.Matrix_Rotate(handle, angle, Gdip.MatrixOrderPrepend);
	}
}

private record ScaleOperation(float scaleX, float scaleY) implements Operation {
	@Override
	public void apply(TransformHandle transformHandle) {
		long handle = transformHandle.handle;
		Gdip.Matrix_Scale(handle, scaleX, scaleY, Gdip.MatrixOrderPrepend);
	}
}

private class SetElementsOperation implements Operation {
	private final float m11;
	private final float m12;
	private final float m21;
	private final float m22;
	private final float dx;
	private final float dy;

	public SetElementsOperation(float m11, float m12, float m21, float m22, float dx, float dy) {
		this.m11 = m11;
		this.m12 = m12;
		this.m21 = m21;
		this.m22 = m22;
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public void apply(TransformHandle transformHandle) {
		Drawable drawable = getDevice();
		long handle = transformHandle.handle;
		int zoom = transformHandle.zoom;
		Gdip.Matrix_SetElements(handle, m11, m12, m21, m22, DPIUtil.scaleUp(drawable, dx, zoom), DPIUtil.scaleUp(drawable, dy, zoom));
	}
}

private record ShearOperation(float shearX, float shearY) implements Operation {
	@Override
	public void apply(TransformHandle transformHandle) {
		long handle = transformHandle.handle;
		Gdip.Matrix_Shear(handle, shearX, shearY, Gdip.MatrixOrderPrepend);
	}
}

private class TranslateOperation implements Operation {
	private final float offsetX;
	private final float offsetY;

	public TranslateOperation(float offsetX, float offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	@Override
	public void apply(TransformHandle transformHandle) {
		Drawable drawable = getDevice();
		long handle = transformHandle.handle;
		int zoom = transformHandle.zoom;
		Gdip.Matrix_Translate(handle, DPIUtil.scaleUp(drawable, offsetX, zoom), DPIUtil.scaleUp(drawable, offsetY, zoom), Gdip.MatrixOrderPrepend);
	}
}

private interface Operation {
	void apply(TransformHandle transformHandle);
}

private void storeAndApplyOperationForAllHandles(Operation operation) {
	operations.add(operation);
	zoomToHandle.forEach((zoom, handle) -> operation.apply(handle));
}

private <T> T applyUsingAnyHandle(Function<TransformHandle, T> function) {
	if (zoomToHandle.isEmpty()) {
		TransformHandle temporaryHandle = newTransformHandle(DPIUtil.getDeviceZoom());
		try {
			return function.apply(temporaryHandle);
		} finally {
			temporaryHandle.destroy();
		}
	} else {
		return function.apply(zoomToHandle.values().iterator().next());
	}
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

private TransformHandle newTransformHandle(int zoom) {
	long newHandle = Gdip.Matrix_new(0, 0, 0, 0, 0, 0);
	if (newHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	TransformHandle newTransformHandle = new TransformHandle(newHandle, zoom);
	for(Operation operation : operations) {
		operation.apply(newTransformHandle);
	}
	return newTransformHandle;
}

private TransformHandle getTransformHandle(int zoom) {
	if (!zoomToHandle.containsKey(zoom)) {
		TransformHandle newHandle = newTransformHandle(zoom);
		zoomToHandle.put(zoom, newHandle);
		return newHandle;
	}
	return zoomToHandle.get(zoom);
}

long getHandle(int zoom) {
	return getTransformHandle(zoom).handle;
}
}

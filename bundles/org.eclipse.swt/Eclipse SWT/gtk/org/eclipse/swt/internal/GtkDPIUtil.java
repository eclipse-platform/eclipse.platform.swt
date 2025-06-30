/*******************************************************************************
 * Copyright (c) 2022 IBM Corporation and others.
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
 *     Daniel Kruegler - #420 - [High DPI] "swt.autoScale" should add new "half" option
 *     Yatta Solutions - #131 - Additional methods to specify target zoom directly
 *******************************************************************************/
package org.eclipse.swt.internal;

import org.eclipse.swt.graphics.*;

/**
 * This class hold common constants and utility functions w.r.t. to SWT high DPI
 * functionality.
 * <p>
 * The {@code autoScaleUp(..)} methods convert from API coordinates (in
 * SWT points) to internal high DPI coordinates (in pixels) that interface with
 * native widgets.
 * </p>
 * <p>
 * The {@code autoScaleDown(..)} convert from high DPI pixels to API coordinates
 * (in SWT points).
 * </p>
 *
 * @since 3.105
 */
public class GtkDPIUtil {

	/**
	 * Auto-scale down ImageData
	 */
	public static ImageData autoScaleDown (Device device, final ImageData imageData) {
		if (DPIUtil.deviceZoom == 100 || imageData == null || (device != null && !device.isAutoScalable())) return imageData;
		float scaleFactor = 1.0f;
		return DPIUtil.autoScaleImageData(device, imageData, scaleFactor);
	}

	public static int[] autoScaleDown(int[] pointArray) {
		if (DPIUtil.deviceZoom == 100 || pointArray == null) return pointArray;
		int [] returnArray = new int[pointArray.length];
		for (int i = 0; i < pointArray.length; i++) {
			returnArray [i] =  Math.round (pointArray [i]);
		}
		return returnArray;
	}

	public static int[] autoScaleDown(Drawable drawable, int[] pointArray) {
		if (drawable != null && !drawable.isAutoScalable ()) return pointArray;
		return autoScaleDown (pointArray);
	}

	/**
	 * Auto-scale down float array dimensions if enabled for Drawable class.
	 */
	public static float[] autoScaleDown(Drawable drawable, float size[]) {
		return DPIUtil.scaleDown(drawable, size, DPIUtil.deviceZoom);
	}

	/**
	 * Auto-scale down float array dimensions.
	 */
	public static float[] autoScaleDown(float size[]) {
		return DPIUtil.scaleDown(size, DPIUtil.deviceZoom);
	}

	/**
	 * Auto-scale down int dimensions.
	 */
	public static int autoScaleDown(int size) {
		return DPIUtil.scaleDown(size, DPIUtil.deviceZoom);
	}

	/**
	 * Auto-scale down int dimensions if enabled for Drawable class.
	 */
	public static int autoScaleDown(Drawable drawable, int size) {
		return DPIUtil.scaleDown(drawable, size, DPIUtil.deviceZoom);
	}

	/**
	 * Auto-scale down float dimensions.
	 */
	public static float autoScaleDown(float size) {
		return DPIUtil.scaleDown(size, DPIUtil.deviceZoom);
	}

	/**
	 * Auto-scale down float dimensions if enabled for Drawable class.
	 */
	public static float autoScaleDown(Drawable drawable, float size) {
		return DPIUtil.scaleDown (drawable, size, DPIUtil.deviceZoom);
	}

	/**
	 * Returns a new scaled down Point.
	 */
	public static Point autoScaleDown(Point point) {
		return DPIUtil.scaleDown(point, DPIUtil.deviceZoom);
	}

	/**
	 * Returns a new scaled down Point if enabled for Drawable class.
	 */
	public static Point autoScaleDown(Drawable drawable, Point point) {
		return DPIUtil.scaleDown(drawable, point, DPIUtil.deviceZoom);
	}

	/**
	 * Returns a new scaled down Rectangle.
	 */
	public static Rectangle autoScaleDown(Rectangle rect) {
		return DPIUtil.scaleDown(rect, DPIUtil.deviceZoom);
	}

	/**
	 * Returns a new scaled down Rectangle if enabled for Drawable class.
	 */
	public static Rectangle autoScaleDown(Drawable drawable, Rectangle rect) {
		if (drawable != null && !drawable.isAutoScalable()) return rect;
		return DPIUtil.scaleDown(rect, DPIUtil.deviceZoom);
	}

	/**
	 * Auto-scale up ImageData to device zoom that is at 100%.
	 */
	public static ImageData autoScaleUp (Device device, final ImageData imageData) {
		return autoScaleImageData(device, imageData, 100);
	}

	public static int[] autoScaleUp(int[] pointArray) {
		return DPIUtil.scaleUp(pointArray, DPIUtil.deviceZoom);
	}

	public static int[] autoScaleUp(Drawable drawable, int[] pointArray) {
		return DPIUtil.scaleUp(drawable, pointArray, DPIUtil.deviceZoom);
	}

	/**
	 * Auto-scale up int dimensions.
	 */
	public static int autoScaleUp(int size) {
		return DPIUtil.scaleUp(size, DPIUtil.deviceZoom);
	}

	/**
	 * Auto-scale up int dimensions if enabled for Drawable class.
	 */
	public static int autoScaleUp(Drawable drawable, int size) {
		return DPIUtil.scaleUp(drawable, size, DPIUtil.deviceZoom);
	}

	public static float autoScaleUp(float size) {
		return DPIUtil.scaleUp(size, DPIUtil.deviceZoom);
	}

	public static float autoScaleUp(Drawable drawable, float size) {
		return DPIUtil.scaleUp(drawable, size, DPIUtil.deviceZoom);
	}

	/**
	 * Returns a new scaled up Point.
	 */
	public static Point autoScaleUp(Point point) {
		return DPIUtil.scaleUp(point, DPIUtil.deviceZoom);
	}

	/**
	 * Returns a new scaled up Point if enabled for Drawable class.
	 */
	public static Point autoScaleUp(Drawable drawable, Point point) {
		return DPIUtil.scaleUp (drawable, point, DPIUtil.deviceZoom);
	}

	/**
	 * Returns a new scaled up Rectangle.
	 */
	public static Rectangle autoScaleUp(Rectangle rect) {
		return DPIUtil.scaleUp(rect, DPIUtil.deviceZoom);
	}

	/**
	 * Returns a new scaled up Rectangle if enabled for Drawable class.
	 */
	public static Rectangle autoScaleUp(Drawable drawable, Rectangle rect) {
		return DPIUtil.scaleUp(drawable, rect, DPIUtil.deviceZoom);
	}

	/**
	 * Auto-scale ImageData to device zoom that are at given zoom factor.
	 */
	public static ImageData autoScaleImageData (Device device, final ImageData imageData, int imageDataZoomFactor) {
		if (DPIUtil.deviceZoom == imageDataZoomFactor || imageData == null || (device != null && !device.isAutoScalable())) return imageData;
		float scaleFactor = (float) DPIUtil.deviceZoom / imageDataZoomFactor;
		return DPIUtil.autoScaleImageData(device, imageData, scaleFactor);
	}

}

/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * This class hold common constants and utility functions w.r.t. to SWT high DPI
 * functionality.
 *
 * @since 3.105
 */
public class DPIUtil {

	/* DPI Constants */
	static final int DPI_ZOOM_200 = 192;
	static final int DPI_ZOOM_150 = 144;
	static final int DPI_ZOOM_100 = 96;

	private static boolean autoScale = true;
	private static int deviceZoom = 100;

/**
 * Auto-scale down ImageData
 */
public static ImageData autoScaleDown (ImageData imageData) {
	if (!getAutoScale () || imageData == null) return imageData;
	float scaleFactor = getScalingFactor ();
	return scaleFactor == 1 ? imageData
			: imageData.scaledTo (Math.round ((float)imageData.width / scaleFactor), Math.round ((float)imageData.height / scaleFactor));
}

public static int[] autoScaleUp(int[] pointArray) {
	if (!getAutoScale ()) return pointArray;
	float scaleFactor = getScalingFactor ();
	int [] returnArray = new int[pointArray.length];
	for (int i = 0; i < pointArray.length; i++) {
		returnArray [i] =  Math.round (pointArray [i] * scaleFactor);
	}
	return returnArray;
}
public static int[] autoScaleDown(int[] pointArray) {
	if (!getAutoScale ()) return pointArray;
	float scaleFactor = getScalingFactor ();
	int [] returnArray = new int[pointArray.length];
	for (int i = 0; i < pointArray.length; i++) {
		returnArray [i] =  Math.round (pointArray [i] / scaleFactor);
	}
	return returnArray;
}
/**
 * Auto-scale down int dimensions.
 */
public static int autoScaleDown (int size) {
	if (!getAutoScale ()) return size;
	float scaleFactor = getScalingFactor ();
	return Math.round (size / scaleFactor);
}
/**
 * Auto-scale down float dimensions.
 */
public static float autoScaleDown (float size) {
	if (!getAutoScale ()) return size;
	float scaleFactor = getScalingFactor ();
	return (size / scaleFactor);
}

/**
 * Returns a new scaled down Point.
 */
public static Point autoScaleDown (Point point) {
	if (!getAutoScale () || point == null) return point;
	float scaleFactor = getScalingFactor ();
	if (scaleFactor == 1) return point;
	Point scaledPoint = new Point (0,0);
	scaledPoint.x = Math.round (point.x / scaleFactor);
	scaledPoint.y = Math.round (point.y / scaleFactor);
	return scaledPoint;
}

/**
 * Returns a new scaled down Rectangle.
 */
public static Rectangle autoScaleDown (Rectangle rect) {
	if (!getAutoScale () || rect == null) return rect;
	float scaleFactor = getScalingFactor ();
	if (scaleFactor == 1) return rect;
	Rectangle scaledRect = new Rectangle (0,0,0,0);
	scaledRect.x = Math.round (rect.x / scaleFactor);
	scaledRect.y = Math.round (rect.y / scaleFactor);
	scaledRect.width = Math.round (rect.width / scaleFactor);
	scaledRect.height = Math.round (rect.height / scaleFactor);
	return scaledRect;
}

/**
 * Auto-scale image with ImageData
 */
public static ImageData autoScaleImageData (ImageData imageData, int targetZoom, int currentZoom) {
	if (!getAutoScale () || imageData == null || targetZoom == currentZoom) return imageData;
	float scaleFactor = ((float) targetZoom)/((float) currentZoom);
	return imageData.scaledTo (Math.round ((float)imageData.width * scaleFactor), Math.round ((float)imageData.height * scaleFactor));
}

/**
 * Auto-scale up ImageData
 */
public static ImageData autoScaleUp (ImageData imageData) {
	if (!getAutoScale () || imageData == null) return imageData;
	float scaleFactor = getScalingFactor ();
	return scaleFactor == 1 ? imageData
			: imageData.scaledTo (Math.round ((float)imageData.width * scaleFactor), Math.round ((float)imageData.height * scaleFactor));
}

/**
 * Auto-scale up int dimensions.
 */
public static int autoScaleUp (int size) {
	if (!getAutoScale ()||size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor ();
	return Math.round (size * scaleFactor);
}

/**
 * Returns a new scaled up Point.
 */
public static Point autoScaleUp (Point point) {
	if (!getAutoScale () || point == null) return point;
	float scaleFactor = getScalingFactor ();
	if (scaleFactor == 1) return point;
	Point scaledPoint = new Point (0,0);
	scaledPoint.x = Math.round (point.x * scaleFactor);
	scaledPoint.y = Math.round (point.y * scaleFactor);
	return scaledPoint;
}

/**
 * Returns a new scaled up Rectangle.
 */
public static Rectangle autoScaleUp (Rectangle rect) {
	if (!getAutoScale () || rect == null) return rect;
	float scaleFactor = getScalingFactor ();
	if (scaleFactor == 1) return rect;
	Rectangle scaledRect = new Rectangle (0,0,0,0);
	scaledRect.x = Math.round (rect.x * scaleFactor);
	scaledRect.y = Math.round (rect.y * scaleFactor);
	scaledRect.width = Math.round (rect.width * scaleFactor);
	scaledRect.height = Math.round (rect.height * scaleFactor);
	return scaledRect;
}
public static boolean getAutoScale () {
	return autoScale;
}

/**
 * Returns Scaling factor from the display
 * @return float scaling factor
 */
private static float getScalingFactor () {
	float scalingFactor = 1;
	if (getAutoScale ()) {
		scalingFactor = getDeviceZoom ()/100f;
	}
	return scalingFactor;
}

/**
 * Compute the zoom value based on the scaleFactor value.
 *
 * @return zoom
 */
public static int mapSFToZoom (float scaleFactor) {
	return mapDPIToZoom ((int) (scaleFactor * DPI_ZOOM_100));
}
/**
 * Compute the zoom value based on the DPI value.
 *
 * @return zoom
 */
public static int mapDPIToZoom (int dpi) {
	int zoom;
	if (dpi >= DPI_ZOOM_200) {
		zoom = 200;
	} else if (dpi >= DPI_ZOOM_150) {
		zoom = 150;
	} else {
		zoom = 100;
	}
	return zoom;
}
/**
 * Returns a new rectangle as per the scaleFactor.
 */
static Rectangle scale (Rectangle rect, int targetZoom, int currentZoom) {
	if (rect == null || targetZoom == currentZoom) return rect;
	float scaleFactor = ((float)targetZoom) / (float)currentZoom;
	Rectangle returnRect = new Rectangle (0,0,0,0);
	returnRect.x = Math.round (rect.x * scaleFactor);
	returnRect.y = Math.round (rect.y * scaleFactor);
	returnRect.width = Math.round (rect.width * scaleFactor);
	returnRect.height = Math.round (rect.height * scaleFactor);
	return returnRect;
}

/**
 * Gets Image data at specified zoom level, if image is missing then
 * fall-back to 100% image. If provider or fall-back image is not available,
 * throw error.
 */
public static ImageData validateAndGetImageDataAtZoom (ImageDataProvider provider, int zoom, boolean[] found) {
	if (provider == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	ImageData data = provider.getImageData (zoom);
	found [0] = (data != null);
	/* If image is null when (zoom != 100%), fall-back to image at 100% zoom */
	if (zoom != 100 && !found [0]) data = provider.getImageData (100);
	if (data == null) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	return data;
}

/**
 * Gets Image file path at specified zoom level, if image is missing then
 * fall-back to 100% image. If provider or fall-back image is not available,
 * throw error.
 */
public static String validateAndGetImagePathAtZoom (ImageFileNameProvider provider, int zoom, boolean[] found) {
	if (provider == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	String filename = provider.getImagePath (zoom);
	found [0] = (filename != null);
	/* If image is null when (zoom != 100%), fall-back to image at 100% zoom */
	if (zoom != 100 && !found [0]) filename = provider.getImagePath (100);
	if (filename == null) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	return filename;
}

public static float autoScaleUp(float size) {
	if (!getAutoScale ()||size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor ();
	return (size * scaleFactor);
}

/**
 * @return the deviceZoom
 */
public static int getDeviceZoom() {
	return deviceZoom;
}

/**
 * @param deviceZoom the deviceZoom to set
 */
public static void setDeviceZoom(int deviceZoom) {
	DPIUtil.deviceZoom = deviceZoom;
}

}

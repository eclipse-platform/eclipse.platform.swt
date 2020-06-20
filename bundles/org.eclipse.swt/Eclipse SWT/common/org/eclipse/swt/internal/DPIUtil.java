/*******************************************************************************
 * Copyright (c) 2017 IBM Corporation and others.
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
package org.eclipse.swt.internal;

import org.eclipse.swt.*;
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
public class DPIUtil {

	private static final int DPI_ZOOM_100 = 96;

	private static int deviceZoom = 100;
	private static int nativeDeviceZoom = 100;

	private static enum AutoScaleMethod { AUTO, NEAREST, SMOOTH }
	private static AutoScaleMethod autoScaleMethodSetting = AutoScaleMethod.AUTO;
	private static AutoScaleMethod autoScaleMethod = AutoScaleMethod.NEAREST;

	private static String autoScaleValue;
	private static boolean useCairoAutoScale = false;

	/**
	 * System property that controls the autoScale functionality.
	 * <ul>
	 * <li><b>false</b>: deviceZoom is set to 100%</li>
	 * <li><b>integer</b>: deviceZoom depends on the current display resolution,
	 *     but only uses integer multiples of 100%. The detected native zoom is
	 *     generally rounded down (e.g. at 150%, will use 100%), unless close to
	 *     the next integer multiple (currently at 175%, will use 200%).</li>
	 * <li><b>integer200</b>: like <b>integer</b>, but the maximal zoom level is 200%.</li>
	 * <li><b>quarter</b>: deviceZoom depends on the current display resolution,
	 *     but only uses integer multiples of 25%. The detected native zoom is
	 *     rounded to the closest permissible value.</li>
	 * <li><b>exact</b>: deviceZoom uses the native zoom (with 1% as minimal
	 *     step).</li>
	 * <li><i>&lt;value&gt;</i>: deviceZoom uses the given integer value in
	 *     percent as zoom level.</li>
	 * </ul>
	 * The current default is "integer200".
	 */
	private static final String SWT_AUTOSCALE = "swt.autoScale";

	/**
	 * System property that controls the method for scaling images:
	 * <ul>
	 * <li>"nearest": nearest-neighbor interpolation, may look jagged</li>
	 * <li>"smooth": smooth edges, may look blurry</li>
	 * </ul>
	 * The current default is to use "nearest", except on
	 * GTK when the deviceZoom is not an integer multiple of 100%.
	 * The smooth strategy currently doesn't work on Win32 and Cocoa, see
	 * <a href="https://bugs.eclipse.org/493455">bug 493455</a>.
	 */
	private static final String SWT_AUTOSCALE_METHOD = "swt.autoScale.method";
	static {
		autoScaleValue = System.getProperty (SWT_AUTOSCALE);

		String value = System.getProperty (SWT_AUTOSCALE_METHOD);
		if (value != null) {
			if (AutoScaleMethod.NEAREST.name().equalsIgnoreCase(value)) {
				autoScaleMethod = autoScaleMethodSetting = AutoScaleMethod.NEAREST;
			} else if (AutoScaleMethod.SMOOTH.name().equalsIgnoreCase(value)) {
				autoScaleMethod = autoScaleMethodSetting = AutoScaleMethod.SMOOTH;
			}
		}
	}

/**
 * Auto-scale down ImageData
 */
public static ImageData autoScaleDown (Device device, final ImageData imageData) {
	if (deviceZoom == 100 || imageData == null || (device != null && !device.isAutoScalable())) return imageData;
	float scaleFactor = 1.0f / getScalingFactor ();
	return autoScaleImageData(device, imageData, scaleFactor);
}

public static int[] autoScaleDown(int[] pointArray) {
	if (deviceZoom == 100 || pointArray == null) return pointArray;
	float scaleFactor = getScalingFactor ();
	int [] returnArray = new int[pointArray.length];
	for (int i = 0; i < pointArray.length; i++) {
		returnArray [i] =  Math.round (pointArray [i] / scaleFactor);
	}
	return returnArray;
}

public static int[] autoScaleDown(Drawable drawable, int[] pointArray) {
	if (drawable != null && !drawable.isAutoScalable ()) return pointArray;
	return autoScaleDown (pointArray);
}

/**
 * Auto-scale down float array dimensions.
 */
public static float[] autoScaleDown (float size[]) {
	if (deviceZoom == 100 || size == null) return size;
	float scaleFactor = getScalingFactor ();
	float scaledSize[] = new float[size.length];
	for (int i = 0; i < scaledSize.length; i++) {
		scaledSize[i] = size[i] / scaleFactor;
	}
	return scaledSize;
}

/**
 * Auto-scale down float array dimensions if enabled for Drawable class.
 */
public static float[] autoScaleDown (Drawable drawable, float size[]) {
	if (drawable != null && !drawable.isAutoScalable ()) return size;
	return autoScaleDown (size);
}

/**
 * Auto-scale down int dimensions.
 */
public static int autoScaleDown (int size) {
	if (deviceZoom == 100 || size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor ();
	return Math.round (size / scaleFactor);
}
/**
 * Auto-scale down int dimensions if enabled for Drawable class.
 */
public static int autoScaleDown (Drawable drawable, int size) {
	if (drawable != null && !drawable.isAutoScalable ()) return size;
	return autoScaleDown (size);
}

/**
 * Auto-scale down float dimensions.
 */
public static float autoScaleDown (float size) {
	if (deviceZoom == 100 || size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor ();
	return (size / scaleFactor);
}

/**
 * Auto-scale down float dimensions if enabled for Drawable class.
 */
public static float autoScaleDown (Drawable drawable, float size) {
	if (drawable != null && !drawable.isAutoScalable ()) return size;
	return autoScaleDown (size);
}

/**
 * Returns a new scaled down Point.
 */
public static Point autoScaleDown (Point point) {
	if (deviceZoom == 100 || point == null) return point;
	float scaleFactor = getScalingFactor ();
	Point scaledPoint = new Point (0,0);
	scaledPoint.x = Math.round (point.x / scaleFactor);
	scaledPoint.y = Math.round (point.y / scaleFactor);
	return scaledPoint;
}

/**
 * Returns a new scaled down Point if enabled for Drawable class.
 */
public static Point autoScaleDown (Drawable drawable, Point point) {
	if (drawable != null && !drawable.isAutoScalable ()) return point;
	return autoScaleDown (point);
}

/**
 * Returns a new scaled down Rectangle.
 */
public static Rectangle autoScaleDown (Rectangle rect) {
	if (deviceZoom == 100 || rect == null) return rect;
	Rectangle scaledRect = new Rectangle (0,0,0,0);
	Point scaledTopLeft = DPIUtil.autoScaleDown (new Point (rect.x, rect.y));
	Point scaledBottomRight = DPIUtil.autoScaleDown (new Point (rect.x + rect.width, rect.y + rect.height));

	scaledRect.x = scaledTopLeft.x;
	scaledRect.y = scaledTopLeft.y;
	scaledRect.width = scaledBottomRight.x - scaledTopLeft.x;
	scaledRect.height = scaledBottomRight.y - scaledTopLeft.y;
	return scaledRect;
}
/**
 * Returns a new scaled down Rectangle if enabled for Drawable class.
 */
public static Rectangle autoScaleDown (Drawable drawable, Rectangle rect) {
	if (drawable != null && !drawable.isAutoScalable ()) return rect;
	return autoScaleDown (rect);
}

/**
 * Auto-scale image with ImageData
 */
public static ImageData autoScaleImageData (Device device, final ImageData imageData, int targetZoom, int currentZoom) {
	if (imageData == null || targetZoom == currentZoom || (device != null && !device.isAutoScalable())) return imageData;
	float scaleFactor = (float) targetZoom / (float) currentZoom;
	return autoScaleImageData(device, imageData, scaleFactor);
}

private static ImageData autoScaleImageData (Device device, final ImageData imageData, float scaleFactor) {
	// Guards are already implemented in callers: if (deviceZoom == 100 || imageData == null || scaleFactor == 1.0f) return imageData;
	int width = imageData.width;
	int height = imageData.height;
	int scaledWidth = Math.round ((float) width * scaleFactor);
	int scaledHeight = Math.round ((float) height * scaleFactor);
	switch (autoScaleMethod) {
	case SMOOTH:
		Image original = new Image (device, (ImageDataProvider) zoom -> imageData);

		/* Create a 24 bit image data with alpha channel */
		final ImageData resultData = new ImageData (scaledWidth, scaledHeight, 24, new PaletteData (0xFF, 0xFF00, 0xFF0000));
		resultData.alphaData = new byte [scaledWidth * scaledHeight];

		Image resultImage = new Image (device, (ImageDataProvider) zoom -> resultData);
		GC gc = new GC (resultImage);
		gc.setAntialias (SWT.ON);
		gc.drawImage (original, 0, 0, DPIUtil.autoScaleDown (width), DPIUtil.autoScaleDown (height),
				/* E.g. destWidth here is effectively DPIUtil.autoScaleDown (scaledWidth), but avoiding rounding errors.
				 * Nevertheless, we still have some rounding errors due to the point-based API GC#drawImage(..).
				 */
				0, 0, Math.round (DPIUtil.autoScaleDown ((float) width * scaleFactor)), Math.round (DPIUtil.autoScaleDown ((float) height * scaleFactor)));
		gc.dispose ();
		original.dispose ();
		ImageData result = resultImage.getImageData (DPIUtil.getDeviceZoom ());
		resultImage.dispose ();
		return result;
	case NEAREST:
	default:
		return imageData.scaledTo (scaledWidth, scaledHeight);
	}
}

/**
 * Returns a new rectangle as per the scaleFactor.
 */
public static Rectangle autoScaleBounds (Rectangle rect, int targetZoom, int currentZoom) {
	if (deviceZoom == 100 || rect == null || targetZoom == currentZoom) return rect;
	float scaleFactor = ((float)targetZoom) / (float)currentZoom;
	Rectangle returnRect = new Rectangle (0,0,0,0);
	returnRect.x = Math.round (rect.x * scaleFactor);
	returnRect.y = Math.round (rect.y * scaleFactor);
	returnRect.width = Math.round (rect.width * scaleFactor);
	returnRect.height = Math.round (rect.height * scaleFactor);
	return returnRect;
}

/**
 * Auto-scale up ImageData
 */
public static ImageData autoScaleUp (Device device, final ImageData imageData) {
	if (deviceZoom == 100 || imageData == null || (device != null && !device.isAutoScalable())) return imageData;
	float scaleFactor = deviceZoom / 100f;
	return autoScaleImageData(device, imageData, scaleFactor);
}

public static int[] autoScaleUp(int[] pointArray) {
	if (deviceZoom == 100 || pointArray == null) return pointArray;
	float scaleFactor = getScalingFactor ();
	int [] returnArray = new int[pointArray.length];
	for (int i = 0; i < pointArray.length; i++) {
		returnArray [i] =  Math.round (pointArray [i] * scaleFactor);
	}
	return returnArray;
}

public static int[] autoScaleUp(Drawable drawable, int[] pointArray) {
	if (drawable != null && !drawable.isAutoScalable ()) return pointArray;
	return autoScaleUp (pointArray);
}

/**
 * Auto-scale up int dimensions.
 */
public static int autoScaleUp (int size) {
	if (deviceZoom == 100 || size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor ();
	return Math.round (size * scaleFactor);
}

/**
 * Auto-scale up int dimensions using Native DPI
 */
public static int autoScaleUpUsingNativeDPI (int size) {
	if (nativeDeviceZoom == 100 || size == SWT.DEFAULT) return size;
	float nativeScaleFactor = nativeDeviceZoom / 100f;
	return Math.round (size * nativeScaleFactor);
}

/**
 * Auto-scale up int dimensions if enabled for Drawable class.
 */
public static int autoScaleUp (Drawable drawable, int size) {
	if (drawable != null && !drawable.isAutoScalable ()) return size;
	return autoScaleUp (size);
}

public static float autoScaleUp(float size) {
	if (deviceZoom == 100 || size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor ();
	return (size * scaleFactor);
}

public static float autoScaleUp(Drawable drawable, float size) {
	if (drawable != null && !drawable.isAutoScalable ()) return size;
	return autoScaleUp (size);
}

/**
 * Returns a new scaled up Point.
 */
public static Point autoScaleUp (Point point) {
	if (deviceZoom == 100 || point == null) return point;
	float scaleFactor = getScalingFactor ();
	Point scaledPoint = new Point (0,0);
	scaledPoint.x = Math.round (point.x * scaleFactor);
	scaledPoint.y = Math.round (point.y * scaleFactor);
	return scaledPoint;
}

/**
 * Returns a new scaled up Point if enabled for Drawable class.
 */
public static Point autoScaleUp (Drawable drawable, Point point) {
	if (drawable != null && !drawable.isAutoScalable ()) return point;
	return autoScaleUp (point);
}

/**
 * Returns a new scaled up Rectangle.
 */
public static Rectangle autoScaleUp (Rectangle rect) {
	if (deviceZoom == 100 || rect == null) return rect;
	Rectangle scaledRect = new Rectangle (0,0,0,0);
	Point scaledTopLeft = DPIUtil.autoScaleUp (new Point (rect.x, rect.y));
	Point scaledBottomRight = DPIUtil.autoScaleUp (new Point (rect.x + rect.width, rect.y + rect.height));

	scaledRect.x = scaledTopLeft.x;
	scaledRect.y = scaledTopLeft.y;
	scaledRect.width = scaledBottomRight.x - scaledTopLeft.x;
	scaledRect.height = scaledBottomRight.y - scaledTopLeft.y;
	return scaledRect;
}

/**
 * Returns a new scaled up Rectangle if enabled for Drawable class.
 */
public static Rectangle autoScaleUp (Drawable drawable, Rectangle rect) {
	if (drawable != null && !drawable.isAutoScalable ()) return rect;
	return autoScaleUp (rect);
}

/**
 * Returns Scaling factor from the display
 * @return float scaling factor
 */
private static float getScalingFactor () {
	if (useCairoAutoScale) {
		return 1;
	}
	return deviceZoom / 100f;
}

/**
 * Compute the zoom value based on the DPI value.
 *
 * @return zoom
 */
public static int mapDPIToZoom (int dpi) {
	double zoom = (double) dpi * 100 / DPI_ZOOM_100;
	int roundedZoom = (int) Math.round (zoom);
	return roundedZoom;
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
	if (data == null) SWT.error (SWT.ERROR_INVALID_ARGUMENT, null, ": ImageDataProvider [" + provider + "] returns null ImageData at 100% zoom.");
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
	if (filename == null) SWT.error (SWT.ERROR_INVALID_ARGUMENT, null, ": ImageFileNameProvider [" + provider + "] returns null filename at 100% zoom.");
	return filename;
}

public static int getDeviceZoom() {
	return deviceZoom;
}

public static void setDeviceZoom (int nativeDeviceZoom) {
	DPIUtil.nativeDeviceZoom = nativeDeviceZoom;
	int deviceZoom = getZoomForAutoscaleProperty (nativeDeviceZoom);

	DPIUtil.deviceZoom = deviceZoom;
	System.setProperty("org.eclipse.swt.internal.deviceZoom", Integer.toString(deviceZoom));
	if (deviceZoom != 100 && autoScaleMethodSetting == AutoScaleMethod.AUTO) {
		if (deviceZoom / 100 * 100 == deviceZoom || !"gtk".equals(SWT.getPlatform())) {
			autoScaleMethod = AutoScaleMethod.NEAREST;
		} else {
			autoScaleMethod = AutoScaleMethod.SMOOTH;
		}
	}
}

public static void setUseCairoAutoScale (boolean cairoAutoScale) {
	useCairoAutoScale = cairoAutoScale;
}

public static boolean useCairoAutoScale() {
	return useCairoAutoScale;
}

public static int getZoomForAutoscaleProperty (int nativeDeviceZoom) {
	int zoom = 0;
	if (autoScaleValue != null) {
		if ("false".equalsIgnoreCase (autoScaleValue)) {
			zoom = 100;
		} else if ("quarter".equalsIgnoreCase (autoScaleValue)) {
			zoom = (int) (Math.round (nativeDeviceZoom / 25f) * 25);
		} else if ("exact".equalsIgnoreCase (autoScaleValue)) {
			zoom = nativeDeviceZoom;
		} else {
			try {
				int zoomValue = Integer.parseInt (autoScaleValue);
				zoom = Math.max (Math.min (zoomValue, 1600), 25);
			} catch (NumberFormatException e) {
				// unsupported value, use default
			}
		}
	}
	if (zoom == 0) { // || "integer".equalsIgnoreCase (value) || "integer200".equalsIgnoreCase (value)
		zoom = Math.max ((nativeDeviceZoom + 25) / 100 * 100, 100);
		if (!"integer".equalsIgnoreCase(autoScaleValue)) {
			// integer200, or default
			zoom = Math.min (zoom, 200);
		}
	}
	return zoom;
}

/**
 * AutoScale ImageDataProvider.
 */
public static final class AutoScaleImageDataProvider implements ImageDataProvider {
	Device device;
	ImageData imageData;
	int currentZoom;
	public AutoScaleImageDataProvider(Device device, ImageData data, int zoom){
		this.device = device;
		this.imageData = data;
		this.currentZoom = zoom;
	}
	@Override
	public ImageData getImageData(int zoom) {
		return DPIUtil.autoScaleImageData(device, imageData, zoom, currentZoom);
	}
}
}

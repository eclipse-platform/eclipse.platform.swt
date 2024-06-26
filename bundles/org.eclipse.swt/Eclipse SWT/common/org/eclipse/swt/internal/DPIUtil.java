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

import java.util.function.*;

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
	private static boolean autoScaleOnRuntime = false;

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
	 * <li><b>half</b>: deviceZoom depends on the current display resolution,
	 *     but only uses integer multiples of 50%. The detected native zoom is
	 *     rounded to the closest permissible value, with tie-breaker towards even.</li>
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

	/**
	 * System property to enable to scale the application on runtime
	 * when a DPI change is detected.
	 * <ul>
	 * <li>"true": the application is scaled on DPI changes</li>
	 * <li>"false": the application will remain in its initial scaling</li>
	 * </ul>
	 * <b>Important:</b> This flag is only parsed and used on Win32. Setting it to
	 * true on GTK or cocoa will be ignored.
	 */
	private static final String SWT_AUTOSCALE_UPDATE_ON_RUNTIME = "swt.autoScale.updateOnRuntime";
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

		if ("win32".equals(SWT.getPlatform())) {
			String updateOnRuntimeValue = System.getProperty (SWT_AUTOSCALE_UPDATE_ON_RUNTIME);
			autoScaleOnRuntime = Boolean.parseBoolean(updateOnRuntimeValue);
		}
	}

/**
 * Auto-scale down ImageData
 */
public static ImageData autoScaleDown (Device device, final ImageData imageData) {
	if (deviceZoom == 100 || imageData == null || (device != null && !device.isAutoScalable())) return imageData;
	float scaleFactor = 1.0f / getScalingFactor (deviceZoom);
	return autoScaleImageData(device, imageData, scaleFactor);
}

public static int[] autoScaleDown(int[] pointArray) {
	if (deviceZoom == 100 || pointArray == null) return pointArray;
	float scaleFactor = getScalingFactor (deviceZoom);
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
public static float[] autoScaleDown(float size[]) {
	return scaleDown(size, deviceZoom);
}

public static float[] scaleDown(float size[], int zoom) {
	if (zoom == 100 || size == null) return size;
	float scaleFactor = getScalingFactor (zoom);
	float scaledSize[] = new float[size.length];
	for (int i = 0; i < scaledSize.length; i++) {
		scaledSize[i] = size[i] / scaleFactor;
	}
	return scaledSize;
}

/**
 * Auto-scale down float array dimensions if enabled for Drawable class.
 */
public static float[] autoScaleDown(Drawable drawable, float size[]) {
	return scaleDown(drawable, size, deviceZoom);
}

public static float[] scaleDown(Drawable drawable, float size[], int zoom) {
	if (drawable != null && !drawable.isAutoScalable()) return size;
	return scaleDown(size, zoom);
}

/**
 * Auto-scale down int dimensions.
 */
public static int autoScaleDown(int size) {
	return scaleDown(size, deviceZoom);
}

public static int scaleDown(int size, int zoom) {
	if (zoom == 100 || size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor (zoom);
	return Math.round (size / scaleFactor);
}

/**
 * Auto-scale down int dimensions if enabled for Drawable class.
 */
public static int autoScaleDown(Drawable drawable, int size) {
	return scaleDown(drawable, size, deviceZoom);
}

public static int scaleDown(Drawable drawable, int size, int zoom) {
	if (drawable != null && !drawable.isAutoScalable()) return size;
	return scaleDown (size, zoom);
}

/**
 * Auto-scale down float dimensions.
 */
public static float autoScaleDown(float size) {
	return scaleDown(size, deviceZoom);
}

public static float scaleDown(float size, int zoom) {
	if (zoom == 100 || size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor (zoom);
	return (size / scaleFactor);
}

/**
 * Auto-scale down float dimensions if enabled for Drawable class.
 */
public static float autoScaleDown(Drawable drawable, float size) {
	return scaleDown (drawable, size, deviceZoom);
}

public static float scaleDown(Drawable drawable, float size, int zoom) {
	if (drawable != null && !drawable.isAutoScalable()) return size;
	return scaleDown (size, zoom);
}

/**
 * Returns a new scaled down Point.
 */
public static Point autoScaleDown(Point point) {
	return scaleDown(point, deviceZoom);
}

public static Point scaleDown(Point point, int zoom) {
	if (zoom == 100 || point == null) return point;
	float scaleFactor = getScalingFactor(zoom);
	Point scaledPoint = new Point (0,0);
	scaledPoint.x = Math.round (point.x / scaleFactor);
	scaledPoint.y = Math.round (point.y / scaleFactor);
	return scaledPoint;
}

/**
 * Returns a new scaled down Point if enabled for Drawable class.
 */
public static Point autoScaleDown(Drawable drawable, Point point) {
	return scaleDown(drawable, point, deviceZoom);
}

public static Point scaleDown(Drawable drawable, Point point, int zoom) {
	if (drawable != null && !drawable.isAutoScalable()) return point;
	return scaleDown (point, zoom);
}

/**
 * Returns a new scaled down Rectangle.
 */
public static Rectangle autoScaleDown(Rectangle rect) {
	return scaleDown(rect, deviceZoom);
}

public static Rectangle scaleDown(Rectangle rect, int zoom) {
	if (zoom == 100 || rect == null) return rect;
	Rectangle scaledRect = new Rectangle (0,0,0,0);
	Point scaledTopLeft = DPIUtil.scaleDown(new Point (rect.x, rect.y), zoom);
	Point scaledBottomRight = DPIUtil.scaleDown(new Point (rect.x + rect.width, rect.y + rect.height), zoom);

	scaledRect.x = scaledTopLeft.x;
	scaledRect.y = scaledTopLeft.y;
	scaledRect.width = scaledBottomRight.x - scaledTopLeft.x;
	scaledRect.height = scaledBottomRight.y - scaledTopLeft.y;
	return scaledRect;
}
/**
 * Returns a new scaled down Rectangle if enabled for Drawable class.
 */
public static Rectangle autoScaleDown(Drawable drawable, Rectangle rect) {
	if (drawable != null && !drawable.isAutoScalable()) return rect;
	return scaleDown(rect, deviceZoom);
}

public static Rectangle scaleDown(Drawable drawable, Rectangle rect, int zoom) {
	if (drawable != null && !drawable.isAutoScalable()) return rect;
	return scaleDown (rect, zoom);
}

/**
 * Auto-scale image with ImageData
 */
public static ImageData autoScaleImageData (Device device, final ImageData imageData, int targetZoom, int currentZoom) {
	if (imageData == null || targetZoom == currentZoom || (device != null && !device.isAutoScalable())) return imageData;
	float scaleFactor = (float) targetZoom / (float) currentZoom;
	return autoScaleImageData(device, imageData, scaleFactor);
}


public static ImageData autoScaleImageData (Device device, final ElementAtZoom<ImageData> elementAtZoom, int targetZoom) {
	return autoScaleImageData(device, elementAtZoom.element(), targetZoom, elementAtZoom.zoom());
}

private static ImageData autoScaleImageData (Device device, final ImageData imageData, float scaleFactor) {
	// Guards are already implemented in callers: if (deviceZoom == 100 || imageData == null || scaleFactor == 1.0f) return imageData;
	int width = imageData.width;
	int height = imageData.height;
	int scaledWidth = Math.round (width * scaleFactor);
	int scaledHeight = Math.round (height * scaleFactor);
	return switch (autoScaleMethod) {
	case SMOOTH -> {
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
				0, 0, Math.round (DPIUtil.autoScaleDown (width * scaleFactor)), Math.round (DPIUtil.autoScaleDown (height * scaleFactor)));
		gc.dispose ();
		original.dispose ();
		ImageData result = resultImage.getImageData (DPIUtil.getDeviceZoom ());
		resultImage.dispose ();
		yield result;
	}
	default -> imageData.scaledTo (scaledWidth, scaledHeight);
	};
}

/**
 * Returns a new rectangle as per the scaleFactor.
 */
public static Rectangle autoScaleBounds (Rectangle rect, int targetZoom, int currentZoom) {
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
 * Auto-scale ImageData to device zoom that are at given zoom factor.
 */
public static ImageData autoScaleImageData (Device device, final ImageData imageData, int imageDataZoomFactor) {
	if (deviceZoom == imageDataZoomFactor || imageData == null || (device != null && !device.isAutoScalable())) return imageData;
	float scaleFactor = (float) deviceZoom / imageDataZoomFactor;
	return autoScaleImageData(device, imageData, scaleFactor);
}

/**
 * Auto-scale up ImageData to device zoom that is at 100%.
 */
public static ImageData autoScaleUp (Device device, final ImageData imageData) {
	return autoScaleImageData(device, imageData, 100);
}

public static ImageData autoScaleUp (Device device, final ElementAtZoom<ImageData> elementAtZoom) {
	return autoScaleImageData(device, elementAtZoom.element(), elementAtZoom.zoom());
}

public static int[] autoScaleUp(int[] pointArray) {
	return autoScaleUp(pointArray, deviceZoom);
}

public static int[] autoScaleUp(int[] pointArray, int zoom) {
	if (zoom == 100 || pointArray == null) return pointArray;
	float scaleFactor = getScalingFactor(zoom);
	int[] returnArray = new int[pointArray.length];
	for (int i = 0; i < pointArray.length; i++) {
		returnArray [i] =  Math.round (pointArray [i] * scaleFactor);
	}
	return returnArray;
}

public static int[] autoScaleUp(Drawable drawable, int[] pointArray) {
	return autoScaleUp(drawable, pointArray, deviceZoom);
}

public static int[] autoScaleUp(Drawable drawable, int[] pointArray, int zoom) {
	if (drawable != null && !drawable.isAutoScalable()) return pointArray;
	return autoScaleUp (pointArray, zoom);
}
/**
 * Auto-scale up int dimensions.
 */
public static int autoScaleUp(int size) {
	return autoScaleUp(size, deviceZoom);
}

/**
 * Auto-scale up int dimensions to match the given zoom level
 */
public static int autoScaleUp(int size, int zoom) {
	if (zoom == 100 || size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor(zoom);
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
public static int autoScaleUp(Drawable drawable, int size) {
	return autoScaleUp(drawable, size, deviceZoom);
}

public static int autoScaleUp(Drawable drawable, int size, int zoom) {
	if (drawable != null && !drawable.isAutoScalable()) return size;
	return autoScaleUp (size, zoom);
}

public static float autoScaleUp(float size) {
	return autoScaleUp(size, deviceZoom);
}

public static float autoScaleUp(float size, int zoom) {
	if (zoom == 100 || size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor(zoom);
	return (size * scaleFactor);
}

public static float autoScaleUp(Drawable drawable, float size) {
	return autoScaleUp(drawable, size, deviceZoom);
}

public static float autoScaleUp(Drawable drawable, float size, int zoom) {
	if (drawable != null && !drawable.isAutoScalable()) return size;
	return autoScaleUp (size, zoom);
}

/**
 * Returns a new scaled up Point.
 */
public static Point autoScaleUp(Point point) {
	return autoScaleUp(point, deviceZoom);
}

public static Point autoScaleUp(Point point, int zoom) {
	if (zoom == 100 || point == null) return point;
	float scaleFactor = getScalingFactor(zoom);
	Point scaledPoint = new Point(0,0);
	scaledPoint.x = Math.round (point.x * scaleFactor);
	scaledPoint.y = Math.round (point.y * scaleFactor);
	return scaledPoint;
}

/**
 * Returns a new scaled up Point if enabled for Drawable class.
 */
public static Point autoScaleUp(Drawable drawable, Point point) {
	return autoScaleUp (drawable, point, deviceZoom);
}

public static Point autoScaleUp(Drawable drawable, Point point, int zoom) {
	if (drawable != null && !drawable.isAutoScalable()) return point;
	return autoScaleUp (point, zoom);
}

/**
 * Returns a new scaled up Rectangle.
 */
public static Rectangle autoScaleUp(Rectangle rect) {
	return autoScaleUp(rect, deviceZoom);
}

public static Rectangle autoScaleUp(Rectangle rect, int zoom) {
	if (zoom == 100 || rect == null) return rect;
	Rectangle scaledRect = new Rectangle(0,0,0,0);
	Point scaledTopLeft = DPIUtil.autoScaleUp (new Point(rect.x, rect.y), zoom);
	Point scaledBottomRight = DPIUtil.autoScaleUp (new Point(rect.x + rect.width, rect.y + rect.height), zoom);

	scaledRect.x = scaledTopLeft.x;
	scaledRect.y = scaledTopLeft.y;
	scaledRect.width = scaledBottomRight.x - scaledTopLeft.x;
	scaledRect.height = scaledBottomRight.y - scaledTopLeft.y;
	return scaledRect;
}

/**
 * Returns a new scaled up Rectangle if enabled for Drawable class.
 */
public static Rectangle autoScaleUp(Drawable drawable, Rectangle rect) {
	return autoScaleUp(drawable, rect, deviceZoom);
}

public static Rectangle autoScaleUp(Drawable drawable, Rectangle rect, int zoom) {
	if (drawable != null && !drawable.isAutoScalable()) return rect;
	return autoScaleUp (rect, zoom);
}

/**
 * Returns scaling factor from the given device zoom
 * @return float scaling factor
 */
private static float getScalingFactor(int zoom) {
	if (useCairoAutoScale) {
		return 1;
	}
	if (zoom <= 0) {
		zoom = deviceZoom;
	}
	return zoom / 100f;
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
 * Compute the DPI value value based on the zoom.
 *
 * @return DPI
 */
public static int mapZoomToDPI (int zoom) {
	double dpi = (double) zoom / 100 * DPI_ZOOM_100;
	int roundedDpi = (int) Math.round (dpi);
	return roundedDpi;
}

/**
 * Represents an element, such as some image data, at a specific zoom level.
 *
 * @param <T> type of the element to be presented, e.g., {@link ImageData}
 */
public record ElementAtZoom<T>(T element, int zoom) {
}

/**
 * Gets ImageData that are appropriate for the specified zoom level together
 * with the zoom level at which the image data are. If there is an image at the
 * specified zoom level, it is returned. Otherwise the next larger image at 150%
 * and 200% is returned, if existing. If none of these is found, the 100% image
 * is returned as a fallback. If provider or fallback image is not available, an
 * error is thrown.
 */
public static ElementAtZoom<ImageData> validateAndGetImageDataAtZoom(ImageDataProvider provider, int zoom) {
	if (provider == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	ElementAtZoom<ImageData> imageDataAtZoom = getElementAtZoom(z -> provider.getImageData(z), zoom);
	if (imageDataAtZoom == null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT, null,
				": ImageDataProvider [" + provider + "] returns null ImageData at 100% zoom.");
	}
	return imageDataAtZoom;
}

/**
 * Gets the image file path that are appropriate for the specified zoom level
 * together with the zoom level at which the image data are. If there is an
 * image at the specified zoom level, it is returned. Otherwise the next larger
 * image at 150% and 200% is returned, if existing. If none of these is found,
 * the 100% image is returned as a fallback. If provider or fallback image is
 * not available, an error is thrown.
 */
public static ElementAtZoom<String> validateAndGetImagePathAtZoom(ImageFileNameProvider provider, int zoom) {
	if (provider == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	ElementAtZoom<String> imagePathAtZoom = getElementAtZoom(z -> provider.getImagePath(z), zoom);
	if (imagePathAtZoom == null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT, null,
				": ImageFileNameProvider [" + provider + "] returns null filename at 100% zoom.");
	}
	return imagePathAtZoom;
}

private static <T> ElementAtZoom<T> getElementAtZoom(Function<Integer, T> elementForZoomProvider, int zoom) {
	T dataAtOriginalZoom = elementForZoomProvider.apply(zoom);
	if (dataAtOriginalZoom != null) {
		return new ElementAtZoom<>(dataAtOriginalZoom, zoom);
	}
	if (zoom > 100 && zoom <= 150) {
		T dataAt150Percent = elementForZoomProvider.apply(150);
		if (dataAt150Percent != null) {
			return new ElementAtZoom<>(dataAt150Percent, 150);
		}
	}
	if (zoom > 100) {
		T dataAt200Percent = elementForZoomProvider.apply(200);
		if (dataAt200Percent != null) {
			return new ElementAtZoom<>(dataAt200Percent, 200);
		}
	}
	if (zoom != 100) {
		T dataAt100Percent = elementForZoomProvider.apply(100);
		if (dataAt100Percent != null) {
			return new ElementAtZoom<>(dataAt100Percent, 100);
		}
	}
	return null;
}

public static int getNativeDeviceZoom() {
	return nativeDeviceZoom;
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
		} else if ("half".equalsIgnoreCase (autoScaleValue)) {
			// Math.round rounds 125->150 and 175->200,
			// Math.rint rounds 125->100 and 175->200 matching
			// "integer200"
			zoom = (int) Math.rint(nativeDeviceZoom / 50d) * 50;
		} else if ("quarter".equalsIgnoreCase (autoScaleValue)) {
			zoom = Math.round(nativeDeviceZoom / 25f) * 25;
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
	}
	return zoom;
}

public static boolean isAutoScaleOnRuntimeActive() {
	return autoScaleOnRuntime;
}

static boolean setAutoScaleOnRuntimeActive(boolean value) {
	return autoScaleOnRuntime = value;
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
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

import java.util.*;
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

	private static enum AutoScaleMethod { AUTO, NEAREST, SMOOTH;

		public static Optional<AutoScaleMethod> forString(String s) {
			for (AutoScaleMethod v : values()) {
				if (v.name().equalsIgnoreCase(s)) {
					return Optional.of(v);
				}
			}
			return Optional.empty();
		}

	}
	private static final AutoScaleMethod AUTO_SCALE_METHOD_SETTING;
	private static AutoScaleMethod autoScaleMethod;

	private static String autoScaleValue;

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
	 * The current default is to use "smooth" on GTK when deviceZoom is an integer
	 * multiple of 100% and on Windows if monitor-specific scaling is enabled, and
	 * "nearest" otherwise..
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
		AUTO_SCALE_METHOD_SETTING = AutoScaleMethod.forString(value).orElse(AutoScaleMethod.AUTO);
		autoScaleMethod = AUTO_SCALE_METHOD_SETTING != AutoScaleMethod.AUTO ? AUTO_SCALE_METHOD_SETTING : AutoScaleMethod.NEAREST;
	}


public static int pixelToPoint(int size, int zoom) {
	if (zoom == 100 || size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor (zoom);
	return Math.round (size / scaleFactor);
}


public static float pixelToPoint(float size, int zoom) {
	if (zoom == 100 || size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor (zoom);
	return (size / scaleFactor);
}


/**
 * Auto-scale image with ImageData
 */
public static ImageData scaleImageData (Device device, final ImageData imageData, int targetZoom, int currentZoom) {
	if (imageData == null || targetZoom == currentZoom || (device != null && !device.isAutoScalable())) return imageData;
	float scaleFactor = (float) targetZoom / (float) currentZoom;
	return autoScaleImageData(device, imageData, scaleFactor);
}


public static ImageData scaleImageData (Device device, final ElementAtZoom<ImageData> elementAtZoom, int targetZoom) {
	return scaleImageData(device, elementAtZoom.element(), targetZoom, elementAtZoom.zoom());
}

public static ImageData autoScaleImageData (Device device, final ImageData imageData, float scaleFactor) {
	// Guards are already implemented in callers: if (deviceZoom == 100 || imageData == null || scaleFactor == 1.0f) return imageData;
	int width = imageData.width;
	int height = imageData.height;
	int scaledWidth = Math.round (width * scaleFactor);
	int scaledHeight = Math.round (height * scaleFactor);
	int defaultZoomLevel = 100;
	boolean useSmoothScaling = isSmoothScalingEnabled() && imageData.getTransparencyType() != SWT.TRANSPARENCY_MASK;
	if (useSmoothScaling) {
		ImageGcDrawer drawer =  new ImageGcDrawer() {
			@Override
			public void drawOn(GC gc, int imageWidth, int imageHeight) {
				gc.setAntialias (SWT.ON);
				Image.drawScaled(gc, imageData, width, height, scaleFactor);
			};

			@Override
			public int getGcStyle() {
				return SWT.TRANSPARENT;
			}
		};
		Image resultImage = new Image (device, drawer, scaledWidth, scaledHeight);
		ImageData result = resultImage.getImageData (defaultZoomLevel);
		resultImage.dispose ();
		return result;
	} else {
		return imageData.scaledTo (scaledWidth, scaledHeight);
	}
}

public static boolean isSmoothScalingEnabled() {
	return autoScaleMethod == AutoScaleMethod.SMOOTH;
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
 * Returns scaling factor from the given device zoom
 * @return float scaling factor
 */
public static float getScalingFactor(int zoom) {
	return getScalingFactor(zoom, 100);
}


public static float getScalingFactor(int targetZoom, int currentZoom) {
	if (targetZoom <= 0) {
		targetZoom = deviceZoom;
	}
	return targetZoom / (float) currentZoom;
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
	public ElementAtZoom {
		if (element == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (zoom <= 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
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

	// in GTK, preserve the current method when switching to a 100% monitor
	boolean preserveScalingMethod = SWT.getPlatform().equals("gtk") && deviceZoom == 100;
	if (!preserveScalingMethod && AUTO_SCALE_METHOD_SETTING == AutoScaleMethod.AUTO) {
		if (sholdUseSmoothScaling()) {
			autoScaleMethod = AutoScaleMethod.SMOOTH;
		} else {
			autoScaleMethod = AutoScaleMethod.NEAREST;
		}
	}
}

private static boolean sholdUseSmoothScaling() {
	return switch (SWT.getPlatform()) {
	case "gtk" -> deviceZoom / 100 * 100 != deviceZoom;
	case "win32" -> isMonitorSpecificScalingActive();
	default -> false;
	};
}

public static int getZoomForAutoscaleProperty (int nativeDeviceZoom) {
	return getZoomForAutoscaleProperty(nativeDeviceZoom, autoScaleValue);
}

private static int getZoomForAutoscaleProperty (int nativeDeviceZoom, String autoScaleValue) {
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

public static void runWithAutoScaleValue(String autoScaleValue, Runnable runnable) {
	String initialAutoScaleValue = DPIUtil.autoScaleValue;
	DPIUtil.autoScaleValue = autoScaleValue;
	DPIUtil.deviceZoom = getZoomForAutoscaleProperty(nativeDeviceZoom);
	try {
		runnable.run();
	} finally {
		DPIUtil.autoScaleValue = initialAutoScaleValue;
		DPIUtil.deviceZoom = getZoomForAutoscaleProperty(nativeDeviceZoom);
	}
}

public static void setMonitorSpecificScaling(boolean activate) {
	System.setProperty(SWT_AUTOSCALE_UPDATE_ON_RUNTIME, Boolean.toString(activate));
}

public static boolean isMonitorSpecificScalingActive() {
	boolean updateOnRuntimeValue = Boolean.getBoolean (SWT_AUTOSCALE_UPDATE_ON_RUNTIME);
	return updateOnRuntimeValue;
}

public static void setAutoScaleForMonitorSpecificScaling() {
	boolean isDefaultAutoScale = autoScaleValue == null;
	if (isDefaultAutoScale) {
		autoScaleValue = "quarter";
	} else if (!isSupportedAutoScaleForMonitorSpecificScaling()) {
		throw new SWTError(SWT.ERROR_NOT_IMPLEMENTED,
				"monitor-specific scaling is only implemented for auto-scale values \"quarter\", \"exact\", \"false\" or a concrete zoom value, but \""
						+ autoScaleValue + "\" has been specified");
	}
}

/**
 * Monitor-specific scaling on Windows only supports auto-scale modes in which
 * all elements (font, images, control bounds etc.) are scaled equally or almost
 * equally. The previously default mode "integer"/"integer200", which rounded
 * the scale factor for everything but fonts to multiples of 100, is complex and
 * difficult to realize with monitor-specific rescaling of UI elements. Since a
 * uniform scale factor for everything should perspectively be used anyway,
 * there will be support for complex auto-scale modes for monitor-specific
 * scaling.
 *
 * The supported modes are "quarter" and "exact" or explicit zoom values given
 * by the value itself or "false". Every other value will be treated as
 * "integer"/"integer200" and is thus not supported.
 */
private static boolean isSupportedAutoScaleForMonitorSpecificScaling() {
	if (autoScaleValue == null) {
		return false;
	}
	switch (autoScaleValue.toLowerCase()) {
		case "false", "quarter", "exact": return true;
	}
	try {
		Integer.parseInt(autoScaleValue);
		return true;
	} catch (NumberFormatException e) {
		// unsupported value, use default
	}
	return false;
}

}
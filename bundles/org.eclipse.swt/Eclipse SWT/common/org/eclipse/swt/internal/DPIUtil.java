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
import org.eclipse.swt.internal.AutoScaleCalculation.*;

/**
 * This class hold common constants and utility functions w.r.t. to SWT high DPI
 * functionality.
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

	private static AutoScale autoScaleValue;

	/**
	 * System property to enable to scale the application on runtime when a DPI
	 * change is detected.
	 * <ul>
	 * <li>"force": the application is scaled on DPI changes even if an unsupported
	 * value for swt.autoScale are defined. See allowed values in
	 * {@link AutoScaleCalculation}.</li>
	 * <li>"true": the application is scaled on DPI changes</li>
	 * <li>"false": the application will remain in its initial scaling</li>
	 * </ul>
	 * <b>Important:</b> This flag is only parsed and used on Win32. Setting it to
	 * true on GTK or cocoa will be ignored.
	 */
	static final String SWT_AUTOSCALE_UPDATE_ON_RUNTIME = "swt.autoScale.updateOnRuntime";

	/**
	 * System property that controls the autoScale functionality.
	 * <ul>
	 * <li><b>false</b>: deviceZoom is set to 100%</li>
	 * <li><b>integer</b>: deviceZoom depends on the current display resolution,
	 *     but only uses integer multiples of 100%. The detected native zoom is
	 *     generally rounded down (e.g. at 150%, will use 100%), unless close to
	 *     the next integer multiple (currently at 175%, will use 200%).</li>
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
	 * The current default is "integer".
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

	static {
		updateAutoScaleValue();

		String value = System.getProperty (SWT_AUTOSCALE_METHOD);
		AUTO_SCALE_METHOD_SETTING = AutoScaleMethod.forString(value).orElse(AutoScaleMethod.AUTO);
		autoScaleMethod = AUTO_SCALE_METHOD_SETTING != AutoScaleMethod.AUTO ? AUTO_SCALE_METHOD_SETTING : AutoScaleMethod.NEAREST;
	}


private static void updateAutoScaleValue() {
	String autoScaleProperty = System.getProperty (SWT_AUTOSCALE);
	autoScaleValue = AutoScaleCalculation.parseFrom(autoScaleProperty);
	if (DPIUtil.isMonitorSpecificScalingActive() && !DPIUtil.isSetupCompatibleToMonitorSpecificScaling()) {
		throw new SWTError(SWT.ERROR_NOT_IMPLEMENTED,
				"monitor-specific scaling is only implemented for auto-scale values \"quarter\", \"exact\", but \""
						+ autoScaleProperty + "\" has been specified");
	}
}

/**
 * Returns {@code true} only if the current setup is compatible
 * with monitor-specific scaling. Returns {@code false} if:
 * <ul>
 *   <li>Not running on Windows</li>
 *   <li>The current auto-scale mode is incompatible</li>
 * </ul>
 *
 * The supported auto-scale modes are "quarter" and "exact" or explicit zoom values given
 * by the value itself or "false". Every other value will be treated as
 * "integer" and is thus not supported.
 *
 * <p>
 * <b>Background information:</b>
 * Monitor-specific scaling on Windows only supports auto-scale modes in which
 * all elements (font, images, control bounds etc.) are scaled equally or almost
 * equally. The previously default mode "integer", which rounded
 * the scale factor for everything but fonts to multiples of 100, is complex and
 * difficult to realize with monitor-specific rescaling of UI elements. Since a
 * uniform scale factor for everything should perspectively be used anyway,
 * there will be no support for complex auto-scale modes for monitor-specific
 * scaling.
 */
public static boolean isSetupCompatibleToMonitorSpecificScaling() {
	if (!"win32".equals(SWT.getPlatform())) {
		return false;
	}
	if (System.getProperty(SWT_AUTOSCALE) == null || "force".equals(System.getProperty(SWT_AUTOSCALE_UPDATE_ON_RUNTIME))) {
		return true;
	}
	return autoScaleValue.isCompatibleToMonitorSpecificScaling();
}

public static void setMonitorSpecificScaling(boolean activate) {
	System.setProperty(DPIUtil.SWT_AUTOSCALE_UPDATE_ON_RUNTIME, Boolean.toString(activate));
	updateAutoScaleValue();
}

public static boolean isMonitorSpecificScalingActive() {
	if (!"win32".equals(SWT.getPlatform())) {
		return false;
	}
	String updateOnRuntimeValue = System.getProperty(SWT_AUTOSCALE_UPDATE_ON_RUNTIME);
	return "force".equalsIgnoreCase(updateOnRuntimeValue) || "true".equalsIgnoreCase(updateOnRuntimeValue);
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
	return scaleImage(device, imageData, Image::drawAtSize, scaledWidth, scaledHeight);
}

@FunctionalInterface
private interface ImageDrawFunction {
	void draw(GC gc, ImageData imageData, int width, int height);
}

private static ImageData scaleImage(Device device, final ImageData imageData, ImageDrawFunction drawFunction,
		int scaledWidth, int scaledHeight) {
	int defaultZoomLevel = 100;
	boolean useSmoothScaling = isSmoothScalingEnabled() && imageData.getTransparencyType() != SWT.TRANSPARENCY_MASK;
	if (useSmoothScaling) {
		ImageGcDrawer drawer =  new ImageGcDrawer() {
			@Override
			public void drawOn(GC gc, int imageWidth, int imageHeight) {
				gc.setAntialias (SWT.ON);
				drawFunction.draw(gc, imageData, imageWidth, imageHeight);
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

public static ImageData autoScaleImageData(Device device, final ImageData imageData, int targetWidth, int targetHeight) {
	return scaleImage(device, imageData, Image::drawAtSize, targetWidth, targetHeight);
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

public static void validateLinearScaling(ImageDataProvider provider) {
	final int baseZoom = 100;
	final int scaledZoom = 200;
	final int scaleFactor = scaledZoom / baseZoom;
	ImageData baseImageData = provider.getImageData(baseZoom);
	ImageData scaledImageData = provider.getImageData(scaledZoom);

	if (scaledImageData == null) {
		return;
	}

	if (scaledImageData.width != scaleFactor * baseImageData.width
			|| scaledImageData.height != scaleFactor * baseImageData.height) {
		System.err.println(String.format(
				"***WARNING: ImageData should be linearly scaled across zooms but size is (%d, %d) at 100%% and (%d, %d) at 200%%.",
				baseImageData.width, baseImageData.height, scaledImageData.width, scaledImageData.height));
		new Error().printStackTrace(System.err);
	}
}

public static int pointToPixel(int size, int zoom) {
	if (zoom == 100 || size == SWT.DEFAULT) return size;
	float scaleFactor = getScalingFactor(zoom);
	return Math.round (size * scaleFactor);
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

/**
 * Returns the native zoom of the UI and its controls according to the adapted
 * OS elements. Particularly on Windows, this is the zoom used for fonts by the
 * OS, no matter what autoscaling of SWT might be applied on top.
 * <p>
 * <b>Warning:</b> When using monitor-specific scaling on Windows, this value
 * may not be reasonable as there is no single zoom for the application but one
 * per shell and its monitor. In that case, this method will return the native
 * zoom for the shell whose zoom has last been changed (via moving the shell to
 * another monitor or changing the zoom of a monitor).
 */
public static int getNativeDeviceZoom() {
	return nativeDeviceZoom;
}

/**
 * Returns the zoom of the UI and its controls according to SWT autoscaling
 * settings. It is an adapted value of {@link #getNativeDeviceZoom()} based on
 * the setting of the {@code swt.autoScale} property.
 * <p>
 * <b>Warning:</b> When using monitor-specific scaling on Windows, this value
 * may not be reasonable as there is no single zoom for the application but one
 * per shell and its monitor. In that case, this method will return the
 * autoscaling zoom for the shell whose zoom has last been changed (via moving
 * the shell to another monitor or changing the zoom of a monitor).
 */
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
		if (shouldUseSmoothScaling()) {
			autoScaleMethod = AutoScaleMethod.SMOOTH;
		} else {
			autoScaleMethod = AutoScaleMethod.NEAREST;
		}
	}
}

private static boolean shouldUseSmoothScaling() {
	if ("gtk".equals(SWT.getPlatform())) {
		return DPIUtil.getDeviceZoom() / 100 * 100 != DPIUtil.getDeviceZoom();
	}
	return isMonitorSpecificScalingActive();
}

public static int getZoomForAutoscaleProperty (int nativeDeviceZoom) {
	return autoScaleValue.getAutoScaledZoom(nativeDeviceZoom);
}

public static void runWithAutoScaleValue(String autoScaleValue, Runnable runnable) {
	AutoScale initialAutoScaleValue = DPIUtil.autoScaleValue;
	// This method is used to adapt to the autoscale value used by the Equinox launcher,
	// which currently defaults to "integer". So we need to use explicit "integer" default here
	// until the Equinox launcher is adapted.
	DPIUtil.autoScaleValue = AutoScaleCalculation.parseFrom(autoScaleValue == null ? "integer" : autoScaleValue);
	DPIUtil.deviceZoom = getZoomForAutoscaleProperty(nativeDeviceZoom);
	try {
		runnable.run();
	} finally {
		DPIUtil.autoScaleValue = initialAutoScaleValue;
		DPIUtil.deviceZoom = getZoomForAutoscaleProperty(nativeDeviceZoom);
	}
}

}
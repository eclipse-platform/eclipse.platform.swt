/*******************************************************************************
 * Copyright (c) 2025 Yatta Solutions and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.internal.win32.version.*;

/**
 * This class is used in the win32 implementation only to provide
 * DPI related utility methods.
 * <p>
 * <b>IMPORTANT:</b> This class is <em>not</em> part of the public
 * API for SWT. It is marked public only so that it can be shared
 * within the packages provided by SWT. It is not available on all
 * platforms, and should never be called from application code.
 * </p>
 * @noreference This class is not intended to be referenced by clients
 */
public class Win32DPIUtils {
	public static boolean setDPIAwareness(int desiredDpiAwareness) {
		if (desiredDpiAwareness == OS.GetThreadDpiAwarenessContext()) {
			return true;
		}
		if (desiredDpiAwareness == OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2) {
			// "Per Monitor V2" only available in more recent Windows version
			boolean perMonitorV2Available = OsVersion.IS_WIN10_1809;
			if (!perMonitorV2Available) {
				System.err.println("***WARNING: the OS version does not support DPI awareness mode PerMonitorV2.");
				return false;
			}
		}
		long setDpiAwarenessResult = OS.SetThreadDpiAwarenessContext(desiredDpiAwareness);
		if (setDpiAwarenessResult == 0L) {
			System.err.println("***WARNING: setting DPI awareness failed.");
			return false;
		}
		return true;
	}

	public static <T> T runWithProperDPIAwareness(Supplier<T> operation) {
		// refreshing is only necessary, when monitor specific scaling is active
		long previousDPIAwareness = OS.GetThreadDpiAwarenessContext();
		try {
			if (!setDPIAwareness(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2)) {
				// awareness was not changed, so no need to reset it
				previousDPIAwareness = 0;
			}
			return operation.get();
		} finally {
			if (previousDPIAwareness > 0) {
				OS.SetThreadDpiAwarenessContext(previousDPIAwareness);
			}
		}
	}

	public static float[] pixelToPoint(float size[], int zoom) {
		if (zoom == 100 || size == null) return size;
		float scaleFactor = DPIUtil.getScalingFactor (zoom);
		float scaledSize[] = new float[size.length];
		for (int i = 0; i < scaledSize.length; i++) {
			scaledSize[i] = size[i] / scaleFactor;
		}
		return scaledSize;
	}

	public static float[] pixelToPoint(Drawable drawable, float size[], int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return size;
		return pixelToPoint(size, zoom);
	}

	public static int pixelToPoint(Drawable drawable, int size, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return size;
		return DPIUtil.pixelToPoint (size, zoom);
	}

	public static float pixelToPoint(Drawable drawable, float size, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return size;
		return DPIUtil.pixelToPoint (size, zoom);
	}

	public static Point pixelToPoint(Point point, int zoom) {
		if (zoom == 100 || point == null) return point;
		Point.OfFloat fPoint = FloatAwareGeometryFactory.createFrom(point);
		float scaleFactor = DPIUtil.getScalingFactor(zoom);
		float scaledX = fPoint.getX() / scaleFactor;
		float scaledY = fPoint.getY() / scaleFactor;
		return new Point.OfFloat(scaledX, scaledY);
	}

	public static Point pixelToPoint(Drawable drawable, Point point, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return point;
		return pixelToPoint (point, zoom);
	}

	public static Rectangle pixelToPoint(Rectangle rect, int zoom) {
		if (zoom == 100 || rect == null) return rect;
		if (rect instanceof Rectangle.OfFloat rectOfFloat) return pixelToPoint(rectOfFloat, zoom);
		Rectangle scaledRect = new Rectangle.OfFloat (0,0,0,0);
		Point scaledTopLeft = pixelToPoint(new Point (rect.x, rect.y), zoom);
		Point scaledBottomRight = pixelToPoint(new Point (rect.x + rect.width, rect.y + rect.height), zoom);

		scaledRect.x = scaledTopLeft.x;
		scaledRect.y = scaledTopLeft.y;
		scaledRect.width = scaledBottomRight.x - scaledTopLeft.x;
		scaledRect.height = scaledBottomRight.y - scaledTopLeft.y;
		return scaledRect;
	}

	private static Rectangle pixelToPoint(Rectangle.OfFloat rect, int zoom) {
		return scaleBounds(rect, 100, zoom);
	}

	public static Rectangle pixelToPoint(Drawable drawable, Rectangle rect, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return rect;
		return pixelToPoint (rect, zoom);
	}

	/**
	 * Returns a new rectangle as per the scaleFactor.
	 */
	public static Rectangle scaleBounds (Rectangle rect, int targetZoom, int currentZoom) {
		if (rect == null || targetZoom == currentZoom) return rect;
		if (rect instanceof Rectangle.OfFloat rectOfFloat) return scaleBounds(rectOfFloat, targetZoom, currentZoom);
		float scaleFactor = ((float)targetZoom) / (float)currentZoom;
		Rectangle returnRect = new Rectangle.OfFloat (0,0,0,0);
		returnRect.x = Math.round (rect.x * scaleFactor);
		returnRect.y = Math.round (rect.y * scaleFactor);
		returnRect.width = Math.round (rect.width * scaleFactor);
		returnRect.height = Math.round (rect.height * scaleFactor);
		return returnRect;
	}

	/**
	 * Returns a new rectangle as per the scaleFactor.
	 */
	private static Rectangle scaleBounds (Rectangle.OfFloat rect, int targetZoom, int currentZoom) {
		if (rect == null || targetZoom == currentZoom) return rect;
		Rectangle.OfFloat fRect = FloatAwareGeometryFactory.createFrom(rect);
		float scaleFactor = DPIUtil.getScalingFactor(targetZoom, currentZoom);
		float scaledX = fRect.getX() * scaleFactor;
		float scaledY = fRect.getY() * scaleFactor;
		float scaledWidth = fRect.getWidth() * scaleFactor;
		float scaledHeight = fRect.getHeight() * scaleFactor;
		return new Rectangle.OfFloat(scaledX, scaledY, scaledWidth, scaledHeight);
	}

	public static int[] pointToPixel(int[] pointArray, int zoom) {
		if (zoom == 100 || pointArray == null) return pointArray;
		float scaleFactor = DPIUtil.getScalingFactor(zoom);
		int[] returnArray = new int[pointArray.length];
		for (int i = 0; i < pointArray.length; i++) {
			returnArray [i] =  Math.round (pointArray [i] * scaleFactor);
		}
		return returnArray;
	}

	public static int[] pointToPixel(Drawable drawable, int[] pointArray, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return pointArray;
		return pointToPixel (pointArray, zoom);
	}

	/**
	 * Auto-scale up int dimensions to match the given zoom level
	 */
	public static int pointToPixel(int size, int zoom) {
		if (zoom == 100 || size == SWT.DEFAULT) return size;
		float scaleFactor = DPIUtil.getScalingFactor(zoom);
		return Math.round (size * scaleFactor);
	}

	public static int pointToPixel(Drawable drawable, int size, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return size;
		return pointToPixel (size, zoom);
	}

	public static float pointToPixel(float size, int zoom) {
		if (zoom == 100 || size == SWT.DEFAULT) return size;
		float scaleFactor = DPIUtil.getScalingFactor(zoom);
		return (size * scaleFactor);
	}

	public static float pointToPixel(Drawable drawable, float size, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return size;
		return pointToPixel (size, zoom);
	}

	public static Point pointToPixel(Point point, int zoom) {
		if (zoom == 100 || point == null) return point;
		Point.OfFloat fPoint = FloatAwareGeometryFactory.createFrom(point);
		float scaleFactor = DPIUtil.getScalingFactor(zoom);
		float scaledX = fPoint.getX() * scaleFactor;
		float scaledY = fPoint.getY() * scaleFactor;
		return new Point.OfFloat(scaledX, scaledY);
	}

	public static Point pointToPixel(Drawable drawable, Point point, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return point;
		return pointToPixel (point, zoom);
	}

	public static Rectangle pointToPixel(Rectangle rect, int zoom) {
		if (zoom == 100 || rect == null) return rect;
		if (rect instanceof Rectangle.OfFloat rectOfFloat) return pointToPixel(rectOfFloat, zoom);
		Rectangle scaledRect = new Rectangle.OfFloat(0,0,0,0);
		Point scaledTopLeft = pointToPixel (new Point(rect.x, rect.y), zoom);
		Point scaledBottomRight = pointToPixel (new Point(rect.x + rect.width, rect.y + rect.height), zoom);

		scaledRect.x = scaledTopLeft.x;
		scaledRect.y = scaledTopLeft.y;
		scaledRect.width = scaledBottomRight.x - scaledTopLeft.x;
		scaledRect.height = scaledBottomRight.y - scaledTopLeft.y;
		return scaledRect;
	}

	private static Rectangle pointToPixel(Rectangle.OfFloat rect, int zoom) {
		return scaleBounds(rect, zoom, 100);
	}

	public static Rectangle pointToPixel(Drawable drawable, Rectangle rect, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return rect;
		return pointToPixel (rect, zoom);
	}

	/**
	 * Auto-scale image with ImageData
	 */
	public static ImageData scaleImageData (Device device, final ImageData imageData, int targetZoom, int currentZoom) {
		if (imageData == null || targetZoom == currentZoom || (device != null && !device.isAutoScalable())) return imageData;
		float scaleFactor = (float) targetZoom / (float) currentZoom;
		return DPIUtil.autoScaleImageData(device, imageData, scaleFactor);
	}

	public static ImageData scaleImageData (Device device, final DPIUtil.ElementAtZoom<ImageData> elementAtZoom, int targetZoom) {
		return scaleImageData(device, elementAtZoom.element(), targetZoom, elementAtZoom.zoom());
	}

	/**
	 * Gets ImageData that are appropriate for the specified zoom level together
	 * with the zoom level at which the image data are. If there is an image at the
	 * specified zoom level, it is returned. Otherwise the next larger image at 150%
	 * and 200% is returned, if existing. If none of these is found, the 100% image
	 * is returned as a fallback. If provider or fallback image is not available, an
	 * error is thrown.
	 */
	public static DPIUtil.ElementAtZoom<ImageData> validateAndGetImageDataAtZoom(ImageDataProvider provider, int zoom) {
		if (provider == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		DPIUtil.ElementAtZoom<ImageData> imageDataAtZoom = getElementAtZoom(z -> provider.getImageData(z), zoom);
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
	public static DPIUtil.ElementAtZoom<String> validateAndGetImagePathAtZoom(ImageFileNameProvider provider, int zoom) {
		if (provider == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		DPIUtil.ElementAtZoom<String> imagePathAtZoom = getElementAtZoom(z -> provider.getImagePath(z), zoom);
		if (imagePathAtZoom == null) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null,
					": ImageFileNameProvider [" + provider + "] returns null filename at 100% zoom.");
		}
		return imagePathAtZoom;
	}

	private static <T> DPIUtil.ElementAtZoom<T> getElementAtZoom(Function<Integer, T> elementForZoomProvider, int zoom) {
		T dataAtOriginalZoom = elementForZoomProvider.apply(zoom);
		if (dataAtOriginalZoom != null) {
			return new DPIUtil.ElementAtZoom<>(dataAtOriginalZoom, zoom);
		}
		if (zoom > 100 && zoom <= 150) {
			T dataAt150Percent = elementForZoomProvider.apply(150);
			if (dataAt150Percent != null) {
				return new DPIUtil.ElementAtZoom<>(dataAt150Percent, 150);
			}
		}
		if (zoom > 100) {
			T dataAt200Percent = elementForZoomProvider.apply(200);
			if (dataAt200Percent != null) {
				return new DPIUtil.ElementAtZoom<>(dataAt200Percent, 200);
			}
		}
		if (zoom != 100) {
			T dataAt100Percent = elementForZoomProvider.apply(100);
			if (dataAt100Percent != null) {
				return new DPIUtil.ElementAtZoom<>(dataAt100Percent, 100);
			}
		}
		return null;
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
			return scaleImageData(device, imageData, zoom, currentZoom);
		}
	}

	private class FloatAwareGeometryFactory {
		static Rectangle.OfFloat createFrom(Rectangle rectangle) {
			if (rectangle instanceof Rectangle.OfFloat) {
				return (Rectangle.OfFloat) rectangle;
			}
			return new Rectangle.OfFloat(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		}

		static Point.OfFloat createFrom(Point point) {
			if (point instanceof Point.OfFloat) {
				return (Point.OfFloat) point;
			}
			return new Point.OfFloat(point.x, point.y);
		}
	}
}

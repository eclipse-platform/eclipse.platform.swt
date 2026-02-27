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
import org.eclipse.swt.widgets.*;

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

	/**
	 * This property allows to customize the DPI awareness of the application's UI
	 * thread, such that different DPI awareness modes are supported via
	 * configuration and not only via the application's executable manifest.
	 * Supported modes are "System", "PerMonitor", and "PerMonitorV2".
	 */
	public static final String CUSTOM_DPI_AWARENESS_PROPERTY = "org.eclipse.swt.internal.win32.dpiAwareness";

	private static long customDpiAwareness = -1;

	public static boolean initializeCustomDpiAwareness() {
		String customDpiAwareness = System.getProperty(CUSTOM_DPI_AWARENESS_PROPERTY);
		if (customDpiAwareness != null) {
			switch (customDpiAwareness.toLowerCase()) {
			case "permonitorv2":
				setDPIAwareness(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2);
				return true;
			case "permonitor":
				setDPIAwareness(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE);
				return true;
			case "system":
				setDPIAwareness(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE);
				return true;
			default:
				System.err.println("Invalid DPI awareness specified: " + customDpiAwareness);
			}
		}
		return false;
	}

	public static boolean setDPIAwareness(long desiredDpiAwareness) {
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
		customDpiAwareness = desiredDpiAwareness;
		return true;
	}

	public static boolean hasProperDpiAwarenessForMonitorSpecificScaling() {
		return OS.AreDpiAwarenessContextsEqual(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2, OS.GetThreadDpiAwarenessContext());
	}

	public static <T> T runWithProperDPIAwareness(Display display, Supplier<T> operation) {
		if (customDpiAwareness == -1) {
			return operation.get();
		}
		long previousDPIAwareness = OS.GetThreadDpiAwarenessContext();
		try {
			if (!setDPIAwareness(customDpiAwareness)) {
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

	public static Point pixelToPointAsSize(Drawable drawable, Point point, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return point;
		return pixelToPointAsSize (point, zoom);
	}

	public static Point pixelToPointAsSufficientlyLargeSize(Drawable drawable, Point point, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return point;
		return pixelToPointAsSufficientlyLargeSize (point, zoom);
	}

	public static Point pixelToPointAsLocation(Drawable drawable, Point point, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return point;
		return pixelToPointAsLocation (point, zoom);
	}

	public static Point pixelToPointAsSize(Point point, int zoom) {
		return pixelToPoint(point, zoom, RoundingMode.ROUND);
	}

	public static Point pixelToPointAsLocation(Point point, int zoom) {
		return pixelToPoint(point, zoom, RoundingMode.ROUND);
	}

	public static Point pixelToPointAsSufficientlyLargeSize(Point point, int zoom) {
		return pixelToPoint(point, zoom, RoundingMode.UP);
	}

	private static Point pixelToPoint(Point point, int zoom, RoundingMode mode) {
		if (zoom == 100 || point == null) return point;
		Point.OfFloat floatPoint = Point.OfFloat.from(point);
		return pixelToPoint(new Point.OfFloat(floatPoint.getX(), floatPoint.getY(), mode), zoom);
	}

	private static Point.OfFloat pixelToPoint(Point.OfFloat point, int zoom) {
		Point.OfFloat scaledPoint = point.clone();
		float scaleFactor = DPIUtil.getScalingFactor(zoom);
		scaledPoint.setX(point.getX() / scaleFactor);
		scaledPoint.setY(point.getY() / scaleFactor);
		return scaledPoint;
	}

	public static Rectangle pixelToPoint(Rectangle rect, int zoom) {
		return pixelToPoint(rect, zoom, RoundingMode.ROUND);
	}

	public static Rectangle pixelToPointWithSufficientlyLargeSize(Rectangle rect, int zoom) {
		return pixelToPoint(rect, zoom, RoundingMode.UP);
	}

	private static Rectangle pixelToPoint(Rectangle rect, int zoom, RoundingMode sizeRounding) {
		if (zoom == 100 || rect == null) return rect;
		if (rect instanceof Rectangle.OfFloat) {
			return scaleBounds(rect, 100, zoom);
		}
		Rectangle.OfFloat floatRect = Rectangle.OfFloat.from(rect);
		Point.OfFloat scaledTopLeft = pixelToPoint(floatRect.getTopLeft(), zoom);
		Point.OfFloat scaledBottomRight = pixelToPoint(floatRect.getBottomRight(), zoom);
		float scaledX = scaledTopLeft.x;
		float scaleyY = scaledTopLeft.y;
		float scaledWidth = scaledBottomRight.x - scaledTopLeft.x;
		float scaledHeight = scaledBottomRight.y - scaledTopLeft.y;
		return new Rectangle.OfFloat(scaledX, scaleyY, scaledWidth, scaledHeight, RoundingMode.ROUND, sizeRounding);
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
		Rectangle.OfFloat fRect = Rectangle.OfFloat.from(rect);
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

	public static int pointToPixel(Drawable drawable, int size, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return size;
		return DPIUtil.pointToPixel (size, zoom);
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

	private static Point pointToPixel(Point point, int zoom, RoundingMode mode) {
		if (zoom == 100 || point == null) return point;
		Point.OfFloat floatPoint = Point.OfFloat.from(point);
		return pointToPixel(new Point.OfFloat(floatPoint.getX(), floatPoint.getY(), mode), zoom);
	}

	private static Point.OfFloat pointToPixelOfFloat(Point.OfFloat point, int zoom) {
		Point.OfFloat scaledPoint = point.clone();
		float scaleFactor = DPIUtil.getScalingFactor(zoom);
		scaledPoint.setX(point.getX() * scaleFactor);
		scaledPoint.setY(point.getY() * scaleFactor);
		return scaledPoint;
	}

	private static Point pointToPixel(Point.OfFloat point, int zoom) {
		Point scaledPoint = pointToPixelOfFloat(point, zoom);
		return new Point(scaledPoint.x, scaledPoint.y);
	}

	public static Point pointToPixelAsSize(Drawable drawable, Point point, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return point;
		return pointToPixelAsSize(point, zoom);
	}

	public static Point pointToPixelAsLocation(Drawable drawable, Point point, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return point;
		return pointToPixelAsLocation(point, zoom);
	}

	public static Point pointToPixelAsSize(Point point, int zoom) {
		return pointToPixel(point, zoom, RoundingMode.ROUND);
	}

	public static Point pointToPixelAsSufficientlyLargeSize(Point point, int zoom) {
		return pointToPixel(point, zoom, RoundingMode.UP);
	}

	public static Point pointToPixelAsLocation(Point point, int zoom) {
		return pointToPixel(point, zoom, RoundingMode.ROUND);
	}

	public static Rectangle pointToPixel(Rectangle rect, int zoom) {
		return pointToPixel(rect, zoom, RoundingMode.ROUND);
	}

	public static Rectangle pointToPixelWithSufficientlyLargeSize(Rectangle rect, int zoom) {
		return pointToPixel(rect, zoom, RoundingMode.UP);
	}

	private static Rectangle pointToPixel(Rectangle rect, int zoom, RoundingMode sizeRounding) {
		if (zoom == 100 || rect == null) return rect;
		if (rect instanceof Rectangle.OfFloat) {
			return scaleBounds(rect, zoom, 100);
		}
		Rectangle.OfFloat floatRect = Rectangle.OfFloat.from(rect);
		Point.OfFloat scaledTopLeft = pointToPixelOfFloat(floatRect.getTopLeft(), zoom);
		Point.OfFloat scaledBottomRight = pointToPixelOfFloat(floatRect.getBottomRight(), zoom);
		float scaledX = scaledTopLeft.x;
		float scaleyY = scaledTopLeft.y;
		float scaledWidth = scaledBottomRight.x - scaledTopLeft.x;
		float scaledHeight = scaledBottomRight.y - scaledTopLeft.y;
		Rectangle scaledRectangle = new Rectangle.OfFloat(scaledX, scaleyY, scaledWidth, scaledHeight, RoundingMode.ROUND, sizeRounding);
		return new Rectangle(scaledRectangle.x, scaledRectangle.y, scaledRectangle.width, scaledRectangle.height);
	}

	public static Rectangle pointToPixel(Drawable drawable, Rectangle rect, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return rect;
		return pointToPixel (rect, zoom);
	}

	public static int getPrimaryMonitorZoomAtStartup() {
		long hDC = OS.GetDC(0);
		int dpi = OS.GetDeviceCaps(hDC, OS.LOGPIXELSX);
		OS.ReleaseDC(0, hDC);
		return DPIUtil.mapDPIToZoom(dpi);
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
			return DPIUtil.scaleImageData(device, imageData, zoom, currentZoom);
		}
	}
}

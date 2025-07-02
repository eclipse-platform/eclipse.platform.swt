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

	public static float[] scaleDown(float size[], int zoom) {
		if (zoom == 100 || size == null) return size;
		float scaleFactor = DPIUtil.getScalingFactor (zoom);
		float scaledSize[] = new float[size.length];
		for (int i = 0; i < scaledSize.length; i++) {
			scaledSize[i] = size[i] / scaleFactor;
		}
		return scaledSize;
	}

	public static float[] scaleDown(Drawable drawable, float size[], int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return size;
		return scaleDown(size, zoom);
	}

	public static int scaleDown(Drawable drawable, int size, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return size;
		return DPIUtil.scaleDown (size, zoom);
	}

	public static float scaleDown(Drawable drawable, float size, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return size;
		return DPIUtil.scaleDown (size, zoom);
	}

	public static Point scaleDown(Point point, int zoom) {
		if (zoom == 100 || point == null) return point;
		Point.OfFloat fPoint = FloatAwareGeometryFactory.createFrom(point);
		float scaleFactor = DPIUtil.getScalingFactor(zoom);
		float scaledX = fPoint.getX() / scaleFactor;
		float scaledY = fPoint.getY() / scaleFactor;
		return new Point.OfFloat(scaledX, scaledY);
	}

	public static Point scaleDown(Drawable drawable, Point point, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return point;
		return scaleDown (point, zoom);
	}

	public static Rectangle scaleDown(Rectangle rect, int zoom) {
		return scaleBounds(rect, 100, zoom);
	}

	public static Rectangle scaleDown(Drawable drawable, Rectangle rect, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return rect;
		return scaleDown (rect, zoom);
	}

	/**
	 * Returns a new rectangle as per the scaleFactor.
	 */
	public static Rectangle scaleBounds (Rectangle rect, int targetZoom, int currentZoom) {
		if (rect == null || targetZoom == currentZoom) return rect;
		Rectangle.OfFloat fRect = FloatAwareGeometryFactory.createFrom(rect);
		float scaleFactor = DPIUtil.getScalingFactor(targetZoom, currentZoom);
		float scaledX = fRect.getX() * scaleFactor;
		float scaledY = fRect.getY() * scaleFactor;
		float scaledWidth = fRect.getWidth() * scaleFactor;
		float scaledHeight = fRect.getHeight() * scaleFactor;
		return new Rectangle.OfFloat(scaledX, scaledY, scaledWidth, scaledHeight);
	}

	public static int[] scaleUp(int[] pointArray, int zoom) {
		if (zoom == 100 || pointArray == null) return pointArray;
		float scaleFactor = DPIUtil.getScalingFactor(zoom);
		int[] returnArray = new int[pointArray.length];
		for (int i = 0; i < pointArray.length; i++) {
			returnArray [i] =  Math.round (pointArray [i] * scaleFactor);
		}
		return returnArray;
	}

	public static int[] scaleUp(Drawable drawable, int[] pointArray, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return pointArray;
		return scaleUp (pointArray, zoom);
	}

	/**
	 * Auto-scale up int dimensions to match the given zoom level
	 */
	public static int scaleUp(int size, int zoom) {
		if (zoom == 100 || size == SWT.DEFAULT) return size;
		float scaleFactor = DPIUtil.getScalingFactor(zoom);
		return Math.round (size * scaleFactor);
	}

	public static int scaleUp(Drawable drawable, int size, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return size;
		return scaleUp (size, zoom);
	}

	public static float scaleUp(float size, int zoom) {
		if (zoom == 100 || size == SWT.DEFAULT) return size;
		float scaleFactor = DPIUtil.getScalingFactor(zoom);
		return (size * scaleFactor);
	}

	public static float scaleUp(Drawable drawable, float size, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return size;
		return scaleUp (size, zoom);
	}

	public static Point scaleUp(Point point, int zoom) {
		if (zoom == 100 || point == null) return point;
		Point.OfFloat fPoint = FloatAwareGeometryFactory.createFrom(point);
		float scaleFactor = DPIUtil.getScalingFactor(zoom);
		float scaledX = fPoint.getX() * scaleFactor;
		float scaledY = fPoint.getY() * scaleFactor;
		return new Point.OfFloat(scaledX, scaledY);
	}

	public static Point scaleUp(Drawable drawable, Point point, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return point;
		return scaleUp (point, zoom);
	}

	public static Rectangle scaleUp(Rectangle rect, int zoom) {
		return scaleBounds(rect, zoom, 100);
	}

	public static Rectangle scaleUp(Drawable drawable, Rectangle rect, int zoom) {
		if (drawable != null && !drawable.isAutoScalable()) return rect;
		return scaleUp (rect, zoom);
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

/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gdip.*;

/**
 * Instances of this class represent patterns to use while drawing. Patterns
 * can be specified either as bitmaps or gradients.
 * <p>
 * Application code must explicitly invoke the <code>Pattern.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * <p>
 * This class requires the operating system's advanced graphics subsystem
 * which may not be available on some platforms.
 * </p>
 *
 * @see <a href="https://eclipse.dev/eclipse/swt/snippets/#path">Path, Pattern snippets</a>
 * @see <a href="https://eclipse.dev/eclipse/swt/examples.html">SWT Example: GraphicsExample</a>
 * @see <a href="https://eclipse.dev/eclipse/swt/">Sample code and further information</a>
 *
 * @since 3.1
 */
public class Pattern extends Resource {
	// These are the possible fields with which a pattern can be initialized from the appropriate constructors.
	private final Image image;
	private float baseX1, baseY1, baseX2, baseY2;
	private Color color1, color2;
	private int alpha1, alpha2;

	private final Map<Integer, PatternHandle> zoomToHandle = new HashMap<>();

	private boolean disposed;

/**
 * Constructs a new Pattern given an image. Drawing with the resulting
 * pattern will cause the image to be tiled over the resulting area.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * <p>
 * You must dispose the pattern when it is no longer required.
 * </p>
 *
 * @param device the device on which to allocate the pattern
 * @param image the image that the pattern will draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the device is null and there is no current device, or the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the pattern could not be obtained</li>
 * </ul>
 *
 * @see #dispose()
 */
public Pattern(Device device, Image image) {
	super(device);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.device.checkGDIP();
	this.image = image;
	init();
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * Constructs a new Pattern that represents a linear, two color
 * gradient. Drawing with the pattern will cause the resulting area to be
 * tiled with the gradient specified by the arguments.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * <p>
 * You must dispose the pattern when it is no longer required.
 * </p>
 *
 * @param device the device on which to allocate the pattern
 * @param x1 the x coordinate of the starting corner of the gradient
 * @param y1 the y coordinate of the starting corner of the gradient
 * @param x2 the x coordinate of the ending corner of the gradient
 * @param y2 the y coordinate of the ending corner of the gradient
 * @param color1 the starting color of the gradient
 * @param color2 the ending color of the gradient
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the device is null and there is no current device,
 *                              or if either color1 or color2 is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if either color1 or color2 has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the pattern could not be obtained</li>
 * </ul>
 *
 * @see #dispose()
 */
public Pattern(Device device, float x1, float y1, float x2, float y2, Color color1, Color color2) {
	this(device, x1, y1, x2, y2, color1, 0xFF, color2, 0xFF);
}

/**
 * Constructs a new Pattern that represents a linear, two color
 * gradient. Drawing with the pattern will cause the resulting area to be
 * tiled with the gradient specified by the arguments.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * <p>
 * You must dispose the pattern when it is no longer required.
 * </p>
 *
 * @param device the device on which to allocate the pattern
 * @param x1 the x coordinate of the starting corner of the gradient
 * @param y1 the y coordinate of the starting corner of the gradient
 * @param x2 the x coordinate of the ending corner of the gradient
 * @param y2 the y coordinate of the ending corner of the gradient
 * @param color1 the starting color of the gradient
 * @param alpha1 the starting alpha value of the gradient
 * @param color2 the ending color of the gradient
 * @param alpha2 the ending alpha value of the gradient
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the device is null and there is no current device,
 *                              or if either color1 or color2 is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if either color1 or color2 has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the pattern could not be obtained</li>
 * </ul>
 *
 * @see #dispose()
 *
 * @since 3.2
 */
public Pattern(Device device, float x1, float y1, float x2, float y2, Color color1, int alpha1, Color color2, int alpha2) {
	super(device);
	if (color1 == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color1.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (color2 == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color2.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.baseX1 = x1;
	this.baseX2 = x2;
	this.baseY1 = y1;
	this.baseY2 = y2;
	this.color1 = color1;
	this.color2 = color2;
	this.alpha1 = alpha1;
	this.alpha2 = alpha2;
	this.image = null;
	init();
	this.device.registerResourceWithZoomSupport(this);
}

private PatternHandle getPatternHandle(int zoom) {
	return zoomToHandle.computeIfAbsent(zoom, z -> image != null ? new ImagePatternHandle(z) : new BasePatternHandle(z));
}

long getHandle(int zoom) {
	return this.getPatternHandle(zoom).handle;
}

@Override
void destroy() {
	device.deregisterResourceWithZoomSupport(this);
	zoomToHandle.values().forEach(PatternHandle::destroy);
	zoomToHandle.clear();
	disposed = true;
}

@Override
void destroyHandlesExcept(Set<Integer> zoomLevels) {
	zoomToHandle.entrySet().removeIf(entry -> {
		final Integer zoom = entry.getKey();
		if (!zoomLevels.contains(zoom) ) {
			entry.getValue().destroy();
			return true;
		}
		return false;
	});
}

/**
 * Package-protected for internal use by fragments.
 * @return the properties of this pattern, which can be used to create a copy of the pattern with the same properties as this pattern
 */
PatternProperties getProperties() {
	return new PatternProperties(image,  baseX1, baseY1, baseX2, baseY2, color1, alpha1, color2, alpha2);
}

Pattern copy() {
	if (image != null) {
		return new Pattern(device, image);
	}
	return new Pattern(device, baseX1, baseY1, baseX2, baseY2, color1, alpha1, color2, alpha2);
}

/**
 * Returns <code>true</code> if the Pattern has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the Pattern.
 * When a Pattern has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the Pattern.
 *
 * @return <code>true</code> when the Pattern is disposed, and <code>false</code> otherwise
 */
@Override
public boolean isDisposed() {
	return disposed;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString() {
	if (isDisposed()) return "Pattern {*DISPOSED*}";
	return "Pattern {" + zoomToHandle + "}";
}

/**
 * Converts a Win32 COLORREF value (0x00BBGGRR) and an alpha byte into a
 * GDI+ ARGB color value (0xAARRGGBB).
 */
private static int colorRefToArgb(int colorRef, int alpha) {
	return ((alpha & 0xFF) << 24) | ((colorRef >> 16) & 0xFF) | (colorRef & 0xFF00) | ((colorRef & 0xFF) << 16);
}

private class BasePatternHandle extends PatternHandle {
	BasePatternHandle(int zoom) {
		super(zoom);
	}

	@Override
	long createHandle(int zoom) {
		long handle;
		float x1 = Win32DPIUtils.pointToPixel(baseX1, zoom);
		float y1 = Win32DPIUtils.pointToPixel(baseY1, zoom);
		float x2 = Win32DPIUtils.pointToPixel(baseX2, zoom);
		float y2 = Win32DPIUtils.pointToPixel(baseY2, zoom);
		if (color1.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if (color2.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		device.checkGDIP();
		int colorRef1 = color1.handle;
		int foreColor = colorRefToArgb(colorRef1, alpha1);
		if (x1 == x2 && y1 == y2) {
			handle = Gdip.SolidBrush_new(foreColor);
			if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		} else {
			int colorRef2 = color2.handle;
			int backColor = colorRefToArgb(colorRef2, alpha2);
			PointF p1 = new PointF();
			p1.X = x1;
			p1.Y = y1;
			PointF p2 = new PointF();
			p2.X = x2;
			p2.Y = y2;
			handle = Gdip.LinearGradientBrush_new(p1, p2, foreColor, backColor);
			if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			if (alpha1 != 0xFF || alpha2 != 0xFF) {
				int a = (int)((alpha1 & 0xFF) * 0.5f + (alpha2 & 0xFF) * 0.5f);
				int r = (int)((colorRef1 & 0xFF) * 0.5f + (colorRef2 & 0xFF) * 0.5f);
				int g = (int)(((colorRef1 & 0xFF00) >> 8) * 0.5f + ((colorRef2 & 0xFF00) >> 8) * 0.5f);
				int b = (int)(((colorRef1 & 0xFF0000) >> 16) * 0.5f + ((colorRef2 & 0xFF0000) >> 16) * 0.5f);
				int midColor = a << 24 | r << 16 | g << 8 | b;
				Gdip.LinearGradientBrush_SetInterpolationColors(handle, new int [] {foreColor, midColor, backColor}, new float[]{0, 0.5f, 1}, 3);
			}
		}
		return handle;
	}
}

private class ImagePatternHandle extends PatternHandle {
	private Image.GdipImage gdipImage;

	ImagePatternHandle(int zoom) {
		super(zoom);
	}

	@Override
	long createHandle(int zoom) {
		gdipImage = image.createGdipImage(zoom);
		long img = gdipImage.bitmap();
		int width = Gdip.Image_GetWidth(img);
		int height = Gdip.Image_GetHeight(img);
		long handle = Gdip.TextureBrush_new(img, Gdip.WrapModeTile, 0, 0, width, height);
		if (handle == 0) {
			cleanupBitmap();
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		return handle;
	}

	@Override
	protected void destroy() {
		super.destroy();
		cleanupBitmap();
	}

	private void cleanupBitmap() {
		if (gdipImage == null) return;
		Image.GdipImage tempGdipImage = gdipImage;
		gdipImage = null;
		tempGdipImage.destroy();
	}
}

private abstract class PatternHandle {
	private final long handle;

	PatternHandle(int zoom) {
		this.handle = createHandle(zoom);
	}

	abstract long createHandle(int zoom);

	protected void destroy() {
		switch (Gdip.Brush_GetType(handle)) {
			case Gdip.BrushTypeSolidColor -> Gdip.SolidBrush_delete(handle);
			case Gdip.BrushTypeHatchFill -> Gdip.HatchBrush_delete(handle);
			case Gdip.BrushTypeLinearGradient -> Gdip.LinearGradientBrush_delete(handle);
			case Gdip.BrushTypeTextureFill -> Gdip.TextureBrush_delete(handle);
		}
	}
}
}
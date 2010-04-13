/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.gdip.*;
import org.eclipse.swt.internal.win32.*;

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
 * @see <a href="http://www.eclipse.org/swt/snippets/#path">Path, Pattern snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: GraphicsExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.1
 */
public class Pattern extends Resource {

	/**
	 * the OS resource for the Pattern
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 * 
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public int /*long*/ handle;

/**
 * Constructs a new Pattern given an image. Drawing with the resulting
 * pattern will cause the image to be tiled over the resulting area.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
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
	int /*long*/[] gdipImage = image.createGdipImage();
	int /*long*/ img = gdipImage[0];
	int width = Gdip.Image_GetWidth(img);
	int height = Gdip.Image_GetHeight(img);
	handle = Gdip.TextureBrush_new(img, Gdip.WrapModeTile, 0, 0, width, height);	
	Gdip.Bitmap_delete(img);
	if (gdipImage[1] != 0) {
		int /*long*/ hHeap = OS.GetProcessHeap ();
		OS.HeapFree(hHeap, 0, gdipImage[1]);
	}
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	init();
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
	this.device.checkGDIP();
	int colorRef1 = color1.handle;
	int rgb = ((colorRef1 >> 16) & 0xFF) | (colorRef1 & 0xFF00) | ((colorRef1 & 0xFF) << 16);
	int /*long*/ foreColor = Gdip.Color_new((alpha1 & 0xFF) << 24 | rgb);
	if (x1 == x2 && y1 == y2) {
		handle = Gdip.SolidBrush_new(foreColor);
		if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	} else {
		int colorRef2 = color2.handle;
		rgb = ((colorRef2 >> 16) & 0xFF) | (colorRef2 & 0xFF00) | ((colorRef2 & 0xFF) << 16);
		int /*long*/ backColor = Gdip.Color_new((alpha2 & 0xFF) << 24 | rgb);
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
			int r = (int)(((colorRef1 & 0xFF) >> 0) * 0.5f + ((colorRef2 & 0xFF) >> 0) * 0.5f);
			int g = (int)(((colorRef1 & 0xFF00) >> 8) * 0.5f + ((colorRef2 & 0xFF00) >> 8) * 0.5f);
			int b = (int)(((colorRef1 & 0xFF0000) >> 16) * 0.5f + ((colorRef2 & 0xFF0000) >> 16) * 0.5f);
			int /*long*/ midColor = Gdip.Color_new(a << 24 | r << 16 | g << 8 | b);
			Gdip.LinearGradientBrush_SetInterpolationColors(handle, new int /*long*/ []{foreColor, midColor, backColor}, new float[]{0, 0.5f, 1}, 3);
			Gdip.Color_delete(midColor);
		}
		Gdip.Color_delete(backColor);
	}
	Gdip.Color_delete(foreColor);
	init();
}
	
void destroy() {
	int type = Gdip.Brush_GetType(handle);
	switch (type) {
		case Gdip.BrushTypeSolidColor:
			Gdip.SolidBrush_delete(handle);
			break;
		case Gdip.BrushTypeHatchFill:
			Gdip.HatchBrush_delete(handle);
			break;
		case Gdip.BrushTypeLinearGradient:
			Gdip.LinearGradientBrush_delete(handle);
			break;
		case Gdip.BrushTypeTextureFill:
			Gdip.TextureBrush_delete(handle);
			break;
	}
	handle = 0;
}

/**
 * Returns <code>true</code> if the Pattern has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the Pattern.
 * When a Pattern has been disposed, it is an error to
 * invoke any other method using the Pattern.
 *
 * @return <code>true</code> when the Pattern is disposed, and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == 0;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString() {
	if (isDisposed()) return "Pattern {*DISPOSED*}";
	return "Pattern {" + handle + "}";
}
	
}

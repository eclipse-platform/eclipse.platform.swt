/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
	 */
	public int handle;

/**
 * Constructs a new Pattern given an image. Drawing with the resulting
 * pattern will cause the image to be tiled over the resulting area.
 * 
 * @param device the device on which to allocate the pattern
 * @param image the image that the pattern will draw
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the device is null and there is no current device, or the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the pattern could not be obtained/li>
 * </ul>
 * 
 * @see #dispose()
 */
public Pattern(Device device, Image image) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.device = device;
	device.checkGDIP();
	int[] gdipImage = image.createGdipImage();
	int img = gdipImage[0];
	int width = Gdip.Image_GetWidth(img);
	int height = Gdip.Image_GetHeight(img);
	handle = Gdip.TextureBrush_new(img, Gdip.WrapModeTile, 0, 0, width, height);	
	Gdip.Bitmap_delete(img);
	if (gdipImage[1] != 0) {
		int hHeap = OS.GetProcessHeap ();
		OS.HeapFree(hHeap, 0, gdipImage[1]);
	}
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

/**
 * Constructs a new Pattern that represents a linear, two color
 * gradient. Drawing with the pattern will cause the resulting area to be
 * tiled with the gradient specified by the arguments.
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
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the pattern could not be obtained/li>
 * </ul>
 * 
 * @see #dispose()
 */
public Pattern(Device device, float x1, float y1, float x2, float y2, Color color1, Color color2) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color1 == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color1.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (color2 == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color2.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.device = device;
	device.checkGDIP();
	//TODO - how about alpha?
	int colorRef = color1.handle;
	int rgb = ((colorRef >> 16) & 0xFF) | (colorRef & 0xFF00) | ((colorRef & 0xFF) << 16);
	int foreColor = Gdip.Color_new(0xFF << 24 | rgb);
	colorRef = color2.handle;
	rgb = ((colorRef >> 16) & 0xFF) | (colorRef & 0xFF00) | ((colorRef & 0xFF) << 16);
	int backColor = Gdip.Color_new(0xFF << 24 | rgb);
	PointF p1 = new PointF();
	p1.X = x1;
	p1.Y = y1;
	PointF p2 = new PointF();
	p2.X = x2;
	p2.Y = y2;
	handle = Gdip.LinearGradientBrush_new(p1, p2, foreColor, backColor);
	Gdip.Color_delete(foreColor);
	Gdip.Color_delete(backColor);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}
	
/**
 * Disposes of the operating system resources associated with
 * the Pattern. Applications must dispose of all Patterns that
 * they allocate.
 */
public void dispose() {
	if (handle == 0) return;
	if (device.isDisposed()) return;
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
	if (device.tracking) device.dispose_Object(this);
	device = null;
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

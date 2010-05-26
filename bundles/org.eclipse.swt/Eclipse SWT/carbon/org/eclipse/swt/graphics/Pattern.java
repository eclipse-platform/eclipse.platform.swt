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
import org.eclipse.swt.internal.carbon.*;

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
	int jniRef;
	Image image;
	float[] color1, color2;
	int alpha1, alpha2;
	float x1, y1, x2, y2;
	int shading;
	CGRect drawRect;

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
	device = this.device;
	this.image = image;
	device.createPatternCallbacks();
	jniRef = OS.NewGlobalRef(this);
	if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
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
	device = this.device;
	this.x1 = x1;
	this.y1 = y1;
	this.x2 = x2;
	this.y2 = y2;
	this.color1 = color1.handle;
	this.color2 = color2.handle;
	this.alpha1 = alpha1;
	this.alpha2 = alpha2;
	device.createPatternCallbacks();
	jniRef = OS.NewGlobalRef(this);
	if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	CGPoint start = new CGPoint();
	start.x = x1;
	start.y = y1;
	CGPoint end = new CGPoint();
	end.x = x2;
	end.y = y2;
	CGFunctionCallbacks fCallbacks = new CGFunctionCallbacks();
	fCallbacks.evaluate = device.axialShadingProc;
	int function = OS.CGFunctionCreate(jniRef, 1, new float[]{0, 1}, 4, new float[]{0, 1, 0, 1, 0, 1, 0, 1}, fCallbacks);
	if (function == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	shading = OS.CGShadingCreateAxial(device.colorspace, start, end, function, true, true);	
	OS.CGFunctionRelease(function);
	if (shading == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	init();
}

int axialShadingProc (int ref, int in, int out) {
	float[] buffer = new float[4];
	OS.memmove(buffer, in, 4);
	float factor2 = buffer[0], factor1 = 1 - factor2;
	float[] c1 = color1;
	float[] c2 = color2;
	float a1 = ((alpha1 & 0xFF) / (float)0xFF);
	float a2 = ((alpha2 & 0xFF) / (float)0xFF);
	buffer[0] = (c2[0] * factor2) + (c1[0] * factor1);
	buffer[1] = (c2[1] * factor2) + (c1[1] * factor1);
	buffer[2] = (c2[2] * factor2) + (c1[2] * factor1);
	buffer[3] = (a2 * factor2) + (a1 * factor1);
	OS.memmove(out, buffer, buffer.length * 4);
	return 0;
}

int createPattern(int context) {
	float[] transform = new float[6];
	OS.CGContextGetCTM(context, transform);
	CGRect rect = new CGRect();
	if (image != null) {
		int imageHandle = image.handle;
		rect.width = OS.CGImageGetWidth(imageHandle);
		rect.height = OS.CGImageGetHeight(imageHandle);
	} else {
		rect.x = x1 - 0.5f;
		rect.y = y1 - 0.5f;
		rect.width = x2 - x1 + 1;
		rect.height = y2 - y1 + 1;
	}
	CGPatternCallbacks callbacks = new CGPatternCallbacks();
	callbacks.drawPattern = device.drawPatternProc;
	int pattern = OS.CGPatternCreate(jniRef, rect, transform, rect.width, rect.height, OS.kCGPatternTilingNoDistortion, 0, callbacks);
	if (pattern == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	return pattern;
}

void destroy() {
	if (jniRef != 0) OS.DeleteGlobalRef(jniRef);
	if (shading != 0) OS.CGShadingRelease(shading);
	jniRef = shading = 0;
	image = null;
	color1 = color2 = null;
}

int drawPatternProc (int ref, int context) {
	if (image != null) {
		if (image.isDisposed()) return 0;
		int imageHandle = image.handle;
		int imageWidth = OS.CGImageGetWidth(imageHandle);
		int imageHeight = OS.CGImageGetHeight(imageHandle);
		CGRect rect = new CGRect();
		rect.width = imageWidth;
		rect.height = imageHeight;
		OS.CGContextScaleCTM(context, 1, -1);
	 	if (drawRect != null && (drawRect.x % imageWidth) + drawRect.width < imageWidth && (drawRect.y % imageHeight) + drawRect.height < imageHeight) {
	 		rect.x = drawRect.x % imageWidth;
	 		rect.y = drawRect.y % imageHeight;
	 		rect.width = drawRect.width;
	 		rect.height = drawRect.height;
	 		if (OS.VERSION >= 0x1040) {
	 			imageHandle = OS.CGImageCreateWithImageInRect(imageHandle, rect);
	 		} else {
		 		int srcX = (int)drawRect.x, srcY = (int)drawRect.y;
		 		int srcWidth = (int)drawRect.width, srcHeight = (int)drawRect.height;
		 		int bpc = OS.CGImageGetBitsPerComponent(imageHandle);
				int bpp = OS.CGImageGetBitsPerPixel(imageHandle);
				int bpr = OS.CGImageGetBytesPerRow(imageHandle);
				int colorspace = OS.CGImageGetColorSpace(imageHandle);
				int alphaInfo = OS.CGImageGetAlphaInfo(imageHandle);
				int data = image.data + (srcY * bpr) + srcX * 4;
				int provider = OS.CGDataProviderCreateWithData(0, data, srcHeight * bpr, 0);
				if (provider != 0) {
					imageHandle = OS.CGImageCreate(srcWidth, srcHeight, bpc, bpp, bpr, colorspace, alphaInfo, provider, null, true, 0);
					OS.CGDataProviderRelease(provider);
				}
			}
	 	}
	 	OS.CGContextTranslateCTM(context, 0, -(rect.height + 2 * rect.y));
	 	OS.CGContextDrawImage(context, rect, imageHandle);
	 	if (imageHandle != 0 && imageHandle != image.handle) OS.CGImageRelease(imageHandle);
	} else {
		OS.CGContextDrawShading(context, shading);
	}
	return 0;
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
public boolean isDisposed() {
	return jniRef == 0;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString() {
	if (isDisposed()) return "Pattern {*DISPOSED*}";
	return "Pattern {" + jniRef + "}";
}
	
}

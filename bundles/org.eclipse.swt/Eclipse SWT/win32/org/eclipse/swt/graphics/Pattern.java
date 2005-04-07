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

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public class Pattern extends Resource {

	/**
	 * the handle to the OS path resource
	 * (Warning: This field is platform dependent)
	 */
	public int handle;

public Pattern(Device device, Image image) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.device = device;
	device.checkGDIP();
	int img;
	if (image.type == SWT.ICON){
		img = Gdip.Bitmap_new(image.handle);
	} else {
		img = Gdip.Bitmap_new(image.handle, 0);
	}
	int width = Gdip.Image_GetWidth(img);
	int height = Gdip.Image_GetHeight(img);
	handle = Gdip.TextureBrush_new(img, Gdip.WrapModeTile, 0, 0, width, height);	
	Gdip.Bitmap_delete(img);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

public Pattern(Device device, float x1, float y1, float x2, float y2, Color foreground, Color background) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (foreground == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (foreground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (background == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (background.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.device = device;
	device.checkGDIP();
	//TODO - how about alpha?
	int colorRef = foreground.handle;
	int rgb = ((colorRef >> 16) & 0xFF) | (colorRef & 0xFF00) | ((colorRef & 0xFF) << 16);
	int foreColor = Gdip.Color_new(0xFF << 24 | rgb);
	colorRef = background.handle;
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

public boolean isDisposed() {
	return handle == 0;
}

public String toString() {
	if (isDisposed()) return "Pattern {*DISPOSED*}";
	return "Pattern {" + handle + "}";
}
	
}

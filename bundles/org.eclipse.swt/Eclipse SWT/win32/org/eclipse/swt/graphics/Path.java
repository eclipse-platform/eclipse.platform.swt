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
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public class Path {
	
	/**
	 * the handle to the OS path resource
	 * (Warning: This field is platform dependent)
	 */
	public int handle;
	
	/**
	 * the device where this font was created
	 */
	Device device;
	
	PointF currentPoint = new PointF();
	
public Path (Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	device.checkGDIP();
	handle = Gdip.GraphicsPath_new(Gdip.FillModeAlternate);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

public void addArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	Gdip.GraphicsPath_AddArc(handle, x, y, width, height, -startAngle, -arcAngle);
	Gdip.GraphicsPath_GetLastPoint(handle, currentPoint);
}

public void addPath(Path path) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	//TODO - expose connect?
	Gdip.GraphicsPath_AddPath(handle, path.handle, false);
	Gdip.GraphicsPath_GetLastPoint(handle, currentPoint);
}

public void addRectangle(float x, float y, float width, float height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	RectF rect = new RectF();
	rect.X = x;
	rect.Y = y;
	rect.Width = width;
	rect.Height = height;
	Gdip.GraphicsPath_AddRectangle(handle, rect);
	Gdip.GraphicsPath_GetLastPoint(handle, currentPoint);
}

public void addString(String string, float x, float y, Font font) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int length = string.length();
	char[] buffer = new char[length];
	string.getChars(0, length, buffer, 0);
	int hDC = device.internal_new_GC(null);
	int gdipFont = Gdip.Font_new(hDC, font.handle);
	PointF point = new PointF();
	point.X = x - (Gdip.Font_GetSize(gdipFont) / 6);
	point.Y = y;
	int family = Gdip.FontFamily_new();
	Gdip.Font_GetFamily(gdipFont, family);
	int style = Gdip.Font_GetStyle(gdipFont);
	float size = Gdip.Font_GetSize(gdipFont);
	Gdip.GraphicsPath_AddString(handle, buffer, length, family, style, size, point, 0);
	Gdip.GraphicsPath_GetLastPoint(handle, currentPoint);
	Gdip.FontFamily_delete(family);
	Gdip.Font_delete(gdipFont);
	device.internal_dispose_GC(hDC, null);
}

public void close() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Gdip.GraphicsPath_CloseFigure(handle);
}

public boolean contains(float x, float y, GC gc, boolean outline) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	//TODO - should use GC transformation
	gc.initGdip(outline, false);
	int mode = OS.GetPolyFillMode(gc.handle) == OS.WINDING ? Gdip.FillModeWinding : Gdip.FillModeAlternate;
	Gdip.GraphicsPath_SetFillMode(handle, mode);
	if (outline) {
		return Gdip.GraphicsPath_IsOutlineVisible(handle, x, y, gc.data.gdipPen, gc.data.gdipGraphics);
	} else {
		return Gdip.GraphicsPath_IsVisible(handle, x, y, gc.data.gdipGraphics);
	}
}

public void curveTo(float cx1, float cy1, float cx2, float cy2, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Gdip.GraphicsPath_AddBezier(handle, currentPoint.X, currentPoint.Y, cx1, cy1, cx2, cy2, x, y);
	Gdip.GraphicsPath_GetLastPoint(handle, currentPoint);
}

public void dispose() {
	if (handle == 0) return;
	if (device.isDisposed()) return;
	Gdip.GraphicsPath_delete(handle);
	handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public void getBounds(float[] bounds) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds.length < 4) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	RectF rect = new RectF();
	Gdip.GraphicsPath_GetBounds(handle, rect, 0, 0);
	bounds[0] = rect.X;
	bounds[1] = rect.Y;
	bounds[2] = rect.Width;
	bounds[3] = rect.Height;
}

public void getCurrentPoint(float[] point) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (point == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (point.length < 2) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	point[0] = currentPoint.X;
	point[1] = currentPoint.Y;
}

public void lineTo(float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Gdip.GraphicsPath_AddLine(handle, currentPoint.X, currentPoint.Y, x, y);
	Gdip.GraphicsPath_GetLastPoint(handle, currentPoint);
}

public boolean isDisposed() {
	return handle == 0;
}

public void moveTo(float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	currentPoint.X = x;
	currentPoint.Y = y;
}

public void quadTo(float cx, float cy, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	float cx1 = currentPoint.X + 2 * (cx - currentPoint.X) / 3;
	float cy1 = currentPoint.Y + 2 * (cy - currentPoint.Y) / 3;
	float cx2 = cx1 + (x - currentPoint.X) / 3;
	float cy2 = cy1 + (y - currentPoint.Y) / 3;
	Gdip.GraphicsPath_AddBezier(handle, currentPoint.X, currentPoint.Y, cx1, cy1, cx2, cy2, x, y);
	Gdip.GraphicsPath_GetLastPoint(handle, currentPoint);
}

public String toString() {
	if (isDisposed()) return "Path {*DISPOSED*}";
	return "Path {" + handle + "}";
}

}

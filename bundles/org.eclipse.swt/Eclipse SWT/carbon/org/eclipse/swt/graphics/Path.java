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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;

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
	
public Path (Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	handle = OS.CGPathCreateMutable();
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

public void addArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	float[] cmt = new float[6];
	OS.CGAffineTransformMake(width / 2f, 0, 0, height / 2f, x + width / 2f, y + height / 2f, cmt);
	OS.CGPathAddArc(handle, cmt, 0, 0, 1, -startAngle * (float)Compatibility.PI / 180,  -(startAngle + arcAngle) * (float)Compatibility.PI / 180, true);
}

public void addPath(Path path) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.CGPathAddPath(handle, null, path.handle);
}

public void addRectangle(float x, float y, float width, float height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	CGRect rect = new CGRect();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	OS.CGPathAddRect(handle, null, rect);
}

int newPathProc(int data) {
	first = true;
	return 0;
}

int closePathProc(int data) {
	first = true;
	return 0;
}

int lineProc(int pt1, int pt2, int data) {
	if (first) {
		first = false;
		OS.memcpy(point, pt1, 8);
		OS.CGPathMoveToPoint(handle, null, originX + point[0], originY + point[1]);
	}
	OS.memcpy(point, pt2, 8);
	OS.CGPathAddLineToPoint(handle, null, originX + point[0], originY + point[1]);
	return 0;
}

int curveProc(int pt1, int controlPt, int pt2, int data) {
	if (first) {
		first = false;
		OS.memcpy(point, pt1, 8);
		OS.CGPathMoveToPoint(handle, null, originX + point[0], originY + point[1]);
	}
	OS.memcpy(point, pt2, 8);
	float x2 = point[0], y2 = point[1];	
	OS.memcpy(point, controlPt, 8);
	OS.CGPathAddQuadCurveToPoint(handle, null, originX + point[0], originY + point[1], originX + x2, originY + y2);
	return 0;
}

float originX, originY;
float[] point = new float[2];
boolean first;
public void addString(String string, float x, float y, Font font) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int length = string.length();
	if (length == 0) return;
	
	Callback newPathCallback = new Callback(this, "newPathProc", 1);
	int newPathProc = newPathCallback.getAddress();
	if (newPathProc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	Callback lineCallback = new Callback(this, "lineProc", 3);
	int lineProc = lineCallback.getAddress();
	if (lineProc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	Callback curveCallback = new Callback(this, "curveProc", 4);
	int curveProc = curveCallback.getAddress();
	if (curveProc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	Callback closePathCallback = new Callback(this, "closePathProc", 1);
	int closePathProc = closePathCallback.getAddress();
	if (closePathProc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);

	int style = font.atsuiStyle;
	if (style == 0) style = font.createStyle();
	if (style == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int[] buffer = new int[1];
	OS.ATSUCreateTextLayout(buffer);
	if (buffer[0] == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int layout = buffer[0];
	char[] chars = new char[length];
	string.getChars(0, length, chars, 0);
	int textPtr = OS.NewPtr(length * 2);
	if (textPtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.memcpy(textPtr, chars, length * 2);
	OS.ATSUSetTextPointerLocation(layout, textPtr, 0, length, length);
	OS.ATSUSetRunStyle(layout, style, 0, length);
	OS.ATSUSetTransientFontMatching(layout, true);
	int[] ascent = new int[1], descent = new int[1];
	OS.ATSUGetUnjustifiedBounds(layout, 0, length, null, null, ascent, descent);
	y += OS.Fix2X(ascent[0]);
	
	int[] layoutRecords = new int[1], numRecords = new int[1], deltaYs = new int[1], numDeltaYs = new int[1];
	OS.ATSUDirectGetLayoutDataArrayPtrFromTextLayout (layout, 0, OS.kATSUDirectDataLayoutRecordATSLayoutRecordCurrent, layoutRecords, numRecords); 
	OS.ATSUDirectGetLayoutDataArrayPtrFromTextLayout (layout, 0, OS.kATSUDirectDataBaselineDeltaFixedArray, deltaYs, numDeltaYs); 
	int[] deltaY = new int[1], status = new int[1];
	ATSLayoutRecord record = new ATSLayoutRecord();
	for (int i = 0; i < numRecords[0]; i++) {
		OS.memcpy(record, layoutRecords[0] + (i * ATSLayoutRecord.sizeof), ATSLayoutRecord.sizeof);
		originX = x + (float)OS.Fix2X(record.realPos);
		if (deltaYs[0] == 0) {
			originY = y;
		} else {
			OS.memcpy(deltaY, deltaYs[0] + (i * 4), 4);
			originY = y - (float)OS.Fix2X(deltaY[0]);
		}
		first = true; 
		if (record.glyphID != OS.kATSDeletedGlyphcode) {
			OS.ATSUGlyphGetQuadraticPaths (style, record.glyphID, newPathProc, lineProc, curveProc, closePathProc, 0, status);
		}
	}
	OS.CGPathCloseSubpath(handle); 
	if (deltaYs[0] != 0) {
		OS.ATSUDirectReleaseLayoutDataArrayPtr(0, OS.kATSUDirectDataBaselineDeltaFixedArray, deltaYs[0]);
	}
	OS.ATSUDirectReleaseLayoutDataArrayPtr(0, OS.kATSUDirectDataLayoutRecordATSLayoutRecordCurrent, layoutRecords[0]);

	if (style != font.atsuiStyle) OS.ATSUDisposeStyle(style);
	if (layout != 0) OS.ATSUDisposeTextLayout(layout);
	if (textPtr != 0) OS.DisposePtr(textPtr);
	
	newPathCallback.dispose();
	lineCallback.dispose();
	curveCallback.dispose();
	closePathCallback.dispose();
}

public void close() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.CGPathCloseSubpath(handle);
}

public boolean contains(float x, float y, GC gc, boolean outline) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	//TODO - see windows
	int pixel = OS.NewPtr(4);
	if (pixel == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int[] buffer = new int[]{0xFFFFFFFF};
	OS.memcpy(pixel, buffer, 4);
	int provider = OS.CGDataProviderCreateWithData(0, pixel, 4, 0);
	int context = OS.CGBitmapContextCreate(pixel, 1, 1, 8, 4, device.colorspace, OS.kCGImageAlphaNoneSkipFirst);
	if (context == 0) {
		OS.DisposePtr(pixel);
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	GCData data = gc.data;
	OS.CGContextSetLineCap(context, data.lineCap);
	OS.CGContextSetLineJoin(context, data.lineJoin);
	OS.CGContextSetLineWidth(context, data.lineWidth);
	OS.CGContextTranslateCTM(context, -x + 0.5f, -y + 0.5f);
	OS.CGContextAddPath(context, handle);
	if (outline) {
		OS.CGContextStrokePath(context);
	} else {
		if (data.fillRule == SWT.FILL_WINDING) {
			OS.CGContextFillPath(context);
		} else {
			OS.CGContextEOFillPath(context);
		}
	}
	OS.CGContextRelease(context);
	OS.memcpy(buffer, pixel, 4);
	OS.DisposePtr(pixel);	
	return buffer[0] != 0xFFFFFFFF;
}

public void curveTo(float cx1, float cy1, float cx2, float cy2, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.CGPathAddCurveToPoint(handle, null, cx1, cy1, cx2, cy2, x, y);
}

public void dispose() {
	if (handle == 0) return;
	OS.CGPathRelease(handle);
	handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public void getBounds(float[] bounds) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds.length < 4) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	CGRect rect = new CGRect();
	OS.CGPathGetBoundingBox(handle, rect);
	bounds[0] = rect.x;
	bounds[1] = rect.y;
	bounds[2] = rect.width;
	bounds[3] = rect.height;
}

public void getCurrentPoint(float[] point) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (point == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (point.length < 2) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	CGPoint pt = new CGPoint();
	OS.CGPathGetCurrentPoint(handle, pt);
	point[0] = pt.x;
	point[1] = pt.y;
}

public boolean isDisposed() {
	return handle == 0;
}

public void lineTo(float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.CGPathAddLineToPoint(handle, null, x, y);
}

public void moveTo(float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.CGPathMoveToPoint(handle, null, x, y);
}

public void quadTo(float cx, float cy, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.CGPathAddQuadCurveToPoint(handle, null, cx, cy, x, y);
}

public String toString () {
	if (isDisposed()) return "Path {*DISPOSED*}";
	return "Path {" + handle + "}";
}

}

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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class represent paths through the two-dimensional
 * coordinate system. Paths do not have to be continuous, and can be
 * described using lines, rectangles, arcs, cubic or quadratic bezier curves,
 * glyphs, or other paths.
 * <p>
 * Application code must explicitly invoke the <code>Path.dispose()</code> 
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
public class Path extends Resource {
	
	/**
	 * the OS resource for the Path
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int handle;
	
	boolean moved, closed = true;

/**
 * Constructs a new empty Path.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param device the device on which to allocate the path
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the device is null and there is no current device</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the path could not be obtained</li>
 * </ul>
 * 
 * @see #dispose()
 */
public Path (Device device) {
	super(device);
	handle = OS.CGPathCreateMutable();
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	init();
}

/**
 * Constructs a new Path that is a copy of <code>path</code>. If
 * <code>flatness</code> is less than or equal to zero, an unflatten
 * copy of the path is created. Otherwise, it specifies the maximum
 * error between the path and its flatten copy. Smaller numbers give
 * better approximation.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param device the device on which to allocate the path
 * @param path the path to make a copy
 * @param flatness the flatness value
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the path is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the path has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the path could not be obtained</li>
 * </ul>
 * 
 * @see #dispose()
 * @since 3.4
 */
public Path (Device device, Path path, float flatness) {
	super(device);
	if (path == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (path.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	flatness = Math.max(0, flatness);
	if (flatness == 0) {
		handle = OS.CGPathCreateMutableCopy(path.handle);
		if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	} else {
		handle = OS.CGPathCreateMutable();
		if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		PathData data = path.getPathData();
		int bezierPath = Cocoa.objc_msgSend(Cocoa.C_NSBezierPath, Cocoa.S_bezierPath);
		byte[] types = data.types;
		float[] points = data.points;
		NSPoint point = new NSPoint(), point2 = new NSPoint(), point3 = new NSPoint();
		for (int i = 0, j = 0; i < types.length; i++) {
			switch (types[i]) {
				case SWT.PATH_MOVE_TO:
					point.x = points[j++];
					point.y = points[j++];
					Cocoa.objc_msgSend(bezierPath, Cocoa.S_moveToPoint, point);
					break;
				case SWT.PATH_LINE_TO:
					point.x = points[j++];
					point.y = points[j++];
					Cocoa.objc_msgSend(bezierPath, Cocoa.S_lineToPoint, point);
					break;
				case SWT.PATH_CUBIC_TO:
					point2.x = points[j++];
					point2.y = points[j++];
					point3.x = points[j++];
					point3.y = points[j++];
					point.x = points[j++];
					point.y = points[j++];
					Cocoa.objc_msgSend(bezierPath, Cocoa.S_curveToPoint, point, point2, point3);
					break;
				case SWT.PATH_QUAD_TO:
					float currentX = point.x;
					float currentY = point.y;
					point2.x = points[j++];
					point2.y = points[j++];
					point.x = points[j++];
					point.y = points[j++];
					float x0 = currentX;
					float y0 = currentY;
					float cx1 = x0 + 2 * (point2.x - x0) / 3;
					float cy1 = y0 + 2 * (point2.y - y0) / 3;
					float cx2 = cx1 + (point.x - x0) / 3;
					float cy2 = cy1 + (point.y - y0) / 3;
					point2.x = cx1;
					point2.y = cy1;
					point3.x = cx2;
					point3.y = cy2;
					Cocoa.objc_msgSend(bezierPath, Cocoa.S_curveToPoint, point, point2, point3);
					break;
				case SWT.PATH_CLOSE:
					Cocoa.objc_msgSend(bezierPath, Cocoa.S_closePath);
					break;
				default:
					dispose();
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		Cocoa.objc_msgSend(Cocoa.C_NSBezierPath, Cocoa.S_setDefaultFlatness, flatness);
		bezierPath = Cocoa.objc_msgSend(bezierPath, Cocoa.S_bezierPathByFlatteningPath);
		int count = Cocoa.objc_msgSend(bezierPath, Cocoa.S_elementCount);
		int pointsPtr = OS.malloc(NSPoint.sizeof * 3);
		if (pointsPtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		for (int i = 0; i < count; i++) {
			int element = Cocoa.objc_msgSend(bezierPath, Cocoa.S_elementAtIndex_associatedPoints, i, pointsPtr);
			switch (element) {
				case Cocoa.NSMoveToBezierPathElement:
					Cocoa.memmove(point, pointsPtr, NSPoint.sizeof);
					moveTo(point.x, point.y);
					break;
				case Cocoa.NSLineToBezierPathElement:
					Cocoa.memmove(point, pointsPtr, NSPoint.sizeof);
					lineTo(point.x, point.y);
					break;
				case Cocoa.NSCurveToBezierPathElement:
					Cocoa.memmove(point, pointsPtr, NSPoint.sizeof);
					Cocoa.memmove(point2, pointsPtr + NSPoint.sizeof, NSPoint.sizeof);
					Cocoa.memmove(point3, pointsPtr + NSPoint.sizeof + NSPoint.sizeof, NSPoint.sizeof);
					cubicTo(point2.x, point2.y, point3.x, point3.y, point.x, point.y);
					break;
				case Cocoa.NSClosePathBezierPathElement:
					close();
					break;
			}
		}
		OS.free(pointsPtr);
	}
	init();
}

/**
 * Constructs a new Path with the specifed PathData.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param device the device on which to allocate the path
 * @param data the data for the path
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the data is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the path could not be obtained</li>
 * </ul>
 * 
 * @see #dispose()
 * @since 3.4
 */
public Path (Device device, PathData data) {
	this(device);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(data);
}

/**
 * Adds to the receiver a circular or elliptical arc that lies within
 * the specified rectangular area.
 * <p>
 * The resulting arc begins at <code>startAngle</code> and extends  
 * for <code>arcAngle</code> degrees.
 * Angles are interpreted such that 0 degrees is at the 3 o'clock
 * position. A positive value indicates a counter-clockwise rotation
 * while a negative value indicates a clockwise rotation.
 * </p><p>
 * The center of the arc is the center of the rectangle whose origin 
 * is (<code>x</code>, <code>y</code>) and whose size is specified by the 
 * <code>width</code> and <code>height</code> arguments. 
 * </p><p>
 * The resulting arc covers an area <code>width + 1</code> pixels wide
 * by <code>height + 1</code> pixels tall.
 * </p>
 *
 * @param x the x coordinate of the upper-left corner of the arc
 * @param y the y coordinate of the upper-left corner of the arc
 * @param width the width of the arc
 * @param height the height of the arc
 * @param startAngle the beginning angle
 * @param arcAngle the angular extent of the arc, relative to the start angle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void addArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	float[] cmt = new float[6];
	OS.CGAffineTransformMake(width / 2f, 0, 0, height / 2f, x + width / 2f, y + height / 2f, cmt);
	float angle = -startAngle * (float)Compatibility.PI / 180;
	if (closed) OS.CGPathMoveToPoint(handle, cmt, (float)Math.cos(angle), (float)Math.sin(angle));
	OS.CGPathAddArc(handle, cmt, 0, 0, 1, angle, -(startAngle + arcAngle) * (float)Compatibility.PI / 180, arcAngle >= 0);
	moved = true;
	closed = false;
	if (Math.abs(arcAngle) >= 360) close();
}

/**
 * Adds to the receiver the path described by the parameter.
 *
 * @param path the path to add to the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void addPath(Path path) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (path.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.CGPathAddPath(handle, null, path.handle);
	moved = false;
	closed = path.closed;
}

/**
 * Adds to the receiver the rectangle specified by x, y, width and height.
 *
 * @param x the x coordinate of the rectangle to add
 * @param y the y coordinate of the rectangle to add
 * @param width the width of the rectangle to add
 * @param height the height of the rectangle to add
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void addRectangle(float x, float y, float width, float height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	CGRect rect = new CGRect();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	OS.CGPathAddRect(handle, null, rect);
	moved = false;
	closed = true;
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
		OS.memmove(point, pt1, 8);
		OS.CGPathMoveToPoint(handle, null, originX + point[0], originY + point[1]);
	}
	OS.memmove(point, pt2, 8);
	OS.CGPathAddLineToPoint(handle, null, originX + point[0], originY + point[1]);
	return 0;
}

int curveProc(int pt1, int controlPt, int pt2, int data) {
	if (first) {
		first = false;
		OS.memmove(point, pt1, 8);
		OS.CGPathMoveToPoint(handle, null, originX + point[0], originY + point[1]);
	}
	OS.memmove(point, pt2, 8);
	float x2 = point[0], y2 = point[1];	
	OS.memmove(point, controlPt, 8);
	OS.CGPathAddQuadCurveToPoint(handle, null, originX + point[0], originY + point[1], originX + x2, originY + y2);
	return 0;
}

float originX, originY;
float[] point = new float[2];
boolean first;
/**
 * Adds to the receiver the pattern of glyphs generated by drawing
 * the given string using the given font starting at the point (x, y).
 *
 * @param string the text to use
 * @param x the x coordinate of the starting point
 * @param y the y coordinate of the starting point
 * @param font the font to use
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the font is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the font has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void addString(String string, float x, float y, Font font) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (font == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int length = string.length();
	if (length == 0) return;
	moved = false;
	closed = true;
	
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
	OS.memmove(textPtr, chars, length * 2);
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
		OS.memmove(record, layoutRecords[0] + (i * ATSLayoutRecord.sizeof), ATSLayoutRecord.sizeof);
		originX = x + (float)OS.Fix2X(record.realPos);
		if (deltaYs[0] == 0) {
			originY = y;
		} else {
			OS.memmove(deltaY, deltaYs[0] + (i * 4), 4);
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

CGPathElement element;
int count, typeCount;
byte[] types;
float[] points;
int applierFunc(int info, int elementPtr) {
	OS.memmove(element, elementPtr, CGPathElement.sizeof);
	int type = 0, length = 1;
	switch (element.type) {
		case OS.kCGPathElementMoveToPoint: type = SWT.PATH_MOVE_TO; break;
		case OS.kCGPathElementAddLineToPoint: type = SWT.PATH_LINE_TO; break;
		case OS.kCGPathElementAddQuadCurveToPoint: type = SWT.PATH_QUAD_TO; length = 2; break;
		case OS.kCGPathElementAddCurveToPoint: type = SWT.PATH_CUBIC_TO; length = 3; break;
		case OS.kCGPathElementCloseSubpath: type = SWT.PATH_CLOSE; length = 0; break;
	}
	if (types != null) {
		types[typeCount] = (byte)type;
		if (length > 0) {
			OS.memmove(point, element.points, length * 8);
			System.arraycopy(point, 0, points, count, length * 2);
		}
	}
	typeCount++;
	count += length * 2;
	return 0;
}

/**
 * Closes the current sub path by adding to the receiver a line
 * from the current point of the path back to the starting point
 * of the sub path.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void close() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.CGPathCloseSubpath(handle);
	moved = false;
	closed = true;
}

/**
 * Returns <code>true</code> if the specified point is contained by
 * the receiver and false otherwise.
 * <p>
 * If outline is <code>true</code>, the point (x, y) checked for containment in
 * the receiver's outline. If outline is <code>false</code>, the point is
 * checked to see if it is contained within the bounds of the (closed) area
 * covered by the receiver.
 *
 * @param x the x coordinate of the point to test for containment
 * @param y the y coordinate of the point to test for containment
 * @param gc the GC to use when testing for containment
 * @param outline controls whether to check the outline or contained area of the path
 * @return <code>true</code> if the path contains the point and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean contains(float x, float y, GC gc, boolean outline) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	gc.checkGC(GC.LINE_CAP | GC.LINE_JOIN | GC.LINE_STYLE | GC.LINE_WIDTH);
	//TODO - see windows
	int pixel = OS.NewPtr(4);
	if (pixel == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int[] buffer = new int[]{0xFFFFFFFF};
	OS.memmove(pixel, buffer, 4);
	int context = OS.CGBitmapContextCreate(pixel, 1, 1, 8, 4, device.colorspace, OS.kCGImageAlphaNoneSkipFirst);
	if (context == 0) {
		OS.DisposePtr(pixel);
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	GCData data = gc.data;
	int capStyle = 0;
	switch (data.lineCap) {
		case SWT.CAP_ROUND: capStyle = OS.kCGLineCapRound; break;
		case SWT.CAP_FLAT: capStyle = OS.kCGLineCapButt; break;
		case SWT.CAP_SQUARE: capStyle = OS.kCGLineCapSquare; break;
	}
	OS.CGContextSetLineCap(context, capStyle);
	int joinStyle = 0;
	switch (data.lineJoin) {
		case SWT.JOIN_MITER: joinStyle = OS.kCGLineJoinMiter; break;
		case SWT.JOIN_ROUND: joinStyle = OS.kCGLineJoinRound; break;
		case SWT.JOIN_BEVEL: joinStyle = OS.kCGLineJoinBevel; break;
	}
	OS.CGContextSetLineJoin(context, joinStyle);
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
	OS.memmove(buffer, pixel, 4);
	OS.DisposePtr(pixel);	
	return buffer[0] != 0xFFFFFFFF;
}

/**
 * Adds to the receiver a cubic bezier curve based on the parameters.
 *
 * @param cx1 the x coordinate of the first control point of the spline
 * @param cy1 the y coordinate of the first control of the spline
 * @param cx2 the x coordinate of the second control of the spline
 * @param cy2 the y coordinate of the second control of the spline
 * @param x the x coordinate of the end point of the spline
 * @param y the y coordinate of the end point of the spline
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void cubicTo(float cx1, float cy1, float cx2, float cy2, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (!moved) {
		CGPoint pt = new CGPoint();
		if (!OS.CGPathIsEmpty(handle)) OS.CGPathGetCurrentPoint(handle, pt);
		OS.CGPathMoveToPoint(handle, null, pt.x, pt.y);
		moved = true;
	}
	closed = false;
	OS.CGPathAddCurveToPoint(handle, null, cx1, cy1, cx2, cy2, x, y);
}

void destroy() {
	OS.CGPathRelease(handle);
	handle = 0;
}

/**
 * Replaces the first four elements in the parameter with values that
 * describe the smallest rectangle that will completely contain the
 * receiver (i.e. the bounding box).
 *
 * @param bounds the array to hold the result
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is too small to hold the bounding box</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
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

/**
 * Replaces the first two elements in the parameter with values that
 * describe the current point of the path.
 *
 * @param point the array to hold the result
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is too small to hold the end point</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void getCurrentPoint(float[] point) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (point == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (point.length < 2) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	CGPoint pt = new CGPoint();
	if (!OS.CGPathIsEmpty(handle)) OS.CGPathGetCurrentPoint(handle, pt);
	point[0] = pt.x;
	point[1] = pt.y;
}

/**
 * Returns a device independent representation of the receiver.
 * 
 * @return the PathData for the receiver
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see PathData
 */
public PathData getPathData() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Callback callback = new Callback(this, "applierFunc", 2);
	int proc = callback.getAddress();
	if (proc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	count = typeCount = 0;
	element = new CGPathElement();
	OS.CGPathApply(handle, 0, proc);
	types = new byte[typeCount];
	points = new float[count];
	point = new float[6];
	count = typeCount = 0;
	OS.CGPathApply(handle, 0, proc);
	callback.dispose();
	PathData result = new PathData();
	result.types = types;
	result.points = points;
	element = null;
	types = null;
	points = null;
	point = null;
	return result;
}

void init(PathData data) {
	byte[] types = data.types;
	float[] points = data.points;
	for (int i = 0, j = 0; i < types.length; i++) {
		switch (types[i]) {
			case SWT.PATH_MOVE_TO:
				moveTo(points[j++], points[j++]);
				break;
			case SWT.PATH_LINE_TO:
				lineTo(points[j++], points[j++]);
				break;
			case SWT.PATH_CUBIC_TO:
				cubicTo(points[j++], points[j++], points[j++], points[j++], points[j++], points[j++]);
				break;
			case SWT.PATH_QUAD_TO:
				quadTo(points[j++], points[j++], points[j++], points[j++]);
				break;
			case SWT.PATH_CLOSE:
				close();
				break;
			default:
				dispose();
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
}

/**
 * Returns <code>true</code> if the Path has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the Path.
 * When a Path has been disposed, it is an error to
 * invoke any other method using the Path.
 *
 * @return <code>true</code> when the Path is disposed, and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == 0;
}

/**
 * Adds to the receiver a line from the current point to
 * the point specified by (x, y).
 *
 * @param x the x coordinate of the end of the line to add
 * @param y the y coordinate of the end of the line to add
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void lineTo(float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (!moved) {
		CGPoint pt = new CGPoint();
		if (!OS.CGPathIsEmpty(handle)) OS.CGPathGetCurrentPoint(handle, pt);
		OS.CGPathMoveToPoint(handle, null, pt.x, pt.y);
		moved = true;
	}
	closed = false;
	OS.CGPathAddLineToPoint(handle, null, x, y);
}

/**
 * Sets the current point of the receiver to the point
 * specified by (x, y). Note that this starts a new
 * sub path.
 *
 * @param x the x coordinate of the new end point
 * @param y the y coordinate of the new end point
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void moveTo(float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.CGPathMoveToPoint(handle, null, x, y);
	/*
	* Bug in Quartz.  If CGPathMoveToPoint() is not called at the
	* begining of a subpath, the following segments do not output
	* anything.  The fix is to detect that the app did not call
	* CGPathMoveToPoint() and call it explicitly.
	*/
	closed = true;
	moved = true;
}

/**
 * Adds to the receiver a quadratic curve based on the parameters.
 *
 * @param cx the x coordinate of the control point of the spline
 * @param cy the y coordinate of the control point of the spline
 * @param x the x coordinate of the end point of the spline
 * @param y the y coordinate of the end point of the spline
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void quadTo(float cx, float cy, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (!moved) {
		CGPoint pt = new CGPoint();
		if (!OS.CGPathIsEmpty(handle)) OS.CGPathGetCurrentPoint(handle, pt);
		OS.CGPathMoveToPoint(handle, null, pt.x, pt.y);
		moved = true;
	}
	closed = false;
	OS.CGPathAddQuadCurveToPoint(handle, null, cx, cy, x, y);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Path {*DISPOSED*}";
	return "Path {" + handle + "}";
}

}

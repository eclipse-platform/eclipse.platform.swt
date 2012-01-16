/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
	 * 
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public NSBezierPath handle;

	boolean closed = true;

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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		handle = NSBezierPath.bezierPath();
		if (handle == null) SWT.error(SWT.ERROR_NO_HANDLES);
		handle.retain();
		handle.moveToPoint(new NSPoint());
		init();
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		if (path == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (path.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		flatness = Math.max(0, flatness);
		if (flatness == 0) {
			handle = new NSBezierPath(path.handle.copy().id);
		} else {
			float /*double*/ defaultFlatness = NSBezierPath.defaultFlatness();
			NSBezierPath.setDefaultFlatness(flatness);
			handle = path.handle.bezierPathByFlatteningPath();
			handle.retain();
			NSBezierPath.setDefaultFlatness(defaultFlatness);		
		}
		if (handle == null) SWT.error(SWT.ERROR_NO_HANDLES);
		init();
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		init(data);
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSAffineTransform transform = NSAffineTransform.transform();
		transform.translateXBy(x + width / 2f, y + height / 2f);
		transform.scaleXBy(width / 2f, height / 2f);
		NSBezierPath path = NSBezierPath.bezierPath();
		NSPoint center = new NSPoint();
		float sAngle = -startAngle;
		float eAngle = -(startAngle + arcAngle);
		path.appendBezierPathWithArcWithCenter(center, 1, sAngle,  eAngle, arcAngle>0);
		path.transformUsingAffineTransform(transform);
//		handle.appendBezierPath(path);
		appendBezierPath(path);
		if (closed = (Math.abs(arcAngle) >= 360)) {
			handle.closePath();
		}
	} finally { 
		if (pool != null) pool.release();
	}
}

void appendBezierPath (NSBezierPath path) {
	int count = (int)/*64*/path.elementCount();
	int /*long*/ points = OS.malloc(3 * NSPoint.sizeof);
	if (points == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	NSPoint pt1 = new NSPoint();
	NSPoint pt2 = new NSPoint();
	NSPoint pt3 = new NSPoint();
	for (int i = 0; i < count; i++) {
		int element = (int)/*64*/path.elementAtIndex(i, points);
		switch (element) {
			case OS.NSMoveToBezierPathElement:
				OS.memmove(pt1, points, NSPoint.sizeof);
				if (closed) {
					handle.moveToPoint(pt1);
				} else {
					handle.lineToPoint(pt1);	
				}
				break;
			case OS.NSLineToBezierPathElement:
				OS.memmove(pt1, points, NSPoint.sizeof);
				handle.lineToPoint(pt1);
				closed = false;
				break;
			case OS.NSCurveToBezierPathElement:
				OS.memmove(pt1, points, NSPoint.sizeof);
				OS.memmove(pt2, points + NSPoint.sizeof, NSPoint.sizeof);
				OS.memmove(pt3, points + NSPoint.sizeof + NSPoint.sizeof, NSPoint.sizeof);
				handle.curveToPoint(pt3, pt1, pt2);
				closed = false;
				break;
			case OS.NSClosePathBezierPathElement:
				handle.closePath();
				closed = true;
				break;
		}
	}
	OS.free(points);
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		handle.appendBezierPath(path.handle);
		closed = path.closed;
	} finally {
		if (pool != null) pool.release();
	}
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
	NSRect rect = new NSRect();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		handle.appendBezierPathWithRect(rect);
		closed = true;
	} finally {
		if (pool != null) pool.release();
	}
}

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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		closed = true;
		NSString str = NSString.stringWith(string);
		NSTextStorage textStorage = (NSTextStorage)new NSTextStorage().alloc().init();
		NSLayoutManager layoutManager = (NSLayoutManager)new NSLayoutManager().alloc().init();
		NSTextContainer textContainer = (NSTextContainer)new NSTextContainer().alloc();
		NSSize size = new NSSize();
		size.width = OS.MAX_TEXT_CONTAINER_SIZE;
		size.height = OS.MAX_TEXT_CONTAINER_SIZE;
		textContainer.initWithContainerSize(size);
		textContainer.setLineFragmentPadding(0);
		textStorage.addLayoutManager(layoutManager);
		layoutManager.addTextContainer(textContainer);
		NSRange range = new NSRange();
		range.length = str.length();
		/*
		* Feature in Cocoa. Adding attributes directly to a NSTextStorage causes
		* output to the console and eventually a segmentation fault when printing 
		* on a thread other than the main thread. The fix is to add attributes to
		* a separate NSMutableAttributedString and add it to text storage when done.
		*/
		NSMutableAttributedString attrStr = (NSMutableAttributedString)new NSMutableAttributedString().alloc();
		attrStr.id = attrStr.initWithString(str).id;
		attrStr.beginEditing();
		attrStr.addAttribute(OS.NSFontAttributeName, font.handle, range);
		font.addTraits(attrStr, range);
		attrStr.endEditing();
		textStorage.setAttributedString(attrStr);
		attrStr.release();
		range = layoutManager.glyphRangeForTextContainer(textContainer);
		if (range.length != 0) {
			int /*long*/ glyphs = OS.malloc((range.length + 1) * 4);
			int count = layoutManager.getGlyphs(glyphs, range);
			NSBezierPath path = NSBezierPath.bezierPath();
			for (int i = 0; i < count; i++) {
				NSPoint pt = layoutManager.locationForGlyphAtIndex(i);
				NSRect lineFragmentRect = layoutManager.lineFragmentUsedRectForGlyphAtIndex(i, 0);
				NSFont actualFont = new NSFont(textStorage.attribute(OS.NSFontAttributeName, layoutManager.characterIndexForGlyphAtIndex(i), 0));
				pt.x = pt.x + x + lineFragmentRect.x;
				pt.y =  - pt.y - y - lineFragmentRect.y;
				path.moveToPoint(pt);
				path.appendBezierPathWithGlyphs(glyphs + (i * 4), 1, actualFont);
			}
			OS.free(glyphs);
			NSAffineTransform transform = NSAffineTransform.transform();
			transform.scaleXBy(1, -1);
			path.transformUsingAffineTransform(transform);
			handle.appendBezierPath(path);
		}
		textContainer.release();
		layoutManager.release();
		textStorage.release();
	} finally  {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		handle.closePath();
		closed = true;
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		//TODO - see windows
		if (outline) {
			int /*long*/ pixel = OS.malloc(4);
			if (pixel == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			int[] buffer = new int[]{0xFFFFFFFF};
			OS.memmove(pixel, buffer, 4);
			int /*long*/ colorspace = OS.CGColorSpaceCreateDeviceRGB();
			int /*long*/ context = OS.CGBitmapContextCreate(pixel, 1, 1, 8, 4, colorspace, OS.kCGImageAlphaNoneSkipFirst);
			OS.CGColorSpaceRelease(colorspace);
			if (context == 0) {
				OS.free(pixel);
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
			int /*long*/ path = GC.createCGPathRef(handle);
			OS.CGContextAddPath(context, path);
			OS.CGPathRelease(path);
			OS.CGContextStrokePath(context);
			OS.CGContextRelease(context);
			OS.memmove(buffer, pixel, 4);
			OS.free(pixel);	
			return buffer[0] != 0xFFFFFFFF;			
		} else {
			NSPoint point = new NSPoint();
			point.x = x;
			point.y = y;
			return handle.containsPoint(point);
		}
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSPoint pt = new NSPoint();
		pt.x = x;
		pt.y = y;
		NSPoint ct1 = new NSPoint();
		ct1.x = cx1;
		ct1.y = cy1;
		NSPoint ct2 = new NSPoint();
		ct2.x = cx2;
		ct2.y = cy2;
		handle.curveToPoint(pt, ct1, ct2);
		closed = false;
	} finally {
		if (pool != null) pool.release();
	}
}

void destroy() {
	handle.release();
	handle = null;
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSRect rect = handle.controlPointBounds();
		bounds[0] = (float)/*64*/rect.x;
		bounds[1] = (float)/*64*/rect.y;
		bounds[2] = (float)/*64*/rect.width;
		bounds[3] = (float)/*64*/rect.height;
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSPoint pt = handle.currentPoint();
		point[0] = (float)/*64*/pt.x;
		point[1] = (float)/*64*/pt.y;
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		int count = (int)/*64*/handle.elementCount();
		int pointCount = 0, typeCount = 0;
		byte[] types = new byte[count];
		float[] pointArray = new float[count * 6];
		int /*long*/ points = OS.malloc(3 * NSPoint.sizeof);
		if (points == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		NSPoint pt = new NSPoint();
		for (int i = 0; i < count; i++) {
			int element = (int)/*64*/handle.elementAtIndex(i, points);
			switch (element) {
				case OS.NSMoveToBezierPathElement:
					types[typeCount++] = SWT.PATH_MOVE_TO;
					OS.memmove(pt, points, NSPoint.sizeof);
					pointArray[pointCount++] = (float)pt.x;
					pointArray[pointCount++] = (float)pt.y;
					break;
				case OS.NSLineToBezierPathElement:
					types[typeCount++] = SWT.PATH_LINE_TO;
					OS.memmove(pt, points, NSPoint.sizeof);
					pointArray[pointCount++] = (float)pt.x;
					pointArray[pointCount++] = (float)pt.y;
					break;
				case OS.NSCurveToBezierPathElement:
					types[typeCount++] = SWT.PATH_CUBIC_TO;
					OS.memmove(pt, points, NSPoint.sizeof);
					pointArray[pointCount++] = (float)pt.x;
					pointArray[pointCount++] = (float)pt.y;
					OS.memmove(pt, points + NSPoint.sizeof, NSPoint.sizeof);
					pointArray[pointCount++] = (float)pt.x;
					pointArray[pointCount++] = (float)pt.y;
					OS.memmove(pt, points + NSPoint.sizeof + NSPoint.sizeof, NSPoint.sizeof);
					pointArray[pointCount++] = (float)pt.x;
					pointArray[pointCount++] = (float)pt.y;
					break;
				case OS.NSClosePathBezierPathElement:
					types[typeCount++] = SWT.PATH_CLOSE;
					break;
			}
		}
		OS.free(points);
		if (pointCount != pointArray.length) {
			float[] temp = new float[pointCount];
			System.arraycopy(pointArray, 0, temp, 0, pointCount);
			pointArray = temp;
		}
		PathData data = new PathData();
		data.types = types;
		data.points = pointArray;
		return data;
	} finally {
		if (pool != null)  pool.release();
	}
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
 * invoke any other method (except {@link #dispose()}) using the Path.
 *
 * @return <code>true</code> when the Path is disposed, and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == null;
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSPoint pt = new NSPoint();
		pt.x = x;
		pt.y = y;
		handle.lineToPoint(pt);
		closed = false;
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSPoint pt = new NSPoint();
		pt.x = x;
		pt.y = y;
		handle.moveToPoint(pt);
		closed = true;
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSPoint current = handle.isEmpty() ? new NSPoint() : handle.currentPoint();
		NSPoint ct1 = new NSPoint();
		ct1.x = current.x + 2 * (cx - current.x) / 3;
		ct1.y = current.y + 2 * (cy - current.y) / 3;
		NSPoint ct2 = new NSPoint();
		ct2.x = ct1.x + (x - current.x) / 3;
		ct2.y = ct1.y + (y - current.y) / 3;		
		NSPoint pt = new NSPoint();
		pt.x = x;
		pt.y = y;
		handle.curveToPoint(pt, ct1, ct2);
		closed = false;
	} finally {
		if (pool != null) pool.release();
	}
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

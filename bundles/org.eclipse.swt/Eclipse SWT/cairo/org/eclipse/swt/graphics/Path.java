/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
import org.eclipse.swt.internal.cairo.*;

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
	public int /*long*/ handle;
	
	boolean move;

/**
 * Constructs a new empty Path.
 * 
 * @param device the device on which to allocate the path
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the device is null and there is no current device</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the path could not be obtained/li>
 * </ul>
 * 
 * @see #dispose()
 */
public Path (Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	device.checkCairo();
	int /*long*/ surface = Cairo.cairo_image_surface_create(Cairo.CAIRO_FORMAT_ARGB32, 1, 1);
	if (surface == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	handle = Cairo.cairo_create(surface);
	Cairo.cairo_surface_destroy(surface);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
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
	move = true;
	if (width == height) {
		if (arcAngle >= 0) {
			Cairo.cairo_arc_negative(handle, x + width / 2f, y + height / 2f, width / 2f, -startAngle * (float)Compatibility.PI / 180, -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
		} else {
			Cairo.cairo_arc(handle, x + width / 2f, y + height / 2f, width / 2f, -startAngle * (float)Compatibility.PI / 180, -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
		}
	} else {
		Cairo.cairo_save(handle);
		Cairo.cairo_translate(handle, x + width / 2f, y + height / 2f);
		Cairo.cairo_scale(handle, width / 2f, height / 2f);
		if (arcAngle >= 0) {
			Cairo.cairo_arc_negative(handle, 0, 0, 1, -startAngle * (float)Compatibility.PI / 180, -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
		} else {
			Cairo.cairo_arc(handle, 0, 0, 1, -startAngle * (float)Compatibility.PI / 180, -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
		}
		Cairo.cairo_restore(handle);
	}
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
	move = false;
	int /*long*/ copy = Cairo.cairo_copy_path(path.handle);
	if (copy == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	Cairo.cairo_append_path(handle, copy);
	Cairo.cairo_path_destroy(copy);
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
	move = false;
	Cairo.cairo_rectangle(handle, x, y, width, height);
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
	move = false;
	GC.addCairoString(handle, string, x, y, font);
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
	Cairo.cairo_close_path(handle);
	move = false;
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
	//TODO - see Windows
	gc.initCairo();
	boolean result = false;
	int /*long*/ cairo = gc.data.cairo;
	int /*long*/ copy = Cairo.cairo_copy_path(handle);
	if (copy == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	Cairo.cairo_append_path(cairo, copy);
	Cairo.cairo_path_destroy(copy);
	if (outline) {
		result = Cairo.cairo_in_stroke(cairo, x, y) != 0;		
	} else {
		result = Cairo.cairo_in_fill(cairo, x, y) != 0;
	}
	Cairo.cairo_new_path(cairo);
	return result;
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
	if (!move) {
		double[] currentX = new double[1], currentY = new double[1];
		Cairo.cairo_get_current_point(handle, currentX, currentY);
		Cairo.cairo_move_to(handle, currentX[0], currentY[0]);
	}
	move = true;
	Cairo.cairo_curve_to(handle, cx1, cy1, cx2, cy2, x, y);
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
	int /*long*/ copy = Cairo.cairo_copy_path(handle);
	if (copy == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	cairo_path_t path = new cairo_path_t();
	Cairo.memmove(path, copy, cairo_path_t.sizeof);
	double minX = 0, minY = 0, maxX = 0, maxY = 0;
	if (path.num_data > 0) {
		minX = minY = Double.POSITIVE_INFINITY;
		maxX = maxY = Double.NEGATIVE_INFINITY;
		int i = 0;
		double[] points = new double[6]; 
		cairo_path_data_t data = new cairo_path_data_t();
		while (i < path.num_data) {
			int /*long*/ offset = path.data + i * cairo_path_data_t.sizeof;
			Cairo.memmove(data, offset, cairo_path_data_t.sizeof);
			switch (data.type) {
				case Cairo.CAIRO_PATH_MOVE_TO:
					Cairo.memmove(points, offset + cairo_path_data_t.sizeof, cairo_path_data_t.sizeof);
					minX = Math.min(minX, points[0]);
					minY = Math.min(minY, points[1]);
					maxX = Math.max(maxX, points[0]);
					maxY = Math.max(maxY, points[1]);
					break;
				case Cairo.CAIRO_PATH_LINE_TO:
					Cairo.memmove(points, offset + cairo_path_data_t.sizeof, cairo_path_data_t.sizeof);
					minX = Math.min(minX, points[0]);
					minY = Math.min(minY, points[1]);
					maxX = Math.max(maxX, points[0]);
					maxY = Math.max(maxY, points[1]);
					break;
				case Cairo.CAIRO_PATH_CURVE_TO:
					Cairo.memmove(points, offset + cairo_path_data_t.sizeof, cairo_path_data_t.sizeof * 3);
					minX = Math.min(minX, points[0]);
					minY = Math.min(minY, points[1]);
					maxX = Math.max(maxX, points[0]);
					maxY = Math.max(maxY, points[1]);
					minX = Math.min(minX, points[2]);
					minY = Math.min(minY, points[3]);
					maxX = Math.max(maxX, points[2]);
					maxY = Math.max(maxY, points[3]);
					minX = Math.min(minX, points[4]);
					minY = Math.min(minY, points[5]);
					maxX = Math.max(maxX, points[4]);
					maxY = Math.max(maxY, points[5]);
					break;
				case Cairo.CAIRO_PATH_CLOSE_PATH: break;
			}
			i += data.length;
		}
	}
	bounds[0] = (float)minX;
	bounds[1] = (float)minY;
	bounds[2] = (float)(maxX - minX);
	bounds[3] = (float)(maxY - minY);
	Cairo.cairo_path_destroy(copy);
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
	double[] x = new double[1], y = new double[1];
	Cairo.cairo_get_current_point(handle, x, y);
	point[0] = (float)x[0];
	point[1] = (float)y[0];
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
	int /*long*/ copy = Cairo.cairo_copy_path(handle);
	if (copy == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	cairo_path_t path = new cairo_path_t();
	Cairo.memmove(path, copy, cairo_path_t.sizeof);
	byte[] types = new byte[path.num_data];
	float[] pts = new float[path.num_data * 6];
	int typeIndex = 0, ptsIndex = 0;
	if (path.num_data > 0) {
		int i = 0;
		double[] points = new double[6]; 
		cairo_path_data_t data = new cairo_path_data_t();
		while (i < path.num_data) {
			int /*long*/ offset = path.data + i * cairo_path_data_t.sizeof;
			Cairo.memmove(data, offset, cairo_path_data_t.sizeof);
			switch (data.type) {
				case Cairo.CAIRO_PATH_MOVE_TO:
					types[typeIndex++] = SWT.PATH_MOVE_TO;
					Cairo.memmove(points, offset + cairo_path_data_t.sizeof, cairo_path_data_t.sizeof);
					pts[ptsIndex++] = (float)points[0];
					pts[ptsIndex++] = (float)points[1];
					break;
				case Cairo.CAIRO_PATH_LINE_TO:
					types[typeIndex++] = SWT.PATH_LINE_TO;
					Cairo.memmove(points, offset + cairo_path_data_t.sizeof, cairo_path_data_t.sizeof);
					pts[ptsIndex++] = (float)points[0];
					pts[ptsIndex++] = (float)points[1];
					break;
				case Cairo.CAIRO_PATH_CURVE_TO:
					types[typeIndex++] = SWT.PATH_CUBIC_TO;
					Cairo.memmove(points, offset + cairo_path_data_t.sizeof, cairo_path_data_t.sizeof * 3);
					pts[ptsIndex++] = (float)points[0];
					pts[ptsIndex++] = (float)points[1];
					pts[ptsIndex++] = (float)points[2];
					pts[ptsIndex++] = (float)points[3];
					pts[ptsIndex++] = (float)points[4];
					pts[ptsIndex++] = (float)points[5];
					break;
				case Cairo.CAIRO_PATH_CLOSE_PATH:
					types[typeIndex++] = SWT.PATH_CLOSE;
					break;
			}
			i += data.length;
		}
	}
	if (typeIndex != types.length) {
		byte[] newTypes = new byte[typeIndex];
		System.arraycopy(types, 0, newTypes, 0, typeIndex);
		types = newTypes;
	}
	if (ptsIndex != pts.length) {
		float[] newPts = new float[ptsIndex];
		System.arraycopy(pts, 0, newPts, 0, ptsIndex);
		pts = newPts;
	}
	Cairo.cairo_path_destroy(copy);
	PathData result = new PathData();
	result.types = types;
	result.points = pts;
	return result;
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
	if (!move) {
		double[] currentX = new double[1], currentY = new double[1];
		Cairo.cairo_get_current_point(handle, currentX, currentY);
		Cairo.cairo_move_to(handle, currentX[0], currentY[0]);
	}
	move = true;
	Cairo.cairo_line_to(handle, x, y);
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
	/*
	* Bug in Cairo.  If cairo_move_to() is not called at the
	* begining of a subpath, the first cairo_line_to() or
	* cairo_curve_to() segment do not output anything.  The fix
	* is to detect that the app did not call cairo_move_to()
	* before those calls and call them explicitly. 
	*/
	move = true;
	Cairo.cairo_move_to(handle, x, y);
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
	double[] currentX = new double[1], currentY = new double[1];
	Cairo.cairo_get_current_point(handle, currentX, currentY);
	if (!move) {
		Cairo.cairo_move_to(handle, currentX[0], currentY[0]);
	}
	move = true;
	float x0 = (float)currentX[0];
	float y0 = (float)currentY[0];
	float cx1 = x0 + 2 * (cx - x0) / 3;
	float cy1 = y0 + 2 * (cy - y0) / 3;
	float cx2 = cx1 + (x - x0) / 3;
	float cy2 = cy1 + (y - y0) / 3;
	Cairo.cairo_curve_to(handle, cx1, cy1, cx2, cy2, x, y);
}

/**
 * Disposes of the operating system resources associated with
 * the Path. Applications must dispose of all Paths that
 * they allocate.
 */
public void dispose() {
	if (handle == 0) return;
	Cairo.cairo_destroy(handle);
	handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
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
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString() {
	if (isDisposed()) return "Path {*DISPOSED*}";
	return "Path {" + handle + "}";
}

}

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
import org.eclipse.swt.internal.wpf.*;

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
	public int handle;
	
	int currentFigure, currentPoint;
	
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
	handle = OS.gcnew_PathGeometry();
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
		handle = OS.PathGeometry_Clone(path.handle);
	} else {
		handle = OS.Geometry_GetFlattenedPathGeometry(path.handle, flatness, OS.ToleranceType_Absolute);
	}
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
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
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	boolean isNegative = arcAngle < 0;
	boolean isLargeAngle = arcAngle > 180 || arcAngle < -180;
	arcAngle = arcAngle + startAngle;
	if (isNegative) {
		// swap angles
	   	float tmp = startAngle;
		startAngle = arcAngle;
		arcAngle = tmp;
	}
	double x1 = Math.cos(startAngle * Math.PI / 180) * width/2.0 + x + width/2.0;
	double y1 = -1 * Math.sin(startAngle * Math.PI / 180) * height/2.0 + y + height/2.0;
	double x2 = Math.cos(arcAngle * Math.PI / 180) * width/2.0 + x + width/2.0;
	double y2 = -1 * Math.sin(arcAngle * Math.PI / 180) * height/2.0 + y + height/2.0;	
	if (currentFigure == 0) {
		currentFigure = OS.gcnew_PathFigure();
		int figures = OS.PathGeometry_Figures(handle);
		OS.PathFigureCollection_Add(figures, currentFigure);
		OS.GCHandle_Free(figures);
	}
	int startPoint = OS.gcnew_Point(x1, y1);
	int endPoint = OS.gcnew_Point(x2, y2);
	int segments = OS.PathFigure_Segments(currentFigure);
	if (OS.PathSegmentCollection_Count(segments) != 0) {
		int segment = OS.gcnew_LineSegment(startPoint, true);
		OS.PathSegmentCollection_Add(segments, segment);
		OS.GCHandle_Free(segment);
	} else {
		OS.PathFigure_StartPoint(currentFigure, startPoint);
	}
	if (arcAngle >= 360 || arcAngle <= -360) {
		int rect = OS.gcnew_Rect(x, y, width, height);
		int geometry = OS.gcnew_EllipseGeometry(rect);
		OS.PathGeometry_AddGeometry(handle, geometry);
		OS.GCHandle_Free(geometry);
		OS.GCHandle_Free(rect);
		OS.GCHandle_Free(currentFigure);
		currentFigure = 0;
	} else {
		int size = OS.gcnew_Size(width / 2.0, height / 2.0);
		int arc = OS.gcnew_ArcSegment(endPoint, size, 0, isLargeAngle, OS.SweepDirection_Clockwise, true);
		OS.PathSegmentCollection_Add(segments, arc);
		OS.GCHandle_Free(size);
		OS.GCHandle_Free(arc);
	}
	OS.GCHandle_Free(segments);
	OS.GCHandle_Free(startPoint);
	if (currentPoint != 0) OS.GCHandle_Free(currentPoint);
	currentPoint = endPoint;	
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
	OS.PathGeometry_AddGeometry(handle, path.handle);
	if (path.currentPoint != 0) {
		currentPoint = OS.gcnew_Point(OS.Point_X(path.currentPoint), OS.Point_Y(path.currentPoint));
	}
	int figures = OS.PathGeometry_Figures(handle);
	int count = OS.PathFigureCollection_Count(figures);
	OS.GCHandle_Free(figures);
	if (count != 0) {
		int figure = OS.PathGeometry_Figures(handle, count - 1);
		if (!OS.PathFigure_IsClosed(figure)) {
			currentFigure = figure;
			return;
		}
		OS.GCHandle_Free(figure);
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
	int rect = OS.gcnew_Rect(x, y, width, height);
	int geometry = OS.gcnew_RectangleGeometry(rect);
	OS.PathGeometry_AddGeometry(handle, geometry);
	OS.GCHandle_Free(geometry);
	OS.GCHandle_Free(rect);
	if (currentFigure != 0) OS.GCHandle_Free(currentFigure);
	currentFigure = 0;
	int point = OS.gcnew_Point(x, y);
	if (currentPoint != 0) OS.GCHandle_Free(currentPoint);
	currentPoint = point;
	
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
	int length = string.length();
	char [] buffer = new char [length + 1];
	string.getChars (0, length, buffer, 0);
	int str = OS.gcnew_String (buffer);
	int culture = OS.CultureInfo_CurrentUICulture();
	int point = OS.gcnew_Point(x, y);
	int brush = OS.Brushes_White();
	int text = OS.gcnew_FormattedText(str, culture, OS.FlowDirection_LeftToRight, font.handle, font.size, brush);
	int geometry = OS.FormattedText_BuildGeometry(text, point);
	OS.PathGeometry_AddGeometry(handle, geometry);
	OS.GCHandle_Free(brush);
	OS.GCHandle_Free(geometry);
	OS.GCHandle_Free(culture);
	OS.GCHandle_Free(str);
	OS.GCHandle_Free(point);
	OS.GCHandle_Free(text);
	if (currentFigure != 0) OS.GCHandle_Free(currentFigure);
	currentFigure = 0;
	if (currentPoint != 0) OS.GCHandle_Free(currentPoint);
	currentPoint = 0;
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
	if (currentFigure != 0) OS.PathFigure_IsClosed(currentFigure, true);
	currentFigure = 0;
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
	gc.checkGC(GC.LINE_CAP | GC.LINE_JOIN | GC.LINE_STYLE | GC.LINE_WIDTH | GC.LINE_MITERLIMIT | GC.TRANSFORM);
	boolean result;
	int point = OS.gcnew_Point(x, y);
	if (outline) {
		result = OS.Geometry_StrokeContains(handle, gc.data.pen, point);
	} else {
		result = OS.Geometry_FillContains(handle, point);
	}
	OS.GCHandle_Free(point);
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
	int controlPoint1 = OS.gcnew_Point(cx1, cy1);
	int controlPoint2 = OS.gcnew_Point(cx2, cy2);
	int point = OS.gcnew_Point(x, y);
	if (currentFigure == 0) newFigure();
	if (currentPoint != 0) OS.GCHandle_Free(currentPoint);
	currentPoint = point;
	int segment = OS.gcnew_BezierSegment(controlPoint1, controlPoint2, point, true);
	int segments = OS.PathFigure_Segments(currentFigure);
	OS.PathSegmentCollection_Add(segments, segment);
	OS.GCHandle_Free(segments);
	OS.GCHandle_Free(segment);
	OS.GCHandle_Free(controlPoint1);
	OS.GCHandle_Free(controlPoint2);
}

void destroy() {
	OS.GCHandle_Free(handle);
	handle = 0;
	if (currentFigure != 0) OS.GCHandle_Free(currentFigure);
	currentFigure = 0;
	if (currentPoint != 0) OS.GCHandle_Free(currentPoint);
	currentPoint = 0;
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
	int rect = OS.PathGeometry_Bounds(handle);
	bounds[0] = (float)OS.Rect_X(rect);
	bounds[1] = (float)OS.Rect_Y(rect);
	bounds[2] = (float)OS.Rect_Width(rect);
	bounds[3] = (float)OS.Rect_Height(rect);
	OS.GCHandle_Free(rect);
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
	if (currentPoint != 0) {
		point[0] = (float)OS.Point_X(currentPoint);
		point[1] = (float)OS.Point_Y(currentPoint);
	} else {
		point[0] = point[1] = 0;
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
	byte[] types = new byte[128];
	float[] points = new float[128 * 2];
	int pointsIndex = 0, typesIndex = 0;
	int figures = OS.PathGeometry_Figures(handle);
	int figureCount = OS.PathFigureCollection_Count(figures);
	OS.GCHandle_Free(figures);
	for (int i = 0; i < figureCount; i++) {
		int figure = OS.PathGeometry_Figures(handle, i);
		int segments = OS.PathFigure_Segments(figure);
		int segmentCount = OS.PathSegmentCollection_Count(segments);
		OS.GCHandle_Free(segments);
		for (int j = 0; j < segmentCount; j++) {
			int segment = OS.PathFigure_Segments(figure, j);
			int type = OS.Object_GetType(segment);
			//TODO - need get points out of every segment
			//TODO - need to convert ArcSegment to beziers
			
			
			OS.GCHandle_Free(type);
			OS.GCHandle_Free(segment);
		}
		OS.GCHandle_Free(figure);
	}
	if (typesIndex != types.length) {
		byte[] newTypes = new byte[typesIndex];
		System.arraycopy(types, 0, newTypes, 0, typesIndex);
		types = newTypes;
	}
	if (pointsIndex != points.length) {
		float[] newPoints  = new float[pointsIndex];
		System.arraycopy(points, 0, newPoints, 0, pointsIndex);
		points = newPoints;
	}
	PathData result = new PathData();
	result.types = types;
	result.points = points;
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
	int point = OS.gcnew_Point(x, y);
	if (currentFigure == 0) newFigure();
	if (currentPoint != 0) OS.GCHandle_Free(currentPoint);
	currentPoint = point;
	int segment = OS.gcnew_LineSegment(point, true);
	int segments = OS.PathFigure_Segments(currentFigure);
	OS.PathSegmentCollection_Add(segments, segment);
	OS.GCHandle_Free(segments);
	OS.GCHandle_Free(segment);
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
	return handle == 0;
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
	int point = OS.gcnew_Point(x, y);
	if (currentPoint != 0) OS.GCHandle_Free(currentPoint);
	currentPoint = point;
	if (currentFigure != 0) {
		int segments = OS.PathFigure_Segments(currentFigure);
		int count = OS.PathSegmentCollection_Count(segments);
		OS.GCHandle_Free(segments);
		if (count == 0) {
			OS.PathFigure_StartPoint(currentFigure, point);
			return;
		}
	}
	if (currentFigure != 0) OS.GCHandle_Free(currentFigure);
	newFigure();
}

void newFigure() {
	currentFigure = OS.gcnew_PathFigure();
	if (currentPoint != 0) {
		OS.PathFigure_StartPoint(currentFigure, currentPoint);
	}
	int figures = OS.PathGeometry_Figures(handle);
	OS.PathFigureCollection_Add(figures, currentFigure);
	OS.GCHandle_Free(figures);
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
	int controlPoint = OS.gcnew_Point(cx, cy);
	int point = OS.gcnew_Point(x, y);
	if (currentFigure == 0) newFigure();
	if (currentPoint != 0) OS.GCHandle_Free(currentPoint);
	currentPoint = point;
	int segment = OS.gcnew_QuadraticBezierSegment(controlPoint, point, true);
	int segments = OS.PathFigure_Segments(currentFigure);
	OS.PathSegmentCollection_Add(segments, segment);
	OS.GCHandle_Free(segments);
	OS.GCHandle_Free(segment);
	OS.GCHandle_Free(controlPoint);
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

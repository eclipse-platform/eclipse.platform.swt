/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.util.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gdip.*;
import org.eclipse.swt.internal.win32.*;

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
	private Map<Integer, PathHandle> zoomToHandle = new HashMap<>();

	private List<Operation> operations = new ArrayList<>();

	private boolean isDestroyed;

/**
 * Constructs a new empty Path.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * <p>
 * You must dispose the path when it is no longer required.
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
	this.device.checkGDIP();
	init();
	this.device.registerResourceWithZoomSupport(this);
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
 * <p>
 * You must dispose the path when it is no longer required.
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
	path.operations.forEach(this::storeAndApplyOperationOnAllHandles);
	if (flatness != 0) {
		storeAndApplyOperationOnAllHandles(new FlattenOperation(flatness));
	}
	init();
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * Constructs a new Path with the specified PathData.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * <p>
 * You must dispose the path when it is no longer required.
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
 * The resulting arc covers an area <code>width + 1</code> points wide
 * by <code>height + 1</code> points tall.
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
public void addArc (float x, float y, float width, float height, float startAngle, float arcAngle) {
	if (width == 0 || height == 0 || arcAngle == 0) return;
	storeAndApplyOperationOnAllHandles(new AddArcOperation(x, y, width, height, startAngle, arcAngle));
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
	storeAndApplyOperationOnAllHandles(new AddPathOperation(path));
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
public void addRectangle (float x, float y, float width, float height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	storeAndApplyOperationOnAllHandles(new AddRectangleOperation(x, y, width, height));
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
public void addString (String string, float x, float y, Font font) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (font == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	storeAndApplyOperationOnAllHandles(new AddStringOperation(string, x, y, font));
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
	storeAndApplyOperationOnAllHandles(new CloseOperation());
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
public boolean contains (float x, float y, GC gc, boolean outline) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return applyUsingAnyHandle(handle -> {
		return handle.contains(x, y, gc, outline);
	});
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
public void cubicTo (float cx1, float cy1, float cx2, float cy2, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	storeAndApplyOperationOnAllHandles(new CubicToOperation(cx1, cy1, cx2, cy2, x, y));
}

@Override
void destroy() {
	device.deregisterResourceWithZoomSupport(this);
	zoomToHandle.values().forEach(PathHandle::destroy);
	zoomToHandle.clear();
	this.isDestroyed = true;
}

@Override
void destroyHandlesExcept(Set<Integer> zoomLevels) {
	zoomToHandle.entrySet().removeIf(entry -> {
		final Integer zoom = entry.getKey();
		if (!zoomLevels.contains(zoom)) {
			entry.getValue().destroy();
			return true;
		}
		return false;
	});
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
public void getBounds (float[] bounds) {
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (bounds.length < 4) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	applyUsingAnyHandle(handle -> {
		handle.fillBounds(bounds);
		return true;
	});
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
public void getCurrentPoint (float[] point) {
	if (point == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (point.length < 2) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	applyUsingAnyHandle(handle -> {
		handle.fillCurrentPoint(point);
		return true;
	});
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
	return applyUsingAnyHandle(handle -> {
		return handle.getPathData();
	});
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
public void lineTo (float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	storeAndApplyOperationOnAllHandles(new LineToOperation(x, y));
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
@Override
public boolean isDisposed() {
	return this.isDestroyed;
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
public void moveTo (float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	storeAndApplyOperationOnAllHandles(new MoveToOperation(x, y));
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
public void quadTo (float cx, float cy, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	storeAndApplyOperationOnAllHandles(new QuadToOperation(cx, cy, x, y));
}

private static class PathHandle {
	private final Device device;
	private final long handle;
	private final int zoom;
	private PointF currentPoint = new PointF();
	private PointF startPoint = new PointF();

	public PathHandle(final Device device, final long handle, final int zoom) {
		this.device = device;
		this.handle = handle;
		this.zoom = zoom;
	}

	boolean contains (float x, float y, GC gc, boolean outline) {
		float xInPixels = Win32DPIUtils.pointToPixel(device, x, zoom);
		float yInPixels = Win32DPIUtils.pointToPixel(device, y, zoom);
		return containsInPixels(xInPixels, yInPixels, gc, outline);
	}

	private boolean containsInPixels(float x, float y, GC gc, boolean outline) {
		//TODO - should use GC transformation
		gc.initGdip();
		gc.checkGC(GC.LINE_CAP | GC.LINE_JOIN | GC.LINE_STYLE | GC.LINE_WIDTH);
		int mode = OS.GetPolyFillMode(gc.handle) == OS.WINDING ? Gdip.FillModeWinding : Gdip.FillModeAlternate;
		Gdip.GraphicsPath_SetFillMode(handle, mode);
		if (outline) {
			return Gdip.GraphicsPath_IsOutlineVisible(handle, x, y, gc.data.gdipPen, gc.data.gdipGraphics);
		} else {
			return Gdip.GraphicsPath_IsVisible(handle, x, y, gc.data.gdipGraphics);
		}
	}

	void destroy() {
		Gdip.GraphicsPath_delete(handle);
	}

	void fillBounds (float[] bounds) {
		getBoundsInPixels(bounds);
		float[] scaledbounds= Win32DPIUtils.pixelToPoint(device, bounds, zoom);
		System.arraycopy(scaledbounds, 0, bounds, 0, 4);
	}

	private void getBoundsInPixels(float[] bounds) {
		RectF rect = new RectF();
		Gdip.GraphicsPath_GetBounds(handle, rect, 0, 0);
		bounds[0] = rect.X;
		bounds[1] = rect.Y;
		bounds[2] = rect.Width;
		bounds[3] = rect.Height;
	}

	void fillCurrentPoint (float[] point) {
		getCurrentPointInPixels(point);
		float[] scaledpoint= Win32DPIUtils.pixelToPoint(device, point, zoom);
		System.arraycopy(scaledpoint, 0, point, 0, 2);
	}

	private void getCurrentPointInPixels(float[] point) {
		point[0] = currentPoint.X;
		point[1] = currentPoint.Y;
	}

	PathData getPathData() {
		PathData result = getPathDataInPixels();
		result.points = Win32DPIUtils.pixelToPoint(device, result.points, zoom);
		return result;
	}

	private PathData getPathDataInPixels() {
		int count = Gdip.GraphicsPath_GetPointCount(handle);
		byte[] gdipTypes = new byte[count];
		float[] points = new float[count * 2];
		Gdip.GraphicsPath_GetPathTypes(handle, gdipTypes, count);
		Gdip.GraphicsPath_GetPathPoints(handle, points, count);
		byte[] types = new byte[count * 2];
		int index = 0, typesIndex = 0;
		while (index < count) {
			byte type = gdipTypes[index];
			boolean close = false;
			switch (type & Gdip.PathPointTypePathTypeMask) {
				case Gdip.PathPointTypeStart:
					types[typesIndex++] = SWT.PATH_MOVE_TO;
					close = (type & Gdip.PathPointTypeCloseSubpath) != 0;
					index += 1;
					break;
				case Gdip.PathPointTypeLine:
					types[typesIndex++] = SWT.PATH_LINE_TO;
					close = (type & Gdip.PathPointTypeCloseSubpath) != 0;
					index += 1;
					break;
				case Gdip.PathPointTypeBezier:
					types[typesIndex++] = SWT.PATH_CUBIC_TO;
					close = (gdipTypes[index + 2] & Gdip.PathPointTypeCloseSubpath) != 0;
					index += 3;
					break;
				default:
					index++;
			}
			if (close) {
				types[typesIndex++] = SWT.PATH_CLOSE;
			}
		}
		if (typesIndex != types.length) {
			byte[] newTypes = new byte[typesIndex];
			System.arraycopy(types, 0, newTypes, 0, typesIndex);
			types = newTypes;
		}
		PathData result = new PathData();
		result.types = types;
		result.points = points;
		return result;
	}

	@Override
	public String toString() {
		return "PathHandle [handle=" + handle + ", zoom=" + zoom + "]";
	}
}

private class AddArcOperation implements Operation {
	private final float x;
	private final float y;
	private final float width;
	private final float height;
	private final float startAngle;
	private final float arcAngle;

	public AddArcOperation(float x, float y, float width, float height, float startAngle, float arcAngle) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.startAngle = startAngle;
		this.arcAngle = arcAngle;
	}

	@Override
	public void apply(PathHandle pathHandle) {
		if (width == 0 || height == 0 || arcAngle == 0) return;
		int zoom = pathHandle.zoom;
		Drawable drawable = getDevice();
		float xInPixels = Win32DPIUtils.pointToPixel(drawable, x, zoom);
		float yInPixels = Win32DPIUtils.pointToPixel(drawable, y, zoom);
		float widthInPixels = Win32DPIUtils.pointToPixel(drawable, width, zoom);
		float heightInPixels = Win32DPIUtils.pointToPixel(drawable, height, zoom);
		addArcInPixels(pathHandle, xInPixels, yInPixels, widthInPixels, heightInPixels, startAngle, arcAngle);
	}

	private void addArcInPixels(PathHandle pathHandle, float x, float y, float width, float height, float startAngle, float arcAngle) {
		PointF currentPoint = pathHandle.currentPoint;
		long handle = pathHandle.handle;
		if (width < 0) {
			x = x + width;
			width = -width;
		}
		if (height < 0) {
			y = y + height;
			height = -height;
		}
		if (width == height) {
			Gdip.GraphicsPath_AddArc(handle, x, y, width, height, -startAngle, -arcAngle);
		} else {
			long path = Gdip.GraphicsPath_new(Gdip.FillModeAlternate);
			if (path == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			long matrix = Gdip.Matrix_new(width, 0, 0, height, x, y);
			if (matrix == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			Gdip.GraphicsPath_AddArc(path, 0, 0, 1, 1, -startAngle, -arcAngle);
			Gdip.GraphicsPath_Transform(path, matrix);
			Gdip.GraphicsPath_AddPath(handle, path, true);
			Gdip.Matrix_delete(matrix);
			Gdip.GraphicsPath_delete(path);
		}
		Gdip.GraphicsPath_GetLastPoint(handle, currentPoint);
	}
}

private class AddRectangleOperation implements Operation {
	private final float x;
	private final float y;
	private final float width;
	private final float height;

	public AddRectangleOperation(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void apply(PathHandle pathHandle) {
		int zoom = pathHandle.zoom;
		Drawable drawable = getDevice();
		float xInPixels = Win32DPIUtils.pointToPixel(drawable, x, zoom);
		float yInPixels = Win32DPIUtils.pointToPixel(drawable, y, zoom);
		float widthInPixels = Win32DPIUtils.pointToPixel(drawable, width, zoom);
		float heightInPixels = Win32DPIUtils.pointToPixel(drawable, height, zoom);
		addRectangleInPixels(pathHandle, xInPixels, yInPixels, widthInPixels, heightInPixels);
	}

	private void addRectangleInPixels(PathHandle pathHandle, float x, float y, float width, float height) {
		PointF currentPoint = pathHandle.currentPoint;
		long handle = pathHandle.handle;
		RectF rect = new RectF();
		rect.X = x;
		rect.Y = y;
		rect.Width = width;
		rect.Height = height;
		Gdip.GraphicsPath_AddRectangle(handle, rect);
		currentPoint.X = x;
		currentPoint.Y = y;
	}
}

private class AddPathOperation implements Operation {
	private final List<Operation> pathOperations;

	public AddPathOperation(Path path) {
		this.pathOperations = path.operations;
	}

	@Override
	public void apply(PathHandle pathHandle) {
		applyOnTemporaryHandle(getDevice(), pathHandle.zoom, pathOperations, temporaryHandle -> {
			Gdip.GraphicsPath_AddPath(pathHandle.handle, temporaryHandle.handle, false);
			pathHandle.currentPoint.X = temporaryHandle.currentPoint.X;
			pathHandle.currentPoint.Y = temporaryHandle.currentPoint.Y;
			return true;
		});
	}
}

private class AddStringOperation implements Operation {
	private final String string;
	private final float x;
	private final float y;
	private final FontData fontData;

	public AddStringOperation(String string, float x, float y, Font font) {
		this.string = string;
		this.x = x;
		this.y = y;
		this.fontData = font.getFontData()[0];
	}

	@Override
	public void apply(PathHandle pathHandle) {
		int zoom = pathHandle.zoom;
		Drawable drawable = getDevice();
		float xInPixels = Win32DPIUtils.pointToPixel(drawable, x, zoom);
		float yInPixels = Win32DPIUtils.pointToPixel(drawable, y, zoom);
		addStringInPixels(pathHandle, xInPixels, yInPixels);
	}

	private void addStringInPixels(PathHandle pathHandle, float x, float y) {
		int zoom = pathHandle.zoom;
		PointF currentPoint = pathHandle.currentPoint;
		long handle = pathHandle.handle;
		char[] buffer = string.toCharArray();
		long hDC = device.internal_new_GC(null);
		long [] family = new long [1];
		long gdipFont = GC.createGdipFont(hDC, SWTFontProvider.getFontHandle(device, this.fontData, zoom), 0, device.fontCollection, family, null);
		PointF point = new PointF();
		point.X = x - (Gdip.Font_GetSize(gdipFont) / 6);
		point.Y = y;
		int style = Gdip.Font_GetStyle(gdipFont);
		float size = Gdip.Font_GetSize(gdipFont);
		Gdip.GraphicsPath_AddString(handle, buffer, buffer.length, family[0], style, size, point, 0);
		Gdip.GraphicsPath_GetLastPoint(handle, currentPoint);
		Gdip.FontFamily_delete(family[0]);
		Gdip.Font_delete(gdipFont);
		device.internal_dispose_GC(hDC, null);
	}
}

private record CloseOperation() implements Operation {
	@Override
	public void apply(PathHandle pathHandle) {
		PointF startPoint = pathHandle.startPoint;
		PointF currentPoint = pathHandle.currentPoint;
		long handle = pathHandle.handle;
		Gdip.GraphicsPath_CloseFigure(handle);
		/*
		* Feature in GDI+. CloseFigure() does affect the last
		* point, so GetLastPoint() does not return the starting
		* point of the subpath after calling CloseFigure().  The
		* fix is to remember the subpath starting point and use
		* it instead.
		*/
		currentPoint.X = startPoint.X;
		currentPoint.Y = startPoint.Y;
	}
}

private record FlattenOperation(float flatness) implements Operation {
	@Override
	public void apply(PathHandle pathHandle) {
		long handle = pathHandle.handle;
		Gdip.GraphicsPath_Flatten(handle, 0, flatness);
	}
}

private class CubicToOperation implements Operation {
	private final float cx1;
	private final float cy1;
	private final float cx2;
	private final float cy2;
	private final float x;
	private final float y;

	public CubicToOperation(float cx1, float cy1, float cx2, float cy2, float x, float y) {
		this.cx1 = cx1;
		this.cy1 = cy1;
		this.cx2 = cx2;
		this.cy2 = cy2;
		this.x = x;
		this.y = y;
	}

	@Override
	public void apply(PathHandle pathHandle) {
		int zoom = pathHandle.zoom;
		Drawable drawable = getDevice();
		float cx1InPixels = Win32DPIUtils.pointToPixel(drawable, cx1, zoom);
		float cy1InPixels = Win32DPIUtils.pointToPixel(drawable, cy1, zoom);
		float cx2InPixels = Win32DPIUtils.pointToPixel(drawable, cx2, zoom);
		float cy2InPixels = Win32DPIUtils.pointToPixel(drawable, cy2, zoom);
		float xInPixels = Win32DPIUtils.pointToPixel(drawable, x, zoom);
		float yInPixels = Win32DPIUtils.pointToPixel(drawable, y, zoom);
		cubicToInPixels(pathHandle, cx1InPixels, cy1InPixels, cx2InPixels, cy2InPixels, xInPixels, yInPixels);
	}

	private void cubicToInPixels(PathHandle pathHandle, float cx1, float cy1, float cx2, float cy2, float x, float y) {
		PointF currentPoint = pathHandle.currentPoint;
		long handle = pathHandle.handle;
		Gdip.GraphicsPath_AddBezier(handle, currentPoint.X, currentPoint.Y, cx1, cy1, cx2, cy2, x, y);
		Gdip.GraphicsPath_GetLastPoint(handle, currentPoint);
	}
}

private class LineToOperation implements Operation {
	private final float x;
	private final float y;

	public LineToOperation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void apply(PathHandle pathHandle) {
		int zoom = pathHandle.zoom;
		Drawable drawable = getDevice();
		lineToInPixels(pathHandle, Win32DPIUtils.pointToPixel(drawable, x, zoom), Win32DPIUtils.pointToPixel(drawable, y, zoom));
	}

	private void lineToInPixels(PathHandle pathHandle, float x, float y) {
		PointF currentPoint = pathHandle.currentPoint;
		long handle = pathHandle.handle;
		Gdip.GraphicsPath_AddLine(handle, currentPoint.X, currentPoint.Y, x, y);
		Gdip.GraphicsPath_GetLastPoint(handle, currentPoint);
	}
}

private class MoveToOperation implements Operation {
	private final float x;
	private final float y;

	public MoveToOperation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void apply(PathHandle pathHandle) {
		int zoom = pathHandle.zoom;
		Drawable drawable = getDevice();
		moveToInPixels(pathHandle, Win32DPIUtils.pointToPixel(drawable, x, zoom), Win32DPIUtils.pointToPixel(drawable, y, zoom));
	}

	void moveToInPixels(PathHandle pathHandle, float x, float y) {
		PointF startPoint = pathHandle.startPoint;
		PointF currentPoint = pathHandle.currentPoint;
		long handle = pathHandle.handle;
		Gdip.GraphicsPath_StartFigure(handle);
		currentPoint.X = startPoint.X = x;
		currentPoint.Y = startPoint.Y = y;
	}
}

private class QuadToOperation implements Operation {
	private final float cx;
	private final float cy;
	private final float x;
	private final float y;

	public QuadToOperation(float cx, float cy, float x, float y) {
		this.cx = cx;
		this.cy = cy;
		this.x = x;
		this.y = y;
	}

	@Override
	public void apply(PathHandle pathHandle) {
		Drawable drawable = getDevice();
		int zoom = pathHandle.zoom;
		float cxInPixels = Win32DPIUtils.pointToPixel(drawable, cx, zoom);
		float cyInPixels = Win32DPIUtils.pointToPixel(drawable, cy, zoom);
		float xInPixels = Win32DPIUtils.pointToPixel(drawable, x, zoom);
		float yInPixels = Win32DPIUtils.pointToPixel(drawable, y, zoom);
		quadToInPixels(pathHandle, cxInPixels, cyInPixels, xInPixels, yInPixels);
	}

	private void quadToInPixels(PathHandle pathHandle, float cx, float cy, float x, float y) {
		PointF currentPoint = pathHandle.currentPoint;
		long handle = pathHandle.handle;
		float cx1 = currentPoint.X + 2 * (cx - currentPoint.X) / 3;
		float cy1 = currentPoint.Y + 2 * (cy - currentPoint.Y) / 3;
		float cx2 = cx1 + (x - currentPoint.X) / 3;
		float cy2 = cy1 + (y - currentPoint.Y) / 3;
		Gdip.GraphicsPath_AddBezier(handle, currentPoint.X, currentPoint.Y, cx1, cy1, cx2, cy2, x, y);
		Gdip.GraphicsPath_GetLastPoint(handle, currentPoint);
	}
}

private interface Operation {
	void apply(PathHandle pathHandle);
}

private void storeAndApplyOperationOnAllHandles(Operation operation) {
	operations.add(operation);
	zoomToHandle.values().forEach(operation::apply);
}

private <T> T applyUsingAnyHandle(Function<PathHandle, T> function) {
	if (zoomToHandle.isEmpty()) {
		return applyOnTemporaryHandle(getDevice(), DPIUtil.getDeviceZoom(), this.operations, function);
	} else {
		return function.apply(zoomToHandle.values().iterator().next());
	}
}

private static <T> T applyOnTemporaryHandle(Device device, int zoom, List<Operation> operations, Function<PathHandle, T> function) {
	PathHandle temporaryHandle = newEmptyPathHandle(device, zoom);
	try {
		for (Operation operation : operations) {
			operation.apply(temporaryHandle);
		}
		return function.apply(temporaryHandle);
	} finally {
		temporaryHandle.destroy();
	}
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString() {
	if (isDisposed()) return "Path {*DISPOSED*}";
	return "Path " + zoomToHandle;
}

private static PathHandle newEmptyPathHandle(Device device, int zoom) {
	long newHandle = Gdip.GraphicsPath_new(Gdip.FillModeAlternate);
	if (newHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	PathHandle newPathHandle = new PathHandle(device, newHandle, zoom);
	return newPathHandle;
}

private PathHandle newPathHandle(int zoom) {
	PathHandle newPathHandle = newEmptyPathHandle(getDevice(), zoom);
	for (Operation operation : operations) {
		operation.apply(newPathHandle);
	}
	return newPathHandle;
}

private PathHandle getPathHandle(int zoom) {
	if (!zoomToHandle.containsKey(zoom)) {
		PathHandle newHandle = newPathHandle(zoom);
		zoomToHandle.put(zoom, newHandle);
		return newHandle;
	}
	return zoomToHandle.get(zoom);
}

long getHandle(int zoom) {
	return getPathHandle(zoom).handle;
}

}

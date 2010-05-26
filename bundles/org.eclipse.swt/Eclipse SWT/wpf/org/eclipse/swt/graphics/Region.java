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


import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.*;

/**
 * Instances of this class represent areas of an x-y coordinate
 * system that are aggregates of the areas covered by a number
 * of polygons.
 * <p>
 * Application code must explicitly invoke the <code>Region.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: GraphicsExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */

public final class Region extends Resource {
	
	/**
	 * the OS resource for the region
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

/**
 * Constructs a new empty region.
 * 
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for region creation</li>
 * </ul>
 */
public Region () {
	this(null);
}

/**
 * Constructs a new empty region.
 * <p>
 * You must dispose the region when it is no longer required. 
 * </p>
 *
 * @param device the device on which to allocate the region
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for region creation</li>
 * </ul>
 *
 * @see #dispose
 * 
 * @since 3.0
 */
public Region (Device device) {
	super(device);
	handle = OS.gcnew_GeometryGroup();
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	init();
}

/**
 * Constructs a new region given a handle to the operating
 * system resources that it should represent.
 * 
 * @param handle the handle for the result
 */
Region(Device device, int handle) {
	super(device);
	this.handle = handle;
}

/**
 * Adds the given polygon to the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param pointArray points that describe the polygon to merge with the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.0
*
 */
public void add (int[] pointArray) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	combine(pointArray, OS.GeometryCombineMode_Union);
}



/**
 * Adds the given rectangle to the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param rect the rectangle to merge with the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void add (Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	add (rect.x, rect.y, rect.width, rect.height);
}

/**
 * Adds the given rectangle to the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param x the x coordinate of the rectangle
 * @param y the y coordinate of the rectangle
 * @param width the width coordinate of the rectangle
 * @param height the height coordinate of the rectangle
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void add (int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0 || height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int rect = OS.gcnew_Rect(x, y, width, height);
	int geometry1 = OS.gcnew_RectangleGeometry(rect);
	int geometries = OS.GeometryGroup_Children(handle);
	if (OS.GeometryCollection_Count(geometries) == 0) {
		OS.GeometryCollection_Add(geometries, geometry1);
	} else {
		int geometry2 = OS.GeometryGroup_Children(handle, 0);
		int geometry3 = OS.gcnew_CombinedGeometry(OS.GeometryCombineMode_Union, geometry1, geometry2);
		OS.GeometryCollection_Remove(geometries, geometry2);
		OS.GeometryCollection_Add(geometries, geometry3);
		OS.GCHandle_Free(geometry2);
		OS.GCHandle_Free(geometry3);
	}
	OS.GCHandle_Free(rect);
	OS.GCHandle_Free(geometry1);
	OS.GCHandle_Free(geometries);
}

/**
 * Adds all of the polygons which make up the area covered
 * by the argument to the collection of polygons the receiver
 * maintains to describe its area.
 *
 * @param region the region to merge
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void add (Region region) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int geometries = OS.GeometryGroup_Children(handle);
	if (OS.GeometryCollection_Count(geometries) == 0) {
		OS.GeometryCollection_Add(geometries, region.handle);
	} else {
		int geometry2 = OS.GeometryGroup_Children(handle, 0);
		int geometry3 = OS.gcnew_CombinedGeometry(OS.GeometryCombineMode_Union, region.handle, geometry2);
		OS.GeometryCollection_Remove(geometries, geometry2);
		OS.GeometryCollection_Add(geometries, geometry3);
		OS.GCHandle_Free(geometry2);
		OS.GCHandle_Free(geometry3);
	}
	OS.GCHandle_Free(geometries);
}


void combine (int[] pointArray, int mode) {
	if (pointArray.length < 4) return;
	int list = OS.gcnew_PointCollection(pointArray.length / 2);
	for (int i = 2; i < pointArray.length; i += 2) {
		int point = OS.gcnew_Point(pointArray[i], pointArray[i + 1]);
		OS.PointCollection_Add(list, point);
		OS.GCHandle_Free(point);
	}
	int poly = OS.gcnew_PolyLineSegment(list, true);
	OS.GCHandle_Free(list);
	int figure = OS.gcnew_PathFigure();
	int startPoint = OS.gcnew_Point(pointArray[0], pointArray[1]);
	OS.PathFigure_StartPoint(figure, startPoint);
	OS.PathFigure_IsClosed(figure, true);
	int segments = OS.PathFigure_Segments(figure);
	OS.PathSegmentCollection_Add(segments, poly);
	int path = OS.gcnew_PathGeometry();
	int figures = OS.PathGeometry_Figures(path);
	OS.PathFigureCollection_Add(figures, figure);
	int geometries = OS.GeometryGroup_Children(handle);
	if (OS.GeometryCollection_Count(geometries) == 0) {
		if (mode == OS.GeometryCombineMode_Union) OS.GeometryCollection_Add(geometries, path);
	} else {
		int geometry2 = OS.GeometryGroup_Children(handle, 0);
		int geometry3 = OS.gcnew_CombinedGeometry(mode, geometry2, path);
		OS.GeometryCollection_Remove(geometries, geometry2);
		OS.GeometryCollection_Add(geometries, geometry3);
		OS.GCHandle_Free(geometry2);
		OS.GCHandle_Free(geometry3);
	}
	OS.GCHandle_Free(geometries);
	OS.GCHandle_Free(figures);
	OS.GCHandle_Free(path);
	OS.GCHandle_Free(segments);
	OS.GCHandle_Free(figure);
	OS.GCHandle_Free(startPoint);
	OS.GCHandle_Free(poly);
}

/**
 * Returns <code>true</code> if the point specified by the
 * arguments is inside the area specified by the receiver,
 * and <code>false</code> otherwise.
 *
 * @param x the x coordinate of the point to test for containment
 * @param y the y coordinate of the point to test for containment
 * @return <code>true</code> if the region contains the point and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean contains (int x, int y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int point = OS.gcnew_Point(x, y);
	boolean result = OS.Geometry_FillContains(handle, point);
	OS.GCHandle_Free(point);
	return result;
}

/**
 * Returns <code>true</code> if the given point is inside the
 * area specified by the receiver, and <code>false</code>
 * otherwise.
 *
 * @param pt the point to test for containment
 * @return <code>true</code> if the region contains the point and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean contains (Point pt) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pt == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return contains(pt.x, pt.y);
}

void destroy() {
	OS.GCHandle_Free(handle);
	handle = 0;
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode
 */
public boolean equals (Object object) {
	if (this == object) return true;
	if (!(object instanceof Region)) return false;
	Region rgn = (Region)object;
	return handle == rgn.handle;
}

/**
 * Returns a rectangle which represents the rectangular
 * union of the collection of polygons the receiver
 * maintains to describe its area.
 *
 * @return a bounding rectangle for the region
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Rectangle#union
 */
public Rectangle getBounds() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (OS.Geometry_IsEmpty(handle)) return new Rectangle(0, 0, 0, 0);
	int rect = OS.Geometry_Bounds(handle);
	Rectangle result = new Rectangle((int)OS.Rect_X(rect), (int)OS.Rect_Y(rect), (int)OS.Rect_Width(rect), (int)OS.Rect_Height(rect));
	OS.GCHandle_Free(rect);
	return result;
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects that return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals
 */
public int hashCode () {
	return handle;
}

/**
 * Intersects the given rectangle to the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param rect the rectangle to intersect with the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void intersect (Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	intersect (rect.x, rect.y, rect.width, rect.height);
}

/**
 * Intersects the given rectangle to the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param x the x coordinate of the rectangle
 * @param y the y coordinate of the rectangle
 * @param width the width coordinate of the rectangle
 * @param height the height coordinate of the rectangle
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void intersect (int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0 || height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int rect = OS.gcnew_Rect(x, y, width, height);
	int geometry1 = OS.gcnew_RectangleGeometry(rect);
	int geometries = OS.GeometryGroup_Children(handle);
	if (OS.GeometryCollection_Count(geometries) != 0) {
		int geometry2 = OS.GeometryGroup_Children(handle, 0);
		int geometry3 = OS.gcnew_CombinedGeometry(OS.GeometryCombineMode_Intersect, geometry1, geometry2);
		OS.GeometryCollection_Remove(geometries, geometry2);
		OS.GeometryCollection_Add(geometries, geometry3);
		OS.GCHandle_Free(geometry2);
		OS.GCHandle_Free(geometry3);
	}
	OS.GCHandle_Free(rect);
	OS.GCHandle_Free(geometry1);
	OS.GCHandle_Free(geometries);
}

/**
 * Intersects all of the polygons which make up the area covered
 * by the argument to the collection of polygons the receiver
 * maintains to describe its area.
 *
 * @param region the region to intersect
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void intersect (Region region) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int geometries = OS.GeometryGroup_Children(handle);
	if (OS.GeometryCollection_Count(geometries) != 0) {
		int geometry2 = OS.GeometryGroup_Children(handle, 0);
		int geometry3 = OS.gcnew_CombinedGeometry(OS.GeometryCombineMode_Intersect, region.handle, geometry2);
		OS.GeometryCollection_Remove(geometries, geometry2);
		OS.GeometryCollection_Add(geometries, geometry3);
		OS.GCHandle_Free(geometry2);
		OS.GCHandle_Free(geometry3);
	}
	OS.GCHandle_Free(geometries);
}

/**
 * Returns <code>true</code> if the rectangle described by the
 * arguments intersects with any of the polygons the receiver
 * maintains to describe its area, and <code>false</code> otherwise.
 *
 * @param x the x coordinate of the origin of the rectangle
 * @param y the y coordinate of the origin of the rectangle
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 * @return <code>true</code> if the rectangle intersects with the receiver, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Rectangle#intersects(Rectangle)
 */
public boolean intersects (int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int rect = OS.gcnew_Rect(x, y, width, height);
	int geometry = OS.gcnew_RectangleGeometry(rect);
	int result = OS.Geometry_FillContainsWithDetail(handle, geometry);
	OS.GCHandle_Free(geometry);
	OS.GCHandle_Free(rect);
	return result != OS.IntersectionDetail_Empty;
}

/**
 * Returns <code>true</code> if the given rectangle intersects
 * with any of the polygons the receiver maintains to describe
 * its area and <code>false</code> otherwise.
 *
 * @param rect the rectangle to test for intersection
 * @return <code>true</code> if the rectangle intersects with the receiver, and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Rectangle#intersects(Rectangle)
 */
public boolean intersects (Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return intersects(rect.x, rect.y, rect.width, rect.height);
}

/**
 * Returns <code>true</code> if the region has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the region.
 * When a region has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the region.
 *
 * @return <code>true</code> when the region is disposed, and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == 0;
}

/**
 * Returns <code>true</code> if the receiver does not cover any
 * area in the (x, y) coordinate plane, and <code>false</code> if
 * the receiver does cover some area in the plane.
 *
 * @return <code>true</code> if the receiver is empty, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean isEmpty () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return OS.Geometry_IsEmpty(handle);
}

/**
 * Subtracts the given polygon from the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param pointArray points that describe the polygon to merge with the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void subtract (int[] pointArray) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	combine(pointArray, OS.GeometryCombineMode_Exclude);
}

/**
 * Subtracts the given rectangle from the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param rect the rectangle to subtract from the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void subtract (Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	subtract (rect.x, rect.y, rect.width, rect.height);
}

/**
 * Subtracts the given rectangle from the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param x the x coordinate of the rectangle
 * @param y the y coordinate of the rectangle
 * @param width the width coordinate of the rectangle
 * @param height the height coordinate of the rectangle
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void subtract (int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0 || height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);	
	int rect = OS.gcnew_Rect(x, y, width, height);
	int geometry1 = OS.gcnew_RectangleGeometry(rect);
	int geometries = OS.GeometryGroup_Children(handle);
	if (OS.GeometryCollection_Count(geometries) != 0) {
		int geometry2 = OS.GeometryGroup_Children(handle, 0);
		int geometry3 = OS.gcnew_CombinedGeometry(OS.GeometryCombineMode_Exclude, geometry2, geometry1);
		OS.GeometryCollection_Remove(geometries, geometry2);
		OS.GeometryCollection_Add(geometries, geometry3);
		OS.GCHandle_Free(geometry2);
		OS.GCHandle_Free(geometry3);
	}
	OS.GCHandle_Free(rect);
	OS.GCHandle_Free(geometry1);
	OS.GCHandle_Free(geometries);
}

/**
 * Subtracts all of the polygons which make up the area covered
 * by the argument from the collection of polygons the receiver
 * maintains to describe its area.
 *
 * @param region the region to subtract
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void subtract (Region region) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int geometries = OS.GeometryGroup_Children(handle);
	if (OS.GeometryCollection_Count(geometries) != 0) {
		int geometry2 = OS.GeometryGroup_Children(handle, 0);
		int geometry3 = OS.gcnew_CombinedGeometry(OS.GeometryCombineMode_Exclude, geometry2, region.handle);
		OS.GeometryCollection_Remove(geometries, geometry2);
		OS.GeometryCollection_Add(geometries, geometry3);
		OS.GCHandle_Free(geometry2);
		OS.GCHandle_Free(geometry3);
	}
	OS.GCHandle_Free(geometries);
}

/**
 * Translate all of the polygons the receiver maintains to describe
 * its area by the specified point.
 *
 * @param x the x coordinate of the point to translate
 * @param y the y coordinate of the point to translate
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void translate (int x, int y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int transform1 = OS.Geometry_Transform(handle);
	int transform2 = OS.gcnew_TranslateTransform(x, y);
	int transform = OS.gcnew_TransformGroup();
	int transforms = OS.TransformGroup_Children(transform);
	OS.TransformCollection_Add(transforms, transform1);
	OS.TransformCollection_Add(transforms, transform2);
	OS.Geometry_Transform(handle, transform);
	OS.GCHandle_Free(transform1);
	OS.GCHandle_Free(transform2);
	OS.GCHandle_Free(transform);
	OS.GCHandle_Free(transforms);
}

/**
 * Translate all of the polygons the receiver maintains to describe
 * its area by the specified point.
 *
 * @param pt the point to translate
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void translate (Point pt) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pt == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	translate (pt.x, pt.y);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Region {*DISPOSED*}";
	return "Region {" + handle + "}";
}

/**	 
 * Invokes platform specific functionality to allocate a new region.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Region</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param device the device on which to allocate the region
 * @param handle the handle for the region
 * @return a new region object containing the specified device and handle
 * 
 * @noreference This method is not intended to be referenced by clients.
 */
public static Region wpf_new(Device device, int handle) {
	return new Region(device, handle);
}

}

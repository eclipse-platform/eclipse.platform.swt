/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

 
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
 
/**
 * Instances of this class represent areas of an x-y coordinate
 * system that are aggregates of the areas covered by a number
 * of rectangles.
 * <p>
 * Application code must explicitly invoke the <code>Region.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 */
public final class Region {
	/**
	 * the OS resource for the region
	 * (Warning: This field is platform dependent)
	 */
	public int handle;

	/**
	 * The device where this image was created.
	 */
	Device device;

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
public Region (Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	handle = OS.XCreateRegion ();
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}
Region (Device device, int handle) {
	this.device = device;
	this.handle = handle;
}
/**
 * Adds the given polygon to the collection of rectangles
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
	short[] points = new short[pointArray.length];
	for (int i = 0; i < pointArray.length; i++) {
		points[i] = (short)pointArray[i];
	}
	int polyRgn = OS.XPolygonRegion(points, points.length / 2, OS.EvenOddRule);
	OS.XUnionRegion(handle, polyRgn, handle);
	OS.XDestroyRegion(polyRgn);
}
/**
 * Adds the given rectangle to the collection of rectangles
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
	if (rect.width < 0 || rect.height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

	XRectangle xRect = new XRectangle();
	xRect.x = (short)rect.x;
	xRect.y = (short)rect.y;
	xRect.width = (short)rect.width;
	xRect.height = (short)rect.height;
	OS.XUnionRectWithRegion(xRect, handle, handle);
}
/**
 * Adds all of the rectangles which make up the area covered
 * by the argument to the collection of rectangles the receiver
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
	OS.XUnionRegion(handle, region.handle, handle);
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
	return OS.XPointInRegion(handle, x, y);
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
	if (pt == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return contains(pt.x, pt.y);
}
/**
 * Disposes of the operating system resources associated with
 * the region. Applications must dispose of all regions which
 * they allocate.
 */
public void dispose () {
	if (handle == 0) return;
	if (device.isDisposed()) return;
	OS.XDestroyRegion(handle);
	handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
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
	Region region = (Region)object;
	return handle == region.handle;
}
/**
 * Returns a rectangle which represents the rectangular
 * union of the collection of rectangles the receiver
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
	XRectangle rect = new XRectangle();
	OS.XClipBox(handle, rect);
	return new Rectangle(rect.x, rect.y, rect.width, rect.height);
}
/**
 * Returns an integer hash code for the receiver. Any two 
 * objects which return <code>true</code> when passed to 
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
public void intersect (Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (rect.width < 0 || rect.height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

	int rectRgn = OS.XCreateRegion();
	XRectangle xRect = new XRectangle();
	xRect.x = (short)rect.x;
	xRect.y = (short)rect.y;
	xRect.width = (short)rect.width;
	xRect.height = (short)rect.height;
	OS.XUnionRectWithRegion(xRect, rectRgn, rectRgn);
	OS.XIntersectRegion(handle, rectRgn, handle);
	OS.XDestroyRegion(rectRgn);
}
public void intersect (Region region) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.XIntersectRegion(handle, region.handle, handle);
}
/**
 * Returns <code>true</code> if the rectangle described by the
 * arguments intersects with any of the rectangles the receiver
 * mainains to describe its area, and <code>false</code> otherwise.
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
 * @see Rectangle#intersects
 */
public boolean intersects (int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return OS.XRectInRegion (handle, x, y, width, height) != OS.RectangleOut;
}
/**
 * Returns <code>true</code> if the given rectangle intersects
 * with any of the rectangles the receiver mainains to describe
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
 * @see Rectangle#intersects
 */
public boolean intersects (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return intersects(rect.x, rect.y, rect.width, rect.height);
}
/**
 * Returns <code>true</code> if the region has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the region.
 * When a region has been disposed, it is an error to
 * invoke any other method using the region.
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
	return OS.XEmptyRegion(handle);
}
public static Region motif_new(Device device, int handle) {
	return new Region(device, handle);
}
/**
 * Subtracts the given polygon from the collection of rectangles
 * the receiver maintains to describe its area.
 *
 * param pointArray points that describe the polygon to merge with the receiver
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
	short[] points = new short[pointArray.length];
	for (int i = 0; i < pointArray.length; i++) {
		points[i] = (short)pointArray[i];
	}
	int polyRgn = OS.XPolygonRegion(points, points.length / 2, OS.EvenOddRule);
	OS.XSubtractRegion(handle, polyRgn, handle);
	OS.XDestroyRegion(polyRgn);
}
public void subtract (Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (rect.width < 0 || rect.height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

	int rectRgn = OS.XCreateRegion();
	XRectangle xRect = new XRectangle();
	xRect.x = (short)rect.x;
	xRect.y = (short)rect.y;
	xRect.width = (short)rect.width;
	xRect.height = (short)rect.height;
	OS.XUnionRectWithRegion(xRect, rectRgn, rectRgn);
	OS.XSubtractRegion(handle, rectRgn, handle);
	OS.XDestroyRegion(rectRgn);
}
public void subtract (Region region) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.XSubtractRegion(handle, region.handle, handle);
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
}

package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.gtk.*;
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
 * Constructs a new empty region.
 */
public Region() {
	handle = OS.gdk_region_new();
}
Region(int handle) {
	this.handle = handle;
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
public void add(Rectangle rect) {
	if (rect == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (rect.width < 0 || rect.height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	GdkRectangle gdkRect = new GdkRectangle();
	gdkRect.x = (short)rect.x;
	gdkRect.y = (short)rect.y;
	gdkRect.width = (short)rect.width;
	gdkRect.height = (short)rect.height;
	int hOld = handle;
	/**
	 * Feature in GDK. Due to the way the GDK region calls work,
	 * we have to reassign the handle and destroy the old one.
	 */
	handle = OS.gdk_region_union_with_rect(handle, gdkRect);
	OS.gdk_region_destroy(hOld);
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
public void add(Region region) {
	if (region == null) error(SWT.ERROR_NULL_ARGUMENT);
	/**
	 * Feature in GDK. Due to the way the GDK region calls work,
	 * we have to reassign the handle and destroy the old one.
	 */
	int hOld = handle;
	handle = OS.gdk_regions_union(handle, region.handle);
	OS.gdk_region_destroy(hOld);
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
public boolean contains(int x, int y) {
	return OS.gdk_region_point_in(handle, x, y);
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
public boolean contains(Point pt) {
	if (pt == null) error(SWT.ERROR_NULL_ARGUMENT);
	return contains(pt.x, pt.y);
}
/**
 * Disposes of the operating system resources associated with
 * the region. Applications must dispose of all regions which
 * they allocate.
 */
public void dispose() {
	if (handle != 0) OS.gdk_region_destroy(handle);
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
public boolean equals(Object object) {
	if (this == object) return true;
	if (!(object instanceof Region)) return false;
	int xRegion = ((Region)object).handle;
	if (handle == xRegion) return true;
	if (xRegion == 0) return false;
	return OS.gdk_region_equal(handle, xRegion);
}
void error(int code) {
	throw new SWTError(code);
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
	GdkRectangle rect = new GdkRectangle();
	OS.gdk_region_get_clipbox(handle, rect);
	return new Rectangle(rect.x, rect.y, rect.width, rect.height);
}
public static Region gtk_new(int handle) {
	return new Region(handle);
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
public int hashCode() {
	return handle;
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
	GdkRectangle osRect = new GdkRectangle();
	osRect.x = (short)x;
	osRect.y = (short)y;
	osRect.width = (short)width;
	osRect.height = (short)height;
	return OS.gdk_region_rect_in(handle, osRect) != OS.GDK_OVERLAP_RECTANGLE_OUT;
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
public boolean intersects(Rectangle rect) {
	if (rect == null) error(SWT.ERROR_NULL_ARGUMENT);
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
public boolean isEmpty() {
	return OS.gdk_region_empty(handle);
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

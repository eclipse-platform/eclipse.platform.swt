/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;

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
	public long handle;

/**
 * Constructs a new empty region.
 * <p>
 * You must dispose the region when it is no longer required.
 * </p>
 *
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for region creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Region() {
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
 * @see #dispose()
 *
 * @since 3.0
 */
public Region(Device device) {
	super(device);
	handle = Cairo.cairo_region_create();
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	init();
}

Region(Device device, long handle) {
	super(device);
	this.handle = handle;
}

static long gdk_region_polygon(int[] pointArray, int npoints, int fill_rule) {
	//TODO this does not perform well and could fail if the polygon is too big
	int minX = pointArray[0], maxX = minX;
	int minY = pointArray[1], maxY = minY;
	int count = npoints * 2;
	for (int i=2; i<count; i+=2) {
		int x = pointArray[i], y = pointArray[i + 1];
		if (x < minX) minX = x;
		if (x > maxX) maxX = x;
		if (y < minY) minY = y;
		if (y > maxY) maxY = y;
	}
	long surface = Cairo.cairo_image_surface_create(Cairo.CAIRO_FORMAT_ARGB32, maxX - minX, maxY - minY);
	if (surface == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	long cairo = Cairo.cairo_create(surface);
	if (cairo == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	Cairo.cairo_move_to(cairo, pointArray[0] - minX, pointArray[1] - minY);
	for (int i=2; i<count; i+=2) {
		Cairo.cairo_line_to(cairo, pointArray[i]- minX, pointArray[i+1] - minY);
	}
	Cairo.cairo_close_path(cairo);
	Cairo.cairo_set_source_rgb(cairo, 1, 1, 1);
	int cairo_rule = Cairo.CAIRO_FILL_RULE_WINDING;
	if (fill_rule == GDK.GDK_EVEN_ODD_RULE) {
		cairo_rule = Cairo.CAIRO_FILL_RULE_EVEN_ODD;
	}
	Cairo.cairo_set_fill_rule(cairo, cairo_rule);
	Cairo.cairo_fill(cairo);
	Cairo.cairo_destroy(cairo);
	long polyRgn = GDK.gdk_cairo_region_create_from_surface(surface);
	Cairo.cairo_region_translate (polyRgn, minX, minY);
	Cairo.cairo_surface_destroy(surface);
	return polyRgn;
}

static void cairo_region_get_rectangles(long region, long [] rectangles, int[] n_rectangles) {
	int num = Cairo.cairo_region_num_rectangles (region);
	if (n_rectangles != null) n_rectangles[0] = num;
	rectangles[0] = OS.g_malloc(cairo_rectangle_int_t.sizeof * num);
	for (int n = 0; n < num; n++) {
		Cairo.cairo_region_get_rectangle (region, n, rectangles[0] + (n * cairo_rectangle_int_t.sizeof));
	}
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
	addInPixels(DPIUtil.autoScaleUp(pointArray));
}
void addInPixels (int[] pointArray) {
	/*
	* Bug in GTK. If gdk_region_polygon() is called with one point,
	* it segment faults. The fix is to make sure that it is called
	* with enough points for a polygon.
	*/
	if (pointArray.length < 6) return;
	long polyRgn = gdk_region_polygon(pointArray, pointArray.length / 2, GDK.GDK_EVEN_ODD_RULE);
	Cairo.cairo_region_union(handle, polyRgn);
	Cairo.cairo_region_destroy(polyRgn);
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
public void add(Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	addInPixels(DPIUtil.autoScaleUp(rect));
}
void addInPixels(Rectangle rect) {
	addInPixels (rect.x, rect.y, rect.width, rect.height);
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
public void add(int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0 || height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	add(new Rectangle(x, y, width, height));
}
void addInPixels(int x, int y, int width, int height) {
	cairo_rectangle_int_t rect = new cairo_rectangle_int_t();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	Cairo.cairo_region_union_rectangle(handle, rect);
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
public void add(Region region) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Cairo.cairo_region_union(handle, region.handle);
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
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return contains(new Point(x, y));
}
boolean containsInPixels(int x, int y) {
	return Cairo.cairo_region_contains_point(handle, x, y);
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
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return containsInPixels(DPIUtil.autoScaleUp(pt));
}
boolean containsInPixels(Point pt) {
	if (pt == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return containsInPixels(pt.x, pt.y);
}

@Override
void destroy() {
	Cairo.cairo_region_destroy(handle);
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
@Override
public boolean equals(Object object) {
	if (this == object) return true;
	if (!(object instanceof Region)) return false;
	Region region = (Region)object;
	return handle == region.handle;
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
	return DPIUtil.autoScaleDown(getBoundsInPixels());
}
Rectangle getBoundsInPixels() {
	cairo_rectangle_int_t rect = new cairo_rectangle_int_t();
	Cairo.cairo_region_get_extents(handle, rect);
	return new Rectangle(rect.x, rect.y, rect.width, rect.height);
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
public static Region gtk_new(Device device, long handle) {
	return new Region(device, handle);
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
@Override
public int hashCode() {
	return (int)handle;
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
public void intersect(Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	intersectInPixels(DPIUtil.autoScaleUp(rect));
}

void intersectInPixels(Rectangle rect) {
	intersectInPixels (rect.x, rect.y, rect.width, rect.height);
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
public void intersect(int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0 || height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	intersect(new Rectangle(x, y, width, height));
}

void intersectInPixels(int x, int y, int width, int height) {
	cairo_rectangle_int_t rect = new cairo_rectangle_int_t();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	long rectRgn = Cairo.cairo_region_create_rectangle(rect);
	Cairo.cairo_region_intersect(handle, rectRgn);
	Cairo.cairo_region_destroy(rectRgn);
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
public void intersect(Region region) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Cairo.cairo_region_intersect(handle, region.handle);
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
	return intersects(new Rectangle(x, y, width, height));
}

boolean intersectsInPixels (int x, int y, int width, int height) {
	cairo_rectangle_int_t rect = new cairo_rectangle_int_t();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	return Cairo.cairo_region_contains_rectangle(handle, rect) != Cairo.CAIRO_REGION_OVERLAP_OUT;
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
public boolean intersects(Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return intersectsInPixels(DPIUtil.autoScaleUp(rect));
}

boolean intersectsInPixels(Rectangle rect) {
	return intersectsInPixels(rect.x, rect.y, rect.width, rect.height);
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
@Override
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
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return Cairo.cairo_region_is_empty(handle);
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
	subtractInPixels(DPIUtil.autoScaleUp(pointArray));
}

void subtractInPixels (int[] pointArray) {
	/*
	* Bug in GTK. If gdk_region_polygon() is called with one point,
	* it segment faults. The fix is to make sure that it is called
	* with enough points for a polygon.
	*/
	if (pointArray.length < 6) return;
	long polyRgn = gdk_region_polygon(pointArray, pointArray.length / 2, GDK.GDK_EVEN_ODD_RULE);
	Cairo.cairo_region_subtract(handle, polyRgn);
	Cairo.cairo_region_destroy(polyRgn);
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
public void subtract(Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	subtractInPixels(DPIUtil.autoScaleUp(rect));
}

void subtractInPixels(Rectangle rect) {
	subtractInPixels (rect.x, rect.y, rect.width, rect.height);
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
public void subtract(int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0 || height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	subtract(new Rectangle(x, y, width, height));
}

void subtractInPixels(int x, int y, int width, int height) {
	cairo_rectangle_int_t rect = new cairo_rectangle_int_t ();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	long rectRgn = Cairo.cairo_region_create_rectangle(rect);
	Cairo.cairo_region_subtract(handle, rectRgn);
	Cairo.cairo_region_destroy(rectRgn);
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
public void subtract(Region region) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Cairo.cairo_region_subtract(handle, region.handle);
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
	translate(new Point(x, y));
}

void translateInPixels (int x, int y) {
	Cairo.cairo_region_translate (handle, x, y);
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
	pt = DPIUtil.autoScaleUp(pt);
	translateInPixels(pt.x, pt.y);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString () {
	if (isDisposed()) return "Region {*DISPOSED*}";
	return "Region {" + handle + "}";
}
}

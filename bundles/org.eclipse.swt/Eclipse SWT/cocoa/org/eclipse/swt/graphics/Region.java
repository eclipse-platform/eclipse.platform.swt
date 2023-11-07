/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
import org.eclipse.swt.internal.cocoa.*;

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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		handle = OS.NewRgn();
		if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		init();
	} finally {
		if (pool != null) pool.release();
	}
}

Region(Device device, long handle) {
	super(device);
	this.handle = handle;
	/*
	 * When created this way, Font doesn't own its .handle, and
	 * for this reason it can't be disposed. Tell leak detector
	 * to just ignore it.
	 */
	this.ignoreNonDisposed();
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
public static Region cocoa_new(Device device, long handle) {
	return new Region(device, handle);
}

static long polyToRgn(int[] poly, int length) {
	short[] r = new short[4];
	long polyRgn = OS.NewRgn(), rectRgn = OS.NewRgn();
	int minY = poly[1], maxY = poly[1];
	for (int y = 3; y < length; y += 2) {
		if (poly[y] < minY) minY = poly[y];
		if (poly[y] > maxY) maxY = poly[y];
	}
	int[] inter = new int[length + 1];
	for (int y = minY; y <= maxY; y++) {
		int count = 0;
		int x1 = poly[0], y1 = poly[1];
		for (int p = 2; p < length; p += 2) {
			int x2 = poly[p], y2 = poly[p + 1];
			if (y1 != y2 && ((y1 <= y && y < y2) || (y2 <= y && y < y1))) {
				inter[count++] = (int)((((y - y1) / (float)(y2 - y1)) * (x2 - x1)) + x1 + 0.5f);
			}
			x1 = x2;
			y1 = y2;
		}
		int x2 = poly[0], y2 = poly[1];
		if (y1 != y2 && ((y1 <= y && y < y2) || (y2 <= y && y < y1))) {
			inter[count++] = (int)((((y - y1) / (float)(y2 - y1)) * (x2 - x1)) + x1 + 0.5f);
		}
		for (int gap=count/2; gap>0; gap/=2) {
			for (int i=gap; i<count; i++) {
				for (int j=i-gap; j>=0; j-=gap) {
					if ((inter[j] - inter[j + gap]) <= 0)
						break;
					int temp = inter[j];
					inter[j] = inter[j + gap];
					inter[j + gap] = temp;
				}
			}
		}
		for (int i = 0; i < count; i += 2) {
			OS.SetRect(r, (short)inter[i], (short)y, (short)(inter[i + 1]),(short)(y + 1));
			OS.RectRgn(rectRgn, r);
			OS.UnionRgn(polyRgn, rectRgn, polyRgn);
		}
	}
	OS.DisposeRgn(rectRgn);
	return polyRgn;
}

static long polyRgn(int[] pointArray, int count) {
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		long polyRgn;
		if (C.PTR_SIZEOF == 4) {
			polyRgn = OS.NewRgn();
			OS.OpenRgn();
			OS.MoveTo((short)pointArray[0], (short)pointArray[1]);
			for (int i = 1; i < count / 2; i++) {
				OS.LineTo((short)pointArray[2 * i], (short)pointArray[2 * i + 1]);
			}
			OS.LineTo((short)pointArray[0], (short)pointArray[1]);
			OS.CloseRgn(polyRgn);
		} else {
			polyRgn = polyToRgn(pointArray, count);
		}
		return polyRgn;
	} finally {
		if (pool != null) pool.release();
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
 */
public void add (int[] pointArray) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		add(pointArray, pointArray.length);
	} finally {
		if (pool != null) pool.release();
	}
}

void add(int[] pointArray, int count) {
	count = count / 2 * 2;
	if (count <= 2) return;
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		long polyRgn = polyRgn(pointArray, count);
		OS.UnionRgn(handle, polyRgn, handle);
		OS.DisposeRgn(polyRgn);
	} finally {
		if (pool != null) pool.release();
	}
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
	if (rect.width < 0 || rect.height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		add (rect.x, rect.y, rect.width, rect.height);
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		long rectRgn = OS.NewRgn();
		short[] r = new short[4];
		OS.SetRect(r, (short)x, (short)y, (short)(x + width),(short)(y + height));
		OS.RectRgn(rectRgn, r);
		OS.UnionRgn(handle, rectRgn, handle);
		OS.DisposeRgn(rectRgn);
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		OS.UnionRgn(handle, region.handle, handle);
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		short[] point = new short[]{(short)y, (short)x};
		return OS.PtInRgn(point, handle);
	} finally {
		if (pool != null) pool.release();
	}
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
	if (pt == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return contains(pt.x, pt.y);
}

NSAffineTransform transform;
void convertRgn(NSAffineTransform transform) {
	long newRgn = OS.NewRgn();
	Callback callback = new Callback(this, "convertRgn", 4);
	this.transform = transform;
	OS.QDRegionToRects(handle, OS.kQDParseRegionFromTopLeft, callback.getAddress(), newRgn);
	this.transform = null;
	callback.dispose();
	OS.CopyRgn(newRgn, handle);
	OS.DisposeRgn(newRgn);
}

long convertRgn(long message, long rgn, long r, long newRgn) {
	if (message == OS.kQDRegionToRectsMsgParse) {
		short[] rect = new short[4];
		C.memmove(rect, r, rect.length * 2);
		int i = 0;
		NSPoint point = new NSPoint();
		int[] points = new int[10];
		point.x = rect[1];
		point.y = rect[0];
		point = transform.transformPoint(point);
		short startX, startY;
		points[i++] = startX = (short)point.x;
		points[i++] = startY = (short)point.y;
		point.x = rect[3];
		point.y = rect[0];
		point = transform.transformPoint(point);
		points[i++] = (short)Math.round(point.x);
		points[i++] = (short)point.y;
		point.x = rect[3];
		point.y = rect[2];
		point = transform.transformPoint(point);
		points[i++] = (short)Math.round(point.x);
		points[i++] = (short)Math.round(point.y);
		point.x = rect[1];
		point.y = rect[2];
		point = transform.transformPoint(point);
		points[i++] = (short)point.x;
		points[i++] = (short)Math.round(point.y);
		points[i++] = startX;
		points[i++] = startY;
		long polyRgn = polyRgn(points, points.length);
		OS.UnionRgn(newRgn, polyRgn, newRgn);
		OS.DisposeRgn(polyRgn);
	}
	return 0;
}

@Override
void destroy() {
	OS.DisposeRgn(handle);
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
	if (!(object instanceof Region region)) return false;
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		short[] bounds = new short[4];
		OS.GetRegionBounds(handle, bounds);
		int width = bounds[3] - bounds[1];
		int height = bounds[2] - bounds[0];
		return new Rectangle(bounds[1], bounds[0], width, height);
	} finally {
		if (pool != null) pool.release();
	}
}

NSBezierPath getPath() {
	Callback callback = new Callback(this, "regionToRects", 4);
	NSBezierPath path = NSBezierPath.bezierPath();
	path.retain();
	OS.QDRegionToRects(handle, OS.kQDParseRegionFromTopLeft, callback.getAddress(), path.id);
	callback.dispose();
	if (path.isEmpty()) path.appendBezierPathWithRect(new NSRect());
	return path;
}

NSPoint pt = new NSPoint();
short[] rect = new short[4];
long regionToRects(long message, long rgn, long r, long path) {
	if (message == OS.kQDRegionToRectsMsgParse) {
		C.memmove(rect, r, rect.length * 2);
		pt.x = rect[1];
		pt.y = rect[0];
		OS.objc_msgSend(path, OS.sel_moveToPoint_, pt);
		pt.x = rect[3];
		OS.objc_msgSend(path, OS.sel_lineToPoint_, pt);
		pt.x = rect[3];
		pt.y = rect[2];
		OS.objc_msgSend(path, OS.sel_lineToPoint_, pt);
		pt.x = rect[1];
		OS.objc_msgSend(path, OS.sel_lineToPoint_, pt);
		OS.objc_msgSend(path, OS.sel_closePath);
	}
	return 0;
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
public void intersect(int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0 || height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		long rectRgn = OS.NewRgn();
		short[] r = new short[4];
		OS.SetRect(r, (short)x, (short)y, (short)(x + width),(short)(y + height));
		OS.RectRgn(rectRgn, r);
		OS.SectRgn(handle, rectRgn, handle);
		OS.DisposeRgn(rectRgn);
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		OS.SectRgn(handle, region.handle, handle);
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		short[] r = new short[4];
		OS.SetRect(r, (short)x, (short)y, (short)(x + width),(short)(y + height));
		return OS.RectInRgn(r, handle);
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		return OS.EmptyRgn(handle);
	} finally {
		if (pool != null) pool.release();
	}
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
	if (pointArray.length < 2) return;
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		long polyRgn = polyRgn(pointArray, pointArray.length);
		OS.DiffRgn(handle, polyRgn, handle);
		OS.DisposeRgn(polyRgn);
	} finally {
		if (pool != null) pool.release();
	}
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
public void subtract(int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0 || height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		long rectRgn = OS.NewRgn();
		short[] r = new short[4];
		OS.SetRect(r, (short)x, (short)y, (short)(x + width),(short)(y + height));
		OS.RectRgn(rectRgn, r);
		OS.DiffRgn(handle, rectRgn, handle);
		OS.DisposeRgn(rectRgn);
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		OS.DiffRgn(handle, region.handle, handle);
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		OS.OffsetRgn (handle, (short)x, (short)y);
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		translate (pt.x, pt.y);
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
@Override
public String toString () {
	if (isDisposed()) return "Region {*DISPOSED*}";
	return "Region {" + handle + "}";
}
}

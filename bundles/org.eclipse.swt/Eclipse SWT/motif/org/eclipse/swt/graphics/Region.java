package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public final class Region {
	/**
	 * The handle to the OS region resource.
	 * Warning: This field is platform dependent.
	 */
	public int handle;
public Region () {
	handle = OS.XCreateRegion ();
}
Region (int handle) {
	this.handle = handle;
}
public void add (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	XRectangle xRect = new XRectangle();
	xRect.x = (short)rect.x;
	xRect.y = (short)rect.y;
	xRect.width = (short)rect.width;
	xRect.height = (short)rect.height;
	OS.XUnionRectWithRegion(xRect, handle, handle);
}
public void add (Region region) {
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	OS.XUnionRegion(handle, region.handle, handle);
}
public boolean contains (int x, int y) {
	return OS.XPointInRegion(handle, x, y);
}
public boolean contains (Point pt) {
	if (pt == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return contains(pt.x, pt.y);
}
public void dispose () {
	if (handle != 0) OS.XDestroyRegion(handle);
	handle = 0;
}
public boolean equals (Object object) {
	if (this == object) return true;
	if (!(object instanceof Region)) return false;
	int xRegion = ((Region)object).handle;
	if (handle == xRegion) return true;
	if (xRegion == 0) return false;
	return OS.XEqualRegion(handle, xRegion);
}
public Rectangle getBounds() {
	XRectangle rect = new XRectangle();
	OS.XClipBox(handle, rect);
	return new Rectangle(rect.x, rect.y, rect.width, rect.height);
}
public int hashCode () {
	return handle;
}
public boolean intersects (int x, int y, int width, int height) {
	return OS.XRectInRegion (handle, x, y, width, height) != OS.RectangleOut;
}
public boolean intersects (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return intersects(rect.x, rect.y, rect.width, rect.height);
}
public boolean isDisposed() {
	return handle == 0;
}
public boolean isEmpty () {
	return OS.XEmptyRegion(handle);
}
public static Region motif_new(int handle) {
	return new Region(handle);
}
}

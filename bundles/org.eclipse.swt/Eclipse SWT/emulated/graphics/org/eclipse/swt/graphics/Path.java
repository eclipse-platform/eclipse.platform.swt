/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public class Path extends Resource {
	
	/**
	 * the handle to the OS path resource
	 * (Warning: This field is platform dependent)
	 */
	public int handle;
	
	/**
	 * the device where this font was created
	 */
	Device device;
	
public Path (Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	SWT.error(SWT.ERROR_NO_GRAPHICS_LIBRARY);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

public void addArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

public void addPath(Path path) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
}

public void addRectangle(float x, float y, float width, float height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

public void addString(String string, float x, float y, Font font) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (font == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
}

public boolean contains(float x, float y, GC gc, boolean outline) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return false;
}

public void close() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

public void curveTo(float cx1, float cy1, float cx2, float cy2, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

public void getCurrentPoint(float[] point) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (point == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (point.length < 2) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
}

public void lineTo(float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

public void moveTo(float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

public void quadTo(float cx, float cy, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

public void dispose() {
	if (handle == 0) return;
	handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean isDisposed() {
	return handle == 0;
}

public String toString() {
	if (isDisposed()) return "Path {*DISPOSED*}";
	return "Path {" + handle + "}";
}

}

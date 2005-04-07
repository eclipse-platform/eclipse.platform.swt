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
import org.eclipse.swt.internal.cairo.*;

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public class Pattern extends Resource {

	/**
	 * the handle to the OS path resource
	 * (Warning: This field is platform dependent)
	 */
	public int /*long*/ handle;

public Pattern(Device device, Image image) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.device = device;
	device.checkCairo();
	image.createSurface();
	handle = Cairo.cairo_pattern_create_for_surface(image.surface);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	Cairo.cairo_pattern_set_extend(handle, Cairo.CAIRO_EXTEND_REPEAT);
	if (device.tracking) device.new_Object(this);
}

public Pattern(Device device, float x1, float y1, float x2, float y2, Color color1, Color color2) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color1 == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color1.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (color2 == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color2.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.device = device;
	device.checkCairo();
	handle = Cairo.cairo_pattern_create_linear(x1, y1, x2, y2);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	//TODO - how about alpha?
	GC.setCairoPatternColor(handle, 0, color1);
	GC.setCairoPatternColor(handle, 1, color2);
	Cairo.cairo_pattern_set_extend(handle, Cairo.CAIRO_EXTEND_REPEAT);
	if (device.tracking) device.new_Object(this);
}
	
public void dispose() {
	if (handle == 0) return;
	if (device.isDisposed()) return;
	Cairo.cairo_pattern_destroy(handle);
	handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean isDisposed() {
	return handle == 0;
}

public String toString() {
	if (isDisposed()) return "Pattern {*DISPOSED*}";
	return "Pattern {" + handle + "}";
}
	
}

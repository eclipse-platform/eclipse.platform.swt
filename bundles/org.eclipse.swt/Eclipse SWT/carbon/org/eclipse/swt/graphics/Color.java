package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;

public final class Color {
	
	public int handle;
	Device device;
	
Color() {
}

public Color (Device device, int red, int green, int blue) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, red, green, blue);
	if (device.tracking) device.new_Object(this);
}

public Color (Device device, RGB rgb) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (rgb == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, rgb.red, rgb.green, rgb.blue);
	if (device.tracking) device.new_Object(this);
}

public void dispose() {
	if (handle == -1) return;
	if (device.isDisposed()) return;
	handle = -1;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Color)) return false;
	Color color = (Color)object;
	return device == color.device && handle == color.handle; 
}

public int getBlue () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return handle & 0xFF;
}

public int getGreen () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return (handle >> 8) & 0xFF;
}

public int getRed () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return (handle >> 16) & 0xFF;
}

public RGB getRGB () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return new RGB((handle >> 16) & 0xFF, (handle >> 8) & 0xFF, (handle >> 0) & 0xFF);
}

public int hashCode () {
	return handle;
}

void init(Device device, int red, int green, int blue) {
	if (red > 255 || red < 0 || green > 255 || green < 0 || blue > 255 || blue < 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.device = device;
	handle = ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
}

public boolean isDisposed() {
	return handle == -1;
}

public String toString () {
	if (isDisposed()) return "Color {*DISPOSED*}";
	return "Color {" + getRed() + ", " + getGreen() + ", " + getBlue() + "}";
}

public static Color carbon_new(Device device, int packed, boolean system) {
	if (device == null) device = Device.getDevice();
	Color color = new Color();
	color.device = device;
	color.handle = packed;
//	if (system)
//		color.fFlags |= SYSTEM;
	return color;
}
}

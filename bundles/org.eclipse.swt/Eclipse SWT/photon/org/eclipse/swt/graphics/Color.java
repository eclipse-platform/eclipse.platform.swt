package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;

public final class Color {

	/**
	 * the handle to the OS color resource 
	 * (Warning: This field is platform dependent)
	 */
	public int handle;

	/**
	 * the device where this color was created
	 */
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

	handle = -1;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Color)) return false;
	Color color = (Color) object;
	return device == color.device && (handle & 0xFFFFFF) == (color.handle & 0xFFFFFF);
}

public int getBlue () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return handle & 0xFF;
}

public int getGreen () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return (handle & 0xFF00) >> 8;
}

public int getRed () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return (handle & 0xFF0000) >> 16;
}

public RGB getRGB () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return new RGB((handle & 0xFF0000) >> 16, (handle & 0xFF00) >> 8, handle & 0xFF);
}

public int hashCode () {
	return handle;
}

void init(Device device, int red, int green, int blue) {
	if (red > 255 || red < 0 || green > 255 || green < 0 || blue > 255 || blue < 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.device = device;
	handle = (blue & 0xFF) | ((green & 0xFF) << 8) | ((red & 0xFF) << 16);
}

public boolean isDisposed() {
	return handle == -1;
}

public String toString () {
	if (isDisposed()) return "Color {*DISPOSED*}";
	return "Color {" + getRed() + ", " + getGreen() + ", " + getBlue() + "}";
}

public static Color photon_new(Device device, int handle) {
	if (device == null) device = Device.getDevice();
	Color color = new Color();
	color.handle = handle;
	color.device = device;
	return color;
}
}

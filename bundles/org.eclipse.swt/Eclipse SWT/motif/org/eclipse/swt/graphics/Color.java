package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public final class Color {
	/**
	 * The handle to the OS color resource.
	 * Warning: This field is platform dependent.
	 */
	public XColor handle;

	/**
	 * The device where this image was created.
	 */
	Device device;

Color() {
}
public Color (Device device, int red, int green, int blue) {
	init(device, red, green, blue);
}
public Color (Device device, RGB rgb) {
	if (rgb == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, rgb.red, rgb.green, rgb.blue);
}
public void dispose() {
	if (handle == null) return;
	int xDisplay = device.xDisplay;
	int pixel = handle.pixel;
	if (device.colorRefCount != null) {
		/* If this was the last reference, remove the color from the list */
		if (--device.colorRefCount[pixel] == 0) {
			device.xcolors[pixel] = null;
		}
	}
	int colormap = OS.XDefaultColormap(xDisplay, OS.XDefaultScreen(xDisplay));
	OS.XFreeColors(xDisplay, colormap, new int[] { pixel }, 1, 0);
	device = null;
	handle = null;
}
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Color)) return false;
	Color color = (Color)object;
	XColor xColor = color.handle;
	return device == color.device && handle.red == xColor.red &&
		handle.green == xColor.green && handle.blue == xColor.blue;
}
public int getBlue () {
	return (handle.blue >> 8) & 0xFF;
}
public int getGreen () {
	return (handle.green >> 8) & 0xFF;
}
public int getRed () {
	return (handle.red >> 8) & 0xFF;
}
public RGB getRGB () {
	return new RGB((handle.red >> 8) & 0xFF, (handle.green >> 8) & 0xFF, (handle.blue >> 8) & 0xFF);
}
public int hashCode () {
	return handle.red ^ handle.green ^ handle.blue;
}
void init(Device device, int red, int green, int blue) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if ((red > 255) || (red < 0) ||
		(green > 255) || (green < 0) ||
		(blue > 255) || (blue < 0)) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	XColor xColor = new XColor();
	xColor.red = (short)((red & 0xFF) | ((red & 0xFF) << 8));
	xColor.green = (short)((green & 0xFF) | ((green & 0xFF) << 8));
	xColor.blue = (short)((blue & 0xFF) | ((blue & 0xFF) << 8));
	handle = xColor;
	int xDisplay = device.xDisplay;
	int screen = OS.XDefaultScreen(xDisplay);
	int colormap = OS.XDefaultColormap(xDisplay, screen);
	/* 1. Try to allocate the color */
	if (OS.XAllocColor(xDisplay, colormap, xColor) != 0) {
		if (device.colorRefCount != null) {
			/* Make a copy of the color to put in the colors array */
			XColor colorCopy = new XColor();
			colorCopy.red = xColor.red;
			colorCopy.green = xColor.green;
			colorCopy.blue = xColor.blue;
			colorCopy.pixel = xColor.pixel;
			device.xcolors[colorCopy.pixel] = colorCopy;
			device.colorRefCount[xColor.pixel]++;
		}
		return;
	}
	/**
	 * 2. Allocation failed. Query the entire colormap and
	 * find the closest match which can be allocated.
	 * This should never occur on a truecolor display.
	 */
	Visual visual = new Visual();
	OS.memmove(visual, OS.XDefaultVisual(xDisplay, screen), Visual.sizeof);
	int mapEntries = visual.map_entries;
	XColor[] queried = new XColor[mapEntries];
	int[] distances = new int[mapEntries];
	/**
	 * Query all colors in the colormap and calculate the distance
	 * from each to the desired color.
	 */
	for (int i = 0; i < mapEntries; i++) {
		XColor color = new XColor();
		color.pixel = i;
		queried[i] = color;
		OS.XQueryColor(xDisplay, colormap, color);
		int r = red - ((color.red >> 8) & 0xFF);
		int g = green - ((color.green >> 8) & 0xFF);
		int b = blue - ((color.blue >> 8) & 0xFF);
		distances[i] = r*r + g*g + b*b;
	}
	/**
	 * Try to allocate closest matching queried color.
	 * The allocation can fail if the closest matching
	 * color is allocated privately, so go through them
	 * in order of increasing distance.
	 */
	for (int i = 0; i < mapEntries; i++) {
		int minDist = 0x30000;
		int minIndex = 0;
		for (int j = 0; j < mapEntries; j++) {
			if (distances[j] < minDist) {
				minDist = distances[j];
				minIndex = j;
			}
		}
		XColor queriedColor = queried[minIndex];
		XColor osColor = new XColor();
		osColor.red = queriedColor.red;
		osColor.green = queriedColor.green;
		osColor.blue = queriedColor.blue;
		if (OS.XAllocColor(xDisplay, colormap, osColor) != 0) {
			/* Allocation succeeded. Copy the fields into the handle */
			xColor.red = osColor.red;
			xColor.green = osColor.green;
			xColor.blue = osColor.blue;
			xColor.pixel = osColor.pixel;
			if (device.colorRefCount != null) {
				/* Put osColor in the colors array */
				device.xcolors[osColor.pixel] = osColor;
				device.colorRefCount[osColor.pixel]++;
			}
			return;
		}
		/* The allocation failed; matching color is allocated privately */
		distances[minIndex] = 0x30000;
	}
	/**
	 * 3. Couldn't allocate any of the colors in the colormap.
	 * This means all colormap entries were allocated privately
	 * by other applications. Give up and allocate black.
	 */
	XColor osColor = new XColor();
	OS.XAllocColor(xDisplay, colormap, osColor);
	/* Copy the fields into the handle */
	xColor.red = osColor.red;
	xColor.green = osColor.green;
	xColor.blue = osColor.blue;
	xColor.pixel = osColor.pixel;
	if (device.colorRefCount != null) {
		/* Put osColor in the colors array */
		device.xcolors[osColor.pixel] = osColor;
		device.colorRefCount[osColor.pixel]++;
	}
}
public boolean isDisposed() {
	return handle == null;
}
public static Color motif_new(Device device, XColor xColor) {
	if (device == null) device = Device.getDevice();
	Color color = new Color();
	color.handle = xColor;
	color.device = device;
	return color;
}
/**
 * Return a string representation of the Color.
 *
 * @return a string representation of the Color
 */
public String toString () {
	return "Color {" + getRed() + ", " + getGreen() + ", " + getBlue() + "}";
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

 
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

/**
 * Instances of this class manage the operating system resources that
 * implement SWT's RGB color model. To create a color you can either
 * specify the individual color components as integers in the range 
 * 0 to 255 or provide an instance of an <code>RGB</code>. 
 * <p>
 * Application code must explicitly invoke the <code>Color.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see RGB
 * @see Device#getSystemColor
 * @see <a href="http://www.eclipse.org/swt/snippets/#color">Color and RGB snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: PaintExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class Color extends Resource {
	/**
	 * the handle to the OS color resource 
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
	public XColor handle;

Color(Device device) {
	super(device);
}
/**	 
 * Constructs a new instance of this class given a device and the
 * desired red, green and blue values expressed as ints in the range
 * 0 to 255 (where 0 is black and 255 is full brightness). On limited
 * color devices, the color instance created by this call may not have
 * the same RGB values as the ones specified by the arguments. The
 * RGB values on the returned instance will be the color values of 
 * the operating system color.
 * <p>
 * You must dispose the color when it is no longer required. 
 * </p>
 *
 * @param device the device on which to allocate the color
 * @param red the amount of red in the color
 * @param green the amount of green in the color
 * @param blue the amount of blue in the color
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the red, green or blue argument is not between 0 and 255</li>
 * </ul>
 *
 * @see #dispose
 */
public Color (Device device, int red, int green, int blue) {
	super(device);
	init(red, green, blue);
	init();
}
/**	 
 * Constructs a new instance of this class given a device and an
 * <code>RGB</code> describing the desired red, green and blue values.
 * On limited color devices, the color instance created by this call
 * may not have the same RGB values as the ones specified by the
 * argument. The RGB values on the returned instance will be the color
 * values of the operating system color.
 * <p>
 * You must dispose the color when it is no longer required. 
 * </p>
 *
 * @param device the device on which to allocate the color
 * @param rgb the RGB values of the desired color
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the rgb argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the red, green or blue components of the argument are not between 0 and 255</li>
 * </ul>
 *
 * @see #dispose
 */
public Color (Device device, RGB rgb) {
	super(device);
	if (rgb == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(rgb.red, rgb.green, rgb.blue);
	init();
}
void destroy() {
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
	handle = null;
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
	if (object == this) return true;
	if (!(object instanceof Color)) return false;
	Color color = (Color)object;
	XColor xColor = color.handle;
	if (handle == xColor) return true;
	return device == color.device && handle.red == xColor.red &&
		handle.green == xColor.green && handle.blue == xColor.blue;
}
/**
 * Returns the amount of blue in the color, from 0 to 255.
 *
 * @return the blue component of the color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getBlue () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return (handle.blue >> 8) & 0xFF;
}
/**
 * Returns the amount of green in the color, from 0 to 255.
 *
 * @return the green component of the color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getGreen () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return (handle.green >> 8) & 0xFF;
}
/**
 * Returns the amount of red in the color, from 0 to 255.
 *
 * @return the red component of the color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getRed () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return (handle.red >> 8) & 0xFF;
}
/**
 * Returns an <code>RGB</code> representing the receiver.
 *
 * @return the RGB for the color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public RGB getRGB () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return new RGB((handle.red >> 8) & 0xFF, (handle.green >> 8) & 0xFF, (handle.blue >> 8) & 0xFF);
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
public int hashCode () {
	if (isDisposed()) return 0;
	return handle.red ^ handle.green ^ handle.blue;
}
void init(int red, int green, int blue) {
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
	/*
	 * 2. Allocation failed. Query the entire colormap and
	 * find the closest match which can be allocated.
	 * This should never occur on a truecolor display.
	 */
	Visual visual = new Visual();
	OS.memmove(visual, OS.XDefaultVisual(xDisplay, screen), Visual.sizeof);
	int mapEntries = visual.map_entries;
	XColor[] queried = new XColor[mapEntries];
	int[] distances = new int[mapEntries];
	/*
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
	/*
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
	/*
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
/**
 * Returns <code>true</code> if the color has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the color.
 * When a color has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the color.
 *
 * @return <code>true</code> when the color is disposed and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == null;
}
public static Color motif_new(Device device, XColor xColor) {
	Color color = new Color(device);
	color.handle = xColor;
	return color;
}
/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Color {*DISPOSED*}";
	return "Color {" + getRed() + ", " + getGreen() + ", " + getBlue() + "}";
}
}

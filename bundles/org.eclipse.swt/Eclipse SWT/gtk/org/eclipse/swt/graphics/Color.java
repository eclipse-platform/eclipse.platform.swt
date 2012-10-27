/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.internal.gtk.*;
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
	public GdkColor handle;

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
public Color(Device device, int red, int green, int blue) {
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
public Color(Device device, RGB rgb) {
	super(device);
	if (rgb == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(rgb.red, rgb.green, rgb.blue);
	init();
}

void destroy() {
	int pixel = handle.pixel;
	if (device.colorRefCount != null) {
		/* If this was the last reference, remove the color from the list */
		if (--device.colorRefCount[pixel] == 0) {
			device.gdkColors[pixel] = null;
		}
	}
	if (OS.GTK_VERSION < OS.VERSION(3, 0, 0)) {
		long /*int*/ colormap = OS.gdk_colormap_get_system();
		OS.gdk_colormap_free_colors(colormap, handle, 1);
	}
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
public boolean equals(Object object) {
	if (object == this) return true;
	if (!(object instanceof Color)) return false;
	Color color = (Color)object;
	GdkColor gdkColor = color.handle;
	if (handle == gdkColor) return true;
	return device == color.device && handle.red == gdkColor.red &&
		handle.green == gdkColor.green && handle.blue == gdkColor.blue;
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
public int getBlue() {
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
public int getGreen() {
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
public int getRed() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return (handle.red >> 8) & 0xFF;
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
public int hashCode() {
	if (isDisposed()) return 0;
	return handle.red ^ handle.green ^ handle.blue;
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
	return new RGB(getRed(), getGreen(), getBlue());
}

/**	 
 * Invokes platform specific functionality to allocate a new color.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Color</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param device the device on which to allocate the color
 * @param gdkColor the handle for the color
 * 
 * @noreference This method is not intended to be referenced by clients.
 */
public static Color gtk_new(Device device, GdkColor gdkColor) {
	Color color = new Color(device);
	color.handle = gdkColor;
	return color;
}

void init(int red, int green, int blue) {
	if ((red > 255) || (red < 0) ||
		(green > 255) || (green < 0) ||
		(blue > 255) || (blue < 0)) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	GdkColor gdkColor = new GdkColor();
	gdkColor.red = (short)((red & 0xFF) | ((red & 0xFF) << 8));
	gdkColor.green = (short)((green & 0xFF) | ((green & 0xFF) << 8));
	gdkColor.blue = (short)((blue & 0xFF) | ((blue & 0xFF) << 8));
	if (OS.GTK_VERSION < OS.VERSION(3, 0, 0)) {
		long /*int*/ colormap = OS.gdk_colormap_get_system();
		if (!OS.gdk_colormap_alloc_color(colormap, gdkColor, true, true)) {
			/* Allocate black. */
			gdkColor = new GdkColor();
			OS.gdk_colormap_alloc_color(colormap, gdkColor, true, true);
		}
	}
	handle = gdkColor;
	if (device.colorRefCount != null) {
		/* Make a copy of the color to put in the colors array */
		GdkColor colorCopy = new GdkColor();
		colorCopy.red = handle.red;
		colorCopy.green = handle.green;
		colorCopy.blue = handle.blue;
		colorCopy.pixel = handle.pixel;
		device.gdkColors[colorCopy.pixel] = colorCopy;
		device.colorRefCount[colorCopy.pixel]++;
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

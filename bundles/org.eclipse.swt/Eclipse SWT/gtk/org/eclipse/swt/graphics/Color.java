package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.gtk.*;

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
 */
public final class Color {
	/**
	 * the handle to the OS color resource 
	 * (Warning: This field is platform dependent)
	 */
	public GdkColor handle;
	Device display;
Color() {
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
 *    <li>ERROR_INVALID_ARGUMENT - if the red, green or blue argument is not between 0 and 255</li>
 * </ul>
 *
 * @see #dispose
 */
public Color(Device display, int red, int green, int blue) {
	init(display, red, green, blue);
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
 * @param RGB the RGB values of the desired color
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the red, green or blue components of the argument are not between 0 and 255</li>
 *    <li>ERROR_NULL_ARGUMENT - if the rgb argument is null</li>
 * </ul>
 *
 * @see #dispose
 */
public Color(Device display, RGB rgb) {
	if (rgb == null) error(SWT.ERROR_NULL_ARGUMENT);
	init(display, rgb.red, rgb.green, rgb.blue);
}
/**
 * Disposes of the operating system resources associated with
 * the color. Applications must dispose of all colors which
 * they allocate.
 */
public void dispose() {
	/**
	 * If this is a palette-based display,
	 * Decrease the reference count for this color.
	 * If the reference count reaches 0, the slot may
	 * be reused when another color is allocated.
	 */
	if (display.colorRefCount != null) {
		if (--display.colorRefCount[handle.pixel] == 0) {
			display.gdkColors[handle.pixel] = null;
		}
	}
	int colormap = OS.gdk_colormap_get_system();
	OS.gdk_colors_free(colormap, new int[] { handle.pixel }, 1, 0);
	this.display = null;
	this.handle = null;
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
	GdkColor xColor = color.handle;
	return (handle.pixel == xColor.pixel)&&(handle.red == xColor.red) && (handle.green == xColor.green) && (handle.blue == xColor.blue) && (this.display == color.display);
}
void error(int code) {
	throw new SWTError(code);
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
	return (handle.red >> 8) & 0xFF;
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects which return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals
 */
public int hashCode() {
	return handle.red ^ handle.green ^ handle.blue;
}
/**
 * Returns an <code>RGB</code> representing the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public RGB getRGB () {
	return new RGB(getRed(), getGreen(), getBlue());
}

void init(Device display, int red, int green, int blue) {
	if (display == null) display = Display.getDefault();
	this.display = display;
	handle = new GdkColor();
	handle.red = (short)((red & 0xFF) | ((red & 0xFF) << 8));
	handle.green = (short)((green & 0xFF) | ((green & 0xFF) << 8));
	handle.blue = (short)((blue & 0xFF) | ((blue & 0xFF) << 8));
	int colormap = OS.gdk_colormap_get_system();
	OS.gdk_color_alloc(colormap, handle);
	if (display.colorRefCount != null) {
		// Make a copy of the color to put in the colors array
		GdkColor colorCopy = new GdkColor();
		colorCopy.red = handle.red;
		colorCopy.green = handle.green;
		colorCopy.blue = handle.blue;
		colorCopy.pixel = handle.pixel;
		display.gdkColors[colorCopy.pixel] = colorCopy;
		display.colorRefCount[colorCopy.pixel]++;
	}
}
/**
 * Returns <code>true</code> if the color has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the color.
 * When a color has been disposed, it is an error to
 * invoke any other method using the color.
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
	return "Color {" + getRed() + ", " + getGreen() + ", " + getBlue() + "}";
}

public static Color gtk_new(GdkColor gdkColor) {
	Color color = new Color(null, gtk_getRGBIntensities(gdkColor));
	return color;
}

static RGB gtk_getRGBIntensities(GdkColor gdkColor) {
	boolean intensitiesAreZero = (gdkColor.red==0) && (gdkColor.green==0) && (gdkColor.blue==0);
	if (!intensitiesAreZero) return new RGB ((gdkColor.red&0xFF00)>>8,
	                                        (gdkColor.green&0xFF00)>>8,
	                                        (gdkColor.blue&0xFF00)>>8 );
	GdkVisual visual = new GdkVisual(OS.gdk_visual_get_system());
	int r = (gdkColor.pixel&visual.red_mask) >> visual.red_shift;
	if (visual.red_prec<8) r = r << (8 - visual.red_prec);
		else r = r >> (visual.red_prec - 8);
	int g = (gdkColor.pixel&visual.green_mask) >> visual.green_shift;
		if (visual.green_prec<8) g = g << (8 - visual.green_prec);
	else g = g >> (visual.green_prec - 8);
		int b = (gdkColor.pixel&visual.blue_mask) >> visual.blue_shift;
	if (visual.blue_prec<8) b = b << (8 - visual.blue_prec);
		else b = b >> (visual.blue_prec - 8);

	return new RGB(r, g, b);
}

}

package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.image.*;

/**
 * Instances of this class manage operating system resources that
 * specify the appearance of the on-screen pointer. To create a
 * cursor you specify the device and either a simple cursor style
 * describing one of the standard operating system provided cursors
 * or the image and mask data for the desired appearance.
 * <p>
 * Application code must explicitly invoke the <code>Cursor.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>
 *   CURSOR_ARROW, CURSOR_WAIT, CURSOR_CROSS, CURSOR_APPSTARTING, CURSOR_HELP,
 *   CURSOR_SIZEALL, CURSOR_SIZENESW, CURSOR_SIZENS, CURSOR_SIZENWSE, CURSOR_SIZEWE,
 *   CURSOR_SIZEN, CURSOR_SIZES, CURSOR_SIZEE, CURSOR_SIZEW, CURSOR_SIZENE, CURSOR_SIZESE,
 *   CURSOR_SIZESW, CURSOR_SIZENW, CURSOR_UPARROW, CURSOR_IBEAM, CURSOR_NO, CURSOR_HAND
 * </dd>
 * </dl>
 */
public final class Cursor {
	/**
	 * the handle to the OS cursor resource
	 * (Warning: This field is platform dependent)
	 */
	public int handle;

	/**
	 * The device where this image was created.
	 */
	Device device;

Cursor () {
}

/**	 
 * Constructs a new cursor given a device and a style
 * constant describing the desired cursor appearance.
 * <p>
 * You must dispose the cursor when it is no longer required. 
 * </p>
 *
 * @param device the device on which to allocate the cursor
 * @param style the style of cursor to allocate
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - when an unknown style is specified</li>
 * </ul>
 *
 * @see Cursor for the supported style values
 */
public Cursor(Device display, int style) {
	int osFlag = 0;
	switch (style) {
		case SWT.CURSOR_ARROW:
			osFlag = OS.GDK_LEFT_PTR;
			break;
		case SWT.CURSOR_WAIT:
			osFlag = OS.GDK_WATCH;
			break;
		case SWT.CURSOR_CROSS:
			osFlag = OS.GDK_CROSS;
			break;
		case SWT.CURSOR_APPSTARTING:
			osFlag = OS.GDK_WATCH;
			break;
		case SWT.CURSOR_HAND:
			osFlag = OS.GDK_HAND1;
			break;
		case SWT.CURSOR_HELP:
			osFlag = OS.GDK_QUESTION_ARROW;
			break;
		case SWT.CURSOR_SIZEALL:
			osFlag = OS.GDK_DIAMOND_CROSS;
			break;
		case SWT.CURSOR_SIZENESW:
			osFlag = OS.GDK_SIZING;
			break;
		case SWT.CURSOR_SIZENS:
			osFlag = OS.GDK_DOUBLE_ARROW;
			break;
		case SWT.CURSOR_SIZENWSE:
			osFlag = OS.GDK_SIZING;
			break;
		case SWT.CURSOR_SIZEWE:
			osFlag = OS.GDK_SB_H_DOUBLE_ARROW;
			break;
		case SWT.CURSOR_SIZEN:
			osFlag = OS.GDK_TOP_SIDE;
			break;
		case SWT.CURSOR_SIZES:
			osFlag = OS.GDK_BOTTOM_SIDE;
			break;
		case SWT.CURSOR_SIZEE:
			osFlag = OS.GDK_RIGHT_SIDE;
			break;
		case SWT.CURSOR_SIZEW:
			osFlag = OS.GDK_LEFT_SIDE;
			break;
		case SWT.CURSOR_SIZENE:
			osFlag = OS.GDK_TOP_RIGHT_CORNER;
			break;
		case SWT.CURSOR_SIZESE:
			osFlag = OS.GDK_BOTTOM_RIGHT_CORNER;
			break;
		case SWT.CURSOR_SIZESW:
			osFlag = OS.GDK_BOTTOM_LEFT_CORNER;
			break;
		case SWT.CURSOR_SIZENW:
			osFlag = OS.GDK_TOP_LEFT_CORNER;
			break;
		case SWT.CURSOR_UPARROW:
			osFlag = OS.GDK_SB_UP_ARROW;
			break;
		case SWT.CURSOR_IBEAM:
			osFlag = OS.GDK_XTERM;
			break;
		case SWT.CURSOR_NO:
			osFlag = OS.GDK_X_CURSOR;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	this.handle = OS.gdk_cursor_new(osFlag);
}

/**	 
 * Constructs a new cursor given a device, image and mask
 * data describing the desired cursor appearance, and the x
 * and y co-ordinates of the <em>hotspot</em> (that is, the point
 * within the area covered by the cursor which is considered
 * to be where the on-screen pointer is "pointing").
 * <p>
 * The mask data is allowed to be null, but in this case the source
 * must be an ImageData representing an icon that specifies both
 * color data and mask data.
 * <p>
 * You must dispose the cursor when it is no longer required. 
 * </p>
 *
 * @param device the device on which to allocate the cursor
 * @param source the color data for the cursor
 * @param mask the mask data for the cursor (or null)
 * @param hotspotX the x coordinate of the cursor's hotspot
 * @param hotspotY the y coordinate of the cursor's hotspot
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - when a null argument is passed that is not allowed</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the source and the mask are not the same 
 *          size, or either is not of depth one, or if the hotspot is outside 
 *          the bounds of the image</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if an error occurred constructing the cursor</li>
 * </ul>
 */
public Cursor(Device display, ImageData source, ImageData mask, int hotspotX, int hotspotY) {
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) {
		if (!(source.getTransparencyType() == SWT.TRANSPARENCY_MASK)) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		mask = source.getTransparencyMask();
	}
	/* Check the bounds. Mask must be the same size as source */
	if (mask.width != source.width || mask.height != source.height) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	/* Check depths */
	if (mask.depth != 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (source.depth != 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	/* Check the hotspots */
	if (hotspotX >= source.width || hotspotX < 0 ||
		hotspotY >= source.height || hotspotY < 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	/* Swap the bits if necessary */
	byte[] sourceData = new byte[source.data.length];
	byte[] maskData = new byte[mask.data.length];
	/* Swap the bits in each byte */
	byte[] data = source.data;
	for (int i = 0; i < data.length; i++) {
		byte s = data[i];
		sourceData[i] = (byte)(((s & 0x80) >> 7) |
			((s & 0x40) >> 5) |
			((s & 0x20) >> 3) |
			((s & 0x10) >> 1) |
			((s & 0x08) << 1) |
			((s & 0x04) << 3) |
			((s & 0x02) << 5) |
			((s & 0x01) << 7));
		sourceData[i] = (byte) ~sourceData[i];
	}
	data = mask.data;
	for (int i = 0; i < data.length; i++) {
		byte s = data[i];
		maskData[i] = (byte)(((s & 0x80) >> 7) |
			((s & 0x40) >> 5) |
			((s & 0x20) >> 3) |
			((s & 0x10) >> 1) |
			((s & 0x08) << 1) |
			((s & 0x04) << 3) |
			((s & 0x02) << 5) |
			((s & 0x01) << 7));
		maskData[i] = (byte) ~maskData[i];
	}

	int sourcePixmap = OS.gdk_bitmap_create_from_data(0, sourceData, source.width, source.height);
	if (sourcePixmap==0) SWT.error(SWT.ERROR_NO_HANDLES);
	int maskPixmap = OS.gdk_bitmap_create_from_data(0, maskData, source.width, source.height);
	if (maskPixmap==0) SWT.error(SWT.ERROR_NO_HANDLES);

	/* Get the colors */
	GdkColor foreground = new GdkColor();
	foreground.red = 0;
	foreground.green = 0;
	foreground.blue = 0;
	GdkColor background = new GdkColor();
	background.red = (short)65535;
	background.green = (short)65535;
	background.blue = (short)65535;

	/* Create the cursor */
	/* For some reason, mask and source take reverse roles, both here and on Motif */
	handle = OS.gdk_cursor_new_from_pixmap (maskPixmap, sourcePixmap, foreground, background, hotspotX, hotspotY);
	/* Dispose the pixmaps */
	OS.g_object_unref (sourcePixmap);
	OS.g_object_unref (maskPixmap);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
}

/**
 * Disposes of the operating system resources associated with
 * the cursor. Applications must dispose of all cursors which
 * they allocate.
 */
public void dispose() {
	if (handle != 0) OS.gdk_cursor_destroy(handle);
	handle = 0;
	device = null;
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
	if (!(object instanceof Cursor)) return false;
	Cursor cursor = (Cursor) object;
	return device == cursor.device && handle == cursor.handle;
}

/**	 
 * Invokes platform specific functionality to allocate a new cursor.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Cursor</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param device the device on which to allocate the color
 * @param handle the handle for the cursor
 * 
 * @private
 */
public static Cursor gtk_new(Device device, int handle) {
	if (device == null) device = Device.getDevice();
	Cursor cursor = new Cursor();
	cursor.handle = handle;
	cursor.device = device;
	return cursor;
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
	return handle;
}

/**
 * Returns <code>true</code> if the cursor has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the cursor.
 * When a cursor has been disposed, it is an error to
 * invoke any other method using the cursor.
 *
 * @return <code>true</code> when the cursor is disposed and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == 0;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Cursor {*DISPOSED*}";
	return "Cursor {" + handle + "}";
}

}

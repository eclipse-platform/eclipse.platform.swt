package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

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
public Cursor (Device device, int style) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	int shape = 0;
	switch (style) {
		case SWT.CURSOR_ARROW: shape = OS.XC_left_ptr; break;
		case SWT.CURSOR_WAIT: shape = OS.XC_watch; break;
		case SWT.CURSOR_HAND: shape = OS.XC_hand2; break;
		case SWT.CURSOR_CROSS: shape = OS.XC_cross; break;
		case SWT.CURSOR_APPSTARTING: shape = OS.XC_watch; break;
		case SWT.CURSOR_HELP: shape = OS.XC_question_arrow; break;
		case SWT.CURSOR_SIZEALL: shape = OS.XC_diamond_cross; break;
		case SWT.CURSOR_SIZENESW: shape = OS.XC_sizing; break;
		case SWT.CURSOR_SIZENS: shape = OS.XC_double_arrow; break;
		case SWT.CURSOR_SIZENWSE: shape = OS.XC_sizing; break;
		case SWT.CURSOR_SIZEWE: shape = OS.XC_sb_h_double_arrow; break;
		case SWT.CURSOR_SIZEN: shape = OS.XC_top_side; break;
		case SWT.CURSOR_SIZES: shape = OS.XC_bottom_side; break;
		case SWT.CURSOR_SIZEE: shape = OS.XC_right_side; break;
		case SWT.CURSOR_SIZEW: shape = OS.XC_left_side; break;
		case SWT.CURSOR_SIZENE: shape = OS.XC_top_right_corner; break;
		case SWT.CURSOR_SIZESE: shape = OS.XC_bottom_right_corner; break;
		case SWT.CURSOR_SIZESW: shape = OS.XC_bottom_left_corner; break;
		case SWT.CURSOR_SIZENW: shape = OS.XC_top_left_corner; break;
		case SWT.CURSOR_UPARROW: shape = OS.XC_sb_up_arrow; break;
		case SWT.CURSOR_IBEAM: shape = OS.XC_xterm; break;
		case SWT.CURSOR_NO: shape = OS.XC_X_cursor; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.handle = OS.XCreateFontCursor(device.xDisplay, shape);
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
public Cursor (Device device, ImageData source, ImageData mask, int hotspotX, int hotspotY) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) {
		if (source.getTransparencyType() != SWT.TRANSPARENCY_MASK) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
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
	int xDisplay = device.xDisplay;
	int drawable = OS.XDefaultRootWindow(xDisplay);
	int sourcePixmap = OS.XCreateBitmapFromData(xDisplay, drawable, sourceData, source.width, source.height);
	int maskPixmap = OS.XCreateBitmapFromData(xDisplay, drawable, maskData, source.width, source.height);
	/* Get the colors */
	int screenNum = OS.XDefaultScreen(xDisplay);
	XColor foreground = new XColor();
	foreground.pixel = OS.XBlackPixel(xDisplay, screenNum);
	foreground.red = foreground.green = foreground.blue = 0;
	XColor background = new XColor();
	background.pixel = OS.XWhitePixel(xDisplay, screenNum);
	background.red = background.green = background.blue = (short)0xFFFF;
	/* Create the cursor */
	handle = OS.XCreatePixmapCursor(xDisplay, maskPixmap, sourcePixmap, foreground, background, hotspotX, hotspotY);
	/* Dispose the pixmaps */
	OS.XFreePixmap(xDisplay, sourcePixmap);
	OS.XFreePixmap(xDisplay, maskPixmap);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
}
/**
 * Disposes of the operating system resources associated with
 * the cursor. Applications must dispose of all cursors which
 * they allocate.
 */
public void dispose () {
	if (handle == 0) return;
	if (device.isDisposed()) return;
	OS.XFreeCursor(device.xDisplay, handle);
	device = null;
	handle = 0;
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
	if (!(object instanceof Cursor)) return false;
	Cursor cursor = (Cursor)object;
	return device == cursor.device && handle == cursor.handle;
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
public int hashCode () {
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
public static Cursor motif_new(Device device, int handle) {
	if (device == null) device = Device.getDevice();
	Cursor cursor = new Cursor();
	cursor.device = device;
	cursor.handle = handle;
	return cursor;
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

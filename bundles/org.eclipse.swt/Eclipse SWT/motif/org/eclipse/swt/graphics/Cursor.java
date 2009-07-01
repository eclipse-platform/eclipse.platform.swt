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
 * <p>
 * Note: Only one of the above styles may be specified.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#cursor">Cursor snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class Cursor extends Resource {
	/**
	 * the handle to the OS cursor resource
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int handle;

	static final byte[] APPSTARTING_SRC = {
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00,
		0x0c, 0x00, 0x00, 0x00, 0x1c, 0x00, 0x00, 0x00, 0x3c, 0x00, 0x00, 0x00,
		0x7c, 0x00, 0x00, 0x00, (byte)0xfc, 0x00, 0x00, 0x00, (byte)0xfc, 0x01, 0x00, 0x00,
		(byte)0xfc, 0x3b, 0x00, 0x00, 0x7c, 0x38, 0x00, 0x00, 0x6c, 0x54, 0x00, 0x00,
		(byte)0xc4, (byte)0xdc, 0x00, 0x00, (byte)0xc0, 0x44, 0x00, 0x00, (byte)0x80, 0x39, 0x00, 0x00,
		(byte)0x80, 0x39, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

	static final byte[] APPSTARTING_MASK = {
		0x00, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x0e, 0x00, 0x00, 0x00,
		0x1e, 0x00, 0x00, 0x00, 0x3e, 0x00, 0x00, 0x00, 0x7e, 0x00, 0x00, 0x00,
		(byte)0xfe, 0x00, 0x00, 0x00, (byte)0xfe, 0x01, 0x00, 0x00, (byte)0xfe, 0x3b, 0x00, 0x00,
		(byte)0xfe, 0x7f, 0x00, 0x00, (byte)0xfe, 0x7f, 0x00, 0x00, (byte)0xfe, (byte)0xfe, 0x00, 0x00,
		(byte)0xee, (byte)0xff, 0x01, 0x00, (byte)0xe4, (byte)0xff, 0x00, 0x00, (byte)0xc0, 0x7f, 0x00, 0x00,
		(byte)0xc0, 0x7f, 0x00, 0x00, (byte)0x80, 0x39, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

Cursor (Device device) {
	super(device);
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
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_INVALID_ARGUMENT - when an unknown style is specified</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a handle could not be obtained for cursor creation</li>
 * </ul>
 *
 * @see SWT#CURSOR_ARROW
 * @see SWT#CURSOR_WAIT
 * @see SWT#CURSOR_CROSS
 * @see SWT#CURSOR_APPSTARTING
 * @see SWT#CURSOR_HELP
 * @see SWT#CURSOR_SIZEALL
 * @see SWT#CURSOR_SIZENESW
 * @see SWT#CURSOR_SIZENS
 * @see SWT#CURSOR_SIZENWSE
 * @see SWT#CURSOR_SIZEWE
 * @see SWT#CURSOR_SIZEN
 * @see SWT#CURSOR_SIZES
 * @see SWT#CURSOR_SIZEE
 * @see SWT#CURSOR_SIZEW
 * @see SWT#CURSOR_SIZENE
 * @see SWT#CURSOR_SIZESE
 * @see SWT#CURSOR_SIZESW
 * @see SWT#CURSOR_SIZENW
 * @see SWT#CURSOR_UPARROW
 * @see SWT#CURSOR_IBEAM
 * @see SWT#CURSOR_NO
 * @see SWT#CURSOR_HAND
 */
public Cursor (Device device, int style) {
	super(device);
	int shape = 0;
	switch (style) {
		case SWT.CURSOR_APPSTARTING: break;
		case SWT.CURSOR_ARROW: shape = OS.XC_left_ptr; break;
		case SWT.CURSOR_WAIT: shape = OS.XC_watch; break;
		case SWT.CURSOR_HAND: shape = OS.XC_hand2; break;
		case SWT.CURSOR_CROSS: shape = OS.XC_cross; break;
		case SWT.CURSOR_HELP: shape = OS.XC_question_arrow; break;
		case SWT.CURSOR_SIZEALL: shape = OS.XC_fleur; break;
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
	if (shape == 0 && style == SWT.CURSOR_APPSTARTING) {
		handle = createCursor(APPSTARTING_SRC, APPSTARTING_MASK, 32, 32, 2, 2, true);
	} else {
		handle = OS.XCreateFontCursor(this.device.xDisplay, shape);
	}
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	init();
}
/**	 
 * Constructs a new cursor given a device, image and mask
 * data describing the desired cursor appearance, and the x
 * and y coordinates of the <em>hotspot</em> (that is, the point
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
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the source is null</li>
 *    <li>ERROR_NULL_ARGUMENT - if the mask is null and the source does not have a mask</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the source and the mask are not the same 
 *          size, or if the hotspot is outside the bounds of the image</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a handle could not be obtained for cursor creation</li>
 * </ul>
 */
public Cursor (Device device, ImageData source, ImageData mask, int hotspotX, int hotspotY) {
	super(device);
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
	/* Check the hotspots */
	if (hotspotX >= source.width || hotspotX < 0 ||
		hotspotY >= source.height || hotspotY < 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	/* Convert depth to 1 */
	source = ImageData.convertMask(source);
	mask = ImageData.convertMask(mask);
	byte[] sourceData = new byte[source.data.length];
	byte[] maskData = new byte[mask.data.length];
	/* Swap the bits in each byte and convert to appropriate scanline pad */
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
	sourceData = ImageData.convertPad(sourceData, source.width, source.height, source.depth, source.scanlinePad, 1);
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
	maskData = ImageData.convertPad(maskData, mask.width, mask.height, mask.depth, mask.scanlinePad, 1);
	/* Note that the mask and source are reversed */
	handle = createCursor(maskData, sourceData, source.width, source.height, hotspotX, hotspotY, true);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	init();
}
/**	 
 * Constructs a new cursor given a device, image data describing
 * the desired cursor appearance, and the x and y coordinates of
 * the <em>hotspot</em> (that is, the point within the area
 * covered by the cursor which is considered to be where the
 * on-screen pointer is "pointing").
 * <p>
 * You must dispose the cursor when it is no longer required. 
 * </p>
 *
 * @param device the device on which to allocate the cursor
 * @param source the image data for the cursor
 * @param hotspotX the x coordinate of the cursor's hotspot
 * @param hotspotY the y coordinate of the cursor's hotspot
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the hotspot is outside the bounds of the
 * 		 image</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a handle could not be obtained for cursor creation</li>
 * </ul>
 * 
 * @since 3.0
 */
public Cursor(Device device, ImageData source, int hotspotX, int hotspotY) {
	super(device);
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (hotspotX >= source.width || hotspotX < 0 ||
		hotspotY >= source.height || hotspotY < 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	ImageData mask = source.getTransparencyMask();

	/* Ensure depth is equal to 1 */
	if (source.depth > 1) {
		/* Create a destination image with no data */
		ImageData newSource = new ImageData(
			source.width, source.height, 1, ImageData.bwPalette(),
			1, null, 0, null, null, -1, -1, 0, 0, 0, 0, 0);

		byte[] newReds = new byte[]{0, (byte)255}, newGreens = newReds, newBlues = newReds;

		/* Convert the source to a black and white image of depth 1 */
		PaletteData palette = source.palette;
		if (palette.isDirect) {
			ImageData.blit(ImageData.BLIT_SRC,
					source.data, source.depth, source.bytesPerLine, source.getByteOrder(), 0, 0, source.width, source.height, palette.redMask, palette.greenMask, palette.blueMask,
					ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
					newSource.data, newSource.depth, newSource.bytesPerLine, newSource.getByteOrder(), 0, 0, newSource.width, newSource.height, newReds, newGreens, newBlues,
					false, false);
		} else {
			RGB[] rgbs = palette.getRGBs();
			int length = rgbs.length;
			byte[] srcReds = new byte[length];
			byte[] srcGreens = new byte[length];
			byte[] srcBlues = new byte[length];
			for (int i = 0; i < rgbs.length; i++) {
				RGB rgb = rgbs[i];
				if (rgb == null) continue;
				srcReds[i] = (byte)rgb.red;
				srcGreens[i] = (byte)rgb.green;
				srcBlues[i] = (byte)rgb.blue;
			}
			ImageData.blit(ImageData.BLIT_SRC,
					source.data, source.depth, source.bytesPerLine, source.getByteOrder(), 0, 0, source.width, source.height, srcReds, srcGreens, srcBlues,
					ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
					newSource.data, newSource.depth, newSource.bytesPerLine, newSource.getByteOrder(), 0, 0, newSource.width, newSource.height, newReds, newGreens, newBlues,
					false, false);
		}
		source = newSource;
	}

	/* Swap the bits in each byte and convert to appropriate scanline pad */
	byte[] sourceData = new byte[source.data.length];
	byte[] maskData = new byte[mask.data.length];
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
	}
	sourceData = ImageData.convertPad(sourceData, source.width, source.height, source.depth, source.scanlinePad, 1);
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
	}
	maskData = ImageData.convertPad(maskData, mask.width, mask.height, mask.depth, mask.scanlinePad, 1);
	handle = createCursor(sourceData, maskData, source.width, source.height, hotspotX, hotspotY, false);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	init();
}
int createCursor(byte[] sourceData, byte[] maskData, int width, int height, int hotspotX, int hotspotY, boolean reverse) {
	int xDisplay = device.xDisplay;
	int drawable = OS.XDefaultRootWindow(xDisplay);
	int sourcePixmap = OS.XCreateBitmapFromData(xDisplay, drawable, sourceData, width, height);
	int maskPixmap = OS.XCreateBitmapFromData(xDisplay, drawable, maskData, width, height);
	int cursor = 0;
	if (sourcePixmap != 0 && maskPixmap != 0) {
		int screenNum = OS.XDefaultScreen(xDisplay);
		XColor foreground = new XColor();
		foreground.pixel = !reverse ? OS.XWhitePixel(xDisplay, screenNum) : OS.XBlackPixel(xDisplay, screenNum);
		if (!reverse) foreground.red = foreground.green = foreground.blue = (short)0xFFFF;
		XColor background = new XColor();
		background.pixel = reverse ? OS.XWhitePixel(xDisplay, screenNum) : OS.XBlackPixel(xDisplay, screenNum);
		if (reverse) background.red = background.green = background.blue = (short)0xFFFF;
		cursor = OS.XCreatePixmapCursor(xDisplay, sourcePixmap, maskPixmap, foreground, background, hotspotX, hotspotY);
	}
	if (sourcePixmap != 0) OS.XFreePixmap(xDisplay, sourcePixmap);
	if (maskPixmap != 0) OS.XFreePixmap(xDisplay, maskPixmap);
	return cursor;
}
void destroy() {
	OS.XFreeCursor(device.xDisplay, handle);
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
 * objects that return <code>true</code> when passed to 
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
	Cursor cursor = new Cursor(device);
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

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


import org.eclipse.swt.internal.photon.*;
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
	 * the type to the OS cursor resource
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
	public int type;

	/**
	 * the handle to the OS cursor resource
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
	public int bitmap;

Cursor(Device device) {
	super(device);
}

/**	 
 * Constructs a new cursor given a device and a style
 * constant describing the desired cursor appearance.
 * <p>
 * You must dispose the cursor when it is no longer required. 
 * </p>
 * NOTE:
 * It is recommended to use {@link org.eclipse.swt.widgets.Display#getSystemCursor(int)}
 * instead of using this constructor. This way you can avoid the 
 * overhead of disposing the Cursor resource.
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
public Cursor(Device device, int style) {
	super(device);
	switch (style) {
		case SWT.CURSOR_ARROW: 		type = OS.Ph_CURSOR_POINTER; break;
		case SWT.CURSOR_WAIT: 		type = OS.Ph_CURSOR_CLOCK; break;
		case SWT.CURSOR_HAND: 		type = OS.Ph_CURSOR_FINGER; break;
		case SWT.CURSOR_CROSS: 		type = OS.Ph_CURSOR_CROSSHAIR; break;
		case SWT.CURSOR_APPSTARTING:	type = OS.Ph_CURSOR_POINT_WAIT; break;
		case SWT.CURSOR_HELP: 		type = OS.Ph_CURSOR_QUESTION_POINT; break;
		case SWT.CURSOR_SIZEALL: 	type = OS.Ph_CURSOR_MOVE; break;
		case SWT.CURSOR_SIZENESW: 	type = OS.Ph_CURSOR_MOVE; break;
		case SWT.CURSOR_SIZENS: 	type = OS.Ph_CURSOR_DRAG_VERTICAL; break;
		case SWT.CURSOR_SIZENWSE:	type = OS.Ph_CURSOR_MOVE; break;
		case SWT.CURSOR_SIZEWE: 	type = OS.Ph_CURSOR_DRAG_HORIZONTAL; break;
		case SWT.CURSOR_SIZEN: 		type = OS.Ph_CURSOR_DRAG_TOP; break;
		case SWT.CURSOR_SIZES: 		type = OS.Ph_CURSOR_DRAG_BOTTOM; break;
		case SWT.CURSOR_SIZEE: 		type = OS.Ph_CURSOR_DRAG_RIGHT; break;
		case SWT.CURSOR_SIZEW: 		type = OS.Ph_CURSOR_DRAG_LEFT; break;
		case SWT.CURSOR_SIZENE: 	type = OS.Ph_CURSOR_DRAG_TR; break;
		case SWT.CURSOR_SIZESE: 	type = OS.Ph_CURSOR_DRAG_BR; break;
		case SWT.CURSOR_SIZESW:		type = OS.Ph_CURSOR_DRAG_BL; break;
		case SWT.CURSOR_SIZENW: 	type = OS.Ph_CURSOR_DRAG_TL; break;
		case SWT.CURSOR_UPARROW: 	type = OS.Ph_CURSOR_FINGER; break;
		case SWT.CURSOR_IBEAM: 		type = OS.Ph_CURSOR_INSERT; break;
		case SWT.CURSOR_NO: 		type = OS.Ph_CURSOR_DONT; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (type == 0) SWT.error(SWT.ERROR_NO_HANDLES);
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
public Cursor(Device device, ImageData source, ImageData mask, int hotspotX, int hotspotY) {
	super(device);
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) {
		if (source.getTransparencyType() != SWT.TRANSPARENCY_MASK) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
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
	/* Convert to depth 1 */
	mask = ImageData.convertMask(mask);
	source = ImageData.convertMask(source);
	type = OS.Ph_CURSOR_BITMAP;
	
	short w = (short)source.width;
	short h = (short)source.height;
	ImageData mask1 = new ImageData(w, h, 1, source.palette);
	ImageData mask2 = new ImageData(w, h, 1, mask.palette);
	for (int y=0; y<h; y++) {
		for (int x=0; x<w; x++) {
			int mask1_pixel, src_pixel = source.getPixel(x, y);
			int mask2_pixel, mask_pixel = mask.getPixel(x, y);
			if (src_pixel == 0 && mask_pixel == 0) {
				// BLACK
				mask1_pixel = 0;
				mask2_pixel = 1;
			} else if (src_pixel == 0 && mask_pixel == 1) {
				// WHITE - cursor color
				mask1_pixel = 1;
				mask2_pixel = 0;
			} else if (src_pixel == 1 && mask_pixel == 0) {
				// SCREEN
				mask1_pixel = 0;
				mask2_pixel = 0;
			} else {
				/*
				* Feature in Photon. It is not possible to have
				* the reverse screen case using the Photon support.
				* Reverse screen will be the same as screen.
				*/
				// REVERSE SCREEN -> SCREEN
				mask1_pixel = 0;
				mask2_pixel = 0;
			}
			mask1.setPixel(x, y, mask1_pixel);
			mask2.setPixel(x, y, mask2_pixel);
		}
	}
	
	PhCursorDef_t cursor = new PhCursorDef_t();
	cursor.size1_x = w;
	cursor.size1_y = h;
	cursor.offset1_x = (short)-hotspotX;
	cursor.offset1_y = (short)-hotspotY;
	cursor.bytesperline1 = (byte)mask1.bytesPerLine;
	cursor.color1 = OS.Ph_CURSOR_DEFAULT_COLOR;
	cursor.size2_x = w;
	cursor.size2_y = h;
	cursor.offset2_x = (short)-hotspotX;
	cursor.offset2_y = (short)-hotspotY;
	cursor.bytesperline2 = (byte)mask2.bytesPerLine;
	cursor.color2 = 0x000000;
	int mask1Size = cursor.bytesperline1 * cursor.size1_y;
	int mask2Size = cursor.bytesperline2 * cursor.size2_y;	
	bitmap = OS.malloc(PhCursorDef_t.sizeof + mask1Size + mask2Size);
	if (bitmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.memmove(bitmap, cursor, PhCursorDef_t.sizeof);
	OS.memmove(bitmap + PhCursorDef_t.sizeof, mask1.data, mask1Size);
	OS.memmove(bitmap + PhCursorDef_t.sizeof + mask1Size, mask2.data, mask2Size);
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
	type = OS.Ph_CURSOR_BITMAP;
	
	short w = (short)source.width;
	short h = (short)source.height;
	ImageData mask1 = new ImageData(w, h, 1, source.palette);
	ImageData mask2 = new ImageData(w, h, 1, mask.palette);
	for (int y=0; y<h; y++) {
		for (int x=0; x<w; x++) {
			int mask1_pixel, src_pixel = source.getPixel(x, y);
			int mask2_pixel, mask_pixel = mask.getPixel(x, y);
			if (src_pixel == 0 && mask_pixel == 1) {
				// BLACK
				mask1_pixel = 0;
				mask2_pixel = 1;
			} else if (src_pixel == 1 && mask_pixel == 1) {
				// WHITE - cursor color
				mask1_pixel = 1;
				mask2_pixel = 0;
			} else if (src_pixel == 0 && mask_pixel == 0) {
				// SCREEN
				mask1_pixel = 0;
				mask2_pixel = 0;
			} else {
				/*
				* Feature in Photon. It is not possible to have
				* the reverse screen case using the Photon support.
				* Reverse screen will be the same as screen.
				*/
				// REVERSE SCREEN -> SCREEN
				mask1_pixel = 0;
				mask2_pixel = 0;
			}
			mask1.setPixel(x, y, mask1_pixel);
			mask2.setPixel(x, y, mask2_pixel);
		}
	}
	
	PhCursorDef_t cursor = new PhCursorDef_t();
	cursor.size1_x = w;
	cursor.size1_y = h;
	cursor.offset1_x = (short)-hotspotX;
	cursor.offset1_y = (short)-hotspotY;
	cursor.bytesperline1 = (byte)mask1.bytesPerLine;
	cursor.color1 = OS.Ph_CURSOR_DEFAULT_COLOR;
	cursor.size2_x = w;
	cursor.size2_y = h;
	cursor.offset2_x = (short)-hotspotX;
	cursor.offset2_y = (short)-hotspotY;
	cursor.bytesperline2 = (byte)mask2.bytesPerLine;
	cursor.color2 = 0x000000;
	int mask1Size = cursor.bytesperline1 * cursor.size1_y;
	int mask2Size = cursor.bytesperline2 * cursor.size2_y;	
	bitmap = OS.malloc(PhCursorDef_t.sizeof + mask1Size + mask2Size);
	if (bitmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.memmove(bitmap, cursor, PhCursorDef_t.sizeof);
	OS.memmove(bitmap + PhCursorDef_t.sizeof, mask1.data, mask1Size);
	OS.memmove(bitmap + PhCursorDef_t.sizeof + mask1Size, mask2.data, mask2Size);
	init();
}

void destroy() {
	if (type == OS.Ph_CURSOR_BITMAP && bitmap != 0) {
		OS.free(bitmap);
	}
	type = bitmap = 0;
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
	Cursor cursor = (Cursor) object;
	return device == cursor.device && type == cursor.type &&
		bitmap == cursor.bitmap;
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
	return bitmap ^ type;
}

/**
 * Returns <code>true</code> if the cursor has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the cursor.
 * When a cursor has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the cursor.
 *
 * @return <code>true</code> when the cursor is disposed and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return type == 0;
}

public static Cursor photon_new(Device device, int type, int bitmap) {
	Cursor cursor = new Cursor(device);
	cursor.type = type;
	cursor.bitmap = bitmap;
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
	return "Cursor {" + type + "," + bitmap + "}";
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

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
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public long handle;

Cursor (Device device) {
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
 * @see #dispose()
 */
public Cursor(Device device, int style) {
	super(device);
	String name1 = null, name2 = null;
	switch (style) {
		case SWT.CURSOR_APPSTARTING:	name1 = "left_ptr_watch"; break;
		case SWT.CURSOR_ARROW:			name1 = "left_ptr"; break;
		case SWT.CURSOR_WAIT:			name1 = "watch"; break;
		case SWT.CURSOR_CROSS:			name1 = "crosshair"; break;
		case SWT.CURSOR_HAND:			name1 = "hand2"; break;
		case SWT.CURSOR_HELP:			name1 = "question_arrow"; break;
		case SWT.CURSOR_SIZEALL:		name1 = "fleur"; break;
		case SWT.CURSOR_SIZENESW:		name1 = "size_bdiag"; break;
		case SWT.CURSOR_SIZENS:			name1 = "sb_v_double_arrow"; break;
		case SWT.CURSOR_SIZENWSE:		name1 = "size_fdiag"; break;
		case SWT.CURSOR_SIZEWE:			name1 = "sb_h_double_arrow"; break;
		case SWT.CURSOR_SIZEN:			name1 = "top_side"; break;
		case SWT.CURSOR_SIZES:			name1 = "bottom_side"; break;
		case SWT.CURSOR_SIZEE:			name1 = "right_side"; break;
		case SWT.CURSOR_SIZEW:			name1 = "left_side"; break;
		case SWT.CURSOR_SIZENE:			name1 = "top_right_corner"; break;
		case SWT.CURSOR_SIZESE:			name1 = "bottom_right_corner"; break;
		case SWT.CURSOR_SIZESW:			name1 = "bottom_left_corner"; break;
		case SWT.CURSOR_SIZENW:			name1 = "top_left_corner"; break;
		case SWT.CURSOR_UPARROW:		name1 = "sb_up_arrow"; name2 = "up-arrow"; break;
		case SWT.CURSOR_IBEAM:			name1 = "xterm"; break;
		case SWT.CURSOR_NO:				name1 = "crossed_circle"; name2 = "not-allowed"; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	for (String name : new String[] { name1, name2, "left_ptr", "default" }) {
		if (name == null) continue;
		if (GTK.GTK4) {
			handle = GDK.gdk_cursor_new_from_name (name, 0);
		} else {
			handle = GDK.gdk_cursor_new_from_name (GDK.gdk_display_get_default(), name);
		}
		if (handle != 0) break;
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
 *
 * @see #dispose()
 */
public Cursor(Device device, ImageData source, ImageData mask, int hotspotX, int hotspotY) {
	super(device);
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) {
		if (!(source.getTransparencyType() == SWT.TRANSPARENCY_MASK)) SWT.error(SWT.ERROR_NULL_ARGUMENT);
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
	handle = createCursor(sourceData, maskData, source.width, source.height, hotspotX, hotspotY, true);
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
 * @see #dispose()
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

	long display = GDK.gdk_display_get_default();
	int width = source.width;
	int height = source.height;
	PaletteData palette = source.palette;
	long pixbuf = GDK.gdk_pixbuf_new(GDK.GDK_COLORSPACE_RGB, true, 8, width, height);
	if (pixbuf == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int stride = GDK.gdk_pixbuf_get_rowstride(pixbuf);
	long data = GDK.gdk_pixbuf_get_pixels(pixbuf);
	byte[] buffer = source.data;
	if (!palette.isDirect || source.depth != 24 || stride != source.bytesPerLine || palette.redMask != 0xFF000000 || palette.greenMask != 0xFF0000 || palette.blueMask != 0xFF00) {
		buffer = new byte[source.width * source.height * 4];
		if (palette.isDirect) {
			ImageData.blit(
				source.data, source.depth, source.bytesPerLine, source.getByteOrder(), source.width, source.height, palette.redMask, palette.greenMask, palette.blueMask,
				buffer, 32, source.width * 4, ImageData.MSB_FIRST, source.width, source.height, 0xFF000000, 0xFF0000, 0xFF00,
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
			ImageData.blit(
				source.width, source.height,
				source.data, source.depth, source.bytesPerLine, source.getByteOrder(), srcReds, srcGreens, srcBlues,
				buffer, 32, source.width * 4, ImageData.MSB_FIRST, 0xFF000000, 0xFF0000, 0xFF00);
		}
		if (source.maskData != null || source.transparentPixel != -1) {
			ImageData mask = source.getTransparencyMask();
			byte[] maskData = mask.data;
			int maskBpl = mask.bytesPerLine;
			int offset = 3, maskOffset = 0;
			for (int y = 0; y<source.height; y++) {
				for (int x = 0; x<source.width; x++) {
					buffer[offset] = ((maskData[maskOffset + (x >> 3)]) & (1 << (7 - (x & 0x7)))) != 0 ? (byte)0xff : 0;
					offset += 4;
				}
				maskOffset += maskBpl;
			}
		} else if (source.alpha != -1) {
			byte alpha = (byte)source.alpha;
			for (int i=3; i<buffer.length; i+=4) {
				buffer[i] = alpha;
			}
		} else if (source.alphaData != null) {
			byte[] alphaData = source.alphaData;
			for (int i=3; i<buffer.length; i+=4) {
				buffer[i] = alphaData[i/4];
			}
		} else {
			for (int i=3; i<buffer.length; i+=4) {
				buffer[i] = -1;
			}
		}
	}
	C.memmove(data, buffer, stride * height);

	if (GTK.GTK4) {
		long texture = GDK.gdk_texture_new_for_pixbuf(pixbuf);
		handle = GDK.gdk_cursor_new_from_texture(texture, hotspotX, hotspotY, 0);
		OS.g_object_unref(texture);
	} else {
		handle = GDK.gdk_cursor_new_from_pixbuf(display, pixbuf, hotspotX, hotspotY);
	}
	OS.g_object_unref(pixbuf);

	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	init();
}

long createCursor(byte[] sourceData, byte[] maskData, int width, int height, int hotspotX, int hotspotY, boolean reverse) {
	for (int i = 0; i < sourceData.length; i++) {
		byte s = sourceData[i];
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
	for (int i = 0; i < maskData.length; i++) {
		byte s = maskData[i];
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
	PaletteData palette = new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255));
	ImageData source = new ImageData(width, height, 1, palette, 1, sourceData);
	ImageData mask = new ImageData(width, height, 1, palette, 1, maskData);
	byte[] data = new byte[source.width * source.height * 4];
	for (int y = 0; y < source.height; y++) {
		int offset = y * source.width * 4;
		for (int x = 0; x < source.width; x++) {
			int pixel = source.getPixel(x, y);
			int maskPixel = mask.getPixel(x, y);
			if (pixel == 0 && maskPixel == 0) {
				// BLACK
				data[offset+3] = (byte)0xFF;
			} else if (pixel == 0 && maskPixel == 1) {
				// WHITE - cursor color
				data[offset] = data[offset + 1] = data[offset + 2] = data[offset + 3] = (byte)0xFF;
			} else if (pixel == 1 && maskPixel == 0) {
				// SCREEN
			} else {
				/* no support */
				// REVERSE SCREEN -> SCREEN
			}
			offset += 4;
		}
	}
	long pixbuf = GDK.gdk_pixbuf_new(GDK.GDK_COLORSPACE_RGB, true, 8, width, height);
	if (pixbuf == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int stride = GDK.gdk_pixbuf_get_rowstride(pixbuf);
	long pixels = GDK.gdk_pixbuf_get_pixels(pixbuf);
	C.memmove(pixels, data, stride * height);

	long cursor;
	if (GTK.GTK4) {
		long texture = GDK.gdk_texture_new_for_pixbuf(pixbuf);
		cursor = GDK.gdk_cursor_new_from_texture(texture, hotspotX, hotspotY, 0);
		OS.g_object_unref(texture);
	} else {
		cursor = GDK.gdk_cursor_new_from_pixbuf(GDK.gdk_display_get_default(), pixbuf, hotspotX, hotspotY);
	}
	OS.g_object_unref(pixbuf);

	return cursor;
}

@Override
void destroy() {
	OS.g_object_unref(handle);
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
@Override
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
 * @noreference This method is not intended to be referenced by clients.
 */
public static Cursor gtk_new(Device device, long handle) {
	Cursor cursor = new Cursor(device);
	cursor.handle = handle;
	return cursor;
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
@Override
public int hashCode() {
	return (int)handle;
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
@Override
public boolean isDisposed() {
	return handle == 0;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString () {
	if (isDisposed()) return "Cursor {*DISPOSED*}";
	return "Cursor {" + handle + "}";
}

}

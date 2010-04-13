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


import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.internal.win32.*;

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
	 * 
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public int handle;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Cursor(Device device) {
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
public Cursor(Device device, int style) {
	super(device);
	switch (style) {
		case SWT.CURSOR_HAND: 		handle = OS.Cursors_Hand(); break;
		case SWT.CURSOR_ARROW: 		handle = OS.Cursors_Arrow(); break;
		case SWT.CURSOR_WAIT: 		handle = OS.Cursors_Wait(); break;
		case SWT.CURSOR_CROSS: 		handle = OS.Cursors_Cross(); break;
		case SWT.CURSOR_APPSTARTING: handle = OS.Cursors_AppStarting(); break;
		case SWT.CURSOR_HELP: 		handle = OS.Cursors_Help(); break;
		case SWT.CURSOR_SIZEALL: 	handle = OS.Cursors_SizeAll(); break;
		case SWT.CURSOR_SIZENESW: 	handle = OS.Cursors_SizeNESW(); break;
		case SWT.CURSOR_SIZENS: 	handle = OS.Cursors_SizeNS(); break;
		case SWT.CURSOR_SIZENWSE: 	handle = OS.Cursors_SizeNWSE(); break;
		case SWT.CURSOR_SIZEWE: 	handle = OS.Cursors_SizeWE(); break;
		case SWT.CURSOR_SIZEN: 		handle = OS.Cursors_ScrollN(); break;
		case SWT.CURSOR_SIZES: 		handle = OS.Cursors_ScrollS(); break;
		case SWT.CURSOR_SIZEE: 		handle = OS.Cursors_ScrollE(); break;
		case SWT.CURSOR_SIZEW: 		handle = OS.Cursors_ScrollW(); break;
		case SWT.CURSOR_SIZENE: 	handle = OS.Cursors_ScrollNE(); break;
		case SWT.CURSOR_SIZESE: 	handle = OS.Cursors_ScrollSE(); break;
		case SWT.CURSOR_SIZESW: 	handle = OS.Cursors_ScrollSW(); break;
		case SWT.CURSOR_SIZENW: 	handle = OS.Cursors_ScrollNW(); break;
		case SWT.CURSOR_UPARROW: 	handle = OS.Cursors_UpArrow(); break;
		case SWT.CURSOR_IBEAM: 		handle = OS.Cursors_IBeam(); break;
		case SWT.CURSOR_NO: 		handle = OS.Cursors_No(); break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
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
	/* Convert depth to 1 */
	mask = ImageData.convertMask(mask);
	source = ImageData.convertMask(source);

	/* Make sure source and mask scanline pad is 2 */
	byte[] sourceData = ImageData.convertPad(source.data, source.width, source.height, source.depth, source.scanlinePad, 2);
	byte[] maskData = ImageData.convertPad(mask.data, mask.width, mask.height, mask.depth, mask.scanlinePad, 2);
	
	/* Create the cursor */
	int hInst = Win32.GetModuleHandleW(null);
	int cursor = Win32.CreateCursor(hInst, hotspotX, hotspotY, source.width, source.height, sourceData, maskData);
	if (cursor == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int safeHandle = OS.gcnew_SWTSafeHandle(cursor, false);
	if (safeHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	handle = OS.CursorInteropHelper_Create(safeHandle);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.GCHandle_Free(safeHandle);
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
	/* Check the hotspots */
	if (hotspotX >= source.width || hotspotX < 0 ||
		hotspotY >= source.height || hotspotY < 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	PaletteData palette = source.palette;
	if (!(((source.depth == 1 || source.depth == 2 || source.depth == 4 || source.depth == 8) && !palette.isDirect) ||
		((source.depth == 8) || (source.depth == 16 || source.depth == 24 || source.depth == 32) && palette.isDirect)))
			SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
	int width = source.width;
	int height = source.height;
	int redMask = palette.redMask;
	int greenMask = palette.greenMask;
	int blueMask = palette.blueMask;
	ImageData newData = null;
	int pixelFormat = 0;
	boolean transparent = source.maskData != null || source.transparentPixel != -1 || source.alpha != -1 || source.alphaData != null;
	if (transparent) {
		pixelFormat = Win32.PixelFormat_Format32bppArgb;
		if (!(palette.isDirect && source.depth == 32 && redMask == 0xFF00 && greenMask == 0xFF0000 && blueMask == 0xFF000000)) {
			newData = new ImageData(width, height, 32, new PaletteData(0xFF00, 0xFF0000, 0xFF000000));
		}
	} else {
		switch (source.depth) {
			case 1:
			case 2:
			case 4:
			case 8:
				pixelFormat = Win32.PixelFormat_Format24bppRgb;
				newData = new ImageData(source.width, source.height, 24, new PaletteData(0xFF, 0xFF00, 0xFF0000));
				break;
			case 16:
				if (redMask == 0x7C00 && greenMask == 0x3E0 && blueMask == 0x1F) {
					pixelFormat = Win32.PixelFormat_Format16bppRgb555;
				} else if (redMask == 0xF800 && greenMask == 0x7E0 && blueMask == 0x1F) {
					pixelFormat = Win32.PixelFormat_Format16bppRgb565;
				} else {
					pixelFormat = Win32.PixelFormat_Format16bppRgb555;
					newData = new ImageData(source.width, source.height, 16, new PaletteData(0x7C00, 0x3E0, 0x1F));
				}
				break;
			case 24:
				if (redMask == 0xFF && greenMask == 0xFF00 && blueMask == 0xFF0000) {
					pixelFormat = Win32.PixelFormat_Format24bppRgb;
				} else {
					pixelFormat = Win32.PixelFormat_Format24bppRgb;
					newData = new ImageData(source.width, source.height, 24, new PaletteData(0xFF, 0xFF00, 0xFF0000));
				}
				break;
			case 32:
				if (redMask == 0xFF00 && greenMask == 0xFF0000 && blueMask == 0xFF000000) {
					pixelFormat = Win32.PixelFormat_Format32bppRgb;
				} else {
					pixelFormat = Win32.PixelFormat_Format32bppRgb;
					newData = new ImageData(source.width, source.height, 32, new PaletteData(0xFF00, 0xFF0000, 0xFF000000));
				}
				break;
		}
	}
	if (newData != null) {
		PaletteData newPalette = newData.palette;
		if (palette.isDirect) {
			ImageData.blit(ImageData.BLIT_SRC, 
					source.data, source.depth, source.bytesPerLine, source.getByteOrder(), 0, 0, width, height, redMask, greenMask, blueMask,
					ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
					newData.data, newData.depth, newData.bytesPerLine, newData.getByteOrder(), 0, 0, width, height, newPalette.redMask, newPalette.greenMask, newPalette.blueMask,
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
				source.data, source.depth, source.bytesPerLine, source.getByteOrder(), 0, 0, width, height, srcReds, srcGreens, srcBlues,
				ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
				newData.data, newData.depth, newData.bytesPerLine, newData.getByteOrder(), 0, 0, width, height, newPalette.redMask, newPalette.greenMask, newPalette.blueMask,
				false, false);
		}
		if (source.transparentPixel != -1) {
			newData.transparentPixel = newPalette.getPixel(palette.getRGB(source.transparentPixel));
		}
		newData.maskPad = source.maskPad;
		newData.maskData = source.maskData;
		newData.alpha = source.alpha;
		newData.alphaData = source.alphaData;
		source = newData;
		palette = source.palette;
	}
	if (transparent) {
		if (source.maskData != null || source.transparentPixel != -1) {
			ImageData maskImage = source.getTransparencyMask();
			byte[] maskData = maskImage.data;
			int maskBpl = maskImage.bytesPerLine;
			int offset = 3, maskOffset = 0;
			for (int y = 0; y<height; y++) {
				for (int x = 0; x<width; x++) {
					source.data[offset] = ((maskData[maskOffset + (x >> 3)]) & (1 << (7 - (x & 0x7)))) != 0 ? (byte)0xff : 0;
					offset += 4;
				}
				maskOffset += maskBpl;
			}
		} else if (source.alpha != -1) {
			byte alpha = (byte)source.alpha;
			for (int i = 3, j = 0; i < source.data.length; i+=4, j++) {
				source.data[i] = alpha;
			}
		} else {
			for (int i = 3, j = 0; i < source.data.length; i+=4, j++) {
				source.data[i] = source.alphaData[j];
			}
		}
	}
	int bitmap = OS.gcnew_Bitmap(source.width, source.height, source.bytesPerLine, pixelFormat, source.data);
	if (bitmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int hIcon = OS.Bitmap_GetHicon(bitmap);
	if (hIcon == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	ICONINFO info = new ICONINFO(); 
	Win32.GetIconInfo(hIcon, info);
	info.fIcon = false;
	info.xHotspot = hotspotX;
	info.yHotspot = hotspotY;
	Win32.DestroyIcon(hIcon);
	hIcon = Win32.CreateIconIndirect(info);
	if (info.hbmColor != 0) Win32.DeleteObject(info.hbmColor);
	if (info.hbmMask != 0)Win32.DeleteObject(info.hbmMask);
	if (hIcon == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	
	/* Create the cursor */
	int safeHandle = OS.gcnew_SWTSafeHandle(hIcon, true);
	if (safeHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	handle = OS.CursorInteropHelper_Create(safeHandle);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.GCHandle_Free(safeHandle);
	OS.GCHandle_Free(bitmap);
	init();
}

void destroy() {
	OS.GCHandle_Free(handle);
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
	Cursor cursor = (Cursor) object;
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
 * @return a new cursor object containing the specified device and handle
 * 
 * @noreference This method is not intended to be referenced by clients.
 */
public static Cursor wpf_new(Device device, int handle) {
	Cursor cursor = new Cursor(device);
	cursor.handle = handle;
	return cursor;
}

}

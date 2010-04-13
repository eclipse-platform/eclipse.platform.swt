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


import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.internal.cocoa.*;
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

	static boolean initialized;

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
		case SWT.CURSOR_HAND: 			handle = OS.kThemePointingHandCursor; break;
		case SWT.CURSOR_ARROW: 		handle = OS.kThemeArrowCursor; break;
		case SWT.CURSOR_WAIT: 			handle = OS.kThemeSpinningCursor; break;
		case SWT.CURSOR_CROSS: 		handle = OS.kThemeCrossCursor; break;
		case SWT.CURSOR_APPSTARTING: 	handle = OS.kThemeArrowCursor; break;
		case SWT.CURSOR_HELP: 			handle = OS.kThemeCrossCursor; break;
		case SWT.CURSOR_SIZEALL: 		handle = OS.kThemeCrossCursor; break;
		case SWT.CURSOR_SIZENESW: 		handle = OS.kThemeCrossCursor; break;
		case SWT.CURSOR_SIZENS:			handle = OS.kThemeResizeUpDownCursor; break;
		case SWT.CURSOR_SIZENWSE: 		handle = OS.kThemeCrossCursor; break;
		case SWT.CURSOR_SIZEWE: 		handle = OS.kThemeResizeLeftRightCursor; break;
		case SWT.CURSOR_SIZEN: 		handle = OS.kThemeResizeUpCursor; break;
		case SWT.CURSOR_SIZES: 		handle = OS.kThemeResizeDownCursor; break;
		case SWT.CURSOR_SIZEE: 		handle = OS.kThemeResizeRightCursor; break;
		case SWT.CURSOR_SIZEW: 		handle = OS.kThemeResizeLeftCursor; break;
		case SWT.CURSOR_SIZENE: 		handle = OS.kThemeCrossCursor; break;
		case SWT.CURSOR_SIZESE: 		handle = OS.kThemeCrossCursor; break;
		case SWT.CURSOR_SIZESW: 		handle = OS.kThemeCrossCursor; break;
		case SWT.CURSOR_SIZENW: 		handle = OS.kThemeCrossCursor; break;
		case SWT.CURSOR_UPARROW: 		handle = OS.kThemeCrossCursor; break;
		case SWT.CURSOR_IBEAM: 		handle = OS.kThemeIBeamCursor; break;
		case SWT.CURSOR_NO: 			handle = OS.kThemeNotAllowedCursor; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
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
	if (OS.VERSION >= 0x1040) {
		byte[] data = new byte[source.width * source.height * 4];
		for (int y = 0; y < source.height; y++) {
			int offset = y * source.width * 4;
			for (int x = 0; x < source.width; x++) {
				int pixel = source.getPixel(x, y);
				int maskPixel = mask.getPixel(x, y);
				if (pixel == 0 && maskPixel == 0) {
					// BLACK
					data[offset] = (byte)0xFF;
				} else if (pixel == 0 && maskPixel == 1) {
					// WHITE - cursor color
					data[offset] = data[offset + 1] = data[offset + 2] = data[offset + 3] = (byte)0xFF;
				} else if (pixel == 1 && maskPixel == 0) {
					// SCREEN
				} else {
					/*
					* Feature in the Macintosh. It is not possible to have
					* the reverse screen case using NSCursor.
					* Reverse screen will be the same as screen.
					*/
					// REVERSE SCREEN -> SCREEN
				}
				offset += 4;
			}
		}
		createNSCursor(hotspotX, hotspotY, data, source.width, source.height);
		return;
	}
	/* Convert depth to 1 */
	mask = ImageData.convertMask(mask);
	source = ImageData.convertMask(source);

	/* Find the first non transparent pixel if cursor bigger than 16x16. */
	int width = source.width;
	int height = source.height;
	int minX = 0, minY = 0;
	if (width > 16 || height > 16) {
		minX = width;
		minY = height;
		int maxX = 0, maxY = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (!(source.getPixel(x, y) == 1 && mask.getPixel(x, y) == 0)) {
					minX = Math.min(minX, x);
					minY = Math.min(minY, y);
					maxX = Math.max(maxX, x);
					maxY = Math.max(maxY, y);
				}
			}
		}
		width = maxX - minX + 1;
		height = maxY - minY + 1;
		
		/* Stretch cursor if still bigger than 16x16. */
		if (width > 16 || height > 16) {
			int newWidth = Math.min(width, 16);
			int newHeight = Math.min(height, 16);
			ImageData newSource =
				new ImageData(newWidth, newHeight, source.depth, source.palette,
					1, null, 0, null, null, -1, -1, source.type,
					source.x, source.y, source.disposalMethod, source.delayTime);
			ImageData newMask = new ImageData(newWidth, newHeight, mask.depth,
					mask.palette, 1, null, 0, null, null, -1, -1, mask.type,
					mask.x, mask.y, mask.disposalMethod, mask.delayTime);
			ImageData.blit(ImageData.BLIT_SRC,
				source.data, source.depth, source.bytesPerLine, source.getByteOrder(), minX, minY, width, height, null, null, null,
				ImageData.ALPHA_OPAQUE, null, 0, minX, minY,
				newSource.data, newSource.depth, newSource.bytesPerLine, newSource.getByteOrder(), 0, 0, newWidth, newHeight, null, null, null,
				false, false);
			ImageData.blit(ImageData.BLIT_SRC,
				mask.data, mask.depth, mask.bytesPerLine, mask.getByteOrder(), minX, minY, width, height, null, null, null,
				ImageData.ALPHA_OPAQUE, null, 0, minX, minY,
				newMask.data, newMask.depth, newMask.bytesPerLine, newMask.getByteOrder(), 0, 0, newWidth, newHeight, null, null, null,
				false, false);
			width = newWidth;
			height = newHeight;
			minX = minY = 0;
			source = newSource;
			mask = newMask;
		}
	}

	/* Create the cursor */
	org.eclipse.swt.internal.carbon.Cursor cursor = new org.eclipse.swt.internal.carbon.Cursor();
	byte[] srcData = cursor.data;
	byte[] maskData = cursor.mask;
	for (int y = 0; y < height; y++) {
		short d = 0, m = 0;
		for (int x = 0; x < width; x++) {
			int bit = 1 << (width - 1 - x);
			if (source.getPixel(minX + x, minY + y) == 0) {
				m |= bit;
				if (mask.getPixel(minX + x, minY + y) == 0) d |= bit;
			} else if (mask.getPixel(minX + x, minY + y) != 0) {
				d |= bit;
			}
		}
		srcData[y * 2] = (byte)(d >> 8);
		srcData[y * 2 + 1] = (byte)(d & 0xFF);
		maskData[y * 2] = (byte)(m >> 8);
		maskData[y * 2 + 1] = (byte)(m & 0xFF);
	}
	cursor.hotSpot_h = (short)Math.max(0, Math.min(15, hotspotX - minX));
	cursor.hotSpot_v = (short)Math.max(0, Math.min(15, hotspotY - minY));
	handle = OS.NewPtr(org.eclipse.swt.internal.carbon.Cursor.sizeof);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.memmove(handle, cursor, org.eclipse.swt.internal.carbon.Cursor.sizeof);
	init();
}

void createNSCursor(int hotspotX, int hotspotY, byte[] buffer, int width, int height) {
	if (!initialized) {
		initialized = true;
		int window = Cocoa.objc_msgSend(Cocoa.objc_msgSend(Cocoa.C_NSWindow, Cocoa.S_alloc), Cocoa.S_init);
		Cocoa.objc_msgSend(window, Cocoa.S_release);
	}
	int nsImage = Cocoa.objc_msgSend(Cocoa.C_NSImage, Cocoa.S_alloc);
	if (nsImage == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int nsImageRep = Cocoa.objc_msgSend(Cocoa.C_NSBitmapImageRep, Cocoa.S_alloc);
	if (nsImageRep == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	this.handle = Cocoa.objc_msgSend(Cocoa.C_NSCursor, Cocoa.S_alloc);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	NSSize size = new NSSize();
	size.width = width;
	size.height =  height;
	nsImage = Cocoa.objc_msgSend(nsImage, Cocoa.S_initWithSize, size);
	nsImageRep = Cocoa.objc_msgSend(nsImageRep, Cocoa.S_initWithBitmapDataPlanes, null, width, height,
			8, 4, 1, 0, Cocoa.NSDeviceRGBColorSpace(),
			Cocoa.NSAlphaFirstBitmapFormat | Cocoa.NSAlphaNonpremultipliedBitmapFormat, width * 4, 32);
	int bitmapData = Cocoa.objc_msgSend(nsImageRep, Cocoa.S_bitmapData);
	OS.memmove(bitmapData, buffer, buffer.length);
	Cocoa.objc_msgSend(nsImage, Cocoa.S_addRepresentation, nsImageRep);
	NSPoint point = new NSPoint();
	point.x = hotspotX;
	point.y = hotspotY;
	handle = Cocoa.objc_msgSend(handle, Cocoa.S_initWithImage_hotSpot, nsImage, point);
	Cocoa.objc_msgSend(nsImage, Cocoa.S_release);
	Cocoa.objc_msgSend(nsImageRep, Cocoa.S_release);
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

	if (OS.VERSION >= 0x1040) {
		byte[] data = new byte[source.width * source.height * 4];
		PaletteData palette = source.palette;
		if (palette.isDirect) {
			ImageData.blit(ImageData.BLIT_SRC,
				source.data, source.depth, source.bytesPerLine, source.getByteOrder(), 0, 0, source.width, source.height, palette.redMask, palette.greenMask, palette.blueMask,
				ImageData.ALPHA_OPAQUE, null, 0, 0, 0, 
				data, 32, source.width * 4, ImageData.MSB_FIRST, 0, 0, source.width, source.height, 0xFF0000, 0xFF00, 0xFF,
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
				data, 32, source.width * 4, ImageData.MSB_FIRST, 0, 0, source.width, source.height, 0xFF0000, 0xFF00, 0xFF,
				false, false);
		}
		if (source.maskData != null || source.transparentPixel != -1) {
			ImageData mask = source.getTransparencyMask();
			byte[] maskData = mask.data;
			int maskBpl = mask.bytesPerLine;
			int offset = 0, maskOffset = 0;
			for (int y = 0; y<source.height; y++) {
				for (int x = 0; x<source.width; x++) {
					data[offset] = ((maskData[maskOffset + (x >> 3)]) & (1 << (7 - (x & 0x7)))) != 0 ? (byte)0xff : 0;
					offset += 4;
				}
				maskOffset += maskBpl;
			}
		} else if (source.alpha != -1) {
			byte alpha = (byte)source.alpha;
			for (int i=0; i<data.length; i+=4) {
				data[i] = alpha;				
			}
		} else if (source.alphaData != null) {
			byte[] alphaData = source.alphaData;
			for (int i=0; i<data.length; i+=4) {
				data[i] = alphaData[i/4];
			}
		}
		createNSCursor(hotspotX, hotspotY, data, source.width, source.height);
	} else {
	
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
	
		/* Find the first non transparent pixel if cursor bigger than 16x16. */
		int width = source.width;
		int height = source.height;
		int minX = 0, minY = 0;
		if (width > 16 || height > 16) {
			minX = width;
			minY = height;
			int maxX = 0, maxY = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (!(source.getPixel(x, y) == 1 && mask.getPixel(x, y) == 0)) {
						minX = Math.min(minX, x);
						minY = Math.min(minY, y);
						maxX = Math.max(maxX, x);
						maxY = Math.max(maxY, y);
					}
				}
			}
			width = maxX - minX + 1;
			height = maxY - minY + 1;
			
			/* Stretch cursor if still bigger than 16x16. */
			if (width > 16 || height > 16) {
				int newWidth = Math.min(width, 16);
				int newHeight = Math.min(height, 16);
				ImageData newSource =
					new ImageData(newWidth, newHeight, source.depth, source.palette,
						1, null, 0, null, null, -1, -1, source.type,
						source.x, source.y, source.disposalMethod, source.delayTime);
				ImageData newMask = new ImageData(newWidth, newHeight, mask.depth,
						mask.palette, 1, null, 0, null, null, -1, -1, mask.type,
						mask.x, mask.y, mask.disposalMethod, mask.delayTime);
				ImageData.blit(ImageData.BLIT_SRC,
					source.data, source.depth, source.bytesPerLine, source.getByteOrder(), minX, minY, width, height, null, null, null,
					ImageData.ALPHA_OPAQUE, null, 0, minX, minY,
					newSource.data, newSource.depth, newSource.bytesPerLine, newSource.getByteOrder(), 0, 0, newWidth, newHeight, null, null, null,
					false, false);
				ImageData.blit(ImageData.BLIT_SRC,
					mask.data, mask.depth, mask.bytesPerLine, mask.getByteOrder(), minX, minY, width, height, null, null, null,
					ImageData.ALPHA_OPAQUE, null, 0, minX, minY,
					newMask.data, newMask.depth, newMask.bytesPerLine, newMask.getByteOrder(), 0, 0, newWidth, newHeight, null, null, null,
					false, false);
				width = newWidth;
				height = newHeight;
				minX = minY = 0;
				source = newSource;
				mask = newMask;
			}
		}
	
		/* Create the cursor */
		org.eclipse.swt.internal.carbon.Cursor cursor = new org.eclipse.swt.internal.carbon.Cursor();
		byte[] srcData = cursor.data;
		byte[] maskData = cursor.mask;
		for (int y= 0; y < height; y++) {
			short d = 0, m = 0;
			for (int x = 0; x < width; x++) {
				int bit = 1 << (width - 1 - x);			
				if (source.getPixel(x + minX, y + minY) == 0) {
					if (mask.getPixel(x + minX, y + minY) != 0) {
						d |= bit;
						m |= bit;
					}
				} else {
					if (mask.getPixel(x + minX, y + minY) != 0) m |= bit;
				}
			}
			srcData[y * 2] = (byte)(d >> 8);
			srcData[y * 2 + 1] = (byte)(d & 0xFF);
			maskData[y * 2] = (byte)(m >> 8);
			maskData[y * 2 + 1] = (byte)(m & 0xFF);
		}
		cursor.hotSpot_h = (short)Math.max(0, Math.min(15, hotspotX - minX));
		cursor.hotSpot_v = (short)Math.max(0, Math.min(15, hotspotY - minY));
		handle = OS.NewPtr(org.eclipse.swt.internal.carbon.Cursor.sizeof);
		if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.memmove(handle, cursor, org.eclipse.swt.internal.carbon.Cursor.sizeof);
	}
	init();
}

void destroy() {
	switch (handle) {
		case OS.kThemePointingHandCursor:
		case OS.kThemeArrowCursor:
		case OS.kThemeSpinningCursor:
		case OS.kThemeCrossCursor:
		case OS.kThemeWatchCursor:
		case OS.kThemeIBeamCursor:
		case OS.kThemeNotAllowedCursor:
		case OS.kThemeResizeLeftRightCursor:
		case OS.kThemeResizeLeftCursor:
		case OS.kThemeResizeRightCursor:
		case OS.kThemeResizeUpDownCursor:
		case OS.kThemeResizeUpCursor:
		case OS.kThemeResizeDownCursor:
			break;
		default:
			if (OS.VERSION >= 0x1040) {
				Cocoa.objc_msgSend(handle, Cocoa.S_release);
			} else {
				OS.DisposePtr(handle);
			}
	}
	handle = -1;
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
	return handle == -1;
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
 * 
 * @noreference This method is not intended to be referenced by clients.
 */
public static Cursor carbon_new(Device device, int handle) {
	Cursor cursor = new Cursor(device);
	cursor.handle = handle;
	return cursor;
}

}

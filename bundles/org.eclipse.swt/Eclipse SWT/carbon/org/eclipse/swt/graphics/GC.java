/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.*;

/**
 * Class <code>GC</code> is where all of the drawing capabilities that are 
 * supported by SWT are located. Instances are used to draw on either an 
 * <code>Image</code>, a <code>Control</code>, or directly on a <code>Display</code>.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * </dl>
 * 
 * <p>
 * The SWT drawing coordinate system is the two-dimensional space with the origin
 * (0,0) at the top left corner of the drawing area and with (x,y) values increasing
 * to the right and downward respectively.
 * </p>
 * 
 * <p>
 * Application code must explicitly invoke the <code>GC.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required. This is <em>particularly</em>
 * important on Windows95 and Windows98 where the operating system has a limited
 * number of device contexts available.
 * </p>
 * 
 * <p>
 * Note: Only one of LEFT_TO_RIGHT and RIGHT_TO_LEFT may be specified.
 * </p>
 *
 * @see org.eclipse.swt.events.PaintEvent
 */
public final class GC {
	/**
	 * the handle to the OS device context
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int handle;
	
	Drawable drawable;
	GCData data;

	static final int TAB_COUNT = 32;

GC() {
}

/**	 
 * Constructs a new instance of this class which has been
 * configured to draw on the specified drawable. Sets the
 * foreground and background color in the GC to match those
 * in the drawable.
 * <p>
 * You must dispose the graphics context when it is no longer required. 
 * </p>
 * @param drawable the drawable to draw on
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
 *    <li>ERROR_NULL_ARGUMENT - if there is no current device</li>
 *    <li>ERROR_INVALID_ARGUMENT
 *          - if the drawable is an image that is not a bitmap or an icon
 *          - if the drawable is an image or printer that is already selected
 *            into another graphics context</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for gc creation</li>
 * </ul>
 */
public GC(Drawable drawable) {
	this(drawable, 0);
}

/**	 
 * Constructs a new instance of this class which has been
 * configured to draw on the specified drawable. Sets the
 * foreground and background color in the GC to match those
 * in the drawable.
 * <p>
 * You must dispose the graphics context when it is no longer required. 
 * </p>
 * 
 * @param drawable the drawable to draw on
 * @param style the style of GC to construct
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
 *    <li>ERROR_NULL_ARGUMENT - if there is no current device</li>
 *    <li>ERROR_INVALID_ARGUMENT
 *          - if the drawable is an image that is not a bitmap or an icon
 *          - if the drawable is an image or printer that is already selected
 *            into another graphics context</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for gc creation</li>
 * </ul>
 *  
 * @since 2.1.2
 */
public GC(Drawable drawable, int style) {
	if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	GCData data = new GCData();
	data.style = checkStyle(style);
	int gdkGC = drawable.internal_new_GC(data);
	Device device = data.device;
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	data.device = device;
	init(drawable, data, gdkGC);
}

static int checkStyle (int style) {
	if ((style & SWT.LEFT_TO_RIGHT) != 0) style &= ~SWT.RIGHT_TO_LEFT;
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

/**	 
 * Invokes platform specific functionality to allocate a new graphics context.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>GC</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param drawable the Drawable for the receiver.
 * @param data the data for the receiver.
 *
 * @return a new <code>GC</code>
 *
 * @private
 */
public static GC carbon_new(Drawable drawable, GCData data) {
	GC gc = new GC();
	int context = drawable.internal_new_GC(data);
	gc.init(drawable, data, context);
	return gc;
}

/**
 * Copies a rectangular area of the receiver at the specified
 * position into the image, which must be of type <code>SWT.BITMAP</code>.
 *
 * @param image the image to copy into
 * @param x the x coordinate in the receiver of the area to be copied
 * @param y the y coordinate in the receiver of the area to be copied
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image is not a bitmap or has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void copyArea(Image image, int x, int y) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.type != SWT.BITMAP || image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (data.control != 0) {
		int[] offscreen = new int[1];
		OS.HIViewCreateOffscreenImage(data.control, 0, null, offscreen);
		copyArea(image, x, y, offscreen[0]);
		if (offscreen[0] != 0) OS.CGImageRelease(offscreen[0]);
	} else if (data.image != null) {
		copyArea(image, x, y, data.image.handle);
	} else if (data.window != 0) {
		int imageHandle = image.handle;
		CGRect rect = new CGRect();
		rect.x = x;
		rect.y = y;
		rect.width = OS.CGImageGetWidth(imageHandle);
		rect.height = OS.CGImageGetHeight(imageHandle);
		int[] displays = new int[16];
		int[] count = new int[1];
		if (OS.CGGetDisplaysWithRect(rect, displays.length, displays, count) != 0) return;
		for (int i = 0; i < count[0]; i++) {
			int display = displays[i];
			int address = OS.CGDisplayBaseAddress(display);
			if (address != 0) {
				int width = OS.CGDisplayPixelsWide(display);
				int height = OS.CGDisplayPixelsHigh(display);
				int bpr = OS.CGDisplayBytesPerRow(display);
				int bpp = OS.CGDisplayBitsPerPixel(display);
				int bps = OS.CGDisplayBitsPerSample(display);
				int provider = OS.CGDataProviderCreateWithData(0, address, bpr * height, 0);
				int srcImage = OS.CGImageCreate(width, height, bps, bpp, bpr, data.device.colorspace, OS.kCGImageAlphaNoneSkipFirst, provider, null, false, 0);
				OS.CGDataProviderRelease(provider);
				copyArea(image, x, y, srcImage);
				if (srcImage != 0) OS.CGImageRelease(srcImage);
			}
		}
	}	
}

void copyArea (Image image, int x, int y, int srcImage) {
	if (srcImage == 0) return;
	int imageHandle = image.handle;
	int bpc = OS.CGImageGetBitsPerComponent(imageHandle);
	int width = OS.CGImageGetWidth(imageHandle);
	int height = OS.CGImageGetHeight(imageHandle);
	int bpr = OS.CGImageGetBytesPerRow(imageHandle);
	int alphaInfo = OS.CGImageGetAlphaInfo(imageHandle);
	int context = OS.CGBitmapContextCreate(image.data, width, height, bpc, bpr, data.device.colorspace, alphaInfo);
	if (context != 0) {
	 	CGRect rect = new CGRect();
	 	rect.x = -x;
	 	rect.y = y;
	 	rect.width = OS.CGImageGetWidth(srcImage);
		rect.height = OS.CGImageGetHeight(srcImage);
		OS.CGContextTranslateCTM(context, 0, -(rect.height - height));
		OS.CGContextDrawImage(context, rect, srcImage);
		OS.CGContextRelease(context);
	}
}

/**
 * Copies a rectangular area of the receiver at the source
 * position onto the receiver at the destination position.
 *
 * @param srcX the x coordinate in the receiver of the area to be copied
 * @param srcY the y coordinate in the receiver of the area to be copied
 * @param width the width of the area to copy
 * @param height the height of the area to copy
 * @param destX the x coordinate in the receiver of the area to copy to
 * @param destY the y coordinate in the receiver of the area to copy to
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.updateClip) setCGClipping();
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - srcX, deltaY = destY - srcY;
	if (deltaX == 0 && deltaY == 0) return;
	if (data.image != null) {
 		OS.CGContextSaveGState(handle);
 		OS.CGContextScaleCTM(handle, 1, -1);
 		OS.CGContextTranslateCTM(handle, 0, -(height + 2 * destY));
 		CGRect rect = new CGRect();
 		rect.x = destX;
 		rect.y = destY;
 		rect.width = width;
		rect.height = height;
		//NOT DONE - transparency
 		OS.CGContextDrawImage(handle, rect, data.image.handle);
 		OS.CGContextRestoreGState(handle);
 		return;
	}
	if (data.control != 0) {
		int port = data.port;
		int window = OS.GetControlOwner(data.control);
		if (port == 0) port = OS.GetWindowPort(window);

		/* Calculate src and dest rectangles/regions */
		Rect rect = new Rect();
		OS.GetControlBounds(data.control, rect);		
		Rect srcRect = new Rect();
		int left = rect.left + srcX;
		int top = rect.top + srcY;
		OS.SetRect(srcRect, (short)left, (short)top, (short)(left + width), (short)(top + height));
		int srcRgn = OS.NewRgn();
		OS.RectRgn(srcRgn, srcRect);		
		OS.SectRect(rect, srcRect, srcRect);
		Rect destRect = new Rect ();
		OS.SetRect(destRect, srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		OS.OffsetRect(destRect, (short)deltaX, (short)deltaY);
		int destRgn = OS.NewRgn();
		OS.RectRgn(destRgn, destRect);
		
		/* Copy bits with appropriated clipping region */
		if (!OS.EmptyRect(srcRect)) {
			if (data.visibleRgn == 0 || OS.RectInRgn(srcRect, data.visibleRgn)) {
				int clipRgn = data.visibleRgn;
				if (data.clipRgn != 0) {
					clipRgn = OS.NewRgn();
					OS.SectRgn(data.clipRgn, data.visibleRgn, clipRgn);
				}
	
				/*
				* Feature in the Macintosh.  ScrollRect() only copies bits
				* that are inside the specified rectangle.  This means that
				* it is not possible to copy non overlaping bits without
				* copying the bits in between the source and destination
				* rectangles.  The fix is to check if the source and
				* destination rectangles are disjoint and use CopyBits()
				* instead.
				*/
				if (!OS.EmptyRgn(clipRgn)) {
					boolean disjoint = (destX + width < srcX) || (srcX + width < destX) || (destY + height < srcY) || (srcY + height < destY);
					if (!disjoint && (deltaX == 0 || deltaY == 0)) {
						int[] currentPort = new int[1];
						OS.GetPort(currentPort);
						OS.SetPort(port);
						int oldClip = OS.NewRgn();
						OS.GetClip(oldClip);
						OS.SetClip(clipRgn);
						OS.UnionRect(srcRect, destRect, rect);
						OS.ScrollRect(rect, (short)deltaX, (short)deltaY, 0);
						OS.SetClip(oldClip);
						OS.DisposeRgn(oldClip);
						OS.SetPort(currentPort[0]);
					} else {
						int portBitMap = OS.GetPortBitMapForCopyBits (port);
						OS.CopyBits(portBitMap, portBitMap, srcRect, destRect, (short)OS.srcCopy, clipRgn);
						OS.QDFlushPortBuffer(port, destRgn);
					}
				}
				
				if (clipRgn != data.visibleRgn) OS.DisposeRgn(clipRgn);
			}
		}
		
		/* Invalidate src and obscured areas */
		int invalRgn = OS.NewRgn();
		OS.DiffRgn(srcRgn, data.visibleRgn, invalRgn);
		OS.OffsetRgn(invalRgn, (short)deltaX, (short)deltaY);
		OS.DiffRgn(srcRgn, destRgn, srcRgn);
		OS.UnionRgn(srcRgn, invalRgn, invalRgn);
		OS.SectRgn(data.visibleRgn, invalRgn, invalRgn);
		OS.InvalWindowRgn(window, invalRgn);
		OS.DisposeRgn(invalRgn);
		
		/* Dispose src and dest regions */
		OS.DisposeRgn(destRgn);
		OS.DisposeRgn(srcRgn);
	}
}

void createLayout () {
	int[] buffer = new int[1];
	OS.ATSUCreateTextLayout(buffer);
	if (buffer[0] == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	data.layout = buffer[0];
	int ptr1 = OS.NewPtr(4);
	buffer[0] = handle;
	OS.memcpy(ptr1, buffer, 4);	
	int ptr2 = OS.NewPtr(4);
	buffer[0] = OS.kATSLineUseDeviceMetrics;
	OS.memcpy(ptr2, buffer, 4);
	int lineDir = OS.kATSULeftToRightBaseDirection;
	if ((data.style & SWT.RIGHT_TO_LEFT) != 0) lineDir = OS.kATSURightToLeftBaseDirection;
	int ptr3 = OS.NewPtr(1);
	OS.memcpy(ptr3, new byte[] {(byte)lineDir}, 1);
	int[] tags = new int[]{OS.kATSUCGContextTag, OS.kATSULineLayoutOptionsTag, OS.kATSULineDirectionTag};
	int[] sizes = new int[]{4, 4, 1};
	int[] values = new int[]{ptr1, ptr2, ptr3};
	OS.ATSUSetLayoutControls(data.layout, tags.length, tags, sizes, values);
	OS.DisposePtr(ptr1);
	OS.DisposePtr(ptr2);
	OS.DisposePtr(ptr3);
}

void createTabs () {
	ATSUTab tabs = new ATSUTab();
	int tabWidth = getCharWidth(' ') * 8;
	int ptr = OS.NewPtr(ATSUTab.sizeof * TAB_COUNT);
	for (int i=0, offset=ptr; i<TAB_COUNT; i++, offset += ATSUTab.sizeof) {
		tabs.tabPosition += OS.Long2Fix(tabWidth);
		OS.memcpy(offset, tabs, ATSUTab.sizeof);
	}
	data.tabs = ptr;
}

/**
 * Disposes of the operating system resources associated with
 * the graphics context. Applications must dispose of all GCs
 * which they allocate.
 */
public void dispose() {
	if (handle == 0) return;
	if (data.device.isDisposed()) return;

	/* Free resources */
	int clipRgn = data.clipRgn;
	if (clipRgn != 0) OS.DisposeRgn(clipRgn);
	Image image = data.image;
	if (image != null) {
		image.memGC = null;
		if (image.transparentPixel != -1) image.createMask();
	}
	int layout = data.layout;
	if (layout != 0) OS.ATSUDisposeTextLayout(layout);
	int atsuiStyle = data.atsuiStyle;
	if (atsuiStyle != 0) OS.ATSUDisposeStyle(atsuiStyle);
	int stringPtr = data.stringPtr;
	if (stringPtr != 0) OS.DisposePtr(stringPtr);
	int tabs = data.tabs;
	if (tabs != 0) OS.DisposePtr(tabs);
	
	/* Dispose the GC */
	drawable.internal_dispose_GC(handle, data);

	data.clipRgn = data.atsuiStyle = data.stringPtr = data.layout = data.tabs = 0;
	drawable = null;
	data.image = null;
	data.string = null;
	data = null;
	handle = 0;
}

/**
 * Draws the outline of a circular or elliptical arc 
 * within the specified rectangular area.
 * <p>
 * The resulting arc begins at <code>startAngle</code> and extends  
 * for <code>arcAngle</code> degrees, using the current color.
 * Angles are interpreted such that 0 degrees is at the 3 o'clock
 * position. A positive value indicates a counter-clockwise rotation
 * while a negative value indicates a clockwise rotation.
 * </p><p>
 * The center of the arc is the center of the rectangle whose origin 
 * is (<code>x</code>, <code>y</code>) and whose size is specified by the 
 * <code>width</code> and <code>height</code> arguments. 
 * </p><p>
 * The resulting arc covers an area <code>width + 1</code> pixels wide
 * by <code>height + 1</code> pixels tall.
 * </p>
 *
 * @param x the x coordinate of the upper-left corner of the arc to be drawn
 * @param y the y coordinate of the upper-left corner of the arc to be drawn
 * @param width the width of the arc to be drawn
 * @param height the height of the arc to be drawn
 * @param startAngle the beginning angle
 * @param arcAngle the angular extent of the arc, relative to the start angle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.updateClip) setCGClipping();
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	OS.CGContextBeginPath(handle);
	OS.CGContextSaveGState(handle);
	float offset = (data.lineWidth % 2) == 1 ? 0.5f : 0f;
	OS.CGContextTranslateCTM(handle, x + offset + width / 2f, y + offset + height / 2f);
	OS.CGContextScaleCTM(handle, width / 2f, height / 2f);
	OS.CGContextAddArc(handle, 0, 0, 1, -startAngle * (float)Compatibility.PI / 180,  -(startAngle + arcAngle) * (float)Compatibility.PI / 180, true);
	OS.CGContextRestoreGState(handle);
	OS.CGContextStrokePath(handle);
	flush();
}

/** 
 * Draws a rectangle, based on the specified arguments, which has
 * the appearance of the platform's <em>focus rectangle</em> if the
 * platform supports such a notion, and otherwise draws a simple
 * rectangle in the receiver's foreground color.
 *
 * @param x the x coordinate of the rectangle
 * @param y the y coordinate of the rectangle
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle
 */
public void drawFocus(int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.updateClip) setCGClipping();
	//NOT DONE
//	drawRectangle (x, y, width - 1, height - 1);
	flush();
}

/**
 * Draws the given image in the receiver at the specified
 * coordinates.
 *
 * @param image the image to draw
 * @param x the x coordinate of where to draw
 * @param y the y coordinate of where to draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the given coordinates are outside the bounds of the image</li>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
 * </ul>
 */
public void drawImage(Image image, int x, int y) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	drawImage(image, 0, 0, -1, -1, x, y, -1, -1, true);
}

/**
 * Copies a rectangular area from the source image into a (potentially
 * different sized) rectangular area in the receiver. If the source
 * and destination areas are of differing sizes, then the source
 * area will be stretched or shrunk to fit the destination area
 * as it is copied. The copy fails if any part of the source rectangle
 * lies outside the bounds of the source image, or if any of the width
 * or height arguments are negative.
 *
 * @param image the source image
 * @param srcX the x coordinate in the source image to copy from
 * @param srcY the y coordinate in the source image to copy from
 * @param srcWidth the width in pixels to copy from the source
 * @param srcHeight the height in pixels to copy from the source
 * @param destX the x coordinate in the destination to copy to
 * @param destY the y coordinate in the destination to copy to
 * @param destWidth the width in pixels of the destination rectangle
 * @param destHeight the height in pixels of the destination rectangle
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the width or height arguments are negative.
 *    <li>ERROR_INVALID_ARGUMENT - if the source rectangle is not contained within the bounds of the source image</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
 * </ul>
 */
public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (srcWidth == 0 || srcHeight == 0 || destWidth == 0 || destHeight == 0) return;
	if (srcX < 0 || srcY < 0 || srcWidth < 0 || srcHeight < 0 || destWidth < 0 || destHeight < 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false);
}

void drawImage(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
	if (data.updateClip) setCGClipping();
	int imageHandle = srcImage.handle;
 	int imgWidth = OS.CGImageGetWidth(imageHandle);
 	int imgHeight = OS.CGImageGetHeight(imageHandle);
 	if (simple) {
 		srcWidth = destWidth = imgWidth;
 		srcHeight = destHeight = imgHeight;
 	} else {
 		simple = srcX == 0 && srcY == 0 &&
 			srcWidth == destWidth && destWidth == imgWidth &&
 			srcHeight == destHeight && destHeight == imgHeight;
		if (srcX + srcWidth > imgWidth || srcY + srcHeight > imgHeight) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
 	}
 	OS.CGContextSaveGState(handle);
 	OS.CGContextScaleCTM(handle, 1, -1);
 	OS.CGContextTranslateCTM(handle, 0, -(destHeight + 2 * destY));
 	CGRect rect = new CGRect();
 	rect.x = destX;
 	rect.y = destY;
 	rect.width = destWidth;
	rect.height = destHeight;
 	if (simple) {
 		OS.CGContextDrawImage(handle, rect, imageHandle);
 	} else {
		int bpc = OS.CGImageGetBitsPerComponent(imageHandle);
		int bpp = OS.CGImageGetBitsPerPixel(imageHandle);
		int bpr = OS.CGImageGetBytesPerRow(imageHandle);
		int colorspace = OS.CGImageGetColorSpace(imageHandle);
		int alphaInfo = OS.CGImageGetAlphaInfo(imageHandle);
		int data = srcImage.data + (srcY * bpr) + srcX * 4;
		int provider = OS.CGDataProviderCreateWithData(0, data, srcHeight * bpr, 0);
		if (provider != 0) {
			int subImage = OS.CGImageCreate(srcWidth, srcHeight, bpc, bpp, bpr, colorspace, alphaInfo, provider, null, false, 0);
			OS.CGDataProviderRelease(provider);
			if (subImage != 0) {
		 		OS.CGContextDrawImage(handle, rect, subImage);
 				OS.CGImageRelease(subImage);
			}
		}
 	}
 	OS.CGContextRestoreGState(handle);
 	flush();
}

/** 
 * Draws a line, using the foreground color, between the points 
 * (<code>x1</code>, <code>y1</code>) and (<code>x2</code>, <code>y2</code>).
 *
 * @param x1 the first point's x coordinate
 * @param y1 the first point's y coordinate
 * @param x2 the second point's x coordinate
 * @param y2 the second point's y coordinate
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawLine(int x1, int y1, int x2, int y2) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.updateClip) setCGClipping();
	if (x1 == x2 && y1 == y2 && data.lineWidth <= 1) {
		drawPoint(x1, y1);
		return;
	}
	OS.CGContextBeginPath(handle);
	float offset = (data.lineWidth % 2) == 1 ? 0.5f : 0f;
	OS.CGContextMoveToPoint(handle, x1 + offset, y1 + offset);
	OS.CGContextAddLineToPoint(handle, x2 + offset, y2 + offset);
	OS.CGContextStrokePath(handle);
	flush();
}

/** 
 * Draws the outline of an oval, using the foreground color,
 * within the specified rectangular area.
 * <p>
 * The result is a circle or ellipse that fits within the 
 * rectangle specified by the <code>x</code>, <code>y</code>, 
 * <code>width</code>, and <code>height</code> arguments. 
 * </p><p> 
 * The oval covers an area that is <code>width + 1</code> 
 * pixels wide and <code>height + 1</code> pixels tall.
 * </p>
 *
 * @param x the x coordinate of the upper left corner of the oval to be drawn
 * @param y the y coordinate of the upper left corner of the oval to be drawn
 * @param width the width of the oval to be drawn
 * @param height the height of the oval to be drawn
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawOval(int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.updateClip) setCGClipping();
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	OS.CGContextBeginPath(handle);
	OS.CGContextSaveGState(handle);
	float offset = (data.lineWidth % 2) == 1 ? 0.5f : 0f;
	OS.CGContextTranslateCTM(handle, x + offset + width / 2f, y + offset + height / 2f);
	OS.CGContextScaleCTM(handle, width / 2f, height / 2f);
	OS.CGContextMoveToPoint(handle, 1, 0);
	OS.CGContextAddArc(handle, 0, 0, 1, 0, (float)(2 *Compatibility.PI), true);
	OS.CGContextRestoreGState(handle);
	OS.CGContextStrokePath(handle);
	flush();
}

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public void drawPath(Path path) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path.handle == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (data.updateClip) setCGClipping();
	OS.CGContextBeginPath(handle);
	OS.CGContextSaveGState(handle);
	float offset = (data.lineWidth % 2) == 1 ? 0.5f : 0f;
	OS.CGContextTranslateCTM(handle, offset, offset);
	OS.CGContextAddPath(handle, path.handle);
	OS.CGContextRestoreGState(handle);
	OS.CGContextStrokePath(handle);
	flush();
}

/** 
 * Draws a pixel, using the foreground color, at the specified
 * point (<code>x</code>, <code>y</code>).
 * <p>
 * Note that the receiver's line attributes do not affect this
 * operation.
 * </p>
 *
 * @param x the point's x coordinate
 * @param y the point's y coordinate
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *  
 * @since 3.0
 */
public void drawPoint(int x, int y) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.updateClip) setCGClipping();
	CGRect rect = new CGRect();
	rect.x = x;
	rect.y = y;
	rect.width = 1;
	rect.height = 1;
	OS.CGContextSetFillColor(handle, data.foreground);
	OS.CGContextFillRect(handle, rect);
	OS.CGContextSetFillColor(handle, data.background);
	flush();
}

/** 
 * Draws the closed polygon which is defined by the specified array
 * of integer coordinates, using the receiver's foreground color. The array 
 * contains alternating x and y values which are considered to represent
 * points which are the vertices of the polygon. Lines are drawn between
 * each consecutive pair, and between the first pair and last pair in the
 * array.
 *
 * @param pointArray an array of alternating x and y values which are the vertices of the polygon
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT if pointArray is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawPolygon(int[] pointArray) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data.updateClip) setCGClipping();
	float[] points = new float[pointArray.length];
	for (int i=0; i<points.length; i++) {
		points[i] = pointArray[i];
	}
	OS.CGContextBeginPath(handle);
	OS.CGContextAddLines(handle, points, points.length / 2);
	OS.CGContextClosePath(handle);
	OS.CGContextStrokePath(handle);
	flush();
}

/** 
 * Draws the polyline which is defined by the specified array
 * of integer coordinates, using the receiver's foreground color. The array 
 * contains alternating x and y values which are considered to represent
 * points which are the corners of the polyline. Lines are drawn between
 * each consecutive pair, but not between the first pair and last pair in
 * the array.
 *
 * @param pointArray an array of alternating x and y values which are the corners of the polyline
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point array is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawPolyline(int[] pointArray) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data.updateClip) setCGClipping();
	float[] points = new float[pointArray.length];
	for (int i=0; i<points.length; i++) {
		points[i] = pointArray[i] + 0.5f;
	}
	OS.CGContextBeginPath(handle);
	OS.CGContextAddLines(handle, points, points.length / 2);
	OS.CGContextStrokePath(handle);
	flush();
}

/** 
 * Draws the outline of the rectangle specified by the arguments,
 * using the receiver's foreground color. The left and right edges
 * of the rectangle are at <code>x</code> and <code>x + width</code>. 
 * The top and bottom edges are at <code>y</code> and <code>y + height</code>. 
 *
 * @param x the x coordinate of the rectangle to be drawn
 * @param y the y coordinate of the rectangle to be drawn
 * @param width the width of the rectangle to be drawn
 * @param height the height of the rectangle to be drawn
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRectangle(int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.updateClip) setCGClipping();
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	CGRect rect = new CGRect();
	float offset = (data.lineWidth % 2) == 1 ? 0.5f : 0f;
	rect.x = x + offset;
	rect.y = y + offset;
	rect.width = width;
	rect.height = height;
	OS.CGContextStrokeRect(handle, rect);
	flush();
}

/** 
 * Draws the outline of the specified rectangle, using the receiver's
 * foreground color. The left and right edges of the rectangle are at
 * <code>rect.x</code> and <code>rect.x + rect.width</code>. The top 
 * and bottom edges are at <code>rect.y</code> and 
 * <code>rect.y + rect.height</code>. 
 *
 * @param rect the rectangle to draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRectangle(Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	drawRectangle (rect.x, rect.y, rect.width, rect.height);
}

/** 
 * Draws the outline of the round-cornered rectangle specified by 
 * the arguments, using the receiver's foreground color. The left and
 * right edges of the rectangle are at <code>x</code> and <code>x + width</code>. 
 * The top and bottom edges are at <code>y</code> and <code>y + height</code>.
 * The <em>roundness</em> of the corners is specified by the 
 * <code>arcWidth</code> and <code>arcHeight</code> arguments, which
 * are respectively the width and height of the ellipse used to draw
 * the corners.
 *
 * @param x the x coordinate of the rectangle to be drawn
 * @param y the y coordinate of the rectangle to be drawn
 * @param width the width of the rectangle to be drawn
 * @param height the height of the rectangle to be drawn
 * @param arcWidth the width of the arc
 * @param arcHeight the height of the arc
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.updateClip) setCGClipping();
	if (arcWidth == 0 || arcHeight == 0) {
		drawRectangle(x, y, width, height);
    	return;
	}
	int nx = x;
	int ny = y;
	int nw = width;
	int nh = height;
	int naw = arcWidth;
	int nah = arcHeight;	
	if (nw < 0) {
		nw = 0 - nw;
		nx = nx - nw;
	}
	if (nh < 0) {
		nh = 0 - nh;
		ny = ny - nh;
	}
	if (naw < 0) naw = 0 - naw;
	if (nah < 0) nah = 0 - nah;
	if (naw > nw) naw = nw;
	if (nah > nh) nah = nh;	
	float naw2 = naw / 2f;
	float nah2 = nah / 2f;
	float fw = nw / naw2;
	float fh = nh / nah2;
	OS.CGContextBeginPath(handle);
	OS.CGContextSaveGState(handle);
	float offset = (data.lineWidth % 2) == 1 ? 0.5f : 0f;
	OS.CGContextTranslateCTM(handle, nx + offset, ny + offset);
	OS.CGContextScaleCTM(handle, naw2, nah2);
	OS.CGContextMoveToPoint(handle, fw - 1, 0);
	OS.CGContextAddArcToPoint(handle, 0, 0, 0, 1, 1);
	OS.CGContextAddArcToPoint(handle, 0, fh, 1, fh, 1);
	OS.CGContextAddArcToPoint(handle, fw, fh, fw, fh - 1, 1);
	OS.CGContextAddArcToPoint(handle, fw, 0, fw - 1, 0, 1);
	OS.CGContextClosePath(handle);
	OS.CGContextRestoreGState(handle);
	OS.CGContextStrokePath(handle);
	flush();
}

/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. No tab expansion or carriage return processing
 * will be performed. The background of the rectangular area where
 * the string is being drawn will be filled with the receiver's
 * background color.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the string is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the string is to be drawn
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawString (String string, int x, int y) {
	drawString(string, x, y, false);
}

/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. No tab expansion or carriage return processing
 * will be performed. If <code>isTransparent</code> is <code>true</code>,
 * then the background of the rectangular area where the string is being
 * drawn will not be modified, otherwise it will be filled with the
 * receiver's background color.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the string is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the string is to be drawn
 * @param isTransparent if <code>true</code> the background will be transparent, otherwise it will be opaque
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawString(String string, int x, int y, boolean isTransparent) {
	drawText(string, x, y, isTransparent ? SWT.DRAW_TRANSPARENT : 0);
}

/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. Tab expansion and carriage return processing
 * are performed. The background of the rectangular area where
 * the text is being drawn will be filled with the receiver's
 * background color.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawText(String string, int x, int y) {
	drawText(string, x, y, SWT.DRAW_DELIMITER | SWT.DRAW_TAB);
}

/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. Tab expansion and carriage return processing
 * are performed. If <code>isTransparent</code> is <code>true</code>,
 * then the background of the rectangular area where the text is being
 * drawn will not be modified, otherwise it will be filled with the
 * receiver's background color.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param isTransparent if <code>true</code> the background will be transparent, otherwise it will be opaque
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawText(String string, int x, int y, boolean isTransparent) {
	int flags = SWT.DRAW_DELIMITER | SWT.DRAW_TAB;
	if (isTransparent) flags |= SWT.DRAW_TRANSPARENT;
	drawText(string, x, y, flags);
}

/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. Tab expansion, line delimiter and mnemonic
 * processing are performed according to the specified flags. If
 * <code>flags</code> includes <code>DRAW_TRANSPARENT</code>,
 * then the background of the rectangular area where the text is being
 * drawn will not be modified, otherwise it will be filled with the
 * receiver's background color.
 * <p>
 * The parameter <code>flags</code> may be a combination of:
 * <dl>
 * <dt><b>DRAW_DELIMITER</b></dt>
 * <dd>draw multiple lines</dd>
 * <dt><b>DRAW_TAB</b></dt>
 * <dd>expand tabs</dd>
 * <dt><b>DRAW_MNEMONIC</b></dt>
 * <dd>underline the mnemonic character</dd>
 * <dt><b>DRAW_TRANSPARENT</b></dt>
 * <dd>transparent background</dd>
 * </dl>
 * </p>
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param flags the flags specifing how to process the text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawText (String string, int x, int y, int flags) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data.updateClip) setCGClipping();
	int length = string.length();
	if (length == 0) return;
	length = setString(string, flags);
	OS.CGContextSaveGState(handle);
	OS.CGContextScaleCTM(handle, 1, -1);
	if ((flags & SWT.DRAW_DELIMITER) != 0) {
		int layout = data.layout;
		int[] breakCount = new int[1];
		OS.ATSUGetSoftLineBreaks(layout, 0, length, 0, null, breakCount);
		int[] breaks = new int[breakCount[0] + 1];
		OS.ATSUGetSoftLineBreaks(layout, 0, length, breakCount[0], breaks, breakCount);
		breaks[breakCount[0]] = length;
		for (int i=0, start=0; i<breaks.length; i++) {
			int lineBreak = breaks[i];
			drawText(x, y, start, lineBreak - start, flags);
			y += data.fontAscent + data.fontDescent;
			start = lineBreak;
		}
	} else {
		drawText(x, y, 0, length, flags);
	}
	OS.CGContextRestoreGState(handle);
	flush();
}

void drawText(int x, int y, int start, int length, int flags) {
	int layout = data.layout;
	if ((flags & SWT.DRAW_TRANSPARENT) == 0) {
		ATSTrapezoid trapezoid = new ATSTrapezoid();
		OS.ATSUGetGlyphBounds(layout, 0, 0, start, length, (short)OS.kATSUseDeviceOrigins, 1, trapezoid, null);
		int width = OS.Fix2Long(trapezoid.upperRight_x) - OS.Fix2Long(trapezoid.upperLeft_x);
		int height = OS.Fix2Long(trapezoid.lowerRight_y) - OS.Fix2Long(trapezoid.upperRight_y);
		CGRect rect = new CGRect();
		rect.x = x;
		rect.y = -(y + height);
		rect.width = width;
		rect.height = height;
		OS.CGContextSetFillColor(handle, data.background);
		OS.CGContextFillRect(handle, rect);
	}
	OS.CGContextSetFillColor(handle, data.foreground);
	OS.ATSUDrawText(layout, start, length, OS.Long2Fix(x), OS.Long2Fix(-(y + data.fontAscent)));	
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
	if (!(object instanceof GC)) return false;
	return handle == ((GC)object).handle;
}

/**
 * Fills the interior of a circular or elliptical arc within
 * the specified rectangular area, with the receiver's background
 * color.
 * <p>
 * The resulting arc begins at <code>startAngle</code> and extends  
 * for <code>arcAngle</code> degrees, using the current color.
 * Angles are interpreted such that 0 degrees is at the 3 o'clock
 * position. A positive value indicates a counter-clockwise rotation
 * while a negative value indicates a clockwise rotation.
 * </p><p>
 * The center of the arc is the center of the rectangle whose origin 
 * is (<code>x</code>, <code>y</code>) and whose size is specified by the 
 * <code>width</code> and <code>height</code> arguments. 
 * </p><p>
 * The resulting arc covers an area <code>width + 1</code> pixels wide
 * by <code>height + 1</code> pixels tall.
 * </p>
 *
 * @param x the x coordinate of the upper-left corner of the arc to be filled
 * @param y the y coordinate of the upper-left corner of the arc to be filled
 * @param width the width of the arc to be filled
 * @param height the height of the arc to be filled
 * @param startAngle the beginning angle
 * @param arcAngle the angular extent of the arc, relative to the start angle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawArc
 */
public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.updateClip) setCGClipping();
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	OS.CGContextBeginPath(handle);
    OS.CGContextSaveGState(handle);
    OS.CGContextTranslateCTM(handle, x + width / 2f, y + height / 2f);
    OS.CGContextScaleCTM(handle, width / 2f, height / 2f);
    OS.CGContextMoveToPoint(handle, 0, 0);
    OS.CGContextAddArc(handle, 0, 0, 1, -startAngle * (float)Compatibility.PI / 180,  -(startAngle + arcAngle) * (float)Compatibility.PI / 180, true);
    OS.CGContextClosePath(handle);
    OS.CGContextRestoreGState(handle);
	OS.CGContextFillPath(handle);
	flush();
}

/**
 * Fills the interior of the specified rectangle with a gradient
 * sweeping from left to right or top to bottom progressing
 * from the receiver's foreground color to its background color.
 *
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled, may be negative
 *        (inverts direction of gradient if horizontal)
 * @param height the height of the rectangle to be filled, may be negative
 *        (inverts direction of gradient if vertical)
 * @param vertical if true sweeps from top to bottom, else 
 *        sweeps from left to right
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle
 */
public void fillGradientRectangle(int x, int y, int width, int height, boolean vertical) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if ((width == 0) || (height == 0)) return;
	
	/* Rewrite this to use GdkPixbuf */

	RGB backgroundRGB, foregroundRGB;
	backgroundRGB = getBackground().getRGB();
	foregroundRGB = getForeground().getRGB();

	RGB fromRGB, toRGB;
	fromRGB = foregroundRGB;
	toRGB   = backgroundRGB;
	boolean swapColors = false;
	if (width < 0) {
		x += width; width = -width;
		if (! vertical) swapColors = true;
	}
	if (height < 0) {
		y += height; height = -height;
		if (vertical) swapColors = true;
	}
	if (swapColors) {
		fromRGB = backgroundRGB;
		toRGB   = foregroundRGB;
	}
	if (fromRGB.equals(toRGB)) {
		fillRectangle(x, y, width, height);
		return;
	}
	ImageData.fillGradientRectangle(this, data.device,
		x, y, width, height, vertical, fromRGB, toRGB,
		8, 8, 8);
}

/** 
 * Fills the interior of an oval, within the specified
 * rectangular area, with the receiver's background
 * color.
 *
 * @param x the x coordinate of the upper left corner of the oval to be filled
 * @param y the y coordinate of the upper left corner of the oval to be filled
 * @param width the width of the oval to be filled
 * @param height the height of the oval to be filled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawOval
 */
public void fillOval(int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.updateClip) setCGClipping();
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	OS.CGContextBeginPath(handle);
    OS.CGContextSaveGState(handle);
    OS.CGContextTranslateCTM(handle, x + width / 2f, y + height / 2f);
    OS.CGContextScaleCTM(handle, width / 2f, height / 2f);
    OS.CGContextMoveToPoint(handle, 1, 0);
    OS.CGContextAddArc(handle, 0, 0, 1, 0,  (float)(Compatibility.PI * 2), false);
    OS.CGContextClosePath(handle);
    OS.CGContextRestoreGState(handle);
	OS.CGContextFillPath(handle);
	flush();
}

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public void fillPath(Path path) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path.handle == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (data.updateClip) setCGClipping();
	OS.CGContextBeginPath(handle);
	OS.CGContextAddPath(handle, path.handle);
	if (data.fillRule == SWT.FILL_WINDING) {
		OS.CGContextFillPath(handle);
	} else {
		OS.CGContextEOFillPath(handle);
	}
	flush();
}

/** 
 * Fills the interior of the closed polygon which is defined by the
 * specified array of integer coordinates, using the receiver's
 * background color. The array contains alternating x and y values
 * which are considered to represent points which are the vertices of
 * the polygon. Lines are drawn between each consecutive pair, and
 * between the first pair and last pair in the array.
 *
 * @param pointArray an array of alternating x and y values which are the vertices of the polygon
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT if pointArray is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawPolygon	
 */
public void fillPolygon(int[] pointArray) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data.updateClip) setCGClipping();
	float[] points = new float[pointArray.length];
	for (int i=0; i<points.length; i++) {
		points[i] = pointArray[i];
	}
	OS.CGContextBeginPath(handle);
	OS.CGContextAddLines(handle, points, points.length / 2);
	OS.CGContextClosePath(handle);
	if (data.fillRule == SWT.FILL_WINDING) {
		OS.CGContextFillPath(handle);
	} else {
		OS.CGContextEOFillPath(handle);
	}
	flush();
}

/** 
 * Fills the interior of the rectangle specified by the arguments,
 * using the receiver's background color. 
 *
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle
 */
public void fillRectangle(int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.updateClip) setCGClipping();
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	CGRect rect = new CGRect();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	OS.CGContextFillRect(handle, rect);
	flush();
}

/** 
 * Fills the interior of the specified rectangle, using the receiver's
 * background color. 
 *
 * @param rect the rectangle to be filled
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle
 */
public void fillRectangle(Rectangle rect) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	fillRectangle(rect.x, rect.y, rect.width, rect.height);
}

/** 
 * Fills the interior of the round-cornered rectangle specified by 
 * the arguments, using the receiver's background color. 
 *
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 * @param arcWidth the width of the arc
 * @param arcHeight the height of the arc
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRoundRectangle
 */
public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.updateClip) setCGClipping();
	if (arcWidth == 0 || arcHeight == 0) {
		fillRectangle(x, y, width, height);
    	return;
	}
	int nx = x;
	int ny = y;
	int nw = width;
	int nh = height;
	int naw = arcWidth;
	int nah = arcHeight;	
	if (nw < 0) {
		nw = 0 - nw;
		nx = nx - nw;
	}
	if (nh < 0) {
		nh = 0 - nh;
		ny = ny - nh;
	}
	if (naw < 0) naw = 0 - naw;
	if (nah < 0) nah = 0 - nah;
	if (naw > nw) naw = nw;
	if (nah > nh) nah = nh;	
	float naw2 = naw / 2f;
	float nah2 = nah / 2f;
	float fw = nw / naw2;
	float fh = nh / nah2;
	OS.CGContextBeginPath(handle);
	OS.CGContextSaveGState(handle);
	OS.CGContextTranslateCTM(handle, nx, ny);
	OS.CGContextScaleCTM(handle, naw2, nah2);
	OS.CGContextMoveToPoint(handle, fw - 1, 0);
	OS.CGContextAddArcToPoint(handle, 0, 0, 0, 1, 1);
	OS.CGContextAddArcToPoint(handle, 0, fh, 1, fh, 1);
	OS.CGContextAddArcToPoint(handle, fw, fh, fw, fh - 1, 1);
	OS.CGContextAddArcToPoint(handle, fw, 0, fw - 1, 0, 1);
	OS.CGContextClosePath(handle);
	OS.CGContextRestoreGState(handle);
	OS.CGContextFillPath(handle);
	flush();
}

void flush () {
	if (data.control != 0 && data.paintEvent == 0) {
		if (data.thread != Thread.currentThread()) {
			OS.CGContextFlush(handle);
		} else {
			OS.CGContextSynchronize(handle);
		}
	}
}

/**
 * Returns the <em>advance width</em> of the specified character in
 * the font which is currently selected into the receiver.
 * <p>
 * The advance width is defined as the horizontal distance the cursor
 * should move after printing the character in the selected font.
 * </p>
 *
 * @param ch the character to measure
 * @return the distance in the x direction to move past the character before painting the next
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getAdvanceWidth(char ch) {	
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	//NOT DONE
	return stringExtent(new String(new char[]{ch})).x;
}

/** 
 * Returns the background color.
 *
 * @return the receiver's background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Color getBackground() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return Color.carbon_new (data.device, data.background);
}

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public int getAlpha() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.alpha;
}

/**
 * Returns the width of the specified character in the font
 * selected into the receiver. 
 * <p>
 * The width is defined as the space taken up by the actual
 * character, not including the leading and tailing whitespace
 * or overhang.
 * </p>
 *
 * @param ch the character to measure
 * @return the width of the character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getCharWidth(char ch) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	//NOT DONE
	return stringExtent(new String(new char[]{ch})).x;
}

/** 
 * Returns the bounding rectangle of the receiver's clipping
 * region. If no clipping region is set, the return value
 * will be a rectangle which covers the entire bounds of the
 * object the receiver is drawing on.
 *
 * @return the bounding rectangle of the clipping region
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Rectangle getClipping() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Region region = new Region();
	getClipping(region);
	Rectangle rect = region.getBounds();
	region.dispose();
	return rect;
}

/** 
 * Sets the region managed by the argument to the current
 * clipping region of the receiver.
 *
 * @param region the region to fill with the clipping region
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the region is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the region is disposed</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void getClipping(Region region) {	
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Rect bounds = null;
	int clipping = region.handle;
	if (data.clipRgn == 0) {
		int width = 0, height = 0;
		if (data.control != 0) {
			if (bounds == null) bounds = new Rect();
			OS.GetControlBounds(data.control, bounds);
			width = bounds.right - bounds.left;
			height = bounds.bottom - bounds.top;
		} else {
			if (data.image != null) {
				int image = data.image.handle;
				width = OS.CGImageGetWidth(image);
				height = OS.CGImageGetHeight(image);
			}
		}
		OS.SetRectRgn(clipping, (short)0, (short)0, (short)width, (short)height);
	} else {
		OS.CopyRgn(data.clipRgn, clipping);
		if (!isIdentity(data.transform)) return;
	}
	if (data.paintEvent != 0 && data.visibleRgn != 0) {
		if (bounds == null) bounds = new Rect();
		OS.GetControlBounds(data.control, bounds);
		OS.OffsetRgn(data.visibleRgn, (short)-bounds.left, (short)-bounds.top);
		OS.SectRgn(data.visibleRgn, clipping, clipping);
		OS.OffsetRgn(data.visibleRgn, bounds.left, bounds.top);
	}
}

public int getFillRule() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.fillRule;
}

/** 
 * Returns the font currently being used by the receiver
 * to draw and measure text.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Font getFont() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.font;
}

/**
 * Returns a FontMetrics which contains information
 * about the font currently being used by the receiver
 * to draw and measure text.
 *
 * @return font metrics for the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public FontMetrics getFontMetrics() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Font font = data.font;
	FontInfo info = new FontInfo();
	OS.FetchFontInfo(font.id, font.size, font.style, info);
	int ascent = info.ascent;
	int descent = info.descent;
	int leading = info.leading;
	/* This code is intentionaly comment. Not right for fixed width fonts. */
	//fm.averageCharWidth = info.widMax / 3;
	String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; 
	int averageCharWidth = stringExtent(s).x / s.length();
	return FontMetrics.carbon_new(ascent, descent, averageCharWidth, leading, ascent + leading + descent);
}

/** 
 * Returns the receiver's foreground color.
 *
 * @return the color used for drawing foreground things
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Color getForeground() {	
	if (handle == 0) SWT.error(SWT.ERROR_WIDGET_DISPOSED);
	return Color.carbon_new(data.device, data.foreground);	
}

/** 
 * Returns the receiver's line cap style, which will be one
 * of the constants <code>SWT.CAP_FLAT</code>, <code>SWT.CAP_ROUND</code>,
 * or <code>SWT.CAP_SQUARE</code>.
 *
 * @return the cap style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public int getLineCap() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.lineCap;
}

/** 
 * Returns the receiver's line dash style.
 *
 * @return the lin dash style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public int[] getLineDash() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	float[] lengths = data.dashes;
	if (lengths == null) return null;
	int[] dashes = new int[lengths.length];
	for (int i = 0; i < dashes.length; i++) {
		dashes[i] = (int)lengths[i];
	}
	return dashes;
}

/** 
 * Returns the receiver's line join style, which will be one
 * of the constants <code>SWT.JOIN_MITER</code>, <code>SWT.JOIN_ROUND</code>,
 * or <code>SWT.JOIN_BEVEL</code>.
 *
 * @return the join style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public int getLineJoin() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.lineJoin;
}

/** 
 * Returns the receiver's line style, which will be one
 * of the constants <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
 * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
 * <code>SWT.LINE_DASHDOTDOT</code>.
 *
 * @return the style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getLineStyle() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.lineStyle;
}

/** 
 * Returns the width that will be used when drawing lines
 * for all of the figure drawing operations (that is,
 * <code>drawLine</code>, <code>drawRectangle</code>, 
 * <code>drawPolyline</code>, and so forth.
 *
 * @return the receiver's line width 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getLineWidth() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.lineWidth;
}

/**
 * Returns the receiver's style information.
 * <p>
 * Note that the value which is returned by this method <em>may
 * not match</em> the value which was provided to the constructor
 * when the receiver was created. This can occur when the underlying
 * operating system does not support a particular combination of
 * requested styles. 
 * </p>
 *
 * @return the style bits
 *  
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *   
 * @since 2.1.2
 */
public int getStyle () {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.style;
}

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public void getTransform (Transform transform) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	float[] cmt = data.transform; 
	transform.setElements(cmt[0], cmt[1], cmt[2], cmt[3], cmt[4], cmt[5]);
}

/** 
 * Returns <code>true</code> if this GC is drawing in the mode
 * where the resulting color in the destination is the
 * <em>exclusive or</em> of the color values in the source
 * and the destination, and <code>false</code> if it is
 * drawing in the mode where the destination color is being
 * replaced with the source color value.
 *
 * @return <code>true</code> true if the receiver is in XOR mode, and false otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean getXORMode() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.xorMode;
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects which return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #equals
 */
public int hashCode() {
	return handle;
}

void init(Drawable drawable, GCData data, int context) {
	int colorspace = data.device.colorspace;
	OS.CGContextSetStrokeColorSpace(context, colorspace);
	OS.CGContextSetFillColorSpace(context, colorspace);
	float[] foreground = data.foreground;
	if (foreground != null) OS.CGContextSetStrokeColor(context, foreground);
	float[] background = data.background;
	if (background != null) OS.CGContextSetFillColor(context, background);

	Image image = data.image;
	if (image != null) image.memGC = this;
	this.drawable = drawable;
	this.data = data;
	handle = context;
	
	if (data.font != null) setGCFont();
}

/**
 * Returns <code>true</code> if the receiver has a clipping
 * region set into it, and <code>false</code> otherwise.
 * If this method returns false, the receiver will draw on all
 * available space in the destination. If it returns true, 
 * it will draw only in the area that is covered by the region
 * that can be accessed with <code>getClipping(region)</code>.
 *
 * @return <code>true</code> if the GC has a clipping region, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean isClipped() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.clipRgn != 0;
}

/**
 * Returns <code>true</code> if the GC has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the GC.
 * When a GC has been disposed, it is an error to
 * invoke any other method using the GC.
 *
 * @return <code>true</code> when the GC is disposed and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == 0;
}

boolean isIdentity(float[] transform) {
	return transform[0] == 1 && transform[1] == 0 && transform[2] == 0
	 	&& transform[3] == 1 && transform[4] == 0 && transform[5] == 0;
}

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public void setAlpha(int alpha) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	data.alpha = alpha & 0xFF;
	OS.CGContextSetAlpha(handle, data.alpha / 255f);
}

/**
 * Sets the background color. The background color is used
 * for fill operations and as the background color when text
 * is drawn.
 *
 * @param color the new background color for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the color is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the color has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setBackground(Color color) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.background = color.handle;
	OS.CGContextSetFillColor(handle, color.handle);
}

void setClipping(int clipRgn) {
	if (clipRgn == 0) {
		if (data.clipRgn != 0) {
			OS.DisposeRgn(data.clipRgn);
			data.clipRgn = 0;
		} else {
			return;
		}
	} else {
		if (data.clipRgn == 0) data.clipRgn = OS.NewRgn();
		OS.CopyRgn(clipRgn, data.clipRgn);
	}
	data.updateClip = true;
	setCGClipping();
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the rectangular area specified
 * by the arguments.
 *
 * @param x the x coordinate of the clipping rectangle
 * @param y the y coordinate of the clipping rectangle
 * @param width the width of the clipping rectangle
 * @param height the height of the clipping rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping(int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	int clipRgn = OS.NewRgn();
	OS.SetRectRgn(clipRgn, (short)x, (short)y, (short)(x + width), (short)(y + height));
	setClipping(clipRgn);
	OS.DisposeRgn(clipRgn);
}

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public void setClipping(Path path) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path != null && path.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	setClipping(0);
	if (path != null) {
		OS.CGContextAddPath(handle, path.handle);
		OS.CGContextEOClip(handle);
	}
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the rectangular area specified
 * by the argument.
 *
 * @param rect the clipping rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping(Rectangle rect) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) {
		setClipping(0);
	} else {
		setClipping(rect.x, rect.y, rect.width, rect.height);
	}
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the region specified
 * by the argument.
 *
 * @param region the clipping region.
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the region has been disposed</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping(Region region) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region != null && region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	setClipping(region != null ? region.handle : 0);
}

void setCGClipping () {
	data.updateClip = false;
	if (data.control == 0) {
		OS.CGContextScaleCTM(handle, 1, -1);
		if (data.clipRgn != 0) {
			OS.ClipCGContextToRegion(handle, new Rect(), data.clipRgn);
		} else {
			int rgn = OS.NewRgn();
			OS.SetRectRgn(rgn, (short)-32768, (short)-32768, (short)32767, (short)32767);
			OS.ClipCGContextToRegion(handle, new Rect(), rgn);
			OS.DisposeRgn(rgn);
		}
		OS.CGContextScaleCTM(handle, 1, -1);
		return;
	}
	int port = data.port;
	if (port == 0) {
		int window = OS.GetControlOwner(data.control);
		port = OS.GetWindowPort(window);
	}
	Rect portRect = data.portRect;
	Rect rect = data.controlRect;
	OS.CGContextTranslateCTM(handle, -rect.left, (portRect.bottom - portRect.top) - rect.top);
	OS.CGContextScaleCTM(handle, 1, -1);
	OS.GetPortBounds(port, portRect);
	OS.GetControlBounds(data.control, rect);
	if (data.clipRgn != 0) {
		int rgn = OS.NewRgn();
		OS.CopyRgn(data.clipRgn, rgn);
		OS.OffsetRgn(rgn, rect.left, rect.top);
		OS.SectRgn(data.visibleRgn, rgn, rgn);
		OS.ClipCGContextToRegion(handle, portRect, rgn);
		OS.DisposeRgn(rgn);
	} else {
		OS.ClipCGContextToRegion(handle, portRect, data.visibleRgn);
	}
	OS.CGContextScaleCTM(handle, 1, -1);
	OS.CGContextTranslateCTM(handle, rect.left, -(portRect.bottom - portRect.top) + rect.top);
}

public void setFillRule(int rule) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	switch (rule) {
		case SWT.FILL_WINDING:
		case SWT.FILL_EVEN_ODD: break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.fillRule = rule;
}

/** 
 * Sets the font which will be used by the receiver
 * to draw and measure text to the argument. If the
 * argument is null, then a default font appropriate
 * for the platform will be used instead.
 *
 * @param font the new font for the receiver, or null to indicate a default font
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the font has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setFont(Font font) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (font == null) font = data.device.systemFont;
	if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.font = font;
	setGCFont ();
}

void setGCFont() {
	int tabs = data.tabs;
	if (tabs != 0) OS.DisposePtr(tabs);
	data.tabs = 0;	
	Font font = data.font;
	FontInfo info = new FontInfo();
	OS.FetchFontInfo(font.id, font.size, font.style, info);
	data.fontAscent = info.ascent;
	data.fontDescent = info.descent;
	if (font.atsuiStyle == 0) {
		if (data.atsuiStyle != 0) OS.ATSUDisposeStyle(data.atsuiStyle);
		data.atsuiStyle = font.createStyle();
	}
	data.string = null;
	data.stringWidth = data.stringHeight = -1;
}

/**
 * Sets the foreground color. The foreground color is used
 * for drawing operations including when text is drawn.
 *
 * @param color the new foreground color for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the color is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the color has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setForeground(Color color) {	
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.foreground = color.handle;
	OS.CGContextSetStrokeColor(handle, color.handle);
}

/** 
 * Sets the receiver's line cap style to the argument, which must be one
 * of the constants <code>SWT.CAP_FLAT</code>, <code>SWT.CAP_ROUND</code>,
 * or <code>SWT.CAP_SQUARE</code>.
 *
 * @param cap the cap style to be used for drawing lines
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the style is not valid</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public void setLineCap(int cap) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int cap_style = 0;
	switch (cap) {
		case SWT.CAP_ROUND:
			cap_style = OS.kCGLineCapRound;
			break;
		case SWT.CAP_FLAT:
			cap_style = OS.kCGLineCapButt;
			break;
		case SWT.CAP_SQUARE:
			cap_style = OS.kCGLineCapSquare;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineCap = cap;
	OS.CGContextSetLineCap(handle, cap_style);
}

/** 
 * Sets the receiver's line dash style to the argument.
 *
 * @param dashes the dash style to be used for drawing lines
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the values in the array is less than or equal 0</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public void setLineDash(int[] dashes) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	float[] lengths = null;
	if (dashes != null && dashes.length != 0) {
		lengths = new float[dashes.length];
		for (int i = 0; i < lengths.length; i++) {
			int dash = dashes[i];
			if (dash <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			lengths[i] = dash;
		}
		data.lineStyle = SWT.LINE_CUSTOM;
	} else {
		data.lineStyle = SWT.LINE_SOLID;
	}
	OS.CGContextSetLineDash(handle, 0, lengths, lengths != null ? lengths.length : 0);
	data.dashes = lengths;
}

/** 
 * Sets the receiver's line join style to the argument, which must be one
 * of the constants <code>SWT.JOIN_MITER</code>, <code>SWT.JOIN_ROUND</code>,
 * or <code>SWT.JOIN_BEVEL</code>.
 *
 * @param join the join style to be used for drawing lines
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the style is not valid</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public void setLineJoin(int join) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int join_style = 0;
	switch (join) {
		case SWT.JOIN_MITER:
			join_style = OS.kCGLineJoinMiter;
			break;
		case SWT.JOIN_ROUND:
			join_style = OS.kCGLineJoinRound;
			break;
		case SWT.JOIN_BEVEL:
			join_style = OS.kCGLineJoinBevel;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineJoin = join;
	OS.CGContextSetLineJoin(handle, join_style);
}

/** 
 * Sets the receiver's line style to the argument, which must be one
 * of the constants <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
 * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
 * <code>SWT.LINE_DASHDOTDOT</code>.
 *
 * @param lineStyle the style to be used for drawing lines
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the style is not valid</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setLineStyle(int lineStyle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	switch (lineStyle) {
		case SWT.LINE_SOLID:
			OS.CGContextSetLineDash(handle, 0, null, 0);
			break;
		case SWT.LINE_DASH:
			OS.CGContextSetLineDash(handle, 0, new float[]{18, 6}, 2);
			break;
		case SWT.LINE_DOT:
			OS.CGContextSetLineDash(handle, 0, new float[]{3, 3}, 2);
			break;
		case SWT.LINE_DASHDOT:
			OS.CGContextSetLineDash(handle, 0, new float[]{9, 6, 3, 6}, 4);
			break;
		case SWT.LINE_DASHDOTDOT:
			OS.CGContextSetLineDash(handle, 0, new float[]{9, 3, 3, 3, 3, 3}, 6);
			break;
		case SWT.LINE_CUSTOM:
			float[] lengths = data.dashes;
			if (lengths != null) {
				OS.CGContextSetLineDash(handle, 0, lengths, lengths.length);
			} else {
				OS.CGContextSetLineDash(handle, 0, null, 0);
				lineStyle = SWT.LINE_SOLID;
			}
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineStyle = lineStyle;
}

/** 
 * Sets the width that will be used when drawing lines
 * for all of the figure drawing operations (that is,
 * <code>drawLine</code>, <code>drawRectangle</code>, 
 * <code>drawPolyline</code>, and so forth.
 *
 * @param lineWidth the width of a line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setLineWidth(int width) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	data.lineWidth = width;
	OS.CGContextSetLineWidth(handle, width);
}

int setString(String string, int flags) {
	if (data.layout == 0) createLayout ();
	if (string == data.string && (flags & ~SWT.DRAW_TRANSPARENT) == (data.drawFlags & ~SWT.DRAW_TRANSPARENT)) {
		return data.stringLength;
	}
	int layout = data.layout;
	int length = string.length();
	char[] chars = new char[length];
	string.getChars(0, length, chars, 0);
	int breakCount = 0;
	int[] breaks = null;
	if ((flags & (SWT.DRAW_MNEMONIC | SWT.DRAW_DELIMITER)) != 0) {
		int i=0, j=0;
		while (i < chars.length) {
			char c = chars [j++] = chars [i++];
			switch (c) {
				case '&': {
					if ((flags & SWT.DRAW_MNEMONIC) != 0) {
						if (i == chars.length) {continue;}
						if (chars [i] == '&') {i++; continue;}
						j--;
					}
					break;
				}
				case '\r':
				case '\n': {
					if ((flags & SWT.DRAW_DELIMITER) != 0) {
						if (c == '\r' && i != chars.length && chars[i] == '\n') i++;
						j--;
						if (breaks == null) {
							breaks = new int[4];
						} else if (breakCount == breaks.length) {
							int[] newBreaks = new int[breaks.length + 4];
							System.arraycopy(breaks, 0, newBreaks, 0, breaks.length);
							breaks = newBreaks;
						}
						breaks[breakCount++] = j;
					}
					break;
				}
			}
		}
		length = j;
	}
	if ((flags & SWT.DRAW_TAB) != 0) {
		if (data.tabs == 0) createTabs();
		OS.ATSUSetTabArray(layout, data.tabs, TAB_COUNT);
	} else {
		OS.ATSUSetTabArray(layout, 0, 0);
	}
	int ptr = OS.NewPtr(length * 2);
	OS.memcpy(ptr, chars, length * 2);
	OS.ATSUSetTextPointerLocation(layout, ptr, 0, length, length);
	if ((flags & SWT.DRAW_DELIMITER) != 0 && breaks != null) {
		for (int i=0; i<breakCount; i++) {
			OS.ATSUSetSoftLineBreak(layout, breaks[i]);
		}
	}
	Font font = data.font;
	int atsuiStyle = font.atsuiStyle != 0 ? font.atsuiStyle : data.atsuiStyle;
	OS.ATSUSetRunStyle(layout, atsuiStyle, 0, length);
	OS.ATSUSetTransientFontMatching(layout, true);
	if (data.stringPtr != 0) OS.DisposePtr(data.stringPtr);
	data.stringPtr = ptr;
	data.string = string;
	data.stringLength = length;
	data.stringWidth = data.stringHeight = -1;
	data.drawFlags = flags;
	return length;
}

/** 
 * If the argument is <code>true</code>, puts the receiver
 * in a drawing mode where the resulting color in the destination
 * is the <em>exclusive or</em> of the color values in the source
 * and the destination, and if the argument is <code>false</code>,
 * puts the receiver in a drawing mode where the destination color
 * is replaced with the source color value.
 *
 * @param xor if <code>true</code>, then <em>xor</em> mode is used, otherwise <em>source copy</em> mode is used
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setXORMode(boolean xor) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	//NOT DONE
	data.xorMode = xor;
}

int regionToRects(int message, int rgn, int r, int newRgn) {
	if (message == OS.kQDRegionToRectsMsgParse) {
		Rect rect = new Rect();
		OS.memcpy(rect, r, Rect.sizeof);
		CGPoint point = new CGPoint(); 
		int polyRgn = OS.NewRgn();
		OS.OpenRgn();
		point.x = rect.left;
		point.y = rect.top;
		OS.CGPointApplyAffineTransform(point, data.inverseTransform, point);
		OS.MoveTo((short)Math.round(point.x), (short)Math.round(point.y));
		point.x = rect.right;
		point.y = rect.top;
		OS.CGPointApplyAffineTransform(point, data.inverseTransform, point);
		OS.LineTo((short)Math.round(point.x), (short)Math.round(point.y));
		point.x = rect.right;
		point.y = rect.bottom;
		OS.CGPointApplyAffineTransform(point, data.inverseTransform, point);
		OS.LineTo((short)Math.round(point.x), (short)Math.round(point.y));
		point.x = rect.left;
		point.y = rect.bottom;
		OS.CGPointApplyAffineTransform(point, data.inverseTransform, point);
		OS.LineTo((short)Math.round(point.x), (short)Math.round(point.y));
		OS.CloseRgn(polyRgn);
		OS.UnionRgn(newRgn, polyRgn, newRgn);
		OS.DisposeRgn(polyRgn);
	}
	return 0;
}

/**
 * WARNING API STILL UNDER CONSTRUCTION AND SUBJECT TO CHANGE
 */
public void setTransform(Transform transform) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (transform != null && transform.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.CGContextConcatCTM(handle, data.inverseTransform);
	if (transform != null) {
		OS.CGContextConcatCTM(handle, transform.handle);
		System.arraycopy(transform.handle, 0, data.transform, 0, data.transform.length);
		System.arraycopy(transform.handle, 0, data.inverseTransform, 0, data.inverseTransform.length);
		OS.CGAffineTransformInvert(data.inverseTransform, data.inverseTransform);
	} else {
		data.transform = new float[]{1, 0, 0, 1, 0, 0};
		data.inverseTransform = new float[]{1, 0, 0, 1, 0, 0};
	}
	//TODO - rounds off problems
	int clipRgn = data.clipRgn;
	if (clipRgn != 0) {
		int newRgn = OS.NewRgn();
		Region region = new Region();
		Callback callback = new Callback(this, "regionToRects", 4);
		int proc = callback.getAddress();
		if (proc != 0) {
			OS.QDRegionToRects(clipRgn, OS.kQDParseRegionFromTopLeft, proc, newRgn);
			callback.dispose();
		}
		OS.DisposeRgn(clipRgn);
		data.clipRgn = newRgn;
	}
}

/**
 * Returns the extent of the given string. No tab
 * expansion or carriage return processing will be performed.
 * <p>
 * The <em>extent</em> of a string is the width and height of
 * the rectangular area it would cover if drawn in a particular
 * font (in this case, the current font in the receiver).
 * </p>
 *
 * @param string the string to measure
 * @return a point containing the extent of the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point stringExtent(String string) {
	return textExtent(string, 0);
}

/**
 * Returns the extent of the given string. Tab expansion and
 * carriage return processing are performed.
 * <p>
 * The <em>extent</em> of a string is the width and height of
 * the rectangular area it would cover if drawn in a particular
 * font (in this case, the current font in the receiver).
 * </p>
 *
 * @param string the string to measure
 * @return a point containing the extent of the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point textExtent(String string) {
	return textExtent(string, SWT.DRAW_DELIMITER | SWT.DRAW_TAB);
}

/**
 * Returns the extent of the given string. Tab expansion, line
 * delimiter and mnemonic processing are performed according to
 * the specified flags, which can be a combination of:
 * <dl>
 * <dt><b>DRAW_DELIMITER</b></dt>
 * <dd>draw multiple lines</dd>
 * <dt><b>DRAW_TAB</b></dt>
 * <dd>expand tabs</dd>
 * <dt><b>DRAW_MNEMONIC</b></dt>
 * <dd>underline the mnemonic character</dd>
 * <dt><b>DRAW_TRANSPARENT</b></dt>
 * <dd>transparent background</dd>
 * </dl>
 * <p>
 * The <em>extent</em> of a string is the width and height of
 * the rectangular area it would cover if drawn in a particular
 * font (in this case, the current font in the receiver).
 * </p>
 *
 * @param string the string to measure
 * @param flags the flags specifing how to process the text
 * @return a point containing the extent of the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point textExtent(String string, int flags) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int length = setString(string, flags);
	if (data.stringWidth != -1) return new Point(data.stringWidth, data.stringHeight);
	int width = 0, height;
	if (length == 0) {
		height = data.fontAscent + data.fontDescent;
	} else {
		ATSTrapezoid trapezoid = new ATSTrapezoid();
		if ((flags & SWT.DRAW_DELIMITER) != 0) {
			height = 0;
			int layout = data.layout;
			int[] breakCount = new int[1];
			OS.ATSUGetSoftLineBreaks(layout, 0, length, 0, null, breakCount);
			int[] breaks = new int[breakCount[0] + 1];
			OS.ATSUGetSoftLineBreaks(layout, 0, length, breakCount[0], breaks, breakCount);
			breaks[breakCount[0]] = length;
			for (int i=0, start=0; i<breaks.length; i++) {
				int lineBreak = breaks[i];
				OS.ATSUGetGlyphBounds(layout, 0, 0, start, lineBreak - start, (short)OS.kATSUseDeviceOrigins, 1, trapezoid, null);
				width = Math.max(width, OS.Fix2Long(trapezoid.upperRight_x) - OS.Fix2Long(trapezoid.upperLeft_x));
				height += OS.Fix2Long(trapezoid.lowerRight_y) - OS.Fix2Long(trapezoid.upperRight_y);
				start = lineBreak;
			}
		} else {
			OS.ATSUGetGlyphBounds(data.layout, 0, 0, 0, length, (short)OS.kATSUseDeviceOrigins, 1, trapezoid, null);
			width = OS.Fix2Long(trapezoid.upperRight_x) - OS.Fix2Long(trapezoid.upperLeft_x);
			height = OS.Fix2Long(trapezoid.lowerRight_y) - OS.Fix2Long(trapezoid.upperRight_y);
		}
	}
	return new Point(data.stringWidth = width, data.stringHeight = height);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "GC {*DISPOSED*}";
	return "GC {" + handle + "}";
}

}

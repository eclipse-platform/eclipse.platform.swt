/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
 
/**
 * Class <code>GC</code> is where all of the drawing capabilities that are 
 * supported by SWT are located. Instances are used to draw on either an 
 * <code>Image</code>, a <code>Control</code>, or directly on a <code>Display</code>.
 * <p>
 * Application code must explicitly invoke the <code>GC.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required. This is <em>particularly</em>
 * important on Windows95 and Windows98 where the operating system has a limited
 * number of device contexts available.
 * </p>
 *
 * @see org.eclipse.swt.events.PaintEvent
 */
public final class GC {
	/**
	 * the handle to the OS device context
	 * (Warning: This field is platform dependent)
	 */
	public int handle;
	
	Drawable drawable;
	GCData data;

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
public GC(Drawable drawable, int style) {
	if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	GCData data = new GCData();
	data.style = checkStyle(style);
	int xGC = drawable.internal_new_GC(data);
	Device device = data.device;
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	data.device = device;
	init(drawable, data, xGC);
	if (device.tracking) device.new_Object(this);
}
static int checkStyle (int style) {
	if ((style & SWT.LEFT_TO_RIGHT) != 0) style &= ~SWT.RIGHT_TO_LEFT;
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
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
public void copyArea(int x, int y, int width, int height, int destX, int destY) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - x, deltaY = destY - y;
	if (deltaX == 0 && deltaY == 0) return;
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	OS.XSetGraphicsExposures (xDisplay, handle, true);
	OS.XCopyArea(xDisplay, xDrawable, xDrawable, handle, x, y, width, height, destX, destY);
	OS.XSetGraphicsExposures (xDisplay, handle, false);
	if (data.image != null) return;
	boolean disjoint = (destX + width < x) || (x + width < destX) || (destY + height < y) || (y + height < destY);
	if (disjoint) {
		OS.XClearArea (xDisplay, xDrawable, x, y, width, height, true);
	} else {
		if (deltaX != 0) {
			int newX = destX - deltaX;
			if (deltaX < 0) newX = destX + width;
			OS.XClearArea (xDisplay, xDrawable, newX, y, Math.abs (deltaX), height, true);
		}
		if (deltaY != 0) {
			int newY = destY - deltaY;
			if (deltaY < 0) newY = destY + height;
			OS.XClearArea (xDisplay, xDrawable, x, newY, width, Math.abs (deltaY), true);
		}
	}
}
/**
 * Copies a rectangular area of the receiver at the specified
 * position into the image, which must be of type <code>SWT.BITMAP</code>.
 *
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
	Rectangle rect = image.getBounds();
	int xDisplay = data.display;
	int xGC = OS.XCreateGC(xDisplay, image.pixmap, 0, null);
	if (xGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.XSetSubwindowMode (xDisplay, xGC, OS.IncludeInferiors);
	OS.XCopyArea(xDisplay, data.drawable, image.pixmap, xGC, x, y, rect.width, rect.height, 0, 0);
	OS.XFreeGC(xDisplay, xGC);
}
/**
 * Disposes of the operating system resources associated with
 * the graphics context. Applications must dispose of all GCs
 * which they allocate.
 */
public void dispose () {
	if (handle == 0) return;
	if (data.device.isDisposed()) return;
	
	/* Free resources */
	int clipRgn = data.clipRgn;
	if (clipRgn != 0) OS.XDestroyRegion(clipRgn);
	Image image = data.image;
	if (image != null) {
		image.memGC = null;
		if (image.transparentPixel != -1) image.createMask();
	}

	int renderTable = data.renderTable;
	if (renderTable != 0) OS.XmRenderTableFree(renderTable);

	/* Dispose the GC */
	Device device = data.device;
	drawable.internal_dispose_GC(handle, data);

	data.display = data.drawable = data.colormap = data.fontList = 
		data.clipRgn = data.renderTable = 0;
	drawable = null;
	handle = 0;
	data.image = null;
	data.codePage = null;
	if (device.tracking) device.dispose_Object(this);
	data.device = null;
	data = null;
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
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the width, height or endAngle is zero.</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawArc(int x, int y, int width, int height, int startAngle, int endAngle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || endAngle == 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	OS.XDrawArc(data.display,data.drawable,handle,x,y,width,height,startAngle * 64 ,endAngle * 64);
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
public void drawFocus (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	/*
	* When the drawable is not a widget, the highlight
	* color is zero.
	*/
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	int highlightColor = 0;
	int widget = OS.XtWindowToWidget (xDisplay, xDrawable);
	if (widget != 0) {
		int [] argList = {OS.XmNhighlightColor, 0};
		OS.XtGetValues (widget, argList, argList.length / 2);
		highlightColor = argList [1];
	}

	/* Draw the focus rectangle */
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	XGCValues values = new XGCValues ();
	OS.XGetGCValues (xDisplay, handle, OS.GCForeground, values);
	OS.XSetForeground (xDisplay, handle, highlightColor);
	OS.XDrawRectangle (xDisplay, xDrawable, handle, x, y, width - 1, height - 1);
	OS.XSetForeground (xDisplay, handle, values.foreground);
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
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
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
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
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
	int[] width = new int[1];
	int[] height = new int[1];
	int[] depth = new int[1];
	int[] unused = new int[1];
 	OS.XGetGeometry(data.display, srcImage.pixmap, unused, unused, unused, width, height, unused, depth);
 	int imgWidth = width[0];
 	int imgHeight = height[0];
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
	if (srcImage.alpha != -1 || srcImage.alphaData != null) {
		drawImageAlpha(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, depth[0]);
	} else if (srcImage.transparentPixel != -1 || srcImage.mask != 0) {
		drawImageMask(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, depth[0]);
	} else {
		drawImage(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, depth[0]);
	}
}
void drawImageAlpha(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight, int depth) {
	/* Simple cases */
	if (srcImage.alpha == 0) return;
	if (srcImage.alpha == 255) {
		drawImage(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, depth);
		return;
	}

	/* Check the clipping */
	Rectangle rect = getClipping();
	rect = rect.intersection(new Rectangle(destX, destY, destWidth, destHeight));
	if (rect.isEmpty()) return;
	
	/* Optimization. Recalculate the src and dest rectangles so that
	* only the clipping area is drawn.
	*/
	int sx1 = srcX + (((rect.x - destX) * srcWidth) / destWidth);
	int sx2 = srcX + ((((rect.x + rect.width) - destX) * srcWidth) / destWidth);
	int sy1 = srcY + (((rect.y - destY) * srcHeight) / destHeight);
	int sy2 = srcY + ((((rect.y + rect.height) - destY) * srcHeight) / destHeight);
	destX = rect.x;
	destY = rect.y;
	destWidth = rect.width;
	destHeight = rect.height;
	srcX = sx1;
	srcY = sy1;
	srcWidth = Math.max(1, sx2 - sx1);
	srcHeight = Math.max(1, sy2 - sy1);
	
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	int xDestImagePtr = 0, xSrcImagePtr = 0;
	try {
		/* Get the background pixels */
		xDestImagePtr = OS.XGetImage(xDisplay, xDrawable, destX, destY, destWidth, destHeight, OS.AllPlanes, OS.ZPixmap);
		if (xDestImagePtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		XImage xDestImage = new XImage();
		OS.memmove(xDestImage, xDestImagePtr, XImage.sizeof);
		byte[] destData = new byte[xDestImage.bytes_per_line * xDestImage.height];
		OS.memmove(destData, xDestImage.data, destData.length);	
	
		/* Get the foreground pixels */
		xSrcImagePtr = OS.XGetImage(xDisplay, srcImage.pixmap, srcX, srcY, srcWidth, srcHeight, OS.AllPlanes, OS.ZPixmap);
		if (xSrcImagePtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		XImage xSrcImage = new XImage();
		OS.memmove(xSrcImage, xSrcImagePtr, XImage.sizeof);
		byte[] srcData = new byte[xSrcImage.bytes_per_line * xSrcImage.height];
		OS.memmove(srcData, xSrcImage.data, srcData.length);
		
		/* Compose the pixels */		
		if (xSrcImage.depth <= 8) {
			XColor[] xcolors = data.device.xcolors;
			if (xcolors == null) SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
			byte[] reds = new byte[xcolors.length];
			byte[] greens = new byte[xcolors.length];
			byte[] blues = new byte[xcolors.length];
			for (int i = 0; i < xcolors.length; i++) {
				XColor color = xcolors[i];
				if (color == null) continue;
				reds[i] = (byte)((color.red >> 8) & 0xFF);
				greens[i] = (byte)((color.green >> 8) & 0xFF);
				blues[i] = (byte)((color.blue >> 8) & 0xFF);
			}
			ImageData.blit(ImageData.BLIT_ALPHA,
				srcData, xSrcImage.bits_per_pixel, xSrcImage.bytes_per_line, xSrcImage.byte_order, 0, 0, srcWidth, srcHeight, reds, greens, blues,
				srcImage.alpha, srcImage.alphaData, imgWidth, srcX, srcY,
				destData, xDestImage.bits_per_pixel, xDestImage.bytes_per_line, xDestImage.byte_order, 0, 0, destWidth, destHeight, reds, greens, blues,
				false, false);
		} else {
			int srcRedMask = xSrcImage.red_mask;
			int srcGreenMask = xSrcImage.green_mask;
			int srcBlueMask = xSrcImage.blue_mask;
			int destRedMask = xDestImage.red_mask;
			int destGreenMask = xDestImage.green_mask;
			int destBlueMask = xDestImage.blue_mask;
			
			/*
			* Feature in X.  XGetImage does not retrieve the RGB masks if the drawable
			* is a Pixmap.  The fix is to detect that the masks are not valid and use
			* the default visual masks instead.
			* 
			* NOTE: It is safe to use the default Visual masks, since we always
			* create images with these masks. 
			*/
			int visual = OS.XDefaultVisual(xDisplay, OS.XDefaultScreen(xDisplay));
			Visual xVisual = new Visual();
			OS.memmove(xVisual, visual, Visual.sizeof);
			if (srcRedMask == 0 && srcGreenMask == 0 && srcBlueMask == 0) {
				srcRedMask = xVisual.red_mask;
				srcGreenMask = xVisual.green_mask;
				srcBlueMask = xVisual.blue_mask;
			}
			if (destRedMask == 0 && destGreenMask == 0 && destBlueMask == 0) {
				destRedMask = xVisual.red_mask;
				destGreenMask = xVisual.green_mask;
				destBlueMask = xVisual.blue_mask;
			}
			
			ImageData.blit(ImageData.BLIT_ALPHA,
				srcData, xSrcImage.bits_per_pixel, xSrcImage.bytes_per_line, xSrcImage.byte_order, 0, 0, srcWidth, srcHeight, srcRedMask, srcGreenMask, srcBlueMask,
				srcImage.alpha, srcImage.alphaData, imgWidth, srcX, srcY,
				destData, xDestImage.bits_per_pixel, xDestImage.bytes_per_line, xDestImage.byte_order, 0, 0, destWidth, destHeight, destRedMask, destGreenMask, destBlueMask,
				false, false);
		}
		
		/* Draw the composed pixels */
		OS.memmove(xDestImage.data, destData, destData.length);
		OS.XPutImage(xDisplay, xDrawable, handle, xDestImagePtr, 0, 0, destX, destY, destWidth, destHeight);
	} finally {
		if (xSrcImagePtr != 0) OS.XDestroyImage(xSrcImagePtr);
		if (xDestImagePtr != 0) OS.XDestroyImage(xDestImagePtr);
	}
}
void drawImageMask(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight, int depth) {
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	int colorPixmap = srcImage.pixmap;
	/* Generate the mask if necessary. */
	if (srcImage.transparentPixel != -1) srcImage.createMask();
	int maskPixmap = srcImage.mask;
	int foreground = 0x00000000;
	if (!(simple || (srcWidth == destWidth && srcHeight == destHeight))) {
		/* Stretch the color and mask*/
		int xImagePtr = scalePixmap(xDisplay, colorPixmap, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false, false);
		int xMaskPtr = scalePixmap(xDisplay, maskPixmap, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false, false);

		/* Create color scaled pixmaps */
		colorPixmap = OS.XCreatePixmap(xDisplay, xDrawable, destWidth, destHeight, depth);
		int tempGC = OS.XCreateGC(xDisplay, colorPixmap, 0, null);
		OS.XPutImage(xDisplay, colorPixmap, tempGC, xImagePtr, 0, 0, 0, 0, destWidth, destHeight);
		OS.XDestroyImage(xImagePtr);
		OS.XFreeGC(xDisplay, tempGC);

		/* Create mask scaled pixmaps */
		maskPixmap = OS.XCreatePixmap(xDisplay, xDrawable, destWidth, destHeight, 1);
		tempGC = OS.XCreateGC(xDisplay, maskPixmap, 0, null);
		OS.XPutImage(xDisplay, maskPixmap, tempGC, xMaskPtr, 0, 0, 0, 0, destWidth, destHeight);
		OS.XDestroyImage(xMaskPtr);
		OS.XFreeGC(xDisplay, tempGC);
		
		/* Change the source rectangle */
		srcX = srcY = 0;
		srcWidth = destWidth;
		srcHeight = destHeight;

		foreground = ~foreground;
	}
	
	/* Do the blts */
	XGCValues values = new XGCValues();
	OS.XGetGCValues(xDisplay, handle, OS.GCForeground | OS. GCBackground | OS.GCFunction, values);
	OS.XSetFunction(xDisplay, handle, OS.GXxor);
	OS.XCopyArea(xDisplay, colorPixmap, xDrawable, handle, srcX, srcY, srcWidth, srcHeight, destX, destY);
	OS.XSetForeground(xDisplay, handle, foreground);
	OS.XSetBackground(xDisplay, handle, ~foreground);
	OS.XSetFunction(xDisplay, handle, OS.GXand);
	OS.XCopyPlane(xDisplay, maskPixmap, xDrawable, handle, srcX, srcY, srcWidth, srcHeight, destX, destY, 1);
	OS.XSetFunction(xDisplay, handle, OS.GXxor);
	OS.XCopyArea(xDisplay, colorPixmap, xDrawable, handle, srcX, srcY, srcWidth, srcHeight, destX, destY);
	OS.XSetForeground(xDisplay, handle, values.foreground);
	OS.XSetBackground(xDisplay, handle, values.background);
	OS.XSetFunction(xDisplay, handle, values.function);

	/* Destroy scaled pixmaps */
	if (srcImage.pixmap != colorPixmap) OS.XFreePixmap(xDisplay, colorPixmap);
	if (srcImage.mask != maskPixmap) OS.XFreePixmap(xDisplay, maskPixmap);
	/* Destroy the image mask if the there is a GC created on the image */
	if (srcImage.transparentPixel != -1 && srcImage.memGC != null) srcImage.destroyMask();
}
void drawImage(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight, int depth) {
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	/* Simple case: no stretching */
	if ((srcWidth == destWidth) && (srcHeight == destHeight)) {
		OS.XCopyArea(xDisplay, srcImage.pixmap, xDrawable, handle, srcX, srcY, srcWidth, srcHeight, destX, destY);
		return;
	}
	
	/* Streching case */
	int xImagePtr = scalePixmap(xDisplay, srcImage.pixmap, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false, false);
	OS.XPutImage(xDisplay, xDrawable, handle, xImagePtr, 0, 0, destX, destY, destWidth, destHeight);
	OS.XDestroyImage(xImagePtr);
}
static int scalePixmap(int display, int pixmap, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean flipX, boolean flipY) {
	int xSrcImagePtr = OS.XGetImage(display, pixmap, srcX, srcY, srcWidth, srcHeight, OS.AllPlanes, OS.ZPixmap);
	if (xSrcImagePtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	XImage xSrcImage = new XImage();
	OS.memmove(xSrcImage, xSrcImagePtr, XImage.sizeof);
	byte[] srcData = new byte[xSrcImage.bytes_per_line * xSrcImage.height];
	OS.memmove(srcData, xSrcImage.data, srcData.length);
	int error = 0, xImagePtr = 0;
	int visual = OS.XDefaultVisual(display, OS.XDefaultScreen(display));
	switch (xSrcImage.bits_per_pixel) {
		case 1:
		case 4:
		case 8: {
			int format = xSrcImage.bits_per_pixel == 1 ? OS.XYBitmap : OS.ZPixmap;
			xImagePtr = OS.XCreateImage(display, visual, xSrcImage.depth, format, 0, 0, destWidth, destHeight, xSrcImage.bitmap_pad, 0);
			if (xImagePtr == 0) break;
			XImage xImage = new XImage();
			OS.memmove(xImage, xImagePtr, XImage.sizeof);
			int bufSize = xImage.bytes_per_line * xImage.height;
			int bufPtr = OS.XtMalloc(bufSize);
			xImage.data = bufPtr;
			OS.memmove(xImagePtr, xImage, XImage.sizeof);
			byte[] buf = new byte[bufSize];
			int srcOrder = xSrcImage.bits_per_pixel == 1 ? xSrcImage.bitmap_bit_order : xSrcImage.byte_order;
			int destOrder = xImage.bits_per_pixel == 1 ? xImage.bitmap_bit_order : xImage.byte_order;
			ImageData.blit(ImageData.BLIT_SRC,
				srcData, xSrcImage.bits_per_pixel, xSrcImage.bytes_per_line, srcOrder, 0, 0, srcWidth, srcHeight, null, null, null,
				ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
				buf, xImage.bits_per_pixel, xImage.bytes_per_line, destOrder, 0, 0, destWidth, destHeight, null, null, null,
				flipX, flipY);
			OS.memmove(bufPtr, buf, bufSize);
			break;
		}
		case 16:
		case 24:
		case 32: {
			xImagePtr = OS.XCreateImage(display, visual, xSrcImage.depth, OS.ZPixmap, 0, 0, destWidth, destHeight, xSrcImage.bitmap_pad, 0);
			if (xImagePtr == 0) break;
			XImage xImage = new XImage();
			OS.memmove(xImage, xImagePtr, XImage.sizeof);
			int bufSize = xImage.bytes_per_line * xImage.height;
			int bufPtr = OS.XtMalloc(bufSize);
			xImage.data = bufPtr;
			OS.memmove(xImagePtr, xImage, XImage.sizeof);
			byte[] buf = new byte[bufSize];
			ImageData.blit(ImageData.BLIT_SRC,
				srcData, xSrcImage.bits_per_pixel, xSrcImage.bytes_per_line, xSrcImage.byte_order, 0, 0, srcWidth, srcHeight, 0, 0, 0,
				ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
				buf, xImage.bits_per_pixel, xImage.bytes_per_line, xImage.byte_order, 0, 0, destWidth, destHeight, 0, 0, 0,
				flipX, flipY);
			OS.memmove(bufPtr, buf, bufSize);
			break;
		}
		default:
			error = SWT.ERROR_UNSUPPORTED_DEPTH;
	}
	OS.XDestroyImage(xSrcImagePtr);
	if (xImagePtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (error != 0) {
		if (xImagePtr != 0) OS.XDestroyImage(xImagePtr);
		SWT.error(error);
	}
	return xImagePtr;
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
public void drawLine (int x1, int y1, int x2, int y2) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.XDrawLine (data.display, data.drawable, handle, x1, y1, x2, y2);
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
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	OS.XDrawArc(data.display, data.drawable, handle, x, y, width, height, 0, 23040);
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
	
	// Motif does not have a native drawPolygon() call. Instead we ensure 
	// that the first and last points are the same and call drawPolyline().
	
	int length = pointArray.length;

	// Need at least 3 points to define the polygon. If 2 or fewer points
	// passed in, it is either a line or point so just call drawPolyline().
	// Check what happens when XOR is implemented. We may not be able to
	// do this optimization.
	
	if (length < 4) {
			drawPolyline(pointArray);
			return;
	}

	// If first and last points are the same, the polygon is already closed.
	// Just call drawPolyline().
	//
	// Check what happens when XOR is implemented. We may not be able to
	// do this optimization.
	
	if (pointArray[0] == pointArray[length - 2] && (pointArray[1] == pointArray[length - 1])) {
		drawPolyline(pointArray);
		return;
	}

	// Grow the list of points by one element and make sure the first and last
	// points are the same. This will close the polygon and we can use the
	// drawPolyline() call. 
		
	int newPoints[] = new int[length + 2];
	for (int i = 0; i < length ; i++) 
		newPoints[i] = pointArray[i];
	newPoints[length] = pointArray[0];
	newPoints[length + 1] = pointArray[1];

	drawPolyline(newPoints);
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
	short[] xPoints = new short[pointArray.length];
	for (int i = 0; i<pointArray.length;i++) {
		xPoints[i] = (short) pointArray[i];
	}
	OS.XDrawLines(data.display,data.drawable,handle,xPoints,xPoints.length / 2, OS.CoordModeOrigin);
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
public void drawRectangle (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	OS.XDrawRectangle (data.display, data.drawable, handle, x, y, width, height);
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
public void drawRectangle (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	drawRectangle (rect.x, rect.y, rect.width, rect.height);
}
/** 
 * Draws the outline of the round-cornered rectangle specified by 
 * the arguments, using the receiver's foreground color. The left and
 * right edges of the rectangle are at <code>x</code> and <code>x + width</code>. 
 * The top and bottom edges are at <code>y</code> and <code>y + height</code>.
 * The <em>roundness</em> of the corners is specified by the 
 * <code>arcWidth</code> and <code>arcHeight</code> arguments. 
 *
 * @param x the x coordinate of the rectangle to be drawn
 * @param y the y coordinate of the rectangle to be drawn
 * @param width the width of the rectangle to be drawn
 * @param height the height of the rectangle to be drawn
 * @param arcWidth the horizontal diameter of the arc at the four corners
 * @param arcHeight the vertical diameter of the arc at the four corners
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRoundRectangle (int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);

	// X does not have a native for drawing round rectangles. Do the work in Java
	// and use drawLine() drawArc() calls.
	
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
	if (naw < 0) 
		naw = 0 - naw;
	if (nah < 0)
		nah = 0 - nah;
				
	int naw2 = naw / 2;
	int nah2 = nah / 2;

	int xDisplay = data.display;
	int xDrawable = data.drawable;
	
	if (nw > naw) {
		if (nh > nah) {
			OS.XDrawArc(xDisplay, xDrawable, handle, nx, ny, naw, nah, 5760, 5760);
			OS.XDrawLine(xDisplay, xDrawable, handle, nx + naw2, ny, nx + nw - naw2, ny);
			OS.XDrawArc(xDisplay, xDrawable, handle, nx + nw - naw, ny, naw, nah, 0, 5760);
			OS.XDrawLine(xDisplay, xDrawable, handle, nx + nw, ny + nah2, nx + nw, ny + nh - nah2);
			OS.XDrawArc(xDisplay, xDrawable, handle, nx + nw - naw, ny + nh - nah, naw, nah, 17280, 5760);
			OS.XDrawLine(xDisplay,xDrawable,handle, nx + naw2, ny + nh, nx + nw - naw2, ny + nh);
			OS.XDrawArc(xDisplay, xDrawable, handle, nx, ny + nh - nah, naw, nah, 11520, 5760);
			OS.XDrawLine(xDisplay, xDrawable, handle, nx, ny + nah2, nx, ny + nh - nah2);
		} else {
			OS.XDrawArc(xDisplay, xDrawable, handle, nx, ny, naw, nh, 5760, 11520);
			OS.XDrawLine(xDisplay, xDrawable, handle, nx + naw2, ny, nx + nw - naw2, ny);
			OS.XDrawArc(xDisplay, xDrawable, handle, nx + nw - naw, ny, naw, nh, 17280, 11520);
			OS.XDrawLine(xDisplay,xDrawable,handle, nx + naw2, ny + nh, nx + nw - naw2, ny + nh);
		}
	} else {
		if (nh > nah) {
			OS.XDrawArc(xDisplay, xDrawable, handle, nx, ny, nw, nah, 0, 11520);
			OS.XDrawLine(xDisplay, xDrawable, handle, nx + nw, ny + nah2, nx + nw, ny + nh - nah2);
			OS.XDrawArc(xDisplay, xDrawable, handle, nx, ny + nh - nah, nw, nah, 11520, 11520);
			OS.XDrawLine(xDisplay,xDrawable,handle, nx, ny + nah2, nx, ny + nh - nah2);
		} else {
			OS.XDrawArc(xDisplay, xDrawable, handle, nx, ny, nw, nh, 0, 23040);
		}
	}
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
public void drawString (String string, int x, int y, boolean isTransparent) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
	int xmString = OS.XmStringCreate (buffer, OS.XmFONTLIST_DEFAULT_TAG);
	if (isTransparent) {
		OS.XmStringDraw (data.display, data.drawable, data.fontList, xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null);
	} else {
		OS.XmStringDrawImage (data.display, data.drawable, data.fontList, xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null);
	}			
//	OS.XmStringDrawUnderline (display, drawable, fontList, xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null, 0);
	OS.XmStringFree (xmString);
}
void createRenderTable() {
	int xDisplay = data.display;
	int fontList = data.fontList;	
	/* Get the width of the tabs */
	byte[] buffer = {(byte)' ', 0};
	int xmString = OS.XmStringCreate(buffer, OS.XmFONTLIST_DEFAULT_TAG);
	int tabWidth = OS.XmStringWidth(fontList, xmString) * 8;
	OS.XmStringFree(xmString);
	
	/* Create the tab list */
	int [] tabs = new int[16]; 
	int tab = OS.XmTabCreate(tabWidth, (byte) OS.XmPIXELS, (byte) OS.XmRELATIVE, (byte) OS.XmALIGNMENT_BEGINNING, null);
	for (int i = 0; i < tabs.length; i++) tabs[i] = tab;
	int tabList = OS.XmTabListInsertTabs(0, tabs, tabs.length, 0);	
		
	/* Create a font context to iterate over the elements in the font list */
	int[] fontBuffer = new int[1];
	if (!OS.XmFontListInitFontContext(fontBuffer, fontList)) {
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	int context = fontBuffer[0], fontListEntry = 0;
	int widgetClass = OS.TopLevelShellWidgetClass ();
	int[] renditions = new int[4]; int renditionCount = 0;	
		
	/* Create a rendition for each entry in the font list */
	int shellHandle = OS.XtAppCreateShell (null, null, widgetClass, xDisplay, null, 0);
	while ((fontListEntry = OS.XmFontListNextEntry(context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont(fontListEntry, fontBuffer);
		int fontType = (fontBuffer [0] == 0) ? OS.XmFONT_IS_FONT : OS.XmFONT_IS_FONTSET;
		if (fontPtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int [] argList = {
			OS.XmNtabList, tabList,
			OS.XmNfont, fontPtr,
			OS.XmNfontType, fontType,
		};
		int rendition = OS.XmRenditionCreate(shellHandle, OS.XmFONTLIST_DEFAULT_TAG, argList, argList.length / 2);
		renditions[renditionCount++] = rendition;
		if (renditionCount == renditions.length) {
			int[] newArray = new int[renditions.length + 4];
			System.arraycopy(newArray, 0, renditions, 0, renditionCount);
			renditions = newArray;
		}
	}	
	OS.XmFontListFreeFontContext(context);
	OS.XmTabFree(tab);
	OS.XmTabListFree(tabList);
	OS.XtDestroyWidget (shellHandle);		
	
	/* Create the render table from the renditions */
	data.renderTable = OS.XmRenderTableAddRenditions(0, renditions, renditionCount, OS.XmMERGE_REPLACE);
	for (int i = 0; i < renditionCount; i++) OS.XmRenditionFree(renditions[i]);
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
public void drawText (String string, int x, int y) {
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
public void drawText (String string, int x, int y, boolean isTransparent) {
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
	if (data.renderTable == 0) createRenderTable();
	int renderTable = data.renderTable;

	char mnemonic=0;
	int tableLength = 0;
	Device device = data.device;
	int[] parseTable = new int[2];
	char[] text = new char[string.length()];
	string.getChars(0, text.length, text, 0);
	if ((flags & SWT.DRAW_DELIMITER) != 0) parseTable[tableLength++] = device.crMapping;
	if ((flags & SWT.DRAW_TAB) != 0) parseTable[tableLength++] = device.tabMapping;
	if ((flags & SWT.DRAW_MNEMONIC) != 0) mnemonic = stripMnemonic(text);
	
	String codePage = getCodePage();
	byte[] buffer = Converter.wcsToMbcs(codePage, text, true);
	int xmString = OS.XmStringParseText(buffer, 0, OS.XmFONTLIST_DEFAULT_TAG, OS.XmCHARSET_TEXT, parseTable, tableLength, 0);
	if (mnemonic != 0) {
		byte [] buffer1 = Converter.wcsToMbcs(codePage, new char[]{mnemonic}, true);
		int xmStringUnderline = OS.XmStringCreate (buffer1, OS.XmFONTLIST_DEFAULT_TAG);
		OS.XmStringDrawUnderline(data.display, data.drawable, renderTable, xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null, xmStringUnderline);
		OS.XmStringFree(xmStringUnderline);
	} else {
		if ((flags & SWT.DRAW_TRANSPARENT) != 0) {
			OS.XmStringDraw(data.display, data.drawable, renderTable, xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null);
		} else {
			OS.XmStringDrawImage(data.display, data.drawable, renderTable, xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null);
		}
	}
	OS.XmStringFree(xmString);
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
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the width, height or endAngle is zero.</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawArc
 */
public void fillArc(int x, int y, int width, int height, int startAngle, int endAngle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || endAngle == 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int xDisplay = data.display;
	XGCValues values = new XGCValues ();
	OS.XGetGCValues (xDisplay, handle, OS.GCForeground | OS.GCBackground, values);
	OS.XSetForeground (xDisplay, handle, values.background);
	OS.XFillArc(xDisplay,data.drawable,handle,x,y,width,height,startAngle * 64 ,endAngle * 64);
	OS.XSetForeground (xDisplay, handle, values.foreground);
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
	int xDisplay = data.display;
	int xScreenNum = OS.XDefaultScreen(xDisplay);
	XGCValues values = new XGCValues();
	int fromColor, toColor;
	OS.XGetGCValues(xDisplay, handle, OS.GCForeground | OS.GCBackground, values);
	fromColor = values.foreground;
	toColor = values.background;
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
		final int t = fromColor;
		fromColor = toColor;
		toColor = t;
	}
	if (fromColor == toColor) {
		OS.XFillRectangle(xDisplay, data.drawable, handle, x, y, width, height);
		return;
	}
	/* X Window deals with a virtually limitless array of color formats
	 * but we only distinguish between paletted and direct modes
	 */	
	final int xScreen = OS.XDefaultScreenOfDisplay(xDisplay);
	final int xVisual = OS.XDefaultVisual(xDisplay, xScreenNum);
	Visual visual = new Visual();
	OS.memmove(visual, xVisual, Visual.sizeof);
	final int depth = OS.XDefaultDepthOfScreen(xScreen);
	final boolean directColor = (depth > 8);

	// This code is intentionally commented since elsewhere in SWT we
	// assume that depth <= 8 means we are in a paletted mode though
	// this is not always the case.
	//final boolean directColor = (visual.c_class == OS.TrueColor) || (visual.c_class == OS.DirectColor);

	XColor xColor = new XColor();
	xColor.pixel = fromColor;
	OS.XQueryColor(xDisplay, data.colormap, xColor);
	final RGB fromRGB = new RGB((xColor.red & 0xffff) >>> 8, (xColor.green & 0xffff) >>> 8, (xColor.blue & 0xffff) >>> 8);
	xColor.pixel = toColor;
	OS.XQueryColor(xDisplay, data.colormap, xColor);
	final RGB toRGB = new RGB((xColor.red & 0xffff) >>> 8, (xColor.green & 0xffff) >>> 8, (xColor.blue & 0xffff) >>> 8);

	final int redBits, greenBits, blueBits;
	if (directColor) {
		// RGB mapped display
		redBits = getChannelWidth(visual.red_mask);
		greenBits = getChannelWidth(visual.green_mask);
		blueBits = getChannelWidth(visual.blue_mask);
	} else {
		// Index display
		redBits = greenBits = blueBits = 0;
	}
	ImageData.fillGradientRectangle(this, data.device,
		x, y, width, height, vertical, fromRGB, toRGB,
		redBits, greenBits, blueBits);
}

/**
 * Computes the required channel width (depth) from a mask.
 */
static int getChannelWidth(int mask) {
	int width = 0;
	while (mask != 0) {
		width += (mask & 1);
		mask >>>= 1;
	}
	return width;
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
public void fillOval (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	int display = data.display;
	XGCValues values = new XGCValues ();
	OS.XGetGCValues (display, handle, OS.GCForeground | OS.GCBackground, values);
	OS.XSetForeground (display, handle, values.background);
	OS.XFillArc (display, data.drawable, handle, x, y, width, height, 0, 23040);
	OS.XSetForeground (display, handle, values.foreground);
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
	short[] xPoints = new short[pointArray.length];
	for (int i = 0; i<pointArray.length;i++) {
		xPoints[i] = (short) pointArray[i];
	}
	int xDisplay = data.display;
	XGCValues values = new XGCValues ();
	OS.XGetGCValues (xDisplay, handle, OS.GCForeground | OS.GCBackground, values);
	OS.XSetForeground (xDisplay, handle, values.background);
	OS.XFillPolygon(xDisplay, data.drawable, handle,xPoints, xPoints.length / 2, OS.Complex, OS.CoordModeOrigin);
	OS.XSetForeground (xDisplay, handle, values.foreground);
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
public void fillRectangle (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	int xDisplay = data.display;
	XGCValues values = new XGCValues ();
	OS.XGetGCValues (xDisplay, handle, OS.GCForeground | OS.GCBackground, values);
	OS.XSetForeground (xDisplay, handle, values.background);
	OS.XFillRectangle (xDisplay, data.drawable, handle, x, y, width, height);
	OS.XSetForeground (xDisplay, handle, values.foreground);
}
/** 
 * Fills the interior of the specified rectangle, using the receiver's
 * background color. 
 *
 * @param rectangle the rectangle to be filled
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
public void fillRectangle (Rectangle rect) {
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
 * @param arcWidth the horizontal diameter of the arc at the four corners
 * @param arcHeight the vertical diameter of the arc at the four corners
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRoundRectangle
 */
public void fillRoundRectangle (int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
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
		ny = ny -nh;
	}
	if (naw < 0) 
		naw = 0 - naw;
	if (nah < 0)
		nah = 0 - nah;

	int naw2 = naw / 2;
	int nah2 = nah / 2;

	int xDisplay = data.display;
	int xDrawable = data.drawable;
	XGCValues values = new XGCValues ();
	OS.XGetGCValues(xDisplay, handle, OS.GCForeground | OS.GCBackground, values);
	OS.XSetForeground(xDisplay, handle, values.background);

	if (nw > naw) {
		if (nh > nah) {
			OS.XFillArc(xDisplay, xDrawable, handle, nx, ny, naw, nah, 5760, 5760);
			OS.XFillRectangle(xDisplay, xDrawable, handle, nx + naw2, ny, nw - naw, nah2);
			OS.XFillArc(xDisplay, xDrawable, handle, nx + nw - naw, ny, naw, nah, 0, 5760);
			OS.XFillRectangle(xDisplay, xDrawable, handle, nx, ny + nah2, nw, nh - nah);
			OS.XFillArc(xDisplay, xDrawable, handle, nx + nw - naw, ny + nh - nah, naw, nah, 17280, 5760);
			OS.XFillRectangle(xDisplay, xDrawable, handle, nx + naw2, ny + nh - nah2, nw - naw, nah2);
			OS.XFillArc(xDisplay, xDrawable, handle, nx, ny + nh - nah, naw, nah, 11520, 5760);
		} else {
			OS.XFillArc(xDisplay, xDrawable, handle, nx, ny, naw, nh, 5760, 11520);
			OS.XFillRectangle(xDisplay, xDrawable, handle, nx + naw2, ny, nw - naw, nh);
			OS.XFillArc(xDisplay, xDrawable, handle, nx + nw - naw, ny, naw, nh, 17280, 11520);
		}
	} else {
		if (nh > nah) {
			OS.XFillArc(xDisplay, xDrawable, handle, nx, ny, nw, nah, 0, 11520);
			OS.XFillRectangle(xDisplay, xDrawable, handle, nx, ny + nah2, nw, nh - nah);
			OS.XFillArc(xDisplay, xDrawable, handle, nx, ny + nh - nah, nw, nah, 11520, 11520);
		} else {
			OS.XFillArc(xDisplay, xDrawable, handle, nx, ny, nw, nh, 0, 23040);
		}
	}
	OS.XSetForeground(xDisplay, handle, values.foreground);
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
	int fontList  = data.fontList;
	byte[] charBuffer = Converter.wcsToMbcs(getCodePage (), new char[] { ch }, false);
	int val = charBuffer[0] & 0xFF;
	/* Create a font context to iterate over each element in the font list */
	int[] buffer = new int[1];
	if (!OS.XmFontListInitFontContext(buffer, fontList)) {
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	int context = buffer[0];
	XFontStruct fontStruct = new XFontStruct();
	XCharStruct charStruct = new XCharStruct();
	int fontListEntry;
	int[] fontStructPtr = new int[1];
	int[] fontNamePtr = new int[1];
	/* Go through each entry in the font list. */
	while ((fontListEntry = OS.XmFontListNextEntry(context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont(fontListEntry, buffer);
		if (buffer[0] == 0) {
			OS.memmove(fontStruct, fontPtr, XFontStruct.sizeof);
			/* FontList contains a single font */
			if (fontStruct.min_byte1 == 0 && fontStruct.max_byte1 == 0) {
				/* Single byte fontStruct */
				if (fontStruct.min_char_or_byte2 <= val && val <= fontStruct.max_char_or_byte2) {
					/* The font contains the character */
					int charWidth = 0;
					int perCharPtr = fontStruct.per_char;	
					if (perCharPtr == 0) {
						/*
						 * If perCharPtr is 0 then all glyphs in the font have
						 * the same width as the font's maximum width.
						 */
						 charWidth = fontStruct.max_bounds_width;
					} else {
						OS.memmove(charStruct, perCharPtr + ((val - fontStruct.min_char_or_byte2) * XCharStruct.sizeof), XCharStruct.sizeof);
						charWidth = charStruct.width;
					} 
					if (charWidth != 0) {
						OS.XmFontListFreeFontContext(context);
						return charWidth;
					}
				}
			} else {
				/* Double byte fontStruct */
				int charsPerRow = fontStruct.max_char_or_byte2 - fontStruct.min_char_or_byte2 + 1;
				int row = 0;
				if (charBuffer.length > 1) row = charBuffer[1] - fontStruct.min_byte1;
				int col = charBuffer[0] - fontStruct.min_char_or_byte2;
				if (row <= fontStruct.max_byte1 && col <= fontStruct.max_char_or_byte2) {
					/* The font contains the character */
					int charWidth = 0;
					int perCharPtr = fontStruct.per_char;	
					if (perCharPtr == 0) {
						/*
						 * If perCharPtr is 0 then all glyphs in the font have
						 * the same width as the font's maximum width.
						 */
						 charWidth = fontStruct.max_bounds_width;
					} else {
						int offset = row * charsPerRow + col;
						OS.memmove(charStruct, perCharPtr + offset * XCharStruct.sizeof, XCharStruct.sizeof);
						charWidth = charStruct.width;
					}
					if (charWidth != 0) {
						OS.XmFontListFreeFontContext(context);
						return charWidth;
					}
				}
			} 
		} else { 
			/* FontList contains a fontSet */
			int nFonts = OS.XFontsOfFontSet(fontPtr, fontStructPtr, fontNamePtr);
			int [] fontStructs = new int[nFonts];
			OS.memmove(fontStructs, fontStructPtr[0], nFonts * 4);
			/* Go through each fontStruct in the font set */
			for (int i = 0; i < nFonts; i++) { 
				OS.memmove(fontStruct, fontStructs[i], XFontStruct.sizeof);
				if (fontStruct.min_byte1 == 0 && fontStruct.max_byte1 == 0) {
					/* Single byte fontStruct */
					if (fontStruct.min_char_or_byte2 <= val && val <= fontStruct.max_char_or_byte2) {
						/* The font contains the character */
						int charWidth = 0;
						int perCharPtr = fontStruct.per_char;	
						if (perCharPtr == 0) {
							/*
							 * If perCharPtr is 0 then all glyphs in the font have
							 * the same width as the font's maximum width.
							 */
							 charWidth = fontStruct.max_bounds_width;
						} else {
							OS.memmove(charStruct, perCharPtr + (val - fontStruct.min_char_or_byte2 * XCharStruct.sizeof), XCharStruct.sizeof);
							charWidth = charStruct.width;
						}
						if (charWidth != 0) {
							OS.XmFontListFreeFontContext(context);
							return charWidth;
						}
					}
				} else {
					/* Double byte fontStruct */
					int charsPerRow = fontStruct.max_char_or_byte2 - fontStruct.min_char_or_byte2 + 1;
					int row = 0;
					if (charBuffer.length > 1) row = charBuffer[1] - fontStruct.min_byte1;
					int col = charBuffer[0] - fontStruct.min_char_or_byte2;
					if (row <= fontStruct.max_byte1 && col <= fontStruct.max_char_or_byte2) {
						/* The font contains the character */
						int charWidth = 0;
						int perCharPtr = fontStruct.per_char;	
						if (perCharPtr == 0) {
							/*
							 * If perCharPtr is 0 then all glyphs in the font have
							 * the same width as the font's maximum width.
							 */
							 charWidth = fontStruct.max_bounds_width;
						} else {
							int offset = row * charsPerRow + col;
							OS.memmove(charStruct, perCharPtr + offset * XCharStruct.sizeof, XCharStruct.sizeof);
							charWidth = charStruct.width;
						}
						if (charWidth != 0) {
							OS.XmFontListFreeFontContext(context);
							return charWidth;
						}
					}
				} 
			}
		}
	}
	OS.XmFontListFreeFontContext(context);
	return 0;
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
	int xDisplay = data.display;
	XGCValues values = new XGCValues();
	OS.XGetGCValues(xDisplay, handle, OS.GCBackground, values);
	XColor xColor = new XColor();
	xColor.pixel = values.background;
	OS.XQueryColor(xDisplay,data.colormap,xColor);
	return Color.motif_new(data.device, xColor);
	
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
	int fontList = data.fontList;
	byte[] charBuffer = Converter.wcsToMbcs(getCodePage (), new char[] { ch }, false);
	int val = charBuffer[0] & 0xFF;
	/* Create a font context to iterate over each element in the font list */
	int[] buffer = new int[1];
	if (!OS.XmFontListInitFontContext(buffer, fontList)) {
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	int context = buffer[0];
	XFontStruct fontStruct = new XFontStruct();
	XCharStruct charStruct = new XCharStruct();
	int fontListEntry;
	int[] fontStructPtr = new int[1];
	int[] fontNamePtr = new int[1];
	/* Go through each entry in the font list. */
	while ((fontListEntry = OS.XmFontListNextEntry(context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont(fontListEntry, buffer);
		if (buffer[0] == 0) {
			OS.memmove(fontStruct, fontPtr, XFontStruct.sizeof);
			/* FontList contains a single font */
			if (fontStruct.min_byte1 == 0 && fontStruct.max_byte1 == 0) {
				/* Single byte fontStruct */
				if (fontStruct.min_char_or_byte2 <= val && val <= fontStruct.max_char_or_byte2) {
					/* The font contains the character */
					int charWidth = 0;
					int lBearing = 0;
					int rBearing = 0;
					int perCharPtr = fontStruct.per_char;	
					if (perCharPtr == 0) {
						/*
						 * If perCharPtr is 0 then all glyphs in the font have
						 * the same width and left/right bearings as the font.
						 */
						charWidth = fontStruct.max_bounds_width;
						lBearing = fontStruct.min_bounds_lbearing;
						rBearing = fontStruct.max_bounds_rbearing;
					} else {
						OS.memmove(charStruct, perCharPtr + ((val - fontStruct.min_char_or_byte2) * XCharStruct.sizeof), XCharStruct.sizeof);
						charWidth = charStruct.width;
						lBearing = charStruct.lbearing;
						rBearing = charStruct.rbearing;
					}
					if (charWidth != 0) {
						OS.XmFontListFreeFontContext(context);
						return rBearing - lBearing;
					}
				}
			} else {
				/* Double byte fontStruct */
				int charsPerRow = fontStruct.max_char_or_byte2 - fontStruct.min_char_or_byte2 + 1;
				int row = 0;
				if (charBuffer.length > 1) row = charBuffer[1] - fontStruct.min_byte1;
				int col = charBuffer[0] - fontStruct.min_char_or_byte2;
				if (row <= fontStruct.max_byte1 && col <= fontStruct.max_char_or_byte2) {
					/* The font contains the character */
					int charWidth = 0;
					int lBearing = 0;
					int rBearing = 0;
					int perCharPtr = fontStruct.per_char;	
					if (perCharPtr == 0) {
						/*
						 * If perCharPtr is 0 then all glyphs in the font have
						 * the same width and left/right bearings as the font.
						 */
						charWidth = fontStruct.max_bounds_width;
						lBearing = fontStruct.min_bounds_lbearing;
						rBearing = fontStruct.max_bounds_rbearing;
					} else {
						int offset = row * charsPerRow + col;
						OS.memmove(charStruct, perCharPtr + offset * XCharStruct.sizeof, XCharStruct.sizeof);
						charWidth = charStruct.width;
						lBearing = charStruct.lbearing;
						rBearing = charStruct.rbearing;
					}
					if (charWidth != 0) {
						OS.XmFontListFreeFontContext(context);
						return rBearing - lBearing;
					}
				}
			} 
		} else { 
			/* FontList contains a fontSet */
			int nFonts = OS.XFontsOfFontSet(fontPtr, fontStructPtr, fontNamePtr);
			int [] fontStructs = new int[nFonts];
			OS.memmove(fontStructs, fontStructPtr[0], nFonts * 4);
			/* Go through each fontStruct in the font set */
			for (int i = 0; i < nFonts; i++) { 
				OS.memmove(fontStruct, fontStructs[i], XFontStruct.sizeof);
				if (fontStruct.min_byte1 == 0 && fontStruct.max_byte1 == 0) {
					/* Single byte fontStruct */
					if (fontStruct.min_char_or_byte2 <= val && val <= fontStruct.max_char_or_byte2) {
						/* The font contains the character */
						int charWidth = 0;
						int lBearing = 0;
						int rBearing = 0;
						int perCharPtr = fontStruct.per_char;	
						if (perCharPtr == 0) {
							/*
							 * If perCharPtr is 0 then all glyphs in the font have
							 * the same width and left/right bearings as the font.
							 */
							charWidth = fontStruct.max_bounds_width;
							lBearing = fontStruct.min_bounds_lbearing;
							rBearing = fontStruct.max_bounds_rbearing;
						} else {
							OS.memmove(charStruct, perCharPtr + (val - fontStruct.min_char_or_byte2 * XCharStruct.sizeof), XCharStruct.sizeof);
							charWidth = charStruct.width;
							lBearing = charStruct.lbearing;
							rBearing = charStruct.rbearing;
						}
						if (charWidth != 0) {
							OS.XmFontListFreeFontContext(context);
							return rBearing - lBearing;
						}
					}
				} else {
					/* Double byte fontStruct */
					int charsPerRow = fontStruct.max_char_or_byte2 - fontStruct.min_char_or_byte2 + 1;
					int row = 0;
					if (charBuffer.length > 1) row = charBuffer[1] - fontStruct.min_byte1;
					int col = charBuffer[0] - fontStruct.min_char_or_byte2;
					if (row <= fontStruct.max_byte1 && col <= fontStruct.max_char_or_byte2) {
						/* The font contains the character */
						int charWidth = 0;
						int lBearing = 0;
						int rBearing = 0;
						int perCharPtr = fontStruct.per_char;	
						if (perCharPtr == 0) {
							/*
							 * If perCharPtr is 0 then all glyphs in the font have
							 * the same width and left/right bearings as the font.
							 */
							charWidth = fontStruct.max_bounds_width;
							lBearing = fontStruct.min_bounds_lbearing;
							rBearing = fontStruct.max_bounds_rbearing;
						} else {
							int offset = row * charsPerRow + col;
							OS.memmove(charStruct, perCharPtr + offset * XCharStruct.sizeof, XCharStruct.sizeof);
							charWidth = charStruct.width;
							lBearing = charStruct.lbearing;
							rBearing = charStruct.rbearing;
						}
						if (charWidth != 0) {
							OS.XmFontListFreeFontContext(context);
							return rBearing - lBearing;
						}
					}
				} 
			}
		}
	}
	OS.XmFontListFreeFontContext(context);
	return 0;
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
	int clipRgn = data.clipRgn;
	if (clipRgn == 0) {
		int[] width = new int[1]; int[] height = new int[1];
		int[] unused = new int[1];
		OS.XGetGeometry(data.display, data.drawable, unused, unused, unused, width, height, unused, unused);
		return new Rectangle(0, 0, width[0], height[0]);
	}
	XRectangle rect = new XRectangle();
	OS.XClipBox(clipRgn, rect);
	return new Rectangle(rect.x, rect.y, rect.width, rect.height);
}
/** 
 * Sets the region managed by the argument to the current
 * clipping region of the receiver.
 *
 * @param region the region to fill with the clipping region
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the region is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void getClipping(Region region) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int hRegion = region.handle;
	int clipRgn = data.clipRgn;
	if (clipRgn == 0) {
		int[] width = new int[1]; int[] height = new int[1];
		int[] unused = new int[1];
		OS.XGetGeometry(data.display, data.drawable, unused, unused, unused, width, height, unused, unused);
		OS.XSubtractRegion (hRegion, hRegion, hRegion);
		XRectangle rect = new XRectangle();
		rect.x = 0; rect.y = 0;
		rect.width = (short)width[0]; rect.height = (short)height[0];
		OS.XUnionRectWithRegion(rect, hRegion, hRegion);
		return;
	}
	OS.XSubtractRegion (hRegion, hRegion, hRegion);
	OS.XUnionRegion (clipRgn, hRegion, hRegion);
}
String getCodePage () {
	return data.codePage;
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
public Font getFont () {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return Font.motif_new(data.device, data.fontList);
}
int getFontHeight () {
	int fontList = data.fontList;
	/* Create a font context to iterate over each element in the font list */
	int [] buffer = new int [1];
	if (!OS.XmFontListInitFontContext (buffer, fontList)) {
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	int context = buffer [0];
	
	/* Values discovering during iteration */
	int height = 0;
	XFontStruct fontStruct = new XFontStruct ();
	int fontListEntry;
	int [] fontStructPtr = new int [1];
	int [] fontNamePtr = new int [1];
	
	/* Go through each entry in the font list. */
	while ((fontListEntry = OS.XmFontListNextEntry (context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont (fontListEntry, buffer);
		if (buffer [0] == 0) { 
			/* FontList contains a single font */
			OS.memmove (fontStruct, fontPtr, XFontStruct.sizeof);
			int fontHeight = fontStruct.ascent + fontStruct.descent;
			if (fontHeight > height) height = fontHeight;
		} else {
			/* FontList contains a fontSet */
			int nFonts = OS.XFontsOfFontSet (fontPtr, fontStructPtr, fontNamePtr);
			int [] fontStructs = new int [nFonts];
			OS.memmove (fontStructs, fontStructPtr [0], nFonts * 4);
			
			/* Go through each fontStruct in the font set */
			for (int i=0; i<nFonts; i++) { 
				OS.memmove (fontStruct, fontStructs[i], XFontStruct.sizeof);
				int fontHeight = fontStruct.ascent + fontStruct.descent;
				if (fontHeight > height) height = fontHeight;
			}
		}
	}
	
	OS.XmFontListFreeFontContext (context);
	return height;
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
	int xDisplay = data.display;
	int fontList = data.fontList;
	/* Create a font context to iterate over each element in the font list */
	int[] buffer = new int[1];
	if (!OS.XmFontListInitFontContext(buffer, fontList)) {
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	int context = buffer[0];
	/* Values discovering during iteration */
	int ascent = 0;
	int descent = 0;
	int averageCharWidth = 0, numAverageCharWidth = 0;
	int leading = 0;
	int height = 0;
		
	XFontStruct fontStruct = new XFontStruct();
	int fontListEntry;
	int[] fontStructPtr = new int[1];
	int[] fontNamePtr = new int[1];
	/* Go through each entry in the font list. */
	while ((fontListEntry = OS.XmFontListNextEntry(context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont(fontListEntry, buffer);
		if (buffer[0] == 0) { 
			/* FontList contains a single font */
			OS.memmove(fontStruct, fontPtr, XFontStruct.sizeof);
			ascent = ascent > fontStruct.max_bounds_ascent ? ascent : fontStruct.max_bounds_ascent;
			descent = descent > fontStruct.descent ? descent : fontStruct.descent;
			int tmp = fontStruct.ascent + fontStruct.descent;
			height = height > tmp ? height : tmp;
			tmp = fontStruct.ascent - fontStruct.max_bounds_ascent;
			leading = leading > tmp ? leading : tmp;
			/* Calculate average character width */
			int propPtr = fontStruct.properties;
			for (int i = 0; i < fontStruct.n_properties; i++) {
				/* Reef through properties looking for XAFONT */
				int[] prop = new int[2];
				OS.memmove(prop, propPtr, 8);
				if (prop[0] == OS.XA_FONT) {
					/* Found it, prop[1] points to the string */
					StringBuffer stringBuffer = new StringBuffer();
					int ptr = OS.XmGetAtomName(xDisplay, prop[1]);
					int strPtr = ptr;
					byte[] c = new byte[1];
					OS.memmove(c, strPtr, 1);
					while (c[0] != 0) {
						stringBuffer.append((char)c[0]);
						strPtr++;
						OS.memmove(c, strPtr, 1);
					}
					String xlfd = stringBuffer.toString().toLowerCase();
					int avg = FontData.motif_new(xlfd).averageWidth / 10;
					OS.XtFree(ptr);
					if (avg == 0) {
						/*
						 * Not all fonts have average character width encoded
						 * in the xlfd. This one doesn't, so do it the hard
						 * way by averaging all the character widths.
						 */						
						int perCharPtr = fontStruct.per_char;	
						if (perCharPtr == 0) {
							/*
							 * If perCharPtr is 0 then all glyphs in the font have
							 * the same width as the font's maximum width.  So no
							 * averaging is required.
							 */
							 averageCharWidth = fontStruct.max_bounds_width;
						} else {
							int sum = 0, count = 0;
							int cols = fontStruct.max_char_or_byte2 - fontStruct.min_char_or_byte2 + 1;
							XCharStruct struct = new XCharStruct();
							for (int index = 0; index < cols; index++) {
								OS.memmove(struct, perCharPtr + (index * XCharStruct.sizeof), XCharStruct.sizeof);
								int w = struct.width;
								if (w != 0) {
									sum += w;
									count++;
								}
							}
							averageCharWidth += sum / count;
						}
					} else {
						/* Average character width was in the xlfd */
						averageCharWidth += avg;
					}
					numAverageCharWidth++;
					break;
				}
				propPtr += 8;
			}
		}
		else { 
			/* FontList contains a fontSet */
			int nFonts = OS.XFontsOfFontSet(fontPtr, fontStructPtr, fontNamePtr);
			int [] fontStructs = new int[nFonts];
			OS.memmove(fontStructs, fontStructPtr[0], nFonts * 4);
			/* Go through each fontStruct in the font set */
			for (int i = 0; i < nFonts; i++) { 
				OS.memmove(fontStruct, fontStructs[i], XFontStruct.sizeof);
				ascent = ascent > fontStruct.max_bounds_ascent ? ascent : fontStruct.max_bounds_ascent;
				descent = descent > fontStruct.descent ? descent : fontStruct.descent;
				int tmp = fontStruct.ascent + fontStruct.descent;
				height = height > tmp ? height : tmp;
				tmp = fontStruct.ascent - fontStruct.max_bounds_ascent;
				leading = leading > tmp ? leading : tmp;
				/* Calculate average character width */
				int propPtr = fontStruct.properties;
				for (int j = 0; j < fontStruct.n_properties; j++) {
					/* Reef through properties looking for XAFONT */
					int[] prop = new int[2];
					OS.memmove(prop, propPtr, 8);
					if (prop[0] == OS.XA_FONT) {
						/* Found it, prop[1] points to the string */
						StringBuffer stringBuffer = new StringBuffer();
						int ptr = OS.XmGetAtomName(xDisplay, prop[1]);
						int strPtr = ptr;
						byte[] c = new byte[1];
						OS.memmove(c, strPtr, 1);
						while (c[0] != 0) {
							stringBuffer.append((char)c[0]);
							strPtr++;
							OS.memmove(c, strPtr, 1);
						}
						String xlfd = stringBuffer.toString().toLowerCase();
						int avg = FontData.motif_new(xlfd).averageWidth / 10;
						OS.XFree(ptr);
						if (avg == 0) {
							/*
							 * Not all fonts have average character width encoded
							 * in the xlfd. This one doesn't, so do it the hard
							 * way by averaging all the character widths.
							 */
							int perCharPtr = fontStruct.per_char;
							if (perCharPtr == 0) {
								/*
								 * If perCharPtr is 0 then all glyphs in the font have
								 * the same width as the font's maximum width.  So no
								 * averaging is required.
								 */
								 averageCharWidth = fontStruct.max_bounds_width;
							} else {
								int sum = 0, count = 0;
								int cols = fontStruct.max_char_or_byte2 - fontStruct.min_char_or_byte2 + 1;
								XCharStruct struct = new XCharStruct();
								for (int index = 0; index < cols; index++) {
									OS.memmove(struct, perCharPtr + (index * XCharStruct.sizeof), XCharStruct.sizeof);
									int w = struct.width;
									if (w != 0) {
										sum += w;
										count++;
									}
								}
								averageCharWidth += sum / count;
							}
						} else {
							/* Average character width was in the xlfd */
							averageCharWidth += avg;
						}
						numAverageCharWidth++;
						break;
					}
					propPtr += 8;
				}
			}
		}
	}
	OS.XmFontListFreeFontContext(context);
	return FontMetrics.motif_new(ascent, descent, averageCharWidth / numAverageCharWidth, leading, height);
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
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int xDisplay = data.display;
	XGCValues values = new XGCValues();
	OS.XGetGCValues(xDisplay, handle, OS.GCForeground, values);
	XColor xColor = new XColor();
	xColor.pixel = values.foreground;
	OS.XQueryColor(xDisplay,data.colormap,xColor);
	return Color.motif_new(data.device, xColor);
	
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
	XGCValues values = new XGCValues();
	OS.XGetGCValues(data.display, handle, OS.GCLineWidth, values);
	return values.line_width;
}
public int getStyle () {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.style;
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
	XGCValues values = new XGCValues ();
	OS.XGetGCValues (data.display, handle, OS.GCFunction, values);
	return values.function == OS.GXxor;
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
public int hashCode () {
	return handle;
}
void init(Drawable drawable, GCData data, int xGC) {
	int xDisplay = data.display;
	int foreground = data.foreground;
	if (foreground != -1) OS.XSetForeground (xDisplay, xGC, foreground);
	int background = data.background;
	if (background != -1) OS.XSetBackground (xDisplay, xGC, background);
	Image image = data.image;
	if (image != null) {
		image.memGC = this;
		/*
		 * The transparent pixel mask might change when drawing on
		 * the image.  Destroy it so that it is regenerated when
		 * necessary.
		 */
		if (image.transparentPixel != -1) image.destroyMask();
	}
	this.drawable = drawable;
	this.data = data;
	handle = xGC;
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
public static GC motif_new(Drawable drawable, GCData data) {
	GC gc = new GC();
	int xGC = drawable.internal_new_GC(data);
	gc.init(drawable, data, xGC);
	return gc;
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
public void setBackground (Color color) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.XSetBackground(data.display, handle, color.handle.pixel);
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
public void setClipping (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int clipRgn = data.clipRgn;
	if (clipRgn == 0) {
		data.clipRgn = clipRgn = OS.XCreateRegion ();
	} else {
		OS.XSubtractRegion (clipRgn, clipRgn, clipRgn);
	}
	XRectangle rect = new XRectangle ();
	rect.x = (short) x;  rect.y = (short) y;
	rect.width = (short) width;  rect.height = (short) height;
	OS.XSetClipRectangles (data.display, handle, 0, 0, rect, 1, OS.Unsorted);
	OS.XUnionRectWithRegion(rect, clipRgn, clipRgn);
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
public void setClipping (Rectangle rect) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) {
		OS.XSetClipMask (data.display, handle, OS.None);
		int clipRgn = data.clipRgn;
		if (clipRgn != 0) {
			OS.XDestroyRegion (clipRgn);
			data.clipRgn = 0;
		}
		return;
	}
	setClipping (rect.x, rect.y, rect.width, rect.height);
}
/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the region specified
 * by the argument.
 *
 * @param rect the clipping region.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping (Region region) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int clipRgn = data.clipRgn;
	if (region == null) {
		OS.XSetClipMask (data.display, handle, OS.None);
		if (clipRgn != 0) {
			OS.XDestroyRegion (clipRgn);
			data.clipRgn = 0;
		}
	} else {
		if (clipRgn == 0) {
			data.clipRgn = clipRgn = OS.XCreateRegion ();
		} else {
			OS.XSubtractRegion (clipRgn, clipRgn, clipRgn);
		}
		OS.XUnionRegion (region.handle, clipRgn, clipRgn);
		OS.XSetRegion (data.display, handle, region.handle);
	}
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
public void setFont (Font font) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (font == null) font = data.device.systemFont;
	if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.fontList = font.handle;
	data.codePage = font.codePage;
	if (data.renderTable != 0) OS.XmRenderTableFree(data.renderTable);
	data.renderTable = 0;
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
public void setForeground (Color color) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.XSetForeground(data.display, handle, color.handle.pixel);
}
/** 
 * Sets the receiver's line style to the argument, which must be one
 * of the constants <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
 * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
 * <code>SWT.LINE_DASHDOTDOT</code>.
 *
 * @param lineStyle the style to be used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setLineStyle(int lineStyle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int xDisplay = data.display;
	switch (lineStyle) {
		case SWT.LINE_SOLID:
			data.lineStyle = lineStyle;
			OS.XSetLineAttributes(xDisplay, handle, 0, OS.LineSolid, OS.CapButt, OS.JoinMiter);
			return;
		case SWT.LINE_DASH:
			OS.XSetDashes(xDisplay,handle,0, new byte[] {6, 2},2);
			break;
		case SWT.LINE_DOT:
			OS.XSetDashes(xDisplay,handle,0, new byte[] {3, 1},2);
			break;
		case SWT.LINE_DASHDOT:
			OS.XSetDashes(xDisplay,handle,0, new byte[] {6, 2, 3, 1},4);
			break;
		case SWT.LINE_DASHDOTDOT:
			OS.XSetDashes(xDisplay,handle,0, new byte[] {6, 2, 3, 1, 3, 1},6);
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineStyle = lineStyle;
	OS.XSetLineAttributes(xDisplay, handle, 0, OS.LineOnOffDash, OS.CapButt, OS.JoinMiter);
	
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
	if (data.lineStyle == SWT.LINE_SOLID) {
		OS.XSetLineAttributes(data.display, handle, width, OS.LineSolid, OS.CapButt, OS.JoinMiter);
	} else {
		OS.XSetLineAttributes(data.display, handle, width, OS.LineDoubleDash, OS.CapButt, OS.JoinMiter);
	}
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
	OS.XSetFunction(data.display, handle, xor ? OS.GXxor : OS.GXcopy);
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
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (string.length () == 0) return new Point(0, getFontHeight());
	byte[] buffer = Converter.wcsToMbcs(getCodePage (), string, true);
	int xmString = OS.XmStringCreate(buffer, OS.XmFONTLIST_DEFAULT_TAG);
	int fontList = data.fontList;
	int width = OS.XmStringWidth(fontList, xmString);
	int height = OS.XmStringHeight(fontList, xmString);
	OS.XmStringFree(xmString);
	return new Point(width, height);
}
char stripMnemonic(char[] text) {
	char mnemonic=0;
	int i=0, j=0;
	while (i < text.length) {
		if ((text [j++] = text [i++]) == '&') {
			if (i == text.length) {continue;}
			if (text [i] == '&') {i++; continue;}
			if (mnemonic == 0) mnemonic = text [i];
			j--;
		}
	}
	while (j < text.length) text [j++] = 0;
	return mnemonic;
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
	if (string.length () == 0) return new Point(0, getFontHeight());
	if (data.renderTable == 0) createRenderTable();
	int renderTable = data.renderTable;

	int tableLength = 0;
	Device device = data.device;
	int[] parseTable = new int[2];
	char[] text = new char[string.length()];
	string.getChars(0, text.length, text, 0);	
	if ((flags & SWT.DRAW_DELIMITER) != 0) parseTable[tableLength++] = device.crMapping;
	if ((flags & SWT.DRAW_TAB) != 0) parseTable[tableLength++] = device.tabMapping;
	if ((flags & SWT.DRAW_MNEMONIC) != 0) stripMnemonic(text);	

	byte[] buffer = Converter.wcsToMbcs(getCodePage(), text, true);
	int xmString = OS.XmStringParseText(buffer, 0, OS.XmFONTLIST_DEFAULT_TAG, OS.XmCHARSET_TEXT, parseTable, tableLength, 0);
	int width = OS.XmStringWidth(renderTable, xmString);
	int height =  OS.XmStringHeight(renderTable, xmString);
	OS.XmStringFree(xmString);
	return new Point(width, height);
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

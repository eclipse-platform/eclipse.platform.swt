package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;
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
	public int handle;	// a Mac CGrafPort
	
	Drawable drawable;
	GCData data;
		
	//---- AW
	private MacRect fRect= new MacRect();
	private int[] fSavePort= new int[1];
	private int[] fSaveGWorld= new int[1];
	private int fSaveClip= OS.NewRgn();
	private boolean fIsFocused= false;
	private int fLineWidth= 1;
	private boolean fXorMode= false;
	private int fClipCache;
	private short fOffsetX;
	private short fOffsetY;
	//---- AW

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
public GC (Drawable drawable) {
	if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	GCData data = new GCData();
	int xGC = drawable.internal_new_GC(data);
	init(drawable, data, xGC);
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
		
	focus(true);
	
	Rectangle src= new Rectangle(x, y, width, height);
	src= src.union(new Rectangle(destX, destY, width, height));
	MacRect r= new MacRect(src);
	
	int wHandle= OS.GetWindowFromPort(handle);

	int rgn= OS.NewRgn();
	OS.ScrollRect(r.getData(), (short)deltaX, (short)deltaY, rgn);

	org.eclipse.swt.widgets.Display.getDefault().repairWindow(wHandle, rgn, fOffsetX, fOffsetY);

	unfocus(true);
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
	/* AW
	int xDisplay = data.display;
	int xGC = OS.XCreateGC(xDisplay, image.pixmap, 0, null);
	if (xGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.XSetSubwindowMode (xDisplay, xGC, OS.IncludeInferiors);
	OS.XCopyArea(xDisplay, data.drawable, image.pixmap, xGC, x, y, rect.width, rect.height, 0, 0);
	OS.XFreeGC(xDisplay, xGC);
	*/
	System.out.println("GC.copyArea(Image): nyi");
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
	if (clipRgn != 0) OS.DisposeRgn(clipRgn);
	
	if (fClipCache != 0) OS.DisposeRgn(fClipCache);
	fClipCache= 0;
	
	Image image = data.image;
	if (image != null) image.memGC = null;
	/* AW
	int renderTable = data.renderTable;
	if (renderTable != 0) OS.XmRenderTableFree(renderTable);
	*/

	/* Dispose the GC */
	drawable.internal_dispose_GC(handle, data);

	data.clipRgn = 0;
	data.font = null;
	drawable = null;
	data.device = null;
	data.image = null;
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
	/* AW
	OS.XDrawArc(data.display,data.drawable,handle,x,y,width,height,startAngle * 64 ,endAngle * 64);
	*/
	System.out.println("GC.drawArc");
}
/** 
 * Draws a rectangle, based on the specified arguments, which has
 * the appearance of the platform's <em>focus rectangle</em> if the
 * platform supports such a notion, and otherwise draws a simple
 * rectangle in the receiver's forground color.
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
	int highlightColor = 0;
	/* AW
	int widget = OS.XtWindowToWidget (xDisplay, xDrawable);
	if (widget != 0) {
		int [] argList = {OS.XmNhighlightColor, 0};
		OS.XtGetValues (widget, argList, argList.length / 2);
		highlightColor = argList [1];
	}
	*/
	
	/* Draw the focus rectangle */
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	/* AW
	XGCValues values = new XGCValues ();
	OS.XGetGCValues (xDisplay, handle, OS.GCForeground, values);
	OS.XSetForeground (xDisplay, handle, highlightColor);
	OS.XDrawRectangle (xDisplay, xDrawable, handle, x, y, width - 1, height - 1);
	OS.XSetForeground (xDisplay, handle, values.foreground);
	*/
	//System.out.println("GC.drawFocus");
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
	/* AW
	int[] width = new int[1];
	int[] height = new int[1];
	int[] depth = new int[1];
	int[] unused = new int[1];
 	OS.XGetGeometry(data.display, srcImage.pixmap, unused, unused, unused, width, height, unused, depth);
 	int imgWidth = width[0];
 	int imgHeight = height[0];
	*/
	int depth = OS.GetPixDepth(srcImage.pixmap);
	MacRect bounds= new MacRect();
	OS.GetPixBounds(srcImage.pixmap, bounds.getData());
 	int imgWidth = bounds.getWidth();
 	int imgHeight = bounds.getHeight();

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
		drawImageAlpha(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, depth);
	} else if (srcImage.transparentPixel != -1 || srcImage.mask != 0) {
		drawImageMask(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, depth);
	} else {
		drawImage(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, depth);
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
	rect.width = Math.max(rect.width, Compatibility.ceil(destWidth, srcWidth));
	rect.height = Math.max(rect.height, Compatibility.ceil(destHeight, srcHeight));
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
	srcWidth = sx2 - sx1;
	srcHeight = sy2 - sy1;
	
	int xDestImagePtr = 0, xSrcImagePtr = 0;
	try {
		/* Get the background pixels */
		/* AW
		xDestImagePtr = OS.XGetImage(xDisplay, xDrawable, destX, destY, destWidth, destHeight, OS.AllPlanes, OS.ZPixmap);
		if (xDestImagePtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		XImage xDestImage = new XImage();
		OS.memmove(xDestImage, xDestImagePtr, XImage.sizeof);
		byte[] destData = new byte[xDestImage.bytes_per_line * xDestImage.height];
		OS.memmove(destData, xDestImage.data, destData.length);	
		*/
	
		/* Get the foreground pixels */
		/* AW
		xSrcImagePtr = OS.XGetImage(xDisplay, srcImage.pixmap, srcX, srcY, srcWidth, srcHeight, OS.AllPlanes, OS.ZPixmap);
		if (xSrcImagePtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		XImage xSrcImage = new XImage();
		OS.memmove(xSrcImage, xSrcImagePtr, XImage.sizeof);
		byte[] srcData = new byte[xSrcImage.bytes_per_line * xSrcImage.height];
		OS.memmove(srcData, xSrcImage.data, srcData.length);
		*/
		
		/* Compose the pixels */		
		/* AW
		int srcOrder = xSrcImage.byte_order == OS.MSBFirst ? ImageData.MSB_FIRST : ImageData.LSB_FIRST;
		int destOrder = xDestImage.byte_order == OS.MSBFirst ? ImageData.MSB_FIRST : ImageData.LSB_FIRST;
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
				srcData, xSrcImage.bits_per_pixel, xSrcImage.bytes_per_line, srcOrder, 0, 0, srcWidth, srcHeight, reds, greens, blues,
				srcImage.alpha, srcImage.alphaData, imgWidth,
				destData, xDestImage.bits_per_pixel, xDestImage.bytes_per_line, destOrder, 0, 0, destWidth, destHeight, reds, greens, blues,
				false, false);
		} else {
			int srcRedMask = xSrcImage.red_mask;
			int srcGreenMask = xSrcImage.green_mask;
			int srcBlueMask = xSrcImage.blue_mask;
			int destRedMask = xDestImage.red_mask;
			int destGreenMask = xDestImage.green_mask;
			int destBlueMask = xDestImage.blue_mask;
			*/
			
			/*
			* Feature in X.  XGetImage does not retrieve the RGB masks if the drawable
			* is a Pixmap.  The fix is to detect that the masks are not valid and use
			* the default visual masks instead.
			* 
			* NOTE: It is safe to use the default Visual masks, since we always
			* create images with these masks. 
			*/
			/* AW
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
				srcData, xSrcImage.bits_per_pixel, xSrcImage.bytes_per_line, srcOrder, 0, 0, srcWidth, srcHeight, srcRedMask, srcGreenMask, srcBlueMask,
				srcImage.alpha, srcImage.alphaData, imgWidth,
				destData, xDestImage.bits_per_pixel, xDestImage.bytes_per_line, destOrder, 0, 0, destWidth, destHeight, destRedMask, destGreenMask, destBlueMask,
				false, false);
		}
		*/
		
		/* Draw the composed pixels */
		/* AW
		OS.memmove(xDestImage.data, destData, destData.length);
		OS.XPutImage(xDisplay, xDrawable, handle, xDestImagePtr, 0, 0, destX, destY, destWidth, destHeight);
		*/
	} finally {
		/* AW
		if (xSrcImagePtr != 0) OS.XDestroyImage(xSrcImagePtr);
		if (xDestImagePtr != 0) OS.XDestroyImage(xDestImagePtr);
		*/
	}
	System.out.println("GC.drawImageAlpha: nyi");
}
void drawImageMask(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight, int depth) {
	/* AW
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	int colorPixmap = srcImage.pixmap;
	*/
	/* Generate the mask if necessary. */
	if (srcImage.transparentPixel != -1) srcImage.createMask();
	/* AW
	int maskPixmap = srcImage.mask;
	int foreground = 0x00000000;
	if (!(simple || (srcWidth == destWidth && srcHeight == destHeight))) {
	*/
		/* Stretch the color and mask*/
		/* AW
		int xImagePtr = scalePixmap(xDisplay, colorPixmap, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false, false);
		int xMaskPtr = scalePixmap(xDisplay, maskPixmap, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false, false);
		*/
		
		/* Create color scaled pixmaps */
		/* AW
		colorPixmap = OS.XCreatePixmap(xDisplay, xDrawable, destWidth, destHeight, depth);
		int tempGC = OS.XCreateGC(xDisplay, colorPixmap, 0, null);
		OS.XPutImage(xDisplay, colorPixmap, tempGC, xImagePtr, 0, 0, 0, 0, destWidth, destHeight);
		OS.XDestroyImage(xImagePtr);
		OS.XFreeGC(xDisplay, tempGC);
		*/

		/* Create mask scaled pixmaps */
		/* AW
		maskPixmap = OS.XCreatePixmap(xDisplay, xDrawable, destWidth, destHeight, 1);
		tempGC = OS.XCreateGC(xDisplay, maskPixmap, 0, null);
		OS.XPutImage(xDisplay, maskPixmap, tempGC, xMaskPtr, 0, 0, 0, 0, destWidth, destHeight);
		OS.XDestroyImage(xMaskPtr);
		OS.XFreeGC(xDisplay, tempGC);
		*/
		
		/* Change the source rectangle */
		/* AW
		srcX = srcY = 0;
		srcWidth = destWidth;
		srcHeight = destHeight;

		foreground = ~foreground;
		*/
	/* AW
	}
	*/
	
	/* Do the blts */
	/* AW
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
	*/
	
	// AW
	focus(true);
	int srcBits= OS.getBitMapForCopyBits(srcImage.pixmap);
	int maskBits= OS.getBitMapForCopyBits(srcImage.mask);
	int destBits= OS.GetPortBitMapForCopyBits(handle);
	if (srcBits != 0 && maskBits != 0 && destBits != 0) {
		MacRect ib= new MacRect(srcX, srcY, srcWidth, srcHeight);
		fRect.set(destX, destY, destWidth, destHeight);
		OS.RGBBackColor(0x00FFFFFF);
		OS.RGBForeColor(0x00000000);
		OS.CopyDeepMask(srcBits, maskBits, destBits, ib.getData(), ib.getData(), fRect.getData(), (short)0, 0);
	}
	unfocus(true);
	// AW

	/* Destroy scaled pixmaps */
	/* AW
	if (srcImage.pixmap != colorPixmap) OS.XFreePixmap(xDisplay, colorPixmap);
	if (srcImage.mask != maskPixmap) OS.XFreePixmap(xDisplay, maskPixmap);
	*/
	/* Destroy the image mask if the there is a GC created on the image */
	if (srcImage.transparentPixel != -1 && srcImage.memGC != null) srcImage.destroyMask();
	
}
void drawImage(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight, int depth) {
	/* AW
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	*/
	/* Simple case: no stretching */
	/* AW
	if ((srcWidth == destWidth) && (srcHeight == destHeight)) {
		OS.XCopyArea(xDisplay, srcImage.pixmap, xDrawable, handle, srcX, srcY, srcWidth, srcHeight, destX, destY);
		return;
	}
	*/
	/* Streching case */
	/* AW
	int xImagePtr = scalePixmap(xDisplay, srcImage.pixmap, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false, false);
	OS.XPutImage(xDisplay, xDrawable, handle, xImagePtr, 0, 0, destX, destY, destWidth, destHeight);
	OS.XDestroyImage(xImagePtr);
	*/

	// AW
	focus(true);
	int srcBits= OS.getBitMapForCopyBits(srcImage.pixmap);
	int destBits= OS.GetPortBitMapForCopyBits(handle);
	if (srcBits != 0 && destBits != 0) {
		//System.out.println("  copybits");
		MacRect ib= new MacRect(srcX, srcY, srcWidth, srcHeight);
		fRect.set(destX, destY, destWidth, destHeight);
		OS.RGBBackColor(0x00FFFFFF);
		OS.RGBForeColor(0x00000000);
		OS.CopyBits(srcBits, destBits, ib.getData(), fRect.getData(), (short)0, 0);
	}	
	unfocus(true);
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
	/* AW
	OS.XDrawLine (data.display, data.drawable, handle, x1, y1, x2, y2);
	*/
	focus(true);
	installForeColor(data.foreground);
	OS.PenSize((short) fLineWidth, (short) fLineWidth);
	OS.MoveTo((short)x1, (short)y1);
	OS.LineTo((short)x2, (short)y2);
	unfocus(true);
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
	focus(true);
	installForeColor(data.foreground);
	OS.PenSize((short) fLineWidth, (short) fLineWidth);
	fRect.set(x, y, width+1, height+1);
	OS.FrameOval(fRect.getData());
	unfocus(true);
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
	/* AW
	short[] xPoints = new short[pointArray.length];
	for (int i = 0; i<pointArray.length;i++) {
		xPoints[i] = (short) pointArray[i];
	}
	OS.XDrawLines(data.display,data.drawable,handle,xPoints,xPoints.length / 2, OS.CoordModeOrigin);
	*/
	
	if (pointArray.length < 4)
		return;
		
	focus(true);

	int poly= OS.OpenPoly();
	OS.MoveTo((short)pointArray[0], (short)pointArray[1]);
	for (int i= 2; i < pointArray.length; i+= 2)
		OS.LineTo((short)pointArray[i], (short)pointArray[i+1]);
	OS.ClosePoly();
	
	installForeColor(data.foreground);
	OS.PenSize((short) fLineWidth, (short) fLineWidth);
	OS.FramePoly(poly);
	unfocus(true);
	
	OS.KillPoly(poly);
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
	focus(true);
	installForeColor(data.foreground);
	OS.PenSize((short) fLineWidth, (short) fLineWidth);
	fRect.set(x, y, width+1, height+1);
	OS.FrameRect(fRect.getData());
	unfocus(true);
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
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	focus(true);
	installForeColor(data.foreground);
	OS.PenSize((short) fLineWidth, (short) fLineWidth);
	fRect.set(x, y, width+1, height+1);
	OS.FrameRoundRect(fRect.getData(), (short)arcWidth, (short)arcHeight);
	unfocus(true);
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
	/* AW
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
	int xmString = OS.XmStringCreate (buffer, OS.XmFONTLIST_DEFAULT_TAG);
	if (isTransparent) {
		OS.XmStringDraw (data.display, data.drawable, data.fontList, xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null);
	} else {
		OS.XmStringDrawImage (data.display, data.drawable, data.fontList, xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null);
	}			
//	OS.XmStringDrawUnderline (display, drawable, fontList, xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null, 0);
	OS.XmStringFree (xmString);
	*/
	focus(true);
	installFont();
	installForeColor(data.foreground);
	if (isTransparent) {
		OS.TextMode(OS.srcOr);
	} else {
		if ((data.background & 0xff000000) == 0) {
			OS.RGBBackColor(data.background);
			OS.TextMode(OS.srcCopy);
		} else {
			//System.out.println("GC.drawString: " + Integer.toHexString(data.background));
			OS.TextMode(OS.srcOr);
		}
	}
	short[] fontInfo= new short[4];
	OS.GetFontInfo(fontInfo);	// FontInfo
	OS.MoveTo((short)x, (short)(y+fontInfo[0]));
	OS.DrawText(string, data.font.fID, data.font.fSize, data.font.fFace);
	unfocus(true);
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
	
	/* AW
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
	*/
	focus(true);
	installFont();
	installForeColor(data.foreground);
	if ((flags & SWT.DRAW_TRANSPARENT) != 0) {
		OS.TextMode(OS.srcOr);
	} else {
		if ((data.background & 0xff000000) == 0) {
			OS.RGBBackColor(data.background);
			OS.TextMode(OS.srcCopy);
		} else {
			//System.out.println("GC.drawText: " + Integer.toHexString(data.background));
			OS.TextMode(OS.srcOr);
		}
	}
	short[] fontInfo= new short[4];
	OS.GetFontInfo(fontInfo);	// FontInfo
	OS.MoveTo((short)x, (short)(y+fontInfo[0]));
	OS.DrawText(string, data.font.fID, data.font.fSize, data.font.fFace);
	unfocus(true);
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
	/* AW
	XGCValues values = new XGCValues ();
	OS.XGetGCValues (xDisplay, handle, OS.GCForeground | OS.GCBackground, values);
	OS.XSetForeground (xDisplay, handle, values.background);
	OS.XFillArc(xDisplay,data.drawable,handle,x,y,width,height,startAngle * 64 ,endAngle * 64);
	OS.XSetForeground (xDisplay, handle, values.foreground);
	*/
	System.out.println("GC.fillArc");
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
	
	focus(true);
	
	/* AW
	int xDisplay = data.display;
	int xScreenNum = OS.XDefaultScreen(xDisplay);
	XGCValues values = new XGCValues();
	*/
	int fromColor, toColor;
	/* AW
	OS.XGetGCValues(xDisplay, handle, OS.GCForeground | OS.GCBackground, values);
	fromColor = values.foreground;
	toColor = values.background;
	*/
	fromColor = data.foreground;
	toColor = data.background;
	
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
		/* AW
		OS.XFillRectangle(xDisplay, data.drawable, handle, x, y, width, height);
		*/
		installForeColor(data.foreground);
		fRect.set(x, y, width, height);
		OS.PaintRect(fRect.getData());
		
		unfocus(true);
		return;
	}
	/* X Window deals with a virtually limitless array of color formats
	 * but we only distinguish between paletted and direct modes
	 */	
	/* AW
	final int xScreen = OS.XDefaultScreenOfDisplay(xDisplay);
	final int xVisual = OS.XDefaultVisual(xDisplay, xScreenNum);
	Visual visual = new Visual();
	OS.memmove(visual, xVisual, visual.sizeof);
	final int depth = OS.XDefaultDepthOfScreen(xScreen);
	*/
	int depth= 32;
	final boolean directColor = (depth > 8);

	// This code is intentionally commented since elsewhere in SWT we
	// assume that depth <= 8 means we are in a paletted mode though
	// this is not always the case.
	//final boolean directColor = (visual.c_class == OS.TrueColor) || (visual.c_class == OS.DirectColor);

	/* AW
	XColor xColor = new XColor();
	xColor.pixel = fromColor;
	OS.XQueryColor(xDisplay, data.colormap, xColor);
	final RGB fromRGB = new RGB((xColor.red & 0xffff) >>> 8, (xColor.green & 0xffff) >>> 8, (xColor.blue & 0xffff) >>> 8);
	xColor.pixel = toColor;
	OS.XQueryColor(xDisplay, data.colormap, xColor);
	final RGB toRGB = new RGB((xColor.red & 0xffff) >>> 8, (xColor.green & 0xffff) >>> 8, (xColor.blue & 0xffff) >>> 8);
	*/
	
	RGB fromRGB = Color.carbon_new(data.device, fromColor).getRGB();
	RGB toRGB = Color.carbon_new(data.device, toColor).getRGB();

	final int redBits, greenBits, blueBits;
	if (directColor) {
		// RGB mapped display
		redBits = getChannelWidth(0x00ff0000 /* AW visual.red_mask */);
		greenBits = getChannelWidth(0x0000ff00 /* AW visual.green_mask */);
		blueBits = getChannelWidth(0x000000ff /* AW visual.blue_mask */);
	} else {
		// Index display
		redBits = greenBits = blueBits = 0;
	}

	ImageData.fillGradientRectangle(this, data.device,
		x, y, width, height, vertical, fromRGB, toRGB,
		redBits, greenBits, blueBits);

	unfocus(true);
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
	focus(true);
	if ((data.background & 0xff000000) == 0) {
		OS.RGBForeColor(data.background);
		fRect.set(x, y, width, height);
		OS.PaintOval(fRect.getData());
	} else {
		//	System.out.println("GC.fillOval: " + Integer.toHexString(data.background));
	}
	unfocus(true);
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

	/* AW
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
	*/
	
	focus(true);

	int poly= OS.OpenPoly();
	OS.MoveTo((short)pointArray[0], (short)pointArray[1]);
	for (int i= 2; i < pointArray.length; i+= 2)
		OS.LineTo((short)pointArray[i], (short)pointArray[i+1]);
	OS.ClosePoly();
	
	installForeColor(data.background);
	OS.PaintPoly(poly);
	unfocus(true);
	
	OS.KillPoly(poly);
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
	focus(true);
	fRect.set(x, y, width, height);
	if ((data.background & 0xff000000) == 0) {
		OS.RGBForeColor(data.background);
		OS.PaintRect(fRect.getData());
	} else {
		/*
		OS.RGBForeColor(0xffffff);
		OS.PaintRect(fRect.getData());
		*/
		int[] state= new int[1];
		OS.GetThemeDrawingState(state);
		OS.SetThemeBackground(OS.kThemeBrushDialogBackgroundActive, (short)32, true);
		OS.EraseRect(fRect.getData());
		OS.SetThemeDrawingState(state[0], true);
		//	System.out.println("GC.fillRectangle: " + Integer.toHexString(data.background));
	}
	unfocus(true);
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
	focus(true);
	if ((data.background & 0xff000000) == 0) {
		OS.RGBForeColor(data.background);
		fRect.set(x, y, width, height);
		OS.PaintRoundRect(fRect.getData(), (short)arcWidth, (short)arcHeight);
	} else {
		//	System.out.println("GC.fillRoundRectangle: " + Integer.toHexString(data.background));
	}
	unfocus(true);
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
	focus(false);
	installFont();
	int width= OS.CharWidth((byte) ch);
	unfocus(false);
	return width;
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
	/* AW
	int xDisplay = data.display;
	XGCValues values = new XGCValues();
	OS.XGetGCValues(xDisplay, handle, OS.GCBackground, values);
	XColor xColor = new XColor();
	xColor.pixel = values.background;
	OS.XQueryColor(xDisplay,data.colormap,xColor);
	*/
	return Color.carbon_new(data.device, data.background);
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
	System.out.println("GC.getCharWidth");
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
	/* AW
	if (clipRgn == 0) {
		int[] width = new int[1]; int[] height = new int[1];
		int[] unused = new int[1];
		OS.XGetGeometry(data.display, data.drawable, unused, unused, unused, width, height, unused, unused);
		return new Rectangle(0, 0, width[0], height[0]);
	}
	XRectangle rect = new XRectangle();
	OS.XClipBox(clipRgn, rect);
	return new Rectangle(rect.x, rect.y, rect.width, rect.height);
	*/
	MacRect bounds= new MacRect();
	if (data.clipRgn == 0) {
		if (data.controlHandle != 0) {
			OS.GetControlBounds(data.controlHandle, bounds.getData());
			return new Rectangle(0, 0, bounds.getWidth(), bounds.getHeight());
		}
		if (data.image != null) {
			return data.image.getBounds();
		}	
		System.out.println("GC.getClipping(): should not happen");
		return new Rectangle(0, 0, 100, 100);
	}
	OS.GetRegionBounds(data.clipRgn, bounds.getData());
	return bounds.toRectangle();
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
	
	if (region.handle == 0)
		region.handle= OS.NewRgn();
		
	if (data.clipRgn == 0) {
		if (data.controlHandle != 0) {
			OS.GetControlRegion(data.controlHandle, OS.kWindowContentRgn, region.handle);
		} else
			System.out.println("GC.getClipping(Region): nyi");
	} else {
		OS.CopyRgn(data.clipRgn, region.handle);
	}
	
	/* AW
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
	*/
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
	return Font.carbon_new(data.device, data.font);
}
int getFontHeight () {
	focus(false);
	installFont();
	short[] fontInfo= new short[4];
	OS.GetFontInfo(fontInfo);	// FontInfo
	int height= fontInfo[0] + fontInfo[1];
	unfocus(false);
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
	
	focus(false);
	installFont();
	short[] fontInfo= new short[4];
	OS.GetFontInfo(fontInfo);	// FontInfo
	unfocus(false);	
	
	return FontMetrics.carbon_new(fontInfo[0], fontInfo[1], fontInfo[2], fontInfo[3], fontInfo[0]+fontInfo[1]);
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
	/* AW
	int xDisplay = data.display;
	XGCValues values = new XGCValues();
	OS.XGetGCValues(xDisplay, handle, OS.GCForeground, values);
	XColor xColor = new XColor();
	xColor.pixel = values.foreground;
	OS.XQueryColor(xDisplay,data.colormap,xColor);
	return Color.motif_new(data.device, xColor);
	*/
	return Color.carbon_new(data.device, data.foreground);
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
	/* AW
	XGCValues values = new XGCValues();
	OS.XGetGCValues(data.display, handle, OS.GCLineWidth, values);
	return values.line_width;
	*/
	return fLineWidth;
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
	/* AW
	XGCValues values = new XGCValues ();
	OS.XGetGCValues (data.display, handle, OS.GCFunction, values);
	return values.function == OS.GXxor;
	*/
	return fXorMode;
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
	/* AW
	int xDisplay = data.display;
	int foreground = data.foreground;
	if (foreground != -1) OS.XSetForeground (xDisplay, xGC, foreground);
	int background = data.background;
	if (background != -1) OS.XSetBackground (xDisplay, xGC, background);
	*/
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
	if (xGC == 0)
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
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
	System.out.println("GC.isClipped: nyi");
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
public static GC macosx_new(Drawable drawable, GCData data) {
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
	/* AW
	OS.XSetBackground(data.display, handle, color.handle.pixel);
	*/
	data.background= color.handle;
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
	if (data.clipRgn == 0)
		data.clipRgn = OS.NewRgn ();
	OS.SetRectRgn(data.clipRgn, (short) x, (short) y, (short) (x+width), (short) (y+height));
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
		if (data.clipRgn != 0) {
			OS.DisposeRgn(data.clipRgn);
			data.clipRgn= 0;
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
	if (region == null) {
		if (data.clipRgn != 0) {
			OS.DisposeRgn (data.clipRgn);
			data.clipRgn = 0;
		}
	} else {
		if (data.clipRgn == 0)
			data.clipRgn = OS.NewRgn();
		OS.CopyRgn(region.handle, data.clipRgn);
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
	if (font == null) {
		data.font = data.device.systemFont;
	} else {
		if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		data.font = font.handle;
	}
	/* AW
	if (data.renderTable != 0) OS.XmRenderTableFree(data.renderTable);
	data.renderTable = 0;
	*/
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
	/* AW
	OS.XSetForeground(data.display, handle, color.handle.pixel);
	*/
	data.foreground= color.handle;
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
	/* AW
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
	OS.XSetLineAttributes(xDisplay, handle, 0, OS.LineDoubleDash, OS.CapButt, OS.JoinMiter);
	*/
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
		/* AW
		OS.XSetLineAttributes(data.display, handle, width, OS.LineSolid, OS.CapButt, OS.JoinMiter);
		*/
	} else {
		/* AW
		OS.XSetLineAttributes(data.display, handle, width, OS.LineDoubleDash, OS.CapButt, OS.JoinMiter);
		*/
	}
	fLineWidth= width;
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
	/* AW
	if (xor)
		OS.XSetFunction(data.display, handle, OS.GXxor);
	else
		OS.XSetFunction(data.display, handle, OS.GXcopy);
	*/
	fXorMode= xor;
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
	/* AW
	byte[] buffer = Converter.wcsToMbcs(getCodePage (), string, true);
	int xmString = OS.XmStringCreate(buffer, OS.XmFONTLIST_DEFAULT_TAG);
	int fontList = data.fontList;
	int width = OS.XmStringWidth(fontList, xmString);
	int height = OS.XmStringHeight(fontList, xmString);
	OS.XmStringFree(xmString);
	*/
	focus(false);
	installFont();
	int width= OS.TextWidth(string, data.font.fID, data.font.fSize, data.font.fFace);
	short[] fontInfo= new short[4];
	OS.GetFontInfo(fontInfo);	// FontInfo
	int height= fontInfo[0] + fontInfo[1];
	unfocus(false);
	return new Point(width, height);
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
	
	/* AW
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
	*/
	focus(false);
	installFont();
	int width= OS.TextWidth(string, data.font.fID, data.font.fSize, data.font.fFace);
	short[] fontInfo= new short[4];
	OS.GetFontInfo(fontInfo);	// FontInfo
	unfocus(false);
	return new Point(width, fontInfo[0] + fontInfo[1]);
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

//---- Mac Stuff

	public void installFont() {
		if (data != null && data.font != null)
			data.font.installInGrafPort();
	}

	private void focus(boolean clip) {
	
		// save global state
		OS.GetGWorld(fSavePort, fSaveGWorld);		
		OS.SetGWorld(handle, fSaveGWorld[0]);
		
		fOffsetX= 0;
		fOffsetY= 0;
		
		if (fIsFocused)
			return;

		if (clip) {
			// set origin of port using drawable bounds
			if (data.controlHandle != 0) {
				OS.GetControlBounds(data.controlHandle, fRect.getData());
				fOffsetX= (short)fRect.getX();
				fOffsetY= (short)fRect.getY();
				OS.SetOrigin((short)-fOffsetX, (short)-fOffsetY);
			}
			// save clip region 
			OS.GetClip(fSaveClip);
			
			// calculate new clip based on the controls bound and GC clipping region
			if (data.controlHandle != 0) {
				if (fClipCache == 0) {
					fClipCache= OS.NewRgn();
					MacUtil.getVisibleRegion(data.controlHandle, fClipCache, true);
					OS.OffsetRgn(fClipCache, (short)-fRect.getX(), (short)-fRect.getY());
				}
				if (data.clipRgn != 0) {
					int rgn= OS.NewRgn();
					OS.SectRgn(fClipCache, data.clipRgn, rgn);
					OS.SetClip(rgn);
					OS.DisposeRgn(rgn);
				} else 
					OS.SetClip(fClipCache);
				// caching doesn't work yet
				OS.DisposeRgn(fClipCache);
				fClipCache= 0;
			} else {
				if (data.clipRgn != 0)
					OS.SetClip(data.clipRgn);
			}
		}
	}

	private void unfocus(boolean clip) {
		
		if (! fIsFocused) {
			if (clip) {
				// restore clipping and origin of port
				OS.SetClip(fSaveClip);
				OS.SetOrigin((short)0, (short)0);
			}
		}
		
		// restore globals
		OS.SetGWorld(fSavePort[0], fSaveGWorld[0]);
	}
	
	public void carbon_focus() {
		OS.LockPortBits(handle);
		//focus(true);
		//fIsFocused= true;
	}
	
	public void carbon_unfocus() {
		//fIsFocused= false;
		//unfocus(true);
		OS.UnlockPortBits(handle);
	}
	
	private void installForeColor(int color) {
		if ((color & 0xff000000) == 0) {
			OS.RGBForeColor(color);
		} else {
			OS.RGBForeColor(0x000000);
		}
	}
	
	private void installBackColor(int color) {
		if ((color & 0xff000000) == 0) {
			OS.RGBBackColor(color);
		} else {
			OS.RGBBackColor(0xFFFFFF);
		}
	}
}

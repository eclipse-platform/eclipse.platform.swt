package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public final class GC {
	/**
	 * The handle to the OS GC resource.
	 * Warning: This field is platform dependent.
	 */
	public int handle;
	
	Drawable drawable;
	GCData data;
	
	static final byte[] _MOTIF_DEFAULT_LOCALE = Converter.wcsToMbcs(null, "_MOTIF_DEFAULT_LOCALE");

GC() {
}
public GC (Drawable drawable) {
	if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	GCData data = new GCData();
	int xGC = drawable.internal_new_GC(data);
	init(drawable, data, xGC);
}

public void copyArea(int x, int y, int width, int height, int destX, int destY) {
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - x, deltaY = destY - y;
	if (deltaX == 0 && deltaY == 0) return;
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	OS.XSetGraphicsExposures (xDisplay, handle, true);
	OS.XCopyArea(xDisplay, xDrawable, xDrawable, handle, x, y, width, height, destX, destY);
	OS.XSetGraphicsExposures (xDisplay, handle, false);
	boolean disjoint = (destX + width < x) || (x + width < destX) || (destY + height < y) || (y + height < destY);
	if (data.image != null) return;
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
public void copyArea(Image image, int x, int y) {
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.type != SWT.BITMAP) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Rectangle rect = image.getBounds();
	int xDisplay = data.display;
	int xGC = OS.XCreateGC(xDisplay, image.pixmap, 0, null);
	if (xGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.XSetSubwindowMode (xDisplay, xGC, OS.IncludeInferiors);
	OS.XCopyArea(xDisplay, data.drawable, image.pixmap, xGC, x, y, rect.width, rect.height, 0, 0);
	OS.XFreeGC(xDisplay, xGC);
}
public void dispose () {
	if (handle == 0) return;
	
	/* Free resources */
	int clipRgn = data.clipRgn;
	if (clipRgn != 0) OS.XDestroyRegion(clipRgn);
	Image image = data.image;
	if (image != null) image.memGC = null;
	int renderTable = data.renderTable;
	if (renderTable != 0) OS.XmRenderTableFree(renderTable);

	/* Dispose the GC */
	drawable.internal_dispose_GC(handle, data);

	data.display = data.drawable = data.colormap = data.fontList = 
		data.clipRgn = data.renderTable = 0;
	drawable = null;
	data.device = null;
	data.image = null;
	data = null;
	handle = 0;
}
public void drawArc(int x, int y, int width, int height, int startAngle, int endAngle) {
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
public void drawFocus (int x, int y, int width, int height) {
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
public void drawImage(Image image, int x, int y) {
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	drawImage(image, 0, 0, -1, -1, x, y, -1, -1, true);
}
public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
	if (srcWidth == 0 || srcHeight == 0 || destWidth == 0 || destHeight == 0) return;
	if (srcX < 0 || srcY < 0 || srcWidth < 0 || srcHeight < 0 || destWidth < 0 || destHeight < 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
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
	rect.width = Math.max(rect.width, (int)Math.ceil(destWidth / (float)srcWidth));
	rect.height = Math.max(rect.height, (int)Math.ceil(destHeight / (float)srcHeight));
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
				srcData, xSrcImage.bits_per_pixel, xSrcImage.bytes_per_line, srcOrder, 0, 0, srcWidth, srcHeight, reds, greens, blues, srcImage.alpha, srcImage.alphaData, imgWidth,
				destData, xDestImage.bits_per_pixel, xDestImage.bytes_per_line, destOrder, 0, 0, destWidth, destHeight, reds, greens, blues,
				false, false);
		} else {
			ImageData.blit(ImageData.BLIT_ALPHA,
				srcData, xSrcImage.bits_per_pixel, xSrcImage.bytes_per_line, srcOrder, 0, 0, srcWidth, srcHeight, xDestImage.red_mask, xDestImage.green_mask, xDestImage.blue_mask, srcImage.alpha, srcImage.alphaData, imgWidth,
				destData, xDestImage.bits_per_pixel, xDestImage.bytes_per_line, destOrder, 0, 0, destWidth, destHeight, xDestImage.red_mask, xDestImage.green_mask, xDestImage.blue_mask,
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
		case 1: {
			int bitOrder = xSrcImage.bitmap_bit_order == OS.MSBFirst ? ImageData.MSB_FIRST : ImageData.LSB_FIRST;
			int bplX = ((destWidth + 7) / 8 + 3) & 0xFFFC;
			int bufSize = bplX * destHeight;
			byte[] buf = new byte[bufSize];
			ImageData.stretch1(srcData, xSrcImage.bytes_per_line, bitOrder, 0, 0, srcWidth, srcHeight, buf, bplX, bitOrder, 0, 0, destWidth, destHeight, flipX, flipY);
			int bufPtr = OS.XtMalloc(bufSize);
			OS.memmove(bufPtr, buf, bufSize);
			xImagePtr = OS.XCreateImage(display, visual, 1, OS.XYBitmap, 0, bufPtr, destWidth, destHeight, 32, bplX);
			if (xImagePtr == 0) break;
			XImage xImage = new XImage();
			OS.memmove(xImage, xImagePtr, XImage.sizeof);
			xImage.byte_order = OS.MSBFirst;
			xImage.bitmap_unit = 8;
			xImage.bitmap_bit_order = xSrcImage.bitmap_bit_order;
			OS.memmove(xImagePtr, xImage, XImage.sizeof);
			break;
		}
		case 4: {
			/* Untested */
			int bplX = (destWidth + 3) & 0xFFFC;
			int bufSize = bplX * destHeight;
			byte[] buf = new byte[bufSize];
			ImageData.stretch4(srcData, xSrcImage.bytes_per_line, 0, 0, srcWidth, srcHeight, buf, bplX, 0, 0, destWidth, destHeight, null, flipX, flipY);
			int bufPtr = OS.XtMalloc(bufSize);
			OS.memmove(bufPtr, buf, bufSize);
			xImagePtr = OS.XCreateImage(display, visual, 4, OS.ZPixmap, 0, bufPtr, destWidth, destHeight, 32, bplX);
			break;
		}
		case 8: {
			int bplX = (destWidth + 3) & 0xFFFC;
			int bufSize = bplX * destHeight;
			byte[] buf = new byte[bufSize];
			ImageData.stretch8(srcData, xSrcImage.bytes_per_line, 0, 0, srcWidth, srcHeight, buf, bplX, 0, 0, destWidth, destHeight, null, flipX, flipY);
			int bufPtr = OS.XtMalloc(bufSize);
			OS.memmove(bufPtr, buf, bufSize);
			xImagePtr = OS.XCreateImage(display, visual, 8, OS.ZPixmap, 0, bufPtr, destWidth, destHeight, 32, bplX);
			break;
		}
		case 16: {
			xImagePtr = OS.XCreateImage(display, visual, 16, OS.ZPixmap, 0, 0, destWidth, destHeight, 32, 0);
			if (xImagePtr == 0) break;
			XImage xImage = new XImage();
			OS.memmove(xImage, xImagePtr, XImage.sizeof);
			int bufSize = xImage.bytes_per_line * destHeight;
			byte[] buf = new byte[bufSize];
			ImageData.stretch16(srcData, xSrcImage.bytes_per_line, 0, 0, srcWidth, srcHeight, buf, xImage.bytes_per_line, 0, 0, destWidth, destHeight, flipX, flipY);
			int bufPtr = OS.XtMalloc(bufSize);
			OS.memmove(bufPtr, buf, bufSize);
			xImage.data = bufPtr;
			OS.memmove(xImagePtr, xImage, XImage.sizeof);
			break;
		}
		case 24: {
			xImagePtr = OS.XCreateImage(display, visual, 24, OS.ZPixmap, 0, 0, destWidth, destHeight, 32, 0);
			if (xImagePtr == 0) break;
			XImage xImage = new XImage();
			OS.memmove(xImage, xImagePtr, XImage.sizeof);
			int bufSize = xImage.bytes_per_line * destHeight;
			byte[] buf = new byte[bufSize];
			ImageData.stretch24(srcData, xSrcImage.bytes_per_line, 0, 0, srcWidth, srcHeight, buf, xImage.bytes_per_line, 0, 0, destWidth, destHeight, flipX, flipY);
			int bufPtr = OS.XtMalloc(bufSize);
			OS.memmove(bufPtr, buf, bufSize);
			xImage.data = bufPtr;
			OS.memmove(xImagePtr, xImage, XImage.sizeof);
			break;
		}
		case 32: {
			xImagePtr = OS.XCreateImage(display, visual, 24, OS.ZPixmap, 0, 0, destWidth, destHeight, 32, 0);
			if (xImagePtr == 0) break;
			XImage xImage = new XImage();
			OS.memmove(xImage, xImagePtr, XImage.sizeof);
			int bufSize = xImage.bytes_per_line * destHeight;
			byte[] buf = new byte[bufSize];
			ImageData.stretch32(srcData, xSrcImage.bytes_per_line, 0, 0, srcWidth, srcHeight, buf, xImage.bytes_per_line, 0, 0, destWidth, destHeight, flipX, flipY);
			int bufPtr = OS.XtMalloc(bufSize);
			OS.memmove(bufPtr, buf, bufSize);
			xImage.data = bufPtr;
			OS.memmove(xImagePtr, xImage, XImage.sizeof);
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
public void drawLine (int x1, int y1, int x2, int y2) {
	OS.XDrawLine (data.display, data.drawable, handle, x1, y1, x2, y2);
}
public void drawOval(int x, int y, int width, int height) {
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
public void drawPolygon(int[] pointArray) {
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
public void drawPolyline(int[] pointArray) {
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	short[] xPoints = new short[pointArray.length];
	for (int i = 0; i<pointArray.length;i++) {
		xPoints[i] = (short) pointArray[i];
	}
	OS.XDrawLines(data.display,data.drawable,handle,xPoints,xPoints.length / 2, OS.CoordModeOrigin);
}
public void drawRectangle (int x, int y, int width, int height) {
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
public void drawRectangle (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	drawRectangle (rect.x, rect.y, rect.width, rect.height);
}
public void drawRoundRectangle (int x, int y, int width, int height, int arcWidth, int arcHeight) {

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
	OS.XDrawArc(xDisplay,xDrawable,handle,nx,ny,naw,nah,5760,5760);
	OS.XDrawArc(xDisplay,xDrawable,handle,nx,ny + nh - nah,naw,nah,11520,5760);
	OS.XDrawArc(xDisplay,xDrawable,handle,nx + nw - naw, ny + nh - nah, naw, nah,17280,5760);
	OS.XDrawArc(xDisplay,xDrawable,handle,nx + nw - naw, ny, naw, nah, 0, 5760);
	OS.XDrawLine(xDisplay,xDrawable,handle,nx + naw2, ny, nx + nw - naw2, ny);
	OS.XDrawLine(xDisplay,xDrawable,handle,nx,ny + nah2, nx, ny + nh - nah2);
	OS.XDrawLine(xDisplay,xDrawable,handle,nx + naw2, ny + nh, nx + nw - naw2, ny + nh);
	OS.XDrawLine(xDisplay,xDrawable,handle,nx + nw, ny + nah2, nx + nw, ny + nh - nah2);
}
public void drawString (String string, int x, int y) {
	drawString(string, x, y, false);
}
/** 
 * Draws the given string, using this graphics context's 
 * current font and foreground color.
 * No tab expansion or CR processing will be performed.
 *
 * @param       string      the string to be drawn.
 * @param       x           the <i>x</i> coordinate.
 * @param       y           the <i>y</i> coordinate.
 * @param 		isTransparent if true, the background will be transparent.
 */
public void drawString (String string, int x, int y, boolean isTransparent) {
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
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
		int rendition = OS.XmRenditionCreate(shellHandle, _MOTIF_DEFAULT_LOCALE, argList, argList.length / 2);
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
public void drawText (String string, int x, int y) {
	drawText(string, x, y, false);
}
/** 
 * Draws the text given by the specified string, using this 
 * graphics context's current font and foreground color.
 * The string will be formatted with tab expansion and CR processing.
 * 
 * @param       string      the string to be drawn.
 * @param       x           the <i>x</i> coordinate.
 * @param       y           the <i>y</i> coordinate.
 * @param 		isTransparent if true, the background will be transparent. 
 */
public void drawText (String string, int x, int y, boolean isTransparent) {
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data.renderTable == 0) createRenderTable();
	int renderTable = data.renderTable;
	byte [] textBuffer = Converter.wcsToMbcs (null, string, true);
	int xmString = OS.XmStringGenerate(textBuffer, null, OS.XmCHARSET_TEXT, _MOTIF_DEFAULT_LOCALE);
	if (isTransparent) {
		OS.XmStringDraw (data.display, data.drawable, renderTable, xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null);
	} else {
		OS.XmStringDrawImage (data.display, data.drawable, renderTable, xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null);	
	}		
//	OS.XmStringDrawUnderline (display, drawable, renderTable, xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null, 0);
	OS.XmStringFree (xmString);
}
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof GC)) return false;
	GC gc = (GC)object;
	return data.device == gc.data.device && handle == gc.handle;
}
public void fillArc(int x, int y, int width, int height, int startAngle, int endAngle) {
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
public void fillOval (int x, int y, int width, int height) {
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
public void fillPolygon(int[] pointArray) {
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	short[] xPoints = new short[pointArray.length];
	for (int i = 0; i<pointArray.length;i++) {
		xPoints[i] = (short) pointArray[i];
	}
	int xDisplay = data.display;
	XGCValues values = new XGCValues ();
	OS.XGetGCValues (xDisplay, handle, OS.GCForeground | OS.GCBackground, values);
	OS.XSetForeground (xDisplay, handle, values.background);
	OS.XFillPolygon(xDisplay, data.drawable, handle,xPoints, xPoints.length / 2, OS.Convex, OS.CoordModeOrigin);
	OS.XSetForeground (xDisplay, handle, values.foreground);
}
public void fillRectangle (int x, int y, int width, int height) {
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
public void fillRectangle (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int xDisplay = data.display;
	XGCValues values = new XGCValues ();
	OS.XGetGCValues (xDisplay, handle, OS.GCForeground | OS.GCBackground, values);
	OS.XSetForeground (xDisplay, handle, values.background);
	OS.XFillRectangle (xDisplay, data.drawable, handle, rect.x, rect.y, rect.width, rect.height);
	OS.XSetForeground (xDisplay, handle, values.foreground);
}
public void fillRoundRectangle (int x, int y, int width, int height, int arcWidth, int arcHeight) {
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

	naw = naw < nw ? naw : nw;
	nah = nah < nh ? nah : nh;
		
	int naw2 = naw / 2;
	int nah2 = nah / 2;

	int xDisplay = data.display;
	int xDrawable = data.drawable;
	XGCValues values = new XGCValues ();
	OS.XGetGCValues(xDisplay, handle, OS.GCForeground | OS.GCBackground, values);
	OS.XSetForeground(xDisplay, handle, values.background);
	OS.XFillArc(xDisplay,xDrawable,handle,nx,ny,naw,nah,5760,5760);
	OS.XFillArc(xDisplay,xDrawable,handle,nx,ny + nh - nah,naw,nah,11520,5760);
	OS.XFillArc(xDisplay,xDrawable,handle,nx + nw - naw, ny + nh - nah, naw, nah,17280,5760);
	OS.XFillArc(xDisplay,xDrawable,handle,nx + nw - naw, ny, naw, nah, 0, 5760);
	OS.XFillRectangle(xDisplay,xDrawable,handle,nx + naw2, ny, nw - naw, nh);
	OS.XFillRectangle(xDisplay,xDrawable,handle,nx,ny + nah2, naw2, nh - nah);
	OS.XFillRectangle(xDisplay,xDrawable,handle,nx + nw - (naw / 2), ny + nah2, naw2, nh -nah);
	OS.XSetForeground(xDisplay, handle, values.foreground);
}
public int getAdvanceWidth(char ch) {
	int fontList  = data.fontList;
	byte[] charBuffer = Converter.wcsToMbcs(null, new char[] { ch }, false);
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
					OS.memmove(charStruct, fontStruct.per_char + ((val - fontStruct.min_char_or_byte2) * XCharStruct.sizeof), XCharStruct.sizeof);
					if (charStruct.width != 0) {
						OS.XmFontListFreeFontContext(context);
						return charStruct.width;
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
					int offset = row * charsPerRow + col;
					OS.memmove(charStruct, fontStruct.per_char + offset * XCharStruct.sizeof, XCharStruct.sizeof);
					if (charStruct.width != 0) {
						OS.XmFontListFreeFontContext(context);
						return charStruct.width;
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
						OS.memmove(charStruct, fontStruct.per_char + (val - fontStruct.min_char_or_byte2 * XCharStruct.sizeof), XCharStruct.sizeof);
						if (charStruct.width != 0) {
							OS.XmFontListFreeFontContext(context);
							return charStruct.width;
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
						int offset = row * charsPerRow + col;
						OS.memmove(charStruct, fontStruct.per_char + offset * XCharStruct.sizeof, XCharStruct.sizeof);
						if (charStruct.width != 0) {
							OS.XmFontListFreeFontContext(context);
							return charStruct.width;
						}
					}
				} 
			}
		}
	}
	OS.XmFontListFreeFontContext(context);
	return 0;
}
public Color getBackground() {
	int xDisplay = data.display;
	XGCValues values = new XGCValues();
	if (OS.XGetGCValues(xDisplay, handle, OS.GCBackground, values) == 0) {
		// Check error case here. If a palette has been set we may be able
		// to do a better job. 
		return null;
	}
	XColor xColor = new XColor();
	xColor.pixel = values.background;
	OS.XQueryColor(xDisplay,data.colormap,xColor);
	return Color.motif_new(data.device, xColor);
	
}
public int getCharWidth(char ch) {
	int fontList = data.fontList;
	byte[] charBuffer = Converter.wcsToMbcs(null, new char[] { ch }, false);
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
					OS.memmove(charStruct, fontStruct.per_char + ((val - fontStruct.min_char_or_byte2) * XCharStruct.sizeof), XCharStruct.sizeof);
					if (charStruct.width != 0) {
						OS.XmFontListFreeFontContext(context);
						return charStruct.rbearing - charStruct.lbearing;
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
					int offset = row * charsPerRow + col;
					OS.memmove(charStruct, fontStruct.per_char + offset * XCharStruct.sizeof, XCharStruct.sizeof);
					if (charStruct.width != 0) {
						OS.XmFontListFreeFontContext(context);
						return charStruct.rbearing - charStruct.lbearing;
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
						OS.memmove(charStruct, fontStruct.per_char + (val - fontStruct.min_char_or_byte2 * XCharStruct.sizeof), XCharStruct.sizeof);
						if (charStruct.width != 0) {
							OS.XmFontListFreeFontContext(context);
							return charStruct.rbearing - charStruct.lbearing;
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
						int offset = row * charsPerRow + col;
						OS.memmove(charStruct, fontStruct.per_char + offset * XCharStruct.sizeof, XCharStruct.sizeof);
						if (charStruct.width != 0) {
							OS.XmFontListFreeFontContext(context);
							return charStruct.rbearing - charStruct.lbearing;
						}
					}
				} 
			}
		}
	}
	OS.XmFontListFreeFontContext(context);
	return 0;
}
public Rectangle getClipping() {
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
public void getClipping(Region region) {
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
public Font getFont () {
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
public FontMetrics getFontMetrics() {
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
						/**
						 * Not all fonts have average character width encoded
						 * in the xlfd. This one doesn't, so do it the hard
						 * way by averaging all the character widths.
						 */
						int sum = 0, count = 0;
						int cols = fontStruct.max_char_or_byte2 - fontStruct.min_char_or_byte2 + 1;
						int perCharPtr = fontStruct.per_char;
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
							/**
							 * Not all fonts have average character width encoded
							 * in the xlfd. This one doesn't, so do it the hard
							 * way by averaging all the character widths.
							 */
							int sum = 0, count = 0;
							int cols = fontStruct.max_char_or_byte2 - fontStruct.min_char_or_byte2 + 1;
							int perCharPtr = fontStruct.per_char;
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
public Color getForeground() {
	int xDisplay = data.display;
	XGCValues values = new XGCValues();
	if (OS.XGetGCValues(xDisplay, handle, OS.GCForeground, values) == 0) {
		// Check error case here. If a palette has been set we may be able
		// to do a better job. 
		return null;
	}
	XColor xColor = new XColor();
	xColor.pixel = values.foreground;
	OS.XQueryColor(xDisplay,data.colormap,xColor);
	return Color.motif_new(data.device, xColor);
	
}
public int getLineStyle() {
	return data.lineStyle;
}
public int getLineWidth() {
	XGCValues values = new XGCValues();
	OS.XGetGCValues(data.display, handle, OS.GCLineWidth, values);
	return values.line_width;
}
public boolean getXORMode() {
	XGCValues values = new XGCValues ();
	OS.XGetGCValues (data.display, handle, OS.GCFunction, values);
	return values.function == OS.GXxor;
}
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
public boolean isClipped() {
	return data.clipRgn != 0;
}
public boolean isDisposed() {
	return handle == 0;
}
public static GC motif_new(Drawable drawable, GCData data) {
	GC gc = new GC();
	int xGC = drawable.internal_new_GC(data);
	gc.init(drawable, data, xGC);
	return gc;
}
public void setBackground (Color color) {
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.XSetBackground(data.display, handle, color.handle.pixel);
}
public void setClipping (int x, int y, int width, int height) {
	int clipRgn = data.clipRgn;
	if (clipRgn == 0) {
		clipRgn = OS.XCreateRegion ();
	} else {
		OS.XSubtractRegion (clipRgn, clipRgn, clipRgn);
	}
	XRectangle rect = new XRectangle ();
	rect.x = (short) x;  rect.y = (short) y;
	rect.width = (short) width;  rect.height = (short) height;
	OS.XSetClipRectangles (data.display, handle, 0, 0, rect, 1, OS.Unsorted);
	OS.XUnionRectWithRegion(rect, clipRgn, clipRgn);
}
public void setClipping (Rectangle rect) {
	if (rect == null) {
		OS.XSetClipMask (data.display, handle, OS.None);
		return;
	}
	setClipping (rect.x, rect.y, rect.width, rect.height);
}
public void setClipping (Region region) {
	int clipRgn = data.clipRgn;
	if (region == null) {
		OS.XSetClipMask (data.display, handle, OS.None);
		if (clipRgn != 0) {
			OS.XDestroyRegion (clipRgn);
			clipRgn = 0;
		}
	} else {
		if (clipRgn == 0) {
			clipRgn = OS.XCreateRegion ();
		} else {
			OS.XSubtractRegion (clipRgn, clipRgn, clipRgn);
		}
		OS.XUnionRegion (region.handle, clipRgn, clipRgn);
		OS.XSetRegion (data.display, handle, region.handle);
	}
}
public void setFont (Font font) {
	if (font == null) font = data.device.getSystemFont ();
	if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.fontList = font.handle;
	if (data.renderTable != 0) OS.XmRenderTableFree(data.renderTable);
	data.renderTable = 0;
}
public void setForeground (Color color) {
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.XSetForeground(data.display, handle, color.handle.pixel);
}
public void setLineStyle(int lineStyle) {
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
	
}
public void setLineWidth(int width) {
	if (data.lineStyle == SWT.LINE_SOLID) {
		OS.XSetLineAttributes(data.display, handle, width, OS.LineSolid, OS.CapButt, OS.JoinMiter);
	} else {
		OS.XSetLineAttributes(data.display, handle, width, OS.LineDoubleDash, OS.CapButt, OS.JoinMiter);
	}
}
public void setXORMode(boolean val) {
	if (val)
		OS.XSetFunction(data.display, handle, OS.GXxor);
	else
		OS.XSetFunction(data.display, handle, OS.GXcopy);
}
public Point stringExtent(String string) {
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (string.length () == 0) return new Point(0, getFontHeight());
	byte[] buffer = Converter.wcsToMbcs(null, string, true);
	int xmString = OS.XmStringCreate(buffer, OS.XmFONTLIST_DEFAULT_TAG);
	int fontList = data.fontList;
	int width = OS.XmStringWidth(fontList, xmString);
	int height = OS.XmStringHeight(fontList, xmString);
	OS.XmStringFree(xmString);
	return new Point(width, height);
}
public Point textExtent(String string) {
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (string.length () == 0) return new Point(0, getFontHeight());
	byte [] textBuffer = Converter.wcsToMbcs (null, string, true);
	int xmString = OS.XmStringGenerate(textBuffer, null, OS.XmCHARSET_TEXT, _MOTIF_DEFAULT_LOCALE);
	if (data.renderTable == 0) createRenderTable();
	int renderTable = data.renderTable;
	int width = OS.XmStringWidth(renderTable, xmString);
	int height =  OS.XmStringHeight(renderTable, xmString);
	OS.XmStringFree(xmString);
	return new Point(width, height);
}
}

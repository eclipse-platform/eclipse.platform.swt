/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.*;
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
	 */
	public int /*long*/ handle;
	
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
	int /*long*/ gdkGC = drawable.internal_new_GC(data);
	Device device = data.device;
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	data.device = device;
	init(drawable, data, gdkGC);
	if (device.tracking) device.new_Object(this);
}

static int checkStyle (int style) {
	if ((style & SWT.LEFT_TO_RIGHT) != 0) style &= ~SWT.RIGHT_TO_LEFT;
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

public static GC gtk_new(Drawable drawable, GCData data) {
	GC gc = new GC();
	int /*long*/ gdkGC = drawable.internal_new_GC(data);
	gc.init(drawable, data, gdkGC);
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
	Rectangle rect = image.getBounds();
	int /*long*/ gdkGC = OS.gdk_gc_new(image.pixmap);
	if (gdkGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.gdk_gc_set_subwindow(gdkGC, OS.GDK_INCLUDE_INFERIORS);
	OS.gdk_draw_drawable(image.pixmap, gdkGC, data.drawable, x, y, 0, 0, rect.width, rect.height);
	OS.g_object_unref(gdkGC);
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
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - srcX, deltaY = destY - srcY;
	if (deltaX == 0 && deltaY == 0) return;
	int /*long*/ drawable = data.drawable;
	OS.gdk_gc_set_exposures(handle, true);
	OS.gdk_draw_drawable(drawable, handle, drawable, srcX, srcY, destX, destY, width, height);
	OS.gdk_gc_set_exposures(handle, false);
	if (data.image != null) return;
	boolean disjoint = (destX + width < srcX) || (srcX + width < destX) || (destY + height < srcY) || (srcY + height < destY);
	GdkRectangle rect = new GdkRectangle ();
	if (disjoint) {
		rect.x = srcX;
		rect.y = srcY;
		rect.width = width;
		rect.height = height;
		OS.gdk_window_invalidate_rect (drawable, rect, false);
//		OS.gdk_window_clear_area_e(drawable, srcX, srcY, width, height);
	} else {
		if (deltaX != 0) {
			int newX = destX - deltaX;
			if (deltaX < 0) newX = destX + width;
			rect.x = newX;
			rect.y = srcY;
			rect.width = Math.abs(deltaX);
			rect.height = height;
			OS.gdk_window_invalidate_rect (drawable, rect, false);
//			OS.gdk_window_clear_area_e(drawable, newX, srcY, Math.abs(deltaX), height);
		}
		if (deltaY != 0) {
			int newY = destY - deltaY;
			if (deltaY < 0) newY = destY + height;
			rect.x = srcX;
			rect.y = newY;
			rect.width = width;
			rect.height = Math.abs(deltaY);
			OS.gdk_window_invalidate_rect (drawable, rect, false);
//			OS.gdk_window_clear_area_e(drawable, srcX, newY, width, Math.abs(deltaY));
		}
	}
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
	int /*long*/ clipRgn = data.clipRgn;
	if (clipRgn != 0) OS.gdk_region_destroy(clipRgn);
	Image image = data.image;
	if (image != null) {
		image.memGC = null;
		if (image.transparentPixel != -1) image.createMask();
	}
	
	int /*long*/ context = data.context;
	if (context != 0) OS.g_object_unref(context);
	int /*long*/ layout = data.layout;
	if (layout != 0) OS.g_object_unref(layout);

	/* Dispose the GC */
	Device device = data.device;
	drawable.internal_dispose_GC(handle, data);

	data.layout = data.context = data.drawable = data.clipRgn = 0;
	drawable = null;
	handle = 0;
	data.image = null;
	data.string = null;
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
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	OS.gdk_draw_arc(data.drawable, handle, 0, x, y, width, height, startAngle * 64, arcAngle * 64);
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
	//CHECK - default style might not be attached to any window
	GdkColor color = new GdkColor();
	OS.gtk_style_get_fg (OS.gtk_widget_get_default_style(), OS.GTK_STATE_NORMAL, color);
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	OS.gdk_gc_set_foreground(handle, color);
	OS.gdk_draw_rectangle(data.drawable, handle, 0, x, y, width - 1, height - 1);
	color.pixel = values.foreground_pixel;
	color.red = values.foreground_red;
	color.green = values.foreground_green;
	color.blue = values.foreground_blue;
	OS.gdk_gc_set_foreground(handle, color);
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
 	OS.gdk_drawable_get_size(srcImage.pixmap, width, height);
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
		drawImageAlpha(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight);
	} else if (srcImage.transparentPixel != -1 || srcImage.mask != 0) {
		drawImageMask(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight);
	} else {
		drawImage(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight);
	}
}
void drawImage(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight) {
	if (srcWidth == destWidth && srcHeight == destHeight) {
		OS.gdk_draw_drawable(data.drawable, handle, srcImage.pixmap, srcX, srcY, destX, destY, destWidth, destHeight);
	} else {
		int /*long*/ pixbuf = scale(srcImage.pixmap, srcX, srcY, srcWidth, srcHeight, destWidth, destHeight);
		if (pixbuf != 0) {
			OS.gdk_pixbuf_render_to_drawable(pixbuf, data.drawable, handle, 0, 0, destX, destY, destWidth, destHeight, OS.GDK_RGB_DITHER_NORMAL, 0, 0);
			OS.g_object_unref(pixbuf);
		}
	}
}
void drawImageAlpha(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight) {
	if (srcImage.alpha == 0) return;
	if (srcImage.alpha == 255) {
		drawImage(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight);
		return;
	}
	int /*long*/ pixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, true, 8, srcWidth, srcHeight);
	if (pixbuf == 0) return;
	int /*long*/ colormap = OS.gdk_colormap_get_system();
	OS.gdk_pixbuf_get_from_drawable(pixbuf, srcImage.pixmap, colormap, srcX, srcY, 0, 0, srcWidth, srcHeight);
	int stride = OS.gdk_pixbuf_get_rowstride(pixbuf);
	int /*long*/ pixels = OS.gdk_pixbuf_get_pixels(pixbuf);
	byte[] line = new byte[stride];
	byte alpha = (byte)srcImage.alpha;
	byte[] alphaData = srcImage.alphaData;
	for (int y=0; y<srcHeight; y++) {
		int alphaIndex = (y + srcY) * imgWidth + srcX;
		OS.memmove(line, pixels + (y * stride), stride);
		for (int x=3; x<stride; x+=4) {
			line[x] = alphaData == null ? alpha : alphaData[alphaIndex++];
		}
		OS.memmove(pixels + (y * stride), line, stride);
	}
	if (srcWidth != destWidth || srcHeight != destHeight) {
		int /*long*/ scaledPixbuf = OS.gdk_pixbuf_scale_simple(pixbuf, destWidth, destHeight, OS.GDK_INTERP_BILINEAR);
		OS.g_object_unref(pixbuf);
		if (scaledPixbuf == 0) return;
		pixbuf = scaledPixbuf;
	}
	OS.gdk_pixbuf_render_to_drawable_alpha(
			pixbuf, data.drawable,
			0, 0, destX, destY, destWidth, destHeight,
			OS.GDK_PIXBUF_ALPHA_BILEVEL, 128, OS.GDK_RGB_DITHER_NORMAL, 0, 0);
	OS.g_object_unref(pixbuf);
}
void drawImageMask(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight) {
	int /*long*/ drawable = data.drawable;
	int /*long*/ colorPixmap = srcImage.pixmap;
	/* Generate the mask if necessary. */
	if (srcImage.transparentPixel != -1) srcImage.createMask();
	int /*long*/ maskPixmap = srcImage.mask;

	if (srcWidth != destWidth || srcHeight != destHeight) {
		//NOT DONE - there must be a better way of scaling a GdkBitmap
		int /*long*/ pixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, true, 8, srcWidth, srcHeight);
		if (pixbuf != 0) {
			int /*long*/ colormap = OS.gdk_colormap_get_system();
			OS.gdk_pixbuf_get_from_drawable(pixbuf, colorPixmap, colormap, srcX, srcY, 0, 0, srcWidth, srcHeight);
			int /*long*/ gdkImagePtr = OS.gdk_drawable_get_image(maskPixmap, 0, 0, imgWidth, imgHeight);
			if (gdkImagePtr != 0) {
				int stride = OS.gdk_pixbuf_get_rowstride(pixbuf);
				int /*long*/ pixels = OS.gdk_pixbuf_get_pixels(pixbuf);
				byte[] line = new byte[stride];
				for (int y=0; y<srcHeight; y++) {
					int /*long*/ offset = pixels + (y * stride);
					OS.memmove(line, offset, stride);
					for (int x=0; x<srcWidth; x++) {
						if (OS.gdk_image_get_pixel(gdkImagePtr, x + srcX, y + srcY) == 0) {
							line[x*4+3] = 0;
						}
					}
					OS.memmove(offset, line, stride);
				}
				OS.g_object_unref(gdkImagePtr);
				int /*long*/ scaledPixbuf = OS.gdk_pixbuf_scale_simple(pixbuf, destWidth, destHeight, OS.GDK_INTERP_BILINEAR);
				if (scaledPixbuf != 0) {
					int /*long*/[] colorBuffer = new int /*long*/[1];
					int /*long*/[] maskBuffer = new int /*long*/[1];
					OS.gdk_pixbuf_render_pixmap_and_mask(scaledPixbuf, colorBuffer, maskBuffer, 128);
					colorPixmap = colorBuffer[0];
					maskPixmap = maskBuffer[0];
					OS.g_object_unref(scaledPixbuf);
				}
			}
			OS.g_object_unref(pixbuf);
		}
		srcX = 0;
		srcY = 0;
		srcWidth = destWidth;
		srcHeight = destHeight;
	}

	/* Merge clipping with mask if necessary */
	if (data.clipRgn != 0)	 {
		int newWidth =  srcX + srcWidth;
		int newHeight = srcY + srcHeight;
		int bytesPerLine = (newWidth + 7) / 8;
		byte[] maskData = new byte[bytesPerLine * newHeight];
		int /*long*/ mask = OS.gdk_bitmap_create_from_data(0, maskData, newWidth, newHeight);
		if (mask != 0) {
			int /*long*/ gc = OS.gdk_gc_new(mask);
			OS.gdk_region_offset(data.clipRgn, -destX + srcX, -destY + srcY);
			OS.gdk_gc_set_clip_region(gc, data.clipRgn);
			OS.gdk_region_offset(data.clipRgn, destX - srcX, destY - srcY);
			GdkColor color = new GdkColor();
			color.pixel = 1;
			OS.gdk_gc_set_foreground(gc, color);
			OS.gdk_draw_rectangle(mask, gc, 1, 0, 0, newWidth, newHeight);
			OS.gdk_gc_set_function(gc, OS.GDK_AND);
			OS.gdk_draw_drawable(mask, gc, maskPixmap, 0, 0, 0, 0, newWidth, newHeight);
			OS.g_object_unref(gc);
			if (maskPixmap != 0 && srcImage.mask != maskPixmap) OS.g_object_unref(maskPixmap);
			maskPixmap = mask;
		}
	}

	/* Blit cliping the mask */
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	OS.gdk_gc_set_clip_mask(handle, maskPixmap);
	OS.gdk_gc_set_clip_origin(handle, destX - srcX, destY - srcY);
	OS.gdk_draw_drawable(drawable, handle, colorPixmap, srcX, srcY, destX, destY, srcWidth, srcHeight);
	OS.gdk_gc_set_values(handle, values, OS.GDK_GC_CLIP_MASK | OS.GDK_GC_CLIP_X_ORIGIN | OS.GDK_GC_CLIP_Y_ORIGIN);
	if (data.clipRgn != 0) OS.gdk_gc_set_clip_region(handle, data.clipRgn);

	/* Destroy scaled pixmaps */
	if (colorPixmap != 0 && srcImage.pixmap != colorPixmap) OS.g_object_unref(colorPixmap);
	if (maskPixmap != 0 && srcImage.mask != maskPixmap) OS.g_object_unref(maskPixmap);
	/* Destroy the image mask if the there is a GC created on the image */
	if (srcImage.transparentPixel != -1 && srcImage.memGC != null) srcImage.destroyMask();
}
int /*long*/ scale(int /*long*/ src, int srcX, int srcY, int srcWidth, int srcHeight, int destWidth, int destHeight) {
	int /*long*/ pixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, false, 8, srcWidth, srcHeight);
	if (pixbuf == 0) return 0;
	int /*long*/ colormap = OS.gdk_colormap_get_system();
	OS.gdk_pixbuf_get_from_drawable(pixbuf, src, colormap, srcX, srcY, 0, 0, srcWidth, srcHeight);
	int /*long*/ scaledPixbuf = OS.gdk_pixbuf_scale_simple(pixbuf, destWidth, destHeight, OS.GDK_INTERP_BILINEAR);
	OS.g_object_unref(pixbuf);
	return scaledPixbuf;
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
	OS.gdk_draw_line (data.drawable, handle, x1, y1, x2, y2);
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
	OS.gdk_draw_arc(data.drawable, handle, 0, x, y, width, height, 0, 23040);
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
public void drawPoint (int x, int y) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.gdk_draw_point(data.drawable, handle, x, y);
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
	OS.gdk_draw_polygon(data.drawable, handle, 0, pointArray, pointArray.length / 2);
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
	OS.gdk_draw_lines(data.drawable, handle, pointArray, pointArray.length / 2);
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
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	OS.gdk_draw_rectangle(data.drawable, handle, 0, x, y, width, height);
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
public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
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

	int /*long*/ drawable = data.drawable;
	if (nw > naw) {
		if (nh > nah) {
			OS.gdk_draw_arc(drawable, handle, 0, nx, ny, naw, nah, 5760, 5760);
			OS.gdk_draw_line(drawable, handle, nx + naw2, ny, nx + nw - naw2, ny);
			OS.gdk_draw_arc(drawable, handle, 0, nx + nw - naw, ny, naw, nah, 0, 5760);
			OS.gdk_draw_line(drawable, handle, nx + nw, ny + nah2, nx + nw, ny + nh - nah2);
			OS.gdk_draw_arc(drawable, handle, 0, nx + nw - naw, ny + nh - nah, naw, nah, 17280, 5760);
			OS.gdk_draw_line(drawable,handle, nx + naw2, ny + nh, nx + nw - naw2, ny + nh);
			OS.gdk_draw_arc(drawable, handle, 0, nx, ny + nh - nah, naw, nah, 11520, 5760);
			OS.gdk_draw_line(drawable, handle,  nx, ny + nah2, nx, ny + nh - nah2);
		} else {
			OS.gdk_draw_arc(drawable, handle, 0, nx, ny, naw, nh, 5760, 11520);
			OS.gdk_draw_line(drawable, handle, nx + naw2, ny, nx + nw - naw2, ny);
			OS.gdk_draw_arc(drawable, handle, 0, nx + nw - naw, ny, naw, nh, 17280, 11520);
			OS.gdk_draw_line(drawable,handle, nx + naw2, ny + nh, nx + nw - naw2, ny + nh);
		}
	} else {
		if (nh > nah) {
			OS.gdk_draw_arc(drawable, handle, 0, nx, ny, nw, nah, 0, 11520);
			OS.gdk_draw_line(drawable, handle, nx + nw, ny + nah2, nx + nw, ny + nh - nah2);
			OS.gdk_draw_arc(drawable, handle, 0, nx, ny + nh - nah, nw, nah, 11520, 11520);
			OS.gdk_draw_line(drawable,handle, nx, ny + nah2, nx, ny + nh - nah2);
		} else {
			OS.gdk_draw_arc(drawable, handle, 0, nx, ny, nw, nh, 0, 23040);
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
	if (string.length() == 0) return;
	setString(string, flags);
	GdkColor background = null;
	GdkGCValues values = null;
	if ((flags & SWT.DRAW_TRANSPARENT) == 0) {
		values = new GdkGCValues();
		OS.gdk_gc_get_values(handle, values);
		background = new GdkColor();
		background.pixel = values.background_pixel;
		int /*long*/ colormap = OS.gdk_colormap_get_system();
		OS.gdk_colormap_query_color(colormap, background.pixel, background);
	}
	if (!data.xorMode) {
		OS.gdk_draw_layout_with_colors(data.drawable, handle, x, y, data.layout, null, background);
	} else {
		int /*long*/ layout = data.layout;
		int[] w = new int[1], h = new int[1];
		OS.pango_layout_get_size(layout, w, h);
		int width = OS.PANGO_PIXELS(w[0]);
		int height = OS.PANGO_PIXELS(h[0]);
		int /*long*/ pixmap = OS.gdk_pixmap_new(OS.GDK_ROOT_PARENT(), width, height, -1);
		if (pixmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int /*long*/ gdkGC = OS.gdk_gc_new(pixmap);
		if (gdkGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		GdkColor foreground = new GdkColor();
		OS.gdk_gc_set_foreground(gdkGC, foreground);
		OS.gdk_draw_rectangle(pixmap, gdkGC, 1, 0, 0, width, height);
		if (values == null) {
			values = new GdkGCValues();
			OS.gdk_gc_get_values(handle, values);
		}
		foreground.pixel = values.foreground_pixel;
		OS.gdk_gc_set_foreground(gdkGC, foreground);
		OS.gdk_draw_layout_with_colors(pixmap, gdkGC, 0, 0, layout, null, background);
		OS.g_object_unref(gdkGC);
		OS.gdk_draw_drawable(data.drawable, handle, pixmap, 0, 0, x, y, width, height);
		OS.g_object_unref(pixmap);
	}
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
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor color = new GdkColor();
	color.pixel = values.background_pixel;
	OS.gdk_gc_set_foreground(handle, color);
	OS.gdk_draw_arc(data.drawable, handle, 1, x, y, width, height, startAngle * 64, arcAngle * 64);
	color.pixel = values.foreground_pixel;
	OS.gdk_gc_set_foreground(handle, color);
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

	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	
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
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor color = new GdkColor();
	color.pixel = values.background_pixel;
	OS.gdk_gc_set_foreground(handle, color);
	OS.gdk_draw_arc(data.drawable, handle, 1, x, y, width, height, 0, 23040);
	color.pixel = values.foreground_pixel;
	OS.gdk_gc_set_foreground(handle, color);
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
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor color = new GdkColor();
	color.pixel = values.background_pixel;
	OS.gdk_gc_set_foreground(handle, color);
	OS.gdk_draw_polygon(data.drawable, handle, 1, pointArray, pointArray.length / 2);
	color.pixel = values.foreground_pixel;
	OS.gdk_gc_set_foreground(handle, color);
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
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor color = new GdkColor();
	color.pixel = values.background_pixel;
	OS.gdk_gc_set_foreground(handle, color);
	OS.gdk_draw_rectangle(data.drawable, handle, 1, x, y, width, height);
	color.pixel = values.foreground_pixel;
	OS.gdk_gc_set_foreground(handle, color);
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
 * @param arcWidth the horizontal diameter of the arc at the four corners
 * @param arcHeight the vertical diameter of the arc at the four corners
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRoundRectangle
 */
public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
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

	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor color = new GdkColor();
	color.pixel = values.background_pixel;
	OS.gdk_gc_set_foreground(handle, color);
	
	int /*long*/ drawable = data.drawable;
	if (nw > naw) {
		if (nh > nah) {
			OS.gdk_draw_arc(drawable, handle, 1, nx, ny, naw, nah, 5760, 5760);
			OS.gdk_draw_rectangle(drawable, handle, 1, nx + naw2, ny, nw - naw2 * 2, nh);
			OS.gdk_draw_arc(drawable, handle, 1, nx + nw - naw, ny, naw, nah, 0, 5760);
			OS.gdk_draw_rectangle(drawable, handle, 1, nx, ny + nah2, naw2, nh - nah2 * 2);
			OS.gdk_draw_arc(drawable, handle, 1, nx + nw - naw, ny + nh - nah, naw, nah, 17280, 5760);
			OS.gdk_draw_rectangle(drawable, handle, 1, nx + nw - naw2, ny + nah2, naw2, nh - nah2 * 2);
			OS.gdk_draw_arc(drawable, handle, 1, nx, ny + nh - nah, naw, nah, 11520, 5760);
		} else {
			OS.gdk_draw_arc(drawable, handle, 1, nx, ny, naw, nh, 5760, 11520);
			OS.gdk_draw_rectangle(drawable, handle, 1, nx + naw2, ny, nw - naw2 * 2, nh);
			OS.gdk_draw_arc(drawable, handle, 1, nx + nw - naw, ny, naw, nh, 17280, 11520);
		}
	} else {
		if (nh > nah) {
			OS.gdk_draw_arc(drawable, handle, 1, nx, ny, nw, nah, 0, 11520);
			OS.gdk_draw_rectangle(drawable, handle, 1, nx, ny + nah2, nw, nh - nah2 * 2);
			OS.gdk_draw_arc(drawable, handle, 1, nx, ny + nh - nah, nw, nah, 11520, 11520);
		} else {
			OS.gdk_draw_arc(drawable, handle, 1, nx, ny, nw, nh, 0, 23040);
		}
	}

	color.pixel = values.foreground_pixel;
	OS.gdk_gc_set_foreground(handle, color);
}

int fixMnemonic (char [] buffer) {
	int i=0, j=0;
	int mnemonic=-1;
	while (i < buffer.length) {
		if ((buffer [j++] = buffer [i++]) == '&') {
			if (i == buffer.length) {continue;}
			if (buffer [i] == '&') {i++; continue;}
			if (mnemonic == -1) mnemonic = j;
			j--;
		}
	}
	while (j < buffer.length) buffer [j++] = 0;
	return mnemonic;
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
	//BOGUS
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
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor color = new GdkColor();
	color.pixel = values.background_pixel;
	int /*long*/ colormap = OS.gdk_colormap_get_system();
	OS.gdk_colormap_query_color(colormap, color.pixel, color);
	return Color.gtk_new(data.device, color);	
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
	//BOGUS
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
	int /*long*/ clipRgn = data.clipRgn;
	if (clipRgn == 0) {
		int[] width = new int[1]; int[] height = new int[1];
		OS.gdk_drawable_get_size(data.drawable, width, height);
		return new Rectangle(0, 0, width[0], height[0]);
	}
	GdkRectangle rect = new GdkRectangle();
	OS.gdk_region_get_clipbox(clipRgn, rect);
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
	int /*long*/ hRegion = region.handle;
	int /*long*/ clipRgn = data.clipRgn;
	OS.gdk_region_subtract(hRegion, hRegion);
	if (data.clipRgn == 0) {
		int[] width = new int[1]; int[] height = new int[1];
		OS.gdk_drawable_get_size(data.drawable, width, height);
		GdkRectangle rect = new GdkRectangle();
		rect.x = 0; rect.y = 0;
		rect.width = width[0]; rect.height = height[0];
		OS.gdk_region_union_with_rect(hRegion, rect);
	} else {
		OS.gdk_region_union(hRegion, clipRgn);
	}
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
	return Font.gtk_new(data.device, data.font);
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
	int /*long*/ context = data.context;
	int /*long*/ lang = OS.pango_context_get_language(context);
	int /*long*/ metrics = OS.pango_context_get_metrics(context, data.font, lang);
	FontMetrics fm = new FontMetrics();
	fm.ascent = OS.PANGO_PIXELS(OS.pango_font_metrics_get_ascent(metrics));
	fm.descent = OS.PANGO_PIXELS(OS.pango_font_metrics_get_descent(metrics));
	fm.averageCharWidth = OS.PANGO_PIXELS(OS.pango_font_metrics_get_approximate_char_width(metrics));
	fm.height = fm.ascent + fm.descent;
	OS.pango_font_metrics_unref(metrics);
	return fm;
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
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor color = new GdkColor();
	color.pixel = values.foreground_pixel;
	int /*long*/ colormap = OS.gdk_colormap_get_system();
	OS.gdk_colormap_query_color(colormap, color.pixel, color);
	return Color.gtk_new(data.device, color);	
}

public int getLineCap() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	int cap = SWT.CAP_FLAT;
	switch (values.cap_style) {
		case OS.GDK_CAP_ROUND: cap = SWT.CAP_ROUND; break;
		case OS.GDK_CAP_BUTT: cap = SWT.CAP_FLAT; break;
		case OS.GDK_CAP_PROJECTING: cap = SWT.CAP_SQUARE; break;
	}
	return cap;
}

public int getLineJoin() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	int join = SWT.JOIN_ROUND;
	switch (values.join_style) {
		case OS.GDK_JOIN_MITER: join = SWT.JOIN_MITER; break;
		case OS.GDK_JOIN_ROUND: join = SWT.JOIN_ROUND; break;
		case OS.GDK_JOIN_BEVEL: join = SWT.JOIN_BEVEL; break;
	}
	return join;
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
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	return values.line_width;
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
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	return values.function == OS.GDK_XOR;
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
	return (int)/*64*/handle;
}

void init(Drawable drawable, GCData data, int /*long*/ gdkGC) {
	int /*long*/ context = OS.gdk_pango_context_get();
	if (context == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.pango_context_set_language(context, OS.gtk_get_default_language());
	OS.pango_context_set_base_dir(context, OS.PANGO_DIRECTION_LTR);
	OS.gdk_pango_context_set_colormap(context, OS.gdk_colormap_get_system());
	data.context = context;	
	int /*long*/ layout = OS.pango_layout_new(context);
	if (layout == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	data.layout = layout;

	GdkColor foreground = data.foreground;
	if (foreground != null) OS.gdk_gc_set_foreground(gdkGC, foreground);
	GdkColor background = data.background;
	if (background != null) OS.gdk_gc_set_background(gdkGC, background);	
	int /*long*/ font = data.font;
	if (font != 0) OS.pango_layout_set_font_description(layout, font);

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
	handle = gdkGC;
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
	OS.gdk_gc_set_background(handle, color.handle);
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
	int /*long*/ clipRgn = data.clipRgn;
	if (clipRgn == 0) {
		data.clipRgn = clipRgn = OS.gdk_region_new();
	} else {
		OS.gdk_region_subtract(clipRgn, clipRgn);
	}
	GdkRectangle rect = new GdkRectangle();
	rect.x = x;  rect.y = y;
	rect.width = width;  rect.height = height;
	OS.gdk_gc_set_clip_rectangle(handle, rect);
	OS.gdk_region_union_with_rect(clipRgn, rect);
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
	int /*long*/ clipRgn = data.clipRgn;
	if (rect == null) {
		OS.gdk_gc_set_clip_region(handle, 0);
		if (clipRgn != 0) {
			OS.gdk_region_destroy(clipRgn);
			data.clipRgn = clipRgn = 0;
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
 * @param region the clipping region.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping(Region region) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int /*long*/ clipRgn = data.clipRgn;
	if (region == null) {
		OS.gdk_gc_set_clip_region(handle, 0);
		if (clipRgn != 0) {
			OS.gdk_region_destroy(clipRgn);
			data.clipRgn = clipRgn = 0;
		}
	} else {
		if (clipRgn == 0) {
			data.clipRgn = clipRgn = OS.gdk_region_new();
		} else {
			OS.gdk_region_subtract(clipRgn, clipRgn);
		}
		OS.gdk_region_union(clipRgn, region.handle);
		OS.gdk_gc_set_clip_region(handle, clipRgn);
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
public void setFont(Font font) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (font == null) font = data.device.systemFont;
	if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int /*long*/ fontHandle = data.font = font.handle;
	OS.pango_layout_set_font_description(data.layout, fontHandle);
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
	OS.gdk_gc_set_foreground(handle, color.handle);
}

public void setLineCap(int cap) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int cap_style = 0;
	switch (cap) {
		case SWT.CAP_ROUND:
			cap_style = OS.GDK_CAP_ROUND;
			break;
		case SWT.CAP_FLAT:
			cap_style = OS.GDK_CAP_BUTT;
			break;
		case SWT.CAP_SQUARE:
			cap_style = OS.GDK_CAP_PROJECTING;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	int line_style = data.lineStyle == SWT.LINE_SOLID ? OS.GDK_LINE_SOLID : OS.GDK_LINE_ON_OFF_DASH;
	OS.gdk_gc_set_line_attributes(handle, values.line_width, line_style, cap_style, values.join_style);
}

public void setLineJoin(int join) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int join_style = 0;
	switch (join) {
		case SWT.JOIN_MITER:
			join_style = OS.GDK_JOIN_MITER;
			break;
		case SWT.JOIN_ROUND:
			join_style = OS.GDK_JOIN_ROUND;
			break;
		case SWT.JOIN_BEVEL:
			join_style = OS.GDK_JOIN_BEVEL;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	int line_style = data.lineStyle == SWT.LINE_SOLID ? OS.GDK_LINE_SOLID : OS.GDK_LINE_ON_OFF_DASH;
	OS.gdk_gc_set_line_attributes(handle, values.line_width, line_style, values.cap_style, join_style);
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
	int line_style = OS.GDK_LINE_ON_OFF_DASH;
	switch (lineStyle) {
		case SWT.LINE_SOLID:
			line_style = OS.GDK_LINE_SOLID;
			break;
		case SWT.LINE_DASH:
			OS.gdk_gc_set_dashes(handle, 0, new byte[] {18, 6}, 2);
			break;
		case SWT.LINE_DOT:
			OS.gdk_gc_set_dashes(handle, 0, new byte[] {3, 3}, 2);
			break;
		case SWT.LINE_DASHDOT:
			OS.gdk_gc_set_dashes(handle, 0, new byte[] {9, 6, 3, 6}, 4);
			break;
		case SWT.LINE_DASHDOTDOT:
			OS.gdk_gc_set_dashes(handle, 0, new byte[] {9, 3, 3, 3, 3, 3}, 6);
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineStyle = lineStyle;
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	OS.gdk_gc_set_line_attributes(handle, values.line_width, line_style, values.cap_style, values.join_style);
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
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	int line_style = data.lineStyle == SWT.LINE_SOLID ? OS.GDK_LINE_SOLID : OS.GDK_LINE_ON_OFF_DASH;
	OS.gdk_gc_set_line_attributes(handle, width, line_style, values.cap_style, values.join_style);
}

void setString(String string, int flags) {
	if (string == data.string && (flags & ~SWT.DRAW_TRANSPARENT) == (data.drawFlags  & ~SWT.DRAW_TRANSPARENT)) {
		return;
	}
	byte[] buffer;
	int mnemonic, length = string.length ();
	int /*long*/ layout = data.layout;
	char[] text = new char[length];
	string.getChars(0, length, text, 0);	
	if ((flags & SWT.DRAW_MNEMONIC) != 0 && (mnemonic = fixMnemonic(text)) != -1) {
		char[] text1 = new char[mnemonic - 1];
		System.arraycopy(text, 0, text1, 0, text1.length);
		byte[] buffer1 = Converter.wcsToMbcs(null, text1, false);
		char[] text2 = new char[text.length - mnemonic];
		System.arraycopy(text, mnemonic - 1, text2, 0, text2.length);
		byte[] buffer2 = Converter.wcsToMbcs(null, text2, false);
		buffer = new byte[buffer1.length + buffer2.length];
		System.arraycopy(buffer1, 0, buffer, 0, buffer1.length);
		System.arraycopy(buffer2, 0, buffer, buffer1.length, buffer2.length);
		int /*long*/ attr_list = OS.pango_attr_list_new();
		int /*long*/ attr = OS.pango_attr_underline_new(OS.PANGO_UNDERLINE_LOW);
		PangoAttribute attribute = new PangoAttribute();
		OS.memmove(attribute, attr, PangoAttribute.sizeof);
		attribute.start_index = buffer1.length;
		attribute.end_index = buffer1.length + 1;
		OS.memmove(attr, attribute, PangoAttribute.sizeof);
		OS.pango_attr_list_insert(attr_list, attr);
		OS.pango_layout_set_attributes(layout, attr_list);
		OS.pango_attr_list_unref(attr_list);
	} else {
		buffer = Converter.wcsToMbcs(null, text, false);
		OS.pango_layout_set_attributes(layout, 0);
	}
	OS.pango_layout_set_text(layout, buffer, buffer.length);
	OS.pango_layout_set_single_paragraph_mode(layout, (flags & SWT.DRAW_DELIMITER) == 0);
	OS.pango_layout_set_tabs(layout, (flags & SWT.DRAW_TAB) != 0 ? 0 : data.device.emptyTab);
	data.string = string;
	data.stringWidth = data.stringHeight = -1;
	data.drawFlags = flags;
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
	OS.gdk_gc_set_function(handle, xor ? OS.GDK_XOR : OS.GDK_COPY);
	data.xorMode = xor;
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
	setString(string, flags);
	if (data.stringWidth != -1) return new Point(data.stringWidth, data.stringHeight);
	int[] width = new int[1], height = new int[1];
	OS.pango_layout_get_size(data.layout, width, height);
	return new Point(data.stringWidth = OS.PANGO_PIXELS(width[0]), data.stringHeight = OS.PANGO_PIXELS(height[0]));
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

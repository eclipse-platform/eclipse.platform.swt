/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
import org.eclipse.swt.internal.photon.*;
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
 * The result of drawing on an image that was created with an indexed
 * palette using a color that is not in the palette is platform specific.
 * Some platforms will match to the nearest color while other will draw
 * the color itself. This happens because the allocated image might use
 * a direct palette on platforms that do not support indexed palette.
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
 * @see <a href="http://www.eclipse.org/swt/snippets/#gc">GC snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: GraphicsExample, PaintExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class GC extends Resource {
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

	int dirtyBits;

	static final int DefaultBack = 0xffffff;
	static final int DefaultFore = 0x000000;
	static final byte[][] DashList = {
		{ },                   // SWT.LINE_SOLID
		{ 18, 6 },             // SWT.LINE_DASH
		{ 3, 3 },              // SWT.LINE_DOT
		{ 9, 6, 3, 6 },       // SWT.LINE_DASHDOT
		{ 9, 3, 3, 3, 3, 3 }  // SWT.LINE_DASHDOTDOT
	};
	// Photon Draw Buffer Size for off screen drawing.
	static int DrawBufferSize = 48 * 1024;
	
	static final int DIRTY_BACKGROUND = 1 << 0;
	static final int DIRTY_FOREGROUND = 1 << 1;
	static final int DIRTY_CLIPPING = 1 << 2;
	static final int DIRTY_FONT = 1 << 3;
	static final int DIRTY_LINESTYLE = 1 << 4;
	static final int DIRTY_LINEWIDTH = 1 << 5;
	static final int DIRTY_LINECAP = 1 << 6;
	static final int DIRTY_LINEJOIN = 1 << 7;
	static final int DIRTY_XORMODE = 1 << 8;
	
GC() {
}

/**	 
 * Constructs a new instance of this class which has been
 * configured to draw on the specified drawable. Sets the
 * foreground color, background color and font in the GC
 * to match those in the drawable.
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
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for GC creation</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS if not called from the thread that created the drawable</li>
 * </ul>
 */
public GC(Drawable drawable) {
	this(drawable, 0);
}

/**	 
 * Constructs a new instance of this class which has been
 * configured to draw on the specified drawable. Sets the
 * foreground color, background color and font in the GC
 * to match those in the drawable.
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
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for GC creation</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS if not called from the thread that created the drawable</li>
 * </ul>
 *  
 * @since 2.1.2
 */
public GC(Drawable drawable, int style) {
	int flags = OS.PtEnter(0);
	try {
		if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		GCData data = new GCData ();
		data.style = checkStyle(style);
		int hDC = drawable.internal_new_GC (data);
		Device device = data.device;
		if (device == null) device = Device.getDevice();
		if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		this.device = data.device = device;
		init (drawable, data, hDC);
		init();
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

static int checkStyle (int style) {
	if ((style & SWT.LEFT_TO_RIGHT) != 0) style &= ~SWT.RIGHT_TO_LEFT;
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
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
	if (image == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (image.type != SWT.BITMAP || image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int flags = OS.PtEnter(0);
	try {
		Rectangle bounds = image.getBounds();
		int memImage = 0;
		PhRect_t rect = new PhRect_t();
		rect.ul_x = (short)x; rect.ul_y = (short)y;
		rect.lr_x = (short)(x + bounds.width - 1); rect.lr_y = (short)(y + bounds.height - 1);
		boolean sharedMem = true;
		int rid = data.rid;
		int widget = data.widget;
		if (rid == OS.Ph_DEV_RID) {
			memImage = OS.PgShmemCreate(OS.PgReadScreenSize(rect), null);
			if (memImage != 0) memImage = OS.PgReadScreen(rect, memImage);
		} else if (widget != 0) {
			short [] widget_x = new short [1], widget_y = new short [1];
			OS.PtGetAbsPosition(widget, widget_x, widget_y);
			rect.ul_x += widget_x[0];
			rect.ul_y += widget_y[0];
			rect.lr_x += widget_y[0];
			rect.lr_y += widget_y[0];
			memImage = OS.PgShmemCreate(OS.PgReadScreenSize(rect), null);
			if (memImage != 0) memImage = OS.PgReadScreen(rect, memImage);
		} else if (data.image != null) {
			memImage = OS.PiCropImage(data.image.handle, rect, 0);
			sharedMem = false;
		}
		if (memImage == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		PhImage_t phImage = new PhImage_t();
		OS.memmove(phImage, memImage, PhImage_t.sizeof);
		PhPoint_t trans = new PhPoint_t();
		PhPoint_t pos = new PhPoint_t();
		PhDim_t scale = new PhDim_t();
		scale.w = (short)bounds.width;
		scale.h = (short)bounds.height;
		int mc = OS.PmMemCreateMC(image.handle, scale, trans);
		int prevContext = OS.PmMemStart(mc);
		OS.PgSetDrawBufferSize(DrawBufferSize);
		if (phImage.palette != 0) OS.PgSetPalette(phImage.palette, 0, (short)0, (short)phImage.colors, OS.Pg_PALSET_SOFT, 0);
		OS.PgDrawImage(phImage.image, phImage.type, pos, scale, phImage.bpl, 0);
		if (phImage.palette != 0) OS.PgSetPalette(0, 0, (short)0, (short)-1, 0, 0);
		OS.PmMemFlush(mc, image.handle);
		OS.PmMemStop(mc);
		OS.PmMemReleaseMC(mc);
		OS.PhDCSetCurrent(prevContext);
		if (sharedMem) {
			OS.PgShmemDestroy(memImage);
		} else {
			phImage.flags = (byte)OS.Ph_RELEASE_IMAGE_ALL;
			OS.memmove(memImage, phImage, PhImage_t.sizeof);
			OS.PhReleaseImage(memImage);
			OS.free(memImage);	
		}
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
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
public void copyArea(int x, int y, int width, int height, int destX, int destY) {
	copyArea(x, y, width, height, destX, destY, true);
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
 * @param paint if <code>true</code> paint events will be generated for old and obscured areas
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public void copyArea(int x, int y, int width, int height, int destX, int destY, boolean paint) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width == 0 || height == 0) return;
	int deltaX = destX - x, deltaY = destY - y;
	if (deltaX == 0 && deltaY == 0) return;

	int flags = OS.PtEnter(0);
	try {
		boolean overlaps = (destX < x + width) && (destY < y + height) &&
			(destX + width > x) && (destY + height > y);
		int widget = data.widget;
		Image image = data.image;
		if (image != null) {
			int drawImage = image.handle;
			PhImage_t phDrawImage = new PhImage_t();
			OS.memmove(phDrawImage, drawImage, PhImage_t.sizeof);
			if (overlaps) {
				PhPoint_t trans = new PhPoint_t();
				PhDim_t scale = new PhDim_t();
				scale.w = (short)width;
				scale.h = (short)height;
				PhPoint_t pos = new PhPoint_t();
				pos.x = (short)-x;
				pos.y = (short)-y;
				PhDim_t dim = new PhDim_t();
				dim.w = (short)Math.min(phDrawImage.size_w, x + width);
				dim.h = (short)Math.min(phDrawImage.size_h, y + height);
				/* Feature on Photon - It is only possible to draw on images of
				   type Pg_IMAGE_PALETTE_BYTE and Pg_IMAGE_DIRECT_888.
				*/
				int type = OS.Pg_IMAGE_PALETTE_BYTE;
				if ((phDrawImage.type & OS.Pg_IMAGE_CLASS_MASK) == OS.Pg_IMAGE_CLASS_DIRECT) {
					type = OS.Pg_IMAGE_DIRECT_888;
				}
				int memImage = OS.PhCreateImage(null, (short)width, (short)height, type, phDrawImage.palette, phDrawImage.colors, 0);
				int mc = OS.PmMemCreateMC(memImage, scale, trans);
				int prevContext = OS.PmMemStart(mc);
				OS.PgSetDrawBufferSize(DrawBufferSize);
				if (phDrawImage.palette != 0) OS.PgSetPalette(phDrawImage.palette, 0, (short)0, (short)phDrawImage.colors, OS.Pg_PALSET_SOFT, 0);
				OS.PgDrawImage(phDrawImage.image, phDrawImage.type, pos, dim, phDrawImage.bpl, 0);
				if (phDrawImage.palette != 0) OS.PgSetPalette(0, 0, (short)0, (short)-1, 0, 0);
				OS.PmMemFlush(mc, memImage);
				OS.PmMemStop(mc);
				OS.PmMemReleaseMC(mc);
				OS.PhDCSetCurrent(prevContext);
				x = (short)0;
				y = (short)0;
				drawImage = memImage;
				OS.memmove(phDrawImage, drawImage, PhImage_t.sizeof);
				phDrawImage.flags = (byte)OS.Ph_RELEASE_IMAGE_ALL;
				OS.memmove(drawImage, phDrawImage, PhImage_t.sizeof);
			}
			PhPoint_t pos = new PhPoint_t();
			pos.x = (short)(destX - x);
			pos.y = (short)(destY - y);
			PhRect_t clip = new PhRect_t();
			clip.ul_x = (short)destX;
			clip.ul_y = (short)destY;
			clip.lr_x = (short)(destX + width - 1);
			clip.lr_y = (short)(destY + height - 1);
			PhDim_t dim = new PhDim_t();
			dim.w = (short)Math.min(phDrawImage.size_w, x + width);
			dim.h = (short)Math.min(phDrawImage.size_h, y + height);
			int prevContext = setGC();
			setGCTranslation();
			setGCClipping();
			OS.PgSetUserClip(clip);
			if (phDrawImage.palette != 0) OS.PgSetPalette(phDrawImage.palette, 0, (short)0, (short)phDrawImage.colors, OS.Pg_PALSET_SOFT, 0);
			OS.PgDrawImage(phDrawImage.image, phDrawImage.type, pos, dim, phDrawImage.bpl, 0);
			if (phDrawImage.palette != 0) OS.PgSetPalette(0, 0, (short)0, (short)-1, 0, 0);
			OS.PgSetUserClip(null);
			unsetGC(prevContext);
			if (drawImage != image.handle) {
				OS.PhReleaseImage(drawImage);
				OS.free(drawImage);
			}
		} else if (widget != 0) {
			PhRect_t rect = new PhRect_t();
			rect.ul_x = (short)x;
			rect.ul_y = (short)y;
			rect.lr_x = (short)(x + width - 1);
			rect.lr_y = (short)(y + height - 1);
			PhPoint_t delta = new PhPoint_t();
			delta.x = (short)deltaX;
			delta.y = (short)deltaY;
			int visibleTiles = getClipping(widget, data.topWidget, true, true, null);
			if ( visibleTiles == 0 )
				OS.PtBlit(widget, rect, delta);
			 else {
				int srcTile = OS.PhGetTile();
				OS.memmove(srcTile, rect, PhRect_t.sizeof);
				OS.PtClippedBlit(widget, srcTile, delta, visibleTiles);
				OS.PhFreeTiles(srcTile);;
				OS.PhFreeTiles(visibleTiles);
			}	
		}
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

void destroy() {
	int flags = OS.PtEnter(0);
	try {
		int clipRects = data.clipRects;
		if (clipRects != 0) {
			OS.free(clipRects);
			data.clipRects = data.clipRectsCount = 0;		
		}
		Image image = data.image;
		if (image != null) {
			flushImage();
			/* Regenerate the mask if necessary */
			if (image.transparentPixel != -1) {
				PhImage_t phImage = new PhImage_t ();
				OS.memmove(phImage, image.handle, PhImage_t.sizeof);
				if (phImage.mask_bm == 0) {
					createMask(image.handle, phImage.type, image.transparentPixel);
				}
			}
			image.memGC = null;
		}
		
		/*
		* Dispose the HDC.
		*/
		drawable.internal_dispose_GC(handle, data);
		drawable = null;
		handle = 0;
		data.image = null;
		data.font = null;
		data.rid = data.widget = data.topWidget = 0;
		data = null;
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
public void drawArc (int x, int y, int width, int height, int startAngle, int arcAngle) {
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
	if (startAngle > 0) {
		if (arcAngle > 0) {
			//No need to modify start angle.
			arcAngle += startAngle;
		} else {
			int newStartAngle;
			int newStopAngle = startAngle;
			if (startAngle > Math.abs(arcAngle)) {
				newStartAngle = startAngle - Math.abs(arcAngle);
			} else {
				newStartAngle = startAngle + 360 - Math.abs(arcAngle);
			}
			startAngle = newStartAngle;
			arcAngle = newStopAngle;
		}
	} else {
		if (arcAngle > 0) {
			arcAngle = arcAngle + startAngle;
			startAngle = 360 - Math.abs(startAngle);
		} else {
			int newStopAngle = 360 + startAngle;
			startAngle = newStopAngle - Math.abs(arcAngle);
			arcAngle = newStopAngle;			
		}
	}
	startAngle = (int) (startAngle * 65536 / 360);
	arcAngle = (int) (arcAngle * 65536 / 360);

	PhPoint_t center = new PhPoint_t();
	center.x = (short)(x + (width / 2));
	center.y = (short)(y + (height / 2));
	PhPoint_t radii = new PhPoint_t();
	radii.x = (short)(width / 2);
	radii.y = (short)(height / 2);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgDrawArc(center, radii, startAngle, arcAngle, OS.Pg_ARC | OS.Pg_DRAW_STROKE);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
 * @see #drawRectangle(int, int, int, int)
 */
public void drawFocus (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();	
		setGCTranslation();
		setGCClipping();
		if (width < 0) width -= width;
		if (height < 0) height -= height;
		OS.PgSetStrokeColor(0x9098F8);
		OS.PgDrawIRect(x, y, x + width - 1, y + height - 1, OS.Pg_DRAW_STROKE);
		OS.PgSetStrokeColor(data.foreground);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
	if (image == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
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
	if (image == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false);
}
void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
	int flags = OS.PtEnter(0);
	try {
		if (image.memGC != null) image.memGC.flushImage();
		int drawImage = image.handle;
		PhImage_t phDrawImage = new PhImage_t();
		OS.memmove(phDrawImage, drawImage, PhImage_t.sizeof);
	 	int imgWidth = phDrawImage.size_w;
	 	int imgHeight = phDrawImage.size_h;
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
		if (srcWidth != destWidth || srcHeight != destHeight) {
			drawImage = scaleImage(image, phDrawImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight);
			if (drawImage == 0) return;
			srcX = (short)0;
			srcY = (short)0;
			srcWidth = (short)destWidth;
			srcHeight = (short)destHeight;
			OS.memmove(phDrawImage, drawImage, PhImage_t.sizeof);
		}
		PhPoint_t pos = new PhPoint_t();
		pos.x = (short)(destX - srcX);
		pos.y = (short)(destY - srcY);
		PhDim_t dim = new PhDim_t();
		dim.w = (short)Math.min(phDrawImage.size_w, srcX + srcWidth);
		dim.h = (short)Math.min(phDrawImage.size_h, srcY + srcHeight);
		PhRect_t clip = new PhRect_t();
		clip.ul_x = (short)destX;
		clip.ul_y = (short)destY;
		clip.lr_x = (short)(destX + destWidth - 1);
		clip.lr_y = (short)(destY + destHeight - 1);
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgSetDrawMode(data.xorMode ? OS.Pg_DrawModeDSx : OS.Pg_DrawModeS);
		dirtyBits |= DIRTY_XORMODE;
		OS.PgSetUserClip(clip);
		if (phDrawImage.palette != 0) OS.PgSetPalette(phDrawImage.palette, 0, (short)0, (short)phDrawImage.colors, OS.Pg_PALSET_SOFT, 0);
		if (phDrawImage.alpha != 0) {
			drawImageAlpha(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, phDrawImage, drawImage, pos, dim);
		} else if (image.transparentPixel != -1) {
			drawImageTransparent(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, phDrawImage, drawImage, pos, dim);
		} else if (phDrawImage.mask_bm != 0) {
			drawImageMask(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, phDrawImage, drawImage, pos, dim);
		} else {
			drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, phDrawImage, drawImage, pos, dim);
		}
		if (phDrawImage.palette != 0) OS.PgSetPalette(0, 0, (short)0, (short)-1, 0, 0);
		OS.PgSetUserClip(null);
		unsetGC(prevContext);	
		if (drawImage != image.handle) {
			phDrawImage.flags = (byte)OS.Ph_RELEASE_IMAGE_ALL;
			OS.memmove(drawImage, phDrawImage, PhImage_t.sizeof);
			OS.PhReleaseImage(drawImage);
			OS.free(drawImage);
		}
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}
void drawImageAlpha(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, PhImage_t phImage, int imgHandle, PhPoint_t pos, PhDim_t dim) {
	PgAlpha_t phAlpha = new PgAlpha_t();
	OS.memmove(phAlpha, phImage.alpha, PgAlpha_t.sizeof);
	if ((phAlpha.alpha_op & OS.Pg_ALPHA_OP_SRC_GLOBAL) != 0) {
		OS.PgSetAlpha(phAlpha.alpha_op, null, 0, phAlpha.src_global_alpha, phAlpha.dest_global_alpha);
		OS.PgAlphaOn();
		OS.PgDrawImage(phImage.image, phImage.type, pos, dim, phImage.bpl, 0);
		OS.PgAlphaOff();
		return;
	}

	/*
	* Feature/Bug in Photon - When drawing images with alpha blending
	* enabled, there is a limitation in the size of the alpha map.
	* This limitation is probably related to the draw buffer size and
	* it seems to be worse when drawing to a memory context.  The
	* fix/workaround is to draw the image line by line.
	*/
	PgMap_t imageMap = new PgMap_t();
	OS.memmove(imageMap, phImage.alpha + 4, PgMap_t.sizeof);
	PgMap_t lineMap = new PgMap_t();
	lineMap.dim_w = imageMap.dim_w;
	lineMap.dim_h = 1;
	/*
	* Feature in Photon - The alpha map set in a graphics context by
	* PgSetAlpha is freed when the graphics context is destroyed.
	*/
	lineMap.map = OS.malloc(lineMap.dim_w);
	OS.PgSetAlpha(phAlpha.alpha_op, lineMap, 0, phAlpha.src_global_alpha, phAlpha.dest_global_alpha);
	OS.PgAlphaOn();
	pos.y = (short)(destY);
	int end = dim.h;
	dim.h = (short)1;
	for (int y=srcY; y<end; y+=lineMap.dim_h) {
		OS.memmove(lineMap.map, imageMap.map + (imageMap.dim_w * y), lineMap.dim_w);
		/* 
		* Bug in Photon - When drawing an image to a memory context created by
		* PmMemCreateMC at a negative position, the alpha map is not offset.
		*/
		if (data.image != null && pos.x < 0) {
			OS.memmove(lineMap.map, lineMap.map - pos.x, lineMap.dim_w + pos.x);
		}
		OS.PgDrawImage(phImage.image + (phImage.bpl * y), phImage.type, pos, dim, phImage.bpl, 0);
		/*
		* Flushing is necessary in order to change the alpha map.
		*/
		if (data.image != null) OS.PmMemFlush(handle, data.image.handle);
		else OS.PgFlush();
		pos.y += lineMap.dim_h;
	}
	OS.PgAlphaOff();
}
void drawImageTransparent(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, PhImage_t phImage, int imgHandle, PhPoint_t pos, PhDim_t dim) {
	/* Generate the mask if necessary */
	if (phImage.mask_bm == 0) {
		createMask(imgHandle, phImage.type, image.transparentPixel);
		OS.memmove(phImage, imgHandle, PhImage_t.sizeof);
	}
	OS.PgDrawTImage(phImage.image, phImage.type, pos, dim, phImage.bpl, 0, phImage.mask_bm, phImage.mask_bpl);
	/* Destroy the mask if there is a GC created on the image */
	if (image.memGC != null && image.handle == imgHandle) {
		OS.free(phImage.mask_bm);
		phImage.mask_bm = 0;
		phImage.mask_bpl = 0;
		OS.memmove(imgHandle, phImage, PhImage_t.sizeof);
	}
}
void drawImageMask(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, PhImage_t phImage, int imgHandle, PhPoint_t pos, PhDim_t dim) {
	OS.PgDrawTImage(phImage.image, phImage.type, pos, dim, phImage.bpl, 0, phImage.mask_bm, phImage.mask_bpl);
}
void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, PhImage_t phImage, int imgHandle, PhPoint_t pos, PhDim_t dim) {
	OS.PgDrawImage(phImage.image, phImage.type, pos, dim, phImage.bpl, 0);
}
static void createMask(int image, int type, int transparent) {
	if ((type & OS.Pg_IMAGE_CLASS_MASK) == OS.Pg_IMAGE_CLASS_PALETTE) {
		transparent = (transparent & 0xFF) | OS.Pg_INDEX_COLOR;
	} else {
 		switch (type) {
 			case OS.Pg_IMAGE_DIRECT_888:
				transparent = ((transparent & 0xFF) << 16) | (transparent & 0xFF00) | ((transparent & 0xFF0000) >> 16);
				break;
			case OS.Pg_IMAGE_DIRECT_8888:
				transparent = ((transparent & 0xFF00) << 8) | ((transparent & 0xFF0000) >> 8) | ((transparent & 0xFF000000) >> 24);
				break;
			case OS.Pg_IMAGE_DIRECT_565:
				transparent = ((transparent & 0xF800) << 8) | ((transparent & 0x7E0) << 5) | ((transparent & 0x1F) << 3);
				break;
			case OS.Pg_IMAGE_DIRECT_555:
				transparent = ((transparent & 0x7C00) << 9) | ((transparent & 0x3E0) << 6) | ((transparent & 0x1F) << 3);
				break;
			case OS.Pg_IMAGE_DIRECT_444:
				transparent = ((transparent & 0xF00) << 12) | ((transparent & 0xF0) << 8) | ((transparent & 0xF) << 4);
				break;
		}
	}
	OS.PhMakeTransBitmap(image, transparent);
}
static int scaleImage(Image image, PhImage_t phImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
	PhPoint_t trans = new PhPoint_t();
	PhDim_t scale = new PhDim_t();
	scale.w = (short)srcWidth;
	scale.h = (short)srcHeight;
	PhPoint_t pos = new PhPoint_t();
	pos.x = (short)-srcX;
	pos.y = (short)-srcY;
	PhDim_t dim = new PhDim_t();
	dim.w = (short)Math.min(phImage.size_w, srcX + srcWidth);
	dim.h = (short)Math.min(phImage.size_h, srcY + srcHeight);
	/*
	* Feature on Photon - It is only possible to draw on images of
	* type Pg_IMAGE_PALETTE_BYTE and Pg_IMAGE_DIRECT_888.
	*/
	int type = OS.Pg_IMAGE_PALETTE_BYTE;
	if ((phImage.type & OS.Pg_IMAGE_CLASS_MASK) == OS.Pg_IMAGE_CLASS_DIRECT) {
		type = OS.Pg_IMAGE_DIRECT_888;
	}
	/* Scale the image */
	int memImage = OS.PhCreateImage(null, (short)destWidth, (short)destHeight, type, phImage.palette, phImage.colors, 0);
	if (memImage == 0) return 0;
	int mc = OS.PmMemCreateMC(memImage, scale, trans);
	if (mc == 0) {
		Image.destroyImage(memImage);
		return 0;
	}
	int prevContext = OS.PmMemStart(mc);
	OS.PgSetDrawBufferSize(DrawBufferSize);
	if (phImage.palette != 0) OS.PgSetPalette(phImage.palette, 0, (short)0, (short)phImage.colors, OS.Pg_PALSET_SOFT, 0);
	OS.PgDrawImage(phImage.image, phImage.type, pos, dim, phImage.bpl, 0);
	if (phImage.palette != 0) OS.PgSetPalette(0, 0, (short)0, (short)-1, 0, 0);
	OS.PmMemFlush(mc, memImage);
	OS.PmMemStop(mc);
	OS.PmMemReleaseMC(mc);
	OS.PhDCSetCurrent(prevContext);
	
	PhImage_t phMemImage = new PhImage_t();
	OS.memmove(phMemImage, memImage, PhImage_t.sizeof);
	if (image.transparentPixel != -1) {
		/* Generate the mask if it was created originally */
		if (phImage.mask_bm != 0) {
			createMask(memImage, phImage.type, image.transparentPixel);
		}
	} else if (phImage.mask_bm != 0) {
		/* Scale the mask */
		int[] palette = new int[2];
		palette[0] = 0x000000;
		palette[1] = 0xffffff;
		int palettePtr = OS.malloc(palette.length * 4);
		OS.memmove(palettePtr, palette, palette.length * 4);
		/*
		* Feature on Photon - It is only possible to draw on images of
		* type Pg_IMAGE_PALETTE_BYTE and Pg_IMAGE_DIRECT_888.
		*/
		int maskImage = OS.PhCreateImage(null, (short)destWidth, (short)destHeight, OS.Pg_IMAGE_PALETTE_BYTE, palettePtr, palette.length, 0);
		if (maskImage == 0) {
			Image.destroyImage(memImage);
			return 0;
		}
		mc = OS.PmMemCreateMC(maskImage, scale, trans);
		if (mc == 0) {
			Image.destroyImage(maskImage);
			Image.destroyImage(memImage);
			return 0;
		}
		prevContext = OS.PmMemStart(mc);
		OS.PgSetDrawBufferSize(DrawBufferSize);
		OS.PgSetFillColor(palette[0]);
		OS.PgSetTextColor(palette[1]);
		OS.PgDrawBitmap(phImage.mask_bm, OS.Pg_BACK_FILL, pos, dim, phImage.mask_bpl, 0);
		OS.PmMemFlush(mc, maskImage);
		OS.PmMemStop(mc);
		OS.PmMemReleaseMC(mc);
		OS.PhDCSetCurrent(prevContext);
		OS.free(palettePtr);
		
		/* Transfer the mask to the scaled image */
		OS.PhMakeTransBitmap(maskImage, 0 | OS.Pg_INDEX_COLOR);			
		PhImage_t phMaskImage = new PhImage_t();
		OS.memmove(phMaskImage, maskImage, PhImage_t.sizeof);
		phMemImage.mask_bm = phMaskImage.mask_bm;
		phMemImage.mask_bpl = phMaskImage.mask_bpl;
		OS.memmove(memImage, phMemImage, PhImage_t.sizeof);
		
		/* Release the temporary image but not the mask data */
		phMaskImage.mask_bm = 0;
		phMaskImage.mask_bpl = 0;
		phMaskImage.flags = (byte)OS.Ph_RELEASE_IMAGE_ALL;
		OS.memmove(maskImage, phMaskImage, PhImage_t.sizeof);
		OS.PhReleaseImage(maskImage);
		OS.free(maskImage);
	} else if (phImage.alpha != 0) {
		PgAlpha_t alpha = new PgAlpha_t();
		OS.memmove(alpha, phImage.alpha, PgAlpha_t.sizeof);
		int alphaPtr = OS.malloc(PgAlpha_t.sizeof);
		if (alphaPtr == 0) {
			Image.destroyImage(memImage);
			return 0;
		}
		
		/* Scale alpha data */
		if (alpha.src_alpha_map_map != 0) {
			int[] palette = new int[256];
			for (int i = 0; i < palette.length; i++) {
				palette[i] = i;
			}
			int palettePtr = OS.malloc(palette.length * 4);
			OS.memmove(palettePtr, palette, palette.length * 4);
			/*
			* Feature on Photon - It is only possible to draw on images of
			* type Pg_IMAGE_PALETTE_BYTE and Pg_IMAGE_DIRECT_888.
			*/
			int alphaImage = OS.PhCreateImage(null, (short)destWidth, (short)destHeight, OS.Pg_IMAGE_PALETTE_BYTE, palettePtr, palette.length, 0);
			if (alphaImage == 0) {
				OS.free(palettePtr);
				OS.free(alphaPtr);
				Image.destroyImage(memImage);
				return 0;
			}
			mc = OS.PmMemCreateMC(alphaImage, scale, trans);
			if (mc == 0) {
				OS.free(palettePtr);
				OS.free(alphaPtr);
				Image.destroyImage(alphaImage);
				Image.destroyImage(memImage);
				return 0;
			}
			prevContext = OS.PmMemStart(mc);
			OS.PgSetPalette(palettePtr, 0, (short)0, (short)palette.length, OS.Pg_PALSET_SOFT, 0);
			OS.PgDrawImage(alpha.src_alpha_map_map, OS.Pg_IMAGE_PALETTE_BYTE, pos, dim, alpha.src_alpha_map_bpl, 0);
			OS.PgSetPalette(0, 0, (short)0, (short)-1, 0, 0);
			OS.PmMemFlush(mc, alphaImage);
			OS.PmMemStop(mc);
			OS.PmMemReleaseMC(mc);
			OS.PhDCSetCurrent(prevContext);
			OS.free(palettePtr);
				
			/* Transfer the image to the scaled image alpha data*/
			PhImage_t phAlphaImage = new PhImage_t();
			OS.memmove(phAlphaImage, alphaImage, PhImage_t.sizeof);
			alpha.src_alpha_map_bpl = (short)phAlphaImage.bpl;
			alpha.src_alpha_map_dim_w = (short)phAlphaImage.bpl;
			alpha.src_alpha_map_dim_h = (short)phAlphaImage.size_h;
			alpha.src_alpha_map_map = phAlphaImage.image;

			/* Release the temporary image but not the image data */
			phAlphaImage.image = 0;
			phAlphaImage.bpl = 0;
			phAlphaImage.flags = (byte)OS.Ph_RELEASE_IMAGE_ALL;
			OS.memmove(alphaImage, phAlphaImage, PhImage_t.sizeof);
			OS.PhReleaseImage(alphaImage);
			OS.free(alphaImage);
		}

		OS.memmove(alphaPtr, alpha, PgAlpha_t.sizeof);
		phMemImage.alpha = alphaPtr;
		OS.memmove(memImage, phMemImage, PhImage_t.sizeof);
	}
	return memImage;
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
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgDrawILine(x1, y1, x2, y2);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
public void drawOval (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	PhPoint_t center = new PhPoint_t();
	center.x = (short)x; center.y = (short)y;
	PhPoint_t radii = new PhPoint_t();
	// Don't subtract one, so that the bottom/right edges are drawn
	radii.x = (short)(x + width); radii.y = (short)(y + height);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgDrawEllipse(center, radii,	OS.Pg_DRAW_STROKE | OS.Pg_EXTENT_BASED);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

/** 
 * Draws the path described by the parameter.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param path the path to draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see Path
 * 
 * @since 3.1
 */
public void drawPath(Path path) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (path.handle == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
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
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgDrawIPixel(x, y);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
	
	short[] points = new short[pointArray.length];
	for (int i = pointArray.length - 1; i >= 0; i--) {
		points[i] = (short)pointArray[i];
	}
	
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgDrawPolygon(points, pointArray.length / 2,	new PhPoint_t(), OS.Pg_DRAW_STROKE | OS.Pg_CLOSED);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
	
	short[] points = new short[pointArray.length];
	for (int i = pointArray.length - 1; i >= 0; i--) {
		points[i] = (short)pointArray[i];
	}
	
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgDrawPolygon(points, pointArray.length / 2,	new PhPoint_t(), OS.Pg_DRAW_STROKE);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		// Don't subtract one, so that the bottom/right edges are drawn
		OS.PgDrawIRect(x, y, x + width, y + height, OS.Pg_DRAW_STROKE);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
public void drawRoundRectangle (int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	PhRect_t rect  = new PhRect_t();
	rect.ul_x = (short)x; rect.ul_y = (short)y;
	// Don't subtract one, so that the bottom/right edges are drawn
	rect.lr_x = (short)(x + width); rect.lr_y = (short)(y + height);
	PhPoint_t radii = new PhPoint_t();
	radii.x = (short)(arcWidth / 2); radii.y = (short)(arcHeight / 2);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgDrawRoundRect(rect, radii, OS.Pg_DRAW_STROKE);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
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

	int drawFlags = OS.Pg_TEXT_LEFT | OS.Pg_TEXT_TOP;
	if (!isTransparent) drawFlags |= OS.Pg_BACK_FILL;
	byte[] buffer = Converter.wcsToMbcs(null, string, false);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();	
		setGCTranslation();
		setGCClipping();
		PhPoint_t pos = new PhPoint_t();
		pos.x = (short)x;
		pos.y = (short)y;
		if (!data.xorMode) {
			OS.PgDrawText(buffer, buffer.length, pos, drawFlags);
		} else {
			if (isTransparent) {
				Font font = data.font;
				PhRect_t rect = new PhRect_t();
				OS.PfExtentText(rect, null, font.handle, buffer, buffer.length);
				short width = (short)(rect.lr_x - rect.ul_x + 1);
				short height = (short)(rect.lr_y - rect.ul_y + 1);
				int image = OS.PhCreateImage(null, width, height, OS.Pg_IMAGE_DIRECT_888, 0, 0, 0);
				PhDim_t dim = new PhDim_t();
				dim.w = width;
				dim.h = height;
				PhPoint_t point = new PhPoint_t();
				int pmMC = OS.PmMemCreateMC(image, dim, point);
				if (pmMC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				int prevCont = OS.PmMemStart(pmMC);
				OS.PgSetTextColor(data.foreground);
				OS.PgSetFont(font.handle);
				pos.x = pos.y = (short)0;
				OS.PgDrawText(buffer, buffer.length, pos, drawFlags);
				OS.PmMemFlush(pmMC, image);
				OS.PmMemStop(pmMC);
				OS.PhDCSetCurrent(prevCont);
				OS.PmMemReleaseMC(pmMC);
				point.x = (short)x;
				point.y = (short)y;
				PhImage_t phImage = new PhImage_t();
				OS.memmove(phImage, image, PhImage_t.sizeof);
				OS.PgSetDrawMode(OS.Pg_DrawModeDSx);
				dirtyBits |= DIRTY_XORMODE;
				OS.PgDrawImage(phImage.image, phImage.type, point, dim, phImage.bpl, 0);
				phImage.flags = (byte)OS.Ph_RELEASE_IMAGE_ALL;
				OS.memmove(image, phImage, PhImage_t.sizeof);
				OS.PhReleaseImage(image);
				OS.free(image);
			} else {
				OS.PgSetTextXORColor(data.foreground, data.background);
				OS.PgSetDrawMode(OS.Pg_DrawModeS);
				OS.PgDrawText(buffer, buffer.length, pos, drawFlags);
				dirtyBits |= DIRTY_XORMODE | DIRTY_FOREGROUND;				
			}
		}
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
 * @param flags the flags specifying how to process the text
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
	if ((flags & ~SWT.DRAW_TRANSPARENT) == 0) {
		drawString(string, x, y, (flags & SWT.DRAW_TRANSPARENT) != 0);
	} else {
		drawText(string, x, y, flags, true);
	}
}

Point drawText(String text, int x, int y, int flags, boolean draw) {
	/* NOT DONE - inline code for performance */
	
	int length = text.length();
	char[] buffer = new char[length];
	text.getChars(0, length, buffer, 0);
	
	/* NOT DONE - tabstops */
	int spaceWidth = stringExtent(" ").x;
	int tabWidth = spaceWidth *8 + 1;
	
	boolean transparent = (flags & SWT.DRAW_TRANSPARENT) != 0;
	int mnemonic = -1;
	int start = 0, i = 0, j = 0;
	int initialX = x, initialY = y;
	int maxX = x, maxY = y;
	while (i < length) {
		char c = buffer[j] = buffer[i];
		switch (c) {
			case '\t': {
				if ((flags & SWT.DRAW_TAB) == 0) break;
				String string = new String(buffer, start, j - start);
				if (draw) drawString(string, x, y, transparent);
				Point extent = stringExtent(string);
				x += extent.x + tabWidth;
				maxX = Math.max(x, maxX);
				maxY = Math.max(y + extent.y, maxY);
				start = j + 1;
				break;
			}
			case '\n': {
				if ((flags & SWT.DRAW_DELIMITER) == 0) break;
				String string = new String(buffer, start, j - start);
				if (draw) drawString(string, x, y, transparent);
				Point extent = stringExtent(string);
				maxX = Math.max(x + extent.x, maxX);
				x = initialX;
				y += extent.y;
				maxY = Math.max(y, maxY);
				start = j + 1;
				break;
			}
			case '&': {
				if ((flags & SWT.DRAW_MNEMONIC) == 0) break;
				if (i + 1 == length) break;
				if (buffer[i + 1] == '&') {i++; break;}
				if (mnemonic == -1) {
					mnemonic = i + 1;
					String string = new String(buffer, start, j - start);
					if (draw) drawString(string, x, y, transparent);
					Point extent = stringExtent(string);
					x += extent.x;
					start = mnemonic;
					string = new String(buffer, start, 1);
					if (draw) drawString(string, x, y, transparent);
					extent = stringExtent(string);
					int underlineY = y + extent.y - 1;
					if (draw) drawLine(x, underlineY, x + extent.x, underlineY);
					x += extent.x;
					maxX = Math.max(x, maxX);
					maxY = Math.max(y + extent.y, maxY);
					start = j + 1;
				}
				j--;
				break;
			}
		}
		j++;
		i++;
	}
	if (start != j) {
		String string = new String(buffer, start, j - start);
		if (draw) drawString(string, x, y, transparent);
		Point extent = stringExtent(string);
		maxX = Math.max(x + extent.x, maxX);
		maxY = Math.max(y + extent.y, maxY);
	}
	return new Point(maxX - initialX, maxY - initialY);
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
	return (object == this) || ((object instanceof GC) && (handle == ((GC)object).handle));
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
public void fillArc (int x, int y, int width, int height, int startAngle, int arcAngle) {
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
	if (startAngle > 0) {
		if (arcAngle > 0) {
			//No need to modify start angle.
			arcAngle += startAngle;
		} else {
			int newStartAngle;
			int newStopAngle = startAngle;
			if (startAngle > Math.abs(arcAngle)) {
				newStartAngle = startAngle - Math.abs(arcAngle);
			} else {
				newStartAngle = startAngle + 360 - Math.abs(arcAngle);
			}
			startAngle = newStartAngle;
			arcAngle = newStopAngle;
		}
	} else {
		if (arcAngle > 0) {
			arcAngle = arcAngle + startAngle;
			startAngle = 360 - Math.abs(startAngle);
		} else {
			int newStopAngle = 360 + startAngle;
			startAngle = newStopAngle - Math.abs(arcAngle);
			arcAngle = newStopAngle;			
		}
	}			
	startAngle = (int) (startAngle * 65536 / 360);
	arcAngle = (int) (arcAngle * 65536 / 360);
	
	PhPoint_t center = new PhPoint_t();
	center.x = (short)(x + (width / 2));
	center.y = (short)(y + (height / 2));
	PhPoint_t radii = new PhPoint_t();
	radii.x = (short)(width / 2);
	radii.y = (short)(height / 2);
	
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgDrawArc(center, radii, startAngle, arcAngle, OS.Pg_ARC_PIE | OS.Pg_DRAW_FILL);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
 * @see #drawRectangle(int, int, int, int)
 */
public void fillGradientRectangle(int x, int y, int width, int height, boolean vertical) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if ((width == 0) || (height == 0)) return;
	int fromColor = data.foreground;
	int toColor = data.background;
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
		final int t = toColor;
		toColor = fromColor;
		fromColor = t;
	}
	if (fromColor == toColor) {
		fillRectangle(x, y, width, height);
		return;
	}
	PhPoint_t upperLeft = new PhPoint_t();
	upperLeft.x = (short)x;
	upperLeft.y = (short)y;
	PhPoint_t lowerRight = new PhPoint_t();
	lowerRight.x = (short)(x + width - 1);
	lowerRight.y = (short)(y + height - 1);
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgDrawGradient(upperLeft, lowerRight,
			vertical ? OS.Pg_GRAD_VERTICAL : OS.Pg_GRAD_HORIZONTAL, OS.Pg_GRAD_LINEAR,
			vertical ? height : width, fromColor, toColor, 0, 0, 0, null);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
	PhPoint_t center = new PhPoint_t();
	center.x = (short)x; center.y = (short)y;
	PhPoint_t radii = new PhPoint_t();
	radii.x = (short)(x + width);
	radii.y = (short)(y + height);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgDrawEllipse(center, radii,	OS.Pg_DRAW_FILL | OS.Pg_EXTENT_BASED);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

/** 
 * Fills the path described by the parameter.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 *
 * @param path the path to fill
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see Path
 * 
 * @since 3.1
 */
public void fillPath (Path path) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (path.handle == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
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
	
	short[] points = new short[pointArray.length];
	for (int i = pointArray.length - 1; i >= 0; i--) {
		points[i] = (short)pointArray[i];
	}
	
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgDrawPolygon(points, pointArray.length / 2,	new PhPoint_t(), OS.Pg_DRAW_FILL | OS.Pg_CLOSED);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
 * @see #drawRectangle(int, int, int, int)
 */
public void fillRectangle (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width == 0 || height == 0) return;
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();	
		setGCTranslation();
		setGCClipping();
		OS.PgDrawIRect(x, y, x + width - 1, y + height - 1, OS.Pg_DRAW_FILL);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
 * @see #drawRectangle(int, int, int, int)
 */
public void fillRectangle (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	fillRectangle (rect.x, rect.y, rect.width, rect.height);
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
public void fillRoundRectangle (int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	PhRect_t rect  = new PhRect_t();
	rect.ul_x = (short)x; rect.ul_y = (short)y;
	rect.lr_x = (short)(x + width - 1); rect.lr_y = (short)(y + height - 1);
	PhPoint_t radii = new PhPoint_t();
	radii.x = (short)(arcWidth / 2); radii.y = (short)(arcHeight / 2);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCTranslation();
		setGCClipping();
		OS.PgDrawRoundRect(rect, radii, OS.Pg_DRAW_FILL);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

/**
 * Force outstanding drawing commands to be processed.
 */
void flushImage () {
	Image image = data.image;
	if (image == null) return;
	int prevContext = OS.PmMemStart(handle);
	OS.PmMemFlush(handle, image.handle);
	OS.PmMemStop(handle);
	OS.PhDCSetCurrent(prevContext);
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
	return getCharWidth(ch);
}

/**
 * Returns <code>true</code> if receiver is using the operating system's
 * advanced graphics subsystem.  Otherwise, <code>false</code> is returned
 * to indicate that normal graphics are in use.
 * <p>
 * Advanced graphics may not be installed for the operating system.  In this
 * case, <code>false</code> is always returned.  Some operating system have
 * only one graphics subsystem.  If this subsystem supports advanced graphics,
 * then <code>true</code> is always returned.  If any graphics operation such
 * as alpha, antialias, patterns, interpolation, paths, clipping or transformation
 * has caused the receiver to switch from regular to advanced graphics mode,
 * <code>true</code> is returned.  If the receiver has been explicitly switched
 * to advanced mode and this mode is supported, <code>true</code> is returned.
 * </p>
 *
 * @return the advanced value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public boolean getAdvanced() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return false;
}

/**
 * Returns the receiver's alpha value. The alpha value
 * is between 0 (transparent) and 255 (opaque).
 *
 * @return the alpha value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public int getAlpha() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return 0xFF;
}

/**
 * Returns the receiver's anti-aliasing setting value, which will be
 * one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or
 * <code>SWT.ON</code>. Note that this controls anti-aliasing for all
 * <em>non-text drawing</em> operations.
 *
 * @return the anti-aliasing setting
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getTextAntialias
 * 
 * @since 3.1
 */
public int getAntialias() {
    if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    return SWT.DEFAULT;
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
	return Color.photon_new(data.device, data.background);
}

/** 
 * Returns the background pattern. The default value is
 * <code>null</code>.
 *
 * @return the receiver's background pattern
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Pattern
 * 
 * @since 3.1
 */
public Pattern getBackgroundPattern() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return null;
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
	String string = new String(new char[] {ch});
	Point point = stringExtent(string);
	return point.x;
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
	int flags = OS.PtEnter(0);
	try {
		PhRect_t rect = new PhRect_t();
		int rid = data.rid;
		int widget = data.widget;
		Image image = data.image;
		if (rid == OS.Ph_DEV_RID) {
			OS.PhRegionQuery (rid, null, rect, 0, 0);
		} else if (widget != 0) {
			OS.PtWidgetCanvas(widget, rect);	
		} else if (image != null) {
			PhImage_t img = new PhImage_t();
			OS.memmove(img, image.handle, PhImage_t.sizeof);
			rect.lr_x = (short)(img.size_w - 1);
			rect.lr_y = (short)(img.size_h - 1);
		}
		int clipRects = data.clipRects;
		if (clipRects != 0) {
			int clipRectsCount = data.clipRectsCount;
			int clip_ptr = OS.malloc(PhRect_t.sizeof);
			OS.memmove(clip_ptr, clipRects, PhRect_t.sizeof);
			for (int i = 1; i < clipRectsCount; i++) {
				OS.PhRectUnion (clip_ptr, clipRects + (i * PhRect_t.sizeof));
			}
			int rect_ptr = OS.malloc(PhRect_t.sizeof);
			OS.memmove(rect_ptr, rect, PhRect_t.sizeof);
			boolean intersect = OS.PhRectIntersect(rect_ptr, clip_ptr) != 0;
			OS.memmove(rect, rect_ptr, PhRect_t.sizeof);
			OS.free(rect_ptr);
			OS.free(clip_ptr);
			if (!intersect) return new Rectangle(0, 0, 0, 0);
		}
		int width = rect.lr_x - rect.ul_x + 1;
		int height = rect.lr_y - rect.ul_y + 1;
		return new Rectangle(rect.ul_x, rect.ul_y, width, height);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
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
public void getClipping (Region region) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int flags = OS.PtEnter(0);
	try {
		if (region.handle != 0 && region.handle != Region.EMPTY_REGION) {
			OS.PhFreeTiles(region.handle);
		}
		int clipRects = data.clipRects;
		if (clipRects != 0) {
			region.handle = OS.PhRectsToTiles(clipRects, data.clipRectsCount);
		} else {
			region.handle = OS.PhGetTile();
			PhRect_t rect = new PhRect_t ();
			int rid = data.rid;
			int widget = data.widget;
			Image image = data.image;
			if (rid == OS.Ph_DEV_RID) {
				OS.PhRegionQuery (rid, null, rect, 0, 0);
			} else if (widget != 0) {
				OS.PtWidgetCanvas(widget, rect);
			} else if (image != null) {
				PhImage_t img = new PhImage_t();
				OS.memmove(img, image.handle, PhImage_t.sizeof);
				rect.lr_x = (short)(img.size_w - 1);
				rect.lr_y = (short)(img.size_h - 1);
			}
			OS.memmove(region.handle, rect, PhRect_t.sizeof);
		}
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

/** 
 * Returns the receiver's fill rule, which will be one of
 * <code>SWT.FILL_EVEN_ODD</code> or <code>SWT.FILL_WINDING</code>.
 *
 * @return the receiver's fill rule
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public int getFillRule() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	//TODO - implement fill rule
	return SWT.FILL_EVEN_ODD;
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
	FontQueryInfo info = new FontQueryInfo();
	OS.PfQueryFontInfo(data.font.handle, info);
	return FontMetrics.photon_new(info);
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
	return Color.photon_new(data.device, data.foreground);
}

/** 
 * Returns the foreground pattern. The default value is
 * <code>null</code>.
 *
 * @return the receiver's foreground pattern
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Pattern
 * 
 * @since 3.1
 */
public Pattern getForegroundPattern() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return null;
}

/** 
 * Returns the GCData.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>GC</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @return the receiver's GCData
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see GCData
 * 
 * @since 3.2
 * @noreference This method is not intended to be referenced by clients.
 */
public GCData getGCData() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data;
}

/** 
 * Returns the receiver's interpolation setting, which will be one of
 * <code>SWT.DEFAULT</code>, <code>SWT.NONE</code>, 
 * <code>SWT.LOW</code> or <code>SWT.HIGH</code>.
 *
 * @return the receiver's interpolation setting
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public int getInterpolation() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return SWT.DEFAULT;
}

/** 
 * Returns the receiver's line attributes.
 *
 * @return the line attributes used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.3 
 */
public LineAttributes getLineAttributes() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	float[] dashes = null;
	if (data.dashes != null) {
		dashes = new float[data.dashes.length];
		for (int i = 0; i < dashes.length; i++) {
			dashes[i] = data.dashes[i];
		}
	}
	return new LineAttributes(data.lineWidth, data.lineCap, data.lineJoin, data.lineStyle, dashes, 0, 10);
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
 * Returns the receiver's line dash style. The default value is
 * <code>null</code>.
 *
 * @return the line dash style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public int[] getLineDash() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	byte[] dashList = data.dashes;
	if (dashList == null) return null;
	int[] dashes = new int[dashList.length];
	for (int i = 0; i < dashes.length; i++) {
		dashes[i] = dashList[i] & 0xFF;
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
 * Returns the receiver's text drawing anti-aliasing setting value,
 * which will be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or
 * <code>SWT.ON</code>. Note that this controls anti-aliasing
 * <em>only</em> for text drawing operations.
 *
 * @return the anti-aliasing setting
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getAntialias
 * 
 * @since 3.1
 */
public int getTextAntialias() {
    if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    return SWT.DEFAULT;
}

/** 
 * Sets the parameter to the transform that is currently being
 * used by the receiver.
 *
 * @param transform the destination to copy the transform into
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Transform
 * 
 * @since 3.1
 */
public void getTransform(Transform transform) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (transform == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (transform.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
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
 * objects that return <code>true</code> when passed to 
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

void init(Drawable drawable, GCData data, int context) {
	if (data.foreground == -1) data.foreground = DefaultFore;
	if (data.background == -1) data.background = DefaultBack;
	if (data.font == null) data.font = data.device.systemFont;
	dirtyBits = DIRTY_FOREGROUND | DIRTY_BACKGROUND | DIRTY_FONT;

	Image image = data.image;
	if (image != null) {
		image.memGC = this;
		int prevContext = OS.PmMemStart(context);
		OS.PgSetDrawBufferSize(DrawBufferSize);
		OS.PmMemStop(context);
		OS.PhDCSetCurrent(prevContext);
		
		/*
		* Destroy the mask when it is generated from a transparent
		* pixel since drawing on the image might change the mask.
		*/
		if (image.transparentPixel != -1) {
			PhImage_t phImage = new PhImage_t ();
			OS.memmove(phImage, image.handle, PhImage_t.sizeof);
			if (phImage.mask_bm != 0) {
				OS.free(phImage.mask_bm);
				phImage.mask_bm = 0;
				phImage.mask_bpl = 0;
				OS.memmove(image.handle, phImage, PhImage_t.sizeof);
			}
		}
	}
	this.drawable = drawable;
	this.data = data;
	handle = context;
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
	return data.clipRects != 0;
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
 * Sets the receiver to always use the operating system's advanced graphics
 * subsystem for all graphics operations if the argument is <code>true</code>.
 * If the argument is <code>false</code>, the advanced graphics subsystem is 
 * no longer used, advanced graphics state is cleared and the normal graphics
 * subsystem is used from now on.
 * <p>
 * Normally, the advanced graphics subsystem is invoked automatically when
 * any one of the alpha, antialias, patterns, interpolation, paths, clipping
 * or transformation operations in the receiver is requested.  When the receiver
 * is switched into advanced mode, the advanced graphics subsystem performs both
 * advanced and normal graphics operations.  Because the two subsystems are
 * different, their output may differ.  Switching to advanced graphics before
 * any graphics operations are performed ensures that the output is consistent.
 * </p><p>
 * Advanced graphics may not be installed for the operating system.  In this
 * case, this operation does nothing.  Some operating system have only one
 * graphics subsystem, so switching from normal to advanced graphics does
 * nothing.  However, switching from advanced to normal graphics will always
 * clear the advanced graphics state, even for operating systems that have
 * only one graphics subsystem.
 * </p>
 *
 * @param advanced the new advanced graphics state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setAlpha
 * @see #setAntialias
 * @see #setBackgroundPattern
 * @see #setClipping(Path)
 * @see #setForegroundPattern
 * @see #setLineAttributes
 * @see #setInterpolation
 * @see #setTextAntialias
 * @see #setTransform
 * @see #getAdvanced
 * 
 * @since 3.1
 */
public void setAdvanced(boolean advanced) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (!advanced) {
		setAlpha(0xFF);
		setAntialias(SWT.DEFAULT);
		setBackgroundPattern(null);
		setClipping((Rectangle)null);
		setForegroundPattern(null);
		setInterpolation(SWT.DEFAULT);
		setTextAntialias(SWT.DEFAULT);
		setTransform(null);
	}
}

/**
 * Sets the receiver's alpha value which must be
 * between 0 (transparent) and 255 (opaque).
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * @param alpha the alpha value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public void setAlpha(int alpha) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

/**
 * Sets the receiver's anti-aliasing value to the parameter, 
 * which must be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code>
 * or <code>SWT.ON</code>. Note that this controls anti-aliasing for all
 * <em>non-text drawing</em> operations.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 *
 * @param antialias the anti-aliasing setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is not one of <code>SWT.DEFAULT</code>,
 *                                 <code>SWT.OFF</code> or <code>SWT.ON</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see #getAdvanced
 * @see #setAdvanced
 * @see #setTextAntialias
 * 
 * @since 3.1
 */
public void setAntialias(int antialias) {
    if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    switch (antialias) {
        case SWT.DEFAULT: break;
        case SWT.OFF: break;
        case SWT.ON:
            break;
        default:
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
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
	data.background = color.handle;
	dirtyBits |= DIRTY_BACKGROUND;
}

/** 
 * Sets the background pattern. The default value is <code>null</code>.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param pattern the new background pattern
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see Pattern
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public void setBackgroundPattern (Pattern pattern) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pattern == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (pattern.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
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
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	int clipRects = data.clipRects;
	if (clipRects != 0)
		OS.free(clipRects);
	clipRects = OS.malloc(PhRect_t.sizeof);
	int clipRectsCount = 1;
	PhRect_t rect = new PhRect_t();
	rect.ul_x = (short)x;
	rect.ul_y = (short)y;
	rect.lr_x = (short)(x + width - 1);
	rect.lr_y = (short)(y + height - 1);
	OS.memmove(clipRects, rect, PhRect_t.sizeof);
	data.clipRects = clipRects;
	data.clipRectsCount = clipRectsCount;
	dirtyBits |= DIRTY_CLIPPING;
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the path specified
 * by the argument.  
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param path the clipping path.
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the path has been disposed</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see Path
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public void setClipping(Path path) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path != null && path.isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the rectangular area specified
 * by the argument.  Specifying <code>null</code> for the
 * rectangle reverts the receiver's clipping area to its
 * original value.
 *
 * @param rect the clipping rectangle or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping (Rectangle rect) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) {
		int clipRects = data.clipRects;
		if (clipRects != 0)
			OS.free(clipRects);
		data.clipRects = data.clipRectsCount = 0;
		dirtyBits |= DIRTY_CLIPPING;
	} else {
		setClipping (rect.x, rect.y, rect.width, rect.height);
	}
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the region specified
 * by the argument.  Specifying <code>null</code> for the
 * region reverts the receiver's clipping area to its
 * original value.
 *
 * @param region the clipping region or <code>null</code>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the region has been disposed</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping (Region region) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region != null && region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int clipRects = data.clipRects;
	int clipRectsCount = data.clipRectsCount;
	if (clipRects != 0)
		OS.free(clipRects);
	if (region == null || region.handle == 0) {
		clipRects = clipRectsCount = 0;
	} else if (region.handle == Region.EMPTY_REGION) {
		clipRects = OS.malloc(PhRect_t.sizeof);
		OS.memset(clipRects, 0, PhRect_t.sizeof);
		clipRectsCount = 1;
	} else {
		int[] clip_rects_count = new int[1];
		clipRects = OS.PhTilesToRects(region.handle, clip_rects_count);
		clipRectsCount = clip_rects_count[0];
	}
	data.clipRects = clipRects;
	data.clipRectsCount = clipRectsCount;
	dirtyBits |= DIRTY_CLIPPING;
}

/** 
 * Sets the receiver's fill rule to the parameter, which must be one of
 * <code>SWT.FILL_EVEN_ODD</code> or <code>SWT.FILL_WINDING</code>.
 *
 * @param rule the new fill rule
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rule is not one of <code>SWT.FILL_EVEN_ODD</code>
 *                                 or <code>SWT.FILL_WINDING</code></li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setFillRule(int rule) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	switch (rule) {
		case SWT.FILL_WINDING:
		case SWT.FILL_EVEN_ODD:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	//TODO - implement fill rule
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
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.font = font == null ? data.device.systemFont : font;
	dirtyBits |= DIRTY_FONT;
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
	data.foreground = color.handle;
	dirtyBits |= DIRTY_FOREGROUND;
}

/** 
 * Sets the receiver's interpolation setting to the parameter, which
 * must be one of <code>SWT.DEFAULT</code>, <code>SWT.NONE</code>, 
 * <code>SWT.LOW</code> or <code>SWT.HIGH</code>.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param interpolation the new interpolation setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rule is not one of <code>SWT.DEFAULT</code>, 
 *                                 <code>SWT.NONE</code>, <code>SWT.LOW</code> or <code>SWT.HIGH</code>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public void setInterpolation(int interpolation) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	switch (interpolation) {
		case SWT.DEFAULT:
		case SWT.NONE:
		case SWT.LOW:
		case SWT.HIGH:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
}

/** 
 * Sets the foreground pattern. The default value is <code>null</code>.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * @param pattern the new foreground pattern
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see Pattern
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public void setForegroundPattern (Pattern pattern) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pattern == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (pattern.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
}

/**
 * Sets the receiver's line attributes.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * @param attributes the line attributes
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the attributes is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the line attributes is not valid</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see LineAttributes
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.3
 */
public void setLineAttributes(LineAttributes attributes) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (attributes == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	//TODO - implement setLineAttributes
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
	switch (cap) {
		case SWT.CAP_ROUND:
		case SWT.CAP_FLAT:
		case SWT.CAP_SQUARE:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineCap = cap;
	dirtyBits |= DIRTY_LINECAP;
}

/** 
 * Sets the receiver's line dash style to the argument. The default
 * value is <code>null</code>. If the argument is not <code>null</code>,
 * the receiver's line style is set to <code>SWT.LINE_CUSTOM</code>, otherwise
 * it is set to <code>SWT.LINE_SOLID</code>.
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
	if (dashes != null && dashes.length != 0) {
		byte[] dashList = new byte[dashes.length];
		for (int i = 0; i < dashes.length; i++) {
			dashList[i] = (byte)dashes[i];
		}
		data.dashes = dashList;
		data.lineStyle = SWT.LINE_CUSTOM;
	} else {
		data.dashes = null;
		data.lineStyle = SWT.LINE_SOLID;
	}
	dirtyBits |= DIRTY_LINESTYLE;
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
	switch (join) {
		case SWT.JOIN_MITER:
		case SWT.JOIN_ROUND:
		case SWT.JOIN_BEVEL:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineJoin = join;
	dirtyBits |= DIRTY_LINEJOIN;
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
		case SWT.LINE_DASH:
		case SWT.LINE_DOT:
		case SWT.LINE_DASHDOT:
		case SWT.LINE_DASHDOTDOT:
			break;
		case SWT.LINE_CUSTOM:
			if (data.dashes == null) lineStyle = SWT.LINE_SOLID;
			break;
		default:
			SWT.error (SWT.ERROR_INVALID_ARGUMENT);
			return;
	}
	data.lineStyle = lineStyle;
	dirtyBits |= DIRTY_LINESTYLE;
}

/** 
 * Sets the width that will be used when drawing lines
 * for all of the figure drawing operations (that is,
 * <code>drawLine</code>, <code>drawRectangle</code>, 
 * <code>drawPolyline</code>, and so forth.
 * <p>
 * Note that line width of zero is used as a hint to
 * indicate that the fastest possible line drawing
 * algorithms should be used. This means that the
 * output may be different from line width one.
 * </p>
 *
 * @param lineWidth the width of a line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setLineWidth(int lineWidth) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	data.lineWidth = lineWidth;
	dirtyBits |= DIRTY_LINEWIDTH;
}

int setGC() {
	int result = 0;
	if (data.image != null) result = OS.PmMemStart(handle);
	else if (data.rid == OS.Ph_DEV_RID || data.widget != 0) result = OS.PgSetGC(handle);
	else return result;
	
	if (dirtyBits != 0) {
		if ((dirtyBits & DIRTY_BACKGROUND) != 0) {
			OS.PgSetFillColor(data.background);
		}
		if ((dirtyBits & DIRTY_FOREGROUND) != 0) {
			int foreColor = data.foreground;
			OS.PgSetStrokeColor(foreColor);
			OS.PgSetTextColor(foreColor);
		}
		if ((dirtyBits & DIRTY_FONT) != 0) {
			Font font = data.font;
			OS.PfLoadMetrics(font.handle);
			OS.PgSetFont(font.handle);
		}
		if ((dirtyBits & DIRTY_CLIPPING) != 0) {
			OS.PgSetMultiClip(data.clipRectsCount, data.clipRects);
		}
		if ((dirtyBits & DIRTY_LINECAP) != 0) {
			int cap_style = 0;
			switch (data.lineCap) {
				case SWT.CAP_ROUND:cap_style = OS.Pg_ROUND_CAP; break;
				case SWT.CAP_FLAT:cap_style = OS.Pg_BUTT_CAP; break;
				case SWT.CAP_SQUARE:cap_style = OS.Pg_SQUARE_CAP; break;
			}
			OS.PgSetStrokeCap(cap_style);
		}
		if ((dirtyBits & DIRTY_LINEJOIN) != 0) {
			int join_style = 0;
			switch (data.lineJoin) {
				case SWT.JOIN_ROUND:join_style = OS.Pg_ROUND_JOIN; break;
				case SWT.JOIN_MITER:join_style = OS.Pg_MITER_JOIN; break;
				case SWT.JOIN_BEVEL:join_style = OS.Pg_BEVEL_JOIN; break;
			}
			OS.PgSetStrokeJoin(join_style);
		}
		if ((dirtyBits & DIRTY_LINESTYLE) != 0) {
			byte[] dashList = null;
			switch (data.lineStyle) {
				case SWT.LINE_SOLID: dashList = DashList[0]; break;
				case SWT.LINE_DASH:	dashList = DashList[1]; break;
				case SWT.LINE_DOT: dashList = DashList[2]; break;
				case SWT.LINE_DASHDOT: dashList = DashList[3]; break;
				case SWT.LINE_DASHDOTDOT: dashList = DashList[4]; break;
				case SWT.LINE_CUSTOM: dashList = data.dashes; break;
			}
			OS.PgSetStrokeDash(dashList, dashList.length, 0x10000);
		}
		if ((dirtyBits & DIRTY_LINEWIDTH) != 0) {
			OS.PgSetStrokeWidth(data.lineWidth);
		}
		if ((dirtyBits & DIRTY_XORMODE) != 0) {
			OS.PgSetDrawMode(data.xorMode ? OS.Pg_DRAWMODE_XOR : OS.Pg_DRAWMODE_OPAQUE);
		}
		dirtyBits = 0;
	}
	return result;
}

void setGCClipping() {
	int rid = data.rid;
    int widget = data.widget;
    if(OS.QNX_MAJOR >= 6 && OS.QNX_MINOR >= 3)
    {
        if(widget > 0)
        {
            int visibleTiles = OS.PtGetVisibleTiles(widget);
            if(data.clipRects != 0)
            {
                int gcClip = OS.PhRectsToTiles(data.clipRects, data.clipRectsCount);
                PhPoint_t pt = new PhPoint_t();
                PhRect_t tran_rect = new PhRect_t();
                OS.PtWidgetExtent(widget, tran_rect);
                OS.PtWidgetOffset(widget, pt);
                pt.x += tran_rect.ul_x;
                pt.y += tran_rect.ul_y;
                OS.PhTranslateTiles(gcClip, pt);
                int inter = OS.PhIntersectTilings(visibleTiles, gcClip, new short[1]);
                if(inter != 0)
                {
                    OS.PgSetMultiClipTiles(inter);
                    OS.free(inter);
                }
                OS.free(gcClip);
            } else
            {
                OS.PgSetMultiClipTiles(visibleTiles);
            }
            OS.free(visibleTiles);
        }
        return;
    }
    if(rid == 1)
        OS.PgSetRegion(rid);
    else
    if(widget != 0)
        OS.PgSetRegion(OS.PtWidgetRid(widget));
    else
    if(data.image != null)
        return;
    if(widget == 0)
        return;
    OS.PgSetMultiClip(data.clipRectsCount, data.clipRects);
    int clip_tile = getClipping(widget, data.topWidget, true, true, null);
    int clip_rects_count[] = new int[1];
    int clip_rects = OS.PhTilesToRects(clip_tile, clip_rects_count);
    OS.PhFreeTiles(clip_tile);
    if(clip_rects_count[0] == 0)
    {
        clip_rects_count[0] = 1;
        OS.free(clip_rects);
        clip_rects = OS.malloc(8);
        OS.memset(clip_rects, 0, 8);
    }
    OS.PgSetClipping((short)clip_rects_count[0], clip_rects);
    OS.free(clip_rects);
}

void setGCTranslation() {
	PhPoint_t pt = new PhPoint_t ();
	PhRect_t tran_rect = new PhRect_t();
	if (data.widget <= 0 ) return; 
	OS.PtWidgetExtent(data.widget, tran_rect) ;
	OS.PtWidgetOffset(data.widget, pt);
	pt.x += tran_rect.ul_x;
	pt.y += tran_rect.ul_y;
	OS.PgSetTranslation(pt,0);
}

int getClipping(int widget, int topWidget, boolean clipChildren, boolean clipSiblings, int[] child_tiles) {
	if(OS.QNX_MAJOR >= 6 && OS.QNX_MINOR >= 3)
        if(widget > 0)
        {
            int visTiles = OS.PtGetVisibleTiles(widget);
            PhPoint_t pt = new PhPoint_t();
            PhRect_t tranRect = new PhRect_t();
            OS.PtWidgetExtent(widget, tranRect);
            OS.PtWidgetOffset(widget, pt);
            pt.x += tranRect.ul_x;
            pt.y += tranRect.ul_y;
            int tranPoint = OS.malloc(4);
            OS.memmove(tranPoint, pt, 4);
            OS.PhDeTranslateTiles(visTiles, tranPoint);
            return visTiles;
        } else
        {
            return 0;
        }
    int child_tile = 0;
    int widget_tile = OS.PhGetTile();
    PhRect_t rect = new PhRect_t();
    int args[] = {
        1006, 0, 0, 2015, 0, 0
    };
    if(clipSiblings && OS.PtWidgetClass(topWidget) != OS.PtWindow())
    {
        for(int temp_widget = topWidget; (temp_widget = OS.PtWidgetBrotherInFront(temp_widget)) != 0;)
            if(OS.PtWidgetIsRealized(temp_widget))
            {
                int tile = OS.PhGetTile();
                if(child_tile == 0)
                    child_tile = tile;
                else
                    child_tile = OS.PhAddMergeTiles(tile, child_tile, null);
                OS.PtWidgetExtent(temp_widget, tile);
                args[1] = args[4] = 0;
                OS.PtGetResources(temp_widget, args.length / 3, args);
                if((args[1] & 0x100) != 0)
                {
                    int basic_flags = args[4];
                    OS.memmove(rect, tile, 8);
                    if((basic_flags & 1) != 0)
                        rect.ul_y++;
                    if((basic_flags & 2) != 0)
                        rect.lr_y--;
                    if((basic_flags & 8) != 0)
                        rect.ul_x++;
                    if((basic_flags & 4) != 0)
                        rect.lr_x--;
                    OS.memmove(tile, rect, 8);
                }
            }

        OS.PtWidgetCanvas(topWidget, widget_tile);
        OS.PhDeTranslateTiles(child_tile, widget_tile);
    }
    if(clipChildren)
    {
        for(int temp_widget = OS.PtWidgetChildBack(widget); temp_widget != 0; temp_widget = OS.PtWidgetBrotherInFront(temp_widget))
            if(OS.PtWidgetIsRealized(temp_widget))
            {
                int tile = OS.PhGetTile();
                if(child_tile == 0)
                    child_tile = tile;
                else
                    child_tile = OS.PhAddMergeTiles(tile, child_tile, null);
                OS.PtWidgetExtent(temp_widget, tile);
                args[1] = args[4] = 0;
                OS.PtGetResources(temp_widget, args.length / 3, args);
                if((args[1] & 0x100) != 0)
                {
                    int basic_flags = args[4];
                    OS.memmove(rect, tile, 8);
                    if((basic_flags & 1) != 0)
                        rect.ul_y++;
                    if((basic_flags & 2) != 0)
                        rect.lr_y--;
                    if((basic_flags & 8) != 0)
                        rect.ul_x++;
                    if((basic_flags & 4) != 0)
                        rect.lr_x--;
                    OS.memmove(tile, rect, 8);
                }
            }

    }
    OS.PtWidgetCanvas(widget, widget_tile);
    OS.PhDeTranslateTiles(widget_tile, widget_tile);
    if(child_tile != 0)
    {
        if(child_tiles != null)
            child_tiles[0] = OS.PhIntersectTilings(widget_tile, child_tile, new short[1]);
        int clip_tile = OS.PhClipTilings(widget_tile, child_tile, null);
        OS.PhFreeTiles(child_tile);
        return clip_tile;
    } else
    {
        return widget_tile;
    }
}

/**
 * Sets the receiver's text anti-aliasing value to the parameter, 
 * which must be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code>
 * or <code>SWT.ON</code>. Note that this controls anti-aliasing only
 * for all <em>text drawing</em> operations.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param antialias the anti-aliasing setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is not one of <code>SWT.DEFAULT</code>,
 *                                 <code>SWT.OFF</code> or <code>SWT.ON</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see #getAdvanced
 * @see #setAdvanced
 * @see #setAntialias
 * 
 * @since 3.1
 */
public void setTextAntialias(int antialias) {
    if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    switch (antialias) {
        case SWT.DEFAULT: break;
        case SWT.OFF: break;
        case SWT.ON: break;
        default:
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
    }
}

/**
 * Sets the transform that is currently being used by the receiver. If
 * the argument is <code>null</code>, the current transform is set to
 * the identity transform.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param transform the transform to set
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see Transform
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public void setTransform(Transform transform) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (transform != null && transform.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
}

/** 
 * If the argument is <code>true</code>, puts the receiver
 * in a drawing mode where the resulting color in the destination
 * is the <em>exclusive or</em> of the color values in the source
 * and the destination, and if the argument is <code>false</code>,
 * puts the receiver in a drawing mode where the destination color
 * is replaced with the source color value.
 * <p>
 * Note that this mode in fundamentally unsupportable on certain
 * platforms, notably Carbon (Mac OS X). Clients that want their
 * code to run on all platforms need to avoid this method.
 * </p>
 *
 * @param xor if <code>true</code>, then <em>xor</em> mode is used, otherwise <em>source copy</em> mode is used
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @deprecated this functionality is not supported on some platforms
 */
public void setXORMode(boolean xor) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	data.xorMode = xor;
	dirtyBits |= DIRTY_XORMODE;
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
	PhRect_t rect = new PhRect_t();
	int size = string.length();
	char[] buffer = new char[size];
	string.getChars(0, size, buffer, 0);

	int flags = OS.PtEnter(0);
	try {
		OS.PfExtentWideText(rect, null, data.font.handle, buffer, size * 2);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
	
	int width;
	if (size == 0) width = 0;
	else width = rect.lr_x - (rect.ul_x < 0 ? rect.ul_x : 0) + 1;
	int height = rect.lr_y - rect.ul_y + 1;
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
 * @param flags the flags specifying how to process the text
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
	if ((flags & ~SWT.DRAW_TRANSPARENT) == 0 || string.length() == 0) {
		return stringExtent(string);
	} else {
		return drawText(string, 0, 0, flags, false);
	}
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

void unsetGC(int prevContext) {
	Image image = data.image;
	if (image != null) {
//		OS.PmMemFlush(handle, image.handle);
		OS.PmMemStop(handle);
		OS.PhDCSetCurrent(prevContext);
	} else if (data.rid == OS.Ph_DEV_RID || data.widget != 0) {
		OS.PgSetGC(prevContext);
//		OS.PgFlush();
	}
}

public static GC photon_new(Drawable drawable, GCData data) {
	GC gc = new GC();
	int context = drawable.internal_new_GC(data);
	gc.device = data.device;
	gc.init(drawable, data, context);
	return gc;
}

}

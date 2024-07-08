/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.internal.swing.CGC;
import org.eclipse.swt.internal.swing.LookAndFeelUtils;
import org.eclipse.swt.internal.swing.Utils;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

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
	public CGC handle;

	Drawable drawable;
	GCData data;

	// Saved Affine Transform
	private AffineTransform saveAT, gcAT;

	// Fill Rule
	private int fillRule = SWT.FILL_EVEN_ODD;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
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
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for GC creation</li>
 * </ul>
 */
public GC(Drawable drawable) {
	this(drawable, SWT.NONE);
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
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for GC creation</li>
 * </ul>
 *  
 * @since 2.1.2
 */
public GC(Drawable drawable, int style) {
	if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	GCData data = new GCData ();
	data.style = checkStyle(style);
	Device device = data.device;
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
  this.device = data.device = device;
  CGC handle = drawable.internal_new_GC(data);
  if (handle == null) SWT.error(SWT.ERROR_NO_HANDLES);
  data.background = java.awt.Color.WHITE;
	init (drawable, data, handle);
	if (device.tracking) device.new_Object(this);
}

static int checkStyle(int style) {
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
	if (drawable instanceof Control) {
		Control control = (Control) drawable;
		Graphics graphics = image.handle.getGraphics();
		graphics.translate(-x, -y);
    control.handle.paint(graphics);
    graphics.translate(x, y);
    return;
	}
	if (drawable instanceof Display) {
	  // TODO: better way?
		Rectangle r = image.getBounds();
    try {
			image.handle = new Robot().createScreenCapture(new java.awt.Rectangle(r.x, r.y, r.width, r.height));
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return;
	}
	// TODO: what about other cases?
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.type != SWT.BITMAP || image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
  
  // TODO: check that it works
  java.awt.Composite oldComposite = handle.getComposite();
  java.awt.Composite newComposite = new java.awt.Composite() {
    public java.awt.CompositeContext createContext(java.awt.image.ColorModel srcColorModel, java.awt.image.ColorModel dstColorModel, java.awt.RenderingHints hints) {
      return new java.awt.CompositeContext() {
        public void compose(java.awt.image.Raster src, java.awt.image.Raster dstIn, java.awt.image.WritableRaster dstOut) {
          // TODO: do the composition
        }
        public void dispose() {
        }
      };
    }
  };
  handle.setComposite(newComposite);
  // TODO: find how to flush
  handle.setComposite(oldComposite);
//  
//  
//	/* Get the HDC for the device */
//	Device device = data.device;
// 	int hDC = device.internal_new_GC(null);
// 	
// 	/* Copy the bitmap area */
//	Rectangle rect = image.getBounds(); 	
//	int memHdc = OS.CreateCompatibleDC(hDC);
//	int hOldBitmap = OS.SelectObject(memHdc, image.handle);
//	OS.BitBlt(memHdc, 0, 0, rect.width, rect.height, handle, x, y, OS.SRCCOPY);
//	OS.SelectObject(memHdc, hOldBitmap);
//	OS.DeleteDC(memHdc);
//	
//	/* Release the HDC for the device */
//	device.internal_dispose_GC(hDC, null);
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
  copyArea(srcX, srcY, width, height, destX, destY, true);
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
public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY, boolean paint) {
  // TODO: check what to do with the paint argument
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  handle.copyArea(srcX, srcY, width, height, destX - srcX, destY - srcY);
//  ensureAreaClean(destX, destY, width, height);
}

//int createDIB(int width, int height) {
//	short depth = 32;
//
//	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
//	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
//	bmiHeader.biWidth = width;
//	bmiHeader.biHeight = -height;
//	bmiHeader.biPlanes = 1;
//	bmiHeader.biBitCount = depth;
//	if (OS.IsWinCE) bmiHeader.biCompression = OS.BI_BITFIELDS;
//	else bmiHeader.biCompression = OS.BI_RGB;
//	byte[]	bmi = new byte[BITMAPINFOHEADER.sizeof + (OS.IsWinCE ? 12 : 0)];
//	OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
//	/* Set the rgb colors into the bitmap info */
//	if (OS.IsWinCE) {
//		int redMask = 0xFF00;
//		int greenMask = 0xFF0000;
//		int blueMask = 0xFF000000;
//		/* big endian */
//		int offset = BITMAPINFOHEADER.sizeof;
//		bmi[offset] = (byte)((redMask & 0xFF000000) >> 24);
//		bmi[offset + 1] = (byte)((redMask & 0xFF0000) >> 16);
//		bmi[offset + 2] = (byte)((redMask & 0xFF00) >> 8);
//		bmi[offset + 3] = (byte)((redMask & 0xFF) >> 0);
//		bmi[offset + 4] = (byte)((greenMask & 0xFF000000) >> 24);
//		bmi[offset + 5] = (byte)((greenMask & 0xFF0000) >> 16);
//		bmi[offset + 6] = (byte)((greenMask & 0xFF00) >> 8);
//		bmi[offset + 7] = (byte)((greenMask & 0xFF) >> 0);
//		bmi[offset + 8] = (byte)((blueMask & 0xFF000000) >> 24);
//		bmi[offset + 9] = (byte)((blueMask & 0xFF0000) >> 16);
//		bmi[offset + 10] = (byte)((blueMask & 0xFF00) >> 8);
//		bmi[offset + 11] = (byte)((blueMask & 0xFF) >> 0);
//	}
//
//	int[] pBits = new int[1];
//	int hDib = OS.CreateDIBSection(0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
//	if (hDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//	return hDib;
//}
//
//int createGdipBrush() {
//	int colorRef = OS.GetBkColor (handle);
//	int rgb = ((colorRef >> 16) & 0xFF) | (colorRef & 0xFF00) | ((colorRef & 0xFF) << 16);
//	int color = Gdip.Color_new(data.alpha << 24 | rgb);
//	if (color == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//	int brush = Gdip.SolidBrush_new(color);
//	if (brush == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//	Gdip.Color_delete(color);
//	return brush;
//}
//
//int createGdipPen() {
//	int style, colorRef, width, size, hPen = OS.GetCurrentObject(handle, OS.OBJ_PEN);
//	if ((size = OS.GetObject(hPen, 0, (LOGPEN)null)) == LOGPEN.sizeof) {
//		LOGPEN logPen = new LOGPEN();
//		OS.GetObject(hPen, LOGPEN.sizeof, logPen);
//		colorRef = logPen.lopnColor;
//		width = logPen.x;
//		style = logPen.lopnStyle;
//	} else {
//		EXTLOGPEN logPen = new EXTLOGPEN();
//		if (size <= EXTLOGPEN.sizeof) {
//			OS.GetObject(hPen, size, logPen);
//		} else {
//			int hHeap = OS.GetProcessHeap();
//			int ptr = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, size);
//			OS.GetObject(hPen, size, ptr);
//			OS.MoveMemory(logPen, ptr, EXTLOGPEN.sizeof);
//			OS.HeapFree(hHeap, 0, ptr);
//		}
//		colorRef = logPen.elpColor;
//		width = logPen.elpWidth;
//		style = logPen.elpPenStyle;
//	}
//	int rgb = ((colorRef >> 16) & 0xFF) | (colorRef & 0xFF00) | ((colorRef & 0xFF) << 16);
//	int color = Gdip.Color_new(data.alpha << 24 | rgb);
//	int pen = Gdip.Pen_new(color, width);
//	Gdip.Color_delete(color);
//	int dashStyle = 0; 
//	switch (style & OS.PS_STYLE_MASK) {
//		case OS.PS_SOLID: dashStyle = Gdip.DashStyleSolid; break;
//		case OS.PS_DOT: dashStyle = Gdip.DashStyleDot; break;
//		case OS.PS_DASH: dashStyle = Gdip.DashStyleDash; break;
//		case OS.PS_DASHDOT: dashStyle = Gdip.DashStyleDashDot; break;
//		case OS.PS_DASHDOTDOT: dashStyle = Gdip.DashStyleDashDotDot; break;
//		case OS.PS_USERSTYLE: {
//			if (data.dashes != null) {
//				float[] pattern = new float[data.dashes.length];
//				for (int i = 0; i < pattern.length; i++) {
//					pattern[i] = (float)data.dashes[i] / width;
//				}
//				Gdip.Pen_SetDashPattern(pen, pattern, pattern.length);
//				dashStyle = Gdip.DashStyleCustom;
//			} else {
//				dashStyle = Gdip.DashStyleSolid;
//			}
//		}
//	}
//	if (dashStyle != Gdip.DashStyleCustom) Gdip.Pen_SetDashStyle(pen, dashStyle);
//	int joinStyle = 0;
//	switch (style & OS.PS_JOIN_MASK) {
//		case OS.PS_JOIN_MITER: joinStyle = Gdip.LineJoinMiter; break;
//		case OS.PS_JOIN_BEVEL: joinStyle = Gdip.LineJoinBevel; break;
//		case OS.PS_JOIN_ROUND: joinStyle = Gdip.LineJoinRound; break;
//	}
//	Gdip.Pen_SetLineJoin(pen, joinStyle);
//	int dashCap = Gdip.DashCapFlat, capStyle = 0;
//	switch (style & OS.PS_ENDCAP_MASK) {
//		case OS.PS_ENDCAP_FLAT: capStyle = Gdip.LineCapFlat; break;
//		case OS.PS_ENDCAP_ROUND: capStyle = Gdip.LineCapRound; dashCap = Gdip.DashCapRound; break;
//		case OS.PS_ENDCAP_SQUARE: capStyle = Gdip.LineCapSquare; break;
//	}
//	Gdip.Pen_SetLineCap(pen, capStyle, capStyle, dashCap);
//	return pen;
//}

/**
 * Disposes of the operating system resources associated with
 * the graphics context. Applications must dispose of all GCs
 * which they allocate.
 */
public void dispose() {
	if (this.handle == null) return;
//	if (data.device.isDisposed()) return;
	
//	if (data.gdipGraphics != 0) Gdip.Graphics_delete(data.gdipGraphics);
//	if (data.gdipPen != 0) Gdip.Pen_delete(data.gdipPen);
//	if (data.gdipBrush != 0) Gdip.SolidBrush_delete(data.gdipBrush);
//	data.gdipBrush = data.gdipPen = data.gdipGraphics = 0;
//
//	/* Select stock pen and brush objects and free resources */
//	if (data.hPen != 0) {
//		int nullPen = OS.GetStockObject(OS.NULL_PEN);
//		OS.SelectObject(handle, nullPen);
//		OS.DeleteObject(data.hPen);
//		data.hPen = 0;
//	}
//	if (data.hBrush != 0) {
//		int nullBrush = OS.GetStockObject(OS.NULL_BRUSH);
//		OS.SelectObject(handle, nullBrush);
//		OS.DeleteObject(data.hBrush);
//		data.hBrush = 0;
//	}
	
//	/*
//	* Put back the original bitmap into the device context.
//	* This will ensure that we have not left a bitmap
//	* selected in it when we delete the HDC.
//	*/
//	int hNullBitmap = data.hNullBitmap;
//	if (hNullBitmap != 0) {
//		OS.SelectObject(handle, hNullBitmap);
//		data.hNullBitmap = 0;
//	}
//	Image image = data.image;
//	if (image != null) image.memGC = null;
//	
//	/*
//	* Dispose the HDC.
//	*/
  handle.dispose();
	Device device = data.device;
	if (drawable != null) drawable.internal_dispose_GC(handle, data);
	drawable = null;
	handle = null;
//	data.image = null;
//	data.ps = null;
	if (device.tracking) device.dispose_Object(this);
//	data.device = null;
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
public void drawArc (int x, int y, int width, int height, int startAngle, int arcAngle) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
  handle.drawArc(x, y, width, height, startAngle, arcAngle);
//  ensureAreaClean(x, y, width, height);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  java.awt.Color oldColor = handle.getColor();
  handle.setColor(LookAndFeelUtils.getFocusColor());
  handle.drawRect(x, y, width, height);
  handle.setColor(oldColor);
//  ensureAreaClean(x, y, width, height);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (image == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (srcWidth == 0 || srcHeight == 0 || destWidth == 0 || destHeight == 0) return;
	if (srcX < 0 || srcY < 0 || srcWidth < 0 || srcHeight < 0 || destWidth < 0 || destHeight < 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (image == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false);	
}

void drawImage(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
  if(srcWidth == -1) {
    srcWidth = srcImage.handle.getWidth();
  }
  if(srcHeight == -1) {
    srcHeight = srcImage.handle.getHeight();
  }
  if(destWidth == -1) {
    destWidth = srcWidth;
  }
  if(destHeight == -1) {
    destHeight = srcHeight;
  }
  // Simple == no stretch
  if(!simple || srcWidth == destWidth && srcHeight == destHeight) {
    handle.drawImage(srcImage.handle, destX, destY, destX + destWidth, destY + destHeight, srcX, srcY, srcX + srcWidth, srcY + srcHeight, null);
  } else {
//    Shape oldClip = handle.getClip();
//    handle.setClip(systemClip);
//    handle.clipRect(destX, destY, destWidth, destHeight);
    handle.drawImage(srcImage.handle, destX, destY, destX + destWidth, destY + destHeight, srcX, srcY, srcX + srcWidth, srcY + srcHeight, null);
//    handle.setClip(oldClip);
  }
//  ensureAreaClean(destX, destY, destWidth, destHeight);
//	switch (srcImage.type) {
//		case SWT.BITMAP:
//			drawBitmap(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple);
//			break;
//		case SWT.ICON:
//			drawIcon(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple);
//			break;
//		default:
//			SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
//	}
}
//
//void drawIcon(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
//	int technology = OS.GetDeviceCaps(handle, OS.TECHNOLOGY);
//
//	/* Simple case: no stretching, entire icon */
//	if (simple && technology != OS.DT_RASPRINTER) {
//		OS.DrawIconEx(handle, destX, destY, srcImage.handle, 0, 0, 0, 0, OS.DI_NORMAL);
//		return;
//	}
//
//	/* Get the icon info */
//	ICONINFO srcIconInfo = new ICONINFO();
//	if (OS.IsWinCE) {
//		Image.GetIconInfo(srcImage, srcIconInfo);
//	} else {
//		OS.GetIconInfo(srcImage.handle, srcIconInfo);
//	}
//
//	/* Get the icon width and height */
//	int hBitmap = srcIconInfo.hbmColor;
//	if (hBitmap == 0) hBitmap = srcIconInfo.hbmMask;
//	BITMAP bm = new BITMAP();
//	OS.GetObject(hBitmap, BITMAP.sizeof, bm);
//	int iconWidth = bm.bmWidth, iconHeight = bm.bmHeight;
//	if (hBitmap == srcIconInfo.hbmMask) iconHeight /= 2;
//	
//	if (simple) {
//		srcWidth = destWidth = iconWidth;
//		srcHeight = destHeight = iconHeight;
//	}
//
//	/* Draw the icon */
//	boolean failed = srcX + srcWidth > iconWidth || srcY + srcHeight > iconHeight;
//	if (!failed) {
//		simple = srcX == 0 && srcY == 0 &&
//			srcWidth == destWidth && srcHeight == destHeight &&
//			srcWidth == iconWidth && srcHeight == iconHeight;
//		if (simple && technology != OS.DT_RASPRINTER)	{
//			/* Simple case: no stretching, entire icon */
//			OS.DrawIconEx(handle, destX, destY, srcImage.handle, 0, 0, 0, 0, OS.DI_NORMAL);
//		} else {
//			/* Get the HDC for the device */
//			Device device = data.device;
// 			int hDC = device.internal_new_GC(null);
// 	
// 			/* Create the icon info and HDC's */
//			ICONINFO newIconInfo = new ICONINFO();
//			newIconInfo.fIcon = true;
//			int srcHdc = OS.CreateCompatibleDC(hDC);
//			int dstHdc = OS.CreateCompatibleDC(hDC);
//						
//			/* Blt the color bitmap */
//			int srcColorY = srcY;
//			int srcColor = srcIconInfo.hbmColor;
//			if (srcColor == 0) {
//				srcColor = srcIconInfo.hbmMask;
//				srcColorY += iconHeight;
//			}
//			int oldSrcBitmap = OS.SelectObject(srcHdc, srcColor);
//			newIconInfo.hbmColor = OS.CreateCompatibleBitmap(srcHdc, destWidth, destHeight);
//			if (newIconInfo.hbmColor == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//			int oldDestBitmap = OS.SelectObject(dstHdc, newIconInfo.hbmColor);
//			boolean stretch = !simple && (srcWidth != destWidth || srcHeight != destHeight);
//			if (stretch) {
//				if (!OS.IsWinCE) OS.SetStretchBltMode(dstHdc, OS.COLORONCOLOR);
//				OS.StretchBlt(dstHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcColorY, srcWidth, srcHeight, OS.SRCCOPY);
//			} else {
//				OS.BitBlt(dstHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcColorY, OS.SRCCOPY);
//			}
//			
//			/* Blt the mask bitmap */
//			OS.SelectObject(srcHdc, srcIconInfo.hbmMask);
//			newIconInfo.hbmMask = OS.CreateBitmap(destWidth, destHeight, 1, 1, null);
//			if (newIconInfo.hbmMask == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//			OS.SelectObject(dstHdc, newIconInfo.hbmMask);
//			if (stretch) {
//				OS.StretchBlt(dstHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, OS.SRCCOPY);
//			} else {
//				OS.BitBlt(dstHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY, OS.SRCCOPY);
//			}
//			
//			if (technology == OS.DT_RASPRINTER) {
//				OS.SelectObject(srcHdc, newIconInfo.hbmColor);
//				OS.SelectObject(dstHdc, srcIconInfo.hbmMask);
//				drawBitmapTransparentByClipping(srcHdc, dstHdc, 0, 0, destWidth, destHeight, destX, destY, destWidth, destHeight, true, destWidth, destHeight);	
//				OS.SelectObject(srcHdc, oldSrcBitmap);
//				OS.SelectObject(dstHdc, oldDestBitmap);
//			} else {
//				OS.SelectObject(srcHdc, oldSrcBitmap);
//				OS.SelectObject(dstHdc, oldDestBitmap);
//				int hIcon = OS.CreateIconIndirect(newIconInfo);
//				if (hIcon == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//				OS.DrawIconEx(handle, destX, destY, hIcon, destWidth, destHeight, 0, 0, OS.DI_NORMAL);
//				OS.DestroyIcon(hIcon);
//			}
//			
//			/* Destroy the new icon src and mask and hdc's*/
//			OS.DeleteObject(newIconInfo.hbmMask);
//			OS.DeleteObject(newIconInfo.hbmColor);
//			OS.DeleteDC(dstHdc);
//			OS.DeleteDC(srcHdc);
//
//			/* Release the HDC for the device */
//			device.internal_dispose_GC(hDC, null);
//		}
//	}
//
//	/* Free icon info */
//	OS.DeleteObject(srcIconInfo.hbmMask);
//	if (srcIconInfo.hbmColor != 0) {
//		OS.DeleteObject(srcIconInfo.hbmColor);
//	}
//	
//	if (failed) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//}
//
//void drawBitmap(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
//	BITMAP bm = new BITMAP();
//	OS.GetObject(srcImage.handle, BITMAP.sizeof, bm);
//	int imgWidth = bm.bmWidth;
//	int imgHeight = bm.bmHeight;
//	if (simple) {
//		srcWidth = destWidth = imgWidth;
//		srcHeight = destHeight = imgHeight;
//	} else {
//		if (srcX + srcWidth > imgWidth || srcY + srcHeight > imgHeight) {
//			SWT.error (SWT.ERROR_INVALID_ARGUMENT);
//		}
//		simple = srcX == 0 && srcY == 0 && 
//			srcWidth == destWidth && destWidth == imgWidth &&
//			srcHeight == destHeight && destHeight == imgHeight;
//	}
//	boolean mustRestore = false;
//	GC memGC = srcImage.memGC;
//	if (memGC != null && !memGC.isDisposed()) {
//		mustRestore = true;
//		GCData data = memGC.data;
//		if (data.hNullBitmap != 0) {
//			OS.SelectObject(memGC.handle, data.hNullBitmap);
//			data.hNullBitmap = 0;
//		}
//	}
//	if (srcImage.alpha != -1 || srcImage.alphaData != null) {
//		drawBitmapAlpha(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, bm, imgWidth, imgHeight);
//	} else if (srcImage.transparentPixel != -1) {
//		drawBitmapTransparent(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, bm, imgWidth, imgHeight);
//	} else {
//		drawBitmap(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, bm, imgWidth, imgHeight);
//	}
//	if (mustRestore) {
//		int hOldBitmap = OS.SelectObject(memGC.handle, srcImage.handle);
//		memGC.data.hNullBitmap = hOldBitmap;
//	}
//}
//
//void drawBitmapAlpha(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, BITMAP bm, int imgWidth, int imgHeight) {
//	/* Simple cases */
//	if (srcImage.alpha == 0) return;
//	if (srcImage.alpha == 255) {
//		drawBitmap(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, bm, imgWidth, imgHeight);
//		return;
//	}
//
//	/* Check clipping */
//	Rectangle rect = getClipping();
//	rect = rect.intersection(new Rectangle(destX, destY, destWidth, destHeight));
//	if (rect.isEmpty()) return;
//
//	/* 
//	* Optimization.  Recalculate src and dest rectangles so that
//	* only the clipping area is drawn.
//	*/
//	int sx1 = srcX + (((rect.x - destX) * srcWidth) / destWidth);
//	int sx2 = srcX + ((((rect.x + rect.width) - destX) * srcWidth) / destWidth);
//	int sy1 = srcY + (((rect.y - destY) * srcHeight) / destHeight);
//	int sy2 = srcY + ((((rect.y + rect.height) - destY) * srcHeight) / destHeight);
//	destX = rect.x;
//	destY = rect.y;
//	destWidth = rect.width;
//	destHeight = rect.height;
//	srcX = sx1;
//	srcY = sy1;
//	srcWidth = Math.max(1, sx2 - sx1);
//	srcHeight = Math.max(1, sy2 - sy1);
//	
//	/* Create resources */
//	int srcHdc = OS.CreateCompatibleDC(handle);
//	int oldSrcBitmap = OS.SelectObject(srcHdc, srcImage.handle);
//	int memHdc = OS.CreateCompatibleDC(handle);
//	int memDib = createDIB(Math.max(srcWidth, destWidth), Math.max(srcHeight, destHeight));
//	int oldMemBitmap = OS.SelectObject(memHdc, memDib);
//
//	BITMAP dibBM = new BITMAP();
//	OS.GetObject(memDib, BITMAP.sizeof, dibBM);
//	int sizeInBytes = dibBM.bmWidthBytes * dibBM.bmHeight;
//
//	/* Get the background pixels */
//	OS.BitBlt(memHdc, 0, 0, destWidth, destHeight, handle, destX, destY, OS.SRCCOPY);
//	byte[] destData = new byte[sizeInBytes];
//	OS.MoveMemory(destData, dibBM.bmBits, sizeInBytes);
//
// 	/* Get the foreground pixels */
// 	OS.BitBlt(memHdc, 0, 0, srcWidth, srcHeight, srcHdc, srcX, srcY, OS.SRCCOPY);
// 	byte[] srcData = new byte[sizeInBytes];
//	OS.MoveMemory(srcData, dibBM.bmBits, sizeInBytes);
//	
//	/* Merge the alpha channel in place */
//	int alpha = srcImage.alpha;
//	final boolean hasAlphaChannel = (srcImage.alpha == -1);
//	if (hasAlphaChannel) {
//		final int apinc = imgWidth - srcWidth;
//		final int spinc = dibBM.bmWidthBytes - srcWidth * 4;
//		int ap = srcY * imgWidth + srcX, sp = 3;
//		byte[] alphaData = srcImage.alphaData;
//		for (int y = 0; y < srcHeight; ++y) {
//			for (int x = 0; x < srcWidth; ++x) {
//				srcData[sp] = alphaData[ap++];
//				sp += 4;
//			}
//			ap += apinc;
//			sp += spinc;
//		}
//	}
//	
//	/* Scale the foreground pixels with alpha */
//	OS.MoveMemory(dibBM.bmBits, srcData, sizeInBytes);
//	/* 
//	* Bug in WinCE and Win98.  StretchBlt does not correctly stretch when
//	* the source and destination HDCs are the same.  The workaround is to
//	* stretch to a temporary HDC and blit back into the original HDC.
//	* Note that on WinCE StretchBlt correctly compresses the image when the
//	* source and destination HDCs are the same.
//	*/
//	if ((OS.IsWinCE && (destWidth > srcWidth || destHeight > srcHeight)) || (!OS.IsWinNT && !OS.IsWinCE)) {
//		int tempHdc = OS.CreateCompatibleDC(handle);
//		int tempDib = createDIB(destWidth, destHeight);
//		int oldTempBitmap = OS.SelectObject(tempHdc, tempDib);
//		if (!simple && (srcWidth != destWidth || srcHeight != destHeight)) {
//			if (!OS.IsWinCE) OS.SetStretchBltMode(memHdc, OS.COLORONCOLOR);
//			OS.StretchBlt(tempHdc, 0, 0, destWidth, destHeight, memHdc, 0, 0, srcWidth, srcHeight, OS.SRCCOPY);
//		} else {
//			OS.BitBlt(tempHdc, 0, 0, destWidth, destHeight, memHdc, 0, 0, OS.SRCCOPY);
//		}
//		OS.BitBlt(memHdc, 0, 0, destWidth, destHeight, tempHdc, 0, 0, OS.SRCCOPY);
//		OS.SelectObject(tempHdc, oldTempBitmap);
//		OS.DeleteObject(tempDib);
//		OS.DeleteDC(tempHdc);
//	} else {
//		if (!simple && (srcWidth != destWidth || srcHeight != destHeight)) {
//			if (!OS.IsWinCE) OS.SetStretchBltMode(memHdc, OS.COLORONCOLOR);
//			OS.StretchBlt(memHdc, 0, 0, destWidth, destHeight, memHdc, 0, 0, srcWidth, srcHeight, OS.SRCCOPY);
//		} else {
//			OS.BitBlt(memHdc, 0, 0, destWidth, destHeight, memHdc, 0, 0, OS.SRCCOPY);
//		}
//	}
//	OS.MoveMemory(srcData, dibBM.bmBits, sizeInBytes);
//	
//	/* Compose the pixels */
//	final int dpinc = dibBM.bmWidthBytes - destWidth * 4;
//	int dp = 0;
//	for (int y = 0; y < destHeight; ++y) {
//		for (int x = 0; x < destWidth; ++x) {
//			if (hasAlphaChannel) alpha = srcData[dp + 3] & 0xff;
//			destData[dp] += ((srcData[dp] & 0xff) - (destData[dp] & 0xff)) * alpha / 255;
//			destData[dp + 1] += ((srcData[dp + 1] & 0xff) - (destData[dp + 1] & 0xff)) * alpha / 255;
//			destData[dp + 2] += ((srcData[dp + 2] & 0xff) - (destData[dp + 2] & 0xff)) * alpha / 255;
//			dp += 4;
//		}
//		dp += dpinc;
//	}
//
//	/* Draw the composed pixels */
//	OS.MoveMemory(dibBM.bmBits, destData, sizeInBytes);
//	OS.BitBlt(handle, destX, destY, destWidth, destHeight, memHdc, 0, 0, OS.SRCCOPY);
//
//	/* Free resources */
//	OS.SelectObject(memHdc, oldMemBitmap);
//	OS.DeleteDC(memHdc);
//	OS.DeleteObject(memDib);
//	OS.SelectObject(srcHdc, oldSrcBitmap);
//	OS.DeleteDC(srcHdc);
//}
//
//void drawBitmapTransparentByClipping(int srcHdc, int maskHdc, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight) {
//	int rgn = OS.CreateRectRgn(0, 0, 0, 0);
//	for (int y=0; y<imgHeight; y++) {
//		for (int x=0; x<imgWidth; x++) {
//			if (OS.GetPixel(maskHdc, x, y) == 0) {
//				int tempRgn = OS.CreateRectRgn(x, y, x+1, y+1);
//				OS.CombineRgn(rgn, rgn, tempRgn, OS.RGN_OR);
//				OS.DeleteObject(tempRgn);
//			}
//		}
//	}
//	OS.OffsetRgn(rgn, destX, destY);
//	int clip = OS.CreateRectRgn(0, 0, 0, 0);
//	int result = OS.GetClipRgn(handle, clip);
//	if (result == 1) OS.CombineRgn(rgn, rgn, clip, OS.RGN_AND);
//	OS.SelectClipRgn(handle, rgn);
//	int rop2 = 0;
//	if (!OS.IsWinCE) {
//		rop2 = OS.GetROP2(handle);
//	} else {
//		rop2 = OS.SetROP2 (handle, OS.R2_COPYPEN);
//		OS.SetROP2 (handle, rop2);
//	}
//	int dwRop = rop2 == OS.R2_XORPEN ? OS.SRCINVERT : OS.SRCCOPY;
//	if (!simple && (srcWidth != destWidth || srcHeight != destHeight)) {
//		int mode = 0;
//		if (!OS.IsWinCE) mode = OS.SetStretchBltMode(handle, OS.COLORONCOLOR);
//		OS.StretchBlt(handle, destX, destY, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, dwRop);
//		if (!OS.IsWinCE) OS.SetStretchBltMode(handle, mode);
//	} else {
//		OS.BitBlt(handle, destX, destY, destWidth, destHeight, srcHdc, srcX, srcY, dwRop);
//	}
//	OS.SelectClipRgn(handle, result == 1 ? clip : 0);
//	OS.DeleteObject(clip);
//	OS.DeleteObject(rgn);
//}
//
//void drawBitmapTransparent(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, BITMAP bm, int imgWidth, int imgHeight) {
//
//	/* Get the HDC for the device */
//	Device device = data.device;
// 	int hDC = device.internal_new_GC(null);
// 		
//	/* Find the RGB values for the transparent pixel. */
//	int transBlue = 0, transGreen = 0, transRed = 0;
//	boolean isDib = bm.bmBits != 0;
//	int hBitmap = srcImage.handle;
//	int srcHdc = OS.CreateCompatibleDC(handle);
//	int oldSrcBitmap = OS.SelectObject(srcHdc, hBitmap);
//	byte[] originalColors = null;
//	if (bm.bmBitsPixel <= 8) {
//		if (isDib) {
//			/* Palette-based DIBSECTION */
//			if (OS.IsWinCE) {
//				byte[] pBits = new byte[1];
//				OS.MoveMemory(pBits, bm.bmBits, 1);
//				byte oldValue = pBits[0];			
//				int mask = (0xFF << (8 - bm.bmBitsPixel)) & 0x00FF;
//				pBits[0] = (byte)((srcImage.transparentPixel << (8 - bm.bmBitsPixel)) | (pBits[0] & ~mask));
//				OS.MoveMemory(bm.bmBits, pBits, 1);
//				int color = OS.GetPixel(srcHdc, 0, 0);
//          		pBits[0] = oldValue;
//           		OS.MoveMemory(bm.bmBits, pBits, 1);				
//				transBlue = (color & 0xFF0000) >> 16;
//				transGreen = (color & 0xFF00) >> 8;
//				transRed = color & 0xFF;				
//			} else {
//				int maxColors = 1 << bm.bmBitsPixel;
//				byte[] oldColors = new byte[maxColors * 4];
//				OS.GetDIBColorTable(srcHdc, 0, maxColors, oldColors);
//				int offset = srcImage.transparentPixel * 4;
//				byte[] newColors = new byte[oldColors.length];
//				transRed = transGreen = transBlue = 0xff;
//				newColors[offset] = (byte)transBlue;
//				newColors[offset+1] = (byte)transGreen;
//				newColors[offset+2] = (byte)transRed;
//				OS.SetDIBColorTable(srcHdc, 0, maxColors, newColors);
//				originalColors = oldColors;
//			}
//		} else {
//			/* Palette-based bitmap */
//			int numColors = 1 << bm.bmBitsPixel;
//			/* Set the few fields necessary to get the RGB data out */
//			BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
//			bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
//			bmiHeader.biPlanes = bm.bmPlanes;
//			bmiHeader.biBitCount = bm.bmBitsPixel;
//			byte[] bmi = new byte[BITMAPINFOHEADER.sizeof + numColors * 4];
//			OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
//			if (OS.IsWinCE) SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//			OS.GetDIBits(srcHdc, srcImage.handle, 0, 0, 0, bmi, OS.DIB_RGB_COLORS);
//			int offset = BITMAPINFOHEADER.sizeof + 4 * srcImage.transparentPixel;
//			transRed = bmi[offset + 2] & 0xFF;
//			transGreen = bmi[offset + 1] & 0xFF;
//			transBlue = bmi[offset] & 0xFF;
//		}
//	} else {
//		/* Direct color image */
//		int pixel = srcImage.transparentPixel;
//		switch (bm.bmBitsPixel) {
//			case 16:
//				transBlue = (pixel & 0x1F) << 3;
//				transGreen = (pixel & 0x3E0) >> 2;
//				transRed = (pixel & 0x7C00) >> 7;
//				break;
//			case 24:
//				transBlue = (pixel & 0xFF0000) >> 16;
//				transGreen = (pixel & 0xFF00) >> 8;
//				transRed = pixel & 0xFF;
//				break;
//			case 32:
//				transBlue = (pixel & 0xFF000000) >>> 24;
//				transGreen = (pixel & 0xFF0000) >> 16;
//				transRed = (pixel & 0xFF00) >> 8;
//				break;
//		}
//	}
//
//	if (OS.IsWinCE) {
//		/*
//		* Note in WinCE. TransparentImage uses the first entry of a palette
//		* based image when there are multiple entries that have the same
//		* transparent color.
//		*/
//		int transparentColor = transBlue << 16 | transGreen << 8 | transRed;
//		OS.TransparentImage(handle, destX, destY, destWidth, destHeight,
//			srcHdc, srcX, srcY, srcWidth, srcHeight, transparentColor);
//	} else {
//		/* Create the mask for the source image */
//		int maskHdc = OS.CreateCompatibleDC(hDC);
//		int maskBitmap = OS.CreateBitmap(imgWidth, imgHeight, 1, 1, null);
//		int oldMaskBitmap = OS.SelectObject(maskHdc, maskBitmap);
//		OS.SetBkColor(srcHdc, (transBlue << 16) | (transGreen << 8) | transRed);
//		OS.BitBlt(maskHdc, 0, 0, imgWidth, imgHeight, srcHdc, 0, 0, OS.SRCCOPY);
//		if (originalColors != null) OS.SetDIBColorTable(srcHdc, 0, 1 << bm.bmBitsPixel, originalColors);
//	
//		if (OS.GetDeviceCaps(handle, OS.TECHNOLOGY) == OS.DT_RASPRINTER) {
//			/* Most printers do not support BitBlt(), draw the source bitmap transparently using clipping */
//			drawBitmapTransparentByClipping(srcHdc, maskHdc, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight);
//		} else {
//			/* Draw the source bitmap transparently using invert/and mask/invert */
//			int tempHdc = OS.CreateCompatibleDC(hDC);
//			int tempBitmap = OS.CreateCompatibleBitmap(hDC, destWidth, destHeight);	
//			int oldTempBitmap = OS.SelectObject(tempHdc, tempBitmap);
//			OS.BitBlt(tempHdc, 0, 0, destWidth, destHeight, handle, destX, destY, OS.SRCCOPY);
//			if (!simple && (srcWidth != destWidth || srcHeight != destHeight)) {
//				if (!OS.IsWinCE) OS.SetStretchBltMode(tempHdc, OS.COLORONCOLOR);
//				OS.StretchBlt(tempHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, OS.SRCINVERT);
//				OS.StretchBlt(tempHdc, 0, 0, destWidth, destHeight, maskHdc, srcX, srcY, srcWidth, srcHeight, OS.SRCAND);
//				OS.StretchBlt(tempHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, OS.SRCINVERT);
//			} else {
//				OS.BitBlt(tempHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY, OS.SRCINVERT);
//				OS.BitBlt(tempHdc, 0, 0, destWidth, destHeight, maskHdc, srcX, srcY, OS.SRCAND);
//				OS.BitBlt(tempHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY, OS.SRCINVERT);
//			}
//			OS.BitBlt(handle, destX, destY, destWidth, destHeight, tempHdc, 0, 0, OS.SRCCOPY);
//			OS.SelectObject(tempHdc, oldTempBitmap);
//			OS.DeleteDC(tempHdc);
//			OS.DeleteObject(tempBitmap);
//		}
//		OS.SelectObject(maskHdc, oldMaskBitmap);
//		OS.DeleteDC(maskHdc);
//		OS.DeleteObject(maskBitmap);
//	}
//	OS.SelectObject(srcHdc, oldSrcBitmap);
//	if (hBitmap != srcImage.handle) OS.DeleteObject(hBitmap);
//	OS.DeleteDC(srcHdc);
//	
//	/* Release the HDC for the device */
//	device.internal_dispose_GC(hDC, null);
//}
//
//void drawBitmap(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, BITMAP bm, int imgWidth, int imgHeight) {
//	if (data.gdipGraphics != 0) {
//		//TODO - cache bitmap
//		int img = Gdip.Bitmap_new(srcImage.handle, 0);
//		Rect rect = new Rect();
//		rect.X = destX;
//		rect.Y = destY;
//		rect.Width = destWidth;
//		rect.Height = destHeight;
//		Gdip.Graphics_DrawImage(data.gdipGraphics, img, rect, srcX, srcY, srcWidth, srcHeight, Gdip.UnitPixel, 0, 0, 0);
//		Gdip.Bitmap_delete(img);			
//		return;
//	}
//	int srcHdc = OS.CreateCompatibleDC(handle);
//	int oldSrcBitmap = OS.SelectObject(srcHdc, srcImage.handle);
//	int rop2 = 0;
//	if (!OS.IsWinCE) {
//		rop2 = OS.GetROP2(handle);
//	} else {
//		rop2 = OS.SetROP2 (handle, OS.R2_COPYPEN);
//		OS.SetROP2 (handle, rop2);
//	}
//	int dwRop = rop2 == OS.R2_XORPEN ? OS.SRCINVERT : OS.SRCCOPY;
//	if (!simple && (srcWidth != destWidth || srcHeight != destHeight)) {
//		int mode = 0;
//		if (!OS.IsWinCE) mode = OS.SetStretchBltMode(handle, OS.COLORONCOLOR);
//		OS.StretchBlt(handle, destX, destY, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, dwRop);
//		if (!OS.IsWinCE) OS.SetStretchBltMode(handle, mode);
//	} else {
//		OS.BitBlt(handle, destX, destY, destWidth, destHeight, srcHdc, srcX, srcY, dwRop);
//	}
//	OS.SelectObject(srcHdc, oldSrcBitmap);
//	OS.DeleteDC(srcHdc);
//}

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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  handle.drawLine(x1, y1, x2, y2);
//  Point p1 = new Point(Math.min(x1, x2), Math.min(y1, y2));
//  Point p2 = new Point(Math.max(x1, x2), Math.max(y1, y2));
//  ensureAreaClean(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  handle.drawOval(x, y, width, height);
//  ensureAreaClean(x, y, width, height);
}

/** 
 * Draws the path described by the parameter.
 *
 * @param path the path to draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Path
 * 
 * @since 3.1
 */
public void drawPath (Path path) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path.handle == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	handle.draw(path.handle);
//  java.awt.Rectangle bounds = path.handle.getBounds();
//  ensureAreaClean(bounds.x, bounds.y, bounds.width, bounds.height);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  handle.drawLine(x, y, x, y);
//  ensureAreaClean(x, y, 1, 1);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
//  int maxX = Integer.MIN_VALUE;
//  int maxY = Integer.MIN_VALUE;
//  int minX = Integer.MAX_VALUE;
//  int minY = Integer.MAX_VALUE;
  int[] xPoints = new int[pointArray.length/2];
  int[] yPoints = new int[xPoints.length];
  for(int i=0; i<xPoints.length; i++) {
    xPoints[i] = pointArray[i * 2];
    yPoints[i] = pointArray[i * 2 + 1];
//    maxX = Math.max(maxX, xPoints[i]);
//    maxY = Math.max(maxY, yPoints[i]);
//    minX = Math.min(minX, xPoints[i]);
//    minY = Math.min(minY, yPoints[i]);
  }
  handle.drawPolygon(xPoints, yPoints, xPoints.length);
//  ensureAreaClean(minX, minY, maxX - minX, maxY - minY);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
//  int maxX = Integer.MIN_VALUE;
//  int maxY = Integer.MIN_VALUE;
//  int minX = Integer.MAX_VALUE;
//  int minY = Integer.MAX_VALUE;
  int[] xPoints = new int[pointArray.length/2];
  int[] yPoints = new int[xPoints.length];
  for(int i=0; i<xPoints.length; i++) {
    xPoints[i] = pointArray[i * 2];
    yPoints[i] = pointArray[i * 2 + 1];
//    maxX = Math.max(maxX, xPoints[i]);
//    maxY = Math.max(maxY, yPoints[i]);
//    minX = Math.min(minX, xPoints[i]);
//    minY = Math.min(minY, yPoints[i]);
  }
  handle.drawPolyline(xPoints, yPoints, xPoints.length);
//  ensureAreaClean(minX, minY, maxX - minX, maxY - minY);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  handle.drawRect(x, y, width, height);
//  ensureAreaClean(x, y, width, height);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  handle.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
//  ensureAreaClean(x, y, width, height);
}

//void drawRoundRectangleGdip (int gdipGraphics, int brush, int x, int y, int width, int height, int arcWidth, int arcHeight) {
//	int nx = x;
//	int ny = y;
//	int nw = width;
//	int nh = height;
//	int naw = arcWidth;
//	int nah = arcHeight;
//	
//	if (nw < 0) {
//		nw = 0 - nw;
//		nx = nx - nw;
//	}
//	if (nh < 0) {
//		nh = 0 - nh;
//		ny = ny - nh;
//	}
//	if (naw < 0) 
//		naw = 0 - naw;
//	if (nah < 0)
//		nah = 0 - nah;
//				
//	int naw2 = naw / 2;
//	int nah2 = nah / 2;
//	
//	if (nw > naw) {
//		if (nh > nah) {
//			Gdip.Graphics_DrawArc(gdipGraphics, brush, nx, ny, naw, nah, -90, -90);
//			Gdip.Graphics_DrawLine(gdipGraphics, brush, nx + naw2, ny, nx + nw - naw2, ny);
//			Gdip.Graphics_DrawArc(gdipGraphics, brush, nx + nw - naw, ny, naw, nah, 0, -90);
//			Gdip.Graphics_DrawLine(gdipGraphics, brush, nx + nw, ny + nah2, nx + nw, ny + nh - nah2);
//			Gdip.Graphics_DrawArc(gdipGraphics, brush, nx + nw - naw, ny + nh - nah, naw, nah, -270, -90);
//			Gdip.Graphics_DrawLine(gdipGraphics, brush, nx + naw2, ny + nh, nx + nw - naw2, ny + nh);
//			Gdip.Graphics_DrawArc(gdipGraphics, brush, nx, ny + nh - nah, naw, nah, -180, -90);
//			Gdip.Graphics_DrawLine(gdipGraphics, brush, nx, ny + nah2, nx, ny + nh - nah2);
//		} else {
//			Gdip.Graphics_DrawArc(gdipGraphics, brush, nx, ny, naw, nh, 90, -180);
//			Gdip.Graphics_DrawLine(gdipGraphics, brush, nx + naw2, ny, nx + nw - naw2, ny);
//			Gdip.Graphics_DrawArc(gdipGraphics, brush, nx + nw - naw, ny, naw, nh, 270, -180);
//			Gdip.Graphics_DrawLine(gdipGraphics, brush, nx + naw2, ny + nh, nx + nw - naw2, ny + nh);
//		}
//	} else {
//		if (nh > nah) {
//			Gdip.Graphics_DrawArc(gdipGraphics, brush, nx, ny, nw, nah, 0, -180);
//			Gdip.Graphics_DrawLine(gdipGraphics, brush, nx + nw, ny + nah2, nx + nw, ny + nh - nah2);
//			Gdip.Graphics_DrawArc(gdipGraphics, brush, nx, ny + nh - nah, nw, nah, -180, -180);
//			Gdip.Graphics_DrawLine(gdipGraphics, brush, nx, ny + nah2, nx, ny + nh - nah2);
//		} else {
//			Gdip.Graphics_DrawArc(gdipGraphics, brush, nx, ny, nw, nh, 0, 360);
//		}
//	}
//}

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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
  drawText(string, x, y, isTransparent? SWT.DRAW_TRANSPARENT: 0);
//  if(!isTransparent) {
//    java.awt.Color oldColor = handle.getColor();
//    handle.setColor(data.background);
//    Point extent = stringExtent(string);
//    fillRectangle(x, y, extent.x, extent.y);
//    handle.setColor(oldColor);
//  }
//  handle.drawString(string, x, y + handle.getFontMetrics().getMaxAscent());
//  // TODO: optimize if the ensureAreaClean works
//  Point extent = stringExtent(string);
//  ensureAreaClean(x, y, extent.x, extent.y);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (string.length() == 0) return;
	if((flags & SWT.DRAW_TAB) != 0) {
	  string = string.replaceAll("\t", "    ");
	}
	int mnemonicIndex = -1;
	if((flags & SWT.DRAW_MNEMONIC) != 0) {
	  // Copied from Label
	  mnemonicIndex = findMnemonicIndex(string);
	  if(mnemonicIndex > 0) {
	    String s = string.substring(0, mnemonicIndex - 1).replaceAll("&&", "&");
	    string = s + string.substring(mnemonicIndex).replaceAll("&&", "&");
	    mnemonicIndex -= mnemonicIndex - 1 - s.length();
	    mnemonicIndex--;
	  } else {
	    string = string.replaceAll("&&", "&");
	  }
	  // TODO: if the mnemonic index is not -1, then draw a line under the char.
	}
	String[] tokens;
	if((flags & SWT.DRAW_DELIMITER) != 0) {
	  tokens = string.split("\n");
	} else {
	  tokens = new String[] {string};
	}
  boolean isTransparent = (flags & SWT.DRAW_TRANSPARENT) != 0;
  java.awt.FontMetrics fm = handle.getFontMetrics();
  int fmHeight = fm.getHeight();
  int maxAscent = fm.getMaxAscent();
  int currentHeight = 0;
  for(int i=0; i<tokens.length; i++) {
    y += currentHeight;
    currentHeight += fmHeight;
    if(!isTransparent) {
      java.awt.Color oldColor = handle.getColor();
      handle.setColor(data.background);
      fillRectangle(x, y, stringExtent(tokens[i]).x, currentHeight);
      handle.setColor(oldColor);
    }
    handle.drawString(tokens[i], x, y + maxAscent);
  }
  // TODO: optimize if the ensureAreaClean works
//  int width = 0;
//  int height = tokens.length * fmHeight;
//  for(int i=0; i<tokens.length; i++) {
//    width = Math.max(width, fm.stringWidth(tokens[i]));
//  }
//  ensureAreaClean(x, y, width, height);
//	TCHAR buffer = new TCHAR(getCodePage(), string, false);
//	int length = buffer.length();
//	if (length == 0) return;
//	RECT rect = new RECT();
//	/*
//	* Feature in Windows.  For some reason DrawText(), the maximum
//    * value for the bottom and right coordinates for the RECT that
//    * is used to position the text is different on between Windows
//    * versions.  If this value is larger than the maximum, nothing
//	* is drawn.  On Windows 98, the limit is 0x7FFF.  On Windows CE,
//	* NT, and 2000 it is 0x6FFFFFF. And on XP, it is 0x7FFFFFFF.
//	* The fix is to use the the smaller limit for Windows 98 and the
//	* larger limit on the other Windows platforms. 
//	*/
//	int limit = OS.IsWin95 ? 0x7FFF : 0x6FFFFFF;
//	OS.SetRect(rect, x, y, limit, limit);
//	int uFormat = OS.DT_LEFT;
//	if ((flags & SWT.DRAW_DELIMITER) == 0) uFormat |= OS.DT_SINGLELINE;
//	if ((flags & SWT.DRAW_TAB) != 0) uFormat |= OS.DT_EXPANDTABS;
//	if ((flags & SWT.DRAW_MNEMONIC) == 0) uFormat |= OS.DT_NOPREFIX;
//	int rop2 = 0;
//	if (OS.IsWinCE) {
//		rop2 = OS.SetROP2(handle, OS.R2_COPYPEN);
//		OS.SetROP2(handle, rop2);
//	} else {
//		rop2 = OS.GetROP2(handle);
//	}
//	int oldBkMode = OS.SetBkMode(handle, (flags & SWT.DRAW_TRANSPARENT) != 0 ? OS.TRANSPARENT : OS.OPAQUE);
//	if (rop2 != OS.R2_XORPEN) {
//		OS.DrawText(handle, buffer, length, rect, uFormat);
//	} else {
//		int foreground = OS.GetTextColor(handle);
//		if ((flags & SWT.DRAW_TRANSPARENT) != 0) {
//			OS.DrawText(handle, buffer, buffer.length(), rect, uFormat | OS.DT_CALCRECT);
//			int width = rect.right - rect.left;
//			int height = rect.bottom - rect.top;
//			int hBitmap = OS.CreateCompatibleBitmap(handle, width, height);
//			if (hBitmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//			int memDC = OS.CreateCompatibleDC(handle);
//			int hOldBitmap = OS.SelectObject(memDC, hBitmap);
//			OS.PatBlt(memDC, 0, 0, width, height, OS.BLACKNESS);
//			OS.SetBkMode(memDC, OS.TRANSPARENT);
//			OS.SetTextColor(memDC, foreground);
//			OS.SelectObject(memDC, OS.GetCurrentObject(handle, OS.OBJ_FONT));
//			OS.SetRect(rect, 0, 0, 0x7FFF, 0x7FFF);
//			OS.DrawText(memDC, buffer, length, rect, uFormat);
//			OS.BitBlt(handle, x, y, width, height, memDC, 0, 0, OS.SRCINVERT);
//			OS.SelectObject(memDC, hOldBitmap);
//			OS.DeleteDC(memDC);
//			OS.DeleteObject(hBitmap);
//		} else {
//			int background = OS.GetBkColor(handle);
//			OS.SetTextColor(handle, foreground ^ background);
//			OS.DrawText(handle, buffer, length, rect, uFormat);
//			OS.SetTextColor(handle, foreground);
//		}
//	}
//	OS.SetBkMode(handle, oldBkMode);
}

// Copied from Widget
int findMnemonicIndex (String string) {
  int index = 0;
  int length = string.length ();
  do {
    while (index < length && string.charAt (index) != '&') index++;
    if (++index >= length) return -1;
    if (string.charAt (index) != '&') return index;
    index++;
  } while (index < length);
  return -1;
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
  java.awt.Color oldColor = handle.getColor();
  handle.setColor(data.background);
  handle.fillArc(x, y, width, height, startAngle, arcAngle);
  handle.setColor(oldColor);
//  ensureAreaClean(x, y, width, height);
//	if (data.gdipGraphics != 0) {
//		initGdip(false, true);
//		Gdip.Graphics_FillPie(data.gdipGraphics, data.gdipBrush, x, y, width, height, -startAngle, -arcAngle);
//		return;
//	}
//	
//	/*
//	* Feature in WinCE.  The function Pie is not present in the
//	* WinCE SDK.  The fix is to emulate it by using Polygon.
//	*/
//	if (OS.IsWinCE) {
//		/* compute arc with a simple linear interpolation */
//		if (arcAngle < 0) {
//			startAngle += arcAngle;
//			arcAngle = -arcAngle;
//		}
//		boolean drawSegments = true;
//		if (arcAngle >= 360) {
//			arcAngle = 360;
//			drawSegments = false;
//		}
//		int[] points = new int[(arcAngle + 1) * 2 + (drawSegments ? 4 : 0)];		
//		int cteX = 2 * x + width;
//		int cteY = 2 * y + height;
//		int index = (drawSegments ? 2 : 0);
//		for (int i = 0; i <= arcAngle; i++) {
//			points[index++] = (Compatibility.cos(startAngle + i, width) + cteX) >> 1;
//			points[index++] = (cteY - Compatibility.sin(startAngle + i, height)) >> 1;
//		} 
//		if (drawSegments) {
//			points[0] = points[points.length - 2] = cteX >> 1;
//			points[1] = points[points.length - 1] = cteY >> 1;
//		}
//		int nullPen = OS.GetStockObject(OS.NULL_PEN);
//		int oldPen = OS.SelectObject(handle, nullPen);
//		OS.Polygon(handle, points, points.length / 2);
//		OS.SelectObject(handle, oldPen);	
//	} else {
//	 	int x1, y1, x2, y2,tmp;
//		boolean isNegative;
//		if (arcAngle >= 360 || arcAngle <= -360) {
//			x1 = x2 = x + width;
//			y1 = y2 = y + height / 2;
//		} else {
//			isNegative = arcAngle < 0;
//	
//			arcAngle = arcAngle + startAngle;
//			if (isNegative) {
//				// swap angles
//			   	tmp = startAngle;
//				startAngle = arcAngle;
//				arcAngle = tmp;
//			}
//			x1 = Compatibility.cos(startAngle, width) + x + width/2;
//			y1 = -1 * Compatibility.sin(startAngle, height) + y + height/2;
//			
//			x2 = Compatibility.cos(arcAngle, width) + x + width/2;
//			y2 = -1 * Compatibility.sin(arcAngle, height) + y + height/2; 				
//		}
//	
//		int nullPen = OS.GetStockObject(OS.NULL_PEN);
//		int oldPen = OS.SelectObject(handle, nullPen);
//		OS.Pie(handle, x, y, x + width + 1, y + height + 1, x1, y1, x2, y2);
//		OS.SelectObject(handle, oldPen);
//	}
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width == 0 || height == 0) return;
  java.awt.Color fromColor = handle.getColor();
  java.awt.Color toColor = handle.getBackground();
  Paint oldPaint = handle.getPaint();
  java.awt.Point p1;
  java.awt.Point p2;
  if(vertical) {
    p1 = new java.awt.Point(x, y);
    p2 = new java.awt.Point(x, y + height);
  } else {
    p1 = new java.awt.Point(x, y);
    p2 = new java.awt.Point(x + width, y);
  }
//  java.awt.Color oldColor = handle.getColor();
//  handle.setColor(data.background);
  handle.setPaint(new GradientPaint(p1, fromColor, p2, toColor));
  handle.fill(new java.awt.Rectangle(x, y, width, height));
//  handle.setColor(oldColor);
  handle.setPaint(oldPaint);
//  ensureAreaClean(x, y, width, height);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  java.awt.Color oldColor = handle.getColor();
  handle.setColor(data.background);
  handle.fillOval(x, y, width, height);
  handle.setColor(oldColor);
//  ensureAreaClean(x, y, width, height);
//	if (data.gdipGraphics != 0) {
//		initGdip(false, true);
//		Gdip.Graphics_FillEllipse(data.gdipGraphics, data.gdipBrush, x, y, width, height);
//		return;
//	}
//	/* Assumes that user sets the background color. */
//	int nullPen = OS.GetStockObject(OS.NULL_PEN);
//	int oldPen = OS.SelectObject(handle, nullPen);
//	OS.Ellipse(handle, x, y, x + width + 1, y + height + 1);
//	OS.SelectObject(handle,oldPen);
}

/** 
 * Fills the path described by the parameter.
 *
 * @param path the path to fill
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Path
 * 
 * @since 3.1
 */
public void fillPath (Path path) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path.handle == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	// Set fill(winding) rule
	int windingRule = fillRule == SWT.FILL_EVEN_ODD ? GeneralPath.WIND_EVEN_ODD: GeneralPath.WIND_NON_ZERO;
	path.handle.setWindingRule(windingRule);
	// Use background color for paint.
	Paint old = handle.getPaint();
	handle.setPaint(handle.getBackground());
	handle.fill(path.handle);
	handle.setPaint(old);
//  java.awt.Rectangle bounds = path.handle.getBounds();
//  ensureAreaClean(bounds.x, bounds.y, bounds.width, bounds.height);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
  int maxX = Integer.MIN_VALUE;
  int maxY = Integer.MIN_VALUE;
  int minX = Integer.MAX_VALUE;
  int minY = Integer.MAX_VALUE;
  int[] xPoints = new int[pointArray.length/2];
  int[] yPoints = new int[xPoints.length];
  for(int i=0; i<xPoints.length; i++) {
    xPoints[i] = pointArray[i * 2];
    yPoints[i] = pointArray[i * 2 + 1];
    maxX = Math.max(maxX, xPoints[i]);
    maxY = Math.max(maxY, yPoints[i]);
    minX = Math.min(minX, xPoints[i]);
    minY = Math.min(minY, yPoints[i]);
  }
  java.awt.Color oldColor = handle.getColor();
  handle.setColor(data.background);
  handle.fillPolygon(xPoints, yPoints, xPoints.length);
  handle.setColor(oldColor);
//  ensureAreaClean(minX, minY, maxX - minX, maxY - minY);
//	if (data.gdipGraphics != 0) {
//		initGdip(false, true);
//		int mode = OS.GetPolyFillMode(handle) == OS.WINDING ? Gdip.FillModeWinding : Gdip.FillModeAlternate;
//		Gdip.Graphics_FillPolygon(data.gdipGraphics, data.gdipBrush, pointArray, pointArray.length / 2, mode);
//		return;
//	}
//	int nullPen = OS.GetStockObject(OS.NULL_PEN);
//	int oldPen = OS.SelectObject(handle, nullPen);
//	OS.Polygon(handle, pointArray, pointArray.length / 2);
//	OS.SelectObject(handle,oldPen);	
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  java.awt.Color oldColor = handle.getColor();
  handle.setColor(data.background);
  handle.fillRect(x, y, width, height);
  handle.setColor(oldColor);
//  ensureAreaClean(x, y, width, height);
//	if (data.gdipGraphics != 0) {
//		initGdip(false, true);
//		Gdip.Graphics_FillRectangle(data.gdipGraphics, data.gdipBrush, x, y, width, height);
//		return;
//	}
//	int rop2 = 0;
//	if (OS.IsWinCE) {
//		rop2 = OS.SetROP2(handle, OS.R2_COPYPEN);
//		OS.SetROP2(handle, rop2);
//	} else {
//		rop2 = OS.GetROP2(handle);
//	}
//	int dwRop = rop2 == OS.R2_XORPEN ? OS.PATINVERT : OS.PATCOPY;
//	OS.PatBlt(handle, x, y, width, height, dwRop);
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
  java.awt.Color oldColor = handle.getColor();
  handle.setColor(data.background);
  fillRectangle (rect.x, rect.y, rect.width, rect.height);
  handle.setColor(oldColor);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  java.awt.Color oldColor = handle.getColor();
  handle.setColor(data.background);
  handle.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
  handle.setColor(oldColor);
//  ensureAreaClean(x, y, width, height);
//	if (data.gdipGraphics != 0) {
//		initGdip(false, true);
//		fillRoundRectangleGdip(data.gdipGraphics, data.gdipBrush, x, y, width, height, arcWidth, arcHeight);
//		return;
//	}
//	int nullPen = OS.GetStockObject(OS.NULL_PEN);
//	int oldPen = OS.SelectObject(handle, nullPen);
//	OS.RoundRect(handle, x,y,x+width+1,y+height+1,arcWidth, arcHeight);
//	OS.SelectObject(handle,oldPen);
}

//void fillRoundRectangleGdip (int gdipGraphics, int brush, int x, int y, int width, int height, int arcWidth, int arcHeight) {
//	int nx = x;
//	int ny = y;
//	int nw = width;
//	int nh = height;
//	int naw = arcWidth;
//	int nah = arcHeight;
//	
//	if (nw < 0) {
//		nw = 0 - nw;
//		nx = nx - nw;
//	}
//	if (nh < 0) {
//		nh = 0 - nh;
//		ny = ny -nh;
//	}
//	if (naw < 0) 
//		naw = 0 - naw;
//	if (nah < 0)
//		nah = 0 - nah;
//
//	int naw2 = naw / 2;
//	int nah2 = nah / 2;
//
//	if (nw > naw) {
//		if (nh > nah) {
//			Gdip.Graphics_FillPie(gdipGraphics, brush, nx, ny, naw, nah, -90, -90);
//			Gdip.Graphics_FillRectangle(gdipGraphics, brush, nx + naw2, ny, nw - naw2 * 2, nah2);
//			Gdip.Graphics_FillPie(gdipGraphics, brush, nx + nw - naw, ny, naw, nah, 0, -90);
//			Gdip.Graphics_FillRectangle(gdipGraphics, brush, nx, ny + nah2, nw, nh - nah2 * 2);
//			Gdip.Graphics_FillPie(gdipGraphics, brush, nx + nw - naw, ny + nh - nah, naw, nah, -270, -90);
//			Gdip.Graphics_FillRectangle(gdipGraphics, brush, nx + naw2, ny + nh - nah2, nw - naw2 * 2, nah2);
//			Gdip.Graphics_FillPie(gdipGraphics, brush, nx, ny + nh - nah, naw, nah, -180, -90);
//		} else {
//			Gdip.Graphics_FillPie(gdipGraphics, brush, nx, ny, naw, nh, -90, -180);
//			Gdip.Graphics_FillRectangle(gdipGraphics, brush, nx + naw2, ny, nw - naw2 * 2, nh);
//			Gdip.Graphics_FillPie(gdipGraphics, brush, nx + nw - naw, ny, naw, nh, -270, -180);
//		}
//	} else {
//		if (nh > nah) {
//			Gdip.Graphics_FillPie(gdipGraphics, brush, nx, ny, nw, nah, 0, -180);
//			Gdip.Graphics_FillRectangle(gdipGraphics, brush, nx, ny + nah2, nw, nh - nah2 * 2);
//			Gdip.Graphics_FillPie(gdipGraphics, brush, nx, ny + nh - nah, nw, nah, -180, -180);
//		} else {
//			Gdip.Graphics_FillPie(gdipGraphics, brush, nx, ny, nw, nh, 0, 360);
//		}
//	}
//}

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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  return handle.getFontMetrics().charWidth(ch);
//	if (OS.IsWinCE) {
//		SIZE size = new SIZE();
//		OS.GetTextExtentPoint32W(handle, new char[]{ch}, 1, size);
//		return size.cx;
//	}
//	int tch = ch;
//	if (ch > 0x7F) {
//		TCHAR buffer = new TCHAR(getCodePage(), ch, false);
//		tch = buffer.tcharAt(0);
//	}
//	int[] width = new int[1];
//	OS.GetCharWidth(handle, tch, tch, width);
//	return width[0];
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
 * @since 3.1
 */
public boolean getAdvanced() {
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  return data.advanced;
}

/**
 * Returns the receiver's alpha value.
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.alpha;
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
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  Object value = handle.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
  if(value == RenderingHints.VALUE_ANTIALIAS_OFF) return SWT.OFF;
  if(value == RenderingHints.VALUE_ANTIALIAS_ON) return SWT.ON;
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  return Color.swing_new(data.device, handle.getBackground());
//	int color = OS.GetBkColor(handle);
//	if (color == OS.CLR_INVALID) {
//		color = OS.GetSysColor(OS.COLOR_WINDOW);
//	}
//	return Color.win32_new(data.device, color);
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
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  return data.backgroundPattern;
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  // TODO: find the difference between advance width and char width
  return handle.getFontMetrics().charWidth(ch);
//	
//	/* GetCharABCWidths only succeeds on truetype fonts */
//	if (!OS.IsWinCE) {
//		int tch = ch;
//		if (ch > 0x7F) {
//			TCHAR buffer = new TCHAR(getCodePage(), ch, false);
//			tch = buffer.tcharAt (0);
//		}
//		int[] width = new int[3];
//		if (OS.GetCharABCWidths(handle, tch, tch, width)) {
//			return width[1];
//		}
//	}
//	
//	/* It wasn't a truetype font */
//	TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW() : new TEXTMETRICA();
//	OS.GetTextMetrics(handle, lptm);
//	SIZE size = new SIZE();
//	OS.GetTextExtentPoint32W(handle, new char[]{ch}, 1, size);
//	return size.cx - lptm.tmOverhang;
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
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  Shape userClip = handle.getUserClip();
  java.awt.Rectangle bounds;
  if(userClip != null) {
    bounds = userClip.getBounds();
  } else {
    bounds = new java.awt.Rectangle(handle.getDeviceSize());
  }
  return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
  Shape userClip = handle.getUserClip();
  if(userClip == null) {
    userClip = new java.awt.Rectangle(handle.getDeviceSize());
  }
  region.add(new Region(data.device, userClip));
//	int result = OS.GetClipRgn (handle, region.handle);
//	if (result != 1) {
//		RECT rect = new RECT();
//		OS.GetClipBox(handle, rect);
//		OS.SetRectRgn(region.handle, rect.left, rect.top, rect.right, rect.bottom);
//	}
//	if (!OS.IsWinCE) {
//		int flags = 0;
//		if (OS.WIN32_VERSION >= OS.VERSION(4, 10)) {
//			flags =  OS.GetLayout(handle);
//		}
//		int hwnd = data.hwnd;
//		if (hwnd != 0 && data.ps != null && (flags & OS.LAYOUT_RTL) == 0) {
//			int sysRgn = OS.CreateRectRgn (0, 0, 0, 0);
//			if (OS.GetRandomRgn (handle, sysRgn, OS.SYSRGN) == 1) {
//				if (OS.IsWinNT) {
//					POINT pt = new POINT();
//					OS.MapWindowPoints(0, hwnd, pt, 1);
//					OS.OffsetRgn(sysRgn, pt.x, pt.y);
//				}
//				OS.CombineRgn (region.handle, sysRgn, region.handle, OS.RGN_AND);
//			}
//			OS.DeleteObject(sysRgn);
//		}
//	}
}

//int getCodePage () {
//	if (OS.IsUnicode) return OS.CP_ACP;
//	int[] lpCs = new int[8];
//	int cs = OS.GetTextCharset(handle);
//	OS.TranslateCharsetInfo(cs, lpCs, OS.TCI_SRCCHARSET);
//	return lpCs[1];
//}

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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return fillRule;
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  return Font.swing_new(data.device, handle.getFont());
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  return FontMetrics.swing_new(handle.getFontMetrics());
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  return Color.swing_new(data.device, handle.getColor());
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
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  return data.foregroundPattern;
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
 */
public GCData getGCData() {
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
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
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  Utils.notImplemented(); return SWT.DEFAULT;
//  if (data.gdipGraphics == 0) return SWT.DEFAULT;
//  int mode = Gdip.Graphics_GetInterpolationMode(data.gdipGraphics);
//  switch (mode) {
//    case Gdip.InterpolationModeDefault: return SWT.DEFAULT;
//    case Gdip.InterpolationModeNearestNeighbor: return SWT.NONE;
//    case Gdip.InterpolationModeBilinear:
//    case Gdip.InterpolationModeLowQuality: return SWT.LOW;
//    case Gdip.InterpolationModeBicubic:
//    case Gdip.InterpolationModeHighQualityBilinear:
//    case Gdip.InterpolationModeHighQualityBicubic:
//    case Gdip.InterpolationModeHighQuality: return SWT.HIGH;
//  }
//  return SWT.DEFAULT;
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  switch(getCurrentBasicStroke().getEndCap()) {
    case BasicStroke.CAP_ROUND:
      return SWT.CAP_ROUND;
    case BasicStroke.CAP_SQUARE:
      return SWT.CAP_SQUARE;
  }
  return SWT.CAP_FLAT;
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  float[] dashArray = getCurrentBasicStroke().getDashArray();
  if(dashArray == null) return null;
  int[] dashes = new int[dashArray.length];
  for(int i=0; i<dashes.length; i++) {
    dashes[i] = Math.round(dashArray[i]);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  switch(getCurrentBasicStroke().getLineJoin()) {
    case BasicStroke.JOIN_BEVEL:
      return SWT.JOIN_BEVEL;
    case BasicStroke.JOIN_ROUND:
      return SWT.JOIN_ROUND;
  }
  return SWT.JOIN_MITER;
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  float[] dashArray = getCurrentBasicStroke().getDashArray();
  if(dashArray == null) {
    return SWT.LINE_SOLID;
  } else if(dashArray == lineDashArray) {
    return SWT.LINE_DASH;
  } else if(dashArray == lineDotArray) {
    return SWT.LINE_DOT;
  } else if(dashArray == lineDashDotArray) {
    return SWT.LINE_DASHDOT;
  } else if(dashArray == lineDashDotDotArray) {
    return SWT.LINE_DASHDOTDOT;
  } else if(dashArray != null && dashArray == data.dashes) {
    return SWT.LINE_CUSTOM;
  }
  return SWT.LINE_SOLID;
//
//	int style, size;
//	int hPen = OS.GetCurrentObject(handle, OS.OBJ_PEN);
//	if ((size = OS.GetObject(hPen, 0, (LOGPEN)null)) == LOGPEN.sizeof) {
//		LOGPEN logPen = new LOGPEN();
//		OS.GetObject(hPen, LOGPEN.sizeof, logPen);
//		style = logPen.lopnStyle;
//	} else {
//		EXTLOGPEN logPen = new EXTLOGPEN();
//		if (size <= EXTLOGPEN.sizeof) {
//			OS.GetObject(hPen, size, logPen);
//		} else {
//			int hHeap = OS.GetProcessHeap();
//			int ptr = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, size);
//			OS.GetObject(hPen, size, ptr);
//			OS.MoveMemory(logPen, ptr, EXTLOGPEN.sizeof);
//			OS.HeapFree(hHeap, 0, ptr);
//		}
//		style = logPen.elpPenStyle & OS.PS_STYLE_MASK;
//	}
//	switch (style) {
//		case OS.PS_SOLID:		return SWT.LINE_SOLID;
//		case OS.PS_DASH:		return SWT.LINE_DASH;
//		case OS.PS_DOT:			return SWT.LINE_DOT;
//		case OS.PS_DASHDOT:		return SWT.LINE_DASHDOT;
//		case OS.PS_DASHDOTDOT:	return SWT.LINE_DASHDOTDOT;
//		case OS.PS_USERSTYLE:	return SWT.LINE_CUSTOM;
//		default:				return SWT.LINE_SOLID;
//	}
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  return (int)getCurrentBasicStroke().getLineWidth();
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
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
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  Object value = handle.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
  if(value == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF) return SWT.OFF;
  if(value == RenderingHints.VALUE_TEXT_ANTIALIAS_ON) return SWT.ON;
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (transform == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (transform.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if(gcAT == null) {
	  transform.handle = new AffineTransform();
	} else {
	  transform.handle = new AffineTransform(gcAT);
	}
}

boolean isXORMode = false;

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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  // TODO: Not a solution because if swing is used directly in future implementation, this does not detect.
  return isXORMode;
//	int rop2 = 0;
//	if (OS.IsWinCE) {
//		rop2 = OS.SetROP2 (handle, OS.R2_COPYPEN);
//		OS.SetROP2 (handle, rop2);
//	} else {
//		rop2 = OS.GetROP2(handle);
//	}
//	return rop2 == OS.R2_XORPEN;
}

//void initGdip(boolean draw, boolean fill) {
//	data.device.checkGDIP();
//	int gdipGraphics = data.gdipGraphics;
//	if (gdipGraphics == 0) gdipGraphics = data.gdipGraphics = Gdip.Graphics_new(handle);
//	if (gdipGraphics == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//	if (draw && data.gdipPen == 0) data.gdipPen = createGdipPen();
//	if (fill && data.gdipBrush == 0) data.gdipBrush = createGdipBrush();
//}

void init(Drawable drawable, GCData data, CGC handle) {
  if(data.foreground != null) {
    handle.setColor(data.foreground);
  }
  if(data.background != null) {
    handle.setBackground(data.background);
  }
  if(data.hFont != null) {
    handle.setFont(data.hFont);
  }
//	int foreground = data.foreground;
//	if (foreground != -1 && OS.GetTextColor(hDC) != foreground) {
//		OS.SetTextColor(hDC, foreground);
//		int newPen = OS.CreatePen(OS.PS_SOLID, 0, foreground);
//		OS.SelectObject(hDC, newPen);
//		if (data.hPen != 0) OS.DeleteObject(data.hPen);
//		data.hPen = newPen;
//	}
//	int background = data.background;
//	if (background != -1 && OS.GetBkColor(hDC) != background) {
//		OS.SetBkColor(hDC, background);
//		int newBrush = OS.CreateSolidBrush(background);
//		OS.SelectObject(hDC, newBrush);
//		if (data.hBrush != 0) OS.DeleteObject (data.hBrush);
//		data.hBrush = newBrush;
//	}
//	int hFont = data.hFont;
//	if (hFont != 0) OS.SelectObject (hDC, hFont);
//	int hPalette = data.device.hPalette;
//	if (hPalette != 0) {
//		OS.SelectPalette(hDC, hPalette, true);
//		OS.RealizePalette(hDC);
//	}
//	Image image = data.image;
//	if (image != null) {
//		data.hNullBitmap = OS.SelectObject(hDC, image.handle);
//		image.memGC = this;
//	}
//	int layout = data.layout;
//	if (layout != -1) {
//		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION(4, 10)) {
//			int flags = OS.GetLayout(hDC);
//			if ((flags & OS.LAYOUT_RTL) != (layout & OS.LAYOUT_RTL)) {
//				flags &= ~OS.LAYOUT_RTL;
//				OS.SetLayout(hDC, flags | layout);
//			}
//			if ((data.style & SWT.RIGHT_TO_LEFT) != 0) data.style |= SWT.MIRRORED;
//		}
//	}	
	this.drawable = drawable;
	this.data = data;
	this.handle = handle;
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
  return handle == null? 0: handle.hashCode();
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  return handle.getUserClip() != null;
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
	return handle == null;
}

//boolean isIdentity(float[] xform) {
//	return xform[0] == 1 && xform[1] == 0 && xform[2] == 0 &&
//		xform[3] == 1 && xform[4] == 0 && xform[5] == 0;
//}

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
 * </p>
 * <p>
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
 * @see #setInterpolation
 * @see #setTextAntialias
 * @see #setTransform
 * @see #getAdvanced
 * 
 * @since 3.1
 */
public void setAdvanced(boolean advanced) {
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  data.advanced = advanced;
}

/**
 * Sets the receiver's anti-aliasing value to the parameter, 
 * which must be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code>
 * or <code>SWT.ON</code>. Note that this controls anti-aliasing for all
 * <em>non-text drawing</em> operations.
 *
 * @param antialias the anti-aliasing setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is not one of <code>SWT.DEFAULT</code>,
 *                                 <code>SWT.OFF</code> or <code>SWT.ON</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setTextAntialias
 * 
 * @since 3.1
 */
public void setAntialias(int antialias) {
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  switch (antialias) {
    case SWT.DEFAULT:
      handle.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
      break;
    case SWT.OFF:
      handle.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
      break;
    case SWT.ON:
      handle.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      break;      
    default:
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
  }
}

/**
 * Sets the receiver's alpha value.
 *
 * @param alpha the alpha value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setAlpha(int alpha) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	data.alpha = alpha & 0xFF;
	AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, data.alpha / 255.0f);
	handle.setComposite(comp);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
  data.background = color.handle;
  handle.setBackground(color.handle);
//	if (OS.GetBkColor(handle) == color.handle) return;
//	data.background = color.handle;
//	OS.SetBkColor (handle, color.handle);
//	int newBrush = OS.CreateSolidBrush (color.handle);
//	OS.SelectObject (handle, newBrush);
//	if (data.hBrush != 0) OS.DeleteObject (data.hBrush);
//	data.hBrush = newBrush;
//	if (data.gdipBrush != 0) {
//		Gdip.SolidBrush_delete(data.gdipBrush);		
//		data.gdipBrush = 0;
//	}
}

/** 
 * Sets the background pattern. The default value is <code>null</code>.
 *
 * @param pattern the new background pattern
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Pattern
 * 
 * @since 3.1
 */
public void setBackgroundPattern (Pattern pattern) {
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  if (pattern != null && pattern.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
  Utils.notImplemented();
//  if (data.gdipGraphics == 0 && pattern == null) return;
//  initGdip(false, false);
//  if (data.gdipBrush != 0) destroyGdipBrush(data.gdipBrush);
//  if (pattern != null) {
//    data.gdipBrush = Gdip.Brush_Clone(pattern.handle);
//  } else {
//    data.gdipBrush = 0;
//  }
  data.backgroundPattern = pattern;
}

//void setClipping(int clipRgn) {
//	int hRgn = clipRgn;
//	int gdipGraphics = data.gdipGraphics;
//	if (hRgn != 0) {
//		boolean result;
//		float[] xform = new float[]{1, 0, 0, 1, 0, 0};
//		if (gdipGraphics != 0) {
//			int matrix = Gdip.Matrix_new(1, 0, 0, 1, 0, 0);
//			result = Gdip.Graphics_GetTransform(gdipGraphics, matrix) == 0;
//			Gdip.Matrix_GetElements(matrix, xform);
//			Gdip.Matrix_delete(matrix);
//		} else {
//			result = OS.GetWorldTransform(handle, xform);
//		}
//		if (result && !isIdentity(xform)) {
//			int count = OS.GetRegionData(hRgn, 0, null);
//			int[] lpRgnData = new int[count / 4];
//			OS.GetRegionData(hRgn, count, lpRgnData);
//			hRgn = OS.ExtCreateRegion(xform, count, lpRgnData);
//		}
//	}
//	OS.SelectClipRgn (handle, hRgn);
//	if (gdipGraphics != 0) {
//		if (hRgn != 0) {
//			Gdip.Graphics_SetClip(gdipGraphics, hRgn, Gdip.CombineModeReplace);
//		} else {
//			Gdip.Graphics_ResetClip(gdipGraphics);
//		}
//	}
//	if (hRgn != 0 && hRgn != clipRgn) {
//		OS.DeleteObject(hRgn);
//	}
//}

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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  setClipping(new Rectangle(x, y, width, height));
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the path specified
 * by the argument.
 *
 * @param path the clipping path.
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the path has been disposed</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Path
 * 
 * @since 3.1
 */
public void setClipping (Path path) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path != null && path.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	handle.setUserClip(path.handle);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	java.awt.Rectangle userClip = rect == null? null: new java.awt.Rectangle(rect.x, rect.y, rect.width, rect.height);
	handle.setUserClip(userClip);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region != null && region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
  handle.setUserClip(region.handle);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	switch (rule) {
		case SWT.FILL_WINDING: 
		case SWT.FILL_EVEN_ODD:
			fillRule = rule;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (font == null) {
    handle.setFont(LookAndFeelUtils.getSystemFont());
	} else {
		if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		handle.setFont(font.handle);
	}
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.foreground = color.handle;
  handle.setColor(color.handle);
}

/** 
 * Sets the foreground pattern. The default value is <code>null</code>.
 *
 * @param pattern the new foreground pattern
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Pattern
 * 
 * @since 3.1
 */
public void setForegroundPattern (Pattern pattern) {
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  if (pattern != null && pattern.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
  Utils.notImplemented();
//  if (data.gdipGraphics == 0 && pattern == null) return;
//  initGdip(false, false);
//  if (pattern != null) {
//    if (data.gdipPen != 0) Gdip.Pen_SetBrush(data.gdipPen, pattern.handle);
//  } else {
//    if (data.gdipPen != 0) {
//      Gdip.Pen_delete(data.gdipPen);
//      data.gdipPen = 0;
//    }
//  }
  data.foregroundPattern = pattern;
}

/** 
 * Sets the receiver's interpolation setting to the parameter, which
 * must be one of <code>SWT.DEFAULT</code>, <code>SWT.NONE</code>, 
 * <code>SWT.LOW</code> or <code>SWT.HIGH</code>.
 *
 * @param interpolation the new interpolation setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rule is not one of <code>SWT.DEFAULT</code>, 
 *                                 <code>SWT.NONE</code>, <code>SWT.LOW</code> or <code>SWT.HIGH</code>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setInterpolation(int interpolation) {
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  Utils.notImplemented();
//  if (data.gdipGraphics == 0 && interpolation == SWT.DEFAULT) return;
//  int mode = 0;
//  switch (interpolation) {
//    case SWT.DEFAULT: mode = Gdip.InterpolationModeDefault; break;
//    case SWT.NONE: mode = Gdip.InterpolationModeNearestNeighbor; break;
//    case SWT.LOW: mode = Gdip.InterpolationModeLowQuality; break;
//    case SWT.HIGH: mode = Gdip.InterpolationModeHighQuality; break;
//    default:
//      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//  }
//  initGdip(false, false);
//  Gdip.Graphics_SetInterpolationMode(data.gdipGraphics, mode);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  BasicStroke stroke = getCurrentBasicStroke();
	int capStyle = 0;
	switch (cap) {
		case SWT.CAP_ROUND:
			capStyle = BasicStroke.CAP_ROUND;
			break;
		case SWT.CAP_FLAT:
			capStyle = BasicStroke.CAP_BUTT;
			break;
		case SWT.CAP_SQUARE:
			capStyle = BasicStroke.CAP_SQUARE;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
  handle.setStroke(new BasicStroke(stroke.getLineWidth(), capStyle, stroke.getLineJoin(), stroke.getMiterLimit(), stroke.getDashArray(), 0));
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  BasicStroke stroke = getCurrentBasicStroke();
  if(dashes == null) dashes = new int[0];
  data.dashes = new float[dashes.length];
  for(int i=0; i<dashes.length; i++) {
    data.dashes[i] = dashes[i];
  }
  handle.setStroke(new BasicStroke(stroke.getLineWidth(), stroke.getEndCap(), stroke.getLineJoin(), stroke.getMiterLimit(), data.dashes, 0));
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  BasicStroke stroke = getCurrentBasicStroke();
  int joinStyle = 0;
  switch (join) {
    case SWT.JOIN_MITER:
      joinStyle = BasicStroke.JOIN_MITER;
      break;
    case SWT.JOIN_ROUND:
      joinStyle = BasicStroke.JOIN_ROUND;
      break;
    case SWT.JOIN_BEVEL:
      joinStyle = BasicStroke.JOIN_BEVEL;
      break;
    default:
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
  }
  handle.setStroke(new BasicStroke(stroke.getLineWidth(), stroke.getEndCap(), joinStyle, stroke.getMiterLimit(), stroke.getDashArray(), 0));
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  BasicStroke stroke = getCurrentBasicStroke();
  float[] array = null;
  switch(lineStyle) {
    case SWT.LINE_SOLID:
      break;
    case SWT.LINE_DASH:
      array = lineDashArray;
      break;
    case SWT.LINE_DOT:
      array = lineDotArray;
      break;
    case SWT.LINE_DASHDOT:
      array = lineDashDotArray;
      break;
    case SWT.LINE_DASHDOTDOT:
      array = lineDashDotDotArray;
      break;
    case SWT.LINE_CUSTOM:
      array = data.dashes;
      break;
    default:
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
  }
  handle.setStroke(new BasicStroke(stroke.getLineWidth(), stroke.getEndCap(), stroke.getLineJoin(), stroke.getMiterLimit(), array, 0));
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  BasicStroke stroke = getCurrentBasicStroke();
  handle.setStroke(new BasicStroke(lineWidth, stroke.getEndCap(), stroke.getLineJoin(), stroke.getMiterLimit(), stroke.getDashArray(), stroke.getDashPhase()));
}

//void setPen(int newColor, int newWidth, int lineStyle, int capStyle, int joinStyle, int[] dashes) {
//	boolean extPen = false, changed = false;
//	int style, color, width, size, hPen = OS.GetCurrentObject(handle, OS.OBJ_PEN);
//	if ((size = OS.GetObject(hPen, 0, (LOGPEN)null)) == LOGPEN.sizeof) {
//		LOGPEN logPen = new LOGPEN();
//		OS.GetObject(hPen, LOGPEN.sizeof, logPen);
//		color = logPen.lopnColor;
//		width = logPen.x;
//		style = logPen.lopnStyle;
//		/*
//		* Feature in Windows.  The default end caps is PS_ENDCAP_ROUND
//		* and the default line join is PS_JOIN_ROUND which are different
//		* from other platforms.  The fix is to change these values when
//		* line width is widened.
//		*/
//		if (width <= 1 && (newWidth > 1 || lineStyle == OS.PS_USERSTYLE)) {
//			if (capStyle == -1) capStyle = OS.PS_ENDCAP_FLAT;
//			if (joinStyle == -1) joinStyle = OS.PS_JOIN_MITER;
//		}
//	} else {
//		EXTLOGPEN logPen = new EXTLOGPEN();
//		if (size <= EXTLOGPEN.sizeof) {
//			OS.GetObject(hPen, size, logPen);
//		} else {
//			int hHeap = OS.GetProcessHeap();
//			int ptr = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, size);
//			OS.GetObject(hPen, size, ptr);
//			OS.MoveMemory(logPen, ptr, EXTLOGPEN.sizeof);
//			OS.HeapFree(hHeap, 0, ptr);
//		}
//		color = logPen.elpColor;
//		width = logPen.elpWidth;
//		style = logPen.elpPenStyle;
//		extPen = true;
//		if (newWidth == 0 || newWidth == 1) {
//			if (dashes == null && (style & OS.PS_ENDCAP_MASK) == OS.PS_ENDCAP_FLAT && (style & OS.PS_JOIN_MASK) == OS.PS_JOIN_MITER) {
//				style &= ~(OS.PS_ENDCAP_MASK | OS.PS_JOIN_MASK | OS.PS_TYPE_MASK);
//				extPen = false;
//			}
//		}
//	}
//	if (newColor != -1) {
//		if (newColor != color) {
//			color = newColor;
//			changed = true;
//		}
//	}
//	if (newWidth != -1) {
//		if (newWidth != width) {
//			width = newWidth;
//			changed = true;
//		}
//	}
//	if (lineStyle != -1) {
//		if ((style & OS.PS_STYLE_MASK) != lineStyle || (style & OS.PS_STYLE_MASK) == OS.PS_USERSTYLE) {
//			style = (style & ~OS.PS_STYLE_MASK) | lineStyle;
//			changed = true;
//		}
//	}
//	if (capStyle != -1) {
//		if ((style & OS.PS_ENDCAP_MASK) != capStyle) {
//			style = (style & ~OS.PS_ENDCAP_MASK) | capStyle;
//			changed = true;
//		}
//	}
//	if (joinStyle != -1) {
//		if ((style & OS.PS_JOIN_MASK) != joinStyle) {
//			style = (style & ~OS.PS_JOIN_MASK) | joinStyle;
//			changed = true;
//		}
//	}
//	if (!changed) return;
//	if ((style & OS.PS_STYLE_MASK) != OS.PS_USERSTYLE) dashes = null;
//	/*
//	* Feature in Windows.  Windows does not honour line styles other then
//	* PS_SOLID for pens wider than 1 pixel created with CreatePen().  The fix
//	* is to use ExtCreatePen() instead.
//	*/
//	int newPen;
//	if (!OS.IsWinCE && (extPen || width > 1 || (style & OS.PS_STYLE_MASK) == OS.PS_USERSTYLE)) {
//		LOGBRUSH logBrush = new LOGBRUSH();
//		logBrush.lbStyle = OS.BS_SOLID;
//		logBrush.lbColor = color;
//		/* Feature in Windows. PS_GEOMETRIC pens cannot have zero width. */
//		newPen = OS.ExtCreatePen (style | OS.PS_GEOMETRIC, Math.max(1, width), logBrush, dashes != null ? dashes.length : 0, dashes);
//	} else {
//		newPen = OS.CreatePen(style, width, color);
//	}
//	OS.SelectObject(handle, newPen);
//	if (data.hPen != 0) OS.DeleteObject(data.hPen);
//	data.hPen = newPen;
//	data.lineWidth = width;
//	if (data.gdipPen != 0) {
//		Gdip.Pen_delete(data.gdipPen);
//		data.gdipPen = 0;
//	}
//}

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
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  if(xor) {
    // need to check if "handle.setXORMode(getBackground().handle)" must be called any time the background color changes.
    handle.setXORMode(getBackground().handle);
  } else {
    handle.setPaintMode();
  }
  isXORMode = xor;
}

/**
 * Sets the receiver's text anti-aliasing value to the parameter, 
 * which must be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code>
 * or <code>SWT.ON</code>. Note that this controls anti-aliasing only
 * for all <em>text drawing</em> operations.
 *
 * @param antialias the anti-aliasing setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is not one of <code>SWT.DEFAULT</code>,
 *                                 <code>SWT.OFF</code> or <code>SWT.ON</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setAntialias
 * 
 * @since 3.1
 */
public void setTextAntialias(int antialias) {
  if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  switch (antialias) {
    case SWT.DEFAULT:
      handle.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
      break;
    case SWT.OFF:
      handle.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      break;
    case SWT.ON:
      handle.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      break;      
    default:
      SWT.error(SWT.ERROR_INVALID_ARGUMENT);
  }
}

/** 
 * Sets the transform that is currently being used by the receiver. If
 * the argument is <code>null</code>, the current transform is set to
 * the identity transform.
 *
 * @param transform the transform to set
 * 
 * @exception IllegalArgumentException <ul>
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
public void setTransform(Transform transform) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (saveAT == null) {
		saveAT = handle.getTransform();
	} else {
	  handle.setTransform(saveAT);
  }
	AffineTransform at;
	if (transform == null) {
	  at = new AffineTransform();
	  gcAT = null;
	} else {
	  at = transform.handle;
	  gcAT = at;
	}
	handle.transform(at);
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return textExtent(string, 0);
//  java.awt.FontMetrics fm = handle.getFontMetrics();
//  // TODO: check why the StyledText queries the stringExtent and this is null sometimes (cf RSSOwl)
//  if(fm == null) return new Point(0, 0);
//  return new Point(fm.stringWidth(string), fm.getMaxAscent() + fm.getMaxDescent());
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
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
  if((flags & SWT.DRAW_TAB) != 0) {
    string = string.replaceAll("\t", "    ");
  }
  int mnemonicIndex = -1;
  if((flags & SWT.DRAW_MNEMONIC) != 0) {
    // Copied from Label
    mnemonicIndex = findMnemonicIndex(string);
    if(mnemonicIndex > 0) {
      String s = string.substring(0, mnemonicIndex - 1).replaceAll("&&", "&");
      string = s + string.substring(mnemonicIndex).replaceAll("&&", "&");
      mnemonicIndex -= mnemonicIndex - 1 - s.length();
      mnemonicIndex--;
    } else {
      string = string.replaceAll("&&", "&");
    }
  }
  String[] tokens;
  if((flags & SWT.DRAW_DELIMITER) != 0) {
    tokens = string.split("\n");
  } else {
    tokens = new String[] {string};
  }
//  boolean isTransparent = (flags & SWT.DRAW_TRANSPARENT) != 0;
  java.awt.FontMetrics fm = handle.getFontMetrics();
  int fmHeight = fm.getHeight();
  int currentHeight = fmHeight;
  int maxWidth = 0;
  int maxHeight = 0;
  FontRenderContext fontRenderContext = handle.getFontRenderContext();
  java.awt.Font font = handle.getFont();
  for(int i=0; i<tokens.length; i++) {
//    if(!isTransparent) {
      maxWidth = Math.max(maxWidth, font.getStringBounds(tokens[i], fontRenderContext).getBounds().width);
      maxHeight = currentHeight;
//    }
    currentHeight += fmHeight;
  }
  return new Point(maxWidth, maxHeight);
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
 */
public static GC swing_new(Drawable drawable, GCData data) {
	GC gc = new GC();
	CGC handle = drawable.internal_new_GC(data);
	gc.init(drawable, data, handle);
	return gc;
}

/**	 
 * Invokes platform specific functionality to wrap a graphics context.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>GC</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param hDC the Windows HDC.
 * @param data the data for the receiver.
 *
 * @return a new <code>GC</code>
 */
public static GC swing_new(CGC handle, GCData data) {
	GC gc = new GC();
	gc.init(null, data, handle);
	return gc;
}

static final float[] lineDashArray = new float[] {18, 6};
static final float[] lineDotArray = new float[] {3, 3};
static final float[] lineDashDotArray = new float[] {9, 6, 3, 6};
static final float[] lineDashDotDotArray = new float[] {9, 3, 3, 3, 3, 3};

BasicStroke getCurrentBasicStroke() {
  Stroke stroke = handle.getStroke();
  if(stroke instanceof BasicStroke) {
    return (BasicStroke)stroke;
  }
  return new BasicStroke();
}

}

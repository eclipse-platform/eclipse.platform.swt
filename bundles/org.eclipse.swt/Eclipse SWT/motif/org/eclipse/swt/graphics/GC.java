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
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.motif.*;
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

	final static int FOREGROUND = 1 << 0;
	final static int BACKGROUND = 1 << 1;
	final static int FONT = 1 << 2;
	final static int LINE_STYLE = 1 << 3;
	final static int LINE_CAP = 1 << 4;
	final static int LINE_JOIN = 1 << 5;
	final static int LINE_WIDTH = 1 << 6;
	final static int LINE_MITERLIMIT = 1 << 7;
	final static int BACKGROUND_BG = 1 << 8;
	final static int FOREGROUND_RGB = 1 << 9;
	final static int BACKGROUND_RGB = 1 << 10;
	final static int DRAW_OFFSET = 1 << 11;
	final static int DRAW = FOREGROUND | LINE_WIDTH | LINE_STYLE  | LINE_CAP  | LINE_JOIN | LINE_MITERLIMIT | DRAW_OFFSET;
	final static int FILL = BACKGROUND;

	static final float[] LINE_DOT = new float[]{1, 1};
	static final float[] LINE_DASH = new float[]{3, 1};
	static final float[] LINE_DASHDOT = new float[]{3, 1, 1, 1};
	static final float[] LINE_DASHDOTDOT = new float[]{3, 1, 1, 1, 1, 1};
	static final float[] LINE_DOT_ZERO = new float[]{3, 3};
	static final float[] LINE_DASH_ZERO = new float[]{18, 6};
	static final float[] LINE_DASHDOT_ZERO = new float[]{9, 6, 3, 6};
	static final float[] LINE_DASHDOTDOT_ZERO = new float[]{9, 3, 3, 3, 3, 3};

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
	if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	GCData data = new GCData();
	data.style = checkStyle(style);
	int xGC = drawable.internal_new_GC(data);
	Device device = data.device;
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = data.device = device;
	init(drawable, data, xGC);
	init();
}
static void addCairoString(int cairo, String string, float x, float y, Font font) {
	byte[] buffer = Converter.wcsToMbcs(null, string, true);
	GC.setCairoFont(cairo, font);
	cairo_font_extents_t extents = new cairo_font_extents_t();
	Cairo.cairo_font_extents(cairo, extents);
	double baseline = y + extents.ascent;
	Cairo.cairo_move_to(cairo, x, baseline);
	Cairo.cairo_text_path(cairo, buffer);
}
static int checkStyle (int style) {
	if ((style & SWT.LEFT_TO_RIGHT) != 0) style &= ~SWT.RIGHT_TO_LEFT;
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

void checkGC (int mask) {
	int state = data.state;
	if ((state & mask) == mask) return;
	state = (state ^ mask) & mask;	
	data.state |= mask;
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		if ((state & (BACKGROUND | FOREGROUND)) != 0) {
			XColor color;
			Pattern pattern;
			if ((state & FOREGROUND) != 0) {
				color = data.foreground;
				if ((data.state & FOREGROUND_RGB) == 0) {
					OS.XQueryColor (data.display, data.colormap, color);
					data.state |= FOREGROUND_RGB;
				}
				pattern = data.foregroundPattern;
				data.state &= ~BACKGROUND;
			} else {
				color = data.background;
				if ((data.state & BACKGROUND_RGB) == 0) {
					OS.XQueryColor (data.display, data.colormap, color);
					data.state |= BACKGROUND_RGB;
				}
				pattern = data.backgroundPattern;
				data.state &= ~FOREGROUND;
			}
			if  (pattern != null) {
				Cairo.cairo_set_source(cairo, pattern.handle);
			} else {
				Cairo.cairo_set_source_rgba(cairo, (color.red & 0xFFFF) / (float)0xFFFF, (color.green & 0xFFFF) / (float)0xFFFF, (color.blue & 0xFFFF) / (float)0xFFFF, data.alpha / (float)0xFF);
			}
		}
		if ((state & FONT) != 0) {
			setCairoFont(cairo, data.font);
		}
		if ((state & LINE_CAP) != 0) {
			int cap_style = 0;
			switch (data.lineCap) {
				case SWT.CAP_ROUND: cap_style = Cairo.CAIRO_LINE_CAP_ROUND; break;
				case SWT.CAP_FLAT: cap_style = Cairo.CAIRO_LINE_CAP_BUTT; break;
				case SWT.CAP_SQUARE: cap_style = Cairo.CAIRO_LINE_CAP_SQUARE; break;
			}
			Cairo.cairo_set_line_cap(cairo, cap_style);
		}
		if ((state & LINE_JOIN) != 0) {
			int join_style = 0;
			switch (data.lineJoin) {
				case SWT.JOIN_MITER: join_style = Cairo.CAIRO_LINE_JOIN_MITER; break;
				case SWT.JOIN_ROUND:  join_style = Cairo.CAIRO_LINE_JOIN_ROUND; break;
				case SWT.JOIN_BEVEL: join_style = Cairo.CAIRO_LINE_JOIN_BEVEL; break;
			}
			Cairo.cairo_set_line_join(cairo, join_style);
		}
		if ((state & LINE_WIDTH) != 0) {
			Cairo.cairo_set_line_width(cairo, data.lineWidth == 0 ? 1 : data.lineWidth);
			switch (data.lineStyle) {
				case SWT.LINE_DOT:
				case SWT.LINE_DASH:
				case SWT.LINE_DASHDOT:
				case SWT.LINE_DASHDOTDOT:
					state |= LINE_STYLE;
			}
		}
		if ((state & LINE_STYLE) != 0) {
			float dashesOffset = 0;
			float[] dashes = null;
			float width = data.lineWidth;
			switch (data.lineStyle) {
				case SWT.LINE_SOLID: break;
				case SWT.LINE_DASH: dashes = width != 0 ? LINE_DASH : LINE_DASH_ZERO; break;
				case SWT.LINE_DOT: dashes = width != 0 ? LINE_DOT : LINE_DOT_ZERO; break;
				case SWT.LINE_DASHDOT: dashes = width != 0 ? LINE_DASHDOT : LINE_DASHDOT_ZERO; break;
				case SWT.LINE_DASHDOTDOT: dashes = width != 0 ? LINE_DASHDOTDOT : LINE_DASHDOTDOT_ZERO; break;
				case SWT.LINE_CUSTOM: dashes = data.lineDashes; break;
			}
			if (dashes != null) {
				dashesOffset = data.lineDashesOffset;
				double[] cairoDashes = new double[dashes.length];
				for (int i = 0; i < cairoDashes.length; i++) {
					cairoDashes[i] = width == 0 || data.lineStyle == SWT.LINE_CUSTOM ? dashes[i] : dashes[i] * width;
				}
				Cairo.cairo_set_dash(cairo, cairoDashes, cairoDashes.length, dashesOffset);
			} else {
				Cairo.cairo_set_dash(cairo, null, 0, 0);
			}
		}
		if ((state & LINE_MITERLIMIT) != 0) {
			Cairo.cairo_set_miter_limit(cairo, data.lineMiterLimit);
		}
		if ((state & DRAW_OFFSET) != 0) {
			data.cairoXoffset = data.cairoYoffset = 0;
			double[] matrix = new double[6];
			Cairo.cairo_get_matrix(cairo, matrix);
			double[] dx = new double[]{1};
			double[] dy = new double[]{1};
			Cairo.cairo_user_to_device_distance(cairo, dx, dy);
			double scaling = dx[0];
			if (scaling < 0) scaling = -scaling;
			double strokeWidth = data.lineWidth * scaling;
			if (strokeWidth == 0 || ((int)strokeWidth % 2) == 1) {
				data.cairoXoffset = 0.5 / scaling;
			}
			scaling = dy[0];
			if (scaling < 0) scaling = -scaling;
			strokeWidth = data.lineWidth * scaling;
			if (strokeWidth == 0 || ((int)strokeWidth % 2) == 1) {
				data.cairoYoffset = 0.5 / scaling;
			}
		}
		return;
	}
	int xDisplay = data.display;
	if ((state & (BACKGROUND | FOREGROUND)) != 0) {
		XColor foreground;
		if ((state & FOREGROUND) != 0) {
			foreground = data.foreground;
			data.state &= ~BACKGROUND;
		} else {
			foreground = data.background;
			data.state &= ~FOREGROUND;
		}
		OS.XSetForeground (xDisplay, handle, foreground.pixel);
	}
	if ((state & BACKGROUND_BG) != 0) {
		XColor background = data.background;
		OS.XSetBackground(xDisplay, handle, background.pixel);
	}
	if ((state & (LINE_CAP | LINE_JOIN | LINE_STYLE | LINE_WIDTH)) != 0) {
		int cap_style = 0;
		int join_style = 0;
		int width = (int)data.lineWidth;
		int line_style = 0;
		float[] dashes = null;
		switch (data.lineCap) {
			case SWT.CAP_ROUND: cap_style = OS.CapRound; break;
			case SWT.CAP_FLAT: cap_style = OS.CapButt; break;
			case SWT.CAP_SQUARE: cap_style = OS.CapProjecting; break;
		}
		switch (data.lineJoin) {
			case SWT.JOIN_ROUND: join_style = OS.JoinRound; break;
			case SWT.JOIN_MITER: join_style = OS.JoinMiter; break;
			case SWT.JOIN_BEVEL: join_style = OS.JoinBevel; break;
		}
		switch (data.lineStyle) {
			case SWT.LINE_SOLID: break;
			case SWT.LINE_DASH: dashes = width != 0 ? LINE_DASH : LINE_DASH_ZERO; break;
			case SWT.LINE_DOT: dashes = width != 0 ? LINE_DOT : LINE_DOT_ZERO; break;
			case SWT.LINE_DASHDOT: dashes = width != 0 ? LINE_DASHDOT : LINE_DASHDOT_ZERO; break;
			case SWT.LINE_DASHDOTDOT: dashes = width != 0 ? LINE_DASHDOTDOT : LINE_DASHDOTDOT_ZERO; break;
			case SWT.LINE_CUSTOM: dashes = data.lineDashes; break;
		}
		if (dashes != null) {
			if ((state & LINE_STYLE) != 0) {
				byte[] dash_list = new byte[dashes.length];
				for (int i = 0; i < dash_list.length; i++) {
					dash_list[i] = (byte)(width == 0 || data.lineStyle == SWT.LINE_CUSTOM ? dashes[i] : dashes[i] * width);
				}
				OS.XSetDashes(xDisplay, handle, 0, dash_list, dash_list.length);
			}
			line_style = OS.LineOnOffDash;
		} else {
			line_style = OS.LineSolid;
		}
		OS.XSetLineAttributes(xDisplay, handle, width, line_style, cap_style, join_style);
	}
}
int convertRgn(int rgn, double[] matrix) {
	int /*long*/ newRgn = OS.XCreateRegion();
	//TODO - get rectangles from region instead of clip box
	XRectangle rect = new XRectangle();
	OS.XClipBox(rgn, rect);
	short[] pointArray = new short[8];
	double[] x = new double[1], y = new double[1];
	x[0] = rect.x;
	y[0] = rect.y;
	Cairo.cairo_matrix_transform_point(matrix, x, y);
	pointArray[0] = (short)x[0];
	pointArray[1] = (short)y[0];
	x[0] = rect.x + rect.width;
	y[0] = rect.y;
	Cairo.cairo_matrix_transform_point(matrix, x, y);
	pointArray[2] = (short)Math.round(x[0]);
	pointArray[3] = (short)y[0];
	x[0] = rect.x + rect.width;
	y[0] = rect.y + rect.height;
	Cairo.cairo_matrix_transform_point(matrix, x, y);
	pointArray[4] = (short)Math.round(x[0]);
	pointArray[5] = (short)Math.round(y[0]);
	x[0] = rect.x;
	y[0] = rect.y + rect.height;
	Cairo.cairo_matrix_transform_point(matrix, x, y);
	pointArray[6] = (short)x[0];
	pointArray[7] = (short)Math.round(y[0]);
	int /*long*/ polyRgn = OS.XPolygonRegion(pointArray, pointArray.length / 2, OS.EvenOddRule);
	OS.XUnionRegion(newRgn, polyRgn, newRgn);
	OS.XDestroyRegion(polyRgn);
	return newRgn;
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
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - x, deltaY = destY - y;
	if (deltaX == 0 && deltaY == 0) return;
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	if (data.backgroundImage != null && paint) {
		OS.XClearArea (xDisplay, xDrawable, x, y, width, height, true);
		OS.XClearArea (xDisplay, xDrawable, destX, destY, width, height, true);
		return;
	}
	if (data.image == null && paint) OS.XSetGraphicsExposures (xDisplay, handle, true);
	OS.XCopyArea(xDisplay, xDrawable, xDrawable, handle, x, y, width, height, destX, destY);
	if (data.image == null && paint) {
		OS.XSetGraphicsExposures (xDisplay, handle, false);
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
	int xDisplay = data.display;
	int xGC = OS.XCreateGC(xDisplay, image.pixmap, 0, null);
	if (xGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.XSetSubwindowMode (xDisplay, xGC, OS.IncludeInferiors);
	OS.XCopyArea(xDisplay, data.drawable, image.pixmap, xGC, x, y, rect.width, rect.height, 0, 0);
	OS.XFreeGC(xDisplay, xGC);
}
void destroy() {
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) Cairo.cairo_destroy(cairo);
	data.cairo = 0;

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
	int xmString = data.xmString;
	if (xmString != 0) OS.XmStringFree (xmString);
	int xmText = data.xmText;
	if (xmText != 0) OS.XmStringFree (xmText);
	int xmMnemonic = data.xmMnemonic;
	if (xmMnemonic != 0) OS.XmStringFree (xmMnemonic);

	/* Dispose the GC */
	if (drawable != null) drawable.internal_dispose_GC(handle, data);

	data.display = data.drawable = data.colormap = 
		data.clipRgn = data.renderTable = data.xmString = data.xmText = 
			data.xmMnemonic = 0;
	data.font = null;
	drawable = null;
	handle = 0;
	data.image = null;
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
	checkGC(DRAW);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		double xOffset = data.cairoXoffset, yOffset = data.cairoYoffset;
		if (width == height) {
            if (arcAngle >= 0) {
                Cairo.cairo_arc_negative(cairo, x + xOffset + width / 2f, y + yOffset + height / 2f, width / 2f, -startAngle * (float)Compatibility.PI / 180, -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
            } else { 
                Cairo.cairo_arc(cairo, x + xOffset + width / 2f, y + yOffset + height / 2f, width / 2f, -startAngle * (float)Compatibility.PI / 180, -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
            }
		} else {
			Cairo.cairo_save(cairo);
			Cairo.cairo_translate(cairo, x + xOffset + width / 2f, y + yOffset + height / 2f);
			Cairo.cairo_scale(cairo, width / 2f, height / 2f);
            if (arcAngle >= 0) {
                Cairo.cairo_arc_negative(cairo, 0, 0, 1, -startAngle * (float)Compatibility.PI / 180, -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
            } else {
                Cairo.cairo_arc(cairo, 0, 0, 1, -startAngle * (float)Compatibility.PI / 180, -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
            }
			Cairo.cairo_restore(cairo);
		}
		Cairo.cairo_stroke(cairo);
		return;
	}
	OS.XDrawArc(data.display, data.drawable, handle, x, y, width, height, startAngle * 64, arcAngle * 64);
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
	int cairo = data.cairo;
	if (cairo != 0) {
		int lineWidth = 1;
		Cairo.cairo_save(cairo);		
		Cairo.cairo_set_line_width(cairo, lineWidth);
		XColor color = new XColor();
		color.pixel = highlightColor;
		OS.XQueryColor (data.display, data.colormap, color);
		Cairo.cairo_set_source_rgba(cairo, (color.red & 0xFFFF) / (float)0xFFFF, (color.green & 0xFFFF) / (float)0xFFFF, (color.blue & 0xFFFF) / (float)0xFFFF, 1);
		Cairo.cairo_rectangle(cairo, x + lineWidth / 2f, y + lineWidth / 2f, width, height);
		Cairo.cairo_stroke(cairo);
		Cairo.cairo_restore(cairo);
		return;
	}
	OS.XSetForeground (xDisplay, handle, highlightColor);
	OS.XDrawRectangle (xDisplay, xDrawable, handle, x, y, width - 1, height - 1);
	data.state &= ~(BACKGROUND | FOREGROUND);
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
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		if (data.alpha != 0) {
			srcImage.createSurface();
			Cairo.cairo_save(cairo);
			Cairo.cairo_rectangle(cairo, destX , destY, destWidth, destHeight);
			Cairo.cairo_clip(cairo);
			Cairo.cairo_translate(cairo, destX - srcX, destY - srcY);
			if (srcWidth != destWidth || srcHeight != destHeight) {
				Cairo.cairo_scale(cairo, destWidth / (float)srcWidth,  destHeight / (float)srcHeight);
			}
			int filter = Cairo.CAIRO_FILTER_GOOD;
			switch (data.interpolation) {
				case SWT.DEFAULT: filter = Cairo.CAIRO_FILTER_GOOD; break;
				case SWT.NONE: filter = Cairo.CAIRO_FILTER_NEAREST; break;
				case SWT.LOW: filter = Cairo.CAIRO_FILTER_FAST; break;
				case SWT.HIGH: filter = Cairo.CAIRO_FILTER_BEST; break;
			}
			int /*long*/ pattern = Cairo.cairo_pattern_create_for_surface(srcImage.surface);
			if (pattern == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			if (srcWidth != destWidth || srcHeight != destHeight) {
				/*
				* Bug in Cairo.  When drawing the image streched with an interpolation
				* alghorithm, the edges of the image are faded.  This is not a bug, but
				* it is not desired.  To avoid the faded edges, it should be possible to
				* use cairo_pattern_set_extend() to set the pattern extend to either
				* CAIRO_EXTEND_REFLECT or CAIRO_EXTEND_PAD, but these are not implemented
				* in some versions of cairo (1.2.x) and have bugs in others (in 1.4.2 it
				* draws with black edges).  The fix is to implement CAIRO_EXTEND_REFLECT
				* by creating an image that is 3 times bigger than the original, drawing
				* the original image in every quadrant (with an appropriate transform) and
				* use this image as the pattern.
				* 
				* NOTE: For some reaons, it is necessary to use CAIRO_EXTEND_PAD with
				* the image that was created or the edges are still faded.
				*/
				if (Cairo.cairo_version () >= Cairo.CAIRO_VERSION_ENCODE(1, 4, 2)) {
					int /*long*/ surface = Cairo.cairo_image_surface_create(Cairo.CAIRO_FORMAT_ARGB32, imgWidth * 3, imgHeight * 3);
					int /*long*/ cr = Cairo.cairo_create(surface);
					Cairo.cairo_set_source_surface(cr, srcImage.surface, imgWidth, imgHeight);
					Cairo.cairo_paint(cr);
					Cairo.cairo_scale(cr, -1, -1);
					Cairo.cairo_set_source_surface(cr, srcImage.surface, -imgWidth, -imgHeight);
					Cairo.cairo_paint(cr);
					Cairo.cairo_set_source_surface(cr, srcImage.surface, -imgWidth * 3, -imgHeight);
					Cairo.cairo_paint(cr);
					Cairo.cairo_set_source_surface(cr, srcImage.surface, -imgWidth, -imgHeight * 3);
					Cairo.cairo_paint(cr);
					Cairo.cairo_set_source_surface(cr, srcImage.surface, -imgWidth * 3, -imgHeight * 3);
					Cairo.cairo_paint(cr);
					Cairo.cairo_scale(cr, 1, -1);
					Cairo.cairo_set_source_surface(cr, srcImage.surface, -imgWidth, imgHeight);
					Cairo.cairo_paint(cr);
					Cairo.cairo_set_source_surface(cr, srcImage.surface, -imgWidth * 3, imgHeight);
					Cairo.cairo_paint(cr);
					Cairo.cairo_scale(cr, -1, -1);
					Cairo.cairo_set_source_surface(cr, srcImage.surface, imgWidth, -imgHeight);
					Cairo.cairo_paint(cr);
					Cairo.cairo_set_source_surface(cr, srcImage.surface, imgWidth, -imgHeight * 3);
					Cairo.cairo_paint(cr);
					Cairo.cairo_destroy(cr);
					int /*long*/ newPattern = Cairo.cairo_pattern_create_for_surface(surface);
					Cairo.cairo_surface_destroy(surface);
					if (newPattern == 0) SWT.error(SWT.ERROR_NO_HANDLES);
					Cairo.cairo_pattern_destroy(pattern);
					pattern = newPattern;
					Cairo.cairo_pattern_set_extend(pattern, Cairo.CAIRO_EXTEND_PAD);
					double[] matrix = new double[6]; 
					Cairo.cairo_matrix_init_translate(matrix, imgWidth, imgHeight);
					Cairo.cairo_pattern_set_matrix(pattern, matrix);
				}
//				Cairo.cairo_pattern_set_extend(pattern, Cairo.CAIRO_EXTEND_REFLECT);
			}
			Cairo.cairo_pattern_set_filter(pattern, filter);
			Cairo.cairo_set_source(cairo, pattern);
			if (data.alpha != 0xFF) {
				Cairo.cairo_paint_with_alpha(cairo, data.alpha / (float)0xFF);
			} else {
				Cairo.cairo_paint(cairo);
			}
			Cairo.cairo_restore(cairo);
			Cairo.cairo_pattern_destroy(pattern);
		}
		return;
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
	if (device.useXRender) {
		drawImageXRender(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, srcImage.mask, OS.PictStandardA8);
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
		if (xDestImagePtr == 0) return;
		XImage xDestImage = new XImage();
		OS.memmove(xDestImage, xDestImagePtr, XImage.sizeof);
		byte[] destData = new byte[xDestImage.bytes_per_line * xDestImage.height];
		OS.memmove(destData, xDestImage.data, destData.length);	
	
		/* Get the foreground pixels */
		xSrcImagePtr = OS.XGetImage(xDisplay, srcImage.pixmap, srcX, srcY, srcWidth, srcHeight, OS.AllPlanes, OS.ZPixmap);
		if (xSrcImagePtr == 0) return;
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
	/* Generate the mask if necessary. */
	if (srcImage.transparentPixel != -1) srcImage.createMask();

	if (device.useXRender) {
		drawImageXRender(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, srcImage.mask, OS.PictStandardA1);
	} else {
		int colorPixmap = 0, maskPixmap = 0;
		int foreground = 0x00000000;
		if (simple || (srcWidth == destWidth && srcHeight == destHeight)) {
			colorPixmap = srcImage.pixmap;
			maskPixmap = srcImage.mask;
		} else {
			/* Stretch the color and mask*/
			int xImagePtr = scalePixmap(xDisplay, srcImage.pixmap, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false, false);
			if (xImagePtr != 0) {
				int xMaskPtr = scalePixmap(xDisplay, srcImage.mask, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false, false);
				if (xMaskPtr != 0) {
					/* Create color scaled pixmaps */
					colorPixmap = OS.XCreatePixmap(xDisplay, xDrawable, destWidth, destHeight, depth);
					int tempGC = OS.XCreateGC(xDisplay, colorPixmap, 0, null);
					OS.XPutImage(xDisplay, colorPixmap, tempGC, xImagePtr, 0, 0, 0, 0, destWidth, destHeight);
					OS.XFreeGC(xDisplay, tempGC);
			
					/* Create mask scaled pixmaps */
					maskPixmap = OS.XCreatePixmap(xDisplay, xDrawable, destWidth, destHeight, 1);
					tempGC = OS.XCreateGC(xDisplay, maskPixmap, 0, null);
					OS.XPutImage(xDisplay, maskPixmap, tempGC, xMaskPtr, 0, 0, 0, 0, destWidth, destHeight);
					OS.XFreeGC(xDisplay, tempGC);
	
					OS.XDestroyImage(xMaskPtr);
				}
				OS.XDestroyImage(xImagePtr);
			}
			
			/* Change the source rectangle */
			srcX = srcY = 0;
			srcWidth = destWidth;
			srcHeight = destHeight;
	
			foreground = ~foreground;
		}
		
		/* Do the blts */
		if (colorPixmap != 0 && maskPixmap != 0) {
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
		}

		/* Destroy scaled pixmaps */
		if (colorPixmap != 0 && srcImage.pixmap != colorPixmap) OS.XFreePixmap(xDisplay, colorPixmap);
		if (maskPixmap != 0 && srcImage.mask != maskPixmap) OS.XFreePixmap(xDisplay, maskPixmap);
	}

	/* Destroy the image mask if the there is a GC created on the image */
	if (srcImage.transparentPixel != -1 && srcImage.memGC != null) srcImage.destroyMask();
}
void drawImageXRender(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight, int maskPixmap, int maskType) {
	int drawable = data.drawable;
	int xDisplay = data.display;
	int maskPict = 0;
	if (maskPixmap != 0) {
		int attribCount = 0;
		XRenderPictureAttributes attrib = null;
		if (srcImage.alpha != -1) {
			attribCount = 1;
			attrib = new XRenderPictureAttributes();
			attrib.repeat = true;
		}
		maskPict = OS.XRenderCreatePicture(xDisplay, maskPixmap, OS.XRenderFindStandardFormat(xDisplay, maskType), attribCount, attrib);
		if (maskPict == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	}
	int format = OS.XRenderFindVisualFormat(xDisplay, OS.XDefaultVisual(xDisplay, OS.XDefaultScreen(xDisplay)));
	int destPict = OS.XRenderCreatePicture(xDisplay, drawable, format, 0, null);
	if (destPict == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int srcPict = OS.XRenderCreatePicture(xDisplay, srcImage.pixmap, format, 0, null);
	if (srcPict == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (srcWidth != destWidth || srcHeight != destHeight) {
		int[] transform = new int[]{(int)(((float)srcWidth / destWidth) * 65536), 0, 0, 0, (int)(((float)srcHeight / destHeight) * 65536), 0, 0, 0, 65536};
		OS.XRenderSetPictureTransform(xDisplay, srcPict, transform);
		if (maskPict != 0) OS.XRenderSetPictureTransform(xDisplay, maskPict, transform);
		srcX *= destWidth / (float)srcWidth;
		srcY *= destHeight / (float)srcHeight;
	}
	int clipping = data.clipRgn;
	if (data.damageRgn != 0) {
		if (clipping == 0) {
			clipping = data.damageRgn;
		} else {
			clipping = OS.XCreateRegion ();
			OS.XUnionRegion(clipping, data.clipRgn, clipping);
			OS.XIntersectRegion(clipping, data.damageRgn, clipping);
		}
	}
	if (clipping != 0) {
		OS.XRenderSetPictureClipRegion(xDisplay, destPict, clipping);
		if (clipping != data.clipRgn && clipping != data.damageRgn) {
			OS.XDestroyRegion(clipping);
		}
	}
	OS.XRenderComposite(xDisplay, maskPict != 0 ? OS.PictOpOver : OS.PictOpSrc, srcPict, maskPict, destPict, srcX, srcY, srcX, srcY, destX, destY, destWidth, destHeight);
	OS.XRenderFreePicture(xDisplay, destPict);
	OS.XRenderFreePicture(xDisplay, srcPict);
	if (maskPict != 0) OS.XRenderFreePicture(xDisplay, maskPict);
}
void drawImage(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight, int depth) {
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	/* Simple case: no stretching */
	if ((srcWidth == destWidth) && (srcHeight == destHeight)) {
		OS.XCopyArea(xDisplay, srcImage.pixmap, xDrawable, handle, srcX, srcY, srcWidth, srcHeight, destX, destY);
		return;
	}
	if (device.useXRender) {
		drawImageXRender(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, 0, -1);
		return;
	}
	
	/* Streching case */
	int xImagePtr = scalePixmap(xDisplay, srcImage.pixmap, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false, false);
	if (xImagePtr != 0) {
		OS.XPutImage(xDisplay, xDrawable, handle, xImagePtr, 0, 0, destX, destY, destWidth, destHeight);
		OS.XDestroyImage(xImagePtr);
	}
}
static int scalePixmap(int display, int pixmap, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean flipX, boolean flipY) {
	int xSrcImagePtr = OS.XGetImage(display, pixmap, srcX, srcY, srcWidth, srcHeight, OS.AllPlanes, OS.ZPixmap);
	if (xSrcImagePtr == 0) return 0;
	XImage xSrcImage = new XImage();
	OS.memmove(xSrcImage, xSrcImagePtr, XImage.sizeof);
	byte[] srcData = new byte[xSrcImage.bytes_per_line * xSrcImage.height];
	OS.memmove(srcData, xSrcImage.data, srcData.length);
	OS.XDestroyImage(xSrcImagePtr);
	int xImagePtr = 0;
	int visual = OS.XDefaultVisual(display, OS.XDefaultScreen(display));
	switch (xSrcImage.bits_per_pixel) {
		case 1:
		case 4:
		case 8: {
			int format = xSrcImage.bits_per_pixel == 1 ? OS.XYBitmap : OS.ZPixmap;
			xImagePtr = OS.XCreateImage(display, visual, xSrcImage.depth, format, 0, 0, destWidth, destHeight, xSrcImage.bitmap_pad, 0);
			if (xImagePtr == 0) return 0;
			XImage xImage = new XImage();
			OS.memmove(xImage, xImagePtr, XImage.sizeof);
			int bufSize = xImage.bytes_per_line * xImage.height;
			if (bufSize < 0) {
				OS.XDestroyImage(xImagePtr);
				return 0;
			} 
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
			if (xImagePtr == 0) return 0;
			XImage xImage = new XImage();
			OS.memmove(xImage, xImagePtr, XImage.sizeof);
			int bufSize = xImage.bytes_per_line * xImage.height;
			if (bufSize < 0) {
				OS.XDestroyImage(xImagePtr);
				return 0;
			} 
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
	checkGC(DRAW);
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		double xOffset = data.cairoXoffset, yOffset = data.cairoYoffset;
		Cairo.cairo_move_to(cairo, x1 + xOffset, y1 + yOffset);
		Cairo.cairo_line_to(cairo, x2 + xOffset, y2 + yOffset);
		Cairo.cairo_stroke(cairo);
		return;
	}
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
	checkGC(DRAW);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		double xOffset = data.cairoXoffset, yOffset = data.cairoYoffset;
		if (width == height) {
			Cairo.cairo_arc_negative(cairo, x + xOffset + width / 2f, y + yOffset + height / 2f, width / 2f, 0, -2 * (float)Compatibility.PI);
		} else {
			Cairo.cairo_save(cairo);
			Cairo.cairo_translate(cairo, x + xOffset + width / 2f, y + yOffset + height / 2f);
			Cairo.cairo_scale(cairo, width / 2f, height / 2f);
			Cairo.cairo_arc_negative(cairo, 0, 0, 1, 0, -2 * (float)Compatibility.PI);
			Cairo.cairo_restore(cairo);
		}
		Cairo.cairo_stroke(cairo);
		return;
	}
	OS.XDrawArc(data.display, data.drawable, handle, x, y, width, height, 0, 23040);
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
	initCairo();
	checkGC(DRAW);
	int /*long*/ cairo = data.cairo;
	Cairo.cairo_save(cairo);
	double xOffset = data.cairoXoffset, yOffset = data.cairoYoffset;
	Cairo.cairo_translate(cairo, xOffset, yOffset);
	int /*long*/ copy = Cairo.cairo_copy_path(path.handle);
	if (copy == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	Cairo.cairo_append_path(cairo, copy);
	Cairo.cairo_path_destroy(copy);
	Cairo.cairo_stroke(cairo);
	Cairo.cairo_restore(cairo);
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
	checkGC(DRAW);
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		Cairo.cairo_rectangle(cairo, x, y, 1, 1);
		Cairo.cairo_fill(cairo);
		return;
	}
	OS.XDrawPoint(data.display, data.drawable, handle, x, y);
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
	checkGC(DRAW);
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		drawPolyline(cairo, pointArray, true);
		Cairo.cairo_stroke(cairo);
		return;
	}
	
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
	checkGC(DRAW);
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		drawPolyline(cairo, pointArray, false);
		Cairo.cairo_stroke(cairo);
		return;
	}
	short[] xPoints = new short[pointArray.length];
	for (int i = 0; i<pointArray.length;i++) {
		xPoints[i] = (short) pointArray[i];
	}
	OS.XDrawLines(data.display,data.drawable,handle,xPoints,xPoints.length / 2, OS.CoordModeOrigin);
}
void drawPolyline(int /*long*/ cairo, int[] pointArray, boolean close) {
	int count = pointArray.length / 2;
	if (count == 0) return;
	double xOffset = data.cairoXoffset, yOffset = data.cairoYoffset;
	Cairo.cairo_move_to(cairo, pointArray[0] + xOffset, pointArray[1] + yOffset);
	for (int i = 1, j=2; i < count; i++, j += 2) {
		Cairo.cairo_line_to(cairo, pointArray[j] + xOffset, pointArray[j + 1] + yOffset);
	}
	if (close) Cairo.cairo_close_path(cairo);
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
	checkGC(DRAW);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		double xOffset = data.cairoXoffset, yOffset = data.cairoYoffset;
		Cairo.cairo_rectangle(cairo, x + xOffset, y + yOffset, width, height);
		Cairo.cairo_stroke(cairo);
		return;
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
	checkGC(DRAW);
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
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		double xOffset = data.cairoXoffset, yOffset = data.cairoYoffset;
		if (naw == 0 || nah == 0) {
			Cairo.cairo_rectangle(cairo, x + xOffset, y + yOffset, width, height);
		} else {
			float naw2 = naw / 2f;
			float nah2 = nah / 2f;
			float fw = nw / naw2;
			float fh = nh / nah2;
			Cairo.cairo_save(cairo);
			Cairo.cairo_translate(cairo, nx + xOffset, ny + yOffset);
			Cairo.cairo_scale(cairo, naw2, nah2);
			Cairo.cairo_move_to(cairo, fw - 1, 0);
		    Cairo.cairo_arc(cairo, fw - 1, 1, 1, Compatibility.PI + Compatibility.PI/2.0, Compatibility.PI*2.0);
		    Cairo.cairo_arc(cairo, fw - 1, fh - 1, 1, 0, Compatibility.PI/2.0);
		    Cairo.cairo_arc(cairo, 1, fh - 1, 1, Compatibility.PI/2, Compatibility.PI);
		    Cairo.cairo_arc(cairo, 1, 1, 1, Compatibility.PI, 270.0*Compatibility.PI/180.0);
			Cairo.cairo_close_path(cairo);
			Cairo.cairo_restore(cairo);
		}
		Cairo.cairo_stroke(cairo);
		return;
	}
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
	if (string.length() == 0) return;
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		//TODO - honor isTransparent
		checkGC(FOREGROUND | FONT);
		cairo_font_extents_t extents = new cairo_font_extents_t();
		Cairo.cairo_font_extents(cairo, extents);
		double baseline = y + extents.ascent;
		Cairo.cairo_move_to(cairo, x, baseline);
		byte[] buffer = Converter.wcsToMbcs(null, string, true);
		Cairo.cairo_show_text(cairo, buffer);
		Cairo.cairo_new_path(cairo);
		return;
	}
	setString(string);
	checkGC(FOREGROUND | FONT | BACKGROUND_BG);
	if (isTransparent) {
		OS.XmStringDraw (data.display, data.drawable, data.font.handle, data.xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null);
	} else {
		OS.XmStringDrawImage (data.display, data.drawable, data.font.handle, data.xmString, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null);
	}			
}
void createRenderTable() {
	int fontList = data.font.handle;	
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
	int[] renditions = new int[4]; int renditionCount = 0;	
		
	/* Create a rendition for each entry in the font list */
	while ((fontListEntry = OS.XmFontListNextEntry(context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont(fontListEntry, fontBuffer);
		int fontType = (fontBuffer [0] == 0) ? OS.XmFONT_IS_FONT : OS.XmFONT_IS_FONTSET;
		if (fontPtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int [] argList = {
			OS.XmNtabList, tabList,
			OS.XmNfont, fontPtr,
			OS.XmNfontType, fontType,
		};
		int rendition = OS.XmRenditionCreate(data.device.shellHandle, OS.XmFONTLIST_DEFAULT_TAG, argList, argList.length / 2);
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
	if (string.length() == 0) return;
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		//TODO - honor flags
		checkGC(FOREGROUND | FONT);
		cairo_font_extents_t extents = new cairo_font_extents_t();
		Cairo.cairo_font_extents(cairo, extents);
		double baseline = y + extents.ascent;
		Cairo.cairo_move_to(cairo, x, baseline);
		byte[] buffer = Converter.wcsToMbcs(null, string, true);
		Cairo.cairo_show_text(cairo, buffer);
		Cairo.cairo_new_path(cairo);
		return;
	}
	setText(string, flags);
	checkGC(FOREGROUND | FONT | BACKGROUND_BG);
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	if (data.image != null) OS.XtRegisterDrawable (xDisplay, xDrawable, data.device.shellHandle);
	int xmMnemonic = data.xmMnemonic;
	if (xmMnemonic != 0) {
		OS.XmStringDrawUnderline(xDisplay, xDrawable, data.renderTable, data.xmText, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null, xmMnemonic);
	} else {
		if ((flags & SWT.DRAW_TRANSPARENT) != 0) {
			OS.XmStringDraw(xDisplay, xDrawable, data.renderTable, data.xmText, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null);
		} else {
			OS.XmStringDrawImage(xDisplay, xDrawable, data.renderTable, data.xmText, handle, x, y, 0x7FFFFFFF, OS.XmALIGNMENT_BEGINNING, 0, null);
		}
	}
	if (data.image != null) OS.XtUnregisterDrawable (xDisplay, xDrawable);
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
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawArc
 */
public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(FILL);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		if (width == height) {
            if (arcAngle >= 0) {
            	Cairo.cairo_arc_negative(cairo, x + width / 2f, y + height / 2f, width / 2f, -startAngle * (float)Compatibility.PI / 180,  -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
            } else {
            	Cairo.cairo_arc(cairo, x + width / 2f, y + height / 2f, width / 2f, -startAngle * (float)Compatibility.PI / 180,  -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
            }
			Cairo.cairo_line_to(cairo, x + width / 2f, y + height / 2f);
		} else {
			Cairo.cairo_save(cairo);
			Cairo.cairo_translate(cairo, x + width / 2f, y + height / 2f);
			Cairo.cairo_scale(cairo, width / 2f, height / 2f);
			if (arcAngle >= 0) {
				Cairo.cairo_arc_negative(cairo, 0, 0, 1, -startAngle * (float)Compatibility.PI / 180,  -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
			} else {
				Cairo.cairo_arc(cairo, 0, 0, 1, -startAngle * (float)Compatibility.PI / 180,  -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
			}
			Cairo.cairo_line_to(cairo, 0, 0);
			Cairo.cairo_restore(cairo);
		}
		Cairo.cairo_fill(cairo);
		return;
	}
	OS.XFillArc(data.display, data.drawable, handle, x, y, width, height, startAngle * 64, arcAngle * 64);
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
	/* X Window deals with a virtually limitless array of color formats
	 * but we only distinguish between paletted and direct modes
	 */	
	int xDisplay = data.display;
	int xScreenNum = OS.XDefaultScreen(xDisplay);
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

	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		int /*long*/ pattern;
		if (vertical) {
			pattern = Cairo.cairo_pattern_create_linear (0.0, 0.0, 0.0, 1.0);
		} else {
			pattern = Cairo.cairo_pattern_create_linear (0.0, 0.0, 1.0, 0.0);
		}
		Cairo.cairo_pattern_add_color_stop_rgba (pattern, 0, fromRGB.red / 255f, fromRGB.green / 255f, fromRGB.blue / 255f, data.alpha / 255f);
		Cairo.cairo_pattern_add_color_stop_rgba (pattern, 1, toRGB.red / 255f, toRGB.green / 255f, toRGB.blue / 255f, data.alpha / 255f);
		Cairo.cairo_save(cairo);
		Cairo.cairo_translate(cairo, x, y);
		Cairo.cairo_scale(cairo, width, height);
		Cairo.cairo_rectangle(cairo, 0, 0, 1, 1);
		Cairo.cairo_set_source(cairo, pattern);
		Cairo.cairo_fill(cairo);
		Cairo.cairo_restore(cairo);
		Cairo.cairo_pattern_destroy(pattern);
		return;
	}
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
	checkGC(FILL);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		if (width == height) {
			Cairo.cairo_arc_negative(cairo, x + width / 2f, y + height / 2f, width / 2f, 0, 2 * (float)Compatibility.PI);
		} else {
			Cairo.cairo_save(cairo);
			Cairo.cairo_translate(cairo, x + width / 2f, y + height / 2f);
			Cairo.cairo_scale(cairo, width / 2f, height / 2f);
			Cairo.cairo_arc_negative(cairo, 0, 0, 1, 0, 2 * (float)Compatibility.PI);
			Cairo.cairo_restore(cairo);
		}
		Cairo.cairo_fill(cairo);
		return;
	}
	OS.XFillArc (data.display, data.drawable, handle, x, y, width, height, 0, 23040);
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
	initCairo();
	checkGC(FILL);
	int /*long*/ cairo = data.cairo;
	int /*long*/ copy = Cairo.cairo_copy_path(path.handle);
	if (copy == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	Cairo.cairo_append_path(cairo, copy);
	Cairo.cairo_path_destroy(copy);
	Cairo.cairo_fill(cairo);
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
	checkGC(FILL);
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		drawPolyline(cairo, pointArray, true);
		Cairo.cairo_fill(cairo);
		return;
	}
	short[] xPoints = new short[pointArray.length];
	for (int i = 0; i<pointArray.length;i++) {
		xPoints[i] = (short) pointArray[i];
	}
	OS.XFillPolygon(data.display, data.drawable, handle,xPoints, xPoints.length / 2, OS.Complex, OS.CoordModeOrigin);
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
	checkGC(FILL);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	int /*long*/ cairo = data.cairo; 
	if (cairo != 0) {
		Cairo.cairo_rectangle(cairo, x, y, width, height);
		Cairo.cairo_fill(cairo);
		return;
	}
	OS.XFillRectangle (data.display, data.drawable, handle, x, y, width, height);
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
public void fillRoundRectangle (int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(FILL);
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
	if (naw < 0) naw = 0 - naw;
	if (nah < 0) nah = 0 - nah;
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		if (naw == 0 || nah == 0) {
			Cairo.cairo_rectangle(cairo, x, y, width, height);
		} else {
			float naw2 = naw / 2f;
			float nah2 = nah / 2f;
			float fw = nw / naw2;
			float fh = nh / nah2;
			Cairo.cairo_save(cairo);
			Cairo.cairo_translate(cairo, nx, ny);
			Cairo.cairo_scale(cairo, naw2, nah2);
			Cairo.cairo_move_to(cairo, fw - 1, 0);
		    Cairo.cairo_arc(cairo, fw - 1, 1, 1, Compatibility.PI + Compatibility.PI/2.0, Compatibility.PI*2.0);
		    Cairo.cairo_arc(cairo, fw - 1, fh - 1, 1, 0, Compatibility.PI/2.0);
		    Cairo.cairo_arc(cairo, 1, fh - 1, 1, Compatibility.PI/2, Compatibility.PI);
		    Cairo.cairo_arc(cairo, 1, 1, 1, Compatibility.PI, 270.0*Compatibility.PI/180.0);		
			Cairo.cairo_close_path(cairo);
			Cairo.cairo_restore(cairo);
		}
		Cairo.cairo_fill(cairo);
		return;
	}
	int naw2 = naw / 2;
	int nah2 = nah / 2;
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	if (nw > naw) {
		if (nh > nah) {
			OS.XFillArc(xDisplay, xDrawable, handle, nx, ny, naw, nah, 5760, 5760);
			OS.XFillRectangle(xDisplay, xDrawable, handle, nx + naw2, ny, nw - naw2 * 2, nah2);
			OS.XFillArc(xDisplay, xDrawable, handle, nx + nw - naw, ny, naw, nah, 0, 5760);
			OS.XFillRectangle(xDisplay, xDrawable, handle, nx, ny + nah2, nw, nh - nah2 * 2);
			OS.XFillArc(xDisplay, xDrawable, handle, nx + nw - naw, ny + nh - nah, naw, nah, 17280, 5760);
			OS.XFillRectangle(xDisplay, xDrawable, handle, nx + naw2, ny + nh - nah2, nw - naw2 * 2, nah2);
			OS.XFillArc(xDisplay, xDrawable, handle, nx, ny + nh - nah, naw, nah, 11520, 5760);
		} else {
			OS.XFillArc(xDisplay, xDrawable, handle, nx, ny, naw, nh, 5760, 11520);
			OS.XFillRectangle(xDisplay, xDrawable, handle, nx + naw2, ny, nw - naw2 * 2, nh);
			OS.XFillArc(xDisplay, xDrawable, handle, nx + nw - naw, ny, naw, nh, 17280, 11520);
		}
	} else {
		if (nh > nah) {
			OS.XFillArc(xDisplay, xDrawable, handle, nx, ny, nw, nah, 0, 11520);
			OS.XFillRectangle(xDisplay, xDrawable, handle, nx, ny + nah2, nw, nh - nah2 * 2);
			OS.XFillArc(xDisplay, xDrawable, handle, nx, ny + nh - nah, nw, nah, 11520, 11520);
		} else {
			OS.XFillArc(xDisplay, xDrawable, handle, nx, ny, nw, nh, 0, 23040);
		}
	}
}
char fixMnemonic(char[] text) {
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
	checkGC(FONT);
	int fontList  = data.font.handle;
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
			}
		}
	}
	OS.XmFontListFreeFontContext(context);
	return stringExtent(new String(new char[]{ch})).x;
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
	return data.cairo != 0;
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
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
    if (data.cairo == 0) return SWT.DEFAULT;
    int antialias = Cairo.cairo_get_antialias(data.cairo);
	switch (antialias) {
		case Cairo.CAIRO_ANTIALIAS_DEFAULT: return SWT.DEFAULT;
		case Cairo.CAIRO_ANTIALIAS_NONE: return SWT.OFF;
		case Cairo.CAIRO_ANTIALIAS_GRAY:
		case Cairo.CAIRO_ANTIALIAS_SUBPIXEL: return SWT.ON;
	}
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
	XColor color = data.background;
	if ((data.state & BACKGROUND_RGB) == 0) {
		OS.XQueryColor(data.display, data.colormap, color);
		data.state |= BACKGROUND_RGB;
	}
	return Color.motif_new(data.device, color);
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
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(FONT);
	int fontList = data.font.handle;
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
			}
		}
	}
	OS.XmFontListFreeFontContext(context);
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
	/* Calculate visible bounds in device space */
	int x = 0, y = 0, width = 0, height = 0;
	int[] w = new int[1], h = new int[1], unused = new int[1];
	OS.XGetGeometry(data.display, data.drawable, unused, unused, unused, w, h, unused, unused);
	width = w[0];
	height = h[0];
	/* Intersect visible bounds with clipping in device space and then convert then to user space */
	int cairo = data.cairo;
	int clipRgn = data.clipRgn;
	int damageRgn = data.damageRgn;
	if (clipRgn != 0 || damageRgn != 0 || cairo != 0) {
		int rgn = OS.XCreateRegion ();
		XRectangle rect = new XRectangle();
		rect.width = (short)width;
		rect.height = (short)height;
		OS.XUnionRectWithRegion(rect, rgn, rgn);
		if (damageRgn != 0) {
			OS.XIntersectRegion (damageRgn, rgn, rgn);
		}
		/* Intersect visible bounds with clipping */
		if (clipRgn != 0) {
			/* Convert clipping to device space if needed */
			if (data.clippingTransform != null) {
				clipRgn = convertRgn(clipRgn, data.clippingTransform);
				OS.XIntersectRegion(rgn, clipRgn, rgn);
				OS.XDestroyRegion(clipRgn);
			} else {
				OS.XIntersectRegion(rgn, clipRgn, rgn);
			}
		}
		/* Convert to user space */
		if (cairo != 0) {
			double[] matrix = new double[6];
			Cairo.cairo_get_matrix(cairo, matrix);
			Cairo.cairo_matrix_invert(matrix);
			clipRgn = convertRgn(rgn, matrix);
			OS.XDestroyRegion(rgn);
			rgn = clipRgn;
		}
		OS.XClipBox(rgn, rect);
		OS.XDestroyRegion(rgn);
		x = rect.x;
		y = rect.y;
		width = rect.width;
		height = rect.height;
	}
	return new Rectangle(x, y, width, height);
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
	int clipping = region.handle;
	OS.XSubtractRegion (clipping, clipping, clipping);
	int /*long*/ cairo = data.cairo;
	int clipRgn = data.clipRgn;
	if (clipRgn == 0) {
		int[] width = new int[1], height = new int[1], unused = new int[1];
		OS.XGetGeometry(data.display, data.drawable, unused, unused, unused, width, height, unused, unused);
		XRectangle rect = new XRectangle();
		rect.x = 0;
		rect.y = 0;
		rect.width = (short)width[0];
		rect.height = (short)height[0];
		OS.XUnionRectWithRegion(rect, clipping, clipping);
	} else {
		/* Convert clipping to device space if needed */
		if (data.clippingTransform != null) {
			int rgn = convertRgn(clipRgn, data.clippingTransform);
			OS.XUnionRegion(clipping, rgn, clipping);
			OS.XDestroyRegion(rgn);
		} else {
			OS.XUnionRegion(clipping, clipRgn, clipping);
		}
	}
	if (data.damageRgn != 0) {
		OS.XIntersectRegion(clipping, data.damageRgn, clipping);
	}
	/* Convert to user space */
	if (cairo != 0) {
		double[] matrix = new double[6];
		Cairo.cairo_get_matrix(cairo, matrix);
		Cairo.cairo_matrix_invert(matrix);
		int rgn = convertRgn(clipping, matrix);
		OS.XSubtractRegion(clipping, clipping, clipping);
		OS.XUnionRegion(clipping, rgn, clipping);
		OS.XDestroyRegion(rgn);
	}
}
String getCodePage () {
	return data.font.codePage;
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
	XGCValues values = new XGCValues();
	OS.XGetGCValues(data.display, handle, OS.GCFillRule, values);
	return values.fill_rule == OS.WindingRule ? SWT.FILL_WINDING : SWT.FILL_EVEN_ODD;
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
	return Font.motif_new(data.device, data.font.handle);
}
int getFontHeight () {
	int fontList = data.font.handle;
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
			height = Math.max(height, fontHeight);
		} else {
			/* FontList contains a fontSet */
			int nFonts = OS.XFontsOfFontSet (fontPtr, fontStructPtr, fontNamePtr);
			int [] fontStructs = new int [nFonts];
			OS.memmove (fontStructs, fontStructPtr [0], nFonts * 4);
			
			/* Go through each fontStruct in the font set */
			for (int i=0; i<nFonts; i++) {
				OS.memmove (fontStruct, fontStructs[i], XFontStruct.sizeof);
				int fontHeight = fontStruct.ascent + fontStruct.descent;
				height = Math.max(height, fontHeight);
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
	checkGC(FONT);
	int xDisplay = data.display;
	Font font = data.font;
	int fontList = font.handle;
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
			ascent = Math.max(ascent, fontStruct.ascent);
			descent = Math.max(descent, fontStruct.descent);
			int fontHeight = fontStruct.ascent + fontStruct.descent;
			height = Math.max(height, fontHeight);
			/* Calculate average character width */
			int propPtr = fontStruct.properties;
			for (int i = 0; i < fontStruct.n_properties; i++) {
				/* Look through properties for XAFONT */
				int[] prop = new int[2];
				OS.memmove(prop, propPtr, 8);
				if (prop[0] == OS.XA_FONT) {
					/* Found it, prop[1] points to the string */
					int ptr = OS.XmGetAtomName(xDisplay, prop[1]);
					int length = OS.strlen(ptr);
					byte[] nameBuf = new byte[length];
					OS.memmove(nameBuf, ptr, length);
					OS.XFree(ptr);
					String xlfd = new String(Converter.mbcsToWcs(null, nameBuf)).toLowerCase();
					int avg = 0;
					try {
						avg = FontData.motif_new(xlfd).averageWidth / 10;
					} catch (Exception e) {
						// leave avg unchanged so that it will be computed below
					}
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
		} else {
			/* FontList contains a fontSet */
			int nFonts = OS.XFontsOfFontSet(fontPtr, fontStructPtr, fontNamePtr);
			int [] fontStructs = new int[nFonts];
			OS.memmove(fontStructs, fontStructPtr[0], nFonts * 4);
			/* Go through each fontStruct in the font set */
			for (int i = 0; i < nFonts; i++) {
				OS.memmove(fontStruct, fontStructs[i], XFontStruct.sizeof);
				ascent = Math.max(ascent, fontStruct.ascent);
				descent = Math.max(descent, fontStruct.descent);
				int fontHeight = fontStruct.ascent + fontStruct.descent;
				height = Math.max(height, fontHeight);
				/* Calculate average character width */
				int propPtr = fontStruct.properties;
				for (int j = 0; j < fontStruct.n_properties; j++) {
					/* Look through properties for XAFONT */
					int[] prop = new int[2];
					OS.memmove(prop, propPtr, 8);
					if (prop[0] == OS.XA_FONT) {
						/* Found it, prop[1] points to the string */
						int ptr = OS.XmGetAtomName(xDisplay, prop[1]);
						int length = OS.strlen(ptr);
						byte[] nameBuf = new byte[length];
						OS.memmove(nameBuf, ptr, length);
						OS.XFree(ptr);
						String xlfd = new String(Converter.mbcsToWcs(null, nameBuf)).toLowerCase();
						int avg = 0;
						try {
							avg = FontData.motif_new(xlfd).averageWidth / 10;
						} catch (Exception e) {
							/*
							 * Some font servers, for example, xfstt, do not pass
							 * reasonable font properties to the client, so we
							 * cannot construct a FontData for these. Use the font
							 * name instead.
							 */
							int[] fontName = new int[1];
							OS.memmove(fontName, fontNamePtr [0] + (i * 4), 4);
							ptr = fontName[0];
							if (ptr != 0 ) {
								length = OS.strlen(ptr);
								nameBuf = new byte[length];
								OS.memmove(nameBuf, ptr, length);
								xlfd = new String(Converter.mbcsToWcs(null, nameBuf)).toLowerCase();
								try {
									avg = FontData.motif_new(xlfd).averageWidth / 10;
								} catch (Exception ex) {
									// leave avg unchanged (0) so that it will be computed below
								}
							}
						}
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
	XColor color = data.foreground;
	if ((data.state & FOREGROUND_RGB) == 0) {
		OS.XQueryColor(data.display, data.colormap, color);
		data.state |= FOREGROUND_RGB;
	}
	return Color.motif_new(data.device, color);
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
	return data.interpolation;
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
	if (data.lineDashes != null) {
		dashes = new float[data.lineDashes.length];
		System.arraycopy(data.lineDashes, 0, dashes, 0, dashes.length);
	}
	return new LineAttributes(data.lineWidth, data.lineCap, data.lineJoin, data.lineStyle, dashes, data.lineDashesOffset, data.lineMiterLimit);
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
	if (data.lineDashes == null) return null;
	int[] lineDashes = new int[data.lineDashes.length];
	for (int i = 0; i < lineDashes.length; i++) {
		lineDashes[i] = (int)data.lineDashes[i];
	}
	return lineDashes;
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
	return (int)data.lineWidth;
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
    if (data.cairo == 0) return SWT.DEFAULT;
    int /*long*/ options = Cairo.cairo_font_options_create();
    Cairo.cairo_get_font_options(data.cairo, options);
    int antialias = Cairo.cairo_font_options_get_antialias(options);
    Cairo.cairo_font_options_destroy(options);
	switch (antialias) {
		case Cairo.CAIRO_ANTIALIAS_DEFAULT: return SWT.DEFAULT;
		case Cairo.CAIRO_ANTIALIAS_NONE: return SWT.OFF;
		case Cairo.CAIRO_ANTIALIAS_GRAY:
		case Cairo.CAIRO_ANTIALIAS_SUBPIXEL: return SWT.ON;
	}
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
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		Cairo.cairo_get_matrix(cairo, transform.handle);
	} else {
		transform.setElements(1, 0, 0, 1, 0, 0);
	}
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
void init(Drawable drawable, GCData data, int xGC) {
	if (data.foreground != null) data.state &= ~(FOREGROUND | FOREGROUND_RGB);
	if (data.background != null) data.state &= ~(BACKGROUND | BACKGROUND_BG | BACKGROUND_RGB);
	if (data.font != null) data.state &= ~FONT;
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
void initCairo() {
	data.device.checkCairo();
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) return;
	int xDisplay = data.display;
	int xDrawable = data.drawable;
	int xVisual = OS.XDefaultVisual(xDisplay, OS.XDefaultScreen(xDisplay));
	int[] width = new int[1], height = new int[1], unused = new int[1];
	OS.XGetGeometry(xDisplay, xDrawable, unused, unused, unused, width, height, unused, unused);
	int /*long*/ surface = Cairo.cairo_xlib_surface_create(xDisplay, xDrawable, xVisual, width[0], height[0]);
	if (surface == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	data.cairo = cairo = Cairo.cairo_create(surface);
	Cairo.cairo_surface_destroy(surface);
	if (cairo == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	Cairo.cairo_set_fill_rule(cairo, Cairo.CAIRO_FILL_RULE_EVEN_ODD);
	data.state &= ~(BACKGROUND | FOREGROUND | FONT | LINE_WIDTH | LINE_CAP | LINE_JOIN | LINE_STYLE | DRAW_OFFSET);
	setCairoClip(data.damageRgn, data.clipRgn);
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
boolean isIdentity(double[] matrix) {
	if (matrix == null) return true;
	return matrix[0] == 1 && matrix[1] == 0 && matrix[2] == 0 && matrix[3] == 1 && matrix[4] == 0 && matrix[5] == 0;
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
	if (advanced && data.cairo != 0) return;
	if (advanced) {
		try {
			initCairo();
		} catch (SWTException e) {}
	} else {
		int /*long*/ cairo = data.cairo;
		if (cairo != 0) Cairo.cairo_destroy(cairo);
		data.cairo = 0;
		data.interpolation = SWT.DEFAULT;
		data.backgroundPattern = data.foregroundPattern = null;
		data.state = 0;
		setClipping(0);
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
	if (data.cairo == 0 && (alpha & 0xff) == 0xff) return;
	initCairo();
	data.alpha = alpha & 0xff;
	data.state &= ~(BACKGROUND | FOREGROUND | BACKGROUND_BG);
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
	if (data.cairo == 0 && antialias == SWT.DEFAULT) return;
	int mode = 0;
	switch (antialias) {
		case SWT.DEFAULT: mode = Cairo.CAIRO_ANTIALIAS_DEFAULT; break;
		case SWT.OFF: mode = Cairo.CAIRO_ANTIALIAS_NONE; break;
		case SWT.ON: mode = Cairo.CAIRO_ANTIALIAS_GRAY;
            break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
    initCairo();
    int /*long*/ cairo = data.cairo;
    Cairo.cairo_set_antialias(cairo, mode);
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
public static GC motif_new(Drawable drawable, GCData data) {
	GC gc = new GC();
	int xGC = drawable.internal_new_GC(data);
	gc.device = data.device;
	gc.init(drawable, data, xGC);
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
 * @param xGC the X Windows graphics context.
 * @param data the data for the receiver.
 *
 * @return a new <code>GC</code>
 */
public static GC motif_new(int xGC, GCData data) {
	GC gc = new GC();
	gc.device = data.device;
	gc.init(null, data, xGC);
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
	data.background = color.handle;
	data.backgroundPattern = null;
	data.state &= ~(BACKGROUND | BACKGROUND_BG);
	data.state |= BACKGROUND_RGB;
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
public void setBackgroundPattern(Pattern pattern) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pattern != null && pattern.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (data.cairo == 0 && pattern == null) return;
	initCairo();
	if (data.backgroundPattern == pattern) return;
	data.backgroundPattern = pattern;
	data.state &= ~BACKGROUND;
}
static void setCairoFont(int /*long*/ cairo, Font font) {
	//TODO - use X font instead of loading new one???
	FontData[] fds = font.getFontData();
	FontData fd = fds[0];
	int style = fd.getStyle();
	int slant = Cairo.CAIRO_FONT_SLANT_NORMAL;
	if ((style & SWT.ITALIC) != 0) slant = Cairo.CAIRO_FONT_SLANT_ITALIC;
	int weight = Cairo.CAIRO_FONT_WEIGHT_NORMAL;
	if ((style & SWT.BOLD) != 0) weight = Cairo.CAIRO_FONT_WEIGHT_BOLD;
	String name = fd.getName();
	int index = name.indexOf('-');
	if (index != -1) name = name.substring(index + 1);
	byte[] buffer = Converter.wcsToMbcs(null, name, true);
	Cairo.cairo_select_font_face(cairo, buffer, slant, weight);
	Cairo.cairo_set_font_size(cairo, fd.getHeight());
}
static void setCairoRegion(int /*long*/ cairo, int /*long*/ rgn) {
	//TODO - get rectangles from region instead of clip box
	XRectangle rect = new XRectangle();
	OS.XClipBox(rgn, rect);
	Cairo.cairo_rectangle(cairo, rect.x, rect.y, rect.width, rect.height);
}
static void setCairoPatternColor(int /*long*/ pattern, int offset, Color c, int alpha) {
	XColor color = c.handle;
	double aa = (alpha & 0xFF) / (double)0xFF;
	double red = ((color.red & 0xFFFF) / (double)0xFFFF);
	double green = ((color.green & 0xFFFF) / (double)0xFFFF);
	double blue = ((color.blue & 0xFFFF) / (double)0xFFFF);
	Cairo.cairo_pattern_add_color_stop_rgba(pattern, offset, red, green, blue, aa);
}
void setCairoClip(int /*long*/ damageRgn, int /*long*/ clipRgn) {
	int /*long*/ cairo = data.cairo;
	Cairo.cairo_reset_clip(cairo);
	if (damageRgn != 0) {
		double[] matrix = new double[6];
		Cairo.cairo_get_matrix(cairo, matrix);
		Cairo.cairo_identity_matrix(cairo);
		setCairoRegion(cairo, damageRgn);
		Cairo.cairo_clip(cairo);
		Cairo.cairo_set_matrix(cairo, matrix);
	}
	if (clipRgn != 0) {
		setCairoRegion(cairo, clipRgn);
		Cairo.cairo_clip(cairo);
	}
}
void setClipping(int clipRgn) {
	int /*long*/ cairo = data.cairo;
	if (clipRgn == 0) {
		if (data.clipRgn != 0) {
			OS.XDestroyRegion (data.clipRgn);
			data.clipRgn = 0;
		}
		if (cairo != 0) {
			data.clippingTransform = null;
			setCairoClip(data.damageRgn, 0);
		} else {
			if (data.damageRgn == 0) {
				OS.XSetClipMask (data.display, handle, OS.None);
			} else {
				OS.XSetRegion (data.display, handle, data.damageRgn);			
			}
		}
	} else {
		if (data.clipRgn == 0) data.clipRgn = OS.XCreateRegion ();
		OS.XSubtractRegion (data.clipRgn, data.clipRgn, data.clipRgn);
		OS.XUnionRegion (clipRgn, data.clipRgn, data.clipRgn);
		if (cairo != 0) {
			if (data.clippingTransform == null) data.clippingTransform = new double[6];
			Cairo.cairo_get_matrix(cairo, data.clippingTransform);
			setCairoClip(data.damageRgn, clipRgn);
		} else {
			int clipping = clipRgn;
			if (data.damageRgn != 0) {
				clipping = OS.XCreateRegion();
				OS.XUnionRegion(clipping, clipRgn, clipping);
				OS.XIntersectRegion(clipping, data.damageRgn, clipping);
			}
			OS.XSetRegion (data.display, handle, clipping);
			if (clipping != clipRgn) OS.XDestroyRegion(clipping);
		}
	}
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
	XRectangle rect = new XRectangle ();
	rect.x = (short) x; 
	rect.y = (short) y;
	rect.width = (short) Math.max (0, width); 
	rect.height = (short) Math.max (0, height);
	int clipRgn = OS.XCreateRegion();
	OS.XUnionRectWithRegion(rect, clipRgn, clipRgn);
	setClipping(clipRgn);
	OS.XDestroyRegion(clipRgn);
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
	setClipping(0);
	if (path != null) {
		initCairo();
		int /*long*/ cairo = data.cairo;
		int /*long*/ copy = Cairo.cairo_copy_path(path.handle);
		if (copy == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		Cairo.cairo_append_path(cairo, copy);
		Cairo.cairo_path_destroy(copy);
		Cairo.cairo_clip(cairo);
		Cairo.cairo_new_path(cairo);
	}
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
		setClipping(0);
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
	setClipping(region != null ? region.handle : 0);
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
	int mode = OS.EvenOddRule, cairo_mode = Cairo.CAIRO_FILL_RULE_EVEN_ODD;
	switch (rule) {
		case SWT.FILL_WINDING:
			mode = OS.WindingRule; cairo_mode = Cairo.CAIRO_FILL_RULE_WINDING; break;
		case SWT.FILL_EVEN_ODD:
			mode = OS.EvenOddRule; cairo_mode = Cairo.CAIRO_FILL_RULE_EVEN_ODD; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	OS.XSetFillRule(data.display, handle, mode);
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		Cairo.cairo_set_fill_rule(cairo, cairo_mode);
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
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.font = font != null ? font : data.device.systemFont;
	data.state &= ~FONT;
	if (data.renderTable != 0) OS.XmRenderTableFree(data.renderTable);
	data.renderTable = 0;
	data.stringWidth = data.stringHeight = data.textWidth = data.textHeight = -1;
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
	data.foregroundPattern = null;
	data.state &= ~FOREGROUND;
	data.state |= FOREGROUND_RGB;
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
public void setForegroundPattern(Pattern pattern) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pattern != null && pattern.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (data.cairo == 0 && pattern == null) return;
	initCairo();
	if (data.foregroundPattern == pattern) return;
	data.foregroundPattern = pattern;
	data.state &= ~FOREGROUND;
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
	if (data.cairo == 0 && interpolation == SWT.DEFAULT) return;
	switch (interpolation) {
		case SWT.DEFAULT:
		case SWT.NONE:
		case SWT.LOW:
		case SWT.HIGH:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	initCairo();
	data.interpolation = interpolation;
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
	int mask = 0;
	float lineWidth = attributes.width;
	if (lineWidth != data.lineWidth) {
		mask |= LINE_WIDTH | DRAW_OFFSET;
	}
	int lineStyle = attributes.style;
	if (lineStyle != data.lineStyle) {
		mask |= LINE_STYLE;
		switch (lineStyle) {
			case SWT.LINE_SOLID:
			case SWT.LINE_DASH:
			case SWT.LINE_DOT:
			case SWT.LINE_DASHDOT:
			case SWT.LINE_DASHDOTDOT:
				break;
			case SWT.LINE_CUSTOM:
				if (attributes.dash == null) lineStyle = SWT.LINE_SOLID;
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	int join = attributes.join;
	if (join != data.lineJoin) {
		mask |= LINE_JOIN;
		switch (join) {
			case SWT.JOIN_MITER:
			case SWT.JOIN_ROUND:
			case SWT.JOIN_BEVEL:
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	int cap = attributes.cap;
	if (cap != data.lineCap) {
		mask |= LINE_CAP;
		switch (cap) {
			case SWT.CAP_FLAT:
			case SWT.CAP_ROUND:
			case SWT.CAP_SQUARE:
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	float[] dashes = attributes.dash;
	float[] lineDashes = data.lineDashes;
	if (dashes != null && dashes.length > 0) {
		boolean changed = lineDashes == null || lineDashes.length != dashes.length;
		for (int i = 0; i < dashes.length; i++) {
			float dash = dashes[i];
			if (dash <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			if (!changed && lineDashes[i] != dash) changed = true;
		}
		if (changed) {
			float[] newDashes = new float[dashes.length];
			System.arraycopy(dashes, 0, newDashes, 0, dashes.length);
			dashes = newDashes;
			mask |= LINE_STYLE;
		} else {
			dashes = lineDashes;
		}
	} else {
		if (lineDashes != null && lineDashes.length > 0) {
			mask |= LINE_STYLE;
		} else {
			dashes = lineDashes;
		}
	}
	float dashOffset = attributes.dashOffset;
	if (dashOffset != data.lineDashesOffset) {
		mask |= LINE_STYLE;		
	}
	float miterLimit = attributes.miterLimit;
	if (miterLimit != data.lineMiterLimit) {
		mask |= LINE_MITERLIMIT;		
	}
	initCairo();
	if (mask == 0) return;
	data.lineWidth = lineWidth;
	data.lineStyle = lineStyle;
	data.lineCap = cap;
	data.lineJoin = join;
	data.lineDashes = dashes;
	data.lineDashesOffset = dashOffset;
	data.lineMiterLimit = miterLimit;
	data.state &= ~mask;
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
	if (data.lineCap == cap) return;
	switch (cap) {
		case SWT.CAP_ROUND:
		case SWT.CAP_FLAT:
		case SWT.CAP_SQUARE:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineCap = cap;
	data.state &= ~LINE_CAP;
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
	float[] lineDashes = data.lineDashes;
	if (dashes != null && dashes.length > 0) {
		boolean changed = data.lineStyle != SWT.LINE_CUSTOM || lineDashes == null || lineDashes.length != dashes.length;
		for (int i = 0; i < dashes.length; i++) {
			int dash = dashes[i];
			if (dash <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			if (!changed && lineDashes[i] != dash) changed = true;
		}
		if (!changed) return;
		data.lineDashes = new float[dashes.length];
		for (int i = 0; i < dashes.length; i++) {
			data.lineDashes[i] = dashes[i];
		}
		data.lineStyle = SWT.LINE_CUSTOM;
	} else {
		if (data.lineStyle == SWT.LINE_SOLID && (lineDashes == null || lineDashes.length == 0)) return;
		data.lineDashes = null;
		data.lineStyle = SWT.LINE_SOLID;
	}
	data.state &= ~LINE_STYLE;
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
	if (data.lineJoin == join) return;
	switch (join) {
		case SWT.JOIN_MITER:
		case SWT.JOIN_ROUND:
		case SWT.JOIN_BEVEL:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineJoin = join;
	data.state &= ~LINE_JOIN;
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
	if (data.lineStyle == lineStyle) return;
	switch (lineStyle) {
		case SWT.LINE_SOLID:
		case SWT.LINE_DASH:
		case SWT.LINE_DOT:
		case SWT.LINE_DASHDOT:
		case SWT.LINE_DASHDOTDOT:
			break;
		case SWT.LINE_CUSTOM:
			if (data.lineDashes == null) lineStyle = SWT.LINE_SOLID;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineStyle = lineStyle;
	data.state &= ~LINE_STYLE;
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
	if (data.lineWidth == lineWidth) return;
	data.lineWidth = lineWidth;
	data.state &= ~(LINE_WIDTH | DRAW_OFFSET);
}
void setString(String string) {
	if (string == data.string) return;
	if (data.xmString != 0) OS.XmStringFree(data.xmString);
	byte[] buffer = Converter.wcsToMbcs(getCodePage (), string, true);
	data.xmString = OS.XmStringCreate(buffer, OS.XmFONTLIST_DEFAULT_TAG);
	data.string = string;
	data.stringWidth = data.stringHeight = -1;
}
void setText(String string, int flags) {
	if (data.renderTable == 0) createRenderTable();
	if (string == data.text && (flags & ~SWT.DRAW_TRANSPARENT) == (data.drawFlags & ~SWT.DRAW_TRANSPARENT)) {
		return;
	}
	if (data.xmText != 0) OS.XmStringFree(data.xmText);
	if (data.xmMnemonic != 0) OS.XmStringFree(data.xmMnemonic);
	char mnemonic = 0;
	int tableLength = 0;
	Device device = data.device;
	int[] parseTable = new int[2];
	char[] text = new char[string.length()];
	string.getChars(0, text.length, text, 0);	
	if ((flags & SWT.DRAW_DELIMITER) != 0) parseTable[tableLength++] = device.crMapping;
	if ((flags & SWT.DRAW_TAB) != 0) parseTable[tableLength++] = device.tabMapping;
	if ((flags & SWT.DRAW_MNEMONIC) != 0) mnemonic = fixMnemonic(text);
	String codePage = getCodePage();
	byte[] buffer = Converter.wcsToMbcs(codePage, text, true);
	data.xmText = OS.XmStringParseText(buffer, 0, OS.XmFONTLIST_DEFAULT_TAG, OS.XmCHARSET_TEXT, parseTable, tableLength, 0);
	if (mnemonic != 0) {
		byte [] buffer1 = Converter.wcsToMbcs(codePage, new char[]{mnemonic}, true);
		data.xmMnemonic = OS.XmStringCreate (buffer1, OS.XmFONTLIST_DEFAULT_TAG);
	} else {
		data.xmMnemonic = 0;
	}
	data.text = string;
	data.textWidth = data.textHeight = -1;
	data.drawFlags = flags;
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
	if (data.cairo == 0 && antialias == SWT.DEFAULT) return;
	int mode = 0;
	switch (antialias) {
		case SWT.DEFAULT: mode = Cairo.CAIRO_ANTIALIAS_DEFAULT; break;
		case SWT.OFF: mode = Cairo.CAIRO_ANTIALIAS_NONE; break;
		case SWT.ON: mode = Cairo.CAIRO_ANTIALIAS_GRAY;
            break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
    initCairo();
    int /*long*/ options = Cairo.cairo_font_options_create();
    Cairo.cairo_font_options_set_antialias(options, mode);
    Cairo.cairo_set_font_options(data.cairo, options);
    Cairo.cairo_font_options_destroy(options);
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
	if (data.cairo == 0 && transform == null) return;
	initCairo();
	int /*long*/ cairo = data.cairo;
	if (transform != null) {
		Cairo.cairo_set_matrix(cairo, transform.handle);
	} else {
		Cairo.cairo_identity_matrix(cairo);
	}
	data.state &= ~DRAW_OFFSET;
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
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		checkGC(FONT);
		byte[] buffer = Converter.wcsToMbcs(null, string, true);
		cairo_font_extents_t font_extents = new cairo_font_extents_t();
		Cairo.cairo_font_extents(cairo, font_extents);
		cairo_text_extents_t extents = new cairo_text_extents_t();
		Cairo.cairo_text_extents(cairo, buffer, extents);
		return new Point((int)extents.width, (int)font_extents.height);
	}
	setString(string);
	checkGC(FONT);
	if (data.stringWidth != -1) return new Point(data.stringWidth, data.stringHeight);
	int width, height;
	if (string.length() == 0) {
		width = 0;
		height = getFontHeight();
	} else {
		int fontList = data.font.handle;
		int xmString = data.xmString;
		width = OS.XmStringWidth(fontList, xmString);
		height = OS.XmStringHeight(fontList, xmString);
	}
	return new Point(data.stringWidth = width, data.stringHeight = height);
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
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		//TODO - honor flags
		checkGC(FONT);
		byte[] buffer = Converter.wcsToMbcs(null, string, true);
		cairo_font_extents_t font_extents = new cairo_font_extents_t();
		Cairo.cairo_font_extents(cairo, font_extents);
		cairo_text_extents_t extents = new cairo_text_extents_t();
		Cairo.cairo_text_extents(cairo, buffer, extents);
		return new Point((int)extents.width, (int)font_extents.height);
	}
	setText(string, flags);
	checkGC(FONT);
	if (data.textWidth != -1) return new Point(data.textWidth, data.textHeight);
	int width, height;
	if (string.length() == 0) {
		width = 0;
		height = getFontHeight();
	} else {
		int fontList = data.font.handle;
		int xmText = data.xmText;
		width = OS.XmStringWidth(fontList, xmText);
		height = OS.XmStringHeight(fontList, xmText);
	}
	return new Point(data.textWidth = width, data.textHeight = height);
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

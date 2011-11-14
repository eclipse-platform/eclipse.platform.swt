/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.internal.cairo.*;
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
	 * 
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public int /*long*/ handle;
	
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
	final static int DRAW_OFFSET = 1 << 9;	
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
	int /*long*/ gdkGC = drawable.internal_new_GC(data);
	Device device = data.device;
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = data.device = device;
	init(drawable, data, gdkGC);
	init();
}

static void addCairoString(int /*long*/ cairo, String string, float x, float y, Font font) {
	byte[] buffer = Converter.wcsToMbcs(null, string, true);
	if (OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
		int /*long*/ layout = OS.pango_cairo_create_layout(cairo);
		if (layout == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.pango_layout_set_text(layout, buffer, -1);
		OS.pango_layout_set_font_description(layout, font.handle);
		double[] currentX = new double[1], currentY = new double[1];
		Cairo.cairo_get_current_point(cairo, currentX, currentY);
		if (currentX[0] != x || currentY[0] != y) {
			Cairo.cairo_move_to(cairo, x, y);
		}
		OS.pango_cairo_layout_path(cairo, layout);
		OS.g_object_unref(layout);
	} else {
		GC.setCairoFont(cairo, font);
		cairo_font_extents_t extents = new cairo_font_extents_t();
		Cairo.cairo_font_extents(cairo, extents);
		double baseline = y + extents.ascent;
		Cairo.cairo_move_to(cairo, x, baseline);
		Cairo.cairo_text_path(cairo, buffer);
	}
}

static int checkStyle (int style) {
	if ((style & SWT.LEFT_TO_RIGHT) != 0) style &= ~SWT.RIGHT_TO_LEFT;
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

public static GC gtk_new(int /*long*/ handle, GCData data) {
	GC gc = new GC();
	gc.device = data.device;
	gc.init(null, data, handle);
	return gc;
}

public static GC gtk_new(Drawable drawable, GCData data) {
	GC gc = new GC();
	int /*long*/ gdkGC = drawable.internal_new_GC(data);
	gc.device = data.device;
	gc.init(drawable, data, gdkGC);
	return gc;
}

void checkGC (int mask) {
	int state = data.state;
	if ((state & mask) == mask) return;
	state = (state ^ mask) & mask;	
	data.state |= mask;
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		if ((state & (BACKGROUND | FOREGROUND)) != 0) {
			GdkColor color;
			Pattern pattern;
			if ((state & FOREGROUND) != 0) {
				color = data.foreground;
				pattern = data.foregroundPattern;
				data.state &= ~BACKGROUND;
			} else {
				color = data.background;
				pattern = data.backgroundPattern;
				data.state &= ~FOREGROUND;
			}
			if  (pattern != null) {
				if ((data.style & SWT.MIRRORED) != 0 && pattern.surface != 0) {
					int /*long*/ newPattern = Cairo.cairo_pattern_create_for_surface(pattern.surface);
					if (newPattern == 0) SWT.error(SWT.ERROR_NO_HANDLES);
					Cairo.cairo_pattern_set_extend(newPattern, Cairo.CAIRO_EXTEND_REPEAT);
					double[] matrix = {-1, 0, 0, 1, 0, 0};
					Cairo.cairo_pattern_set_matrix(newPattern, matrix);
					Cairo.cairo_set_source(cairo, newPattern);
					Cairo.cairo_pattern_destroy(newPattern);
				} else {
					Cairo.cairo_set_source(cairo, pattern.handle);
				}
			} else {
				Cairo.cairo_set_source_rgba(cairo, (color.red & 0xFFFF) / (float)0xFFFF, (color.green & 0xFFFF) / (float)0xFFFF, (color.blue & 0xFFFF) / (float)0xFFFF, data.alpha / (float)0xFF);
			}
		}
		if ((state & FONT) != 0) {
			if (data.layout != 0) {
				Font font = data.font;
				OS.pango_layout_set_font_description(data.layout, font.handle);
			}
			if (OS.GTK_VERSION < OS.VERSION(2, 8, 0)) {
				setCairoFont(cairo, data.font);
			}
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
	if ((state & (BACKGROUND | FOREGROUND)) != 0) {
		GdkColor foreground;
		if ((state & FOREGROUND) != 0) {
			foreground = data.foreground;
			data.state &= ~BACKGROUND;
		} else {
			foreground = data.background;
			data.state &= ~FOREGROUND;
		}
		OS.gdk_gc_set_foreground(handle, foreground);
	}
	if ((state & BACKGROUND_BG) != 0) {
		GdkColor background = data.background;
		OS.gdk_gc_set_background(handle, background);
	}
	if ((state & FONT) != 0) {
		if (data.layout != 0) {
			Font font = data.font;
			OS.pango_layout_set_font_description(data.layout, font.handle);
		}
	}
	if ((state & (LINE_CAP | LINE_JOIN | LINE_STYLE | LINE_WIDTH)) != 0) {
		int cap_style = 0;
		int join_style = 0;
		int width = (int)data.lineWidth;
		int line_style = 0;
		float[] dashes = null;
		switch (data.lineCap) {
			case SWT.CAP_ROUND: cap_style = OS.GDK_CAP_ROUND; break;
			case SWT.CAP_FLAT: cap_style = OS.GDK_CAP_BUTT; break;
			case SWT.CAP_SQUARE: cap_style = OS.GDK_CAP_PROJECTING; break;
		}
		switch (data.lineJoin) {
			case SWT.JOIN_ROUND: join_style = OS.GDK_JOIN_ROUND; break;
			case SWT.JOIN_MITER: join_style = OS.GDK_JOIN_MITER; break;
			case SWT.JOIN_BEVEL: join_style = OS.GDK_JOIN_BEVEL; break;
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
				OS.gdk_gc_set_dashes(handle, 0, dash_list, dash_list.length);
			}
			line_style = OS.GDK_LINE_ON_OFF_DASH;
		} else {
			line_style = OS.GDK_LINE_SOLID;
		}
		OS.gdk_gc_set_line_attributes(handle, width, line_style, cap_style, join_style);
	}
}

int /*long*/ convertRgn(int /*long*/ rgn, double[] matrix) {
	int /*long*/ newRgn = OS.gdk_region_new();
	int[] nRects = new int[1];
	int /*long*/[] rects = new int /*long*/[1];
	OS.gdk_region_get_rectangles(rgn, rects, nRects);
	GdkRectangle rect = new GdkRectangle();
	int[] pointArray = new int[8];
	double[] x = new double[1], y = new double[1];
	for (int i=0; i<nRects[0]; i++) {
		OS.memmove(rect, rects[0] + (i * GdkRectangle.sizeof), GdkRectangle.sizeof);
		x[0] = rect.x;
		y[0] = rect.y;
		Cairo.cairo_matrix_transform_point(matrix, x, y);
		pointArray[0] = (int)x[0];
		pointArray[1] = (int)y[0];
		x[0] = rect.x + rect.width;
		y[0] = rect.y;
		Cairo.cairo_matrix_transform_point(matrix, x, y);
		pointArray[2] = (int)Math.round(x[0]);
		pointArray[3] = (int)y[0];
		x[0] = rect.x + rect.width;
		y[0] = rect.y + rect.height;
		Cairo.cairo_matrix_transform_point(matrix, x, y);
		pointArray[4] = (int)Math.round(x[0]);
		pointArray[5] = (int)Math.round(y[0]);
		x[0] = rect.x;
		y[0] = rect.y + rect.height;
		Cairo.cairo_matrix_transform_point(matrix, x, y);
		pointArray[6] = (int)x[0];
		pointArray[7] = (int)Math.round(y[0]);
		int /*long*/ polyRgn = OS.gdk_region_polygon(pointArray, pointArray.length / 2, OS.GDK_EVEN_ODD_RULE);
		OS.gdk_region_union(newRgn, polyRgn);
		OS.gdk_region_destroy(polyRgn);
	}
	if (rects[0] != 0) OS.g_free(rects[0]);
	return newRgn;
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
	if (OS.USE_CAIRO) {
		int /*long*/ cairo = Cairo.cairo_create(image.surface);
		if (data.image != null) {
			Cairo.cairo_set_source_surface(cairo, data.image.surface, x, y);
		} else if (OS.GTK_VERSION >= OS.VERSION(2, 24, 0)) {
			OS.gdk_cairo_set_source_window(cairo, data.drawable, x, y);
		} else {
			int[] w = new int[1], h = new int[1];
			OS.gdk_drawable_get_size(data.drawable, w, h);
			int width = w[0], height = h[0];
			int /*long*/ xDisplay = OS.GDK_DISPLAY();
			int /*long*/ xDrawable = OS.gdk_x11_drawable_get_xid(data.drawable);
			int /*long*/ xVisual = OS.gdk_x11_visual_get_xvisual(OS.gdk_visual_get_system());
			int /*long*/ srcSurface = Cairo.cairo_xlib_surface_create(xDisplay, xDrawable, xVisual, width, height);
			Cairo.cairo_set_source_surface(cairo, srcSurface, x, y);
		}
		Cairo.cairo_paint(cairo);
		Cairo.cairo_destroy(cairo);
        return;
	}
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
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - srcX, deltaY = destY - srcY;
	if (deltaX == 0 && deltaY == 0) return;
	//TODO fix for USE_CAIRO
	int /*long*/ drawable = data.drawable;
	if (data.image == null && paint) OS.gdk_gc_set_exposures(handle, true);
	OS.gdk_draw_drawable(drawable, handle, drawable, srcX, srcY, destX, destY, width, height);
	if (data.image == null & paint) {
		OS.gdk_gc_set_exposures(handle, false);
		boolean disjoint = (destX + width < srcX) || (srcX + width < destX) || (destY + height < srcY) || (srcY + height < destY);
		GdkRectangle rect = new GdkRectangle ();
		if (disjoint) {
			rect.x = srcX;
			rect.y = srcY;
			rect.width = width;
			rect.height = height;
			OS.gdk_window_invalidate_rect (drawable, rect, false);
//			OS.gdk_window_clear_area_e(drawable, srcX, srcY, width, height);
		} else {
			if (deltaX != 0) {
				int newX = destX - deltaX;
				if (deltaX < 0) newX = destX + width;
				rect.x = newX;
				rect.y = srcY;
				rect.width = Math.abs(deltaX);
				rect.height = height;
				OS.gdk_window_invalidate_rect (drawable, rect, false);
//				OS.gdk_window_clear_area_e(drawable, newX, srcY, Math.abs(deltaX), height);
			}
			if (deltaY != 0) {
				int newY = destY - deltaY;
				if (deltaY < 0) newY = destY + height;
				rect.x = srcX;
				rect.y = newY;
				rect.width = width;
				rect.height = Math.abs(deltaY);
				OS.gdk_window_invalidate_rect (drawable, rect, false);
//				OS.gdk_window_clear_area_e(drawable, srcX, newY, width, Math.abs(deltaY));
			}
		}
	}
}

void createLayout() {
	int /*long*/ context = OS.gdk_pango_context_get();
	if (context == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	data.context = context;
	int /*long*/ layout = OS.pango_layout_new(context);
	if (layout == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	data.layout = layout;
	OS.pango_context_set_language(context, OS.gtk_get_default_language());
	OS.pango_context_set_base_dir(context, (data.style & SWT.MIRRORED) != 0 ? OS.PANGO_DIRECTION_RTL : OS.PANGO_DIRECTION_LTR);
	OS.gdk_pango_context_set_colormap(context, OS.gdk_colormap_get_system());	
	if (OS.GTK_VERSION >= OS.VERSION(2, 4, 0)) {
		OS.pango_layout_set_auto_dir(layout, false);
	}
}

void disposeLayout() {
	data.string = null;
	if (data.context != 0) OS.g_object_unref(data.context);
	if (data.layout != 0) OS.g_object_unref(data.layout);
	data.layout = data.context = 0;
}

void destroy() {
	if (data.disposeCairo) {
		int /*long*/ cairo = data.cairo;
		if (cairo != 0) Cairo.cairo_destroy(cairo);
	}
	data.cairo = 0;

	/* Free resources */
	int /*long*/ clipRgn = data.clipRgn;
	if (clipRgn != 0) OS.gdk_region_destroy(clipRgn);
	Image image = data.image;
	if (image != null) {
		image.memGC = null;
		if (image.transparentPixel != -1) image.createMask();
	}
	
	disposeLayout();

	/* Dispose the GC */
	if (drawable != null) {
		drawable.internal_dispose_GC(handle, data);
	}
	data.drawable = data.clipRgn = 0;
	drawable = null;
	handle = 0;
	data.image = null;
	data.string = null;
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
 * @see #drawRectangle(int, int, int, int)
 */
public void drawFocus(int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	/*
	* Feature in GTK.  The function gtk_widget_get_default_style() 
	* can't be used here because gtk_paint_focus() uses GCs, which
	* are not valid in the default style. The fix is to use a style
	* from a widget.
	*/
	int /*long*/ style = OS.gtk_widget_get_style(data.device.shellHandle);
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		checkGC(FOREGROUND);
		int[] lineWidth = new int[1];
		OS.gtk_widget_style_get(data.device.shellHandle, OS.focus_line_width, lineWidth, 0);
		Cairo.cairo_save(cairo);		
		Cairo.cairo_set_line_width(cairo, lineWidth[0]);
		double[] dashes = new double[]{1, 1};
		double dash_offset = -lineWidth[0] / 2f;
		while (dash_offset < 0) dash_offset += 2;
		Cairo.cairo_set_dash(cairo, dashes, dashes.length, dash_offset);
		Cairo.cairo_rectangle(cairo, x + lineWidth[0] / 2f, y + lineWidth[0] / 2f, width, height);
		Cairo.cairo_stroke(cairo);
		Cairo.cairo_restore(cairo);
		return;
	}
	OS.gtk_paint_focus(style, data.drawable, OS.GTK_STATE_NORMAL, null, data.device.shellHandle, new byte[1], x, y, width, height);
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
//	int[] width = new int[1];
//	int[] height = new int[1];
// 	OS.gdk_drawable_get_size(srcImage.pixmap, width, height);
 	int imgWidth = srcImage.width;
 	int imgHeight = srcImage.height;
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
			if ((data.style & SWT.MIRRORED) != 0) {
				Cairo.cairo_scale(cairo, -1f,  1);
				Cairo.cairo_translate(cairo, - 2 * destX - destWidth, 0);
			}
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
				* Bug in Cairo.  When drawing the image stretched with an interpolation
				* algorithm, the edges of the image are faded.  This is not a bug, but
				* it is not desired.  To avoid the faded edges, it should be possible to
				* use cairo_pattern_set_extend() to set the pattern extend to either
				* CAIRO_EXTEND_REFLECT or CAIRO_EXTEND_PAD, but these are not implemented
				* in some versions of cairo (1.2.x) and have bugs in others (in 1.4.2 it
				* draws with black edges).  The fix is to implement CAIRO_EXTEND_REFLECT
				* by creating an image that is 3 times bigger than the original, drawing
				* the original image in every quadrant (with an appropriate transform) and
				* use this image as the pattern.
				* 
				* NOTE: For some reason, it is necessary to use CAIRO_EXTEND_PAD with
				* the image that was created or the edges are still faded.
				* 
				* NOTE: Cairo.CAIRO_EXTEND_PAD works on Cairo 1.8.x and greater.
				*/
				int version = Cairo.cairo_version ();
				if (version >= Cairo.CAIRO_VERSION_ENCODE(1, 4, 0) && version < Cairo.CAIRO_VERSION_ENCODE(1, 8, 0)) {
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
				} else if (version >= Cairo.CAIRO_VERSION_ENCODE(1, 8, 0)) {
					Cairo.cairo_pattern_set_extend(pattern, Cairo.CAIRO_EXTEND_PAD);
				}
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
		if (device.useXRender) {
			drawImageXRender(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, 0, -1);
			return;
		}
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
	if (device.useXRender) {
		drawImageXRender(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, srcImage.mask, OS.PictStandardA8);
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
	/*
	* Feature in GTK.  gdk_draw_pixbuf was introduced in GTK+ 2.2.0 and
	* supports clipping.
	*/
	if (OS.GTK_VERSION >= OS.VERSION (2, 2, 0)) {
		OS.gdk_draw_pixbuf(data.drawable, handle, pixbuf, 0, 0, destX, destY, destWidth, destHeight, OS.GDK_RGB_DITHER_NORMAL, 0, 0);
	} else {
		OS.gdk_pixbuf_render_to_drawable_alpha(pixbuf, data.drawable, 0, 0, destX, destY, destWidth, destHeight, OS.GDK_PIXBUF_ALPHA_BILEVEL, 128, OS.GDK_RGB_DITHER_NORMAL, 0, 0);
	}
	OS.g_object_unref(pixbuf);
}
void drawImageMask(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight) {
	int /*long*/ drawable = data.drawable;
	int /*long*/ colorPixmap = srcImage.pixmap;
	/* Generate the mask if necessary. */
	if (srcImage.transparentPixel != -1) srcImage.createMask();
	int /*long*/ maskPixmap = srcImage.mask;

	if (device.useXRender) {
		drawImageXRender(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight, maskPixmap, OS.PictStandardA1);
	} else {
		if (srcWidth != destWidth || srcHeight != destHeight) {
			int /*long*/ pixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, true, 8, srcWidth, srcHeight);
			if (pixbuf != 0) {
				int /*long*/ colormap = OS.gdk_colormap_get_system();
				OS.gdk_pixbuf_get_from_drawable(pixbuf, colorPixmap, colormap, srcX, srcY, 0, 0, srcWidth, srcHeight);
				int /*long*/ maskPixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, false, 8, srcWidth, srcHeight);
				if (maskPixbuf != 0) {
					OS.gdk_pixbuf_get_from_drawable(maskPixbuf, maskPixmap, 0, srcX, srcY, 0, 0, srcWidth, srcHeight);
					int stride = OS.gdk_pixbuf_get_rowstride(pixbuf);
					int /*long*/ pixels = OS.gdk_pixbuf_get_pixels(pixbuf);
					byte[] line = new byte[stride];
					int maskStride = OS.gdk_pixbuf_get_rowstride(maskPixbuf);
					int /*long*/ maskPixels = OS.gdk_pixbuf_get_pixels(maskPixbuf);
					byte[] maskLine = new byte[maskStride];
					for (int y=0; y<srcHeight; y++) {
						int /*long*/ offset = pixels + (y * stride);
						OS.memmove(line, offset, stride);
						int /*long*/ maskOffset = maskPixels + (y * maskStride);
						OS.memmove(maskLine, maskOffset, maskStride);
						for (int x=0; x<srcWidth; x++) {
							if (maskLine[x * 3] == 0) {
								line[x*4+3] = 0;
							}
						}
						OS.memmove(offset, line, stride);
					}
					OS.g_object_unref(maskPixbuf);
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
	}

	/* Destroy scaled pixmaps */
	if (colorPixmap != 0 && srcImage.pixmap != colorPixmap) OS.g_object_unref(colorPixmap);
	if (maskPixmap != 0 && srcImage.mask != maskPixmap) OS.g_object_unref(maskPixmap);
	/* Destroy the image mask if the there is a GC created on the image */
	if (srcImage.transparentPixel != -1 && srcImage.memGC != null) srcImage.destroyMask();
}
void drawImageXRender(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight, int /*long*/ maskPixmap, int maskType) {
	int translateX = 0, translateY = 0;
	int /*long*/ drawable = data.drawable;
	if (data.image == null && !data.realDrawable) {
		int[] x = new int[1], y = new int[1];
		int /*long*/ [] real_drawable = new int /*long*/ [1];
		OS.gdk_window_get_internal_paint_info(drawable, real_drawable, x, y);
		drawable = real_drawable[0];
		translateX = -x[0];
		translateY = -y[0];
	}
	int /*long*/ xDisplay = OS.GDK_DISPLAY();
	int /*long*/ maskPict = 0;
	if (maskPixmap != 0) {
		int attribCount = 0;
		XRenderPictureAttributes attrib = null;
		if (srcImage.alpha != -1) {
			attribCount = 1;
			attrib = new XRenderPictureAttributes();
			attrib.repeat = true;
		}
		maskPict = OS.XRenderCreatePicture(xDisplay, OS.gdk_x11_drawable_get_xid(maskPixmap), OS.XRenderFindStandardFormat(xDisplay, maskType), attribCount, attrib);
		if (maskPict == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	}
	int /*long*/ format = OS.XRenderFindVisualFormat(xDisplay, OS.gdk_x11_visual_get_xvisual(OS.gdk_visual_get_system()));
	int /*long*/ destPict = OS.XRenderCreatePicture(xDisplay, OS.gdk_x11_drawable_get_xid(drawable), format, 0, null);
	if (destPict == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int /*long*/ srcPict = OS.XRenderCreatePicture(xDisplay, OS.gdk_x11_drawable_get_xid(srcImage.pixmap), format, 0, null);
	if (srcPict == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (srcWidth != destWidth || srcHeight != destHeight) {
		int[] transform = new int[]{(int)(((float)srcWidth / destWidth) * 65536), 0, 0, 0, (int)(((float)srcHeight / destHeight) * 65536), 0, 0, 0, 65536};
		OS.XRenderSetPictureTransform(xDisplay, srcPict, transform);
		if (maskPict != 0) OS.XRenderSetPictureTransform(xDisplay, maskPict, transform);
		srcX *= destWidth / (float)srcWidth;
		srcY *= destHeight / (float)srcHeight;
	}
	int /*long*/ clipping = data.clipRgn;
	if (data.damageRgn != 0) {
		if (clipping == 0) {
			clipping = data.damageRgn;
		} else {
			clipping = OS.gdk_region_new();
			OS.gdk_region_union(clipping, data.clipRgn);
			OS.gdk_region_intersect(clipping, data.damageRgn);
		}
	}
	if (clipping != 0) {
		int[] nRects = new int[1];
		int /*long*/[] rects = new int /*long*/[1];
		OS.gdk_region_get_rectangles(clipping, rects, nRects);
		GdkRectangle rect = new GdkRectangle();
		short[] xRects = new short[nRects[0] * 4];
		for (int i=0, j=0; i<nRects[0]; i++, j+=4) {
			OS.memmove(rect, rects[0] + (i * GdkRectangle.sizeof), GdkRectangle.sizeof);
			xRects[j] = (short)(translateX + rect.x);
			xRects[j+1] = (short)(translateY + rect.y);
			xRects[j+2] = (short)rect.width;
			xRects[j+3] = (short)rect.height;
		}
		OS.XRenderSetPictureClipRectangles(xDisplay, destPict, 0, 0, xRects, nRects[0]);
		if (clipping != data.clipRgn && clipping != data.damageRgn) {
			OS.gdk_region_destroy(clipping);
		}
		if (rects[0] != 0) OS.g_free(rects[0]);
	}
	OS.XRenderComposite(xDisplay, maskPict != 0 ? OS.PictOpOver : OS.PictOpSrc, srcPict, maskPict, destPict, srcX, srcY, srcX, srcY, destX + translateX, destY + translateY, destWidth, destHeight);
	OS.XRenderFreePicture(xDisplay, destPict);
	OS.XRenderFreePicture(xDisplay, srcPict);
	if (maskPict != 0) OS.XRenderFreePicture(xDisplay, maskPict);
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
	checkGC(DRAW);
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		double xOffset = data.cairoXoffset, yOffset = data.cairoYoffset;
		Cairo.cairo_move_to(cairo, x1 + xOffset, y1 + yOffset);
		Cairo.cairo_line_to(cairo, x2 + xOffset, y2 + yOffset);
		Cairo.cairo_stroke(cairo);
		return;
	}
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
	OS.gdk_draw_arc(data.drawable, handle, 0, x, y, width, height, 0, 23040);
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
	checkGC(DRAW);
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		drawPolyline(cairo, pointArray, true);
		Cairo.cairo_stroke(cairo);
		return;
	}
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
	checkGC(DRAW);
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		drawPolyline(cairo, pointArray, false);
		Cairo.cairo_stroke(cairo);
		return;
	}
	OS.gdk_draw_lines(data.drawable, handle, pointArray, pointArray.length / 2);
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
public void drawRectangle(int x, int y, int width, int height) {
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
		ny = ny -nh;
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
		if (OS.GTK_VERSION < OS.VERSION(2, 8, 0)) {
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
	}
	setString(string, flags);
	if (cairo != 0) {
		checkGC(FONT);
		if ((flags & SWT.DRAW_TRANSPARENT) == 0) {
			checkGC(BACKGROUND);
			if (data.stringWidth == -1) {
				computeStringSize();
			}
			Cairo.cairo_rectangle(cairo, x, y, data.stringWidth, data.stringHeight);
			Cairo.cairo_fill(cairo);
		}
		checkGC(FOREGROUND);
		if ((data.style & SWT.MIRRORED) != 0) {
			Cairo.cairo_save(cairo);
			if (data.stringWidth == -1) {
				computeStringSize();
			}
			Cairo.cairo_scale(cairo, -1f,  1);
			Cairo.cairo_translate(cairo, -2 * x - data.stringWidth, 0);
		}
		Cairo.cairo_move_to(cairo, x, y);
		OS.pango_cairo_show_layout(cairo, data.layout);
		if ((data.style & SWT.MIRRORED) != 0) {
			Cairo.cairo_restore(cairo);
		}
		Cairo.cairo_new_path(cairo);
		return;
	}
	checkGC(FOREGROUND | FONT | BACKGROUND_BG);
	GdkColor background = null;
	if ((flags & SWT.DRAW_TRANSPARENT) == 0) background = data.background;
	if (!data.xorMode) {
		OS.gdk_draw_layout_with_colors(data.drawable, handle, x, y, data.layout, null, background);
	} else {
		int /*long*/ layout = data.layout;
		if (data.stringWidth == -1) {
			computeStringSize();
		}
		int /*long*/ pixmap = OS.gdk_pixmap_new(OS.GDK_ROOT_PARENT(), data.stringWidth, data.stringHeight, -1);
		if (pixmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int /*long*/ gdkGC = OS.gdk_gc_new(pixmap);
		if (gdkGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		GdkColor black = new GdkColor();
		OS.gdk_gc_set_foreground(gdkGC, black);
		OS.gdk_draw_rectangle(pixmap, gdkGC, 1, 0, 0, data.stringWidth, data.stringHeight);
		OS.gdk_gc_set_foreground(gdkGC, data.foreground);
		OS.gdk_draw_layout_with_colors(pixmap, gdkGC, 0, 0, layout, null, background);
		OS.g_object_unref(gdkGC);
		OS.gdk_draw_drawable(data.drawable, handle, pixmap, 0, 0, x, y, data.stringWidth, data.stringHeight);
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
	OS.gdk_draw_arc(data.drawable, handle, 1, x, y, width, height, startAngle * 64, arcAngle * 64);
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
	OS.gdk_draw_arc(data.drawable, handle, 1, x, y, width, height, 0, 23040);
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
	OS.gdk_draw_polygon(data.drawable, handle, 1, pointArray, pointArray.length / 2);
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
public void fillRectangle(int x, int y, int width, int height) {
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
	OS.gdk_draw_rectangle(data.drawable, handle, 1, x, y, width, height);
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
	return Color.gtk_new(data.device, data.background);
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
	/* Calculate visible bounds in device space */
	int x = 0, y = 0, width = 0, height = 0;
	int[] w = new int[1], h = new int[1];
	getSize(w, h);
	width = w[0];
	height = h[0];
	/* Intersect visible bounds with clipping in device space and then convert then to user space */
	int /*long*/ cairo = data.cairo;
	int /*long*/ clipRgn = data.clipRgn;
	int /*long*/ damageRgn = data.damageRgn;
	if (clipRgn != 0 || damageRgn != 0 || cairo != 0) {
		int /*long*/ rgn = OS.gdk_region_new();
		GdkRectangle rect = new GdkRectangle();
		rect.width = width;
		rect.height = height;
		OS.gdk_region_union_with_rect(rgn, rect);
		if (damageRgn != 0) {
			OS.gdk_region_intersect (rgn, damageRgn);
		}
		/* Intersect visible bounds with clipping */
		if (clipRgn != 0) {
			/* Convert clipping to device space if needed */
			if (data.clippingTransform != null) {
				clipRgn = convertRgn(clipRgn, data.clippingTransform);
				OS.gdk_region_intersect(rgn, clipRgn);
				OS.gdk_region_destroy(clipRgn);
			} else {
				OS.gdk_region_intersect(rgn, clipRgn);
			}
		}
		/* Convert to user space */
		if (cairo != 0) {
			double[] matrix = new double[6];
			Cairo.cairo_get_matrix(cairo, matrix);
			Cairo.cairo_matrix_invert(matrix);
			clipRgn = convertRgn(rgn, matrix);
			OS.gdk_region_destroy(rgn);
			rgn = clipRgn;
		}
		OS.gdk_region_get_clipbox(rgn, rect);
		OS.gdk_region_destroy(rgn);
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
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int /*long*/ clipping = region.handle;
	OS.gdk_region_subtract(clipping, clipping);
	int /*long*/ cairo = data.cairo;
	int /*long*/ clipRgn = data.clipRgn;
	if (clipRgn == 0) {
		GdkRectangle rect = new GdkRectangle();
		int[] width = new int[1], height = new int[1];
		getSize(width, height);
		rect.width = width[0];
		rect.height = height[0];
		OS.gdk_region_union_with_rect(clipping, rect);
	} else {
		/* Convert clipping to device space if needed */
		if (data.clippingTransform != null) {
			int /*long*/ rgn = convertRgn(clipRgn, data.clippingTransform);
			OS.gdk_region_union(clipping, rgn);
			OS.gdk_region_destroy(rgn);
		} else {
			OS.gdk_region_union(clipping, clipRgn);
		}
	}
	if (data.damageRgn != 0) {
		OS.gdk_region_intersect(clipping, data.damageRgn);
	}
	/* Convert to user space */
	if (cairo != 0) {
		double[] matrix = new double[6];
		Cairo.cairo_get_matrix(cairo, matrix);
		Cairo.cairo_matrix_invert(matrix);
		int /*long*/ rgn = convertRgn(clipping, matrix);
		OS.gdk_region_subtract(clipping, clipping);
		OS.gdk_region_union(clipping, rgn);
		OS.gdk_region_destroy(rgn);
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
	int /*long*/ cairo = data.cairo;
	if (cairo == 0) return SWT.FILL_EVEN_ODD;
	return Cairo.cairo_get_fill_rule(cairo) == Cairo.CAIRO_FILL_RULE_WINDING ? SWT.FILL_WINDING : SWT.FILL_EVEN_ODD;
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
	if (data.context == 0) createLayout();
	checkGC(FONT);
	Font font = data.font;
	int /*long*/ context = data.context;
	int /*long*/ lang = OS.pango_context_get_language(context);
	int /*long*/ metrics = OS.pango_context_get_metrics(context, font.handle, lang);
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
	return Color.gtk_new(data.device, data.foreground);
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
 * @noreference This method is not intended to be referenced by clients.
 * 
 * @since 3.2
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

void getSize(int[] width, int[] height) {
	if (data.width != -1 && data.height != -1) {
		width[0] = data.width;
		height[0] = data.height;
		return;
	}
	if (OS.USE_CAIRO) {
		int /*long*/ surface = Cairo.cairo_get_target(handle);
		switch (Cairo.cairo_surface_get_type(surface)) {
			case Cairo.CAIRO_SURFACE_TYPE_IMAGE:
				width[0] = Cairo.cairo_image_surface_get_width(surface);
				height[0] = Cairo.cairo_image_surface_get_height(surface);
				break;
			case Cairo.CAIRO_SURFACE_TYPE_XLIB: 
				width[0] = Cairo.cairo_xlib_surface_get_width(surface);
				height[0] = Cairo.cairo_xlib_surface_get_height(surface);
				break;
		}
	} else {
		OS.gdk_drawable_get_size(data.drawable, width, height);
	}
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
    int antialias = Cairo.CAIRO_ANTIALIAS_DEFAULT;
    if (OS.GTK_VERSION < OS.VERSION(2, 8, 0)) {
    	int /*long*/ options = Cairo.cairo_font_options_create();
    	Cairo.cairo_get_font_options(data.cairo, options);
    	antialias = Cairo.cairo_font_options_get_antialias(options);
    	Cairo.cairo_font_options_destroy(options);
    } else {
    	if (data.context != 0) {
    		int /*long*/ options = OS.pango_cairo_context_get_font_options(data.context);
    		if (options != 0) antialias = Cairo.cairo_font_options_get_antialias(options);
    	}
    }
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
		double[] identity = identity();
		Cairo.cairo_matrix_invert(identity);
		Cairo.cairo_matrix_multiply(transform.handle, transform.handle, identity);
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
public int hashCode() {
	return (int)/*64*/handle;
}

double[] identity() {
	double[] identity = new double[6];
	if ((data.style & SWT.MIRRORED) != 0) {
		int[] w = new int[1], h = new int[1];
		getSize(w, h);
		Cairo.cairo_matrix_init(identity, -1, 0, 0, 1, w[0], 0);
	} else {
		Cairo.cairo_matrix_init_identity(identity);
	}
	return identity;
}

void init(Drawable drawable, GCData data, int /*long*/ gdkGC) {
	if (data.foreground != null) data.state &= ~FOREGROUND;
	if (data.background != null) data.state &= ~(BACKGROUND | BACKGROUND_BG);
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
	handle = gdkGC;
	if (OS.USE_CAIRO) {
		int /*long*/ cairo = data.cairo = handle;
		Cairo.cairo_set_fill_rule(cairo, Cairo.CAIRO_FILL_RULE_EVEN_ODD);
		data.state &= ~(BACKGROUND | FOREGROUND | FONT | LINE_WIDTH | LINE_CAP | LINE_JOIN | LINE_STYLE | DRAW_OFFSET);
	}
	setClipping(data.clipRgn);
	if ((data.style & SWT.MIRRORED) != 0) {
	  initCairo();
	  int /*long*/ cairo = data.cairo;
	  Cairo.cairo_set_matrix(cairo, identity());
	}
}

void initCairo() {
	data.device.checkCairo();
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) return;
	if (OS.GTK_VERSION < OS.VERSION(2, 17, 0)) {
		int /*long*/ xDisplay = OS.GDK_DISPLAY();
		int /*long*/ xVisual = OS.gdk_x11_visual_get_xvisual(OS.gdk_visual_get_system());
		int /*long*/ xDrawable = 0;
		int translateX = 0, translateY = 0;
		int /*long*/ drawable = data.drawable;
		if (data.image != null) {
			xDrawable = OS.GDK_PIXMAP_XID(drawable);
		} else {
			if (!data.realDrawable) {
				int[] x = new int[1], y = new int[1];
				int /*long*/ [] real_drawable = new int /*long*/ [1];
				OS.gdk_window_get_internal_paint_info(drawable, real_drawable, x, y);
				xDrawable = OS.gdk_x11_drawable_get_xid(real_drawable[0]);
				translateX = -x[0];
				translateY = -y[0];
			}
		}
		int[] w = new int[1], h = new int[1];
		OS.gdk_drawable_get_size(drawable, w, h);
		int width = w[0], height = h[0];
		int /*long*/ surface = Cairo.cairo_xlib_surface_create(xDisplay, xDrawable, xVisual, width, height);
		if (surface == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		Cairo.cairo_surface_set_device_offset(surface, translateX, translateY);
		data.cairo = cairo = Cairo.cairo_create(surface);
		Cairo.cairo_surface_destroy(surface);
	} else {
		data.cairo = cairo = OS.gdk_cairo_create(data.drawable);
	}
	if (cairo == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	data.disposeCairo = true;
	Cairo.cairo_set_fill_rule(cairo, Cairo.CAIRO_FILL_RULE_EVEN_ODD);
	data.state &= ~(BACKGROUND | FOREGROUND | FONT | LINE_WIDTH | LINE_CAP | LINE_JOIN | LINE_STYLE | DRAW_OFFSET);
	setCairoClip(data.damageRgn, data.clipRgn);
}

void computeStringSize() {
	int[] width = new int[1], height = new int[1];
	OS.pango_layout_get_size(data.layout, width, height);
	data.stringHeight = OS.PANGO_PIXELS(height[0]);
	data.stringWidth = OS.PANGO_PIXELS(width[0]);
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
 * invoke any other method (except {@link #dispose()}) using the GC.
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
	if ((data.style & SWT.MIRRORED) != 0 || OS.USE_CAIRO) {
		if (!advanced) {
			setAlpha(0xFF);
			setAntialias(SWT.DEFAULT);
			setBackgroundPattern(null);
			setClipping(0);
			setForegroundPattern(null);
			setInterpolation(SWT.DEFAULT);
			setTextAntialias(SWT.DEFAULT);
			setTransform(null);
		}
		return;
	}
	if (advanced && data.cairo != 0) return;
	if (advanced) {
		try {
			initCairo();
		} catch (SWTException e) {}
	} else {
		if (!data.disposeCairo) return;
		int /*long*/ cairo = data.cairo;
		if (cairo != 0) Cairo.cairo_destroy(cairo);
		data.cairo = 0;
		data.interpolation = SWT.DEFAULT;
		data.alpha = 0xFF;
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
	data.backgroundPattern = null;
	data.state &= ~(BACKGROUND | BACKGROUND_BG);
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
	setCairoFont(cairo, font.handle);
}

static void setCairoFont(int /*long*/ cairo, int /*long*/ font) {
	int /*long*/ family = OS.pango_font_description_get_family(font);
	int length = OS.strlen(family);
	byte[] buffer = new byte[length + 1];
	OS.memmove(buffer, family, length);
	//TODO - convert font height from pango to cairo
	double height = OS.PANGO_PIXELS(OS.pango_font_description_get_size(font)) * 96 / 72;
	int pangoStyle = OS.pango_font_description_get_style(font);
	int pangoWeight = OS.pango_font_description_get_weight(font);
	int slant = Cairo.CAIRO_FONT_SLANT_NORMAL;
	if (pangoStyle == OS.PANGO_STYLE_ITALIC) slant = Cairo.CAIRO_FONT_SLANT_ITALIC;
	if (pangoStyle == OS.PANGO_STYLE_OBLIQUE) slant = Cairo.CAIRO_FONT_SLANT_OBLIQUE;
	int weight = Cairo.CAIRO_FONT_WEIGHT_NORMAL;
	if (pangoWeight == OS.PANGO_WEIGHT_BOLD) weight = Cairo.CAIRO_FONT_WEIGHT_BOLD;
	Cairo.cairo_select_font_face(cairo, buffer, slant, weight);
	Cairo.cairo_set_font_size(cairo, height);
}

static void setCairoRegion(int /*long*/ cairo, int /*long*/ rgn) {
	if (OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
		OS.gdk_cairo_region(cairo, rgn);
	} else {
		int[] nRects = new int[1];
		int /*long*/[] rects = new int /*long*/[1];
		OS.gdk_region_get_rectangles(rgn, rects, nRects);
		GdkRectangle rect = new GdkRectangle();
		for (int i=0; i<nRects[0]; i++) {
			OS.memmove(rect, rects[0] + (i * GdkRectangle.sizeof), GdkRectangle.sizeof);
			Cairo.cairo_rectangle(cairo, rect.x, rect.y, rect.width, rect.height);
		}
		if (rects[0] != 0) OS.g_free(rects[0]);
	}
}

static void setCairoPatternColor(int /*long*/ pattern, int offset, Color c, int alpha) {
	GdkColor color = c.handle;
	double aa = (alpha & 0xFF) / (double)0xFF;
	double red = ((color.red & 0xFFFF) / (double)0xFFFF);
	double green = ((color.green & 0xFFFF) / (double)0xFFFF);
	double blue = ((color.blue & 0xFFFF) / (double)0xFFFF);
	Cairo.cairo_pattern_add_color_stop_rgba(pattern, offset, red, green, blue, aa);
}

void setCairoClip(int /*long*/ damageRgn, int /*long*/ clipRgn) {
	int /*long*/ cairo = data.cairo;
	if (OS.GTK_VERSION >= OS.VERSION(2,18,0) && data.drawable != 0) {
		OS.gdk_cairo_reset_clip(cairo, data.drawable);
	} else {
		Cairo.cairo_reset_clip(cairo);
	}
	if (damageRgn != 0) {
		double[] matrix = new double[6];
		Cairo.cairo_get_matrix(cairo, matrix);
		double[] identity = new double[6];
		Cairo.cairo_matrix_init_identity(identity);
		Cairo.cairo_set_matrix(cairo, identity);
		setCairoRegion(cairo, damageRgn);
		Cairo.cairo_clip(cairo);
		Cairo.cairo_set_matrix(cairo, matrix);
	}
	if (clipRgn != 0) {
		setCairoRegion(cairo, clipRgn);
		Cairo.cairo_clip(cairo);
	}
}

void setClipping(int /*long*/ clipRgn) {
	int /*long*/ cairo = data.cairo;
	if (clipRgn == 0) {
		if (data.clipRgn != 0) {
			OS.gdk_region_destroy(data.clipRgn);
			data.clipRgn = 0;
		}
		if (cairo != 0) {
			data.clippingTransform = null;
			setCairoClip(data.damageRgn, 0);
		} else {
			int /*long*/ clipping = data.damageRgn != 0 ? data.damageRgn : 0;
			OS.gdk_gc_set_clip_region(handle, clipping);
		}
	} else {
		if (data.clipRgn == 0) data.clipRgn = OS.gdk_region_new();
		OS.gdk_region_subtract(data.clipRgn, data.clipRgn);
		OS.gdk_region_union(data.clipRgn, clipRgn);
		if (cairo != 0) {
			if (data.clippingTransform == null) data.clippingTransform = new double[6];
			Cairo.cairo_get_matrix(cairo, data.clippingTransform);
			setCairoClip(data.damageRgn, clipRgn);
		} else {
			int /*long*/ clipping = clipRgn;
			if (data.damageRgn != 0) {
				clipping = OS.gdk_region_new();
				OS.gdk_region_union(clipping, clipRgn);
				OS.gdk_region_intersect(clipping, data.damageRgn);
			}
			OS.gdk_gc_set_clip_region(handle, clipping);
			if (clipping != clipRgn) OS.gdk_region_destroy(clipping);
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
	GdkRectangle rect = new GdkRectangle();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	int /*long*/ clipRgn = OS.gdk_region_new();
	OS.gdk_region_union_with_rect(clipRgn, rect);
	setClipping(clipRgn);
	OS.gdk_region_destroy(clipRgn);
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
public void setClipping(Region region) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region != null && region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	setClipping(region != null ? region.handle : 0);
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
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.font = font != null ? font : data.device.systemFont;
	data.state &= ~FONT;
	data.stringWidth = data.stringHeight = -1;
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
	int cairo_mode = Cairo.CAIRO_FILL_RULE_EVEN_ODD;
	switch (rule) {
		case SWT.FILL_WINDING:
			cairo_mode = Cairo.CAIRO_FILL_RULE_WINDING; break;
		case SWT.FILL_EVEN_ODD:
			cairo_mode = Cairo.CAIRO_FILL_RULE_EVEN_ODD; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	//TODO - need fill rule in X, GDK has no API
	initCairo();
	int /*long*/ cairo = data.cairo;
	if (cairo != 0) {
		Cairo.cairo_set_fill_rule(cairo, cairo_mode);
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
public void setForeground(Color color) {	
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.foreground = color.handle;
	data.foregroundPattern = null;
	data.state &= ~FOREGROUND;
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

void setString(String string, int flags) {
	if (data.layout == 0) createLayout();
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
    if (OS.GTK_VERSION < OS.VERSION(2, 8, 0)) {
    	Cairo.cairo_set_font_options(data.cairo, options);
    } else {
    	if (data.context == 0) createLayout();
    	OS.pango_cairo_context_set_font_options(data.context, options);
    }
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
	double[] identity = identity();
	if (transform != null) {
		Cairo.cairo_matrix_multiply(identity, transform.handle, identity);
	} 
	Cairo.cairo_set_matrix(cairo, identity);
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
		if (OS.GTK_VERSION < OS.VERSION(2, 8, 0)) {
			//TODO - honor flags
			checkGC(FONT);
			byte[] buffer = Converter.wcsToMbcs(null, string, true);
			cairo_font_extents_t font_extents = new cairo_font_extents_t();
			Cairo.cairo_font_extents(cairo, font_extents);
			cairo_text_extents_t extents = new cairo_text_extents_t();
			Cairo.cairo_text_extents(cairo, buffer, extents);
			return new Point((int)extents.width, (int)font_extents.height);
		}
	}
	setString(string, flags);
	checkGC(FONT);
	if (data.stringWidth == -1) {
		computeStringSize();
	}
	return new Point(data.stringWidth, data.stringHeight);
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

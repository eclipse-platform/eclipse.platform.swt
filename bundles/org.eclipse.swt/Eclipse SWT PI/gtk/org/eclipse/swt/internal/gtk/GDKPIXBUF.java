package org.eclipse.swt.internal.gtk;

/*
 * Copyright (c) IBM Corp. 2000, 2001.  All rights reserved.
 *
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 */

import org.eclipse.swt.internal.Library;

public class GDKPIXBUF extends OS {

	/* GdkColorspace enumeration */
	/* R/G/B additive color space */
	public static final native int GDK_COLORSPACE_RGB();
	
	/* GIF-like animation overlay modes for frames */
	public final static int GDK_PIXBUF_FRAME_RETAIN = 0;
	public final static int GDK_PIXBUF_FRAME_DISPOSE = 1;
	public final static int GDK_PIXBUF_FRAME_REVERT = 2;
	
	/* Alpha compositing mode */
	public final static int GDK_PIXBUF_ALPHA_BILEVEL = 0;
	public final static int GDK_PIXBUF_ALPHA_FULL = 1;

	/* Interpolation modes */
	public final static int GDK_INTERP_NEAREST = 0;
	public final static int GDK_INTERP_TILES = 1;
	public final static int GDK_INTERP_BILINEAR = 2;
	public final static int GDK_INTERP_HYPER = 3;


/*
 * NATIVES
 */

/* GdkPixbuf accessors */

/**
 * Returns the colorspace of the pixbuf argument
 */
public static final native int gdk_pixbuf_get_colorspace (int pixbuf);

/**
 * Returns the number of channels in the pixbuf argument
 */
public static final native int gdk_pixbuf_get_n_channels (int pixbuf);

/**
 * Returns true if the pixbuf specified by the argument has an alpha channel
 * (opacity information), and false otherwise.
 */
public static final native boolean gdk_pixbuf_get_has_alpha (int pixbuf);

/**
 * Returns the number of bits per pixel in each channel.
 * Normally 8.
 */
public static final native int gdk_pixbuf_get_bits_per_sample (int pixbuf);

/**
 * Returns the address of the actual image data in the OS memory.
 */
public static final native int gdk_pixbuf_get_pixels (int pixbuf);

/**
 * Returns the width of the pixbuf specified by the argument.
 */
public static final native int gdk_pixbuf_get_width (int pixbuf);

/**
 * Returns the height of the pixbuf specified by the argument.
 */
public static final native int gdk_pixbuf_get_height (int pixbuf);

/**
 * Returns the rowstride of the pixbuf specified by the argument.
 */
public static final native int gdk_pixbuf_get_rowstride (int pixbuf);


/* PIXBUF CREATION FROM DATA IN MEMORY */

/**
 * Create a blank pixbuf with an optimal rowstride and a new buffer
 */
public static final native int gdk_pixbuf_new (
				int colorspace,
				boolean has_alpha,
				int bits_per_sample,
				int width,
				int height);

public static final native int gdk_pixbuf_copy(int source);

public static final native int gdk_pixbuf_new_from_data (
				byte[] data,
				int colorspace,
				boolean has_alpha,
				int bits_per_sample,
				int width,
				int height,
				int rowstride,
				int destroy_fn,
				int destroy_fn_data);

public static final native int gdk_pixbuf_new_from_xpm_data (int pdata);


/* PIXBUF CREATION - FILE LOADING */

public static final native int gdk_pixbuf_new_from_file (byte[] filename);



/* RENDERING TO A DRAWABLE */


public static final native void gdk_pixbuf_render_to_drawable_alpha (int pixbuf,
				int drawable,
				int src_x, int src_y,
				int dest_x, int dest_y,
				int width, int height,
				int alpha_mode,
				int alpha_threshold,
				int dither,
				int x_dither, int y_dither);

public static final native void gdk_pixbuf_render_to_drawable (int pixbuf,
				int drawable,
				int gc,
				int src_x, int src_y,
				int dest_x, int dest_y,
				int width, int height,
				int dither,
				int x_dither, int y_dither);

/* SCALING */

public static final native void gdk_pixbuf_scale (
				int src, int dest,
				int dest_x,
                int dest_y,
                int dest_width,
                int dest_height,
                double offset_x,
                double offset_y,
                double scale_x,
                double scale_y,
                int interp_type);

public static final native void gdk_pixbuf_composite (
				int src, int dest,
				int dest_x,
				int dest_y,
				int dest_width,
				int dest_height,
				double offset_x,
				double offset_y,
				double scale_x,
				double scale_y,
				int interp_type,
				int overall_alpha);

public static final native void gdk_pixbuf_composite_color (
				int src,
				int dest,
				int dest_x,
				int dest_y, 
				int dest_width,
				int dest_height,
				double offset_x,
				double offset_y,
				double scale_x,double scale_y,
				int interp_type,
				int overall_alpha,
				int check_x,
				int check_y,
				int check_size,
				int color1,
				int color2);

public static final native int gdk_pixbuf_scale_simple (
				int src,
				int dest_width,
				int dest_height,
				int interp_type);

public static final native int gdk_pixbuf_composite_color_simple (
				int src,int dest_width,
				int dest_height,
				int interp_type,
				int overall_alpha,
				int check_size,
				int color1,
				int color2);



public static final native int gdk_pixbuf_get_from_drawable (
				int dest,
				int src,
				int cmap,
				int src_x,
				int src_y,
				int dest_x,
				int dest_y,
				int width,
				int height);

}

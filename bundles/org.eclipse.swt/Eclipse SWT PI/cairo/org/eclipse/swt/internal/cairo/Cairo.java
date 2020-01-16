/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Cairo and SWT
 * -  Copyright (C) 2005, 2018 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.cairo;

import org.eclipse.swt.internal.*;

public class Cairo extends Platform {
	static {
		Library.loadLibrary("swt-cairo");
	}

	/** Constants */
	public static final int CAIRO_ANTIALIAS_DEFAULT = 0;
	public static final int CAIRO_ANTIALIAS_NONE = 1;
	public static final int CAIRO_ANTIALIAS_GRAY = 2;
	public static final int CAIRO_ANTIALIAS_SUBPIXEL = 3;
	public static final int CAIRO_ANTIALIAS_BEST = 6;
	public static final int CAIRO_CONTENT_COLOR = 0x1000;
	public static final int CAIRO_CONTENT_ALPHA = 0x2000;
	public static final int CAIRO_CONTENT_COLOR_ALPHA = 0x3000;
	public static final int CAIRO_FORMAT_ARGB32 = 0;
	public static final int CAIRO_FORMAT_RGB24 = 1;
	public static final int CAIRO_FORMAT_A8 = 2;
	public static final int CAIRO_FORMAT_A1 = 3;
	public static final int CAIRO_OPERATOR_SOURCE = 1;
	public static final int CAIRO_OPERATOR_OVER = 2;
	public static final int CAIRO_OPERATOR_DIFFERENCE = 23;
	public static final int CAIRO_FILL_RULE_WINDING = 0;
	public static final int CAIRO_FILL_RULE_EVEN_ODD = 1;
	public static final int CAIRO_LINE_CAP_BUTT = 0;
	public static final int CAIRO_LINE_CAP_ROUND = 1;
	public static final int CAIRO_LINE_CAP_SQUARE = 2;
	public static final int CAIRO_LINE_JOIN_MITER = 0;
	public static final int CAIRO_LINE_JOIN_ROUND = 1;
	public static final int CAIRO_LINE_JOIN_BEVEL = 2;
	public static final int CAIRO_FONT_SLANT_NORMAL = 0;
	public static final int CAIRO_FONT_SLANT_ITALIC = 1;
	public static final int CAIRO_FONT_SLANT_OBLIQUE = 2;
	public static final int CAIRO_FONT_WEIGHT_NORMAL = 0;
	public static final int CAIRO_FONT_WEIGHT_BOLD = 1;
	public static final int CAIRO_SURFACE_TYPE_IMAGE = 0;
	public static final int CAIRO_SURFACE_TYPE_PDF = 1;
	public static final int CAIRO_SURFACE_TYPE_PS = 2;
	public static final int CAIRO_SURFACE_TYPE_XLIB = 3;
	public static final int CAIRO_REGION_OVERLAP_OUT = 1;
	public static final int CAIRO_FILTER_FAST = 0;
	public static final int CAIRO_FILTER_GOOD = 1;
	public static final int CAIRO_FILTER_BEST = 2;
	public static final int CAIRO_FILTER_NEAREST = 3;
	public static final int CAIRO_FILTER_BILINEAR = 4;
	public static final int CAIRO_FILTER_GAUSSIAN = 5;
	public static final int CAIRO_EXTEND_NONE = 0;
	public static final int CAIRO_EXTEND_REPEAT = 1;
	public static final int CAIRO_EXTEND_REFLECT = 2;
	public static final int CAIRO_EXTEND_PAD = 3;
	public static final int CAIRO_PATH_MOVE_TO = 0;
	public static final int CAIRO_PATH_LINE_TO = 1;
	public static final int CAIRO_PATH_CURVE_TO = 2;
	public static final int CAIRO_PATH_CLOSE_PATH = 3;

/** 64*/
public static final native int cairo_path_data_t_sizeof ();
public static final native int cairo_rectangle_int_t_sizeof ();
public static final native int cairo_path_t_sizeof ();

/** Natives */
public static final native int CAIRO_VERSION_ENCODE(int major, int minor, int micro);
/**
 * @param cr cast=(cairo_t *)
 * @param path cast=(cairo_path_t *)
 */
public static final native void cairo_append_path(long cr, long path);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_arc(long cr, double xc, double yc, double radius, double angle1, double angle2);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_arc_negative(long cr, double xc, double yc, double radius, double angle1, double angle2);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_clip(long cr);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_close_path(long cr);
/** @param cr cast=(cairo_t *) */
public static final native long cairo_copy_path(long cr);
/** @param cr cast=(cairo_t *) */
public static final native long cairo_copy_path_flat(long cr);
/** @param target cast=(cairo_surface_t *) */
public static final native long cairo_create(long target);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_curve_to(long cr, double x1, double y1, double x2, double y2, double x3, double y3);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_destroy(long cr);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_fill(long cr);
public static final native long cairo_font_options_create();
/** @param options cast=(cairo_font_options_t *) */
public static final native void cairo_font_options_destroy(long options);
/** @param options cast=(cairo_font_options_t *) */
public static final native int cairo_font_options_get_antialias(long options);
/** @param options cast=(cairo_font_options_t *) */
public static final native void cairo_font_options_set_antialias(long options, int antialias);
/** @param cr cast=(cairo_t *) */
public static final native int cairo_get_antialias(long cr);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_get_current_point(long cr, double[] x, double[] y);
/** @param cr cast=(cairo_t *) */
public static final native int cairo_get_fill_rule(long cr);
/** @param cr cast=(cairo_t *) */
public static final native long cairo_get_font_face(long cr);
/**
 * @param cr cast=(cairo_t *)
 * @param matrix cast=(cairo_matrix_t *)
 */
public static final native void cairo_get_matrix(long cr, double[] matrix);
/** @param cr cast=(cairo_t *) */
public static final native long cairo_get_source(long cr);
/** @param cr cast=(cairo_t *) */
public static final native long cairo_get_target(long cr);
/** @param cr cast=(cairo_t *) */
public static final native double cairo_get_tolerance(long cr);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_identity_matrix(long cr);
public static final native long cairo_image_surface_create(int format, int width, int height);
/**
 * @param surface cast=(cairo_surface_t *)
 */
public static final native long cairo_image_surface_get_data(long surface);
/**
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int cairo_image_surface_get_format(long surface);
/** @param surface cast=(cairo_surface_t *) */
public static final native int cairo_image_surface_get_height(long surface);
/** @param surface cast=(cairo_surface_t *) */
public static final native int cairo_image_surface_get_width(long surface);
/**
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int cairo_image_surface_get_stride(long surface);
/** @param cr cast=(cairo_t *) */
public static final native int cairo_in_fill(long cr, double x, double y);
/** @param cr cast=(cairo_t *) */
public static final native int cairo_in_stroke(long cr, double x, double y);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_line_to(long cr, double x, double y);
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void cairo_matrix_init(double[] matrix, double xx, double yx, double xy, double yy, double x0, double y0);
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void cairo_matrix_init_identity(double[] matrix);
/** @param matrix cast=(cairo_matrix_t *) */
public static final native int cairo_matrix_invert(double[] matrix);
/**
 * @param result cast=(cairo_matrix_t *)
 * @param a cast=(cairo_matrix_t *)
 * @param b cast=(cairo_matrix_t *)
 */
public static final native void cairo_matrix_multiply(double[] result, double[] a, double[] b);
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void cairo_matrix_rotate(double[] matrix, double radians);
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void cairo_matrix_scale(double[] matrix, double sx, double sy);
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void cairo_matrix_transform_point(double[] matrix, double[] x, double[] y);
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void cairo_matrix_translate(double[] matrix, double tx, double ty);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_move_to(long cr, double x, double y);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_new_path(long cr);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_paint(long cr);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_paint_with_alpha(long cr, double alpha);
/** @param path cast=(cairo_path_t *) */
public static final native void cairo_path_destroy(long path);
/** @param pattern cast=(cairo_pattern_t *) */
public static final native void cairo_pattern_add_color_stop_rgba(long pattern, double offset, double red, double green, double blue, double alpha);
/** @param surface cast=(cairo_surface_t *) */
public static final native long cairo_pattern_create_for_surface(long surface);
public static final native long cairo_pattern_create_linear(double x0, double y0, double x1, double y1);
/** @param pattern cast=(cairo_pattern_t *) */
public static final native void cairo_pattern_destroy(long pattern);
/** @param pattern cast=(cairo_pattern_t *) */
public static final native void cairo_pattern_set_extend(long pattern, int extend);
/** @param pattern cast=(cairo_pattern_t *) */
public static final native void cairo_pattern_set_filter(long pattern, int filter);
/**
 * @param pattern cast=(cairo_pattern_t *)
 * @param matrix cast=(cairo_matrix_t *)
 */
public static final native void cairo_pattern_set_matrix(long pattern, double[] matrix);
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native void cairo_pdf_surface_set_size(long surface, double width_in_points, double height_in_points);
/**
 * @param cairo cast=(cairo_t *)
 */
public static final native void cairo_push_group(long cairo);
/**
 * @param cairo cast=(cairo_t *)
 */
public static final native void cairo_pop_group_to_source(long cairo);
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native void cairo_ps_surface_set_size(long surface, double width_in_points, double height_in_points);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_rectangle(long cr, double x, double y, double width, double height);
/** @param cr cast=(cairo_t *) */
public static final native long cairo_reference(long cr);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_reset_clip(long cr);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_restore(long cr);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_save(long cr);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_scale(long cr, double sx, double sy);
/**
 * @method flags=dynamic
 * @param cr cast=(cairo_surface_t *)
 */
public static final native void cairo_surface_set_device_scale(long cr, double sx, double sy);
/**
 * @method flags=dynamic
 * @param cr cast=(cairo_surface_t *)
 * @param sx cast=(double *)
 * @param sy cast=(double *)
 */
public static final native void cairo_surface_get_device_scale(long cr, double [] sx, double [] sy);
/**
 * @param cr cast=(cairo_t *)
 * @param family cast=(const char *)
 */
public static final native void cairo_select_font_face(long cr, byte[] family, int slant, int weight);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_set_antialias(long cr, int antialias);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_set_dash(long cr, double[] dashes, int ndash, double offset);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_set_fill_rule(long cr, int fill_rule);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_set_font_size(long cr, double size);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_set_line_cap(long cr, int line_cap);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_set_line_join(long cr, int line_join);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_set_line_width(long cr, double width);
/**
 * @param cr cast=(cairo_t *)
 * @param matrix cast=(cairo_matrix_t *)
 */
public static final native void cairo_set_matrix(long cr, double[] matrix);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_set_miter_limit(long cr, double limit);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_set_operator(long cr, int op);
/**
 * @param cr cast=(cairo_t *)
 * @param source cast=(cairo_pattern_t *)
 */
public static final native void cairo_set_source(long cr, long source);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_set_source_rgb(long cr, double red, double green, double blue);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_set_source_rgba(long cr, double red, double green, double blue, double alpha);
/**
 * @param cr cast=(cairo_t *)
 * @param surface cast=(cairo_surface_t *)
 */
public static final native void cairo_set_source_surface(long cr, long surface, double x, double y);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_set_tolerance(long cr, double tolerance);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_show_page(long cr);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_stroke(long cr);
/** @param surface cast=(cairo_surface_t *) */
public static final native void cairo_surface_destroy(long surface);
/** @param surface cast=(cairo_surface_t *) */
public static final native void cairo_surface_flush(long surface);
/** @param surface cast=(cairo_surface_t *) */
public static final native void cairo_surface_finish(long surface);
/**
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int cairo_surface_get_type(long surface);
/**
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int cairo_surface_get_content(long surface);
/** @param surface cast=(cairo_surface_t *) */
public static final native void cairo_surface_mark_dirty(long surface);
/** @param surface cast=(cairo_surface_t *) */
public static final native void cairo_surface_reference(long surface);
/**
 * @param cr cast=(cairo_t *)
 * @param matrix cast=(cairo_matrix_t *)
 */
public static final native void cairo_transform(long cr, double[] matrix);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_translate(long cr, double tx, double ty);
/** @param cr cast=(cairo_t *) */
public static final native void cairo_user_to_device_distance(long cr, double[] dx, double[] dy);
public static final native int cairo_version();
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int cairo_xlib_surface_get_height(long surface);
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int cairo_xlib_surface_get_width(long surface);
/**
 * @param region cast=(cairo_region_t *)
 */
public static final native int cairo_region_num_rectangles(long region);
public static final native long cairo_region_create();
/**
 * @param region cast=(cairo_region_t *)
 */
public static final native long cairo_region_copy(long region);
/**
 * @param region cast=(cairo_region_t *)
 */
public static final native boolean cairo_region_contains_point(long region, int x, int y);
/**
 * @param region cast=(cairo_region_t *)
 * @param rect cast=(cairo_rectangle_int_t *)
 */
public static final native long cairo_region_contains_rectangle(long region, cairo_rectangle_int_t rect);
/** @param region cast=(cairo_region_t *) */
public static final native void cairo_region_destroy(long region);
/** @param region cast=(cairo_region_t *) */
public static final native boolean cairo_region_is_empty(long region);
/**
 * @param region cast=(cairo_region_t *)
 * @param rectangle cast=(cairo_rectangle_int_t *),flags=no_in
 */
public static final native void cairo_region_get_extents(long region, cairo_rectangle_int_t rectangle);
/**
 * @param region cast=(cairo_region_t *)
 * @param rectangle cast=(cairo_rectangle_int_t *)
 */
public static final native void cairo_region_get_rectangle(long region, int nth, long rectangle);
/**
 * @param source1 cast=(cairo_region_t *)
 * @param source2 cast=(const cairo_region_t *)
 */
public static final native void cairo_region_intersect(long source1, long source2);
/**
 * @param rectangle cast=(const cairo_rectangle_int_t *)
 */
public static final native long cairo_region_create_rectangle(cairo_rectangle_int_t rectangle);
/**
 * @param source1 cast=(cairo_region_t *)
 * @param source2 cast=(const cairo_region_t *)
 */
public static final native void cairo_region_subtract(long source1, long source2);
/**
 * @param region cast=(cairo_region_t *)
 */
public static final native void cairo_region_translate(long region, int dx, int dy);
/**
 * @param source1 cast=(cairo_region_t *)
 * @param source2 cast=(const cairo_region_t *)
 */
public static final native void cairo_region_union(long source1, long source2);
/**
 * @param region cast=(cairo_region_t *)
 * @param rect cast=(cairo_rectangle_int_t *),flags=no_out
 */
public static final native void cairo_region_union_rectangle(long region, cairo_rectangle_int_t rect);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(cairo_path_t dest, long src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(cairo_path_data_t dest, long src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(cairo_rectangle_int_t dest, long src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(double[] dest, long src, long size);

}

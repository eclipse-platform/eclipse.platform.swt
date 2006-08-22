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
 * -  Copyright (C) 2005 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.cairo;

import org.eclipse.swt.internal.*;

public class Cairo {
	static {
		Library.loadLibrary("swt-cairo");
	}

	/** Constants */
	public static final int CAIRO_ANTIALIAS_DEFAULT = 0;
	public static final int CAIRO_ANTIALIAS_NONE = 1;
	public static final int CAIRO_ANTIALIAS_GRAY = 2;
	public static final int CAIRO_ANTIALIAS_SUBPIXEL = 3;
	public static final int CAIRO_FORMAT_ARGB32 = 0;
	public static final int CAIRO_FORMAT_RGB24 = 1;
	public static final int CAIRO_FORMAT_A8 = 2;
	public static final int CAIRO_FORMAT_A1 = 3;
	public static final int CAIRO_OPERATOR_CLEAR = 0;
	public static final int CAIRO_OPERATOR_SRC = 1;
	public static final int CAIRO_OPERATOR_DST = 2;
	public static final int CAIRO_OPERATOR_OVER = 3;
	public static final int CAIRO_OPERATOR_OVER_REVERSE = 4;
	public static final int CAIRO_OPERATOR_IN = 5;
	public static final int CAIRO_OPERATOR_IN_REVERSE = 6;
	public static final int CAIRO_OPERATOR_OUT = 7;
	public static final int CAIRO_OPERATOR_OUT_REVERSE = 8;
	public static final int CAIRO_OPERATOR_ATOP = 9;
	public static final int CAIRO_OPERATOR_ATOP_REVERSE = 10;
	public static final int CAIRO_OPERATOR_XOR = 11;
	public static final int CAIRO_OPERATOR_ADD = 12;
	public static final int CAIRO_OPERATOR_SATURATE = 13;
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
	public static final int CAIRO_STATUS_SUCCESS = 0;
	public static final int CAIRO_STATUS_NO_MEMORY = 1;
	public static final int CAIRO_STATUS_INVALID_RESTORE = 2;
	public static final int CAIRO_STATUS_INVALID_POP_GROUP = 3;
	public static final int CAIRO_STATUS_NO_CURRENT_POINT = 4;
	public static final int CAIRO_STATUS_INVALID_MATRIX = 5;
	public static final int CAIRO_STATUS_NO_TARGET_SURFACE = 6;
	public static final int CAIRO_STATUS_NULL_POINTER =7;
	public static final int CAIRO_FILTER_FAST = 0;
	public static final int CAIRO_FILTER_GOOD = 1;
	public static final int CAIRO_FILTER_BEST = 2;
	public static final int CAIRO_FILTER_NEAREST = 3;
	public static final int CAIRO_FILTER_BILINEAR = 4;
	public static final int CAIRO_FILTER_GAUSSIAN = 5;
	public static final int CAIRO_EXTEND_NONE = 0;
	public static final int CAIRO_EXTEND_REPEAT = 1;
	public static final int CAIRO_EXTEND_REFLECT = 2;
	public static final int CAIRO_PATH_MOVE_TO = 0;
	public static final int CAIRO_PATH_LINE_TO = 1;
	public static final int CAIRO_PATH_CURVE_TO = 2;
	public static final int CAIRO_PATH_CLOSE_PATH = 3;
	
/** 64*/
public static final synchronized native int cairo_font_extents_t_sizeof ();
public static final synchronized native int cairo_path_data_t_sizeof ();
public static final synchronized native int cairo_path_t_sizeof ();
public static final synchronized native int cairo_text_extents_t_sizeof ();
	
/** Natives */
public static final synchronized native int /*long*/ cairo_create (int /*long*/ target);
public static final synchronized native int /*long*/ cairo_reference (int /*long*/ cr);
public static final synchronized native void cairo_destroy (int /*long*/ cr);
public static final synchronized native void cairo_save (int /*long*/ cr);
public static final synchronized native void cairo_restore (int /*long*/ cr);
public static final synchronized native void cairo_set_operator (int /*long*/ cr, int op);
public static final synchronized native void cairo_set_source_rgb(int /*long*/ cr, double red, double green, double blue);
public static final synchronized native void cairo_set_source_rgba(int /*long*/ cr, double red, double green, double blue, double alpha);
public static final synchronized native void cairo_set_source(int /*long*/ cr, int /*long*/ source);
public static final synchronized native void cairo_set_source_surface(int /*long*/ cr, int /*long*/ surface, double x, double y);
public static final synchronized native void cairo_set_tolerance (int /*long*/ cr, double tolerance);
public static final synchronized native void cairo_set_antialias (int /*long*/ cr, int antialias);
public static final synchronized native void cairo_set_fill_rule (int /*long*/ cr, int fill_rule);
public static final synchronized native void cairo_set_line_width (int /*long*/ cr, double width);
public static final synchronized native void cairo_set_line_cap (int /*long*/ cr, int line_cap);
public static final synchronized native void cairo_set_line_join (int /*long*/ cr, int line_join);
public static final synchronized native void cairo_set_dash (int /*long*/ cr, double[] dashes, int ndash, double offset);
public static final synchronized native void cairo_set_miter_limit (int /*long*/ cr, double limit);
public static final synchronized native void cairo_translate (int /*long*/ cr, double tx, double ty);
public static final synchronized native void cairo_scale (int /*long*/ cr, double sx, double sy);
public static final synchronized native void cairo_rotate (int /*long*/ cr, double angle);
public static final synchronized native void cairo_transform (int /*long*/ cr, double[] matrix);
public static final synchronized native void cairo_set_matrix (int /*long*/ cr, double[] matrix);
public static final synchronized native void cairo_identity_matrix (int /*long*/ cr);
public static final synchronized native void cairo_user_to_device (int /*long*/ cr, double[] x, double[] y);
public static final synchronized native void cairo_user_to_device_distance (int /*long*/ cr, double[] dx, double[] dy);
public static final synchronized native void cairo_device_to_user (int /*long*/ cr, double[] x, double[] y);
public static final synchronized native void cairo_device_to_user_distance (int /*long*/ cr, double[] dx, double[] dy);
public static final synchronized native void cairo_new_path (int /*long*/ cr);
public static final synchronized native void cairo_move_to (int /*long*/ cr, double x, double y);
public static final synchronized native void cairo_line_to (int /*long*/ cr, double x, double y);
public static final synchronized native void cairo_curve_to (int /*long*/ cr, double x1, double y1, double x2, double y2, double x3, double y3);
public static final synchronized native void cairo_arc (int /*long*/ cr, double xc, double yc, double radius, double angle1, double angle2);
public static final synchronized native void cairo_arc_negative (int /*long*/ cr, double xc, double yc, double radius, double angle1, double angle2);
//public static final synchronized native void cairo_arc_to (int /*long*/ cr, double x1, double y1, double x2, double y2, double radius);
public static final synchronized native void cairo_rel_move_to (int /*long*/ cr, double dx, double dy);
public static final synchronized native void cairo_rel_line_to (int /*long*/ cr, double dx, double dy);
public static final synchronized native void cairo_rel_curve_to (int /*long*/ cr, double dx1, double dy1, double dx2, double dy2, double dx3, double dy3);
public static final synchronized native void cairo_rectangle (int /*long*/ cr, double x, double y, double width, double height);
public static final synchronized native void cairo_close_path (int /*long*/ cr);
public static final synchronized native void cairo_paint (int /*long*/ cr);
public static final synchronized native void cairo_paint_with_alpha (int /*long*/ cr, double alpha);
public static final synchronized native void cairo_mask (int /*long*/ cr, int /*long*/ pattern);
public static final synchronized native void cairo_mask_surface (int /*long*/ cr, int /*long*/ surface, double surface_x, double surface_y);
public static final synchronized native void cairo_stroke (int /*long*/ cr);
public static final synchronized native void cairo_stroke_preserve (int /*long*/ cr);
public static final synchronized native void cairo_fill (int /*long*/ cr);
public static final synchronized native void cairo_fill_preserve (int /*long*/ cr);
public static final synchronized native void cairo_copy_page (int /*long*/ cr);
public static final synchronized native void cairo_show_page (int /*long*/ cr);
public static final synchronized native int cairo_in_stroke (int /*long*/ cr, double x, double y);
public static final synchronized native int cairo_in_fill (int /*long*/ cr, double x, double y);
public static final synchronized native void cairo_stroke_extents (int /*long*/ cr, double[] x1, double[] y1, double[] x2, double[] y2);
public static final synchronized native void cairo_fill_extents (int /*long*/ cr, double[] x1, double[] y1, double[] x2, double[] y2);
public static final synchronized native void cairo_clip (int /*long*/ cr);
public static final synchronized native void cairo_clip_preserve (int /*long*/ cr);
public static final synchronized native void cairo_reset_clip (int /*long*/ cr);
public static final synchronized native int /*long*/ cairo_font_options_create ();
public static final synchronized native void cairo_font_options_destroy (int /*long*/ options);
public static final synchronized native void cairo_font_options_set_antialias (int /*long*/ options, int antialias);
public static final synchronized native int  cairo_font_options_get_antialias (int /*long*/ options);
public static final synchronized native void cairo_set_font_options (int /*long*/ cr, int /*long*/ options);
public static final synchronized native void cairo_get_font_options (int /*long*/ cr, int /*long*/ options);
public static final synchronized native void cairo_select_font_face (int /*long*/ cr, byte[] family, int slant, int weight);
public static final synchronized native void cairo_set_font_size (int /*long*/ cr, double size);
public static final synchronized native void cairo_set_font_matrix (int /*long*/ cr, double[] matrix);
public static final synchronized native void cairo_get_font_matrix (int /*long*/ cr, double[] matrix);
public static final synchronized native void cairo_show_text (int /*long*/ cr, byte[] utf8);
public static final synchronized native void cairo_show_glyphs (int /*long*/ cr, int /*long*/ glyphs, int num_glyphs);
public static final synchronized native int /*long*/ cairo_get_font_face (int /*long*/ cr);
public static final synchronized native void cairo_font_extents (int /*long*/ cr, cairo_font_extents_t extents);
public static final synchronized native void cairo_set_font_face (int /*long*/ cr, int /*long*/ font_face);
public static final synchronized native void cairo_text_extents (int /*long*/ cr, byte[] utf8, cairo_text_extents_t extents);
public static final synchronized native void cairo_glyph_extents (int /*long*/ cr, int /*long*/ glyphs, int num_glyphs, int /*long*/ extents);
public static final synchronized native void cairo_text_path (int /*long*/ cr, byte[] utf8);
public static final synchronized native void cairo_glyph_path (int /*long*/ cr, int /*long*/ glyphs, int num_glyphs);
public static final synchronized native int cairo_get_operator (int /*long*/ cr);
public static final synchronized native int /*long*/ cairo_get_source (int /*long*/ cr);
public static final synchronized native double cairo_get_tolerance (int /*long*/ cr);
public static final synchronized native void cairo_get_current_point (int /*long*/ cr, double[] x, double[] y);
public static final synchronized native int cairo_get_antialias (int /*long*/ cr);
public static final synchronized native int cairo_get_fill_rule (int /*long*/ cr);
public static final synchronized native double cairo_get_line_width (int /*long*/ cr);
public static final synchronized native int cairo_get_line_cap (int /*long*/ cr);
public static final synchronized native int cairo_get_line_join (int /*long*/ cr);
public static final synchronized native double cairo_get_miter_limit (int /*long*/ cr);
public static final synchronized native void cairo_get_matrix (int /*long*/ cr, double[] matrix);
public static final synchronized native int /*long*/ cairo_get_target (int /*long*/ cr);
public static final synchronized native int /*long*/ cairo_copy_path (int /*long*/ cr);
public static final synchronized native int /*long*/ cairo_copy_path_flat (int /*long*/ cr);
public static final synchronized native void cairo_append_path (int /*long*/ cr, int /*long*/ path);
public static final synchronized native void cairo_path_destroy (int /*long*/ path);
public static final synchronized native int cairo_status (int /*long*/ cr);
public static final synchronized native int /*long*/ cairo_status_to_string (int status);
public static final synchronized native int /*long*/ cairo_image_surface_create (int format, int width, int height);
public static final synchronized native int /*long*/ cairo_image_surface_create_for_data (int /*long*/ data, int format, int width, int height, int stride);
public static final synchronized native int cairo_image_surface_get_width (int /*long*/ surface);
public static final synchronized native int cairo_image_surface_get_height (int /*long*/ surface);
public static final synchronized native int /*long*/ cairo_surface_create_similar (int /*long*/ other, int format, int width, int height);
public static final synchronized native void cairo_surface_reference (int /*long*/ surface);
public static final synchronized native void cairo_surface_destroy (int /*long*/ surface);
public static final synchronized native void cairo_surface_finish (int /*long*/ surface);
public static final synchronized native int cairo_surface_set_user_data (int /*long*/ surface, int /*long*/ key, int /*long*/ user_data, int /*long*/ destroy);
public static final synchronized native int /*long*/ cairo_surface_get_user_data (int /*long*/ surface, int /*long*/ key);
public static final synchronized native void cairo_surface_set_device_offset (int /*long*/ surface, double x_offset, double y_offset);
public static final synchronized native int /*long*/ cairo_pattern_create_for_surface (int /*long*/ surface);
public static final synchronized native int /*long*/ cairo_pattern_create_linear (double x0, double y0, double x1, double y1);
public static final synchronized native int /*long*/ cairo_pattern_create_radial (double cx0, double cy0, double radius0, double cx1, double cy1, double radius1);
public static final synchronized native void cairo_pattern_reference (int /*long*/ pattern);
public static final synchronized native void cairo_pattern_destroy (int /*long*/ pattern);
public static final synchronized native void cairo_pattern_add_color_stop_rgb (int /*long*/ pattern, double offset, double red, double green, double blue);
public static final synchronized native void cairo_pattern_add_color_stop_rgba (int /*long*/ pattern, double offset, double red, double green, double blue, double alpha);
public static final synchronized native void cairo_pattern_set_matrix (int /*long*/ pattern, double[] matrix);
public static final synchronized native void cairo_pattern_get_matrix (int /*long*/ pattern, double[] matrix);
public static final synchronized native void cairo_pattern_set_extend (int /*long*/ pattern, int extend);
public static final synchronized native int cairo_pattern_get_extend (int /*long*/ pattern);
public static final synchronized native void cairo_pattern_set_filter (int /*long*/ pattern, int filter);
public static final synchronized native int cairo_pattern_get_filter (int /*long*/ pattern);
public static final synchronized native void cairo_matrix_init (double[] matrix, double xx, double yx, double xy, double yy, double x0, double y0);
public static final synchronized native void cairo_matrix_init_identity (double[] matrix);
public static final synchronized native void cairo_matrix_init_translate (double[] matrix, double tx, double ty);
public static final synchronized native void cairo_matrix_init_scale (double[] matrix, double sx, double sy);
public static final synchronized native void cairo_matrix_init_rotate (double[] matrix, double radians);
public static final synchronized native void cairo_matrix_translate (double[] matrix, double tx, double ty);
public static final synchronized native void cairo_matrix_scale (double[] matrix, double sx, double sy);
public static final synchronized native void cairo_matrix_rotate (double[] matrix, double radians);
public static final synchronized native int cairo_matrix_invert (double[] matrix);
public static final synchronized native void cairo_matrix_multiply (double[] result, double[] a, double[] b);
public static final synchronized native void cairo_matrix_transform_distance (double[] matrix, double[] dx, double[] dy);
public static final synchronized native void cairo_matrix_transform_point (double[] matrix, double[] x, double[] y);
public static final synchronized native int /*long*/ cairo_xlib_surface_create (int /*long*/ dpy, int /*long*/ drawable, int /*long*/ visual, int width, int height);
public static final synchronized native int /*long*/ cairo_xlib_surface_create_for_bitmap (int /*long*/ dpy, int /*long*/ pixmap, int /*long*/ screen, int width, int height);
public static final synchronized native void cairo_xlib_surface_set_size (int /*long*/ surface, int width, int height);
public static final native void memmove(cairo_path_t dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove(cairo_path_data_t dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove(double[] dest, int /*long*/ src, int /*long*/ size);

}

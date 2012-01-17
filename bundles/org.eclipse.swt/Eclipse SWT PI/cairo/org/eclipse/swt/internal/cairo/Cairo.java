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
 * -  Copyright (C) 2005, 2008 IBM Corp.  All Rights Reserved.
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
	public static final int CAIRO_CONTENT_COLOR = 0x1000;
	public static final int CAIRO_CONTENT_ALPHA = 0x2000;
	public static final int CAIRO_CONTENT_COLOR_ALPHA = 0x3000;
	public static final int CAIRO_FORMAT_ARGB32 = 0;
	public static final int CAIRO_FORMAT_RGB24 = 1;
	public static final int CAIRO_FORMAT_A8 = 2;
	public static final int CAIRO_FORMAT_A1 = 3;
	public static final int CAIRO_OPERATOR_CLEAR = 0;
	public static final int CAIRO_OPERATOR_SOURCE = 1;
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
	public static final int CAIRO_STATUS_SUCCESS = 0;
	public static final int CAIRO_STATUS_NO_MEMORY = 1;
	public static final int CAIRO_STATUS_INVALID_RESTORE = 2;
	public static final int CAIRO_STATUS_INVALID_POP_GROUP = 3;
	public static final int CAIRO_STATUS_NO_CURRENT_POINT = 4;
	public static final int CAIRO_STATUS_INVALID_MATRIX = 5;
	public static final int CAIRO_STATUS_NO_TARGET_SURFACE = 6;
	public static final int CAIRO_STATUS_NULL_POINTER =7;
	public static final int CAIRO_SURFACE_TYPE_IMAGE = 0;
	public static final int CAIRO_SURFACE_TYPE_PDF = 1;
    public static final int CAIRO_SURFACE_TYPE_PS = 2;
    public static final int CAIRO_SURFACE_TYPE_XLIB = 3;
    public static final int CAIRO_SURFACE_TYPE_XCB = 4;
    public static final int CAIRO_SURFACE_TYPE_GLITZ = 5;
    public static final int CAIRO_SURFACE_TYPE_QUARTZ = 6;
    public static final int CAIRO_SURFACE_TYPE_WIN32 = 7;
    public static final int CAIRO_SURFACE_TYPE_BEOS = 8;
    public static final int CAIRO_SURFACE_TYPE_DIRECTFB = 9;
    public static final int CAIRO_SURFACE_TYPE_SVG = 10;
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
public static final native int cairo_font_extents_t_sizeof ();
public static final native int cairo_path_data_t_sizeof ();
public static final native int cairo_path_t_sizeof ();
public static final native int cairo_text_extents_t_sizeof ();
	
/** Natives */
public static final native int CAIRO_VERSION_ENCODE(int major, int minor, int micro);
/**
 * @param cr cast=(cairo_t *)
 * @param path cast=(cairo_path_t *)
 */
public static final native void _cairo_append_path(int /*long*/ cr, int /*long*/ path);
public static final void cairo_append_path(int /*long*/ cr, int /*long*/ path) {
	lock.lock();
	try {
		_cairo_append_path(cr, path);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_arc(int /*long*/ cr, double xc, double yc, double radius, double angle1, double angle2);
public static final void cairo_arc(int /*long*/ cr, double xc, double yc, double radius, double angle1, double angle2) {
	lock.lock();
	try {
		_cairo_arc(cr, xc, yc, radius, angle1, angle2);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_arc_negative(int /*long*/ cr, double xc, double yc, double radius, double angle1, double angle2);
public static final void cairo_arc_negative(int /*long*/ cr, double xc, double yc, double radius, double angle1, double angle2) {
	lock.lock();
	try {
		_cairo_arc_negative(cr, xc, yc, radius, angle1, angle2);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_clip(int /*long*/ cr);
public static final void cairo_clip(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_clip(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_clip_preserve(int /*long*/ cr);
public static final void cairo_clip_preserve(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_clip_preserve(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_close_path(int /*long*/ cr);
public static final void cairo_close_path(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_close_path(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_copy_page(int /*long*/ cr);
public static final void cairo_copy_page(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_copy_page(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int /*long*/ _cairo_copy_path(int /*long*/ cr);
public static final int /*long*/ cairo_copy_path(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_copy_path(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int /*long*/ _cairo_copy_path_flat(int /*long*/ cr);
public static final int /*long*/ cairo_copy_path_flat(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_copy_path_flat(cr);
	} finally {
		lock.unlock();
	}
}
/** @param target cast=(cairo_surface_t *) */
public static final native int /*long*/ _cairo_create(int /*long*/ target);
public static final int /*long*/ cairo_create(int /*long*/ target) {
	lock.lock();
	try {
		return _cairo_create(target);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_curve_to(int /*long*/ cr, double x1, double y1, double x2, double y2, double x3, double y3);
public static final void cairo_curve_to(int /*long*/ cr, double x1, double y1, double x2, double y2, double x3, double y3) {
	lock.lock();
	try {
		_cairo_curve_to(cr, x1, y1, x2, y2, x3, y3);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_destroy(int /*long*/ cr);
public static final void cairo_destroy(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_destroy(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_device_to_user(int /*long*/ cr, double[] x, double[] y);
public static final void cairo_device_to_user(int /*long*/ cr, double[] x, double[] y) {
	lock.lock();
	try {
		_cairo_device_to_user(cr, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_device_to_user_distance(int /*long*/ cr, double[] dx, double[] dy);
public static final void cairo_device_to_user_distance(int /*long*/ cr, double[] dx, double[] dy) {
	lock.lock();
	try {
		_cairo_device_to_user_distance(cr, dx, dy);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_fill(int /*long*/ cr);
public static final void cairo_fill(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_fill(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_fill_extents(int /*long*/ cr, double[] x1, double[] y1, double[] x2, double[] y2);
public static final void cairo_fill_extents(int /*long*/ cr, double[] x1, double[] y1, double[] x2, double[] y2) {
	lock.lock();
	try {
		_cairo_fill_extents(cr, x1, y1, x2, y2);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_fill_preserve(int /*long*/ cr);
public static final void cairo_fill_preserve(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_fill_preserve(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_font_extents(int /*long*/ cr, cairo_font_extents_t extents);
public static final void cairo_font_extents(int /*long*/ cr, cairo_font_extents_t extents) {
	lock.lock();
	try {
		_cairo_font_extents(cr, extents);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _cairo_font_options_create();
public static final int /*long*/ cairo_font_options_create() {
	lock.lock();
	try {
		return _cairo_font_options_create();
	} finally {
		lock.unlock();
	}
}
/** @param options cast=(cairo_font_options_t *) */
public static final native void _cairo_font_options_destroy(int /*long*/ options);
public static final void cairo_font_options_destroy(int /*long*/ options) {
	lock.lock();
	try {
		_cairo_font_options_destroy(options);
	} finally {
		lock.unlock();
	}
}
/** @param options cast=(cairo_font_options_t *) */
public static final native int _cairo_font_options_get_antialias(int /*long*/ options);
public static final int cairo_font_options_get_antialias(int /*long*/ options) {
	lock.lock();
	try {
		return _cairo_font_options_get_antialias(options);
	} finally {
		lock.unlock();
	}
}
/** @param options cast=(cairo_font_options_t *) */
public static final native void _cairo_font_options_set_antialias(int /*long*/ options, int antialias);
public static final void cairo_font_options_set_antialias(int /*long*/ options, int antialias) {
	lock.lock();
	try {
		_cairo_font_options_set_antialias(options, antialias);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _cairo_format_stride_for_width(int format, int width);
public static final int cairo_format_stride_for_width(int format, int width) {
	lock.lock();
	try {
		return _cairo_format_stride_for_width(format, width);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int _cairo_get_antialias(int /*long*/ cr);
public static final int cairo_get_antialias(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_get_antialias(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_get_current_point(int /*long*/ cr, double[] x, double[] y);
public static final void cairo_get_current_point(int /*long*/ cr, double[] x, double[] y) {
	lock.lock();
	try {
		_cairo_get_current_point(cr, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int _cairo_get_fill_rule(int /*long*/ cr);
public static final int cairo_get_fill_rule(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_get_fill_rule(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int /*long*/ _cairo_get_font_face(int /*long*/ cr);
public static final int /*long*/ cairo_get_font_face(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_get_font_face(cr);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param matrix cast=(cairo_matrix_t *)
 */
public static final native void _cairo_get_font_matrix(int /*long*/ cr, double[] matrix);
public static final void cairo_get_font_matrix(int /*long*/ cr, double[] matrix) {
	lock.lock();
	try {
		_cairo_get_font_matrix(cr, matrix);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param options cast=(cairo_font_options_t *)
 */
public static final native void _cairo_get_font_options(int /*long*/ cr, int /*long*/ options);
public static final void cairo_get_font_options(int /*long*/ cr, int /*long*/ options) {
	lock.lock();
	try {
		_cairo_get_font_options(cr, options);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int _cairo_get_line_cap(int /*long*/ cr);
public static final int cairo_get_line_cap(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_get_line_cap(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int _cairo_get_line_join(int /*long*/ cr);
public static final int cairo_get_line_join(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_get_line_join(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native double _cairo_get_line_width(int /*long*/ cr);
public static final double cairo_get_line_width(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_get_line_width(cr);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param matrix cast=(cairo_matrix_t *)
 */
public static final native void _cairo_get_matrix(int /*long*/ cr, double[] matrix);
public static final void cairo_get_matrix(int /*long*/ cr, double[] matrix) {
	lock.lock();
	try {
		_cairo_get_matrix(cr, matrix);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native double _cairo_get_miter_limit(int /*long*/ cr);
public static final double cairo_get_miter_limit(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_get_miter_limit(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int _cairo_get_operator(int /*long*/ cr);
public static final int cairo_get_operator(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_get_operator(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int /*long*/ _cairo_get_source(int /*long*/ cr);
public static final int /*long*/ cairo_get_source(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_get_source(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int /*long*/ _cairo_get_target(int /*long*/ cr);
public static final int /*long*/ cairo_get_target(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_get_target(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native double _cairo_get_tolerance(int /*long*/ cr);
public static final double cairo_get_tolerance(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_get_tolerance(cr);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param glyphs cast=(cairo_glyph_t *)
 * @param extents cast=(cairo_text_extents_t *)
 */
public static final native void _cairo_glyph_extents(int /*long*/ cr, int /*long*/ glyphs, int num_glyphs, int /*long*/ extents);
public static final void cairo_glyph_extents(int /*long*/ cr, int /*long*/ glyphs, int num_glyphs, int /*long*/ extents) {
	lock.lock();
	try {
		_cairo_glyph_extents(cr, glyphs, num_glyphs, extents);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param glyphs cast=(cairo_glyph_t *)
 */
public static final native void _cairo_glyph_path(int /*long*/ cr, int /*long*/ glyphs, int num_glyphs);
public static final void cairo_glyph_path(int /*long*/ cr, int /*long*/ glyphs, int num_glyphs) {
	lock.lock();
	try {
		_cairo_glyph_path(cr, glyphs, num_glyphs);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_identity_matrix(int /*long*/ cr);
public static final void cairo_identity_matrix(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_identity_matrix(cr);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _cairo_image_surface_create(int format, int width, int height);
public static final int /*long*/ cairo_image_surface_create(int format, int width, int height) {
	lock.lock();
	try {
		return _cairo_image_surface_create(format, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param data cast=(unsigned char *) */
public static final native int /*long*/ _cairo_image_surface_create_for_data(int /*long*/ data, int format, int width, int height, int stride);
public static final int /*long*/ cairo_image_surface_create_for_data(int /*long*/ data, int format, int width, int height, int stride) {
	lock.lock();
	try {
		return _cairo_image_surface_create_for_data(data, format, width, height, stride);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int /*long*/ _cairo_image_surface_get_data(int /*long*/ surface);
public static final int /*long*/ cairo_image_surface_get_data(int /*long*/ surface) {
	lock.lock();
	try {
		return _cairo_image_surface_get_data(surface);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int _cairo_image_surface_get_format(int /*long*/ surface);
public static final int cairo_image_surface_get_format(int /*long*/ surface) {
	lock.lock();
	try {
		return _cairo_image_surface_get_format(surface);
	} finally {
		lock.unlock();
	}
}
/** @param surface cast=(cairo_surface_t *) */
public static final native int _cairo_image_surface_get_height(int /*long*/ surface);
public static final int cairo_image_surface_get_height(int /*long*/ surface) {
	lock.lock();
	try {
		return _cairo_image_surface_get_height(surface);
	} finally {
		lock.unlock();
	}
}
/** @param surface cast=(cairo_surface_t *) */
public static final native int _cairo_image_surface_get_width(int /*long*/ surface);
public static final int cairo_image_surface_get_width(int /*long*/ surface) {
	lock.lock();
	try {
		return _cairo_image_surface_get_width(surface);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int _cairo_image_surface_get_stride(int /*long*/ surface);
public static final int cairo_image_surface_get_stride(int /*long*/ surface) {
	lock.lock();
	try {
		return _cairo_image_surface_get_stride(surface);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int _cairo_in_fill(int /*long*/ cr, double x, double y);
public static final int cairo_in_fill(int /*long*/ cr, double x, double y) {
	lock.lock();
	try {
		return _cairo_in_fill(cr, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int _cairo_in_stroke(int /*long*/ cr, double x, double y);
public static final int cairo_in_stroke(int /*long*/ cr, double x, double y) {
	lock.lock();
	try {
		return _cairo_in_stroke(cr, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_line_to(int /*long*/ cr, double x, double y);
public static final void cairo_line_to(int /*long*/ cr, double x, double y) {
	lock.lock();
	try {
		_cairo_line_to(cr, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param pattern cast=(cairo_pattern_t *)
 */
public static final native void _cairo_mask(int /*long*/ cr, int /*long*/ pattern);
public static final void cairo_mask(int /*long*/ cr, int /*long*/ pattern) {
	lock.lock();
	try {
		_cairo_mask(cr, pattern);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param surface cast=(cairo_surface_t *)
 */
public static final native void _cairo_mask_surface(int /*long*/ cr, int /*long*/ surface, double surface_x, double surface_y);
public static final void cairo_mask_surface(int /*long*/ cr, int /*long*/ surface, double surface_x, double surface_y) {
	lock.lock();
	try {
		_cairo_mask_surface(cr, surface, surface_x, surface_y);
	} finally {
		lock.unlock();
	}
}
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void _cairo_matrix_init(double[] matrix, double xx, double yx, double xy, double yy, double x0, double y0);
public static final void cairo_matrix_init(double[] matrix, double xx, double yx, double xy, double yy, double x0, double y0) {
	lock.lock();
	try {
		_cairo_matrix_init(matrix, xx, yx, xy, yy, x0, y0);
	} finally {
		lock.unlock();
	}
}
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void _cairo_matrix_init_identity(double[] matrix);
public static final void cairo_matrix_init_identity(double[] matrix) {
	lock.lock();
	try {
		_cairo_matrix_init_identity(matrix);
	} finally {
		lock.unlock();
	}
}
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void _cairo_matrix_init_rotate(double[] matrix, double radians);
public static final void cairo_matrix_init_rotate(double[] matrix, double radians) {
	lock.lock();
	try {
		_cairo_matrix_init_rotate(matrix, radians);
	} finally {
		lock.unlock();
	}
}
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void _cairo_matrix_init_scale(double[] matrix, double sx, double sy);
public static final void cairo_matrix_init_scale(double[] matrix, double sx, double sy) {
	lock.lock();
	try {
		_cairo_matrix_init_scale(matrix, sx, sy);
	} finally {
		lock.unlock();
	}
}
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void _cairo_matrix_init_translate(double[] matrix, double tx, double ty);
public static final void cairo_matrix_init_translate(double[] matrix, double tx, double ty) {
	lock.lock();
	try {
		_cairo_matrix_init_translate(matrix, tx, ty);
	} finally {
		lock.unlock();
	}
}
/** @param matrix cast=(cairo_matrix_t *) */
public static final native int _cairo_matrix_invert(double[] matrix);
public static final int cairo_matrix_invert(double[] matrix) {
	lock.lock();
	try {
		return _cairo_matrix_invert(matrix);
	} finally {
		lock.unlock();
	}
}
/**
 * @param result cast=(cairo_matrix_t *)
 * @param a cast=(cairo_matrix_t *)
 * @param b cast=(cairo_matrix_t *)
 */
public static final native void _cairo_matrix_multiply(double[] result, double[] a, double[] b);
public static final void cairo_matrix_multiply(double[] result, double[] a, double[] b) {
	lock.lock();
	try {
		_cairo_matrix_multiply(result, a, b);
	} finally {
		lock.unlock();
	}
}
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void _cairo_matrix_rotate(double[] matrix, double radians);
public static final void cairo_matrix_rotate(double[] matrix, double radians) {
	lock.lock();
	try {
		_cairo_matrix_rotate(matrix, radians);
	} finally {
		lock.unlock();
	}
}
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void _cairo_matrix_scale(double[] matrix, double sx, double sy);
public static final void cairo_matrix_scale(double[] matrix, double sx, double sy) {
	lock.lock();
	try {
		_cairo_matrix_scale(matrix, sx, sy);
	} finally {
		lock.unlock();
	}
}
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void _cairo_matrix_transform_distance(double[] matrix, double[] dx, double[] dy);
public static final void cairo_matrix_transform_distance(double[] matrix, double[] dx, double[] dy) {
	lock.lock();
	try {
		_cairo_matrix_transform_distance(matrix, dx, dy);
	} finally {
		lock.unlock();
	}
}
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void _cairo_matrix_transform_point(double[] matrix, double[] x, double[] y);
public static final void cairo_matrix_transform_point(double[] matrix, double[] x, double[] y) {
	lock.lock();
	try {
		_cairo_matrix_transform_point(matrix, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param matrix cast=(cairo_matrix_t *) */
public static final native void _cairo_matrix_translate(double[] matrix, double tx, double ty);
public static final void cairo_matrix_translate(double[] matrix, double tx, double ty) {
	lock.lock();
	try {
		_cairo_matrix_translate(matrix, tx, ty);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_move_to(int /*long*/ cr, double x, double y);
public static final void cairo_move_to(int /*long*/ cr, double x, double y) {
	lock.lock();
	try {
		_cairo_move_to(cr, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_new_path(int /*long*/ cr);
public static final void cairo_new_path(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_new_path(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_paint(int /*long*/ cr);
public static final void cairo_paint(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_paint(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_paint_with_alpha(int /*long*/ cr, double alpha);
public static final void cairo_paint_with_alpha(int /*long*/ cr, double alpha) {
	lock.lock();
	try {
		_cairo_paint_with_alpha(cr, alpha);
	} finally {
		lock.unlock();
	}
}
/** @param path cast=(cairo_path_t *) */
public static final native void _cairo_path_destroy(int /*long*/ path);
public static final void cairo_path_destroy(int /*long*/ path) {
	lock.lock();
	try {
		_cairo_path_destroy(path);
	} finally {
		lock.unlock();
	}
}
/** @param pattern cast=(cairo_pattern_t *) */
public static final native void _cairo_pattern_add_color_stop_rgb(int /*long*/ pattern, double offset, double red, double green, double blue);
public static final void cairo_pattern_add_color_stop_rgb(int /*long*/ pattern, double offset, double red, double green, double blue) {
	lock.lock();
	try {
		_cairo_pattern_add_color_stop_rgb(pattern, offset, red, green, blue);
	} finally {
		lock.unlock();
	}
}
/** @param pattern cast=(cairo_pattern_t *) */
public static final native void _cairo_pattern_add_color_stop_rgba(int /*long*/ pattern, double offset, double red, double green, double blue, double alpha);
public static final void cairo_pattern_add_color_stop_rgba(int /*long*/ pattern, double offset, double red, double green, double blue, double alpha) {
	lock.lock();
	try {
		_cairo_pattern_add_color_stop_rgba(pattern, offset, red, green, blue, alpha);
	} finally {
		lock.unlock();
	}
}
/** @param surface cast=(cairo_surface_t *) */
public static final native int /*long*/ _cairo_pattern_create_for_surface(int /*long*/ surface);
public static final int /*long*/ cairo_pattern_create_for_surface(int /*long*/ surface) {
	lock.lock();
	try {
		return _cairo_pattern_create_for_surface(surface);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _cairo_pattern_create_linear(double x0, double y0, double x1, double y1);
public static final int /*long*/ cairo_pattern_create_linear(double x0, double y0, double x1, double y1) {
	lock.lock();
	try {
		return _cairo_pattern_create_linear(x0, y0, x1, y1);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _cairo_pattern_create_radial(double cx0, double cy0, double radius0, double cx1, double cy1, double radius1);
public static final int /*long*/ cairo_pattern_create_radial(double cx0, double cy0, double radius0, double cx1, double cy1, double radius1) {
	lock.lock();
	try {
		return _cairo_pattern_create_radial(cx0, cy0, radius0, cx1, cy1, radius1);
	} finally {
		lock.unlock();
	}
}
/** @param pattern cast=(cairo_pattern_t *) */
public static final native void _cairo_pattern_destroy(int /*long*/ pattern);
public static final void cairo_pattern_destroy(int /*long*/ pattern) {
	lock.lock();
	try {
		_cairo_pattern_destroy(pattern);
	} finally {
		lock.unlock();
	}
}
/** @param pattern cast=(cairo_pattern_t *) */
public static final native int _cairo_pattern_get_extend(int /*long*/ pattern);
public static final int cairo_pattern_get_extend(int /*long*/ pattern) {
	lock.lock();
	try {
		return _cairo_pattern_get_extend(pattern);
	} finally {
		lock.unlock();
	}
}
/** @param pattern cast=(cairo_pattern_t *) */
public static final native int _cairo_pattern_get_filter(int /*long*/ pattern);
public static final int cairo_pattern_get_filter(int /*long*/ pattern) {
	lock.lock();
	try {
		return _cairo_pattern_get_filter(pattern);
	} finally {
		lock.unlock();
	}
}
/**
 * @param pattern cast=(cairo_pattern_t *)
 * @param matrix cast=(cairo_matrix_t *)
 */
public static final native void _cairo_pattern_get_matrix(int /*long*/ pattern, double[] matrix);
public static final void cairo_pattern_get_matrix(int /*long*/ pattern, double[] matrix) {
	lock.lock();
	try {
		_cairo_pattern_get_matrix(pattern, matrix);
	} finally {
		lock.unlock();
	}
}
/** @param pattern cast=(cairo_pattern_t *) */
public static final native void _cairo_pattern_reference(int /*long*/ pattern);
public static final void cairo_pattern_reference(int /*long*/ pattern) {
	lock.lock();
	try {
		_cairo_pattern_reference(pattern);
	} finally {
		lock.unlock();
	}
}
/** @param pattern cast=(cairo_pattern_t *) */
public static final native void _cairo_pattern_set_extend(int /*long*/ pattern, int extend);
public static final void cairo_pattern_set_extend(int /*long*/ pattern, int extend) {
	lock.lock();
	try {
		_cairo_pattern_set_extend(pattern, extend);
	} finally {
		lock.unlock();
	}
}
/** @param pattern cast=(cairo_pattern_t *) */
public static final native void _cairo_pattern_set_filter(int /*long*/ pattern, int filter);
public static final void cairo_pattern_set_filter(int /*long*/ pattern, int filter) {
	lock.lock();
	try {
		_cairo_pattern_set_filter(pattern, filter);
	} finally {
		lock.unlock();
	}
}
/**
 * @param pattern cast=(cairo_pattern_t *)
 * @param matrix cast=(cairo_matrix_t *)
 */
public static final native void _cairo_pattern_set_matrix(int /*long*/ pattern, double[] matrix);
public static final void cairo_pattern_set_matrix(int /*long*/ pattern, double[] matrix) {
	lock.lock();
	try {
		_cairo_pattern_set_matrix(pattern, matrix);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native void _cairo_pdf_surface_set_size(int /*long*/ surface, double width_in_points, double height_in_points);
public static final void cairo_pdf_surface_set_size(int /*long*/ surface, double width_in_points, double height_in_points) {
	lock.lock();
	try {
		_cairo_pdf_surface_set_size(surface, width_in_points, height_in_points);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param cairo cast=(cairo_t *)
 */
public static final native void _cairo_push_group(int /*long*/ cairo);
public static final void cairo_push_group(int /*long*/ cairo) {
	lock.lock();
	try {
		_cairo_push_group(cairo);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param cairo cast=(cairo_t *)
 */
public static final native void _cairo_pop_group_to_source(int /*long*/ cairo);
public static final void cairo_pop_group_to_source(int /*long*/ cairo) {
	lock.lock();
	try {
		_cairo_pop_group_to_source(cairo);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native void _cairo_ps_surface_set_size(int /*long*/ surface, double width_in_points, double height_in_points);
public static final void cairo_ps_surface_set_size(int /*long*/ surface, double width_in_points, double height_in_points) {
	lock.lock();
	try {
		_cairo_ps_surface_set_size(surface, width_in_points, height_in_points);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_rectangle(int /*long*/ cr, double x, double y, double width, double height);
public static final void cairo_rectangle(int /*long*/ cr, double x, double y, double width, double height) {
	lock.lock();
	try {
		_cairo_rectangle(cr, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int /*long*/ _cairo_reference(int /*long*/ cr);
public static final int /*long*/ cairo_reference(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_reference(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_rel_curve_to(int /*long*/ cr, double dx1, double dy1, double dx2, double dy2, double dx3, double dy3);
public static final void cairo_rel_curve_to(int /*long*/ cr, double dx1, double dy1, double dx2, double dy2, double dx3, double dy3) {
	lock.lock();
	try {
		_cairo_rel_curve_to(cr, dx1, dy1, dx2, dy2, dx3, dy3);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_rel_line_to(int /*long*/ cr, double dx, double dy);
public static final void cairo_rel_line_to(int /*long*/ cr, double dx, double dy) {
	lock.lock();
	try {
		_cairo_rel_line_to(cr, dx, dy);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_rel_move_to(int /*long*/ cr, double dx, double dy);
public static final void cairo_rel_move_to(int /*long*/ cr, double dx, double dy) {
	lock.lock();
	try {
		_cairo_rel_move_to(cr, dx, dy);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_reset_clip(int /*long*/ cr);
public static final void cairo_reset_clip(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_reset_clip(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_restore(int /*long*/ cr);
public static final void cairo_restore(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_restore(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_rotate(int /*long*/ cr, double angle);
public static final void cairo_rotate(int /*long*/ cr, double angle) {
	lock.lock();
	try {
		_cairo_rotate(cr, angle);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_save(int /*long*/ cr);
public static final void cairo_save(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_save(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_scale(int /*long*/ cr, double sx, double sy);
public static final void cairo_scale(int /*long*/ cr, double sx, double sy) {
	lock.lock();
	try {
		_cairo_scale(cr, sx, sy);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param family cast=(const char *)
 */
public static final native void _cairo_select_font_face(int /*long*/ cr, byte[] family, int slant, int weight);
public static final void cairo_select_font_face(int /*long*/ cr, byte[] family, int slant, int weight) {
	lock.lock();
	try {
		_cairo_select_font_face(cr, family, slant, weight);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_set_antialias(int /*long*/ cr, int antialias);
public static final void cairo_set_antialias(int /*long*/ cr, int antialias) {
	lock.lock();
	try {
		_cairo_set_antialias(cr, antialias);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_set_dash(int /*long*/ cr, double[] dashes, int ndash, double offset);
public static final void cairo_set_dash(int /*long*/ cr, double[] dashes, int ndash, double offset) {
	lock.lock();
	try {
		_cairo_set_dash(cr, dashes, ndash, offset);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_set_fill_rule(int /*long*/ cr, int fill_rule);
public static final void cairo_set_fill_rule(int /*long*/ cr, int fill_rule) {
	lock.lock();
	try {
		_cairo_set_fill_rule(cr, fill_rule);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param font_face cast=(cairo_font_face_t *)
 */
public static final native void _cairo_set_font_face(int /*long*/ cr, int /*long*/ font_face);
public static final void cairo_set_font_face(int /*long*/ cr, int /*long*/ font_face) {
	lock.lock();
	try {
		_cairo_set_font_face(cr, font_face);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param matrix cast=(cairo_matrix_t *)
 */
public static final native void _cairo_set_font_matrix(int /*long*/ cr, double[] matrix);
public static final void cairo_set_font_matrix(int /*long*/ cr, double[] matrix) {
	lock.lock();
	try {
		_cairo_set_font_matrix(cr, matrix);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param options cast=(cairo_font_options_t *)
 */
public static final native void _cairo_set_font_options(int /*long*/ cr, int /*long*/ options);
public static final void cairo_set_font_options(int /*long*/ cr, int /*long*/ options) {
	lock.lock();
	try {
		_cairo_set_font_options(cr, options);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_set_font_size(int /*long*/ cr, double size);
public static final void cairo_set_font_size(int /*long*/ cr, double size) {
	lock.lock();
	try {
		_cairo_set_font_size(cr, size);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_set_line_cap(int /*long*/ cr, int line_cap);
public static final void cairo_set_line_cap(int /*long*/ cr, int line_cap) {
	lock.lock();
	try {
		_cairo_set_line_cap(cr, line_cap);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_set_line_join(int /*long*/ cr, int line_join);
public static final void cairo_set_line_join(int /*long*/ cr, int line_join) {
	lock.lock();
	try {
		_cairo_set_line_join(cr, line_join);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_set_line_width(int /*long*/ cr, double width);
public static final void cairo_set_line_width(int /*long*/ cr, double width) {
	lock.lock();
	try {
		_cairo_set_line_width(cr, width);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param matrix cast=(cairo_matrix_t *)
 */
public static final native void _cairo_set_matrix(int /*long*/ cr, double[] matrix);
public static final void cairo_set_matrix(int /*long*/ cr, double[] matrix) {
	lock.lock();
	try {
		_cairo_set_matrix(cr, matrix);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_set_miter_limit(int /*long*/ cr, double limit);
public static final void cairo_set_miter_limit(int /*long*/ cr, double limit) {
	lock.lock();
	try {
		_cairo_set_miter_limit(cr, limit);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_set_operator(int /*long*/ cr, int op);
public static final void cairo_set_operator(int /*long*/ cr, int op) {
	lock.lock();
	try {
		_cairo_set_operator(cr, op);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param source cast=(cairo_pattern_t *)
 */
public static final native void _cairo_set_source(int /*long*/ cr, int /*long*/ source);
public static final void cairo_set_source(int /*long*/ cr, int /*long*/ source) {
	lock.lock();
	try {
		_cairo_set_source(cr, source);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_set_source_rgb(int /*long*/ cr, double red, double green, double blue);
public static final void cairo_set_source_rgb(int /*long*/ cr, double red, double green, double blue) {
	lock.lock();
	try {
		_cairo_set_source_rgb(cr, red, green, blue);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_set_source_rgba(int /*long*/ cr, double red, double green, double blue, double alpha);
public static final void cairo_set_source_rgba(int /*long*/ cr, double red, double green, double blue, double alpha) {
	lock.lock();
	try {
		_cairo_set_source_rgba(cr, red, green, blue, alpha);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param surface cast=(cairo_surface_t *)
 */
public static final native void _cairo_set_source_surface(int /*long*/ cr, int /*long*/ surface, double x, double y);
public static final void cairo_set_source_surface(int /*long*/ cr, int /*long*/ surface, double x, double y) {
	lock.lock();
	try {
		_cairo_set_source_surface(cr, surface, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_set_tolerance(int /*long*/ cr, double tolerance);
public static final void cairo_set_tolerance(int /*long*/ cr, double tolerance) {
	lock.lock();
	try {
		_cairo_set_tolerance(cr, tolerance);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param glyphs cast=(cairo_glyph_t *)
 */
public static final native void _cairo_show_glyphs(int /*long*/ cr, int /*long*/ glyphs, int num_glyphs);
public static final void cairo_show_glyphs(int /*long*/ cr, int /*long*/ glyphs, int num_glyphs) {
	lock.lock();
	try {
		_cairo_show_glyphs(cr, glyphs, num_glyphs);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_show_page(int /*long*/ cr);
public static final void cairo_show_page(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_show_page(cr);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param utf8 cast=(const char *)
 */
public static final native void _cairo_show_text(int /*long*/ cr, byte[] utf8);
public static final void cairo_show_text(int /*long*/ cr, byte[] utf8) {
	lock.lock();
	try {
		_cairo_show_text(cr, utf8);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native int _cairo_status(int /*long*/ cr);
public static final int cairo_status(int /*long*/ cr) {
	lock.lock();
	try {
		return _cairo_status(cr);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _cairo_status_to_string(int status);
public static final int /*long*/ cairo_status_to_string(int status) {
	lock.lock();
	try {
		return _cairo_status_to_string(status);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_stroke(int /*long*/ cr);
public static final void cairo_stroke(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_stroke(cr);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_stroke_extents(int /*long*/ cr, double[] x1, double[] y1, double[] x2, double[] y2);
public static final void cairo_stroke_extents(int /*long*/ cr, double[] x1, double[] y1, double[] x2, double[] y2) {
	lock.lock();
	try {
		_cairo_stroke_extents(cr, x1, y1, x2, y2);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_stroke_preserve(int /*long*/ cr);
public static final void cairo_stroke_preserve(int /*long*/ cr) {
	lock.lock();
	try {
		_cairo_stroke_preserve(cr);
	} finally {
		lock.unlock();
	}
}
/** @param other cast=(cairo_surface_t *) */
public static final native int /*long*/ _cairo_surface_create_similar(int /*long*/ other, int format, int width, int height);
public static final int /*long*/ cairo_surface_create_similar(int /*long*/ other, int format, int width, int height) {
	lock.lock();
	try {
		return _cairo_surface_create_similar(other, format, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param surface cast=(cairo_surface_t *) */
public static final native void _cairo_surface_destroy(int /*long*/ surface);
public static final void cairo_surface_destroy(int /*long*/ surface) {
	lock.lock();
	try {
		_cairo_surface_destroy(surface);
	} finally {
		lock.unlock();
	}
}
/** @param surface cast=(cairo_surface_t *) */
public static final native void _cairo_surface_flush(int /*long*/ surface);
public static final void cairo_surface_flush(int /*long*/ surface) {
	lock.lock();
	try {
		_cairo_surface_flush(surface);
	} finally {
		lock.unlock();
	}
}
/** @param surface cast=(cairo_surface_t *) */
public static final native void _cairo_surface_finish(int /*long*/ surface);
public static final void cairo_surface_finish(int /*long*/ surface) {
	lock.lock();
	try {
		_cairo_surface_finish(surface);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int _cairo_surface_get_type(int /*long*/ surface);
public static final int cairo_surface_get_type(int /*long*/ surface) {
	lock.lock();
	try {
		return _cairo_surface_get_type(surface);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int _cairo_surface_get_content(int /*long*/ surface);
public static final int cairo_surface_get_content(int /*long*/ surface) {
	lock.lock();
	try {
		return _cairo_surface_get_content(surface);
	} finally {
		lock.unlock();
	}
}
/**
 * @param surface cast=(cairo_surface_t *)
 * @param key cast=(cairo_user_data_key_t *)
 */
public static final native int /*long*/ _cairo_surface_get_user_data(int /*long*/ surface, int /*long*/ key);
public static final int /*long*/ cairo_surface_get_user_data(int /*long*/ surface, int /*long*/ key) {
	lock.lock();
	try {
		return _cairo_surface_get_user_data(surface, key);
	} finally {
		lock.unlock();
	}
}

/** @param surface cast=(cairo_surface_t *) */
public static final native void _cairo_surface_mark_dirty(int /*long*/ surface);
public static final void cairo_surface_mark_dirty(int /*long*/ surface) {
	lock.lock();
	try {
		_cairo_surface_mark_dirty(surface);
	} finally {
		lock.unlock();
	}
}
/** @param surface cast=(cairo_surface_t *) */
public static final native void _cairo_surface_reference(int /*long*/ surface);
public static final void cairo_surface_reference(int /*long*/ surface) {
	lock.lock();
	try {
		_cairo_surface_reference(surface);
	} finally {
		lock.unlock();
	}
}
/** @param surface cast=(cairo_surface_t *) */
public static final native void _cairo_surface_set_device_offset(int /*long*/ surface, double x_offset, double y_offset);
public static final void cairo_surface_set_device_offset(int /*long*/ surface, double x_offset, double y_offset) {
	lock.lock();
	try {
		_cairo_surface_set_device_offset(surface, x_offset, y_offset);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _cairo_surface_set_fallback_resolution(int /*long*/ surface, double x_pixels_per_inch, double y_pixels_per_inch);
public static final void cairo_surface_set_fallback_resolution(int /*long*/ surface, double x_pixels_per_inch, double y_pixels_per_inch) {
	lock.lock();
	try {
		_cairo_surface_set_fallback_resolution(surface, x_pixels_per_inch, y_pixels_per_inch);
	} finally {
		lock.unlock();
	}
}
/**
 * @param surface cast=(cairo_surface_t *)
 * @param key cast=(cairo_user_data_key_t *)
 * @param user_data cast=(void *)
 * @param destroy cast=(cairo_destroy_func_t)
 */
public static final native int _cairo_surface_set_user_data(int /*long*/ surface, int /*long*/ key, int /*long*/ user_data, int /*long*/ destroy);
public static final int cairo_surface_set_user_data(int /*long*/ surface, int /*long*/ key, int /*long*/ user_data, int /*long*/ destroy) {
	lock.lock();
	try {
		return _cairo_surface_set_user_data(surface, key, user_data, destroy);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param utf8 cast=(const char *)
 * @param extents cast=(cairo_text_extents_t *)
 */
public static final native void _cairo_text_extents(int /*long*/ cr, byte[] utf8, cairo_text_extents_t extents);
public static final void cairo_text_extents(int /*long*/ cr, byte[] utf8, cairo_text_extents_t extents) {
	lock.lock();
	try {
		_cairo_text_extents(cr, utf8, extents);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param utf8 cast=(const char *)
 */
public static final native void _cairo_text_path(int /*long*/ cr, byte[] utf8);
public static final void cairo_text_path(int /*long*/ cr, byte[] utf8) {
	lock.lock();
	try {
		_cairo_text_path(cr, utf8);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cr cast=(cairo_t *)
 * @param matrix cast=(cairo_matrix_t *)
 */
public static final native void _cairo_transform(int /*long*/ cr, double[] matrix);
public static final void cairo_transform(int /*long*/ cr, double[] matrix) {
	lock.lock();
	try {
		_cairo_transform(cr, matrix);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_translate(int /*long*/ cr, double tx, double ty);
public static final void cairo_translate(int /*long*/ cr, double tx, double ty) {
	lock.lock();
	try {
		_cairo_translate(cr, tx, ty);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_user_to_device(int /*long*/ cr, double[] x, double[] y);
public static final void cairo_user_to_device(int /*long*/ cr, double[] x, double[] y) {
	lock.lock();
	try {
		_cairo_user_to_device(cr, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param cr cast=(cairo_t *) */
public static final native void _cairo_user_to_device_distance(int /*long*/ cr, double[] dx, double[] dy);
public static final void cairo_user_to_device_distance(int /*long*/ cr, double[] dx, double[] dy) {
	lock.lock();
	try {
		_cairo_user_to_device_distance(cr, dx, dy);
	} finally {
		lock.unlock();
	}
}
public static final native int cairo_version();
/**
 * @param dpy cast=(Display *)
 * @param drawable cast=(Drawable)
 * @param visual cast=(Visual *)
 */
public static final native int /*long*/ _cairo_xlib_surface_create(int /*long*/ dpy, int /*long*/ drawable, int /*long*/ visual, int width, int height);
public static final int /*long*/ cairo_xlib_surface_create(int /*long*/ dpy, int /*long*/ drawable, int /*long*/ visual, int width, int height) {
	lock.lock();
	try {
		return _cairo_xlib_surface_create(dpy, drawable, visual, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param dpy cast=(Display *)
 * @param pixmap cast=(Pixmap)
 * @param screen cast=(Screen *)
 */
public static final native int /*long*/ _cairo_xlib_surface_create_for_bitmap(int /*long*/ dpy, int /*long*/ pixmap, int /*long*/ screen, int width, int height);
public static final int /*long*/ cairo_xlib_surface_create_for_bitmap(int /*long*/ dpy, int /*long*/ pixmap, int /*long*/ screen, int width, int height) {
	lock.lock();
	try {
		return _cairo_xlib_surface_create_for_bitmap(dpy, pixmap, screen, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param surface cast=(cairo_surface_t *) */
public static final native void _cairo_xlib_surface_set_size(int /*long*/ surface, int width, int height);
public static final void cairo_xlib_surface_set_size(int /*long*/ surface, int width, int height) {
	lock.lock();
	try {
		_cairo_xlib_surface_set_size(surface, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int /*long*/ _cairo_xlib_surface_get_drawable(int /*long*/ surface);
public static final int /*long*/ cairo_xlib_surface_get_drawable(int /*long*/ surface) {
	lock.lock();
	try {
		return _cairo_xlib_surface_get_drawable(surface);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int _cairo_xlib_surface_get_height(int /*long*/ surface);
public static final int cairo_xlib_surface_get_height(int /*long*/ surface) {
	lock.lock();
	try {
		return _cairo_xlib_surface_get_height(surface);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param surface cast=(cairo_surface_t *)
 */
public static final native int _cairo_xlib_surface_get_width(int /*long*/ surface);
public static final int cairo_xlib_surface_get_width(int /*long*/ surface) {
	lock.lock();
	try {
		return _cairo_xlib_surface_get_width(surface);
	} finally {
		lock.unlock();
	}
}
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(cairo_path_t dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(cairo_path_data_t dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(double[] dest, int /*long*/ src, int /*long*/ size);

}

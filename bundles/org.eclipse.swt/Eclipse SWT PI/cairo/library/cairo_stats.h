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
 * -  Copyright (C) 2005, 2012 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */

#ifdef NATIVE_STATS
extern int Cairo_nativeFunctionCount;
extern int Cairo_nativeFunctionCallCount[];
extern char* Cairo_nativeFunctionNames[];
#define Cairo_NATIVE_ENTER(env, that, func) Cairo_nativeFunctionCallCount[func]++;
#define Cairo_NATIVE_EXIT(env, that, func) 
#else
#ifndef Cairo_NATIVE_ENTER
#define Cairo_NATIVE_ENTER(env, that, func) 
#endif
#ifndef Cairo_NATIVE_EXIT
#define Cairo_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	CAIRO_1VERSION_1ENCODE_FUNC,
	_1cairo_1append_1path_FUNC,
	_1cairo_1arc_FUNC,
	_1cairo_1arc_1negative_FUNC,
	_1cairo_1clip_FUNC,
	_1cairo_1clip_1preserve_FUNC,
	_1cairo_1close_1path_FUNC,
	_1cairo_1copy_1page_FUNC,
	_1cairo_1copy_1path_FUNC,
	_1cairo_1copy_1path_1flat_FUNC,
	_1cairo_1create_FUNC,
	_1cairo_1curve_1to_FUNC,
	_1cairo_1destroy_FUNC,
	_1cairo_1device_1to_1user_FUNC,
	_1cairo_1device_1to_1user_1distance_FUNC,
	_1cairo_1fill_FUNC,
	_1cairo_1fill_1extents_FUNC,
	_1cairo_1fill_1preserve_FUNC,
	_1cairo_1font_1extents_FUNC,
	_1cairo_1font_1options_1create_FUNC,
	_1cairo_1font_1options_1destroy_FUNC,
	_1cairo_1font_1options_1get_1antialias_FUNC,
	_1cairo_1font_1options_1set_1antialias_FUNC,
	_1cairo_1format_1stride_1for_1width_FUNC,
	_1cairo_1get_1antialias_FUNC,
	_1cairo_1get_1current_1point_FUNC,
	_1cairo_1get_1fill_1rule_FUNC,
	_1cairo_1get_1font_1face_FUNC,
	_1cairo_1get_1font_1matrix_FUNC,
	_1cairo_1get_1font_1options_FUNC,
	_1cairo_1get_1line_1cap_FUNC,
	_1cairo_1get_1line_1join_FUNC,
	_1cairo_1get_1line_1width_FUNC,
	_1cairo_1get_1matrix_FUNC,
	_1cairo_1get_1miter_1limit_FUNC,
	_1cairo_1get_1operator_FUNC,
	_1cairo_1get_1source_FUNC,
	_1cairo_1get_1target_FUNC,
	_1cairo_1get_1tolerance_FUNC,
	_1cairo_1glyph_1extents_FUNC,
	_1cairo_1glyph_1path_FUNC,
	_1cairo_1identity_1matrix_FUNC,
	_1cairo_1image_1surface_1create_FUNC,
	_1cairo_1image_1surface_1create_1for_1data_FUNC,
	_1cairo_1image_1surface_1get_1data_FUNC,
	_1cairo_1image_1surface_1get_1format_FUNC,
	_1cairo_1image_1surface_1get_1height_FUNC,
	_1cairo_1image_1surface_1get_1stride_FUNC,
	_1cairo_1image_1surface_1get_1width_FUNC,
	_1cairo_1in_1fill_FUNC,
	_1cairo_1in_1stroke_FUNC,
	_1cairo_1line_1to_FUNC,
	_1cairo_1mask_FUNC,
	_1cairo_1mask_1surface_FUNC,
	_1cairo_1matrix_1init_FUNC,
	_1cairo_1matrix_1init_1identity_FUNC,
	_1cairo_1matrix_1init_1rotate_FUNC,
	_1cairo_1matrix_1init_1scale_FUNC,
	_1cairo_1matrix_1init_1translate_FUNC,
	_1cairo_1matrix_1invert_FUNC,
	_1cairo_1matrix_1multiply_FUNC,
	_1cairo_1matrix_1rotate_FUNC,
	_1cairo_1matrix_1scale_FUNC,
	_1cairo_1matrix_1transform_1distance_FUNC,
	_1cairo_1matrix_1transform_1point_FUNC,
	_1cairo_1matrix_1translate_FUNC,
	_1cairo_1move_1to_FUNC,
	_1cairo_1new_1path_FUNC,
	_1cairo_1paint_FUNC,
	_1cairo_1paint_1with_1alpha_FUNC,
	_1cairo_1path_1destroy_FUNC,
	_1cairo_1pattern_1add_1color_1stop_1rgb_FUNC,
	_1cairo_1pattern_1add_1color_1stop_1rgba_FUNC,
	_1cairo_1pattern_1create_1for_1surface_FUNC,
	_1cairo_1pattern_1create_1linear_FUNC,
	_1cairo_1pattern_1create_1radial_FUNC,
	_1cairo_1pattern_1destroy_FUNC,
	_1cairo_1pattern_1get_1extend_FUNC,
	_1cairo_1pattern_1get_1filter_FUNC,
	_1cairo_1pattern_1get_1matrix_FUNC,
	_1cairo_1pattern_1reference_FUNC,
	_1cairo_1pattern_1set_1extend_FUNC,
	_1cairo_1pattern_1set_1filter_FUNC,
	_1cairo_1pattern_1set_1matrix_FUNC,
	_1cairo_1pdf_1surface_1set_1size_FUNC,
	_1cairo_1pop_1group_1to_1source_FUNC,
	_1cairo_1ps_1surface_1set_1size_FUNC,
	_1cairo_1push_1group_FUNC,
	_1cairo_1rectangle_FUNC,
	_1cairo_1reference_FUNC,
	_1cairo_1rel_1curve_1to_FUNC,
	_1cairo_1rel_1line_1to_FUNC,
	_1cairo_1rel_1move_1to_FUNC,
	_1cairo_1reset_1clip_FUNC,
	_1cairo_1restore_FUNC,
	_1cairo_1rotate_FUNC,
	_1cairo_1save_FUNC,
	_1cairo_1scale_FUNC,
	_1cairo_1select_1font_1face_FUNC,
	_1cairo_1set_1antialias_FUNC,
	_1cairo_1set_1dash_FUNC,
	_1cairo_1set_1fill_1rule_FUNC,
	_1cairo_1set_1font_1face_FUNC,
	_1cairo_1set_1font_1matrix_FUNC,
	_1cairo_1set_1font_1options_FUNC,
	_1cairo_1set_1font_1size_FUNC,
	_1cairo_1set_1line_1cap_FUNC,
	_1cairo_1set_1line_1join_FUNC,
	_1cairo_1set_1line_1width_FUNC,
	_1cairo_1set_1matrix_FUNC,
	_1cairo_1set_1miter_1limit_FUNC,
	_1cairo_1set_1operator_FUNC,
	_1cairo_1set_1source_FUNC,
	_1cairo_1set_1source_1rgb_FUNC,
	_1cairo_1set_1source_1rgba_FUNC,
	_1cairo_1set_1source_1surface_FUNC,
	_1cairo_1set_1tolerance_FUNC,
	_1cairo_1show_1glyphs_FUNC,
	_1cairo_1show_1page_FUNC,
	_1cairo_1show_1text_FUNC,
	_1cairo_1status_FUNC,
	_1cairo_1status_1to_1string_FUNC,
	_1cairo_1stroke_FUNC,
	_1cairo_1stroke_1extents_FUNC,
	_1cairo_1stroke_1preserve_FUNC,
	_1cairo_1surface_1create_1similar_FUNC,
	_1cairo_1surface_1destroy_FUNC,
	_1cairo_1surface_1finish_FUNC,
	_1cairo_1surface_1flush_FUNC,
	_1cairo_1surface_1get_1content_FUNC,
	_1cairo_1surface_1get_1type_FUNC,
	_1cairo_1surface_1get_1user_1data_FUNC,
	_1cairo_1surface_1mark_1dirty_FUNC,
	_1cairo_1surface_1reference_FUNC,
	_1cairo_1surface_1set_1device_1offset_FUNC,
	_1cairo_1surface_1set_1fallback_1resolution_FUNC,
	_1cairo_1surface_1set_1user_1data_FUNC,
	_1cairo_1text_1extents_FUNC,
	_1cairo_1text_1path_FUNC,
	_1cairo_1transform_FUNC,
	_1cairo_1translate_FUNC,
	_1cairo_1user_1to_1device_FUNC,
	_1cairo_1user_1to_1device_1distance_FUNC,
	_1cairo_1xlib_1surface_1create_FUNC,
	_1cairo_1xlib_1surface_1create_1for_1bitmap_FUNC,
	_1cairo_1xlib_1surface_1get_1drawable_FUNC,
	_1cairo_1xlib_1surface_1get_1height_FUNC,
	_1cairo_1xlib_1surface_1get_1width_FUNC,
	_1cairo_1xlib_1surface_1set_1size_FUNC,
	cairo_1font_1extents_1t_1sizeof_FUNC,
	cairo_1path_1data_1t_1sizeof_FUNC,
	cairo_1path_1t_1sizeof_FUNC,
	cairo_1text_1extents_1t_1sizeof_FUNC,
	cairo_1version_FUNC,
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2JJ_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2JJ_FUNC,
#endif
#ifndef JNI64
	memmove___3DII_FUNC,
#else
	memmove___3DJJ_FUNC,
#endif
} Cairo_FUNCS;

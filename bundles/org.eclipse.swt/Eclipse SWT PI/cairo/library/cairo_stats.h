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

#ifdef NATIVE_STATS
extern int Cairo_nativeFunctionCount;
extern int Cairo_nativeFunctionCallCount[];
extern char* Cairo_nativeFunctionNames[];
#define Cairo_NATIVE_ENTER(env, that, func) Cairo_nativeFunctionCallCount[func]++;
#define Cairo_NATIVE_EXIT(env, that, func) 
#else
#define Cairo_NATIVE_ENTER(env, that, func) 
#define Cairo_NATIVE_EXIT(env, that, func) 
#endif

typedef enum {
	cairo_1add_1path_FUNC,
	cairo_1arc_FUNC,
	cairo_1arc_1negative_FUNC,
	cairo_1clip_FUNC,
	cairo_1close_1path_FUNC,
	cairo_1concat_1matrix_FUNC,
	cairo_1copy_FUNC,
	cairo_1copy_1page_FUNC,
	cairo_1create_FUNC,
	cairo_1current_1alpha_FUNC,
	cairo_1current_1fill_1rule_FUNC,
	cairo_1current_1font_FUNC,
	cairo_1current_1font_1extents_FUNC,
	cairo_1current_1line_1cap_FUNC,
	cairo_1current_1line_1join_FUNC,
	cairo_1current_1line_1width_FUNC,
	cairo_1current_1matrix_FUNC,
	cairo_1current_1miter_1limit_FUNC,
	cairo_1current_1operator_FUNC,
	cairo_1current_1path_FUNC,
	cairo_1current_1path_1flat_FUNC,
	cairo_1current_1pattern_FUNC,
	cairo_1current_1point_FUNC,
	cairo_1current_1rgb_1color_FUNC,
	cairo_1current_1target_1surface_FUNC,
	cairo_1current_1tolerance_FUNC,
	cairo_1curve_1to_FUNC,
	cairo_1default_1matrix_FUNC,
	cairo_1destroy_FUNC,
	cairo_1extents_FUNC,
	cairo_1fill_FUNC,
	cairo_1fill_1extents_FUNC,
	cairo_1font_1destroy_FUNC,
	cairo_1font_1extents_1t_1sizeof_FUNC,
	cairo_1font_1reference_FUNC,
	cairo_1glyph_1extents_FUNC,
	cairo_1glyph_1path_FUNC,
	cairo_1identity_1matrix_FUNC,
	cairo_1image_1surface_1create_FUNC,
	cairo_1image_1surface_1create_1for_1data_FUNC,
	cairo_1in_1fill_FUNC,
	cairo_1in_1stroke_FUNC,
	cairo_1init_1clip_FUNC,
	cairo_1inverse_1transform_1distance_FUNC,
	cairo_1inverse_1transform_1point_FUNC,
	cairo_1line_1to_FUNC,
	cairo_1matrix_1copy_FUNC,
	cairo_1matrix_1create_FUNC,
	cairo_1matrix_1destroy_FUNC,
	cairo_1matrix_1get_1affine_FUNC,
	cairo_1matrix_1invert_FUNC,
	cairo_1matrix_1multiply_FUNC,
	cairo_1matrix_1rotate_FUNC,
	cairo_1matrix_1scale_FUNC,
	cairo_1matrix_1set_1affine_FUNC,
	cairo_1matrix_1set_1identity_FUNC,
	cairo_1matrix_1transform_1distance_FUNC,
	cairo_1matrix_1transform_1point_FUNC,
	cairo_1matrix_1translate_FUNC,
	cairo_1move_1to_FUNC,
	cairo_1new_1path_FUNC,
	cairo_1pattern_1add_1color_1stop_FUNC,
	cairo_1pattern_1create_1for_1surface_FUNC,
	cairo_1pattern_1create_1linear_FUNC,
	cairo_1pattern_1create_1radial_FUNC,
	cairo_1pattern_1destroy_FUNC,
	cairo_1pattern_1get_1extend_FUNC,
	cairo_1pattern_1get_1filter_FUNC,
	cairo_1pattern_1get_1matrix_FUNC,
	cairo_1pattern_1reference_FUNC,
	cairo_1pattern_1set_1extend_FUNC,
	cairo_1pattern_1set_1filter_FUNC,
	cairo_1pattern_1set_1matrix_FUNC,
	cairo_1points_FUNC,
	cairo_1rectangle_FUNC,
	cairo_1reference_FUNC,
	cairo_1rel_1curve_1to_FUNC,
	cairo_1rel_1line_1to_FUNC,
	cairo_1rel_1move_1to_FUNC,
	cairo_1restore_FUNC,
	cairo_1rotate_FUNC,
	cairo_1save_FUNC,
	cairo_1scale_FUNC,
	cairo_1scale_1font_FUNC,
	cairo_1select_1font_FUNC,
	cairo_1set_1alpha_FUNC,
	cairo_1set_1dash_FUNC,
	cairo_1set_1fill_1rule_FUNC,
	cairo_1set_1font_FUNC,
	cairo_1set_1line_1cap_FUNC,
	cairo_1set_1line_1join_FUNC,
	cairo_1set_1line_1width_FUNC,
	cairo_1set_1matrix_FUNC,
	cairo_1set_1miter_1limit_FUNC,
	cairo_1set_1operator_FUNC,
	cairo_1set_1pattern_FUNC,
	cairo_1set_1rgb_1color_FUNC,
	cairo_1set_1target_1drawable_FUNC,
	cairo_1set_1target_1image_FUNC,
	cairo_1set_1target_1surface_FUNC,
	cairo_1set_1tolerance_FUNC,
	cairo_1show_1glyphs_FUNC,
	cairo_1show_1page_FUNC,
	cairo_1show_1surface_FUNC,
	cairo_1show_1text_FUNC,
	cairo_1status_FUNC,
	cairo_1status_1string_FUNC,
	cairo_1stroke_FUNC,
	cairo_1stroke_1extents_FUNC,
	cairo_1surface_1create_1for_1image_FUNC,
	cairo_1surface_1create_1similar_FUNC,
	cairo_1surface_1destroy_FUNC,
	cairo_1surface_1get_1filter_FUNC,
	cairo_1surface_1get_1matrix_FUNC,
	cairo_1surface_1reference_FUNC,
	cairo_1surface_1set_1filter_FUNC,
	cairo_1surface_1set_1matrix_FUNC,
	cairo_1surface_1set_1repeat_FUNC,
	cairo_1text_1extents_FUNC,
	cairo_1text_1path_FUNC,
	cairo_1transform_1distance_FUNC,
	cairo_1transform_1font_FUNC,
	cairo_1transform_1point_FUNC,
	cairo_1translate_FUNC,
	cairo_1xlib_1surface_1create_FUNC,
} Cairo_FUNCS;

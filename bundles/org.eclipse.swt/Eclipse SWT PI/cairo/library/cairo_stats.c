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

#include "swt.h"
#include "cairo_stats.h"

#ifdef NATIVE_STATS

char * Cairo_nativeFunctionNames[] = {
	"CAIRO_1VERSION_1ENCODE",
	"_1cairo_1append_1path",
	"_1cairo_1arc",
	"_1cairo_1arc_1negative",
	"_1cairo_1clip",
	"_1cairo_1clip_1preserve",
	"_1cairo_1close_1path",
	"_1cairo_1copy_1page",
	"_1cairo_1copy_1path",
	"_1cairo_1copy_1path_1flat",
	"_1cairo_1create",
	"_1cairo_1curve_1to",
	"_1cairo_1destroy",
	"_1cairo_1device_1to_1user",
	"_1cairo_1device_1to_1user_1distance",
	"_1cairo_1fill",
	"_1cairo_1fill_1extents",
	"_1cairo_1fill_1preserve",
	"_1cairo_1font_1extents",
	"_1cairo_1font_1options_1create",
	"_1cairo_1font_1options_1destroy",
	"_1cairo_1font_1options_1get_1antialias",
	"_1cairo_1font_1options_1set_1antialias",
	"_1cairo_1format_1stride_1for_1width",
	"_1cairo_1get_1antialias",
	"_1cairo_1get_1current_1point",
	"_1cairo_1get_1fill_1rule",
	"_1cairo_1get_1font_1face",
	"_1cairo_1get_1font_1matrix",
	"_1cairo_1get_1font_1options",
	"_1cairo_1get_1line_1cap",
	"_1cairo_1get_1line_1join",
	"_1cairo_1get_1line_1width",
	"_1cairo_1get_1matrix",
	"_1cairo_1get_1miter_1limit",
	"_1cairo_1get_1operator",
	"_1cairo_1get_1source",
	"_1cairo_1get_1target",
	"_1cairo_1get_1tolerance",
	"_1cairo_1glyph_1extents",
	"_1cairo_1glyph_1path",
	"_1cairo_1identity_1matrix",
	"_1cairo_1image_1surface_1create",
	"_1cairo_1image_1surface_1create_1for_1data",
	"_1cairo_1image_1surface_1get_1data",
	"_1cairo_1image_1surface_1get_1format",
	"_1cairo_1image_1surface_1get_1height",
	"_1cairo_1image_1surface_1get_1stride",
	"_1cairo_1image_1surface_1get_1width",
	"_1cairo_1in_1fill",
	"_1cairo_1in_1stroke",
	"_1cairo_1line_1to",
	"_1cairo_1mask",
	"_1cairo_1mask_1surface",
	"_1cairo_1matrix_1init",
	"_1cairo_1matrix_1init_1identity",
	"_1cairo_1matrix_1init_1rotate",
	"_1cairo_1matrix_1init_1scale",
	"_1cairo_1matrix_1init_1translate",
	"_1cairo_1matrix_1invert",
	"_1cairo_1matrix_1multiply",
	"_1cairo_1matrix_1rotate",
	"_1cairo_1matrix_1scale",
	"_1cairo_1matrix_1transform_1distance",
	"_1cairo_1matrix_1transform_1point",
	"_1cairo_1matrix_1translate",
	"_1cairo_1move_1to",
	"_1cairo_1new_1path",
	"_1cairo_1paint",
	"_1cairo_1paint_1with_1alpha",
	"_1cairo_1path_1destroy",
	"_1cairo_1pattern_1add_1color_1stop_1rgb",
	"_1cairo_1pattern_1add_1color_1stop_1rgba",
	"_1cairo_1pattern_1create_1for_1surface",
	"_1cairo_1pattern_1create_1linear",
	"_1cairo_1pattern_1create_1radial",
	"_1cairo_1pattern_1destroy",
	"_1cairo_1pattern_1get_1extend",
	"_1cairo_1pattern_1get_1filter",
	"_1cairo_1pattern_1get_1matrix",
	"_1cairo_1pattern_1reference",
	"_1cairo_1pattern_1set_1extend",
	"_1cairo_1pattern_1set_1filter",
	"_1cairo_1pattern_1set_1matrix",
	"_1cairo_1pdf_1surface_1set_1size",
	"_1cairo_1pop_1group_1to_1source",
	"_1cairo_1ps_1surface_1set_1size",
	"_1cairo_1push_1group",
	"_1cairo_1rectangle",
	"_1cairo_1reference",
	"_1cairo_1region_1get_1rectangle",
	"_1cairo_1region_1num_1rectangles",
	"_1cairo_1rel_1curve_1to",
	"_1cairo_1rel_1line_1to",
	"_1cairo_1rel_1move_1to",
	"_1cairo_1reset_1clip",
	"_1cairo_1restore",
	"_1cairo_1rotate",
	"_1cairo_1save",
	"_1cairo_1scale",
	"_1cairo_1select_1font_1face",
	"_1cairo_1set_1antialias",
	"_1cairo_1set_1dash",
	"_1cairo_1set_1fill_1rule",
	"_1cairo_1set_1font_1face",
	"_1cairo_1set_1font_1matrix",
	"_1cairo_1set_1font_1options",
	"_1cairo_1set_1font_1size",
	"_1cairo_1set_1line_1cap",
	"_1cairo_1set_1line_1join",
	"_1cairo_1set_1line_1width",
	"_1cairo_1set_1matrix",
	"_1cairo_1set_1miter_1limit",
	"_1cairo_1set_1operator",
	"_1cairo_1set_1source",
	"_1cairo_1set_1source_1rgb",
	"_1cairo_1set_1source_1rgba",
	"_1cairo_1set_1source_1surface",
	"_1cairo_1set_1tolerance",
	"_1cairo_1show_1glyphs",
	"_1cairo_1show_1page",
	"_1cairo_1show_1text",
	"_1cairo_1status",
	"_1cairo_1status_1to_1string",
	"_1cairo_1stroke",
	"_1cairo_1stroke_1extents",
	"_1cairo_1stroke_1preserve",
	"_1cairo_1surface_1create_1similar",
	"_1cairo_1surface_1destroy",
	"_1cairo_1surface_1finish",
	"_1cairo_1surface_1flush",
	"_1cairo_1surface_1get_1content",
	"_1cairo_1surface_1get_1type",
	"_1cairo_1surface_1get_1user_1data",
	"_1cairo_1surface_1mark_1dirty",
	"_1cairo_1surface_1reference",
	"_1cairo_1surface_1set_1device_1offset",
	"_1cairo_1surface_1set_1fallback_1resolution",
	"_1cairo_1surface_1set_1user_1data",
	"_1cairo_1text_1extents",
	"_1cairo_1text_1path",
	"_1cairo_1transform",
	"_1cairo_1translate",
	"_1cairo_1user_1to_1device",
	"_1cairo_1user_1to_1device_1distance",
	"_1cairo_1xlib_1surface_1create",
	"_1cairo_1xlib_1surface_1create_1for_1bitmap",
	"_1cairo_1xlib_1surface_1get_1drawable",
	"_1cairo_1xlib_1surface_1get_1height",
	"_1cairo_1xlib_1surface_1get_1width",
	"_1cairo_1xlib_1surface_1set_1size",
	"cairo_1font_1extents_1t_1sizeof",
	"cairo_1path_1data_1t_1sizeof",
	"cairo_1path_1t_1sizeof",
	"cairo_1text_1extents_1t_1sizeof",
	"cairo_1version",
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2JJ",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2JJ",
#endif
#ifndef JNI64
	"memmove___3DII",
#else
	"memmove___3DJJ",
#endif
};
#define NATIVE_FUNCTION_COUNT sizeof(Cairo_nativeFunctionNames) / sizeof(char*)
int Cairo_nativeFunctionCount = NATIVE_FUNCTION_COUNT;
int Cairo_nativeFunctionCallCount[NATIVE_FUNCTION_COUNT];

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(Cairo_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return Cairo_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(Cairo_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, Cairo_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(Cairo_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return Cairo_nativeFunctionCallCount[index];
}

#endif

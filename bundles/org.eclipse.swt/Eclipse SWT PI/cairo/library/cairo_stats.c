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
 * -  Copyright (C) 2005, 2006 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */

#include "swt.h"
#include "cairo_stats.h"

#ifdef NATIVE_STATS

int Cairo_nativeFunctionCount = 143;
int Cairo_nativeFunctionCallCount[143];
char * Cairo_nativeFunctionNames[] = {
	"cairo_1append_1path",
	"cairo_1arc",
	"cairo_1arc_1negative",
	"cairo_1clip",
	"cairo_1clip_1preserve",
	"cairo_1close_1path",
	"cairo_1copy_1page",
	"cairo_1copy_1path",
	"cairo_1copy_1path_1flat",
	"cairo_1create",
	"cairo_1curve_1to",
	"cairo_1destroy",
	"cairo_1device_1to_1user",
	"cairo_1device_1to_1user_1distance",
	"cairo_1fill",
	"cairo_1fill_1extents",
	"cairo_1fill_1preserve",
	"cairo_1font_1extents",
	"cairo_1font_1extents_1t_1sizeof",
	"cairo_1font_1options_1create",
	"cairo_1font_1options_1destroy",
	"cairo_1font_1options_1get_1antialias",
	"cairo_1font_1options_1set_1antialias",
	"cairo_1get_1antialias",
	"cairo_1get_1current_1point",
	"cairo_1get_1fill_1rule",
	"cairo_1get_1font_1face",
	"cairo_1get_1font_1matrix",
	"cairo_1get_1font_1options",
	"cairo_1get_1line_1cap",
	"cairo_1get_1line_1join",
	"cairo_1get_1line_1width",
	"cairo_1get_1matrix",
	"cairo_1get_1miter_1limit",
	"cairo_1get_1operator",
	"cairo_1get_1source",
	"cairo_1get_1target",
	"cairo_1get_1tolerance",
	"cairo_1glyph_1extents",
	"cairo_1glyph_1path",
	"cairo_1identity_1matrix",
	"cairo_1image_1surface_1create",
	"cairo_1image_1surface_1create_1for_1data",
	"cairo_1image_1surface_1get_1height",
	"cairo_1image_1surface_1get_1width",
	"cairo_1in_1fill",
	"cairo_1in_1stroke",
	"cairo_1line_1to",
	"cairo_1mask",
	"cairo_1mask_1surface",
	"cairo_1matrix_1init",
	"cairo_1matrix_1init_1identity",
	"cairo_1matrix_1init_1rotate",
	"cairo_1matrix_1init_1scale",
	"cairo_1matrix_1init_1translate",
	"cairo_1matrix_1invert",
	"cairo_1matrix_1multiply",
	"cairo_1matrix_1rotate",
	"cairo_1matrix_1scale",
	"cairo_1matrix_1transform_1distance",
	"cairo_1matrix_1transform_1point",
	"cairo_1matrix_1translate",
	"cairo_1move_1to",
	"cairo_1new_1path",
	"cairo_1paint",
	"cairo_1paint_1with_1alpha",
	"cairo_1path_1data_1t_1sizeof",
	"cairo_1path_1destroy",
	"cairo_1path_1t_1sizeof",
	"cairo_1pattern_1add_1color_1stop_1rgb",
	"cairo_1pattern_1add_1color_1stop_1rgba",
	"cairo_1pattern_1create_1for_1surface",
	"cairo_1pattern_1create_1linear",
	"cairo_1pattern_1create_1radial",
	"cairo_1pattern_1destroy",
	"cairo_1pattern_1get_1extend",
	"cairo_1pattern_1get_1filter",
	"cairo_1pattern_1get_1matrix",
	"cairo_1pattern_1reference",
	"cairo_1pattern_1set_1extend",
	"cairo_1pattern_1set_1filter",
	"cairo_1pattern_1set_1matrix",
	"cairo_1pdf_1surface_1set_1size",
	"cairo_1ps_1surface_1set_1size",
	"cairo_1rectangle",
	"cairo_1reference",
	"cairo_1rel_1curve_1to",
	"cairo_1rel_1line_1to",
	"cairo_1rel_1move_1to",
	"cairo_1reset_1clip",
	"cairo_1restore",
	"cairo_1rotate",
	"cairo_1save",
	"cairo_1scale",
	"cairo_1select_1font_1face",
	"cairo_1set_1antialias",
	"cairo_1set_1dash",
	"cairo_1set_1fill_1rule",
	"cairo_1set_1font_1face",
	"cairo_1set_1font_1matrix",
	"cairo_1set_1font_1options",
	"cairo_1set_1font_1size",
	"cairo_1set_1line_1cap",
	"cairo_1set_1line_1join",
	"cairo_1set_1line_1width",
	"cairo_1set_1matrix",
	"cairo_1set_1miter_1limit",
	"cairo_1set_1operator",
	"cairo_1set_1source",
	"cairo_1set_1source_1rgb",
	"cairo_1set_1source_1rgba",
	"cairo_1set_1source_1surface",
	"cairo_1set_1tolerance",
	"cairo_1show_1glyphs",
	"cairo_1show_1page",
	"cairo_1show_1text",
	"cairo_1status",
	"cairo_1status_1to_1string",
	"cairo_1stroke",
	"cairo_1stroke_1extents",
	"cairo_1stroke_1preserve",
	"cairo_1surface_1create_1similar",
	"cairo_1surface_1destroy",
	"cairo_1surface_1finish",
	"cairo_1surface_1get_1type",
	"cairo_1surface_1get_1user_1data",
	"cairo_1surface_1reference",
	"cairo_1surface_1set_1device_1offset",
	"cairo_1surface_1set_1fallback_1resolution",
	"cairo_1surface_1set_1user_1data",
	"cairo_1text_1extents",
	"cairo_1text_1extents_1t_1sizeof",
	"cairo_1text_1path",
	"cairo_1transform",
	"cairo_1translate",
	"cairo_1user_1to_1device",
	"cairo_1user_1to_1device_1distance",
	"cairo_1xlib_1surface_1create",
	"cairo_1xlib_1surface_1create_1for_1bitmap",
	"cairo_1xlib_1surface_1set_1size",
	"memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2II",
	"memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2II",
	"memmove___3DII",
};

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

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

#include "swt.h"
#include "cairo_stats.h"

#ifdef NATIVE_STATS

int Cairo_nativeFunctionCount = 125;
int Cairo_nativeFunctionCallCount[125];
char * Cairo_nativeFunctionNames[] = {
	"cairo_1add_1path",
	"cairo_1arc",
	"cairo_1arc_1negative",
	"cairo_1clip",
	"cairo_1close_1path",
	"cairo_1concat_1matrix",
	"cairo_1copy",
	"cairo_1copy_1page",
	"cairo_1create",
	"cairo_1current_1alpha",
	"cairo_1current_1fill_1rule",
	"cairo_1current_1font",
	"cairo_1current_1font_1extents",
	"cairo_1current_1line_1cap",
	"cairo_1current_1line_1join",
	"cairo_1current_1line_1width",
	"cairo_1current_1matrix",
	"cairo_1current_1miter_1limit",
	"cairo_1current_1operator",
	"cairo_1current_1path",
	"cairo_1current_1path_1flat",
	"cairo_1current_1pattern",
	"cairo_1current_1point",
	"cairo_1current_1rgb_1color",
	"cairo_1current_1target_1surface",
	"cairo_1current_1tolerance",
	"cairo_1curve_1to",
	"cairo_1default_1matrix",
	"cairo_1destroy",
	"cairo_1extents",
	"cairo_1fill",
	"cairo_1fill_1extents",
	"cairo_1font_1destroy",
	"cairo_1font_1extents_1t_1sizeof",
	"cairo_1font_1reference",
	"cairo_1glyph_1extents",
	"cairo_1glyph_1path",
	"cairo_1identity_1matrix",
	"cairo_1image_1surface_1create",
	"cairo_1image_1surface_1create_1for_1data",
	"cairo_1in_1fill",
	"cairo_1in_1stroke",
	"cairo_1init_1clip",
	"cairo_1inverse_1transform_1distance",
	"cairo_1inverse_1transform_1point",
	"cairo_1line_1to",
	"cairo_1matrix_1copy",
	"cairo_1matrix_1create",
	"cairo_1matrix_1destroy",
	"cairo_1matrix_1get_1affine",
	"cairo_1matrix_1invert",
	"cairo_1matrix_1multiply",
	"cairo_1matrix_1rotate",
	"cairo_1matrix_1scale",
	"cairo_1matrix_1set_1affine",
	"cairo_1matrix_1set_1identity",
	"cairo_1matrix_1transform_1distance",
	"cairo_1matrix_1transform_1point",
	"cairo_1matrix_1translate",
	"cairo_1move_1to",
	"cairo_1new_1path",
	"cairo_1pattern_1add_1color_1stop",
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
	"cairo_1points",
	"cairo_1rectangle",
	"cairo_1reference",
	"cairo_1rel_1curve_1to",
	"cairo_1rel_1line_1to",
	"cairo_1rel_1move_1to",
	"cairo_1restore",
	"cairo_1rotate",
	"cairo_1save",
	"cairo_1scale",
	"cairo_1scale_1font",
	"cairo_1select_1font",
	"cairo_1set_1alpha",
	"cairo_1set_1dash",
	"cairo_1set_1fill_1rule",
	"cairo_1set_1font",
	"cairo_1set_1line_1cap",
	"cairo_1set_1line_1join",
	"cairo_1set_1line_1width",
	"cairo_1set_1matrix",
	"cairo_1set_1miter_1limit",
	"cairo_1set_1operator",
	"cairo_1set_1pattern",
	"cairo_1set_1rgb_1color",
	"cairo_1set_1target_1drawable",
	"cairo_1set_1target_1image",
	"cairo_1set_1target_1surface",
	"cairo_1set_1tolerance",
	"cairo_1show_1glyphs",
	"cairo_1show_1page",
	"cairo_1show_1surface",
	"cairo_1show_1text",
	"cairo_1status",
	"cairo_1status_1string",
	"cairo_1stroke",
	"cairo_1stroke_1extents",
	"cairo_1surface_1create_1for_1image",
	"cairo_1surface_1create_1similar",
	"cairo_1surface_1destroy",
	"cairo_1surface_1get_1filter",
	"cairo_1surface_1get_1matrix",
	"cairo_1surface_1reference",
	"cairo_1surface_1set_1filter",
	"cairo_1surface_1set_1matrix",
	"cairo_1surface_1set_1repeat",
	"cairo_1text_1extents",
	"cairo_1text_1path",
	"cairo_1transform_1distance",
	"cairo_1transform_1font",
	"cairo_1transform_1point",
	"cairo_1translate",
	"cairo_1xlib_1surface_1create",
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

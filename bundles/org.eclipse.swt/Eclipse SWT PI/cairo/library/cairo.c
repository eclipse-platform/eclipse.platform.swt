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
#include "cairo_structs.h"
#include "cairo_stats.h"

#define Cairo_NATIVE(func) Java_org_eclipse_swt_internal_cairo_Cairo_##func

#ifndef NO_cairo_1arc
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1arc)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1arc_FUNC);
	cairo_arc((cairo_t *)arg0, arg1, arg2, arg3, arg4, arg5);
	Cairo_NATIVE_EXIT(env, that, cairo_1arc_FUNC);
}
#endif

#ifndef NO_cairo_1arc_1negative
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1arc_1negative)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1arc_1negative_FUNC);
	cairo_arc_negative((cairo_t *)arg0, arg1, arg2, arg3, arg4, arg5);
	Cairo_NATIVE_EXIT(env, that, cairo_1arc_1negative_FUNC);
}
#endif

#ifndef NO_cairo_1clip
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1clip)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1clip_FUNC);
	cairo_clip((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1clip_FUNC);
}
#endif

#ifndef NO_cairo_1close_1path
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1close_1path)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1close_1path_FUNC);
	cairo_close_path((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1close_1path_FUNC);
}
#endif

#ifndef NO_cairo_1concat_1matrix
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1concat_1matrix)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1concat_1matrix_FUNC);
	cairo_concat_matrix((cairo_t *)arg0, (cairo_matrix_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1concat_1matrix_FUNC);
}
#endif

#ifndef NO_cairo_1copy
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1copy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1copy_FUNC);
	cairo_copy((cairo_t *)arg0, (cairo_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1copy_FUNC);
}
#endif

#ifndef NO_cairo_1copy_1page
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1copy_1page)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1copy_1page_FUNC);
	cairo_copy_page((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1copy_1page_FUNC);
}
#endif

#ifndef NO_cairo_1create
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1create)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1create_FUNC);
	rc = (jint)cairo_create();
	Cairo_NATIVE_EXIT(env, that, cairo_1create_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1current_1alpha
JNIEXPORT jdouble JNICALL Cairo_NATIVE(cairo_1current_1alpha)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1alpha_FUNC);
	rc = (jdouble)cairo_current_alpha((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1alpha_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1current_1fill_1rule
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1current_1fill_1rule)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1fill_1rule_FUNC);
	rc = (jint)cairo_current_fill_rule((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1fill_1rule_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1current_1font
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1current_1font)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1font_FUNC);
	rc = (jint)cairo_current_font((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1font_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1current_1font_1extents
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1current_1font_1extents)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	cairo_font_extents_t _arg1, *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1font_1extents_FUNC);
	if (arg1) if ((lparg1 = getcairo_font_extents_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	cairo_current_font_extents((cairo_t *)arg0, (cairo_font_extents_t *)lparg1);
fail:
	if (arg1 && lparg1) setcairo_font_extents_tFields(env, arg1, lparg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1font_1extents_FUNC);
}
#endif

#ifndef NO_cairo_1current_1line_1cap
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1current_1line_1cap)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1line_1cap_FUNC);
	rc = (jint)cairo_current_line_cap((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1line_1cap_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1current_1line_1join
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1current_1line_1join)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1line_1join_FUNC);
	rc = (jint)cairo_current_line_join((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1line_1join_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1current_1line_1width
JNIEXPORT jdouble JNICALL Cairo_NATIVE(cairo_1current_1line_1width)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1line_1width_FUNC);
	rc = (jdouble)cairo_current_line_width((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1line_1width_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1current_1matrix
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1current_1matrix)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1matrix_FUNC);
	cairo_current_matrix((cairo_t *)arg0, (cairo_matrix_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1matrix_FUNC);
}
#endif

#ifndef NO_cairo_1current_1miter_1limit
JNIEXPORT jdouble JNICALL Cairo_NATIVE(cairo_1current_1miter_1limit)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1miter_1limit_FUNC);
	rc = (jdouble)cairo_current_miter_limit((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1miter_1limit_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1current_1operator
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1current_1operator)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1operator_FUNC);
	rc = (jint)cairo_current_operator((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1operator_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1current_1path
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1current_1path)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1path_FUNC);
	cairo_current_path((cairo_t *)arg0, (cairo_move_to_func_t *)arg1, (cairo_line_to_func_t *)arg2, (cairo_curve_to_func_t *)arg3, (cairo_close_path_func_t *)arg4, (void *)arg5);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1path_FUNC);
}
#endif

#ifndef NO_cairo_1current_1path_1flat
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1current_1path_1flat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1path_1flat_FUNC);
	cairo_current_path_flat((cairo_t *)arg0, (cairo_move_to_func_t *)arg1, (cairo_line_to_func_t *)arg2, (cairo_close_path_func_t *)arg3, (void *)arg4);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1path_1flat_FUNC);
}
#endif

#ifndef NO_cairo_1current_1pattern
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1current_1pattern)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1pattern_FUNC);
	rc = (jint)cairo_current_pattern((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1pattern_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1current_1point
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1current_1point)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1point_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_current_point((cairo_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1point_FUNC);
}
#endif

#ifndef NO_cairo_1current_1rgb_1color
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1current_1rgb_1color)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jdoubleArray arg2, jdoubleArray arg3)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	jdouble *lparg3=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1rgb_1color_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetDoubleArrayElements(env, arg3, NULL)) == NULL) goto fail;
	cairo_current_rgb_color((cairo_t *)arg0, lparg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseDoubleArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1rgb_1color_FUNC);
}
#endif

#ifndef NO_cairo_1current_1target_1surface
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1current_1target_1surface)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1target_1surface_FUNC);
	rc = (jint)cairo_current_target_surface((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1target_1surface_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1current_1tolerance
JNIEXPORT jdouble JNICALL Cairo_NATIVE(cairo_1current_1tolerance)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1current_1tolerance_FUNC);
	rc = (jdouble)cairo_current_tolerance((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1current_1tolerance_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1curve_1to
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1curve_1to)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5, jdouble arg6)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1curve_1to_FUNC);
	cairo_curve_to((cairo_t *)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	Cairo_NATIVE_EXIT(env, that, cairo_1curve_1to_FUNC);
}
#endif

#ifndef NO_cairo_1default_1matrix
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1default_1matrix)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1default_1matrix_FUNC);
	cairo_default_matrix((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1default_1matrix_FUNC);
}
#endif

#ifndef NO_cairo_1destroy
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1destroy_FUNC);
	cairo_destroy((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1destroy_FUNC);
}
#endif

#ifndef NO_cairo_1fill
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1fill)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1fill_FUNC);
	cairo_fill((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1fill_FUNC);
}
#endif

#ifndef NO_cairo_1fill_1extents
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1fill_1extents)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jdoubleArray arg2, jdoubleArray arg3, jdoubleArray arg4)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	jdouble *lparg3=NULL;
	jdouble *lparg4=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1fill_1extents_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetDoubleArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetDoubleArrayElements(env, arg4, NULL)) == NULL) goto fail;
	cairo_fill_extents((cairo_t *)arg0, lparg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseDoubleArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseDoubleArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1fill_1extents_FUNC);
}
#endif

#ifndef NO_cairo_1font_1destroy
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1font_1destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1font_1destroy_FUNC);
	cairo_font_destroy((cairo_font_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1font_1destroy_FUNC);
}
#endif

#ifndef NO_cairo_1font_1extents_1t_1sizeof
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1font_1extents_1t_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1font_1extents_1t_1sizeof_FUNC);
	rc = (jint)cairo_font_extents_t_sizeof();
	Cairo_NATIVE_EXIT(env, that, cairo_1font_1extents_1t_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1font_1reference
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1font_1reference)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1font_1reference_FUNC);
	cairo_font_reference((cairo_font_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1font_1reference_FUNC);
}
#endif

#ifndef NO_cairo_1glyph_1extents
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1glyph_1extents)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1glyph_1extents_FUNC);
	cairo_glyph_extents((cairo_t *)arg0, (cairo_glyph_t *)arg1, arg2, (cairo_text_extents_t *)arg3);
	Cairo_NATIVE_EXIT(env, that, cairo_1glyph_1extents_FUNC);
}
#endif

#ifndef NO_cairo_1glyph_1path
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1glyph_1path)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1glyph_1path_FUNC);
	cairo_glyph_path((cairo_t *)arg0, (cairo_glyph_t *)arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1glyph_1path_FUNC);
}
#endif

#ifndef NO_cairo_1identity_1matrix
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1identity_1matrix)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1identity_1matrix_FUNC);
	cairo_identity_matrix((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1identity_1matrix_FUNC);
}
#endif

#ifndef NO_cairo_1image_1surface_1create
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1image_1surface_1create)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1image_1surface_1create_FUNC);
	rc = (jint)cairo_image_surface_create(arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1image_1surface_1create_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1image_1surface_1create_1for_1data
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1image_1surface_1create_1for_1data)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1image_1surface_1create_1for_1data_FUNC);
	rc = (jint)cairo_image_surface_create_for_data((char *)arg0, arg1, arg2, arg3, arg4);
	Cairo_NATIVE_EXIT(env, that, cairo_1image_1surface_1create_1for_1data_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1in_1fill
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1in_1fill)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1in_1fill_FUNC);
	rc = (jint)cairo_in_fill((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1in_1fill_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1in_1stroke
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1in_1stroke)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1in_1stroke_FUNC);
	rc = (jint)cairo_in_stroke((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1in_1stroke_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1init_1clip
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1init_1clip)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1init_1clip_FUNC);
	cairo_init_clip((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1init_1clip_FUNC);
}
#endif

#ifndef NO_cairo_1inverse_1transform_1distance
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1inverse_1transform_1distance)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1inverse_1transform_1distance_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_inverse_transform_distance((cairo_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1inverse_1transform_1distance_FUNC);
}
#endif

#ifndef NO_cairo_1inverse_1transform_1point
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1inverse_1transform_1point)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1inverse_1transform_1point_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_inverse_transform_point((cairo_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1inverse_1transform_1point_FUNC);
}
#endif

#ifndef NO_cairo_1line_1to
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1line_1to)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1line_1to_FUNC);
	cairo_line_to((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1line_1to_FUNC);
}
#endif

#ifndef NO_cairo_1matrix_1copy
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1matrix_1copy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1copy_FUNC);
	rc = (jint)cairo_matrix_copy((cairo_matrix_t *)arg0, (cairo_matrix_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1copy_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1matrix_1create
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1matrix_1create)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1create_FUNC);
	rc = (jint)cairo_matrix_create();
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1create_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1matrix_1destroy
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1matrix_1destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1destroy_FUNC);
	cairo_matrix_destroy((cairo_matrix_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1destroy_FUNC);
}
#endif

#ifndef NO_cairo_1matrix_1get_1affine
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1matrix_1get_1affine)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jdoubleArray arg2, jdoubleArray arg3, jdoubleArray arg4, jdoubleArray arg5, jdoubleArray arg6)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	jdouble *lparg3=NULL;
	jdouble *lparg4=NULL;
	jdouble *lparg5=NULL;
	jdouble *lparg6=NULL;
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1get_1affine_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetDoubleArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetDoubleArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetDoubleArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetDoubleArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)cairo_matrix_get_affine((cairo_matrix_t *)arg0, lparg1, lparg2, lparg3, lparg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseDoubleArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseDoubleArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseDoubleArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseDoubleArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1get_1affine_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1matrix_1invert
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1matrix_1invert)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1invert_FUNC);
	rc = (jint)cairo_matrix_invert((cairo_matrix_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1invert_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1matrix_1multiply
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1matrix_1multiply)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1multiply_FUNC);
	rc = (jint)cairo_matrix_multiply((cairo_matrix_t *)arg0, (cairo_matrix_t *)arg1, (cairo_matrix_t *)arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1multiply_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1matrix_1rotate
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1matrix_1rotate)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1rotate_FUNC);
	rc = (jint)cairo_matrix_rotate((cairo_matrix_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1rotate_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1matrix_1scale
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1matrix_1scale)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1scale_FUNC);
	rc = (jint)cairo_matrix_scale((cairo_matrix_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1scale_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1matrix_1set_1affine
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1matrix_1set_1affine)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5, jdouble arg6)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1set_1affine_FUNC);
	rc = (jint)cairo_matrix_set_affine((cairo_matrix_t *)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1set_1affine_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1matrix_1set_1identity
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1matrix_1set_1identity)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1set_1identity_FUNC);
	rc = (jint)cairo_matrix_set_identity((cairo_matrix_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1set_1identity_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1matrix_1transform_1distance
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1matrix_1transform_1distance)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1transform_1distance_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)cairo_matrix_transform_distance((cairo_matrix_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1transform_1distance_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1matrix_1transform_1point
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1matrix_1transform_1point)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1transform_1point_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)cairo_matrix_transform_point((cairo_matrix_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1transform_1point_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1matrix_1translate
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1matrix_1translate)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1matrix_1translate_FUNC);
	rc = (jint)cairo_matrix_translate((cairo_matrix_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1matrix_1translate_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1move_1to
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1move_1to)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1move_1to_FUNC);
	cairo_move_to((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1move_1to_FUNC);
}
#endif

#ifndef NO_cairo_1new_1path
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1new_1path)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1new_1path_FUNC);
	cairo_new_path((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1new_1path_FUNC);
}
#endif

#ifndef NO_cairo_1pattern_1add_1color_1stop
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1pattern_1add_1color_1stop)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1pattern_1add_1color_1stop_FUNC);
	rc = (jint)cairo_pattern_add_color_stop((cairo_pattern_t *)arg0, arg1, arg2, arg3, arg4, arg5);
	Cairo_NATIVE_EXIT(env, that, cairo_1pattern_1add_1color_1stop_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1pattern_1create_1for_1surface
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1pattern_1create_1for_1surface)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1pattern_1create_1for_1surface_FUNC);
	rc = (jint)cairo_pattern_create_for_surface((cairo_surface_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1pattern_1create_1for_1surface_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1pattern_1create_1linear
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1pattern_1create_1linear)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1pattern_1create_1linear_FUNC);
	rc = (jint)cairo_pattern_create_linear(arg0, arg1, arg2, arg3);
	Cairo_NATIVE_EXIT(env, that, cairo_1pattern_1create_1linear_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1pattern_1create_1radial
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1pattern_1create_1radial)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1pattern_1create_1radial_FUNC);
	rc = (jint)cairo_pattern_create_radial(arg0, arg1, arg2, arg3, arg4, arg5);
	Cairo_NATIVE_EXIT(env, that, cairo_1pattern_1create_1radial_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1pattern_1destroy
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1pattern_1destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1pattern_1destroy_FUNC);
	cairo_pattern_destroy((cairo_pattern_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1pattern_1destroy_FUNC);
}
#endif

#ifndef NO_cairo_1pattern_1get_1extend
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1pattern_1get_1extend)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1pattern_1get_1extend_FUNC);
	rc = (jint)cairo_pattern_get_extend((cairo_pattern_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1pattern_1get_1extend_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1pattern_1get_1filter
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1pattern_1get_1filter)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1pattern_1get_1filter_FUNC);
	rc = (jint)cairo_pattern_get_filter((cairo_pattern_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1pattern_1get_1filter_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1pattern_1get_1matrix
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1pattern_1get_1matrix)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1pattern_1get_1matrix_FUNC);
	rc = (jint)cairo_pattern_get_matrix((cairo_pattern_t *)arg0, (cairo_matrix_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1pattern_1get_1matrix_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1pattern_1reference
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1pattern_1reference)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1pattern_1reference_FUNC);
	cairo_pattern_reference((cairo_pattern_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1pattern_1reference_FUNC);
}
#endif

#ifndef NO_cairo_1pattern_1set_1extend
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1pattern_1set_1extend)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1pattern_1set_1extend_FUNC);
	rc = (jint)cairo_pattern_set_extend((cairo_pattern_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1pattern_1set_1extend_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1pattern_1set_1filter
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1pattern_1set_1filter)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1pattern_1set_1filter_FUNC);
	rc = (jint)cairo_pattern_set_filter((cairo_pattern_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1pattern_1set_1filter_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1pattern_1set_1matrix
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1pattern_1set_1matrix)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1pattern_1set_1matrix_FUNC);
	rc = (jint)cairo_pattern_set_matrix((cairo_pattern_t *)arg0, (cairo_matrix_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1pattern_1set_1matrix_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1rectangle
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1rectangle)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1rectangle_FUNC);
	cairo_rectangle((cairo_t *)arg0, arg1, arg2, arg3, arg4);
	Cairo_NATIVE_EXIT(env, that, cairo_1rectangle_FUNC);
}
#endif

#ifndef NO_cairo_1reference
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1reference)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1reference_FUNC);
	cairo_reference((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1reference_FUNC);
}
#endif

#ifndef NO_cairo_1rel_1curve_1to
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1rel_1curve_1to)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5, jdouble arg6)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1rel_1curve_1to_FUNC);
	cairo_rel_curve_to((cairo_t *)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	Cairo_NATIVE_EXIT(env, that, cairo_1rel_1curve_1to_FUNC);
}
#endif

#ifndef NO_cairo_1rel_1line_1to
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1rel_1line_1to)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1rel_1line_1to_FUNC);
	cairo_rel_line_to((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1rel_1line_1to_FUNC);
}
#endif

#ifndef NO_cairo_1rel_1move_1to
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1rel_1move_1to)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1rel_1move_1to_FUNC);
	cairo_rel_move_to((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1rel_1move_1to_FUNC);
}
#endif

#ifndef NO_cairo_1restore
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1restore)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1restore_FUNC);
	cairo_restore((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1restore_FUNC);
}
#endif

#ifndef NO_cairo_1rotate
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1rotate)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1rotate_FUNC);
	cairo_rotate((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1rotate_FUNC);
}
#endif

#ifndef NO_cairo_1save
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1save)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1save_FUNC);
	cairo_save((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1save_FUNC);
}
#endif

#ifndef NO_cairo_1scale
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1scale)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1scale_FUNC);
	cairo_scale((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1scale_FUNC);
}
#endif

#ifndef NO_cairo_1scale_1font
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1scale_1font)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1scale_1font_FUNC);
	cairo_scale_font((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1scale_1font_FUNC);
}
#endif

#ifndef NO_cairo_1select_1font
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1select_1font)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1select_1font_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_select_font((cairo_t *)arg0, lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1select_1font_FUNC);
}
#endif

#ifndef NO_cairo_1set_1alpha
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1alpha)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1alpha_FUNC);
	cairo_set_alpha((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1alpha_FUNC);
}
#endif

#ifndef NO_cairo_1set_1dash
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1dash)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jint arg2, jdouble arg3)
{
	jdouble *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1dash_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_set_dash((cairo_t *)arg0, lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1dash_FUNC);
}
#endif

#ifndef NO_cairo_1set_1fill_1rule
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1fill_1rule)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1fill_1rule_FUNC);
	cairo_set_fill_rule((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1fill_1rule_FUNC);
}
#endif

#ifndef NO_cairo_1set_1font
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1font)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1font_FUNC);
	cairo_set_font((cairo_t *)arg0, (cairo_font_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1font_FUNC);
}
#endif

#ifndef NO_cairo_1set_1line_1cap
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1line_1cap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1line_1cap_FUNC);
	cairo_set_line_cap((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1line_1cap_FUNC);
}
#endif

#ifndef NO_cairo_1set_1line_1join
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1line_1join)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1line_1join_FUNC);
	cairo_set_line_join((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1line_1join_FUNC);
}
#endif

#ifndef NO_cairo_1set_1line_1width
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1line_1width)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1line_1width_FUNC);
	cairo_set_line_width((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1line_1width_FUNC);
}
#endif

#ifndef NO_cairo_1set_1matrix
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1matrix)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1matrix_FUNC);
	cairo_set_matrix((cairo_t *)arg0, (cairo_matrix_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1matrix_FUNC);
}
#endif

#ifndef NO_cairo_1set_1miter_1limit
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1miter_1limit)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1miter_1limit_FUNC);
	cairo_set_miter_limit((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1miter_1limit_FUNC);
}
#endif

#ifndef NO_cairo_1set_1operator
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1operator)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1operator_FUNC);
	cairo_set_operator((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1operator_FUNC);
}
#endif

#ifndef NO_cairo_1set_1pattern
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1pattern)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1pattern_FUNC);
	cairo_set_pattern((cairo_t *)arg0, (cairo_pattern_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1pattern_FUNC);
}
#endif

#ifndef NO_cairo_1set_1rgb_1color
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1rgb_1color)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1rgb_1color_FUNC);
	cairo_set_rgb_color((cairo_t *)arg0, arg1, arg2, arg3);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1rgb_1color_FUNC);
}
#endif

#ifndef NO_cairo_1set_1target_1drawable
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1target_1drawable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1target_1drawable_FUNC);
	cairo_set_target_drawable((cairo_t *)arg0, (Display *)arg1, (Drawable)arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1target_1drawable_FUNC);
}
#endif

#ifndef NO_cairo_1set_1target_1image
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1target_1image)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1target_1image_FUNC);
	cairo_set_target_image((cairo_t *)arg0, (char *)arg1, arg2, arg3, arg4, arg5);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1target_1image_FUNC);
}
#endif

#ifndef NO_cairo_1set_1target_1surface
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1target_1surface)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1target_1surface_FUNC);
	cairo_set_target_surface((cairo_t *)arg0, (cairo_surface_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1target_1surface_FUNC);
}
#endif

#ifndef NO_cairo_1set_1tolerance
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1set_1tolerance)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1set_1tolerance_FUNC);
	cairo_set_tolerance((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1set_1tolerance_FUNC);
}
#endif

#ifndef NO_cairo_1show_1glyphs
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1show_1glyphs)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1show_1glyphs_FUNC);
	cairo_show_glyphs((cairo_t *)arg0, (cairo_glyph_t *)arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1show_1glyphs_FUNC);
}
#endif

#ifndef NO_cairo_1show_1page
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1show_1page)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1show_1page_FUNC);
	cairo_show_page((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1show_1page_FUNC);
}
#endif

#ifndef NO_cairo_1show_1surface
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1show_1surface)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1show_1surface_FUNC);
	cairo_show_surface((cairo_t *)arg0, (cairo_surface_t *)arg1, arg2, arg3);
	Cairo_NATIVE_EXIT(env, that, cairo_1show_1surface_FUNC);
}
#endif

#ifndef NO_cairo_1show_1text
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1show_1text)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1show_1text_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_show_text((cairo_t *)arg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1show_1text_FUNC);
}
#endif

#ifndef NO_cairo_1status
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1status)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1status_FUNC);
	rc = (jint)cairo_status((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1status_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1status_1string
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1status_1string)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1status_1string_FUNC);
	rc = (jint)cairo_status_string((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1status_1string_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1stroke
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1stroke)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1stroke_FUNC);
	cairo_stroke((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1stroke_FUNC);
}
#endif

#ifndef NO_cairo_1stroke_1extents
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1stroke_1extents)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jdoubleArray arg2, jdoubleArray arg3, jdoubleArray arg4)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	jdouble *lparg3=NULL;
	jdouble *lparg4=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1stroke_1extents_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetDoubleArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetDoubleArrayElements(env, arg4, NULL)) == NULL) goto fail;
	cairo_stroke_extents((cairo_t *)arg0, lparg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseDoubleArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseDoubleArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1stroke_1extents_FUNC);
}
#endif

#ifndef NO_cairo_1surface_1create_1for_1image
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1surface_1create_1for_1image)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1surface_1create_1for_1image_FUNC);
	rc = (jint)cairo_surface_create_for_image((char *)arg0, arg1, arg2, arg3, arg4);
	Cairo_NATIVE_EXIT(env, that, cairo_1surface_1create_1for_1image_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1surface_1create_1similar
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1surface_1create_1similar)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1surface_1create_1similar_FUNC);
	rc = (jint)cairo_surface_create_similar((cairo_surface_t *)arg0, arg1, arg2, arg3);
	Cairo_NATIVE_EXIT(env, that, cairo_1surface_1create_1similar_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1surface_1destroy
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1surface_1destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1surface_1destroy_FUNC);
	cairo_surface_destroy((cairo_surface_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1surface_1destroy_FUNC);
}
#endif

#ifndef NO_cairo_1surface_1get_1filter
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1surface_1get_1filter)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1surface_1get_1filter_FUNC);
	rc = (jint)cairo_surface_get_filter((cairo_surface_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1surface_1get_1filter_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1surface_1get_1matrix
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1surface_1get_1matrix)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1surface_1get_1matrix_FUNC);
	rc = (jint)cairo_surface_get_matrix((cairo_surface_t *)arg0, (cairo_matrix_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1surface_1get_1matrix_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1surface_1reference
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1surface_1reference)
	(JNIEnv *env, jclass that, jint arg0)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1surface_1reference_FUNC);
	cairo_surface_reference((cairo_surface_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1surface_1reference_FUNC);
}
#endif

#ifndef NO_cairo_1surface_1set_1filter
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1surface_1set_1filter)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1surface_1set_1filter_FUNC);
	rc = (jint)cairo_surface_set_filter((cairo_surface_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1surface_1set_1filter_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1surface_1set_1matrix
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1surface_1set_1matrix)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1surface_1set_1matrix_FUNC);
	rc = (jint)cairo_surface_set_matrix((cairo_surface_t *)arg0, (cairo_matrix_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1surface_1set_1matrix_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1surface_1set_1repeat
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1surface_1set_1repeat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1surface_1set_1repeat_FUNC);
	rc = (jint)cairo_surface_set_repeat((cairo_surface_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1surface_1set_1repeat_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1text_1extents
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1text_1extents)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1text_1extents_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_text_extents((cairo_t *)arg0, lparg1, (cairo_text_extents_t *)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1text_1extents_FUNC);
}
#endif

#ifndef NO_cairo_1text_1path
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1text_1path)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1text_1path_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_text_path((cairo_t *)arg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1text_1path_FUNC);
}
#endif

#ifndef NO_cairo_1transform_1distance
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1transform_1distance)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1transform_1distance_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_transform_distance((cairo_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1transform_1distance_FUNC);
}
#endif

#ifndef NO_cairo_1transform_1font
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1transform_1font)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1transform_1font_FUNC);
	cairo_transform_font((cairo_t *)arg0, (cairo_matrix_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, cairo_1transform_1font_FUNC);
}
#endif

#ifndef NO_cairo_1transform_1point
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1transform_1point)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, cairo_1transform_1point_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_transform_point((cairo_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, cairo_1transform_1point_FUNC);
}
#endif

#ifndef NO_cairo_1translate
JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1translate)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1translate_FUNC);
	cairo_translate((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, cairo_1translate_FUNC);
}
#endif

#ifndef NO_cairo_1xlib_1surface_1create
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1xlib_1surface_1create)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1xlib_1surface_1create_FUNC);
	rc = (jint)cairo_xlib_surface_create((Display *)arg0, (Drawable)arg1, (Visual *)arg2, arg3, (Colormap)arg4);
	Cairo_NATIVE_EXIT(env, that, cairo_1xlib_1surface_1create_FUNC);
	return rc;
}
#endif


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
 * -  Copyright (C) 2005, 2011 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */

#include "swt.h"
#include "cairo_structs.h"
#include "cairo_stats.h"

#define Cairo_NATIVE(func) Java_org_eclipse_swt_internal_cairo_Cairo_##func

#ifndef NO_CAIRO_1VERSION_1ENCODE
JNIEXPORT jint JNICALL Cairo_NATIVE(CAIRO_1VERSION_1ENCODE)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, CAIRO_1VERSION_1ENCODE_FUNC);
	rc = (jint)CAIRO_VERSION_ENCODE(arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, CAIRO_1VERSION_1ENCODE_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1append_1path
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1append_1path)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1append_1path_FUNC);
	cairo_append_path((cairo_t *)arg0, (cairo_path_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1append_1path_FUNC);
}
#endif

#ifndef NO__1cairo_1arc
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1arc)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1arc_FUNC);
	cairo_arc((cairo_t *)arg0, arg1, arg2, arg3, arg4, arg5);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1arc_FUNC);
}
#endif

#ifndef NO__1cairo_1arc_1negative
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1arc_1negative)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1arc_1negative_FUNC);
	cairo_arc_negative((cairo_t *)arg0, arg1, arg2, arg3, arg4, arg5);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1arc_1negative_FUNC);
}
#endif

#ifndef NO__1cairo_1clip
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1clip)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1clip_FUNC);
	cairo_clip((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1clip_FUNC);
}
#endif

#ifndef NO__1cairo_1clip_1preserve
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1clip_1preserve)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1clip_1preserve_FUNC);
	cairo_clip_preserve((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1clip_1preserve_FUNC);
}
#endif

#ifndef NO__1cairo_1close_1path
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1close_1path)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1close_1path_FUNC);
	cairo_close_path((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1close_1path_FUNC);
}
#endif

#ifndef NO__1cairo_1copy_1page
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1copy_1page)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1copy_1page_FUNC);
	cairo_copy_page((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1copy_1page_FUNC);
}
#endif

#ifndef NO__1cairo_1copy_1path
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1copy_1path)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1copy_1path_FUNC);
	rc = (jintLong)cairo_copy_path((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1copy_1path_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1copy_1path_1flat
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1copy_1path_1flat)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1copy_1path_1flat_FUNC);
	rc = (jintLong)cairo_copy_path_flat((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1copy_1path_1flat_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1create
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1create)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1create_FUNC);
	rc = (jintLong)cairo_create((cairo_surface_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1create_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1curve_1to
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1curve_1to)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5, jdouble arg6)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1curve_1to_FUNC);
	cairo_curve_to((cairo_t *)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1curve_1to_FUNC);
}
#endif

#ifndef NO__1cairo_1destroy
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1destroy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1destroy_FUNC);
	cairo_destroy((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1destroy_FUNC);
}
#endif

#ifndef NO__1cairo_1device_1to_1user
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1device_1to_1user)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1device_1to_1user_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_device_to_user((cairo_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1device_1to_1user_FUNC);
}
#endif

#ifndef NO__1cairo_1device_1to_1user_1distance
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1device_1to_1user_1distance)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1device_1to_1user_1distance_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_device_to_user_distance((cairo_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1device_1to_1user_1distance_FUNC);
}
#endif

#ifndef NO__1cairo_1fill
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1fill)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1fill_FUNC);
	cairo_fill((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1fill_FUNC);
}
#endif

#ifndef NO__1cairo_1fill_1extents
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1fill_1extents)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jdoubleArray arg2, jdoubleArray arg3, jdoubleArray arg4)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	jdouble *lparg3=NULL;
	jdouble *lparg4=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1fill_1extents_FUNC);
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
	Cairo_NATIVE_EXIT(env, that, _1cairo_1fill_1extents_FUNC);
}
#endif

#ifndef NO__1cairo_1fill_1preserve
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1fill_1preserve)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1fill_1preserve_FUNC);
	cairo_fill_preserve((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1fill_1preserve_FUNC);
}
#endif

#ifndef NO__1cairo_1font_1extents
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1font_1extents)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	cairo_font_extents_t _arg1, *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1font_1extents_FUNC);
	if (arg1) if ((lparg1 = getcairo_font_extents_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	cairo_font_extents((cairo_t *)arg0, lparg1);
fail:
	if (arg1 && lparg1) setcairo_font_extents_tFields(env, arg1, lparg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1font_1extents_FUNC);
}
#endif

#ifndef NO__1cairo_1font_1options_1create
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1font_1options_1create)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1font_1options_1create_FUNC);
	rc = (jintLong)cairo_font_options_create();
	Cairo_NATIVE_EXIT(env, that, _1cairo_1font_1options_1create_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1font_1options_1destroy
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1font_1options_1destroy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1font_1options_1destroy_FUNC);
	cairo_font_options_destroy((cairo_font_options_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1font_1options_1destroy_FUNC);
}
#endif

#ifndef NO__1cairo_1font_1options_1get_1antialias
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1font_1options_1get_1antialias)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1font_1options_1get_1antialias_FUNC);
	rc = (jint)cairo_font_options_get_antialias((cairo_font_options_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1font_1options_1get_1antialias_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1font_1options_1set_1antialias
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1font_1options_1set_1antialias)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1font_1options_1set_1antialias_FUNC);
	cairo_font_options_set_antialias((cairo_font_options_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1font_1options_1set_1antialias_FUNC);
}
#endif

#ifndef NO__1cairo_1get_1antialias
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1get_1antialias)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1antialias_FUNC);
	rc = (jint)cairo_get_antialias((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1antialias_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1get_1current_1point
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1get_1current_1point)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1current_1point_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_get_current_point((cairo_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1current_1point_FUNC);
}
#endif

#ifndef NO__1cairo_1get_1fill_1rule
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1get_1fill_1rule)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1fill_1rule_FUNC);
	rc = (jint)cairo_get_fill_rule((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1fill_1rule_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1get_1font_1face
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1get_1font_1face)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1font_1face_FUNC);
	rc = (jintLong)cairo_get_font_face((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1font_1face_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1get_1font_1matrix
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1get_1font_1matrix)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1font_1matrix_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_get_font_matrix((cairo_t *)arg0, (cairo_matrix_t *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1font_1matrix_FUNC);
}
#endif

#ifndef NO__1cairo_1get_1font_1options
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1get_1font_1options)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1font_1options_FUNC);
	cairo_get_font_options((cairo_t *)arg0, (cairo_font_options_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1font_1options_FUNC);
}
#endif

#ifndef NO__1cairo_1get_1line_1cap
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1get_1line_1cap)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1line_1cap_FUNC);
	rc = (jint)cairo_get_line_cap((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1line_1cap_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1get_1line_1join
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1get_1line_1join)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1line_1join_FUNC);
	rc = (jint)cairo_get_line_join((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1line_1join_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1get_1line_1width
JNIEXPORT jdouble JNICALL Cairo_NATIVE(_1cairo_1get_1line_1width)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jdouble rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1line_1width_FUNC);
	rc = (jdouble)cairo_get_line_width((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1line_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1get_1matrix
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1get_1matrix)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1matrix_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_get_matrix((cairo_t *)arg0, (cairo_matrix_t *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1matrix_FUNC);
}
#endif

#ifndef NO__1cairo_1get_1miter_1limit
JNIEXPORT jdouble JNICALL Cairo_NATIVE(_1cairo_1get_1miter_1limit)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jdouble rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1miter_1limit_FUNC);
	rc = (jdouble)cairo_get_miter_limit((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1miter_1limit_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1get_1operator
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1get_1operator)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1operator_FUNC);
	rc = (jint)cairo_get_operator((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1operator_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1get_1source
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1get_1source)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1source_FUNC);
	rc = (jintLong)cairo_get_source((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1source_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1get_1target
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1get_1target)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1target_FUNC);
	rc = (jintLong)cairo_get_target((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1target_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1get_1tolerance
JNIEXPORT jdouble JNICALL Cairo_NATIVE(_1cairo_1get_1tolerance)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jdouble rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1get_1tolerance_FUNC);
	rc = (jdouble)cairo_get_tolerance((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1get_1tolerance_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1glyph_1extents
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1glyph_1extents)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1glyph_1extents_FUNC);
	cairo_glyph_extents((cairo_t *)arg0, (cairo_glyph_t *)arg1, arg2, (cairo_text_extents_t *)arg3);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1glyph_1extents_FUNC);
}
#endif

#ifndef NO__1cairo_1glyph_1path
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1glyph_1path)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1glyph_1path_FUNC);
	cairo_glyph_path((cairo_t *)arg0, (cairo_glyph_t *)arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1glyph_1path_FUNC);
}
#endif

#ifndef NO__1cairo_1identity_1matrix
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1identity_1matrix)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1identity_1matrix_FUNC);
	cairo_identity_matrix((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1identity_1matrix_FUNC);
}
#endif

#ifndef NO__1cairo_1image_1surface_1create
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1image_1surface_1create)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1image_1surface_1create_FUNC);
	rc = (jintLong)cairo_image_surface_create(arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1image_1surface_1create_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1image_1surface_1create_1for_1data
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1image_1surface_1create_1for_1data)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1image_1surface_1create_1for_1data_FUNC);
	rc = (jintLong)cairo_image_surface_create_for_data((unsigned char *)arg0, arg1, arg2, arg3, arg4);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1image_1surface_1create_1for_1data_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1image_1surface_1get_1height
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1image_1surface_1get_1height)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1image_1surface_1get_1height_FUNC);
	rc = (jint)cairo_image_surface_get_height((cairo_surface_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1image_1surface_1get_1height_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1image_1surface_1get_1width
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1image_1surface_1get_1width)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1image_1surface_1get_1width_FUNC);
	rc = (jint)cairo_image_surface_get_width((cairo_surface_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1image_1surface_1get_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1in_1fill
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1in_1fill)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1in_1fill_FUNC);
	rc = (jint)cairo_in_fill((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1in_1fill_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1in_1stroke
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1in_1stroke)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1in_1stroke_FUNC);
	rc = (jint)cairo_in_stroke((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1in_1stroke_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1line_1to
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1line_1to)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1line_1to_FUNC);
	cairo_line_to((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1line_1to_FUNC);
}
#endif

#ifndef NO__1cairo_1mask
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1mask)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1mask_FUNC);
	cairo_mask((cairo_t *)arg0, (cairo_pattern_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1mask_FUNC);
}
#endif

#ifndef NO__1cairo_1mask_1surface
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1mask_1surface)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jdouble arg2, jdouble arg3)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1mask_1surface_FUNC);
	cairo_mask_surface((cairo_t *)arg0, (cairo_surface_t *)arg1, arg2, arg3);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1mask_1surface_FUNC);
}
#endif

#ifndef NO__1cairo_1matrix_1init
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1matrix_1init)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5, jdouble arg6)
{
	jdouble *lparg0=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1matrix_1init_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	cairo_matrix_init((cairo_matrix_t *)lparg0, arg1, arg2, arg3, arg4, arg5, arg6);
fail:
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1matrix_1init_FUNC);
}
#endif

#ifndef NO__1cairo_1matrix_1init_1identity
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1matrix_1init_1identity)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1matrix_1init_1identity_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	cairo_matrix_init_identity((cairo_matrix_t *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1matrix_1init_1identity_FUNC);
}
#endif

#ifndef NO__1cairo_1matrix_1init_1rotate
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1matrix_1init_1rotate)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jdouble arg1)
{
	jdouble *lparg0=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1matrix_1init_1rotate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	cairo_matrix_init_rotate((cairo_matrix_t *)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1matrix_1init_1rotate_FUNC);
}
#endif

#ifndef NO__1cairo_1matrix_1init_1scale
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1matrix_1init_1scale)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jdouble arg1, jdouble arg2)
{
	jdouble *lparg0=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1matrix_1init_1scale_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	cairo_matrix_init_scale((cairo_matrix_t *)lparg0, arg1, arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1matrix_1init_1scale_FUNC);
}
#endif

#ifndef NO__1cairo_1matrix_1init_1translate
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1matrix_1init_1translate)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jdouble arg1, jdouble arg2)
{
	jdouble *lparg0=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1matrix_1init_1translate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	cairo_matrix_init_translate((cairo_matrix_t *)lparg0, arg1, arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1matrix_1init_1translate_FUNC);
}
#endif

#ifndef NO__1cairo_1matrix_1invert
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1matrix_1invert)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1matrix_1invert_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)cairo_matrix_invert((cairo_matrix_t *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1matrix_1invert_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1matrix_1multiply
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1matrix_1multiply)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg0=NULL;
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1matrix_1multiply_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_matrix_multiply((cairo_matrix_t *)lparg0, (cairo_matrix_t *)lparg1, (cairo_matrix_t *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1matrix_1multiply_FUNC);
}
#endif

#ifndef NO__1cairo_1matrix_1rotate
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1matrix_1rotate)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jdouble arg1)
{
	jdouble *lparg0=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1matrix_1rotate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	cairo_matrix_rotate((cairo_matrix_t *)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1matrix_1rotate_FUNC);
}
#endif

#ifndef NO__1cairo_1matrix_1scale
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1matrix_1scale)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jdouble arg1, jdouble arg2)
{
	jdouble *lparg0=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1matrix_1scale_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	cairo_matrix_scale((cairo_matrix_t *)lparg0, arg1, arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1matrix_1scale_FUNC);
}
#endif

#ifndef NO__1cairo_1matrix_1transform_1distance
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1matrix_1transform_1distance)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg0=NULL;
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1matrix_1transform_1distance_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_matrix_transform_distance((cairo_matrix_t *)lparg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1matrix_1transform_1distance_FUNC);
}
#endif

#ifndef NO__1cairo_1matrix_1transform_1point
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1matrix_1transform_1point)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg0=NULL;
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1matrix_1transform_1point_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_matrix_transform_point((cairo_matrix_t *)lparg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1matrix_1transform_1point_FUNC);
}
#endif

#ifndef NO__1cairo_1matrix_1translate
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1matrix_1translate)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jdouble arg1, jdouble arg2)
{
	jdouble *lparg0=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1matrix_1translate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	cairo_matrix_translate((cairo_matrix_t *)lparg0, arg1, arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1matrix_1translate_FUNC);
}
#endif

#ifndef NO__1cairo_1move_1to
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1move_1to)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1move_1to_FUNC);
	cairo_move_to((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1move_1to_FUNC);
}
#endif

#ifndef NO__1cairo_1new_1path
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1new_1path)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1new_1path_FUNC);
	cairo_new_path((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1new_1path_FUNC);
}
#endif

#ifndef NO__1cairo_1paint
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1paint)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1paint_FUNC);
	cairo_paint((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1paint_FUNC);
}
#endif

#ifndef NO__1cairo_1paint_1with_1alpha
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1paint_1with_1alpha)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1paint_1with_1alpha_FUNC);
	cairo_paint_with_alpha((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1paint_1with_1alpha_FUNC);
}
#endif

#ifndef NO__1cairo_1path_1destroy
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1path_1destroy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1path_1destroy_FUNC);
	cairo_path_destroy((cairo_path_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1path_1destroy_FUNC);
}
#endif

#ifndef NO__1cairo_1pattern_1add_1color_1stop_1rgb
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1pattern_1add_1color_1stop_1rgb)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1add_1color_1stop_1rgb_FUNC);
	cairo_pattern_add_color_stop_rgb((cairo_pattern_t *)arg0, arg1, arg2, arg3, arg4);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1add_1color_1stop_1rgb_FUNC);
}
#endif

#ifndef NO__1cairo_1pattern_1add_1color_1stop_1rgba
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1pattern_1add_1color_1stop_1rgba)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1add_1color_1stop_1rgba_FUNC);
	cairo_pattern_add_color_stop_rgba((cairo_pattern_t *)arg0, arg1, arg2, arg3, arg4, arg5);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1add_1color_1stop_1rgba_FUNC);
}
#endif

#ifndef NO__1cairo_1pattern_1create_1for_1surface
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1pattern_1create_1for_1surface)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1create_1for_1surface_FUNC);
	rc = (jintLong)cairo_pattern_create_for_surface((cairo_surface_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1create_1for_1surface_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1pattern_1create_1linear
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1pattern_1create_1linear)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1create_1linear_FUNC);
	rc = (jintLong)cairo_pattern_create_linear(arg0, arg1, arg2, arg3);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1create_1linear_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1pattern_1create_1radial
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1pattern_1create_1radial)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1create_1radial_FUNC);
	rc = (jintLong)cairo_pattern_create_radial(arg0, arg1, arg2, arg3, arg4, arg5);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1create_1radial_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1pattern_1destroy
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1pattern_1destroy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1destroy_FUNC);
	cairo_pattern_destroy((cairo_pattern_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1destroy_FUNC);
}
#endif

#ifndef NO__1cairo_1pattern_1get_1extend
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1pattern_1get_1extend)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1get_1extend_FUNC);
	rc = (jint)cairo_pattern_get_extend((cairo_pattern_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1get_1extend_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1pattern_1get_1filter
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1pattern_1get_1filter)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1get_1filter_FUNC);
	rc = (jint)cairo_pattern_get_filter((cairo_pattern_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1get_1filter_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1pattern_1get_1matrix
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1pattern_1get_1matrix)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1get_1matrix_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_pattern_get_matrix((cairo_pattern_t *)arg0, (cairo_matrix_t *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1get_1matrix_FUNC);
}
#endif

#ifndef NO__1cairo_1pattern_1reference
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1pattern_1reference)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1reference_FUNC);
	cairo_pattern_reference((cairo_pattern_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1reference_FUNC);
}
#endif

#ifndef NO__1cairo_1pattern_1set_1extend
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1pattern_1set_1extend)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1set_1extend_FUNC);
	cairo_pattern_set_extend((cairo_pattern_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1set_1extend_FUNC);
}
#endif

#ifndef NO__1cairo_1pattern_1set_1filter
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1pattern_1set_1filter)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1set_1filter_FUNC);
	cairo_pattern_set_filter((cairo_pattern_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1set_1filter_FUNC);
}
#endif

#ifndef NO__1cairo_1pattern_1set_1matrix
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1pattern_1set_1matrix)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pattern_1set_1matrix_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_pattern_set_matrix((cairo_pattern_t *)arg0, (cairo_matrix_t *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pattern_1set_1matrix_FUNC);
}
#endif

#ifndef NO__1cairo_1pdf_1surface_1set_1size
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1pdf_1surface_1set_1size)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1pdf_1surface_1set_1size_FUNC);
/*
	cairo_pdf_surface_set_size((cairo_surface_t *)arg0, arg1, arg2);
*/
	{
		Cairo_LOAD_FUNCTION(fp, cairo_pdf_surface_set_size)
		if (fp) {
			((void (CALLING_CONVENTION*)(cairo_surface_t *, jdouble, jdouble))fp)((cairo_surface_t *)arg0, arg1, arg2);
		}
	}
	Cairo_NATIVE_EXIT(env, that, _1cairo_1pdf_1surface_1set_1size_FUNC);
}
#endif

#ifndef NO__1cairo_1ps_1surface_1set_1size
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1ps_1surface_1set_1size)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1ps_1surface_1set_1size_FUNC);
/*
	cairo_ps_surface_set_size((cairo_surface_t *)arg0, arg1, arg2);
*/
	{
		Cairo_LOAD_FUNCTION(fp, cairo_ps_surface_set_size)
		if (fp) {
			((void (CALLING_CONVENTION*)(cairo_surface_t *, jdouble, jdouble))fp)((cairo_surface_t *)arg0, arg1, arg2);
		}
	}
	Cairo_NATIVE_EXIT(env, that, _1cairo_1ps_1surface_1set_1size_FUNC);
}
#endif

#ifndef NO__1cairo_1rectangle
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1rectangle)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1rectangle_FUNC);
	cairo_rectangle((cairo_t *)arg0, arg1, arg2, arg3, arg4);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1rectangle_FUNC);
}
#endif

#ifndef NO__1cairo_1reference
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1reference)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1reference_FUNC);
	rc = (jintLong)cairo_reference((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1reference_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1rel_1curve_1to
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1rel_1curve_1to)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5, jdouble arg6)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1rel_1curve_1to_FUNC);
	cairo_rel_curve_to((cairo_t *)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1rel_1curve_1to_FUNC);
}
#endif

#ifndef NO__1cairo_1rel_1line_1to
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1rel_1line_1to)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1rel_1line_1to_FUNC);
	cairo_rel_line_to((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1rel_1line_1to_FUNC);
}
#endif

#ifndef NO__1cairo_1rel_1move_1to
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1rel_1move_1to)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1rel_1move_1to_FUNC);
	cairo_rel_move_to((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1rel_1move_1to_FUNC);
}
#endif

#ifndef NO__1cairo_1reset_1clip
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1reset_1clip)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1reset_1clip_FUNC);
	cairo_reset_clip((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1reset_1clip_FUNC);
}
#endif

#ifndef NO__1cairo_1restore
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1restore)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1restore_FUNC);
	cairo_restore((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1restore_FUNC);
}
#endif

#ifndef NO__1cairo_1rotate
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1rotate)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1rotate_FUNC);
	cairo_rotate((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1rotate_FUNC);
}
#endif

#ifndef NO__1cairo_1save
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1save)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1save_FUNC);
	cairo_save((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1save_FUNC);
}
#endif

#ifndef NO__1cairo_1scale
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1scale)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1scale_FUNC);
	cairo_scale((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1scale_FUNC);
}
#endif

#ifndef NO__1cairo_1select_1font_1face
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1select_1font_1face)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1select_1font_1face_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_select_font_face((cairo_t *)arg0, (const char *)lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1select_1font_1face_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1antialias
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1antialias)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1antialias_FUNC);
	cairo_set_antialias((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1antialias_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1dash
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1dash)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jint arg2, jdouble arg3)
{
	jdouble *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1dash_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_set_dash((cairo_t *)arg0, lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1dash_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1fill_1rule
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1fill_1rule)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1fill_1rule_FUNC);
	cairo_set_fill_rule((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1fill_1rule_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1font_1face
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1font_1face)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1font_1face_FUNC);
	cairo_set_font_face((cairo_t *)arg0, (cairo_font_face_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1font_1face_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1font_1matrix
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1font_1matrix)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1font_1matrix_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_set_font_matrix((cairo_t *)arg0, (cairo_matrix_t *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1font_1matrix_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1font_1options
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1font_1options)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1font_1options_FUNC);
	cairo_set_font_options((cairo_t *)arg0, (cairo_font_options_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1font_1options_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1font_1size
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1font_1size)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1font_1size_FUNC);
	cairo_set_font_size((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1font_1size_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1line_1cap
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1line_1cap)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1line_1cap_FUNC);
	cairo_set_line_cap((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1line_1cap_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1line_1join
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1line_1join)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1line_1join_FUNC);
	cairo_set_line_join((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1line_1join_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1line_1width
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1line_1width)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1line_1width_FUNC);
	cairo_set_line_width((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1line_1width_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1matrix
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1matrix)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1matrix_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_set_matrix((cairo_t *)arg0, (cairo_matrix_t *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1matrix_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1miter_1limit
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1miter_1limit)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1miter_1limit_FUNC);
	cairo_set_miter_limit((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1miter_1limit_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1operator
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1operator)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1operator_FUNC);
	cairo_set_operator((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1operator_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1source
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1source)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1source_FUNC);
	cairo_set_source((cairo_t *)arg0, (cairo_pattern_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1source_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1source_1rgb
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1source_1rgb)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1source_1rgb_FUNC);
	cairo_set_source_rgb((cairo_t *)arg0, arg1, arg2, arg3);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1source_1rgb_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1source_1rgba
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1source_1rgba)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1source_1rgba_FUNC);
	cairo_set_source_rgba((cairo_t *)arg0, arg1, arg2, arg3, arg4);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1source_1rgba_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1source_1surface
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1source_1surface)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jdouble arg2, jdouble arg3)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1source_1surface_FUNC);
	cairo_set_source_surface((cairo_t *)arg0, (cairo_surface_t *)arg1, arg2, arg3);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1source_1surface_FUNC);
}
#endif

#ifndef NO__1cairo_1set_1tolerance
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1set_1tolerance)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1set_1tolerance_FUNC);
	cairo_set_tolerance((cairo_t *)arg0, arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1set_1tolerance_FUNC);
}
#endif

#ifndef NO__1cairo_1show_1glyphs
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1show_1glyphs)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1show_1glyphs_FUNC);
	cairo_show_glyphs((cairo_t *)arg0, (cairo_glyph_t *)arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1show_1glyphs_FUNC);
}
#endif

#ifndef NO__1cairo_1show_1page
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1show_1page)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1show_1page_FUNC);
	cairo_show_page((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1show_1page_FUNC);
}
#endif

#ifndef NO__1cairo_1show_1text
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1show_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1show_1text_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_show_text((cairo_t *)arg0, (const char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1show_1text_FUNC);
}
#endif

#ifndef NO__1cairo_1status
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1status)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1status_FUNC);
	rc = (jint)cairo_status((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1status_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1status_1to_1string
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1status_1to_1string)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1status_1to_1string_FUNC);
	rc = (jintLong)cairo_status_to_string(arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1status_1to_1string_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1stroke
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1stroke)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1stroke_FUNC);
	cairo_stroke((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1stroke_FUNC);
}
#endif

#ifndef NO__1cairo_1stroke_1extents
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1stroke_1extents)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jdoubleArray arg2, jdoubleArray arg3, jdoubleArray arg4)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	jdouble *lparg3=NULL;
	jdouble *lparg4=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1stroke_1extents_FUNC);
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
	Cairo_NATIVE_EXIT(env, that, _1cairo_1stroke_1extents_FUNC);
}
#endif

#ifndef NO__1cairo_1stroke_1preserve
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1stroke_1preserve)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1stroke_1preserve_FUNC);
	cairo_stroke_preserve((cairo_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1stroke_1preserve_FUNC);
}
#endif

#ifndef NO__1cairo_1surface_1create_1similar
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1surface_1create_1similar)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1surface_1create_1similar_FUNC);
	rc = (jintLong)cairo_surface_create_similar((cairo_surface_t *)arg0, arg1, arg2, arg3);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1surface_1create_1similar_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1surface_1destroy
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1surface_1destroy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1surface_1destroy_FUNC);
	cairo_surface_destroy((cairo_surface_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1surface_1destroy_FUNC);
}
#endif

#ifndef NO__1cairo_1surface_1finish
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1surface_1finish)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1surface_1finish_FUNC);
	cairo_surface_finish((cairo_surface_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1surface_1finish_FUNC);
}
#endif

#ifndef NO__1cairo_1surface_1get_1type
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1surface_1get_1type)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1surface_1get_1type_FUNC);
/*
	rc = (jint)cairo_surface_get_type((cairo_surface_t *)arg0);
*/
	{
		Cairo_LOAD_FUNCTION(fp, cairo_surface_get_type)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(cairo_surface_t *))fp)((cairo_surface_t *)arg0);
		}
	}
	Cairo_NATIVE_EXIT(env, that, _1cairo_1surface_1get_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1surface_1get_1user_1data
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1surface_1get_1user_1data)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1surface_1get_1user_1data_FUNC);
	rc = (jintLong)cairo_surface_get_user_data((cairo_surface_t *)arg0, (cairo_user_data_key_t *)arg1);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1surface_1get_1user_1data_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1surface_1reference
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1surface_1reference)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1surface_1reference_FUNC);
	cairo_surface_reference((cairo_surface_t *)arg0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1surface_1reference_FUNC);
}
#endif

#ifndef NO__1cairo_1surface_1set_1device_1offset
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1surface_1set_1device_1offset)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1surface_1set_1device_1offset_FUNC);
	cairo_surface_set_device_offset((cairo_surface_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1surface_1set_1device_1offset_FUNC);
}
#endif

#ifndef NO__1cairo_1surface_1set_1fallback_1resolution
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1surface_1set_1fallback_1resolution)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1surface_1set_1fallback_1resolution_FUNC);
/*
	cairo_surface_set_fallback_resolution(arg0, arg1, arg2);
*/
	{
		Cairo_LOAD_FUNCTION(fp, cairo_surface_set_fallback_resolution)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jdouble, jdouble))fp)(arg0, arg1, arg2);
		}
	}
	Cairo_NATIVE_EXIT(env, that, _1cairo_1surface_1set_1fallback_1resolution_FUNC);
}
#endif

#ifndef NO__1cairo_1surface_1set_1user_1data
JNIEXPORT jint JNICALL Cairo_NATIVE(_1cairo_1surface_1set_1user_1data)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1surface_1set_1user_1data_FUNC);
	rc = (jint)cairo_surface_set_user_data((cairo_surface_t *)arg0, (cairo_user_data_key_t *)arg1, (void *)arg2, (cairo_destroy_func_t)arg3);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1surface_1set_1user_1data_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1text_1extents
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1text_1extents)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jobject arg2)
{
	jbyte *lparg1=NULL;
	cairo_text_extents_t _arg2, *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1text_1extents_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getcairo_text_extents_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	cairo_text_extents((cairo_t *)arg0, (const char *)lparg1, (cairo_text_extents_t *)lparg2);
fail:
	if (arg2 && lparg2) setcairo_text_extents_tFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1text_1extents_FUNC);
}
#endif

#ifndef NO__1cairo_1text_1path
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1text_1path)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1text_1path_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_text_path((cairo_t *)arg0, (const char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1text_1path_FUNC);
}
#endif

#ifndef NO__1cairo_1transform
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1transform)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1transform_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	cairo_transform((cairo_t *)arg0, (cairo_matrix_t *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1transform_FUNC);
}
#endif

#ifndef NO__1cairo_1translate
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1translate)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1translate_FUNC);
	cairo_translate((cairo_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1translate_FUNC);
}
#endif

#ifndef NO__1cairo_1user_1to_1device
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1user_1to_1device)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1user_1to_1device_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_user_to_device((cairo_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1user_1to_1device_FUNC);
}
#endif

#ifndef NO__1cairo_1user_1to_1device_1distance
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1user_1to_1device_1distance)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1user_1to_1device_1distance_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	cairo_user_to_device_distance((cairo_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1user_1to_1device_1distance_FUNC);
}
#endif

#ifndef NO__1cairo_1xlib_1surface_1create
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1xlib_1surface_1create)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3, jint arg4)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1xlib_1surface_1create_FUNC);
	rc = (jintLong)cairo_xlib_surface_create((Display *)arg0, (Drawable)arg1, (Visual *)arg2, arg3, arg4);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1xlib_1surface_1create_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1xlib_1surface_1create_1for_1bitmap
JNIEXPORT jintLong JNICALL Cairo_NATIVE(_1cairo_1xlib_1surface_1create_1for_1bitmap)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3, jint arg4)
{
	jintLong rc = 0;
	Cairo_NATIVE_ENTER(env, that, _1cairo_1xlib_1surface_1create_1for_1bitmap_FUNC);
	rc = (jintLong)cairo_xlib_surface_create_for_bitmap((Display *)arg0, (Pixmap)arg1, (Screen *)arg2, arg3, arg4);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1xlib_1surface_1create_1for_1bitmap_FUNC);
	return rc;
}
#endif

#ifndef NO__1cairo_1xlib_1surface_1set_1size
JNIEXPORT void JNICALL Cairo_NATIVE(_1cairo_1xlib_1surface_1set_1size)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	Cairo_NATIVE_ENTER(env, that, _1cairo_1xlib_1surface_1set_1size_FUNC);
	cairo_xlib_surface_set_size((cairo_surface_t *)arg0, arg1, arg2);
	Cairo_NATIVE_EXIT(env, that, _1cairo_1xlib_1surface_1set_1size_FUNC);
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

#ifndef NO_cairo_1path_1data_1t_1sizeof
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1path_1data_1t_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1path_1data_1t_1sizeof_FUNC);
	rc = (jint)cairo_path_data_t_sizeof();
	Cairo_NATIVE_EXIT(env, that, cairo_1path_1data_1t_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1path_1t_1sizeof
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1path_1t_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1path_1t_1sizeof_FUNC);
	rc = (jint)cairo_path_t_sizeof();
	Cairo_NATIVE_EXIT(env, that, cairo_1path_1t_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1text_1extents_1t_1sizeof
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1text_1extents_1t_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1text_1extents_1t_1sizeof_FUNC);
	rc = (jint)cairo_text_extents_t_sizeof();
	Cairo_NATIVE_EXIT(env, that, cairo_1text_1extents_1t_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_cairo_1version
JNIEXPORT jint JNICALL Cairo_NATIVE(cairo_1version)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Cairo_NATIVE_ENTER(env, that, cairo_1version_FUNC);
	rc = (jint)cairo_version();
	Cairo_NATIVE_EXIT(env, that, cairo_1version_FUNC);
	return rc;
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL Cairo_NATIVE(memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL Cairo_NATIVE(memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	cairo_path_data_t _arg0, *lparg0=NULL;
#ifndef JNI64
	Cairo_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2II_FUNC);
#else
	Cairo_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getcairo_path_data_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setcairo_path_data_tFields(env, arg0, lparg0);
#ifndef JNI64
	Cairo_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2II_FUNC);
#else
	Cairo_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1data_1t_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL Cairo_NATIVE(memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL Cairo_NATIVE(memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	cairo_path_t _arg0, *lparg0=NULL;
#ifndef JNI64
	Cairo_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2II_FUNC);
#else
	Cairo_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getcairo_path_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setcairo_path_tFields(env, arg0, lparg0);
#ifndef JNI64
	Cairo_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2II_FUNC);
#else
	Cairo_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cairo_cairo_1path_1t_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove___3DII) && !defined(JNI64)) || (!defined(NO_memmove___3DJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL Cairo_NATIVE(memmove___3DII)(JNIEnv *env, jclass that, jdoubleArray arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL Cairo_NATIVE(memmove___3DJJ)(JNIEnv *env, jclass that, jdoubleArray arg0, jintLong arg1, jintLong arg2)
#endif
{
	jdouble *lparg0=NULL;
#ifndef JNI64
	Cairo_NATIVE_ENTER(env, that, memmove___3DII_FUNC);
#else
	Cairo_NATIVE_ENTER(env, that, memmove___3DJJ_FUNC);
#endif
	if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
#ifndef JNI64
	Cairo_NATIVE_EXIT(env, that, memmove___3DII_FUNC);
#else
	Cairo_NATIVE_EXIT(env, that, memmove___3DJJ_FUNC);
#endif
}
#endif


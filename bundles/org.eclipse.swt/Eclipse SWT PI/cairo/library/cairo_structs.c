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

#ifndef NO_cairo_font_extents_t
typedef struct cairo_font_extents_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID ascent, descent, height, max_x_advance, max_y_advance;
} cairo_font_extents_t_FID_CACHE;

cairo_font_extents_t_FID_CACHE cairo_font_extents_tFc;

void cachecairo_font_extents_tFields(JNIEnv *env, jobject lpObject)
{
	if (cairo_font_extents_tFc.cached) return;
	cairo_font_extents_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	cairo_font_extents_tFc.ascent = (*env)->GetFieldID(env, cairo_font_extents_tFc.clazz, "ascent", "F");
	cairo_font_extents_tFc.descent = (*env)->GetFieldID(env, cairo_font_extents_tFc.clazz, "descent", "F");
	cairo_font_extents_tFc.height = (*env)->GetFieldID(env, cairo_font_extents_tFc.clazz, "height", "F");
	cairo_font_extents_tFc.max_x_advance = (*env)->GetFieldID(env, cairo_font_extents_tFc.clazz, "max_x_advance", "F");
	cairo_font_extents_tFc.max_y_advance = (*env)->GetFieldID(env, cairo_font_extents_tFc.clazz, "max_y_advance", "F");
	cairo_font_extents_tFc.cached = 1;
}

cairo_font_extents_t *getcairo_font_extents_tFields(JNIEnv *env, jobject lpObject, cairo_font_extents_t *lpStruct)
{
	if (!cairo_font_extents_tFc.cached) cachecairo_font_extents_tFields(env, lpObject);
	lpStruct->ascent = (*env)->GetFloatField(env, lpObject, cairo_font_extents_tFc.ascent);
	lpStruct->descent = (*env)->GetFloatField(env, lpObject, cairo_font_extents_tFc.descent);
	lpStruct->height = (*env)->GetFloatField(env, lpObject, cairo_font_extents_tFc.height);
	lpStruct->max_x_advance = (*env)->GetFloatField(env, lpObject, cairo_font_extents_tFc.max_x_advance);
	lpStruct->max_y_advance = (*env)->GetFloatField(env, lpObject, cairo_font_extents_tFc.max_y_advance);
	return lpStruct;
}

void setcairo_font_extents_tFields(JNIEnv *env, jobject lpObject, cairo_font_extents_t *lpStruct)
{
	if (!cairo_font_extents_tFc.cached) cachecairo_font_extents_tFields(env, lpObject);
	(*env)->SetFloatField(env, lpObject, cairo_font_extents_tFc.ascent, (jfloat)lpStruct->ascent);
	(*env)->SetFloatField(env, lpObject, cairo_font_extents_tFc.descent, (jfloat)lpStruct->descent);
	(*env)->SetFloatField(env, lpObject, cairo_font_extents_tFc.height, (jfloat)lpStruct->height);
	(*env)->SetFloatField(env, lpObject, cairo_font_extents_tFc.max_x_advance, (jfloat)lpStruct->max_x_advance);
	(*env)->SetFloatField(env, lpObject, cairo_font_extents_tFc.max_y_advance, (jfloat)lpStruct->max_y_advance);
}
#endif


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
	cairo_font_extents_tFc.ascent = (*env)->GetFieldID(env, cairo_font_extents_tFc.clazz, "ascent", "D");
	cairo_font_extents_tFc.descent = (*env)->GetFieldID(env, cairo_font_extents_tFc.clazz, "descent", "D");
	cairo_font_extents_tFc.height = (*env)->GetFieldID(env, cairo_font_extents_tFc.clazz, "height", "D");
	cairo_font_extents_tFc.max_x_advance = (*env)->GetFieldID(env, cairo_font_extents_tFc.clazz, "max_x_advance", "D");
	cairo_font_extents_tFc.max_y_advance = (*env)->GetFieldID(env, cairo_font_extents_tFc.clazz, "max_y_advance", "D");
	cairo_font_extents_tFc.cached = 1;
}

cairo_font_extents_t *getcairo_font_extents_tFields(JNIEnv *env, jobject lpObject, cairo_font_extents_t *lpStruct)
{
	if (!cairo_font_extents_tFc.cached) cachecairo_font_extents_tFields(env, lpObject);
	lpStruct->ascent = (*env)->GetDoubleField(env, lpObject, cairo_font_extents_tFc.ascent);
	lpStruct->descent = (*env)->GetDoubleField(env, lpObject, cairo_font_extents_tFc.descent);
	lpStruct->height = (*env)->GetDoubleField(env, lpObject, cairo_font_extents_tFc.height);
	lpStruct->max_x_advance = (*env)->GetDoubleField(env, lpObject, cairo_font_extents_tFc.max_x_advance);
	lpStruct->max_y_advance = (*env)->GetDoubleField(env, lpObject, cairo_font_extents_tFc.max_y_advance);
	return lpStruct;
}

void setcairo_font_extents_tFields(JNIEnv *env, jobject lpObject, cairo_font_extents_t *lpStruct)
{
	if (!cairo_font_extents_tFc.cached) cachecairo_font_extents_tFields(env, lpObject);
	(*env)->SetDoubleField(env, lpObject, cairo_font_extents_tFc.ascent, (jdouble)lpStruct->ascent);
	(*env)->SetDoubleField(env, lpObject, cairo_font_extents_tFc.descent, (jdouble)lpStruct->descent);
	(*env)->SetDoubleField(env, lpObject, cairo_font_extents_tFc.height, (jdouble)lpStruct->height);
	(*env)->SetDoubleField(env, lpObject, cairo_font_extents_tFc.max_x_advance, (jdouble)lpStruct->max_x_advance);
	(*env)->SetDoubleField(env, lpObject, cairo_font_extents_tFc.max_y_advance, (jdouble)lpStruct->max_y_advance);
}
#endif

#ifndef NO_cairo_path_data_t
typedef struct cairo_path_data_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, length;
} cairo_path_data_t_FID_CACHE;

cairo_path_data_t_FID_CACHE cairo_path_data_tFc;

void cachecairo_path_data_tFields(JNIEnv *env, jobject lpObject)
{
	if (cairo_path_data_tFc.cached) return;
	cairo_path_data_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	cairo_path_data_tFc.type = (*env)->GetFieldID(env, cairo_path_data_tFc.clazz, "type", "I");
	cairo_path_data_tFc.length = (*env)->GetFieldID(env, cairo_path_data_tFc.clazz, "length", "I");
	cairo_path_data_tFc.cached = 1;
}

cairo_path_data_t *getcairo_path_data_tFields(JNIEnv *env, jobject lpObject, cairo_path_data_t *lpStruct)
{
	if (!cairo_path_data_tFc.cached) cachecairo_path_data_tFields(env, lpObject);
	lpStruct->header.type = (*env)->GetIntField(env, lpObject, cairo_path_data_tFc.type);
	lpStruct->header.length = (*env)->GetIntField(env, lpObject, cairo_path_data_tFc.length);
	return lpStruct;
}

void setcairo_path_data_tFields(JNIEnv *env, jobject lpObject, cairo_path_data_t *lpStruct)
{
	if (!cairo_path_data_tFc.cached) cachecairo_path_data_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, cairo_path_data_tFc.type, (jint)lpStruct->header.type);
	(*env)->SetIntField(env, lpObject, cairo_path_data_tFc.length, (jint)lpStruct->header.length);
}
#endif

#ifndef NO_cairo_path_t
typedef struct cairo_path_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID status, data, num_data;
} cairo_path_t_FID_CACHE;

cairo_path_t_FID_CACHE cairo_path_tFc;

void cachecairo_path_tFields(JNIEnv *env, jobject lpObject)
{
	if (cairo_path_tFc.cached) return;
	cairo_path_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	cairo_path_tFc.status = (*env)->GetFieldID(env, cairo_path_tFc.clazz, "status", "I");
	cairo_path_tFc.data = (*env)->GetFieldID(env, cairo_path_tFc.clazz, "data", I_J);
	cairo_path_tFc.num_data = (*env)->GetFieldID(env, cairo_path_tFc.clazz, "num_data", "I");
	cairo_path_tFc.cached = 1;
}

cairo_path_t *getcairo_path_tFields(JNIEnv *env, jobject lpObject, cairo_path_t *lpStruct)
{
	if (!cairo_path_tFc.cached) cachecairo_path_tFields(env, lpObject);
	lpStruct->status = (*env)->GetIntField(env, lpObject, cairo_path_tFc.status);
	lpStruct->data = (cairo_path_data_t *)(*env)->GetIntLongField(env, lpObject, cairo_path_tFc.data);
	lpStruct->num_data = (*env)->GetIntField(env, lpObject, cairo_path_tFc.num_data);
	return lpStruct;
}

void setcairo_path_tFields(JNIEnv *env, jobject lpObject, cairo_path_t *lpStruct)
{
	if (!cairo_path_tFc.cached) cachecairo_path_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, cairo_path_tFc.status, (jint)lpStruct->status);
	(*env)->SetIntLongField(env, lpObject, cairo_path_tFc.data, (jintLong)lpStruct->data);
	(*env)->SetIntField(env, lpObject, cairo_path_tFc.num_data, (jint)lpStruct->num_data);
}
#endif

#ifndef NO_cairo_text_extents_t
typedef struct cairo_text_extents_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x_bearing, y_bearing, width, height, x_advance, y_advance;
} cairo_text_extents_t_FID_CACHE;

cairo_text_extents_t_FID_CACHE cairo_text_extents_tFc;

void cachecairo_text_extents_tFields(JNIEnv *env, jobject lpObject)
{
	if (cairo_text_extents_tFc.cached) return;
	cairo_text_extents_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	cairo_text_extents_tFc.x_bearing = (*env)->GetFieldID(env, cairo_text_extents_tFc.clazz, "x_bearing", "D");
	cairo_text_extents_tFc.y_bearing = (*env)->GetFieldID(env, cairo_text_extents_tFc.clazz, "y_bearing", "D");
	cairo_text_extents_tFc.width = (*env)->GetFieldID(env, cairo_text_extents_tFc.clazz, "width", "D");
	cairo_text_extents_tFc.height = (*env)->GetFieldID(env, cairo_text_extents_tFc.clazz, "height", "D");
	cairo_text_extents_tFc.x_advance = (*env)->GetFieldID(env, cairo_text_extents_tFc.clazz, "x_advance", "D");
	cairo_text_extents_tFc.y_advance = (*env)->GetFieldID(env, cairo_text_extents_tFc.clazz, "y_advance", "D");
	cairo_text_extents_tFc.cached = 1;
}

cairo_text_extents_t *getcairo_text_extents_tFields(JNIEnv *env, jobject lpObject, cairo_text_extents_t *lpStruct)
{
	if (!cairo_text_extents_tFc.cached) cachecairo_text_extents_tFields(env, lpObject);
	lpStruct->x_bearing = (*env)->GetDoubleField(env, lpObject, cairo_text_extents_tFc.x_bearing);
	lpStruct->y_bearing = (*env)->GetDoubleField(env, lpObject, cairo_text_extents_tFc.y_bearing);
	lpStruct->width = (*env)->GetDoubleField(env, lpObject, cairo_text_extents_tFc.width);
	lpStruct->height = (*env)->GetDoubleField(env, lpObject, cairo_text_extents_tFc.height);
	lpStruct->x_advance = (*env)->GetDoubleField(env, lpObject, cairo_text_extents_tFc.x_advance);
	lpStruct->y_advance = (*env)->GetDoubleField(env, lpObject, cairo_text_extents_tFc.y_advance);
	return lpStruct;
}

void setcairo_text_extents_tFields(JNIEnv *env, jobject lpObject, cairo_text_extents_t *lpStruct)
{
	if (!cairo_text_extents_tFc.cached) cachecairo_text_extents_tFields(env, lpObject);
	(*env)->SetDoubleField(env, lpObject, cairo_text_extents_tFc.x_bearing, (jdouble)lpStruct->x_bearing);
	(*env)->SetDoubleField(env, lpObject, cairo_text_extents_tFc.y_bearing, (jdouble)lpStruct->y_bearing);
	(*env)->SetDoubleField(env, lpObject, cairo_text_extents_tFc.width, (jdouble)lpStruct->width);
	(*env)->SetDoubleField(env, lpObject, cairo_text_extents_tFc.height, (jdouble)lpStruct->height);
	(*env)->SetDoubleField(env, lpObject, cairo_text_extents_tFc.x_advance, (jdouble)lpStruct->x_advance);
	(*env)->SetDoubleField(env, lpObject, cairo_text_extents_tFc.y_advance, (jdouble)lpStruct->y_advance);
}
#endif


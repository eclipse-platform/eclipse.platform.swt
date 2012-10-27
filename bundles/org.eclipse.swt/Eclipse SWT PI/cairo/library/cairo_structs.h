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

#include "cairo.h"

#ifndef NO_cairo_font_extents_t
void cachecairo_font_extents_tFields(JNIEnv *env, jobject lpObject);
cairo_font_extents_t *getcairo_font_extents_tFields(JNIEnv *env, jobject lpObject, cairo_font_extents_t *lpStruct);
void setcairo_font_extents_tFields(JNIEnv *env, jobject lpObject, cairo_font_extents_t *lpStruct);
#define cairo_font_extents_t_sizeof() sizeof(cairo_font_extents_t)
#else
#define cachecairo_font_extents_tFields(a,b)
#define getcairo_font_extents_tFields(a,b,c) NULL
#define setcairo_font_extents_tFields(a,b,c)
#define cairo_font_extents_t_sizeof() 0
#endif

#ifndef NO_cairo_path_data_t
void cachecairo_path_data_tFields(JNIEnv *env, jobject lpObject);
cairo_path_data_t *getcairo_path_data_tFields(JNIEnv *env, jobject lpObject, cairo_path_data_t *lpStruct);
void setcairo_path_data_tFields(JNIEnv *env, jobject lpObject, cairo_path_data_t *lpStruct);
#define cairo_path_data_t_sizeof() sizeof(cairo_path_data_t)
#else
#define cachecairo_path_data_tFields(a,b)
#define getcairo_path_data_tFields(a,b,c) NULL
#define setcairo_path_data_tFields(a,b,c)
#define cairo_path_data_t_sizeof() 0
#endif

#ifndef NO_cairo_path_t
void cachecairo_path_tFields(JNIEnv *env, jobject lpObject);
cairo_path_t *getcairo_path_tFields(JNIEnv *env, jobject lpObject, cairo_path_t *lpStruct);
void setcairo_path_tFields(JNIEnv *env, jobject lpObject, cairo_path_t *lpStruct);
#define cairo_path_t_sizeof() sizeof(cairo_path_t)
#else
#define cachecairo_path_tFields(a,b)
#define getcairo_path_tFields(a,b,c) NULL
#define setcairo_path_tFields(a,b,c)
#define cairo_path_t_sizeof() 0
#endif

#ifndef NO_cairo_text_extents_t
void cachecairo_text_extents_tFields(JNIEnv *env, jobject lpObject);
cairo_text_extents_t *getcairo_text_extents_tFields(JNIEnv *env, jobject lpObject, cairo_text_extents_t *lpStruct);
void setcairo_text_extents_tFields(JNIEnv *env, jobject lpObject, cairo_text_extents_t *lpStruct);
#define cairo_text_extents_t_sizeof() sizeof(cairo_text_extents_t)
#else
#define cachecairo_text_extents_tFields(a,b)
#define getcairo_text_extents_tFields(a,b,c) NULL
#define setcairo_text_extents_tFields(a,b,c)
#define cairo_text_extents_t_sizeof() 0
#endif


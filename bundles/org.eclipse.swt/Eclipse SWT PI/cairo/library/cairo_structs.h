/*******************************************************************************
* Copyright (c) 2000, 2004 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

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


/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
#include <X11/X.h>
#include <X11/Xlib.h>
#include "swt.h"
#include "structs.h"

typedef struct XVisualInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID visual, visualid, screen, depth, cclass, red_mask, green_mask, blue_mask, colormap_size, bits_per_rgb;
} XVisualInfo_FID_CACHE;
typedef XVisualInfo_FID_CACHE *PXVisualInfo_FID_CACHE;

XVisualInfo_FID_CACHE XVisualInfoFc;

void cacheXVisualInfoFids(JNIEnv *env, jobject lpObject, PXVisualInfo_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->visual = (*env)->GetFieldID(env, lpCache->clazz, "visual", "I");
	lpCache->visualid = (*env)->GetFieldID(env, lpCache->clazz, "visualid", "I");
	lpCache->screen = (*env)->GetFieldID(env, lpCache->clazz, "screen", "I");
	lpCache->depth = (*env)->GetFieldID(env, lpCache->clazz, "depth", "I");
	lpCache->cclass = (*env)->GetFieldID(env, lpCache->clazz, "cclass", "I");
	lpCache->red_mask = (*env)->GetFieldID(env, lpCache->clazz, "red_mask", "I");
	lpCache->green_mask = (*env)->GetFieldID(env, lpCache->clazz, "green_mask", "I");
	lpCache->blue_mask = (*env)->GetFieldID(env, lpCache->clazz, "blue_mask", "I");
	lpCache->colormap_size = (*env)->GetFieldID(env, lpCache->clazz, "colormap_size", "I");
	lpCache->bits_per_rgb = (*env)->GetFieldID(env, lpCache->clazz, "bits_per_rgb", "I");
	lpCache->cached = 1;
}

XVisualInfo *getXVisualInfoFields(JNIEnv *env, jobject lpObject, XVisualInfo *lpStruct)
{
	PXVisualInfo_FID_CACHE lpCache = &XVisualInfoFc;
	if (!lpCache->cached) cacheXVisualInfoFids(env, lpObject, lpCache);
	lpStruct->visual = (Visual *)(*env)->GetIntField(env, lpObject, lpCache->visual);
	lpStruct->visualid = (*env)->GetIntField(env, lpObject, lpCache->visualid);
	lpStruct->screen = (*env)->GetIntField(env, lpObject, lpCache->screen);
	lpStruct->depth = (*env)->GetIntField(env, lpObject, lpCache->depth);
	lpStruct->class = (*env)->GetIntField(env, lpObject, lpCache->cclass);
	lpStruct->red_mask = (*env)->GetIntField(env, lpObject, lpCache->red_mask);
	lpStruct->green_mask = (*env)->GetIntField(env, lpObject, lpCache->green_mask);
	lpStruct->blue_mask = (*env)->GetIntField(env, lpObject, lpCache->blue_mask);
	lpStruct->colormap_size = (*env)->GetIntField(env, lpObject, lpCache->colormap_size);
	lpStruct->bits_per_rgb = (*env)->GetIntField(env, lpObject, lpCache->bits_per_rgb);
	return lpStruct;
}

void setXVisualInfoFields(JNIEnv *env, jobject lpObject, XVisualInfo *lpStruct)
{
	PXVisualInfo_FID_CACHE lpCache = &XVisualInfoFc;
	if (!lpCache->cached) cacheXVisualInfoFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->visual, (jint)lpStruct->visual);
	(*env)->SetIntField(env, lpObject, lpCache->visualid, (jint)lpStruct->visualid);
	(*env)->SetIntField(env, lpObject, lpCache->screen, (jint)lpStruct->screen);
	(*env)->SetIntField(env, lpObject, lpCache->depth, (jint)lpStruct->depth);
	(*env)->SetIntField(env, lpObject, lpCache->cclass, (jint)lpStruct->class);
	(*env)->SetIntField(env, lpObject, lpCache->red_mask, (jint)lpStruct->red_mask);
	(*env)->SetIntField(env, lpObject, lpCache->green_mask, (jint)lpStruct->green_mask);
	(*env)->SetIntField(env, lpObject, lpCache->blue_mask, (jint)lpStruct->blue_mask);
	(*env)->SetIntField(env, lpObject, lpCache->colormap_size, (jint)lpStruct->colormap_size);
	(*env)->SetIntField(env, lpObject, lpCache->bits_per_rgb, (jint)lpStruct->bits_per_rgb);
}

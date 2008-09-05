/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "glx_structs.h"

#ifndef NO_XVisualInfo
typedef struct XVisualInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID visual, visualid, screen, depth, cclass, red_mask, green_mask, blue_mask, colormap_size, bits_per_rgb;
} XVisualInfo_FID_CACHE;

XVisualInfo_FID_CACHE XVisualInfoFc;

void cacheXVisualInfoFields(JNIEnv *env, jobject lpObject)
{
	if (XVisualInfoFc.cached) return;
	XVisualInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XVisualInfoFc.visual = (*env)->GetFieldID(env, XVisualInfoFc.clazz, "visual", I_J);
	XVisualInfoFc.visualid = (*env)->GetFieldID(env, XVisualInfoFc.clazz, "visualid", "I");
	XVisualInfoFc.screen = (*env)->GetFieldID(env, XVisualInfoFc.clazz, "screen", "I");
	XVisualInfoFc.depth = (*env)->GetFieldID(env, XVisualInfoFc.clazz, "depth", "I");
	XVisualInfoFc.cclass = (*env)->GetFieldID(env, XVisualInfoFc.clazz, "cclass", "I");
	XVisualInfoFc.red_mask = (*env)->GetFieldID(env, XVisualInfoFc.clazz, "red_mask", "I");
	XVisualInfoFc.green_mask = (*env)->GetFieldID(env, XVisualInfoFc.clazz, "green_mask", "I");
	XVisualInfoFc.blue_mask = (*env)->GetFieldID(env, XVisualInfoFc.clazz, "blue_mask", "I");
	XVisualInfoFc.colormap_size = (*env)->GetFieldID(env, XVisualInfoFc.clazz, "colormap_size", "I");
	XVisualInfoFc.bits_per_rgb = (*env)->GetFieldID(env, XVisualInfoFc.clazz, "bits_per_rgb", "I");
	XVisualInfoFc.cached = 1;
}

XVisualInfo *getXVisualInfoFields(JNIEnv *env, jobject lpObject, XVisualInfo *lpStruct)
{
	if (!XVisualInfoFc.cached) cacheXVisualInfoFields(env, lpObject);
	lpStruct->visual = (Visual *)(*env)->GetIntLongField(env, lpObject, XVisualInfoFc.visual);
	lpStruct->visualid = (*env)->GetIntField(env, lpObject, XVisualInfoFc.visualid);
	lpStruct->screen = (*env)->GetIntField(env, lpObject, XVisualInfoFc.screen);
	lpStruct->depth = (*env)->GetIntField(env, lpObject, XVisualInfoFc.depth);
	lpStruct->class = (*env)->GetIntField(env, lpObject, XVisualInfoFc.cclass);
	lpStruct->red_mask = (*env)->GetIntField(env, lpObject, XVisualInfoFc.red_mask);
	lpStruct->green_mask = (*env)->GetIntField(env, lpObject, XVisualInfoFc.green_mask);
	lpStruct->blue_mask = (*env)->GetIntField(env, lpObject, XVisualInfoFc.blue_mask);
	lpStruct->colormap_size = (*env)->GetIntField(env, lpObject, XVisualInfoFc.colormap_size);
	lpStruct->bits_per_rgb = (*env)->GetIntField(env, lpObject, XVisualInfoFc.bits_per_rgb);
	return lpStruct;
}

void setXVisualInfoFields(JNIEnv *env, jobject lpObject, XVisualInfo *lpStruct)
{
	if (!XVisualInfoFc.cached) cacheXVisualInfoFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, XVisualInfoFc.visual, (jintLong)lpStruct->visual);
	(*env)->SetIntField(env, lpObject, XVisualInfoFc.visualid, (jint)lpStruct->visualid);
	(*env)->SetIntField(env, lpObject, XVisualInfoFc.screen, (jint)lpStruct->screen);
	(*env)->SetIntField(env, lpObject, XVisualInfoFc.depth, (jint)lpStruct->depth);
	(*env)->SetIntField(env, lpObject, XVisualInfoFc.cclass, (jint)lpStruct->class);
	(*env)->SetIntField(env, lpObject, XVisualInfoFc.red_mask, (jint)lpStruct->red_mask);
	(*env)->SetIntField(env, lpObject, XVisualInfoFc.green_mask, (jint)lpStruct->green_mask);
	(*env)->SetIntField(env, lpObject, XVisualInfoFc.blue_mask, (jint)lpStruct->blue_mask);
	(*env)->SetIntField(env, lpObject, XVisualInfoFc.colormap_size, (jint)lpStruct->colormap_size);
	(*env)->SetIntField(env, lpObject, XVisualInfoFc.bits_per_rgb, (jint)lpStruct->bits_per_rgb);
}
#endif


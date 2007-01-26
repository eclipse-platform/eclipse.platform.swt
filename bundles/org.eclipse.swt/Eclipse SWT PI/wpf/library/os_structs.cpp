/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "os_structs.h"

#ifndef NO_ICONINFO
typedef struct ICONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fIcon, xHotspot, yHotspot, hbmMask, hbmColor;
} ICONINFO_FID_CACHE;

ICONINFO_FID_CACHE ICONINFOFc;

void cacheICONINFOFields(JNIEnv *env, jobject lpObject)
{
	if (ICONINFOFc.cached) return;
	ICONINFOFc.clazz = env->GetObjectClass(lpObject);
	ICONINFOFc.fIcon = env->GetFieldID(ICONINFOFc.clazz, "fIcon", "Z");
	ICONINFOFc.xHotspot = env->GetFieldID(ICONINFOFc.clazz, "xHotspot", "I");
	ICONINFOFc.yHotspot = env->GetFieldID(ICONINFOFc.clazz, "yHotspot", "I");
	ICONINFOFc.hbmMask = env->GetFieldID(ICONINFOFc.clazz, "hbmMask", "I");
	ICONINFOFc.hbmColor = env->GetFieldID(ICONINFOFc.clazz, "hbmColor", "I");
	ICONINFOFc.cached = 1;
}

ICONINFO *getICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct)
{
	if (!ICONINFOFc.cached) cacheICONINFOFields(env, lpObject);
	lpStruct->fIcon = env->GetBooleanField(lpObject, ICONINFOFc.fIcon);
	lpStruct->xHotspot = env->GetIntField(lpObject, ICONINFOFc.xHotspot);
	lpStruct->yHotspot = env->GetIntField(lpObject, ICONINFOFc.yHotspot);
	lpStruct->hbmMask = (HBITMAP)env->GetIntField(lpObject, ICONINFOFc.hbmMask);
	lpStruct->hbmColor = (HBITMAP)env->GetIntField(lpObject, ICONINFOFc.hbmColor);
	return lpStruct;
}

void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct)
{
	if (!ICONINFOFc.cached) cacheICONINFOFields(env, lpObject);
	env->SetBooleanField(lpObject, ICONINFOFc.fIcon, (jboolean)lpStruct->fIcon);
	env->SetIntField(lpObject, ICONINFOFc.xHotspot, (jint)lpStruct->xHotspot);
	env->SetIntField(lpObject, ICONINFOFc.yHotspot, (jint)lpStruct->yHotspot);
	env->SetIntField(lpObject, ICONINFOFc.hbmMask, (jint)lpStruct->hbmMask);
	env->SetIntField(lpObject, ICONINFOFc.hbmColor, (jint)lpStruct->hbmColor);
}
#endif

#ifndef NO_POINT
typedef struct POINT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} POINT_FID_CACHE;

POINT_FID_CACHE POINTFc;

void cachePOINTFields(JNIEnv *env, jobject lpObject)
{
	if (POINTFc.cached) return;
	POINTFc.clazz = env->GetObjectClass(lpObject);
	POINTFc.x = env->GetFieldID(POINTFc.clazz, "x", "I");
	POINTFc.y = env->GetFieldID(POINTFc.clazz, "y", "I");
	POINTFc.cached = 1;
}

POINT *getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFields(env, lpObject);
	lpStruct->x = env->GetIntField(lpObject, POINTFc.x);
	lpStruct->y = env->GetIntField(lpObject, POINTFc.y);
	return lpStruct;
}

void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFields(env, lpObject);
	env->SetIntField(lpObject, POINTFc.x, (jint)lpStruct->x);
	env->SetIntField(lpObject, POINTFc.y, (jint)lpStruct->y);
}
#endif


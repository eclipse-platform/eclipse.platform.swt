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

#include "swt.h"
#include "gdip_structs.h"

#ifndef NO_GdiplusStartupInput
typedef struct GdiplusStartupInput_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID GdiplusVersion, DebugEventCallback, SuppressBackgroundThread, SuppressExternalCodecs;
} GdiplusStartupInput_FID_CACHE;

GdiplusStartupInput_FID_CACHE GdiplusStartupInputFc;

void cacheGdiplusStartupInputFields(JNIEnv *env, jobject lpObject)
{
	if (GdiplusStartupInputFc.cached) return;
	GdiplusStartupInputFc.clazz = env->GetObjectClass(lpObject);
	GdiplusStartupInputFc.GdiplusVersion = env->GetFieldID(GdiplusStartupInputFc.clazz, "GdiplusVersion", "I");
	GdiplusStartupInputFc.DebugEventCallback = env->GetFieldID(GdiplusStartupInputFc.clazz, "DebugEventCallback", "I");
	GdiplusStartupInputFc.SuppressBackgroundThread = env->GetFieldID(GdiplusStartupInputFc.clazz, "SuppressBackgroundThread", "I");
	GdiplusStartupInputFc.SuppressExternalCodecs = env->GetFieldID(GdiplusStartupInputFc.clazz, "SuppressExternalCodecs", "I");
	GdiplusStartupInputFc.cached = 1;
}

GdiplusStartupInput *getGdiplusStartupInputFields(JNIEnv *env, jobject lpObject, GdiplusStartupInput *lpStruct)
{
	if (!GdiplusStartupInputFc.cached) cacheGdiplusStartupInputFields(env, lpObject);
	lpStruct->GdiplusVersion = env->GetIntField(lpObject, GdiplusStartupInputFc.GdiplusVersion);
	lpStruct->DebugEventCallback = (DebugEventProc)env->GetIntField(lpObject, GdiplusStartupInputFc.DebugEventCallback);
	lpStruct->SuppressBackgroundThread = (BOOL)env->GetIntField(lpObject, GdiplusStartupInputFc.SuppressBackgroundThread);
	lpStruct->SuppressExternalCodecs = (BOOL)env->GetIntField(lpObject, GdiplusStartupInputFc.SuppressExternalCodecs);
	return lpStruct;
}

void setGdiplusStartupInputFields(JNIEnv *env, jobject lpObject, GdiplusStartupInput *lpStruct)
{
	if (!GdiplusStartupInputFc.cached) cacheGdiplusStartupInputFields(env, lpObject);
	env->SetIntField(lpObject, GdiplusStartupInputFc.GdiplusVersion, (jint)lpStruct->GdiplusVersion);
	env->SetIntField(lpObject, GdiplusStartupInputFc.DebugEventCallback, (jint)lpStruct->DebugEventCallback);
	env->SetIntField(lpObject, GdiplusStartupInputFc.SuppressBackgroundThread, (jint)lpStruct->SuppressBackgroundThread);
	env->SetIntField(lpObject, GdiplusStartupInputFc.SuppressExternalCodecs, (jint)lpStruct->SuppressExternalCodecs);
}
#endif

#ifndef NO_PointF
typedef struct PointF_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID X, Y;
} PointF_FID_CACHE;

PointF_FID_CACHE PointFFc;

void cachePointFFields(JNIEnv *env, jobject lpObject)
{
	if (PointFFc.cached) return;
	PointFFc.clazz = env->GetObjectClass(lpObject);
	PointFFc.X = env->GetFieldID(PointFFc.clazz, "X", "F");
	PointFFc.Y = env->GetFieldID(PointFFc.clazz, "Y", "F");
	PointFFc.cached = 1;
}

PointF *getPointFFields(JNIEnv *env, jobject lpObject, PointF *lpStruct)
{
	if (!PointFFc.cached) cachePointFFields(env, lpObject);
	lpStruct->X = (REAL)env->GetFloatField(lpObject, PointFFc.X);
	lpStruct->Y = (REAL)env->GetFloatField(lpObject, PointFFc.Y);
	return lpStruct;
}

void setPointFFields(JNIEnv *env, jobject lpObject, PointF *lpStruct)
{
	if (!PointFFc.cached) cachePointFFields(env, lpObject);
	env->SetFloatField(lpObject, PointFFc.X, (jfloat)lpStruct->X);
	env->SetFloatField(lpObject, PointFFc.Y, (jfloat)lpStruct->Y);
}
#endif

#ifndef NO_Rect
typedef struct Rect_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID X, Y, Width, Height;
} Rect_FID_CACHE;

Rect_FID_CACHE RectFc;

void cacheRectFields(JNIEnv *env, jobject lpObject)
{
	if (RectFc.cached) return;
	RectFc.clazz = env->GetObjectClass(lpObject);
	RectFc.X = env->GetFieldID(RectFc.clazz, "X", "I");
	RectFc.Y = env->GetFieldID(RectFc.clazz, "Y", "I");
	RectFc.Width = env->GetFieldID(RectFc.clazz, "Width", "I");
	RectFc.Height = env->GetFieldID(RectFc.clazz, "Height", "I");
	RectFc.cached = 1;
}

Rect *getRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct)
{
	if (!RectFc.cached) cacheRectFields(env, lpObject);
	lpStruct->X = env->GetIntField(lpObject, RectFc.X);
	lpStruct->Y = env->GetIntField(lpObject, RectFc.Y);
	lpStruct->Width = env->GetIntField(lpObject, RectFc.Width);
	lpStruct->Height = env->GetIntField(lpObject, RectFc.Height);
	return lpStruct;
}

void setRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct)
{
	if (!RectFc.cached) cacheRectFields(env, lpObject);
	env->SetIntField(lpObject, RectFc.X, (jint)lpStruct->X);
	env->SetIntField(lpObject, RectFc.Y, (jint)lpStruct->Y);
	env->SetIntField(lpObject, RectFc.Width, (jint)lpStruct->Width);
	env->SetIntField(lpObject, RectFc.Height, (jint)lpStruct->Height);
}
#endif

#ifndef NO_RectF
typedef struct RectF_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID X, Y, Width, Height;
} RectF_FID_CACHE;

RectF_FID_CACHE RectFFc;

void cacheRectFFields(JNIEnv *env, jobject lpObject)
{
	if (RectFFc.cached) return;
	RectFFc.clazz = env->GetObjectClass(lpObject);
	RectFFc.X = env->GetFieldID(RectFFc.clazz, "X", "F");
	RectFFc.Y = env->GetFieldID(RectFFc.clazz, "Y", "F");
	RectFFc.Width = env->GetFieldID(RectFFc.clazz, "Width", "F");
	RectFFc.Height = env->GetFieldID(RectFFc.clazz, "Height", "F");
	RectFFc.cached = 1;
}

RectF *getRectFFields(JNIEnv *env, jobject lpObject, RectF *lpStruct)
{
	if (!RectFFc.cached) cacheRectFFields(env, lpObject);
	lpStruct->X = env->GetFloatField(lpObject, RectFFc.X);
	lpStruct->Y = env->GetFloatField(lpObject, RectFFc.Y);
	lpStruct->Width = env->GetFloatField(lpObject, RectFFc.Width);
	lpStruct->Height = env->GetFloatField(lpObject, RectFFc.Height);
	return lpStruct;
}

void setRectFFields(JNIEnv *env, jobject lpObject, RectF *lpStruct)
{
	if (!RectFFc.cached) cacheRectFFields(env, lpObject);
	env->SetFloatField(lpObject, RectFFc.X, (jfloat)lpStruct->X);
	env->SetFloatField(lpObject, RectFFc.Y, (jfloat)lpStruct->Y);
	env->SetFloatField(lpObject, RectFFc.Width, (jfloat)lpStruct->Width);
	env->SetFloatField(lpObject, RectFFc.Height, (jfloat)lpStruct->Height);
}
#endif


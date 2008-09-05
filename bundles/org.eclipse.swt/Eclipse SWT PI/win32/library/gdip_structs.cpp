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
#include "gdip_structs.h"

#ifndef NO_BitmapData
typedef struct BitmapData_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID Width, Height, Stride, PixelFormat, Scan0, Reserved;
} BitmapData_FID_CACHE;

BitmapData_FID_CACHE BitmapDataFc;

void cacheBitmapDataFields(JNIEnv *env, jobject lpObject)
{
	if (BitmapDataFc.cached) return;
	BitmapDataFc.clazz = env->GetObjectClass(lpObject);
	BitmapDataFc.Width = env->GetFieldID(BitmapDataFc.clazz, "Width", "I");
	BitmapDataFc.Height = env->GetFieldID(BitmapDataFc.clazz, "Height", "I");
	BitmapDataFc.Stride = env->GetFieldID(BitmapDataFc.clazz, "Stride", "I");
	BitmapDataFc.PixelFormat = env->GetFieldID(BitmapDataFc.clazz, "PixelFormat", "I");
	BitmapDataFc.Scan0 = env->GetFieldID(BitmapDataFc.clazz, "Scan0", I_J);
	BitmapDataFc.Reserved = env->GetFieldID(BitmapDataFc.clazz, "Reserved", I_J);
	BitmapDataFc.cached = 1;
}

BitmapData *getBitmapDataFields(JNIEnv *env, jobject lpObject, BitmapData *lpStruct)
{
	if (!BitmapDataFc.cached) cacheBitmapDataFields(env, lpObject);
	lpStruct->Width = env->GetIntField(lpObject, BitmapDataFc.Width);
	lpStruct->Height = env->GetIntField(lpObject, BitmapDataFc.Height);
	lpStruct->Stride = env->GetIntField(lpObject, BitmapDataFc.Stride);
	lpStruct->PixelFormat = (PixelFormat)env->GetIntField(lpObject, BitmapDataFc.PixelFormat);
	lpStruct->Scan0 = (void*)env->GetIntLongField(lpObject, BitmapDataFc.Scan0);
	lpStruct->Reserved = (UINT_PTR)env->GetIntLongField(lpObject, BitmapDataFc.Reserved);
	return lpStruct;
}

void setBitmapDataFields(JNIEnv *env, jobject lpObject, BitmapData *lpStruct)
{
	if (!BitmapDataFc.cached) cacheBitmapDataFields(env, lpObject);
	env->SetIntField(lpObject, BitmapDataFc.Width, (jint)lpStruct->Width);
	env->SetIntField(lpObject, BitmapDataFc.Height, (jint)lpStruct->Height);
	env->SetIntField(lpObject, BitmapDataFc.Stride, (jint)lpStruct->Stride);
	env->SetIntField(lpObject, BitmapDataFc.PixelFormat, (jint)lpStruct->PixelFormat);
	env->SetIntLongField(lpObject, BitmapDataFc.Scan0, (jintLong)lpStruct->Scan0);
	env->SetIntLongField(lpObject, BitmapDataFc.Reserved, (jintLong)lpStruct->Reserved);
}
#endif

#ifndef NO_ColorPalette
typedef struct ColorPalette_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID Flags, Count, Entries;
} ColorPalette_FID_CACHE;

ColorPalette_FID_CACHE ColorPaletteFc;

void cacheColorPaletteFields(JNIEnv *env, jobject lpObject)
{
	if (ColorPaletteFc.cached) return;
	ColorPaletteFc.clazz = env->GetObjectClass(lpObject);
	ColorPaletteFc.Flags = env->GetFieldID(ColorPaletteFc.clazz, "Flags", "I");
	ColorPaletteFc.Count = env->GetFieldID(ColorPaletteFc.clazz, "Count", "I");
	ColorPaletteFc.Entries = env->GetFieldID(ColorPaletteFc.clazz, "Entries", "[I");
	ColorPaletteFc.cached = 1;
}

ColorPalette *getColorPaletteFields(JNIEnv *env, jobject lpObject, ColorPalette *lpStruct)
{
	if (!ColorPaletteFc.cached) cacheColorPaletteFields(env, lpObject);
	lpStruct->Flags = env->GetIntField(lpObject, ColorPaletteFc.Flags);
	lpStruct->Count = env->GetIntField(lpObject, ColorPaletteFc.Count);
	{
	jintArray lpObject1 = (jintArray)env->GetObjectField(lpObject, ColorPaletteFc.Entries);
	env->GetIntArrayRegion(lpObject1, 0, sizeof(lpStruct->Entries) / sizeof(jint), (jint *)lpStruct->Entries);
	}
	return lpStruct;
}

void setColorPaletteFields(JNIEnv *env, jobject lpObject, ColorPalette *lpStruct)
{
	if (!ColorPaletteFc.cached) cacheColorPaletteFields(env, lpObject);
	env->SetIntField(lpObject, ColorPaletteFc.Flags, (jint)lpStruct->Flags);
	env->SetIntField(lpObject, ColorPaletteFc.Count, (jint)lpStruct->Count);
	{
	jintArray lpObject1 = (jintArray)env->GetObjectField(lpObject, ColorPaletteFc.Entries);
	env->SetIntArrayRegion(lpObject1, 0, sizeof(lpStruct->Entries) / sizeof(jint), (jint *)lpStruct->Entries);
	}
}
#endif

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
	GdiplusStartupInputFc.DebugEventCallback = env->GetFieldID(GdiplusStartupInputFc.clazz, "DebugEventCallback", I_J);
	GdiplusStartupInputFc.SuppressBackgroundThread = env->GetFieldID(GdiplusStartupInputFc.clazz, "SuppressBackgroundThread", "Z");
	GdiplusStartupInputFc.SuppressExternalCodecs = env->GetFieldID(GdiplusStartupInputFc.clazz, "SuppressExternalCodecs", "Z");
	GdiplusStartupInputFc.cached = 1;
}

GdiplusStartupInput *getGdiplusStartupInputFields(JNIEnv *env, jobject lpObject, GdiplusStartupInput *lpStruct)
{
	if (!GdiplusStartupInputFc.cached) cacheGdiplusStartupInputFields(env, lpObject);
	lpStruct->GdiplusVersion = env->GetIntField(lpObject, GdiplusStartupInputFc.GdiplusVersion);
	lpStruct->DebugEventCallback = (DebugEventProc)env->GetIntLongField(lpObject, GdiplusStartupInputFc.DebugEventCallback);
	lpStruct->SuppressBackgroundThread = (BOOL)env->GetBooleanField(lpObject, GdiplusStartupInputFc.SuppressBackgroundThread);
	lpStruct->SuppressExternalCodecs = (BOOL)env->GetBooleanField(lpObject, GdiplusStartupInputFc.SuppressExternalCodecs);
	return lpStruct;
}

void setGdiplusStartupInputFields(JNIEnv *env, jobject lpObject, GdiplusStartupInput *lpStruct)
{
	if (!GdiplusStartupInputFc.cached) cacheGdiplusStartupInputFields(env, lpObject);
	env->SetIntField(lpObject, GdiplusStartupInputFc.GdiplusVersion, (jint)lpStruct->GdiplusVersion);
	env->SetIntLongField(lpObject, GdiplusStartupInputFc.DebugEventCallback, (jintLong)lpStruct->DebugEventCallback);
	env->SetBooleanField(lpObject, GdiplusStartupInputFc.SuppressBackgroundThread, (jboolean)lpStruct->SuppressBackgroundThread);
	env->SetBooleanField(lpObject, GdiplusStartupInputFc.SuppressExternalCodecs, (jboolean)lpStruct->SuppressExternalCodecs);
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


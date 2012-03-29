/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
#include "gdip_stats.h"

#ifndef Gdip_NATIVE
#define Gdip_NATIVE(func) Java_org_eclipse_swt_internal_gdip_Gdip_##func
#endif

#ifndef NO_BitmapData_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(BitmapData_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(BitmapData_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, BitmapData_1delete_FUNC);
	delete (BitmapData *)arg0;
	Gdip_NATIVE_EXIT(env, that, BitmapData_1delete_FUNC);
}
#endif

#ifndef NO_BitmapData_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(BitmapData_1new)(JNIEnv *env, jclass that);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(BitmapData_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, BitmapData_1new_FUNC);
	rc = (jintLong)new BitmapData();
	Gdip_NATIVE_EXIT(env, that, BitmapData_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Bitmap_1GetHBITMAP
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Bitmap_1GetHBITMAP)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Bitmap_1GetHBITMAP)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Bitmap_1GetHBITMAP_FUNC);
	if (arg2) if ((lparg2 = env->GetIntLongArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((Bitmap*)arg0)->GetHBITMAP(*(Color*)arg1, (HBITMAP*)lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseIntLongArrayElements(arg2, lparg2, 0);
	Gdip_NATIVE_EXIT(env, that, Bitmap_1GetHBITMAP_FUNC);
	return rc;
}
#endif

#ifndef NO_Bitmap_1GetHICON
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Bitmap_1GetHICON)(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Bitmap_1GetHICON)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Bitmap_1GetHICON_FUNC);
	if (arg1) if ((lparg1 = env->GetIntLongArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jint)((Bitmap*)arg0)->GetHICON((HICON*)lparg1);
fail:
	if (arg1 && lparg1) env->ReleaseIntLongArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, Bitmap_1GetHICON_FUNC);
	return rc;
}
#endif

#ifndef NO_Bitmap_1LockBits
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Bitmap_1LockBits)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jintLong arg4);
JNIEXPORT jint JNICALL Gdip_NATIVE(Bitmap_1LockBits)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jintLong arg4)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Bitmap_1LockBits_FUNC);
	rc = (jint)((Bitmap*)arg0)->LockBits((Rect*)arg1, arg2, (PixelFormat)arg3, (BitmapData*)arg4);
	Gdip_NATIVE_EXIT(env, that, Bitmap_1LockBits_FUNC);
	return rc;
}
#endif

#ifndef NO_Bitmap_1UnlockBits
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Bitmap_1UnlockBits)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Bitmap_1UnlockBits)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Bitmap_1UnlockBits_FUNC);
	rc = (jint)((Bitmap*)arg0)->UnlockBits((BitmapData*)arg1);
	Gdip_NATIVE_EXIT(env, that, Bitmap_1UnlockBits_FUNC);
	return rc;
}
#endif

#ifndef NO_Bitmap_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(Bitmap_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(Bitmap_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, Bitmap_1delete_FUNC);
	delete (Bitmap *)arg0;
	Gdip_NATIVE_EXIT(env, that, Bitmap_1delete_FUNC);
}
#endif

#if (!defined(NO_Bitmap_1new__I) && !defined(JNI64)) || (!defined(NO_Bitmap_1new__J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new__I)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new__I)(JNIEnv *env, jclass that, jintLong arg0)
#else
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new__J)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new__J)(JNIEnv *env, jclass that, jintLong arg0)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Bitmap_1new__I_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Bitmap_1new__J_FUNC);
#endif
	rc = (jintLong)new Bitmap((HICON)arg0);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Bitmap_1new__I_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Bitmap_1new__J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_Bitmap_1new__II) && !defined(JNI64)) || (!defined(NO_Bitmap_1new__JJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new__II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new__II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#else
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new__JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new__JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Bitmap_1new__II_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Bitmap_1new__JJ_FUNC);
#endif
	rc = (jintLong)new Bitmap((HBITMAP)arg0, (HPALETTE)arg1);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Bitmap_1new__II_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Bitmap_1new__JJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_Bitmap_1new__IIIII) && !defined(JNI64)) || (!defined(NO_Bitmap_1new__IIIIJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new__IIIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintLong arg4);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new__IIIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintLong arg4)
#else
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new__IIIIJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintLong arg4);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new__IIIIJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintLong arg4)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Bitmap_1new__IIIII_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Bitmap_1new__IIIIJ_FUNC);
#endif
	rc = (jintLong)new Bitmap(arg0, arg1, arg2, (PixelFormat)arg3, (BYTE *)arg4);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Bitmap_1new__IIIII_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Bitmap_1new__IIIIJ_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_Bitmap_1new___3CZ
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new___3CZ)(JNIEnv *env, jclass that, jcharArray arg0, jboolean arg1);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Bitmap_1new___3CZ)
	(JNIEnv *env, jclass that, jcharArray arg0, jboolean arg1)
{
	jchar *lparg0=NULL;
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, Bitmap_1new___3CZ_FUNC);
	if (arg0) if ((lparg0 = env->GetCharArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)new Bitmap((WCHAR*)lparg0, arg1);
fail:
	if (arg0 && lparg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
	Gdip_NATIVE_EXIT(env, that, Bitmap_1new___3CZ_FUNC);
	return rc;
}
#endif

#ifndef NO_Brush_1Clone
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Brush_1Clone)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Brush_1Clone)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, Brush_1Clone_FUNC);
	rc = (jintLong)((Brush *)arg0)->Clone();
	Gdip_NATIVE_EXIT(env, that, Brush_1Clone_FUNC);
	return rc;
}
#endif

#ifndef NO_Brush_1GetType
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Brush_1GetType)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Brush_1GetType)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Brush_1GetType_FUNC);
	rc = (jint)((Brush *)arg0)->GetType();
	Gdip_NATIVE_EXIT(env, that, Brush_1GetType_FUNC);
	return rc;
}
#endif

#ifndef NO_ColorPalette_1sizeof
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(ColorPalette_1sizeof)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL Gdip_NATIVE(ColorPalette_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, ColorPalette_1sizeof_FUNC);
	rc = (jint)ColorPalette_sizeof();
	Gdip_NATIVE_EXIT(env, that, ColorPalette_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_Color_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(Color_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(Color_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, Color_1delete_FUNC);
	delete (Color *)arg0;
	Gdip_NATIVE_EXIT(env, that, Color_1delete_FUNC);
}
#endif

#ifndef NO_Color_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Color_1new)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Color_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, Color_1new_FUNC);
	rc = (jintLong)new Color((ARGB)arg0);
	Gdip_NATIVE_EXIT(env, that, Color_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_FontFamily_1GetFamilyName
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(FontFamily_1GetFamilyName)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jchar arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(FontFamily_1GetFamilyName)
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jchar arg2)
{
	jchar *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, FontFamily_1GetFamilyName_FUNC);
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jint)((FontFamily *)arg0)->GetFamilyName((WCHAR *)lparg1, (WCHAR)arg2);
fail:
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, FontFamily_1GetFamilyName_FUNC);
	return rc;
}
#endif

#ifndef NO_FontFamily_1IsAvailable
extern "C" JNIEXPORT jboolean JNICALL Gdip_NATIVE(FontFamily_1IsAvailable)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jboolean JNICALL Gdip_NATIVE(FontFamily_1IsAvailable)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	Gdip_NATIVE_ENTER(env, that, FontFamily_1IsAvailable_FUNC);
	rc = (jboolean)((FontFamily *)arg0)->IsAvailable();
	Gdip_NATIVE_EXIT(env, that, FontFamily_1IsAvailable_FUNC);
	return rc;
}
#endif

#ifndef NO_FontFamily_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(FontFamily_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(FontFamily_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, FontFamily_1delete_FUNC);
	delete (FontFamily *)arg0;
	Gdip_NATIVE_EXIT(env, that, FontFamily_1delete_FUNC);
}
#endif

#ifndef NO_FontFamily_1new__
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(FontFamily_1new__)(JNIEnv *env, jclass that);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(FontFamily_1new__)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, FontFamily_1new___FUNC);
	rc = (jintLong)new FontFamily();
	Gdip_NATIVE_EXIT(env, that, FontFamily_1new___FUNC);
	return rc;
}
#endif

#if (!defined(NO_FontFamily_1new___3CI) && !defined(JNI64)) || (!defined(NO_FontFamily_1new___3CJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(FontFamily_1new___3CI)(JNIEnv *env, jclass that, jcharArray arg0, jintLong arg1);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(FontFamily_1new___3CI)(JNIEnv *env, jclass that, jcharArray arg0, jintLong arg1)
#else
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(FontFamily_1new___3CJ)(JNIEnv *env, jclass that, jcharArray arg0, jintLong arg1);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(FontFamily_1new___3CJ)(JNIEnv *env, jclass that, jcharArray arg0, jintLong arg1)
#endif
{
	jchar *lparg0=NULL;
	jintLong rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, FontFamily_1new___3CI_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, FontFamily_1new___3CJ_FUNC);
#endif
	if (arg0) if ((lparg0 = env->GetCharArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)new FontFamily((const WCHAR *)lparg0, (const FontCollection *)arg1);
fail:
	if (arg0 && lparg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, FontFamily_1new___3CI_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, FontFamily_1new___3CJ_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_Font_1GetFamily
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Font_1GetFamily)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Font_1GetFamily)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Font_1GetFamily_FUNC);
	rc = (jint)((Font *)arg0)->GetFamily((FontFamily *)arg1);
	Gdip_NATIVE_EXIT(env, that, Font_1GetFamily_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1GetLogFontW
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Font_1GetLogFontW)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Font_1GetLogFontW)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Font_1GetLogFontW_FUNC);
	rc = (jint)((Font *)arg0)->GetLogFontW((const Graphics *)arg1, (LOGFONTW *)arg2);
	Gdip_NATIVE_EXIT(env, that, Font_1GetLogFontW_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1GetSize
extern "C" JNIEXPORT jfloat JNICALL Gdip_NATIVE(Font_1GetSize)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jfloat JNICALL Gdip_NATIVE(Font_1GetSize)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jfloat rc = 0;
	Gdip_NATIVE_ENTER(env, that, Font_1GetSize_FUNC);
	rc = (jfloat)((Font *)arg0)->GetSize();
	Gdip_NATIVE_EXIT(env, that, Font_1GetSize_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1GetStyle
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Font_1GetStyle)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Font_1GetStyle)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Font_1GetStyle_FUNC);
	rc = (jint)((Font *)arg0)->GetStyle();
	Gdip_NATIVE_EXIT(env, that, Font_1GetStyle_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1IsAvailable
extern "C" JNIEXPORT jboolean JNICALL Gdip_NATIVE(Font_1IsAvailable)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jboolean JNICALL Gdip_NATIVE(Font_1IsAvailable)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	Gdip_NATIVE_ENTER(env, that, Font_1IsAvailable_FUNC);
	rc = (jboolean)((Font *)arg0)->IsAvailable();
	Gdip_NATIVE_EXIT(env, that, Font_1IsAvailable_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(Font_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(Font_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, Font_1delete_FUNC);
	delete (Font *)arg0;
	Gdip_NATIVE_EXIT(env, that, Font_1delete_FUNC);
}
#endif

#if (!defined(NO_Font_1new__IFII) && !defined(JNI64)) || (!defined(NO_Font_1new__JFII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Font_1new__IFII)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jint arg2, jint arg3);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Font_1new__IFII)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jint arg2, jint arg3)
#else
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Font_1new__JFII)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jint arg2, jint arg3);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Font_1new__JFII)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jint arg2, jint arg3)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Font_1new__IFII_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Font_1new__JFII_FUNC);
#endif
	rc = (jintLong)new Font((const FontFamily *)arg0, (REAL)arg1, (INT)arg2, (Unit)arg3);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Font_1new__IFII_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Font_1new__JFII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_Font_1new__II) && !defined(JNI64)) || (!defined(NO_Font_1new__JJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Font_1new__II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Font_1new__II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#else
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Font_1new__JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Font_1new__JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Font_1new__II_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Font_1new__JJ_FUNC);
#endif
	rc = (jintLong)new Font((HDC)arg0, (HFONT)arg1);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Font_1new__II_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Font_1new__JJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_Font_1new___3CFIII) && !defined(JNI64)) || (!defined(NO_Font_1new___3CFIIJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Font_1new___3CFIII)(JNIEnv *env, jclass that, jcharArray arg0, jfloat arg1, jint arg2, jint arg3, jintLong arg4);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Font_1new___3CFIII)(JNIEnv *env, jclass that, jcharArray arg0, jfloat arg1, jint arg2, jint arg3, jintLong arg4)
#else
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Font_1new___3CFIIJ)(JNIEnv *env, jclass that, jcharArray arg0, jfloat arg1, jint arg2, jint arg3, jintLong arg4);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Font_1new___3CFIIJ)(JNIEnv *env, jclass that, jcharArray arg0, jfloat arg1, jint arg2, jint arg3, jintLong arg4)
#endif
{
	jchar *lparg0=NULL;
	jintLong rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Font_1new___3CFIII_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Font_1new___3CFIIJ_FUNC);
#endif
	if (arg0) if ((lparg0 = env->GetCharArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)new Font((const WCHAR *)lparg0, (REAL)arg1, (INT)arg2, (Unit)arg3, (const FontCollection *)arg4);
fail:
	if (arg0 && lparg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Font_1new___3CFIII_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Font_1new___3CFIIJ_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_GdiplusShutdown
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(GdiplusShutdown)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(GdiplusShutdown)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, GdiplusShutdown_FUNC);
	GdiplusShutdown((ULONG_PTR)arg0);
	Gdip_NATIVE_EXIT(env, that, GdiplusShutdown_FUNC);
}
#endif

#ifndef NO_GdiplusStartup
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GdiplusStartup)(JNIEnv *env, jclass that, jintLongArray arg0, jobject arg1, jintLong arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(GdiplusStartup)
	(JNIEnv *env, jclass that, jintLongArray arg0, jobject arg1, jintLong arg2)
{
	jintLong *lparg0=NULL;
	GdiplusStartupInput _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GdiplusStartup_FUNC);
	if (arg0) if ((lparg0 = env->GetIntLongArrayElements(arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getGdiplusStartupInputFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)GdiplusStartup((ULONG_PTR *)lparg0, (const GdiplusStartupInput *)lparg1, (GdiplusStartupOutput *)arg2);
fail:
	if (arg1 && lparg1) setGdiplusStartupInputFields(env, arg1, lparg1);
	if (arg0 && lparg0) env->ReleaseIntLongArrayElements(arg0, lparg0, 0);
	Gdip_NATIVE_EXIT(env, that, GdiplusStartup_FUNC);
	return rc;
}
#endif

#ifndef NO_GdiplusStartupInput_1sizeof
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GdiplusStartupInput_1sizeof)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL Gdip_NATIVE(GdiplusStartupInput_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GdiplusStartupInput_1sizeof_FUNC);
	rc = (jint)GdiplusStartupInput_sizeof();
	Gdip_NATIVE_EXIT(env, that, GdiplusStartupInput_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1AddArc
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddArc)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloat arg6);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddArc)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloat arg6)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1AddArc_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->AddArc((REAL)arg1, (REAL)arg2, (REAL)arg3, (REAL)arg4, (REAL)arg5, (REAL)arg6);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1AddArc_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1AddBezier
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddBezier)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloat arg6, jfloat arg7, jfloat arg8);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddBezier)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloat arg6, jfloat arg7, jfloat arg8)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1AddBezier_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->AddBezier((REAL)arg1, (REAL)arg2, (REAL)arg3, (REAL)arg4, (REAL)arg5, (REAL)arg6, (REAL)arg7, (REAL)arg8);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1AddBezier_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1AddLine
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddLine)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddLine)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1AddLine_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->AddLine((REAL)arg1, (REAL)arg2, (REAL)arg3, (REAL)arg4);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1AddLine_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1AddPath
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddPath)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddPath)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1AddPath_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->AddPath((GraphicsPath *)arg1, (BOOL)arg2);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1AddPath_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1AddRectangle
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddRectangle)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddRectangle)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	RectF _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1AddRectangle_FUNC);
	if (arg1) if ((lparg1 = getRectFFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((GraphicsPath *)arg0)->AddRectangle(*lparg1);
fail:
	if (arg1 && lparg1) setRectFFields(env, arg1, lparg1);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1AddRectangle_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1AddString
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddString)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jint arg4, jfloat arg5, jobject arg6, jintLong arg7);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddString)
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jint arg4, jfloat arg5, jobject arg6, jintLong arg7)
{
	jchar *lparg1=NULL;
	PointF _arg6, *lparg6=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1AddString_FUNC);
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getPointFFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)((GraphicsPath *)arg0)->AddString((const WCHAR *)lparg1, (INT)arg2, (const FontFamily *)arg3, (INT)arg4, (REAL)arg5, *(const PointF *)lparg6, (const StringFormat *)arg7);
fail:
	if (arg6 && lparg6) setPointFFields(env, arg6, lparg6);
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1AddString_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1Clone
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(GraphicsPath_1Clone)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(GraphicsPath_1Clone)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1Clone_FUNC);
	rc = (jintLong)((GraphicsPath *)arg0)->Clone();
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1Clone_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1CloseFigure
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1CloseFigure)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1CloseFigure)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1CloseFigure_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->CloseFigure();
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1CloseFigure_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1Flatten
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1Flatten)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jfloat arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1Flatten)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jfloat arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1Flatten_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->Flatten((Matrix *)arg1, arg2);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1Flatten_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1GetBounds
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetBounds)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2, jintLong arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetBounds)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2, jintLong arg3)
{
	RectF _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1GetBounds_FUNC);
	if (arg1) if ((lparg1 = getRectFFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((GraphicsPath *)arg0)->GetBounds(lparg1, (Matrix *)arg2, (Pen *)arg3);
fail:
	if (arg1 && lparg1) setRectFFields(env, arg1, lparg1);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1GetBounds_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1GetLastPoint
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetLastPoint)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetLastPoint)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	PointF _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1GetLastPoint_FUNC);
	if (arg1) if ((lparg1 = getPointFFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((GraphicsPath *)arg0)->GetLastPoint((PointF *)lparg1);
fail:
	if (arg1 && lparg1) setPointFFields(env, arg1, lparg1);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1GetLastPoint_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1GetPathTypes
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetPathTypes)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetPathTypes)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1GetPathTypes_FUNC);
	if (arg1) if ((lparg1 = env->GetByteArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jint)((GraphicsPath *)arg0)->GetPathTypes((BYTE *)lparg1, arg2);
fail:
	if (arg1 && lparg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1GetPathTypes_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1GetPointCount
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetPointCount)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetPointCount)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1GetPointCount_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->GetPointCount();
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1GetPointCount_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1IsOutlineVisible
extern "C" JNIEXPORT jboolean JNICALL Gdip_NATIVE(GraphicsPath_1IsOutlineVisible)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jintLong arg3, jintLong arg4);
JNIEXPORT jboolean JNICALL Gdip_NATIVE(GraphicsPath_1IsOutlineVisible)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jintLong arg3, jintLong arg4)
{
	jboolean rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1IsOutlineVisible_FUNC);
	rc = (jboolean)((GraphicsPath *)arg0)->IsOutlineVisible(arg1, arg2, (const Pen *)arg3, (const Graphics *)arg4);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1IsOutlineVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1IsVisible
extern "C" JNIEXPORT jboolean JNICALL Gdip_NATIVE(GraphicsPath_1IsVisible)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jintLong arg3);
JNIEXPORT jboolean JNICALL Gdip_NATIVE(GraphicsPath_1IsVisible)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jintLong arg3)
{
	jboolean rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1IsVisible_FUNC);
	rc = (jboolean)((GraphicsPath *)arg0)->IsVisible(arg1, arg2, (const Graphics *)arg3);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1IsVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1SetFillMode
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1SetFillMode)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1SetFillMode)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1SetFillMode_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->SetFillMode((FillMode)arg1);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1SetFillMode_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1StartFigure
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1StartFigure)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1StartFigure)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1StartFigure_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->StartFigure();
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1StartFigure_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1Transform
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1Transform)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1Transform)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1Transform_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->Transform((Matrix *)arg1);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1Transform_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(GraphicsPath_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(GraphicsPath_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1delete_FUNC);
	delete (GraphicsPath *)arg0;
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1delete_FUNC);
}
#endif

#ifndef NO_GraphicsPath_1new__I
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(GraphicsPath_1new__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(GraphicsPath_1new__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1new__I_FUNC);
	rc = (jintLong)new GraphicsPath((FillMode)arg0);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1new__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawArc
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawArc)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jfloat arg6, jfloat arg7);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawArc)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jfloat arg6, jfloat arg7)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawArc_FUNC);
	rc = (jint)((Graphics *)arg0)->DrawArc((Pen *)arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawArc_FUNC);
	return rc;
}
#endif

#if (!defined(NO_Graphics_1DrawDriverString__IIIIILorg_eclipse_swt_internal_gdip_PointF_2II) && !defined(JNI64)) || (!defined(NO_Graphics_1DrawDriverString__JJIJJLorg_eclipse_swt_internal_gdip_PointF_2IJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawDriverString__IIIIILorg_eclipse_swt_internal_gdip_PointF_2II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4, jobject arg5, jint arg6, jintLong arg7);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawDriverString__IIIIILorg_eclipse_swt_internal_gdip_PointF_2II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4, jobject arg5, jint arg6, jintLong arg7)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawDriverString__JJIJJLorg_eclipse_swt_internal_gdip_PointF_2IJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4, jobject arg5, jint arg6, jintLong arg7);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawDriverString__JJIJJLorg_eclipse_swt_internal_gdip_PointF_2IJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4, jobject arg5, jint arg6, jintLong arg7)
#endif
{
	PointF _arg5, *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawDriverString__IIIIILorg_eclipse_swt_internal_gdip_PointF_2II_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawDriverString__JJIJJLorg_eclipse_swt_internal_gdip_PointF_2IJ_FUNC);
#endif
	if (arg5) if ((lparg5 = getPointFFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->DrawDriverString((const UINT16 *)arg1, arg2, (const Font *)arg3, (const Brush *)arg4, (const PointF *)lparg5, arg6, (const Matrix *)arg7);
fail:
	if (arg5 && lparg5) setPointFFields(env, arg5, lparg5);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawDriverString__IIIIILorg_eclipse_swt_internal_gdip_PointF_2II_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawDriverString__JJIJJLorg_eclipse_swt_internal_gdip_PointF_2IJ_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawEllipse
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawEllipse)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawEllipse)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawEllipse_FUNC);
	rc = (jint)((Graphics *)arg0)->DrawEllipse((Pen *)arg1, arg2, arg3, arg4, arg5);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawEllipse_FUNC);
	return rc;
}
#endif

#if (!defined(NO_Graphics_1DrawImage__IIII) && !defined(JNI64)) || (!defined(NO_Graphics_1DrawImage__JJII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawImage__IIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawImage__IIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawImage__JJII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawImage__JJII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
#endif
{
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawImage__IIII_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawImage__JJII_FUNC);
#endif
	rc = (jint)((Graphics *)arg0)->DrawImage((Image *)arg1, (INT)arg2, (INT)arg3);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawImage__IIII_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawImage__JJII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_Graphics_1DrawImage__IILorg_eclipse_swt_internal_gdip_Rect_2IIIIIIII) && !defined(JNI64)) || (!defined(NO_Graphics_1DrawImage__JJLorg_eclipse_swt_internal_gdip_Rect_2IIIIIJJJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawImage__IILorg_eclipse_swt_internal_gdip_Rect_2IIIIIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jintLong arg8, jintLong arg9, jintLong arg10);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawImage__IILorg_eclipse_swt_internal_gdip_Rect_2IIIIIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jintLong arg8, jintLong arg9, jintLong arg10)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawImage__JJLorg_eclipse_swt_internal_gdip_Rect_2IIIIIJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jintLong arg8, jintLong arg9, jintLong arg10);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawImage__JJLorg_eclipse_swt_internal_gdip_Rect_2IIIIIJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jintLong arg8, jintLong arg9, jintLong arg10)
#endif
{
	Rect _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawImage__IILorg_eclipse_swt_internal_gdip_Rect_2IIIIIIII_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawImage__JJLorg_eclipse_swt_internal_gdip_Rect_2IIIIIJJJ_FUNC);
#endif
	if (arg2) if ((lparg2 = getRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->DrawImage((Image *)arg1, *(const Rect *)lparg2, (INT)arg3, (INT)arg4, (INT)arg5, (INT)arg6, (Unit)arg7, (ImageAttributes *)arg8, (DrawImageAbort)arg9, (VOID *)arg10);
fail:
	if (arg2 && lparg2) setRectFields(env, arg2, lparg2);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawImage__IILorg_eclipse_swt_internal_gdip_Rect_2IIIIIIII_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawImage__JJLorg_eclipse_swt_internal_gdip_Rect_2IIIIIJJJ_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawLine
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawLine)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawLine)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawLine_FUNC);
	rc = (jint)((Graphics *)arg0)->DrawLine((Pen *)arg1, arg2, arg3, arg4, arg5);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawLine_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawPath
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawPath)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawPath)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawPath_FUNC);
	rc = (jint)((Graphics *)arg0)->DrawPath((Pen *)arg1, (GraphicsPath *)arg2);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawPath_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawRectangle
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawRectangle)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawRectangle)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawRectangle_FUNC);
	rc = (jint)((Graphics *)arg0)->DrawRectangle((Pen *)arg1, arg2, arg3, arg4, arg5);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawRectangle_FUNC);
	return rc;
}
#endif

#if (!defined(NO_Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2I) && !defined(JNI64)) || (!defined(NO_Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2I)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jintLong arg5);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2I)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jintLong arg5)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2J)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jintLong arg5);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2J)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jintLong arg5)
#endif
{
	jchar *lparg1=NULL;
	PointF _arg4, *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2I_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getPointFFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->DrawString((WCHAR *)lparg1, (int)arg2, (Font *)arg3, *lparg4, (Brush *)arg5);
fail:
	if (arg4 && lparg4) setPointFFields(env, arg4, lparg4);
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2I_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2II) && !defined(JNI64)) || (!defined(NO_Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2II)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jintLong arg5, jintLong arg6);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2II)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jintLong arg5, jintLong arg6)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JJ)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jintLong arg5, jintLong arg6);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JJ)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jintLong arg5, jintLong arg6)
#endif
{
	jchar *lparg1=NULL;
	PointF _arg4, *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2II_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JJ_FUNC);
#endif
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getPointFFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->DrawString((WCHAR *)lparg1, (int)arg2, (Font *)arg3, *lparg4, (StringFormat *)arg5, (Brush *)arg6);
fail:
	if (arg4 && lparg4) setPointFFields(env, arg4, lparg4);
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2II_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JJ_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_Graphics_1FillEllipse
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillEllipse)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillEllipse)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1FillEllipse_FUNC);
	rc = (jint)((Graphics *)arg0)->FillEllipse((Brush *)arg1, (INT)arg2, (INT)arg3, (INT)arg4, (INT)arg5);
	Gdip_NATIVE_EXIT(env, that, Graphics_1FillEllipse_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1FillPath
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillPath)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillPath)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1FillPath_FUNC);
	rc = (jint)((Graphics *)arg0)->FillPath((Brush *)arg1, (GraphicsPath *)arg2);
	Gdip_NATIVE_EXIT(env, that, Graphics_1FillPath_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1FillPie
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillPie)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jfloat arg6, jfloat arg7);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillPie)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jfloat arg6, jfloat arg7)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1FillPie_FUNC);
	rc = (jint)((Graphics *)arg0)->FillPie((Brush *)arg1, (INT)arg2, (INT)arg3, (INT)arg4, (INT)arg5, (REAL)arg6, (REAL)arg7);
	Gdip_NATIVE_EXIT(env, that, Graphics_1FillPie_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1FillRectangle
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillRectangle)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillRectangle)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1FillRectangle_FUNC);
	rc = (jint)((Graphics *)arg0)->FillRectangle((Brush *)arg1, (INT)arg2, (INT)arg3, (INT)arg4, (INT)arg5);
	Gdip_NATIVE_EXIT(env, that, Graphics_1FillRectangle_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1Flush
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(Graphics_1Flush)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT void JNICALL Gdip_NATIVE(Graphics_1Flush)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	Gdip_NATIVE_ENTER(env, that, Graphics_1Flush_FUNC);
	((Graphics *)arg0)->Flush((FlushIntention)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1Flush_FUNC);
}
#endif

#ifndef NO_Graphics_1GetClip
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClip)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClip)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetClip_FUNC);
	rc = (jint)((Graphics *)arg0)->GetClip((Region *)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetClip_FUNC);
	return rc;
}
#endif

#if (!defined(NO_Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_RectF_2) && !defined(JNI64)) || (!defined(NO_Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_RectF_2) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_RectF_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_RectF_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_RectF_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_RectF_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
	RectF _arg1, *lparg1=NULL;
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
#endif
	if (arg1) if ((lparg1 = getRectFFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->GetClipBounds(lparg1);
fail:
	if (arg1 && lparg1) setRectFFields(env, arg1, lparg1);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_Rect_2) && !defined(JNI64)) || (!defined(NO_Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_Rect_2) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_Rect_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_Rect_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_Rect_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_Rect_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
	Rect _arg1, *lparg1=NULL;
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_Rect_2_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_Rect_2_FUNC);
#endif
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->GetClipBounds(lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_Rect_2_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_Rect_2_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_Graphics_1GetHDC
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Graphics_1GetHDC)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Graphics_1GetHDC)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetHDC_FUNC);
	rc = (jintLong)((Graphics *)arg0)->GetHDC();
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetHDC_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1GetInterpolationMode
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetInterpolationMode)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetInterpolationMode)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetInterpolationMode_FUNC);
	rc = (jint)((Graphics *)arg0)->GetInterpolationMode();
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetInterpolationMode_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1GetSmoothingMode
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetSmoothingMode)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetSmoothingMode)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetSmoothingMode_FUNC);
	rc = (jint)((Graphics *)arg0)->GetSmoothingMode();
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetSmoothingMode_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1GetTextRenderingHint
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetTextRenderingHint)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetTextRenderingHint)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetTextRenderingHint_FUNC);
	rc = (jint)((Graphics *)arg0)->GetTextRenderingHint();
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetTextRenderingHint_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1GetTransform
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetTransform)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetTransform_FUNC);
	rc = (jint)((Graphics *)arg0)->GetTransform((Matrix *)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1GetVisibleClipBounds
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetVisibleClipBounds)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetVisibleClipBounds)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetVisibleClipBounds_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->GetVisibleClipBounds(lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetVisibleClipBounds_FUNC);
	return rc;
}
#endif

#if (!defined(NO_Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2ILorg_eclipse_swt_internal_gdip_RectF_2) && !defined(JNI64)) || (!defined(NO_Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JLorg_eclipse_swt_internal_gdip_RectF_2) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2ILorg_eclipse_swt_internal_gdip_RectF_2)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jintLong arg5, jobject arg6);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2ILorg_eclipse_swt_internal_gdip_RectF_2)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jintLong arg5, jobject arg6)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JLorg_eclipse_swt_internal_gdip_RectF_2)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jintLong arg5, jobject arg6);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JLorg_eclipse_swt_internal_gdip_RectF_2)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jintLong arg5, jobject arg6)
#endif
{
	jchar *lparg1=NULL;
	PointF _arg4, *lparg4=NULL;
	RectF _arg6, *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2ILorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JLorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
#endif
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getPointFFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getRectFFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->MeasureString((const WCHAR *)lparg1, (INT)arg2, (const Font *)arg3, *(const PointF *)lparg4, (StringFormat *)arg5, lparg6);
fail:
	if (arg6 && lparg6) setRectFFields(env, arg6, lparg6);
	if (arg4 && lparg4) setPointFFields(env, arg4, lparg4);
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2ILorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JLorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2) && !defined(JNI64)) || (!defined(NO_Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jobject arg5);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jobject arg5)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jobject arg5);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jintLong arg3, jobject arg4, jobject arg5)
#endif
{
	jchar *lparg1=NULL;
	PointF _arg4, *lparg4=NULL;
	RectF _arg5, *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
#endif
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getPointFFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getRectFFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->MeasureString((const WCHAR *)lparg1, (INT)arg2, (const Font *)arg3, *(const PointF *)lparg4, (RectF *)lparg5);
fail:
	if (arg5 && lparg5) setRectFFields(env, arg5, lparg5);
	if (arg4 && lparg4) setPointFFields(env, arg4, lparg4);
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_Graphics_1ReleaseHDC
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(Graphics_1ReleaseHDC)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT void JNICALL Gdip_NATIVE(Graphics_1ReleaseHDC)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	Gdip_NATIVE_ENTER(env, that, Graphics_1ReleaseHDC_FUNC);
	((Graphics *)arg0)->ReleaseHDC((HDC)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1ReleaseHDC_FUNC);
}
#endif

#ifndef NO_Graphics_1ResetClip
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1ResetClip)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1ResetClip)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1ResetClip_FUNC);
	rc = (jint)((Graphics *)arg0)->ResetClip();
	Gdip_NATIVE_EXIT(env, that, Graphics_1ResetClip_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1Restore
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1Restore)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1Restore)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1Restore_FUNC);
	rc = (jint)((Graphics *)arg0)->Restore((GraphicsState)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1Restore_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1Save
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1Save)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1Save)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1Save_FUNC);
	rc = (jint)((Graphics *)arg0)->Save();
	Gdip_NATIVE_EXIT(env, that, Graphics_1Save_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1ScaleTransform
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1ScaleTransform)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1ScaleTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1ScaleTransform_FUNC);
	rc = (jint)((Graphics *)arg0)->ScaleTransform(arg1, arg2, (MatrixOrder)arg3);
	Gdip_NATIVE_EXIT(env, that, Graphics_1ScaleTransform_FUNC);
	return rc;
}
#endif

#if (!defined(NO_Graphics_1SetClip__III) && !defined(JNI64)) || (!defined(NO_Graphics_1SetClip__JJI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClip__III)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClip__III)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClip__JJI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClip__JJI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
#endif
{
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetClip__III_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetClip__JJI_FUNC);
#endif
	rc = (jint)((Graphics *)arg0)->SetClip((Region *)arg1, (CombineMode)arg2);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetClip__III_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetClip__JJI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_Graphics_1SetClip__ILorg_eclipse_swt_internal_gdip_Rect_2I) && !defined(JNI64)) || (!defined(NO_Graphics_1SetClip__JLorg_eclipse_swt_internal_gdip_Rect_2I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClip__ILorg_eclipse_swt_internal_gdip_Rect_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClip__ILorg_eclipse_swt_internal_gdip_Rect_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClip__JLorg_eclipse_swt_internal_gdip_Rect_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClip__JLorg_eclipse_swt_internal_gdip_Rect_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	Rect _arg1, *lparg1=NULL;
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetClip__ILorg_eclipse_swt_internal_gdip_Rect_2I_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetClip__JLorg_eclipse_swt_internal_gdip_Rect_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->SetClip(*(Rect *)lparg1, (CombineMode)arg2);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetClip__ILorg_eclipse_swt_internal_gdip_Rect_2I_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetClip__JLorg_eclipse_swt_internal_gdip_Rect_2I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_Graphics_1SetClipPath__II) && !defined(JNI64)) || (!defined(NO_Graphics_1SetClipPath__JJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClipPath__II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClipPath__II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClipPath__JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClipPath__JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#endif
{
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetClipPath__II_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetClipPath__JJ_FUNC);
#endif
	rc = (jint)((Graphics *)arg0)->SetClip((GraphicsPath *)arg1);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetClipPath__II_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetClipPath__JJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_Graphics_1SetClipPath__III) && !defined(JNI64)) || (!defined(NO_Graphics_1SetClipPath__JJI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClipPath__III)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClipPath__III)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClipPath__JJI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClipPath__JJI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
#endif
{
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetClipPath__III_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetClipPath__JJI_FUNC);
#endif
	rc = (jint)((Graphics *)arg0)->SetClip((GraphicsPath *)arg1, (CombineMode)arg2);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetClipPath__III_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetClipPath__JJI_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_Graphics_1SetCompositingQuality
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetCompositingQuality)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetCompositingQuality)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetCompositingQuality_FUNC);
	rc = (jint)((Graphics *)arg0)->SetCompositingQuality((CompositingQuality)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetCompositingQuality_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetInterpolationMode
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetInterpolationMode)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetInterpolationMode)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetInterpolationMode_FUNC);
	rc = (jint)((Graphics *)arg0)->SetInterpolationMode((InterpolationMode)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetInterpolationMode_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetPageUnit
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetPageUnit)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetPageUnit)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetPageUnit_FUNC);
	rc = (jint)((Graphics *)arg0)->SetPageUnit((Unit)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetPageUnit_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetPixelOffsetMode
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetPixelOffsetMode)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetPixelOffsetMode)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetPixelOffsetMode_FUNC);
	rc = (jint)((Graphics *)arg0)->SetPixelOffsetMode((PixelOffsetMode)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetPixelOffsetMode_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetSmoothingMode
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetSmoothingMode)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetSmoothingMode)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetSmoothingMode_FUNC);
	rc = (jint)((Graphics *)arg0)->SetSmoothingMode((SmoothingMode)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetSmoothingMode_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetTextRenderingHint
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetTextRenderingHint)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetTextRenderingHint)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetTextRenderingHint_FUNC);
	rc = (jint)((Graphics *)arg0)->SetTextRenderingHint((TextRenderingHint)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetTextRenderingHint_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetTransform
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetTransform)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetTransform_FUNC);
	rc = (jint)((Graphics *)arg0)->SetTransform((Matrix *)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1TranslateTransform
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1TranslateTransform)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1TranslateTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1TranslateTransform_FUNC);
	rc = (jint)((Graphics *)arg0)->TranslateTransform(arg1, arg2, (MatrixOrder)arg3);
	Gdip_NATIVE_EXIT(env, that, Graphics_1TranslateTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(Graphics_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(Graphics_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, Graphics_1delete_FUNC);
	delete (Graphics *)arg0;
	Gdip_NATIVE_EXIT(env, that, Graphics_1delete_FUNC);
}
#endif

#ifndef NO_Graphics_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Graphics_1new)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Graphics_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1new_FUNC);
	rc = (jintLong)new Graphics((HDC)arg0);
	Gdip_NATIVE_EXIT(env, that, Graphics_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_HatchBrush_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(HatchBrush_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(HatchBrush_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, HatchBrush_1delete_FUNC);
	delete (HatchBrush *)arg0;
	Gdip_NATIVE_EXIT(env, that, HatchBrush_1delete_FUNC);
}
#endif

#ifndef NO_HatchBrush_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(HatchBrush_1new)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(HatchBrush_1new)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, HatchBrush_1new_FUNC);
	rc = (jintLong)new HatchBrush((HatchStyle)arg0, *(Color *)arg1, *(Color *)arg2);
	Gdip_NATIVE_EXIT(env, that, HatchBrush_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageAttributes_1SetColorMatrix
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(ImageAttributes_1SetColorMatrix)(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(ImageAttributes_1SetColorMatrix)
	(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1, jint arg2, jint arg3)
{
	jfloat *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, ImageAttributes_1SetColorMatrix_FUNC);
	if (arg1) if ((lparg1 = env->GetFloatArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jint)((ImageAttributes *)arg0)->SetColorMatrix((ColorMatrix *)lparg1, (ColorMatrixFlags)arg2, (ColorAdjustType)arg3);
fail:
	if (arg1 && lparg1) env->ReleaseFloatArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, ImageAttributes_1SetColorMatrix_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageAttributes_1SetWrapMode
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(ImageAttributes_1SetWrapMode)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(ImageAttributes_1SetWrapMode)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, ImageAttributes_1SetWrapMode_FUNC);
	rc = (jint)((ImageAttributes *)arg0)->SetWrapMode((WrapMode)arg1);
	Gdip_NATIVE_EXIT(env, that, ImageAttributes_1SetWrapMode_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageAttributes_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(ImageAttributes_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(ImageAttributes_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, ImageAttributes_1delete_FUNC);
	delete (ImageAttributes *)arg0;
	Gdip_NATIVE_EXIT(env, that, ImageAttributes_1delete_FUNC);
}
#endif

#ifndef NO_ImageAttributes_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(ImageAttributes_1new)(JNIEnv *env, jclass that);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(ImageAttributes_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, ImageAttributes_1new_FUNC);
	rc = (jintLong)new ImageAttributes();
	Gdip_NATIVE_EXIT(env, that, ImageAttributes_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Image_1GetHeight
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetHeight)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetHeight)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Image_1GetHeight_FUNC);
	rc = (jint)((Image *)arg0)->GetHeight();
	Gdip_NATIVE_EXIT(env, that, Image_1GetHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_Image_1GetLastStatus
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetLastStatus)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetLastStatus)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Image_1GetLastStatus_FUNC);
	rc = (jint)((Image*)arg0)->GetLastStatus();
	Gdip_NATIVE_EXIT(env, that, Image_1GetLastStatus_FUNC);
	return rc;
}
#endif

#ifndef NO_Image_1GetPalette
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetPalette)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetPalette)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Image_1GetPalette_FUNC);
	rc = (jint)((Image*)arg0)->GetPalette((ColorPalette*)arg1, arg2);
	Gdip_NATIVE_EXIT(env, that, Image_1GetPalette_FUNC);
	return rc;
}
#endif

#ifndef NO_Image_1GetPaletteSize
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetPaletteSize)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetPaletteSize)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Image_1GetPaletteSize_FUNC);
	rc = (jint)((Image*)arg0)->GetPaletteSize();
	Gdip_NATIVE_EXIT(env, that, Image_1GetPaletteSize_FUNC);
	return rc;
}
#endif

#ifndef NO_Image_1GetPixelFormat
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetPixelFormat)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetPixelFormat)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Image_1GetPixelFormat_FUNC);
	rc = (jint)((Image*)arg0)->GetPixelFormat();
	Gdip_NATIVE_EXIT(env, that, Image_1GetPixelFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_Image_1GetWidth
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetWidth)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetWidth)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Image_1GetWidth_FUNC);
	rc = (jint)((Image *)arg0)->GetWidth();
	Gdip_NATIVE_EXIT(env, that, Image_1GetWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_LinearGradientBrush_1ResetTransform
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(LinearGradientBrush_1ResetTransform)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(LinearGradientBrush_1ResetTransform)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, LinearGradientBrush_1ResetTransform_FUNC);
	rc = (jint)((LinearGradientBrush *)arg0)->ResetTransform();
	Gdip_NATIVE_EXIT(env, that, LinearGradientBrush_1ResetTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_LinearGradientBrush_1ScaleTransform
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(LinearGradientBrush_1ScaleTransform)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(LinearGradientBrush_1ScaleTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, LinearGradientBrush_1ScaleTransform_FUNC);
	rc = (jint)((LinearGradientBrush *)arg0)->ScaleTransform(arg1, arg2, (MatrixOrder)arg3);
	Gdip_NATIVE_EXIT(env, that, LinearGradientBrush_1ScaleTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_LinearGradientBrush_1SetWrapMode
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(LinearGradientBrush_1SetWrapMode)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(LinearGradientBrush_1SetWrapMode)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, LinearGradientBrush_1SetWrapMode_FUNC);
	rc = (jint)((LinearGradientBrush *)arg0)->SetWrapMode((WrapMode)arg1);
	Gdip_NATIVE_EXIT(env, that, LinearGradientBrush_1SetWrapMode_FUNC);
	return rc;
}
#endif

#ifndef NO_LinearGradientBrush_1TranslateTransform
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(LinearGradientBrush_1TranslateTransform)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(LinearGradientBrush_1TranslateTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, LinearGradientBrush_1TranslateTransform_FUNC);
	rc = (jint)((LinearGradientBrush *)arg0)->TranslateTransform(arg1, arg2, (MatrixOrder)arg3);
	Gdip_NATIVE_EXIT(env, that, LinearGradientBrush_1TranslateTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_LinearGradientBrush_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(LinearGradientBrush_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(LinearGradientBrush_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, LinearGradientBrush_1delete_FUNC);
	delete (LinearGradientBrush *)arg0;
	Gdip_NATIVE_EXIT(env, that, LinearGradientBrush_1delete_FUNC);
}
#endif

#ifndef NO_LinearGradientBrush_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(LinearGradientBrush_1new)(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jintLong arg2, jintLong arg3);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(LinearGradientBrush_1new)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jintLong arg2, jintLong arg3)
{
	PointF _arg0, *lparg0=NULL;
	PointF _arg1, *lparg1=NULL;
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, LinearGradientBrush_1new_FUNC);
	if (arg0) if ((lparg0 = getPointFFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPointFFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jintLong)new LinearGradientBrush(*lparg0, *lparg1, *(Color *)arg2, *(Color *)arg3);
fail:
	if (arg1 && lparg1) setPointFFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPointFFields(env, arg0, lparg0);
	Gdip_NATIVE_EXIT(env, that, LinearGradientBrush_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1GetElements
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1GetElements)(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1GetElements)
	(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1GetElements_FUNC);
	if (arg1) if ((lparg1 = env->GetFloatArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jint)((Matrix *)arg0)->GetElements((REAL *)lparg1);
fail:
	if (arg1 && lparg1) env->ReleaseFloatArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, Matrix_1GetElements_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1Invert
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Invert)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Invert)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1Invert_FUNC);
	rc = (jint)((Matrix *)arg0)->Invert();
	Gdip_NATIVE_EXIT(env, that, Matrix_1Invert_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1IsIdentity
extern "C" JNIEXPORT jboolean JNICALL Gdip_NATIVE(Matrix_1IsIdentity)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jboolean JNICALL Gdip_NATIVE(Matrix_1IsIdentity)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1IsIdentity_FUNC);
	rc = (jboolean)((Matrix *)arg0)->IsIdentity();
	Gdip_NATIVE_EXIT(env, that, Matrix_1IsIdentity_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1Multiply
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Multiply)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Multiply)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1Multiply_FUNC);
	rc = (jint)((Matrix *)arg0)->Multiply((Matrix *)arg1, (MatrixOrder)arg2);
	Gdip_NATIVE_EXIT(env, that, Matrix_1Multiply_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1Rotate
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Rotate)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Rotate)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jint arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1Rotate_FUNC);
	rc = (jint)((Matrix *)arg0)->Rotate((REAL)arg1, (MatrixOrder)arg2);
	Gdip_NATIVE_EXIT(env, that, Matrix_1Rotate_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1Scale
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Scale)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Scale)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1Scale_FUNC);
	rc = (jint)((Matrix *)arg0)->Scale((REAL)arg1, (REAL)arg2, (MatrixOrder)arg3);
	Gdip_NATIVE_EXIT(env, that, Matrix_1Scale_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1SetElements
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1SetElements)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloat arg6);
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1SetElements)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloat arg6)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1SetElements_FUNC);
	rc = (jint)((Matrix *)arg0)->SetElements((REAL)arg1, (REAL)arg2, (REAL)arg3, (REAL)arg4, (REAL)arg5, (REAL)arg6);
	Gdip_NATIVE_EXIT(env, that, Matrix_1SetElements_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1Shear
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Shear)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Shear)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1Shear_FUNC);
	rc = (jint)((Matrix *)arg0)->Shear((REAL)arg1, (REAL)arg2, (MatrixOrder)arg3);
	Gdip_NATIVE_EXIT(env, that, Matrix_1Shear_FUNC);
	return rc;
}
#endif

#if (!defined(NO_Matrix_1TransformPoints__ILorg_eclipse_swt_internal_gdip_PointF_2I) && !defined(JNI64)) || (!defined(NO_Matrix_1TransformPoints__JLorg_eclipse_swt_internal_gdip_PointF_2I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1TransformPoints__ILorg_eclipse_swt_internal_gdip_PointF_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1TransformPoints__ILorg_eclipse_swt_internal_gdip_PointF_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1TransformPoints__JLorg_eclipse_swt_internal_gdip_PointF_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1TransformPoints__JLorg_eclipse_swt_internal_gdip_PointF_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	PointF _arg1, *lparg1=NULL;
	jint rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Matrix_1TransformPoints__ILorg_eclipse_swt_internal_gdip_PointF_2I_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Matrix_1TransformPoints__JLorg_eclipse_swt_internal_gdip_PointF_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getPointFFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((Matrix *)arg0)->TransformPoints(lparg1, arg2);
fail:
	if (arg1 && lparg1) setPointFFields(env, arg1, lparg1);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Matrix_1TransformPoints__ILorg_eclipse_swt_internal_gdip_PointF_2I_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Matrix_1TransformPoints__JLorg_eclipse_swt_internal_gdip_PointF_2I_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_Matrix_1TransformVectors
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1TransformVectors)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1TransformVectors)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
{
	PointF _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1TransformVectors_FUNC);
	if (arg1) if ((lparg1 = getPointFFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((Matrix *)arg0)->TransformVectors(lparg1, arg2);
fail:
	if (arg1 && lparg1) setPointFFields(env, arg1, lparg1);
	Gdip_NATIVE_EXIT(env, that, Matrix_1TransformVectors_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1Translate
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Translate)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Translate)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1Translate_FUNC);
	rc = (jint)((Matrix *)arg0)->Translate((REAL)arg1, (REAL)arg2, (MatrixOrder)arg3);
	Gdip_NATIVE_EXIT(env, that, Matrix_1Translate_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(Matrix_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(Matrix_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, Matrix_1delete_FUNC);
	delete (Matrix *)arg0;
	Gdip_NATIVE_EXIT(env, that, Matrix_1delete_FUNC);
}
#endif

#ifndef NO_Matrix_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Matrix_1new)(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Matrix_1new)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1new_FUNC);
	rc = (jintLong)new Matrix((REAL)arg0, (REAL)arg1, (REAL)arg2, (REAL)arg3, (REAL)arg4, (REAL)arg5);
	Gdip_NATIVE_EXIT(env, that, Matrix_1new_FUNC);
	return rc;
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1);
JNIEXPORT void JNICALL Gdip_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1);
JNIEXPORT void JNICALL Gdip_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2I_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2J_FUNC);
#endif
	if (arg0) setBitmapDataFields(env, arg0, (BitmapData *)arg1);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2I_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2JI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2);
JNIEXPORT void JNICALL Gdip_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2);
JNIEXPORT void JNICALL Gdip_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	ColorPalette _arg0, *lparg0=NULL;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2II_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = getColorPaletteFields(env, arg0, &_arg0)) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID*)arg1, arg2);
fail:
	if (arg0 && lparg0) setColorPaletteFields(env, arg0, lparg0);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2II_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2JI_FUNC);
#endif
}
#endif

#ifndef NO_PathGradientBrush_1SetCenterColor
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetCenterColor)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetCenterColor)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1SetCenterColor_FUNC);
	rc = (jint)((PathGradientBrush *)arg0)->SetCenterColor(*(Color *)arg1);
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1SetCenterColor_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGradientBrush_1SetCenterPoint
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetCenterPoint)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetCenterPoint)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	PointF _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1SetCenterPoint_FUNC);
	if (arg1) if ((lparg1 = getPointFFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((PathGradientBrush *)arg0)->SetCenterPoint(*lparg1);
fail:
	if (arg1 && lparg1) setPointFFields(env, arg1, lparg1);
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1SetCenterPoint_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGradientBrush_1SetGraphicsPath
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetGraphicsPath)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetGraphicsPath)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1SetGraphicsPath_FUNC);
	rc = (jint)((PathGradientBrush *)arg0)->SetGraphicsPath((GraphicsPath *)arg1);
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1SetGraphicsPath_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGradientBrush_1SetWrapMode
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetWrapMode)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetWrapMode)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1SetWrapMode_FUNC);
	rc = (jint)((PathGradientBrush *)arg0)->SetWrapMode((WrapMode)arg1);
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1SetWrapMode_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGradientBrush_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(PathGradientBrush_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(PathGradientBrush_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1delete_FUNC);
	delete (PathGradientBrush *)arg0;
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1delete_FUNC);
}
#endif

#ifndef NO_PathGradientBrush_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(PathGradientBrush_1new)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(PathGradientBrush_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1new_FUNC);
	rc = (jintLong)new PathGradientBrush((GraphicsPath *)arg0);
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1GetBrush
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Pen_1GetBrush)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Pen_1GetBrush)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1GetBrush_FUNC);
	rc = (jintLong)((Pen *)arg0)->GetBrush();
	Gdip_NATIVE_EXIT(env, that, Pen_1GetBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1SetBrush
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetBrush)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetBrush)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1SetBrush_FUNC);
	rc = (jint)((Pen *)arg0)->SetBrush((Brush *)arg1);
	Gdip_NATIVE_EXIT(env, that, Pen_1SetBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1SetDashOffset
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetDashOffset)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetDashOffset)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1SetDashOffset_FUNC);
	rc = (jint)((Pen *)arg0)->SetDashOffset(arg1);
	Gdip_NATIVE_EXIT(env, that, Pen_1SetDashOffset_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1SetDashPattern
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetDashPattern)(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1, jint arg2);
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetDashPattern)
	(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1, jint arg2)
{
	jfloat *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1SetDashPattern_FUNC);
	if (arg1) if ((lparg1 = env->GetFloatArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jint)((Pen *)arg0)->SetDashPattern((REAL *)lparg1, (int)arg2);
fail:
	if (arg1 && lparg1) env->ReleaseFloatArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, Pen_1SetDashPattern_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1SetDashStyle
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetDashStyle)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetDashStyle)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1SetDashStyle_FUNC);
	rc = (jint)((Pen *)arg0)->SetDashStyle((DashStyle)arg1);
	Gdip_NATIVE_EXIT(env, that, Pen_1SetDashStyle_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1SetLineCap
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetLineCap)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetLineCap)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1SetLineCap_FUNC);
	rc = (jint)((Pen *)arg0)->SetLineCap((LineCap)arg1, (LineCap)arg2, (DashCap)arg3);
	Gdip_NATIVE_EXIT(env, that, Pen_1SetLineCap_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1SetLineJoin
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetLineJoin)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetLineJoin)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1SetLineJoin_FUNC);
	rc = (jint)((Pen *)arg0)->SetLineJoin((LineJoin)arg1);
	Gdip_NATIVE_EXIT(env, that, Pen_1SetLineJoin_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1SetMiterLimit
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetMiterLimit)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetMiterLimit)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1SetMiterLimit_FUNC);
	rc = (jint)((Pen *)arg0)->SetMiterLimit(arg1);
	Gdip_NATIVE_EXIT(env, that, Pen_1SetMiterLimit_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1SetWidth
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetWidth)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetWidth)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1SetWidth_FUNC);
	rc = (jint)((Pen *)arg0)->SetWidth((REAL)arg1);
	Gdip_NATIVE_EXIT(env, that, Pen_1SetWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(Pen_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(Pen_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, Pen_1delete_FUNC);
	delete (Pen *)arg0;
	Gdip_NATIVE_EXIT(env, that, Pen_1delete_FUNC);
}
#endif

#ifndef NO_Pen_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Pen_1new)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Pen_1new)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1new_FUNC);
	rc = (jintLong)new Pen((Brush *)arg0, (REAL)arg1);
	Gdip_NATIVE_EXIT(env, that, Pen_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Point_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(Point_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(Point_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, Point_1delete_FUNC);
	delete (Point *)arg0;
	Gdip_NATIVE_EXIT(env, that, Point_1delete_FUNC);
}
#endif

#ifndef NO_Point_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Point_1new)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Point_1new)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, Point_1new_FUNC);
	rc = (jintLong)new Point(arg0, arg1);
	Gdip_NATIVE_EXIT(env, that, Point_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_PrivateFontCollection_1AddFontFile
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(PrivateFontCollection_1AddFontFile)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(PrivateFontCollection_1AddFontFile)
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1)
{
	jchar *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, PrivateFontCollection_1AddFontFile_FUNC);
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jint)((PrivateFontCollection *)arg0)->AddFontFile((const WCHAR *)lparg1);
fail:
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, PrivateFontCollection_1AddFontFile_FUNC);
	return rc;
}
#endif

#ifndef NO_PrivateFontCollection_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(PrivateFontCollection_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(PrivateFontCollection_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, PrivateFontCollection_1delete_FUNC);
	delete (PrivateFontCollection *)arg0;
	Gdip_NATIVE_EXIT(env, that, PrivateFontCollection_1delete_FUNC);
}
#endif

#ifndef NO_PrivateFontCollection_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(PrivateFontCollection_1new)(JNIEnv *env, jclass that);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(PrivateFontCollection_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, PrivateFontCollection_1new_FUNC);
	rc = (jintLong)new PrivateFontCollection();
	Gdip_NATIVE_EXIT(env, that, PrivateFontCollection_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Region_1GetHRGN
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Region_1GetHRGN)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Region_1GetHRGN)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, Region_1GetHRGN_FUNC);
	rc = (jintLong)((Region *)arg0)->GetHRGN((Graphics *)arg1);
	Gdip_NATIVE_EXIT(env, that, Region_1GetHRGN_FUNC);
	return rc;
}
#endif

#ifndef NO_Region_1IsInfinite
extern "C" JNIEXPORT jboolean JNICALL Gdip_NATIVE(Region_1IsInfinite)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jboolean JNICALL Gdip_NATIVE(Region_1IsInfinite)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	Gdip_NATIVE_ENTER(env, that, Region_1IsInfinite_FUNC);
	rc = (jboolean)((Region *)arg0)->IsInfinite((Graphics *)arg1);
	Gdip_NATIVE_EXIT(env, that, Region_1IsInfinite_FUNC);
	return rc;
}
#endif

#ifndef NO_Region_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(Region_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(Region_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, Region_1delete_FUNC);
	delete (Region *)arg0;
	Gdip_NATIVE_EXIT(env, that, Region_1delete_FUNC);
}
#endif

#ifndef NO_Region_1new__
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Region_1new__)(JNIEnv *env, jclass that);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Region_1new__)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, Region_1new___FUNC);
	rc = (jintLong)new Region();
	Gdip_NATIVE_EXIT(env, that, Region_1new___FUNC);
	return rc;
}
#endif

#if (!defined(NO_Region_1new__I) && !defined(JNI64)) || (!defined(NO_Region_1new__J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Region_1new__I)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Region_1new__I)(JNIEnv *env, jclass that, jintLong arg0)
#else
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Region_1new__J)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Region_1new__J)(JNIEnv *env, jclass that, jintLong arg0)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	Gdip_NATIVE_ENTER(env, that, Region_1new__I_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Region_1new__J_FUNC);
#endif
	rc = (jintLong)new Region((HRGN)arg0);
#ifndef JNI64
	Gdip_NATIVE_EXIT(env, that, Region_1new__I_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Region_1new__J_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_Region_1newGraphicsPath
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(Region_1newGraphicsPath)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(Region_1newGraphicsPath)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, Region_1newGraphicsPath_FUNC);
	rc = (jintLong)new Region((GraphicsPath*)arg0);
	Gdip_NATIVE_EXIT(env, that, Region_1newGraphicsPath_FUNC);
	return rc;
}
#endif

#ifndef NO_SolidBrush_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(SolidBrush_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(SolidBrush_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, SolidBrush_1delete_FUNC);
	delete (SolidBrush *)arg0;
	Gdip_NATIVE_EXIT(env, that, SolidBrush_1delete_FUNC);
}
#endif

#ifndef NO_SolidBrush_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(SolidBrush_1new)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(SolidBrush_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, SolidBrush_1new_FUNC);
	rc = (jintLong)new SolidBrush(*(Color *)arg0);
	Gdip_NATIVE_EXIT(env, that, SolidBrush_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1Clone
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(StringFormat_1Clone)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(StringFormat_1Clone)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1Clone_FUNC);
	rc = (jintLong)((StringFormat *)arg0)->Clone();
	Gdip_NATIVE_EXIT(env, that, StringFormat_1Clone_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1GenericDefault
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(StringFormat_1GenericDefault)(JNIEnv *env, jclass that);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(StringFormat_1GenericDefault)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1GenericDefault_FUNC);
	rc = (jintLong)StringFormat::GenericDefault();
	Gdip_NATIVE_EXIT(env, that, StringFormat_1GenericDefault_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1GenericTypographic
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(StringFormat_1GenericTypographic)(JNIEnv *env, jclass that);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(StringFormat_1GenericTypographic)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1GenericTypographic_FUNC);
	rc = (jintLong)StringFormat::GenericTypographic();
	Gdip_NATIVE_EXIT(env, that, StringFormat_1GenericTypographic_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1GetFormatFlags
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1GetFormatFlags)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1GetFormatFlags)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1GetFormatFlags_FUNC);
	rc = (jint)((StringFormat *)arg0)->GetFormatFlags();
	Gdip_NATIVE_EXIT(env, that, StringFormat_1GetFormatFlags_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1SetFormatFlags
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1SetFormatFlags)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1SetFormatFlags)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1SetFormatFlags_FUNC);
	rc = (jint)((StringFormat *)arg0)->SetFormatFlags((StringFormatFlags)arg1);
	Gdip_NATIVE_EXIT(env, that, StringFormat_1SetFormatFlags_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1SetHotkeyPrefix
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1SetHotkeyPrefix)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1SetHotkeyPrefix)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1SetHotkeyPrefix_FUNC);
	rc = (jint)((StringFormat *)arg0)->SetHotkeyPrefix((HotkeyPrefix)arg1);
	Gdip_NATIVE_EXIT(env, that, StringFormat_1SetHotkeyPrefix_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1SetTabStops
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1SetTabStops)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jint arg2, jfloatArray arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1SetTabStops)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jint arg2, jfloatArray arg3)
{
	jfloat *lparg3=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1SetTabStops_FUNC);
	if (arg3) if ((lparg3 = env->GetFloatArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((StringFormat *)arg0)->SetTabStops(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseFloatArrayElements(arg3, lparg3, 0);
	Gdip_NATIVE_EXIT(env, that, StringFormat_1SetTabStops_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(StringFormat_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(StringFormat_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, StringFormat_1delete_FUNC);
	delete (StringFormat *)arg0;
	Gdip_NATIVE_EXIT(env, that, StringFormat_1delete_FUNC);
}
#endif

#ifndef NO_TextureBrush_1ResetTransform
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(TextureBrush_1ResetTransform)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL Gdip_NATIVE(TextureBrush_1ResetTransform)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, TextureBrush_1ResetTransform_FUNC);
	rc = (jint)((TextureBrush *)arg0)->ResetTransform();
	Gdip_NATIVE_EXIT(env, that, TextureBrush_1ResetTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_TextureBrush_1ScaleTransform
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(TextureBrush_1ScaleTransform)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(TextureBrush_1ScaleTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, TextureBrush_1ScaleTransform_FUNC);
	rc = (jint)((TextureBrush *)arg0)->ScaleTransform(arg1, arg2, (MatrixOrder)arg3);
	Gdip_NATIVE_EXIT(env, that, TextureBrush_1ScaleTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_TextureBrush_1SetTransform
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(TextureBrush_1SetTransform)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL Gdip_NATIVE(TextureBrush_1SetTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, TextureBrush_1SetTransform_FUNC);
	rc = (jint)((TextureBrush *)arg0)->SetTransform((Matrix *)arg1);
	Gdip_NATIVE_EXIT(env, that, TextureBrush_1SetTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_TextureBrush_1TranslateTransform
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(TextureBrush_1TranslateTransform)(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3);
JNIEXPORT jint JNICALL Gdip_NATIVE(TextureBrush_1TranslateTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, TextureBrush_1TranslateTransform_FUNC);
	rc = (jint)((TextureBrush *)arg0)->TranslateTransform(arg1, arg2, (MatrixOrder)arg3);
	Gdip_NATIVE_EXIT(env, that, TextureBrush_1TranslateTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_TextureBrush_1delete
extern "C" JNIEXPORT void JNICALL Gdip_NATIVE(TextureBrush_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL Gdip_NATIVE(TextureBrush_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	Gdip_NATIVE_ENTER(env, that, TextureBrush_1delete_FUNC);
	delete (TextureBrush *)arg0;
	Gdip_NATIVE_EXIT(env, that, TextureBrush_1delete_FUNC);
}
#endif

#ifndef NO_TextureBrush_1new
extern "C" JNIEXPORT jintLong JNICALL Gdip_NATIVE(TextureBrush_1new)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5);
JNIEXPORT jintLong JNICALL Gdip_NATIVE(TextureBrush_1new)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5)
{
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, TextureBrush_1new_FUNC);
	rc = (jintLong)new TextureBrush((Image *)arg0, (WrapMode)arg1, arg2, arg3, arg4, arg5);
	Gdip_NATIVE_EXIT(env, that, TextureBrush_1new_FUNC);
	return rc;
}
#endif


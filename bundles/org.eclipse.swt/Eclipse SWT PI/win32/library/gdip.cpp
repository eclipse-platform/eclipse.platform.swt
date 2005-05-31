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
#include "gdip_stats.h"

extern "C" {

#define Gdip_NATIVE(func) Java_org_eclipse_swt_internal_gdip_Gdip_##func

#ifndef NO_Bitmap_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(Bitmap_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, Bitmap_1delete_FUNC);
	delete (Bitmap *)arg0;
	Gdip_NATIVE_EXIT(env, that, Bitmap_1delete_FUNC);
}
#endif

#ifndef NO_Bitmap_1new__I
JNIEXPORT jint JNICALL Gdip_NATIVE(Bitmap_1new__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Bitmap_1new__I_FUNC);
	rc = (jint)new Bitmap((HICON)arg0);
	Gdip_NATIVE_EXIT(env, that, Bitmap_1new__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Bitmap_1new__II
JNIEXPORT jint JNICALL Gdip_NATIVE(Bitmap_1new__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Bitmap_1new__II_FUNC);
	rc = (jint)new Bitmap((HBITMAP)arg0, (HPALETTE)arg1);
	Gdip_NATIVE_EXIT(env, that, Bitmap_1new__II_FUNC);
	return rc;
}
#endif

#ifndef NO_Bitmap_1new__IIIII
JNIEXPORT jint JNICALL Gdip_NATIVE(Bitmap_1new__IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Bitmap_1new__IIIII_FUNC);
	rc = (jint)new Bitmap(arg0, arg1, arg2, (PixelFormat)arg3, (BYTE *)arg4);
	Gdip_NATIVE_EXIT(env, that, Bitmap_1new__IIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_Brush_1Clone
JNIEXPORT jint JNICALL Gdip_NATIVE(Brush_1Clone)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Brush_1Clone_FUNC);
	rc = (jint)((Brush *)arg0)->Clone();
	Gdip_NATIVE_EXIT(env, that, Brush_1Clone_FUNC);
	return rc;
}
#endif

#ifndef NO_Brush_1GetType
JNIEXPORT jint JNICALL Gdip_NATIVE(Brush_1GetType)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Brush_1GetType_FUNC);
	rc = (jint)((Brush *)arg0)->GetType();
	Gdip_NATIVE_EXIT(env, that, Brush_1GetType_FUNC);
	return rc;
}
#endif

#ifndef NO_Color_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(Color_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, Color_1delete_FUNC);
	delete (Color *)arg0;
	Gdip_NATIVE_EXIT(env, that, Color_1delete_FUNC);
}
#endif

#ifndef NO_Color_1new
JNIEXPORT jint JNICALL Gdip_NATIVE(Color_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Color_1new_FUNC);
	rc = (jint)new Color((ARGB)arg0);
	Gdip_NATIVE_EXIT(env, that, Color_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_FontFamily_1GetFamilyName
JNIEXPORT jint JNICALL Gdip_NATIVE(FontFamily_1GetFamilyName)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jchar arg2)
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

#ifndef NO_FontFamily_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(FontFamily_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, FontFamily_1delete_FUNC);
	delete (FontFamily *)arg0;
	Gdip_NATIVE_EXIT(env, that, FontFamily_1delete_FUNC);
}
#endif

#ifndef NO_FontFamily_1new
JNIEXPORT jint JNICALL Gdip_NATIVE(FontFamily_1new)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, FontFamily_1new_FUNC);
	rc = (jint)new FontFamily();
	Gdip_NATIVE_EXIT(env, that, FontFamily_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1GetFamily
JNIEXPORT jint JNICALL Gdip_NATIVE(Font_1GetFamily)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Font_1GetFamily_FUNC);
	rc = (jint)((Font *)arg0)->GetFamily((FontFamily *)arg1);
	Gdip_NATIVE_EXIT(env, that, Font_1GetFamily_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1GetSize
JNIEXPORT jfloat JNICALL Gdip_NATIVE(Font_1GetSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jfloat rc = 0;
	Gdip_NATIVE_ENTER(env, that, Font_1GetSize_FUNC);
	rc = (jfloat)((Font *)arg0)->GetSize();
	Gdip_NATIVE_EXIT(env, that, Font_1GetSize_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1GetStyle
JNIEXPORT jint JNICALL Gdip_NATIVE(Font_1GetStyle)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Font_1GetStyle_FUNC);
	rc = (jint)((Font *)arg0)->GetStyle();
	Gdip_NATIVE_EXIT(env, that, Font_1GetStyle_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1IsAvailable
JNIEXPORT jboolean JNICALL Gdip_NATIVE(Font_1IsAvailable)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	Gdip_NATIVE_ENTER(env, that, Font_1IsAvailable_FUNC);
	rc = (jboolean)((Font *)arg0)->IsAvailable();
	Gdip_NATIVE_EXIT(env, that, Font_1IsAvailable_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(Font_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, Font_1delete_FUNC);
	delete (Font *)arg0;
	Gdip_NATIVE_EXIT(env, that, Font_1delete_FUNC);
}
#endif

#ifndef NO_Font_1new__II
JNIEXPORT jint JNICALL Gdip_NATIVE(Font_1new__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Font_1new__II_FUNC);
	rc = (jint)new Font((HDC)arg0, (HFONT)arg1);
	Gdip_NATIVE_EXIT(env, that, Font_1new__II_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1new___3CFIII
JNIEXPORT jint JNICALL Gdip_NATIVE(Font_1new___3CFIII)
	(JNIEnv *env, jclass that, jcharArray arg0, jfloat arg1, jint arg2, jint arg3, jint arg4)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Font_1new___3CFIII_FUNC);
	if (arg0) if ((lparg0 = env->GetCharArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)new Font((const WCHAR *)lparg0, (REAL)arg1, (INT)arg2, (Unit)arg3, (const FontCollection *)arg4);
fail:
	if (arg0 && lparg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
	Gdip_NATIVE_EXIT(env, that, Font_1new___3CFIII_FUNC);
	return rc;
}
#endif

#ifndef NO_GdiplusShutdown
JNIEXPORT void JNICALL Gdip_NATIVE(GdiplusShutdown)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	Gdip_NATIVE_ENTER(env, that, GdiplusShutdown_FUNC);
	if (arg0) if ((lparg0 = env->GetIntArrayElements(arg0, NULL)) == NULL) goto fail;
	GdiplusShutdown((ULONG_PTR)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseIntArrayElements(arg0, lparg0, 0);
	Gdip_NATIVE_EXIT(env, that, GdiplusShutdown_FUNC);
}
#endif

#ifndef NO_GdiplusStartup
JNIEXPORT jint JNICALL Gdip_NATIVE(GdiplusStartup)
	(JNIEnv *env, jclass that, jintArray arg0, jobject arg1, jint arg2)
{
	jint *lparg0=NULL;
	GdiplusStartupInput _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GdiplusStartup_FUNC);
	if (arg0) if ((lparg0 = env->GetIntArrayElements(arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getGdiplusStartupInputFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)GdiplusStartup((ULONG_PTR *)lparg0, (const GdiplusStartupInput *)lparg1, (GdiplusStartupOutput *)arg2);
fail:
	if (arg1 && lparg1) setGdiplusStartupInputFields(env, arg1, lparg1);
	if (arg0 && lparg0) env->ReleaseIntArrayElements(arg0, lparg0, 0);
	Gdip_NATIVE_EXIT(env, that, GdiplusStartup_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1AddArc
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddArc)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloat arg6)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1AddArc_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->AddArc((REAL)arg1, (REAL)arg2, (REAL)arg3, (REAL)arg4, (REAL)arg5, (REAL)arg6);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1AddArc_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1AddBezier
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddBezier)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloat arg6, jfloat arg7, jfloat arg8)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1AddBezier_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->AddBezier((REAL)arg1, (REAL)arg2, (REAL)arg3, (REAL)arg4, (REAL)arg5, (REAL)arg6, (REAL)arg7, (REAL)arg8);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1AddBezier_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1AddLine
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddLine)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1AddLine_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->AddLine((REAL)arg1, (REAL)arg2, (REAL)arg3, (REAL)arg4);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1AddLine_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1AddPath
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddPath)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1AddPath_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->AddPath((GraphicsPath *)arg1, (BOOL)arg2);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1AddPath_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1AddRectangle
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddRectangle)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
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
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1AddString)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jint arg4, jfloat arg5, jobject arg6, jint arg7)
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

#ifndef NO_GraphicsPath_1CloseFigure
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1CloseFigure)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1CloseFigure_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->CloseFigure();
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1CloseFigure_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1GetBounds
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
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
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetLastPoint)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
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
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetPathTypes)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
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
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetPointCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1GetPointCount_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->GetPointCount();
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1GetPointCount_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1IsOutlineVisible
JNIEXPORT jboolean JNICALL Gdip_NATIVE(GraphicsPath_1IsOutlineVisible)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jint arg3, jint arg4)
{
	jboolean rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1IsOutlineVisible_FUNC);
	rc = (jboolean)((GraphicsPath *)arg0)->IsOutlineVisible(arg1, arg2, (const Pen *)arg3, (const Graphics *)arg4);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1IsOutlineVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1IsVisible
JNIEXPORT jboolean JNICALL Gdip_NATIVE(GraphicsPath_1IsVisible)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jboolean rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1IsVisible_FUNC);
	rc = (jboolean)((GraphicsPath *)arg0)->IsVisible(arg1, arg2, (const Graphics *)arg3);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1IsVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1SetFillMode
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1SetFillMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1SetFillMode_FUNC);
	rc = (jint)((GraphicsPath *)arg0)->SetFillMode((FillMode)arg1);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1SetFillMode_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(GraphicsPath_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1delete_FUNC);
	delete (GraphicsPath *)arg0;
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1delete_FUNC);
}
#endif

#ifndef NO_GraphicsPath_1new
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1new_FUNC);
	rc = (jint)new GraphicsPath((FillMode)arg0);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawArc
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawArc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jfloat arg6, jfloat arg7)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawArc_FUNC);
	rc = (jint)((Graphics *)arg0)->DrawArc((Pen *)arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawArc_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawEllipse
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawEllipse)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawEllipse_FUNC);
	rc = (jint)((Graphics *)arg0)->DrawEllipse((Pen *)arg1, arg2, arg3, arg4, arg5);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawEllipse_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawImage__IIII
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawImage__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawImage__IIII_FUNC);
	rc = (jint)((Graphics *)arg0)->DrawImage((Image *)arg1, (INT)arg2, (INT)arg3);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawImage__IIII_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawImage__IILorg_eclipse_swt_internal_gdip_Rect_2IIIIIIII
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawImage__IILorg_eclipse_swt_internal_gdip_Rect_2IIIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	Rect _arg2, *lparg2=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawImage__IILorg_eclipse_swt_internal_gdip_Rect_2IIIIIIII_FUNC);
	if (arg2) if ((lparg2 = getRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->DrawImage((Image *)arg1, *(const Rect *)lparg2, (INT)arg3, (INT)arg4, (INT)arg5, (INT)arg6, (Unit)arg7, (ImageAttributes *)arg8, (DrawImageAbort)arg9, (VOID *)arg10);
fail:
	if (arg2 && lparg2) setRectFields(env, arg2, lparg2);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawImage__IILorg_eclipse_swt_internal_gdip_Rect_2IIIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawLine
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawLine)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawLine_FUNC);
	rc = (jint)((Graphics *)arg0)->DrawLine((Pen *)arg1, arg2, arg3, arg4, arg5);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawLine_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawPath
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawPath)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawPath_FUNC);
	rc = (jint)((Graphics *)arg0)->DrawPath((Pen *)arg1, (GraphicsPath *)arg2);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawPath_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawRectangle
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawRectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawRectangle_FUNC);
	rc = (jint)((Graphics *)arg0)->DrawRectangle((Pen *)arg1, arg2, arg3, arg4, arg5);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawRectangle_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2I
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2I)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	jchar *lparg1=NULL;
	PointF _arg4, *lparg4=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2I_FUNC);
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getPointFFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->DrawString((WCHAR *)lparg1, (int)arg2, (Font *)arg3, *lparg4, (Brush *)arg5);
fail:
	if (arg4 && lparg4) setPointFFields(env, arg4, lparg4);
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2II
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2II)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jint arg6)
{
	jchar *lparg1=NULL;
	PointF _arg4, *lparg4=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2II_FUNC);
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getPointFFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->DrawString((WCHAR *)lparg1, (int)arg2, (Font *)arg3, *lparg4, (StringFormat *)arg5, (Brush *)arg6);
fail:
	if (arg4 && lparg4) setPointFFields(env, arg4, lparg4);
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1FillEllipse
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillEllipse)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1FillEllipse_FUNC);
	rc = (jint)((Graphics *)arg0)->FillEllipse((Brush *)arg1, (INT)arg2, (INT)arg3, (INT)arg4, (INT)arg5);
	Gdip_NATIVE_EXIT(env, that, Graphics_1FillEllipse_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1FillPath
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillPath)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1FillPath_FUNC);
	rc = (jint)((Graphics *)arg0)->FillPath((Brush *)arg1, (GraphicsPath *)arg2);
	Gdip_NATIVE_EXIT(env, that, Graphics_1FillPath_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1FillPie
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillPie)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jfloat arg6, jfloat arg7)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1FillPie_FUNC);
	rc = (jint)((Graphics *)arg0)->FillPie((Brush *)arg1, (INT)arg2, (INT)arg3, (INT)arg4, (INT)arg5, (REAL)arg6, (REAL)arg7);
	Gdip_NATIVE_EXIT(env, that, Graphics_1FillPie_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1FillRectangle
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillRectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1FillRectangle_FUNC);
	rc = (jint)((Graphics *)arg0)->FillRectangle((Brush *)arg1, (INT)arg2, (INT)arg3, (INT)arg4, (INT)arg5);
	Gdip_NATIVE_EXIT(env, that, Graphics_1FillRectangle_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1Flush
JNIEXPORT void JNICALL Gdip_NATIVE(Graphics_1Flush)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Gdip_NATIVE_ENTER(env, that, Graphics_1Flush_FUNC);
	((Graphics *)arg0)->Flush((FlushIntention)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1Flush_FUNC);
}
#endif

#ifndef NO_Graphics_1GetClip
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClip)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetClip_FUNC);
	rc = (jint)((Graphics *)arg0)->GetClip((Region *)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetClip_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_RectF_2
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_RectF_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RectF _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
	if (arg1) if ((lparg1 = getRectFFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->GetClipBounds(lparg1);
fail:
	if (arg1 && lparg1) setRectFFields(env, arg1, lparg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_Rect_2
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_Rect_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_Rect_2_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->GetClipBounds(lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_Rect_2_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1GetHDC
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetHDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetHDC_FUNC);
	rc = (jint)((Graphics *)arg0)->GetHDC();
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetHDC_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1GetInterpolationMode
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetInterpolationMode)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetInterpolationMode_FUNC);
	rc = (jint)((Graphics *)arg0)->GetInterpolationMode();
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetInterpolationMode_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1GetSmoothingMode
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetSmoothingMode)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetSmoothingMode_FUNC);
	rc = (jint)((Graphics *)arg0)->GetSmoothingMode();
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetSmoothingMode_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1GetTextRenderingHint
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetTextRenderingHint)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetTextRenderingHint_FUNC);
	rc = (jint)((Graphics *)arg0)->GetTextRenderingHint();
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetTextRenderingHint_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1GetTransform
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1GetTransform)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1GetTransform_FUNC);
	rc = (jint)((Graphics *)arg0)->GetTransform((Matrix *)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1GetTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2ILorg_eclipse_swt_internal_gdip_RectF_2
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2ILorg_eclipse_swt_internal_gdip_RectF_2)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jobject arg6)
{
	jchar *lparg1=NULL;
	PointF _arg4, *lparg4=NULL;
	RectF _arg6, *lparg6=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2ILorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getPointFFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getRectFFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->MeasureString((const WCHAR *)lparg1, (INT)arg2, (const Font *)arg3, *(const PointF *)lparg4, (StringFormat *)arg5, lparg6);
fail:
	if (arg6 && lparg6) setRectFFields(env, arg6, lparg6);
	if (arg4 && lparg4) setPointFFields(env, arg4, lparg4);
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2ILorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jobject arg4, jobject arg5)
{
	jchar *lparg1=NULL;
	PointF _arg4, *lparg4=NULL;
	RectF _arg5, *lparg5=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getPointFFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getRectFFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->MeasureString((const WCHAR *)lparg1, (INT)arg2, (const Font *)arg3, *(const PointF *)lparg4, (RectF *)lparg5);
fail:
	if (arg5 && lparg5) setRectFFields(env, arg5, lparg5);
	if (arg4 && lparg4) setPointFFields(env, arg4, lparg4);
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1ReleaseHDC
JNIEXPORT void JNICALL Gdip_NATIVE(Graphics_1ReleaseHDC)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Gdip_NATIVE_ENTER(env, that, Graphics_1ReleaseHDC_FUNC);
	((Graphics *)arg0)->ReleaseHDC((HDC)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1ReleaseHDC_FUNC);
}
#endif

#ifndef NO_Graphics_1ResetClip
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1ResetClip)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1ResetClip_FUNC);
	rc = (jint)((Graphics *)arg0)->ResetClip();
	Gdip_NATIVE_EXIT(env, that, Graphics_1ResetClip_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetClip__II
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClip__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetClip__II_FUNC);
	rc = (jint)((Graphics *)arg0)->SetClip((GraphicsPath *)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetClip__II_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetClip__III
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClip__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetClip__III_FUNC);
	rc = (jint)((Graphics *)arg0)->SetClip((Region *)arg1, (CombineMode)arg2);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetClip__III_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetClip__ILorg_eclipse_swt_internal_gdip_RectF_2
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetClip__ILorg_eclipse_swt_internal_gdip_RectF_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RectF _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetClip__ILorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
	if (arg1) if ((lparg1 = getRectFFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->SetClip(*lparg1);
fail:
	if (arg1 && lparg1) setRectFFields(env, arg1, lparg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetClip__ILorg_eclipse_swt_internal_gdip_RectF_2_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetCompositingQuality
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetCompositingQuality)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetCompositingQuality_FUNC);
	rc = (jint)((Graphics *)arg0)->SetCompositingQuality((CompositingQuality)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetCompositingQuality_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetInterpolationMode
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetInterpolationMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetInterpolationMode_FUNC);
	rc = (jint)((Graphics *)arg0)->SetInterpolationMode((InterpolationMode)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetInterpolationMode_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetPixelOffsetMode
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetPixelOffsetMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetPixelOffsetMode_FUNC);
	rc = (jint)((Graphics *)arg0)->SetPixelOffsetMode((PixelOffsetMode)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetPixelOffsetMode_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetSmoothingMode
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetSmoothingMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetSmoothingMode_FUNC);
	rc = (jint)((Graphics *)arg0)->SetSmoothingMode((SmoothingMode)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetSmoothingMode_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetTextRenderingHint
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetTextRenderingHint)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetTextRenderingHint_FUNC);
	rc = (jint)((Graphics *)arg0)->SetTextRenderingHint((TextRenderingHint)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetTextRenderingHint_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1SetTransform
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1SetTransform)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1SetTransform_FUNC);
	rc = (jint)((Graphics *)arg0)->SetTransform((Matrix *)arg1);
	Gdip_NATIVE_EXIT(env, that, Graphics_1SetTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(Graphics_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, Graphics_1delete_FUNC);
	delete (Graphics *)arg0;
	Gdip_NATIVE_EXIT(env, that, Graphics_1delete_FUNC);
}
#endif

#ifndef NO_Graphics_1new
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1new_FUNC);
	rc = (jint)new Graphics((HDC)arg0);
	Gdip_NATIVE_EXIT(env, that, Graphics_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_HatchBrush_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(HatchBrush_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, HatchBrush_1delete_FUNC);
	delete (HatchBrush *)arg0;
	Gdip_NATIVE_EXIT(env, that, HatchBrush_1delete_FUNC);
}
#endif

#ifndef NO_HatchBrush_1new
JNIEXPORT jint JNICALL Gdip_NATIVE(HatchBrush_1new)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, HatchBrush_1new_FUNC);
	rc = (jint)new HatchBrush((HatchStyle)arg0, *(Color *)arg1, *(Color *)arg2);
	Gdip_NATIVE_EXIT(env, that, HatchBrush_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Image_1GetHeight
JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetHeight)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Image_1GetHeight_FUNC);
	rc = (jint)((Image *)arg0)->GetHeight();
	Gdip_NATIVE_EXIT(env, that, Image_1GetHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_Image_1GetWidth
JNIEXPORT jint JNICALL Gdip_NATIVE(Image_1GetWidth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Image_1GetWidth_FUNC);
	rc = (jint)((Image *)arg0)->GetWidth();
	Gdip_NATIVE_EXIT(env, that, Image_1GetWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_LinearGradientBrush_1SetWrapMode
JNIEXPORT jint JNICALL Gdip_NATIVE(LinearGradientBrush_1SetWrapMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, LinearGradientBrush_1SetWrapMode_FUNC);
	rc = (jint)((LinearGradientBrush *)arg0)->SetWrapMode((WrapMode)arg1);
	Gdip_NATIVE_EXIT(env, that, LinearGradientBrush_1SetWrapMode_FUNC);
	return rc;
}
#endif

#ifndef NO_LinearGradientBrush_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(LinearGradientBrush_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, LinearGradientBrush_1delete_FUNC);
	delete (LinearGradientBrush *)arg0;
	Gdip_NATIVE_EXIT(env, that, LinearGradientBrush_1delete_FUNC);
}
#endif

#ifndef NO_LinearGradientBrush_1new
JNIEXPORT jint JNICALL Gdip_NATIVE(LinearGradientBrush_1new)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3)
{
	PointF _arg0, *lparg0=NULL;
	PointF _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, LinearGradientBrush_1new_FUNC);
	if (arg0) if ((lparg0 = getPointFFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPointFFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)new LinearGradientBrush(*lparg0, *lparg1, *(Color *)arg2, *(Color *)arg3);
fail:
	if (arg1 && lparg1) setPointFFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPointFFields(env, arg0, lparg0);
	Gdip_NATIVE_EXIT(env, that, LinearGradientBrush_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1GetElements
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1GetElements)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
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
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Invert)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1Invert_FUNC);
	rc = (jint)((Matrix *)arg0)->Invert();
	Gdip_NATIVE_EXIT(env, that, Matrix_1Invert_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1IsIdentity
JNIEXPORT jboolean JNICALL Gdip_NATIVE(Matrix_1IsIdentity)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1IsIdentity_FUNC);
	rc = (jboolean)((Matrix *)arg0)->IsIdentity();
	Gdip_NATIVE_EXIT(env, that, Matrix_1IsIdentity_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1Multiply
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Multiply)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1Multiply_FUNC);
	rc = (jint)((Matrix *)arg0)->Multiply((Matrix *)arg1, (MatrixOrder)arg2);
	Gdip_NATIVE_EXIT(env, that, Matrix_1Multiply_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1Rotate
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Rotate)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jint arg2)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1Rotate_FUNC);
	rc = (jint)((Matrix *)arg0)->Rotate((REAL)arg1, (MatrixOrder)arg2);
	Gdip_NATIVE_EXIT(env, that, Matrix_1Rotate_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1Scale
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Scale)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1Scale_FUNC);
	rc = (jint)((Matrix *)arg0)->Scale((REAL)arg1, (REAL)arg2, (MatrixOrder)arg3);
	Gdip_NATIVE_EXIT(env, that, Matrix_1Scale_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1SetElements
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1SetElements)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloat arg6)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1SetElements_FUNC);
	rc = (jint)((Matrix *)arg0)->SetElements((REAL)arg1, (REAL)arg2, (REAL)arg3, (REAL)arg4, (REAL)arg5, (REAL)arg6);
	Gdip_NATIVE_EXIT(env, that, Matrix_1SetElements_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1Shear
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Shear)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1Shear_FUNC);
	rc = (jint)((Matrix *)arg0)->Shear((REAL)arg1, (REAL)arg2, (MatrixOrder)arg3);
	Gdip_NATIVE_EXIT(env, that, Matrix_1Shear_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1TransformPoints__ILorg_eclipse_swt_internal_gdip_PointF_2I
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1TransformPoints__ILorg_eclipse_swt_internal_gdip_PointF_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PointF _arg1, *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1TransformPoints__ILorg_eclipse_swt_internal_gdip_PointF_2I_FUNC);
	if (arg1) if ((lparg1 = getPointFFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((Matrix *)arg0)->TransformPoints(lparg1, arg2);
fail:
	if (arg1 && lparg1) setPointFFields(env, arg1, lparg1);
	Gdip_NATIVE_EXIT(env, that, Matrix_1TransformPoints__ILorg_eclipse_swt_internal_gdip_PointF_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1Translate
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1Translate)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1Translate_FUNC);
	rc = (jint)((Matrix *)arg0)->Translate((REAL)arg1, (REAL)arg2, (MatrixOrder)arg3);
	Gdip_NATIVE_EXIT(env, that, Matrix_1Translate_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(Matrix_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, Matrix_1delete_FUNC);
	delete (Matrix *)arg0;
	Gdip_NATIVE_EXIT(env, that, Matrix_1delete_FUNC);
}
#endif

#ifndef NO_Matrix_1new
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1new)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1new_FUNC);
	rc = (jint)new Matrix((REAL)arg0, (REAL)arg1, (REAL)arg2, (REAL)arg3, (REAL)arg4, (REAL)arg5);
	Gdip_NATIVE_EXIT(env, that, Matrix_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGradientBrush_1SetCenterColor
JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetCenterColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1SetCenterColor_FUNC);
	rc = (jint)((PathGradientBrush *)arg0)->SetCenterColor(*(Color *)arg1);
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1SetCenterColor_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGradientBrush_1SetCenterPoint
JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetCenterPoint)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
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
JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetGraphicsPath)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1SetGraphicsPath_FUNC);
	rc = (jint)((PathGradientBrush *)arg0)->SetGraphicsPath((GraphicsPath *)arg1);
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1SetGraphicsPath_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGradientBrush_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(PathGradientBrush_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1delete_FUNC);
	delete (PathGradientBrush *)arg0;
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1delete_FUNC);
}
#endif

#ifndef NO_PathGradientBrush_1new
JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1new_FUNC);
	rc = (jint)new PathGradientBrush((GraphicsPath *)arg0);
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1GetBrush
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1GetBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1GetBrush_FUNC);
	rc = (jint)((Pen *)arg0)->GetBrush();
	Gdip_NATIVE_EXIT(env, that, Pen_1GetBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1SetBrush
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetBrush)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1SetBrush_FUNC);
	rc = (jint)((Pen *)arg0)->SetBrush((Brush *)arg1);
	Gdip_NATIVE_EXIT(env, that, Pen_1SetBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1SetDashPattern
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetDashPattern)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jint arg2)
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
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetDashStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1SetDashStyle_FUNC);
	rc = (jint)((Pen *)arg0)->SetDashStyle((DashStyle)arg1);
	Gdip_NATIVE_EXIT(env, that, Pen_1SetDashStyle_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1SetLineCap
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetLineCap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1SetLineCap_FUNC);
	rc = (jint)((Pen *)arg0)->SetLineCap((LineCap)arg1, (LineCap)arg2, (DashCap)arg3);
	Gdip_NATIVE_EXIT(env, that, Pen_1SetLineCap_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1SetLineJoin
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1SetLineJoin)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1SetLineJoin_FUNC);
	rc = (jint)((Pen *)arg0)->SetLineJoin((LineJoin)arg1);
	Gdip_NATIVE_EXIT(env, that, Pen_1SetLineJoin_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(Pen_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, Pen_1delete_FUNC);
	delete (Pen *)arg0;
	Gdip_NATIVE_EXIT(env, that, Pen_1delete_FUNC);
}
#endif

#ifndef NO_Pen_1new
JNIEXPORT jint JNICALL Gdip_NATIVE(Pen_1new)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Pen_1new_FUNC);
	rc = (jint)new Pen(*(Color *)arg0, (REAL)arg1);
	Gdip_NATIVE_EXIT(env, that, Pen_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Point_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(Point_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, Point_1delete_FUNC);
	delete (Point *)arg0;
	Gdip_NATIVE_EXIT(env, that, Point_1delete_FUNC);
}
#endif

#ifndef NO_Point_1new
JNIEXPORT jint JNICALL Gdip_NATIVE(Point_1new)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Point_1new_FUNC);
	rc = (jint)new Point(arg0, arg1);
	Gdip_NATIVE_EXIT(env, that, Point_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_Region_1GetHRGN
JNIEXPORT jint JNICALL Gdip_NATIVE(Region_1GetHRGN)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Region_1GetHRGN_FUNC);
	rc = (jint)((Region *)arg0)->GetHRGN((Graphics *)arg1);
	Gdip_NATIVE_EXIT(env, that, Region_1GetHRGN_FUNC);
	return rc;
}
#endif

#ifndef NO_Region_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(Region_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, Region_1delete_FUNC);
	delete (Region *)arg0;
	Gdip_NATIVE_EXIT(env, that, Region_1delete_FUNC);
}
#endif

#ifndef NO_Region_1new__
JNIEXPORT jint JNICALL Gdip_NATIVE(Region_1new__)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Region_1new___FUNC);
	rc = (jint)new Region();
	Gdip_NATIVE_EXIT(env, that, Region_1new___FUNC);
	return rc;
}
#endif

#ifndef NO_Region_1new__I
JNIEXPORT jint JNICALL Gdip_NATIVE(Region_1new__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Region_1new__I_FUNC);
	rc = (jint)new Region((HRGN)arg0);
	Gdip_NATIVE_EXIT(env, that, Region_1new__I_FUNC);
	return rc;
}
#endif

#ifndef NO_SolidBrush_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(SolidBrush_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, SolidBrush_1delete_FUNC);
	delete (SolidBrush *)arg0;
	Gdip_NATIVE_EXIT(env, that, SolidBrush_1delete_FUNC);
}
#endif

#ifndef NO_SolidBrush_1new
JNIEXPORT jint JNICALL Gdip_NATIVE(SolidBrush_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, SolidBrush_1new_FUNC);
	rc = (jint)new SolidBrush(*(Color *)arg0);
	Gdip_NATIVE_EXIT(env, that, SolidBrush_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1Clone
JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1Clone)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1Clone_FUNC);
	rc = (jint)((StringFormat *)arg0)->Clone();
	Gdip_NATIVE_EXIT(env, that, StringFormat_1Clone_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1GenericDefault
JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1GenericDefault)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1GenericDefault_FUNC);
	rc = (jint)StringFormat::GenericDefault();
	Gdip_NATIVE_EXIT(env, that, StringFormat_1GenericDefault_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1GenericTypographic
JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1GenericTypographic)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1GenericTypographic_FUNC);
	rc = (jint)StringFormat::GenericTypographic();
	Gdip_NATIVE_EXIT(env, that, StringFormat_1GenericTypographic_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1GetFormatFlags
JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1GetFormatFlags)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1GetFormatFlags_FUNC);
	rc = (jint)((StringFormat *)arg0)->GetFormatFlags();
	Gdip_NATIVE_EXIT(env, that, StringFormat_1GetFormatFlags_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1SetFormatFlags
JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1SetFormatFlags)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1SetFormatFlags_FUNC);
	rc = (jint)((StringFormat *)arg0)->SetFormatFlags((StringFormatFlags)arg1);
	Gdip_NATIVE_EXIT(env, that, StringFormat_1SetFormatFlags_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1SetHotkeyPrefix
JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1SetHotkeyPrefix)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, StringFormat_1SetHotkeyPrefix_FUNC);
	rc = (jint)((StringFormat *)arg0)->SetHotkeyPrefix((HotkeyPrefix)arg1);
	Gdip_NATIVE_EXIT(env, that, StringFormat_1SetHotkeyPrefix_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFormat_1SetTabStops
JNIEXPORT jint JNICALL Gdip_NATIVE(StringFormat_1SetTabStops)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jint arg2, jfloatArray arg3)
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
JNIEXPORT void JNICALL Gdip_NATIVE(StringFormat_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, StringFormat_1delete_FUNC);
	delete (StringFormat *)arg0;
	Gdip_NATIVE_EXIT(env, that, StringFormat_1delete_FUNC);
}
#endif

#ifndef NO_TextureBrush_1SetTransform
JNIEXPORT void JNICALL Gdip_NATIVE(TextureBrush_1SetTransform)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	Gdip_NATIVE_ENTER(env, that, TextureBrush_1SetTransform_FUNC);
	((TextureBrush *)arg0)->SetTransform((Matrix *)arg1);
	Gdip_NATIVE_EXIT(env, that, TextureBrush_1SetTransform_FUNC);
}
#endif

#ifndef NO_TextureBrush_1delete
JNIEXPORT void JNICALL Gdip_NATIVE(TextureBrush_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	Gdip_NATIVE_ENTER(env, that, TextureBrush_1delete_FUNC);
	delete (TextureBrush *)arg0;
	Gdip_NATIVE_EXIT(env, that, TextureBrush_1delete_FUNC);
}
#endif

#ifndef NO_TextureBrush_1new
JNIEXPORT jint JNICALL Gdip_NATIVE(TextureBrush_1new)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5)
{
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, TextureBrush_1new_FUNC);
	rc = (jint)new TextureBrush((Image *)arg0, (WrapMode)arg1, arg2, arg3, arg4, arg5);
	Gdip_NATIVE_EXIT(env, that, TextureBrush_1new_FUNC);
	return rc;
}
#endif

}

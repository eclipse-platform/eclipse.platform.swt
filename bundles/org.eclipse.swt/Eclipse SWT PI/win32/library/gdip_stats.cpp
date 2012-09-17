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
#include "gdip_stats.h"

#ifdef NATIVE_STATS

char * Gdip_nativeFunctionNames[] = {
	"BitmapData_1delete",
	"BitmapData_1new",
	"Bitmap_1GetHBITMAP",
	"Bitmap_1GetHICON",
	"Bitmap_1LockBits",
	"Bitmap_1UnlockBits",
	"Bitmap_1delete",
#ifndef JNI64
	"Bitmap_1new__I",
#else
	"Bitmap_1new__J",
#endif
#ifndef JNI64
	"Bitmap_1new__II",
#else
	"Bitmap_1new__JJ",
#endif
#ifndef JNI64
	"Bitmap_1new__IIIII",
#else
	"Bitmap_1new__IIIIJ",
#endif
	"Bitmap_1new___3CZ",
	"Brush_1Clone",
	"Brush_1GetType",
	"ColorPalette_1sizeof",
	"Color_1delete",
	"Color_1new",
	"FontFamily_1GetFamilyName",
	"FontFamily_1IsAvailable",
	"FontFamily_1delete",
	"FontFamily_1new__",
#ifndef JNI64
	"FontFamily_1new___3CI",
#else
	"FontFamily_1new___3CJ",
#endif
	"Font_1GetFamily",
	"Font_1GetLogFontW",
	"Font_1GetSize",
	"Font_1GetStyle",
	"Font_1IsAvailable",
	"Font_1delete",
#ifndef JNI64
	"Font_1new__IFII",
#else
	"Font_1new__JFII",
#endif
#ifndef JNI64
	"Font_1new__II",
#else
	"Font_1new__JJ",
#endif
#ifndef JNI64
	"Font_1new___3CFIII",
#else
	"Font_1new___3CFIIJ",
#endif
	"GdiplusShutdown",
	"GdiplusStartup",
	"GdiplusStartupInput_1sizeof",
	"GraphicsPath_1AddArc",
	"GraphicsPath_1AddBezier",
	"GraphicsPath_1AddLine",
	"GraphicsPath_1AddPath",
	"GraphicsPath_1AddRectangle",
	"GraphicsPath_1AddString",
	"GraphicsPath_1Clone",
	"GraphicsPath_1CloseFigure",
	"GraphicsPath_1Flatten",
	"GraphicsPath_1GetBounds",
	"GraphicsPath_1GetLastPoint",
	"GraphicsPath_1GetPathPoints",
	"GraphicsPath_1GetPathTypes",
	"GraphicsPath_1GetPointCount",
	"GraphicsPath_1IsOutlineVisible",
	"GraphicsPath_1IsVisible",
	"GraphicsPath_1SetFillMode",
	"GraphicsPath_1StartFigure",
	"GraphicsPath_1Transform",
	"GraphicsPath_1delete",
	"GraphicsPath_1new__I",
	"GraphicsPath_1new___3I_3BII",
	"Graphics_1DrawArc",
#ifndef JNI64
	"Graphics_1DrawDriverString__IIIIILorg_eclipse_swt_internal_gdip_PointF_2II",
#else
	"Graphics_1DrawDriverString__JJIJJLorg_eclipse_swt_internal_gdip_PointF_2IJ",
#endif
#ifndef JNI64
	"Graphics_1DrawDriverString__IIIII_3FII",
#else
	"Graphics_1DrawDriverString__JJIJJ_3FIJ",
#endif
	"Graphics_1DrawEllipse",
#ifndef JNI64
	"Graphics_1DrawImage__IIII",
#else
	"Graphics_1DrawImage__JJII",
#endif
#ifndef JNI64
	"Graphics_1DrawImage__IILorg_eclipse_swt_internal_gdip_Rect_2IIIIIIII",
#else
	"Graphics_1DrawImage__JJLorg_eclipse_swt_internal_gdip_Rect_2IIIIIJJJ",
#endif
	"Graphics_1DrawLine",
	"Graphics_1DrawLines",
	"Graphics_1DrawPath",
	"Graphics_1DrawPolygon",
	"Graphics_1DrawRectangle",
#ifndef JNI64
	"Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2I",
#else
	"Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2J",
#endif
#ifndef JNI64
	"Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2II",
#else
	"Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JJ",
#endif
	"Graphics_1FillEllipse",
	"Graphics_1FillPath",
	"Graphics_1FillPie",
	"Graphics_1FillPolygon",
	"Graphics_1FillRectangle",
	"Graphics_1Flush",
	"Graphics_1GetClip",
#ifndef JNI64
	"Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_RectF_2",
#else
	"Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_RectF_2",
#endif
#ifndef JNI64
	"Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_Rect_2",
#else
	"Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_Rect_2",
#endif
	"Graphics_1GetHDC",
	"Graphics_1GetInterpolationMode",
	"Graphics_1GetSmoothingMode",
	"Graphics_1GetTextRenderingHint",
	"Graphics_1GetTransform",
	"Graphics_1GetVisibleClipBounds",
	"Graphics_1MeasureDriverString",
#ifndef JNI64
	"Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2ILorg_eclipse_swt_internal_gdip_RectF_2",
#else
	"Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JLorg_eclipse_swt_internal_gdip_RectF_2",
#endif
#ifndef JNI64
	"Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2",
#else
	"Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2",
#endif
	"Graphics_1ReleaseHDC",
	"Graphics_1ResetClip",
	"Graphics_1Restore",
	"Graphics_1Save",
	"Graphics_1ScaleTransform",
#ifndef JNI64
	"Graphics_1SetClip__III",
#else
	"Graphics_1SetClip__JJI",
#endif
#ifndef JNI64
	"Graphics_1SetClip__ILorg_eclipse_swt_internal_gdip_Rect_2I",
#else
	"Graphics_1SetClip__JLorg_eclipse_swt_internal_gdip_Rect_2I",
#endif
#ifndef JNI64
	"Graphics_1SetClipPath__II",
#else
	"Graphics_1SetClipPath__JJ",
#endif
#ifndef JNI64
	"Graphics_1SetClipPath__III",
#else
	"Graphics_1SetClipPath__JJI",
#endif
	"Graphics_1SetCompositingQuality",
	"Graphics_1SetInterpolationMode",
	"Graphics_1SetPageUnit",
	"Graphics_1SetPixelOffsetMode",
	"Graphics_1SetSmoothingMode",
	"Graphics_1SetTextRenderingHint",
	"Graphics_1SetTransform",
	"Graphics_1TranslateTransform",
	"Graphics_1delete",
	"Graphics_1new",
	"HatchBrush_1delete",
	"HatchBrush_1new",
	"ImageAttributes_1SetColorMatrix",
	"ImageAttributes_1SetWrapMode",
	"ImageAttributes_1delete",
	"ImageAttributes_1new",
	"Image_1GetHeight",
	"Image_1GetLastStatus",
	"Image_1GetPalette",
	"Image_1GetPaletteSize",
	"Image_1GetPixelFormat",
	"Image_1GetWidth",
	"LinearGradientBrush_1ResetTransform",
	"LinearGradientBrush_1ScaleTransform",
	"LinearGradientBrush_1SetInterpolationColors",
	"LinearGradientBrush_1SetWrapMode",
	"LinearGradientBrush_1TranslateTransform",
	"LinearGradientBrush_1delete",
	"LinearGradientBrush_1new",
	"Matrix_1GetElements",
	"Matrix_1Invert",
	"Matrix_1IsIdentity",
	"Matrix_1Multiply",
	"Matrix_1Rotate",
	"Matrix_1Scale",
	"Matrix_1SetElements",
	"Matrix_1Shear",
#ifndef JNI64
	"Matrix_1TransformPoints__ILorg_eclipse_swt_internal_gdip_PointF_2I",
#else
	"Matrix_1TransformPoints__JLorg_eclipse_swt_internal_gdip_PointF_2I",
#endif
#ifndef JNI64
	"Matrix_1TransformPoints__I_3FI",
#else
	"Matrix_1TransformPoints__J_3FI",
#endif
	"Matrix_1TransformVectors",
	"Matrix_1Translate",
	"Matrix_1delete",
	"Matrix_1new",
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2I",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2J",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2JI",
#endif
	"PathGradientBrush_1SetCenterColor",
	"PathGradientBrush_1SetCenterPoint",
	"PathGradientBrush_1SetGraphicsPath",
	"PathGradientBrush_1SetInterpolationColors",
	"PathGradientBrush_1SetSurroundColors",
	"PathGradientBrush_1SetWrapMode",
	"PathGradientBrush_1delete",
	"PathGradientBrush_1new",
	"Pen_1GetBrush",
	"Pen_1SetBrush",
	"Pen_1SetDashOffset",
	"Pen_1SetDashPattern",
	"Pen_1SetDashStyle",
	"Pen_1SetLineCap",
	"Pen_1SetLineJoin",
	"Pen_1SetMiterLimit",
	"Pen_1SetWidth",
	"Pen_1delete",
	"Pen_1new",
	"Point_1delete",
	"Point_1new",
	"PrivateFontCollection_1AddFontFile",
	"PrivateFontCollection_1delete",
	"PrivateFontCollection_1new",
	"Region_1GetHRGN",
	"Region_1IsInfinite",
	"Region_1delete",
	"Region_1new__",
#ifndef JNI64
	"Region_1new__I",
#else
	"Region_1new__J",
#endif
	"Region_1newGraphicsPath",
	"SolidBrush_1delete",
	"SolidBrush_1new",
	"StringFormat_1Clone",
	"StringFormat_1GenericDefault",
	"StringFormat_1GenericTypographic",
	"StringFormat_1GetFormatFlags",
	"StringFormat_1SetFormatFlags",
	"StringFormat_1SetHotkeyPrefix",
	"StringFormat_1SetTabStops",
	"StringFormat_1delete",
	"TextureBrush_1ResetTransform",
	"TextureBrush_1ScaleTransform",
	"TextureBrush_1SetTransform",
	"TextureBrush_1TranslateTransform",
	"TextureBrush_1delete",
	"TextureBrush_1new",
};
#define NATIVE_FUNCTION_COUNT sizeof(Gdip_nativeFunctionNames) / sizeof(char*)
int Gdip_nativeFunctionCount = NATIVE_FUNCTION_COUNT;
int Gdip_nativeFunctionCallCount[NATIVE_FUNCTION_COUNT];

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(Gdip_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return Gdip_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(Gdip_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return env->NewStringUTF(Gdip_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(Gdip_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return Gdip_nativeFunctionCallCount[index];
}

#endif

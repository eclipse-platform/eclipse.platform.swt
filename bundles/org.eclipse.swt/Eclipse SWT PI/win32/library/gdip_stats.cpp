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
#include "gdip_stats.h"

#ifdef NATIVE_STATS

int Gdip_nativeFunctionCount = 130;
int Gdip_nativeFunctionCallCount[130];
char * Gdip_nativeFunctionNames[] = {
	"Bitmap_1delete",
	"Bitmap_1new__I",
	"Bitmap_1new__II",
	"Bitmap_1new__IIIII",
	"Brush_1Clone",
	"Brush_1GetType",
	"Color_1delete",
	"Color_1new",
	"FontFamily_1GetFamilyName",
	"FontFamily_1delete",
	"FontFamily_1new",
	"Font_1GetFamily",
	"Font_1GetSize",
	"Font_1GetStyle",
	"Font_1IsAvailable",
	"Font_1delete",
	"Font_1new__II",
	"Font_1new___3CFIII",
	"GdiplusShutdown",
	"GdiplusStartup",
	"GraphicsPath_1AddArc",
	"GraphicsPath_1AddBezier",
	"GraphicsPath_1AddLine",
	"GraphicsPath_1AddPath",
	"GraphicsPath_1AddRectangle",
	"GraphicsPath_1AddString",
	"GraphicsPath_1CloseFigure",
	"GraphicsPath_1GetBounds",
	"GraphicsPath_1GetLastPoint",
	"GraphicsPath_1GetPathPoints",
	"GraphicsPath_1GetPathTypes",
	"GraphicsPath_1GetPointCount",
	"GraphicsPath_1IsOutlineVisible",
	"GraphicsPath_1IsVisible",
	"GraphicsPath_1SetFillMode",
	"GraphicsPath_1delete",
	"GraphicsPath_1new",
	"Graphics_1DrawArc",
	"Graphics_1DrawEllipse",
	"Graphics_1DrawImage__IIII",
	"Graphics_1DrawImage__IILorg_eclipse_swt_internal_gdip_Rect_2IIIIIIII",
	"Graphics_1DrawLine",
	"Graphics_1DrawLines",
	"Graphics_1DrawPath",
	"Graphics_1DrawPolygon",
	"Graphics_1DrawRectangle",
	"Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2I",
	"Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2II",
	"Graphics_1FillEllipse",
	"Graphics_1FillPath",
	"Graphics_1FillPie",
	"Graphics_1FillPolygon",
	"Graphics_1FillRectangle",
	"Graphics_1Flush",
	"Graphics_1GetClip",
	"Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_RectF_2",
	"Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_Rect_2",
	"Graphics_1GetHDC",
	"Graphics_1GetInterpolationMode",
	"Graphics_1GetSmoothingMode",
	"Graphics_1GetTextRenderingHint",
	"Graphics_1GetTransform",
	"Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2ILorg_eclipse_swt_internal_gdip_RectF_2",
	"Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2",
	"Graphics_1ReleaseHDC",
	"Graphics_1ResetClip",
	"Graphics_1SetClip__II",
	"Graphics_1SetClip__III",
	"Graphics_1SetClip__ILorg_eclipse_swt_internal_gdip_RectF_2",
	"Graphics_1SetCompositingQuality",
	"Graphics_1SetInterpolationMode",
	"Graphics_1SetPixelOffsetMode",
	"Graphics_1SetSmoothingMode",
	"Graphics_1SetTextRenderingHint",
	"Graphics_1SetTransform",
	"Graphics_1delete",
	"Graphics_1new",
	"HatchBrush_1delete",
	"HatchBrush_1new",
	"Image_1GetHeight",
	"Image_1GetWidth",
	"LinearGradientBrush_1SetWrapMode",
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
	"Matrix_1TransformPoints__ILorg_eclipse_swt_internal_gdip_PointF_2I",
	"Matrix_1TransformPoints__I_3FI",
	"Matrix_1Translate",
	"Matrix_1delete",
	"Matrix_1new",
	"PathGradientBrush_1SetCenterColor",
	"PathGradientBrush_1SetCenterPoint",
	"PathGradientBrush_1SetGraphicsPath",
	"PathGradientBrush_1SetSurroundColors",
	"PathGradientBrush_1delete",
	"PathGradientBrush_1new",
	"Pen_1GetBrush",
	"Pen_1SetBrush",
	"Pen_1SetDashPattern",
	"Pen_1SetDashStyle",
	"Pen_1SetLineCap",
	"Pen_1SetLineJoin",
	"Pen_1delete",
	"Pen_1new",
	"Point_1delete",
	"Point_1new",
	"Region_1GetHRGN",
	"Region_1delete",
	"Region_1new__",
	"Region_1new__I",
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
	"TextureBrush_1SetTransform",
	"TextureBrush_1delete",
	"TextureBrush_1new",
};

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

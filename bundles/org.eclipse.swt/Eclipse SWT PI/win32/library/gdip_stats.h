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

#ifdef NATIVE_STATS
extern int Gdip_nativeFunctionCount;
extern int Gdip_nativeFunctionCallCount[];
extern char* Gdip_nativeFunctionNames[];
#define Gdip_NATIVE_ENTER(env, that, func) Gdip_nativeFunctionCallCount[func]++;
#define Gdip_NATIVE_EXIT(env, that, func) 
#else
#ifndef Gdip_NATIVE_ENTER
#define Gdip_NATIVE_ENTER(env, that, func) 
#endif
#ifndef Gdip_NATIVE_EXIT
#define Gdip_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	BitmapData_1delete_FUNC,
	BitmapData_1new_FUNC,
	Bitmap_1GetHBITMAP_FUNC,
	Bitmap_1GetHICON_FUNC,
	Bitmap_1LockBits_FUNC,
	Bitmap_1UnlockBits_FUNC,
	Bitmap_1delete_FUNC,
#ifndef JNI64
	Bitmap_1new__I_FUNC,
#else
	Bitmap_1new__J_FUNC,
#endif
#ifndef JNI64
	Bitmap_1new__II_FUNC,
#else
	Bitmap_1new__JJ_FUNC,
#endif
#ifndef JNI64
	Bitmap_1new__IIIII_FUNC,
#else
	Bitmap_1new__IIIIJ_FUNC,
#endif
	Bitmap_1new___3CZ_FUNC,
	Brush_1Clone_FUNC,
	Brush_1GetType_FUNC,
	ColorPalette_1sizeof_FUNC,
	Color_1delete_FUNC,
	Color_1new_FUNC,
	FontFamily_1GetFamilyName_FUNC,
	FontFamily_1IsAvailable_FUNC,
	FontFamily_1delete_FUNC,
	FontFamily_1new___FUNC,
#ifndef JNI64
	FontFamily_1new___3CI_FUNC,
#else
	FontFamily_1new___3CJ_FUNC,
#endif
	Font_1GetFamily_FUNC,
	Font_1GetLogFontW_FUNC,
	Font_1GetSize_FUNC,
	Font_1GetStyle_FUNC,
	Font_1IsAvailable_FUNC,
	Font_1delete_FUNC,
#ifndef JNI64
	Font_1new__IFII_FUNC,
#else
	Font_1new__JFII_FUNC,
#endif
#ifndef JNI64
	Font_1new__II_FUNC,
#else
	Font_1new__JJ_FUNC,
#endif
#ifndef JNI64
	Font_1new___3CFIII_FUNC,
#else
	Font_1new___3CFIIJ_FUNC,
#endif
	GdiplusShutdown_FUNC,
	GdiplusStartup_FUNC,
	GdiplusStartupInput_1sizeof_FUNC,
	GraphicsPath_1AddArc_FUNC,
	GraphicsPath_1AddBezier_FUNC,
	GraphicsPath_1AddLine_FUNC,
	GraphicsPath_1AddPath_FUNC,
	GraphicsPath_1AddRectangle_FUNC,
	GraphicsPath_1AddString_FUNC,
	GraphicsPath_1Clone_FUNC,
	GraphicsPath_1CloseFigure_FUNC,
	GraphicsPath_1Flatten_FUNC,
	GraphicsPath_1GetBounds_FUNC,
	GraphicsPath_1GetLastPoint_FUNC,
	GraphicsPath_1GetPathPoints_FUNC,
	GraphicsPath_1GetPathTypes_FUNC,
	GraphicsPath_1GetPointCount_FUNC,
	GraphicsPath_1IsOutlineVisible_FUNC,
	GraphicsPath_1IsVisible_FUNC,
	GraphicsPath_1SetFillMode_FUNC,
	GraphicsPath_1StartFigure_FUNC,
	GraphicsPath_1Transform_FUNC,
	GraphicsPath_1delete_FUNC,
	GraphicsPath_1new__I_FUNC,
	GraphicsPath_1new___3I_3BII_FUNC,
	Graphics_1DrawArc_FUNC,
#ifndef JNI64
	Graphics_1DrawDriverString__IIIIILorg_eclipse_swt_internal_gdip_PointF_2II_FUNC,
#else
	Graphics_1DrawDriverString__JJIJJLorg_eclipse_swt_internal_gdip_PointF_2IJ_FUNC,
#endif
#ifndef JNI64
	Graphics_1DrawDriverString__IIIII_3FII_FUNC,
#else
	Graphics_1DrawDriverString__JJIJJ_3FIJ_FUNC,
#endif
	Graphics_1DrawEllipse_FUNC,
#ifndef JNI64
	Graphics_1DrawImage__IIII_FUNC,
#else
	Graphics_1DrawImage__JJII_FUNC,
#endif
#ifndef JNI64
	Graphics_1DrawImage__IILorg_eclipse_swt_internal_gdip_Rect_2IIIIIIII_FUNC,
#else
	Graphics_1DrawImage__JJLorg_eclipse_swt_internal_gdip_Rect_2IIIIIJJJ_FUNC,
#endif
	Graphics_1DrawLine_FUNC,
	Graphics_1DrawLines_FUNC,
	Graphics_1DrawPath_FUNC,
	Graphics_1DrawPolygon_FUNC,
	Graphics_1DrawRectangle_FUNC,
#ifndef JNI64
	Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2I_FUNC,
#else
	Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2J_FUNC,
#endif
#ifndef JNI64
	Graphics_1DrawString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2II_FUNC,
#else
	Graphics_1DrawString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JJ_FUNC,
#endif
	Graphics_1FillEllipse_FUNC,
	Graphics_1FillPath_FUNC,
	Graphics_1FillPie_FUNC,
	Graphics_1FillPolygon_FUNC,
	Graphics_1FillRectangle_FUNC,
	Graphics_1Flush_FUNC,
	Graphics_1GetClip_FUNC,
#ifndef JNI64
	Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_RectF_2_FUNC,
#else
	Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_RectF_2_FUNC,
#endif
#ifndef JNI64
	Graphics_1GetClipBounds__ILorg_eclipse_swt_internal_gdip_Rect_2_FUNC,
#else
	Graphics_1GetClipBounds__JLorg_eclipse_swt_internal_gdip_Rect_2_FUNC,
#endif
	Graphics_1GetHDC_FUNC,
	Graphics_1GetInterpolationMode_FUNC,
	Graphics_1GetSmoothingMode_FUNC,
	Graphics_1GetTextRenderingHint_FUNC,
	Graphics_1GetTransform_FUNC,
	Graphics_1GetVisibleClipBounds_FUNC,
	Graphics_1MeasureDriverString_FUNC,
#ifndef JNI64
	Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2ILorg_eclipse_swt_internal_gdip_RectF_2_FUNC,
#else
	Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2JLorg_eclipse_swt_internal_gdip_RectF_2_FUNC,
#endif
#ifndef JNI64
	Graphics_1MeasureString__I_3CIILorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2_FUNC,
#else
	Graphics_1MeasureString__J_3CIJLorg_eclipse_swt_internal_gdip_PointF_2Lorg_eclipse_swt_internal_gdip_RectF_2_FUNC,
#endif
	Graphics_1ReleaseHDC_FUNC,
	Graphics_1ResetClip_FUNC,
	Graphics_1Restore_FUNC,
	Graphics_1Save_FUNC,
	Graphics_1ScaleTransform_FUNC,
#ifndef JNI64
	Graphics_1SetClip__III_FUNC,
#else
	Graphics_1SetClip__JJI_FUNC,
#endif
#ifndef JNI64
	Graphics_1SetClip__ILorg_eclipse_swt_internal_gdip_Rect_2I_FUNC,
#else
	Graphics_1SetClip__JLorg_eclipse_swt_internal_gdip_Rect_2I_FUNC,
#endif
#ifndef JNI64
	Graphics_1SetClipPath__II_FUNC,
#else
	Graphics_1SetClipPath__JJ_FUNC,
#endif
#ifndef JNI64
	Graphics_1SetClipPath__III_FUNC,
#else
	Graphics_1SetClipPath__JJI_FUNC,
#endif
	Graphics_1SetCompositingQuality_FUNC,
	Graphics_1SetInterpolationMode_FUNC,
	Graphics_1SetPageUnit_FUNC,
	Graphics_1SetPixelOffsetMode_FUNC,
	Graphics_1SetSmoothingMode_FUNC,
	Graphics_1SetTextRenderingHint_FUNC,
	Graphics_1SetTransform_FUNC,
	Graphics_1TranslateTransform_FUNC,
	Graphics_1delete_FUNC,
	Graphics_1new_FUNC,
	HatchBrush_1delete_FUNC,
	HatchBrush_1new_FUNC,
	ImageAttributes_1SetColorMatrix_FUNC,
	ImageAttributes_1SetWrapMode_FUNC,
	ImageAttributes_1delete_FUNC,
	ImageAttributes_1new_FUNC,
	Image_1GetHeight_FUNC,
	Image_1GetLastStatus_FUNC,
	Image_1GetPalette_FUNC,
	Image_1GetPaletteSize_FUNC,
	Image_1GetPixelFormat_FUNC,
	Image_1GetWidth_FUNC,
	LinearGradientBrush_1ResetTransform_FUNC,
	LinearGradientBrush_1ScaleTransform_FUNC,
	LinearGradientBrush_1SetInterpolationColors_FUNC,
	LinearGradientBrush_1SetWrapMode_FUNC,
	LinearGradientBrush_1TranslateTransform_FUNC,
	LinearGradientBrush_1delete_FUNC,
	LinearGradientBrush_1new_FUNC,
	Matrix_1GetElements_FUNC,
	Matrix_1Invert_FUNC,
	Matrix_1IsIdentity_FUNC,
	Matrix_1Multiply_FUNC,
	Matrix_1Rotate_FUNC,
	Matrix_1Scale_FUNC,
	Matrix_1SetElements_FUNC,
	Matrix_1Shear_FUNC,
#ifndef JNI64
	Matrix_1TransformPoints__ILorg_eclipse_swt_internal_gdip_PointF_2I_FUNC,
#else
	Matrix_1TransformPoints__JLorg_eclipse_swt_internal_gdip_PointF_2I_FUNC,
#endif
#ifndef JNI64
	Matrix_1TransformPoints__I_3FI_FUNC,
#else
	Matrix_1TransformPoints__J_3FI_FUNC,
#endif
	Matrix_1TransformVectors_FUNC,
	Matrix_1Translate_FUNC,
	Matrix_1delete_FUNC,
	Matrix_1new_FUNC,
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2I_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_gdip_BitmapData_2J_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_gdip_ColorPalette_2JI_FUNC,
#endif
	PathGradientBrush_1SetCenterColor_FUNC,
	PathGradientBrush_1SetCenterPoint_FUNC,
	PathGradientBrush_1SetGraphicsPath_FUNC,
	PathGradientBrush_1SetInterpolationColors_FUNC,
	PathGradientBrush_1SetSurroundColors_FUNC,
	PathGradientBrush_1SetWrapMode_FUNC,
	PathGradientBrush_1delete_FUNC,
	PathGradientBrush_1new_FUNC,
	Pen_1GetBrush_FUNC,
	Pen_1SetBrush_FUNC,
	Pen_1SetDashOffset_FUNC,
	Pen_1SetDashPattern_FUNC,
	Pen_1SetDashStyle_FUNC,
	Pen_1SetLineCap_FUNC,
	Pen_1SetLineJoin_FUNC,
	Pen_1SetMiterLimit_FUNC,
	Pen_1SetWidth_FUNC,
	Pen_1delete_FUNC,
	Pen_1new_FUNC,
	Point_1delete_FUNC,
	Point_1new_FUNC,
	PrivateFontCollection_1AddFontFile_FUNC,
	PrivateFontCollection_1delete_FUNC,
	PrivateFontCollection_1new_FUNC,
	Region_1GetHRGN_FUNC,
	Region_1IsInfinite_FUNC,
	Region_1delete_FUNC,
	Region_1new___FUNC,
#ifndef JNI64
	Region_1new__I_FUNC,
#else
	Region_1new__J_FUNC,
#endif
	Region_1newGraphicsPath_FUNC,
	SolidBrush_1delete_FUNC,
	SolidBrush_1new_FUNC,
	StringFormat_1Clone_FUNC,
	StringFormat_1GenericDefault_FUNC,
	StringFormat_1GenericTypographic_FUNC,
	StringFormat_1GetFormatFlags_FUNC,
	StringFormat_1SetFormatFlags_FUNC,
	StringFormat_1SetHotkeyPrefix_FUNC,
	StringFormat_1SetTabStops_FUNC,
	StringFormat_1delete_FUNC,
	TextureBrush_1ResetTransform_FUNC,
	TextureBrush_1ScaleTransform_FUNC,
	TextureBrush_1SetTransform_FUNC,
	TextureBrush_1TranslateTransform_FUNC,
	TextureBrush_1delete_FUNC,
	TextureBrush_1new_FUNC,
} Gdip_FUNCS;

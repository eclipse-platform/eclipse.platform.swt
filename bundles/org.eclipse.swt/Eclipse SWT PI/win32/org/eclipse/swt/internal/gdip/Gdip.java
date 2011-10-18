/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gdip;

import org.eclipse.swt.internal.*;

/** @jniclass flags=cpp */
public class Gdip extends Platform {
	static {
		Library.loadLibrary ("swt-gdip"); //$NON-NLS-1$
	}
	
	/** GdiPlus constants */
	public static final float FlatnessDefault = 0.25f;
	public static final int BrushTypeSolidColor = 0;
	public static final int BrushTypeHatchFill = 1;
	public static final int BrushTypeTextureFill = 2;
	public static final int BrushTypePathGradient = 3;
	public static final int BrushTypeLinearGradient = 4;
	public static final int ColorAdjustTypeBitmap = 1;
	public static final int ColorMatrixFlagsDefault = 0;
	public static final int CombineModeReplace = 0;
	public static final int CombineModeIntersect = 1;
	public static final int CombineModeUnion = 2;
	public static final int CombineModeXor = 3;
	public static final int CombineModeExclude = 4;
	public static final int CombineModeComplement = 5;
	public static final int FillModeAlternate = 0;
	public static final int FillModeWinding = 1;
	public static final int DashCapFlat = 0;
	public static final int DashCapRound = 2;
	public static final int DashCapTriangle = 3;
	public static final int DashStyleSolid = 0;
	public static final int DashStyleDash = 1;
	public static final int DashStyleDot = 2;
	public static final int DashStyleDashDot = 3;
	public static final int DashStyleDashDotDot = 4;
	public static final int DashStyleCustom = 5;
	public static final int DriverStringOptionsRealizedAdvance = 4; 
	public static final int FontStyleRegular = 0;
	public static final int FontStyleBold = 1;
	public static final int FontStyleItalic = 2;
	public static final int FontStyleBoldItalic = 3;
	public static final int FontStyleUnderline = 4;
	public static final int FontStyleStrikeout = 8;
	public static final int PaletteFlagsHasAlpha = 0x0001;
	public static final int FlushIntentionFlush = 0;
	public static final int FlushIntentionSync = 1;
	public static final int HotkeyPrefixNone = 0;
	public static final int HotkeyPrefixShow = 1;
	public static final int HotkeyPrefixHide = 2;
	public static final int LineJoinMiter = 0;
	public static final int LineJoinBevel = 1;
	public static final int LineJoinRound = 2;
	public static final int LineCapFlat = 0;
	public static final int LineCapSquare = 1;
	public static final int LineCapRound = 2;
	public static final int MatrixOrderPrepend = 0;
	public static final int MatrixOrderAppend = 1;
	public static final int QualityModeDefault = 0;
	public static final int QualityModeLow = 1;
	public static final int QualityModeHigh = 2;
	public static final int InterpolationModeDefault = QualityModeDefault;
	public static final int InterpolationModeLowQuality = QualityModeLow;
	public static final int InterpolationModeHighQuality = QualityModeHigh;
	public static final int InterpolationModeBilinear = QualityModeHigh + 1;
	public static final int InterpolationModeBicubic = QualityModeHigh + 2;
	public static final int InterpolationModeNearestNeighbor = QualityModeHigh + 3;
	public static final int InterpolationModeHighQualityBilinear = QualityModeHigh + 4;
	public static final int InterpolationModeHighQualityBicubic = QualityModeHigh + 5;
	public static final int PathPointTypeStart = 0;
	public static final int PathPointTypeLine = 1;
	public static final int PathPointTypeBezier = 3;
	public static final int PathPointTypePathTypeMask = 0x7;
	public static final int PathPointTypePathDashMode = 0x10;
	public static final int PathPointTypePathMarker = 0x20;
	public static final int PathPointTypeCloseSubpath = 0x80;
	public static final int PathPointTypeBezier3 = 3;
	public static final int PixelFormatIndexed = 0x00010000;
	public static final int PixelFormatGDI = 0x00020000;
	public static final int PixelFormatAlpha = 0x00040000;
	public static final int PixelFormatPAlpha = 0x00080000;
	public static final int PixelFormatExtended = 0x00100000;
	public static final int PixelFormatCanonical = 0x00200000;
	public static final int PixelFormat1bppIndexed = (1 | ( 1 << 8) | PixelFormatIndexed | PixelFormatGDI);
	public static final int PixelFormat4bppIndexed = (2 | ( 4 << 8) | PixelFormatIndexed | PixelFormatGDI);
	public static final int PixelFormat8bppIndexed = (3 | ( 8 << 8) | PixelFormatIndexed | PixelFormatGDI);
	public static final int PixelFormat16bppGrayScale = (4 | (16 << 8) | PixelFormatExtended);
	public static final int PixelFormat16bppRGB555 = (5 | (16 << 8) | PixelFormatGDI);
	public static final int PixelFormat16bppRGB565 = (6 | (16 << 8) | PixelFormatGDI);
	public static final int PixelFormat16bppARGB1555 = (7 | (16 << 8) | PixelFormatAlpha | PixelFormatGDI);
	public static final int PixelFormat24bppRGB = (8 | (24 << 8) | PixelFormatGDI);
	public static final int PixelFormat32bppRGB = (9 | (32 << 8) | PixelFormatGDI);
	public static final int PixelFormat32bppARGB = (10 | (32 << 8) | PixelFormatAlpha | PixelFormatGDI | PixelFormatCanonical);
	public static final int PixelFormat32bppPARGB = (11 | (32 << 8) | PixelFormatAlpha | PixelFormatPAlpha | PixelFormatGDI);
	public static final int PixelFormat48bppRGB = (12 | (48 << 8) | PixelFormatExtended);
	public static final int PixelFormat64bppARGB = (13 | (64 << 8) | PixelFormatAlpha  | PixelFormatCanonical | PixelFormatExtended);
	public static final int PixelFormat64bppPARGB = (14 | (64 << 8) | PixelFormatAlpha  | PixelFormatPAlpha | PixelFormatExtended);
	public static final int PixelFormat32bppCMYK = (15 | (32 << 8));
	public static final int PixelFormatMax = 16;
	public static final int PixelOffsetModeNone = QualityModeHigh + 1;
	public static final int PixelOffsetModeHalf = QualityModeHigh + 2;
	public static final int SmoothingModeDefault = QualityModeDefault;
	public static final int SmoothingModeHighSpeed = QualityModeLow;
	public static final int SmoothingModeHighQuality = QualityModeHigh;
	public static final int SmoothingModeNone = 3;
	public static final int SmoothingModeAntiAlias8x4 = 4;
	public static final int SmoothingModeAntiAlias = SmoothingModeAntiAlias8x4;
	public static final int SmoothingModeAntiAlias8x8 = 5;
	public static final int StringFormatFlagsDirectionRightToLeft = 0x00000001;
	public static final int StringFormatFlagsDirectionVertical = 0x00000002;
	public static final int StringFormatFlagsNoFitBlackBox = 0x00000004;
	public static final int StringFormatFlagsDisplayFormatControl = 0x00000020;
	public static final int StringFormatFlagsNoFontFallback = 0x00000400;
	public static final int StringFormatFlagsMeasureTrailingSpaces = 0x00000800;
	public static final int StringFormatFlagsNoWrap = 0x00001000;
	public static final int StringFormatFlagsLineLimit = 0x00002000;
	public static final int StringFormatFlagsNoClip = 0x00004000;
	public static final int TextRenderingHintSystemDefault = 0;
	public static final int TextRenderingHintSingleBitPerPixelGridFit = 1;
	public static final int TextRenderingHintSingleBitPerPixel = 2;
	public static final int TextRenderingHintAntiAliasGridFit = 3;
	public static final int TextRenderingHintAntiAlias = 4;
	public static final int TextRenderingHintClearTypeGridFit = 5;
	public static final int UnitPixel = 2;
	public static final int WrapModeTile = 0;
	public static final int WrapModeTileFlipX = 1;
	public static final int WrapModeTileFlipY = 2;
	public static final int WrapModeTileFlipXY = 3;
	public static final int WrapModeClamp = 4;

/** 64 bit */
public static final native int ColorPalette_sizeof();
public static final native int GdiplusStartupInput_sizeof();

/** GdiPlus natives */

/**
 * @param token cast=(ULONG_PTR *)
 * @param input cast=(const GdiplusStartupInput *)
 * @param output cast=(GdiplusStartupOutput *)
 */
public static final native int GdiplusStartup(int /*long*/[] token, GdiplusStartupInput input, int /*long*/ output);
/** @param token cast=(ULONG_PTR) */
public static final native void GdiplusShutdown(int /*long*/ token);
/**
 * @method flags=new
 * @param hbm cast=(HBITMAP)
 * @param hpal cast=(HPALETTE)
 */
public static final native int /*long*/ Bitmap_new(int /*long*/ hbm, int /*long*/ hpal);
/**
 * @method flags=new
 * @param hicon cast=(HICON)
 */
public static final native int /*long*/ Bitmap_new(int /*long*/ hicon);
/**
 * @method flags=new
 * @param format cast=(PixelFormat)
 * @param scan0 cast=(BYTE *)
 */
public static final native int /*long*/ Bitmap_new(int width, int height, int stride, int format, int /*long*/ scan0);
/**
 * @method flags=new
 * @param filename cast=(WCHAR*)
 */
public static final native int /*long*/ Bitmap_new(char[] filename, boolean useIcm);
/** @method flags=delete */
public static final native void Bitmap_delete(int /*long*/ bitmap);
/**
 * @method flags=cpp
 * @param bitmap cast=(Bitmap*)
 * @param colorBackground cast=(Color*),flags=struct
 * @param hbmReturn cast=(HBITMAP*)
 */
public static final native int Bitmap_GetHBITMAP(int /*long*/ bitmap, int /*long*/ colorBackground, int /*long*/[] hbmReturn);
/**
 * @method flags=cpp
 * @param bitmap cast=(Bitmap*)
 * @param hicon cast=(HICON*)
 */
public static final native int Bitmap_GetHICON(int /*long*/ bitmap, int /*long*/[] hicon);
/** @method flags=new */
public static final native int /*long*/ BitmapData_new();
/** @method flags=delete */
public static final native void BitmapData_delete(int /*long*/ bitmapData);
/**
 * @method flags=cpp
 * @param bitmap cast=(Bitmap*)
 * @param rect cast=(Rect*)
 * @param pixelFormat cast=(PixelFormat)
 * @param lockedBitmapData cast=(BitmapData*)
 */
public static final native int Bitmap_LockBits(int /*long*/ bitmap, int /*long*/ rect, int flags, int pixelFormat, int /*long*/ lockedBitmapData);
/**
 * @method flags=cpp
 * @param bitmap cast=(Bitmap*)
 * @param lockedBitmapData cast=(BitmapData*)
 */
public static final native int Bitmap_UnlockBits(int /*long*/ bitmap, int /*long*/ lockedBitmapData);
/**
 * @method flags=cpp
 * @param brush cast=(Brush *)
 */
public static final native int /*long*/ Brush_Clone(int /*long*/ brush);
/**
 * @method flags=cpp
 * @param brush cast=(Brush *)
 */
public static final native int Brush_GetType(int /*long*/ brush);
/**
 * @method flags=new
 * @param argb cast=(ARGB)
 */
public static final native int /*long*/ Color_new(int argb);
/** @method flags=delete */
public static final native void Color_delete(int /*long*/ color);
/** @method flags=new */
public static final native int /*long*/ PrivateFontCollection_new();
/** @method flags=delete */
public static final native void PrivateFontCollection_delete(int /*long*/ collection);
/**
 * @method flags=cpp
 * @param collection cast=(PrivateFontCollection *)
 * @param filename cast=(const WCHAR *)
 */
public static final native int PrivateFontCollection_AddFontFile(int /*long*/ collection, char[] filename);
/**
 * @method flags=new
 * @param hdc cast=(HDC)
 * @param hfont cast=(HFONT)
 */
public static final native int /*long*/ Font_new(int /*long*/ hdc, int /*long*/ hfont);
/**
 * @method flags=new
 * @param family cast=(const FontFamily *)
 * @param emSize cast=(REAL)
 * @param style cast=(INT)
 * @param unit cast=(Unit)
 */
public static final native int /*long*/ Font_new(int /*long*/ family, float emSize, int style, int unit);
/**
 * @method flags=new
 * @param familyName cast=(const WCHAR *)
 * @param emSize cast=(REAL)
 * @param style cast=(INT)
 * @param unit cast=(Unit)
 * @param fontCollection cast=(const FontCollection *)
 */
public static final native int /*long*/ Font_new(char[] familyName, float emSize, int style, int unit, int /*long*/ fontCollection);
/** @method flags=delete */
public static final native void Font_delete(int /*long*/ font);
/**
 * @method flags=cpp
 * @param font cast=(Font *)
 * @param family cast=(FontFamily *)
 */
public static final native int Font_GetFamily(int /*long*/ font, int /*long*/ family);
/**
 * @method flags=cpp
 * @param font cast=(Font *)
 */
public static final native float Font_GetSize(int /*long*/ font);
/**
 * @method flags=cpp
 * @param font cast=(Font *)
 */
public static final native int Font_GetStyle(int /*long*/ font);
/**
 * @method flags=cpp
 * @param font cast=(Font *)
 * @param g cast=(const Graphics *)
 * @param logfontW cast=(LOGFONTW *)
 */
public static final native int Font_GetLogFontW (int /*long*/ font, int /*long*/ g, int /*long*/  logfontW);
/**
 * @method flags=cpp
 * @param font cast=(Font *)
 */
public static final native boolean Font_IsAvailable(int /*long*/ font);
/** @method flags=new */
public static final native int /*long*/ FontFamily_new();
/**
 * @method flags=new
 * @param name cast=(const WCHAR *)
 * @param fontCollection cast=(const FontCollection *)
 */
public static final native int /*long*/ FontFamily_new(char[] name, int /*long*/ fontCollection);
/** @method flags=delete */
public static final native void FontFamily_delete(int /*long*/ family);
/**
 * @method flags=cpp
 * @param family cast=(FontFamily *)
 * @param name cast=(WCHAR *)
 * @param language cast=(WCHAR)
 */
public static final native int FontFamily_GetFamilyName(int /*long*/ family, char[] name, char language);
/**
 * @method flags=cpp
 * @param family cast=(FontFamily *)
 */
public static final native boolean FontFamily_IsAvailable(int /*long*/ family);
/**
 * @method flags=new
 * @param hdc cast=(HDC)
 */
public static final native int /*long*/ Graphics_new(int /*long*/ hdc);
/** @method flags=delete */
public static final native void Graphics_delete(int /*long*/ graphics);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param pen cast=(Pen *)
 */
public static final native int Graphics_DrawArc(int /*long*/ graphics, int /*long*/ pen, int x, int y, int width, int height, float startAngle, float sweepAngle);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param text cast=(const UINT16 *)
 * @param font cast=(const Font *)
 * @param brush cast=(const Brush *)
 * @param positions cast=(const PointF *)
 * @param matrix cast=(const Matrix *)
 */
public static final native int Graphics_DrawDriverString(int /*long*/ graphics, int /*long*/ text, int length, int /*long*/ font, int /*long*/ brush, PointF /*long*/ positions, int flags, int /*long*/ matrix);
/**
 * @method flags=no_gen cpp
 * @param graphics cast=(Graphics *)
 * @param text cast=(const UINT16 *)
 * @param font cast=(const Font *)
 * @param brush cast=(const Brush *)
 * @param positions cast=(const PointF *)
 * @param matrix cast=(const Matrix *)
 */
public static final native int Graphics_DrawDriverString(int /*long*/ graphics, int /*long*/ text, int length, int /*long*/ font, int /*long*/ brush, float[] positions, int flags, int /*long*/ matrix);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param pen cast=(Pen *)
 */
public static final native int Graphics_DrawEllipse(int /*long*/ graphics, int /*long*/ pen, int x, int y, int width, int height);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param image cast=(Image *)
 * @param x cast=(INT)
 * @param y cast=(INT)
 */
public static final native int Graphics_DrawImage(int /*long*/ graphics, int /*long*/ image, int x, int y);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param image cast=(Image *)
 * @param destRect cast=(const Rect *),flags=struct
 * @param srcx cast=(INT)
 * @param srcy cast=(INT)
 * @param srcwidth cast=(INT)
 * @param srcheight cast=(INT)
 * @param srcUnit cast=(Unit)
 * @param imageAttributes cast=(ImageAttributes *)
 * @param callback cast=(DrawImageAbort)
 * @param callbackData cast=(VOID *)
 */
public static final native int Graphics_DrawImage(int /*long*/ graphics, int /*long*/ image, Rect destRect, int srcx, int srcy, int srcwidth, int srcheight, int srcUnit, int /*long*/ imageAttributes, int /*long*/ callback, int /*long*/ callbackData);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param pen cast=(Pen *)
 */
public static final native int Graphics_DrawLine(int /*long*/ graphics, int /*long*/ pen, int x1, int y1, int x2, int y2);
/**
 * @method flags=no_gen cpp
 * @param graphics cast=(Graphics *)
 * @param pen cast=(Pen *)
 * @param count cast=(INT)
 */
public static final native int Graphics_DrawLines(int /*long*/ graphics, int /*long*/ pen, int[] points, int count);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param pen cast=(Pen *)
 * @param path cast=(GraphicsPath *)
 */
public static final native int Graphics_DrawPath(int /*long*/ graphics, int /*long*/ pen, int /*long*/ path);
/**
 * @method flags=no_gen cpp
 * @param graphics cast=(Graphics *)
 * @param pen cast=(Pen *)
 * @param count cast=(INT)
 */
public static final native int Graphics_DrawPolygon(int /*long*/ graphics, int /*long*/ pen, int[] points, int count);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param pen cast=(Pen *)
 */
public static final native int Graphics_DrawRectangle(int /*long*/ graphics, int /*long*/ pen, int x, int y, int width, int height);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param string cast=(WCHAR *)
 * @param length cast=(int)
 * @param font cast=(Font *)
 * @param origin flags=struct
 * @param brush cast=(Brush *)
 */
public static final native int Graphics_DrawString(int /*long*/ graphics, char[] string, int length, int /*long*/ font, PointF origin, int /*long*/ brush);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param string cast=(WCHAR *)
 * @param length cast=(int)
 * @param font cast=(Font *)
 * @param origin flags=struct
 * @param format cast=(StringFormat *)
 * @param brush cast=(Brush *)
 */
public static final native int Graphics_DrawString(int /*long*/ graphics, char[] string, int length, int /*long*/ font, PointF origin, int /*long*/ format, int /*long*/ brush);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param brush cast=(Brush *)
 * @param x cast=(INT)
 * @param y cast=(INT)
 * @param width cast=(INT)
 * @param height cast=(INT)
 */
public static final native int Graphics_FillEllipse(int /*long*/ graphics, int /*long*/ brush, int x, int y, int width, int height);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param brush cast=(Brush *)
 * @param path cast=(GraphicsPath *)
 */
public static final native int Graphics_FillPath(int /*long*/ graphics, int /*long*/ brush, int /*long*/ path);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param intention cast=(FlushIntention)
 */
public static final native void Graphics_Flush(int /*long*/ graphics, int intention);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param brush cast=(Brush *)
 * @param x cast=(INT)
 * @param y cast=(INT)
 * @param width cast=(INT)
 * @param height cast=(INT)
 * @param startAngle cast=(REAL)
 * @param sweepAngle cast=(REAL)
 */
public static final native int Graphics_FillPie(int /*long*/ graphics, int /*long*/ brush, int x, int y, int width, int height, float startAngle, float sweepAngle);
/**
 * @method flags=no_gen cpp
 * @param graphics cast=(Graphics *)
 * @param brush cast=(Brush *)
 * @param points cast=(Point *)
 * @param count cast=(INT)
 * @param fillMode cast=(FillMode)
 */
public static final native int Graphics_FillPolygon(int /*long*/ graphics, int /*long*/ brush, int[] points, int count, int fillMode);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param brush cast=(Brush *)
 * @param x cast=(INT)
 * @param y cast=(INT)
 * @param width cast=(INT)
 * @param height cast=(INT)
 */
public static final native int Graphics_FillRectangle(int /*long*/ graphics, int /*long*/ brush, int x, int y, int width, int height);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 */
public static final native int Graphics_GetClipBounds(int /*long*/ graphics, RectF rect);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 */
public static final native int Graphics_GetClipBounds(int /*long*/ graphics, Rect rect);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param region cast=(Region *)
 */
public static final native int Graphics_GetClip(int /*long*/ graphics, int /*long*/ region);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 */
public static final native int /*long*/ Graphics_GetHDC(int /*long*/ graphics);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param hdc cast=(HDC)
 */
public static final native void Graphics_ReleaseHDC(int /*long*/ graphics, int /*long*/ hdc);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 */
public static final native int Graphics_GetInterpolationMode(int /*long*/ graphics);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 */
public static final native int Graphics_GetSmoothingMode(int /*long*/ graphics);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 */
public static final native int Graphics_GetTextRenderingHint(int /*long*/ graphics);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param matrix cast=(Matrix *)
 */
public static final native int Graphics_GetTransform(int /*long*/ graphics, int /*long*/ matrix);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 */
public static final native int Graphics_GetVisibleClipBounds(int /*long*/ graphics, Rect rect);
/** @method flags=no_gen */
public static final native int Graphics_MeasureDriverString(int /*long*/ graphics, int /*long*/ text, int length, int /*long*/ font, float[] positions, int flags, int /*long*/ matrix, RectF boundingBox);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param string cast=(const WCHAR *)
 * @param length cast=(INT)
 * @param font cast=(const Font *)
 * @param origin cast=(const PointF *),flags=struct
 * @param boundingBox cast=(RectF *)
 */
public static final native int Graphics_MeasureString(int /*long*/ graphics, char[] string, int length, int /*long*/ font, PointF origin, RectF boundingBox);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param string cast=(const WCHAR *)
 * @param length cast=(INT)
 * @param font cast=(const Font *)
 * @param origin cast=(const PointF *),flags=struct
 * @param format cast=(StringFormat *)
 */
public static final native int Graphics_MeasureString(int /*long*/ graphics, char[] string, int length, int /*long*/ font, PointF origin, int /*long*/ format, RectF boundingBox);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 */
public static final native int Graphics_ResetClip(int /*long*/ graphics);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param gstate cast=(GraphicsState)
 */
public static final native int Graphics_Restore(int /*long*/ graphics, int gstate);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 */
public static final native int Graphics_Save(int /*long*/ graphics);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param order cast=(MatrixOrder)
 */
public static final native int Graphics_ScaleTransform(int /*long*/ graphics, float sx, float sy, int order);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param hrgn cast=(Region *)
 * @param combineMode cast=(CombineMode)
 */
public static final native int Graphics_SetClip(int /*long*/ graphics, int /*long*/ hrgn, int combineMode);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param rect cast=(Rect *),flags=struct
 * @param combineMode cast=(CombineMode)
 */
public static final native int Graphics_SetClip(int /*long*/ graphics, Rect rect, int combineMode);
/**
 * @method accessor=SetClip,flags=cpp
 * @param graphics cast=(Graphics *)
 * @param path cast=(GraphicsPath *)
 */
public static final native int Graphics_SetClipPath(int /*long*/ graphics, int /*long*/ path);
/**
 * @method accessor=SetClip,flags=cpp
 * @param graphics cast=(Graphics *)
 * @param path cast=(GraphicsPath *)
 * @param combineMode cast=(CombineMode)
 */
public static final native int Graphics_SetClipPath(int /*long*/ graphics, int /*long*/ path, int combineMode);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param compositingQuality cast=(CompositingQuality)
 */
public static final native int Graphics_SetCompositingQuality(int /*long*/ graphics, int compositingQuality);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param unit cast=(Unit)
 */
public static final native int Graphics_SetPageUnit(int /*long*/ graphics, int unit);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param pixelOffsetMode cast=(PixelOffsetMode)
 */
public static final native int Graphics_SetPixelOffsetMode(int /*long*/ graphics, int pixelOffsetMode);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param smoothingMode cast=(SmoothingMode)
 */
public static final native int Graphics_SetSmoothingMode(int /*long*/ graphics, int smoothingMode);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param matrix cast=(Matrix *)
 */
public static final native int Graphics_SetTransform(int /*long*/ graphics, int /*long*/ matrix);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param mode cast=(InterpolationMode)
 */
public static final native int Graphics_SetInterpolationMode(int /*long*/ graphics, int mode);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param mode cast=(TextRenderingHint)
 */
public static final native int Graphics_SetTextRenderingHint(int /*long*/ graphics, int mode);
/**
 * @method flags=cpp
 * @param graphics cast=(Graphics *)
 * @param order cast=(MatrixOrder)
 */
public static final native int Graphics_TranslateTransform(int /*long*/ graphics, float dx, float dy, int order);
/**
 * @method flags=new
 * @param fillMode cast=(FillMode)
 */
public static final native int /*long*/ GraphicsPath_new(int fillMode);
/**
 * @method flags=no_gen new
 * @param points cast=(FillMode)
 */
public static final native int /*long*/ GraphicsPath_new(int[] points, byte[] types, int count, int fillMode);
/** @method flags=delete */
public static final native void GraphicsPath_delete(int /*long*/ path);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param x cast=(REAL)
 * @param y cast=(REAL)
 * @param width cast=(REAL)
 * @param height cast=(REAL)
 * @param startAngle cast=(REAL)
 * @param sweepAngle cast=(REAL)
 */
public static final native int GraphicsPath_AddArc(int /*long*/ path, float x, float y, float width, float height, float startAngle, float sweepAngle);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param x1 cast=(REAL)
 * @param y1 cast=(REAL)
 * @param x2 cast=(REAL)
 * @param y2 cast=(REAL)
 * @param x3 cast=(REAL)
 * @param y3 cast=(REAL)
 * @param x4 cast=(REAL)
 * @param y4 cast=(REAL)
 */
public static final native int GraphicsPath_AddBezier(int /*long*/ path, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param x1 cast=(REAL)
 * @param y1 cast=(REAL)
 * @param x2 cast=(REAL)
 * @param y2 cast=(REAL)
 */
public static final native int GraphicsPath_AddLine(int /*long*/ path, float x1, float y1, float x2, float y2);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param addingPath cast=(GraphicsPath *)
 * @param connect cast=(BOOL)
 */
public static final native int GraphicsPath_AddPath(int /*long*/ path, int /*long*/ addingPath, boolean connect);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param rect flags=struct
 */
public static final native int GraphicsPath_AddRectangle(int /*long*/ path, RectF rect);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param string cast=(const WCHAR *)
 * @param length cast=(INT)
 * @param family cast=(const FontFamily *)
 * @param style cast=(INT)
 * @param emSize cast=(REAL)
 * @param origin cast=(const PointF *),flags=struct
 * @param format cast=(const StringFormat *)
 */
public static final native int GraphicsPath_AddString(int /*long*/ path, char[] string, int length, int /*long*/ family, int style, float emSize, PointF origin, int /*long*/ format);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 */
public static final native int GraphicsPath_CloseFigure(int /*long*/ path);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 */
public static final native int /*long*/ GraphicsPath_Clone(int /*long*/ path);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param matrix cast=(Matrix *)
 */
public static final native int GraphicsPath_Flatten(int /*long*/ path, int /*long*/ matrix, float flatness);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param matrix cast=(Matrix *)
 * @param pen cast=(Pen *)
 */
public static final native int GraphicsPath_GetBounds(int /*long*/ path, RectF bounds, int /*long*/ matrix, int /*long*/ pen);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param lastPoint cast=(PointF *)
 */
public static final native int GraphicsPath_GetLastPoint(int /*long*/ path, PointF lastPoint);
/**
 * @method flags=no_gen cpp
 * @param path cast=(GraphicsPath *)
 * @param points cast=(PointF *)
 */
public static final native int GraphicsPath_GetPathPoints(int /*long*/ path, float[] points, int count);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param types cast=(BYTE *)
 */
public static final native int GraphicsPath_GetPathTypes(int /*long*/ path, byte[] types, int count);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 */
public static final native int GraphicsPath_GetPointCount(int /*long*/ path);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param pen cast=(const Pen *)
 * @param g cast=(const Graphics *)
 */
public static final native boolean GraphicsPath_IsOutlineVisible(int /*long*/ path, float x, float y, int /*long*/ pen, int /*long*/ g);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param g cast=(const Graphics *)
 */
public static final native boolean GraphicsPath_IsVisible(int /*long*/ path, float x, float y, int /*long*/ g);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param fillmode cast=(FillMode)
 */
public static final native int GraphicsPath_SetFillMode(int /*long*/ path, int fillmode);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 */
public static final native int GraphicsPath_StartFigure(int /*long*/ path);
/**
 * @method flags=cpp
 * @param path cast=(GraphicsPath *)
 * @param matrix cast=(Matrix *)
 */
public static final native int GraphicsPath_Transform(int /*long*/ path, int /*long*/ matrix);
/**
 * @method flags=new
 * @param hatchStyle cast=(HatchStyle)
 * @param foreColor cast=(Color *),flags=struct
 * @param backColor cast=(Color *),flags=struct
 */
public static final native int /*long*/ HatchBrush_new(int hatchStyle, int /*long*/ foreColor, int /*long*/ backColor);
/**
 * @method flags=cpp
 * @param image cast=(Image*)
 */
public static final native int Image_GetLastStatus(int /*long*/ image);
/**
 * @method flags=cpp
 * @param image cast=(Image*)
 */
public static final native int Image_GetPixelFormat(int /*long*/ image);
/**
 * @method flags=cpp
 * @param image cast=(Image *)
 */
public static final native int Image_GetWidth(int /*long*/ image);
/**
 * @method flags=cpp
 * @param image cast=(Image *)
 */
public static final native int Image_GetHeight(int /*long*/ image);
/**
 * @method flags=cpp
 * @param image cast=(Image*)
 * @param palette cast=(ColorPalette*)
 */
public static final native int Image_GetPalette(int /*long*/ image, int /*long*/ palette, int size);
/**
 * @method flags=cpp
 * @param image cast=(Image*)
 */
public static final native int Image_GetPaletteSize(int /*long*/ image);
/** @method flags=new */
public static final native int /*long*/ ImageAttributes_new();
/** @method flags=delete */
public static final native void ImageAttributes_delete(int /*long*/ attrib);
/**
 * @method flags=cpp
 * @param attrib cast=(ImageAttributes *)
 * @param wrap cast=(WrapMode)
 */
public static final native int ImageAttributes_SetWrapMode(int /*long*/ attrib, int wrap);
/**
 * @method flags=cpp
 * @param attrib cast=(ImageAttributes *)
 * @param matrix cast=(ColorMatrix *)
 * @param mode cast=(ColorMatrixFlags)
 * @param type cast=(ColorAdjustType)
 */
public static final native int ImageAttributes_SetColorMatrix(int /*long*/ attrib, float[] matrix, int mode, int type);
/** @method flags=delete */
public static final native void HatchBrush_delete(int /*long*/ brush);
/**
 * @method flags=new
 * @param point1 flags=struct
 * @param point2 flags=struct
 * @param color1 cast=(Color *),flags=struct
 * @param color2 cast=(Color *),flags=struct
 */
public static final native int /*long*/ LinearGradientBrush_new(PointF point1, PointF point2, int /*long*/ color1, int /*long*/ color2);
/** @method flags=delete */
public static final native void LinearGradientBrush_delete(int /*long*/ brush);
/**
 * @method flags=no_gen cpp
 * @param brush cast=(LinearGradientBrush *)
 * @param presetColors cast=(const Color *)
 * @param blendPositions cast=(const REAL *)
 */
public static final native int LinearGradientBrush_SetInterpolationColors(int /*long*/ brush, int /*long*/[] presetColors, float[] blendPositions, int count);
/**
 * @method flags=cpp
 * @param brush cast=(LinearGradientBrush *)
 * @param wrapMode cast=(WrapMode)
 */
public static final native int LinearGradientBrush_SetWrapMode(int /*long*/ brush, int wrapMode);
/**
 * @method flags=cpp
 * @param brush cast=(LinearGradientBrush *)
 */
public static final native int LinearGradientBrush_ResetTransform(int /*long*/ brush);
/**
 * @method flags=cpp
 * @param brush cast=(LinearGradientBrush *)
 * @param order cast=(MatrixOrder)
 */
public static final native int LinearGradientBrush_ScaleTransform(int /*long*/ brush, float sx, float sy, int order);
/**
 * @method flags=cpp
 * @param brush cast=(LinearGradientBrush *)
 * @param order cast=(MatrixOrder)
 */
public static final native int LinearGradientBrush_TranslateTransform(int /*long*/ brush, float dx, float dy, int order);
/**
 * @method flags=new
 * @param m11 cast=(REAL)
 * @param m12 cast=(REAL)
 * @param m21 cast=(REAL)
 * @param m22 cast=(REAL)
 * @param dx cast=(REAL)
 * @param dy cast=(REAL)
 */
public static final native int /*long*/ Matrix_new(float m11, float m12, float m21, float m22, float dx, float dy);
/** @method flags=delete */
public static final native void Matrix_delete(int /*long*/ matrix);
/**
 * @method flags=cpp
 * @param matrix cast=(Matrix *)
 * @param m cast=(REAL *)
 */
public static final native int Matrix_GetElements(int /*long*/ matrix, float[] m);
/**
 * @method flags=cpp
 * @param matrix cast=(Matrix *)
 */
public static final native int Matrix_Invert(int /*long*/ matrix);
/**
 * @method flags=cpp
 * @param matrix cast=(Matrix *)
 */
public static final native boolean Matrix_IsIdentity(int /*long*/ matrix);
/**
 * @method flags=cpp
 * @param matrix cast=(Matrix *)
 * @param matrix1 cast=(Matrix *)
 * @param order cast=(MatrixOrder)
 */
public static final native int Matrix_Multiply(int /*long*/ matrix, int /*long*/ matrix1, int order);
/**
 * @method flags=cpp
 * @param matrix cast=(Matrix *)
 * @param angle cast=(REAL)
 * @param order cast=(MatrixOrder)
 */
public static final native int Matrix_Rotate(int /*long*/ matrix, float angle, int order);
/**
 * @method flags=cpp
 * @param matrix cast=(Matrix *)
 * @param scaleX cast=(REAL)
 * @param scaleY cast=(REAL)
 * @param order cast=(MatrixOrder)
 */
public static final native int Matrix_Scale(int /*long*/ matrix, float scaleX, float scaleY, int order);
/**
 * @method flags=cpp
 * @param matrix cast=(Matrix *)
 * @param shearX cast=(REAL)
 * @param shearY cast=(REAL)
 * @param order cast=(MatrixOrder)
 */
public static final native int Matrix_Shear(int /*long*/ matrix, float shearX, float shearY, int order);
/**
 * @method flags=cpp
 * @param matrix cast=(Matrix *)
 */
public static final native int Matrix_TransformPoints(int /*long*/ matrix, PointF pts, int count);
/** @method flags=no_gen */
public static final native int Matrix_TransformPoints(int /*long*/ matrix, float[] pts, int count);
/**
 * @method flags=cpp
 * @param matrix cast=(Matrix *)
 */
public static final native int Matrix_TransformVectors(int /*long*/ matrix, PointF pts, int count);
/**
 * @method flags=cpp
 * @param matrix cast=(Matrix *)
 * @param offsetX cast=(REAL)
 * @param offsetY cast=(REAL)
 * @param order cast=(MatrixOrder)
 */
public static final native int Matrix_Translate(int /*long*/ matrix, float offsetX, float offsetY, int order);
/**
 * @method flags=cpp
 * @param matrix cast=(Matrix *)
 * @param m11 cast=(REAL)
 * @param m12 cast=(REAL)
 * @param m21 cast=(REAL)
 * @param m22 cast=(REAL)
 * @param dx cast=(REAL)
 * @param dy cast=(REAL)
 */
public static final native int Matrix_SetElements(int /*long*/ matrix, float m11, float m12, float m21, float m22, float dx, float dy);
/**
 * @param Destination cast=(PVOID)
 * @param SourcePtr cast=(CONST VOID*)
 */
public static final native void MoveMemory(ColorPalette Destination, int /*long*/ SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param SourcePtr cast=(CONST VOID*)
 */
public static final native void MoveMemory(BitmapData Destination, int /*long*/ SourcePtr);
/**
 * @method flags=new
 * @param path cast=(GraphicsPath *)
 */
public static final native int /*long*/ PathGradientBrush_new(int /*long*/ path);
/** @method flags=delete */
public static final native void PathGradientBrush_delete(int /*long*/ brush);
/**
 * @method flags=cpp
 * @param brush cast=(PathGradientBrush *)
 * @param color cast=(Color *),flags=struct
 */
public static final native int PathGradientBrush_SetCenterColor(int /*long*/ brush, int /*long*/ color);
/**
 * @method flags=cpp
 * @param brush cast=(PathGradientBrush *)
 * @param pt flags=struct
 */
public static final native int PathGradientBrush_SetCenterPoint(int /*long*/ brush, PointF pt);
/**
 * @method flags=no_gen
 * @param brush cast=(PathGradientBrush *)
 * @param presetColors cast=(const Color *)
 * @param blendPositions cast=(const REAL *)
 */
public static final native int PathGradientBrush_SetInterpolationColors(int /*long*/ brush, int /*long*/[] presetColors, float[] blendPositions, int count);
/**
 * @method flags=no_gen
 * @param brush cast=(PathGradientBrush *)
 * @param colors cast=(Color *)
 * @param count cast=(INT *)
 */
public static final native int PathGradientBrush_SetSurroundColors(int /*long*/ brush, int /*long*/[] colors, int[] count);
/**
 * @method flags=cpp
 * @param brush cast=(PathGradientBrush *)
 * @param path cast=(GraphicsPath *)
 */
public static final native int PathGradientBrush_SetGraphicsPath(int /*long*/ brush, int /*long*/ path);
/**
 * @method flags=cpp
 * @param brush cast=(PathGradientBrush *)
 * @param wrapMode cast=(WrapMode)
 */
public static final native int PathGradientBrush_SetWrapMode(int /*long*/ brush, int wrapMode);
/**
 * @method flags=new
 * @param brush cast=(Brush *)
 * @param width cast=(REAL)
 */
public static final native int /*long*/ Pen_new(int /*long*/ brush, float width);
/** @method flags=delete */
public static final native void Pen_delete(int /*long*/ pen);
/**
 * @method flags=cpp
 * @param pen cast=(Pen *)
 */
public static final native int /*long*/ Pen_GetBrush(int /*long*/ pen);
/**
 * @method flags=cpp
 * @param pen cast=(Pen *)
 * @param brush cast=(Brush *)
 */
public static final native int Pen_SetBrush(int /*long*/ pen, int /*long*/ brush);
/**
 * @method flags=cpp
 * @param pen cast=(Pen *)
 */
public static final native int Pen_SetDashOffset(int /*long*/ pen, float dashOffset);
/**
 * @method flags=cpp
 * @param pen cast=(Pen *)
 * @param dashArray cast=(REAL *)
 * @param count cast=(int)
 */
public static final native int Pen_SetDashPattern(int /*long*/ pen, float[] dashArray, int count);
/**
 * @method flags=cpp
 * @param pen cast=(Pen *)
 * @param dashStyle cast=(DashStyle)
 */
public static final native int Pen_SetDashStyle(int /*long*/ pen, int dashStyle);
/**
 * @method flags=cpp
 * @param pen cast=(Pen *)
 * @param startCap cast=(LineCap)
 * @param endCap cast=(LineCap)
 * @param dashCap cast=(DashCap)
 */
public static final native int Pen_SetLineCap(int /*long*/ pen, int startCap, int endCap, int dashCap);
/**
 * @method flags=cpp
 * @param pen cast=(Pen *)
 * @param lineJoin cast=(LineJoin)
 */
public static final native int Pen_SetLineJoin(int /*long*/ pen, int lineJoin);
/**
 * @method flags=cpp
 * @param pen cast=(Pen *)
 */
public static final native int Pen_SetMiterLimit(int /*long*/ pen, float miterLimit);
/**
 * @method flags=cpp
 * @param pen cast=(Pen *)
 * @param width cast=(REAL)
 */
public static final native int Pen_SetWidth(int /*long*/ pen, float width);
/** @method flags=new */
public static final native int /*long*/ Point_new(int x, int y);
/** @method flags=delete */
public static final native void Point_delete(int /*long*/ point);
/**
 * @method flags=new
 * @param hRgn cast=(HRGN)
 */
public static final native int /*long*/ Region_new(int /*long*/ hRgn);
/**
 * @method flags=new
 * @param path cast=(GraphicsPath*)
 */
public static final native int /*long*/ Region_newGraphicsPath(int /*long*/ path);
/** @method flags=new */
public static final native int /*long*/ Region_new();
/** @method flags=delete */
public static final native void Region_delete(int /*long*/ region);
/**
 * @method flags=cpp
 * @param region cast=(Region *)
 * @param graphics cast=(Graphics *)
 */
public static final native int /*long*/ Region_GetHRGN(int /*long*/ region, int /*long*/ graphics);
/**
 * @method flags=cpp
 * @param region cast=(Region *)
 * @param graphics cast=(Graphics *)
 */
public static final native boolean Region_IsInfinite(int /*long*/ region, int /*long*/ graphics);
/**
 * @method flags=new
 * @param color cast=(Color *),flags=struct
 */
public static final native int /*long*/ SolidBrush_new(int /*long*/ color);
/** @method flags=delete */
public static final native void SolidBrush_delete(int /*long*/ brush);
/** @method flags=delete */
public static final native void StringFormat_delete(int /*long*/ format);
/**
 * @method flags=cpp
 * @param format cast=(StringFormat *)
 */
public static final native int /*long*/ StringFormat_Clone(int /*long*/ format);
/** @method accessor=StringFormat::GenericDefault */
public static final native int /*long*/ StringFormat_GenericDefault();
/** @method accessor=StringFormat::GenericTypographic */
public static final native int /*long*/ StringFormat_GenericTypographic();
/**
 * @method flags=cpp
 * @param format cast=(StringFormat *)
 */
public static final native int StringFormat_GetFormatFlags(int /*long*/ format);
/**
 * @method flags=cpp
 * @param format cast=(StringFormat *)
 * @param hotkeyPrefix cast=(HotkeyPrefix)
 */
public static final native int StringFormat_SetHotkeyPrefix(int /*long*/ format, int hotkeyPrefix);
/**
 * @method flags=cpp
 * @param format cast=(StringFormat *)
 * @param flags cast=(StringFormatFlags)
 */
public static final native int StringFormat_SetFormatFlags(int /*long*/ format, int flags);
/**
 * @method flags=cpp
 * @param format cast=(StringFormat *)
 */
public static final native int StringFormat_SetTabStops(int /*long*/ format, float firstTabOffset, int count, float[] tabStops);
/**
 * @method flags=new
 * @param image cast=(Image *)
 * @param wrapMode cast=(WrapMode)
 */
public static final native int /*long*/ TextureBrush_new(int /*long*/ image, int wrapMode, float dstX, float dstY, float dstWidth, float dstHeight);
/** @method flags=delete */
public static final native void TextureBrush_delete(int /*long*/ brush);
/**
 * @method flags=cpp
 * @param brush cast=(TextureBrush *)
 * @param matrix cast=(Matrix *)
 */
public static final native int TextureBrush_SetTransform(int /*long*/ brush, int /*long*/ matrix);
/**
 * @method flags=cpp
 * @param brush cast=(TextureBrush *)
 */
public static final native int TextureBrush_ResetTransform(int /*long*/ brush);
/**
 * @method flags=cpp
 * @param brush cast=(TextureBrush *)
 * @param order cast=(MatrixOrder)
 */
public static final native int TextureBrush_ScaleTransform(int /*long*/ brush, float sx, float sy, int order);
/**
 * @method flags=cpp
 * @param brush cast=(TextureBrush *)
 * @param order cast=(MatrixOrder)
 */
public static final native int TextureBrush_TranslateTransform(int /*long*/ brush, float dx, float dy, int order);
}

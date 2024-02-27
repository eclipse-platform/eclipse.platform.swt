/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gdip.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Class <code>GC</code> is where all of the drawing capabilities that are
 * supported by SWT are located. Instances are used to draw on either an
 * <code>Image</code>, a <code>Control</code>, or directly on a <code>Display</code>.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * </dl>
 *
 * <p>
 * The SWT drawing coordinate system is the two-dimensional space with the origin
 * (0,0) at the top left corner of the drawing area and with (x,y) values increasing
 * to the right and downward respectively.
 * </p>
 *
 * <p>
 * The result of drawing on an image that was created with an indexed
 * palette using a color that is not in the palette is platform specific.
 * Some platforms will match to the nearest color while other will draw
 * the color itself. This happens because the allocated image might use
 * a direct palette on platforms that do not support indexed palette.
 * </p>
 *
 * <p>
 * Application code must explicitly invoke the <code>GC.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required. This is <em>particularly</em>
 * important on Windows95 and Windows98 where the operating system has a limited
 * number of device contexts available.
 * </p>
 *
 * <p>
 * Note: Only one of LEFT_TO_RIGHT and RIGHT_TO_LEFT may be specified.
 * </p>
 *
 * @see org.eclipse.swt.events.PaintEvent
 * @see <a href="http://www.eclipse.org/swt/snippets/#gc">GC snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: GraphicsExample, PaintExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class GC extends Resource {

	/**
	 * the handle to the OS device context
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public long handle;

	Drawable drawable;
	GCData data;

	static final int FOREGROUND = 1 << 0;
	static final int BACKGROUND = 1 << 1;
	static final int FONT = 1 << 2;
	static final int LINE_STYLE = 1 << 3;
	static final int LINE_WIDTH = 1 << 4;
	static final int LINE_CAP = 1 << 5;
	static final int LINE_JOIN = 1 << 6;
	static final int LINE_MITERLIMIT = 1 << 7;
	static final int FOREGROUND_TEXT = 1 << 8;
	static final int BACKGROUND_TEXT = 1 << 9;
	static final int BRUSH = 1 << 10;
	static final int PEN = 1 << 11;
	static final int NULL_BRUSH = 1 << 12;
	static final int NULL_PEN = 1 << 13;
	static final int DRAW_OFFSET = 1 << 14;

	static final int DRAW = FOREGROUND | LINE_STYLE | LINE_WIDTH | LINE_CAP | LINE_JOIN | LINE_MITERLIMIT | PEN | NULL_BRUSH | DRAW_OFFSET;
	static final int FILL = BACKGROUND | BRUSH | NULL_PEN;

	static final float[] LINE_DOT_ZERO = new float[]{3, 3};
	static final float[] LINE_DASH_ZERO = new float[]{18, 6};
	static final float[] LINE_DASHDOT_ZERO = new float[]{9, 6, 3, 6};
	static final float[] LINE_DASHDOTDOT_ZERO = new float[]{9, 3, 3, 3, 3, 3};

/**
 * Prevents uninitialized instances from being created outside the package.
 */
GC() {
}

/**
 * Constructs a new instance of this class which has been
 * configured to draw on the specified drawable. Sets the
 * foreground color, background color and font in the GC
 * to match those in the drawable.
 * <p>
 * You must dispose the graphics context when it is no longer required.
 * </p>
 * @param drawable the drawable to draw on
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
 *    <li>ERROR_NULL_ARGUMENT - if there is no current device</li>
 *    <li>ERROR_INVALID_ARGUMENT
 *          - if the drawable is an image that is not a bitmap or an icon
 *          - if the drawable is an image or printer that is already selected
 *            into another graphics context</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for GC creation</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS if not called from the thread that created the drawable</li>
 * </ul>
 * @see #dispose()
 */
public GC(Drawable drawable) {
	this(drawable, SWT.NONE);
}

/**
 * Constructs a new instance of this class which has been
 * configured to draw on the specified drawable. Sets the
 * foreground color, background color and font in the GC
 * to match those in the drawable.
 * <p>
 * You must dispose the graphics context when it is no longer required.
 * </p>
 *
 * @param drawable the drawable to draw on
 * @param style the style of GC to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
 *    <li>ERROR_NULL_ARGUMENT - if there is no current device</li>
 *    <li>ERROR_INVALID_ARGUMENT
 *          - if the drawable is an image that is not a bitmap or an icon
 *          - if the drawable is an image or printer that is already selected
 *            into another graphics context</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for GC creation</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS if not called from the thread that created the drawable</li>
 * </ul>
 *
 * @see #dispose()
 *
 * @since 2.1.2
 */
public GC(Drawable drawable, int style) {
	if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	GCData data = new GCData ();
	data.style = checkStyle(style);
	long hDC = drawable.internal_new_GC(data);
	Device device = data.device;
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = data.device = device;
	init (drawable, data, hDC);
	init();
}

static int checkStyle(int style) {
	if ((style & SWT.LEFT_TO_RIGHT) != 0) style &= ~SWT.RIGHT_TO_LEFT;
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

void checkGC(int mask) {
	int state = data.state;
	if ((state & mask) == mask) return;
	state = (state ^ mask) & mask;
	data.state |= mask;
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		long pen = data.gdipPen;
		float width = data.lineWidth;
		if ((state & FOREGROUND) != 0 || (pen == 0 && (state & (LINE_WIDTH | LINE_STYLE | LINE_MITERLIMIT | LINE_JOIN | LINE_CAP)) != 0)) {
			if (data.gdipFgBrush != 0) Gdip.SolidBrush_delete(data.gdipFgBrush);
			data.gdipFgBrush = 0;
			long brush;
			Pattern pattern = data.foregroundPattern;
			if (pattern != null) {
				if(data.alpha == 0xFF) {
					brush = pattern.handle;
				} else {
					brush = data.gdipFgPatternBrushAlpha != 0 ? Gdip.Brush_Clone(data.gdipFgPatternBrushAlpha) : createAlphaTextureBrush(pattern.handle, data.alpha);
					data.gdipFgPatternBrushAlpha = brush;
				}
				if ((data.style & SWT.MIRRORED) != 0) {
					switch (Gdip.Brush_GetType(brush)) {
						case Gdip.BrushTypeTextureFill:
							brush = Gdip.Brush_Clone(brush);
							if (brush == 0) SWT.error(SWT.ERROR_NO_HANDLES);
							Gdip.TextureBrush_ScaleTransform(brush, -1, 1, Gdip.MatrixOrderPrepend);
							data.gdipFgBrush = brush;
					}
				}
			} else {
				int foreground = data.foreground;
				int color = (data.alpha << 24) | ((foreground >> 16) & 0xFF) | (foreground & 0xFF00) | ((foreground & 0xFF) << 16);
				brush = Gdip.SolidBrush_new(color);
				if (brush == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				data.gdipFgBrush = brush;
			}
			if (pen != 0) {
				Gdip.Pen_SetBrush(pen, brush);
			} else {
				pen = data.gdipPen = Gdip.Pen_new(brush, width);
			}
		}
		if ((state & LINE_WIDTH) != 0) {
			Gdip.Pen_SetWidth(pen, width);
			switch (data.lineStyle) {
				case SWT.LINE_CUSTOM:
					state |= LINE_STYLE;
			}
		}
		if ((state & LINE_STYLE) != 0) {
			float[] dashes = null;
			float dashOffset = 0;
			int dashStyle = Gdip.DashStyleSolid;
			switch (data.lineStyle) {
				case SWT.LINE_SOLID: break;
				case SWT.LINE_DOT: dashStyle = Gdip.DashStyleDot; if (width == 0) dashes = LINE_DOT_ZERO; break;
				case SWT.LINE_DASH: dashStyle = Gdip.DashStyleDash; if (width == 0) dashes = LINE_DASH_ZERO; break;
				case SWT.LINE_DASHDOT: dashStyle = Gdip.DashStyleDashDot; if (width == 0) dashes = LINE_DASHDOT_ZERO; break;
				case SWT.LINE_DASHDOTDOT: dashStyle = Gdip.DashStyleDashDotDot; if (width == 0) dashes = LINE_DASHDOTDOT_ZERO; break;
				case SWT.LINE_CUSTOM: {
					if (data.lineDashes != null) {
						dashOffset = data.lineDashesOffset / Math.max (1, width);
						dashes = new float[data.lineDashes.length * 2];
						for (int i = 0; i < data.lineDashes.length; i++) {
							float dash = data.lineDashes[i] / Math.max (1, width);
							dashes[i] = dash;
							dashes[i + data.lineDashes.length] = dash;
						}
					}
				}
			}
			if (dashes != null) {
				Gdip.Pen_SetDashPattern(pen, dashes, dashes.length);
				Gdip.Pen_SetDashStyle(pen, Gdip.DashStyleCustom);
				Gdip.Pen_SetDashOffset(pen, dashOffset);
			} else {
				Gdip.Pen_SetDashStyle(pen, dashStyle);
			}
		}
		if ((state & LINE_MITERLIMIT) != 0) {
			Gdip.Pen_SetMiterLimit(pen, data.lineMiterLimit);
		}
		if ((state & LINE_JOIN) != 0) {
			int joinStyle = 0;
			switch (data.lineJoin) {
				case SWT.JOIN_MITER: joinStyle = Gdip.LineJoinMiter; break;
				case SWT.JOIN_BEVEL: joinStyle = Gdip.LineJoinBevel; break;
				case SWT.JOIN_ROUND: joinStyle = Gdip.LineJoinRound; break;
			}
			Gdip.Pen_SetLineJoin(pen, joinStyle);
		}
		if ((state & LINE_CAP) != 0) {
			int dashCap = Gdip.DashCapFlat, capStyle = 0;
			switch (data.lineCap) {
				case SWT.CAP_FLAT: capStyle = Gdip.LineCapFlat; break;
				case SWT.CAP_ROUND: capStyle = Gdip.LineCapRound; dashCap = Gdip.DashCapRound; break;
				case SWT.CAP_SQUARE: capStyle = Gdip.LineCapSquare; break;
			}
			Gdip.Pen_SetLineCap(pen, capStyle, capStyle, dashCap);
		}
		if ((state & BACKGROUND) != 0) {
			if (data.gdipBgBrush != 0) Gdip.SolidBrush_delete(data.gdipBgBrush);
			data.gdipBgBrush = 0;
			Pattern pattern = data.backgroundPattern;
			if (pattern != null) {
				if(data.alpha == 0xFF) {
					data.gdipBrush = pattern.handle;
				} else {
					long brush = data.gdipBgPatternBrushAlpha != 0 ? Gdip.Brush_Clone(data.gdipBgPatternBrushAlpha) : createAlphaTextureBrush(pattern.handle, data.alpha);
					data.gdipBrush = data.gdipBgBrush /*= data.gdipBgPatternBrushAlpha */ = brush;
				}
				if ((data.style & SWT.MIRRORED) != 0) {
					switch (Gdip.Brush_GetType(data.gdipBrush)) {
						case Gdip.BrushTypeTextureFill:
							long brush = Gdip.Brush_Clone(data.gdipBrush);
							if (brush == 0) SWT.error(SWT.ERROR_NO_HANDLES);
							Gdip.TextureBrush_ScaleTransform(brush, -1, 1, Gdip.MatrixOrderPrepend);
							data.gdipBrush = data.gdipBgBrush = brush;
					}
				}
			} else {
				int background = data.background;
				int color = (data.alpha << 24) | ((background >> 16) & 0xFF) | (background & 0xFF00) | ((background & 0xFF) << 16);
				long brush = Gdip.SolidBrush_new(color);
				if (brush == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				data.gdipBrush = data.gdipBgBrush = brush;
			}
		}
		if ((state & FONT) != 0) {
			Font font = data.font;
			OS.SelectObject(handle, font.handle);
			long[] hFont = new long[1];
			long gdipFont = createGdipFont(handle, font.handle, gdipGraphics, device.fontCollection, null, hFont);
			if (hFont[0] != 0) OS.SelectObject(handle, hFont[0]);
			if (data.hGDIFont != 0) OS.DeleteObject(data.hGDIFont);
			data.hGDIFont = hFont[0];
			if (data.gdipFont != 0) Gdip.Font_delete(data.gdipFont);
			data.gdipFont = gdipFont;
		}
		if ((state & DRAW_OFFSET) != 0) {
			data.gdipXOffset = data.gdipYOffset = 0;
			long matrix = Gdip.Matrix_new(1, 0, 0, 1, 0, 0);
			PointF point = new PointF();
			point.X = point.Y = 1;
			Gdip.Graphics_GetTransform(gdipGraphics, matrix);
			Gdip.Matrix_TransformVectors(matrix, point, 1);
			Gdip.Matrix_delete(matrix);
			float scaling = point.X;
			if (scaling < 0) scaling = -scaling;
			float penWidth = data.lineWidth * scaling;
			if (penWidth == 0 || (((int)penWidth) & 1) == 1) {
				data.gdipXOffset = 0.5f / scaling;
			}
			scaling = point.Y;
			if (scaling < 0) scaling = -scaling;
			penWidth = data.lineWidth * scaling;
			if (penWidth == 0 || (((int)penWidth) & 1) == 1) {
				data.gdipYOffset = 0.5f / scaling;
			}
		}
		return;
	}
	if ((state & (FOREGROUND | LINE_CAP | LINE_JOIN | LINE_STYLE | LINE_WIDTH)) != 0) {
		int color = data.foreground;
		int width = (int)data.lineWidth;
		int[] dashes = null;
		int lineStyle = OS.PS_SOLID;
		switch (data.lineStyle) {
			case SWT.LINE_SOLID: break;
			case SWT.LINE_DASH: lineStyle = OS.PS_DASH; break;
			case SWT.LINE_DOT: lineStyle = OS.PS_DOT; break;
			case SWT.LINE_DASHDOT: lineStyle = OS.PS_DASHDOT; break;
			case SWT.LINE_DASHDOTDOT: lineStyle = OS.PS_DASHDOTDOT; break;
			case SWT.LINE_CUSTOM: {
				if (data.lineDashes != null) {
					lineStyle = OS.PS_USERSTYLE;
					dashes = new int[data.lineDashes.length];
					for (int i = 0; i < dashes.length; i++) {
						dashes[i] = (int)data.lineDashes[i];
					}
				}
				break;
			}
		}
		if ((state & LINE_STYLE) != 0) {
			OS.SetBkMode(handle, data.lineStyle == SWT.LINE_SOLID ? OS.OPAQUE : OS.TRANSPARENT);
		}
		int joinStyle = 0;
		switch (data.lineJoin) {
			case SWT.JOIN_MITER: joinStyle = OS.PS_JOIN_MITER; break;
			case SWT.JOIN_ROUND: joinStyle = OS.PS_JOIN_ROUND; break;
			case SWT.JOIN_BEVEL: joinStyle = OS.PS_JOIN_BEVEL; break;
		}
		int capStyle = 0;
		switch (data.lineCap) {
			case SWT.CAP_ROUND: capStyle = OS.PS_ENDCAP_ROUND; break;
			case SWT.CAP_FLAT: capStyle = OS.PS_ENDCAP_FLAT; break;
			case SWT.CAP_SQUARE: capStyle = OS.PS_ENDCAP_SQUARE;break;
		}
		int style = lineStyle | joinStyle | capStyle;
		/*
		* Feature in Windows.  Windows does not honour line styles other then
		* PS_SOLID for pens wider than 1 pixel created with CreatePen().  The fix
		* is to use ExtCreatePen() instead.
		*/
		long newPen;
		if ((width == 0 && lineStyle != OS.PS_USERSTYLE) || style == 0) {
			newPen = OS.CreatePen(style & OS.PS_STYLE_MASK, width, color);
		} else {
			LOGBRUSH logBrush = new LOGBRUSH();
			logBrush.lbStyle = OS.BS_SOLID;
			logBrush.lbColor = color;
			/* Feature in Windows. PS_GEOMETRIC pens cannot have zero width. */
			newPen = OS.ExtCreatePen (style | OS.PS_GEOMETRIC, Math.max(1, width), logBrush, dashes != null ? dashes.length : 0, dashes);
		}
		OS.SelectObject(handle, newPen);
		data.state |= PEN;
		data.state &= ~NULL_PEN;
		if (data.hPen != 0) OS.DeleteObject(data.hPen);
		data.hPen = data.hOldPen = newPen;
	} else if ((state & PEN) != 0) {
		OS.SelectObject(handle, data.hOldPen);
		data.state &= ~NULL_PEN;
	} else if ((state & NULL_PEN) != 0) {
		data.hOldPen = OS.SelectObject(handle, OS.GetStockObject(OS.NULL_PEN));
		data.state &= ~PEN;
	}
	if ((state & BACKGROUND) != 0) {
		long newBrush = OS.CreateSolidBrush(data.background);
		OS.SelectObject(handle, newBrush);
		data.state |= BRUSH;
		data.state &= ~NULL_BRUSH;
		if (data.hBrush != 0) OS.DeleteObject(data.hBrush);
		data.hOldBrush = data.hBrush = newBrush;
	} else if ((state & BRUSH) != 0) {
		OS.SelectObject(handle, data.hOldBrush);
		data.state &= ~NULL_BRUSH;
	} else if ((state & NULL_BRUSH) != 0) {
		data.hOldBrush = OS.SelectObject(handle, OS.GetStockObject(OS.NULL_BRUSH));
		data.state &= ~BRUSH;
	}
	if ((state & BACKGROUND_TEXT) != 0) {
		OS.SetBkColor(handle, data.background);
	}
	if ((state & FOREGROUND_TEXT) != 0) {
		OS.SetTextColor(handle, data.foreground);
	}
	if ((state & FONT) != 0) {
		Font font = data.font;
		OS.SelectObject(handle, font.handle);
	}
}

/**
 * Copies a rectangular area of the receiver at the specified
 * position into the image, which must be of type <code>SWT.BITMAP</code>.
 *
 * @param image the image to copy into
 * @param x the x coordinate in the receiver of the area to be copied
 * @param y the y coordinate in the receiver of the area to be copied
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image is not a bitmap or has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void copyArea (Image image, int x, int y) {
	x = DPIUtil.autoScaleUp(drawable, x);
	y = DPIUtil.autoScaleUp(drawable, y);
	copyAreaInPixels(image, x, y);
}

void copyAreaInPixels(Image image, int x, int y) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.type != SWT.BITMAP || image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	/* Copy the bitmap area */
	Rectangle rect = image.getBoundsInPixels();
	long memHdc = OS.CreateCompatibleDC(handle);
	long hOldBitmap = OS.SelectObject(memHdc, image.handle);
	OS.BitBlt(memHdc, 0, 0, rect.width, rect.height, handle, x, y, OS.SRCCOPY);
	OS.SelectObject(memHdc, hOldBitmap);
	OS.DeleteDC(memHdc);
}

/**
 * Copies a rectangular area of the receiver at the source
 * position onto the receiver at the destination position.
 *
 * @param srcX the x coordinate in the receiver of the area to be copied
 * @param srcY the y coordinate in the receiver of the area to be copied
 * @param width the width of the area to copy
 * @param height the height of the area to copy
 * @param destX the x coordinate in the receiver of the area to copy to
 * @param destY the y coordinate in the receiver of the area to copy to
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void copyArea (int srcX, int srcY, int width, int height, int destX, int destY) {
	copyArea (srcX, srcY, width, height, destX, destY, true);
}

/**
 * Copies a rectangular area of the receiver at the source
 * position onto the receiver at the destination position.
 *
 * @param srcX the x coordinate in the receiver of the area to be copied
 * @param srcY the y coordinate in the receiver of the area to be copied
 * @param width the width of the area to copy
 * @param height the height of the area to copy
 * @param destX the x coordinate in the receiver of the area to copy to
 * @param destY the y coordinate in the receiver of the area to copy to
 * @param paint if <code>true</code> paint events will be generated for old and obscured areas
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
public void copyArea (int srcX, int srcY, int width, int height, int destX, int destY, boolean paint) {
	srcX = DPIUtil.autoScaleUp(drawable, srcX);
	srcY = DPIUtil.autoScaleUp(drawable, srcY);
	width = DPIUtil.autoScaleUp(drawable, width);
	height = DPIUtil.autoScaleUp(drawable, height);
	destX = DPIUtil.autoScaleUp(drawable, destX);
	destY = DPIUtil.autoScaleUp(drawable, destY);
	copyAreaInPixels(srcX, srcY, width, height, destX, destY, paint);
}

void copyAreaInPixels(int srcX, int srcY, int width, int height, int destX, int destY, boolean paint) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	long hwnd = data.hwnd;
	if (hwnd == 0) {
		OS.BitBlt(handle, destX, destY, width, height, handle, srcX, srcY, OS.SRCCOPY);
	} else {
		RECT lprcClip = null;
		long hrgn = OS.CreateRectRgn(0, 0, 0, 0);
		if (OS.GetClipRgn(handle, hrgn) == 1) {
			lprcClip = new RECT();
			OS.GetRgnBox(hrgn, lprcClip);
		}
		OS.DeleteObject(hrgn);
		RECT lprcScroll = new RECT();
		OS.SetRect(lprcScroll, srcX, srcY, srcX + width, srcY + height);
		int flags = paint ? OS.SW_INVALIDATE | OS.SW_ERASE : 0;
		OS.ScrollWindowEx(hwnd, destX - srcX, destY - srcY, lprcScroll, lprcClip, 0, null, flags);
	}
}
static long createGdipFont(long hDC, long hFont, long graphics, long fontCollection, long [] outFamily, long[] outFont) {
	long font = Gdip.Font_new(hDC, hFont);
	if (font == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	long family = 0;
	if (!Gdip.Font_IsAvailable(font)) {
		Gdip.Font_delete(font);
		LOGFONT logFont = new LOGFONT ();
		OS.GetObject(hFont, LOGFONT.sizeof, logFont);
		int size = Math.abs(logFont.lfHeight);
		int style = Gdip.FontStyleRegular;
		if (logFont.lfWeight == 700) style |= Gdip.FontStyleBold;
		if (logFont.lfItalic != 0) style |= Gdip.FontStyleItalic;
		char[] chars = logFont.lfFaceName;
		int index = 0;
		while (index < chars.length) {
			if (chars [index] == 0) break;
			index++;
		}
		String name = new String (chars, 0, index);
		if (name.equalsIgnoreCase("Courier")) { //$NON-NLS-1$
			name = "Courier New"; //$NON-NLS-1$
		}
		char[] buffer = new char[name.length() + 1];
		name.getChars(0, name.length(), buffer, 0);
		if (fontCollection != 0) {
			family = Gdip.FontFamily_new(buffer, fontCollection);
			if (!Gdip.FontFamily_IsAvailable(family)) {
				Gdip.FontFamily_delete(family);
				family = Gdip.FontFamily_new(buffer, 0);
				if (!Gdip.FontFamily_IsAvailable(family)) {
					Gdip.FontFamily_delete(family);
					family = 0;
				}
			}
		}
		if (family != 0) {
			font = Gdip.Font_new(family, size, style, Gdip.UnitPixel);
		} else {
			font = Gdip.Font_new(buffer, size, style, Gdip.UnitPixel, 0);
		}
		if (outFont != null && font != 0) {
			long hHeap = OS.GetProcessHeap();
			long pLogFont = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, LOGFONT.sizeof);
			Gdip.Font_GetLogFontW(font, graphics, pLogFont);
			outFont[0] = OS.CreateFontIndirect(pLogFont);
			OS.HeapFree(hHeap, 0, pLogFont);
		}
	}
	if (outFamily != null && font != 0) {
		if (family == 0) {
			family = Gdip.FontFamily_new();
			Gdip.Font_GetFamily(font, family);
		}
		outFamily [0] = family;
	} else {
		if (family != 0) Gdip.FontFamily_delete(family);
	}
	if (font == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	return font;
}

/**
 * Create a new brush with transparency from the image in {@link brush}.
 *
 * The returned brush has to be disposed by the caller.
 *
 * @param brush Brush with pattern
 * @return new brush with transparency
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the image in the brush is null</li>
 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
 * </ul>
 */
static long createAlphaTextureBrush(long brush, int alpha) {
	if (Gdip.Brush_GetType(brush) != Gdip.BrushTypeTextureFill) {
		return Gdip.Brush_Clone(brush);
	}
	long hatchImage = Gdip.TextureBrush_GetImage(brush);
	if (hatchImage == 0) SWT.error(SWT.ERROR_CANNOT_BE_ZERO);
	long transparentHatchImage = Gdip.Image_Clone(hatchImage);
	if (transparentHatchImage == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	long attrib = Gdip.ImageAttributes_new();
	Gdip.ImageAttributes_SetWrapMode(attrib, Gdip.WrapModeTile);
	float[] matrix = new float[]{
		1,0,0,0,0,
		0,1,0,0,0,
		0,0,1,0,0,
		0,0,0,alpha / (float)0xFF,0,
		0,0,0,0,1,
	};
	Gdip.ImageAttributes_SetColorMatrix(attrib, matrix, Gdip.ColorMatrixFlagsDefault, Gdip.ColorAdjustTypeBitmap);
	Rect rect = new Rect();
	rect.X = 0;
	rect.Y = 0;
	rect.Width = Gdip.Image_GetWidth(transparentHatchImage);
	rect.Height = Gdip.Image_GetHeight(transparentHatchImage);
	long transparentBrush = Gdip.TextureBrush_new(transparentHatchImage, rect, attrib);
	if (brush == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	Gdip.ImageAttributes_delete(attrib);
	Gdip.Image_delete(transparentHatchImage);
	return transparentBrush;
}

static void destroyGdipBrush(long brush) {
	int type = Gdip.Brush_GetType(brush);
	switch (type) {
		case Gdip.BrushTypeSolidColor:
			Gdip.SolidBrush_delete(brush);
			break;
		case Gdip.BrushTypeHatchFill:
			Gdip.HatchBrush_delete(brush);
			break;
		case Gdip.BrushTypeLinearGradient:
			Gdip.LinearGradientBrush_delete(brush);
			break;
		case Gdip.BrushTypeTextureFill:
			Gdip.TextureBrush_delete(brush);
			break;
	}
}

/**
 * Disposes of the operating system resources associated with
 * the graphics context. Applications must dispose of all GCs
 * which they allocate.
 *
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS if not called from the thread that created the drawable</li>
 * </ul>
 */
@Override
void destroy() {
	boolean gdip = data.gdipGraphics != 0;
	disposeGdip();
	if (gdip && (data.style & SWT.MIRRORED) != 0) {
		OS.SetLayout(handle, OS.GetLayout(handle) | OS.LAYOUT_RTL);
	}

	/* Select stock pen and brush objects and free resources */
	if (data.hPen != 0) {
		OS.SelectObject(handle, OS.GetStockObject(OS.NULL_PEN));
		OS.DeleteObject(data.hPen);
		data.hPen = 0;
	}
	if (data.hBrush != 0) {
		OS.SelectObject(handle, OS.GetStockObject(OS.NULL_BRUSH));
		OS.DeleteObject(data.hBrush);
		data.hBrush = 0;
	}

	/*
	* Put back the original bitmap into the device context.
	* This will ensure that we have not left a bitmap
	* selected in it when we delete the HDC.
	*/
	long hNullBitmap = data.hNullBitmap;
	if (hNullBitmap != 0) {
		OS.SelectObject(handle, hNullBitmap);
		data.hNullBitmap = 0;
	}
	Image image = data.image;
	if (image != null) image.memGC = null;

	/*
	* Dispose the HDC.
	*/
	if (drawable != null) drawable.internal_dispose_GC(handle, data);
	drawable = null;
	handle = 0;
	data.image = null;
	data.ps = null;
	data = null;
}

void disposeGdip() {
	if (data.gdipPen != 0) Gdip.Pen_delete(data.gdipPen);
	if (data.gdipBgBrush != 0) destroyGdipBrush(data.gdipBgBrush);
	if (data.gdipFgBrush != 0) destroyGdipBrush(data.gdipFgBrush);
	if (data.gdipFont != 0) Gdip.Font_delete(data.gdipFont);
	if (data.hGDIFont != 0) OS.DeleteObject(data.hGDIFont);
	if (data.gdipGraphics != 0) Gdip.Graphics_delete(data.gdipGraphics);
	if (data.gdipBgPatternBrushAlpha != 0) destroyGdipBrush(data.gdipBgPatternBrushAlpha);
	if (data.gdipFgPatternBrushAlpha != 0) destroyGdipBrush(data.gdipFgPatternBrushAlpha);
	data.gdipGraphics = data.gdipBrush = data.gdipBgBrush = data.gdipFgBrush =
		data.gdipFont = data.gdipPen = data.hGDIFont = data.gdipBgPatternBrushAlpha =
		data.gdipFgPatternBrushAlpha = 0;
}

/**
 * Draws the outline of a circular or elliptical arc
 * within the specified rectangular area.
 * <p>
 * The resulting arc begins at <code>startAngle</code> and extends
 * for <code>arcAngle</code> degrees, using the current color.
 * Angles are interpreted such that 0 degrees is at the 3 o'clock
 * position. A positive value indicates a counter-clockwise rotation
 * while a negative value indicates a clockwise rotation.
 * </p><p>
 * The center of the arc is the center of the rectangle whose origin
 * is (<code>x</code>, <code>y</code>) and whose size is specified by the
 * <code>width</code> and <code>height</code> arguments.
 * </p><p>
 * The resulting arc covers an area <code>width + 1</code> points wide
 * by <code>height + 1</code> points tall.
 * </p>
 *
 * @param x the x coordinate of the upper-left corner of the arc to be drawn
 * @param y the y coordinate of the upper-left corner of the arc to be drawn
 * @param width the width of the arc to be drawn
 * @param height the height of the arc to be drawn
 * @param startAngle the beginning angle
 * @param arcAngle the angular extent of the arc, relative to the start angle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawArc (int x, int y, int width, int height, int startAngle, int arcAngle) {
	x = DPIUtil.autoScaleUp(drawable, x);
	y = DPIUtil.autoScaleUp(drawable, y);
	width = DPIUtil.autoScaleUp(drawable, width);
	height = DPIUtil.autoScaleUp(drawable, height);
	drawArcInPixels(x, y, width, height, startAngle, arcAngle);
}

void drawArcInPixels (int x, int y, int width, int height, int startAngle, int arcAngle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(DRAW);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		Gdip.Graphics_TranslateTransform(gdipGraphics, data.gdipXOffset, data.gdipYOffset, Gdip.MatrixOrderPrepend);
		if (width == height) {
			Gdip.Graphics_DrawArc(gdipGraphics, data.gdipPen, x, y, width, height, -startAngle, -arcAngle);
		} else {
			long path = Gdip.GraphicsPath_new(Gdip.FillModeAlternate);
			if (path == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			long matrix = Gdip.Matrix_new(width, 0, 0, height, x, y);
			if (matrix == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			Gdip.GraphicsPath_AddArc(path, 0, 0, 1, 1, -startAngle, -arcAngle);
			Gdip.GraphicsPath_Transform(path, matrix);
			Gdip.Graphics_DrawPath(gdipGraphics, data.gdipPen, path);
			Gdip.Matrix_delete(matrix);
			Gdip.GraphicsPath_delete(path);
		}
		Gdip.Graphics_TranslateTransform(gdipGraphics, -data.gdipXOffset, -data.gdipYOffset, Gdip.MatrixOrderPrepend);
		return;
	}
	if ((data.style & SWT.MIRRORED) != 0) {
		if (data.lineWidth != 0 && data.lineWidth % 2 == 0) x--;
	}
	int x1, y1, x2, y2,tmp;
	boolean isNegative;
	if (arcAngle >= 360 || arcAngle <= -360) {
		x1 = x2 = x + width;
		y1 = y2 = y + height / 2;
	} else {
		isNegative = arcAngle < 0;

		arcAngle = arcAngle + startAngle;
		if (isNegative) {
			// swap angles
			tmp = startAngle;
			startAngle = arcAngle;
			arcAngle = tmp;
		}
		x1 = cos(startAngle, width) + x + width/2;
		y1 = -1 * sin(startAngle, height) + y + height/2;

		x2 = cos(arcAngle, width) + x + width/2;
		y2 = -1 * sin(arcAngle, height) + y + height/2;
	}
	OS.Arc(handle, x, y, x + width + 1, y + height + 1, x1, y1, x2, y2);
}

/**
 * Draws a rectangle, based on the specified arguments, which has
 * the appearance of the platform's <em>focus rectangle</em> if the
 * platform supports such a notion, and otherwise draws a simple
 * rectangle in the receiver's foreground color.
 *
 * @param x the x coordinate of the rectangle
 * @param y the y coordinate of the rectangle
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle(int, int, int, int)
 */
public void drawFocus (int x, int y, int width, int height) {
	x = DPIUtil.autoScaleUp (drawable, x);
	y = DPIUtil.autoScaleUp (drawable, y);
	width = DPIUtil.autoScaleUp (drawable, width);
	height = DPIUtil.autoScaleUp (drawable, height);
	drawFocusInPixels(x, y, width, height);
}

void drawFocusInPixels (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if ((data.uiState & OS.UISF_HIDEFOCUS) != 0) return;
	data.focusDrawn = true;
	long hdc = handle;
	int state = 0;
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		long clipRgn = 0;
		Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeNone);
		long rgn = Gdip.Region_new();
		if (rgn == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		Gdip.Graphics_GetClip(gdipGraphics, rgn);
		if (!Gdip.Region_IsInfinite(rgn, gdipGraphics)) {
			clipRgn = Gdip.Region_GetHRGN(rgn, gdipGraphics);
		}
		Gdip.Region_delete(rgn);
		Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeHalf);
		float[] lpXform = null;
		long matrix = Gdip.Matrix_new(1, 0, 0, 1, 0, 0);
		if (matrix == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		Gdip.Graphics_GetTransform(gdipGraphics, matrix);
		if (!Gdip.Matrix_IsIdentity(matrix)) {
			lpXform = new float[6];
			Gdip.Matrix_GetElements(matrix, lpXform);
		}
		Gdip.Matrix_delete(matrix);
		hdc = Gdip.Graphics_GetHDC(gdipGraphics);
		state = OS.SaveDC(hdc);
		if (lpXform != null) {
			OS.SetGraphicsMode(hdc, OS.GM_ADVANCED);
			OS.SetWorldTransform(hdc, lpXform);
		}
		if (clipRgn != 0) {
			OS.SelectClipRgn(hdc, clipRgn);
			OS.DeleteObject(clipRgn);
		}
	}
	OS.SetBkColor(hdc, 0xFFFFFF);
	OS.SetTextColor(hdc, 0x000000);
	RECT rect = new RECT();
	OS.SetRect(rect, x, y, x + width, y + height);
	OS.DrawFocusRect(hdc, rect);
	if (gdipGraphics != 0) {
		OS.RestoreDC(hdc, state);
		Gdip.Graphics_ReleaseHDC(gdipGraphics, hdc);
	} else {
		data.state &= ~(BACKGROUND_TEXT | FOREGROUND_TEXT);
	}
}

/**
 * Draws the given image in the receiver at the specified
 * coordinates.
 *
 * @param image the image to draw
 * @param x the x coordinate of where to draw
 * @param y the y coordinate of where to draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the given coordinates are outside the bounds of the image</li></ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
 * </ul>
 */
public void drawImage (Image image, int x, int y) {
	x = DPIUtil.autoScaleUp(drawable, x);
	y = DPIUtil.autoScaleUp(drawable, y);
	drawImageInPixels(image, x, y);
}

void drawImageInPixels(Image image, int x, int y) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (image == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	drawImage(image, 0, 0, -1, -1, x, y, -1, -1, true);
}

/**
 * Copies a rectangular area from the source image into a (potentially
 * different sized) rectangular area in the receiver. If the source
 * and destination areas are of differing sizes, then the source
 * area will be stretched or shrunk to fit the destination area
 * as it is copied. The copy fails if any part of the source rectangle
 * lies outside the bounds of the source image, or if any of the width
 * or height arguments are negative.
 *
 * @param image the source image
 * @param srcX the x coordinate in the source image to copy from
 * @param srcY the y coordinate in the source image to copy from
 * @param srcWidth the width in points to copy from the source
 * @param srcHeight the height in points to copy from the source
 * @param destX the x coordinate in the destination to copy to
 * @param destY the y coordinate in the destination to copy to
 * @param destWidth the width in points of the destination rectangle
 * @param destHeight the height in points of the destination rectangle
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the width or height arguments are negative.
 *    <li>ERROR_INVALID_ARGUMENT - if the source rectangle is not contained within the bounds of the source image</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
 * </ul>
 */
public void drawImage (Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (srcWidth == 0 || srcHeight == 0 || destWidth == 0 || destHeight == 0) return;
	if (srcX < 0 || srcY < 0 || srcWidth < 0 || srcHeight < 0 || destWidth < 0 || destHeight < 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (image == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

	Rectangle src = DPIUtil.autoScaleUp(drawable, new Rectangle(srcX, srcY, srcWidth, srcHeight));
	Rectangle dest = DPIUtil.autoScaleUp(drawable, new Rectangle(destX, destY, destWidth, destHeight));
	int deviceZoom = DPIUtil.getDeviceZoom();
	if (deviceZoom != 100) {
		/*
		 * This is a HACK! Due to rounding errors at fractional scale factors,
		 * the coordinates may be slightly off. The workaround is to restrict
		 * coordinates to the allowed bounds.
		 */
		Rectangle b = image.getBoundsInPixels();
		int errX = src.x + src.width - b.width;
		int errY = src.y + src.height - b.height;
		if (errX != 0 || errY != 0) {
			if (errX <= deviceZoom / 100 && errY <= deviceZoom / 100) {
				src.intersect(b);
			} else {
				SWT.error (SWT.ERROR_INVALID_ARGUMENT);
			}
		}
	}
	drawImage(image, src.x, src.y, src.width, src.height, dest.x, dest.y, dest.width, dest.height, false);
}

void drawImage(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
	/* Refresh Image as per zoom level, if required. */
	srcImage.refreshImageForZoom ();

	if (data.gdipGraphics != 0) {
		//TODO - cache bitmap
		long [] gdipImage = srcImage.createGdipImage();
		long img = gdipImage[0];
		int imgWidth = Gdip.Image_GetWidth(img);
		int imgHeight = Gdip.Image_GetHeight(img);
		if (simple) {
			srcWidth = destWidth = imgWidth;
			srcHeight = destHeight = imgHeight;
		} else {
			if (srcX + srcWidth > imgWidth || srcY + srcHeight > imgHeight) {
				SWT.error (SWT.ERROR_INVALID_ARGUMENT);
			}
			simple = srcX == 0 && srcY == 0 &&
				srcWidth == destWidth && destWidth == imgWidth &&
				srcHeight == destHeight && destHeight == imgHeight;
		}
		Rect rect = new Rect();
		rect.X = destX;
		rect.Y = destY;
		rect.Width = destWidth;
		rect.Height = destHeight;
		/*
		* Note that if the wrap mode is not WrapModeTileFlipXY, the scaled image
		* is translucent around the borders.
		*/
		long attrib = Gdip.ImageAttributes_new();
		Gdip.ImageAttributes_SetWrapMode(attrib, Gdip.WrapModeTileFlipXY);
		if (data.alpha != 0xFF) {
			float[] matrix = new float[]{
				1,0,0,0,0,
				0,1,0,0,0,
				0,0,1,0,0,
				0,0,0,data.alpha / (float)0xFF,0,
				0,0,0,0,1,
			};
			Gdip.ImageAttributes_SetColorMatrix(attrib, matrix, Gdip.ColorMatrixFlagsDefault, Gdip.ColorAdjustTypeBitmap);
		}
		int gstate = 0;
		if ((data.style & SWT.MIRRORED) != 0) {
			gstate = Gdip.Graphics_Save(data.gdipGraphics);
			Gdip.Graphics_ScaleTransform(data.gdipGraphics, -1, 1, Gdip.MatrixOrderPrepend);
			Gdip.Graphics_TranslateTransform(data.gdipGraphics, - 2 * destX - destWidth, 0, Gdip.MatrixOrderPrepend);
		}
		Gdip.Graphics_DrawImage(data.gdipGraphics, img, rect, srcX, srcY, srcWidth, srcHeight, Gdip.UnitPixel, attrib, 0, 0);
		if ((data.style & SWT.MIRRORED) != 0) {
			Gdip.Graphics_Restore(data.gdipGraphics, gstate);
		}
		Gdip.ImageAttributes_delete(attrib);
		Gdip.Bitmap_delete(img);
		if (gdipImage[1] != 0) {
			long hHeap = OS.GetProcessHeap ();
			OS.HeapFree(hHeap, 0, gdipImage[1]);
		}
		return;
	}
	switch (srcImage.type) {
		case SWT.BITMAP:
			drawBitmap(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple);
			break;
		case SWT.ICON:
			drawIcon(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple);
			break;
	}
}

void drawIcon(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
	int technology = OS.GetDeviceCaps(handle, OS.TECHNOLOGY);

	boolean drawIcon = true;
	int flags = OS.DI_NORMAL;
	int offsetX = 0, offsetY = 0;

	if ((OS.GetLayout(handle) & OS.LAYOUT_RTL) != 0) {
		flags |= OS.DI_NOMIRROR;
		/*
		* Bug in Windows.  For some reason, DrawIconEx() does not take
		* into account the window origin when the DI_NOMIRROR and
		* LAYOUT_RTL are set.  The fix is to set the window origin to
		* (0, 0) and offset the drawing ourselves.
		*/
		POINT pt = new POINT();
		OS.GetWindowOrgEx(handle, pt);
		offsetX = pt.x;
		offsetY = pt.y;
	}

	/* Simple case: no stretching, entire icon */
	if (simple && technology != OS.DT_RASPRINTER && drawIcon) {
		if (offsetX != 0 || offsetY != 0) OS.SetWindowOrgEx(handle, 0, 0, null);
		OS.DrawIconEx(handle, destX - offsetX, destY - offsetY, srcImage.handle, 0, 0, 0, 0, flags);
		if (offsetX != 0 || offsetY != 0) OS.SetWindowOrgEx(handle, offsetX, offsetY, null);
		return;
	}

	/* Get the icon info */
	ICONINFO srcIconInfo = new ICONINFO();
	OS.GetIconInfo(srcImage.handle, srcIconInfo);

	/* Get the icon width and height */
	long hBitmap = srcIconInfo.hbmColor;
	if (hBitmap == 0) hBitmap = srcIconInfo.hbmMask;
	BITMAP bm = new BITMAP();
	OS.GetObject(hBitmap, BITMAP.sizeof, bm);
	int iconWidth = bm.bmWidth, iconHeight = bm.bmHeight;
	if (hBitmap == srcIconInfo.hbmMask) iconHeight /= 2;

	if (simple) {
		srcWidth = destWidth = iconWidth;
		srcHeight = destHeight = iconHeight;
	}

	/* Draw the icon */
	boolean failed = srcX + srcWidth > iconWidth || srcY + srcHeight > iconHeight;
	if (!failed) {
		simple = srcX == 0 && srcY == 0 &&
			srcWidth == destWidth && srcHeight == destHeight &&
			srcWidth == iconWidth && srcHeight == iconHeight;
		if (!drawIcon) {
			drawBitmapMask(srcImage, srcIconInfo.hbmColor, srcIconInfo.hbmMask, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, iconWidth, iconHeight, false);
		} else if (simple && technology != OS.DT_RASPRINTER) {
			/* Simple case: no stretching, entire icon */
			if (offsetX != 0 || offsetY != 0) OS.SetWindowOrgEx(handle, 0, 0, null);
			OS.DrawIconEx(handle, destX - offsetX, destY - offsetY, srcImage.handle, 0, 0, 0, 0, flags);
			if (offsetX != 0 || offsetY != 0) OS.SetWindowOrgEx(handle, offsetX, offsetY, null);
		} else {
			/* Create the icon info and HDC's */
			ICONINFO newIconInfo = new ICONINFO();
			newIconInfo.fIcon = true;
			long srcHdc = OS.CreateCompatibleDC(handle);
			long dstHdc = OS.CreateCompatibleDC(handle);

			/* Blt the color bitmap */
			int srcColorY = srcY;
			long srcColor = srcIconInfo.hbmColor;
			if (srcColor == 0) {
				srcColor = srcIconInfo.hbmMask;
				srcColorY += iconHeight;
			}
			long oldSrcBitmap = OS.SelectObject(srcHdc, srcColor);
			newIconInfo.hbmColor = OS.CreateCompatibleBitmap(srcHdc, destWidth, destHeight);
			if (newIconInfo.hbmColor == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			long oldDestBitmap = OS.SelectObject(dstHdc, newIconInfo.hbmColor);
			boolean stretch = !simple && (srcWidth != destWidth || srcHeight != destHeight);
			if (stretch) {
				OS.SetStretchBltMode(dstHdc, OS.COLORONCOLOR);
				OS.StretchBlt(dstHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcColorY, srcWidth, srcHeight, OS.SRCCOPY);
			} else {
				OS.BitBlt(dstHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcColorY, OS.SRCCOPY);
			}

			/* Blt the mask bitmap */
			OS.SelectObject(srcHdc, srcIconInfo.hbmMask);
			newIconInfo.hbmMask = OS.CreateBitmap(destWidth, destHeight, 1, 1, null);
			if (newIconInfo.hbmMask == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			OS.SelectObject(dstHdc, newIconInfo.hbmMask);
			if (stretch) {
				OS.StretchBlt(dstHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, OS.SRCCOPY);
			} else {
				OS.BitBlt(dstHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY, OS.SRCCOPY);
			}

			if (technology == OS.DT_RASPRINTER) {
				OS.SelectObject(srcHdc, newIconInfo.hbmColor);
				OS.SelectObject(dstHdc, newIconInfo.hbmMask);
				drawBitmapTransparentByClipping(srcHdc, dstHdc, 0, 0, destWidth, destHeight, destX, destY, destWidth, destHeight, true, destWidth, destHeight);
				OS.SelectObject(srcHdc, oldSrcBitmap);
				OS.SelectObject(dstHdc, oldDestBitmap);
			} else {
				OS.SelectObject(srcHdc, oldSrcBitmap);
				OS.SelectObject(dstHdc, oldDestBitmap);
				long hIcon = OS.CreateIconIndirect(newIconInfo);
				if (hIcon == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				if (offsetX != 0 || offsetY != 0) OS.SetWindowOrgEx(handle, 0, 0, null);
				OS.DrawIconEx(handle, destX - offsetX, destY - offsetY, hIcon, destWidth, destHeight, 0, 0, flags);
				if (offsetX != 0 || offsetY != 0) OS.SetWindowOrgEx(handle, offsetX, offsetY, null);
				OS.DestroyIcon(hIcon);
			}

			/* Destroy the new icon src and mask and hdc's*/
			OS.DeleteObject(newIconInfo.hbmMask);
			OS.DeleteObject(newIconInfo.hbmColor);
			OS.DeleteDC(dstHdc);
			OS.DeleteDC(srcHdc);
		}
	}

	/* Free icon info */
	OS.DeleteObject(srcIconInfo.hbmMask);
	if (srcIconInfo.hbmColor != 0) {
		OS.DeleteObject(srcIconInfo.hbmColor);
	}

	if (failed) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
}

void drawBitmap(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
	BITMAP bm = new BITMAP();
	OS.GetObject(srcImage.handle, BITMAP.sizeof, bm);
	int imgWidth = bm.bmWidth;
	int imgHeight = bm.bmHeight;
	if (simple) {
		srcWidth = destWidth = imgWidth;
		srcHeight = destHeight = imgHeight;
	} else {
		if (srcX + srcWidth > imgWidth || srcY + srcHeight > imgHeight) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		simple = srcX == 0 && srcY == 0 &&
			srcWidth == destWidth && destWidth == imgWidth &&
			srcHeight == destHeight && destHeight == imgHeight;
	}
	boolean mustRestore = false;
	GC memGC = srcImage.memGC;
	if (memGC != null && !memGC.isDisposed()) {
		memGC.flush();
		mustRestore = true;
		GCData data = memGC.data;
		if (data.hNullBitmap != 0) {
			OS.SelectObject(memGC.handle, data.hNullBitmap);
			data.hNullBitmap = 0;
		}
	}
	boolean isDib = bm.bmBits != 0;
	int depth = bm.bmPlanes * bm.bmBitsPixel;
	if (isDib && depth == 32) {
		drawBitmapAlpha(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple);
	} else if (srcImage.transparentPixel != -1) {
		drawBitmapTransparent(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, bm, imgWidth, imgHeight);
	} else {
		drawBitmapColor(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple);
	}
	if (mustRestore) {
		long hOldBitmap = OS.SelectObject(memGC.handle, srcImage.handle);
		memGC.data.hNullBitmap = hOldBitmap;
	}
}

void drawBitmapAlpha(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
	boolean alphaBlendSupport = true;
	boolean isPrinter = OS.GetDeviceCaps(handle, OS.TECHNOLOGY) == OS.DT_RASPRINTER;
	int sourceAlpha = -1;
	if (isPrinter) {
		int caps = OS.GetDeviceCaps(handle, OS.SHADEBLENDCAPS);
		if (caps != 0) {
			long srcHdc = OS.CreateCompatibleDC(handle);
			long oldSrcBitmap = OS.SelectObject(srcHdc, srcImage.handle);
			long memDib = Image.createDIB(srcWidth, srcHeight, 32);
			if (memDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			long memHdc = OS.CreateCompatibleDC(handle);
			long oldMemBitmap = OS.SelectObject(memHdc, memDib);
			BITMAP dibBM = new BITMAP();
			OS.GetObject(memDib, BITMAP.sizeof, dibBM);
			OS.BitBlt(memHdc, 0, 0, srcWidth, srcHeight, srcHdc, srcX, srcY, OS.SRCCOPY);
			byte[] srcData = new byte[dibBM.bmWidthBytes * dibBM.bmHeight];
			OS.MoveMemory(srcData, dibBM.bmBits, srcData.length);
			int size = srcData.length;
			sourceAlpha = srcData[3] & 0xFF;
			for (int sp = 7; sp < size; sp += 4) {
				int currentAlpha = srcData[sp] & 0xFF;
				if (sourceAlpha != currentAlpha) {
					sourceAlpha = -1;
					break;
				}
			}
			OS.SelectObject(memHdc, oldMemBitmap);
			OS.DeleteDC(memHdc);
			OS.DeleteObject(memDib);
			OS.SelectObject(srcHdc, oldSrcBitmap);
			OS.DeleteDC(srcHdc);
			if (sourceAlpha != -1) {
				if (sourceAlpha == 0) return;
				if (sourceAlpha == 255) {
					drawBitmapColor(srcImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple);
					return;
				}
				alphaBlendSupport = (caps & OS.SB_CONST_ALPHA) != 0;
			}
			else {
				alphaBlendSupport = (caps & OS.SB_PIXEL_ALPHA) != 0;
			}
		}
	}
	if (alphaBlendSupport) {
		BLENDFUNCTION blend = new BLENDFUNCTION();
		blend.BlendOp = OS.AC_SRC_OVER;
		long srcHdc = OS.CreateCompatibleDC(handle);
		long oldSrcBitmap = OS.SelectObject(srcHdc, srcImage.handle);
		blend.SourceConstantAlpha = (byte)sourceAlpha;
		blend.AlphaFormat = OS.AC_SRC_ALPHA;
		OS.AlphaBlend(handle, destX, destY, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, blend);
		OS.SelectObject(srcHdc, oldSrcBitmap);
		OS.DeleteDC(srcHdc);
		return;
	}

	/* Check clipping */
	Rectangle rect = getClippingInPixels();
	rect = rect.intersection(new Rectangle(destX, destY, destWidth, destHeight));
	if (rect.isEmpty()) return;

	/*
	* Optimization.  Recalculate src and dest rectangles so that
	* only the clipping area is drawn.
	*/
	int sx1 = srcX + (((rect.x - destX) * srcWidth) / destWidth);
	int sx2 = srcX + ((((rect.x + rect.width) - destX) * srcWidth) / destWidth);
	int sy1 = srcY + (((rect.y - destY) * srcHeight) / destHeight);
	int sy2 = srcY + ((((rect.y + rect.height) - destY) * srcHeight) / destHeight);
	destX = rect.x;
	destY = rect.y;
	destWidth = rect.width;
	destHeight = rect.height;
	srcX = sx1;
	srcY = sy1;
	srcWidth = Math.max(1, sx2 - sx1);
	srcHeight = Math.max(1, sy2 - sy1);

	/* Create resources */
	long srcHdc = OS.CreateCompatibleDC(handle);
	long oldSrcBitmap = OS.SelectObject(srcHdc, srcImage.handle);
	long memHdc = OS.CreateCompatibleDC(handle);
	long memDib = Image.createDIB(Math.max(srcWidth, destWidth), Math.max(srcHeight, destHeight), 32);
	if (memDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	long oldMemBitmap = OS.SelectObject(memHdc, memDib);

	BITMAP dibBM = new BITMAP();
	OS.GetObject(memDib, BITMAP.sizeof, dibBM);
	int sizeInBytes = dibBM.bmWidthBytes * dibBM.bmHeight;

	/* Get the background pixels */
	OS.BitBlt(memHdc, 0, 0, destWidth, destHeight, handle, destX, destY, OS.SRCCOPY);
	byte[] destData = new byte[sizeInBytes];
	OS.MoveMemory(destData, dibBM.bmBits, sizeInBytes);

	/* Get the foreground pixels */
	OS.BitBlt(memHdc, 0, 0, srcWidth, srcHeight, srcHdc, srcX, srcY, OS.SRCCOPY);
	byte[] srcData = new byte[sizeInBytes];
	OS.MoveMemory(srcData, dibBM.bmBits, sizeInBytes);

	/*
	* When drawing to a printer, StretchBlt does not correctly stretch if
	* the source and destination HDCs are the same.  The workaround is to
	* stretch to a temporary HDC and blit back into the original HDC.
	*/
	if (isPrinter) {
		long tempHdc = OS.CreateCompatibleDC(handle);
		long tempDib = Image.createDIB(destWidth, destHeight, 32);
		if (tempDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		long oldTempBitmap = OS.SelectObject(tempHdc, tempDib);
		if (!simple && (srcWidth != destWidth || srcHeight != destHeight)) {
			OS.SetStretchBltMode(memHdc, OS.COLORONCOLOR);
			OS.StretchBlt(tempHdc, 0, 0, destWidth, destHeight, memHdc, 0, 0, srcWidth, srcHeight, OS.SRCCOPY);
		} else {
			OS.BitBlt(tempHdc, 0, 0, destWidth, destHeight, memHdc, 0, 0, OS.SRCCOPY);
		}
		OS.BitBlt(memHdc, 0, 0, destWidth, destHeight, tempHdc, 0, 0, OS.SRCCOPY);
		OS.SelectObject(tempHdc, oldTempBitmap);
		OS.DeleteObject(tempDib);
		OS.DeleteDC(tempHdc);
	} else {
		if (!simple && (srcWidth != destWidth || srcHeight != destHeight)) {
			OS.SetStretchBltMode(memHdc, OS.COLORONCOLOR);
			OS.StretchBlt(memHdc, 0, 0, destWidth, destHeight, memHdc, 0, 0, srcWidth, srcHeight, OS.SRCCOPY);
		} else {
			OS.BitBlt(memHdc, 0, 0, destWidth, destHeight, memHdc, 0, 0, OS.SRCCOPY);
		}
	}
	OS.MoveMemory(srcData, dibBM.bmBits, sizeInBytes);

	/* Compose the pixels */
	final int dpinc = dibBM.bmWidthBytes - destWidth * 4;
	int dp = 0;
	for (int y = 0; y < destHeight; ++y) {
		for (int x = 0; x < destWidth; ++x) {
			int alpha = srcData[dp + 3] & 0xFF;
			destData[dp    ] += (srcData[dp    ] & 0xFF) - (destData[dp    ] & 0xFF) * alpha / 255;
			destData[dp + 1] += (srcData[dp + 1] & 0xFF) - (destData[dp + 1] & 0xFF) * alpha / 255;
			destData[dp + 2] += (srcData[dp + 2] & 0xFF) - (destData[dp + 2] & 0xFF) * alpha / 255;
			dp += 4;
		}
		dp += dpinc;
	}

	/* Draw the composed pixels */
	OS.MoveMemory(dibBM.bmBits, destData, sizeInBytes);
	OS.BitBlt(handle, destX, destY, destWidth, destHeight, memHdc, 0, 0, OS.SRCCOPY);

	/* Free resources */
	OS.SelectObject(memHdc, oldMemBitmap);
	OS.DeleteDC(memHdc);
	OS.DeleteObject(memDib);
	OS.SelectObject(srcHdc, oldSrcBitmap);
	OS.DeleteDC(srcHdc);
}

void drawBitmapTransparentByClipping(long srcHdc, long maskHdc, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight) {
	/* Create a clipping region from the mask */
	long rgn = OS.CreateRectRgn(0, 0, 0, 0);
	for (int y=0; y<imgHeight; y++) {
		for (int x=0; x<imgWidth; x++) {
			if (OS.GetPixel(maskHdc, x, y) == 0) {
				long tempRgn = OS.CreateRectRgn(x, y, x+1, y+1);
				OS.CombineRgn(rgn, rgn, tempRgn, OS.RGN_OR);
				OS.DeleteObject(tempRgn);
			}
		}
	}
	/* Stretch the clipping mask if needed */
	if (destWidth != srcWidth || destHeight != srcHeight) {
		int nBytes = OS.GetRegionData (rgn, 0, null);
		int[] lpRgnData = new int[nBytes / 4];
		OS.GetRegionData (rgn, nBytes, lpRgnData);
		float[] lpXform = new float[] {(float)destWidth/srcWidth, 0, 0, (float)destHeight/srcHeight, 0, 0};
		long tmpRgn = OS.ExtCreateRegion(lpXform, nBytes, lpRgnData);
		OS.DeleteObject(rgn);
		rgn = tmpRgn;
	}
	OS.OffsetRgn(rgn, destX, destY);
	long clip = OS.CreateRectRgn(0, 0, 0, 0);
	int result = OS.GetClipRgn(handle, clip);
	if (result == 1) OS.CombineRgn(rgn, rgn, clip, OS.RGN_AND);
	OS.SelectClipRgn(handle, rgn);
	int dwRop = OS.GetROP2(handle) == OS.R2_XORPEN ? OS.SRCINVERT : OS.SRCCOPY;
	if (!simple && (srcWidth != destWidth || srcHeight != destHeight)) {
		int mode = OS.SetStretchBltMode(handle, OS.COLORONCOLOR);
		OS.StretchBlt(handle, destX, destY, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, dwRop);
		OS.SetStretchBltMode(handle, mode);
	} else {
		OS.BitBlt(handle, destX, destY, destWidth, destHeight, srcHdc, srcX, srcY, dwRop);
	}
	OS.SelectClipRgn(handle, result == 1 ? clip : 0);
	OS.DeleteObject(clip);
	OS.DeleteObject(rgn);
}

void drawBitmapMask(Image srcImage, long srcColor, long srcMask, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight, boolean offscreen) {
	int srcColorY = srcY;
	if (srcColor == 0) {
		srcColor = srcMask;
		srcColorY += imgHeight;
	}
	long srcHdc = OS.CreateCompatibleDC(handle);
	long oldSrcBitmap = OS.SelectObject(srcHdc, srcColor);
	long destHdc = handle;
	int x = destX, y = destY;
	long tempHdc = 0, tempBitmap = 0, oldTempBitmap = 0;
	int oldBkColor = 0, oldTextColor = 0;
	if (offscreen) {
		tempHdc = OS.CreateCompatibleDC(handle);
		tempBitmap = OS.CreateCompatibleBitmap(handle, destWidth, destHeight);
		oldTempBitmap = OS.SelectObject(tempHdc, tempBitmap);
		OS.BitBlt(tempHdc, 0, 0, destWidth, destHeight, handle, destX, destY, OS.SRCCOPY);
		destHdc = tempHdc;
		x = y = 0;
	} else {
		oldBkColor = OS.SetBkColor(handle, 0xFFFFFF);
		oldTextColor = OS.SetTextColor(handle, 0);
	}
	if (!simple && (srcWidth != destWidth || srcHeight != destHeight)) {
		int mode = OS.SetStretchBltMode(handle, OS.COLORONCOLOR);
		OS.StretchBlt(destHdc, x, y, destWidth, destHeight, srcHdc, srcX, srcColorY, srcWidth, srcHeight, OS.SRCINVERT);
		OS.SelectObject(srcHdc, srcMask);
		OS.StretchBlt(destHdc, x, y, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, OS.SRCAND);
		OS.SelectObject(srcHdc, srcColor);
		OS.StretchBlt(destHdc, x, y, destWidth, destHeight, srcHdc, srcX, srcColorY, srcWidth, srcHeight, OS.SRCINVERT);
		OS.SetStretchBltMode(handle, mode);
	} else {
		OS.BitBlt(destHdc, x, y, destWidth, destHeight, srcHdc, srcX, srcColorY, OS.SRCINVERT);
		OS.SetTextColor(destHdc, 0);
		OS.SelectObject(srcHdc, srcMask);
		OS.BitBlt(destHdc, x, y, destWidth, destHeight, srcHdc, srcX, srcY, OS.SRCAND);
		OS.SelectObject(srcHdc, srcColor);
		OS.BitBlt(destHdc, x, y, destWidth, destHeight, srcHdc, srcX, srcColorY, OS.SRCINVERT);
	}
	if (offscreen) {
		OS.BitBlt(handle, destX, destY, destWidth, destHeight, tempHdc, 0, 0, OS.SRCCOPY);
		OS.SelectObject(tempHdc, oldTempBitmap);
		OS.DeleteDC(tempHdc);
		OS.DeleteObject(tempBitmap);
	} else {
		OS.SetBkColor(handle, oldBkColor);
		OS.SetTextColor(handle, oldTextColor);
	}
	OS.SelectObject(srcHdc, oldSrcBitmap);
	OS.DeleteDC(srcHdc);
}

void drawBitmapTransparent(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, BITMAP bm, int imgWidth, int imgHeight) {

	/* Find the RGB values for the transparent pixel. */
	boolean isDib = bm.bmBits != 0;
	long hBitmap = srcImage.handle;
	long srcHdc = OS.CreateCompatibleDC(handle);
	long oldSrcBitmap = OS.SelectObject(srcHdc, hBitmap);
	byte[] originalColors = null;
	int transparentColor = srcImage.transparentColor;
	if (transparentColor == -1) {
		int transBlue = 0, transGreen = 0, transRed = 0;
		boolean fixPalette = false;
		if (bm.bmBitsPixel <= 8) {
			if (isDib) {
				int maxColors = 1 << bm.bmBitsPixel;
				byte[] oldColors = new byte[maxColors * 4];
				OS.GetDIBColorTable(srcHdc, 0, maxColors, oldColors);
				int offset = srcImage.transparentPixel * 4;
				for (int i = 0; i < oldColors.length; i += 4) {
					if (i != offset) {
						if (oldColors[offset] == oldColors[i] && oldColors[offset+1] == oldColors[i+1] && oldColors[offset+2] == oldColors[i+2]) {
							fixPalette = true;
							break;
						}
					}
				}
				if (fixPalette) {
					byte[] newColors = new byte[oldColors.length];
					transRed = transGreen = transBlue = 0xff;
					newColors[offset] = (byte)transBlue;
					newColors[offset+1] = (byte)transGreen;
					newColors[offset+2] = (byte)transRed;
					OS.SetDIBColorTable(srcHdc, 0, maxColors, newColors);
					originalColors = oldColors;
				} else {
					transBlue = oldColors[offset] & 0xFF;
					transGreen = oldColors[offset+1] & 0xFF;
					transRed = oldColors[offset+2] & 0xFF;
				}
			} else {
				/* Palette-based bitmap */
				int numColors = 1 << bm.bmBitsPixel;
				/* Set the few fields necessary to get the RGB data out */
				BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
				bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
				bmiHeader.biPlanes = bm.bmPlanes;
				bmiHeader.biBitCount = bm.bmBitsPixel;
				byte[] bmi = new byte[BITMAPINFOHEADER.sizeof + numColors * 4];
				OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
				OS.GetDIBits(srcHdc, srcImage.handle, 0, 0, null, bmi, OS.DIB_RGB_COLORS);
				int offset = BITMAPINFOHEADER.sizeof + 4 * srcImage.transparentPixel;
				transRed = bmi[offset + 2] & 0xFF;
				transGreen = bmi[offset + 1] & 0xFF;
				transBlue = bmi[offset] & 0xFF;
			}
		} else {
			/* Direct color image */
			int pixel = srcImage.transparentPixel;
			switch (bm.bmBitsPixel) {
				case 16:
					transBlue = (pixel & 0x1F) << 3;
					transGreen = (pixel & 0x3E0) >> 2;
					transRed = (pixel & 0x7C00) >> 7;
					break;
				case 24:
					transBlue = (pixel & 0xFF0000) >> 16;
					transGreen = (pixel & 0xFF00) >> 8;
					transRed = pixel & 0xFF;
					break;
				case 32:
					transBlue = (pixel & 0xFF000000) >>> 24;
					transGreen = (pixel & 0xFF0000) >> 16;
					transRed = (pixel & 0xFF00) >> 8;
					break;
			}
		}
		transparentColor = transBlue << 16 | transGreen << 8 | transRed;
		if (!fixPalette) srcImage.transparentColor = transparentColor;
	}

	if (originalColors == null) {
		int mode = OS.SetStretchBltMode(handle, OS.COLORONCOLOR);
		OS.TransparentBlt(handle, destX, destY, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, transparentColor);
		OS.SetStretchBltMode(handle, mode);
	} else {
		/* Create the mask for the source image */
		long maskHdc = OS.CreateCompatibleDC(handle);
		long maskBitmap = OS.CreateBitmap(imgWidth, imgHeight, 1, 1, null);
		long oldMaskBitmap = OS.SelectObject(maskHdc, maskBitmap);
		OS.SetBkColor(srcHdc, transparentColor);
		OS.BitBlt(maskHdc, 0, 0, imgWidth, imgHeight, srcHdc, 0, 0, OS.SRCCOPY);
		if (originalColors != null) OS.SetDIBColorTable(srcHdc, 0, 1 << bm.bmBitsPixel, originalColors);

		if (OS.GetDeviceCaps(handle, OS.TECHNOLOGY) == OS.DT_RASPRINTER) {
			/* Most printers do not support BitBlt(), draw the source bitmap transparently using clipping */
			drawBitmapTransparentByClipping(srcHdc, maskHdc, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, imgWidth, imgHeight);
		} else {
			/* Draw the source bitmap transparently using invert/and mask/invert */
			long tempHdc = OS.CreateCompatibleDC(handle);
			long tempBitmap = OS.CreateCompatibleBitmap(handle, destWidth, destHeight);
			long oldTempBitmap = OS.SelectObject(tempHdc, tempBitmap);
			OS.BitBlt(tempHdc, 0, 0, destWidth, destHeight, handle, destX, destY, OS.SRCCOPY);
			if (!simple && (srcWidth != destWidth || srcHeight != destHeight)) {
				OS.SetStretchBltMode(tempHdc, OS.COLORONCOLOR);
				OS.StretchBlt(tempHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, OS.SRCINVERT);
				OS.StretchBlt(tempHdc, 0, 0, destWidth, destHeight, maskHdc, srcX, srcY, srcWidth, srcHeight, OS.SRCAND);
				OS.StretchBlt(tempHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, OS.SRCINVERT);
			} else {
				OS.BitBlt(tempHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY, OS.SRCINVERT);
				OS.BitBlt(tempHdc, 0, 0, destWidth, destHeight, maskHdc, srcX, srcY, OS.SRCAND);
				OS.BitBlt(tempHdc, 0, 0, destWidth, destHeight, srcHdc, srcX, srcY, OS.SRCINVERT);
			}
			OS.BitBlt(handle, destX, destY, destWidth, destHeight, tempHdc, 0, 0, OS.SRCCOPY);
			OS.SelectObject(tempHdc, oldTempBitmap);
			OS.DeleteDC(tempHdc);
			OS.DeleteObject(tempBitmap);
		}
		OS.SelectObject(maskHdc, oldMaskBitmap);
		OS.DeleteDC(maskHdc);
		OS.DeleteObject(maskBitmap);
	}
	OS.SelectObject(srcHdc, oldSrcBitmap);
	if (hBitmap != srcImage.handle) OS.DeleteObject(hBitmap);
	OS.DeleteDC(srcHdc);
}

void drawBitmapColor(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
	long srcHdc = OS.CreateCompatibleDC(handle);
	long oldSrcBitmap = OS.SelectObject(srcHdc, srcImage.handle);
	int dwRop = OS.GetROP2(handle) == OS.R2_XORPEN ? OS.SRCINVERT : OS.SRCCOPY;
	if (!simple && (srcWidth != destWidth || srcHeight != destHeight)) {
		int mode = OS.SetStretchBltMode(handle, OS.COLORONCOLOR);
		OS.StretchBlt(handle, destX, destY, destWidth, destHeight, srcHdc, srcX, srcY, srcWidth, srcHeight, dwRop);
		OS.SetStretchBltMode(handle, mode);
	} else {
		OS.BitBlt(handle, destX, destY, destWidth, destHeight, srcHdc, srcX, srcY, dwRop);
	}
	OS.SelectObject(srcHdc, oldSrcBitmap);
	OS.DeleteDC(srcHdc);
}

/**
 * Draws a line, using the foreground color, between the points
 * (<code>x1</code>, <code>y1</code>) and (<code>x2</code>, <code>y2</code>).
 *
 * @param x1 the first point's x coordinate
 * @param y1 the first point's y coordinate
 * @param x2 the second point's x coordinate
 * @param y2 the second point's y coordinate
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawLine (int x1, int y1, int x2, int y2) {
	x1 = DPIUtil.autoScaleUp (drawable, x1);
	x2 = DPIUtil.autoScaleUp (drawable, x2);
	y1 = DPIUtil.autoScaleUp (drawable, y1);
	y2 = DPIUtil.autoScaleUp (drawable, y2);
	drawLineInPixels(x1, y1, x2, y2);
}

void drawLineInPixels (int x1, int y1, int x2, int y2) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(DRAW);
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		Gdip.Graphics_TranslateTransform(gdipGraphics, data.gdipXOffset, data.gdipYOffset, Gdip.MatrixOrderPrepend);
		Gdip.Graphics_DrawLine(gdipGraphics, data.gdipPen, x1, y1, x2, y2);
		Gdip.Graphics_TranslateTransform(gdipGraphics, -data.gdipXOffset, -data.gdipYOffset, Gdip.MatrixOrderPrepend);
		return;
	}
	if ((data.style & SWT.MIRRORED) != 0) {
		if (data.lineWidth != 0 && data.lineWidth % 2 == 0) {
			x1--;
			x2--;
		}
	}
	OS.MoveToEx (handle, x1, y1, 0);
	OS.LineTo (handle, x2, y2);
	if (data.lineWidth <= 1) {
		OS.SetPixel (handle, x2, y2, data.foreground);
	}
}

/**
 * Draws the outline of an oval, using the foreground color,
 * within the specified rectangular area.
 * <p>
 * The result is a circle or ellipse that fits within the
 * rectangle specified by the <code>x</code>, <code>y</code>,
 * <code>width</code>, and <code>height</code> arguments.
 * </p><p>
 * The oval covers an area that is <code>width + 1</code>
 * points wide and <code>height + 1</code> points tall.
 * </p>
 *
 * @param x the x coordinate of the upper left corner of the oval to be drawn
 * @param y the y coordinate of the upper left corner of the oval to be drawn
 * @param width the width of the oval to be drawn
 * @param height the height of the oval to be drawn
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawOval (int x, int y, int width, int height) {
	x = DPIUtil.autoScaleUp (drawable, x);
	y = DPIUtil.autoScaleUp (drawable, y);
	width = DPIUtil.autoScaleUp (drawable, width);
	height = DPIUtil.autoScaleUp (drawable, height);
	drawOvalInPixels(x, y, width, height);
}

void drawOvalInPixels (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(DRAW);
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		Gdip.Graphics_TranslateTransform(gdipGraphics, data.gdipXOffset, data.gdipYOffset, Gdip.MatrixOrderPrepend);
		Gdip.Graphics_DrawEllipse(gdipGraphics, data.gdipPen, x, y, width, height);
		Gdip.Graphics_TranslateTransform(gdipGraphics, -data.gdipXOffset, -data.gdipYOffset, Gdip.MatrixOrderPrepend);
		return;
	}
	if ((data.style & SWT.MIRRORED) != 0) {
		if (data.lineWidth != 0 && data.lineWidth % 2 == 0) x--;
	}
	OS.Ellipse(handle, x, y, x + width + 1, y + height + 1);
}

/**
 * Draws the path described by the parameter.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 *
 * @param path the path to draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 *
 * @see Path
 *
 * @since 3.1
 */
public void drawPath (Path path) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (path.handle == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	initGdip();
	checkGC(DRAW);
	long gdipGraphics = data.gdipGraphics;
	Gdip.Graphics_TranslateTransform(gdipGraphics, data.gdipXOffset, data.gdipYOffset, Gdip.MatrixOrderPrepend);
	Gdip.Graphics_DrawPath(gdipGraphics, data.gdipPen, path.handle);
	Gdip.Graphics_TranslateTransform(gdipGraphics, -data.gdipXOffset, -data.gdipYOffset, Gdip.MatrixOrderPrepend);
}

/**
 * Draws an SWT logical point, using the foreground color, at the specified
 * point (<code>x</code>, <code>y</code>).
 * <p>
 * Note that the receiver's line attributes do not affect this
 * operation.
 * </p>
 *
 * @param x the point's x coordinate
 * @param y the point's y coordinate
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void drawPoint (int x, int y) {
	x = DPIUtil.autoScaleUp (drawable, x);
	y = DPIUtil.autoScaleUp (drawable, y);
	drawPointInPixels(x, y);
}

void drawPointInPixels (int x, int y) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.gdipGraphics != 0) {
		checkGC(DRAW);
		Gdip.Graphics_FillRectangle(data.gdipGraphics, getFgBrush(), x, y, 1, 1);
		return;
	}
	OS.SetPixel (handle, x, y, data.foreground);
}

/**
 * Draws the closed polygon which is defined by the specified array
 * of integer coordinates, using the receiver's foreground color. The array
 * contains alternating x and y values which are considered to represent
 * points which are the vertices of the polygon. Lines are drawn between
 * each consecutive pair, and between the first pair and last pair in the
 * array.
 *
 * @param pointArray an array of alternating x and y values which are the vertices of the polygon
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT if pointArray is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawPolygon (int[] pointArray) {
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	drawPolygonInPixels(DPIUtil.autoScaleUp(drawable, pointArray));
}

void drawPolygonInPixels(int[] pointArray) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(DRAW);
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		Gdip.Graphics_TranslateTransform(gdipGraphics, data.gdipXOffset, data.gdipYOffset, Gdip.MatrixOrderPrepend);
		Gdip.Graphics_DrawPolygon(gdipGraphics, data.gdipPen, pointArray, pointArray.length / 2);
		Gdip.Graphics_TranslateTransform(gdipGraphics, -data.gdipXOffset, -data.gdipYOffset, Gdip.MatrixOrderPrepend);
		return;
	}
	if ((data.style & SWT.MIRRORED) != 0) {
		if (data.lineWidth != 0 && data.lineWidth % 2 == 0) {
			for (int i = 0; i < pointArray.length; i+=2) {
				pointArray[i]--;
			}
		}
	}
	OS.Polygon(handle, pointArray, pointArray.length / 2);
	if ((data.style & SWT.MIRRORED) != 0) {
		if (data.lineWidth != 0 && data.lineWidth % 2 == 0) {
			for (int i = 0; i < pointArray.length; i+=2) {
				pointArray[i]++;
			}
		}
	}
}

/**
 * Draws the polyline which is defined by the specified array
 * of integer coordinates, using the receiver's foreground color. The array
 * contains alternating x and y values which are considered to represent
 * points which are the corners of the polyline. Lines are drawn between
 * each consecutive pair, but not between the first pair and last pair in
 * the array.
 *
 * @param pointArray an array of alternating x and y values which are the corners of the polyline
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawPolyline (int[] pointArray) {
	drawPolylineInPixels(DPIUtil.autoScaleUp(drawable, pointArray));
}

void drawPolylineInPixels(int[] pointArray) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	checkGC(DRAW);
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		Gdip.Graphics_TranslateTransform(gdipGraphics, data.gdipXOffset, data.gdipYOffset, Gdip.MatrixOrderPrepend);
		Gdip.Graphics_DrawLines(gdipGraphics, data.gdipPen, pointArray, pointArray.length / 2);
		Gdip.Graphics_TranslateTransform(gdipGraphics, -data.gdipXOffset, -data.gdipYOffset, Gdip.MatrixOrderPrepend);
		return;
	}
	if ((data.style & SWT.MIRRORED) != 0) {
		if (data.lineWidth != 0 && data.lineWidth % 2 == 0) {
			for (int i = 0; i < pointArray.length; i+=2) {
				pointArray[i]--;
			}
		}
	}
	OS.Polyline(handle, pointArray, pointArray.length / 2);
	int length = pointArray.length;
	if (length >= 2) {
		if (data.lineWidth <= 1) {
			OS.SetPixel (handle, pointArray[length - 2], pointArray[length - 1], data.foreground);
		}
	}
	if ((data.style & SWT.MIRRORED) != 0) {
		if (data.lineWidth != 0 && data.lineWidth % 2 == 0) {
			for (int i = 0; i < pointArray.length; i+=2) {
				pointArray[i]++;
			}
		}
	}
}

/**
 * Draws the outline of the rectangle specified by the arguments,
 * using the receiver's foreground color. The left and right edges
 * of the rectangle are at <code>x</code> and <code>x + width</code>.
 * The top and bottom edges are at <code>y</code> and <code>y + height</code>.
 *
 * @param x the x coordinate of the rectangle to be drawn
 * @param y the y coordinate of the rectangle to be drawn
 * @param width the width of the rectangle to be drawn
 * @param height the height of the rectangle to be drawn
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRectangle (int x, int y, int width, int height) {
	x = DPIUtil.autoScaleUp (drawable, x);
	y = DPIUtil.autoScaleUp (drawable, y);
	width = DPIUtil.autoScaleUp (drawable, width);
	height = DPIUtil.autoScaleUp (drawable, height);
	drawRectangleInPixels(x, y, width, height);
}

void drawRectangleInPixels (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(DRAW);
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		if (width < 0) {
			x = x + width;
			width = -width;
		}
		if (height < 0) {
			y = y + height;
			height = -height;
		}
		Gdip.Graphics_TranslateTransform(gdipGraphics, data.gdipXOffset, data.gdipYOffset, Gdip.MatrixOrderPrepend);
		Gdip.Graphics_DrawRectangle(gdipGraphics, data.gdipPen, x, y, width, height);
		Gdip.Graphics_TranslateTransform(gdipGraphics, -data.gdipXOffset, -data.gdipYOffset, Gdip.MatrixOrderPrepend);
		return;
	}
	if ((data.style & SWT.MIRRORED) != 0) {
		/*
		* Note that Rectangle() subtracts one pixel in MIRRORED mode when
		* the pen was created with CreatePen() and its width is 0 or 1.
		*/
		if (data.lineWidth > 1) {
			if ((data.lineWidth % 2) == 1) x++;
		} else {
			if (data.hPen != 0 && OS.GetObject(data.hPen, 0, 0) != OS.LOGPEN_sizeof()) {
				x++;
			}
		}
	}
	OS.Rectangle (handle, x, y, x + width + 1, y + height + 1);
}

/**
 * Draws the outline of the specified rectangle, using the receiver's
 * foreground color. The left and right edges of the rectangle are at
 * <code>rect.x</code> and <code>rect.x + rect.width</code>. The top
 * and bottom edges are at <code>rect.y</code> and
 * <code>rect.y + rect.height</code>.
 *
 * @param rect the rectangle to draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRectangle (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	rect = DPIUtil.autoScaleUp(drawable, rect);
	drawRectangleInPixels(rect.x, rect.y, rect.width, rect.height);
}

/**
 * Draws the outline of the round-cornered rectangle specified by
 * the arguments, using the receiver's foreground color. The left and
 * right edges of the rectangle are at <code>x</code> and <code>x + width</code>.
 * The top and bottom edges are at <code>y</code> and <code>y + height</code>.
 * The <em>roundness</em> of the corners is specified by the
 * <code>arcWidth</code> and <code>arcHeight</code> arguments, which
 * are respectively the width and height of the ellipse used to draw
 * the corners.
 *
 * @param x the x coordinate of the rectangle to be drawn
 * @param y the y coordinate of the rectangle to be drawn
 * @param width the width of the rectangle to be drawn
 * @param height the height of the rectangle to be drawn
 * @param arcWidth the width of the arc
 * @param arcHeight the height of the arc
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRoundRectangle (int x, int y, int width, int height, int arcWidth, int arcHeight) {
	x = DPIUtil.autoScaleUp (drawable, x);
	y = DPIUtil.autoScaleUp (drawable, y);
	width = DPIUtil.autoScaleUp (drawable, width);
	height = DPIUtil.autoScaleUp (drawable, height);
	arcWidth = DPIUtil.autoScaleUp (drawable, arcWidth);
	arcHeight = DPIUtil.autoScaleUp (drawable, arcHeight);
	drawRoundRectangleInPixels(x, y, width, height, arcWidth, arcHeight);
}

void drawRoundRectangleInPixels (int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(DRAW);
	if (data.gdipGraphics != 0) {
		drawRoundRectangleGdip(data.gdipGraphics, data.gdipPen, x, y, width, height, arcWidth, arcHeight);
		return;
	}
	if ((data.style & SWT.MIRRORED) != 0) {
		if (data.lineWidth != 0 && data.lineWidth % 2 == 0) x--;
	}
	OS.RoundRect(handle, x,y,x+width+1,y+height+1, arcWidth, arcHeight);
}

void drawRoundRectangleGdip (long gdipGraphics, long pen, int x, int y, int width, int height, int arcWidth, int arcHeight) {
	int nx = x;
	int ny = y;
	int nw = width;
	int nh = height;
	int naw = arcWidth;
	int nah = arcHeight;

	if (nw < 0) {
		nw = 0 - nw;
		nx = nx - nw;
	}
	if (nh < 0) {
		nh = 0 - nh;
		ny = ny - nh;
	}
	if (naw < 0)
		naw = 0 - naw;
	if (nah < 0)
		nah = 0 - nah;

	Gdip.Graphics_TranslateTransform(gdipGraphics, data.gdipXOffset, data.gdipYOffset, Gdip.MatrixOrderPrepend);
	if (naw == 0 || nah == 0) {
		Gdip.Graphics_DrawRectangle(gdipGraphics, data.gdipPen, x, y, width, height);
	} else {
		long path = Gdip.GraphicsPath_new(Gdip.FillModeAlternate);
		if (path == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		if (nw > naw) {
			if (nh > nah) {
				Gdip.GraphicsPath_AddArc(path, nx + nw - naw, ny, naw, nah, 0, -90);
				Gdip.GraphicsPath_AddArc(path, nx, ny, naw, nah, -90, -90);
				Gdip.GraphicsPath_AddArc(path, nx, ny + nh - nah, naw, nah, -180, -90);
				Gdip.GraphicsPath_AddArc(path, nx + nw - naw, ny + nh - nah, naw, nah, -270, -90);
			} else {
				Gdip.GraphicsPath_AddArc(path, nx + nw - naw, ny, naw, nh, -270, -180);
				Gdip.GraphicsPath_AddArc(path, nx, ny, naw, nh, -90, -180);
			}
		} else {
			if (nh > nah) {
				Gdip.GraphicsPath_AddArc(path, nx, ny, nw, nah, 0, -180);
				Gdip.GraphicsPath_AddArc(path, nx, ny + nh - nah, nw, nah, -180, -180);
			} else {
				Gdip.GraphicsPath_AddArc(path, nx, ny, nw, nh, 0, 360);
			}
		}
		Gdip.GraphicsPath_CloseFigure(path);
		Gdip.Graphics_DrawPath(gdipGraphics, pen, path);
		Gdip.GraphicsPath_delete(path);
	}
	Gdip.Graphics_TranslateTransform(gdipGraphics, -data.gdipXOffset, -data.gdipYOffset, Gdip.MatrixOrderPrepend);
}

/**
 * Draws the given string, using the receiver's current font and
 * foreground color. No tab expansion or carriage return processing
 * will be performed. The background of the rectangular area where
 * the string is being drawn will be filled with the receiver's
 * background color.
 * <br><br>
 * On Windows, {@link #drawString} and {@link #drawText} are slightly
 * different, see {@link #drawString(String, int, int, boolean)} for
 * explanation.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the string is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the string is to be drawn
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawString (String string, int x, int y) {
	x = DPIUtil.autoScaleUp(drawable, x);
	y = DPIUtil.autoScaleUp(drawable, y);
	drawStringInPixels(string, x, y, false);
}

/**
 * Draws the given string, using the receiver's current font and
 * foreground color. No tab expansion or carriage return processing
 * will be performed. If <code>isTransparent</code> is <code>true</code>,
 * then the background of the rectangular area where the string is being
 * drawn will not be modified, otherwise it will be filled with the
 * receiver's background color.
 * <br><br>
 * On Windows, {@link #drawString} and {@link #drawText} are slightly
 * different:
 * <ul>
 *     <li>{@link #drawString} is faster (depends on string size)<br>~7x for 1-char strings<br>~4x for 10-char strings<br>~2x for 100-char strings</li>
 *     <li>{@link #drawString} doesn't try to find a good fallback font when character doesn't have a glyph in currently selected font</li>
 * </ul>
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the string is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the string is to be drawn
 * @param isTransparent if <code>true</code> the background will be transparent, otherwise it will be opaque
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawString (String string, int x, int y, boolean isTransparent) {
	x = DPIUtil.autoScaleUp(drawable, x);
	y = DPIUtil.autoScaleUp(drawable, y);
	drawStringInPixels(string, x, y, isTransparent);
}

void drawStringInPixels (String string, int x, int y, boolean isTransparent) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (string.isEmpty()) return;
	char[] buffer = string.toCharArray();
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		checkGC(FONT | FOREGROUND | (isTransparent ? 0 : BACKGROUND));
		drawText(gdipGraphics, string, x, y, isTransparent ? SWT.DRAW_TRANSPARENT : 0, null);
		return;
	}
	checkGC(FONT | FOREGROUND_TEXT | BACKGROUND_TEXT);
	int oldBkMode = OS.SetBkMode(handle, isTransparent ? OS.TRANSPARENT : OS.OPAQUE);
	RECT rect = null;
	SIZE size = null;
	int flags = 0;
	if ((data.style & SWT.MIRRORED) != 0) {
		if (!isTransparent) {
			size = new SIZE();
			OS.GetTextExtentPoint32(handle, buffer, buffer.length, size);
			rect = new RECT ();
			rect.left = x;
			rect.right = x + size.cx;
			rect.top = y;
			rect.bottom = y + size.cy;
			flags = OS.ETO_CLIPPED;
		}
		x--;
	}
	if (OS.GetROP2(handle) != OS.R2_XORPEN) {
		/* Note: The use of ExtTextOut() causes documented differences from GC.drawText() */
		OS.ExtTextOut(handle, x, y, flags, rect, buffer, buffer.length, null);
	} else {
		int foreground = OS.GetTextColor(handle);
		if (isTransparent) {
			if (size == null) {
				size = new SIZE();
				OS.GetTextExtentPoint32(handle, buffer, buffer.length, size);
			}
			int width = size.cx, height = size.cy;
			long hBitmap = OS.CreateCompatibleBitmap(handle, width, height);
			if (hBitmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			long memDC = OS.CreateCompatibleDC(handle);
			long hOldBitmap = OS.SelectObject(memDC, hBitmap);
			OS.PatBlt(memDC, 0, 0, width, height, OS.BLACKNESS);
			OS.SetBkMode(memDC, OS.TRANSPARENT);
			OS.SetTextColor(memDC, foreground);
			OS.SelectObject(memDC, OS.GetCurrentObject(handle, OS.OBJ_FONT));
			OS.ExtTextOut(memDC, 0, 0, 0, null, buffer, buffer.length, null);
			OS.BitBlt(handle, x, y, width, height, memDC, 0, 0, OS.SRCINVERT);
			OS.SelectObject(memDC, hOldBitmap);
			OS.DeleteDC(memDC);
			OS.DeleteObject(hBitmap);
		} else {
			int background = OS.GetBkColor(handle);
			OS.SetTextColor(handle, foreground ^ background);
			OS.ExtTextOut(handle, x, y, flags, rect, buffer, buffer.length, null);
			OS.SetTextColor(handle, foreground);
		}
	}
	OS.SetBkMode(handle, oldBkMode);
}

/**
 * Draws the given string, using the receiver's current font and
 * foreground color. Tab expansion and carriage return processing
 * are performed. The background of the rectangular area where
 * the text is being drawn will be filled with the receiver's
 * background color.
 * <br><br>
 * On Windows, {@link #drawString} and {@link #drawText} are slightly
 * different, see {@link #drawString(String, int, int, boolean)} for
 * explanation.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawText (String string, int x, int y) {
	x = DPIUtil.autoScaleUp(drawable, x);
	y = DPIUtil.autoScaleUp(drawable, y);
	drawTextInPixels(string, x, y);
}

void drawTextInPixels (String string, int x, int y) {
	drawTextInPixels(string, x, y, SWT.DRAW_DELIMITER | SWT.DRAW_TAB);
}

/**
 * Draws the given string, using the receiver's current font and
 * foreground color. Tab expansion and carriage return processing
 * are performed. If <code>isTransparent</code> is <code>true</code>,
 * then the background of the rectangular area where the text is being
 * drawn will not be modified, otherwise it will be filled with the
 * receiver's background color.
 * <br><br>
 * On Windows, {@link #drawString} and {@link #drawText} are slightly
 * different, see {@link #drawString(String, int, int, boolean)} for
 * explanation.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param isTransparent if <code>true</code> the background will be transparent, otherwise it will be opaque
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawText (String string, int x, int y, boolean isTransparent) {
	x = DPIUtil.autoScaleUp(drawable, x);
	y = DPIUtil.autoScaleUp(drawable, y);
	drawTextInPixels(string, x, y, isTransparent);
}

void drawTextInPixels (String string, int x, int y, boolean isTransparent) {
	int flags = SWT.DRAW_DELIMITER | SWT.DRAW_TAB;
	if (isTransparent) flags |= SWT.DRAW_TRANSPARENT;
	drawTextInPixels(string, x, y, flags);
}

/**
 * Draws the given string, using the receiver's current font and
 * foreground color. Tab expansion, line delimiter and mnemonic
 * processing are performed according to the specified flags. If
 * <code>flags</code> includes <code>DRAW_TRANSPARENT</code>,
 * then the background of the rectangular area where the text is being
 * drawn will not be modified, otherwise it will be filled with the
 * receiver's background color.
 * <br><br>
 * On Windows, {@link #drawString} and {@link #drawText} are slightly
 * different, see {@link #drawString(String, int, int, boolean)} for
 * explanation.
 *
 * <p>
 * The parameter <code>flags</code> may be a combination of:
 * </p>
 * <dl>
 * <dt><b>DRAW_DELIMITER</b></dt>
 * <dd>draw multiple lines</dd>
 * <dt><b>DRAW_TAB</b></dt>
 * <dd>expand tabs</dd>
 * <dt><b>DRAW_MNEMONIC</b></dt>
 * <dd>underline the mnemonic character</dd>
 * <dt><b>DRAW_TRANSPARENT</b></dt>
 * <dd>transparent background</dd>
 * </dl>
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param flags the flags specifying how to process the text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawText (String string, int x, int y, int flags) {
	x = DPIUtil.autoScaleUp(drawable, x);
	y = DPIUtil.autoScaleUp(drawable, y);
	drawTextInPixels(string, x, y, flags);
}

void drawTextInPixels (String string, int x, int y, int flags) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (string.isEmpty()) return;
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		checkGC(FONT | FOREGROUND | ((flags & SWT.DRAW_TRANSPARENT) != 0 ? 0 : BACKGROUND));
		drawText(gdipGraphics, string, x, y, flags, null);
		return;
	}
	char [] buffer = string.toCharArray();
	RECT rect = new RECT();
	OS.SetRect(rect, x, y, 0x6FFFFFF, 0x6FFFFFF);
	int uFormat = OS.DT_LEFT;
	if ((flags & SWT.DRAW_DELIMITER) == 0) uFormat |= OS.DT_SINGLELINE;
	if ((flags & SWT.DRAW_TAB) != 0) uFormat |= OS.DT_EXPANDTABS;
	if ((flags & SWT.DRAW_MNEMONIC) == 0) uFormat |= OS.DT_NOPREFIX;
	if ((flags & SWT.DRAW_MNEMONIC) != 0 && (data.uiState & OS.UISF_HIDEACCEL) != 0) {
		uFormat |= OS.DT_HIDEPREFIX;
	}
	checkGC(FONT | FOREGROUND_TEXT | BACKGROUND_TEXT);
	int oldBkMode = OS.SetBkMode(handle, (flags & SWT.DRAW_TRANSPARENT) != 0 ? OS.TRANSPARENT : OS.OPAQUE);
	if (OS.GetROP2(handle) != OS.R2_XORPEN) {
		/* Note: The use of DrawText() causes documented differences from GC.drawString() */
		OS.DrawText(handle, buffer, buffer.length, rect, uFormat);
	} else {
		int foreground = OS.GetTextColor(handle);
		if ((flags & SWT.DRAW_TRANSPARENT) != 0) {
			OS.DrawText(handle, buffer, buffer.length, rect, uFormat | OS.DT_CALCRECT);
			int width = rect.right - rect.left;
			int height = rect.bottom - rect.top;
			long hBitmap = OS.CreateCompatibleBitmap(handle, width, height);
			if (hBitmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			long memDC = OS.CreateCompatibleDC(handle);
			long hOldBitmap = OS.SelectObject(memDC, hBitmap);
			OS.PatBlt(memDC, 0, 0, width, height, OS.BLACKNESS);
			OS.SetBkMode(memDC, OS.TRANSPARENT);
			OS.SetTextColor(memDC, foreground);
			OS.SelectObject(memDC, OS.GetCurrentObject(handle, OS.OBJ_FONT));
			OS.SetRect(rect, 0, 0, 0x7FFF, 0x7FFF);
			OS.DrawText(memDC, buffer, buffer.length, rect, uFormat);
			OS.BitBlt(handle, x, y, width, height, memDC, 0, 0, OS.SRCINVERT);
			OS.SelectObject(memDC, hOldBitmap);
			OS.DeleteDC(memDC);
			OS.DeleteObject(hBitmap);
		} else {
			int background = OS.GetBkColor(handle);
			OS.SetTextColor(handle, foreground ^ background);
			OS.DrawText(handle, buffer, buffer.length, rect, uFormat);
			OS.SetTextColor(handle, foreground);
		}
	}
	OS.SetBkMode(handle, oldBkMode);
}

boolean useGDIP (long hdc, char[] buffer) {
	short[] glyphs = new short[buffer.length];
	OS.GetGlyphIndices(hdc, buffer, buffer.length, glyphs, OS.GGI_MARK_NONEXISTING_GLYPHS);
	for (int i = 0; i < glyphs.length; i++) {
		if (glyphs [i] == -1) {
			switch (buffer[i]) {
				case '\t':
				case '\n':
				case '\r':
					break;
				default:
					return true;
			}
		}
	}
	return false;
}

void drawText(long gdipGraphics, String string, int x, int y, int flags, Point size) {
	int length = string.length();
	char[] chars = string.toCharArray();
	long hdc = Gdip.Graphics_GetHDC(gdipGraphics);
	long hFont = data.hGDIFont;
	if (hFont == 0 && data.font != null) hFont = data.font.handle;
	long oldFont = 0;
	if (hFont != 0) oldFont = OS.SelectObject(hdc, hFont);
	TEXTMETRIC lptm = new TEXTMETRIC();
	OS.GetTextMetrics(hdc, lptm);
	boolean gdip = useGDIP(hdc, chars);
	if (hFont != 0) OS.SelectObject(hdc, oldFont);
	Gdip.Graphics_ReleaseHDC(gdipGraphics, hdc);
	if (gdip) {
		drawTextGDIP(gdipGraphics, string, x, y, flags, size == null, size);
		return;
	}
	int i = 0, start = 0, end = 0, drawX = x, drawY = y, width = 0, mnemonicIndex = -1;
	if ((flags & (SWT.DRAW_DELIMITER | SWT.DRAW_TAB | SWT.DRAW_MNEMONIC)) != 0) {
		int tabWidth = lptm.tmAveCharWidth * 8;
		while (i < length) {
			char c = chars [end++] = chars [i++];
			switch (c) {
				case '\t':
					if ((flags & SWT.DRAW_TAB) != 0) {
						int l = end - start - 1;
						RectF bounds = drawText(gdipGraphics, chars, start, l, drawX, drawY, flags, mnemonicIndex, lptm, size == null);
						drawX += Math.ceil(bounds.Width);
						drawX = x + (((drawX - x) / tabWidth) + 1) * tabWidth;
						mnemonicIndex = -1;
						start = end;
					}
					break;
				case '&':
					if ((flags & SWT.DRAW_MNEMONIC) != 0) {
						if (i == length) {end--; continue;}
						if (chars [i] == '&') {i++; continue;}
						end--;
						mnemonicIndex = end - start;
					}
					break;
				case '\r':
				case '\n':
					if ((flags & SWT.DRAW_DELIMITER) != 0) {
						int l = end - start - 1;
						if (c == '\r' && end != length && chars[end] == '\n') {
							end++;
							i++;
						}
						RectF bounds = drawText(gdipGraphics, chars, start, l, drawX, drawY, flags, mnemonicIndex, lptm, size == null);
						drawY += Math.ceil(bounds.Height);
						width = Math.max(width, drawX + (int)Math.ceil(bounds.Width));
						drawX = x;
						mnemonicIndex = -1;
						start = end;
					}
					break;
			}
		}
		length = end;
	}
	RectF bounds = drawText(gdipGraphics, chars, start, length - start, drawX, drawY, flags, mnemonicIndex, lptm, size == null);
	if (size != null) {
		drawY += Math.ceil(bounds.Height);
		width = Math.max(width, drawX + (int)Math.ceil(bounds.Width));
		size.x = width;
		size.y = drawY;
	}
}

RectF drawText(long gdipGraphics, char[] buffer, int start, int length, int x, int y, int flags, int mnemonicIndex, TEXTMETRIC lptm, boolean draw) {
	boolean drawMnemonic = draw && mnemonicIndex != -1 && (data.uiState & OS.UISF_HIDEACCEL) == 0;
	boolean needsBounds = !draw || drawMnemonic || (flags & SWT.DRAW_TRANSPARENT) == 0 || (data.style & SWT.MIRRORED) != 0 || (flags & SWT.DRAW_DELIMITER) != 0;
	if (length <= 0) {
		RectF bounds = null;
		if (needsBounds) {
			bounds = new RectF();
			bounds.Height = lptm.tmHeight;
		}
		return bounds;
	}
	int nGlyphs = (length * 3 / 2) + 16;
	GCP_RESULTS result = new GCP_RESULTS();
	result.lStructSize = GCP_RESULTS.sizeof;
	result.nGlyphs = nGlyphs;
	long hHeap = OS.GetProcessHeap();
	long lpDx = result.lpDx = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, nGlyphs * 4);
	long lpGlyphs = result.lpGlyphs = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, nGlyphs * 2);
	long lpOrder = 0;
	int dwFlags = OS.GCP_GLYPHSHAPE | OS.GCP_REORDER | OS.GCP_LIGATE;
	if (drawMnemonic) {
		lpOrder = result.lpOrder = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, nGlyphs * 4);
	}
	long hdc = Gdip.Graphics_GetHDC(gdipGraphics);
	long hFont = data.hGDIFont;
	if (hFont == 0 && data.font != null) hFont = data.font.handle;
	long oldFont = 0;
	if (hFont != 0) oldFont = OS.SelectObject(hdc, hFont);
	if (start != 0) {
		char[] temp = new char[length];
		System.arraycopy(buffer, start, temp, 0, length);
		buffer = temp;
	}
	if ((data.style & SWT.MIRRORED) != 0) OS.SetLayout(hdc, OS.GetLayout(hdc) | OS.LAYOUT_RTL);
	OS.GetCharacterPlacement(hdc, buffer, length, 0, result, dwFlags);
	if ((data.style & SWT.MIRRORED) != 0) OS.SetLayout(hdc, OS.GetLayout(hdc) & ~OS.LAYOUT_RTL);
	if (hFont != 0) OS.SelectObject(hdc, oldFont);
	Gdip.Graphics_ReleaseHDC(gdipGraphics, hdc);
	nGlyphs = result.nGlyphs;
	int drawX = x, drawY = y + lptm.tmAscent;
	int[] dx = new int[nGlyphs];
	OS.MoveMemory(dx, result.lpDx, nGlyphs * 4);
	float[] points = new float[dx.length * 2];
	for (int i = 0, j = 0; i < dx.length; i++) {
		points[j++] = drawX;
		points[j++] = drawY;
		drawX += dx[i];
	}
	RectF bounds = null;
	if (needsBounds) {
		bounds = new RectF();
		Gdip.Graphics_MeasureDriverString(gdipGraphics, lpGlyphs, nGlyphs, data.gdipFont, points, 0, 0, bounds);
	}
	if (draw) {
		if ((flags & SWT.DRAW_TRANSPARENT) == 0) {
			Gdip.Graphics_FillRectangle(gdipGraphics, data.gdipBrush, x, y, (int)Math.ceil(bounds.Width), (int)Math.ceil(bounds.Height));
		}
		int gstate = 0;
		long brush = getFgBrush();
		if ((data.style & SWT.MIRRORED) != 0) {
			switch (Gdip.Brush_GetType(brush)) {
				case Gdip.BrushTypeLinearGradient:
					Gdip.LinearGradientBrush_ScaleTransform(brush, -1, 1, Gdip.MatrixOrderPrepend);
					Gdip.LinearGradientBrush_TranslateTransform(brush, - 2 * x - bounds.Width, 0, Gdip.MatrixOrderPrepend);
					break;
				case Gdip.BrushTypeTextureFill:
					Gdip.TextureBrush_ScaleTransform(brush, -1, 1, Gdip.MatrixOrderPrepend);
					Gdip.TextureBrush_TranslateTransform(brush, - 2 * x - bounds.Width, 0, Gdip.MatrixOrderPrepend);
					break;
			}
			gstate = Gdip.Graphics_Save(gdipGraphics);
			Gdip.Graphics_ScaleTransform(gdipGraphics, -1, 1, Gdip.MatrixOrderPrepend);
			Gdip.Graphics_TranslateTransform(gdipGraphics, - 2 * x - bounds.Width, 0, Gdip.MatrixOrderPrepend);
		}
		Gdip.Graphics_DrawDriverString(gdipGraphics, lpGlyphs, result.nGlyphs, data.gdipFont, brush, points, 0, 0);
		if ((data.style & SWT.MIRRORED) != 0) {
			switch (Gdip.Brush_GetType(brush)) {
				case Gdip.BrushTypeLinearGradient:
					Gdip.LinearGradientBrush_ResetTransform(brush);
					break;
				case Gdip.BrushTypeTextureFill:
					Gdip.TextureBrush_ResetTransform(brush);
					break;
			}
			Gdip.Graphics_Restore(gdipGraphics, gstate);
		}
		if (drawMnemonic) {
			long pen = Gdip.Pen_new(brush, 1);
			if (pen != 0) {
				int[] order = new int[1];
				OS.MoveMemory(order, result.lpOrder + mnemonicIndex * 4, 4);
				int mnemonicLeft, mnemonicRight;
				if ((data.style & SWT.MIRRORED) != 0) {
					mnemonicLeft = (int)Math.ceil(bounds.Width) - (int)points[order[0] * 2] + 2 * x;
					mnemonicRight = mnemonicLeft - dx[order[0]];
				} else {
					mnemonicLeft = (int)points[order[0] * 2];
					mnemonicRight = mnemonicLeft + dx[order[0]];
				}
				int mnemonicY = y + lptm.tmAscent + 2;
				int smoothingMode = Gdip.Graphics_GetSmoothingMode(gdipGraphics);
				Gdip.Graphics_SetSmoothingMode(gdipGraphics, Gdip.SmoothingModeNone);
				Gdip.Graphics_DrawLine(gdipGraphics, pen, mnemonicLeft, mnemonicY, mnemonicRight, mnemonicY);
				Gdip.Graphics_SetSmoothingMode(gdipGraphics, smoothingMode);
				Gdip.Pen_delete(pen);
			}
		}
	}
	if (lpOrder != 0) OS.HeapFree(hHeap, 0, lpOrder);
	OS.HeapFree(hHeap, 0, lpGlyphs);
	OS.HeapFree(hHeap, 0, lpDx);
	return bounds;
}

void drawTextGDIP(long gdipGraphics, String string, int x, int y, int flags, boolean draw, Point size) {
	boolean needsBounds = !draw || (flags & SWT.DRAW_TRANSPARENT) == 0;
	char[] buffer;
	int length = string.length();
	if (length != 0) {
		buffer = string.toCharArray();
	} else {
		if (draw) return;
		buffer = new char[]{' '};
	}
	PointF pt = new PointF();
	long format = Gdip.StringFormat_Clone(Gdip.StringFormat_GenericTypographic());
	int formatFlags = Gdip.StringFormat_GetFormatFlags(format) | Gdip.StringFormatFlagsMeasureTrailingSpaces;
	if ((data.style & SWT.MIRRORED) != 0) formatFlags |= Gdip.StringFormatFlagsDirectionRightToLeft;
	Gdip.StringFormat_SetFormatFlags(format, formatFlags);
	float[] tabs = (flags & SWT.DRAW_TAB) != 0 ? new float[]{measureSpace(data.gdipFont, format) * 8} : new float[1];
	Gdip.StringFormat_SetTabStops(format, 0, tabs.length, tabs);
	int hotkeyPrefix = (flags & SWT.DRAW_MNEMONIC) != 0 ? Gdip.HotkeyPrefixShow : Gdip.HotkeyPrefixNone;
	if ((flags & SWT.DRAW_MNEMONIC) != 0 && (data.uiState & OS.UISF_HIDEACCEL) != 0) hotkeyPrefix = Gdip.HotkeyPrefixHide;
	Gdip.StringFormat_SetHotkeyPrefix(format, hotkeyPrefix);
	RectF bounds = null;
	if (needsBounds) {
		bounds = new RectF();
		Gdip.Graphics_MeasureString(gdipGraphics, buffer, buffer.length, data.gdipFont, pt, format, bounds);
	}
	if (draw) {
		if ((flags & SWT.DRAW_TRANSPARENT) == 0) {
			Gdip.Graphics_FillRectangle(gdipGraphics, data.gdipBrush, x, y, (int)Math.ceil(bounds.Width), (int)Math.ceil(bounds.Height));
		}
		int gstate = 0;
		long brush = getFgBrush();
		if ((data.style & SWT.MIRRORED) != 0) {
			switch (Gdip.Brush_GetType(brush)) {
				case Gdip.BrushTypeLinearGradient:
					Gdip.LinearGradientBrush_ScaleTransform(brush, -1, 1, Gdip.MatrixOrderPrepend);
					Gdip.LinearGradientBrush_TranslateTransform(brush, - 2 * x, 0, Gdip.MatrixOrderPrepend);
					break;
				case Gdip.BrushTypeTextureFill:
					Gdip.TextureBrush_ScaleTransform(brush, -1, 1, Gdip.MatrixOrderPrepend);
					Gdip.TextureBrush_TranslateTransform(brush, - 2 * x, 0, Gdip.MatrixOrderPrepend);
					break;
			}
			gstate = Gdip.Graphics_Save(gdipGraphics);
			Gdip.Graphics_ScaleTransform(gdipGraphics, -1, 1, Gdip.MatrixOrderPrepend);
			Gdip.Graphics_TranslateTransform(gdipGraphics, - 2 * x, 0, Gdip.MatrixOrderPrepend);
		}
		pt.X = x;
		pt.Y = y;
		Gdip.Graphics_DrawString(gdipGraphics, buffer, buffer.length, data.gdipFont, pt, format, brush);
		if ((data.style & SWT.MIRRORED) != 0) {
			switch (Gdip.Brush_GetType(brush)) {
				case Gdip.BrushTypeLinearGradient:
					Gdip.LinearGradientBrush_ResetTransform(brush);
					break;
				case Gdip.BrushTypeTextureFill:
					Gdip.TextureBrush_ResetTransform(brush);
					break;
			}
			Gdip.Graphics_Restore(gdipGraphics, gstate);
		}
	}
	Gdip.StringFormat_delete(format);
	if (length == 0) bounds.Width = 0;
	if (size != null) {
		size.x = (int)Math.ceil(bounds.Width);
		size.y = (int)Math.ceil(bounds.Height);
	}
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode
 */
@Override
public boolean equals (Object object) {
	return (object == this) || ((object instanceof GC) && (handle == ((GC)object).handle));
}

/**
 * Fills the interior of a circular or elliptical arc within
 * the specified rectangular area, with the receiver's background
 * color.
 * <p>
 * The resulting arc begins at <code>startAngle</code> and extends
 * for <code>arcAngle</code> degrees, using the current color.
 * Angles are interpreted such that 0 degrees is at the 3 o'clock
 * position. A positive value indicates a counter-clockwise rotation
 * while a negative value indicates a clockwise rotation.
 * </p><p>
 * The center of the arc is the center of the rectangle whose origin
 * is (<code>x</code>, <code>y</code>) and whose size is specified by the
 * <code>width</code> and <code>height</code> arguments.
 * </p><p>
 * The resulting arc covers an area <code>width + 1</code> points wide
 * by <code>height + 1</code> points tall.
 * </p>
 *
 * @param x the x coordinate of the upper-left corner of the arc to be filled
 * @param y the y coordinate of the upper-left corner of the arc to be filled
 * @param width the width of the arc to be filled
 * @param height the height of the arc to be filled
 * @param startAngle the beginning angle
 * @param arcAngle the angular extent of the arc, relative to the start angle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawArc
 */
public void fillArc (int x, int y, int width, int height, int startAngle, int arcAngle) {
	x = DPIUtil.autoScaleUp (drawable, x);
	y = DPIUtil.autoScaleUp (drawable, y);
	width = DPIUtil.autoScaleUp (drawable, width);
	height = DPIUtil.autoScaleUp (drawable, height);
	fillArcInPixels(x, y, width, height, startAngle, arcAngle);
}

void fillArcInPixels (int x, int y, int width, int height, int startAngle, int arcAngle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(FILL);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		if (width == height) {
			Gdip.Graphics_FillPie(gdipGraphics, data.gdipBrush, x, y, width, height, -startAngle, -arcAngle);
		} else {
			int state = Gdip.Graphics_Save(gdipGraphics);
			Gdip.Graphics_TranslateTransform(gdipGraphics, x, y, Gdip.MatrixOrderPrepend);
			Gdip.Graphics_ScaleTransform(gdipGraphics, width, height, Gdip.MatrixOrderPrepend);
			Gdip.Graphics_FillPie(gdipGraphics, data.gdipBrush, 0, 0, 1, 1, -startAngle, -arcAngle);
			Gdip.Graphics_Restore(gdipGraphics, state);
		}
		return;
	}

	if ((data.style & SWT.MIRRORED) != 0) x--;
	int x1, y1, x2, y2,tmp;
	boolean isNegative;
	if (arcAngle >= 360 || arcAngle <= -360) {
		x1 = x2 = x + width;
		y1 = y2 = y + height / 2;
	} else {
		isNegative = arcAngle < 0;

		arcAngle = arcAngle + startAngle;
		if (isNegative) {
			// swap angles
			tmp = startAngle;
			startAngle = arcAngle;
			arcAngle = tmp;
		}
		x1 = cos(startAngle, width) + x + width/2;
		y1 = -1 * sin(startAngle, height) + y + height/2;

		x2 = cos(arcAngle, width) + x + width/2;
		y2 = -1 * sin(arcAngle, height) + y + height/2;
	}
	OS.Pie(handle, x, y, x + width + 1, y + height + 1, x1, y1, x2, y2);
}

/**
 * Fills the interior of the specified rectangle with a gradient
 * sweeping from left to right or top to bottom progressing
 * from the receiver's foreground color to its background color.
 *
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled, may be negative
 *        (inverts direction of gradient if horizontal)
 * @param height the height of the rectangle to be filled, may be negative
 *        (inverts direction of gradient if vertical)
 * @param vertical if true sweeps from top to bottom, else
 *        sweeps from left to right
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle(int, int, int, int)
 */
public void fillGradientRectangle (int x, int y, int width, int height, boolean vertical) {
	x = DPIUtil.autoScaleUp (drawable, x);
	y = DPIUtil.autoScaleUp (drawable, y);
	width = DPIUtil.autoScaleUp (drawable, width);
	height = DPIUtil.autoScaleUp (drawable, height);
	fillGradientRectangleInPixels(x, y, width, height, vertical);
}

void fillGradientRectangleInPixels(int x, int y, int width, int height, boolean vertical) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width == 0 || height == 0) return;

	RGB backgroundRGB, foregroundRGB;
	backgroundRGB = getBackground().getRGB();
	foregroundRGB = getForeground().getRGB();

	RGB fromRGB, toRGB;
	fromRGB = foregroundRGB;
	toRGB   = backgroundRGB;

	boolean swapColors = false;
	if (width < 0) {
		x += width; width = -width;
		if (! vertical) swapColors = true;
	}
	if (height < 0) {
		y += height; height = -height;
		if (vertical) swapColors = true;
	}
	if (swapColors) {
		fromRGB = backgroundRGB;
		toRGB   = foregroundRGB;
	}
	if (fromRGB.equals(toRGB)) {
		fillRectangleInPixels(x, y, width, height);
		return;
	}
	if (data.gdipGraphics != 0) {
		initGdip();
		PointF p1= new PointF(), p2 = new PointF();
		p1.X = x;
		p1.Y = y;
		if (vertical) {
			p2.X = p1.X;
			p2.Y = p1.Y + height;
		} else {
			p2.X = p1.X + width;
			p2.Y = p1.Y;
		}
		int fromGpColor = (data.alpha << 24) | ((fromRGB.red & 0xFF) << 16) | ((fromRGB.green & 0xFF) << 8) | (fromRGB.blue & 0xFF);
		int toGpColor = (data.alpha << 24) | ((toRGB.red & 0xFF) << 16) | ((toRGB.green & 0xFF) << 8) | (toRGB.blue & 0xFF);
		long brush = Gdip.LinearGradientBrush_new(p1, p2, fromGpColor, toGpColor);
		Gdip.Graphics_FillRectangle(data.gdipGraphics, brush, x, y, width, height);
		Gdip.LinearGradientBrush_delete(brush);
		return;
	}
	/*
	* Bug in Windows: On Windows 2000 when the device is a printer,
	* GradientFill swaps red and blue color components, causing the
	* gradient to be printed in the wrong color. The fix is not to use
	* GradientFill for printer devices.
	*/
	if (OS.GetROP2(handle) != OS.R2_XORPEN && OS.GetDeviceCaps(handle, OS.TECHNOLOGY) != OS.DT_RASPRINTER) {
		final long hHeap = OS.GetProcessHeap();
		final long pMesh = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, GRADIENT_RECT.sizeof + TRIVERTEX.sizeof * 2);
		if (pMesh == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		final long pVertex = pMesh + GRADIENT_RECT.sizeof;

		GRADIENT_RECT gradientRect = new GRADIENT_RECT();
		gradientRect.UpperLeft = 0;
		gradientRect.LowerRight = 1;
		OS.MoveMemory(pMesh, gradientRect, GRADIENT_RECT.sizeof);

		TRIVERTEX trivertex = new TRIVERTEX();
		trivertex.x = x;
		trivertex.y = y;
		trivertex.Red = (short)((fromRGB.red << 8) | fromRGB.red);
		trivertex.Green = (short)((fromRGB.green << 8) | fromRGB.green);
		trivertex.Blue = (short)((fromRGB.blue << 8) | fromRGB.blue);
		trivertex.Alpha = -1;
		OS.MoveMemory(pVertex, trivertex, TRIVERTEX.sizeof);

		trivertex.x = x + width;
		trivertex.y = y + height;
		trivertex.Red = (short)((toRGB.red << 8) | toRGB.red);
		trivertex.Green = (short)((toRGB.green << 8) | toRGB.green);
		trivertex.Blue = (short)((toRGB.blue << 8) | toRGB.blue);
		trivertex.Alpha = -1;
		OS.MoveMemory(pVertex + TRIVERTEX.sizeof, trivertex, TRIVERTEX.sizeof);

		boolean success = OS.GradientFill(handle, pVertex, 2, pMesh, 1, vertical ? OS.GRADIENT_FILL_RECT_V : OS.GRADIENT_FILL_RECT_H);
		OS.HeapFree(hHeap, 0, pMesh);
		if (success) return;
	}

	final int depth = OS.GetDeviceCaps(handle, OS.BITSPIXEL);
	final int bitResolution = (depth >= 24) ? 8 : (depth >= 15) ? 5 : 0;
	ImageData.fillGradientRectangle(this, data.device,
		x, y, width, height, vertical, fromRGB, toRGB,
		bitResolution, bitResolution, bitResolution);
}

/**
 * Fills the interior of an oval, within the specified
 * rectangular area, with the receiver's background
 * color.
 *
 * @param x the x coordinate of the upper left corner of the oval to be filled
 * @param y the y coordinate of the upper left corner of the oval to be filled
 * @param width the width of the oval to be filled
 * @param height the height of the oval to be filled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawOval
 */
public void fillOval (int x, int y, int width, int height) {
	x = DPIUtil.autoScaleUp (drawable, x);
	y = DPIUtil.autoScaleUp (drawable, y);
	width = DPIUtil.autoScaleUp (drawable, width);
	height = DPIUtil.autoScaleUp (drawable, height);
	fillOvalInPixels(x, y, width, height);
}

void fillOvalInPixels (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(FILL);
	if (data.gdipGraphics != 0) {
		Gdip.Graphics_FillEllipse(data.gdipGraphics, data.gdipBrush, x, y, width, height);
		return;
	}
	if ((data.style & SWT.MIRRORED) != 0) x--;
	OS.Ellipse(handle, x, y, x + width + 1, y + height + 1);
}

/**
 * Fills the path described by the parameter.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 *
 * @param path the path to fill
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 *
 * @see Path
 *
 * @since 3.1
 */
public void fillPath (Path path) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (path.handle == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	initGdip();
	checkGC(FILL);
	int mode = OS.GetPolyFillMode(handle) == OS.WINDING ? Gdip.FillModeWinding : Gdip.FillModeAlternate;
	Gdip.GraphicsPath_SetFillMode(path.handle, mode);
	Gdip.Graphics_FillPath(data.gdipGraphics, data.gdipBrush, path.handle);
}

/**
 * Fills the interior of the closed polygon which is defined by the
 * specified array of integer coordinates, using the receiver's
 * background color. The array contains alternating x and y values
 * which are considered to represent points which are the vertices of
 * the polygon. Lines are drawn between each consecutive pair, and
 * between the first pair and last pair in the array.
 *
 * @param pointArray an array of alternating x and y values which are the vertices of the polygon
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT if pointArray is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawPolygon
 */
public void fillPolygon (int[] pointArray) {
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	fillPolygonInPixels(DPIUtil.autoScaleUp(drawable, pointArray));
}

void fillPolygonInPixels (int[] pointArray) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(FILL);
	if (data.gdipGraphics != 0) {
		int mode = OS.GetPolyFillMode(handle) == OS.WINDING ? Gdip.FillModeWinding : Gdip.FillModeAlternate;
		/*
		 * GC.fillPolygon method paints at wrong coordinates when GDI+ is used, the
		 * difference is marginal, hence applying the transformation with additional
		 * 0.5f pixel correction to avoid the issue seen in bug 139791
		 */
		float offsetCorrection = 0.5f;
		Gdip.Graphics_TranslateTransform(data.gdipGraphics, data.gdipXOffset + offsetCorrection, data.gdipYOffset + offsetCorrection, Gdip.MatrixOrderPrepend);
		Gdip.Graphics_FillPolygon(data.gdipGraphics, data.gdipBrush, pointArray, pointArray.length / 2, mode);
		Gdip.Graphics_TranslateTransform(data.gdipGraphics, -(data.gdipXOffset + offsetCorrection), -(data.gdipYOffset + offsetCorrection), Gdip.MatrixOrderPrepend);
		return;
	}
	if ((data.style & SWT.MIRRORED) != 0) {
		for (int i = 0; i < pointArray.length; i+=2) {
			pointArray[i]--;
		}
	}
	OS.Polygon(handle, pointArray, pointArray.length / 2);
	if ((data.style & SWT.MIRRORED) != 0) {
		for (int i = 0; i < pointArray.length; i+=2) {
			pointArray[i]++;
		}
	}

}

/**
 * Fills the interior of the rectangle specified by the arguments,
 * using the receiver's background color.
 *
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle(int, int, int, int)
 */
public void fillRectangle (int x, int y, int width, int height) {
	x = DPIUtil.autoScaleUp (drawable, x);
	y = DPIUtil.autoScaleUp (drawable, y);
	width = DPIUtil.autoScaleUp (drawable, width);
	height = DPIUtil.autoScaleUp (drawable, height);
	fillRectangleInPixels(x, y, width, height);
}

void fillRectangleInPixels (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(FILL);
	if (data.gdipGraphics != 0) {
		if (width < 0) {
			x = x + width;
			width = -width;
		}
		if (height < 0) {
			y = y + height;
			height = -height;
		}
		Gdip.Graphics_FillRectangle(data.gdipGraphics, data.gdipBrush, x, y, width, height);
		return;
	}
	int dwRop = OS.GetROP2(handle) == OS.R2_XORPEN ? OS.PATINVERT : OS.PATCOPY;
	OS.PatBlt(handle, x, y, width, height, dwRop);
}

/**
 * Fills the interior of the specified rectangle, using the receiver's
 * background color.
 *
 * @param rect the rectangle to be filled
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle(int, int, int, int)
 */
public void fillRectangle (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	rect = DPIUtil.autoScaleUp(drawable, rect);
	fillRectangleInPixels(rect.x, rect.y, rect.width, rect.height);
}

/**
 * Fills the interior of the round-cornered rectangle specified by
 * the arguments, using the receiver's background color.
 *
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 * @param arcWidth the width of the arc
 * @param arcHeight the height of the arc
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRoundRectangle
 */
public void fillRoundRectangle (int x, int y, int width, int height, int arcWidth, int arcHeight) {
	x = DPIUtil.autoScaleUp (drawable, x);
	y = DPIUtil.autoScaleUp (drawable, y);
	width = DPIUtil.autoScaleUp (drawable, width);
	height = DPIUtil.autoScaleUp (drawable, height);
	arcWidth = DPIUtil.autoScaleUp (drawable, arcWidth);
	arcHeight = DPIUtil.autoScaleUp (drawable, arcHeight);
	fillRoundRectangleInPixels(x, y, width, height, arcWidth, arcHeight);
}

void fillRoundRectangleInPixels (int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(FILL);
	if (data.gdipGraphics != 0) {
		fillRoundRectangleGdip(data.gdipGraphics, data.gdipBrush, x, y, width, height, arcWidth, arcHeight);
		return;
	}
	if ((data.style & SWT.MIRRORED) != 0) x--;
	OS.RoundRect(handle, x,y,x+width+1,y+height+1,arcWidth, arcHeight);
}

void fillRoundRectangleGdip (long gdipGraphics, long brush, int x, int y, int width, int height, int arcWidth, int arcHeight) {
	int nx = x;
	int ny = y;
	int nw = width;
	int nh = height;
	int naw = arcWidth;
	int nah = arcHeight;

	if (nw < 0) {
		nw = 0 - nw;
		nx = nx - nw;
	}
	if (nh < 0) {
		nh = 0 - nh;
		ny = ny -nh;
	}
	if (naw < 0)
		naw = 0 - naw;
	if (nah < 0)
		nah = 0 - nah;

	if (naw == 0 || nah == 0) {
		Gdip.Graphics_FillRectangle(data.gdipGraphics, data.gdipBrush, x, y, width, height);
	} else {
		long path = Gdip.GraphicsPath_new(Gdip.FillModeAlternate);
		if (path == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		if (nw > naw) {
			if (nh > nah) {
				Gdip.GraphicsPath_AddArc(path, nx + nw - naw, ny, naw, nah, 0, -90);
				Gdip.GraphicsPath_AddArc(path, nx, ny, naw, nah, -90, -90);
				Gdip.GraphicsPath_AddArc(path, nx, ny + nh - nah, naw, nah, -180, -90);
				Gdip.GraphicsPath_AddArc(path, nx + nw - naw, ny + nh - nah, naw, nah, -270, -90);
			} else {
				Gdip.GraphicsPath_AddArc(path, nx + nw - naw, ny, naw, nh, -270, -180);
				Gdip.GraphicsPath_AddArc(path, nx, ny, naw, nh, -90, -180);
			}
		} else {
			if (nh > nah) {
				Gdip.GraphicsPath_AddArc(path, nx, ny, nw, nah, 0, -180);
				Gdip.GraphicsPath_AddArc(path, nx, ny + nh - nah, nw, nah, -180, -180);
			} else {
				Gdip.GraphicsPath_AddArc(path, nx, ny, nw, nh, 0, 360);
			}
		}
		Gdip.GraphicsPath_CloseFigure(path);
		Gdip.Graphics_FillPath(gdipGraphics, brush, path);
		Gdip.GraphicsPath_delete(path);
	}
}

void flush () {
	if (data.gdipGraphics != 0) {
		Gdip.Graphics_Flush(data.gdipGraphics, 0);
		/*
		* Note Flush() does not flush the output to the
		* underline HDC. This is done by calling GetHDC()
		* followed by ReleaseHDC().
		*/
		long hdc = Gdip.Graphics_GetHDC(data.gdipGraphics);
		Gdip.Graphics_ReleaseHDC(data.gdipGraphics, hdc);
	}
}

/**
 * Returns the <em>advance width</em> of the specified character in
 * the font which is currently selected into the receiver.
 * <p>
 * The advance width is defined as the horizontal distance the cursor
 * should move after printing the character in the selected font.
 * </p>
 *
 * @param ch the character to measure
 * @return the distance in the x direction to move past the character before painting the next
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getAdvanceWidth(char ch) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(FONT);
	int[] width = new int[1];
	OS.GetCharWidth(handle, ch, ch, width);
	return width[0];
}

/**
 * Returns <code>true</code> if receiver is using the operating system's
 * advanced graphics subsystem.  Otherwise, <code>false</code> is returned
 * to indicate that normal graphics are in use.
 * <p>
 * Advanced graphics may not be installed for the operating system.  In this
 * case, <code>false</code> is always returned.  Some operating system have
 * only one graphics subsystem.  If this subsystem supports advanced graphics,
 * then <code>true</code> is always returned.  If any graphics operation such
 * as alpha, antialias, patterns, interpolation, paths, clipping or transformation
 * has caused the receiver to switch from regular to advanced graphics mode,
 * <code>true</code> is returned.  If the receiver has been explicitly switched
 * to advanced mode and this mode is supported, <code>true</code> is returned.
 * </p>
 *
 * @return the advanced value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setAdvanced
 *
 * @since 3.1
 */
public boolean getAdvanced() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.gdipGraphics != 0;
}

/**
 * Returns the receiver's alpha value. The alpha value
 * is between 0 (transparent) and 255 (opaque).
 *
 * @return the alpha value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
public int getAlpha() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.alpha;
}

/**
 * Returns the receiver's anti-aliasing setting value, which will be
 * one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or
 * <code>SWT.ON</code>. Note that this controls anti-aliasing for all
 * <em>non-text drawing</em> operations.
 *
 * @return the anti-aliasing setting
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getTextAntialias
 *
 * @since 3.1
 */
public int getAntialias() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.gdipGraphics == 0) return SWT.DEFAULT;
	int mode = Gdip.Graphics_GetSmoothingMode(data.gdipGraphics);
	switch (mode) {
		case Gdip.SmoothingModeDefault: return SWT.DEFAULT;
		case Gdip.SmoothingModeHighSpeed:
		case Gdip.SmoothingModeNone: return SWT.OFF;
		case Gdip.SmoothingModeAntiAlias:
		case Gdip.SmoothingModeAntiAlias8x8:
		case Gdip.SmoothingModeHighQuality: return SWT.ON;
	}
	return SWT.DEFAULT;
}

/**
 * Returns the background color.
 *
 * @return the receiver's background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Color getBackground() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return Color.win32_new(data.device, data.background);
}

/**
 * Returns the background pattern. The default value is
 * <code>null</code>.
 *
 * @return the receiver's background pattern
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Pattern
 *
 * @since 3.1
 */
public Pattern getBackgroundPattern() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.backgroundPattern;
}

/**
 * Returns the width of the specified character in the font
 * selected into the receiver.
 * <p>
 * The width is defined as the space taken up by the actual
 * character, not including the leading and tailing whitespace
 * or overhang.
 * </p>
 *
 * @param ch the character to measure
 * @return the width of the character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getCharWidth(char ch) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(FONT);

	/* GetCharABCWidths only succeeds on truetype fonts */
	int[] width = new int[3];
	if (OS.GetCharABCWidths(handle, ch, ch, width)) {
		return width[1];
	}

	/* It wasn't a truetype font */
	TEXTMETRIC lptm = new TEXTMETRIC();
	OS.GetTextMetrics(handle, lptm);
	SIZE size = new SIZE();
	OS.GetTextExtentPoint32(handle, new char[]{ch}, 1, size);
	return size.cx - lptm.tmOverhang;
}

/**
 * Returns the bounding rectangle of the receiver's clipping
 * region. If no clipping region is set, the return value
 * will be a rectangle which covers the entire bounds of the
 * object the receiver is drawing on.
 *
 * @return the bounding rectangle of the clipping region
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Rectangle getClipping () {
	return DPIUtil.autoScaleDown(drawable, getClippingInPixels());
}

Rectangle getClippingInPixels() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		Rect rect = new Rect();
		Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeNone);
		Gdip.Graphics_GetVisibleClipBounds(gdipGraphics, rect);
		Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeHalf);
		return new Rectangle(rect.X, rect.Y, rect.Width, rect.Height);
	}
	RECT rect = new RECT();
	OS.GetClipBox(handle, rect);
	return new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
}

/**
 * Sets the region managed by the argument to the current
 * clipping region of the receiver.
 *
 * @param region the region to fill with the clipping region
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the region is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the region is disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void getClipping (Region region) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		long rgn = Gdip.Region_new();
		Gdip.Graphics_GetClip(data.gdipGraphics, rgn);
		if (Gdip.Region_IsInfinite(rgn, gdipGraphics)) {
			Rect rect = new Rect();
			Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeNone);
			Gdip.Graphics_GetVisibleClipBounds(gdipGraphics, rect);
			Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeHalf);
			OS.SetRectRgn(region.handle, rect.X, rect.Y, rect.X + rect.Width, rect.Y + rect.Height);
		} else {
			long matrix = Gdip.Matrix_new(1, 0, 0, 1, 0, 0);
			long identity = Gdip.Matrix_new(1, 0, 0, 1, 0, 0);
			Gdip.Graphics_GetTransform(gdipGraphics, matrix);
			Gdip.Graphics_SetTransform(gdipGraphics, identity);
			long hRgn = Gdip.Region_GetHRGN(rgn, data.gdipGraphics);
			Gdip.Graphics_SetTransform(gdipGraphics, matrix);
			Gdip.Matrix_delete(identity);
			Gdip.Matrix_delete(matrix);
			POINT pt = new POINT ();
			OS.GetWindowOrgEx (handle, pt);
			OS.OffsetRgn (hRgn, pt.x, pt.y);
			OS.CombineRgn(region.handle, hRgn, 0, OS.RGN_COPY);
			OS.DeleteObject(hRgn);
		}
		Gdip.Region_delete(rgn);
		return;
	}
	POINT pt = new POINT ();
	OS.GetWindowOrgEx (handle, pt);
	int result = OS.GetClipRgn (handle, region.handle);
	if (result != 1) {
		RECT rect = new RECT();
		OS.GetClipBox(handle, rect);
		OS.SetRectRgn(region.handle, rect.left, rect.top, rect.right, rect.bottom);
	} else {
		OS.OffsetRgn (region.handle, pt.x, pt.y);
	}
	long metaRgn = OS.CreateRectRgn (0, 0, 0, 0);
	if (OS.GetMetaRgn (handle, metaRgn) != 0) {
		OS.OffsetRgn (metaRgn, pt.x, pt.y);
		OS.CombineRgn (region.handle, metaRgn, region.handle, OS.RGN_AND);
	}
	OS.DeleteObject(metaRgn);
	long hwnd = data.hwnd;
	if (hwnd != 0 && data.ps != null) {
		long sysRgn = OS.CreateRectRgn (0, 0, 0, 0);
		if (OS.GetRandomRgn (handle, sysRgn, OS.SYSRGN) == 1) {
			if ((OS.GetLayout(handle) & OS.LAYOUT_RTL) != 0) {
				int nBytes = OS.GetRegionData (sysRgn, 0, null);
				int [] lpRgnData = new int [nBytes / 4];
				OS.GetRegionData (sysRgn, nBytes, lpRgnData);
				long newSysRgn = OS.ExtCreateRegion(new float [] {-1, 0, 0, 1, 0, 0}, nBytes, lpRgnData);
				OS.DeleteObject(sysRgn);
				sysRgn = newSysRgn;
			}
			OS.MapWindowPoints (0, hwnd, pt, 1);
			OS.OffsetRgn (sysRgn, pt.x, pt.y);
			OS.CombineRgn (region.handle, sysRgn, region.handle, OS.RGN_AND);
		}
		OS.DeleteObject(sysRgn);
	}
}

long getFgBrush() {
	return data.foregroundPattern != null ? data.foregroundPattern.handle : data.gdipFgBrush;
}

/**
 * Returns the receiver's fill rule, which will be one of
 * <code>SWT.FILL_EVEN_ODD</code> or <code>SWT.FILL_WINDING</code>.
 *
 * @return the receiver's fill rule
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
public int getFillRule() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return OS.GetPolyFillMode(handle) == OS.WINDING ? SWT.FILL_WINDING : SWT.FILL_EVEN_ODD;
}

/**
 * Returns the font currently being used by the receiver
 * to draw and measure text.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Font getFont () {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.font;
}

/**
 * Returns a FontMetrics which contains information
 * about the font currently being used by the receiver
 * to draw and measure text.
 *
 * @return font metrics for the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public FontMetrics getFontMetrics() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(FONT);
	TEXTMETRIC lptm = new TEXTMETRIC();
	OS.GetTextMetrics(handle, lptm);
	return FontMetrics.win32_new(lptm);
}

/**
 * Returns the receiver's foreground color.
 *
 * @return the color used for drawing foreground things
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Color getForeground() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return Color.win32_new(data.device, data.foreground);
}

/**
 * Returns the foreground pattern. The default value is
 * <code>null</code>.
 *
 * @return the receiver's foreground pattern
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Pattern
 *
 * @since 3.1
 */
public Pattern getForegroundPattern() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.foregroundPattern;
}

/**
 * Returns the GCData.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>GC</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @return the receiver's GCData
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see GCData
 *
 * @noreference This method is not intended to be referenced by clients.
 *
 * @since 3.2
 */
public GCData getGCData() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data;
}

/**
 * Returns the receiver's interpolation setting, which will be one of
 * <code>SWT.DEFAULT</code>, <code>SWT.NONE</code>,
 * <code>SWT.LOW</code> or <code>SWT.HIGH</code>.
 *
 * @return the receiver's interpolation setting
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
public int getInterpolation() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.gdipGraphics == 0) return SWT.DEFAULT;
	int mode = Gdip.Graphics_GetInterpolationMode(data.gdipGraphics);
	switch (mode) {
		case Gdip.InterpolationModeDefault: return SWT.DEFAULT;
		case Gdip.InterpolationModeNearestNeighbor: return SWT.NONE;
		case Gdip.InterpolationModeBilinear:
		case Gdip.InterpolationModeLowQuality: return SWT.LOW;
		case Gdip.InterpolationModeBicubic:
		case Gdip.InterpolationModeHighQualityBilinear:
		case Gdip.InterpolationModeHighQualityBicubic:
		case Gdip.InterpolationModeHighQuality: return SWT.HIGH;
	}
	return SWT.DEFAULT;
}

/**
 * Returns the receiver's line attributes.
 *
 * @return the line attributes used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.3
 */
public LineAttributes getLineAttributes () {
	LineAttributes attributes = getLineAttributesInPixels();
	attributes.width = DPIUtil.autoScaleDown(drawable, attributes.width);
	if(attributes.dash != null) {
		attributes.dash = DPIUtil.autoScaleDown(drawable, attributes.dash);
	}
	return attributes;
}

LineAttributes getLineAttributesInPixels () {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	float[] dashes = null;
	if (data.lineDashes != null) {
		dashes = new float[data.lineDashes.length];
		System.arraycopy(data.lineDashes, 0, dashes, 0, dashes.length);
	}
	return new LineAttributes(data.lineWidth, data.lineCap, data.lineJoin, data.lineStyle, dashes, data.lineDashesOffset, data.lineMiterLimit);
}

/**
 * Returns the receiver's line cap style, which will be one
 * of the constants <code>SWT.CAP_FLAT</code>, <code>SWT.CAP_ROUND</code>,
 * or <code>SWT.CAP_SQUARE</code>.
 *
 * @return the cap style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
public int getLineCap() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.lineCap;
}

/**
 * Returns the receiver's line dash style. The default value is
 * <code>null</code>.
 *
 * @return the line dash style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
public int[] getLineDash() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.lineDashes == null) return null;
	int[] lineDashes = new int[data.lineDashes.length];
	for (int i = 0; i < lineDashes.length; i++) {
		lineDashes[i] = DPIUtil.autoScaleDown(drawable, (int)data.lineDashes[i]);
	}
	return lineDashes;
}

/**
 * Returns the receiver's line join style, which will be one
 * of the constants <code>SWT.JOIN_MITER</code>, <code>SWT.JOIN_ROUND</code>,
 * or <code>SWT.JOIN_BEVEL</code>.
 *
 * @return the join style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
public int getLineJoin() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.lineJoin;
}

/**
 * Returns the receiver's line style, which will be one
 * of the constants <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
 * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
 * <code>SWT.LINE_DASHDOTDOT</code>.
 *
 * @return the style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getLineStyle() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.lineStyle;
}

/**
 * Returns the width that will be used when drawing lines
 * for all of the figure drawing operations (that is,
 * <code>drawLine</code>, <code>drawRectangle</code>,
 * <code>drawPolyline</code>, and so forth.
 *
 * @return the receiver's line width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getLineWidth () {
	return DPIUtil.autoScaleDown(drawable, getLineWidthInPixels());
}

int getLineWidthInPixels() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return (int)data.lineWidth;
}

/**
 * Returns the receiver's style information.
 * <p>
 * Note that the value which is returned by this method <em>may
 * not match</em> the value which was provided to the constructor
 * when the receiver was created. This can occur when the underlying
 * operating system does not support a particular combination of
 * requested styles.
 * </p>
 *
 * @return the style bits
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 2.1.2
 */
public int getStyle () {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.style;
}

/**
 * Returns the receiver's text drawing anti-aliasing setting value,
 * which will be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or
 * <code>SWT.ON</code>. Note that this controls anti-aliasing
 * <em>only</em> for text drawing operations.
 *
 * @return the anti-aliasing setting
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getAntialias
 *
 * @since 3.1
 */
public int getTextAntialias() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.gdipGraphics == 0) return SWT.DEFAULT;
	int mode = Gdip.Graphics_GetTextRenderingHint(data.gdipGraphics);
	switch (mode) {
		case Gdip.TextRenderingHintSystemDefault: return SWT.DEFAULT;
		case Gdip.TextRenderingHintSingleBitPerPixel:
		case Gdip.TextRenderingHintSingleBitPerPixelGridFit: return SWT.OFF;
		case Gdip.TextRenderingHintAntiAlias:
		case Gdip.TextRenderingHintAntiAliasGridFit:
		case Gdip.TextRenderingHintClearTypeGridFit: return SWT.ON;
	}
	return SWT.DEFAULT;
}

/**
 * Sets the parameter to the transform that is currently being
 * used by the receiver.
 *
 * @param transform the destination to copy the transform into
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Transform
 *
 * @since 3.1
 */
public void getTransform(Transform transform) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (transform == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (transform.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		Gdip.Graphics_GetTransform(gdipGraphics, transform.handle);
		long identity = identity();
		Gdip.Matrix_Invert(identity);
		Gdip.Matrix_Multiply(transform.handle, identity, Gdip.MatrixOrderAppend);
		Gdip.Matrix_delete(identity);
	} else {
		transform.setElements(1, 0, 0, 1, 0, 0);
	}
}

/**
 * Returns <code>true</code> if this GC is drawing in the mode
 * where the resulting color in the destination is the
 * <em>exclusive or</em> of the color values in the source
 * and the destination, and <code>false</code> if it is
 * drawing in the mode where the destination color is being
 * replaced with the source color value.
 *
 * @return <code>true</code> true if the receiver is in XOR mode, and false otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean getXORMode() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return OS.GetROP2(handle) == OS.R2_XORPEN;
}

void initGdip() {
	data.device.checkGDIP();
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) return;
	/*
	* Feature in GDI+. The GDI+ clipping set with Graphics->SetClip()
	* is always intersected with the GDI clipping at the time the
	* GDI+ graphics is created.  This means that the clipping
	* cannot be reset.  The fix is to clear the clipping before
	* the GDI+ graphics is created and reset it afterwards.
	*/
	long hRgn = OS.CreateRectRgn(0, 0, 0, 0);
	int result = OS.GetClipRgn(handle, hRgn);
	POINT pt = new POINT ();
	OS.GetWindowOrgEx (handle, pt);
	OS.OffsetRgn (hRgn, pt.x, pt.y);
	OS.SelectClipRgn(handle, 0);

	/*
	* Bug in GDI+.  GDI+ does not work when the HDC layout is RTL.  There
	* are many issues like pixel corruption, but the most visible problem
	* is that it does not have an effect when drawing to an bitmap.  The
	* fix is to clear the bit before creating the GDI+ graphics and install
	* a mirroring matrix ourselves.
	*/
	if ((data.style & SWT.MIRRORED) != 0) {
		OS.SetLayout(handle, OS.GetLayout(handle) & ~OS.LAYOUT_RTL);
	}

	gdipGraphics = data.gdipGraphics = Gdip.Graphics_new(handle);
	if (gdipGraphics == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	Gdip.Graphics_SetPageUnit(gdipGraphics, Gdip.UnitPixel);
	Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeHalf);
	if ((data.style & SWT.MIRRORED) != 0) {
		long matrix = identity();
		Gdip.Graphics_SetTransform(gdipGraphics, matrix);
		Gdip.Matrix_delete(matrix);
	}
	if (result == 1) setClipping(hRgn);
	OS.DeleteObject(hRgn);
	data.state = 0;
	if (data.hPen != 0) {
		OS.SelectObject(handle, OS.GetStockObject(OS.NULL_PEN));
		OS.DeleteObject(data.hPen);
		data.hPen = 0;
	}
	if (data.hBrush != 0) {
		OS.SelectObject(handle, OS.GetStockObject(OS.NULL_BRUSH));
		OS.DeleteObject(data.hBrush);
		data.hBrush = 0;
	}
}

long identity() {
	if ((data.style & SWT.MIRRORED) != 0) {
		int width = 0;
		int technology = OS.GetDeviceCaps(handle, OS.TECHNOLOGY);
		if (technology == OS.DT_RASPRINTER) {
			width = OS.GetDeviceCaps(handle, OS.PHYSICALWIDTH);
		} else {
			Image image = data.image;
			if (image != null) {
				BITMAP bm = new BITMAP();
				OS.GetObject(image.handle, BITMAP.sizeof, bm);
				width = bm.bmWidth;
			} else {
				long hwnd = OS.WindowFromDC(handle);
				if (hwnd != 0) {
					RECT rect = new RECT();
					OS.GetClientRect(hwnd, rect);
					width = rect.right - rect.left;
				} else {
					long hBitmap = OS.GetCurrentObject(handle, OS.OBJ_BITMAP);
					BITMAP bm = new BITMAP();
					OS.GetObject(hBitmap, BITMAP.sizeof, bm);
					width = bm.bmWidth;
				}
			}
		}
		POINT pt = new POINT ();
		OS.GetWindowOrgEx (handle, pt);
		return Gdip.Matrix_new(-1, 0, 0, 1, width + 2 * pt.x, 0);
	}
	return Gdip.Matrix_new(1, 0, 0, 1, 0, 0);
}

void init(Drawable drawable, GCData data, long hDC) {
	int foreground = data.foreground;
	if (foreground != -1) {
		data.state &= ~(FOREGROUND | FOREGROUND_TEXT | PEN);
	} else {
		data.foreground = OS.GetTextColor(hDC);
	}
	int background = data.background;
	if (background != -1) {
		data.state &= ~(BACKGROUND | BACKGROUND_TEXT | BRUSH);
	} else {
		data.background = OS.GetBkColor(hDC);
	}
	data.state &= ~(NULL_BRUSH | NULL_PEN);
	Font font = data.font;
	if (font != null) {
		data.state &= ~FONT;
	} else {
		data.font = Font.win32_new(device, OS.GetCurrentObject(hDC, OS.OBJ_FONT));
	}
	Image image = data.image;
	if (image != null) {
		data.hNullBitmap = OS.SelectObject(hDC, image.handle);
		image.memGC = this;
	}
	int layout = data.layout;
	if (layout != -1) {
		int flags = OS.GetLayout(hDC);
		if ((flags & OS.LAYOUT_RTL) != (layout & OS.LAYOUT_RTL)) {
			flags &= ~OS.LAYOUT_RTL;
			OS.SetLayout(hDC, flags | layout);
		}
		if ((data.style & SWT.RIGHT_TO_LEFT) != 0) data.style |= SWT.MIRRORED;
	}
	this.drawable = drawable;
	this.data = data;
	handle = hDC;
}

/**
 * Returns an integer hash code for the receiver. Any two
 * objects that return <code>true</code> when passed to
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #equals
 */
@Override
public int hashCode () {
	return (int)handle;
}

/**
 * Returns <code>true</code> if the receiver has a clipping
 * region set into it, and <code>false</code> otherwise.
 * If this method returns false, the receiver will draw on all
 * available space in the destination. If it returns true,
 * it will draw only in the area that is covered by the region
 * that can be accessed with <code>getClipping(region)</code>.
 *
 * @return <code>true</code> if the GC has a clipping region, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean isClipped() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		long rgn = Gdip.Region_new();
		Gdip.Graphics_GetClip(data.gdipGraphics, rgn);
		boolean isInfinite = Gdip.Region_IsInfinite(rgn, gdipGraphics);
		Gdip.Region_delete(rgn);
		return !isInfinite;
	}
	long region = OS.CreateRectRgn(0, 0, 0, 0);
	int result = OS.GetClipRgn(handle, region);
	OS.DeleteObject(region);
	return result > 0;
}

/**
 * Returns <code>true</code> if the GC has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the GC.
 * When a GC has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the GC.
 *
 * @return <code>true</code> when the GC is disposed and <code>false</code> otherwise
 */
@Override
public boolean isDisposed() {
	return handle == 0;
}

float measureSpace(long font, long format) {
	PointF pt = new PointF();
	RectF bounds = new RectF();
	Gdip.Graphics_MeasureString(data.gdipGraphics, new char[]{' '}, 1, font, pt, format, bounds);
	return bounds.Width;
}

/**
 * Sets the receiver to always use the operating system's advanced graphics
 * subsystem for all graphics operations if the argument is <code>true</code>.
 * If the argument is <code>false</code>, the advanced graphics subsystem is
 * no longer used, advanced graphics state is cleared and the normal graphics
 * subsystem is used from now on.
 * <p>
 * Normally, the advanced graphics subsystem is invoked automatically when
 * any one of the alpha, antialias, patterns, interpolation, paths, clipping
 * or transformation operations in the receiver is requested.  When the receiver
 * is switched into advanced mode, the advanced graphics subsystem performs both
 * advanced and normal graphics operations.  Because the two subsystems are
 * different, their output may differ.  Switching to advanced graphics before
 * any graphics operations are performed ensures that the output is consistent.
 * </p><p>
 * Advanced graphics may not be installed for the operating system.  In this
 * case, this operation does nothing.  Some operating system have only one
 * graphics subsystem, so switching from normal to advanced graphics does
 * nothing.  However, switching from advanced to normal graphics will always
 * clear the advanced graphics state, even for operating systems that have
 * only one graphics subsystem.
 * </p>
 *
 * @param advanced the new advanced graphics state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setAlpha
 * @see #setAntialias
 * @see #setBackgroundPattern
 * @see #setClipping(Path)
 * @see #setForegroundPattern
 * @see #setLineAttributes
 * @see #setInterpolation
 * @see #setTextAntialias
 * @see #setTransform
 * @see #getAdvanced
 *
 * @since 3.1
 */
public void setAdvanced(boolean advanced) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (advanced && data.gdipGraphics != 0) return;
	if (advanced) {
		initGdip();
	} else {
		disposeGdip();
		data.alpha = 0xFF;
		data.backgroundPattern = data.foregroundPattern = null;
		data.state = 0;
		setClipping(0);
		if ((data.style & SWT.MIRRORED) != 0) {
			OS.SetLayout(handle, OS.GetLayout(handle) | OS.LAYOUT_RTL);
		}
	}
}

/**
 * Sets the receiver's anti-aliasing value to the parameter,
 * which must be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code>
 * or <code>SWT.ON</code>. Note that this controls anti-aliasing for all
 * <em>non-text drawing</em> operations.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 *
 * @param antialias the anti-aliasing setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is not one of <code>SWT.DEFAULT</code>,
 *                                 <code>SWT.OFF</code> or <code>SWT.ON</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 *
 * @see #getAdvanced
 * @see #setAdvanced
 * @see #setTextAntialias
 *
 * @since 3.1
 */
public void setAntialias(int antialias) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.gdipGraphics == 0 && antialias == SWT.DEFAULT) return;
	int mode = 0;
	switch (antialias) {
		case SWT.DEFAULT:
			mode = Gdip.SmoothingModeDefault;
			break;
		case SWT.OFF:
			mode = Gdip.SmoothingModeNone;
			break;
		case SWT.ON:
			mode = Gdip.SmoothingModeAntiAlias;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	initGdip();
	Gdip.Graphics_SetSmoothingMode(data.gdipGraphics, mode);
}

/**
 * Sets the receiver's alpha value which must be
 * between 0 (transparent) and 255 (opaque).
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * @param alpha the alpha value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 *
 * @see #getAdvanced
 * @see #setAdvanced
 *
 * @since 3.1
 */
public void setAlpha(int alpha) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.gdipGraphics == 0 && (alpha & 0xFF) == 0xFF) return;
	initGdip();
	data.alpha = alpha & 0xFF;
	data.state &= ~(BACKGROUND | FOREGROUND);
	if(data.gdipFgPatternBrushAlpha != 0) {
		Gdip.TextureBrush_delete(data.gdipFgPatternBrushAlpha);
		data.gdipFgPatternBrushAlpha = 0;
	}
	if(data.gdipBgPatternBrushAlpha != 0) {
		Gdip.TextureBrush_delete(data.gdipBgPatternBrushAlpha);
		data.gdipBgPatternBrushAlpha = 0;
	}
}

/**
 * Sets the background color. The background color is used
 * for fill operations and as the background color when text
 * is drawn.
 *
 * @param color the new background color for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the color is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the color has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setBackground (Color color) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (data.backgroundPattern == null && data.background == color.handle) return;
	data.backgroundPattern = null;
	data.background = color.handle;
	data.state &= ~(BACKGROUND | BACKGROUND_TEXT);
}

/**
 * Sets the background pattern. The default value is <code>null</code>.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 *
 * @param pattern the new background pattern
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 *
 * @see Pattern
 * @see #getAdvanced
 * @see #setAdvanced
 *
 * @since 3.1
 */
public void setBackgroundPattern (Pattern pattern) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pattern != null && pattern.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (data.gdipGraphics == 0 && pattern == null) return;
	initGdip();
	if (data.backgroundPattern == pattern) return;
	data.backgroundPattern = pattern;
	data.state &= ~BACKGROUND;
	if(data.gdipBgPatternBrushAlpha != 0) {
		Gdip.TextureBrush_delete(data.gdipBgPatternBrushAlpha);
		data.gdipBgPatternBrushAlpha = 0;
	}
}

void setClipping(long clipRgn) {
	long hRgn = clipRgn;
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		if (hRgn != 0) {
			long region = Gdip.Region_new(hRgn);
			Gdip.Graphics_SetClip(gdipGraphics, region, Gdip.CombineModeReplace);
			Gdip.Region_delete(region);
		} else {
			Gdip.Graphics_ResetClip(gdipGraphics);
		}
	} else {
		POINT pt = null;
		if (hRgn != 0) {
			pt = new POINT();
			OS.GetWindowOrgEx(handle, pt);
			OS.OffsetRgn(hRgn, -pt.x, -pt.y);
		}
		OS.SelectClipRgn(handle, hRgn);
		if (hRgn != 0) {
			OS.OffsetRgn(hRgn, pt.x, pt.y);
		}
	}
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the rectangular area specified
 * by the arguments.
 *
 * @param x the x coordinate of the clipping rectangle
 * @param y the y coordinate of the clipping rectangle
 * @param width the width of the clipping rectangle
 * @param height the height of the clipping rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping (int x, int y, int width, int height) {
	x = DPIUtil.autoScaleUp(drawable, x);
	y = DPIUtil.autoScaleUp(drawable, y);
	width = DPIUtil.autoScaleUp(drawable, width);
	height = DPIUtil.autoScaleUp(drawable, height);
	setClippingInPixels(x, y, width, height);
}

void setClippingInPixels (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	long hRgn = OS.CreateRectRgn(x, y, x + width, y + height);
	setClipping(hRgn);
	OS.DeleteObject(hRgn);
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the path specified
 * by the argument.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 *
 * @param path the clipping path.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the path has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 *
 * @see Path
 * @see #getAdvanced
 * @see #setAdvanced
 *
 * @since 3.1
 */
public void setClipping (Path path) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path != null && path.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	setClipping(0);
	if (path != null) {
		initGdip();
		int mode = OS.GetPolyFillMode(handle) == OS.WINDING ? Gdip.FillModeWinding : Gdip.FillModeAlternate;
		Gdip.GraphicsPath_SetFillMode(path.handle, mode);
		Gdip.Graphics_SetClipPath(data.gdipGraphics, path.handle);
	}
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the rectangular area specified
 * by the argument.  Specifying <code>null</code> for the
 * rectangle reverts the receiver's clipping area to its
 * original value.
 *
 * @param rect the clipping rectangle or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping (Rectangle rect) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) {
		setClipping(0);
	}
	else {
		rect = DPIUtil.autoScaleUp(drawable, rect);
		setClippingInPixels(rect.x, rect.y, rect.width, rect.height);
	}
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the region specified
 * by the argument.  Specifying <code>null</code> for the
 * region reverts the receiver's clipping area to its
 * original value.
 *
 * @param region the clipping region or <code>null</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the region has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping (Region region) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region != null && region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	setClipping(region != null ? region.handle : 0);
}

/**
 * Sets the receiver's fill rule to the parameter, which must be one of
 * <code>SWT.FILL_EVEN_ODD</code> or <code>SWT.FILL_WINDING</code>.
 *
 * @param rule the new fill rule
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rule is not one of <code>SWT.FILL_EVEN_ODD</code>
 *                                 or <code>SWT.FILL_WINDING</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
public void setFillRule(int rule) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int mode = OS.ALTERNATE;
	switch (rule) {
		case SWT.FILL_WINDING: mode = OS.WINDING; break;
		case SWT.FILL_EVEN_ODD: mode = OS.ALTERNATE; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	OS.SetPolyFillMode(handle, mode);
}

/**
 * Sets the font which will be used by the receiver
 * to draw and measure text to the argument. If the
 * argument is null, then a default font appropriate
 * for the platform will be used instead.
 *
 * @param font the new font for the receiver, or null to indicate a default font
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the font has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setFont (Font font) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.font = font != null ? font : data.device.systemFont;
	data.state &= ~FONT;
}

/**
 * Sets the foreground color. The foreground color is used
 * for drawing operations including when text is drawn.
 *
 * @param color the new foreground color for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the color is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the color has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setForeground (Color color) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (data.foregroundPattern == null && color.handle == data.foreground) return;
	data.foregroundPattern = null;
	data.foreground = color.handle;
	data.state &= ~(FOREGROUND | FOREGROUND_TEXT);
}

/**
 * Sets the foreground pattern. The default value is <code>null</code>.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * @param pattern the new foreground pattern
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 *
 * @see Pattern
 * @see #getAdvanced
 * @see #setAdvanced
 *
 * @since 3.1
 */
public void setForegroundPattern (Pattern pattern) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pattern != null && pattern.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (data.gdipGraphics == 0 && pattern == null) return;
	initGdip();
	if (data.foregroundPattern == pattern) return;
	data.foregroundPattern = pattern;
	data.state &= ~FOREGROUND;
	if(data.gdipFgPatternBrushAlpha != 0) {
		Gdip.TextureBrush_delete(data.gdipFgPatternBrushAlpha);
		data.gdipFgPatternBrushAlpha = 0;
	}
}

/**
 * Sets the receiver's interpolation setting to the parameter, which
 * must be one of <code>SWT.DEFAULT</code>, <code>SWT.NONE</code>,
 * <code>SWT.LOW</code> or <code>SWT.HIGH</code>.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 *
 * @param interpolation the new interpolation setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rule is not one of <code>SWT.DEFAULT</code>,
 *                                 <code>SWT.NONE</code>, <code>SWT.LOW</code> or <code>SWT.HIGH</code>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 *
 * @see #getAdvanced
 * @see #setAdvanced
 *
 * @since 3.1
 */
public void setInterpolation(int interpolation) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.gdipGraphics == 0 && interpolation == SWT.DEFAULT) return;
	int mode = 0;
	switch (interpolation) {
		case SWT.DEFAULT: mode = Gdip.InterpolationModeDefault; break;
		case SWT.NONE: mode = Gdip.InterpolationModeNearestNeighbor; break;
		case SWT.LOW: mode = Gdip.InterpolationModeLowQuality; break;
		case SWT.HIGH: mode = Gdip.InterpolationModeHighQuality; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	initGdip();
	Gdip.Graphics_SetInterpolationMode(data.gdipGraphics, mode);
}

/**
 * Sets the receiver's line attributes.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * @param attributes the line attributes
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the attributes is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the line attributes is not valid</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 *
 * @see LineAttributes
 * @see #getAdvanced
 * @see #setAdvanced
 *
 * @since 3.3
 */
public void setLineAttributes (LineAttributes attributes) {
	if (attributes == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	attributes.width = DPIUtil.autoScaleUp(drawable, attributes.width);
	setLineAttributesInPixels(attributes);
}

void setLineAttributesInPixels (LineAttributes attributes) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int mask = 0;
	float lineWidth = attributes.width;
	if (lineWidth != data.lineWidth) {
		mask |= LINE_WIDTH | DRAW_OFFSET;
	}
	int lineStyle = attributes.style;
	if (lineStyle != data.lineStyle) {
		mask |= LINE_STYLE;
		switch (lineStyle) {
			case SWT.LINE_SOLID:
			case SWT.LINE_DASH:
			case SWT.LINE_DOT:
			case SWT.LINE_DASHDOT:
			case SWT.LINE_DASHDOTDOT:
				break;
			case SWT.LINE_CUSTOM:
				if (attributes.dash == null) lineStyle = SWT.LINE_SOLID;
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	int join = attributes.join;
	if (join != data.lineJoin) {
		mask |= LINE_JOIN;
		switch (join) {
			case SWT.JOIN_MITER:
			case SWT.JOIN_ROUND:
			case SWT.JOIN_BEVEL:
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	int cap = attributes.cap;
	if (cap != data.lineCap) {
		mask |= LINE_CAP;
		switch (cap) {
			case SWT.CAP_FLAT:
			case SWT.CAP_ROUND:
			case SWT.CAP_SQUARE:
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	float[] dashes = attributes.dash;
	float[] lineDashes = data.lineDashes;
	if (dashes != null && dashes.length > 0) {
		boolean changed = lineDashes == null || lineDashes.length != dashes.length;
		for (int i = 0; i < dashes.length; i++) {
			float dash = dashes[i];
			if (dash <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			if (!changed && lineDashes[i] != dash) changed = true;
		}
		if (changed) {
			float[] newDashes = new float[dashes.length];
			for (int i = 0; i < newDashes.length; i++) {
				newDashes[i] = DPIUtil.autoScaleUp(drawable, dashes[i]);
			}
			dashes = newDashes;
			mask |= LINE_STYLE;
		} else {
			dashes = lineDashes;
		}
	} else {
		if (lineDashes != null && lineDashes.length > 0) {
			mask |= LINE_STYLE;
		} else {
			dashes = lineDashes;
		}
	}
	float dashOffset = attributes.dashOffset;
	if (dashOffset != data.lineDashesOffset) {
		mask |= LINE_STYLE;
	}
	float miterLimit = attributes.miterLimit;
	if (miterLimit != data.lineMiterLimit) {
		mask |= LINE_MITERLIMIT;
	}
	initGdip();
	if (mask == 0) return;
	data.lineWidth = lineWidth;
	data.lineStyle = lineStyle;
	data.lineCap = cap;
	data.lineJoin = join;
	data.lineDashes = dashes;
	data.lineDashesOffset = dashOffset;
	data.lineMiterLimit = miterLimit;
	data.state &= ~mask;
}

/**
 * Sets the receiver's line cap style to the argument, which must be one
 * of the constants <code>SWT.CAP_FLAT</code>, <code>SWT.CAP_ROUND</code>,
 * or <code>SWT.CAP_SQUARE</code>.
 *
 * @param cap the cap style to be used for drawing lines
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the style is not valid</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
public void setLineCap(int cap) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.lineCap == cap) return;
	switch (cap) {
		case SWT.CAP_ROUND:
		case SWT.CAP_FLAT:
		case SWT.CAP_SQUARE:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineCap = cap;
	data.state &= ~LINE_CAP;
}

/**
 * Sets the receiver's line dash style to the argument. The default
 * value is <code>null</code>. If the argument is not <code>null</code>,
 * the receiver's line style is set to <code>SWT.LINE_CUSTOM</code>, otherwise
 * it is set to <code>SWT.LINE_SOLID</code>.
 *
 * @param dashes the dash style to be used for drawing lines
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the values in the array is less than or equal 0</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
public void setLineDash(int[] dashes) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	float[] lineDashes = data.lineDashes;
	if (dashes != null && dashes.length > 0) {
		boolean changed = data.lineStyle != SWT.LINE_CUSTOM || lineDashes == null || lineDashes.length != dashes.length;
		float[] newDashes = new float[dashes.length];
		for (int i = 0; i < dashes.length; i++) {
			if (dashes[i] <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			newDashes[i] = DPIUtil.autoScaleUp(drawable, (float) dashes[i]);
			if (!changed && lineDashes[i] != newDashes[i]) changed = true;
		}
		if (!changed) return;
		data.lineDashes = newDashes;
		data.lineStyle = SWT.LINE_CUSTOM;
	} else {
		if (data.lineStyle == SWT.LINE_SOLID && (lineDashes == null || lineDashes.length == 0)) return;
		data.lineDashes = null;
		data.lineStyle = SWT.LINE_SOLID;
	}
	data.state &= ~LINE_STYLE;
}

/**
 * Sets the receiver's line join style to the argument, which must be one
 * of the constants <code>SWT.JOIN_MITER</code>, <code>SWT.JOIN_ROUND</code>,
 * or <code>SWT.JOIN_BEVEL</code>.
 *
 * @param join the join style to be used for drawing lines
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the style is not valid</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
public void setLineJoin(int join) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.lineJoin == join) return;
	switch (join) {
		case SWT.JOIN_MITER:
		case SWT.JOIN_ROUND:
		case SWT.JOIN_BEVEL:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineJoin = join;
	data.state &= ~LINE_JOIN;
}

/**
 * Sets the receiver's line style to the argument, which must be one
 * of the constants <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
 * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
 * <code>SWT.LINE_DASHDOTDOT</code>.
 *
 * @param lineStyle the style to be used for drawing lines
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the style is not valid</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setLineStyle(int lineStyle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.lineStyle == lineStyle) return;
	switch (lineStyle) {
		case SWT.LINE_SOLID:
		case SWT.LINE_DASH:
		case SWT.LINE_DOT:
		case SWT.LINE_DASHDOT:
		case SWT.LINE_DASHDOTDOT:
			break;
		case SWT.LINE_CUSTOM:
			if (data.lineDashes == null) lineStyle = SWT.LINE_SOLID;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineStyle = lineStyle;
	data.state &= ~LINE_STYLE;
}

/**
 * Sets the width that will be used when drawing lines
 * for all of the figure drawing operations (that is,
 * <code>drawLine</code>, <code>drawRectangle</code>,
 * <code>drawPolyline</code>, and so forth.
 * <p>
 * Note that line width of zero is used as a hint to
 * indicate that the fastest possible line drawing
 * algorithms should be used. This means that the
 * output may be different from line width one and
 * specially at high DPI it's not recommended to mix
 * line width zero with other line widths.
 * </p>
 *
 * @param lineWidth the width of a line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setLineWidth(int lineWidth) {
	lineWidth = DPIUtil.autoScaleUp (drawable, lineWidth);
	setLineWidthInPixels(lineWidth);
}

void setLineWidthInPixels(int lineWidth) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.lineWidth == lineWidth) return;
	data.lineWidth = lineWidth;
	data.state &= ~(LINE_WIDTH | DRAW_OFFSET);
}

/**
 * If the argument is <code>true</code>, puts the receiver
 * in a drawing mode where the resulting color in the destination
 * is the <em>exclusive or</em> of the color values in the source
 * and the destination, and if the argument is <code>false</code>,
 * puts the receiver in a drawing mode where the destination color
 * is replaced with the source color value.
 *
 * @param xor if <code>true</code>, then <em>xor</em> mode is used, otherwise <em>source copy</em> mode is used
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setXORMode(boolean xor) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.SetROP2(handle, xor ? OS.R2_XORPEN : OS.R2_COPYPEN);
}

/**
 * Sets the receiver's text anti-aliasing value to the parameter,
 * which must be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code>
 * or <code>SWT.ON</code>. Note that this controls anti-aliasing only
 * for all <em>text drawing</em> operations.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 *
 * @param antialias the anti-aliasing setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is not one of <code>SWT.DEFAULT</code>,
 *                                 <code>SWT.OFF</code> or <code>SWT.ON</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 *
 * @see #getAdvanced
 * @see #setAdvanced
 * @see #setAntialias
 *
 * @since 3.1
 */
public void setTextAntialias(int antialias) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.gdipGraphics == 0 && antialias == SWT.DEFAULT) return;
	int textMode = 0;
	switch (antialias) {
		case SWT.DEFAULT:
			textMode = Gdip.TextRenderingHintSystemDefault;
			break;
		case SWT.OFF:
			textMode = Gdip.TextRenderingHintSingleBitPerPixelGridFit;
			break;
		case SWT.ON:
			int[] type = new int[1];
			OS.SystemParametersInfo(OS.SPI_GETFONTSMOOTHINGTYPE, 0, type, 0);
			if (type[0] == OS.FE_FONTSMOOTHINGCLEARTYPE) {
				textMode = Gdip.TextRenderingHintClearTypeGridFit;
			} else {
				textMode = Gdip.TextRenderingHintAntiAliasGridFit;
			}
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	initGdip();
	Gdip.Graphics_SetTextRenderingHint(data.gdipGraphics, textMode);
}

/**
 * Sets the transform that is currently being used by the receiver. If
 * the argument is <code>null</code>, the current transform is set to
 * the identity transform.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 *
 * @param transform the transform to set
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 *
 * @see Transform
 * @see #getAdvanced
 * @see #setAdvanced
 *
 * @since 3.1
 */
public void setTransform(Transform transform) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (transform != null && transform.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (data.gdipGraphics == 0 && transform == null) return;
	initGdip();
	long identity = identity();
	if (transform != null) {
		Gdip.Matrix_Multiply(identity, transform.handle, Gdip.MatrixOrderPrepend);
	}
	Gdip.Graphics_SetTransform(data.gdipGraphics, identity);
	Gdip.Matrix_delete(identity);
	data.state &= ~DRAW_OFFSET;
}

/**
 * Returns the extent of the given string. No tab
 * expansion or carriage return processing will be performed.
 * <p>
 * The <em>extent</em> of a string is the width and height of
 * the rectangular area it would cover if drawn in a particular
 * font (in this case, the current font in the receiver).
 * </p>
 *
 * @param string the string to measure
 * @return a point containing the extent of the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point stringExtent (String string) {
	if (string == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return DPIUtil.autoScaleDown(drawable, stringExtentInPixels(string));
}

Point stringExtentInPixels (String string) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	checkGC(FONT);
	int length = string.length();
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		Point size = new Point(0, 0);
		drawText(gdipGraphics, string, 0, 0, 0, size);
		return size;
	}
	SIZE size = new SIZE();
	if (length == 0) {
//		OS.GetTextExtentPoint32(handle, SPACE, SPACE.length(), size);
		OS.GetTextExtentPoint32(handle, new char[]{' '}, 1, size);
		return new Point(0, size.cy);
	} else {
		char[] buffer = string.toCharArray();
		OS.GetTextExtentPoint32(handle, buffer, length, size);
		return new Point(size.cx, size.cy);
	}
}

/**
 * Returns the extent of the given string. Tab expansion and
 * carriage return processing are performed.
 * <p>
 * The <em>extent</em> of a string is the width and height of
 * the rectangular area it would cover if drawn in a particular
 * font (in this case, the current font in the receiver).
 * </p>
 *
 * @param string the string to measure
 * @return a point containing the extent of the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point textExtent (String string) {
	return DPIUtil.autoScaleDown(drawable, textExtentInPixels(string, SWT.DRAW_DELIMITER | SWT.DRAW_TAB));
}

/**
 * Returns the extent of the given string. Tab expansion, line
 * delimiter and mnemonic processing are performed according to
 * the specified flags, which can be a combination of:
 * <dl>
 * <dt><b>DRAW_DELIMITER</b></dt>
 * <dd>draw multiple lines</dd>
 * <dt><b>DRAW_TAB</b></dt>
 * <dd>expand tabs</dd>
 * <dt><b>DRAW_MNEMONIC</b></dt>
 * <dd>underline the mnemonic character</dd>
 * <dt><b>DRAW_TRANSPARENT</b></dt>
 * <dd>transparent background</dd>
 * </dl>
 * <p>
 * The <em>extent</em> of a string is the width and height of
 * the rectangular area it would cover if drawn in a particular
 * font (in this case, the current font in the receiver).
 * </p>
 *
 * @param string the string to measure
 * @param flags the flags specifying how to process the text
 * @return a point containing the extent of the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point textExtent (String string, int flags) {
	return DPIUtil.autoScaleDown(drawable, textExtentInPixels(string, flags));
}

Point textExtentInPixels(String string, int flags) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	checkGC(FONT);
	long gdipGraphics = data.gdipGraphics;
	if (gdipGraphics != 0) {
		Point size = new Point(0, 0);
		drawText(gdipGraphics, string, 0, 0, flags, size);
		return size;
	}
	if (string.length () == 0) {
		SIZE size = new SIZE();
		OS.GetTextExtentPoint32(handle, new char [] {' '}, 1, size);
		return new Point(0, size.cy);
	}
	RECT rect = new RECT();
	char [] buffer = string.toCharArray();
	int uFormat = OS.DT_LEFT | OS.DT_CALCRECT;
	if ((flags & SWT.DRAW_DELIMITER) == 0) uFormat |= OS.DT_SINGLELINE;
	if ((flags & SWT.DRAW_TAB) != 0) uFormat |= OS.DT_EXPANDTABS;
	if ((flags & SWT.DRAW_MNEMONIC) == 0) uFormat |= OS.DT_NOPREFIX;
	OS.DrawText(handle, buffer, buffer.length, rect, uFormat);
	return new Point(rect.right, rect.bottom);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString () {
	if (isDisposed()) return "GC {*DISPOSED*}";
	return "GC {" + handle + "}";
}

/**
 * Invokes platform specific functionality to allocate a new graphics context.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>GC</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param drawable the Drawable for the receiver.
 * @param data the data for the receiver.
 *
 * @return a new <code>GC</code>
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static GC win32_new(Drawable drawable, GCData data) {
	GC gc = new GC();
	long hDC = drawable.internal_new_GC(data);
	gc.device = data.device;
	gc.init(drawable, data, hDC);
	return gc;
}

/**
 * Invokes platform specific functionality to wrap a graphics context.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>GC</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param hDC the Windows HDC.
 * @param data the data for the receiver.
 *
 * @return a new <code>GC</code>
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static GC win32_new(long hDC, GCData data) {
	GC gc = new GC();
	gc.device = data.device;
	data.style |= SWT.LEFT_TO_RIGHT;
	int flags = OS.GetLayout (hDC);
	if ((flags & OS.LAYOUT_RTL) != 0) {
		data.style |= SWT.RIGHT_TO_LEFT | SWT.MIRRORED;
	}
	gc.init(null, data, hDC);
	return gc;
}

/**
 * Answers the length of the side adjacent to the given angle
 * of a right triangle. In other words, it returns the integer
 * conversion of length * cos (angle).
 *
 * @param angle the angle in degrees
 * @param length the length of the triangle's hypotenuse
 * @return the integer conversion of length * cos (angle)
 */
private static int cos(int angle, int length) {
	return (int)(Math.cos(angle * (Math.PI/180)) * length);
}

/**
 * Answers the length of the side opposite to the given angle
 * of a right triangle. In other words, it returns the integer
 * conversion of length * sin (angle).
 *
 * @param angle the angle in degrees
 * @param length the length of the triangle's hypotenuse
 * @return the integer conversion of length * sin (angle)
 */
private static int sin(int angle, int length) {
	return (int)(Math.sin(angle * (Math.PI/180)) * length);
}

}

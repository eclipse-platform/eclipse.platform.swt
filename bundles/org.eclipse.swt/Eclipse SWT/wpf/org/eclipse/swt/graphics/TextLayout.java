/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.*;

/**
 * <code>TextLayout</code> is a graphic object that represents
 * styled text.
 * <p>
 * Instances of this class provide support for drawing, cursor
 * navigation, hit testing, text wrapping, alignment, tab expansion
 * line breaking, etc.  These are aspects required for rendering internationalized text.
 * </p><p>
 * Application code must explicitly invoke the <code>TextLayout#dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#textlayout">TextLayout, TextStyle snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: CustomControlExample, StyledText tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.0
 */
public final class TextLayout extends Resource {
	Font font;
	String text, segmentsText;
	int lineSpacing;
	int ascent, descent;
	int alignment;
	int wrapWidth;
	int orientation;
	int indent;
	boolean justify;
	int[] tabs;
	int[] segments;
	StyleItem[] styles;
	
	int string, defaultTextProperties;
	int[] runs;
	int[] lines;

	static final RGB LINK_FOREGROUND = new RGB (0, 51, 153);
	static final char LTR_MARK = '\u200E', RTL_MARK = '\u200F';
	static final int TAB_COUNT = 32;
	
class StyleItem {
	TextStyle style;
	int start, length;
	int textProperties;
	
	void free() {		
		if (textProperties != 0) OS.GCHandle_Free(textProperties);
		textProperties = 0;
	}
	public String toString () {
		return "StyleItem {" + start + ", " + style + "}";
	}
}

/**	 
 * Constructs a new instance of this class on the given device.
 * <p>
 * You must dispose the text layout when it is no longer required. 
 * </p>
 * 
 * @param device the device on which to allocate the text layout
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 * </ul>
 * 
 * @see #dispose()
 */
public TextLayout (Device device) {
	super(device);
	wrapWidth = ascent = descent = -1;
	lineSpacing = 0;
	orientation = SWT.LEFT_TO_RIGHT;
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	text = ""; //$NON-NLS-1$
	init();
}

void checkLayout () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

/* 
*  Compute the runs: itemize, shape, place, and reorder the runs.
* 	Break paragraphs into lines, wraps the text, and initialize caches.
*/
void computeRuns () {
	if (lines != null) return;
	
	int jniRef = OS.NewGlobalRef(this);
	int textSource = OS.gcnew_SWTTextSource(jniRef);
	int formatter = OS.TextFormatter_Create();
	Font font = this.font != null ? this.font : device.systemFont;
	segmentsText = getSegmentsText();
	int length = segmentsText.length();
	char [] buffer = new char [length];
	segmentsText.getChars (0, length, buffer, 0);
	string = OS.gcnew_String(buffer, 0 ,length);
	int culture = OS.CultureInfo_CurrentUICulture();
	defaultTextProperties = OS.gcnew_SWTTextRunProperties(font.handle, font.size, font.size, 0, 0, 0, OS.BaselineAlignment_Baseline, culture);
	for (int i = 0; i < styles.length; i++) {
		StyleItem run = styles[i];
		TextStyle style = run.style;
		if (style != null) {
			Font styleFont = style.font != null ? style.font : font;
			int fg = 0;
			if (style.foreground != null) {
				fg = OS.gcnew_SolidColorBrush(style.foreground.handle);
			} else {
				if (style.underline && style.underlineStyle == SWT.UNDERLINE_LINK) {
					int color = OS.Color_FromArgb((byte)0xFF, (byte)LINK_FOREGROUND.red, (byte)LINK_FOREGROUND.green, (byte)LINK_FOREGROUND.blue);
					fg = OS.gcnew_SolidColorBrush(color);
					OS.GCHandle_Free(color);
				}
			}
			int bg = 0;
			if (style.background != null) {
				bg = OS.gcnew_SolidColorBrush(style.background.handle);
			}
			int decorations = 0;
			if (style.strikeout || style.underline) {
				decorations = OS.gcnew_TextDecorationCollection(2);
				if (style.strikeout) {
					int pen = 0;
					if (style.strikeoutColor != null) {
						int color = style.strikeoutColor.handle;
						int brush = OS.gcnew_SolidColorBrush(color);
						pen = OS.gcnew_Pen(brush, 1);
						OS.GCHandle_Free(brush);
					}
					int strikeout = OS.gcnew_TextDecoration(OS.TextDecorationLocation_Strikethrough, pen, 0, OS.TextDecorationUnit_FontRecommended, OS.TextDecorationUnit_FontRecommended);
					OS.TextDecorationCollection_Add(decorations, strikeout);
					OS.GCHandle_Free(strikeout);
					if (pen != 0) OS.GCHandle_Free(pen);
				}
				if (style.underline) {
					int brush;
					if (style.underlineColor != null) {
						Color color = style.underlineColor;
						brush = OS.gcnew_SolidColorBrush(color.handle);
					} else {
						if (fg != 0) {
							brush = fg;
						} else {
							brush = OS.Brushes_Black();
						}
					}
					int pen = OS.gcnew_Pen(brush, 1f);
					if (brush != fg) OS.GCHandle_Free(brush);
					int underline;
					switch (style.underlineStyle) {
						case SWT.UNDERLINE_SQUIGGLE:
							//TODO implement
						case SWT.UNDERLINE_ERROR:
							int dashStyle = OS.DashStyles_Dash();
							OS.Pen_DashStyle(pen, dashStyle);
							underline = OS.gcnew_TextDecoration(OS.TextDecorationLocation_Underline, pen, 0, OS.TextDecorationUnit_FontRecommended, OS.TextDecorationUnit_FontRecommended);
							OS.TextDecorationCollection_Add(decorations, underline);
							OS.GCHandle_Free(underline);
							OS.GCHandle_Free(dashStyle);
							break;
						case SWT.UNDERLINE_DOUBLE: 
							underline = OS.gcnew_TextDecoration(OS.TextDecorationLocation_Underline, pen, 1, OS.TextDecorationUnit_FontRecommended, OS.TextDecorationUnit_FontRecommended);
							OS.TextDecorationCollection_Add(decorations, underline);
							OS.GCHandle_Free(underline);
							//FALLTHROU
						case SWT.UNDERLINE_LINK: 
						case SWT.UNDERLINE_SINGLE: 
							underline = OS.gcnew_TextDecoration(OS.TextDecorationLocation_Underline, pen, 0, OS.TextDecorationUnit_FontRecommended, OS.TextDecorationUnit_FontRecommended);
							OS.TextDecorationCollection_Add(decorations, underline);
							OS.GCHandle_Free(underline);
							break;
					}
					if (pen != 0) OS.GCHandle_Free(pen);
				}
			}
			run.textProperties = OS.gcnew_SWTTextRunProperties(styleFont.handle, styleFont.size, styleFont.size, decorations, fg, bg, OS.BaselineAlignment_Baseline, culture);
			if (fg != 0) OS.GCHandle_Free(fg);
			if (bg != 0) OS.GCHandle_Free(bg);
			if (decorations != 0) OS.GCHandle_Free(decorations);
		}
	}
	int textAlignment = OS.TextAlignment_Left;
	if (justify) {
		textAlignment = OS.TextAlignment_Justify;
	} else {
		switch (alignment) {
			case SWT.CENTER: textAlignment = OS.TextAlignment_Center; break; 
			case SWT.RIGHT: textAlignment = OS.TextAlignment_Right; break;
		}
	}
	int flowDirection = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? OS.FlowDirection_RightToLeft : OS.FlowDirection_LeftToRight;
	int textWrapping = wrapWidth != -1 ? OS.TextWrapping_Wrap : OS.TextWrapping_NoWrap;
	int tabCollection = 0;
	if (tabs != null) {
		int position = 0;
		int tabLength = Math.max(tabs.length, TAB_COUNT), i;
		tabCollection = OS.gcnew_TextTabPropertiesCollection(tabLength);
		for (i = 0; i < tabs.length; i++) {
			position = tabs[i];
			int tab = OS.gcnew_TextTabProperties(OS.TextTabAlignment_Left, position, 0, 0);
			OS.TextTabPropertiesCollection_Add(tabCollection, tab);
			OS.GCHandle_Free(tab);
		}
		int width = tabs[tabs.length - 1];
		if (tabs.length > 1) width -= tabs[tabs.length - 2];
		if (width > 0) {
			for (; i < length; i++) {
				position += width;
				int tab = OS.gcnew_TextTabProperties(OS.TextTabAlignment_Left, position, 0, 0);
				OS.TextTabPropertiesCollection_Add(tabCollection, tab);
				OS.GCHandle_Free(tab);
			}
		}
	}
	int paragraphProperties = OS.gcnew_SWTTextParagraphProperties(flowDirection, textAlignment, false, defaultTextProperties, textWrapping, 0, 0, tabCollection); 
	int firstParagraphProperties = OS.gcnew_SWTTextParagraphProperties(flowDirection, textAlignment, true, defaultTextProperties, textWrapping, 0, indent, tabCollection);  
	int offset = 0;
	int index = 0;
	lines = new int[4];
	int lineBreak = 0;
	while (offset < length || offset == 0) {
		char ch;
		boolean firstLine = offset == 0 || (ch = segmentsText.charAt(offset - 1)) == '\r' || ch == '\n';
		int paragraphProps = firstLine ? firstParagraphProperties : paragraphProperties;
		int textLine = OS.TextFormatter_FormatLine(formatter, textSource, offset, wrapWidth != -1 ? wrapWidth : 0, paragraphProps, lineBreak);
		offset += OS.TextLine_Length(textLine);
		lineBreak = OS.TextLine_GetTextLineBreak(textLine);
		if (index == lines.length) {
			int[] tmpLines = new int[index + 4];
			System.arraycopy(lines, 0, tmpLines, 0, index);
			lines = tmpLines;
		}
		lines[index++] = textLine;
	}
	if (index != lines.length) {
		int[] tmpLines = new int[index];
		System.arraycopy(lines, 0, tmpLines, 0, index);
		lines = tmpLines;
	}
	if (tabCollection != 0) OS.GCHandle_Free(tabCollection);
	OS.GCHandle_Free(paragraphProperties);
	OS.GCHandle_Free(firstParagraphProperties);
	OS.GCHandle_Free(culture);
	OS.GCHandle_Free(formatter);
	OS.GCHandle_Free(textSource);
	OS.DeleteGlobalRef(jniRef);
}

 void destroy() {
	freeRuns();
	font = null;	
	text = null;
	segmentsText = null;
	tabs = null;
	styles = null;
//	lineOffset = null;
//	lineY = null;
//	lineWidth = null;
}

/**
 * Draws the receiver's text using the specified GC at the specified
 * point.
 * 
 * @param gc the GC to draw
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 * </ul>
 */
public void draw (GC gc, int x, int y) {
	draw(gc, x, y, -1, -1, null, null);
}

/**
 * Draws the receiver's text using the specified GC at the specified
 * point.
 * 
 * @param gc the GC to draw
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param selectionStart the offset where the selections starts, or -1 indicating no selection
 * @param selectionEnd the offset where the selections ends, or -1 indicating no selection
 * @param selectionForeground selection foreground, or NULL to use the system default color
 * @param selectionBackground selection background, or NULL to use the system default color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 * </ul>
 */
public void draw (GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground) {
	draw(gc, x, y, selectionStart, selectionEnd, selectionForeground, selectionBackground, 0);
}
/**
 * Draws the receiver's text using the specified GC at the specified
 * point.
 * <p>
 * The parameter <code>flags</code> can include one of <code>SWT.DELIMITER_SELECTION</code>
 * or <code>SWT.FULL_SELECTION</code> to specify the selection behavior on all lines except
 * for the last line, and can also include <code>SWT.LAST_LINE_SELECTION</code> to extend
 * the specified selection behavior to the last line.
 * </p>
 * @param gc the GC to draw
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param selectionStart the offset where the selections starts, or -1 indicating no selection
 * @param selectionEnd the offset where the selections ends, or -1 indicating no selection
 * @param selectionForeground selection foreground, or NULL to use the system default color
 * @param selectionBackground selection background, or NULL to use the system default color
 * @param flags drawing options
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 * </ul>
 * 
 * @since 3.3
 */
public void draw (GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground, int flags) {
	checkLayout();
	computeRuns();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);	
	if (selectionForeground != null && selectionForeground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionBackground != null && selectionBackground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int length = text.length();
	if (length == 0 && flags == 0) return;
	gc.checkGC(GC.FOREGROUND);
	int fg = OS.Pen_Brush(gc.data.pen);
	OS.SWTTextRunProperties_ForegroundBrush(defaultTextProperties, fg);
	for (int i = 0; i < styles.length; i++) {
		StyleItem run = styles[i];
		if (run.textProperties == 0) continue;
		if (run.style != null && run.style.foreground != null) continue;
		if (run.style != null && run.style.underline && run.style.underlineStyle == SWT.UNDERLINE_LINK) continue;
		OS.SWTTextRunProperties_ForegroundBrush(run.textProperties, fg);
	}
	int drawingContext = gc.handle;
	boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
	int selBrush = 0, selGeometry = 0, geometries = 0;
	if (hasSelection || (flags & SWT.LAST_LINE_SELECTION) != 0) {
		selectionStart = Math.min(Math.max(0, selectionStart), length - 1);
		selectionEnd = Math.min(Math.max(0, selectionEnd), length - 1);
		selectionStart = translateOffset(selectionStart);
		selectionEnd = translateOffset(selectionEnd);
		if (selectionBackground != null) {
			selBrush = OS.gcnew_SolidColorBrush(selectionBackground.handle);
		} else {
			selBrush = OS.Brushes_LightSkyBlue();
		}
		selGeometry = OS.gcnew_GeometryGroup();
		geometries = OS.GeometryGroup_Children(selGeometry);
	}
	int lineStart = 0, lineEnd = 0;
	double drawY = y;
	for (int i = 0; i < lines.length; i++) {
		int line = lines[i];
		if (line == 0) break;
		lineStart = lineEnd;
		lineEnd = lineStart + OS.TextLine_Length(line);
		double nextDrawY, selY = drawY;
		int lineHeight = (int)OS.TextLine_Height(line);
		if (ascent != -1 && descent != -1) {
			lineHeight = Math.max(lineHeight, ascent + descent);
			nextDrawY = drawY + lineHeight + lineSpacing;
			int baseline = (int)OS.TextLine_Baseline(line);
			if (ascent > baseline) drawY += ascent - baseline;
		} else {
			nextDrawY = drawY + lineHeight + lineSpacing;
		}

		//draw line text
		int point = OS.gcnew_Point(x, drawY);
		OS.TextLine_Draw(line, drawingContext, point, 0);
		OS.GCHandle_Free(point);
		
		//draw line selection
		boolean fullSelection = selectionStart <= lineStart && selectionEnd >= lineEnd;
		boolean partialSelection = !(selectionStart > lineEnd || lineStart > selectionEnd);
		if (flags != 0 && (hasSelection || (flags & SWT.LAST_LINE_SELECTION) != 0)) {
			boolean extent = false;
			if (i == lines.length - 1 && (flags & SWT.LAST_LINE_SELECTION) != 0) {
				extent = true;
			} else {
				int breakLength = OS.TextLine_NewlineLength(line);
				if (breakLength != 0) {
					if (selectionStart <= lineEnd && lineEnd <= selectionEnd) extent = true;
				} else {
					if (selectionStart <= lineEnd && lineEnd < selectionEnd && (flags & SWT.FULL_SELECTION) != 0) {
						extent = true;
					}
				}
			}
			if (extent) {
				int extentWidth = (flags & SWT.FULL_SELECTION) != 0 ? 0x7ffffff : lineHeight / 3;
				int textRect = OS.gcnew_Rect(OS.TextLine_WidthIncludingTrailingWhitespace(line) + x, selY, extentWidth, lineHeight);
				int geometry = OS.gcnew_RectangleGeometry(textRect);
				OS.GeometryCollection_Add(geometries, geometry);
				OS.GCHandle_Free(geometry);
				OS.GCHandle_Free(textRect);
			}
		}
		if (hasSelection && (fullSelection || partialSelection)) {
			int selLineStart = Math.max (lineStart, selectionStart);
			int selLineEnd = Math.min (lineEnd, selectionEnd);
			int rects = OS.TextLine_GetTextBounds(line, selLineStart, selLineEnd - selLineStart + 1);
			if (rects != 0) {
				int enumerator = OS.TextBoundsCollection_GetEnumerator(rects);
				while (OS.IEnumerator_MoveNext(enumerator)) {
					int bounds = OS.TextBoundsCollection_Current(enumerator);
					int textRect = OS.TextBounds_Rectangle(bounds);
					OS.Rect_X(textRect, OS.Rect_X(textRect) + x);
					OS.Rect_Y(textRect, selY);
					OS.Rect_Height(textRect, lineHeight);
					int geometry = OS.gcnew_RectangleGeometry(textRect);
					OS.GeometryCollection_Add(geometries, geometry);
					OS.GCHandle_Free(geometry);
					OS.GCHandle_Free(textRect);
					OS.GCHandle_Free(bounds);
				}
				OS.GCHandle_Free(enumerator);
			}
			OS.GCHandle_Free(rects);
		}

		drawY = nextDrawY;
	}
	for (int i = 0; i < styles.length - 1; i++) {
		StyleItem run = styles[i];
		TextStyle style = run.style;
		if (style == null) continue;
		if (style.borderStyle != SWT.NONE && (i + 1 >= styles.length || !style.isAdherentBorder(styles[i + 1].style))) {
			int start = run.start;
			int end = styles[i + 1].start - 1;
			for (int j = i; j > 0 && style.isAdherentBorder(styles[j - 1].style); j--) {
				start = styles[j - 1].start;
			}
			Color color = style.borderColor;
			if (color == null) color = style.foreground;
			if (color == null) color = gc.getForeground();
			int brush = OS.gcnew_SolidColorBrush(color.handle);
			int pen = OS.gcnew_Pen(brush, 1);
			OS.GCHandle_Free(brush);
			int dashStyle = 0;
			switch (style.borderStyle) {
				case SWT.BORDER_SOLID:
					dashStyle = OS.DashStyles_Solid(); break;
				case SWT.BORDER_DOT:
					dashStyle = OS.DashStyles_Dot(); break;
				case SWT.BORDER_DASH:
					dashStyle = OS.DashStyles_Dash(); break;
			}
			OS.Pen_DashStyle(pen, dashStyle);
			if (dashStyle != 0) OS.GCHandle_Free(dashStyle);
			int lineY = y;
			lineStart = lineEnd = 0;
			for (int j = 0; j < lines.length; j++) {
				int lineLength = OS.TextLine_Length(lines[j]);
				lineStart = lineEnd;
				lineEnd = lineStart + lineLength;
				if (start < lineEnd) {
					if (end < lineStart) break;
					int rangeStart = Math.max(start, lineStart);
					int rangLength = Math.min(end, lineEnd) - rangeStart + 1;
					int rects = OS.TextLine_GetTextBounds(lines[j], rangeStart, rangLength);	
					if (rects != 0) {
						int enumerator = OS.TextBoundsCollection_GetEnumerator(rects);
						while (OS.IEnumerator_MoveNext(enumerator)) {
							int bounds = OS.TextBoundsCollection_Current(enumerator);
							int textRect = OS.TextBounds_Rectangle(bounds);
							OS.Rect_Y(textRect, OS.Rect_Y(textRect) + lineY);
							OS.Rect_X(textRect, OS.Rect_X(textRect) + x);
							OS.Rect_Width(textRect, OS.Rect_Width(textRect) - 1);
							OS.Rect_Height(textRect, OS.Rect_Height(textRect) - 1);
							OS.DrawingContext_DrawRectangle(drawingContext, 0, pen, textRect);
							OS.GCHandle_Free(textRect);
							OS.GCHandle_Free(bounds);
						}
						OS.GCHandle_Free(enumerator);
					}
					OS.GCHandle_Free(rects);
				}
				int lineHeight = (int)OS.TextLine_Height(lines[j]);
				if (ascent != -1 && descent != -1) lineHeight = Math.max(lineHeight, ascent + descent);
				lineY += lineHeight + lineSpacing;
			}
			OS.GCHandle_Free(pen);
		} 
	}
	
	if (selGeometry != 0) {
		OS.DrawingContext_PushOpacity(drawingContext, 0.4);
		OS.DrawingContext_DrawGeometry(drawingContext, selBrush, 0, selGeometry);
		OS.DrawingContext_Pop(drawingContext);
		OS.GCHandle_Free(geometries);
		OS.GCHandle_Free(selGeometry);
	}
	if (selBrush != 0) OS.GCHandle_Free(selBrush);
	OS.GCHandle_Free(fg);
}

void freeRuns () {
	if (lines == null) return;
	for (int i = 0; i < lines.length; i++) {
		if (lines[i] != 0) {
			OS.GCHandle_Free(lines[i]);
		}
	}
	lines = null;
	if (runs != null) {
		for (int i = 0; i < runs.length; i++) {
			if (runs[i] == 0) break;
			OS.GCHandle_Free(runs[i]);
		}
		runs = null;
	}
	for (int i = 0; i < styles.length; i++) {
		 styles[i].free();
	}
	if (defaultTextProperties != 0) OS.GCHandle_Free(defaultTextProperties);
	if (string != 0) OS.GCHandle_Free(string);
	defaultTextProperties = string = 0;
	segmentsText = null;
}

/** 
 * Returns the receiver's horizontal text alignment, which will be one
 * of <code>SWT.LEFT</code>, <code>SWT.CENTER</code> or
 * <code>SWT.RIGHT</code>.
 *
 * @return the alignment used to positioned text horizontally
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getAlignment () {
	checkLayout();
	return alignment;
}

/**
 * Returns the ascent of the receiver.
 *
 * @return the ascent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getDescent()
 * @see #setDescent(int)
 * @see #setAscent(int)
 * @see #getLineMetrics(int)
 */
public int getAscent () {
	checkLayout();
	return ascent;
}

/**
 * Returns the bounds of the receiver. The width returned is either the
 * width of the longest line or the width set using {@link TextLayout#setWidth(int)}.
 * To obtain the text bounds of a line use {@link TextLayout#getLineBounds(int)}.
 * 
 * @return the bounds of the receiver
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setWidth(int)
 * @see #getLineBounds(int)
 */
public Rectangle getBounds () {
	checkLayout();
	computeRuns();
	double width = 0;
	double height = 0;
	for (int line=0; line<lines.length; line++) {
		if (wrapWidth == -1) width = Math.max(width, OS.TextLine_WidthIncludingTrailingWhitespace(lines[line]));
		int lineHeight = (int)OS.TextLine_Height(lines[line]);
		if (ascent != -1 && descent != -1) lineHeight = Math.max(lineHeight, ascent + descent);
		height += lineHeight + lineSpacing;
	}
	if (wrapWidth != -1) width = wrapWidth; 
	return new Rectangle (0, 0, (int)width, (int)height);
}

/**
 * Returns the bounds for the specified range of characters. The
 * bounds is the smallest rectangle that encompasses all characters
 * in the range. The start and end offsets are inclusive and will be
 * clamped if out of range.
 * 
 * @param start the start offset
 * @param end the end offset
 * @return the bounds of the character range
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Rectangle getBounds (int start, int end) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (length == 0) return new Rectangle(0, 0, 0, 0);
	if (start > end) return new Rectangle(0, 0, 0, 0);
	start = Math.min(Math.max(0, start), length - 1);
	end = Math.min(Math.max(0, end), length - 1);
	start = translateOffset(start);
	end = translateOffset(end);
	int lineStart = 0, lineEnd = 0, lineY = 0;
	int rect = 0;
	for (int i = 0; i < lines.length; i++) {
		int lineLength = OS.TextLine_Length(lines[i]);
		lineStart = lineEnd;
		lineEnd = lineStart + lineLength;
		if (start < lineEnd) {
			if (end < lineStart) break;
			int rangeStart = Math.max(start, lineStart);
			int rangLength = Math.min(end, lineEnd) - rangeStart + 1;
			int rects = OS.TextLine_GetTextBounds(lines[i], rangeStart, rangLength);	
			if (rects != 0) {
				int enumerator = OS.TextBoundsCollection_GetEnumerator(rects);
				while (OS.IEnumerator_MoveNext(enumerator)) {
					int bounds = OS.TextBoundsCollection_Current(enumerator);
					int textRect = OS.TextBounds_Rectangle(bounds);
					OS.Rect_Y(textRect, OS.Rect_Y(textRect) + lineY);
					if (rect != 0) {
						OS.Rect_Union(rect, textRect);
						OS.GCHandle_Free(textRect);
					} else {
						rect = textRect;
					}				
					OS.GCHandle_Free(bounds);
				}
				OS.GCHandle_Free(enumerator);
			}
			OS.GCHandle_Free(rects);
		}
		int lineHeight = (int)OS.TextLine_Height(lines[i]);
		if (ascent != -1 && descent != -1) lineHeight = Math.max(lineHeight, ascent + descent);
		lineY += lineHeight + lineSpacing;
	}
	if (rect == 0) return new Rectangle(0, 0, 0, 0);
	Rectangle result = new Rectangle((int)OS.Rect_X(rect), (int)OS.Rect_Y(rect), (int)OS.Rect_Width(rect), (int)OS.Rect_Height(rect));
	OS.GCHandle_Free(rect);
	return result;
}

/**
 * Returns the descent of the receiver.
 *
 * @return the descent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getAscent()
 * @see #setAscent(int)
 * @see #setDescent(int)
 * @see #getLineMetrics(int)
 */
public int getDescent () {
	checkLayout();
	return descent;
}

/** 
 * Returns the default font currently being used by the receiver
 * to draw and measure text.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Font getFont () {
	checkLayout();
	return font;
}

/**
* Returns the receiver's indent.
*
* @return the receiver's indent
* 
* @exception SWTException <ul>
*    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
* </ul>
* 
* @since 3.2
*/
public int getIndent () {
	checkLayout();
	return indent;
}

/**
* Returns the receiver's justification.
*
* @return the receiver's justification
* 
* @exception SWTException <ul>
*    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
* </ul>
* 
* @since 3.2
*/
public boolean getJustify () {
	checkLayout();
	return justify;
}

/**
 * Returns the embedding level for the specified character offset. The
 * embedding level is usually used to determine the directionality of a
 * character in bidirectional text.
 * 
 * @param offset the character offset
 * @return the embedding level
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the character offset is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 */
public int getLevel (int offset) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	offset = translateOffset(offset);
	int level = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? 1 : 0;
	for (int i = 0; i < lines.length; i++) {
		int lineLength = OS.TextLine_Length(lines[i]);
		if (lineLength > offset) {
			int runs = OS.TextLine_GetIndexedGlyphRuns (lines[i]);
			int enumerator = OS.IndexedGlyphRunCollection_GetEnumerator(runs);
			while (OS.IEnumerator_MoveNext(enumerator)) {
				int indexedGlyphRun = OS.IndexedGlyphRunCollection_Current(enumerator);
				int rangeStart = OS.IndexedGlyphRun_TextSourceCharacterIndex(indexedGlyphRun);
				int rangeEnd = rangeStart +  OS.IndexedGlyphRun_TextSourceLength(indexedGlyphRun);
				int glyphRun = OS.IndexedGlyphRun_GlyphRun(indexedGlyphRun);
				int bidiLevel = OS.GlyphRun_BidiLevel(glyphRun);
				OS.GCHandle_Free(glyphRun);
				OS.GCHandle_Free(indexedGlyphRun);
				if (rangeStart <= offset && offset < rangeEnd) {
					level = bidiLevel;
					break;
				}
			}
			OS.GCHandle_Free(enumerator);
			OS.GCHandle_Free(runs);
			break;
		}
	}
	return level;
}

/**
 * Returns the bounds of the line for the specified line index.
 * 
 * @param lineIndex the line index
 * @return the line bounds 
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the line index is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Rectangle getLineBounds(int lineIndex) {
	checkLayout();
	computeRuns();
	if (!(0 <= lineIndex && lineIndex < runs.length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int offset = 0;
	double y = 0;
	for (int i=0; i<lineIndex; i++) {
		offset += OS.TextLine_Length(lines[i]);
		int lineHeight = (int)OS.TextLine_Height(lines[i]);
		if (ascent != -1 && descent != -1) lineHeight = Math.max(lineHeight, ascent + descent);
		y += lineHeight + lineSpacing;
	}
	int line = lines[lineIndex];
	double x = OS.TextLine_Start(line);
	double width = OS.TextLine_WidthIncludingTrailingWhitespace(line);
	double height = OS.TextLine_Height(line);
	if (ascent != -1 && descent != -1) height = Math.max(height, ascent + descent);
	char ch;
	boolean firstLine = offset == 0 || (ch = segmentsText.charAt(offset - 1)) == '\r' || ch == '\n';
	if (firstLine) {
		x += indent;
		width -= indent;
	}
	return new Rectangle ((int)x, (int)y, (int)width, (int)height);
}

/**
 * Returns the receiver's line count. This includes lines caused
 * by wrapping.
 *
 * @return the line count
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getLineCount () {
	checkLayout();
	computeRuns();
	return lines.length;
}

/**
 * Returns the index of the line that contains the specified
 * character offset.
 * 
 * @param offset the character offset
 * @return the line index
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the character offset is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getLineIndex (int offset) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	offset = translateOffset(offset);
	int start = 0;
	for (int line=0; line<lines.length; line++) {
		int lineLength = OS.TextLine_Length(lines[line]);
		if (start + lineLength > offset) return line;
		start += lineLength;
	}
	return lines.length - 1;
}

/**
 * Returns the font metrics for the specified line index.
 * 
 * @param lineIndex the line index
 * @return the font metrics 
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the line index is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public FontMetrics getLineMetrics (int lineIndex) {
	checkLayout();
	computeRuns();
	if (!(0 <= lineIndex && lineIndex < runs.length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int length = text.length();
	double baseline, height;
	if (length == 0) {
		Font font = this.font != null ? this.font : device.systemFont;
		int str = OS.gcnew_String (new char []{' ', '\0'});
		int culture = OS.CultureInfo_CurrentUICulture();
		int direction = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? OS.FlowDirection_RightToLeft : OS.FlowDirection_LeftToRight;
		int brush = OS.Brushes_White();
		int text = OS.gcnew_FormattedText(str, culture, direction, font.handle, font.size, brush);
		height = OS.FormattedText_Height(text);
		baseline = OS.FormattedText_Baseline(text);
		OS.GCHandle_Free(text);
		OS.GCHandle_Free(str);
		OS.GCHandle_Free(brush);
		OS.GCHandle_Free(culture);
	} else {
		baseline = OS.TextLine_Baseline(lines[lineIndex]);
		height = OS.TextLine_Height(lines[lineIndex]);
		if (ascent != -1 && descent != -1) {
			baseline = Math.max(baseline, ascent);
			height = Math.max(height, ascent + descent);
		}
	}
	return FontMetrics.wpf_new((int)baseline, (int)height - (int)baseline, 0, 0, (int)height);
}

/**
 * Returns the line offsets.  Each value in the array is the
 * offset for the first character in a line except for the last
 * value, which contains the length of the text.
 * 
 * @return the line offsets
 *  
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int[] getLineOffsets () {
	checkLayout();
	computeRuns();
	int start = 0;
	int[] offsets = new int[lines.length+1];
	for (int i = 0; i < lines.length; i++) {
		start += OS.TextLine_Length(lines[i]);
		offsets[i+1] = untranslateOffset(start);
	}
	return offsets;
}

/**
 * Returns the location for the specified character offset. The
 * <code>trailing</code> argument indicates whether the offset
 * corresponds to the leading or trailing edge of the cluster.
 * 
 * @param offset the character offset
 * @param trailing the trailing flag
 * @return the location of the character offset
 *  
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getOffset(Point, int[])
 * @see #getOffset(int, int, int[])
 */
public Point getLocation (int offset, boolean trailing) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	offset = translateOffset(offset);
	double y = 0;
	int start = 0, line;	
	for (line=0; line<lines.length; line++) {
		int lineLength = OS.TextLine_Length(lines[line]);
		if (start + lineLength > offset) break;
		start += lineLength;
		int lineHeight = (int)OS.TextLine_Height(lines[line]);
		if (ascent != -1 && descent != -1) lineHeight = Math.max(lineHeight, ascent + descent);
		y += lineHeight + lineSpacing;
	}
	int characterHit = OS.gcnew_CharacterHit(offset, trailing ? 1 : 0);
	double x = OS.TextLine_GetDistanceFromCharacterHit(lines[line], characterHit);
	OS.GCHandle_Free(characterHit);
	return new Point((int)x, (int)y);
}

/**
 * Returns the next offset for the specified offset and movement
 * type.  The movement is one of <code>SWT.MOVEMENT_CHAR</code>, 
 * <code>SWT.MOVEMENT_CLUSTER</code>, <code>SWT.MOVEMENT_WORD</code>,
 * <code>SWT.MOVEMENT_WORD_END</code> or <code>SWT.MOVEMENT_WORD_START</code>.
 * 
 * @param offset the start offset
 * @param movement the movement type 
 * @return the next offset
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the offset is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getPreviousOffset(int, int)
 */
public int getNextOffset (int offset, int movement) {
	checkLayout();
	return _getOffset (offset, movement, true);
}

int _getOffset(int offset, int movement, boolean forward) {
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	if (forward && offset == length) return length;
	if (!forward && offset == 0) return 0;
	int step = forward ? 1 : -1;
	if ((movement & SWT.MOVEMENT_CHAR) != 0) return offset + step;
	offset = translateOffset(offset);
	int lineStart = 0, lineIndex;	
	for (lineIndex=0; lineIndex<lines.length; lineIndex++) {
		int lineLength = OS.TextLine_Length(lines[lineIndex]);
		if (lineStart + lineLength > offset) break;
		lineStart += lineLength;
	}
	int line = lines[lineIndex];
	int lineLength = OS.TextLine_Length(line);
	int lineBreak = OS.TextLine_NewlineLength (line);
	while (lineStart <= offset && offset <= lineStart + lineLength) {
		int resultCharHit;
		int characterHit = OS.gcnew_CharacterHit(offset, 0);
		if (forward) {
			resultCharHit = OS.TextLine_GetNextCaretCharacterHit(line, characterHit);
		} else {
			resultCharHit = OS.TextLine_GetPreviousCaretCharacterHit(line, characterHit);
		}
		int newOffset = OS.CharacterHit_FirstCharacterIndex(resultCharHit);
		int trailing = OS.CharacterHit_TrailingLength(resultCharHit);
		OS.GCHandle_Free(resultCharHit);
		OS.GCHandle_Free(characterHit);
		if (forward) {
			if (newOffset + trailing >= lineStart + lineLength - lineBreak) {
				int lineEnd = lineStart + lineLength;
				if (trailing != 0) lineEnd -= lineBreak;
				return untranslateOffset(Math.min(length, lineEnd)); 
			}
		} else {
			if (newOffset + trailing == lineStart) {
				if (lineIndex == 0) return 0;
				int lineEnd = 0;
				if (newOffset + trailing == offset) lineEnd = OS.TextLine_NewlineLength(lines[lineIndex - 1]);
				return untranslateOffset(Math.max(0, newOffset + trailing - lineEnd)); 
			}
		}
		offset = newOffset + trailing;

		switch (movement) {
			case SWT.MOVEMENT_CLUSTER:
				return untranslateOffset(offset);
			case SWT.MOVEMENT_WORD:
			case SWT.MOVEMENT_WORD_START: {
				if (offset > 0) {
					boolean letterOrDigit = Compatibility.isLetterOrDigit(segmentsText.charAt(offset));
					boolean previousLetterOrDigit = Compatibility.isLetterOrDigit(segmentsText.charAt(offset - 1));
					if (letterOrDigit != previousLetterOrDigit || !letterOrDigit) {
						if (!Compatibility.isWhitespace(segmentsText.charAt(offset))) {
							return untranslateOffset(offset);
						}
					}
				}
				break;
			}
			case SWT.MOVEMENT_WORD_END: {
				if (offset > 0) {
					boolean isLetterOrDigit = Compatibility.isLetterOrDigit(segmentsText.charAt(offset));
					boolean previousLetterOrDigit = Compatibility.isLetterOrDigit(segmentsText.charAt(offset - 1));
					if (!isLetterOrDigit && previousLetterOrDigit) {
						return untranslateOffset(offset);
					}
				}
				break;
			}
		}
	}
	return forward ? length : 0;
}

/**
 * Returns the character offset for the specified point.  
 * For a typical character, the trailing argument will be filled in to 
 * indicate whether the point is closer to the leading edge (0) or
 * the trailing edge (1).  When the point is over a cluster composed 
 * of multiple characters, the trailing argument will be filled with the 
 * position of the character in the cluster that is closest to
 * the point.
 * 
 * @param point the point
 * @param trailing the trailing buffer
 * @return the character offset
 *  
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the trailing length is less than <code>1</code></li>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getLocation(int, boolean)
 */
public int getOffset (Point point, int[] trailing) {
	checkLayout();
	if (point == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return getOffset (point.x, point.y, trailing) ;
}

/**
 * Returns the character offset for the specified point.  
 * For a typical character, the trailing argument will be filled in to 
 * indicate whether the point is closer to the leading edge (0) or
 * the trailing edge (1).  When the point is over a cluster composed 
 * of multiple characters, the trailing argument will be filled with the 
 * position of the character in the cluster that is closest to
 * the point.
 * 
 * @param x the x coordinate of the point
 * @param y the y coordinate of the point
 * @param trailing the trailing buffer
 * @return the character offset
 *  
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the trailing length is less than <code>1</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getLocation(int, boolean)
 */
public int getOffset (int x, int y, int[] trailing) {
	checkLayout();
	computeRuns();
	if (trailing != null && trailing.length < 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	double lineY = 0;
	int line;	
	for (line=0; line<lines.length; line++) {
		double lineHeight = OS.TextLine_Length(lines[line]);
		if (lineY + lineHeight > y) break;
		lineY += lineHeight;
	}
	if (line >= lines.length) line = lines.length - 1;
	int characterHit = OS.TextLine_GetCharacterHitFromDistance(lines[line], x);
	int offset = OS.CharacterHit_FirstCharacterIndex(characterHit);
	if (trailing != null) trailing[0] = OS.CharacterHit_TrailingLength(characterHit);
	OS.GCHandle_Free(characterHit);
	return untranslateOffset(offset);
}

/**
 * Returns the orientation of the receiver.
 *
 * @return the orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getOrientation () {
	checkLayout();
	return orientation;
}

/**
 * Returns the previous offset for the specified offset and movement
 * type.  The movement is one of <code>SWT.MOVEMENT_CHAR</code>, 
 * <code>SWT.MOVEMENT_CLUSTER</code> or <code>SWT.MOVEMENT_WORD</code>,
 * <code>SWT.MOVEMENT_WORD_END</code> or <code>SWT.MOVEMENT_WORD_START</code>.
 * 
 * @param offset the start offset
 * @param movement the movement type 
 * @return the previous offset
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the offset is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getNextOffset(int, int)
 */
public int getPreviousOffset (int offset, int movement) {
	checkLayout();
	return _getOffset (offset, movement, false);
}

/**
 * Gets the ranges of text that are associated with a <code>TextStyle</code>.
 *
 * @return the ranges, an array of offsets representing the start and end of each
 * text style. 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getStyles()
 * 
 * @since 3.2
 */
public int[] getRanges () {
	checkLayout();
	int[] result = new int[styles.length * 2];
	int count = 0;
	for (int i=0; i<styles.length - 1; i++) {
		if (styles[i].style != null) {
			result[count++] = styles[i].start;
			result[count++] = styles[i + 1].start - 1;
		}
	}
	if (count != result.length) {
		int[] newResult = new int[count];
		System.arraycopy(result, 0, newResult, 0, count);
		result = newResult;
	}
	return result;
}

/**
 * Returns the text segments offsets of the receiver.
 *
 * @return the text segments offsets
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int[] getSegments () {
	checkLayout();
	return segments;
}

String getSegmentsText() {
	if (segments == null) return text;
	int nSegments = segments.length;
	if (nSegments <= 1) return text;
	int length = text.length();
	if (length == 0) return text;
	if (nSegments == 2) {
		if (segments[0] == 0 && segments[1] == length) return text;
	}
	char[] oldChars = new char[length];
	text.getChars(0, length, oldChars, 0);
	char[] newChars = new char[length + nSegments];
	int charCount = 0, segmentCount = 0;
	char separator = orientation == SWT.RIGHT_TO_LEFT ? RTL_MARK : LTR_MARK;
	while (charCount < length) {
		if (segmentCount < nSegments && charCount == segments[segmentCount]) {
			newChars[charCount + segmentCount++] = separator;
		} else {
			newChars[charCount + segmentCount] = oldChars[charCount++];
		}
	}
	if (segmentCount < nSegments) {
		segments[segmentCount] = charCount;
		newChars[charCount + segmentCount++] = separator;
	}
	return new String(newChars, 0, Math.min(charCount + segmentCount, newChars.length));
}

/**
 * Returns the line spacing of the receiver.
 *
 * @return the line spacing
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getSpacing () {
	checkLayout();	
	return lineSpacing;
}

/**
 * Gets the style of the receiver at the specified character offset.
 *
 * @param offset the text offset
 * @return the style or <code>null</code> if not set
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the character offset is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public TextStyle getStyle (int offset) {
	checkLayout();
	int length = text.length();
	if (!(0 <= offset && offset < length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	for (int i=1; i<styles.length; i++) {
		if (styles[i].start > offset) {
			return styles[i - 1].style;
		}
	}
	return null;
}

/**
 * Gets all styles of the receiver.
 *
 * @return the styles
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getRanges()
 * 
 * @since 3.2
 */
public TextStyle[] getStyles () {
	checkLayout();
	TextStyle[] result = new TextStyle[styles.length];
	int count = 0;
	for (int i=0; i<styles.length; i++) {
		if (styles[i].style != null) {
			result[count++] = styles[i].style;
		}
	}
	if (count != result.length) {
		TextStyle[] newResult = new TextStyle[count];
		System.arraycopy(result, 0, newResult, 0, count);
		result = newResult;
	}
	return result;
}

/**
 * Returns the tab list of the receiver.
 *
 * @return the tab list
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int[] getTabs () {
	checkLayout();
	return tabs;
}

int GetTextRun(int textSourceCharacterIndex) {
	if (runs == null) runs = new int[4];
	int index = 0;
	while (index < runs.length && runs[index] != 0) index++; 
	if (index == runs.length) {
		int[] tmpRuns = new int[index + 4];
		System.arraycopy(runs, 0, tmpRuns, 0, index);
		runs = tmpRuns;
	}
	int length = OS.String_Length(string);
	if (textSourceCharacterIndex >= length) {
		runs[index] = OS.gcnew_TextEndOfParagraph(1, defaultTextProperties);
	} else {
		int styleIndex = 1;
		while (styleIndex < styles.length && styles[styleIndex].start <= textSourceCharacterIndex) styleIndex++;
		TextStyle textStyle = styles[styleIndex - 1].style;
		int textProperties = styles[styleIndex - 1].textProperties;
		if (textProperties == 0) textProperties = defaultTextProperties;
		int end = styles[styleIndex].start;
		if (textStyle != null && textStyle.metrics != null) {
			GlyphMetrics metrics = textStyle.metrics; 
			runs[index] = OS.gcnew_SWTTextEmbeddedObject(textProperties, end - textSourceCharacterIndex, metrics.width, metrics.ascent + metrics.descent, metrics.ascent);
		} else {
			char ch = segmentsText.charAt(textSourceCharacterIndex);
			if (ch == '\n' || ch == '\r') {
				int breakLength = 1;
				if (ch == '\r' && textSourceCharacterIndex + 1 < end && segmentsText.charAt(textSourceCharacterIndex + 1) == '\n') breakLength++;
				runs[index] = OS.gcnew_TextEndOfLine(breakLength, textProperties);
			} else {
				int i = textSourceCharacterIndex;
				while (i < end && (ch = segmentsText.charAt(i)) != '\n' && ch != '\r') i++;
				runs[index] = OS.gcnew_TextCharacters(string, textSourceCharacterIndex, i - textSourceCharacterIndex, textProperties);
			}
		}
	}
	return runs[index];
}

int GetPrecedingText(int textSourceCharacterIndexLimit) {
	return 0;
}

/**
 * Gets the receiver's text, which will be an empty
 * string if it has never been set.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public String getText () {
	checkLayout();
	return text;
}

/**
 * Returns the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getWidth () {
	checkLayout();
	return wrapWidth;
}

/**
 * Returns <code>true</code> if the text layout has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the text layout.
 * When a text layout has been disposed, it is an error to
 * invoke any other method using the text layout.
 * </p>
 *
 * @return <code>true</code> when the text layout is disposed and <code>false</code> otherwise
 */
public boolean isDisposed () {
	return device == null;
}

/**
 * Sets the text alignment for the receiver. The alignment controls
 * how a line of text is positioned horizontally. The argument should
 * be one of <code>SWT.LEFT</code>, <code>SWT.RIGHT</code> or <code>SWT.CENTER</code>.
 * <p>
 * The default alignment is <code>SWT.LEFT</code>.  Note that the receiver's
 * width must be set in order to use <code>SWT.RIGHT</code> or <code>SWT.CENTER</code>
 * alignment.
 * </p>
 *
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setWidth(int)
 */
public void setAlignment (int alignment) {
	checkLayout();
	int mask = SWT.LEFT | SWT.CENTER | SWT.RIGHT;
	alignment &= mask;
	if (alignment == 0) return;
	if ((alignment & SWT.LEFT) != 0) alignment = SWT.LEFT;
	if ((alignment & SWT.RIGHT) != 0) alignment = SWT.RIGHT;
	if (this.alignment == alignment) return;
	freeRuns();
	this.alignment = alignment;
}

/**
 * Sets the ascent of the receiver. The ascent is distance in pixels
 * from the baseline to the top of the line and it is applied to all
 * lines. The default value is <code>-1</code> which means that the
 * ascent is calculated from the line fonts.
 *
 * @param ascent the new ascent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the ascent is less than <code>-1</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setDescent(int)
 * @see #getLineMetrics(int)
 */
public void setAscent(int ascent) {
	checkLayout();
	if (ascent < -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.ascent == ascent) return;
	freeRuns();
	this.ascent = ascent;
}

/**
 * Sets the descent of the receiver. The descent is distance in pixels
 * from the baseline to the bottom of the line and it is applied to all
 * lines. The default value is <code>-1</code> which means that the
 * descent is calculated from the line fonts.
 *
 * @param descent the new descent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the descent is less than <code>-1</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setAscent(int)
 * @see #getLineMetrics(int)
 */
public void setDescent(int descent) {
	checkLayout();
	if (descent < -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.descent == descent) return;
	freeRuns();
	this.descent = descent;
}

/** 
 * Sets the default font which will be used by the receiver
 * to draw and measure text. If the
 * argument is null, then a default font appropriate
 * for the platform will be used instead. Note that a text
 * style can override the default font.
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
	checkLayout();
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Font oldFont = this.font;
	if (oldFont == font) return;
	this.font = font;
	if (oldFont != null && oldFont.equals(font)) return;
	freeRuns();
}

/**
 * Sets the indent of the receiver. This indent it applied of the first line of 
 * each paragraph.  
 *
 * @param indent new indent
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setIndent (int indent) {
	checkLayout();
	if (indent < 0) return;	
	if (this.indent == indent) return;
	freeRuns();
	this.indent = indent;
}

/**
 * Sets the justification of the receiver. Note that the receiver's
 * width must be set in order to use justification. 
 *
 * @param justify new justify
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setJustify (boolean justify) {
	checkLayout();
	if (this.justify == justify) return;
	freeRuns();
	this.justify = justify;
}
 
/**
 * Sets the orientation of the receiver, which must be one
 * of <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @param orientation new orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setOrientation (int orientation) {
	checkLayout();
	int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	orientation &= mask;
	if (orientation == 0) return;
	if ((orientation & SWT.LEFT_TO_RIGHT) != 0) orientation = SWT.LEFT_TO_RIGHT;
	if (this.orientation == orientation) return;
	this.orientation = orientation;
	freeRuns();
}

/**
 * Sets the offsets of the receiver's text segments. Text segments are used to
 * override the default behaviour of the bidirectional algorithm.
 * Bidirectional reordering can happen within a text segment but not 
 * between two adjacent segments.
 * <p>
 * Each text segment is determined by two consecutive offsets in the 
 * <code>segments</code> arrays. The first element of the array should 
 * always be zero and the last one should always be equals to length of
 * the text.
 * </p>
 * 
 * @param segments the text segments offset
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setSegments(int[] segments) {
	checkLayout();
	if (this.segments == null && segments == null) return;
	if (this.segments != null && segments != null) {
		if (this.segments.length == segments.length) {
			int i;
			for (i = 0; i <segments.length; i++) {
				if (this.segments[i] != segments[i]) break;
			}
			if (i == segments.length) return;
		}
	}
	freeRuns();
	this.segments = segments;
}

/**
 * Sets the line spacing of the receiver.  The line spacing
 * is the space left between lines.
 *
 * @param spacing the new line spacing 
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the spacing is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setSpacing (int spacing) {
	checkLayout();
	if (spacing < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.lineSpacing == spacing) return;
	freeRuns();
	this.lineSpacing = spacing;
}

/**
 * Sets the style of the receiver for the specified range.  Styles previously
 * set for that range will be overwritten.  The start and end offsets are
 * inclusive and will be clamped if out of range.
 * 
 * @param style the style
 * @param start the start offset
 * @param end the end offset
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setStyle (TextStyle style, int start, int end) {
	checkLayout();
	int length = text.length();
	if (length == 0) return;
	if (start > end) return;
	start = Math.min(Math.max(0, start), length - 1);
	end = Math.min(Math.max(0, end), length - 1);
	int low = -1;
	int high = styles.length;
	while (high - low > 1) {
		int index = (high + low) / 2;
		if (styles[index + 1].start > start) {
			high = index;
		} else {
			low = index;
		}
	}
	if (0 <= high && high < styles.length) {
		StyleItem item = styles[high];
		if (item.start == start && styles[high + 1].start - 1 == end) {
			if (style == null) {
				if (item.style == null) return;
			} else {
				if (style.equals(item.style)) return;
			}
		}
	}
	freeRuns();
	int modifyStart = high;
	int modifyEnd = modifyStart;
	while (modifyEnd < styles.length) {
		if (styles[modifyEnd + 1].start > end) break;
		modifyEnd++;
	}
	if (modifyStart == modifyEnd) {
		int styleStart = styles[modifyStart].start; 
		int styleEnd = styles[modifyEnd + 1].start - 1;
		if (styleStart == start && styleEnd == end) {
			styles[modifyStart].style = style;
			return;
		}
		if (styleStart != start && styleEnd != end) {
			StyleItem[] newStyles = new StyleItem[styles.length + 2];
			System.arraycopy(styles, 0, newStyles, 0, modifyStart + 1);
			StyleItem item = new StyleItem();
			item.start = start;
			item.style = style;
			newStyles[modifyStart + 1] = item;	
			item = new StyleItem();
			item.start = end + 1;
			item.style = styles[modifyStart].style;
			newStyles[modifyStart + 2] = item;
			System.arraycopy(styles, modifyEnd + 1, newStyles, modifyEnd + 3, styles.length - modifyEnd - 1);
			styles = newStyles;
			return;
		}
	}
	if (start == styles[modifyStart].start) modifyStart--;
	if (end == styles[modifyEnd + 1].start - 1) modifyEnd++;
	int newLength = styles.length + 1 - (modifyEnd - modifyStart - 1);
	StyleItem[] newStyles = new StyleItem[newLength];
	System.arraycopy(styles, 0, newStyles, 0, modifyStart + 1);	
	StyleItem item = new StyleItem();
	item.start = start;
	item.style = style;
	newStyles[modifyStart + 1] = item;
	styles[modifyEnd].start = end + 1;
	System.arraycopy(styles, modifyEnd, newStyles, modifyStart + 2, styles.length - modifyEnd);
	styles = newStyles;
}

/**
 * Sets the receiver's tab list. Each value in the tab list specifies
 * the space in pixels from the origin of the text layout to the respective
 * tab stop.  The last tab stop width is repeated continuously.
 * 
 * @param tabs the new tab list
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setTabs (int[] tabs) {
	checkLayout();
	if (this.tabs == null && tabs == null) return;
	if (this.tabs != null && tabs !=null) {
		if (this.tabs.length == tabs.length) {
			int i;
			for (i = 0; i <tabs.length; i++) {
				if (this.tabs[i] != tabs[i]) break;
			}
			if (i == tabs.length) return;
		}
	}
	freeRuns();
	this.tabs = tabs;
} 

/**
 * Sets the receiver's text.
 *<p>
 * Note: Setting the text also clears all the styles. This method 
 * returns without doing anything if the new text is the same as 
 * the current text.
 * </p>
 * 
 * @param text the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setText (String text) {
	checkLayout();
	if (text == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (text.equals(this.text)) return;
	freeRuns();
	this.text = text;
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	styles[1].start = text.length();
}

/**
 * Sets the line width of the receiver, which determines how
 * text should be wrapped and aligned. The default value is
 * <code>-1</code> which means wrapping is disabled.
 *
 * @param width the new width 
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the width is <code>0</code> or less than <code>-1</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setAlignment(int)
 */
public void setWidth (int width) {
	checkLayout();
	if (width < -1 || width == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.wrapWidth == width) return;
	freeRuns();
	this.wrapWidth = width;
}

int validadeOffset(int offset, int step) {
	offset += step;
	if (segments != null && segments.length > 2) {
		for (int i = 0; i < segments.length; i++) {
			if (translateOffset(segments[i]) - 1 == offset) {
				offset += step;
				break;
			}
		}
	}	
	return offset;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "TextLayout {*DISPOSED*}";
	return "TextLayout {}";
}

int translateOffset(int offset) {
	if (segments == null) return offset;
	int nSegments = segments.length;
	if (nSegments <= 1) return offset;
	int length = text.length();
	if (length == 0) return offset;
	if (nSegments == 2) {
		if (segments[0] == 0 && segments[1] == length) return offset;
	}
	for (int i = 0; i < nSegments && offset - i >= segments[i]; i++) {
		offset++;
	}	
	return offset;
}

int untranslateOffset(int offset) {
	if (segments == null) return offset;
	int nSegments = segments.length;
	if (nSegments <= 1) return offset;
	int length = text.length();
	if (length == 0) return offset;
	if (nSegments == 2) {
		if (segments[0] == 0 && segments[1] == length) return offset;
	}
	for (int i = 0; i < nSegments && offset > segments[i]; i++) {
		offset--;
	}
	return offset;
}
}

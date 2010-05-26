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
package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;
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
	
	static class StyleItem {
		TextStyle style;
		int start;
		int atsuStyle;

		void createStyle(Device device, Font defaultFont) {
			if (atsuStyle != 0) return;
			int[] buffer = new int[1];
			OS.ATSUCreateStyle(buffer);
			atsuStyle = buffer[0];
			if (atsuStyle == 0) SWT.error(SWT.ERROR_NO_HANDLES);	
			int length = 0, ptrLength = 0, index = 0;
			Font font = null;
			RGBColor foreground = null;
			GlyphMetrics metrics = null;
			if (style != null) {
				font = style.font;
				if (style.foreground != null) {
					float[] color = style.foreground.handle;
					foreground = new RGBColor ();
					foreground.red = (short) (color [0] * 0xffff);
					foreground.green = (short) (color [1] * 0xffff);
					foreground.blue = (short) (color [2] * 0xffff);
				} else {
					if (style.underline && style.underlineStyle == SWT.UNDERLINE_LINK) {
						foreground = new RGBColor ();
						foreground.red = (short) 0;
						foreground.green = (short) 0x3333;
						foreground.blue = (short) 0x9999;
					}
				}
				metrics = style.metrics;
				if (isUnderlineSupported(style)) {
					length += 1;
					ptrLength += 1;
					if (style.underlineStyle == SWT.UNDERLINE_DOUBLE) {
						length += 1;
						ptrLength += 2;
					}
					if (style.underlineColor != null) {
						length += 1;
						ptrLength += 4;
					}
				}
				if (style.strikeout) {
					length += 1;
					ptrLength += 1;
					if (style.strikeoutColor != null) {
						length += 1;
						ptrLength += 4;
					}
				}
				if (metrics != null) {
					length += 4;
					ptrLength += 28;
				}
				if (style.rise != 0) {
					length += 1;
					ptrLength += 4;
				}
			}
			if (font == null) font = defaultFont;
			boolean synthesize = false;
			if (font != null) {
				length += 2;
				ptrLength += 8;
				synthesize = font.style != 0;
				if (synthesize) {
					length += 2;
					ptrLength += 2;
				}
			}
			if (foreground != null && metrics == null) {
				length += 1;
				ptrLength += RGBColor.sizeof;
			}
			byte[] buffer1 = new byte[1];
			int[] tags = new int[length];
			int[] sizes = new int[length];
			int[] values = new int[length];
			int ptr = OS.NewPtr(ptrLength), ptr1 = ptr;
			if (font != null) {
				buffer[0] = font.handle;
				tags[index] = OS.kATSUFontTag;
				sizes[index] = 4;
				values[index] = ptr1;
				OS.memmove(values[index], buffer, sizes[index]);
				ptr1 += sizes[index];
				index++;

				buffer[0] = OS.X2Fix(font.size);
				tags[index] = OS.kATSUSizeTag;
				sizes[index] = 4;
				values[index] = ptr1;
				OS.memmove(values[index], buffer, sizes[index]);
				ptr1 += sizes[index];
				index++;

				if (synthesize) {
					buffer1[0] = (font.style & OS.italic) != 0 ? (byte)1 : 0;
					tags[index] = OS.kATSUQDItalicTag;
					sizes[index] = 1;
					values[index] = ptr1;
					OS.memmove(values[index], buffer1, sizes[index]);
					ptr1 += sizes[index];
					index++;	

					buffer1[0] = (font.style & OS.bold) != 0 ? (byte)1 : 0;
					tags[index] = OS.kATSUQDBoldfaceTag;
					sizes[index] = 1;
					values[index] = ptr1;
					OS.memmove(values[index], buffer1, sizes[index]);
					ptr1 += sizes[index];
					index++;
				}
			}
			int underlineColor = 0, strikeoutColor = 0;;
			if (isUnderlineSupported(style)) {
				buffer1[0] = (byte)1;
				tags[index] = OS.kATSUQDUnderlineTag;
				sizes[index] = 1;
				values[index] = ptr1;
				OS.memmove(values[index], buffer1, sizes[index]);
				ptr1 += sizes[index];
				index++;
				if (style.underlineStyle == SWT.UNDERLINE_DOUBLE) {
					short buffer2[] = {OS.kATSUStyleDoubleLineCount};
					tags[index] = OS.kATSUStyleUnderlineCountOptionTag;
					sizes[index] = 2;
					values[index] = ptr1;
					OS.memmove(values[index], buffer2, sizes[index]);
					ptr1 += sizes[index];
					index++;
				}
				if (style.underlineColor != null) {
					buffer[0] = underlineColor = OS.CGColorCreate(device.colorspace, style.underlineColor.handle);
					tags[index] = OS.kATSUStyleUnderlineColorOptionTag;
					sizes[index] = 4;
					values[index] = ptr1;
					OS.memmove(values[index], buffer, sizes[index]);
					ptr1 += sizes[index];
					index++;
				}
			}
			if (style != null && style.strikeout) {
				buffer1[0] = (byte)1;
				tags[index] = OS.kATSUStyleStrikeThroughTag;
				sizes[index] = 1;
				values[index] = ptr1;
				OS.memmove(values[index], buffer1, sizes[index]);
				ptr1 += sizes[index];
				index++;
				if (style.strikeoutColor != null) {
					buffer[0] = strikeoutColor = OS.CGColorCreate(device.colorspace, style.strikeoutColor.handle);
					tags[index] = OS.kATSUStyleStrikeThroughColorOptionTag;
					sizes[index] = 4;
					values[index] = ptr1;
					OS.memmove(values[index], buffer, sizes[index]);
					ptr1 += sizes[index];
					index++;
				}
			}
			if (metrics != null) {
				buffer[0] = OS.Long2Fix(metrics.ascent);
				tags[index] = OS.kATSUAscentTag;
				sizes[index] = 4;
				values[index] = ptr1;
				OS.memmove(values[index], buffer, sizes[index]);
				ptr1 += sizes[index];
				index++;
				
				buffer[0] = OS.Long2Fix(metrics.descent);
				tags[index] = OS.kATSUDescentTag;
				sizes[index] = 4;
				values[index] = ptr1;
				OS.memmove(values[index], buffer, sizes[index]);
				ptr1 += sizes[index];
				index++;
				
				buffer[0] = OS.Long2Fix(metrics.width);
				tags[index] = OS.kATSUImposeWidthTag;
				sizes[index] = 4;
				values[index] = ptr1;
				OS.memmove(values[index], buffer, sizes[index]);
				ptr1 += sizes[index];
				index++;
				
				float[] ATSURGBAlphaColor = {0, 0, 0, 0};
				tags[index] = OS.kATSURGBAlphaColorTag;
				sizes[index] = 16;
				values[index] = ptr1;
				OS.memmove(values[index], ATSURGBAlphaColor, sizes[index]);
				ptr1 += sizes[index];
				index++;
			}
			if (style != null && style.rise != 0) {
				buffer[0] = OS.Long2Fix(style.rise);
				tags[index] = OS.kATSUCrossStreamShiftTag;
				sizes[index] = 4;
				values[index] = ptr1;
				OS.memmove(values[index], buffer, sizes[index]);
				ptr1 += sizes[index];
				index++;
			}
			if (foreground != null && metrics == null) {
				tags[index] = OS.kATSUColorTag;
				sizes[index] = RGBColor.sizeof;
				values[index] = ptr1;
				OS.memmove(values[index], foreground, sizes[index]);
				ptr1 += sizes[index];
				index++;
			}
			OS.ATSUSetAttributes(atsuStyle, tags.length, tags, sizes, values);
			OS.DisposePtr(ptr);	
			if (underlineColor != 0) OS.CGColorRelease (underlineColor);
			if (strikeoutColor != 0) OS.CGColorRelease (strikeoutColor);
		}

		void freeStyle() {
			if (atsuStyle == 0) return;
			OS.ATSUDisposeStyle(atsuStyle);
			atsuStyle = 0;
		}

		public String toString () {
			return "StyleItem {" + start + ", " + style + "}";
		}
	}
	
	Font font;
	String text;
	int textPtr;
	StyleItem[] styles;
	int stylesCount;
	int layout;
	int spacing, ascent, descent, indent, wrapIndent;
	int indentStyle;
	int[] tabs;
	int[] segments;
	char[] segmentsChars;
	int tabsPtr;
	int[] breaks, hardBreaks, lineX, lineWidth, lineHeight, lineAscent;

	static final int TAB_COUNT = 32;
	int[] invalidOffsets;
	static final char LTR_MARK = '\u200E', RTL_MARK = '\u200F', ZWS = '\u200B';
	
	static final int UNDERLINE_IME_INPUT = 1 << 16;
	static final int UNDERLINE_IME_TARGET_CONVERTED = 2 << 16;
	static final int UNDERLINE_IME_CONVERTED = 3 << 16;
	
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
	int[] buffer = new int[1];
	OS.ATSUCreateTextLayout(buffer);
	if (buffer[0] == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	layout = buffer[0];
	setLayoutControl(OS.kATSULineDirectionTag, OS.kATSULeftToRightBaseDirection, 1);
	int lineOptions = OS.kATSLineLastNoJustification | OS.kATSLineUseDeviceMetrics | OS.kATSLineKeepSpacesOutOfMargin;
	setLayoutControl(OS.kATSULineLayoutOptionsTag, lineOptions, 4);
	OS.ATSUSetHighlightingMethod(layout, OS.kRedrawHighlighting, new ATSUUnhighlightData());
	ascent = descent = -1;
	text = "";
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	stylesCount = 2;
	init();
}

void checkLayout() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

void computeRuns() {
	if (breaks != null) return;
	String segmentsText = getSegmentsText();
	int textLength = segmentsText.length();
	char[] chars = new char[textLength + 1];
	segmentsText.getChars(0, textLength, chars, 1);
	chars[0] = ZWS;
	int breakCount = 1;
	for (int i = 0; i < chars.length; i++) {
		char c = chars[i];
		if (c == '\n' || c == '\r') {
			breakCount++;
		}
	}
	hardBreaks = new int [breakCount];
	breakCount = 0;
	for (int i = 0; i < chars.length; i++) {
		char c = chars[i];
		if (c == '\n' || c == '\r') {
			chars[i] = ZWS;
			hardBreaks[breakCount++] = i;
		}
	}
	if (invalidOffsets != null) {
		for (int i = 0; i < invalidOffsets.length; i++) {
			invalidOffsets[i]++;
		}
	} else {
		invalidOffsets = new int[0];
	}

	hardBreaks[breakCount] = chars.length;
	int newTextPtr = OS.NewPtr(chars.length * 2);
	OS.memmove(newTextPtr, chars, chars.length * 2);
	OS.ATSUSetTextPointerLocation(layout, newTextPtr, 0, chars.length, chars.length);
	OS.ATSUSetTransientFontMatching(layout, true);
	if (textPtr != 0) OS.DisposePtr(textPtr);
	textPtr = newTextPtr;

	int[] buffer = new int[1];
	Font font = this.font != null ? this.font : device.systemFont;
	for (int i = 0; i < stylesCount - 1; i++) {
		StyleItem run = styles[i];
		run.createStyle(device, font);
		//set the default font in the ZWS when text is empty fixes text metrics
		int start = textLength != 0 ? translateOffset(run.start) : 0;
		int runLength = translateOffset(styles[i + 1].start) - start;
		OS.ATSUSetRunStyle(layout, run.atsuStyle, start, runLength);
	}
	int ptr = OS.NewPtr(12);
	buffer = new int[]{OS.Long2Fix(indent), 0, 0};
	OS.memmove(ptr, buffer, 12);
	int[] tags = new int[]{OS.kATSUImposeWidthTag, OS.kATSUAscentTag, OS.kATSUDescentTag};
	int[] sizes = new int[]{4, 4, 4};
	int[] values = new int[]{ptr, ptr + 4, ptr + 8};
	OS.ATSUCreateStyle(buffer);
	indentStyle = buffer[0];
	OS.ATSUSetAttributes(indentStyle, tags.length, tags, sizes, values);
	OS.DisposePtr(ptr);
	OS.ATSUSetRunStyle(layout, indentStyle, 0, 1);
	for (int i = 0; i < hardBreaks.length-1; i++) {
		int offset = hardBreaks[i];
		OS.ATSUSetRunStyle(layout, indentStyle, offset, 1);
	}
	OS.ATSUGetLayoutControl(layout, OS.kATSULineWidthTag, 4, buffer, null);
	int wrapWidth = buffer[0];
	for (int i=0, start=0; i<hardBreaks.length; i++) {
		int hardBreak = hardBreaks[i];
		buffer[0] = 0;
		if (wrapWidth != 0) OS.ATSUBatchBreakLines(layout, start, hardBreak - start, wrapWidth, buffer);
		OS.ATSUSetSoftLineBreak(layout, hardBreak);
		start = hardBreak;
	}
	OS.ATSUGetSoftLineBreaks(layout, 0, OS.kATSUToTextEnd, 0, null, buffer);
	int count = buffer[0];
	breaks = new int[count];
	OS.ATSUGetSoftLineBreaks(layout, 0, OS.kATSUToTextEnd, count, breaks, null);
	int lineCount = breaks.length;
	lineX = new int[lineCount];
	lineWidth = new int[lineCount];
	lineHeight = new int[lineCount];
	lineAscent = new int[lineCount];
	ATSTrapezoid trapezoid = new ATSTrapezoid();
	for (int i=0, start=0; i<lineCount; i++) {
		if (ascent != -1) {
			ptr = OS.NewPtr(4);
			buffer[0] = OS.kATSUseLineHeight;
			OS.memmove(ptr, buffer, 4);
			tags = new int[]{OS.kATSULineAscentTag};
			sizes = new int[]{4};
			values = new int[]{ptr};
			OS.ATSUSetLineControls(layout, start, tags.length, tags, sizes, values);
			OS.ATSUGetLineControl(layout, start, OS.kATSULineAscentTag, 4, buffer, null);
			buffer[0] = OS.Long2Fix(Math.max(ascent, OS.Fix2Long(buffer[0])));
			OS.memmove(ptr, buffer, 4);
			OS.ATSUSetLineControls(layout, start, tags.length, tags, sizes, values);
			OS.DisposePtr(ptr);
		}
		if (descent != -1) {
			ptr = OS.NewPtr(4);
			buffer[0] = OS.kATSUseLineHeight;
			OS.memmove(ptr, buffer, 4);
			tags = new int[]{OS.kATSULineDescentTag};
			sizes = new int[]{4};
			values = new int[]{ptr};
			OS.ATSUSetLineControls(layout, start, tags.length, tags, sizes, values);
			OS.ATSUGetLineControl(layout, start, OS.kATSULineDescentTag, 4, buffer, null);
			buffer[0] = OS.Long2Fix(Math.max(descent, OS.Fix2Long(buffer[0])));
			OS.memmove(ptr, buffer, 4);
			OS.ATSUSetLineControls(layout, start, tags.length, tags, sizes, values);
			OS.DisposePtr(ptr);
		}
		int lineBreak = breaks[i];
		int lineLength = lineBreak - start;
		OS.ATSUGetGlyphBounds(layout, 0, 0, start, lineLength, (short)OS.kATSUseDeviceOrigins, 1, trapezoid, null);
		lineX[i] = OS.Fix2Long(trapezoid.lowerLeft_x);
		lineAscent[i] = -OS.Fix2Long(trapezoid.upperRight_y);
		if (lineLength != 0) {
			lineWidth[i] = OS.Fix2Long(trapezoid.lowerRight_x) - OS.Fix2Long(trapezoid.lowerLeft_x);
		}
		lineHeight[i] = OS.Fix2Long(trapezoid.lowerRight_y) + lineAscent[i] + spacing;
		start = lineBreak;
	}
}

float[] computePolyline(int left, int top, int right, int bottom) {
	int height = bottom - top; // can be any number
	int width = 2 * height; // must be even
	int peaks = Compatibility.ceil(right - left, width);
	if (peaks == 0 && right - left > 2) {
		peaks = 1;
	}
	int length = ((2 * peaks) + 1) * 2;
	if (length < 0) return new float[0];
	
	float[] coordinates = new float[length];
	for (int i = 0; i < peaks; i++) {
		int index = 4 * i;
		coordinates[index] = left + (width * i);
		coordinates[index+1] = bottom;
		coordinates[index+2] = coordinates[index] + width / 2;
		coordinates[index+3] = top;
	}
	coordinates[length-2] = left + (width * peaks);
	coordinates[length-1] = bottom;
	return coordinates;
}

void destroy() {
	freeRuns();
	font = null;
	text = null;
	styles = null;
	if (layout != 0) OS.ATSUDisposeTextLayout(layout);
	layout = 0;
	if (textPtr != 0) OS.DisposePtr(textPtr);
	textPtr = 0;
	if (tabsPtr != 0) OS.DisposePtr(tabsPtr);
	tabsPtr = 0;
	if (indentStyle != 0) OS.ATSUDisposeStyle(indentStyle);
	indentStyle = 0;
	segments = null;
	segmentsChars = null;
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
public void draw(GC gc, int x, int y) {
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
public void draw(GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground) {
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
public void draw(GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground, int flags) {
	checkLayout ();
	computeRuns();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionForeground != null && selectionForeground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionBackground != null && selectionBackground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int length = translateOffset(text.length());
	if (length == 0 && flags == 0) return;
	gc.checkGC(GC.FOREGROUND_FILL);
	if (gc.data.updateClip) gc.setCGClipping();
	OS.CGContextSaveGState(gc.handle);
	setLayoutControl(OS.kATSUCGContextTag, gc.handle, 4);
	boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
	boolean restoreColor = false;
	if (hasSelection || (flags & SWT.LAST_LINE_SELECTION) != 0) {
		if (selectionBackground != null) {
			restoreColor = true;
			int color = OS.CGColorCreate(device.colorspace, selectionBackground.handle);
			setLayoutControl(OS.kATSULineHighlightCGColorTag, color, 4);
			OS.CGColorRelease(color);
		} else {
			selectionBackground = device.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		}
	}
	/* 
	* Feature in ATSU. There is no API to set a background attribute
	* of an ATSU style. Draw the background of styles ourselfs.
	*/
	int rgn = 0;
	CGRect rect = null;
	for (int j = 0; j < stylesCount; j++) {
		StyleItem run = styles[j];
		TextStyle style = run.style;
		if (style == null || style.background == null) continue;
		int start = translateOffset(run.start);
		int end = j + 1 < stylesCount ? translateOffset(styles[j + 1].start - 1) : length;
		for (int i=0, lineStart=0, lineY = 0; i<breaks.length; i++) {
			int lineBreak = breaks[i];
			int lineEnd = lineBreak - 1;
			if (!(start > lineEnd || end < lineStart)) {
				int highStart = Math.max(lineStart, start);
				int highEnd = Math.min(lineEnd, end);
				int highLen = highEnd - highStart + 1;
				if (highLen > 0) {
					OS.CGContextSaveGState(gc.handle);
					if (rgn == 0) rgn = OS.NewRgn();
					OS.ATSUGetTextHighlight(layout, OS.Long2Fix(x), OS.Long2Fix(y + lineY + lineAscent[i]), highStart, highLen, rgn);
					int shape = OS.HIShapeCreateWithQDRgn(rgn);
					OS.HIShapeReplacePathInCGContext(shape, gc.handle);
					if (rect == null) rect = new CGRect();
					OS.CGContextGetPathBoundingBox(gc.handle, rect);
					OS.CGContextEOClip(gc.handle);
					OS.CGContextSetFillColorSpace(gc.handle, device.colorspace);
					OS.CGContextSetFillColor(gc.handle, style.background.handle);
					OS.CGContextFillRect(gc.handle, rect);
					OS.DisposeControl(shape);
					OS.CGContextRestoreGState(gc.handle);
				}
			}
			if (lineEnd > end) break;
			lineY += lineHeight[i];
			lineStart = lineBreak;
		}
	}

	selectionStart = translateOffset(selectionStart);
	selectionEnd = translateOffset(selectionEnd);
	OS.CGContextScaleCTM(gc.handle, 1, -1);
	int drawX = OS.Long2Fix(x);
	int drawY = y;
	for (int i=0, start=0; i<breaks.length; i++) {
		int lineBreak = breaks[i];
		int lineLength = lineBreak - start;
		if (lineLength > 0) {
			int fixYDraw = OS.Long2Fix(-(drawY + lineAscent[i]));
			OS.ATSUDrawText(layout, start, lineLength, drawX, fixYDraw);
			int end = start + lineLength - 1;
			if (flags != 0 && (hasSelection || (flags & SWT.LAST_LINE_SELECTION) != 0)) {
				boolean extent = false;
				if (i == breaks.length - 1 && (flags & SWT.LAST_LINE_SELECTION) != 0) {
					extent = true;
				} else {
					boolean hardBreak = false;
					for (int j = 0; j < hardBreaks.length; j++) {
						if (end + 1 == hardBreaks[j]) {
							hardBreak = true;
							break;
						}
					}
					if (hardBreak) {
						if (selectionStart <= end + 1 && end + 1 <= selectionEnd) extent = true;
					} else {
						if (selectionStart <= end + 1 && end + 1 < selectionEnd && (flags & SWT.FULL_SELECTION) != 0) {
							extent = true;
						}
					}
				}
				if (extent) {
					if (rect == null) rect = new CGRect();
					rect.x = x + lineWidth[i];
					rect.y = drawY;
					rect.width = (flags & SWT.FULL_SELECTION) != 0 ? 0x7fffffff : lineHeight[i] / 3;
					rect.height = lineHeight[i];
					OS.CGContextSaveGState(gc.handle);
					OS.CGContextTranslateCTM(gc.handle, 0, -(lineHeight[i] + 2 * drawY));
					OS.CGContextSetFillColorSpace(gc.handle, device.colorspace);
					OS.CGContextSetFillColor(gc.handle, selectionBackground.handle);
					OS.CGContextFillRect(gc.handle, rect);
					OS.CGContextRestoreGState(gc.handle);
				}
			}
			if (hasSelection && !(selectionStart > end || start > selectionEnd)) {
				int selStart = Math.max(selectionStart, start);
				int selEnd = Math.min(selectionEnd, end);
				OS.ATSUHighlightText(layout, drawX, fixYDraw, selStart, selEnd - selStart + 1);
			}
		}
		drawY += lineHeight[i];
		start = lineBreak;
	}
	if (restoreColor) setLayoutControl(OS.kATSULineHighlightCGColorTag, 0, 4);
	OS.CGContextRestoreGState(gc.handle);

	for (int j = 0; j < stylesCount; j++) {
		StyleItem run = styles[j];
		TextStyle style = run.style;
		if (style == null) continue;
		boolean drawUnderline = style.underline && !isUnderlineSupported(style);
		drawUnderline = drawUnderline && (j + 1 == stylesCount || !style.isAdherentUnderline(styles[j + 1].style)); 
		boolean drawBorder = style.borderStyle != SWT.NONE;
		drawBorder = drawBorder && (j + 1 == stylesCount || !style.isAdherentBorder(styles[j + 1].style)); 
		if (!drawUnderline && !drawBorder) continue;
		int end = j + 1 < stylesCount ? translateOffset(styles[j + 1].start - 1) : length;
		for (int i=0, lineStart=0, lineY = 0; i<breaks.length; i++) {
			int lineBreak = breaks[i];
			int lineEnd = lineBreak - 1;
			if (drawUnderline) {
				int start = run.start;
				for (int k = j; k > 0 && style.isAdherentUnderline(styles[k - 1].style); k--) {
					start = styles[k - 1].start;
				}
				start = translateOffset(start);
				if (!(start > lineEnd || end < lineStart)) {
					int highStart = Math.max(lineStart, start);
					int highEnd = Math.min(lineEnd, end);
					int highLen = highEnd - highStart + 1;
					if (highLen > 0) {
						OS.CGContextSaveGState(gc.handle);
						float underlineY = y + lineY;
						float[] foreground = gc.data.foreground;
						float lineWidth = 1;
						float[] dashes = null;
						int lineCap = OS.kCGLineCapButt;
						int lineJoin = OS.kCGLineJoinMiter;
						switch (style.underlineStyle) {
							case SWT.UNDERLINE_ERROR:
								lineWidth = 2;
								dashes = new float[]{1, 3};
								lineCap = OS.kCGLineCapRound;
								lineJoin = OS.kCGLineJoinRound;
								//FALLTHROUGH
							case SWT.UNDERLINE_SQUIGGLE: 
								if (style.underlineColor != null) {
									foreground = style.underlineColor.handle;
								} else {
									if (style.foreground != null) {
										foreground = style.foreground.handle;
									}
								}
								underlineY += 2 * lineAscent [i] + lineWidth;
								break;
							case UNDERLINE_IME_INPUT:
							case UNDERLINE_IME_TARGET_CONVERTED:
							case UNDERLINE_IME_CONVERTED:
								lineWidth = 1.5f;
								foreground = style.underlineStyle == UNDERLINE_IME_CONVERTED ? new float[]{0.5f, 0.5f, 0.5f, 1} : new float[]{0, 0, 0, 1};
								Font font = style.font;
								if (font == null) font = this.font != null ? this.font : device.systemFont;
								ATSFontMetrics metrics = new ATSFontMetrics();
								OS.ATSFontGetHorizontalMetrics(font.handle, OS.kATSOptionFlagsDefault, metrics);
								underlineY += lineAscent [i] + lineHeight [i] + (metrics.descent * font.size);
								break;
						}
						OS.CGContextSetStrokeColorSpace(gc.handle, device.colorspace);
						OS.CGContextSetStrokeColor(gc.handle, foreground);
						OS.CGContextSetLineWidth(gc.handle, lineWidth);
						OS.CGContextSetLineCap(gc.handle, lineCap);
						OS.CGContextSetLineJoin(gc.handle, lineJoin);
						OS.CGContextSetLineDash(gc.handle, 0, dashes, dashes != null ? dashes.length : 0);
						OS.CGContextTranslateCTM(gc.handle, 0.5f, 0.5f);
						
						int[] count = new int[1];
						OS.ATSUGetGlyphBounds(layout, OS.Long2Fix(x), OS.X2Fix(underlineY), highStart, highLen, (short)OS.kATSUseDeviceOrigins, 0, 0, count);
						int trapezoidsPtr = OS.malloc(count[0] * ATSTrapezoid.sizeof);
						OS.ATSUGetGlyphBounds(layout, OS.Long2Fix(x), OS.X2Fix(underlineY), highStart, highLen, (short)OS.kATSUseDeviceOrigins, count[0], trapezoidsPtr, count);
						ATSTrapezoid trapezoid = new ATSTrapezoid();
						for (int k = 0; k < count[0]; k++) {
							OS.memmove(trapezoid, trapezoidsPtr + (k * ATSTrapezoid.sizeof), ATSTrapezoid.sizeof);
							float left, right;
							if (trapezoid.upperLeft_x != trapezoid.lowerLeft_x) {
								float ux = OS.Fix2Long(trapezoid.upperLeft_x);
								float uy = OS.Fix2Long(trapezoid.upperLeft_y);
								float lx = OS.Fix2Long(trapezoid.lowerLeft_x);
								float ly = OS.Fix2Long(trapezoid.lowerLeft_y);
								float a = (uy - ly) / (ux - lx);
								float b = uy - ux * a;
								left = (underlineY - b) / a;
							} else {
								left = OS.Fix2Long(trapezoid.upperLeft_x);
							}
							if (trapezoid.upperRight_x != trapezoid.lowerRight_x) {
								float ux = OS.Fix2Long(trapezoid.upperRight_x);
								float uy = OS.Fix2Long(trapezoid.upperRight_y);
								float lx = OS.Fix2Long(trapezoid.lowerRight_x);
								float ly = OS.Fix2Long(trapezoid.lowerRight_y);
								float a = (uy - ly) / (ux - lx);
								float b = uy - ux * a;
								right = (underlineY - b) / a;
							} else {
								right = OS.Fix2Long(trapezoid.upperRight_x);
							}
							switch (style.underlineStyle) {
								case UNDERLINE_IME_TARGET_CONVERTED:
								case UNDERLINE_IME_CONVERTED:
									left += 1;
									right -= 1;
							}
							if (style.underlineStyle == SWT.UNDERLINE_SQUIGGLE) {
								int lineBottom = y + lineY + lineHeight[i];
								int squigglyThickness = 1;
								int squigglyHeight = 2 * squigglyThickness;
								float squigglyY = Math.min(OS.Fix2Long(trapezoid.upperLeft_y) - squigglyHeight / 2, lineBottom - squigglyHeight - 1);
								float[] points = computePolyline((int)left, (int)squigglyY, (int)right, (int)(squigglyY + squigglyHeight));
								OS.CGContextBeginPath(gc.handle);
								OS.CGContextAddLines(gc.handle, points, points.length / 2);
							} else {
								OS.CGContextMoveToPoint(gc.handle, left, OS.Fix2Long(trapezoid.upperLeft_y));
								OS.CGContextAddLineToPoint(gc.handle, right, OS.Fix2Long(trapezoid.upperRight_y));
							}
						}
						OS.free(trapezoidsPtr);
						OS.CGContextStrokePath(gc.handle);
						OS.CGContextRestoreGState(gc.handle);
					}
				}
			}
			
			if (drawBorder) {
				int start = run.start;
				for (int k = j; k > 0 && style.isAdherentBorder(styles[k - 1].style); k--) {
					start = styles[k - 1].start;
				}
				start = translateOffset(start);
				if (!(start > lineEnd || end < lineStart)) {
					int highStart = Math.max(lineStart, start);
					int highEnd = Math.min(lineEnd, end);
					int highLen = highEnd - highStart + 1;
					if (highLen > 0) {
						OS.CGContextSaveGState(gc.handle);
						int[] count = new int[1];
						OS.ATSUGetGlyphBounds(layout, OS.Long2Fix(x), OS.Long2Fix(y + lineY + lineAscent[i]), highStart, highLen, (short)OS.kATSUseDeviceOrigins, 0, 0, count);
						int trapezoidsPtr = OS.malloc(count[0] * ATSTrapezoid.sizeof);
						OS.ATSUGetGlyphBounds(layout, OS.Long2Fix(x), OS.Long2Fix(y + lineY + lineAscent[i]), highStart, highLen, (short)OS.kATSUseDeviceOrigins, count[0], trapezoidsPtr, count);
						ATSTrapezoid trapezoid = new ATSTrapezoid();
						for (int k = 0; k < count[0]; k++) {
							OS.memmove(trapezoid, trapezoidsPtr + (k * ATSTrapezoid.sizeof), ATSTrapezoid.sizeof);
							int upperY = y + lineY + 1;
							int lowerY = y + lineY + lineHeight[i];
							OS.CGContextMoveToPoint(gc.handle, OS.Fix2Long(trapezoid.lowerLeft_x), lowerY);
							OS.CGContextAddLineToPoint(gc.handle, OS.Fix2Long(trapezoid.upperLeft_x), upperY);
							OS.CGContextAddLineToPoint(gc.handle, OS.Fix2Long(trapezoid.upperRight_x) - 1, upperY);
							OS.CGContextAddLineToPoint(gc.handle, OS.Fix2Long(trapezoid.lowerRight_x) - 1, lowerY);
							OS.CGContextClosePath(gc.handle);
						}
						OS.free(trapezoidsPtr);
						int width = 1;
						OS.CGContextSetShouldAntialias(gc.handle, false);
						OS.CGContextSetLineCap(gc.handle, OS.kCGLineCapButt);
						OS.CGContextSetLineJoin(gc.handle, OS.kCGLineJoinMiter);
						OS.CGContextSetLineWidth(gc.handle, width);
						float[] dashes = null;
						switch (style.borderStyle) {
							case SWT.BORDER_SOLID:	break;
							case SWT.BORDER_DASH: dashes = width != 0 ? GC.LINE_DASH : GC.LINE_DASH_ZERO; break;
							case SWT.BORDER_DOT: dashes = width != 0 ? GC.LINE_DOT : GC.LINE_DOT_ZERO; break;
						}
						OS.CGContextSetLineDash(gc.handle, 0, dashes, dashes != null ? dashes.length : 0);
						float[] color = null;
						if (style.borderColor != null) color = style.borderColor.handle;
						if (color == null && style.foreground != null) color = style.foreground.handle;
						if (color != null) {
							OS.CGContextSetStrokeColorSpace(gc.handle, device.colorspace);
							OS.CGContextSetStrokeColor(gc.handle, color);
						}
						OS.CGContextTranslateCTM (gc.handle, 0.5f, 0.5f);
						OS.CGContextStrokePath(gc.handle);
						OS.CGContextRestoreGState(gc.handle);
					}
				}
			}
			if (lineEnd > end) break;
			lineY += lineHeight[i];
			lineStart = lineBreak;
		}
	}
	if (rgn != 0) OS.DisposeRgn(rgn);
}

void freeRuns() {
	if (breaks == null) return;
	for (int i = 0; i < stylesCount; i++) {
		StyleItem run = styles[i];
		run.freeStyle();
	}
	if (indentStyle != 0) OS.ATSUDisposeStyle(indentStyle);
	indentStyle = 0;
	breaks = lineX = lineWidth = lineHeight = lineAscent = null;
	invalidOffsets = null;
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
public int getAlignment() {
	checkLayout();
	int[] buffer = new int[1];
	OS.ATSUGetLayoutControl(layout, OS.kATSULineFlushFactorTag, 4, buffer, null);
	switch (buffer[0]) {
		case OS.kATSUCenterAlignment: return SWT.CENTER;
		case OS.kATSUEndAlignment: return SWT.RIGHT;
	}
	return SWT.LEFT;
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
public Rectangle getBounds() {
	checkLayout();
	computeRuns();
	int width = 0, height = 0;
	int length = text.length();
	if (length == 0) {
		Font font = this.font != null ? this.font : device.systemFont;
		ATSFontMetrics metrics = new ATSFontMetrics();
		OS.ATSFontGetVerticalMetrics(font.handle, OS.kATSOptionFlagsDefault, metrics);
		OS.ATSFontGetHorizontalMetrics(font.handle, OS.kATSOptionFlagsDefault, metrics);
		int ascent = (int)(0.5f + metrics.ascent * font.size);
		int descent = (int)(0.5f + (-metrics.descent + metrics.leading) * font.size);
		ascent = Math.max(ascent, this.ascent);
		descent = Math.max(descent, this.descent);
		height = ascent + descent;
	} else {
		for (int i=0; i<breaks.length; i++) {
			width = Math.max(width, lineWidth[i]);
			height += lineHeight[i];
		}
	}
	int[] buffer = new int[1];
	OS.ATSUGetLayoutControl(layout, OS.kATSULineWidthTag, 4, buffer, null);
	int wrapWidth = OS.Fix2Long(buffer[0]);
	if (wrapWidth != 0) width = Math.max(width, wrapWidth);
	return new Rectangle(0, 0, width, height);
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
public Rectangle getBounds(int start, int end) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (length == 0) return new Rectangle(0, 0, 0, 0);
	if (start > end) return new Rectangle(0, 0, 0, 0);
	start = Math.min(Math.max(0, start), length - 1);
	end = Math.min(Math.max(0, end), length - 1);
	start = translateOffset(start);
	end = translateOffset(end);
	for (int i = 0; i < hardBreaks.length; i++) {
		if (start == hardBreaks[i]) {
			if (start > 0) start--;
		}
		if (end == hardBreaks[i]) {
			if (end > 0) end--;
		}
	}
	int rgn = OS.NewRgn();
	Rect rect = new Rect();
	Rect rect1 = new Rect();
	for (int i=0, lineStart=0, lineY = 0; i<breaks.length; i++) {
		int lineBreak = breaks[i];
		int lineEnd = lineBreak - 1;
		if (!(start > lineEnd || end < lineStart)) {
			int highStart = Math.max(lineStart, start);
			int highEnd = Math.min(lineEnd, end);
			int highLen = highEnd - highStart + 1;
			if (highLen > 0) {
				OS.ATSUGetTextHighlight(layout, 0, OS.Long2Fix(lineY + lineAscent[i]), highStart, highLen, rgn);
				OS.GetRegionBounds(rgn, rect1);
				OS.UnionRect(rect, rect1, rect);
			}
		}
		if (lineEnd > end) break;
		lineY += lineHeight[i];
		lineStart = lineBreak;
	}
	OS.DisposeRgn(rgn);
	return new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
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
	int[] buffer = new int[1];
	OS.ATSUGetLayoutControl(layout, OS.kATSULineJustificationFactorTag, 4, buffer, null);
	return buffer[0] == OS.kATSUFullJustification;
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
public int getLevel(int offset) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	offset = translateOffset(offset);	
	int level = 0;
	//TODO
	return level;
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
public int[] getLineOffsets() {
	checkLayout ();
	computeRuns();
	int[] offsets = new int[breaks.length + 1];
	for (int i = 1; i < offsets.length; i++) {
		int offset = breaks[i - 1];
		for (int j = 0; j < hardBreaks.length - 1; j++) {
			if (offset == hardBreaks[j]) {
				offset++;
				break;
			}
		}
		offsets[i] = untranslateOffset(offset);	
	}
	return offsets;
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
public int getLineIndex(int offset) {
	checkLayout ();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	offset = translateOffset(offset);
	for (int i = 0; i < hardBreaks.length - 1; i++) {
		if (offset == hardBreaks[i]) {
			if (offset > 0) offset--;
			break;
		}
	}
	for (int i=0; i<breaks.length-1; i++) {
		int lineBreak = breaks[i];
		if (lineBreak > offset) return i;
	}
	return breaks.length - 1;
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
	int lineCount = breaks.length;
	if (!(0 <= lineIndex && lineIndex < lineCount)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int lineY = 0;
	for (int i=0; i<lineIndex; i++) {
		lineY += lineHeight[i];
	}
	int lineX = this.lineX[lineIndex];
	int lineWidth = this.lineWidth[lineIndex];
	int lineHeight = this.lineHeight[lineIndex] - spacing;
	return new Rectangle(lineX, lineY, lineWidth, lineHeight);
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
public int getLineCount() {
	checkLayout ();
	computeRuns();
	return breaks.length;
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
	checkLayout ();
	computeRuns();
	int lineCount = breaks.length;
	if (!(0 <= lineIndex && lineIndex < lineCount)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int length = text.length();
	if (length == 0) {
		Font font = this.font != null ? this.font : device.systemFont;
		ATSFontMetrics metrics = new ATSFontMetrics();
		OS.ATSFontGetVerticalMetrics(font.handle, OS.kATSOptionFlagsDefault, metrics);
		OS.ATSFontGetHorizontalMetrics(font.handle, OS.kATSOptionFlagsDefault, metrics);
		int ascent = (int)(0.5f + metrics.ascent * font.size);
		int descent = (int)(0.5f + (-metrics.descent + metrics.leading) * font.size);
		ascent = Math.max(ascent, this.ascent);
		descent = Math.max(descent, this.descent);
		return FontMetrics.carbon_new(ascent, descent, 0, 0, ascent + descent);
	}
	int start = lineIndex == 0 ? 0 : breaks[lineIndex - 1];
	int lineLength = breaks[lineIndex] - start;
	int[] ascent = new int[1], descent = new int[1];
	OS.ATSUGetUnjustifiedBounds(layout, start, lineLength, null, null, ascent, descent);
	int height = OS.Fix2Long(ascent[0]) + OS.Fix2Long(descent[0]);
	return FontMetrics.carbon_new(OS.Fix2Long(ascent[0]), OS.Fix2Long(descent[0]), 0, 0, height);
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
public Point getLocation(int offset, boolean trailing) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	if (length == 0) return new Point(0, 0);
	offset = translateOffset(offset);
	for (int i = 0; i < hardBreaks.length; i++) {
		if (offset == hardBreaks[i]) {
			trailing = true;
			if (offset > 0) offset--;
			break;
		}
	}
	int lineY = 0;
	for (int i=0; i<breaks.length-1; i++) {
		int lineBreak = breaks[i];
		if (lineBreak > offset) break;
		lineY += lineHeight[i];
	}
	if (trailing) offset++;
	ATSUCaret caret = new ATSUCaret();
	OS.ATSUOffsetToPosition(layout, offset, !trailing, caret, null, null);
	return new Point(Math.min(OS.Fix2Long(caret.fX), OS.Fix2Long(caret.fDeltaX)), lineY);
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
	return _getOffset(offset, movement, true);
}

int _getOffset (int offset, int movement, boolean forward) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	if (length == 0) return 0;
	offset = translateOffset(offset);
	int newOffset;
	int type = OS.kATSUByCharacter;
	switch (movement) {
		case SWT.MOVEMENT_CLUSTER: type = OS.kATSUByCharacterCluster; break;
		case SWT.MOVEMENT_WORD: type = OS.kATSUByWord; break;
	}
	if (forward) {
		offset = _getNativeOffset(offset, type, forward);
		newOffset = untranslateOffset(offset);
		if (movement == SWT.MOVEMENT_WORD || movement == SWT.MOVEMENT_WORD_END) {
			while (newOffset < length && 
					(!(!Compatibility.isLetterOrDigit(text.charAt(newOffset)) &&
					Compatibility.isLetterOrDigit(text.charAt(newOffset - 1))))) {
				offset = _getNativeOffset(offset, type, forward);
				newOffset = untranslateOffset(offset);
			}
		}
		if (movement == SWT.MOVEMENT_WORD_START) {
			while (newOffset < length && 
					(!(Compatibility.isLetterOrDigit(text.charAt(newOffset)) &&
					!Compatibility.isLetterOrDigit(text.charAt(newOffset - 1))))) {
				offset = _getNativeOffset(offset, type, forward);
				newOffset = untranslateOffset(offset);
			}
		}
	} else {
		offset = _getNativeOffset(offset, type, forward);
		newOffset = untranslateOffset(offset);
		if (movement == SWT.MOVEMENT_WORD || movement == SWT.MOVEMENT_WORD_START) {
			while (newOffset > 0 && 
					(!(Compatibility.isLetterOrDigit(text.charAt(newOffset)) && 
					!Compatibility.isLetterOrDigit(text.charAt(newOffset - 1))))) {
				offset = _getNativeOffset(offset, type, forward);
				newOffset = untranslateOffset(offset);
			}
		}
		if (movement == SWT.MOVEMENT_WORD_END) {
			while (newOffset > 0 && 
					(!(!Compatibility.isLetterOrDigit(text.charAt(newOffset)) && 
					Compatibility.isLetterOrDigit(text.charAt(newOffset - 1))))) {
				offset = _getNativeOffset(offset, type, forward);
				newOffset = untranslateOffset(offset);
			}
		}
	}
	return newOffset;
}

int _getNativeOffset(int offset, int movement, boolean forward) {
	int[] buffer = new int [1];
	boolean invalid = false;
	do {
		if (forward) {
			OS.ATSUNextCursorPosition(layout, offset, movement, buffer);
		} else {
			OS.ATSUPreviousCursorPosition(layout, offset, movement, buffer);
		}
		if (buffer[0] == offset) return offset;
		offset = buffer[0];
		invalid = false;
		for (int i = 0; i < invalidOffsets.length; i++) {
			if (offset == invalidOffsets[i]) {
				invalid = true;
				break;
			}
		}
	} while (invalid);
	return offset;
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
public int getOffset(Point point, int[] trailing) {
	checkLayout();
	if (point == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return getOffset(point.x, point.y, trailing);
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
public int getOffset(int x, int y, int[] trailing) {
	checkLayout();
	computeRuns();
	if (trailing != null && trailing.length < 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int length = text.length();
	if (length == 0) return 0;
	int lineY = 0, start = 0, lineIndex;
	for (lineIndex=0; lineIndex<breaks.length-1; lineIndex++) {
		int lineBreak = breaks[lineIndex];
		int height = lineHeight[lineIndex];
		if (lineY + height > y) break;
		lineY += height;
		start = lineBreak;
	}
	int[] offset = new int[]{start};
	boolean[] leading = new boolean[1];
	OS.ATSUPositionToOffset(layout, OS.Long2Fix(x), OS.Long2Fix(y - lineY), offset, leading, null);
	if (trailing != null) trailing[0] = (leading[0] ? 0 : 1);
	if (!leading[0]) offset[0]--;
	for (int i = 0; i < hardBreaks.length; i++) {
		if (offset[0] == hardBreaks[i]) {
			offset[0]++;
			break;
		}
	}
	offset[0] = untranslateOffset(offset[0]);
	if (offset[0] > length - 1) {
		offset[0] = length - 1;
		if (trailing != null) trailing[0] = 1; 
	}
	return offset[0];
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
public int getOrientation() {
	checkLayout();
	int[] lineDir = new int[1];
	OS.ATSUGetLayoutControl(layout, OS.kATSULineDirectionTag, 1, lineDir, null);
	return lineDir[0] == OS.kATSURightToLeftBaseDirection ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
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
public int getPreviousOffset (int index, int movement) {
	return _getOffset(index, movement, false);
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
	int[] result = new int[stylesCount * 2];
	int count = 0;
	for (int i=0; i<stylesCount - 1; i++) {
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
public int[] getSegments() {
	checkLayout();
	return segments;
}

/**
 * Returns the segments characters of the receiver.
 *
 * @return the segments characters
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.6
 */
public char[] getSegmentsChars () {
	checkLayout();
	return segmentsChars;
}

String getSegmentsText() {
	int length = text.length();
	if (length == 0) return text;
	if (segments == null) return text;
	int nSegments = segments.length;
	if (nSegments == 0) return text;
	if (segmentsChars == null) {
		if (nSegments == 1) return text;
		if (nSegments == 2) {
			if (segments[0] == 0 && segments[1] == length) return text;
		}
	}
	invalidOffsets = new int[nSegments];
	char[] oldChars = new char[length];
	text.getChars(0, length, oldChars, 0);
	char[] newChars = new char[length + nSegments];
	int charCount = 0, segmentCount = 0;
	char defaultSeparator = getOrientation() == SWT.RIGHT_TO_LEFT ? RTL_MARK : LTR_MARK;
	while (charCount < length) {
		if (segmentCount < nSegments && charCount == segments[segmentCount]) {
			invalidOffsets[segmentCount] = charCount + segmentCount;
			char separator = segmentsChars != null && segmentsChars.length > segmentCount ? segmentsChars[segmentCount] : defaultSeparator;
			newChars[charCount + segmentCount++] = separator;
		} else {
			newChars[charCount + segmentCount] = oldChars[charCount++];
		}
	}
	while (segmentCount < nSegments) {
		invalidOffsets[segmentCount] = charCount + segmentCount;
		segments[segmentCount] = charCount;
		char separator = segmentsChars != null && segmentsChars.length > segmentCount ? segmentsChars[segmentCount] : defaultSeparator;
		newChars[charCount + segmentCount++] = separator;
	}
	return new String(newChars, 0, newChars.length);
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
	return spacing;
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
	for (int i=1; i<stylesCount; i++) {
		StyleItem item = styles[i];
		if (item.start > offset) {
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
	TextStyle[] result = new TextStyle[stylesCount];
	int count = 0;
	for (int i=0; i<stylesCount; i++) {
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
public int[] getTabs() {
	checkLayout();
	return tabs;
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
	checkLayout ();
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
	checkLayout ();
	int[] buffer = new int[1];
	OS.ATSUGetLayoutControl(layout, OS.kATSULineWidthTag, 4, buffer, null);
	int wrapWidth = OS.Fix2Long(buffer[0]);
	return wrapWidth == 0 ? -1 : wrapWidth;
}

/**
* Returns the receiver's wrap indent.
*
* @return the receiver's wrap indent
* 
* @exception SWTException <ul>
*    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
* </ul>
* 
* @since 3.6
*/
public int getWrapIndent () {
	checkLayout();	
	return wrapIndent;
}

/*
 * Returns true if the underline style is supported natively by ATSUI
 */
static boolean isUnderlineSupported (TextStyle style) {
	if (style != null && style.underline) {
		int uStyle = style.underlineStyle;
		return uStyle == SWT.UNDERLINE_SINGLE || uStyle == SWT.UNDERLINE_DOUBLE || uStyle == SWT.UNDERLINE_LINK;
	}
	return false;
}

/**
 * Returns <code>true</code> if the text layout has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the text layout.
 * When a text layout has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the text layout.
 * </p>
 *
 * @return <code>true</code> when the text layout is disposed and <code>false</code> otherwise
 */
public boolean isDisposed () {
	return layout == 0;
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
	if (alignment == getAlignment()) return;
	freeRuns();
	if ((alignment & SWT.LEFT) != 0) alignment = SWT.LEFT; 
	if ((alignment & SWT.RIGHT) != 0) alignment = SWT.RIGHT; 
	int align = OS.kATSUStartAlignment;
	switch (alignment) {
		case SWT.CENTER: align = OS.kATSUCenterAlignment; break;
		case SWT.RIGHT: align = OS.kATSUEndAlignment; break;
	}
	setLayoutControl(OS.kATSULineFlushFactorTag, align, 4);
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
public void setAscent (int ascent) {
	checkLayout ();
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
public void setDescent (int descent) {
	checkLayout ();
	if (descent < -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.descent == descent) return;
	freeRuns();
	this.descent = descent;
}

void setLayoutControl(int tag, int value, int size) {
	int ptr1 = OS.NewPtr(size);
	if (size == 1) {
		byte[] buffer = new byte[1];
		buffer[0] = (byte) value;
		OS.memmove(ptr1, buffer, size);
	} else {
		int[] buffer = new int[1];
		buffer[0] = value;
		OS.memmove(ptr1, buffer, size);
	}
	int[] tags = new int[]{tag};
	int[] sizes = new int[]{size};
	int[] values = new int[]{ptr1};
	OS.ATSUSetLayoutControls(layout, tags.length, tags, sizes, values);
	OS.DisposePtr(ptr1);
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
	checkLayout ();
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Font oldFont = this.font;
	if (oldFont == font) return;
	this.font = font;
	if (oldFont != null && oldFont.equals(font)) return;
	freeRuns();
}

/**
 * Sets the indent of the receiver. This indent is applied to the first line of 
 * each paragraph.  
 *
 * @param indent new indent
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setWrapIndent(int)
 * 
 * @since 3.2
 */
public void setIndent (int indent) {
	checkLayout ();
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
	checkLayout ();
	if (justify == getJustify()) return;
	freeRuns();
	setLayoutControl(OS.kATSULineJustificationFactorTag, justify ? OS.kATSUFullJustification : OS.kATSUNoJustification, 4);
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
public void setOrientation(int orientation) {
	checkLayout();
	int mask = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
	orientation &= mask;
	if (orientation == 0) return;
	if ((orientation & SWT.LEFT_TO_RIGHT) != 0) orientation = SWT.LEFT_TO_RIGHT;
	if (orientation == getOrientation()) return;
	freeRuns();
	int lineDir = OS.kATSULeftToRightBaseDirection;
	if (orientation == SWT.RIGHT_TO_LEFT) lineDir = OS.kATSURightToLeftBaseDirection;
	setLayoutControl(OS.kATSULineDirectionTag, lineDir, 1);
}

/**
 * Sets the offsets of the receiver's text segments. Text segments are used to
 * override the default behavior of the bidirectional algorithm.
 * Bidirectional reordering can happen within a text segment but not 
 * between two adjacent segments.
 * <p>
 * Each text segment is determined by two consecutive offsets in the 
 * <code>segments</code> arrays. The first element of the array should 
 * always be zero and the last one should always be equals to length of
 * the text.
 * </p>
 * <p>
 * When segments characters are set, the segments are the offsets where
 * the characters are inserted in the text.
 * <p> 
 * 
 * @param segments the text segments offset
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setSegmentsChars(char[])
 */
public void setSegments(int[] segments) {
	checkLayout();
	if (this.segments == null && segments == null) return;
	if (this.segments != null && segments !=null) {
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
 * Sets the characters to be used in the segments boundaries. The segments 
 * are set by calling <code>setSegments(int[])</code>. The application can
 * use this API to insert Unicode Control Characters in the text to control
 * the display of the text and bidi reordering. The characters are not 
 * accessible by any other API in <code>TextLayout</code>.
 * 
 * @param segmentsChars the segments characters 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setSegments(int[])
 * 
 * @since 3.6
 */
public void setSegmentsChars(char[] segmentsChars) {
	checkLayout();
	if (this.segmentsChars == null && segmentsChars == null) return;
	if (this.segmentsChars != null && segmentsChars != null) {
		if (this.segmentsChars.length == segmentsChars.length) {
			int i;
			for (i = 0; i <segmentsChars.length; i++) {
				if (this.segmentsChars[i] != segmentsChars[i]) break;
			}
			if (i == segmentsChars.length) return;
		}
	}
	freeRuns();
	this.segmentsChars = segmentsChars;
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
	if (this.spacing == spacing) return;
	freeRuns();
	this.spacing = spacing;
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
	int high = stylesCount;
	while (high - low > 1) {
		int index = (high + low) / 2;
		if (styles[index + 1].start > start) {
			high = index;
		} else {
			low = index;
		}
	}
	if (0 <= high && high < stylesCount) {
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
	while (modifyEnd < stylesCount) {
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
			int newLength = stylesCount + 2; 
			if (newLength > styles.length) {
				int newSize = Math.min(newLength + 1024, Math.max(64, newLength * 2));
				StyleItem[] newStyles = new StyleItem[newSize];
				System.arraycopy(styles, 0, newStyles, 0, stylesCount);
				styles = newStyles;
			}
			System.arraycopy(styles, modifyEnd + 1, styles, modifyEnd + 3, stylesCount - modifyEnd - 1);
			StyleItem item = new StyleItem();
			item.start = start;
			item.style = style;
			styles[modifyStart + 1] = item;	
			item = new StyleItem();
			item.start = end + 1;
			item.style = styles[modifyStart].style;
			styles[modifyStart + 2] = item;
			stylesCount = newLength;
			return;
		}
	}
	if (start == styles[modifyStart].start) modifyStart--;
	if (end == styles[modifyEnd + 1].start - 1) modifyEnd++;
	int newLength = stylesCount + 1 - (modifyEnd - modifyStart - 1);
	if (newLength > styles.length) {
		int newSize = Math.min(newLength + 1024, Math.max(64, newLength * 2));
		StyleItem[] newStyles = new StyleItem[newSize];
		System.arraycopy(styles, 0, newStyles, 0, stylesCount);
		styles = newStyles;
	}
	System.arraycopy(styles, modifyEnd, styles, modifyStart + 2, stylesCount - modifyEnd);
	StyleItem item = new StyleItem();
	item.start = start;
	item.style = style;
	styles[modifyStart + 1] = item;
	styles[modifyStart + 2].start = end + 1;
	stylesCount = newLength;
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
public void setTabs(int[] tabs) {
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
	if (tabsPtr != 0) OS.DisposePtr(tabsPtr);
	tabsPtr = 0;
	if (tabs == null) {
		OS.ATSUSetTabArray(layout, 0, 0);
	} else {
		ATSUTab tab = new ATSUTab();
		tab.tabPosition = OS.Long2Fix(0);
		int length = Math.max(TAB_COUNT, tabs.length);
		int ptr = tabsPtr = OS.NewPtr(ATSUTab.sizeof * length), i, offset;
		for (i=0, offset=ptr; i<tabs.length; i++, offset += ATSUTab.sizeof) {
			tab.tabType = (short)OS.kATSULeftTab;
			tab.tabPosition = OS.Long2Fix(tabs[i]);
			OS.memmove(offset, tab, ATSUTab.sizeof);
		}
		int width = i - 2 >= 0 ? tabs[i - 1] - tabs[i - 2] : tabs[i - 1];
		if (width > 0) {
			for (; i<length; i++, offset += ATSUTab.sizeof) {
				tab.tabType = (short)OS.kATSULeftTab;
				tab.tabPosition += OS.Long2Fix(width);
				OS.memmove(offset, tab, ATSUTab.sizeof);
			}
		}
		OS.ATSUSetTabArray(layout, ptr, i);
	}
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
	checkLayout ();
	if (text == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (text.equals(this.text)) return;
	freeRuns();
	this.text = text;
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	styles[1].start = text.length();
	stylesCount = 2;
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
	checkLayout ();
	if (width < -1 || width == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (width == getWidth()) return;
	freeRuns();
	setLayoutControl(OS.kATSULineWidthTag, OS.Long2Fix(Math.max(0, width)), 4);
}

/**
 * Sets the wrap indent of the receiver. This indent is applied to all lines
 * in the paragraph except the first line.  
 *
 * @param wrapIndent new wrap indent
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setIndent(int)
 * 
 * @since 3.6
 */
public void setWrapIndent (int wrapIndent) {
	checkLayout ();
	if (wrapIndent < 0) return;
	if (this.wrapIndent == wrapIndent) return;
	freeRuns();
	this.wrapIndent = wrapIndent;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "TextLayout {*DISPOSED*}";
	return "TextLayout {" + layout + "}";
}

/*
 *  Translate a client offset to an internal offset
 */
int translateOffset(int offset) {
	offset++;
	for (int i = 0; i < invalidOffsets.length; i++) {
		if (offset < invalidOffsets[i]) break; 
		offset++;
	}
	return offset;
}

/*
 *  Translate an internal offset to a client offset
 */
int untranslateOffset(int offset) {
	int i = 0;
	while (i < invalidOffsets.length && offset > invalidOffsets[i]) {
		i++;
	}
	return Math.max(0, offset - i - 1);
}

}

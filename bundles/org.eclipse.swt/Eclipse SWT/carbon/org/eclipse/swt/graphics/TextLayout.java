/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.*;

public final class TextLayout {

	final public static int MOVEMENT_CHAR = 1;
	final public static int MOVEMENT_CLUSTER = 2;
	final public static int MOVEMENT_WORD = 4;
	
	static class StyleItem {
		TextStyle style;
		int start;
		int atsuStyle;

		void createStyle(Font defaultFont) {
			if (atsuStyle != 0) return;
			int[] buffer = new int[1];
			OS.ATSUCreateStyle(buffer);
			atsuStyle = buffer[0];
			if (atsuStyle == 0) SWT.error(SWT.ERROR_NO_HANDLES);	
			int length = 0, ptrLength = 0, index = 0;
			Font font = null;
			Color foreground = null;
			if (style != null) {
				font = style.font;
				foreground = style.foreground;
			}
			if (font == null) font = defaultFont;
			if (font != null) {
				length += 2;
				ptrLength += 8;
			}
			if (foreground != null) {
				length += 1;
				ptrLength += RGBColor.sizeof;
			}
			int[] tags = new int[length];
			int[] sizes = new int[length];
			int[] values = new int[length];
			int ptr = OS.NewPtr(ptrLength), ptr1 = ptr;
			if (font != null) {
				buffer[0] = font.handle;
				tags[index] = OS.kATSUFontTag;
				sizes[index] = 4;
				values[index] = ptr1;
				OS.memcpy(values[index], buffer, sizes[index]);
				ptr1 += sizes[index];
				index++;

				buffer[0] = OS.X2Fix(font.size);
				tags[index] = OS.kATSUSizeTag;
				sizes[index] = 4;
				values[index] = ptr1;
				OS.memcpy(values[index], buffer, sizes[index]);
				ptr1 += sizes[index];
				index++;
			}
			if (foreground != null) {
				RGBColor rgb = new RGBColor ();
				float[] color = foreground.handle;
				rgb.red = (short) (color [0] * 0xffff);
				rgb.green = (short) (color [1] * 0xffff);
				rgb.blue = (short) (color [2] * 0xffff);		
				tags[index] = OS.kATSUColorTag;
				sizes[index] = RGBColor.sizeof;
				values[index] = ptr1;
				OS.memcpy(values[index], rgb, sizes[index]);
				ptr1 += sizes[index];
				index++;
			}
			OS.ATSUSetAttributes(atsuStyle, tags.length, tags, sizes, values);
			OS.DisposePtr(ptr);	
		}

		void freeStyle() {
			if (atsuStyle == 0) return;
			OS.ATSUDisposeStyle(atsuStyle);
			atsuStyle = 0;
		}
	}
	
	Device device;
	Font font;
	String text;
	int textPtr;
	StyleItem[] styles;
	int layout;
	int spacing;	
	int[] tabs;
	int[] segments;
	int tabsPtr;
	int[] breaks, lineX, lineWidth, lineHeight, lineAscent;

	static final int TAB_COUNT = 32;
	
public TextLayout (Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;	
	int[] buffer = new int[1];
	OS.ATSUCreateTextLayout(buffer);
	if (buffer[0] == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	layout = buffer[0];	
	int ptr = OS.NewPtr(4);
	buffer[0] = OS.kATSLineUseDeviceMetrics;
	OS.memcpy(ptr, buffer, 4);		
	int[] tags = new int[]{OS.kATSULineLayoutOptionsTag};
	int[] sizes = new int[]{4};
	int[] values = new int[]{ptr};
	OS.ATSUSetLayoutControls(layout, tags.length, tags, sizes, values);
	OS.DisposePtr(ptr);	
	OS.ATSUSetHighlightingMethod(layout, 1, new ATSUUnhighlightData());
	text = "";
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
}

void checkLayout() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

void computeRuns() {
	if (breaks != null) return;
	int length = text.length();
	if (length != 0) {
		for (int i = 0; i < styles.length - 1; i++) {
			StyleItem run = styles[i];
			run.createStyle(font);
			int runLength = styles[i + 1].start - run.start;
			OS.ATSUSetRunStyle(layout, run.atsuStyle, run.start, runLength);
		}
		int[] buffer = new int[1];
		OS.ATSUGetLayoutControl(layout, OS.kATSULineWidthTag, 4, buffer, null);
		int wrapWidth = OS.Fix2Long(buffer[0]);
		int width = wrapWidth == 0 ? 0x7fff : wrapWidth;
		OS.ATSUBatchBreakLines(layout, 0, OS.kATSUToTextEnd, OS.Long2Fix(width), buffer);
		int count = Math.max(0, buffer[0]);
		breaks = new int[count + 1];
		OS.ATSUGetSoftLineBreaks(layout, 0, OS.kATSUToTextEnd, count, breaks, buffer);
		breaks[count] = length;
	} else {
		breaks = new int[1];
	}
	int lineCount = breaks.length;
	lineX = new int[lineCount];
	lineWidth = new int[lineCount];
	lineHeight = new int[lineCount];
	lineAscent = new int[lineCount];
	if (length != 0) {
		ATSTrapezoid trapezoid = new ATSTrapezoid();
		for (int i=0, start=0; i<lineCount; i++) {
			int lineBreak = breaks[i];
			int lineLength = skipHardBreak(lineBreak) - start;
			OS.ATSUGetGlyphBounds(layout, 0, 0, start, lineLength == 0 ? 1 : lineLength, (short)OS.kATSUseDeviceOrigins, 1, trapezoid, null);
			lineX[i] = OS.Fix2Long(trapezoid.lowerLeft_x);
			lineAscent[i] = -OS.Fix2Long(trapezoid.upperRight_y);
			if (lineLength != 0) {
				lineWidth[i] = OS.Fix2Long(trapezoid.upperRight_x) - OS.Fix2Long(trapezoid.upperLeft_x);
			}
			lineHeight[i] = OS.Fix2Long(trapezoid.lowerRight_y) + lineAscent[i];
			start = lineBreak;
		}
	}
}

public void dispose() {
	if (layout == 0) return;
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
	device = null;
}

public void draw(GC gc, int x, int y) {
	draw(gc, x, y, -1, -1, null, null);
}

public void draw(GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground) {
	checkLayout ();
	computeRuns();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionForeground != null && selectionForeground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionBackground != null && selectionBackground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int length = text.length();
	if (length == 0) return;
	int[] buffer = new int[1];
	int ptr = OS.NewPtr(4);
	buffer[0] = gc.handle;
	OS.memcpy(ptr, buffer, 4);	
	int[] tags = new int[]{OS.kATSUCGContextTag};
	int[] sizes = new int[]{4};
	int[] values = new int[]{ptr};
	OS.ATSUSetLayoutControls(layout, tags.length, tags, sizes, values);
	OS.DisposePtr(ptr);	
	boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
	OS.CGContextSaveGState(gc.handle);

	/* 
	* Feature in ATSU. There is no API to set a background attribute
	* of an ATSU style. Draw the background of styles ourselfs.
	*/
	Rectangle rect = null;
	Region clipping = null, region = null;
	for (int j = 0; j < styles.length; j++) {
		StyleItem run = styles[j];
		if (run.style == null || run.style.background == null) continue;
		OS.CGContextSetFillColor(gc.handle, run.style.background.handle);
		if (clipping == null) {
			region = new Region();
			clipping = new Region();
			gc.getClipping(clipping);
			rect = clipping.getBounds();
		}
		int start = run.start;
		int end = j + 1 < styles.length ? styles[j + 1].start - 1 : length;
		for (int i=0, lineStart=0, lineY = 0; i<breaks.length; i++) {
			int lineBreak = breaks[i];
			int lineEnd = lineBreak - 1;
			if (!(start > lineEnd || end < lineStart)) {
				int highStart = Math.max(lineStart, start);
				int highEnd = Math.min(lineEnd, end);
				int highLen = skipHardBreak(highEnd) - highStart + 1;
				if (highLen > 0) {
					OS.ATSUGetTextHighlight(layout, lineX[i], lineY, highStart, highLen, region.handle);
					OS.OffsetRgn(region.handle, (short)0, (short)(lineY + lineAscent[i]));
					OS.OffsetRgn(region.handle, (short)x, (short)y);
					region.intersect(clipping);
					gc.setClipping(region);
					gc.fillRectangle(rect);
				}
			}
			if (lineEnd > end) break;
			lineY += lineHeight[i];
			lineStart = lineBreak;
		}
	}
	if (clipping != null) {
		OS.CGContextRestoreGState(gc.handle);
		OS.CGContextSaveGState(gc.handle);
		clipping.dispose();
		region.dispose();
	}

	OS.CGContextScaleCTM(gc.handle, 1, -1);
	OS.CGContextSetFillColor(gc.handle, gc.data.foreground);
	int drawX = OS.Long2Fix(x);
	int drawY = y;
	for (int i=0, start=0; i<breaks.length; i++) {
		int lineBreak = breaks[i];
		int lineLength = skipHardBreak(lineBreak) - start;
		if (lineLength > 0) {
			int fixYDraw = OS.Long2Fix(-(drawY + lineAscent[i]));
			OS.ATSUDrawText(layout, start, lineLength, drawX, fixYDraw);
			int end = start + lineLength - 1;
			if (hasSelection && !(selectionStart > end || start > selectionEnd)) {
				int selStart = Math.max(selectionStart, start);
				int selEnd = Math.min(selectionEnd, end);
				OS.ATSUHighlightText(layout, drawX, fixYDraw, selStart, selEnd - selStart + 1);
			}
		}
		drawY += lineHeight[i];
		start = lineBreak;
	}
	OS.CGContextRestoreGState(gc.handle);
}

void freeRuns() {
	if (breaks == null) return;
	for (int i = 0; i < styles.length; i++) {
		StyleItem run = styles[i];
		run.freeStyle();
	}
	breaks = lineX = lineWidth = lineHeight = lineAscent = null;
}

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

public Rectangle getBounds() {
	checkLayout();
	computeRuns();
	int width = 0, height = 0;
	for (int i=0; i<breaks.length; i++) {
		width = Math.max(width, lineWidth[i]);
		height += lineHeight[i];
	}
	int[] buffer = new int[1];
	OS.ATSUGetLayoutControl(layout, OS.kATSULineWidthTag, 4, buffer, null);
	int wrapWidth = OS.Fix2Long(buffer[0]);
	if (wrapWidth != 0) width = Math.max(width, wrapWidth);
	return new Rectangle(0, 0, width, height);
}

public Rectangle getBounds(int start, int end) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (length == 0) return new Rectangle(0, 0, 0, 0);
	if (start > end) return new Rectangle(0, 0, 0, 0);
	start = Math.min(Math.max(0, start), length - 1);
	end = Math.min(Math.max(0, end), length - 1);
	int rgn = OS.NewRgn();
	Rect rect = new Rect();
	Rect rect1 = new Rect();
	for (int i=0, lineStart=0, lineY = 0; i<breaks.length; i++) {
		int lineBreak = breaks[i];
		int lineEnd = lineBreak - 1;
		if (!(start > lineEnd || end < lineStart)) {
			int highStart = Math.max(lineStart, start);
			int highEnd = Math.min(lineEnd, end);
			int highLen = skipHardBreak(highEnd) - highStart + 1;
			if (highLen > 0) {
				OS.ATSUGetTextHighlight(layout, lineX[i], lineY, highStart, highLen, rgn);
				OS.GetRegionBounds(rgn, rect1);
				OS.OffsetRect(rect1, (short)0, (short)(lineY + lineAscent[i]));
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

public Font getFont () {
	checkLayout();
	return font;
}

public int getLevel(int offset) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int level = 0;
	//TODO
	return level;
}

public Point getLineOffsets(int lineIndex) {
	checkLayout ();
	computeRuns();
	int lineCount = breaks.length;
	if (!(0 <= lineIndex && lineIndex < lineCount)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int start = lineIndex == 0 ? 0 : breaks[lineIndex - 1];
	int end = breaks[lineIndex] - 1;
	return new Point(start, Math.max(start, end));
}

public int getLineIndex(int offset) {
	checkLayout ();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	for (int i=0; i<breaks.length-1; i++) {
		int lineBreak = breaks[i];
		if (lineBreak > offset) return i;
	}
	return breaks.length - 1;
}

public Rectangle getLineBounds(int lineIndex) {
	checkLayout();
	computeRuns();
	int lineCount = breaks.length;
	if (!(0 <= lineIndex && lineIndex < lineCount)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int lineY = 0;
	for (int i=0, start=0; i<lineIndex; i++) {
		int lineBreak = breaks[i];
		lineY += lineHeight[i];
		start = lineBreak;
	}
	return new Rectangle(lineX[lineIndex], lineY, lineWidth[lineIndex], lineHeight[lineIndex]);
}

public int getLineCount() {
	checkLayout ();
	computeRuns();
	return breaks.length;
}

public FontMetrics getLineMetrics (int lineIndex) {
	checkLayout ();
	computeRuns();
	int lineCount = breaks.length;
	if (!(0 <= lineIndex && lineIndex < lineCount)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int length = text.length();
	if (length == 0) {
		Font font = this.font != null ? this.font : device.getSystemFont();
		FontInfo info = new FontInfo();
		OS.FetchFontInfo(font.id, font.size, font.style, info);
		int ascent = info.ascent;
		int descent = info.descent;
		int leading = info.leading;
		return FontMetrics.carbon_new(ascent, descent, 0, leading, ascent + leading + descent);
	}
	int start = lineIndex == 0 ? 0 : breaks[lineIndex - 1];
	int lineLength = breaks[lineIndex] - start;
	int[] ascent = new int[1], descent = new int[1];
	OS.ATSUGetUnjustifiedBounds(layout, start, lineLength, null, null, ascent, descent);
	int height = OS.Fix2Long(ascent[0]) + OS.Fix2Long(descent[0]);
	return FontMetrics.carbon_new(OS.Fix2Long(ascent[0]), OS.Fix2Long(descent[0]), 0, 0, height);
}

public Point getLocation(int offset, boolean trailing) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	if (length == 0) return new Point(0, 0);
	int lineY = 0;
	for (int i=0; i<breaks.length-1; i++) {
		int lineBreak = breaks[i];
		if (lineBreak > offset) break;
		lineY += lineHeight[i];
	}
	if (offset != length && text.charAt(offset) != '\n' && trailing) offset++;
	ATSUCaret caret = new ATSUCaret();
	OS.ATSUOffsetToPosition(layout, offset, !trailing, caret, null, null);
	return new Point(OS.Fix2Long(caret.fX), lineY);
}

public int getNextOffset (int offset, int movement) {
	return _getOffset(offset, movement, true);
}

int _getOffset (int offset, int movement, boolean forward) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	if (length == 0) return 0;
	int[] newOffset = new int[1];
	int type = OS.kATSUByCharacter;
	switch (movement) {
		case MOVEMENT_CLUSTER: type = OS.kATSUByCharacterCluster; break;
		case MOVEMENT_WORD: type = OS.kATSUByWord; break;
	}
	if (forward) {
		OS.ATSUNextCursorPosition(layout, offset, type, newOffset);
		if (movement == MOVEMENT_WORD) {
			while (newOffset[0] < length && Compatibility.isWhitespace(text.charAt(newOffset[0]))) {
				newOffset[0]++;
			}
		}
	} else {
		OS.ATSUPreviousCursorPosition(layout, offset, type, newOffset);
		if (movement == MOVEMENT_WORD) {
			while (newOffset[0] > 0 && !Compatibility.isWhitespace(text.charAt(newOffset[0] - 1))) {
				newOffset[0]--;
			}
		}
	}
	return newOffset[0];
}

public int getOffset(Point point, int[] trailing) {
	checkLayout();
	computeRuns();
	if (point == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return getOffset(point.x, point.y, trailing);
}

public int getOffset(int x, int y, int[] trailing) {
	checkLayout();
	computeRuns();
	if (trailing != null && trailing.length < 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int length = text.length();
	if (length == 0) return 0;
	int lineY = 0, start = 0;
	for (int i=0; i<breaks.length-1; i++) {
		int lineBreak = breaks[i];
		int height = lineHeight[i];
		if (lineY + height > y) break;
		lineY += height;
		start = lineBreak;
	}
	int[] offset = new int[]{start};
	boolean[] leading = new boolean[1];
	OS.ATSUPositionToOffset(layout, OS.Long2Fix(x), OS.Long2Fix(y - lineY), offset, leading, null);
	if (trailing != null) trailing[0] = (leading[0] ? 0 : 1);
	if (!leading[0]) offset[0]--;
	return offset[0];
}

public int getOrientation() {
	checkLayout();
	int[] lineDir = new int[1];
	OS.ATSUGetLayoutControl(layout, OS.kATSULineDirectionTag, 1, lineDir, null);
	return lineDir[0] == OS.kATSURightToLeftBaseDirection ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
}

public int getPreviousOffset (int index, int movement) {
	return _getOffset(index, movement, false);
}

public int[] getSegments() {
	checkLayout();
	return segments;
}

public int getSpacing () {
	checkLayout();	
	return spacing;
}

public TextStyle getStyle (int offset) {
	checkLayout();
	int length = text.length();
	if (!(0 <= offset && offset < length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	for (int i=1; i<styles.length; i++) {
		StyleItem item = styles[i];
		if (item.start > offset) {
			return styles[i - 1].style;
		}
	}
	return null;
}

public int[] getTabs() {
	checkLayout();
	return tabs;
}

public String getText () {
	checkLayout ();
	return text;
}

public int getWidth () {
	checkLayout ();
	int[] buffer = new int[1];
	OS.ATSUGetLayoutControl(layout, OS.kATSULineWidthTag, 4, buffer, null);
	int wrapWidth = OS.Fix2Long(buffer[0]);
	return wrapWidth == 0 ? -1 : wrapWidth;
}

public boolean isDisposed () {
	return layout == 0;
}

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

void setLayoutControl(int tag, int value, int size) {
	int[] buffer = new int[1];
	int ptr1 = OS.NewPtr(4);
	buffer[0] = value;
	OS.memcpy(ptr1, buffer, 4);
	int[] tags = new int[]{tag};
	int[] sizes = new int[]{size};
	int[] values = new int[]{ptr1};
	OS.ATSUSetLayoutControls(layout, tags.length, tags, sizes, values);
	OS.DisposePtr(ptr1);
}

public void setFont (Font font) {
	checkLayout ();
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.font == font) return;
	if (font != null && font.equals(this.font)) return;
	freeRuns();
	this.font = font;
}

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

public void setSpacing (int spacing) {
	checkLayout();
	if (spacing < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.spacing == spacing) return;
	this.spacing = spacing;
}

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
		if (start <= styles[index].start) {
			high = index;
		} else {
			low = index;
		}
	}
	if (0 <= high && high < styles.length) {
		StyleItem item = styles[high];
		if (item.start == start && styles[high + 1].start - 1 == end) {
			if (style == item.style) return;
			if (style != null && style.equals(item.style)) return;
		}
	}
	freeRuns();
	int count = 0, i;
	StyleItem[] newStyles = new StyleItem[styles.length + 2];
	for (i = 0; i < styles.length; i++) {
		StyleItem item = styles[i];
		if (item.start >= start) break;
		newStyles[count++] = item;
	}
	StyleItem newItem = new StyleItem();
	newItem.start = start;
	newItem.style = style;
	newStyles[count++] = newItem;
	if (styles[i].start > end) {
		newItem = new StyleItem();
		newItem.start = end + 1;
		newItem.style = styles[i -1].style;
		newStyles[count++] = newItem;
	} else {
		for (; i<styles.length; i++) {
			StyleItem item = styles[i];
			if (item.start > end) break;
		}
		if (end != styles[i].start - 1) {
			i--;
			styles[i].start = end + 1;
		}
	}
	for (; i<styles.length; i++) {
		StyleItem item = styles[i];
		if (item.start > end) newStyles[count++] = item;
	}
	if (newStyles.length != count) {
		styles = new StyleItem[count];
		System.arraycopy(newStyles, 0, styles, 0, count);
	} else {
		styles = newStyles;
	}
}

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
			tab.tabType = OS.kATSULeftTab;
			tab.tabPosition += OS.Long2Fix(tabs[i]);
			OS.memcpy(offset, tab, ATSUTab.sizeof);
		}
		int width = i - 2 >= 0 ? tabs[i - 1] - tabs[i - 2] : tabs[i - 1];
		if (width > 0) {
			for (; i<length; i++, offset += ATSUTab.sizeof) {
				tab.tabType = OS.kATSULeftTab;
				tab.tabPosition += OS.Long2Fix(width);
				OS.memcpy(offset, tab, ATSUTab.sizeof);
			}
		}
		OS.ATSUSetTabArray(layout, ptr, i);
	}
}

public void setText (String text) {
	checkLayout ();
	if (text == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (text.equals(this.text)) return;
	freeRuns();
	this.text = text;
	int length = text.length();
	if (length != 0) {
		char[] chars = new char[length];
		text.getChars(0, length, chars, 0);
		textPtr = OS.NewPtr(length * 2);
		OS.memcpy(textPtr, chars, length * 2);
		OS.ATSUSetTextPointerLocation(layout, textPtr, 0, length, length);
		OS.ATSUSetTransientFontMatching(layout, true);
	}
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	styles[styles.length - 1].start = text.length();
}

public void setWidth (int width) {
	checkLayout ();
	if (width < -1 || width == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (width == getWidth()) return;
	freeRuns();
	setLayoutControl(OS.kATSULineWidthTag, OS.Long2Fix(width), 4);
}

int skipHardBreak(int lineBreak) {
	if (true) return lineBreak;
	while (lineBreak > 0) {
		char c = text.charAt(lineBreak - 1);
		switch (c) {
			case '\r':
			case '\n':
				break;
			default:
				return lineBreak;
		}
		lineBreak--;
	}
	return lineBreak;
}

} 
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
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

public final class TextLayout {
	Device device;
	Font font;
	String text;
	int lineSpacing;
	int alignment;
	int wrapWidth;
	int orientation;
	int[] tabs;
	int[] segments;
	StyleItem[] styles;
	
	StyleItem[][] runs;
	int[] lineOffset, lineY, lineWidth;
	int defaultFontHeight;
	
	static class StyleItem {
		TextStyle style;
		int start, length, width, height, baseline;
		boolean lineBreak, softBreak, tab;
	}
	
public TextLayout (Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	wrapWidth = -1;
	lineSpacing = 0;
	orientation = SWT.LEFT_TO_RIGHT;
	defaultFontHeight = getFontHeigth(device.getSystemFont());
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	if (device.tracking) device.new_Object(this);
}

void checkLayout () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

int stringWidth (StyleItem run, char[] ch) {
	if (ch.length == 0) return 0;
	int fontList = getItemFont(run).handle;
	byte[] buffer = Converter.wcsToMbcs(null, ch, true);
	int xmString = OS.XmStringCreateLocalized(buffer);
	int width = OS.XmStringWidth(fontList, xmString);
	OS.XmStringFree(xmString);
	return width;
}

void computeRuns () {
	if (runs != null) return;
	StyleItem[] allRuns = itemize();
	for (int i=0; i<allRuns.length-1; i++) {
		StyleItem run = allRuns[i];
		place(run);
	}
	int lineWidth = 0, lineStart = 0, lineCount = 1;
	for (int i=0; i<allRuns.length - 1; i++) {
		StyleItem run = allRuns[i];
		if (run.length == 1) {
			char ch = text.charAt(run.start);
			switch (ch) {
				case '\t': {
					run.tab = true;
					run.baseline = 0;
					if (tabs == null) break;
					int tabsLength = tabs.length, j;
					for (j = 0; j < tabsLength; j++) {
						if (tabs[j] > lineWidth) {
							run.width = tabs[j] - lineWidth;
							break;
						}
					}
					if (j == tabsLength) {
						int tabX = tabs[tabsLength-1];
						int lastTabWidth = tabsLength > 1 ? tabs[tabsLength-1] - tabs[tabsLength-2] : tabs[0];
						if (lastTabWidth > 0) {
							while (tabX <= lineWidth) tabX += lastTabWidth;
							run.width = tabX - lineWidth;
						}
					}
					break;
				}
				case '\n':
					run.lineBreak = true;
					run.baseline = run.width = 0;
					break;
				case '\r':
					run.lineBreak = true;
					run.baseline = run.width = 0;
					StyleItem next = allRuns[i + 1];
					if (next.length != 0 && text.charAt(next.start) == '\n') {
						run.length += 1;
						i++;
					}
					break;
			}
		}
		if (wrapWidth != -1 && lineWidth + run.width > wrapWidth) {
			int start = 0;
			char[] chars = new char[run.length];
			text.getChars(run.start, run.start + run.length, chars, 0);
			int width = 0, maxWidth = wrapWidth - lineWidth;
			char[] buffer = new char[1];
			buffer[0] = chars[start];
			int charWidth = stringWidth(run, buffer);
			while (width + charWidth < maxWidth) {
				width += charWidth;
				start++;
				buffer[0] = chars[start];
				charWidth =	stringWidth(run, buffer);
			}
			int firstStart = start;
			int firstIndice = i;			
			while (i >= lineStart) {
				chars = new char[run.length];
				text.getChars(run.start, run.start + run.length, chars, 0);
				while(start >= 0) {
					if (Compatibility.isSpaceChar(chars[start]) || Compatibility.isWhitespace(chars[start])) break;
					start--;
				}
				if (start >= 0 || i == lineStart) break;
				run = allRuns[--i];
				start = run.length - 1;
			}
			if (start == 0 && i != lineStart) {
				run = allRuns[--i];
			} else if (start <= 0 && i == lineStart) {
				i = firstIndice; 
				run = allRuns[i];
				start = Math.max(1, firstStart);
			}
			chars = new char[run.length];
			text.getChars(run.start, run.start + run.length, chars, 0);
			while (start < run.length) {
				if (!Compatibility.isWhitespace(chars[start])) break;
				start++;
			}
			if (0 < start && start < run.length) {
				StyleItem newRun = new StyleItem();
				newRun.start = run.start + start;
				newRun.length = run.length - start;
				newRun.style = run.style;
				run.length = start;
				place (run);
				place (newRun);
				StyleItem[] newAllRuns = new StyleItem[allRuns.length + 1];
				System.arraycopy(allRuns, 0, newAllRuns, 0, i + 1);
				System.arraycopy(allRuns, i + 1, newAllRuns, i + 2, allRuns.length - i - 1);
				allRuns = newAllRuns;
				allRuns[i + 1] = newRun;
			}
			if (i != allRuns.length - 2) {
				run.softBreak = run.lineBreak = true;
			}
		}
		lineWidth += run.width;
		if (run.lineBreak) {
			lineStart = i + 1;
			lineWidth = 0;
			lineCount++;
		}
	}
	lineWidth = 0;
	runs = new StyleItem[lineCount][];
	lineOffset = new int[lineCount + 1];
	lineY = new int[lineCount + 1];
	this.lineWidth = new int[lineCount];
	int lineRunCount = 0, line = 0;
	int lineHeight = defaultFontHeight + lineSpacing;
	StyleItem[] lineRuns = new StyleItem[allRuns.length];
	for (int i=0; i<allRuns.length; i++) {
		StyleItem run = allRuns[i];
		lineRuns[lineRunCount++] = run;
		lineWidth += run.width;
		if (run.style != null && run.style.font != null) {
			int runLogicalHeight = getFontHeigth(run.style.font) + lineSpacing;
			lineHeight = Math.max(lineHeight, runLogicalHeight);
		}
		if (run.lineBreak || i == allRuns.length - 1) {
			runs[line] = new StyleItem[lineRunCount];
			System.arraycopy(lineRuns, 0, runs[line], 0, lineRunCount);
			StyleItem lastRun = runs[line][lineRunCount - 1];
			this.lineWidth[line] = lineWidth;
			line++;
			lineY[line] = lineY[line - 1] + lineHeight;
			lineOffset[line] = lastRun.start + lastRun.length;
			lineRunCount = lineWidth = 0;
			lineHeight = defaultFontHeight + lineSpacing;
		}
	}
}

public void dispose () {
	if (device == null) return;
	freeRuns();
	font = null;
	text = null;
	tabs = null;
	styles = null;
	lineOffset = null;
	lineY = null;
	lineWidth = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public void draw (GC gc, int x, int y) {
	draw(gc, x, y, -1, -1, null, null);
}

public void draw(GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground) {
	checkLayout();
	computeRuns();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionForeground != null && selectionForeground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionBackground != null && selectionBackground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int length = text.length(); 
	if (length == 0) return;
	boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
	if (hasSelection) {
		selectionStart = Math.min(Math.max(0, selectionStart), length - 1);
		selectionEnd = Math.min(Math.max(0, selectionEnd), length - 1);		
		if (selectionForeground == null) selectionForeground = device.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
		if (selectionBackground == null) selectionBackground = device.getSystemColor(SWT.COLOR_LIST_SELECTION);
	}
	final Color foreground = gc.getForeground();
	final Color background = gc.getBackground();
	final Font gcFont = gc.getFont();
	Rectangle clip = gc.getClipping();
	for (int line=0; line<runs.length; line++) {
		int drawX = x, drawY = y + lineY[line];
		StyleItem[] lineRuns = runs[line];
		if (wrapWidth != -1) {
			switch (alignment) {
				case SWT.CENTER: drawX += (wrapWidth - lineWidth[line]) / 2; break;
				case SWT.RIGHT: drawX += wrapWidth - lineWidth[line]; break;
			}
		}
		if (drawX > clip.x + clip.width) continue;
		if (drawX + lineWidth[line] < clip.x) continue;
		int baseline = 0;
		for (int i = 0; i < lineRuns.length; i++) {
			baseline = Math.max(baseline, lineRuns[i].baseline);
		}
		int lineHeight = lineY[line+1] - lineY[line];
		for (int i = 0; i < lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			if (run.length == 0) continue;
			if (drawX > clip.x + clip.width) break;
			if (drawX + run.width >= clip.x) {
				if (!run.lineBreak || run.softBreak) {
					String string = text.substring(run.start, run.start + run.length);
					int drawRunY = drawY + (baseline - run.baseline);
					int end = run.start + run.length - 1;
					gc.setFont(getItemFont(run));
					boolean fullSelection = hasSelection && selectionStart <= run.start && selectionEnd >= end;
					if (fullSelection) {
						gc.setBackground(selectionBackground);
						gc.fillRectangle(drawX, drawY, run.width, lineHeight);
						if (!run.tab) {
							gc.setForeground(selectionForeground);
							gc.drawString(string, drawX, drawRunY, true);
						}
					} else {
						if (run.style != null && run.style.background != null) {
							Color bg = run.style.background;
							gc.setBackground(bg);
							gc.fillRectangle(drawX, drawRunY, run.width, run.height);
						}
						if (!run.tab) {
							Color fg = foreground;
							if (run.style != null && run.style.foreground != null) fg = run.style.foreground;
							gc.setForeground(fg);
							gc.drawString(string, drawX, drawRunY, true);
							boolean partialSelection = hasSelection && !(selectionStart > end || run.start > selectionEnd);
							if (partialSelection) {
								int selStart = Math.max(selectionStart, run.start);
								int selEnd = Math.min(selectionEnd, end);
								string = text.substring(run.start, selStart);
								int selX = drawX + gc.stringExtent(string).x;
								string = text.substring(selStart, selEnd + 1);
								int selWidth = gc.stringExtent(string).x;
								gc.setBackground(selectionBackground);
								gc.fillRectangle(selX, drawY, selWidth, lineHeight);
								if (fg != selectionForeground) {
									gc.setForeground(selectionForeground);
									gc.drawString(string, selX, drawRunY, true);
								}
							}
						}
					}
				}
			}
			drawX += run.width;
		}
	}
	gc.setForeground(foreground);
	gc.setBackground(background);
	gc.setFont(gcFont);
}

void freeRuns() {
	runs = null;
}

public int getAlignment () {
	checkLayout();
	return alignment;
}

public Rectangle getBounds () {
	checkLayout();
	computeRuns();
	int width = 0;
	if (wrapWidth != -1) {
		width = wrapWidth;
	} else {
		for (int line=0; line<runs.length; line++) {
			width = Math.max(width, lineWidth[line]);
		}
	}
	return new Rectangle (0, 0, width, lineY[lineY.length - 1]);
}


public Rectangle getBounds (int start, int end) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (length == 0) return new Rectangle(0, 0, 0, 0);
	if (start > end) return new Rectangle(0, 0, 0, 0);
	start = Math.min(Math.max(0, start), length - 1);
	end = Math.min(Math.max(0, end), length - 1);
	int startLine = getLineIndex(start);
	int endLine = getLineIndex(end);

	Rectangle rect = new Rectangle(0, 0, 0, 0);
	rect.y = lineY[startLine];
	rect.height = lineY[endLine + 1] - rect.y;
	if (startLine == endLine) {
		rect.x = getLocation(start, false).x;
		rect.width = getLocation(end, true).x - rect.x;
	} else {
		while (startLine <= endLine) {
			rect.width = Math.max(rect.width, lineWidth[startLine++]);
		}
	}
	return rect;
}

public Font getFont () {
	checkLayout();
	return font;
}

int getFontHeigth(Font font) {
	int fontList = font.handle;
	int [] buffer = new int [1];
	if (!OS.XmFontListInitFontContext (buffer, fontList)) {
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	int context = buffer [0];
	int height = 0;
	XFontStruct fontStruct = new XFontStruct ();
	int fontListEntry;
	int [] fontStructPtr = new int [1];
	int [] fontNamePtr = new int [1];
	while ((fontListEntry = OS.XmFontListNextEntry (context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont (fontListEntry, buffer);
		if (buffer [0] == 0) {
			OS.memmove (fontStruct, fontPtr, XFontStruct.sizeof);
			int fontHeight = fontStruct.max_bounds_ascent + fontStruct.max_bounds_descent;
			height = Math.max(height, fontHeight);
		} else {
			int nFonts = OS.XFontsOfFontSet (fontPtr, fontStructPtr, fontNamePtr);
			int [] fontStructs = new int [nFonts];
			OS.memmove (fontStructs, fontStructPtr [0], nFonts * 4);
			for (int i=0; i<nFonts; i++) {
				OS.memmove (fontStruct, fontStructs[i], XFontStruct.sizeof);
				int fontHeight = fontStruct.max_bounds_ascent + fontStruct.max_bounds_descent;
				height = Math.max(height, fontHeight);
			}
		}
	}
	OS.XmFontListFreeFontContext (context);
	return height;
}

public int getLevel (int offset) {
	checkLayout();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	return 0;
}

public Point getLineOffsets (int lineIndex) {
	checkLayout();
	computeRuns();
	if (!(0 <= lineIndex && lineIndex < runs.length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int start = lineOffset[lineIndex];
	int end = lineOffset[lineIndex + 1] - 1;
	return new Point (start, Math.max(start, end));
}

public Rectangle getLineBounds(int lineIndex) {
	checkLayout();
	computeRuns();
	if (!(0 <= lineIndex && lineIndex < runs.length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int x = 0, y = lineY[lineIndex];
	int width = lineWidth[lineIndex], height = lineY[lineIndex + 1] - y;
	if (wrapWidth != -1) {
		switch (alignment) {
			case SWT.CENTER: x = (wrapWidth - width) / 2; break;
			case SWT.RIGHT: x = wrapWidth - width; break;
		}
	}
	return new Rectangle (x, y, width, height);
}

public int getLineCount () {
	checkLayout();
	computeRuns();
	return runs.length;
}

public int getLineIndex (int offset) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	for (int line=0; line<runs.length; line++) {
		if (lineOffset[line + 1] > offset) {
			return line;
		}
	}
	return runs.length - 1;
}

public FontMetrics getLineMetrics (int lineIndex) {
	checkLayout();
	computeRuns();
	if (!(0 <= lineIndex && lineIndex < runs.length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	GC gc = new GC(device);
	Font font = this.font != null ? this.font : device.getSystemFont();
	FontMetrics metrics = null;
	if (text.length() == 0) {
		gc.setFont(font);
		metrics = gc.getFontMetrics();
	} else {
		int ascent = 0, descent = 0, leading = 0, aveCharWidth = 0, height = 0;
		StyleItem[] lineRuns = runs[lineIndex];
		for (int i = 0; i < lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			Font runFont = run.style != null ? run.style.font : null;
			if (runFont == null) runFont = font;
			gc.setFont(font);
			metrics = gc.getFontMetrics();
			ascent = Math.max (ascent, metrics.getAscent());
			descent = Math.max (descent, metrics.getDescent());
			height = Math.max (height, metrics.getHeight());
			leading = Math.max (leading, metrics.getLeading());
			aveCharWidth += metrics.getAverageCharWidth();
		}
		metrics = FontMetrics.motif_new(ascent, descent, aveCharWidth / lineRuns.length, leading, height);
	}
	gc.dispose();
	return metrics;
}

public Point getLocation (int offset, boolean trailing) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int line;
	for (line=0; line<runs.length; line++) {
		if (lineOffset[line + 1] > offset) break;
	}
	line = Math.min(line, runs.length - 1);
	StyleItem[] lineRuns = runs[line];
	Point result = null;
	if (offset == length) {
		result = new Point(lineWidth[line], lineY[line]);
	} else {
		int width = 0;
		for (int i=0; i<lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			int end = run.start + run.length;
			if (run.start <= offset && offset < end) {
				if (run.tab) {
					if (trailing || offset == length) width += run.width;
				} else {
					if (trailing) offset++;
					char[] chars = new char[offset - run.start];
					text.getChars(run.start, offset, chars, 0);
					width += stringWidth(run, chars);
				}
				result = new Point(width, lineY[line]);
				break;
			}
			width += run.width;
		}
	}
	if (result == null) result = new Point(0, 0);
	if (wrapWidth != -1) {
		switch (alignment) {
			case SWT.CENTER: result.x += (wrapWidth - lineWidth[line]) / 2; break;
			case SWT.RIGHT: result.x += wrapWidth - lineWidth[line]; break;
		}
	}
	return result;
}

Font getItemFont(StyleItem item) {
	if (item.style != null && item.style.font != null) {
		return item.style.font;
	}
	if (this.font != null) {
		return this.font;
	}
	return device.getSystemFont();
}

public int getNextOffset (int offset, int movement) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	if (offset == length) return length;
	if ((movement & (SWT.MOVEMENT_CHAR | SWT.MOVEMENT_CLUSTER)) != 0) return offset + 1;
	int lineEnd = 0;
	for (int i=1; i<lineOffset.length; i++) {
		if (lineOffset[i] > offset) {
			lineEnd = Math.max(lineOffset[i - 1], lineOffset[i] - 1);
			if (i == runs.length) lineEnd++;
			break;
		}
	}
	boolean previousSpaceChar = Compatibility.isSpaceChar(text.charAt(offset));
	offset++;
	while (offset < lineEnd) {
		boolean spaceChar = Compatibility.isSpaceChar(text.charAt(offset));
		if (!spaceChar && previousSpaceChar) break;
		previousSpaceChar = spaceChar;
		offset++;
	}
	return offset;
}

public int getOffset (Point point, int[] trailing) {
	checkLayout();
	if (point == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return getOffset (point.x, point.y, trailing);
}

public int getOffset (int x, int y, int[] trailing) {
	checkLayout();
	computeRuns();
	if (trailing != null && trailing.length < 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int line;
	int lineCount = runs.length;
	for (line=0; line<lineCount; line++) {
		if (lineY[line + 1] > y) break;
	}
	line = Math.min(line, runs.length - 1);
	if (wrapWidth != -1) {
		switch (alignment) {
			case SWT.CENTER: x -= (wrapWidth - lineWidth[line]) / 2; break;
			case SWT.RIGHT: x -= wrapWidth - lineWidth[line]; break;
		}
	}
	if (x < 0) return lineOffset[line];
	if (x > lineWidth[line]) return lineOffset[line + 1] - 1;	
	StyleItem[] lineRuns = runs[line];
	int width = 0;
	for (int i = 0; i < lineRuns.length; i++) {
		StyleItem run = lineRuns[i];
		if (run.lineBreak && !run.softBreak) return run.start;
		if (width + run.width > x) {
			if (run.tab) {
				if (trailing != null) {
					trailing[0] = x < (width + run.width / 2) ? 0 : 1; 
				}
				return run.start;
			}
			int offset = 0;
			char[] buffer = new char[1];
			char[] chars = new char[run.length];
			text.getChars(run.start, run.start + run.length, chars, 0);
			for (offset = 0; offset < chars.length; offset++) {
				buffer[0] = chars[offset];
				int charWidth = stringWidth(run, buffer);
				if (width + charWidth > x) {
					if (trailing != null) {
						trailing[0] = x < (width + charWidth / 2) ? 0 : 1;
					}
					break;
				}
				width += charWidth;
			}
			return run.start + offset; 
		}
		width += run.width;
	}
	if (trailing != null) trailing[0] = 0;
	return lineOffset[line + 1];
}

public int getOrientation () {
	checkLayout();
	return orientation;
}

public int getPreviousOffset (int offset, int movement) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	if (offset == 0) return 0;
	if ((movement & (SWT.MOVEMENT_CHAR | SWT.MOVEMENT_CLUSTER)) != 0) return offset - 1;
	int lineStart = 0;
	for (int i=0; i<lineOffset.length-1; i++) {
		int lineEnd = lineOffset[i+1];
		if (i == runs.length - 1) lineEnd++;
		if (lineEnd > offset) {
			lineStart = lineOffset[i];
			break;
		}
	}	
	offset--;
	boolean previousSpaceChar = Compatibility.isSpaceChar(text.charAt(offset));
	while (lineStart < offset) {
		boolean spaceChar = Compatibility.isSpaceChar(text.charAt(offset - 1));
		if (spaceChar && !previousSpaceChar) break;
		offset--;
		previousSpaceChar = spaceChar;
	}
	return offset;
}

public int[] getSegments() {
	checkLayout();
	return segments;
}

public int getSpacing () {
	checkLayout();	
	return lineSpacing;
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

public int[] getTabs () {
	checkLayout();
	return tabs;
}

public String getText () {
	checkLayout();
	return text;
}

public int getWidth () {
	checkLayout();
	return wrapWidth;
}

public boolean isDisposed () {
	return device == null;
}

/*
 *  Itemize the receiver text, create run for 
 */
StyleItem[] itemize () {
	int length = text.length();
	if (length == 0) {
		return new StyleItem[]{new StyleItem(), new StyleItem()};
	}
	int runCount = 0, start = 0;
	StyleItem[] runs = new StyleItem[length];
	char[] chars = text.toCharArray();
	for (int i = 0; i<length; i++) {
		char ch = chars[i];
		if (ch == '\t' || ch == '\r' || ch == '\n') {
			if (i != start) {
				StyleItem item = new StyleItem();
				item.start = start;
				runs[runCount++] = item;
			}
			StyleItem item = new StyleItem();
			item.start = i;
			runs[runCount++] = item;
			start = i + 1;
		}
	}
	char lastChar = chars[length - 1];
	if (!(lastChar == '\t' || lastChar == '\r' || lastChar == '\n')) {
		StyleItem item = new StyleItem();
		item.start = start;
		runs[runCount++] = item;
	}
	if (runCount != length) {
		StyleItem[] newRuns = new StyleItem[runCount];
		System.arraycopy(runs, 0, newRuns, 0, runCount);
		runs = newRuns;
	}
	runs = merge(runs, runCount);
	return runs;
}

/* 
 *  Merge styles ranges and script items 
 */
StyleItem[] merge (StyleItem[] items, int itemCount) {
	int length = text.length();
	int count = 0, start = 0, end = length, itemIndex = 0, styleIndex = 0;
	StyleItem[] runs = new StyleItem[itemCount + styles.length];
	while (start < end) {
		StyleItem item = new StyleItem();
		item.start = start;
		item.style = styles[styleIndex].style;
		runs[count++] = item;
		int itemLimit = itemIndex + 1 < items.length ? items[itemIndex + 1].start : length;
		int styleLimit = styleIndex + 1 < styles.length ? styles[styleIndex + 1].start : length;
		if (styleLimit <= itemLimit) {
			styleIndex++;
			start = styleLimit;
		}
		if (itemLimit <= styleLimit) {
			itemIndex++;
			start = itemLimit;
		}
		item.length = start - item.start;
	}
	StyleItem item = new StyleItem();
	item.start = end;
	runs[count++] = item;
	if (runs.length != count) {
		StyleItem[] result = new StyleItem[count];
		System.arraycopy(runs, 0, result, 0, count);
		return result;
	}
	return runs;
}

void place (StyleItem run) {
	if (run.length == 0) return;
	char[] chars = new char[run.length];
	text.getChars(run.start, run.start + run.length, chars, 0);
	int fontList = getItemFont(run).handle;
	byte[] buffer = Converter.wcsToMbcs(null, chars, true);
	short[] width = new short[1], height = new short[1];
	int xmString = OS.XmStringCreateLocalized(buffer);
	OS.XmStringExtent(fontList, xmString, width, height);
	run.width = width[0] & 0xFFFF;
	run.height = height[0] & 0xFFFF;
	run.baseline = OS.XmStringBaseline(fontList, xmString);
	OS.XmStringFree(xmString);
}

public void setAlignment (int alignment) {
	checkLayout();
	int mask = SWT.LEFT | SWT.CENTER | SWT.RIGHT;
	alignment &= mask;
	if (alignment == 0) return;
	if ((alignment & SWT.LEFT) != 0) alignment = SWT.LEFT;
	if ((alignment & SWT.RIGHT) != 0) alignment = SWT.RIGHT;
	this.alignment = alignment;
}

public void setFont (Font font) {
	checkLayout ();
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (this.font == font) return;
	if (font != null && font.equals(this.font)) return;
	freeRuns();
	this.font = font;
	defaultFontHeight = getFontHeigth(font != null ? font : device.getSystemFont());
}

public void setOrientation (int orientation) {
	checkLayout();
	int mask = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
	orientation &= mask;
	if (orientation == 0) return;
	if ((orientation & SWT.LEFT_TO_RIGHT) != 0) orientation = SWT.LEFT_TO_RIGHT;
	this.orientation = orientation;
}

public void setSpacing (int spacing) {
	checkLayout();
	if (spacing < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.lineSpacing == spacing) return;
	freeRuns();
	this.lineSpacing = spacing;
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

public void setWidth (int width) {
	checkLayout();
	if (width < -1 || width == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.wrapWidth == width) return;
	freeRuns();
	this.wrapWidth = width;
}
}

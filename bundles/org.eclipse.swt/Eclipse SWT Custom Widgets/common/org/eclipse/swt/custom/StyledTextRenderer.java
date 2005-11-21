/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * A StyledTextRenderer renders the content of a StyledText widget.
 * This class can be used to render to the display or to a printer.
 */
class StyledTextRenderer {
	Device device;
	StyledText styledText;
	StyledTextContent content;

	/* Font info */
	Font regularFont, boldFont, italicFont, boldItalicFont;
	int tabWidth;
	int ascent, descent;
	int lineEndSpaceWidth;
	int averageCharWidth;
	
	/* Line data */
	int topIndex = -1;
	TextLayout[] layouts;
	int lineCount;
	int[] lineWidth;
	int[] lineHeight;
	LineInfo[] lines;
	int maxWidth;
	int maxWidthLineIndex;
	boolean idleRunning;
	
	/* Style data */
	int[] ranges;
	int styleCount;	
	StyleRange[] styles;
	
	final static int GROW = 32;
	final static int IDLE_TIME = 50;
	final static int CACHE_SIZE = 128;
	
	final static int BACKGROUND = 1 << 0;
	final static int ALIGNMENT = 1 << 1;
	final static int INDENT = 1 << 2;
	final static int JUSTIFY = 1 << 3;
	final static int BULLET = 1 << 4;
	final static int SEGMENTS = 1 << 5;
	
	class LineInfo {
		int flags;
		Color background;
		int alignment;
		int indent;
		boolean justify;
		Bullet bullet;
		int[] segments;
		
		public LineInfo() {			
		}
		public LineInfo(LineInfo info) {
			flags = info.flags;
			background = info.background; 
			alignment = info.alignment;
			indent = info.indent;
			justify = info.justify;
			bullet = info.bullet;
			segments = info.segments;
		}
	}
	
	
StyledTextRenderer(Device device, StyledText styledText) {
	this.device = device;
	this.styledText = styledText;
}
void calculate(int startLine, int lineCount) {
	int endLine = startLine + lineCount;
	if (startLine < 0 || endLine > lineWidth.length) {
		return;
	}
	int hTrim = styledText.leftMargin + styledText.rightMargin + styledText.getCaretWidth();
	for (int i = startLine; i < endLine; i++) {
		if (lineWidth[i] == -1 || lineHeight[i] == -1) {
			TextLayout layout = getTextLayout(i);
			Rectangle rect = layout.getBounds();
			lineWidth[i] = rect.width + hTrim;
			lineHeight[i] = rect.height;
			disposeTextLayout(layout);
		}
		if (lineWidth[i] > maxWidth) {
			maxWidth = lineWidth[i];
			maxWidthLineIndex = i;
		}
	}
}
void calculateClientArea () {
	int index = styledText.getTopIndex();
	int lineCount = content.getLineCount();
	int height = styledText.getClientArea().height;
	int y = 0;
	while (height > y && lineCount > index) {
		calculate(index, 1);
		y += lineHeight[index++];
	}
}
void calculateIdle () {
	if (idleRunning) return;
	Runnable runnable = new Runnable() {
		public void run() {
			if (styledText == null) return;
			int i;
			long start = System.currentTimeMillis();
			for (i = 0; i < lineCount; i++) {
				if (lineHeight[i] == -1 || lineWidth[i] == -1) {
					calculate(i, 1);
					if (System.currentTimeMillis() - start > IDLE_TIME) break;
				}
			}
			if (i < lineCount) {
				Display display = styledText.getDisplay();				
				display.asyncExec(this);
			} else {
				idleRunning = false;
				styledText.setScrollBars(true);
				ScrollBar bar = styledText.getVerticalBar();
				if (bar != null) {
					bar.setSelection(styledText.getVerticalScrollOffset());
				}					
			}
		}
	};		
	Display display = styledText.getDisplay();
	display.asyncExec(runnable);
	idleRunning = true;
}
void clearLineBackground(int startLine, int count) {
	if (lines == null) return;
	for (int i = startLine; i < startLine + count; i++) {
		LineInfo info = lines[i];
		if (info != null) {
			info.flags &= ~BACKGROUND;
			info.background = null;
			if (info.flags == 0) lines[i] = null;
		}
	}
}
void clearLineStyle(int startLine, int count) {
	if (lines == null) return;
	for (int i = startLine; i < startLine + count; i++) {
		LineInfo info = lines[i];
		if (info != null) {
			info.flags &= ~(ALIGNMENT | INDENT | BULLET | JUSTIFY);
			info.bullet = null;
			if (info.flags == 0) lines[i] = null;
		}
	}
}
void copyInto(StyledTextRenderer renderer) {
	if (ranges != null) {
		int[] newRanges = renderer.ranges = new int[styleCount << 1];
		System.arraycopy(ranges, 0, newRanges, 0, newRanges.length);
	}
	if (styles != null) {
		StyleRange[] newStyles = renderer.styles = new StyleRange[styleCount];
		for (int i = 0; i < newStyles.length; i++) {
			newStyles[i] = (StyleRange)styles[i].clone();
		}
		renderer.styleCount = styleCount;
	}
	if (lines != null) {
		LineInfo[] newLines = renderer.lines = new LineInfo[lineCount];
		for (int i = 0; i < newLines.length; i++) {
			newLines[i] = new LineInfo(lines[i]);				
		}
		renderer.lineCount = lineCount;
	}
}
void dispose() {
	if (boldFont != null) boldFont.dispose();
	if (italicFont != null) italicFont.dispose();
	if (boldItalicFont != null) boldItalicFont.dispose();
	boldFont = italicFont = boldItalicFont = null;
	reset();
	content = null;
	device = null;
	styledText = null;
}
void disposeTextLayout (TextLayout layout) {
	if (layouts != null) {
		for (int i = 0; i < layouts.length; i++) {
			if (layouts[i] == layout) return;
		}
	}
	layout.dispose();
}
int drawLine(int lineIndex, int paintX, int paintY, GC gc, Color widgetBackground, Color widgetForeground) {
	TextLayout layout = getTextLayout(lineIndex);
	String line = content.getLine(lineIndex);
	int lineOffset = content.getOffsetAtLine(lineIndex);
	int lineLength = line.length();
	Point selection = styledText.getSelection();
	int selectionStart = selection.x;
	int selectionEnd = selection.y;
	Rectangle client = styledText.getClientArea();  
	Color lineBackground = getLineBackground(lineIndex, widgetBackground);
	StyledTextEvent event = styledText.getLineBackgroundData(lineOffset, line);
	if (event != null && event.lineBackground != null) lineBackground = event.lineBackground;
	
	boolean fullSelection = (styledText.getStyle() & SWT.FULL_SELECTION) != 0;
	if (!fullSelection || selectionStart > lineOffset || selectionEnd <= lineOffset + lineLength) {
		// draw background if full selection is off or if line is not completely selected
		gc.setBackground(lineBackground);
		gc.fillRectangle(client.x, paintY, client.width, layout.getBounds().height);
	}
	if (selectionStart != selectionEnd) {
		int y = paintY;
		gc.setBackground(styledText.getSelectionBackground());
		int lineCount = layout.getLineCount();
		if (fullSelection) {
			int[] offsets = layout.getLineOffsets();
			for (int i = 0; i < lineCount - 1; i++) {
				int lineStart = offsets[i];
				if (lineStart >= selectionEnd - lineOffset) break;
				int lineEnd = offsets[i + 1];
				Rectangle lineBounds = layout.getLineBounds(i);
				if (selectionStart - lineOffset <= lineEnd && lineEnd <= selectionEnd - lineOffset) {
					int x = paintX + lineBounds.x + lineBounds.width;
					gc.fillRectangle(x, y, client.width - x, lineBounds.height);
				}
				y += lineBounds.height + layout.getSpacing();
			}
		}
		if (selectionStart - lineOffset <= lineLength && lineLength < selectionEnd - lineOffset) {
			Rectangle lineBounds = layout.getLineBounds(lineCount - 1);
			int x = paintX + lineBounds.x + lineBounds.width;
			if (fullSelection) {
				gc.fillRectangle(x, paintY + lineBounds.y, client.width - styledText.rightMargin - x, lineBounds.height);
			} else {
				gc.fillRectangle(x, paintY + lineBounds.y, lineEndSpaceWidth, lineBounds.height);
			}
		}
	}
	gc.setForeground(widgetForeground);
	gc.setBackground(lineBackground);	
	if (selectionStart == selectionEnd || (selectionEnd <= lineOffset && selectionStart > lineOffset + lineLength - 1)) {
		layout.draw(gc, paintX, paintY);
	} else {
		int start = Math.max(0, selectionStart - lineOffset);
		int end = Math.min(lineLength, selectionEnd - lineOffset);
		Color selectionFg = styledText.getSelectionForeground();
		Color selectionBg = styledText.getSelectionBackground();
		layout.draw(gc, paintX, paintY, start, end - 1, selectionFg, selectionBg);
	}

	// draw objects
//	Bullet bullet = null;
//	if (lines != null) {
//		LineInfo info = lines[lineIndex];
//		if (info != null) {
//			if ((info.flags & BULLET) != 0) bullet = info.bullet;
//		}
//	}
	TextStyle[] styles = layout.getStyles();
	int[] ranges = null;
	for (int i = 0; i < styles.length; i++) {
		if (styles[i].metrics != null) {
			if (ranges == null) ranges = layout.getRanges();
			int start = ranges[i << 1];
			int length = ranges[(i << 1) + 1] - start;
			Point point = layout.getLocation(start, false);
			FontMetrics metrics = layout.getLineMetrics(layout.getLineIndex(start));
			StyleRange style = (StyleRange)((StyleRange)styles[i]).clone();
			style.start = start + lineOffset;
			style.length = length;
			styledText.paintObject(gc, point.x + paintX, point.y + paintY, metrics.getAscent(), metrics.getDescent(), style);
		}
	}
	int height = layout.getBounds().height;
	disposeTextLayout(layout);
	return height;
}
int getBaseline() {
	return ascent;
}
Font getFont(int style) {
	switch (style) {
		case SWT.BOLD:
			if (boldFont != null) return boldFont;
			return boldFont = new Font(device, getFontData(style));
		case SWT.ITALIC:
			if (italicFont != null) return italicFont;
			return italicFont = new Font(device, getFontData(style));
		case SWT.BOLD | SWT.ITALIC:
			if (boldItalicFont != null) return boldItalicFont;
			return boldItalicFont = new Font(device, getFontData(style));
		default:
			return regularFont;
	}
}
FontData[] getFontData(int style) {
	FontData[] fontDatas = regularFont.getFontData();
	for (int i = 0; i < fontDatas.length; i++) {
		fontDatas[i].setStyle(style);
	}
	return fontDatas;
}
int getHeight () {
	int defaultLineHeight = getLineHeight();
	if (styledText.isFixedLineHeight()) {
		return lineCount * defaultLineHeight;
	}
	int totalHeight = 0;
	int width = styledText.getWrapWidth();
	for (int i = 0; i < lineCount; i++) {
		int height = lineHeight[i];
		if (height == -1) {
			if (width > 0) {
				int length = content.getLine(i).length();
				height = ((length * averageCharWidth / width) + 1) * defaultLineHeight;
			} else {
				height = defaultLineHeight;
			}
		}
		totalHeight += height;
	}
	return totalHeight + styledText.topMargin + styledText.bottomMargin;
}
int getLineAlignment(int index, int defaultAlignment) {
	if (lines == null) return defaultAlignment;
	LineInfo info = lines[index];
	if (info != null && (info.flags & ALIGNMENT) != 0) {
		return info.alignment;
	}
	return defaultAlignment;
}
Color getLineBackground(int index, Color defaultBackground) {
	if (lines == null) return defaultBackground;
	LineInfo info = lines[index];
	if (info != null && (info.flags & BACKGROUND) != 0) {
		return info.background;
	}
	return defaultBackground;
}
Bullet getLineBullet(int index, Bullet defaultBullet) {
	if (lines == null) return defaultBullet;
	LineInfo info = lines[index];
	if (info != null && (info.flags & BULLET) != 0) {
		return info.bullet;
	}
	return defaultBullet;
}
int getLineEndSpace() {
	return lineEndSpaceWidth;
}
int getLineHeight() {
	return ascent + descent;
}
int getLineHeight(int lineIndex) {
	if (lineHeight[lineIndex] == -1) {
		calculate(lineIndex, 1);
	}
	return lineHeight[lineIndex];
}
int getLineIndent(int index, int defaultIndent) {
	if (lines == null) return defaultIndent;
	LineInfo info = lines[index];
	if (info != null && (info.flags & INDENT) != 0) {
		return info.indent;
	}
	return defaultIndent;
}
boolean getLineJustify(int index, boolean defaultJustify) {
	if (lines == null) return defaultJustify;
	LineInfo info = lines[index];
	if (info != null && (info.flags & JUSTIFY) != 0) {
		return info.justify;
	}
	return defaultJustify;
}
int[] getLineSegments(int index, int[] defaultSegments) {
	if (lines == null) return defaultSegments;
	LineInfo info = lines[index];
	if (info != null && (info.flags & SEGMENTS) != 0) {
		return info.segments;
	}
	return defaultSegments;
}
int getRangeIndex(int offset, int low, int high) {
	if (styleCount == 0) return 0;
	if (ranges != null)  {
		while (high - low > 2) {
			int index = ((high + low) / 2) / 2 * 2;
			int end = ranges[index] + ranges[index + 1];
			if (end > offset) {
				high = index;
			} else {
				low = index;
			}
		}
	} else {
		while (high - low > 1) {
			int index = ((high + low) / 2);
			int end = styles[index].start + styles[index].length;
			if (end > offset) {
				high = index;
			} else {
				low = index;
			}
		}
	}
	return high;
}
int[] getRanges(int start, int length) {
	int[] newRanges;
	int end = start + length - 1;
	if (ranges != null) {
		int rangeCount = styleCount << 1;
		int rangeStart = getRangeIndex(start, -1, rangeCount);
		if (rangeStart >= rangeCount) return null;
		if (ranges[rangeStart] > end) return null;
		int rangeEnd = Math.min(rangeCount - 2, getRangeIndex(end, rangeStart - 1, rangeCount) + 1);
		newRanges = new int[rangeEnd - rangeStart + 2];
		System.arraycopy(ranges, rangeStart, newRanges, 0, newRanges.length);
	} else {
		int rangeStart = getRangeIndex(start, -1, styleCount);
		if (rangeStart >= styleCount) return null;
		if (styles[rangeStart].start > end) return null;
		int rangeEnd = Math.min(styleCount - 1, getRangeIndex(end, rangeStart - 1, styleCount));
		newRanges = new int[(rangeEnd - rangeStart + 1) << 1];
		for (int i = rangeStart, j = 0; i <= rangeEnd; i++, j += 2) {
			StyleRange style = styles[i];
			newRanges[j] = style.start;
			newRanges[j + 1] = style.length;
		}		
	}
	if (start > newRanges[0]) {
		newRanges[1] = newRanges[0] + newRanges[1] - start;
		newRanges[0] = start;
	}
	if (end < newRanges[newRanges.length - 2] + newRanges[newRanges.length - 1] - 1) {
		newRanges[newRanges.length - 1] = end - newRanges[newRanges.length - 2];
	}
	return newRanges;
}
StyleRange[] getStyleRanges(int start, int length, boolean includeRanges) {
	StyleRange[] newStyles;
	int end = start + length - 1;
	if (ranges != null) {
		int rangeCount = styleCount << 1;
		int rangeStart = getRangeIndex(start, -1, rangeCount);
		if (rangeStart >= rangeCount) return null;
		if (ranges[rangeStart] > end) return null;
		int rangeEnd = Math.min(rangeCount - 2, getRangeIndex(end, rangeStart - 1, rangeCount) + 1);
		newStyles = new StyleRange[((rangeEnd - rangeStart) >> 1) + 1];
		if (includeRanges) {
			for (int i = rangeStart, j = 0; i <= rangeEnd; i += 2, j++) {
				newStyles[j] = (StyleRange)styles[i >> 1].clone();
				newStyles[j].start = ranges[i];
				newStyles[j].length = ranges[i + 1];
			}
		} else {
			System.arraycopy(styles, rangeStart >> 1, newStyles, 0, newStyles.length);
		}
	} else {
		int rangeStart = getRangeIndex(start, -1, styleCount);
		if (rangeStart >= styleCount) return null;
		if (styles[rangeStart].start > end) return null;
		int rangeEnd = Math.min(styleCount - 1, getRangeIndex(end, rangeStart - 1, styleCount));
		newStyles = new StyleRange[rangeEnd - rangeStart + 1];
		System.arraycopy(styles, rangeStart, newStyles, 0, newStyles.length);
	}
	StyleRange style = newStyles[0];
	if (start > style.start) {
		if (!includeRanges || ranges == null) newStyles[0] = style = (StyleRange)style.clone();
		style.length = style.start + style.length - start;
		style.start = start;
	}
	style = newStyles[newStyles.length - 1];
	if (end < style.start + style.length - 1) {
		if (!includeRanges || ranges == null) newStyles[newStyles.length - 1] = style = (StyleRange)style.clone();
		style.length = end - style.start + 1;
	}
	return newStyles;
}
StyleRange getStyleRange(StyleRange style) {
	if (style.start == 0 && style.length == 0 && style.fontStyle == SWT.NORMAL) return style;
	StyleRange clone = (StyleRange)style.clone();
	clone.start = clone.length = 0;
	clone.fontStyle = SWT.NORMAL;
	if (clone.font == null) clone.font = getFont(style.fontStyle);
	return clone;
}
TextLayout getTextLayout(int lineIndex) {
	return getTextLayout(lineIndex, styledText.getOrientation(), styledText.getWrapWidth(), styledText.lineSpacing);
}
TextLayout getTextLayout(int lineIndex, int orientation, int width, int lineSpacing) {
	TextLayout layout = null;
	if (styledText != null) {
		int topIndex = styledText.topIndex > 0 ? styledText.topIndex - 1 : 0;	
		if (layouts == null || topIndex != this.topIndex) {
			TextLayout[] newLayouts = new TextLayout[CACHE_SIZE];
			if (layouts != null) {
				for(int i = 0; i < layouts.length; i++) {
					if (layouts[i] != null) {
						int layoutIndex = (i + this.topIndex) - topIndex;
						if (0 <= layoutIndex && layoutIndex < newLayouts.length) {
							newLayouts[layoutIndex] = layouts[i];
						} else {
							layouts[i].dispose();
						}
					}
				}
			}
			this.topIndex = topIndex;
			layouts = newLayouts;
		}
		if (layouts != null) {
			int layoutIndex = lineIndex - topIndex;
			if (0 <= layoutIndex && layoutIndex < layouts.length) {
				layout = layouts[layoutIndex];
				if (layout != null) {
					if (lineWidth[lineIndex] != -1) return layout;
				} else {
					layout = layouts[layoutIndex] = new TextLayout(device);
				}
			}
		}
	}
	if (layout == null) layout = new TextLayout(device);
	String line = content.getLine(lineIndex);
	int lineOffset = content.getOffsetAtLine(lineIndex);
	int[] segments = null;
	int indent = 0;
	int alignment = SWT.LEFT;
	boolean justify = false;
	Bullet bullet = null;
	int[] ranges = null;
	StyleRange[] styles = null;
	int rangeStart = 0, styleCount = 0;
	StyledTextEvent event = null;
	if (styledText != null) {
		event = styledText.getLineStyleData(lineOffset, line);
		segments = styledText.getBidiSegments(lineOffset, line);
		indent = styledText.indent;
		alignment = styledText.alignment;
		justify = styledText.justify;
	}
	if (event != null) {
		indent = event.indent;
		alignment = event.alignment;
		justify = event.justify;
		bullet = event.bullet;
		ranges = event.ranges;
		styles = event.styles;
		if (styles != null) {
			styleCount = styles.length;
			if (styledText.isFixedLineHeight()) {
				for (int i = 0; i < styleCount; i++) {
					if (styles[i].isVariableHeight()) {
						styledText.verticalScrollOffset = -1;
						styledText.setVariableLineHeight();
						styledText.redraw();
						break;
					}
				}
			}
		}
	} else {
		if (lines != null) {
			LineInfo info = lines[lineIndex];
			if (info != null) {
				if ((info.flags & INDENT) != 0) indent = info.indent;
				if ((info.flags & ALIGNMENT) != 0) alignment = info.alignment;
				if ((info.flags & JUSTIFY) != 0) justify = info.justify;
				if ((info.flags & BULLET) != 0) bullet = info.bullet;
				if ((info.flags & SEGMENTS) != 0) segments = info.segments;
			}
		}
		ranges = this.ranges;
		styles = this.styles;
		styleCount = this.styleCount;
		if (ranges != null) {
			rangeStart = getRangeIndex(lineOffset, -1, styleCount << 1);
		} else {
			rangeStart = getRangeIndex(lineOffset, -1, styleCount);
		}
	}
	if (bullet != null) indent += bullet.metrics.width;
	
	layout.setFont(regularFont);
	layout.setAscent(ascent);
	layout.setDescent(descent);
	layout.setText(line);
	layout.setOrientation(orientation);
	layout.setSegments(segments);
	layout.setWidth(width);
	layout.setSpacing(lineSpacing);
	layout.setTabs(new int[]{tabWidth});	
	layout.setIndent(indent);
	layout.setAlignment(alignment);
	layout.setJustify(justify);
	
	int lastOffset = 0;
	int length = line.length();
	if (styles != null) {
		if (ranges != null) {
			int rangeCount = styleCount << 1;
			for (int i = rangeStart; i < rangeCount; i += 2) {
				int start, end;
				if (lineOffset > ranges[i]) {
					start = 0;
					end = Math.min (length, ranges[i + 1] - lineOffset + ranges[i]);
				} else {
					start = ranges[i] - lineOffset;
					end = Math.min(length, start + ranges[i + 1]);
				}
				if (start >= length) break;
				if (lastOffset < start) {
					layout.setStyle(null, lastOffset, start - 1);
				}
				layout.setStyle(getStyleRange(styles[i >> 1]), start, end);
				lastOffset = Math.max(lastOffset, end);
			}
		} else {
			for (int i = rangeStart; i < styleCount; i++) {
				int start, end;
				if (lineOffset > styles[i].start) {
					start = 0;
					end = Math.min (length, styles[i].length - lineOffset + styles[i].start);
				} else {
					start = styles[i].start - lineOffset;
					end = Math.min(length, start + styles[i].length);
				}
				if (start >= length) break;
				if (lastOffset < start) {
					layout.setStyle(null, lastOffset, start - 1);
				}
				layout.setStyle(getStyleRange(styles[i]), start, end);
				lastOffset = Math.max(lastOffset, end);
			}
		}
	}
	if (lastOffset < length) layout.setStyle(null, lastOffset, length);
	return layout;
}
int getWidth() {
	return maxWidth;
}
void reset() {
	if (layouts != null) {
		for (int i = 0; i < layouts.length; i++) {
			TextLayout layout = layouts[i];
			if (layout != null) layout.dispose();
		}
		layouts = null;
	}
	topIndex = -1;
	styleCount = lineCount = 0;
	ranges = null;
	styles = null;
	lines = null;
	lineWidth = null;
	lineHeight = null;
}
void reset(int startLine, int lineCount) {
	int endLine = startLine + lineCount;
	if (startLine < 0 || endLine > lineWidth.length) return;
	for (int i = startLine; i < endLine; i++) {
		lineWidth[i] = -1;
		lineHeight[i] = -1;
	}
	if (startLine <= maxWidthLineIndex && maxWidthLineIndex < endLine) {
		maxWidth = 0;
		maxWidthLineIndex = -1;
		if (lineCount != this.lineCount) {
			for (int i = 0; i < this.lineCount; i++) {
				if (lineWidth[i] > maxWidth) {
					maxWidth = lineWidth[i];
					maxWidthLineIndex = i;
				}
			}
		}
	}
}
void setContent(StyledTextContent content) {
	reset();
	this.content = content;
	lineCount = content.getLineCount();
	lineWidth = new int[lineCount];
	lineHeight = new int[lineCount];
	reset(0, lineCount);
}
void setFont(Font font, int tabs) {
	TextLayout layout = new TextLayout(device);
	layout.setFont(regularFont);
	if (font != regularFont) {
		if (boldFont != null) boldFont.dispose();
		if (italicFont != null) italicFont.dispose();
		if (boldItalicFont != null) boldItalicFont.dispose();
		boldFont = italicFont = boldItalicFont = null;
		regularFont = font;
		layout.setText("    ");
		layout.setFont(font);
		layout.setStyle(new TextStyle(getFont(SWT.NORMAL), null, null), 0, 0);
		layout.setStyle(new TextStyle(getFont(SWT.BOLD), null, null), 1, 1);
		layout.setStyle(new TextStyle(getFont(SWT.ITALIC), null, null), 2, 2);
		layout.setStyle(new TextStyle(getFont(SWT.BOLD | SWT.ITALIC), null, null), 3, 3);
		FontMetrics metrics = layout.getLineMetrics(0);
		ascent = metrics.getAscent() + metrics.getLeading();
		descent = metrics.getDescent();
		lineEndSpaceWidth = layout.getBounds(0, 0).width;
		boldFont.dispose();
		italicFont.dispose();
		boldItalicFont.dispose();
		boldFont = italicFont = boldItalicFont = null;
	}
	StringBuffer tabBuffer = new StringBuffer(tabs);
	for (int i = 0; i < tabs; i++) {
		tabBuffer.append(' ');
	}
	layout.setText(tabBuffer.toString());
	tabWidth = layout.getBounds().width;
	layout.dispose();
}
void setLineAlignment(int startLine, int count, int alignment) {
	if (lines == null) lines = new LineInfo[lineCount];
	for (int i = startLine; i < startLine + count; i++) {
		if (lines[i] == null) {
			lines[i] = new LineInfo();
		}
		lines[i].flags |= ALIGNMENT;
		lines[i].alignment = alignment;
	}
}
void setLineBackground(int startLine, int count, Color background) {
	if (lines == null) lines = new LineInfo[lineCount];
	for (int i = startLine; i < startLine + count; i++) {
		if (lines[i] == null) {
			lines[i] = new LineInfo();
		}
		lines[i].flags |= BACKGROUND;
		lines[i].background = background;
	}
}
void setLineBullet(int startLine, int count, Bullet bullet) {
	if (lines == null) lines = new LineInfo[lineCount];
	for (int i = startLine; i < startLine + count; i++) {
		if (lines[i] == null) {
			lines[i] = new LineInfo();
		}
		lines[i].flags |= BULLET;
		lines[i].bullet = bullet;
	}
}
void setLineIndent(int startLine, int count, int indent) {
	if (lines == null) lines = new LineInfo[lineCount];
	for (int i = startLine; i < startLine + count; i++) {
		if (lines[i] == null) {
			lines[i] = new LineInfo();
		}
		lines[i].flags |= INDENT;
		lines[i].indent = indent;
	}
}
void setLineJustify(int startLine, int count, boolean justify) {
	if (lines == null) lines = new LineInfo[lineCount];
	for (int i = startLine; i < startLine + count; i++) {
		if (lines[i] == null) {
			lines[i] = new LineInfo();
		}
		lines[i].flags |= JUSTIFY;
		lines[i].justify = justify;
	}
}
void setLineSegments(int startLine, int count, int[] segments) {
	if (lines == null) lines = new LineInfo[lineCount];
	for (int i = startLine; i < startLine + count; i++) {
		if (lines[i] == null) {
			lines[i] = new LineInfo();
		}
		lines[i].flags |= SEGMENTS;
		lines[i].segments = segments;
	}
}
void setStyleRanges (int[] newRanges, StyleRange[] newStyles) {
	if (newStyles == null) {
		styleCount = 0;
		ranges = null;
		styles = null;
		return;
	}	
	if (styleCount == 0) {
		if (newRanges != null) {
			ranges = new int[newRanges.length];
			System.arraycopy(newRanges, 0, ranges, 0, ranges.length);
		}
		styles = new StyleRange[newStyles.length];
		System.arraycopy(newStyles, 0, styles, 0, styles.length);
		styleCount = newStyles.length;
		return;
	}
	if (newRanges != null && ranges == null) {
		ranges = new int[styles.length << 1];
		for (int i = 0, j = 0; i < styleCount; i++) {
			ranges[j++] = styles[i].start;
			ranges[j++] = styles[i].length;
		}
	}
	if (newRanges == null && ranges != null) {
		newRanges = new int[newStyles.length << 1];
		for (int i = 0, j = 0; i < newStyles.length; i++) {
			newRanges[j++] = newStyles[i].start;
			newRanges[j++] = newStyles[i].length;
		}
	}
	if (ranges != null) {
		int start = newRanges[0];
		int rangeCount = styleCount << 1;
		int modifyStart = getRangeIndex(start, -1, rangeCount);
		if (modifyStart == rangeCount) {
			if (newStyles.length + styleCount >= styles.length) {
				int[] tmpRanges = new int[ranges.length + newRanges.length];
				System.arraycopy(ranges, 0, tmpRanges, 0, ranges.length);
				ranges = tmpRanges;
				StyleRange[] tmpStyles = new StyleRange[styles.length + newStyles.length];
				System.arraycopy(styles, 0, tmpStyles, 0, styles.length);
				styles = tmpStyles;
			}
			System.arraycopy(newRanges, 0, ranges, rangeCount, newRanges.length);
			System.arraycopy(newStyles, 0, styles, styleCount, newStyles.length);
			styleCount += newStyles.length;
			return;
		}
		int modifyEnd = modifyStart;
		int[] mergeRanges = new int[6];
		StyleRange[] mergeStyles = new StyleRange[3];
		for (int i = 0; i < newRanges.length; i += 2) {
			int newStart = newRanges[i];
			int newEnd = newStart + newRanges[i + 1];
			if (newStart == newEnd) continue;
			int modifyLast = 0, mergeCount = 0;
			while (modifyEnd < rangeCount) {
				if (newStart >= ranges[modifyStart] + ranges[modifyStart + 1]) modifyStart += 2;
				if (ranges[modifyEnd] + ranges[modifyEnd + 1] > newEnd) break;
				modifyEnd += 2;
			}
			if (ranges[modifyStart] < newStart && newStart < ranges[modifyStart] + ranges[modifyStart + 1]) {
				mergeStyles[mergeCount >> 1] = styles[modifyStart >> 1];
				mergeRanges[mergeCount] = ranges[modifyStart];
				mergeRanges[mergeCount + 1] = newStart - ranges[modifyStart];				
				mergeCount += 2;
			}
			mergeStyles[mergeCount >> 1] = newStyles[i >> 1];
			mergeRanges[mergeCount] = newStart;
			mergeRanges[mergeCount + 1] = newRanges[i + 1];
			mergeCount += 2;
			if (modifyEnd < rangeCount && ranges[modifyEnd] < newEnd && newEnd < ranges[modifyEnd] + ranges[modifyEnd + 1]) {
				mergeStyles[mergeCount >> 1] = styles[modifyEnd >> 1];
				mergeRanges[mergeCount] = newEnd;
				mergeRanges[mergeCount + 1] = ranges[modifyEnd] + ranges[modifyEnd + 1] - newEnd;
				mergeCount += 2;
				modifyLast = 2;
			}
			modifyEnd += modifyLast;
			int grow = mergeCount - (modifyEnd - modifyStart);
			if (rangeCount + grow >= ranges.length) {
				int[] tmpRanges = new int[ranges.length + grow + (GROW << 1)];
				System.arraycopy(ranges, 0, tmpRanges, 0, modifyStart);
				StyleRange[] tmpStyles = new StyleRange[styles.length + (grow >> 1) + GROW];
				System.arraycopy(styles, 0, tmpStyles, 0, modifyStart >> 1);
				if (rangeCount > modifyEnd) {
					System.arraycopy(ranges, modifyEnd, tmpRanges, modifyStart + mergeCount, rangeCount - modifyEnd);
					System.arraycopy(styles, modifyEnd >> 1, tmpStyles, (modifyStart + mergeCount) >> 1, styleCount - (modifyEnd >> 1));
				}
				ranges = tmpRanges;
				styles = tmpStyles;
			} else {
				if (rangeCount > modifyEnd) {
					System.arraycopy(ranges, modifyEnd, ranges, modifyStart + mergeCount, rangeCount - modifyEnd);
					System.arraycopy(styles, modifyEnd >> 1, styles, (modifyStart + mergeCount) >> 1, styleCount - (modifyEnd >> 1));
				}
			}
			System.arraycopy(mergeRanges, 0, ranges, modifyStart, mergeCount);
			System.arraycopy(mergeStyles, 0, styles, modifyStart >> 1, mergeCount >> 1);
			rangeCount += grow;
			styleCount += grow >> 1;
			modifyStart = modifyEnd = modifyEnd + grow - modifyLast;
		}		
	} else {
		int start = newStyles[0].start;
		int modifyStart = getRangeIndex(start, -1, styleCount);
		int modifyEnd = modifyStart;
		if (modifyStart == styleCount) {
			addMerge(newStyles, newStyles.length, modifyStart, modifyEnd);
			return;
		}
		StyleRange[] mergeStyles = new StyleRange[3];
		for (int i = 0; i < newStyles.length; i++) {
			StyleRange newStyle = newStyles[i], style; 
			int newStart = newStyle.start;
			int newEnd = newStart + newStyle.length;
			if (newStart == newEnd) continue;
			int modifyLast = 0, mergeCount = 0;
			while (modifyEnd < styleCount) {
				if (newStart >= styles[modifyStart].start + styles[modifyStart].length) modifyStart++;
				if (styles[modifyEnd].start + styles[modifyEnd].length > newEnd) break;
				modifyEnd++;
			}
			style = styles[modifyStart];
			if (style.start < newStart && newStart < style.start + style.length) {
				style = mergeStyles[mergeCount++] = (StyleRange)style.clone();
				style.length = newStart - style.start;
			}
			mergeStyles[mergeCount++] = newStyle;
			if (modifyEnd < styleCount) {
				style = styles[modifyEnd];
				if (style.start < newEnd && newEnd < style.start + style.length) {
					style = mergeStyles[mergeCount++] = (StyleRange)style.clone();
					style.length += style.start - newEnd;
					style.start = newEnd;
					modifyLast = 1;
				}
			}
			int grow = addMerge(mergeStyles, mergeCount, modifyStart, modifyEnd + modifyLast);
			modifyStart = modifyEnd += grow;
		}
	}
}
int addMerge(StyleRange[] mergeStyles, int mergeCount, int modifyStart, int modifyEnd) {
	int grow = mergeCount - (modifyEnd - modifyStart);
	StyleRange endStyle = null;
	if (modifyEnd < styleCount) endStyle = styles[modifyEnd];
	if (styleCount + grow >= styles.length) {
		StyleRange[] tmpStyles = new StyleRange[styles.length + grow + GROW];
		System.arraycopy(styles, 0, tmpStyles, 0, modifyStart);
		if (styleCount > modifyEnd) {
			System.arraycopy(styles, modifyEnd, tmpStyles, modifyStart + mergeCount, styleCount - modifyEnd);
		}
		styles = tmpStyles;
	} else {
		if (styleCount > modifyEnd) {
			System.arraycopy(styles, modifyEnd, styles, modifyStart + mergeCount, styleCount - modifyEnd);
		}
	}
	int j = modifyStart;
	for (int i = 0; i < mergeCount; i++) {
		StyleRange newStyle = mergeStyles[i], style;
		if (j > 0 && (style = styles[j - 1]).start + style.length == newStyle.start && newStyle.similarTo(style)) {
			style.length += newStyle.length;
		} else {
			styles[j++] = newStyle;
		}
	}
	StyleRange style = styles[j - 1];
	if (endStyle != null && style.start + style.length == endStyle.start && endStyle.similarTo(style)) {
		style.length += endStyle.length;
		modifyEnd++;
		mergeCount++;
	}
	if (styleCount > modifyEnd) {
		System.arraycopy(styles, modifyStart + mergeCount, styles, j, styleCount - modifyEnd);
	}
	grow = (j - modifyStart) - (modifyEnd - modifyStart);
	styleCount += grow;
	return grow;
}
void textChanging(TextChangingEvent event) {
	int start = event.start;
	int newCharCount = event.newCharCount, replaceCharCount = event.replaceCharCount;
	int newLineCount = event.newLineCount, replaceLineCount = event.replaceLineCount;
	
	updateRanges(start, replaceCharCount, newCharCount);	
	
	int startLine = content.getLineAtOffset(start);
	if (replaceCharCount == content.getCharCount()) lines = null;
	if (replaceLineCount == lineCount) {
		lineCount = newLineCount;
		lineWidth = new int[lineCount];
		lineHeight = new int[lineCount];
		reset(0, lineCount);
	} else {
		int delta = newLineCount - replaceLineCount;
		if (lineCount + delta > lineWidth.length) {
			int[] newWidths = new int[lineCount + delta + GROW];
			System.arraycopy(lineWidth, 0, newWidths, 0, lineCount);
			lineWidth = newWidths;			
			int[] newHeights = new int[lineCount + delta + GROW];
			System.arraycopy(lineHeight, 0, newHeights, 0, lineCount);
			lineHeight = newHeights;
			if (lines != null) {
				LineInfo[] newLines = new LineInfo[lineCount + delta + GROW];
				System.arraycopy(lines, 0, newLines, 0, lineCount);
				lines = newLines;
			}
		}
		int startIndex = startLine + replaceLineCount + 1;
		int endIndex = startLine + newLineCount + 1;
		System.arraycopy(lineWidth, startIndex, lineWidth, endIndex, lineCount - startIndex);
		System.arraycopy(lineHeight, startIndex, lineHeight, endIndex, lineCount - startIndex);
		for (int i = startLine; i < endIndex; i++) {
			lineWidth[i] = lineHeight[i] = -1;
		}
		for (int i = lineCount + delta; i < lineCount; i++) {
			lineWidth[i] = lineHeight[i] = -1;
		}
		if (layouts != null) {
			int layoutStartLine = startLine - topIndex;
			int layoutEndLine = layoutStartLine + replaceLineCount + 1;
			for (int i = layoutStartLine; i < layoutEndLine; i++) {
				if (0 <= i && i < layouts.length) {
					if (layouts[i] != null) layouts[i].dispose();
					layouts[i] = null;
				}
			}
			if (delta > 0) {
				for (int i = layouts.length - 1; i >= layoutEndLine; i--) {
					if (0 <= i && i < layouts.length) {
						endIndex = i + delta;
						if (0 <= endIndex && endIndex < layouts.length) {
							layouts[endIndex] = layouts[i];
							layouts[i] = null;
						} else {
							if (layouts[i] != null) layouts[i].dispose();
							layouts[i] = null;
						}
					}
				}
			} else if (delta < 0) {
				for (int i = layoutEndLine; i < layouts.length; i++) {
					if (0 <= i && i < layouts.length) {
						endIndex = i + delta;
						if (0 <= endIndex && endIndex < layouts.length) {
							layouts[endIndex] = layouts[i];
							layouts[i] = null;
						} else {
							if (layouts[i] != null) layouts[i].dispose();
							layouts[i] = null;
						}
					}
				}
			}
		}
		if (lines != null && !(replaceLineCount == 0 && newLineCount == 0)) {
			int startLineOffset = content.getOffsetAtLine(startLine);
			if (startLineOffset != start) startLine++;
			startIndex = startLine + replaceLineCount;
			endIndex = startLine + newLineCount;
			System.arraycopy(lines, startIndex, lines, endIndex, lineCount - startIndex);
			for (int i = startLine; i < endIndex; i++) {
				lines[i] = null;
			}
			for (int i = lineCount + delta; i < lineCount; i++) {
				lines[i] = null;
			}
		}
		lineCount += delta;		
		if (maxWidthLineIndex != -1 && startLine <= maxWidthLineIndex && maxWidthLineIndex <= startLine + replaceLineCount) {
			maxWidth = 0;
			maxWidthLineIndex = -1;
			for (int i = 0; i < lineCount; i++) {
				if (lineWidth[i] > maxWidth) {
					maxWidth = lineWidth[i];
					maxWidthLineIndex = i;
				}
			}
		}
	}	
}
void updateRanges(int start, int replaceCharCount, int newCharCount) {
	if (styleCount == 0 || (replaceCharCount == 0 && newCharCount == 0)) return;
	if (ranges != null) {
		int rangeCount = styleCount << 1;
		int modifyStart = getRangeIndex(start, -1, rangeCount);
		if (modifyStart == rangeCount) return;
		int end = start + replaceCharCount;
		int modifyEnd = getRangeIndex(end, modifyStart - 1, rangeCount);
		int offset = newCharCount - replaceCharCount;
		if (modifyStart == modifyEnd && ranges[modifyStart] < start && end < ranges[modifyEnd] + ranges[modifyEnd + 1]) {
			if (newCharCount == 0) {
				ranges[modifyStart + 1] -= replaceCharCount;
				modifyEnd += 2;
			} else {
				if (rangeCount + 2 > ranges.length) {
					int[] newRanges = new int[ranges.length + (GROW << 1)];
					System.arraycopy(ranges, 0, newRanges, 0, rangeCount);
					ranges = newRanges;
					StyleRange[] newStyles = new StyleRange[styles.length + GROW];
					System.arraycopy(styles, 0, newStyles, 0, styleCount);
					styles = newStyles;
				}
				System.arraycopy(ranges, modifyStart + 2, ranges, modifyStart + 4, rangeCount - (modifyStart + 2));
				System.arraycopy(styles, (modifyStart + 2) >> 1, styles, (modifyStart + 4) >> 1, styleCount - ((modifyStart + 2) >> 1));
				ranges[modifyStart + 3] = ranges[modifyStart] + ranges[modifyStart + 1] - end;
				ranges[modifyStart + 2] = start + newCharCount;
				ranges[modifyStart + 1] = start - ranges[modifyStart];
				styles[(modifyStart >> 1) + 1] = styles[modifyStart >> 1]; 
				rangeCount += 2;
				styleCount++;
				modifyEnd += 4;
			}
			if (offset != 0) {
				for (int i = modifyEnd; i < rangeCount; i += 2) {
					ranges[i] += offset;
				}
			}
		} else {
			if (ranges[modifyStart] < start && start < ranges[modifyStart] + ranges[modifyStart + 1]) {
				ranges[modifyStart + 1] = start - ranges[modifyStart];
				modifyStart += 2;
			}
			if (modifyEnd < rangeCount && ranges[modifyEnd] < end && end < ranges[modifyEnd] + ranges[modifyEnd + 1]) {
				ranges[modifyEnd + 1] = ranges[modifyEnd] + ranges[modifyEnd + 1] - end;
				ranges[modifyEnd] = end;
			}
			if (offset != 0) {
				for (int i = modifyEnd; i < rangeCount; i += 2) {
					ranges[i] += offset;
				}
			}
			System.arraycopy(ranges, modifyEnd, ranges, modifyStart, rangeCount - modifyEnd);
			System.arraycopy(styles, modifyEnd >> 1, styles, modifyStart >> 1, styleCount - (modifyEnd >> 1));
			styleCount -= (modifyEnd - modifyStart) >> 1;
		}
	} else {
		int modifyStart = getRangeIndex(start, -1, styleCount);
		if (modifyStart == styleCount) return;
		int end = start + replaceCharCount;
		int modifyEnd = getRangeIndex(end, modifyStart - 1, styleCount);
		int offset = newCharCount - replaceCharCount;
		if (modifyStart == modifyEnd && styles[modifyStart].start < start && end < styles[modifyEnd].start + styles[modifyEnd].length) {
			if (newCharCount == 0) {
				styles[modifyStart].length -= replaceCharCount;
				modifyEnd++;
			} else {
				if (styleCount + 1 > styles.length) {
					StyleRange[] newStyles = new StyleRange[styles.length + GROW];
					System.arraycopy(styles, 0, newStyles, 0, styleCount);
					styles = newStyles;
				}
				System.arraycopy(styles, modifyStart + 1, styles, modifyStart + 2, styleCount - (modifyStart + 1));
				styles[modifyStart + 1] = (StyleRange)styles[modifyStart].clone();
				styles[modifyStart + 1].length = styles[modifyStart].start + styles[modifyStart].length - end;
				styles[modifyStart + 1].start = start + newCharCount;
				styles[modifyStart].length = start - styles[modifyStart].start;
				styleCount++;
				modifyEnd += 2;
			}
			if (offset != 0) {
				for (int i = modifyEnd; i < styleCount; i++) {
					styles[i].start += offset;
				}
			}
		} else {
			if (styles[modifyStart].start < start && start < styles[modifyStart].start + styles[modifyStart].length) {
				styles[modifyStart].length = start - styles[modifyStart].start;
				modifyStart++;
			}
			if (modifyEnd < styleCount && styles[modifyEnd].start < end && end < styles[modifyEnd].start + styles[modifyEnd].length) {
				styles[modifyEnd].length = styles[modifyEnd].start + styles[modifyEnd].length - end;
				styles[modifyEnd].start = end;
			}
			if (offset != 0) {
				for (int i = modifyEnd; i < styleCount; i++) {
					styles[i].start += offset;
				}
			}
			System.arraycopy(styles, modifyEnd, styles, modifyStart, styleCount - modifyEnd);
			styleCount -= modifyEnd - modifyStart;
		}
	}
}
}

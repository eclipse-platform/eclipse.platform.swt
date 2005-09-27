
/* UNDER CONSTRUCTION - DO NOT USE THIS CLASS */

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

/**
 * A StyledTextRenderer renders the content of a StyledText widget.
 * This class can be used to render to the display or to a printer.
 */
class StyledTextRenderer2 {
	private Device device;					// device to render on
	protected Font regularFont, boldFont, italicFont, boldItalicFont;
	private int tabWidth;					// width in pixels of a tab character
	private int ascent, descent;
	private int lineEndSpaceWidth;			// width in pixels of the space used to represent line delimiters
	private int averageCharWidth;
	private StyledText2 styledText;			// used to get content and styles during rendering, can be null when on a printer
	private int topIndex = -1;
	private TextLayout[] layouts;
	private GC printerGC;
	
	private static final int CACHE_SIZE = 128; //text layout cache size
	
/**
 * Creates an instance of <class>StyledTextRenderer</class> that renders 
 * to the display.
 * </p>
 * @param device Device to render on
 * @param regularFont Font to use for regular text
 * @param leftMargin margin to the left of the text
 */
StyledTextRenderer2(Device device, Font font, StyledText2 styledText, int tabLength) {
	this.device = device;
	this.regularFont = font;
	this.styledText = styledText;
	calculateLineHeight();
	setTabLength(tabLength);
}
/**
 * Creates an instance of <class>StyledTextRenderer</class> that renders 
 * to the display.
 * </p>
 * @param device Device to render on
 * @param regularFont Font to use for regular text
 * @param leftMargin margin to the left of the text
 */
StyledTextRenderer2(Device device, Font font, GC gc, int tabLength) {
	this.device = device;
	this.regularFont = font;
	this.printerGC = gc;
	calculateLineHeight();
	setTabLength(tabLength);
}
/**
 * Calculates the line height and space width.
 */
private void calculateLineHeight() {
	GC gc = getGC();
	lineEndSpaceWidth = gc.stringExtent(" ").x;	
	
	// don't assume that bold and normal fonts have the same height
	// fixes bug 41773
	Font originalFont = gc.getFont();
	FontMetrics metrics = gc.getFontMetrics();
	ascent = Math.max(ascent, metrics.getAscent() + metrics.getLeading());
	descent = Math.max(descent, metrics.getDescent());
	averageCharWidth = Math.max(averageCharWidth, metrics.getAverageCharWidth());
	gc.setFont(getFont(SWT.BOLD));
	metrics = gc.getFontMetrics();
	ascent = Math.max(ascent, metrics.getAscent() + metrics.getLeading());
	descent = Math.max(descent, metrics.getDescent());
	averageCharWidth = Math.max(averageCharWidth, metrics.getAverageCharWidth());
	gc.setFont(getFont(SWT.ITALIC));
	metrics = gc.getFontMetrics();
	ascent = Math.max(ascent, metrics.getAscent() + metrics.getLeading());
	descent = Math.max(descent, metrics.getDescent());
	averageCharWidth = Math.max(averageCharWidth, metrics.getAverageCharWidth());
	gc.setFont(getFont(SWT.BOLD | SWT.ITALIC));
	metrics = gc.getFontMetrics();
	ascent = Math.max(ascent, metrics.getAscent() + metrics.getLeading());
	descent = Math.max(descent, metrics.getDescent());
	averageCharWidth = Math.max(averageCharWidth, metrics.getAverageCharWidth());
	gc.setFont(originalFont);
	disposeGC(gc);
	
	// clear the font cache
	if (boldFont != null) boldFont.dispose();
	if (italicFont != null) italicFont.dispose();
	if (boldItalicFont != null) boldItalicFont.dispose();
	boldFont = italicFont = boldItalicFont = null;
}
/**
 * Disposes the resource created by the receiver.
 */
void dispose() {
	if (boldFont != null) boldFont.dispose();
	if (italicFont != null) italicFont.dispose();
	if (boldItalicFont != null) boldItalicFont.dispose();
	boldFont = italicFont = boldItalicFont = null;
	if (layouts != null) {
		for (int i = 0; i < layouts.length; i++) {
			TextLayout layout = layouts[i];
			if (layout != null) layout.dispose();
		}
		layouts = null;
	}
	topIndex = -1;
}
/**
 * Dispose the specified GC.
 * Allows subclasses to reuse GCs.
 * </p>
 * @param gc GC to dispose.
 */
private void disposeGC(GC gc) {
	if (gc != printerGC) {
		gc.dispose();
	}
}
/** 
 * Draws a line of text at the specified location.
 * </p>
 *
 * @param line the line to draw
 * @param lineIndex	index of the line to draw
 * @param paintY y location to draw at
 * @param gc GC to draw on
 * @param widgetBackground the widget background color. 
 * 	Used as the default rendering color.
 * @param widgetForeground the widget foreground color. 
 * 	Used as the default rendering color. 
 * @param clearBackground true if the line background should be drawn
 * explicitly.
 */
int drawLine(String line, int lineIndex, int paintX, int paintY, GC gc, Color widgetBackground, Color widgetForeground, boolean clearBackground) {
	if (styledText == null) return 0;
	StyledTextContent content = styledText.getContent();
	int lineOffset = content.getOffsetAtLine(lineIndex);
	int lineLength = line.length();
	Point selection = styledText.getSelection();
	int selectionStart = selection.x;
	int selectionEnd = selection.y;
	TextLayout layout = getTextLayout(line, lineOffset);
	Rectangle client = styledText.getClientArea();  
	StyledTextEvent event = styledText.getLineBackgroundData(lineOffset, line);
	Color lineBackground = null;
	if (event != null) {
		lineBackground = event.lineBackground;
	}
	if (lineBackground == null) {
		lineBackground = widgetBackground;
	}
	boolean fullSelection = (styledText.getStyle() & SWT.FULL_SELECTION) != 0;
	if (clearBackground &&
		(!fullSelection || 
		 selectionStart > lineOffset || 
		 selectionEnd <= lineOffset + lineLength)) {
		// draw background if full selection is off or if line is not 
		// completely selected
		gc.setBackground(lineBackground);
		gc.fillRectangle(client.x, paintY, client.width, layout.getBounds().height);
	}
	if (selectionStart != selectionEnd) {
		int y = paintY;
		int[] offsets = layout.getLineOffsets();
		int lineCount = layout.getLineCount();
		gc.setBackground(styledText.getSelectionBackground());
		for (int i = 0; i < lineCount; i++) {
			int lineStart = offsets[i];
			int lineEnd = Math.max(0, offsets[i + 1] - 1);
			if (lineStart >= selectionEnd - lineOffset) break;
			Rectangle lineBounds = layout.getLineBounds(i);
			if (selectionStart - lineOffset <= lineEnd && lineEnd < selectionEnd - lineOffset - 1) {
				int x = paintX + lineBounds.x + lineBounds.width;
				if (fullSelection) {
					gc.fillRectangle(x, y, client.width - x, lineBounds.height);
				} else {
					if (i == lineCount - 1) {
						gc.fillRectangle(x, y, lineEndSpaceWidth, lineBounds.height);
					}
				}
			}
			y += lineBounds.height;
		}
	}
	gc.setForeground(widgetForeground);
	gc.setBackground(lineBackground);	
	if (selectionStart == selectionEnd || (selectionEnd <= lineOffset && selectionStart > lineOffset + lineLength - 1)) {
		layout.draw(gc, paintX, paintY);
	} else {
		int start = Math.max(0, selectionStart - lineOffset);
		int end = Math.min(lineLength, selectionEnd - lineOffset);
		Color selectionFk = styledText.getSelectionForeground();
		Color selectionBk = styledText.getSelectionBackground();
		layout.draw(gc, paintX, paintY, start, end - 1, selectionFk, selectionBk);
	}
	int height = layout.getBounds().height;
	disposeTextLayout(layout);
	return height;
}
/**
 * 	Draw a line using the printerGC
 * 
 */
void drawLine(int paintX, int paintY, GC gc, Color foreground, Color background, TextLayout layout) {
	if (background != null) {
		gc.setBackground(background);
		int lineCount = layout.getLineCount();
		for (int i = 0; i < lineCount; i++) {
			Rectangle rect = layout.getLineBounds(i);
			rect.x += paintX;
			rect.y += paintY;
			gc.fillRectangle(rect);
		}
	}
	gc.setForeground(foreground);
	layout.draw(gc, paintX, paintY);
}
/**
 * Returns the baseline of the receiver
 */
int getBaseline() {
	return ascent;
}
/**
 * Returns the text segments that should be treated as if they 
 * had a different direction than the surrounding text.
 * </p>
 *
 * @param lineOffset offset of the first character in the line. 
 * 	0 based from the beginning of the document.
 * @param line text of the line to specify bidi segments for
 * @return text segments that should be treated as if they had a
 * 	different direction than the surrounding text. Only the start 
 * 	index of a segment is specified, relative to the start of the 
 * 	line. Always starts with 0 and ends with the line length. 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the segment indices returned 
 * 		by the listener do not start with 0, are not in ascending order,
 * 		exceed the line length or have duplicates</li>
 * </ul>
 */
private int[] getBidiSegments(int lineOffset, String lineText) {
	if (styledText == null || !styledText.isBidi()) return null;
	return styledText.getBidiSegments(lineOffset, lineText);
}
/**
 *  Returns the Font for a StyleRange
 */
Font getFont (StyleRange styleRange) {
	if (styleRange.font != null) return styleRange.font;
	return getFont(styleRange.fontStyle);
}
/**
 *  Returns the Font according with the given style
 */
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
private FontData[] getFontData(int style) {
	FontData[] fontDatas = regularFont.getFontData();
	for (int i = 0; i < fontDatas.length; i++) {
		fontDatas[i].setStyle(style);
	}
	return fontDatas;
}
/**
 * Returns the GC to use for rendering and measuring.
 * Allows subclasses to reuse GCs.
 * </p>
 * @return the GC to use for rendering and measuring.
 */
private GC getGC() {
	if (printerGC != null) return printerGC;
	return new GC(styledText);
}
/**
 * Returns the height in pixels of a line.
 * </p>
 * @return the height in pixels of a line.
 */
int getLineHeight() {
	return ascent + descent;
}
int getAverageCharWidth () {
	return averageCharWidth;
}
/**
 * Returns the line style data for the given line or null if there is 
 * none. If there is a LineStyleListener but it does not set any styles, 
 * the StyledTextEvent.styles field will be initialized to an empty 
 * array.
 * </p>
 * 
 * @param lineOffset offset of the line start relative to the start of 
 * 	the content.
 * @param line line to get line styles for
 * @return line style data for the given line. Styles may start before 
 * 	line start and end after line end
 */
private StyledTextEvent getLineStyleData(int lineOffset, String line) {
	return styledText != null ? styledText.getLineStyleData(lineOffset, line) : null;
}
/**
 *
 */
private int getOrientation () {
	if (styledText != null) {
		return styledText.getOrientation();
	} else {
		int mask = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
		return printerGC.getStyle() & mask;
	}
}
/**
 * Calculates the width in pixel of a tab character
 * </p>
 * @param tabLength number of space characters represented by a tab character.
 */
void setTabLength(int tabLength) {
	GC gc = getGC();
	StringBuffer tabBuffer = new StringBuffer(tabLength);
	for (int i = 0; i < tabLength; i++) {
		tabBuffer.append(' ');
	}
	tabWidth = gc.stringExtent(tabBuffer.toString()).x;
	disposeGC(gc);
}
int getWidth () {
	return styledText != null ? styledText.getWrapWidth() : -1;
}
TextLayout getTextLayout(String line, int lineOffset) {
	int[] bidiSegments = getBidiSegments(lineOffset, line);
	StyledTextEvent event = getLineStyleData(lineOffset, line);
	StyleRange[] styles = event != null ? event.styles : null;
	return getTextLayout(line, lineOffset, bidiSegments, styles);
}
/**
 *  Returns TextLayout given a line, a list of styles, and a list of bidi segments 
 */
TextLayout getTextLayout(String line, int lineOffset, int[] bidiSegments, StyleRange[] styles) {
	TextLayout layout = createTextLayout(lineOffset);
	layout.setFont(regularFont);
	layout.setAscent(ascent);
	layout.setDescent(descent);
	layout.setText(line);
	layout.setOrientation(getOrientation());
	layout.setSegments(bidiSegments);
	layout.setTabs(new int[]{tabWidth});
	layout.setWidth(getWidth());
	int length = line.length();
	int lastOffset = 0;
	if (styles != null) {
		for (int styleIndex = 0; styleIndex < styles.length; styleIndex++) {
			StyleRange style = styles[styleIndex];
			if (style.isUnstyled()) continue;
			int start, end;
			if (lineOffset > style.start) {
				start = 0;
				end = Math.min (length, style.length - lineOffset + style.start);
			} else {
				start = style.start - lineOffset;
				end = Math.min(length, start + style.length);
			}
			if (start >= length) break;
			if (lastOffset < start) {
				layout.setStyle(null, lastOffset, start - 1);	
			}
			TextStyle textStyle = new TextStyle(getFont(style), style.foreground, style.background);
			textStyle.underline = style.underline;
			textStyle.strikeout = style.strikeout;
			layout.setStyle(textStyle, start, end - 1);
			lastOffset = Math.max(lastOffset, end);
		}
	}
	if (lastOffset < length) layout.setStyle(null, lastOffset, length);
	return layout;
}
private TextLayout createTextLayout(int lineOffset) {
	if (styledText != null) {
		StyledTextContent content = styledText.getContent();
		int lineIndex = content.getLineAtOffset(lineOffset);
		updateTopIndex();
		if (layouts != null) {
			int layoutIndex = lineIndex - topIndex;
			if (0 <= layoutIndex && layoutIndex < layouts.length) {
				TextLayout layout = layouts[layoutIndex];
				if (layout != null) return layout;
				return layouts[layoutIndex] = new TextLayout(device);
			}
		}
	}
	return new TextLayout(device);
}
void disposeTextLayout (TextLayout layout) {
	if (layouts != null) {
		for (int i = 0; i < layouts.length; i++) {
			if (layouts[i] == layout) return;
		}
	}
	layout.dispose();
}
private void updateTopIndex() {
	int topIndex = styledText.topIndex > 0 ? styledText.topIndex - 1 : 0;	
	if (layouts == null || topIndex != this.topIndex) {
		TextLayout[] newLayouts = new TextLayout[CACHE_SIZE];
		if (layouts != null) {
			for(int i = 0; i < layouts.length; i++) {
				TextLayout layout = layouts[i];
				if (layout != null) {
					int layoutIndex = (i + this.topIndex) - topIndex;
					if (0 <= layoutIndex && layoutIndex < newLayouts.length) {
						newLayouts[layoutIndex] = layout;
					} else {
						layout.dispose();
					}
				}
			}
		}
		this.topIndex = topIndex;
		layouts = newLayouts;
	}
}
}

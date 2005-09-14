
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
	private StyledText2 styledText;			// used to get content and styles during rendering, can be null when on a printer
	private int topIndex = -1;
	private TextLayout[] layouts;
	private GC printerGC;
	
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
	gc.setFont(getFont(SWT.BOLD));
	metrics = gc.getFontMetrics();
	ascent = Math.max(ascent, metrics.getAscent() + metrics.getLeading());
	descent = Math.max(descent, metrics.getDescent());
	gc.setFont(getFont(SWT.ITALIC));
	metrics = gc.getFontMetrics();
	ascent = Math.max(ascent, metrics.getAscent() + metrics.getLeading());
	descent = Math.max(descent, metrics.getDescent());
	gc.setFont(getFont(SWT.BOLD | SWT.ITALIC));
	metrics = gc.getFontMetrics();
	ascent = Math.max(ascent, metrics.getAscent() + metrics.getLeading());
	descent = Math.max(descent, metrics.getDescent());
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
void drawLine(String line, int lineIndex, int paintX, int paintY, GC gc, Color widgetBackground, Color widgetForeground, boolean clearBackground) {
	int lineOffset = getContent().getOffsetAtLine(lineIndex);
	int lineLength = line.length();
	Point selection = getSelection();
	int selectionStart = selection.x;
	int selectionEnd = selection.y;
	Color lineBackground = null;
	TextLayout layout = getTextLayout(line, lineOffset);
	Rectangle client = getClientArea();
	StyledTextEvent event = getLineBackgroundData(lineOffset, line);
	if (event != null) {
		lineBackground = event.lineBackground;
	}
	if (lineBackground == null) {
		lineBackground = widgetBackground;
	}
	
	if (clearBackground &&
		(!isFullLineSelection() || 
		 selectionStart > lineOffset || 
		 selectionEnd <= lineOffset + lineLength)) {
		// draw background if full selection is off or if line is not 
		// completely selected
		gc.setBackground(lineBackground);
		gc.fillRectangle(client.x, paintY, client.width, ascent + descent);
	}
	if (selectionStart != selectionEnd) {
		Rectangle rect = layout.getLineBounds(0);
		drawFullLineSelection(line, lineOffset, paintX + rect.x + rect.width, paintY, gc);
	}
	gc.setForeground(widgetForeground);
	gc.setBackground(lineBackground);	
	if (selectionStart == selectionEnd || (selectionEnd <= lineOffset && selectionStart > lineOffset + lineLength - 1)) {
		layout.draw(gc, paintX, paintY);
	} else {
		int start = Math.max(0, selectionStart - lineOffset);
		int end = Math.min(lineLength, selectionEnd - lineOffset);
		layout.draw(gc, paintX, paintY, start, end - 1, getSelectionForeground(), getSelectionBackground());
	}
	disposeTextLayout(layout);
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
 * Draws the background of the line selection.
 * Implemented by subclasses for optional selection rendering.
 * </p>
 *
 * @param line the line to draw
 * @param lineOffset offset of the first character in the line.
 * 	Relative to the start of the document.
 * @param styles line styles
 * @param paintY y location to draw at
 * @param gc GC to draw on
 * @param bidi the bidi object to use for measuring and rendering 	text in bidi
 * locales. null when not in bidi mode.
 */
private void drawFullLineSelection(String line, int lineOffset, int paintX, int paintY, GC gc) {
	if (styledText == null) return;//don't draw full selection on a printer
	Point selection = styledText.internalGetSelection();
	int lineLength = line.length();
	int selectionStart = Math.max(0, selection.x - lineOffset);
	int selectionEnd = selection.y - lineOffset;
	
	if (selectionEnd == selectionStart || selectionEnd < 0 || selectionStart > lineLength || selectionEnd <= lineLength) {
		return;
	}
	
	int lineHeight = getLineHeight();
	gc.setBackground(styledText.getSelectionBackground());
	if ((styledText.getStyle() & SWT.FULL_SELECTION) != 0) {
		Rectangle rect = getClientArea();
		gc.fillRectangle(paintX, paintY, rect.width - paintX, lineHeight);
	} else {
		boolean isWrappedLine = false;
		if (styledText.internalGetWordWrap()) {
			StyledTextContent content = getContent();
			int lineEnd = lineOffset + lineLength;
			int lineIndex = content.getLineAtOffset(lineEnd);

			// is the start offset of the next line the same as the end 
			// offset of this line?
			if (lineIndex < content.getLineCount() - 1 &&
				content.getOffsetAtLine(lineIndex + 1) == lineEnd) {
				isWrappedLine = true;
			}
		}
		if (!isWrappedLine) {
			// render the line break selection
			gc.fillRectangle(paintX, paintY, lineEndSpaceWidth, lineHeight);
		}
	}
}

/**
 * Returns the visible client area that can be used for rendering.
 * </p>
 * @return the visible client area that can be used for rendering.
 */
private Rectangle getClientArea() {
	return styledText != null ? styledText.getClientArea() : null;
}
/**
 * Returns the <class>StyledTextContent</class> to use for line offset
 * calculations.
 * </p>
 * @return the <class>StyledTextContent</class> to use for line offset
 * calculations.
 */
private StyledTextContent getContent() {
	return styledText != null ? styledText.internalGetContent() : null;
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
 * Returns the line background data for the given line or null if 
 * there is none. 
 * </p>
 * @param lineOffset offset of the line start relative to the start
 * 	of the content.
 * @param line line to get line background data for
 * @return line background data for the given line. may return null
 */
private StyledTextEvent getLineBackgroundData(int lineOffset, String line) {
	return styledText != null ? styledText.getLineBackgroundData(lineOffset, line) : null;
}
/**
 * Returns the height in pixels of a line.
 * </p>
 * @return the height in pixels of a line.
 */
int getLineHeight() {
	return ascent + descent;
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
	if (styledText != null) {
		StyledTextEvent event = styledText.getLineStyleData(lineOffset, line);
		if (event != null) {
			if (event.styles != null && getWordWrap()) {
				event.styles = getVisualLineStyleData(event.styles, lineOffset, line.length());
			}		
		}
		return event;
	}
	return null;
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
 *
 */
private Color getSelectionForeground() {
	return styledText != null ? styledText.getSelectionForeground() : null;
}
/**
 *
 */
private Color getSelectionBackground() {
	return styledText != null ? styledText.getSelectionBackground() : null;
}
/**
 * Returns the widget selection.
 * Implemented by subclasses for optional selection rendering.
 * </p>
 * @return the widget selection.
 */
private Point getSelection() {
	return styledText != null ? styledText.internalGetSelection() : new Point (0, 0);
}
/**
 * Returns styles for the specified visual (wrapped) line.
 * </p>
 * 
 * @param logicalStyles the styles for a logical (unwrapped) line
 * @param lineOffset offset of the visual line
 * @param lineLength length of the visual line
 * @return styles in the logicalStyles array that are at least 
 * 	partially on the specified visual line.
 */
private StyleRange[] getVisualLineStyleData(StyleRange[] logicalStyles, int lineOffset, int lineLength) {
	int lineEnd = lineOffset + lineLength;
	int oldStyleCount = logicalStyles.length;
	int newStyleCount = 0;
	
	for (int i = 0; i < oldStyleCount; i++) {
		StyleRange style = logicalStyles[i];
		if (style.start < lineEnd && style.start + style.length > lineOffset) {
			newStyleCount++;
		}
	}
	if (newStyleCount != oldStyleCount) {
		StyleRange[] newStyles = new StyleRange[newStyleCount];
		for (int i = 0, j = 0; i < oldStyleCount; i++) {
			StyleRange style = logicalStyles[i];
			if (style.start < lineEnd && style.start + style.length > lineOffset) {
				newStyles[j++] = logicalStyles[i];						
			}
		}
		logicalStyles = newStyles;
	}
	return logicalStyles;
}
/**
 * Returns the word wrap state.
 * </p>
 * @return true=word wrap is on. false=no word wrap, lines may extend 
 * 	beyond the right side of the client area.
 */
private boolean getWordWrap() {
	return styledText != null ? styledText.getWordWrap () : true;
}
/**
 * Returns whether the widget was created with the SWT.FULL_SELECTION style.
 * Implemented by subclasses for optional selection rendering.
 * </p>
 * @return true=the widget is running in full line selection mode, 
 * 	false=otherwise
 */
private boolean isFullLineSelection() {
	return styledText != null ? (styledText.getStyle() & SWT.FULL_SELECTION) != 0 : false;
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
TextLayout getTextLayout(String line, int lineOffset) {
	int[] bidiSegments = getBidiSegments(lineOffset, line);
	StyledTextEvent event = getLineStyleData(lineOffset, line);
	StyleRange[] styles = event != null ? event.styles : null;
	return getTextLayout(line, lineOffset, bidiSegments, styles);
}
/**
 *  Returns TextLayout given a line offset, an array of styles, and the bidi segments 
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
			TextStyle textStyle = new TextStyle(getFont(style.fontStyle), style.foreground, style.background);
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
	if (styledText != null && !styledText.internalGetWordWrap()) {
		int lineIndex = getContent().getLineAtOffset(lineOffset);
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
	int verticalIncrement = styledText.getVerticalIncrement();
	int topIndex = verticalIncrement == 0 ? 0 : styledText.verticalScrollOffset / verticalIncrement;
	int newLength = Math.max(1 , styledText.getPartialBottomIndex() - topIndex + 1);
	if (layouts == null || topIndex != this.topIndex || newLength != layouts.length) {
		TextLayout[] newLayouts = new TextLayout[newLength];
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

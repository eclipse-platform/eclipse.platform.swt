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
 * Subclasses can provide a different device (e.g., Display, Printer) 
 * to render on and implement abstract methods to return resources 
 * created on that device.
 */
abstract class StyledTextRenderer {
	private Device device;					// device to render on
	protected Font regularFont, boldFont, italicFont, boldItalicFont;
	private int tabWidth;					// width in pixels of a tab character
	private int ascent, descent;
	private int lineEndSpaceWidth;			// width in pixels of the space used to represent line delimiters
	
/**
 * Creates an instance of <class>StyledTextRenderer</class>.
 * </p>
 * @param device Device to render on
 * @param regularFont Font to use for regular text
 * @param leftMargin margin to the left of the text
 */
StyledTextRenderer(Device device, Font regularFont) {
	this.device = device;
	this.regularFont = regularFont;
}
/**
 * Calculates the line height and space width.
 */
void calculateLineHeight() {
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
}
/**
 * Dispose the specified GC.
 * Allows subclasses to reuse GCs.
 * </p>
 * @param gc GC to dispose.
 */
protected abstract void disposeGC(GC gc);
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
void drawLine(String line, int lineIndex, int paintY, GC gc, Color widgetBackground, Color widgetForeground, boolean clearBackground) {
	int lineOffset = getContent().getOffsetAtLine(lineIndex);
	int lineLength = line.length();
	Point selection = getSelection();
	int selectionStart = selection.x;
	int selectionEnd = selection.y;
	int leftMargin = getLeftMargin();
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
		(isFullLineSelection() == false || 
		 selectionStart > lineOffset || 
		 selectionEnd <= lineOffset + lineLength)) {
		// draw background if full selection is off or if line is not 
		// completely selected
		gc.setBackground(lineBackground);
		gc.setForeground(lineBackground);
		gc.fillRectangle(client.x + leftMargin, paintY, client.width, ascent + descent);
	}
	int paintX = client.x + leftMargin - getHorizontalPixel();
	if (selectionStart != selectionEnd) {
		Rectangle rect = layout.getLineBounds(0);
		drawLineBreakSelection(line, lineOffset, paintX + rect.x + rect.width, paintY, gc);
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
protected abstract void drawLineBreakSelection(String line, int lineOffset, int paintX, int paintY, GC gc);

/**
 * Returns the visible client area that can be used for rendering.
 * </p>
 * @return the visible client area that can be used for rendering.
 */
protected abstract Rectangle getClientArea();
/**
 * Returns the <class>StyledTextContent</class> to use for line offset
 * calculations.
 * </p>
 * @return the <class>StyledTextContent</class> to use for line offset
 * calculations.
 */
protected abstract StyledTextContent getContent();
/**
 * Returns the Device that is being rendered on.
 * </p>
 * @return the Device that is being rendered on.
 */
Device getDevice() {
	return device;
}
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
protected abstract int[] getBidiSegments(int lineOffset, String lineText);
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
FontData[] getFontData(int style) {
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
protected abstract GC getGC();
/**
 * Returns the horizontal scroll position.
 * </p>
 * @return the horizontal scroll position.
 */
protected abstract int getHorizontalPixel();
protected int getLeftMargin() {
	return 0;
}
/**
 * Returns the width in pixels of the space used to represent line delimiters.
 * @return the width in pixels of the space used to represent line delimiters.
 */
int getLineEndSpaceWidth() {
	return lineEndSpaceWidth;
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
protected abstract StyledTextEvent getLineBackgroundData(int lineOffset, String line);
/**
 * Returns the height in pixels of a line.
 * </p>
 * @return the height in pixels of a line.
 */
int getLineHeight() {
	return ascent + descent;
}
/**
 * Returns the line style data for the specified line.
 * The lineOffset and line may specify a segment of a logical line stored
 * in the <class>StyledTextContent</class> of the widget.
 * The returned styles are guaranteed to be at least partially on the
 * segment.
 * </p>
 * @param event the styles for the logical line
 * @param lineOffset offset of the line start relative to the start of 
 * 	the content.
 * @param line line to get line styles for
 * @return line style data for the given line segment. Styles may start 
 * 	before line start and end after line end but are guaranteed to be at 
 * 	least partially on the line.
 */
StyledTextEvent getLineStyleData(StyledTextEvent event, int lineOffset, String line) {
	int lineLength = line.length();
	
	if (event.styles != null && getWordWrap()) {
		event.styles = getVisualLineStyleData(event.styles, lineOffset, lineLength);
	}
	if (event.styles == null) {
		event.styles = new StyleRange[0];
	}
	return event;
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
protected abstract StyledTextEvent getLineStyleData(int lineOffset, String line);
/**
 *
 */
protected abstract int getOrientation ();
/**
*
*/
protected int getRightMargin() {
	return 0;
}
/**
 *
 */
protected abstract Color getSelectionForeground();
/**
 *
 */
protected abstract Color getSelectionBackground();
/**
 * Returns the widget selection.
 * Implemented by subclasses for optional selection rendering.
 * </p>
 * @return the widget selection.
 */
protected abstract Point getSelection();
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
StyleRange[] getVisualLineStyleData(StyleRange[] logicalStyles, int lineOffset, int lineLength) {
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
protected abstract boolean getWordWrap();
/**
 * Returns whether the widget was created with the SWT.FULL_SELECTION style.
 * Implemented by subclasses for optional selection rendering.
 * </p>
 * @return true=the widget is running in full line selection mode, 
 * 	false=otherwise
 */
protected abstract boolean isFullLineSelection();
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
/**
 *  Returns TextLayout given a line index and an array of styles 
 */
TextLayout getTextLayout(String line, int lineOffset) {
	TextLayout layout = createTextLayout(lineOffset);
	layout.setFont(regularFont);
	layout.setAscent(ascent);
	layout.setDescent(descent);
	layout.setText(line);
	layout.setOrientation(getOrientation());
	layout.setSegments(getBidiSegments(lineOffset, line));
	layout.setTabs(new int[]{tabWidth});
	int length = line.length();
	StyledTextEvent event = getLineStyleData(lineOffset, line);
	StyleRange[] styles = event != null ? event.styles : null;
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
TextLayout createTextLayout(int lineOffset) {
	return new TextLayout(device);
}
void disposeTextLayout (TextLayout layout) {
	layout.dispose();
}
}

package org.eclipse.swt.custom;

/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;

/**
 * A PrintRenderer renders the content of a StyledText widget on 
 * a printer device.
 * Print rendering may occur in a non-UI thread. Therefore all 
 * requests for styles, content and any other information normally 
 * stored in the StyledText widget are served from cached data.
 * Caching also guarantees immutable data for threaded printing.
 */
class PrintRenderer extends StyledTextRenderer {
	StyledTextContent logicalContent;		// logical, unwrapped, content
	WrappedContent content;					// wrapped content
	Rectangle clientArea;					// printer client area
	GC gc;									// printer GC, there can be only one GC for each printer device
	Hashtable lineBackgrounds;				// line background colors used during rendering
	Hashtable lineStyles;					// line styles colors used during rendering
	Hashtable bidiSegments;			 		// bidi segments used during rendering on bidi platforms
	
/**
 * Creates an instance of <class>PrintRenderer</class>.
 * </p>
 * @param device Device to render on
 * @param regularFont Font to use for regular (non-bold) text.
 * @param isBidi true=bidi platform, false=no bidi platform.
 * @param gc printer GC to use for rendering. There can be only one GC for 
 * 	each printer device at any given time.
 * @param logicalContent StyledTextContent to print.
 * @param lineBackgrounds line background colors to use during rendering.
 * @param lineStyles line styles colors to use during rendering.
 * @param bidiSegments bidi segments to use during rendering on bidi platforms.
 * @param leftMargin margin to the left of the text.
 * @param tabLength length in characters of a tab character
 * @param clientArea the printer client area.
 */
PrintRenderer(
		Device device, Font regularFont, boolean isBidi, GC gc, 
		StyledTextContent logicalContent, Hashtable lineBackgrounds, 
		Hashtable lineStyles, Hashtable bidiSegments,
		int tabLength, Rectangle clientArea) {
	super(device, regularFont, isBidi, clientArea.x);
	this.logicalContent = logicalContent;
	this.lineBackgrounds = lineBackgrounds;
	this.lineStyles = lineStyles;
	this.bidiSegments = bidiSegments;	
	this.clientArea = clientArea;	
	this.gc = gc;
	calculateLineHeight();
	setTabLength(tabLength);
	content = new WrappedContent(this, logicalContent);
	// wrapLines requires tab width to be known	
	content.wrapLines();
}
/**
 * Disposes the resource created by the receiver.
 */
protected void dispose() {
	content = null;
	super.dispose();
}
/**
 * Do nothing. PrintRenderer does not create GCs.
 * @see StyledTextRenderer#disposeGC
 */
protected void disposeGC(GC gc) {
}
/** 
 * Do not print the selection.
 * @see StyledTextRenderer#drawLineSelectionBackground
 */
protected void drawLineSelectionBackground(String line, int lineOffset, StyleRange[] styles, int paintY, GC gc, StyledTextBidi bidi) {
}
/**
 * Returns from cache the text segments that should be treated as 
 * if they had a different direction than the surrounding text.
 * <p>
 * Use cached data.
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
protected int[] getBidiSegments(int lineOffset, String lineText) {
	int lineLength = lineText.length();
	int logicalLineOffset = getLogicalLineOffset(lineOffset);
	int[] segments = (int []) bidiSegments.get(new Integer(logicalLineOffset));
	
	if (segments == null) {
		segments = new int[] {0, lineLength};
	}
	else {
		// cached bidi segments are for logical lines.
		// make sure that returned segments match requested line since
		// line wrapping may require either entire or part of logical 
		// line bidi segments
		int logicalLineIndex = logicalContent.getLineAtOffset(lineOffset);
		int logicalLineLength = logicalContent.getLine(logicalLineIndex).length();
		
		if (lineOffset != logicalLineOffset || lineLength != logicalLineLength) {
			int lineOffsetDelta = lineOffset - logicalLineOffset;
			int newSegmentCount = 0;
			int[] newSegments = new int[segments.length];
			
			for (int i = 0; i < segments.length; i++) {
				newSegments[i] = Math.max(0, segments[i] - lineOffsetDelta);
				if (newSegments[i] > lineLength) {
					newSegments[i] = lineLength;
					newSegmentCount++;
					break;
				}
				if (i == 0 || newSegments[i] > 0) {
					newSegmentCount++;
				}
			}
			segments = new int[newSegmentCount];
			for (int i = 0, newIndex = 0; i < newSegments.length && newIndex < newSegmentCount; i++) {
				if (i == 0 || newSegments[i] > 0) {
					segments[newIndex++] = newSegments[i];
				}
			}
		}
	}
	return segments;
}
/**
 * Returns the printer client area.
 * </p>
 * @return the visible client area that can be used for rendering.
 * @see StyledTextRenderer#getClientArea
 */
protected Rectangle getClientArea() {
	return clientArea;
}
/**
 * Returns the <class>StyledTextContent</class> to use for line offset
 * calculations.
 * This is the wrapped content, calculated in the constructor from the 
 * logical printing content.
 * </p>
 * @return the <class>StyledTextContent</class> to use for line offset
 * calculations.
 */
protected StyledTextContent getContent() {
	return content;
}
/**
 * Returns the printer GC to use for rendering and measuring.
 * There can be only one GC for each printer device at any given
 * time.
 * </p>
 * @return the printer GC to use for rendering and measuring.
 */
protected GC getGC() {
	return gc;
}
/**
 * Returns 0. Scrolling does not affect printing. Text is wrapped
 * for printing.
 * </p>
 * @return 0
 * @see StyledTextRenderer#getHorizontalPixel
 */
protected int getHorizontalPixel() {
	return 0;
}
/**
 * Returns the start offset of the line at the given offset.
 * </p>
 * @param visualLineOffset an offset that may be anywhere within a 
 * 	line.
 * @return the start offset of the line at the given offset, 
 * 	relative to the start of the document.
 */
private int getLogicalLineOffset(int visualLineOffset) {
	int logicalLineIndex = logicalContent.getLineAtOffset(visualLineOffset);
	
	return logicalContent.getOffsetAtLine(logicalLineIndex);
}
/**
 * Return cached line background data.
 * @see StyledTextRenderer#getLineBackgroundData
 */
protected StyledTextEvent getLineBackgroundData(int lineOffset, String line) {
	int logicalLineOffset = getLogicalLineOffset(lineOffset);
	
	return (StyledTextEvent) lineBackgrounds.get(new Integer(logicalLineOffset));
}
/**
 * Return cached line style background data.
 * @see StyledTextRenderer#getLineStyleData
 */
protected StyledTextEvent getLineStyleData(int lineOffset, String line) {
	int logicalLineOffset = getLogicalLineOffset(lineOffset);
	StyledTextEvent logicalLineEvent = (StyledTextEvent) lineStyles.get(new Integer(logicalLineOffset));
	
	if (logicalLineEvent != null) {
		StyledTextEvent clone = new StyledTextEvent((StyledTextContent) logicalLineEvent.data);
		clone.detail = logicalLineEvent.detail;
		clone.styles = logicalLineEvent.styles;
		clone.text = logicalLineEvent.text;
		logicalLineEvent = getLineStyleData(clone, lineOffset, line);
	}
	return logicalLineEvent;
}
/** 
 * Selection is not printed.
 * </p>
 * @return Point(0,0)
 * @see StyledTextRenderer#getSelection
 */
protected Point getSelection() {
	return new Point(0, 0);
}
/**
 * Returns the width of the specified text segment. 
 * Expands tabs to tab stops using the widget tab width.
 * </p>
 *
 * @param text text to measure
 * @param textStartOffset offset of the first character in text relative 
 * 	to the first character in the document
 * @param lineStyles styles of the line
 * @param paintX x location to start drawing at
 * @param gc GC to measure with
 * @return the width of the specified text segment.
 */
protected int getStyledTextWidth(String text, int textStartOffset, StyleRange[] lineStyles, int paintX, GC gc) {
	String textSegment;
	int textLength = text.length();
	int textIndex = 0;

	for (int styleIndex = 0; styleIndex < lineStyles.length; styleIndex++) {
		StyleRange style = lineStyles[styleIndex];
		int textEnd;
		int styleSegmentStart = style.start - textStartOffset;
		if (styleSegmentStart + style.length < 0) {
			continue;
		}
		if (styleSegmentStart >= textLength) {
			break;
		}
		// is there a style for the current string position?
		if (textIndex < styleSegmentStart) {
			textSegment = text.substring(textIndex, styleSegmentStart);
			setLineFont(gc, SWT.NORMAL);
			paintX += gc.stringExtent(textSegment).x;
			textIndex = styleSegmentStart;
		}
		textEnd = Math.min(textLength, styleSegmentStart + style.length);
		textSegment = text.substring(textIndex, textEnd);
		setLineFont(gc, style.fontStyle);
		paintX += gc.stringExtent(textSegment).x;
		textIndex = textEnd;
	}
	// is there unmeasured and unstyled text?
	if (textIndex < textLength) {
		textSegment = text.substring(textIndex, textLength);
		setLineFont(gc, SWT.NORMAL);
		paintX += gc.stringExtent(textSegment).x;
	}
	return paintX;
}
/**
 * Do not print the selection. Returns the styles that were passed into
 * the method without modifications.
 * <p>
 * @return the same styles that were passed into the method.
 * @see StyledTextRenderer#getSelectionLineStyles
 */
protected StyleRange[] mergeSelectionLineStyles(StyleRange[] styles) {
	return styles;
}
/**
 * Printed content is always wrapped.
 * </p>
 * @return true
 * @see StyledTextRenderer#getWordWrap
 */
protected boolean getWordWrap() {
	return true;
}
/**
 * Selection is not printed. Returns false.
 * <p>
 * @return false
 * @see StyledTextRenderer#isFullLineSelection
 */
protected boolean isFullLineSelection() {
	return false;
}
}

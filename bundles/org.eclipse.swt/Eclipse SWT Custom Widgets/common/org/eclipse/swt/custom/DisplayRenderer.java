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
package org.eclipse.swt.custom;


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.StyledText.*;
import org.eclipse.swt.graphics.*;

/**
 * A DisplayRenderer renders the content of a StyledText widget on 
 * a display device.
 */
class DisplayRenderer extends StyledTextRenderer {
	private StyledText parent;			// used to get content and styles during rendering
	
/**
 * Creates an instance of <class>DisplayRenderer</class>.
 * </p>
 * @param device Device to render on
 * @param regularFont Font to use for regular (non-bold) text
 * @param isBidi true=bidi platform, false=no bidi platform
 * @param leftMargin margin to the left of the text
 * @param parent <class>StyledText</class> widget to render
 * @param tabLength length in characters of a tab character
 */
DisplayRenderer(Device device, Font regularFont, boolean isBidi, int leftMargin, StyledText parent, int tabLength) {
	super(device, regularFont, isBidi, leftMargin);
	this.parent = parent;
	calculateLineHeight();
	setTabLength(tabLength);
}
/**
 * Dispose the specified GC.
 * </p>
 * @param gc GC to dispose.
 */
protected void disposeGC(GC gc) {
	gc.dispose();
}
/** 
 * Draws the background of the line selection.
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
protected void drawLineSelectionBackground(String line, int lineOffset, StyleRange[] styles, int paintY, GC gc, StyledTextBidi bidi) {
	Point selection = parent.internalGetSelection();
	LineCache lineCache = parent.internalGetLineCache();
	StyledTextContent content = getContent();
	int lineLength = line.length();
	int paintX;
	int selectionBackgroundWidth = -1;
	int selectionStart = Math.max(0, selection.x - lineOffset);
	int selectionEnd = selection.y - lineOffset;
	int selectionLength = selectionEnd - selectionStart;
	int horizontalScrollOffset = parent.internalGetHorizontalPixel();
	int leftMargin = getLeftMargin();
	int lineEndSpaceWidth = getLineEndSpaceWidth();
	int lineHeight = getLineHeight();
	boolean wordWrap = parent.internalGetWordWrap();
	boolean isRightOriented = (parent.getStyle() & SWT.MIRRORED) != 0;
	
	if (selectionEnd == selectionStart || selectionEnd < 0 || selectionStart > lineLength) {
		return;
	}
	if (bidi != null) {
		paintX = parent.getBidiTextPosition(line, selectionStart, bidi);
	}
	else {
		paintX = getTextPosition(line, lineOffset, selectionStart, filterLineStyles(styles), gc);
	}
	// selection extends past end of line?
	if (selectionEnd > lineLength) {
		if ((parent.getStyle() & SWT.FULL_SELECTION) != 0) {
			// use the greater of the client area width and the content 
			// width. fixes 1G8IYRD
			selectionBackgroundWidth = Math.max(getClientArea().width, lineCache.getWidth());
		}
		else {
			selectionLength = lineLength - selectionStart;
		}
	}
	gc.setBackground(parent.getSelectionBackground());
	gc.setForeground(parent.getSelectionForeground());
	if (selectionBackgroundWidth == -1) {
		boolean isWrappedLine = false;

		if (wordWrap) {
			int lineEnd = lineOffset + lineLength;
			int lineIndex = content.getLineAtOffset(lineEnd);

			// is the start offset of the next line the same as the end 
			// offset of this line?			
			if (lineIndex < content.getLineCount() - 1 &&
				content.getOffsetAtLine(lineIndex + 1) == lineEnd) {
				isWrappedLine = true;
			}
		}
		if (bidi != null) {
			selectionBackgroundWidth = parent.getBidiTextPosition(line, selectionStart + selectionLength, bidi) - paintX;
		}
		else {
			selectionBackgroundWidth = getTextWidth(line, lineOffset, selectionStart, selectionLength, styles, paintX, gc);
		}
		if (selectionBackgroundWidth < 0) {
			// width can be negative when in R2L bidi segment
			paintX += selectionBackgroundWidth;
			selectionBackgroundWidth *= -1;
		}
		if (selectionEnd > lineLength && isWrappedLine == false) {
			selectionEnd = selectionStart + selectionLength;
			// if the selection extends past this line, render an additional 
			// whitespace background at the end of the line to represent the 
			// selected line break
			if (bidi != null && selectionEnd > 0 && (bidi.isRightToLeft(selectionEnd - 1) || (isRightOriented && bidi.isRightToLeft(selectionEnd - 1) == false))) {
				int lineEndX = bidi.getTextWidth();
				gc.fillRectangle(lineEndX - horizontalScrollOffset + leftMargin, paintY, lineEndSpaceWidth, lineHeight);
			}
			else {
				selectionBackgroundWidth += lineEndSpaceWidth;
			}
		}
	}	
	// handle empty line case
	if (bidi != null && paintX == 0) {
		paintX = StyledText.XINSET;	
	}
	// fill the background first since expanded tabs are not 
	// drawn as spaces. tabs just move the draw position. 
	gc.fillRectangle(paintX - horizontalScrollOffset + leftMargin, paintY, selectionBackgroundWidth, lineHeight);
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
protected int[] getBidiSegments(int lineOffset, String lineText) {
	return parent.getBidiSegments(lineOffset, lineText);
}
/**
 * Returns the visible client area that can be used for rendering.
 * </p>
 * @return the visible client area that can be used for rendering.
 */
protected Rectangle getClientArea() {
	return parent.getClientArea();
}
/**
 * Returns the <class>StyledTextContent</class> to use for line offset
 * calculations.
 * </p>
 * @return the <class>StyledTextContent</class> to use for line offset
 * calculations.
 */
protected StyledTextContent getContent() {
	return parent.internalGetContent();
}
/**
 * Returns a new GC to use for rendering and measuring.
 * When the GC is no longer used it needs to be disposed by 
 * calling disposeGC.
 * </p>
 * @return the GC to use for rendering and measuring.
 * @see disposeGC
 */
protected GC getGC() {
	return new GC(parent);
}
/**
 * Returns the horizontal scroll position.
 * </p>
 * @return the horizontal scroll position.
 */
protected int getHorizontalPixel() {
	return parent.internalGetHorizontalPixel();
}
/**
 * @see StyledTextRenderer#getLineBackgroundData
 */
protected StyledTextEvent getLineBackgroundData(int lineOffset, String line) {
	return parent.getLineBackgroundData(lineOffset, line);
}
/**
 * @see StyledTextRenderer#getLineStyleData
 */
protected StyledTextEvent getLineStyleData(int lineOffset, String line) {
	StyledTextEvent logicalLineEvent = parent.getLineStyleData(lineOffset, line);
	
	if (logicalLineEvent != null) {
		logicalLineEvent = getLineStyleData(logicalLineEvent, lineOffset, line);
	}
	return logicalLineEvent;
}
/**
 * @see StyledTextRenderer#getSelection
 */
protected Point getSelection() {
	return parent.internalGetSelection();
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
protected  int getStyledTextWidth(String text, int textStartOffset, StyleRange[] lineStyles, int paintX, GC gc) {
	String textSegment;
	int textLength = text.length();
	int textIndex = 0;
	GC boldGC = null;
	GC normalGC = null;
	int fontStyle = getCurrentFontStyle();
	
	// Use two gcs for performance reasons (i.e., minimize number of times setFont gets called).
	if (fontStyle == SWT.NORMAL) normalGC = gc;
	else boldGC = gc;
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
			if (normalGC == null) {
				normalGC = getGC();
				normalGC.setFont(regularFont);
			}
			paintX += normalGC.stringExtent(textSegment).x;
			textIndex = styleSegmentStart;
		}
		textEnd = Math.min(textLength, styleSegmentStart + style.length);
		textSegment = text.substring(textIndex, textEnd);
		if (style.fontStyle == SWT.NORMAL) {
			if (normalGC == null) {
				normalGC = getGC();
				normalGC.setFont(regularFont);
			}
			paintX += normalGC.stringExtent(textSegment).x;
		} else {
			if (boldGC == null) {
				boldGC = getGC();
				boldGC.setFont(boldFont);
			}
			paintX += boldGC.stringExtent(textSegment).x;
		} 
		textIndex = textEnd;
	}
	// is there unmeasured and unstyled text?
	if (textIndex < textLength) {
		textSegment = text.substring(textIndex, textLength);
		if (normalGC == null) {
			normalGC = getGC();
			normalGC.setFont(regularFont);
		}
		paintX += normalGC.stringExtent(textSegment).x;
	}
	if (fontStyle == SWT.NORMAL) {
		if (boldGC != null) disposeGC(boldGC);
	} else {
		if (normalGC != null) disposeGC(normalGC);
	}
	return paintX;
}
/**
 * @see StyledTextRenderer#getSelectionLineStyles
 */
/*
Pseudo code for getSelectionLineStyles
	for each style {
		if (style ends before selection start) {
			add style to list
		}
		else
		if (style overlaps selection start (i.e., starts before selection start, ends after selection start) {
			change style end
			create new selection style with same font style starting at selection start ending at style end
			add selection style
			// does style extend beyond selection?
			if (selection style end > selection end) {
				selection style end = selection end
				// preserve rest (unselected part) of old style
				style start = selection end
				style length = old style end - selection end
				add style
			}
		}
		else
		if (style starts within selection) {
			if (no selection style created) {
				create selection style with regular font style, starting at selection start, ending at style start
				add selection style				
				if (style start == selection start) {
					set selection style font to style font
				}
			}
			// gap between current selection style end and new style start?
			if (style start > selection styke end && selection style font style != NORMAL) {
				create selection style with regular font style, starting at selection style end, ending at style start
				add selection style
			}
			if (selection style font != style font) {
				selection style end = style start
				add selection style
				create selection style with style font style, starting at style start, ending at style end
			}
			else {
				selection style end = style end
			}
			// does style extend beyond selection?			
			if (selection style end > selection end) {
				selection style end = selection end
				// preserve rest (unselected part) of old style
				style start = selection end
				style length = old style end - selection end
				style start = selection end
				add style
			}
		}
		else {
			if (no selection style created) {
				create selection style with regular font style, starting at selection start, ending at selection end
				add selection style
			}
			else
			if (selection style end < selection end) {
				if (selection style font style != NORMAL) {
					create selection style with regular font style, starting at selection style end, ending at selection end					
					add selection style
				}
				else {
					selection style end = selection end
				}
			}
			add style
		}									
	}
	if (no selection style created) {
		create selection style with regular font style, starting at selection start, ending at selection end
		add selection style to list
	}
	else
	if (selection style end < selection end) {
		if (selection style font style != NORMAL) {
			create selection style with regular font style, starting at selection style end, ending at selection end					
			add selection style
		}
		else {
			selection style end = selection end
		}
	}
*/
protected StyleRange[] mergeSelectionLineStyles(StyleRange[] styles) {
	Point selection = parent.internalGetSelection();	
	int selectionStart = selection.x;
	int selectionEnd = selection.y;
	Vector newStyles = new Vector(styles.length);	
	StyleRange selectionStyle = null;
	Color foreground = parent.getSelectionForeground();
	Color background = parent.getSelectionBackground();

	// potential optimization: ignore styles if there is no bold style and the entire line is selected
	for (int i = 0; i < styles.length; i++) {
		StyleRange style = styles[i];
		int styleEnd = style.start + style.length;
		
		if (styleEnd <= selectionStart) {
			newStyles.addElement(style);
		}
		else // style overlaps selection start? (i.e., starts before selection start, ends after selection start
		if (style.start < selectionStart && styleEnd > selectionStart) {
			StyleRange newStyle = (StyleRange) style.clone();
			newStyle.length -= styleEnd - selectionStart;
			newStyles.addElement(newStyle);
			// create new selection style with same font style starting at selection start ending at style end
			selectionStyle = new StyleRange(selectionStart, styleEnd - selectionStart, foreground, background, newStyle.fontStyle);
			newStyles.addElement(selectionStyle);
			// if style extends beyond selection a new style is returned for the unselected part of the style
			newStyle = setSelectionStyleEnd(selectionStyle, style);
			if (newStyle != null) {
				newStyles.addElement(newStyle);					
			}				
		}
		else // style starts within selection?
		if (style.start >= selectionStart && style.start < selectionEnd) {
			StyleRange newStyle;
			int selectionStyleEnd;
			// no selection style created yet?
			if (selectionStyle == null) {
				// create selection style with regular font style, starting at selection start, ending at style start
				selectionStyle = new StyleRange(selectionStart, style.start - selectionStart, foreground, background);
				newStyles.addElement(selectionStyle);
				if (style.start == selectionStart) {
					selectionStyle.fontStyle = style.fontStyle;
				}
			}
			selectionStyleEnd = selectionStyle.start + selectionStyle.length;
			// gap between current selection style end and style start?
			if (style.start > selectionStyleEnd && selectionStyle.fontStyle != SWT.NORMAL) {
				// create selection style with regular font style, starting at selection style end, ending at style start
				selectionStyle = new StyleRange(selectionStyleEnd, style.start - selectionStyleEnd, foreground, background);
				newStyles.addElement(selectionStyle);
			}
			if (selectionStyle.fontStyle != style.fontStyle) {
				// selection style end = style start
				selectionStyle.length = style.start - selectionStyle.start;
				// create selection style with style font style, starting at style start, ending at style end
				selectionStyle = new StyleRange(style.start, style.length, foreground, background, style.fontStyle);
				newStyles.addElement(selectionStyle);
			}
			else {
				// selection style end = style end
				selectionStyle.length = styleEnd - selectionStyle.start;
			}
			// if style extends beyond selection a new style is returned for the unselected part of the style
			newStyle = setSelectionStyleEnd(selectionStyle, style);
			if (newStyle != null) {
				newStyles.addElement(newStyle);					
			}				
		}
		else {
			// no selection style created yet?
			if (selectionStyle == null) {
				// create selection style with regular font style, starting at selection start, ending at selection end
				selectionStyle = new StyleRange(selectionStart, selectionEnd - selectionStart, foreground, background);
				newStyles.addElement(selectionStyle);
			}
			else // does the current selection style end before the selection end?
			if (selectionStyle.start + selectionStyle.length < selectionEnd) {
				if (selectionStyle.fontStyle != SWT.NORMAL) {
					int selectionStyleEnd = selectionStyle.start + selectionStyle.length;
					// create selection style with regular font style, starting at selection style end, ending at selection end
					selectionStyle = new StyleRange(selectionStyleEnd, selectionEnd - selectionStyleEnd, foreground, background);
					newStyles.addElement(selectionStyle);
				}
				else {
					selectionStyle.length = selectionEnd - selectionStyle.start;
				}
			}
			newStyles.addElement(style);
		}
	}
	if (selectionStyle == null) {
		// create selection style with regular font style, starting at selection start, ending at selection end
		selectionStyle = new StyleRange(selectionStart, selectionEnd - selectionStart, foreground, background);
		newStyles.addElement(selectionStyle);
	}
	else // does the current selection style end before the selection end?
	if (selectionStyle.start + selectionStyle.length < selectionEnd) {
		if (selectionStyle.fontStyle != SWT.NORMAL) {
			int selectionStyleEnd = selectionStyle.start + selectionStyle.length;
			// create selection style with regular font style, starting at selection style end, ending at selection end
			selectionStyle = new StyleRange(selectionStyleEnd, selectionEnd - selectionStyleEnd, foreground, background);
			newStyles.addElement(selectionStyle);
		}
		else {
			selectionStyle.length = selectionEnd - selectionStyle.start;
		}
	}
	styles = new StyleRange[newStyles.size()];
	newStyles.copyInto(styles);
	return styles;
}
/**
 * @see StyledTextRenderer#getWordWrap
 */
protected boolean getWordWrap() {
	return parent.getWordWrap();
}
/**
 * @see StyledTextRenderer#isFullLineSelection
 */
protected boolean isFullLineSelection() {
	return (parent.getStyle() & SWT.FULL_SELECTION) != 0;
}
/**
 * Ensures that the selection style ends at the selection end.
 * <code>selectionStyle</code> is assumed to be created based on the style 
 * range of <code>style</code>. If <code>selectionStyle</code> does extend
 * beyond the selection range a new style is returned to preserve the style
 * passed in with <code>style</code>.
 * <p>
 * @param selectionStyle the selection style based on the style range in 
 * 	<code>style</code>
 * @param style the existing style that is to be merged with the selection
 * @return a new style that preserves the style passed in with <code>style</code>
 * 	if the selection does not fully extend over the existing style range.
 *  null otherwise.
 */
private StyleRange setSelectionStyleEnd(StyleRange selectionStyle, StyleRange style) {
	int selectionEnd = parent.internalGetSelection().y;
	StyleRange newStyle = null;
	
	// does style extend beyond selection?				
	if (selectionStyle.start + selectionStyle.length > selectionEnd) {
		int styleEnd = style.start + style.length;	
		selectionStyle.length = selectionEnd - selectionStyle.start;
		// preserve rest (unselected part) of old style					
		newStyle = (StyleRange) style.clone();
		newStyle.start = selectionEnd;
		newStyle.length = styleEnd - selectionEnd;
	}
	return newStyle;				
}
}

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

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.Compatibility;
import java.util.Vector;

class DefaultLineStyler implements LineStyleListener, LineBackgroundListener {
	StyledTextContent content;
	StyleRange styles[] = new StyleRange[0];
	int styleCount = 0;	// the number of styles	
	int styleExpandExp = 1; // the expansion exponent, used to increase the styles array exponentially
	int lineExpandExp = 1; 	// the expansion exponent, used to increase the lines array exponentially
	int lineCount = 0;
	Color lineBackgrounds[];
	
/** 
 * Creates a new default line styler.
 * <p>
 *
 * @param content the text to which the styles apply
 */
public DefaultLineStyler(StyledTextContent content) {
	this.content = content;
	lineCount = content.getLineCount();
	lineBackgrounds = new Color[lineCount];
}	
/** 
 * Inserts a style at the given location.
 * <p>
 *
 * @param style	the new style
 * @param index	the index at which to insert the style (the new style
 * 	will reside at this index)
 *
 */
void insertStyle(StyleRange style, int index) {
	insertStyles(new StyleRange[] {style}, index);
}
/** 
 * Insert the styles at the given location.
 * <p>
 *
 * @param insertStyles	the new styles
 * @param index	the index at which to insert the styles (the first new style
 * 	will reside at this index)
 *
 */
void insertStyles(StyleRange[] insertStyles, int index) {
	int size = styles.length;
	int insertCount = insertStyles.length;
	int spaceNeeded = styleCount + insertCount - size;
	if (spaceNeeded > 0) {
		StyleRange[] newStyles = new StyleRange[size+spaceNeeded];
		System.arraycopy(styles, 0, newStyles, 0, size);
		styles = newStyles;
	}
	// shift the styles down to make room for the new styles
	System.arraycopy(styles, index, styles, index + insertCount, styleCount - index);
	// add the new styles
	System.arraycopy(insertStyles, 0, styles, index, insertCount);
	styleCount = styleCount + insertCount;
}
/** 
 * Inserts a style, merging it with adjacent styles if possible.
 * <p>
 *
 * @param style	the new style
 * @param index	the index at which to insert the style (the new style
 * 	will reside at this index)
 * @return true if the style was inserted, false if the style was merged with an adjacent 
 * 	style
 */
boolean insertMergeStyle(StyleRange style, int index) {
	if (mergeStyleBefore(style, index)) return false;
	if (mergeStyleAfter(style, index)) return false;
	insertStyle(style, index);
	return true;
}
/** 
 * Merges the style with the style before it if possible.
 * <p>
 *
 * @param style	the new style
 * @param index	the index at which to attempt the merge.
 * @return true if the style was merged, false otherwise
 */
boolean mergeStyleBefore(StyleRange style, int index) {
	// see if the style is similar to the style before it and merge the
	// styles if possible
	if (index > 0) {
		StyleRange previous = styles[index-1];
		if (style.similarTo(previous)) {
			// the start of style needs to be in the range of the previous style
			// and the end of style needs to be < the start of the next style
			int previousEnd = previous.start + previous.length;
			if ((style.start <= previousEnd) && (style.start >= previous.start)) {
				int styleEnd = style.start + style.length;
				if ((index == styleCount) || (styleEnd <= styles[index].start)) {
					previous.length = style.start + style.length - previous.start;
					return true;
				}
			}
		}
	}
	return false;
}
/** 
 * Merges the style with the style after it if possible.
 * <p>
 *
 * @param style	the new style
 * @param index	the index at which to attempt the merge.
 * @return true if the style was merged, false otherwise
 */
boolean mergeStyleAfter(StyleRange style, int index) {
	// see if the style is similar to the style that will be after it and 
	// merge the styles if possible
	if (index < styleCount) {
		StyleRange next = styles[index];
		if (style.similarTo(next)) {
			// the end of style needs to be in the range of the next style and
			// the start of style needs to be > the end of the previous style
			int styleEnd = style.start + style.length;
			int nextEnd = next.start + next.length;
			if ((styleEnd <= nextEnd) && (styleEnd >= next.start)) {
				if ((index == 0) || (style.start >= styles[index-1].start + styles[index-1].length)) {
					next.length = next.start + next.length - style.start;
					next.start = style.start;
					return true;
				}
			}
		}
	}
	return false;
}
/** 
 * Removes style information that is defined for the range of text in <code>clearStyle</code>.
 * <p>
 *
 * @param clearStyle the style information to use for clearing 
 */ 
void clearStyle(StyleRange clearStyle) {
	Point pt = getOverlappingStyles(clearStyle.start, clearStyle.length);
	int clearStyleEnd = clearStyle.start + clearStyle.length - 1;
	
	// no overlapped styles exist
	if ((pt == null) || (pt.y == 0)) return; 

	// the newStyle overlaps one or more of the existing styles
	// pt.x is the index of the first overlapped style, pt.y is the number of overlapped
	// styles
	int count = 0;
	int deleteStyle = -1;
	int deleteCount = 0;
	for (int i=pt.x; count<pt.y; i++) {
		StyleRange overlap = styles[i];
		int overlapEnd = overlap.start + overlap.length - 1;
		if (overlap.start < clearStyle.start) {
			if (overlapEnd <= clearStyleEnd) {
				// the end of overlap needs to be cleared
				overlap.length=clearStyle.start - overlap.start;
			} else {
				// middle of overlap needs to be cleared, this will
				// cause overlap to be broken into two
				StyleRange endStyle = (StyleRange)overlap.clone();
				endStyle.start = clearStyleEnd + 1;
				endStyle.length = overlapEnd - clearStyleEnd;
				overlap.length = clearStyle.start - overlap.start;
				insertStyle(endStyle, i+1);
				break;
			}
		} else {
			if (overlapEnd <= clearStyleEnd) {	
				// entire overlap needs to be cleared
				if (deleteStyle == -1) {
					deleteStyle = i;
				} 
				deleteCount++;
			} else {
				// beginning of overlap needs to be cleared
				overlap.start=clearStyleEnd + 1;
				overlap.length=overlapEnd - overlap.start + 1;
				break;
			}
		}				
		count++;
	}
	deleteStyles(deleteStyle, deleteCount);
}
/**
 * Increases the <code>linebackgrounds</code> array to accomodate new line background
 * information.
 * <p>
 *
 * @param numLines the number to increase the array by
 */
void expandLinesBy(int numLines) {
	int size = lineBackgrounds.length;
	if (size - lineCount >= numLines) {
		return;
	}
	Color[] newLines = new Color[size+Math.max(Compatibility.pow2(lineExpandExp), numLines)];
	System.arraycopy(lineBackgrounds, 0, newLines, 0, size);
	lineBackgrounds = newLines;
	lineExpandExp++;
}
/** 
 * Deletes the style at <code>index</code>.
 * <p>
 *
 * @param index	the index of the style to be deleted
 */
void deleteStyle(int index) {
	deleteStyles(index, 1);
}
/** 
 * Delete count styles starting at <code>index</code>.
 * <p>
 *
 * @param index	the index of the style to be deleted
 * @param count	the number of styles to be deleted
 */
void deleteStyles(int index, int count) {
	if ((count == 0) || (index < 0)) return;
	// shift the styles up 
	System.arraycopy(styles, index + count, styles, index, styleCount - (index + count));
	for (int i=0; i<count; i++) {
		styles[styleCount - i - 1] = null;
	}
	styleCount = styleCount - count;
}
/** 
 * Returns the styles that are defined.
 * <p>
 *
 * @return the copied array of styles
 */
StyleRange [] getStyleRanges() {
	StyleRange[] newStyles = new StyleRange[styleCount];
	System.arraycopy(styles, 0, newStyles, 0, styleCount);
	return newStyles;
}
/**
 * Handles the get line background color callback.
 * <p>
 *
 * @param event.lineOffset line number (input)	
 * @param event.lineText line text (input)
 * @param event.background line background color (output)
 */
public void lineGetBackground(LineBackgroundEvent event) {
	int lineIndex = content.getLineAtOffset(event.lineOffset);
	event.lineBackground = lineBackgrounds[lineIndex];
}
/**
 * Handles the get line style information callback.
 * <p>
 *
 * @param event.lineOffset line number (input)	
 * @param event.lineText line text (input)
 * @param event.styles array of StyleRanges, need to be in order (output)
 */
public void lineGetStyle(LineStyleEvent event) {
	int lineStart = event.lineOffset;
	int lineEnd = lineStart + event.lineText.length();

	int high = searchForStyle(lineStart, lineEnd);
	StyleRange style = null;
	Vector lineStyles = new Vector();

	// index will represent a style that 
	// -- starts after the line (end processing)
	// -- ends before the line (continue processing)
	// -- starts before the line, ends in the line (add range)
	// -- starts in the line, ends in the line (add range)
	// -- starts in the line, ends after the line (add range)
	// -- starts before the line, ends after the line (add range)
	for (int index = high; index < styleCount; index++) {
		style = styles[index];
		if (style.start > lineEnd)
			// style starts after the line, end looping 
			break;
		int styleEnd = style.start + style.length - 1;
		if (styleEnd >= lineStart) lineStyles.addElement(style);
	}
	event.styles = new StyleRange[lineStyles.size()];
	lineStyles.copyInto(event.styles);
}
/** 
 * Searches for the first style in the <code>start</code> - <code>end</code> range.
 * <p>
 *
 * @return the index of the first style that overlaps the input range  
 */
int searchForStyle(int start, int end) {
	int high = styleCount;
	int low = -1;
	int index = high;
	// find the index of the first style for the given range, use a binary search
	while (high - low > 1) {
		index = (high + low) / 2;
		StyleRange style = styles[index];
		int styleEnd = style.start + style.length - 1;
		if (start <= style.start || end <= styleEnd || (start > style.start && styleEnd >= start && styleEnd < end)) {
			high = index;			
		}
		else {
			low = index;
		}
	}
	return high;
}
/** 
 * Updates the line background colors to reflect a new color.  Called by StyledText.
 * <p>
 *
 * @param startLine index of the first line to color
 * @param lineCount number of lines to color starting at startLine
 * @param background the background color for the lines
 */ 
void setLineBackground(int startLine, int count, Color background) {
	for (int i=startLine; i<startLine + count; i++) {
		lineBackgrounds[i]=background;
	}
}
/** 
 * Update the styles to reflect the new style.  <code>newStyle</code> will 
 * replace any old style for the range.  When this method is called, the 
 * DefaultLineStyler may merge the new style with an existing style (if possible).
 * Called by StyledText when a style is added.  Called by StyledText.
 * <p>
 *
 * @param newStyle the new style information.
 */ 
void setStyleRange(StyleRange newStyle) {
	if (newStyle == null) {
		styles = new StyleRange[0];
		styleExpandExp = 1;
		styleCount = 0;
		return;
	}
	if (newStyle.length ==0) return;
	if (newStyle.isUnstyled()) {
		clearStyle(newStyle);
		return;
	}
	
	Point pt = getOverlappingStyles(newStyle.start, newStyle.length);
	int newStyleEnd = newStyle.start + newStyle.length - 1;
	
	// no styles exist
	if (pt == null) {
		insertStyle(newStyle, 0);
		return;
	}
	
	// newStyle does not overlap any other styles
	if (pt.y == 0) {
		insertMergeStyle(newStyle, pt.x);
		return;
	} 

	// the newStyle overlaps one or more of the existing styles
	boolean added = false; // indicates whether or not the new style has been added
	int count = 0;
	// pt.x is the index of the first overlapped style, pt.y is the number of overlapped
	// styles
	for (int i=pt.x; count<pt.y; i++) {
		StyleRange overlap = styles[i];
		int overlapEnd = overlap.start + overlap.length - 1;
		if (overlap.start < newStyle.start) {
			if (overlapEnd <= newStyleEnd) {
				// the end of overlap needs to be replaced by newStyle
				if (newStyle.similarTo(overlap)) {
					// update overlap to accomodate the new style
					overlap.length = newStyle.start + newStyle.length - overlap.start;
				} else {
					overlap.length=newStyle.start - overlap.start;
					// see if newStyle can be merged with the style after overlap, if so,
					// processing is done
					if (mergeStyleAfter(newStyle, i+1)) break;
					// otherwise, insert the newStyle, newStyle may still overlap other
					// styles after it so continue processing	
					insertStyle(newStyle, i+1);
					i++;
				}
				added = true;
			} else {
				// middle of overlap needs to be replaced by newStyle, this will
				// cause overlap to be broken into two
				if (newStyle.similarTo(overlap)) break;
				StyleRange endStyle = (StyleRange)overlap.clone();
				endStyle.start = newStyleEnd + 1;
				endStyle.length = overlapEnd - newStyleEnd;
				overlap.length = newStyle.start - overlap.start;
				insertStyle(newStyle, i+1);
				i++;
				insertStyle(endStyle, i+1);
				// when newStyle overlaps the middle of a style, this implies that
				// processing is done (no more overlapped styles)
				break;
			}
		} else {
			if (overlapEnd <= newStyleEnd) {	
				// overlap will be replaced by the newStyle, make sure newStyle
				// hasn't already been added, if it has just delete overlap
				if (!added) {
					styles[i] = newStyle;
					added = true;
				} else {
					deleteStyle(i);
					i--;
				}
			} else {
				// beginning of overlap needs to be replaced by newStyle
				overlap.start=newStyleEnd + 1;
				overlap.length=overlapEnd - overlap.start + 1;
				if (!added) {
					insertMergeStyle(newStyle, i);
				}
				// when newStyle overlaps only the beginning of a style, this implies 
				// that processing is done (no more overlapped styles)
				break;
			}
		}				
	count++;
	}
}
/** 
 * Replace the styles for the given range.
 * <p>
 *
 * @param styles the new styles, must be in order and non-overlapping
 */
void replaceStyleRanges(int start, int length, StyleRange[] ranges) {
	clearStyle(new StyleRange(start, length, null, null));
	// find insert point
	int high = styleCount;
	int low = -1;
	int index = high;
	while (high - low > 1) {
		index = (high + low) / 2;
		StyleRange style = styles[index];
		if (start <= style.start) {
			high = index;			
		}
		else {
			low = index;
		}
	}
	insertStyles(ranges, high);
}
/** 
 * Sets the array of styles and discards old styles.  Called by StyledText.
 * <p>
 *
 * @param styles the new styles, must be in order and non-overlapping
 */
void setStyleRanges(StyleRange[] styles) {
	this.styles = new StyleRange[styles.length];
	System.arraycopy(styles, 0, this.styles, 0, styles.length);
	styleCount = styles.length;
	styleExpandExp = 1;
}
/** 
 * Updates the style ranges and line backgrounds to reflect a pending text 
 * change.
 * Called by StyledText when a TextChangingEvent is received.
 * <p>
 *
 * @param event	the event with the text change information
 */  
public void textChanging(TextChangingEvent event) {
	int startLine = content.getLineAtOffset(event.start);
	int startLineOffset = content.getOffsetAtLine(startLine);
	
	textChanging(event.start, -event.replaceCharCount);
	textChanging(event.start, event.newCharCount);
	
	if (event.replaceCharCount == content.getCharCount()) {
		// all text is going to be replaced, clear line backgrounds
		linesChanging(0, -lineCount);
		linesChanging(0, content.getLineCount() - event.replaceLineCount + event.newLineCount);
		return;
	}

	if (event.start != startLineOffset) {
		startLine = startLine + 1;
	}
			
	linesChanging(startLine, -event.replaceLineCount);
	linesChanging(startLine, event.newLineCount);
}
/*
 * Updates the line backgrounds to reflect a pending text change.
 * <p>
 *
 * @param start	the starting line of the change that is about to take place
 * @param delta	the number of lines in the change, > 0 indicates lines inserted,
 * 	< 0 indicates lines deleted
 */
void linesChanging(int start, int delta) {
	if (delta == 0) return;
	boolean inserting = delta > 0;
	if (inserting) {
		// shift the lines down to make room for new lines
		expandLinesBy(delta);
		for (int i = lineCount-1; i >= start; i--) {
			lineBackgrounds[i + delta]=lineBackgrounds[i];
		}
		for (int i=start; i<start + delta; i++) {
			lineBackgrounds[i]=null;
		}
	} else {
		// shift up the lines
		for (int i = start - delta; i < lineCount; i++) {
			lineBackgrounds[i+delta]=lineBackgrounds[i];
		}
	}
	lineCount += delta;
}
/*
 * Updates the style ranges to reflect a text change.
 * <p>
 *
 * @param start	the starting offset of the change that is about to 
 *	take place
 * @param delta	the length of the change, > 0 indicates text inserted,
 * 	< 0 indicates text deleted
 */
void textChanging(int start, int delta) {
	if (delta == 0) return;
	StyleRange style;
	// find the index of the first style for the given offset, use a binary search
	// to find the index
	int end;
	int deleteStart = -1;
	int deleteCount = 0;
	boolean inserting = delta > 0;
	if (inserting) {
		end = (start + delta) - 1;
	} else {
		end = (start - delta) - 1;
	}
	int high = searchForStyle(start, end);
	int index;
	// update the styles that are in the affected range
	for (index = high; index < styleCount; index++) {
		style = styles[index];
		if (inserting) {
			if (style.start >= start) break;
			// in the insert case only one style range will be directly affected,
			// it will need to be split into two and then the newStyle inserted
			StyleRange beforeStyle = (StyleRange)style.clone();
			beforeStyle.length = start - style.start;
			style.start = start;
			style.length = style.length - beforeStyle.length;
			if (beforeStyle.length != 0) insertStyle(beforeStyle, index);
			index++;
			break;
		} else {
			int styleEnd = style.start + style.length - 1;
			if (style.start > end) break;
			// in the delete case, any style that overlaps the change range will be
			// affected
			if (style.start < start) {
				if (styleEnd <= end) {
					// style starts before change range, ends in change range
					style.length = start - style.start;
				} else {
					// style starts before change range, ends after change range
					style.length = style.length + delta;
					index++;
					break;
				}
			} else {
				if (styleEnd <= end) {
					// style starts in change range, ends in change range
					if (deleteStart == -1)	{
						deleteStart = index;
					} 
					deleteCount++;
				} else {
					// style starts in change range, ends after change range
					style.start = start;
					style.length = styleEnd - end;
					index++;
					break;
				}
			}	
		}
	}
	deleteStyles(deleteStart, deleteCount);
	// change the offsets of the styles after the affected styles
	for (int i = index - deleteCount; i < styleCount; i++) {
		style = styles[i];
		style.start = style.start + delta;
	}
}
/** 
 * Returns the indexes of the styles that overlap the given range.  Styles that partially
 * or fully overlap the range will be returned.
 * <p>
 *
 * @return Point where x is the index of the starting overlap style, y is the number of
 * 	styles that overlap the range
 */ 
Point getOverlappingStyles(int start, int length) {	
	StyleRange style;
	if (styleCount == 0) return null;
	// find the index of the first style for the given offset, use a binary search
	// to find the index
	int end = start + length - 1;
	int high = searchForStyle(start, end);
	int count = 0;
	for (int index = high; index < styleCount; index++) {
		style = styles[index];
		int styleEnd = style.start + style.length - 1;
		if (style.start > end) break;
		if (styleEnd >= start) count++;	
	}
	return new Point(high, count);
}
/** 
 * Returns the background color of a line.  Called by StyledText.  It is safe to return 
 * the existing Color object since the colors are set and managed by the client.
 * <p>
 *
 * @param index	the line index
 * @return the background color of the line at the given index
 */ 
Color getLineBackground(int index) {
	return lineBackgrounds[index];
}
/** 
 * Returns the style for the character at <code>offset</code>.  Called by StyledText.  
 * Returns a new style.  Does not return the existing style.
 * <p>
 *
 * @param offset the character position in the text
 * @return a cloned StyleRange with start == offset and length == 1 if a style is
 * 	specified or null if no style is specified
 */ 
StyleRange getStyleRangeAtOffset(int offset) {
	if (styleCount == 0) return null;
	Point pt = getOverlappingStyles(offset, 1);
	if (pt == null || pt.y == 0) return null;
	StyleRange newStyle = (StyleRange)styles[pt.x].clone();
	newStyle.start = offset;
	newStyle.length = 1;
	return newStyle;
}
/** 
 * Returns the styles for the given range. Returns the existing styles,
 * so be careful not to modify the return value.  Styles are not cloned
 * in order to make this method as efficient as possible. 
 * <p>
 *
 * @param offset the start position of the text range
 * @param length the length of the text range
 * @return a StyleRange array or null if no styles are specified for the text
 * 	range
 */ 
StyleRange[] getStyleRangesFor(int offset, int length) {
	if (styleCount == 0) return null;
	Point pt = getOverlappingStyles(offset, length);
	if (pt == null || pt.y == 0) return null;
	StyleRange[] ranges = new StyleRange[pt.y];
	for (int i=0; i<pt.y; i++) {
		StyleRange newStyle = styles[pt.x + i];
		ranges[i]=newStyle;
	}
	return ranges;
}
void release () {
	styles = null;
}
}

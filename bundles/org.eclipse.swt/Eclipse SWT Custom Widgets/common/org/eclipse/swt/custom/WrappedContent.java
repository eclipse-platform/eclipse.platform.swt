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


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * An instance of class <code>WrappedContent</code> is used by 
 * StyledText to display wrapped lines. Lines are wrapped at word 
 * breaks which are marked by a space character. Trailing space 
 * behind words is kept on the current line.
 * If the last remaining word on a line can not be fully displayed 
 * the line is wrapped character by character.
 * WrappedContent wraps a StyledTextContent which provides the line
 * data. The start offset and length of wrapped lines is calculated
 * and updated based on recalculation requests and text changes.
 * <p>
 * All public methods in this class implement the 
 * <code>StyledTextContent</code> interface. Package visible 
 * methods are internal API for use by <code>StyledText</code>.
 * </p>
 */
class WrappedContent implements StyledTextContent {
    final static int LINE_OFFSET = 0; 	// index of line offset in visualLines array
    final static int LINE_LENGTH = 1; 	// index of line lenght in visualLines array
	    
    StyledTextRenderer renderer;
	StyledTextContent logicalContent;
	int[][] visualLines; 				 // start and length of each visual line
	int visualLineCount = 0;

/**
 * Create a new instance.
 * 
 * @param renderer <class>StyledTextRenderer</class> that renders 
 * 	the lines wrapped by the new instance.
 * @param logicalContent StyledTextContent that provides the line 
 * 	data.
 */
WrappedContent(StyledTextRenderer renderer, StyledTextContent logicalContent) {
    this.renderer = renderer;
    this.logicalContent = logicalContent;
}
/**
 * @see StyledTextContent#addTextChangeListener(TextChangeListener)
 */
public void addTextChangeListener(TextChangeListener listener) {
    logicalContent.addTextChangeListener(listener);
}
/**
 * Grow the lines array to at least the specified size.
 * <p>
 * 
 * @param numLines number of elements that the array should have
 * 	at a minimum
 */
private void ensureSize(int numLines) {
	int size = visualLines.length;
	if (size >= numLines) {
		return;
	}
	int[][] newLines = new int[Math.max(size * 2, numLines)][2];
	System.arraycopy(visualLines, 0, newLines, 0, size);
	visualLines = newLines;
	resetVisualLines(size, visualLines.length - size);	
}
/**
 * @see StyledTextContent#getCharCount()
 */
public int getCharCount() {
    return logicalContent.getCharCount();
}
/**
 * @return the visual (wrapped) line at the specified index
 * @see StyledTextContent#getLine(int)
 */
public String getLine(int lineIndex) {
	String line;
	
	// redirect call to logical content if there are no wrapped lines
	if (visualLineCount == 0) {
		line = logicalContent.getLine(lineIndex);
	}
	else {
		if (lineIndex >= visualLineCount || lineIndex < 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}		
		line = logicalContent.getTextRange(visualLines[lineIndex][LINE_OFFSET], visualLines[lineIndex][LINE_LENGTH]);
	}
    return line;
}
/**
 * Returns the visual (wrapped) line at given offset.
 * <p>
 * The offset is ambiguous if it identifies the end of a visual line and 
 * there is another visual line below. In this case the end of the visual
 * line has the same offset as the beginning of the next visual line 
 * since the visual line break is not represented by any character in the
 * logical line.
 * In this ambiguous case the offset is assumed to represent the end of a
 * visual line and the index of the first visual line is returned.
 * </p>
 * 
 * @param offset offset of the desired line. 
 * @return the index of the visual (wrapped) line at the specified offset
 * @see StyledTextContent#getLineAtOffset(int)
 */
public int getLineAtOffset(int offset) {
	int lastLine = visualLineCount - 1;
	int lastChar;

	// redirect call to logical content if there are no wrapped lines
	if (visualLineCount == 0) {
		return logicalContent.getLineAtOffset(offset);
	}
	// can't use getCharCount to get the number of characters since this
	// method is called in textChanged, when the logicalContent used by
	// getCharCount has already changed. at that point the visual lines
	// have not been updated yet and we thus need to use the old character
	// count which is only available in the visual content.
	lastChar = visualLines[lastLine][LINE_OFFSET] + visualLines[lastLine][LINE_LENGTH];
	if (offset < 0 || (offset > 0 && offset > lastChar)) {
	    SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	// if last line and the line is not empty you can ask for 
	// a position that doesn't exist (the one to the right of the 
	// last character) - for inserting
	if (offset == lastChar) {
		return lastLine;
	}

	int high = visualLineCount;
	int low = -1;
	int index = visualLineCount;
	while (high - low > 1) {
		index = (high + low) / 2;
		int lineStart = visualLines[index][LINE_OFFSET];
		if (offset >= lineStart) {
			int lineEnd = lineStart + visualLines[index][LINE_LENGTH];
		    low = index;			
		    if (offset <= lineEnd) {
		    	break;
		    }
		} 
		else {
			high = index;
		}
	}
	if (low > 0 && offset == visualLines[low - 1][LINE_OFFSET] + visualLines[low - 1][LINE_LENGTH]) {
		// end of a visual line/beginning of next visual line is ambiguous 
		// (they have the same offset). always return the first visual line
		low--;
	}
	return low;
}
/**
 * @return the number of visual (wrapped) lines
 * @see StyledTextContent#getLineCount()
 */
public int getLineCount() {
	int lineCount = visualLineCount;
	
	// redirect call to logical content if there are no wrapped lines
	if (visualLineCount == 0) {
		lineCount = logicalContent.getLineCount();
	}
    return lineCount;
}
/**
 * @see StyledTextContent#getLineDelimiter()
 */
public String getLineDelimiter() {
    return logicalContent.getLineDelimiter();
}
/**
 * @return the start offset of the visual (wrapped) line at the given 
 * 	index
 * @see StyledTextContent#getOffsetAtLine(int)
 */
public int getOffsetAtLine(int lineIndex) {
	int offset;
	
	// redirect call to logical content if there are no wrapped lines
	if (visualLineCount == 0) {
		offset = logicalContent.getOffsetAtLine(lineIndex);
	}
	else {
		if (lineIndex >= visualLineCount || lineIndex < 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		offset = visualLines[lineIndex][LINE_OFFSET];
	}
    return offset;
}
/**
 * @see StyledTextContent#getTextRange(int, int)
 */
public String getTextRange(int start, int length) {
    return logicalContent.getTextRange(start, length);
}
/**
 * Returns the number of visual (wrapped) lines.
 * 
 * @return the number of visual (wrapped) lines
 */
int getVisualLineCount() {
	return visualLineCount;	
}
/**
 * @see StyledTextContent#removeTextChangeListener(TextChangeListener)
 */
public void removeTextChangeListener(TextChangeListener listener) {
    logicalContent.removeTextChangeListener(listener);
}
/**
 * Reset the visual (wrapped) lines in the specified range.
 * If the range specifies partial logical lines (e.g., startLine is
 * the second of two visual lines) it is extended to reset all visual 
 * lines of a logical line.
 * Following the reset the logical lines in the reset visual range are 
 * rewrapped.
 * <p>
 * 
 * @param startLine index of the first visual line 
 * @param lineCount number of visual lines
 */
void reset(int startLine, int lineCount) {
    if (lineCount <= 0 || visualLineCount == 0) {
        return;
	}       
	reset(startLine, lineCount, true);
}
/**
 * Reset the visual (wrapped) lines in the specified range.
 * If the range specifies partial logical lines (e.g., startLine is
 * the second of two visual lines) it is extended to reset all visual 
 * lines of a logical line.
 * <p>
 * 
 * @param startLine index of the first visual line 
 * @param lineCount number of visual lines
 * @param wrap true=rewrap the logical lines in the reset visual range 
 * 	false=don't rewrap lines. Visual lines will be left in an inconsistent
 * 	state since there will be a range of unwrapped and unknown lines.
 * @return the first visual line that was reset
 */
private int reset(int startLine, int lineCount, boolean wrap) {
    if (lineCount <= 0) {
        return startLine;
	}       
    // make sure that all visual lines of the first logical line are 
    // being reset. visualFirstLine is the first visual line of the 
    // first logical line that has at least one visual line reset.
	int visualFirstLineOffset = getOffsetAtLine(startLine);
    int logicalFirstLine = logicalContent.getLineAtOffset(visualFirstLineOffset);
    int logicalFirstLineOffset = logicalContent.getOffsetAtLine(logicalFirstLine);
    int visualFirstLine = getLineAtOffset(logicalFirstLineOffset);

    lineCount += startLine - visualFirstLine;
    startLine = visualFirstLine;
        
    // make sure that all visual lines of the last logical line are 
    // being reset.
	int lastLine = startLine + lineCount - 1;
    int lastLineEnd = visualLines[lastLine][LINE_OFFSET] + visualLines[lastLine][LINE_LENGTH];
    int logicalEndLine = 0;
        
    while (lastLine < visualLineCount - 1 && lastLineEnd == visualLines[lastLine + 1][LINE_OFFSET]) {
    	lastLine++;
    	lastLineEnd = visualLines[lastLine][LINE_OFFSET] + visualLines[lastLine][LINE_LENGTH];
    }
    if (wrap) {
		if (lastLine == visualLineCount - 1) {
			logicalEndLine = logicalContent.getLineCount();
		}
		else {
			logicalEndLine = logicalContent.getLineAtOffset(visualLines[lastLine + 1][LINE_OFFSET]);
		}
    }
    lineCount = lastLine - startLine + 1;
	resetVisualLines(startLine, lineCount);
	visualLineCount -= lineCount;
	if (wrap) {
	    // always recalculate line wrap immediately after a reset 
	    // because the content always needs to be in a usable state.
	    // i.e., there must not be any reset but unwrapped lines
	    wrapLineRange(logicalFirstLine, logicalEndLine, startLine);
	}
	return startLine;
}
/**
 * Reset the visual (wrapped) lines in the specified range.
 * <p>
 * 
 * @param startLine index of the first visual line 
 * @param lineCount number of visual lines
 */
private void resetVisualLines(int startLine, int lineCount) {
    int endLine = startLine + lineCount;

	for (int i = startLine; i < endLine; i++) {
		visualLines[i] = new int[] {-1, -1};		
	}
}
/**
 * @see StyledTextContent#replaceTextRange(int, int, String)
 */
public void replaceTextRange(int start, int replaceLength, String text) {
	logicalContent.replaceTextRange(start, replaceLength, text);
}
/**
 * @see StyledTextContent#setText(String)
 */
public void setText(String text) {
    logicalContent.setText(text);
}
/**
 * Set the line wrap data for the specified visual (wrapped) line.
 * <p>
 * 
 * @param visualLineIndex index of the visual line
 * @param visualLineOffset start offset of the visual line, relative 
 * 	to the start of the document
 * @param visualLineLength length of the visual line
 */
private void setVisualLine(int visualLineIndex, int visualLineOffset, int visualLineLength) {
	ensureSize(visualLineCount + 1);
	// is the space for the visual line already taken? can happen if 
	// there are more visual lines for a given logical line than before
	if (visualLines[visualLineIndex][LINE_OFFSET] != -1) {
		System.arraycopy(visualLines, visualLineIndex, visualLines, visualLineIndex + 1, visualLineCount - visualLineIndex);
		visualLines[visualLineIndex] = new int[2];
	}
	visualLines[visualLineIndex][LINE_OFFSET] = visualLineOffset;
	visualLines[visualLineIndex][LINE_LENGTH] = visualLineLength;
	visualLineCount++;
}
/** 
 * Recalculates the line wrap for the lines affected by the 
 * text change.
 * <p>
 *
 * @param startOffset	the start offset of the text change
 * @param newLineCount the number of inserted lines
 * @param replaceLineCount the number of deleted lines
 * @param newCharCount the number of new characters
 * @param replaceCharCount the number of deleted characters
 */  
void textChanged(int startOffset, int newLineCount, int replaceLineCount, int newCharCount, int replaceCharCount) {
	// do nothing if there are no wrapped lines
	if (visualLineCount == 0) {
		return;
	}
	int logicalStartLine = logicalContent.getLineAtOffset(startOffset);
	int visualStartLine = getLineAtOffset(startOffset);
	int visualReplaceLastLine = visualLineCount - 1;
	int textChangeDelta = newCharCount - replaceCharCount;
		
	if (replaceLineCount > 0) {	
		visualReplaceLastLine = getLineAtOffset(startOffset + replaceCharCount);
	    // at the start of a visual line/end of the previous visual line?
		if ((visualReplaceLastLine == 0 || 
		    visualLines[visualReplaceLastLine][LINE_OFFSET] == visualLines[visualReplaceLastLine - 1][LINE_OFFSET] + visualLines[visualReplaceLastLine - 1][LINE_LENGTH]) &&
		    visualReplaceLastLine != visualLineCount - 1) {
			visualReplaceLastLine++;
		}		
		visualStartLine = reset(visualStartLine, visualReplaceLastLine - visualStartLine + 1, false);
	}
	else {
		visualStartLine = reset(visualStartLine, 1, false);
	}
	visualReplaceLastLine = wrapLineRange(logicalStartLine, logicalStartLine + 1 + newLineCount, visualStartLine);
	for (int i = visualReplaceLastLine; i < visualLineCount; i++) {
		visualLines[i][LINE_OFFSET] += textChangeDelta;
	}
}
/**
 * Wrap the logical lines in the given range at the current client 
 * area width of the StyledText widget
 * <p>
 * 
 * @param startLine first logical line to wrap
 * @param endLine line after last logical line 
 * @param visualLineIndex visual (wrapped) line index that startLine
 * 	corresponds to.
 * @return index of the line following the last wrapped line
 */
private int wrapLineRange(int startLine, int endLine, int visualLineIndex) {
	int emptyLineCount = 0;
		
	int width = renderer.getClientArea().width - renderer.getLeftMargin() - renderer.getRightMargin();
	visualLineIndex = wrapLineRange(startLine, endLine, visualLineIndex, width);
	// is there space left for more visual lines? can happen if there are fewer
	// visual lines for a given logical line than before
	for (int i = visualLineIndex; i < visualLines.length; i++, emptyLineCount++) {
	    if (visualLines[i][LINE_OFFSET] != -1) {
	        break;
	    }
	}
	if (emptyLineCount > 0) {
		int copyLineCount = visualLineCount - visualLineIndex;
		System.arraycopy(visualLines, visualLineIndex + emptyLineCount, visualLines, visualLineIndex, copyLineCount);
		resetVisualLines(visualLineIndex + copyLineCount, emptyLineCount);
	}
	return visualLineIndex;
}
/**
 * Wrap the lines in the given range. Skip lines that have already 
 * been wrapped.
 * <p>
 * 
 * @param startLine first logical line to wrap
 * @param endLine line after last logical line 
 * @param visualLineIndex visual (wrapped) line index that startLine
 * 	corresponds to.
 * @param width line width to wrap at
 * @return index of last wrapped line
 */
private int wrapLineRange(int startLine, int endLine, int visualLineIndex, int width) {
	// if there are no wrapped lines and the width is 0 the widget has
	// not been made visible/sized yet. don't wrap until the widget size 
	// is known.
	if (visualLineCount == 0 && width == 0) {
		return visualLineIndex;
	}

	for (int i = startLine; i < endLine; i++) {
	    String line = logicalContent.getLine(i);
   	    int lineOffset = logicalContent.getOffsetAtLine(i);
   	    int lineLength = line.length();
   		if (lineLength == 0) {
			setVisualLine(visualLineIndex, lineOffset, 0);
			visualLineIndex++;
			continue;
   		}
		TextLayout layout = renderer.getTextLayout(line, lineOffset);
		layout.setWidth(Math.max(1, width));
		int[] offsets = layout.getLineOffsets();
		for (int j = 0; j < offsets.length - 1; j++) {
			setVisualLine(visualLineIndex++, lineOffset + offsets[j], offsets[j+1] - offsets[j]);
		}
		renderer.disposeTextLayout(layout); 
	}
	return visualLineIndex;
}
/**
 * Wrap all logical lines at the current client area width of the 
 * StyledText widget
 */
void wrapLines() {
	int width = renderer.getClientArea().width - renderer.getLeftMargin() - renderer.getRightMargin();
	wrapLines(width);
}
/**
 * Wrap all logical lines at the given width.
 * <p>
 * 
 * @param width width to wrap lines at
 */
void wrapLines(int width) {
    int lineCount = logicalContent.getLineCount();

	visualLineCount = 0;
	visualLines = new int[lineCount][2];
	resetVisualLines(0, visualLines.length);		
	wrapLineRange(0, lineCount, 0, width);	
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
 * A DisplayRenderer renders the content of a StyledText widget on 
 * a display device.
 */
class DisplayRenderer extends StyledTextRenderer {
	private StyledText parent;			// used to get content and styles during rendering
	private int topIndex = -1;
	private TextLayout[] layouts;
	
/**
 * Creates an instance of <class>DisplayRenderer</class>.
 * </p>
 * @param device Device to render on
 * @param regularFont Font to use for regular text
 * @param parent <class>StyledText</class> widget to render
 * @param tabLength length in characters of a tab character
 */
DisplayRenderer(Device device, Font regularFont, StyledText parent, int tabLength) {
	super(device, regularFont);
	this.parent = parent;
	calculateLineHeight();
	setTabLength(tabLength);
}
void dispose() {
	super.dispose();
	if (layouts != null) {
		for (int i = 0; i < layouts.length; i++) {
			TextLayout layout = layouts[i];
			if (layout != null) super.disposeTextLayout(layout);
		}
		topIndex = -1;
		layouts = null;
	}
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
 * Draws the line delimiter selection if the selection extends beyond the given line.
 * </p>
 *
 * @param line the line to draw
 * @param lineOffset offset of the first character in the line.
 * 	Relative to the start of the document.
 * @param paintX x location to draw at
 * @param paintY y location to draw at
 * @param gc GC to draw on
 */
protected void drawLineBreakSelection(String line, int lineOffset, int paintX, int paintY, GC gc) {
	Point selection = parent.internalGetSelection();
	int lineLength = line.length();
	int selectionStart = Math.max(0, selection.x - lineOffset);
	int selectionEnd = selection.y - lineOffset;
	int lineEndSpaceWidth = getLineEndSpaceWidth();
	int lineHeight = getLineHeight();
	
	if (selectionEnd == selectionStart || selectionEnd < 0 || selectionStart > lineLength || selectionEnd <= lineLength) {
		return;
	}
	
	gc.setBackground(parent.getSelectionBackground());
	gc.setForeground(parent.getSelectionForeground());
	if ((parent.getStyle() & SWT.FULL_SELECTION) != 0) {
		Rectangle rect = getClientArea();
		gc.fillRectangle(paintX, paintY, rect.width - paintX, lineHeight);
	} else {
		boolean isWrappedLine = false;
		if (parent.internalGetWordWrap()) {
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
		if (isWrappedLine == false) {
			// render the line break selection
			gc.fillRectangle(paintX, paintY, lineEndSpaceWidth, lineHeight);
		}
	}	
}
/**
 * Returns the text segments that should be treated as if they 
 * had a different direction than the surrounding text.
 * </p>
 *
 * @param lineOffset offset of the first character in the line. 
 * 	0 based from the beginning of the document.
 * @param lineText text of the line to specify bidi segments for
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
	if (!parent.isBidi()) return null;
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
 * @see #disposeGC
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
protected int getLeftMargin() {
	return parent.leftMargin;
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
protected  int getOrientation () {
	return parent.getOrientation();
}
protected int getRightMargin() {
	return parent.rightMargin;
}
protected Color getSelectionBackground() {
	return parent.getSelectionBackground();
}
protected Color getSelectionForeground() {
	return parent.getSelectionForeground();
}
/**
 * @see StyledTextRenderer#getSelection
 */
protected Point getSelection() {
	return parent.internalGetSelection();
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
TextLayout createTextLayout(int lineOffset) {
	if (!parent.internalGetWordWrap()) {
		int lineIndex = getContent().getLineAtOffset(lineOffset);
		updateTopIndex();
		if (layouts != null) {
			int layoutIndex = lineIndex - topIndex;
			if (0 <= layoutIndex && layoutIndex < layouts.length) {
				TextLayout layout = layouts[layoutIndex];
				if (layout != null) return layout;
				return layouts[layoutIndex] = super.createTextLayout(lineIndex); 
			}
		}
	}
	return super.createTextLayout(lineOffset);
}
void disposeTextLayout (TextLayout layout) {
	if (layouts != null) {
		for (int i=0; i<layouts.length; i++) {
			if (layouts[i] == layout) return;
		}
	}
	super.disposeTextLayout(layout);
}
void updateTopIndex() {
	int verticalIncrement = parent.getVerticalIncrement();
	int topIndex = verticalIncrement == 0 ? 0 : parent.verticalScrollOffset / verticalIncrement;
	int newLength = Math.max(1 , parent.getPartialBottomIndex() - topIndex + 1);
	if (layouts == null || topIndex != this.topIndex || newLength != layouts.length) {
		TextLayout[] newLayouts = new TextLayout[newLength];
		if (layouts != null) {
			for(int i=0; i<layouts.length; i++) {
				TextLayout layout = layouts[i];
				if (layout != null) {
					int layoutIndex = (i + this.topIndex) - topIndex;
					if (0 <= layoutIndex && layoutIndex < newLayouts.length) {
						newLayouts[layoutIndex] = layout;
					} else {
						super.disposeTextLayout(layout);
					}
				}
			}
		}
		this.topIndex = topIndex;
		layouts = newLayouts;
	}
}
}

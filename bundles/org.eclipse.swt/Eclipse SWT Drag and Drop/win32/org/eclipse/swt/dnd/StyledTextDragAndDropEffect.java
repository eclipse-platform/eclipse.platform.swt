/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;


import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

class StyledTextDragAndDropEffect extends DragAndDropEffect {
	StyledText text;
	long scrollBeginTime;
	int scrollX = -1, scrollY = -1;
	
	static final int SCROLL_HYSTERESIS = 100; // milli seconds
	static final int SCROLL_TOLERANCE = 20; // pixels
	
StyledTextDragAndDropEffect(StyledText control) {
	text = control;
}
void showDropTargetEffect(int effect, int x, int y) {
	Point pt = text.getDisplay().map(null, text, x, y);
	if ((effect & DND.FEEDBACK_SCROLL) == 0) {
		scrollBeginTime = 0;
		scrollY = scrollX = -1;
	} else {
		if (scrollX != -1 && scrollY != -1 && scrollBeginTime != 0 &&
			(pt.x >= scrollX && pt.x <= (scrollX + SCROLL_TOLERANCE) ||
			 pt.y >= scrollY && pt.y <= (scrollY + SCROLL_TOLERANCE))) {
			if (System.currentTimeMillis() >= scrollBeginTime) {
				Rectangle area = text.getClientArea();
				Rectangle bounds = text.getTextBounds(0, 0);
				int charWidth = bounds.width;
				if (pt.x < area.x + 2*charWidth) {
					int leftPixel = text.getHorizontalPixel();
					text.setHorizontalPixel(leftPixel - charWidth);
					if (text.getHorizontalPixel() != leftPixel) {
						text.redraw();
					}
				}
				if (pt.x > area.width - 2*charWidth) {
					int leftPixel = text.getHorizontalPixel();
					text.setHorizontalPixel(leftPixel + charWidth);
					if (text.getHorizontalPixel() != leftPixel) {
						text.redraw();
					}
				}
				int lineHeight = bounds.height;
				if (pt.y < area.y + lineHeight) {
					int topPixel = text.getTopPixel();
					text.setTopPixel(topPixel - lineHeight);
					if (text.getTopPixel() != topPixel) {
						text.redraw();
					}
				}
				if (pt.y > area.height - lineHeight) {
					int topPixel = text.getTopPixel();
					text.setTopPixel(topPixel + lineHeight);
					if (text.getTopPixel() != topPixel) {
						text.redraw();
					}
				}
				scrollBeginTime = 0;
				scrollX = scrollY = -1;
			}
		} else {
			scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
			scrollX = pt.x;
			scrollY = pt.y;
		}
	}
		
	if ((effect & DND.FEEDBACK_SELECT) != 0) {
		int oldOffset = text.getCaretOffset();
		int newOffset = -1;
		try {
			newOffset = text.getOffsetAtLocation(pt);
		} catch (IllegalArgumentException ex1) {
			int maxOffset = text.getCharCount();
			Point maxLocation = text.getLocationAtOffset(maxOffset);
			if (pt.y >= maxLocation.y) {
				if (pt.x >= maxLocation.x) {
					newOffset = maxOffset;
				} else {
					try {
						newOffset = text.getOffsetAtLocation(new Point(pt.x, maxLocation.y));
					} catch (IllegalArgumentException ex2) {
						newOffset = -1;
					}
				}
			} else {
				try {
					int startOffset = text.getOffsetAtLocation(new Point(0, pt.y));
					int endOffset = maxOffset;
					int line = text.getLineAtOffset(startOffset);
					int lineCount = text.getLineCount();
					if (line + 1 < lineCount) {
						endOffset = text.getOffsetAtLine(line + 1)  - 1;
					}
					int lineHeight = text.getLineHeight(startOffset);
					for (int i = endOffset; i >= startOffset; i--) {
						Point p = text.getLocationAtOffset(i);
						if (p.x < pt.x && p.y < pt.y && p.y + lineHeight > pt.y) {
							newOffset = i;
							break;
						}
					}
				} catch (IllegalArgumentException ex2) {
					newOffset = -1;
				}
			}
		}
		if (newOffset != -1 && newOffset != oldOffset) {
			text.setFocus();
			OS.ImageList_DragShowNolock(false);
			text.setCaretOffset(newOffset);
			OS.ImageList_DragShowNolock(true);
		}
	}
}
}

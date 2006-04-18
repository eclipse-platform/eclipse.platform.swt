/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
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
import org.eclipse.swt.internal.motif.*;

class StyledTextDragAndDropEffect extends DragAndDropEffect {
	StyledText text;
	Rectangle caretBounds;
	
StyledTextDragAndDropEffect(StyledText control) {
	text = control;
}
void showDropTargetEffect(int effect, int x, int y) {
	int newOffset = -1;
	if ((effect & DND.FEEDBACK_SELECT) != 0) {
		Point pt = text.getDisplay().map(null, text, x, y);
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
	}
	if (caretBounds != null) {
		// hide caret
		drawCaret();
		caretBounds = null; 
	}
	if (newOffset != -1) {
		text.setCaretOffset(newOffset);
		// show caret
		caretBounds = text.getCaret().getBounds();
		drawCaret();
	}
}
void drawCaret() {
	int window = OS.XtWindow(text.handle);
	if (window == 0) return;
	int xDisplay = OS.XtDisplay(text.handle);
	int gc = OS.XCreateGC(xDisplay, window, 0, null);
	int color = 0;
	if (OS.IsSunOS) {
		int[] argList = {OS.XmNforeground, 0};
		OS.XtGetValues(text.handle, argList, argList.length / 2);
		int foreground = argList[1];
		argList = new int[] {OS.XmNbackground, 0};
		OS.XtGetValues(text.handle, argList, argList.length / 2);
		int background = argList[1];
		color = foreground ^ background;
	} else {
		int screenNum = OS.XDefaultScreen(xDisplay);	
		color = OS.XWhitePixel(xDisplay, screenNum);
	}
	OS.XSetForeground(xDisplay, gc, color);
	OS.XSetFunction(xDisplay, gc, OS.GXxor);
	int nWidth = caretBounds.width, nHeight = caretBounds.height;
	if (nWidth <= 0) nWidth = 1;
	OS.XFillRectangle(xDisplay, window, gc, caretBounds.x, caretBounds.y, nWidth, nHeight);
	OS.XFreeGC(xDisplay, gc);
}
}

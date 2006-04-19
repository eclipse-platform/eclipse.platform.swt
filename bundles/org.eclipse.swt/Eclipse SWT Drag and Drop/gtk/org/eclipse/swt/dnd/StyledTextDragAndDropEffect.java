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
import org.eclipse.swt.internal.gtk.*;

class StyledTextDragAndDropEffect extends DragAndDropEffect {
	StyledText text;
	Rectangle caretBounds;
	long scrollBeginTime;
	int scrollY;
	
	static final int SCROLL_HYSTERESIS = 100; // milli seconds
	static final int SCROLL_TOLERANCE = 20; // pixels
	
StyledTextDragAndDropEffect(StyledText control) {
	text = control;
}
void showDropTargetEffect(int effect, int x, int y) {
	Point pt = text.getDisplay().map(null, text, x, y);
	if ((effect & DND.FEEDBACK_SCROLL) == 0) {
		scrollBeginTime = 0;
		scrollY = -1;
	} else {
		if (scrollY != -1 && scrollBeginTime != 0
			&& pt.y >= scrollY && pt.y <= (scrollY + SCROLL_TOLERANCE)) {
			if (System.currentTimeMillis() >= scrollBeginTime) {
				Rectangle area = text.getClientArea();
				int lineHeight = text.getLineHeight();
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
				scrollY = -1;
			}
		} else {
			scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
			scrollY = pt.y;
		}
	}
	
	int newOffset = -1;
	if ((effect & DND.FEEDBACK_SELECT) != 0) {
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
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (text.handle);
	int /*long*/ gc = OS.gdk_gc_new (window);
	GdkColor color = new GdkColor ();
	color.red = (short) 0xffff;
	color.green = (short) 0xffff;
	color.blue = (short) 0xffff;
	int /*long*/ colormap = OS.gdk_colormap_get_system ();
	OS.gdk_colormap_alloc_color (colormap, color, true, true);
	OS.gdk_gc_set_foreground (gc, color);
	OS.gdk_gc_set_function (gc, OS.GDK_XOR);
	int nWidth = caretBounds.width, nHeight = caretBounds.height;
	if (nWidth <= 0) nWidth = 1;
	OS.gdk_draw_rectangle (window, gc, 1, caretBounds.x, caretBounds.y, nWidth, nHeight);
	OS.g_object_unref (gc);
	OS.gdk_colormap_free_colors (colormap, color, 1);
}
}

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
	
StyledTextDragAndDropEffect(StyledText control) {
	text = control;
}
void showDropTargetEffect(int effect, int x, int y) {
	int offset = -1;
	if ((effect & DND.FEEDBACK_SELECT) != 0) {
		Point pt = text.getDisplay().map(null, text, x, y);
		try {
			//text.setFocus();	
			offset = text.getOffsetAtLocation(pt);
		} catch (IllegalArgumentException ex) {
			int maxOffset = text.getCharCount();
			Point maxLocation = text.getLocationAtOffset(maxOffset);
			if (pt.y >= maxLocation.y) {
				if (pt.x >= maxLocation.x) {
					offset = maxOffset;
				} else {
					offset = text.getOffsetAtLocation(new Point(pt.x, maxLocation.y));
				}
			} else {
				offset = maxOffset;
			}
		}
	}
	if (caretBounds != null) {
			// hide caret
			drawCaret();
			caretBounds = null; 
	}
	if (offset != -1) {
		text.setCaretOffset(offset);
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

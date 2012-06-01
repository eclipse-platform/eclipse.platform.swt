/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.theme;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

public class DrawData {
	public int style;
	public int[] state;
	public Rectangle clientArea;

	/** Part states */
	public static final int SELECTED = 1 << 1;
	public static final int FOCUSED = 1 << 2;
	public static final int PRESSED = 1 << 3;
	public static final int ACTIVE = 1 << 4;
	public static final int DISABLED = 1 << 5;
	public static final int HOT = 1 << 6;
	public static final int DEFAULTED = 1 << 7;
	public static final int GRAYED = 1 << 8;
	
	/** Text and Image drawing flags */
	public static final int DRAW_LEFT = 1 << 4;
	public static final int DRAW_TOP = 1 << 5;
	public static final int DRAW_RIGHT = 1 << 6;
	public static final int DRAW_BOTTOM = 1 << 7;
	public static final int DRAW_HCENTER = 1 << 8;
	public static final int DRAW_VCENTER = 1 << 9;

	/** Widget parts */
	public static final int WIDGET_NOWHERE = -1;
	public static final int WIDGET_WHOLE = 0;

	/** Scrollbar parts */
	public static final int SCROLLBAR_UP_ARROW = 1;
	public static final int SCROLLBAR_DOWN_ARROW = 2;
	public static final int SCROLLBAR_LEFT_ARROW = SCROLLBAR_UP_ARROW;
	public static final int SCROLLBAR_RIGHT_ARROW = SCROLLBAR_DOWN_ARROW;
	public static final int SCROLLBAR_UP_TRACK = 3;
	public static final int SCROLLBAR_DOWN_TRACK = 4;
	public static final int SCROLLBAR_LEFT_TRACK = SCROLLBAR_UP_TRACK;
	public static final int SCROLLBAR_RIGHT_TRACK = SCROLLBAR_DOWN_TRACK;
	public static final int SCROLLBAR_THUMB = 5;
	
	/** Scale parts */
	public static final int SCALE_UP_TRACK = 1;
	public static final int SCALE_LEFT_TRACK = SCALE_UP_TRACK;
	public static final int SCALE_DOWN_TRACK = 2;
	public static final int SCALE_RIGHT_TRACK = SCALE_DOWN_TRACK;
	public static final int SCALE_THUMB = 3;
	
	/** ToolItem parts */
	public static final int TOOLITEM_ARROW = 1;
	
	/** Combo parts */
	public static final int COMBO_ARROW = 1;
	

public DrawData() {
	state = new int[1];
}

Rectangle computeTrim(Theme theme, GC gc) {
	return new Rectangle(clientArea.x, clientArea.y, clientArea.width, clientArea.height);
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	
}

void drawImage(Theme theme, Image image, GC gc, Rectangle bounds) {
	int /*long*/ drawable = gc.getGCData().drawable;
	Rectangle rect = image.getBounds();
	int state_type = getStateType(DrawData.WIDGET_WHOLE);
	if (state_type == OS.GTK_STATE_NORMAL) {
		gc.drawImage(image, 0, 0, rect.width, rect.height, bounds.x, bounds.y, bounds.width, bounds.height);
	} else {
		int /*long*/ pixbuf = ImageList.createPixbuf(image);
		int /*long*/ source = OS.gtk_icon_source_new();
		if (source != 0) {
			OS.gtk_icon_source_set_pixbuf(source, pixbuf);
			//TODO - always uses buttonHandle
			int /*long*/ buttonHandle = theme.buttonHandle;
			int /*long*/ gtkStyle = OS.gtk_widget_get_style (buttonHandle);
			theme.transferClipping(gc, gtkStyle);
			int /*long*/ rendered = OS.gtk_style_render_icon(gtkStyle, source, OS.GTK_TEXT_DIR_NONE, state_type, -1, buttonHandle, null);
			OS.g_object_unref(pixbuf);
			//TODO - stretching
			if (rendered != 0) {
				OS.gdk_draw_pixbuf(drawable, gc.handle, rendered, 0, 0, bounds.x, bounds.y, bounds.width, bounds.height, OS.GDK_RGB_DITHER_NORMAL, 0, 0);
				OS.g_object_unref(rendered);
			}
			OS.gtk_icon_source_free(source);
		}
	}
}

void drawText(Theme theme, String text, int flags, GC gc, Rectangle bounds) {
	int /*long*/ widget = getTextHandle(theme);
	int /*long*/ gtkStyle = OS.gtk_widget_get_style(widget);
	int /*long*/ drawable = gc.getGCData().drawable;
	theme.transferClipping (gc, gtkStyle);
	byte[] buffer = Converter.wcsToMbcs(null, text, true);
	int /*long*/ layout = OS.gtk_widget_create_pango_layout(widget, buffer);
	int[] width = new int[1], height = new int[1];
	OS.pango_layout_get_size(layout, width, height);
	OS.pango_layout_set_width(layout, bounds.width * OS.PANGO_SCALE);
	int x = bounds.x;
	int y = bounds.y;
	if ((flags & DrawData.DRAW_LEFT) != 0) {
		OS.pango_layout_set_alignment(layout, OS.PANGO_ALIGN_LEFT);
	}
	if ((flags & DrawData.DRAW_HCENTER) != 0) {
		OS.pango_layout_set_alignment(layout, OS.PANGO_ALIGN_CENTER);
	}
	if ((flags & DrawData.DRAW_RIGHT) != 0) {
		OS.pango_layout_set_alignment(layout, OS.PANGO_ALIGN_RIGHT);
	}
	if ((flags & DrawData.DRAW_VCENTER) != 0) {
		y += (bounds.height - OS.PANGO_PIXELS(height[0])) / 2;
	}
	if ((flags & DrawData.DRAW_BOTTOM) != 0) {
		y += bounds.height - OS.PANGO_PIXELS(height[0]);
	}
	int state_type = getStateType(DrawData.WIDGET_WHOLE);
	byte[] detail = Converter.wcsToMbcs(null, "label", true);
	OS.gtk_paint_layout(gtkStyle, drawable, state_type, false, null, widget, detail, x, y, layout);
	OS.g_object_unref(layout);
}

Rectangle getBounds(int part, Rectangle bounds) {
	return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
}

int getStateType(int part) {
	int state = this.state[part];
	int state_type = OS.GTK_STATE_NORMAL;
	if ((state & DrawData.DISABLED) != 0) {
		state_type = OS.GTK_STATE_INSENSITIVE;
	} else {
		if ((state & DrawData.SELECTED) != 0) state_type = OS.GTK_STATE_ACTIVE;
		if ((state & DrawData.HOT) != 0) {
			if ((state & DrawData.PRESSED) != 0) { 
				state_type = OS.GTK_STATE_ACTIVE;
			} else {
				state_type = OS.GTK_STATE_PRELIGHT;
			}
		}
	}
	return state_type;
}

int /*long*/ getTextHandle(Theme theme) {
	return theme.labelHandle;
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return -1;
}

Rectangle measureText(Theme theme, String text, int flags, GC gc, Rectangle bounds) {
	int /*long*/ widget = getTextHandle(theme);
	byte[] buffer = Converter.wcsToMbcs(null, text, true);
	int /*long*/ layout = OS.gtk_widget_create_pango_layout(widget, buffer);
	if (bounds != null) OS.pango_layout_set_width(layout, bounds.width);
	if ((flags & DrawData.DRAW_LEFT) != 0) {
		OS.pango_layout_set_alignment(layout, OS.PANGO_ALIGN_LEFT);
	}
	if ((flags & DrawData.DRAW_HCENTER) != 0) {
		OS.pango_layout_set_alignment(layout, OS.PANGO_ALIGN_CENTER);
	}
	if ((flags & DrawData.DRAW_RIGHT) != 0) {
		OS.pango_layout_set_alignment(layout, OS.PANGO_ALIGN_RIGHT);
	}
	int[] width = new int[1], height = new int[1];
	OS.pango_layout_get_size(layout, width, height);
	OS.g_object_unref(layout);
	return new Rectangle(0, 0, OS.PANGO_PIXELS(width[0]), OS.PANGO_PIXELS(height[0]));
}

}

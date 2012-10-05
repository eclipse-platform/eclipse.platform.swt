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
import org.eclipse.swt.internal.cairo.Cairo;
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
	long /*int*/ drawable = gc.getGCData().drawable;
	Rectangle rect = image.getBounds();
	int state_type = getStateType(DrawData.WIDGET_WHOLE);
	if (state_type == OS.GTK_STATE_NORMAL) {
		gc.drawImage(image, 0, 0, rect.width, rect.height, bounds.x, bounds.y, bounds.width, bounds.height);
	} else {
		long /*int*/ pixbuf = ImageList.createPixbuf(image);
		long /*int*/ source = OS.gtk_icon_source_new();
		if (source != 0) {
			OS.gtk_icon_source_set_pixbuf(source, pixbuf);
			//TODO - always uses buttonHandle
			long /*int*/ buttonHandle = theme.buttonHandle;
			long /*int*/ gtkStyle = OS.gtk_widget_get_style (buttonHandle);
			theme.transferClipping(gc, gtkStyle);
			long /*int*/ rendered = OS.gtk_style_render_icon(gtkStyle, source, OS.GTK_TEXT_DIR_NONE, state_type, -1, buttonHandle, null);
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
	long /*int*/ widget = getTextHandle(theme);
	long /*int*/ gtkStyle = OS.gtk_widget_get_style(widget);
	long /*int*/ drawable = gc.getGCData().drawable;
	theme.transferClipping (gc, gtkStyle);
	byte[] buffer = Converter.wcsToMbcs(null, text, true);
	long /*int*/ layout = OS.gtk_widget_create_pango_layout(widget, buffer);
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
	gtk_render_layout(gtkStyle, drawable, state_type, false, null, widget, detail, x, y, layout);
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

long /*int*/ getTextHandle(Theme theme) {
	return theme.labelHandle;
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return -1;
}

Rectangle measureText(Theme theme, String text, int flags, GC gc, Rectangle bounds) {
	long /*int*/ widget = getTextHandle(theme);
	byte[] buffer = Converter.wcsToMbcs(null, text, true);
	long /*int*/ layout = OS.gtk_widget_create_pango_layout(widget, buffer);
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

void gtk_render_frame (long /*int*/ style, long /*int*/ window, int state_type, int shadow_type, GdkRectangle area, long /*int*/ widget, byte[] detail, int x , int y, int width, int height) {
	if (OS.GTK_VERSION >= OS.VERSION (3, 0, 0)) {
		long /*int*/ cairo = OS.gdk_cairo_create (window);
		long /*int*/ context = OS.gtk_widget_get_style_context (style);
		OS.gtk_render_frame (context, cairo, context, y, width, height);
		Cairo.cairo_destroy (cairo);
	} else {
		OS.gtk_paint_flat_box (style, window, state_type, shadow_type, area, widget, detail, x, y, width, height);
	}
}

void gtk_render_box (long /*int*/ style, long /*int*/ window, int state_type, int shadow_type, GdkRectangle area, long /*int*/ widget, byte[] detail, int x , int y, int width, int height) {
	if (OS.GTK_VERSION >= OS.VERSION(3, 0, 0)) {
		long /*int*/ cairo = OS.gdk_cairo_create (window);
		long /*int*/ context = OS.gtk_widget_get_style_context (style);
		OS.gtk_render_frame (context, cairo, x, y, width, height);
		OS.gtk_render_background (context, cairo, x, y, width, height);
		Cairo.cairo_destroy (cairo);
	} else {
		OS.gtk_paint_box (style, window, state_type, shadow_type, area, widget, detail, x, y, width, height);
	}
}


void gtk_render_layout (long /*int*/ style, long /*int*/ window, int state_type, boolean use_text, GdkRectangle area, long /*int*/ widget, byte[] detail, int x , int y, long /*int*/ layout) {
	if (OS.GTK_VERSION >= OS.VERSION(3, 0, 0)) {
		long /*int*/ cairo = OS.gdk_cairo_create (window);
		long /*int*/ context = OS.gtk_widget_get_style_context (style);
		OS.gtk_render_layout (context, cairo, x, y, layout);
		Cairo.cairo_destroy (cairo);
	} else {
		OS.gtk_paint_layout (style, window, state_type, use_text, area, widget, detail, x , y, layout);
	}
}

void gtk_render_focus (long /*int*/ style, long /*int*/ window, int state_type, GdkRectangle area, long /*int*/ widget, byte[] detail, int x , int y, int width, int height) {
	if (OS.GTK_VERSION >= OS.VERSION(3, 2, 0)) {
		long /*int*/ cairo = OS.gdk_cairo_create (window);
		long /*int*/  context = OS.gtk_widget_get_style_context (style);
		OS.gtk_style_context_save (context);
		OS.gtk_style_context_set_state (context, OS.gtk_widget_get_state_flags (widget));
		Cairo.cairo_save (cairo);
		OS.gtk_render_focus(context, cairo, x, y, width, height);
		Cairo.cairo_restore (cairo);
		OS.gtk_style_context_restore (context);
		Cairo.cairo_destroy (cairo);
		
	} else {
		OS.gtk_paint_focus (style, window, state_type, area, widget, detail, x, y, width, height);
	}
}

}

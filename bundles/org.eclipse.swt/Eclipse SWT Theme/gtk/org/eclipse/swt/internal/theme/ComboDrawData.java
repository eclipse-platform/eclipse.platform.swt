/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

public class ComboDrawData extends DrawData {

	static final int ARROW_HEIGHT = 6;
	static final int MIN_ARROW_SIZE = 15;

public ComboDrawData() {
	state = new int[2];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	long /*int*/ buttonHandle = theme.buttonHandle;
	long /*int*/ gtkStyle = OS.gtk_widget_get_style(buttonHandle);
	long /*int*/ drawable = gc.getGCData().drawable;
	theme.transferClipping(gc, gtkStyle);
	
	int x = bounds.x;
	int y = bounds.y ;
	int width = bounds.width;
	int height = bounds.height;

	int shadow_type = OS.GTK_SHADOW_OUT;
	if ((state[DrawData.COMBO_ARROW] & DrawData.PRESSED) != 0) shadow_type = OS.GTK_SHADOW_IN;
	int state_type = getStateType(DrawData.COMBO_ARROW);
	
	int relief = OS.gtk_button_get_relief(buttonHandle);
	int interior_focus = theme.getWidgetProperty(buttonHandle, "interior-focus");
	int focus_line_width = theme.getWidgetProperty(buttonHandle, "focus-line-width");
	int focus_padding = theme.getWidgetProperty(buttonHandle, "focus-padding");
	int xthickness = OS.gtk_style_get_xthickness(gtkStyle);
	int ythickness = OS.gtk_style_get_xthickness(gtkStyle);
	int arrow_width = MIN_ARROW_SIZE;
	int arrow_height = ARROW_HEIGHT;
	int x_border = xthickness + focus_padding;
	//int y_border = ythickness + focus_padding;
	if (interior_focus == 0) {
		x_border += focus_line_width;
		//y_border += focus_line_width;
	}
	int arrow_button_width = arrow_width + x_border * 2;		
	int arrow_button_x = x + width - arrow_button_width;
	int arrow_x = arrow_button_x + (arrow_button_width - arrow_width) / 2;
	int arrow_y = y + (height - arrow_height) / 2 + 1;
	if (relief != OS.GTK_RELIEF_NONE || ((state[DrawData.COMBO_ARROW] & (DrawData.PRESSED | DrawData.HOT)) != 0)) {
		byte[] detail = Converter.wcsToMbcs(null, "button", true);
		gtk_render_box (gtkStyle, drawable, state_type, shadow_type, null, buttonHandle, detail, arrow_button_x, y, arrow_button_width, height);
	}		
	byte[] arrow_detail = Converter.wcsToMbcs(null, "arrow", true);
	long /*int*/ arrowHandle = theme.arrowHandle;
	gtk_render_arrow (gtkStyle, drawable, state_type, OS.GTK_SHADOW_OUT, null, arrowHandle, arrow_detail, OS.GTK_ARROW_DOWN, true, arrow_x, arrow_y, arrow_width, arrow_height);
	
	long /*int*/ entryHandle = theme.entryHandle;
	gtkStyle = OS.gtk_widget_get_style(entryHandle);
	theme.transferClipping(gc, gtkStyle);
	state_type = getStateType(DrawData.WIDGET_WHOLE);
	byte[] detail = Converter.wcsToMbcs(null, "entry", true);
	gtk_render_shadow (gtkStyle, drawable, OS.GTK_STATE_NORMAL, OS.GTK_SHADOW_IN, null, entryHandle, detail, x, y, width - arrow_button_width, height);
	xthickness = OS.gtk_style_get_xthickness(gtkStyle);
	ythickness = OS.gtk_style_get_xthickness(gtkStyle);
	x += xthickness;
	y += ythickness;
	width -= 2 * xthickness;
	height -= 2 * ythickness;
	detail = Converter.wcsToMbcs(null, "entry_bg", true);
	gtk_render_frame (gtkStyle, drawable, state_type, OS.GTK_SHADOW_NONE, null, entryHandle, detail, x, y, width - arrow_button_width, height);
		
	if (clientArea != null) {
		clientArea.x = x;
		clientArea.y = y;
		clientArea.width = width - arrow_button_width;
		clientArea.height = height;
	}	
}

int getStateType(int part) {
	if (part == DrawData.WIDGET_WHOLE) {
		int state_type = OS.GTK_STATE_NORMAL;
		if ((state[DrawData.WIDGET_WHOLE] & DrawData.DISABLED) != 0) {
			state_type = OS.GTK_STATE_INSENSITIVE;
		}
		return state_type;
	}
	return super.getStateType(part);
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	long /*int*/ buttonHandle = theme.buttonHandle;
	long /*int*/ gtkStyle = OS.gtk_widget_get_style(buttonHandle);
	int interior_focus = theme.getWidgetProperty(buttonHandle, "interior-focus");
	int focus_line_width = theme.getWidgetProperty(buttonHandle, "focus-line-width");
	int focus_padding = theme.getWidgetProperty(buttonHandle, "focus-padding");
	int xthickness = OS.gtk_style_get_xthickness(gtkStyle);
//	int ythickness = OS.gtk_style_get_xthickness(gtkStyle);
	int arrow_width = MIN_ARROW_SIZE;
	int x_border = xthickness + focus_padding;
	//int y_border = ythickness + focus_padding;
	if (interior_focus == 0) {
		x_border += focus_line_width;
		//y_border += focus_line_width;
	}
	int arrow_button_width = arrow_width + x_border * 2;		
	int arrow_button_x = bounds.x + bounds.width - arrow_button_width;

	Rectangle arrowRect = new Rectangle(arrow_button_x, bounds.y, arrow_button_width, bounds.height);
	if (arrowRect.contains(position)) return DrawData.COMBO_ARROW;
	return DrawData.WIDGET_WHOLE;
}

void gtk_render_shadow(long /*int*/ style, long /*int*/ window, int state_type, int shadow_type, GdkRectangle area, long /*int*/ widget, byte[] detail, int x , int y, int width, int height) {
	if (OS.GTK3) {
		long /*int*/ cairo = OS.gdk_cairo_create (window);
		long /*int*/ context = OS.gtk_widget_get_style_context (style);
		OS.gtk_style_context_save(context);
		OS.gtk_style_context_set_state (style, state_type);
		OS.gtk_render_frame (context, cairo, x, y, width, height);
		Cairo.cairo_destroy (cairo);
	} else {
		OS.gtk_paint_shadow(style, window, state_type, shadow_type, area, widget, detail, x, y, width, height);
	}
}
}

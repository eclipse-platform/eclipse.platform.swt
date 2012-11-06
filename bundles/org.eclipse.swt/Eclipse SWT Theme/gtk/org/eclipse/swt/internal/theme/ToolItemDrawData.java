/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.theme;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.Cairo;
import org.eclipse.swt.internal.gtk.*;

public class ToolItemDrawData extends DrawData {

	public ToolBarDrawData parent;

	static final int ARROW_WIDTH = 8;
	static final int ARROW_HEIGHT = 6;

public ToolItemDrawData() {
	state = new int[2];
}

Rectangle computeTrim(Theme theme, GC gc) {
	long /*int*/ buttonHandle = theme.buttonHandle;
	long /*int*/ gtkStyle = OS.gtk_widget_get_style(buttonHandle);
	int focus_width = theme.getWidgetProperty(buttonHandle, "focus-line-width");
	int focus_padding = theme.getWidgetProperty(buttonHandle, "focus-padding");
	int xthickness = OS.gtk_style_get_xthickness(gtkStyle);
	int ythickness = OS.gtk_style_get_ythickness(gtkStyle);
	int borderX = xthickness + focus_width + focus_padding;
	int borderY = ythickness + focus_width + focus_padding;
	int x = clientArea.x - borderX;
	int y = clientArea.y - borderY;
	int width = clientArea.width + 2 * borderX;
	int height = clientArea.height + 2 * borderY;
	if ((style & SWT.DROP_DOWN) != 0) {
		width += ARROW_WIDTH;
	}
	return new Rectangle(x, y, width, height);
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	int state = this.state[DrawData.WIDGET_WHOLE];
	long /*int*/ drawable = gc.getGCData().drawable;

	if ((style & SWT.SEPARATOR) != 0) {
		int state_type = getStateType(DrawData.WIDGET_WHOLE);
		long /*int*/ separatorHandle = theme.separatorHandle;
		byte[] detail = Converter.wcsToMbcs(null, "vseparator", true);
		long /*int*/ gtkStyle = OS.gtk_widget_get_style (separatorHandle);
		theme.transferClipping(gc, gtkStyle);
		if ((parent.style & SWT.VERTICAL) != 0) {
			if (OS.GTK3) {
				long /*int*/ cairo = OS.gdk_cairo_create (drawable);
				long /*int*/ context = OS.gtk_widget_get_style_context (separatorHandle);
				OS.gtk_render_line (context, cairo, bounds.x, bounds.y + bounds.height / 2, bounds.x + bounds.width, bounds.y + bounds.height / 2);
				Cairo.cairo_destroy(cairo);
			} else {
				OS.gtk_paint_hline(gtkStyle, drawable, state_type, null, separatorHandle, detail, bounds.x, bounds.x + bounds.width, bounds.y + bounds.height / 2);
			}	
		} else {
			if (OS.GTK3) {
				long /*int*/ cairo = OS.gdk_cairo_create (drawable);
				long /*int*/ context = OS.gtk_widget_get_style_context (separatorHandle);
				OS.gtk_render_line (context, cairo, bounds.x + bounds.width / 2, bounds.y, bounds.x + bounds.width / 2, bounds.y + bounds.height);
				Cairo.cairo_destroy (cairo);
			} else {
				OS.gtk_paint_vline(gtkStyle, drawable, state_type, null, separatorHandle, detail, bounds.y, bounds.y + bounds.height, bounds.x + bounds.width / 2);
			}
		}
		return;
	}

	long /*int*/ buttonHandle = theme.buttonHandle;
	long /*int*/ gtkStyle = OS.gtk_widget_get_style (buttonHandle);
	theme.transferClipping (gc, gtkStyle);
	int focus_line_width = theme.getWidgetProperty(buttonHandle, "focus-line-width");
	int focus_padding = theme.getWidgetProperty(buttonHandle, "focus-padding");
	int border_width = OS.gtk_container_get_border_width(buttonHandle);

	int x = bounds.x + border_width;
	int y = bounds.y + border_width;
	int width = bounds.width - border_width * 2;
	int height = bounds.height - border_width * 2;

	byte[] detail = null;
	if ((style & (SWT.PUSH | SWT.DROP_DOWN)) != 0) {
		detail = Converter.wcsToMbcs(null, "button", true);
	} else if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		detail = Converter.wcsToMbcs(null, "togglebutton", true);
	}
	
	int[] relief = new int[1];
	long /*int*/ toolbarHandle = theme.toolbarHandle;
	OS.gtk_widget_style_get(toolbarHandle, OS.button_relief, relief, 0);

	int shadow_type = OS.GTK_SHADOW_OUT;
	if ((state & (DrawData.SELECTED | DrawData.PRESSED)) != 0) shadow_type = OS.GTK_SHADOW_IN;
	int state_type = getStateType(DrawData.WIDGET_WHOLE);
		
	if (relief[0] != OS.GTK_RELIEF_NONE || ((state & (DrawData.PRESSED | DrawData.HOT | DrawData.SELECTED)) != 0)) {
		gtk_render_box(gtkStyle, drawable, state_type, shadow_type, null, buttonHandle, detail, x, y, width, height);
	}

	if (clientArea != null) {
		clientArea.x = bounds.x + border_width;
		clientArea.y = bounds.y + border_width;
		clientArea.width = bounds.width - 2 * border_width;
		clientArea.height = bounds.height - 2 * border_width;
	}

	int xthickness = OS.gtk_style_get_xthickness(gtkStyle);
	int interior_focus = theme.getWidgetProperty(buttonHandle, "interior-focus");
	if ((style & SWT.DROP_DOWN) != 0) {
		int arrow_width = ARROW_WIDTH;
		int arrow_height = ARROW_HEIGHT;
		int arrow_x = x + width - arrow_width - xthickness - focus_padding;
		if (interior_focus == 0) arrow_x -= focus_line_width;
		int arrow_y = y + (height - arrow_height) / 2;
		byte[] arrow_detail = Converter.wcsToMbcs(null, "arrow", true);
		gtk_render_arrow (gtkStyle, drawable, state_type, OS.GTK_SHADOW_NONE, null, theme.arrowHandle, arrow_detail, OS.GTK_ARROW_DOWN, true, arrow_x, arrow_y, arrow_width, arrow_height);
		if (clientArea != null) {
			clientArea.width -= bounds.x + bounds.width - arrow_x;
		}
	}

    if ((state & DrawData.FOCUSED) != 0) {
    	int child_displacement_y = theme.getWidgetProperty(buttonHandle, "child-displacement-y");
    	int child_displacement_x = theme.getWidgetProperty(buttonHandle, "child-displacement-x");
    	int displace_focus = theme.getWidgetProperty(buttonHandle, "displace-focus");
    	
    	if (interior_focus != 0) {
    		int ythickness = OS.gtk_style_get_ythickness(gtkStyle);
    		x += xthickness + focus_padding;
            y += ythickness + focus_padding;
            width -= 2 * (xthickness + focus_padding);
            height -=  2 * (ythickness + focus_padding);
    	} else {
    		x -= focus_line_width + focus_padding;
    		y -= focus_line_width + focus_padding;
    		width += 2 * (focus_line_width + focus_padding);
    		height += 2 * (focus_line_width + focus_padding);
    	}
    	
    	if ((state & (DrawData.PRESSED | DrawData.SELECTED)) != 0 && displace_focus != 0) {
              x += child_displacement_x;
              y += child_displacement_y;
    	}
    	
    	gtk_render_focus (gtkStyle, drawable, state_type, null, buttonHandle, detail, x, y, width, height);
    }
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	if ((style & SWT.DROP_DOWN) != 0) {
		long /*int*/ buttonHandle = theme.buttonHandle;
		long /*int*/ gtkStyle = OS.gtk_widget_get_style (buttonHandle);
		int xthickness = OS.gtk_style_get_xthickness(gtkStyle);
		int interior_focus = theme.getWidgetProperty(buttonHandle, "interior-focus");
		int focus_line_width = theme.getWidgetProperty(buttonHandle, "focus-line-width");
		int focus_padding = theme.getWidgetProperty(buttonHandle, "focus-padding");
		int arrow_width = ARROW_WIDTH;
		int arrow_x = bounds.x + bounds.width - arrow_width - xthickness - focus_padding;
		if (interior_focus == 0) arrow_x -= focus_line_width;
		if (arrow_x <= position.x) return DrawData.TOOLITEM_ARROW;
	}
	return DrawData.WIDGET_WHOLE; 
}

}

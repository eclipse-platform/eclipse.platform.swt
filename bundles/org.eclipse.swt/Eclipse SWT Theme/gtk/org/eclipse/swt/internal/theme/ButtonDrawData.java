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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

public class ButtonDrawData extends DrawData {
	
public ButtonDrawData() {
	state = new int[1];
}


void draw(Theme theme, GC gc, Rectangle bounds) {
	int state = this.state[DrawData.WIDGET_WHOLE];

	long /*int*/ drawable = gc.getGCData().drawable;
	if ((style & SWT.RADIO) != 0) {
		long /*int*/ radioButtonHandle = theme.radioButtonHandle;
		long /*int*/ gtkStyle = OS.gtk_widget_get_style (radioButtonHandle);
		theme.transferClipping (gc, gtkStyle);
		int indicator_size = theme.getWidgetProperty(radioButtonHandle, "indicator-size");
		int indicator_spacing = theme.getWidgetProperty(radioButtonHandle, "indicator-spacing");
		int interior_focus = theme.getWidgetProperty(radioButtonHandle, "interior-focus");
		int focus_line_width = theme.getWidgetProperty(radioButtonHandle, "focus-line-width");
		int focus_padding = theme.getWidgetProperty(radioButtonHandle, "focus-padding");
		int border_width = OS.gtk_container_get_border_width(radioButtonHandle);
		
		int x = bounds.x + indicator_spacing + border_width;
		int y = bounds.y + (bounds.height - indicator_size) / 2;
		
		if (interior_focus == 0) {
			x += focus_line_width + focus_padding;
		}
		
		int shadow_type;
		if ((state & DrawData.GRAYED) != 0) {
			shadow_type = OS.GTK_SHADOW_ETCHED_IN;
		} else if ((state & DrawData.SELECTED) != 0) {
			shadow_type = OS.GTK_SHADOW_IN;
		} else {
			shadow_type = OS.GTK_SHADOW_OUT;
		}
		
		byte[] detail = Converter.wcsToMbcs(null, "radiobutton", true);
		if ((state & DrawData.HOT) != 0) {
			int prelight_x, prelight_y, prelight_width, prelight_height;
			prelight_x = bounds.x + border_width;
			prelight_y = bounds.y + border_width;
			prelight_width = bounds.width - (2 * border_width);
			prelight_height = bounds.height - (2 * border_width);
			gtk_render_frame (gtkStyle, drawable, OS.GTK_STATE_PRELIGHT, OS.GTK_SHADOW_ETCHED_OUT, null, radioButtonHandle, detail, prelight_x, prelight_y, prelight_width, prelight_height);
		}
		int state_type = getStateType(DrawData.WIDGET_WHOLE);
		OS.gtk_paint_option(gtkStyle, drawable, state_type, shadow_type, null, radioButtonHandle, detail, x, y, indicator_size, indicator_size);
		if (clientArea != null) {
			clientArea.x = bounds.x + 2 * indicator_spacing + border_width + indicator_size;
			clientArea.y = bounds.y + border_width;
			clientArea.width = bounds.width - (2 * indicator_spacing + 2 * border_width + indicator_size);
			clientArea.height = bounds.height - 2 * border_width;
		}
		return;
	}
	
	if ((style & SWT.CHECK) != 0) {
		long /*int*/ checkButtonHandle = theme.checkButtonHandle;
		long /*int*/ gtkStyle = OS.gtk_widget_get_style (checkButtonHandle);
		theme.transferClipping (gc, gtkStyle);
		int indicator_size = theme.getWidgetProperty(checkButtonHandle, "indicator-size");
		int indicator_spacing = theme.getWidgetProperty(checkButtonHandle, "indicator-spacing");
		int interior_focus = theme.getWidgetProperty(checkButtonHandle, "interior-focus");
		int focus_line_width = theme.getWidgetProperty(checkButtonHandle, "focus-line-width");
		int focus_padding = theme.getWidgetProperty(checkButtonHandle, "focus-padding");
		int border_width = OS.gtk_container_get_border_width(checkButtonHandle);
		
		int x = bounds.x + indicator_spacing + border_width;
		int y = bounds.y + (bounds.height - indicator_size) / 2;
		
		if (interior_focus == 0) {
			x += focus_line_width + focus_padding;
		}
		
		int shadow_type;
		if ((state & DrawData.GRAYED) != 0) {
			shadow_type = OS.GTK_SHADOW_ETCHED_IN;
		} else if ((state & DrawData.SELECTED) != 0) {
			shadow_type = OS.GTK_SHADOW_IN;
		} else {
			shadow_type = OS.GTK_SHADOW_OUT;
		}
		
		byte[] detail = Converter.wcsToMbcs(null, "checkbutton", true);
		if ((state & DrawData.HOT) != 0) {
			int prelight_x, prelight_y, prelight_width, prelight_height;
			prelight_x = bounds.x + border_width;
			prelight_y = bounds.y + border_width;
			prelight_width = bounds.width - (2 * border_width);
			prelight_height = bounds.height - (2 * border_width);
			gtk_render_frame (gtkStyle, drawable, OS.GTK_STATE_PRELIGHT, OS.GTK_SHADOW_ETCHED_OUT, null, checkButtonHandle, detail, prelight_x, prelight_y, prelight_width, prelight_height);
		}
		int state_type = getStateType(DrawData.WIDGET_WHOLE);
		OS.gtk_paint_check(gtkStyle, drawable, state_type, shadow_type, null, checkButtonHandle, detail, x, y, indicator_size, indicator_size);
		if (clientArea != null) {
			clientArea.x = bounds.x + 2 * indicator_spacing + border_width + indicator_size;
			clientArea.y = bounds.y + border_width;
			clientArea.width = bounds.width - (2 * indicator_spacing + 2 * border_width + indicator_size);
			clientArea.height = bounds.height - 2 * border_width;
		}
		return;
	}
	

	if ((style & SWT.PUSH) != 0) {
		long /*int*/ buttonHandle = theme.buttonHandle;
		long /*int*/ gtkStyle = OS.gtk_widget_get_style (buttonHandle);
		theme.transferClipping (gc, gtkStyle);		
		int focus_line_width = theme.getWidgetProperty(buttonHandle, "focus-line-width");
		int focus_padding = theme.getWidgetProperty(buttonHandle, "focus-padding");
		int border_width = OS.gtk_container_get_border_width(buttonHandle);
		
		GtkBorder default_border = new GtkBorder();
		long /*int*/ default_border_ptr = theme.getBorderProperty(buttonHandle, "default-border");
		if (default_border_ptr != 0) {
			OS.memmove(default_border, default_border_ptr, GtkBorder.sizeof);
			OS.gtk_border_free(default_border_ptr);
		} else {
			default_border.left = default_border.right = default_border.top = default_border.bottom = 1;
		}

		GtkBorder default_outside_border = new GtkBorder();
		long /*int*/ default_outside_border_ptr = theme.getBorderProperty(buttonHandle, "default-outside-border");
		if (default_outside_border_ptr != 0) {
			OS.memmove(default_outside_border, default_outside_border_ptr, GtkBorder.sizeof);
			OS.gtk_border_free(default_outside_border_ptr);
		} else {
			default_outside_border.left = default_outside_border.right = default_outside_border.top = default_outside_border.bottom = 0;
		}
		
		int x = bounds.x + border_width;
		int y = bounds.y + border_width;
		int width = bounds.width - border_width * 2;
		int height = bounds.height - border_width * 2;

		int relief = OS.gtk_button_get_relief(buttonHandle);
		byte[] detail = Converter.wcsToMbcs(null, (state & DrawData.DEFAULTED) != 0 ? "buttondefault" : "button", true);
		if ((state & DrawData.DEFAULTED) != 0 && relief == OS.GTK_RELIEF_NORMAL) {
            OS.gtk_paint_box(gtkStyle, drawable, OS.GTK_STATE_NORMAL, OS.GTK_SHADOW_IN, null, buttonHandle, detail, x, y, width, height);
            x += default_border.left;
            y += default_border.top;
            width -= default_border.left + default_border.right;
            height -= default_border.top + default_border.bottom;
		} else if ((state & DrawData.DEFAULTED) != 0) {
			x += default_outside_border.left;
			y += default_outside_border.top;
			width -= default_outside_border.left + default_outside_border.right;
			height -= default_outside_border.top + default_outside_border.bottom;
        }

		int shadow_type = OS.GTK_SHADOW_OUT;
		if ((state & (DrawData.SELECTED | DrawData.PRESSED)) != 0) shadow_type = OS.GTK_SHADOW_IN;
		int state_type = getStateType(DrawData.WIDGET_WHOLE);

		if (relief != OS.GTK_RELIEF_NONE || ((state & (DrawData.PRESSED | DrawData.HOT)) != 0)) {
			OS.gtk_paint_box(gtkStyle, drawable, state_type, shadow_type, null, buttonHandle, detail, x, y, width, height);
		}
		
	    if ((state & DrawData.FOCUSED) != 0) {
	    	int child_displacement_y = theme.getWidgetProperty(buttonHandle, "child-displacement-y");
	    	int child_displacement_x = theme.getWidgetProperty(buttonHandle, "child-displacement-x");
	    	int displace_focus = 0;
	    	if (OS.GTK_VERSION >= OS.VERSION (2, 6, 0)) {
	    		displace_focus = theme.getWidgetProperty(buttonHandle, "displace-focus");		    	
	    	}
	    	int interior_focus = theme.getWidgetProperty(buttonHandle, "interior-focus");
	    	
	    	if (interior_focus != 0) {
	    		int xthickness = OS.gtk_style_get_xthickness(gtkStyle);
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
	    	
	    	if ((state & DrawData.PRESSED) != 0 && displace_focus != 0) {
	              x += child_displacement_x;
	              y += child_displacement_y;
	    	}
	    	
	        OS.gtk_paint_focus(gtkStyle, drawable, state_type, null, buttonHandle, detail, x, y, width, height);
	    }
		if (clientArea != null) {
			clientArea.x = bounds.x + border_width;
			clientArea.y = bounds.y + border_width;
			clientArea.width = bounds.width - 2 * border_width;
			clientArea.height = bounds.height - 2 * border_width;
		}
	    return;
	}
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

}

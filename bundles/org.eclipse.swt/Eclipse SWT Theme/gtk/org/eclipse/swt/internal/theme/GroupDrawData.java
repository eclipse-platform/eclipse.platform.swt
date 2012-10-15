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

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.Cairo;
import org.eclipse.swt.internal.gtk.*;

public class GroupDrawData extends DrawData {
	public int headerWidth;
	public int headerHeight;
	public Rectangle headerArea;
	
	static final int GROUP_X = 2;
	static final int GROUP_PAD = 1;
	
public GroupDrawData() {
	state = new int[1];
}

static final int GROUP_HEADER_X = 9;
static final int GROUP_HEADER_PAD = 2;
void draw(Theme theme, GC gc, Rectangle bounds) {
	long /*int*/ frameHandle = theme.frameHandle;
	long /*int*/ gtkStyle = OS.gtk_widget_get_style (frameHandle);
	long /*int*/ drawable = gc.getGCData().drawable;
	theme.transferClipping(gc, gtkStyle);
	int xthickness = OS.gtk_style_get_xthickness(gtkStyle);
	int ythickness = OS.gtk_style_get_ythickness(gtkStyle);
	int x = bounds.x, y = bounds.y + headerHeight / 2, width = bounds.width, height = bounds.height - headerHeight / 2;
	byte[] detail = Converter.wcsToMbcs(null, "frame", true);
	int gap_x = xthickness + GROUP_X, gap_width = headerWidth + GROUP_PAD * 2, gap_side = OS.GTK_POS_TOP;
	int state_type = getStateType(DrawData.WIDGET_WHOLE);
	gtk_render_shadow_gap (gtkStyle, drawable, state_type, OS.GTK_SHADOW_ETCHED_IN, null, frameHandle, detail, x, y, width, height, gap_side, gap_x, gap_width);
	if (headerArea != null) {
		headerArea.x = bounds.x + gap_x + GROUP_PAD;
		headerArea.y = bounds.y;
		headerArea.width = headerWidth;
		headerArea.height = headerHeight;
	}
	if (clientArea != null) {
		clientArea.x = bounds.x + xthickness;
		clientArea.y = bounds.y + ythickness + headerHeight;
		clientArea.width = bounds.width - 2 * xthickness;
		clientArea.height = bounds.height - 2 * ythickness - headerHeight;
	}
}

int getStateType(int part) {
	int state = this.state[part];
	int state_type = OS.GTK_STATE_NORMAL;
	if ((state & DrawData.DISABLED) != 0) state_type = OS.GTK_STATE_INSENSITIVE;
	return state_type;
}

int hit(Theme theme, Point position, Rectangle bounds) {
   	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

void gtk_render_shadow_gap (long /*int*/ style, long /*int*/ window, int state_type, int shadow_type, GdkRectangle area, long /*int*/ widget, byte[] detail, int x , int y, int width, int height, int gap_side, int gap_x, int gap_width) {
	if (OS.GTK_VERSION >= OS.VERSION(3, 0, 0)) {
		long /*int*/ cairo = OS.gdk_cairo_create (window);
		long /*int*/ context = OS.gtk_widget_get_style_context (style);
		OS.gtk_style_context_save (context);
		OS.gtk_style_context_set_state (context, state_type);
		OS.gtk_render_frame_gap (context, cairo, context, y, gap_width, height, gap_side, gap_x, gap_x + gap_width);
		Cairo.cairo_destroy (cairo);
	} else {
		OS.gtk_paint_shadow_gap (style, window, state_type, shadow_type, area, widget, detail, x, y, width, height, gap_side, gap_x, gap_width);
	}
}
}

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

public class ExpanderDrawData extends DrawData {
	
public ExpanderDrawData() {
	state = new int[1];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	long /*int*/ treeHandle = theme.treeHandle;
	long /*int*/ gtkStyle = OS.gtk_widget_get_style (treeHandle);
	long /*int*/ drawable = gc.getGCData().drawable;
	theme.transferClipping(gc, gtkStyle);
	int state_type = getStateType(DrawData.WIDGET_WHOLE);
	int expander_style = OS.GTK_EXPANDER_COLAPSED;
	if ((this.style & SWT.DOWN) != 0) expander_style = OS.GTK_EXPANDER_EXPANDED;
	byte[] detail = Converter.wcsToMbcs(null, "treeview", true);
	int expander_size = theme.getWidgetProperty(treeHandle, "expander-size");
	int x = bounds.x + expander_size / 2;
	int y = bounds.y + expander_size / 2;
	if (OS.GTK_VERSION >= OS.VERSION(3, 0, 0)) {
		long /*int*/ cairo = OS.gdk_cairo_create (drawable);
		long /*int*/ context = OS.gtk_widget_get_style_context (gtkStyle);
		OS.gtk_render_expander (context, cairo, bounds.x, bounds.y, expander_size, expander_size);
		Cairo.cairo_destroy (cairo);
	} else {
		OS.gtk_paint_expander(gtkStyle, drawable, state_type, null, treeHandle, detail, x, y, expander_style);	
	}
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	long /*int*/ treeHandle = theme.treeHandle;
	int expander_size = theme.getWidgetProperty(treeHandle, "expander-size");
	if (new Rectangle(bounds.x, bounds.y, expander_size, expander_size).contains(position)) {
		return DrawData.WIDGET_WHOLE;
	}
	return DrawData.WIDGET_NOWHERE;
}

}

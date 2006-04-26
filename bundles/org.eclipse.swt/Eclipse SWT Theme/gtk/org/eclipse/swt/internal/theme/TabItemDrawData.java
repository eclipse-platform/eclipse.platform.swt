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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

public class TabItemDrawData extends DrawData {
	
	public TabFolderDrawData parent;
	public int position;

	static final int TAB_CURVATURE = 1;

public TabItemDrawData() {
	state = new int[1];
}

Rectangle computeTrim(Theme theme, GC gc) {
	int /*long*/ notebookHandle = theme.notebookHandle;
	int /*long*/ gtkStyle = OS.gtk_widget_get_style(notebookHandle);
	int hborder, vborder;
	if (OS.GTK_VERSION >= OS.VERSION(2, 4, 0)) {
		hborder = theme.getWidgetProperty(notebookHandle, "tab-hborder");
		vborder = theme.getWidgetProperty(notebookHandle, "tab-vborder");
	} else {
		hborder = 2;
		vborder = 2;
	}
	int focus_width = theme.getWidgetProperty(notebookHandle, "focus-line-width");
	int xthickness = OS.gtk_style_get_xthickness(gtkStyle);
	int ythickness = OS.gtk_style_get_ythickness(gtkStyle);
	int borderX = xthickness + TAB_CURVATURE + focus_width + hborder;
	int borderY = ythickness + TAB_CURVATURE + focus_width + vborder;
	int x = clientArea.x - borderX;
	int y = clientArea.y - borderY;
	int width = clientArea.width + 2 * borderX;
	int height = clientArea.height + 2 * borderY;
	return new Rectangle(x, y, width, height);
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	int /*long*/ notebookHandle = theme.notebookHandle;
	int /*long*/ gtkStyle = OS.gtk_widget_get_style (notebookHandle);
	int /*long*/ drawable = gc.getGCData().drawable;
	theme.transferClipping(gc, gtkStyle);
	int x = bounds.x, y = bounds.y, width = bounds.width, height = bounds.height;
	if ((state[DrawData.WIDGET_WHOLE] & DrawData.SELECTED) == 0) {
		if ((parent.style & SWT.BOTTOM) == 0) {
			y += TAB_CURVATURE;
		}
		height -= TAB_CURVATURE;
	}
	int gap_side = OS.GTK_POS_BOTTOM;
	if ((parent.style & SWT.BOTTOM) != 0) {
		gap_side = OS.GTK_POS_TOP;
	}
	int state_type = getStateType(DrawData.WIDGET_WHOLE);
	byte[] detail = Converter.wcsToMbcs(null, "tab", true);
	OS.gtk_paint_extension(gtkStyle, drawable, state_type, OS.GTK_SHADOW_OUT, null, notebookHandle, detail, x, y, width, height, gap_side);
	if (clientArea != null) {
		int hborder, vborder;
		if (OS.GTK_VERSION >= OS.VERSION(2, 4, 0)) {
			hborder = theme.getWidgetProperty(notebookHandle, "tab-hborder");
			vborder = theme.getWidgetProperty(notebookHandle, "tab-vborder");
		} else {
			hborder = 2;
			vborder = 2;
		}
		int focus_line_width = theme.getWidgetProperty(notebookHandle, "focus-line-width");
		int xthickness = OS.gtk_style_get_xthickness(gtkStyle);
		int ythickness = OS.gtk_style_get_ythickness(gtkStyle);
		int borderX = xthickness + TAB_CURVATURE + focus_line_width + hborder;
		int borderY = ythickness + TAB_CURVATURE + focus_line_width + vborder;
		clientArea.x = bounds.x + borderX;
		clientArea.y = bounds.y + borderY;
		clientArea.width = bounds.width - 2 * borderX;
		clientArea.height = bounds.height - 2 * borderY;
	}
}

int getStateType(int part) {
	int state = this.state[part];
	int state_type = OS.GTK_STATE_ACTIVE;
	if ((state & DrawData.SELECTED) != 0) state_type = OS.GTK_STATE_NORMAL;
	return state_type;
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

}

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

public class TabFolderDrawData extends DrawData {
	public int tabsWidth;
	public int tabsHeight;
	public Rectangle tabsArea;
	public int selectedX;
	public int selectedWidth;
	public int spacing;
	
public TabFolderDrawData() {
	state = new int[1];
	if (SWT.getPlatform().equals("gtk")) {
		spacing = -2;
	}
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	long /*int*/ notebookHandle = theme.notebookHandle;
	long /*int*/ gtkStyle = OS.gtk_widget_get_style (notebookHandle);
	long /*int*/ drawable = gc.getGCData().drawable;
	theme.transferClipping(gc, gtkStyle);
	int x = bounds.x, y = bounds.y, width = bounds.width, height = bounds.height;
	height -= tabsHeight;
	int gap_x = selectedX, gap_width = selectedWidth, gap_side = OS.GTK_POS_TOP;
	if ((style & SWT.BOTTOM) != 0) {
		gap_side = OS.GTK_POS_BOTTOM;
	} else {
		y += tabsHeight;
	}
	byte[] detail = Converter.wcsToMbcs(null, "notebook", true);
	OS.gtk_paint_box_gap(gtkStyle, drawable, getStateType(DrawData.WIDGET_WHOLE), OS.GTK_SHADOW_OUT, null, notebookHandle, detail, x, y, width, height, gap_side, gap_x, gap_width);
	if (tabsArea != null) {
		tabsArea.x = bounds.x;
		tabsArea.y = bounds.y;
		tabsArea.width = bounds.width;
		tabsArea.height = tabsHeight;
		if ((style & SWT.BOTTOM) != 0) {
			tabsArea.y += bounds.height - tabsHeight;
		}
	}
}

int getStateType(int part) {
	return OS.GTK_STATE_NORMAL;
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

}

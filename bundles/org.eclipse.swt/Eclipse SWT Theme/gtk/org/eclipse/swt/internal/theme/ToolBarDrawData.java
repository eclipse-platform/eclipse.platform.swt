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
import org.eclipse.swt.internal.gtk.*;

public class ToolBarDrawData extends DrawData {
	
public ToolBarDrawData() {
	state = new int[1];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	int /*long*/ toolbarHandle = theme.toolbarHandle;
	int /*long*/ gtkStyle = OS.gtk_widget_get_style (toolbarHandle);
	int /*long*/ drawable = gc.getGCData().drawable;
	theme.transferClipping(gc, gtkStyle);
	int x = bounds.x, y = bounds.y, width = bounds.width, height = bounds.height;
	byte[] detail = Converter.wcsToMbcs(null, "toolbar", true);
	OS.gtk_paint_box(gtkStyle, drawable, getStateType(DrawData.WIDGET_WHOLE), OS.GTK_SHADOW_NONE, null, toolbarHandle, detail, x, y, width, height);
	if (clientArea != null) {
		clientArea.x = bounds.x;
		clientArea.y = bounds.y;
		clientArea.width = bounds.width;
		clientArea.height = bounds.height;
	}
}

int getStateType(int part) {
	return OS.GTK_STATE_NORMAL;
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

}

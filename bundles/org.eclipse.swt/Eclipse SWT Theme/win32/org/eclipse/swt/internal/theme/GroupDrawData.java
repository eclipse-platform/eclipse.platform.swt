/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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
import org.eclipse.swt.internal.win32.*;

public class GroupDrawData extends DrawData {
	public int headerWidth;
	public int headerHeight;
	public Rectangle headerArea;
	
public GroupDrawData() {
	state = new int[1];
}

static final int GROUP_HEADER_X = 9;
static final int GROUP_HEADER_PAD = 2;
void draw(Theme theme, GC gc, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		long /*int*/ hTheme = OS.OpenThemeData(0, getClassId());
		RECT rect = new RECT ();
		rect.left = bounds.x;
		rect.right = bounds.x + bounds.width;
		rect.top = bounds.y + this.headerHeight / 2;
		rect.bottom = bounds.y + bounds.height;
		int headerX = bounds.x + GROUP_HEADER_X, headerY = bounds.y;
		int savedDC = OS.SaveDC(gc.handle);
		OS.ExcludeClipRect (gc.handle, headerX - GROUP_HEADER_PAD, headerY, headerX + this.headerWidth + GROUP_HEADER_PAD, headerY + this.headerHeight);
		int[] part = getPartId(DrawData.WIDGET_WHOLE);
		OS.DrawThemeBackground(hTheme, gc.handle, part[0], part[1], rect, null);
		OS.RestoreDC(gc.handle, savedDC);
		Rectangle headerArea = this.headerArea;
		if (headerArea != null) {
			headerArea.x = headerX;
			headerArea.y = headerY;
			headerArea.width = this.headerWidth;
			headerArea.height = this.headerHeight;
		}
		Rectangle clientArea = this.clientArea;			
		if (clientArea != null) {
			RECT contentRect = new RECT();
			OS.GetThemeBackgroundContentRect(hTheme, gc.handle, part[0], part[1], rect, contentRect);				
			clientArea.x = contentRect.left;
			clientArea.y = contentRect.top;
			clientArea.width = contentRect.right - contentRect.left;
			clientArea.height = contentRect.bottom - contentRect.top;
		}
		OS.CloseThemeData(hTheme);
	}
}

int[] getPartId(int part) {
	int state = this.state[part];
	int iPartId = OS.BP_GROUPBOX, iStateId = OS.GBS_NORMAL;			
	if ((state & DrawData.DISABLED) != 0) iStateId = OS.GBS_DISABLED;
	return new int[]{iPartId, iStateId};	
}

int hit(Theme theme, Point position, Rectangle bounds) {
   	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

}

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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

public class TabItemDrawData extends DrawData {

	public TabFolderDrawData parent;
	public int position;
	
	static final int TABITEM_INSET = 2;
	static final int TABITEM_INSET2 = 6;
	
public TabItemDrawData() {
	state = new int[1];
}

Rectangle computeTrim(Theme theme, GC gc) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		long /*int*/ hTheme = OS.OpenThemeData(0, getClassId());
		int x = clientArea.x, y = clientArea.y, width = clientArea.width, height = clientArea.height;
		if ((style & SWT.LEFT) != 0) {
			x -= TABITEM_INSET;
			width += TABITEM_INSET;
		}
		y -= TABITEM_INSET;
		height += TABITEM_INSET;
		RECT rect = new RECT ();
		rect.left = x;
		rect.right = x + width;
		rect.top = y;
		rect.bottom = y + height;
		RECT extent = new RECT ();
		int[] part = getPartId(DrawData.WIDGET_WHOLE);
		OS.GetThemeBackgroundExtent(hTheme, gc.handle, part[0], part[1], rect, extent);
		extent.left -= TABITEM_INSET2;
		extent.top -= TABITEM_INSET2;
		extent.right += TABITEM_INSET2;
		OS.CloseThemeData(hTheme);
		return new Rectangle(extent.left, extent.top, extent.right - extent.left, extent.bottom - extent.top);
	}
	return new Rectangle(0, 0, 0, 0);
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		int state = this.state[DrawData.WIDGET_WHOLE];
		long /*int*/ hTheme = OS.OpenThemeData(0, getClassId());
		int x = bounds.x, y = bounds.y, width = bounds.width, height = bounds.height;
		if ((position & SWT.LEFT) != 0) {
			x += TABITEM_INSET;
			width -= TABITEM_INSET;
		}
		y += TABITEM_INSET;
		height -= TABITEM_INSET;
		if ((state & DrawData.SELECTED) != 0) {
			//TODO - draws outside of bounds
			x -= TABITEM_INSET;
			y -= TABITEM_INSET;
			width += TABITEM_INSET * 2;
			height += TABITEM_INSET * 2;
		}
		RECT rect = new RECT ();
		rect.left = x;
		rect.right = x + width;
		rect.top = y;
		rect.bottom = y + height;
		int[] part = getPartId(DrawData.WIDGET_WHOLE);
		OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
		OS.CloseThemeData(hTheme);
		Rectangle clientArea = this.clientArea;
		if (clientArea != null) {
			RECT contentRect = new RECT();
			OS.GetThemeBackgroundContentRect(hTheme, gc.handle, part[0], part[1], rect, contentRect);
			clientArea.x = contentRect.left;
			clientArea.y = contentRect.top;
			clientArea.width = contentRect.right - contentRect.left;
			clientArea.height = contentRect.bottom - contentRect.top;
		}
	}
}

char[] getClassId() {
	return TAB;
}

int[] getPartId(int part) {
	int state = this.state[part];
	int iPartId = OS.TABP_TABITEM, iStateId = OS.TIS_NORMAL;
	if ((style & SWT.LEFT) != 0 && (style & SWT.RIGHT) != 0) {
		iPartId = OS.TABP_TABITEMLEFTEDGE;
	} else if ((style & SWT.LEFT) != 0) {
		iPartId = OS.TABP_TABITEMLEFTEDGE;
	} else if ((style & SWT.RIGHT) != 0) {
	}
	if ((state & DrawData.HOT) != 0) iStateId = OS.TIS_HOT;
	if ((state & DrawData.FOCUSED) != 0) iStateId = OS.TIS_FOCUSED;
	if ((state & DrawData.SELECTED) != 0) iStateId = OS.TIS_SELECTED;
	if ((state & DrawData.DISABLED) != 0) iStateId = OS.TIS_DISABLED;
	return new int[]{iPartId, iStateId};	
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	int style = this.style;
	int x = bounds.x, y = bounds.y, width = bounds.width, height = bounds.height;
	if ((style & SWT.LEFT) != 0) {
		x += TABITEM_INSET;
		width -= TABITEM_INSET;
	}
	y += TABITEM_INSET;
	height -= TABITEM_INSET;
	Rectangle content = new Rectangle(x, y, width, height);
	if (!content.contains(position)) return DrawData.WIDGET_NOWHERE;
	return DrawData.WIDGET_WHOLE;
}

}

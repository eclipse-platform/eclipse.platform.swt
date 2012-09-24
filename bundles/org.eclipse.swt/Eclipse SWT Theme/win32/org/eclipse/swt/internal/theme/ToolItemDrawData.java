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

public class ToolItemDrawData extends DrawData {

	public ToolBarDrawData parent;
	
	static final int INSET = 1;
	
public ToolItemDrawData() {
	state = new int[2];
}

Rectangle computeTrim(Theme theme, GC gc) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		long /*int*/ hTheme = OS.OpenThemeData(0, getClassId());
		RECT rect = new RECT ();
		rect.left = clientArea.x;
		rect.right = clientArea.x + clientArea.width;
		rect.top = clientArea.y;
		rect.bottom = clientArea.y + clientArea.height;
		RECT extent = new RECT ();
		int[] part = getPartId(DrawData.WIDGET_WHOLE);
		OS.GetThemeBackgroundExtent(hTheme, gc.handle, part[0], part[1], rect, extent);
		OS.CloseThemeData(hTheme);
		if ((style & SWT.DROP_DOWN) != 0) {
			SIZE size = new SIZE();
			part = getPartId(DrawData.TOOLITEM_ARROW);
			OS.GetThemePartSize(hTheme, 0, part[0], part[1], null, OS.TS_TRUE, size);
			extent.right = Math.max(extent.left, extent.right + size.cx);
		} else {
			extent.left -= INSET;
			extent.top -= INSET;
			extent.right += INSET;
			extent.bottom += INSET;
		}
		return new Rectangle(extent.left, extent.top, extent.right - extent.left, extent.bottom - extent.top);
	}
	return new Rectangle(0, 0, 0, 0);
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		long /*int*/ hTheme = OS.OpenThemeData(0, getClassId());
		RECT rect = new RECT ();
		rect.left = bounds.x;
		rect.right = bounds.x + bounds.width;
		rect.top = bounds.y;
		rect.bottom = bounds.y + bounds.height;
		SIZE size = null;
		int[] dropPart = null;
		if ((style & SWT.DROP_DOWN) != 0) {
			size = new SIZE();
			dropPart = getPartId(DrawData.TOOLITEM_ARROW);
			OS.GetThemePartSize(hTheme, gc.handle, dropPart[0], dropPart[1], rect, OS.TS_TRUE, size);
			rect.right -= size.cx;
			if (rect.right < rect.left) rect.right = rect.left;
		}
		int[] part = getPartId(DrawData.WIDGET_WHOLE);
		OS.DrawThemeBackground(hTheme, gc.handle, part[0], part[1], rect, null);
		Rectangle clientArea = this.clientArea;
		if (clientArea != null) {
			RECT contentRect = new RECT();
			OS.GetThemeBackgroundContentRect(hTheme, gc.handle, part[0], part[1], rect, contentRect);
			clientArea.x = contentRect.left;
			clientArea.y = contentRect.top;
			clientArea.width = contentRect.right - contentRect.left;
			clientArea.height = contentRect.bottom - contentRect.top;
		}
		if ((style & SWT.DROP_DOWN) != 0) {
			rect.left = rect.right;
			rect.right = rect.left + size.cx;
			OS.DrawThemeBackground(hTheme, gc.handle, dropPart[0], dropPart[1], rect, null);			
		}
		OS.CloseThemeData(hTheme);
	}
}

char[] getClassId() {
	return TOOLBAR;
}

int[] getPartId(int part) {
	int state = this.state[part];
	int iPartId = 0, iStateId = 0;
	switch (part) {
		case DrawData.WIDGET_WHOLE:
			if ((style & (SWT.PUSH | SWT.CHECK | SWT.RADIO)) != 0) {
				iPartId = OS.TP_BUTTON;
			} else if ((style & SWT.DROP_DOWN) != 0) {
				iPartId = OS.TP_SPLITBUTTON;
			} else if ((style & SWT.SEPARATOR) != 0) {
				if ((parent.style & SWT.VERTICAL) != 0) {
					iPartId = OS.TP_SEPARATORVERT;
				} else {
					iPartId = OS.TP_SEPARATOR;
				}
			}
			if ((style & SWT.SEPARATOR) == 0) {
				if ((state & DrawData.HOT) != 0) {
					if ((style & (SWT.RADIO | SWT.CHECK)) != 0 && (state & DrawData.SELECTED) != 0) {
						iStateId = OS.TS_HOTCHECKED;
					} else {
						iStateId = OS.TS_HOT;
					}
				}
				if ((style & (SWT.RADIO | SWT.CHECK)) != 0 && (state & DrawData.SELECTED) != 0) {
					iStateId = OS.TS_CHECKED;			
				}
				if ((state & DrawData.PRESSED) != 0) iStateId = OS.TS_PRESSED;
				if ((state & DrawData.DISABLED) != 0) iStateId = OS.TS_DISABLED;
			}
			break;
		case DrawData.TOOLITEM_ARROW:
			iPartId = OS.TP_SPLITBUTTONDROPDOWN;
			if ((state & DrawData.HOT) != 0) iStateId = OS.TS_HOT;
			if ((state & DrawData.PRESSED) != 0) iStateId = OS.TS_PRESSED;
			if ((state & DrawData.DISABLED) !=0) iStateId = OS.TS_DISABLED;
			break;
	}
	return new int[]{iPartId, iStateId};
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!(OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ())) return DrawData.WIDGET_NOWHERE;
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	long /*int*/ hTheme = OS.OpenThemeData(0, getClassId());
	try {
		RECT rect = new RECT ();
		rect.left = bounds.x;
		rect.right = bounds.x + bounds.width;
		rect.top = bounds.y;
		rect.bottom = bounds.y + bounds.height;
		POINT pt = new POINT();
		pt.x = position.x;
		pt.y = position.y;
		short[] code = new short[1];
		int[] part = getPartId(DrawData.WIDGET_WHOLE);
		OS.HitTestThemeBackground(hTheme, 0, part[0], part[1], 0, rect, 0, pt, code);
		if (code[0] == OS.HTNOWHERE) return DrawData.WIDGET_NOWHERE;
		int style = this.style;
		if ((style & SWT.DROP_DOWN) != 0) {
			SIZE size = new SIZE();
			part = getPartId(DrawData.TOOLITEM_ARROW);
			OS.GetThemePartSize(hTheme, 0, part[0], part[1], rect, OS.TS_TRUE, size);
			rect.left = Math.max(rect.left, rect.right - size.cx);
			OS.HitTestThemeBackground(hTheme, 0, part[0], part[1], 0, rect, 0, pt, code);
			if (code[0] != OS.HTNOWHERE) return DrawData.TOOLITEM_ARROW;
		}
	} finally {
		OS.CloseThemeData(hTheme);
	}
	return DrawData.WIDGET_WHOLE;
}

}

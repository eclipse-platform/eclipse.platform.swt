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

public class ComboDrawData extends DrawData {
	
public ComboDrawData() {
	state = new int[2];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		int /*long*/ hTheme = OS.OpenThemeData(0, EDIT);
		RECT rect = new RECT ();
		rect.left = bounds.x;
		rect.right = bounds.x + bounds.width;
		rect.top = bounds.y;
		rect.bottom = bounds.y + bounds.height;
		int[] part = getPartId(DrawData.WIDGET_WHOLE);
		OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
		RECT contentRect = new RECT();
		OS.GetThemeBackgroundContentRect(hTheme, gc.handle, part[0], part[1], rect, contentRect);				
		Rectangle clientArea = this.clientArea;
		if (clientArea != null) {
			clientArea.x = contentRect.left;
			clientArea.y = contentRect.top;
			clientArea.width = contentRect.right - contentRect.left;
			clientArea.height = contentRect.bottom - contentRect.top;
		}
		OS.CloseThemeData(hTheme);
		hTheme = OS.OpenThemeData(0, getClassId());
		int width = OS.GetThemeSysSize(hTheme, OS.SM_CXVSCROLL);
		rect.left = contentRect.right - width;
		rect.top = contentRect.top;
		rect.right = contentRect.right;
		rect.bottom = contentRect.bottom;
		part = getPartId(DrawData.COMBO_ARROW);
		OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
		OS.CloseThemeData(hTheme);
		if (clientArea != null) {
			clientArea.width -= width;
		}
	}
}

char[] getClassId() {
	return COMBOBOX;
}

int[] getPartId(int part) {
	int state = this.state[part];
	int iPartId = 0, iStateId = 0;
	switch (part) {
		case DrawData.WIDGET_WHOLE: 
			iPartId = OS.EP_EDITTEXT;
			iStateId = OS.ETS_NORMAL;
			if ((state & DrawData.DISABLED) != 0) iStateId = OS.ETS_DISABLED;
			break;
		case DrawData.COMBO_ARROW:
			iPartId = OS.CP_DROPDOWNBUTTON;
			iStateId = OS.CBXS_NORMAL;
			if ((state & DrawData.DISABLED) != 0) iStateId = OS.CBXS_DISABLED;
			if ((state & DrawData.HOT) != 0) iStateId = OS.CBXS_HOT;
			if ((state & DrawData.PRESSED) != 0) iStateId = OS.CBXS_PRESSED;
			break;
	}
	return new int[]{iPartId, iStateId};	
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!(OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ())) return DrawData.WIDGET_NOWHERE;
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	int /*long*/ hTheme = OS.OpenThemeData(0, EDIT);
	int[] part = getPartId(DrawData.WIDGET_WHOLE);
	int iPartId = part[0];
	int iStateId = part[1];
	RECT rect = new RECT ();
	rect.left = bounds.x;
	rect.right = bounds.x + bounds.width;
	rect.top = bounds.y;
	rect.bottom = bounds.y + bounds.height;
	RECT contentRect = new RECT();
	OS.GetThemeBackgroundContentRect(hTheme, 0, iPartId, iStateId, rect, contentRect);
	OS.CloseThemeData(hTheme);
	hTheme = OS.OpenThemeData(0, getClassId());
	int width = OS.GetThemeSysSize(hTheme, OS.SM_CXVSCROLL);
	OS.CloseThemeData(hTheme);
	Rectangle arrowRect = new Rectangle(contentRect.right - width, contentRect.top, contentRect.bottom - contentRect.top, width);
	if (arrowRect.contains(position)) return DrawData.COMBO_ARROW;
	return DrawData.WIDGET_WHOLE;
}

}

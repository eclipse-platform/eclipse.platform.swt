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

public class ButtonDrawData extends DrawData {
	
public ButtonDrawData() {
	state = new int[1];
}

int[] getPartId(int part) {
	int state = this.state[part];
	int style = this.style;
	int iPartId = 0, iStateId = 0;			
	if ((style & SWT.PUSH) != 0) {
		iPartId = OS.BP_PUSHBUTTON;
		iStateId = OS.PBS_NORMAL;
		if ((state & DrawData.DEFAULTED) != 0 && (state & DrawData.ACTIVE) != 0) iStateId = OS.PBS_DEFAULTED;
		if ((state & DrawData.HOT) != 0) iStateId = OS.PBS_HOT;
		if ((state & DrawData.PRESSED) != 0) iStateId = OS.PBS_PRESSED;
		if ((state & DrawData.DISABLED) != 0) iStateId = OS.PBS_DISABLED;
	}
	if ((style & SWT.RADIO) != 0) {
		iPartId = OS.BP_RADIOBUTTON;
	}
	if ((style & SWT.CHECK) != 0) {
		iPartId = OS.BP_CHECKBOX;
	}
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		if ((state & DrawData.SELECTED) != 0) {
			iStateId = OS.CBS_CHECKEDNORMAL;
			if ((state & DrawData.HOT) != 0) iStateId = OS.CBS_CHECKEDHOT;
			if ((state & DrawData.PRESSED) != 0) iStateId = OS.CBS_CHECKEDPRESSED;
			if ((state & DrawData.DISABLED) != 0) iStateId = OS.CBS_CHECKEDDISABLED;
		} else {
			iStateId = OS.CBS_UNCHECKEDNORMAL;
			if ((state & DrawData.HOT) != 0) iStateId = OS.CBS_UNCHECKEDHOT;
			if ((state & DrawData.PRESSED) != 0) iStateId = OS.CBS_UNCHECKEDPRESSED;
			if ((state & DrawData.DISABLED) != 0) iStateId = OS.CBS_UNCHECKEDDISABLED;
		}
	}
	return new int[]{iPartId, iStateId};
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		//TODO - arrow and toggle
		int /*long*/ hTheme = OS.OpenThemeData(0, getClassId());
		RECT rect = new RECT ();
		rect.left = bounds.x;
		rect.right = bounds.x + bounds.width;
		rect.top = bounds.y;
		rect.bottom = bounds.y + bounds.height;
		int[] part = getPartId(DrawData.WIDGET_WHOLE);
		if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
			SIZE size = new SIZE();
			OS.GetThemePartSize(hTheme, gc.handle, part[0], part[1], rect, 2, size);
			rect.right = rect.left + size.cx;
			OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);			
			rect.left = rect.right + 3;
			rect.right = rect.left + bounds.width - size.cx - 3;
		} else {
			OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
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

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!(OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ())) return DrawData.WIDGET_NOWHERE;
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	int /*long*/ hTheme = OS.OpenThemeData(0, getClassId());
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
	OS.CloseThemeData (hTheme);
	return code[0] == OS.HTNOWHERE ?  DrawData.WIDGET_NOWHERE : DrawData.WIDGET_WHOLE;
}

}

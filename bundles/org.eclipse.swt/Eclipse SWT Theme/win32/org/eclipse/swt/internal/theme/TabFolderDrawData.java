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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

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
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		int /*long*/ hTheme = OS.OpenThemeData(0, getClassId());
		RECT rect = new RECT ();
		rect.left = bounds.x;
		rect.right = bounds.x + bounds.width;
		rect.top = bounds.y;
		if ((style & SWT.BOTTOM) != 0) {
			rect.bottom = bounds.y + bounds.height - tabsHeight;
		} else {
			rect.top += tabsHeight;
			rect.bottom = bounds.y + bounds.height;
		}
		int[] part = getPartId(DrawData.WIDGET_WHOLE);
		OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
		OS.CloseThemeData(hTheme);
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
}

char[] getClassId() {
	return TAB;
}

int[] getPartId(int part) {
	int state = this.state[part];
	int iPartId = OS.TABP_PANE, iStateId = OS.TIS_NORMAL;
	if ((state & DrawData.DISABLED) != 0) {
		iStateId = OS.TIS_DISABLED;
	} else {
		if ((state & DrawData.HOT) != 0) iStateId = OS.TIS_HOT;
		if ((state & DrawData.SELECTED) != 0) iStateId = OS.TIS_SELECTED;
	}
	return new int[]{iPartId, iStateId};	
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	return DrawData.WIDGET_WHOLE;
}

}

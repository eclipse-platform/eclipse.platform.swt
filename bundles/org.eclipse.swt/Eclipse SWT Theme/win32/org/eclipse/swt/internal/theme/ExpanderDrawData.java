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

public class ExpanderDrawData extends DrawData {
	
public ExpanderDrawData() {
	state = new int[1];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		long /*int*/ hTheme = OS.OpenThemeData(0, getClassId());
		int iStateId = OS.GLPS_CLOSED;
		if ((this.style & SWT.DOWN) != 0) iStateId = OS.GLPS_OPENED;
		SIZE size = new SIZE();
		OS.GetThemePartSize(hTheme, gc.handle, OS.TVP_GLYPH, iStateId, null, OS.TS_TRUE, size);
		RECT rect = new RECT ();
		rect.left = bounds.x;
		rect.right = rect.left + size.cx;
		rect.top = bounds.y;
		rect.bottom = rect.top + size.cy;
		OS.DrawThemeBackground (hTheme, gc.handle, OS.TVP_GLYPH, iStateId, rect, null);		
		OS.CloseThemeData (hTheme);
	}
}

char[] getClassId() {
	return TREEVIEW;
}

int[] getPartId(int part) {
	int iPartId = OS.TVP_GLYPH;
	int iStateId = OS.GLPS_CLOSED;
	if ((this.style & SWT.DOWN) != 0) iStateId = OS.GLPS_OPENED;
	return new int[]{iPartId, iStateId};	
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!(OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ())) return DrawData.WIDGET_NOWHERE;
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	long /*int*/ hTheme = OS.OpenThemeData(0, getClassId());
	SIZE size = new SIZE();
	int[] part = getPartId(DrawData.WIDGET_WHOLE);
	OS.GetThemePartSize(hTheme, 0, part[0], part[1], null, OS.TS_TRUE, size);
	OS.CloseThemeData (hTheme);
	if (new Rectangle(bounds.x, bounds.y, size.cx, size.cy).contains(position)) {
		return DrawData.WIDGET_WHOLE;
	}
	return DrawData.WIDGET_NOWHERE;
}

}

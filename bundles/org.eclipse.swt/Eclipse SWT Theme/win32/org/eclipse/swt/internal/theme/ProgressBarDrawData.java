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

public class ProgressBarDrawData extends RangeDrawData {

public ProgressBarDrawData() {
	state = new int[1];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		int /*long*/ hTheme = OS.OpenThemeData(0, getClassId());
		RECT rect = new RECT ();
		rect.left = bounds.x;
		rect.right = rect.left + bounds.width;
		rect.top = bounds.y;
		rect.bottom = rect.top + bounds.height;
		int[] buffer = new int[1];
		OS.GetThemeInt(hTheme, 0, 0, OS.PROGRESSCHUNKSIZE, buffer);
		int chunkSize = buffer[0];
		OS.GetThemeInt(hTheme, 0, 0, OS.PROGRESSSPACESIZE, buffer);
		int spaceSize = buffer[0];		
		RECT content = new RECT();
		int[] part = getPartId(DrawData.WIDGET_WHOLE);
		if ((style & SWT.VERTICAL) != 0) {
			OS.GetThemeBackgroundContentRect(hTheme, gc.handle, part[0], part[1], rect, content);
			OS.DrawThemeBackground(hTheme, gc.handle, part[0], part[1], rect, null);
			int top = content.bottom - (((content.bottom - content.top) * (selection - minimum)) / (maximum - minimum));
			content.top = content.bottom - chunkSize;
			while (content.top >= top) {
				OS.DrawThemeBackground(hTheme, gc.handle, OS.PP_CHUNKVERT, 0, content, null);
				content.bottom -= chunkSize + spaceSize;
				content.top = content.bottom - chunkSize;
			}
			if (selection != 0) {
				OS.DrawThemeBackground(hTheme, gc.handle, OS.PP_CHUNKVERT, 0, content, null);
			}
		} else {
			OS.GetThemeBackgroundContentRect(hTheme, gc.handle, part[0], part[1], rect, content);
			OS.DrawThemeBackground(hTheme, gc.handle, part[0], part[1], rect, null);
			int right = content.left + (((content.right - content.left) * (selection - minimum)) / (maximum - minimum));
			content.right = content.left + chunkSize;
			while (content.right <= right) {
				OS.DrawThemeBackground(hTheme, gc.handle, OS.PP_CHUNK, 0, content, null);
				content.left += chunkSize + spaceSize;
				content.right = content.left + chunkSize;
			}
			if (selection != 0) {
				OS.DrawThemeBackground(hTheme, gc.handle, OS.PP_CHUNK, 0, content, null);				
			}
		}
		OS.CloseThemeData (hTheme);
	}
}

char[] getClassId() {
	return PROGRESS;
}

int[] getPartId(int part) {
	int iPartId = 0, iStateId = 0;	
	if ((style & SWT.VERTICAL) != 0) {
		iPartId = OS.PP_BARVERT;
	} else {
		iPartId = OS.PP_BAR;
	}
	return new int[]{iPartId, iStateId};	
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

}

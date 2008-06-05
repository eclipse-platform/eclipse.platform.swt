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

public class ScaleDrawData extends RangeDrawData {
	public int increment;
	public int pageIncrement;
	
	static final int TICS_MARGIN = 10;

public ScaleDrawData() {
	state = new int[4];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		// TODO - drawScale not done
		int style = this.style;
		int minimum = this.minimum;
		int maximum = this.maximum;
		int selection = this.selection;
		int pageIncrement = this.pageIncrement;
		int /*long*/ hTheme = OS.OpenThemeData(0, getClassId());
		RECT rect = new RECT ();
		rect.left = bounds.x;
		rect.right = rect.left + bounds.width;
		rect.top = bounds.y;
		rect.bottom = rect.top + bounds.height;
		SIZE size = new SIZE();
		if ((style & SWT.VERTICAL) != 0) {
			OS.GetThemePartSize(hTheme, gc.handle, OS.TKP_TRACKVERT, 0, null, OS.TS_TRUE, size);
			int trackWidth = size.cx - 1;
			OS.GetThemePartSize(hTheme, gc.handle, OS.TKP_THUMBVERT, 0, null, OS.TS_TRUE, size);
			int thumbWidth = size.cx, thumbHeight = size.cy;
			OS.GetThemePartSize(hTheme, gc.handle, OS.TKP_TICS, 0, rect, OS.TS_TRUE, size);
			int ticWidth = size.cx;
			int marginX = (thumbWidth - trackWidth) / 2;
			int marginY = marginX;
			marginX += TICS_MARGIN;
			rect.left += marginX;
			rect.top += marginY;
			rect.right = rect.left + trackWidth;
			rect.bottom -= marginY;
			int trackHeight = rect.bottom - rect.top;
			OS.DrawThemeBackground(hTheme, gc.handle, OS.TKP_TRACKVERT, 0, rect, null);
			rect.top += ((trackHeight - thumbHeight) * (selection - minimum)) / Math.max(1, maximum - minimum);
			rect.left -= (thumbWidth - trackWidth) / 2;
			rect.right = rect.left + thumbWidth;
			rect.bottom = rect.top + thumbHeight;
			OS.DrawThemeBackground(hTheme, gc.handle, OS.TKP_THUMBVERT, 0, rect, null);
			rect.top = bounds.y + marginY + thumbHeight / 2;
			rect.bottom = rect.top + 1;
			for (int sel = minimum; sel <= maximum; sel += pageIncrement) {
				rect.left = bounds.x + TICS_MARGIN / 2;
				rect.right = rect.left + ticWidth;
				if (sel != minimum && sel != maximum) rect.left++;
				rect.top = bounds.y + marginY + thumbHeight / 2;
				rect.top += ((trackHeight - thumbHeight) * (sel - minimum)) / Math.max(1, maximum - minimum);
				rect.bottom = rect.top + 1;
				//TODO - why tics are ot drawn
				OS.DrawThemeBackground(hTheme, gc.handle, OS.TKP_TICSVERT, 1, rect, null);
				gc.drawLine(rect.left, rect.top, rect.right, rect.top);
				rect.left = bounds.x + TICS_MARGIN + thumbWidth + 1;
				rect.right = rect.left + ticWidth;
				if (sel != minimum && sel != maximum) rect.right--;
				//TODO - why tics are ot drawn
				OS.DrawThemeBackground(hTheme, gc.handle, OS.TKP_TICSVERT, 1, rect, null);
				gc.drawLine(rect.left, rect.top, rect.right, rect.top);
			}
		} else {

		}
		OS.CloseThemeData (hTheme);
	}
}

char[] getClassId() {
	return TRACKBAR;
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

}

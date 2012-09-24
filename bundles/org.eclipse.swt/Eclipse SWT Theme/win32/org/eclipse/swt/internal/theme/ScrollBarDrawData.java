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

public class ScrollBarDrawData extends RangeDrawData {
	public int thumb;
	public int increment;
	public int pageIncrement;
	
public ScrollBarDrawData() {
	state = new int[6];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		long /*int*/ hTheme = OS.OpenThemeData(0, getClassId());
		RECT rect = new RECT ();
		if ((style & SWT.VERTICAL) != 0) {
			int width = OS.GetThemeSysSize(hTheme, OS.SM_CXVSCROLL);
			rect.left = bounds.x;
			rect.right = rect.left + bounds.width;
			rect.top = bounds.y;
			rect.bottom = rect.top + width;
			int[] part = getPartId(DrawData.SCROLLBAR_UP_ARROW);
			OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
			rect.bottom = bounds.y + bounds.height;
			rect.top = rect.bottom - width;
			part = getPartId(DrawData.SCROLLBAR_DOWN_ARROW);
			OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
			int totalWidth = bounds.height - 2 * width;
			int thumbWidth = Math.max(width / 2, (totalWidth * thumb) / Math.max(1, (maximum - minimum)));//BAD
			int thumbPos = bounds.y + width + Math.max(0, (totalWidth * selection) / Math.max(1, (maximum - minimum)));
			rect.top = bounds.y + width;		
			rect.bottom = thumbPos;
			part = getPartId(DrawData.SCROLLBAR_UP_TRACK);
			OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
			rect.top = rect.bottom;
			rect.bottom = rect.top + thumbWidth;
			part = getPartId(DrawData.SCROLLBAR_THUMB);
			OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
			OS.DrawThemeBackground (hTheme, gc.handle, OS.SBP_GRIPPERVERT, part[1], rect, null);
			rect.top = rect.bottom;		
			rect.bottom = bounds.y + bounds.height - width;
			part = getPartId(DrawData.SCROLLBAR_DOWN_TRACK);
			OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
		} else {
			//TODO - why SM_CXHSCROLL = 0?
			int height = OS.GetThemeSysSize(hTheme, OS.SM_CXVSCROLL);
			rect.top = bounds.y;
			rect.bottom = rect.top + bounds.height;
			rect.left = bounds.x;
			rect.right = rect.left + height;
			int[] part = getPartId(DrawData.SCROLLBAR_LEFT_ARROW);
			OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
			rect.right = bounds.x + bounds.width;
			rect.left = rect.right - height;
			part = getPartId(DrawData.SCROLLBAR_RIGHT_ARROW);
			OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
			int totalWidth = bounds.width - 2 * height;
			int thumbWidth = Math.max(height / 2, (totalWidth * thumb) / (maximum - minimum));//BAD
			int thumbPos = bounds.x + height + Math.max(0, (totalWidth * selection) / Math.max(1, (maximum - minimum)));
			rect.left = bounds.x + height;		
			rect.right = thumbPos;
			part = getPartId(DrawData.SCROLLBAR_UP_TRACK);
			OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
			rect.left = rect.right;
			rect.right = rect.left + thumbWidth;
			part = getPartId(DrawData.SCROLLBAR_THUMB);
			OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
			OS.DrawThemeBackground (hTheme, gc.handle, OS.SBP_GRIPPERHORZ, part[1], rect, null);
			rect.left = rect.right;		
			rect.right = bounds.x + bounds.width - height;
			part = getPartId(DrawData.SCROLLBAR_DOWN_TRACK);
			OS.DrawThemeBackground (hTheme, gc.handle, part[0], part[1], rect, null);
		}
		OS.CloseThemeData (hTheme);
	}
}

char[] getClassId() {
	return SCROLLBAR;
}

int[] getPartId(int part) {
	int iPartId = 0, iStateId = 0;
	int state = this.state[part];
	switch (part) {
		case DrawData.SCROLLBAR_UP_ARROW:
			iPartId = OS.SBP_ARROWBTN;
			if ((style & SWT.VERTICAL) != 0) {
				iStateId = OS.ABS_UPNORMAL;
				if ((state & DrawData.HOT) != 0) iStateId = OS.ABS_UPHOT;
				if ((state & DrawData.PRESSED) != 0) iStateId = OS.ABS_UPPRESSED;
				if ((state & DrawData.DISABLED) != 0) iStateId = OS.ABS_UPDISABLED;
			} else {
				iStateId = OS.ABS_LEFTNORMAL;
				if ((state & DrawData.HOT) != 0) iStateId = OS.ABS_LEFTHOT;
				if ((state & DrawData.PRESSED) != 0) iStateId = OS.ABS_LEFTPRESSED;
				if ((state & DrawData.DISABLED) != 0) iStateId = OS.ABS_LEFTDISABLED;				
			}
			break;
		case DrawData.SCROLLBAR_DOWN_ARROW:
			iPartId = OS.SBP_ARROWBTN;
			if ((style & SWT.VERTICAL) != 0) {
				iStateId = OS.ABS_DOWNNORMAL;
				if ((state & DrawData.HOT) != 0) iStateId = OS.ABS_DOWNHOT;
				if ((state & DrawData.PRESSED) != 0) iStateId = OS.ABS_DOWNPRESSED;
				if ((state & DrawData.DISABLED) != 0) iStateId = OS.ABS_DOWNDISABLED;
			} else {
				iStateId = OS.ABS_RIGHTNORMAL;
				if ((state & DrawData.HOT) != 0) iStateId = OS.ABS_RIGHTHOT;
				if ((state & DrawData.PRESSED) != 0) iStateId = OS.ABS_RIGHTPRESSED;
				if ((state & DrawData.DISABLED) != 0) iStateId = OS.ABS_RIGHTDISABLED;
			}
			break;
		case DrawData.WIDGET_WHOLE:
		case DrawData.SCROLLBAR_THUMB:
			if ((style & SWT.VERTICAL) != 0) {
				iPartId = OS.SBP_THUMBBTNVERT;
			} else {
				iPartId = OS.SBP_THUMBBTNHORZ;
			}
			break;
		case DrawData.SCROLLBAR_UP_TRACK:
			if ((style & SWT.VERTICAL) != 0) {
				iPartId = OS.SBP_UPPERTRACKVERT;
			} else {
				iPartId = OS.SBP_UPPERTRACKHORZ;
			}
			break;
		case DrawData.SCROLLBAR_DOWN_TRACK:
			if ((style & SWT.VERTICAL) != 0) {
				iPartId = OS.SBP_LOWERTRACKVERT;
			} else {
				iPartId = OS.SBP_LOWERTRACKHORZ;
			}
			break;
	}
	if (part != DrawData.SCROLLBAR_DOWN_ARROW && part != DrawData.SCROLLBAR_UP_ARROW) {
		iStateId = OS.SCRBS_NORMAL;
		if ((state & DrawData.HOT) != 0) iStateId = OS.SCRBS_HOT;
		if ((state & DrawData.PRESSED) != 0) iStateId = OS.SCRBS_PRESSED;
		if ((state & DrawData.DISABLED) != 0) iStateId = OS.SCRBS_DISABLED;
	}
	return new int[]{iPartId, iStateId};	
}

Rectangle getBounds(int part, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		long /*int*/ hTheme = OS.OpenThemeData(0, getClassId());
		if ((style & SWT.VERTICAL) != 0) {
			int width = OS.GetThemeSysSize(hTheme, OS.SM_CXVSCROLL);
			int totalWidth = bounds.height - 2 * width;
			int thumbWidth = Math.max(width / 2, (totalWidth * thumb) / Math.max(1, (maximum - minimum)));//BAD
			int thumbPos = bounds.y + width + Math.max(0, (totalWidth * selection) / Math.max(1, (maximum - minimum)));
			switch (part) {
				case DrawData.SCROLLBAR_DOWN_ARROW:
					return new Rectangle(bounds.x, bounds.y + bounds.height - width, bounds.width, width);
				case DrawData.SCROLLBAR_UP_ARROW:
					return new Rectangle(bounds.x, bounds.y, bounds.width, width);
				case DrawData.SCROLLBAR_UP_TRACK:
					return new Rectangle(bounds.x, bounds.y + width, bounds.width, thumbPos - bounds.y - width);
				case DrawData.SCROLLBAR_THUMB:
					return new Rectangle(bounds.x, thumbPos, bounds.width, thumbWidth);
				case DrawData.SCROLLBAR_DOWN_TRACK:
					return new Rectangle(bounds.x, thumbPos + thumbWidth, bounds.width, bounds.y + bounds.height - width - thumbPos - thumbWidth);
			}
		} else {
			
		}
		OS.CloseThemeData (hTheme);
	}
	return super.getBounds(part, bounds);
}

int getSelection(Point position, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		long /*int*/ hTheme = OS.OpenThemeData(0, getClassId());
		if ((style & SWT.VERTICAL) != 0) {
			int width = OS.GetThemeSysSize(hTheme, OS.SM_CXVSCROLL);			
			int totalWidth = bounds.height - 2 * width;
			int thumbPos = bounds.y + width + Math.max(0, (totalWidth * selection) / Math.max(1, (maximum - minimum)));
			thumbPos += position.y;
			int selection = ((thumbPos - bounds.y - width) * (maximum - minimum)) / totalWidth;
			return Math.max(0, Math.min(selection, maximum - thumb));
		} else {
			
		}
		OS.CloseThemeData (hTheme);
	}
	return 0;
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!(OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ())) return -1;
	long /*int*/ hTheme = OS.OpenThemeData(0, getClassId());
	//TODO - should we take a GC?
	int hDC = 0;
	RECT rect = new RECT ();
	POINT pt = new POINT();
	pt.x = position.x;
	pt.y = position.y;
	short[] code = new short[1];
	try {
		if ((style & SWT.VERTICAL) != 0) {
			int width = OS.GetThemeSysSize(hTheme, OS.SM_CXVSCROLL);
			rect.left = bounds.x;
			rect.right = rect.left + bounds.width;
			rect.top = bounds.y;
			rect.bottom = rect.top + width;
			int[] part = getPartId(DrawData.SCROLLBAR_UP_ARROW);
			OS.HitTestThemeBackground(hTheme, hDC, part[0], part[1], 0, rect, 0, pt, code);
			if (code[0] != OS.HTNOWHERE) return DrawData.SCROLLBAR_UP_ARROW;
			rect.bottom = bounds.y + bounds.height;
			rect.top = rect.bottom - width;
			part = getPartId(DrawData.SCROLLBAR_DOWN_ARROW);
			OS.HitTestThemeBackground(hTheme, hDC, part[0], part[1], 0, rect, 0, pt, code);
			if (code[0] != OS.HTNOWHERE) return DrawData.SCROLLBAR_DOWN_ARROW;
			int totalWidth = bounds.height - 2 * width;
			int thumbWidth = Math.max(width / 2, (totalWidth * thumb) / Math.max(1, (maximum - minimum)));//BAD
			int thumbPos = bounds.y + width + Math.max(0, (totalWidth * selection) / Math.max(1, (maximum - minimum)));
			rect.top = bounds.y + width;		
			rect.bottom = thumbPos;
			part = getPartId(DrawData.SCROLLBAR_THUMB);
			OS.HitTestThemeBackground(hTheme, hDC, part[0], part[1], 0, rect, 0, pt, code);
			if (code[0] != OS.HTNOWHERE) return DrawData.SCROLLBAR_UP_TRACK;
			rect.top = rect.bottom;
			rect.bottom = rect.top + thumbWidth;
			part = getPartId(DrawData.SCROLLBAR_UP_TRACK);
			OS.HitTestThemeBackground(hTheme, hDC, part[0], part[1], 0, rect, 0, pt, code);
			if (code[0] != OS.HTNOWHERE) return DrawData.SCROLLBAR_THUMB;
			rect.top = rect.bottom;		
			rect.bottom = bounds.y + bounds.height - width;
			part = getPartId(DrawData.SCROLLBAR_DOWN_TRACK);
			OS.HitTestThemeBackground(hTheme, hDC, part[0], part[1], 0, rect, 0, pt, code);
			if (code[0] != OS.HTNOWHERE) return DrawData.SCROLLBAR_DOWN_TRACK;
		} else {
			int height = OS.GetThemeSysSize(hTheme, OS.SM_CXVSCROLL);//BAD - why SM_CXHSCROLL = 0?
			rect.top = bounds.y;
			rect.bottom = rect.top + bounds.height;
			rect.left = bounds.x;
			rect.right = rect.left + height;
			int[] part = getPartId(DrawData.SCROLLBAR_LEFT_ARROW);
			OS.HitTestThemeBackground(hTheme, hDC, part[0], part[1], 0, rect, 0, pt, code);
			if (code[0] != OS.HTNOWHERE) return DrawData.SCROLLBAR_UP_ARROW;
			rect.right = bounds.x + bounds.width;
			rect.left = rect.right - height;
			part = getPartId(DrawData.SCROLLBAR_RIGHT_ARROW);
			OS.HitTestThemeBackground(hTheme, hDC, part[0], part[1], 0, rect, 0, pt, code);
			if (code[0] != OS.HTNOWHERE) return DrawData.SCROLLBAR_DOWN_ARROW;
			int totalWidth = bounds.width - 2 * height;
			int thumbWidth = Math.max(height / 2, (totalWidth * thumb) / (maximum - minimum));//BAD
			int thumbPos = bounds.x + height + Math.max(0, (totalWidth * selection) / Math.max(1, (maximum - minimum)));
			rect.left = bounds.x + height;		
			rect.right = thumbPos;
			part = getPartId(DrawData.SCROLLBAR_LEFT_TRACK);
			OS.HitTestThemeBackground(hTheme, hDC, part[0], part[1], 0, rect, 0, pt, code);
			if (code[0] != OS.HTNOWHERE) return DrawData.SCROLLBAR_UP_TRACK;
			rect.left = rect.right;
			rect.right = rect.left + thumbWidth;
			part = getPartId(DrawData.SCROLLBAR_THUMB);
			OS.HitTestThemeBackground(hTheme, hDC, part[0], part[1], 0, rect, 0, pt, code);
			if (code[0] != OS.HTNOWHERE) return DrawData.SCROLLBAR_THUMB;
			rect.left = rect.right;		
			rect.right = bounds.x + bounds.width - height;
			part = getPartId(DrawData.SCROLLBAR_RIGHT_TRACK);
			OS.HitTestThemeBackground(hTheme, hDC, part[0], part[1], 0, rect, 0, pt, code);
			if (code[0] != OS.HTNOWHERE) return DrawData.SCROLLBAR_DOWN_TRACK;
		}
	} finally {
		OS.CloseThemeData (hTheme);
	}
	return DrawData.WIDGET_NOWHERE;
}

}

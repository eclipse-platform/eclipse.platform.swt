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

public class DrawData {
	public int style;
	public int[] state;
	public Rectangle clientArea;

	/** Part states */
	public static final int SELECTED = 1 << 1;
	public static final int FOCUSED = 1 << 2;
	public static final int PRESSED = 1 << 3;
	public static final int ACTIVE = 1 << 4;
	public static final int DISABLED = 1 << 5;
	public static final int HOT = 1 << 6;
	public static final int DEFAULTED = 1 << 7;
	public static final int GRAYED = 1 << 8;
	
	/** Text and Image drawing flags */
	public static final int DRAW_LEFT = 1 << 4;
	public static final int DRAW_TOP = 1 << 5;
	public static final int DRAW_RIGHT = 1 << 6;
	public static final int DRAW_BOTTOM = 1 << 7;
	public static final int DRAW_HCENTER = 1 << 8;
	public static final int DRAW_VCENTER = 1 << 9;

	/** Widget parts */
	public static final int WIDGET_NOWHERE = -1;
	public static final int WIDGET_WHOLE = 0;

	/** Scrollbar parts */
	public static final int SCROLLBAR_UP_ARROW = 1;
	public static final int SCROLLBAR_DOWN_ARROW = 2;
	public static final int SCROLLBAR_LEFT_ARROW = SCROLLBAR_UP_ARROW;
	public static final int SCROLLBAR_RIGHT_ARROW = SCROLLBAR_DOWN_ARROW;
	public static final int SCROLLBAR_UP_TRACK = 3;
	public static final int SCROLLBAR_DOWN_TRACK = 4;
	public static final int SCROLLBAR_LEFT_TRACK = SCROLLBAR_UP_TRACK;
	public static final int SCROLLBAR_RIGHT_TRACK = SCROLLBAR_DOWN_TRACK;
	public static final int SCROLLBAR_THUMB = 5;
	
	/** Scale parts */
	public static final int SCALE_UP_TRACK = 1;
	public static final int SCALE_LEFT_TRACK = SCALE_UP_TRACK;
	public static final int SCALE_DOWN_TRACK = 2;
	public static final int SCALE_RIGHT_TRACK = SCALE_DOWN_TRACK;
	public static final int SCALE_THUMB = 3;
	
	/** ToolItem parts */
	public static final int TOOLITEM_ARROW = 1;
	
	/** Combo parts */
	public static final int COMBO_ARROW = 1;
	
	static final char [] EDIT = new char [] {'E', 'D', 'I', 'T', 0};
	static final char [] COMBOBOX = new char [] {'C', 'O', 'M', 'B', 'O', 'B', 'O', 'X', 0};
	static final char [] BUTTON = new char [] {'B', 'U', 'T', 'T', 'O', 'N', 0};
	static final char [] PROGRESS = new char [] {'P', 'R', 'O', 'G', 'R', 'E', 'S', 'S', 0};
	static final char [] SCROLLBAR = new char [] {'S', 'C', 'R', 'O', 'L', 'L', 'B', 'A', 'R', 0};
	static final char [] TAB = new char [] {'T', 'A', 'B', 0};
	static final char [] TRACKBAR = new char [] {'T', 'R', 'A', 'C', 'K', 'B', 'A', 'R', 0};
	static final char [] TOOLBAR = new char [] {'T', 'O', 'O', 'L', 'B', 'A', 'R', 0};
	static final char [] TREEVIEW = new char [] {'T', 'R', 'E', 'E', 'V', 'I', 'E', 'W', 0};

public DrawData() {
	state = new int[1];
}

Rectangle computeTrim(Theme theme, GC gc) {
	return new Rectangle(clientArea.x, clientArea.y, clientArea.width, clientArea.height);
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	
}

void drawImage(Theme theme, Image image, GC gc, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
//		int /*long*/ hTheme = OS.OpenThemeData(0, getClassId());
//		RECT rect = new RECT ();
//		rect.left = bounds.x;
//		rect.right = bounds.x + bounds.width;
//		rect.top = bounds.y;
//		rect.bottom = bounds.y + bounds.height;
//		//TODO - remove reference to widgets.
//		ImageList imageList = new ImageList(0);
//		int imageIndex = imageList.add(image);
//		int[] part = getPartId(DrawData.WIDGET_WHOLE);
//		OS.DrawThemeIcon(hTheme, gc.handle, part[0], part[1], rect, imageList.getHandle(), imageIndex);
//		imageList.dispose();
//		OS.CloseThemeData(hTheme);
		Rectangle rect = image.getBounds();
		gc.drawImage(image, 0, 0, rect.width, rect.height, bounds.x, bounds.y, bounds.width, bounds.height);
	}
}

void drawText(Theme theme, String text, int flags, GC gc, Rectangle bounds) { 
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		int /*long*/ hTheme = OS.OpenThemeData(0, getClassId());
		char[] chars = new char[text.length()];
		text.getChars(0, chars.length, chars, 0);
		int textFlags = OS.DT_SINGLELINE;
		if ((flags & DrawData.DRAW_LEFT) != 0) textFlags |= OS.DT_LEFT;
		if ((flags & DrawData.DRAW_HCENTER) != 0) textFlags |= OS.DT_CENTER;
		if ((flags & DrawData.DRAW_RIGHT) != 0) textFlags |= OS.DT_RIGHT;
		if ((flags & DrawData.DRAW_TOP) != 0) textFlags |= OS.DT_TOP;
		if ((flags & DrawData.DRAW_BOTTOM) != 0) textFlags |= OS.DT_BOTTOM;
		if ((flags & DrawData.DRAW_VCENTER) != 0) textFlags |= OS.DT_VCENTER;
		RECT rect = new RECT ();
		rect.left = bounds.x;
		rect.right = bounds.x + bounds.width;
		rect.top = bounds.y;
		rect.bottom = bounds.y + bounds.height;
		int[] part = getPartId(DrawData.WIDGET_WHOLE);
		int iPartId = part[0];
		int iStateId = part[1];
		OS.DrawThemeText(hTheme, gc.handle, iPartId, iStateId, chars, chars.length, textFlags, 0, rect);
		OS.CloseThemeData(hTheme);
	}
}

char[] getClassId() {
	return BUTTON;
}

int[] getPartId(int part) {
	return new int[]{0, 0};
}

Rectangle getBounds(int part, Rectangle bounds) {
	return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return -1;
}

Rectangle measureText(Theme theme, String text, int flags, GC gc, Rectangle bounds) {
	if (!(OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ())) return new Rectangle(0, 0, 0, 0);
	int /*long*/ hTheme = OS.OpenThemeData(0, getClassId());
	char[] chars = new char[text.length()];
	text.getChars(0, chars.length, chars, 0);
	//TODO - constant for VCENTER and flags
	int textFlags = 0;//OS.DT_VCENTER | OS.DT_SINGLELINE | OS.DT_CALCRECT;
	if ((style & SWT.LEFT) != 0) textFlags |= OS.DT_LEFT;
	if ((style & SWT.CENTER) != 0) textFlags |= OS.DT_CENTER;
	if ((style & SWT.RIGHT) != 0) textFlags |= OS.DT_RIGHT;
	RECT extent = new RECT();
	RECT rect = null;
	if (bounds != null) {
		rect = new RECT();
		rect.left = bounds.x;
		rect.right = bounds.x + bounds.width;
		rect.top = bounds.y;
		rect.bottom = bounds.y + bounds.height;
	}
	int[] part = getPartId(DrawData.WIDGET_WHOLE);
	int iPartId = part[0];
	int iStateId = part[1];
	OS.GetThemeTextExtent(hTheme, gc.handle, iPartId, iStateId, chars, chars.length, textFlags, rect, extent);
	OS.CloseThemeData(hTheme);
	return new Rectangle(extent.left, extent.top, extent.right - extent.left, extent.bottom - extent.top);
}

}

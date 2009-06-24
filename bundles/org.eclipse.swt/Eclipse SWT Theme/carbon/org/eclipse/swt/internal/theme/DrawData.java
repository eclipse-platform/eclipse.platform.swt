/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.HIThemeTextInfo;

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
	
public DrawData() {
	state = new int[1];
}

Rectangle computeTrim(Theme theme, GC gc) {
	return new Rectangle(clientArea.x, clientArea.y, clientArea.width, clientArea.height);
}

void draw(Theme theme, GC gc, Rectangle bounds) {
}

void drawImage(Theme theme, Image image, GC gc, Rectangle bounds) {
	Image drawImage = image;
	Rectangle rect = drawImage.getBounds();
	int state = this.state[DrawData.WIDGET_WHOLE];
	if (OS.VERSION >= 0x1040) {
		if ((state & (DrawData.PRESSED | DrawData.DISABLED)) != 0) {
			int transform = OS.kHITransformNone;
			if ((state & DrawData.DISABLED) != 0) {
				transform = OS.kHITransformDisabled;
			} else {
				if ((state & DrawData.PRESSED) != 0) {
					transform = OS.kHITransformSelected;
				}
			}
			if (transform != OS.kHITransformNone) {
				int[] buffer = new int[1];
				OS.HICreateTransformedCGImage(drawImage.handle, transform, buffer);
				if (buffer[0] != 0) {
					//TODO - get device
					//TODO - is data needed
					drawImage = Image.carbon_new(null, drawImage.type, buffer[0], 0);
				}
			}
		}
	}		
	gc.drawImage(drawImage, 0, 0, rect.width, rect.height, bounds.x, bounds.y, bounds.width, bounds.height);
	if (drawImage != image) {
		drawImage.dispose();
	}
}

void drawText(Theme theme, String text, int flags, GC gc, Rectangle bounds) {
	int state = this.state[DrawData.WIDGET_WHOLE];
	char[] chars = new char[text.length()];
	text.getChars(0, chars.length, chars, 0);
	int ptr = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, chars, chars.length);	
	OS.CGContextSaveGState(gc.handle);
	if ((state & DrawData.DISABLED) != 0) {
		//TODO - find out disable color
		OS.CGContextSetFillColor(gc.handle, new float[]{0.5f, 0.5f, 0.5f, 1});
	} else {
		if ((state & DrawData.ACTIVE) != 0) {
			OS.CGContextSetFillColor(gc.handle, new float[]{0, 0, 0, 1});
		} else {
			//TODO - find out inative color
			OS.CGContextSetFillColor(gc.handle, new float[]{0.6f, 0.6f, 0.6f, 1});					
		}
	}
	CGRect rect = new CGRect();
	rect.x = bounds.x;
	rect.y = bounds.y;
	rect.width = bounds.width;
	rect.height = bounds.height;
	HIThemeTextInfo info = getTextInfo(flags);
	OS.HIThemeDrawTextBox(ptr, rect, info, gc.handle, OS.kHIThemeOrientationNormal);
	OS.CGContextRestoreGState(gc.handle);
	OS.CFRelease(ptr);
}

Rectangle getBounds(int part, Rectangle bounds) {
	return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
}

int getFontId() {
	return OS.kThemeSmallSystemFont;
}

HIThemeTextInfo getTextInfo(int flags) {
	int state = this.state[DrawData.WIDGET_WHOLE];
	HIThemeTextInfo info = new HIThemeTextInfo();
	if ((state & DrawData.PRESSED) != 0) {
		info.state = OS.kThemeStatePressed;
	} else {
		if ((state & DrawData.ACTIVE) != 0) {
			info.state = (state & DrawData.DISABLED) == 0 ? OS.kThemeStateActive : OS.kThemeStateUnavailable;
		} else {
			info.state = (state & DrawData.DISABLED) == 0 ? OS.kThemeStateInactive : OS.kThemeStateUnavailableInactive;
		}
	}
	info.state = info.state;
	info.fontID = (short)getFontId();
	if ((flags & DrawData.DRAW_LEFT) != 0) info.horizontalFlushness = OS.kHIThemeTextHorizontalFlushLeft;
	if ((flags & DrawData.DRAW_HCENTER) != 0) info.horizontalFlushness = OS.kHIThemeTextHorizontalFlushCenter;
	if ((flags & DrawData.DRAW_RIGHT) != 0) info.horizontalFlushness = OS.kHIThemeTextHorizontalFlushRight;
	if ((flags & DrawData.DRAW_TOP) != 0) info.verticalFlushness = OS.kHIThemeTextVerticalFlushTop;
	if ((flags & DrawData.DRAW_VCENTER) != 0) info.verticalFlushness = OS.kHIThemeTextVerticalFlushCenter;
	if ((flags & DrawData.DRAW_BOTTOM) != 0) info.verticalFlushness = OS.kHIThemeTextVerticalFlushBottom;
	info.truncationMaxLines = 0;
	info.truncationPosition = 0;
	info.options = 0;
	return info;
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return -1;
}

Rectangle measureText(Theme theme, String text, int flags, GC gc, Rectangle bounds) {
	//TODO - decide if should take only width and return only width/height
	char[] chars = new char[text.length()];
	text.getChars(0, chars.length, chars, 0);
	int ptr = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, chars, chars.length);
	int width = bounds != null ? bounds.width : 0;
	float[] outWidth = new float[1], outHeight = new float[1];
	HIThemeTextInfo info = getTextInfo(flags);
	OS.HIThemeGetTextDimensions(ptr, width, info, outWidth, outHeight, null);
	OS.CFRelease(ptr);
	return new Rectangle(0, 0, (int)outWidth[0], (int)outHeight[0]);
}

}

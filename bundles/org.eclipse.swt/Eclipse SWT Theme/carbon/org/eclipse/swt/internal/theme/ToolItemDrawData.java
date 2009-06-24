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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.HIThemeSeparatorDrawInfo;
import org.eclipse.swt.internal.carbon.HIThemePopupArrowDrawInfo;

public class ToolItemDrawData extends DrawData {

	public ToolBarDrawData parent;
	
	static final int ARROW_WIDTH = 9;
	static final int INSET = 3;

public ToolItemDrawData() {
	state = new int[2];
}

Rectangle computeTrim(Theme theme, GC gc) {
	int x = clientArea.x - INSET;
	int y = clientArea.y - INSET;
	int width = clientArea.width + INSET * 2;
	int height = clientArea.height + INSET * 2;
	if ((style & SWT.DROP_DOWN) != 0) {
		width += ARROW_WIDTH;
	}
	return new Rectangle(x, y, width, height);
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	CGRect rect = new CGRect();
	rect.x = bounds.x;
	rect.y = bounds.y;
	rect.width = bounds.width;
	rect.height = bounds.height;
	if ((style & SWT.SEPARATOR) != 0) {
		HIThemeSeparatorDrawInfo info = getSeparatorInfo();
		OS.HIThemeDrawSeparator (rect, info, gc.handle, OS.kHIThemeOrientationNormal);
	} else {
		if ((state[DrawData.WIDGET_WHOLE] & DrawData.SELECTED) != 0) {
			OS.CGContextSaveGState (gc.handle);
			OS.CGContextSetFillColor (gc.handle, new float[]{0.025f, 0.025f, 0.025f, 0.025f});
			OS.CGContextFillRect (gc.handle, rect);
			OS.CGContextSetStrokeColor (gc.handle, new float[]{0.2f, 0.2f, 0.2f, 0.2f});
			rect.x += 0.5f;
			rect.y += 0.5f;
			rect.width -= 1;
			rect.height -= 1;
			OS.CGContextStrokeRect (gc.handle, rect);
			OS.CGContextRestoreGState (gc.handle);
		}
	}
	if (clientArea != null) {
		clientArea.x = bounds.x;
		clientArea.y = bounds.y;
		clientArea.width = bounds.width;
		clientArea.height = bounds.height;
	}
	if ((style & SWT.DROP_DOWN) != 0) {
		rect.y = bounds.y + rect.height / 2 - 1;
		rect.x = bounds.x + rect.width - ARROW_WIDTH;
		HIThemePopupArrowDrawInfo info = getArrowInfo();
		OS.HIThemeDrawPopupArrow (rect, info, gc.handle, OS.kHIThemeOrientationNormal);
		if (clientArea != null) {
			clientArea.width -= ARROW_WIDTH;
		}
	}
}

HIThemePopupArrowDrawInfo getArrowInfo() {
	HIThemePopupArrowDrawInfo info = new HIThemePopupArrowDrawInfo();
	int state = this.state[DrawData.TOOLITEM_ARROW];
	if ((state & DrawData.PRESSED) != 0) {
		info.state = OS.kThemeStatePressed;
	} else {
		if ((state & DrawData.ACTIVE) != 0) {
			info.state = (state & DrawData.DISABLED) == 0 ? OS.kThemeStateActive : OS.kThemeStateUnavailable;
		} else {
			info.state = (state & DrawData.DISABLED) == 0 ? OS.kThemeStateInactive : OS.kThemeStateUnavailableInactive;
		}
	}
	info.orientation = (short) OS.kThemeArrowDown;
	info.size = (short) OS.kThemeArrow5pt;
	return info;
}

HIThemeSeparatorDrawInfo getSeparatorInfo() {
	HIThemeSeparatorDrawInfo info = new HIThemeSeparatorDrawInfo();
	int state = this.state[DrawData.WIDGET_WHOLE];
	if ((state & DrawData.ACTIVE) != 0) {
		info.state = (state & DrawData.DISABLED) == 0 ? OS.kThemeStateActive : OS.kThemeStateUnavailable;
	} else {
		info.state = (state & DrawData.DISABLED) == 0 ? OS.kThemeStateInactive : OS.kThemeStateUnavailableInactive;
	}
	return info;
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	if ((style & SWT.DROP_DOWN) != 0) {
		if (bounds.x + bounds.width - ARROW_WIDTH <= position.x) {
			return DrawData.TOOLITEM_ARROW; 
		}
	}
	return DrawData.WIDGET_WHOLE;
}

}

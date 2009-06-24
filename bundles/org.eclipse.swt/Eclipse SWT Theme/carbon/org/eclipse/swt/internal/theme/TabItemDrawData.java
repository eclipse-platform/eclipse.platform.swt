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
import org.eclipse.swt.internal.carbon.HIThemeTabDrawInfo;
import org.eclipse.swt.internal.carbon.CGRect;

public class TabItemDrawData extends DrawData {

	public TabFolderDrawData parent;
	public int position;
	
public TabItemDrawData() {
	state = new int[1];
}

Rectangle computeTrim(Theme theme, GC gc) {
	int[] metric = new int[1];
	OS.GetThemeMetric(OS.kThemeMetricLargeTabCapsWidth, metric);
	int x = clientArea.x - metric[0];
	int y = clientArea.y - metric[0];
	int width = clientArea.width + metric[0] * 2;
	int height = clientArea.height + metric[0] * 2;
	return new Rectangle(x, y, width, height);
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	CGRect rect = new CGRect();
	rect.x = bounds.x;
	rect.y = bounds.y;
	rect.width = bounds.width;
	rect.height = bounds.height;
	CGRect labelRect = clientArea != null ? new CGRect() : null;
	HIThemeTabDrawInfo info = getInfo();
	OS.HIThemeDrawTab(rect, info, gc.handle, OS.kHIThemeOrientationNormal, labelRect);
	if (clientArea != null) {
		clientArea.x = (int)labelRect.x;
		clientArea.y = (int)labelRect.y;
		clientArea.width = (int)labelRect.width;
		clientArea.height = (int)labelRect.height;
	}
}

HIThemeTabDrawInfo getInfo() {
	int state = this.state[DrawData.WIDGET_WHOLE];
	HIThemeTabDrawInfo info = new HIThemeTabDrawInfo();
	info.version = 1;
	if ((state & DrawData.DISABLED) == 0) {
		if ((state & DrawData.ACTIVE) != 0) {
			if ((state & DrawData.SELECTED) != 0) {
				info.style = OS.kThemeTabFront;
			} else {
				if ((state & DrawData.PRESSED) != 0) {
					info.style = OS.kThemeTabNonFrontPressed;
				} else {
					info.style = OS.kThemeTabNonFront;					
				}
			}
		} else {
			if ((state & DrawData.SELECTED) != 0) {
				info.style = OS.kThemeTabFrontInactive;				
			} else {
				info.style = OS.kThemeTabNonFrontInactive;	
			}
		}
	} else {
		if ((state & DrawData.ACTIVE) != 0) {
			info.style = OS.kThemeTabFrontUnavailable;
		} else {
			info.style = OS.kThemeTabNonFrontUnavailable;				
		}
	}
	info.direction = OS.kThemeTabNorth;
	if ((parent.style & SWT.BOTTOM) != 0) info.direction = OS.kThemeTabSouth;
	if ((parent.style & SWT.TOP) != 0) info.direction = OS.kThemeTabNorth;
	info.size = 0;
	if ((state & DrawData.FOCUSED) != 0) info.adornment = OS.kHIThemeTabAdornmentFocus;
	info.adornment |= OS.kHIThemeTabAdornmentTrailingSeparator;
	info.position = OS.kHIThemeTabPositionMiddle;	
	if ((position & SWT.RIGHT) != 0 && (position & SWT.LEFT) != 0) {
		info.position = OS.kHIThemeTabPositionOnly;
	} else if ((position & SWT.LEFT) != 0) {
		info.position = OS.kHIThemeTabPositionFirst;
		info.adornment |= OS.kHIThemeTabAdornmentTrailingSeparator;
	} else if ((position & SWT.RIGHT) != 0) {
		info.position = OS.kHIThemeTabPositionLast;
	}
	return info;
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	return DrawData.WIDGET_WHOLE;
}

}

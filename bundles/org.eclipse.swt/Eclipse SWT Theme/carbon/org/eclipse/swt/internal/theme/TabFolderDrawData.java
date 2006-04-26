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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.HIThemeTabPaneDrawInfo;
import org.eclipse.swt.internal.carbon.CGRect;

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
	int[] metric = new int[1];
	OS.GetThemeMetric(OS.kThemeMetricLargeTabHeight, metric);
	int tabHeight = metric[0];
	OS.GetThemeMetric(OS.kThemeMetricLargeTabCapsWidth, metric);
	int capsWidth = metric[0];
	CGRect rect = new CGRect();
	rect.x = bounds.x;
	rect.y = bounds.y;
	rect.width = bounds.width;
	rect.height = bounds.height;
	if ((style & SWT.BOTTOM) != 0) {
		rect.height -= tabHeight / 2;
	} else {		
		rect.y += (capsWidth + tabHeight) / 2;
		rect.height  -= (capsWidth + tabHeight) / 2;
	}
	HIThemeTabPaneDrawInfo info = getInfo();
	OS.HIThemeDrawTabPane(rect, info, gc.handle, OS.kHIThemeOrientationNormal);
	if (tabsArea != null) {
		tabsArea.x = bounds.x + capsWidth + ((bounds.width - capsWidth * 2) - tabsWidth) / 2;
		tabsArea.y = bounds.y;
		tabsArea.width = bounds.width - capsWidth * 2;
		tabsArea.height = tabHeight;
		if ((style & SWT.BOTTOM) != 0) {
			tabsArea.y += bounds.height - tabHeight;
		} else {
			tabsArea.y += capsWidth / 2;
		}
	}
}

HIThemeTabPaneDrawInfo getInfo() {
	int state = this.state[DrawData.WIDGET_WHOLE];
	HIThemeTabPaneDrawInfo info = new HIThemeTabPaneDrawInfo();
	info.version = 1;
	info.direction = OS.kThemeTabNorth;
	if ((style & SWT.BOTTOM) != 0) info.direction = OS.kThemeTabSouth;
	if ((style & SWT.TOP) != 0) info.direction = OS.kThemeTabNorth;
	if ((state & DrawData.PRESSED) != 0) {
		info.state = OS.kThemeStatePressed;
	} else {
		if ((state & DrawData.ACTIVE) != 0) {
			info.state = (state & DrawData.DISABLED) == 0 ? OS.kThemeStateActive : OS.kThemeStateUnavailable;
		} else {
			info.state = (state & DrawData.DISABLED) == 0 ? OS.kThemeStateInactive : OS.kThemeStateUnavailableInactive;
		}
	}
	return info;
}


int hit(Theme theme, Point position, Rectangle bounds) {
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	return DrawData.WIDGET_WHOLE;
}

}

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
import org.eclipse.swt.internal.carbon.HIThemeButtonDrawInfo;

public class ExpanderDrawData extends DrawData {
	
public ExpanderDrawData() {
	state = new int[1];
}

HIThemeButtonDrawInfo getInfo() {
	int state = this.state[DrawData.WIDGET_WHOLE];
	HIThemeButtonDrawInfo info = new HIThemeButtonDrawInfo();
	info.version = 0;
	info.kind = OS.kThemeDisclosureTriangle;
	if ((style & SWT.DOWN) != 0) {
		info.value = OS.kThemeButtonOn;
	} else {
		info.value = OS.kThemeButtonOff;
	}
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

void draw(Theme theme, GC gc, Rectangle bounds) {
	HIThemeButtonDrawInfo info = getInfo();
	CGRect rect = new CGRect();
	rect.x = bounds.x;
	rect.y = bounds.y;
	rect.width = bounds.width;
	rect.height = bounds.height;
	CGRect backRect = new CGRect();
	OS.HIThemeGetButtonBackgroundBounds(rect, info, backRect);
	rect.x += (rect.x - backRect.x);
	rect.y += (rect.y - backRect.y);
	int[] metric = new int[1];
	OS.GetThemeMetric(OS.kThemeMetricDisclosureTriangleWidth, metric);
	rect.width = metric[0];
	OS.GetThemeMetric(OS.kThemeMetricDisclosureTriangleHeight, metric);
	rect.height = metric[0];		 
	OS.HIThemeDrawButton(rect, info, gc.handle, OS.kHIThemeOrientationNormal, null);
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	int[] metric = new int[1];
	OS.GetThemeMetric(OS.kThemeMetricDisclosureTriangleWidth, metric);
	int width = metric[0];
	OS.GetThemeMetric(OS.kThemeMetricDisclosureTriangleHeight, metric);
	int height = metric[0];
	if (new Rectangle(bounds.x, bounds.y, width, height).contains(position)) return DrawData.WIDGET_WHOLE;
	return DrawData.WIDGET_NOWHERE;
}

}

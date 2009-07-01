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
import org.eclipse.swt.internal.carbon.HIThemeGroupBoxDrawInfo;

public class GroupDrawData extends DrawData {
	public int headerWidth;
	public int headerHeight;
	public Rectangle headerArea;
	
public GroupDrawData() {
	state = new int[1];
}

HIThemeGroupBoxDrawInfo getInfo () {
	int state = this.state[DrawData.WIDGET_WHOLE];
	HIThemeGroupBoxDrawInfo info = new HIThemeGroupBoxDrawInfo();
	info.version = 0;
	info.kind = OS.kHIThemeGroupBoxKindPrimary;
	if ((style & SWT.READ_ONLY) != 0) info.kind = OS.kThemePopupButton;
	if ((state & DrawData.DISABLED) != 0) {
		info.state = (state & DrawData.ACTIVE) != 0 ? OS.kThemeStateUnavailable : OS.kThemeStateUnavailableInactive;
	} else {
		if ((state & DrawData.PRESSED) != 0) {
			info.state = OS.kThemeStatePressed;
		} else {
			info.state = (state & DrawData.ACTIVE) != 0 ? OS.kThemeStateActive : OS.kThemeStateInactive;
		}
	}
	return info;
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	HIThemeGroupBoxDrawInfo info = getInfo();
	int headerHeight = this.headerHeight + 1;
	CGRect rect = new CGRect();
	rect.x = bounds.x;
	rect.y = bounds.y + headerHeight;
	rect.width = bounds.width;
	rect.height = bounds.height - headerHeight;
	OS.HIThemeDrawGroupBox(rect, info, gc.handle, OS.kHIThemeOrientationNormal);
	int[] metric = new int[1];
	if (headerArea != null) {
		OS.GetThemeMetric(OS.kThemeMetricLargeTabCapsWidth, metric);
		int capsWidth = metric[0];
		headerArea.x = bounds.x + capsWidth;
		headerArea.y = bounds.y;
		headerArea.width = bounds.width - capsWidth * 2;
		headerArea.height = this.headerHeight;
	}
	if (clientArea != null) {
		OS.GetThemeMetric(OS.kThemeMetricPrimaryGroupBoxContentInset, metric);
		int inset = metric[0];
		clientArea.x = bounds.x + inset;
		clientArea.y = bounds.y + inset + headerHeight;
		clientArea.width = bounds.width - (2 * inset);
		clientArea.height = bounds.height - (2 * inset) - headerHeight;
	}
}

int hit(Theme theme, Point position, Rectangle bounds) {
   	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

}

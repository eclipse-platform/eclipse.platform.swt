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

public class ComboDrawData extends DrawData {
	
public ComboDrawData() {
	state = new int[2];
}

HIThemeButtonDrawInfo getInfo() {
	int state = this.state[DrawData.WIDGET_WHOLE];
	HIThemeButtonDrawInfo info = new HIThemeButtonDrawInfo();
	info.version = 0;
	info.kind = OS.kThemeComboBox;
	if ((style & SWT.READ_ONLY) != 0) info.kind = OS.kThemePopupButton;
	if ((state & DrawData.DISABLED) != 0) {
		info.state = (state & DrawData.ACTIVE) != 0 ? OS.kThemeStateUnavailable : OS.kThemeStateUnavailableInactive;
	} else {
		if (((style & SWT.READ_ONLY) != 0 && (state & DrawData.PRESSED) != 0) || ((style & SWT.READ_ONLY) == 0 && (this.state[DrawData.COMBO_ARROW] & DrawData.PRESSED) != 0)) {
			info.state = OS.kThemeStatePressed;
		} else {
			info.state = (state & DrawData.ACTIVE) != 0 ? OS.kThemeStateActive : OS.kThemeStateInactive;
		}
	}
	if ((state & DrawData.FOCUSED) != 0)	info.adornment |= OS.kThemeAdornmentFocus;
	return info;
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	HIThemeButtonDrawInfo info = getInfo();
	CGRect rect = new CGRect();
	rect.x = bounds.x;
	rect.y = bounds.y;
	rect.width = bounds.width;
	rect.height = bounds.height;
	int inset = 0, arrowWidth = 0;
	if ((style & SWT.READ_ONLY) == 0) {
		int[] metric = new int[1];
		OS.GetThemeMetric(OS.kThemeMetricFocusRectOutset, metric);
		inset = metric[0];
		OS.GetThemeMetric(OS.kThemeMetricComboBoxLargeDisclosureWidth, metric);
		arrowWidth = metric[0];
		rect.x += inset;
		rect.y += inset;
		rect.width -= 2 * inset;
		rect.height -= 2 * inset;
	} else {		
		CGRect backRect = new CGRect();
		OS.HIThemeGetButtonBackgroundBounds(rect, info, backRect);
		rect.x += (rect.x - backRect.x);
		rect.y += (rect.y - backRect.y);
		rect.width -= (backRect.width - rect.width);
		rect.height -= (backRect.height - rect.height);
	}
	CGRect labelRect = clientArea != null && (style & SWT.READ_ONLY) != 0 ? new CGRect() : null;
	OS.HIThemeDrawButton(rect, info, gc.handle, OS.kHIThemeOrientationNormal, labelRect);
	if (clientArea != null) {
		if ((style & SWT.READ_ONLY) != 0) {
			clientArea.x = (int)labelRect.x;
			clientArea.y = (int)labelRect.y;
			clientArea.width = (int)labelRect.width;
			clientArea.height = (int)labelRect.height;
		} else {
			clientArea.x = (int)bounds.x + inset;
			clientArea.y = (int)bounds.y + inset;
			clientArea.width = (int)bounds.width - (2 * inset) - arrowWidth;
			clientArea.height = (int)bounds.height - (2 * inset);
		}
	}
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	if ((style & SWT.READ_ONLY) == 0) {
		int[] metric = new int[1];
		OS.GetThemeMetric(OS.kThemeMetricFocusRectOutset, metric);
		int inset = metric[0];
		OS.GetThemeMetric(OS.kThemeMetricComboBoxLargeDisclosureWidth, metric);
		int arrowWidth = metric[0];
		Rectangle arrowRect = new Rectangle(bounds.x + bounds.width - inset - arrowWidth, bounds.y - inset, arrowWidth, bounds.height - inset * 2);
		if (arrowRect.contains(position)) return DrawData.COMBO_ARROW;
	}
	return DrawData.WIDGET_WHOLE;
}

}

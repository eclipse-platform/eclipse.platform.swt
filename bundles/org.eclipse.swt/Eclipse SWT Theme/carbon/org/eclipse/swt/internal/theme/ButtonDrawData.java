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
import org.eclipse.swt.internal.carbon.HIThemeButtonDrawInfo;
import org.eclipse.swt.internal.carbon.CGRect;

public class ButtonDrawData extends DrawData {
	
public ButtonDrawData() {
	state = new int[1];
}

HIThemeButtonDrawInfo getInfo () {
	int state = this.state[DrawData.WIDGET_WHOLE];
	HIThemeButtonDrawInfo info = new HIThemeButtonDrawInfo();
	info.version = 0;
	if ((style & SWT.PUSH) != 0) info.kind = OS.kThemePushButton;
	if ((style & SWT.CHECK) != 0) info.kind = OS.kThemeCheckBox;
	if ((style & SWT.RADIO) != 0) info.kind = OS.kThemeRadioButton;
	if ((state & DrawData.SELECTED) != 0) {
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
	if ((state & DrawData.DEFAULTED) != 0) info.adornment |= OS.kThemeAdornmentDefault;
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
	CGRect backRect = new CGRect();
	OS.HIThemeGetButtonBackgroundBounds(rect, info, backRect);
	rect.x += (rect.x - backRect.x);
	rect.y += (rect.y - backRect.y);
	rect.width -= (backRect.width - rect.width);
	rect.height -= (backRect.height - rect.height);	
	CGRect labelRect = clientArea != null ? new CGRect() : null;
	OS.HIThemeDrawButton(rect, info, gc.handle, OS.kHIThemeOrientationNormal, labelRect);
	if (clientArea != null) {
		clientArea.x = (int)labelRect.x;
		clientArea.y = (int)labelRect.y;
		clientArea.width = (int)labelRect.width;
		clientArea.height = (int)labelRect.height;
	}
}

int hit(Theme theme, Point position, Rectangle bounds) {
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
	rect.width -= (backRect.width - rect.width);
	rect.height -= (backRect.height - rect.height);
	return new Rectangle((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height).contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

}

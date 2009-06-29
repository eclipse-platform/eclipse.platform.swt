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
import org.eclipse.swt.internal.carbon.HIThemeTrackDrawInfo;
import org.eclipse.swt.internal.carbon.ProgressTrackInfo;

public class ProgressBarDrawData extends RangeDrawData {

public ProgressBarDrawData() {
	state = new int[1];
}

HIThemeTrackDrawInfo getInfo() {
	int state = this.state[DrawData.WIDGET_WHOLE];
	HIThemeTrackDrawInfo info = new HIThemeTrackDrawInfo();
	info.min = minimum;
	info.max = maximum;
	info.value = selection;	
	info.kind = OS.kThemeProgressBarMedium;
	info.attributes = OS.kThemeTrackShowThumb;
	if ((style & SWT.HORIZONTAL) != 0) info.attributes |= OS.kThemeTrackHorizontal;
	info.enableState = OS.kThemeTrackInactive;
	if ((state & DrawData.ACTIVE) != 0) info.enableState = OS.kThemeTrackActive;
	if ((state & DrawData.DISABLED) != 0) info.enableState = OS.kThemeTrackDisabled;
	info.progress = new ProgressTrackInfo();
	return info;
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	HIThemeTrackDrawInfo info = getInfo();
	info.bounds_x = bounds.x;
	info.bounds_y = bounds.y;
	info.bounds_width = bounds.width;
	info.bounds_height = bounds.height;
	info.progress = new ProgressTrackInfo();
	OS.HIThemeDrawTrack(info, null, gc.handle, OS.kHIThemeOrientationNormal);
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

}

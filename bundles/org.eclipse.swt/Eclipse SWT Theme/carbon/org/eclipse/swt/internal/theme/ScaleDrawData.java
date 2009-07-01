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
import org.eclipse.swt.internal.carbon.CGPoint;
import org.eclipse.swt.internal.carbon.HIThemeTrackDrawInfo;
import org.eclipse.swt.internal.carbon.SliderTrackInfo;

public class ScaleDrawData extends RangeDrawData {
	public int increment;
	public int pageIncrement;
	
public ScaleDrawData() {
	state = new int[4];
}

HIThemeTrackDrawInfo getInfo() {
	int upTrackState = this.state[DrawData.SCALE_UP_TRACK];
	int downTrackState = this.state[DrawData.SCALE_DOWN_TRACK];
	int thumbState = this.state[DrawData.SCALE_THUMB];
	HIThemeTrackDrawInfo info = new HIThemeTrackDrawInfo();
	info.min = minimum;
	info.max = Math.max(minimum, maximum);
	info.value = selection;	
	info.kind = OS.kThemeSliderMedium;
	info.attributes = OS.kThemeTrackShowThumb;
	if ((style & SWT.HORIZONTAL) != 0) info.attributes |= OS.kThemeTrackHorizontal;
	info.enableState = OS.kThemeTrackInactive;
	if ((this.state[DrawData.WIDGET_WHOLE] & DrawData.ACTIVE) != 0) info.enableState = OS.kThemeTrackActive;
	if ((this.state[DrawData.WIDGET_WHOLE] & DrawData.DISABLED) != 0) info.enableState = OS.kThemeTrackDisabled;
	info.slider = new SliderTrackInfo();
	int state = 0;
	if ((upTrackState & DrawData.PRESSED) != 0) state |= OS.kThemeLeftInsideArrowPressed;
	if ((downTrackState & DrawData.PRESSED) != 0) state |= OS.kThemeRightInsideArrowPressed;
	if ((thumbState & DrawData.PRESSED) != 0) state |= OS.kThemeThumbPressed;
	info.slider.pressState = (byte)state;
	return info;
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	HIThemeTrackDrawInfo info = getInfo();
	info.bounds_x = bounds.x;
	info.bounds_y = bounds.y;
	info.bounds_width = bounds.width;
	info.bounds_height = bounds.height;
	OS.HIThemeDrawTrack(info, null, gc.handle, OS.kHIThemeOrientationNormal);
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	CGPoint pt = new CGPoint();
	pt.x = position.x;
	pt.y = position.y;
	short[] part = new short[1];
	HIThemeTrackDrawInfo info = getInfo();
	if (OS.HIThemeHitTestTrack(info, pt, part)) {
		switch (part[0]) {
			case 22: return DrawData.SCALE_UP_TRACK;
			case 23: return DrawData.SCALE_DOWN_TRACK;
			case 129: return DrawData.SCALE_THUMB;
		}
	}
	return DrawData.WIDGET_NOWHERE;
}

}

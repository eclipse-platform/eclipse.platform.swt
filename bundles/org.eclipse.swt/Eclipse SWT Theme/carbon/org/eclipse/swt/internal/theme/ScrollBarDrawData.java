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
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.HIThemeTrackDrawInfo;
import org.eclipse.swt.internal.carbon.ScrollBarTrackInfo;
import org.eclipse.swt.internal.carbon.HIScrollBarTrackInfo;

public class ScrollBarDrawData extends RangeDrawData {
	public int thumb;
	public int increment;
	public int pageIncrement;
	
public ScrollBarDrawData() {
	state = new int[6];
}

HIScrollBarTrackInfo getArrowsInfo() {
	int upArrowState = this.state[DrawData.SCROLLBAR_UP_ARROW];
	int downArrowState = this.state[DrawData.SCROLLBAR_DOWN_ARROW];
	int upTrackState = this.state[DrawData.SCROLLBAR_UP_TRACK];
	int downTrackState = this.state[DrawData.SCROLLBAR_DOWN_TRACK];
	int thumbState = this.state[DrawData.SCROLLBAR_THUMB];
	int state = 0;
	if ((upArrowState & DrawData.PRESSED) != 0) state |= OS.kThemeLeftOutsideArrowPressed;
	if ((downArrowState & DrawData.PRESSED) != 0) state |= OS.kThemeRightOutsideArrowPressed;
	if ((upTrackState & DrawData.PRESSED) != 0) state |= OS.kThemeLeftInsideArrowPressed;
	if ((downTrackState & DrawData.PRESSED) != 0) state |= OS.kThemeRightInsideArrowPressed;
	if ((thumbState & DrawData.PRESSED) != 0) state |= OS.kThemeThumbPressed;
	int enableState = OS.kThemeTrackInactive;
	if ((this.state[DrawData.WIDGET_WHOLE] & DrawData.ACTIVE) != 0) enableState = OS.kThemeTrackActive;
	if ((this.state[DrawData.WIDGET_WHOLE] & DrawData.DISABLED) != 0) enableState = OS.kThemeTrackDisabled;
	HIScrollBarTrackInfo scrollInfo = new HIScrollBarTrackInfo();
	scrollInfo.pressState = (byte)state;
	scrollInfo.enableState = (byte)enableState;
	scrollInfo.viewsize = thumb;
	return scrollInfo;
}

HIThemeTrackDrawInfo getInfo() {
	int upArrowState = this.state[DrawData.SCROLLBAR_UP_ARROW];
	int downArrowState = this.state[DrawData.SCROLLBAR_DOWN_ARROW];
	int upTrackState = this.state[DrawData.SCROLLBAR_UP_TRACK];
	int downTrackState = this.state[DrawData.SCROLLBAR_DOWN_TRACK];
	int thumbState = this.state[DrawData.SCROLLBAR_THUMB];
	HIThemeTrackDrawInfo info = new HIThemeTrackDrawInfo();
	info.min = minimum;
	info.max = Math.max(minimum, maximum - thumb);
	info.value = selection;	
	info.kind = OS.kThemeScrollBarMedium;
	info.attributes = OS.kThemeTrackShowThumb;
	if ((style & SWT.HORIZONTAL) != 0) info.attributes |= OS.kThemeTrackHorizontal;
	info.enableState = OS.kThemeTrackInactive;
	if ((this.state[DrawData.WIDGET_WHOLE] & DrawData.ACTIVE) != 0) info.enableState = OS.kThemeTrackActive;
	if ((this.state[DrawData.WIDGET_WHOLE] & DrawData.DISABLED) != 0) info.enableState = OS.kThemeTrackDisabled;
	info.scrollbar = new ScrollBarTrackInfo();
	int state = 0;
	if ((upArrowState & DrawData.PRESSED) != 0) state |= OS.kThemeLeftOutsideArrowPressed;
	if ((downArrowState & DrawData.PRESSED) != 0) state |= OS.kThemeRightOutsideArrowPressed;
	if ((upTrackState & DrawData.PRESSED) != 0) state |= OS.kThemeLeftInsideArrowPressed;
	if ((downTrackState & DrawData.PRESSED) != 0) state |= OS.kThemeRightInsideArrowPressed;
	if ((thumbState & DrawData.PRESSED) != 0) state |= OS.kThemeThumbPressed;
	info.scrollbar.pressState = (byte)state;
	info.scrollbar.viewsize = thumb;
	return info;
}


Rectangle getBounds(int part, Rectangle bounds) {
	HIThemeTrackDrawInfo info = getInfo();
	info.bounds_x = bounds.x;
	info.bounds_y = bounds.y;
	info.bounds_width = bounds.width;
	info.bounds_height = bounds.height;
	CGRect rect = new CGRect();
	short partCode = 0;
	switch (part) {
		case DrawData.SCROLLBAR_UP_ARROW: partCode = 20; break;
		case DrawData.SCROLLBAR_DOWN_ARROW: partCode = 21; break;
		case DrawData.SCROLLBAR_UP_TRACK: partCode = 22; break;
		case DrawData.SCROLLBAR_THUMB: partCode = 129; break;
		case DrawData.SCROLLBAR_DOWN_TRACK: partCode = 23; break;
	}
	OS.HIThemeGetTrackPartBounds(info, partCode, rect);
	return new Rectangle((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
}

int getSelection (Point position, Rectangle bounds) {
	HIThemeTrackDrawInfo info = getInfo();
	info.bounds_x = bounds.x;
	info.bounds_y = bounds.y;
	info.bounds_width = bounds.width;
	info.bounds_height = bounds.height;
	CGPoint pt = new CGPoint();
	pt.x = position.x;
	pt.y = position.y;
	float[] relativePosition = new float[1];
	OS.HIThemeGetTrackThumbPositionFromOffset(info, pt, relativePosition);
	int[] selection = new int[1];
	OS.HIThemeGetTrackLiveValue(info, relativePosition[0], selection);
	return (int)selection[0];
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
	CGPoint pt = new CGPoint();
	pt.x = position.x;
	pt.y = position.y;
	CGRect rect = new CGRect();
	rect.x = bounds.x;
	rect.y = bounds.y;
	rect.width = bounds.width;
	rect.height = bounds.height;
	short[] part = new short[1];
	HIScrollBarTrackInfo scrollInfo = getArrowsInfo();
	OS.HIThemeHitTestScrollBarArrows(rect, scrollInfo, (style & SWT.HORIZONTAL) != 0, pt, null, part);
	switch (part[0]) {
		case 20: return DrawData.SCROLLBAR_UP_ARROW;
		case 21: return DrawData.SCROLLBAR_DOWN_ARROW;
	}
	HIThemeTrackDrawInfo info = getInfo();
	info.bounds_x = bounds.x;
	info.bounds_y = bounds.y;
	info.bounds_width = bounds.width;
	info.bounds_height = bounds.height;
	if (OS.HIThemeHitTestTrack(info, pt, part)) {
		switch (part[0]) {
		case 22: return DrawData.SCROLLBAR_UP_TRACK;
		case 23: return DrawData.SCROLLBAR_DOWN_TRACK;
		case 129: return DrawData.SCROLLBAR_THUMB;
		}
	}
	return DrawData.WIDGET_NOWHERE;
}

}

package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class Scrollable extends Control {
 	int scrolledHandle;
	int hScrollBar, vScrollBar;
	ScrollBar horizontalBar, verticalBar;
	
Scrollable () {
	/* Do nothing */
}

public Scrollable (Composite parent, int style) {
	super (parent, style);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	return new Rectangle (x, y, width, height);
}

ScrollBar createScrollBar (int type) {
    return new ScrollBar (this, type);
}

ScrollBar createStandardBar (int style) {
	short [] count = new short [1];
	OS.CountSubControls (handle, count);
	if (count [0] == 0) return null;
	int [] outControl = new int [1];
	int index = (style & SWT.HORIZONTAL) != 0 ? 1 : 2;
	int status = OS.GetIndexedSubControl (handle, (short)index, outControl);
	if (status != OS.noErr) return null;
	ScrollBar bar = new ScrollBar ();
	bar.parent = this;
	bar.style = style;
	bar.handle = outControl [0];
	bar.register ();
	bar.hookEvents ();
	return bar;
}

void createWidget () {
	super.createWidget ();
	if ((style & SWT.H_SCROLL) != 0) horizontalBar = createScrollBar (SWT.H_SCROLL);
	if ((style & SWT.V_SCROLL) != 0) verticalBar = createScrollBar (SWT.V_SCROLL);
}

void deregister () {
	super.deregister ();
	if (scrolledHandle != 0) WidgetTable.remove (scrolledHandle);
}

public Rectangle getClientArea () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	return new Rectangle (0, 0, rect.right - rect.left, rect.bottom - rect.top);
}

public ScrollBar getHorizontalBar () {
	checkWidget();
	return horizontalBar;
}

public ScrollBar getVerticalBar () {
	checkWidget();
	return verticalBar;
}

void hookEvents () {
	super.hookEvents ();
	if ((state & CANVAS) != 0 && scrolledHandle != 0) {
		Display display = getDisplay ();
		int controlProc = display.controlProc;
		int [] mask = new int [] {
			OS.kEventClassControl, OS.kEventControlDraw,
		};
		int controlTarget = OS.GetControlEventTarget (scrolledHandle);
		OS.InstallEventHandler (controlTarget, controlProc, mask.length / 2, mask, scrolledHandle, null);
	}
}

void layoutControl () {
	if ((state & CANVAS) != 0  && (horizontalBar != null || verticalBar != null)) {
		int vWidth = 0, hHeight = 0;
		if (horizontalBar != null && horizontalBar.getVisible ()) {
			Point size = horizontalBar.computeSize (SWT.DEFAULT, SWT.DEFAULT, false);
			hHeight = size.y;
		}
		if (verticalBar != null && verticalBar.getVisible ()) {
			Point size = verticalBar.computeSize (SWT.DEFAULT, SWT.DEFAULT, false);
			vWidth = size.x;
		}
		Rect rect = new Rect ();
		OS.GetControlBounds (scrolledHandle, rect);
		int width = Math.max (0, rect.right - rect.left - vWidth);
		int height = Math.max (0, rect.bottom - rect.top - hHeight);
		if (horizontalBar != null) {
			setBounds (horizontalBar.handle, 0, height, width, hHeight, true, true);
		}
		if (verticalBar != null) {
			setBounds (verticalBar.handle, width, 0, vWidth, height, true, true);
		}
		setBounds (handle, 0, 0, width, height, true, true);
	}	
}

void register () {
	super.register ();
	if (scrolledHandle != 0) WidgetTable.put (scrolledHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	scrolledHandle = 0;
}

void releaseWidget () {
	if (horizontalBar != null) horizontalBar.releaseResources ();
	if (verticalBar != null) verticalBar.releaseResources ();
	horizontalBar = verticalBar = null;
	super.releaseWidget ();
}

int setBounds (int control, int x, int y, int width, int height, boolean move, boolean resize) {
	int result = super.setBounds(control, x, y, width, height, move, resize);
	if (control == scrolledHandle) layoutControl ();
	return result;
}

int topHandle () {
	if (scrolledHandle != 0) return scrolledHandle;
	return handle;
}

}

package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;

public class ProgressBar extends Control {
	
public ProgressBar (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricNormalProgressBarThickness, outMetric);
	int width = 0, height = 0;
	if ((style & SWT.HORIZONTAL) != 0) {
		height = outMetric [0];
		width = height * 10;
	} else {
		width = outMetric [0];
		height = width * 10;
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createHandle () {
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	OS.CreateProgressBarControl (window, null, 0, 0, 100, (style & SWT.INDETERMINATE) != 0, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
}

public int getMaximum () {
	checkWidget();
    return OS.GetControl32BitMaximum (handle);
}

public int getMinimum () {
	checkWidget();
    return OS.GetControl32BitMinimum (handle);
}

public int getSelection () {
	checkWidget();
    return OS.GetControl32BitValue (handle);
}

public void setMaximum (int value) {
	checkWidget();
	if (value < 0) return;
    OS.SetControl32BitMaximum (handle, value);
}

public void setMinimum (int value) {
	checkWidget();
	if (value < 0) return;
    OS.SetControl32BitMinimum (handle, value);
}

public void setSelection (int value) {
	checkWidget();
	if (value < 0) return;
    OS.SetControl32BitValue (handle, value);
}

}

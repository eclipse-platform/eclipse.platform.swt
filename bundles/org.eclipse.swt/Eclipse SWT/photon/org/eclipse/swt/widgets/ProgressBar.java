package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class ProgressBar extends Control {

public ProgressBar (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

/*
* Not done - check Windows
*/
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = wHint, height = hHint;
	if ((style & SWT.HORIZONTAL) != 0) {
		if (width == SWT.DEFAULT) {
			width = 64;
//			width = getMaximum() - getMinimum() + 1;
		}
		if (height == SWT.DEFAULT) height = 15;
	} else {
		if (height == SWT.DEFAULT) {
			height = 64;
//			height = getMaximum() - getMinimum() + 1;
		}
		if (width == SWT.DEFAULT) width = 15;
	}
	PhRect_t rect = new PhRect_t ();
	PhArea_t area = new PhArea_t ();
	rect.lr_x = (short) (width - 1);
	rect.lr_y = (short) (height - 1);
	OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
	width = area.size_w;
	height = area.size_h;
	return new Point (width, height);
}

void createHandle (int index) {
	state |= HANDLE;
	Display display = getDisplay ();
	int clazz = display.PtProgress;
	int parentHandle = parent.handle;
	int [] args = {
//		OS.Pt_ARG_GAUGE_FLAGS, OS.Pt_GAUGE_LIVE, OS.Pt_GAUGE_LIVE,
		OS.Pt_ARG_ORIENTATION, (style & SWT.HORIZONTAL) != 0 ? OS.Pt_HORIZONTAL : OS.Pt_VERTICAL, 0,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

int processPaint (int damage) {
	OS.PtSuperClassDraw (OS.PtProgress (), handle, damage);
	return super.processPaint (damage);
}

public int getMaximum () {
	checkWidget();
	int [] args = {OS.Pt_ARG_MAXIMUM, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public int getMinimum () {
	checkWidget();
	int [] args = {OS.Pt_ARG_MINIMUM, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public int getSelection () {
	checkWidget();
	int [] args = {OS.Pt_ARG_GAUGE_VALUE, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

public void setMaximum (int value) {
	checkWidget();
	int minimum = getMinimum();
	if (0 <= minimum && minimum < value) {
		int [] args = {OS.Pt_ARG_MAXIMUM, value, 0};
		OS.PtSetResources (handle, args.length / 3, args);
	}
}

public void setMinimum (int value) {
	checkWidget();
	int maximum = getMaximum();
	if (0 <= value && value < maximum) {
		int [] args = {OS.Pt_ARG_MINIMUM, value, 0};
		OS.PtSetResources (handle, args.length / 3, args);
	}
}

public void setSelection (int value) {
	checkWidget();
	if (value < 0) return;
	int [] args = {OS.Pt_ARG_GAUGE_VALUE, value, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}
}

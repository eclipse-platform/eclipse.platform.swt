package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;

public /*final*/ class ScrollBar extends Widget {
	Scrollable parent;
	private int increment, pageIncrement;
	
ScrollBar () {
	/* Do Nothing */
}
ScrollBar (Scrollable parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
}

public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

void createHandle (int index) {
}

public Display getDisplay () {
	Scrollable parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public boolean getEnabled () {
	checkWidget();
	return false;
}

public int getIncrement () {
	checkWidget();
    return increment;
}

public int getMaximum () {
	checkWidget();
    return 0;
}

public int getMinimum () {
	checkWidget();
    return 0;
}

public int getPageIncrement () {
	checkWidget();
    return pageIncrement;
}

public Scrollable getParent () {
	checkWidget();
	return parent;
}

public int getSelection () {
	checkWidget();
	return 0;
}

public Point getSize () {
	checkWidget();
	return new Point(0, 0);
}

public int getThumb () {
	checkWidget();
    return 0;
}

public boolean getVisible () {
	checkWidget();
	return false;
}

public boolean isEnabled () {
	checkWidget();
	return false;
}

public boolean isVisible () {
	checkWidget();
	return false;
}

void releaseChild () {
	super.releaseChild ();
	if (parent.horizontalBar == this) parent.horizontalBar = null;
	if (parent.verticalBar == this) parent.verticalBar = null;
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
}

public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}

public void setEnabled (boolean enabled) {
	checkWidget();
}

public void setIncrement (int value) {
	checkWidget();
	if (value < 1) return;

}

public void setMaximum (int value) {
	checkWidget();
}

public void setMinimum (int value) {
	checkWidget();
}

public void setPageIncrement (int value) {
	checkWidget();
}

public void setSelection (int selection) {
	checkWidget();
}

public void setThumb (int value) {
	checkWidget();
}

public void setValues (int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement) {
	checkWidget();
}

public void setVisible (boolean visible) {
	checkWidget();
}
}

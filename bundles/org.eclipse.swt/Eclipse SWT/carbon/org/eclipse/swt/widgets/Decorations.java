package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;


public class Decorations extends Canvas {
	Image image;
	Control savedFocus;
	Button defaultButton, saveDefault;
	
Decorations () {
	/* Do nothing */
}

public Decorations (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	if ((style & (SWT.MENU | SWT.MIN | SWT.MAX | SWT.CLOSE)) != 0) {
		style |= SWT.TITLE;
	}
	return style;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

Control computeTabGroup () {
	return this;
}

Control computeTabRoot () {
	return this;
}

void createHandle (int index) {
	state |= HANDLE | CANVAS;
}

public Button getDefaultButton () {
	checkWidget();
	return defaultButton;
}

public Image getImage () {
	checkWidget();
	return image;
}

public boolean getMaximized () {
	checkWidget();
	return false;
}

public boolean getMinimized () {
	checkWidget();
	return false;
}

String getNameText () {
	return getText ();
}

public String getText () {
	checkWidget();
	return "";
}
boolean isTabGroup () {
	return true;
}
boolean isTabItem () {
	return false;
}

void releaseWidget () {
	super.releaseWidget ();
	defaultButton = saveDefault = null;
}

boolean restoreFocus () {
	if (savedFocus != null && savedFocus.isDisposed ()) savedFocus = null;
	if (savedFocus == null) return false;
	return savedFocus.forceFocus ();
}

void saveFocus () {
	Control control = getDisplay ().getFocusControl ();
	if (control != null) setSavedFocus (control);
}

public void setDefaultButton (Button button) {
	checkWidget();
	setDefaultButton (button, true);
}

void setDefaultButton (Button button, boolean save) {
}

public void setImage (Image image) {
	checkWidget();
	if (image != null) {
		if (image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.image = image;
}

public void setMaximized (boolean maximized) {
	checkWidget();
}

public void setMinimized (boolean minimized) {
	checkWidget();
}

void setSavedFocus (Control control) {
	if (this == control) return;
	savedFocus = control;
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
}

public void setVisible (boolean visible) {
	super.setVisible (visible);
}
}

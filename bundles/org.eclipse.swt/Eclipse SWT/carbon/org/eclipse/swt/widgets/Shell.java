package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public /*final*/ class Shell extends Decorations {
	Display display;
	int shellHandle;

public Shell () {
	this ((Display) null);
}

public Shell (int style) {
	this ((Display) null, style);
}

public Shell (Display display) {
	this (display, SWT.SHELL_TRIM);
}

public Shell (Display display, int style) {
	this (display, null, style, 0);
}

Shell (Display display, Shell parent, int style, int handle) {
	super ();
	
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.style = checkStyle (style);
	this.parent = parent;
	this.display = display;
	this.handle = handle;
	createWidget (0);
}

public Shell (Shell parent) {
	this (parent, SWT.DIALOG_TRIM);
}

public Shell (Shell parent, int style) {
	this (parent != null ? parent.getDisplay () : null, parent, style, 0);
}

static int checkStyle (int style) {
	style = Decorations.checkStyle (style);
	int mask = SWT.SYSTEM_MODAL | SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL;
	int bits = style & ~mask;
	if ((style & SWT.SYSTEM_MODAL) != 0) return bits | SWT.SYSTEM_MODAL;
	if ((style & SWT.APPLICATION_MODAL) != 0) return bits | SWT.APPLICATION_MODAL;
	if ((style & SWT.PRIMARY_MODAL) != 0) return bits | SWT.PRIMARY_MODAL;
	return bits;
}

public void addShellListener(ShellListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.Activate,typedListener);
	addListener(SWT.Close,typedListener);
	addListener(SWT.Deactivate,typedListener);
	addListener(SWT.Iconify,typedListener);
	addListener(SWT.Deiconify,typedListener);
}

public void close () {
	checkWidget();
}

void createHandle (int index) {
	state |= HANDLE | CANVAS;
}
void deregister () {
	super.deregister ();
	WidgetTable.remove (shellHandle);
}

public void dispose () {
	if (isDisposed()) return;
	super.dispose ();
}

public void forceActive () {
	checkWidget ();
}

public int getBorderWidth () {
	checkWidget();
    return 0;
}
public Rectangle getBounds () {
	checkWidget();
	return new Rectangle (0, 0, 0, 0);
}
public Display getDisplay () {
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return display;
}

public boolean getEnabled () {
	checkWidget();
	return false;
}

public Point getLocation () {
	checkWidget();
	return new Point(0, 0);
}

public Shell getShell () {
	checkWidget();
	return this;
}

public Shell [] getShells () {
	checkWidget();
	int count = 0;
	Shell [] shells = display.getShells ();
	for (int i=0; i<shells.length; i++) {
		Control shell = shells [i];
		do {
			shell = shell.parent;
		} while (shell != null && shell != this);
		if (shell == this) count++;
	}
	int index = 0;
	Shell [] result = new Shell [count];
	for (int i=0; i<shells.length; i++) {
		Control shell = shells [i];
		do {
			shell = shell.parent;
		} while (shell != null && shell != this);
		if (shell == this) {
			result [index++] = shells [i];
		}
	}
	return result;
}
public Point getSize () {
	checkWidget();
	return new Point (0, 0);
}

public boolean getVisible () {
	checkWidget();
    return false;
}

public boolean isEnabled () {
	checkWidget();
	return getEnabled ();
}

public boolean isVisible () {
	checkWidget();
	return getVisible ();
}

public void open () {
	checkWidget();
}

public void removeShellListener(ShellListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Activate, listener);
	eventTable.unhook(SWT.Close, listener);
	eventTable.unhook(SWT.Deactivate, listener);
	eventTable.unhook(SWT.Iconify,listener);
	eventTable.unhook(SWT.Deiconify,listener);
}

public void setBounds (int x, int y, int width, int height) {
	checkWidget();
}

public void setLocation (int x, int y) {
	checkWidget();
}

public void setMinimized (boolean minimized) {
	checkWidget();
}

public void setSize (int width, int height) {
	checkWidget();
}
public void setText (String string) {
	checkWidget();
}
public void setVisible (boolean visible) {
	checkWidget();
}
}
